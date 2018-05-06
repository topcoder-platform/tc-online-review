/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKAccessDeniedException.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * <p>This exception is thrown when the operation could not be completed due insuficient access rights.</p>
 * <p>It encapsulates the following LDAP result codes: </p>
 * <ul type="disc">
 *     <li>(49) INVALID_CREDENTIALS: The credentials presented to the server for authentication are not valid.
 *     (For example, the password sent to the server does not match the user's password in the directory.)</li>
 *     <li>(50) INSUFFICIENT_ACCESS_RIGHTS: The client is authenticated as a user who does not have the access
 *     privileges to perform this operation.</li>
 * </ul>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKAccessDeniedException extends LDAPSDKException {

    /**
     * Constructs new <code>LDAPSDKAccessDeniedException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKAccessDeniedException(String message, int code) {
        super(message, code);
    }

    /**
     * Constructs new <code>LDAPSDKAccessDeniedException</code> with specified message, error code and an original
     * exception thrown by the underlying LDAP implementation.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @param cause the exception that was caugth before throwing this one.
     */
    public LDAPSDKAccessDeniedException(String message, int code, Throwable cause) {
        super(message, code, cause);
    }
}
