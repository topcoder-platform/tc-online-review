/*
 * PrimitiveFormatter.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.format;

import java.util.Date;


/**
 * A formatter for primitive types.  Instances of
 * <code>PrimitiveFormatter</code> are usually created using a factory
 * method from {@link PrimitiveFormatterFactory}. <p>
 *
 * @author KurtSteinkraus
 * @version 1.0
 **/
public interface PrimitiveFormatter {   
    /**
     * Converts the given <code>byte</code> into an appropriate string
     * representation.
     *
     * @param b a <code>byte</code> value
     * @return a formatted string representing <code>b</code>
     **/
    public String format(byte b);
    
    /**
     * Converts the given <code>char</code> into an appropriate string
     * representation.
     *
     * @param ch a <code>char</code> value
     * @return a formatted string representing <code>ch</code>
     **/
    public String format(char ch);
    
    /**
     * Converts the given <code>short</code> into an appropriate
     * string representation.
     *
     * @param s a <code>short</code> value
     * @return a formatted string representing <code>s</code>
     **/
    public String format(short s);
    
    /**
     * Converts the given <code>int</code> into an appropriate string
     * representation.
     *
     * @param i a <code>int</code> value
     * @return a formatted string representing <code>i</code>
     **/
    public String format(int i);
    
    /**
     * Converts the given <code>long</code> into an appropriate string
     * representation.
     *
     * @param l a <code>long</code> value
     * @return a formatted string representing <code>l</code>
     **/
    public String format(long l);
    
    /**
     * Converts the given <code>float</code> into an appropriate
     * string representation.
     *
     * @param r a <code>float</code> value
     * @return a formatted string representing <code>r</code>
     **/
    public String format(float r);
    
    /**
     * Converts the given <code>double</code> into an appropriate
     * string representation.
     *
     * @param lr a <code>double</code> value
     * @return a formatted string representing <code>lr</code>
     **/
    public String format(double lr);
}
