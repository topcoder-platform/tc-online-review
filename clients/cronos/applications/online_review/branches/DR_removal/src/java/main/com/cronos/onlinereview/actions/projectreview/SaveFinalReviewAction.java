/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.cronos.onlinereview.util.LookupHelper;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to save the final review.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SaveFinalReviewAction extends BaseProjectReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -7909873581214281098L;

    /**
     * This method is an implementation of &quot;Save Final Review&quot; Struts Action defined for
     * this assembly, which is supposed to save information posted from /jsp/editFinalReview.jsp
     * page. This method will update (edit) final review.
     *
     * @return &quot;success&quot;, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent review id, or the lack of permissions, etc.).
     * @throws BaseException
     *         if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that user has the permission to perform final review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to save
        Review review = verification.getReview();

        // Check if project has SVN Module property set
        String svnModule = (String) verification.getProject().getProperty("SVN Module");
        request.setAttribute("projectHasSVNModuleSet", (svnModule != null) && (svnModule.trim().length() > 0));

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review") &&
            !scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Approval")) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewCommitted", null);
        }

        // Get an array of all phases for current project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(false), verification.getProject());
        // Get an active phase for the project
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.FINAL_REVIEW_PHASE_NAME);
        // Check that Final Review Phase is really active (open)
        if (phase == null) {
            return ActionsHelper.produceErrorReport(this, request,
                Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.IncorrectPhase", null);
        }

        // This variable determines if 'Save and Mark Complete' button has been clicked
        final boolean commitRequested = "submit".equalsIgnoreCase(request.getParameter("save"));
        // This variable determines if Preview button has been clicked
        final boolean previewRequested = "preview".equalsIgnoreCase(request.getParameter("save"));

        // Retrieve a resource for the Final Review phase
        Resource resource = ActionsHelper.getMyResourceForPhase(request, phase);

        // Get form's fields
        String[] fixStatuses = (String[]) getModel().get("fix_status");
        String[] finalComments = (String[]) getModel().get("final_comment");
        Boolean approveFixesObj = (Boolean) getModel().get("approve_fixes");
        boolean approveFixes = (approveFixesObj != null && approveFixesObj);
        int commentIdx = 0;
        int itemIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx) {
                    // Get the ID of the current scorecard template's question
                    final long questionId = section.getQuestion(questionIdx).getId();

                    // Iterate over the items of existing review that needs updating
                    for (int i = 0; i < review.getNumberOfItems(); ++i) {
                        // Get an item for the current iteration
                        Item item = review.getItem(i);
                        // Skip items that are not for the current scorecard template question
                        // or items that do not have any comments
                        if (item.getQuestion() != questionId || item.getNumberOfComments() == 0) {
                            continue;
                        }

                        Comment finalReviewComment = null;

                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            Comment comment = item.getComment(j);
                            String commentType = comment.getCommentType().getName();

                            if (commentIdx < fixStatuses.length && ActionsHelper.isReviewerComment(comment)) {
                                comment.setExtraInfo(fixStatuses[commentIdx++]);
                            }
                            if (commentType.equalsIgnoreCase("Final Review Comment")) {
                                finalReviewComment = comment;
                            }
                        }

                        if (finalReviewComment == null) {
                            finalReviewComment = new Comment();
                            finalReviewComment.setCommentType(LookupHelper.getCommentType("Final Review Comment"));
                            item.addComment(finalReviewComment);
                        }

                        finalReviewComment.setComment(finalComments[itemIdx++]);
                        finalReviewComment.setAuthor(resource.getId());
                    }
                }
            }
        }

        Comment reviewLevelComment = null;

        for (int i = 0; i < review.getNumberOfComments(); ++i) {
            Comment comment = review.getComment(i);
            if (comment.getCommentType().getName().equalsIgnoreCase("Final Review Comment")) {
                reviewLevelComment = comment;
                break;
            }
        }

        if (reviewLevelComment == null) {
            reviewLevelComment = new Comment();
            reviewLevelComment.setCommentType(LookupHelper.getCommentType("Final Review Comment"));
            review.addComment(reviewLevelComment);
        }

        reviewLevelComment.setAuthor(resource.getId());
        reviewLevelComment.setExtraInfo("Approving");
        reviewLevelComment.setComment("");

        boolean validationSucceeded =
                (!commitRequested) || validateFinalReviewScorecard(request, scorecardTemplate, review);

        request.setAttribute("approvalBased", scorecardTemplate.getScorecardType().getName().equals("Approval"));

        // If the user has requested to complete the review
        if (validationSucceeded && commitRequested) {
            reviewLevelComment.setExtraInfo((approveFixes) ? "Approved" : "Rejected");

            // Set the completed status of the review
            review.setCommitted(true);
        } else if (previewRequested) {
            reviewLevelComment.setExtraInfo((approveFixes) ? "Approved" : "Rejected");

            // Retrieve some basic aggregation info and store it into the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "FinalReview");

            // Update review object stored in the request
            request.setAttribute("review", review);

            // Notify View page that this is actually a preview operation
            request.setAttribute("isPreview", Boolean.TRUE);

            // Forward to preview page
            return Constants.PREVIEW_FORWARD_NAME;
        }

        // Update (save) edited Aggregation
        ReviewManager revMgr = ActionsHelper.createReviewManager();
        revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        if (!validationSucceeded) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "FinalReview");

            return INPUT;
        }

        // Forward to project details page
        setPid(verification.getProject().getId());
        return Constants.SUCCESS_FORWARD_NAME;
    }

    /**
     * This static method validates Final Review scorecard. This type of scorecard is considered
     * valid if all reviewers' notes have been marked as &#39;Fixed&#39;, or if comments have been
     * entered for notes marked as &#39;Not&#160;Fixed&#39;.
     *
     * @return <code>true</code> if scorecard passes validation, <code>false</code> if it does
     *         not.
     * @param request
     *        an <code>HttpServletRequest</code> object where validation error messages will
     *        be placed to in case there are any.
     * @param scorecardTemplate
     *        a scorecard template of type &quot;Review&quot; that was used to generate the
     *        aggregation review scorecard to be validated.
     * @param finalReview
     *        a final review scorecard to be validated.
     * @throws IllegalArgumentException
     *         if any of the parameters are <code>null</code>.
     */
    private static boolean validateFinalReviewScorecard(
        HttpServletRequest request, Scorecard scorecardTemplate, Review finalReview) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(scorecardTemplate, "scorecardTemplate");
        ActionsHelper.validateParameterNotNull(finalReview, "finalReview");

        final int numberOfItems = finalReview.getNumberOfItems();
        int itemIdx = -1;
        int commentIdx = -1;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx) {
                    Question question = section.getQuestion(questionIdx);
                    long questionId = question.getId();

                    for (int i = 0; i < numberOfItems; ++i) {
                        if (finalReview.getItem(i).getQuestion() != questionId) {
                            continue;
                        }

                        // Get a review's item
                        Item item = finalReview.getItem(i);
                        // Specifies whether at least one "Not Fixed" radio box is checked
                        boolean notFixed = false;
                        // Specifies whether the fix is required
                        boolean required = false;

                        // Validate item's Accept/Reject status
                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            // Get a comment for the current iteration
                            Comment comment = item.getComment(j);

                            if (ActionsHelper.isReviewerComment(comment)) {
                                ++commentIdx;
                                // Verify that the item is marked as requiring a fix
                                if (comment.getCommentType().getName().equalsIgnoreCase("Required")) {
                                    required = true;
                                }
                                String fixed = (String) comment.getExtraInfo();
                                if (fixed == null ||
                                    !(fixed.equalsIgnoreCase("Fixed") || fixed.equalsIgnoreCase("Not Fixed"))) {
                                    ActionsHelper.addErrorToRequest(request,
                                        "fix_status[" + commentIdx + "]", "Error.saveFinalReview.Fix.Absent");
                                    continue;
                                }
                                if (fixed.equalsIgnoreCase("Not Fixed")) {
                                    notFixed = true;
                                }
                                continue;
                            }

                            // Skip unneeded comments
                            if (!ActionsHelper.isFinalReviewComment(comment)) {
                                continue;
                            }

                            ++itemIdx;
                            String commentText = comment.getComment();

                            if (commentText == null || commentText.trim().length() == 0) {
                                if (notFixed && required) {
                                    ActionsHelper.addErrorToRequest(request, "final_comment[" + itemIdx + "]",
                                        "Error.saveFinalReview.Response.Absent");
                                }
                            } else if (commentText.length() > MAX_COMMENT_LENGTH) {
                                ActionsHelper.addErrorToRequest(request, "final_comment[" + itemIdx + "]",
                                    "Error.saveFinalReview.Comment.MaxExceeded");
                            }
                        }
                    }
                }
            }
        }

        return !ActionsHelper.isErrorsPresent(request);
    }
}

