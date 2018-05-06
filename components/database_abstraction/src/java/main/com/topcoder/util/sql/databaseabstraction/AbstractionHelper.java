/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

/**
 * <p>
 * Helper class for the Database Abstraction.
 * </p>
 * <p>
 * <strong>Changes in 2.0:</strong>
 * <ol>
 * <li>Specified generic parameters for all generic types in the code.</li>
 * <li>Add missing throws tag for checkColumnIndex().</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class is immutable and thread safe.
 * </p>
 *
 * @author aubergineanode, saarixx, justforplay, suhugo
 * @version 2.0
 * @since 1.1
 */
public final class AbstractionHelper {
    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private AbstractionHelper() {
    }

    /**
     * <p>
     * Checks whether the given Object is null.
     * </p>
     *
     * @param arg
     *            the argument to check
     * @param name
     *            the name of the argument to check
     * @throws IllegalArgumentException
     *             if the given Object is null
     */
    public static void checkNull(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks whether the given arg is null or any of the element in arg is
     * null.
     * </p>
     *
     * @param arg
     *            the Array to check
     * @param name
     *            the name of the argument to check
     * @throws IllegalArgumentException
     *             if any element in the given arg is null or any of the element
     *             in arg is null.
     */
    public static void checkArray(Object[] arg, String name) {
        checkNull(arg, name);

        for (Object element : arg) {
            if (element == null) {
                throw new IllegalArgumentException("element in array " + name + " should not be null.");
            }
        }
    }

    /**
     * <p>
     * Checks whether the given column is valid. If column <= 0 or column >
     * maxValue, it is invalid.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Add missing throws tag.</li>
     * </ol>
     * </p>
     *
     * @param column
     *            given column to check.
     * @param maxValue
     *            max value.
     * @throws IllegalArgumentException
     *             if column <= 0 or column > maxValue.
     */
    public static void checkColumnIndex(int column, int maxValue) {
        if (column <= 0 || column > maxValue) {
            throw new IllegalArgumentException("column should be greater than 0 and less than " + maxValue);
        }
    }

    /**
     * <p>
     * Checks given value can be converted to the specified type. if value is
     * not null and is the same type of subclass of orgClass, and if desiredType
     * can be converted to the any of the listed class in canConvertTo array, it
     * is considered that true is returned.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Using "Class<?>" instead of "Class" for parameter types.</li>
     * </ol>
     * </p>
     *
     * @param value
     *            value to check.
     * @param orgClass
     *            expected class of value
     * @param desiredType
     *            desired type to convert to
     * @param canConvertTo
     *            classes list that can be converted to
     * @return If value is not null and is the same type of subclass of
     *         orgClass, and if desiredType can be converted to the any of the
     *         listed class in canConvertTo array, true is returned.
     */
    public static boolean canConvert(Object value, Class<?> orgClass, Class<?> desiredType, Class<?>[] canConvertTo) {
        if (value == null || !orgClass.isAssignableFrom(value.getClass())) {
            return false;
        }
        for (Class<?> element : canConvertTo) {
            if (desiredType.isAssignableFrom(element)) {
                return true;
            }
        }
        return false;
    }
}
