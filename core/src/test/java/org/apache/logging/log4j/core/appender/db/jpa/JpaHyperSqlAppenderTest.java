package org.apache.logging.log4j.core.appender.db.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.core.LogEvent;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class JpaHyperSqlAppenderTest extends AbstractJpaAppenderTest {
    private static final String USER_ID = "sa";
    private static final String PASSWORD = "";

    public JpaHyperSqlAppenderTest() {
        super("hsqldb");
    }

    @Override
    protected Connection setUpConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:Log4j", USER_ID, PASSWORD);

        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE jpaBaseLogEntry ( " +
                "id INTEGER IDENTITY, eventDate DATETIME, level VARCHAR(10), logger VARCHAR(255), " +
                "message VARCHAR(1024), exception VARCHAR(1048576)" +
                " )");
        statement.close();

        statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE jpaBasicLogEntry ( " +
                "id INTEGER IDENTITY, millis BIGINT, level VARCHAR(10), loggerName VARCHAR(255), " +
                "message VARCHAR(1024), thrown VARCHAR(1048576), contextMapJson VARCHAR(1048576)," +
                "fqcn VARCHAR(1024), contextStack VARCHAR(1048576), marker VARCHAR(255), source VARCHAR(2048)," +
                "threadName VARCHAR(255)" +
                " )");
        statement.close();

        return connection;
    }

    @Test
    public void testNoEntityClassName() {
        final JPAAppender appender = JPAAppender.createAppender("name", null, null, null, null, "jpaAppenderTestUnit");

        assertNull("The appender should be null.", appender);
    }

    @Test
    public void testNoPersistenceUnitName() {
        final JPAAppender appender = JPAAppender.createAppender("name", null, null, null, TestBaseEntity.class.getName(),
                null);

        assertNull("The appender should be null.", appender);
    }

    @Test
    public void testBadEntityClassName() {
        final JPAAppender appender = JPAAppender.createAppender("name", null, null, null, "com.foo.Bar",
                "jpaAppenderTestUnit");

        assertNull("The appender should be null.", appender);
    }

    @Test
    public void testNonLogEventEntity() {
        final JPAAppender appender = JPAAppender.createAppender("name", null, null, null, Object.class.getName(),
                "jpaAppenderTestUnit");

        assertNull("The appender should be null.", appender);
    }

    @Test
    public void testBadConstructorEntity01() {
        final JPAAppender appender = JPAAppender.createAppender("name", null, null, null,
                BadConstructorEntity1.class.getName(), "jpaAppenderTestUnit");

        assertNull("The appender should be null.", appender);
    }

    @Test
    public void testBadConstructorEntity02() {
        final JPAAppender appender = JPAAppender.createAppender("name", null, null, null,
                BadConstructorEntity2.class.getName(), "jpaAppenderTestUnit");

        assertNull("The appender should be null.", appender);
    }

    @SuppressWarnings("unused")
    public static class BadConstructorEntity1 extends TestBaseEntity {
        private static final long serialVersionUID = 1L;

        public BadConstructorEntity1(final LogEvent wrappedEvent) {
            super(wrappedEvent);
        }
    }

    @SuppressWarnings("unused")
    public static class BadConstructorEntity2 extends TestBaseEntity {
        private static final long serialVersionUID = 1L;

        public BadConstructorEntity2() {
            super(null);
        }

        public BadConstructorEntity2(final LogEvent wrappedEvent, final String badParameter) {
            super(wrappedEvent);
        }
    }
}