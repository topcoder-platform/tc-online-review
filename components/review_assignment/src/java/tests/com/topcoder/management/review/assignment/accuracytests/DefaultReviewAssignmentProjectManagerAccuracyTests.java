/**
 * Copyright (c) 2013, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.assignment.accuracytests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.web.ejb.forums.Forums;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.project.DefaultReviewAssignmentProjectManager;
import com.topcoder.web.ejb.forums.ForumsBean;

/**
 * <p>
 * Unit tests for {@link DefaultReviewAssignmentProjectManager} class. <br/>
 * </p>
 *
 * @author KennyAlive
 * @version 1.0
 */
public class DefaultReviewAssignmentProjectManagerAccuracyTests {
    /**
     * Constant for configuration namespace for accuracy tests.
     */
    private static final String CONFIGURATION = "ProjectManager";

    /**
     * The {@code  DefaultReviewAssignmentProjectManager} instance used for testing.
     */
    private DefaultReviewAssignmentProjectManager instance;

    /**
     * Creates a suite with all test methods for JUnix3.x runner.
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DefaultReviewAssignmentProjectManagerAccuracyTests.class);
    }

    /**
     * Sets up the test environment.
     *
     * @throws Exception
     *             to JUnit
     */
    @Before
    public void setUp() throws Exception {
        instance = new DefaultReviewAssignmentProjectManager();
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
        try {
            Helper.clearDB();
            Helper.insertDB();
            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            UserRetrieval userRetrieval = (UserRetrieval) Helper.getField(instance, "userRetrieval");
            assertNotNull("The userRetrieval should not be null", userRetrieval);

            PhaseManager phaseManager = (PhaseManager) Helper.getField(instance, "phaseManager");
            assertNotNull("The phaseManager should not be null", phaseManager);

            ResourceManager resourceManager = (ResourceManager) Helper.getField(instance, "resourceManager");
            assertNotNull("The resourceManager should not be null", resourceManager);

            @SuppressWarnings("unchecked")
            Map<Long, ResourceRole> resourceRoleById = (Map<Long, ResourceRole>) Helper.getField(instance,
                    "resourceRoleById");
            assertNotNull("The resourceRoleById should not be null", resourceRoleById);

            String registrationDateFormatString = (String) Helper.getField(instance, "registrationDateFormatString");
            assertNotNull("The registrationDateFormatString should not be null", registrationDateFormatString);
            assertEquals("The registrationDateFormatString should be correct", "MM.dd.yyyy hh:mm a",
                    registrationDateFormatString);

            @SuppressWarnings("unchecked")
            Map<Long, Long> phaseTypeExtensionRules = (Map<Long, Long>) Helper.getField(instance,
                    "phaseTypeExtensionRules");
            assertNotNull("The phaseTypeExtensionRules should not be null", phaseTypeExtensionRules);
            assertEquals("Extension for Screening phase should be correct", 86400L, phaseTypeExtensionRules.get(3L)
                    .longValue());
            assertEquals("Extension for Review phase should be correct", 172800L, phaseTypeExtensionRules.get(4L)
                    .longValue());

            String operator = (String) Helper.getField(instance, "operator");
            assertNotNull("The operator should not be null", operator);
            assertEquals("The operator should be correct", "4", operator);

            Object forumsBeanUrl = Helper.getField(instance, "forumsBeanUrl");
            assertNotNull("The forumsBeanUrl should not be null", forumsBeanUrl);

            Object forumsBeanName = Helper.getField(instance, "forumsBeanName");
            assertNotNull("forumsBeanName is not initialized", forumsBeanName);

            Object forumsBeanFactory = Helper.getField(instance, "forumsBeanFactory");
            assertNotNull("forumsBeanFactory is not initialized", forumsBeanFactory);

        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code addReviewersToProject} method.
     *
     * Preconditions: add single reviewer to the project
     *
     * Expected behavior: no errors, the reviewers should be added to the project as resource
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_addReviewersToProject_1() throws Exception {
        try {
            Helper.clearDB();
            Helper.insertDB();

            ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION);
            instance.configure(configuration);

            long userId = 2L;
            long projectId = 11L;
            ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
            auction.setProjectId(projectId);
            ReviewApplication application1 = Helper.createReviewApplication(1L, userId, 1L);
            ReviewApplicationRole role = Helper.createReviewApplicationRole(1L, "Primary Reviewer", 1L);

            Project project = new Project(projectId, ProjectCategory.WIREFRAMES, new ProjectStatus(1L, "testStatus"));
            project.setProperty("Project Name", "Test Project");
            project.setProperty("Project Version", "1.0.0");

            Map<ReviewApplication, ReviewApplicationRole> assignment =
                    new HashMap<ReviewApplication, ReviewApplicationRole>();
            assignment.put(application1, role);

            long resourcesCount = Helper.getProjectResourcesCount(projectId);
            instance.addReviewersToProject(auction, project, assignment);
            long addedReviewersCount = Helper.getProjectResourcesCount(projectId) - resourcesCount;
            assertEquals("The reviewers count should be correct", 1L, addedReviewersCount);
        } finally {
            Helper.clearDB();
        }
    }

    /**
     * Accuracy test for {@code addReviewersToProject} method.
     * 
     * Preconditions: add two reviewers to the project
     * 
     * Expected behavior: no errors, the reviewers should be added to the project as resource
     * 
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_addReviewersToProject_2() throws Exception {
        try {
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
            ReviewApplicationRole role1 = Helper.createReviewApplicationRole(1L, "Primary Reviewer", 1L);
            ReviewApplicationRole role2 = Helper.createReviewApplicationRole(2L, "Secondary Reviewer", 1L);

            Project project = new Project(projectId, ProjectCategory.WIREFRAMES, new ProjectStatus(1L, "testStatus"));
            project.setProperty("Project Name", "Test Project");
            project.setProperty("Project Version", "1.0.0");

            Map<ReviewApplication, ReviewApplicationRole> assignment =
                    new HashMap<ReviewApplication, ReviewApplicationRole>();
            assignment.put(application1, role1);
            assignment.put(application2, role2);

            long resourcesCount = Helper.getProjectResourcesCount(projectId);
            instance.addReviewersToProject(auction, project, assignment);
            long addedReviewersCount = Helper.getProjectResourcesCount(projectId) - resourcesCount;
            assertEquals("The reviewers count should be correct", 2L, addedReviewersCount);
        } finally {
            Helper.clearDB();
        }
    }
}
