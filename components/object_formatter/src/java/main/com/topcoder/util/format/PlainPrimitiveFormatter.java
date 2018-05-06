/*
 * PlainPrimitiveFormatter.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

/**
 * A plain formatter for primitive types.  This formatter can be 
 * created in <code>PrimitiveFormatterFactory</code> using the 
 * <code>getPlainFormatter</code> method.<p>
 *
 * @author garyk
 * @version 1.0
 **/
class PlainPrimitiveFormatter implements PrimitiveFormatter {

    /**
     * Creates a PlainPrimitiveFormatter
     **/
    PlainPrimitiveFormatter() {}

    /**
     * Converts the given <code>byte</code> into an appropriate string
     * representation using <code>String.valueOf</code> method.
     *
     * @param b a <code>byte</code> value
     * @return a formatted string representing <code>b</code>
     **/
    public String format(byte b) {
	    return String.valueOf(b);
    }
    
    /**
     * Converts the given <code>char</code> into an appropriate string
     * representation using <code>String.valueOf</code> method.
     *
     * @param ch a <code>char</code> value
     * @return a formatted string representing <code>ch</code>
     **/
    public String format(char ch) {
        return String.valueOf(ch);
    }
    
    /**
     * Converts the given <code>short</code> into an appropriate
     * string representation using <code>String.valueOf</code> method.
     *
     * @param s a <code>short</code> value
     * @return a formatted string representing <code>s</code>
     **/
    public String format(short s) {
        return String.valueOf(s);
    }
    
    /**
     * Converts the given <code>int</code> into an appropriate string
     * representation using <code>String.valueOf</code> method.
     *
     * @param i a <code>int</code> value
     * @return a formatted string representing <code>i</code>
     **/
    public String format(int i) {
        return String.valueOf(i);
    }
    
    /**
     * Converts the given <code>long</code> into an appropriate string
     * representation using <code>String.valueOf</code> method.
     *
     * @param l a <code>long</code> value
     * @return a formatted string representing <code>l</code>
     **/
    public String format(long l) {
        return String.valueOf(l);
    }
    
    /**
     * Converts the given <code>float</code> into an appropriate
     * string representation using <code>String.valueOf</code> method.
     *
     * @param r a <code>float</code> value
     * @return a formatted string representing <code>r</code>
     **/
    public String format(float r) {
        return String.valueOf(r);
    }
    
    /**
     * Converts the given <code>double</code> into an appropriate
     * string representation using <code>String.valueOf</code> method.
     *
     * @param lr a <code>double</code> value
     * @return a formatted string representing <code>lr</code>
     **/
    public String format(double lr) {
        return String.valueOf(lr);
    }
}
