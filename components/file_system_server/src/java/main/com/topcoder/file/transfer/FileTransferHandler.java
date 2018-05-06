/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

/**
 * This interface should be implemented by the user to handle the errors that might occur during a file transfer.
 * Instances are used by the upload worker and retrieve worker threads to signal errors.
 * @author Luca, FireIce
 * @version 1.0
 */
public interface FileTransferHandler {
    /**
     * Handle the error, using the given arguments. Null arguments are allowed, since some of them may not be available.
     * The upload worker will always provide the fileLocation and fileName, and possibly the fileId. The retrieve worker
     * will always provide the fileId and fileLocation, and possibly the fileName.
     * @param fileId
     *            the file id
     * @param fileLocation
     *            the file location
     * @param fileName
     *            the file name
     * @param finalRequestId
     *            the final message request id
     * @param exception
     *            the error
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws NullPointerException
     *             if fileLocation or finalRequestId are null
     */
    public void handleError(String fileId, String fileLocation, String fileName, String finalRequestId,
            Exception exception);

    /**
     * Handle the transfer proccess, using the given arguments. Null arguments are allowed, since some of them may not
     * be available. The upload worker will always provide the fileLocation and fileName, and possibly the fileId. The
     * retrieve worker will always provide the fileId and fileLocation, and possibly the fileName.
     * @param fileId
     *            the file id
     * @param fileLocation
     *            the file location
     * @param fileName
     *            the file name
     * @param finalRequestId
     *            the final message request id
     * @param transferBytesSize
     *            the size of the bytes transfered in the last message.
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws NullPointerException
     *             if fileLocation or finalRequestId are null
     */
    public void handleTransferProgress(String fileId, String fileLocation, String fileName, String finalRequestId,
            long transferBytesSize);
}
