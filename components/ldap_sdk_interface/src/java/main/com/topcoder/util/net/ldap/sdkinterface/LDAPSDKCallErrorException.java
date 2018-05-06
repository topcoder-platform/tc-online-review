/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKCallErrorException.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * <p>This exception is thrown when a call to an LDAP service was unsuccesfull due to a protocol difference, a bad
 * parameter or not supported functionality.</p>
 * <p>It encapsulates the following LDAP result codes: </p>
 * <ul type="disc">
 *     <li>(2) PROTOCOL_ERROR: A LDAP server could not correctly interpret the request sent by your client because the
 *     request does not strictly comply with the LDAP protocol </li>
 *     <li>(89) PARAM_ERROR: An ldap routine was called with a bad parameter.</li>
 *     <li>(92) LDAP_NOT_SUPPORTED: Indicates that the requested functionality is not supported by the client.</li>
 * </ul>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKCallErrorException extends LDAPSDKException {

    /**
     * Constructs a new <code>LDAPSDKCallErrorException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKCallErrorException(String message, int code) {
        super(message, code);
    }

    /**
     * Constructs a new <code>LDAPSDKCallErrorException</code> with specified message, error code and an original
     * exception thrown by the underlying LDAP implementation.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @param cause the exception that was caugth before throwing this one.
     */
    public LDAPSDKCallErrorException(String message, int code, Throwable cause) {
        super(message, code, cause);
    }
}
