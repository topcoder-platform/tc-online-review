/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * <p>
 * This class represents an uploaded file stored in the local file system. Some file information and the file contents
 * (byte data) can be retrieved from the locally stored file. Every such instance is identified by a file id. It is
 * not necessary to have the file name as the file id however.
 * </p>
 *
 * <p>
 * Instances of this class are created by the concrete <code>LocalFileUpload</code> and <code>RemoteFileUpload</code>
 * classes (because the files are downloaded to the local file system before access).
 * </p>
 *
 * <p>
 * The class is Serializable and the internal file object is also Serializable.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>This class is thread-safe by being immutable.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class LocalUploadedFile extends UploadedFile {
    /**
     * <p>
     * Represents the local file instance. It cannot be null.
     * </p>
     */
    private final File file;

    /**
     * <p>
     * Creates a new instance of <code>LocalUploadedFile</code> class. Package-private constructor to prevent
     * instantiation outside the package.
     * </p>
     *
     * @param file the local file instance.
     * @param contentType the content type of the file.
     *
     * @throws IllegalArgumentException if file is null.
     */
    LocalUploadedFile(File file, String contentType) {
        super(((File) FileUploadHelper.validateNotNull(file, "file")).getName(), contentType);

        this.file = file;
    }

    /**
     * <p>
     * Creates a new instance of <code>LocalUploadedFile</code> class. Package-private constructor to prevent
     * instantiation outside the package.
     * </p>
     *
     * @param file the local file instance.
     * @param fileId the unique file id.
     * @param contentType the content type of the file.
     *
     * @throws IllegalArgumentException if file is null, or fileId is null or empty string.
     */
    LocalUploadedFile(File file, String fileId, String contentType) {
        super(FileUploadHelper.validateString(fileId, "fileId"), contentType);

        FileUploadHelper.validateNotNull(file, "file");
        this.file = file;
    }

    /**
     * <p>
     * Gets the remote file name of the uploaded file.
     * </p>
     *
     * @return the remote file name of the uploaded file.
     *
     * @throws FileDoesNotExistException if the file does not exist.
     */
    public String getRemoteFileName() throws FileDoesNotExistException {
        if (!file.exists()) {
            throw new FileDoesNotExistException(getFileId());
        }

        return FileUpload.getRemoteFileName(file.getName());
    }

    /**
     * <p>
     * Gets the size of the file, in bytes.
     * </p>
     *
     * @return the size of the file, in bytes
     *
     * @throws FileDoesNotExistException if the file does not exist.
     */
    public long getSize() throws FileDoesNotExistException {
        if (!file.exists()) {
            throw new FileDoesNotExistException(getFileId());
        }

        return file.length();
    }

    /**
     * <p>
     * Gets the input stream pointing to the start of file contents. User is required to close the stream after use.
     * </p>
     *
     * @return the underlying input stream.
     *
     * @throws FileDoesNotExistException if the file does not exist.
     * @throws IOException if the stream cannot be opened.
     */
    public InputStream getInputStream() throws FileDoesNotExistException, IOException {
        if (!file.exists()) {
            throw new FileDoesNotExistException(getFileId());
        }

        return new FileInputStream(file);
    }
}
