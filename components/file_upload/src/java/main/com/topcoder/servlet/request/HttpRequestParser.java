/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;


/**
 * <p>
 * An implementation of <code>RequestParser</code> interface to parse the uploaded files and parameters from the
 * servlet request using the HTTP 1.1 standard. For details of the protocol please refer to RFC 1867.
 * </p>
 *
 * <p>
 * The usage of this class should follow that defined in the interface. This parser can be reused (for parsing another
 * request), but this is not recommended. Note that if any exception is caught, make sure the underlying input stream
 * is closed in any case.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is not thread-safe. In fact it requires a sequence of method calls to work
 * properly. But the <code>FileUpload</code> class will use it in a thread-safe manner.
 * </p>
 *
 * @author colau, PE
 * @version 2.0
 */
public class HttpRequestParser implements RequestParser {
    /**
     * <p>
     * Represents the attribute name of the boundary.
     * </p>
     */
    private static final String BOUNDARY = "boundary=";

    /**
     * <p>
     * Represents the attribute name of the form file name.
     * </p>
     */
    private static final String FORM_FILE_NAME = "name=\"";

    /**
     * <p>
     * Represents the attribute name of the remote file name.
     * </p>
     */
    private static final String REMOTE_FILE_NAME = "filename=\"";

    /**
     * <p>
     * Represents the method parseRequest(ServletRequest) has been called.
     * </p>
     */
    private static final int PARSER_EQUEST_CALLED = 0;

    /**
     * <p>
     * Represents the method hasNextFile() has been called and the return value is true.
     * </p>
     */
    private static final int HAS_NEXT_FILE_CALLED_AND_RETURN_TRUE = 1;

    /**
     * <p>
     * Represents the method writeNextFile(OutputStream, long) has been called.
     * </p>
     */
    private static final int WRITE_NEXT_FILE_CALLED = 2;

    /**
     * <p>
     * Represents the end of the input stream is reached, no operations should be done any more.
     * </p>
     */
    private static final int END_REACHED = 3;

    /**
     * <p>
     * Represents the form file name of the last parsed file. It is initially set to null. It is reset in the
     * parseRequest() method, updated in the hasNextFile() method and accessed in the getFormFileName() method.
     * </p>
     */
    private String formFileName = null;

    /**
     * <p>
     * Represents the remote file name of the last parsed file. It is initially set to null. It is reset in the
     * parseRequest() method, updated in the hasNextFile() method and accessed in the getRemoteFileName() method.
     * </p>
     */
    private String remoteFileName = null;

    /**
     * <p>
     * Represents the content type of the last parsed file. It is initially set to null. It is reset in the
     * parseRequest() method, updated in the hasNextFile() method and accessed in the getContentType() method.
     * </p>
     */
    private String contentType = null;

    /**
     * <p>
     * Represents the parameters collected in the request during the parsing process. The key should be parameter name
     * (String), the value should be a list (List) of parameter values (String). It is initially set to an empty map
     * and the reference does not change. It is reset in the parseRequest() method, (the elements) updated in the
     * hasNextFile() method and accessed in the getParameters() method.
     * </p>
     */
    private final Map parameters = new HashMap();

    /**
     * <p>
     * Represents the input stream pointing to data submitted in the request. It is initially set to null. It is set in
     * the parseRequest() method and accessed in the hasNextFile() and writeNextFile() methods.
     * </p>
     */
    private ServletInputStream in = null;

    /**
     * <p>
     * Represents the buffer to reading.
     * </p>
     */
    private byte[] buffer = new byte[8 * 1024];

    /**
     * <p>
     * Represents the boundary which is a unique string that does not occur in the data (according to HTTP 1.1). It is
     * initially set to null. It is set in the parseRequest() method and accessed in the hasNextFile() and
     * writeNextFile() methods.
     * </p>
     */
    private String boundary = null;

    /**
     * <p>
     * Represents the flag which indicates the last operation. It is initialized into -1, and 0 means the method
     * parseRequest(ServletRequest) has been called; 1 means the method hasNextFile() has been called and the return
     * value is true; 2 means the method writeNextFile(OutputStream, long) has been called; 3 means the end of the
     * input stream is reached, no operations should be done any more.
     * </p>
     */
    private int lastOperation = -1;

    /**
     * <p>
     * Represents whether the end of the input stream has been reached.
     * </p>
     */
    private boolean isReachEnd = false;

    /**
     * <p>
     * Default constructor. Does nothing.
     * </p>
     */
    public HttpRequestParser() {
    }

    /**
     * <p>
     * Parses the given request to initialize the parsing process. This must be the first method to call for parsing.
     * </p>
     *
     * @param request the servlet request to be parsed.
     *
     * @throws RequestParsingException if any I/O error occurs during parsing, or the parsing violates the constraints,
     *         e.g. invalid content type.
     */
    public void parseRequest(ServletRequest request) throws RequestParsingException {
        this.isReachEnd = false;
        try {
            // get the content type of request.
            String requestContentType = this.getContentType(request);

            // retrieves the boundary
            resetBoundary(requestContentType);

            // get the servlet input stream
            this.in = request.getInputStream();

            // Read the first boundary line.
            String line = readTextLine();

            if ((line == null) || !line.startsWith(boundary)) {
                throw new RequestParsingException("Incorrect multipart form; should begin with boundary: " + boundary);
            }

            if (line.startsWith(boundary + "--")) {
                this.isReachEnd = true;
            }
        } catch (IOException e) {
            this.closeInputStream();
            throw new RequestParsingException("I/O error occurs during parsing", e);
        } catch (RequestParsingException e) {
            this.closeInputStream();
            throw e;
        }

        // reset all variables to their initial values
        this.contentType = null;
        this.formFileName = null;
        this.remoteFileName = null;
        this.parameters.clear();
        this.lastOperation = PARSER_EQUEST_CALLED;
    }

    /**
     * <p>
     * Read a line ending in "\n" or end-of stream from input stream.
     * </p>
     *
     * @return a String containing the next line of input from the stream, or null to indicate the end of the stream.
     *
     * @throws IOException In case of I/O Errors.
     */
    private String readTextLine() throws IOException {
        StringBuffer lineBuffer = new StringBuffer();
        int result = in.readLine(buffer, 0, buffer.length);

        if (result != -1) {
            lineBuffer.append(new String(buffer, 0, result, "ISO-8859-1"));
        } else {
            return null; //Nothing read, presumably at end of input stream
        }

        // Cut off the trailing \n or \r\n
        int len = lineBuffer.length();

        if (lineBuffer.charAt(len - 2) == '\r') {
            lineBuffer.setLength(len - 2); // cut \r\n
        } else {
            lineBuffer.setLength(len - 1); // cut \n
        }

        return lineBuffer.toString();
    }

    /**
     * <p>
     * Gets the content type of request.
     * </p>
     *
     * @param request the servlet request to be parsed.
     *
     * @return the content type of request.
     *
     * @throws InvalidContentTypeException if the contentType is not multipart/form-data.
     */
    private String getContentType(ServletRequest request) throws InvalidContentTypeException {
        String requestContentType = request.getContentType();

        // check that content type is "multipart/form-data"
        if (requestContentType == null) {
            throw new InvalidContentTypeException("contentType is not set in the ServletRequest.");
        }

        if (!requestContentType.toLowerCase().startsWith("multipart/form-data")) {
            throw new InvalidContentTypeException("contentType " + requestContentType + " is not multipart/form-data.");
        }

        return requestContentType;
    }

    /**
     * <p>
     * Retrieves the boundary from the <code>Content-type</code> header.
     * </p>
     *
     * @param requestContentType The value of the content type header from which to extract the boundary value.
     *
     * @throws InvalidContentTypeException if the boundary can not be properly got.
     */
    private void resetBoundary(String requestContentType) throws InvalidContentTypeException {
        int boundaryIndex = requestContentType.lastIndexOf(BOUNDARY);

        if (boundaryIndex < 0) {
            throw new InvalidContentTypeException("Required boundary string absent.");
        }

        // get the content of the boundary
        boundary = requestContentType.substring(boundaryIndex + BOUNDARY.length());

        // remove quotes if browser inserted them
        if (boundary.charAt(0) == '"') {
            boundary = boundary.substring(1, boundary.lastIndexOf('"'));
        }

        //boundary should actually begin with extra '--' in stream
        boundary = "--" + boundary;
    }

    /**
     * <p>
     * Retrieves the form file name from the given line.
     * </p>
     *
     * @param line the given line to retrieve the form file name.
     * @param lowerCaseLine the lower case representation of the line.
     *
     * @throws RequestParsingException if the form file name can not be retrieved or it is an empty string.
     */
    private void resetFormFileName(String line, String lowerCaseLine) throws RequestParsingException {
        int start = line.indexOf(";");

        if (start == -1) {
            throw new RequestParsingException("The content disposition line is malformed: " + line);
        }

        start = lowerCaseLine.indexOf(FORM_FILE_NAME, start);

        if (start == -1) {
            throw new RequestParsingException("The content disposition line is malformed: " + line);
        }

        int end = line.indexOf("\"", start + FORM_FILE_NAME.length());

        if (end == -1) {
            throw new RequestParsingException("The content disposition line is malformed: " + line);
        }

        // pull out stuff till first quotes after name
        this.formFileName = line.substring(start + FORM_FILE_NAME.length(), end);
        if (this.formFileName.trim().length() == 0) {
            throw new RequestParsingException("formFileName can not be empty string.");
        }
    }

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
     * <p>
     * Content-Disposition: form-data; name=&quot;&lt;form_file_name&gt;&quot;; filename=
     * &quot;&lt;remoteFileDir/remoteFileName&gt;&quot;<br>
     * Content-Type: &lt;fileContentType&gt;<br>
     * Then, an empty line always separates header from actual file, which is what we want to read until.<br>
     * Extension: line could, instead, hold a non-file parameter:<br>
     * Content-Disposition: form-data; name=&quot;&lt;parameter_name&gt;&quot; &lt;Value&gt;
     * </p>
     *
     * @return true if more files are available, false otherwise.
     *
     * @throws RequestParsingException if any I/O error occurs during parsing, or the parsing violates the constraints.
     * @throws IllegalStateException if this method is not called after parseRequest() or writeNextFile().
     */
    public boolean hasNextFile() throws RequestParsingException {
        if ((lastOperation != PARSER_EQUEST_CALLED) && (lastOperation != WRITE_NEXT_FILE_CALLED)) {
            this.closeInputStream();
            throw new IllegalStateException("This method can be called only after parseRequest() or writeNextFile().");
        }

        // Read the header
        boolean readingFile = false;

        try {
            while (!isReachEnd) {
                String line = null;

                while (((line = readTextLine()) != null) && (line.length() > 0)) {
                    String lowerCaseLine = line.toLowerCase();

                    if (lowerCaseLine.startsWith("content-disposition:")) {
                        // Get the form file name
                        // name should be 2nd field, after content-disposition
                        resetFormFileName(line, lowerCaseLine);

                        // Get the remote file name
                        int start = lowerCaseLine.indexOf(REMOTE_FILE_NAME);
                        int end = 0;

                        if (start != -1) {
                            end = line.indexOf("\"", start + REMOTE_FILE_NAME.length());
                        }

                        if ((start != -1) && (end != -1)) {
                            this.remoteFileName = line.substring(start + REMOTE_FILE_NAME.length(), end);

                            // Extract just file name, not directory name(consider both Windows and UNIX style slashes)
                            int slash = Math.max(remoteFileName.lastIndexOf('/'), remoteFileName.lastIndexOf('\\'));

                            if (slash >= 0) {
                                this.remoteFileName = remoteFileName.substring(slash + 1);
                            }

                            readingFile = true;
                        }
                    }

                    if (lowerCaseLine.startsWith("content-type:")) {
                        int start = line.indexOf(" ");

                        //that's what should be between 'content-type' and actual spec if everything is OK
                        if (start == -1) {
                            throw new InvalidContentTypeException("The content type line is malformed: " + line);
                        }

                        this.contentType = line.substring(start + 1);
                    }
                }

                if (readingFile) {
                    this.lastOperation = HAS_NEXT_FILE_CALLED_AND_RETURN_TRUE;

                    return true;
                }

                // it is parameter (no "filename" in the "Content-Disposition")
                OutputStream paramOut = new ByteArrayOutputStream();
                this.readContentValue(paramOut, -1);

                // add into the parameters map
                List values = (List) this.parameters.get(formFileName);

                if (values == null) {
                    values = new ArrayList();
                    this.parameters.put(formFileName, values);
                }

                values.add(paramOut.toString());
                paramOut.close();
            }

            this.lastOperation = END_REACHED;

            return false;
        } catch (IOException e) {
            this.closeInputStream();
            throw new RequestParsingException("I/O error occurs during parsing", e);
        } catch (RequestParsingException e) {
            this.closeInputStream();
            throw e;
        } finally {
            if (isReachEnd) {
                this.closeInputStream();
            }
        }
    }

    /**
     * <p>
     * Reads the content value from the input stream of the request. The content can be either the parameter value of
     * the file content value.
     * </p>
     *
     * @param out The output stream to write the content to.
     * @param fileLimit the file size limit in bytes.
     *
     * @throws IOException if any I/O error occurs in writing the file.
     * @throws FileSizeLimitExceededException if the uploaded file is too large.
     */
    private void readContentValue(OutputStream out, long fileLimit) throws IOException, FileSizeLimitExceededException {
        long fileSize = 0;
        boolean firstReadOfFile = true;

        while (true) {
            //Problem - browser sticks on two characters ('\r\n')
            //at end of file stream just before boundary
            //Need to make sure we don't read them into file
            int readOffset = firstReadOfFile ? 0 : 2;
            int thisRead = in.readLine(buffer, readOffset, buffer.length - readOffset);

            if (thisRead == -1) { //Unexpected end of stream
                throw new IOException("Unexpected end of stream while reading field " + this.formFileName);
            }

            //Check whether we've reached boundary
            boolean reachedBoundary = true;

            for (int i = 0; i < boundary.length(); i++) {
                if (buffer[i + readOffset] != boundary.charAt(i)) {
                    reachedBoundary = false;

                    break;
                }
            }

            if (!reachedBoundary) {
                firstReadOfFile = false;
                fileSize += (thisRead - 2 + readOffset);

                //Check whether exceeded limit on single file size
                if ((fileSize > fileLimit) && (fileLimit >= 0)) {
                    throw new FileSizeLimitExceededException("File " + this.formFileName
                        + " exceeds single file size limit of " + fileLimit + " bytes");
                }

                out.write(buffer, 0, thisRead - 2 + readOffset);
                out.flush();

                //Copy last two chars back to beginning of buffer - haven't reached boundary, so they are not '\r\n'
                System.arraycopy(buffer, thisRead - 2 + readOffset, buffer, 0, 2);
            } else {
                if ((buffer[readOffset + boundary.length()] == '-')
                        && (buffer[readOffset + boundary.length() + 1] == '-')) {
                    this.isReachEnd = true;
                }

                return;
            }
        }
    }

    /**
     * <p>
     * Gets the form file name of the last parsed file. Can be null if it is not available.
     * </p>
     *
     * @return the form file name of the last parsed file.
     */
    public String getFormFileName() {
        return this.formFileName;
    }

    /**
     * <p>
     * Gets the remote file name of the last parsed file. Can be null if it is not available.
     * </p>
     *
     * @return the remote file name of the last parsed file.
     */
    public String getRemoteFileName() {
        return this.remoteFileName;
    }

    /**
     * <p>
     * Gets the content type of the last parsed file. Can be null if it is not available.
     * </p>
     *
     * @return the content type of the last parsed file.
     */
    public String getContentType() {
        return this.contentType;
    }

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
     * @throws IOException if any I/O error occurs in writing the file.
     * @throws FileSizeLimitExceededException if the uploaded file is too large.
     * @throws IllegalStateException if this method is not called after hasNextFile() has been called and
     * returns true.
     */
    public void writeNextFile(OutputStream out, long fileLimit) throws IOException, FileSizeLimitExceededException {
        if (this.lastOperation != HAS_NEXT_FILE_CALLED_AND_RETURN_TRUE) {
            this.closeInputStream();
            throw new IllegalStateException(
                "This method can be called only if hasNextFile() has been called and returns true.");
        }

        try {
            this.readContentValue(out, fileLimit);
        } catch (IOException e) {
            this.closeInputStream();
            throw e;
        } catch (FileSizeLimitExceededException e) {
            this.closeInputStream();
            throw e;
        }

        this.lastOperation = WRITE_NEXT_FILE_CALLED;
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
        return new HashMap(this.parameters);
    }

    /**
     * <p>
     * Close the input stream of the request.
     * </p>
     */
    private void closeInputStream() {
        try {
            if (this.in != null) {
                this.in.close();
            }
        } catch (IOException e) {
            // ignore
        }
    }
}
