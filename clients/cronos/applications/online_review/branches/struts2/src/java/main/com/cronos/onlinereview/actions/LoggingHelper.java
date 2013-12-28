/*
 * Copyright (C) 2006-2013 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;


/**
 * This class provides helper methods for logging some certain application's activity. This might
 * include writing to log informational messages, warnings, error reports as well as thrown
 * exceptions (the message and stack trace).
 * <p>
 * This class is thread safe as it contains only static methods and no inner state.
 * </p>
 * 
 * @author George1, real_vg, gjw99
 * @version 1.1
 */
final class LoggingHelper {
    private static Log logger = LogManager.getLog("OnlineReview");
    private static java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    // Hidden constructor
    
    /**
     * This constructor is declared private to prohibit instantiation of the
     * <code>LoggingHelper</code> class.
     */
    private LoggingHelper() {
    }
    
    
    // Static methods
    
    /**
     * Log All incoming requests.
     * 
     * Logging must comprise user ID, timestamp, action taken, and entity IDs effected
     */
    public static void logAction(HttpServletRequest request) {
    	String action = request.getParameter("method");
    	String subAction = request.getParameter("action");
    	String pid = request.getParameter("pid");
    	String rid = request.getParameter("rid");
    	String sid = request.getParameter("sid");
    	long uid = AuthorizationHelper.getLoggedInUserId(request);

        String handle = "";
        try
        {
            UserRetrieval usrMgr = ActionsHelper.createUserRetrieval(request);
            // Get External User object for the currently logged in user
            ExternalUser extUser = usrMgr.retrieveUser(AuthorizationHelper.getLoggedInUserId(request));
            handle = extUser.getHandle();
        } catch (Exception e) {
            logger.log(Level.ERROR, "Unable to retrieve current user handle.");
        }
       
        String servletPath = request.getContextPath() + request.getServletPath();
        String query = request.getQueryString();
        String queryString = (query == null) ? ("") : ("?" + query);
        StringBuilder buf = new StringBuilder(200);
        buf.append(request.getScheme()+"://");        
        buf.append(request.getServerName());
        buf.append(servletPath);
        buf.append(queryString);
        String requestString = buf.toString();

        StringBuilder loginfo = new StringBuilder(100);
        loginfo.append("[* ");
        loginfo.append(handle);
        loginfo.append(" * ");
        loginfo.append(request.getRemoteAddr());
        loginfo.append(" * ");
        loginfo.append(request.getMethod());
        loginfo.append(" ");
        loginfo.append(requestString);
        loginfo.append(" *]");
        logger.log(Level.INFO, loginfo.toString());

    	StringBuilder sb = new StringBuilder();
    	sb.append(dateFormat.format(new Date())).append(" - ");
    	if (uid != AuthorizationHelper.NO_USER_LOGGED_IN_ID) {
    		sb.append("  User ID : ");
                sb.append(uid);
    	}
    	if (action != null) {
    		sb.append("  Action : ").append(action);
    	}
    	if (subAction != null) {
    		sb.append("[").append(subAction).append(']');
    	}
    	if (pid != null) {
    		sb.append("  Project ID : ").append(pid);
    	}
    	if (rid != null) {
    		sb.append("  Review ID : ").append(rid);
    	}
    	if (sid != null) {
    		sb.append("  Submission ID : ").append(sid);
    	}
    	logger.log(Level.INFO, sb.toString());
    }
    
    /**
     * This static method logs the information about the exception.
     * TODO: determine is the exception should be rethrown from logException static method
     *
     * @param message
     *            the error message to be logged
     * @param e
     *            exception containing the information to be logged
     */
    public static void logException(String message, Exception e) {
    	logger.log(Level.ERROR, e, message);
    }
    
    /**
     * This static method logs the information about the error.
     * 
     * @param error the error message to be logged
     * @since 1.1
     */
    public static void logError(String error) {
    	logger.log(Level.ERROR, error);
    }
}
