/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This is a utility class that provides static methods for logging method entrance, method exit and exception using
 * Logging Wrapper component. It allows to optionally log method input/output parameters, method execution time and
 * timestamps. Exceptions are logged together with stack traces. Default level for method entrance/exit message is
 * DEBUG, for exceptions - ERROR; but both can be overridden.
 * </p>
 *
 * <p>
 * <em>API Usage: </em> A part of a sample IssuesTracker#updateIssue() method that uses most of the utilities defined
 * in this component. Note that only some methods of this utility class are demonstrated since their usage is very
 * similar.
 * <pre>
 * private final Log log = LogManager.getLog(getClass().getName());
 *
 * public boolean updateIssue(String issueName, int priority, File paramFile) throws IssueTrackingException {
 *     // Save method entrance timestamp
 *     Date enterTimestamp = new Date();
 *     // Prepare method signature that will appear in logged messages
 *     String signature = &quot;IssuesTracker#updateIssue(String issueName, int priority, File paramFile)&quot;;
 *
 *     // Log method entrance
 *     LoggingWrapperUtility.logEntrance(log, signature,
 *         new String[] {&quot;issueName&quot;, &quot;priority&quot;, &quot;paramFile&quot;},
 *         new Object[] {issueName, priority, paramFile});
 *
 *     try {
 *         // Check input arguments
 *         ParameterCheckUtility.checkNotNullNorEmpty(issueName, &quot;issueName&quot;);
 *         ParameterCheckUtility.checkPositive(priority, &quot;priority&quot;);
 *         ParameterCheckUtility.checkNotNull(paramFile, &quot;paramFile&quot;);
 *         ParameterCheckUtility.checkIsFile(paramFile, &quot;paramFile&quot;);
 *
 *         // Load additional parameters
 *         Properties properties = loadProperties(paramFile);
 *
 *         // Get user ID from the properties
 *         Long userId = PropertiesUtility.getLongProperty(properties, &quot;userId&quot;, true,
 *             IssueTrackingException.class);
 *         // Check whether user ID is positive
 *         ValidationUtility.checkPositive(userId, &quot;The property 'userId'&quot;, IssueTrackingException.class);
 *
 *         boolean result;
 *
 *         // Get cached DB connection
 *         Connection connection = getConnection();
 *
 *         try {
 *             // Retrieve issue type ID from DB
 *             Object[][] queryResult = JDBCUtility.executeQuery(connection,
 *                 &quot;SELECT id FROM issue_types WHERE name = ?&quot;,
 *                 new int[] {Types.VARCHAR},
 *                 new Object[] {issueName},
 *                 new Class&lt;?&gt;[] {Long.class},
 *                 IssueTrackingException.class);
 *
 *             result = (queryResult.length &gt; 0);
 *
 *             if (result) {
 *                 long issueTypeId = (Long) queryResult[0][0];
 *
 *                 // Update issues in DB
 *                 int updatedNum = JDBCUtility.executeUpdate(connection,
 *                     &quot;UPDATE issues SET priority = ? WHERE issue_type_id = ? AND user_id = ?&quot;,
 *                     new int[] {Types.INTEGER, Types.BIGINT, Types.INTEGER},
 *                     new Object[] {priority, issueTypeId, userId},
 *                     IssueTrackingException.class);
 *
 *                 // Commit transaction
 *                 JDBCUtility.commitTransaction(connection, IssueTrackingException.class);
 *
 *                 result = (updatedNum &gt; 0);
 *             }
 *         } catch (IssueTrackingException e) {
 *             try {
 *                 // Rollback transaction
 *                 JDBCUtility.rollbackTransaction(connection, IssueTrackingException.class);
 *             } catch (IssueTrackingException e2) {
 *                 // Log exception at WARN level
 *                 LoggingWrapperUtility.logException(log, signature, e2, false, Level.WARN);
 *
 *                 // Ignore this exception
 *             }
 *             throw e;
 *         } finally {
 *             releaseConnection(connection);
 *         }
 *
 *         // Log method exit
 *         LoggingWrapperUtility.logExit(log, signature, new Object[] {result}, enterTimestamp);
 *
 *         return result;
 *     } catch (IllegalArgumentException e) {
 *         // Log and re-throw the exception
 *         throw LoggingWrapperUtility.logException(log, signature, e);
 *     } catch (IssueTrackingException e) {
 *         // Log and re-throw the exception
 *         throw LoggingWrapperUtility.logException(log, signature, e);
 *     }
 * }
 * </pre>
 *
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is immutable and thread safe when array parameters passed to it are
 * used by the caller in thread safe manner.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0
 */
public class LoggingWrapperUtility {
    /**
     * <p>
     * The format of timestamps to be put to log messages.
     * </p>
     *
     * <p>
     * Is initialized during class loading and never changed after that. Is used in logEntrance(), logExit() and
     * logException().
     * </p>
     */
    private static final String TIMESTAMP_FORMAT = "[yyyy-MM-dd HH:mm:ss] ";

    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private LoggingWrapperUtility() {
        // Empty
    }

    /**
     * <p>
     * Logs the method entrance together with input parameters (if present). It's assumed that paramNames and
     * paramValues contain the same number of elements.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> When this method is used, timestamp is not logged explicitly and DEBUG level is used.
     * </p>
     *
     * @param log
     *            the logger to be used (null if logging is not required to be performed).
     * @param paramValues
     *            the values of input parameters.
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param paramNames
     *            the names of input parameters (null of method doesn't accept any parameters).
     */
    public static void logEntrance(Log log, String signature, String[] paramNames, Object[] paramValues) {
        logEntrance(log, signature, paramNames, paramValues, false, Level.DEBUG);
    }

    /**
     * <p>
     * Logs the method entrance together with input parameters (if present) and timestamp (optionally). It's assumed
     * that paramNames and paramValues contain the same number of elements.
     * </p>
     *
     * @param level
     *            the logging level to be used.
     * @param log
     *            the logger to be used (null if logging is not required to be performed).
     * @param paramValues
     *            the values of input parameters.
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param addTimestamp
     *            true if timestamp must be added to the logged message, false otherwise.
     * @param paramNames
     *            the names of input parameters (null of method doesn't accept any parameters).
     */
    public static void logEntrance(Log log, String signature, String[] paramNames, Object[] paramValues,
        boolean addTimestamp, Level level) {
        if (log == null) {
            // No Logging
            return;
        }

        log.log(level, getMessage(LoggingUtilityHelper.getMethodEntranceMessage(signature), addTimestamp));
        if (paramNames != null) {
            log.log(level, LoggingUtilityHelper.getInputParametersMessage(paramNames, paramValues));
        }
    }

    /**
     * <p>
     * Logs the method exit together with the returned value (if present).
     * </p>
     *
     * <p>
     * <em>NOTE: </em> When this method is used, timestamp is not logged explicitly, method execution time is not
     * logged and DEBUG level is used.
     * </p>
     *
     * @param log
     *            the logger to be used (null if logging is not required to be performed).
     * @param value
     *            the value returned from the method (should contain 1 element with the returned value, or should be
     *            null if the method returns void).
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     */
    public static void logExit(Log log, String signature, Object[] value) {
        logExit(log, signature, value, null);
    }

    /**
     * <p>
     * Logs the method exit together with the returned value (if present) and method execution time.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> When this method is used, timestamp is not logged explicitly and DEBUG level is used.
     * </p>
     *
     * @param log
     *            the logger to be used (null if logging is not required to be performed).
     * @param value
     *            the value returned from the method (should contain 1 element with the returned value, or should be
     *            null if the method returns void).
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param entranceTimestamp
     *            the method entrance timestamp (null if not available), is used for calculating method execution
     *            time.
     */
    public static void logExit(Log log, String signature, Object[] value, Date entranceTimestamp) {
        logExit(log, signature, value, entranceTimestamp, false, Level.DEBUG);
    }

    /**
     * <p>
     * Logs the method exit together with the returned value (if present) and method execution time.
     * </p>
     *
     * @param level
     *            the logging level to be used.
     * @param log
     *            the logger to be used (null if logging is not required to be performed).
     * @param value
     *            the value returned from the method (should contain 1 element with the returned value, or should be
     *            null if the method returns void).
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param addTimestamp
     *            true if timestamp must be added to the logged message, false otherwise.
     * @param entranceTimestamp
     *            the method entrance timestamp (null if not available), is used for calculating method execution
     *            time.
     */
    public static void logExit(Log log, String signature, Object[] value, Date entranceTimestamp,
        boolean addTimestamp, Level level) {
        if (log == null) {
            // No Logging
            return;
        }

        log.log(level,
            getMessage(LoggingUtilityHelper.getMethodExitMessage(signature, entranceTimestamp), addTimestamp));
        if (value != null) {
            log.log(level, LoggingUtilityHelper.getOutputValueMessage(value[0]));
        }
    }

    /**
     * <p>
     * Logs the given exception.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> When this method is used, timestamp is not logged explicitly and ERROR level is used.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be logged and returned.
     * @param log
     *            the logger to be used (null if logging is not required to be performed).
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName) where the exception is
     *            logged.
     * @param exception
     *            the exception to be logged (assumed to be not null).
     *
     * @return the passed in exception.
     */
    public static <T extends Throwable> T logException(Log log, String signature, T exception) {
        return logException(log, signature, exception, false, Level.ERROR);
    }

    /**
     * <p>
     * Logs the given exception together with timestamp (optionally).
     * </p>
     *
     * @param <T>
     *            the type of the exception to be logged and returned.
     * @param level
     *            the logging level to be used.
     * @param log
     *            the logger to be used (null if logging is not required to be performed).
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName) where the exception is
     *            logged.
     * @param exception
     *            the exception to be logged (assumed to be not null).
     * @param addTimestamp
     *            true if timestamp must be added to the logged message, false otherwise.
     *
     * @return the passed in exception.
     */
    public static <T extends Throwable> T logException(Log log, String signature, T exception, boolean addTimestamp,
        Level level) {
        if (log != null) {
            log.log(level, getMessage(LoggingUtilityHelper.getExceptionMessage(signature, exception), addTimestamp));
        }

        return exception;
    }

    /**
     * <p>
     * Adds timestamp to the message if needed.
     * </p>
     *
     * @param message
     *            the message.
     * @param addTimestamp
     *            true if timestamp must be added to the logged message, false otherwise.
     *
     * @return the message to be logged.
     */
    private static String getMessage(String message, boolean addTimestamp) {
        if (addTimestamp) {
            return new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date()) + message;
        }

        return message;
    }
}
