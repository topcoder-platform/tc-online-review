/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.search.builder.filter.Filter;

/**
 * Mock implementation of UploadManager.
 *
 * @author assistant
 * @version 1.0
 */
public class MockUploadManager implements UploadManager {

    /**
     * <p>A <code>Throwable</code> representing the exception to be thrown from any method of the mock class.</p>
     */
    private static Throwable globalException = null;

    /**
     * Create submission.
     * @param submission the submission to create
     * @param operator the operator
     */
    public void createSubmission(Submission submission, String operator) {
    }

    /**
     * Create upload.
     * @param upload the upload
     * @param operator the operator
     */
    public void createUpload(Upload upload, String operator) {
    }

    /**
     * Get all submission statuses.
     * @return all the submission statuses.
     */
    public SubmissionStatus[] getAllSubmissionStatuses() {
        return null;
    }

    /**
     * Get all upload statuses.
     * @return all upload statuses
     */
    public UploadStatus[] getAllUploadStatuses() {
        return null;
    }

    /**
     * Get all types.
     * @return all types
     */
    public UploadType[] getAllUploadTypes() {
        return null;
    }

    /**
     * Get submission.
     * @param id the id
     * @return the submission
     */
    public Submission getSubmission(long id) throws UploadPersistenceException {
        if (MockUploadManager.globalException != null) {
            if (MockUploadManager.globalException instanceof UploadPersistenceException) {
                throw (UploadPersistenceException) MockUploadManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly",
                                           MockUploadManager.globalException);
            }
        }
        Submission sub = new MockSubmission();
        sub.setId(1);
        Upload upload = new MockUpload();
        upload.setId(1);
        upload.setOwner(2);
        sub.setUpload(upload);
        return sub;
    }

    /**
     * Get upload.
     * @param id the id
     * @return the upload
     */
    public Upload getUpload(long id) {
        return null;
    }

    /**
     * Search submission.
     * @param filter the filter
     * @return submissions found
     */
    public Submission[] searchSubmissions(Filter filter) {
        return null;
    }

    /**
     * Search uploads.
     * @param filter the filter
     * @return the uploads
     */
    public Upload[] searchUploads(Filter filter) {
        return null;
    }

    /**
     * Update submission.
     * @param submission the submission
     * @param operator the operator
     */
    public void updateSubmission(Submission submission, String operator) {

    }

    /**
     * Update upload.
     * @param upload the upload
     * @param operator the operator
     */
    public void updateUpload(Upload upload, String operator) {

    }

    public void createSubmissionStatus(SubmissionStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void createUploadStatus(UploadStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void createUploadType(UploadType arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeSubmission(Submission arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeSubmissionStatus(SubmissionStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeUpload(Upload arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeUploadStatus(UploadStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void removeUploadType(UploadType arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void updateSubmissionStatus(SubmissionStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void updateUploadStatus(UploadStatus arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void updateUploadType(UploadType arg0, String arg1) throws UploadPersistenceException {
        // TODO Auto-generated method stub
        
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockUploadManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockLog</code> so all collected method arguments, configured method results and
     * exceptions are lost.</p>
     */
    public static void releaseState() {
        MockUploadManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockResourceManager</code> class.</p>
     */
    public static void init() {
    }

}
