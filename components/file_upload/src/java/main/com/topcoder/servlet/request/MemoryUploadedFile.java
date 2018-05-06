/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 * <p>
 * This class represents an uploaded file stored in the memory. Some file information and the file contents (byte data)
 * can be retrieved from the memory. No file id is associated with this instance.
 * </p>
 *
 * <p>
 * Instances of this class are created by the concrete <code>MemoryFileUpload</code> class.
 * </p>
 *
 * <p>
 * The class is Serializable and the internal byte array is also Serializable.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>This class is thread-safe by being immutable.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class MemoryUploadedFile extends UploadedFile {
    /**
     * <p>
     * Represents the remote file name of the uploaded file. It cannot be null or empty string.
     * </p>
     */
    private final String remoteFileName;

    /**
     * <p>
     * Represents the byte contents of the file. It cannot be null.
     * </p>
     */
    private final byte[] data;

    /**
     * <p>
     * Creates a new instance of <code>MemoryUploadedFile</code> class. Package-private constructor to prevent
     * instantiation outside the package.
     * </p>
     *
     * @param data the byte contents of the file.
     * @param remoteFileName the remote file name of the uploaded file.
     * @param contentType the content type of the file.
     *
     * @throws IllegalArgumentException if data is null, or remoteFileName is null or empty string.
     */
    MemoryUploadedFile(byte[] data, String remoteFileName, String contentType) {
        super(null, contentType);
        FileUploadHelper.validateNotNull(data, "data");
        FileUploadHelper.validateString(remoteFileName, "remoteFileName");

        // No need to make a shallow copy of the array because of performance issues
        this.data = data;
        this.remoteFileName = remoteFileName;
    }

    /**
     * <p>
     * Gets the remote file name of the uploaded file.
     * </p>
     *
     * @return the remote file name of the uploaded file.
     */
    public String getRemoteFileName() {
        return this.remoteFileName;
    }

    /**
     * <p>
     * Gets the size of the file, in bytes.
     * </p>
     *
     * @return the size of the file, in bytes.
     */
    public long getSize() {
        return data.length;
    }

    /**
     * <p>
     * Gets the input stream pointing to the start of file contents. User is required to close the stream after use.
     * </p>
     *
     * @return the underlying input stream.
     */
    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }
}
