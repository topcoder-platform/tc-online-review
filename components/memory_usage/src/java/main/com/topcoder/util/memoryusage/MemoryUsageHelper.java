/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.IdentityHashMap;

// NOTICE: this is a helper class. It is public because some of its methods are
// required by classes in the analyzers package.

/**
 * <p><b>This is an internal helper class, and is not meant to be used from the outside.</b></p>
 * This helper class contains several utility methods which are used throughout
 * the component.
 *
 * @author AleaActaEst, TexWiller
 * @version 2.0
 * @since 2.0
 */
public class MemoryUsageHelper {

    /**
     * The message sent when a <code>null</code> object is found. Parameter 0
     * will be substituted with the variable name.
     */
    private static final String NULL_MESSAGE = "The parameter '{0}' cannot be null";

    /**
     * The message sent when a <code>null</code> or empty String is found. Parameter 0
     * will be substituted with the variable name.
     */
    private static final String EMPTY_MESSAGE = "The parameter '{0}' cannot be null or empty";

    /**
     * The message sent when a negative value is found. Parameter 0
     * will be substituted with the variable name.
     */
    private static final String NEG_MESSAGE = "The parameter '{0}' cannot be negative";

    /**
     * Since this class exports only static methods, it is not necessary
     * to create any instance of it.
     */
    private MemoryUsageHelper() {
    }

    /**
     * Returns the default storage size of the objects of a certain class.
     * This value is 4 for everything, except for primitive types. For double
     * and long, the value is 8; for int and float, it is 4; for char and short,
     * it is 2; for byte and boolean, it is 1.
     *
     * @param cls The class whose memory usage needs to be known
     * @return The amount of memory required to store a field of the
     * specified class
     * @throws IllegalArgumentException If the <i>cls</i> parameter is <code>null</code>
     */
    public static int getFieldSize(Class cls) {
        MemoryUsageHelper.checkObjectNotNull(cls, "cls");

        if (cls.isPrimitive()) {
            if (cls == Double.TYPE || cls == Long.TYPE) {
                return 8;
            }

            if (cls == Integer.TYPE || cls == Float.TYPE) {
                return 4;
            }

            if (cls == Short.TYPE || cls == Character.TYPE) {
                return 2;
            }

            if (cls == Byte.TYPE || cls == Boolean.TYPE) {
                return 1;
            }
        }

        return 4;
    }

    /**
     * This method compares the <code>java.vendor</code> and <code>java.class.version</code>
     * system properties with the ones provided, and checks if the provided ones are contained
     * in the system ones.
     * @param vendorContains The string to look for inside the <code>java.vendor</code> property
     * @param versionContains The string to look for inside the <code>java.class.version</code> property
     * @return <code>true</code> if both strings are contained in their respective properties;
     * <code>false</code> otherwise
     * @throws IllegalArgumentException If any of the parameters is <code>null</code> or empty (trimmed)
     */
    public static boolean checkVendorVersion(String vendorContains, String versionContains) {
        checkStringNotNullOrEmpty(vendorContains, "vendorContains");
        checkStringNotNullOrEmpty(versionContains, "versionContains");
        String vendor = System.getProperty("java.vendor");
        String version = System.getProperty("java.class.version");
        return (vendor.indexOf(vendorContains) != -1) && (version.indexOf(versionContains) != -1);
    }

    /**
     * This method scans all the non-static fields of an object, and of its superclasses,
     * independently of their access modifiers. All the fields will be reported
     * to the FieldListener <i>listener</i>, with access level checks suppressed.
     * All the objects contained within
     * these fields will be collected, and reported in the returned array. This
     * behaviour is not recursive, so only top-level objects will be reported.
     * The method checks for reference-equals objects, in order not to report
     * the same object twice.
     * @param obj The object whose fields have to be scanned. It cannot be <code>null</code>
     * @param listener The listener which will receive notification of all the
     * fields found during the scan. If it is <code>null</code>, no notifications
     * will be issued
     * @return An array containing all the different objects found with the scan
     * @throws MemoryUsageException If exceptions occurred while traversing the object; typically,
     * security-related exceptions can be raised
     */
    static Object[] getEmbeddedObjects(Object obj, FieldsListener listener)
        throws MemoryUsageException {
        MemoryUsageHelper.checkObjectNotNull(obj, "obj");

        // This map will maintain all the already processed objects, in order
        // not to record duplicates in the array. It is a IdentityHashMap instead
        // of a plain Set, because we need to skip object which are reference equals
        // (==), but not necessarily .equals().
        IdentityHashMap visited = new IdentityHashMap();

        // Process Array
        if (obj.getClass().isArray()) {
            // Handle primitive arrays
            if (!(obj instanceof Object[])) {
                return new Object[0];
            }

            // Go through array and wrap the objects
            int length = Array.getLength(obj);

            for (int i = 0; i < length; i++) {
                Object value = Array.get(obj, i);

                // Make sure we don't include self references
                if (value != null && value != obj) {
                    visited.put(value, value);
                }
            }

            return visited.keySet().toArray();
        } else {
            // Process object
            try {
                // Go through all fields, and inherited fields, and wrap them
                for (Class cls = obj.getClass(); cls != null; cls = cls.getSuperclass()) {
                    Field[] fields = cls.getDeclaredFields();
                    Field.setAccessible(fields, true);

                    for (int i = 0; i < fields.length; i++) {
                        if (isStatic(fields[i])) {
                            continue;
                        }

                        // Inform the listener about the new field
                        if (listener != null) {
                            listener.fieldReached(fields[i]);
                        }
                        if (!fields[i].getType().isPrimitive()) {
                            Object value = fields[i].get(obj);

                            // Make sure we don't include self references
                            if (value != null && value != obj) {
                                visited.put(value, value);
                            }
                        }
                    }
                }

                return visited.keySet().toArray();
            } catch (IllegalAccessException iae) {
                throw new MemoryUsageException("Error retrieving object fields", iae);
            }
        }
    }

    /**
     * Checks if the passed object is not <code>null</code>. If it is, an
     * IllegalArgumentException is raised.
     * @param object The object to be checked
     * @param variableName The name of the object variable - will be used in the
     * exception message.
     * @throws IllegalArgumentException If the passed <i>object</i> is <code>null</code>,
     * or if <i>variableName</i> is <code>null</code> or empty.
     */
    public static void checkObjectNotNull(Object object, String variableName) {
        if ((variableName == null) || (variableName.trim().length() == 0)) {
            throw new IllegalArgumentException(format(EMPTY_MESSAGE, "variableName"));
        }
        if (object == null) {
            throw new IllegalArgumentException(format(NULL_MESSAGE, variableName));
        }
    }

    /**
     * Checks that the passed array is not <code>null</code>, and does not
     * contain any <code>null</code> element. If it does, an IllegalArgumentException
     * is raised.
     * @param array The array to be checked
     * @param arrayName The name of the array variable - will be used in the
     * exception message.
     * @throws IllegalArgumentException If the passed <i>array</i> is <code>null</code>,
     * or contains any <code>null</code> element, or if <i>arrayName</i> is <code>null</code>
     * or empty.
     */
    public static void checkArrayContentNotNull(Object[] array, String arrayName) {
        if ((arrayName == null) || (arrayName.trim().length() == 0)) {
            throw new IllegalArgumentException(format(EMPTY_MESSAGE, "arrayName"));
        }
        if (array == null) {
            throw new IllegalArgumentException(format(NULL_MESSAGE, arrayName));
        }
        if (array.length == 0) {
            throw new IllegalArgumentException(format(EMPTY_MESSAGE, arrayName));
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalArgumentException(format(NULL_MESSAGE, arrayName + "[" + i + "]"));
            }
        }
    }

    /**
     * Checks if the passed String is not <code>null</code>, or empty (trimmed).
     * If it is, an IllegalArgumentException is raised.
     * @param string The String to be checked
     * @param variableName The name of the String variable - will be used in the
     * exception message.
     * @throws IllegalArgumentException If the passed <i>string</i> is <code>null</code>
     * or empty, or if <i>variableName</i> is <code>null</code> or missing.
     */
    public static void checkStringNotNullOrEmpty(String string, String variableName) {
        checkObjectNotNull(string, variableName);
        if (string.trim().length() == 0) {
            throw new IllegalArgumentException(format(EMPTY_MESSAGE, variableName));
        }
    }

    /**
     * Checks if the passed long is not <=0. If it is, an IllegalArgumentException
     * is raised.
     * @param value The value to check for positivity
     * @param variableName The name of the original variable holding the
     * value - will be used in the exception message
     * @throws IllegalArgumentException If the passed value is <= 0
     */
    public static void checkValuePositive(long value, String variableName) {
        if (value <= 0) {
            throw new IllegalArgumentException(format(NEG_MESSAGE, variableName));
        }
    }

    /**
     * Formats the specified message, substituting "{0}" with the
     * <i>variableName</i> parameter. No validity check is performed
     * on the parameters.
     * @param message The message to be formatted
     * @param variableName The name of the variable to be inserted in the message
     * @return The formatted message
     */
    private static String format(String message, String variableName) {
        return MessageFormat.format(message, new Object[] {variableName });
    }

    /**
     * Helper to check if a field is static or not.
     *
     * @param field The field to analyze
     *
     * @return <code>true</code> if the field is static, <code>false</code> otherwise
     */
    static boolean isStatic(Field field) {
        int mod = field.getModifiers();
        return Modifier.isStatic(mod);
    }

    /**
     * This very simple interface is used to notify a listener about
     * all the fields found while analyzing an object. There is no guarantee
     * about the order in which fields will arrive to the interface. Passed fields
     * will have their access level check suppressed.
     *
     * @see MemoryUsageHelper#getEmbeddedObjects(Object, FieldsListener)
     */
    interface FieldsListener {

        /**
         * Informs the listener about a new field.
         * @param field The field which has been reached in the object
         */
        public void fieldReached(Field field);
    }
}
