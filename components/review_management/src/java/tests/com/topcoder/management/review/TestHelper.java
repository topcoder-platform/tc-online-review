/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review;

import com.topcoder.util.config.ConfigManager;

import java.lang.reflect.Field;

import java.util.Iterator;

/**
 * <p>
 * Helper class to simplify the unit testing.
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Moved checkNull/checkString/checkPositive methods to MockReviewPersistence class.</li>
 * <li>Fixed the code to meet the TopCoder standard.</li>
 * </ol>
 * </p>
 *
 * @author icyriver, sparemax
 * @version 1.2
 */
final class TestHelper {
    /**
     * <p>
     * The private constructor to avoid creating instance of this class.
     * </p>
     */
    private TestHelper() {
        // Empty
    }

    /**
     * <p>
     * Clear the namespaces in ConfigManager.
     * </p>
     *
     * @throws Exception
     *             if configuration could not be clear.
     */
    public static void clearNamespace() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator<String> it = cm.getAllNamespaces();

        while (it.hasNext()) {
            cm.removeNamespace(it.next());
        }
    }

    /**
     * <p>
     * Returns the value of the given field in the given Object using Reflection.
     * </p>
     *
     * @param obj
     *            the given Object instance to get the field value.
     * @param fieldName
     *            the name of the filed to get value from the object.
     *
     * @return the field value in the obj.
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            try {
                return field.get(obj);
            } finally {
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            // ignore the exception and return null.
        } catch (IllegalAccessException e) {
            // ignore the exception and return null.
        }

        return null;
    }
}
