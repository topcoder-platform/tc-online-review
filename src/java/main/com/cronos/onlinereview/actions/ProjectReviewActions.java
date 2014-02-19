/*
 * Copyright (C) 2006-2013 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.LazyValidatorForm;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
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
import com.topcoder.management.scorecard.PersistenceException;
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
import com.cronos.onlinereview.phases.OnlineReviewServices;

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
 * <li>Create Post-Mortem</li>
 * <li>Edit Post-Mortem</li>
 * <li>Save Post-Mortem</li>
 * <li>View Post-Mortem</li>
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
 * <p>
 * Version 1.1 (Online Review End Of Project Analysis Release Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added logic for processing Post-Mortem reviews.</li>
 *     <li>Updated {@link #saveGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     saving review comments for <code>Approval</code> phase.</li>
 *     <li>Updated {@link #createGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     handling <code>Post-Mortem</code> phase.</li>
 *     <li>Updated {@link #viewGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     handling <code>Post-Mortem</code> phase.</li>
 *     <li>Updated {@link #editGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     handling <code>Post-Mortem</code> phase.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.2 (Specification Review Part 1 Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added logic for processing Specification reviews.</li>
 *     <li>Updated {@link #saveGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     saving review comments for <code>Approval</code> phase.</li>
 *     <li>Updated {@link #createGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     handling <code>Post-Mortem</code> phase.</li>
 *     <li>Updated {@link #viewGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     handling <code>Post-Mortem</code> phase.</li>
 *     <li>Updated {@link #editGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     handling <code>Post-Mortem</code> phase.</li>
 *   </ol>
 * </p>
 * <p>
 * Version 1.2.1 (Checkpoint Support 1 Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added logic for processing Checkpoint reviews.</li>
 *     <li>Updated {@link #saveGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     saving review comments for <code>Checkpoint</code> phases.</li>
 *     <li>Updated {@link #createGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     handling <code>Checkpoint</code> phases.</li>
 *     <li>Updated {@link #viewGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     handling <code>Checkpoint</code> phases.</li>
 *     <li>Updated {@link #editGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} to add logic for
 *     handling <code>Checkpoint</code> phases.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.2.2 (Online Review Replatforming Release 2) Change notes:
 *   <ol>
 *     <li>Change submission.getUplaods.get(0) to submission.getUpload().</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.3 (Online Review Payments and Status Automation Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #saveFinalReview(org.apache.struts.action.ActionMapping,org.apache.struts.action.ActionForm,javax.servlet.http.HttpServletRequest)} to
 *         fix the scorecard preview bug.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.4 (Online Review Status Validation Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Methods adjusted for new signatures of create managers methods from ActionsHelper</li>
 *     <li>Updated {@link #viewGenericReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, String reviewType)}
 *     to allow only owner of submission to place appeals and</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.5 (Online Review - Project Payments Integration Part 1 v1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #viewGenericReview(ActionMapping, ActionForm, HttpServletRequest, String)} method to set
 *     the attribute about whether the user can reopen scorecard.</li>
 *     <li>Added {@link #reopenScorecard(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)} method to
 *     handle the requests to reopen a scorecard.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.6 (Online Review - Project Payments Integration Part 3 v1.0) Change notes:
 *   <ol>
 *       <li>Updated {@link #updateFinalAggregatedScore(HttpServletRequest, Project, Phase, Submission)} to pass
 *       operator when calling ActionsHelper.resetProjectResultWithChangedScores.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.7 (Online Review - Review Export) Change notes:
 *   <ol>
 *       <li>Added method {@link #exportReviewResult(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}
 *       and related private methods for exporting review result as xlsx file.</li>
 *       <li>Extracted the logic for viewing aggregation review and final fix review and add corresponding exporting
 *       logic.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.8 (Online Review - Iterative Review v1.0) Change notes:
 *   <ol>
 *       <li>Added methods to create/view/edit/export iterative review scorecard.</li>
 *   </ol>
 * </p>
 *
 * @author George1, real_vg, isv, FireIce, rac_, flexme, duxiaoyang
 * @version 1.8
 */
public class ProjectReviewActions extends DispatchAction {
    private static final com.topcoder.util.log.Log log = com.topcoder.util.log.LogManager
            .getLog(ProjectReviewActions.class.getName());

    /**
     * The maximum size of the comment length.
     */
    private static final int MAX_COMMENT_LENGTH = 4096;

    private static final long ACTIVE_SCORECARD = 1;

    /**
     * This member variable is a constant that specifies the count of comments displayed for each
     * item by default on Edit Screening, Edit Review, and Edit Approval, Edit Post-Mortem pages.
     */
    private static final int DEFAULT_COMMENTS_NUMBER = 1;

    /**
     * This member variable is a constant that specifies the count of comments displayed for each
     * item when Manager opens either Edit Screening, Edit Review, or Edit Approval or Edit Post-Mortem page.
     */
    private static final int MANAGER_COMMENTS_NUMBER = 1;

    /**
     * This member variable holds the all possible values of answers to &#39;Scale&#160;(1-4)&#39;
     * and &#39;Scale&#160;(1-10)&#39; types of scorecard question.
     */
    private static final Map<String, Set<String>> correctAnswers = new HashMap<String, Set<String>>();

    /**
     * Represents the mapping between comment type and the cell color when exporting the comment.
     * @since 1.7
     */
    private static final Map<String, Integer> commentExportColor = new HashMap<String, Integer>();

    /**
     * Represents the review comment types.
     * @since 1.7
     */
    private static final Set<String> reviewerCommentTypes = new HashSet<String>();

    /**
     * Represents the comment types which are exported for final review.
     * @since 1.7
     */
    private static final Set<String> finalReviewCommentTypes = new HashSet<String>();

    /**
     * Represents the comment types which are exported for aggregation.
     * @since 1.7
     */
    private static final Set<String> aggregationCommentTypes = new HashSet<String>();

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

        commentExportColor.put("Group", 0xA6A6A6);
        commentExportColor.put("Section", 0xD9D9D9);
        commentExportColor.put("Question", 0xFFFFFF);
        commentExportColor.put("Attachment", 0xDCE6F1);
        commentExportColor.put("Comment", 0xDCE6F1);
        commentExportColor.put("Recommended", 0xDCE6F1);
        commentExportColor.put("Required", 0xDCE6F1);
        commentExportColor.put("Appeal", 0xF2DCDB);
        commentExportColor.put("Appeal Response", 0xB8CCE4);
        commentExportColor.put("Aggregation Comment", 0xDCE6F1);
        commentExportColor.put("Aggregation Review Comment", 0xDCE6F1);
        commentExportColor.put("Submitter Comment", 0xDCE6F1);
        commentExportColor.put("Final Fix Comment", 0xDCE6F1);
        commentExportColor.put("Final Review Comment", 0xDCE6F1);
        commentExportColor.put("Manager Comment", 0xFFC000);
        commentExportColor.put("Approval Review Comment", 0xDCE6F1);
        commentExportColor.put("Approval Review Comment - Other Fixes", 0xDCE6F1);
        commentExportColor.put("Specification Review Comment", 0xDCE6F1);

        reviewerCommentTypes.add("Required");
        reviewerCommentTypes.add("Recommended");
        reviewerCommentTypes.add("Comment");

        finalReviewCommentTypes.addAll(reviewerCommentTypes);
        finalReviewCommentTypes.add("Appeal");
        finalReviewCommentTypes.add("Appeal Response");
        finalReviewCommentTypes.add("Aggregation Comment");
        finalReviewCommentTypes.add("Final Review Comment");
        finalReviewCommentTypes.add("Manager Comment");
        finalReviewCommentTypes.add("Submitter Comment");

        aggregationCommentTypes.addAll(reviewerCommentTypes);
        aggregationCommentTypes.add("Appeal");
        aggregationCommentTypes.add("Appeal Response");
        aggregationCommentTypes.add("Aggregation Comment");
        aggregationCommentTypes.add("Manager Comment");
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
        return viewOrExportGenericReview(mapping, form, request, response, "Screening", false);
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
        return viewOrExportGenericReview(mapping, form, request, response, "Review", false);
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
                                           HttpServletRequest request, HttpServletResponse response) throws BaseException {

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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREGATION_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
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
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
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

        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                LookupHelper.getCommentType("Comment"),
                LookupHelper.getCommentType("Required"),
                LookupHelper.getCommentType("Recommended") };

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
                                responseTypeIds[commentIdx] = comment.getCommentType().getId();
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

        request.setAttribute("tableTitle", messages.getMessage("editReview.EditAggregation.title"));

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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREGATION_PERM_NAME);
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
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
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
                ActionsHelper.createPhaseManager(false), verification.getProject());
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
                                comment.setCommentType(LookupHelper.getCommentType(responseTypeIds[commentIndex]));
                                ++commentIndex;
                            }
                            if (typeName.equalsIgnoreCase("Aggregation Comment")) {
                                aggregatorComment = comment;
                            }
                        }

                        if (aggregatorComment == null) {
                            aggregatorComment = new Comment();
                            aggregatorComment.setCommentType(LookupHelper.getCommentType("Aggregation Comment"));
                            item.addComment(aggregatorComment);
                        }

                        aggregatorComment.setComment(responses[itemIdx++]);
                        aggregatorComment.setAuthor(resource.getId());
                    }
                }
            }
        }

        boolean validationSucceeded =
                (!commitRequested) || validateAggregationScorecard(request, scorecardTemplate, review);

        // If the user has requested to complete the review
        if (validationSucceeded && commitRequested) {
            // Set the completed status of the review
            review.setCommitted(true);
        } else if (previewRequested) {
            // Put the review object into the request
            request.setAttribute("review", review);
            // Retrieve some basic aggregation info and store it into the request
            retrieveAndStoreBasicAggregationInfo(request, verification, scorecardTemplate, "Aggregation");

            // Notify View page that this is actually a preview operation
            request.setAttribute("isPreview", Boolean.TRUE);

            // Forward to preview page
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Update (save) edited Aggregation
        ReviewManager revMgr = ActionsHelper.createReviewManager();
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

        ActionForward actionForward = viewOrExportAggregation(mapping, request, response, false);
        if (actionForward != null) {
            return actionForward;
        } else {
            return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
        }
    }

    /**
     * <p>
     * Handles the request for viewing or exporting the aggregation details.
     * </p>
     * @param mapping
     *            an <code>ActionMapping</code> used to map the request to this method.
     * @param request
     *            an <code>HttpServletRequest</code> representing the incoming request from the client.
     * @param response
     *            an <code>HttpServletResponse</code> representing the response to be written to the client.
     * @param export
     *            true if the review details are to be exported; or false if they are to be viewed only.
     * @return an <code>ActionForward</code> referencing the next view to be used for processing the request, or null if
     *         <code>export</code> is true (because the result is written to response directly).
     * @throws BaseException
     *             if an unexpected error occurs.
     */
    private ActionForward viewOrExportAggregation(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, boolean export) throws BaseException {
        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request,
                getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, Constants.VIEW_AGGREGATION_PERM_NAME);

        // Get message resources
        MessageResources messages = getResources(request);

        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            // Need to support view aggregation just by specifying the project id
            verification = ActionsHelper.checkForCorrectProjectId(
                    mapping, getResources(request), request, Constants.VIEW_AGGREGATION_PERM_NAME, false);
            if (!verification.isSuccessful()) {
                return verification.getForward();
            }

            // Create filters to select Aggregators for the project
            Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(verification.getProject().getId());
            Filter filterRole = ResourceFilterBuilder.createResourceRoleIdFilter(
                    LookupHelper.getResourceRole(Constants.AGGREGATOR_ROLE_NAME).getId());
            // Combine the upper two filter
            Filter filterAggregators = new AndFilter(filterProject, filterRole);
            // Fetch all Aggregators for the project
            Resource[] aggregators = ActionsHelper.createResourceManager().searchResources(filterAggregators);

            // If the project does not have any Aggregators,
            // there cannot be any Aggregation worksheets. Signal about the error to the user
            if (aggregators.length == 0) {
                return ActionsHelper.produceErrorReport(mapping, messages, request,
                        Constants.VIEW_AGGREGATION_PERM_NAME, "Error.NoAggregations", null);
            }

            List<Long> resourceIds = new ArrayList<Long>();

            for (Resource aggregator : aggregators) {
                resourceIds.add(aggregator.getId());
            }

            Filter filterReviewers = new InFilter("reviewer", resourceIds);
            Filter filterCommitted = new EqualToFilter("committed", 1);
            // Build final combined filter
            Filter filter = new AndFilter(filterReviewers, filterCommitted);
            // Obtain an instance of Review Manager
            ReviewManager reviewMgr = ActionsHelper.createReviewManager();
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
            UploadManager upMgr = ActionsHelper.createUploadManager();
            // Get Submission by its id
            Submission submission = upMgr.getSubmission(review.getSubmission());

            // Store Submission object in the result bean
            verification.setSubmission(submission);
            // Place the id of the submission as attribute in the request
            request.setAttribute("sid", submission.getId());
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
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
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

        if (export) {
            exportGenericReview(request, response, "Aggregation");
        } else {
            request.setAttribute("tableTitle", messages.getMessage("viewAggregation.AggregationWorksheet"));
            request.setAttribute("canExport", true);
        }
        return null;
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREG_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
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
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
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
            for (Resource myResource : myResources) {
                if (comment.getAuthor() == myResource.getId()) {
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

        // Do actual verification. Values "Approved" and "Rejected" denote committed Aggregation Review
        String myExtaInfo = (String) myReviewComment.getExtraInfo();
        if ("Approved".equalsIgnoreCase(myExtaInfo) || "Rejected".equalsIgnoreCase(myExtaInfo)) {
            log.log(Level.DEBUG, "failed Do actual verification. Values \"Approved\" and \"Rejected\" denote committed Aggregation Review");
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.PERFORM_AGGREG_REVIEW_PERM_NAME, "Error.ReviewCommitted", null);
        }

        boolean isSubmitter = false;

        // If the user is a Submitter, let underlying JSP page know about this fact
        if (AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME)) {
            isSubmitter = true;
            request.setAttribute("isSubmitter", isSubmitter);
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_AGGREG_REVIEW_PERM_NAME);
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
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
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
            for (Resource myResource : myResources) {
                if (comment.getAuthor() == myResource.getId()) {
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

        // Do actual verification. Values "Approved" and "Rejected" denote committed Aggregation Review
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

        // Determine the type of comment to search for
        CommentType commentType = LookupHelper.getCommentType(
                (isSubmitter) ? "Submitter Comment" : "Aggregation Review Comment");

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

                        // If review function equals to anything but "Accept", regard the item as
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

        boolean validationSucceeded = (!commitRequested) || validateAggregationReviewScorecard(
                request, scorecardTemplate, review, myReviewComment.getAuthor());

        // If the user has requested to complete the review
        if (validationSucceeded && commitRequested) {
            // Values "Approved" or "Rejected" will denote committed review
            myReviewComment.setExtraInfo((rejected) ? "Rejected" : "Approved");
        }

        // Update (save) edited Aggregation Review
        ReviewManager revMgr = ActionsHelper.createReviewManager();
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, Constants.VIEW_AGGREG_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
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
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
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
                                           HttpServletRequest request, HttpServletResponse response) throws BaseException {
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
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
        request.setAttribute("approvalBased", scorecardTemplate.getScorecardType().getName().equals("Approval"));

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
        Boolean approveFixes = fixesApproved;

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

        request.setAttribute("tableTitle", messages.getMessage("editFinalReview.Scorecard.title"));

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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, Constants.PERFORM_FINAL_REVIEW_PERM_NAME);
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
                ActionsHelper.createPhaseManager(false), verification.getProject());
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
            return mapping.findForward(Constants.PREVIEW_FORWARD_NAME);
        }

        // Update (save) edited Aggregation
        ReviewManager revMgr = ActionsHelper.createReviewManager();
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

        ActionForward actionForward = viewOrExportFinalReview(mapping, request, response, false);
        if (actionForward != null) {
            return actionForward;
        } else {
            return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
        }
    }

    /**
     * <p>
     * Handles the request for viewing or exporting the final review details.
     * </p>
     * @param mapping
     *            an <code>ActionMapping</code> used to map the request to this method.
     * @param request
     *            an <code>HttpServletRequest</code> representing the incoming request from the client.
     * @param response
     *            an <code>HttpServletResponse</code> representing the response to be written to the client.
     * @param export
     *            true if the review details are to be exported; or false if they are to be viewed only.
     * @return an <code>ActionForward</code> referencing the next view to be used for processing the request, or null if
     *         <code>export</code> is true (because the result is written to response directly).
     * @throws BaseException
     *             if an unexpected error occurs.
     */
    private ActionForward viewOrExportFinalReview(ActionMapping mapping, HttpServletRequest request,
            HttpServletResponse response, boolean export) throws BaseException {
        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request,
                getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, Constants.VIEW_FINAL_REVIEW_PERM_NAME);
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
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        // Verify that the scorecard template for this review is of correct type
        if (!scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Review") &&
            !scorecardTemplate.getScorecardType().getName().equalsIgnoreCase("Approval")) {
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
        request.setAttribute("approvalBased", scorecardTemplate.getScorecardType().getName().equals("Approval"));

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

        if (export) {
            exportGenericReview(request, response, "Final Review");
        } else {
            request.setAttribute("tableTitle", messages.getMessage("editFinalReview.Scorecard.title"));
            request.setAttribute("canExport", true);
        }
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
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        ActionForward genericForward = createGenericReview(mapping, form, request, "Approval");
        if (Constants.SUCCESS_FORWARD_NAME.equals(genericForward.getName())) {
            LazyValidatorForm approvalForm = (LazyValidatorForm) form;
            approvalForm.set("accept_but_require_fixes", Boolean.FALSE);
        }
        return genericForward;
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

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        ActionForward genericForward = editGenericReview(mapping, form, request, "Approval");
        if (Constants.SUCCESS_FORWARD_NAME.equals(genericForward.getName())) {
            Boolean fixesRejected = null;
            boolean fixesAcceptedButOtherFixesRequired = false;
            Review review = (Review) request.getAttribute("review");
            int numberOfComments = review.getNumberOfComments();
            for (int i = 0; i < numberOfComments; ++i) {
                Comment comment = review.getComment(i);
                if (comment.getCommentType().getName().equalsIgnoreCase("Approval Review Comment")) {
                    fixesRejected = ("Rejected".equalsIgnoreCase((String) comment.getExtraInfo()));
                } else if (comment.getCommentType().getName().equalsIgnoreCase("Approval Review Comment - Other Fixes")) {
                    fixesAcceptedButOtherFixesRequired = ("Required".equalsIgnoreCase((String) comment.getExtraInfo()));
                }
            }

            LazyValidatorForm approvalForm = (LazyValidatorForm) form;
            if (fixesRejected != null) {
                approvalForm.set("approve_fixes", !fixesRejected);
            }
            approvalForm.set("accept_but_require_fixes", fixesAcceptedButOtherFixesRequired);
        }
        return genericForward;
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
        return viewOrExportGenericReview(mapping, form, request, response, "Approval", false);
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
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectSubmissionId(mapping, request, Constants.VIEW_COMPOS_SCORECARD_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
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
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);

        Resource[] reviewers = null;
        Phase phase = null;

        if (request.getParameter("phid") != null && request.getParameter("phid").trim().length() > 0) {
            // this is for iterative review phase
            Resource submitter = getMySubmitterResource(request);
            if (submitter != null && verification.getSubmission().getUpload().getOwner() != submitter.getId()) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.NoPermission", null);
            }
            long phaseId = Long.parseLong(request.getParameter("phid"));
            for (Phase ph : phases) {
                if (ph.getId() == phaseId) {
                    phase = ph;
                    break;
                }
            }
            Resource[] allResources = ActionsHelper.getAllResourcesForProject(project);
            List<Resource> iterativeReviewers = new ArrayList<Resource>();
            for (Resource resource : allResources) {
                if (resource.getResourceRole().getName().equals(Constants.ITERATIVE_REVIEWER_ROLE_NAME)) {
                    iterativeReviewers.add(resource);
                }
            }
            reviewers = iterativeReviewers.toArray(new Resource[iterativeReviewers.size()]);
        } else {
            // this is for review phase
            if (!ActionsHelper.isAfterAppealsResponse(phases)) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.CompositeScorecardWrongStage", null);
            }

            // Get the Review phase
            phase = ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME);

            // Build a filter to select resources (i.e. reviewers) for Review phase
            Filter filterPhase = ResourceFilterBuilder.createPhaseIdFilter(phase.getId());
            // Obtain an instance of Resource Manager
            ResourceManager resMgr = ActionsHelper.createResourceManager();
            // Retrieve reviewers that did the reviews
            reviewers = resMgr.searchResources(filterPhase);
        }

        // Retrieve a scorecard template for the Review phase
        Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(phase, false);

        // Get the count of questions in the current scorecard
        final int questionsCount = ActionsHelper.getScorecardQuestionsCount(scorecardTemplate);

        for (Resource reviewer : reviewers) {
            ActionsHelper.populateEmailProperty(request, reviewer);
        }

        if (reviewers.length == 0) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.InternalError", null);
        }

        List<Long> reviewerIds = new ArrayList<Long>();
        for (Resource reviewer : reviewers) {
            reviewerIds.add(reviewer.getId());
        }

        // Prepare filters
        Filter filterReviewers = new InFilter("reviewer", reviewerIds);
        Filter filterSubmission = new EqualToFilter("submission", verification.getSubmission().getId());
        Filter filterCommitted = new EqualToFilter("committed", 1);
        Filter filterScorecard = new EqualToFilter("scorecardType",
                scorecardTemplate.getScorecardType().getId());

        // Prepare final combined filter
        Filter filter = new AndFilter(Arrays.asList(
            filterReviewers, filterSubmission, filterCommitted, filterScorecard));
        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager();
        // Retrieve an array of reviews
        Review[] reviews = revMgr.searchReviews(filter, true);

        if (reviews.length != reviewers.length) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.CompositeScorecardIsNotReady", null);
        }

        // Verify that number of items in every review scorecard match the number of questions in scorecard template
        for (Review review : reviews) {
            if (review.getNumberOfItems() != questionsCount) {
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
            for (Resource reviewer1 : reviewers) {
                if (review.getAuthor() == reviewer1.getId()) {
                    reviewer = reviewer1;
                    break;
                }
            }

            if (reviewer == null) {
                return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                        Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.InternalError", null);
            }

            authors[i] = Long.parseLong((String) reviewer.getProperty("External Reference ID"));
            avgScore += (review.getScore() != null) ? review.getScore() : 0;
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
        request.setAttribute("avgScore", avgScore);
        request.setAttribute("avgScores", avgScores);
        request.setAttribute("scores", scores);

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, "CompositeReview", scorecardTemplate);

        // Store reviews in the request
        request.setAttribute("reviews", reviews);

        request.setAttribute("tableTitle", messages.getMessage("editReview.CompositeScorecard.title"));

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
     *            permission to check against, or <code>null</code> if no check is required.
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
        UploadManager upMgr = ActionsHelper.createUploadManager();
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
        request.setAttribute("sid", sid);

        // Retrieve the project following submission's information chain
        Project project = ActionsHelper.getProjectForSubmission(submission);
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
     * This method verifies the request for certain conditions to be met. This includes verifying
     * if the user has specified an ID of project he wants to perform an operation on, if the
     * ID of the project specified by user denotes an existing project, and whether the user
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
     *            permission to check against, or <code>null</code> if no check is required.
     * @throws BaseException
     *             if any error occurs.
     * @since 1.1
     */
    private CorrectnessCheckResult checkForCorrectProjectId(ActionMapping mapping, HttpServletRequest request,
                                                            String permission) throws BaseException {
        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        if (permission == null || permission.trim().length() == 0) {
            permission = null;
        }

        // Verify that Project ID was specified and denotes correct project
        String pidParam = request.getParameter("pid");
        if (pidParam == null || pidParam.trim().length() == 0) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.ProjectIdNotSpecified", null));
            // Return the result of the check
            return result;
        }

        long pid;

        try {
            // Try to convert specified pid parameter to its integer representation
            pid = Long.parseLong(pidParam, 10);
        } catch (NumberFormatException e) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.ProjectNotFound", null));
            // Return the result of the check
            return result;
        }

        // Retrieve the project following submission's information chain
        Project project = ActionsHelper.createProjectManager().getProject(pid);
        if (project == null) {
            result.setForward(ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permission, "Error.ProjectNotFound", null));
            // Return the result of the check
            return result;
        }

        request.setAttribute("pid", pid);

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
     *            permission to check against, or <code>null</code> if no check is required.
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
        ReviewManager revMgr = ActionsHelper.createReviewManager();

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
        Project project;

        // Review may not be associated to submission
        if (review.getSubmission() > 0) {
            // Obtain an instance of Deliverable Manager
            UploadManager upMgr = ActionsHelper.createUploadManager();
            // Get Submission by its id
            Submission submission = upMgr.getSubmission(review.getSubmission());

            // Store Submission object in the result bean
            result.setSubmission(submission);
            // Place the id of the submission as attribute in the request
            request.setAttribute("sid", submission.getId());

            // Retrieve the project following submission's information chain
            project = ActionsHelper.getProjectForSubmission(submission);
        } else {
            long reviewAuthorId = review.getAuthor();
            Resource resource = ActionsHelper.createResourceManager().getResource(reviewAuthorId);
            project = ActionsHelper.createProjectManager().getProject(resource.getProject());
        }


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
                ActionsHelper.createPhaseManager(false), project);

        // Get a Review phase
        Phase reviewPhase = ActionsHelper.getPhase(phases, false, Constants.REVIEW_PHASE_NAME);
        // Retrieve all resources (reviewers) for that phase
        Resource[] reviewResources = ActionsHelper.getAllResourcesForPhase(reviewPhase);
        for (Resource reviewResource : reviewResources) {
            ActionsHelper.populateEmailProperty(request, reviewResource);
        }
        // Place information about reviews into the request
        request.setAttribute("reviewResources", reviewResources);

        // Prepare a list of reviewer IDs. This list will later be used to build filter
        List<Long> reviewerIds = new ArrayList<Long>();
        for (Resource reviewResource : reviewResources) {
            reviewerIds.add(reviewResource.getId());
        }

        // Build filters to fetch the reviews that were used to form current Aggregation
        Filter filterResources = new InFilter("reviewer", reviewerIds);
        Filter filterCommitted = new EqualToFilter("committed", 1);
        Filter filterSubmission = new EqualToFilter("submission", submission.getId());
        Filter filterProject = new EqualToFilter("project", project.getId());
        Filter filterScorecard = new EqualToFilter(
                "scorecardType", scorecardTemplate.getScorecardType().getId());

        // Prepare final filter that combines all the above filters
        Filter filter = new AndFilter(Arrays.asList(filterResources, filterCommitted, filterSubmission, filterProject, filterScorecard));

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager();
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
     * .
     *
     * @param request
     * @throws BaseException
     */
    private void retreiveAndStoreReviewLookUpData(HttpServletRequest request) throws BaseException {
        // Select only those needed for this scorecard
        CommentType reviewCommentTypes[] = new CommentType[] {
                LookupHelper.getCommentType("Comment"),
                LookupHelper.getCommentType("Required"),
                LookupHelper.getCommentType("Recommended") };

        // Place comment types in the request
        request.setAttribute("allCommentTypes", reviewCommentTypes);
    }

    /**
     *
     *
     * @param request
     * @param review
     * @throws BaseException
     */
    private void retrieveAndStoreReviewAuthorInfo(HttpServletRequest request, Review review)
        throws BaseException {
        // TODO: Remove this and other functions to a separate helper class. Name it ProjectReviewActionsHelper

        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(review, "review");

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager();
        // Get review author's resource
        Resource author = resMgr.getResource(review.getAuthor());
        ActionsHelper.populateEmailProperty(request, author);

        // Place submitter's user ID into the request
        request.setAttribute("authorId", author.getProperty("External Reference ID"));
        // Place submitter's resource into the request
        request.setAttribute("authorResource", author);
    }

    /**
     * .
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
        boolean isSubmissionDependentPhase = !reviewType.equals("Post-Mortem");

        // Retrieve some basic project info (such as icons' names) and place it into request
        ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
        // Retrieve an information about my role(s) and place it into the request
        ActionsHelper.retrieveAndStoreMyRole(request, getResources(request));
        // Retrieve the information about the submitter and place it into the request
        if (isSubmissionDependentPhase) {
            ActionsHelper.retrieveAndStoreSubmitterInfo(request, verification.getSubmission().getUpload());
        }

        Review review = verification.getReview();
        if (review != null) {
            // Retrieve the information about the review author and place it into the request
            retrieveAndStoreReviewAuthorInfo(request, review);
            request.setAttribute("modificationDate", review.getModificationTimestamp());
        }
        // Place Scorecard template in the request
        request.setAttribute("scorecardTemplate", scorecardTemplate);
        // Place the type of the review into the request
        if (reviewType.equals("Post-Mortem")) {
            request.setAttribute("reviewType", "PostMortem");
        } else if (reviewType.equals("Specification Review")) {
            request.setAttribute("reviewType", "SpecificationReview");
        } else if (reviewType.equals("Checkpoint Screening")) {
            request.setAttribute("reviewType", "CheckpointScreening");
        } else if (reviewType.equals("Checkpoint Review")) {
            request.setAttribute("reviewType", "CheckpointReview");
        } else if (reviewType.equals("Iterative Review")) {
            request.setAttribute("reviewType", "IterativeReview");
        } else {
            request.setAttribute("reviewType", reviewType);
        }
    }

    /**
     * <p>Handles the request for creating the generic review details.</p>
     *
     * @param mapping an <code>ActionMapping</code> used to map the request to this method.
     * @param form an <code>ActionForm</code> mapped to this request.
     * @param request an <code>HttpServletRequest</code> representing the incoming request from the client.
     * @param reviewType a <code>String</code> referencing the type of the review.
     * @return an <code>ActionForward</code> referencing the next view to be used for processing the request.
     * @throws BaseException if an unexpected error occurs.
     */
    private ActionForward createGenericReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                              String reviewType) throws BaseException {

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        String permName;
        String phaseName;
        boolean isPostMortemPhase = false;
        // Determine permission name and phase name from the review type
        if ("Screening".equals(reviewType)) {
            permName = Constants.PERFORM_SCREENING_PERM_NAME;
            phaseName = Constants.SCREENING_PHASE_NAME;
        } else if ("Review".equals(reviewType)) {
            permName = Constants.PERFORM_REVIEW_PERM_NAME;
            phaseName = Constants.REVIEW_PHASE_NAME;
        } else if ("Approval".equals(reviewType)) {
            permName = Constants.PERFORM_APPROVAL_PERM_NAME;
            phaseName = Constants.APPROVAL_PHASE_NAME;
        } else if ("Specification Review".equals(reviewType)) {
            permName = Constants.PERFORM_SPECIFICATION_REVIEW_PERM_NAME;
            phaseName = Constants.SPECIFICATION_REVIEW_PHASE_NAME;
        } else if ("Checkpoint Screening".equals(reviewType)) {
            permName = Constants.PERFORM_CHECKPOINT_SCREENING_PERM_NAME;
            phaseName = Constants.CHECKPOINT_SCREENING_PHASE_NAME;
        } else if ("Checkpoint Review".equals(reviewType)) {
            permName = Constants.PERFORM_CHECKPOINT_REVIEW_PERM_NAME;
            phaseName = Constants.CHECKPOINT_REVIEW_PHASE_NAME;
        } else if ("Iterative Review".equals(reviewType)) {
            permName = Constants.PERFORM_ITERATIVE_REVIEW_PERM_NAME;
            phaseName = Constants.ITERATIVE_REVIEW_PHASE_NAME;
        } else {
            isPostMortemPhase = true;
            permName = Constants.PERFORM_POST_MORTEM_REVIEW_PERM_NAME;
            phaseName = Constants.POST_MORTEM_PHASE_NAME;
        }

        // Verify that certain requirements are met before proceeding with the Action
        // If any error has occurred, return action forward contained in the result bean
        if (isPostMortemPhase) {
            verification = checkForCorrectProjectId(mapping, request, permName);
        } else {
            verification = checkForCorrectSubmissionId(mapping, request, permName);
        }
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
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);

        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, phaseName);
        // Check that the phase in question is really active (open)
        if (phase == null) {
            return ActionsHelper.produceErrorReport(
                    mapping, getResources(request), request, permName, "Error.IncorrectPhase", null);
        }

        // Get "My" resource for the appropriate phase
        // For Post-Mortem and Approval phases resource is retrieved by role but not by phase
        Resource myResource;
        if (phase.getPhaseType().getName().equals(Constants.POST_MORTEM_PHASE_NAME)) {
            myResource = ActionsHelper.getMyResourceForRole(request, Constants.POST_MORTEM_REVIEWER_ROLE_NAME);
        } else if (phase.getPhaseType().getName().equals(Constants.APPROVAL_PHASE_NAME)) {
            myResource = ActionsHelper.getMyResourceForRole(request, Constants.APPROVER_ROLE_NAME);
        } else if (phase.getPhaseType().getName().equals(Constants.ITERATIVE_REVIEW_PHASE_NAME)) {
            myResource = ActionsHelper.getMyResourceForRole(request, Constants.ITERATIVE_REVIEWER_ROLE_NAME);
        } else {
            myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        }
        // Retrieve a scorecard template for the appropriate phase
        Scorecard scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(phase, false);

        /*
         * Verify that the user is not trying to create review that already exists
         */
        // Prepare filters
        Filter filterResource = new EqualToFilter("reviewer", myResource.getId());
        Filter filterScorecard = new EqualToFilter("scorecardType", scorecardTemplate.getScorecardType().getId());
        Filter filterPhase = new EqualToFilter("projectPhase", phase.getId());

        Filter filter;
        if (isPostMortemPhase) {
            // Prepare final combined filter
            filter = new AndFilter(Arrays.asList(filterResource, filterScorecard, filterPhase));
        } else {
            // Prepare final combined filter
            Filter filterSubmission = new EqualToFilter("submission", verification.getSubmission().getId());
            filter = new AndFilter(Arrays.asList(filterResource, filterSubmission, filterScorecard, filterPhase));
        }

        // Obtain an instance of Review Manager
        ReviewManager revMgr = ActionsHelper.createReviewManager();
        // Retrieve an array of reviews
        Review[] reviews = revMgr.searchReviews(filter, false);

        // Non-empty array of reviews indicates that user is trying to create review that already exists
        if (reviews.length != 0) {
            // Forward to Edit Screening page
            return ActionsHelper.cloneForwardAndAppendToPath(
                    mapping.findForward(Constants.EDIT_FORWARD_NAME), "&rid=" + reviews[0].getId());
        }

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);
        // Place current user's id as author's id
        request.setAttribute("authorId", AuthorizationHelper.getLoggedInUserId(request));
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

        CommentType typeComment = LookupHelper.getCommentType("Comment");

        Integer[] commentCounts = new Integer[questionsCount];
        Arrays.fill(commentCounts, DEFAULT_COMMENTS_NUMBER);
        reviewForm.set("comment_count", commentCounts);

        for (int i = 0; i < questionsCount; i++) {
            for (int j = 0; j <= DEFAULT_COMMENTS_NUMBER; j++) {
                reviewForm.set("comment", i + "." + j, "");
                reviewForm.set("comment_type", i + "." + j, typeComment);
            }
        }

        request.setAttribute("tableTitle", scorecardTemplate.getName());

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     * <p>Handles the request for editing the generic review details.</p>
     *
     * @param mapping an <code>ActionMapping</code> used to map the request to this method.
     * @param form an <code>ActionForm</code> mapped to this request.
     * @param request an <code>HttpServletRequest</code> representing the incoming request from the client.
     * @param reviewType a <code>String</code> referencing the type of the review.
     * @return an <code>ActionForward</code> referencing the next view to be used for processing the request.
     * @throws BaseException if an unexpected error occurs.
     */
    private ActionForward editGenericReview(ActionMapping mapping, ActionForm form, HttpServletRequest request, String reviewType) throws BaseException {

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        String scorecardTypeName;
        // Determine permission name and phase name from the review type
        boolean isSpecReviewPhase = false;
        if ("Screening".equals(reviewType)) {
            scorecardTypeName = "Screening";
        } else if ("Review".equals(reviewType)) {
            scorecardTypeName = "Review";
        } else if ("Approval".equals(reviewType)) {
            scorecardTypeName = "Approval";
        } else if ("Specification Review".equals(reviewType)) {
            scorecardTypeName = "Specification Review";
            isSpecReviewPhase = true;
        } else if ("Checkpoint Screening".equals(reviewType)) {
            scorecardTypeName = "Checkpoint Screening";
        } else if ("Checkpoint Review".equals(reviewType)) {
            scorecardTypeName = "Checkpoint Review";
        } else if ("Iterative Review".equals(reviewType)) {
            scorecardTypeName = "Iterative Review";
        } else {
            scorecardTypeName = "Post-Mortem";
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification =
                checkForCorrectReviewId(mapping, request, Constants.EDIT_MY_REVIEW_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to edit
        Review review = verification.getReview();

        // Obtain an instance of Scorecard Manager
        ScorecardManager scorMgr = ActionsHelper.createScorecardManager();
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
        	// Nobody is allowed to edit committed iterative review scorecard
            if (!scorecardTypeName.equals("Iterative Review") && AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
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
        retreiveAndStoreReviewLookUpData(request);

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

                    reviewForm.set("comment_count", itemIdx, commentCount);

                    for (int i = 0; i < commentCount; ++i) {
                        String commentKey = itemIdx + "." + (i + 1);
                        Comment comment = (i < comments.length) ? comments[i] : null;

                        reviewForm.set("comment", commentKey, (comment != null) ? comment.getComment() : "");
                        reviewForm.set("comment_type", commentKey,
                                (comment != null) ? comment.getCommentType().getId() :
                                        LookupHelper.getCommentType("Comment").getId());
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

        if (isSpecReviewPhase) {
            // set the approve specification check box.
            boolean specReviewApproved = false;
            Comment[] allComments = review.getAllComments();
            for (Comment comment : allComments) {
                if ("Specification Review Comment".equalsIgnoreCase(comment.getCommentType().getName())) {
                    specReviewApproved = "Approved".equals(comment.getExtraInfo());
                    break;
                }
            }
            reviewForm.set("approve_specification", specReviewApproved);
        }

        request.setAttribute("tableTitle", scorecardTemplate.getName());

        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

    /**
     *
     *
     * @param item
     * @return
     */
    private static Comment[] getItemManagerComments(Item item) {
        List<Comment> result = new ArrayList<Comment>();
        for (int i = 0; i < item.getNumberOfComments(); i++) {
            Comment comment = item.getComment(i);
            if (ActionsHelper.isManagerComment(comment)) {
                result.add(comment);
            }
        }
        return result.toArray(new Comment[result.size()]);
    }

    /**
     *
     *
     * @param item
     * @return
     */
    private static Comment[] getItemReviewerComments(Item item) {
        List<Comment> result = new ArrayList<Comment>();
        for (int i = 0; i < item.getNumberOfComments(); i++) {
            Comment comment = item.getComment(i);
            if (ActionsHelper.isReviewerComment(comment)) {
                result.add(comment);
            }
        }
        return result.toArray(new Comment[result.size()]);
    }

    /**
     * <p>Handles the request for saving the generic review details.</p>
     *
     * @param mapping an <code>ActionMapping</code> used to map the request to this method.
     * @param form an <code>ActionForm</code> mapped to this request.
     * @param request an <code>HttpServletRequest</code> representing the incoming request from the client.
     * @param reviewType a <code>String</code> referencing the type of the review.
     * @return an <code>ActionForward</code> referencing the next view to be used for processing the request.
     * @throws BaseException if an unexpected error occurs.
     */
    private ActionForward saveGenericReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                            String reviewType) throws BaseException {
        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        String permName;
        String phaseName;
        String scorecardTypeName;
        boolean isApprovalPhase = false;
        boolean isSpecReviewPhase = false;
        boolean isSubmissionDependentPhase = true;
        // Determine permission name and phase name from the review type
        if ("Screening".equals(reviewType)) {
            permName = Constants.PERFORM_SCREENING_PERM_NAME;
            phaseName = Constants.SCREENING_PHASE_NAME;
            scorecardTypeName = "Screening";
        } else if ("Checkpoint Screening".equals(reviewType)) {
            permName = Constants.PERFORM_CHECKPOINT_SCREENING_PERM_NAME;
            phaseName = Constants.CHECKPOINT_SCREENING_PHASE_NAME;
            scorecardTypeName = "Checkpoint Screening";
        } else if ("Checkpoint Review".equals(reviewType)) {
            permName = Constants.PERFORM_CHECKPOINT_REVIEW_PERM_NAME;
            phaseName = Constants.CHECKPOINT_REVIEW_PHASE_NAME;
            scorecardTypeName = "Checkpoint Review";
        } else if ("Review".equals(reviewType)) {
            permName = Constants.PERFORM_REVIEW_PERM_NAME;
            phaseName = Constants.REVIEW_PHASE_NAME;
            scorecardTypeName = "Review";
        } else if ("Approval".equals(reviewType)) {
            isApprovalPhase = true;
            permName = Constants.PERFORM_APPROVAL_PERM_NAME;
            phaseName = Constants.APPROVAL_PHASE_NAME;
            scorecardTypeName = "Approval";
        } else if ("Specification Review".equals(reviewType)) {
            isSpecReviewPhase = true;
            permName = Constants.PERFORM_SPECIFICATION_REVIEW_PERM_NAME;
            phaseName = Constants.SPECIFICATION_REVIEW_PHASE_NAME;
            scorecardTypeName = "Specification Review";
        } else if ("Iterative Review".equals(reviewType)) {
            permName = Constants.PERFORM_ITERATIVE_REVIEW_PERM_NAME;
            phaseName = Constants.ITERATIVE_REVIEW_PHASE_NAME;
            scorecardTypeName = "Iterative Review";
        } else {
            isSubmissionDependentPhase = false;
            permName = Constants.PERFORM_POST_MORTEM_REVIEW_PERM_NAME;
            phaseName = Constants.POST_MORTEM_PHASE_NAME;
            scorecardTypeName = "Post-Mortem";
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = null;
        if (request.getParameter("rid") != null) {
            verification = checkForCorrectReviewId(mapping, request, permName);
        }
        if (verification == null && request.getParameter("pid") != null) {
            verification = checkForCorrectProjectId(mapping, request, permName);
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
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
        // Get active (current) phase
        Phase phase = ActionsHelper.getPhase(phases, true, phaseName);
        // Check that the phase in question is really active (open)
        if (phase == null) {
            if (!scorecardTypeName.equals("Iterative Review") && AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
                // Managers can edit reviews in any phase, except iterative review
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
            scorecardTemplate = ActionsHelper.getScorecardTemplateForPhase(phase, false);

            // Prepare filters
            Filter filterResource = new EqualToFilter("reviewer", myResource.getId());
            Filter filterScorecard = new EqualToFilter("scorecardType", scorecardTemplate.getScorecardType().getId());
            Filter filterPhase = new EqualToFilter("projectPhase", phase.getId());

            Filter filter;
            if (isSubmissionDependentPhase) {
                Filter filterSubmission = new EqualToFilter("submission", verification.getSubmission().getId());
                filter = new AndFilter(Arrays.asList(filterResource, filterSubmission, filterScorecard, filterPhase));
            } else {
                // Prepare final combined filter
                filter = new AndFilter(Arrays.asList(filterResource, filterScorecard, filterPhase));
            }

            // Obtain an instance of Review Manager
            ReviewManager revMgr = ActionsHelper.createReviewManager();
            // Retrieve an array of reviews
            Review[] reviews = revMgr.searchReviews(filter, false);

            // Non-empty array of reviews indicates that user is trying to create review that already exists
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
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
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
            if(!scorecardTypeName.equals("Iterative Review") && AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
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
        Map<String, String> replies = (Map<String, String>) reviewForm.get("comment");
        Map<String, String> commentTypeIds = (Map<String, String>) reviewForm.get("comment_type");
        FormFile[] files = (FormFile[]) reviewForm.get("file");

        // Uploaded files will be held here
        UploadedFile[] uploadedFiles = null;

        // Files won't be uploaded, if a mere Preview operation was requested
        if (!previewRequested) {
            StrutsRequestParser parser = new StrutsRequestParser();

            // Collect uploaded files and add them to adapter
            for (FormFile file : files) {
                if (file != null && file.getFileName().trim().length() != 0) {
                    parser.AddFile(file);
                }
            }

            // Obtain an instance of File Upload Manager
            FileUpload fileUpload = ActionsHelper.createFileUploadManager(request);

            // Upload files to the file server
            FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);
            // Get an information about uploaded files (an uploaded files' ID in particular)
            uploadedFiles = uploadResult.getUploadedFiles("file");
        }

        ReviewManager revMgr = ActionsHelper.createReviewManager();
        // Obtain an instance of Upload Manager
        UploadManager upMgr = ActionsHelper.createUploadManager();

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
                                commentCounts[itemIdx], myResource, managerEdit);

                        if (newCommentCount != commentCounts[itemIdx]) {
                            commentCounts[itemIdx] = newCommentCount;
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
                                upload.setProjectPhase(phase.getId());
                                upload.setParameter(uploadedFiles[uploadedFileIdx++].getFileId());
                                upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
                                upload.setUploadType(LookupHelper.getUploadType("Review Document"));

                                upMgr.createUpload(upload, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

                                item.setDocument(upload.getId());
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
            reviewEditor.setProjectPhase(phase.getId());
            // Skip setting submission ID for Post-Mortem phase
            if (isSubmissionDependentPhase) {
                reviewEditor.setSubmission(verification.getSubmission().getId());
            }
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
                                commentCounts[itemIdx], myResource, managerEdit);

                        if (newCommentCount != commentCounts[itemIdx]) {
                            commentCounts[itemIdx] = newCommentCount;
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
                                    oldUpload = upMgr.getUpload(item.getDocument());
                                }

                                Upload upload = new Upload();

                                // Set fields of the new upload
                                upload.setOwner(myResource.getId());
                                upload.setParameter(uploadedFiles[uploadedFileIdx++].getFileId());
                                upload.setProject(project.getId());
                                upload.setProjectPhase(phase.getId());
                                upload.setUploadStatus(LookupHelper.getUploadStatus("Active"));
                                upload.setUploadType(LookupHelper.getUploadType("Review Document"));

                                // Update and store old upload (if there was any)
                                if (oldUpload != null) {
                                    oldUpload.setUploadStatus(LookupHelper.getUploadStatus("Deleted"));
                                    upMgr.updateUpload(oldUpload,
                                            Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
                                }

                                // Save information about current upload
                                upMgr.createUpload(upload,
                                        Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

                                item.setDocument(upload.getId());
                            }
                            ++fileIdx;
                        }
                    }
                }
            }
        }

        // For Manager Edits this variable indicates whether recomputation of
        // final aggregated score for the submitter may be required
        boolean possibleFinalScoreUpdate = false;


        if (isApprovalPhase) {
            Comment reviewLevelComment1 = null;
            Comment reviewLevelComment2 = null;
            boolean approveFixes;
            boolean acceptButRequireOtherFixes;

            Resource resource = ActionsHelper.getMyResourceForRole(request, Constants.APPROVER_ROLE_NAME);

            Boolean approveFixesObj = (Boolean) reviewForm.get("approve_fixes");
            if (approveFixesObj == null) {
                ActionsHelper.addErrorToRequest(request, "approve_status", "Error.saveApproval.Absent");
            }

            Boolean acceptButRequireOtherFixesObj = (Boolean) reviewForm.get("accept_but_require_fixes");
            approveFixes = (approveFixesObj != null && approveFixesObj);
            acceptButRequireOtherFixes = (acceptButRequireOtherFixesObj != null && acceptButRequireOtherFixesObj);

            for (int i = 0; i < review.getNumberOfComments(); ++i) {
                Comment comment = review.getComment(i);
                if (comment.getCommentType().getName().equalsIgnoreCase("Approval Review Comment")) {
                    reviewLevelComment1 = comment;
                } else if (comment.getCommentType().getName().equalsIgnoreCase("Approval Review Comment - Other Fixes")) {
                    reviewLevelComment2 = comment;
                }
            }

            if (reviewLevelComment1 == null) {
                reviewLevelComment1 = new Comment();
            }

            if (approveFixesObj != null) {
                reviewLevelComment1.setCommentType(
                    LookupHelper.getCommentType("Approval Review Comment"));
                reviewLevelComment1.setAuthor(resource.getId());
                reviewLevelComment1.setExtraInfo(approveFixes ? "Approved" : "Rejected");
                reviewLevelComment1.setComment("");
                review.addComment(reviewLevelComment1);
            }

            if (reviewLevelComment2 == null) {
                reviewLevelComment2 = new Comment();
            }

            reviewLevelComment2.setCommentType(
                LookupHelper.getCommentType("Approval Review Comment - Other Fixes"));
            reviewLevelComment2.setAuthor(resource.getId());
            reviewLevelComment2.setExtraInfo(acceptButRequireOtherFixes ? "Required" : "");
            reviewLevelComment2.setComment("");
            review.addComment(reviewLevelComment2);
        }

        if (isSpecReviewPhase) {
            Comment reviewLevelComment1 = null;

            Boolean approveSpecObj = (Boolean) reviewForm.get("approve_specification");
            boolean approveSpecification;
            approveSpecification = (approveSpecObj != null && approveSpecObj);

            for (int i = 0; i < review.getNumberOfComments(); ++i) {
                Comment comment = review.getComment(i);
                if (comment.getCommentType().getName().equalsIgnoreCase("Specification Review Comment")) {
                    reviewLevelComment1 = comment;
                }
            }

            if (reviewLevelComment1 == null) {
                reviewLevelComment1 = new Comment();
            }

            reviewLevelComment1.setCommentType(
                LookupHelper.getCommentType("Specification Review Comment"));
            reviewLevelComment1.setAuthor(myResource.getId());
            reviewLevelComment1.setExtraInfo(approveSpecification ? "Approved" : "Rejected");
            reviewLevelComment1.setComment("");
            review.addComment(reviewLevelComment1);
        }

        boolean validationSucceeded = (!commitRequested && !managerEdit) || validateGenericScorecard(request, scorecardTemplate, review, managerEdit);

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

        // At this point going forward we assume the validation succeeded
        // If the user has requested to complete the review
        if (commitRequested || managerEdit) {
            // Obtain an instance of CalculationManager
            CalculationManager scoreCalculator = new CalculationManager();
            // Compute scorecard's score
            double newScore = scoreCalculator.getScore(scorecardTemplate, review);
            // If score has been updated during Manager Edit, additional actions may need to be taken
            if ((reviewType.equals("Review") || reviewType.equals("Checkpoint Review")) &&
                    managerEdit && (review.getScore() == null || review.getScore() != newScore)) {
                possibleFinalScoreUpdate = true;
            }

            // Update scorecard's score
            review.setScore(newScore);
            if (review.getInitialScore() == null) {
                review.setInitialScore(review.getScore());
            }
            // Set the completed status of the review
            if (commitRequested) {

                // Make sure that each item has at least one comment before committing the review.
                // If not, create an empty one.
                for (int i = 0; i < review.getNumberOfItems(); ++i) {
                    Item item = review.getItem(i);
                    if (item.getNumberOfComments() == 0) {
                        Comment comment = new Comment();
                        comment.setAuthor(myResource.getId());
                        comment.setComment("");
                        comment.setCommentType(LookupHelper.getCommentType("Comment"));
                        item.addComment(comment);
                    }
                }

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

        // Determine which action should be performed - creation or updating
        if (verification.getReview() == null) {
            revMgr.createReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        } else {
            revMgr.updateReview(review, Long.toString(AuthorizationHelper.getLoggedInUserId(request)));
        }

        // This operation will possibly update final aggregated score for the submitter
        if (possibleFinalScoreUpdate) {
            updateFinalAggregatedScore(request, project, phase, verification.getSubmission());
        }

        if (commitRequested) {
            // Put the review object into the bean (it may not always be there by default)
            verification.setReview(review);
            // Retrieve some basic review info and store it in the request
            retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

            // Place information about the final score into the request
            request.setAttribute("reviewScore", review.getScore());
            // Place review ID into the request
            request.setAttribute("rid", review.getId());

            // Forward to the page that says that scorecard has been committed
            return mapping.findForward(Constants.REVIEW_COMMITTD_FORWARD_NAME);
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
     *            phase of type &quot;Review&quot; or &quot;Checkpoint Review&quot; used internally to retrieve
     *            reviewers' resources.
     * @param submission
     *            a submission in question, i.e. the one that needs its final score updated.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws BaseException
     *             if any unexpected error occurs during final score update.
     */
    private static void updateFinalAggregatedScore(HttpServletRequest request, Project project,
        Phase reviewPhase, Submission submission) throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(project, "project");
        ActionsHelper.validateParameterNotNull(reviewPhase, "reviewPhase");
        ActionsHelper.validateParameterNotNull(submission, "submission");

        // Get final aggregated score for this submitter, if any
        Double finalScore = submission.getFinalScore();
        // If there is no final (post Appeals Response) score for the submission yet, there is nothing to do anymore
        log.log(Level.INFO, "Final Score: " + finalScore);
        if (finalScore == null) {
            return;
        }

        OnlineReviewServices orServices = new OnlineReviewServices();
        orServices.updateSubmissionsResults(reviewPhase,
            String.valueOf(AuthorizationHelper.getLoggedInUserId(request)), false, true);
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        ActionsHelper.resetProjectResultWithChangedScores(project, operator);
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
     * @param managerEdit
     * @throws IllegalArgumentException
     *             if any of the parameters <code>item</code>, <code>replies</code>,
     *             <code>commentTypeIds</code> or
     *             <code>myResource</code> is <code>null</code>, or if parameters
     *             <code>itemIdx</code> or <code>commentCount</code> are negative.
     * @throws BaseException if any other error occur
     */
    private static int populateItemComments(Item item, int itemIdx, Map<String, String> replies,
                                            Map<String, String> commentTypeIds, int commentCount, Resource myResource,
                                            boolean managerEdit) throws BaseException {

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
                commentType = LookupHelper.getCommentType("Manager Comment");
            } else {
                // Check that a mapping for the commentKey actually exists.
                // It may not exist when the browser is confused when the user clicks 'Back' button after validation fails (probably an error in the JS).
                if (!commentTypeIds.containsKey(commentKey)) {
                    replies.remove(commentKey);
                    continue;
                }

                commentType = LookupHelper.getCommentType(Long.parseLong(commentTypeIds.get(commentKey)));
            }
            // Check that correct comment type ID has been specified
            // (user may intentionally submit malformed form data)
            if (commentType == null) {
                replies.remove(commentKey);
                commentTypeIds.remove(commentKey);
                continue;
            }

            // Get user's reply, i.e. comment's text
            String reply = replies.get(commentKey);
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
                commentTypeIds.put(updatedCommentKey, Long.toString(commentType.getId()));
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
            commentTypeIds.put(emptyCommentKey, Long.toString(LookupHelper.getCommentType("Comment").getId()));
        }

        return newCommentCount - 1;
    }

    /**
     * <p>
     * Handles the request for viewing or exporting the generic review details.
     * </p>
     * @param mapping
     *            an <code>ActionMapping</code> used to map the request to this method.
     * @param form
     *            an <code>ActionForm</code> mapped to this request.
     * @param request
     *            an <code>HttpServletRequest</code> representing the incoming request from the client.
     * @param response
     *            an <code>HttpServletResponse</code> representing the response to be written to the client.
     * @param reviewType
     *            a <code>String</code> referencing the type of the review.
     * @param export
     *            true if the review details are to be exported; or false if they are to be viewed only.
     * @return an <code>ActionForward</code> referencing the next view to be used for processing the request, or null if
     *         <code>export</code> is true (because the result is written to response directly).
     * @throws BaseException
     *             if an unexpected error occurs.
     */
    private ActionForward viewOrExportGenericReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, String reviewType, boolean export) throws BaseException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(mapping, "mapping");
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterStringNotEmpty(reviewType, "reviewType");

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        String permName;
        String phaseName;
        String scorecardTypeName;

        // Determine permission name and phase name from the review type
        boolean isSubmissionDependentPhase = true;
        if (reviewType.equals("Screening")) {
            permName = Constants.VIEW_SCREENING_PERM_NAME;
            phaseName = Constants.SCREENING_PHASE_NAME;
            scorecardTypeName = "Screening";
        } else if (reviewType.equals("Checkpoint Screening") || reviewType.equals("CheckpointScreening")) {
            permName = Constants.VIEW_CHECKPOINT_SCREENING_PERM_NAME;
            phaseName = Constants.CHECKPOINT_SCREENING_PHASE_NAME;
            scorecardTypeName = "Checkpoint Screening";
            reviewType = "Checkpoint Screening";
        } else if (reviewType.equals("Checkpoint Review") || reviewType.equals("CheckpointReview")) {
            permName = Constants.VIEW_CHECKPOINT_REVIEW_PERM_NAME;
            phaseName = Constants.CHECKPOINT_REVIEW_PHASE_NAME;
            scorecardTypeName = "Checkpoint Review";
            reviewType = "Checkpoint Review";
        } else if (reviewType.equals("Review")) {
            permName = Constants.VIEW_ALL_REVIEWS_PERM_NAME;
            phaseName = Constants.REVIEW_PHASE_NAME;
            scorecardTypeName = "Review";
        } else if (reviewType.equals("Approval")) {
            permName = Constants.VIEW_APPROVAL_PERM_NAME;
            phaseName = Constants.APPROVAL_PHASE_NAME;
            scorecardTypeName = "Approval";
        } else if (reviewType.equals("Specification Review") || reviewType.equals("SpecificationReview")) {
            permName = Constants.VIEW_SPECIFICATION_REVIEW_PERM_NAME;
            phaseName = Constants.SPECIFICATION_REVIEW_PHASE_NAME;
            scorecardTypeName = "Specification Review";
            reviewType = "Specification Review";
        } else if (reviewType.equals("Iterative Review") || reviewType.equals("IterativeReview")) {
            permName = Constants.VIEW_ITERATIVE_REVIEW_PERM_NAME;
            phaseName = Constants.ITERATIVE_REVIEW_PHASE_NAME;
            scorecardTypeName = "Iterative Review";
            reviewType = "Iterative Review";
        } else if (reviewType.equals("Post-Mortem") || reviewType.equals("PostMortem")) {
            isSubmissionDependentPhase = false;
            permName = Constants.VIEW_POST_MORTEM_PERM_NAME;
            phaseName = Constants.POST_MORTEM_PHASE_NAME;
            scorecardTypeName = "Post-Mortem";
            reviewType = "Post-Mortem";
        } else {
            throw new IllegalArgumentException("Incorrect review type specified: " + reviewType + ".");
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, permName);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Get current project
        Project project = verification.getProject();

        // Get an array of all phases for the project
        Phase[] phases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
        // Get active (opened) phases names
        List<String> activePhases = new ArrayList<String>();
        for (Phase phase : phases) {
            if (phase.getPhaseStatus().getName().equals(PhaseStatus.OPEN.getName())) {
                activePhases.add(phase.getPhaseType().getName());
            }
        }

        // Get a phase with the specified name
        Phase phase = null;
        if (reviewType.equals(Constants.ITERATIVE_REVIEW_PHASE_NAME)) {
            long phaseId = verification.getReview().getProjectPhase();
            for (Phase ph : phases) {
                if (phaseId == ph.getId()) {
                    phase = ph;
                    break;
                }
            }
        } else {
            phase = ActionsHelper.getPhase(phases, false, phaseName);
        }

        // Get "My" resource for the appropriate phase (for reviewers actually)
        Resource myResource = ActionsHelper.getMyResourceForPhase(request, phase);
        if (myResource == null) {
            myResource = ActionsHelper.getMyResourceForPhase(request, null);
        }
        Resource mySubmitterResource = getMySubmitterResource(request);

        /*
         *  Verify that user has the permission to view the review
         */
        boolean isAllowed = false;
        if (AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES) ||
                AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME) ||
                AuthorizationHelper.hasUserRole(request, Constants.CLIENT_MANAGER_ROLE_NAME) ||
                AuthorizationHelper.hasUserRole(request, Constants.COPILOT_ROLE_NAME) ||
                AuthorizationHelper.hasUserRole(request, Constants.OBSERVER_ROLE_NAME)) {
            // User is manager or observer
            isAllowed = true;
        } else if (myResource != null && verification.getReview().getAuthor() == myResource.getId()) {
            // User is authorized to view review authored by him
            isAllowed = true;
        } else if (isSubmissionDependentPhase && mySubmitterResource != null &&
                   verification.getSubmission().getUpload().getOwner() == mySubmitterResource.getId()) {
            // User is authorized to view review for his own submission after the phase has closed
            if (phase.getPhaseStatus().getName().equals(Constants.CLOSED_PH_STATUS_NAME)) {
                isAllowed = true;
            }
        } else if (AuthorizationHelper.hasUserPermission(request, permName)) {
            if (reviewType.equals(Constants.REVIEW_PHASE_NAME)) {
                // User is authorized to view all reviews (when not in Review, Appeals or Appeals Response)
                if (!activePhases.contains(Constants.REVIEW_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                    isAllowed = true;
                }
            } else if (reviewType.equals(Constants.SCREENING_PHASE_NAME)) {
                if (!activePhases.contains(Constants.SCREENING_PHASE_NAME) &&
                        !activePhases.contains(Constants.REVIEW_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                    isAllowed = true;
                } else {
                    //if any of those phases are open, a user can see the screening only if he is not a submitter
                    isAllowed = !AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME);
                }
            } else if (reviewType.equals(Constants.CHECKPOINT_SCREENING_PHASE_NAME) ||
                    reviewType.equals(Constants.CHECKPOINT_REVIEW_PHASE_NAME)) {
                if (!activePhases.contains(Constants.CHECKPOINT_SCREENING_PHASE_NAME) &&
                        !activePhases.contains(Constants.CHECKPOINT_REVIEW_PHASE_NAME) &&
                        !activePhases.contains(Constants.SUBMISSION_PHASE_NAME) &&
                        !activePhases.contains(Constants.SCREENING_PHASE_NAME) &&
                        !activePhases.contains(Constants.REVIEW_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_PHASE_NAME) &&
                        !activePhases.contains(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                    isAllowed = true;
                } else {
                    //if any of those phases are open, a user can see the checkpoint screening/review only if he is not a submitter
                    isAllowed = !AuthorizationHelper.hasUserRole(request, Constants.SUBMITTER_ROLE_NAME);
                }
            } else if (reviewType.equals(Constants.ITERATIVE_REVIEW_PHASE_NAME)) {
                // For Iterative Reviews, submitters can view reviews only for their own submissions.
                if (mySubmitterResource == null ||
                        verification.getSubmission().getUpload().getOwner() == mySubmitterResource.getId()) {
                    // User is authorized to view all reviews after the phase has closed.
                    if (phase.getPhaseStatus().getName().equals(Constants.CLOSED_PH_STATUS_NAME)) {
                        isAllowed = true;
                    }
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
        ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
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
            // But for iterative review, no one can edit it once it is committed
            if (!reviewType.equals(Constants.ITERATIVE_REVIEW_PHASE_NAME)
                    && AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAMES)) {
                request.setAttribute("canEditScorecard", Boolean.TRUE);
            }

            if (AuthorizationHelper.hasUserPermission(request, Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME)
                    && ActionsHelper.isPhaseOpen(phase.getPhaseStatus())) {
                request.setAttribute("canReopenScorecard", Boolean.TRUE);
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
                if(mySubmitterResource != null && verification.getSubmission() != null &&
                    verification.getSubmission().getUpload().getOwner() == mySubmitterResource.getId()) {
                    // Can place appeal, put an appropriate flag to request
                    request.setAttribute("canPlaceAppeal", Boolean.TRUE);
                    canPlaceAppeal = true;
                }
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
                    Comment appealResponse = getCommentAppealResponse(verification.getReview().getItem(i).getAllComments());
                    if (appeal != null && appealResponse == null) {
                        appealStatuses[i] = messages.getMessage("editReview.Appeal.Unresolved");
                    } else if (appeal != null) {
                        appealStatuses[i] = messages.getMessage("editReview.Appeal.Resolved." + appeal.getExtraInfo());
                    } else {
                        appealStatuses[i] = "";
                    }

                    answers[i] = verification.getReview().getItem(i).getAnswer().toString();
                }
                // Set review item answers form property
                if (!export) {
                    ((LazyValidatorForm) form).set("answer", answers);
                }
                // Place appeal statuses to request
                request.setAttribute("appealStatuses", appealStatuses);

                // Retrieve some look-up data and store it into the request
                retreiveAndStoreReviewLookUpData(request);
            }
        }

        // Retrieve some basic review info and store it in the request
        retrieveAndStoreBasicReviewInfo(request, verification, reviewType, scorecardTemplate);

        // Now all the information regarding the review is stored in request. We can forward to the view or export
        if (!export) {
            request.setAttribute("canExport", true);
            request.setAttribute("tableTitle", scorecardTemplate.getName());
            return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
        } else {
            exportGenericReview(request, response, reviewType);
            return null;
        }
    }

    /**
     * Exports generic review result into xlsx file.
     * @param request
     *            the HTTP request.
     * @param response
     *            the HTTP response.
     * @param reviewType
     *            the review type.
     * @throws BaseException
     *             if any error occurs.
     */
    private void exportGenericReview(HttpServletRequest request, HttpServletResponse response, String reviewType)
            throws BaseException {
        MessageResources messages = getResources(request);
        String fileName = reviewType.toLowerCase().replace(' ', '_').replace('-', '_') + "_"
                + request.getParameter("rid");
        // create workbook and sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        sheet.setRowSumsBelow(false);
        // create font, color, and cell style
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        XSSFCellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        XSSFCellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setTopBorderColor(new XSSFColor(Color.BLACK));
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setBottomBorderColor(new XSSFColor(Color.BLACK));
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setLeftBorderColor(new XSSFColor(Color.BLACK));
        borderStyle.setBorderRight(BorderStyle.THIN);
        borderStyle.setRightBorderColor(new XSSFColor(Color.BLACK));
        XSSFCellStyle boldBorderStyle = (XSSFCellStyle) borderStyle.clone();
        boldBorderStyle.setFont(boldFont);

        // get review related objects
        Project project = (Project) request.getAttribute("project");
        Review review = (Review) request.getAttribute("review");
        Resource reviewer = (Resource) request.getAttribute("authorResource");
        Scorecard scorecard = (Scorecard) request.getAttribute("scorecardTemplate");

        int rowNum = 0;
        // header
        rowNum = exportHeader(sheet, rowNum, request, reviewType, project, review, reviewer, messages, boldStyle);

        // table header
        rowNum += 2;
        rowNum = exportTableHeader(sheet, rowNum, reviewType, boldBorderStyle, messages);

        // table content
        exportTableContent(sheet, rowNum, request, reviewType, scorecard, review, borderStyle, boldBorderStyle,
                messages);

        sheet.autoSizeColumn(0);
        sheet.setColumnWidth(1, (int) (12.5 * sheet.getColumnWidth(1)));
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            workbook.write(stream);
            stream.flush();
        } catch (IOException e) {
            throw new BaseException("Error occurs while exporting Excel sheet.", e);
        }
    }

    /**
     * Exports the header for the Excel sheet.
     * @param sheet
     *            the Excel sheet.
     * @param rowNum
     *            the starting row number.
     * @param request
     *            the HTTP request.
     * @param reviewType
     *            the review type.
     * @param project
     *            the project.
     * @param review
     *            the review.
     * @param reviewer
     *            the reviewer resource.
     * @param messages
     *            the message resources.
     * @param boldStyle
     *            the style with bold text.
     * @return the finishing row number.
     */
    private int exportHeader(Sheet sheet, int rowNum, HttpServletRequest request, String reviewType, Project project,
            Review review, Resource reviewer, MessageResources messages, XSSFCellStyle boldStyle) {
        Row row = null;
        XSSFCellStyle leftAlignCellStyle = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
        leftAlignCellStyle.setAlignment(HorizontalAlignment.LEFT);
        // header - project name
        row = sheet.createRow(rowNum++);
        fillCell(row, 0, boldStyle, null, messages.getMessage("exportReview.Header.Project"));
        fillCell(row, 1, null, null, (String) project.getProperty("Project Name"));
        // header - review id
        row = sheet.createRow(rowNum++);
        fillCell(row, 0, boldStyle, null, messages.getMessage("exportReview.Header.ReviewID"));
        fillCell(row, 1, leftAlignCellStyle, null, Long.parseLong(request.getParameter("rid")));
        // header - review type
        row = sheet.createRow(rowNum++);
        fillCell(row, 0, boldStyle, null, messages.getMessage("exportReview.Header.ReviewType"));
        fillCell(row, 1, null, null, reviewType);
        // header - author
        row = sheet.createRow(rowNum++);
        fillCell(row, 0, boldStyle, null, messages.getMessage("exportReview.Header.Author"));
        fillCell(row, 1, null, null, (String) reviewer.getProperty("Handle"));
        // header - submission
        if (request.getAttribute("sid") != null) {
            row = sheet.createRow(rowNum++);
            fillCell(row, 0, boldStyle, null, messages.getMessage("exportReview.Header.Submission"));
            fillCell(row, 1, leftAlignCellStyle, null, (Long) request.getAttribute("sid"));
        }
        // header - total score
        if (!reviewType.equals("Aggregation") && !reviewType.equals("Final Review")) {
            row = sheet.createRow(rowNum++);
            fillCell(row, 0, boldStyle, null, messages.getMessage("exportReview.Header.TotalScore"));
            fillCell(row, 1, leftAlignCellStyle, null, (double) review.getScore());
        }
        // header - status
        if (reviewType.equals("Specification Review") || reviewType.equals("Final Review")
                || reviewType.equals("Approval")) {
            row = sheet.createRow(rowNum++);
            fillCell(row, 0, boldStyle, null, messages.getMessage("exportReview.Header.Status"));
            if (reviewType.equals("Specification Review")) {
                fillCell(row, 1, null, null, getReviewStatus(review, "Specification Review Comment", messages));
            } else if (reviewType.equals("Final Review")) {
                fillCell(row, 1, null, null, getReviewStatus(review, "Final Review Comment", messages));
            } else if (reviewType.equals("Approval")) {
                fillCell(row, 1, null, null, getReviewStatus(review, "Approval Review Comment", messages));
            }
        }
        return rowNum;
    }

    /**
     * Gets the review status. It is ether approved or rejected.
     * @param review
     *            the review.
     * @param commentType
     *            the comment type.
     * @param messages
     *            the message resources.
     * @return the review status.
     */
    private String getReviewStatus(Review review, String commentType, MessageResources messages) {
        for (Comment comment : review.getAllComments()) {
            if (comment.getCommentType().getName().equals(commentType)) {
                if (comment.getExtraInfo() != null && comment.getExtraInfo().toString().length() > 0) {
                    if (comment.getExtraInfo().equals("Rejected")) {
                        return messages.getMessage("exportReview.TableHeader.Rejected");
                    } else {
                        return messages.getMessage("exportReview.TableHeader.Approved");
                    }
                }
                break;
            }
        }
        return "";
    }

    /**
     * Exports the table header for the Excel sheet.
     * @param sheet
     *            the Excel sheet.
     * @param rowNum
     *            the starting row number.
     * @param reviewType
     *            the review type.
     * @param boldBorderStyle
     *            the style with bold text and border.
     * @param messages
     *            the message resources.
     * @return the finishing row number.
     */
    private int exportTableHeader(Sheet sheet, int rowNum, String reviewType, XSSFCellStyle boldBorderStyle,
            MessageResources messages) {
        Row row = sheet.createRow(rowNum++);
        fillCell(row, 0, boldBorderStyle, null, messages.getMessage("exportReview.TableHeader.Name"));
        fillCell(row, 1, boldBorderStyle, null, messages.getMessage("exportReview.TableHeader.Description"));
        if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
            fillCell(row, 2, boldBorderStyle, null, messages.getMessage("exportReview.TableHeader.Reviewer"));
        } else {
            fillCell(row, 2, boldBorderStyle, null, messages.getMessage("exportReview.TableHeader.Weight"));
        }
        fillCell(row, 3, boldBorderStyle, null, messages.getMessage("exportReview.TableHeader.ResponseType"));
        if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
            fillCell(row, 4, boldBorderStyle, null, messages.getMessage("exportReview.TableHeader.Status"));
        } else {
            fillCell(row, 4, boldBorderStyle, null, messages.getMessage("exportReview.TableHeader.Score"));
        }
        return rowNum;
    }

    /**
     * Exports the table content for the Excel sheet.
     * @param sheet
     *            the Excel sheet.
     * @param rowNum
     *            the starting row number.
     * @param request
     *            the HTTP request.
     * @param reviewType
     *            the review type.
     * @param scorecard
     *            the scorecard.
     * @param review
     *            the review.
     * @param borderStyle
     *            the style with border.
     * @param boldBorderStyle
     *            the style with bold text and border.
     * @param messages
     *            the message resources.
     */
    private void exportTableContent(Sheet sheet, int rowNum, HttpServletRequest request, String reviewType,
            Scorecard scorecard, Review review, XSSFCellStyle borderStyle, XSSFCellStyle boldBorderStyle,
            MessageResources messages) {
        XSSFColor groupColor = new XSSFColor(new Color(commentExportColor.get("Group")));
        XSSFColor sectionColor = new XSSFColor(new Color(commentExportColor.get("Section")));
        XSSFColor questionColor = new XSSFColor(new Color(commentExportColor.get("Question")));
        XSSFColor attachmentColor = new XSSFColor(new Color(commentExportColor.get("Attachment")));
        XSSFCellStyle wrapTextStyle = (XSSFCellStyle) borderStyle.clone();
        wrapTextStyle.setWrapText(true);

        Row row = null;
        int groupIdx = 0;
        for (Group group : scorecard.getAllGroups()) {
            row = sheet.createRow(rowNum++);
            groupIdx++;
            fillCell(row, 0, boldBorderStyle, groupColor, messages.getMessage("exportReview.Table.Group", groupIdx));
            fillCell(row, 1, wrapTextStyle, groupColor, group.getName());
            if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
                fillCell(row, 2, borderStyle, groupColor, "");
            } else {
                fillCell(row, 2, borderStyle, groupColor, group.getWeight());
            }
            fillCell(row, 3, borderStyle, groupColor, "");
            fillCell(row, 4, borderStyle, groupColor, "");
            int sectionIdx = 0;
            for (Section section : group.getAllSections()) {
                row = sheet.createRow(rowNum++);
                sectionIdx++;
                fillCell(row, 0, boldBorderStyle, sectionColor,
                        messages.getMessage("exportReview.Table.Section", new Object[] { groupIdx, sectionIdx }));
                fillCell(row, 1, wrapTextStyle, sectionColor, section.getName());
                if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
                    fillCell(row, 2, borderStyle, sectionColor, "");
                } else {
                    fillCell(row, 2, borderStyle, sectionColor, section.getWeight());
                }
                fillCell(row, 3, borderStyle, sectionColor, "");
                fillCell(row, 4, borderStyle, sectionColor, "");
                int questionIdx = 0;
                for (Question question : section.getAllQuestions()) {
                    int startRow = rowNum;
                    if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
                        row = sheet.createRow(rowNum++);
                        questionIdx++;
                        fillCell(
                                row,
                                0,
                                boldBorderStyle,
                                questionColor,
                                messages.getMessage("exportReview.Table.Question", new Object[] { groupIdx, sectionIdx,
                                        questionIdx }));
                        fillCell(row, 1, wrapTextStyle, questionColor, question.getDescription());
                        fillCell(row, 2, borderStyle, questionColor, "");
                        fillCell(row, 3, borderStyle, questionColor, "");
                        fillCell(row, 4, borderStyle, questionColor, "");
                    }
                    for (Item item : review.getAllItems()) {
                        if (item.getQuestion() == question.getId()) {
                            if (!reviewType.equals("Aggregation") && !reviewType.equals("Final Review")) {
                                row = sheet.createRow(rowNum++);
                                questionIdx++;
                                fillCell(
                                        row,
                                        0,
                                        boldBorderStyle,
                                        questionColor,
                                        messages.getMessage("exportReview.Table.Question", new Object[] { groupIdx,
                                                sectionIdx, questionIdx }));
                                fillCell(row, 1, wrapTextStyle, questionColor, question.getDescription());
                                fillCell(row, 2, borderStyle, questionColor, question.getWeight());
                                fillCell(row, 3, borderStyle, questionColor, "");
                                fillCell(row, 4, borderStyle, questionColor, getReviewAnswer(question, item, messages));
                            }
                            // attached document
                            if (!reviewType.equals("Aggregation") && !reviewType.equals("Final Review")) {
                                if (item.getDocument() != null) {
                                    row = sheet.createRow(rowNum++);
                                    fillCell(row, 0, boldBorderStyle, attachmentColor,
                                            messages.getMessage("exportReview.Table.AttachedDocument"));
                                    String url = request.getRequestURL().toString();
                                    fillCell(
                                            row,
                                            1,
                                            borderStyle,
                                            attachmentColor,
                                            url.substring(0, url.lastIndexOf("/"))
                                                    + messages.getMessage("exportReview.Table.DownloadDocumentURL")
                                                    + item.getDocument());
                                    fillCell(row, 2, borderStyle, attachmentColor, "");
                                    fillCell(row, 3, borderStyle, attachmentColor, "");
                                    fillCell(row, 4, borderStyle, attachmentColor, "");
                                }
                            }
                            // comments
                            rowNum = exportComments(sheet, rowNum, request, reviewType, item, borderStyle,
                                    boldBorderStyle, messages);
                        }
                    }
                    // grouping
                    sheet.groupRow(startRow + 1, rowNum - 1);
                }
            }
        }
    }

    /**
     * Exports comments for one review item.
     * @param sheet
     *            the Excel sheet.
     * @param rowNum
     *            the starting row number.
     * @param request
     *            the HTTP request.
     * @param reviewType
     *            the review type.
     * @param item
     *            the review item.
     * @param boldBorderStyle
     *            the style with bold text and border.
     * @param borderStyle
     *            the style with border.
     * @param messages
     *            the message resources.
     * @return the finishing row number.
     */
    private int exportComments(Sheet sheet, int rowNum, HttpServletRequest request, String reviewType, Item item,
            XSSFCellStyle borderStyle, XSSFCellStyle boldBorderStyle, MessageResources messages) {
        int commentIdx = 1;
        Resource reviewer = null;
        Row row = null;
        XSSFCellStyle commentStyle = (XSSFCellStyle) borderStyle.clone();
        commentStyle.setWrapText(true);
        for (Comment comment : item.getAllComments()) {
            // skip empty comment
            if (comment.getComment().length() == 0) {
                continue;
            }
            String commentType = comment.getCommentType().getName();
            if ((reviewType.equals("Aggregation") && !aggregationCommentTypes.contains(commentType))
                    || (reviewType.equals("Final Review") && !finalReviewCommentTypes.contains(commentType))) {
                continue;
            }
            row = sheet.createRow(rowNum++);
            XSSFColor color = new XSSFColor(new Color(commentExportColor.get(comment.getCommentType().getName())));
            if (reviewerCommentTypes.contains(commentType)) {
                fillCell(row, 0, boldBorderStyle, color,
                        messages.getMessage("exportReview.Table.ReviewerResponse", commentIdx));
                commentIdx++;
            } else {
                fillCell(row, 0, boldBorderStyle, color, commentType);
            }
            fillCell(row, 1, commentStyle, color, comment.getComment());
            if (reviewType.equals("Aggregation") || reviewType.equals("Final Review")) {
                Resource[] reviewers = (Resource[]) request.getAttribute("reviewResources");
                boolean resourceFound = false;
                for (Resource resource : reviewers) {
                    if (resource.getId() == comment.getAuthor()) {
                        if (reviewer != null && reviewer != resource) {
                            commentIdx = 1;
                        }
                        reviewer = resource;
                        resourceFound = true;
                        fillCell(row, 2, borderStyle, color, (String) resource.getProperty("Handle"));
                        break;
                    }
                }
                if (!resourceFound) {
                    fillCell(row, 2, borderStyle, color, "");
                }
            } else {
                fillCell(row, 2, borderStyle, color, "");
            }
            fillCell(row, 3, borderStyle, color, commentType);
            if (reviewType.equals("Aggregation")) {
                if (reviewerCommentTypes.contains(commentType)) {
                    if (comment.getExtraInfo() != null && comment.getExtraInfo().toString().length() > 0) {
                        fillCell(row, 4, borderStyle, color,
                                messages.getMessage("AggregationItemStatus." + comment.getExtraInfo()));
                    }
                } else {
                    fillCell(row, 4, borderStyle, color, "");
                }
            } else if (reviewType.equals("Final Review")) {
                if (reviewerCommentTypes.contains(commentType)) {
                    if (comment.getExtraInfo() != null && comment.getExtraInfo().toString().length() > 0) {
                        if (comment.getExtraInfo().equals("Fixed")) {
                            fillCell(row, 4, borderStyle, color, messages.getMessage("FinalReviewItemStatus.Fixed"));
                        } else {
                            fillCell(row, 4, borderStyle, color, messages.getMessage("FinalReviewItemStatus.NotFixed"));
                        }
                    }
                } else {
                    fillCell(row, 4, borderStyle, color, "");
                }
            } else {
                fillCell(row, 4, borderStyle, color, "");
            }
        }
        return rowNum;
    }

    /**
     * Fills a spreadsheet cell using the given style and value.
     * @param row
     *            the row.
     * @param column
     *            the column number.
     * @param cellStyle
     *            the cell style.
     * @param fillColor
     *            the fill background color.
     * @param value
     *            the cell value.
     */
    private void fillCell(Row row, int column, XSSFCellStyle cellStyle, XSSFColor fillColor, Object value) {
        Cell cell = row.createCell(column);
        if (cellStyle != null) {
            if (fillColor != null) {
                cellStyle = (XSSFCellStyle) cellStyle.clone();
                cellStyle.setFillForegroundColor(fillColor);
                cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            }
            cell.setCellStyle(cellStyle);
        } else {
            cellStyle = (XSSFCellStyle) row.getSheet().getWorkbook().createCellStyle();
        }
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cell.setCellStyle(cellStyle);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            XSSFCellStyle numberCellStyle = (XSSFCellStyle) cellStyle.clone();
            DataFormat dataFormat = row.getSheet().getWorkbook().createDataFormat();
            numberCellStyle.setDataFormat(dataFormat.getFormat("0.00"));
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        }
    }

    /**
     * Gets review answer.
     * @param question
     *            the review question.
     * @param item
     *            the review item.
     * @param messages
     *            the message resources.
     * @return the review answer.
     */
    private String getReviewAnswer(Question question, Item item, MessageResources messages) {
        String questionType = question.getQuestionType().getName();
        String answer = item.getAnswer().toString();
        if (questionType.equals("Yes/No")) {
            return answer.equals("1") ? messages.getMessage("global.answer.Yes") : messages
                    .getMessage("global.answer.No");
        } else if (questionType.equals("Scale (1-4)")) {
            answer = answer.replace("/4", "");
            if (answer.length() > 0) {
                return messages.getMessage("Answer.Score4.ans" + answer);
            }
        } else if (questionType.equals("Scale (1-10)")) {
            answer = answer.replace("/10", "");
            return messages.getMessage("Answer.Score10.Rating.title") + " " + answer;
        } else if (questionType.equals("Test Case")) {
            return answer.replace("/", " " + messages.getMessage("editReview.Question.Response.TestCase.of") + " ");
        } else if (questionType.equals("Scale (0-3)")) {
            answer = answer.replace("/3", "");
            if (answer.length() > 0) {
                return messages.getMessage("Answer.Score3.ans" + answer);
            }
        } else if (questionType.equals("Scale (0-9)")) {
            answer = answer.replace("/9", "");
            return messages.getMessage("Answer.Score9.Rating.title") + " " + answer;
        } else if (questionType.equals("Scale (0-4)")) {
            answer = answer.replace("/4", "");
            if (answer.length() > 0) {
                return messages.getMessage("Answer.Score0_4.ans" + answer);
            }
        } else {
            return answer;
        }
        return "";
    }

    /**
     * Gets my 'submitter' resource.
     *
     * @param request the HttpServletRequest
     * @return my submitter resource
     */
    private static Resource getMySubmitterResource(HttpServletRequest request) {
        Resource[] myResources = ActionsHelper.getMyResourcesForPhase(request, null);
        for (Resource resource : myResources) {
            if (resource.getResourceRole().getName().equals("Submitter")) {
                return resource;
            }
        }
        return null;
    }

    /**
     *
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
     *
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

                    if (managerEdit) {
                        validateManagerComments(request, item, itemIdx);
                    } else {
                        validateScorecardComments(request, item, itemIdx);
                        if (question.isUploadDocument()) {
                            validateScorecardItemUpload(request, question, item, fileIdx++);
                        }
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
     * @throws IllegalArgumentException
     *             if parameters <code>request</code>, <code>scorecardTemplate</code>, or
     *             <code>aggregation</code> are <code>null</code>.
     */
    private static boolean validateAggregationScorecard(
            HttpServletRequest request, Scorecard scorecardTemplate, Review aggregation) {
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

                            if (commentType.equalsIgnoreCase("Aggregation Comment")) {
                                /* Request from David Messinger [11/06/2006]:
                                   No need to verify presence of comments
                                validateScorecardComment(request, comment, "aggregator_response[" + itemIdx + "]");
                                */

                                // But we still need to verify comment's maximum length.
                                String commentText = comment.getComment();
                                if (commentText != null && commentText.length() > MAX_COMMENT_LENGTH) {
                                    ActionsHelper.addErrorToRequest(request, "aggregator_response[" + itemIdx + "]",
                                        "Error.saveAggregation.Comment.MaxExceeded");
                                }
                            }
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

    /**
     *
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
//                questionType.equalsIgnoreCase("Scale (0-9)") || questionType.equalsIgnoreCase("Scale (0-3)") ||
//                questionType.equalsIgnoreCase("Scale (0-4)")) {
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
     * @param request
     * @param item
     * @param itemNum
     * @throws IllegalArgumentException
     *             if <code>request</code> or <code>item</code> parameters are
     *             <code>null</code>, or if <code>itemNum</code> parameter is negative (less
     *             than zero).
     */
    private static void validateScorecardComments(HttpServletRequest request, Item item, int itemNum) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(itemNum, "itemNum", 0, Integer.MAX_VALUE);

        /* Request from Jessica Williams [Sep 16 2011]:
           No need to verify presence of review item comments (for Studio but we do it for everything).
        */
        //boolean noCommentsEntered = true;

        //for (int i = 0; i < item.getNumberOfComments(); ++i) {
        //    if (ActionsHelper.isReviewerComment(item.getComment(i))) {
        //        noCommentsEntered = false;
        //        break;
        //    }
        //}

        //if (noCommentsEntered) {
        //    ActionsHelper.addErrorToRequest(request,
        //            "comment(" + itemNum + ".1)", "Error.saveReview.Comment.AtLeastOne");
        //    return;
        //}

        for (int i = 0; i < item.getNumberOfComments(); ++i) {
            Comment comment = item.getComment(i);

            if (ActionsHelper.isReviewerComment(comment)) {
                validateScorecardComment(request, comment, "comment(" + itemNum + "." + (i + 1) + ")");
            }
        }
    }

    /**
     * @param request
     * @param item
     * @param itemNum
     * @throws IllegalArgumentException
     *             if <code>request</code> or <code>item</code> parameters are
     *             <code>null</code>, or if <code>itemNum</code> parameter is negative (less
     *             than zero).
     */
    private static void validateManagerComments(HttpServletRequest request, Item item, int itemNum) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(item, "item");
        ActionsHelper.validateParameterInRange(itemNum, "itemNum", 0, Integer.MAX_VALUE);

        int managerCommentIdx = 0;
        for (int i = 0; i < item.getNumberOfComments(); ++i) {
            Comment comment = item.getComment(i);
            if (ActionsHelper.isManagerComment(comment)) {
                managerCommentIdx++;
                if (comment.getComment() != null && comment.getComment().length() > MAX_COMMENT_LENGTH) {
                    ActionsHelper.addErrorToRequest(request, "comment(" + itemNum + "." + managerCommentIdx + ")",
                            "Error.saveReview.Comment.MaxExceeded");
                }
            }
        }
    }

    /**
     * This static method validates single comment at a time. The comment must have its text to be
     * non-null and non-empty string to be regarded as passing validation.
     *
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
    private static void validateScorecardComment(
            HttpServletRequest request, Comment comment, String errorMessageProperty) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(request, "request");
        ActionsHelper.validateParameterNotNull(comment, "comment");
        ActionsHelper.validateParameterStringNotEmpty(errorMessageProperty, "errorMessageProperty");

        String commentText = comment.getComment();
        if (commentText == null || commentText.trim().length() == 0) {
            ActionsHelper.addErrorToRequest(request, errorMessageProperty, "Error.saveReview.Comment.Absent");
        } else if (commentText.length() > MAX_COMMENT_LENGTH) {
            ActionsHelper.addErrorToRequest(request, errorMessageProperty, "Error.saveReview.Comment.MaxExceeded");
        }
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
     *
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
     *
     *
     * @return
     * @param allComments
     */
    private static Comment getCommentAppeal(Comment[] allComments) {
        for (Comment comment : allComments) {
            if (comment.getCommentType().getName().equals("Appeal")) {
                return comment;
            }
        }
        return null;
    }

    /**
     *
     *
     * @return
     * @param allComments
     */
    private static Comment getCommentAppealResponse(Comment[] allComments) {
        for (Comment comment : allComments) {
            if (comment.getCommentType().getName().equals("Appeal Response")) {
                return comment;
            }
        }
        return null;
    }

    /**
     * This method is an implementation of &quot;View Scorecard&quot; Struts Action.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewScorecard.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about the error).
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
    public ActionForward viewScorecard(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws BaseException {

        try {
            // At this point, redirect-after-login attribute should be removed (if it exists)
            AuthorizationHelper.removeLoginRedirect(request);

            LoggingHelper.logAction(request);

            CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping, request, getResources(request));
            if (!verification.isSuccessful()) {
                return verification.getForward();
            }

            // Gather the roles the user has for current request
            AuthorizationHelper.gatherUserRoles(request);

            // Validate parameters
            ActionsHelper.validateParameterNotNull(mapping, "mapping");
            ActionsHelper.validateParameterNotNull(request, "request");

            // Verify that Scorecard ID was specified and denotes correct scorecard
            String scidParam = request.getParameter("scid");
            if (scidParam == null || scidParam.trim().length() == 0) {
               return(ActionsHelper.produceErrorReport(
                   mapping, getResources(request), request, null, "Error.ScorecardIdNotSpecified", null));
            }

            long scid;

            try {
               // Try to convert specified scid parameter to its integer representation
               scid = Long.parseLong(scidParam, 10);
            } catch (NumberFormatException e) {
               return(ActionsHelper.produceErrorReport(
                   mapping, getResources(request), request, null, "Error.ScorecardIdInvalid", null));
            }

            // Obtain an instance of Scorecard Manager
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
            Scorecard scorecardTemplate = null;
            try {
               // Get Scorecard by its id
               scorecardTemplate = scrMgr.getScorecard(scid);
            } catch (PersistenceException e) {
               // Eat the exception
            }

              // Verify that scorecard with specified ID exists
            if (scorecardTemplate == null) {
               return(ActionsHelper.produceErrorReport(
                   mapping, getResources(request), request, null, "Error.ScorecardNotFound", null));
            }

            // Verify the scorecard is active
            if (scorecardTemplate.getScorecardStatus().getId() != ACTIVE_SCORECARD) {
               return(ActionsHelper.produceErrorReport(
                   mapping, getResources(request), request, null, "Error.ScorecardNotActive", null));
            }

            // Place Scorecard template in the request
            request.setAttribute("scorecardTemplate", scorecardTemplate);

            // Signal about successful execution of the Action
            return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
        } catch (Throwable e) {
            StringWriter buf = new StringWriter();
            e.printStackTrace(new PrintWriter(buf));
            log.log(Level.ERROR, buf);
            throw new BaseException(e);
        }
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
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @since 1.1
     */
    public ActionForward createPostMortem(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws BaseException{
        LoggingHelper.logAction(request);
        return createGenericReview(mapping, form, request, "Post-Mortem");
    }

    /**
     * This method is an implementation of &quot;Edit Post-Mortem&quot; Struts Action defined for this
     * assembly, which is supposed to gather needed information (post-mortem and scorecard template)
     * and present it to editReview.jsp page, which will fill the required fields and post them to
     * the &quot;Save Post-Mortem&quot; action. The action implemented by this method is executed to
     * edit post-mortem that has already been created, but has not been submitted yet, and hence is
     * supposed to be edited.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @since 1.1
     */
    public ActionForward editPostMortem(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws BaseException{
        LoggingHelper.logAction(request);
        return editGenericReview(mapping, form, request, "Post-Mortem");
    }

    /**
     * This method is an implementation of &quot;Save Post-Mortem&quot; Struts Action defined for this
     * assembly, which is supposed to save information posted from /jsp/editReview.jsp page. This
     * method will either create new post-mortem or update (edit) an existing one depending on which
     * action was called to display /jsp/editReview.jsp page.
     *
     * @return &quot;success&quot; forward, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent submission id, or the lack of permissions, etc.).
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @since 1.1
     */
    public ActionForward savePostMortem(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return saveGenericReview(mapping, form, request, "Post-Mortem");
    }

    /**
     * This method is an implementation of &quot;View Post-Mortem&quot; Struts Action defined for this
     * assembly, which is supposed to view completed post-mortem.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @since 1.1
     */
    public ActionForward viewPostMortem(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return viewOrExportGenericReview(mapping, form, request, response, "Post-Mortem", false);
    }

    /**
     * This method is an implementation of &quot;Create Specification Review&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (scorecard template) and
     * present it to editSpecificationReview page, which will fill the required fields and post them to the
     * &quot;Save Specification Review&quot; action. The action implemented by this method is executed to edit
     * specification review that does not exist yet, and hence is supposed to be created.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editSpecificationReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent submission id, or the lack of
     *         permissions, etc.).
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @since 1.2
     */
    public ActionForward createSpecificationReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                   HttpServletResponse response) throws BaseException{
        LoggingHelper.logAction(request);
        return createGenericReview(mapping, form, request, "Specification Review");
    }

    /**
     * This method is an implementation of &quot;Edit Specification Review&quot; Struts Action defined for this
     * assembly, which is supposed to gather needed information (specification review scorecard template)
     * and present it to editSpecificationReview.jsp page, which will fill the required fields and post them to
     * the &quot;Save Specification Reviewquot; action. The action implemented by this method is executed to
     * edit specification review that has already been created, but has not been submitted yet, and hence is
     * supposed to be edited.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/editSpecificationReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @since 1.2
     */
    public ActionForward editSpecificationReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                 HttpServletResponse response) throws BaseException{
        LoggingHelper.logAction(request);
        return editGenericReview(mapping, form, request, "Specification Review");
    }

    /**
     * This method is an implementation of &quot;Save Specification Review&quot; Struts Action defined for this
     * assembly, which is supposed to save information posted from /jsp/editSpecificationReview.jsp page. This
     * method will either create new specification review or update (edit) an existing one depending on which
     * action was called to display /jsp/editSpecificationReview.jsp page.
     *
     * @return &quot;success&quot; forward, which forwards to the &quot;View Project Details&quot;
     *         action, or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect
     *         user input (such as absent submission id, or the lack of permissions, etc.).
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @since 1.2
     */
    public ActionForward saveSpecificationReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                 HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);
        return saveGenericReview(mapping, form, request, "Specification Review");
    }

    /**
     * This method is an implementation of &quot;View Specification Review&quot; Struts Action defined for this
     * assembly, which is supposed to view completed specification review.
     *
     * @return &quot;success&quot; forward, which forwards to the /jsp/viewSpecificationReview.jsp page (as
     *         defined in struts-config.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about an error that is
     *         usually caused by incorrect user input (such as absent review id, or the lack of
     *         permissions, etc.).
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @throws BaseException if any error occurs.
     * @since 1.2
     */
    public ActionForward viewSpecificationReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                 HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);
        return viewOrExportGenericReview(mapping, form, request, response, "Specification Review", false);
    }

    /**
     * This method is an implementation of &quot;Create Checkpoint Screening&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (scorecard template) and
     * present it to editReview.jsp page, which will fill the required fields and post them to the
     * &quot;Save Checkpoint Screening&quot; action. The action implemented by this method is executed to edit
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
    public ActionForward createCheckpointScreening(ActionMapping mapping, ActionForm form,
                                                  HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return createGenericReview(mapping, form, request, "Checkpoint Screening");
    }

    /**
     * This method is an implementation of &quot;Edit Checkpoint Screening&quot; Struts Action defined for this
     * assembly, which is supposed to gather needed information (screening and scorecard template)
     * and present it to editReview.jsp page, which will fill the required fields and post them to
     * the &quot;Save Checkpoint Screening&quot; action. The action implemented by this method is executed to
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
    public ActionForward editCheckpointScreening(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return editGenericReview(mapping, form, request, "Checkpoint Screening");
    }

    /**
     * This method is an implementation of &quot;Save Checkpoint Screening&quot; Struts Action defined for this
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
    public ActionForward saveCheckpointScreening(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return saveGenericReview(mapping, form, request, "Checkpoint Screening");
    }

    /**
     * This method is an implementation of &quot;View Checkpoint Screening&quot; Struts Action defined for this
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
    public ActionForward viewCheckpointScreening(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return viewOrExportGenericReview(mapping, form, request, response, "Checkpoint Screening", false);
    }



    /**
     * This method is an implementation of &quot;Create Checkpoint Review&quot; Struts Action defined for
     * this assembly, which is supposed to gather needed information (scorecard template) and
     * present it to editReview.jsp page, which will fill the required fields and post them to the
     * &quot;Save Checkpoint Review&quot; action. The action implemented by this method is executed to edit
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
    public ActionForward createCheckpointReview(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return createGenericReview(mapping, form, request, "Checkpoint Review");
    }

    /**
     * This method is an implementation of &quot;Edit Checkpoint Review&quot; Struts Action defined for this
     * assembly, which is supposed to gather needed information (screening and scorecard template)
     * and present it to editReview.jsp page, which will fill the required fields and post them to
     * the &quot;Save Checkpoint Review&quot; action. The action implemented by this method is executed to
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
    public ActionForward editCheckpointReview(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return editGenericReview(mapping, form, request, "Checkpoint Review");
    }

    /**
     * This method is an implementation of &quot;Save Checkpoint Review&quot; Struts Action defined for this
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
    public ActionForward saveCheckpointReview(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return saveGenericReview(mapping, form, request, "Checkpoint Review");
    }

    /**
     * This method is an implementation of &quot;View Checkpoint Review&quot; Struts Action defined for this
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
    public ActionForward viewCheckpointReview(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return viewOrExportGenericReview(mapping, form, request, response, "Checkpoint Review", false);
    }

    /**
     * This method is an implementation of &quot;Reopen Scorecard&quot; Struts Action defined for this
     * assembly, which is supposed to reopen a scorecard..
     *
     * @return an action forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to /jsp/confirmReopenScorecard.jsp page,
     *         which displays the confirmation dialog where user can confirm his intention to reopen the scorecard.
     *         If this action was called during the post back (the second time), then this method verifies if
     *         everything is correct, and process the reopen logic. After this it returns a forward to the
     *         View Project Details page.
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
     * @since 1.5
     */
    public ActionForward reopenScorecard(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, mapping,
                request, getResources(request));
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectReviewId(mapping, request, Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        if (!AuthorizationHelper.hasUserPermission(request, Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request),
                    request, Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME, "Error.NoPermission", Boolean.TRUE);
        }
        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        Review review = verification.getReview();
        Project project = verification.getProject();
        if (!review.isCommitted()) {
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME, "Error.ReopenReviewNotCommitted", null);
        }

        Phase[] allPhases = ActionsHelper.getPhasesForProject(ActionsHelper.createPhaseManager(false), project);
        if (!ActionsHelper.isPhaseOpen(ActionsHelper.findPhaseById(allPhases,
                review.getProjectPhase()).getPhaseStatus())) {
            // phase is not open
            return ActionsHelper.produceErrorReport(mapping, getResources(request), request,
                    Constants.REOPEN_REVIEW_SCORECARD_PERM_NAME, "Error.ReopenReviewPhaseNotOpen", null);
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("reopen") != null);

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), getResources(request));
            return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
        }

        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));
        review.setCommitted(false);
        ActionsHelper.createReviewManager().updateReview(review, operator);

        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME), "&pid=" + project.getId());
    }

    /**
     * This method is supposed to export completed review result into xlsx file.
     * @return always null because the resulting file is expected to be written to response directly.
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
    public ActionForward exportReview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);
        String reviewType = request.getParameter("reviewType");
        if (reviewType.equals("Aggregation")) {
            return viewOrExportAggregation(mapping, request, response, true);
        } else if (reviewType.equals("FinalReview")) {
            return viewOrExportFinalReview(mapping, request, response, true);
        } else {
            return viewOrExportGenericReview(mapping, form, request, response, reviewType, true);
        }
    }

    /**
     * This method is an implementation of &quot;Create Iterative Review&quot; Struts Action defined
     * for this assembly, which is supposed to gather needed information (scorecard template) and
     * present it to editReview.jsp page, which will fill the required fields and post them to the
     * &quot;Save Iterative Review&quot; Action. The action implemented by this method is executed
     * to edit iterative review that does not exist yet, and hence is supposed to be created.
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
    public ActionForward createIterativeReview(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return createGenericReview(mapping, form, request, "Iterative Review");
    }

    /**
     * This method is an implementation of &quot;Edit Iterative Review&quot; Struts Action defined
     * for this assembly, which is supposed to gather needed information (review and scorecard
     * template) and present it to editReview.jsp page, which will fill the required fields and
     * post them to the &quot;Save Iterative Review&quot; action. The action implemented by this
     * method is executed to edit iterative review that has already been created, but has not been
     * submitted yet, and hence is supposed to be edited.
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
    public ActionForward editIterativeReview(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return editGenericReview(mapping, form, request, "Iterative Review");
    }

    /**
     * This method is an implementation of &quot;Save Iterative Review&quot; Struts Action defined
     * for this assembly, which is supposed to save information posted from /jsp/editReview.jsp
     * page. This method will either create new review or update (edit) an existing one depending
     * on which action was called to display /jsp/editReview.jsp page.
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
    public ActionForward saveIterativeReview(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return saveGenericReview(mapping, form, request, "Iterative Review");
    }

    /**
     * This method is an implementation of &quot;View Iterative Review&quot; Struts Action defined
     * for this assembly, which is supposed to view completed review.
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
    public ActionForward viewIterativeReview(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        LoggingHelper.logAction(request);
        return viewOrExportGenericReview(mapping, form, request, response, "Iterative Review", false);
    }
}
