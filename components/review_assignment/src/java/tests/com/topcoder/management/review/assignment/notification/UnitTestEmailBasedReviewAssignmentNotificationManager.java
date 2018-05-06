/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.BaseTestCase;
import com.topcoder.management.review.assignment.MockResourceManager;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;
import com.topcoder.management.review.assignment.ReviewAssignmentNotificationException;
import com.topcoder.search.builder.SearchBuilderException;

/**
 * Testcases for EmailBasedReviewAssignmentNotificationManager class.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTestEmailBasedReviewAssignmentNotificationManager extends BaseTestCase {
    /**
     * Represents EmailBasedReviewAssignmentNotificationManager instance to test.
     */
    private EmailBasedReviewAssignmentNotificationManager instance;

    /**
     * The <code>ConfigurationObject</code> instance used for testing.
     */
    private ConfigurationObject config;

    /**
     * The <code>{@link ReviewAuction}</code> instance used for testing.
     */
    private ReviewAuction reviewAuction;

    /**
     * The <code>{@link Project}</code> instance used for testing.
     */
    private Project project;

    /**
     * The map of <code>{@link ReviewApplication}</code> and <code>{@link ReviewApplicationRole}</code>
     * instance used for testing.
     */
    private Map<ReviewApplication, ReviewApplicationRole> assignment;

    /**
     * The list of unassignedUserId used for testing.
     */
    private List<Long> unassignedUserIds;

    /**
     * setUp the test environment.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        instance = new EmailBasedReviewAssignmentNotificationManager();

        config = getConfigurationObject("test_files/config/config.properties", "notification",
            EmailBasedReviewAssignmentNotificationManager.class.getName());

        instance.configure(config);

        reviewAuction = new ReviewAuction();
        List<Long> openPositions = new ArrayList<Long>();
        openPositions.add(new Long(2));
        reviewAuction.setOpenPositions(openPositions);
        ReviewApplication reviewApplication = new ReviewApplication();
        reviewApplication.setUserId(100001);

        ReviewApplicationRole reviewApplicationRole = new ReviewApplicationRole(1, "Reviewer", 2, null);

        assignment = new HashMap<ReviewApplication, ReviewApplicationRole>();
        assignment.put(reviewApplication, reviewApplicationRole);

        unassignedUserIds = new ArrayList<Long>();
        unassignedUserIds.add(new Long(12345));
        ProjectStatus projectStatus = new ProjectStatus(1, "developement");
        project = new Project(1, ProjectCategory.BANNERS_ICONS, projectStatus);
        project.setProperty("Project Name", "Review Assignment");
        project.setProperty("Project Version", "1.0.0");
    }

    /**
     * tearDown the test environment.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        instance = null;
    }

    /**
     * Accuracy testcase for <code>EmailBasedReviewAssignmentNotificationManager()</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailBasedReviewAssignmentNotificationManagerAccuracy() throws Exception {
        assertNotNull("EmailBasedReviewAssignmentNotificationManager() error.", instance);
    }

    /**
     * Failure tests <code>notifyApprovedApplicants</code> method. with null reviewAuction,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyApprovedApplicantsNullReviewAuction() throws Exception {
        try {
            instance.notifyApprovedApplicants(null, project, assignment);
            fail("testNotifyApprovedApplicantsNullReviewAuction is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyApprovedApplicants</code> method. with null project, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyApprovedApplicantsNullProject() throws Exception {
        try {
            instance.notifyApprovedApplicants(reviewAuction, null, assignment);
            fail("testNotifyApprovedApplicantsNullProject is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyApprovedApplicants</code> method. with null assignment,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyApprovedApplicantsNullAssignment() throws Exception {
        try {
            instance.notifyApprovedApplicants(reviewAuction, project, null);
            fail("testNotifyApprovedApplicantsNullAssignment is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyApprovedApplicants</code> method. with null key in assignment,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyApprovedApplicantsNullInAssignmentKey() throws Exception {
        Map<ReviewApplication, ReviewApplicationRole> a = new HashMap<ReviewApplication, ReviewApplicationRole>();
        a.put(null, new ReviewApplicationRole(1, "Reviewer", 2, null));

        try {
            instance.notifyApprovedApplicants(reviewAuction, project, a);
            fail("testNotifyApprovedApplicantsNullInAssignmentKey is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyApprovedApplicants</code> method. with property is not set,
     * IllegalStateException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyApprovedApplicantsProNotSet() throws Exception {
        try {
            new EmailBasedReviewAssignmentNotificationManager().notifyApprovedApplicants(reviewAuction,
                project, assignment);
            fail("testNotifyApprovedApplicantsProNotSet is failure.");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>notifyApprovedApplicants</code> method. Please check the SMTP server as i
     * use DevNullStmp.jar, you will see the email with content:
     *
     * Date: Fri, 11 Jan 2013 21:21:19 -0800 (PST) From: service@topcoder.com To: email100008@topcoder.com
     * Message-ID: <27959193.1.1357968079171.JavaMail.tc@127.0.0.1> Subject: Subject [Review Assignment 1.0.0
     * with id: 1] has the role [Reviewer] with handler : [handler100008] MIME-Version: 1.0 Content-Type:
     * multipart/mixed; boundary="----=_Part_0_22608339.1357968079109"
     *
     * ------=_Part_0_22608339.1357968079109 Content-Type: text/html; charset=us-ascii
     * Content-Transfer-Encoding: 7bit
     *
     * <p>
     * handler100008, your review application has been approved. Details are provided below. Project ID: 1
     * Project Name: Review Assignment Project Version: 1.0.0 Review application role: Reviewer
     * </p>
     *
     * ------=_Part_0_22608339.1357968079109--
     *
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyApprovedApplicantsAccuracy() throws Exception {
        instance.notifyApprovedApplicants(reviewAuction, project, assignment);
    }

    /**
     * Failure tests <code>notifyRejectedApplicants</code> method. with null reviewAuction,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyRejectedApplicantsNullReviewAuction() throws Exception {
        try {
            instance.notifyRejectedApplicants(null, project, unassignedUserIds);
            fail("testNotifyRejectedApplicantsNullReviewAuction is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyRejectedApplicants</code> method. with null project, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyRejectedApplicantsNullProject() throws Exception {
        try {
            instance.notifyRejectedApplicants(reviewAuction, null, unassignedUserIds);
            fail("testNotifyRejectedApplicantsNullProject is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyRejectedApplicants</code> method. with null unassignedUserIds,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyRejectedApplicantsNullUnassignedUserIds() throws Exception {
        try {
            instance.notifyRejectedApplicants(reviewAuction, project, null);
            fail("testNotifyRejectedApplicantsNullUnassignedUserIds is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyRejectedApplicants</code> method. with null unassignedUserIds,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyRejectedApplicantsNullInUnassignedUserIds() throws Exception {
        unassignedUserIds.add(null);
        try {
            instance.notifyRejectedApplicants(reviewAuction, project, unassignedUserIds);
            fail("testNotifyRejectedApplicantsNullInUnassignedUserIds is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyRejectedApplicants</code> method. with property is not set,
     * IllegalStateException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyRejectedApplicantsProNotSet() throws Exception {
        try {
            new EmailBasedReviewAssignmentNotificationManager().notifyRejectedApplicants(reviewAuction,
                project, unassignedUserIds);
            fail("testNotifyApprovedApplicantsProNotSet is failure.");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>notifyRejectedApplicants</code> method. Please check the SMTP server as i
     * use DevNullStmp.jar, you will see the email with content:
     *
     * Date: Fri, 11 Jan 2013 21:23:40 -0800 (PST) From: service@topcoder.com To: email12345@topcoder.com
     * Message-ID: <3154093.1.1357968220250.JavaMail.tc@127.0.0.1> Subject: rejected suject [Review Assignment
     * 1.0.0 with id: 1] has handler : [handler12345] MIME-Version: 1.0 Content-Type: multipart/mixed;
     * boundary="----=_Part_0_2056742.1357968220218"
     *
     * ------=_Part_0_2056742.1357968220218 Content-Type: text/html; charset=us-ascii
     * Content-Transfer-Encoding: 7bit
     *
     * <p>
     * handler12345, your review application has been rejected. Details are provided below. Project ID: 1
     * Project Name: Review Assignment Project Version: 1.0.0
     * </p>
     *
     * ------=_Part_0_2056742.1357968220218--
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyRejectedApplicantsAccuracy() throws Exception {
        instance.notifyRejectedApplicants(reviewAuction, project, unassignedUserIds);
    }

    /**
     * Failure tests <code>notifyManagers</code> method. with null reviewAuction, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyManagersNullReviewAuction() throws Exception {
        try {
            instance.notifyManagers(null, project, assignment);
            fail("testNotifyManagersNullReviewAuction is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyManagers</code> method. with null project, IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyManagersNullProject() throws Exception {
        try {
            instance.notifyManagers(reviewAuction, null, assignment);
            fail("testNotifyManagersNullProject is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyManagers</code> method. with null assignment, IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyManagersNullAssignment() throws Exception {
        try {
            instance.notifyManagers(reviewAuction, project, null);
            fail("testNotifyManagersNullAssignment is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyManagers</code> method. with null key in assignment, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyManagersNullInAssignmentKey() throws Exception {
        Map<ReviewApplication, ReviewApplicationRole> a = new HashMap<ReviewApplication, ReviewApplicationRole>();
        a.put(null, new ReviewApplicationRole(1, "Reviewer", 2, null));

        try {
            instance.notifyManagers(reviewAuction, project, a);
            fail("testNotifyManagersNullInAssignmentKey is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyManagers</code> method. with property is not set, IllegalStateException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyManagersProNotSet() throws Exception {
        try {
            new EmailBasedReviewAssignmentNotificationManager().notifyManagers(reviewAuction, project,
                assignment);
            fail("testNotifyManagersProNotSet is failure.");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyManagers</code> method with error to search resource,
     * ReviewAssignmentNotificationException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyManagersFail2() throws Exception {
        MockResourceManager mrm = new MockResourceManager();
        mrm.setResult(new SearchBuilderException("SearchBuilderException for testing."));
        setField(EmailBasedReviewAssignmentNotificationManager.class, instance, "resourceManager", mrm);
        try {
            instance.notifyManagers(reviewAuction, project, assignment);
            fail("testNotifyManagersFail2 is failure.");
        } catch (ReviewAssignmentNotificationException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>notifyManagers</code> method with error to search resource,
     * ReviewAssignmentNotificationException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyManagersFail3() throws Exception {
        MockResourceManager mrm = new MockResourceManager();
        mrm.setResult(new ResourcePersistenceException("ResourcePersistenceException for testing."));
        setField(EmailBasedReviewAssignmentNotificationManager.class, instance, "resourceManager", mrm);
        try {
            instance.notifyManagers(reviewAuction, project, assignment);
            fail("testNotifyManagersFail3 is failure.");
        } catch (ReviewAssignmentNotificationException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>notifyManagers</code> method. Please check the SMTP server as i use
     * DevNullStmp.jar, you will see the email with content:
     *
     * Date: Fri, 11 Jan 2013 21:27:55 -0800 (PST) From: service@topcoder.com To: email1000098@topcoder.com
     * Message-ID: <15493888.3.1357968475812.JavaMail.tc@127.0.0.1> Subject: manager subject [Review
     * Assignment 1.0.0 with id: 1] has position [true] with assignment
     * [[{REVIEW_APPLICATION_ROLE_NAME=Reviewer, USER_HANDLE=handler100008}]] MIME-Version: 1.0 Content-Type:
     * multipart/mixed; boundary="----=_Part_2_1683934.1357968475812"
     *
     * ------=_Part_2_1683934.1357968475812 Content-Type: text/html; charset=us-ascii
     * Content-Transfer-Encoding: 7bit
     *
     * <p>
     * Reviewers have been assigned to your project. Details are provided below. Project ID: 1 Project Name:
     * Review Assignment Project Version: 1.0.0 Review assignment:
     *
     * Reviewer handler100008 assigned to role "Reviewer".
     *
     *
     *
     * Please note that there's no enough reviewers for the contest!
     *
     * </p>
     *
     * ------=_Part_2_1683934.1357968475812--
     *
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testNotifyManagersAccuracy() throws Exception {
        instance.notifyManagers(reviewAuction, project, assignment);
    }

    /**
     * Failure tests <code>configure(ConfigurationObject config)</code> method. with null config,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail1() throws Exception {
        try {
            instance.configure(null);
            fail("testConfigureNullConfig is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The approvedApplicantEmailSubjectTemplateText property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail2() throws Exception {
        config.removeProperty("approvedApplicantEmailSubjectTemplateText");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The approvedApplicantEmailSubjectTemplateText property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail3() throws Exception {
        config.setPropertyValue("approvedApplicantEmailSubjectTemplateText", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The approvedApplicantEmailBodyTemplatePath property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail4() throws Exception {
        config.removeProperty("approvedApplicantEmailBodyTemplatePath");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The approvedApplicantEmailBodyTemplatePath property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail5() throws Exception {
        config.setPropertyValue("approvedApplicantEmailBodyTemplatePath", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The rejectedApplicantEmailSubjectTemplateText property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail6() throws Exception {
        config.removeProperty("rejectedApplicantEmailSubjectTemplateText");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The rejectedApplicantEmailSubjectTemplateText property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail7() throws Exception {
        config.setPropertyValue("rejectedApplicantEmailSubjectTemplateText", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The rejectedApplicantEmailBodyTemplatePath property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail8() throws Exception {
        config.removeProperty("rejectedApplicantEmailBodyTemplatePath");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The rejectedApplicantEmailBodyTemplatePath property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail9() throws Exception {
        config.setPropertyValue("rejectedApplicantEmailBodyTemplatePath", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The managerEmailSubjectTemplateText property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail10() throws Exception {
        config.removeProperty("managerEmailSubjectTemplateText");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The managerEmailSubjectTemplateText property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail11() throws Exception {
        config.setPropertyValue("managerEmailSubjectTemplateText", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The managerApplicantEmailBodyTemplatePath property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail12() throws Exception {
        config.removeProperty("managerApplicantEmailBodyTemplatePath");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The managerApplicantEmailBodyTemplatePath property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail13() throws Exception {
        config.setPropertyValue("managerApplicantEmailBodyTemplatePath", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The managerResourceRoleIds property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail14() throws Exception {
        config.removeProperty("managerResourceRoleIds");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The managerResourceRoleIds property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail15() throws Exception {
        config.setPropertyValues("managerResourceRoleIds", new Long[0]);
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The managerResourceRoleIds property is invalid in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail16() throws Exception {
        config.setPropertyValues("managerResourceRoleIds", new Object[] {new Long(1), "a"});

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The objectFactoryConfig property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail19() throws Exception {
        config.removeChild("objectFactoryConfig");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The resourceManagerKey property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail20() throws Exception {
        config.removeProperty("resourceManagerKey");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The resourceManagerKey property is not in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail21() throws Exception {
        config.setPropertyValue("resourceManagerKey", "notfind");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The resourceManagerKey property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail22() throws Exception {
        config.setPropertyValue("resourceManagerKey", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The userRetrievalKey property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail23() throws Exception {
        config.removeProperty("userRetrievalKey");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The userRetrievalKey property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail24() throws Exception {
        config.setPropertyValue("userRetrievalKey", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link EmailBasedReviewAssignmentNotificationManager#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The userRetrievalKey property is not find in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail25() throws Exception {
        config.setPropertyValue("userRetrievalKey", "notfind");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>configure(ConfigurationObject config)</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureAccuracy() throws Exception {
        assertNotNull("userRetrieval field should not be null", getField(instance, "userRetrieval"));

        assertNotNull("resourceManager field should not be null", getField(instance, "resourceManager"));

        assertNotNull("emailSendingUtility field should not be null",
            getField(instance, "emailSendingUtility"));

        assertNotNull("approvedApplicantEmailSubjectTemplateText field should not be null",
            getField(instance, "approvedApplicantEmailSubjectTemplateText"));
        assertNotNull("approvedApplicantEmailBodyTemplatePath field should not be null",
            getField(instance, "approvedApplicantEmailBodyTemplatePath"));

        assertNotNull("rejectedApplicantEmailSubjectTemplateText field should not be null",
            getField(instance, "rejectedApplicantEmailSubjectTemplateText"));
        assertNotNull("rejectedApplicantEmailBodyTemplatePath field should not be null",
            getField(instance, "rejectedApplicantEmailBodyTemplatePath"));

        assertNotNull("managerEmailSubjectTemplateText field should not be null",
            getField(instance, "managerEmailSubjectTemplateText"));
        assertNotNull("managerApplicantEmailBodyTemplatePath field should not be null",
            getField(instance, "managerApplicantEmailBodyTemplatePath"));

        assertNotNull("managerResourceRoleIds field should not be null",
            getField(instance, "managerResourceRoleIds"));

        assertNotNull("log field should be null", getField(instance, "log"));
    }

}
