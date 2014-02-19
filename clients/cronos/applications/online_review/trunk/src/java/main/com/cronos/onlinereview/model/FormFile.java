/*
 * Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * <p>
 * This class represents the form file for uploading.
 * </p>
 * <p>
 * Thread safety: This class is not thread safe.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class FormFile {
    /**
     * Represents the file size.
     */
    private final int fileSize;

    /**
     * Represents the file name.
     */
    private final String fileName;

    /**
     * Represents the file.
     */
    private final File file;

    /**
     * Represents the content type.
     */
    private final String contentType;

    /**
     * Constructor with the fields.
     */
    public FormFile() {
        this.fileSize = 0;
        this.fileName = null;
        this.file = null;
        this.contentType = null;
    }

    /**
     * Constructor with the fields.
     * @param file the file
     * @param fileName the file name
     * @param contentType the content type.
     */
    public FormFile(String fileName, File file, String contentType) {
        this.fileSize = (int) file.length();
        this.fileName = fileName;
        this.file = file;
        this.contentType = contentType;
    }

    /**
     * Get the input stream of this form file.
     * @return the input stream
     * @throws FileNotFoundException if the file can't be found
     */
    public InputStream getInputStream() throws FileNotFoundException {
        if (file == null) {
            throw new FileNotFoundException("The file doesn't exist");
        }
        return new FileInputStream(file);
    }

    /**
     * Getter of fileSize.
     * @return the fileSize
     */
    public int getFileSize() {
        return fileSize;
    }

    /**
     * Getter of fileName.
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Getter of contentType.
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }
}
