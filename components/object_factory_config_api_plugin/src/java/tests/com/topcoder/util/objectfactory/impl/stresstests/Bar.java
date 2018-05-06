/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl.stresstests;

/**
 * The simple class used in the stress test.
 *
 * @author King_Bette
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
     * @param f a <code>float</code> value
     * @param buffer a <code>StringBuffer</code> value
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
        return this.f;
    }

    /**
     * Get the <code>StringBuffer</code> field.
     *
     * @return a <code>StringBuffer</code> value.
     */
    public StringBuffer getBuffer() {
        return this.buffer;
    }
}
