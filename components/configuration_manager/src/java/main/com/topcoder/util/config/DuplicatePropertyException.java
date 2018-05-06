/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

/**
 * Thrown when there is an attempt to add a nested property to <code>Property
 * </code> with name that already exists within the <code>Property</code>.
 *
 * @author  isv, WishingBone
 * @version 1.0  05/07/2003
 * @since   Configuration Manager 2.1
 */
@SuppressWarnings("serial")
public class DuplicatePropertyException extends ConfigManagerException {

    /**
     * Constructs a <code>DuplicatePropertyException</code> with <code>null
     * </code> as it's error detail message.
     */
    public DuplicatePropertyException() {
        super();
    }

    /**
     * Constructs a <code>DuplicatePropertyException</code> with the specified
     * detail message. The error message string <code>detail</code> can later be
     * retrieved by the <code>getMessage()</code> method.
     *
     * @param detail the detail message
     */
    public DuplicatePropertyException(String detail) {
        super(detail);
    }

}
