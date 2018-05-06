/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;

import junit.framework.TestCase;

import java.util.GregorianCalendar;

/**
 * This class contains unit tests for Job class.
 *
 * @author King_Bette
 * @version 3.1
 * @since 3.0
 */
public class FailureTestJob extends TestCase {
    /** The Job instance used to test. */
    private Job job = null;

    /**
     * Set Up the test environment before testing.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        job = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "dir");
    }

    /**
     * Clean up the test environment after testing.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        job = null;
    }

    /**
     * Tests <code>Job(Job job)</code> method for failure with null Job.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testJobNullJob() throws Exception {
        try {
            new Job(null);
            fail("testJobNullJob is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>Job(String jobName, JobType jobType, String runCommand)</code>
     * method for failure with null JobName. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testJobNullJobName() throws Exception {
        try {
            new Job(null, JobType.JOB_TYPE_EXTERNAL, "dir");
            fail("testJobNullJobName is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>Job(String jobName, JobType jobType, String runCommand)</code>
     * method for failure with empty JobName, IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testJobEmptyJobName() throws Exception {
        try {
            new Job(" ", JobType.JOB_TYPE_EXTERNAL, "dir");
            fail("testJobEmptyJobName is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>Job(String jobName, JobType jobType, String runCommand)</code>
     * method for failure with null JobType. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testJobNullJobType() throws Exception {
        try {
            new Job("jobName", null, "dir");
            fail("testJobNullJobType is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>Job(String jobName, JobType jobType, String runCommand)</code>
     * method for failure with null RunCommand. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testJobNullRunCommand() throws Exception {
        try {
            new Job("jobName", JobType.JOB_TYPE_EXTERNAL, null);
            fail("testJobNullRunCommand is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>Job(String jobName, JobType jobType, String runCommand)</code>
     * method for failure with empty RunCommand, IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testJobEmptyRunCommand() throws Exception {
        try {
            new Job("jobName", JobType.JOB_TYPE_EXTERNAL, " ");
            fail("testJobEmptyRunCommand is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setStartDate(GregorianCalendar startDate)</code> method for
     * failure with null StartDate. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetStartDateNullStartDate() throws Exception {
        try {
            job.setStartDate(null);
            fail("testSetStartDateNullStartDate is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setStartDate(GregorianCalendar startDate)</code> method for
     * failure with invalid StartDate. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetStartDateInvalidStartDate() throws Exception {
        GregorianCalendar cal = new GregorianCalendar(2, 2, 2);
        job.setStopDate(cal);

        try {
            job.setStartDate(new GregorianCalendar(3, 3, 3));
            fail("testSetStartDateInvalidStartDate is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setName(String name)</code> method for failure with null
     * Name. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetNameNullName() throws Exception {
        try {
            job.setName(null);
            fail("testSetNameNullName is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setName(String name)</code> method for failure with empty
     * Name, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetNameEmptyName() throws Exception {
        try {
            job.setName(" ");
            fail("testSetNameEmptyName is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setStopDate(GregorianCalendar stopDate)</code> method for
     * failure with null StopDate. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetStopDateInvalidStopDate() throws Exception {
        GregorianCalendar cal = new GregorianCalendar(2, 2, 2);
        job.setStartDate(cal);

        try {
            job.setStopDate(new GregorianCalendar(1, 1, 1));
            fail("testSetStopDateInvalidStopDate is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setIntervalValue(int intervalValue)</code> method for
     * failure with invalid IntervalValue. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetIntervalValueInvalidIntervalValue() throws Exception {
        try {
            job.setIntervalValue(0);
            fail("testSetIntervalValueInvalidIntervalValue is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setRunCommand(String runCommand)</code> method for failure
     * with null RunCommand. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetRunCommandNullRunCommand() throws Exception {
        try {
            job.setRunCommand(null);
            fail("testSetRunCommandNullRunCommand is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setRunCommand(String runCommand)</code> method for failure
     * with empty RunCommand, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetRunCommandEmptyRunCommand() throws Exception {
        try {
            job.setRunCommand(" ");
            fail("testSetRunCommandEmptyRunCommand is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setRunCommand(String runCommand)</code> method for failure
     * with invalid RunCommand, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetRunCommandInvalidRunCommand() throws Exception {
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        try {
            job.setRunCommand("xxx");
            fail("testSetRunCommandEmptyRunCommand is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setStatus(String status)</code> method for failure with
     * null Status. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetStatusNullStatus() throws Exception {
        try {
            job.setStatus(null);
            fail("testSetStatusNullStatus is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setStatus(String status)</code> method for failure with
     * empty Status, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetStatusEmptyStatus() throws Exception {
        try {
            job.setStatus(" ");
            fail("testSetStatusEmptyStatus is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setIntervalUnit(DateUnit intervalUnit)</code> method for
     * failure with null IntervalUnit. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetIntervalUnitNullIntervalUnit() throws Exception {
        try {
            job.setIntervalUnit(null);
            fail("testSetIntervalUnitNullIntervalUnit is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>addHandler(EventHandler handler)</code> method for failure
     * with null Handler. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddHandlerNullHandler() throws Exception {
        try {
            job.addHandler(null);
            fail("testAddHandlerNullHandler is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>removeHandler(EventHandler handler)</code> method for
     * failure with null Handler. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveHandlerNullHandler() throws Exception {
        try {
            job.removeHandler(null);
            fail("testRemoveHandlerNullHandler is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>addGroup(JobGroup group)</code> method for failure with
     * null Group. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddGroupNullGroup() throws Exception {
        try {
            job.addGroup(null);
            fail("testAddGroupNullGroup is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>removeGroup(JobGroup group)</code> method for failure with
     * null Group. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveGroupNullGroup() throws Exception {
        try {
            job.removeGroup(null);
            fail("testRemoveGroupNullGroup is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setStartTime(int startTime)</code> method for failure with
     * invalid StartTime. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetStartTimeInvalidStartTime() throws Exception {
        try {
            job.setStartTime(0);
            fail("testSetStartTimeInvalidStartTime is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setStartTime(int startTime)</code> method for failure with
     * invalid StartTime. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetStartTimeInvalidStartTime2() throws Exception {
        try {
            job.setStartTime(999999990);
            fail("testSetStartTimeInvalidStartTime2 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setRecurrence(int recurrence)</code> method for failure
     * with invalid Recurrence. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetRecurrenceInvalidRecurrence() throws Exception {
        try {
            job.setRecurrence(-1);
            fail("testSetRecurrenceInvalidRecurrence is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setJobType(JobType jobType)</code> method for failure with
     * null JobType. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetJobTypeNullJobType() throws Exception {
        try {
            job.setJobType(null);
            fail("testSetJobTypeNullJobType is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setExecutionCount(int executionCount)</code> method for
     * failure with invalid ExecutionCount. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetExecutionCountInvalidExecutionCount() throws Exception {
        try {
            job.setExecutionCount(-1);
            fail("testSetExecutionCountInvalidExecutionCount is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setExecutionCount(int executionCount)</code> method for
     * failure with invalid ExecutionCount. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetExecutionCountInvalidExecutionCount2() throws Exception {
        try {
            job.setExecutionCount(999999999);
            fail("testSetExecutionCountInvalidExecutionCount2 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getAttribute(String name)</code> method for failure with
     * null name. IllegalArgumentException should be thrown.
     */
    public void testGetAttributeNullName() {
        try {
            job.getAttribute(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getAttribute(String name)</code> method for failure with
     * empty name. IllegalArgumentException should be thrown.
     */
    public void testGetAttributeEmptyName() {
        try {
            job.getAttribute("   ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setAttribute(String name, Object value)</code> method for
     * failure with null name. IllegalArgumentException should be thrown.
     */
    public void testSetAttributeNullName() {
        try {
            job.setAttribute(null, new Object());
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setAttribute(String name, Object value)</code> method for
     * failure with empty name. IllegalArgumentException should be thrown.
     */
    public void testSetAttributeEmptyName() {
        try {
            job.setAttribute("   ", new Object());
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>setAttribute(String name, Object value)</code> method for
     * failure with null value. IllegalArgumentException should be thrown.
     */
    public void testSetAttributeNullValue() {
        try {
            job.setAttribute("test", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>removeAttribute(String name)</code> method for failure with
     * null name. IllegalArgumentException should be thrown.
     */
    public void testRemoveAttributeNullName() {
        try {
            job.removeAttribute(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>removeAttribute(String name)</code> method for failure with
     * empty name. IllegalArgumentException should be thrown.
     */
    public void testRemoveAttributeEmptyName() {
        try {
            job.removeAttribute("   ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }
}
