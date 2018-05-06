/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Iterator;
import java.util.Locale;


/**
 * <p>
 * Test the functionality of class <code>AbstractAssociativeObjectValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public abstract class AbstractAssociativeObjectValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * An instance of <code>ObjectValidator</code> for testing.<br>
     * </p>
     */
    protected ObjectValidator validator1 = null;

    /**
     * <p>
     * An instance of <code>ObjectValidator</code> for testing.<br>
     * </p>
     */
    protected ObjectValidator validator2 = null;

    /**
     * <p>
     * An instance of <code>AbstractAssociativeObjectValidator</code> for testing.<br>
     * </p>
     */
    protected AbstractAssociativeObjectValidator validator = null;

    /**
     * <p>
     * Failure test case for method 'addValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testAddValidator_ObjectValidator_Null1() {
        try {
            validator.addValidator(null);
            fail("Test failure for addValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'addValidator()'.<br>
     * </p>
     */
    public void testAddValidator_ObjectValidator_Accuracy1() {
        validator.addValidator(validator1);

        int cnt = 0;

        for (Iterator iter = validator.getValidators(); iter.hasNext();) {
            iter.next();
            ++cnt;
        }

        assertEquals("Test accuracy for method addValidator() failed.", 3, cnt);
    }

    /**
     * <p>
     * Failure test case for method 'removeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testRemoveValidator_ObjectValidator_Null1() {
        try {
            validator.removeValidator(null);

            fail("Test failure for removeValidator() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'removeValidator()'.<br>
     * </p>
     */
    public void testRemoveValidator_ObjectValidator_Accuracy1() {
        validator.addValidator(validator1);
        validator.addValidator(validator2);
        validator.removeValidator(validator2);

        int cnt = 0;

        for (Iterator iter = validator.getValidators(); iter.hasNext();) {
            iter.next();
            ++cnt;
        }

        assertEquals("Test accuracy for method removeValidator() failed.", 3, cnt);
    }

    /**
     * <p>
     * Accuracy test case for method 'getValidators()'.<br>The value of field 'validators' should be return properly.
     * </p>
     */
    public void testGetValidators_Accuracy() {
        validator.addValidator(validator1);

        Iterator iter = validator.getValidators();

        assertEquals("Test accuracy for method getValidators() failed, the value should be return properly.",
            validator1, iter.next());
    }

    /**
     * <p>
     * Get the <code>AbstractObjectValidator</code> instance.<br>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator getObjectValidator() {
        return validator;
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on AbstractAssociativeObjectValidator
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as AbstractAssociativeObjectValidator does not provide a corresponding constructor.
     * </p>
     *
     * @param validationMessage This is the validation message to use for the underlying validator.
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator(String validationMessage) {
        fail("constructor not supported");

        return null;  // never reached
    }

}
