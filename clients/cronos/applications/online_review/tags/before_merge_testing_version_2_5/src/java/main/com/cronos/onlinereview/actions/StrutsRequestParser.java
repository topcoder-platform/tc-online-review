/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.struts.upload.FormFile;

import com.topcoder.servlet.request.FileSizeLimitExceededException;
import com.topcoder.servlet.request.RequestParser;
import com.topcoder.servlet.request.RequestParsingException;

/**
 * This class implements the <code>RequestParser</code> interface and provides a way to use the
 * File Upload 2.0 component with Struts framework.
 *
 * @author George1
 * @author real_vg
 * @version 1.0
 */
class StrutsRequestParser implements RequestParser {

    /**
     * This member variable contains a list of uploaded files.
     *
     * @see #AddFile(FormFile)
     * @see #AddFiles(FormFile[])
     * @see #AddFiles(List)
     */
    private final List files = new ArrayList();

    /**
     * This member variable references to the instance of <code>FormFile</code> class that denotes
     * current uploaded file all operation will be performed on until the next call of
     * {@link #hasNextFile()} method.
     */
    private FormFile currentFile = null;

    /**
     * This member variable denotes current file in the list of uploaded files that all operations
     * will be performed on. This variable initially has a value -1, which denotes that there is not
     * current file. Calling method {@link #hasNextFile()} will advance this pointer to the first
     * file in the internal list of uploaded files.
     *
     * @see #files
     * @see #hasNextFile()
     */
    private int currentFileIdx = -1;


    /**
     * This is the default constructor of the class which constucts a new instance of the
     * <code>StrutsRequestParser</code> class.
     */
    public StrutsRequestParser() {

    }


    /**
     * This method adds uploaded file into internal buffer of uploaded files. It is developer's
     * responsibility to check that each file is added only once.
     *
     * @param file
     *            an uploaded file to add.
     * @throws IllegalArgumentException
     *             if parameter <code>file</code> is <code>null</code>.
     */
    public void AddFile(FormFile file) {
        // Validate parameter
        ActionsHelper.validateParameterNotNull(file, "file");

        this.files.add(file);
    }

    /**
     * This method adds an array of uploaded files into internal buffer of uploaded files. It is
     * developer's responsibility to check that specified array does not contain <code>null</code>
     * values, duplicate items, or files that have already been added into internal buffer.
     *
     * @param files
     *            an array of uploaded files to add.
     * @throws IllegalArgumentException
     *             if parameter <code>files</code> is <code>null</code>.
     */
    public void AddFiles(FormFile[] files) {
        AddFiles(Arrays.asList(files));
    }

    /**
     * This method adds a list of uploaded files into internal buffer of uploaded files. It is
     * developer's responsibility to check that specified list does not contain <code>null</code>
     * values, duplicate items, or files that have already been added into internal buffer.
     *
     * @param files
     *            a list of uploaded files to add.
     * @throws IllegalArgumentException
     *             if parameter <code>files</code> is <code>null</code>.
     */
    public void AddFiles(List files) {
        // Validate parameter
        ActionsHelper.validateParameterNotNull(files, "files");

        this.files.addAll(files);
    }

    /**
     * This method does nothing, as all parsing of the request is done by Struts framework.
     *
     * @param request
     *            this parameter is not used.
     */
    public void parseRequest(ServletRequest request) throws RequestParsingException {
        // Do nothing
    }

    /**
     * This method advances the pointer to the next uploaded file (if any).
     *
     * @return <code>true</code> if there are more files available,
     *         <code>false</code> if there aren't.
     */
    public boolean hasNextFile() throws RequestParsingException {
        if (this.currentFileIdx < this.files.size()) {
            ++this.currentFileIdx;
        }
        if (this.currentFileIdx < this.files.size()) {
            this.currentFile = (FormFile) this.files.get(this.currentFileIdx);
        } else {
            this.currentFile = null;
        }
        return (this.currentFile != null);
    }

    /**
     * This method gets the form file name of the current file.
     *
     * @return the form file name, which always equals to &quot;file&quot; string.
     */
    public String getFormFileName() {
        return "file";
    }

    /**
     * This method returns the remote file name of the current file. Can be <code>null</code> if
     * it is not available.
     *
     * @return the remote file name of the current file, or <code>null</code> if the remote file
     *         name is not available or there is no current file.
     */
    public String getRemoteFileName() {
        if (this.currentFile == null) {
            return null;
        }
        return this.currentFile.getFileName();
    }

    /**
     * This method return the content type of the current file. Can be <code>null</code> if it is
     * not available.
     *
     * @return the content type of the current file, or <code>null</code> if the content type is
     *         not available, or there is no current file.
     */
    public String getContentType() {
        if (this.currentFile == null) {
            return null;
        }
        return this.currentFile.getContentType();
    }

    /**
     * This method writes the next uploaded file contents to the specified output stream. The
     * fileLimit parameter specifies the maximum size of this file, in bytes. A value of -1
     * indicates no limit. If it is exceeded, a FileSizeLimitExceededException will be thrown.
     * <p>
     * Note that this method can be called only if hasNextFile() has been called and returns true.
     * </p>
     *
     * @param out
     *            the output stream to write file contents to.
     * @param fileLimit
     *            the file size limit in bytes.
     * @throws IllegalArgumentException
     *             if parameter <code>out</code> is <code>null</code>.
     * @throws IOException
     *             if any I/O error occurs while writing the file.
     * @throws FileSizeLimitExceededException
     *             if the uploaded file is too large.
     * @throws IllegalStateException
     *             if {@link #hasNextFile()} has not been called or the last call to
     *             {@link #hasNextFile()} returned <code>false</code>.
     */
    public void writeNextFile(OutputStream out, long fileLimit)
        throws IOException, FileSizeLimitExceededException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(out, "out");
        if (this.currentFile == null) {
            throw new IllegalStateException("There are no more uploaded files to write.");
        }
        if (fileLimit > 0 && this.currentFile.getFileSize() > fileLimit) {
            throw new FileSizeLimitExceededException("Uploaded file is too big. The maximum allowed size is "
                    + fileLimit + " while file's size is " + this.currentFile.getFileSize() + ".");
        }

        /*
         * Copy the contents of the uploaded file into the output stream
         */

        InputStream in = this.currentFile.getInputStream();
        byte[] buffer = new byte[65536];

        for (;;) {
            try {
                int numOfBytesRead = in.read(buffer);
                if (numOfBytesRead == -1) {
                    break;
                }
                out.write(buffer, 0, numOfBytesRead);
            } catch (IOException ioe) {
                in.close();
                throw ioe;
            }
        }

        in.close();
    }

    /**
     * This method always returns empty map.
     *
     * @return empty <code>HashMap</code> object.
     */
    public Map getParameters() {
        return new HashMap();
    }

}
