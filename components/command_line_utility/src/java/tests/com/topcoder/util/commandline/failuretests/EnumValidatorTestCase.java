package com.topcoder.util.commandline.failuretests;

import com.topcoder.util.commandline.* ;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This class test the correct behavior when 
 * providing invalid input to the <code>EnumValidator
 * </code>
 *
 * @author Tomson
 * @version 1.0
 */
public class EnumValidatorTestCase extends TestCase {
    private EnumValidator validator1;
    private EnumValidator validator2;
    
    public void setUp() {
        String str[] = {"-D" , "-D a , b" , "-F c, d", "-Foo=dir,k"};
        Integer _int[] = { 
                new Integer(1),
                new Integer(2),
                new Integer(3)};        
        validator1 = new EnumValidator(Arrays.asList(str));
        validator2 = new EnumValidator(Arrays.asList(_int));        
    }	
    
    /** 
     * <p> test validate(X) method when the given String is 
     * not an element of the Collection</p>
     */
    public void testNotInCollection() {
        assertNotNull(
            "construct failed",
            validator1);
        try {
    	    // test the legal values
            validator1.validate("-F c, d");
            validator1.validate("-D a , b");
            validator1.validate("-Foo=dir,k");
        } catch(Exception e) {
            fail("should not throw exception");
        }
        // test the illegal values
        try {
            validator1.validate("-F c,d");
            fail("should throw an exception");
        } catch(ArgumentValidationException e) {
            // correct
        }
        try {
            validator1.validate("-Foo=dir ,k");
            fail("should throw an exception");
        } catch(ArgumentValidationException e) {
            // correct
        }
    }
    
    /**
     * <p>test validate(X) method when the specified collection
     * is not a collection of Strings</p>
     */
    public void testValidator2() {
    	assertNotNull(
    	    "construct failed",
    	    validator2);
        try {
            validator2.validate("Dir");
            fail("should throw an exception");
        } catch(ArgumentValidationException e) {
            // correct
        }
        try {
            validator2.validate("1");
            fail("should throw an exception");
        } catch(ArgumentValidationException e) {
            // correct
        }        	
    } 
    public static Test suite() {
        return new TestSuite(EnumValidatorTestCase.class);
    }
}