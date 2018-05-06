/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling.stresstests.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.Week;
import com.topcoder.util.scheduler.scheduling.persistence.ConfigurationObjectScheduler;
import com.topcoder.util.scheduler.scheduling.stresstests.StressTestHelper31;

/**
 * Stress test for class <code>ConfigurationObjectScheduler</code>.
 * 
 * @author 80x86
 * @version 3.1
 */
public class ConfigurationObjectSchedulerTests extends TestCase {

    /**
     * Represents the <code>ConfigurationObjectScheduler</code> used for testing.
     */
    private ConfigurationObjectScheduler scheduler = null;

    /**
     * Represents the <code>ConfigurationObject</code> instance used to create the
     * <code>ConfigurationObjectScheduler</code>.
     */
    private ConfigurationObject root = null;

    /**
     * The job used for testing.
     */
    private Job job1 = null;

    /**
     * The job group used for testing.
     */
    private JobGroup group1 = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    protected void setUp() throws Exception {
        root = createRootConfigurationObject();
        scheduler = new ConfigurationObjectScheduler(root);
        job1 = scheduler.getJob("Job1");
        group1 = scheduler.getGroup("Group1");
    }

    /**
     * <p>
     * Stress test for constructor <code>ConfigurationObjectScheduler(ConfigurationObject)</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testConfigurationObjectScheduler() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            assertNotNull("Fails to create ConfigurationObjectScheduler.", new ConfigurationObjectScheduler(root));
        }
        long end = System.currentTimeMillis();
        System.out.println("Constructs ConfigurationObjectScheduler for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>addJob(Job)</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testAddJob() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            Job newJob = createNewJob("Job" + (10 + i), JobType.JOB_TYPE_EXTERNAL, "dir");
            scheduler.addJob(newJob);
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the addJob method for " + StressTestHelper31.REPEAT_COUNT + " times takes "
                + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>updateJob(Job)</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testUpdateJob() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            Job newJob = createNewJob("Job1", JobType.JOB_TYPE_EXTERNAL, "rm XX");
            newJob.setActive(false);
            newJob.setIntervalValue(10 + i);
            newJob.setStopDate(new GregorianCalendar());
            scheduler.updateJob(newJob);
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the updateJob method for " + StressTestHelper31.REPEAT_COUNT + " times takes "
                + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>deleteJob(String)</code>.
     * </p>
     * <p>
     * Verifies that the job could be deleted successfully.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testDeleteJob() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            Job newJob = createNewJob("Job" + (10 + i), JobType.JOB_TYPE_EXTERNAL, "dir");
            scheduler.addJob(newJob);
            scheduler.deleteJob(newJob);
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the addJob and deleteJob method for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>getJob(String)</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testGetJob() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            scheduler.getJob("job1");
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the getJob method for " + StressTestHelper31.REPEAT_COUNT + " times takes "
                + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>getJobList()</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testGetJobList() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            Job[] jobs = scheduler.getJobList();
            assertEquals("Fails to get job list.", 2, jobs.length);
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the getJobList method for " + StressTestHelper31.REPEAT_COUNT + " times takes "
                + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>getAllDependentJobs(Job)</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testGetAllDependentJobs() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            Job[] jobs = scheduler.getAllDependentJobs(job1);
            assertEquals("Fails to get all dependent jobs.", 1, jobs.length);
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the getAllDependentJobs method for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>addGroup(JobGroup)</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testAddGroup() throws Exception {
        List jobs = new ArrayList();
        jobs.add(job1);
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            scheduler.addGroup(new JobGroup("group" + (10 + i), jobs));
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the addGroup method for " + StressTestHelper31.REPEAT_COUNT + " times takes "
                + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>updateGroup(JobGroup)</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testUpdateGroup() throws Exception {
        List jobs = new ArrayList();
        jobs.add(job1);
        group1 = new JobGroup("Group1", jobs);
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            scheduler.updateGroup(group1);
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the updateGroup method for " + StressTestHelper31.REPEAT_COUNT + " times takes "
                + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>deleteGroup(String)</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testDeleteGroup() throws Exception {
        List jobs = new ArrayList();
        jobs.add(job1);
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            scheduler.addGroup(new JobGroup("group" + (10 + i), jobs));
            scheduler.deleteGroup("group" + (10 + i));
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the addGroup and deleteGroup method for " + StressTestHelper31.REPEAT_COUNT
                + " times takes " + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>getAllGroups()</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testGetAllGroups() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            assertEquals("Fails to get all groups.", 1, scheduler.getAllGroups().length);
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the getAllGroups method for " + StressTestHelper31.REPEAT_COUNT + " times takes "
                + (end - start) + " ms.");
    }

    /**
     * <p>
     * Stress test for method <code>getGroup(String)</code>.
     * </p>
     * 
     * @throws Exception
     *             if there is any problem.
     */
    public void testGetGroup() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < StressTestHelper31.REPEAT_COUNT; i++) {
            scheduler.getGroup("Group1");
        }
        long end = System.currentTimeMillis();
        System.out.println("Calling the getGroup method for " + StressTestHelper31.REPEAT_COUNT + " times takes "
                + (end - start) + " ms.");
    }

    /**
     * Returns the configuration object used for testing.
     * 
     * @return a root configuration object for testing.
     * @throws Exception
     *             if there is any problem.
     */
    private ConfigurationObject createRootConfigurationObject() throws Exception {
        root = new DefaultConfigurationObject("root");

        // set the Logger property.
        root.setPropertyValue("Logger", "test");

        // add two Job child configuration.
        root.addChild(createJobConfigurationObject("Job1"));
        root.addChild(createDependentJobConfigurationObject("Job4", "Job1"));

        // add DefinedGroups child configuration.
        ConfigurationObject definedGroups = new DefaultConfigurationObject("DefinedGroups");
        definedGroups.addChild(createJobGroupConfigurationObject("Group1", new String[] { "Job1" }));

        root.addChild(definedGroups);
        return root;
    }

    /**
     * Returns a job configuration object with basic property.
     * 
     * @param jobName
     *            the name of a job to create.
     * @return a job configuration object with basic property.
     * @throws Exception
     *             if there is any problem.
     */
    private ConfigurationObject createBasicJobConfigurationObject(String jobName) throws Exception {
        ConfigurationObject job = new DefaultConfigurationObject(jobName);

        // set the properties.
        job.setPropertyValue("JobType", "JOB_TYPE_EXTERNAL");
        job.setPropertyValue("JobCommand", "dir");
        job.setPropertyValue("Active", "True");
        job.setPropertyValue("Recurrence", "10");

        // set the interval.
        ConfigurationObject interval = new DefaultConfigurationObject("Interval");
        interval.setPropertyValue("Value", "2");
        ConfigurationObject unit = new DefaultConfigurationObject("Unit");
        unit.setPropertyValue("Type", "com.topcoder.util.scheduler.scheduling.WeekMonthOfYear");
        unit.setPropertyValue("DateUnitDays", "1");
        unit.setPropertyValue("DateUnitWeek", "1");
        unit.setPropertyValue("DateUnitMonth", "1");
        interval.addChild(unit);
        job.addChild(interval);

        job.setPropertyValue("ModificationDate", "Oct 1, 2007 01:23:45 AM");

        return job;
    }

    /**
     * Returns a dependent job configuration object.
     * 
     * @param jobName
     *            name of the job to create.
     * @param dependedJobName
     *            the name of depended job.
     * @return a dependent job configuration object.
     * @throws Exception
     *             if there is any problem.
     */
    private ConfigurationObject createDependentJobConfigurationObject(String jobName, String dependedJobName)
            throws Exception {

        // get the basic job.
        ConfigurationObject job = createBasicJobConfigurationObject(jobName);

        // add the dependence.
        ConfigurationObject dependence = new DefaultConfigurationObject("Dependence");
        ConfigurationObject dependedJob = new DefaultConfigurationObject(dependedJobName);
        dependedJob.setPropertyValue("Status", "BOTH");
        dependedJob.setPropertyValue("Delay", "1000");
        dependence.addChild(dependedJob);
        job.addChild(dependence);
        return job;
    }

    /**
     * Returns a independent job configuration object.
     * 
     * @param jobName
     *            the name of the job.
     * @return a independent job configuration object.
     * @throws Exception
     *             if there is any problem.
     */
    private ConfigurationObject createJobConfigurationObject(String jobName) throws Exception {
        // get the basic job.
        ConfigurationObject job = createBasicJobConfigurationObject(jobName);

        // set the scheduled time.
        job.setPropertyValue("StartDate", "Oct 8, 2007 01:00:00 AM");
        job.setPropertyValue("StartTime", "3000000");
        job.setPropertyValue("EndDate", "Oct 9, 2007 01:00:00 AM");
        return job;
    }

    /**
     * Returns a group configuration object.
     * 
     * @param groupNames
     *            the name of the group.
     * @param jobNames
     *            the names of jobs in this group.
     * @return a group configuration object.
     * @throws Exception
     *             if there is any problem.
     */
    private ConfigurationObject createJobGroupConfigurationObject(String groupNames, String[] jobNames)
            throws Exception {
        ConfigurationObject jobGroup = new DefaultConfigurationObject(groupNames);
        // set the jobs
        jobGroup.setPropertyValues("Jobs", jobNames);
        return jobGroup;
    }

    /**
     * Creates a new job independent on another job.
     * 
     * @param jobName
     *            the job name.
     * @param jobType
     *            the job type.
     * @param runCommand
     *            the command used to create the running object.
     * @return a new dependent job.
     */
    private Job createNewJob(String jobName, JobType jobType, String runCommand) {
        // create basic job
        Job job = new Job(jobName, jobType, runCommand);
        job.setActive(true);
        job.setIntervalUnit(new Week());
        job.setIntervalValue(2);
        job.setRecurrence(10);
        job.setModificationDate(new GregorianCalendar());
        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar stopDate = (GregorianCalendar) startDate.clone();
        stopDate.add(Calendar.DATE, 10);
        job.setStartDate(startDate);
        job.setStartTime(3000000);
        job.setStopDate(stopDate);
        return job;
    }
}
