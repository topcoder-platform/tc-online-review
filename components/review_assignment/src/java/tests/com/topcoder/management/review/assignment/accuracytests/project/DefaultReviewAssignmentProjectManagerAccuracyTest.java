/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests.project;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.accuracytests.AccuracyTestsHelper;
import com.topcoder.management.review.assignment.accuracytests.TestDataFactory;
import com.topcoder.management.review.assignment.algorithm.BruteForceBasedReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.project.DefaultReviewAssignmentProjectManager;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * <p>A test case for {@link DefaultReviewAssignmentProjectManager} class.</p>
 * 
 * @author isv
 * @version 1.0                                 
 */
public class DefaultReviewAssignmentProjectManagerAccuracyTest {

    /**
     * <p>A <code>DefaultReviewAssignmentProjectManager</code> instance to run the test against.</p>
     */
    private DefaultReviewAssignmentProjectManager testedInstance;

    /**
     * <p>Gets the test suite with the tests for this test case.</p>
     *
     * @return a <code>Test</code> providing the test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DefaultReviewAssignmentProjectManagerAccuracyTest.class);
    }

    /**
     * <p>Constructs new <code>DefaultReviewAssignmentProjectManagerAccuracyTest</code> instance. This 
     * implementation does nothing.</p>
     */
    public DefaultReviewAssignmentProjectManagerAccuracyTest() {
    }

    /**
     * <p>Sets up the test environment for the test case.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Before
    public void setUp() throws Exception {
        this.testedInstance = new DefaultReviewAssignmentProjectManager();
    }

    /**
     * <p>Tears down the environment for the test case.</p>
     */
    @After
    public void tearDown() {
        this.testedInstance = null;
    }

    /**
     * <p>Tests the {@link DefaultReviewAssignmentProjectManager#configure(ConfigurationObject)} method for accuracy.
     * </p>
     *
     * <p>Passes configuration object with required parameters set only and verifies that the instance is initialized
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_RequiredParametersOnly() throws Exception {
        ConfigurationObject config = TestDataFactory.getReviewAssignmentProjectManagerConfig(true, true);
        this.testedInstance.configure(config);

        Object log = AccuracyTestsHelper.getField(this.testedInstance, "log");
        Assert.assertNull("log is initialized", log);

        Object userRetrieval = AccuracyTestsHelper.getField(this.testedInstance, "userRetrieval");
        Assert.assertNotNull("userRetrieval is not initialized", userRetrieval);

        Object resourceManager = AccuracyTestsHelper.getField(this.testedInstance, "resourceManager");
        Assert.assertNotNull("resourceManager is not initialized", resourceManager);

        Object phaseManager = AccuracyTestsHelper.getField(this.testedInstance, "phaseManager");
        Assert.assertNotNull("phaseManager is not initialized", phaseManager);

        Object resourceRoleById = AccuracyTestsHelper.getField(this.testedInstance, "resourceRoleById");
        Assert.assertNotNull("resourceRoleById is not initialized", resourceRoleById);

        Object phaseTypeExtensionRules = AccuracyTestsHelper.getField(this.testedInstance, "phaseTypeExtensionRules");
        Assert.assertNotNull("phaseTypeExtensionRules is not initialized", phaseTypeExtensionRules);

        Object forumsBeanUrl = AccuracyTestsHelper.getField(this.testedInstance, "forumsBeanUrl");
        Assert.assertNotNull("forumsBeanUrl is not initialized", forumsBeanUrl);

        Object forumsBeanName = AccuracyTestsHelper.getField(this.testedInstance, "forumsBeanName");
        Assert.assertNotNull("forumsBeanName is not initialized", forumsBeanName);

        Object forumsBeanFactory = AccuracyTestsHelper.getField(this.testedInstance, "forumsBeanFactory");
        Assert.assertNotNull("forumsBeanFactory is not initialized", forumsBeanFactory);

        Object registrationDateFormatString = AccuracyTestsHelper.getField(this.testedInstance, 
                "registrationDateFormatString");
        Assert.assertNotNull("registrationDateFormatString is not initialized", registrationDateFormatString);
        Assert.assertEquals("registrationDateFormatString is not set to default value", 
                "MM.dd.yyyy hh:mm a", registrationDateFormatString);

        testConfiguredParameter("operator", "operator", config);
    }

    /**
     * <p>Tests the {@link DefaultReviewAssignmentProjectManager#configure(ConfigurationObject)} method for accuracy.
     * </p>
     *
     * <p>Passes configuration object having both required and optional parameters set and verifies that the instance is 
     * initialized correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_AllParameters() throws Exception {
        ConfigurationObject config = TestDataFactory.getReviewAssignmentProjectManagerConfig(false, false);
        this.testedInstance.configure(config);

        Object log = AccuracyTestsHelper.getField(this.testedInstance, "log");
        Assert.assertNotNull("log is not initialized", log);

        Object userRetrieval = AccuracyTestsHelper.getField(this.testedInstance, "userRetrieval");
        Assert.assertNotNull("userRetrieval is not initialized", userRetrieval);

        Object resourceManager = AccuracyTestsHelper.getField(this.testedInstance, "resourceManager");
        Assert.assertNotNull("resourceManager is not initialized", resourceManager);

        Object phaseManager = AccuracyTestsHelper.getField(this.testedInstance, "phaseManager");
        Assert.assertNotNull("phaseManager is not initialized", phaseManager);

        Object resourceRoleById = AccuracyTestsHelper.getField(this.testedInstance, "resourceRoleById");
        Assert.assertNotNull("resourceRoleById is not initialized", resourceRoleById);

        Object phaseTypeExtensionRules = AccuracyTestsHelper.getField(this.testedInstance, "phaseTypeExtensionRules");
        Assert.assertNotNull("phaseTypeExtensionRules is not initialized", phaseTypeExtensionRules);

        Object forumsBeanUrl = AccuracyTestsHelper.getField(this.testedInstance, "forumsBeanUrl");
        Assert.assertNotNull("forumsBeanUrl is not initialized", forumsBeanUrl);

        Object forumsBeanName = AccuracyTestsHelper.getField(this.testedInstance, "forumsBeanName");
        Assert.assertNotNull("forumsBeanName is not initialized", forumsBeanName);

        Object forumsBeanFactory = AccuracyTestsHelper.getField(this.testedInstance, "forumsBeanFactory");
        Assert.assertNotNull("forumsBeanFactory is not initialized", forumsBeanFactory);

        testConfiguredParameter("operator", "operator", config);
        testConfiguredParameter("registrationDateFormatString", "registrationDateFormatString", config);
    }

    /**
     * <p>Tests the {@link DefaultReviewAssignmentProjectManager#addReviewersToProject(ReviewAuction, Project, Map)} 
     * method for accuracy.</p>
     *
     * <p>Calls the method against the review assignment for review auction for the project which does not have review 
     * phases started yet so that there is no extension to phases timeline is necessary and verifies that phases were 
     * not affected and that appropriate resources have been added to project.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testAddReviewersToProject_NoPhaseExtension() throws Exception {
        ConfigurationObject config = TestDataFactory.getReviewAssignmentProjectManagerConfig(false, false);
        this.testedInstance.configure(config);

        final long auctionId = 100024;
        ReviewAuction reviewAuction = TestDataFactory.getReviewAuction(auctionId);
        List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
        Project project = TestDataFactory.getRelatedProject(auctionId);
        BruteForceBasedReviewAssignmentAlgorithm algorithm = TestDataFactory.getAlgorithm();
        Map<ReviewApplication, ReviewApplicationRole> assignment = algorithm.assign(reviewAuction, reviewApplications);

        // Get the phases and resource before update
        com.topcoder.project.phases.Project phasesBefore = TestDataFactory.getPhaseManager().getPhases(project.getId());
        Resource[] resourcesBefore = TestDataFactory.getResourceManager()
                .searchResources(ResourceFilterBuilder.createProjectIdFilter(project.getId()));

        try {
            this.testedInstance.addReviewersToProject(reviewAuction, project, assignment);

            // Get the phases after update
            com.topcoder.project.phases.Project phasesAfter = TestDataFactory.getPhaseManager().getPhases(project.getId());
            Resource[] resourcesAfter = TestDataFactory.getResourceManager()
                    .searchResources(ResourceFilterBuilder.createProjectIdFilter(project.getId()));

            AccuracyTestsHelper.testPhasesForEquality(phasesBefore, phasesAfter);
            AccuracyTestsHelper.testResources(resourcesBefore, resourcesAfter, assignment, 
                    "DefaultReviewAssignmentProjectManagerAccuracyTest#testAddReviewersToProject_NoPhaseExtension");
        } finally {
            AccuracyTestsHelper.cleanupDatabase();
        }
    }

    /**
     * <p>Tests the {@link DefaultReviewAssignmentProjectManager#addReviewersToProject(ReviewAuction, Project, Map)}
     * method for accuracy.</p>
     *
     * <p>Calls the method against the review assignment for review auction for the project which does have review
     * phases started yet so that there is extension to phases timeline necessary and verifies that phases were
     * affected and that appropriate resources have been added to project.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testAddReviewersToProject_PhaseExtension() throws Exception {
        System.out.println("testAddReviewersToProject_PhaseExtension *****************");
        ConfigurationObject config = TestDataFactory.getReviewAssignmentProjectManagerConfig(false, false);
        this.testedInstance.configure(config);

        final long auctionId = 100027;
        ReviewAuction reviewAuction = TestDataFactory.getReviewAuction(auctionId);
        List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
        Project project = TestDataFactory.getRelatedProject(auctionId);
        BruteForceBasedReviewAssignmentAlgorithm algorithm = TestDataFactory.getAlgorithm();
        Map<ReviewApplication, ReviewApplicationRole> assignment = algorithm.assign(reviewAuction, reviewApplications);

        // Get the phases and resource before update
        com.topcoder.project.phases.Project phasesBefore = TestDataFactory.getPhaseManager().getPhases(project.getId());
        Resource[] resourcesBefore = TestDataFactory.getResourceManager()
                .searchResources(ResourceFilterBuilder.createProjectIdFilter(project.getId()));

        try {
            this.testedInstance.addReviewersToProject(reviewAuction, project, assignment);

            // Get the phases after update
            com.topcoder.project.phases.Project phasesAfter =
                    TestDataFactory.getPhaseManager().getPhases(project.getId());
            Resource[] resourcesAfter = TestDataFactory.getResourceManager()
                    .searchResources(ResourceFilterBuilder.createProjectIdFilter(project.getId()));

            AccuracyTestsHelper.testPhasesForExtension(phasesBefore, phasesAfter);
            AccuracyTestsHelper.testResources(resourcesBefore, resourcesAfter, assignment, 
                    "DefaultReviewAssignmentProjectManagerAccuracyTest#testAddReviewersToProject_PhaseExtension");
        } finally {
            AccuracyTestsHelper.cleanupDatabase();
            System.out.println("testAddReviewersToProject_PhaseExtension FINISHED *****************");
        }
    }

    /**
     * <p>Tests that the specified property of the tested instance has been set to the value of specified parameter from
     * specified configuration.</p>
     *
     * @param propertyName a <code>String</code> providing the name of the property.
     * @param parameterName a <code>String</code> providing the name of the configuration parameter.
     * @param config a <code>ConfigurationObject</code> providing the configuration.
     * @throws Exception if an unexpected error occurs.
     */
    private void testConfiguredParameter(String propertyName, String parameterName, ConfigurationObject config)
            throws Exception {
        Object parameterValue = AccuracyTestsHelper.getField(this.testedInstance, propertyName);
        Assert.assertNotNull(propertyName + " is not initialized", parameterValue);
        Assert.assertEquals(propertyName + " is incorrect", config.getPropertyValue(parameterName), parameterValue);
    }
}
