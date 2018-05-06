/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

/**
 * <p> The Upload class is the one of the main modeling classes of this component. It represents an uploaded document.
 * The Upload class is simply a container for a few basic data fields. All data fields in this class are mutable and
 * have get and set methods. </p>
 *
 * <p><strong>Thread Safety:</strong> This class is highly mutable. All fields can be changed.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public class Upload extends AuditedDeliverableStructure {

    /**
     * <p> The value that the owner field will have (and that the getOwner method will return) when the owner field has
     * not been through the setOwner method. </p>
     */
    public static final long UNSET_OWNER = -1;

    /**
     * <p> The value that the project field will have (and that the getProject method will return) when the project
     * field has not been through the setProject method. </p>
     */
    public static final long UNSET_PROJECT = -1;

    /**
     * <p> The type of the upload. This field can be null or non-null and is mutable. The default value is null, which
     * indicates that this field has not been set. This field can be set through the setUploadType method and retrieved
     * through the getUploadType method. </p>
     */
    private UploadType uploadType = null;

    /**
     * <p> The status of the upload. This field can be null or non-null and is mutable. The default value is null, which
     * indicates that this field has not been set. This field can be set through the setUploadStatus method and
     * retrieved through the getUploadStatus method. </p>
     */
    private UploadStatus uploadStatus = null;

    /**
     * <p> The owner of the upload, i.e. the person responsible for the upload. This field can be greater than 0 or
     * UNSET_OWNER and is mutable. The default value is UNSET_OWNER, which indicates that this field has not been set or
     * that no owner is associated with the upload. This field can be set through the setOwner method and retrieved
     * through the getOwner method. </p>
     */
    private long owner = UNSET_OWNER;

    /**
     * <p> The project that the upload is associated with. This field can be greater than 0 or UNSET_PROJECT and is
     * mutable. The default value is UNSET_PROJECT, which indicates that this field has not been set. This field can be
     * set through the setProject method and retrieved through the getProject method. </p>
     */
    private long project = UNSET_PROJECT;

    /**
     * <p> The parameter that identifies the uploaded file (and implicitly contains information about how to get the
     * uploaded file). This field can be null or non-null and is mutable. The default value is null, which indicates
     * that this field has not been set. This field can be set through the setParameter method and retrieved through the
     * getParameter method. </p>
     */
    private String parameter = null;

    /**
     * <p>Creates a new Upload.</p>
     */
    public Upload() {
        super();
    }

    /**
     * <p>Creates a new Upload.</p>
     *
     * @param id The id of the Upload
     *
     * @throws IllegalArgumentException If id is <= 0
     */
    public Upload(long id) {
        super(id);
    }

    /**
     * <p>Sets the upload type of the upload. The value can be either null or non-null.</p>
     *
     * @param uploadType The type of the upload.
     */
    public void setUploadType(UploadType uploadType) {
        this.uploadType = uploadType;
    }

    /**
     * <p>Gets the upload type of the upload. The return value may be null or non-null.</p>
     *
     * @return The type of the upload
     */
    public UploadType getUploadType() {
        return uploadType;
    }

    /**
     * <p>Sets the status of the upload. The value can be null or non-null.</p>
     *
     * @param uploadStatus The status of the upload.
     */
    public void setUploadStatus(UploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    /**
     * <p>Gets the status of the upload. The return value may be null or non-null.</p>
     *
     * @return The status of the upload.
     */
    public UploadStatus getUploadStatus() {
        return uploadStatus;
    }

    /**
     * <p>Sets the owner of the upload. This method does not allow the owner (once set) to be unset, but it does allow
     * the owner to be changed.</p>
     *
     * @param owner The owner of the upload
     *
     * @throws IllegalArgumentException If owner is <= 0
     */
    public void setOwner(long owner) {
        DeliverableHelper.checkGreaterThanZero(owner, "owner");
        this.owner = owner;
    }

    /**
     * <p>Gets the owner of the upload. This method will return UNSET_OWNER or a value greater than 0.</p>
     *
     * @return The owner of the upload.
     */
    public long getOwner() {
        return owner;
    }

    /**
     * <p>Sets the project the upload is associated with. This method does not allow the project (once set) to be unset,
     * but it does allow the project to be changed.</p>
     *
     * @param project The project the upload is associated with
     *
     * @throws IllegalArgumentException If project is <= 0
     */
    public void setProject(long project) {
        DeliverableHelper.checkGreaterThanZero(project, "project");
        this.project = project;
    }

    /**
     * <p>Gets the project that the upload is associated with. This method will return UNSET_PROJECT or a value greater
     * than 0.</p>
     *
     * @return The project the upload is associated with.
     */
    public long getProject() {
        return project;
    }

    /**
     * <p>Sets the parameter that identifies the uploaded file (tells where to find it). The value may be null or
     * non-null, and when non-null, any value is allowed.</p>
     *
     * @param parameter The identifier for the uploaded file.
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * <p>Gets the parameter that identifies the uploaded file (tells where it is). The return value may be null or
     * non-null.</p>
     *
     * @return The identifier for the uploaded file
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * <p>Tells whether all the required fields of this Submission have values set.</p>
     *
     * @return True if all fields required for persistence are present
     */
    public boolean isValidToPersist() {
        // This method returns true if all of the following are true: id is not UNSET_ID, uploadType
        // is not null, uploadStatus is not null, owner is not UNSET_OWNER, project is not
        // UNSET_PROJECT, parameter is not null, base.isValidToPersist
        return ((super.getId() != UNSET_ID)
                && (uploadType != null)
                && (uploadType.isValidToPersist())
                && (uploadStatus != null)
                && (uploadStatus.isValidToPersist())
                && (owner != UNSET_OWNER)
                && (project != UNSET_PROJECT)
                && (parameter != null)
                && (super.isValidToPersist()));
    }
}
