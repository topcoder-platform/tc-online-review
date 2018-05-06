/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

/**
 * <p>
 * Thrown to indicate that a misconfiguration was encountered.
 * </p>
 *
 * <p>
 * <b>Thread Safety</b>: This class is thread safe.
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class ConfigurationException extends CalculationException {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new ConfigurationException, with the specified details.
     *
     * @param   details
     *          A message describing what caused the exception to be thrown.
     */
    public ConfigurationException(String details) {
        super(details);
    }

    /**
     * Creates a new ConfigurationException, with the specified details and root cause.
     *
     * @param   details
     *          A message describing what caused the exception to be thrown.
     * @param   cause
     *          The exception that prompted this exception to be thrown.
     */
    public ConfigurationException(String details, Throwable cause) {
        super(details, cause);
    }
}
