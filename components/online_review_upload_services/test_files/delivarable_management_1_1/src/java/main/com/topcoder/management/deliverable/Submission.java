/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

/**
 * <p> The Submission class is the one of the main modeling classes of this component. It represents a submission, which
 * consists of an upload and a status. The Submission class is simply a container for a few basic data fields. All data
 * fields in this class are mutable and have get and set methods. </p>
 *
 * <p>Changes in version 1.1: Added SubmissionType field with getter and setter.</p>
 *
 * <p><strong>Thread Safety:</strong>  This class is highly mutable. All fields can be changed.</p>
 *
 * @author aubergineanode, saarixx, singlewood, TCSDEVELOPER
 * @version 1.1
 */
public class Submission extends AuditedDeliverableStructure {

    /**
     * <p> The upload that is associated with the submission. This field can be null or non-null and is mutable. The
     * default value is null, which indicates that this field has not been set. This field can be set through the
     * setUpload method and retrieved through the getUpload method. </p>
     */
    private Upload upload;

    /**
     * <p> The status of the submission. This field can be null or non-null and is mutable. The default value is null,
     * which indicates that this field has not been set. </p>
     */
    private SubmissionStatus submissionStatus;

    /**
     * <p> The type of the submission. This field can be null or non-null and is mutable. The default value is null,
     * which indicates that this field has not been set. This field can be set through the setSubmissionType method and
     * retrieved through the getSubmissionType method. </p>
     *
     * <p>Added in version 1.1.</p>
     */
    private SubmissionType submissionType;

    /**
     * <p>The screening score for the submission. Can be any value. Has getter and setter.</p>
     */
    private Double screeningScore;

    /**
     * <p>The initial score for the submission. Can be any value. Has getter and setter.</p>
     */
    private Double initialScore;

    /**
     * <p>The final score for the submission. Can be any value. Has getter and setter.</p>
     */
    private Double finalScore;

    /**
     * <p>The placement for this submission. Can be any value. Has getter and setter.</p>
     */
    private Long placement;

    /**
     * <p>Creates instance of <strong>Submission</strong> class.</p>
     */
    public Submission() {
        super();
    }

    /**
     * <p>Creates instance of <strong>Submission</strong> class with given id.</p>
     *
     * @param id The id of the submission
     *
     * @throws IllegalArgumentException if id is <= 0
     */
    public Submission(long id) {
        super(id);
    }

    /**
     * <p>Sets the upload associated with the Submission. The parameter may be null.</p>
     *
     * @param upload The upload associated with the Submission.
     */
    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    /**
     * <p>Gets the upload associated with the Submission. May return null.</p>
     *
     * @return The upload associated with the Submission.
     */
    public Upload getUpload() {
        return upload;
    }

    /**
     * <p>Sets the submission status of the Submission. The parameter may be null.</p>
     *
     * @param submissionStatus The status of the submission.
     */
    public void setSubmissionStatus(SubmissionStatus submissionStatus) {
        this.submissionStatus = submissionStatus;
    }

    /**
     * <p>Gets the submission status of the Submission. May return null.</p>
     *
     * @return The status of the submission.
     */
    public SubmissionStatus getSubmissionStatus() {
        return submissionStatus;
    }

    /**
     * <p>Sets the submission type of the Submission.</p>
     *
     * @param submissionType The type of the submission, can be any value.
     *
     * @since 1.1
     */
    public void setSubmissionType(SubmissionType submissionType) {
        this.submissionType = submissionType;
    }

    /**
     * <p>Gets the submission type of the Submission.</p>
     *
     * @return The type of the submission, can be null.
     *
     * @since 1.1
     */
    public SubmissionType getSubmissionType() {
        return submissionType;
    }

    /**
     * <p>Retrieves the final score for the submission.</p>
     *
     * @return the final score for the submission
     */
    public Double getFinalScore() {
        return finalScore;
    }

    /**
     * <p>Sets the final score for the submission.</p>
     *
     * @param finalScore the final score for the submission
     */
    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }

    /**
     * <p>Retrieves the initial score for the submission.</p>
     *
     * @param initialScore the initial score for the submission
     */
    public void setInitialScore(Double initialScore) {
        this.initialScore = initialScore;
    }

    /**
     * <p>Sets the initial score for the submission.</p>
     *
     * @return the initial score for the submission
     */
    public Double getInitialScore() {
        return initialScore;
    }

    /**
     * <p>Retrieves the placement for the submission.</p>
     *
     * @return the placement for the submission
     */
    public Long getPlacement() {
        return placement;
    }

    /**
     * <p>Sets the placement for the submission.</p>
     *
     * @param placement the placement for the submission
     */
    public void setPlacement(Long placement) {
        this.placement = placement;
    }

    /**
     * <p>Retrieves the screening score for the submission.</p>
     *
     * @return the screening score for the submission
     */
    public Double getScreeningScore() {
        return screeningScore;
    }

    /**
     * <p>Sets the screening score for the submission.</p>
     *
     * @param screeningScore the screening score for the submission
     */
    public void setScreeningScore(Double screeningScore) {
        this.screeningScore = screeningScore;
    }

    /**
     * <p> Tells whether all the required fields of this Submission have values set. This method returns true if all of
     * the following are true:
     * <ul>
     * <li>id is not UNSET_ID</li>
     * <li>upload is not null and isValidToPersist</li>
     * <li>submissionStatus is not null and isValidToPersist</li>
     * <li>submissionType is not null and isValidToPersist</li>
     * <li>super.isValidToPersist is true</li>
     * </ul>
     * </p>
     *
     * <p>Changes in 1.1: Added check "submissionType is not null and isValidToPersist.</p>
     *
     * @return True if all fields required for persistence are present
     */
    public boolean isValidToPersist() {
        // This method returns true if all of the following are true: id is not UNSET_ID, upload is
        // not null, submissionStatus is not null and isValidToPersist,
        // submissionType is not null and isValidToPersist, super.isValidTopPersist is true.
        return ((super.getId() != UNSET_ID) && (upload != null) && (upload.isValidToPersist())
                && (submissionStatus != null) && (submissionStatus.isValidToPersist())
                && (submissionType != null) && (submissionType.isValidToPersist())
                && super.isValidToPersist());
    }
}
