/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.Date;

import com.cronos.onlinereview.autoscreening.management.ScreeningTask;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;

/**
 * This class defines a phase group bean.
 *
 * @author George1
 * @author real_vg
 * @version 1.0
 */
public class PhaseGroup {

    /**
     * This member variable holds the name of this group. This member variable is initialized in the
     * constructor and can be accessed and changed via corresponding get/set methods. The default
     * value of this variable is an empty string.
     *
     * @see #getName()
     * @see #setName(String)
     */
    private String name = "";

    /**
     * This member variable holds the name of application's functionality that should be executed to
     * initially collect all required information for this phase group, and later present it
     * properly to the end user. This member variable is initialized in the constructor and can be
     * accessed and changed via corresponding get/set methods. The default value of this variable is
     * an empty string.
     *
     * @see #getAppFunc()
     * @see #setAppFunc(String)
     */
    private String applicationFunction = "";

    /**
     * This member variable holds an array of registrants' emails that might have been assigned to
     * this phase group, or <code>null</code> value if no such array has been assigned to the
     * phase group.
     *
     * @see #getRegistantsEmails()
     * @see #setRegistantsEmails(String[])
     */
    private String[] registantsEmails = null;

    /**
     * This member variable holds an array of submitters who made their submissions for the project.
     *
     * @see #getSubmitters()
     * @see #setSubmitters(Resource[])
     */
    private Resource[] submitters = null;

    /**
     * This member variable holds an array of submissions that might have been assigned to this
     * phase group, or <code>null</code> value if no such array has been assigned to the phase
     * group.
     *
     * @see #getSubmissions()
     * @see #setSubmissions(Submission[])
     */
    private Submission[] submissions = null;

    /**
     * This member variable holds an array of uploads of Submission type which were deleted due to a
     * newer version has been uploaded by Submitter, or <code>null</code> value if no such array
     * has been assigned to the phase group.
     *
     * @see #getPastSubmissions()
     * @see #setPastSubmissions(Upload[][])
     */
    private Upload[][] pastSubmissions = null;

    /**
     * This member variable holds an array of screening tasks that might have been assigned to this
     * phase group, or <code>null</code> value if no such array has been assigned to the phase
     * group.
     *
     * @see #getScreeningTasks()
     * @see #setScreeningTasks(ScreeningTask[])
     */
    private ScreeningTask[] screeningTasks = null;

    /**
     * This member variable holds an array of reviewers that might have been assigned to this phase
     * group, or <code>null</code> value if no such array has been assigned to the phase group.
     *
     * @see #getReviewers()
     * @see #setReviews(Review[][])
     */
    private Resource[] reviewers = null;

    /**
     * This member variable holds an array of screenings that might have been assigned to this phase
     * group, or <code>null</code> value if no such array has been assigned to the phase group.
     *
     * @see #getScreenings()
     * @see #setScreenings(Review[])
     */
    private Review[] screenings = null;

    /**
     * This member variable holds an array of reviews that might have been assigned to this phase
     * group, or <code>null</code> value if no such array has been assigned to the phase group.
     *
     * @see #getReviews()
     * @see #setReviews(Review[][])
     */
    private Review[][] reviews = null;

    /**
     * This member variable holds an array of test case uploads that might have been assigned to
     * this phase group, or <code>null</code> value if no such array has been assigned to the
     * phase group.
     *
     * @see #getTestCases()
     * @see #setTestCases(Upload[])
     */
    private Upload[] testCases = null;

    /**
     * This member variable holds an array of dated when certain reviews were committed that might
     * have been assigned to this phase group, or <code>null</code> value if no such array has
     * been assigned to the phase group.
     *
     * @see #getReviewDates()
     * @see #setReviewDates(Date[])
     */
    private Date[] reviewDates = null;

    /**
     * This member variable holds an aggregation scorecard that might have been assigned assigned to
     * this phase group, or <code>null</code> value if no such scorecard has been assigned to the
     * phase group.
     *
     * @see #getAggregation()
     * @see #setAggregation(Review)
     */
    private Review aggregation = null;

    /**
     * This member variable holds a value that determines whether aggregation scorecard has been
     * committed.
     *
     * @see #isAggregationReviewCommitted()
     * @see #setAggregationReviewCommitted(boolean)
     */
    private boolean aggregationReviewCommitted = false;

    /**
     * This member variable holds an upload for a final fix that might might have been assigned
     * assigned to this phase group, or <code>null</code> value if no such upload has been
     * assigned to the phase group.
     *
     * @see #getFinalFix()
     * @see #setFinalFix(Upload)
     */
    private Upload finalFix = null;

    /**
     * This member variable holds a final review scorecard that might have been assigned assigned to
     * this phase group, or <code>null</code> value if no such scorecard has been assigned to the
     * phase group.
     *
     * @see #getFinalReview()
     * @see #setFinalReview(Review)
     */
    private Review finalReview = null;

    /**
     * This member variable holds a value that determines whether final review scorecard has been
     * committed.
     *
     * @see #isFinalReviewCommitted()
     * @see #setFinalReviewCommitted(boolean)
     */
    private boolean finalReviewCommitted = false;

    /**
     * This member variable holds an approval scorecard that might have been assigned assigned to
     * this phase group, or <code>null</code> value if no such scorecard has been assigned to the
     * phase group.
     *
     * @see #getApproval()
     * @see #setApproval(Review)
     */
    private Review approval = null;

    /**
     * This member variable holds a reference to the winner resource that might have been assigned
     * to this phase group, or <code>null</code> value if no such resource has been assigned to
     * the phase group.
     *
     * @see #getWinner()
     * @see #setWinner(Resource)
     */
    private Resource winner = null;

    /**
     * This member variable holds a reference to the object containing additional info for this
     * group. This object can typically be an array or a list. This member variable is initialized
     * in the constructor and can be accessed and changed via corresponding get/set methods. The
     * default value of this variable is <code>null</code>.
     *
     * @see #getAdditionalInfo()
     * @see #setAdditionalInfo(Object)
     */
    private Object additionalInfo = null;

    /**
     * This member variable indicates if the phase represented by this phase group has been opened.
     */
    private boolean phaseOpen = false;

    /**
     * Constructs a new instance of the <code>PhaseGroup</code> class setting all fields to their
     * default values.
     */
    public PhaseGroup() {
    }

    /**
     * This method returns the name of this group.
     *
     * @return the name of this group.
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method sets new name of this group. Name cannot be <code>null</code>, but can be an
     * empty string.
     *
     * @param name
     *            a new name to set.
     * @throws IllegalArgumentException
     *             if parameter <code>name</code> is <code>null</code>.
     */
    public void setName(String name) {
        // Validate parameter
        ActionsHelper.validateParameterNotNull(name, "name");

        this.name = name;
    }

    /**
     * This method returns the name of application's functionality that should be executed for this
     * phase group.
     *
     * @return the name of application's functionality.
     */
    public String getAppFunc() {
        return this.applicationFunction;
    }

    /**
     * This method sets new name of application's functionality that should be executed for this
     * phase group.
     *
     * @param appFunc
     *            a new name of application's functionality. This parameter cannot be
     *            <code>null</code>, but can be an empty string.
     * @throws IllegalArgumentException
     *             if <code>appFunc</code> parameter is <code>null</code>.
     */
    public void setAppFunc(String appFunc) {
        // Validate parameter
        ActionsHelper.validateParameterNotNull(appFunc, "appFunc");

        this.applicationFunction = appFunc;
    }

    /**
     * This method returns a reference to array of registrants' emails.
     *
     * @return an array of registrants' emails, or <code>null</code> if no such array has been
     *         assigned to this phase group.
     */
    public String[] getRegistantsEmails() {
        return this.registantsEmails;
    }

    /**
     * This method sets a reference to array of registrants' emails that should be assignedto this
     * phase group.
     *
     * @param registantsEmails
     *            a reference to array of registrants' emails.
     */
    public void setRegistantsEmails(String[] registantsEmails) {
        this.registantsEmails = registantsEmails;
    }

    /**
     * This method returns array of Submitter resources who made ther submissions for the project.
     *
     * @return an array of submitters.
     */
    public Resource[] getSubmitters() {
        return this.submitters;
    }

    /**
     * This method sets a reference to array of submitters.
     *
     * @param submitters
     *            a reference to array of submitters.
     */
    public void setSubmitters(Resource[] submitters) {
        this.submitters = submitters;
    }

    /**
     * This method returns array of submissions that could have been set for this phase group.
     *
     * @return an array of submissions.
     */
    public Submission[] getSubmissions() {
        return this.submissions;
    }

    /**
     * This method sets a reference to array of submissions.
     *
     * @param submissions
     *            a reference to array of submissions.
     */
    public void setSubmissions(Submission[] submissions) {
        this.submissions = submissions;
    }

    /**
     * This method returns array of uploads of Submission type that could have been assigned to this
     * phase group.
     *
     * @return an array of uploads.
     */
    public Upload[][] getPastSubmissions() {
        return this.pastSubmissions;
    }

    /**
     * This method sets a reference to array of uploads of Submission type.
     *
     * @param pastSubmissions
     *            a reference to array of uploads.
     */
    public void setPastSubmissions(Upload[][] pastSubmissions) {
        this.pastSubmissions = pastSubmissions;
    }

    public ScreeningTask[] getScreeningTasks() {
        return this.screeningTasks;
    }

    public void setScreeningTasks(ScreeningTask[] screeningTasks) {
        this.screeningTasks = screeningTasks;
    }

    /**
     * This method returns array of reviewers assigned to this phase group.
     *
     * @return an array of reviewers.
     */
    public Resource[] getReviewers() {
        return this.reviewers;
    }

    /**
     * This method sets a reference to array of reviewers.
     *
     * @param reviewers
     *            a reference to array of reviewers.
     */
    public void setReviewers(Resource[] reviewers) {
        this.reviewers = reviewers;
    }

    /**
     * This method returns array of screening reviews assigned to this phase group.
     *
     * @return an array of screening reviews.
     */
    public Review[] getScreenings() {
        return this.screenings;
    }

    /**
     * This method sets a reference to array of screening reviews.
     *
     * @param screenings
     *            a reference to array of screening reviews.
     */
    public void setScreenings(Review[] screenings) {
        this.screenings = screenings;
    }

    /**
     * This method returns array of reviews assigned to this phase group.
     *
     * @return an array of reviews.
     */
    public Review[][] getReviews() {
        return this.reviews;
    }

    /**
     * This method sets a reference to array of reviews.
     *
     * @param reviews
     *            a reference to array of reviews.
     */
    public void setReviews(Review[][] reviews) {
        this.reviews = reviews;
    }

    /**
     * This method returns array of uploaded test cases assigned to this phase group.
     *
     * @return an array of uploaded test cases.
     */
    public Upload[] getTestCases() {
        return this.testCases;
    }

    /**
     * This method sets a reference to array of uploaded test cases.
     *
     * @param testCases
     *            a reference to array of uploaded test cases.
     */
    public void setTestCases(Upload[] testCases) {
        this.testCases = testCases;
    }

    /**
     * This method returns array of dates when the reviews were completed cases assigned to this
     * phase group.
     *
     * @return an array of dates.
     */
    public Date[] getReviewDates() {
        return this.reviewDates;
    }

    /**
     * This method sets a reference to array of dates when reviews were completed.
     *
     * @param reviewDates
     *            a reference to array of dates.
     */
    public void setReviewDates(Date[] reviewDates) {
        this.reviewDates = reviewDates;
    }

    /**
     * This method returns review scorecard of type Aggregation or Aggregation Review.
     *
     * @return an appropriate review object.
     */
    public Review getAggregation() {
        return this.aggregation;
    }

    /**
     * This method sets a reference to a review scorecard of type Aggregation or Aggregation Review.
     *
     * @param aggregation
     *            a reference to review object.
     */
    public void setAggregation(Review aggregation) {
        this.aggregation = aggregation;
    }

    /**
     * This method determines whether Aggregation Review has been committed by all users who had to
     * do that.
     *
     * @return <code>true</code> if aggregation review has been committed by all users who had to
     *         do that, <code>false</code> if at least one user did not do that.
     */
    public boolean isAggregationReviewCommitted() {
        return this.aggregationReviewCommitted;
    }

    /**
     * This method sets status flag that determines whether Aggregation Review has been committed.
     *
     * @param aggregationReviewCommitted
     *            a value indicating whether Aggregation Review has been committed.
     */
    public void setAggregationReviewCommitted(boolean aggregationReviewCommitted) {
        this.aggregationReviewCommitted = aggregationReviewCommitted;
    }

    /**
     * This method returns upload object for final fix.
     *
     * @return an upload for final fix.
     */
    public Upload getFinalFix() {
        return this.finalFix;
    }

    /**
     * This method sets a reference to an upload object for final fix.
     *
     * @param finalFix
     *            a reference to upload.
     */
    public void setFinalFix(Upload finalFix) {
        this.finalFix = finalFix;
    }

    /**
     * This method returns review scorecard of type Final Review.
     *
     * @return a final review object.
     */
    public Review getFinalReview() {
        return this.finalReview;
    }

    /**
     * This method sets a reference to a review scorecard of type Final Review.
     *
     * @param finalReview
     *            a reference to final review object.
     */
    public void setFinalReview(Review finalReview) {
        this.finalReview = finalReview;
    }

    /**
     * This method determines whether Final Review has been committed.
     *
     * @return <code>true</code> if final review has been committed, <code>false</code> if it
     *         hasn't.
     */
    public boolean isFinalReviewCommitted() {
        return this.finalReviewCommitted;
    }

    /**
     * This method sets status flag that determines whether Final Review has been committed.
     *
     * @param finalReviewCommitted
     *            a value indicating whether Final Review has been committed.
     */
    public void setFinalReviewCommitted(boolean finalReviewCommitted) {
        this.finalReviewCommitted = finalReviewCommitted;
    }

    /**
     * This method returns approval scorecard.
     *
     * @return an approval scorecard.
     */
    public Review getApproval() {
        return this.approval;
    }

    /**
     * This method sets a reference to an approval scorecard.
     *
     * @param approval
     *            a reference to approval scorecard.
     */
    public void setApproval(Review approval) {
        this.approval = approval;
    }

    /**
     * This method returns the Submitter resource that is a winnier.
     *
     * @return a winner resource.
     */
    public Resource getWinner() {
        return this.winner;
    }

    /**
     * This method associates new winner resource with the phase group.
     *
     * @param winner
     *            a new winner resource to set for this phase group.
     */
    public void setWinner(Resource winner) {
        this.winner = winner;
    }

    /**
     * This method returns additional info that could have been set for this phase group.
     *
     * @return an object representing additional info.
     */
    public Object getAdditionalInfo() {
        return this.additionalInfo;
    }

    /**
     * This method sets additional info object that can be needed to correctly display information
     * about this phase group.
     *
     * @param additionalInfo
     *            a reference to some object containing additional info. This parameter can be
     *            <code>null</code> if no additional info is needed.
     */
    public void setAdditionalInfo(Object additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    /**
     * This method returns a value indication whether the phase represented by this phase group has
     * been opened.
     *
     * @return <code>true</code> if the phase has been opened, <code>false</code> if hte phase
     *         is surrently in scheduled state.
     */
    public boolean isPhaseOpen() {
        return this.phaseOpen;
    }

    /**
     * Ths method sets an indication of whether the phase represented by this phase group has been
     * opened. If this bean represents several phases, then value <code>true</code> indicates that
     * at least one of them has been opened.
     *
     * @param phaseOpen
     *            a value indicating whether the phase has been opened.
     */
    public void setPhaseOpen(boolean phaseOpen) {
        this.phaseOpen = phaseOpen;
    }
}
