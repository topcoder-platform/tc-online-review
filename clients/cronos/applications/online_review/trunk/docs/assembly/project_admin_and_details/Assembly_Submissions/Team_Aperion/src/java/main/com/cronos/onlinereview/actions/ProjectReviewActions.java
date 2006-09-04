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
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.LazyValidatorForm;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.DefaultReviewManager;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.data.ReviewEditor;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseDateComparator;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_SCREENING_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Screening");

        // Retrieve a scorecard template for the Screening phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.SCREENING_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an instance of Review Manager
        ReviewManager revMgr = new DefaultReviewManager();

        // Retrieve all comment types first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                findCommentTypeByName(reviewCommentTypesAll, "Required"),
                findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);

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
        CommentType typeComment = findCommentTypeByName(reviewCommentTypesAll, "Comment");

        Arrays.fill(commentTypes, new Long(typeComment.getId()));
        reviewForm.set("commentType", commentTypes);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_SCREENING_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return produceErrorReport(mapping, request,
                    Constants.PERFORM_SCREENING_PERM_NAME, "Error.ReviewCommited");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Screening");

        // Retrieve a scorecard template for the Screening phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.SCREENING_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an instance of Review Manager
        ReviewManager revMgr = new DefaultReviewManager();

        // Retrieve all comment types at first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                findCommentTypeByName(reviewCommentTypesAll, "Required"),
                findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);

        // Prepare the arrays
        String[] answers = new String[review.getNumberOfItems()];
        String[] replies = new String[review.getNumberOfItems()];
        Long[] commentTypes = new Long[review.getNumberOfItems()];

        // Walk the items in the review setting appropriate values in the arrays
        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            Item item = review.getItem(i);
            Comment comment = item.getComment(0); // TODO: Retrieve all comments

            answers[i] = (String) item.getAnswer();
            replies[i] = comment.getComment();
            commentTypes[i] = new Long(comment.getCommentType().getId());
        }

        /*
         * Populate the form
         */

        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Populate form properties
        reviewForm.set("answer", answers);
        reviewForm.set("comment", replies);
        reviewForm.set("commentType", commentTypes);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = null;
        if (request.getParameter("rid") != null) {
            verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_SCREENING_PERM_NAME);
        }
        if (verification == null && request.getParameter("sid") != null) {
            verification = checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_SCREENING_PERM_NAME);
        }

        // If neither "sid" nor "rid" was specified, return an action forward to the error page
        if (verification == null) {
            return produceErrorReport(mapping, request,
                    Constants.PERFORM_SCREENING_PERM_NAME, "Error.SubmissionAndReviewIdNotSpecified");
        }

        // If check was not successful, return an appropriate action forward
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve the review to edit (if any)
        Review review = verification.getReview();

        // Verify that review has not been committed yet
        if (review != null && review.isCommitted()) {
            return produceErrorReport(mapping, request,
                    Constants.PERFORM_SCREENING_PERM_NAME, "Error.ReviewCommited");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Screening");

        // Retrieve a scorecard template for the Screening phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.SCREENING_PHASE_NAME);
        // Get an active phase for the project
        getActivePhase(verification, Constants.SCREENING_PHASE_NAME);
        // Retrieve a resource for the Screening phase
        Resource resource = getResourceForPhase(verification, AuthorizationHelper.getLoggedInUserId(request));
        // Get the form defined for this action
        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] answers = (String[]) reviewForm.get("answer");
        String[] replies = (String[]) reviewForm.get("comment");
        Long[] commentTypeIds = (Long[]) reviewForm.get("commentType");
        int index = 0;

        // Obtain an instance of review manager
        ReviewManager revMgr = new DefaultReviewManager();

        // Retrieve all comment types
        CommentType[] commentTypes = revMgr.getAllCommentTypes();

        // If the review hasn't been created yet
        if (review == null) {
            // Create a convenient review editor
            ReviewEditor reviewEditor = new ReviewEditor(AuthorizationHelper.getLoggenInUserHandle(request));

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
                        comment.setAuthor(resource.getId());
                        comment.setComment(replies[index]);
                        comment.setCommentType(findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
                        // Add comment to the item
                        item.addComment(comment);

                        // Set required fields of the item
                        item.setAnswer(answers[index]);
                        item.setQuestion(question.getId());
                        // Add item to the review
                        reviewEditor.addItem(item);

                        ++index;
                    }
                }
            }

            // Finally, set required fields of the review
            reviewEditor.setAuthor(resource.getId());
            reviewEditor.setSubmission(verification.getSubmission().getId());
            reviewEditor.setScorecard(scorecardTemplate.getId());

            review = reviewEditor.getReview();
        } else {
            // Iterate over items of the existing review that needs updating
            for (int i = 0; i < review.getNumberOfItems(); ++i) {
                // Get an item and its comment
                Item item = review.getItem(i);
                Comment comment = item.getComment(0); // TODO: Retrieve and update all comments

                // Update the comment only if type or text have changed
                if (comment.getCommentType().getId() != commentTypeIds[i].longValue() ||
                        !comment.getComment().equals(replies[i])) {
                    comment.setComment(replies[i]);
                    comment.setCommentType(findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
                    // Update the author of the comment
                    comment.setAuthor(resource.getId());
                }

                // Update the answer
                item.setAnswer(answers[i]);
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
            // Place scorecard template object into request as attribute
            request.setAttribute("scorecardTemplate", scorecardTemplate);
            // Place review object into request as attribute
            request.setAttribute("review", review);

            // Get the word "of" for Test Case type of question
            String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
            // Plase the string into the request as attribute
            request.setAttribute("wordOf", " "  + wordOf + " ");

            // Forward to preview page
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Determine which action should be performed -- creation or updating
        if (verification.getReview() == null) {
            revMgr.createReview(review, AuthorizationHelper.getLoggenInUserHandle(request));
        } else {
            revMgr.updateReview(review, AuthorizationHelper.getLoggenInUserHandle(request));
        }

        // Forward to project details page
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.VIEW_SCREENING_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Make sure that the user is not trying to view unfinished review
        if (!verification.getReview().isCommitted()) {
            return produceErrorReport(mapping, request, Constants.VIEW_SCREENING_PERM_NAME, "Error.ReviewNotCommited");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));

        // Retrieve a scorecard template for the Screening phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.SCREENING_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Plase the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Review");

        // Retrieve a scorecard template for the Review phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.REVIEW_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an instance of Review Manager
        ReviewManager revMgr = new DefaultReviewManager();

        // Retrieve all comment types first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                findCommentTypeByName(reviewCommentTypesAll, "Required"),
                findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);

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
        CommentType typeComment = findCommentTypeByName(reviewCommentTypesAll, "Comment");

        Arrays.fill(commentTypes, new Long(typeComment.getId()));
        reviewForm.set("commentType", commentTypes);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return produceErrorReport(mapping, request,
                    Constants.PERFORM_REVIEW_PERM_NAME, "Error.ReviewCommited");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Review");

        // Retrieve a scorecard template for the Review phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.REVIEW_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an instance of Review Manager
        ReviewManager revMgr = new DefaultReviewManager();

        // Retrieve all comment types at first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                findCommentTypeByName(reviewCommentTypesAll, "Required"),
                findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);

        // Prepare the arrays
        String[] answers = new String[review.getNumberOfItems()];
        String[] replies = new String[review.getNumberOfItems()];
        Long[] commentTypes = new Long[review.getNumberOfItems()];

        // Walk the items in the review setting appropriate values in the arrays
        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            Item item = review.getItem(i);
            Comment comment = item.getComment(0); // TODO: Retrieve all comments

            answers[i] = (String) item.getAnswer();
            replies[i] = comment.getComment();
            commentTypes[i] = new Long(comment.getCommentType().getId());
        }

        /*
         * Populate the form
         */

        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Populate form properties
        reviewForm.set("answer", answers);
        reviewForm.set("comment", replies);
        reviewForm.set("commentType", commentTypes);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = null;
        if (request.getParameter("rid") != null) {
            verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_REVIEW_PERM_NAME);
        }
        if (verification == null && request.getParameter("sid") != null) {
            verification = checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_REVIEW_PERM_NAME);
        }

        // If neither "sid" nor "rid" was specified, return an action forward to the error page
        if (verification == null) {
            return produceErrorReport(mapping, request,
                    Constants.PERFORM_REVIEW_PERM_NAME, "Error.SubmissionAndReviewIdNotSpecified");
        }

        // If check was not successful, return an appropriate action forward
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve the review to edit (if any)
        Review review = verification.getReview();

        // Verify that review has not been committed yet
        if (review != null && review.isCommitted()) {
            return produceErrorReport(mapping, request,
                    Constants.PERFORM_REVIEW_PERM_NAME, "Error.ReviewCommited");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Review");

        // Retrieve a scorecard template for the Review phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.REVIEW_PHASE_NAME);
        // Get an active phase for the project
        getActivePhase(verification, Constants.REVIEW_PHASE_NAME);
        // Retrieve a resource for the Screening phase
        Resource resource = getResourceForPhase(verification, AuthorizationHelper.getLoggedInUserId(request));
        // Get the form defined for this action
        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] answers = (String[]) reviewForm.get("answer");
        String[] replies = (String[]) reviewForm.get("comment");
        Long[] commentTypeIds = (Long[]) reviewForm.get("commentType");
        int index = 0;

        // Obtain an instance of review manager
        ReviewManager revMgr = new DefaultReviewManager();

        // Retrieve all comment types
        CommentType[] commentTypes = revMgr.getAllCommentTypes();

        // If the review hasn't been created yet
        if (review == null) {
            // Create a convenient review editor
            ReviewEditor reviewEditor = new ReviewEditor(AuthorizationHelper.getLoggenInUserHandle(request));

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
                        comment.setAuthor(resource.getId());
                        comment.setComment(replies[index]);
                        comment.setCommentType(findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
                        // Add comment to the item
                        item.addComment(comment);

                        // Set required fields of the item
                        item.setAnswer(answers[index]);
                        item.setQuestion(question.getId());
                        // Add item to the review
                        reviewEditor.addItem(item);

                        ++index;
                    }
                }
            }

            // Finally, set required fields of the review
            reviewEditor.setAuthor(resource.getId());
            reviewEditor.setSubmission(verification.getSubmission().getId());
            reviewEditor.setScorecard(scorecardTemplate.getId());

            review = reviewEditor.getReview();
        } else {
            // Iterate over items of the existing review that needs updating
            for (int i = 0; i < review.getNumberOfItems(); ++i) {
                // Get an item and its comment
                Item item = review.getItem(i);
                Comment comment = item.getComment(0); // TODO: Retrieve and update all comments

                // Update the comment only if type or text have changed
                if (comment.getCommentType().getId() != commentTypeIds[i].longValue() ||
                        !comment.getComment().equals(replies[i])) {
                    comment.setComment(replies[i]);
                    comment.setCommentType(findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
                    // Update the author of the comment
                    comment.setAuthor(resource.getId());
                }

                // Update the answer
                item.setAnswer(answers[i]);
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
            // Place scorecard template object into request as attribute
            request.setAttribute("scorecardTemplate", scorecardTemplate);
            // Place review object into request as attribute
            request.setAttribute("review", review);

            // Get the word "of" for Test Case type of question
            String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
            // Plase the string into the request as attribute
            request.setAttribute("wordOf", " "  + wordOf + " ");

            // Forward to preview page
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Determine which action should be performed -- creation or updating
        if (verification.getReview() == null) {
            revMgr.createReview(review, AuthorizationHelper.getLoggenInUserHandle(request));
        } else {
            revMgr.updateReview(review, AuthorizationHelper.getLoggenInUserHandle(request));
        }

        // Forward to project details page
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.VIEW_ALL_REVIEWS_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Make sure that the user is not trying to view unfinished review
        if (!verification.getReview().isCommitted()) {
            return produceErrorReport(mapping, request,
                    Constants.VIEW_ALL_REVIEWS_PERM_NAME, "Error.ReviewNotCommited");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));

        // Retrieve a scorecard template for the Review phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.REVIEW_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Plase the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
     * this assembly, which is supposed to gather needed information (agrregation and review
     * scorecard template) and present it to editAggregation.jsp page, which will fill the required
     * fields and post them to the &quot;Save Aggrgation&quot; action. The action implemented by
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREGATION_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));

        // Retrieve a scorecard template for the Review phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.REVIEW_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Plase the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * TODO: Write sensible description for method saveAggregation here
     *
     * @return TODO: Write sensible description of return value for method saveAggregation
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward saveAggregation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method saveAggregation here
        return null;
    }

    /**
     * TODO: Write sensible description for method viewAggregation here
     *
     * @return TODO: Write sensible description of return value for method viewAggregation
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward viewAggregation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method viewAggregation here
        return null;
    }

    /**
     * TODO: Write sensible description for method editAggregationReview here
     *
     * @return TODO: Write sensible description of return value for method editAggregationReview
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward editAggregationReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method editAggregationReview here
        return null;
    }

    /**
     * TODO: Write sensible description for method saveAggregationReview here
     *
     * @return TODO: Write sensible description of return value for method saveAggregationReview
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward saveAggregationReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method saveAggregationReview here
        return null;
    }

    /**
     * TODO: Write sensible description for method viewAggregationReview here
     *
     * @return TODO: Write sensible description of return value for method viewAggregationReview
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward viewAggregationReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method viewAggregationReview here
        return null;
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
     * TODO: Write sensible description for method editFinalReview here
     *
     * @return TODO: Write sensible description of return value for method editFinalReview
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward editFinalReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method editFinalReview here
        return null;
    }

    /**
     * TODO: Write sensible description for method saveFinalReview here
     *
     * @return TODO: Write sensible description of return value for method saveFinalReview
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward saveFinalReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method saveFinalReview here
        return null;
    }

    /**
     * TODO: Write sensible description for method viewFinalReview here
     *
     * @return TODO: Write sensible description of return value for method viewFinalReview
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward viewFinalReview(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method viewFinalReview here
        return null;
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_APPROVAL_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Approval");

        // Retrieve a scorecard template for the Approval phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.APPROVAL_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an instance of Review Manager
        ReviewManager revMgr = new DefaultReviewManager();

        // Retrieve all comment types first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                findCommentTypeByName(reviewCommentTypesAll, "Required"),
                findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);

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
        CommentType typeComment = findCommentTypeByName(reviewCommentTypesAll, "Comment");

        Arrays.fill(commentTypes, new Long(typeComment.getId()));
        reviewForm.set("commentType", commentTypes);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_APPROVAL_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return produceErrorReport(mapping, request,
                    Constants.PERFORM_APPROVAL_PERM_NAME, "Error.ReviewCommited");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Approval");

        // Retrieve a scorecard template for the Approval phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.APPROVAL_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an instance of Review Manager
        ReviewManager revMgr = new DefaultReviewManager();

        // Retrieve all comment types at first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                findCommentTypeByName(reviewCommentTypesAll, "Required"),
                findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);

        // Prepare the arrays
        String[] answers = new String[review.getNumberOfItems()];
        String[] replies = new String[review.getNumberOfItems()];
        Long[] commentTypes = new Long[review.getNumberOfItems()];

        // Walk the items in the review setting appropriate values in the arrays
        for (int i = 0; i < review.getNumberOfItems(); ++i) {
            Item item = review.getItem(i);
            Comment comment = item.getComment(0); // TODO: Retrieve all comments

            answers[i] = (String) item.getAnswer();
            replies[i] = comment.getComment();
            commentTypes[i] = new Long(comment.getCommentType().getId());
        }

        /*
         * Populate the form
         */

        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Populate form properties
        reviewForm.set("answer", answers);
        reviewForm.set("comment", replies);
        reviewForm.set("commentType", commentTypes);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = null;
        if (request.getParameter("rid") != null) {
            verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_APPROVAL_PERM_NAME);
        }
        if (verification == null && request.getParameter("sid") != null) {
            verification = checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_APPROVAL_PERM_NAME);
        }

        // If neither "sid" nor "rid" was specified, return an action forward to the error page
        if (verification == null) {
            return produceErrorReport(mapping, request,
                    Constants.PERFORM_APPROVAL_PERM_NAME, "Error.SubmissionAndReviewIdNotSpecified");
        }

        // If check was not successful, return an appropriate action forward
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve the review to edit (if any)
        Review review = verification.getReview();

        // Verify that review has not been committed yet
        if (review != null && review.isCommitted()) {
            return produceErrorReport(mapping, request,
                    Constants.PERFORM_APPROVAL_PERM_NAME, "Error.ReviewCommited");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Approval");

        // Retrieve a scorecard template for the Approval phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.APPROVAL_PHASE_NAME);
        // Get an active phase for the project
        getActivePhase(verification, Constants.APPROVAL_PHASE_NAME);
        // Retrieve a resource for the Screening phase
        Resource resource = getResourceForPhase(verification, AuthorizationHelper.getLoggedInUserId(request));
        // Get the form defined for this action
        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] answers = (String[]) reviewForm.get("answer");
        String[] replies = (String[]) reviewForm.get("comment");
        Long[] commentTypeIds = (Long[]) reviewForm.get("commentType");
        int index = 0;

        // Obtain an instance of review manager
        ReviewManager revMgr = new DefaultReviewManager();

        // Retrieve all comment types
        CommentType[] commentTypes = revMgr.getAllCommentTypes();

        // If the review hasn't been created yet
        if (review == null) {
            // Create a convenient review editor
            ReviewEditor reviewEditor = new ReviewEditor(AuthorizationHelper.getLoggenInUserHandle(request));

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
                        comment.setAuthor(resource.getId());
                        comment.setComment(replies[index]);
                        comment.setCommentType(findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
                        // Add comment to the item
                        item.addComment(comment);

                        // Set required fields of the item
                        item.setAnswer(answers[index]);
                        item.setQuestion(question.getId());
                        // Add item to the review
                        reviewEditor.addItem(item);

                        ++index;
                    }
                }
            }

            // Finally, set required fields of the review
            reviewEditor.setAuthor(resource.getId());
            reviewEditor.setSubmission(verification.getSubmission().getId());
            reviewEditor.setScorecard(scorecardTemplate.getId());

            review = reviewEditor.getReview();
        } else {
            // Iterate over items of the existing review that needs updating
            for (int i = 0; i < review.getNumberOfItems(); ++i) {
                // Get an item and its comment
                Item item = review.getItem(i);
                Comment comment = item.getComment(0); // TODO: Retrieve and update all comments

                // Update the comment only if type or text have changed
                if (comment.getCommentType().getId() != commentTypeIds[i].longValue() ||
                        !comment.getComment().equals(replies[i])) {
                    comment.setComment(replies[i]);
                    comment.setCommentType(findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
                    // Update the author of the comment
                    comment.setAuthor(resource.getId());
                }

                // Update the answer
                item.setAnswer(answers[i]);
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
            // Place scorecard template object into request as attribute
            request.setAttribute("scorecardTemplate", scorecardTemplate);
            // Place review object into request as attribute
            request.setAttribute("review", review);

            // Get the word "of" for Test Case type of question
            String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
            // Plase the string into the request as attribute
            request.setAttribute("wordOf", " "  + wordOf + " ");

            // Forward to preview page
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Determine which action should be performed -- creation or updating
        if (verification.getReview() == null) {
            revMgr.createReview(review, AuthorizationHelper.getLoggenInUserHandle(request));
        } else {
            revMgr.updateReview(review, AuthorizationHelper.getLoggenInUserHandle(request));
        }

        // Forward to project details page
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.VIEW_APPROVAL_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Make sure that the user is not trying to view unfinished review
        if (!verification.getReview().isCommitted()) {
            return produceErrorReport(mapping, request, Constants.VIEW_APPROVAL_PERM_NAME, "Error.ReviewNotCommited");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));

        // Retrieve a scorecard template for the Approval phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.APPROVAL_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Plase the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification =
                checkForCorrectSubmissionId(mapping, request, Constants.VIEW_COMPOS_SCORECARD_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));

        // Retrieve a scorecard template for the Review phase
        Scorecard scorecardTemplate = getScorecardTemplateForPhase(verification, Constants.REVIEW_PHASE_NAME);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        Filter filterSubmission = new EqualToFilter("submission", new Long(verification.getSubmission().getId()));
        // Obtain an instance of Review Manager
        ReviewManager reviewManager = new DefaultReviewManager();

        // Retrieve the list of completed reviews for the specified submission
        Review[] reviews =  reviewManager.searchReviews(filterSubmission, true);

        // TODO: Probably check the number of returned reviews

        // Store them in the request
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
            result.setForward(produceErrorReport(mapping, request, permission, "Error.SubmissionIdNotSpecified"));
            // Return the result of the check
            return result;
        }

        long sid;

        try {
            // Try to convert specified sid parameter to its integer representation
            sid = Long.parseLong(sidParam, 10);
        } catch (NumberFormatException e) {
            result.setForward(produceErrorReport(mapping, request, permission, "Error.SubmissionNotFound"));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Deliverable Manager
        UploadManager upMgr = ActionsHelper.createUploadManager();
        // Store UploadManager object in the result bean
        result.setUploadManager(upMgr);

        // Get Submission by its id
        Submission submission = upMgr.getSubmission(sid);
        // Verify that submission with specified ID exists
        if (submission == null) {
            result.setForward(produceErrorReport(mapping, request, permission, "Error.SubmissionNotFound"));
            // Return the result of the check
            return result;
        }

        // Store Submission object in the result bean
        result.setSubmission(submission);
        // Place the id of the submission as attribute in the request
        request.setAttribute("sid", new Long(sid));

        // Retrieve the project following submission's infromation chain
        Project project = getProjectFromSubmission(submission);
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
                result.setForward(produceErrorReport(mapping, request, permission, "Error.NoPermission"));
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
            result.setForward(produceErrorReport(mapping, request, permission, "Error.ReviewIdNotSpecified"));
            // Return the result of the check
            return result;
        }

        long rid;

        try {
            // Try to convert specified rid parameter to its integer representation
            rid = Long.parseLong(ridParam, 10);
        } catch (NumberFormatException e) {
            result.setForward(produceErrorReport(mapping, request, permission, "Error.ReviewNotFound"));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Review Manager
        ReviewManager revMgr = new DefaultReviewManager();

        // Get Review by its id
        Review review = revMgr.getReview(rid);
        // Verify that review with specified ID exists
        if (review == null) {
            result.setForward(produceErrorReport(mapping, request, permission, "Error.ReviewNotFound"));
            // Return the result of the check
            return result;
        }

        // Store Review object in the result bean
        result.setReview(review);
        // Place the review object as attribute in the request
        request.setAttribute("review", review);

        // Obtain an instance of Deliverable Manager
        UploadManager upMgr = ActionsHelper.createUploadManager();
        // Get Submission by its id
        Submission submission = upMgr.getSubmission(review.getSubmission());

        // Store Submission object in the result bean
        result.setSubmission(submission);
        // Place the id of the submission as attribute in the request
        request.setAttribute("sid", new Long(submission.getId()));

        // Retrieve the project following submission's infromation chain
        Project project = getProjectFromSubmission(submission);
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
                result.setForward(produceErrorReport(mapping, request, permission, "Error.NoPermission"));
                // Return the result of the check
                return result;
            }
        }

        // Return the result of the check
        return result;
    }

    /**
     * This static method retrieves the project the submission specified by <code>submission</code>
     * parameter was made for.
     *
     * @return a retrieved project.
     * @param submission
     *            a submission to retrieve the project for.
     * @throws BaseException
     *             if any error occurs.
     */
    private static Project getProjectFromSubmission(Submission submission) throws BaseException {
        // Get an upload for this submission
        Upload upload = submission.getUpload();

        // Obtain an instance of Project Manager
        ProjectManager projMgr = new ProjectManagerImpl();
        // Get Project by its id
        Project project = projMgr.getProject(upload.getProject());

        return project;
    }

    /**
     * This method places certain attributes into the request and returns a forard to the error
     * page.
     *
     * @return an action forward to the appropriate error page.
     * @param mapping
     *            action mapping.
     * @param request
     *            the http request.
     * @param permission
     *            permission to check against, or <code>null</code> if no check is required.
     * @param reasonKey
     *            a key in Message resources which the reason of the error is stored under.
     * @throws BaseException
     *             if any error occurs.
     */
    private ActionForward produceErrorReport(ActionMapping mapping,
            HttpServletRequest request, String permission, String reasonKey)
        throws BaseException{
        // Gather roles, so tabs will be displayed,
        // but only do this if roles haven't been gathered yet
        if (request.getAttribute("roles") == null) {
            AuthorizationHelper.gatherUserRoles(request);
        }

        // Message Resources to be used for loading error messages from
        MessageResources messages = getResources(request);

        // Place error title into request
        if (permission == null) {
            request.setAttribute("errorTitle", messages.getMessage("Error.Title.General"));
        } else {
            request.setAttribute("errorTitle", messages.getMessage("Error.Title." + permission.replaceAll(" ", "")));
        }
        // Place error message (reason) into request
        request.setAttribute("errorMessage", messages.getMessage(reasonKey));
        // Find appropriate forward and return it
        return mapping.findForward(Constants.USER_ERRROR_FORWARD_NAME);
    }

    /**
     * This static method finds the phase with the specified name, and returns a Scorecard template
     * associated with that phase.
     *
     * @return found Scorecard template, or <code>null</code> if no phase with the specified name
     *         has been found, or the phase found does not have a scorecard assigned to it.
     * @param verification
     *            a bean containing project to search a phase in. This bean will also have its
     *            scorecard template and phase assigneded upon return from this method.
     * @param phaseName
     *            the phase name to find a scorecard assigned to.
     * @throws BaseException
     *             if any error occurs.
     */
    private static Scorecard getScorecardTemplateForPhase(CorrectnessCheckResult verification, String phaseName)
        throws BaseException {
        // Retrieve an array of all phases from the bean
        Phase[] phases = verification.getPhases();
        if (phases == null) {
            // Obtain an instance of Phase Manager
            PhaseManager phaseMgr = new DefaultPhaseManager("com.topcoder.management.phase");

            // Get all phases for the current project
            com.topcoder.project.phases.Project phProj = phaseMgr.getPhases(verification.getProject().getId());
            phases = phProj.getAllPhases(new PhaseDateComparator());
            // Store array of phases inside the bean
            verification.setPhases(phases);
        }

        // Iterate over the array of phases
        for (int i = 0; i < phases.length; ++i) {
            // Get a phase for the current iteration
            Phase phase = phases[i];
            // If the name of the current phase doesn't equal the value
            // provided in phaseName parameter, continue the search
            if (!phase.getPhaseType().getName().equalsIgnoreCase(phaseName)) {
                continue;
            }

            // Get an ID of scorecard template associated with this phase
            String strScorecardId = (String)phase.getAttribute("Scorecard ID");
            // If no scorecard template is assigned to the current phase,
            // try to find other phase with the same name
            if (strScorecardId == null || strScorecardId.trim().length() == 0) {
                continue;
            }

            // Convert the ID from text to its numeric representation
            long scorecardId = Long.parseLong(strScorecardId, 10);
            // Obtain an instance of Scorecard Manager
            ScorecardManager scMgr = new ScorecardManagerImpl();
            // Get the Scorecard template by its ID
            verification.setScorecard(scMgr.getScorecard(scorecardId));
            // Return it
            return verification.getScorecard();
        }

        return null;
    }

    /**
     * This static method returns the active phase for a project. If there is more than one active
     * phase for the project at some stage, the <code>phaseName</code> parameter can be used to
     * specify the name of the phase that is expected to be active
     *
     * @return the active phase, or <code>null</code> if there is no active phase with specified
     *         name or there is some error in the database.
     * @param verification
     *            a bean containing additional information such as the project which an active phase
     *            should be retrieved for. On return this bean will contain the list of all phases
     *            for the project, as well as the active phase if such phase has been found.
     * @param phaseName
     *            Optional name of the phase to search for if there is a possiblity that more than
     *            one phase is active.
     * @throws BaseException
     *             if any error occurs.
     */
    private static Phase getActivePhase(CorrectnessCheckResult verification, String phaseName)
        throws BaseException {
        // Retrieve an array of all phases from the bean
        Phase[] phases = verification.getPhases();
        if (phases == null) {
            // Obtain an instance of Phase Manager
            PhaseManager phaseMgr = new DefaultPhaseManager("com.topcoder.management.phase");

            // Get all phases for the current project
            com.topcoder.project.phases.Project phProj = phaseMgr.getPhases(verification.getProject().getId());
            phases = phProj.getAllPhases(new PhaseDateComparator());
            // Store array of phases inside the bean
            verification.setPhases(phases);
        }

        for (int i = 0; i < phases.length; ++i) {
            Phase phase = phases[i];
            String strPhaseStatus = phase.getPhaseStatus().getName();
            // Skip already closed phase
            if (strPhaseStatus.equalsIgnoreCase("Closed")) {
                continue;
            }
            // There is no active phase with specified name, or there is an error in database
/* TODO: Uncomment this when phases have correct status
            if (strPhaseStatus.equalsIgnoreCase("Sheduled")) {
                return null;
            }
*/
            // If the name of the phase was not specified,
            // or the name of the current phase equals desired name, return this phase
            if (phaseName == null || phaseName.equalsIgnoreCase(phase.getPhaseType().getName())) {
                // Store the phase found into the bean
                verification.setActivePhase(phase);
                // Return it
                return phase;
            }
        }
        return null;
    }

    private static Resource getResourceForPhase(CorrectnessCheckResult verification, long extUserId)
        throws BaseException {
        // Prepare filter to select resource by the External ID of the user
        Filter filterExtIDname = ResourceFilterBuilder.createExtensionPropertyNameFilter("External Reference ID");
        Filter filterExtIDvalue =
                ResourceFilterBuilder.createExtensionPropertyValueFilter(String.valueOf(extUserId));
        Filter filterExtID = new AndFilter(filterExtIDname, filterExtIDvalue);
        // Prepare filter to select resource by project ID
        Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(verification.getProject().getId());
        // Prepare filterr to select resource by phase ID
        Filter filterPhase = ResourceFilterBuilder.createPhaseIdFilter(verification.getActivePhase().getId());

        // The list that will contain all the individual
        // filters that will later be combined by the AndFilter
        List filters = new ArrayList();

        // Add individual filters to list
        filters.add(filterExtID);
        filters.add(filterProject);
        filters.add(filterPhase);

        // Create the main filter for this resource-retrieveal operaion
        Filter filter = new AndFilter(filters);
        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager();
        // Perform search for the resource
        Resource[] resources = resMgr.searchResources(filter);

        return (resources.length != 0) ? resources[0] : null;
    }

    /**
     * This static method helps to find certain comment type in the provided array of comment types
     * by its name.
     *
     * @return found comment type, or <code>null</code> if the comment type with specified name
     *         was not found.
     * @param commentTypes
     *            the array of comment types.
     * @param name
     *            the name of the needed comment type.
     */
    private static CommentType findCommentTypeByName(CommentType[] commentTypes, String name) {
        for (int i = 0; i < commentTypes.length; ++i) {
            if (commentTypes[i].getName().equalsIgnoreCase(name)) {
                return commentTypes[i];
            }
        }
        return null;
    }

    /**
     * This static method helps to find certain comment type in the provided array of comment types
     * by its id.
     *
     * @return found comment type, or <code>null</code> if the comment type with specified id was
     *         not found.
     * @param commentTypes
     *            the array of comment types.
     * @param id
     *            the id of the needed comment type.
     */
    private static CommentType findCommentTypeById(CommentType[] commentTypes, long id) {
        for (int i = 0; i < commentTypes.length; ++i) {
            if (commentTypes[i].getId() == id) {
                return commentTypes[i];
            }
        }
        return null;
    }
}
