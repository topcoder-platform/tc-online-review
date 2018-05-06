/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.persistence.sql.SqlUploadPersistence;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.deliverable.search.UploadFilterBuilder;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit test for PersistenceUploadManager.
 *
 * @author singlewood, TCSDEVELOPER
 * @version 1.1
 */
public class PersistenceUploadManagerTests extends TestCase {

    /**
     * The name under which the upload search bundle should appear in the SearchBundleManager, if the
     * SearchBundleManager constructor is used.
     */
    public static final String UPLOAD_SEARCH_BUNDLE_NAME = "Upload Search Bundle";

    /**
     * The name under which the submission search bundle should appear in the SearchBundleManager, if the
     * SearchBundleManager constructor is used.
     */
    public static final String SUBMISSION_SEARCH_BUNDLE_NAME = "Submission Search Bundle";

    /**
     * The path of the configuration file.
     */
    private static final String CONFIG = "SearchBundleManager.xml";

    /**
     * File contains sql statement to initial database for upload search.
     */
    private static final String INIT_DB_SQL = "test_files/InitDB.sql";

    /**
     * File contains sql statement to clear database for upload search.
     */
    private static final String CLEAR_DB_SQL = "test_files/ClearDB.sql";

    /**
     * Represents the configuration namespace for search builder.
     */
    private static final String SEARCH_BUILDER_NAMESPACE = "com.topcoder.searchbuilder.DeliverableManager";

    /**
     * The test PersistenceUploadManager instance.
     */
    private PersistenceUploadManager persistenceUploadManager = null;

    /**
     * The test SqlUploadPersistence instance. Here We use SqlUploadPersistence instead of UploadPersistence, that way
     * we can add test methods in the SqlUploadPersistence.
     */
    private SqlUploadPersistence persistence = null;

    /**
     * The test SearchBundle instance.
     */
    private SearchBundle uploadSearchBundle = null;

    /**
     * The test SearchBundle instance.
     */
    private SearchBundle submissionSearchBundle = null;

    /**
     * The test IDGenerator instance.
     */
    private IDGenerator uploadIdGenerator = null;

    /**
     * The test IDGenerator instance.
     */
    private IDGenerator submissionIdGenerator = null;

    /**
     * The test IDGenerator instance.
     */
    private IDGenerator uploadTypeIdGenerator = null;

    /**
     * The test IDGenerator instance.
     */
    private IDGenerator uploadStatusIdGenerator = null;

    /**
     * The test IDGenerator instance.
     */
    private IDGenerator submissionStatusIdGenerator = null;

    /**
     * The test IDGenerator instance.
     */
    private IDGenerator submissionTypeIdGenerator = null;

    /**
     * The test SearchBundleManager instance.
     */
    private SearchBundleManager searchBundleManager = null;

    /**
     * Create the test instance.
     *
     * @throws Exception exception to JUnit.
     */
    public void setUp() throws Exception {
        DeliverableTestHelper.unloadConfig();
        DeliverableTestHelper.loadConfig(CONFIG);

        // Set up the SearchBundleManager.
        searchBundleManager = new SearchBundleManager(SEARCH_BUILDER_NAMESPACE);

        uploadSearchBundle = searchBundleManager.getSearchBundle(UPLOAD_SEARCH_BUNDLE_NAME);
        submissionSearchBundle = searchBundleManager.getSearchBundle(SUBMISSION_SEARCH_BUNDLE_NAME);

        // Bulid IDGenerators for test. Here we implement a simple IDGeneratorIncrease.
        uploadIdGenerator = new IDGeneratorIncrease();
        uploadIdGenerator = new IDGeneratorIncrease();
        submissionIdGenerator = new IDGeneratorIncrease();
        uploadTypeIdGenerator = new IDGeneratorIncrease();
        uploadStatusIdGenerator = new IDGeneratorIncrease();
        submissionStatusIdGenerator = new IDGeneratorIncrease();
        submissionTypeIdGenerator = new IDGeneratorIncrease();

        // No real database is used in this test.
        persistence = new SqlUploadPersistence(null);

        // Create a PersistenceUploadManager.
        persistenceUploadManager = new PersistenceUploadManager(persistence, uploadSearchBundle,
                submissionSearchBundle, uploadIdGenerator, uploadTypeIdGenerator, uploadStatusIdGenerator,
                submissionIdGenerator, submissionStatusIdGenerator, submissionTypeIdGenerator);
    }

    /**
     * Clean the config.
     *
     * @throws Exception exception to JUnit.
     */
    public void tearDown() throws Exception {
        // no operation.
    }

    /**
     * Set the first parameter of constructor1 to null. IllegalArgumentException should be thrown.
     */
    public void testConstructor1_Null1() {
        try {
            new PersistenceUploadManager(null, uploadSearchBundle, submissionSearchBundle, uploadIdGenerator,
                    uploadTypeIdGenerator, uploadStatusIdGenerator, submissionIdGenerator, submissionStatusIdGenerator,
                    submissionTypeIdGenerator);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Set the 2th parameter of constructor1 to null. IllegalArgumentException should be thrown.
     */
    public void testConstructor1_Null2() {
        try {
            new PersistenceUploadManager(persistence, null, submissionSearchBundle, uploadIdGenerator,
                    uploadTypeIdGenerator, uploadStatusIdGenerator, submissionIdGenerator, submissionStatusIdGenerator,
                    submissionTypeIdGenerator);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Set the 3th parameter of constructor1 to null. IllegalArgumentException should be thrown.
     */
    public void testConstructor1_Null3() {
        try {
            new PersistenceUploadManager(persistence, uploadSearchBundle, null, uploadIdGenerator,
                    uploadTypeIdGenerator, uploadStatusIdGenerator, submissionIdGenerator, submissionStatusIdGenerator,
                    submissionTypeIdGenerator);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Set the 4th parameter of constructor1 to null. IllegalArgumentException should be thrown.
     */
    public void testConstructor1_Null4() {
        try {
            new PersistenceUploadManager(persistence, uploadSearchBundle, submissionSearchBundle, null,
                    uploadTypeIdGenerator, uploadStatusIdGenerator, submissionIdGenerator, submissionStatusIdGenerator,
                    submissionTypeIdGenerator);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Set the 5th parameter of constructor1 to null. IllegalArgumentException should be thrown.
     */
    public void testConstructor1_Null5() {
        try {
            new PersistenceUploadManager(persistence, uploadSearchBundle, submissionSearchBundle, uploadIdGenerator,
                    null, uploadStatusIdGenerator, submissionIdGenerator, submissionStatusIdGenerator,
                    submissionTypeIdGenerator);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Set the 6th parameter of constructor1 to null. IllegalArgumentException should be thrown.
     */
    public void testConstructor1_Null6() {
        try {
            new PersistenceUploadManager(persistence, uploadSearchBundle, submissionSearchBundle, uploadIdGenerator,
                    uploadTypeIdGenerator, null, submissionIdGenerator, submissionStatusIdGenerator,
                    submissionTypeIdGenerator);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Set the 7th parameter of constructor1 to null. IllegalArgumentException should be thrown.
     */
    public void testConstructor1_Null7() {
        try {
            new PersistenceUploadManager(persistence, uploadSearchBundle, submissionSearchBundle, uploadIdGenerator,
                    uploadTypeIdGenerator, uploadStatusIdGenerator, null, submissionStatusIdGenerator,
                    submissionTypeIdGenerator);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Set constructor1 with valid parameter. No exception should be thrown.
     */
    public void testConstructor1_Valid() {
        try {
            new PersistenceUploadManager(persistence, uploadSearchBundle, submissionSearchBundle, uploadIdGenerator,
                    uploadTypeIdGenerator, uploadStatusIdGenerator, submissionIdGenerator, submissionStatusIdGenerator,
                    submissionTypeIdGenerator);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown because of valid parameters.");
        }
    }

    /**
     * Set the 1th parameter of constructor2 to null. IllegalArgumentException should be thrown.
     */
    public void testConstructor2_Null1() {
        try {
            new PersistenceUploadManager(null, searchBundleManager);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (IDGenerationException e) {
            // IllegalArgumentException is not be thrown here because checking on persistence
            // is after the checking of IDGeneration. But it won't affect the function.
            //fail("IDGenerationException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the 2th parameter of constructor2 to null. IllegalArgumentException should be thrown.
     */
    public void testConstructor2_Null2() {
        try {
            new PersistenceUploadManager(persistence, null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (IDGenerationException e) {
            fail("IDGenerationException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createUpload. Set the 1th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testCreateUpload_Null1() {
        try {
            persistenceUploadManager.createUpload(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createUpload. Set the 2th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testCreateUpload_Null2() {
        try {
            persistenceUploadManager.createUpload(new Upload(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createUpload. Set the upload's id to 10. Since upload's id should be UNSET_ID,
     * IllegalArgumentException should be thrown.
     */
    public void testCreateUpload_Invalid1() {
        try {
            persistenceUploadManager.createUpload(new Upload(10), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of createUpload. Give some valid parameters. See if the method can successfully add the Upload
     * object to persistence No exception should be thrown.
     */
    public void testCreateUpload_Accuracy1() {
        try {
            // Get a Upload.
            Upload upload = DeliverableTestHelper.getValidToPersistUpload();

            // Call the method.
            persistenceUploadManager.createUpload(upload, "user");

            // Check creationUser field.
            assertEquals("createUpload doesn't work properly", "user", persistence.getUpload().getCreationUser());

            // Check modificationUser field.
            assertEquals("createUpload doesn't work properly", "user", persistence.getUpload().getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("createUpload doesn't work properly", persistence.getUpload().getCreationTimestamp());
            assertNotNull("createUpload doesn't work properly", persistence.getUpload().getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of updateUpload. Set the 1th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testUpdateUpload_Null1() {
        try {
            persistenceUploadManager.updateUpload(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateUpload. Set the 2th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testUpdateUpload_Null2() {
        try {
            persistenceUploadManager.updateUpload(new Upload(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateUpload. Set the upload's id to UNSET_ID. Since upload's id should not be UNSET_ID,
     * IllegalArgumentException should be thrown.
     */
    public void testUpdateUpload_Invalid1() {
        try {
            persistenceUploadManager.updateUpload(new Upload(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of updateUpload. Give some valid parameters. See if the method can successfully add the Upload
     * object to persistence No exception should be thrown.
     */
    public void testUpdateUpload_Accuracy1() {
        try {
            // Get a Upload.
            Upload upload = DeliverableTestHelper.getValidToPersistUpload();

            // Create a upload first.
            persistenceUploadManager.createUpload(upload, "CreationUser");

            // Call the method.
            persistenceUploadManager.updateUpload(upload, "ModificationUser");

            // Check creationUser field.
            assertEquals("updateUpload doesn't work properly", "CreationUser", persistence.getUpload()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("updateUpload doesn't work properly", "ModificationUser", persistence.getUpload()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("updateUpload doesn't work properly", persistence.getUpload().getCreationTimestamp());
            assertNotNull("updateUpload doesn't work properly", persistence.getUpload().getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of removeUpload. Set the 1th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testRemoveUpload_Null1() {
        try {
            persistenceUploadManager.removeUpload(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeUpload. Set the 2th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testRemoveUpload_Null2() {
        try {
            persistenceUploadManager.removeUpload(new Upload(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeUpload. Set the upload's id to UNSET_ID. Since upload's id should not be UNSET_ID,
     * IllegalArgumentException should be thrown.
     */
    public void testRemoveUpload_Invalid1() {
        try {
            persistenceUploadManager.removeUpload(new Upload(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of removeUpload. Give some valid parameters. See if the method can successfully add the Upload
     * object to persistence No exception should be thrown.
     */
    public void testRemoveUpload_Accuracy1() {
        try {
            // Get a Upload.
            Upload upload = DeliverableTestHelper.getValidToPersistUpload();

            // Create a upload first.
            persistenceUploadManager.createUpload(upload, "CreationUser");

            // Call the method.
            persistenceUploadManager.removeUpload(upload, "ModificationUser");

            // Check creationUser field.
            assertEquals("removeUpload doesn't work properly", "CreationUser", persistence.getUpload()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("removeUpload doesn't work properly", "ModificationUser", persistence.getUpload()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("removeUpload doesn't work properly", persistence.getUpload().getCreationTimestamp());
            assertNotNull("removeUpload doesn't work properly", persistence.getUpload().getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of getUpload. Set the paramter = 0. IllegalArgumentException should be thrown.
     */
    public void testGetUpload_Invalid() {
        try {
            persistenceUploadManager.getUpload(0);
            fail("IllegalArgumentException should be thrown because of invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of getUpload. Set the paramter = 111, then the id field of return Upload will be 111 too.
     */
    public void testGetUpload_Accuracy() {
        try {
            assertEquals("getUpload", 111, persistenceUploadManager.getUpload(111).getId());
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown under this config.");
        }
    }

    /**
     * Set the behaviour of searchUpload. Set the paramter = null. IllegalArgumentException should be thrown.
     */
    public void testSearchUpload_Null() {
        try {
            persistenceUploadManager.searchUploads(null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        } catch (SearchBuilderException e) {
            fail("SearchBuilderException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createUploadType. Set the 1th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testCreateUploadType_Null1() {
        try {
            persistenceUploadManager.createUploadType(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createUploadType. Set the 2th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testCreateUploadType_Null2() {
        try {
            persistenceUploadManager.createUploadType(new UploadType(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createUploadType. Set the uploadType's id to 10. Since uploadType's id should be UNSET_ID,
     * IllegalArgumentException should be thrown.
     */
    public void testCreateUploadType_Invalid1() {
        try {
            persistenceUploadManager.createUploadType(new UploadType(10), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of createUploadType. Give some valid parameters. See if the method can successfully add the
     * UploadType object to persistence No exception should be thrown.
     */
    public void testCreateUploadType_Accuracy1() {
        try {
            // Get a UploadType.
            UploadType uploadType = DeliverableTestHelper.getValidToPersistUploadType();

            // Call the method.
            persistenceUploadManager.createUploadType(uploadType, "user");

            // Check creationUser field.
            assertEquals("createUploadType doesn't work properly", "user", persistence.getUploadType()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("createUploadType doesn't work properly", "user", persistence.getUploadType()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("createUploadType doesn't work properly", persistence.getUploadType().getCreationTimestamp());
            assertNotNull("createUploadType doesn't work properly", persistence.getUploadType()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of updateUploadType. Set the 1th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testUpdateUploadType_Null1() {
        try {
            persistenceUploadManager.updateUploadType(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateUploadType. Set the 2th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testUpdateUploadType_Null2() {
        try {
            persistenceUploadManager.updateUploadType(new UploadType(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateUploadType. Set the uploadType's id to UNSET_ID. Since uploadType's id should not be
     * UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testUpdateUploadType_Invalid1() {
        try {
            persistenceUploadManager.updateUploadType(new UploadType(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of updateUploadType. Give some valid parameters. See if the method can successfully add the
     * UploadType object to persistence No exception should be thrown.
     */
    public void testUpdateUploadType_Accuracy1() {
        try {
            // Get a UploadType.
            UploadType uploadType = DeliverableTestHelper.getValidToPersistUploadType();

            // Create a uploadType first.
            persistenceUploadManager.createUploadType(uploadType, "CreationUser");

            // Call the method.
            persistenceUploadManager.updateUploadType(uploadType, "ModificationUser");

            // Check creationUser field.
            assertEquals("updateUploadType doesn't work properly", "CreationUser", persistence.getUploadType()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("updateUploadType doesn't work properly", "ModificationUser", persistence.getUploadType()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("updateUploadType doesn't work properly", persistence.getUploadType().getCreationTimestamp());
            assertNotNull("updateUploadType doesn't work properly", persistence.getUploadType()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of removeUploadType. Set the 1th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testRemoveUploadType_Null1() {
        try {
            persistenceUploadManager.removeUploadType(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeUploadType. Set the 2th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testRemoveUploadType_Null2() {
        try {
            persistenceUploadManager.removeUploadType(new UploadType(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeUploadType. Set the uploadType's id to UNSET_ID. Since uploadType's id should not be
     * UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testRemoveUploadType_Invalid1() {
        try {
            persistenceUploadManager.removeUploadType(new UploadType(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of removeUploadType. Give some valid parameters. See if the method can successfully add the
     * UploadType object to persistence No exception should be thrown.
     */
    public void testRemoveUploadType_Accuracy1() {
        try {
            // Get a UploadType.
            UploadType uploadType = DeliverableTestHelper.getValidToPersistUploadType();

            // Create a uploadType first.
            persistenceUploadManager.createUploadType(uploadType, "CreationUser");

            // Call the method.
            persistenceUploadManager.removeUploadType(uploadType, "ModificationUser");

            // Check creationUser field.
            assertEquals("removeUploadType doesn't work properly", "CreationUser", persistence.getUploadType()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("removeUploadType doesn't work properly", "ModificationUser", persistence.getUploadType()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("removeUploadType doesn't work properly", persistence.getUploadType().getCreationTimestamp());
            assertNotNull("removeUploadType doesn't work properly", persistence.getUploadType()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Sicne the getAllUploadTypes totally invoke method in persistence, We just use MOCK persistence class to prove it
     * is invoked properly.
     */
    public void testGetAllUploadTypes_Accuracy() {
        UploadType uploadType = DeliverableTestHelper.getValidToPersistUploadType();
        uploadType.setId(2);
        try {
            assertEquals("getAllUploadStatus doesn't work.",
                    uploadType.getId(),
                    (persistenceUploadManager.getAllUploadTypes())[0].getId());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown.");
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown.");
        }
    }

    /**
     * Set the behaviour of createUploadStatus. Set the 1th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testCreateUploadStatus_Null1() {
        try {
            persistenceUploadManager.createUploadStatus(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createUploadStatus. Set the 2th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testCreateUploadStatus_Null2() {
        try {
            persistenceUploadManager.createUploadStatus(new UploadStatus(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createUploadStatus. Set the uploadStatus's id to 10. Since uploadStatus's id should be
     * UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testCreateUploadStatus_Invalid1() {
        try {
            persistenceUploadManager.createUploadStatus(new UploadStatus(10), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of createUploadStatus. Give some valid parameters. See if the method can successfully add the
     * UploadStatus object to persistence No exception should be thrown.
     */
    public void testCreateUploadStatus_Accuracy1() {
        try {
            // Get a UploadStatus.
            UploadStatus uploadStatus = DeliverableTestHelper.getValidToPersistUploadStatus();

            // Call the method.
            persistenceUploadManager.createUploadStatus(uploadStatus, "user");

            // Check creationUser field.
            assertEquals("createUploadStatus doesn't work properly", "user", persistence.getUploadStatus()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("createUploadStatus doesn't work properly", "user", persistence.getUploadStatus()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("createUploadStatus doesn't work properly", persistence.getUploadStatus()
                    .getCreationTimestamp());
            assertNotNull("createUploadStatus doesn't work properly", persistence.getUploadStatus()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of updateUploadStatus. Set the 1th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testUpdateUploadStatus_Null1() {
        try {
            persistenceUploadManager.updateUploadStatus(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateUploadStatus. Set the 2th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testUpdateUploadStatus_Null2() {
        try {
            persistenceUploadManager.updateUploadStatus(new UploadStatus(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateUploadStatus. Set the uploadStatus's id to UNSET_ID. Since uploadStatus's id should
     * not be UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testUpdateUploadStatus_Invalid1() {
        try {
            persistenceUploadManager.updateUploadStatus(new UploadStatus(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of updateUploadStatus. Give some valid parameters. See if the method can successfully add the
     * UploadStatus object to persistence No exception should be thrown.
     */
    public void testUpdateUploadStatus_Accuracy1() {
        try {
            // Get a UploadStatus.
            UploadStatus uploadStatus = DeliverableTestHelper.getValidToPersistUploadStatus();

            // Create a uploadStatus first.
            persistenceUploadManager.createUploadStatus(uploadStatus, "CreationUser");

            // Call the method.
            persistenceUploadManager.updateUploadStatus(uploadStatus, "ModificationUser");

            // Check creationUser field.
            assertEquals("updateUploadStatus doesn't work properly", "CreationUser", persistence.getUploadStatus()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("updateUploadStatus doesn't work properly", "ModificationUser", persistence.getUploadStatus()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("updateUploadStatus doesn't work properly", persistence.getUploadStatus()
                    .getCreationTimestamp());
            assertNotNull("updateUploadStatus doesn't work properly", persistence.getUploadStatus()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of removeUploadStatus. Set the 1th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testRemoveUploadStatus_Null1() {
        try {
            persistenceUploadManager.removeUploadStatus(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeUploadStatus. Set the 2th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testRemoveUploadStatus_Null2() {
        try {
            persistenceUploadManager.removeUploadStatus(new UploadStatus(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeUploadStatus. Set the uploadStatus's id to UNSET_ID. Since uploadStatus's id should
     * not be UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testRemoveUploadStatus_Invalid1() {
        try {
            persistenceUploadManager.removeUploadStatus(new UploadStatus(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of removeUploadStatus. Give some valid parameters. See if the method can successfully add the
     * UploadStatus object to persistence No exception should be thrown.
     */
    public void testRemoveUploadStatus_Accuracy1() {
        try {
            // Get a UploadStatus.
            UploadStatus uploadStatus = DeliverableTestHelper.getValidToPersistUploadStatus();

            // Create a uploadStatus first.
            persistenceUploadManager.createUploadStatus(uploadStatus, "CreationUser");

            // Call the method.
            persistenceUploadManager.removeUploadStatus(uploadStatus, "ModificationUser");

            // Check creationUser field.
            assertEquals("removeUploadStatus doesn't work properly", "CreationUser", persistence.getUploadStatus()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("removeUploadStatus doesn't work properly", "ModificationUser", persistence.getUploadStatus()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("removeUploadStatus doesn't work properly", persistence.getUploadStatus()
                    .getCreationTimestamp());
            assertNotNull("removeUploadStatus doesn't work properly", persistence.getUploadStatus()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Sicne the getAllUploadStatuses totally invoke method in persistence, We just use MOCK persistence class to prove
     * it is invoked properly.
     */
    public void testGetAllUploadStatuses_Accuracy() {
        UploadStatus uploadStatus1 = DeliverableTestHelper.getValidToPersistUploadStatus();
        uploadStatus1.setId(2);
        try {
            assertEquals("getAllUploadStatus doesn't work.", uploadStatus1.getId(),
                    (persistenceUploadManager.getAllUploadStatuses())[0].getId());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown.");
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown.");
        }
    }

    /**
     * Set the behaviour of createSubmission. Set the 1th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testCreateSubmission_Null1() {
        try {
            persistenceUploadManager.createSubmission(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createSubmission. Set the 2th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testCreateSubmission_Null2() {
        try {
            persistenceUploadManager.createSubmission(new Submission(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createSubmission. Set the submission's id to 10. Since submission's id should be UNSET_ID,
     * IllegalArgumentException should be thrown.
     */
    public void testCreateSubmission_Invalid1() {
        try {
            persistenceUploadManager.createSubmission(new Submission(10), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of createSubmission. Give some valid parameters. See if the method can successfully add the
     * Submission object to persistence No exception should be thrown.
     */
    public void testCreateSubmission_Accuracy1() {
        try {
            // Get a Submission.
            Submission submission = DeliverableTestHelper.getValidToPersistSubmission();

            // Call the method.
            persistenceUploadManager.createSubmission(submission, "user");

            // Check creationUser field.
            assertEquals("createSubmission doesn't work properly", "user", persistence.getSubmission()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("createSubmission doesn't work properly", "user", persistence.getSubmission()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("createSubmission doesn't work properly", persistence.getSubmission().getCreationTimestamp());
            assertNotNull("createSubmission doesn't work properly", persistence.getSubmission()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmission. Set the 1th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testUpdateSubmission_Null1() {
        try {
            persistenceUploadManager.updateSubmission(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmission. Set the 2th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testUpdateSubmission_Null2() {
        try {
            persistenceUploadManager.updateSubmission(new Submission(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmission. Set the submission's id to UNSET_ID. Since submission's id should not be
     * UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testUpdateSubmission_Invalid1() {
        try {
            persistenceUploadManager.updateSubmission(new Submission(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmission. Give some valid parameters. See if the method can successfully add the
     * Submission object to persistence No exception should be thrown.
     */
    public void testUpdateSubmission_Accuracy1() {
        try {
            // Get a Submission.
            Submission submission = DeliverableTestHelper.getValidToPersistSubmission();

            // Create a submission first.
            persistenceUploadManager.createSubmission(submission, "CreationUser");

            // Call the method.
            persistenceUploadManager.updateSubmission(submission, "ModificationUser");

            // Check creationUser field.
            assertEquals("updateSubmission doesn't work properly", "CreationUser", persistence.getSubmission()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("updateSubmission doesn't work properly", "ModificationUser", persistence.getSubmission()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("updateSubmission doesn't work properly", persistence.getSubmission().getCreationTimestamp());
            assertNotNull("updateSubmission doesn't work properly", persistence.getSubmission()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmission. Set the 1th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testRemoveSubmission_Null1() {
        try {
            persistenceUploadManager.removeSubmission(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmission. Set the 2th paramter to null. IllegalArgumentException should be thrown.
     */
    public void testRemoveSubmission_Null2() {
        try {
            persistenceUploadManager.removeSubmission(new Submission(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmission. Set the submission's id to UNSET_ID. Since submission's id should not be
     * UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testRemoveSubmission_Invalid1() {
        try {
            persistenceUploadManager.removeSubmission(new Submission(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmission. Give some valid parameters. See if the method can successfully add the
     * Submission object to persistence No exception should be thrown.
     */
    public void testRemoveSubmission_Accuracy1() {
        try {
            // Get a Submission.
            Submission submission = DeliverableTestHelper.getValidToPersistSubmission();

            // Create a submission first.
            persistenceUploadManager.createSubmission(submission, "CreationUser");

            // Call the method.
            persistenceUploadManager.removeSubmission(submission, "ModificationUser");

            // Check creationUser field.
            assertEquals("removeSubmission doesn't work properly", "CreationUser", persistence.getSubmission()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("removeSubmission doesn't work properly", "ModificationUser", persistence.getSubmission()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("removeSubmission doesn't work properly", persistence.getSubmission().getCreationTimestamp());
            assertNotNull("removeSubmission doesn't work properly", persistence.getSubmission()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of getSubmission. Set the paramter = 0. IllegalArgumentException should be thrown.
     */
    public void testGetSubmission_Invalid() {
        try {
            persistenceUploadManager.getUpload(0);
            fail("IllegalArgumentException should be thrown because of invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of getSubmission. Set the paramter = 111, then the id field of return Submission will be 111
     * too.
     */
    public void testGetSubmission_Accuracy() {
        try {
            assertEquals("getSubmission doesn't work.", 111, persistenceUploadManager.getSubmission(111).getId());
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown under this config.");
        }
    }

    /**
     * Set the behaviour of searchSubmission. Set the paramter = null. IllegalArgumentException should be thrown.
     */
    public void testSearchSubmission_Null() {
        try {
            persistenceUploadManager.searchSubmissions(null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (SearchBuilderException e) {
            fail("SearchBuilderException should not be thrown because of null parameter.");
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createSubmissionStatus. Set the 1th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testCreateSubmissionStatus_Null1() {
        try {
            persistenceUploadManager.createSubmissionStatus(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createSubmissionStatus. Set the 2th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testCreateSubmissionStatus_Null2() {
        try {
            persistenceUploadManager.createSubmissionStatus(new SubmissionStatus(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createSubmissionStatus. Set the submissionStatus's id to 10. Since submissionStatus's id
     * should be UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testCreateSubmissionStatus_Invalid1() {
        try {
            persistenceUploadManager.createSubmissionStatus(new SubmissionStatus(10), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of createSubmissionStatus. Give some valid parameters. See if the method can successfully add
     * the SubmissionStatus object to persistence No exception should be thrown.
     */
    public void testCreateSubmissionStatus_Accuracy1() {
        try {
            // Get a SubmissionStatus.
            SubmissionStatus submissionStatus = DeliverableTestHelper.getValidToPersistSubmissionStatus();

            // Call the method.
            persistenceUploadManager.createSubmissionStatus(submissionStatus, "user");

            // Check creationUser field.
            assertEquals("createSubmissionStatus doesn't work properly", "user", persistence.getSubmissionStatus()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("createSubmissionStatus doesn't work properly", "user", persistence.getSubmissionStatus()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("createSubmissionStatus doesn't work properly", persistence.getSubmissionStatus()
                    .getCreationTimestamp());
            assertNotNull("createSubmissionStatus doesn't work properly", persistence.getSubmissionStatus()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmissionStatus. Set the 1th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testUpdateSubmissionStatus_Null1() {
        try {
            persistenceUploadManager.updateSubmissionStatus(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmissionStatus. Set the 2th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testUpdateSubmissionStatus_Null2() {
        try {
            persistenceUploadManager.updateSubmissionStatus(new SubmissionStatus(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmissionStatus. Set the submissionStatus's id to UNSET_ID. Since submissionStatus's
     * id should not be UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testUpdateSubmissionStatus_Invalid1() {
        try {
            persistenceUploadManager.updateSubmissionStatus(new SubmissionStatus(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmissionStatus. Give some valid parameters. See if the method can successfully add
     * the SubmissionStatus object to persistence No exception should be thrown.
     */
    public void testUpdateSubmissionStatus_Accuracy1() {
        try {
            // Get a SubmissionStatus.
            SubmissionStatus submissionStatus = DeliverableTestHelper.getValidToPersistSubmissionStatus();

            // Create a submissionStatus first.
            persistenceUploadManager.createSubmissionStatus(submissionStatus, "CreationUser");

            // Call the method.
            persistenceUploadManager.updateSubmissionStatus(submissionStatus, "ModificationUser");

            // Check creationUser field.
            assertEquals("updateSubmissionStatus doesn't work properly", "CreationUser", persistence
                    .getSubmissionStatus().getCreationUser());

            // Check modificationUser field.
            assertEquals("updateSubmissionStatus doesn't work properly", "ModificationUser", persistence
                    .getSubmissionStatus().getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("updateSubmissionStatus doesn't work properly", persistence.getSubmissionStatus()
                    .getCreationTimestamp());
            assertNotNull("updateSubmissionStatus doesn't work properly", persistence.getSubmissionStatus()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmissionStatus. Set the 1th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testRemoveSubmissionStatus_Null1() {
        try {
            persistenceUploadManager.removeSubmissionStatus(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmissionStatus. Set the 2th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testRemoveSubmissionStatus_Null2() {
        try {
            persistenceUploadManager.removeSubmissionStatus(new SubmissionStatus(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmissionStatus. Set the submissionStatus's id to UNSET_ID. Since submissionStatus's
     * id should not be UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testRemoveSubmissionStatus_Invalid1() {
        try {
            persistenceUploadManager.removeSubmissionStatus(new SubmissionStatus(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmissionStatus. Give some valid parameters. See if the method can successfully add
     * the SubmissionStatus object to persistence No exception should be thrown.
     */
    public void testRemoveSubmissionStatus_Accuracy1() {
        try {
            // Get a SubmissionStatus.
            SubmissionStatus submissionStatus = DeliverableTestHelper.getValidToPersistSubmissionStatus();

            // Create a submissionStatus first.
            persistenceUploadManager.createSubmissionStatus(submissionStatus, "CreationUser");

            // Call the method.
            persistenceUploadManager.removeSubmissionStatus(submissionStatus, "ModificationUser");

            // Check creationUser field.
            assertEquals("removeSubmissionStatus doesn't work properly", "CreationUser", persistence
                    .getSubmissionStatus().getCreationUser());

            // Check modificationUser field.
            assertEquals("removeSubmissionStatus doesn't work properly", "ModificationUser", persistence
                    .getSubmissionStatus().getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("removeSubmissionStatus doesn't work properly", persistence.getSubmissionStatus()
                    .getCreationTimestamp());
            assertNotNull("removeSubmissionStatus doesn't work properly", persistence.getSubmissionStatus()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Since the getAllSubmissionStatuses totally invoke method in persistence, We just use MOCK persistence class to
     * prove it is invoked properly.
     */
    public void testGetAllSubmissionStatuses_Accuracy() {
        SubmissionStatus submissionStatus = DeliverableTestHelper.getValidToPersistSubmissionStatus();
        submissionStatus.setId(2);
        try {
            assertEquals("getAllUploadStatus doesn't work.", submissionStatus.getId(),
                    (persistenceUploadManager.getAllSubmissionStatuses())[0].getId());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown.");
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown.");
        }
    }

    /**
     * Testss searchUploads method with upload id filter.
     *
     * @throws Exception if any error occurs
     */
    public void testSearchUploadsByUploadId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            for (int i = 1; i <= 5; ++i) {
                Upload[] uploads = persistenceUploadManager.searchUploads(UploadFilterBuilder.createUploadIdFilter(i));
                assertEquals("Wrong number of uploads retrieved", 1, uploads.length);
                assertEquals("Wrong upload retrieved", i, uploads[0].getId());
            }

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadUploads"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    /**
     * Testss searchUploads method with upload type id filter.
     *
     * @throws Exception if any error occurs
     */
    public void testSearchUploadsByUploadTypeId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            Upload[] uploads = persistenceUploadManager.searchUploads(UploadFilterBuilder.createUploadTypeIdFilter(3));
            assertEquals("Wrong number of uploads retrieved", 2, uploads.length);

            Set ids = new HashSet();

            for (int i = 0; i < uploads.length; ++i) {
                ids.add(new Long(uploads[i].getId()));
            }

            assertTrue("Wrong upload retrieved", ids.contains(new Long(2)));
            assertTrue("Wrong upload retrieved", ids.contains(new Long(3)));

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadUploads"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    /**
     * Testss searchUploads method with upload status id filter.
     *
     * @throws Exception if any error occurs
     */
    public void testSearchUploadsByUploadStatusId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            Upload[] uploads = persistenceUploadManager
                    .searchUploads(UploadFilterBuilder.createUploadStatusIdFilter(1));
            assertEquals("Wrong number of uploads retrieved", 3, uploads.length);

            Set ids = new HashSet();

            for (int i = 0; i < uploads.length; ++i) {
                ids.add(new Long(uploads[i].getId()));
            }

            assertTrue("Wrong upload retrieved", ids.contains(new Long(1)));
            assertTrue("Wrong upload retrieved", ids.contains(new Long(3)));
            assertTrue("Wrong upload retrieved", ids.contains(new Long(4)));

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadUploads"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    /**
     * Testss searchUploads method with project id filter.
     *
     * @throws Exception if any error occurs
     */
    public void testSearchUploadsByProjectId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            Upload[] uploads = persistenceUploadManager.searchUploads(UploadFilterBuilder.createProjectIdFilter(1));
            assertEquals("Wrong number of uploads retrieved", 2, uploads.length);

            Set ids = new HashSet();

            for (int i = 0; i < uploads.length; ++i) {
                ids.add(new Long(uploads[i].getId()));
            }

            assertTrue("Wrong upload retrieved", ids.contains(new Long(1)));
            assertTrue("Wrong upload retrieved", ids.contains(new Long(4)));

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadUploads"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    /**
     * Testss searchUploads method with resource id filter.
     *
     * @throws Exception if any error occurs
     */
    public void testSearchUploadsByResourceId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            Upload[] uploads = persistenceUploadManager.searchUploads(UploadFilterBuilder.createResourceIdFilter(2));
            assertEquals("Wrong number of uploads retrieved", 2, uploads.length);

            Set ids = new HashSet();

            for (int i = 0; i < uploads.length; ++i) {
                ids.add(new Long(uploads[i].getId()));
            }

            assertTrue("Wrong upload retrieved", ids.contains(new Long(2)));
            assertTrue("Wrong upload retrieved", ids.contains(new Long(5)));

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadUploads"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    /**
     * Testss searchSubmissions method with submission id filter.
     *
     * @throws Exception if any error occurs
     */
    public void testSearchSubmissionsBySubmissionId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            for (int i = 1; i <= 5; ++i) {
                Submission[] submissions = persistenceUploadManager.searchSubmissions(SubmissionFilterBuilder
                        .createSubmissionIdFilter(i));
                assertEquals("Wrong number of submissions retrieved", 1, submissions.length);
                assertEquals("Wrong submission retrieved", i, submissions[0].getId());
            }

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadSubmissions"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    /**
     * Testss searchSubmissions method with upload id filter.
     *
     * @throws Exception if any error occurs
     */
    public void testSearchSubmissionsByUploadId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            Submission[] submissions = persistenceUploadManager.searchSubmissions(SubmissionFilterBuilder
                    .createUploadIdFilter(1));
            assertEquals("Wrong number of submissions retrieved", 2, submissions.length);

            Set ids = new HashSet();

            for (int i = 0; i < submissions.length; ++i) {
                ids.add(new Long(submissions[i].getId()));
            }

            assertTrue("Wrong submission retrieved", ids.contains(new Long(1)));
            assertTrue("Wrong submission retrieved", ids.contains(new Long(4)));

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadSubmissions"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    /**
     * Testss searchSubmissions method with submission status id filter.
     *
     * @throws Exception if any error occurs
     */
    public void testSearchSubmissionsBySubmissionStatusId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            Submission[] submissions = persistenceUploadManager.searchSubmissions(SubmissionFilterBuilder
                    .createSubmissionStatusIdFilter(4));
            assertEquals("Wrong number of submissions retrieved", 3, submissions.length);

            Set ids = new HashSet();

            for (int i = 0; i < submissions.length; ++i) {
                ids.add(new Long(submissions[i].getId()));
            }

            assertTrue("Wrong submission retrieved", ids.contains(new Long(1)));
            assertTrue("Wrong submission retrieved", ids.contains(new Long(4)));
            assertTrue("Wrong submission retrieved", ids.contains(new Long(5)));

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadSubmissions"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    /**
     * Testss searchSubmissions method with submission status id filter.
     *
     * Added in version 1.1
     *
     * @throws Exception if any error occurs
     */
    public void testSearchSubmissionsBySubmissionTypeId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            Submission[] submissions = persistenceUploadManager.searchSubmissions(SubmissionFilterBuilder
                    .createSubmissionTypeIdFilter(2));
            assertEquals("Wrong number of submissions retrieved", 2, submissions.length);

            Set ids = new HashSet();

            for (int i = 0; i < submissions.length; ++i) {
                ids.add(new Long(submissions[i].getId()));
            }

            assertTrue("Wrong submission retrieved", ids.contains(new Long(2)));
            assertTrue("Wrong submission retrieved", ids.contains(new Long(3)));

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadSubmissions"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    /**
     * Testss searchSubmissions method with project id filter.
     *
     * @throws Exception if any error occurs
     */
    public void testSearchSubmissionsByProjectId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            Submission[] submissions = persistenceUploadManager.searchSubmissions(SubmissionFilterBuilder
                    .createProjectIdFilter(1));
            assertEquals("Wrong number of submissions retrieved", 2, submissions.length);

            Set ids = new HashSet();

            for (int i = 0; i < submissions.length; ++i) {
                ids.add(new Long(submissions[i].getId()));
            }

            assertTrue("Wrong submission retrieved", ids.contains(new Long(1)));
            assertTrue("Wrong submission retrieved", ids.contains(new Long(4)));

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadSubmissions"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    /**
     * Testss searchSubmissions method with resource id filter.
     *
     * @throws Exception if any error occurs
     */
    public void testSearchSubmissionsByResourceId() throws Exception {
        try {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
            DeliverableTestHelper.executeBatch(INIT_DB_SQL);

            Submission[] submissions = persistenceUploadManager.searchSubmissions(SubmissionFilterBuilder
                    .createResourceIdFilter(2));
            assertEquals("Wrong number of submissions retrieved", 2, submissions.length);

            Set ids = new HashSet();

            for (int i = 0; i < submissions.length; ++i) {
                ids.add(new Long(submissions[i].getId()));
            }

            assertTrue("Wrong submission retrieved", ids.contains(new Long(2)));
            assertTrue("Wrong submission retrieved", ids.contains(new Long(3)));

            assertTrue("Persistence method not called correctly", SqlUploadPersistence.getLastCalled().startsWith(
                    "loadSubmissions"));
        } finally {
            DeliverableTestHelper.executeBatch(CLEAR_DB_SQL);
        }
    }

    // --- modified

    /**
     * Set the behaviour of createSubmissionType. Set the 1th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testCreateSubmissionType_Null1() {
        try {
            persistenceUploadManager.createSubmissionType(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createSubmissionType. Set the 2th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testCreateSubmissionType_Null2() {
        try {
            persistenceUploadManager.createSubmissionType(new SubmissionType(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of createSubmissionType. Set the SubmissionType's id to 10. Since SubmissionType's id
     * should be UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testCreateSubmissionType_Invalid1() {
        try {
            persistenceUploadManager.createSubmissionType(new SubmissionType(10), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of createSubmissionType. Give some valid parameters. See if the method can successfully add
     * the SubmissionType object to persistence No exception should be thrown.
     */
    public void testCreateSubmissionType_Accuracy1() {
        try {
            // Get a submissionType.
            SubmissionType submissionType = DeliverableTestHelper.getValidToPersistSubmissionType();

            // Call the method.
            persistenceUploadManager.createSubmissionType(submissionType, "user");

            // Check creationUser field.
            assertEquals("createSubmissionType doesn't work properly", "user", persistence.getSubmissionType()
                    .getCreationUser());

            // Check modificationUser field.
            assertEquals("createSubmissionType doesn't work properly", "user", persistence.getSubmissionType()
                    .getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("createSubmissionType doesn't work properly", persistence.getSubmissionType()
                    .getCreationTimestamp());
            assertNotNull("createSubmissionType doesn't work properly", persistence.getSubmissionType()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmissionType. Set the 1th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testUpdateSubmissionType_Null1() {
        try {
            persistenceUploadManager.updateSubmissionType(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmissionType. Set the 2th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testUpdateSubmissionType_Null2() {
        try {
            persistenceUploadManager.updateSubmissionType(new SubmissionType(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmissionType. Set the SubmissionType's id to UNSET_ID. Since SubmissionType's
     * id should not be UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testUpdateSubmissionType_Invalid1() {
        try {
            persistenceUploadManager.updateSubmissionType(new SubmissionType(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of updateSubmissionType. Give some valid parameters. See if the method can successfully add
     * the SubmissionType object to persistence No exception should be thrown.
     */
    public void testUpdateSubmissionType_Accuracy1() {
        try {
            // Get a submissionType.
            SubmissionType submissionType = DeliverableTestHelper.getValidToPersistSubmissionType();

            // Create a submissionType first.
            persistenceUploadManager.createSubmissionType(submissionType, "CreationUser");

            // Call the method.
            persistenceUploadManager.updateSubmissionType(submissionType, "ModificationUser");

            // Check creationUser field.
            assertEquals("updateSubmissionType doesn't work properly", "CreationUser", persistence
                    .getSubmissionType().getCreationUser());

            // Check modificationUser field.
            assertEquals("updateSubmissionType doesn't work properly", "ModificationUser", persistence
                    .getSubmissionType().getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("updateSubmissionType doesn't work properly", persistence.getSubmissionType()
                    .getCreationTimestamp());
            assertNotNull("updateSubmissionType doesn't work properly", persistence.getSubmissionType()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmissionType. Set the 1th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testRemoveSubmissionType_Null1() {
        try {
            persistenceUploadManager.removeSubmissionType(null, "user");
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmissionType. Set the 2th paramter to null. IllegalArgumentException should be
     * thrown.
     */
    public void testRemoveSubmissionType_Null2() {
        try {
            persistenceUploadManager.removeSubmissionType(new SubmissionType(), null);
            fail("IllegalArgumentException should be thrown because of null parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of null parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmissionType. Set the SubmissionType's id to UNSET_ID. Since SubmissionType's
     * id should not be UNSET_ID, IllegalArgumentException should be thrown.
     */
    public void testRemoveSubmissionType_Invalid1() {
        try {
            persistenceUploadManager.removeSubmissionType(new SubmissionType(), "user");
            fail("IllegalArgumentException should be thrown because of this invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of invalid parameter.");
        }
    }

    /**
     * Set the behaviour of removeSubmissionType. Give some valid parameters. See if the method can successfully add
     * the SubmissionType object to persistence No exception should be thrown.
     */
    public void testRemoveSubmissionType_Accuracy1() {
        try {
            // Get a submissionType.
            SubmissionType submissionType = DeliverableTestHelper.getValidToPersistSubmissionType();

            // Create a submissionType first.
            persistenceUploadManager.createSubmissionType(submissionType, "CreationUser");

            // Call the method.
            persistenceUploadManager.removeSubmissionType(submissionType, "ModificationUser");

            // Check creationUser field.
            assertEquals("removeSubmissionType doesn't work properly", "CreationUser", persistence
                    .getSubmissionType().getCreationUser());

            // Check modificationUser field.
            assertEquals("removeSubmissionType doesn't work properly", "ModificationUser", persistence
                    .getSubmissionType().getModificationUser());

            // Check modificationUser field. Since time is changing all the time, we assert it
            // to be not null is OK for test.
            assertNotNull("removeSubmissionType doesn't work properly", persistence.getSubmissionType()
                    .getCreationTimestamp());
            assertNotNull("removeSubmissionType doesn't work properly", persistence.getSubmissionType()
                    .getModificationTimestamp());

        } catch (UploadPersistenceException e) {
            fail("UploadPersistenceException should not be thrown because of valid parameter.");
        }
    }

    /**
     * Since the getAllSubmissionTypees totally invoke method in persistence, We just use MOCK persistence class to
     * prove it is invoked properly.
     *
     * @throws Exception if any error occurs
     */
    public void testGetAllSubmissionTypees_Accuracy() throws Exception {

        SubmissionType[] submissionTypes = persistenceUploadManager.getAllSubmissionTypes();

        assertEquals("getAllUploadStatus doesn't work correctly, three submissionType were expected .", 3,
                submissionTypes.length);

        assertEquals("submissionType has invalid id", 1, submissionTypes[0].getId());
        assertEquals("submissionType has invalid id", 2, submissionTypes[1].getId());
        assertEquals("submissionType has invalid id", 3, submissionTypes[2].getId());
    }
}
