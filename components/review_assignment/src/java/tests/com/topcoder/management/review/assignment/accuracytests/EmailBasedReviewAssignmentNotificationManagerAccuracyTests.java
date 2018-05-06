/**
 * Copyright (c) 2013, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.assignment.accuracytests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.notification.EmailBasedReviewAssignmentNotificationManager;
import com.topcoder.management.review.assignment.notification.EmailSendingUtility;

/**
 * <p>
 * Unit tests for {@link EmailBasedReviewAssignmentNotificationManager} class. <br/>
 * </p>
 *
 * @author KennyAlive
 * @version 1.0
 */
public class EmailBasedReviewAssignmentNotificationManagerAccuracyTests {
    /**
     * Constant for configuration namespace for accuracy tests.
     */
    private static final String CONFIGURATION = "NotificationManager";

    /**
     * The {@code  EmailBasedReviewAssignmentNotificationManager} instance used for testing.
     */
    private EmailBasedReviewAssignmentNotificationManager instance;

    /**
     * Creates a suite with all test methods for JUnix3.x runner.
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(EmailBasedReviewAssignmentNotificationManagerAccuracyTests.class);
    }

    /**
     * Sets up the test environment.
     *
     * @throws Exception
     *             to JUnit
     */
    @Before
    public void setUp() throws Exception {
        instance = new EmailBasedReviewAssignmentNotificationManager();
        Helper.preloadConfiguration();
    }

    /**
     * Accuracy test for {@code configure} method.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_configure_1() throws Exception {
        Helper.clearDB();
        Helper.insertDB();

        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
        instance.configure(configuration);

        UserRetrieval userRetrieval = (UserRetrieval) Helper.getField(instance, "userRetrieval");
        assertNotNull("The userRetrieval should not be null", userRetrieval);

        ResourceManager resourceManager = (ResourceManager) Helper.getField(instance, "resourceManager");
        assertNotNull("The resourceManager should not be null", resourceManager);

        EmailSendingUtility emailUtility = (EmailSendingUtility) Helper.getField(instance, "emailSendingUtility");
        assertNotNull("The emailSendingUtility should not be null", emailUtility);

        String approvedSubject = (String) Helper.getField(instance, "approvedApplicantEmailSubjectTemplateText");
        assertNotNull("The approvedApplicantEmailSubjectTemplateText should not be null", approvedSubject);
        assertEquals("The approvedApplicantEmailSubjectTemplateText should be correct", "ApprovedSubject",
                approvedSubject);

        String rejectedSubject = (String) Helper.getField(instance, "rejectedApplicantEmailSubjectTemplateText");
        assertNotNull("The rejectedApplicantEmailSubjectTemplateText should not be null", rejectedSubject);
        assertEquals("The rejectedApplicantEmailSubjectTemplateText should be correct", "RejectedSubject",
                rejectedSubject);

        String approvedPath = (String) Helper.getField(instance, "approvedApplicantEmailBodyTemplatePath");
        assertNotNull("The approvedApplicantEmailBodyTemplatePath should not be null", approvedPath);

        String rejectedPath = (String) Helper.getField(instance, "rejectedApplicantEmailBodyTemplatePath");
        assertNotNull("The rejectedApplicantEmailBodyTemplatePath should not be null", rejectedPath);

        String managerSubject = (String) Helper.getField(instance, "managerEmailSubjectTemplateText");
        assertNotNull("The managerEmailSubjectTemplateText should not be null", managerSubject);
        assertEquals("The managerEmailSubjectTemplateText should be correct", "ManagerSubject",
                managerSubject);

        String managerPath = (String) Helper.getField(instance, "managerApplicantEmailBodyTemplatePath");
        assertNotNull("The managerApplicantEmailBodyTemplatePath should not be null", managerPath);

        @SuppressWarnings("unchecked")
        Set<Long> roleIds = (Set<Long>) Helper.getField(instance, "managerResourceRoleIds");
        assertNotNull("The managerResourceRoleIds should not be null", roleIds);
        assertTrue("managerResourceRoleIds shold contain correct role id", roleIds.contains(1L));
        assertTrue("managerResourceRoleIds shold contain correct role id", roleIds.contains(5L));


        Helper.clearDB();
    }

    /**
     * Accuracy test for {@code notifyApprovedApplicants} method.
     *
     * Preconditions: send email to single approved user
     *
     * Expected behavior: no errors, check email on the server manually
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_notifyApprovedApplicants_1() throws Exception {
        Helper.clearDB();
        Helper.insertDB();

        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
        instance.configure(configuration);

        long userId = 2L;
        long projectId = 11L;

        ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
        auction.setProjectId(projectId);
        ReviewApplication application1 = Helper.createReviewApplication(1L, userId, 1L);
        ReviewApplicationRole role = Helper.createReviewApplicationRole(1L);

        Project project = new Project(projectId, ProjectCategory.WIREFRAMES, new ProjectStatus(1L, "testStatus"));
        project.setProperty("Project Name", "Test Project");
        project.setProperty("Project Version", "1.0.0");

        Map<ReviewApplication, ReviewApplicationRole> assignment =
                new HashMap<ReviewApplication, ReviewApplicationRole>();
        assignment.put(application1, role);

        instance.notifyApprovedApplicants(auction, project, assignment);
        Helper.clearDB();
    }

    /**
     * Accuracy test for {@code notifyApprovedApplicants} method.
     *
     * Preconditions: send email to two approved users
     *
     * Expected behavior: no errors, check emails on the server manually
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_notifyApprovedApplicants_2() throws Exception {
        Helper.clearDB();
        Helper.insertDB();

        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
        instance.configure(configuration);

        long userId1 = 2L;
        long userId2 = 3L;
        long projectId = 11L;

        ReviewAuction auction = Helper.createReviewAuction_twoRoles(1L, 1L);
        auction.setProjectId(projectId);
        ReviewApplication application1 = Helper.createReviewApplication(1L, userId1, 1L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, userId2, 2L);
        ReviewApplicationRole role1 = Helper.createReviewApplicationRole(1L, "Role1", 1L);
        ReviewApplicationRole role2 = Helper.createReviewApplicationRole(2L, "Role2", 2L);

        Project project = new Project(projectId, ProjectCategory.WIREFRAMES, new ProjectStatus(1L, "testStatus"));
        project.setProperty("Project Name", "Test Project");
        project.setProperty("Project Version", "1.0.0");

        Map<ReviewApplication, ReviewApplicationRole> assignment =
                new HashMap<ReviewApplication, ReviewApplicationRole>();
        assignment.put(application1, role1);
        assignment.put(application2, role2);

        instance.notifyApprovedApplicants(auction, project, assignment);
        Helper.clearDB();
    }

    /**
     * Accuracy test for {@code notifyRejectedApplicants} method.
     *
     * Preconditions: send email to single rejected user
     *
     * Expected behavior: no errors, check email on the server manually
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_notifyRejectedApplicants_1() throws Exception {
        Helper.clearDB();
        Helper.insertDB();

        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
        instance.configure(configuration);

        long userId = 1L;
        long projectId = 11L;

        ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
        auction.setProjectId(projectId);

        Project project = new Project(projectId, ProjectCategory.WIREFRAMES, new ProjectStatus(1L, "testStatus"));
        project.setProperty("Project Name", "Test Project");
        project.setProperty("Project Version", "1.0.0");

        instance.notifyRejectedApplicants(auction, project, Arrays.asList(userId));
        Helper.clearDB();
    }

    /**
     * Accuracy test for {@code notifyRejectedApplicants} method.
     *
     * Preconditions: send email to two rejected users
     *
     * Expected behavior: no errors, check emails on the server manually
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_notifyRejectedApplicants_2() throws Exception {
        Helper.clearDB();
        Helper.insertDB();

        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
        instance.configure(configuration);

        long userId1 = 1L;
        long userId2 = 4L;
        long projectId = 11L;

        ReviewAuction auction = Helper.createReviewAuction_twoRoles(1L, 1L);
        auction.setProjectId(projectId);

        Project project = new Project(projectId, ProjectCategory.WIREFRAMES, new ProjectStatus(1L, "testStatus"));
        project.setProperty("Project Name", "Test Project");
        project.setProperty("Project Version", "1.0.0");

        instance.notifyRejectedApplicants(auction, project, Arrays.asList(userId1, userId2));
        Helper.clearDB();
    }

    /**
     * Accuracy test for {@code notifyManagers} method.
     *
     * Preconditions: send email to the project with single manager.
     *
     * Expected behavior: no errors, check email on the server manually
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_notifyManagers_1() throws Exception {
        Helper.clearDB();
        Helper.insertDB();

        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
        instance.configure(configuration);

        long userId = 2L;
        long projectId = 15L;

        ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
        auction.setProjectId(projectId);
        ReviewApplication application1 = Helper.createReviewApplication(1L, userId, 1L);
        ReviewApplicationRole role = Helper.createReviewApplicationRole(1L);

        Project project = new Project(projectId, ProjectCategory.WIREFRAMES, new ProjectStatus(1L, "testStatus"));
        project.setProperty("Project Name", "Test Project");
        project.setProperty("Project Version", "1.0.0");

        Map<ReviewApplication, ReviewApplicationRole> assignment =
                new HashMap<ReviewApplication, ReviewApplicationRole>();
        assignment.put(application1, role);

        instance.notifyManagers(auction, project, assignment);
        Helper.clearDB();
    }

    /**
     * Accuracy test for {@code notifyManagers} method.
     *
     * Preconditions: send email to the project with two managers
     *
     * Expected behavior: no errors, check email on the server manually
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_notifyManagers_2() throws Exception {
        Helper.clearDB();
        Helper.insertDB();

        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
        instance.configure(configuration);

        long userId = 2L;
        long projectId = 11L;

        ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
        auction.setProjectId(projectId);
        ReviewApplication application1 = Helper.createReviewApplication(1L, userId, 1L);
        ReviewApplicationRole role = Helper.createReviewApplicationRole(1L);

        Project project = new Project(projectId, ProjectCategory.WIREFRAMES, new ProjectStatus(1L, "testStatus"));
        project.setProperty("Project Name", "Test Project");
        project.setProperty("Project Version", "1.0.0");

        Map<ReviewApplication, ReviewApplicationRole> assignment =
                new HashMap<ReviewApplication, ReviewApplicationRole>();
        assignment.put(application1, role);

        instance.notifyManagers(auction, project, assignment);
        Helper.clearDB();
    }
}
