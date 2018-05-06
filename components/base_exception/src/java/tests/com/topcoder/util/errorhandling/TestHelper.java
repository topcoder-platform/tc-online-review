/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.lang.reflect.Field;

/**
 * Defines helper methods used in unit tests of this component.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
final class TestHelper {

    /**
     * This private constructor prevents the creation of a new instance.
     */
    private TestHelper() {
    }

    /**
     * Gets the value of a private field in the given class. The field has the given name. The value is retrieved from
     * the given instance. If the instance is null, the field is a static field. If any error occurs, null is returned.
     *
     * @param type
     *            the class which the private field belongs to
     * @param instance
     *            the instance which the private field belongs to
     * @param name
     *            the name of the private field to be retrieved
     * @return the value of the private field
     */
    static Object getPrivateField(Class type, Object instance, String name) {
        Field field = null;
        Object obj = null;
        try {
            // get the reflection of the field
            field = type.getDeclaredField(name);
            // set the field accessible
            field.setAccessible(true);
            // get the value
            obj = field.get(instance);
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        } finally {
            if (field != null) {
                // reset the accessibility
                field.setAccessible(false);
            }
        }
        return obj;
    }
}