/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.validator;

import java.io.File;
import java.io.IOException;

/**
 * This is the non-native implementation of the FreeDiskSpaceChecker.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FreeDiskSpaceNonNativeChecker implements FreeDiskSpaceChecker {

    /**
     * Represents the file location used by this class. This class checks if there is enough free space at this file
     * location. Initialized in the constructor and never changed later. Not null. Represents a valid file location
     * (directory).
     */
    private final String fileLocation;

    /**
     * Creates an intance with the given file location. Check if the argument represents a valid file location
     * (directory) and assign the argument to the corresponding field.
     * @param fileLocation
     *            the file location
     * @throws NullPointerException
     *             if the argument is null
     * @throws IllegalArgumentException
     *             if the argument doesn't represent a valid file location (directory)
     */
    public FreeDiskSpaceNonNativeChecker(String fileLocation) {
        if (fileLocation == null) {
            throw new NullPointerException("the fileLocation is null");
        } else if (!new File(fileLocation).isDirectory()) {
            throw new IllegalArgumentException("the fileLocation should be a directory");
        }
        this.fileLocation = fileLocation;
    }

    /**
     * Checks whether the disk size exceeds the file size. It will check if at the this.fileLocation the disk space
     * exceeds the fileSize, using the private method. Impl. note: freeDiskSpaceExceedsSize(fileLocation,fileSize);
     * @param fileSize
     *            the size of the file
     * @return whether the disk size exceeds the file size, or not
     * @throws IllegalArgumentException
     *             if the argument is not positive
     * @throws FreeDiskSpaceCheckerException
     *             if an error occurs while performing the check
     */
    public boolean freeDiskSpaceExceedsSize(long fileSize) throws FreeDiskSpaceCheckerException {
        if (fileSize <= 0) {
            throw new IllegalArgumentException("the argument should be positve");
        }
        try {
            return FreeDiskSpaceUtils.freeDiskSpace(fileLocation) > fileSize;
        } catch (IOException e) {
            throw new FreeDiskSpaceCheckerException("I/O error occurs", e);
        }
    }

}
