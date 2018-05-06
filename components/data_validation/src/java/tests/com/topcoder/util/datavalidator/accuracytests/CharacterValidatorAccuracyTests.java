/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.CharacterValidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * Accuracy tests for the CharacterValidator class.
 *
 * @author KLW
 * @version 1.1
 */
public class CharacterValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * The ByteValidator instance for testing.
     * </p>
     */
    private CharacterValidator characterValidator;

    /**
     * <p>
     * BundleInfo case used to test.
     * </p>
     */
    private BundleInfo bundleInfo;

    /**
     * <p>
     * The key for the bundle.
     * </p>
     */
    private String key;

    /**
     * <p>
     * setUp().
     * </p>
     */
    protected void setUp() {
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle("accuracy.test", new Locale("en"));
        bundleInfo.setDefaultMessage("accuracy test");
    }

    /**
     * <p>
     * tear down the test environment.
     * </p>
     */
    protected void tearDown() {
        bundleInfo = null;
        key = null;
        characterValidator = null;
    }

    /**
     * <p>
     * accuracy test method valid().
     * </p>
     */
    public void testValid() {
        characterValidator = CharacterValidator.isDigit();
        assertTrue(characterValidator.valid(new Character('5')));
        assertFalse(characterValidator.valid(new Character('d')));
        assertTrue(characterValidator.valid("5"));
        assertFalse(characterValidator.valid("d"));
        assertFalse(characterValidator.valid("5678"));
        assertFalse(characterValidator.valid(new Object()));
        assertFalse(characterValidator.valid(characterValidator));
    }

    /**
     * <p>
     * test method isDigit().
     * </p>
     */
    public void testIsDigit() {
        characterValidator = CharacterValidator.isDigit();
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid('5'));
        assertFalse(characterValidator.valid('d'));
        assertTrue(characterValidator.valid(new Character('5')));
        assertFalse(characterValidator.valid(new Character('d')));
        assertTrue(characterValidator.valid("5"));
        assertFalse(characterValidator.valid("d"));
        assertFalse(characterValidator.valid("5678"));
        assertFalse(characterValidator.valid(new Object()));
        assertFalse(characterValidator.valid(characterValidator));
        assertNull(characterValidator.getMessage('5'));
        assertNotNull(characterValidator.getMessage('d'));
    }

    /**
     * <p>
     * test method isLetter().
     * </p>
     */
    public void testIsLetter() {
        characterValidator = CharacterValidator.isLetter();
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid('d'));
        assertFalse(characterValidator.valid('5'));
        assertTrue(characterValidator.valid(new Character('d')));
        assertFalse(characterValidator.valid(new Character('5')));
        assertTrue(characterValidator.valid("d"));
        assertFalse(characterValidator.valid("5"));
        assertFalse(characterValidator.valid("5678"));
        assertFalse(characterValidator.valid(new Object()));
        assertFalse(characterValidator.valid(characterValidator));
        assertNull(characterValidator.getMessage('d'));
        assertNotNull(characterValidator.getMessage('5'));
    }

    /**
     * <p>
     * test method isLetterOrDigit().
     * </p>
     */
    public void testIsLetterOrDigit() {
        characterValidator = CharacterValidator.isLetterOrDigit();
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid('5'));
        assertTrue(characterValidator.valid('d'));
        assertFalse(characterValidator.valid('*'));
        assertTrue(characterValidator.valid(new Character('d')));
        assertTrue(characterValidator.valid(new Character('5')));
        assertFalse(characterValidator.valid(new Character('*')));
        assertTrue(characterValidator.valid("5"));
        assertTrue(characterValidator.valid("d"));
        assertFalse(characterValidator.valid("*"));
        assertFalse(characterValidator.valid("5678"));
        assertFalse(characterValidator.valid(new Object()));
        assertFalse(characterValidator.valid(characterValidator));
        assertNull(characterValidator.getMessage('d'));
        assertNull(characterValidator.getMessage('5'));
        assertNotNull(characterValidator.getMessage('*'));
    }

    /**
     * <p>
     * test method isUpperCase().
     * </p>
     */
    public void testIsUpperCase() {
        characterValidator = CharacterValidator.isUpperCase();
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid('A'));
        assertFalse(characterValidator.valid('a'));
        assertNull(characterValidator.getMessage('A'));
        assertNotNull(characterValidator.getMessage('a'));
    }

    /**
     * <p>
     * test method isLowerCase().
     * </p>
     */
    public void testIsLowerCase() {
        characterValidator = CharacterValidator.isLowerCase();
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid('a'));
        assertFalse(characterValidator.valid('A'));
        assertNull(characterValidator.getMessage('a'));
        assertNotNull(characterValidator.getMessage('A'));
    }

    /**
     * <p>
     * test method isWhitespace().
     * </p>
     */
    public void testIsWhitespace() {
        characterValidator = CharacterValidator.isWhitespace();
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid(' '));
        assertTrue(characterValidator.valid('\t'));
        assertTrue(characterValidator.valid('\r'));
        assertFalse(characterValidator.valid('A'));
        assertNull(characterValidator.getMessage(' '));
        assertNull(characterValidator.getMessage('\t'));
        assertNull(characterValidator.getMessage('\r'));
        assertNotNull(characterValidator.getMessage('A'));
    }

    /**
     * <p>
     * test method isDigit(bundleInfo).
     * </p>
     */
    public void testIsDigit_Bundle() {
        key = "IsDigit_Bundle_Key";
        bundleInfo.setMessageKey(key);
        characterValidator = CharacterValidator.isDigit(bundleInfo);
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid('5'));
        assertFalse(characterValidator.valid('d'));
        assertTrue(characterValidator.valid(new Character('5')));
        assertFalse(characterValidator.valid(new Character('d')));
        assertTrue(characterValidator.valid("5"));
        assertFalse(characterValidator.valid("d"));
        assertFalse(characterValidator.valid("5678"));
        assertFalse(characterValidator.valid(new Object()));
        assertFalse(characterValidator.valid(characterValidator));
        assertNull(characterValidator.getMessage('5'));
        assertEquals("The massage should be equal.", key, characterValidator.getMessage('d'));
    }

    /**
     * <p>
     * test method isLetter(bundleInfo).
     * </p>
     */
    public void testIsLetter_Bundle() {
        key = "IsLetter_Bundle_Key";
        bundleInfo.setMessageKey(key);
        characterValidator = CharacterValidator.isLetter(bundleInfo);
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid('d'));
        assertFalse(characterValidator.valid('5'));
        assertTrue(characterValidator.valid(new Character('d')));
        assertFalse(characterValidator.valid(new Character('5')));
        assertTrue(characterValidator.valid("d"));
        assertFalse(characterValidator.valid("5"));
        assertFalse(characterValidator.valid("5678"));
        assertFalse(characterValidator.valid(new Object()));
        assertFalse(characterValidator.valid(characterValidator));
        assertNull(characterValidator.getMessage('d'));
        assertEquals("The massage should be equal.", key, characterValidator.getMessage('5'));
    }

    /**
     * <p>
     * test method isLetterOrDigit(bundleInfo).
     * </p>
     */
    public void testIsLetterOrDigit_Bundle() {
        key = "IsLetterOrDigit_Bundle_Key";
        bundleInfo.setMessageKey(key);
        characterValidator = CharacterValidator.isLetterOrDigit(bundleInfo);
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid('5'));
        assertTrue(characterValidator.valid('d'));
        assertFalse(characterValidator.valid('*'));
        assertTrue(characterValidator.valid(new Character('d')));
        assertTrue(characterValidator.valid(new Character('5')));
        assertFalse(characterValidator.valid(new Character('*')));
        assertTrue(characterValidator.valid("5"));
        assertTrue(characterValidator.valid("d"));
        assertFalse(characterValidator.valid("*"));
        assertFalse(characterValidator.valid("5678"));
        assertFalse(characterValidator.valid(new Object()));
        assertFalse(characterValidator.valid(characterValidator));
        assertNull(characterValidator.getMessage('d'));
        assertNull(characterValidator.getMessage('5'));
        assertNotNull(characterValidator.getMessage('*'));
        //assertEquals("The massage should be equal.", key, characterValidator.getMessage('*'));
    }

    /**
     * <p>
     * test method isUpperCase(bundleInfo).
     * </p>
     */
    public void testIsUpperCase_Bundle() {
        key = "IsUpperCase_Bundle_Key";
        bundleInfo.setMessageKey(key);
        characterValidator = CharacterValidator.isUpperCase(this.bundleInfo);
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid('A'));
        assertFalse(characterValidator.valid('a'));
        assertNull(characterValidator.getMessage('A'));
        assertEquals("The massage should be equal.", key, characterValidator.getMessage('a'));
    }

    /**
     * <p>
     * test method isLowerCase(bundleInfo).
     * </p>
     */
    public void testIsLowerCase_Bundle() {
        key = "IsLowerCase_Bundle_Key";
        bundleInfo.setMessageKey(key);
        characterValidator = CharacterValidator.isLowerCase(this.bundleInfo);
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid('a'));
        assertFalse(characterValidator.valid('A'));
        assertNull(characterValidator.getMessage('a'));
        assertNotNull(characterValidator.getMessage('A'));
        assertEquals("The massage should be equal.", key, characterValidator.getMessage('A'));
    }

    /**
     * <p>
     * test method isWhitespace(bundleInfo).
     * </p>
     */
    public void testIsWhitespace_Bundle() {
        key = "IsWhitespace_Bundle_Key";
        bundleInfo.setMessageKey(key);
        characterValidator = CharacterValidator.isWhitespace(bundleInfo);
        assertNotNull("The instance should not be null.", characterValidator);
        assertTrue(characterValidator.valid(' '));
        assertTrue(characterValidator.valid('\t'));
        assertTrue(characterValidator.valid('\r'));
        assertFalse(characterValidator.valid('A'));
        assertNull(characterValidator.getMessage(' '));
        assertNull(characterValidator.getMessage('\t'));
        assertNull(characterValidator.getMessage('\r'));
        assertNotNull(characterValidator.getMessage('A'));
        assertEquals("The massage should be equal.", key, characterValidator.getMessage('A'));
    }
}
