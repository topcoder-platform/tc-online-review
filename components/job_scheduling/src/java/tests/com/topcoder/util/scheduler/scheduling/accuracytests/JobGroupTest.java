/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;

/**
 * <p>
 * Accuracy tests of <code>{@link JobGroup}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class JobGroupTest extends TestCase {
    /**
     * <p>
     * Represents the command for this job.
     * </p>
     */
    private static final String COMMAND = "mkdir test_files\\accuracytests\\temp";

    /**
     * <p>
     * Represents the JobGroup name.
     * </p>
     */
    private static final String GROUP_NAME = "JUNIT_GRP";

    /**
     * <p>
     * Represents the <code>{@link JobGroup}</code> instance for testing.
     * </p>
     */
    private JobGroup jobGroup;

    /**
     * <p>
     * Represents the list of jobs.
     * </p>
     */
    private List jobs;

    /**
     * <p>
     * Represents the <code>{@link Job}</code> for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     */
    protected void setUp() {
        job = new Job("FOLDER_CREATION", JobType.JOB_TYPE_EXTERNAL, COMMAND);
        jobs = new ArrayList();
        jobs.add(job);

        jobGroup = new JobGroup(GROUP_NAME, jobs);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        jobGroup = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(JobGroupTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link JobGroup#JobGroup(String, List)}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     */
    public void testJobGroup() {
        assertNotNull("failed to create JobGroup", jobGroup);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link JobGroup#getName()}</code>.
     * </p>
     * <p>
     * Expects the same which is set during construction.
     * </p>
     */
    public void testGetName() {
        assertEquals("failed to get the group name", GROUP_NAME, jobGroup.getName());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link JobGroup#getJobs()}</code>.
     * </p>
     * <p>
     * Expects the same which is set during construction.
     * </p>
     */
    public void testGetJobs() {
        assertEquals("failed to get jobs from job group", job, jobGroup.getJobs()[0]);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link JobGroup#addHandler(EventHandler)}</code>,
     * <code>{@link JobGroup#getHandlers()}</code> and
     * <code>{@link JobGroup#removeHandler(EventHandler)}</code>.
     * </p>
     * <p>
     * Verifies all 3 functions are working as desired.
     * </p>
     */
    public void testHandlers() {
        EventHandler handler = new MockEventHandler();
        jobGroup.addHandler(handler);
        assertEquals("failed to add handler", 1, jobGroup.getHandlers().length);
        jobGroup.removeHandler(handler);
        assertEquals("failed to remove handler", 0, jobGroup.getHandlers().length);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link JobGroup#fireEvent(Job, String)}</code>.
     * </p>
     * <p>
     * Verifies that event is fired properly.
     * </p>
     */
    public void testFireEvent() {
        MockEventHandler handler = new MockEventHandler();
        jobGroup.addHandler(handler);
        jobGroup.fireEvent(job, EventHandler.FAILED);
        assertEquals("failed to fire the event properly", EventHandler.FAILED, handler.getEvent());
        assertEquals("failed to fire the event properly", job, handler.getJob());
    }
}
