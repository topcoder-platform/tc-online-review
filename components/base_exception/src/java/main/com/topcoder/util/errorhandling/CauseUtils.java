/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

/**
 * <p>
 * Utility Class maintained to provide backwards compatibility with <code>Base
 * Exception 1.0</code>.
 * </p>
 * <p>
 * As this component is to be used in an environment with automatic exception
 * chaining (java 1.4 and later), this is not required, but for compatibility it
 * has been provided, its single method rewritten to match the new technology.
 * </p>
 * <p>
 * <b>Thread safety</b>: The class is stateless to ensure thread safety.
 * </p>
 *
 * @author srowen, Sleeve
 * @author sql_lall, TCSDEVELOPER
 * @version 2.0
 * @since 1.0
 */
public class CauseUtils {
    /**
     * <p>
     * Default no-arg constructor.
     * </p>
     */
    protected CauseUtils() {
        // does nothing
    }

    /**
     * <p>
     * Returns the throwable reason which caused the given exception.
     * </p>
     *
     * @param t The object whose cause is being retrieved.
     * @return The throwable cause that triggered the given throwable object.
     * @deprecated As of Base Exception 2.0, simply calling
     *             <code>Throwable</code>'s own <code>getCause()</code>
     *             method in JDK 1.4 or newer
     * @since 1.0
     */
    public static Throwable getCause(Throwable t) {
        // use t.getCause() to get cause is it is not null
        return (t == null) ? null : t.getCause();
    }
}
