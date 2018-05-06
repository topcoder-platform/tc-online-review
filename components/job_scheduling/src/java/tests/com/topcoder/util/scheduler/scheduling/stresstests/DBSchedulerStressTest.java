/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * @(#)DBSchedulerStressTest.java
 */
package com.topcoder.util.scheduler.scheduling.stresstests;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

import junit.framework.TestCase;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.scheduler.scheduling.persistence.DBScheduler;

/**
 * Stress test of DBScheduler.
 *
 * @author  smell
 * @version 3.0
 */
public class DBSchedulerStressTest extends TestCase {

    /**
     * Repeat count of the stress test.
     */
    private static final int REPEAT_COUNT = 20;
    /**
     * Configuration file of the DBScheduler.
     */
    private static final String CONFIG_FILE = "stresstests/DBConfig.xml";
    /**
     * Configuration namespace used to create the DBScheduler.
     */
    private static final String NAMESPACE = "com.topcoder.util.scheduler.scheduling.stresstests.DBScheduler";
    /**
     * Namespace of the DBConnectionFactory.
     */
    private static final String DB_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";
    /**
     * The DBScheduler instance to test.
     */
    private DBScheduler scheduler = null;

    /**
     * Creates the DBScheduler instance.
     *
     * @throws Exception to JUnit.
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(CONFIG_FILE);

        scheduler = new DBScheduler(NAMESPACE);
    }

    /**
     * Clears the database and config manager.
     *
     * @throws Exception to JUnit.
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        Connection connection = null;
        Statement stmt = null;

        try {
            connection = new DBConnectionFactoryImpl(DB_NAMESPACE).createConnection();
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM groupjob");
            stmt.executeUpdate("DELETE FROM message");
            stmt.executeUpdate("DELETE FROM job");
            stmt.executeUpdate("DELETE FROM group");
            connection.commit();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator iterator = cm.getAllNamespaces(); iterator.hasNext();) {
            String ns = (String) iterator.next();
            cm.removeNamespace(ns);
        }

    }

    /**
     * Tests the addJob method.
     *
     * It tries to add REPEAT_COUNT jobs to the scheduler.
     *
     * @throws Exception to JUnit.
     */
    public void testAddJob() throws Exception {
        long start = System.currentTimeMillis();
        StressTestHelper.addJobs(scheduler, REPEAT_COUNT);
        long end = System.currentTimeMillis();
        System.out.println("Adding " + REPEAT_COUNT + " jobs to ConfigManagerScheduler takes " + (end - start)
            + " ms.");
    }

    /**
     * Tests the deleteJob method.
     *
     * It tries to delete REPEAT_COUNT jobs from the scheduler.
     *
     * @throws Exception to JUnit.
     */
    public void testDeleteJob() throws Exception {
        StressTestHelper.addJobs(scheduler, REPEAT_COUNT);

        long start = System.currentTimeMillis();
        StressTestHelper.deleteJobs(scheduler, REPEAT_COUNT);
        long end = System.currentTimeMillis();
        System.out.println("Deleting " + REPEAT_COUNT + " jobs from ConfigManagerScheduler takes " + (end - start)
            + " ms.");
    }

    /**
     * Tests the updateJob method.
     *
     * It tries to update REPEAT_COUNT jobs on the scheduler.
     *
     * @throws Exception to JUnit.
     */
    public void testUpdateJob() throws Exception {
        StressTestHelper.addJobs(scheduler, REPEAT_COUNT);

        long start = System.currentTimeMillis();
        StressTestHelper.updateJobs(scheduler, REPEAT_COUNT);
        long end = System.currentTimeMillis();
        System.out.println("Updating " + REPEAT_COUNT + " jobs on ConfigManagerScheduler takes " + (end - start)
            + " ms.");
    }

    /**
     * Tests the readJob method.
     *
     * It tries to read REPEAT_COUNT jobs from the scheduler.
     *
     * @throws Exception to JUnit.
     */
    public void testReadJob() throws Exception {
        StressTestHelper.addJobs(scheduler, REPEAT_COUNT);

        long start = System.currentTimeMillis();
        StressTestHelper.readJobs(scheduler, REPEAT_COUNT);
        long end = System.currentTimeMillis();
        System.out.println("Reading " + REPEAT_COUNT + " jobs from ConfigManagerScheduler takes " + (end - start)
            + " ms.");
    }

    /**
     * Tests the addGroup method.
     *
     * It tries to add REPEAT_COUNT job groups on the scheduler.
     *
     * @throws Exception to JUnit.
     */
    public void testAddGroup() throws Exception {
        StressTestHelper.addJobs(scheduler, REPEAT_COUNT);
        StressTestHelper.addJobGroups(scheduler, REPEAT_COUNT);

        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        System.out.println("Adding " + REPEAT_COUNT + " groups to ConfigManagerScheduler takes " + (end - start)
            + " ms.");
    }

    /**
     * Tests the deleteGroup method.
     *
     * It tries to delete REPEAT_COUNT job groups from the scheduler.
     *
     * @throws Exception to JUnit.
     */
    public void testDeleteGroup() throws Exception {
        StressTestHelper.addJobs(scheduler, REPEAT_COUNT);
        StressTestHelper.addJobGroups(scheduler, REPEAT_COUNT);

        long start = System.currentTimeMillis();
        StressTestHelper.deleteJobGroups(scheduler, REPEAT_COUNT);
        long end = System.currentTimeMillis();
        System.out.println("Deleting " + REPEAT_COUNT + " groups from ConfigManagerScheduler takes " + (end - start)
            + " ms.");
    }

    /**
     * Tests the updateGroup method.
     *
     * It tries to update REPEAT_COUNT job groups on the scheduler.
     *
     * @throws Exception to JUnit.
     */
    public void testUpdateGroup() throws Exception {
        StressTestHelper.addJobs(scheduler, REPEAT_COUNT);
        StressTestHelper.addJobGroups(scheduler, REPEAT_COUNT);

        long start = System.currentTimeMillis();
        StressTestHelper.updateJobGroups(scheduler, REPEAT_COUNT);
        long end = System.currentTimeMillis();
        System.out.println("Updating " + REPEAT_COUNT + " groups on ConfigManagerScheduler takes " + (end - start)
            + " ms.");
    }

    /**
     * Tests the readGroup method.
     *
     * It tries to read REPEAT_COUNT job groups from the scheduler.
     *
     * @throws Exception to JUnit.
     */
    public void testReadGroup() throws Exception {
        StressTestHelper.addJobs(scheduler, REPEAT_COUNT);
        StressTestHelper.addJobGroups(scheduler, REPEAT_COUNT);

        long start = System.currentTimeMillis();
        StressTestHelper.readJobGroups(scheduler, REPEAT_COUNT);
        long end = System.currentTimeMillis();
        System.out.println("Reading " + REPEAT_COUNT + " groups from ConfigManagerScheduler takes " + (end - start)
            + " ms.");
    }

    /**
     * Tests the combined CRUD operation upon the scheduler.
     *
     * @throws Exception to JUnit.
     */
    public void testCRUD() throws Exception {
        long start = System.currentTimeMillis();
        StressTestHelper.crud(scheduler, REPEAT_COUNT);
        long end = System.currentTimeMillis();
        System.out.println("Combined " + REPEAT_COUNT + " CRUD operations upon ConfigManagerScheduler takes "
            + (end - start) + " ms.");
    }

}
