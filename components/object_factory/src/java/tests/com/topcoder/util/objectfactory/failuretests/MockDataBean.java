/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.failuretests;


/**
 * The is a simple mock databean.
 * Used to test the Object Factory 2.0 component.
 *
 */
public class MockDataBean {
    /**
     * A int member.
     */
    private int state = 0;

    /**
     * A String member.
     */
    private String string = "string";

    /**
     * The MockSubDataBean member.
     */
    private MockSubDataBean subBean = null;

    /**
     * The constructor of the mock MockDataBean
     *
     * @param state the int state
     * @param string the String
     * @param subBean the MockSubDataBean
     */
    public MockDataBean(int state, String string, MockSubDataBean subBean) {
        this.state = state;
        this.string = string;
        this.subBean = subBean;
    }
}
