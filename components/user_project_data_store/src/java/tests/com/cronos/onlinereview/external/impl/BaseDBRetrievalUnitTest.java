/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UnitTestHelper;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the BaseDBRetrieval class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class BaseDBRetrievalUnitTest extends TestCase {

    /**
     * <p>
     * Represents the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "SampleConfig.xml";

    /**
     * <p>
     * The name of the namespace that the calling program can populate which contains DBConnectionFactory and other
     * configuration values.
     * </p>
     */
    private static final String NAMESPACE = "com.cronos.onlinereview.external";

    /**
     * <p>
     * The default ConnName which is defined in the configure file.
     * </p>
     */
    private static final String DEFAULTCONNNAME = "UserProjectDataStoreConnection";

    /**
     * <p>
     * The default DB connection factory.
     * </p>
     */
    private DBConnectionFactory defaultConnFactory = null;

    /**
     * <p>
     * An BaseDBRetrieval instance for testing.
     * </p>
     */
    private MockBaseDBRetrieval defaultDBRetrieval = null;

    /**
     * <p>
     * The default connection used for db operations.
     * </p>
     */
    private Connection defaultConnection = null;

    /**
     * <p>
     * Initialization.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        UnitTestHelper.addConfig(CONFIG_FILE);

        defaultDBRetrieval = new MockBaseDBRetrieval(NAMESPACE);

        // Retrieves Connection and DBConnectionFactory.
        defaultConnection = defaultDBRetrieval.getConnection();
        defaultConnFactory = new DBConnectionFactoryImpl(NAMESPACE);

        // Inserts into the comp_catalog table.
        UnitTestHelper.insertIntoComponentCataLog(defaultConnection);
    }

    /**
     * <p>
     * Set defaultDBRetrieval to null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        // Cleans up the comp_catalog table.
        UnitTestHelper.cleanupTable(defaultConnection, "comp_catalog");

        // Sets connection and defaultDBRetrieval to initial values.
        defaultConnection.close();
        defaultDBRetrieval = null;

        UnitTestHelper.clearConfig();

        super.tearDown();
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(String).
     * </p>
     * <p>
     * The BaseDBRetrieval instance should be created successfully.
     * </p>
     */
    public void testCtor_String() {
        assertNotNull("BaseDBRetrieval should be accurately created.", defaultDBRetrieval);
        assertTrue("defaultDBRetrieval should be instance of BaseDBRetrieval.",
                defaultDBRetrieval instanceof BaseDBRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory = UnitTestHelper.getPrivateField(BaseDBRetrieval.class, defaultDBRetrieval, "connFactory");
        Object connName = UnitTestHelper.getPrivateField(BaseDBRetrieval.class, defaultDBRetrieval, "connName");

        // Asserts the set.
        assertNotNull("connFactory should be set correctly.", connFactory);
        assertEquals("connName should be set correctly.", DEFAULTCONNNAME, connName);
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * If the parameter is null. Then IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws ConfigException
     *             this exception would never be thrown in this test case.
     */
    public void testCtor_String_NullNamespace() throws ConfigException {
        try {
            new MockBaseDBRetrieval(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * If the parameter is empty after trimmed. Then IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws ConfigException
     *             this exception would never be thrown in this test case.
     */
    public void testCtor_String_EmptyNamespace() throws ConfigException {
        try {
            new MockBaseDBRetrieval("  ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * The connName which is got from the configure file, is not contained in the connFactoryImpl. Then ConfigException
     * should be thrown.
     * </p>
     */
    public void testCtor_String_ConnNameNotInclude() {
        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            new MockBaseDBRetrieval("com.cronos.onlinereview.external.connNameNotInclude");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * The connName which is got from the configure file, is defined empty. Then ConfigException should be thrown.
     * </p>
     */
    public void testCtor_String_ConnNameEmpty() {
        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            new MockBaseDBRetrieval("com.cronos.onlinereview.external.connNameEmpty");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * The default connName which is got from the configure file, is missing. Then ConfigException should be thrown.
     * </p>
     */
    public void testCtor_String_DefaultConnNameUnknown() {
        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            new MockBaseDBRetrieval("com.cronos.onlinereview.external.defaultConnNameUnknown");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * If gives an unknown namespace. Then ConfigException should be thrown.
     * </p>
     */
    public void testCtor_String_UnknowNamespace() {
        try {
            new MockBaseDBRetrieval("Unknown");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(DBConnectionFactory, String).
     * </p>
     * <p>
     * The BaseDBRetrieval instance should be created successfully.
     * </p>
     *
     * @throws ConfigException
     *             this exception would never be thrown in this test case.
     */
    public void testCtor_DBConnectionFactoryString() throws ConfigException {
        defaultDBRetrieval = new MockBaseDBRetrieval(defaultConnFactory, DEFAULTCONNNAME);

        assertNotNull("BaseDBRetrieval should be accurately created.", defaultDBRetrieval);
        assertTrue("defaultDBRetrieval should be instance of BaseDBRetrieval.",
                defaultDBRetrieval instanceof BaseDBRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory = UnitTestHelper.getPrivateField(BaseDBRetrieval.class, defaultDBRetrieval, "connFactory");
        Object connName = UnitTestHelper.getPrivateField(BaseDBRetrieval.class, defaultDBRetrieval, "connName");

        // Asserts the set.
        assertNotNull("connFactory should be set correctly.", connFactory);
        assertEquals("connFactory should be set correctly.", defaultConnFactory, connFactory);
        assertEquals("connName should be set correctly.", DEFAULTCONNNAME, connName);
    }

    /**
     * <p>
     * Tests the failure of the ctor(DBConnectionFactory, String).
     * </p>
     * <p>
     * If DBConnectionFactory given is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws ConfigException
     *             this exception would never be thrown in this test case.
     */
    public void testCtor_DBConnectionFactoryString_NullDBConnectionFactory() throws ConfigException {
        try {
            new MockBaseDBRetrieval(null, DEFAULTCONNNAME);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(DBConnectionFactory, String).
     * </p>
     * <p>
     * If connName given is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws ConfigException
     *             this exception would never be thrown in this test case.
     */
    public void testCtor_DBConnectionFactoryString_NullConnName() throws ConfigException {
        try {
            new MockBaseDBRetrieval(defaultConnFactory, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(DBConnectionFactory, String).
     * </p>
     * <p>
     * If connName doesn't correspond to a connection the factory knows about, ConfigException should be thrown.
     * </p>
     */
    public void testCtor_DBConnectionFactoryString_UnknownConnName() {
        try {
            new MockBaseDBRetrieval(defaultConnFactory, "UnknownConnName");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the getConnection().
     * </p>
     *
     * @throws RetrievalException
     *             if a connection could not be created, this exception would never be thrown in this test case.
     */
    public void testGetConnection() throws RetrievalException {
        Connection conn = defaultDBRetrieval.getConnection();

        assertNotNull("Connection should be accurately got.", conn);

        try {
            conn.close();
        } catch (SQLException e) {
            // Will never happen.
        }
    }

    /**
     * <p>
     * Tests the failure of the getConnection().
     * </p>
     * <p>
     * If there is no default Connection and connName, connection could not be created, RetrievalException would be
     * thrown.
     * </p>
     */
    public void testGetConnection_NoDefaultConnectionAndConnName() {
        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            defaultDBRetrieval = new MockBaseDBRetrieval("com.cronos.onlinereview.external.NoDefaultConnAndConnName");
        } catch (ConfigException e) {
            // Will never happen.
        }

        try {
            defaultDBRetrieval.getConnection();
            fail("RetrievalException should be thrown.");
        } catch (RetrievalException e1) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the close(PreparedStatement, Connection).
     * </p>
     *
     * @throws RetrievalException
     *             if an exception occurred while closing any of the parameters, this exception would never be thrown in
     *             this test case.
     * @throws SQLException
     *             this exception would never be thrown in this test case.
     */
    public void testClose_PreparedStatementConnection() throws RetrievalException, SQLException {
        // Creates the Connection and PreparedStatement.
        Connection conn = defaultDBRetrieval.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from user");

        // Closes them.
        defaultDBRetrieval.close(ps, conn);

        assertTrue("Connection should be accurately closed.", conn.isClosed());
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveObjects(PreparedStatement).
     * </p>
     *
     * @throws RetrievalException
     *             if an exception occurred while closing any of the parameters, this exception would never be thrown in
     *             this test case.
     * @throws SQLException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveObjects_PreparedStatement() throws RetrievalException, SQLException {
        // Creates the Connection and PreparedStatement.
        Connection conn = defaultDBRetrieval.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from comp_catalog");

        // Retrieves Objects.
        Map objects = defaultDBRetrieval.retrieveObjects(ps);

        assertEquals("There should be 4 items in the Map.", 4, objects.size());
    }

    /**
     * <p>
     * Tests the retrieveObjects(PreparedStatement) method while SQLException occurs.
     * </p>
     * <p>
     * Should wrap the SQLException with RetrievalException.
     * </p>
     */
    public void testRetrieveObjects_SQLException() throws Exception {
        PreparedStatement pstmt = new MockPreparedStatement(new MockConnection(), "test") {
            public ResultSet executeQuery() throws SQLException {
                throw new SQLException("mock");
            }
        };

        try {
            defaultDBRetrieval.retrieveObjects(pstmt);
            fail("expect throw RetrievalException.");
        } catch (RetrievalException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests the close(PreparedStatement, Connection) method.
     * </p>
     * <p>
     * If there are problem while close the prepared statement, RetrievalException should be thrown.
     * </p>
     */
    public void testClose_SQLException1() {
        PreparedStatement pstmt = new MockPreparedStatement(new MockConnection(), "test") {
            public void close() throws SQLException {
                throw new SQLException("mock");
            }
        };

        try {
            defaultDBRetrieval.close(pstmt, null);
            fail("RetrievalException should be thrown.");
        } catch (RetrievalException e) {
            assertEquals("incorrect internal cause.", "mock", e.getCause().getMessage());
            // expected
        }
    }

    /**
     * <p>
     * Tests the close(PreparedStatement, Connection) method.
     * </p>
     * <p>
     * If there are problem while close the connection, RetrievalException should be thrown.
     * </p>
     */
    public void testClose_SQLException2() {
        Connection conn = new MockConnection() {
            public void close() throws SQLException {
                throw new SQLException("mock");
            }
        };

        try {
            defaultDBRetrieval.close(null, conn);
            fail("RetrievalException should be thrown.");
        } catch (RetrievalException e) {
            // expected
        }
    }
}
