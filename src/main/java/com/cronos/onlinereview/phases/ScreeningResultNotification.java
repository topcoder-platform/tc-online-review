/*
 * Copyright (C) 2011 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.onlinereview.component.deliverable.Submission;
import com.topcoder.onlinereview.component.deliverable.SubmissionFilterBuilder;
import com.topcoder.onlinereview.component.deliverable.SubmissionStatus;
import com.topcoder.onlinereview.component.deliverable.SubmissionType;
import com.topcoder.onlinereview.component.deliverable.UploadManager;
import com.topcoder.onlinereview.component.deliverable.UploadPersistenceException;
import com.topcoder.onlinereview.component.document.DocumentGenerator;
import com.topcoder.onlinereview.component.document.Template;
import com.topcoder.onlinereview.component.document.fieldconfig.Condition;
import com.topcoder.onlinereview.component.document.fieldconfig.Field;
import com.topcoder.onlinereview.component.document.fieldconfig.Node;
import com.topcoder.onlinereview.component.document.fieldconfig.NodeList;
import com.topcoder.onlinereview.component.document.fieldconfig.TemplateFields;
import com.topcoder.onlinereview.component.document.templatesource.FileTemplateSource;
import com.topcoder.onlinereview.component.email.EmailEngine;
import com.topcoder.onlinereview.component.email.TCSEmailMessage;
import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.external.ExternalUser;
import com.topcoder.onlinereview.component.external.UserRetrieval;
import com.topcoder.onlinereview.component.project.management.PersistenceException;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.phase.ManagerHelper;
import com.topcoder.onlinereview.component.resource.Resource;
import com.topcoder.onlinereview.component.resource.ResourceManager;
import com.topcoder.onlinereview.component.review.Review;
import com.topcoder.onlinereview.component.review.ReviewManagementException;
import com.topcoder.onlinereview.component.review.ReviewManager;
import com.topcoder.onlinereview.component.scorecard.ScorecardManager;
import com.topcoder.onlinereview.component.scorecard.ScorecardType;
import com.topcoder.onlinereview.component.search.SearchBuilderException;
import com.topcoder.onlinereview.component.search.filter.AndFilter;
import com.topcoder.onlinereview.component.search.filter.EqualToFilter;
import com.topcoder.onlinereview.component.search.filter.Filter;
import com.topcoder.onlinereview.component.search.filter.NotFilter;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
     * Maximum screening score
     */
    private static final Double MAX_SCREENING_SCORE = 100.0;

    /**
     * Email title for pass screening
     */
    private static final String EMAIL_TITLE_PASS = "Topcoder Submission Screening Passed";

    /**
     * Email title if action required
     */
    private static final String EMAIL_TITLE_ACTION_REQUIRED = "ACTION REQUIRED - Topcoder Submission Screening";

    /**
     * Email title for failed screening
     */
    private static final String EMAIL_TITLE_FAILED = "Topcoder Submission Screening Failed";

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
            long submitterUserId = submitterResource.getUserId();
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
        if (failedScreeningStatusName.equalsIgnoreCase(submission.getSubmissionStatus().getName())) {
            message.setSubject(EMAIL_TITLE_FAILED);
        } else {
            if (review.getScore() != null &&
                    review.getScore() < MAX_SCREENING_SCORE) {
                message.setSubject(EMAIL_TITLE_ACTION_REQUIRED);
            } else {
                message.setSubject(EMAIL_TITLE_PASS);
            }
        }
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
                } else if ("SCREENING_SCORE".equals(field.getName())) {
                    field.setValue(String.valueOf(review.getScore()));
                }
            } else if (node instanceof Condition) {
                Condition condition = ((Condition) node);
                if ("SCREENING_STATUS".equals(condition.getName())) {
                    // 1: pass screening
                    // 2: pass with issues
                    // 3: fail
                    if (failedScreeningStatusName.equalsIgnoreCase(submission.getSubmissionStatus().getName())) {
                        condition.setValue("3");
                    } else {
                        if (review.getScore() != null &&
                                review.getScore() < MAX_SCREENING_SCORE) {
                            condition.setValue("2");
                        } else {
                            condition.setValue("1");
                        }
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

    public void setEmailTemplateName(String emailTemplateName) {
        this.emailTemplateName = emailTemplateName;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public void setEmailFromAddress(String emailFromAddress) {
        this.emailFromAddress = emailFromAddress;
    }

    public void setProjectLinkTemplate(String projectLinkTemplate) {
        this.projectLinkTemplate = projectLinkTemplate;
    }

    public void setScorecardLinkTemplate(String scorecardLinkTemplate) {
        this.scorecardLinkTemplate = scorecardLinkTemplate;
    }

    public void setManagerHelper(ManagerHelper managerHelper) {
        this.managerHelper = managerHelper;
    }

    public void setSubmissionTypeName(String submissionTypeName) {
        this.submissionTypeName = submissionTypeName;
    }

    public void setScorecardTypeName(String scorecardTypeName) {
        this.scorecardTypeName = scorecardTypeName;
    }

    public void setFailedScreeningStatusName(String failedScreeningStatusName) {
        this.failedScreeningStatusName = failedScreeningStatusName;
    }
}
