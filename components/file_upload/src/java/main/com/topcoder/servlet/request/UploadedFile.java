/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;


/**
 * <p>
 * This class abstracts an uploaded file stored in the persistence. Some file information and the file contents (byte
 * data) can be retrieved. Implementations should define the way to retrieve file information, while assuring the
 * <code>UploadedFile</code> object is serializable.
 * </p>
 *
 * <p>
 * It is assumed that the uploaded file will not be modified externally in the persistence. Users can safely assume
 * that the data in the returned instance is valid and accurate (compared to the persistence), except when the file is
 * removed.
 * </p>
 *
 * <p>
 * Normally, instances of this class are created by the concrete <code>FileUpload</code> class. Implementations are
 * recommended to have non-public constructors.
 * </p>
 *
 * <p>
 * This class implements the <code>Serializable</code> interface so that it can be stored as session data in applicable
 * scenarios. All subclasses implement the <code>Serializable</code> interface indirectly.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>This class is thread-safe by being immutable. Implementations are required to be thread-safe
 * too.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public abstract class UploadedFile implements Serializable {
    /**
     * <p>
     * Represents the unique id that identifies the uploaded file. It can be null or empty string (if not available).
     * </p>
     */
    private final String fileId;

    /**
     * <p>
     * Represents the content type of the file. It can be null or empty string (if not available).
     * </p>
     */
    private final String contentType;

    /**
     * <p>
     * Creates a new instance of <code>UploadedFile</code> class with the specified file id and content type. They can
     * be null or empty string if not available.
     * </p>
     *
     * @param fileId the unique id that identifies the uploaded file.
     * @param contentType the content type of the file.
     */
    protected UploadedFile(String fileId, String contentType) {
        this.fileId = fileId;
        this.contentType = contentType;
    }

    /**
     * <p>
     * Gets the unique id that identifies the uploaded file. It can be null or empty string (if not available).
     * </p>
     *
     * @return the unique file id that identifies the uploaded file.
     */
    public String getFileId() {
        return this.fileId;
    }

    /**
     * <p>
     * Gets the content type of the file. It can be null or empty string (if not available).
     * </p>
     *
     * @return the content type of the file.
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * <p>
     * Gets the remote file name of the uploaded file.
     * </p>
     *
     * @return the remote file name of the uploaded file.
     *
     * @throws PersistenceException if any error occurs in retrieving the data from persistence.
     * @throws FileDoesNotExistException if the file does not exist.
     */
    public abstract String getRemoteFileName() throws PersistenceException, FileDoesNotExistException;

    /**
     * <p>
     * Gets the size of the file, in bytes.
     * </p>
     *
     * @return the size of the file, in bytes.
     *
     * @throws PersistenceException if any error occurs in retrieving the data from persistence.
     * @throws FileDoesNotExistException if the file does not exist.
     */
    public abstract long getSize() throws PersistenceException, FileDoesNotExistException;

    /**
     * <p>
     * Gets the input stream pointing to the start of file contents. User is required to close the stream after use.
     * </p>
     *
     * @return the underlying input stream.
     *
     * @throws PersistenceException if any error occurs in retrieving the data from persistence.
     * @throws FileDoesNotExistException if the file does not exist.
     * @throws IOException if the stream cannot be opened.
     */
    public abstract InputStream getInputStream() throws PersistenceException, FileDoesNotExistException, IOException;
}
