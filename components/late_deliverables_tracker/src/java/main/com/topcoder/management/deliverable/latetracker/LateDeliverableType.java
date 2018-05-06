/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

/**
 * <p>
 * This is an enumeration for late deliverable types. Currently two late deliverable types are supported by this
 * component: Missed Deadline and Rejected Final Fix.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This enumeration is immutable and thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.3
 * @since 1.3
 */
public enum LateDeliverableType {
    /**
     * Represents the missed deadline late deliverable type.
     */
    MISSED_DEADLINE("Missed Deadline"),

    /**
     * Represents the rejected final fix late deliverable type.
     */
    REJECTED_FINAL_FIX("Rejected Final Fix");

    /**
     * The string representation of the late deliverable type. Is initialized in the constructor and never changed
     * after that. Cannot be null or empty. Is used in toString().
     */
    private final String value;

    /**
     * Private constructor for LateDeliverableType enumeration values.
     *
     * @param value
     *            the string representation of the late deliverable type.
     */
    private LateDeliverableType(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the late deliverable type.
     *
     * @return the string representation of the late deliverable type
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Converts the given string value to LateDeliverableType instance.
     *
     * @param value
     *            the string value to be converted.
     *
     * @return the late deliverable type instance (null if no matching found).
     */
    public static LateDeliverableType fromString(String value) {
        for (LateDeliverableType lateDeliverableType : LateDeliverableType.values()) {
            if (lateDeliverableType.toString().equals(value)) {
                return lateDeliverableType;
            }
        }
        return null;
    }
}
