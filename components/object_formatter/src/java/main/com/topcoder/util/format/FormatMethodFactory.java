/*
 * FormatMethodFactory.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * A factory class for creating standard format methods. Most of these
 * factory methods take parameters that indicate how formatting should
 * occur. <p>
 *
 * <b>Formatting dates:</b> <p>
 *
 * The format strings for <code>Date</code>s have a syntax that
 * follows the date and time pattern strings for
 * <code>java.text.SimpleDateFormat</code>.  A
 * <code>SimpleDateFormat</code> can be supplied in addition to a
 * format string, in which case that <code>SimpleDateFormat</code>'s
 * date-formatting information will be used.  If none is supplied,
 * then the default <code>SimpleDateFormat</code> for the current
 * locale will be used for date-formatting information.  If no format
 * string is supplied, then the format string used is the current
 * pattern string for the supplied <code>SimpleDateFormat</code> (or
 * the default pattern string for <code>SimpleDateFormat</code> if no
 * <code>SimpleDateFormat</code> is supplied). <p>
 *
 * @see java.text.SimpleDateFormat
 *
 * @author KurtSteinkraus
 * @author garyk
 * @version 1.0
 **/
public class FormatMethodFactory {

    /**
     * Private empty contructor to prevent this factory class from 
     * being instantiated
     **/
    private FormatMethodFactory() {}

    /**
     * Gets a <code>Date</code> format method that will format dates
     * according to the supplied format string.  The syntax of the
     * format string is given above, in the class javadoc.  The
     * current default locale is assumed.  If <code>format ==
     * null</code>, then the default formatting string "MM/dd/yyyy"
	 * is used.
     *
     * @param format the formatting string
     * @return a class to format <code>Date</code>s
     * @throws IllegalArgumentException if <code>format</code> is
     *         an invalid format string
     */
    public static DateFormatMethod getDefaultDateFormatMethod(String format)
            throws IllegalArgumentException {
        return getDefaultDateFormatMethod(format, null);
    }
    
    /**
     * Gets a <code>Date</code> format method that will format dates
     * according to the supplied format string and the supplied
     * <code>SimpleDateFormat</code>.  The syntax of the format string
     * is given above, in the class javadoc.  If <code>format ==
     * null</code>, then <code>sdf</code>'s current formatting string
     * is used.
     *
     * @param format the formatting string
     * @return a class to format <code>Date</code>s
     * @throws IllegalArgumentException if <code>format</code> is
     *         an invalid format string
     */
    public static DateFormatMethod getDefaultDateFormatMethod(String format,
            SimpleDateFormat sdf) throws IllegalArgumentException { 
        return new DefaultDateFormatMethod(format, sdf); 
    }
    
    /**
     * Gets a default method for formatting Objects.  The formatter
     * simply returns the value of the <code>toString</code> method
     * for any Object it formats.
     *
     * @return a class that trivially formats Objects
     */
    public static ObjectFormatMethod getDefaultObjectFormatMethod() { 
        return new DefaultObjectFormatMethod();
    }
}