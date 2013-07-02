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

package org.apache.logging.log4j.core.appender.rolling;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;

/**
 * Tests the FastRollingFileManager class.
 */
public class FastRollingFileManagerTest {

    /**
     * Test method for
     * {@link org.apache.logging.log4j.core.appender.rolling.FastRollingFileManager#write(byte[], int, int)}
     * .
     */
    @Test
    public void testWrite_multiplesOfBufferSize() throws IOException {
        File file = File.createTempFile("log4j2", "test");
        file.deleteOnExit();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        OutputStream os = new FastRollingFileManager.DummyOutputStream();
        boolean append = false;
        boolean flushNow = false;
        long triggerSize = Long.MAX_VALUE;
        long time = System.currentTimeMillis();
        TriggeringPolicy triggerPolicy = new SizeBasedTriggeringPolicy(
                triggerSize);
        RolloverStrategy rolloverStrategy = null;
        FastRollingFileManager manager = new FastRollingFileManager(raf,
                file.getName(), "", os, append, flushNow, triggerSize, time,
                triggerPolicy, rolloverStrategy, null, null);

        int size = FastRollingFileManager.DEFAULT_BUFFER_SIZE * 3;
        byte[] data = new byte[size];
        manager.write(data, 0, data.length); // no buffer overflow exception

        // buffer is full but not flushed yet
        assertEquals(FastRollingFileManager.DEFAULT_BUFFER_SIZE * 2,
                raf.length());
    }

    /**
     * Test method for
     * {@link org.apache.logging.log4j.core.appender.rolling.FastRollingFileManager#write(byte[], int, int)}
     * .
     */
    @Test
    public void testWrite_dataExceedingBufferSize() throws IOException {
        File file = File.createTempFile("log4j2", "test");
        file.deleteOnExit();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        OutputStream os = new FastRollingFileManager.DummyOutputStream();
        boolean append = false;
        boolean flushNow = false;
        long triggerSize = 0;
        long time = System.currentTimeMillis();
        TriggeringPolicy triggerPolicy = new SizeBasedTriggeringPolicy(
                triggerSize);
        RolloverStrategy rolloverStrategy = null;
        FastRollingFileManager manager = new FastRollingFileManager(raf,
                file.getName(), "", os, append, flushNow, triggerSize, time,
                triggerPolicy, rolloverStrategy, null, null);

        int size = FastRollingFileManager.DEFAULT_BUFFER_SIZE * 3 + 1;
        byte[] data = new byte[size];
        manager.write(data, 0, data.length); // no exception
        assertEquals(FastRollingFileManager.DEFAULT_BUFFER_SIZE * 3,
                raf.length());

        manager.flush();
        assertEquals(size, raf.length()); // all data written to file now
    }

    @Test
    public void testAppendDoesNotOverwriteExistingFile() throws IOException {
        boolean isAppend = true;
        File file = File.createTempFile("log4j2", "test");
        file.deleteOnExit();
        assertEquals(0, file.length());

        byte[] bytes = new byte[4 * 1024];

        // create existing file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
        } finally {
            fos.close();
        }
        assertEquals("all flushed to disk", bytes.length, file.length());

        FastRollingFileManager manager = FastRollingFileManager
                .getFastRollingFileManager(
                        //
                        file.getAbsolutePath(), "", isAppend, true,
                        new SizeBasedTriggeringPolicy(Long.MAX_VALUE), //
                        null, null, null);
        manager.write(bytes, 0, bytes.length);
        int expected = bytes.length * 2;
        assertEquals("appended, not overwritten", expected, file.length());
    }

    @Test
    public void testFileTimeBasedOnSystemClockWhenAppendIsFalse()
            throws IOException {
        File file = File.createTempFile("log4j2", "test");
        file.deleteOnExit();
        LockSupport.parkNanos(1000000); // 1 millisec

        // append is false deletes the file if it exists
        boolean isAppend = false;
        long expectedMin = System.currentTimeMillis();
        long expectedMax = expectedMin + 50;
        assertTrue(file.lastModified() < expectedMin);
        
        FastRollingFileManager manager = FastRollingFileManager
                .getFastRollingFileManager(
                        //
                        file.getAbsolutePath(), "", isAppend, true,
                        new SizeBasedTriggeringPolicy(Long.MAX_VALUE), //
                        null, null, null);
        assertTrue(manager.getFileTime() < expectedMax);
        assertTrue(manager.getFileTime() >= expectedMin);
    }

    @Test
    public void testFileTimeBasedOnFileModifiedTimeWhenAppendIsTrue()
            throws IOException {
        File file = File.createTempFile("log4j2", "test");
        file.deleteOnExit();
        LockSupport.parkNanos(1000000); // 1 millisec

        boolean isAppend = true;
        assertTrue(file.lastModified() < System.currentTimeMillis());
        
        FastRollingFileManager manager = FastRollingFileManager
                .getFastRollingFileManager(
                        //
                        file.getAbsolutePath(), "", isAppend, true,
                        new SizeBasedTriggeringPolicy(Long.MAX_VALUE), //
                        null, null, null);
        assertEquals(file.lastModified(), manager.getFileTime());
    }

}