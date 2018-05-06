/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKInvalidFilterException.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * <p>This exception is thrown when the filter for the search is invalid.</p>
 * <p>It encapsulates the following LDAP result codes: </p>
 * <ul type="disc">
 *     <li>(18) INAPPROPRIATE_MATCHING: Filter type not supported for the specified attribute. </li>
 * </ul>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKInvalidFilterException extends LDAPSDKException {

    /**
     * Constructs new <code>LDAPSDKInvalidFilterException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKInvalidFilterException(String message, int code) {
        super(message, code);
    }

    /**
     * Constructs new <code>LDAPSDKInvalidFilterException</code> with specified message, error code and an original
     * exception thrown by the underlying LDAP implementation.
     *
     * @param message message the error message.
     * @param code code result code from the LDAP specification.
     * @param cause cause the exception that was caugth before throwing this one.
     */
    public LDAPSDKInvalidFilterException(String message, int code, Throwable cause) {
        super(message, code, cause);
    }
}
