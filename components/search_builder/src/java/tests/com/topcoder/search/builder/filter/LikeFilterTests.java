/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import com.topcoder.search.builder.ValidationResult;

import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.StringValidator;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * The unit tests of LikeFilter Class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class LikeFilterTests extends TestCase {
    /**
     * The constant String 'value' used to test.
     */
    private static final String VALUE = "value";

    /**
     * The constant String 'name' used to test.
     */
    private static final String NAME = "name";

    /**
    * the map validators to check valid.
    */
    private Map validators = null;

    /**
     * the map alias to check valid.
     */
    private Map alias = null;

    /**
     * The Like Filter instance used to test.
     */
    private LikeFilter likeFilter = null;

    /**
     * The setUp of the Unit test,
     * construct the LikeFilter instance.
     *
     */
    protected void setUp() {
        likeFilter = new LikeFilter(NAME, LikeFilter.CONTAIN_TAGS + VALUE);
        validators = new HashMap();
        validators.put("alias name",
            StringValidator.hasLength(IntegerValidator.inRange(0, 15)));
        validators.put("real name",
            StringValidator.hasLength(IntegerValidator.inRange(0, 15)));
        alias = new HashMap();
        alias.put(NAME, "alias name");
    }

    /**
     * The accuracy Test of the constructor without escapeChar.
     *
     */
    public void testContructor_WithOutEscapeChar_success() {
        try {
            new LikeFilter(NAME, LikeFilter.CONTAIN_TAGS + VALUE);
        } catch (Exception e) {
            fail("No Exception should be thrown.");
        }
    }

    /**
     * Test the constructor without escapeChar,
     * fail for the name is null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithOutEscapeChar_FailForNameNull() {
        try {
            new LikeFilter(null, LikeFilter.CONTAIN_TAGS + VALUE);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Inheritance test.
     *
     */
    public void testInheritance() {
        assertTrue("Like filter should be implements Filter interface.", likeFilter instanceof Filter);
    }
    /**
     * Test the constructor without escapeChar,
     * fail for the name is Empty,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithOutEscapeChar_FailForNameEmpty() {
        try {
            new LikeFilter(" ", LikeFilter.CONTAIN_TAGS + VALUE);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor without escapeChar,
     * fail for the value is null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithOutEscapeChar_FailForValueNull() {
        try {
            new LikeFilter(NAME, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor without escapeChar,
     * fail for the value is invalid, does not start with 'SS:', 'SW:', 'EW:', 'WC:'.
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithOutEscapeChar_FailForValueInvalid() {
        try {
            new LikeFilter(NAME, "Invalid Value");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor without escapeChar,
     * fail for the value is empty, which means value is empty(zero length) without the prefix.
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithOutEscapeChar_FailForValueEmpty() {
        try {
            new LikeFilter(NAME, LikeFilter.CONTAIN_TAGS);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * The accuracy Test of the constructor with escapeChar.
     *
     */
    public void testContructor_WithEscapeChar_success() {
        try {
            new LikeFilter(NAME, LikeFilter.CONTAIN_TAGS + VALUE, '\\');
        } catch (Exception e) {
            fail("No Exception should be thrown.");
        }
    }

    /**
     * Test the constructor with escapeChar,
     * fail for the name is null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithEscapeChar_FailForNameNull() {
        try {
            new LikeFilter(null, LikeFilter.CONTAIN_TAGS + VALUE, '\\');
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor with escapeChar,
     * fail for the name is Empty,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithEscapeChar_FailForNameEmpty() {
        try {
            new LikeFilter(" ", LikeFilter.CONTAIN_TAGS + VALUE, '\\');
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor with escapeChar,
     * fail for the value is null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithEscapeChar_FailForValueNull() {
        try {
            new LikeFilter(NAME, null, '\\');
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor with escapeChar,
     * fail for the value is invalid, does not start with 'SS:', 'SW:', 'EW:', 'WC:'.
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithEscapeChar_FailForValueInvalid() {
        try {
            new LikeFilter(NAME, "Invalid Value", '\\');
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor with escapeChar,
     * fail for the value is empty, which means value is empty(zero length) without the prefix.
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithEscapeChar_FailForValueEmpty() {
        try {
            new LikeFilter(NAME, LikeFilter.CONTAIN_TAGS, '\\');
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the constructor with escapeChar,
     * fail for the EscapeChar is invalid,.
     * IllegalArgumentException should be thrown.
     *
     */
    public void testConstructor_WithEscapeChar_FailForEscapeCharInvalid() {
        try {
            new LikeFilter(NAME, LikeFilter.CONTAIN_TAGS, '%');
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }

        try {
            new LikeFilter(NAME, LikeFilter.CONTAIN_TAGS, '*');
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }

        try {
            new LikeFilter(NAME, LikeFilter.CONTAIN_TAGS, '_');
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method isValid,
     * fail for the validators is null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testisValid_failForvalidatorsNull() {
        try {
            likeFilter.isValid(null, alias);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method isValid,
     * fail for the validators is invalid, contains non_String key,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testisValid_failForvalidatorsWithNonStringkey() {
        try {
            validators.put(new Object(), IntegerValidator.inRange(0, 100));
            likeFilter.isValid(validators, alias);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method isValid,
     * fail for the validators is invalid, contains non_ObjectValidator Value,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testisValid_failForvalidatorsWithNonObjectValidatorValue() {
        try {
            validators.put("invalid", new Object());
            likeFilter.isValid(validators, alias);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method isValid,
     * fail for the alias is null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testisValid_failForaliasNull() {
        try {
            likeFilter.isValid(validators, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method isValid,
     * fail for the alias is invalid, contains non_String key,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testisValid_failForaliasWithNonStringkey() {
        try {
            alias.put(new Object(), "alias");
            likeFilter.isValid(validators, alias);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method isValid,
     * fail for the alias is invalid, contains non_String Value,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testisValid_failForaliasWithNonStringValue() {
        try {
            alias.put("invalid", new Object());
            likeFilter.isValid(validators, alias);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method isValid,
     * fail for the no rule can be fould for the value,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testisValid_failForNoRuleCanBeFound() {
        try {
            likeFilter = new LikeFilter("wrong", LikeFilter.CONTAIN_TAGS + VALUE);
            likeFilter.isValid(validators, alias);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }

    /**
     * Test the method isValid,
     * validate failed for the String is too long.
     *
     */
    public void testisValid_validateFailed() {
        likeFilter = new LikeFilter(NAME,
                LikeFilter.CONTAIN_TAGS + "The value is too long.");

        ValidationResult result = likeFilter.isValid(validators, alias);

        //the validation should not be successful
        assertFalse("The validation should be failed.", result.isValid());
    }

    /**
     * Test the method isValid,
     * validate successed, the rule is retrieved by aliasMap.
     *
     */
    public void testisValid_validateSuccessViaAliasMap() {
        ValidationResult result = likeFilter.isValid(validators, alias);

        //the validation should be successful
        assertTrue("The validation should be all right.", result.isValid());
    }

    /**
     * Test the method isValid,
     * validate successed, the rule is not retrieved by aliasMap.
     *
     */
    public void testisValid_validateSuccessNotViaAliasMap() {
        likeFilter = new LikeFilter("real name",
                LikeFilter.END_WITH_TAG + "value");

        ValidationResult result = likeFilter.isValid(validators, alias);

        //the validation should be successful
        assertTrue("The validation should be all right.", result.isValid());
    }

    /**
     * The accuracy test of the method getFilterType.
     *
     */
    public void testgetFilterType_accuracy() {
        assertTrue("The type should be Filter.LIKE_FILTER.",
            Filter.LIKE_FILTER == likeFilter.getFilterType());
    }

    /**
     * The accuracy test of the method getEscapeCharacter.
     *
     */
    public void testgetEscapeCharacter() {
        //test the default EscapeCharacter
        assertTrue("The default EscapeCharacter should be '\\'.",
            '\\' == likeFilter.getEscapeCharacter());
        likeFilter = new LikeFilter(NAME, LikeFilter.CONTAIN_TAGS + VALUE, 'a');

        //test the EscapeCharacter set in the constructor
        assertTrue("The default EscapeCharacter should be 'a'.",
            'a' == likeFilter.getEscapeCharacter());
    }

    /**
     * The accuracy test of the method getName.
     *
     */
    public void testgetName() {
        assertEquals("The name should be same as set in constructor.", NAME,
            likeFilter.getName());
    }

    /**
     * The accuracy test of the method getValue.
     *
     */
    public void testgetValue() {
        assertEquals("The Value should be same as set in constructor.",
            LikeFilter.CONTAIN_TAGS + VALUE, likeFilter.getValue());
    }

    /**
     * The accuracy test of the clone method.
     *
     */
    public void testClone() {
        Object cloneObject = likeFilter.clone();
        assertTrue("The Object should be instance of LikeFilter.",
            cloneObject instanceof LikeFilter);

        LikeFilter another = (LikeFilter) cloneObject;

        //value should be same
        assertEquals("The filter value should be the same as the clone.",
            likeFilter.getValue(), another.getValue());

        //name should be same
        assertEquals("The filter name should be the same as the clone.",
            likeFilter.getName(), another.getName());

        //type should be same
        assertEquals("The filter type should be the same as the clone.",
            likeFilter.getFilterType(), another.getFilterType());

        //The escape char be same
        assertEquals("The filter escape char should be the same as the clone.",
            likeFilter.getEscapeCharacter(), another.getEscapeCharacter());
    }
}
