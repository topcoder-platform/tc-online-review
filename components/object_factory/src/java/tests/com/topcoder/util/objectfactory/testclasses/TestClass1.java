/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.testclasses;

/**
 * Test class 1.
 *
 * @author mgmg
 * @version 2.0
 */
public class TestClass1 {
    /**
     * Private field for test.
     */
    private final String represent;

    /**
     * Test constructor 1.
     *
     * @param a
     *            param1.
     * @param b
     *            param2.
     */
    public TestClass1(int a, String b) {
        represent = "TestClass1" + a + (b == null ? "null" : b);
    }

    /**
     * Test constructor 2.
     *
     * @param a
     *            param1.
     * @param b
     *            param2.
     * @throws IllegalArgumentException
     *             if b is null.
     */
    public TestClass1(int a, TestClass2 b) {
        if (b == null) {
            throw new IllegalArgumentException("The parameter should not be null.");
        }

        represent = "TestClass1" + a + b.toString();
    }

    /**
     * Test constructor 3.
     *
     * @param d
     *            the param.
     */
    public TestClass1(Double d) {
        represent = "TestClass1" + (d == null ? "null" : d.toString());
    }

    /**
     * Get the string representing class.
     *
     * @return get the string representing this class.
     */
    public String toString() {
        return represent;
    }
}
