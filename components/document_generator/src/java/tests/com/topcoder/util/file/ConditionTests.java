/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for Condition.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.1
 */
public class ConditionTests extends TestCase {
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
     *
     */
    protected void setUp() {
        condition = new Condition("name", new NodeList(new Node[0]), "description", false, "conditionalStatement");
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        condition = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(ConditionTests.class);
    }

    /**
     * <p>
     * Tests ctor Condition#Condition(String,NodeList,String,boolean,String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created Condition instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new Condition instance.", condition);
    }

    /**
     * <p>
     * Tests ctor Condition#Condition(String,NodeList,String,boolean,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when name is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullName() {
        try {
            new Condition(null, new NodeList(new Node[0]), "description", true, "conditionalStatement");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Condition#Condition(String,NodeList,String,boolean,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when name is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyName() {
        try {
            new Condition("   ", new NodeList(new Node[0]), "description", true, "conditionalStatement");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Condition#Condition(String,NodeList,String,boolean,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when subNodes is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullSubNodes() {
        try {
            new Condition("name", null, "description", true, "conditionalStatement");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Condition#Condition(String,NodeList,String,boolean,String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that when description is null and expects success.
     * </p>
     */
    public void testCtor_NullDescription() {
        assertNotNull("Failed to create a new Condition instance.", new Condition("name", new NodeList(new Node[0]),
            null, true, "conditionalStatement"));
    }

    /**
     * <p>
     * Tests ctor Condition#Condition(String,NodeList,String,boolean,String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that when description is empty and expects success.
     * </p>
     */
    public void testCtor_EmptyDescription() {
        assertNotNull("Failed to create a new Condition instance.", new Condition("name", new NodeList(new Node[0]),
            " ", true, "conditionalStatement"));
    }

    /**
     * <p>
     * Tests ctor Condition#Condition(String,NodeList,String,boolean,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when conditionalStatement is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullConditionalStatement() {
        try {
            new Condition("name", new NodeList(new Node[0]), "description", true, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Condition#Condition(String,NodeList,String,boolean,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when conditionalStatement is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyConditionalStatement() {
        try {
            new Condition("name", new NodeList(new Node[0]), "description", true, "   ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Condition#getConditionalStatement() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Condition#getConditionalStatement() is correct.
     * </p>
     */
    public void testGetConditionalStatement() {
        assertEquals("Failed to get conditional statement.", "conditionalStatement",
            condition.getConditionalStatement());
    }

    /**
     * <p>
     * Tests Condition#getName() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Condition#getName() is correct.
     * </p>
     */
    public void testGetName() {
        assertEquals("Failed to get name.", "name", condition.getName());
    }

    /**
     * <p>
     * Tests Condition#getValue() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Condition#getValue() is correct.
     * </p>
     */
    public void testGetValue() {
        assertEquals("Failed to get value.", "", condition.getValue());
    }

    /**
     * <p>
     * Tests Condition#isReadOnly() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Condition#isReadOnly() is correct.
     * </p>
     */
    public void testIsReadOnly() {
        assertFalse("Failed to return value correctly.", condition.isReadOnly());
    }

    /**
     * <p>
     * Tests Condition#setValue(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies Condition#setValue(String) is correct.
     * </p>
     */
    public void testSetValue() {
        condition.setValue("value");
        assertEquals("Failed to set value.", "value", condition.getValue());
    }

    /**
     * <p>
     * Tests Condition#setValue(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when value is null and expects IllegalArgumentException.
     * </p>
     */
    public void testSetValue_NullValue() {
        try {
            condition.setValue(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Condition#setValue(String) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case that when value is empty and expects success.
     * </p>
     */
    public void testSetValue_EmptyValue() {
        condition.setValue(" ");
        assertEquals("Failed to set value.", " ", condition.getValue());
    }

    /**
     * <p>
     * Tests Condition#setValue(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when value is empty and expects IllegalStateException.
     * </p>
     */
    public void testSetValue_ReadOnly() {
        condition = new Condition("name", new NodeList(new Node[0]), "description", true, "conditionalStatement");
        try {
            condition.setValue("ReadOnly");
            fail("IllegalStateException expected.");
        } catch (IllegalStateException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Condition#getDescription() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Condition#getDescription() is correct.
     * </p>
     */
    public void testGetDescription() {
        assertEquals("Failed to get description.", "description", condition.getDescription());
    }

}