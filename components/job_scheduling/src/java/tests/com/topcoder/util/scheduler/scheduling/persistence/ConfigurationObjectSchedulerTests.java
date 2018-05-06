/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.SynchronizedConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.TemplateFormatException;
import com.topcoder.util.file.xslttemplate.XsltTemplate;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.ConfigurationException;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EmailEventHandler;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.SchedulingException;
import com.topcoder.util.scheduler.scheduling.TestHelper;
import com.topcoder.util.scheduler.scheduling.Week;

/**
 * Unit test for class <code>ConfigurationObjectScheduler</code>.
 * @author fuyun
 * @version 3.1
 */
public class ConfigurationObjectSchedulerTests extends TestCase {

    /**
     * Represents the <code>ConfigurationObjectScheduler</code> used for
     * testing.
     */
    private ConfigurationObjectScheduler scheduler = null;

    /**
     * Represents the <code>ConfigurationObject</code> instance used to create
     * the <code>ConfigurationObjectScheduler</code>.
     */
    private ConfigurationObject root = null;

    /**
     * The job used for testing.
     */
    private Job job1 = null;

    /**
     * The job used for testing.
     */
    private Job job2 = null;

    /**
     * The job used for testing.
     */
    private Job job3 = null;

    /**
     * The job used for testing.
     */
    private Job job4 = null;

    /**
     * The job group used for testing.
     */
    private JobGroup group1 = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * <p>
     * Constructs the <code>ConfigurationObject</code> used to create the
     * <code>ConfigurationObjectScheduler</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    @Override
    protected void setUp() throws Exception {
        root = getRootConfigurationObject();
        scheduler = new ConfigurationObjectScheduler(root);
        job1 = scheduler.getJob("Job1");
        job2 = scheduler.getJob("Job2");
        job3 = scheduler.getJob("Job3");
        job4 = scheduler.getJob("Job4");
        group1 = scheduler.getGroup("Group1");
        job1.setAsyncMode(true);
    }

    /**
     * Cleans up the test environment.
     * @throws Exception if there is any problem.
     */
    @Override
    protected void tearDown() throws Exception {
        group1 = null;
        scheduler = null;
        job1 = null;
        job2 = null;
        job3 = null;
        job4 = null;
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ConfigurationObjectScheduler(ConfigurationObject)</code>.
     * </p>
     * <p>
     * Verifies that the instance is created and the inner fields are
     * initialized properly.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testConfigurationObjectSchedulerAccuracy() throws Exception {
        assertNotNull("Fails to create ConfigurationObjectScheduler.",
                scheduler);

        assertNotNull("Fails to initialize the logger field.", TestHelper
                .getPrivateFieldValue(scheduler,
                        ConfigurationObjectScheduler.class, "log"));
        assertTrue(
                "Fails to initialize the configuration field.",
                TestHelper.getPrivateFieldValue(scheduler,
                        ConfigurationObjectScheduler.class, "configuration")
                        instanceof SynchronizedConfigurationObject);

    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ConfigurationObjectScheduler(ConfigurationObject)</code>.
     * </p>
     * <p>
     * Verifies that the instance is created and the inner fields are
     * initialized properly.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testConfigurationObjectSchedulerAccuracyWrapped()
        throws Exception {
        assertNotNull("Fails to create ConfigurationObjectScheduler.",
                new ConfigurationObjectScheduler(
                        new SynchronizedConfigurationObject(root)));
    }

    /**
     * <p>
     * Accuracy test for constructor
     * <code>ConfigurationObjectScheduler(ConfigurationObject)</code>.
     * </p>
     * <p>
     * Verifies that the instance could be created when the Logger is not
     * configured.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testConfigurationObjectSchedulerAccuracyNoLoggerName()
        throws Exception {
        root.removeProperty("Logger");
        scheduler = null;
        scheduler = new ConfigurationObjectScheduler(root);
        assertNotNull("Fails to create ConfigurationObjectScheduler.",
                scheduler);

        assertNotNull("Fails to initialize the logger field.", TestHelper
                .getPrivateFieldValue(scheduler,
                        ConfigurationObjectScheduler.class, "log"));
        assertTrue(
                "Fails to initialize the configuration field.",
                TestHelper.getPrivateFieldValue(scheduler,
                ConfigurationObjectScheduler.class, "configuration") instanceof SynchronizedConfigurationObject);

    }

    /**
     * <p>
     * Failure test for constructor
     * <code>ConfigurationObjectScheduler(ConfigurationObject)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the argument is <code>null</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testConfigurationObjectSchedulerFailrueNullConfiguration()
        throws Exception {
        try {
            new ConfigurationObjectScheduler(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for constructor
     * <code>ConfigurationObjectScheduler(ConfigurationObject)</code>.
     * </p>
     * <p>
     * Verifies that <code>ConfigurationException</code> will be thrown if the
     * Logger is empty string in the configuration.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testConfigurationObjectSchedulerFailrueEmptyLoggerName()
        throws Exception {
        root.setPropertyValue("Logger", "    ");
        try {
            new ConfigurationObjectScheduler(root);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException ce) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>addJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that the job could be added successfully into the configuration.
     * The job is independent.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddJobAccuracyIndependent() throws Exception {
        Job newJob = createIndependentNewJob("Job5", JobType.JOB_TYPE_EXTERNAL,
                "dir");
        newJob.addGroup(group1);
        scheduler.addJob(newJob);
        compareJobAndConfiguration(newJob, root);
        // check the job and the group
        assertTrue("Fails to add the job to group.", isJobInGroups(newJob
                .getGroups(), newJob.getName()));
    }

    /**
     * <p>
     * Accuracy test for method <code>addJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that the job could be added successfully into the configuration.
     * The job is dependent.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddJobAccuracyDependent() throws Exception {
        Dependence dependence = new Dependence("Job1", Dependence.BOTH, 1000);
        Job newJob = createDependentNewJob("Job5", JobType.JOB_TYPE_EXTERNAL,
                "dir", dependence);
        scheduler.addJob(newJob);
        compareJobAndConfiguration(newJob, root);
    }

    /**
     * <p>
     * Failure test for method <code>addJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>IllegalArgumentException</code> will be thrown if the job is <code>null</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddJobFailureNullJob() throws Exception {
        try {
            scheduler.addJob(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>addJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>IllegalArgumentException</code> will be thrown if the job has invalid dependence.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddJobFailureInvalidDependence() throws Exception {
        try {
            Dependence dependence = new Dependence("Job", Dependence.BOTH, 1000);
            scheduler.addJob(createDependentNewJob("Job5",
                    JobType.JOB_TYPE_EXTERNAL, "dir", dependence));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>addJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>IllegalArgumentException</code> will be thrown if the job has the same name with existing job.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddJobFailureDuplicateJobName() throws Exception {
        try {
            scheduler.addJob(createIndependentNewJob("Job1",
                    JobType.JOB_TYPE_EXTERNAL, "dir"));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>addJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>IllegalArgumentException</code> will be thrown if the job has both dependence and scheduled time.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddJobFailureBothDependenceAndScheduledTime()
        throws Exception {
        try {
            Dependence dependence = new Dependence("Job1", Dependence.BOTH,
                    1000);
            Job newJob = createDependentNewJob("Job5",
                    JobType.JOB_TYPE_EXTERNAL, "dir", dependence);
            newJob.setStartDate(new GregorianCalendar());
            scheduler.addJob(newJob);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>addJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>IllegalArgumentException</code> will be thrown if the both dependence and scheduled time do not exist.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddJobFailureNoDependenceAndScheduledTime()
        throws Exception {
        try {
            Job newJob = createNewJob("Job5", JobType.JOB_TYPE_EXTERNAL, "dir");
            scheduler.addJob(newJob);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>addJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>SchedulingException</code> will be thrown if the job has group but there is no DefinedGroups configuration.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddJobFailureNoDefinedJobGroup() throws Exception {
        root.removeChild("DefinedGroups");
        List jobs = new ArrayList();
        jobs.add(job3);
        JobGroup group = new JobGroup("group", jobs);
        Job job = createIndependentNewJob("job", JobType.JOB_TYPE_EXTERNAL,
                "dir");
        job.addGroup(group);
        try {
            scheduler.addJob(job);
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>addJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>SchedulingException</code> will be thrown if the job has group but there is no such group in configuration.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddJobFailureNoJobGroup() throws Exception {
        List jobs = new ArrayList();
        jobs.add(job3);
        JobGroup group = new JobGroup("group", jobs);
        Job job = createIndependentNewJob("job", JobType.JOB_TYPE_EXTERNAL,
                "dir");
        job.addGroup(group);
        try {
            scheduler.addJob(job);
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>updateJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that the job could be updated successfully into the
     * configuration.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testUpdateJobAccuracy() throws Exception {
        Job newJob = createIndependentNewJob("Job1", JobType.JOB_TYPE_EXTERNAL,
                "rm abc");
        newJob.setActive(false);
        newJob.setIntervalValue(30);
        newJob.setStopDate(new GregorianCalendar());
        newJob.setAsyncMode(false);
        // update the job group
        newJob.removeGroup(group1);
        scheduler.updateJob(newJob);
        compareJobAndConfiguration(newJob, root);
        assertTrue("Fails to update the group for job.", isJobNotInGroups(
                new JobGroup[]{group1}, newJob.getName()));
    }

    /**
     * <p>
     * Failure test for method <code>updateJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>IllegalArgumentException</code> will be thrown if the job is <code>null</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testUpdateJobFailureNullJob() throws Exception {
        try {
            scheduler.updateJob(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>updateJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>IllegalArgumentException</code> will be thrown if the job has invalid dependence.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testUpdateJobFailureInvalidDependence() throws Exception {
        try {
            Dependence dependence = new Dependence("Job", Dependence.BOTH, 1000);
            scheduler.updateJob(createDependentNewJob("Job5",
                    JobType.JOB_TYPE_EXTERNAL, "dir", dependence));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>updateJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>IllegalArgumentException</code> will be thrown if the job has the same name with existing job.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddJobFailureJobNameNotExist() throws Exception {
        try {
            scheduler.updateJob(createIndependentNewJob("Job",
                    JobType.JOB_TYPE_EXTERNAL, "dir"));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>updateJob(Job)</code>.
     * </p>
     * <p>
     * Verifies that
     * <code>IllegalArgumentException</code> will be thrown if the job has both dependence and scheduled time.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testUpdateJobFailureBothDependenceAndScheduledTime() throws Exception {
        try {
            Dependence dependence = new Dependence("Job1", Dependence.BOTH,
                    1000);
            Job newJob = createDependentNewJob("Job5",
                    JobType.JOB_TYPE_EXTERNAL, "dir", dependence);
            newJob.setStartDate(new GregorianCalendar());
            scheduler.updateJob(newJob);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>deleteJob(String)</code>.
     * </p>
     * <p>
     * Verifies that the job could be deleted successfully.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testDeleteJobAccuracy() throws Exception {
        scheduler.deleteJob(createIndependentNewJob("Job1",
                JobType.JOB_TYPE_EXTERNAL, "dir"));
        assertNull("Fails to delete job.", root.getChild("Job1"));
        assertTrue("The job is not deleted from group configuration.",
                isJobNotInGroups(new JobGroup[]{group1}, "Job1"));
    }

    /**
     * <p>
     * Failure test for method <code>deleteJob(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the job argument is <code>null</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testDeleteJobFailureNullJob() throws Exception {
        try {
            scheduler.deleteJob(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>deleteJob(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * job does not exist.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testDeleteJobFailureNonExistJob() throws Exception {
        try {
            scheduler.deleteJob(createIndependentNewJob("Job5",
                    JobType.JOB_TYPE_EXTERNAL, "dir"));
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that the job could be returned successfully from the
     * configuration.
     * </p>
     * <p>
     * The job is independent.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobAccuracyIndependent() throws Exception {
        compareJobAndConfiguration(job1, root);
        assertTrue("Fails to get the job group configuration.", isJobInGroups(
                job1.getGroups(), job1.getName()));
        assertEquals("Fails to get the job group configuration.", 1, job1
                .getGroups().length);
    }

    /**
     * <p>
     * Accuracy test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that the job could be returned successfully from the
     * configuration.
     * </p>
     * <p>
     * The job is dependent.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobAccuracyDependent() throws Exception {
        compareJobAndConfiguration(job4, root);
        assertTrue("Fails to get the job group configuration.", isJobInGroups(
                job4.getGroups(), job4.getName()));
        assertEquals("Fails to get the job group configuration.", 1, job4
                .getGroups().length);
    }

    /**
     * <p>
     * Accuracy test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that the job's AsyncMode configuration.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobAccuracyAsyncMode() throws Exception {
        assertFalse("Fails to get the job's AsyncMode configuration.", job3.isAsyncMode());
        assertTrue("Fails to get the job's AsyncMode configuration.", job2.isAsyncMode());
    }

    /**
     * <p>
     * Failure test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the required config is missing.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobFailureIllegalConfiguration1() throws Exception {
        root.getChild("Job1").removeProperty("Active");
        try {
            scheduler.getJob("Job1");
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the single config has multiple
     * values.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobFailureIllegalConfiguration2() throws Exception {
        root.getChild("Job1").setPropertyValues("Active",
                new String[]{"True", "False"});
        try {
            scheduler.getJob("Job1");
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the property's value is not type
     * of string.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobFailureIllegalConfiguration3() throws Exception {
        root.getChild("Job1").setPropertyValue("Active", new Exception());
        try {
            scheduler.getJob("Job1");
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the argument is <code>null</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobFailureNullJobName() throws Exception {
        try {
            scheduler.getJob(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the argument is empty string.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobFailureEmptyJobName() throws Exception {
        try {
            scheduler.getJob("   ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * Interval is not configured.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobFailureNoRequiredConfiguration() throws Exception {
        root.getChild("Job1").removeChild("Interval");
        try {
            scheduler.getJob("Job1");
            fail("SchedulingException is expected.");
        } catch (SchedulingException se) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> <code>null</code> will
     * be returned if the job does not exist.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobAccuracyJobNotExist() throws Exception {
        assertNull("Fails to get the job.", scheduler.getJob("job"));
    }

    /**
     * <p>
     * Accuracy test for method <code>getJob(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> <code>null</code> will
     * be returned if the job does not exist.
     * </p>
     * <p>
     * The job has not messages.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobAccuracyJobNoMessages() throws Exception {
        root.getChild("Job1").removeChild("Messages");
        compareJobAndConfiguration(scheduler.getJob("Job1"), root);
    }

    /**
     * <p>
     * Accuracy test for method <code>getJobList()</code>.
     * </p>
     * <p>
     * Verifies that all the jobs in configuration could be returned.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobListAccuracy() throws Exception {
        Job[] jobs = scheduler.getJobList();
        assertEquals("Fails to get job list.", 4, jobs.length);
    }

    /**
     * <p>
     * Failure test for method <code>getJobList()</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the required config is missing.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobListFailureIllegalConfiguration1() throws Exception {
        root.getChild("Job1").removeProperty("Active");
        try {
            scheduler.getJobList();
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getJobList()</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the single config has multiple
     * values.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobListFailureIllegalConfiguration2() throws Exception {
        root.getChild("Job1").setPropertyValues("Active",
                new String[]{"True", "False"});
        try {
            scheduler.getJobList();
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getJobList()</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the property's value is not type
     * of string.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetJobListFailureIllegalConfiguration3() throws Exception {
        root.getChild("Job1").setPropertyValue("Active", new Exception());
        try {
            scheduler.getJobList();
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>getAllDependentJobs(Job)</code>.
     * </p>
     * <p>
     * Verifies that the dependent job(s) could be got successfully.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllDependentJobsAccuracy() throws Exception {
        Job[] jobs = scheduler.getAllDependentJobs(job1);
        assertEquals("Fails to get all dependent jobs.", 1, jobs.length);
        assertEquals("Fails to get all dependent jobs.", "Job4", jobs[0]
                .getName());
    }

    /**
     * <p>
     * Accuracy test for method <code>getAllDependentJobs(Job)</code>.
     * </p>
     * <p>
     * Verifies that empty array will be returned if no dependent job exists.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllDependentJobsAccuracyEmpty() throws Exception {
        Job[] jobs = scheduler.getAllDependentJobs(job2);
        assertEquals("Fails to get all dependent jobs.", 0, jobs.length);
    }

    /**
     * <p>
     * Failure test for method <code>getAllDependentJobs(Job)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the argument is <code>null</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllDependentJobsFailureNullJob() throws Exception {
        try {
            scheduler.getAllDependentJobs(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getAllDependentJobs(Job)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the job does not exist
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllDependentJobsFailureJobNotExist() throws Exception {
        try {
            scheduler.getAllDependentJobs(createIndependentNewJob("job",
                    JobType.JOB_TYPE_EXTERNAL, "dir"));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getAllDependentJobs(Job)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the required config is missing.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllDependentJobsFailureIllegalConfiguration1() throws Exception {
        root.getChild("Job4").removeProperty("Active");
        try {
            scheduler.getAllDependentJobs(job1);
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getAllDependentJobs(Job)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the single config has multiple
     * values.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllDependentJobsFailureIllegalConfiguration2() throws Exception {
        root.getChild("Job4").setPropertyValues("Active",
                new String[]{"True", "False"});
        try {
            scheduler.getAllDependentJobs(job1);
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getAllDependentJobs(Job)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the property's value is not type
     * of string.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllDependentJobsFailureIllegalConfiguration3() throws Exception {
        root.getChild("Job4").setPropertyValue("Active", new Exception());
        try {
            scheduler.getAllDependentJobs(job1);
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>addGroup(JobGroup)</code>.
     * </p>
     * <p>
     * Verifies that the group could be added to configuration successfully.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddGroupAccuracy() throws Exception {
        List jobs = new ArrayList();
        jobs.add(job1);
        jobs.add(job2);

        JobGroup group3 = new JobGroup("group3", jobs);
        group3.addHandler(getEmailEventHandler());
        scheduler.addGroup(group3);
        compareGroup(group3, root);
    }

    /**
     * <p>
     * Accuracy test for method <code>addGroup(JobGroup)</code>.
     * </p>
     * <p>
     * Verifies that the group could be added to configuration successfully. The
     * DefinedGroups does not exist in the configuration.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddGroupAccuracyNoDefinedGroups() throws Exception {
        root.removeChild("DefinedGroups");
        List jobs = new ArrayList();
        jobs.add(job1);
        jobs.add(job2);

        JobGroup group3 = new JobGroup("group3", jobs);
        group3.addHandler(getEmailEventHandler());
        scheduler.addGroup(group3);
        compareGroup(group3, root);
    }

    /**
     * <p>
     * Failure test for method <code>addGroup(JobGroup)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the argument is <code>null</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddGroupFailureNullJobGroup() throws Exception {

        try {
            scheduler.addGroup(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }

    }

    /**
     * <p>
     * Failure test for method <code>addGroup(JobGroup)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * group already exists.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddGroupFailureDuplicateJobGroup() throws Exception {
        List jobs = new ArrayList();
        jobs.add(job1);
        jobs.add(job2);

        JobGroup group3 = new JobGroup("Group1", jobs);
        try {
            scheduler.addGroup(group3);
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>addGroup(JobGroup)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * jobs referred by the group can not be found in configuration
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testAddGroupFailureInvalidGroupJob() throws Exception {
        List jobs = new ArrayList();
        jobs.add(job1);
        jobs.add(createIndependentNewJob("job", JobType.JOB_TYPE_EXTERNAL,
                "dir"));

        JobGroup group3 = new JobGroup("Group3", jobs);
        try {
            scheduler.addGroup(group3);
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>updateGroup(JobGroup)</code>.
     * </p>
     * <p>
     * Verifies that the group could be updated successfully.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testUpdateGroupAccuracy() throws Exception {
        List jobs = new ArrayList();
        jobs.add(job1);
        jobs.add(job2);
        group1 = new JobGroup("Group1", jobs);
        scheduler.updateGroup(group1);
        compareGroup(group1, root);
    }

    /**
     * <p>
     * Failure test for method <code>updateGroup(JobGroup)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * group to update does not exist in configuration.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testUpdateGroupFailureGroupNotExist() throws Exception {
        List jobs = new ArrayList();
        jobs.add(job1);
        JobGroup group = new JobGroup("Group", jobs);
        try {
            scheduler.updateGroup(group);
            fail("SchedulingException is expected.");
        } catch (SchedulingException se) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>updateGroup(JobGroup)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the group to update does not exist in configuration.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testUpdateGroupFailureNullJobGroup() throws Exception {
        try {
            scheduler.updateGroup(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException se) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>addGroup(JobGroup)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * jobs referred by the group can not be found in configuration
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testUpdateGroupFailureInvalidGroupJob() throws Exception {
        List jobs = new ArrayList();
        jobs.add(job1);
        jobs.add(createIndependentNewJob("job", JobType.JOB_TYPE_EXTERNAL,
                "dir"));

        JobGroup group3 = new JobGroup("Group1", jobs);
        try {
            scheduler.updateGroup(group3);
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>deleteGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that the group could be removed successfully.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testDeleteGroupAccuracy() throws Exception {
        scheduler.deleteGroup("Group1");
        assertNull("Fails to delete group.", root.getChild("DefinedGroups")
                .getChild("Group1"));
    }

    /**
     * <p>
     * Failure test for method <code>deleteGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the argument is <code>null</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testDeleteGroupFailureNullGroupName() throws Exception {
        try {
            scheduler.deleteGroup(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>deleteGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the argument is empty string
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testDeleteGroupFailureEmptyGroupName() throws Exception {
        try {
            scheduler.deleteGroup("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>deleteGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that nothing will happen if the group does not exist.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testDeleteGroupAccuracyGroupNotExist() throws Exception {
        scheduler.deleteGroup("group");
    }

    /**
     * <p>
     * Accuracy test for method <code>deleteGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that nothing will happen if the
     * DefinedGroups configuration does not exist.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testDeleteGroupAccuracyDefinedGroupsNotExist() throws Exception {
        root.removeChild("DefinedGroups");
        scheduler.deleteGroup("Group1");
    }

    /**
     * <p>
     * Accuracy test for method <code>getAllGroups()</code>.
     * </p>
     * <p>
     * Verifies that all the groups could be returned successfully.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllGroupsAccuracy() throws Exception {
        assertEquals("Fails to get all groups.", 1,
                scheduler.getAllGroups().length);
    }

    /**
     * <p>
     * Accuracy test for method <code>getAllGroups()</code>.
     * </p>
     * <p>
     * Verifies that empty array will be returned if there is no group defined.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllGroupsAccuracyNoGroup() throws Exception {
        root.removeChild("DefinedGroups");
        assertEquals("Fails to get all groups.", 0,
                scheduler.getAllGroups().length);
    }

    /**
     * <p>
     * Failure test for method <code>getAllGroups()</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the required property is missing.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllGroupsFailureIllegalConfiguration1() throws Exception {
        root.getChild("DefinedGroups").getChild("Group1").clearProperties();
        try {
            scheduler.getAllGroups();
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getAllGroups()</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the multiple value property has
     * no value.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllGroupsFailureIllegalConfiguration2() throws Exception {
        root.getChild("DefinedGroups").getChild("Group1").setPropertyValues(
                "Jobs", new String[]{});
        try {
            scheduler.getAllGroups();
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getAllGroups()</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the property's value is not type
     * of string.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetAllGroupsFailureIllegalConfiguration3() throws Exception {
        root.getChild("DefinedGroups").getChild("Group1").setPropertyValues(
                "Jobs", new Object[]{});
        try {
            scheduler.getAllGroups();
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method <code>getGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that the group could be returned successfully.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetGroupAccuracy() throws Exception {
        compareGroup(scheduler.getGroup("Group1"), root);
    }

    /**
     * <p>
     * Accuracy test for method <code>getGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>null</code> will be returned if the group could not
     * be found.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetGroupAccuracyNotExist() throws Exception {
        assertNull("Fails to get the group.", scheduler.getGroup("group"));
    }

    /**
     * <p>
     * Failure test for method <code>getGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the argument is <code>null</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetGroupFailureNullGroupName() throws Exception {
        try {
            scheduler.getGroup(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>IllegalArgumentException</code> will be thrown if
     * the argument is empty string.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetGroupFailureEmptyGroupName() throws Exception {
        try {
            scheduler.getGroup("  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the required property is missing.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetGroupFailureIllegalConfiguration1() throws Exception {
        root.getChild("DefinedGroups").getChild("Group1").clearProperties();
        try {
            scheduler.getGroup("Group1");
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the multiple value property has
     * no value.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetGroupFailureIllegalConfiguration2() throws Exception {
        root.getChild("DefinedGroups").getChild("Group1").setPropertyValues(
                "Jobs", new String[]{});
        try {
            scheduler.getGroup("Group1");
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method <code>getGroup(String)</code>.
     * </p>
     * <p>
     * Verifies that <code>SchedulingException</code> will be thrown if the
     * configuration is illegal. In this case, the property's value is not type
     * of string.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testGetGroupFailureIllegalConfiguration3() throws Exception {
        root.getChild("DefinedGroups").getChild("Group1").setPropertyValues(
                "Jobs", new Object[]{});
        try {
            scheduler.getGroup("Group1");
            fail("SchedulingException is expected.");
        } catch (SchedulingException iae) {
            // success
        }
    }

    /**
     * Returns the configuration object used for testing. It configures some
     * jobs and job groups.
     * @return a root configuration object for testing.
     * @throws Exception if there is any problem.
     */
    private ConfigurationObject getRootConfigurationObject() throws Exception {

        root = new DefaultConfigurationObject("root");

        // set the Logger property.
        root.setPropertyValue("Logger", "test");

        // add two Job child configuration.
        root.addChild(getIndependentJobConfigurationObject("Job1"));
        root.addChild(getIndependentJobConfigurationObject("Job2"));
        root.addChild(getIndependentJobConfigurationObject("Job3"));
        root.addChild(getDependentJobConfigurationObject("Job4", "Job1"));

        // add DefinedGroups child configuration.
        ConfigurationObject definedGroups = new DefaultConfigurationObject(
                "DefinedGroups");
        definedGroups.addChild(getJobGroupConfigurationObject("Group1",
                new String[]{"Job1", "Job4"}));

        root.addChild(definedGroups);
        return root;
    }

    /**
     * Returns a job configuration object with basic property.
     * @param jobName the name of a job to create.
     * @return a job configuration object with basic property.
     * @throws Exception if there is any problem.
     */
    private ConfigurationObject getBasicJobConfigurationObject(String jobName) throws Exception {
        ConfigurationObject job = new DefaultConfigurationObject(jobName);

        // set the properties.
        job.setPropertyValue("JobType", "JOB_TYPE_EXTERNAL");
        job.setPropertyValue("JobCommand", "dir");
        job.setPropertyValue("Active", "True");
        job.setPropertyValue("Recurrence", "10");

        // set the interval.
        ConfigurationObject interval = new DefaultConfigurationObject(
                "Interval");
        interval.setPropertyValue("Value", "2");
        ConfigurationObject unit = new DefaultConfigurationObject("Unit");
        unit.setPropertyValue("Type",
                "com.topcoder.util.scheduler.scheduling.WeekMonthOfYear");
        unit.setPropertyValue("DateUnitDays", "1");
        unit.setPropertyValue("DateUnitWeek", "1");
        unit.setPropertyValue("DateUnitMonth", "1");
        interval.addChild(unit);
        job.addChild(interval);

        job.setPropertyValue("ModificationDate", "May 1, 2007 05:00:00 AM");

        // set the messages
        job.addChild(getMessagesConfigurationObject());

        return job;
    }

    /**
     * Returns a dependent job configuration object.
     * @param jobName name of the job to create.
     * @param dependedJobName the name of depended job.
     * @return a dependent job configuration object.
     * @throws Exception if there is any problem.
     */
    private ConfigurationObject getDependentJobConfigurationObject(
            String jobName, String dependedJobName) throws Exception {

        // get the basic job.
        ConfigurationObject job = getBasicJobConfigurationObject(jobName);

        // add the dependence.
        ConfigurationObject dependence = new DefaultConfigurationObject(
                "Dependence");
        ConfigurationObject dependedJob = new DefaultConfigurationObject(
                dependedJobName);
        dependedJob.setPropertyValue("Status", "BOTH");
        dependedJob.setPropertyValue("Delay", "1000");
        dependence.addChild(dependedJob);
        job.addChild(dependence);
        return job;
    }

    /**
     * Returns a independent job configuration object.
     * @param jobName the name of the job.
     * @return a independent job configuration object.
     * @throws Exception if there is any problem.
     */
    private ConfigurationObject getIndependentJobConfigurationObject(
            String jobName) throws Exception {
        // get the basic job.
        ConfigurationObject job = getBasicJobConfigurationObject(jobName);

        // set the scheduled time.
        job.setPropertyValue("StartDate", "May 5, 2007 05:00:00 AM");
        job.setPropertyValue("StartTime", "3000000");
        job.setPropertyValue("EndDate", "May 9, 2007 05:00:00 AM");
        if (jobName.equals("Job3")) {
            job.setPropertyValue("AsyncMode", "False");
        } else if (jobName.equals("Job2")) {
            job.setPropertyValue("AsyncMode", "True");
        }
        return job;
    }

    /**
     * Returns a group configuration object.
     * @param groupNames the name of the group.
     * @param jobNames the names of jobs in this group.
     * @return a group configuration object.
     * @throws Exception if there is any problem.
     */
    private ConfigurationObject getJobGroupConfigurationObject(
            String groupNames, String[] jobNames) throws Exception {
        ConfigurationObject jobGroup = new DefaultConfigurationObject(
                groupNames);

        // set the jobs
        jobGroup.setPropertyValues("Jobs", jobNames);
        // add a message
        jobGroup.addChild(getMessagesConfigurationObject());
        return jobGroup;
    }

    /**
     * Returns the message configuration object to create job.
     * @return a message configuration object.
     * @throws Exception if there is any problem.
     */
    private ConfigurationObject getMessagesConfigurationObject() throws Exception {

        ConfigurationObject messages = new DefaultConfigurationObject(
                "Messages");

        // create the successful event.
        ConfigurationObject successful = new DefaultConfigurationObject(
                "SUCCESSFUL");

        // set the properties.
        successful
                .setPropertyValue(
                        "TemplateText",
                        "This email notifies you that the job %JobName% has the status %JobStatus% now...");
        successful.setPropertyValue("FromAddress", "abc@topcoder.com");
        successful.setPropertyValue("Subject", "Job Runs Successfully");
        successful.setPropertyValues("Recipients", new String[]{"def@topcoder.com", "ghi@topcoder.com"});
        messages.addChild(successful);
        return messages;
    }

    /**
     * Compares the group and the configuration object to check if they match
     * each other.
     * @param group the job group to check.
     * @param config the configuration object to check.
     * @throws Exception if there is any problem.
     */
    private void compareGroup(JobGroup group, ConfigurationObject config) throws Exception {
        // check if the DefinedGroups child exists.
        ConfigurationObject definedGroups = config.getChild("DefinedGroups");
        assertNotNull(
                "Fails to compare the group and corresponding configuration object.",
                definedGroups);
        // check the child configuration for the job.
        ConfigurationObject groupConfig = definedGroups.getChild(group
                .getName());
        assertNotNull(
                "Fails to compare the group and corresponding configuration object.",
                groupConfig);
        // check the handlers.
        compareMessages(group.getHandlers(), groupConfig);
        List jobNames = Arrays.asList(groupConfig.getPropertyValues("Jobs"));
        Job[] jobs = group.getJobs();
        // check the size.
        assertEquals(
                "Fails to compare the group and corresponding configuration object.",
                jobNames.size(), jobs.length);
        // check the content.
        for (int i = 0; i < jobs.length; i++) {
            assertTrue(
                    "Fails to compare the group and corresponding configuration object.",
                    jobNames.contains(jobs[i].getName()));
        }
    }

    /**
     * Compares the job and the configuration object to test if they match each
     * other.
     * @param job the job to compare with.
     * @param configuration the configuration object to compare with.
     * @throws Exception if there is any problem.
     */
    private void compareJobAndConfiguration(Job job,
            ConfigurationObject configuration) throws Exception {
        ConfigurationObject jobConfig = configuration.getChild(job.getName());
        assertNotNull(
                "Fails to compare job and the corresponding configuration object.",
                jobConfig);
        Dependence dependence = job.getDependence();
        if (dependence != null) {
            // The start date, start time, end date should not exist.
            assertFalse(
                    "Fails to compare job and the corresponding configuration object.",
                    jobConfig.containsProperty("StartDate"));
            assertFalse(
                    "Fails to compare job and the corresponding configuration object.",
                    jobConfig.containsProperty("StartTime"));
            assertFalse(
                    "Fails to compare job and the corresponding configuration object.",
                    jobConfig.containsProperty("EndDate"));
            // compare the dependence
            compareDependence(dependence, jobConfig);
        }

        // check the job type.
        assertEquals(
                "Fails to compare job and the corresponding configuration object.",
                getJobTypeString(job.getJobType()), jobConfig
                        .getPropertyValue("JobType"));
        // check the job command.
        assertEquals(
                "Fails to compare job and the corresponding configuration object.",
                job.getRunCommand(), jobConfig.getPropertyValue("JobCommand"));
        // check the active
        assertEquals(
                "Fails to compare job and the corresponding configuration object.",
                String.valueOf(job.getActive()).toLowerCase(),
                ((String) jobConfig.getPropertyValue("Active")).toLowerCase());
        // check the Recurrence
        assertEquals(
                "Fails to compare job and the corresponding configuration object.",
                String.valueOf(job.getRecurrence()), jobConfig
                        .getPropertyValue("Recurrence"));

        // compare the interval.

        compareInterval(job, jobConfig);
        compareMessages(job.getHandlers(), jobConfig);

    }

    /**
     * Checks if the job name is in all the groups in configuration.
     * @param groups the groups to check
     * @param jobName the job name to check.
     * @return <code>true</code> if the job name is in all the groups in
     *         configuration, <code>false</code> otherwise.
     * @throws Exception if there is any problem.
     */
    private boolean isJobInGroups(JobGroup[] groups, String jobName) throws Exception {
        ConfigurationObject definedGroups = root.getChild("DefinedGroups");
        for (int i = 0; i < groups.length; i++) {
            // get the group configuration
            ConfigurationObject groupConfig = definedGroups.getChild(groups[i]
                    .getName());
            // no such group, return false.
            if (groupConfig == null) {
                if (i == 0) {
                    return false;
                }
            }
            // the job name is not in the group.
            if (!Arrays.asList(groupConfig.getPropertyValues("Jobs")).contains(
                    jobName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the job name is in not all the groups in configuration.
     * @param groups the groups to check
     * @param jobName the job name to check.
     * @return <code>true</code> if the job name is not in all the groups in
     *         configuration, <code>false</code> otherwise.
     * @throws Exception if there is any problem.
     */
    private boolean isJobNotInGroups(JobGroup[] groups, String jobName) throws Exception {
        ConfigurationObject definedGroups = root.getChild("DefinedGroups");
        for (int i = 0; i < groups.length; i++) {
            // get the group configuration
            ConfigurationObject groupConfig = definedGroups.getChild(groups[i]
                    .getName());
            // no such group, return false.
            if (groupConfig == null) {
                continue;
            }
            // the job name is not in the group.
            if (Arrays.asList(groupConfig.getPropertyValues("Jobs")).contains(
                    jobName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares the dependence configuration object with job's dependence to
     * test if they match each other.
     * @param dependence the dependence to compare.
     * @param jobConfig the configuration to get the dependence configuration
     *            object child.
     * @throws Exception if there is any problem.
     */
    private void compareDependence(Dependence dependence,
            ConfigurationObject jobConfig) throws Exception {
        ConfigurationObject depConfig = jobConfig.getChild("Dependence");
        assertNotNull(
                "Fails to compare job and the corresponding configuration object.",
                depConfig);
        ConfigurationObject dep = depConfig.getChild(dependence
                .getDependentJobName());
        assertNotNull(
                "Fails to compare job and the corresponding configuration object.",
                dep);
        // check the Status
        assertEquals(
                "Fails to compare job and the corresponding configuration object.",
                dependence.getDependentEvent(), dep.getPropertyValue("Status"));
        // check the delay.
        assertEquals(
                "Fails to compare job and the corresponding configuration object.",
                String.valueOf(dependence.getDelay()), dep
                        .getPropertyValue("Delay"));
    }

    /**
     * Compares the messages configuration object with the handlers to check if
     * they match each other.
     * @param handlers the handlers to compare.
     * @param config the configuration to get the messages configuration object
     *            child.
     * @throws Exception if there is any problem.
     */
    private void compareMessages(EventHandler[] handlers,
            ConfigurationObject config) throws Exception {
        // first filter the Email handlers.
        Map emailHandlers = new HashMap();
        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i] instanceof EmailEventHandler) {
                emailHandlers.put(((EmailEventHandler) handlers[i])
                        .getRequiredEvent(), handlers[i]);
            }
        }

        ConfigurationObject messages = config.getChild("Messages");

        // no email handlers, so there should no Messages configuration object.
        if (emailHandlers.size() == 0) {
            assertNull(
                    "Fails to compare email handlers and the corresponding configuration object.",
                    messages);
            return;
        }

        assertEquals(
                "Fails to compare email handlers and the corresponding configuration object.",
                emailHandlers.size(), messages.getAllChildren().length);
        for (Iterator it = emailHandlers.entrySet().iterator(); it.hasNext();) {
            Entry entry = (Entry) it.next();
            ConfigurationObject status = messages.getChild((String) entry
                    .getKey());
            assertNotNull(
                    "Fails to compare email handlers and the corresponding configuration object.",
                    status);
            EmailEventHandler handler = (EmailEventHandler) entry.getValue();
            // check the Subject
            assertEquals(
                    "Fails to compare email handlers and the corresponding configuration object.",
                    handler.getEmailAlertSubject(), status
                            .getPropertyValue("Subject"));
            // check the from address
            assertEquals(
                    "Fails to compare email handlers and the corresponding configuration object.",
                    handler.getEmailFromAddress(), status
                            .getPropertyValue("FromAddress"));
            // check the Recipients
            assertEquals(
                    "Fails to compare email handlers and the corresponding configuration object.",
                    handler.getRecipients().size(), status
                            .getPropertyValuesCount("Recipients"));

        }

    }

    /**
     * Compare the Interval configuration object with the given job to check if
     * they match each other.
     * @param job the job to get the interval to compare.
     * @param jobConfig the job configuration object to get the interval child
     *            to compare.
     * @throws Exception if there is any problem.
     */
    private void compareInterval(Job job, ConfigurationObject jobConfig) throws Exception {
        // check the Interval child.
        ConfigurationObject interval = jobConfig.getChild("Interval");
        assertNotNull(
                "Fails to compare job and the corresponding configuration object.",
                interval);
        // check the Value.
        assertEquals(
                "Fails to compare job and the corresponding configuration object.",
                String.valueOf(job.getIntervalValue()), interval
                        .getPropertyValue("Value"));
        // check the unit.
        ConfigurationObject unit = interval.getChild("Unit");
        assertNotNull(
                "Fails to compare job and the corresponding configuration object.",
                unit);
        // check the date unit
        assertEquals(
                "Fails to compare job and the corresponding configuration object.",
                job.getIntervalUnit().getClass().getName(), unit
                        .getPropertyValue("Type"));
    }

    /**
     * Gets the job type string representation according to the actual job type.
     * @param jobType the job type to get the string representation.
     * @return the string representation for the given job type.
     */
    private String getJobTypeString(JobType jobType) {
        if (jobType == JobType.JOB_TYPE_EXTERNAL) {
            return "JOB_TYPE_EXTERNAL";
        } else if (jobType == JobType.JOB_TYPE_JAVA_CLASS) {
            return "JOB_TYPE_JAVA_CLASS";
        } else if (jobType == JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY) {
            return "JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY";
        } else {
            return "JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE";
        }
    }

    /**
     * Create a job with basic information.
     * @param jobName the job name
     * @param jobType the job type
     * @param runCommand the command used to create the running object.
     * @return a job with basic information.
     */
    private Job createNewJob(String jobName, JobType jobType, String runCommand) {
        Job job = new Job(jobName, jobType, runCommand);
        job.setActive(true);
        job.setIntervalUnit(new Week());
        job.setIntervalValue(2);
        job.setRecurrence(10);
        job.setModificationDate(new GregorianCalendar());
        // add a email handler.
        job.addHandler(getEmailEventHandler());

        return job;
    }

    /**
     * Creates a new job independent on another job.
     * @param jobName the job name.
     * @param jobType the job type.
     * @param runCommand the command used to create the running object.
     * @return a new dependent job.
     */
    private Job createIndependentNewJob(String jobName, JobType jobType,
            String runCommand) {
        // create basic job
        Job job = createNewJob(jobName, jobType, runCommand);
        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar stopDate = (GregorianCalendar) startDate.clone();
        stopDate.add(Calendar.DATE, 10);
        job.setStartDate(startDate);
        job.setStartTime(3000000);
        job.setStopDate(stopDate);
        return job;
    }

    /**
     * Creates a new job dependent on another job.
     * @param jobName the job name.
     * @param jobType the job type.
     * @param runCommand the command used to create the running object.
     * @param dependence the job's dependence
     * @return a new dependent job.
     */
    private Job createDependentNewJob(String jobName, JobType jobType,
            String runCommand, Dependence dependence) {
        Job job = createNewJob(jobName, jobType, runCommand);
        job.setDependence(dependence);
        return job;
    }

    /**
     * <p>
     * Creates the default <code>Template</code> used in this component.
     * </p>
     * @return the default <code>Template</code> used in this component.
     */
    private static Template createDefaultTemplate() {
        Template defaultTemplate = new XsltTemplate();
        try {
            defaultTemplate
                    .setTemplate("Hi,\nThis email notifies you that the job %JobName%"
                            + " has the status %JobStatus% now.");
        } catch (TemplateFormatException e) {
            // ignore
        }

        return defaultTemplate;
    }

    /**
     * Gets an <code>EmailEventHandler</code> instance used for testing.
     * @return an <code>EmailEventHandler</code> instance used for testing.
     */
    private EventHandler getEmailEventHandler() {
        List recipients = new ArrayList();
        recipients.add("recipients@topcoder.com");
        return new EmailEventHandler(createDefaultTemplate(), recipients,
                "FAILED", "abc@topcoder.com", "Subject", LogManager.getLog());
    }

}
