/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.accuracytests;

import junit.framework.TestCase;

import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;

/**
 * <p>
 * Test the Condition class
 * </p>
 *
 * @author evilisneo
 * @version 2.1
 */
public class ConditionAccuracyTests extends TestCase {
    /**
     * <p>
     * The Condition instance for testing.
     * </p>
     */
    private Condition condition;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     */
    protected void setUp() {
        condition = new Condition("name", new NodeList(new Node[0]), "description", false, "statement");
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     */
    protected void tearDown() {
        condition = null;
    }

    /**
     * <p>
     * Tests {@link Condition#Condition(String, String, String, boolean, String)} for accuracy.
     * </p>
     */
    public void testConditionAccuracy() {
        assertNotNull("Condition construction failed", condition);
    }

    /**
     * <p>
     * Tests {@link Condition#Condition(String, String, String, boolean, String)} for accuracy with the
     * defaultValue as empty.
     * </p>
     */
    public void testConditionAccuracy1() {
        assertNotNull("Condition construction failed", new Condition("name", new NodeList(new Node[0]), "description",
            true, "statement"));
    }

    /**
     * <p>
     * Tests {@link Condition#Condition(String, String, String, boolean, String)} for accuracy with the description
     * as null.
     * </p>
     */
    public void testConditionAccuracy2() {
        assertNotNull("Condition construction failed", new Condition("name", new NodeList(new Node[0]), null, true,
            "statement"));
    }

    /**
     * <p>
     * Tests {@link Condition#getConditionalStatement()} for accuracy.
     * </p>
     */
    public void testgetConditionalStatementAccuracy1() {
        assertEquals("getConditionalStatement failed.", "statement", condition.getConditionalStatement());
    }

    /**
     * <p>
     * Tests {@link Condition#getName()} for accuracy.
     * </p>
     */
    public void testGetNameAccuracy() {
        assertEquals("getName failed", "name", condition.getName());
    }

    /**
     * <p>
     * Tests {@link Condition#getValue()} for accuracy.
     * </p>
     */
    public void testGetValueAccuracy() {
        assertEquals("getValue failed", "", condition.getValue());
    }

    /**
     * <p>
     * Tests {@link Condition#isReadOnly()} for accuracy.
     * </p>
     */
    public void testIsReadOnlyAccuracy() {
        assertFalse("isReadOnly failed", condition.isReadOnly());
    }

    /**
     * <p>
     * Tests {@link Condition#setValue(String)} for accuracy.
     * </p>
     */
    public void testSetValueAccuracy() {
        condition.setValue("NewValue");
        assertEquals("setValue failed.", "NewValue", condition.getValue());
    }

    /**
     * <p>
     * Tests {@link Condition#setValue(String)} for accuracy with empty value.
     * </p>
     */
    public void testSetValueAccuracy1() {
        condition.setValue("");
        assertEquals("setValue failed.", "", condition.getValue());
    }

    /**
     * <p>
     * Tests {@link Condition#getDescription()} for accuracy.
     * </p>
     */
    public void testGetDescriptionAccuracy() {
        assertEquals("getDescription failed.", "description", condition.getDescription());
    }

}