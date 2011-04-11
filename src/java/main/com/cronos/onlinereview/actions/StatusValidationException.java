/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.project.ValidationException;

/**
 * The Class StatusValidationException.
 * Used to indicate that project's current state doesn't allow setting some specific status.
 *
 * @author rac_
 * @version 1.0
 */
public class StatusValidationException extends ValidationException {

    /** The status violation key indicates type of status violation */
    String statusViolationKey = null;

    /**
     * Instantiates a new status validation exception.
     *
     * @param message the message
     * @param statusViolationKey the status violation key
     */
    public StatusValidationException(String message, String statusViolationKey) {
        super(message);
        this.statusViolationKey = statusViolationKey;
    }

    /**
     * Instantiates a new status validation exception.
     *
     * @param message the message
     * @param throwable throwable to wrap
     * @param statusViolationKey the status violation key
     * @throws StatusValidationException the status validation exception
     */
    public StatusValidationException(String message, Throwable throwable, String statusViolationKey) throws StatusValidationException {
        super(message, throwable);
        this.statusViolationKey = statusViolationKey;
    }

    /**
     * Gets the status violation key. It says about type of status violation.
     *
     * @return the status violation key
     */
    public String getStatusViolationKey() {
        return statusViolationKey;
    }


}
