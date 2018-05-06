/*
 * Copyright (C) 2003 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

/**
 * Thrown from any of <code>ConfigManager</code>'s methods (except <code>
 * existsNamespace()</code> that is invoked with namespace that does not exist
 * within <code>ConfigManager</code>.
 *
 * @author  debedeb, ilya, isv, WishingBone
 * @version 2.1  05/07/2003
 */
@SuppressWarnings("serial")
public class UnknownNamespaceException extends ConfigManagerException  {

    /**
     * Constructs an <code>UnknownNamespaceException</code> with <code>null
     * </code> as its error detail message.
     */
    public UnknownNamespaceException() {
        super();
    }

    /**
     * Constructs an <code>UnknownNamespaceException</code> with the specified
     * detail message. The error message string <code>detail</code> can later be
     * retrieved by the <code>getMessage()</code> method.
     *
     * @param detail the detail message
     */
    public UnknownNamespaceException(String detail) {
        super(detail);
    }

}
