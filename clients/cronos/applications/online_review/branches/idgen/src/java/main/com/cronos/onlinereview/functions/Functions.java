/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.functions;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.actions.AuthorizationHelper;

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
     *            call this method to pass it valid object.
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
     *            call this method to pass it valid object.
     */
    public static String getLoggedInUserId(HttpServletRequest request) {
        if (request == null) {
            return String.valueOf(AuthorizationHelper.NO_USER_LOGGED_IN_ID);
        }

        return String.valueOf(AuthorizationHelper.getLoggedInUserId(request));
    }
}
