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
import org.apache.struts.validator.LazyValidatorForm;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_SCREENING_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request), project);
        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.SCREENING_PHASE_NAME);
        // Get "My" resource for the Screening phase
        Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Retrieve a scorecard template for the Screening phase
        Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(
                ActionsHelper.createScorecardManager(request), phase);

        /*
         * Verify that the user is not trying to create screening that already exists
         */

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
            // Forward to Edit Sceeening page
            return ActionsHelper.cloneForwardAndAppendToPath(
                    mapping.findForward(Constants.EDIT_FORWARD_NAME), "&rid=" + reviews[0].getId());
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, project, getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Screening");
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Retrieve all comment types first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Required"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

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
        CommentType typeComment = ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Comment");

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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_SCREENING_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scorMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scorMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Screening")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_SCREENING_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_SCREENING_PERM_NAME, "Error.ReviewCommitted");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Screening");
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);

        // Retrieve all comment types at first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Required"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification = null;
        if (request.getParameter("rid") != null) {
            verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_SCREENING_PERM_NAME);
        }
        if (verification == null && request.getParameter("sid") != null) {
            verification = checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_SCREENING_PERM_NAME);
        }

        // If neither "sid" nor "rid" was specified, return an action forward to the error page
        if (verification == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_SCREENING_PERM_NAME, "Error.SubmissionAndReviewIdNotSpecified");
        }

        // If check was not successful, return an appropriate action forward
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve the review to edit (if any)
        Review review = verification.getReview();
        Scorecard scorecardTemplate = null;

        if (review == null) {
            /*
             * Verify that the user is not trying to create screening that already exists
             */

            // Get current project
            Project project = verification.getProject();

            // Get an array of all phases for the project
            Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request), project);
            // Get active (current) phase
            Phase phase = ActionsHelper.getPhase(phases, true, Constants.SCREENING_PHASE_NAME);
            // Get "My" resource for the Screening phase
            Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
            // Retrieve a scorecard template for the Screening phase
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
        } else {
            // Obtain an instance of Scorecard Manager
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
            // Retrieve a scorecard template for the review
            scorecardTemplate = scrMgr.getScorecard(review.getScorecard());
        }

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Screening")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_SCREENING_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        // Verify that review has not been committed yet
        if (review != null && review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_SCREENING_PERM_NAME, "Error.ReviewCommitted");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Screening");

        // Get an array of phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(request), verification.getProject());
        // Get an active phase for the project
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.SCREENING_PHASE_NAME);
        // Retrieve a resource for the Screening phase
        Resource resource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Get the form defined for this action
        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] answers = (String[]) reviewForm.get("answer");
        String[] replies = (String[]) reviewForm.get("comment");
        Long[] commentTypeIds = (Long[]) reviewForm.get("commentType");
        int index = 0;

        // Obtain an instance of review manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);

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
                        comment.setCommentType(
                                ActionsHelper.findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
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
                    comment.setCommentType(
                            ActionsHelper.findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.VIEW_SCREENING_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for this review
        Scorecard scorecardTemplate = scrMgr.getScorecard(verification.getReview().getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Screening")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_SCREENING_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        // Make sure that the user is not trying to view unfinished review
        if (!verification.getReview().isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_SCREENING_PERM_NAME, "Error.ReviewNotCommitted");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Place the string into the request as attribute
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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request), project);
        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.REVIEW_PHASE_NAME);
        // Get "My" resource for the Review phase
        Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Retrieve a scorecard template for the Review phase
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
        // user is trying to create review that already exists
        if (reviews.length != 0) {
            // Forward to Edit Review page
            return ActionsHelper.cloneForwardAndAppendToPath(
                    mapping.findForward(Constants.EDIT_FORWARD_NAME), "&rid=" + reviews[0].getId());
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Review");
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Retrieve all comment types first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Required"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

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
        CommentType typeComment = ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Comment");

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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scorMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scorMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_REVIEW_PERM_NAME, "Error.ReviewCommitted");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Review");
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);

        // Retrieve all comment types at first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Required"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification = null;
        if (request.getParameter("rid") != null) {
            verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_REVIEW_PERM_NAME);
        }
        if (verification == null && request.getParameter("sid") != null) {
            verification = checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_REVIEW_PERM_NAME);
        }

        // If neither "sid" nor "rid" was specified, return an action forward to the error page
        if (verification == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_REVIEW_PERM_NAME, "Error.SubmissionAndReviewIdNotSpecified");
        }

        // If check was not successful, return an appropriate action forward
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve the review to save (if any)
        Review review = verification.getReview();
        Scorecard scorecardTemplate = null;

        if (review == null) {
            /*
             * Verify that the user is not trying to create review that already exists
             */

            // Get current project
            Project project = verification.getProject();

            // Get an array of all phases for the project
            Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request), project);
            // Get active (current) phase
            Phase phase = ActionsHelper.getPhase(phases, true, Constants.REVIEW_PHASE_NAME);
            // Get "My" resource for the Review phase
            Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
            // Retrieve a scorecard template for the Review phase
            scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(
                    ActionsHelper.createScorecardManager(request), phase);

            // Prepare filters
            Filter filterResource = new EqualToFilter("reviewer", new Long(myResource.getId()));
            Filter filterSubmission = new EqualToFilter("submission", new Long(verification.getSubmission().getId()));
            Filter filterScorecard = new EqualToFilter("scorecardType",
                    new Long(scorecardTemplate.getScorecardType().getId()));

            // Prepare final combined filter
            Filter filter = new AndFilter(Arrays.asList(new Filter[]
                    {filterResource, filterSubmission, filterScorecard}));
            // Obtain an instance of Review Manager
            ReviewManager revMgr = ActionsHelper.createReviewManager(request);
            // Retrieve an array of reviews
            Review[] reviews = revMgr.searchReviews(filter, false);

            // Non-empty array of reviews indicates that
            // user is trying to create review that already exists
            if (reviews.length != 0) {
                review = reviews[0];
                verification.setReview(review);
            }
        } else {
            // Obtain an instance of Scorecard Manager
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
            // Retrieve a scorecard template for the review
            scorecardTemplate = scrMgr.getScorecard(review.getScorecard());
        }

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        // Verify that review has not been committed yet
        if (review != null && review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_REVIEW_PERM_NAME, "Error.ReviewCommitted");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Review");

        // Get an array of phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(request), verification.getProject());
        // Get an active phase for the project
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.REVIEW_PHASE_NAME);
        // Retrieve a resource for the Review phase
        Resource resource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Get the form defined for this action
        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] answers = (String[]) reviewForm.get("answer");
        String[] replies = (String[]) reviewForm.get("comment");
        Long[] commentTypeIds = (Long[]) reviewForm.get("commentType");
        int index = 0;

        // Obtain an instance of review manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);

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
                        comment.setCommentType(
                                ActionsHelper.findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
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
                    comment.setCommentType(
                            ActionsHelper.findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.VIEW_ALL_REVIEWS_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for this review
        Scorecard scorecardTemplate = scrMgr.getScorecard(verification.getReview().getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_ALL_REVIEWS_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        // Make sure that the user is not trying to view unfinished review
        if (!verification.getReview().isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_ALL_REVIEWS_PERM_NAME, "Error.ReviewNotCommitted");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Place the string into the request as attribute
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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREGATION_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecad Manager
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

        // Retrieve current project
        Project project = verification.getProject();

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, project, getResources(request));
        // Retrieve some basic aggregation info and place it into request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");
        // Place a string that represents "my" current role(s) into the request
        request.setAttribute("myRole", ActionsHelper.determineRolesForResources(getResources(request), myResources));

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
                    if ("Rejected".equalsIgnoreCase(aggregFunction)) {
                        aggregateFunctions[commentIndex] = "Rejected";
                    } else if ("Accepted".equalsIgnoreCase(aggregFunction)) {
                        aggregateFunctions[commentIndex] = "Accepted";
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

        // Obtain an instance of Scorecad Manager
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
        CommentType[] commentTypes = revMgr.getAllCommentTypes();

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
                    comment.setCommentType(
                            ActionsHelper.findCommentTypeById(commentTypes, responseTypeIds[commentIndex].longValue()));
                    ++commentIndex;
                }
                if (typeName.equalsIgnoreCase("Aggregation Comment")) {
                    aggregatorComment = comment;
                }
            }

            if (aggregatorComment == null) {
                aggregatorComment = new Comment();
                aggregatorComment.setCommentType(
                        ActionsHelper.findCommentTypeByName(commentTypes, "Aggregation Comment"));
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
        } else if ("preview".equalsIgnoreCase(request.getParameter("save"))) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            // Retrieve some basic aggregation info and place it into request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate);
            // Place scorecard template object into request as attribute
            request.setAttribute("scorecardTemplate", scorecardTemplate);
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

        // Obtain an instance of Scorecad Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREGATION_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        // Make sure that the user is not trying to view unfinished review
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREGATION_PERM_NAME, "Error.ReviewNotCommitted");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Retrieve some basic aggregation info and place it into request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate);
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");
        // Place a string that represents "my" current role(s) into the request
        request.setAttribute("myRole", ActionsHelper.determineRolesForResources(getResources(request), myResources));

        // Get the word "of" for Test Case type of question
        String wordOf = getResources(request).getMessage("editReview.Question.Response.TestCase.of");
        // Place the string into the request as attribute
        request.setAttribute("wordOf", " "  + wordOf + " ");

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_APPROVAL_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request), project);
        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.APPROVAL_PHASE_NAME);
        // Get "My" resource for the Approval phase
        Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Retrieve a scorecard template for the Approval phase
        Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(
                ActionsHelper.createScorecardManager(request), phase);

        /*
         * Verify that the user is not trying to create approval that already exists
         */

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
        // user is trying to create approval that already exists
        if (reviews.length != 0) {
            // Forward to Edit Approval page
            return ActionsHelper.cloneForwardAndAppendToPath(
                    mapping.findForward(Constants.EDIT_FORWARD_NAME), "&rid=" + reviews[0].getId());
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Approval");
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Retrieve all comment types first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Required"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

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
        CommentType typeComment = ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Comment");

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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_APPROVAL_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scorMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scorMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Client Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_APPROVAL_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_APPROVAL_PERM_NAME, "Error.ReviewCommitted");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Approval");
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);

        // Retrieve all comment types at first
        CommentType reviewCommentTypesAll[] = revMgr.getAllCommentTypes();
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Comment"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Required"),
                ActionsHelper.findCommentTypeByName(reviewCommentTypesAll, "Recommended") };

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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification = null;
        if (request.getParameter("rid") != null) {
            verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_APPROVAL_PERM_NAME);
        }
        if (verification == null && request.getParameter("sid") != null) {
            verification = checkForCorrectSubmissionId(mapping, request, Constants.PERFORM_APPROVAL_PERM_NAME);
        }

        // If neither "sid" nor "rid" was specified, return an action forward to the error page
        if (verification == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_APPROVAL_PERM_NAME, "Error.SubmissionAndReviewIdNotSpecified");
        }

        // If check was not successful, return an appropriate action forward
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve the review to edit (if any)
        Review review = verification.getReview();
        Scorecard scorecardTemplate = null;

        if (review == null) {
            /*
             * Verify that the user is not trying to create approval that already exists
             */

            // Get current project
            Project project = verification.getProject();

            // Get an array of all phases for the project
            Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request), project);
            // Get active (current) phase
            Phase phase = ActionsHelper.getPhase(phases, true, Constants.APPROVAL_PHASE_NAME);
            // Get "My" resource for the Approval phase
            Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
            // Retrieve a scorecard template for the Approval phase
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
            // user is trying to create approval that already exists
            if (reviews.length != 0) {
                review = reviews[0];
                verification.setReview(review);
            }
        } else {
            // Obtain an instance of Scorecard Manager
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
            // Retrieve a scorecard template for the review
            scorecardTemplate = scrMgr.getScorecard(review.getScorecard());
        }

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Client Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_APPROVAL_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        // Verify that review has not been committed yet
        if (review != null && review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_APPROVAL_PERM_NAME, "Error.ReviewCommitted");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place the type of the review into the request
        request.setAttribute("reviewType", "Approval");

        // Get an array of phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(request), verification.getProject());
        // Get an active phase for the project
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.APPROVAL_PHASE_NAME);
        // Retrieve a resource for the Approval phase
        Resource resource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Get the form defined for this action
        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] answers = (String[]) reviewForm.get("answer");
        String[] replies = (String[]) reviewForm.get("comment");
        Long[] commentTypeIds = (Long[]) reviewForm.get("commentType");
        int index = 0;

        // Obtain an instance of review manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);

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
                        comment.setCommentType(
                                ActionsHelper.findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
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
                    comment.setCommentType(
                            ActionsHelper.findCommentTypeById(commentTypes, commentTypeIds[i].longValue()));
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
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.VIEW_APPROVAL_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for this review
        Scorecard scorecardTemplate = scrMgr.getScorecard(verification.getReview().getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Client Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_APPROVAL_PERM_NAME, "Error.ReviewTypeIncorrect");
        }

        // Make sure that the user is not trying to view unfinished review
        if (!verification.getReview().isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_APPROVAL_PERM_NAME, "Error.ReviewNotCommitted");
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
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
        Review[] reviews = revMgr.searchReviews(filter, false);

        if (reviews.length != 3) {
            return null; // TODO: Forward to userError.jsp page
        }

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);
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
        Project project = getProjectFromSubmission(ActionsHelper.createProjectManager(request), submission);
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
        Project project = getProjectFromSubmission(ActionsHelper.createProjectManager(request), submission);
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
     * This static method retrieves the project that the submission specified by the
     * <code>submission</code> parameter was made for.
     *
     * @return a retrieved project.
     * @param manager
     *            an instance of <code>ProjectManager</code> object used to retrieve project from
     *            the submission.
     * @param submission
     *            a submission to retrieve the project for.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws com.topcoder.management.project.PersistenceException
     *             if an error occurred while accessing the database.
     */
    private static Project getProjectFromSubmission(ProjectManager manager, Submission submission)
        throws com.topcoder.management.project.PersistenceException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(manager, "manager");
        ActionsHelper.validateParameterNotNull(submission, "submission");

        // Get an upload for this submission
        Upload upload = submission.getUpload();

        // Get Project by its id
        Project project = manager.getProject(upload.getProject());

        return project;
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
     * @throws BaseException
     *             if any error occurs.
     */
    private static void retrieveAndStoreBasicAggregationInfo(
            HttpServletRequest request, CorrectnessCheckResult verification, Scorecard scorecardTemplate)
        throws BaseException {
        // Retrieve a project from verification-result bean
        Project project = verification.getProject();
        // Retrieve a review from verification-result bean
        Review review = verification.getReview();

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        // Retrieve a submission to edit an aggregation scorecard for
        Submission submission = verification.getSubmission();
        // Get submitter's resource
        Resource submitter = resMgr.getResource(submission.getUpload().getOwner());
        // Get Aggregator's resource
        Resource aggregator = resMgr.getResource(review.getAuthor());

        // Place submitter's and aggregator's user IDs into the request
        request.setAttribute("submitterId", submitter.getProperty("External Reference ID"));
        request.setAttribute("aggregatorId", aggregator.getProperty("External Reference ID"));

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
}
