/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.testclasses;

/**
 * Test class 3.
 *
 * @author TCSDEVELOPER
 * @version 2.2
 * @since 2.2
 */
public class TestClass3 {
    /**
     * Private field for test.
     */
    private final String represent;

    /**
     * Default constructor for test.
     */
    public TestClass3() {
        represent = "TestClass3";
    }

    /**
     * Test constructor.
     *
     * @param p1
     *            param1.
     * @param p2
     *            param2.
     * @param p3
     *            param3.
     * @param p4
     *            param4.
     * @param p5
     *            param5.
     * @param p6
     *            param6.
     * @param p7
     *            param7.
     * @param p8
     *            param8.
     * @param p9
     *            param9.
     * @param p10
     *            param10.
     */
    public TestClass3(boolean p1, byte p2, char p3, double p4, float p5, int p6, long p7, short p8, String p9,
            String p10) {
        represent = "TestClass3" + p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9 + p10;
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
