/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.topcoder.search.builder.failuretests;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.search.builder.filter.LikeFilter;
import com.topcoder.util.datavalidator.IntegerValidator;
import junit.framework.TestCase;


/**
 * Failure Tests for <code>LikeFilter</code> class.
 * 
 * @author qiucx0161
 * @version 1.2
 */
public class LikeFilterTest12 extends TestCase {

    /**
     * The LikeFilter instance used for test.
     */
    private LikeFilter filter = null;
    
    /**
     * the map instance used for test.
     */
     private Map validators = null;

     /**
      * the map instance used for test.
      */
     private Map alias = null;
    
    /**
     * Setup the test envrionment.
     */
    protected void setUp() throws Exception {
        super.setUp();
        filter = new LikeFilter("123", "SS:123");
        
        validators = new HashMap();
        validators.put("1", IntegerValidator.lessThan(8));
        validators.put("2", IntegerValidator.lessThan(9));
        alias = new HashMap();
        alias.put("name", "123");
    }

    /**
     * Setup the test envrionment.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        filter = null;
    }

    /**
     * Test for LikeFilter(String, String) with null name,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter1NullName() {
        try {
            new LikeFilter(null, "SS:value");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for LikeFilter(String, String) with empty name,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter1EmptyName() {
        try {
            new LikeFilter(" ", "SS:value");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for LikeFilter(String, String) with null value,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter1NullValue() {
        try {
            new LikeFilter("name", "SS:value");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for LikeFilter(String, String) with empty name,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter1EmptyValue() {
        try {
            new LikeFilter("name", " ");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for LikeFilter(String, String) with invalid value,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter1InvalidValue1() {
        try {
            new LikeFilter("name", "value");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for LikeFilter(String, String) with invalid value,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter1InvalidValue2() {
        try {
            new LikeFilter("name", "SS:");
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
        
    }

    /**
     * Test for LikeFilter(String name, String value, char escapeCharacter) with null name,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter2NullName() {
        try {
            new LikeFilter(null, "SS:value", '\\');
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for LikeFilter(String name, String value, char escapeCharacter) with empty name,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter2EmptyName() {
        try {
            new LikeFilter(" ", "SS:value", '\\');
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for LikeFilter(String name, String value, char escapeCharacter) with null value,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter2NullValue() {
        try {
            new LikeFilter("name", "SS:value", '\\');
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for LikeFilter(String name, String value, char escapeCharacter) with empty name,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter2EmptyValue() {
        try {
            new LikeFilter("name", " ", '\\');
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for LikeFilter(String name, String value, char escapeCharacter) with invalid value,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter2InvalidValue1() {
        try {
            new LikeFilter("name", "value", '\\');
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for LikeFilter(String name, String value, char escapeCharacter) with invalid value,
     * IllegalArgumentException should be thrown.
     */
    public void testLikeFilter2InvalidValue2() {
        try {
            new LikeFilter("name", "SS:", '\\');
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }

    /**
     * Test for isValid(Map validators, Map alias) method with null validators.
     * IllegalArgumentException should be thrown.
     */
    public void testIsValidNullValidators() {
        try {
            filter.isValid(null, alias);
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for isValid(Map validators, Map alias) method with invalid key in validators.
     * IllegalArgumentException should be thrown.
     */
    public void testIsValidInvalidKeyInValidators() {
        validators.put(new Object(), IntegerValidator.lessThan(7));
        try {
            filter.isValid(validators, alias);
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for isValid(Map validators, Map alias) method with null validators.
     * IllegalArgumentException should be thrown.
     */
    public void testIsValidNullAlias() {
        try {
            filter.isValid(validators, null);
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for isValid(Map validators, Map alias) method with invalid key in validators.
     * IllegalArgumentException should be thrown.
     */
    public void testIsValidInvalidKeyInAlias() {
        alias.put(new Object(), "SS:value");
        try {
            filter.isValid(validators, alias);
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
    
    /**
     * Test for isValid(Map validators, Map alias) method with invalid value in validators.
     * IllegalArgumentException should be thrown.
     */
    public void testIsValidInvalidValueInAlias() {
        alias.put("name2", new Object());
        try {
            filter.isValid(validators, alias);
        } catch (IllegalArgumentException iae) {
            // ignore.
        }
    }
}
