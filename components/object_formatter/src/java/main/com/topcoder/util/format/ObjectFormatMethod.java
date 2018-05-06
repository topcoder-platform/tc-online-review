/*
 * ObjectFormatMethod.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;


/**
 * An interface that provides a method for formatting
 * <code>Object</code>s as strings.  Usually, an
 * <code>ObjectFormatMethod</code> will handle the formatting for a
 * single type, or a single type and its subtypes;
 * <code>ObjectFormatter</code>s are used to format many different
 * <code>Object</code> types. <p>
 *
 * @author KurtSteinkraus
 * @version 1.0
 **/
public interface ObjectFormatMethod {

    /**
     * Converts the given <code>Object</code> into an appropriate
     * string representation.
     *
     * @param obj a <code>Object</code> value
     * @return a formatted string representing <code>obj</code>
     * @throws IllegalArgumentException if this format method is not
     *         capable of formatting the supplied <code>Object</code>
     **/
    public String format(Object obj) throws IllegalArgumentException;
}
