/*
 * Copyright (C) 2006 - 2013 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.model;

import com.cronos.onlinereview.util.ActionsHelper;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;

import com.topcoder.project.phases.Phase;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>This class defines a phase group bean.</p>
 * <p>
 * Thread safety: This class is not thread safe.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
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
    private final Set<String> accumulatedPhases = new HashSet<String>();

    /**
     * This member variable holds an array of registrants' emails that might have been assigned to
     * this phase group, or <code>null</code> value if no such array has been assigned to the
     * phase group.
     *
     * @see #getRegistrantsEmails
     * @see #setRegistrantsEmails
     */
    private String[] registrantsEmails = null;

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
     * This member variable holds an approval scorecards that might have been assigned assigned to
     * this phase group, or <code>null</code> value if no such scorecards has been assigned to the
     * phase group.
     *
     * @see #getApproval()
     * @see #setApproval(Review[])
     */
    private Review[] approval = null;

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
     * <p>A <code>Review</code> array listing the existing <code>Post-Mortem</code> reviews for project.</p>
     */
    private Review[] postMortemReviews;

    /**
     * <p>A <code>Resource</code> array listing the existing <code>Post-Mortem</code> reviewers for project.</p>
     */
    private Resource[] postMortemReviewers;

    /**
     * <p>A <code>Resource</code> array listing the existing <code>Approval</code> reviewers for project.</p>
     */
    private Resource[] approvalReviewers;

    /**
     * <p>A <code>long</code> referencing the current status of the <code>Screening</code> phase.</p>
     */
    private long screeningPhaseStatus;

    /**
     * <p>A <code>long</code> referencing the current status of the <code>Checkpoint Screening</code> phase.</p>
     */
    private long checkpointScreeningPhaseStatus;

    /**
     * <p>A <code>long</code> referencing the current status of the <code>Approval</code> phase.</p>
     */
    private long approvalPhaseStatus;

    /**
     * <p>A <code>long</code> referencing the current status of the <code>Post-Mortem</code> phase.</p>
     */
    private long postMortemPhaseStatus;

    /**
     * <p>A <code>Review</code> providing the details for Specification Review.</p>
     */
    private Review specificationReview;

    /**
     * <p>A <code>Resource</code> providing the details on specification reviewer.</p>
     */
    private Resource specificationReviewer;

    /**
     * <p>A <code>Submission</code> providing the details on specification submission.</p>
     */
    private Submission specificationSubmission;

    /**
     * <p>A <code>Resource</code> providing the details for specification submitter.</p>
     */
    private Resource specificationSubmitter;

    /**
     * <p>A <code>Submission[]</code> providing the list of checkpoint submissions.</p>
     */
    private Submission[] checkpointSubmissions;

    /**
     * <p>A <code>Resource</code> providing the details on checkpoint screener.</p>
     */
    private Resource checkpointScreener;

    /**
     * <p>A <code>Resource</code> providing the details on checkpoint reviewer.</p>
     */
    private Resource checkpointReviewer;

    /**
     * <p>A <code>Review[]</code> providing the list of checkpoint screening reviews.</p>
     */
    private Review[] checkpointScreeningReviews;

    /**
     * <p>A <code>Review[]</code> providing the list of checkpoint reviews.</p>
     */
    private Review[] checkpointReviews;

    /**
     * <p>A <code>Upload[][]</code> providing the list of past checkpoint submissions.</p>
     */
    private Upload[][] pastCheckpointSubmissions;

    /**
     * <p>A <code>boolean</code> providing the flag indicating if checkpoint review phase for project has finished or
     * not.</p>
     */
    private boolean checkpointReviewFinished;

    /**
     * <p>A <code>Phase</code> providing the iterative review phase.</p>
     */
    private Phase iterativeReviewPhase;

    /**
     * <p>A <code>Review[]</code> providing the details for iterative Reviews.</p>
     */
    private Review[] iterativeReviewReviews;

    /**
     * <p>A <code>Resource[]</code> providing the details on iterative reviewers.</p>
     */
    private Resource[] iterativeReviewers;

    /**
     * <p>A <code>Submission</code> providing the details on iterative review submission.</p>
     */
    private Submission iterativeReviewSubmission;

    /**
     * <p>A <code>Resource</code> providing the details for iterative review submitter.</p>
     */
    private Resource iterativeReviewSubmitter;

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
    public String[] getRegistrantsEmails() {
        return this.registrantsEmails;
    }

    /**
     * This method sets a reference to array of registrants' emails that should be assigned to this
     * phase group.
     *
     * @param registrantsEmails
     *            a reference to array of registrants' emails.
     */
    public void setRegistrantsEmails(String[] registrantsEmails) {
        this.registrantsEmails = registrantsEmails;
    }

    /**
     * This method returns array of Submitter resources who made their submissions for the project.
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
     * This method returns approval scorecards.
     *
     * @return an approval scorecards.
     */
    public Review[] getApproval() {
        return this.approval;
    }

    /**
     * This method sets a reference to an approval scorecards.
     *
     * @param approval
     *            a reference to approval scorecards.
     */
    public void setApproval(Review[] approval) {
        this.approval = approval;
    }

    /**
     * This method returns the Submitter resource that is a winner.
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
     * @return <code>true</code> if the phase has been opened, <code>false</code> if the phase
     *         is currently in scheduled state.
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

    /**
     * <p>Gets the existing <code>Post-Mortem</code> reviews for project.</p>
     *
     * @return a <code>Review</code> array listing the existing <code>Post-Mortem</code> reviews for project.
     */
    public Review[] getPostMortemReviews() {
        return this.postMortemReviews;
    }

    /**
     * <p>Sets the existing <code>Post-Mortem</code> reviews for project.</p>
     *
     * @param postMortemReviews a <code>Review</code> array listing the existing <code>Post-Mortem</code> reviews for
     *        project.
     */
    public void setPostMortemReviews(Review[] postMortemReviews) {
        this.postMortemReviews = postMortemReviews;
    }

    /**
     * <p>Gets the list of <code>Post-Mortem</code> reviewers assigned to project..</p>
     *
     * @return a <code>Resource</code> array listing the <code>Post-Mortem</code> reviewers.
     */
    public Resource[] getPostMortemReviewers() {
        return postMortemReviewers;
    }

    /**
     * <p>Sets the list of <code>Post-Mortem</code> reviewers assigned to project..</p>
     *
     * @param postMortemReviewers a <code>Resource</code> array listing the <code>Post-Mortem</code> reviewers.
     */
    public void setPostMortemReviewers(Resource[] postMortemReviewers) {
        this.postMortemReviewers = postMortemReviewers;
    }

    /**
     * <p>Gets the list of <code>Approval</code> reviewers assigned to project..</p>
     *
     * @return a <code>Resource</code> array listing the <code>Approval</code> reviewers.
     */
    public Resource[] getApprovalReviewers() {
        return approvalReviewers;
    }

    /**
     * <p>Gets the list of <code>Approval</code> reviewers assigned to project..</p>
     *
     * @param approvalReviewers a <code>Resource</code> array listing the <code>Approval</code> reviewers.
     */
    public void setApprovalReviewers(Resource[] approvalReviewers) {
        this.approvalReviewers = approvalReviewers;
    }

    /**
     * <p>Gets the current <code>Screening</code> phase status.</p>
     *
     * @return a <code>long</code> referencing the current status of the <code>Screening</code> phase.
     */
    public long getScreeningPhaseStatus() {
        return screeningPhaseStatus;
    }

    /**
     * <p>Sets the current <code>Screening</code> phase status.</p>
     *
     * @param screeningPhaseStatus a <code>long</code> referencing the current status of the <code>Screening</code> phase.
     */
    public void setScreeningPhaseStatus(long screeningPhaseStatus) {
        this.screeningPhaseStatus = screeningPhaseStatus;
    }

    /**
     * <p>Gets the current <code>Checkpoint Screening</code> phase status.</p>
     *
     * @return a <code>long</code> referencing the current status of the <code>Checkpoint Screening</code> phase.
     */
    public long getCheckpointScreeningPhaseStatus() {
        return checkpointScreeningPhaseStatus;
    }

    /**
     * <p>Sets the current <code>Checkpoint Screening</code> phase status.</p>
     *
     * @param checkpointScreeningPhaseStatus a <code>long</code> referencing the current status of the <code>Checkpoint Screening</code> phase.
     */
    public void setCheckpointScreeningPhaseStatus(long checkpointScreeningPhaseStatus) {
        this.checkpointScreeningPhaseStatus = checkpointScreeningPhaseStatus;
    }

    /**
     * <p>Gets the current <code>Approval</code> phase status.</p>
     *
     * @return a <code>long</code> referencing the current status of the <code>Approval</code> phase.
     */
    public long getApprovalPhaseStatus() {
        return approvalPhaseStatus;
    }

    /**
     * <p>Sets the current <code>Approval</code> phase status.</p>
     *
     * @param approvalPhaseStatus a <code>long</code> referencing the current status of the <code>Approval</code> phase.
     */
    public void setApprovalPhaseStatus(long approvalPhaseStatus) {
        this.approvalPhaseStatus = approvalPhaseStatus;
    }

    /**
     * <p>Gets the current <code>Post-Mortem</code> phase status.</p>
     *
     * @return a <code>long</code> referencing the current status of the <code>Post-Mortem</code> phase.
     */
    public long getPostMortemPhaseStatus() {
        return postMortemPhaseStatus;
    }

    /**
     * <p>Sets the current <code>Post-Mortem</code> phase status.</p>
     *
     * @param postMortemPhaseStatus a <code>long</code> referencing the current status of the <code>Post-Mortem</code>
     *        phase.
     */
    public void setPostMortemPhaseStatus(long postMortemPhaseStatus) {
        this.postMortemPhaseStatus = postMortemPhaseStatus;
    }

    /**
     * <p>Gets the details on specification submission.</p>
     *
     * @return a <code>Submission</code> providing the details on specification submission.
     */
    public Submission getSpecificationSubmission() {
        return this.specificationSubmission;
    }

    /**
     * <p>Sets the details on specification submission.</p>
     *
     * @param specificationSubmission a <code>Submission</code> providing the details on specification submission.
     */
    public void setSpecificationSubmission(Submission specificationSubmission) {
        this.specificationSubmission = specificationSubmission;
    }

    /**
     * <p>Gets the details on specification reviewer.</p>
     *
     * @return a <code>Resource</code> providing the details on specification reviewer.
     */
    public Resource getSpecificationReviewer() {
        return this.specificationReviewer;
    }

    /**
     * <p>Sets the details on specification reviewer.</p>
     *
     * @param specificationReviewer a <code>Resource</code> providing the details on specification reviewer.
     */
    public void setSpecificationReviewer(Resource specificationReviewer) {
        this.specificationReviewer = specificationReviewer;
    }

    /**
     * <p>Gets the details for Specification Review.</p>
     *
     * @return a <code>Review</code> providing the details for Specification Review.
     */
    public Review getSpecificationReview() {
        return this.specificationReview;
    }

    /**
     * <p>Sets the details for Specification Review.</p>
     *
     * @param specificationReview a <code>Review</code> providing the details for Specification Review.
     */
    public void setSpecificationReview(Review specificationReview) {
        this.specificationReview = specificationReview;
    }

    /**
     * <p>Gets the details for specification submitter.</p>
     *
     * @return a <code>Resource</code> providing the details for specification submitter.
     */
    public Resource getSpecificationSubmitter() {
        return this.specificationSubmitter;
    }

    /**
     * <p>Sets the details for specification submitter.</p>
     *
     * @param specificationSubmitter a <code>Resource</code> providing the details for specification submitter.
     */
    public void setSpecificationSubmitter(Resource specificationSubmitter) {
        this.specificationSubmitter = specificationSubmitter;
    }
    
    /**
     * <p>Gets the list of checkpoint reviews..</p>
     *
     * @return a <code>Review[]</code> providing the list of checkpoint reviews.
     */
    public Review[] getCheckpointReviews() {
        return this.checkpointReviews;
    }

    /**
     * <p>Sets the list of checkpoint reviews..</p>
     *
     * @param checkpointReviews a <code>Review[]</code> providing the list of checkpoint reviews.
     */
    public void setCheckpointReviews(Review[] checkpointReviews) {
        this.checkpointReviews = checkpointReviews;
    }

    /**
     * <p>Gets the list of checkpoint screening reviews.</p>
     *
     * @return a <code>Review[]</code> providing the list of checkpoint screening reviews.
     */
    public Review[] getCheckpointScreeningReviews() {
        return this.checkpointScreeningReviews;
    }

    /**
     * <p>Sets the list of checkpoint screening reviews.</p>
     *
     * @param checkpointScreeningReviews a <code>Review[]</code> providing the list of checkpoint screening reviews.
     */
    public void setCheckpointScreeningReviews(Review[] checkpointScreeningReviews) {
        this.checkpointScreeningReviews = checkpointScreeningReviews;
    }

    /**
     * <p>Gets the details on checkpoint reviewer.</p>
     *
     * @return a <code>Resource</code> providing the details on checkpoint reviewer.
     */
    public Resource getCheckpointReviewer() {
        return this.checkpointReviewer;
    }

    /**
     * <p>Sets the details on checkpoint reviewer.</p>
     *
     * @param checkpointReviewer a <code>Resource</code> providing the details on checkpoint reviewer.
     */
    public void setCheckpointReviewer(Resource checkpointReviewer) {
        this.checkpointReviewer = checkpointReviewer;
    }

    /**
     * <p>Gets the details on checkpoint screener.</p>
     *
     * @return a <code>Resource</code> providing the details on checkpoint screener.
     */
    public Resource getCheckpointScreener() {
        return this.checkpointScreener;
    }

    /**
     * <p>Sets the details on checkpoint screener.</p>
     *
     * @param checkpointScreener a <code>Resource</code> providing the details on checkpoint screener.
     */
    public void setCheckpointScreener(Resource checkpointScreener) {
        this.checkpointScreener = checkpointScreener;
    }

    /**
     * <p>Gets the list of checkpoint submissions.</p>
     *
     * @return a <code>Submission[]</code> providing the list of checkpoint submissions.
     */
    public Submission[] getCheckpointSubmissions() {
        return this.checkpointSubmissions;
    }

    /**
     * <p>Sets the list of checkpoint submissions.</p>
     *
     * @param checkpointSubmissions a <code>Submission[]</code> providing the list of checkpoint submissions.
     */
    public void setCheckpointSubmissions(Submission[] checkpointSubmissions) {
        this.checkpointSubmissions = checkpointSubmissions;
    }

    /**
     * <p>Gets the list of past checkpoint submissions.</p>
     *
     * @return a <code>Upload[][]</code> providing the list of past checkpoint submissions.
     */
    public Upload[][] getPastCheckpointSubmissions() {
        return this.pastCheckpointSubmissions;
    }

    /**
     * <p>Sets the list of past checkpoint submissions.</p>
     *
     * @param pastCheckpointSubmissions a <code>Upload[][]</code> providing the list of past checkpoint submissions.
     */
    public void setPastCheckpointSubmissions(Upload[][] pastCheckpointSubmissions) {
        this.pastCheckpointSubmissions = pastCheckpointSubmissions;
    }

    /**
     * <p>Gets the flag indicating if checkpoint review phase for project has finished or not.</p>
     *
     * @return a <code>boolean</code> providing the flag indicating if checkpoint review phase for project has finished
     *         or not.
     */
    public boolean getCheckpointReviewFinished() {
        return this.checkpointReviewFinished;
    }

    /**
     * <p>Sets the flag indicating if checkpoint review phase for project has finished or not.</p>
     *
     * @param checkpointReviewFinished a <code>boolean</code> providing the flag indicating if checkpoint review phase for
     *                                project has finished or not.
     */
    public void setCheckpointReviewFinished(boolean checkpointReviewFinished) {
        this.checkpointReviewFinished = checkpointReviewFinished;
    }

    /**
     * <p>Gets the iterative review phase.</p>
     *
     * @return a <code>Phase</code> providing the iterative review phase.
     */
    public Phase getIterativeReviewPhase() {
        return this.iterativeReviewPhase;
    }

    /**
     * <p>Sets the iterative review phase.</p>
     *
     * @param iterativeReviewPhase a <code>Phase</code> providing the iterative review phase.
     */
    public void setIterativeReviewPhase(Phase iterativeReviewPhase) {
        this.iterativeReviewPhase = iterativeReviewPhase;
    }

    /**
     * <p>Gets the details on iterative review submission.</p>
     *
     * @return a <code>Submission</code> providing the details on iterative review submission.
     */
    public Submission getIterativeReviewSubmission() {
        return this.iterativeReviewSubmission;
    }

    /**
     * <p>Sets the details on iterative review submission.</p>
     *
     * @param iterativeReviewSubmission a <code>Submission</code> providing the details on iterative review submission.
     */
    public void setIterativeReviewSubmission(Submission iterativeReviewSubmission) {
        this.iterativeReviewSubmission = iterativeReviewSubmission;
    }

    /**
     * <p>Gets the details on iterative reviewers.</p>
     *
     * @return a <code>Resource[]</code> providing the details on iterative reviewers.
     */
    public Resource[] getIterativeReviewers() {
        return this.iterativeReviewers;
    }

    /**
     * <p>Sets the details on iterative reviewers.</p>
     *
     * @param iterativeReviewers a <code>Resource[]</code> providing the details on iterative reviewers.
     */
    public void setIterativeReviewers(Resource[] iterativeReviewers) {
        this.iterativeReviewers = iterativeReviewers;
    }

    /**
     * <p>Gets the details for Iterative Reviews.</p>
     *
     * @return a <code>Review[]</code> providing the details for Iterative Reviews.
     */
    public Review[] getIterativeReviewReviews() {
        return this.iterativeReviewReviews;
    }

    /**
     * <p>Sets the details for Iterative Reviews.</p>
     *
     * @param iterativeReviewReviews a <code>Review[]</code> providing the details for Iterative Reviews.
     */
    public void setIterativeReviewReviews(Review[] iterativeReviewReviews) {
        this.iterativeReviewReviews = iterativeReviewReviews;
    }

    /**
     * <p>Gets the details for iterative review submitter.</p>
     *
     * @return a <code>Resource</code> providing the details for iterative review submitter.
     */
    public Resource getIterativeReviewSubmitter() {
        return this.iterativeReviewSubmitter;
    }

    /**
     * <p>Sets the details for iterative review submitter.</p>
     *
     * @param iterativeReviewSubmitter a <code>Resource</code> providing the details for iterative review submitter.
     */
    public void setIterativeReviewSubmitter(Resource iterativeReviewSubmitter) {
        this.iterativeReviewSubmitter = iterativeReviewSubmitter;
    }
}
