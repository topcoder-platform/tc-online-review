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
     * @param intValue an <code>int</code> value
     * @param str a <code>String</code> value
     * @param bar a <code>Bar</code> value
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
        return this.intValue;
    }

    /**
     * Get the <code>String</code> field.
     *
     * @return a <code>String</code> value.
     */
    public String getStr() {
        return this.str;
    }

    /**
     * Get the <code>Bar</code> field.
     *
     * @return a <code>Bar</code> value.
     */
    public Bar getBar() {
        return this.bar;
    }
}
