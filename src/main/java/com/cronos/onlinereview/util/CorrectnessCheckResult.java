/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import com.topcoder.onlinereview.component.deliverable.Submission;
import com.topcoder.onlinereview.component.deliverable.Upload;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.review.Review;


/**
 * This class is bean that provides a convenient way to return several values from some
 * <code>checkForXXX</code> methods and indicate success/failure of the check performed by those
 * methods.
 * <p>
 * This class is not thread safe.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public final class CorrectnessCheckResult {
    /**
     * <p>This filed contains the result string of check result. It will be used by the struts to
     * forward to the expected page.</p>
     * <p>It has getter and setter. The default value for this variable is <code>null</code>.</p>
     */
    private String result;

    /**
     * This member variable contains a reference to a project assigned to this bean, or
     * <code>null</code> if no project has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * appropriate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private Project project = null;

    /**
     * This member variable contains a reference to an upload assigned to this bean, or
     * <code>null</code> if no upload has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * appropriate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private Upload upload = null;

    /**
     * This member variable contains a reference to a submission assigned to this bean, or
     * <code>null</code> if no submission has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * appropriate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private Submission submission = null;

    /**
     * This member variable contains a reference to a review assigned to this bean, or
     * <code>null</code> if no review has been assigned.
     * <p>
     * This member variable is initialized in the constructor and can be accessed or changed via
     * appropriate get/set methods. The default value for this variable is <code>null</code>.
     * </p>
     */
    private Review review = null;

    /**
     * Creates a new instance of the <code>CorrectnessCheckResult</code> class and sets all its
     * member variables to their respective default values.
     */
    public CorrectnessCheckResult() {
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
     * This method retrieves upload object that could have been assigned to this bean earlier.
     *
     * @return an instance of the <code>Upload</code> class, or <code>null</code> if no upload
     *         has been assigned to this bean.
     */
    public Upload getUpload() {
        return this.upload;
    }

    /**
     * This method assigns an upload object to this bean.
     *
     * @param upload
     *            an instance of the <code>Upload</code> class to assign.
     */
    public void setUpload(Upload upload) {
        this.upload = upload;
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
     * This method gets a value indicating whether the check performed by one of the
     * <code>checkForXXX</code> methods was successful. If this method returns <code>false</code>,
     * action forward should be retrieved from this bean and used to forward the request. Usually
     * this action forward will contain a forward to the error page, but actual contents of the
     * forward is determined by the checker method and forward-mappings configuration in Struts
     * configuration file.
     *
     * @return <code>true</code> if the check was successful, <code>false</code> if it wasn't.
     */
    public boolean isSuccessful() {
        return (getResult() == null);
    }

    /**
     * Getter of result.
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * Setter of result.
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }
}
