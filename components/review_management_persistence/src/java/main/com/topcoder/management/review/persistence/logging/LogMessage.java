/*
 * Copyright (C) 2007-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.persistence.logging;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * <p>
 * Encapsulates the entry log data and generates consistent log messages.
 * </p>
 * <p>
 * Changes in 1.2:
 * <ul>
 * <li>Made immutable fields final.</li>
 * </ul>
 * </p>
 * <p>
 * Thread Safety: This class has mutable logMessage field, and thus it is not thread safe. But
 * InformixReviewPersistence always uses it in thread safe manner - LogMessage instances are never accessed from
 * multiple threads.
 * </p>
 * @author saarixx, TCSDEVELOPER
 * @version 1.2
 */
public class LogMessage {

    /**
     * <p>
     * Project id for the log message. Is initialized in the constructor and never changed after that. Can be any
     * value. Has a getter. Is used in getLogMessage().
     * </p>
     */
    private final Long reviewId;

    /**
     * <p>
     * Operator doing the action. Is initialized in the constructor and never changed after that. Can be any value. Has
     * a getter. Is used in getLogMessage().
     * </p>
     */
    private final String operator;

    /**
     * <p>
     * Free text message to log. Is initialized in the constructor and never changed after that. Can be any value. Has
     * a getter. Is used in getLogMessage().
     * </p>
     */
    private final String message;

    /**
     * <p>
     * Exception to append to the log message. Is initialized in the constructor and never changed after that. Can be
     * any value. Has a getter. Is used in getLogMessage().
     * </p>
     */
    private final Throwable error;

    /**
     * <p>
     * Generated log message. Is initialized in getLogMessage() once and never changed after that. Can be any value. Is
     * null when not initialized.
     * </p>
     */
    private String logMessage = null;

    /**
     * Creates a log message. Any parameter can be null.
     * @param reviewId the project id to log.
     * @param operator the operator to log.
     * @param message a free text message.
     * @param error an exception to append to the log message.
     */
    public LogMessage(Long reviewId, String operator, String message, Throwable error) {
        this.reviewId = reviewId;
        this.operator = operator;
        this.message = message;
        this.error = error;
    }

    /**
     * Creates a log message. Any parameter can be null
     * @param reviewId the project id to log.
     * @param operator the operator to log.
     * @param message a free text message.
     */
    public LogMessage(Long reviewId, String operator, String message) {
        this(reviewId, operator, message, null);
    }

    /**
     * Return the error cause.Can be null.
     * @return the error cause.
     */
    public Throwable getError() {
        return error;
    }

    /**
     * Returns the message.
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the operator. Can be null.
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Returns the review id, can be null.
     * @return project id
     */
    public Long getReviewId() {
        return reviewId;
    }

    /**
     * Generate the message need to be logged for this class.
     * @return The log message
     */
    public String getLogMessage() {
        if (logMessage == null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("operator: ").append((operator == null) ? "Unknown" : operator).append("reviewId: ")
                    .append((reviewId == null) ? "Unknown" : reviewId.toString()).append(" - ").append(message);
            // This should be done while the Logging Wrapper 1.2 is used.
            // When the LW 1.3 would be ready, it will be possible pass the exception directly to LW.
            if (error != null) {
                buffer.append('\n').append(getExceptionStackTrace(error));
            }
            logMessage = buffer.toString();
        }
        return logMessage;
    }

    /**
     * Return the exception stack trace string.
     * @param cause the exception to be recorded
     * @return stack trace
     */
    public static String getExceptionStackTrace(Throwable cause) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        cause.printStackTrace(new PrintStream(out));
        return out.toString();
    }

    /**
     * Override the toString method returns the log message.
     * @return logged message
     */
    public String toString() {
        return getLogMessage();
    }
}
