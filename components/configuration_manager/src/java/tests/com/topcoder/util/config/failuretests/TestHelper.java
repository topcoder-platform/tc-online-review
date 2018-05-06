/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config.failuretests;

import java.lang.reflect.Field;


/**
 * <p>Defines helper methods used in tests.</p>
 *
 * @author onsky
 * @version 1.0
 */
public final class TestHelper {
/**
     * <p>
     * Creates a new instance of <code>AccuracyTestHelper</code> class. The private constructor prevents the creation
     * of a new instance.
     * </p>
     */
    private TestHelper() {
    }

    /**
     * <p>Sets the value of a private field in the given class.</p>
     *
     * @param type the type of the class.
     * @param instance the instance which the private field belongs to.
     * @param name the name of the private field to be retrieved.
     * @param value the value to be set
     */
    public static void setPrivateField(Class<?> type, Object instance, String name, Object value) {
        Field field = null;

        try {
            // Get the reflection of the field and get the value
            field = type.getDeclaredField(name);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        } finally {
            if (field != null) {
                // Reset the accessibility
                field.setAccessible(false);
            }
        }
    }
}
