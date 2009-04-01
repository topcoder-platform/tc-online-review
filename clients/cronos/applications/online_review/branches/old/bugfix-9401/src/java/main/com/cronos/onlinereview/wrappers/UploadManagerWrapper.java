package com.cronos.onlinereview.wrappers;


/**
 * This class is a wrapper for PersistenceUploadManager class, which allows construction of it using OF.
 * Actually it has just the default constructor that passes the parameter values
 * to superclass constructor, which are hardcoded for Online Review application.
 * 
 * @author real_vg
 * @author George1
 *
 */ /*
public class UploadManagerWrapper extends PersistenceUploadManager {
    public UploadManagerWrapper() {
        // Get connection factory
        DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
        // Get the persistence
        UploadPersistence persistence = new SqlUploadPersistence(dbconn);

        // Get the ID generators
        IDGenerator uploadIdGenerator =
                IDGeneratorFactory.getIDGenerator(PersistenceUploadManager.UPLOAD_ID_GENERATOR_NAME);
        IDGenerator uploadTypeIdGenerator =
                IDGeneratorFactory.getIDGenerator(PersistenceUploadManager.UPLOAD_TYPE_ID_GENERATOR_NAME);
        IDGenerator uploadStatusIdGenerator =
                IDGeneratorFactory.getIDGenerator(PersistenceUploadManager.UPLOAD_STATUS_ID_GENERATOR_NAME);
        IDGenerator submissionIdGenerator =
                IDGeneratorFactory.getIDGenerator(PersistenceUploadManager.SUBMISSION_ID_GENERATOR_NAME);
        IDGenerator submissionStatusIdGenerator =
                IDGeneratorFactory.getIDGenerator(PersistenceUploadManager.SUBMISSION_STATUS_ID_GENERATOR_NAME);        
        
        // Get the search bundles
        SearchBundleManager searchBundleManager =
                new SearchBundleManager("com.topcoder.searchbuilder.common");

        SearchBundle uploadSearchBundle = searchBundleManager.getSearchBundle(
                PersistenceUploadManager.UPLOAD_SEARCH_BUNDLE_NAME);
        SearchBundle submissionSearchBundle = searchBundleManager.getSearchBundle(
                PersistenceUploadManager.SUBMISSION_SEARCH_BUNDLE_NAME);

        // Initialize the PersistenceUploadManager
        super(persistence,
                uploadSearchBundle, submissionSearchBundle,
                uploadIdGenerator, uploadTypeIdGenerator, uploadStatusIdGenerator,
                submissionIdGenerator, submissionStatusIdGenerator);

    }
}
*/