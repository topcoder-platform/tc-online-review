/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class contains several static methods to manage the mappings from string
 * name to <code>ScheduledEnableObjectFactory</code> instances.
 * </p>
 * <p>
 * It is used by the <code>Job</code> class to get the
 * <code>ScheduledEnableObjectFactory</code> by the factory name(<code>runCommand</code>
 * argument of <code>setRunCommand(String)</code> method) when its job type is
 * <code>JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>.
 * </p>
 * <p>
 * Thread-safety: Its variable's reference is immutable and initialized to a
 * synchronized map. So this class is thread-safe.
 * </p>
 * @author Standlove, fuyun
 * @version 3.1
 * @since 3.1
 */
public class ScheduledEnableObjectFactoryManager {

    /**
     * <p>
     * Represents mappings from the factory name to the
     * <code>ScheduledEnableObjectFactory</code> instances.
     * </p>
     * <p>
     * The key must be non-null, non-empty string. And the value must be
     * non-null <code>ScheduledEnableObjectFactory</code>. It is initialized
     * when it is declared, and its reference is never changed afterwards.
     * </p>
     * <p>
     * Its content can be accessed/modified in all those get/add/remove/clear
     * methods. It must be non-null. It can be empty map.
     * </p>
     */
    private static final Map FACTORIES = Collections
            .synchronizedMap(new HashMap());

    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private ScheduledEnableObjectFactoryManager() {
    }

    /**
     * <p>
     * Gets the <code>ScheduledEnableObjectFactory</code> by the given name.
     * </p>
     * <p>
     * Returns <code>null</code> if the name doesn't exist.
     * </p>
     * or empty string.
     * @param name the factory name.
     * @return the corresponding <code>ScheduledEnableObjectFactory</code>
     *         object.
     * @throws IllegalArgumentException if the given argument is
     *             <code>null</code>.
     */
    public static ScheduledEnableObjectFactory getScheduledEnableObjectFactory(
            String name) {
        Util.checkStringNotNullAndEmpty(name, "name");
        return (ScheduledEnableObjectFactory) FACTORIES.get(name);
    }

    /**
     * <p>
     * Adds the factory name and <code>ScheduledEnableObjectFactory</code>
     * object mapping into this manager.
     * </p>
     * <p>
     * If the factory name already exists, the old value is overwritten by the
     * given factory.
     * </p>
     * @param name the factory name.
     * @param factory the <code>ScheduledEnableObjectFactory</code> object to
     *            add.
     * @throws IllegalArgumentException if any argument is <code>null</code>
     *             or the name argument is empty string.
     */
    public static void addScheduledEnableObjectFactory(String name,
            ScheduledEnableObjectFactory factory) {
        Util.checkStringNotNullAndEmpty(name, "name");
        Util.checkObjectNotNull(factory, "factory");
        FACTORIES.put(name, factory);
    }

    /**
     * <p>
     * Removes the factory by the given name.
     * </p>
     * <p>
     * If the name doesn't exist, nothing happens.
     * </p>
     * @param name the factory name for which the factory to remove from the
     *            manager.
     * @throws IllegalArgumentException if the given argument is null or empty
     *             string.
     */
    public static void removeScheduledEnableObjectFactory(String name) {
        Util.checkStringNotNullAndEmpty(name, "name");
        FACTORIES.remove(name);
    }

    /**
     * <p>
     * Gets all registered factory names as a string array to return.
     * </p>
     * <p>
     * If no factory is registered, it will return empty array.
     * </p>
     * @return all registered factory names.
     */
    public static String[] getScheduledEnableObjectFactoryNames() {
        return (String[]) FACTORIES.keySet().toArray(
                new String[0]);
    }

    /**
     * <p>
     * Clears all registered factories.
     * </p>
     */
    public static void clearScheduledEnableObjectFactories() {
        FACTORIES.clear();
    }

}
