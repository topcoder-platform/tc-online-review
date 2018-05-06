/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import junit.framework.TestCase;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UnitTestHelper;
import com.cronos.onlinereview.external.accuracytests.AccuracyHelper;
import com.topcoder.db.connectionfactory.DBConnectionFactory;


/**
 * <p>
 * Tests the BaseDBRetrieval class.
 * </p>
 *
 * @author lyt, restarter
 * @version 1.1
 */
public abstract class BaseDBRetrievalAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "Accuracy/Config.xml";

    /**
     * <p>
     * The name of the namespace that the calling program can populate which contains DBConnectionFactory and other
     * configuration values.
     * </p>
     */
    protected static final String NAMESPACE = "com.cronos.onlinereview.external";

    /**
     * <p>
     * The default ConnName which is defined in the configure file.
     * </p>
     */
    protected static final String DEFAULT_CONNECTION_NAME = "UserProjectDataStoreConnection";

    /**
     * <p>
     * The default ConnName which is defined in the configure file.
     * </p>
     */
    protected static final String DEFAULT_CONN_NAME = "UserProjectDataStoreConnection";

    /**
     * <p>
     * The default DB connection factory.
     * </p>
     */
    protected DBConnectionFactory defaultConnFactory = null;

    /**
     * <p>
     * An BaseDBRetrieval instanciated by Constructor(String)
     * </p>
     */
    protected BaseDBRetrieval baseDBRetrievalByString = null;

    /**
     * <p>
     * An BaseDBRetrieval instanciated by Constructor(DBConnectionFactory connFactory, String connName).
     * </p>
     */
    protected BaseDBRetrieval baseDBRetrieval = null;

    /**
     * <p>
     * The default connection used for db operations.
     * </p>
     */
    protected Connection defaultConnection = null;

    /**
     * <p>
     * Initialization.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        AccuracyHelper.addConfig(CONFIG_FILE);

        // Inserts into the comp_catalog table.
        //UnitTestHelper.insertIntoComponentCataLog(defaultConnection);
    }

    /**
     * <p>
     * Set defaultDBRetrieval to null.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        // Cleans up the comp_catalog table.
        UnitTestHelper.cleanupTable(defaultConnection, "comp_catalog");

        // Sets connection and defaultDBRetrieval to initial values.
        defaultConnection.close();
        baseDBRetrieval = null;

        AccuracyHelper.clearConfig();
    }

    /**
     * <p>
     * Tests the accuracy of the Constructor(String).
     * </p>
     *
     * <p>
     * The BaseDBRetrieval instance should be created successfully.
     * </p>
     */
    public void testConstructor_String() {
        assertTrue("defaultDBRetrieval should be instance of BaseDBRetrieval.",
            baseDBRetrievalByString instanceof BaseDBRetrieval);

        assertTrue("defaultDBRetrieval should be instance of BaseDBRetrieval.",
            baseDBRetrievalByString instanceof BaseDBRetrieval);

        assertNotNull("connFactory should be set correctly.",
            AccuracyHelper.getPrivateField(BaseDBRetrieval.class, baseDBRetrievalByString, "connFactory").toString());
        assertEquals("connName should be set correctly.", DEFAULT_CONNECTION_NAME,
            AccuracyHelper.getPrivateField(BaseDBRetrieval.class, baseDBRetrievalByString, "connName"));
    }

    /**
     * <p>
     * Tests the accuracy of the Constructor(DBConnectionFactory, String).
     * </p>
     *
     * @throws ConfigException this exception would never be thrown in this test case.
     */
    public void testConstructor_DBConnectionFactoryString_Accuracy()
        throws ConfigException {
        assertTrue("defaultDBRetrieval should be instance of BaseDBRetrieval.",
            baseDBRetrieval instanceof BaseDBRetrieval);

        assertEquals("connFactory should be set correctly.", defaultConnFactory,
            AccuracyHelper.getPrivateField(BaseDBRetrieval.class, baseDBRetrieval, "connFactory"));
        assertEquals("connName should be set correctly.", DEFAULT_CONNECTION_NAME,
            AccuracyHelper.getPrivateField(BaseDBRetrieval.class, baseDBRetrieval, "connName"));
    }

    /**
     * <p>
     * Tests the accuracy of the getConnection().
     * </p>
     *
     * @throws RetrievalException if a connection could not be created, this exception would never be thrown in this
     *         test case.
     */
    public void testGetConnection_Accuracy() throws RetrievalException, SQLException {
        Connection conn = baseDBRetrieval.getConnection();

        try {
            assertFalse("Connection should be accurately got.", conn.isClosed());
            conn.close();
        } catch (SQLException e) {
            // Will never happen.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the close(PreparedStatement, Connection).
     * </p>
     *
     * @throws RetrievalException if an exception occurred while closing any of the parameters, this exception would
     *         never be thrown in this test case.
     * @throws SQLException this exception would never be thrown in this test case.
     */
    public void testClose_Accuracy() throws RetrievalException, SQLException {
        // Creates the Connection and PreparedStatement.
        Connection conn = baseDBRetrieval.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from user");

        // Closes them.
        baseDBRetrieval.close(ps, conn);

        assertTrue("Connection should be accurately closed.", conn.isClosed());
    }

    /**
     * <p>
     * Tests the accuracy of the close(PreparedStatement, Connection).
     * </p>
     *
     * @throws SQLException this exception would never be thrown in this test case.
     * @throws RetrievalException to junit
     */
    public void testClose_ConnAlreadyClosed() throws SQLException, RetrievalException {
        // Creates the Connection and PreparedStatement.
        Connection conn = null;
        PreparedStatement ps = null;

        conn = baseDBRetrieval.getConnection();
        ps = conn.prepareStatement("select * from user");

        // Closes the Connection first.
        ps.close();

        // Closes them.
        baseDBRetrieval.close(ps, conn);

        // The connection should be closed too
        assertTrue("The PreparedStatement should be closed.", conn.isClosed());
    }

    /**
     * <p>
     * Tests the accuracy of the close(PreparedStatement, Connection).
     * </p>
     *
     * @throws SQLException this exception would never be thrown in this test case.
     * @throws RetrievalException to junit
     */
    public void testClose_AllowNull() throws RetrievalException {
        baseDBRetrieval.close(null, null);

        // Success
    }

    //
    //    /**
    //     * <p>
    //     * Tests the accuracy of the retrieveObjects(PreparedStatement).
    //     * </p>
    //     *
    //     * @throws RetrievalException if an exception occurred while closing any of the parameters, this exception would
    //     *         never be thrown in this test case.
    //     * @throws SQLException this exception would never be thrown in this test case.
    //     */
    //    public void testRetrieveObjects_PreparedStatement()
    //        throws RetrievalException, SQLException {
    //        // Creates the Connection and PreparedStatement.
    //        Connection conn = defaultDBRetrieval.getConnection();
    //        PreparedStatement ps = conn.prepareStatement("select * from comp_catalog");
    //
    //        // Retrieves Objects.
    //        Map objects = defaultDBRetrieval.retrieveObjects(ps);
    //
    //        assertEquals("There should be 4 items in the Map.", 4, objects.size());
    //    }
}
