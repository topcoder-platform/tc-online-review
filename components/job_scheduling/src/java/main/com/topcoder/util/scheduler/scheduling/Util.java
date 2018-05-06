/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import java.util.List;

/**
 * <p>
 * Helper class for the whole component.
 * </p>
 *
 * <p>
 * Thread Safety : This class is immutable and so thread safe.
 * </p>
 *
 * @author singlewood
 * @author TCSDEVELOPER
 * @version 3.0
 * @since 2.0
 */
public final class Util {
    /**
     * <p>
     * Represents the length of one day in milliseconds.
     * </p>
     */
    public static final int ONE_DAY = 86400000;

    /**
     * <p>
     * Represents the total days of a week.
     * </p>
     */
    static final int WEEK_COUNT = 7;

    /**
     * <p>
     * Private empty constructor.
     * </P>
     */
    private Util() {
    }

    /**
     * <p>
     * Checks if the object is null.
     * </p>
     *
     * @param obj the object to check.
     * @param name the object name
     * @throws IllegalArgumentException if the object is null.
     */
    public static void checkObjectNotNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException("the object " + name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks if the string is null or empty.
     * </p>
     *
     * @return true if the string is not null and not empty.
     * @param str the String to check
     * @param name the name of string.
     * @throws IllegalArgumentException if the string is empty.
     */
    public static boolean checkStringNotNullAndEmpty(String str, String name) {
        checkObjectNotNull(str, name);
        if (str.trim().length() == 0) {
            throw new IllegalArgumentException(name + " can't be empty.");
        }
        return true;
    }

    /**
     * <p>
     * Checks the list the list is null or empty.
     * </p>
     *
     * @param list the list to check.
     * @param name the object name
     * @throws IllegalArgumentException if the list null or empty.
     */
    public static void checkListNotNullAndEmpty(List list, String name) {
        checkObjectNotNull(list, name);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("The " + name + " can't be empty.");
        }
    }

    /**
     * <p>
     * Checks if the event is null and is one of the three legal events.
     * </p>
     *
     * @param event the event to check
     * @param name the event's name
     * throws IllegalArgumentException if the event is illegal.
     */
    public static void checkEventHandler(String event, String name) {
        checkObjectNotNull(event, name);

        if (!(event.equals(EventHandler.FAILED) || event.equals(EventHandler.NOT_STARTED)
            || event.equals(EventHandler.SUCCESSFUL))) {
            throw new IllegalArgumentException(name + " is not one of three legal events: EventHandler.FAILED,"
                + " EventHandler.NOT_STARTED, EventHandler.SUCCESSFUL");
        }
    }
}
