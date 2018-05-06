/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.ReviewAssignmentManager;
import com.topcoder.management.review.assignment.algorithm.BruteForceBasedReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.project.DefaultReviewAssignmentProjectManager;
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
 * <p>A test case for {@link ReviewAssignmentManager} class.</p>
 * 
 * @author isv
 * @version 1.0
 */
public class ReviewAssignmentManagerAccuracyTest {

    /**
     * <p>A <code>ReviewAssignmentManager</code> instance to run the test against.</p>
     */
    private ReviewAssignmentManager testedInstance;

    /**
     * <p>Gets the test suite with the tests for this test case.</p>
     *
     * @return a <code>Test</code> providing the test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ReviewAssignmentManagerAccuracyTest.class);
    }

    /**
     * <p>Constructs new <code>ReviewAssignmentManagerAccuracyTest</code> instance. This implementation
     * does nothing.</p>
     */
    public ReviewAssignmentManagerAccuracyTest() {
    }

    /**
     * <p>Sets up the test environment for the test case.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Before
    public void setUp() throws Exception {
        this.testedInstance = new ReviewAssignmentManager(TestDataFactory.getReviewAssignmentManagerConfig(false));
    }

    /**
     * <p>Tears down the environment for the test case.</p>
     */
    @After
    public void tearDown() {
        this.testedInstance = null;
    }

    /**
     * <p>Tests the {@link ReviewAssignmentManager#ReviewAssignmentManager(ConfigurationObject)} constructor for 
     * accuracy.</p>
     *
     * <p>Passes configuration object with required parameters set only and verifies that the instance is initialized
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_RequiredParametersOnly() throws Exception {
        ConfigurationObject config = TestDataFactory.getReviewAssignmentManagerConfig(true);
        this.testedInstance = new ReviewAssignmentManager(config);

        Object log = AccuracyTestsHelper.getField(this.testedInstance, "log");
        Assert.assertNull("log is initialized", log);

        Object pendingReviewApplicationStatus 
                = AccuracyTestsHelper.getField(this.testedInstance, "pendingReviewApplicationStatus");
        Assert.assertNotNull("pendingReviewApplicationStatus is not initialized", pendingReviewApplicationStatus);

        Object approvedReviewApplicationStatus
                = AccuracyTestsHelper.getField(this.testedInstance, "approvedReviewApplicationStatus");
        Assert.assertNotNull("approvedReviewApplicationStatus is not initialized", approvedReviewApplicationStatus);

        Object rejectedReviewApplicationStatus
                = AccuracyTestsHelper.getField(this.testedInstance, "rejectedReviewApplicationStatus");
        Assert.assertNotNull("rejectedReviewApplicationStatus is not initialized", rejectedReviewApplicationStatus);

        Object reviewAuctionManager
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewAuctionManager");
        Assert.assertNotNull("reviewAuctionManager is not initialized", reviewAuctionManager);

        Object reviewApplicationManager
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewApplicationManager");
        Assert.assertNotNull("reviewApplicationManager is not initialized", reviewApplicationManager);

        Object projectManager
                = AccuracyTestsHelper.getField(this.testedInstance, "projectManager");
        Assert.assertNotNull("projectManager is not initialized", projectManager);

        Object reviewAssignmentAlgorithm
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewAssignmentAlgorithm");
        Assert.assertNotNull("reviewAssignmentAlgorithm is not initialized", reviewAssignmentAlgorithm);

        Object reviewAssignmentProjectManager
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewAssignmentProjectManager");
        Assert.assertNotNull("reviewAssignmentProjectManager is not initialized", reviewAssignmentProjectManager);

        Object reviewAssignmentNotificationManager
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewAssignmentNotificationManager");
        Assert.assertNotNull("reviewAssignmentNotificationManager is not initialized",
                reviewAssignmentNotificationManager);
    }

    /**
     * <p>Tests the {@link ReviewAssignmentManager#ReviewAssignmentManager(ConfigurationObject)} constructor for
     * accuracy.</p>
     *
     * <p>Passes configuration object with both required and optional parameters set and verifies that the instance is 
     * initialized correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_AllParameters() throws Exception {
        ConfigurationObject config = TestDataFactory.getReviewAssignmentManagerConfig(false);
        this.testedInstance = new ReviewAssignmentManager(config);

        Object log = AccuracyTestsHelper.getField(this.testedInstance, "log");
        Assert.assertNotNull("log is not initialized", log);

        Object pendingReviewApplicationStatus
                = AccuracyTestsHelper.getField(this.testedInstance, "pendingReviewApplicationStatus");
        Assert.assertNotNull("pendingReviewApplicationStatus is not initialized", pendingReviewApplicationStatus);

        Object approvedReviewApplicationStatus
                = AccuracyTestsHelper.getField(this.testedInstance, "approvedReviewApplicationStatus");
        Assert.assertNotNull("approvedReviewApplicationStatus is not initialized", approvedReviewApplicationStatus);

        Object rejectedReviewApplicationStatus
                = AccuracyTestsHelper.getField(this.testedInstance, "rejectedReviewApplicationStatus");
        Assert.assertNotNull("rejectedReviewApplicationStatus is not initialized", rejectedReviewApplicationStatus);

        Object reviewAuctionManager
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewAuctionManager");
        Assert.assertNotNull("reviewAuctionManager is not initialized", reviewAuctionManager);

        Object reviewApplicationManager
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewApplicationManager");
        Assert.assertNotNull("reviewApplicationManager is not initialized", reviewApplicationManager);

        Object projectManager
                = AccuracyTestsHelper.getField(this.testedInstance, "projectManager");
        Assert.assertNotNull("projectManager is not initialized", projectManager);

        Object reviewAssignmentAlgorithm
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewAssignmentAlgorithm");
        Assert.assertNotNull("reviewAssignmentAlgorithm is not initialized", reviewAssignmentAlgorithm);

        Object reviewAssignmentProjectManager
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewAssignmentProjectManager");
        Assert.assertNotNull("reviewAssignmentProjectManager is not initialized", reviewAssignmentProjectManager);

        Object reviewAssignmentNotificationManager
                = AccuracyTestsHelper.getField(this.testedInstance, "reviewAssignmentNotificationManager");
        Assert.assertNotNull("reviewAssignmentNotificationManager is not initialized",
                reviewAssignmentNotificationManager);
    }

    /**
     * <p>Tests the {@link DefaultReviewAssignmentProjectManager#addReviewersToProject(ReviewAuction, Project, Map)}
     * method for accuracy.</p>
     *
     * <p>Calls the method and verifies that phases have been handled as appropriate and that appropriate resources have 
     * been added to project for open review auctions and that review application statuses have been changed 
     * accordingly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("testExecute *********************");
        // List of review auctions with review applications
        long[] auctionIds = new long[]{100011, 100002, 100003, 100010, 100015, 100017, 100024, 100027};

        Map<Long, com.topcoder.project.phases.Project> allProjectPhasesBefore 
                = new HashMap<Long, com.topcoder.project.phases.Project>();
        Map<Long, Resource[]> allProjectResourcesBefore = new HashMap<Long, Resource[]>();
        Map<Long, Map<ReviewApplication, ReviewApplicationRole>> assignments 
                = new HashMap<Long, Map<ReviewApplication, ReviewApplicationRole>>();

        // Get the data before running the test
        for (int i = 0; i < auctionIds.length; i++) {
            long auctionId = auctionIds[i];
            ReviewAuction reviewAuction = TestDataFactory.getReviewAuction(auctionId);
            List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
            Project project = TestDataFactory.getRelatedProject(auctionId);

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

        AccuracyTestsHelper.cleanupDatabase();

        try {
            this.testedInstance.execute();

            Map<Long, com.topcoder.project.phases.Project> allProjectPhasesAfter
                    = new HashMap<Long, com.topcoder.project.phases.Project>();
            Map<Long, Resource[]> allProjectResourcesAfter = new HashMap<Long, Resource[]>();

            // Get the data after running the test
            for (int i = 0; i < auctionIds.length; i++) {
                long auctionId = auctionIds[i];
                Project project = TestDataFactory.getRelatedProject(auctionId);

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
                        "ReviewAssignmentManagerAccuracyTest#testExecute");
                
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
            System.out.println("testExecute FINISHED *********************");
        }
    }

}
