/*
 * DateFormatMethod.java
 *
 * Copyright (C) 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format;

import java.util.Date;

/**
 * An interface that provides a method for formatting
 * <code>Date</code>s as strings. <p>
 *
 * @author KurtSteinkraus
 * @version 1.0
 **/
public interface DateFormatMethod extends ObjectFormatMethod {
    /**
     * Converts the given <code>Date</code> into an appropriate string
     * representation.
     *
     * @param date a <code>Date</code> value
     * @return a formatted string representing <code>date</code>
     **/
    public String format(Date date);
}
