/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.stresstests;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * <p>
 * This helper class provides some static methods for test usage.
 * </p>
 * @author isv, abelli
 * @version 1.0
 */
public class TestHelper {
    /**
     * <p>
     * This private default constructor to prevent creating new instances.
     * </p>
     */
    private TestHelper() {
        // Empty.
    }

    /**
     * <p>
     * A helper method to be used to <code>nullify</code> the singleton instance. The method uses
     * a <code>Java Reflection API</code> to access the field and initialize the field with
     * <code>null</code> value. The operation may fail if a <code>SecurityManager</code>
     * prohibits such sort of accessing.
     * </p>
     * @param clazz a <code>Class</code> representing the class of the <code>Singleton</code>
     *            instance.
     * @param instanceName a <code>String</code> providing the name of the static field holding
     *            the reference to the singleton instance.
     * @throws Exception - if fail to release the singleton instance.
     */
    public static final void releaseSingletonInstance(Class clazz, String instanceName)
        throws Exception {
        try {
            Field instanceField = clazz.getDeclaredField(instanceName);
            boolean accessibility = instanceField.isAccessible();
            instanceField.setAccessible(true);

            if (Modifier.isStatic(instanceField.getModifiers())) {
                instanceField.set(null, null);
            } else {
                System.out.println("An error occurred while trying to release the singleton instance - the "
                    + " '" + instanceName + "' field is not static");
            }

            instanceField.setAccessible(accessibility);
        } catch (Exception e) {
            throw new Exception(
                "An error occurred while trying to release the singleton instance : " + e, e);
        }
    }

}
