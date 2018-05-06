/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import com.topcoder.util.generator.guid.Generator;
import com.topcoder.util.generator.guid.UUIDType;
import com.topcoder.util.generator.guid.UUIDUtility;

import javax.servlet.ServletRequest;


/**
 * <p>
 * A class abstracting the parsing of <code>ServletRequest</code> to produce the <code>FileUploadResult</code>. The
 * information parsed in the <code>ServletRequest</code> includes uploaded files and form parameters. Implementations
 * should define the strategy to store the uploaded files to persistence (e.g. in memory or some kind of persistent
 * storage).
 * </p>
 *
 * <p>
 * The uploaded file can be retrieved later (e.g. after restarting the application) using a file id. The file id should
 * be unique to every uploaded file. Implementations should also define how to assign this file id if applicable.
 * </p>
 *
 * <p>
 * This class should be initialized from a configuration namespace which should be preloaded. It will look for the
 * following properties:
 *
 * <ul>
 * <li>
 * single_file_limit (optional) - the single file size limit for each uploaded file in the request, in bytes. A value
 * of -1 indicates no limit. Default value is -1.
 * </li>
 * <li>
 * total_file_limit (optional) - the total file size limit for all uploaded files in the request, in bytes. A value of
 * -1 indicates no limit. Default value is -1.
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is thread-safe by being immutable. Its implementations should also be thread-safe
 * (to be used in the servlet environment).
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public abstract class FileUpload {
    /**
     * <p>
     * The property key specifying the single file size limit for each uploaded file in the request. The property value
     * should be a long integer. This property is optional.
     * </p>
     */
    public static final String SINGLE_FILE_LIMIT_PROPERTY = "single_file_limit";

    /**
     * <p>
     * The property key specifying the total file size limit for all uploaded files in the request. The property value
     * should be a long integer. This property is optional.
     * </p>
     */
    public static final String TOTAL_FILE_LIMIT_PROPERTY = "total_file_limit";

    /**
     * <p>
     * The GUID Generator to generate unique ids (for filenames).
     * </p>
     */
    private static final Generator GENERATOR = UUIDUtility.getGenerator(UUIDType.TYPEINT32);

    /**
     * <p>
     * Represents the single file size limit for each uploaded file in the request, in bytes. The value range should be
     * -1 or greater. A value of -1 indicates no limit.
     * </p>
     */
    private final long singleFileLimit;

    /**
     * <p>
     * Represents the total file size limit for all uploaded files in the request, in bytes. The value range should be
     * -1 or greater. A value of -1 indicates no limit.
     * </p>
     */
    private final long totalFileLimit;

    /**
     * <p>
     * Creates a new instance of <code>FileUpload</code> class to load configuration from the specified namespace. The
     * namespace should be preloaded.
     * </p>
     *
     * <p>
     * This constructor will look for the following properties:
     *
     * <ul>
     * <li>
     * single_file_limit (optional) - the single file size limit for each uploaded file in the request, in bytes. A
     * value of -1 indicates no limit. Default value is -1. Any value under -1 will be regarded as invalid.
     * </li>
     * <li>
     * total_file_limit (optional) - the total file size limit for all uploaded files in the request, in bytes. A value
     * of -1 indicates no limit. Default value is -1. Any value under -1 will be regarded as invalid.
     * </li>
     * </ul>
     * </p>
     *
     * @param namespace the configuration namespace to use.
     *
     * @throws IllegalArgumentException if namespace is null or empty string.
     * @throws ConfigurationException if any configuration error occurs.
     */
    protected FileUpload(String namespace) throws ConfigurationException {
        FileUploadHelper.validateString(namespace, "namespace");

        // get the property values
        this.singleFileLimit = FileUploadHelper.getLongPropertyValue(namespace, SINGLE_FILE_LIMIT_PROPERTY);
        this.totalFileLimit = FileUploadHelper.getLongPropertyValue(namespace, TOTAL_FILE_LIMIT_PROPERTY);

        // validate the property values
        if (this.singleFileLimit < -1) {
            throw new ConfigurationException("The singleFileLimit should not be below -1.");
        }

        if (this.totalFileLimit < -1) {
            throw new ConfigurationException("The totalFileLimit should not be below -1.");
        }
    }

    /**
     * <p>
     * Gets the unique file name with the specified remote file name. Subclasses can use it to identify each uploaded
     * file if necessary. Using this method subclasses can support uploads with the same remote file name in a single
     * request or different requests.
     * </p>
     *
     * @param remoteFileName the remote file name.
     *
     * @return the unique file name.
     *
     * @throws IllegalArgumentException if remoteFileName is null or empty string.
     */
    protected static String getUniqueFileName(String remoteFileName) {
        FileUploadHelper.validateString(remoteFileName, "remoteFileName");

        return GENERATOR.getNextUUID().toString() + "_" + remoteFileName;
    }

    /**
     * <p>
     * Gets back the original remote file name from the unique file name. The unique file name should be the one
     * returned from getUniqueFileName() method.
     * </p>
     *
     * @param uniqueFileName the unique file name.
     *
     * @return the remote file name.
     *
     * @throws IllegalArgumentException if uniqueFileName is null or empty string, or does not contain "_" in the
     *         string.
     */
    protected static String getRemoteFileName(String uniqueFileName) {
        FileUploadHelper.validateString(uniqueFileName, "uniqueFileName");

        int index = uniqueFileName.indexOf("_");

        if (index < 0) {
            throw new IllegalArgumentException("The uniqueFileName: " + uniqueFileName
                + " does not contain \"_\" in the string.");
        }

        return uniqueFileName.substring(index + 1);
    }

    /**
     * <p>
     * Gets the single file size limit for each uploaded file in the request, in bytes. A value of -1 indicates no
     * limit.
     * </p>
     *
     * @return the single file size limit.
     */
    public long getSingleFileLimit() {
        return this.singleFileLimit;
    }

    /**
     * <p>
     * Returns the total file size limit for all uploaded files in the request, in bytes. A value of -1 indicates no
     * limit.
     * </p>
     *
     * @return the total file size limit
     */
    public long getTotalFileLimit() {
        return this.totalFileLimit;
    }

    /**
     * <p>
     * Parses the uploaded files and parameters from the given request, using the HTTP 1.1 standard. It will use the
     * <code>HttpRequestParser</code> to do the paring.
     * </p>
     *
     * <p>
     * Note that a new parser is created each time this method is called to ensure thread-safety (since the parser is
     * not thread-safe itself).
     * </p>
     *
     * @param request the servlet request to be parsed.
     *
     * @return the <code>FileUploadResult</code> containing uploaded files and parameters information.
     *
     * @throws IllegalArgumentException if request is null.
     * @throws RequestParsingException if any I/O error occurs during parsing, or the parsing violates the constraints,
     *         e.g. file size limit is exceeded.
     * @throws PersistenceException if it fails to upload the files to the persistence.
     */
    public FileUploadResult uploadFiles(ServletRequest request) throws RequestParsingException, PersistenceException {
        return this.uploadFiles(request, new HttpRequestParser());
    }

    /**
     * <p>
     * Parses the uploaded files and parameters from the given request and parser. Depending on the subclass
     * implementation, the uploaded files should have been properly stored in the persistence after calling this
     * method.
     * </p>
     *
     * <p>
     * Note that the parser should not be used by other threads when this method is called.
     * </p>
     *
     * @param request the servlet request to be parsed.
     * @param parser the parser to use.
     *
     * @return the <code>FileUploadResult</code> containing uploaded files and parameters information.
     *
     * @throws IllegalArgumentException if request or parser is null.
     * @throws RequestParsingException if any I/O error occurs during parsing, or the parsing violates the constraints,
     *         e.g. file size limit is exceeded.
     * @throws PersistenceException if it fails to upload the files to the persistence.
     */
    public abstract FileUploadResult uploadFiles(ServletRequest request, RequestParser parser)
        throws RequestParsingException, PersistenceException;

    /**
     * <p>
     * Retrieves the uploaded file from the persistence with the specified file id. This method should use a local copy
     * of the file if available.
     * </p>
     *
     * <p>
     * Note: It is assumed that the uploaded file will not be modified externally in the persistence. Users can safely
     * assume that the data in the returned instance is valid and accurate (compared to the persistence), except when
     * the file is removed.
     * </p>
     *
     * @param fileId the id of the file to retrieve.
     *
     * @return the uploaded file.
     *
     * @throws IllegalArgumentException if fileId is null or empty string.
     * @throws PersistenceException if any error occurs in retrieving the file from persistence.
     * @throws FileDoesNotExistException if the file does not exist in persistence.
     */
    public UploadedFile getUploadedFile(String fileId) throws PersistenceException, FileDoesNotExistException {
        return getUploadedFile(fileId, false);
    }

    /**
     * <p>
     * Retrieves the uploaded file from the persistence with the specified file id. The behavior depends on the value
     * of the refresh flag.
     * </p>
     *
     * <p>
     * If refresh is true, the method should always retrieve the whole file from the persistence, in case the uploaded
     * file is modified externally.
     * </p>
     *
     * <p>
     * If refresh is false, the method should use a local copy of the file if available. It is assumed that the
     * uploaded file will not be modified externally in the persistence. This is for performance gain.
     * </p>
     *
     * @param fileId the id of the file to retrieve.
     * @param refresh whether to refresh the cached file copy.
     *
     * @return the uploaded file.
     *
     * @throws IllegalArgumentException if fileId is null or empty string.
     * @throws PersistenceException if any error occurs in retrieving the file from persistence.
     * @throws FileDoesNotExistException if the file does not exist in persistence.
     */
    public abstract UploadedFile getUploadedFile(String fileId, boolean refresh)
        throws PersistenceException, FileDoesNotExistException;

    /**
     * <p>
     * Removes the uploaded file from the persistence with the specified file id. Once removed the file contents are
     * lost.
     * </p>
     *
     * @param fileId the id of the file to remove.
     *
     * @throws IllegalArgumentException if fileId is null or empty string.
     * @throws PersistenceException if any error occurs in removing the file from persistence.
     * @throws FileDoesNotExistException if the file does not exist in persistence.
     */
    public abstract void removeUploadedFile(String fileId) throws PersistenceException, FileDoesNotExistException;
}
