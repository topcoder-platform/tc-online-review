/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * <p>
 * This is a utility class that provides static methods for logging method entrance, method exit and exception using
 * Log4j library. It allows to optionally log method input/output parameters and method execution time. Exceptions are
 * logged together with stack traces. Default level for method entrance/exit message is DEBUG, for exceptions - ERROR;
 * but both can be overridden.
 * </p>
 *
 * <p>
 * <em>API Usage: </em> A part of a sample IssuesTracker#updateIssue() method that uses most of the utilities defined
 * in this component. Note that only some methods of this utility class are demonstrated since their usage is very
 * similar.
 *
 * <pre>
 * private final Logger logger = Logger.getLogger(getClass());
 *
 * public boolean updateIssue(String issueName, int priority, File paramFile) throws IssueTrackingException {
 *     // Save method entrance timestamp
 *     Date enterTimestamp = new Date();
 *     // Prepare method signature that will appear in logged messages
 *     String signature = &quot;IssuesTracker#updateIssue(String issueName, int priority, File paramFile)&quot;;
 *
 *     // Log method entrance
 *     Log4jUtility.logEntrance(logger, signature,
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
 *                 Log4jUtility.logException(logger, signature, e2, Level.WARN);
 *
 *                 // Ignore this exception
 *             }
 *             throw e;
 *         } finally {
 *             releaseConnection(connection);
 *         }
 *
 *         // Log method exit
 *         Log4jUtility.logExit(logger, signature, new Object[] {result}, enterTimestamp);
 *
 *         return result;
 *     } catch (IllegalArgumentException e) {
 *         // Log and re-throw the exception
 *         throw Log4jUtility.logException(logger, signature, e);
 *     } catch (IssueTrackingException e) {
 *         // Log and re-throw the exception
 *         throw Log4jUtility.logException(logger, signature, e);
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
public class Log4jUtility {
    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private Log4jUtility() {
        // Empty
    }

    /**
     * <p>
     * Logs the method entrance together with input parameters (if present). It's assumed that paramNames and
     * paramValues contain the same number of elements.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> When this method is used, DEBUG level is used.
     * </p>
     *
     * @param paramValues
     *            the values of input parameters.
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param paramNames
     *            the names of input parameters (null of method doesn't accept any parameters).
     * @param logger
     *            the logger to be used (null if logging is not required to be performed).
     */
    public static void logEntrance(Logger logger, String signature, String[] paramNames, Object[] paramValues) {
        logEntrance(logger, signature, paramNames, paramValues, Level.DEBUG);
    }

    /**
     * <p>
     * Logs the method entrance together with input parameters (if present) and timestamp (optionally). It's assumed
     * that paramNames and paramValues contain the same number of elements.
     * </p>
     *
     * @param paramValues
     *            the values of input parameters.
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param paramNames
     *            the names of input parameters (null of method doesn't accept any parameters).
     * @param logger
     *            the logger to be used (null if logging is not required to be performed).
     * @param level
     *            the logging level to be used.
     */
    public static void logEntrance(Logger logger, String signature, String[] paramNames, Object[] paramValues,
        Level level) {
        if (logger == null) {
            // No logging
            return;
        }
        logger.log(level, LoggingUtilityHelper.getMethodEntranceMessage(signature));

        // Log parameters
        if (paramNames != null) {
            logger.log(level, LoggingUtilityHelper.getInputParametersMessage(paramNames, paramValues));
        }
    }

    /**
     * <p>
     * Logs the method exit together with the returned value (if present).
     * </p>
     *
     * <p>
     * <em>NOTE: </em> When this method is used, method execution time is not logged and DEBUG level is used.
     * </p>
     *
     * @param value
     *            the value returned from the method (should contain 1 element with the returned value, or should be
     *            null if the method returns void).
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param logger
     *            the logger to be used (null if logging is not required to be performed).
     */
    public static void logExit(Logger logger, String signature, Object[] value) {
        logExit(logger, signature, value, null);
    }

    /**
     * <p>
     * Logs the method exit together with the returned value (if present) and method execution time.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> When this method is used, DEBUG level is used.
     * </p>
     *
     * @param value
     *            the value returned from the method (should contain 1 element with the returned value, or should be
     *            null if the method returns void).
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param entranceTimestamp
     *            the method entrance timestamp (null if not available), is used for calculating method execution
     *            time.
     * @param logger
     *            the logger to be used (null if logging is not required to be performed).
     */
    public static void logExit(Logger logger, String signature, Object[] value, Date entranceTimestamp) {
        logExit(logger, signature, value, entranceTimestamp, Level.DEBUG);
    }

    /**
     * <p>
     * Logs the method exit together with the returned value (if present) and method execution time.
     * </p>
     *
     * @param value
     *            the value returned from the method (should contain 1 element with the returned value, or should be
     *            null if the method returns void).
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param entranceTimestamp
     *            the method entrance timestamp (null if not available), is used for calculating method execution
     *            time.
     * @param logger
     *            the logger to be used (null if logging is not required to be performed).
     * @param level
     *            the logging level to be used.
     */
    public static void logExit(Logger logger, String signature, Object[] value, Date entranceTimestamp, Level level) {
        if (logger == null) {
            // No logging
            return;
        }

        logger.log(level, LoggingUtilityHelper.getMethodExitMessage(signature, entranceTimestamp));
        if (value != null) {
            logger.log(level, LoggingUtilityHelper.getOutputValueMessage(value[0]));
        }
    }

    /**
     * <p>
     * Logs the given exception.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> When this method is used, ERROR level is used.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be logged and returned.
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName) where the exception is
     *            logged.
     * @param exception
     *            the exception to be logged (assumed to be not null).
     * @param logger
     *            the logger to be used (null if logging is not required to be performed).
     *
     * @return the passed in exception.
     */
    public static <T extends Throwable> T logException(Logger logger, String signature, T exception) {
        return logException(logger, signature, exception, Level.ERROR);
    }

    /**
     * <p>
     * Logs the given exception using the specified level.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be logged and returned.
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName) where the exception is
     *            logged.
     * @param exception
     *            the exception to be logged (assumed to be not null).
     * @param logger
     *            the logger to be used (null if logging is not required to be performed).
     * @param level
     *            the logging level to be used.
     *
     * @return the passed in exception.
     */
    public static <T extends Throwable> T logException(Logger logger, String signature, T exception, Level level) {
        if (logger != null) {
            logger.log(level, LoggingUtilityHelper.getExceptionMessage(signature, exception));
        }

        return exception;
    }
}
