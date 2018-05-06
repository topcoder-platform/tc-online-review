/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

/**
 * <p>
 * This is an exception which indicates that the specified directory is not allowed to write files under, since it is
 * not one of the allowed directories.
 * </p>
 *
 * <p>
 * This exception is thrown in the constructors of <code>LocalFileUpload</code> class where directory is specified.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> This class is immutable and therefore thread safe.
 * </p>
 * @author colau, PE
 * @version 2.0
 */
public class DisallowedDirectoryException extends FileUploadException {
    /**
     * <p>
     * Represents the directory which is not allowed to write files under.
     * </p>
     */
    private final String dir;

    /**
     * <p>
     * Creates a new instance of <code>DisallowedDirectoryException</code> class with the disallowed directory.
     * </p>
     *
     * @param dir the directory which is not allowed to write files under.
     */
    public DisallowedDirectoryException(String dir) {
        super("The directory " + dir + " is disallowed.");
        this.dir = dir;
    }

    /**
     * <p>
     * Gets the directory which is not allowed to write files under.
     * </p>
     *
     * @return the directory which is not allowed to write files under.
     */
    public String getDir() {
        return this.dir;
    }
}
