/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling.stresstests;

import junit.framework.TestCase;

import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;

/**
 * Unit test cases for the new features in <code>Job</code> class version 3.1.
 * 
 * @author 80x86
 * @version 3.1
 */
public class Job31Tests extends TestCase {

    /**
     * <p>
     * The <code>Job</code> instance for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    protected void setUp() throws Exception {
        // create the job instance.
        job = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "dir");
        job.setAttribute("key1", "value1");
        job.setAttribute("key2", "value2");
    }

    /**
     * <p>
     * Stress test for method <code>getRunningJob()</code>.
     * </p>
     */
    public void testGetRunningJob() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            assertNull("Fails to get the running job.", job.getRunningJob());
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling getRunningJob for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>getAttribute(String)</code>.
     * </p>
     */
    public void testGetAttribute() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            assertEquals("Fails to get the attribute.", "value1", job.getAttribute("key1"));
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling getAttribute for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>setAttribute(String, Object)</code>.
     * </p>
     */
    public void testSetAttribute() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            job.setAttribute("key" + i, "value");
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling setAttribute for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>getAttributeNames()</code>.
     * </p>
     */
    public void testGetAttributeNames() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            assertEquals("Fails to get the attribute names.", 2, job.getAttributeNames().length);
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling getAttributeNames for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>removeAttribute(String)</code>.
     * </p>
     */
    public void testRemoveAttribute() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            job.removeAttribute("key1");
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling removeAttribute for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>clearAttributes()</code>.
     * </p>
     * <p>
     * It tests that all the attributes could be cleared successfully.
     * </p>
     */
    public void testClearAttributes() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            job.clearAttributes();
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling clearAttributes for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

}
