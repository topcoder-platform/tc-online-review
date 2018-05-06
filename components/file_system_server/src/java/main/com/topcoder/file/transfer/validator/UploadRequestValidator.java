/*
 * Copyright (C) 2006-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.validator;

import com.topcoder.file.transfer.message.MessageType;
import com.topcoder.file.transfer.message.RequestMessage;
import com.topcoder.util.datavalidator.AbstractObjectValidator;

/**
 * This class represents the default upload request validator used by the file system handler. It validates objects of
 * RequestMessage type. The request message must have the type equal with RequestType.CHECK_UPLOAD_FILE. It uses a
 * FreeDiskSpaceChecker to check if the upload request is accepted. It will get the file size from request message's
 * args and pass it to the FreeDiskSpaceChecker. This class is thread safe. However, it uses the free disk space
 * checker, so this one should be thread safe.
 * @author Luca, FireIce
 * @version 1.1.1
 */
public class UploadRequestValidator extends AbstractObjectValidator {

    /**
     * Represents the free disk space checker used to check if the upload request is accepted. Initialized in the
     * constructor and never changed later. Not null.
     */
    private final FreeDiskSpaceChecker freeDiskSpaceChecker;

    /**
     * Creates an instance with the given free disk space checker.
     * @param freeDiskSpaceChecker
     *            the free disk space checker
     * @throws NullPointerException
     *             if the argument is null
     */
    public UploadRequestValidator(FreeDiskSpaceChecker freeDiskSpaceChecker) {
        if (freeDiskSpaceChecker == null) {
            throw new NullPointerException("freeDiskSpaceChecker is null");
        }
        this.freeDiskSpaceChecker = freeDiskSpaceChecker;
    }

    /**
     * This method validates objects of RequestMessage type. The request message must have the type equal with
     * MessageType.CHECK_UPLOAD_FILE. It uses the FreeDiskSpaceChecker to check if the upload request is accepted. It
     * will get the file size from request message's args and pass it to the FreeDiskSpaceChecker. In case an exception
     * is raised, it will return false.
     * @param obj
     *            the object to be validated
     * @return whether the object is valid or not
     * @throws NullPointerException
     *             if the argument is null
     */
    public boolean valid(Object obj) {
        if (obj == null) {
            throw new NullPointerException("the argument is null");
        }
        if (obj instanceof RequestMessage) {
            RequestMessage request = (RequestMessage) obj;
            if (MessageType.CHECK_UPLOAD_FILE.equals(request.getType())) {
                Object[] args = request.getArgs();
                // args[0]=the file name; args[1]=a Long representing the file size
                long fileSize = ((Long) args[1]).longValue();
                try {
                    return freeDiskSpaceChecker.freeDiskSpaceExceedsSize(fileSize);
                } catch (FreeDiskSpaceCheckerException e) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * This method validates an object of RequestMessage type and returns a message in case the object is not valid. If
     * it is valid, returns null. The request message must have the type equal with MessageType.CHECK_UPLOAD_FILE. It
     * uses the FreeDiskSpaceChecker to check if the upload request is accepted. It will get the file size from request
     * message's args and pass it to the FreeDiskSpaceChecker. Any exception is caught and is used to build the error
     * message.
     * @param obj
     *            the object to be validated
     * @return an error message, or null if the object is valid
     * @throws NullPointerException
     *             if the argument is null
     */
    public String getMessage(Object obj) {
        if (obj == null) {
            throw new NullPointerException("the argument is null");
        }
        String invalidObjMessage = null;
        if (obj instanceof RequestMessage && MessageType.CHECK_UPLOAD_FILE.equals(((RequestMessage) obj).getType())) {
            Object[] args = ((RequestMessage) obj).getArgs();
            long fileSize = ((Long) args[1]).longValue();
            try {
                if (!freeDiskSpaceChecker.freeDiskSpaceExceedsSize(fileSize)) {
                    invalidObjMessage = "File size exceeds disk size";
                } else {
                    return null;
                }
            } catch (FreeDiskSpaceCheckerException e) {
                invalidObjMessage = "An error occured while validating the upload request message:\n"
                        + e.getStackTrace();
            }
        } else {
            if (!(obj instanceof RequestMessage)) {
                invalidObjMessage = "the object is not type of RequestMessage";
            } else {
                invalidObjMessage = "the RequestMesage's type not equal with MessageType.CHECK_UPLOAD_FILE";
            }
        }
        return invalidObjMessage;
    }
}
