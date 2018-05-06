/*
 * DefaultObjectFormatMethod.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

/**
 * A default formatter for <code>Object</code> type, which return the
 * value of the <code>toString</code> method for any Object it formats.
 * This formatter can be created in <code>FormatMethodFactory</code> 
 * using the <code>getDefaultObjectFormatMethod</code> method.<p>
 *
 * @author garyk
 * @version 1.0
 **/
class DefaultObjectFormatMethod implements ObjectFormatMethod {

    /**
     * Creates a DefaultObjectFormatMethod
     **/
    DefaultObjectFormatMethod() {}

    /**
     * Converts the given <code>Object</code> into an appropriate
     * string representation using the <code>toString</code> method 
     * for any Object it formats.
     *
     * @param obj a <code>Object</code> value
     * @return a formatted string representing <code>obj</code>
     * @throws IllegalArgumentException if this format method is not
     *         capable of formatting the supplied <code>Object</code>
     **/
    public String format(Object obj) throws IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException();
        }

        return obj.toString();
    }
}