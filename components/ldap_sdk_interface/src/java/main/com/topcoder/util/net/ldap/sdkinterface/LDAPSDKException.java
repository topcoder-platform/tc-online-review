/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKException.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

import com.topcoder.util.errorhandling.BaseException;
import netscape.ldap.LDAPException;

/**
 * This class represents an LDAP error. There are many classes inheriting from this class that provide a more specific
 * error. If the error is unknow, an object of this class will be thrown, in other case it will be one of its
 * descendants who will be thrown. </p>
 * <p>The class extends the Base Exception to provide chained exceptions. Depending on the LDAP implementation, when an
 * operation is executed, an exception can be thrown, or may be just a return code indicating error is given. In both
 * cases, or any other variation, the result code must be retrieved, the message error retrieved or generated, and an
 * exception of this type or a subclass must be thrown. Selecting which exception class has to be thrown is the task
 * of LDAPSDKExceptionFactory, who decides it based on the error code. </p>
 * <p>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKException extends BaseException {

    /**
     * An <code>int</code> representing the error code returned by an <code>LDAP</code> server.
     */
    private int errorCode = -1;

    /**
     * Constructs new <code>LDAPSDKException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKException(String message, int code) {
        super(message);
        this.errorCode = code;
    }

    /**
     * Constructs new <code>LDAPSDKException</code> with specified message, error code and an original exception thrown
     * by the underlying LDAP implementation.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @param cause the exception that was caugth before throwing this one.
     */
    public LDAPSDKException(String message, int code, Throwable cause) {
        super(message, cause);
        this.errorCode = code;
    }

    /**
     * Get the error message of this exception.
     *
     * @return a <code>String</code> representing the message explaining the details of this exception.
     */
    public String getMessage() {
        Throwable cause = getCause();
        if (cause != null) {
            if (cause instanceof LDAPException) {
                return ((LDAPException) cause).getLDAPErrorMessage();
            }
        }
        return super.getMessage();
    }

    /**
     * Get the error code (result code in the LDAP protocol) of this exception
     *
     * @return an <code>int</code> representing the error coder returned by the LDAP server.
     */
    public String getErrorCode() {
        return Integer.toString(errorCode);
    }
}
