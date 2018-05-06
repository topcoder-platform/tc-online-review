/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.stresstests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalObject;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.impl.BaseDBRetrieval;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Stress test case of <code>BaseDBRetrieval</code> class.
 *
 * @author fairytale, victorsam
 * @version 1.1
 */
public class BaseDBRetrievalStressTest extends TestCase {
    /** The number of times each method will be run. */
    public static final int RUN_TIMES = 50;
    /** The data item count will be insert into db before test processes. */
    public static final int DATA_COUNT = 500;

    /**
     * Test suite of BaseDBRetrievalStressTest.
     *
     * @return Test suite of BaseDBRetrievalStressTest.
     */
    public static Test suite() {
        return new TestSuite(BaseDBRetrievalStressTest.class);
    }

    /**
     * Initialization for all tests here.
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        // set up for test
        Helper.loadConfig();
    }

    /**
     * <p>Clear the test environment.</p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        Helper.unloadConfig();
    }

    /**
     * <p>Stress test for BaseDBRetrieval#BaseDBRetrieval(String).</p>
     * @throws Exception to junit.
     */
    public void testCtor_namespace() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            assertNotNull("Failed to create BaseDBRetrieval.",
                    new MockBaseDBRetrieval("com.cronos.onlinereview.external.stresstests"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing BaseDBRetrieval(String) for " + RUN_TIMES + " times costs "
                + (end - start) + "ms");
    }

    /**
     * <p>Stress test for BaseDBRetrieval#BaseDBRetrieval(DBConnectionFactory, String).</p>
     * @throws Exception to junit.
     */
    public void testCtor_connFactory_connName() throws Exception {
        DBConnectionFactory factory = new DBConnectionFactoryImpl("com.cronos.onlinereview.external.stresstests");
        long start = System.currentTimeMillis();

        for (int i = 0; i < RUN_TIMES / 10; i++) {
            assertNotNull("Failed to create BaseDBRetrieval.",
                    new MockBaseDBRetrieval(factory, "UserProjectDataStore"));

        }
        long end = System.currentTimeMillis();
        System.out.println("Testing BaseDBRetrieval(DBConnectionFactory, String) for " + (RUN_TIMES / 10)
            + " times costs " + (end - start) + "ms");
    }
    /**
     * <p>Stress test for BaseDBRetrieval#retrieveObjects(PreparedStatement).</p>
     * @throws Exception to junit.
     */
    public void testRetrieveObjects() throws Exception {

        DBConnectionFactory factory = new DBConnectionFactoryImpl("com.cronos.onlinereview.external.stresstests");
        MockBaseDBRetrieval dbRetrieval = new MockBaseDBRetrieval("com.cronos.onlinereview.external.stresstests");
        Connection conn = factory.createConnection("UserProjectDataStore");
        Helper.insertUsers(conn, DATA_COUNT);
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            PreparedStatement ps = conn.prepareStatement("select * from user");
            Map map = dbRetrieval.retrieveObjects(ps);
            assertNotNull("BaseDBRetrieval#retrieveObjects(PreparedStatement) should not return null.", map);
            assertEquals("BaseDBRetrieval#retrieveObjects(PreparedStatement) doesn't get the right objects.",
                map.size(), DATA_COUNT);
        }

        long end = System.currentTimeMillis();
        Helper.clearTable(conn, "user");
        conn.close();
        System.out.println("Testing BaseDBRetrieval#retrieveObjects(PreparedStatement) for " + RUN_TIMES
            + " times costs " + (end - start) + "ms");
    }
    /**
     * <p>Mock class implements <code>ExternalObject</code>. </p>
     * @author fairytale
     * @version 1.0
     */
    private class MockExternalObject implements ExternalObject {
        /** The id for ExternalObject. */
        private final long id;
        /**
         * <p> The constructor of MockExternalObject. </p>
         * @param id the id of the ExternalObject.
         */
        public MockExternalObject(long id) {
            this.id = id;
        }

        /**
         * <p> The getter method of id. </p>
         * @return the id.
         */
        public long getId() {
            return id;
        }

    }
    /**
     * <p>Mock class extends <code>BaseDBRetrieval</code> for testing <code>BaseDBRetrieval</code>. </p>
     * @author fairytale
     * @version 1.0
     */
    private class MockBaseDBRetrieval extends BaseDBRetrieval {
        /**
         * <p>The constructor delegates to its super(String).</p>
         * @param namespace the ConfigManager namespace to retrieve the DBConnectionFactory and optional
         * connection name.
         * @throws ConfigException if super call throws ConfigException.
         */
        public MockBaseDBRetrieval(String namespace) throws ConfigException {
            super(namespace);
        }
        /**
         * <p>The constructor delegates to its super(DBConnectionFactory, String).</p>
         * @param connFactory the connection factory to use with this object.
         * @param connName the connection name to use when creating connections.
         * @throws ConfigException if the connection name doesn't correspond to a connection the
         * factory knows about.
         */
        public MockBaseDBRetrieval(DBConnectionFactory connFactory, String connName)
            throws ConfigException {
            super(connFactory, connName);
        }

        /**
         * <p>
         * The mock method simply gets 'user_id' information for rs and creates a MockExternalObject with the id.
         * </p>
         * @param rs the ResultSet.
         * @return the created MockExternalObject
         * @throws RetrievalException if any exception occurs.
         */
        protected ExternalObject createObject(ResultSet rs) throws RetrievalException {
            try {
                long id = rs.getLong("user_id");
                return new MockExternalObject(id);
            } catch (Exception e) {
                throw new RetrievalException("An exception occured.", e);
            }

        }
        /**
         * <p>
         * The method delegates to its super.retrieveObjects(PreparedStatement).
         * </p>
         * @param ps the PreparedStatement.
         * @return super call returns.
         * @throws RetrievalException if super call throws RetrievalException.
         */
        public Map retrieveObjects(PreparedStatement ps)
            throws RetrievalException {
            return super.retrieveObjects(ps);
        }

    }
}
