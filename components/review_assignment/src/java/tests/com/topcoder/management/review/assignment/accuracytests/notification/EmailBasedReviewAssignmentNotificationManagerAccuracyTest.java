/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests.notification;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.project.Project;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.accuracytests.AccuracyTestsHelper;
import com.topcoder.management.review.assignment.accuracytests.TestDataFactory;
import com.topcoder.management.review.assignment.algorithm.BruteForceBasedReviewAssignmentAlgorithm;
import com.topcoder.management.review.assignment.notification.EmailBasedReviewAssignmentNotificationManager;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>A test case for {@link EmailBasedReviewAssignmentNotificationManager} class.</p>
 * 
 * @author isv
 * @version 1.0
 */
public class EmailBasedReviewAssignmentNotificationManagerAccuracyTest {

    /**
     * <p>A <code>EmailBasedReviewAssignmentNotificationManager</code> instance to run the test against.</p>
     */
    private EmailBasedReviewAssignmentNotificationManager testedInstance;

    /**
     * <p>Gets the test suite with the tests for this test case.</p>
     *
     * @return a <code>Test</code> providing the test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(EmailBasedReviewAssignmentNotificationManagerAccuracyTest.class);
    }

    /**
     * <p>Constructs new <code>EmailBasedReviewAssignmentNotificationManagerAccuracyTest</code> instance. This 
     * implementation does nothing.</p>
     */
    public EmailBasedReviewAssignmentNotificationManagerAccuracyTest() {
    }

    /**
     * <p>Sets up the test environment for the test case.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Before
    public void setUp() throws Exception {
        this.testedInstance = new EmailBasedReviewAssignmentNotificationManager();
    }

    /**
     * <p>Tears down the environment for the test case.</p>
     */
    @After
    public void tearDown() {
        this.testedInstance = null;
    }

    /**
     * <p>Tests the {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method for 
     * accuracy.</p>
     *
     * <p>Passes configuration object with required parameters set only and verifies that the instance is initialized 
     * correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_RequiredParametersOnly() throws Exception {
        ConfigurationObject config = TestDataFactory.getNotificationManagerConfig(true);
        this.testedInstance.configure(config);

        Object log = AccuracyTestsHelper.getField(this.testedInstance, "log");
        Assert.assertNull("log is initialized", log);

        Object userRetrieval = AccuracyTestsHelper.getField(this.testedInstance, "userRetrieval");
        Assert.assertNotNull("userRetrieval is not initialized", userRetrieval);

        Object resourceManager = AccuracyTestsHelper.getField(this.testedInstance, "resourceManager");
        Assert.assertNotNull("resourceManager is not initialized", resourceManager);

        Object emailSendingUtility = AccuracyTestsHelper.getField(this.testedInstance, "emailSendingUtility");
        Assert.assertNotNull("emailSendingUtility is not initialized", emailSendingUtility);

        Object managerResourceRoleIds = AccuracyTestsHelper.getField(this.testedInstance, "managerResourceRoleIds");
        Assert.assertNotNull("managerResourceRoleIds is not initialized", managerResourceRoleIds);

        testConfiguredParameter("approvedApplicantEmailSubjectTemplateText", 
                "approvedApplicantEmailSubjectTemplateText", config);
        testConfiguredParameter("approvedApplicantEmailBodyTemplatePath",
                "approvedApplicantEmailBodyTemplatePath", config);
        testConfiguredParameter("rejectedApplicantEmailSubjectTemplateText", 
                "rejectedApplicantEmailSubjectTemplateText", config);
        testConfiguredParameter("rejectedApplicantEmailBodyTemplatePath", "rejectedApplicantEmailBodyTemplatePath", 
                config);
        testConfiguredParameter("rejectedApplicantEmailBodyTemplatePath", "rejectedApplicantEmailBodyTemplatePath", 
                config);
        testConfiguredParameter("managerEmailSubjectTemplateText", "managerEmailSubjectTemplateText", config);
        testConfiguredParameter("managerApplicantEmailBodyTemplatePath", "managerApplicantEmailBodyTemplatePath",
                config);
    }

    /**
     * <p>Tests the {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method for
     * accuracy.</p>
     *
     * <p>Passes configuration object having both required and optional parameters set and verifies that the instance is 
     * initialized correctly.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testConfigure_AllParameters() throws Exception {
        ConfigurationObject config = TestDataFactory.getNotificationManagerConfig(false);
        this.testedInstance.configure(config);

        Object log = AccuracyTestsHelper.getField(this.testedInstance, "log");
        Assert.assertNotNull("log is not initialized", log);

        Object userRetrieval = AccuracyTestsHelper.getField(this.testedInstance, "userRetrieval");
        Assert.assertNotNull("userRetrieval is not initialized", userRetrieval);

        Object resourceManager = AccuracyTestsHelper.getField(this.testedInstance, "resourceManager");
        Assert.assertNotNull("resourceManager is not initialized", resourceManager);

        Object emailSendingUtility = AccuracyTestsHelper.getField(this.testedInstance, "emailSendingUtility");
        Assert.assertNotNull("emailSendingUtility is not initialized", emailSendingUtility);

        Object managerResourceRoleIds = AccuracyTestsHelper.getField(this.testedInstance, "managerResourceRoleIds");
        Assert.assertNotNull("managerResourceRoleIds is not initialized", managerResourceRoleIds);

        testConfiguredParameter("approvedApplicantEmailSubjectTemplateText",
                "approvedApplicantEmailSubjectTemplateText", config);
        testConfiguredParameter("approvedApplicantEmailBodyTemplatePath",
                "approvedApplicantEmailBodyTemplatePath", config);
        testConfiguredParameter("rejectedApplicantEmailSubjectTemplateText",
                "rejectedApplicantEmailSubjectTemplateText", config);
        testConfiguredParameter("rejectedApplicantEmailBodyTemplatePath", "rejectedApplicantEmailBodyTemplatePath",
                config);
        testConfiguredParameter("rejectedApplicantEmailBodyTemplatePath", "rejectedApplicantEmailBodyTemplatePath",
                config);
        testConfiguredParameter("managerEmailSubjectTemplateText", "managerEmailSubjectTemplateText", config);
        testConfiguredParameter("managerApplicantEmailBodyTemplatePath", "managerApplicantEmailBodyTemplatePath",
                config);
    }

    /**
     * <p>Tests the {@link EmailBasedReviewAssignmentNotificationManager#notifyApprovedApplicants(ReviewAuction, 
     * Project, Map)} method for accuracy.</p>
     *
     * <p>Simply verifies that method executed with no exception. The correctness of sent emails is to be verified
     * manually by examining the mail server logs and intended mailboxes.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testNotifyApprovedApplicants() throws Exception {
        final long auctionId = 100017;
        ReviewAuction reviewAuction = TestDataFactory.getReviewAuction(auctionId);
        List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
        Project project = TestDataFactory.getRelatedProject(auctionId);
        BruteForceBasedReviewAssignmentAlgorithm algorithm = TestDataFactory.getAlgorithm();
        Map<ReviewApplication, ReviewApplicationRole> assignment = algorithm.assign(reviewAuction, reviewApplications);

        this.testedInstance.configure(TestDataFactory.getNotificationManagerConfig(false));
        this.testedInstance.notifyApprovedApplicants(reviewAuction, project, assignment);
    }

    /**
     * <p>Tests the {@link EmailBasedReviewAssignmentNotificationManager#notifyRejectedApplicants(ReviewAuction, 
     * Project, List)} method for accuracy.</p>
     *
     * <p>Simply verifies that method executed with no exception. The correctness of sent emails is to be verified
     * manually by examining the mail server logs and intended mailboxes.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testNotifyRejectedApplicants() throws Exception {
        final long auctionId = 100017;
        ReviewAuction reviewAuction = TestDataFactory.getReviewAuction(auctionId);
        List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
        Project project = TestDataFactory.getRelatedProject(auctionId);
        BruteForceBasedReviewAssignmentAlgorithm algorithm = TestDataFactory.getAlgorithm();
        Map<ReviewApplication, ReviewApplicationRole> assignment = algorithm.assign(reviewAuction, reviewApplications);
        List<Long> rejectedUserIds = new ArrayList<Long>();
        for (ReviewApplication reviewApplication : reviewApplications) {
            if (!assignment.containsKey(reviewApplication)) {
                rejectedUserIds.add(reviewApplication.getUserId());
            }
        }
        if (rejectedUserIds.isEmpty()) {
            Assert.fail("There is invalid test configuration. The list of rejected applications is empty. This should " 
                    + "not count as reviewed code failure");
        }
        this.testedInstance.configure(TestDataFactory.getNotificationManagerConfig(false));
        this.testedInstance.notifyRejectedApplicants(reviewAuction, project, rejectedUserIds);
    }

    /**
     * <p>Tests the {@link EmailBasedReviewAssignmentNotificationManager#notifyManagers(ReviewAuction, Project, Map)} 
     * method for accuracy.</p>
     *
     * <p>Simply verifies that method executed with no exception. The correctness of sent emails is to be verified
     * manually by examining the mail server logs and intended mailboxes.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testNotifyManagers() throws Exception {
        final long auctionId = 100017;
        ReviewAuction reviewAuction = TestDataFactory.getReviewAuction(auctionId);
        List<ReviewApplication> reviewApplications = TestDataFactory.getReviewApplications(auctionId);
        Project project = TestDataFactory.getRelatedProject(auctionId);
        BruteForceBasedReviewAssignmentAlgorithm algorithm = TestDataFactory.getAlgorithm();
        Map<ReviewApplication, ReviewApplicationRole> assignment = algorithm.assign(reviewAuction, reviewApplications);

        this.testedInstance.configure(TestDataFactory.getNotificationManagerConfig(false));
        this.testedInstance.notifyManagers(reviewAuction, project, assignment);
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
