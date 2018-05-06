/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

/**
 * <p>
 * A convenient enumeration of phase operations as defined by the Phase Manager API which currently allows operations
 * of "start" and "end". This is provided as a utility to the user when they need to identify the phase
 * operation when registering a phase handler with a manager. This is used when creating HandlerRegistryInfo instances
 * for handler registrations (used as keys to id handlers), which need to know what operation the handler will handle
 * (as well as which phase status it will deal with - which is covered by PhaseStatusEnum).
 * </p>
 * <p>
 * Changes in 1.1:
 * <ol>
 * <li>Made this class a Java enum instead of extending Enum from Type Safe Enum component.</li>
 * <li>Removed CANCEL enum value.</li>
 * </ol>
 * </p>
 * <p>
 * Thread Safety: This class is immutable and thread safe.
 * </p>
 * @author AleaActaEst, saarixx, RachaelLCook, sokol
 * @version 1.1
 */
public enum PhaseOperationEnum {
    /**
     * <p>
     * Represents the start operation for a phase. This is used to map PhaseHandlers to specific operations.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Converted to enum value.</li>
     * </ol>
     * </p>
     */
    START("start"),
    /**
     * <p>
     * Represents the end operation for a phase. This is used to map PhaseHandlers to specific operations.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Converted to enum value.</li>
     * </ol>
     * </p>
     */
    END("end");

    /**
     * Represents the name of the operation for this instance. It will contain "start" or "end" only.
     */
    private final String name;

    /**
     * <p>
     * Private constructor to prevent instantiation 'from the outside'. When called will assign the <code>name</code>
     * to <code>this.name</code>.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Removed check on null/empty value.</li>
     * </ol>
     * </p>
     * @param name the name of the operation
     */
    private PhaseOperationEnum(String name) {
        this.name = name;
    }

    /**
     * Accessor for the name of this operation.
     * @return the name of this operation
     */
    public String getName() {
        return this.name;
    }
}
