/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.AndValidator;
import com.topcoder.util.datavalidator.StringValidator;

import junit.framework.TestCase;
/**
 * Accuracy tests for the AndValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class AndValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * AndValidator instance for testing.
     * </p>
     */
    private AndValidator instance;
    /**
     * <p>
     * The StringValidator for testing.
     * </p>
     */
    private StringValidator validator1;
    /**
     * <p>
     * The StringValidator for testing.
     * </p>
     */
    private StringValidator validator2;

    /**
     * <p>
     * set up the test environment.
     * </p>
     */
    protected void setUp() {
        validator1 = StringValidator.startsWith("Accuracy");
        validator2 = StringValidator.endsWith("AndValidator");
        instance = new AndValidator(validator1,validator2);
    }
    public void tearDown(){
        instance = null;
        validator1 = null;
        validator2 = null;
        
    }

    /**
     * <p>
     * Accuracy test the default constructor.
     * </p>
     */
    public void testCtor1() {
        instance = new AndValidator();
        assertNotNull("The instance with no parameter should not be null.", instance);
    }

    /**
     * <p>
     * Accuracy test the constructor with the 2 parameter not null.
     * </p>
     */
    public void testCtor2() {
        assertNotNull("The instance with two parameters should not be null.", instance);
    }

    /**
     * <p>
     * Accuracy test method valid().
     * </p>
     */
    public void testValid() {
        assertTrue(instance.valid("Accuracy test for AndValidator"));
        assertFalse(instance.valid("Accuracy test for OrValidator"));
        assertFalse(instance.valid("Failure test for AndValidator"));
    }

    /**
     * <p>
     * Accuracy test method getMessage().
     * </p>
     */
    public void testGetMessage() {
        assertNull(instance.getMessage("Accuracy test for AndValidator"));
        assertNotNull("The message should not be null.", instance.getMessage("Accuracy test for OrValidator"));
        assertNotNull("The message should not be null.", instance.getMessage("Failure test for AndValidator"));
    }

    /**
     * <p>
     * Accuracy test method getMessages().
     * </p>
     */
    public void testGetMessages() {
        assertNull(instance.getMessages("Accuracy test for AndValidator"));
        assertNotNull("The message should not be null.", instance.getMessages("Accuracy test for OrValidator"));
        assertNotNull("The message should not be null.", instance.getMessages("Failure test for AndValidator"));
    }

    /**
     * <p>
     * Accuracy test method getAllMessages().
     * </p>
     */
    public void testGetAllMessages() {
        assertNull(instance.getAllMessages("Accuracy test for AndValidator"));
        assertNotNull("The message should not be null.", instance.getAllMessages("Accuracy test for OrValidator"));
        assertNotNull("The message should not be null.", instance.getAllMessages("Failure test for AndValidator"));
    }
}
