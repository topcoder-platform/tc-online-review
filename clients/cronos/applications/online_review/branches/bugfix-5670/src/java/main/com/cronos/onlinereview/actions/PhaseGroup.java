/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.cronos.onlinereview.autoscreening.management.ScreeningTask;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.project.phases.Phase;

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
     * This member variable holds the name of the table for this phase group. This name is usually
     * the same as the name of the phase group itself, but sometimes it may differ, e.g.
     * &quot;Registration&quot; tab can have table named &quot;Registrants&quot;, etc.
     *
     * @see #getTableName()
     * @see #setTableName(String)
     */
    private String tableName = "";

    /**
     * This member variable holds a custom string &#x96; an index of this phase group. If there are
     * more than one phase group of some type in the whole set, all subsequent phase groups
     * (starting from the second one) will be assigned their index. This index will be displayed to
     * user, so he/she can distinguish between two similarly-named phase groups.
     *
     * @see #getGroupIndex()
     * @see #setGroupIndex(String)
     */
    private String groupIndex = "";

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
     * This member variable is a set that lets the user determine whether some phase has been
     * grouped within this phase group. No two phases with the same name can be grouped into the
     * same phase group.
     *
     * @see #isPhaseInThisGroup(Phase)
     * @see #addPhase(Phase)
     */
    private final Set accumulatedPhases = new HashSet();

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
	 * This member variable determines whether an uploading of testcases is allowed for testcases
	 * type of reviewers.
	 *
	 * @see #isUploadingTestcasesAllowed()
	 * @see #setUploadingTestcasesAllowed(boolean)
	 */
    private boolean uploadingTestcasesAllowed = false;

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
     * This member variable determines if user sees any links to reviews on his page.
     *
     * @see #isDisplayReviewLinks()
     * @see #setDisplayReviewLinks(boolean)
     */
    private boolean displayReviewLinks = false;

    /**
     * This member variable determines whether &quot;Appeals&quot; phase is open or has ever been
     * opened. It is used to indicate if the appeals information should be available to present. The
     * default value of this member variable is <code>false</code>.
     */
    private boolean appealsPhaseOpened = false;

    /**
     * This member variable holds an array of counts of appeals put for every particular review.
     */
    private int totalAppealsCounts[][] = null;

    /**
     * This member variable holds an array of counts of appeals that haven't been resolved yet for
     * every particular review.
     */
    private int unresolvedAppealsCounts[][] = null;

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
     * This member variable holds a value that determines whether a link to commit Aggregation
     * Review can be displayed for the current phase group.
     *
     * @see #isDisplayAggregationReviewLink()
     * @see #setDisplayAggregationReviewLink(boolean)
     */
    private boolean displayAggregationReviewLink = false;

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
     * This method returns the name for this phase group's table.
     *
     * @return a table's name.
     */
    public String getTableName() {
        return (this.tableName != null) ? this.tableName : getName();
    }

    /**
     * This method sets a new name for this phase group's table. If the <code>tableName</code>
     * parameter is <code>null</code>, then the name of this group will be used.
     *
     * @param tableName
     *            a new name for the table.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * This method returns current index of this group.
     *
     * @return an index of this phase group.
     */
    public String getGroupIndex() {
        return this.groupIndex;
    }

    /**
     * This method sets a new index for this phase group.
     *
     * @param groupIndex
     *            a string representing new index to set for this new group.
     */
    public void setGroupIndex(String groupIndex) {
        this.groupIndex = groupIndex;
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
	 * This method determines whether uploading of testcases is allowed for testcases type of
	 * reviewers for this phase group. Usually, uploading of testcases is allowed as soon as Review
	 * phase is open till the end of the Appeals Response phase.
	 *
	 * @return <code>true</code> is testcases can be uploaded, <code>false</code> if they
	 *         cannot.
	 */
    public boolean isUploadingTestcasesAllowed() {
        return this.uploadingTestcasesAllowed;
    }

    /**
     * This method sets a permission to upload testcases.
     *
     * @param uploadingTestcasesAllowed
     *            a value determining whether uploading of testcases is allowed.
     */
    public void setUploadingTestcasesAllowed(boolean uploadingTestcasesAllowed) {
        this.uploadingTestcasesAllowed = uploadingTestcasesAllowed;
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
     * This method determines whether a user is allowed to see links to reviews when he opens a tab
     * which this phase group is assigned to.
     *
     * @return <code>true</code> if the user is allowed to see review links, <code>false</code>
     *         if he isn't.
     */
    public boolean isDisplayReviewLinks() {
        return this.displayReviewLinks;
    }

    /**
     * This method sets a value that determines if a user can see links to reviews.
     *
     * @param displayReviewLinks
     *            a new value.
     */
    public void setDisplayReviewLinks(boolean displayReviewLinks) {
        this.displayReviewLinks = displayReviewLinks;
    }

    /**
     * This method determines whether &quot;Appeals&quot; phase is open or has ever been opened.
     *
     * @return <code>true</code> if &quot;Appeals&quot; phase is currently open or was opened some
     *         time in the past, <code>false</code> if &quot;Appeals&quot; phase is in scheduled
     *         state, or there are no appeals phase for the current phase group.
     */
    public boolean getAppealsPhaseOpened() {
        return this.appealsPhaseOpened;
    }

    /**
     * This method sets the indicator of whether &quot;Appeals&quot; phase is open or has ever been
     * opened.
     *
     * @param appealsPhaseOpened
     *            a new value of type <code>boolean</code> to set.
     */
    public void setAppealsPhaseOpened(boolean appealsPhaseOpened) {
        this.appealsPhaseOpened = appealsPhaseOpened;
    }

    /**
     * This method returns an array containing total amount of appeals put for every review. The
     * counts are per review.
     *
     * @return an array containing appeals counts.
     */
    public int[][] getTotalAppealsCounts() {
        return this.totalAppealsCounts;
    }

    /**
     * This method assigns new array of appeals counts to this phase group.
     *
     * @param totalAppealsCounts
     *            a new array to assign to this phase group.
     */
    public void setTotalAppealsCounts(int[][] totalAppealsCounts) {
        this.totalAppealsCounts = totalAppealsCounts;
    }

    /**
     * This method returns an array containing amount of appeals that have not been resolved yet for
     * every review. The counts are per review.
     *
     * @return an array containing appeals counts.
     */
    public int[][] getUnresolvedAppealsCounts() {
        return this.unresolvedAppealsCounts;
    }

    /**
     * This method assigns new array of appeals counts to this phase group.
     *
     * @param unresolvedAppealsCounts
     *            a new array to assign to this phase group.
     */
    public void setUnresolvedAppealsCounts(int[][] unresolvedAppealsCounts) {
        this.unresolvedAppealsCounts = unresolvedAppealsCounts;
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
     * This method determines whether a link to commit Aggregation Review can be displayed for this
     * phase group.
     *
     * @return <code>true</code> if a link can be displayed, <code>false</code> if it cannot.
     */
    public boolean isDisplayAggregationReviewLink() {
        return this.displayAggregationReviewLink;
    }

    /**
     * This method sets status flag that determines whether a link to commit Aggregation Review can
     * be displayed for this phase group.
     *
     * @param displayAggregationReviewLink
     *            a value indicating whether a link can be displayed.
     */
    public void setDisplayAggregationReviewLink(boolean displayAggregationReviewLink) {
        this.displayAggregationReviewLink = displayAggregationReviewLink;
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

    /**
     * This method determines whether some this phase group already has a phase of similar type as
     * that one specified via <code>phase</code> parameter.
     *
     * @return <code>true</code> if this phase group already contains a phase of the same type as
     *         the one specified by <code>phase</code> parameter, or <code>false</code> if no
     *         such phase has been added to this phase group yet.
     * @param phase
     *            a phase which type needs to be tested.
     * @throws IllegalArgumentException
     *             if <code>phase</code> parameter is <code>null</code>.
     */
    public boolean isPhaseInThisGroup(Phase phase) {
        // Validate parameter
        ActionsHelper.validateParameterNotNull(phase, "phase");

        return accumulatedPhases.contains(phase.getPhaseType().getName().toLowerCase());
    }

    /**
     * This method adds a phase to this group. This does not mean a reference to a phase is actually
     * stored somewhere inside this object, but a mere name of the phase's type in stored into inner
     * variable, so it can be determined if some phase type is already contained inside the phase
     * group represented by this class. This method does not verify if some phase type has already
     * been contained in phase group. It is a user's responsibility to do that.
     *
     * @param phase
     *            a phase to add to this phase group.
     * @throws IllegalArgumentException
     *             if <code>phase</code> parameter is <code>null</code>.
     */
    public void addPhase(Phase phase) {
        // Validate parameter
        ActionsHelper.validateParameterNotNull(phase, "phase");

        accumulatedPhases.add(phase.getPhaseType().getName().toLowerCase());
    }
}
