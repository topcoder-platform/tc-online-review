/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.validator;

import java.io.File;

/**
 * This is the default implementation of the FreeDiskSpaceChecker. It uses JNI to perform its job. The
 * freeDiskSpaceExceedsSize(long) delegates its work to a private native method. The class is thread safe, as
 * freeDiskSpaceExceedsSize(long) is synchronized, to avoid conflicts in the native code. The name of the dynamic
 * library used by this class is "FSSDiskSpaceChecker" (FSS - File System Server). Impl. notes for the native code for
 * "-freeDiskSpaceExceedsSize(fileLocation:String, fileSize:long) : boolean": Windows: ...using the function
 * GetFreeDiskSpace, or GetFreeDiskSpaceEx, or GetFreeDiskSpaceExA ...these functions require to extract the drive from
 * the fileLocation Linux: ...using the functions ls, or df, or dp. Solaris: ...using the functions di or df.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FreeDiskSpaceNativeChecker implements FreeDiskSpaceChecker {

    /**
     * Represents the file location used by this class. This class checks if there is enough free space at this file
     * location. Initialized in the constructor and never changed later. Not null. Represents a valid file location
     * (directory).
     */
    private final String fileLocation;

    /**
     * This is a static class initializer.
     */
    static {
        System.loadLibrary("FSSDiskSpaceChecker");
    }

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
    public FreeDiskSpaceNativeChecker(String fileLocation) {
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
    public synchronized boolean freeDiskSpaceExceedsSize(long fileSize) throws FreeDiskSpaceCheckerException {
        if (fileSize <= 0) {
            throw new IllegalArgumentException("fileSize argument should be positive");
        }
        return freeDiskSpaceExceedsSize(fileLocation, fileSize);
    }

    /**
     * Checks whether the disk size exceeds the file size. It will check if at the fileLocation the disk space exceeds
     * the fileSize. This is a native method. Details about how this method should be implemented for different
     * operationg systems are given in the documentation for the class.
     * @param fileLocation
     *            the file location
     * @param fileSize
     *            the size of the file
     * @return true while the disk size exceeds the file size, or false
     */
    private native boolean freeDiskSpaceExceedsSize(String fileLocation, long fileSize);
}
