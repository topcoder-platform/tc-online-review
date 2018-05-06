/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests.impl;

import java.io.File;
import java.util.Iterator;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.impl.DBUserRetrieval;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * <p>
 * This test case aggregates all test cases for <code>DBUserRetrieval</code>.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class DBUserRetrievalFailureTest extends TestCase {

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

    /** A <code>DBUserRetrieval</code> instance used for tests. */
    private DBUserRetrieval retrieval = null;

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
        retrieval = new DBUserRetrieval(connFactory, CONN_NAME);
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
        retrieval = null;
    }

    /**
     * Test constructor
     * <code>DBUserRetrieval(DBConnectionFactory, String)</code> with null
     * argument. <code>connFactory</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalDBConnectionFactoryString_NullArg_1() throws Exception {
        try {
            new DBUserRetrieval(null, CONN_NAME);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBUserRetrieval(DBConnectionFactory, String)</code> with null
     * argument. <code>connName</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalDBConnectionFactoryString_NullArg_2() throws Exception {
        try {
            new DBUserRetrieval(connFactory, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBUserRetrieval(DBConnectionFactory, String)</code> with
     * empty-string argument. <code>connName</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalDBConnectionFactoryString_EmptyStringArg_1() throws Exception {
        try {
            new DBUserRetrieval(connFactory, "");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBUserRetrieval(DBConnectionFactory, String)</code> with
     * empty-string argument. <code>connName</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalDBConnectionFactoryString_EmptyStringArg_2() throws Exception {
        try {
            new DBUserRetrieval(connFactory, "  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBUserRetrieval(DBConnectionFactory, String)</code> with
     * empty-string argument. <code>connName</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalDBConnectionFactoryString_EmptyStringArg_3() throws Exception {
        try {
            new DBUserRetrieval(connFactory, "\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBUserRetrieval(DBConnectionFactory, String)</code> with
     * empty-string argument. <code>connName</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalDBConnectionFactoryString_EmptyStringArg_4() throws Exception {
        try {
            new DBUserRetrieval(connFactory, "\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBUserRetrieval(DBConnectionFactory, String)</code> with unknown
     * <code>connName</code>. <code>ConfigException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalDBConnectionFactoryString_UnknownConnName() throws Exception {
        try {
            new DBUserRetrieval(connFactory, "unknown");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBUserRetrieval(String)</code> with null
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalString_NullArg_1() throws Exception {
        try {
            new DBUserRetrieval(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBUserRetrieval(String)</code> with empty-string
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalString_EmptyStringArg_1() throws Exception {
        try {
            new DBUserRetrieval("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBUserRetrieval(String)</code> with empty-string
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalString_EmptyStringArg_2() throws Exception {
        try {
            new DBUserRetrieval("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBUserRetrieval(String)</code> with empty-string
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalString_EmptyStringArg_3() throws Exception {
        try {
            new DBUserRetrieval("\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBUserRetrieval(String)</code> with empty-string
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalString_EmptyStringArg_4() throws Exception {
        try {
            new DBUserRetrieval("\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBUserRetrieval(String)</code> with unknown
     * <code>namespace</code>. <code>ConfigException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalString_UnknownNamespace_1() throws Exception {
        try {
            new DBUserRetrieval("unknown");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBUserRetrieval(String)</code> with unknown
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalString_UnknownNamespace_2() throws Exception {
        try {
            tearDown();
            new DBUserRetrieval(NAMESPACE);
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBUserRetrieval(String)</code> with invalid
     * configuration. The <code>connName</code> is missing.
     * <code>ConfigException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalString_InvalidConfig_1() throws Exception {
        try {
            new DBUserRetrieval("invalid1");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBUserRetrieval(String)</code> with invalid
     * configuration. The <code>connName</code> is empty.
     * <code>ConfigException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBUserRetrievalString_InvalidConfig_2() throws Exception {
        try {
            new DBUserRetrieval("invalid2");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUser(long)</code> with negative
     * <code>id</code>. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUserLong_NegativeId() throws Exception {
        try {
            retrieval.retrieveUser(-1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUser(String)</code> with null
     * <code>handle</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUserString_NullArg() throws Exception {
        try {
            retrieval.retrieveUser(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUser(String)</code> with empty-string
     * <code>handle</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUserString_EmptyStringArg_1() throws Exception {
        try {
            retrieval.retrieveUser("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUser(String)</code> with empty-string
     * <code>handle</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUserString_EmptyStringArg_2() throws Exception {
        try {
            retrieval.retrieveUser("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUser(String)</code> with empty-string
     * <code>handle</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUserString_EmptyStringArg_3() throws Exception {
        try {
            retrieval.retrieveUser("\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUser(String)</code> with empty-string
     * <code>handle</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUserString_EmptyStringArg_4() throws Exception {
        try {
            retrieval.retrieveUser("\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUser(String)</code> with
     * <code>RetrievalException</code>. <code>RetrievalException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUserString_WithRetrievalError() throws Exception {
        retrieval = new DBUserRetrieval("invalid3");
        try {
            retrieval.retrieveUser("foobar");
            fail("RetrievalException is expected.");
        } catch (RetrievalException re) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsers(long[])</code> with null
     * <code>ids</code>. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersLongArray_NullIds() throws Exception {
        try {
            retrieval.retrieveUsers((long[]) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsers(long[])</code> with <code>ids</code>
     * containing negative element. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersLongArray_IdsWithNegativeElement() throws Exception {
        try {
            retrieval.retrieveUsers(new long[] {-1});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsers(long[])</code> with
     * <code>RetrievalException</code>. <code>RetrievalException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersLongArray_WithRetrievalError() throws Exception {
        retrieval = new DBUserRetrieval("invalid3");
        try {
            retrieval.retrieveUsers(new long[] {1});
            fail("RetrievalException is expected.");
        } catch (RetrievalException re) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsers(String[])</code> with null
     * <code>handles</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersStringArray_NullArg() throws Exception {
        try {
            retrieval.retrieveUsers((String[]) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsers(String[])</code> with
     * <code>handles</code> containing null element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersStringArray_ArrayWithNullElement() throws Exception {
        try {
            retrieval.retrieveUsers(new String[] {null});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsers(String[])</code> with
     * <code>handles</code> containing empty-string element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersStringArray_ArrayWithEmptyStringElement_1() throws Exception {
        try {
            retrieval.retrieveUsers(new String[] {""});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsers(String[])</code> with
     * <code>handles</code> containing empty-string element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersStringArray_ArrayWithEmptyStringElement_2() throws Exception {
        try {
            retrieval.retrieveUsers(new String[] {"  "});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsers(String[])</code> with
     * <code>handles</code> containing empty-string element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersStringArray_ArrayWithEmptyStringElement_3() throws Exception {
        try {
            retrieval.retrieveUsers(new String[] {"\t"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsers(String[])</code> with
     * <code>handles</code> containing empty-string element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersStringArray_ArrayWithEmptyStringElement_4() throws Exception {
        try {
            retrieval.retrieveUsers(new String[] {"\n"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsers(String[])</code> with
     * <code>RetrievalException</code>. <code>RetrievalException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersStringArray_WithRetrievalError() throws Exception {
        retrieval = new DBUserRetrieval("invalid3");
        try {
            retrieval.retrieveUsers(new String[] {"foobar"});
            fail("RetrievalException is expected.");
        } catch (RetrievalException re) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersIgnoreCase(String[])</code> with null
     * <code>handles</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersIgnoreCase_NullArg() throws Exception {
        try {
            retrieval.retrieveUsersIgnoreCase(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersIgnoreCase(String[])</code> with
     * <code>handles</code> containing null element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersIgnoreCase_ArrayWithNullElement() throws Exception {
        try {
            retrieval.retrieveUsersIgnoreCase(new String[] {null});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersIgnoreCase(String[])</code> with
     * <code>handles</code> containing empty-string element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersIgnoreCase_ArrayWithEmptyStringElement_1() throws Exception {
        try {
            retrieval.retrieveUsersIgnoreCase(new String[] {""});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersIgnoreCase(String[])</code> with
     * <code>handles</code> containing empty-string element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersIgnoreCase_ArrayWithEmptyStringElement_2() throws Exception {
        try {
            retrieval.retrieveUsersIgnoreCase(new String[] {"  "});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersIgnoreCase(String[])</code> with
     * <code>handles</code> containing empty-string element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersIgnoreCase_ArrayWithEmptyStringElement_3() throws Exception {
        try {
            retrieval.retrieveUsersIgnoreCase(new String[] {"\t"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersIgnoreCase(String[])</code> with
     * <code>handles</code> containing empty-string element.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersIgnoreCase_ArrayWithEmptyStringElement_4() throws Exception {
        try {
            retrieval.retrieveUsersIgnoreCase(new String[] {"\n"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersIgnoreCase(String[])</code> with
     * <code>RetrievalException</code>. <code>RetrievalException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersIgnoreCase_WithRetrievalError() throws Exception {
        retrieval = new DBUserRetrieval("invalid3");
        try {
            retrieval.retrieveUsersIgnoreCase(new String[] {"foobar"});
            fail("RetrievalException is expected.");
        } catch (RetrievalException re) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersByName(String, String)</code> with null
     * argument. <code>firstName</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersByName_NullArg_1() throws Exception {
        try {
            retrieval.retrieveUsersByName(null, "bar");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersByName(String, String)</code> with null
     * argument. <code>lastName</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersByName_NullArg_2() throws Exception {
        try {
            retrieval.retrieveUsersByName("foo", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersByName(String, String)</code> with
     * empty-string argument. <code>firstName</code> and <code>lastName</code>
     * are both empty-string. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersByName_EmptyStringArg_1() throws Exception {
        try {
            retrieval.retrieveUsersByName("", "  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersByName(String, String)</code> with
     * empty-string argument. <code>firstName</code> and <code>lastName</code>
     * are both empty-string. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersByName_EmptyStringArg_2() throws Exception {
        try {
            retrieval.retrieveUsersByName("\t", "\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveUsersByName(String, String)</code> with
     * <code>RetrievalException</code>. <code>RetrievalException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveUsersByName_WithRetrievalError() throws Exception {
        retrieval = new DBUserRetrieval("invalid3");
        try {
            retrieval.retrieveUsersByName("foo", "bar");
            fail("RetrievalException is expected.");
        } catch (RetrievalException re) {
            // Success
        }
    }

}
