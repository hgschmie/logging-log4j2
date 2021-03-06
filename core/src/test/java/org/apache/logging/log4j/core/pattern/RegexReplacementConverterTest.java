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
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class RegexReplacementConverterTest {

    private static final String LINE_SEP = System.getProperty("line.separator");
    @Test
    public void testReplacement() {
        ThreadContext.put("MyKey", "Apache");
        LogEvent event = new Log4jLogEvent(RegexReplacementConverterTest.class.getName(), null, null,
            Level.DEBUG, new SimpleMessage("This is a test"), null);
        StringBuilder sb = new StringBuilder();
        LoggerContext ctx = (LoggerContext) LogManager.getContext();
        String[] options = new String[] {
            "%logger %msg%n", "\\.", "/"
        };
        RegexReplacementConverter converter = RegexReplacementConverter.newInstance(ctx.getConfiguration(),
            options);
        converter.format(event, sb);
        assertEquals("org/apache/logging/log4j/core/pattern/RegexReplacementConverterTest This is a test" + LINE_SEP,
            sb.toString());
    }
}
