/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.accuracytests;

/**
 * The mock class used for accuracy test.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockClass1 {
    /** The value of int type used for test. */
    private int intValue = 0;

    /** The value of String used for test. */
    private String strValue = null;

    /** The value of MockClass2 used for test. */
    private MockClass2 mockClass = null;

    /**
     * Creates a new MockClass1 object.
     *
     * @param intValue the value of int type.
     * @param strValue the value of String.
     * @param mockClass the value of MockClass2.
     */
    public MockClass1(int intValue, String strValue, MockClass2 mockClass) {
        this.intValue = intValue;
        this.strValue = strValue;
        this.mockClass = mockClass;
    }

    /**
     * Returns int value.
     *
     * @return the value of int type.
     */
    public int getIntValue() {
        return intValue;
    }

    /**
     * Returns the value of MockClass2.
     *
     * @return the value of MockClass2.
     */
    public MockClass2 getMockClass2() {
        return mockClass;
    }

    /**
     * Returns the value of String.
     *
     * @return the value of String.
     */
    public String getStrValue() {
        return strValue;
    }
}
