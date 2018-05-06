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
public class TestClass2 {
    /**
     * A <code>TestClass1</code> field.
     */
    private TestClass1 testClass1;

    /**
     * A <code>float</code> field.
     */
    private float f;

    /**
     * Construct an instance with the given values.
     *
     * @param testClass1
     *            an <code>TestClass1</code> value
     * @param f
     *            a <code>float</code> value
     */
    public TestClass2(TestClass1 testClass1, float f) {
        this.testClass1 = testClass1;
        this.f = f;
    }

    /**
     * Get the <code>TestClass1</code> field.
     *
     * @return a <code>TestClass1</code> value.
     */
    public TestClass1 getTestClass1() {
        return testClass1;
    }

    /**
     * Get the <code>float</code> field.
     *
     * @return a <code>float</code> value.
     */
    public float getF() {
        return f;
    }
}
