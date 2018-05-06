/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 * This is a static helper class that provides methods for constructing exception instances using reflection. It is
 * used by ValidationUtility, PropertiesUtility and JDBCUtility.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is immutable and thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0
 */
final class ExceptionHelper {
    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private ExceptionHelper() {
        // Empty
    }

    /**
     * <p>
     * Constructs an exception of the specified type with the given message.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> Assume no exception will occur.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be created.
     * @param exceptionClass
     *            the exception class.
     * @param message
     *            the message.
     *
     * @return the constructed exception instance (not null).
     */
    static <T extends Throwable> T constructException(Class<T> exceptionClass, String message) {
        return createException(exceptionClass, new Class<?>[] {String.class}, new Object[] {message});
    }

    /**
     * <p>
     * Constructs an exception of the specified type with the given message and cause.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> Assume no exception will occur.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be created.
     * @param exceptionClass
     *            the exception class.
     * @param message
     *            the message.
     * @param cause
     *            the exception cause.
     *
     * @return the constructed exception instance (not null).
     */
    static <T extends Throwable> T constructException(Class<T> exceptionClass, String message, Throwable cause) {
        return createException(exceptionClass, new Class<?>[] {String.class, Throwable.class},
            new Object[] {message, cause});
    }

    /**
     * <p>
     * Creates an exception of the specified type.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> Assume no exception will occur.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be created.
     * @param exceptionClass
     *            the exception class.
     * @param paramTypes
     *            the parameter types.
     * @param paramValues
     *            the parameter values.
     *
     * @return the constructed exception instance.
     */
    private static <T extends Throwable> T createException(Class<T> exceptionClass, Class<?>[] paramTypes,
        Object[] paramValues) {
        T exception = null;

        try {
            exception = exceptionClass.getConstructor(paramTypes).newInstance(paramValues);
        } catch (InstantiationException e) {
            // Ignore
        } catch (IllegalAccessException e) {
            // Ignore
        } catch (InvocationTargetException e) {
            // Ignore
        } catch (NoSuchMethodException e) {
            // Ignore
        }

        return exception;
    }
}
