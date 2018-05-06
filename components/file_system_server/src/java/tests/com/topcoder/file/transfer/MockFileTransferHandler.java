/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer;

/**
 * Mock implementation of the FileTransferErrorHandler interface, only for testing purpose.
 * @author FireIce
 * @version 1.0
 */
public class MockFileTransferHandler implements FileTransferHandler {
    /**
     * Handle the error, using the given arguments. empty body, only for testing purpose.
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
     */
    public void handleError(String fileId, String fileLocation, String fileName, String finalRequestId,
            Exception exception) {
    }

    /**
     * Handle the transfer proccess, using the given arguments. empty body, only for testing purpose.
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
     */
    public void handleTransferProgress(String fileId, String fileLocation, String fileName, String finalRequestId,
            long transferBytesSize) {
    }

}
