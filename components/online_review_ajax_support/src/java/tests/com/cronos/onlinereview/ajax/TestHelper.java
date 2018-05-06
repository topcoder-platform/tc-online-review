/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * The helper class for test.
 *
 *
 * @author assistant
 * @version 1.0
 */
public final class TestHelper {

    /**
     * Represents the global date object for test.
     */
    public static final Date DATE_1 = new Date();

    /**
     * Represents the global date object for test.
     */
    public static final Date DATE_2 = new Date();

    /**
     * Private constructor.
     *
     */
    private TestHelper() {
        // do nothing
    }

    /**
     * Get private field.
     *
     * @param type the class of the object to get
     * @param name the name of the field.
     * @param obj the object whose field is to be get
     * @return the field value
     * @throws Exception to invoker
     */
    public static Object getPrivateFieldValue(Class type, String name, Object obj)
        throws Exception {
        Field field = type.getDeclaredField(name);
        try {
            field.setAccessible(true);
            return field.get(obj);
        } finally {
            field.setAccessible(false);
        }

    }
}
