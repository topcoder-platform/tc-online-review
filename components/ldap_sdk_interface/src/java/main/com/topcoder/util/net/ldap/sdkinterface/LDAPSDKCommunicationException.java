/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKCommunicationException.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * <p>This exception is thrown when an operation was unsuccesfull because of an error in the communication with the
 * server or in the server. It encapsulates the following LDAP result codes: </p>
 * <ul type="disc">
 *     <li>(1) OPERATION_ERROR: An internal error occurred in the LDAP server. </li>
 *     <li>(51) BUSY: The LDAP server is busy. </li>
 *     <li>(52) UNAVAILABLE: The LDAP server is unavailable. </li>
 *     <li>(53) UNWILLING_TO_PERFORM: The LDAP server is unable to perform the specified operation. </li>
 *     <li>(81) SERVER_DOWN: Indicates that the LDAP libraries cannot establish an initial connection with the LDAP
 *     server </li>
 *     <li>(91) CONNECT_ERROR: Indicates that the LDAP client has lost either its connection or cannot establish a
 *     connection to the LDAP server. </li>
 * </ul>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKCommunicationException extends LDAPSDKException {

    /**
     * Constructs a new <code>LDAPSDKCommunicationException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKCommunicationException(String message, int code) {
        super(message, code);
    }

    /**
     * Constructs a new <code>LDAPSDKCommunicationException</code> with specified message, error code and an original
     * exception thrown by the underlying LDAP implementation.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @param cause the exception that was caugth before throwing this one.
     */
    public LDAPSDKCommunicationException(String message, int code, Throwable cause) {
        super(message, code, cause);
    }
}
