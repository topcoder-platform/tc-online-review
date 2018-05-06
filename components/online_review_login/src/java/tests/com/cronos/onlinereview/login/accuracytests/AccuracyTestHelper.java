/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.accuracytests;

import com.topcoder.naming.jndiutility.JNDIUtils;

import com.topcoder.security.NoSuchUserException;
import com.topcoder.security.TCSubject;
import com.topcoder.security.UserPrincipal;
import com.topcoder.security.admin.PrincipalMgrRemote;
import com.topcoder.security.admin.PrincipalMgrRemoteHome;

import com.topcoder.util.config.ConfigManager;

import java.lang.reflect.Field;

import java.util.Iterator;

import javax.naming.Context;


/**
 * <p>
 * Defines helper methods used in tests.
 * </p>
 *
 * @author PE
 * @version 1.0
 */
public final class AccuracyTestHelper {
    /**
     * <p>
     * Creates a new instance of <code>AccuracyTestHelper</code> class. The private constructor prevents the creation
     * of a new instance.
     * </p>
     */
    private AccuracyTestHelper() {
    }

    /**
     * <p>
     * add the config of given config file.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    public static void addConfig() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        configManager.add("accuracy/OnlineReviewLoginConfig.xml");
        configManager.add("com.topcoder.naming.jndiutility", "accuracy/JNDIUtils.properties",
            ConfigManager.CONFIG_PROPERTIES_FORMAT);
    }

    /**
     * <p>
     * clear the config.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    public static void clearConfig() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        for (Iterator iter = configManager.getAllNamespaces(); iter.hasNext();) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * <p>
     * Gets the value of a private field in the given class. The field has the given name. The value is retrieved from
     * the given instance.
     * </p>
     *
     * @param type the type of the class.
     * @param instance the instance which the private field belongs to.
     * @param name the name of the private field to be retrieved.
     *
     * @return the value of the private field or <code>null</code> if any error occurs.
     */
    public static Object getPrivateField(Class type, Object instance, String name) {
        Field field = null;
        Object obj = null;

        try {
            // Get the reflection of the field and get the value
            field = type.getDeclaredField(name);
            field.setAccessible(true);
            obj = field.get(instance);
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

        return obj;
    }

    /**
     * <p>
     * Sets the value of a private field in the given class.
     * </p>
     *
     * @param type the type of the class.
     * @param instance the instance which the private field belongs to.
     * @param name the name of the private field to be retrieved.
     * @param value the value to be set
     */
    public static void setPrivateField(Class type, Object instance, String name, Object value) {
        Field field = null;
        Object obj = null;

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

    /**
     * Create a user with given name and password. If the user does already exist, the password will be updated.
     *
     * @param name the name of the user
     * @param password the passowrd of the user.
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception to JUnit.
     */
    public static UserPrincipal createUser(String name, String password) throws Exception {
        PrincipalMgrRemote mgr = createPrincipalMgrRemote();

        // create or update the user
        UserPrincipal principal = null;

        try {
            principal = mgr.getUser(name);
            mgr.editPassword(principal, password, null);
        } catch (NoSuchUserException e) {
            principal = mgr.createUser(name, password, null);
        }

        return principal;
    }

    /**
     * Remove a user.
     *
     * @param principal the name of the user
     * @param subject the passowrd of the user.
     *
     * @throws Exception to JUnit.
     */
    public static void removeUser(UserPrincipal principal, TCSubject subject) throws Exception {
        PrincipalMgrRemote mgr = createPrincipalMgrRemote();

        mgr.removeUser(principal, subject);
    }

    private static PrincipalMgrRemote createPrincipalMgrRemote() throws Exception {
        String contextName = ConfigManager.getInstance().getString("com.cronos.onlinereview.login.AccuracyTest",
                "context_name");
        Context context = JNDIUtils.getContext(contextName);
        String principalBean = ConfigManager.getInstance().getString("com.cronos.onlinereview.login.AccuracyTest",
                "principal_bean_name");
        PrincipalMgrRemoteHome home = (PrincipalMgrRemoteHome) context.lookup(principalBean);

        return home.create();
    }
}
