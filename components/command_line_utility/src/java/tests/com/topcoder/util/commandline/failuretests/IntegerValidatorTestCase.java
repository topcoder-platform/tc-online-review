package com.topcoder.util.commandline.failuretests;

import com.topcoder.util.commandline.* ;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This class test the correct behavior when 
 * providing invalid input to the <code>IntegerValidator
 * </code>
 *
 * @author Tomson
 * @version 1.0
 */
public class IntegerValidatorTestCase extends TestCase {
    /** no range check validator */
    private IntegerValidator validator1;
    /** range check validator */
    private IntegerValidator validator2;
        
    public void setUp() {
        validator1 = new IntegerValidator();
        validator2 = new IntegerValidator(
                new Integer(-10) , 
                new Integer(98));
    }
    
    /** 
     * <p>test the validate(X) method when the given String
     * reprenting a Integer is out of range</p>
     */
    public void testOutOfRange() {
        assertNotNull(
            "construct failed",
            validator2);
        try {
            validator2.validate("-5");
        } catch(Exception e) {
            fail("should not throw exception");
        }
        try {
            validator2.validate("-90");
            fail("should throw an exception");
        } catch(ArgumentValidationException e) {
            // correct
        }
        try {
            validator2.validate("100");
            fail("should throw an exception");
        } catch(ArgumentValidationException e) {
            // correct
        }
    }
    
    /**
     * <p>test the validate(X) method when the given String 
     * is not a Integer String representation</p>
     */
    public void testNotInteger() {
    	assertNotNull(
    	    "construct failed",
    	    validator1);
    	try {
    	    validator1.validate("10L");
    	    fail("should throw an exception");
    	} catch(ArgumentValidationException e) {
    	    // correct
    	} catch(Exception e) {
    	    fail("should shrow an ArgumentValidationException");
    	}
    }
    
    /**
     * <p>test the validate(X) method when the given Integer 
     * String representation is greater than the 
     * Integer.MAX_VALUE</p>
     */
    public void testTooGreate() {
        try {
            validator1.validate("2200000000");
            fail("should throw an exception");
        } catch(ArgumentValidationException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an ArgumentValidationException");
        }
    }
    public static Test suite() {
        return new TestSuite(IntegerValidatorTestCase.class);
    }
}