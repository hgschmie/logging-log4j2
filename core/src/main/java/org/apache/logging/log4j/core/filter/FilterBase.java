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
package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Lifecycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.message.Message;

/**
 * Users should extend this class to implement filters. Filters can be either context wide or attached to
 * an appender. A filter may choose to support being called only from the context or only from an appender in
 * which case it will only implement the required method(s). The rest will default to return NEUTRAL.
 *
 */
public abstract class FilterBase implements Filter, Lifecycle {
    /**
     * Allow subclasses access to the status logger without creating another instance.
     */
    protected static final org.apache.logging.log4j.Logger LOGGER = StatusLogger.getLogger();

    /**
     * The onMatch Result.
     */
    protected final Result onMatch;

    /**
     * The onMismatch Result.
     */
    protected final Result onMismatch;

    private boolean started;

    /**
     * The default constructor.
     */
    protected FilterBase() {
        this(null, null);
    }

    /**
     * Constructor that allows the onMatch and onMismatch actions to be set.
     * @param onMatch The result to return when a match occurs.
     * @param onMismatch The result to return when a match dos not occur.
     */
    protected FilterBase(Result onMatch, Result onMismatch) {
        this.onMatch = onMatch == null ? Result.NEUTRAL : onMatch;
        this.onMismatch = onMismatch == null ? Result.DENY : onMismatch;
    }

    /**
     * Mark the Filter as started.
     */
    public void start() {
        started = true;
    }

    /**
     * Determine if the the Filter has started.
     * @return true if the Filter is started, false otherwise.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Mark the Filter as stopped.
     */
    public void stop() {
        started = false;
    }

    /**
     * Return the Result to be returned when a match does not occur.
     * @return the onMismatch Result.
     */
    public final Result getOnMismatch() {
        return onMismatch;
    }

    /**
     * Return the Result to be returned when a match occurs.
     * @return the onMatch Result.
     */
    public final Result getOnMatch() {
        return onMatch;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * Appender Filter method. The default returns NEUTRAL.
     * @param logger the Logger.
     * @param level The logging Level.
     * @param marker The Marker, if any.
     * @param msg The message, if present.
     * @param params An array of parameters or null.
     * @return The Result of filtering.
     */
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object[] params) {
        return Result.NEUTRAL;
    }

    /**
     * Appender Filter method. The default returns NEUTRAL.
     * @param logger the Logger.
     * @param level The logging Level.
     * @param marker The Marker, if any.
     * @param msg The message, if present.
     * @param t A throwable or null.
     * @return The Result of filtering.
     */
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return Result.NEUTRAL;
    }

    /**
     * Appender Filter method. The default returns NEUTRAL.
     * @param logger the Logger.
     * @param level The logging Level.
     * @param marker The Marker, if any.
     * @param msg The message, if present.
     * @param t A throwable or null.
     * @return The Result of filtering.
     */
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return Result.NEUTRAL;
    }

    /**
     * Context Filter method. The default returns NEUTRAL.
     * @param event The LogEvent.
     * @return The Result of filtering.
     */
    public Result filter(LogEvent event) {
        return Result.NEUTRAL;
    }
}
