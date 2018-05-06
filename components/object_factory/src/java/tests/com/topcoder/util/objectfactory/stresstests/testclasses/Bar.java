/*
 * Copyright (c) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.stresstests.testclasses;


/**
 * A class used for testing.
 *
 * @author Wendell
 * @version 1.0
 */
public class Bar {
    /** The float value of this Bar. */
    private float fv;

    /** The StringBuffer value of this Bar. */
    private StringBuffer sb;

    /**
     * Creates a new Bar with the given arguments.
     *
     * @param fv the float value.
     * @param sb the StringBuffer value.
     */
    public Bar(float fv, StringBuffer sb) {
    }

    /**
     * Gets the float value of the Bar.
     *
     * @return the float value of this Bar.
     */
    public float getFv() {
        return fv;
    }

    /**
     * Sets the float value of this Bar.
     *
     * @param fv the float value of this Bar.
     */
    public void setFv(float fv) {
        this.fv = fv;
    }

    /**
     * Gets the StringBuffer value of this Bar.
     *
     * @return the StringBuffer value of this Bar.
     */
    public StringBuffer getSb() {
        return sb;
    }

    /**
     * Sets the StringBuffer value of this Bar.
     *
     * @param sb the StringBuffer value of this Bar.
     */
    public void setSb(StringBuffer sb) {
        this.sb = sb;
    }
}
