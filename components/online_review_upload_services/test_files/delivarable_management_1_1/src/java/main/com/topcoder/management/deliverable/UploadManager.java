/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p> The UploadManager interface provides the ability to persist, retrieve and search for persisted upload and
 * submission modeling objects. This interface provides a higher level of interaction than the UploadPersistence
 * interface. The methods in this interface break down into dealing with the 5 Upload/Submission modeling classes in
 * this component, and the methods for each modeling class are fairly similar. However, searching methods are provided
 * only for the Upload and Submission objects. </p>
 *
 * <p>Changes in 1.1: Added new methods:
 * <ul>
 * <li>createSubmissionType</li>
 * <li>updateSubmissionType</li>
 * <li>removeSubmissionType</li>
 * <li>getAllSubmissionTypes</li>
 * </ul>
 * </p>
 *
 * <p><strong>Thread Safety:</strong> Implementations of this interface are not required to be thread safe.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public interface UploadManager {

    /**
     * <p>Creates a new upload in the persistence store. The id of the upload must be UNSET_ID. The manager will assign
     * an id to the upload.</p>
     *
     * @param upload   The upload to add
     * @param operator The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If upload or operator is null
     * @throws IllegalArgumentException   If the id is not UNSET_ID
     * @throws IllegalArgumentException   If the upload (once an id and creation/modification user/date are assigned) is
     *                                    not in a state to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error persisting the upload
     */
     void createUpload(Upload upload, String operator) throws UploadPersistenceException;

    /**
     * <p>Updates the upload in the persistence store. The id of the upload can not be UNSET_ID and all necessary fields
     * must have already been assigned.</p>
     *
     * @param upload   The upload to update
     * @param operator The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If upload or operator is null
     * @throws IllegalArgumentException   If the upload (once the modification user/date is set) is not in a state to be
     *                                    persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error persisting the upload changes
     */
     void updateUpload(Upload upload, String operator) throws UploadPersistenceException;

    /**
     * <p>Removes the upload (by id) from the persistence store. The id of the upload can not be UNSET_ID.</p>
     *
     * @param upload   The upload to remove
     * @param operator The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If upload or operator is null
     * @throws IllegalArgumentException   If the id of the upload is UNSET_ID
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void removeUpload(Upload upload, String operator) throws UploadPersistenceException;

    /**
     * <p>Gets the upload in the persistence store for the given id. Returns null if there is no upload for the given
     * id.</p>
     *
     * @param id The id of the upload to retrieve
     *
     * @return The retrieved Upload, or null
     *
     * @throws IllegalArgumentException   If id is <= 0
     * @throws UploadPersistenceException If there is an error retrieving the Upload from persistence
     */
     Upload getUpload(long id) throws UploadPersistenceException;

    /**
     * <p>Searches the persistence for uploads meeting the filter. The filter can be easily built using the
     * UploadFilterBuilder.</p>
     *
     * @param filter The filter to use
     *
     * @return The Uploads meeting the filter
     *
     * @throws IllegalArgumentException   If filter is null
     * @throws UploadPersistenceException If there is an error reading the upload from persistence
     * @throws SearchBuilderException     If there is an error executing the filter
     * @throws SearchBuilderConfigurationException
     *                                    If the SearchBundle used by the manager is not configured for proper
     *                                    searching.
     */
     Upload[] searchUploads(Filter filter) throws UploadPersistenceException, SearchBuilderException;

    /**
     * <p>Creates a new UploadType in the persistence store. The id of the upload type must be UNSET_ID. The manager
     * will assign an id before persisting the change.</p>
     *
     * @param uploadType The upload type to add to the persistence store
     * @param operator   The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If uploadType or operator is null
     * @throws IllegalArgumentException   If the id is not UNSET_ID
     * @throws IllegalArgumentException   If the upload type (once an id and creation/modification user/date are
     *                                    assigned) is not in a state to be persisted (i.e. if isValidToPersist returns
     *                                    false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void createUploadType(UploadType uploadType, String operator) throws UploadPersistenceException;

    /**
     * <p>Updates the info for an UploadType in the persistence store. All fields of the upload type must have values
     * assigned when this method is called.</p>
     *
     * @param uploadType The upload type to update
     * @param operator   The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If uploadType or operator is null
     * @throws IllegalArgumentException   If the upload type (once the modification user/date is set) is not in a state
     *                                    to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void updateUploadType(UploadType uploadType, String operator) throws UploadPersistenceException;

    /**
     * <p>Removes the given upload type (by id) from the persistence. The id of the uploadType can not be UNSET_ID.</p>
     *
     * @param uploadType The upload type to remove
     * @param operator   The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If uploadType or operator is null
     * @throws IllegalArgumentException   If the id of the upload type is UNSET_ID
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void removeUploadType(UploadType uploadType, String operator) throws UploadPersistenceException;

    /**
     * <p>Gets all the upload types in the persistence store.</p>
     *
     * @return All UploadTypes in the persistence store.
     *
     * @throws UploadPersistenceException If there is an error accessing the persistence store
     */
     UploadType[] getAllUploadTypes() throws UploadPersistenceException;

    /**
     * <p>Creates a new UploadStatus in the persistence store. The id of the upload status must be UNSET_ID. The manager
     * will assign an id before persisting the change.</p>
     *
     * @param uploadStatus The upload status to add to the persistence store
     * @param operator     The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If uploadStatus or operator is null
     * @throws IllegalArgumentException   If the id is not UNSET_ID
     * @throws IllegalArgumentException   If the upload status (once an id and creation/modification user/date are
     *                                    assigned) is not in a state to be persisted (i.e. if isValidToPersist returns
     *                                    false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void createUploadStatus(UploadStatus uploadStatus, String operator)
            throws UploadPersistenceException;

    /**
     * <p>Updates the info for an UploadStatus in the persistence store. All fields of the upload status must have
     * values assigned when this method is called.</p>
     *
     * @param uploadStatus The upload status to update
     * @param operator     The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If uploadStatus or operator is null
     * @throws IllegalArgumentException   If the upload status (once the modification user/date is set) is not in a
     *                                    state to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void updateUploadStatus(UploadStatus uploadStatus, String operator)
            throws UploadPersistenceException;

    /**
     * <p>Removes the given upload status (by id) from the persistence. The id of the uploadStatus can not be
     * UNSET_ID.</p>
     *
     * @param uploadStatus The upload status to remove
     * @param operator     The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If uploadStatus or operator is null
     * @throws IllegalArgumentException   If the id of the upload status is UNSET_ID
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void removeUploadStatus(UploadStatus uploadStatus, String operator)
            throws UploadPersistenceException;

    /**
     * <p>Gets all the upload statuses in the persistence store.</p>
     *
     * @return All UploadStatuses in the persistence store.
     *
     * @throws UploadPersistenceException If there is an error accessing the persistence store
     */
     UploadStatus[] getAllUploadStatuses() throws UploadPersistenceException;

    /**
     * <p>Creates a new Submission in the persistence store. The id of the submission must be UNSET_ID. The manager will
     * assign an id before persisting the change.</p>
     *
     * @param submission The submission to add to the persistence store
     * @param operator   The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submission or operator is null
     * @throws IllegalArgumentException   If the id is not UNSET_ID
     * @throws IllegalArgumentException   If the submission (once an id and creation/modification user/date are
     *                                    assigned) is not in a state to be persisted (i.e. if isValidToPersist returns
     *                                    false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void createSubmission(Submission submission, String operator) throws UploadPersistenceException;

    /**
     * <p>Updates the info for a Submission in the persistence store. All fields of the submission must have values
     * assigned when this method is called.</p>
     *
     * @param submission The submission to update
     * @param operator   The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submission or operator is null
     * @throws IllegalArgumentException   If the submission (once the modification user/date is set) is not in a state
     *                                    to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void updateSubmission(Submission submission, String operator) throws UploadPersistenceException;

    /**
     * <p>Removes the given submission (by id) from the persistence. The id of the submission can not be UNSET_ID.</p>
     *
     * @param submission The submission to remove
     * @param operator   The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submission or operator is null
     * @throws IllegalArgumentException   If the id of the submission is UNSET_ID
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void removeSubmission(Submission submission, String operator) throws UploadPersistenceException;

    /**
     * <p>Gets the Submission in the persistence store for the given id. Returns null if there is no submission for the
     * given id.</p>
     *
     * @param id The id of the submission to retrieve
     *
     * @return The retrieved Submission, or null
     *
     * @throws IllegalArgumentException   If id is <= 0
     * @throws UploadPersistenceException If there is an error retrieving the Submission from persistence
     */
     Submission getSubmission(long id) throws UploadPersistenceException;

    /**
     * <p>Searches the persistence for submissions meeting the filter. The filter can be easily built using the
     * SubmissionFilterBuilder.</p>
     *
     * @param filter The filter to use
     *
     * @return The submissions meeting the filter
     *
     * @throws IllegalArgumentException   If filter is null
     * @throws UploadPersistenceException If there is an error reading the submissions from persistence
     * @throws SearchBuilderException     If there is an error executing the filter
     * @throws SearchBuilderConfigurationException
     *                                    If the SearchBundle used by the manager is not configured for proper
     *                                    searching.
     */
     Submission[] searchSubmissions(Filter filter) throws UploadPersistenceException,
            SearchBuilderException;

    /**
     * <p>Creates a new SubmissionStatus in the persistence store. The id of the submission status must be UNSET_ID. The
     * manager will assign an id before persisting the change.</p>
     *
     * @param submissionStatus The submission status to add to the persistence store
     * @param operator         The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submissionStatus or operator is null
     * @throws IllegalArgumentException   If the id is not UNSET_ID
     * @throws IllegalArgumentException   If the submission status (once an id and creation/modification user/date are
     *                                    assigned) is not in a state to be persisted (i.e. if isValidToPersist returns
     *                                    false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void createSubmissionStatus(SubmissionStatus submissionStatus, String operator)
            throws UploadPersistenceException;

    /**
     * <p>Updates the info for submission status in the persistence store. All fields of the submission status must have
     * values assigned when this method is called.</p>
     *
     * @param submissionStatus The submission status to update
     * @param operator         The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submissionStatus or operator is null
     * @throws IllegalArgumentException   If the submission status (once the modification user/date is set) is not in a
     *                                    state to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void updateSubmissionStatus(SubmissionStatus submissionStatus, String operator)
            throws UploadPersistenceException;

    /**
     * <p>Removes the given submission status (by id) from the persistence. The id of the submissionStatus can not be
     * UNSET_ID.</p>
     *
     * @param submissionStatus The submission status to remove
     * @param operator         The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submissionStatus or operator is null
     * @throws IllegalArgumentException   If the id of the submission status is UNSET_ID
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
     void removeSubmissionStatus(SubmissionStatus submissionStatus, String operator)
            throws UploadPersistenceException;

    /**
     * <p>Gets all the submission statuses in the persistence store.</p>
     *
     * @return All SubmissionStatuses in the persistence store.
     *
     * @throws UploadPersistenceException If there is an error accessing the persistence store
     */
     SubmissionStatus[] getAllSubmissionStatuses() throws UploadPersistenceException;

    /**
     * <p>Creates a new SubmissionType in the persistence store. The id of the submission type must be UNSET_ID. The
     * manager will assign an id before persisting the change.</p>
     *
     * <p>Added in version 1.1.</p>
     *
     * @param submissionType The submission type to add to the persistence store
     * @param operator       The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submissionType or operator is null
     * @throws IllegalArgumentException   If the id is not UNSET_ID
     * @throws IllegalArgumentException   If the submission type (once an id and creation/modification user/date are
     *                                    assigned) is not in a state to be persisted (i.e. if isValidToPersist returns
     *                                    false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    void createSubmissionType(SubmissionType submissionType, String operator) throws UploadPersistenceException;

    /**
     * <p>Updates the info for submission type in the persistence store. All fields of the submission type must have
     * values assigned when this method is called.</p>
     *
     * <p>Added in version 1.1.</p>
     *
     * @param submissionType The submission type to update
     * @param operator       The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submissionType or operator is null
     * @throws IllegalArgumentException   The id is UNSET_ID
     * @throws IllegalArgumentException   If the submission type (once the modification user/date is set) is not in a
     *                                    state to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     * @since 1.1
     */
    void updateSubmissionType(SubmissionType submissionType, String operator) throws UploadPersistenceException;

    /**
     * <p>Removes the given submission type (by id) from the persistence. The id of the submissionType can not be
     * UNSET_ID.</p>
     *
     * <p>Added in version 1.1.</p>
     *
     * @param submissionType The submission type to remove
     * @param operator       The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submissionType or operator is null
     * @throws IllegalArgumentException   If the id of the submission type is UNSET_ID
     * @throws IllegalArgumentException   If the submissionType (once the modification user/date is set) is not in a
     *                                    state to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     * @since 1.1
     */
    void removeSubmissionType(SubmissionType submissionType, String operator) throws UploadPersistenceException;

    /**
     * <p>Gets all the submission types in the persistence store.</p>
     *
     * <p>Added in version 1.1.</p>
     *
     * @return All SubmissionTypes in the persistence store.
     *
     * @throws UploadPersistenceException If there is an error accessing the persistence store
     * @since 1.1
     */
    SubmissionType[] getAllSubmissionTypes() throws UploadPersistenceException;
}
