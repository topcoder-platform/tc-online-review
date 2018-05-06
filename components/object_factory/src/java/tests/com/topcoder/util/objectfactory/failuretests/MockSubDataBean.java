/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.failuretests;


/**
 * The is a simple mock databean.
 * Used to test the Object Factory 2.0 component.
 *
 */
public class MockSubDataBean {
    /**
     * A int member.
     */
    private int a = 0;

    /**
     * A char member.
     */
    private char c = 'a';

    /**
     * A StringBuffer member.
     */
    private StringBuffer buffer = new StringBuffer();

    /**
     * The constructor of the Mock.
     *
     * @param a the int
     * @param c the char
     * @param buffer the StringBuffer
     */
    public MockSubDataBean(int a, char c, StringBuffer buffer) {
        this.a = a;
        this.c = c;
        this.buffer = buffer;
    }
}
