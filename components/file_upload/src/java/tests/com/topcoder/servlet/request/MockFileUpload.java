/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import javax.servlet.ServletRequest;


/**
 * <p>
 * A mock class which extends the <code>FileUpload</code> abstract class for testing.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class MockFileUpload extends FileUpload {
    /** A flag represents whether the method of uploadFiles(ServletRequest, RequestParser) is called. */
    private boolean isUploadFilesCalledFlag = false;

    /** A flag represents whether the method of getUploadedFile(String, boolean) is called. */
    private boolean isGetUploadedFileFlag = false;

    /**
     * <p>
     * A mock constructor, which simply call the super's constructor.
     * </p>
     *
     * @param namespace the configuration namespace to use.
     *
     * @throws IllegalArgumentException if namespace is null or empty string.
     * @throws ConfigurationException if any configuration error occurs.
     */
    public MockFileUpload(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Checks whether the method of uploadFiles(ServletRequest, RequestParser) is called.
     *
     * @return whether the method of uploadFiles(ServletRequest, RequestParser) is called.
     */
    public boolean isUploadFilesCalled() {
        return this.isUploadFilesCalledFlag;
    }

    /**
     * Checks whether the method of getUploadedFile(String, boolean) is called.
     *
     * @return whether the method of getUploadedFile(String, boolean) is called.
     */
    public boolean isGetUploadedFileCalled() {
        return this.isGetUploadedFileFlag;
    }

    /**
     * <p>
     * A mock method, which simply call the super's method.
     * </p>
     *
     * @param request the servlet request to be parsed.
     * @param parser the parser to use.
     *
     * @return the <code>FileUploadResult</code> containing uploaded files and parameters information.
     */
    public FileUploadResult uploadFiles(ServletRequest request, RequestParser parser) {
        isUploadFilesCalledFlag = true;

        return null;
    }

    /**
     * <p>
     * A mock method, which simply call the super's method.
     * </p>
     *
     * @param fileId the id of the file to retrieve.
     * @param refresh whether to refresh the cached file copy.
     *
     * @return the uploaded file.
     */
    public UploadedFile getUploadedFile(String fileId, boolean refresh) {
        isGetUploadedFileFlag = true;

        return null;
    }

    /**
     * <p>
     * A mock method, do nothing here.
     * </p>
     *
     * @param fileId the id of the file to remove.
     */
    public void removeUploadedFile(String fileId) {
        // do nothing
    }

    /**
     * <p>
     * A mock method, do nothing here.
     * </p>
     */
    public void disconnect() {
        // do nothing
    }
}
