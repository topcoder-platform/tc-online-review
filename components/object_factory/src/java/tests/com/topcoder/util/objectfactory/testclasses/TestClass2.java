/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.testclasses;

/**
 * Test class 2.
 *
 * @author mgmg
 * @version 2.0
 */
public class TestClass2 {
    /**
     * Private field for test.
     */
    private final String represent;

    /**
     * Default constructor for test.
     */
    public TestClass2() {
        represent = "TestClass2";
    }

    /**
     * Test constructor.
     *
     * @param a
     *            param1.
     * @param b
     *            param2.
     */
    public TestClass2(TestClass1 a, float b) {
        represent = "TestClass2" + (a == null ? "null" : a.toString()) + b;
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
