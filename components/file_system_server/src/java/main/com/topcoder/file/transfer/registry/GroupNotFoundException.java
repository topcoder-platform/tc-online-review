/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

/**
 * This exception is thrown by the FileSystemRegistry if a given group name cannot be found.
 * @author Luca, FireIce
 * @version 1.0
 */
public class GroupNotFoundException extends GroupException {

    /**
     * Creates an instance with the given arguments.
     * @param message
     *            a descriptive message
     * @param groupName
     *            the group name
     */
    public GroupNotFoundException(String message, String groupName) {
        super(message, groupName);
    }
}
