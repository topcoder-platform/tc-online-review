/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.servlet.request.FileSizeLimitExceededException;
import com.topcoder.servlet.request.RequestParser;
import com.topcoder.servlet.request.RequestParsingException;

import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A {@link RequestParser} to be passed to <code>File Upload</code> for uploading the files represented by simple
 * text content.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0 (Specification Review Part 1 Assembly)
 */
public class TextContentRequestParser implements RequestParser {

    /**
     * <p>A <code>boolean</code> flag indicating whether this parser has any content for consumption or not.</p>
     */
    private boolean hasNextFile = true;

    /**
     * <p>A <code>String</code> providing the uploaded content.</p>
     */
    private String content;

    /**
     * <p>Constructs new <code>TextContentRequestParser</code> instance. This implementation does nothing.</p>
     *
     * @param content a <code>String</code> providing the uploaded content.
     */
    public TextContentRequestParser(String content) {
        this.content = content;
    }

    /**
     * <p> Parses the given request to initialize the parsing process. This must be the first method to call for
     * parsing. </p>
     *
     * @param request the servlet request to be parsed.
     * @throws IllegalArgumentException if request is null.
     * @throws RequestParsingException if any I/O error occurs during parsing, or the parsing violates the constraints,
     * e.g. invalid content type.
     */
    public void parseRequest(ServletRequest request) throws RequestParsingException {
        // Do nothing
    }

    /**
     * <p> Checks whether next uploaded file is available in the request. If this method returns true, then form file
     * name, remote file name and content type for this file will be available using respective methods. The
     * writeNextFile() method should then be called to store the file contents. </p>
     *
     * <p> Note that this method can be called only after parseRequest() or writeNextFile(). </p>
     *
     * @return true if more files are available, false otherwise.
     * @throws RequestParsingException if any I/O error occurs during parsing, or the parsing violates the constraints.
     * @throws IllegalStateException if parseRequest() or writeNextFile() has not been called.
     */
    public boolean hasNextFile() throws RequestParsingException {
        return this.hasNextFile;
    }

    /**
     * <p> Gets the form file name of the last parsed file. Can be null if it is not available. </p>
     *
     * @return the form file name of the last parsed file.
     */
    public String getFormFileName() {
        return "file";
    }

    /**
     * <p> Gets the remote file name of the last parsed file. Can be null if it is not available. </p>
     *
     * @return the remote file name of the last parsed file.
     */
    public String getRemoteFileName() {
        return "specificationText.txt";
    }

    /**
     * <p> Gets the content type of the last parsed file. Can be null if it is not available. </p>
     *
     * @return the content type of the last parsed file.
     */
    public String getContentType() {
        return "text/plain";
    }

    /**
     * <p> Writes the next uploaded file contents to the specified output stream. The fileLimit parameter specifies the
     * maximum size of this file, in bytes. A value of -1 indicates no limit. If it is exceeded, a
     * <code>FileSizeLimitExceededException</code> will be thrown. </p>
     *
     * <p> Note that this method can be called only if hasNextFile() has been called and returns true. </p>
     *
     * @param out the output stream to write file contents to.
     * @param fileLimit the file size limit in bytes.
     * @throws IllegalArgumentException if outputStream is null or fileLimit is less than -1.
     * @throws IOException if any I/O error occurs in writing the file.
     * @throws FileSizeLimitExceededException if the uploaded file is too large.
     * @throws IllegalStateException if hasNextFile() has not been called or hasNextFile() returns false.
     */
    public void writeNextFile(OutputStream out, long fileLimit) throws IOException, FileSizeLimitExceededException {
        if (!this.hasNextFile) {
            throw new IllegalStateException("There is no content for writting");
        }
        if (fileLimit != -1) {
            if (this.content.length() > fileLimit) {
                throw new FileSizeLimitExceededException("The file limit of " + fileLimit + " is exceeded");
            }
        }
        out.write(this.content.getBytes());
        this.hasNextFile = false;
    }

    /**
     * <p> Gets the parameters collected in the request during the parsing process. The key should be parameter name
     * (String), the value should be a list (List) of parameter values (String). </p>
     *
     * @return a map from parameter names to parameter values.
     */
    public Map getParameters() {
        return new HashMap();
    }
}
