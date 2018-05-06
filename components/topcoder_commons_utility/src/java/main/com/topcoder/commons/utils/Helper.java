/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * Helper class for the component. It provides useful common methods for all the classes in this component.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class has no state, and thus it is thread safe.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
final class Helper {
    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private Helper() {
        // empty
    }

    /**
     * <p>
     * Checks whether the given collection doesn't contain null element.
     * </p>
     *
     * @param collection
     *            the collection.
     *
     * @return <code>true</code> if the collection contains null element; <code>false</code> otherwise;
     */
    static boolean containNull(Collection<?> collection) {
        for (Object element : collection) {
            if (element == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Checks whether the given collection doesn't contain empty element (strings, collection, maps).
     * </p>
     *
     * @param collection
     *            the collection.
     * @param trimStrings
     *            true if strings should be trimmed before emptiness check, false otherwise.
     *
     * @return <code>true</code> if the collection contains empty element (strings, collection, maps);
     *         <code>false</code> otherwise;
     */
    static boolean containEmpty(Collection<?> collection, boolean trimStrings) {
        for (Object element : collection) {
            if (element instanceof String) {
                // A string
                String str = (String) element;
                if (trimStrings) {
                    str = str.trim();
                }
                if (str.length() == 0) {
                    return true;
                }
            } else if (element instanceof Collection<?>) {
                // A collection
                if (((Collection<?>) element).isEmpty()) {
                    return true;
                }
            } else if (element instanceof Map<?, ?>) {
                // A map
                if (((Map<?, ?>) element).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * Concatenates the given values to a string.
     * </p>
     *
     * @param values
     *            the values.
     *
     * @return the result.
     */
    static String concat(Object... values) {
        StringBuilder sb = new StringBuilder();

        for (Object value : values) {
            sb.append(value);
        }

        return sb.toString();
    }
}
