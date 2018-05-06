/*
 * PrettyPrimitiveFormatter.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

import java.text.DecimalFormat;

/**
 * A "pretty" formatter for primitive types.  This formatter can be 
 * created in <code>PrimitiveFormatterFactory</code> using the 
 * <code>getPrettyFormatter</code> method.<p>
 *
 * @author garyk
 * @version 1.0
 **/
class PrettyPrimitiveFormatter implements PrimitiveFormatter {
    private String format; // the format string

	/* the DecimalFormats for primitive types */
    private DecimalFormat intDF;
    private DecimalFormat shortDF;
    private DecimalFormat longDF;
    private DecimalFormat byteDF;
    private DecimalFormat charDF;
    private DecimalFormat floatDF;
    private DecimalFormat doubleDF;   

    /* the default formats */
    private static final String DEFAULT_INTEGER_FORMAT = "##,###";
    private static final String DEFAULT_SHORT_FORMAT = "###";
    private static final String DEFAULT_LONG_FORMAT = "##,###";
    private static final String DEFAULT_BYTE_FORMAT = "#";
    private static final String DEFAULT_CHAR_FORMAT = "#";
    private static final String DEFAULT_FLOAT_FORMAT = "##,###.00";
    private static final String DEFAULT_DOUBLE_FORMAT = "##,###.00";

    /**
     * Creates a PrettyPrimitiveFormatter using the default pattern string and 
     * the default <code>DecimalFormat</code> for the current locale 
     **/
    PrettyPrimitiveFormatter() {
        this(null, null);
    }

    /**
     * Creates a PrettyPrimitiveFormatter using the given format string and 
     * <code>DecimalFormat</code>. Either parameter can be null,
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
     * @param format a format string
     * @param df a <code>DecimalFormat</code> to use to format numbers
     * @throws IllegalArgumentException if <code>format</code> is
     *         an invalid format string
     **/
    PrettyPrimitiveFormatter(String format, DecimalFormat df)
            throws IllegalArgumentException {
        this.format = format;

        if (format == null && df == null) {
            /* initialize the DecimalFormats for primitive types */
            byteDF = new DecimalFormat(DEFAULT_BYTE_FORMAT);
            charDF = new DecimalFormat(DEFAULT_CHAR_FORMAT);
            shortDF = new DecimalFormat(DEFAULT_SHORT_FORMAT);
            intDF = new DecimalFormat(DEFAULT_INTEGER_FORMAT);
            longDF = new DecimalFormat(DEFAULT_LONG_FORMAT);
            floatDF = new DecimalFormat(DEFAULT_FLOAT_FORMAT);
            doubleDF = new DecimalFormat(DEFAULT_DOUBLE_FORMAT);
        } else {
            if (format == null) {
                intDF = df;
            } else if (df == null) {
                intDF = new DecimalFormat(format);
            } else {
                intDF = (DecimalFormat)df.clone();
                intDF.applyPattern(format);
            }

            shortDF = longDF = byteDF = charDF = floatDF = doubleDF = intDF;
        }                    
    }

    /**
     * Converts the given <code>byte</code> into an appropriate string
     * representation.
     *
     * @param b a <code>byte</code> value
     * @return a formatted string representing <code>b</code>
     **/
    public String format(byte b) {
        return byteDF.format(b);
    }
    
    /**
     * Converts the given <code>char</code> into an appropriate string
     * representation.
     *
     * @param ch a <code>char</code> value
     * @return a formatted string representing <code>ch</code>
     **/
    public String format(char ch) {
        return charDF.format(ch);
    }
    
    /**
     * Converts the given <code>short</code> into an appropriate
     * string representation.
     *
     * @param s a <code>short</code> value
     * @return a formatted string representing <code>s</code>
     **/
    public String format(short s) {
        return shortDF.format(s);
    }
    
    /**
     * Converts the given <code>int</code> into an appropriate string
     * representation.
     *
     * @param i a <code>int</code> value
     * @return a formatted string representing <code>i</code>
     **/
    public String format(int i) {
        return intDF.format(i);
    }
    
    /**
     * Converts the given <code>long</code> into an appropriate string
     * representation.
     *
     * @param l a <code>long</code> value
     * @return a formatted string representing <code>l</code>
     **/
    public String format(long l) {
        return longDF.format(l);
    }
    
    /**
     * Converts the given <code>float</code> into an appropriate
     * string representation.
     *
     * @param r a <code>float</code> value
     * @return a formatted string representing <code>r</code>
     **/
    public String format(float r) {
        return floatDF.format(r);
    }
    
    /**
     * Converts the given <code>double</code> into an appropriate
     * string representation.
     *
     * @param lr a <code>double</code> value
     * @return a formatted string representing <code>lr</code>
     **/
    public String format(double lr) {
        return doubleDF.format(lr);
    }
}