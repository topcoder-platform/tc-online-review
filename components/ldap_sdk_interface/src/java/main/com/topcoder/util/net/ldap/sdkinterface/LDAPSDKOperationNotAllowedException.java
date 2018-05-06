/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKOperationNotAllowedException.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * <p>This exception is thrown when an operation is not allowed in the kind of entry specified.</p>
 * <p>It encapsulates the following LDAP result codes: </p>
 * <ul type="disc">
 *     <li>(35) IS_LEAF: Indicates that the specified operation cannot be performed on a leaf entry.</li>
 *     <li>(66) NOT_ALLOWED_ON_NONLEAF: Indicates that the requested operation is permitted only on leaf entries.</li>
 * </ul>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKOperationNotAllowedException extends LDAPSDKException {

    /**
     * Constructs new <code>LDAPSDKOperationNotAllowedException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKOperationNotAllowedException(String message, int code) {
        super(message, code);
    }

    /**
     * Constructs new <code>LDAPSDKOperationNotAllowedException</code> with specified message, error code and an
     * original exception thrown by the underlying LDAP implementation.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @param cause the exception that was caugth before throwing this one.
     */
    public LDAPSDKOperationNotAllowedException(String message, int code, Throwable cause) {
        super(message, code, cause);
    }
}
