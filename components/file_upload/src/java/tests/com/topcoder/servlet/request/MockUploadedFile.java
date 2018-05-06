/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.InputStream;


/**
 * <p>
 * A mock class which extends the <code>UploadedFile</code> abstract class for testing.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class MockUploadedFile extends UploadedFile {
    /**
     * <p>
     * A mock constructor, which simply call the super's constructor.
     * </p>
     *
     * @param fileId the unique id that identifies the uploaded file.
     * @param contentType the content type of the file.
     */
    public MockUploadedFile(String fileId, String contentType) {
        super(fileId, contentType);
    }

    /**
     * <p>
     * A mock method, do nothing here.
     * </p>
     *
     * @return always null.
     */
    public String getRemoteFileName() {
        return null;
    }

    /**
     * <p>
     * A mock method, do nothing here.
     * </p>
     *
     * @return always return 0.
     */
    public long getSize() {
        return 0;
    }

    /**
     * <p>
     * A mock method, do nothing here.
     * </p>
     *
     * @return always null.
     */
    public InputStream getInputStream() {
        return null;
    }
}
