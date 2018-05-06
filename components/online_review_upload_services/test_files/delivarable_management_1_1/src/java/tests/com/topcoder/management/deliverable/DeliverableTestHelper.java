/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;

/**
 * Helper class for test.
 *
 * @author singlewood
 * @version 1.1
 */
public class DeliverableTestHelper {
    /**
     * Represents namespace containing db connection factory configurations.
     */
    private static final String DB_CONNECTION_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * private constructor.
     */
    private DeliverableTestHelper() {
    }

    /**
     * Loads necessary configurations into ConfigManager.
     *
     * @param file the file contains configurations.
     *
     * @throws Exception pass to JUnit.
     */
    public static void loadConfig(String file) throws Exception {
        ConfigManager.getInstance().add(file);
    }

    /**
     * Unloads all configurations. All namespaces in ConfigManager are removed.
     *
     * @throws Exception pass to JUnit.
     */
    public static void unloadConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {
            cm.removeNamespace(it.next().toString());
        }
    }

    /**
     * Executes a sql batch contains in a file.
     *
     * @param file the file contains the sql statements.
     *
     * @throws Exception pass to JUnit.
     */
    public static void executeBatch(String file) throws Exception {
        Connection connection = null;
        Statement statement = null;
        try {
            // Gets db connection
            connection = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE).createConnection();
            statement = connection.createStatement();

            // Gets sql statements and add to statement
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = in.readLine()) != null) {
                if (line.trim().length() != 0) {
                    statement.addBatch(line);
                }
            }

            statement.executeBatch();
        } finally {
            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Set the AuditedDeliverableStructure field (creationUser, creationTimestamp, modificationUser,
     * modificationTimestamp) with valid values.
     *
     * @param obj the object to set
     */
    public static void setAuditedDeliverableStructureField(AuditedDeliverableStructure obj) {
        Date date = new Date();
        obj.setCreationUser("CreationUser");
        obj.setCreationTimestamp(date);
        obj.setModificationUser("ModificationUser");
        obj.setModificationTimestamp(date);
    }

    /**
     * Set the NamedDeliverableStructure field (creationUser, creationTimestamp, modificationUser,
     * modificationTimestamp) with valid values.
     *
     * @param obj the object to set
     */
    public static void setNamedDeliverableStructureField(NamedDeliverableStructure obj) {
        setAuditedDeliverableStructureField(obj);
        obj.setDescription("desp");
        obj.setName("name");
    }

    /**
     * Gets a Upload instance which is valid to persist. Note that the id field is not set.
     *
     * @return a Upload object
     */
    public static Upload getValidToPersistUpload() {
        Upload upload = new Upload();
        setAuditedDeliverableStructureField(upload);
        upload.setOwner(1);
        upload.setParameter("11");
        upload.setProject(1);

        UploadType uploadType = getValidToPersistUploadType();
        uploadType.setId(1);
        UploadStatus uploadStatus = getValidToPersistUploadStatus();
        uploadStatus.setId(1);
        upload.setUploadStatus(uploadStatus);
        upload.setUploadType(uploadType);

        return upload;
    }

    /**
     * Gets a UploadType instance which is valid to persist. Note that the id field is not set.
     *
     * @return a UploadType object
     */
    public static UploadType getValidToPersistUploadType() {
        UploadType uploadType = new UploadType();
        setNamedDeliverableStructureField(uploadType);
        return uploadType;
    }

    /**
     * Gets a UploadStatus instance which is valid to persist. Note that the id field is not set.
     *
     * @return a UploadStatus object
     */
    public static UploadStatus getValidToPersistUploadStatus() {
        UploadStatus uploadStatus = new UploadStatus();
        setNamedDeliverableStructureField(uploadStatus);
        return uploadStatus;
    }

    /**
     * Gets a Submission instance which is valid to persist. Note that the id field is not set.
     *
     * @return a Submission object
     */
    public static Submission getValidToPersistSubmission() {
        Submission submission = new Submission();
        setAuditedDeliverableStructureField(submission);

        Upload upload = getValidToPersistUpload();
        upload.setId(1);
        submission.setUpload(upload);

        SubmissionStatus submissionStatus = getValidToPersistSubmissionStatus();
        submissionStatus.setId(1);
        submission.setSubmissionStatus(submissionStatus);

        SubmissionType submissionType = getValidToPersistSubmissionType();
        submissionType.setId(1);
        submission.setSubmissionType(submissionType);

        return submission;
    }

    /**
     * Gets a SubmissionStatus instance which is valid to persist. Note that the id field is not set.
     *
     * @return a SubmissionStatus object
     */
    public static SubmissionStatus getValidToPersistSubmissionStatus() {
        SubmissionStatus submissionStatus = new SubmissionStatus();
        setNamedDeliverableStructureField(submissionStatus);
        return submissionStatus;
    }

    /**
     * Gets a SubmissionType instance which is valid to persist. Note that the id field is not set.
     *
     * @return a SubmissionType object
     */
    public static SubmissionType getValidToPersistSubmissionType() {
        SubmissionType submissionType = new SubmissionType();
        setNamedDeliverableStructureField(submissionType);
        return submissionType;
    }
}
