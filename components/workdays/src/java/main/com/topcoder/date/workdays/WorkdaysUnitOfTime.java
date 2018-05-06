/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays;

import com.topcoder.util.collection.typesafeenum.Enum;

/**
 * <p>
 * Instances of this class represent units of time. It has a private constructor, so only the three public static final
 * instances can be used: MINUTES, HOURS and DAYS.
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong> This class is immutable and thread safe.
 * </p>
 *
 * <p>
 * <strong> Change log:</strong> Trivial code fixes: Made name and value attributes final.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.1
 */
public class WorkdaysUnitOfTime extends Enum {
    /**
     * <p>
     * This represents minutes as the unit of time to add to a given date. Is initialized during class loading and never
     * changed after that. Cannot be null.
     * </p>
     */
    public static final WorkdaysUnitOfTime MINUTES = new WorkdaysUnitOfTime("MINUTES", 0);

    /**
     * <p>
     * This represents hours as the unit of time to add to a given date. Is initialized during class loading and never
     * changed after that. Cannot be null.
     * </p>
     */
    public static final WorkdaysUnitOfTime HOURS = new WorkdaysUnitOfTime("HOURS", 1);

    /**
     * <p>
     * This represents days as the unit of time to add to a given date. Is initialized during class loading and never
     * changed after that. Cannot be null.
     * </p>
     */
    public static final WorkdaysUnitOfTime DAYS = new WorkdaysUnitOfTime("DAYS", 2);

    /**
     * <p>
     * Name of this unit of time. It is used by toString() for a descriptive display of an instance of this class. Is
     * initialized in the constructor and never changed after that. Cannot be null/empty.
     * </p>
     */
    private final String name;

    /**
     * <p>
     * Value of this unit of time. It is used for ordering the unit of time. Is initialized in the constructor and never
     * changed after that. Cannot be negative. Has a getter.
     * </p>
     */
    private final int value;

    /**
     * <p>
     * Private constructor to create a WorkdaysUnitOfTime with the given name and the given value.
     * </p>
     *
     * @param name
     *            name for the unit of time
     * @param value
     *            value for the unit of time
     *
     * @throws NullPointerException
     *             when parameter name is null
     * @throws IllegalArgumentException
     *             when parameter name is empty
     */
    private WorkdaysUnitOfTime(String name, int value) {
        if (name == null) {
            throw new NullPointerException("Parameter name is null");
        }

        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("parameter name is empty");
        }

        this.name = name;
        this.value = value;
    }

    /**
     * <p>
     * Getter for the value of a WorkdaysUnitOfTime instance. The value is used for ordering.
     * </p>
     *
     * @return value for the unit of time
     */
    public int getValue() {
        return this.value;
    }

    /**
     * <p>
     * Returns a descriptive string of an instance of WorkdaysUnitOfTime class.
     * </p>
     *
     * @return name of this unit of time
     */
    public String toString() {
        return this.name;
    }
}
