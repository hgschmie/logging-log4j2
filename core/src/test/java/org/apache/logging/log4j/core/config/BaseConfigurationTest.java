package org.apache.logging.log4j.core.config;

import static org.junit.Assert.*;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.junit.Test;

public class BaseConfigurationTest {


    @Test
    public void testMissingRootLogger() throws Exception {
        PluginManager.addPackage("org.apache.logging.log4j.test.appender");
        LoggerContext ctx = Configurator.initialize("Test1", null, "missingRootLogger.xml");
        final Logger logger = LogManager.getLogger("sample.Logger1");
        Configuration config = ctx.getConfiguration();
        assertNotNull("Config not null", config);
//        final String MISSINGROOT = "MissingRootTest";
//        assertTrue("Incorrect Configuration. Expected " + MISSINGROOT + " but found " + config.getName(),
//                MISSINGROOT.equals(config.getName()));
        final Map<String, Appender<?>> map = config.getAppenders();
        assertNotNull("Appenders not null", map);
        assertEquals("Appenders Size", 2, map.size());
        assertTrue("Contains List", map.containsKey("List"));
        assertTrue("Contains Console", map.containsKey("Console"));
        
        final Map<String, LoggerConfig> loggerMap = config.getLoggers();
        assertNotNull("loggerMap not null", loggerMap);
        assertEquals("loggerMap Size", 1, loggerMap.size());
        // only the sample logger, no root logger in loggerMap!
        assertTrue("contains key=sample", loggerMap.containsKey("sample"));
        
        LoggerConfig sample = loggerMap.get("sample");
        Map<String, Appender<?>> sampleAppenders = sample.getAppenders();
        assertEquals("sampleAppenders Size", 1, sampleAppenders.size());
        // sample only has List appender, not Console!
        assertTrue("sample has appender List", sampleAppenders.containsKey("List"));
        
        BaseConfiguration baseConfig = (BaseConfiguration) config;
        LoggerConfig root = baseConfig.getRootLogger();
        Map<String, Appender<?>> rootAppenders = root.getAppenders();
        assertEquals("rootAppenders Size", 1, rootAppenders.size());
        // root only has Console appender!
        assertTrue("root has appender Console", rootAppenders.containsKey("Console"));
        assertEquals(Level.ERROR, root.getLevel());

        logger.isDebugEnabled();
        Configurator.shutdown(ctx);
    }

}