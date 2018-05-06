/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;

import junit.framework.TestCase;

/**
 * Failure test cases for class <code>Condition </code>.
 *
 * @author Chenhong
 * @version 2.1
 */
public class TestConditionFailure extends TestCase {

    /**
     * Represents the Condition instance for tesitng.
     */
    private Condition condition = null;

    /**
     * Set up the environment.
     *
     * @throws Exception
     *             to junit.
     */
    public void setUp() throws Exception {
        condition = new Condition("name", new NodeList(new Node[0]), "des", true, "statement");
    }

    /**
     * Test the constructor.
     *
     */
    public void testCondition_1() {
        try {
            new Condition(null, new NodeList(new Node[0]), "des", false, "statement");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // OK.
        }
    }

    /**
     * Test the constructor.
     *
     */
    public void testCondition_2() {
        try {
            new Condition("", new NodeList(new Node[0]), "des", false, "statement");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // OK.
        }
    }

    /**
     * Test the constructor.
     *
     */
    public void testCondition_3() {
        try {
            new Condition("name", null, "des", false, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // OK.
        }
    }

    /**
     * Test the constructor.
     *
     */
    public void testCondition_4() {
        try {
            new Condition("name", new NodeList(new Node[0]), "des", false, "");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // OK.
        }
    }

    /**
     * Test the constructor.
     *
     */
    public void testCondition_5() {
        try {
            new Condition("name", null, "des", false, "statement");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method setValue.
     *
     */
    public void testSetValue_1() {
        try {
            condition.setValue(null);
            fail("IllegalArgumentException is  expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method setValue.
     *
     */
    public void testSetValue_2() {
        try {
            condition.setValue("valuessss");
            fail("IllegalStateException is  expected.");
        } catch (IllegalStateException e) {
            // Ok.
        }
    }

}