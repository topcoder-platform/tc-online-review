/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * <p>
 * This is a static helper class that provides log message generation functionality for LoggingWrapperUtility and
 * Log4jUtility.
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
final class LoggingUtilityHelper {
    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private LoggingUtilityHelper() {
        // Empty
    }

    /**
     * <p>
     * Retrieves the method entrance log message.
     * </p>
     *
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     *
     * @return the constructed method entrance message.
     */
    static String getMethodEntranceMessage(String signature) {
        return Helper.concat("Entering method [", signature, "].");
    }

    /**
     * <p>
     * Retrieves the log message for the given input parameters. It's assumed that paramNames and paramValues contain
     * the same number of elements.
     * </p>
     *
     * @param paramValues
     *            the values of input parameters (not null).
     * @param paramNames
     *            the names of input parameters (not null).
     *
     * @return the constructed log message.
     */
    static String getInputParametersMessage(String[] paramNames, Object[] paramValues) {
        StringBuilder sb = new StringBuilder("Input parameters [");
        int paramNamesLen = paramNames.length;
        for (int i = 0; i < paramNamesLen; i++) {
            if (i != 0) {
                // Append a comma
                sb.append(", ");
            }
            sb.append(paramNames[i]).append(":").append(paramValues[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * <p>
     * Retrieves the method exit log message.
     * </p>
     *
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param entranceTimestamp
     *            the method entrance timestamp (null if not available), is used for calculating method execution
     *            time.
     *
     * @return the constructed method exit message.
     */
    static String getMethodExitMessage(String signature, Date entranceTimestamp) {
        StringBuilder sb = new StringBuilder();
        sb.append("Exiting method [").append(signature).append("]");
        if (entranceTimestamp != null) {
            sb.append(", time spent in the method: ")
                .append(System.currentTimeMillis() - entranceTimestamp.getTime()).append(" milliseconds");
        }
        sb.append(".");
        return sb.toString();
    }

    /**
     * <p>
     * Retrieves the log message for the given method output value.
     * </p>
     *
     * @param value
     *            the value returned by the method.
     *
     * @return the constructed log message.
     */
    static String getOutputValueMessage(Object value) {
        return "Output parameter: " + value;
    }

    /**
     * <p>
     * Retrieves the exception log message.
     * </p>
     *
     * @param signature
     *            the signature that uniquely identifies the method (e.g. className#methodName).
     * @param exception
     *            the exception to be logged (assumed to be not null).
     *
     * @return the retrieved exception message.
     */
    static String getExceptionMessage(String signature, Throwable exception) {
        StringBuilder sb = new StringBuilder();

        sb.append("Error in method [").append(signature).append("], details: ").append(exception.getMessage());

        // Get the stack trace
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        ps.println();
        exception.printStackTrace(ps);

        sb.append(out.toString());
        return sb.toString();
    }
}
