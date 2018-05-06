/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.stresstests;

import java.io.File;
import java.util.Iterator;

import com.topcoder.management.phase.autopilot.AutoPilot;
import com.topcoder.management.phase.autopilot.AutoPilotJob;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;


/**
 * This TestCase aggregates all stress test cases for Auto Pilot Component.
 *
 * @author idx
 * @version 1.0
 */
public class AutoPilotStressTests extends TestCase {

    /** The number of times to run each test case. */
    private static final int RUN_NUM = 10000;

    /**
     * A <code>AutoPilot</code> instance used for tests.
     */
    private AutoPilot autoPilot = null;

    /**
     * A <code>AutoPilotJob</code> instance used for tests.
     */
    private AutoPilotJob autoPilotJob = null;

    /**
     * Set up.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        // Release AutoPilotJob.scheduler.
        TestHelper.releaseSingletonInstance(AutoPilotJob.class, "scheduler");

        tearDown();
        ConfigManager.getInstance().add("StressTests" + File.separator + "AutoPilot.xml");
        autoPilot = new AutoPilot();
        autoPilotJob = new AutoPilotJob(autoPilot, "add");
    }

    /**
     * Tear down.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {
            cm.removeNamespace((String) it.next());
        }
    }

    /**
     * Stress test for method <code>run()</code> of <code>AutoPilotJob</code>.
     */
    public void testRun() {
        long current = System.currentTimeMillis();
        for (int i = 0; i < RUN_NUM; ++i) {
            autoPilotJob.run();
        }
        System.out.println("Test AutoPilotJob#run() for " + RUN_NUM + " times.");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");
        System.out.println();
    }

    /**
     * Stress test for method <code>schedule(String, String, int)</code> of <code>AutoPilotJob</code>.
     * @throws Exception to JUnit
     */
    public void testSchedule() throws Exception {
        long current = System.currentTimeMillis();
        for (int i = 0; i < RUN_NUM; ++i) {
            AutoPilotJob.schedule(AutoPilotJob.class.getName(), "MyJob", 1);
        }
        System.out.println("Test AutoPilotJob#schedule(String, String, int) for " + RUN_NUM + " times.");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");
        System.out.println();
    }

    /**
     * Stress test for method <code>advanceProject(long, String)</code> of <code>AutoPilot</code>.
     * @throws Exception to JUnit
     */
    public void testAdvanceProject() throws Exception {
        long current = System.currentTimeMillis();
        for (int i = 0; i < RUN_NUM; ++i) {
            autoPilot.advanceProject(1, "add");
        }
        System.out.println("Test AutoPilot#advanceProject(long, String) for " + RUN_NUM + " times.");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");
        System.out.println();
    }

    /**
     * Stree test for method <code>advanceProjects(String)</code> of <code>AutoPilot</code>.
     * @throws Exception to JUnit
     */
    public void testAdvanceProjectsString() throws Exception {
        long current = System.currentTimeMillis();
        for (int i = 0; i < RUN_NUM; ++i) {
            autoPilot.advanceProjects("add");
        }
        System.out.println("Test AutoPilot#advanceProjects(String) for " + RUN_NUM + " times.");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");
        System.out.println();
    }

    /**
     * Stree test for method <code>advanceProjects(long[], String)</code> of <code>AutoPilot</code>.
     * @throws Exception to JUnit
     */
    public void testAdvanceProjectsLongArrayString() throws Exception {
        long current = System.currentTimeMillis();
        for (int i = 0; i < RUN_NUM; ++i) {
            autoPilot.advanceProjects(new long[] {1, 2, 3, 4, 5}, "add");
        }
        System.out.println("Test AutoPilot#advanceProjects(long[], String) for " + RUN_NUM + " times.");
        System.out.println("It took " + (System.currentTimeMillis() - current) + " ms.");
        System.out.println();
    }

}
