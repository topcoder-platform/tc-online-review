/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

/**
 * The simple class used in the unit test.
 *
 * @author sparemax
 * @version 1.0
 */
public class Bar {
    /**
     * A <code>float</code> field.
     */
    private float f;

    /**
     * A <code>StringBuffer</code> field.
     */
    private StringBuffer buffer;

    /**
     * Construct an instance with the given values.
     *
     * @param f
     *            a <code>float</code> value
     * @param buffer
     *            a <code>StringBuffer</code> value
     */
    public Bar(float f, StringBuffer buffer) {
        this.f = f;
        this.buffer = buffer;
    }

    /**
     * Get the <code>float</code> field.
     *
     * @return a <code>float</code> value.
     */
    public float getF() {
        return f;
    }

    /**
     * Get the <code>StringBuffer</code> field.
     *
     * @return a <code>StringBuffer</code> value.
     */
    public StringBuffer getBuffer() {
        return buffer;
    }
}
