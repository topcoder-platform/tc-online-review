/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for JobGroup.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class JobGroupTests extends TestCase {
    /**
     * <p>
     * The SimpleEventHandler instance for testing.
     * </p>
     */
    private SimpleEventHandler handler;

    /**
     * <p>
     * The JobGroup instance for testing.
     * </p>
     */
    private JobGroup jobGroup;

    /**
     * <p>
     * The Job instance for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * The jobs list for testing.
     * </p>
     */
    private List jobs;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     */
    protected void setUp() {
        handler = new SimpleEventHandler();
        job = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "dir");
        jobs = new ArrayList();
        jobs.add(job);

        jobGroup = new JobGroup("name", jobs);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        jobGroup = null;
        jobs = null;
        job = null;
        handler = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(JobGroupTests.class);
    }

    /**
     * <p>
     * Tests ctor JobGroup#JobGroup(String,List) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created JobGroup instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new JobGroup instance.", jobGroup);
    }

    /**
     * <p>
     * Tests ctor JobGroup#JobGroup(String,List) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullName() {
        try {
            new JobGroup(null, jobs);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor JobGroup#JobGroup(String,List) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when name is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyName() {
        try {
            new JobGroup(" ", jobs);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor JobGroup#JobGroup(String,List) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobs is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullJobs() {
        try {
            new JobGroup("name", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests JobGroup#JobGroup(String,List) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobs is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyJobs() {
        jobs = new ArrayList();
        try {
            new JobGroup("name", jobs);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests JobGroup#JobGroup(String,List) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobs contains null element and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullInJobs() {
        jobs.add(null);
        try {
            new JobGroup("name", jobs);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests JobGroup#JobGroup(String,List) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobs contains element which is not of Job type and expects
     * IllegalArgumentException.
     * </p>
     */
    public void testCtor_InvalidTypeInJobs() {
        jobs.add("invalid");
        try {
            new JobGroup("name", jobs);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests JobGroup#fireEvent(Job,String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies JobGroup#fireEvent(Job,String) is correct.
     * </p>
     */
    public void testFireEvent() {
        jobGroup.addHandler(handler);
        jobGroup.fireEvent(job, EventHandler.SUCCESSFUL);

        assertTrue("Failed to fire event correctly.", handler.getIsExecute());
    }

    /**
     * <p>
     * Tests JobGroup#fireEvent(Job,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job is null and expects IllegalArgumentException.
     * </p>
     */
    public void testFireEvent_NullJob() {
        try {
            jobGroup.fireEvent(null, EventHandler.SUCCESSFUL);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests JobGroup#fireEvent(Job,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is null and expects IllegalArgumentException.
     * </p>
     */
    public void testFireEvent_NullEvent() {
        try {
            jobGroup.fireEvent(job, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests JobGroup#fireEvent(Job,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testFireEvent_EmptyEvent() {
        try {
            jobGroup.fireEvent(job, " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests JobGroup#fireEvent(Job,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is invalid and expects IllegalArgumentException.
     * </p>
     */
    public void testFireEvent_InvalidEvent() {
        try {
            jobGroup.fireEvent(job, "invalid");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests JobGroup#getJobs() for accuracy.
     * </p>
     *
     * <p>
     * It verifies JobGroup#getJobs() is correct.
     * </p>
     */
    public void testGetJobs() {
        Job[] newJobs = jobGroup.getJobs();
        assertEquals("Expected the length of the jobs is one.", 1, newJobs.length);
        assertEquals("Failed to get the jobs correctly.", job, newJobs[0]);
    }

    /**
     * <p>
     * Tests JobGroup#getName() for accuracy.
     * </p>
     *
     * <p>
     * It verifies JobGroup#getName() is correct.
     * </p>
     */
    public void testGetName() {
        assertEquals("Failed to get the name correctly.", "name", jobGroup.getName());
    }

    /**
     * <p>
     * Tests JobGroup#addHandler(EventHandler) for accuracy.
     * </p>
     *
     * <p>
     * It verifies JobGroup#addHandler(EventHandler) is correct.
     * </p>
     */
    public void testAddHandler() {
        jobGroup.addHandler(handler);

        assertEquals("Expected the length of the handlers is one.", 1, jobGroup.getHandlers().length);
        assertEquals("Failed to add the handler correctly.", handler, jobGroup.getHandlers()[0]);
    }

    /**
     * <p>
     * Tests JobGroup#addHandler(EventHandler) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when handler is null and expects IllegalArgumentException.
     * </p>
     */
    public void testAddHandler_NullHandler() {
        try {
            jobGroup.addHandler(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests JobGroup#getHandlers() for accuracy.
     * </p>
     *
     * <p>
     * It verifies JobGroup#getHandlers() is correct.
     * </p>
     */
    public void testGetHandlers() {
        jobGroup.addHandler(handler);

        assertEquals("Expected the length of the handlers is one.", 1, jobGroup.getHandlers().length);
        assertEquals("Failed to get the handlers correctly.", handler, jobGroup.getHandlers()[0]);
    }

    /**
     * <p>
     * Tests JobGroup#removeHandler(EventHandler) for accuracy.
     * </p>
     *
     * <p>
     * It verifies JobGroup#removeHandler(EventHandler) is correct.
     * </p>
     */
    public void testRemoveHandler() {
        jobGroup.addHandler(handler);
        jobGroup.removeHandler(handler);

        assertEquals("Failed to remove the handler correctly.", 0, jobGroup.getHandlers().length);
    }

    /**
     * <p>
     * Tests JobGroup#removeHandler(EventHandler) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when handler is null and expects IllegalArgumentException.
     * </p>
     */
    public void testRemoveHandler_NullHandler() {
        try {
            jobGroup.removeHandler(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

}