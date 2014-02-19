/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.review.scorecalculator.ScoreCalculator;
import com.topcoder.management.review.scorecalculator.ScorecardMatrix;
import com.topcoder.management.review.scorecalculator.builders.DefaultScorecardMatrixBuilder;
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
import com.topcoder.util.weightedcalculator.LineItem;

/**
 * This class is the struts action class which is used to view the composite scorecard.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ViewCompositeScorecardAction extends BaseViewOrExportGenericReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -4591318881265297795L;

    /**
     * This method is an implementation of &quot;View Composite Scorecard&quot; Struts Action
     * defined for this assembly, which is supposed to gather needed information (scorecard template
     * and reviews from individual reviewers for some submission) and present it to
     * viewCompositeScorecard.jsp page, which will present all the gathered information to the user.
     *
     * @return &quot;success&quot;, which forwards to the /jsp/viewCompositeScorecard.jsp
     *         page (as defined in struts.xml file), or &quot;userError&quot; forward, which
     *         forwards to the /jsp/userError.jsp page, which displays information about an error
     *         that is usually caused by incorrect user input (such as absent submission id, or the
     *         lack of permissions, etc.).
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before proceeding with the Action
        verification = checkForCorrectSubmissionId(request, Constants.VIEW_COMPOS_SCORECARD_PERM_NAME);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that currently logged in user has enough rights to proceed with the action
        if (!AuthorizationHelper.hasUserPermission(request, Constants.VIEW_COMPOS_SCORECARD_PERM_NAME)) {
            return ActionsHelper.produceErrorReport(this, request,
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
                return ActionsHelper.produceErrorReport(this, request,
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
                return ActionsHelper.produceErrorReport(this, request,
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
            return ActionsHelper.produceErrorReport(this, request,
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
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.VIEW_COMPOS_SCORECARD_PERM_NAME, "Error.CompositeScorecardIsNotReady", null);
        }

        // Verify that number of items in every review scorecard match the number of questions in scorecard template
        for (Review review : reviews) {
            if (review.getNumberOfItems() != questionsCount) {
                return ActionsHelper.produceErrorReport(this, request,
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
            // Find a reviewer that is author of the current review
            for (Resource reviewer1 : reviewers) {
                if (review.getAuthor() == reviewer1.getId()) {
                    reviewer = reviewer1;
                    break;
                }
            }

            if (reviewer == null) {
                return ActionsHelper.produceErrorReport(this, request,
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
                            LoggingHelper.logWarning("line item for question id: " + question.getId() +
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

        request.setAttribute("tableTitle", getText("editReview.CompositeScorecard.title"));

        return Constants.SUCCESS_FORWARD_NAME;
    }
}

