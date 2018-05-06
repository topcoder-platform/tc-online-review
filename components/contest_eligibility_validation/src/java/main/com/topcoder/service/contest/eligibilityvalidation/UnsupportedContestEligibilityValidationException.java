/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.contest.eligibilityvalidation;

/**
 * <p>
 * UnsupportedContestEligibilityValidationException is thrown by ContestEligibilityValidationManagerBean. It
 * extends ContestEligibilityValidationManagerException.It is thrown when the corresponding
 * ContestEligibilityValidator for concrete ContestEligibility type is not supported.
 * </p>
 * <p>
 * Thread Safety: This class is not thread safe because its base class is not thread safe.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@SuppressWarnings("serial")
public class UnsupportedContestEligibilityValidationException extends
    ContestEligibilityValidationManagerException {

    /**
     * Create the exception, call parent constructor with the same signature.
     */
    public UnsupportedContestEligibilityValidationException() {
        super();
    }

    /**
     * Create the exception, call parent constructor with the same signature.
     *
     * @param message
     *            the error message to set
     */
    public UnsupportedContestEligibilityValidationException(String message) {
        super(message);
    }

    /**
     * Create the exception, call parent constructor with the same signature.
     *
     * @param cause
     *            the inner cause exception to set
     */
    public UnsupportedContestEligibilityValidationException(Throwable cause) {
        super(cause);
    }

    /**
     * Create the exception, call parent constructor with the same signature.
     *
     * @param message
     *            the error message to set
     * @param cause
     *            the cause error to set
     */
    public UnsupportedContestEligibilityValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}