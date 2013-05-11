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
package org.apache.logging.log4j.core.appender.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * An {@link AbstractDatabaseManager} implementation for relational databases accessed via JDBC.
 */
public final class JDBCDatabaseManager extends AbstractDatabaseManager {
    private static final class Column {
        private final boolean isEventTimestamp;
        private final PatternLayout layout;

        private Column(final PatternLayout layout, final boolean isEventDate) {
            this.layout = layout;
            this.isEventTimestamp = isEventDate;
        }
    }

    private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {
        private final ColumnConfig[] columnConfigs;
        private final ConnectionSource connectionSource;
        private final String tableName;

        protected FactoryData(final int bufferSize, final ConnectionSource connectionSource, final String tableName,
                final ColumnConfig[] columnConfigs) {
            super(bufferSize);
            this.connectionSource = connectionSource;
            this.tableName = tableName;
            this.columnConfigs = columnConfigs;
        }
    }

    private static final class JDBCDatabaseManagerFactory implements ManagerFactory<JDBCDatabaseManager, FactoryData> {
        @Override
        public JDBCDatabaseManager createManager(final String name, final FactoryData data) {
            final StringBuilder columnPart = new StringBuilder();
            final StringBuilder valuePart = new StringBuilder();
            final List<Column> columns = new ArrayList<Column>();
            int i = 0;
            for (final ColumnConfig config : data.columnConfigs) {
                if (i++ > 0) {
                    columnPart.append(',');
                    valuePart.append(',');
                }

                columnPart.append(config.getColumnName());

                if (config.getLiteralValue() != null) {
                    valuePart.append(config.getLiteralValue());
                } else {
                    columns.add(new Column(config.getLayout(), config.isEventTimestamp()));
                    valuePart.append('?');
                }
            }

            final String sqlStatement = "INSERT INTO " + data.tableName + " (" + columnPart + ") VALUES (" + valuePart
                    + ")";

            return new JDBCDatabaseManager(name, data.bufferSize, data.connectionSource, sqlStatement, columns);
        }
    }

    private static final JDBCDatabaseManagerFactory FACTORY = new JDBCDatabaseManagerFactory();

    /**
     * Creates a JDBC manager for use within the {@link JDBCAppender}, or returns a suitable one if it already exists.
     * 
     * @param name
     *            The name of the manager, which should include connection details and hashed passwords where possible.
     * @param bufferSize
     *            The size of the log event buffer.
     * @param connectionSource
     *            The source for connections to the database.
     * @param tableName
     *            The name of the database table to insert log events into.
     * @param columnConfigs
     *            Configuration information about the log table columns.
     * @return a new or existing JDBC manager as applicable.
     */
    public static JDBCDatabaseManager getJDBCDatabaseManager(final String name, final int bufferSize,
            final ConnectionSource connectionSource, final String tableName, final ColumnConfig[] columnConfigs) {

        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, connectionSource, tableName,
                columnConfigs), FACTORY);
    }

    private final List<Column> columns;

    private Connection connection;

    private final ConnectionSource connectionSource;

    private final String sqlStatement;

    private PreparedStatement statement;

    private JDBCDatabaseManager(final String name, final int bufferSize, final ConnectionSource connectionSource,
            final String sqlStatement, final List<Column> columns) {
        super(name, bufferSize);
        this.connectionSource = connectionSource;
        this.sqlStatement = sqlStatement;
        this.columns = columns;
    }

    @Override
    protected void connectInternal() {
        try {
            this.connection = this.connectionSource.getConnection();
            this.statement = this.connection.prepareStatement(this.sqlStatement);
        } catch (final SQLException e) {
            LOGGER.error("Failed to connect to relational database using JDBC connection source [{}] in manager [{}].",
                    this.connectionSource, this.getName(), e);
        }
    }

    @Override
    protected void disconnectInternal() {
        try {
            if (this.statement != null && !this.statement.isClosed()) {
                this.statement.close();
            }
        } catch (final SQLException e) {
            LOGGER.warn("Error while closing prepared statement in database manager [{}].", this.getName(), e);
        }

        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (final SQLException e) {
            LOGGER.warn("Error while disconnecting from relational database in manager [{}].", this.getName(), e);
        }
    }

    @Override
    protected void writeInternal(final LogEvent event) {
        try {
            if (!this.isConnected() || this.connection == null || this.connection.isClosed()) {
                LOGGER.error("Cannot write logging event; manager [{}] not connected to the database.", this.getName());
                return;
            }

            int i = 1;
            for (final Column column : this.columns) {
                if (column.isEventTimestamp) {
                    this.statement.setTimestamp(i++, new Timestamp(event.getMillis()));
                } else {
                    this.statement.setString(i++, column.layout.toSerializable(event));
                }
            }

            if (this.statement.executeUpdate() == 0) {
                LOGGER.warn("No records inserted in database table for log event in manager [{}].", this.getName());
            }
        } catch (final SQLException e) {
            LOGGER.error("Failed to insert record for log event in manager [{}].", this.getName(), e);
        }
    }
}