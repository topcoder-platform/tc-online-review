/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import com.topcoder.file.transfer.FileTransferHandler;


/**
 * <p>
 * The default FileTransferHandler for this component. This handler will do nothing.
 * </p>
 * @author colau, PE
 * @version 2.0
 */
class DefaultFileTransferHandler implements FileTransferHandler {
    /**
     * <p>
     * Handle the error, using the given arguments. Empty body since this component will not use this method.
     * </p>
     *
     * @param fileId the file id.
     * @param fileLocation the file location.
     * @param fileName the file name.
     * @param finalRequestId the final message request id.
     * @param exception the error.
     */
    public void handleError(String fileId, String fileLocation, String fileName, String finalRequestId,
        Exception exception) {
    }

    /**
     * <p>
     * Handle the transfer process, using the given arguments. Empty body since this component will not use this
     * method.
     * </p>
     *
     * @param fileId the file id.
     * @param fileLocation the file location.
     * @param fileName the file name.
     * @param finalRequestId the final message request id.
     * @param transferBytesSize the size of the bytes transfered in the last message.
     */
    public void handleTransferProgress(String fileId, String fileLocation, String fileName, String finalRequestId,
        long transferBytesSize) {
    }
}
