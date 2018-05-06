/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests.impl;

import java.io.File;
import java.util.Iterator;

import com.cronos.onlinereview.external.ConfigException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * <p>
 * This test case aggregates all test cases for <code>BaseDBRetrieval</code>.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class BaseDBRetrievalFailureTest extends TestCase {

    /** Configuration file path. */
    private static final String CONFIG_FILE = "FailureTests" + File.separator + "ConfigFile.xml";

    /** Invalid configuration file path. */
    private static final String INVALID_CONFIG_FILE = "FailureTests" + File.separator + "InvalidConfigFile.xml";

    /** Namespace used for tests. */
    private static final String NAMESPACE = "com.cronos.onlinereview.external";

    /** Connection name used in tests. */
    private static final String CONN_NAME = "UserProjectDataStoreConnection";

    /** A <code>DBConnectionFactory</code> instance used for tests. */
    private DBConnectionFactory connFactory = null;

    /**
     * Set up.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        tearDown();
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(CONFIG_FILE);
        cm.add(INVALID_CONFIG_FILE);

        connFactory = new DBConnectionFactoryImpl(NAMESPACE);
    }

    /**
     * Tear down.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator itr = cm.getAllNamespaces(); itr.hasNext();) {
            cm.removeNamespace((String) itr.next());
        }
        connFactory = null;
    }

    /**
     * Test constructor
     * <code>BaseDBRetrieval(DBConnectionFactory, String)</code> with null
     * argument. <code>connFactory</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalDBConnectionFactoryString_NullArg_1() throws Exception {
        try {
            new DummyBaseDBRetrieval(null, CONN_NAME);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>BaseDBRetrieval(DBConnectionFactory, String)</code> with null
     * argument. <code>connName</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalDBConnectionFactoryString_NullArg_2() throws Exception {
        try {
            new DummyBaseDBRetrieval(connFactory, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>BaseDBRetrieval(DBConnectionFactory, String)</code> with
     * empty-string argument. <code>connName</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalDBConnectionFactoryString_EmptyStringArg_1() throws Exception {
        try {
            new DummyBaseDBRetrieval(connFactory, "");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>BaseDBRetrieval(DBConnectionFactory, String)</code> with
     * empty-string argument. <code>connName</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalDBConnectionFactoryString_EmptyStringArg_2() throws Exception {
        try {
            new DummyBaseDBRetrieval(connFactory, "  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>BaseDBRetrieval(DBConnectionFactory, String)</code> with
     * empty-string argument. <code>connName</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalDBConnectionFactoryString_EmptyStringArg_3() throws Exception {
        try {
            new DummyBaseDBRetrieval(connFactory, "\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>BaseDBRetrieval(DBConnectionFactory, String)</code> with
     * empty-string argument. <code>connName</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalDBConnectionFactoryString_EmptyStringArg_4() throws Exception {
        try {
            new DummyBaseDBRetrieval(connFactory, "\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>BaseDBRetrieval(DBConnectionFactory, String)</code> with unknown
     * <code>connName</code>. <code>ConfigException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalDBConnectionFactoryString_UnknownConnName() throws Exception {
        try {
            new DummyBaseDBRetrieval(connFactory, "unknown");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>BaseDBRetrieval(String)</code> with null
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalString_NullArg() throws Exception {
        try {
            new DummyBaseDBRetrieval(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>BaseDBRetrieval(String)</code> with empty-string
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalString_EmptyStringArg_1() throws Exception {
        try {
            new DummyBaseDBRetrieval("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>BaseDBRetrieval(String)</code> with empty-string
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalString_EmptyStringArg_2() throws Exception {
        try {
            new DummyBaseDBRetrieval("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>BaseDBRetrieval(String)</code> with empty-string
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalString_EmptyStringArg_3() throws Exception {
        try {
            new DummyBaseDBRetrieval("\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>BaseDBRetrieval(String)</code> with empty-string
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalString_EmptyStringArg_4() throws Exception {
        try {
            new DummyBaseDBRetrieval("\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>BaseDBRetrieval(String)</code> with unknown
     * <code>namespace</code>. <code>ConfigException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalString_UnknownNamespace_1() throws Exception {
        try {
            new DummyBaseDBRetrieval("unknown");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>BaseDBRetrieval(String)</code> with unknown
     * <code>namespace</code>. <code>ConfigException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalString_UnknownNamespace_2() throws Exception {
        try {
            tearDown();
            new DummyBaseDBRetrieval(NAMESPACE);
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>BaseDBRetrieval(String)</code> with invalid
     * configuration. The <code>connName</code> is missing.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalString_InvalidConfig_1() throws Exception {
        try {
            new DummyBaseDBRetrieval("invalid1");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>BaseDBRetrieval(String)</code> with invalid
     * configuration. The <code>connName</code> is empty.
     *
     * @throws Exception to JUnit
     */
    public void testBaseDBRetrievalString_InvalidConfig_2() throws Exception {
        try {
            new DummyBaseDBRetrieval("invalid2");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

}
