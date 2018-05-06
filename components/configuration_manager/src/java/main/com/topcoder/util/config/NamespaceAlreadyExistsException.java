/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

/**
 * Thrown when there is an attempt to add a <code>Namespace</code> which already
 * exists in <code>ConfigManager</code>.
 *
 * @author  debedeb, ilya, isv, WishingBone
 * @version 2.1  05/07/2003
 */
@SuppressWarnings("serial")
public class NamespaceAlreadyExistsException extends ConfigManagerException {

    /**
     * Constructs a <code>NamespaceAlreadyExistsException</code> with <code>null
     * </code> as its error detail message.
     */
    public NamespaceAlreadyExistsException() {
        super();
    }

    /**
     * Constructs a <code>NamespaceAlreadyExistsException</code> with the
     * specified detail message. The error message string <code>detail</code>
     * can later be retrieved by the <code>getMessage()</code> method.
     *
     * @param detail the detail message
     */
    public NamespaceAlreadyExistsException(String detail) {
        super(detail);
    }

}
