/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Failure test cases for <code>JobGroup</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class FailureTestJobGroup extends TestCase {
    /** JobGroup instance of test. */
    private JobGroup jobGroup = null;

    /** Job list for test. */
    private List jobList;

    /**
     * Set up for test.
     */
    public void setUp() {
        createTestJobList();
        jobGroup = new JobGroup("group name", jobList);
    }

    /**
     * Test the constructor with illegal arguments. IllegalArgumentException
     * will be thrown.
     */
    public void testJobGroupStringStringNull() {
        try {
            jobGroup = new JobGroup(null, jobList);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the constructor with illegal arguments. IllegalArgumentException
     * will be thrown.
     */
    public void testJobGroupStringStringNull2() {
        try {
            jobGroup = new JobGroup("group name2", null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the constructor with illegal arguments. Put null value in the job
     * list. IllegalArgumentException will be thrown.
     */
    public void testJobGroupStringStringIllegal() {
        try {
            jobList.add(null);
            jobGroup = new JobGroup("group name2", jobList);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the constructor with illegal arguments. Put non-Job object in the
     * job list. IllegalArgumentException will be thrown.
     */
    public void testJobGroupStringStringIllegal2() {
        try {
            jobList.add("non-Job");
            jobGroup = new JobGroup("group name2", jobList);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the addHandler with illegal arguments. Give null value.
     * IllegalArgumentException will be thrown.
     */
    public void testAddHandlerNull() {
        try {
            jobGroup = new JobGroup("group name2", jobList);
            jobGroup.addHandler(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the removeHandler with illegal arguments. Give null value.
     * IllegalArgumentException will be thrown.
     */
    public void testRemoveHandlerNull() {
        try {
            jobGroup = new JobGroup("group name2", jobList);
            jobGroup.removeHandler(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the fireEvent with illegal arguments. Give null job.
     * IllegalArgumentException will be thrown.
     */
    public void testFireEventNull() {
        try {
            jobGroup = new JobGroup("group name2", jobList);
            jobGroup.fireEvent(null, EventHandler.FAILED);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the fireEvent with illegal arguments. Give null event.
     * IllegalArgumentException will be thrown.
     */
    public void testFireEventNull2() {
        try {
            Job job = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "dir");
            jobGroup = new JobGroup("group name2", jobList);
            jobGroup.fireEvent(job, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Test the fireEvent with illegal arguments. Give illegal event value.
     * IllegalArgumentException will be thrown.
     */
    public void testFireEventIllegal() {
        try {
            Job job = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "dir");
            jobGroup = new JobGroup("group name2", jobList);
            jobGroup.fireEvent(job, "illegal value");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception raised
        }
    }

    /**
     * Create a jobList for tests.
     */
    private void createTestJobList() {
        Job jobA = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "dir");

        Job jobB = new Job("jobName2", JobType.JOB_TYPE_EXTERNAL, "dir");
        jobList = new ArrayList();
        jobList.add(jobA);
        jobList.add(jobB);
    }
}
