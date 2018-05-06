/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p>
 * The helper class of this component.
 * </p>
 * <p>
 * This class provides some useful methods.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public final class UserProjectDataStoreHelper {

    /**
     * <p>
     * The default string which indicates the nothing in the component.
     * </p>
     */
    public static final String DEFAULT_NA_STRING = "N/A";

    /**
     * <p>
     * Private constructor to prevent this class being instantiated.
     * </p>
     */
    private UserProjectDataStoreHelper() {
    }

    /**
     * <p>
     * Checks whether the given Object is null.
     * </p>
     *
     * @param arg
     *            the argument to check.
     * @param name
     *            the name of the argument.
     * @throws IllegalArgumentException
     *             if the given Object is <code>null</code>.
     */
    public static void validateNull(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks whether the given String is empty or null.
     * </p>
     *
     * @param arg
     *            the String argument to check.
     * @param name
     *            the name of the String argument.
     * @throws IllegalArgumentException
     *             if the given String is <code>null</code> or empty after trim.
     */
    public static void validateStringEmptyNull(String arg, String name) {
        validateNull(arg, name);

        if (arg.trim().equals("")) {
            throw new IllegalArgumentException("The String " + name + " should not be empty.");
        }
    }

    /**
     * <p>
     * Checks whether the given integer is negative.
     * </p>
     *
     * @param arg
     *            the integer argument to check.
     * @param name
     *            the name of the argument.
     * @throws IllegalArgumentException
     *             if the integer is negative.
     */
    public static void validateNegative(int arg, String name) {
        if (arg < 0) {
            throw new IllegalArgumentException(name + " should not be negative.");
        }
    }

    /**
     * <p>
     * Checks whether the given long integer is negative.
     * </p>
     *
     * @param arg
     *            the long integer argument to check.
     * @param name
     *            the name of the argument.
     * @throws IllegalArgumentException
     *             if the long integer is negative.
     */
    public static void validateNegative(long arg, String name) {
        if (arg < 0) {
            throw new IllegalArgumentException(name + " should not be negative.");
        }
    }

    /**
     * <p>
     * Checks whether the given double is negative.
     * </p>
     *
     * @param arg
     *            the double argument to check.
     * @param name
     *            the name of the argument.
     * @throws IllegalArgumentException
     *             if the double is negative.
     */
    public static void validateNegative(double arg, String name) {
        if (arg < 0) {
            throw new IllegalArgumentException(name + " should not be negative.");
        }
    }

    /**
     * <p>
     * Checks whether the given integer is non-positive.
     * </p>
     *
     * @param arg
     *            the integer argument to check.
     * @param name
     *            the name of the argument.
     * @throws IllegalArgumentException
     *             if the integer is non-positive.
     */
    public static void validateNotPositive(int arg, String name) {
        if (arg <= 0) {
            throw new IllegalArgumentException(name + " should be positive.");
        }
    }

    /**
     * <p>
     * Checks whether the given long integer is non-positive.
     * </p>
     *
     * @param arg
     *            the long integer argument to check.
     * @param name
     *            the name of the argument.
     * @throws IllegalArgumentException
     *             if the long integer is non-positive.
     */
    public static void validateNotPositive(long arg, String name) {
        if (arg <= 0) {
            throw new IllegalArgumentException(name + " should be positive.");
        }
    }

    /**
     * <p>
     * Checks whether the long integer field has already been set.
     * </p>
     *
     * @param arg
     *            the long integer value of the field.
     * @param name
     *            the name of the argument.
     * @throws IllegalArgumentException
     *             if the long integer is not -1, means it has been set already.
     */
    public static void validateFieldAlreadySet(long arg, String name) {
        if (arg != -1) {
            throw new IllegalArgumentException("The field " + name + " has already been set.");
        }
    }

    /**
     * <p>
     * Checks whether the String field has already been set.
     * </p>
     *
     * @param arg
     *            the String value of the field.
     * @param name
     *            the name of the argument.
     * @throws IllegalArgumentException
     *             if the String is not <code>null</code>, means it has been set already.
     */
    public static void validateFieldAlreadySet(String arg, String name) {
        if (arg != null) {
            throw new IllegalArgumentException("The field " + name + " has already been set.");
        }
    }

    /**
     * <p>
     * Checks whether the array of long values is valid.
     * </p>
     *
     * @param array
     *            the array of long values need to check.
     * @param name
     *            the name of the array argument.
     * @throws IllegalArgumentException
     *             if the array is <code>null</code>, or any long entry is not positive.
     */
    public static void validateArray(long[] array, String name) {
        validateNull(array, name);

        for (int i = 0; i < array.length; i++) {
            validateNotPositive(array[i], name + "[" + i + "]");
        }
    }

    /**
     * <p>
     * Checks whether the array of String values is valid.
     * </p>
     *
     * @param array
     *            the array of String values need to check.
     * @param name
     *            the name of the array argument.
     * @throws IllegalArgumentException
     *             if the array is <code>null</code>, or any String entry is <code>null</code> or empty after trim.
     */
    public static void validateArray(String[] array, String name) {
        validateNull(array, name);

        for (int i = 0; i < array.length; i++) {
            validateStringEmptyNull(array[i], name + "[" + i + "]");
        }
    }

    /**
     * <p>
     * Returns the first objects of the given array parameter, the parameter can be <code>ExternalUser[]</code> or
     * <code>ExternalProject[]</code>.
     * </p>
     * <p>
     * This method is used by <code>DBUserRetrieval</code> and <code>DBProjectRetrieval</code>
     * classes.
     * </p>
     *
     * @param objects
     *            the parameter can be <code>ExternalUser[]</code> or <code>ExternalProject[]</code>.
     * @return the first element of the parameter, if the array is empty return <code>null</code>.
     */
    public static ExternalObject retFirstObject(ExternalObject[] objects) {
        if (objects.length == 0) {
            return null;
        } else {
            return objects[0];
        }
    }

    /**
     * <p>
     * This method helps creates a prepareStatement of the given query string.
     * </p>
     *
     * @return the PreparedStatement created.
     * @param conn
     *            the connection for creating the PreparedStatement.
     * @param query
     *            the query string for creating the PreparedStatement.
     * @param queryName
     *            the name of the query.
     * @throws RetrievalException
     *             database access error occurs while preparing the statement.
     */
    public static PreparedStatement createStatement(Connection conn, String query, String queryName)
            throws RetrievalException {
        try {
            return conn.prepareStatement(query);
        } catch (SQLException e) {
            throw new RetrievalException("Database access error occurs while preparing the " + queryName
                    + " statement.", e);
        }
    }

    /**
     * <p>
     * This method helps generate question marks of the given marks number.
     * </p>
     *
     * @param length
     *            how many question marks are needed.
     * @return the string of the question marks.
     */
    public static String generateQuestionMarks(int length) {
        StringBuilder questionMarks = new StringBuilder();

        questionMarks.append("(");
        for (int i = 0; i < length - 1; i++) {
            questionMarks.append("?,");
        }
        questionMarks.append("?)");

        return questionMarks.toString();
    }
}
