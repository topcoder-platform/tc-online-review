/*
 * PrimitiveFormatterFactory.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;


import java.util.Locale;
import java.text.DecimalFormat;


/**
 * A factory class for creating <code>PrimitiveFormatter</code>s.
 * Each factory method returns a formatter that converts each
 * primitive type into the same kind of information; the only
 * difference is how each primitive type's different size and
 * information content are handled. <p>
 *
 * @author KurtSteinkraus
 * @author garyk
 * @version 1.0
 **/
public class PrimitiveFormatterFactory {  

    /**
     * Private empty contructor to prevent this factory class from 
     * being instantiated
     **/
    private PrimitiveFormatterFactory() {}

    /**
     * Returns a formatter that converts each primitive type into its
     * plain string representation.  The plain format is what the
     * appropriate <code>String.valueOf</code> method returns.
     *
     * @return the plain formatter
     **/
    public static PrimitiveFormatter getPlainFormatter() {
        return new PlainPrimitiveFormatter();
    }
    
    /**
     * Returns a formatter that converts each primitive type into a
     * "pretty" string representation.  The formatter returned by this
     * method formats numbers according to the following default
     * format:<p>
     * <code>int</code>: ##,### <br>
     * <code>short</code>: ### <br> 
     * <code>long</code>: ##,### <br>
     * <code>byte</code>: # <br>
     * <code>char</code>: # <br>
     * <code>float</code>: ##,###.00 <br>
     * <code>doulbe</code>: ##,###.00 <br>
     *
     * @return the pretty formatter
     * @see java.text.DecimalFormat
     **/
    public static PrimitiveFormatter getPrettyFormatter() {
        return new PrettyPrimitiveFormatter();
    }
    
    /**
     * Returns a formatter that converts each primitive type into a
     * string representation according to the supplied format string.
     * This method is equivalent to calling <code>getFormatter(format,
     * null)</code>.
     *
     * @param format a format string
     * @return the specified formatter
     * @throws IllegalArgumentException if <code>format</code> is
     *         an invalid format string
     **/
    public static PrimitiveFormatter getFormatter(String format) 
            throws IllegalArgumentException {
        return getFormatter(format, null);
    }
    
    /**
     * Returns a formatter that converts each primitive type into a
     * string representation according to the supplied format string
     * and <code>DecimalFormat</code>.  Either parameter can be null,
     * in which case the appropriate default value is used. <p>
     *
     * The format string has a syntax that follows the pattern strings
     * for <code>java.text.DecimalFormat</code>.  If a
     * <code>DecimalFormat</code> is supplied, that
     * <code>DecimalFormat</code>'s number-formatting information will
     * be used to interpret the format string.  If none is supplied,
     * then the default <code>DecimalFormat</code> for the current
     * locale will be used for number-formatting information.  If no
     * format string is supplied, then the format string used is the
     * current pattern string for the supplied
     * <code>DecimalFormat</code>, or the default pattern string for
     * <code>DecimalFormat</code> if no <code>DecimalFormat</code> is
     * supplied. <p>
     *
     * Numbers are formatted as they would be by the
     * <code>format</code> methods in {@link java.text.DecimalFormat}.
     * <code>byte</code>s, <code>char</code>s, <code>short</code>s,
     * and <code>int</code>s are type-cast to <code>long</code>s, and
     * <code>float</code>s are type-cast to <code>double</code>s, to
     * do the formatting. <p>
     *
     * @param format a format string
     * @param df a <code>DecimalFormat</code> to use to format numbers
     * @return a formatter that uses <code>df</code>
     * @throws IllegalArgumentException if <code>format</code> is
     *         an invalid format string
     */
    public static PrimitiveFormatter getFormatter(String format, 
                                                  DecimalFormat df)
            throws IllegalArgumentException {
        return new PrettyPrimitiveFormatter(format, df);
    }
}