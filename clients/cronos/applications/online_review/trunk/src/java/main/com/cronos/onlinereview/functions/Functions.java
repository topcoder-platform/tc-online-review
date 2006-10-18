/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.functions;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;

import com.cronos.onlinereview.actions.ActionsHelper;
import com.cronos.onlinereview.actions.AuthorizationHelper;
import com.cronos.onlinereview.actions.ConfigHelper;

/**
 * This class implements several helper-functions that can be used from JSP pages.
 * <p>
 * This class is thread-safe, as it contains only static methods and has no inner state.
 * </p>
 *
 * @author George1
 * @author real_vg
 * @version 1.0
 */
public final class Functions {

    /**
     * This static member variable is a constant that holds a formatting rule to format amount of
     * minutes so that the number will be displayed with a leading zero.
     */
    private static final NumberFormat minutesFormat = new DecimalFormat("00");

    /**
     * This is the hidden constructor of the <code>Functions</code> class to prevent this class's
     * instantiation.
     */
    private Functions() {
    }

    /**
     * This static method encodes parts of text passed to it as parameter. It encodes the symbols
     * that can be interpreted as a part of markup replacing them with HTML entities. Additionally,
     * this method encodes all line terminators encountered in the input string into the following
     * character sequence: <code>&lt;br /&gt;</code>. The line terminators are the ones specified
     * in the description of the class <code>java.util.regex.Pattern</code>.
     * <p>
     * This method is an implementeation of <code>htmlEncode</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return encoded string, or empty string if parameter <code>text</code> was
     *         <code>null</code>.
     * @param text
     *            a text to encode.
     */
    public static String htmlEncode(String text) {
        if (text == null || text.length() == 0) {
            return "";
        }

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < text.length(); ++i) {
            char ch = text.charAt(i);

            if (ch == ' ' && (i + 1 != text.length())) {
                char ch2 = text.charAt(i + 1);
                if (ch2 == ' ') {
                    stringBuffer.append("&#160; ");
                    ++i;
                } else {
                    stringBuffer.append(ch);
                }
            } else if (ch == '&') {
                stringBuffer.append("&amp;");
            } else if (ch == '<') {
                stringBuffer.append("&lt;");
            } else if (ch == '>') {
                stringBuffer.append("&gt;");
            } else if (ch == '"') {
                stringBuffer.append("&#034;");
            } else if (ch == '\'') {
                stringBuffer.append("&#039;");
            } else if ((ch == '\r' || ch == '\n') && (i + 1 != text.length())) {
                char ch2 = text.charAt(i + 1);
                if ((ch == '\r' && ch2 == '\n') || (ch == '\n' && ch2 == '\r')) {
                    ++i;
                }
                stringBuffer.append("<br />");
            } else if (ch == '\n' || ch == '\r' || ch == '\u0085' || ch == '\u2029') {
                stringBuffer.append("<br />");
            } else {
                stringBuffer.append(ch);
            }
        }

        return stringBuffer.toString();
    }

    /**
     * This static method determines if the user is logged in.
     * <p>
     * This method is an implementeation of <code>isUserLoggedIn</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return <code>true</code> if the user is logged in, <code>false</code> if it doesn't, or
     *         if <code>request</code> parameter is <code>null</code>.
     * @param request
     *            an <code>HttpServletRequest</code> object. Normally, you should write the
     *            following: &quot;<code>pageContext.request</code>&quot; in a JSP page when you
     *            call this method to pass a valid object to it.
     */
    public static Boolean isUserLoggedIn(HttpServletRequest request) {
        return new Boolean((request != null) ? AuthorizationHelper.isUserLoggedIn(request) : false);
    }

    /**
     * This static method returns the External ID of the currently logged in user. The ID is
     * returned as a <code>String</code> (in text form).
     * <p>
     * This method is an implementeation of <code>getLoggedInUserId</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return the ID of the currently logged in user. If there are no user currently logged in, or
     *         the <code>request</code> parameter is <code>null</code>, the return value is the
     *         value of {@see AuthorizationHelper.NO_USER_LOGGED_IN_ID} constant (usually it is -1).
     * @param request
     *            an <code>HttpServletRequest</code> object. Normally, you should write the
     *            following: &quot;<code>pageContext.request</code>&quot; in a JSP page when you
     *            call this method to pass a valid object to it.
     */
    public static String getLoggedInUserId(HttpServletRequest request) {
        if (request == null) {
            return String.valueOf(AuthorizationHelper.NO_USER_LOGGED_IN_ID);
        }

        return String.valueOf(AuthorizationHelper.getLoggedInUserId(request));
    }

    /**
     * This static method determines whether standar errors bean from Sytruts framework has been
     * created and stored in the request specified by <code>request</code> parameter.
     * <p>
     * This method is an implementeation of <code>isErrorsPresent</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return <code>true</code> if there were errors, <code>false</code> if there weren't.
     * @param request
     *            an <code>HttpServletRequest</code> object. Normally, you should write the
     *            following: &quot;<code>pageContext.request</code>&quot; in a JSP page when you
     *            call this method to pass a valid object to it.
     */
    public static Boolean isErrorsPresent(HttpServletRequest request) {
        if (request == null) {
            return Boolean.FALSE;
        }

        return new Boolean(ActionsHelper.isErrorsPresent(request));
    }

    /**
     * This static method returns message string loaded from default message resources currently set
     * for this session or for entire application. If a message with the specified key was not found
     * in message resources, this method returns empty string.
     * <p>
     * This method is an implementeation of <code>getMessage</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return retrieved message string that corresponds the specified key.
     * @param pageContext
     *            a <code>PageContext</code> object. Normally, you should write the following:
     *            &quot;<code>pageContext</code>&quot; in a JSP page when you call this method
     *            to pass a valid object to it.
     * @param key
     *            a key that the string to retrieve is stored under.
     */
    public static String getMessage(PageContext pageContext, String key) {
        // Check that parameters are correct, and return empty string if that's not the case
        if (pageContext == null || key == null) {
            return null;
        }

        MessageResources messages = null;

        try {
            // Use a helper method from Struts framework to obtain Message Resources
            messages = TagUtils.getInstance().retrieveMessageResources(pageContext, Globals.MESSAGES_KEY, false);
        } catch (JspException e) {
            return "";
        }

        if (messages == null) {
            return "";
        }

        // Retrieve message string from Message Resources
        String message = messages.getMessage(key);
        // If the specified key exists, return a string for it, or return empty string otherwise
        return (message != null) ? message : "";
    }

    /**
     * This static method computes the amount of pixels to display for the specified time duration
     * in minutes.
     * <p>
     * This method is an implementeation of <code>getMessage</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return amount of pixels for the specified time duration.
     * @param minutes
     *            a time duration, in minutes.
     */
    public static Integer getGanttLen(Integer minutes) {
        // Return zero for incorrect input values
        if (minutes == null || minutes.intValue() < 0) {
            return new Integer(0);
        }

        // Compute the amount of pixels
        return new Integer((minutes.intValue() * ConfigHelper.getPixelsPerHour()) / 60);
    }

    /**
     * This static method returns string to display time duration, specified in minutes, in hours-minutes format.
     * <p>
     * This method is an implementeation of <code>getMessage</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return a string containing time duration in hours-minutes format.
     * @param pageContext
     *            a <code>PageContext</code> object. Normally, you should write the following:
     *            &quot;<code>pageContext</code>&quot; in a JSP page when you call this method
     *            to pass a valid object to it.
     * @param minutes
     *            a time duration, in minutes.
     */
    public static String getGanttHours(PageContext pageContext, Integer minutes) {
        // Return empty string for incorrect input values
        if (minutes == null || minutes.intValue() < 0) {
            return "";
        }

        int mins = minutes.intValue();

        // Special formatting is needed in case amount of minutes is not zero
        if (mins % 60 != 0) {
            // Compute amount of hours and minutes
            int hour = mins / 60;
            int min = mins - (hour * 60);

            // Retrieve messages
            String strHours = getMessage(pageContext, "global.hour.shortening");
            String strMinutes = getMessage(pageContext, "global.minute.shortening");

            // Form the resulting string
            return hour + strHours + " " + minutesFormat.format(min) + strMinutes;
        }

        int hours = mins / 60;
        return hours + " " + getMessage(pageContext, (hours == 1) ? "global.hour.singular" : "global.hour.plural");
    }
}
