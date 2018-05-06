/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;

/**
 * <p>
 * Utility class for this component.
 * </p>
 *
 * <p>
 * <b>Thread safety:</b> This class is immutable and so thread safe.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public final class Util {
    /**
     * This private constructor prevents to create a new instance.
     */
    private Util() {
    }

    /**
     * Checks whether the given {@link Object} is null.
     *
     * @param arg
     *            the argument to check
     * @param name
     *            the name of the argument to check
     *
     * @throws IllegalArgumentException
     *             if the given object is null
     */
    public static void checkNonNull(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * Checks whether the given {@link String} is empty(the length of the given
     * string is zero after trimmed).
     *
     * @param arg
     *            the String to check
     * @param name
     *            the name of the String argument to check
     *
     * @throws IllegalArgumentException
     *             if the given string is empty
     */
    private static void checkEmptyString(String arg, String name) {
        if (arg.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty.");
        }
    }

    /**
     * Checks whether the given {@link String} is null or empty(the length of
     * the given string is zero after trimmed).
     *
     * @param arg
     *            the String to check
     * @param name
     *            the name of the String argument to check
     *
     * @throws IllegalArgumentException
     *             if the given string is null or empty
     */
    public static void checkNonNullNonEmpty(String arg, String name) {
        checkNonNull(arg, name);

        checkEmptyString(arg, name);
    }

    /**
     * <p>
     * Close the closeable object safely.
     * </p>
     *
     * @param closeableObject
     *            the closeable object to close
     */
    public static void safeClose(Closeable closeableObject) {
        if (closeableObject != null) {
            try {
                closeableObject.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * <p>
     * Logs the given message at INFO level.
     * </p>
     *
     * @param log
     *            The log object.
     * @param message
     *            The message to be logged.
     */
    public static void logInfo(Log log, String message) {
        if (log != null) {
            log.log(Level.INFO, message);
        }
    }

    /**
     * <p>
     * Check the list of objects if it's null, contains null or contains empty
     * if it's a list of Strings.
     * </p>
     *
     * @param objects
     *            the objects to check
     * @param name
     *            the name of the argument
     *
     * @throws IllegalArgumentException
     *             if the objects is null, or contains null, or contains empty
     *             when it's list of string.
     */
    public static void checkList(List<?> objects, String name) {
        checkNonNull(objects, name);

        for (int i = 0; i < objects.size(); ++i) {
            Object cur = objects.get(i);
            checkNonNull(cur, "The " + i + " th object of " + name);
            if (cur instanceof String) {
                checkEmptyString((String) cur, "The " + i + " th object of "
                        + name);
            }
        }
    }

    /**
     * <p>
     * Check the parameters in the given mapping.
     * </p>
     *
     * @param params
     *            the mapping to check
     * @param name
     *            the name of params
     *
     * @throws IllegalArgumentException
     *             * if params is null, contains null/empty key or contains null
     *             value
     */
    public static void checkParams(Map<String, String> params, String name) {
        checkNonNull(params, name);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Util.checkNonNullNonEmpty(entry.getKey(), "key in " + name);
            Util.checkNonNull(entry.getValue(), "value in " + name);
        }
    }
}
