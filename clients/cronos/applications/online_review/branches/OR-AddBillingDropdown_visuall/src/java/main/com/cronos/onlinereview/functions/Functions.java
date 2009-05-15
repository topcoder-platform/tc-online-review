/*
 * Copyright (C) 2006-2007 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.functions;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;

import com.cronos.onlinereview.actions.ActionsHelper;
import com.cronos.onlinereview.actions.AuthorizationHelper;
import com.cronos.onlinereview.actions.ConfigHelper;
import com.topcoder.management.project.Project;

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
     * This static method returns context of handler base projectCategory.
     *
     * <p>
     * This method is an implementeation of <code>getLoggedInUserId</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return context of handler base projectCategory.
     * @param request
     *            an <code>HttpServletRequest</code> object. Normally, you should write the
     *            following: &quot;<code>pageContext.request</code>&quot; in a JSP page when you
     *            call this method to pass a valid object to it.
     */
    public static String getHandlerContext(HttpServletRequest request) {
        if (request == null) {
            return "";
        }

        Project project = (Project) request.getAttribute("project");
        if (project == null) {
            return "";
        }

        if (project.getProjectCategory().getId() == 1) {
            return "design";
        } else if (project.getProjectCategory().getId() == 2) {
            return "development";
        } else if (project.getProjectCategory().getId() == 5) {
            return "development";
        } else {
            return "";
        }
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
     * This method is an implementeation of <code>getGanttLen</code> function used from EL
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
     * This method is an implementeation of <code>getGanttHours</code> function used from EL
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

            StringBuffer buffer = new StringBuffer();

            // Append hours value, but only if it is not zero.
            if (hour != 0) {
                buffer.append(hour);
                buffer.append(' ');
                buffer.append(getMessage(pageContext, "global.hour.shortening"));
                buffer.append(' ');
            }

            // Append minutes value
            if (hour != 0) {
                // Prepare a format for minutes
                NumberFormat minutesFormat = new DecimalFormat("00");
                // Append minutes value using prepared format
                buffer.append(minutesFormat.format(min));
            } else {
                buffer.append(min);
            }
            buffer.append(' ');
            buffer.append(getMessage(pageContext, "global.minute.shortening"));

            // Return the resulting string
            return buffer.toString();
        }

        int hours = mins / 60;
        return hours + " " + getMessage(pageContext, (hours == 1) ? "global.hour.singular" : "global.hour.plural");
    }

    /**
     * This static method converts specified double value to its string representation, rounding the
     * fractional part to two digits. This method is used to correctly display scorecard scores on
     * JSP pages.
     * <p>
     * This method is an implementeation of <code>displayScore</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return string repesentation of the score.
     * @param request
     *            an <code>HttpServletRequest</code> object, where pre-built formatting object
     *            could be stored for later reuse. Normally, you should write the following:
     *            &quot;<code>pageContext.request</code>&quot; in a JSP page when you call this
     *            method to pass a valid object to it.
     * @param score
     *            a score (double) value to convert to textual form, rounding it to two decimal
     *            digits after decimal point.
     */
    public static String displayScore(HttpServletRequest request, Double score) {
        // Return empty string for incorrect input values
        if (score == null || score.doubleValue() < 0) {
            return "";
        }

        // Try to extract a formatter from the request
        Format format = (Format) ((request != null) ? request.getAttribute("scorecard_score_format") : null);
        // If there is no such attribute stored in the request, build a new one and store it
        if (format == null) {
            format = new DecimalFormat(ConfigHelper.getScorecardScoreFormat());
            if (request != null) {
                request.setAttribute("scorecard_score_format", format);
            }
        }

        // Return converted value
        return format.format(score);
    }

    /**
     * This static method converts specified double value to its string representation, rounding the
     * fractional part to two digits. This method is used to correctly display payment amounts on
     * JSP pages.
     * <p>
     * This method is an implementeation of <code>displayPaymentAmt</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return formatted string representation of amount of the payment, not including the dollar sign.
     * @param request
     *            an <code>HttpServletRequest</code> object, where pre-built formatting object
     *            could be stored for later reuse. Normally, you should write the following:
     *            &quot;<code>pageContext.request</code>&quot; in a JSP page when you call this
     *            method to pass a valid object to it.
     * @param payment
     *            a payment amount (double) value to convert to textual form, rounding it to two decimal
     *            digits after decimal point.
     */
    public static String displayPaymentAmt(HttpServletRequest request, Double payment) {
        // Return empty string for incorrect input values
        if (payment == null || payment.doubleValue() < 0) {
            return "";
        }

        Format format = null;

        if (Math.round(payment.doubleValue() * 100) % 100 == 0) {
            // Try to extract a formatter from the request
            format = (Format) ((request != null) ? request.getAttribute("payment_amount_nf_format") : null);
            // If there is no such attribute stored in the request, build a new one and store it
            if (format == null) {
                format = new DecimalFormat(ConfigHelper.getMonetaryValueNoFracFormat());
                if (request != null) {
                    request.setAttribute("payment_amount_nf_format", format);
                }
            }
        } else {
            // Try to extract a formatter from the request
            format = (Format) ((request != null) ? request.getAttribute("payment_amount_full_format") : null);
            // If there is no such attribute stored in the request, build a new one and store it
            if (format == null) {
                format = new DecimalFormat(ConfigHelper.getMonetaryValueFullFormat());
                if (request != null) {
                    request.setAttribute("payment_amount_full_format", format);
                }
            }
        }

        // Return converted value
        return format.format(payment);
    }

    /**
     * This static method converts specified date value to its string representation, applying the
     * format set in application's configuration file. This method is used to correctly display
     * date/time values on JSP pages.
     * <p>
     * This method is an implementeation of <code>displayDate</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return string representation of the date.
     * @param request
     *            an <code>HttpServletRequest</code> object, where pre-built formatting object
     *            could be stored for later reuse. Normally, you should write the following:
     *            &quot;<code>pageContext.request</code>&quot; in a JSP page when you call this
     *            method to pass a valid object to it.
     * @param date
     *            a date value to convert to textual form.
     */
    public static String displayDate(HttpServletRequest request, Date date) {
        // Return empty string for incorrect input values
        if (date == null) {
            return "";
        }

        // Try to extract a formatter from the request
        Format format = (Format) ((request != null) ? request.getAttribute("date_format") : null);
        // If there is no such attribute stored in the request, build a new one and store it
        if (format == null) {
            format = new SimpleDateFormat(ConfigHelper.getDateFormat());
            if (request != null) {
                request.setAttribute("date_format", format);
            }
        }

        // Return converted value
        return format.format(date);
    }

    /**
     * This static method converts specified date value to its string representation, applying the
     * format set in application's configuration file. It adds <code>&lt;br /&gt;</code> tag between
     * date and time parts of the date, so that dates appear to be displayed on two lines. This
     * method is used to correctly display date/time values on JSP pages.
     * <p>
     * This method is an implementeation of <code>displayDateBr</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return string representation of the date.
     * @param request
     *            an <code>HttpServletRequest</code> object, where pre-built formatting object
     *            could be stored for later reuse. Normally, you should write the following:
     *            &quot;<code>pageContext.request</code>&quot; in a JSP page when you call this
     *            method to pass a valid object to it.
     * @param date
     *            a date value to convert to textual form.
     */
    public static String displayDateBr(HttpServletRequest request, Date date) {
        // Return empty string for incorrect input values
        if (date == null) {
            return "";
        }

        // Try to extract a date-only formatter from the request
        Format dateFormat = (Format) ((request != null) ? request.getAttribute("date_only_format") : null);
        // If there is no such attribute stored in the request, build a new one and store it
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(ConfigHelper.getDateOnlyFormat());
            if (request != null) {
                request.setAttribute("date_only_format", dateFormat);
            }
        }

        // Try to extract a time-only formatter from the request
        Format timeFormat = (Format) ((request != null) ? request.getAttribute("time_only_format") : null);
        // If there is no such attribute stored in the request, build a new one and store it
        if (timeFormat == null) {
            timeFormat = new SimpleDateFormat(ConfigHelper.getTimeOnlyFormat());
            if (request != null) {
                request.setAttribute("time_only_format", timeFormat);
            }
        }

        StringBuffer buffer = new StringBuffer();

        // Build converted date value
        buffer.append(dateFormat.format(date));
        buffer.append("<br />");
        buffer.append(timeFormat.format(date));

        // Return converted value
        return buffer.toString();
    }

    /**
     * This static method returns referer for the page calling the method. The method returns only
     * "safe" referer, i.e. it returns an address if the host part matches exactly to the host of
     * the current request, otherwise the return value is <code>null</code>.
     * <p>
     * This method is an implementeation of <code>getSafeRedirect</code> function used from EL
     * expressions in JSP pages.
     * </p>
     *
     * @return an URL of the page to return to after successful login, or <code>null</code> if
     *         there is no page to return to.
     * @param request
     *            an <code>HttpServletRequest</code> object used to actually obtain a referer
     *            from. Normally, you should write the following: &quot;<code>pageContext.request</code>&quot;
     *            in a JSP page when you call this method to pass a valid object to it.
     */
    public static String getSafeRedirect(HttpServletRequest request) {
        if (request.getSession(false) == null || request.getSession(false).isNew()) {
            return null;
        }

        String referer = (String) request.getSession().getAttribute(AuthorizationHelper.REDIRECT_BACK_URL_ATTRIBUTE);

        if (referer == null || referer.trim().length() == 0) {
            referer = request.getHeader("Referer");
        }

        if (referer == null || referer.trim().length() == 0) {
            return null;
        }

        URL refererURL;

        try {
            refererURL = new URL(referer);
        } catch (MalformedURLException mue) {
            // eat the exception and return null, since no valid address is specified in Referer header
            return null;
        }

        final String requestedUri = request.getRequestURI();
        final int servelPathPos = requestedUri.indexOf(request.getServletPath());
        final String moduleName = requestedUri.substring(0, servelPathPos);
        final int refererPort = (refererURL.getPort() >= 0) ? refererURL.getPort() : request.getServerPort();

        if (refererURL.getHost().compareToIgnoreCase(request.getServerName()) != 0 ||
                refererURL.getPath().indexOf(moduleName) != 0 ||
                (refererPort != 443 && request.getServerPort() != 443 &&
                        refererPort != request.getServerPort())) {
            // The Referer is not safe, return null
            return null;
        }

        if (refererURL.getPath().indexOf("/jsp/login.jsp") == servelPathPos ||
                refererURL.getPath().indexOf("/actions/Login.do") == servelPathPos) {
            // Don't allow redirects to the Login form
            return null;
        }

        request.getSession().setAttribute(AuthorizationHelper.REDIRECT_BACK_URL_ATTRIBUTE, referer);
        return referer;
    }
}
