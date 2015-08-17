/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;

/**
 * <p>A base class for those classes providing library calls to EJBs which have their business interface extending
 * javax.ejb.EJBObject interface.</p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public abstract class BaseEJBLibrary {

    /**
     * <p>Does nothing.</p>
     */
    public void remove() {
        // Do nothing
    }

    /**
     * <p>Does nothing.</p>
     *
     * @return <code>null</code> always.
     */
    public Object getPrimaryKey() {
        return null;
    }

    /**
     * <p>Does nothing.</p>
     *
     * @return <code>null</code> always.
     */
    public EJBHome getEJBHome() {
        return null;
    }

    /**
     * <p>Does nothing.</p>
     *
     * @param ejbObject an <code>EJBObject</code> passed for identification.
     * @return <code>false</code> always.
     */
    public boolean isIdentical(EJBObject ejbObject) {
        return false;
    }

    /**
     * <p>Does nothing.</p>
     *
     * @return <code>null</code> always.
     */
    public Handle getHandle() {
        return null;
    }
}
