/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

/**
 * <p>
 * An enumeration of currently supported phase statuses.
 * </p>
 * <p>
 * Changes in 1.1:
 * <ol>
 * <li>Made this class a Java enum instead of extending Enum from Type Safe Enum component.</li>
 * </ol>
 * </p>
 * <p>
 * Thread Safety: This class is immutable and thread safe.
 * </p>
 * @author AleaActaEst, saarixx, RachaelLCook, sokol
 * @version 1.0
 */
public enum PhaseStatusEnum {
    /**
     * <p>
     * Represents the scheduled status for a phase as mapped to the data base (uses id of 1).
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Converted to enum value.</li>
     * </ol>
     * </p>
     */
    SCHEDULED("Scheduled", 1),
    /**
     * <p>
     * Represents the open status for a phase as mapped to the data base (uses id of 2).
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Converted to enum value.</li>
     * </ol>
     * </p>
     */
    OPEN("Open", 2),
    /**
     * <p>
     * Represents the closed status for a phase as mapped to the data base (uses id of 3).
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Converted to enum value.</li>
     * </ol>
     * </p>
     */
    CLOSED("Closed", 3);

    /**
     * <p>
     * Represents the name of the phase status for this instance. It will currently contain "scheduled", "open", or
     * "closed" only.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Made final.</li>
     * </ol>
     * </p>
     */
    private final String name;

    /**
     * <p>
     * Represents the id of the record in the data store for the specific phase. It will currently contain 1, 2, or 3.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Made final.</li>
     * </ol>
     * </p>
     */
    private final int id;

    /**
     * <p>
     * Private constructor to prevent instantiation 'from the outside'. When called, it will assign <code>name</code>
     * to <code>this.name</code> and <code>id</code> to <code>this.id</code>.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Removed check on null value.</li>
     * </ol>
     * </p>
     * @param name the name of the phase status
     * @param id the store id of the phase
     */
    private PhaseStatusEnum(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Accessor for the name of this phase status.
     * @return the name of this phase status
     */
    public String getName() {
        return this.name;
    }

    /**
     * Accessor for the ID of this phase status.
     * @return the ID of this phase status
     */
    public int getId() {
        return this.id;
    }
}
