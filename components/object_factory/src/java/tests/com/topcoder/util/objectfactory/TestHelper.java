/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.topcoder.util.config.ConfigManager;

/**
 * Test Helper for the component.
 *
 * @author mgmg, TCSDEVELOPER
 * @version 2.2
 */
public final class TestHelper {
    /**
     * Private test helper.
     */
    private TestHelper() {
    }

    /**
     * Get the URL string of the file.
     *
     * @param fileName
     *            the file name
     * @return the url string.
     */
    public static String getURLString(String fileName) {
        return "file:///" + new File(fileName).getAbsolutePath();
    }

    /**
     * Load a config file to ConfigManager.
     *
     * @throws Exception
     *             exception to the caller.
     */
    public static void loadSingleConfigFile() throws Exception {
        clearConfig();
        ConfigManager.getInstance().add("config.xml");
    }

    /**
     * Clear all the configs.
     *
     * @throws Exception
     *             exception to the caller.
     */
    public static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator<String> it = cm.getAllNamespaces();
        List<String> nameSpaces = new ArrayList<String>();

        while (it.hasNext()) {
            nameSpaces.add(it.next());
        }

        for (int i = 0; i < nameSpaces.size(); i++) {
            cm.removeNamespace(nameSpaces.get(i));
        }
    }

    /**
     * <p>
     * Gets the value of a private field in the given class. The field has the
     * given name. The value is retrieved from the given instance. If the
     * instance is null, the field is a static field. If any error occurs, null
     * is returned.
     * </p>
     *
     * @param type
     *            The class which the private field belongs to.
     * @param instance
     *            The instance which the private field belongs to.
     * @param name
     *            The name of the private field to be retrieved.
     * @return The value of the private field.
     * @since 2.2
     */
    public static Object getPrivateField(Class<?> type, Object instance, String name) {
        Field field = null;
        Object obj = null;
        try {
            // get the reflection of the field
            field = type.getDeclaredField(name);
            // set the field accessible
            field.setAccessible(true);
            // get the value
            obj = field.get(instance);
        } catch (IllegalArgumentException e) {
            // Ignore
        } catch (IllegalAccessException e) {
            // Ignore
        } catch (SecurityException e) {
            // Ignore
        } catch (NoSuchFieldException e) {
            // Ignore
        } finally {
            if (field != null) {
                // reset the accessibility
                field.setAccessible(false);
            }
        }
        return obj;
    }

    /**
     * <p>
     * Sets the value of a private field in the given class. The field has the
     * given name. The value is retrieved from the given instance. If the
     * instance is null, the field is a static field. If any error occurs, null
     * is returned.
     * </p>
     *
     * @param type
     *            The class which the private field belongs to.
     * @param instance
     *            The instance which the private field belongs to.
     * @param name
     *            The name of the private field.
     * @param value
     *            The value of the private filed to be set.
     * @since 2.2
     */
    public static void setPrivateField(Class<?> type, Object instance, String name, Object value) {
        Field field = null;
        try {
            field = type.getDeclaredField(name);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalArgumentException e) {
            // Ignore
        } catch (IllegalAccessException e) {
            // Ignore
        } catch (SecurityException e) {
            // Ignore
        } catch (NoSuchFieldException e) {
            // Ignore
        } finally {
            if (field != null) {
                // reset the accessibility
                field.setAccessible(false);
            }
        }
    }

    /**
     * Get the private method.
     *
     * @param clazz
     *            the clazz.
     * @param methodName
     *            the method name.
     * @param paramsTypes
     *            the params types.
     * @return the method.
     * @throws Exception
     *             to JUnit.
     * @since 2.2
     */
    public static Method getPrivateMethod(Class<?> clazz, String methodName, Class<?>[] paramsTypes) throws Exception {
        Method method = null;
        method = clazz.getDeclaredMethod(methodName, paramsTypes);
        method.setAccessible(true);
        return method;
    }
}
