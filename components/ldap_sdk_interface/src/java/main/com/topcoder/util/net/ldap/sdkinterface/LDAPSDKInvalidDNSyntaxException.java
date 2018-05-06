/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSKInvalidDNSyntaxException.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * <p>This exception is thrown when the DN sytnax is invalid</p>
 * <p>It encapsulates the following LDAP result codes: </p>
 * <ul type="disc">
 *     <li>(34) INVALID_DN_SYNTAX: The specified distinguished name (DN) uses invalid syntax.</li>
 * </ul>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKInvalidDNSyntaxException extends LDAPSDKException {

    /**
     * Constructs new <code>LDAPSDKInvalidDNSyntaxException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKInvalidDNSyntaxException(String message, int code) {
        super(message, code);
    }

    /**
     * Constructs new <code>LDAPSDKInvalidDNSyntexException</code> with specified message, error code and an original
     * exception thrown by the underlying LDAP implementation.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @param cause the exception that was caugth before throwing this one.
     */
    public LDAPSDKInvalidDNSyntaxException(String message, int code, Throwable cause) {
        super(message, code, cause);
    }
}
