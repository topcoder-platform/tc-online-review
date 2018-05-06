/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.accuracytests;

import java.util.ArrayList;
import java.util.List;

/**
 * The mock class used for accuracy test.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockClass2 {
    /** The instance of the StringBuffer used for test. */
    private StringBuffer buffer = null;

    /** The instance of the List used for test. */
    private List firstList = null;

    /** The instance of the List used for test. */
    private List secondList = null;

    /**
     * Creates a new MockClass2 object.
     *
     * @param buffer the buffer to assign.
     * @param firstList the first list to assign.
     * @param secondList the second list to assign.
     */
    public MockClass2(StringBuffer buffer, ArrayList firstList, ArrayList secondList) {
        this.buffer = buffer;
        this.firstList = firstList;
        this.secondList = secondList;
    }

    /**
     * Returns the value of buffer.
     *
     * @return the value of buffer.
     */
    public StringBuffer getBuffer() {
        return buffer;
    }

    /**
     * Returns the first list.
     *
     * @return the value of firstList.
     */
    public List getFirstList() {
        return firstList;
    }

    /**
     * Returns the second list.
     *
     * @return the value of secondList.
     */
    public List getSecondList() {
        return secondList;
    }
}
