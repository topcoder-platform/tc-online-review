/**
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.*;
import junit.framework.TestCase;

/**
 * Accuracy tests for the ComplexValidatorTestCase class.
 * The idea is to use and, or, not, and null to come up with some complex
 * ways to make them fail.
 *
 * @author snard6
 * @version 1.0
 */
public class ComplexValidatorTestCase extends TestCase {

    /**
     * Set up environment.
     */
    public void setUp() {
    }

    /**
     * Cleanup environment.
     */
    public void tearDown() {
    }

    /**
     * If it's null, or odds and between 1-100, but not numbers divisable by 5
     * 
     */
    public void testValidatorComplexInteger() {
        ObjectValidator notfivevalidator = new IntegerValidator() {
            public boolean valid(int value) {
                return value % 5 == 0;
            }
            public String getMessage(int value) {
                return null;
            }
        };
        
        assertNotNull(notfivevalidator);
        
        AndValidator andvalidator = new AndValidator();
        OrValidator orvalidator = new OrValidator();
        NotValidator notvalidator = new NotValidator(notfivevalidator);
        NullValidator nullvalidator = new NullValidator();
        
        assertNotNull(andvalidator);
        assertNotNull(orvalidator);
        assertNotNull(notvalidator);
        assertNotNull(nullvalidator);
        
        andvalidator.addValidator(IntegerValidator.inRange(1, 100));
        andvalidator.addValidator(IntegerValidator.isOdd());
        andvalidator.addValidator(notvalidator);
        
        orvalidator.addValidator(new NullValidator());
        orvalidator.addValidator(andvalidator);
        
        assertTrue(orvalidator.valid(null));
        assertTrue(orvalidator.valid("1"));
        assertTrue(orvalidator.valid("37"));
        assertTrue(orvalidator.valid("99"));
        assertFalse(orvalidator.valid("55"));
        assertFalse(orvalidator.valid("153"));
        assertFalse(orvalidator.valid("One"));
    }
    
    /**
     * If it's null, or odds and between 1-100, but not numbers divisable by 5
     * same as above but used for longs instead of ints
     */
    public void testValidatorComplexLong() {
        ObjectValidator notfivevalidator = new LongValidator() {
            public boolean valid(long value) {
                return value % 5 == 0;
            }
            public String getMessage(long value) {
                return null;
            }
        };
        
        assertNotNull(notfivevalidator);
        
        AndValidator andvalidator = new AndValidator();
        OrValidator orvalidator = new OrValidator();
        NotValidator notvalidator = new NotValidator(notfivevalidator);
        NullValidator nullvalidator = new NullValidator();
        
        assertNotNull(andvalidator);
        assertNotNull(orvalidator);
        assertNotNull(notvalidator);
        assertNotNull(nullvalidator);
        
        andvalidator.addValidator(LongValidator.inRange(1, 100));
        andvalidator.addValidator(LongValidator.isOdd());
        andvalidator.addValidator(notvalidator);
        
        orvalidator.addValidator(new NullValidator());
        orvalidator.addValidator(andvalidator);
        
        assertTrue(orvalidator.valid(null));
        assertTrue(orvalidator.valid("1"));
        assertTrue(orvalidator.valid("37"));
        assertTrue(orvalidator.valid("99"));
        assertFalse(orvalidator.valid("55"));
        assertFalse(orvalidator.valid("153"));
        assertFalse(orvalidator.valid("One"));
    }
}


