/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.failuretests;


/**
 * <p>
 * FailureReviewerException.
 * </p>
 * 
 * @author Beijing2008
 * @version 1.0
 */
public class FailureReviewerException extends RuntimeException {
    private static final long serialVersionUID = -6373234884335765966L;
    
    public FailureReviewerException() {
        super();
    }

    public FailureReviewerException(String message) {
        super(message);
    }
    public FailureReviewerException(String message, Throwable cause) {
        super(message, cause);
    }
    public FailureReviewerException(Throwable cause) {
        super(cause);
    }
}
