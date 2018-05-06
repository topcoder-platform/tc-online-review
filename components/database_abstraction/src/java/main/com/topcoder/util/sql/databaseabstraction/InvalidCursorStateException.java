/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

/**
 * <p>
 * The InvalidCursorStateException is thrown by a CustomResultSet to indicate
 * that it is not currently positioned on any row. This could be because it is
 * positioned before the first row or after the last row.
 * </p>
 * <p>
 * This exception is thrown by CustomResultSet in the getXXX methods when data
 * can not be retrieved because of the current row position of the
 * CustomResultSet.
 * </p>
 *
 * @author argolite, aubergineanode, WishingBone, justforplay
 * @version 1.1
 */
@SuppressWarnings("serial")
public class InvalidCursorStateException extends Exception {

    /**
     * No argument constructor used in previous version.
     */
    public InvalidCursorStateException() {
    }

    /**
     * Creates a new InvalidCursorStateException.
     *
     * @param message
     *            Explanation of error
     * @since 1.1
     */
    public InvalidCursorStateException(String message) {
        super(message);
    }

    /**
     * Creates a new InvalidCursorStateException.
     *
     * @param message
     *            Explanation of error
     * @param cause
     *            Underlying cause of error
     * @since 1.1
     */
    public InvalidCursorStateException(String message, Exception cause) {
        super(message, cause);
    }
}
