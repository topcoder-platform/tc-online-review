/*
 * Copyright (C) 2013-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.Helper;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;
import com.topcoder.management.review.assignment.ReviewAssignmentNotificationException;
import com.topcoder.management.review.assignment.ReviewAssignmentNotificationManager;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.log.Log;
import com.topcoder.util.objectfactory.ObjectFactory;

/**
 * <p>
 * Email based implementation of ReviewAssignmentNotificationManager, which sends notifications related to
 * review assignment.
 * </p>
 * <p>
 * It uses EmailSendingUtility to send email and uses UserRetrieval and ResourceManager to retrieve necessary
 * information.
 * </p>
 * <p>
 * This class performs the logging of errors and debug information using Logging Wrapper component.
 * </p>
 *
 * <p>
 * <em>Sample Configuration:</em>
 *
 * <pre>
 * &lt;?xml version="1.0"?&gt;
 * &lt;CMConfig&gt;
 * &lt;Config name="com.topcoder.management.review.assignment.notification.
 * EmailBasedReviewAssignmentNotificationManager"&gt;
 * &lt;Property name="loggerName"&gt;
 * &lt;Value&gt;myLogger&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="connectionName"&gt;
 * &lt;Value&gt;informix_connection&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="objectFactoryConfig"&gt;
 * &lt;property name="resourceManager"&gt;
 * &lt;property name="type"&gt;
 * &lt;value&gt;com.topcoder.management.review.assignment.MockResourceManager&lt;/value&gt;
 * &lt;/property&gt;
 * &lt;/property&gt;
 * &lt;property name="DBConnectionFactory"&gt;
 * &lt;property name="type"&gt;
 * &lt;value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/value&gt;
 * &lt;/property&gt;
 * &lt;Property name="params"&gt;
 * &lt;Property name="param1"&gt;
 * &lt;Property name="type"&gt;
 * &lt;Value&gt;String&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="value"&gt;
 * &lt;Value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/property&gt;
 * &lt;property name="SearchBundleManager"&gt;
 * &lt;property name="type"&gt;
 * &lt;value&gt;com.topcoder.search.builder.SearchBundleManager&lt;/value&gt;
 * &lt;/property&gt;
 * &lt;Property name="params"&gt;
 * &lt;Property name="param1"&gt;
 * &lt;Property name="type"&gt;
 * &lt;Value&gt;String&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="value"&gt;
 * &lt;Value&gt;com.topcoder.search.builder.SearchBundleManager&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/property&gt;
 *
 * &lt;property name="userRetrieval"&gt;
 * &lt;property name="type"&gt;
 * &lt;value&gt;com.topcoder.management.review.assignment.MockUserRetrieval&lt;/value&gt;
 * &lt;/property&gt;
 * &lt;/property&gt;
 *
 * &lt;/Property&gt;
 *
 * &lt;Property name="dbConnectionFactoryConfig"&gt;
 * &lt;Property name="com.topcoder.db.connectionfactory.DBConnectionFactoryImpl"&gt;
 * &lt;Property name="connections"&gt;
 * &lt;Property name="informix_connection"&gt;
 * &lt;Property name="producer"&gt;
 * &lt;Value&gt;com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="parameters"&gt;
 * &lt;Property name="jdbc_driver"&gt;
 * &lt;Value&gt;com.informix.jdbc.IfxDriver&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="jdbc_url"&gt;
 * &lt;Value&gt;jdbc:informix-sqli://127.0.0.1:9088/tcs:informixserver=ol_svr_custom&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="user"&gt;
 * &lt;Value&gt;informix&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="password"&gt;
 * &lt;Value&gt;123456&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="approvedApplicantEmailSubjectTemplateText"&gt;
 * &lt;Value&gt;Subject [%PROJECT_NAME% %PROJECT_VERSION% with id\: %PROJECT_ID%] has the
 * role [%REVIEW_APPLICATION_ROLE_NAME%] with handler \: [%USER_HANDLE%]&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="approvedApplicantEmailBodyTemplatePath"&gt;
 * &lt;Value&gt;E:/review_assignment_1.0.0/test_files/approvedApplicantEmailBodyTemplate.txt&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="rejectedApplicantEmailSubjectTemplateText"&gt;
 * &lt;Value&gt;rejected subject [%PROJECT_NAME% %PROJECT_VERSION% with id\: %PROJECT_ID%]
 * has handler \: [%USER_HANDLE%]&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="rejectedApplicantEmailBodyTemplatePath"&gt;
 * &lt;Value&gt;E:/review_assignment_1.0.0/test_files/rejectedApplicantEmailBodyTemplate.txt&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="managerEmailSubjectTemplateText"&gt;
 * &lt;Value&gt;manager subject [%PROJECT_NAME% %PROJECT_VERSION% with id\: %PROJECT_ID%]
 * has position [%HAS_OPEN_POSITION%] with assignment [%ASSIGNMENTS%]&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="managerApplicantEmailBodyTemplatePath"&gt;
 * &lt;Value&gt;test_files/managerApplicantEmailBodyTemplate.txt&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="managerResourceRoleIds"&gt;
 * &lt;Value&gt;1&lt;/Value&gt;
 * &lt;Value&gt;2&lt;/Value&gt;
 * &lt;Value&gt;3&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="emailSender"&gt;
 * &lt;Value&gt;service@topcoder.com&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="userRetrievalKey"&gt;
 * &lt;Value&gt;userRetrieval&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="resourceManagerKey"&gt;
 * &lt;Value&gt;resourceManager&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * </p>
 *
 * <p>
 * <em>Sample Code:</em>
 *
 * <pre>
 * ReviewAssignmentNotificationManager ranm = new EmailBasedReviewAssignmentNotificationManager();
 *
 * ConfigurationObject ranmConfig = getConfigurationObject(
 *     &quot;test_files/config/EmailBasedReviewAssignmentNotificationManager.xml&quot;,
 *     EmailBasedReviewAssignmentNotificationManager.class.getName());
 * ranm.configure(ranmConfig);
 *
 * reviewAuction = new ReviewAuction();
 * openPositions = new ArrayList&lt;Long&gt;();
 * openPositions.add(new Long(2));
 * reviewAuction.setOpenPositions(openPositions);
 * ReviewApplication reviewApplication = new ReviewApplication();
 * reviewApplication.setUserId(100008);
 *
 * assignment = new HashMap&lt;ReviewApplication, ReviewApplicationRole&gt;();
 * assignment.put(reviewApplication, reviewApplicationRole);
 *
 * List&lt;Long&gt; unassignedUserIds = new ArrayList&lt;Long&gt;();
 * unassignedUserIds.add(new Long(12345));
 * ProjectStatus projectStatus = new ProjectStatus(1, &quot;development&quot;);
 * Project project = new Project(1, ProjectCategory.BANNERS_ICONS, projectStatus);
 * project.setProperty(&quot;Project Name&quot;, &quot;Review Assignment&quot;);
 * project.setProperty(&quot;Project Version&quot;, &quot;1.0.0&quot;);
 *
 * // Show how to use notify
 * ranm.notifyApprovedApplicants(reviewAuction, project, assignment);
 *
 * ranm.notifyRejectedApplicants(reviewAuction, project, unassignedUserIds);
 *
 * ranm.notifyManagers(reviewAuction, project, assignment);
 * </pre>
 *
 * </p>
 *
 * <p>
 * Thread Safety: This class is mutable and not thread safe since it uses ResourceManager instance that is not
 * thread safe.
 * </p>
 * <p>
 * It's assumed that {@link #configure(ConfigurationObject)} method will be called just once right after
 * instantiation, before calling any business methods.
 * </p>
 *
 * @author gevak, zhongqiangzhang
 * @version 1.2.1
 */
public class EmailBasedReviewAssignmentNotificationManager implements ReviewAssignmentNotificationManager {
    /**
     * <p>
     * Represent the class name.
     * </p>
     */
    private static final String CLASS_NAME = EmailBasedReviewAssignmentNotificationManager.class.getName();

    /**
     * <p>
     * Represents &quot;userRetrievalKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_USER_RETRIEVAL = "userRetrievalKey";

    /**
     * <p>
     * Represents &quot;resourceManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_RESOURCE_MANAGER = "resourceManagerKey";

    /**
     * <p>
     * Represents &quot;approvedApplicantEmailSubjectTemplateText&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_APPROVED_APP_EMAIL_SUB_TEMPLATE = "approvedApplicantEmailSubjectTemplateText";

    /**
     * <p>
     * Represents &quot;approvedApplicantEmailBodyTemplatePath&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_APPROVED_APP_EMAIL_BODY_TEMPLATE_PATH = "approvedApplicantEmailBodyTemplatePath";

    /**
     * <p>
     * Represents &quot;rejectedApplicantEmailSubjectTemplateText&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REJECTED_APP_EMAIL_SUB_TEMPLATE = "rejectedApplicantEmailSubjectTemplateText";

    /**
     * <p>
     * Represents &quot;rejectedApplicantEmailBodyTemplatePath&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REJECTED_APP_EMAIL_BODY_TEMPLATE_PATH = "rejectedApplicantEmailBodyTemplatePath";

    /**
     * <p>
     * Represents &quot;managerEmailSubjectTemplateText&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_MANAGER_EMAIL_SUB_TEMPLATE = "managerEmailSubjectTemplateText";

    /**
     * <p>
     * Represents &quot;managerApplicantEmailBodyTemplatePath&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_MANAGER_APP_EMAIL_BODY_TEMPLATE_PATH = "managerApplicantEmailBodyTemplatePath";

    /**
     * <p>
     * Represents &quot;managerResourceRoleIds&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_MANAGER_RESOURCE_ROLE_IDS = "managerResourceRoleIds";

    /**
     * <p>
     * Represents &quot;emailSender&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_EMAIL_SENDER = "emailSender";

    /**
     * <p>
     * Service for retrieving user information.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be null
     * after initialization. Is used in notifyApprovedApplicants, notifyRejectedApplicants and notifyManagers
     * methods.
     * </p>
     */
    private UserRetrieval userRetrieval;
    /**
     * <p>
     * Service for retrieving resource information.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be null
     * after initialization. Is used in notifyApprovedApplicants, notifyRejectedApplicants and notifyManagers
     * methods.
     * </p>
     */
    private ResourceManager resourceManager;
    /**
     * <p>
     * The email sending utility to be used by this class.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be null
     * after initialization. Is used in notifyApprovedApplicants, notifyRejectedApplicants and notifyManagers
     * methods.
     * </p>
     */
    private EmailSendingUtility emailSendingUtility;
    /**
     * <p>
     * The logger used by this class for logging errors and debug information. Is initialized in the
     * {@link #configure(ConfigurationObject)} method and never changed after that.
     * </p>
     * <p>
     * If is null after initialization, logging is not performed. Is used in business methods.
     * </p>
     */
    private Log log;
    /**
     * <p>
     * The email subject template text to be used for constructing messages to be sent to the approved
     * applicants.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be
     * null/empty after initialization. Is used in notifyApprovedApplicants, notifyRejectedApplicants and
     * notifyManagers methods.
     * </p>
     */
    private String approvedApplicantEmailSubjectTemplateText;
    /**
     * <p>
     * The email body template path (resource path or file path) to be used for constructing messages to be
     * approved applicants.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be
     * null/empty after initialization. Is used in notifyApprovedApplicants, notifyRejectedApplicants and
     * notifyManagers methods.
     * </p>
     */
    private String approvedApplicantEmailBodyTemplatePath;

    /**
     * <p>
     * The email subject template text to be used for constructing messages to be sent to the rejected
     * applicants.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be
     * null/empty after initialization. Is used in notifyApprovedApplicants, notifyRejectedApplicants and
     * notifyManagers methods.
     * </p>
     */
    private String rejectedApplicantEmailSubjectTemplateText;

    /**
     * <p>
     * The email body template path (resource path or file path) to be used for constructing messages to be
     * rejected applicants.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be
     * null/empty after initialization. Is used in notifyApprovedApplicants, notifyRejectedApplicants and
     * notifyManagers methods.
     * </p>
     */
    private String rejectedApplicantEmailBodyTemplatePath;

    /**
     * <p>
     * The email subject template text to be used for constructing messages to be sent to the managers.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be
     * null/empty after initialization. Is used in notifyApprovedApplicants, notifyRejectedApplicants and
     * notifyManagers methods.
     * </p>
     */
    private String managerEmailSubjectTemplateText;

    /**
     * <p>
     * The email body template path (resource path or file path) to be used for constructing messages to be
     * sent to the managers.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be
     * null/empty after initialization. Is used in notifyApprovedApplicants, notifyRejectedApplicants and
     * notifyManagers methods.
     * </p>
     */
    private String managerApplicantEmailBodyTemplatePath;

    /**
     * <p>
     * The set of IDs of the manager resource roles that represent managers to be notified about review
     * assignment.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be
     * null/empty or contain null element after initialization. Is used in notifyApprovedApplicants,
     * notifyRejectedApplicants and notifyManagers methods.
     * </p>
     */
    private Set<Long> managerResourceRoleIds;

    /**
     * Create an instance of EmailBasedReviewAssignmentNotificationManager.
     */
    public EmailBasedReviewAssignmentNotificationManager() {
    }

    /**
     * <p>
     * Notifies members who were assigned to a review application role (i.e. those who have one Approved
     * review application).
     * </p>
     * <p>
     * The content of the email will be created based on a configurable email template with the following
     * fields:
     * </p>
     * <ol>
     * <li>Project Name</li>
     * <li>Project Version</li>
     * <li>Project ID</li>
     * <li>Project Category</li>
     * <li>TC Direct Project ID</li>
     * <li>TC Direct Project Name</li>
     * <li>Member handle</li>
     * <li>Review application role name (i.e. ReviewApplicationRole#name value)</li>
     * </ol>
     *
     * @param reviewAuction
     *            the ReviewAuction instance.
     * @param project
     *            the Project instance.
     * @param assignment
     *            Set of approved review applications.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or assignment contains null key.
     * @throws IllegalStateException
     *             if this class was not configured properly with use of
     *             {@link #configure(ConfigurationObject)} method.
     * @throws ReviewAssignmentNotificationException
     *             If any error occurs with populating and sending a notification.
     */
    public void notifyApprovedApplicants(ReviewAuction reviewAuction, Project project,
                                         Set<ReviewApplication> assignment)
        throws ReviewAssignmentNotificationException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME
            + ".notifyApprovedApplicants(ReviewAuction, Project, Map<ReviewApplication, ReviewApplicationRole>)";
        Helper.logEntrance(log, signature, new String[] { "reviewAuction", "project", "assignment" },
            new Object[] { reviewAuction, project, assignment });

        Helper.checkNullIAE(log, signature, reviewAuction, "reviewAuction");
        Helper.checkNullIAE(log, signature, project, "project");
        Helper.checkNullIAE(log, signature, assignment, "assignment");
        Helper.checkListIAE(log, signature, assignment, "assignment|Key");

        Helper.checkState(userRetrieval, "userRetrieval", log, signature);

        Map<String, Object> params = buildProjectParams(project, signature);

        Set<Long> userIDs = new HashSet<Long>();
        for (ReviewApplication reviewApplication : assignment) {
            userIDs.add(reviewApplication.getUserId());
        }
        try {
            // Set external users
            Map<Long, ExternalUser> users = retrieveUsersInfo(userIDs);

            for (ReviewApplication reviewApplication : assignment) {
                if (users.get(reviewApplication.getUserId()) != null) {
                    // Get ReviewApplicationRole associated with this review application.
                    ReviewApplicationRole applicationRole = Helper.getReviewApplicationRoleByID(reviewAuction,
                            reviewApplication.getApplicationRoleId());

                    // Populate specific email parameters.
                    params.put("USER_HANDLE", users.get(reviewApplication.getUserId()).getHandle());
                    params.put("REVIEW_APPLICATION_ROLE_NAME", applicationRole.getName());

                    String emailAddress = users.get(reviewApplication.getUserId()).getEmail();

                    // Send email.
                    emailSendingUtility.sendEmail(approvedApplicantEmailSubjectTemplateText,
                        approvedApplicantEmailBodyTemplatePath, emailAddress, params);

                    Helper.logInfo(log, "Sending email [" + emailAddress + "] with email type [Approved email].");
                }
            }
        } catch (RetrievalException e) {
            throw Helper.logException(log, signature, new ReviewAssignmentNotificationException(
                "Error to retrieve ExternalUser.", e));
        } catch (NullPointerException e) {
            // when the externalUsers is not in db so that NPE throws when getting handle etc.
            throw Helper.logException(log, signature, new ReviewAssignmentNotificationException(
                "Error to retrieve ExternalUser.", e));
        }

        Helper.logExit(log, signature, null, start);
    }

    /**
     * Notifies members who were not assigned to any review application role (i.e. those whose all review
     * applications are Rejected).
     *
     * The content of the email will be created based on a configurable email template with the following
     * fields:
     * <ol>
     * <li>Project Name</li>
     * <li>Project Version</li>
     * <li>Project ID</li>
     * <li>Project Category</li>
     * <li>TC Direct Project ID</li>
     * <li>TC Direct Project Name</li>
     * <li>Member handle</li>
     * </ol>
     *
     * @param reviewAuction
     *            the Review auction.
     * @param project
     *            the Project.
     * @param unassignedUserIds
     *            the User IDs of unassigned applicants.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or unassignedUserIds contains null.
     * @throws IllegalStateException
     *             if this class was not configured properly with use of
     *             {@link #configure(ConfigurationObject)} method.
     * @throws ReviewAssignmentNotificationException
     *             If any error occurs with populating and sending a notification.
     */
    public void notifyRejectedApplicants(ReviewAuction reviewAuction, Project project,
        List<Long> unassignedUserIds) throws ReviewAssignmentNotificationException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME
            + ".notifyRejectedApplicants(ReviewAuction, Project, Map<ReviewApplication, ReviewApplicationRole>)";
        Helper.logEntrance(log, signature, new String[] { "reviewAuction", "project", "unassignedUserIds" },
            new Object[] { reviewAuction, project, unassignedUserIds });

        Helper.checkNullIAE(log, signature, reviewAuction, "reviewAuction");
        Helper.checkNullIAE(log, signature, project, "project");
        Helper.checkListIAE(log, signature, unassignedUserIds, "unassignedUserIds");

        Helper.checkState(userRetrieval, "userRetrieval", log, signature);

        Map<String, Object> params = buildProjectParams(project, signature);

        Map<Long, ExternalUser> users;
        try {
            // Set external users
            users = retrieveUsersInfo(unassignedUserIds);

        } catch (RetrievalException e) {
            throw Helper.logException(log, signature,
                new ReviewAssignmentNotificationException(e.getMessage(), e));
        }

        for (long userId : users.keySet()) {
            // Populate specific email parameters, handle and email in ExternalUser will never be null.
            params.put("USER_HANDLE", users.get(userId).getHandle());

            String emailAddress = users.get(userId).getEmail();

            // Send email.
            emailSendingUtility.sendEmail(rejectedApplicantEmailSubjectTemplateText,
                rejectedApplicantEmailBodyTemplatePath, emailAddress, params);

            Helper.logInfo(log, "Sending email [" + emailAddress + "] with email type [Rejected email].");
        }

        Helper.logExit(log, signature, null, start);
    }

    /**
     * Build the parameters for project, 'Project Name', 'Project Version' and project id parameters will be
     * built.
     *
     * @param project
     *            the project to build
     * @param signature
     *            the signature call this method used to log.
     * @return the project parameters.
     *
     * @throws IllegalArgumentException
     *             If 'Project Name', 'Project Version' or project id is not set.
     */
    private Map<String, Object> buildProjectParams(Project project, String signature) {
        Helper.checkNullIAE(log, signature, project.getProperty("Project Name"), "project|Name");
        Helper.checkNullIAE(log, signature, project.getProperty("Project Version"), "project|Version");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PROJECT_NAME", project.getProperty("Project Name"));
        params.put("PROJECT_VERSION", project.getProperty("Project Version"));
        params.put("PROJECT_ID", project.getId());

        params.put("PROJECT_CATEGORY", project.getProjectCategory().getName());
        params.put("TC_DIRECT_PROJECT_ID", project.getTcDirectProjectId());
        params.put("TC_DIRECT_PROJECT_NAME",
            project.getTcDirectProjectName() != null ? project.getTcDirectProjectName() : "N/A");
        return params;
    }

    /**
     * Notifies all PMs/copilot about review assignment.
     *
     * The content of the email will be created based on a configurable email template with the following
     * fields:
     * <ol>
     * <li>Project Name</li>
     * <li>Project Version</li>
     * <li>Project ID</li>
     * <li>Project Category</li>
     * <li>TC Direct Project ID</li>
     * <li>TC Direct Project Name</li>
     * <li>OPEN_POSITIONS</li>
     * <li>
     * For each approved review application: a. Member's handle. b. The name of the review application role
     * that was assigned to that member (i.e. ReviewApplicationRole#name value).</li>
     * </ol>
     *
     * @param reviewAuction
     *            the Review auction.
     * @param project
     *            the Project.
     * @param assignment
     *            Set of approved review applications.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or assignment contains null key.
     * @throws IllegalStateException
     *             if this class was not configured properly with use of
     *             {@link #configure(ConfigurationObject)} method.
     * @throws ReviewAssignmentNotificationException
     *             If any error occurs with populating and sending a notification.
     */
    public void notifyManagers(ReviewAuction reviewAuction, Project project,
                               Set<ReviewApplication> assignment)
        throws ReviewAssignmentNotificationException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME
            + ".notifyManagers(ReviewAuction, Project, Map<ReviewApplication, ReviewApplicationRole>)";
        Helper.logEntrance(log, signature, new String[] { "reviewAuction", "project", "assignment" },
            new Object[] { reviewAuction, project, assignment });

        Helper.checkNullIAE(log, signature, reviewAuction, "reviewAuction");
        Helper.checkNullIAE(log, signature, project, "project");
        Helper.checkNullIAE(log, signature, assignment, "assignment");
        Helper.checkListIAE(log, signature, assignment, "assignment|Key");

        Helper.checkState(userRetrieval, "userRetrieval", log, signature);

        Helper.checkNullIAE(log, signature, reviewAuction.getOpenPositions(), "reviewAuction|OpenPositions");

        // Populate common email parameters
        Map<String, Object> params = buildProjectParams(project, signature);

        // determine if there is any open position remaining
        long remainingOpenPositions = 0;
        for (long x : reviewAuction.getOpenPositions()) {
            remainingOpenPositions += x;
        }
        remainingOpenPositions -= assignment.size();
        params.put("OPEN_POSITIONS", remainingOpenPositions);

        try {
            Set<Long> managerUserIds = getManagerUserIds(project, signature);
            Set<Long> allUserIDs = new HashSet<Long>(); 
                    
            // Find user IDs of applicants.
            for (ReviewApplication reviewApplication : assignment) {
                Helper.checkNullIAE(log, signature, reviewApplication.getUserId(), "assignment|Key|userId");
                allUserIDs.add(reviewApplication.getUserId());
            }
            for (Long managerId : managerUserIds) {
                allUserIDs.add(managerId);
            }

            // Set external users
            Map<Long, ExternalUser> users = retrieveUsersInfo(allUserIDs);

            // Populate assignment specific parameters:
            List<Map<String, String>> assignmentsParams = new ArrayList<Map<String, String>>();
            for (ReviewApplication reviewApplication : assignment) {
                if (users.get(reviewApplication.getUserId()) != null) {
                    // Get ReviewApplicationRole associated with this review application.
                    ReviewApplicationRole applicationRole = Helper.getReviewApplicationRoleByID(reviewAuction,
                            reviewApplication.getApplicationRoleId());

                    Map<String, String> assignmentParams = new HashMap<String, String>();
                    assignmentParams.put("USER_HANDLE", users.get(reviewApplication.getUserId()).getHandle());
                    assignmentParams.put("REVIEW_APPLICATION_ROLE_NAME", applicationRole.getName());
                    assignmentsParams.add(assignmentParams);
                }
            }
            params.put("ASSIGNMENTS", assignmentsParams);

            // Send email to managers.
            for (long managerId : managerUserIds) {
                ExternalUser eu = users.get(managerId);
                if (eu != null) {
                    String emailAddress = users.get(managerId).getEmail();

                    emailSendingUtility.sendEmail(managerEmailSubjectTemplateText,
                        managerApplicantEmailBodyTemplatePath, emailAddress, params);

                    Helper.logInfo(log, "Sending email [" + emailAddress + "] with email type [PM email].");
                }
            }
        } catch (RetrievalException e) {
            throw Helper.logException(log, signature, new ReviewAssignmentNotificationException(
                "Error to retrieve user caused by " + e.getMessage(), e));
        } catch (NullPointerException e) {
            // It caused by invalid data from db, so it is not IAE.
            throw Helper.logException(log, signature, new ReviewAssignmentNotificationException(
                "Invalid retrieved users caused by " + e.getMessage(), e));
        }

        Helper.logExit(log, signature, null, start);
    }

    /**
     * Get the user ids of manager for the given Project.
     *
     * @param project
     *            the project to get user id.
     * @param signature
     *            the method signature of notifyManagers used to log.
     * @return the retrieved user ids of manager for the given Project.
     * @throws ReviewAssignmentNotificationException
     *             if error to get the user ids of manager for the given Project.
     */
    private Set<Long> getManagerUserIds(Project project, String signature)
        throws ReviewAssignmentNotificationException {
        try {
            Filter resourceRoleIdFilter;
            if (managerResourceRoleIds.size() == 1) {
                // Create filter for filtering resource roles by the only ID.
                resourceRoleIdFilter = ResourceFilterBuilder
                    .createResourceRoleIdFilter(managerResourceRoleIds.iterator().next());
            } else {
                // Create a list for individual resource role ID filters.
                List<Filter> individualResourceRoleIdFilters = new ArrayList<Filter>();
                for (Long managerResourceRoleId : managerResourceRoleIds) {
                    // Create filter for the next resource role ID.
                    Filter individualResourceRoleIdFilter = ResourceFilterBuilder
                        .createResourceRoleIdFilter(managerResourceRoleId);
                    // Add filter to the list.
                    individualResourceRoleIdFilters.add(individualResourceRoleIdFilter);
                }
                // Create filter matching resources with manager roles.
                resourceRoleIdFilter = new OrFilter(individualResourceRoleIdFilters);
            }

            // Get manager resources for the project.
            Resource[] resources = resourceManager.searchResources(new AndFilter(ResourceFilterBuilder
                .createProjectIdFilter(project.getId()), resourceRoleIdFilter));

            // Create a set for manager user IDs.
            Set<Long> managerUserIds = new HashSet<Long>();
            for (Resource resource : resources) {
                Long userId = resource.getUserId();
                if (userId == null) {
                    throw Helper.logException(log, signature, new ReviewAssignmentNotificationException(
                            "User ID is null for resource [" + resource.getId() + "]"));
                }

                // Add user ID to the set. Exclude the "dummy" users (i.e. "Components", "Applications" etc.)
                if (userId != 22719217 && userId != 22770213 && userId != 22873364) {
                    managerUserIds.add(userId);
                }
            }
            return managerUserIds;
        } catch (ResourcePersistenceException e) {
            throw Helper.logException(log, signature, new ReviewAssignmentNotificationException(
                "Error to get manager resources caused by " + e.getMessage(), e));
        } catch (SearchBuilderException e) {
            throw Helper.logException(log, signature, new ReviewAssignmentNotificationException(
                "Error to get manager resources caused by " + e.getMessage(), e));
        }
    }

    /**
     * Configures this instance using the given configuration object. See section 3.2 of CS for details
     * about the configuration parameters.
     *
     * @param config
     *            the configuration object
     *
     * @throws IllegalArgumentException
     *             if config is null
     * @throws ReviewAssignmentConfigurationException
     *             if some error occurred when initializing an instance using the given configuration
     */
    public void configure(ConfigurationObject config) {
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        this.log = Helper.getLog(config);

        // create object factory
        ObjectFactory objectFactory = Helper.createObjectFactory(config);

        this.resourceManager = Helper.createObject(config, objectFactory, KEY_RESOURCE_MANAGER,
            ResourceManager.class);

        this.userRetrieval = Helper.createObject(config, objectFactory, KEY_USER_RETRIEVAL,
            UserRetrieval.class);

        this.approvedApplicantEmailSubjectTemplateText = Helper.getPropertyValue(config,
            KEY_APPROVED_APP_EMAIL_SUB_TEMPLATE, true);

        this.approvedApplicantEmailBodyTemplatePath = Helper.getPropertyValue(config,
            KEY_APPROVED_APP_EMAIL_BODY_TEMPLATE_PATH, true);

        this.rejectedApplicantEmailSubjectTemplateText = Helper.getPropertyValue(config,
            KEY_REJECTED_APP_EMAIL_SUB_TEMPLATE, true);

        this.rejectedApplicantEmailBodyTemplatePath = Helper.getPropertyValue(config,
            KEY_REJECTED_APP_EMAIL_BODY_TEMPLATE_PATH, true);

        this.managerEmailSubjectTemplateText = Helper.getPropertyValue(config,
            KEY_MANAGER_EMAIL_SUB_TEMPLATE, true);

        this.managerApplicantEmailBodyTemplatePath = Helper.getPropertyValue(config,
            KEY_MANAGER_APP_EMAIL_BODY_TEMPLATE_PATH, true);

        Long[] resourceRoleIds = Helper.getLongPropertyValues(config, KEY_MANAGER_RESOURCE_ROLE_IDS);
        this.managerResourceRoleIds = new HashSet<Long>(Arrays.asList(resourceRoleIds));

        this.emailSendingUtility = new EmailSendingUtility(
                Helper.getPropertyValue(config, KEY_EMAIL_SENDER, true), log);
    }

    /**
     * Set external users.
     *            
     * @param idsList
     *            the input ids
     * @return Mapping from user ID to ExternalUser.
     * @throws RetrievalException
     *             if any error occurs while retrieving external users
     */
    private Map<Long, ExternalUser> retrieveUsersInfo(Collection<Long> idsList)
        throws RetrievalException {
        ExternalUser[] externalUsers = userRetrieval.retrieveUsers(Helper.convertLong(idsList));
        
        Map<Long, ExternalUser> users = new HashMap<Long, ExternalUser>();
        for (ExternalUser externalUser : externalUsers) {
            users.put(externalUser.getId(), externalUser);
        }
        return users;
    }
}
