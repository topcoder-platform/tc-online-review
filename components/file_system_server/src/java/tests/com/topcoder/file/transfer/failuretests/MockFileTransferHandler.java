/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.FileTransferHandler;


/**
 * Mock implementation of the FileTransferErrorHandler interface, used for testing only.
 *
 * @author fairytale
 * @version 1.0
 */
public class MockFileTransferHandler implements FileTransferHandler {
    /**
     * Mock implementation, does nothing.
     *
     * @param fileId the file id
     * @param fileLocation the file location
     * @param fileName the file name
     * @param finalRequestId the final message request id
     * @param exception the error
     */
    public void handleError(String fileId, String fileLocation, String fileName, String finalRequestId,
        Exception exception) {
    }

    /**
     * Mock implementation, does nothing.
     *
     * @param fileId the file id
     * @param fileLocation the file location
     * @param fileName the file name
     * @param finalRequestId the final message request id
     * @param transferBytesSize the size of the bytes transfered in the last message.
     */
    public void handleTransferProgress(String fileId, String fileLocation, String fileName, String finalRequestId,
        long transferBytesSize) {
    }
}
