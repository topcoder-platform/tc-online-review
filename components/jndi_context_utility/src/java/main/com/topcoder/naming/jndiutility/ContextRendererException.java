/*
 * Copyright (C) 2003, 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0
 *
 * @ ContextRendererException.java
 */
package com.topcoder.naming.jndiutility;

import com.topcoder.util.errorhandling.BaseRuntimeException;


/**
 * An exception to be thrown if any concrete <code>ContextRenderer</code> implementation related exception occurs.
 * <code>ContextRenderer</code> implementation may throw to indicate that some error preventing it from further
 * context rendering occurred. In addition the <code>ContextRenderer</code> may specify the <code>Throwable</code>
 * that caused the <code>ContextRendererException</code> to be thrown.
 *
 * @author isv, preben
 * @author AleaActaEst, Charizard
 * @version 2.0
 *
 * @since 1.0
 */
public class ContextRendererException extends BaseRuntimeException {
    /**
     * <p>Constructs a new <code>ContextRendererException</code> with null as its detail message.</p>
     *
     * @since 1.0
     */
    public ContextRendererException() {
        super();
    }

    /**
     * <p>Constructs a new <code>ContextRendererException</code> with the specified detail message.</p>
     *
     * @param message the detail message
     *
     * @since 1.0
     */
    public ContextRendererException(String message) {
        super(message);
    }

    /**
     * <p>Constructs a new <code>ContextRendererException</code> with the specified cause.</p>
     *
     * @param cause a <code>Throwable</code> caused this exception to be thrown
     *
     * @since 1.0
     */
    public ContextRendererException(Throwable cause) {
        super(cause);
    }

    /**
     * <p>Constructs a new <code>ContextRendererException</code> with the specified message and cause.</p>
     *
     * @param message the detail message
     * @param cause a <code>Throwable</code> caused this exception to be thrown
     *
     * @since 2.0
     */
    public ContextRendererException(String message, Throwable cause) {
        super(message, cause);
    }
}
