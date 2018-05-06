/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSKNoSuchObjectException.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * <p>This exception is thrown when an entry was not found.</p>
 * <p>It encapsulates the following LDAP result codes: </p>
 * <ul type="disc">
 *     <li>(32) NO_SUCH_OBJECT: The entry specified in the request does not exist.</li>
 * </ul>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKNoSuchObjectException extends LDAPSDKException {

    /**
     * Constructs new <code>LDAPSDKNoSuchObjectException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKNoSuchObjectException(String message, int code) {
        super(message, code);
    }

    /**
     * Constructs new <code>LDAPSDKNoSuchObjectException</code> with specified message, error code and an original
     * exception thrown by the underlying LDAP implementation.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @param cause the exception that was caugth before throwing this one.
     */
    public LDAPSDKNoSuchObjectException(String message, int code, Throwable cause) {
        super(message, code, cause);
    }
}
