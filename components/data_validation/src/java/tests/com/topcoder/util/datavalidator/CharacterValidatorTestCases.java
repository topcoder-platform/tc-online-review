/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * Test the functionality of class <code>CharacterValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class CharacterValidatorTestCases extends AbstractObjectValidatorTestCases {
    /**
     * <p>
     * A String represents the error message.
     * </p>
     */
    private static final String ERROR_MESSAGE = "invalid Character";

    /**
     * <p>
     * An instance of <code>CharacterValidator</code> for testing.<br>
     * </p>
     */
    private CharacterValidator validator = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        validator = CharacterValidator.isDigit();
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_Object_Accuracy1() {
        String str = validator.getMessage("24");

        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, str);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_Object_Accuracy3() {
        String str = validator.getMessage("2");

        assertNull("Test accuracy for method getMessage() failed.", str);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_Object_Accuracy4() {
        String str = validator.getMessage(new Object());
        assertEquals("Test accuracy for method getMessage() failed.", ERROR_MESSAGE, str);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_char_Accuracy1() {
        String str = validator.getMessage('a');

        assertEquals("Test accuracy for method getMessage() failed.", "not a digit", str);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessage()'.<br>
     * </p>
     */
    public void testGetMessage_char_Accuracy2() {
        String str = validator.getMessage('9');

        assertNull("Test accuracy for method getMessage() failed.", str);
    }

    /**
     * <p>
     * Accuracy test case for method 'isDigit()'.<br>
     * </p>
     */
    public void testIsDigit_BundleInfo_Accuracy1() {
        assertEquals("Test accuracy for method isDigit() failed.", 0, 0);
    }

    /**
     * <p>
     * Accuracy test case for method 'isDigit()'.<br>
     * </p>
     */
    public void testIsDigit_Accuracy1() {
        String str = validator.getMessage('0');

        assertNull("Test accuracy for method isDigit() failed.", str);
    }

    /**
     * <p>
     * Failure test case for method 'isLetter()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testIsLetter_BundleInfo_Null1() {
        try {
            CharacterValidator.isLetter(null);
            fail("Test failure for isLetter() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'isLetter()'.<br>
     * </p>
     */
    public void testIsLetter_Accuracy1() {
        validator = CharacterValidator.isLetter();
        assertFalse("Test accuracy for method isLetter() failed.", validator.valid('1'));
        assertFalse("Test accuracy for method isLetter() failed.", validator.valid('^'));
        assertFalse("Test accuracy for method isLetter() failed.", validator.valid('&'));
        assertTrue("Test accuracy for method isLetter() failed.", validator.valid('a'));
        assertTrue("Test accuracy for method isLetter() failed.", validator.valid('A'));
        assertTrue("Test accuracy for method isLetter() failed.", validator.valid('h'));
    }

    /**
     * <p>
     * Failure test case for method 'isLetterOrDigit()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testIsLetterOrDigit_BundleInfo_Null1() {
        try {
            CharacterValidator.isLetterOrDigit(null);
            fail("Test failure for isLetterOrDigit() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'isLetterOrDigit()'.<br>
     * </p>
     */
    public void testIsLetterOrDigit_BundleInfo_Accuracy1() {
        validator = CharacterValidator.isLetterOrDigit();

        assertFalse("Test accuracy for method isLetter() failed.", validator.valid('^'));
        assertFalse("Test accuracy for method isLetter() failed.", validator.valid('&'));
        assertTrue("Test accuracy for method isLetter() failed.", validator.valid('1'));
        assertTrue("Test accuracy for method isLetter() failed.", validator.valid('9'));
        assertTrue("Test accuracy for method isLetter() failed.", validator.valid('a'));
        assertTrue("Test accuracy for method isLetter() failed.", validator.valid('A'));
        assertTrue("Test accuracy for method isLetter() failed.", validator.valid('h'));
    }

    /**
     * <p>
     * Failure test case for method 'isLowerCase()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testIsLowerCase_BundleInfo_Null1() {
        try {
            CharacterValidator.isLowerCase(null);
            fail("Test failure for isLowerCase() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'isLowerCase()'.<br>
     * </p>
     */
    public void testIsLowerCase_Accuracy1() {
        validator = CharacterValidator.isLowerCase();
        assertTrue("Test accuracy for method isLowerCase() failed.", validator.valid('a'));
        assertTrue("Test accuracy for method isLowerCase() failed.", validator.valid('b'));
        assertTrue("Test accuracy for method isLowerCase() failed.", validator.valid('z'));

        assertFalse("Test accuracy for method isLowerCase() failed.", validator.valid('A'));
        assertFalse("Test accuracy for method isLowerCase() failed.", validator.valid('B'));
        assertFalse("Test accuracy for method isLowerCase() failed.", validator.valid('9'));
        assertFalse("Test accuracy for method isLowerCase() failed.", validator.valid('&'));
    }

    /**
     * <p>
     * Accuracy test case for method 'isUpperCase()'.<br>
     * </p>
     */
    public void testIsUpperCase_Accuracy1() {
        validator = CharacterValidator.isUpperCase();
        assertFalse("Test accuracy for method isUpperCase() failed.", validator.valid('a'));
        assertFalse("Test accuracy for method isUpperCase() failed.", validator.valid('b'));
        assertFalse("Test accuracy for method isUpperCase() failed.", validator.valid('z'));
        assertFalse("Test accuracy for method isUpperCase() failed.", validator.valid('9'));
        assertFalse("Test accuracy for method isUpperCase() failed.", validator.valid('&'));

        assertTrue("Test accuracy for method isUpperCase() failed.", validator.valid('A'));
        assertTrue("Test accuracy for method isUpperCase() failed.", validator.valid('B'));
    }

    /**
     * <p>
     * Failure test case for method 'isUpperCase()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testIsUpperCase_BundleInfo_Null1() {
        try {
            CharacterValidator.isUpperCase(null);
            fail("Test failure for isUpperCase() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'isWhitespace()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testIsWhitespace_BundleInfo_Null1() {
        try {
            CharacterValidator.isWhitespace(null);
            fail("Test failure for isWhitespace() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'isWhitespace()'.<br>
     * </p>
     */
    public void testIsWhitespace_BundleInfo_Accuracy1() {
        validator = CharacterValidator.isWhitespace();
        assertTrue("Test accuracy for method isWhitespace() failed.", validator.valid(' '));

        assertFalse("Test accuracy for method isWhitespace() failed.", validator.valid("&#92;u000A"));
        assertFalse("Test accuracy for method isWhitespace() failed.", validator.valid('a'));
        assertFalse("Test accuracy for method isWhitespace() failed.", validator.valid('9'));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_char() {
        ObjectValidator validator0 = new OrValidator(CharacterValidator.isLetter(), CharacterValidator.isWhitespace());
        validator = new CharacterValidator.CharacterValidatorWrapper(validator0);
        validator.getAllMessages("#");
        validator.getAllMessages("#", 10);
        validator.valid("#");
        validator.getMessage("#");
        validator.getMessages("#");

        validator = new CharacterValidator.CharacterValidatorWrapper(validator0, bundleInfo);
        validator.getAllMessages("#");
        validator.getAllMessages("#", 10);
        validator.valid("#");
        validator.getMessage("#");
        validator.getMessages("#");
    }

    /**
     * <p>
     * Failure test case for method 'valid()'.<br> The argument is null, validation fails.
     * </p>
     */
    public void testValid_Object_Null1() {
        assertFalse("Test failure for valid() failed.", validator.valid(null));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_Object_Accuracy1() {
        assertFalse("Test failure for valid() failed.", validator.valid("99"));
        assertFalse("Test failure for valid() failed.", validator.valid("string"));
        assertFalse("Test failure for valid() failed.", validator.valid(new Object()));
        assertFalse("Test failure for valid() failed.", validator.valid(new Long(9)));

        assertTrue("Test failure for valid() failed.", validator.valid("9"));
        assertTrue("Test failure for valid() failed.", validator.valid(new Character('9')));
    }

    /**
     * <p>
     * Accuracy test case for method 'valid()'.<br>
     * </p>
     */
    public void testValid_char_Accuracy1() {
        assertFalse("Test failure for valid() failed.", validator.valid('^'));
        assertFalse("Test failure for valid() failed.", validator.valid('&'));
        assertFalse("Test failure for valid() failed.", validator.valid('-'));

        assertTrue("Test failure for valid() failed.", validator.valid("9"));
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on BooleanValidator
    }

    /**
     * Returns the BooleanValidator under test
     *
     * @return the BooleanValidator
     */
    public AbstractObjectValidator getObjectValidator() {
	return validator;
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as BooleanValidator does not provide a corresponding constructor.
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

    /**
     * <p>
     * Creates an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator() {
        return CharacterValidator.isDigit();
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @param bundleInfo name of the bundle to use
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator(BundleInfo bundleInfo) {
        return CharacterValidator.isDigit(bundleInfo);
    }

}
