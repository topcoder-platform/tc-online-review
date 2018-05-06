/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.logging;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.topcoder.util.errorhandling.ExceptionUtils;


/**
 * <p>
 * Encapsulates the entry log data and generates consistent log messages.
 * </p>
 * <p>
 * Changes in 1.4: Made just some trivial fixes to meet TopCoder standards.
 * </p>
 * <p>
 * Thread Safety: This class is mutable and not thread safe.
 * </p>
 *
 * @author pulky, sarrixx, myxgyy
 * @version 1.4
 */
public class LogMessage {
    /**
     * project id for the log message.
     *
     * Change in 1.4: Made final.
     */
    private final Long phaseId;

    /**
     * Operator doing the action.
     *
     * Change in 1.4: Made final.
     */
    private final String operator;

    /**
     * free text message to log.
     *
     * Change in 1.4: Made final.
     */
    private final String message;

    /**
     * exception to append to the log message.
     *
     * Change in 1.4: Made final.
     */
    private final Throwable error;

    /**
     * generated log message.
     */
    private String logMessage = null;

/**
     * Creates a log message. Any parameter can be null.
     *
     * @param phaseId the project id to log.
     * @param operator the operator to log.
     * @param message a free text message.
     * @param error an exception to append to the log message.
     */
    public LogMessage(Long phaseId, String operator, String message, Throwable error) {
        this.phaseId = phaseId;

        this.operator = operator;

        this.message = message;

        this.error = error;
    }

/**
     * Creates a log message. Any parameter can be null.
     *
     * @param projectId the project id to log.
     * @param operator the operator to log.
     * @param message a free text message.
     */
    public LogMessage(Long projectId, String operator, String message) {
        this(projectId, operator, message, null);
    }

    /**
     * Return the error cause.Can be null.
     *
     * @return the error cause.
     */
    public Throwable getError() {
        return error;
    }

    /**
     * Returns the message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the operator. Can be null.
     *
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Returns the phase id, can be null.
     *
     * @return phase id
     */
    public Long getPhaseId() {
        return phaseId;
    }

    /**
     * Generate the message need to be logged for this class.
     *
     * @return The log message
     */
    public String getLogMessage() {
        if (logMessage == null) {
            StringBuffer buffer = new StringBuffer();

            buffer.append("operator: ").append((operator == null) ? "Unknown" : operator)
                  .append("phaseId: ").append((phaseId == null) ? "Unknown" : phaseId.toString())
                  .append(" - ").append(message);

            // This should be done while the Logging Wrapper 1.2 is used.

            // When the LW 1.3 would be ready, it will be possible pass the
            // exception directly to LW.
            if (error != null) {
                buffer.append('\n').append(getExceptionStackTrace(error));
            }

            logMessage = buffer.toString();
        }

        return logMessage;
    }

    /**
     * <p>
     * Return the exception stack trace string.
     * </p>
     * <p>
     * Change in 1.4: Argument checking added.
     * </p>
     *
     * @param cause the exception to be recorded
     *
     * @return the stack trace of the exception.
     *
     * @throws IllegalArgumentException if cause is null.
     */
    public static String getExceptionStackTrace(Throwable cause) {
        ExceptionUtils.checkNull(cause, null, null, "cause should not be null");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        cause.printStackTrace(new PrintStream(out));

        return out.toString();
    }

    /**
     * Override the toString method returns the log message.
     *
     * @return logged message
     */
    public String toString() {
        return getLogMessage();
    }
}
