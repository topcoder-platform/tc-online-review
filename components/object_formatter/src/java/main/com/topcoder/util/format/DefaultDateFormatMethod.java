/*
 * DefaultDateFormatMethod.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * A formatter for <code>Date</code> type.  This formatter can be 
 * created in <code>FormatMethodFactory</code> using the 
 * <code>getDefaultDateFormatMethod</code> method.<p>
 *
 * The format strings for <code>Date</code>s have a syntax that
 * follows the date and time pattern strings for
 * <code>java.text.SimpleDateFormat</code>. A <code>SimpleDateFormat</code> 
 * can be supplied in addition to a format string, in which case that 
 * <code>SimpleDateFormat</code>'s date-formatting information will be 
 * used.  If none is supplied, then the default 
 * <code>SimpleDateFormat</code> for the current locale will be used for 
 * date-formatting information.  If no format string is supplied, then the 
 * format string used is the current pattern string for the supplied 
 * <code>SimpleDateFormat</code> (or the default pattern string for 
 * <code>SimpleDateFormat</code> if no <code>SimpleDateFormat</code> is 
 * supplied). <p>

 *
 * @author garyk
 * @version 1.0
 **/
class DefaultDateFormatMethod implements DateFormatMethod {
    private String format; // the format string

    /* the SimpleDateFormat to use to format numbers */
    private SimpleDateFormat sdf; 

    /* the default format */
    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

    /**
     * Creates a DefaultDateFormatMethod   
     **/
    DefaultDateFormatMethod() {
        this(DEFAULT_DATE_FORMAT, null);
    }

    /**
     * Creates a DefaultDateFormatMethod using the given format string and 
     * <code>SimpleDateFormat</code>. Either parameter can be null,
     * in which case the appropriate default value is used. <p>
     *
     * @param format a format string
     * @param sdf a <code>SimpleDateFormat</code> to use to format dates
     * @throws IllegalArgumentException if <code>format</code> is
     *         an invalid format string
     **/
    DefaultDateFormatMethod(String format, SimpleDateFormat sdf)
            throws IllegalArgumentException {
        this.format = format;

        if (format == null && sdf == null) {
            this.sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        } else if (format == null) {
            this.sdf = sdf;
        } else if (sdf == null) {
            this.sdf = new SimpleDateFormat(format);
        } else {
            this.sdf = (SimpleDateFormat) sdf.clone();
            this.sdf.applyPattern(format);
        }
    }

    /**
     * Converts the given <code>Date</code> into an appropriate string
     * representation.
     *
     * @param date a <code>Date</code> value
     * @return a formatted string representing <code>date</code>
     **/
    public String format(Date date) {
        return sdf.format(date);
    }

    /**
     * Converts the given <code>Object</code> into an appropriate
     * string representation.
     *
     * @param obj a <code>Object</code> value
     * @return a formatted string representing <code>obj</code>
     * @throws IllegalArgumentException if this format method is not
     *         capable of formatting the supplied <code>Object</code>
     **/
    public String format(Object obj) throws IllegalArgumentException {
        if (!(obj instanceof Date)) {
            throw new IllegalArgumentException("expected Date");
        }

        return format((Date)obj);	
    }
}