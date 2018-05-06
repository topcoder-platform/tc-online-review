/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.scheduler.scheduling.Day;
import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobGroup;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.SchedulingException;
import com.topcoder.util.scheduler.scheduling.persistence.ConfigManagerScheduler;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains unit tests for ConfigManagerScheduler class.
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class FailureTestConfigManagerScheduler extends TestCase {
    /** The Job instance used to test. */
    private Job job = null;

    /** The JobGroup instance used to test. */
    private JobGroup jobGroup = null;

    /** The List instance used to test. */
    private List jobs = null;

    /** The ConfigManagerScheduler instance used to test. */
    private ConfigManagerScheduler scheduler = null;

    /**
     * Set Up the test environment before testing.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        FailureTestHelper.loadXMLConfig();

        job = new Job("NewJob", JobType.JOB_TYPE_EXTERNAL, "dir");

        Dependence dependence = new Dependence("dependence007", EventHandler.SUCCESSFUL, 1000);
        job.setDependence(dependence);
        job.setRecurrence(5);
        job.setIntervalUnit(new Day());
        job.setIntervalValue(3);

        Job exist = new Job("jobName007", JobType.JOB_TYPE_EXTERNAL, "dir");
        jobs = new ArrayList();
        jobs.add(exist);
        jobGroup = new JobGroup("newGroup", jobs);

        scheduler = new ConfigManagerScheduler("persistence.ns");
    }

    /**
     * Clean up the test environment after testing.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        FailureTestHelper.clearConfig();
    }

    /**
     * Tests <code>ConfigManagerScheduler(String namespace)</code> method for
     * failure with null Namespace. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigManagerSchedulerNullNamespace() throws Exception {
        try {
            new ConfigManagerScheduler(null);
            fail("testConfigManagerSchedulerNullNamespace is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>ConfigManagerScheduler(String namespace)</code> method for
     * failure with empty Namespace, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigManagerSchedulerEmptyNamespace() throws Exception {
        try {
            new ConfigManagerScheduler(" ");
            fail("testConfigManagerSchedulerEmptyNamespace is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>addJob(Job job)</code> method for failure with null Job.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddJobNullJob() throws Exception {
        try {
            scheduler.addJob(null);
            fail("testAddJobNullJob is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>addJob(Job job)</code> method for failure with invalid Job.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddJobInvalidJob1() throws Exception {
        try {
            scheduler.addJob(job);
            scheduler.addJob(job);
            fail("testAddJobInvalidJob1 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>addJob(Job job)</code> method for failure with invalid Job.
     * SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddJobInvalidJob2() throws Exception {
        FailureTestHelper.clearConfig();

        try {
            scheduler.addJob(job);
            fail("testAddJobInvalidJob2 is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>addJob(Job job)</code> method for failure with invalid Job.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddJobInvalidJob3() throws Exception {
        job.setDependence(new Dependence("jobName007", EventHandler.SUCCESSFUL, 2));

        try {
            scheduler.addJob(job);
            fail("testAddJobInvalidJob3 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>addJob(Job job)</code> method for failure with invalid Job.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddJobInvalidJob4() throws Exception {
        job.setName("jobName007");

        try {
            scheduler.addJob(job);
            fail("testAddJobInvalidJob4 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>updateJob(Job job)</code> method for failure with null Job.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUpdateJobNullJob() throws Exception {
        try {
            scheduler.updateJob(null);
            fail("testUpdateJobNullJob is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>updateJob(Job job)</code> method for failure with invalid
     * Job. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUpdateJobInvalidJob1() throws Exception {
        job.setName("missing");

        try {
            scheduler.updateJob(job);
            fail("testUpdateJobNullJob1 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>updateJob(Job job)</code> method for failure with invalid
     * Job. SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUpdateJobInvalidJob2() throws Exception {
        FailureTestHelper.clearConfig();

        try {
            scheduler.updateJob(job);
            fail("testupdateJobInvalidJob2 is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>updateJob(Job job)</code> method for failure with invalid
     * Job. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUpdateJobInvalidJob3() throws Exception {
        job.setDependence(new Dependence("missing", EventHandler.SUCCESSFUL, 2));

        try {
            scheduler.updateJob(job);
            fail("testupdateJobInvalidJob3 is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>deleteJob(Job job)</code> method for failure with null Job.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDeleteJobNullJob() throws Exception {
        FailureTestHelper.clearConfig();

        try {
            scheduler.deleteJob(null);
            fail("testDeleteJobNullJob is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>deleteJob(Job job)</code> method for failure with invalid
     * Job. SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDeleteJobInvalidJob() throws Exception {
        job.setName("jobName007");
        FailureTestHelper.clearConfig();

        try {
            scheduler.deleteJob(job);
            fail("testDeleteJobInvalidJob is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>deleteJob(Job job)</code> method for failure with invalid
     * Job. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDeleteJobInvalidJob2() throws Exception {
        try {
            scheduler.deleteJob(job);
            fail("testDeleteJobInvalidJob2 is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getJob(String jobName)</code> method for failure with null
     * JobName. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetJobNullJobName() throws Exception {
        try {
            scheduler.getJob(null);
            fail("testGetJobNullJobName is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getJob(String jobName)</code> method for failure with empty
     * JobName, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetJobEmptyJobName() throws Exception {
        try {
            scheduler.getJob(" ");
            fail("testGetJobEmptyJobName is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getJob(String jobName)</code> method for failure with
     * invalid JobName, SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetJobInvalid() throws Exception {
        FailureTestHelper.clearConfig();

        try {
            scheduler.getJob("null");
            fail("testGetJobInvalid is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getJob(String jobName)</code> method for failure with
     * invalid JobName, SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetJobInvalid2() throws Exception {
        FailureTestHelper.clearConfig();

        try {
            scheduler.getJob("jobName007");
            fail("testGetJobInvalid is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getJobList()</code> method for failure with invalid
     * configuration, SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetJobListInvalid2() throws Exception {
        FailureTestHelper.clearConfig();

        try {
            scheduler.getJobList();
            fail("testGetJobInvalid is failure.");
        } catch (SchedulingException iae) {
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
            scheduler.addGroup(null);
            fail("testAddGroupNullGroup is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>addGroup(JobGroup group)</code> method for failure with
     * invalid Group. SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddGroupInvalidGroup() throws Exception {
        // the job does not exist in configuration.
        jobs.add(job);

        try {
            scheduler.addGroup(new JobGroup("newGroup", jobs));
            fail("testAddGroupInvalidGroup is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>addGroup(JobGroup group)</code> method for failure with
     * invalid Group. SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddGroupInvalidGroup2() throws Exception {
        FailureTestHelper.clearConfig();

        try {
            scheduler.addGroup(jobGroup);
            fail("testAddGroupInvalidGroup2 is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>updateGroup(JobGroup group)</code> method for failure with
     * null Group. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUpdateGroupNullGroup() throws Exception {
        try {
            scheduler.updateGroup(null);
            fail("testUpdateGroupNullGroup is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>updateGroup(JobGroup group)</code> method for failure with
     * invalid Group. SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUpdateGroupInvalidGroup() throws Exception {
        // the job does not exist in configuration.
        jobs.add(job);

        try {
            scheduler.updateGroup(new JobGroup("group_1", jobs));
            fail("testUpdateGroupInvalidGroup is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>updateGroup(JobGroup group)</code> method for failure with
     * invalid Group. SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUpdateGroupInvalidGroup2() throws Exception {
        try {
            scheduler.updateGroup(new JobGroup("missing", jobs));
            fail("testUpdateGroupInvalidGroup2 is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>updateGroup(JobGroup group)</code> method for failure with
     * invalid Group. SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUpdateGroupInvalidGroup3() throws Exception {
        FailureTestHelper.clearConfig();

        try {
            scheduler.updateGroup(new JobGroup("group_1", jobs));
            fail("testUpdateGroupInvalidGroup3 is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>deleteGroup(String name)</code> method for failure with
     * null Name. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDeleteGroupNullName() throws Exception {
        try {
            scheduler.deleteGroup(null);
            fail("testDeleteGroupNullName is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>deleteGroup(String name)</code> method for failure with
     * empty Name, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDeleteGroupEmptyName() throws Exception {
        try {
            scheduler.deleteGroup(" ");
            fail("testDeleteGroupEmptyName is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>deleteGroup(String name)</code> method for failure with
     * invalid, SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDeleteGroupInvalid1() throws Exception {
        FailureTestHelper.clearConfig();

        try {
            scheduler.deleteGroup("group_1");
            fail("testDeleteGroupInvalid1 is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getGroup(String name)</code> method for failure with null
     * Name. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetGroupNullName() throws Exception {
        try {
            scheduler.getGroup(null);
            fail("testGetGroupNullName is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getGroup(String name)</code> method for failure with empty
     * Name, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetGroupEmptyName() throws Exception {
        try {
            scheduler.getGroup(" ");
            fail("testGetGroupEmptyName is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getGroup(String name)</code> method for failure with
     * invalid, SchedulingException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetGroupInvalid() throws Exception {
        FailureTestHelper.clearConfig();

        try {
            scheduler.getGroup("group_1");
            fail("testGetGroupEmptyName is failure.");
        } catch (SchedulingException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getAllDependentJobs(Job job)</code> method for failure with
     * null Job. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetAllDependentJobsNullJob() throws Exception {
        try {
            scheduler.getAllDependentJobs(null);
            fail("testGetAllDependentJobsNullJob is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getAllDependentJobs(Job job)</code> method for failure with
     * invalid job. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetAllDependentJobsInvalidJob() throws Exception {
        // the job does not exist in configuration.
        try {
            scheduler.getAllDependentJobs(job);
            fail("testUpdateGroupInvalidGroup is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }
}
