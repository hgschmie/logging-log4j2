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
package org.apache.logging.log4j.core.appender.db.jpa;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.db.jpa.converter.MessageAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.ThrowableAttributeConverter;
import org.apache.logging.log4j.message.Message;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "jpaBaseLogEntry")
@SuppressWarnings("unused")
public class TestEntity extends AbstractLogEventWrapperEntity {
    private static final long serialVersionUID = 1L;

    private long id = 0L;

    public TestEntity() {
        super();
    }

    public TestEntity(final LogEvent wrappedEvent) {
        super(wrappedEvent);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "eventDate")
    public Date getEventDate() {
        return new Date(this.getMillis());
    }

    public void setEventDate(final Date date) {
        // this entity is write-only
    }

    @Override
    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    public Level getLevel() {
        return getWrappedEvent().getLevel();
    }

    @Override
    @Basic
    @Column(name = "logger")
    public String getLoggerName() {
        return getWrappedEvent().getLoggerName();
    }

    @Override
    @Transient
    public StackTraceElement getSource() {
        return getWrappedEvent().getSource();
    }

    @Override
    @Convert(converter = MessageAttributeConverter.class)
    public Message getMessage() {
        return getWrappedEvent().getMessage();
    }

    @Override
    @Transient
    public Marker getMarker() {
        return getWrappedEvent().getMarker();
    }

    @Override
    @Transient
    public String getThreadName() {
        return getWrappedEvent().getThreadName();
    }

    @Override
    @Transient
    public long getMillis() {
        return getWrappedEvent().getMillis();
    }

    @Override
    @Convert(converter = ThrowableAttributeConverter.class)
    @Column(name = "exception")
    public Throwable getThrown() {
        return getWrappedEvent().getThrown();
    }

    @Override
    @Transient
    public Map<String, String> getContextMap() {
        return getWrappedEvent().getContextMap();
    }

    @Override
    @Transient
    public ThreadContext.ContextStack getContextStack() {
        return getWrappedEvent().getContextStack();
    }

    @Override
    @Transient
    public String getFQCN() {
        return getWrappedEvent().getFQCN();
    }
}
