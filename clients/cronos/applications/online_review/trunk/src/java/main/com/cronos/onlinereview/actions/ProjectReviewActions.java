/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.LazyValidatorForm;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.review.ReviewEntityNotFoundException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.data.ReviewEditor;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.review.scorecalculator.ScoreCalculator;
import com.topcoder.management.review.scorecalculator.ScorecardMatrix;
import com.topcoder.management.review.scorecalculator.builders.DefaultScorecardMatrixBuilder;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class contains Struts Actions that are meant to deal with Project's Reviews. There are
 * following Actions defined in this class:
 * <ul>
 * <li>Create Screening</li>
 * <li>Edit Screening</li>
 * <li>Save Screening</li>
 * <li>View Screening</li>
 * <li>Create Review</li>
 * <li>Edit Review</li>
 * <li>Save Review</li>
 * <li>View Review</li>
 * <li>Edit Aggregation</li>
 * <li>Save Aggregation</li>
 * <li>View Aggregation</li>
 * <li>Edit Aggregation Review</li>
 * <li>Save Aggregation Review</li>
 * <li>View Aggregation Review</li>
 * <li>Edit Final Review</li>
 * <li>Save Final Review</li>
 * <li>View Final Review</li>
 * <li>Create Approval</li>
 * <li>Edit Approval</li>
 * <li>Save Approval</li>
 * <li>View Approval</li>
 * <li>View Composite Scorecard</li>
 * </ul>
 * <p>
 * Note, that although &quot;Create Aggregation&quot; and &quot;Create Final Review&quot; actions
 * were initially specified in the Design Specification document, they are not implemented by this
 * class, as the section 1.1.1 Review Scorecards of the same document states that the corresponding
 * scorecards are produced automatically upon the opening of the corresponding phase. The
 * corresponding methods for these actions were not removed from this class to match Actions
 * Interface Diagram though. These functions simply do nothing and return <code>null</code> as
 * their result.
 * </p>
 * <p>
 * This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSAssemblyTeam
 * @version 1.0
 */
public class ProjectReviewActions extends DispatchAction {

    /**
     * Creates a new instance of the <code>ProjectReviewActions</code> class.
     */
    public ProjectReviewActions() {
    }

    /**
     * This method is an implementation of &quot;Create Screening&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (scorecard template) and
     * present it to editReview.jsp page, which will fill the required fields and post them to the
     * &quot;Save Screening&quot; action. The action implemented by this method is executed to edit
     * screening that does not exist yet, and hence is supposed to be created.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent submission id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward createScreening(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        return createGenericReview(mapping, form, request, "Screening");
    }

    /**
     * This method is an implementation of &quot;Edit Screening&quot; Struts Action defined for this
     * assembly, which is supposed to gather needed information (screening and scorecard template)
     * and present it to editReview.jsp page, which will fill the required fields and post them to
     * the &quot;Save Screening&quot; action. The action implemented by this method is executed to
     * edit screening that has already been created, but has not been submitted yet, and hence is
     * supposed to be edited.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward editScreening(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        return editGenericReview(mapping, form, request, "Screening");
    }

    /**
     * This method is an implementation of &quot;Save Screening&quot; Struts Action defined for this
     * assembly, which is supposed to save information posted from /jsp/editReview.jsp page. This
     * method will either create new screening or update (edit) an existing one depending on which
     * action was called to display /jsp/editReview.jsp page.
     *
     * @return &quot;success&quot; forward, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent submission id, or the lack of permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward saveScreening(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        return saveGenericReview(mapping, form, request, "Screening");
    }

    /**
     * This method is an implementation of &quot;View Screening&quot; Struts Action defined for this
     * assembly, which is supposed to view completed screening.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward viewScreening(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        return viewGenericReview(mapping, form, request, "Screening");
    }

    /**
     * This method is an implementation of &quot;Create Review&quot; Struts Action defined for this
     * assembly, which is supposed to gather needed information (scorecard template) and present it
     * to editReview.jsp page, which will fill the required fields and post them to the &quot;Save
     * Review&quot; Action. The action implemented by this method is executed to edit review that
     * does not exist yet, and hence is supposed to be created.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent submission id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward createReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        return createGenericReview(mapping, form, request, "Review");
    }

    /**
     * This method is an implementation of &quot;Edit Review&quot; Struts Action defined for this
     * assembly, which is supposed to gather needed information (review and scorecard template) and
     * present it to editReview.jsp page, which will fill the required fields and post them to the
     * &quot;Save Review&quot; action. The action implemented by this method is executed to edit
     * review that has already been created, but has not been submitted yet, and hence is supposed
     * to be edited.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward editReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        return editGenericReview(mapping, form, request, "Review");
    }

    /**
     * This method is an implementation of &quot;Save Review&quot; Struts Action defined for this
     * assembly, which is supposed to save information posted from /jsp/editReview.jsp page. This
     * method will either create new review or update (edit) an existing one depending on which
     * action was called to display /jsp/editReview.jsp page.
     *
     * @return &quot;success&quot; forward, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent submission id, or the lack of permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward saveReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        return saveGenericReview(mapping, form, request, "Review");
    }

    /**
     * This method is an implementation of &quot;View Review&quot; Struts Action defined for this
     * assembly, which is supposed to view completed review.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward viewReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        return viewGenericReview(mapping, form, request, "Review");
    }

    /**
     * This method was supposed to be an implementation of the &quot;Create Aggregation&quot; Struts
     * Action defined for this assembly, but as section 1.1.1 Review Scorecards in the Design
     * Specification document states, &quot;Aggregation scorecard will be produced automatically
     * upon the opening of the aggregation phase.&quot; This renders the implementation of this
     * action unnecessary. The method itself was not removed from the class to match Actions
     * Interface Diagram though.
     *
     * @return this method is not implemented, and so it always returns <code>null</code>.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward createAggregation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // Nothing needs to be done
        return null;
    }

    /**
     * This method is an implementation of &quot;Edit Aggregation&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (aggregation and review
     * scorecard template) and present it to editAggregation.jsp page, which will fill the required
     * fields and post them to the &quot;Save Aggregation&quot; action. The action implemented by
     * this method is executed to edit aggregation that has already been created (by the system),
     * but has not been submitted yet, and hence is supposed to be edited.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editAggregation.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward editAggregation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREGATION_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.ReviewTypeIncorrect");
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.ReviewCommitted");
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);

        // Retrieve all comment types first
        CommentType allCommentTypes[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                ActionsHelper.findCommentTypeByName(allCommentTypes, "Comment"),
                ActionsHelper.findCommentTypeByName(allCommentTypes, "Required"),
                ActionsHelper.findCommentTypeByName(allCommentTypes, "Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);

        int allCommentsNum = 0;

        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            Item item = review.getItem(i);
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                String commentType = item.getComment(j).getCommentType().getName();
                if (!commentType.equalsIgnoreCase("Aggregation Comment")) {
                    ++allCommentsNum;
                }
            }
        }

        LazyValidatorForm aggregationForm = (LazyValidatorForm) form;

        String[] aggregatorResponses = new String[review.getNumberOfItems()];
        String[] aggregateFunctions = new String[allCommentsNum];
        Long[] responseTypeIds = new Long[allCommentsNum];
        int commentIndex = 0;
        int itemIndex = 0;

        for (int i = 0; i < aggregatorResponses.length; ++i) {
            Item item = review.getItem(i);
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                Comment comment = item.getComment(j);
                String commentType = comment.getCommentType().getName();

                if (commentType.equalsIgnoreCase("Comment") || commentType.equalsIgnoreCase("Required") ||
                        commentType.equalsIgnoreCase("Recommended")) {
                    String aggregFunction = (String) comment.getExtraInfo();
                    if ("Reject".equalsIgnoreCase(aggregFunction)) {
                        aggregateFunctions[commentIndex] = "Reject";
                    } else if ("Accept".equalsIgnoreCase(aggregFunction)) {
                        aggregateFunctions[commentIndex] = "Accept";
                    } else if ("Duplicate".equalsIgnoreCase(aggregFunction)) {
                        aggregateFunctions[commentIndex] = "Duplicate";
                    } else {
                        aggregateFunctions[commentIndex] = "";
                    }
                    responseTypeIds[commentIndex] = new Long(comment.getCommentType().getId());
                    ++commentIndex;
                }
                if (commentType.equalsIgnoreCase("Aggregation Comment")) {
                    aggregatorResponses[itemIndex++] = comment.getComment();
                }
            }
        }

        aggregationForm.set("aggregator_response", aggregatorResponses);
        aggregationForm.set("aggregate_function", aggregateFunctions);
        aggregationForm.set("aggregator_response_type", responseTypeIds);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method is an implementation of &quot;Save Aggregation&quot; Struts Action defined for
     * this assembly, which is supposed to save information posted from /jsp/editAggregation.jsp
     * page. This method will update (edit) existing aggregation.
     *
     * @return &quot;success&quot; forward, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent review id, or the lack of permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward saveAggregation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREGATION_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to save
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.ReviewTypeIncorrect");
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.ReviewCommitted");
        }

        // Get an array of all phases for current project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(request), verification.getProject());
        // Get an active phase for the project
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.AGGREGATION_PHASE_NAME);
        // Check that Aggregation phase is really active (open)
        if (phase == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.IncorrectPhase");
        }

        // Retrieve a resource for the Aggregation phase
        Resource resource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Get the form defined for this action
        LazyValidatorForm aggregationForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] responses = (String[]) aggregationForm.get("aggregator_response");
        String[] aggregateFunctions = (String[]) aggregationForm.get("aggregate_function");
        Long[] responseTypeIds = (Long[]) aggregationForm.get("aggregator_response_type");
        int commentIndex = 0;
        int itemIndex = 0;

        // Obtain an instance of review manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Retrieve all comment types
        CommentType[] allCommentTypes = revMgr.getAllCommentTypes();

        // Iterate over the items of existing review that needs updating
        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            // Get an item
            Item item = review.getItem(i);
            Comment aggregatorComment = null;

            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                Comment comment = item.getComment(j);
                String typeName = comment.getCommentType().getName();

                if (typeName.equalsIgnoreCase("Comment") || typeName.equalsIgnoreCase("Required") ||
                        typeName.equalsIgnoreCase("Recommended")) {
                    comment.setExtraInfo(aggregateFunctions[commentIndex]);
                    comment.setCommentType(ActionsHelper.findCommentTypeById(
                            allCommentTypes, responseTypeIds[commentIndex].longValue()));
                    ++commentIndex;
                }
                if (typeName.equalsIgnoreCase("Aggregation Comment")) {
                    aggregatorComment = comment;
                }
            }

            if (aggregatorComment == null) {
                aggregatorComment = new Comment();
                aggregatorComment.setCommentType(
                        ActionsHelper.findCommentTypeByName(allCommentTypes, "Aggregation Comment"));
                item.addComment(aggregatorComment);
            }

            aggregatorComment.setComment(responses[itemIndex++]);
            aggregatorComment.setAuthor(resource.getId());
        }

        // If the user has requested to complete the review
        if ("submit".equalsIgnoreCase(request.getParameter("save"))) {
            // TODO: Validate review here

            // Set the completed status of the review
            review.setCommitted(true);

            // Obtain an instance of Resource Manager
            ResourceManager resMgr = ActionsHelper.createResourceManager(request);
            Phase reviewPhase = ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME);
            Resource[] reviewers = ActionsHelper.getAllResourcesForPhase(resMgr, reviewPhase);
            Resource submitter = resMgr.getResource(verification.getSubmission().getUpload().getOwner());

            for (int i = 0; i < reviewers.length; ++i) {
                Resource reviewer = reviewers[i];
                long currentId = Long.parseLong((String) reviewer.getProperty("External Reference ID"));
                if (currentId == AuthorizationHelper.getLoggedInUserId(request)) {
                    continue;
                }
                // A new review-level comment from Reviewer
                Comment comment = new Comment();
                comment.setCommentType(
                        ActionsHelper.findCommentTypeByName(allCommentTypes, "Aggregation Review Comment"));
                comment.setAuthor(reviewer.getId());
                comment.setExtraInfo("Approving");
                comment.setComment("");
                review.addComment(comment);
            }
            // A new review-level comment from Submitter
            Comment comment = new Comment();
            comment.setCommentType(
                    ActionsHelper.findCommentTypeByName(allCommentTypes, "Submitter Comment"));
            comment.setAuthor(submitter.getId());
            comment.setExtraInfo("Approving");
            comment.setComment("");
            review.addComment(comment);
        } else if ("preview".equalsIgnoreCase(request.getParameter("save"))) {
            // Retrieve some basic aggregation info and store it into the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");

            // Update review object stored in the request
            request.setAttribute("review", review);

            // Get the word "of" for Test Case type of question
            String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
            // Plase the string into the request as attribute
            request.setAttribute("wordOf", " "  + wordOf + " ");

            // Forward to preview page
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Update (save) edited Aggregation
        revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        // Forward to project details page
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
    }

    /**
     * This method is an implementation of &quot;View Aggregation&quot; Struts Action defined for
     * this assembly, which is supposed to view completed aggregation.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewAggregation.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward viewAggregation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException{
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.VIEW_AGGREGATION_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review (aggregation) to view
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREGATION_PERM_NAME, "Error.ReviewTypeIncorrect");
        }
        // Make sure that the user is trying to view Aggregation Review, not Aggregation
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREGATION_PERM_NAME, "Error.ReviewNotCommitted");
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Place the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method is an implementation of &quot;Edit Aggregation Review&quot; Struts Action defined
     * for this assembly, which is supposed to gather needed information (completed aggregation and
     * review scorecard template) and present it to editAggregationReview.jsp page, which will fill
     * the required fields and post them to the &quot;Save Aggregation Review&quot; action. The
     * action implemented by this method is executed to edit aggregation review that has already
     * been created (by &quot;Save Aggregation&quot; action), but has not been submitted yet, and
     * hence is supposed to be edited.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editAggregationReview.jsp
     *         page (as defined in struts-config.xml file), or &quot;userError&quot; forward, which
     *         forwards to the /jsp/userError.jsp page, which displays information about an error
     *         that is usually caused by incorrect user input (such as absent review id, or the lack
     *         of permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward editAggregationReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREG_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect");
        }
        // Verify that Aggregation has been committed
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.AggregationNotCommitted");
        }

        /*
         * Verify that Aggregation Review has not been committed by this user
         */

        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");

        Comment myReviewComment = null;
        Comment submitterComment = null;
        // Find "my" comment in the review scope
        for (int i = 0; i < review.getNumberOfComments(); ++i) {
            // Get a comment for the current iteration
            Comment comment = review.getComment(i);

            // If submitter's comment has been found, store it in the corresponding variable
            if (comment.getCommentType().getName().equalsIgnoreCase("Submitter Comment")) {
                submitterComment = comment;
            }

            // If "my" review comment has been found, move on to the next comment
            if (myReviewComment != null) {
                continue;
            }
            // Attempt to find "my" review comment
            for (int j = 0; j < myResources.length; ++j) {
                if (comment.getAuthor() == myResources[j].getId()) {
                    myReviewComment = comment;
                    break;
                }
            }
        }

        // If "my" comment has not been found, then the user is probably an Aggregator
        if (myReviewComment == null) {
            if (AuthorizationHelper.hasUserRole(request, Constants.AGGREGATOR_ROLE_NAME)) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.CannotReviewOwnAggregation");
            } else {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.NoPermission");
            }
        }

        // Do actual verificartion. Values "Approved" and "Rejected" denote committed Aggregation Review
        String myExtaInfo = (String) myReviewComment.getExtraInfo();
        if ("Approved".equalsIgnoreCase(myExtaInfo) || "Rejected".equalsIgnoreCase(myExtaInfo)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.ReviewCommitted");
        }

        boolean isSubmitter = false;

        // If the user is a Submitter, let underlying JSP page know about this fact
        if (AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME)) {
            isSubmitter = true;
            request.setAttribute("isSubmitter", new Boolean(isSubmitter));
        } else {
            // Otherwise examine the submitter's comment
            if (submitterComment != null) {
                String submitterExtraInfo = (String) submitterComment.getExtraInfo();
                if ("Approved".equalsIgnoreCase(submitterExtraInfo) ||
                        "Rejected".equalsIgnoreCase(submitterExtraInfo)) {
                    // Indicate to the underlying JSP page that submitter has committed its
                    // Aggregation Review, so submitter's comments can be displayed to the reviewers
                    request.setAttribute("submitterCommitted", new Boolean(true));
                }
            }
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "AggregationReview");

        LazyValidatorForm aggregationReviewForm = (LazyValidatorForm) form;

        String[] reviewFunctions = new String[review.getNumberOfItems()];
        String[] rejectReasons = new String[review.getNumberOfItems()];

        Arrays.fill(reviewFunctions, "Accept");
        Arrays.fill(rejectReasons, "");

        for (int i = 0; i < reviewFunctions.length; ++i) {
            Item item = review.getItem(i);
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                Comment comment = item.getComment(j);
                String commentType = comment.getCommentType().getName();

                if ((isSubmitter && commentType.equalsIgnoreCase("Submitter Comment")) ||
                        (!isSubmitter && myReviewComment.getAuthor() == comment.getAuthor() &&
                                commentType.equalsIgnoreCase("Aggregation Review Comment"))) {
                    String reviewFunction = (String) comment.getExtraInfo();
                    if ("Reject".equalsIgnoreCase(reviewFunction)) {
                        reviewFunctions[i] = "Reject";
                    }
                    if (comment.getComment() != null && comment.getComment().trim().length() != 0) {
                        rejectReasons[i] = comment.getComment();
                    }
                    break;
                }
            }
        }

        aggregationReviewForm.set("review_function", reviewFunctions);
        aggregationReviewForm.set("reject_reason", rejectReasons);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method is an implementation of &quot;Save Aggregation Review&quot; Struts Action defined
     * for this assembly, which is supposed to save information posted from
     * /jsp/editAggregationReview.jsp page. This method will update (edit) aggregation review.
     *
     * @return &quot;success&quot; forward, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent review id, or the lack of permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward saveAggregationReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREG_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to save
        Review review = verification.getReview();

        // Obtain an instance of Scorecad Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect");
        }
        // Verify that the user is attempting to save Aggregation Review, not Aggregation
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.AggregationNotCommitted");
        }

        /*
         * Verify that Aggregation Review has not been committed by this user
         */

        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");

        Comment myReviewComment = null;
        // Find "my" comment in the review scope
        for (int i = 0; i < review.getNumberOfComments(); ++i) {
            // Get a comment for the current iteration
            Comment comment = review.getComment(i);
            for (int j = 0; j < myResources.length; ++j) {
                if (comment.getAuthor() == myResources[j].getId()) {
                    myReviewComment = comment;
                    break;
                }
            }
            if (myReviewComment != null) {
                break;
            }
        }

        // If "my" comment has not been found, then the user is probably an Aggregator
        if (myReviewComment == null) {
            if (AuthorizationHelper.hasUserRole(request, Constants.AGGREGATOR_ROLE_NAME)) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.CannotReviewOwnAggregation");
            } else {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.NoPermission");
            }
        }

        // Do actual verificartion. Values "Approved" and "Rejected" denote committed Aggregation Review
        String myExtaInfo = (String) myReviewComment.getExtraInfo();
        if ("Approved".equalsIgnoreCase(myExtaInfo) || "Rejected".equalsIgnoreCase(myExtaInfo)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.ReviewCommitted");
        }

        // Determine if the user is Submitter
        boolean isSubmitter = AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME);

        // Get the form defined for this action
        LazyValidatorForm aggregationReviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] reviewFunctions = (String[]) aggregationReviewForm.get("review_function");
        String[] rejectReasons = (String[]) aggregationReviewForm.get("reject_reason");

        // Obtain an instance of review manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Retrieve all comment types
        CommentType[] allCommentTypes = revMgr.getAllCommentTypes();
        // Determine the type of comment to search for
        CommentType commentType = ActionsHelper.findCommentTypeByName(allCommentTypes,
                (isSubmitter == true) ? "Submitter Comment" : "Aggregation Review Comment");

        // Denotes the rejected status of the Aggregation Review.
        // This variable will be updated during the next loop over all items of the review
        boolean rejected = false;

        // Iterate over the items of existing review that needs updating
        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            // Get an item for the current iteration
            Item item = review.getItem(i);

            // Find a comment from this user
            Comment userComment = null;
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                Comment comment = item.getComment(j);

                if (comment.getAuthor() == myReviewComment.getAuthor() &&
                        comment.getCommentType().getId() == commentType.getId()) {
                    userComment = comment;
                    break;
                }
            }

            // If comment has not been found, it means that this user has not had
            // chance to enter his comments yet, so create the Comment object first
            if (userComment == null) {
                userComment = new Comment();
                // Prefill needed fields
                userComment.setCommentType(commentType);
                userComment.setAuthor(myReviewComment.getAuthor());
                // Add this newly-created comment to review's item
                item.addComment(userComment);
            }

            userComment.setComment(rejectReasons[i]);
            // If review function equals to anythning but "Accept", then regard the item as rejected
            if ("Accept".equalsIgnoreCase(reviewFunctions[i])) {
                userComment.setExtraInfo("Accept");
            } else {
                userComment.setExtraInfo("Reject");
                rejected = true;
            }
        }

        // If the user has requested to complete the review
        if ("submit".equalsIgnoreCase(request.getParameter("save"))) {
            // TODO: Validate review here

            // Values "Approved" or "Rejected" will denote committed review
            myReviewComment.setExtraInfo((rejected == true) ? "Rejected" : "Approved");
        }

        // Update (save) edited Aggregation Review
        revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        // Forward to project details page
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
    }

    /**
     * This method is an implementation of &quot;View Aggregation Review&quot; Struts Action defined
     * for this assembly, which is supposed to view completed aggregation review. The Aggregation
     * review must be completed by submitter and all reviewers (except the reviewer that is also an
     * aggregator).
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewAggregationReview.jsp
     *         page (as defined in struts-config.xml file), or &quot;userError&quot; forward, which
     *         forwards to the /jsp/userError.jsp page, which displays information about an error
     *         that is usually caused by incorrect user input (such as absent review id, or the lack
     *         of permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward viewAggregationReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.VIEW_AGGREG_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review (aggregation) to view
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREG_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect");
        }
        // Make sure that the user is not trying to view unfinished review
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREG_REVIEW_PERM_NAME, "Error.AggregationNotCommitted");
        }

        // Verify that Aggregation Review has been committed by all users who should have done that
        for (int i = 0; i < review.getNumberOfComments(); ++i) {
            Comment comment = review.getComment(i);
            String status = (String) comment.getExtraInfo();
            if (!("Approved".equalsIgnoreCase(status) || "Rejected".equalsIgnoreCase(status))) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.VIEW_AGGREG_REVIEW_PERM_NAME, "Error.AggregationReviewNotCommitted");
            }
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "AggregationReview");

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Place the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method was supposed to be an implementation of the &quot;Create Final Review&quot;
     * Struts Action defined for this assembly, but as section 1.1.1 Review Scorecards in the Design
     * Specification document states, &quot;Final review scorecard will be produced automatically
     * upon the opening of the final review phase.&quot; This renders the implementation of this
     * action unnecessary. The method itself was not removed from the class to match Actions
     * Interface Diagram though.
     *
     * @return this method is not implemented, and so it always returns <code>null</code>.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward createFinalReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // Nothing needs to be done
        return null;
    }

    /**
     * This method is an implementation of &quot;Edit Final Review&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (final fix review and review
     * scorecard template) and present it to editFinalReview.jsp page, which will fill the required
     * fields and post them to the &quot;Save Final Review&quot; action. The action implemented by
     * this method is executed to edit final fix review that has already been created (by the
     * system), but has not been submitted yet, and hence is supposed to be edited.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editFinalReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward editFinalReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect");
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewCommitted");
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "AggregationReview");

        int reviewerCommentsNum = 0;
        int[] lastCommentIdxs = new int[review.getNumberOfItems()];

        Arrays.fill(lastCommentIdxs, 0);

        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            Item item = review.getItem(i);
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                String commentType = item.getComment(j).getCommentType().getName();
                if (commentType.equalsIgnoreCase("Comment") || commentType.equalsIgnoreCase("Required") ||
                        commentType.equalsIgnoreCase("Recommended")) {
                    ++reviewerCommentsNum;
                    ++lastCommentIdxs[i];
                } else if (commentType.equalsIgnoreCase("Manager Comment") ||
                        commentType.equalsIgnoreCase("Appeal") ||
                        commentType.equalsIgnoreCase("Appeal Response") ||
                        commentType.equalsIgnoreCase("Aggregation Comment") ||
                        commentType.equalsIgnoreCase("Aggregation Review Comment") ||
                        commentType.equalsIgnoreCase("Submitter Comment")) {
                    ++lastCommentIdxs[i];
                }
            }
        }

        request.setAttribute("lastCommentIdxs", lastCommentIdxs);

        boolean fixesApproved = false;

        for (int i = 0; i < review.getNumberOfComments(); ++i) {
            Comment comment = review.getComment(i);
            if (comment.getCommentType().getName().equalsIgnoreCase("Final Review Comment")) {
                fixesApproved = ("Approved".equalsIgnoreCase((String) comment.getExtraInfo()));
                break;
            }
        }

        LazyValidatorForm finalReviewForm = (LazyValidatorForm) form;

        String[] fixStatuses = new String[reviewerCommentsNum];
        String[] finalComments = new String[review.getNumberOfItems()];
        Boolean approveFixes = new Boolean(fixesApproved);
        int commentIndex = 0;

        for (int i = 0; i < finalComments.length; ++i) {
            Item item = review.getItem(i);
            boolean finalReviewCommentNotFound = true;

            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                Comment comment = item.getComment(j);
                String commentType = comment.getCommentType().getName();

                if (commentType.equalsIgnoreCase("Comment") || commentType.equalsIgnoreCase("Required") ||
                        commentType.equalsIgnoreCase("Recommended")) {
                    String fixStatus = (String) comment.getExtraInfo();
                    if ("Fixed".equalsIgnoreCase(fixStatus)) {
                        fixStatuses[commentIndex] = "Fixed";
                    } else if ("Not Fixed".equalsIgnoreCase(fixStatus)) {
                        fixStatuses[commentIndex] = "Not Fixed";
                    } else {
                        fixStatuses[commentIndex] = "";
                    }
                    ++commentIndex;
                }
                if (finalReviewCommentNotFound && commentType.equalsIgnoreCase("Final Review Comment")) {
                    finalComments[i] = comment.getComment();
                    finalReviewCommentNotFound = false;
                }
            }
        }

        finalReviewForm.set("fix_status", fixStatuses);
        finalReviewForm.set("final_comment", finalComments);
        finalReviewForm.set("approve_fixes", approveFixes);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method is an implementation of &quot;Save Final Review&quot; Struts Action defined for
     * this assembly, which is supposed to save information posted from /jsp/editFinalReview.jsp
     * page. This method will update (edit) final review.
     *
     * @return &quot;success&quot; forward, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent review id, or the lack of permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward saveFinalReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to save
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect");
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewCommitted");
        }

        // Get an array of all phases for current project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(request), verification.getProject());
        // Get an active phase for the project
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.FINAL_REVIEW_PHASE_NAME);
        // Check that Final Review Phase is really active (open)
        if (phase == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.IncorrectPhase");
        }

        // Retrieve a resource for the Final Review phase
        Resource resource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Get the form defined for this action
        LazyValidatorForm finalReviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] fixStatuses = (String[]) finalReviewForm.get("fix_status");
        String[] finalComments = (String[]) finalReviewForm.get("final_comment");
        Boolean approveFixesObj = (Boolean) finalReviewForm.get("approve_fixes");
        boolean approveFixes = (approveFixesObj != null && approveFixesObj.booleanValue() == true);
        int commentIndex = 0;
        int finalIdx = 0;

        // Obtain an instance of review manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Retrieve all comment types
        CommentType[] allCommentTypes = revMgr.getAllCommentTypes();

        // Iterate over the items of existing review that needs updating
        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            // Get an item
            Item item = review.getItem(i);

            if (item.getNumberOfComments() == 0) {
                continue;
            }

            Comment finalReviewComment = null;

            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                Comment comment = item.getComment(j);
                String typeName = comment.getCommentType().getName();

                if (typeName.equalsIgnoreCase("Comment") || typeName.equalsIgnoreCase("Required") ||
                        typeName.equalsIgnoreCase("Recommended")) {
                    comment.setExtraInfo(fixStatuses[commentIndex]);
                    ++commentIndex;
                }
                if (typeName.equalsIgnoreCase("Final Review Comment")) {
                    finalReviewComment = comment;
                }
            }

            if (finalReviewComment == null) {
                finalReviewComment = new Comment();
                finalReviewComment.setCommentType(
                        ActionsHelper.findCommentTypeByName(allCommentTypes, "Final Review Comment"));
                item.addComment(finalReviewComment);
            }

            finalReviewComment.setComment(finalComments[finalIdx++]);
            finalReviewComment.setAuthor(resource.getId());
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
            reviewLevelComment.setAuthor(resource.getId());
            reviewLevelComment.setComment("");
            reviewLevelComment.setCommentType(
                    ActionsHelper.findCommentTypeByName(allCommentTypes, "Final Review Comment"));
            review.addComment(reviewLevelComment);
        }

        reviewLevelComment.setExtraInfo((approveFixes == true) ? "Approved" : "Approving");

        // If the user has requested to complete the review
        if ("submit".equalsIgnoreCase(request.getParameter("save"))) {
            // TODO: Validate review here

            reviewLevelComment.setExtraInfo((approveFixes == true) ? "Approved" : "Rejected");

            // Set the completed status of the review
            review.setCommitted(true);
        } else if ("preview".equalsIgnoreCase(request.getParameter("save"))) {
            // Retrieve some basic aggregation info and store it into the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "FinalReview");

            // Update review object stored in the request
            request.setAttribute("review", review);

            // Get the word "of" for Test Case type of question
            String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
            // Plase the string into the request as attribute
            request.setAttribute("wordOf", " "  + wordOf + " ");

            // Forward to preview page
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Update (save) edited Aggregation
        revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        // Forward to project details page
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
    }

    /**
     * This method is an implementation of &quot;View Final Review&quot; Struts Action defined for
     * this assembly, which is supposed to view completed final review.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewFinalReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward viewFinalReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.VIEW_FINAL_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to view
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_FINAL_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect");
        }
        // Make sure that the user is not trying to view unfinished review
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_FINAL_REVIEW_PERM_NAME, "Error.ReviewNotCommitted");
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "FinalReview");

        int[] lastCommentIdxs = new int[review.getNumberOfItems()];

        Arrays.fill(lastCommentIdxs, 0);

        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            Item item = review.getItem(i);
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                String commentType = item.getComment(j).getCommentType().getName();
                if (commentType.equalsIgnoreCase("Comment") || commentType.equalsIgnoreCase("Required") ||
                        commentType.equalsIgnoreCase("Recommended") ||
                        commentType.equalsIgnoreCase("Appeal") || commentType.equalsIgnoreCase("Appeal Response") ||
                        commentType.equalsIgnoreCase("Manager Comment") ||
                        commentType.equalsIgnoreCase("Aggregation Comment") ||
                        commentType.equalsIgnoreCase("Aggregation Review Comment") ||
                        commentType.equalsIgnoreCase("Submitter Comment") ||
                        commentType.equalsIgnoreCase("Final Review Comment")) {
                    ++lastCommentIdxs[i];
                }
            }
        }

        request.setAttribute("lastCommentIdxs", lastCommentIdxs);

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Place the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method is an implementation of &quot;Create Approval&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (scorecard template) and
     * present it to editReview.jsp page, which will fill the required fields and post them to the
     * &quot;Save Approval&quot; action. The action implemented by this method is executed to edit
     * approval that does not exist yet, and hence is supposed to be created.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent submission id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward createApproval(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException{
        return createGenericReview(mapping, form, request, "Approval");
    }

    /**
     * This method is an implementation of &quot;Edit Approval&quot; Struts Action defined for this
     * assembly, which is supposed to gather needed information (approval and scorecard template)
     * and present it to editReview.jsp page, which will fill the required fields and post them to
     * the &quot;Save Approval&quot; action. The action implemented by this method is executed to
     * edit approval that has already been created, but has not been submitted yet, and hence is
     * supposed to be edited.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward editApproval(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException{
        return editGenericReview(mapping, form, request, "Approval");
    }

    /**
     * This method is an implementation of &quot;Save Approval&quot; Struts Action defined for this
     * assembly, which is supposed to save information posted from /jsp/editReview.jsp page. This
     * method will either create new approval or update (edit) an existing one depending on which
     * action was called to display /jsp/editReview.jsp page.
     *
     * @return &quot;success&quot; forward, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent submission id, or the lack of permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward saveApproval(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        return saveGenericReview(mapping, form, request, "Approval");
    }

    /**
     * This method is an implementation of &quot;View Approval&quot; Struts Action defined for this
     * assembly, which is supposed to view completed approval.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward viewApproval(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        return viewGenericReview(mapping, form, request, "Approval");
    }

    /**
     * This method is an implementation of &quot;View Composite Scorecard&quot; Struts Action
     * defined for this assembly, which is supposed to gather needed information (scorecard template
     * and reviews from individual reviewers for some submission) and present it to
     * viewCompositeScoecard.jsp page, which will present all the gathered information to the user.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewCompositeScoecard.jsp
     *         page (as defined in struts-config.xml file), or &quot;userError&quot; forward, which
     *         forwards to the /jsp/userError.jsp page, which displays information about an error
     *         that is usually caused by incorrect user input (such as absent submission id, or the
     *         lack of permissions, etc.).
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     * @throws BaseException
     *             if any error occurs.
     */
    public ActionForward viewCompositeScorecard(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectSubmissionId(mapping, request, Constants.VIEW_COMPOS_SCORECARD_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request), project);
        // Get the Review phase
        Phase phase = ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME);
        // Retrieve a scorecard template for the Review phase
        Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(
                ActionsHelper.createScorecardManager(request), phase);

        // Prepare filters
        Filter filterSubmission = new EqualToFilter("submission", new Long(verification.getSubmission().getId()));
        Filter filterCommitted = new EqualToFilter("committed", new Integer(1));
        Filter filterScorecard = new EqualToFilter("scorecardType",
                new Long(scorecardTemplate.getScorecardType().getId()));

        // Build the list of all filters that should be joined using AND operator
        List filters = new ArrayList();
        filters.add(filterSubmission);
        filters.add(filterScorecard);
        filters.add(filterCommitted);

        // Prepare final combined filter
        Filter filter = new AndFilter(filters);
        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Retrieve an array of reviews
        Review[] reviews = revMgr.searchReviews(filter, true);

        if (reviews.length != 3) {
            return null; // TODO: Forward to userError.jsp page
        }

        // Obtain Resource Manager instance
        ResourceManager resourceManager = ActionsHelper.createResourceManager(request);
        // Obtain ScorecardMatrix for scorecard
        ScorecardMatrix matrix = (new DefaultScorecardMatrixBuilder()).buildScorecardMatrix(scorecardTemplate);
        // Create CalculationManager instance
        CalculationManager calculationManager = new CalculationManager();

        // Retrieve the user ids for the review authors
        // and additionally the individual item scores and average total score
        long[] authors = new long[reviews.length];
        float avgScore = 0;
        float[][] scores = new float[reviews.length][];
        for (int i = 0; i < reviews.length; i++) {
            Resource authorResource = resourceManager.getResource(reviews[i].getAuthor());
            authors[i] = Long.parseLong((String) authorResource.getProperty("External Reference ID"));
            avgScore += reviews[i].getScore().floatValue();
            scores[i] = new float[reviews[i].getNumberOfItems()];
            int itemIdx = 0;
            for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); groupIdx++) {
                Group group = scorecardTemplate.getGroup(groupIdx);
                for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); sectionIdx++) {
                    Section section = group.getSection(sectionIdx);
                    for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); questionIdx++) {
                        Question question = section.getQuestion(questionIdx);
                        ScoreCalculator scoreCalculator = calculationManager.getScoreCalculator(question.getQuestionType().getId());
                        scores[i][itemIdx] = (float) (matrix.getLineItem(question.getId()).getWeight() *
                            scoreCalculator.evaluateItem(reviews[i].getItem(itemIdx), question));
                        itemIdx++;
                    }
                }
            }
        }
        // TODO: Calculate average per-item scores
        avgScore /= reviews.length;
        // Store gathered data into the request
        request.setAttribute("authors", authors);
        request.setAttribute("avgScore", new Float(avgScore));
        request.setAttribute("scores", scores);

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, "CompositeReview", scorecardTemplate);

        // Store reviews in the request
        request.setAttribute("reviews", reviews);

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Plase the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * This method verifies the request for ceratins conditions to be met. This includes verifying
     * if the user has specified an ID of the submission he wants to perform an operation on, if the
     * ID of the submission specified by user denotes an existing submission, and whether the user
     * has enough rights to perform the operation specified by <code>permission</code> parameter.
     *
     * @return an instance of the {@link CorrectnessCheckResult} class, which specifies whether the
     *         check was successful and, in the case the check was successful, contains additional
     *         information retrieved during the check operation, which might be of some use for the
     *         calling method.
     * @param mapping
     *            action mapping.
     * @param request
     *            the http request.
     * @param permission
     *            permission to check against, or <code>null</code> if no check is requeired.
     * @throws BaseException
     *             if any error occurs.
     */
    private CorrectnessCheckResult checkForCorrectSubmissionId(ActionMapping mapping,
            HttpServletRequest request, String permission)
        throws BaseException {
        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        if (permission == null || permission.trim().length() == 0) {
            permission = null;
        }

        // Verify that Submission ID was specified and denotes correct submission
        String sidParam = request.getParameter("sid");
        if (sidParam == null || sidParam.trim().length() == 0) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.SubmissionIdNotSpecified"));
            // Return the result of the check
            return result;
        }

        long sid;

        try {
            // Try to convert specified sid parameter to its integer representation
            sid = Long.parseLong(sidParam, 10);
        } catch (NumberFormatException e) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.SubmissionNotFound"));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Deliverable Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        // Get Submission by its id
        Submission submission = upMgr.getSubmission(sid);
        // Verify that submission with specified ID exists
        if (submission == null) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.SubmissionNotFound"));
            // Return the result of the check
            return result;
        }

        // Store Submission object in the result bean
        result.setSubmission(submission);
        // Place the id of the submission as attribute in the request
        request.setAttribute("sid", new Long(sid));

        // Retrieve the project following submission's infromation chain
        Project project = ActionsHelper.getProjectForSubmission(
                ActionsHelper.createProjectManager(request), submission);
        // Store Project object in the result bean
        result.setProject(project);
        // Place project as attribute in the request
        request.setAttribute("project", project);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, project.getId());

        // If permission parameter was not null or empty string ...
        if (permission != null) {
            // ... verify that this permission is granted for currently logged in user
            if (!AuthorizationHelper.hasUserPermission(request, permission)) {
                result.setForward(ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, permission, "Error.NoPermission"));
                // Return the result of the check
                return result;
            }
        }

        // Return the result of the check
        return result;
    }

    /**
     * This method verifies the request for ceratins conditions to be met. This includes verifying
     * if the user has specified an ID of the review he wants to perform an operation on, if the
     * ID of the review specified by user denotes an existing review, and whether the user
     * has enough rights to perform the operation specified by <code>permission</code> parameter.
     *
     * @return an instance of the {@link CorrectnessCheckResult} class, which specifies whether the
     *         check was successful and, in the case the check was successful, contains additional
     *         information retrieved during the check operation, which might be of some use for the
     *         calling method.
     * @param mapping
     *            action mapping.
     * @param request
     *            the http request.
     * @param permission
     *            permission to check against, or <code>null</code> if no check is requeired.
     * @throws BaseException
     *             if any error occurs.
     */
    private CorrectnessCheckResult checkForCorrectReviewId(ActionMapping mapping,
            HttpServletRequest request, String permission)
        throws BaseException {
        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        if (permission == null || permission.trim().length() == 0) {
            permission = null;
        }

        // Verify that Review ID was specified and denotes correct review
        String ridParam = request.getParameter("rid");
        if (ridParam == null || ridParam.trim().length() == 0) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.ReviewIdNotSpecified"));
            // Return the result of the check
            return result;
        }

        long rid;

        try {
            // Try to convert specified rid parameter to its integer representation
            rid = Long.parseLong(ridParam, 10);
        } catch (NumberFormatException e) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.ReviewNotFound"));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);

        /*
         * Review Management Persistence component throws an exception
         * if the review with specified ID does not exist in the database,
         * so this exception should be handled correctly
         */

        Review review = null;
        try {
            // Get Review by its id
            review = revMgr.getReview(rid);
        } catch (ReviewEntityNotFoundException e) {
            // Eat the exception
        }

        // Verify that review with specified ID exists
        if (review == null) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.ReviewNotFound"));
            // Return the result of the check
            return result;
        }

        // Store Review object in the result bean
        result.setReview(review);
        // Place the review object as attribute in the request
        request.setAttribute("review", review);

        // Obtain an instance of Deliverable Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        // Get Submission by its id
        Submission submission = upMgr.getSubmission(review.getSubmission());

        // Store Submission object in the result bean
        result.setSubmission(submission);
        // Place the id of the submission as attribute in the request
        request.setAttribute("sid", new Long(submission.getId()));

        // Retrieve the project following submission's infromation chain
        Project project = ActionsHelper.getProjectForSubmission(
                ActionsHelper.createProjectManager(request), submission);
        // Store Project object in the result bean
        result.setProject(project);
        // Place project as attribute in the request
        request.setAttribute("project", project);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, project.getId());

        // Return the result of the check
        return result;
    }

    /**
     * This static method gathers some vluable information for aggregation scorecard. This
     * information includes: the user ID of submitter, the user ID of aggregator, the reviewer
     * resources (who initially did the reviews which was later combined into one single
     * aggregation), individual reviews made by those reviewers, etc.
     *
     * @param request
     *            an <code>HttpServletRequest</code> object.
     * @param verification
     *            an instance of <code>CorrectnessCheckResult</code> class that must specify valid
     *            current project and aggregation for this method to succeed.
     * @param scorecardTemplate
     *            a scorecard template that describes questions (items) of the aggregation.
     * @param reviewType
     *            a type of the review, can be one of "Aggregation", "AggregationReview", "FinalReview"
     * @throws BaseException
     *             if any error occurs.
     */
    private void retrieveAndStoreBasicAggregationInfo(
            HttpServletRequest request, CorrectnessCheckResult verification, Scorecard scorecardTemplate, String reviewType)
        throws BaseException {
        // Retrieve a project from verification-result bean
        Project project = verification.getProject();
        // Retrieve a review from verification-result bean
        Review review = verification.getReview();

        // Retrieve a submission to edit an aggregation scorecard for
        Submission submission = verification.getSubmission();

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

        // Get an array of all phases for current project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(request), project);

        // Get a Review phase
        Phase reviewPhase = ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME);
        // Retrieve all resources (reviewers) for that phase
        Resource[] reviewResources = ActionsHelper.getAllResourcesForPhase(
                ActionsHelper.createResourceManager(request), reviewPhase);
        // Place information about reviews into the request
        request.setAttribute("reviewResources", reviewResources);

        // Prepare a list of reviewer IDs. This list will later be used to build filter
        List reviewerIds = new ArrayList();
        for (int i = 0; i < reviewResources.length; ++i) {
            reviewerIds.add(new Long(reviewResources[i].getId()));
        }

        // Build filters to fetch the reviews that were used to form current Aggregation
        Filter filterResources = new InFilter("reviewer", reviewerIds);
        Filter filterCommitted = new EqualToFilter("committed", new Integer(1));
        Filter filterSubmission = new EqualToFilter("submission", new Long(submission.getId()));
        Filter filterProject = new EqualToFilter("project", new Long(project.getId()));
        Filter filterScorecard = new EqualToFilter(
                "scorecardType", new Long(scorecardTemplate.getScorecardType().getId()));

        // Prepare final filter that combines all the above filters
        Filter filter = new AndFilter(Arrays.asList(new Filter[]
                {filterResources, filterCommitted, filterSubmission, filterProject, filterScorecard}));

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Fetch reviews (only basic review info is fetched, no items/comments)
        Review[] reviews = revMgr.searchReviews(filter, false);
        // Place reviews into the request. This will be used to provide links to individual reviews
        request.setAttribute("reviews", reviews);

        int[] lastCommentIdxs = new int[review.getNumberOfItems()];

        for (int i = 0; i < lastCommentIdxs.length; ++i) {
            Item item = review.getItem(i);
            for (int j = 0; j < item.getNumberOfComments(); ++j) {
                String commentType = item.getComment(j).getCommentType().getName();
                if (!commentType.equalsIgnoreCase("Aggregation Comment")) {
                    lastCommentIdxs[i] = j;
                }
            }
        }
        request.setAttribute("lastCommentIdxs", lastCommentIdxs);
    }

    /**
     * TODO: Document it.
     *
     * @param request
     * @throws BaseException
     */
    private void retreiveAndStoreReviewLookUpData(HttpServletRequest request) throws BaseException {
        // Obtain Review Manager instance
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);

        // Retrieve all comment types first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Required"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);
    }

    /**
     * TODO: Document it
     *
     * @param request
     * @param upload
     * @throws BaseException
     */
    private void retrieveAndStoreReviewAuthorInfo(HttpServletRequest request, Review review)
        throws BaseException {
        // TODO: Remove this and other functions to a separate helper class. Name it ProjectReviewActionsHelper

        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(review, "review");

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        // Get review author's resource
        Resource author = resMgr.getResource(review.getAuthor());

        // Place submitter's user ID into the request
        request.setAttribute("authorId", author.getProperty("External Reference ID"));
        // Place submitter's resource into the request
        request.setAttribute("authorResource", author);
    }

    /**
     * TODO: Document it.
     *
     * @param request
     * @param verification
     * @param reviewType
     * @param scorecardTemplate
     * @throws BaseException
     */
    private void retrieveAndStoreBasicReviewInfo(HttpServletRequest request, CorrectnessCheckResult verification,
            String reviewType, Scorecard scorecardTemplate) throws BaseException {
        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Retrieve an information about my role(s) and place it into the request
        ActionsHelper.retrieveAndStoreMyRole(request, getResources(request));
        // Retrieve the information about the submitter and place it into the request
        ActionsHelper.retrieveAndStoreSubmitterInfo(request, verification.getSubmission().getUpload());
        if (verification.getReview() != null) {
                // Retrieve the information about the review author and place it into the request
                retrieveAndStoreReviewAuthorInfo(request, verification.getReview());
        }
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);
        // Place the type of the review into the request
        request.setAttribute("reviewType", reviewType);
    }

    /**
     * TODO: Document it.
     *
     * @param mapping
     * @param form
     * @param request
     * @param reviewType
     * @return
     * @throws BaseException
     */
    private ActionForward createGenericReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, String reviewType) throws BaseException {
        String permName;
        String phaseName;
        // Determine permission name and phase name from the review type
        if ("Screening".equals(reviewType)) {
            permName = Constants.PERFORM_SCREENING_PERM_NAME;
            phaseName = Constants.SCREENING_PHASE_NAME;
        } else if ("Review".equals(reviewType)) {
            permName = Constants.PERFORM_REVIEW_PERM_NAME;
            phaseName = Constants.REVIEW_PHASE_NAME;
        } else {
            permName = Constants.PERFORM_APPROVAL_PERM_NAME;
            phaseName = Constants.APPROVAL_PHASE_NAME;
        }

        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectSubmissionId(mapping, request, permName);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request), project);

        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, phaseName);
        // Check that the phase in question is really active (open)
        if (phase == null) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permName, "Error.IncorrectPhase");
        }

        // Get "My" resource for the appropriate phase
        Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Retrieve a scorecard template for the appropriate phase
        Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(
                ActionsHelper.createScorecardManager(request), phase);

        /*
         * Verify that the user is not trying to create review that already exists
         */

        // Prepare filters
        Filter filterResource = new EqualToFilter("reviewer", new Long(myResource.getId()));
        Filter filterSubmission = new EqualToFilter("submission", new Long(verification.getSubmission().getId()));
        Filter filterScorecard = new EqualToFilter("scorecardType",
                new Long(scorecardTemplate.getScorecardType().getId()));

        // Prepare final combined filter
        Filter filter = new AndFilter(Arrays.asList(new Filter[] {filterResource, filterSubmission, filterScorecard}));
        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Retrieve an array of reviews
        Review[] reviews = revMgr.searchReviews(filter, false);

        // Non-empty array of reviews indicates that
        // user is trying to create review that already exists
        if (reviews.length != 0) {
            // Forward to Edit Sceeening page
            return ActionsHelper.cloneForwardAndAppendToPath(
                    mapping.findForward(Constants.EDIT_FORWARD_NAME), "&rid=" + reviews[0].getId());
        }

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);
        // Place current user's id as author's id
        request.setAttribute("authorId", new Long(AuthorizationHelper.getLoggedInUserId(request)));
        // Retrive some look-up data and store it into the request
        retreiveAndStoreReviewLookUpData(request);

        /*
         * Populate the form
         */

        // Determine the number of questions in scorecard template
        int questionsCount = ActionsHelper.getScorecardQuestionsCount(scorecardTemplate);

        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        String[] emptyStrings = new String[questionsCount];
        Arrays.fill(emptyStrings, "");

        // Populate form properties
        reviewForm.set("answer", emptyStrings);
        reviewForm.set("comment", emptyStrings.clone());

        Long[] commentTypes = new Long[questionsCount];
        CommentType typeComment = ActionsHelper.findCommentTypeByName(
                (CommentType[]) request.getAttribute("allCommentTypes"), "Comment");

        Arrays.fill(commentTypes, new Long(typeComment.getId()));
        reviewForm.set("commentType", commentTypes);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * TODO: Document it
     *
     * @param mapping
     * @param form
     * @param request
     * @param reviewType
     * @return
     * @throws BaseException
     */
    private ActionForward editGenericReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, String reviewType) throws BaseException {
        String scorecardTypeName;
        // Determine permission name and phase name from the review type
        if ("Screening".equals(reviewType)) {
            scorecardTypeName = "Screening";
        } else if ("Review".equals(reviewType)) {
            scorecardTypeName = "Review";
        } else {
            scorecardTypeName = "Client Review";
        }


        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.EDIT_MY_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that the user has permission to edit review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_ANY_SCORECARD_PERM_NAME)) {
            // FIXME: Temporarly dropped the permission check due to to permission granted to Screener only
            /*if (!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_MY_REVIEW_PERM_NAME)) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.NoPermission");
            } else if(verification.getReview().getAuthor() != AuthorizationHelper.getLoggedInUserId(request)) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request),
                        request, Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.NoPermission");
            }     */
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scorMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scorMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase(scorecardTypeName)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        boolean managerEdit = false;
        // Check if review has been committed
        if (review.isCommitted()) {
            // If user has a Manager role, put special flag to the request,
            // indicating that we need "Manager Edit"
            if(AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)) {
                request.setAttribute("managerEdit", Boolean.TRUE);
                managerEdit = true;
            } else {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                            Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.ReviewCommitted");
            }
        }

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

        // Retrive some look-up data and store it into the request
        retreiveAndStoreReviewLookUpData(request);

        // Prepare the arrays
        String[] answers = new String[review.getNumberOfItems()];
        String[] replies = new String[review.getNumberOfItems()];
        Long[] commentTypes = new Long[review.getNumberOfItems()];
        FormFile[] files = new FormFile[ActionsHelper.getScorecardUploadsCount(scorecardTemplate)];
        Arrays.fill(files, null);

        Long[] uploadedFileIds = new Long[files.length];
        Arrays.fill(uploadedFileIds, null);

        int itemIdx = 0;
        int fileIdx = 0;

        // Walk the items in the review setting appropriate values in the arrays
        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx, ++itemIdx) {
                    Item item = review.getItem(itemIdx);
                    Comment comment;
                    if (!managerEdit) {
                        comment = getItemReviewerComments(item)[0]; // TODO: Retrieve all comments
                    } else {
                        Comment[] managerComments = getItemManagerComments(item);
                        if (managerComments.length > 0) {
                            comment = managerComments[0]; // TODO: Retrieve all comments
                        } else {
                            comment = null;
                        }
                    }
                    answers[itemIdx] = (String) item.getAnswer();
                    if (comment != null) {
                        replies[itemIdx] = comment.getComment();
                        commentTypes[itemIdx] = new Long(comment.getCommentType().getId());
                    } else {
                        replies[itemIdx] = "";
                        commentTypes[itemIdx] = null;
                    }

                    if (!managerEdit && section.getQuestion(questionIdx).isUploadDocument()) {
                        uploadedFileIds[fileIdx++] = item.getDocument();
                    }
                }
            }
        }

        request.setAttribute("uploadedFileIds", uploadedFileIds);

        /*
         * Populate the form
         */

        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Populate form properties
        reviewForm.set("answer", answers);
        reviewForm.set("comment", replies);
        reviewForm.set("commentType", commentTypes);
        reviewForm.set("file", files);

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Plase the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * TODO: Document it
     *
     * @param item
     * @return
     */
    private Comment[] getItemManagerComments(Item item) {
        List result = new ArrayList();
        for (int i = 0; i < item.getNumberOfComments(); i++) {
            if (item.getComment(i).getCommentType().getName().equals("Manager Comment")) {
                result.add(item.getComment(i));
            }
        }
        return (Comment[]) result.toArray(new Comment[result.size()]);
    }

    /**
     * TODO: Document it
     *
     * @param item
     * @return
     */
    private Comment[] getItemReviewerComments(Item item) {
        List result = new ArrayList();
        for (int i = 0; i < item.getNumberOfComments(); i++) {
            if (item.getComment(i).getCommentType().getName().equals("Comment") ||
                    item.getComment(i).getCommentType().getName().equals("Required") ||
                    item.getComment(i).getCommentType().getName().equals("Recommended")) {
                result.add(item.getComment(i));
            }
        }
        return (Comment[]) result.toArray(new Comment[result.size()]);
    }

    /**
     * TODO: Document it
     *
     * @param mapping
     * @param form
     * @param request
     * @param reviewType
     * @return
     * @throws BaseException
     */
    private ActionForward saveGenericReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, String reviewType) throws BaseException {
        // FIXME: IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!
        // FIXME: Check the permissions here and everywhere,
        // as they where dropped from checkForCorrectReviewId(ActionMapping, HttpServletRequest, String)
        // FIXME: Also check current phase everywhere


        String permName;
        String phaseName;
        String scorecardTypeName;
        // Determine permission name and phase name from the review type
        if ("Screening".equals(reviewType)) {
            permName = Constants.PERFORM_SCREENING_PERM_NAME;
            phaseName = Constants.SCREENING_PHASE_NAME;
            scorecardTypeName = "Screening";
        } else if ("Review".equals(reviewType)) {
            permName = Constants.PERFORM_REVIEW_PERM_NAME;
            phaseName = Constants.REVIEW_PHASE_NAME;
            scorecardTypeName = "Review";
        } else {
            permName = Constants.PERFORM_APPROVAL_PERM_NAME;
            phaseName = Constants.APPROVAL_PHASE_NAME;
            scorecardTypeName = "Client Review";
        }

        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification = null;
        if (request.getParameter("rid") != null) {
            verification = checkForCorrectReviewId(mapping, request, permName);
        }
        if (verification == null && request.getParameter("sid") != null) {
            verification = checkForCorrectSubmissionId(mapping, request, permName);
        }

        // If neither "sid" nor "rid" was specified, return an action forward to the error page
        if (verification == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    permName, "Error.SubmissionAndReviewIdNotSpecified");
        }

        // If check was not successful, return an appropriate action forward
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request), project);
        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, phaseName);
        // Check that the phase in question is really active (open)
        if (phase == null) {
            if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)) {
                // Manager can edit review in any phase
                phase = ActionsHelper.getPhase(phases, false, phaseName);
            } else {
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, permName, "Error.IncorrectPhase");
            }
        }

        // Get "My" resource for the appropriate phase
        Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        // If no resource found for particular phase, try to find resource without phase assigned
        if (myResource == null) {
            myResource = ActionsHelper.getMyResourceForPhase(request, null);
        }

        // Retrieve the review to edit (if any)
        Review review = verification.getReview();
        Scorecard scorecardTemplate = null;

        if (review == null) {
            /*
             * Verify that the user is not trying to create review that already exists
             */

            // Retrieve a scorecard template for the appropriate phase
            scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(
                    ActionsHelper.createScorecardManager(request), phase);

            // Prepare filters
            Filter filterResource = new EqualToFilter("reviewer", new Long(myResource.getId()));
            Filter filterSubmission = new EqualToFilter("submission", new Long(verification.getSubmission().getId()));
            Filter filterScorecard = new EqualToFilter("scorecardType",
                    new Long(scorecardTemplate.getScorecardType().getId()));

            // Build the list of all filters that should be joined using AND operator
            List filters = new ArrayList();
            filters.add(filterResource);
            filters.add(filterSubmission);
            filters.add(filterScorecard);

            // Prepare final combined filter
            Filter filter = new AndFilter(filters);
            // Obtain an instance of Review Manager
            ReviewManager revMgr = ActionsHelper.createReviewManager(request);
            // Retrieve an array of reviews
            Review[] reviews = revMgr.searchReviews(filter, false);

            // Non-empty array of reviews indicates that
            // user is trying to create screening that already exists
            if (reviews.length != 0) {
                review = reviews[0];
                verification.setReview(review);
            }
        }
        if (review != null){
            // Obtain an instance of Scorecard Manager
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
            // Retrieve a scorecard template for the review
            scorecardTemplate = scrMgr.getScorecard(review.getScorecard());
        }

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase(scorecardTypeName)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    permName, "Error.ReviewTypeIncorrect");
        }

        boolean managerEdit = false;
        // Check if review has been committed
        if (review != null && review.isCommitted()) {
            // If user has a Manager role, put special flag to the request,
            // indicating that we need "Manager Edit"
            if(AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)) {
                request.setAttribute("managerEdit", Boolean.TRUE);
                managerEdit = true;
            } else {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                            Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.ReviewCommitted");
            }
        }

        // Get the form defined for this action
        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] answers = (String[]) reviewForm.get("answer");
        String[] replies = (String[]) reviewForm.get("comment");
        Long[] commentTypeIds = (Long[]) reviewForm.get("commentType");
        FormFile[] files = (FormFile[]) reviewForm.get("file");

        StrutsRequestParser parser = new StrutsRequestParser();

        for (int i = 0; i < files.length; ++i) {
            if (files[i] != null && files[i].getFileName().trim().length() != 0) {
                parser.AddFile(files[i]);
            }
        }

        // Obtain an instance of File Upload Manager
        FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);

        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
        UploadedFile[] uploadedFiles = uploadResult.getUploadedFiles("file");

        // Obtain an instance of review manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager(request);

        // Retrieve all comment types
        CommentType[] commentTypes = revMgr.getAllCommentTypes();
        // Retrieve all upload statuses
        UploadStatus[] allUploadStatuses = upMgr.getAllUploadStatuses();
        // Retrieve all upload types
        UploadType[] allUploadType = upMgr.getAllUploadTypes();

        int index = 0;
        int fileIdx = 0;
        int uploadedFileIdx = 0;

        // If the review hasn't been created yet
        if (review == null) {
            // Create a convenient review editor
            ReviewEditor reviewEditor =
                new ReviewEditor(Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

            // Iterate over the scorecard template's questions,
            // so items will be created for every question
            for (int i = 0; i < scorecardTemplate.getNumberOfGroups(); ++i) {
                Group group = scorecardTemplate.getGroup(i);
                for (int j = 0; j < group.getNumberOfSections(); ++j) {
                    Section section = group.getSection(j);
                    for (int k = 0; k < section.getNumberOfQuestions(); ++k) {
                        Question question = section.getQuestion(k);

                        // Create review item and comment for that item
                        Item item = new Item();
                        Comment comment = new Comment();

                        // Set required fields of the comment
                        comment.setAuthor(myResource.getId());
                        comment.setComment(replies[index]);
                        comment.setCommentType(
                                ActionsHelper.findCommentTypeById(commentTypes, commentTypeIds[index].longValue()));
                        // Add comment to the item
                        item.addComment(comment);

                        // Set required fields of the item
                        item.setAnswer(answers[index]);
                        item.setQuestion(question.getId());

                        if (question.isUploadDocument()) {
                            if (fileIdx < files.length && files[fileIdx] != null) {
                                Upload upload = new Upload();

                                upload.setOwner(myResource.getId());
                                upload.setProject(project.getId());
                                upload.setParameter(uploadedFiles[uploadedFileIdx++].getFileId());
                                upload.setUploadStatus(
                                        ActionsHelper.findUploadStatusByName(allUploadStatuses, "Active"));
                                upload.setUploadType(
                                        ActionsHelper.findUploadTypeByName(allUploadType, "Review Document"));

                                upMgr.createUpload(upload,
                                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

                                item.setDocument(new Long(upload.getId()));
                            }
                            ++fileIdx;
                        }

                        // Add item to the review
                        reviewEditor.addItem(item);

                        ++index;
                    }
                }
            }

            // Finally, set required fields of the review
            reviewEditor.setAuthor(myResource.getId());
            reviewEditor.setSubmission(verification.getSubmission().getId());
            reviewEditor.setScorecard(scorecardTemplate.getId());

            review = reviewEditor.getReview();
        } else {
            for (int i = 0; i < scorecardTemplate.getNumberOfGroups(); ++i) {
                Group group = scorecardTemplate.getGroup(i);
                for (int j = 0; j < group.getNumberOfSections(); ++j) {
                    Section section = group.getSection(j);
                    for (int k = 0; k < section.getNumberOfQuestions(); ++k, ++index) {
                        // Get an item and its comment
                        Item item = review.getItem(index);
                        Comment comment;
                        if (!managerEdit) {
                            comment = getItemReviewerComments(item)[0]; // TODO: Retrieve all comments
                            // Update the comment only if type or text have changed
                            if (comment.getCommentType().getId() != commentTypeIds[index].longValue() ||
                                    !comment.getComment().equals(replies[index])) {
                                comment.setComment(replies[index]);
                                comment.setCommentType(ActionsHelper.findCommentTypeById(
                                        commentTypes, commentTypeIds[index].longValue()));
                                // Update the author of the comment
                                comment.setAuthor(myResource.getId());
                            }
                        } else {
                            Comment[] managerComments = getItemManagerComments(item);
                            if (managerComments.length > 0) {
                                comment = managerComments[0]; // TODO: Retrieve all comments
                            } else {
                                comment = new Comment();
                                comment.setCommentType(
                                        ActionsHelper.findCommentTypeByName(commentTypes, "Manager Comment"));
                                item.addComment(comment);
                            }
                            comment.setAuthor(myResource.getId());
                            comment.setComment(replies[index]);

                            if (comment.getComment().trim().length() == 0) {
                                item.removeComment(comment);
                            }
                        }

                        // Update the answer
                        item.setAnswer(answers[index]);

                        if (!managerEdit && section.getQuestion(k).isUploadDocument()) {
                            if (fileIdx < files.length && files[fileIdx] != null &&
                                    files[fileIdx].getFileName().trim().length() != 0) {
                                Upload oldUpload = null;
                                if (item.getDocument() != null) {
                                    oldUpload = upMgr.getUpload(item.getDocument().longValue());
                                }

                                Upload upload = new Upload();

                                upload.setOwner(myResource.getId());
                                upload.setProject(oldUpload.getProject());
                                upload.setParameter(uploadedFiles[uploadedFileIdx++].getFileId());
                                upload.setUploadStatus(oldUpload.getUploadStatus());
                                upload.setUploadType(oldUpload.getUploadType());
                                oldUpload.setUploadStatus(
                                        ActionsHelper.findUploadStatusByName(allUploadStatuses, "Deleted"));

                                upMgr.updateUpload(oldUpload,
                                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                                upMgr.createUpload(upload,
                                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

                                item.setDocument(new Long(upload.getId()));
                            }
                            ++fileIdx;
                        }
                    }
                }
            }
        }

        // If the user has requested to complete the review
        if ("submit".equalsIgnoreCase(request.getParameter("save"))) {
            // TODO: Validate review here

            // Obtain an instance of CalculationManager
            CalculationManager scoreCalculator = new CalculationManager();
            // Compute scorecard's score
            review.setScore(new Float(scoreCalculator.getScore(scorecardTemplate, review)));

            // Set the completed status of the review
            review.setCommitted(true);
        } else if ("preview".equalsIgnoreCase(request.getParameter("save"))) {
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

            // Get the word "of" for Test Case type of question
            String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
            // Plase the string into the request as attribute
            request.setAttribute("wordOf", " "  + wordOf + " ");

            // Forward to preview page
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Determine which action should be performed -- creation or updating
        if (verification.getReview() == null) {
            revMgr.createReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        } else {
            revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }

        // Forward to project details page
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
    }

    /**
     * TODO: Document it.
     *
     * @param mapping
     * @param form
     * @param request
     * @param reviewType
     * @return
     * @throws BaseException
     */
    private ActionForward viewGenericReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            String reviewType) throws BaseException {
        String permName;
        String scorecardTypeName;
        // Determine permission name and phase name from the review type
        if ("Screening".equals(reviewType)) {
            permName = Constants.PERFORM_SCREENING_PERM_NAME;
            scorecardTypeName = "Screening";
        } else if ("Review".equals(reviewType)) {
            permName = Constants.PERFORM_REVIEW_PERM_NAME;
            scorecardTypeName = "Review";
        } else {
            permName = Constants.PERFORM_APPROVAL_PERM_NAME;
            scorecardTypeName = "Client Review";
        }

        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, permName);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for this review
        Scorecard scorecardTemplate = scrMgr.getScorecard(verification.getReview().getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase(scorecardTypeName)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    permName, "Error.ReviewTypeIncorrect");
        }
        // Make sure that the user is not trying to view unfinished review
        if (!verification.getReview().isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    permName, "Error.ReviewNotCommitted");
        } else {
            // If user has a Manager role, put special flag to the request,
            // indicating that we can edit the review
            if(AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)) {
                request.setAttribute("canEditScorecard", Boolean.TRUE);
            }
        }

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request), verification.getProject());
        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, null);

        boolean canPlaceAppeal = false;
        boolean canPlaceAppealResponse = false;
        // Check if user can place appeals or appeal responses
        if (phase.getPhaseType().getName().equals(Constants.APPEALS_PHASE_NAME) &&
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPEAL_PERM_NAME)) {
            // Can place appeal, put appropriate flag to request
            request.setAttribute("canPlaceAppeal", Boolean.TRUE);
            canPlaceAppeal = true;
        } else if (phase.getPhaseType().getName().equals(Constants.APPEALS_RESPONSE_PHASE_NAME) &&
                AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPEAL_RESP_PERM_NAME)) {
            // Can place response, put appropriate flag to request
            request.setAttribute("canPlaceAppealResponse", Boolean.TRUE);
            canPlaceAppealResponse = true;
        }

        if (canPlaceAppeal || canPlaceAppealResponse) {
            // Gather the appeal statuses
            String[] appealStatuses = new String[verification.getReview().getNumberOfItems()];
            for (int i = 0; i < appealStatuses.length; i++) {
                Comment appeal = getItemAppeal(verification.getReview().getItem(i).getAllComments());
                Comment response = getItemAppealResponse(verification.getReview().getItem(i).getAllComments());
                if (appeal != null && response == null) {
                    // TODO: Localize the strings
                    appealStatuses[i] = "Unresolved";
                } else if (appeal != null) {
                    appealStatuses[i] = "Resolved";
                } else {
                    appealStatuses[i] = "";
                }
            }
            // Place appeal statuses to request
            request.setAttribute("appealStatuses", appealStatuses);
        }

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Plase the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * TODO: Document it
     *
     * @param allComments
     * @return
     */
    private Comment getItemAppealResponse(Comment[] allComments) {
        for (int i = 0; i < allComments.length; i++) {
            if (allComments[i].getCommentType().getName().equals("Appeal Response")) {
                return allComments[i];
            }
        }
        return null;
    }

    /**
     * TODO: Document it
     *
     * @param allComments
     * @return
     */
    private Comment getItemAppeal(Comment[] allComments) {
        for (int i = 0; i < allComments.length; i++) {
            if (allComments[i].getCommentType().getName().equals("Appeal")) {
                return allComments[i];
            }
        }
        return null;
    }
}
