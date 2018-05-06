/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;


/**
 * <p>
 * This class represents the memory storage of the uploaded files. This class works by saving the uploaded files into
 * memory (using a byte array). Overwriting of files is not allowed.
 * </p>
 *
 * <p>
 * <code>MemoryFileUpload</code> can be used for fast retrieval of data (without I/O) when the file size is small. It
 * cannot be used in a clustered environment however. It is strongly recommended to limit the file size by specifying
 * the single_file_limit property in the configuration.
 * </p>
 *
 * <p>
 * This class should be initialized from a configuration namespace which should be preloaded. This class supports no
 * extra property in addition to the base class.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is thread-safe by being immutable.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class MemoryFileUpload extends FileUpload {
    /**
     * <p>
     * Creates a new instance of <code>MemoryFileUpload</code> class to load configuration from the specified
     * namespace. The namespace should be preloaded.
     * </p>
     *
     * @param namespace the configuration namespace to use.
     *
     * @throws IllegalArgumentException if namespace is null or empty string.
     * @throws ConfigurationException if any configuration error occurs.
     */
    public MemoryFileUpload(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * <p>
     * Parses the uploaded files and parameters from the given request and parser. It saves each uploaded file into a
     * byte array.
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
    public FileUploadResult uploadFiles(ServletRequest request, RequestParser parser)
        throws RequestParsingException, PersistenceException {
        FileUploadHelper.validateNotNull(request, "request");
        FileUploadHelper.validateNotNull(parser, "parser");

        synchronized (parser) {
            // parse the request with the parse
            parser.parseRequest(request);

            Map uploadedFiles = new HashMap();
            long currentTotalSize = 0;

            while (parser.hasNextFile()) {
                // get the remote file name
                String fileName = parser.getRemoteFileName();

                // get the form file name
                String formFileName = parser.getFormFileName();

                // get the content type
                String contentType = parser.getContentType();

                ByteArrayOutputStream output = null;

                try {
                    output = new ByteArrayOutputStream();
                    parser.writeNextFile(output,
                            Math.min(getSingleFileLimit(), getTotalFileLimit() - currentTotalSize));

                    // add the new updated file
                    byte[] content = output.toByteArray();
                    List files = (List) uploadedFiles.get(formFileName);

                    if (files == null) {
                        files = new ArrayList();
                        uploadedFiles.put(formFileName, files);
                    }

                    files.add(new MemoryUploadedFile(content, fileName, contentType));

                    // update the current total size of the uploaded files
                    currentTotalSize += content.length;
                } catch (IOException e) {
                    throw new PersistenceException("Fails to upload the files to the persistence.", e);
                } finally {
                    FileUploadHelper.closeOutputStream(output);
                }
            }

            return new FileUploadResult(parser.getParameters(), uploadedFiles);
        }
    }

    /**
     * <p>
     * Retrieves the uploaded file from the persistence with the specified file id. The behavior depends on the value
     * of the refresh flag.
     * </p>
     *
     * <p>
     * This method throws an UnsupportedOperationException. An uploaded file stored in memory is not identified by a
     * fileId.
     * </p>
     *
     * @param fileId the id of the file to retrieve.
     * @param refresh whether to refresh the cached file copy.
     *
     * @return the uploaded file.
     *
     * @throws IllegalArgumentException if fileId is null or empty string.
     * @throws UnsupportedOperationException to indicate the retrieval operation is not supported.
     */
    public UploadedFile getUploadedFile(String fileId, boolean refresh) {
        throw new UnsupportedOperationException("Retrieval operation is not supported.");
    }

    /**
     * <p>
     * Removes the uploaded file from the persistence with the specified file id. Once removed the file contents are
     * lost.
     * </p>
     *
     * <p>
     * This method throws an UnsupportedOperationException. An uploaded file stored in memory is not identified by a
     * fileId.
     * </p>
     *
     * @param fileId the id of the file to remove.
     *
     * @throws IllegalArgumentException if fileId is null or empty string.
     * @throws UnsupportedOperationException to indicate the removal operation is not supported.
     */
    public void removeUploadedFile(String fileId) {
        throw new UnsupportedOperationException("Removal operation is not supported.");
    }
}
