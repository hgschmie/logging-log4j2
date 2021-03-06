/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.status.StatusLogger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class XMLConfigurationPropsTest {

    private static final String CONFIG = "log4j-props.xml";
    private static final String LOGFILE = "target/test.log";

    @BeforeClass
    public static void setupClass() {
    }

    @AfterClass
    public static void cleanupClass() {
        System.clearProperty(XMLConfigurationFactory.CONFIGURATION_FILE_PROPERTY);
        LoggerContext ctx = (LoggerContext) LogManager.getContext();
        ctx.reconfigure();
        StatusLogger.getLogger().reset();
    }

    @Test
    public void testNoProps() {
        System.setProperty(XMLConfigurationFactory.CONFIGURATION_FILE_PROPERTY, CONFIG);
        LoggerContext ctx = (LoggerContext) LogManager.getContext();
        ctx.reconfigure();
        Configuration config = ctx.getConfiguration();
        assertTrue("Configuration is not an XMLConfiguration", config instanceof XMLConfiguration);
    }

    @Test
    public void testWithConfigProp() {
        System.setProperty(XMLConfigurationFactory.CONFIGURATION_FILE_PROPERTY, CONFIG);
        System.setProperty("log4j.level", "debug");
        try {
            LoggerContext ctx = (LoggerContext) LogManager.getContext();
            ctx.reconfigure();
            Configuration config = ctx.getConfiguration();
            assertTrue("Configuration is not an XMLConfiguration", config instanceof XMLConfiguration);
        } finally {
            System.clearProperty("log4j.level");

        }
    }

    @Test
    public void testWithProps() {
        System.setProperty(XMLConfigurationFactory.CONFIGURATION_FILE_PROPERTY, CONFIG);
        System.setProperty("log4j.level", "debug");
        System.setProperty("log.level", "debug");
        try {
            LoggerContext ctx = (LoggerContext) LogManager.getContext();
            ctx.reconfigure();
            Configuration config = ctx.getConfiguration();
            assertTrue("Configuration is not an XMLConfiguration", config instanceof XMLConfiguration);
        } finally {
            System.clearProperty("log4j.level");
            System.clearProperty("log.level");
        }
    }
}
