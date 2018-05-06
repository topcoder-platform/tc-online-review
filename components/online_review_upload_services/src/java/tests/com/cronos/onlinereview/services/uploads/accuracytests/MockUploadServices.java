/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.services.uploads.accuracytests;

import java.text.MessageFormat;

import com.cronos.onlinereview.services.uploads.InvalidProjectException;
import com.cronos.onlinereview.services.uploads.InvalidProjectPhaseException;
import com.cronos.onlinereview.services.uploads.InvalidSubmissionException;
import com.cronos.onlinereview.services.uploads.InvalidSubmissionStatusException;
import com.cronos.onlinereview.services.uploads.InvalidUserException;
import com.cronos.onlinereview.services.uploads.PersistenceException;
import com.cronos.onlinereview.services.uploads.UploadServices;
import com.cronos.onlinereview.services.uploads.UploadServicesException;

/**
 * <p>
 * Mock implementation for testing.
 * </p>
 *
 * @author kshatriyan, TCSDEVELOPER
 * @since 1.0
 * @version 1.1
 */
public class MockUploadServices implements UploadServices {

    /**
     * Used for testing.
     */
    private String setSubmissionStatus = "";

    /**
     * Used for testing.
     */
    private String uploadFinalFix = "";

    /**
     * Used for testing.
     */
    private String uploadSubmission = "";

    /**
     * Used for testing.
     */
    private String uploadTestCases = "";

    /**
     * Record the project id.
     *
     * @since 1.1
     */
    private static long projectId;

    /**
     * Record the user id.
     *
     * @since 1.1
     */
    private static long userId;

    /**
     * Record the file name.
     *
     * @since 1.1
     */
    private static String filename;

    /**
     * Get the project id.
     *
     * @return the project Id.
     */
    public static long getProjectId() {
        return projectId;
    }

    /**
     * Get the user id.
     *
     * @return the user Id.
     */
    public static long getUserId() {
        return userId;
    }

    /**
     * Get the file name.
     *
     * @return the file name.
     */
    public static String getFilename() {
        return filename;
    }

    /**
     * Mock implementation. For Testing.
     */
    public MockUploadServices() {
        projectId = 0;
        userId = 0;
        filename = null;
    }

    /**
     * Mock implementation. For Testing.
     *
     * @param submissionId
     * @param submissionStatusId
     * @param operator
     * @throws InvalidSubmissionException
     *             not thrown
     * @throws InvalidSubmissionStatusException
     *             not thrown
     * @throws PersistenceException
     *             not thrown
     */
    public void setSubmissionStatus(long submissionId, long submissionStatusId, String operator)
        throws InvalidSubmissionException, InvalidSubmissionStatusException, PersistenceException {
        setSubmissionStatus = MessageFormat.format("{0}:{1}:{2}", new Object[] { submissionId, submissionStatusId,
            operator });
    }

    /**
     * Mock implementation. For Testing.
     *
     * @param projectId
     *            projectId
     * @param userId
     *            userId
     * @param filename
     *            filename
     * @return 1
     * @throws UploadServicesException
     *             not thrown
     */
    public long uploadFinalFix(long projectId, long userId, String filename) throws UploadServicesException {
        uploadFinalFix = MessageFormat.format("{0}:{1}:{2}", new Object[] { projectId, userId, filename });
        return 1;
    }

    /**
     * Mock implementation. For Testing.
     *
     * @param projectId
     * @param userId
     * @param filename
     * @return 2
     * @throws UploadServicesException
     */
    public long uploadSubmission(long projectId, long userId, String filename) throws UploadServicesException {
        uploadSubmission = MessageFormat.format("{0}:{1}:{2}", new Object[] { projectId, userId, filename });
        return 2;
    }

    /**
     * Mock implementation. For Testing.
     *
     * @param projectId
     * @param userId
     * @param filename
     * @return 3
     * @throws UploadServicesException
     *             not thrown
     */
    public long uploadTestCases(long projectId, long userId, String filename) throws UploadServicesException {
        uploadTestCases = MessageFormat.format("{0}:{1}:{2}", new Object[] { projectId, userId, filename });
        return 3;
    }

    /**
     * @return the setSubmissionStatus
     */
    public String getSetSubmissionStatus() {
        return setSubmissionStatus;
    }

    /**
     * @return the uploadFinalFix
     */
    public String getUploadFinalFix() {
        return uploadFinalFix;
    }

    /**
     * @return the uploadSubmission
     */
    public String getUploadSubmission() {
        return uploadSubmission;
    }

    /**
     * @return the uploadTestCases
     */
    public String getUploadTestCases() {
        return uploadTestCases;
    }

    /**
     * Mock implementation.
     *
     * @param projectId
     *            the project id.
     * @param userId
     *            the user id.
     * @since 1.1
     */
    public long addSubmitter(long projectId, long userId) throws InvalidProjectException, InvalidUserException,
        UploadServicesException, InvalidProjectPhaseException {
        MockUploadServices.projectId = projectId;
        MockUploadServices.userId = userId;
        return 3;
    }

    /**
     * Mock implementation.
     *
     * @param projectId
     *            the project id.
     * @param userId
     *            the user id.
     * @param filename
     *            the file name.
     *
     * @throws UploadServicesException
     *             never.
     *
     * @since 1.1
     */
    public long uploadSpecification(long projectId, long userId, String filename) throws UploadServicesException {
        MockUploadServices.projectId = projectId;
        MockUploadServices.userId = userId;
        MockUploadServices.filename = filename;
        return 2;
    }
}