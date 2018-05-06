/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.scheduler.scheduling.DateUnit;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Hour;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.ScheduledEnable;
import com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectFactory;
import com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectFactoryManager;
import com.topcoder.util.scheduler.scheduling.SchedulingException;

/**
 * <p>
 * Accuracy tests of <code>{@link Job}</code> class.
 * </p>
 *
 * @author mittu
 * @author peony
 * @version 3.1
 * @since 3.0
 */
public class JobTest extends TestCase {
    /**
     * <p>
     * Represents the command for this job.
     * </p>
     */
    private static final String COMMAND = "mkdir test_files\\accuracytests\\temp";

    /**
     * <p>
     * Represents the recipient email address.
     * </p>
     */
    private static final String EMAIL_TESTER = "abc@topcoder.com";

    /**
     * <p>
     * Represents the <code>{@link Job}</code> instance for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * The recipients list for testing.
     * </p>
     */
    private List recipients;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    protected void setUp() throws Exception {
        recipients = new ArrayList();
        recipients.add(EMAIL_TESTER);

        job = new Job("FOLDER_CREATION", JobType.JOB_TYPE_EXTERNAL, COMMAND);

        List jobs = new ArrayList();
        jobs.add(job);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to junit.
     */
    protected void tearDown() throws Exception {
        job = null;
        recipients = null;
        Thread.sleep(10);
        // delete file created during test
        File temp = new File("test_files\\accuracytests\\temp");
        temp.delete();
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(JobTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#Job(String, JobType, String)}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     */
    public void testJobCtr1() {
        assertNotNull("failed to create the Job", job);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#Job(Job)}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     */
    public void testJobCtr2() {
        assertNotNull("failed to create the Job", new Job(job));
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setStartDate(java.util.GregorianCalendar)}</code> and
     * <code>{@link Job#getStartDate()}</code>.
     * </p>
     * <p>
     * 1) Should be <code>null</code> by default. 2) Expects the same which is set.
     * </p>
     */
    public void testSetStartDate() {
        assertNull("start date should be null by default", job.getStartDate());

        GregorianCalendar calendar = new GregorianCalendar();
        job.setStartDate(calendar);
        assertEquals("failed to set/get the start date", calendar, job.getStartDate());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setName(String)}</code> and <code>{@link Job#getName()}</code>.
     * </p>
     * <p>
     * 1) Should be same which is set during construction by default. 2) Expects the same which is set.
     * </p>
     */
    public void testSetName() {
        assertEquals("failed to get the name by default", "FOLDER_CREATION", job.getName());

        job.setName("NEW_FOLDER_CREATION");
        assertEquals("failed to set/get the name", "NEW_FOLDER_CREATION", job.getName());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setStopDate(GregorianCalendar)}</code> and
     * <code>{@link Job#getStopDate()}</code>.
     * </p>
     * <p>
     * 1) Should be <code>null</code> by default. 2) Expects the same which is set.
     * </p>
     */
    public void testSetStopDate() {
        assertNull("stop date should be null by default", job.getStopDate());

        GregorianCalendar calendar = new GregorianCalendar();
        job.setStopDate(calendar);
        assertEquals("failed to set/get the stop date", calendar, job.getStopDate());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setIntervalValue(int)}</code> and
     * <code>{@link Job#getIntervalValue()}</code>.
     * </p>
     * <p>
     * Expects the same which is set.
     * </p>
     */
    public void testSetIntervalValue() {
        job.setIntervalValue(5);
        assertEquals("failed to get/set interval value", 5, job.getIntervalValue());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setRunCommand(String)}</code> and
     * <code>{@link Job#getRunCommand()}</code>.
     * </p>
     * <p>
     * 1) Should be same which is set during construction by default. 2) Expects the same which is set.
     * </p>
     */
    public void testSetRunCommand() {
        assertEquals("failed to get the run command by default", COMMAND, job.getRunCommand());

        job.setRunCommand("mkdirs");
        assertEquals("failed to get/set the run command", "mkdirs", job.getRunCommand());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setLastRun(GregorianCalendar)}</code> and
     * <code>{@link Job#getLastRun()}</code>.
     * </p>
     * <p>
     * 1) Should be <code>null</code> by default. 2) Expects the same which is set.
     * </p>
     */
    public void testSetLastRun() {
        assertNull("last run should be null by default", job.getLastRun());

        GregorianCalendar calendar = new GregorianCalendar();
        job.setLastRun(calendar);
        assertEquals("failed to set/get the last run", calendar, job.getLastRun());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setNextRun(GregorianCalendar)}</code> and
     * <code>{@link Job#getNextRun()}</code>.
     * </p>
     * <p>
     * 1) Should be <code>null</code> by default. 2) Expects the same which is set.
     * </p>
     */
    public void testSetNextRun() {
        assertNull("next run should be null by default", job.getNextRun());

        GregorianCalendar calendar = new GregorianCalendar();
        job.setNextRun(calendar);
        assertEquals("failed to set/get the next run", calendar, job.getNextRun());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#start()}</code> and <code>{@link Job#stop()}</code>.
     * </p>
     * <p>
     * Verifies that the command is executed properly without exception and stopped properly.
     * </p>
     */
    public void testStart() throws Exception {
        assertJobStart(job);

        // change the job type and command
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        job.setRunCommand("com.topcoder.util.scheduler.scheduling.accuracytests.MakeDirectory");

        assertJobStart(job);

        job.stop();
        assertEquals("failed to stop the job", ScheduledEnable.NOT_STARTED, job.getRunningStatus());
    }

    /**
     * A helper to check the same job for two different job types.
     *
     * @param job the job to start
     * @throws Exception to junit.
     */
    private void assertJobStart(Job job) throws Exception {
        try {
            File temp = new File("test_files\\accuracytests\\temp");
            temp.delete();
            assertFalse(temp.exists());
            job.start();
            // Wait for creation of file.
            Thread.sleep(1000);
            assertTrue(temp.exists());
            temp.delete();
        } catch (SchedulingException e) {
            fail("failed to start the job");
        }
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#fireEvent(String)}</code>.
     * </p>
     * <p>
     * Verifies that event is fired properly.
     * </p>
     */
    public void testFireEvent() {
        MockEventHandler handler = new MockEventHandler();
        job.addHandler(handler);
        job.fireEvent(EventHandler.FAILED);
        assertEquals("failed to fire the event properly", EventHandler.FAILED, handler.getEvent());
        assertEquals("failed to fire the event properly", job, handler.getJob());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#getRunningStatus()}</code>.
     * </p>
     * <p>
     * Verifies that the running statuses are correct.
     * </p>
     *
     * @throws Exception to junit.
     */
    public void testGetRunningStatus() throws Exception {
        // Not started
        assertEquals("failed to get the correct running status", ScheduledEnable.NOT_STARTED, job
            .getRunningStatus());
        job.start();
        // Running
        assertEquals("failed to get the correct running status", ScheduledEnable.RUNNING, job
            .getRunningStatus());

        job.stop();
        // Not started
        assertEquals("failed to get the correct running status", ScheduledEnable.NOT_STARTED, job
            .getRunningStatus());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#getMessageData()}</code>.
     * </p>
     * <p>
     * 1) Expects <code>null</code> by default 2) Run the job and expects non null message data.
     * </p>
     *
     * @throws Exception to junit.
     */
    public void testGetMessageData() throws Exception {
        assertNull("external jobs should not have message data", job.getMessageData());
        // change the job type and command
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        job.setRunCommand("com.topcoder.util.scheduler.scheduling.accuracytests.MakeDirectory");
        job.start();
        assertNotNull("failed to get message data", job.getMessageData());
    }

    /**
     * <p>
     * Accuracy test for
     * <code>{@link Job#setIntervalUnit(com.topcoder.util.scheduler.scheduling.DateUnit)}</code> and
     * <code>{@link Job#getIntervalUnit()}</code>.
     * </p>
     * <p>
     * Expects the same which is set.
     * </p>
     */
    public void testSetIntervalUnit() {
        DateUnit unit = new Hour();
        job.setIntervalUnit(unit);
        assertEquals("failed to set/get interval unit", unit, job.getIntervalUnit());
    }

    /**
     * <p>
     * Accuracy test for
     * <code>{@link Job#setDependence(com.topcoder.util.scheduler.scheduling.Dependence)}</code> and
     * <code>{@link Job#getDependence()}</code>.
     * </p>
     * <p>
     * Expects the same which is set.
     * </p>
     */
    public void testSetDependence() {
        Dependence dependence = new Dependence("JOB", EventHandler.SUCCESSFUL, 20);
        job.setDependence(dependence);
        assertEquals("failed to set/get dependence", dependence, job.getDependence());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#addHandler(EventHandler)}</code>,
     * <code>{@link Job#getHandlers()}</code> and <code>{@link Job#removeHandler(EventHandler)}</code>.
     * </p>
     * <p>
     * Verifies all 3 functions are working as desired.
     * </p>
     */
    public void testHandler() {
        EventHandler handler = new MockEventHandler();
        job.addHandler(handler);
        assertEquals("failed to add handler", 1, job.getHandlers().length);
        job.removeHandler(handler);
        assertEquals("failed to remove handler", 0, job.getHandlers().length);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#addGroup(JobGroup)}</code>, <code>{@link Job#getGroups()}</code>
     * and <code>{@link Job#removeGroup(JobGroup)}</code>.
     * </p>
     * <p>
     * Verifies all 3 functions are working as desired.
     * </p>
     */
    public void testGroups() {
        List jobs = new ArrayList();
        jobs.add(job);
        JobGroup group = new JobGroup("JUNIT_GRP", jobs);
        job.addGroup(group);
        assertEquals("failed to add job group", 1, job.getGroups().length);
        job.removeGroup(group);
        assertEquals("failed to remove job group", 0, job.getGroups().length);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setStartTime(int)}</code> and
     * <code>{@link Job#getStartTime()}</code>.
     * </p>
     * <p>
     * Expects the same which is set.
     * </p>
     */
    public void testSetStartTime() {
        job.setStartTime(100);
        assertEquals("failed to set/get start time", 100, job.getStartTime());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setRecurrence(int)}</code> and
     * <code>{@link Job#getRecurrence()}</code>.
     * </p>
     * <p>
     * Expects the same which is set.
     * </p>
     */
    public void testSetRecurrence() {
        job.setRecurrence(2);
        assertEquals("failed to set/get recurrence", 2, job.getRecurrence());
        assertEquals("failed to get correct execution count", 2, job.getExecutionCount());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setJobType(JobType)}</code> and
     * <code>{@link Job#getJobType()}</code>.
     * </p>
     * <p>
     * Expects the same which is set.
     * </p>
     */
    public void testSetJobType() {
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        assertEquals("failed to set/get job type", JobType.JOB_TYPE_JAVA_CLASS, job.getJobType());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setExecutionCount(int)}</code> and
     * <code>{@link Job#getExecutionCount()}</code>.
     * </p>
     * <p>
     * Expects the same which is set.
     * </p>
     */
    public void testSetExecutionCount() {
        job.setRecurrence(5);
        job.setExecutionCount(2);
        assertEquals("failed to set/get execution count", 2, job.getExecutionCount());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setActive(boolean)}</code> and
     * <code>{@link Job#getActive()}</code>.
     * </p>
     * <p>
     * Expects the same which is set.
     * </p>
     */
    public void testSetActive() {
        job.setActive(true);
        assertTrue("failed to set/get active", job.getActive());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Job#setModificationDate(GregorianCalendar)}</code> and
     * <code>{@link Job#getModificationDate()}</code>.
     * </p>
     * <p>
     * 1) Should be <code>null</code> by default. 2) Expects the same which is set.
     * </p>
     */
    public void testSetModificationDate() {
        assertNull("modification date should be null by default", job.getModificationDate());

        GregorianCalendar calendar = new GregorianCalendar();
        job.setModificationDate(calendar);
        assertEquals("failed to set/get the modification date", calendar, job.getModificationDate());
    }

    /**
     * <p>
     * Accuracy test for <code>clearAttributes()</code>.
     * </p>
     */
    public void testClearAttributes() {
        job.setAttribute("name", "value");
        job.clearAttributes();
        assertEquals("Failed to clear Attributes correctly.", 0, job.getAttributeNames().length);
    }

    /**
     * <p>
     * Accuracy test for <code>getAttribute(String name)</code>.
     * </p>
     */
    public void testGetAttribute() {
        job.setAttribute("name", "value");
        assertEquals("Failed to get Attribute correctly.", "value", job.getAttribute("name"));
    }

    /**
     * <p>
     * Accuracy test for <code>getAttributeNames()</code>.
     * </p>
     *
     */
    public void testGetAttributeNames() {
        job.setAttribute("name1", "value1");
        job.setAttribute("name2", "value2");
        String[] names = job.getAttributeNames();
        List rs = Arrays.asList(names);
        assertEquals("Failed to get Attribute names correctly.", 2, names.length);
        assertTrue("Failed to get Attribute names correctly.", rs.contains("name1"));
        assertTrue("Failed to get Attribute names correctly.", rs.contains("name2"));
    }

    /**
     * <p>
     * Accuracy test for <code>removeAttribute(String name)</code>.
     * </p>
     *
     */
    public void testRemoveAttribute() {
        job.setAttribute("name", "value");
        job.removeAttribute("name");
        assertNull("Failed to remove Attribute correctly.", job.getAttribute("name"));
    }

    /**
     * <p>
     * Accuracy test for <code>setAttribute(String name, Object value)</code>.
     * </p>
     */
    public void testSetAttribute() {
        job.setAttribute("name", "value");
        job.setAttribute("name", "value2");
        assertEquals("Failed to remove Attribute correctly.", "value2", job.getAttribute("name"));
    }

    /**
     * <p>
     * Accuracy test for <code>getRunningJob()<code>. The JobType is
     * <code>JOB_TYPE_EXTERNAL</code>.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetRunningJobA() throws Exception {
        try {
            job.start();
            assertNotNull("Failed to get runningJob correctly.", job.getRunningJob());
        } finally {
            job.stop();
        }
    }

    /**
     * <p>
     * Accuracy test for <code>getRunningJob()<code>.  The JobType is
     * <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetRunningJobAccuracyB() throws Exception {
        ScheduledEnableObjectFactory factory = new AccuracyScheduledEnableObjectFactory();
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("factory", factory);

        job = new Job("test", JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY, "factory");
        try {
            job.start();
            assertEquals("Failed to get runningJob correctly.", factory.createScheduledEnableObject(), job
                .getRunningJob());
        } finally {
            job.stop();
        }
    }

    /**
     * <p>
     * Accuracy test for <code>setRunCommand(String)</code>. The JobType is <code>JOB_TYPE_EXTERNAL</code>.
     * </p>
     */
    public void testSetRunCommandA() {
        job.setRunCommand("dir");
        assertEquals("Failed to set the run command correctly.", "dir", job.getRunCommand());
    }

    /**
     * <p>
     * Accuracy test for <code>setRunCommand(String)</code>. The JobType is
     * <code>JOB_TYPE_JAVA_CLASS</code>.
     * </p>
     *
     */
    public void testSetRunCommandB() {
        String runCommand = "com.topcoder.util.scheduler.scheduling.MyJob";
        job.setJobType(JobType.JOB_TYPE_JAVA_CLASS);
        job.setRunCommand(runCommand);
        assertEquals("Failed to set the run command correctly.", runCommand, job.getRunCommand());
    }

    /**
     * <p>
     * Tests Job#setRunCommand(String) for accuracy with job type is
     * <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>.
     * </p>
     *
     *
     * @throws Exception to JUnit
     */
    public void testSetRunCommandC() throws Exception {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            manager.add("accuracytests/objectfactory.xml");
            // set job type to JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE job
            // type.
            job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE);
            job.setRunCommand("setCommand");
            assertEquals("Failed to set the run command correctly.", "setCommand", job.getRunCommand());
        } finally {
            manager.removeNamespace("setCommand");
        }
    }

    /**
     * <p>
     * Tests Job#setRunCommand(String) for accuracy with job type is
     * <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>.
     * </p>
     *
     */
    public void testSetRunCommandD() {
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("factory",
            new AccuracyScheduledEnableObjectFactory());

        // set job type to JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY
        job.setJobType(JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY);
        job.setRunCommand("factory");
        assertEquals("Failed to set the run command correctly.", "factory", job.getRunCommand());
    }
}