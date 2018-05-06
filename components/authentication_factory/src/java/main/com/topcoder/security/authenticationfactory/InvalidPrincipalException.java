/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import com.topcoder.util.errorhandling.BaseRuntimeException;

/**
 * <p>
 * InvaildPrincipalException is thrown from the authenticator's authenticate method if the
 * principal is invalid,  for example, the value of a certain key is invalid, etc.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class InvalidPrincipalException extends BaseRuntimeException {
    /**
     * <p>
     * Create InvalidPrincipalException with error message.
     * </p>
     *
     * @param message the error message.
     */
    public InvalidPrincipalException(String message) {
        super(message);
    }
}
