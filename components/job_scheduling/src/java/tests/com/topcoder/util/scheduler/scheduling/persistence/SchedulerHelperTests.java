/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.persistence;

import com.topcoder.util.file.Template;
import com.topcoder.util.scheduler.scheduling.Day;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.SchedulingException;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for SchedulerHelper.
 * </p>
 * <p>
 * Changes in version 3.1: it
 * </p>
 * @author TCSDEVELOPER
 * @author TCSDEVELOEPR
 * @version 3.1
 * @since 3.0
 */
public class SchedulerHelperTests extends TestCase {
    /**
     * <p>
     * SchedulerHelper instance for testing.
     * </p>
     */
    private SchedulerHelper helper;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     */
    protected void setUp() {
        helper = new SchedulerHelper();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     */
    protected void tearDown() {
        helper = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(SchedulerHelperTests.class);
    }

    /**
     * <p>
     * Tests SchedulerHelper#createJob() for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#createJob() is correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testCreateJobWithDependence() throws Exception {
        helper.setJobName("newJobName01");
        helper.setJobType("JOB_TYPE_JAVA_CLASS");
        helper.setJobCommand("com.topcoder.util.scheduler.scheduling.MyJob");
        helper.setActive("True");
        helper.setModificationDate("Jan 5, 2007 05:00:00 AM");
        helper.setRecurrence("4");
        helper.setInterval("2");
        helper.setDateUnit("com.topcoder.util.scheduler.scheduling.Week");
        helper.setDependenceJobName("jobName07");
        helper.setDependenceJobStatus("SUCCESSFUL");
        helper.setDependenceJobDelay("10000");

        Job job = helper.createJob();

        assertEquals("Failed to create the job correctly.", "newJobName01", job
                .getName());
        assertEquals("Failed to create the job correctly.",
                JobType.JOB_TYPE_JAVA_CLASS, job.getJobType());
        assertEquals("Failed to create the job correctly.", 10000, job
                .getDependence().getDelay());
        assertEquals("Failed to create the job correctly.", true, job
                .getActive());
        assertEquals("Failed to create the job correctly.", 4, job
                .getRecurrence());
        assertEquals("Failed to create the job correctly.", 2, job
                .getIntervalValue());
        assertEquals("Failed to create the job correctly.", "jobName07", job
                .getDependence().getDependentJobName());
        assertEquals("Failed to create the job correctly.",
                EventHandler.SUCCESSFUL, job.getDependence()
                        .getDependentEvent());
        assertEquals("Failed to create the job correctly.",
                "com.topcoder.util.scheduler.scheduling.MyJob", job
                        .getRunCommand());

    }

    /**
     * <p>
     * Tests SchedulerHelper#createJob() for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#createJob() is correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testCreateJobWithoutDependence() throws Exception {
        helper.setJobName("jobName07");
        helper.setStartDate("May 5, 2007 05:00:00 AM");
        helper.setStartTime("3000000");
        helper.setEndDate("May 5, 2007 05:00:00 AM");
        helper.setJobType("JOB_TYPE_EXTERNAL");
        helper.setJobCommand("dir");
        helper.setActive("True");
        helper.setRecurrence("5");
        helper.setInterval("2");
        helper.setDateUnit("com.topcoder.util.scheduler.scheduling.Week");
        helper.setModificationDate("Jan 5, 2007 05:00:00 AM");

        Job job = helper.createJob();

        assertEquals("Failed to create the job correctly.", "jobName07", job
                .getName());
        assertEquals("Failed to create the job correctly.",
                JobType.JOB_TYPE_EXTERNAL, job.getJobType());
        assertEquals("Failed to create the job correctly.", "dir", job
                .getRunCommand());
        assertEquals("Failed to create the job correctly.", true, job
                .getActive());
        assertEquals("Failed to create the job correctly.", 5, job
                .getRecurrence());
        assertEquals("Failed to create the job correctly.", 2, job
                .getIntervalValue());

    }

    /**
     * <p>
     * Tests SchedulerHelper#createJob() for failure.
     * </p>
     * <p>
     * It tests the case when active is invalid and expects SchedulingException.
     * </p>
     */
    public void testCreateJob_InvalidActive() {
        helper.setActive("invalid");
        try {
            helper.createJob();
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests SchedulerHelper#createJob() for failure.
     * </p>
     * <p>
     * It tests the case when startTime is invalid and expects
     * SchedulingException.
     * </p>
     */
    public void testCreateJob_InvalidStartTime() {
        helper.setStartTime("0");
        try {
            helper.createJob();
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests SchedulerHelper#createJob() for failure.
     * </p>
     * <p>
     * It tests the case when interval is invalid and expects
     * SchedulingException.
     * </p>
     */
    public void testCreateJob_InvalidInterval() {
        helper.setInterval("-2");
        try {
            helper.createJob();
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests SchedulerHelper#createJob() for failure.
     * </p>
     * <p>
     * It tests the case when recurrence is invalid and expects
     * SchedulingException.
     * </p>
     */
    public void testCreateJob_InvalidRecurrence() {
        helper.setRecurrence("-5");
        try {
            helper.createJob();
            fail("SchedulingException expected.");
        } catch (SchedulingException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests SchedulerHelper#parseJobType(JobType) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#pareJobType(JobType) is correct.
     * </p>
     */
    public void testParseJobType1() {
        assertEquals("Failed to pare job type correctly.", "JOB_TYPE_EXTERNAL",
                SchedulerHelper.parseJobType(JobType.JOB_TYPE_EXTERNAL));
    }

    /**
     * <p>
     * Tests SchedulerHelper#parseJobType(String) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#pareJobType(String) is correct.
     * </p>
     * @throws SchedulingException to JUnit
     */
    public void testParseJobType2() throws SchedulingException {
        assertEquals("Failed to pare job type correctly.",
                JobType.JOB_TYPE_EXTERNAL, SchedulerHelper
                        .parseJobType("JOB_TYPE_EXTERNAL"));
    }

    /**
     * <p>
     * Tests SchedulerHelper#parseJobType(JobType) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#pareJobType(JobType) is correct.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_JAVA_CLASS</code>.
     * </p>
     * @since 3.1
     */
    public void testParseJobType3() {
        assertEquals("Failed to pare job type correctly.",
                "JOB_TYPE_JAVA_CLASS", SchedulerHelper
                        .parseJobType(JobType.JOB_TYPE_JAVA_CLASS));
    }

    /**
     * <p>
     * Tests SchedulerHelper#parseJobType(String) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#pareJobType(String) is correct.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_JAVA_CLASS</code>.
     * </p>
     * @throws SchedulingException to JUnit
     * @since 3.1
     */
    public void testParseJobType4() throws SchedulingException {
        assertEquals("Failed to pare job type correctly.",
                JobType.JOB_TYPE_JAVA_CLASS, SchedulerHelper
                        .parseJobType("JOB_TYPE_JAVA_CLASS"));
    }

    /**
     * <p>
     * Tests SchedulerHelper#parseJobType(JobType) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#pareJobType(JobType) is correct.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>.
     * </p>
     * @since 3.1
     */
    public void testParseJobType5() {
        assertEquals(
                "Failed to pare job type correctly.",
                "JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY",
                SchedulerHelper
                        .parseJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY));
    }

    /**
     * <p>
     * Tests SchedulerHelper#parseJobType(String) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#pareJobType(String) is correct.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>.
     * </p>
     * @throws SchedulingException to JUnit
     * @since 3.1
     */
    public void testParseJobType6() throws SchedulingException {
        assertEquals(
                "Failed to pare job type correctly.",
                JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY,
                SchedulerHelper
                        .parseJobType("JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY"));
    }

    /**
     * <p>
     * Tests SchedulerHelper#parseJobType(JobType) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#pareJobType(JobType) is correct.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>.
     * </p>
     * @since 3.1
     */
    public void testParseJobType7() {
        assertEquals(
                "Failed to pare job type correctly.",
                "JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE",
                SchedulerHelper
                        .parseJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE));
    }

    /**
     * <p>
     * Tests SchedulerHelper#parseJobType(String) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#pareJobType(String) is correct.
     * </p>
     * <p>
     * The job type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>.
     * </p>
     * @throws SchedulingException to JUnit
     * @since 3.1
     */
    public void testParseJobType8() throws SchedulingException {
        assertEquals(
                "Failed to pare job type correctly.",
                JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE,
                SchedulerHelper
                        .parseJobType("JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE"));
    }

    /**
     * <p>
     * Tests SchedulerHelper#getDateFormat() for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#getDateFormat() is correct.
     * </p>
     */
    public void testGetDateFormat() {
        assertNotNull("Failed to pare job type correctly.", SchedulerHelper
                .getDateFormat());
    }

    /**
     * <p>
     * Tests SchedulerHelper#parseDateUnit(DateUnit) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#parseDateUnit(DateUnit) is correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testParseDateUnit() throws Exception {
        String[] dates = SchedulerHelper.parseDateUnit(new Day());
        assertEquals("Failed to parse date unit correctly.", 4, dates.length);
        assertEquals("Failed to parse date unit correctly.",
                "com.topcoder.util.scheduler.scheduling.Day", dates[0]);
        assertNull("Failed to parse date unit correctly.", dates[1]);
        assertNull("Failed to parse date unit correctly.", dates[2]);
        assertNull("Failed to parse date unit correctly.", dates[3]);
    }

    /**
     * <p>
     * Tests SchedulerHelper#createDefaultTemplate() for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#createDefaultTemplate() is correct.
     * </p>
     */
    public void testCreateDefaultTemplate() {
        String templateText = "Hi,\nThis email notifies you that the job %JobName%"
                + " has the status %JobStatus% now.";

        SchedulerHelper.createDefaultTemplate();
        assertEquals("Failed to create default template correctly.",
                templateText, SchedulerHelper.createDefaultTemplate()
                        .getTemplate());

    }

    /**
     * <p>
     * Tests SchedulerHelper#readTextToTemplate(String) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#readTextToTemplate(String) is correct.
     * </p>
     * @throws SchedulingException to JUnit
     */
    public void testReadTextToTemplate() throws SchedulingException {
        String templateText = "Hi,\nThis email notifies you that the job %JobName%"
                + " has the status %JobStatus% now.";
        Template template = SchedulerHelper.readTextToTemplate(templateText);

        assertEquals("Failed to read text to template.", templateText, template
                .getTemplate());
    }

    /**
     * <p>
     * Tests SchedulerHelper#parseBooleanValue(boolean) for accuracy.
     * </p>
     * <p>
     * It verifies SchedulerHelper#parseBooleanValue(boolean) is correct.
     * </p>
     */
    public void testParseBooleanValue() {
        assertEquals("Failed to read text to template.", "True",
                SchedulerHelper.parseBooleanValue(true));
    }

}