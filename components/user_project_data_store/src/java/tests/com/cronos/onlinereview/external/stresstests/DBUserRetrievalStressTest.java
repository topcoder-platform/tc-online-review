/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.stresstests;

import java.sql.Connection;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.impl.DBUserRetrieval;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

/**
 * Stress test case of <code>DBUserRetrieval</code> class.
 * @author fairytale, victorsam
 * @version 1.1
 */
public class DBUserRetrievalStressTest extends TestCase {
    /** The number of times each method will be run. */
    public static final int RUN_TIMES = 10;
    /** The data item count will be insert into db before test processes. */
    public static final int DATA_COUNT = 200;
    /** The DBConnectionFactory used among the tests. */
    private DBConnectionFactory factory;
    /** The connection to db. */
    private Connection conn;
    /** The DBUserRetrieval instance used in the test. */
    private DBUserRetrieval userRetrieval;
    /**
     * Test suite of DBUserRetrievalStressTest.
     *
     * @return Test suite of DBUserRetrievalStressTest.
     */
    public static Test suite() {
        return new TestSuite(DBUserRetrievalStressTest.class);
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
        Helper.clearTable(conn, "email");
        Helper.clearTable(conn, "user");
        Helper.insertUsers(conn, DATA_COUNT);
        Helper.insertEmail(conn, DATA_COUNT);
        userRetrieval = new DBUserRetrieval("com.cronos.onlinereview.external.stresstests");
    }

    /**
     * <p>Clear the test environment.</p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        Helper.unloadConfig();
        Helper.clearTable(conn, "email");
        Helper.clearTable(conn, "user");
        conn.close();
    }

    /**
     * <p>Stress test for DBUserRetrieval#retrieveUser(long).</p>
     * @throws Exception to junit.
     */
    public void testRetrieveUserById() throws Exception {
        Random rand = new Random();
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            long id = Math.abs(rand.nextInt()) % DATA_COUNT;
            ExternalUser user = userRetrieval.retrieveUser(Helper.USER_ID_START + id);
            assertNotNull("Should retrieve user.", user);
            assertEquals("Should retrieve the right user_id.", user.getId(), Helper.USER_ID_START + id);
            assertEquals("Should retrieve alternative email.", user.getAlternativeEmails().length, 1);
            assertEquals("Should contain no design rating information.", user.getDesignRating(), "N/A");
            assertEquals("Should contain no dev rating information.", user.getDevRating(), "N/A");
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DBUserRetrieval#retrieveUser(long) for " + RUN_TIMES
                + " times costs " + (end - start) + "ms");
    }

    /**
     * <p>Stress test for DBUserRetrieval#retrieveUser(long).</p>
     * @throws Exception to junit.
     */
    public void testRetrieveUserByIdNotFound() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            ExternalUser user = userRetrieval.retrieveUser(Helper.USER_ID_START - 1);
            assertNull("No user should be retrieved.", user);
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DBUserRetrieval#retrieveUser(long) for " + RUN_TIMES
                + " times costs " + (end - start) + "ms when no user is found.");
    }

    /**
     * <p>Stress test for DBUserRetrieval#retrieveUser(String).</p>
     * @throws Exception to junit.
     */
    public void testRetrieveUserByHandle() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            ExternalUser user = userRetrieval.retrieveUser("faIryTale");
            assertNotNull("Should retrieve user.", user);
            assertEquals("Should retrieve the right handle.", user.getHandle(), "faIryTale");
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DBUserRetrieval#retrieveUser(String) for " + RUN_TIMES
                + " times costs " + (end - start) + "ms.");
    }

    /**
     * <p>Stress test for DBUserRetrieval#retrieveUser(String).</p>
     * @throws Exception to junit.
     */
    public void testRetrieveUserByHandleNotFound() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            ExternalUser user = userRetrieval.retrieveUser("chenhong");
            assertNull("No user should be retrieved.", user);
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DBUserRetrieval#retrieveUser(String) for " + RUN_TIMES
                + " times costs " + (end - start) + "ms when no user is found.");
    }

    /**
     * <p>Stress test for DBUserRetrieval#retrieveUsers(String[]).</p>
     * @throws Exception to junit.
     */
    public void testRetrieveUsers() throws Exception {

        String []handles = new String[DATA_COUNT / 2];
        Random rand = new Random();
        for (int i = 0; i < DATA_COUNT / 2; i++) {
            String handle = Helper.getRandomHandle(rand);
            handles[i] = handle;
        }
        ExternalUser[] users = userRetrieval.retrieveUsers(handles);
        assertNotNull("Should return non-null users.", users);
        assertTrue("Should return non-empty users.", users.length >= 1);
    }

    /**
     * <p>Stress test for DBUserRetrieval#retrieveUsersIgnoreCase(String[]).</p>
     * @throws Exception to junit.
     */
    public void testRetrieveUsersIgnoreCase() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            ExternalUser[] users = userRetrieval.retrieveUsersIgnoreCase(new String[]{"FAIRYTale"});
            assertNotNull("Should return non-null users.", users);
            assertTrue("Should return non-empty users.", users.length >= 1);
            assertEquals("Each user retrieved should have the right handle.", users[0].getHandle(), "faIryTale");
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DBUserRetrieval#retrieveUsersIgnoreCase(String[]) for " + RUN_TIMES
                + " times costs " + (end - start) + "ms.");
    }
    /**
     * <p>Stress test for DBUserRetrieval#retrieveUsersByName(String, String).</p>
     * @throws Exception to junit.
     */
    public void testRetrieveUsersByFirstName() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            ExternalUser[] users = userRetrieval.retrieveUsersByName("Hong", "");
            assertNotNull("Should return non-null users.", users);
            assertTrue("Should return non-empty users.", users.length >= 1);
            assertEquals("Each user retrieved should have the right handle.", users[0].getHandle(), "faIryTale");
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DBUserRetrieval#retrieveUsersByName(String, String) for " + RUN_TIMES
                + " times costs " + (end - start) + "ms when lastName is empty.");
    }
    /**
     * <p>Stress test for DBUserRetrieval#retrieveUsersByName(String, String).</p>
     * @throws Exception to junit.
     */
    public void testRetriveUsersByLastName() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            ExternalUser[] users = userRetrieval.retrieveUsersByName("", "jinhu");
            assertNotNull("Should return non-null users.", users);
            assertTrue("Should return non-empty users.", users.length >= 1);
            assertEquals("Each user retrieved should have the right handle.", users[0].getHandle(), "faIryTale");
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DBUserRetrieval#retrieveUsersByName(String, String) for " + RUN_TIMES
                + " times costs " + (end - start) + "ms when firstName is empty.");
    }
    /**
     * <p>Stress test for DBUserRetrieval#retrieveUsersByName(String, String).</p>
     * @throws Exception to junit.
     */
    public void testRetriveUsersByFirstAndLastName() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < RUN_TIMES; i++) {
            ExternalUser[] users = userRetrieval.retrieveUsersByName("Hong", "jinhu");
            assertNotNull("Should return non-null users.", users);
            assertTrue("Should return non-empty users.", users.length >= 1);
            assertEquals("Each user retrieved should have the right handle.", users[0].getHandle(), "faIryTale");
        }
        long end = System.currentTimeMillis();
        System.out.println("Testing DBUserRetrieval#retrieveUsersByName(String, String) for " + RUN_TIMES
                + " times costs " + (end - start) + "ms.");
    }
}
