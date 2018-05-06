/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKAuthenticationMethodException.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * <p>This exception is thrown when the server is expecting a different authentication method or there is an error in
 * the way of authenticating. </p>
 * <p>It encapsulates the following LDAP result codes: </p>
 * <ul type="disc">
 *     <li>(7) AUTH_METHOD_NOT_SUPPORTED: The specified authentication method is not supported by the LDAP server that
 *     you are connecting to.</li>
 *     <li>(8) STRONG_AUTH_REQUIRED: A stronger authentication method is required by the LDAP server that you are
 *     connecting to.</li>
 *     <li>(13) CONFIDENTIALITY_REQUIRED: A secure connection is required for this operation.</li>
 *     <li>(14) SASL_BIND_IN_PROGRESS: While authenticating your client by using a SASL (Simple Authentication Security
 *     Layer) mechanism, the server requires the client to send a new SASL bind request (specifying the same SASL
 *     mechanism) to continue the authentication process.</li>
 *     <li>(48) INAPPROPRIATE_AUTHENTICATION: The authentication presented to the server is inappropriate. This result
 *     code might occur, for example, if your client presents a password and the corresponding entry has no
 *     userpassword attribute.</li>
 * </ul>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKAuthenticationMethodException extends LDAPSDKException {

    /**
     * Constructs a new <code>LDAPSDKAuthenticationMethoddException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKAuthenticationMethodException(String message, int code) {
        super(message, code);
    }

    /**
     * Constructs a new <code>LDAPSDKAuthenticationMethodException</code> with specified message, error code and an
     * original exception thrown by the underlying LDAP implementation.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @param cause the exception that was caugth before throwing this one.
     */
    public LDAPSDKAuthenticationMethodException(String message, int code, Throwable cause) {
        super(message, code, cause);
    }
}
