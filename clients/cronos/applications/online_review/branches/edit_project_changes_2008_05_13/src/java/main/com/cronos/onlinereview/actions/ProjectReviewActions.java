/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.LazyValidatorForm;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.ReviewEntityNotFoundException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.data.ReviewEditor;
import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.RankedSubmission;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator;
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
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.UploadedFile;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.log.Level;
import com.topcoder.util.weightedcalculator.LineItem;

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
 * @author George1
 * @author real_vg
 * @version 1.0
 */
public class ProjectReviewActions extends DispatchAction {
	private static final com.topcoder.util.log.Log log = com.topcoder.util.log.LogFactory
			.getLog(ProjectReviewActions.class.getName());
    /**
     * This member variable is a constant that specifies the count of comments displayed for each
     * item by default on Edit Screening, Edit Review, and Edit Approval pages.
     */
    private static final int DEFAULT_COMMENTS_NUMBER = 1;

    /**
     * This member variable is a constant that specifies the count of comments displayed for each
     * item when Manager opens either Edit Screening, Edit Review, or Edit Approval page.
     */
    private static final int MANAGER_COMMENTS_NUMBER = 1;

    /**
     * This member variable holds the all possible values of answers to &#39;Scale&#160;(1-4)&#39;
     * and &#39;Scale&#160;(1-10)&#39; types of scorecard question.
     */
    private static final Map<String, Set<String>> correctAnswers = new HashMap<String, Set<String>>();

    // Initialize the above map
    static {
        String scale1_4 = "Scale (1-4)";
        String scale1_10 = "Scale (1-10)";
        String scale0_3 = "Scale (0-3)";
        String scale0_9 = "Scale (0-9)";
        String scale0_4 = "Scale (0-4)";
        correctAnswers.put(scale1_4, new HashSet<String>());
        correctAnswers.put(scale1_10, new HashSet<String>());
        correctAnswers.put(scale0_3, new HashSet<String>());
        correctAnswers.put(scale0_9, new HashSet<String>());
        correctAnswers.put(scale0_4, new HashSet<String>());
        for (int i = 0; i <= 10; i++) {
        	if (i <= 3) {
                correctAnswers.get(scale0_3).add(i + "/3");
            }
        	if (i >= 1 && i <= 4) {
                correctAnswers.get(scale1_4).add(i + "/4");
            }
        	if (i >= 0 && i <= 4) {
                correctAnswers.get(scale0_4).add(i + "/4");
            }
            if (i >= 1 && i <= 10) {
                correctAnswers.get(scale1_10).add(i + "/10");
            }
            if (i <= 9) {
            	correctAnswers.get(scale0_9).add(i + "/9");
            }
        }
    }

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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREGATION_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that user has the permission to perform aggregation
        if (!AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREGATION_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.ReviewCommitted", null);
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
                if (ActionsHelper.isReviewerComment(item.getComment(j))) {
                    ++allCommentsNum;
                }
            }
        }

        LazyValidatorForm aggregationForm = (LazyValidatorForm) form;

        String[] aggregatorResponses = new String[review.getNumberOfItems()];
        String[] aggregateFunctions = new String[allCommentsNum];
        Long[] responseTypeIds = new Long[allCommentsNum];
        int commentIdx = 0;
        int itemIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx) {
                    Question question = section.getQuestion(questionIdx);
                    long questionId = question.getId();

                    for (int i = 0; i < aggregatorResponses.length; ++i) {
                        if (review.getItem(i).getQuestion() != questionId) {
                            continue;
                        }

                        // Get a review's item
                        Item item = review.getItem(i);
                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            Comment comment = item.getComment(j);

                            if (ActionsHelper.isReviewerComment(comment)) {
                                String aggregFunction = (String) comment.getExtraInfo();
                                if ("Reject".equalsIgnoreCase(aggregFunction)) {
                                    aggregateFunctions[commentIdx] = "Reject";
                                } else if ("Accept".equalsIgnoreCase(aggregFunction)) {
                                    aggregateFunctions[commentIdx] = "Accept";
                                } else if ("Duplicate".equalsIgnoreCase(aggregFunction)) {
                                    aggregateFunctions[commentIdx] = "Duplicate";
                                } else {
                                    aggregateFunctions[commentIdx] = "";
                                }
                                responseTypeIds[commentIdx] = new Long(comment.getCommentType().getId());
                                ++commentIdx;
                            }

                            final String commentType = comment.getCommentType().getName();

                            if (commentType.equalsIgnoreCase("Aggregation Comment")) {
                                aggregatorResponses[itemIdx++] = comment.getComment();
                            }
                        }
                    }
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
    	LoggingHelper.logAction(request);
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREGATION_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that user has the permission to perform aggregation
        if (!AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREGATION_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to save
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.ReviewCommitted", null);
        }

        // Get an array of all phases for current project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(request, false), verification.getProject());
        // Get an active phase for the project
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.AGGREGATION_PHASE_NAME);
        // Check that Aggregation phase is really active (open)
        if (phase == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.PERFORM_AGGREGATION_PERM_NAME, "Error.IncorrectPhase", null);
        }

        // This variable determines if 'Save and Mark Complete' button has been clicked
        final boolean commitRequested = "submit".equalsIgnoreCase(request.getParameter("save"));
        // This variable determines if Preview button has been clicked
        final boolean previewRequested = "preview".equalsIgnoreCase(request.getParameter("save"));

        // Retrieve a resource for the Aggregation phase
        Resource resource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Get the form defined for this action
        LazyValidatorForm aggregationForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] responses = (String[]) aggregationForm.get("aggregator_response");
        String[] aggregateFunctions = (String[]) aggregationForm.get("aggregate_function");
        Long[] responseTypeIds = (Long[]) aggregationForm.get("aggregator_response_type");
        int commentIndex = 0;
        int itemIdx = 0;

        // Obtain an instance of review manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Retrieve all comment types
        CommentType[] allCommentTypes = revMgr.getAllCommentTypes();
        int numberOfItems = review.getNumberOfItems();

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++ questionIdx) {
                    Question question = section.getQuestion(questionIdx);
                    long questionId = question.getId();

                    for (int i = 0; i < numberOfItems; ++i) {
                        if (review.getItem(i).getQuestion() != questionId) {
                            continue;
                        }

                        // Get an item
                        Item item = review.getItem(i);
                        Comment aggregatorComment = null;

                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            Comment comment = item.getComment(j);
                            String typeName = comment.getCommentType().getName();

                            if (typeName.equalsIgnoreCase("Comment") || typeName.equalsIgnoreCase("Required") ||
                                    typeName.equalsIgnoreCase("Recommended")) {
                                if (commentIndex < aggregateFunctions.length && aggregateFunctions[commentIndex] != null &&
                                        aggregateFunctions[commentIndex].trim().length() != 0) {
                                    comment.setExtraInfo(aggregateFunctions[commentIndex]);
                                } else {
                                    comment.setExtraInfo(null);
                                }
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

                        aggregatorComment.setComment(responses[itemIdx++]);
                        aggregatorComment.setAuthor(resource.getId());
                    }
                }
            }
        }

        boolean validationSucceeded =
            (commitRequested) ? validateAggregationScorecard(request, scorecardTemplate, review, false) : true;

        // If the user has requested to complete the review
        if (validationSucceeded && commitRequested) {
            // Set the completed status of the review
            review.setCommitted(true);
        } else if (previewRequested) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Retrieve some basic aggregation info and store it into the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");

            // Forward to preview page
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Update (save) edited Aggregation
        revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        if (!validationSucceeded) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");
            // Retrieve some look-up data and store it into the request
            retreiveAndStoreReviewLookUpData(request);

            return mapping.getInputForward();
        }

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
    	LoggingHelper.logAction(request);
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.VIEW_AGGREGATION_PERM_NAME);

        // Get message resources
        MessageResources messages = getResources(request);

        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            // Need to support view aggregation just by specifying the project id
            verification = ActionsHelper.checkForCorrectProjectId(
                    mapping, getResources(request), request, Constants.VIEW_AGGREGATION_PERM_NAME, false);
            if (!verification.isSuccessful()) {
                return verification.getForward();
            }

            // Obtain an instance of Resource Manager
            ResourceManager resMgr = ActionsHelper.createResourceManager(request);
            // Get the list of all possible resource roles
            ResourceRole[] allResourceRoles = resMgr.getAllResourceRoles();

            // Create filters to select Aggregators for the project
            Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(verification.getProject().getId());
            Filter filterRole = ResourceFilterBuilder.createResourceRoleIdFilter(
                    ActionsHelper.findResourceRoleByName(allResourceRoles, Constants.AGGREGATOR_ROLE_NAME).getId());
            // Combine the upper two filter
            Filter filterAggregators = new AndFilter(filterProject, filterRole);
            // Fetch all Aggregators for the project
            Resource[] aggregators = resMgr.searchResources(filterAggregators);

            // If the project does not have any Aggregators,
            // there cannot be any Aggregation worksheets. Signal about the error to the user
            if (aggregators.length == 0) {
                return ActionsHelper.produceErrorReport(mapping, messages, request,
                        Constants.VIEW_AGGREGATION_PERM_NAME, "Error.NoAggregations", null);
            }

            List resourceIds = new ArrayList();

            for (int i = 0; i < aggregators.length; ++i) {
                resourceIds.add(new Long(aggregators[i].getId()));
            }

            Filter filterReviewers = new InFilter("reviewer", resourceIds);
            Filter filterCommitted = new EqualToFilter("committed", new Integer(1));
            // Build final combined filter
            Filter filter = new AndFilter(filterReviewers, filterCommitted);
            // Obtain an instance of Review Manager
            ReviewManager reviewMgr = ActionsHelper.createReviewManager(request);
            // Fetch all reviews (Aggregations only) for the project
            Review[] reviews = reviewMgr.searchReviews(filter, true);

            if (reviews.length == 0) {
                return ActionsHelper.produceErrorReport(mapping, messages, request,
                        Constants.VIEW_AGGREGATION_PERM_NAME, "Error.NoAggregations", null);
            }

            // Sort reviews in array to find the latest Aggregation worksheet for the project
            Arrays.sort(reviews, new Comparators.ReviewComparer());
            // Fetch the most recent review from the array
            Review review = reviews[reviews.length - 1];

            verification.setReview(review);
            // Place the review object as attribute in the request
            request.setAttribute("review", review);

            // Obtain an instance of Deliverable Manager
            UploadManager upMgr = ActionsHelper.createUploadManager(request);
            // Get Submission by its id
            Submission submission = upMgr.getSubmission(review.getSubmission());

            // Store Submission object in the result bean
            verification.setSubmission(submission);
            // Place the id of the submission as attribute in the request
            request.setAttribute("sid", new Long(submission.getId()));
        }

        // Verify that user has the permission to view aggregation
        if (!AuthorizationHelper.hasUserPermission(request, Constants.VIEW_AGGREGATION_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREGATION_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review (aggregation) to view
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREGATION_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Make sure that the user is trying to view Aggregation Review, not Aggregation
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREGATION_PERM_NAME, "Error.ReviewNotCommitted", null);
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");

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
            HttpServletRequest request, HttpServletResponse response) throws BaseException {
    	LoggingHelper.logAction(request);

        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREG_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
        	log.log(Level.DEBUG, "failed checkForCorrectReviewId");
            return verification.getForward();
        }

        // Verify that user has the permission to perform aggregation review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREG_REVIEW_PERM_NAME)) {
        	log.log(Level.DEBUG, "the user doesn't the permission: " + Constants.PERFORM_AGGREG_REVIEW_PERM_NAME);
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
        	log.log(Level.DEBUG, "failed Verify that the scorecard template for this review is of correct type");
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Verify that Aggregation has been committed
        if (!review.isCommitted()) {
        	log.log(Level.DEBUG, "failed isCommited");
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.AggregationNotCommitted", null);
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
            	log.log(Level.DEBUG, "failed If \"my\" comment has not been found, then the user is probably an Aggregator");
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.CannotReviewOwnAggregation", null);
            } else {
                // Otherwise, the user does not have permission to edit this review
            	log.log(Level.DEBUG, "failed Otherwise, the user does not have permission to edit this review");
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            }
        }

        // Do actual verificartion. Values "Approved" and "Rejected" denote committed Aggregation Review
        String myExtaInfo = (String) myReviewComment.getExtraInfo();
        if ("Approved".equalsIgnoreCase(myExtaInfo) || "Rejected".equalsIgnoreCase(myExtaInfo)) {
        	log.log(Level.DEBUG, "failed Do actual verificartion. Values \"Approved\" and \"Rejected\" denote committed Aggregation Review");
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.ReviewCommitted", null);
        }

        boolean isSubmitter = false;

        // If the user is a Submitter, let underlying JSP page know about this fact
        if (AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME)) {
            isSubmitter = true;
            request.setAttribute("isSubmitter", Boolean.valueOf(isSubmitter));
        } else {
            // Otherwise examine the submitter's comment
            if (submitterComment != null) {
                String submitterExtraInfo = (String) submitterComment.getExtraInfo();
                if ("Approved".equalsIgnoreCase(submitterExtraInfo) ||
                        "Rejected".equalsIgnoreCase(submitterExtraInfo)) {
                    // Indicate to the underlying JSP page that submitter has committed its
                    // Aggregation Review, so submitter's comments can be displayed to the reviewers
                    request.setAttribute("submitterCommitted", Boolean.TRUE);
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

        int itemIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx) {
                    // Get the ID of the current question
                    final long questionId = section.getQuestion(questionIdx).getId();

                    // Iterate over the items of existing review that needs editing
                    for (int i = 0; i < review.getNumberOfItems(); ++i) {
                        // Get an item for the current iteration
                        Item item = review.getItem(i);
                        // Verify that the item is for current scorecard template question
                        if (item.getQuestion() != questionId) {
                            continue;
                        }

                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            Comment comment = item.getComment(j);
                            String commentType = comment.getCommentType().getName();

                            if ((isSubmitter && commentType.equalsIgnoreCase("Submitter Comment")) ||
                                    (!isSubmitter && myReviewComment.getAuthor() == comment.getAuthor() &&
                                            commentType.equalsIgnoreCase("Aggregation Review Comment"))) {
                                String reviewFunction = (String) comment.getExtraInfo();
                                if ("Reject".equalsIgnoreCase(reviewFunction)) {
                                    reviewFunctions[itemIdx] = "Reject";
                                }
                                if (comment.getComment() != null && comment.getComment().trim().length() != 0) {
                                    rejectReasons[itemIdx] = comment.getComment();
                                }

                                ++itemIdx;
                                break;
                            }
                        }
                    }
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
    	LoggingHelper.logAction(request);
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREG_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that user has the permission to perform aggregation review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_AGGREG_REVIEW_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to save
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Verify that the user is attempting to save Aggregation Review, not Aggregation
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.AggregationNotCommitted", null);
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
                        Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.CannotReviewOwnAggregation", null);
            } else {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
            }
        }

        // Do actual verificartion. Values "Approved" and "Rejected" denote committed Aggregation Review
        String myExtaInfo = (String) myReviewComment.getExtraInfo();
        if ("Approved".equalsIgnoreCase(myExtaInfo) || "Rejected".equalsIgnoreCase(myExtaInfo)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.ReviewCommitted", null);
        }

        // This variable determines if 'Save and Mark Complete' button has been clicked
        final boolean commitRequested = "submit".equalsIgnoreCase(request.getParameter("save"));
        // Determine if the user is a Submitter
        final boolean isSubmitter = AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME);

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
                        if (item.getQuestion() != questionId) {
                            continue;
                        }

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

                        // Set the reason of reject/accept (i.e. actual comment's text)
                        userComment.setComment(rejectReasons[itemIdx]);

                        // If review function equals to anythning but "Accept", regard the item as
                        // rejected. If current user is a Submitter, disregard a value in the
                        // reviewFunctions array and always treat it as containing "Accept" string
                        // (thus, Submitter can never reject aggregation worksheet)
                        if (isSubmitter || "Accept".equalsIgnoreCase(reviewFunctions[itemIdx])) {
                            userComment.setExtraInfo("Accept");
                        } else {
                            userComment.setExtraInfo("Reject");
                            rejected = true;
                        }
                        ++itemIdx;
                    }
                }
            }
        }

        // A safety check: reset 'rejected' flag if the current user is a Submitter
        if (isSubmitter) {
        	rejected = false;
        }

        boolean validationSucceeded = (commitRequested) ? validateAggregationReviewScorecard(
                request, scorecardTemplate, review, myReviewComment.getAuthor()) : true;

        // If the user has requested to complete the review
        if (validationSucceeded && commitRequested) {
            // Values "Approved" or "Rejected" will denote committed review
            myReviewComment.setExtraInfo((rejected == true) ? "Rejected" : "Approved");
        }

        // Update (save) edited Aggregation Review
        revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        if (!validationSucceeded) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "AggregationReview");

            return mapping.getInputForward();
        }

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
    	LoggingHelper.logAction(request);
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.VIEW_AGGREG_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that user has the permission to view aggregation review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.VIEW_AGGREG_REVIEW_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREG_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review (aggregation) to view
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREG_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Make sure that the user is not trying to view unfinished review
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_AGGREG_REVIEW_PERM_NAME, "Error.AggregationNotCommitted", null);
        }

        // Verify that Aggregation Review has been committed by all users who should have done that
        for (int i = 0; i < review.getNumberOfComments(); ++i) {
            Comment comment = review.getComment(i);
            String status = (String) comment.getExtraInfo();
            if (!("Approved".equalsIgnoreCase(status) || "Rejected".equalsIgnoreCase(status))) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.VIEW_AGGREG_REVIEW_PERM_NAME, "Error.AggregationReviewNotCommitted", null);
            }
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "AggregationReview");

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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectReviewId(mapping, request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that user has the permission to perform final review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewCommitted", null);
        }

        // Retrieve some basic aggregation info and store it into the request
        retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "FinalReview");

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

        Arrays.fill(fixStatuses, "");
        Arrays.fill(finalComments, "");

        int commentIdx = 0;
        int itemIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx) {
                    // Get the ID of the current scorecard template's question
                    final long questionId = section.getQuestion(questionIdx).getId();

                    // Iterate over the items of existing review that needs editing
                    for (int i = 0; i < review.getNumberOfItems(); ++i) {
                        // Get an item for the current iteration
                        Item item = review.getItem(i);
                        // Verify that this item is for the current scorecard template question
                        if (item.getQuestion() != questionId) {
                            continue;
                        }

                        boolean finalReviewCommentNotFound = true;

                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            Comment comment = item.getComment(j);
                            String commentType = comment.getCommentType().getName();

                            if (ActionsHelper.isReviewerComment(comment)) {
                                String fixStatus = (String) comment.getExtraInfo();
                                if ("Fixed".equalsIgnoreCase(fixStatus)) {
                                    fixStatuses[commentIdx] = "Fixed";
                                } else if ("Not Fixed".equalsIgnoreCase(fixStatus)) {
                                    fixStatuses[commentIdx] = "Not Fixed";
                                }
                                ++commentIdx;
                            }
                            if (finalReviewCommentNotFound && commentType.equalsIgnoreCase("Final Review Comment")) {
                                finalComments[itemIdx++] = comment.getComment();
                                finalReviewCommentNotFound = false;
                            }
                        }
                    }
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
    	LoggingHelper.logAction(request);
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that user has the permission to perform final review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to save
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Verify that review has not been committed yet
        if (review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.ReviewCommitted", null);
        }

        // Get an array of all phases for current project
        Phase[] phases = ActionsHelper.getPhasesForProject(
                ActionsHelper.createPhaseManager(request, false), verification.getProject());
        // Get an active phase for the project
        Phase phase = ActionsHelper.getPhase(phases, true, Constants.FINAL_REVIEW_PHASE_NAME);
        // Check that Final Review Phase is really active (open)
        if (phase == null) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_FINAL_REVIEW_PERM_NAME, "Error.IncorrectPhase", null);
        }

        // This variable determines if 'Save and Mark Complete' button has been clicked
        final boolean commitRequested = "submit".equalsIgnoreCase(request.getParameter("save"));
        // This variable determines if Preview button has been clicked
        final boolean previewRequested = "preview".equalsIgnoreCase(request.getParameter("save"));

        // Retrieve a resource for the Final Review phase
        Resource resource = ActionsHelper.getMyResourceForPhase(request, phase);
        // Get the form defined for this action
        LazyValidatorForm finalReviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] fixStatuses = (String[]) finalReviewForm.get("fix_status");
        String[] finalComments = (String[]) finalReviewForm.get("final_comment");
        Boolean approveFixesObj = (Boolean) finalReviewForm.get("approve_fixes");
        boolean approveFixes = (approveFixesObj != null && approveFixesObj.booleanValue() == true);
        int commentIdx = 0;
        int itemIdx = 0;

        // Obtain an instance of review manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Retrieve all comment types
        CommentType[] allCommentTypes = revMgr.getAllCommentTypes();

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
                            finalReviewComment.setCommentType(
                                    ActionsHelper.findCommentTypeByName(allCommentTypes, "Final Review Comment"));
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
            reviewLevelComment.setCommentType(
                    ActionsHelper.findCommentTypeByName(allCommentTypes, "Final Review Comment"));
            review.addComment(reviewLevelComment);
        }

        reviewLevelComment.setAuthor(resource.getId());
        reviewLevelComment.setExtraInfo("Approving");
        reviewLevelComment.setComment("");

        boolean validationSucceeded =
            (commitRequested) ? validateFinalReviewScorecard(request, scorecardTemplate, review) : true;

        // If the user has requested to complete the review
        if (validationSucceeded && commitRequested) {
            reviewLevelComment.setExtraInfo((approveFixes == true) ? "Approved" : "Rejected");

            // Set the completed status of the review
            review.setCommitted(true);
        } else if (previewRequested) {
            // Retrieve some basic aggregation info and store it into the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "FinalReview");

            // Update review object stored in the request
            request.setAttribute("review", review);

            // Forward to preview page
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Update (save) edited Aggregation
        revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

        if (!validationSucceeded) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "FinalReview");

            return mapping.getInputForward();
        }

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
    	LoggingHelper.logAction(request);
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
            checkForCorrectReviewId(mapping, request, Constants.VIEW_FINAL_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that user has the permission to view final review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.VIEW_FINAL_REVIEW_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_FINAL_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrieve a review to view
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review")) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_FINAL_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }
        // Make sure that the user is not trying to view unfinished review
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_FINAL_REVIEW_PERM_NAME, "Error.ReviewNotCommitted", null);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
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
    	LoggingHelper.logAction(request);
        return viewGenericReview(mapping, form, request, "Approval");
    }

    /**
     * This method is an implementation of &quot;View Composite Scorecard&quot; Struts Action
     * defined for this assembly, which is supposed to gather needed information (scorecard template
     * and reviews from individual reviewers for some submission) and present it to
     * viewCompositeScorecard.jsp page, which will present all the gathered information to the user.
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
    	try {
    	LoggingHelper.logAction(request);
        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification =
                checkForCorrectSubmissionId(mapping, request, Constants.VIEW_COMPOS_SCORECARD_PERM_NAME);
        // If any error has occured, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that currently logged in user has enough rights to proceed with the action
        if (!AuthorizationHelper.hasUserPermission(request, Constants.VIEW_COMPOS_SCORECARD_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request, false), project);

        if (!ActionsHelper.isAfterAppealsResponse(phases)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.CompositeScorecardWrongStage", null);
        }

        // Get the Review phase
        Phase phase = ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME);

        // Retrieve a scorecard template for the Review phase
        Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(
                ActionsHelper.createScorecardManager(request), phase);
        // Get the count of questions in the current scorecard
        final int questionsCount = ActionsHelper.getScorecardQuestionsCount(scorecardTemplate);

        // Build a filter to select resources (i.e. reviewers) for Review phase
        Filter filterPhase = ResourceFilterBuilder.createPhaseIdFilter(phase.getId());
        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        // Retrieve reviewers that did the reviews
        Resource[] reviewers = resMgr.searchResources(filterPhase);
        for (int i = 0; i < reviewers.length; i++) {
            ActionsHelper.populateEmailProperty(request, reviewers[i]);
        }

        if (reviewers.length == 0) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.InternalError", null);
        }

        List reviewerIds = new ArrayList();

        for (int i = 0; i < reviewers.length; ++i) {
            reviewerIds.add(new Long(reviewers[i].getId()));
        }

        // Prepare filters
        Filter filterReviewers = new InFilter("reviewer", reviewerIds);
        Filter filterSubmission = new EqualToFilter("submission", new Long(verification.getSubmission().getId()));
        Filter filterCommitted = new EqualToFilter("committed", new Integer(1));
        Filter filterScorecard = new EqualToFilter("scorecardType",
                new Long(scorecardTemplate.getScorecardType().getId()));

        // Prepare final combined filter
        Filter filter = new AndFilter(Arrays.asList(
                new Filter[] {filterReviewers, filterSubmission, filterCommitted, filterScorecard}));
        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Retrieve an array of reviews
        Review[] reviews = revMgr.searchReviews(filter, true);

        if (reviews.length != reviewers.length) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.CompositeScorecardIsNotReady", null);
        }

        // Verify that number of items in every review scorecard
        // match the number of questions in scorecard template
        for (int i = 0; i < reviews.length; ++i) {
            if (reviews[i].getNumberOfItems() != questionsCount) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.InternalError", null);
            }
        }

        // Obtain ScorecardMatrix for scorecard
        ScorecardMatrix matrix = (new DefaultScorecardMatrixBuilder()).buildScorecardMatrix(scorecardTemplate);
        // Create CalculationManager instance
        CalculationManager calculationManager = new CalculationManager();


        // Retrieve the user ids for the review authors
        // and additionally the individual item scores and average total score
        long[] authors = new long[reviewers.length];
        double avgScore = 0.0;
        double[] avgScores = new double[questionsCount];
        double[][] scores = new double[reviews.length][];

        for (int i = 0; i < reviews.length; i++) {
            // Get a review for the current iteration
            Review review = reviews[i];

            Resource reviewer = null;
            // Find a reviewer that is authour of the current review
            for (int j = 0; j < reviewers.length; ++j) {
                if (review.getAuthor() == reviewers[j].getId()) {
                    reviewer = reviewers[j];
                    break;
                }
            }

            if (reviewer == null) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.InternalError", null);
            }

            authors[i] = Long.parseLong((String) reviewer.getProperty("External Reference ID"));
            avgScore += (review.getScore() != null) ? review.getScore().floatValue() : 0;
            scores[i] = new double[questionsCount];
            int itemIdx = 0;

            for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); groupIdx++) {
                Group group = scorecardTemplate.getGroup(groupIdx);
                for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); sectionIdx++) {
                    Section section = group.getSection(sectionIdx);
                    for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); questionIdx++) {
                        Question question = section.getQuestion(questionIdx);

                        ScoreCalculator scoreCalculator =
                            calculationManager.getScoreCalculator(question.getQuestionType().getId());
                        LineItem lineItem = matrix.getLineItem(question.getId());
                        double weight = 0;
                        if (lineItem != null) {
                        	weight = lineItem.getWeight();
                        } else {
                        	log.log(Level.WARN, "line item for question id: " + question.getId() + 
                        			" for review: " + review.getId());
                        }
                        scores[i][itemIdx] = (float) (weight * scoreCalculator.evaluateItem(review.getItem(itemIdx), question));
                        ++itemIdx;
                    }
                }
            }
        }

        // Calculate average per-item scores
        for (int i = 0; i < questionsCount; ++i) {
            double itemScore = 0.0;

            for (int j = 0; j < reviewers.length; ++j) {
                itemScore += scores[j][i];
                avgScores[i] = itemScore / reviewers.length;
            }
        }

        // Calculate average score
        avgScore /= reviewers.length;

        // Store gathered data into the request
        request.setAttribute("authors", authors);
        request.setAttribute("avgScore", new Double(avgScore));
        request.setAttribute("avgScores", avgScores);
        request.setAttribute("scores", scores);

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, "CompositeReview", scorecardTemplate);

        // Store reviews in the request
        request.setAttribute("reviews", reviews);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    	} catch (Throwable e) {
    		StringWriter buf = new StringWriter();
    		e.printStackTrace(new PrintWriter(buf));
    		log.log(Level.ERROR, buf);
    		throw new BaseException(e);
    	}
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
                    mapping, getResources(request), request, permission, "Error.SubmissionIdNotSpecified", null));
            // Return the result of the check
            return result;
        }

        long sid;

        try {
            // Try to convert specified sid parameter to its integer representation
            sid = Long.parseLong(sidParam, 10);
        } catch (NumberFormatException e) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.SubmissionNotFound", null));
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
                    mapping, getResources(request), request, permission, "Error.SubmissionNotFound", null));
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

        // Return the result of the check
        return result;
    }

    /**
     * This method verifies the request for certains conditions to be met. This includes verifying
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
                    mapping, getResources(request), request, permission, "Error.ReviewIdNotSpecified", null));
            // Return the result of the check
            return result;
        }

        long rid;

        try {
            // Try to convert specified rid parameter to its integer representation
            rid = Long.parseLong(ridParam, 10);
        } catch (NumberFormatException e) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.ReviewNotFound", null));
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
                    mapping, getResources(request), request, permission, "Error.ReviewNotFound", null));
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
    private void retrieveAndStoreBasicAggregationInfo(HttpServletRequest request,
            CorrectnessCheckResult verification, Scorecard scorecardTemplate, String reviewType)
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
                ActionsHelper.createPhaseManager(request, false), project);

        // Get a Review phase
        Phase reviewPhase = ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME);
        // Retrieve all resources (reviewers) for that phase
        Resource[] reviewResources = ActionsHelper.getAllResourcesForPhase(
                ActionsHelper.createResourceManager(request), reviewPhase);
        for (int i = 0; i < reviewResources.length; i++) {
            ActionsHelper.populateEmailProperty(request, reviewResources[i]);
        }
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
    private CommentType[] retreiveAndStoreReviewLookUpData(HttpServletRequest request) throws BaseException {
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
        // and return them
        return reviewCommentTypes;
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
        ActionsHelper.populateEmailProperty(request, author);

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
    private void retrieveAndStoreBasicReviewInfo(HttpServletRequest request,
            CorrectnessCheckResult verification, String reviewType, Scorecard scorecardTemplate)
        throws BaseException {
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

        // Verify that user has the permission to perform review
        if (!AuthorizationHelper.hasUserPermission(request, permName)) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permName, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request, false), project);

        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, phaseName);
        // Check that the phase in question is really active (open)
        if (phase == null) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permName, "Error.IncorrectPhase", null);
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

        CommentType typeComment = ActionsHelper.findCommentTypeByName(
                (CommentType[]) request.getAttribute("allCommentTypes"), "Comment");

        Integer[] commentCounts = new Integer[questionsCount];
        Arrays.fill(commentCounts, new Integer(DEFAULT_COMMENTS_NUMBER));
        reviewForm.set("comment_count", commentCounts);

        for (int i = 0; i < questionsCount; i++) {
            for (int j = 0; j <= DEFAULT_COMMENTS_NUMBER; j++) {
                reviewForm.set("comment", i + "." + j, "");
                reviewForm.set("comment_type", i + "." + j, typeComment);
            }
        }

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

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scorMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scorMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase(scorecardTypeName)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.ReviewTypeIncorrect", null);
        }

        boolean managerEdit = false;
        // Check if review has been committed
        if (review.isCommitted()) {
            // If user has a Manager or Global Manager role, put special flag to the request
            // indicating that we need "Manager Edit"
            if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
                request.setAttribute("managerEdit", Boolean.TRUE);
                managerEdit = true;
            } else {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                            Constants.EDIT_ANY_SCORECARD_PERM_NAME, "Error.ReviewCommitted", null);
            }
        }

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

        // Verify that the user has permission to edit review
        if (!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_ANY_SCORECARD_PERM_NAME)) {
            if (!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_MY_REVIEW_PERM_NAME)) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            } else if(verification.getReview().getAuthor() !=
                    ((Resource) request.getAttribute("authorResource")).getId()) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request),
                        request, Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            }
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Retrive some look-up data and store it into the request
        CommentType[] commentTypes = retreiveAndStoreReviewLookUpData(request);

        // Prepare the arrays
        String[] answers = new String[review.getNumberOfItems()];

        int itemIdx = 0;

        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Walk the items in the review setting appropriate values in the arrays
        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx, ++itemIdx) {
                    Item item = review.getItem(itemIdx);
                    Comment[] comments = (managerEdit) ? getItemManagerComments(item) : getItemReviewerComments(item);

                    answers[itemIdx] = (String) item.getAnswer();

                    reviewForm.set("comment_type", itemIdx + ".0", null);
                    reviewForm.set("comment", itemIdx + ".0", "");

                    final int commentCount =
                        Math.max(comments.length, (managerEdit) ? MANAGER_COMMENTS_NUMBER : DEFAULT_COMMENTS_NUMBER);

                    reviewForm.set("comment_count", itemIdx, new Integer(commentCount));

                    for (int i = 0; i < commentCount; ++i) {
                        String commentKey = itemIdx + "." + (i + 1);
                        Comment comment = (i < comments.length) ? comments[i] : null;

                        reviewForm.set("comment", commentKey, (comment != null) ? comment.getComment() : "");
                        reviewForm.set("comment_type", commentKey,
                                new Long((comment != null) ? comment.getCommentType().getId() :
                                    ActionsHelper.findCommentTypeByName(commentTypes, "Comment").getId()));
                    }
                }
            }
        }

        if (!managerEdit) {
            request.setAttribute("uploadedFileIds", collectUploadedFileIds(scorecardTemplate, review));
        }

        /*
         * Populate the form properties which weren't populated above
         */

        // Populate form properties
        reviewForm.set("answer", answers);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * TODO: Document it
     *
     * @param item
     * @return
     */
    private static Comment[] getItemManagerComments(Item item) {
        List result = new ArrayList();
        for (int i = 0; i < item.getNumberOfComments(); i++) {
            Comment comment = item.getComment(i);
            if (ActionsHelper.isManagerComment(comment)) {
                result.add(comment);
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
    private static Comment[] getItemReviewerComments(Item item) {
        List result = new ArrayList();
        for (int i = 0; i < item.getNumberOfComments(); i++) {
            Comment comment = item.getComment(i);
            if (ActionsHelper.isReviewerComment(comment)) {
                result.add(comment);
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
                    permName, "Error.SubmissionAndReviewIdNotSpecified", null);
        }

        // If check was not successful, return an appropriate action forward
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request, false), project);
        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, phaseName);
        // Check that the phase in question is really active (open)
        if (phase == null) {
            if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
                // Managers can edit reviews in any phase
                phase = ActionsHelper.getPhase(phases, false, phaseName);
            } else {
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, permName, "Error.IncorrectPhase", null);
            }
        }

        // Get "My" resource for the appropriate phase
        Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        // If no resource found for particular phase, try to find resource without phase assigned
        if (myResource == null) {
            myResource = ActionsHelper.getMyResourceForPhase(request, null);
        }
        if (myResource == null) {
        	myResource = (Resource) request.getAttribute("global_resource");
        }

        // Retrieve the review to edit (if any)
        Review review = verification.getReview();
        Scorecard scorecardTemplate = null;

        if (review == null) {
            // Verify that user has the permission to perform the review
            if (!AuthorizationHelper.hasUserPermission(request, permName)) {
                return ActionsHelper.produceErrorReport(
                        mapping, getResources(request), request, permName, "Error.NoPermission", Boolean.TRUE);
            }
            // At this point, redirect-after-login attribute should be removed (if it exists)
            AuthorizationHelper.removeLoginRedirect(request);

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
            // user is trying to create review that already exists
            if (reviews.length != 0) {
                review = reviews[0];
                verification.setReview(review);
            }
        }

        if (review != null) {
            // Verify that the user has permission to edit review
            if (!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_ANY_SCORECARD_PERM_NAME)) {
                if (!AuthorizationHelper.hasUserPermission(request, Constants.EDIT_MY_REVIEW_PERM_NAME)) {
                    return ActionsHelper.produceErrorReport(mapping, getResources(request),
                        request, Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
                } else if(verification.getReview().getAuthor() != myResource.getId()) {
                    return ActionsHelper.produceErrorReport(mapping, getResources(request),
                            request, Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
                }
            }
            // At this point, redirect-after-login attribute should be removed (if it exists)
            AuthorizationHelper.removeLoginRedirect(request);

            // Obtain an instance of Scorecard Manager
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
            // Retrieve a scorecard template for the review
            scorecardTemplate = scrMgr.getScorecard(review.getScorecard());
        }

        // Verify that the scorecard template for this review is of correct type
        if (scorecardTemplate == null || !scorecardTemplate.getScorecardType().getName().equalsIgnoreCase(scorecardTypeName)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    permName, "Error.ReviewTypeIncorrect", null);
        }

        boolean managerEdit = false;
        // Check if review has been committed
        if (review != null && review.isCommitted()) {
            // If user has a Manager role, put special flag to the request,
            // indicating that we need "Manager Edit"
            if(AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
                request.setAttribute("managerEdit", Boolean.TRUE);
                managerEdit = true;
            } else {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                            Constants.EDIT_MY_REVIEW_PERM_NAME, "Error.ReviewCommitted", null);
            }
        }

        // This variable determines if 'Save and Mark Complete' button has been clicked
        boolean commitRequested = "submit".equalsIgnoreCase(request.getParameter("save"));
        // This variable determines if Preview button has been clicked
        boolean previewRequested = "preview".equalsIgnoreCase(request.getParameter("save"));

        // Get the form defined for this action
        LazyValidatorForm reviewForm = (LazyValidatorForm) form;

        // Get form's fields
        String[] answers = (String[]) reviewForm.get("answer");
        Integer[] commentCounts = (Integer[]) reviewForm.get("comment_count");
        Map replies = (Map) reviewForm.get("comment");
        Map commentTypeIds = (Map) reviewForm.get("comment_type");
        FormFile[] files = (FormFile[]) reviewForm.get("file");

        // Uploaded files will be held here
        UploadedFile[] uploadedFiles = null;

        // Files won't be uploaded, if a mere Preview operation was requested
        if (!previewRequested) {
            StrutsRequestParser parser = new StrutsRequestParser();

            // Collect uploaded files and add them to adapter
            for (int i = 0; i < files.length; ++i) {
                if (files[i] != null && files[i].getFileName().trim().length() != 0) {
                    parser.AddFile(files[i]);
                }
            }

            // Obtain an instance of File Upload Manager
            FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);

            // Upload files to the file server
            FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
            // Get an information about uploaded files (an uploaded files' ID in particular)
            uploadedFiles = uploadResult.getUploadedFiles("file");
        }

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

        int itemIdx = 0;
        int fileIdx = 0;
        int uploadedFileIdx = 0;

        // If the review hasn't been created yet
        if (review == null) {
            // Create a convenient review editor
            ReviewEditor reviewEditor =
                new ReviewEditor(Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

            // Iterate over the scorecard template's questions,
            // so items will be created for every question
            for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
                Group group = scorecardTemplate.getGroup(groupIdx);
                for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                    Section section = group.getSection(sectionIdx);
                    for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx) {
                        Question question = section.getQuestion(questionIdx);

                        // Create review item
                        Item item = new Item();

                        // Populate the review item comments
                        int newCommentCount = populateItemComments(item, itemIdx, replies, commentTypeIds,
                                commentCounts[itemIdx].intValue(), commentTypes, myResource, managerEdit);

                        if (newCommentCount != commentCounts[itemIdx].intValue()) {
                            commentCounts[itemIdx] = new Integer(newCommentCount);
                        }

                        // Set required fields of the item
                        item.setAnswer(answers[itemIdx]);
                        item.setQuestion(question.getId());

                        // Handle uploads
                        if (!previewRequested && question.isUploadDocument()) {
                            if (fileIdx < files.length && files[fileIdx] != null &&
                                    files[fileIdx].getFileName().trim().length() != 0) {
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

                        ++itemIdx;
                    }
                }
            }

            // Finally, set required fields of the review
            reviewEditor.setAuthor(myResource.getId());
            reviewEditor.setSubmission(verification.getSubmission().getId());
            reviewEditor.setScorecard(scorecardTemplate.getId());

            review = reviewEditor.getReview();
        } else {
            for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
                Group group = scorecardTemplate.getGroup(groupIdx);
                for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                    Section section = group.getSection(sectionIdx);
                    for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx, ++itemIdx) {
                        // Get an item
                        Item item = review.getItem(itemIdx);

                        // Populate the review item comments
                        int newCommentCount = populateItemComments(item, itemIdx, replies, commentTypeIds,
                                commentCounts[itemIdx].intValue(), commentTypes, myResource, managerEdit);

                        if (newCommentCount != commentCounts[itemIdx].intValue()) {
                            commentCounts[itemIdx] = new Integer(newCommentCount);
                        }

                        // Update the answer
                        item.setAnswer(answers[itemIdx]);

                        // Handle uploads
                        if (!previewRequested && !managerEdit && section.getQuestion(questionIdx).isUploadDocument()) {
                            if (fileIdx < files.length && files[fileIdx] != null &&
                                    files[fileIdx].getFileName().trim().length() != 0) {
                                Upload oldUpload = null;
                                // If this item has already had uploaded file,
                                // it is going to be updated
                                if (item.getDocument() != null) {
                                    oldUpload = upMgr.getUpload(item.getDocument().longValue());
                                }

                                Upload upload = new Upload();

                                // Set fields of the new upload
                                upload.setOwner(myResource.getId());
                                upload.setParameter(uploadedFiles[uploadedFileIdx++].getFileId());
                                upload.setProject(project.getId());
                                upload.setUploadStatus(
                                        ActionsHelper.findUploadStatusByName(allUploadStatuses, "Active"));
                                upload.setUploadType(
                                        ActionsHelper.findUploadTypeByName(allUploadType, "Review Document"));

                                // Update and store old upload (if there was any)
                                if (oldUpload != null) {
                                    oldUpload.setUploadStatus(
                                            ActionsHelper.findUploadStatusByName(allUploadStatuses, "Deleted"));
                                    upMgr.updateUpload(oldUpload,
                                            Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                                }

                                // Save information about current upload
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

        boolean validationSucceeded = (commitRequested || managerEdit) ?
                validateGenericScorecard(request, scorecardTemplate, review, managerEdit) : true;
        // For Manager Edits this variable indicates whether recomputation of
        // final aggregated score for the submitter may be required
        boolean possibleFinalScoreUpdate = false;
        boolean possibleSubmissionStatusUpdate = false;

        // If the user has requested to complete the review
        if (validationSucceeded && (commitRequested || managerEdit)) {
            // Obtain an instance of CalculationManager
            CalculationManager scoreCalculator = new CalculationManager();
            // Compute scorecard's score
            float newScore = scoreCalculator.getScore(scorecardTemplate, review);
            // If score has been updated during Manager Edit, additional actions may need to be taken
            if ("Review".equals(reviewType) && managerEdit &&
                    (review.getScore() == null || review.getScore().floatValue() != newScore)) {
                possibleFinalScoreUpdate = true;
            }
            if ("Screening".equals(reviewType) && managerEdit &&
                    (review.getScore() == null || review.getScore().floatValue() != newScore)) {
            	possibleSubmissionStatusUpdate = true;
            }
            // Update scorecard's score
            review.setScore(new Float(newScore));
            if (review.getInitialScore() == null) {
            	review.setInitialScore(review.getScore());
            }
            // Set the completed status of the review
            if (commitRequested) {
                review.setCommitted(true);
            }
        } else if (previewRequested) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Put the review object into the bean (it may not always be there by default)
            verification.setReview(review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

            // Notify View page that this is actually a preview operation
            request.setAttribute("isPreview", Boolean.TRUE);
            // Forward to preview page
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Do not allow to save invalid manager's edits
        if (!managerEdit || validationSucceeded) {
            // Determine which action should be performed -- creation or updating
            if (verification.getReview() == null) {
                revMgr.createReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
            } else {
                revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
            }
        }

        // This operation will possibly update final aggregated score for the submitter
        if (possibleFinalScoreUpdate) {
            updateFinalAggregatedScore(request, project, phase, verification.getSubmission());
        }

        // This operation will possibly update submission status for the submitter
        if (possibleSubmissionStatusUpdate) {
        	updateSubmissionStatusScreeningScoreChanged(request, project, phases, review, verification.getSubmission());
        }

        if (validationSucceeded && commitRequested) {
            // Put the review object into the bean (it may not always be there by default)
            verification.setReview(review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

            // Place information about the final score into the request
            request.setAttribute("reviewScore", review.getScore());
            // Place review ID into the request
            request.setAttribute("rid", new Long(review.getId()));

            // Forward to the page that says that scorecard has been committed
            return mapping.findForward(Constants.REVIEW_COMMITTD_FORWARD_NAME);
        }

        if (!validationSucceeded) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Put the review object into the bean (it may not always be there by default)
            verification.setReview(review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);
            // Retrive some look-up data and store it into the request
            retreiveAndStoreReviewLookUpData(request);
            // Need to store uploaded file IDs, so that the user will be able to download them
            if (!managerEdit) {
                request.setAttribute("uploadedFileIds", collectUploadedFileIds(scorecardTemplate, review));
            }

            return mapping.getInputForward();
        }

        // Forward to project details page
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + verification.getProject().getId());
    }

    /**
     * This static method updates final score for the particular submitter. The score is updated
     * only in the case, there was originally such score for the submitter (i.e. the project in the
     * Aggregation or past-Aggregation phase). This operation is needed in the situation when
     * Manager edits review scorecard and this operation results in scorecard's score change.
     * Current version of the method does not take into account the possibility of changing places
     * for submitters.
     *
     * @param request
     *            the http request. Used internally by some helper functions.
     * @param project
     *            a project the submission was originally made for.
     * @param reviewPhase
     *            phase of type &quot;Review&quot; used internally to retrieve reviewers' resources.
     * @param sub
     *            a submission in question, i.e. the one that needs its final score updated.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws BaseException
     *             if any unexpected error occurs during final score update.
     */
    private static void updateFinalAggregatedScore(HttpServletRequest request, Project project, Phase reviewPhase, Submission sub)
        	throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(project, "project");
        ActionsHelper.validateParameterNotNull(reviewPhase, "reviewPhase");
        ActionsHelper.validateParameterNotNull(sub, "submission");

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        // Get a resource identificating the submitter for this review
        Resource submitter = resMgr.getResource(sub.getUpload().getOwner());

        // OrChange - Get the final score from the submission instead of the Resource
        // Get final aggregated score for this submitter, if any
        Double finalScore = sub.getFinalScore();

        // If there is no final (post Appeals Response) score for the submitter yet,
        // there is nothing to do anymore
        log.log(Level.INFO, "Final Score: " + finalScore);
        if (finalScore == null) {
            return;
        }

        // Build a filter to select resources (i.e. reviewers) for Review phase
        Filter filterPhase = ResourceFilterBuilder.createPhaseIdFilter(reviewPhase.getId());
        // Retrieve reviewers that did the reviews
        Resource[] reviewers = resMgr.searchResources(filterPhase);

        if (reviewers.length == 0) {
            // Impossible situation, but a safety-check prevents application from crashing
            return;
        }

        List reviewerIds = new ArrayList();

        for (int i = 0; i < reviewers.length; ++i) {
            reviewerIds.add(new Long(reviewers[i].getId()));
        }

        // Prepare filters
        Filter filterReviewers = new InFilter("reviewer", reviewerIds);
        Filter filterSubmission = new EqualToFilter("submission", new Long(sub.getId()));
        Filter filterCommitted = new EqualToFilter("committed", new Integer(1));

        // Prepare final combined filter
        Filter filter = new AndFilter(Arrays.asList(new Filter[] {filterReviewers, filterSubmission, filterCommitted}));
        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager(request);
        // Retrieve an array of reviews
        Review[] reviews = revMgr.searchReviews(filter, true);

        if (reviews.length == 0) {
            // Another generally impossible situation guarded by a safety-check just in case
            return;
        }

        float[] scores = new float[reviews.length];
        // Prepare needed parameters for score aggregator
        for (int i = 0; i < reviews.length; ++i) {
            scores[i] = (reviews[i].getScore() != null) ? reviews[i].getScore().floatValue() : 0.0f;
        }

        // Obtain an instance of Review Score Aggregator
        ReviewScoreAggregator aggregator = ActionsHelper.createScoreAggregator(request);
        // Aggregate scores for the current submission
        AggregatedSubmission[] aggrSubm = aggregator.aggregateScores(new float[][] {scores});
        float newScore = aggrSubm[0].getAggregatedScore();

        float oldScore = Float.parseFloat(String.valueOf(finalScore));
       
        if (newScore == oldScore) {
        	// score is not changed
        	return;
        }
        
        // OrChange - Get the placement from the submission instead of the Resource
        Long oldPlacement = sub.getPlacement() == null ? -1 : sub.getPlacement();
        
        
        // Old Code
        // Object temp = submitter.getProperty("Placement");
        // long oldPlacement = temp == null ? -1 : Long.parseLong(temp.toString());
        // Old Code Ends
        
        Object userId = submitter.getProperty("External Reference ID");
        
        UploadManager upMgr = ActionsHelper.createUploadManager(request);

        // OrChange - Update the final score in the Submission table.
        sub.setFinalScore(new Double(newScore));
        
        // update the final score
        upMgr.updateSubmission(sub, String.valueOf(AuthorizationHelper.getLoggedInUserId(request)));
        
        // Old Code
        // // Update this submitter's final score with aggregated one
        // submitter.setProperty("Final Score", String.valueOf(newScore));
        //        
        //        
        // // Store updated information in the database
        // resMgr.updateResource(submitter, String.valueOf(AuthorizationHelper.getLoggedInUserId(request)));
        // Old Code Ends

        // Retrieve all reviewed submissions to reset placement/submission status if need
        Submission[] submissions = ActionsHelper.searchReviewedSubmissions(request, project);
        List submissionIds = new ArrayList();

        for (int i = 0; i < submissions.length; ++i) {
        	submissionIds.add(new Long(submissions[i].getId()));
        }

        Filter filterSubmissions = new InFilter("submission", submissionIds);
        filter = new AndFilter(Arrays.asList(new Filter[] {filterReviewers, filterSubmissions, filterCommitted}));
        reviews = revMgr.searchReviews(filter, true);
        
        // Retrieve minScore
        ScorecardManager scMgr = ActionsHelper.createScorecardManager(request);
        float minScore =  ActionsHelper.getScorecardMinimumScore(scMgr, reviews[0]);

        //create array to hold scores from all reviewers for all submissions
        com.topcoder.management.review.scoreaggregator.Submission[] submissionScores =
            new com.topcoder.management.review.scoreaggregator.Submission[submissions.length];

        // for each submission, populate scores array to use with review score aggregator.
        for (int iSub = 0; iSub < submissions.length; iSub++) {
            long subId = submissions[iSub].getId();
            List scoresList = new ArrayList();

            //Match the submission with its reviews
            for (int j = 0; j < reviews.length; j++) {
                if (subId == reviews[j].getSubmission()) {
                    //get review score
                    scoresList.add(reviews[j].getScore());
                }
            }

            //create float array
            scores = new float[scoresList.size()];

            for (int iScore = 0; iScore < scores.length; iScore++) {
                scores[iScore] = ((Float) scoresList.get(iScore)).floatValue();
            }

            submissionScores[iSub] = new com.topcoder.management.review.scoreaggregator.Submission(subId, scores);
        }

        //this will hold as many elements as submissions
        AggregatedSubmission[] aggregations = aggregator.aggregateScores(submissionScores);
        RankedSubmission[] placements = aggregator.calcPlacements(aggregations);

        // check if placement is changed
        long newPlacement = -1;
        for (int i = 0; i < placements.length; i++) {
        	if (placements[i].getId() == sub.getId()) {
        		newPlacement = placements[i].getRank();
        	}
        }

        if (newPlacement != -1 && newPlacement == oldPlacement) {
        	// placement is not changed

            // Check if submission status is changed
            if ((oldScore < minScore && newScore < minScore) ||
            		(oldScore >= minScore && newScore >= minScore)) {
            	// the submission status is not changed
            	return;
            }
        }

        //status objects
        SubmissionStatus failedStatus = ActionsHelper.getSubmissionStatus(request,
                "Failed Review");
        SubmissionStatus noWinStatus = ActionsHelper.getSubmissionStatus(request,
                "Completed Without Win");
        SubmissionStatus activeStatus = ActionsHelper.getSubmissionStatus(request,
        		"Active");

        Resource winningSubmitter = null;
        Resource runnerUpSubmitter = null;
    	

        //again iterate over submissions to set the initial score and placement
        for (int iSub = 0; iSub < placements.length; iSub++) {
            RankedSubmission rankedSubmission = placements[iSub];
            rankedSubmission = breakTies(rankedSubmission, submissions, placements);
            Submission submission = getSubmissionById(submissions, rankedSubmission.getId());
            float aggScore = rankedSubmission.getAggregatedScore();
            int placement = rankedSubmission.getRank();

            // OrChange - Update placement
            // update submitter's Placement
            submission.setPlacement(new Long(placement));

            long submitterId = submission.getUpload().getOwner();
            submitter = resMgr.getResource(submitterId);
            // Old Code
            // submitter.setProperty("Placement", String.valueOf(placement));
            // Old Code Ends

            SubmissionStatus newStatus = null; 
            	
            //if failed review, then update the status
            if (aggScore < minScore) {
            	newStatus = failedStatus;
            } else {
                //if not winner, update submission status
                if (placement != 1) {
                	newStatus = noWinStatus;
                } else {
                	newStatus = activeStatus;
                }
            }

            // submission status is changed
            if (!newStatus.equals(submission.getSubmissionStatus())) {
                submission.setSubmissionStatus(newStatus);
                upMgr.updateSubmission(submission, String.valueOf(AuthorizationHelper.getLoggedInUserId(request)));            	
            }

            //cache winning and runner up submitter.
            if (placement == 1 && aggScore >= minScore) {
                winningSubmitter = submitter;
            } else if (placement == 2 && aggScore >= minScore) {
                runnerUpSubmitter = submitter;
            }

            //persist the change
            resMgr.updateResource(submitter, String.valueOf(AuthorizationHelper.getLoggedInUserId(request)));
        } //end for

        ProjectManager projectManager = ActionsHelper.createProjectManager(request);

        // if there is a winner
        if (winningSubmitter != null) {
            //Set project properties to store the winner and the runner up
            //Get the project instance
            Object winnerExtId = winningSubmitter.getProperty("External Reference ID");
            project.setProperty("Winner External Reference ID", winnerExtId);
        } else {
            project.setProperty("Winner External Reference ID", null);
        }

        //if there is a runner up
        if (runnerUpSubmitter != null) {
            Object runnerExtId = runnerUpSubmitter.getProperty("External Reference ID");
            project.setProperty("Runner-up External Reference ID", runnerExtId);
        }else {
            project.setProperty("Runner-up External Reference ID", null);
        }

        //update the project
        projectManager.updateProject(project, "Update the winner and runner up.",
        		String.valueOf(AuthorizationHelper.getLoggedInUserId(request)));

        ActionsHelper.resetProjectResultWithChangedScores(project.getId(), userId);
    }

    /**
     * Return suitable submission for given submissionId.
     * 
     * @param submissions the submission array
     * @param submissionId the submissionId
     * @return submission
     * @throws BaseException
     */
    private static Submission getSubmissionById(Submission[] submissions, long submissionId)
        throws BaseException {
        for (int i = 0; i < submissions.length; i++) {
            if (submissions[i].getId() == submissionId) {
                return submissions[i];
            }
        }
        throw new PhaseHandlingException("submissions not found for submissionId: " + submissionId);
    }

    /**
     * Break ties by submission timestamp
     * @param submission the submission to calculate
     * @param submissions all the submission records
     * @param placements all the ranked submission records
     * @return the submission with fixed placement
     * @throws BaseException
     */
    private static RankedSubmission breakTies(RankedSubmission submission,
            Submission[] submissions, RankedSubmission[] placements) throws BaseException {
        int rank = submission.getRank();
        Date timestamp = getSubmissionById(submissions, submission.getId())
                .getUpload().getCreationTimestamp();
        for (int i = 0; i < placements.length; ++i) {
            if (placements[i].getRank() == submission.getRank()) {
                Submission tie = getSubmissionById(submissions, placements[i].getId());
                if (tie.getUpload().getCreationTimestamp().before(timestamp)) {
                    ++rank;
                }
            }
        }

        return new RankedSubmission(submission, rank);
    }

    /**
     * This static method submission status for the particular submitter. 
     *
     * @param request
     *            the http request. Used internally by some helper functions.
     * @param project
     *            a project the submission was originally made for.
     * @param phases
     *            phases of project.
     * @param submission
     *            a submission in question, i.e. the one that needs its final score updated.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws BaseException
     *             if any unexpected error occurs during final score update.
     */
    private static void updateSubmissionStatusScreeningScoreChanged(
            HttpServletRequest request, Project project, Phase[] phases, Review review, Submission submission)
        throws BaseException {
        // Check if current open phase is Review
        boolean isReviewPhase = false;
        for (int i = 0; i < phases.length; i++) {
        	if ("Review".equals(phases[i].getPhaseType().getName())) {
        		isReviewPhase = "Open".equals(phases[i].getPhaseStatus().getName());
        		break;
        	}

        	// If screening phase is open, then needn't change anything
        	if ("Screening".equals(phases[i].getPhaseType().getName())) {
        		if ("Open".equals(phases[i].getPhaseStatus().getName())) {
        			return;
        		}
        	}
        }

        if (!isReviewPhase) {
        	// Current we only process while review phase is open
        	return;
        }

        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        ScorecardManager scorecardManager = ActionsHelper.createScorecardManager(request);

        // Get minimum score
        float minimumScore = 75;        
        Scorecard[] scoreCards = scorecardManager.getScorecards(new long[]{review.getScorecard()}, false);
        
        if (scoreCards.length > 0) {
        	minimumScore = scoreCards[0].getMinScore();
        }

        boolean passScreening = review.getScore().floatValue() >= minimumScore;

        // Retrieve submission status
        SubmissionStatus failedScreening = null;
        SubmissionStatus active = null;
        SubmissionStatus failedReview = null;
        SubmissionStatus[] statuses = upMgr.getAllSubmissionStatuses();

        for (int i = 0; i < statuses.length; i++) {
        	if ("Failed Screening".equals(statuses[i].getName())) {
        		failedScreening = statuses[i];
        	} else if ("Failed Review".equals(statuses[i].getName())) {
        		failedReview = statuses[i];
        	} else if ("Active".equals(statuses[i].getName())) {
        		active = statuses[i];
        	}
        }

        if (!passScreening) {
        	submission.setSubmissionStatus(failedScreening);
        } else {
        	// pass screening
        	if (isReviewPhase) {
        		submission.setSubmissionStatus(active);
        	} else {
        		submission.setSubmissionStatus(failedReview);
        	}
        }
        upMgr.updateSubmission(submission, String.valueOf(AuthorizationHelper.getLoggedInUserId(request)));
    }

    /**
     * TODO: Document this method
     *
     * @return
     * @param myResource
     * @param item
     * @param itemIdx
     * @param replies
     * @param commentTypeIds
     * @param commentCount
     * @param commentTypes
     * @param managerEdit
     * @throws IllegalArgumentException
     *             if any of the parameters <code>item</code>, <code>replies</code>,
     *             <code>commentTypeIds</code>, <code>commentTypes</code>, or
     *             <code>myResource</code> is <code>null</code>, or if parameters
     *             <code>itemIdx</code> or <code>commentCount</code> are negative.
     */
    private static int populateItemComments(Item item, int itemIdx, Map replies, Map commentTypeIds,
            int commentCount, CommentType[] commentTypes, Resource myResource, boolean managerEdit) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(itemIdx, "itemIdx", 0, Integer.MAX_VALUE);
        ActionsHelper.validateParameterNotNull(replies, "replies");
        ActionsHelper.validateParameterNotNull(commentTypeIds, "commentTypeIds");
        ActionsHelper.validateParameterInRange(commentCount, "commentCount", 0, Integer.MAX_VALUE);
        ActionsHelper.validateParameterNotNull(myResource, "myResource");

        int newCommentCount = 1;

        int commentIdx = 0;
        Comment currentComment = null;

        for (int i = 0; i < commentCount; ++i) {
            if (currentComment == null) {
                for (;commentIdx < item.getNumberOfComments();) {
                    // Get a comment for the current iteration
                    Comment comment = item.getComment(commentIdx++);

                    // Locate next manager's comment for Manager edits or
                    // next reviewer's comment for non-Manager edits
                    if ((managerEdit && ActionsHelper.isManagerComment(comment)) ||
                            (!managerEdit && ActionsHelper.isReviewerComment(comment))) {
                        currentComment = comment;
                        break;
                    }
                }
            }

            String commentKey = itemIdx + "." + (i + 1);
            CommentType commentType;

            if (managerEdit) {
                commentType = ActionsHelper.findCommentTypeByName(commentTypes, "Manager Comment");
            } else {
                commentType = ActionsHelper.findCommentTypeById(
                        commentTypes, Long.parseLong((String) commentTypeIds.get(commentKey)));
            }
            // Check that correct comment type ID has been specified
            // (user may intentionally submit malformed form data)
            if (commentType == null) {
                replies.remove(commentKey);
                commentTypeIds.remove(commentKey);
                continue;
            }

            // Get user's reply, i.e. comment's text
            String reply = (String) replies.get(commentKey);
            // Do not let user's reply to be null
            if (reply == null) {
                reply = "";
            }

            // Skip empty comments of type "Comment" or any empty comment for Manager edits
            if ((managerEdit || commentType.getName().equalsIgnoreCase("Comment")) && reply.trim().length() == 0) {
                replies.remove(commentKey);
                commentTypeIds.remove(commentKey);
                continue;
            }

            // Determine if new comment must be added
            boolean newComment = false;
            // If new comment needs to be added, create new Comment object
            if (currentComment == null) {
                commentIdx = Integer.MAX_VALUE;
                currentComment = new Comment();
                newComment = true;
            }

            String updatedCommentKey = itemIdx + "." + newCommentCount;

            // Update form's fields (so they will be up to date in case scorecard fails validation)
            replies.put(updatedCommentKey, reply);
            if (!managerEdit) {
                commentTypeIds.put(updatedCommentKey, new Long(commentType.getId()));
            }
            // Increase the counter of the processed comments
            ++newCommentCount;

            // Do not update unchanged comments (skip them)
            if (!newComment && reply.equals(currentComment.getComment()) &&
                    currentComment.getCommentType().getId() == commentType.getId()) {
                // Indicate that there are no current comment anymore,
                // so it will be located or created during next iteration
                currentComment = null;
                continue;
            }

            // Update the author of the comment
            currentComment.setAuthor(myResource.getId());
            // Set (possibly new) comment's text
            currentComment.setComment(reply);
            // Set (possibly new) comment's type
            currentComment.setCommentType(commentType);

            // If new comment needs to be added to review's item, add it
            if (newComment) {
                item.addComment(currentComment);
            }
            // Indicate that there are no current comment anymore,
            // so it will be located or created during next iteration
            currentComment = null;
        }

        // If last current comment was not used, the most likely it is needed to be deleted
        if (currentComment != null) {
            --commentIdx;
        }

        // At this point there may exist excess comments, which need to be removed.
        // So, remove them, starting from the last
        for (int i = item.getNumberOfComments() - 1; i >= commentIdx; --i) {
            // Get a comment for the current iteration
            Comment comment = item.getComment(i);

            if ((managerEdit && ActionsHelper.isManagerComment(comment)) ||
                    (!managerEdit && ActionsHelper.isReviewerComment(comment))) {
                item.removeComment(comment);
            }
        }

        final int properCommentCount = (managerEdit) ? 1 : DEFAULT_COMMENTS_NUMBER;

        for (;newCommentCount <= properCommentCount; ++newCommentCount) {
            String emptyCommentKey = itemIdx + "." + newCommentCount;
            replies.put(emptyCommentKey, "");
            commentTypeIds.put(emptyCommentKey,
                    new Long(ActionsHelper.findCommentTypeByName(commentTypes, "Comment").getId()));
        }

        return newCommentCount - 1;
    }

	/**
     * TODO: Document it
     *
     * @return
     * @param mapping
     * @param request
     * @param reviewType
     * @throws BaseException
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or if <code>reviewType</code>
     *             parameter is empty string, or if that parameter contains the value that does not
     *             match either <code>&quot;Screening&quot;</code>, or
     *             <code>&quot;Review&quot;</code>, or <code>&quot;Approval&quot;</code>.
     */
    private ActionForward viewGenericReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, String reviewType)
        throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(mapping, "mapping");
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterStringNotEmpty(reviewType, "reviewType");

        String permName;
        String phaseName;
        String scorecardTypeName;

        // Determine permission name and phase name from the review type
        if (reviewType.equals("Screening")) {
            permName = Constants.VIEW_SCREENING_PERM_NAME;
            phaseName = Constants.SCREENING_PHASE_NAME;
            scorecardTypeName = "Screening";
        } else if (reviewType.equals("Review")) {
            permName = Constants.VIEW_ALL_REVIEWS_PERM_NAME;
            phaseName = Constants.REVIEW_PHASE_NAME;
            scorecardTypeName = "Review";
        } else if (reviewType.equals("Approval")) {
            permName = Constants.VIEW_APPROVAL_PERM_NAME;
            phaseName = Constants.APPROVAL_PHASE_NAME;
            scorecardTypeName = "Client Review";
        } else {
            throw new IllegalArgumentException("Incorrect review type specified: " + reviewType + ".");
        }

        // Verify that certain requirements are met before proceeding with the Action
        CorrectnessCheckResult verification = checkForCorrectReviewId(mapping, request, permName);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(request, false), project);
        // Get active (opened) phases names
        List activePhases = new ArrayList();
        for (int i = 0; i < phases.length; i++) {
            if (phases[i].getPhaseStatus().getName().equals(PhaseStatus.OPEN.getName())) {
                activePhases.add(phases[i].getPhaseType().getName());
            }
        }

        // Get a phase with the specified name
        Phase phase = ActionsHelper.getPhase(phases, false, phaseName);

        // Get "My" resource for the appropriate phase (for reviewers actually)
        Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        // If no resource found for particular phase, try to find resource without phase assigned
        if (myResource == null) {
            myResource = ActionsHelper.getMyResourceForPhase(request, null);
        }

        /*
         *  Verify that user has the permission to view the review
         */
        boolean isAllowed = false;
        if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES) ||
                AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME) ||
                AuthorizationHelper.hasUserRole(request, Constants.OBSERVER_ROLE_NAME)) {
            // User is manager or observer
            isAllowed = true;
        } else if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_REVIEWER_REVIEWS_PERM_NAME) &&
                    myResource != null && verification.getReview().getAuthor() == myResource.getId()) {
            // User is authorized to view review authored by him
            isAllowed = true;
        } else if (myResource != null && verification.getSubmission().getUpload().getOwner() == myResource.getId()) {
            // User is authorized to view review for his submission (when not in Review or in Appeals)
            if (reviewType != "Review" || !activePhases.contains(Constants.REVIEW_PHASE_NAME) ||
                    activePhases.contains(Constants.APPEALS_PHASE_NAME)) {
                isAllowed = true;
            }
        } else if (AuthorizationHelper.hasUserPermission(request, permName)) {
            if (reviewType == "Review") {
                // User is authorized to view all reviews (when not in Review, Appeals or Appeals Response)
                if (!activePhases.contains(Constants.REVIEW_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                    isAllowed = true;
                }
            } else if (reviewType == "Screening") {
            	if (!activePhases.contains(Constants.REVIEW_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                    isAllowed = true;
                } else {
                	//if any of those phases are open only, a user can see the screening if he is not a submitter 
                	isAllowed = !AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME);
                }
            } else {
            	isAllowed = true;
            }
        }

        if (!isAllowed) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permName, "Error.NoPermission", Boolean.FALSE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Obtain an instance of Scorecard Manager
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager(request);
        // Retrieve a scorecard template for this review
        Scorecard scorecardTemplate = scrMgr.getScorecard(verification.getReview().getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase(scorecardTypeName)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    permName, "Error.ReviewTypeIncorrect", null);
        }
        // Make sure that the user is not trying to view unfinished review
        if (!verification.getReview().isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    permName, "Error.ReviewNotCommitted", null);
        } else {
            // If user has a Manager role, put special flag to the request,
            // indicating that we can edit the review
            if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
                request.setAttribute("canEditScorecard", Boolean.TRUE);
            }
        }

        // Check that the type of the review is Review,
        // as appeals and responses to them can only be placed to that type of scorecard
        if (scorecardTypeName.equals("Review")) {
            boolean canPlaceAppeal = false;
            boolean canPlaceAppealResponse = false;

            // Check if user can place appeals or appeal responses
            if (activePhases.contains(Constants.APPEALS_PHASE_NAME) &&
                    AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPEAL_PERM_NAME)) {
                // Can place appeal, put an appropriate flag to request
                request.setAttribute("canPlaceAppeal", Boolean.TRUE);
                canPlaceAppeal = true;
            } else if (activePhases.contains(Constants.APPEALS_RESPONSE_PHASE_NAME)  &&
                    AuthorizationHelper.hasUserPermission(request, Constants.PERFORM_APPEAL_RESP_PERM_NAME)) {
                // Can place response, put an appropriate flag to request
                request.setAttribute("canPlaceAppealResponse", Boolean.TRUE);
                canPlaceAppealResponse = true;
            }

            if (canPlaceAppeal || canPlaceAppealResponse) {
                // Gather the appeal statuses and item answers
                String[] appealStatuses = new String[verification.getReview().getNumberOfItems()];
                String[] answers = new String[verification.getReview().getNumberOfItems()];
                // Message Resources to be used for the Action
                MessageResources messages = getResources(request);
                for (int i = 0; i < appealStatuses.length; i++) {
                    Comment appeal = getCommentAppeal(verification.getReview().getItem(i).getAllComments());
                    Comment response = getCommentAppealResponse(verification.getReview().getItem(i).getAllComments());
                    if (appeal != null && response == null) {
                        appealStatuses[i] = messages.getMessage("editReview.Appeal.Unresolved");
                    } else if (appeal != null) {
                        appealStatuses[i] = messages.getMessage("editReview.Appeal.Resolved." + appeal.getExtraInfo());
                    } else {
                        appealStatuses[i] = "";
                    }

                    answers[i] = verification.getReview().getItem(i).getAnswer().toString();
                }
                // Set review item answers form property
                ((LazyValidatorForm) form).set("answer", answers);
                // Place appeal statuses to request
                request.setAttribute("appealStatuses", appealStatuses);

                // Retrieve some look-up data and store it into the request
                retreiveAndStoreReviewLookUpData(request);
            }
        }

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * TODO: Write documentation for this method
     *
     * @return
     * @param scorecardTemplate
     * @param review
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    private static Long[] collectUploadedFileIds(Scorecard scorecardTemplate, Review review) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(scorecardTemplate, "scorecardTemplate");
        ActionsHelper.validateParameterNotNull(review, "review");

        int uploadsCount = ActionsHelper.getScorecardUploadsCount(scorecardTemplate);
        if (uploadsCount == 0) {
            return null; // No upload IDs
        }

        Long[] uploadedFileIds = new Long[uploadsCount];
        int itemIdx = 0;
        int fileIdx = 0;

        for (int iGroup = 0; iGroup < scorecardTemplate.getNumberOfGroups(); ++iGroup) {
            Group group = scorecardTemplate.getGroup(iGroup);
            for (int iSection = 0; iSection < group.getNumberOfSections(); ++iSection) {
                Section section = group.getSection(iSection);
                for (int iQuestion = 0; iQuestion < section.getNumberOfQuestions(); ++iQuestion, ++itemIdx) {
                    Question question = section.getQuestion(iQuestion);                    
                    if (question.isUploadDocument()) {
                        uploadedFileIds[fileIdx++] = review.getItem(itemIdx).getDocument();
                    }
                }
            }
        }

        return uploadedFileIds;
    }

    /**
     * TODO: Write documentation for this method
     *
     * @return
     * @param request
     * @param scorecardTemplate
     * @param review
     * @param managerEdit
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    private static boolean validateGenericScorecard(HttpServletRequest request,
            Scorecard scorecardTemplate, Review review, boolean managerEdit) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(scorecardTemplate, "scorecardTemplate");
        ActionsHelper.validateParameterNotNull(review, "review");

        int itemIdx = 0;
        int fileIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx, ++itemIdx) {
                    Question question = section.getQuestion(questionIdx);
                    Item item = review.getItem(itemIdx);

                    validateScorecardItemAnswer(request, question, item, itemIdx);

                    // Skip the rest of the validation for Manager edits
                    if (managerEdit) {
                        continue;
                    }

                    validateScorecardComments(request, item, itemIdx);
                    if (question.isUploadDocument()) {
                        validateScorecardItemUpload(request, question, item, fileIdx++);
                    }
                }
            }
        }

        return !ActionsHelper.isErrorsPresent(request);
    }

    /**
     * This static method validates Aggregation scorecard. In order to pass validation, Aggregation
     * must have all its aggregate functions to be specified. Per-item comments ae not required.
     *
     * @return <code>true</code> if aggregation scorecard passes validation, <code>false</code>
     *         if it fails it.
     * @param request
     *            an <code>HttpServletRequest</code> object where validation error messages will
     *            be placed to in case there are any.
     * @param scorecardTemplate
     *            a scorecard template of type &quot;Review&quot; that was used to generate the
     *            aggregation scorecard to be validated.
     * @param aggregation
     *            an aggregation scorecard to be validated.
     * @param managerEdit
     *            specifies whether it was manager who edited the scorecard.
     * @throws IllegalArgumentException
     *             if parameters <code>request</code>, <code>scorecardTemplate</code>, or
     *             <code>aggregation</code> are <code>null</code>.
     */
    private static boolean validateAggregationScorecard(
            HttpServletRequest request, Scorecard scorecardTemplate, Review aggregation, boolean managerEdit) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(scorecardTemplate, "scorecardTemplate");
        ActionsHelper.validateParameterNotNull(aggregation, "aggregation");

        final int numberOfItems = aggregation.getNumberOfItems();
        int itemIdx = 0;
        int commentIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx, ++itemIdx) {
                    Question question = section.getQuestion(questionIdx);
                    long questionId = question.getId();

                    for (int i = 0; i < numberOfItems; ++i) {
                        if (aggregation.getItem(i).getQuestion() != questionId) {
                            continue;
                        }

                        // Get a review's item
                        Item item = aggregation.getItem(i);

                        // Validate item's aggregate functions
                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            Comment comment = item.getComment(j);
                            String commentType = comment.getCommentType().getName();

                            if (commentType.equalsIgnoreCase("Comment") || commentType.equalsIgnoreCase("Required") ||
                                    commentType.equalsIgnoreCase("Recommended")) {
                                validateAggregateFunction(request, item.getComment(j), commentIdx++);
                            }
                            /* Request from David Messinger [11/06/2006]:
                               No need to verify presence of comments
                            if (commentType.equalsIgnoreCase("Aggregation Comment")) {
                                validateScorecardComment(request, comment, "aggregator_response[" + itemIdx + "]");
                            }*/
                        }
                    }
                }
            }
        }

        return !ActionsHelper.isErrorsPresent(request);
    }

    /**
     * This static method validates Aggregation Review scorecard. This type of scorecard is
     * considered valid if all items in the Aggregation have been accepted by user, or if comments
     * have been entered for rejected items.
     *
     * @return <code>true</code> if scorecard passes validation, <code>false</code> if it does
     *         not.
     * @param request
     *            an <code>HttpServletRequest</code> object where validation error messages will
     *            be placed to in case there are any.
     * @param scorecardTemplate
     *            a scorecard template of type &quot;Review&quot; that was used to generate the
     *            aggregation review scorecard to be validated.
     * @param aggregationReview
     *            an aggregation review scorecard to be validated.
     * @param authorId
     *            an ID of the resource which was used to update the aggregation review scorecard
     *            that needs validation.
     * @throws IllegalArgumentException
     *             if any of the <code>request</code>, <code>scorecardTemplate</code>, or
     *             <code>aggregationReview</code> parameters are <code>null</code>, or if
     *             <code>authorId</code> parameter is zero or negative.
     */
    private static boolean validateAggregationReviewScorecard(
            HttpServletRequest request, Scorecard scorecardTemplate, Review aggregationReview, long authorId) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(scorecardTemplate, "scorecardTemplate");
        ActionsHelper.validateParameterNotNull(aggregationReview, "aggregationReview");
        ActionsHelper.validateParameterPositive(authorId, "authorId");

        final int numberOfItems = aggregationReview.getNumberOfItems();
        int itemIdx = 0;

        for (int groupIdx = 0; groupIdx < scorecardTemplate.getNumberOfGroups(); ++groupIdx) {
            Group group = scorecardTemplate.getGroup(groupIdx);
            for (int sectionIdx = 0; sectionIdx < group.getNumberOfSections(); ++sectionIdx) {
                Section section = group.getSection(sectionIdx);
                for (int questionIdx = 0; questionIdx < section.getNumberOfQuestions(); ++questionIdx) {
                    Question question = section.getQuestion(questionIdx);
                    long questionId = question.getId();

                    for (int i = 0; i < numberOfItems; ++i) {
                        if (aggregationReview.getItem(i).getQuestion() != questionId) {
                            continue;
                        }

                        // Get a review's item
                        Item item = aggregationReview.getItem(i);

                        // Validate item's Accept/Reject status
                        for (int j = 0; j < item.getNumberOfComments(); ++j) {
                            // Get a comment for the current iteration
                            Comment comment = item.getComment(j);

                            // Skip unneeded comments
                            if (!ActionsHelper.isAggregationReviewComment(comment)) {
                                continue;
                            }
                            // Skip comments from other people
                            if (comment.getAuthor() != authorId) {
                                continue;
                            }

                            String extraInfo = (String) comment.getExtraInfo();
                            String commentText = comment.getComment();

                            if (extraInfo.equalsIgnoreCase("Reject") &&
                                    (commentText == null || commentText.trim().length() == 0)) {
                                ActionsHelper.addErrorToRequest(request, "reject_reason[" + itemIdx + "]",
                                        "Error.saveAggregationReview.RejectReason.Absent");
                            }
                        }
                        ++itemIdx;
                    }
                }
            }
        }

        return !ActionsHelper.isErrorsPresent(request);
    }

    /**
     * This static method validates Final Review scorecard. This type of scorecard is considered
     * valid if all reviewers' notes have been marked as &#39;Fixed&#39;, or if comments have been
     * entered for notes marked as &#39;Not&#160;Fixed&#39;.
     *
     * @return <code>true</code> if scorecard passes validation, <code>false</code> if it does
     *         not.
     * @param request
     *            an <code>HttpServletRequest</code> object where validation error messages will
     *            be placed to in case there are any.
     * @param scorecardTemplate
     *            a scorecard template of type &quot;Review&quot; that was used to generate the
     *            aggregation review scorecard to be validated.
     * @param finalReview
     *            a final review scorecard to be validated.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
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
                            if (!notFixed || !required) {
                                break; // Everything's good
                            }

                            String commentText = comment.getComment();

                            if (commentText == null || commentText.trim().length() == 0) {
                                ActionsHelper.addErrorToRequest(request, "final_comment[" + itemIdx + "]",
                                        "Error.saveFinalReview.Response.Absent");
                            }
                            break;
                        }
                    }
                }
            }
        }

        return !ActionsHelper.isErrorsPresent(request);
    }

    /**
     * TODO: Write documentation for this method
     *
     * @return
     * @param request
     * @param question
     * @param item
     * @param answerNum
     * @throws IllegalArgumentException
     *             if <code>request</code>, <code>question</code>, or <code>item</code> parameters are
     *             <code>null</code>, or if <code>answerNum</code> parameter is negative (less
     *             than zero).
     */
    private static boolean validateScorecardItemAnswer(
            HttpServletRequest request, Question question, Item item, int answerNum) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(question, "question");
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(answerNum, "answerNum", 0, Integer.MAX_VALUE);

        String errorKey = "answer[" + answerNum + "]";

        // Validate that answer is not null
        if (item.getAnswer() == null || !(item.getAnswer() instanceof String)) {
            ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.Absent");
            return false;
        }

        String answer = (String) item.getAnswer();

        // Validate that answer is not empty
        if (answer.trim().length() == 0) {
            ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.Absent");
            return false;
        }

        // Success indicator
        boolean success = true;
        // Get a type of the question for the current answer
        String questionType = question.getQuestionType().getName();

//        if (questionType.equalsIgnoreCase("Scale (1-4)") || questionType.equalsIgnoreCase("Scale (1-10)") || 
//        		questionType.equalsIgnoreCase("Scale (0-9)") || questionType.equalsIgnoreCase("Scale (0-3)") ||
//        		questionType.equalsIgnoreCase("Scale (0-4)")) {
//            if (!(correctAnswers.containsKey(answer) && correctAnswers.get(answer).equals(questionType))) {
//                ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.Incorrect");
//                success = false;
//            }
        if (correctAnswers.containsKey(questionType)) {
			if (!(correctAnswers.get(questionType).contains(answer))) {
				ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.Incorrect");
				success = false;
			}
        } else if (questionType.equalsIgnoreCase("Test Case")) {
            String[] answers = answer.split("/");
            // The number of answers for Testcase type of question must be exactly 2
            if (answers.length < 2) {
                ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.TestCase.LessTwo");
            } else if (answers.length > 2) {
                ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.TestCase.GreaterTwo");
            } else {
                try {
                    // Try to convert strings to integer value (and validate whether they are convertible)
                    int answer1 = Integer.parseInt(answers[0], 10);
                    int answer2 = Integer.parseInt(answers[1], 10);

                    // Validate some more circumstances
                    if (answer1 < 0 || answer2 < 0) {
                        ActionsHelper.addErrorToRequest(
                                request, errorKey, "Error.saveReview.Answer.TestCase.Negative");
                        success = false;
                    } else if (answer1 > answer2) {
                        ActionsHelper.addErrorToRequest(
                                request, errorKey, "Error.saveReview.Answer.TestCase.FirstGreaterSecond");
                        success = false;
                    }
                } catch (NumberFormatException nfe) {
                    // eat the exception and report about validation error
                    ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.TestCase.NotInt");
                    success = false;
                }
            }
        } else if (questionType.equalsIgnoreCase("Yes/No")) {
            // For 'Yes/No' type of question the two possible values for answer are either "0" or "1"
            if (!(answer.equals("0") || answer.equals("1"))) {
                ActionsHelper.addErrorToRequest(request, errorKey, "Error.saveReview.Answer.Incorrect");
                success = false;
            }
        }

        return success;
    }

    /**
     * TODO: Write documentation for this method
     *
     * @return
     * @param request
     * @param item
     * @param itemNum
     * @throws IllegalArgumentException
     *             if <code>request</code> or <code>item</code> parameters are
     *             <code>null</code>, or if <code>itemNum</code> parameter is negative (less
     *             than zero).
     */
    private static boolean validateScorecardComments(HttpServletRequest request, Item item, int itemNum) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(itemNum, "itemNum", 0, Integer.MAX_VALUE);

        boolean noCommentsEntered = true;

        for (int i = 0; i < item.getNumberOfComments(); ++i) {
            if (ActionsHelper.isReviewerComment(item.getComment(i))) {
                noCommentsEntered = false;
                break;
            }
        }

        if (noCommentsEntered) {
            ActionsHelper.addErrorToRequest(request,
                    "comment(" + itemNum + ".1)", "Error.saveReview.Comment.AtLeastOne");
            return false;
        }

        // Success indicator
        boolean success = true;

        for (int i = 0; i < item.getNumberOfComments(); ++i) {
            Comment comment = item.getComment(i);

            if (ActionsHelper.isReviewerComment(comment)) {
                if (!validateScorecardComment(request, comment, "comment(" + itemNum + "." + (i + 1) + ")")) {
                    success = false;
                }
            }
        }

        return success;
    }

    /**
     * This static method validates single comment at a time. The comment must have its text to be
     * non-null and non-empty string to be regarded as passing validation.
     *
     * @return <code>true<code> if validation succeeds, <code>false</code> if it doesn't.
     * @param request
     *            an <code>HttpServletRequest</code> object where validation error messages will
     *            be placed to in case there are any.
     * @param comment
     *            a comment to validate.
     * @param errorMessageProperty
     *            a string parameter that determines which key an error message will be stored
     *            under.
     * @throws IllegalArgumentException
     *             if parameters <code>request</code>, <code>comment</code>, or
     *             <code>errorMessageProperty</code> are <code>null</code>, or if parameter
     *             <code>errorMessageProperty</code> is empty string.
     */
    private static boolean validateScorecardComment(
            HttpServletRequest request, Comment comment, String errorMessageProperty) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(comment, "comment");
        ActionsHelper.validateParameterStringNotEmpty(errorMessageProperty, "errorMessageProperty");

        String commentText = comment.getComment();
        if (commentText == null || commentText.trim().length() == 0) {
            ActionsHelper.addErrorToRequest(request, errorMessageProperty, "Error.saveReview.Comment.Absent");
            return false;
        }

        return true;
    }

    /**
     * This static method validates a reviewer's comment to have an aggregate function. The
     * aggregate function must be specified (non-null and non-empty string), and must be equal to
     * one of the following values:
     * <ul>
     * <li>&quot;<code>Accept</code>&quot;</li>
     * <li>&quot;<code>Reject</code>&quot;</li>
     * <li>&quot;<code>Duplicate</code>&quot;</li>
     * </ul>
     *
     * @return <code>true</code> if validation was successful, <code>false</code> if it wasn't.
     * @param request
     *            an <code>HttpServletRequest</code> object where validation error messages will
     *            be placed to in case there are any.
     * @param comment
     *            a reviewer's comment to validate.
     * @param commentIdx
     *            absolute index of the comment on the page.
     * @throws IllegalArgumentException
     *             if parameters <code>request</code> or <code>comment</code> are
     *             <code>null</code>, or if comment specified by parameter <code>comment</code>
     *             is not a reviewer's comment.
     */
    private static boolean validateAggregateFunction(HttpServletRequest request, Comment comment, int commentIdx) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        if (!(commentType.equalsIgnoreCase("Comment") || commentType.equalsIgnoreCase("Required") ||
                commentType.equalsIgnoreCase("Recommended"))) {
            throw new IllegalArgumentException(
                    "Specified comment is not a reviewer's comment. Comment type: '" + commentType + "'.");
        }

        String aggregationFunction = (String) comment.getExtraInfo();

        if (aggregationFunction == null || aggregationFunction.trim().length() == 0) {
            ActionsHelper.addErrorToRequest(request,
                    "aggregate_function[" + commentIdx + "]", "Error.saveAggregation.Function.Absent");
            return false;
        }

        if (!(aggregationFunction.equalsIgnoreCase("Accept") || aggregationFunction.equalsIgnoreCase("Reject") ||
                aggregationFunction.equalsIgnoreCase("Duplicate"))) {
            ActionsHelper.addErrorToRequest(request,
                    "aggregate_function[" + commentIdx + "]", "Error.saveAggregation.Function.Invalid");
            return false;
        }

        return true;
    }

    /**
     * TODO: Write documentation for this method
     *
     * @return
     * @param request
     * @param question
     * @param item
     * @param fileNum
     * @throws IllegalArgumentException
     *             if <code>request</code>, <code>question</code>, or <code>item</code>
     *             parameters are <code>null</code>, or if <code>fileNum</code> parameter is
     *             negative (less than zero).
     */
    private static boolean validateScorecardItemUpload(
            HttpServletRequest request, Question question, Item item, int fileNum) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(question, "question");
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(fileNum, "fileNum", 0, Integer.MAX_VALUE);

        if (!question.isUploadDocument() || !question.isUploadRequired()) {
            return true;
        }

        if (item.getDocument() == null) {
            ActionsHelper.addErrorToRequest(request, "file[" + fileNum + "]", "Error.saveReview.File.Absent");
            return false;
        }

        return true;
    }

    /**
     * TODO: Document it
     *
     * @return
     * @param allComments
     */
    private static Comment getCommentAppeal(Comment[] allComments) {
        for (int i = 0; i < allComments.length; i++) {
            if (allComments[i].getCommentType().getName().equals("Appeal")) {
                return allComments[i];
            }
        }
        return null;
    }

    /**
     * TODO: Document it
     *
     * @return
     * @param allComments
     */
    private static Comment getCommentAppealResponse(Comment[] allComments) {
        for (int i = 0; i < allComments.length; i++) {
            if (allComments[i].getCommentType().getName().equals("Appeal Response")) {
                return allComments[i];
            }
        }
        return null;
    }
}
