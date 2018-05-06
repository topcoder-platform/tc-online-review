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
public class TestClass {
    /**
     * An <code>int</code> field.
     */
    private int intValue;

    /**
     * A <code>String</code> field.
     */
    private String str;

    /**
     * A <code>Bar</code> field.
     */
    private Bar bar;

    /**
     * Construct an instance with the given values.
     *
     * @param intValue
     *            an <code>int</code> value
     * @param str
     *            a <code>String</code> value
     * @param bar
     *            a <code>Bar</code> value
     */
    public TestClass(int intValue, String str, Bar bar) {
        this.intValue = intValue;
        this.str = str;
        this.bar = bar;
    }

    /**
     * Get the <code>int</code> field.
     *
     * @return an <code>int</code> value.
     */
    public int getIntValue() {
        return intValue;
    }

    /**
     * Get the <code>String</code> field.
     *
     * @return a <code>String</code> value.
     */
    public String getStr() {
        return str;
    }

    /**
     * Get the <code>Bar</code> field.
     *
     * @return a <code>Bar</code> value.
     */
    public Bar getBar() {
        return bar;
    }
}
