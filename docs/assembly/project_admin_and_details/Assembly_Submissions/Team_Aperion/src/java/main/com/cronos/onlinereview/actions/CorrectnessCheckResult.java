/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import org.apache.struts.action.ActionForward;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;

/**
 * This class is bean that provides a convenient way to return several values from some checkForXXX
 * methods and indicate success/failure of the check performed by those methods.
 * <p>
 * This class is not thread safe.
 * </p>
 *
 * @author TCSAssemblyTeam
 * @version 1.0
 */
final class CorrectnessCheckResult {

    // --------------------------------------------------------------------- Member variables -----

    /**
     * This member variable contains a reference to action forward assigned to this bean, or
     * <code>null</code> if no action forward has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * approptiate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private ActionForward forward = null;

    /**
     * This member variable contains a reference to Project Manager assigned to this bean, or
     * <code>null</code> if no Project Manager has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * approptiate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private ProjectManager projectManager = null;

    /**
     * This member variable contains a reference to Upload Manager assigned to this bean, or
     * <code>null</code> if no Upload Manager has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * approptiate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private UploadManager uploadManager = null;

    /**
     * This member variable contains a reference to a project assigned to this bean, or
     * <code>null</code> if no project has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * approptiate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private Project project = null;

    /**
     * This member variable contains a reference to a phase assigned to this bean, or
     * <code>null</code> if no phase has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * approptiate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private Phase phase = null;

    /**
     * This member variable contains a reference to a submission assigned to this bean, or
     * <code>null</code> if no submission has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * approptiate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private Submission submission = null;

    /**
     * This member variable contains a reference to a review assigned to this bean, or
     * <code>null</code> if no review has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * approptiate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private Review review = null;

    /**
     * This member variable contains a reference to a scorecard template assigned to this bean, or
     * <code>null</code> if no scorecard template has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * approptiate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private Scorecard scorecardTemplate = null;


    // -------------------------------------------------------------------------- Constructor -----

    /**
     * Creates a new instance of the <code>CorrectnessCheckResult</code> class setting all its
     * members to default values.
     */
    public CorrectnessCheckResult() {
    }


    // ---------------------------------------------------------------------- get/set methods -----

    /**
     * This method retrieves action forward value that could have been assigned to this bean
     * earlier.
     *
     * @return an instance of the <code>ActionForward</code> class, or <code>null</code> if no
     *         action forward has been assigned to this bean.
     */
    public ActionForward getForward() {
        return this.forward;
    }

    /**
     * This method assigns an action forward object to this bean.
     *
     * @param forward
     *            an instance of the <code>ActionForward</code> class to assign.
     */
    public void setForward(ActionForward forward) {
        this.forward = forward;
    }

    /**
     * This method retrieves project manager object that could have been assigned to this bean
     * earlier.
     *
     * @return an instance of the <code>ProjectManager</code> class, or <code>null</code> if no
     *         project manager has been assigned to this bean.
     */
    public ProjectManager getProjectManager() {
        return this.projectManager;
    }

    /**
     * This method assigns a project manager object to this bean.
     *
     * @param projectManager
     *            an instance of the <code>ProjectManager</code> class to assign.
     */
    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * This method retrieves upload manager object that could have been assigned to this bean
     * earlier.
     *
     * @return an instance of the <code>UploadManager</code> class, or <code>null</code> if no
     *         upload manager has been assigned to this bean.
     */
    public UploadManager getUploadManager() {
        return this.uploadManager;
    }

    /**
     * This method assigns an upload manager object to this bean.
     *
     * @param uploadManager
     *            an instance of the <code>UploadManager</code> class to assign.
     */
    public void setUploadManager(UploadManager uploadManager) {
        this.uploadManager = uploadManager;
    }

    /**
     * This method retrieves project object that could have been assigned to this bean earlier.
     *
     * @return an instance of the <code>Project</code> class, or <code>null</code> if no project
     *         has been assigned to this bean.
     */
    public Project getProject() {
        return this.project;
    }

    /**
     * This method assigns a project object to this bean.
     *
     * @param project
     *            an instance of the <code>Project</code> class to assign.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * This method retrieves phase object that could have been assigned to this bean earlier.
     *
     * @return an instance of the <code>Phase</code> class, or <code>null</code> if no phase has
     *         been assigned to this bean.
     */
    public Phase getPhase() {
        return this.phase;
    }

    /**
     * This method assigns a phase object to this bean.
     *
     * @param project
     *            an instance of the <code>Phase</code> class to assign.
     */
    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    /**
     * This method retrieves submission object that could have been assigned to this bean earlier.
     *
     * @return an instance of the <code>Submission</code> class, or <code>null</code> if no
     *         submission has been assigned to this bean.
     */
    public Submission getSubmission() {
        return this.submission;
    }

    /**
     * This method assigns a submission object to this bean.
     *
     * @param submission
     *            an instance of the <code>Submission</code> class to assign.
     */
    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    /**
     * This method retrieves review object that could have been assigned to this bean earlier.
     *
     * @return an instance of the <code>Review</code> class, or <code>null</code> if no review
     *         has been assigned to this bean.
     */
    public Review getReview() {
        return this.review;
    }

    /**
     * This method assigns a review object to this bean.
     *
     * @param review
     *            an instance of the <code>Review</code> class to assign.
     */
    public void setReview(Review review) {
        this.review = review;
    }

    /**
     * This method retrieves scorecard template object that could have been assigned to this bean
     * earlier.
     *
     * @return an instance of the <code>Scorecard</code> class, or <code>null</code> if no
     *         scorecard template has been assigned to this bean.
     */
    public Scorecard getScorecard() {
        return this.scorecardTemplate;
    }

    /**
     * This method assigns a scorecard template object to this bean.
     *
     * @param review
     *            an instance of the <code>Scorecard</code> class to assign.
     */
    public void setScorecard(Scorecard scorecardTemplate) {
        this.scorecardTemplate = scorecardTemplate;
    }


    // ------------------------------------------------------------------ Verification method -----

    /**
     * This method gets a value indicating whether the check performed by one of the checkForXXX
     * methods was successfull. If this method returns <code>false</code>, action forward should
     * be retrieved from this bean and used to forward the request. Usually this action forward
     * will contain a forward to the error page, but actual contents of the forward is determined
     * by the checker method and forward-mappings configuration in Struts configuration file.
     *
     * @return <code>true</code> if the check was successfull, <code>false</code> if it wasn't.
     */
    public boolean isSuccessful() {
        return (getForward() == null);
    }
}
