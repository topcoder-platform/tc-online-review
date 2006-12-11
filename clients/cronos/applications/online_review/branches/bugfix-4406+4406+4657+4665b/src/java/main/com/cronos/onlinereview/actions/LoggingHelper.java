/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

/**
 * This class prvides helper methods for logging some ceratin application's activity. This might
 * include writing to log informational messages, warnings, error reports as well as thrown
 * exceptions (the message and stack trace).
 * <p>
 * This class is thread safe as it contains only static methods and no inner state.
 * </p>
 * 
 * @author George1
 * @author real_vg
 * @version 1.0
 */
final class LoggingHelper {
    
    // Hidden constructor
    
    /**
     * This constructor is declared private to prohibit instantiation of the
     * <code>LoggingHelper</code> class.
     */
    private LoggingHelper() {
    }
    
    
    // Static methods
    
    /**
     * TODO: describe the purpose of the logAction static method
     */
    public static void logAction() {
        // TODO: Add appropriate logging code here
    }
    
    /**
     * This static method logs the information about the exception.
     * TODO: detarmine is the exception should be rethrown from logException static method
     * 
     * @param e
     *            exception containing the information to be logged
     */
    public static void logException(Exception e) {
        // TODO: Add logging code here for the exception and probably rethrow the exception
    }
}
