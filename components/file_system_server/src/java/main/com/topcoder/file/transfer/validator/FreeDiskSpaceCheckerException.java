/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.validator;

import com.topcoder.util.errorhandling.BaseException;

/**
 * This exception is thrown by the FreeDiskSpaceChecker if an error occurs while performing the check.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FreeDiskSpaceCheckerException extends BaseException {

    /**
     * Creates an instance with the given argument. Calls super(message).
     * @param message
     *            a descriptive message
     */
    public FreeDiskSpaceCheckerException(String message) {
        super(message);
    }

    /**
     * Creates an instance with the given arguments. Calls super(message,cause).
     * @param message
     *            a descriptive message
     * @param cause
     *            the cause exception
     */
    public FreeDiskSpaceCheckerException(String message, Exception cause) {
        super(message, cause);
    }
}
