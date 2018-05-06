/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import com.topcoder.management.deliverable.logging.LogMessage;
import com.topcoder.management.deliverable.persistence.UploadPersistence;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

import java.util.Date;

/**
 * <p> The PersistenceUploadManager class implements the UploadManager interface. It ties together a persistence
 * mechanism, several Search Builder instances (for searching for various types of data), and several id generators (for
 * generating ids for the various types). This class consists of several methods styles. The first method style just
 * calls directly to a corresponding method of the persistence. The second method style first assigns values to some
 * data fields of the object before calling a persistence method. The third type of method uses a SearchBundle to
 * execute a search and then uses the persistence to load an object for each of the ids found from the search. </p>
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
 * <p><strong>Thread Safety:</strong> This class is immutable and hence thread-safe. </p>
 *
 * @author aubergineanode, saarixx, singlewood, George1, TCSDEVELOPER
 * @version 1.1
 */
public class PersistenceUploadManager implements UploadManager {

    /**
     * <p> The name under which the upload search bundle should appear in the SearchBundleManager, if the
     * SearchBundleManager constructor is used. </p>
     */
    public static final String UPLOAD_SEARCH_BUNDLE_NAME = "Upload Search Bundle";

    /**
     * <p> The name under which the submission search bundle should appear in the SearchBundleManager, if the
     * SearchBundleManager constructor is used. </p>
     */
    public static final String SUBMISSION_SEARCH_BUNDLE_NAME = "Submission Search Bundle";

    /**
     * <p> The name under which the id generator for uploads should appear in the IDGeneratorFactory if the
     * IDGeneratorFactory constructor is used. </p>
     */
    public static final String UPLOAD_ID_GENERATOR_NAME = "upload_id_seq";

    /**
     * <p> The name under which the id generator for upload types should appear in the IDGeneratorFactory if the
     * IDGeneratorFactory constructor is used. This field is public, static, and final. </p>
     */
    public static final String UPLOAD_TYPE_ID_GENERATOR_NAME = "upload_type_id_seq";

    /**
     * <p> The name under which the id generator for upload statuses should appear in the IDGeneratorFactory if the
     * IDGeneratorFactory constructor is used. This field is public, static, and final. </p>
     */
    public static final String UPLOAD_STATUS_ID_GENERATOR_NAME = "upload_status_id_seq";

    /**
     * <p> The name under which the id generator for submissions should appear in the IDGeneratorFactory if the
     * IDGeneratorFactory constructor is used. </p>
     */
    public static final String SUBMISSION_ID_GENERATOR_NAME = "submission_id_seq";

    /**
     * <p> The name under which the id generator for submission statuses should appear in the IDGeneratorFactory if the
     * IDGeneratorFactory constructor is used. </p>
     */
    public static final String SUBMISSION_STATUS_ID_GENERATOR_NAME = "submission_status_id_seq";

    /**
     * <p> The name under which the id generator for submission types should appear in the IDGeneratorFactory if the
     * IDGeneratorFactory constructor is used. </p>
     */
    public static final String SUBMISSION_TYPE_ID_GENERATOR_NAME = "submission_type_id_seq";

    /**
     * <p> Logger instance using the class name as category. </p>
     *
     * <p>Changes in 1.1: Renamed from Logger.</p>
     */    
    private static final Log LOGGER = LogFactory.getLog(PersistenceUploadManager.class.getName());

    /**
     * <p> The flag which indicate the caller of persistenceUploadManagerHelper. </p>
     */
    private static final int CREATE_FLAG = 0;

    /**
     * <p> The flag which indicate the caller of persistenceUploadManagerHelper. </p>
     */
    private static final int UPDATE_FLAG = 1;

    /**
     * <p> The flag which indicate the caller of persistenceUploadManagerHelper. </p>
     */
    private static final int REMOVE_FLAG = 2;

    /**
     * <p> The persistence store for Uploads, Submission, and related objects. This field is set in the constructor, is
     * immutable, and can never be null. </p>
     */
    private final UploadPersistence persistence;

    /**
     * <p> The search bundle that is used when searching for uploads. This field is set in the constructor, is
     * immutable, and can never be null. </p>
     */
    private final SearchBundle uploadSearchBundle;

    /**
     * <p> The search bundle that is used when searching for submissions. This field is set in the constructor, is
     * immutable, and can never be null. </p>
     */
    private final SearchBundle submissionSearchBundle;

    /**
     * <p> The generator used to create ids for new Uploads. This field is set in the constructor, is immutable, and can
     * never be null. </p>
     */
    private final IDGenerator uploadIdGenerator;

    /**
     * <p> The generator used to create ids for new Submissions. This field is set in the constructor, is immutable, and
     * can never be null. </p>
     */
    private final IDGenerator submissionIdGenerator;

    /**
     * <p> The generator used to create ids for new UploadTypes. This field is set in the constructor, is immutable, and
     * can never be null. </p>
     */
    private final IDGenerator uploadTypeIdGenerator;

    /**
     * <p> The generator used to create ids for new UploadStatusess. This field is set in the constructor, is immutable,
     * and can never be null. </p>
     */
    private final IDGenerator uploadStatusIdGenerator;

    /**
     * <p> The generator used to create ids for new SubmissionStatuses. This field is set in the constructor, is
     * immutable, and can never be null. </p>
     */
    private final IDGenerator submissionStatusIdGenerator;

    /**
     * <p> The generator used to create ids for new SubmissionTypes. This field is set in the constructor, is immutable,
     * and can never be null. </p>
     */
    private final IDGenerator submissionTypeIdGenerator;

    /**
     * <p>Creates a new PersistenceUploadManager.</p>
     *
     * <p>Changes in 1.1: Added submissionTypeIdGenerator parameter.</p>
     *
     * @param persistence                 The persistence for Uploads, Submissions, and related objects
     * @param uploadSearchBundle          The search bundle for searching uploads
     * @param submissionSearchBundle      The search bundle for searching submissions
     * @param uploadIdGenerator           The generator for Upload ids
     * @param uploadTypeIdGenerator       The generator for UploadType ids
     * @param uploadStatusIdGenerator     The generator for UploadStatus ids
     * @param submissionIDGenerator       The generator for Submission ids
     * @param submissionStatusIdGenerator The generator for SubmissionStatus ids
     * @param submissionTypeIdGenerator   The generator for SubmissionType ids
     *
     * @throws IllegalArgumentException If any argument is null
     */
    public PersistenceUploadManager(UploadPersistence persistence, SearchBundle uploadSearchBundle,
                                    SearchBundle submissionSearchBundle, IDGenerator uploadIdGenerator,
                                    IDGenerator uploadTypeIdGenerator, IDGenerator uploadStatusIdGenerator,
                                    IDGenerator submissionIDGenerator, IDGenerator submissionStatusIdGenerator,
                                    IDGenerator submissionTypeIdGenerator) {
        // Check parameters.
        DeliverableHelper.checkObjectNotNull(persistence, "persistence");
        DeliverableHelper.checkObjectNotNull(uploadIdGenerator, "uploadIdGenerator");
        DeliverableHelper.checkObjectNotNull(uploadTypeIdGenerator, "uploadTypeIdGenerator");
        DeliverableHelper.checkObjectNotNull(uploadStatusIdGenerator, "uploadStatusIdGenerator");
        DeliverableHelper.checkObjectNotNull(submissionIDGenerator, "submissionIDGenerator");
        DeliverableHelper.checkObjectNotNull(submissionStatusIdGenerator, "submissionStatusIdGenerator");
        DeliverableHelper.checkObjectNotNull(submissionTypeIdGenerator, "submissionTypeIdGenerator");
        DeliverableHelper.checkObjectNotNullFullDesp(uploadSearchBundle,
                "uploadSearchBundle is null, or searchBundleManager doesn't contain"
                        + " SearchBundle of required names UPLOAD_SEARCH_BUNDLE_NAME");
        DeliverableHelper.checkObjectNotNullFullDesp(submissionSearchBundle,
                "submissionSearchBundle is null, or searchBundleManager doesn't contain"
                        + " SearchBundle of required names SUBMISSION_SEARCH_BUNDLE_NAME");

        // Set the searchable fields of search bundles.
        DeliverableHelper.setSearchableFields(uploadSearchBundle, DeliverableHelper.UPLOAD_SEARCH_BUNDLE);
        DeliverableHelper.setSearchableFields(submissionSearchBundle, DeliverableHelper.SUBMISSION_SEARCH_BUNDLE);

        LOGGER.log(Level.INFO, "Instantiate PersistenceUploadManager with UploadPersistence, uploadSearchBundle,"
                + " submissionSearchBundle, uploadIdGenerator, uploadTypeIdGenerator, uploadStatusIdGenerator,"
                + " submissionIDGenerator and submissionStatusIdGenerator.");

        // Initializing all fields to the given values.
        this.persistence = persistence;
        this.uploadSearchBundle = uploadSearchBundle;
        this.submissionSearchBundle = submissionSearchBundle;
        this.uploadIdGenerator = uploadIdGenerator;
        this.uploadTypeIdGenerator = uploadTypeIdGenerator;
        this.uploadStatusIdGenerator = uploadStatusIdGenerator;
        this.submissionIdGenerator = submissionIDGenerator;
        this.submissionStatusIdGenerator = submissionStatusIdGenerator;
        this.submissionTypeIdGenerator = submissionTypeIdGenerator;
    }

    /**
     * <p>Creates a new PersistenceUploadManager.</p>
     *
     * <p>Changes in 1.1: Added initialization of submissionTypeIdGenerator.</p>
     *
     * @param persistence         The persistence for Uploads and related objects
     * @param searchBundleManager The manager containing the various SearchBundles needed
     *
     * @throws IllegalArgumentException If any argument is null
     * @throws IllegalArgumentException If any search bundle or id generator is not available under the required names
     * @throws IDGenerationException    If any error occur when generating IDGenerators.
     */
    public PersistenceUploadManager(UploadPersistence persistence, SearchBundleManager searchBundleManager)
            throws IDGenerationException {
        // Check if searchBundleManager is null, if not, get the SearchBundles.
        // Get IDGenerator from IDGeneratorFactory. If any id generator is not available under
        // the required names, IDGenerationException will be thrown.
        // Finally, delegate to the first constructor.
        this(persistence,
                (DeliverableHelper.checkObjectNotNull(searchBundleManager, "searchBundleManager"))
                        ? searchBundleManager.getSearchBundle(UPLOAD_SEARCH_BUNDLE_NAME)
                        : null,
                searchBundleManager.getSearchBundle(SUBMISSION_SEARCH_BUNDLE_NAME),
                IDGeneratorFactory.getIDGenerator(UPLOAD_ID_GENERATOR_NAME),
                IDGeneratorFactory.getIDGenerator(UPLOAD_TYPE_ID_GENERATOR_NAME),
                IDGeneratorFactory.getIDGenerator(UPLOAD_STATUS_ID_GENERATOR_NAME),
                IDGeneratorFactory.getIDGenerator(SUBMISSION_ID_GENERATOR_NAME),
                IDGeneratorFactory.getIDGenerator(SUBMISSION_STATUS_ID_GENERATOR_NAME),
                IDGeneratorFactory.getIDGenerator(SUBMISSION_TYPE_ID_GENERATOR_NAME));
    }

    /**
     * <p> Creates a new upload in the persistence store. The id of the upload must be UNSET_ID. The manager will assign
     * an id to the upload. </p>
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
    public void createUpload(Upload upload, String operator) throws UploadPersistenceException {
        LOGGER.log(Level.INFO, new LogMessage("Upload", null, operator, "Create new Upload."));
        persistenceUploadManagerHelper(upload, "upload", operator, CREATE_FLAG, uploadIdGenerator);
    }

    /**
     * <p> Updates the upload in the persistence store. The id of the upload can not be UNSET_ID and all necessary
     * fields must have already been assigned. </p>
     *
     * @param upload   The upload to update
     * @param operator The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If upload or operator is null
     * @throws IllegalArgumentException   The id of the upload is UNSET_ID
     * @throws IllegalArgumentException   If the upload (once the modification user/date is set) is not in a state to be
     *                                    persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error persisting the upload changes
     */
    public void updateUpload(Upload upload, String operator) throws UploadPersistenceException {

        Long id = new Long(-1);

        if (upload != null) {
            id = new Long(upload.getId());
        }

        LOGGER.log(Level.INFO,
                new LogMessage("Upload", id, operator, "Update Upload."));

        persistenceUploadManagerHelper(upload, "upload", operator, UPDATE_FLAG, null);
    }

    /**
     * <p> Removes the upload (by id) from the persistence store. The id of the upload can not be UNSET_ID. </p>
     *
     * @param upload   The upload to remove
     * @param operator The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If upload or operator is null
     * @throws IllegalArgumentException   If the id of the upload is UNSET_ID
     * @throws IllegalArgumentException   If the upload (once the modification user/date is set) is not in a state to be
     *                                    persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    public void removeUpload(Upload upload, String operator) throws UploadPersistenceException {

        Long id = new Long(-1);

        if (upload != null) {
            id = new Long(upload.getId());
        }

        LOGGER.log(Level.INFO,
                new LogMessage("Upload", id, operator, "Remove Upload."));

        persistenceUploadManagerHelper(upload, "upload", operator, REMOVE_FLAG, null);
    }

    /**
     * <p> Gets the upload in the persistence store for the given id. Returns null if there is no upload for the given
     * id. </p>
     *
     * @param id The id of the upload to retrieve
     *
     * @return The retrieved Upload, or null
     *
     * @throws IllegalArgumentException   If id is <= 0
     * @throws UploadPersistenceException If there is an error retrieving the Upload from persistence
     */
    public Upload getUpload(long id) throws UploadPersistenceException {
        LOGGER.log(Level.INFO,
                new LogMessage("Upload", new Long(id), null, "Retrieve Upload, delegate to persistence."));
        // Simply call the corresponding persistence method.
        return persistence.loadUpload(id);
    }

    /**
     * <p> Searches the persistence for uploads meeting the filter. The filter can be easily built using the
     * UploadFilterBuilder. </p>
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
    public Upload[] searchUploads(Filter filter) throws UploadPersistenceException, SearchBuilderException {
        DeliverableHelper.checkObjectNotNull(filter, "filter");
        CustomResultSet resultSet = (CustomResultSet) uploadSearchBundle.search(filter);
        LOGGER.log(Level.INFO,
                new LogMessage("Upload", null, null, "search uploads with filter.") + " found: "
                        + resultSet.getRecordCount());
        // Check if the object is a CustomResultSet with a single column consisting of long ids.
        // The parameter 1 indicate that there should be a single column in the CustomResultSet.
        // The return type is long[][], what we need is the first array.
        return persistence.loadUploads(resultSet);
    }

    /**
     * <p> Creates a new UploadType in the persistence store. The id of the upload type must be UNSET_ID. The manager
     * will assign an id before persisting the change. </p>
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
    public void createUploadType(UploadType uploadType, String operator) throws UploadPersistenceException {
        LOGGER.log(Level.INFO, new LogMessage("UploadType", null, operator, "Create new UploadType."));
        persistenceUploadManagerHelper(uploadType, "uploadType", operator, CREATE_FLAG, uploadTypeIdGenerator);
    }

    /**
     * <p> Updates the info for an UploadType in the persistence store. All fields of the upload type must have values
     * assigned when this method is called. </p>
     *
     * @param uploadType The upload type to update
     * @param operator   The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If uploadType or operator is null
     * @throws IllegalArgumentException   The id is UNSET_ID
     * @throws IllegalArgumentException   If the upload type (once the modification user/date is set) is not in a state
     *                                    to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    public void updateUploadType(UploadType uploadType, String operator) throws UploadPersistenceException {

        Long id = new Long(-1);

        if (uploadType != null) {
            id = new Long(uploadType.getId());
        }

        LOGGER.log(Level.INFO,
                new LogMessage("UploadType", id, operator, "update UploadType."));

        persistenceUploadManagerHelper(uploadType, "uploadType", operator, UPDATE_FLAG, null);
    }

    /**
     * <p> Removes the given upload type (by id) from the persistence. The id of the uploadType can not be UNSET_ID.
     * </p>
     *
     * @param uploadType The upload type to remove
     * @param operator   The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If uploadType or operator is null
     * @throws IllegalArgumentException   If the id of the upload type is UNSET_ID
     * @throws IllegalArgumentException   If the uploadType (once the modification user/date is set) is not in a state
     *                                    to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    public void removeUploadType(UploadType uploadType, String operator) throws UploadPersistenceException {

        Long id = new Long(-1);

        if (uploadType != null) {
            id = new Long(uploadType.getId());
        }

        LOGGER.log(Level.INFO,
                new LogMessage("UploadType", id, operator, "remove UploadType."));

        persistenceUploadManagerHelper(uploadType, "uploadType", operator, REMOVE_FLAG, null);
    }

    /**
     * <p> Gets all the upload types in the persistence store. </p>
     *
     * @return All UploadTypes in the persistence store.
     *
     * @throws UploadPersistenceException If there is an error accessing the persistence store
     */
    public UploadType[] getAllUploadTypes() throws UploadPersistenceException {
        LOGGER.log(Level.INFO, "get All upload types, delegate to persistence.");
        return persistence.loadUploadTypes(persistence.getAllUploadTypeIds());
    }

    /**
     * <p> Creates a new UploadStatus in the persistence store. The id of the upload status must be UNSET_ID. The
     * manager will assign an id before persisting the change. </p>
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
    public void createUploadStatus(UploadStatus uploadStatus, String operator)
            throws UploadPersistenceException {
        LOGGER.log(Level.INFO,
                new LogMessage("UploadStatus", null, operator, "create new UploadStatus."));
        persistenceUploadManagerHelper(uploadStatus, "uploadStatus", operator, CREATE_FLAG,
                uploadStatusIdGenerator);
    }

    /**
     * <p> Updates the info for an UploadStatus in the persistence store. All fields of the upload status must have
     * values assigned when this method is called. </p>
     *
     * @param uploadStatus The upload status to update
     * @param operator     The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If uploadStatus or operator is null
     * @throws IllegalArgumentException   The id is UNSET_ID
     * @throws IllegalArgumentException   If the upload status (once the modification user/date is set) is not in a
     *                                    state to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    public void updateUploadStatus(UploadStatus uploadStatus, String operator)
            throws UploadPersistenceException {

        Long id = new Long(-1);

        if (uploadStatus != null) {
            id = new Long(uploadStatus.getId());
        }

        LOGGER.log(Level.INFO,
                new LogMessage("UploadStatus", id, operator, "create new UploadStatus."));

        persistenceUploadManagerHelper(uploadStatus, "uploadStatus", operator, UPDATE_FLAG, null);
    }

    /**
     * <p> Removes the given upload status (by id) from the persistence. The id of the uploadStatus can not be UNSET_ID.
     * </p>
     *
     * @param uploadStatus The upload status to remove
     * @param operator     The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If uploadStatus or operator is null
     * @throws IllegalArgumentException   If the id of the upload status is UNSET_ID
     * @throws IllegalArgumentException   If the uploadStatus (once the modification user/date is set) is not in a state
     *                                    to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    public void removeUploadStatus(UploadStatus uploadStatus, String operator)
            throws UploadPersistenceException {

        Long id = new Long(-1);

        if (uploadStatus != null) {
            id = new Long(uploadStatus.getId());
        }

        LOGGER.log(Level.INFO,
                new LogMessage("UploadStatus", id, operator, "remove UploadStatus."));

        persistenceUploadManagerHelper(uploadStatus, "uploadStatus", operator, REMOVE_FLAG, null);
    }

    /**
     * <p> Gets all the upload statuses in the persistence store. </p>
     *
     * @return All UploadStatuses in the persistence store.
     *
     * @throws UploadPersistenceException If there is an error accessing the persistence store
     */
    public UploadStatus[] getAllUploadStatuses() throws UploadPersistenceException {
        LOGGER.log(Level.INFO, "get All upload status, delegate to persistence.");
        return persistence.loadUploadStatuses(persistence.getAllUploadStatusIds());
    }

    /**
     * <p> Creates a new Submission in the persistence store. The id of the submission must be UNSET_ID. The manager
     * will assign an id before persisting the change. </p>
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
    public void createSubmission(Submission submission, String operator) throws UploadPersistenceException {
        LOGGER.log(Level.INFO,
                new LogMessage("Submission", null, operator, "create new Submission."));
        persistenceUploadManagerHelper(submission, "submission", operator, CREATE_FLAG, submissionIdGenerator);
    }

    /**
     * <p> Updates the info for a Submission in the persistence store. All fields of the submission must have values
     * assigned when this method is called. </p>
     *
     * @param submission The submission to update
     * @param operator   The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submission or operator is null
     * @throws IllegalArgumentException   The id is UNSET_ID
     * @throws IllegalArgumentException   If the submission (once the modification user/date is set) is not in a state
     *                                    to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    public void updateSubmission(Submission submission, String operator) throws UploadPersistenceException {

        Long id = new Long(-1);

        if (submission != null) {
            id = new Long(submission.getId());
        }

        LOGGER.log(Level.INFO,
                new LogMessage("Submission", id, operator, "update Submission."));

        persistenceUploadManagerHelper(submission, "submission", operator, UPDATE_FLAG, null);
    }

    /**
     * <p> Removes the given submission (by id) from the persistence. The id of the submission can not be UNSET_ID.
     * </p>
     *
     * @param submission The submission to remove
     * @param operator   The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submission or operator is null
     * @throws IllegalArgumentException   If the id of the submission is UNSET_ID
     * @throws IllegalArgumentException   If the submission (once the modification user/date is set) is not in a state
     *                                    to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    public void removeSubmission(Submission submission, String operator) throws UploadPersistenceException {

        Long id = new Long(-1);

        if (submission != null) {
            id = new Long(submission.getId());
        }

        LOGGER.log(Level.INFO,
                new LogMessage("Submission", id, operator, "remove Submission."));

        persistenceUploadManagerHelper(submission, "submission", operator, REMOVE_FLAG, null);
    }

    /**
     * <p> Gets the Submission in the persistence store for the given id. Returns null if there is no submission for the
     * given id. </p>
     *
     * @param id The id of the submission to retrieve
     *
     * @return The retrieved Submission, or null
     *
     * @throws IllegalArgumentException   If id is <= 0
     * @throws UploadPersistenceException If there is an error retrieving the Submission from persistence
     */
    public Submission getSubmission(long id) throws UploadPersistenceException {
        LOGGER.log(Level.INFO,
                new LogMessage("Submission", new Long(id), null, "Retrieve Submission, delegate to persistence."));
        return persistence.loadSubmission(id);
    }

    /**
     * <p> Searches the persistence for submissions meeting the filter The filter can be easily built using the
     * SubmissionFilterBuilder. </p>
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
    public Submission[] searchSubmissions(Filter filter) throws UploadPersistenceException, SearchBuilderException {
        DeliverableHelper.checkObjectNotNull(filter, "filter");
        CustomResultSet customResult = (CustomResultSet) submissionSearchBundle.search(filter);

        LOGGER.log(Level.INFO,
                new LogMessage("Upload", null, null, "search submissions with filter.") + " found: "
                        + customResult.getRecordCount());
        return persistence.loadSubmissions(customResult);
    }

    /**
     * <p> Creates a new SubmissionStatus in the persistence store. The id of the submission status must be UNSET_ID.
     * The manager will assign an id before persisting the change. </p>
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
    public void createSubmissionStatus(SubmissionStatus submissionStatus, String operator)
            throws UploadPersistenceException {
        LOGGER.log(Level.INFO,
                new LogMessage("SubmissionStatus", null, operator, "create new SubmissionStatus."));
        persistenceUploadManagerHelper(submissionStatus, "submissionStatus", operator, CREATE_FLAG,
                submissionStatusIdGenerator);
    }

    /**
     * <p> Updates the info for submission status in the persistence store. All fields of the submission status must
     * have values assigned when this method is called. </p>
     *
     * @param submissionStatus The submission status to update
     * @param operator         The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submissionStatus or operator is null
     * @throws IllegalArgumentException   The id is UNSET_ID
     * @throws IllegalArgumentException   If the submission status (once the modification user/date is set) is not in a
     *                                    state to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    public void updateSubmissionStatus(SubmissionStatus submissionStatus, String operator)
            throws UploadPersistenceException {

        Long id = new Long(-1);

        if (submissionStatus != null) {
            id = new Long(submissionStatus.getId());
        }

        LOGGER.log(Level.INFO, new LogMessage("SubmissionStatus",
                id, operator, "update SubmissionStatus."));

        persistenceUploadManagerHelper(submissionStatus, "submissionStatus", operator, UPDATE_FLAG, null);
    }

    /**
     * <p> Removes the given submission status (by id) from the persistence. The id of the submissionStatus can not be
     * UNSET_ID. </p>
     *
     * @param submissionStatus The submission status to remove
     * @param operator         The name of the operator making the change to the persistence store
     *
     * @throws IllegalArgumentException   If submissionStatus or operator is null
     * @throws IllegalArgumentException   If the id of the submission status is UNSET_ID
     * @throws IllegalArgumentException   If the submissionStaus (once the modification user/date is set) is not in a
     *                                    state to be persisted (i.e. if isValidToPersist returns false)
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    public void removeSubmissionStatus(SubmissionStatus submissionStatus, String operator)
            throws UploadPersistenceException {

        Long id = new Long(-1);

        if (submissionStatus != null) {
            id = new Long(submissionStatus.getId());
        }

        LOGGER.log(Level.INFO, new LogMessage("SubmissionStatus",
                id, operator, "remove SubmissionStatus."));

        persistenceUploadManagerHelper(submissionStatus, "submissionStatus", operator, REMOVE_FLAG, null);
    }

    /**
     * <p>Gets all the submission statuses in the persistence store.</p>
     *
     * @return All SubmissionStatuses in the persistence store.
     *
     * @throws UploadPersistenceException If there is an error accessing the persistence store
     */
    public SubmissionStatus[] getAllSubmissionStatuses() throws UploadPersistenceException {
        LOGGER.log(Level.INFO, "get All submission statuses, delegate to persistence.");
        return persistence.loadSubmissionStatuses(persistence.getAllSubmissionStatusIds());
    }

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
    public void createSubmissionType(SubmissionType submissionType, String operator) throws UploadPersistenceException {

        LOGGER.log(Level.INFO,
                new LogMessage("SubmissionType", null, operator, "create new SubmissionType."));
        persistenceUploadManagerHelper(submissionType, "submissionType", operator, CREATE_FLAG,
                submissionTypeIdGenerator);
    }

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
    public void updateSubmissionType(SubmissionType submissionType, String operator) throws UploadPersistenceException {

        Long id = new Long(-1);

        if (submissionType != null) {
            id = new Long(submissionType.getId());
        }

        LOGGER.log(Level.INFO, new LogMessage("SubmissionType",
                id, operator, "update SubmissionType."));

        persistenceUploadManagerHelper(submissionType, "submissionType", operator, UPDATE_FLAG, null);
    }

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
    public void removeSubmissionType(SubmissionType submissionType, String operator) throws UploadPersistenceException {

        Long id = new Long(-1);

        if (submissionType != null) {
            id = new Long(submissionType.getId());
        }

        LOGGER.log(Level.INFO, new LogMessage("SubmissionType",
                id, operator, "remove SubmissionType."));

        persistenceUploadManagerHelper(submissionType, "submissionType", operator, REMOVE_FLAG, null);
    }

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
    public SubmissionType[] getAllSubmissionTypes() throws UploadPersistenceException {

        LOGGER.log(Level.INFO, "get All submission types, delegate to persistence.");
        return persistence.loadSubmissionTypes(persistence.getAllSubmissionTypeIds());
    }

    /**
     * <p>Helper method for PersistenceUploadManager. All the create, update, remove methods delegate to this method. It
     * create (update, remove) AuditedDeliverableStructure object from the persistence.</p>
     *
     * @param obj         object to manipulate
     * @param name        object name which in manipulate in this method
     * @param operator    the operator name
     * @param operation   identifier for the caller
     * @param idGenerator the IDGenerator to use
     *
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    private void persistenceUploadManagerHelper(AuditedDeliverableStructure obj, String name,
                                                String operator, int operation, IDGenerator idGenerator)
            throws UploadPersistenceException {
        DeliverableHelper.checkObjectNotNull(obj, name);
        DeliverableHelper.checkObjectNotNull(operator, "operator");

        // Get current time.
        Date now = new Date();

        if (operation == CREATE_FLAG) {
            // Branch for CREATE_FLAG operation.

            // id should not been set yet.
            if (obj.getId() != AuditedDeliverableStructure.UNSET_ID) {
                LOGGER.log(Level.ERROR, "The id of the " + name + " must be UNSET_ID, " + obj.getId() + " received");
                throw new IllegalArgumentException("The id of the " + name + " must be UNSET_ID, " + obj.getId()
                        + " received");
            }

            // Set the creation user and creation date.
            obj.setCreationUser(operator);
            obj.setCreationTimestamp(now);

            // Create an id using idGenerator.
            try {
                obj.setId(idGenerator.getNextID());
                LOGGER.log(Level.INFO, "generate next id from the idGenerator:" + idGenerator.getIDName());
            } catch (IDGenerationException ide) {
                LOGGER.log(Level.ERROR,
                        "failed to generate an " + name + " id. \n" + LogMessage.getExceptionStackTrace(ide));
                throw new UploadPersistenceException("failed to generate an " + name + " id.", ide);
            }
        } else {
            // Branch for UPDATE_FLAG, REMOVE_FLAG operation

            // id field can not equal to UNSET_ID.
            if (obj.getId() == AuditedDeliverableStructure.UNSET_ID) {
                LOGGER.log(Level.ERROR, "The id of the " + name + " can't be UNSET_ID.");
                throw new IllegalArgumentException("The id of the " + name + " can't be UNSET_ID.");
            }
        }

        // Set the modification user and modification date.
        obj.setModificationUser(operator);
        obj.setModificationTimestamp(now);

        // Check if the object is ready to persist.
        if (!obj.isValidToPersist()) {
            LOGGER.log(Level.ERROR, "the " + name + " is not in a state to be persisted.");
            throw new IllegalArgumentException("the " + name + " is not in a state to be persisted.");
        }

        // Save change to persistence.
        persistenApply(obj, operation);
    }

    /**
     * <p>This is a helper method for applying opertions on persistence layer.</p>
     *
     * @param obj       the object to manipulate
     * @param operation operation identifier for the caller
     *
     * @throws UploadPersistenceException If there is an error making the change in the persistence
     */
    private void persistenApply(AuditedDeliverableStructure obj, int operation)
            throws UploadPersistenceException {
        if (obj instanceof Upload) {
            switch (operation) {
                case CREATE_FLAG:
                    // Add to persistence
                    persistence.addUpload((Upload) obj);
                    break;

                case UPDATE_FLAG:
                    // Update the persistence
                    persistence.updateUpload((Upload) obj);
                    break;

                case REMOVE_FLAG:
                    // Remove update from persistence
                    persistence.removeUpload((Upload) obj);
                    break;

                default:
                    break;
            }

        } else if (obj instanceof UploadType) {
            switch (operation) {
                case CREATE_FLAG:
                    // Add to persistence
                    persistence.addUploadType((UploadType) obj);
                    break;

                case UPDATE_FLAG:
                    // Update the persistence
                    persistence.updateUploadType((UploadType) obj);
                    break;

                case REMOVE_FLAG:
                    // Remove update type from persistence
                    persistence.removeUploadType((UploadType) obj);
                    break;

                default:
                    break;
            }
        } else if (obj instanceof UploadStatus) {
            switch (operation) {
                case CREATE_FLAG:
                    // Add to persistence
                    persistence.addUploadStatus((UploadStatus) obj);
                    break;

                case UPDATE_FLAG:
                    // Update the persistence
                    persistence.updateUploadStatus((UploadStatus) obj);
                    break;

                case REMOVE_FLAG:
                    // Remove upload status from persistence
                    persistence.removeUploadStatus((UploadStatus) obj);
                    break;

                default:
                    break;
            }
        } else if (obj instanceof Submission) {
            switch (operation) {
                case CREATE_FLAG:
                    // Add to persistence
                    persistence.addSubmission((Submission) obj);
                    break;

                case UPDATE_FLAG:
                    // Update the persistence
                    persistence.updateSubmission((Submission) obj);
                    break;

                case REMOVE_FLAG:
                    // Remove submission from persistence
                    persistence.removeSubmission((Submission) obj);
                    break;

                default:
                    break;
            }
        } else if (obj instanceof SubmissionStatus) {
            switch (operation) {
                case CREATE_FLAG:
                    // Add to persistence
                    persistence.addSubmissionStatus((SubmissionStatus) obj);
                    break;

                case UPDATE_FLAG:
                    // Update the persistence
                    persistence.updateSubmissionStatus((SubmissionStatus) obj);
                    break;

                case REMOVE_FLAG:
                    // Remove submission from persistence
                    persistence.removeSubmissionStatus((SubmissionStatus) obj);
                    break;

                default:
                    break;
            }
        } else if (obj instanceof SubmissionType) {
            switch (operation) {
                case CREATE_FLAG:
                    // Add to persistence
                    persistence.addSubmissionType((SubmissionType) obj);
                    break;

                case UPDATE_FLAG:
                    // Update the persistence
                    persistence.updateSubmissionType((SubmissionType) obj);
                    break;

                case REMOVE_FLAG:
                    // Remove submission from persistence
                    persistence.removeSubmissionType((SubmissionType) obj);
                    break;

                default:
                    break;
            }
        }
    }
}
