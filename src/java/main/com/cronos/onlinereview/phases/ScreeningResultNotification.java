/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.SubmissionType;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
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
import com.topcoder.util.file.templatesource.FileTemplateSource;

/**
 * This class is used to send email notification for checkpoint screening result or screening result.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ScreeningResultNotification {

    /** format for the email timestamp. Will format as "Fri, Jul 28, 2006 01:34 PM EST". */
    private static final String EMAIL_TIMESTAMP_FORMAT = "EEE, MMM d, yyyy hh:mm a z";
    
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
     * template for scorecard link URL
     */
    private String scorecardLinkTemplate;

    /**
     * helper class for obtaining several managers
     */
    private ManagerHelper managerHelper;

    /**
     * Represents the submission type name associated with the phase.
     */
    private String submissionTypeName;

    /**
     * Represents the scorecard type name associated with the phase. 
     */
    private String scorecardTypeName;

    /**
     * Represents the submission status name which failed screening. 
     */
    private String failedScreeningStatusName;

    /**
     * Constructor. It reads the configurations from the namespace.
     *
     * @param namespace the configuration namespace.
     * @param submissionTypeName the submission type name
     * @param scorecardTypeName the scorecard type name
     * @param failedScreeningStatusName the submission status which failed screening.
     * @throws ConfigurationException if any error occurs when reading configurations
     */
    public ScreeningResultNotification(String namespace, String submissionTypeName, String scorecardTypeName,
            String failedScreeningStatusName) throws ConfigurationException {
        PhasesHelper.checkString(namespace, "namespace");
        PhasesHelper.checkString(submissionTypeName, "submissionTypeName");
        PhasesHelper.checkString(scorecardTypeName, "scorecardTypeName");
        PhasesHelper.checkString(failedScreeningStatusName, "failedScreeningStatusName");

        this.emailTemplateName = PhasesHelper.getPropertyValue(namespace, "SubmittersEmail.EmailTemplateName", true);
        this.emailSubject = PhasesHelper.getPropertyValue(namespace, "SubmittersEmail.EmailSubject", true);
        this.projectLinkTemplate = PhasesHelper.getPropertyValue(namespace, "SubmittersEmail.ProjectLink", true);
        this.scorecardLinkTemplate = PhasesHelper.getPropertyValue(namespace, "SubmittersEmail.ScorecardLink", true);
        this.emailFromAddress = PhasesHelper.getPropertyValue(namespace, "SubmittersEmail.EmailFromAddress", true);
        this.submissionTypeName = submissionTypeName;
        this.scorecardTypeName = scorecardTypeName;
        this.failedScreeningStatusName = failedScreeningStatusName;

        this.managerHelper = new ManagerHelper();
    }

    /**
     * <p>
     * Sends email to submitters on results of Screening.
     * </p>
     * 
     * @param project
     *            a <code>Project</code> providing details for current project.
     * @throws Exception
     *             if an unexpected error occurs.
     */
    public void sendEmailToSubmitters(Project project) throws Exception {
        Submission[] submissions = getSubmissions(project);
        Review[] reviews = getScreeningReviews(project);

        // Send emails to each submitter on screening results
        ResourceManager resourceManager = managerHelper.getResourceManager();
        UserRetrieval userRetrieval = managerHelper.getUserRetrieval();
        for (Submission submission : submissions) {
            long submitterResourceId = submission.getUpload().getOwner();
            Resource submitterResource = resourceManager.getResource(submitterResourceId);
            long submitterUserId = Long
                    .parseLong(String.valueOf(submitterResource.getProperty("External Reference ID")));
            ExternalUser submitterUser = userRetrieval.retrieveUser(submitterUserId);
            for (Review review : reviews) {
                if (review.getSubmission() == submission.getId()) {
                    sendEmailForUser(project, submitterUser, submission, review);
                    break;
                }
            }
        }
    }

    /**
     * <p>
     * Sends email to submitter on results of Screening for user's submission.
     * </p>
     * 
     * @param project
     *            a <code>Project</code> providing details for project.
     * @param user
     *            a <code>ExternalUser</code> referencing the submitter.
     * @param submission
     *            a <code>Submission</code> providing details for user's submission.
     * @param review
     *            a <code>Review</code> providing details for submission's review.
     * @throws Exception
     *             if an unexpected error occurs.
     */
    private void sendEmailForUser(Project project, ExternalUser user, Submission submission, Review review)
            throws Exception {
        DocumentGenerator docGenerator = new DocumentGenerator();
        docGenerator.setDefaultTemplateSource(new FileTemplateSource());
        Template template = docGenerator.getTemplate(emailTemplateName);
        TemplateFields root = setTemplateFieldValues(docGenerator.getFields(template), project, submission, review);

        String emailContent = docGenerator.applyTemplate(root);
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject(MessageFormat.format(emailSubject, "Studio", project.getProperty("Project Name")));
        message.setBody(emailContent);
        message.setFromAddress(emailFromAddress);
        message.setToAddress(user.getEmail(), TCSEmailMessage.TO);
        message.setContentType("text/html");
        EmailEngine.send(message);
    }

    /**
     * <p>
     * Set template fields with real values.
     * </p>
     * 
     * @param root
     *            a <code>TemplateFields</code> providing the root in template fields hierarchy.
     * @param project
     *            a <code>Project</code> providing details for current project.
     * @param submission
     *            a <code>Submission</code> providing details for user's submission.
     * @param review
     *            a <code>Review</code> providing details for submission's review.
     * @return a <code>TemplateFields</code> providing the initialized template fields.
     * @throws BaseException
     *             if an unexpected error occurs.
     */
    private TemplateFields setTemplateFieldValues(TemplateFields root, Project project, Submission submission,
            Review review) throws BaseException {
        Node[] nodes = root.getNodes();

        for (Node node : nodes) {
            if (node instanceof Field) {
                Field field = (Field) node;
                if ("PROJECT_NAME".equals(field.getName())) {
                    field.setValue((String) project.getProperty("Project Name"));
                } else if ("SCREENING_FAILED".equals(field.getName())) {
                    if (failedScreeningStatusName.equalsIgnoreCase(submission.getSubmissionStatus().getName())) {
                        field.setValue("1");
                    } else {
                        field.setValue("0");
                    }
                } else if ("STUDIO_LINK".equals(field.getName())) {
                    field.setValue(MessageFormat.format(this.projectLinkTemplate, project.getId()));
                } else if ("SCORECARD_LINK".equals(field.getName())) {
                    field.setValue(MessageFormat.format(this.scorecardLinkTemplate, review.getId()));
                } else if ("SUBMISSION_ID".equals(field.getName())) {
                    field.setValue(String.valueOf(submission.getId()));
                } else if ("SUBMISSION_DATE".equals(field.getName())) {
                    field.setValue(formatDate(submission.getCreationTimestamp()));
                }
            } else if (node instanceof Condition) {
                Condition condition = ((Condition) node);
                if ("SCREENING_FAILED".equals(condition.getName())) {
                    if (failedScreeningStatusName.equalsIgnoreCase(submission.getSubmissionStatus().getName())) {
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
     * <p>
     * This static method searches for the submission type with the specified name in a provided array of submission
     * types. The search is case-insensitive.
     * </p>
     * 
     * @param submissionTypes
     *            an array of submission types to search for wanted submission type among.
     * @param submissionTypeName
     *            the name of the needed submission type.
     * @return found submission type, or <code>null</code> if a type with the specified name has not been found in the
     *         provided array of submission types.
     */
    private static SubmissionType findSubmissionTypeByName(SubmissionType[] submissionTypes, String submissionTypeName) {
        for (SubmissionType submissionType : submissionTypes) {
            if (submissionType.getName().equalsIgnoreCase(submissionTypeName)) {
                return submissionType;
            }
        }
        return null;
    }

    /**
     * <p>
     * This static method searches for the submission status with the specified name in a provided array of submission
     * statuses. The search is case-insensitive.
     * </p>
     * 
     * @param submissionStatuses
     *            an array of submission statuses to search for wanted submission status among.
     * @param submissionStatusName
     *            the name of the needed submission status.
     * @return found submission status, or <code>null</code> if a status with the specified name has not been found in
     *         the provided array of submission statuses.
     */
    private static SubmissionStatus findSubmissionStatusByName(SubmissionStatus[] submissionStatuses,
            String submissionStatusName) {
        for (SubmissionStatus submissionStatus : submissionStatuses) {
            if (submissionStatus.getName().equalsIgnoreCase(submissionStatusName)) {
                return submissionStatus;
            }
        }
        return null;
    }

    /**
     * <p>
     * This static method searches for the scorecard type with the specified name in a provided array of scorecard
     * types.
     * </p>
     * 
     * @param scorecardTypes
     *            an array of scorecard types to search for wanted scorecard type among.
     * @param typeName
     *            the name of the needed scorecard type.
     * @return found scorecard type, or <code>null</code> if a type with the specified name has not been found in the
     *         provided array of scorecard types.
     */
    private static ScorecardType findScorecardTypeByName(ScorecardType[] scorecardTypes, String typeName) {
        for (ScorecardType scorecardType : scorecardTypes) {
            if (scorecardType.getName().equalsIgnoreCase(typeName)) {
                return scorecardType;
            }
        }
        return null;
    }

    /**
     * <p>
     * Gets the screening reviews for specified project with the specified scorecard type.
     * </p>
     * 
     * @param project
     *            a <code>Project</code> providing details for current project. .
     * @return a <code>Review</code> array listing the Screening reviews for project with the specified scorecard type.
     * @throws PersistenceException
     *             if an unexpected error occurs.
     * @throws ReviewManagementException
     *             if an unexpected error occurs.
     */
    private Review[] getScreeningReviews(Project project) throws PersistenceException, ReviewManagementException {
        ScorecardManager scorecardManager = managerHelper.getScorecardManager();
        ScorecardType[] scorecardTypes = scorecardManager.getAllScorecardTypes();
        ScorecardType scorecardType = findScorecardTypeByName(scorecardTypes, scorecardTypeName);

        Filter filterProject = new EqualToFilter("project", project.getId());
        Filter filterScorecard = new EqualToFilter("scorecardType", scorecardType.getId());
        Filter filter = new AndFilter(Arrays.asList(filterProject, filterScorecard));

        ReviewManager reviewManager = managerHelper.getReviewManager();
        return reviewManager.searchReviews(filter, true);
    }

    /**
     * <p>
     * Gets the submissions for specified project with the specified submission type.
     * </p>
     * 
     * @param project
     *            a <code>Project</code> providing details for current project.
     * @return a <code>Submission</code> array listing the submissions for project with the specified submission type.
     * @throws UploadPersistenceException
     *             if an unexpected error occurs.
     * @throws SearchBuilderException
     *             if an unexpected error occurs.
     */
    private Submission[] getSubmissions(Project project) throws UploadPersistenceException, SearchBuilderException {
        UploadManager uploadManager = managerHelper.getUploadManager();

        SubmissionStatus[] allSubmissionStatuses = uploadManager.getAllSubmissionStatuses();
        SubmissionType[] allSubmissionTypes = uploadManager.getAllSubmissionTypes();
        SubmissionType submissionType = findSubmissionTypeByName(allSubmissionTypes, submissionTypeName);
        SubmissionStatus deletedSubmissionStatus = findSubmissionStatusByName(allSubmissionStatuses, "Deleted");

        Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterType = SubmissionFilterBuilder.createSubmissionTypeIdFilter(submissionType.getId());
        Filter filterStatus = new NotFilter(
                SubmissionFilterBuilder.createSubmissionStatusIdFilter(deletedSubmissionStatus.getId()));
        Filter filter = new AndFilter(Arrays.asList(filterProject, filterType, filterStatus));

        return uploadManager.searchSubmissions(filter);
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
}
