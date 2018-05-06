/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKLimitsExceededException.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * <p>This exception is thrown when processing time or maximum entries found in the server exceeded its limits.</p>
 * <p>It encapsulates the following LDAP result codes: </p>
 * <ul type="disc">
 *     <li>(3) TIME_LIMIT_EXCEEDED: The search operation could not be completed within the maximum time limit.</li>
 *     <li>(4) SIZE_LIMIT_EXCEEDED: The search found more than the maximum number of results</li>
 *     <li>(11) ADMIN_LIMIT_EXCEEDED: The adminstrative limit on the maximum number of entries to return was exceeded
 *     </li>
 *     <li>(85) LDAP_TIMEOUT: Indicates that the time limit of the LDAP client was exceeded while waiting for a result.
 *     </li>
 * </ul>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKLimitsExceededException extends LDAPSDKException {

    /**
     * Constructs new <code>LDAPSDKLimitsExceededException</code> with specified message and error code.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     */
    public LDAPSDKLimitsExceededException(String message, int code) {
        super(message, code);
    }

    /**
     * Constructs new <code>LDAPSDKLimitsExceededException</code> with specified message, error code and an original
     * exception thrown by the underlying LDAP implementation.
     *
     * @param message the error message.
     * @param code result code from the LDAP specification.
     * @param cause he exception that was caugth before throwing this one.
     */
    public LDAPSDKLimitsExceededException(String message, int code, Throwable cause) {
        super(message, code, cause);
    }
}
