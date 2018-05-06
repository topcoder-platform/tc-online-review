/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * @(#)ConfigManagerSchedulerStressTest.java
 */
package com.topcoder.util.scheduler.scheduling.stresstests;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;

import junit.framework.TestCase;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.scheduler.scheduling.persistence.ConfigManagerScheduler;

/**
 * Stress test of ConfigManagerScheduler class.
 *
 * @author  smell
 * @version 3.0
 */
public class ConfigManagerSchedulerStressTest extends TestCase {

    /**
     * Repeat count of the stress test.
     */
    private static final int REPEAT_COUNT = 50;
    /**
     * Directory to the test files.
     */
    private static final String TEST_FILES_DIR = "test_files/";
    /**
     * Configuration file of the ConfigManagerScheduler.
     */
    private static final String CONFIG_FILE = "stresstests/CMConfig.xml";
    /**
     * The Configuration file which the ConfigManagerScheduler is working on.
     * It will be created before each test, and delete after the running of each test.
     */
    private static final String WORK_FILE = "stresstests/work.xml";
    /**
     * The namespace used to create the ConfigManagerScheduler.
     */
    private static final String NAMESPACE = "com.topcoder.util.scheduler.scheduling.stresstests.ConfigManagerScheduler";
    /**
     * The ConfigManagerScheduler instance to test.
     */
    private ConfigManagerScheduler scheduler = null;

    /**
     * Creates the configuration file and creates the ConfigManagerScheduler instance.
     *
     * @throws Exception to JUnit.
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        copyFile(TEST_FILES_DIR + WORK_FILE, TEST_FILES_DIR + CONFIG_FILE);
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(WORK_FILE);

        scheduler = new ConfigManagerScheduler(NAMESPACE);
    }

    /**
     * Clears ConfigManager and deletes the configuration file.
     * @throws Exception
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator iterator = cm.getAllNamespaces(); iterator.hasNext();) {
            String ns = (String) iterator.next();
            cm.removeNamespace(ns);
        }

        File file = new File(TEST_FILES_DIR + WORK_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Helper method that copies file.
     *
     * @param to    the destination of the file.
     * @param from  the source of the file.
     * @throws Exception    if the operation fails.
     */
    private static void copyFile(String to, String from) throws Exception {
        Reader reader = null;
        Writer writer = null;
        try {
            int bufSize = 1 << 12;
            reader = new FileReader(from);
            writer = new FileWriter(to);
            char[] buffer = new char[bufSize];
            for (int read = reader.read(buffer); read > 0; read = reader.read(buffer)) {
                writer.write(buffer, 0, read);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
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
