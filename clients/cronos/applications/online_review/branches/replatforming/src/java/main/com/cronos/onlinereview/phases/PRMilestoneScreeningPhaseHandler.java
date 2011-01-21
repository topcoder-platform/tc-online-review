/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.SubmissionType;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.file.fieldconfig.TemplateFields;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * <p>A handle for <code>Milestone Screening</code> phase implementing the additional phase processing logic specific to
 * <code>Online Review</code> application.</p>
 *
 * @author isv
 * @version 1.0 (Milestone Support assembly)
 */
public class PRMilestoneScreeningPhaseHandler extends MilestoneScreeningPhaseHandler {

    /**
     * winners email template source type
     */
    private String emailTemplateSource;

    /**
     * winners email template name
     */
    private String emailTemplateName;

    /**
     * subject for the winners email
     */
    private String emailSubject;

    /**
     * sender address of the winners email
     */
    private String emailFromAddress;

    /**
     * template for project link URL
     */
    private String projectLinkTemplate;

    /**
     * helper class for obtaining several managers
     */
    private ManagerHelper managerHelper;

    /**
     * <p>Constructs new <code>PRMilestoneScreeningPhaseHandler</code> instance. This implementation does nothing.</p>
     *
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    public PRMilestoneScreeningPhaseHandler() throws ConfigurationException {
        obtainEmailConfigProperties(DEFAULT_NAMESPACE);
        this.managerHelper = new ManagerHelper();
    }

    /**
     * <p>Constructs new <code>PRMilestoneScreeningPhaseHandler</code> instance initialized based on parameters from
     * specified configuration namespace.</p>
     *
     * @param namespace a <code>String</code> referencing the namespace for configuration parameters.
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    public PRMilestoneScreeningPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
        obtainEmailConfigProperties(namespace);
        this.managerHelper = new ManagerHelper();
    }

    /**
     * <p>Handles the current phase state transition. Sends emails on screening results to submitters.</p>
     *
     * @param phase    The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not &quot;Milestone Screening&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    @Override
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        if (!toStart) {
            long projectId = phase.getProject().getId();
            try {
                sendEmailToSubmitters(getManagerHelper().getProjectManager().getProject(projectId));
            } catch (Exception e) {
                throw new PhaseHandlingException("Failed to send email to submitters on Milestone Screening results", 
                                                 e);
            }
        }
    }

    /**
     * <p>Parses configuration parameters for email sending functionality.</p>
     *
     * @param namespace a <code>String</code> referencing the namespace for configuration parameters.
     * @throws ConfigurationException if an unexpected error occurs while reading the configuration parameters.
     */
    private void obtainEmailConfigProperties(String namespace) throws ConfigurationException {
        this.emailTemplateSource =
            PhasesHelper.getPropertyValue(namespace, "SubmittersEmail.EmailTemplateSource", true);
        this.emailTemplateName =
            PhasesHelper.getPropertyValue(namespace, "SubmittersEmail.EmailTemplateName", true);
        this.emailSubject = PhasesHelper.getPropertyValue(namespace, "SubmittersEmail.EmailSubject", true);
        this.projectLinkTemplate =
            PhasesHelper.getPropertyValue(namespace, "SubmittersEmail.ProjectLink", true);
        this.emailFromAddress = PhasesHelper.getPropertyValue(namespace, "SubmittersEmail.EmailFromAddress", true);
    }

    /**
     * <p>Sends email to submiters on results of Milestone Screening.</p>
     * 
     * @param project a <code>Project</code> providing details for current project. 
     * @throws Exception if an unexpected error occurs.
     */
    private void sendEmailToSubmitters(Project project) throws Exception {
        Submission[] submissions = getMilestoneSubmissions(project);
        Review[] reviews = getMilestoneScreeningReviews(project);

        // Send emails to each submitter on Milestone screening results
        ResourceManager resourceManager = managerHelper.getResourceManager();
        UserRetrieval userRetrieval = managerHelper.getUserRetrieval();
        for (int i = 0; i < submissions.length; i++) {
            Submission submission = submissions[i];
            long submitterResourceId = submission.getUploads().get(0).getOwner();
            Resource submitterResource = resourceManager.getResource(submitterResourceId);
            long submitterUserId 
                = Long.parseLong(String.valueOf(submitterResource.getProperty("External Reference ID")));
            ExternalUser submitterUser = userRetrieval.retrieveUser(submitterUserId);
            for (int j = 0; j < reviews.length; j++) {
                Review review = reviews[j];
                if (review.getSubmission() == submission.getId()) {
                    sendEmailForUser(project, submitterUser, submission, review);
                    break;
                }
            }
        }
    }

    /**
     * <p>Gets the milestone screening reviews for specified project.</p>
     * 
     * @param project a <code>Project</code> providing details for current project. 
     * @return a <code>Review</code> array listing the Milestone Screening reviews for project. 
     * @throws PersistenceException if an unexpected error occurs.
     * @throws ReviewManagementException if an unexpected error occurs.
     */
    private Review[] getMilestoneScreeningReviews(Project project)
        throws PersistenceException, ReviewManagementException {
        ScorecardManager scorecardManager = managerHelper.getScorecardManager();
        ScorecardType[] scorecardTypes = scorecardManager.getAllScorecardTypes();
        ScorecardType scorecardType = findScorecardTypeByName(scorecardTypes, "Milestone Screening");

        Filter filterProject = new EqualToFilter("project", new Long(project.getId()));
        Filter filterScorecard = new EqualToFilter("scorecardType", new Long(scorecardType.getId()));
        Filter filter = new AndFilter(Arrays.asList(filterProject, filterScorecard));

        ReviewManager reviewManager = managerHelper.getReviewManager();
        Review[] reviews = reviewManager.searchReviews(filter, true);
        return reviews;
    }

    /**
     * <p>Gets the milestone submissions for specified project.</p>
     * 
     * @param project a <code>Project</code> providing details for current project. 
     * @return a <code>Submission</code> array listing the Milestone submissions for project. 
     * @throws UploadPersistenceException if an unexpected error occurs.
     * @throws SearchBuilderException if an unexpected error occurs.
     */
    private Submission[] getMilestoneSubmissions(Project project) throws UploadPersistenceException, 
                                                                         SearchBuilderException {
        UploadManager uploadManager = managerHelper.getUploadManager();

        SubmissionStatus[] allSubmissionStatuses = uploadManager.getAllSubmissionStatuses();
        SubmissionType[] allSubmissionTypes = uploadManager.getAllSubmissionTypes();
        SubmissionType submissionType = findSubmissionTypeByName(allSubmissionTypes, "Milestone Submission");
        SubmissionStatus deletedSubmissionStatus = findSubmissionStatusByName(allSubmissionStatuses, "Deleted");

        Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterType = SubmissionFilterBuilder.createSubmissionTypeIdFilter(submissionType.getId());
        Filter filterStatus 
            = new NotFilter(SubmissionFilterBuilder.createSubmissionStatusIdFilter(deletedSubmissionStatus.getId()));
        Filter filter = new AndFilter(Arrays.asList(filterProject, filterType, filterStatus));

        Submission[] submissions = uploadManager.searchSubmissions(filter);
        return submissions;
    }

    /**
     * <p>Sends email to submitter on results of Milestone Screening for user's submission.</p>
     * 
     * @param project a <code>Project</code> providing details for project. 
     * @param user a <code>ExternalUser</code> referencing the submitter.
     * @param submission a <code>Submission</code> providing details for user's submission.
     * @param review a <code>Review</code> providing details for submission's review.  
     * @throws Exception if an unexpected error occurs.
     */
    private void sendEmailForUser(Project project, ExternalUser user, Submission submission, Review review) 
        throws Exception {
        DocumentGenerator docGenerator = DocumentGenerator.getInstance();
        Template template = getEmailTemplate();
        TemplateFields root = setTemplateFieldValues(docGenerator.getFields(template), project, submission, review);

        String emailContent = docGenerator.applyTemplate(root);
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject(MessageFormat.format(emailSubject, "Online Review", project.getProperty("Project Name")));
        message.setBody(emailContent);
        message.setFromAddress(emailFromAddress);
        message.setToAddress(user.getEmail(), TCSEmailMessage.TO);
        EmailEngine.send(message);
    }

    /**
     * <p>Set template fields with real values.</p>
     * 
     * @param root a <code>TemplateFields</code> providing the root in template fields hierarchy. 
     * @param project a <code>Project</code> providing details for current project. 
     * @param submission a <code>Submission</code> providing details for user's submission.
     * @param review a <code>Review</code> providing details for submission's review.  
     * @return a <code>TemplateFields</code> providing the initialized template fields. 
     * @throws BaseException if an unexpected error occurs.
     */
    private TemplateFields setTemplateFieldValues(TemplateFields root, Project project, Submission submission, 
                                                  Review review) throws BaseException {
        Node[] nodes = root.getNodes();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof Field) {
                Field field = (Field) nodes[i];
                if ("PROJECT_NAME".equals(field.getName())) {
                    field.setValue((String) project.getProperty("Project Name"));
                } else if ("SCREENING_FAILED".equals(field.getName())) {
                    if ("Failed Milestone Screening".equalsIgnoreCase(submission.getSubmissionStatus().getName())) {
                        field.setValue("1");
                    } else {
                        field.setValue("0");
                    }
                } else if ("OR_LINK".equals(field.getName())) {
                    field.setValue(MessageFormat.format(this.projectLinkTemplate, project.getId()));
                } else if ("SUBMISSION_ID".equals(field.getName())) {
                    field.setValue(String.valueOf(submission.getId()));
                } else if ("REASON_FOR_FAILING_SCREENING".equals(field.getName())) {
                    StringBuilder b = new StringBuilder();
                    Comment[] comments = review.getAllItems()[0].getAllComments();
                    for (int j = 0; j < comments.length; j++) {
                        Comment comment = comments[j];
                        b.append(comment.getComment()).append("<br/>");
                    }
                    field.setValue(b.toString());
                }
            } else if (nodes[i] instanceof Condition) {
                Condition condition = ((Condition) nodes[i]);
                if ("SCREENING_FAILED".equals(condition.getName())) {
                    if ("Failed Milestone Screening".equalsIgnoreCase(submission.getSubmissionStatus().getName())) {
                        condition.setValue("1");
                    } else {
                        condition.setValue("0");
                    }
                    NodeList subNodes = condition.getSubNodes();
                    TemplateFields block = new TemplateFields(subNodes.getNodes(), root.getTemplate());
                    setTemplateFieldValues(block, project, submission, review);
                }
            }
        }

        return root;
    }

    /**
     * <p>Gets the email template.</p>
     * 
     * @return a <code>Template</code> providing the email template. 
     * @throws Exception if an unexpected error occurs.
     */
    private Template getEmailTemplate() throws Exception {
        return DocumentGenerator.getInstance().getTemplate(this.emailTemplateSource, this.emailTemplateName);
    }

    /**
     * <p>This static method searches for the submission type with the specified name in a provided array of submission
     * types. The search is case-insensitive.</p>
     *
     * @param submissionTypes an array of submission types to search for wanted submission type among.
     * @param submissionTypeName the name of the needed submission type.
     * @return found submission type, or <code>null</code> if a type with the specified name has not been found in
     *         the provided array of submission types.
     */
    private static SubmissionType findSubmissionTypeByName(SubmissionType[] submissionTypes, String submissionTypeName) {
        for (SubmissionType submissionType :  submissionTypes) {
            if (submissionType.getName().equalsIgnoreCase(submissionTypeName)) {
                return submissionType;
            }
        }
        return null;
    }

    /**
     * <p>This static method searches for the submission status with the specified name in a provided
     * array of submission statuses. The search is case-insensitive.</p>
     *
     * @param submissionStatuses an array of submission statuses to search for wanted submission status among.
     * @param submissionStatusName the name of the needed submission status.
     * @return found submission status, or <code>null</code> if a status with the specified name has not been found in 
     *         the provided array of submission statuses.
     */
    private static SubmissionStatus findSubmissionStatusByName(
            SubmissionStatus[] submissionStatuses, String submissionStatusName) {
        for (int i = 0; i < submissionStatuses.length; ++i) {
            if (submissionStatuses[i].getName().equalsIgnoreCase(submissionStatusName)) {
                return submissionStatuses[i];
            }
        }
        return null;
    }

    /**
     * <p>This static method searches for the scorecard type with the specified name in a provided
     * array of scorecard types.</p>
     *
     * @param scorecardTypes an array of scorecard types to search for wanted scorecard type among.
     * @param typeName the name of the needed scorecard type.
     * @return found scorecard type, or <code>null</code> if a type with the specified name has not been found in the 
     *         provided array of scorecard types.
     */
    private static ScorecardType findScorecardTypeByName(ScorecardType[] scorecardTypes, String typeName) {
        for (int i = 0; i < scorecardTypes.length; ++i) {
            if (scorecardTypes[i].getName().equalsIgnoreCase(typeName)) {
                return scorecardTypes[i];
            }
        }
        return null;
    }
}
