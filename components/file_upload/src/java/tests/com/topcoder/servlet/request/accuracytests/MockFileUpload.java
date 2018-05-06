/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.accuracytests;

import com.topcoder.servlet.request.ConfigurationException;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.RequestParser;
import com.topcoder.servlet.request.UploadedFile;

import javax.servlet.ServletRequest;


/**
 * <p>
 * A mock class which extends the <code>FileUpload</code> abstract class for testing.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public class MockFileUpload extends FileUpload {
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

    /**
     * <p>
     * A mock method, which simply call the super's method.
     * </p>
     *
     * @param remoteFileName the remote file name.
     *
     * @return the uploaded file.
     */
    public static String superGetUniqueFileName(String remoteFileName) {
        return getUniqueFileName(remoteFileName);
    }

    /**
     * <p>
     * A mock method, which simply call the super's method.
     * </p>
     *
     * @param uniqueFileName the unique file name.
     *
     * @return the remote file name.
     */
    public static String superGetRemoteFileName(String uniqueFileName) {
        return getRemoteFileName(uniqueFileName);
    }
}
