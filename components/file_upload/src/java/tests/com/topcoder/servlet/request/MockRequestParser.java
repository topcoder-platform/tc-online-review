/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Map;

import javax.servlet.ServletRequest;


/**
 * <p>
 * A mock class which implements the <code>RequestParser</code> interface for testing.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class MockRequestParser implements RequestParser {
    /** Represents the files to be parsed. */
    private File[] files = null;

    /** Represents the form file name of the files to be parsed. */
    private String[] formFileNames = null;

    /** Represents the remote file name of the files to be parsed. */
    private String[] remoteFileNames = null;

    /** Represents the content type of the files to be parsed. */
    private String[] contentTypes = null;

    /** Represents the parsed parameters. */
    private Map parameters = null;

    /** Represents the index which indicates the current index of the file to be parsed. */
    private int curFile = -1;

    /** Represents the buffer to reading. */
    private byte[] buffer = new byte[8 * 1024];

    /**
     * <p>
     * A mock constructor, which will prepare the data for testing.
     * </p>
     *
     * @param files the files to be parsed.
     * @param formFileNames the form file name of the files to be parsed.
     * @param remoteFileNames the remote file name of the files to be parsed.
     * @param contentTypes the content type of the files to be parsed.
     * @param parameters the parsed parameters.
     */
    public MockRequestParser(File[] files, String[] formFileNames, String[] remoteFileNames, String[] contentTypes,
        Map parameters) {
        this.files = files;
        this.formFileNames = formFileNames;
        this.remoteFileNames = remoteFileNames;
        this.contentTypes = contentTypes;
        this.parameters = parameters;
    }

    /**
     * <p>
     * Parses the given request to initialize the parsing process. This must be the first method to call for parsing.
     * </p>
     *
     * @param request the servlet request to be parsed.
     */
    public void parseRequest(ServletRequest request) {
        this.curFile = -1;
    }

    /**
     * <p>
     * Checks whether next uploaded file is available in the request.
     * </p>
     *
     * @return true if more files are available, false otherwise.
     */
    public boolean hasNextFile() {
        if ((this.curFile + 1) < this.files.length) {
            this.curFile++;

            return true;
        }

        return false;
    }

    /**
     * <p>
     * Gets the form file name of the last parsed file. Can be null if it is not available.
     * </p>
     *
     * @return the form file name of the last parsed file.
     */
    public String getFormFileName() {
        return this.formFileNames[curFile];
    }

    /**
     * <p>
     * Gets the remote file name of the last parsed file. Can be null if it is not available.
     * </p>
     *
     * @return the remote file name of the last parsed file.
     */
    public String getRemoteFileName() {
        return this.remoteFileNames[curFile];
    }

    /**
     * <p>
     * Gets the content type of the last parsed file. Can be null if it is not available.
     * </p>
     *
     * @return the content type of the last parsed file.
     */
    public String getContentType() {
        return this.contentTypes[curFile];
    }

    /**
     * <p>
     * Writes the next uploaded file contents to the specified output stream. The fileLimit parameter specifies the
     * maximum size of this file, in bytes. A value of -1 indicates no limit. If it is exceeded, a
     * <code>FileSizeLimitExceededException</code> will be thrown.
     * </p>
     *
     * @param out the output stream to write file contents to.
     * @param fileLimit the file size limit in bytes.
     *
     * @throws IOException if any I/O error occurs in writing the file.
     * @throws FileSizeLimitExceededException if the uploaded file is too large.
     */
    public void writeNextFile(OutputStream out, long fileLimit) throws IOException, FileSizeLimitExceededException {
        InputStream in = null;

        try {
            in = new FileInputStream(this.files[curFile]);

            long fileSize = 0;
            int thisRead = 0;

            while ((thisRead = in.read(buffer)) > 0) {
                fileSize += thisRead;

                //Check whether exceeded limit on single file size
                if ((fileLimit >= 0) && (fileSize > fileLimit)) {
                    throw new FileSizeLimitExceededException("File " + this.formFileNames[curFile]
                        + " exceeds single file size limit of " + fileLimit + " bytes");
                }

                out.write(buffer, 0, thisRead);
                out.flush();
            }
        } finally {
            in.close();
        }
    }

    /**
     * <p>
     * Gets the parameters collected in the request during the parsing process. The key should be parameter name
     * (String), the value should be a list (List) of parameter values (String).
     * </p>
     *
     * @return a map from parameter names to parameter values.
     */
    public Map getParameters() {
        return this.parameters;
    }
}
