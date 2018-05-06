/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

/**
 * This is the base exception for the exceptions related to a group name.
 * @author Luca, FireIce
 * @version 1.0
 */
public class GroupException extends RegistryException {

    /**
     * Represents the group name for which the exception was raised. Initialized in the constructor and never changed
     * later. Can be null. Can be empty.
     */
    private final String groupName;

    /**
     * Creates an instance with the given arguments. Calls super(message) and assigns the argument to the corresponding
     * field.
     * @param message
     *            a descriptive message
     * @param groupName
     *            the group name
     */
    public GroupException(String message, String groupName) {
        super(message);
        this.groupName = groupName;
    }

    /**
     * Returns the group name for which the exception was raised. Simply return the corresponding field.
     * @return the group name for which the exception was raised
     */
    public String getGroupName() {
        return groupName;
    }
}
