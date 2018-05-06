/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.search.builder.filter.Filter;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockUploadManager implements UploadManager {

    public void createUpload(Upload upload, String operator) {
    }

    public void updateUpload(Upload upload, String operator) {
    }

    public Upload getUpload(long id) {
        return null;
    }

    public Upload[] searchUploads(Filter filter) {
        return null;
    }

    public UploadType[] getAllUploadTypes() {
        return null;
    }

    public UploadStatus[] getAllUploadStatuses() {
        return null;
    }

    public void createSubmission(Submission submission, String operator) {
    }

    public void updateSubmission(Submission submission, String operator) {
    }

    public Submission getSubmission(long id) {
        if (id == 12345) {
            return new MockSubmission();
        }
        return null;
    }

    public Submission[] searchSubmissions(Filter filter) {
        return null;
    }

    public SubmissionStatus[] getAllSubmissionStatuses() {
        return null;
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
}
