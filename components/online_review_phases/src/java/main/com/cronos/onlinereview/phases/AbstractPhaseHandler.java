/*
 * Copyright (C) 2004-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.reviewfeedback.ReviewFeedback;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManager;
import com.topcoder.message.email.AddressException;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.SendingException;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.TemplateDataFormatException;
import com.topcoder.util.file.TemplateFormatException;
import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * This abstract class is used as a base class for all phase handlers. This class contains logic in the constructor to
 * load configuration settings for a phase handler. Settings includes database connection, email template and email
 * related information.
 * </p>
 *
 * <p>
 * Sample configuration file (for CheckpointSubmissionPhaseHandler) is given below:
 * <pre>
 *     &lt;Config name="com.cronos.onlinereview.phases.AbstractPhaseHandler"&gt;    
 *         &lt;Property name="DefaultStartPhaseEmail"&gt;
 *             &lt;Property name="EmailSubject"&gt;
 *                 &lt;Value&gt;%PHASE_TYPE% Start\\: %PROJECT_NAME%&lt;/Value&gt;
 *             &lt;/Property&gt;
 *             &lt;Property name="EmailFromAddress"&gt;
 *                 &lt;Value&gt;&notificationEmailFromAddress;&lt;/Value&gt;
 *             &lt;/Property&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name="DefaultEndPhaseEmail"&gt;
 *             &lt;Property name="EmailSubject"&gt;
 *                 &lt;Value&gt;%PHASE_TYPE% End\\: %PROJECT_NAME%&lt;/Value&gt;
 *             &lt;/Property&gt;
 *             &lt;Property name="EmailFromAddress"&gt;
 *                 &lt;Value&gt;&notificationEmailFromAddress;&lt;/Value&gt;
 *             &lt;/Property&gt;
 *         &lt;/Property&gt;
 *     &lt;/Config&gt;    
 *     &lt;Config name="com.cronos.onlinereview.phases.CheckpointSubmissionPhaseHandler"&gt;
 *         &lt;Property name="ManagerHelperNamespace"&gt;
 *             &lt;Value&gt;com.cronos.onlinereview.phases.ManagerHelper&lt;/Value&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name="Schemes"&gt;
 *             &lt;Property name="DefaultScheme"&gt;
 *                 &lt;Property name="Roles"&gt;
 *                     &lt;Value&gt;*&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name="Priority"&gt;
 *                     &lt;Value&gt;0&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name="StartPhaseEmail"&gt;
 *                     &lt;Property name="EmailTemplateName"&gt;
 *                         &lt;Value&gt;&phasesEmailTemplate;&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name="EndPhaseEmail"&gt;
 *                     &lt;Property name="EmailTemplateName"&gt;
 *                         &lt;Value&gt;&phasesEmailTemplate;&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *             &lt;/Property&gt;
 *             &lt;Property name="ExtendedSWScheme"&gt;
 *                 &lt;Property name="Roles"&gt;
 *                     &lt;Value&gt;Manager&lt;/Value&gt;
 *                     &lt;Value&gt;Observer&lt;/Value&gt;
 *                     &lt;Value&gt;Copilot&lt;/Value&gt;
 *                     &lt;Value&gt;Client Manager&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name="ProjectTypes"&gt;
 *                     &lt;Value&gt;1&lt;/Value&gt;
 *                     &lt;Value&gt;2&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name="Priority"&gt;
 *                     &lt;Value&gt;2&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name="StartPhaseEmail"&gt;
 *                     &lt;Property name="EmailTemplateName"&gt;
 *                         &lt;Value&gt;&managerNotificationEmailTemplatesBase;/checkpoint_submission/start.txt&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name="EndPhaseEmail"&gt;
 *                     &lt;Property name="EmailTemplateName"&gt;
 *                         &lt;Value&gt;&managerNotificationEmailTemplatesBase;/checkpoint_submission/end.txt&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *             &lt;/Property&gt;
 *         &lt;/Property&gt;
 *     &lt;/Config&gt;
 * </pre>
 * </p>
 *
 * <p>
 * Sample email template is given below:
 * <pre>
 * %PHASE_TIMESTAMP{Phase timestamp}%&lt;br/&gt;
 * Handle\: %USER_HANDLE{User handle}%&lt;br/&gt;
 * Contest\: &lt;a href\="%OR_LINK%"&gt;%PROJECT_NAME{Project name}%&lt;/a&gt;&lt;br/&gt;
 * Version\: %PROJECT_VERSION{Project version}%&lt;br/&gt;
 * This is the notification about %PHASE_OPERATION{The phase operation - start/end}% of
 *  the %PHASE_TYPE{Phase type}% phase.&lt;br/&gt;
 * </pre>
 * </p>
 *
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 *
 * <p>
 * Version 1.1 (Appeals Early Completion Release Assembly 1.0) Change notes:
 * <ol>
 * <li>
 * Changed timeline notification emails subject.
 * </li>
 * <li>
 * Added new fields to timeline notification emails.
 * </li>
 * <li>
 * Property projectDetailsBaseURL added.
 * </li>
 * </ol>
 * </p>
 *
 * <p>
 * Version 1.2 changes note:
 * <ul>
 * <li>
 * Added capability to support different email template for different role (e.g. Submitter, Reviewer, Manager, etc).
 * </li>
 * <li>
 * Supporting Document Generator 2.1, which has the Loop and Condition.
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * Version 1.6 changes note:
 * <ul>
 * <li>
 * Added link to studio contest for email template.
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>
 * Email templates now depend on the project category type.
 * </li>
 * </ul>
 * </p>
 * 
 * <p>
 * Version 1.6.2 changes note:
 * <ul>
 * <li>
 * Added copilot posting specific notifications.
 * </li>
 * </ul>
 * </p>
 * @author tuenm, bose_java, pulky, argolite, waits, FireIce, microsky, lmmortal, VolodymyrK
 * @version 1.8.5
 */
public abstract class AbstractPhaseHandler implements PhaseHandler {

    /**
     * Represents the default namespace of this class. It is used in the constructor
     * to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.AbstractPhaseHandler";

    /** constant for "Project Name" project info. */
    private static final String PROJECT_NAME = "Project Name";

    /** constant for "Project Version" project info. */
    private static final String PROJECT_VERSION = "Project Version";

    /** constant for "Review Feedback Flag" project info. */
    private static final String REVIEW_FEEDBACK_FLAG = "Review Feedback Flag";

    /** constant for lookup value for notification type id. */
    private static final String NOTIFICATION_TYPE_TIMELINE_NOTIFICATION = "Timeline Notification";

    /** Property name constant for manager helper namespace. */
    private static final String PROP_MANAGER_HELPER_NAMESPACE = "ManagerHelperNamespace";

    /** Format for property name constant for "DefaultStartPhaseEmail" */
    private static final String PROP_DEFAULT_START_PHASE_EMAIL = "DefaultStartPhaseEmail";

    /** Format for property name constant for "DefaultEndPhaseEmail" */
    private static final String PROP_DEFAULT_END_PHASE_EMAIL = "DefaultEndPhaseEmail";

    /** Format for property name constant for "StartPhaseEmail" */
    private static final String PROP_START_PHASE_EMAIL = "StartPhaseEmail";

    /** Format for property name constant for "EndPhaseEmail" */
    private static final String PROP_END_PHASE_EMAIL = "EndPhaseEmail";

    /** Format for property name constant for "EmailTemplateName" */
    private static final String PROP_EMAIL_TEMPLATE_NAME = "EmailTemplateName";

    /** Format for property name constant for "EmailSubject" */
    private static final String PROP_EMAIL_SUBJECT = "EmailSubject";

    /** Format for property name constant for "EmailFromAddress" */
    private static final String PROP_EMAIL_FROM_ADDRESS = "EmailFromAddress";

    /** Format for property name constant for "SendEmail" */
    private static final String PROP_SEND_EMAIL = "SendEmail";

    /** Format for property name constant for "Schemes" */
    private static final String PROP_SCHEMES = "Schemes";

    /** Format for property name constant for "Roles" */
    private static final String PROP_ROLES = "Roles";

    /** Format for property name constant for "ProjectTypes" */
    private static final String PROP_PROJECT_TYPES = "ProjectTypes";

    /** Format for property name constant for "ProjectCategories" */
    private static final String PROP_PROJECT_CATEGORIES = "ProjectCategories";

    /** Format for property name constant for "Priority" */
    private static final String PROP_PRIORITY = "Priority";

    /** Format for property name constant for "ReviewFeedbackScheme" */
    private static final String PROP_REVIEW_FEEDBACK_SCHEME = "ReviewFeedbackScheme";

    /** format for the email timestamp. Will format as "Fri, Jul 28, 2006 01:34 PM EST". */
    private static final String EMAIL_TIMESTAMP_FORMAT = "EEE, MMM d, yyyy hh:mm a z";

    /**
     * <p>
     * This field is used to retrieve manager instances to use in phase handlers. It is initialized in the constructor
     * and never change after that. It is never null.
     * </p>
     */
    private final ManagerHelper managerHelper;

    /**
     * <p>
     * The list of the configured email schemes.
     * </p>
     *
     * @since 1.6.1
     */
    private final List<EmailScheme> emailSchemes;

    /**
     * <p>
     * Email scheme for the review feedback emails.
     * </p>
     *
     * @since 1.7.6
     */
    private EmailScheme reviewFeedbackEmailScheme;

    /**
     * <p>
     * Contains default values for start email scheme.
     * </p>
     *
     * @since 1.8.1
     */
    private EmailOptions defaultStartEmailOption;

    /**
     * <p>
     * Contains default values for end email scheme.
     * </p>
     *
     * @since 1.8.1
     */
    private EmailOptions defaultEndEmailOption;

    /**
     * The log instance used by this handler.
     */
    private static final Log log = LogManager.getLog(AbstractPhaseHandler.class.getName());

    /**
     * <p>
     * Creates a new instance of AbstractPhaseHandler using the given namespace for loading configuration settings.
     * </p>
     *
     * <p>
     * It initializes the DB connection factory, connection name, Manager Helper, start and end phase email variables
     * from configuration properties
     * </p>
     *
     * <p>
     * Update in version 1.2: Now each role can have its own email options. And there will be some default setting
     * for 'start'/'end' phases.
     * </p>
     *
     * @param namespace the namespace to load configuration settings from.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     *         missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    protected AbstractPhaseHandler(String namespace) throws ConfigurationException {
        PhasesHelper.checkString(namespace, "namespace");

        // initialize ManagerHelper from given namespace if provided.
        String managerHelperNamespace = PhasesHelper.getPropertyValue(namespace, PROP_MANAGER_HELPER_NAMESPACE, false);

        if (PhasesHelper.isStringNullOrEmpty(managerHelperNamespace)) {
            this.managerHelper = new ManagerHelper();
        } else {
            this.managerHelper = new ManagerHelper(managerHelperNamespace);
        }

        loadDefaultEmailOptions();

        // load the 'Schemes' property
        emailSchemes = getEmailSchemes(namespace);

        // load the Scheme for review feedback emails
        reviewFeedbackEmailScheme = getReviewFeedbackEmailScheme(namespace);
    }

    /**
     * <p>
     * This method is used by the subclass to get the ManagerHelper instance.
     * </p>
     *
     * @return the ManagerHelper instance.
     */
    protected ManagerHelper getManagerHelper() {
        return managerHelper;
    }

    /**
     * <p>
     * Send email to the external users that are registered to be notified on the phase change.
     * </p>
     *
     * <p>
     * Now each role can have its own email options. If not set for that role, using the default setting.
     * </p>
     *
     * @param phase The current phase. must not be null.
     * @param values The values map. This is a map from String into Object. The key is the template field name and the
     *        value is the template field value.
     *
     * @throws IllegalArgumentException if any argument is null or map contains empty/null key/value.
     * @throws PhaseHandlingException if there was a problem when sending email.
     * @since 1.2
     */
    public void sendPhaseEmails(Phase phase, Map<String, Object> values) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkValuesMap(values);

        // Do not send any e-mails for phases whose duration is zero
        if (phase.getLength() <= 0) {
            return;
        }

        final Date scheduledStartDate = phase.getScheduledStartDate();
        final Date scheduledEndDate = phase.getScheduledEndDate();

        // Check phase's scheduled start & end dates to be sure
        if ((scheduledStartDate != null) && (scheduledEndDate != null)
                && !scheduledStartDate.before(scheduledEndDate)) {
            return;
        }

        // Determine whether phase is starting or ending...
        PhaseStatus status = phase.getPhaseStatus();

        if (PhasesHelper.isPhaseToStart(status)) {
            sendPhaseEmails(phase, values, true);
        } else if (PhasesHelper.isPhaseToEnd(status)) {
            sendPhaseEmails(phase, values, false);
        }
    }

    /**
     * <p>
     * Sends email at the start/end of a phase. If the phase is in "Scheduled" state, start phase email will be sent.
     * If the phase is in "Open" state, end phase email will be sent. If the phase is in any other state, this method
     * does nothing. Besides, this method will not send any email if it is not configured to do so.
     * </p>
     *
     * <p>
     * This method first retrieves all the ExternalUsers to whom the notification is to be sent as well as the project
     * information. It then instantiates document generator to create the email body content and sends out an email to
     * each user using the Email Engine component.
     * </p>
     *
     * <p>
     * This method will directly invoking the sendPhaseEmails(Phase, Map) method with empty map.
     * </p>
     *
     * <p>
     * Update in version 1.2: Now each role can have its own email options. If not set for that role, using the
     * default setting.
     * </p>
     *
     * @param phase phase instance.
     *
     * @throws IllegalArgumentException if the input is null or empty string.
     * @throws PhaseHandlingException if there was a problem when sending email.
     * @see AbstractPhaseHandler#sendPhaseEmails(Phase, Map)
     */
    protected void sendPhaseEmails(Phase phase) throws PhaseHandlingException {
        sendPhaseEmails(phase, new HashMap<String, Object>());
    }

    /**
     * Sends email at the start/end of a phase. This method first retrieves all the ExternalUsers to whom the
     * notification is to be sent as well as the project information.
     *
     * <p>
     * Update in version 1.2: Now each role can have its own email options. If not set for that role, using the
     * default setting.
     * </p>
     *
     * @param phase phase instance.
     * @param values the values map to look up the fields in template
     * @param bStart true if phase is to start, false if phase is to end.
     *
     * @throws PhaseHandlingException if there was an error retrieving information or sending email.
     */
    private void sendPhaseEmails(Phase phase, Map<String, Object> values, boolean bStart) throws PhaseHandlingException {
        long projectId = phase.getProject().getId();
        Project project;

        // maps user IDs to EmailScheme
        Map<Long, EmailScheme> userEmailSchemes = new HashMap<Long, EmailScheme>();
        try {
            // Lookup notification type id for "Timeline Notification"
            ResourceManager rm = managerHelper.getResourceManager();
            long notificationTypeId = LookupHelper.getNotificationType(rm, NOTIFICATION_TYPE_TIMELINE_NOTIFICATION).getId();

            Resource[] resources = rm.searchResources(ResourceFilterBuilder.createProjectIdFilter(projectId));

            // retrieve project information
            project = managerHelper.getProjectManager().getProject(projectId);

            // retrieve users to be notified
            long[] externalIds = rm.getNotifications(projectId, notificationTypeId);

            for (Resource resource : resources) {
                long externalId = resource.getUserId();
                if (!contains(externalIds, externalId)) {
                    continue;
                }
                
                EmailScheme emailScheme = getEmailSchemeForResource(resource, project.getProjectCategory());
                if (emailScheme == null) {
                    continue;
                }
                
                // since one user could have more than one role in project, we only need to set out
                // one email for all the roles of the same user,
                // we need to find the email scheme with largest priority here
                EmailScheme oldScheme = userEmailSchemes.get(externalId);
                if (oldScheme == null || oldScheme.getPriority() < emailScheme.getPriority()) {
                    userEmailSchemes.put(externalId, emailScheme);
                }
            }
        } catch (ResourcePersistenceException ex) {
            throw new PhaseHandlingException("There was a problem with resource retrieval", ex);
        } catch (PersistenceException ex) {
            throw new PhaseHandlingException("There was a problem with project retrieval", ex);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("There was a problem with project retrieval", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("There was a problem with project retrieval", e);
        } catch (Exception e) {
            throw new PhaseHandlingException("Problem with retrieving information.", e);
        }

        for (long userID : userEmailSchemes.keySet()) {
            EmailScheme emailScheme = userEmailSchemes.get(userID);
            EmailOptions emailOptions = bStart ? emailScheme.getStartEmailOptions() : emailScheme.getEndEmailOptions();
            if (emailOptions != null && emailOptions.isSend()) {
                try {
                    sendEmail(userID, emailOptions, project, phase, values, bStart);
                } catch(PhaseHandlingException e) {
                    // If any error happens when sending email, log the error and let the logic continue.
                    log.log(Level.ERROR, e, "Error when sending email.");
                }
            }
        }
    }

    /**
     * Sends email notifying the needed resources that they need to leave a feedback about the review performance.
     *
     * @param phase phase instance.
     * @param values the values map to look up the fields in template
     *
     * @throws PhaseHandlingException if there was an error retrieving information or sending email.
     */
    protected void sendReviewFeedbackEmails(Phase phase, Map<String, Object> values)
            throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkValuesMap(values);
        if (reviewFeedbackEmailScheme == null) {
            return;
        }

        // Determine whether phase is starting or ending
        PhaseStatus status = phase.getPhaseStatus();

        boolean bStart;
        if (PhasesHelper.isPhaseToStart(status)) {
            bStart = true;
        } else if (PhasesHelper.isPhaseToEnd(status)) {
            bStart = false;
        } else {
            return;
        }
        
        EmailOptions emailOptions = bStart ? reviewFeedbackEmailScheme.getStartEmailOptions() :
                reviewFeedbackEmailScheme.getEndEmailOptions();
        if (emailOptions == null || !emailOptions.isSend()) {
            return;
        }

        try {
            long projectId = phase.getProject().getId();
            com.topcoder.management.project.Project project = managerHelper.getProjectManager().getProject(projectId);
            
            boolean containsProjectType = reviewFeedbackEmailScheme.getProjectTypes().contains("*") ||
                    reviewFeedbackEmailScheme.getProjectTypes().contains(""+project.getProjectCategory().getProjectType().getId());
            boolean containsProjectCategory = reviewFeedbackEmailScheme.getProjectCategories().contains("*") ||
                    reviewFeedbackEmailScheme.getProjectCategories().contains(""+project.getProjectCategory().getId());
            if (!containsProjectType || !containsProjectCategory) {
                return;
            }

            // Check that this project is eligible for the review feedback.
            String reviewFeedbackFlag = (String) project.getProperty(REVIEW_FEEDBACK_FLAG);
            if (reviewFeedbackFlag == null || !reviewFeedbackFlag.equalsIgnoreCase("true")) {
                return;
            }

            // Check if there already are some review feedback, in which case no need to send the email.
            ReviewFeedbackManager reviewFeedbackManager = managerHelper.getReviewFeedbackManager();
            if (reviewFeedbackManager.getForProject(projectId).size() > 0) {
                return;
            }

            ResourceManager rm = managerHelper.getResourceManager();
            Resource[] resources = rm.searchResources(ResourceFilterBuilder.createProjectIdFilter(projectId));

            // Collect the IDs of the users who need to receive the review feedback email.
            // A user may have more than one role so we need to make sure not to send multiple emails to them.
            Set<Long> userIDs = new HashSet<Long>();
            for (Resource resource : resources) {
                boolean containsRole = reviewFeedbackEmailScheme.getRoles().contains("*") ||
                        reviewFeedbackEmailScheme.getRoles().contains(resource.getResourceRole().getName());

                if (containsRole) {
                    long externalId = resource.getUserId();

                    // Exclude the "dummy" users such as "Components", "Applications" and "LCSUPPORT"
                    if (externalId != Constants.USER_ID_COMPONENTS &&
                        externalId != Constants.USER_ID_APPLICATIONS &&
                        externalId != Constants.USER_ID_LCSUPPORT) {
                        userIDs.add(externalId);
                    }
                }
            }
            for (Long userID : userIDs) {
                try {
                    sendEmail(userID, emailOptions, project, phase, values, bStart);
                } catch(PhaseHandlingException e) {
                    // If any error happens when sending email, log the error and let the logic continue.
                    log.log(Level.ERROR, e, "Error when sending email.");
                }
            }
        } catch (ResourcePersistenceException ex) {
            throw new PhaseHandlingException("There was a problem with resource retrieval", ex);
        } catch (com.topcoder.management.project.PersistenceException ex) {
            throw new PhaseHandlingException("There was a problem with project retrieval", ex);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("There was a problem with project retrieval", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("There was a problem with project retrieval", e);
        } catch (Exception e) {
            throw new PhaseHandlingException("Problem with retrieving information.", e);
        }
    }

    /**
     * Sends email to the specified user using the specified EmailOptions. It instantiates document generator to
     * create the email body content and sends out an email to the user using the Email Engine component.
     *
     * @param userID user ID.
     * @param emailOptions options to be used for sending the email.
     * @param project project instance.
     * @param phase phase instance.
     * @param values the values map to look up the fields in template
     * @param bStart true if phase is to start, false if phase is to end.
     *
     * @throws PhaseHandlingException if there was an error retrieving information or sending email.
     */
    private void sendEmail(long userID, EmailOptions emailOptions, Project project,
                           Phase phase, Map<String, Object> values, boolean bStart) throws PhaseHandlingException {
        try {
            // instantiate document generator instance
            DocumentGenerator docGenerator = new DocumentGenerator();
            docGenerator.setDefaultTemplateSource(new FileTemplateSource());

            Template bodyTemplate = docGenerator.getTemplate(emailOptions.getTemplateName());
            Template subjectTemplate = docGenerator.parseTemplate(emailOptions.getSubject());
            ExternalUser user = managerHelper.getUserRetrieval().retrieveUser(userID);

            // set all field values
            TemplateFields bodyRoot = setTemplateFieldValues(docGenerator.getFields(bodyTemplate), user, project,
                    phase, values, bStart);
            TemplateFields subjectRoot = setTemplateFieldValues(docGenerator.getFields(subjectTemplate), user, project,
                    phase, values, bStart);

            TCSEmailMessage message = new TCSEmailMessage();
            message.setSubject(docGenerator.applyTemplate(subjectRoot));
            message.setBody(docGenerator.applyTemplate(bodyRoot));
            message.setFromAddress(emailOptions.getFromAddress());
            message.setToAddress(user.getEmail(), TCSEmailMessage.TO);
            message.setContentType("text/html");
            EmailEngine.send(message);
        } catch (TemplateSourceException e) {
            throw new PhaseHandlingException("Problem with template source", e);
        } catch (TemplateFormatException e) {
            throw new PhaseHandlingException("Problem with template format", e);
        } catch (RetrievalException e) {
            throw new PhaseHandlingException("Fail to retrieve the user info when sending email.", e);
        } catch (AddressException e) {
            throw new PhaseHandlingException("Problem with email address", e);
        } catch (TemplateDataFormatException e) {
            throw new PhaseHandlingException("Problem with template data format", e);
        } catch (SendingException e) {
            throw new PhaseHandlingException("Problem with sending email", e);
        } catch (ConfigManagerException e) {
            throw new PhaseHandlingException("There was a configuration error", e);
        } catch (Throwable e) {
            throw new PhaseHandlingException("Problem with sending email", e);
        }
    }
    
    /**
    * Finds email scheme for passed resource.
    *
    * @param resource
    *       the resource to get email scheme for
    * @param projectCategory
    *       the project category
    * @return the email scheme for resource. null if not found.
    * @since 1.6.1.
    */
    private EmailScheme getEmailSchemeForResource(Resource resource, ProjectCategory projectCategory) {
        EmailScheme priorityEmailScheme = null;
        for (EmailScheme emailScheme : emailSchemes) {
            boolean containsProjectType = emailScheme.getProjectTypes().contains("*") ||
                emailScheme.getProjectTypes().contains(""+projectCategory.getProjectType().getId());
            boolean containsProjectCategory = emailScheme.getProjectCategories().contains("*") ||
                emailScheme.getProjectCategories().contains(""+projectCategory.getId());
            boolean containsRole = emailScheme.getRoles().contains("*") ||
                emailScheme.getRoles().contains(resource.getResourceRole().getName());
                
            if (containsProjectType && containsProjectCategory && containsRole) {
                if (priorityEmailScheme == null || priorityEmailScheme.getPriority() < emailScheme.getPriority()) {
                    priorityEmailScheme = emailScheme;
                }
            }
        }
        
        return priorityEmailScheme;
    }
    
    /**
     * This method sets the values of the template fields with user, project information and lookup values
     * based on bStart variable which is true if phase is to start, false if phase is to end.
     *
     * @param root template fields.
     * @param user the user to be notified.
     * @param project project instance.
     * @param phase phase instance.
     * @param values the values map to look up
     * @param bStart true if phase is to start, false if phase is to end.
     *
     * @return template fields with data set.
     * @throws PhaseHandlingException if the values element for the loop is invalid
     */
    private TemplateFields setTemplateFieldValues(TemplateFields root, ExternalUser user, Project project, Phase phase,
        Map<String, Object> values, boolean bStart) throws PhaseHandlingException {
        setNodes(root.getNodes(), user, project, phase, values, bStart);

        return root;
    }
    /**
     * This method sets the values of the nodes with user, project information and lookup values
     * based on bStart variable which is true if phase is to start, false if phase is to end.
     *
     * @param nodes the nodes in template
     * @param user the user to be notified.
     * @param project project instance.
     * @param phase phase instance.
     * @param values the values map to look up
     * @param bStart true if phase is to start, false if phase is to end.
     *
     * @throws PhaseHandlingException if the values element for the loop is invalid
     */
    private void setNodes(Node[] nodes, ExternalUser user, Project project,
        Phase phase, Map<String, Object> values, boolean bStart) throws PhaseHandlingException {
        for (Node node : nodes) {
            if (node instanceof Field) {
                setField((Field) node, user, project, phase, values, bStart);
            } else if (node instanceof Loop) {
                setLoopItems((Loop) node, user, project, phase, values, bStart);
            } else if (node instanceof Condition) {
                Condition condition = ((Condition) node);

                if (values.containsKey(condition.getName())) {
                    condition.setValue(values.get(condition.getName()).toString());
                }

                setNodes(condition.getSubNodes().getNodes(), user, project, phase, values, bStart);
            }
        }
    }
    /**
     * This method sets the values of the Loop with user, project information and lookup values
     * based on bStart variable which is true if phase is to start, false if phase is to end.
     *
     * @param loop the Loop in template
     * @param user the user to be notified.
     * @param project project instance.
     * @param phase phase instance.
     * @param values the values map to look up
     * @param bStart true if phase is to start, false if phase is to end.
     * @throws PhaseHandlingException if the values element for the loop is invalid
     */
    @SuppressWarnings("unchecked")
    private void setLoopItems(Loop loop, ExternalUser user, Project project,
        Phase phase, Map<String, Object> values, boolean bStart) throws PhaseHandlingException {
        try {
            List<?> loopItems = (List<?>) values.get(loop.getLoopElement());
            if (loopItems == null) {
                throw new PhaseHandlingException("For loop :" + loop.getLoopElement()
                                                  + ", the value in look up maps should not be null.");
            }
            for (int t = 0; t < loopItems.size(); t++) {
                NodeList item = loop.insertLoopItem(t);
                setNodes(item.getNodes(), user, project, phase, (Map<String, Object>) loopItems.get(t), bStart);
            }
        } catch (ClassCastException cce) {
            throw new PhaseHandlingException("For loop :" + loop.getLoopElement()
                    + ", the value in look up maps should be of List<Map<String, Object>> type.");
        }
    }

    /**
     * This method sets the values of the Field with user, project information and lookup values
     * based on bStart variable which is true if phase is to start, false if phase is to end.
     *
     * <p>
     * Changes in version 1.6: a new field called STUDIO_LINK is added, which can be referenced in email template.
     * </p>
     *
     * @param field the Field in template
     * @param user the user to be notified.
     * @param project project instance.
     * @param phase phase instance.
     * @param values the values map to look up
     * @param bStart true if phase is to start, false if phase is to end.
     */
    private void setField(Field field, ExternalUser user, Project project,
        Phase phase, Map<String, Object> values, boolean bStart) {
        if ("PHASE_TIMESTAMP".equals(field.getName())) {
            field.setValue(formatDate(new Date()));
        } else if ("USER_FIRST_NAME".equals(field.getName())) {
            field.setValue(user.getFirstName());
        } else if ("USER_LAST_NAME".equals(field.getName())) {
            field.setValue(user.getLastName());
        } else if ("USER_HANDLE".equals(field.getName())) {
            field.setValue(user.getHandle());
        } else if ("PROJECT_NAME".equals(field.getName())) {
            field.setValue((String) project.getProperty(PROJECT_NAME));
        } else if ("PROJECT_VERSION".equals(field.getName())) {
            field.setValue((String) project.getProperty(PROJECT_VERSION));
        } else if ("PROJECT_CATEGORY".equals(field.getName())) {
            field.setValue(project.getProjectCategory().getName());
        } else if ("PHASE_OPERATION".equals(field.getName())) {
            field.setValue(bStart ? "start" : "end");
        } else if ("PHASE_TYPE".equals(field.getName())) {
            field.setValue(getPhaseName(phase));
        } else if ("OR_LINK".equals(field.getName())) {
            field.setValue(managerHelper.getProjectDetailsBaseURL() + project.getId());
        } else if ("CHALLENGE_LINK".equals(field.getName())) {
            field.setValue(managerHelper.getChallengePageBaseURL() + project.getId());
        } else if ("PEER_REVIEW".equals(field.getName())) {
            Object reviewType = project.getProperty("Review Type");
            field.setValue(String.valueOf(reviewType != null && Constants.REVIEW_TYPE_PEER.equals(reviewType.toString())? 1 : 0));
        } else if ("STUDIO_LINK".equals(field.getName())) {
            field.setValue(managerHelper.getStudioProjectDetailsBaseURL() + project.getId());
        } else if ("STUDIO_CONTEST_FINAL_FIX_LINK".equals(field.getName())) {
            field.setValue(managerHelper.getStudioProjectFinalFixBaseURL() + project.getId());
        } else if ("DIRECT_CONTEST_FINAL_FIX_LINK".equals(field.getName())) {
            field.setValue(managerHelper.getDirectContestFinalFixBaseURL() + project.getId());
        } else if ("DIRECT_CONTEST_LINK".equals(field.getName())) {
            if (project.getProjectCategory().getName().equals(Constants.PROJECT_CATEGORY_COPILOT_POSTING)) {
                field.setValue(managerHelper.getCopilotDirectContestBaseURL() + project.getId());
            } else {
                field.setValue(managerHelper.getDirectContestBaseURL() + project.getId());
            }
        } else if ("DIRECT_PROJECT_LINK".equals(field.getName())) {
            field.setValue(managerHelper.getDirectProjectBaseURL() + project.getTcDirectProjectId());
        } else if ("OR_PROJECT_ID".equals(field.getName())) {
            field.setValue(""+ project.getId());
        } else if ("TC_DIRECT_PROJECT_NAME".equals(field.getName())) {
            field.setValue(""+project.getTcDirectProjectName());
        } else if ("TC_DIRECT_PROJECT_ID".equals(field.getName())) {
            field.setValue(""+project.getTcDirectProjectId());
        } else if ("IS_WINNER".equals(field.getName())) {
            long userId = user.getId();
            String projectWinnerId = (String) project.getProperty("Winner External Reference ID");
            field.setValue(projectWinnerId.equals(String.valueOf(userId)) ? "1" : "0");
        } else if (values.containsKey(field.getName())) {
            if (values.get(field.getName()) != null) {
                field.setValue(values.get(field.getName()).toString());
            }
        }
    }

    /**
     * This method returns phase name, which consists of the phase type name plus phase index if there are multiple
     * phases of the same type.
     *
     * @param phase phase instance.
     * @return Phase name.
     */
    private String getPhaseName(Phase phase) {
        String phaseType = phase.getPhaseType().getName();
        int index = 1;
        
        Phase allPhases[] = phase.getProject().getAllPhases();
        for (Phase otherPhase : allPhases) {
            if (otherPhase.getPhaseType().getName().equals(phaseType) &&
                otherPhase.getScheduledStartDate().before(phase.getScheduledStartDate())) {
                index++;
            }
        }

        return phaseType + (index>1 ? " #"+index : "");
    }
    
    /**
     * <p>
     * Creates the Empty EmailOptions with Not Send email.
     * </p>
     * @return EmailOptions instance
     */
    private static EmailOptions createNotSendEmailOptions() {
        EmailOptions options = new EmailOptions();
        options.setSend(Boolean.FALSE);

        return options;
    }

    /**
     * <p>
     * Constructs the EmailOptions from the given config Property.
     * </p>
     *
     * @param phaseEmailProperty the config property
     * @param defaultPhaseEmailProperty EmailOptions with default values
     * @return EmailOptions instance with the setting from property
     * @throws ConfigurationException if any error occurs during retrieving
     */
    private static EmailOptions createEmailOptions(Property phaseEmailProperty, EmailOptions defaultPhaseEmailProperty)
        throws ConfigurationException {
        EmailOptions options = new EmailOptions();

        String fromAddress = phaseEmailProperty.getValue(PROP_EMAIL_FROM_ADDRESS);
        if (fromAddress == null && defaultPhaseEmailProperty != null) {
            fromAddress = defaultPhaseEmailProperty.getFromAddress();
        }
        options.setFromAddress(fromAddress);

        String templateName = phaseEmailProperty.getValue(PROP_EMAIL_TEMPLATE_NAME);
        if (templateName == null && defaultPhaseEmailProperty != null) {
            templateName = defaultPhaseEmailProperty.getTemplateName();
        }
        options.setTemplateName(templateName);

        String emailSubject = phaseEmailProperty.getValue(PROP_EMAIL_SUBJECT);
        if (emailSubject == null && defaultPhaseEmailProperty != null) {
            emailSubject = defaultPhaseEmailProperty.getSubject();
        }
        options.setSubject(emailSubject);

        //'SendEmail' is optional, the value could be 'Yes' or 'No', or 'True' or 'False', case-insensitive
        options.setSend(parseSendEmailPropValue(phaseEmailProperty.getValue(PROP_SEND_EMAIL)));

        return options;
    }

    /**
     * <p>
     * Parses the value of property 'SendEmail' to Boolean type from String type. The allowed string value could be
     * 'yes'/'no' or 'true'/'false' in any case.
     * </p>
     * @param value the value to parse, can be null, then true will be returned
     * @return true if it is 'yes' or 'true', false otherwise
     * @throws ConfigurationException if the value is not in allowed list
     */
    private static Boolean parseSendEmailPropValue(String value) throws ConfigurationException {
        if (value == null) {
            return Boolean.TRUE;
        }

        if (value.trim().equalsIgnoreCase("yes") || value.trim().equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }

        if (value.trim().equalsIgnoreCase("no") || value.trim().equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }

        throw new ConfigurationException(
            "The value for 'SendEmail' should be 'true'/'false' or 'yes'/'no', case-insensitive");
    }

    /**
     * <p>
     * Loads default email options for start and end phase email.
     * </p>
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     *         missing.
     */
    private void loadDefaultEmailOptions() throws ConfigurationException {
        try {
            Property phaseStartProperty =
                    ConfigManager.getInstance().getPropertyObject(DEFAULT_NAMESPACE, PROP_DEFAULT_START_PHASE_EMAIL);
            defaultStartEmailOption =  phaseStartProperty != null ? createEmailOptions(phaseStartProperty, null) : null;
            Property phaseEndProperty =
                    ConfigManager.getInstance().getPropertyObject(DEFAULT_NAMESPACE, PROP_DEFAULT_END_PHASE_EMAIL);
            defaultEndEmailOption =  phaseEndProperty != null ? createEmailOptions(phaseEndProperty, null) : null;
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException("The namespace '" + DEFAULT_NAMESPACE + "' does not exist.", e);
        }
    }

    /**
     * <p>
     * Gets the 'Schemes' property values from the given namespace. If there is no such property, empty list will
     * be returned.
     * </p>
     * @param namespace the namespace to retrieve the values from
     * @return List of EmailScheme objects. Not null, can be empty.
     * @throws ConfigurationException if the namespace does not exist
     */
    private List<EmailScheme> getEmailSchemes(String namespace) throws ConfigurationException {
        try {
            com.topcoder.util.config.Property schemesProperty =
                ConfigManager.getInstance().getPropertyObject(namespace, PROP_SCHEMES);
            if (schemesProperty == null) {
                return new ArrayList<EmailScheme>();
            }

            java.util.Enumeration<?> schemeNames = schemesProperty.propertyNames();
            List<EmailScheme> schemes = new ArrayList<EmailScheme>();
            while (schemeNames.hasMoreElements()) {
                String schemeName = (String) schemeNames.nextElement();
                if (schemeName != null) {
                    EmailScheme emailScheme = getEmailScheme(schemesProperty.getProperty(schemeName));
                    if (emailScheme != null) {
                        schemes.add(emailScheme);                                                              
                    }
                }
            }

            return schemes;
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException("The namespace '" + namespace + "' does not exist.", e);
        }
    }

    /**
     * <p>
     * Gets the EmailScheme for the review feedback emails. If there is no such scheme, null will be returned.
     * </p>
     * @param namespace the namespace to retrieve the values from
     * @return EmailScheme object. Can be null.
     * @throws ConfigurationException if the namespace does not exist
     */
    private EmailScheme getReviewFeedbackEmailScheme(String namespace) throws ConfigurationException {
        try {
            com.topcoder.util.config.Property schemeProperty =
                ConfigManager.getInstance().getPropertyObject(namespace, PROP_REVIEW_FEEDBACK_SCHEME);
            if (schemeProperty != null) {
                return getEmailScheme(schemeProperty);
            } else {
                return null;
            }
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException("The namespace '" + namespace + "' does not exist.", e);
        }
    }

    /**
     * <p>
     * Constructs and returns EmailScheme from the given config Property.
     * </p>
     * @param schemeProperty config property to read the scheme definition from
     * @return The EmailScheme object. Can be null.
     * @throws ConfigurationException if the namespace does not exist
     */
    private EmailScheme getEmailScheme(Property schemeProperty) throws ConfigurationException {
        EmailScheme emailScheme = new EmailScheme();

        String[] rolesArray = schemeProperty.getProperty(PROP_ROLES).getValues();
        if (rolesArray != null && rolesArray.length > 0) {
            emailScheme.setRoles(java.util.Arrays.asList(rolesArray));
        }

        Property projectTypesProp = schemeProperty.getProperty(PROP_PROJECT_TYPES);
        if (projectTypesProp != null && projectTypesProp.getValues().length > 0) {
            emailScheme.setProjectTypes(java.util.Arrays.asList(projectTypesProp.getValues()));
        }

        Property projectCategoriesProp = schemeProperty.getProperty(PROP_PROJECT_CATEGORIES);
        if (projectCategoriesProp != null && projectCategoriesProp.getValues().length > 0) {
            emailScheme.setProjectCategories(java.util.Arrays.asList(projectCategoriesProp.getValues()));
        }

        Property priorityProperty = schemeProperty.getProperty(PROP_PRIORITY);
        if (priorityProperty != null && priorityProperty.getValue() != null) {
            try {
                emailScheme.setPriority(Integer.parseInt(priorityProperty.getValue()));
            } catch (NumberFormatException nfe) {
                throw new ConfigurationException("Can't parse priority value : " + priorityProperty.getValue(), nfe);
            }
        }

        //if the configuration does not exist, create not send email options
        Property phaseStartProperty = schemeProperty.getProperty(PROP_START_PHASE_EMAIL);
        EmailOptions startEmailOption =  phaseStartProperty != null ?
                createEmailOptions(phaseStartProperty, defaultStartEmailOption) : createNotSendEmailOptions();
        emailScheme.setStartEmailOptions(startEmailOption);

        //if the configuration does not exist, create not send email options
        Property phaseEndProperty = schemeProperty.getProperty(PROP_END_PHASE_EMAIL);
        EmailOptions endEmailOption = phaseEndProperty != null ?
                createEmailOptions(phaseEndProperty, defaultEndEmailOption) : createNotSendEmailOptions();
        emailScheme.setEndEmailOptions(endEmailOption);

        return emailScheme;
    }

    /**
     * Returns the date formatted as "Fri, Jul 28, 2006 01:34 PM EST" for example.
     *
     * @param dt date to be formatted.
     *
     * @return formatted date string.
     */
    private static String formatDate(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat(EMAIL_TIMESTAMP_FORMAT);
        return formatter.format(dt);
    }

    /**
     * Checks if the id exists in the id array.
     *
     * @param ids the id array
     * @param id the id to check
     *
     * @return true if exists
     */
    private static boolean contains(long[] ids, long id) {
        for (long id1 : ids) {
            if (id1 == id) {
                return true;
            }
        }

        return false;
    }
}
