/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.validator;

/**
 * This interface declares the contract for the concrete free disk space checkers. This interface is used by the
 * UploadRequestValidator. Implementations should be thread safe.
 * @author Luca, FireIce
 * @version 1.0
 */
public interface FreeDiskSpaceChecker {
    /**
     * Checks whether the disk size exceeds the file size.
     * @param fileSize
     *            the size of the file
     * @return whether the disk size exceeds the file size, or not
     * @throws IllegalArgumentException
     *             if the argument is not positive
     * @throws FreeDiskSpaceCheckerException
     *             if an error occurs while performing the check
     */
    public boolean freeDiskSpaceExceedsSize(long fileSize) throws FreeDiskSpaceCheckerException;
}
