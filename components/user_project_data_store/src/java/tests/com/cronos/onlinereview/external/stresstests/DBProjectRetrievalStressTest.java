/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.stresstests;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.cronos.onlinereview.external.ExternalProject;
import com.cronos.onlinereview.external.impl.DBProjectRetrieval;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Stress test case of <code>DBProjectRetrieval</code> class.
 * @author fairytale, victorsam
 * @version 1.1
 */
public class DBProjectRetrievalStressTest extends TestCase {
    /** The number of times each method will be run. */
    public static final int RUN_TIMES = 100;

    /** The data item count will be insert into db before test processes. */
    public static final int DATA_COUNT = 500;

    /** The DBConnectionFactory used among the tests. */
    private DBConnectionFactory factory;

    /** The connection to db. */
    private Connection conn;

    /** The DBProjectRetrieval instance used in the test. */
    private DBProjectRetrieval projectRetrieval;

    /**
     * Test suite of DBProjectRetrievalStressTest.
     *
     * @return Test suite of DBProjectRetrievalStressTest.
     */
    public static Test suite() {
        return new TestSuite(DBProjectRetrievalStressTest.class);
    }

    /**
     * Initialization for all tests here.
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        // set up for test
        Helper.loadConfig();
        factory = new DBConnectionFactoryImpl("com.cronos.onlinereview.external.stresstests");
        conn = factory.createConnection("UserProjectDataStore");
        //if comp_forum_xref contains data, intsertComponentCataLog will cause exception
        // so first delete data in comp_forum_xref;
        Helper.clearTable(conn, "comp_forum_xref");
        Helper.clearTable(conn, "comp_versions");
        Helper.clearTable(conn, "comp_catalog");

        Helper.intsertComponentCataLog(conn, DATA_COUNT);

        Helper.insertComponentVersions(conn, DATA_COUNT);
        // only insert only half comp_forum_xref data items corresponding to comp_versions
        // [comp_vers_id_start, comp_vers_id_start + DATA_COUNT/2) have corresponding comp_forum_xref.
        Helper.insertCompForumXref(conn, DATA_COUNT / 2);
        projectRetrieval = new DBProjectRetrieval("com.cronos.onlinereview.external.stresstests");
    }

    /**
     * <p>Clear the test environment.</p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        Helper.unloadConfig();
        Helper.clearTable(conn, "comp_forum_xref");
        Helper.clearTable(conn, "comp_versions");
        Helper.clearTable(conn, "comp_catalog");
        conn.close();
    }

    /**
     * <p>Stress test for DBProjectRetrieval#DBProjectRetrieval(String).</p>
     * @throws Exception to junit.
     */
    public void testCtor() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            assertNotNull("Failed to create DBProjectRetrieval.", new DBProjectRetrieval(
                "com.cronos.onlinereview.external.stresstests"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DBProjectRetrieval(String) for " + RUN_TIMES + " times costs " + (end - start)
            + "ms");
    }

    /**
     * <p>Stress test for DBProjectRetrieval#retrieveProject(long).</p>
     * @throws Exception to junit.
     */
     public void testRetrieveProjectsByID() throws Exception {
        Random rand = new Random();
        for (int i = 0; i < RUN_TIMES; i++) {
            long id = Math.abs(rand.nextInt()) % DATA_COUNT;
            ExternalProject project = projectRetrieval.retrieveProject(Helper.COMP_VERS_ID_START + id);
            assertNotNull("Not null project expected.", project);
            assertEquals("Expect the right catalogID.", project.getCatalogId(), id);
            assertEquals("Expect the right id.", project.getId(), Helper.COMP_VERS_ID_START + id);
            assertEquals("Expect the right versionID.", project.getVersionId(), id);
            // if id < DATA_COUNT/2 , then there is a corresponding comp_forum_xref
            if (id < DATA_COUNT / 2) {
                assertEquals("Expect the right forumID.", project.getForumId(), 100);
            } else {
                assertEquals("Expect -1 when forum not existed.", project.getForumId(), -1);
            }
        }

    }

    /**
     * <p>Stress test for DBProjectRetrieval#retrieveProjects(long[]).</p>
     * @throws Exception to junit.
     */
     public void testRetrieveProjectsByIDs() throws Exception {

        Random rand = new Random();
        long[] ids = new long[RUN_TIMES];
        Set set = new HashSet();
        for (int i = 0; i < RUN_TIMES; i++) {
            ids[i] = Helper.COMP_VERS_ID_START + (Math.abs(rand.nextInt()) % DATA_COUNT);
            set.add(new Long(ids[i]));
        }
        ExternalProject[] projects = projectRetrieval.retrieveProjects(ids);
        assertNotNull("Should return non-null projects.", projects);
        assertEquals("Should have the same size.", projects.length, set.size());
        for (int i = 0; i < projects.length; i++) {
            assertTrue("Should contains projects[i]'s id.", set.contains(new Long(projects[i].getId())));
            if (projects[i].getId() < Helper.COMP_VERS_ID_START + DATA_COUNT / 2) {
                assertEquals("Expect the right forumID.", projects[i].getForumId(), 100);
            } else {
                assertEquals("Expect -1 when forum not existed.", projects[i].getForumId(), -1);
            }
        }
    }

    /**
     * <p>Stress test for DBProjectRetrieval#retrieveProject(String, String).</p>
     * @throws Exception to junit.
     */
    public void testRetrieveProjectByNameAndVerision() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES / 5; i++) {
            ExternalProject[] projects = projectRetrieval.retrieveProject(Helper.TEST_COMPONENTS[0], "1.0");
            assertNotNull("Should return non-null projects.", projects);
            assertTrue("The returned array should have at least one item.", projects.length >= 1);
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing retrieveProject(String, String) for " + (RUN_TIMES / 5) + " times costs "
            + (end - start) + "ms");
    }

    /**
     * <p>Stress test for DBProjectRetrieval#retrieveProject(String[], String[]).</p>
     * @throws Exception to junit.
     */
    public void testRetrieveProjectsByNameAndVerision() throws Exception {
        String[] names = new String[DATA_COUNT / 10];
        String[] versions = new String[DATA_COUNT / 10];
        names[0] = Helper.TEST_COMPONENTS[0];
        versions[0] = "1.0";
        for (int i = 1; i < DATA_COUNT / 10; i++) {
            names[i] = "oterh component";
            versions[i] = "nonexisted";
        }
        ExternalProject[] projects = projectRetrieval.retrieveProjects(names, versions);
        assertNotNull("Should return non-null projects.", projects);
        assertTrue("The returned array should have at least one item.", projects.length >= 1);
    }
}
