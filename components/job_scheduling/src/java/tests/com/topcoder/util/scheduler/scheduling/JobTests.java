/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for Job.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class JobTests extends TestCase {
    /**
     * <p>
     * The Job instance for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * The SimpleEventHandler instance for testing.
     * </p>
     */
    private SimpleEventHandler handler;

    /**
     * <p>
     * The recipients list for testing.
     * </p>
     */
    private List recipients;

    /**
     * <p>
     * The JobGroup instance for testing.
     * </p>
     */
    private JobGroup jobGroup;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    @Override
    protected void setUp() throws Exception {
        List jobs = new ArrayList();
        handler = new SimpleEventHandler();
        recipients = new ArrayList();
        recipients.add("abc@topcoder.com");

        job = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "dir");

        jobs.add(job);
        jobGroup = new JobGroup("name", jobs);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    @Override
    protected void tearDown() {
        jobGroup = null;
        job = null;
        recipients = null;
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
        return new TestSuite(JobTests.class);
    }

    /**
     * <p>
     * Tests ctor Job#Job(String,JobType,String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created Job instance should not be null.
     * </p>
     */
    public void testCtor1() {
        assertNotNull("Failed to create a new Job instance.", job);
    }

    /**
     * <p>
     * Tests ctor Job#Job(String,JobType,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobName is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor1_NullJobName() {
        try {
            new Job(null, JobType.JOB_TYPE_EXTERNAL, "dir");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Job#Job(String,JobType,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobName is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor1_EmptyJobName() {
        try {
            new Job(" ", JobType.JOB_TYPE_EXTERNAL, "dir");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Job#Job(String,JobType,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobType is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor1_NullJobType() {
        try {
            new Job("jobName", null, "dir");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Job#Job(String,JobType,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when runCommand is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor1_NullRunCommand() {
        try {
            new Job("jobName", JobType.JOB_TYPE_EXTERNAL, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Job#Job(String,JobType,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when runCommand is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor1_EmptyRunCommand() {
        try {
            new Job("jobName", JobType.JOB_TYPE_EXTERNAL, " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Job#Job(Job) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created Job instance should not be null.
     * </p>
     */
    public void testCtor2() {
        assertNotNull("Failed to create a new Job instance.", new Job(job));
    }

    /**
     * <p>
     * Tests ctor Job#Job(Job) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor2_NullJob() {
        try {
            new Job(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#fireEvent(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#fireEvent(String) is correct.
     * </p>
     */
    public void testFireEvent() {
        job.addHandler(handler);
        job.fireEvent(EventHandler.SUCCESSFUL);
        assertTrue("Failed to fire event correctly.", handler.getIsExecute());
    }

    /**
     * <p>
     * Tests Job#addGroup(JobGroup) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#addGroup(JobGroup) is correct.
     * </p>
     */
    public void testAddGroup() {
        job.addGroup(jobGroup);
        assertEquals("Expected the length of the jobs is one.", 1, job.getGroups().length);
        assertEquals("Failed to add the group correctly.", jobGroup, job.getGroups()[0]);
    }

    /**
     * <p>
     * Tests Job#addGroup(JobGroup) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when group is null and expects IllegalArgumentException.
     * </p>
     */
    public void testAddGroup_NullGroup() {
        try {
            job.addGroup(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#getMessageData() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getMessageData() is correct.
     * </p>
     */
    public void testGetMessageData() {
        assertNull("Failed to get the message data correctly.", job.getMessageData());
    }

    /**
     * <p>
     * Tests Job#getRunningStatus() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getRunningStatus() is correct.
     * </p>
     */
    public void testGetRunningStatus() {
        assertEquals("Failed to get the running status correctly.", ScheduledEnable.NOT_STARTED,
            job.getRunningStatus());
    }

    /**
     * <p>
     * Tests Job#setJobType(JobType) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setJobType(JobType) is correct.
     * </p>
     */
    public void testSetJobType() {
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        assertEquals("Failed to set the job type correctly.", JobType.JOB_TYPE_JAVA_CLASS, job.getJobType());
    }

    /**
     * <p>
     * Tests Job#setJobType(JobType) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobType is null and expects IllegalArgumentException.
     * </p>
     */
    public void testSetJobType_NullJobType() {
        try {
            job.setJobType(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#setRunCommand(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setRunCommand(String) is correct.
     * </p>
     */
    public void testSetRunCommand() {
        job.setRunCommand("dir");
        assertEquals("Failed to set the run command correctly.", "dir", job.getRunCommand());
    }

    /**
     * <p>
     * Tests Job#setRunCommand(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when runCommand is null and expects IllegalArgumentException.
     * </p>
     */
    public void testSetRunCommand_NullRunCommand() {
        try {
            job.setRunCommand(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#setRunCommand(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when runCommand is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testSetRunCommand_EmptyRunCommand() {
        try {
            job.setRunCommand(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#getStartDate() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getStartDate() is correct.
     * </p>
     */
    public void testGetStartDate() {
        job.setStartDate(new GregorianCalendar(2007, 0, 01, 01, 00, 00));
        assertEquals("Failed to get start date correctly.", new GregorianCalendar(2007, 0, 01, 01, 00, 00),
            job.getStartDate());
    }

    /**
     * <p>
     * Tests Job#setStartDate(GregorianCalendar) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setStartDate(GregorianCalendar) is correct.
     * </p>
     */
    public void testSetStartDate() {
        job.setStartDate(new GregorianCalendar(2007, 0, 01, 01, 00, 00));
        assertEquals("Failed to set start date correctly.", new GregorianCalendar(2007, 0, 01, 01, 00, 00),
            job.getStartDate());
    }

    /**
     * <p>
     * Tests Job#setStartDate(GregorianCalendar) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when startDate is null and expects IllegalArgumentException.
     * </p>
     */
    public void testSetStartDate_NullStartDate() {
        try {
            job.setStartDate(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#getStopDate() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getStopDate() is correct.
     * </p>
     */
    public void testGetStopDate() {
        job.setStopDate(new GregorianCalendar(2007, 0, 01, 01, 00, 00));
        assertEquals("Failed to get stop date correctly.", new GregorianCalendar(2007, 0, 01, 01, 00, 00),
            job.getStopDate());
    }

    /**
     * <p>
     * Tests Job#setStopDate(GregorianCalendar) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setStopDate(GregorianCalendar) is correct.
     * </p>
     */
    public void testSetStopDate() {
        job.setStopDate(new GregorianCalendar(2007, 0, 01, 01, 00, 00));
        assertEquals("Failed to set stop date correctly.", new GregorianCalendar(2007, 0, 01, 01, 00, 00),
            job.getStopDate());
    }

    /**
     * <p>
     * Tests Job#getIntervalValue() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getIntervalValue() is correct.
     * </p>
     */
    public void testGetIntervalValue() {
        job.setIntervalValue(5);
        assertEquals("Failed to get the interval value correctly.", 5, job.getIntervalValue());
    }

    /**
     * <p>
     * Tests Job#setIntervalValue(int) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setIntervalValue(int) is correct.
     * </p>
     */
    public void testSetIntervalValue() {
        job.setIntervalValue(5);
        assertEquals("Failed to set the interval value correctly.", 5, job.getIntervalValue());
    }

    /**
     * <p>
     * Tests Job#setIntervalValue(int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when intervalValue is invalid and expects IllegalArgumentException.
     * </p>
     */
    public void testSetIntervalValue_InvalidIntervalValue() {
        try {
            job.setIntervalValue(-5);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#getRunCommand() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getRunCommand() is correct.
     * </p>
     */
    public void testGetRunCommand() {
        job.setRunCommand("dir");
        assertEquals("Failed to get the run command correctly.", "dir", job.getRunCommand());
    }

    /**
     * <p>
     * Tests Job#getLastRun() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getLastRun() is correct.
     * </p>
     */
    public void testGetLastRun() {
        job.setLastRun(new GregorianCalendar(2007, 0, 01, 01, 00, 00));
        assertEquals("Failed to get last run correctly.", new GregorianCalendar(2007, 0, 01, 01, 00, 00),
            job.getLastRun());
    }

    /**
     * <p>
     * Tests Job#setLastRun(GregorianCalendar) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setLastRun(GregorianCalendar) is correct.
     * </p>
     */
    public void testSetLastRun() {
        job.setLastRun(new GregorianCalendar(2007, 0, 01, 01, 00, 00));
        assertEquals("Failed to set last run correctly.", new GregorianCalendar(2007, 0, 01, 01, 00, 00),
            job.getLastRun());
    }

    /**
     * <p>
     * Tests Job#getNextRun() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getNextRun() is correct.
     * </p>
     */
    public void testGetNextRun() {
        job.setNextRun(new GregorianCalendar(2007, 0, 01, 01, 00, 00));
        assertEquals("Failed to get next run correctly.", new GregorianCalendar(2007, 0, 01, 01, 00, 00),
            job.getNextRun());
    }

    /**
     * <p>
     * Tests Job#setNextRun(GregorianCalendar) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setNextRun(GregorianCalendar) is correct.
     * </p>
     */
    public void testSetNextRun() {
        job.setNextRun(new GregorianCalendar(2007, 0, 01, 01, 00, 00));
        assertEquals("Failed to set next run correctly.", new GregorianCalendar(2007, 0, 01, 01, 00, 00),
            job.getNextRun());
    }

    /**
     * <p>
     * Tests Job#getJobType() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getJobType() is correct.
     * </p>
     */
    public void testGetJobType() {
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        assertEquals("Failed to get the job type correctly.", JobType.JOB_TYPE_JAVA_CLASS, job.getJobType());
    }

    /**
     * <p>
     * Tests Job#getIntervalUnit() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getIntervalUnit() is correct.
     * </p>
     */
    public void testGetIntervalUnit() {
        Day day = new Day();
        job.setIntervalUnit(day);

        assertSame("Failed to get the interval unit correctly.", day, job.getIntervalUnit());
    }

    /**
     * <p>
     * Tests Job#setIntervalUnit(DateUnit) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setIntervalUnit(DateUnit) is correct.
     * </p>
     */
    public void testSetIntervalUnit() {
        Week week = new Week();
        job.setIntervalUnit(week);

        assertSame("Failed to get the interval unit correctly.", week, job.getIntervalUnit());
    }

    /**
     * <p>
     * Tests Job#setIntervalUnit(DateUnit) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when intervalUnit is null and expects IllegalArgumentException.
     * </p>
     */
    public void testSetIntervalUnit_NullIntervalUnit() {
        try {
            job.setIntervalUnit(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#getDependence() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getDependence() is correct.
     * </p>
     */
    public void testGetDependence() {
        Dependence dependence = new Dependence("jobName", "FAILED", 1);
        job.setDependence(dependence);
        assertSame("Failed to get the dependence correctly.", dependence, job.getDependence());
    }

    /**
     * <p>
     * Tests Job#setDependence(Dependence) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setDependence(Dependence) is correct.
     * </p>
     */
    public void testSetDependence() {
        Dependence dependence = new Dependence("jobName", "FAILED", 1);

        job.setDependence(dependence);
        assertSame("Failed to set the dependence correctly.", dependence, job.getDependence());
    }

    /**
     * <p>
     * Tests Job#getGroups() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getGroups() is correct.
     * </p>
     */
    public void testGetGroups() {
        job.addGroup(jobGroup);
        assertEquals("Expected the length of the jobs is one.", 1, job.getGroups().length);
        assertEquals("Failed to get the groups correctly.", jobGroup, job.getGroups()[0]);
    }

    /**
     * <p>
     * Tests Job#removeGroup(JobGroup) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#removeGroup(JobGroup) is correct.
     * </p>
     */
    public void testRemoveGroup() {
        job.addGroup(jobGroup);
        job.removeGroup(jobGroup);

        assertEquals("Failed to remove the groups correctly.", 0, job.getGroups().length);
    }

    /**
     * <p>
     * Tests Job#getRecurrence() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getRecurrence() is correct.
     * </p>
     */
    public void testGetRecurrence() {
        job.setRecurrence(5);
        assertEquals("Failed to get the recurrence correctly.", 5, job.getRecurrence());
    }

    /**
     * <p>
     * Tests Job#setRecurrence(int) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setRecurrence(int) is correct.
     * </p>
     */
    public void testSetRecurrence() {
        job.setRecurrence(5);
        assertEquals("Failed to set the recurrence correctly.", 5, job.getRecurrence());
    }

    /**
     * <p>
     * Tests Job#getExecutionCount() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getExecutionCount() is correct.
     * </p>
     */
    public void testGetExecutionCount() {
        job.setRecurrence(15);
        job.setExecutionCount(5);
        assertEquals("Failed to get the execution count correctly.", 5, job.getExecutionCount());
    }

    /**
     * <p>
     * Tests Job#setExecutionCount(int) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setExecutionCount(int) is correct.
     * </p>
     */
    public void testSetExecutionCount() {
        job.setRecurrence(15);
        job.setExecutionCount(5);
        assertEquals("Failed to set the execution count correctly.", 5, job.getExecutionCount());
    }

    /**
     * <p>
     * Tests Job#setExecutionCount(int) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the execution count is larger than recurrence and expects
     * IllegalArgumentException.
     * </p>
     */
    public void testSetExecutionCount_Invalid() {
        try {
            job.setExecutionCount(5);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#getActive() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getActive() is correct.
     * </p>
     */
    public void testGetActive() {
        job.setActive(true);
        assertTrue("Failed to get the active correctly.", job.getActive());
    }

    /**
     * <p>
     * Tests Job#getAsyncMode() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getAsyncMode() is correct.
     * </p>
     */
    public void testGetAsyncMode() {
        job.setAsyncMode(true);
        assertTrue("Failed to get the asyncMode correctly.", job.isAsyncMode());
    }

    /**
     * <p>
     * Tests Job#getModificationDate() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getModificationDate() is correct.
     * </p>
     */
    public void testGetModificationDate() {
        job.setModificationDate(new GregorianCalendar(2007, 0, 01, 01, 00, 00));
        assertEquals("Failed to get modification date correctly.", new GregorianCalendar(2007, 0, 01, 01, 00, 00),
            job.getModificationDate());
    }

    /**
     * <p>
     * Tests Job#setModificationDate(GregorianCalendar) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setModificationDate(GregorianCalendar) is correct.
     * </p>
     */
    public void testSetModificationDate() {
        job.setModificationDate(new GregorianCalendar(2007, 0, 01, 01, 00, 00));
        assertEquals("Failed to set modification date correctly.", new GregorianCalendar(2007, 0, 01, 01, 00, 00),
            job.getModificationDate());
    }

    /**
     * <p>
     * Tests Job#getName() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getName() is correct.
     * </p>
     */
    public void testGetName() {
        job.setName("name");
        assertEquals("Failed to get the name correctly.", "name", job.getName());
    }

    /**
     * <p>
     * Tests Job#start() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#start() is correct.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testStart() throws Exception {
        try {
            File file = new File("test_files/testStart.txt");
            file.delete();
            assertFalse(file.isFile());
            Job j = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "cmd /c dir > test_files/testStart.txt");
            j.start();

            // Wait for creation of file.
            Thread.sleep(1000);
            assertTrue(file.isFile());
            file.delete();
        } catch (SchedulingException e) {
            fail("SchedulingException should not be thrown.");
        }

    }

    /**
     * <p>
     * Tests Job#setName(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setName(String) is correct.
     * </p>
     */
    public void testSetName() {
        job.setName("name");
        assertEquals("Failed to set the name correctly.", "name", job.getName());
    }

    /**
     * <p>
     * Tests Job#setName(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     */
    public void testSetName_NullName() {
        try {
            job.setName(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#setName(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when name is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testSetName_EmptyName() {
        try {
            job.setName(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#stop() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#stop() is correct.
     * </p>
     */
    public void testStop() {
        job.stop();

        // no exception will be thrown.
    }

    /**
     * <p>
     * Tests Job#addHandler(EventHandler) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#addHandler(EventHandler) is correct.
     * </p>
     */
    public void testAddHandler() {
        job.addHandler(handler);
        assertEquals("Failed to add the handler correctly.", 1, job.getHandlers().length);
        assertEquals("Failed to add the handler correctly.", handler, job.getHandlers()[0]);
    }

    /**
     * <p>
     * Tests Job#addHandler(EventHandler) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when handler is null and expects IllegalArgumentException.
     * </p>
     */
    public void testAddHandler_NullHandler() {
        try {
            job.addHandler(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#getHandlers() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getHandlers() is correct.
     * </p>
     */
    public void testGetHandlers() {
        job.addHandler(handler);
        assertEquals("Failed to add the handler correctly.", 1, job.getHandlers().length);
        assertEquals("Failed to add the handler correctly.", handler, job.getHandlers()[0]);
    }

    /**
     * <p>
     * Tests Job#removeHandler(EventHandler) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#removeHandler(EventHandler) is correct.
     * </p>
     */
    public void testRemoveHandler() {
        job.addHandler(handler);
        job.removeHandler(handler);
        assertEquals("Failed to remove the handlers correctly.", 0, job.getHandlers().length);
    }

    /**
     * <p>
     * Tests Job#removeHandler(EventHandler) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when handler is null and expects IllegalArgumentException.
     * </p>
     */
    public void testRemoveHandler_NullHandler() {
        try {
            job.removeHandler(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Job#getStartTime() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getStartTime() is correct.
     * </p>
     */
    public void testGetStartTime() {
        job.setStartTime(5);
        assertEquals("Failed to get the start time correctly.", 5, job.getStartTime());
    }

    /**
     * <p>
     * Tests Job#setStartTime(int) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setStartTime(int) is correct.
     * </p>
     */
    public void testSetStartTime() {
        job.setStartTime(5);
        assertEquals("Failed to set the start time correctly.", 5, job.getStartTime());
    }

    /**
     * <p>
     * Tests Job#setActive(boolean) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setActive(boolean) is correct.
     * </p>
     */
    public void testSetActive() {
        job.setActive(true);
        assertTrue("Failed to get the active correctly.", job.getActive());
    }


    /**
     * <p>
     * Tests Job#setAsyncMode(boolean) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setAsyncMode(boolean) is correct.
     * </p>
     */
    public void testSetAsyncMode() {
        job.setAsyncMode(true);
        assertTrue("Failed to get the asyncMode correctly.", job.isAsyncMode());
    }

    /**
     * <p>
     * Tests Job#getStatus() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#getStatus() is correct.
     * </p>
     */
    public void testGetStatus() {
        job.setStatus("status");
        assertEquals("Failed to get the status correctly.", "status", job.getStatus());
    }

    /**
     * <p>
     * Tests Job#setStatus(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Job#setStatus(String) is correct.
     * </p>
     */
    public void testSetStatus() {
        job.setStatus("status");
        assertEquals("Failed to set the status correctly.", "status", job.getStatus());
    }

}