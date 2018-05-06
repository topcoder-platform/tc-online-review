/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.ReviewAssignmentJobRunner;
import com.topcoder.management.review.assignment.algorithm.BruteForceBasedReviewAssignmentAlgorithm;
import com.topcoder.project.phases.Project;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;
import com.topcoder.util.scheduler.scheduling.ScheduledJobRunner;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>A test case for {@link ReviewAssignmentJobRunner} class.</p>
 * 
 * @author isv
 * @version 1.0
 */
public class ReviewAssignmentJobRunnerAccuracyTest {

    /**
     * <p>A <code>ReviewAssignmentJobRunner</code> instance to run the test against.</p>
     */
    private ReviewAssignmentJobRunner testedInstance;

    /**
     * <p>Gets the test suite with the tests for this test case.</p>
     *
     * @return a <code>Test</code> providing the test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ReviewAssignmentJobRunnerAccuracyTest.class);
    }

    /**
     * <p>Constructs new <code>ReviewAssignmentJobRunnerAccuracyTest</code> instance. This implementation does
     * nothing.</p>
     */
    public ReviewAssignmentJobRunnerAccuracyTest() {
    }

    /**
     * <p>Sets up the test environment for the test case.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Before
    public void setUp() throws Exception {
        this.testedInstance = new ReviewAssignmentJobRunner();
    }

    /**
     * <p>Tears down the environment for the test case.</p>
     */
    @After
    public void tearDown() {
        this.testedInstance = null;
    }

    /**
     * <p>Tests the {@link ReviewAssignmentJobRunner#configure(ConfigurationObject)} method for accuracy.</p>
     *
     * <p>Passes configuration object with required parameters set only and verifies that the instance is initialized
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_RequiredParametersOnly() throws Exception {
        ConfigurationObject config = TestDataFactory.getReviewAssignmentManagerConfig(true);
        this.testedInstance.configure(config);

        Object log = AccuracyTestsHelper.getField(this.testedInstance, "log");
        Assert.assertNull("log is initialized", log);

        Object reviewAssignmentManager
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewAssignmentManager");
        Assert.assertNotNull("reviewAssignmentManager is not initialized", reviewAssignmentManager);

        Object config1 = AccuracyTestsHelper.getField(this.testedInstance, "config");
        Assert.assertNull("config is initialized", config1);

        Assert.assertNull("job is initialized", this.testedInstance.getJob());
        Assert.assertEquals("Wrong initial status", ScheduledJobRunner.NOT_STARTED, this.testedInstance.getStatus());
    }

    /**
     * <p>Tests the {@link ReviewAssignmentJobRunner#configure(ConfigurationObject)} method for accuracy.</p>
     *
     * <p>Passes configuration object with both required and optional parameters set and verifies that the instance is
     * initialized correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_AllParameters() throws Exception {
        ConfigurationObject config = TestDataFactory.getReviewAssignmentManagerConfig(false);
        this.testedInstance.configure(config);

        Object log = AccuracyTestsHelper.getField(this.testedInstance, "log");
        Assert.assertNotNull("log is not initialized", log);

        Object reviewAssignmentManager
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewAssignmentManager");
        Assert.assertNotNull("reviewAssignmentManager is not initialized", reviewAssignmentManager);

        Object config1 = AccuracyTestsHelper.getField(this.testedInstance, "config");
        Assert.assertNull("config is initialized", config1);

        Assert.assertNull("job is initialized", this.testedInstance.getJob());
        Assert.assertEquals("Wrong initial status", ScheduledJobRunner.NOT_STARTED, this.testedInstance.getStatus());
    }

    /**
     * <p>Tests the {@link ReviewAssignmentJobRunner#setConfig(ConfigurationObject)} method for accuracy.</p>
     *
     * <p>Passes configuration object and verifies that it is set correctly..</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testSetConfig() throws Exception {
        ConfigurationObject config = TestDataFactory.getReviewAssignmentManagerConfig(false);
        ReviewAssignmentJobRunner.setConfig(config);

        Object config1 = AccuracyTestsHelper.getField(this.testedInstance, "config");
        Assert.assertSame("config is not initialized", config, config1);
    }

    /**
     * <p>Tests the {@link ReviewAssignmentJobRunner#setJob(Job)} method for accuracy.</p>
     *
     * <p>Passes job object and verifies that it is set correctly. {@link ReviewAssignmentJobRunner#getJob()} is also 
     * tested.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testSetGetJob() throws Exception {
        Job job = new Job("1", JobType.JOB_TYPE_JAVA_CLASS, ReviewAssignmentJobRunner.class.getName());
        this.testedInstance.setJob(job);
        Assert.assertSame("job is not initialized", job, this.testedInstance.getJob());
    }

    /**
     * <p>Tests the {@link ReviewAssignmentJobRunner#getMessageData()} method for accuracy.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testGetMessageData() throws Exception {
        NodeList messageData = this.testedInstance.getMessageData();
        Assert.assertNotNull("Message data is null", messageData);
        Assert.assertEquals("Message data is not empty", 0, messageData.getNodes().length);
    }

    /**
     * <p>Tests the {@link ReviewAssignmentJobRunner#setJobName(String)} method for accuracy.</p>
     *
     * <p>Passes job name and verifies that it is set correctly. {@link ReviewAssignmentJobRunner#getJobName()} is also
     * tested.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testSetGetJobName() throws Exception {
        Job job = new Job("1", JobType.JOB_TYPE_JAVA_CLASS, ReviewAssignmentJobRunner.class.getName());
        this.testedInstance.setJob(job);
        this.testedInstance.setJobName("New Job Name");
        Assert.assertSame("job is not initialized", "New Job Name", this.testedInstance.getJobName());
    }

    /**
     * <p>Tests the {@link ReviewAssignmentJobRunner#close()} method for accuracy.</p>
     *
     * <p>Calls the method and verifies that status is set to NO_STARTED. {@link ReviewAssignmentJobRunner#getStatus()} 
     * and {@link ReviewAssignmentJobRunner#getRunningStatus()} are also tested.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testClose() throws Exception {
        this.testedInstance.close();
        Assert.assertSame("job is not closed", ScheduledJobRunner.NOT_STARTED, this.testedInstance.getStatus());
        Assert.assertSame("job is not closed", ScheduledJobRunner.NOT_STARTED, this.testedInstance.getRunningStatus());
    }

    /**
     * <p>Tests the {@link ReviewAssignmentJobRunner#isDone()} method for accuracy.</p>
     *
     * <p>Calls the method and verifies the result.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testIsDone() throws Exception {
        Assert.assertFalse("Wrong is done status", this.testedInstance.isDone());
    }

    /**
     * <p>Tests the {@link ReviewAssignmentJobRunner#run()} method for accuracy.</p>
     *
     * <p>Calls the method and verifies that phases have been handled as appropriate and that appropriate resources have 
     * been added to project for open review auctions and that review application statuses have been changed 
     * accordingly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testRun() throws Exception {
        System.out.println("testRun ****************************");
        // List of review auctions with review applications
        long[] auctionIds = new long[]{100011, 100002, 100003, 100010, 100015, 100017, 100024, 100027};

        Map<Long, Project> allProjectPhasesBefore
                = new HashMap<Long, Project>();
        Map<Long, Resource[]> allProjectResourcesBefore = new HashMap<Long, Resource[]>();
        Map<Long, Map<ReviewApplication, ReviewApplicationRole>> assignments
                = new HashMap<Long, Map<ReviewApplication, ReviewApplicationRole>>();

        // Get the data before running the test
        for (int i = 0; i < auctionIds.length; i++) {
            long auctionId = auctionIds[i];
            ReviewAuction reviewAuction = TestDataFactory.getReviewAuction(auctionId);
            List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
            com.topcoder.management.project.Project project = TestDataFactory.getRelatedProject(auctionId);

            BruteForceBasedReviewAssignmentAlgorithm algorithm = TestDataFactory.getAlgorithm();
            Map<ReviewApplication, ReviewApplicationRole> assignment;
            if (auctionId != 100002) {
                assignment = algorithm.assign(reviewAuction, reviewApplications);
            } else {
                assignment = new HashMap<ReviewApplication, ReviewApplicationRole>();
            }
            assignments.put(auctionId, assignment);

            // Get the phases and resource before update
            com.topcoder.project.phases.Project phasesBefore =
                    TestDataFactory.getPhaseManager().getPhases(project.getId());
            Resource[] resourcesBefore = TestDataFactory.getResourceManager()
                    .searchResources(ResourceFilterBuilder.createProjectIdFilter(project.getId()));

            allProjectPhasesBefore.put(auctionId, phasesBefore);
            allProjectResourcesBefore.put(auctionId, resourcesBefore);
        }

        try {
            this.testedInstance.configure(TestDataFactory.getReviewAssignmentManagerConfig(false));
            this.testedInstance.run();
            
            Assert.assertEquals("Status is not correct after execution", 
                    ScheduledJobRunner.SUCCESSFUL, this.testedInstance.getStatus());

            Map<Long, com.topcoder.project.phases.Project> allProjectPhasesAfter
                    = new HashMap<Long, com.topcoder.project.phases.Project>();
            Map<Long, Resource[]> allProjectResourcesAfter = new HashMap<Long, Resource[]>();

            // Get the data after running the test
            for (int i = 0; i < auctionIds.length; i++) {
                long auctionId = auctionIds[i];
                com.topcoder.management.project.Project project = TestDataFactory.getRelatedProject(auctionId);

                // Get the phases and resource after update
                com.topcoder.project.phases.Project phasesAfter =
                        TestDataFactory.getPhaseManager().getPhases(project.getId());
                Resource[] resourcesAfter = TestDataFactory.getResourceManager()
                        .searchResources(ResourceFilterBuilder.createProjectIdFilter(project.getId()));

                allProjectPhasesAfter.put(auctionId, phasesAfter);
                allProjectResourcesAfter.put(auctionId, resourcesAfter);
            }

            // Now test the phases and resources for each of the review auction
            for (int i = 0; i < auctionIds.length; i++) {
                long auctionId = auctionIds[i];
                Map<ReviewApplication, ReviewApplicationRole> assignment = assignments.get(auctionId);
                com.topcoder.project.phases.Project phasesBefore = allProjectPhasesBefore.get(auctionId);
                com.topcoder.project.phases.Project phasesAfter = allProjectPhasesAfter.get(auctionId);
                Resource[] resourcesBefore = allProjectResourcesBefore.get(auctionId);
                Resource[] resourcesAfter = allProjectResourcesAfter.get(auctionId);

                if (auctionId == 100002 || auctionId == 100003 || auctionId == 100024) {
                    // Auctions without phase extensions or with no review applications 100003, 100024, 10002 
                    AccuracyTestsHelper.testPhasesForEquality(phasesBefore, phasesAfter);
                } else { // Auctions with phase extension 100027, 100017, 100015, 100002, 100011, 100010
                    AccuracyTestsHelper.testPhasesForExtension(phasesBefore, phasesAfter);
                }
                AccuracyTestsHelper.testResources(resourcesBefore, resourcesAfter, assignment, 
                        "ReviewAssignmentJobRunnerAccuracyTest#testRun");

                // Test that status of the review applications have changed correctly
                List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
                for (ReviewApplication reviewApplication : reviewApplications) {
                    boolean isAccepted = false;
                    Set<ReviewApplication> assignedReviewApplications = assignment.keySet();
                    for (ReviewApplication assignedReviewApplication : assignedReviewApplications) {
                        if (assignedReviewApplication.getId() == reviewApplication.getId()) {
                            isAccepted = true;
                            break;
                        }
                    }

                    if (isAccepted) {
                        Assert.assertEquals("Wrong status for accepted review application",
                                3, reviewApplication.getStatus().getId());
                    } else {
                        Assert.assertEquals("Wrong status for rejected review application",
                                4, reviewApplication.getStatus().getId());
                    }
                }
            }
        } finally {
            AccuracyTestsHelper.cleanupDatabase();
            System.out.println("testRun FINISHED ****************************");
        }
    }


}
