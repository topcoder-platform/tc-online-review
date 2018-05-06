/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests.impl;

import java.io.File;
import java.util.Iterator;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.impl.DBProjectRetrieval;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * <p>
 * This test case aggregates all test cases for <code>DBProjectRetrieval</code>.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class DBProjectRetrievalFailureTest extends TestCase {
    /** Configuration file path. */
    private static final String CONFIG_FILE = "test_files" + File.separator + "FailureTests" + File.separator
            + "ConfigFile.xml";

    /** Invalid configuration file path. */
    private static final String INVALID_CONFIG_FILE = "test_files" + File.separator + "FailureTests" + File.separator
            + "InvalidConfigFile.xml";

    /** Namespace used for tests. */
    private static final String NAMESPACE = "com.cronos.onlinereview.external";

    /** Connection name used in tests. */
    private static final String CONN_NAME = "UserProjectDataStoreConnection";

    /** A <code>DBConnectionFactory</code> instance used for tests. */
    private DBConnectionFactory connFactory = null;

    /** A <code>DBProjectRetrieval</code> instance used for tests. */
    private DBProjectRetrieval retrieval = null;

    /**
     * Set up.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        tearDown();
        ConfigManager cm = ConfigManager.getInstance();

        File file;

        file = new File(CONFIG_FILE);
        cm.add(file.getCanonicalPath());

        File invalidFile;
        invalidFile = new File(INVALID_CONFIG_FILE);
        cm.add(invalidFile.getCanonicalPath());

        connFactory = new DBConnectionFactoryImpl(NAMESPACE);
        retrieval = new DBProjectRetrieval(connFactory, CONN_NAME, 1);
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
     * <code>DBProjectRetrieval(DBConnectionFactory, String, int)</code> with
     * null argument. <code>connFactory</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalDBConnectionFactoryStringInt_NullArg_1() throws Exception {
        try {
            new DBProjectRetrieval(null, CONN_NAME, 1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBProjectRetrieval(DBConnectionFactory, String, int)</code> with
     * null argument. <code>connName</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalDBConnectionFactoryStringInt_NullArg_2() throws Exception {
        try {
            new DBProjectRetrieval(connFactory, null, 1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBProjectRetrieval(DBConnectionFactory, String, int)</code> with
     * negative <code>forumType</code>. <code>IllegalArgumentException</code>
     * is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalDBConnectionFactoryStringInt_NegativeForumType() throws Exception {
        try {
            new DBProjectRetrieval(connFactory, CONN_NAME, -1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBProjectRetrieval(DBConnectionFactory, String, int)</code> with
     * empty-string argument. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalDBConnectionFactoryStringInt_EmptyStringArg_1() throws Exception {
        try {
            new DBProjectRetrieval(connFactory, "", 1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBProjectRetrieval(DBConnectionFactory, String, int)</code> with
     * empty-string argument. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalDBConnectionFactoryStringInt_EmptyStringArg_2() throws Exception {
        try {
            new DBProjectRetrieval(connFactory, "  ", 1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBProjectRetrieval(DBConnectionFactory, String, int)</code> with
     * empty-string argument. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalDBConnectionFactoryStringInt_EmptyStringArg_3() throws Exception {
        try {
            new DBProjectRetrieval(connFactory, "\t", 1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBProjectRetrieval(DBConnectionFactory, String, int)</code> with
     * empty-string argument. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalDBConnectionFactoryStringInt_EmptyStringArg_4() throws Exception {
        try {
            new DBProjectRetrieval(connFactory, "\n", 1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor
     * <code>DBProjectRetrieval(DBConnectionFactory, String, int)</code> with
     * unknown <code>connName</code>. <code>ConfigException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalDBConnectionFactoryStringInt_UnknownConnName() throws Exception {
        try {
            new DBProjectRetrieval(connFactory, "unknown", 1);
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with null
     * <code>namespace</code>. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalString_NullArg() throws Exception {
        try {
            new DBProjectRetrieval(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with
     * empty-string <code>namespace</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalString_EmptyStringArg_1() throws Exception {
        try {
            new DBProjectRetrieval("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with
     * empty-string <code>namespace</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalString_EmptyStringArg_2() throws Exception {
        try {
            new DBProjectRetrieval("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with
     * empty-string <code>namespace</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalString_EmptyStringArg_3() throws Exception {
        try {
            new DBProjectRetrieval("\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with
     * empty-string <code>namespace</code>.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalString_EmptyStringArg_4() throws Exception {
        try {
            new DBProjectRetrieval("\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with unknown
     * <code>namespace</code>. <code>ConfigException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalString_UnknownNamespace_1() throws Exception {
        try {
            new DBProjectRetrieval("unknown");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with unknown
     * <code>namespace</code>. <code>ConfigException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testDBProjectRetrievalString_UnknownNamespace_2() throws Exception {
        try {
            tearDown();
            new DBProjectRetrieval(NAMESPACE);
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with invalid
     * configuration. <code>connName</code> is not defined.
     * <code>ConfigException</code> is expected.
     */
    public void testDBProjectRetrievalString_InvalidConfig_1() {
        try {
            new DBProjectRetrieval("invalid1");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with invalid
     * configuration. <code>connName</code> is empty.
     * <code>ConfigException</code> is expected.
     */
    public void testDBProjectRetrievalString_InvalidConfig_2() {
        try {
            new DBProjectRetrieval("invalid2");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with invalid
     * configuration. <code>forumType</code> cannot be parsed.
     * <code>ConfigException</code> is expected.
     */
    public void testDBProjectRetrievalString_InvalidConfig_3() {
        try {
            new DBProjectRetrieval("invalid4");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test constructor <code>DBProjectRetrieval(String)</code> with invalid
     * configuration. <code>forumType</code> is negative.
     * <code>ConfigException</code> is expected.
     */
    public void testDBProjectRetrievalString_InvalidConfig_4() {
        try {
            new DBProjectRetrieval("invalid5");
            fail("ConfigException is expected.");
        } catch (ConfigException ce) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProject(long)</code> with negative
     * <code>id</code>. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectLong_NegativeId() throws Exception {
        try {
            retrieval.retrieveProject(-1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProject(long)</code> with
     * <code>RetrievalException</code>. <code>RetrievalException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectLong_WithRetrievalError() throws Exception {
        retrieval = new DBProjectRetrieval("invalid3");
        try {
            retrieval.retrieveProject(1);
            fail("RetrievalException is expected.");
        } catch (RetrievalException re) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProject(String, String)</code> with null
     * argument. <code>name</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectStringString_NullArg_1() throws Exception {
        try {
            retrieval.retrieveProject(null, "bar");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProject(String, String)</code> with null
     * argument. <code>version</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectStringString_NullArg_2() throws Exception {
        try {
            retrieval.retrieveProject("foo", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProject(String, String)</code> with
     * empty-string argument. <code>name</code> is empty-string in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectStringString_EmptyStringArg_1() throws Exception {
        try {
            retrieval.retrieveProject("", "bar");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProject(String, String)</code> with
     * empty-string argument. <code>name</code> is empty-string in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectStringString_EmptyStringArg_2() throws Exception {
        try {
            retrieval.retrieveProject("  ", "bar");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProject(String, String)</code> with
     * empty-string argument. <code>version</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectStringString_EmptyStringArg_3() throws Exception {
        try {
            retrieval.retrieveProject("foo", "\t");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProject(String, String)</code> with
     * empty-string argument. <code>version</code> is empty-string in this
     * test. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectStringString_EmptyStringArg_4() throws Exception {
        try {
            retrieval.retrieveProject("foo", "\n");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProject(String, String)</code> with
     * <code>RetrievalException</code>. <code>RetrievalException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectStringString_WithRetrievalError() throws Exception {
        retrieval = new DBProjectRetrieval("invalid3");
        try {
            retrieval.retrieveProject("foo", "bar");
            fail("RetrievalException is expected.");
        } catch (RetrievalException re) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(long[])</code> with null
     * <code>ids</code>. <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsLongArray_NullIds() throws Exception {
        try {
            retrieval.retrieveProjects(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(long[])</code> with <code>ids</code>
     * containing negative element. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsLongArray_IdsWithNegativeElement() throws Exception {
        try {
            retrieval.retrieveProjects(new long[] {1, -1});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(long[])</code> with
     * <code>RetrievalException</code>. <code>RetrievalException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsLongArray_WithRetrievalError() throws Exception {
        retrieval = new DBProjectRetrieval("invalid3");
        try {
            retrieval.retrieveProjects(new long[] {1, 2});
            fail("RetrievalException is expected.");
        } catch (RetrievalException re) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(String[], String[])</code> with null
     * argument. <code>names</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsStringArrayStringArray_NullArg_1() throws Exception {
        try {
            retrieval.retrieveProjects(null, new String[] {"bar"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(String[], String[])</code> with null
     * argument. <code>versions</code> is null in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsStringArrayStringArray_NullArg_2() throws Exception {
        try {
            retrieval.retrieveProjects(new String[] {"foo"}, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(String[], String[])</code> with
     * input array containing null element. <code>names</code> contains null
     * element in this test. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsStringArrayStringArray_ArrayWithNullElement_1() throws Exception {
        try {
            retrieval.retrieveProjects(new String[] {null}, new String[] {"bar"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(String[], String[])</code> with
     * input array containing null element. <code>versions</code> contains
     * null element in this test. <code>IllegalArgumentException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsStringArrayStringArray_ArrayWithNullElement_2() throws Exception {
        try {
            retrieval.retrieveProjects(new String[] {"foo"}, new String[] {null});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(String[], String[])</code> with
     * input array containing empty-string element. <code>names</code>
     * contains empty-string element in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsStringArrayStringArray_ArrayWithEmptyStringElement_1() throws Exception {
        try {
            retrieval.retrieveProjects(new String[] {""}, new String[] {"bar"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(String[], String[])</code> with
     * input array containing empty-string element. <code>names</code>
     * contains empty-string element in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsStringArrayStringArray_ArrayWithEmptyStringElement_2() throws Exception {
        try {
            retrieval.retrieveProjects(new String[] {"  "}, new String[] {"bar"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(String[], String[])</code> with
     * input array containing empty-string element. <code>versions</code>
     * contains empty-string element in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsStringArrayStringArray_ArrayWithEmptyStringElement_3() throws Exception {
        try {
            retrieval.retrieveProjects(new String[] {"foo"}, new String[] {"\t"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(String[], String[])</code> with
     * input array containing empty-string element. <code>versions</code>
     * contains empty-string element in this test.
     * <code>IllegalArgumentException</code> is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsStringArrayStringArray_ArrayWithEmptyStringElement_4() throws Exception {
        try {
            retrieval.retrieveProjects(new String[] {"foo"}, new String[] {"\n"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(String[], String[])</code> with
     * input arrays of different size. <code>IllegalArgumentException</code>
     * is expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsStringArrayStringArray_ArrayWithDifferentSize() throws Exception {
        try {
            retrieval.retrieveProjects(new String[] {"foo"}, new String[] {"bar", "foobar"});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>retrieveProjects(String[], String[])</code> with
     * <code>RetrievalException</code>
     * <code>RetrievalException</code> is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testRetrieveProjectsStringArrayStringArray_WithRetrievalError() throws Exception {
        retrieval = new DBProjectRetrieval("invalid3");
        try {
            retrieval.retrieveProjects(new String[] {"foo"}, new String[] {"bar"});
            fail("RetrievalException is expected.");
        } catch (RetrievalException re) {
            // Success
        }
    }

}
