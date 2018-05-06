/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Map;

import javax.servlet.ServletRequest;


/**
 * <p>
 * This interface defines the contract to parse the uploaded files and parameters from the servlet request. For
 * uploaded file, its form file name, remote file name, content type and file contents will be retrieved. It provides
 * the method to write the file contents to the specified output stream for storage purpose.
 * </p>
 *
 * <p>
 * The usage of this interface is defined as follows:
 *
 * <ul>
 * <li>
 * Initialize the parsing process by parseRequest().
 * </li>
 * <li>
 * Iterate for each uploaded file by hasNextFile(). If it returns true, getFormFileName(), getRemoteFileName(),
 * getContentType() should return appropriate values for the previously parsed file.
 * </li>
 * <li>
 * Call writeNextFile() to write the file contents to the output stream.
 * </li>
 * <li>
 * When hasNextFile() returns false, no more file is available. getParameters() should then return all the parameters
 * parsed in the process.
 * </li>
 * </ul>
 *
 * Note that IllegalStateException can be thrown if the above usage pattern is not followed.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>The implementation is not required to be thread-safe. In fact it requires a sequence of method
 * calls to work properly. But the FileUpload class will use it in a thread-safe manner.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public interface RequestParser {
    /**
     * <p>
     * Parses the given request to initialize the parsing process. This must be the first method to call for parsing.
     * </p>
     *
     * @param request the servlet request to be parsed.
     *
     * @throws IllegalArgumentException if request is null.
     * @throws RequestParsingException if any I/O error occurs during parsing, or the parsing violates the constraints,
     *         e.g. invalid content type.
     */
    void parseRequest(ServletRequest request) throws RequestParsingException;

    /**
     * <p>
     * Checks whether next uploaded file is available in the request. If this method returns true, then form file name,
     * remote file name and content type for this file will be available using respective methods. The writeNextFile()
     * method should then be called to store the file contents.
     * </p>
     *
     * <p>
     * Note that this method can be called only after parseRequest() or writeNextFile().
     * </p>
     *
     * @return true if more files are available, false otherwise.
     *
     * @throws RequestParsingException if any I/O error occurs during parsing, or the parsing violates the constraints.
     * @throws IllegalStateException if parseRequest() or writeNextFile() has not been called.
     */
    boolean hasNextFile() throws RequestParsingException;

    /**
     * <p>
     * Gets the form file name of the last parsed file. Can be null if it is not available.
     * </p>
     *
     * @return the form file name of the last parsed file.
     */
    String getFormFileName();

    /**
     * <p>
     * Gets the remote file name of the last parsed file. Can be null if it is not available.
     * </p>
     *
     * @return the remote file name of the last parsed file.
     */
    String getRemoteFileName();

    /**
     * <p>
     * Gets the content type of the last parsed file. Can be null if it is not available.
     * </p>
     *
     * @return the content type of the last parsed file.
     */
    String getContentType();

    /**
     * <p>
     * Writes the next uploaded file contents to the specified output stream. The fileLimit parameter specifies the
     * maximum size of this file, in bytes. A value of -1 indicates no limit. If it is exceeded, a
     * <code>FileSizeLimitExceededException</code> will be thrown.
     * </p>
     *
     * <p>
     * Note that this method can be called only if hasNextFile() has been called and returns true.
     * </p>
     *
     * @param out the output stream to write file contents to.
     * @param fileLimit the file size limit in bytes.
     *
     * @throws IllegalArgumentException if outputStream is null or fileLimit is less than -1.
     * @throws IOException if any I/O error occurs in writing the file.
     * @throws FileSizeLimitExceededException if the uploaded file is too large.
     * @throws IllegalStateException if hasNextFile() has not been called or hasNextFile() returns false.
     */
    void writeNextFile(OutputStream out, long fileLimit) throws IOException, FileSizeLimitExceededException;

    /**
     * <p>
     * Gets the parameters collected in the request during the parsing process. The key should be parameter name
     * (String), the value should be a list (List) of parameter values (String).
     * </p>
     *
     * @return a map from parameter names to parameter values.
     */
    Map getParameters();
}
