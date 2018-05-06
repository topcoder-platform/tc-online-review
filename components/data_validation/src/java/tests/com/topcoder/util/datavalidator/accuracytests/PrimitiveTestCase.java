/**
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.*;
import junit.framework.TestCase;

/**
 * Accuracy tests for the PrimitiveTestCase class.
 * Tests for all primitive data types such as int, long, short...
 *
 * @author snard6
 * @version 1.0
 */
public class PrimitiveTestCase extends TestCase {

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
     * Tests simple integer types
     * 
     */
    public void testSimpleIntegers() {
        IntegerValidator v = new IntegerValidator() {
            public boolean valid(int value) {
                return true;
            }
            public String getMessage(int value) {
                return null;
            }
        };
        assertNotNull(v);
         
        assertTrue(v.valid(new Long(1000000000000000000L)));
        assertTrue(v.valid(new Float(100000000.33f)));
        assertTrue(v.valid(new Double(100000000.33d)));
        assertFalse(v.valid("43434656.77878778"));
        assertNotNull(v.getMessage("43434656.77878778"));
        assertTrue(v.getMessage("43434656.77878778").length() >= 1);
        assertFalse(v.valid(Boolean.TRUE));
        assertNotNull(v.getMessage(Boolean.TRUE));
        assertTrue(v.getMessage(Boolean.TRUE).length() >= 1);
    }
    
    /**
     * Tests simple double types
     * 
     */
    public void testSimpleDoubles() {
        DoubleValidator v = new DoubleValidator() {
            public boolean valid(double value) {
                return true;
            }
            public String getMessage(double value) {
                return null;
            }
        };
        assertNotNull(v);
        
        assertTrue(v.valid(new Long(1000000000000000000L)));
        assertTrue(v.valid(new Float(100000000.33f)));
        assertTrue(v.valid(new Integer(100000000)));
        assertFalse(v.valid("One"));
        assertNotNull(v.getMessage("One"));
        assertTrue(v.getMessage("One").length() >= 1);
    }
    
    /**
     * Tests simple long types
     * 
     */
    public void testSimpleLongs() {
        LongValidator v = new LongValidator() {
            public boolean valid(long value) {
                return true;
            }
            public String getMessage(long value) {
                return null;
            }
        };
        assertNotNull(v);
        
        assertTrue(v.valid(new Double(100000000.33d)));
        assertTrue(v.valid(new Float(100000000.33f)));
        assertTrue(v.valid(new Integer(100000000)));
        assertFalse(v.valid("43434656.77878778"));
        assertNotNull(v.getMessage("43434656.77878778"));
        assertTrue(v.getMessage("43434656.77878778").length() >= 1);
        assertFalse(v.valid(Boolean.TRUE));
        assertNotNull(v.getMessage(Boolean.TRUE));
        assertTrue(v.getMessage(Boolean.TRUE).length() >= 1);
    }
    
    /**
     * Tests simple short types
     */
    public void testSimpleShorts() {
        LongValidator v = new LongValidator() {
            public boolean valid(long value) {
                return true;
            }
            public String getMessage(long value) {
                return null;
            }
        };
        assertNotNull(v);
        
        assertTrue(v.valid(new Double(100000000.33d)));
        assertTrue(v.valid(new Float(100000000.33f)));
        assertTrue(v.valid(new Integer(100000000)));
        assertFalse(v.valid("43434656.77878778"));
        assertNotNull(v.getMessage("43434656.77878778"));
        assertTrue(v.getMessage("43434656.77878778").length() >= 1);
        assertFalse(v.valid(Boolean.TRUE));
        assertNotNull(v.getMessage(Boolean.TRUE));
        assertTrue(v.getMessage(Boolean.TRUE).length() >= 1);
    }
    
    /**
     * Tests simple boolean types
     */
    public void testSimpleBoolean() {
        BooleanValidator v = new BooleanValidator() {
            public boolean valid(boolean value) {
                return true;
            }
            public String getMessage(boolean value) {
                return null;
            }
        };
        assertNotNull(v);
        
        assertTrue(v.valid("true"));
        assertTrue(v.valid("false"));
        assertTrue(v.valid(Boolean.TRUE));
        assertTrue(v.valid(Boolean.FALSE));
        
        assertFalse(v.valid(new Double(100000000.33d)));
        assertNotNull(v.getMessage(new Double(100000000.33d)));
        assertTrue(v.getMessage(new Double(100000000.33d)).length() >= 1);
        assertFalse(v.valid(new Float(100000000.33f)));
        assertNotNull(v.getMessage(new Float(100000000.33f)));
        assertTrue(v.getMessage(new Float(100000000.33f)).length() >= 1);
        assertFalse(v.valid(new Integer(100000000)));
        assertNotNull(v.getMessage(new Integer(100000000)));
        assertTrue(v.getMessage(new Integer(100000000)).length() >= 1);
    }
    
    /**
     * Tests simple character types
     */
    public void testSimpleCharacter() {
        CharacterValidator v = new CharacterValidator() {
            public boolean valid(char value) {
                return true;
            }
            public String getMessage(char value) {
                return null;
            }
        };
        
        assertNotNull(v);
        
        assertTrue(v.valid("t"));
        assertTrue(v.valid("f"));
        assertTrue(v.valid('a'));
        
        assertFalse(v.valid(new Double(100000000.33d)));
        assertNotNull(v.getMessage(new Double(100000000.33d)));
        assertTrue(v.getMessage(new Double(100000000.33d)).length() >= 1);
        assertFalse(v.valid(new Float(100000000.33f)));
        assertNotNull(v.getMessage(new Float(100000000.33f)));
        assertTrue(v.getMessage(new Float(100000000.33f)).length() >= 1);
        assertFalse(v.valid(new Integer(100000000)));
        assertNotNull(v.getMessage(new Integer(100000000)));
        assertTrue(v.getMessage(new Integer(100000000)).length() >= 1);
        assertFalse(v.valid("Lengthy"));
        assertNotNull(v.getMessage("Lengthy"));
        assertTrue(v.getMessage("Lengthy").length() >= 1);
    }
    
    /**
     * Tests simple string types
     */
    public void testSimpleString() {
        StringValidator v = new StringValidator() {
            public boolean valid(String value) {
                return true;
            }
            public String getMessage(String value) {
                return null;
            }
        };
        
        assertNotNull(v);
        
        assertTrue(v.valid("String"));
        assertTrue(v.valid("Topcoder"));
        assertTrue(v.valid("1000"));
        
        assertFalse(v.valid(new Double(100000000.33d)));
        assertNotNull(v.getMessage(new Double(100000000.33d)));
        assertTrue(v.getMessage(new Double(100000000.33d)).length() >= 1);
        assertFalse(v.valid(new Float(100000000.33f)));
        assertNotNull(v.getMessage(new Float(100000000.33f)));
        assertTrue(v.getMessage(new Float(100000000.33f)).length() >= 1);
        assertFalse(v.valid(new Integer(100000000)));
        assertNotNull(v.getMessage(new Integer(100000000)));
        assertTrue(v.getMessage(new Integer(100000000)).length() >= 1);
        assertFalse(v.valid(new Character('L')));
        assertNotNull(v.getMessage(new Character('L')));
        assertTrue(v.getMessage(new Character('L')).length() >= 1);
    }
    
    /**
     * Tests simple byte types
     */
    public void testSimpleByte() {
        ByteValidator v = new ByteValidator() {
            public boolean valid(byte value) {
                return true;
            }
            public String getMessage(byte value) {
                return null;
            }
        };
        
        assertNotNull(v);
        
        assertTrue(v.valid(new Byte("12")));
        assertTrue(v.valid(new Byte("127")));
        assertTrue(v.valid(new Byte("-35")));
        
        assertFalse(v.valid("Byte"));
        assertNotNull(v.getMessage("Byte"));
        assertTrue(v.getMessage("Byte").length() >= 1);
    }
    
    /**
     * Tests simple float types
     */
    public void testSimpleFloat() {
        FloatValidator v = new FloatValidator() {
            public boolean valid(float value) {
                return true;
            }
            public String getMessage(float value) {
                return null;
            }
        };
        
        assertNotNull(v);
        
        assertTrue(v.valid(new Long(1000000000000000000L)));
        assertTrue(v.valid(new Float(100000000.33f)));
        assertTrue(v.valid(new Double(100000000.33d)));
        assertFalse(v.valid("One"));
        assertNotNull(v.getMessage("One"));
        assertTrue(v.getMessage("One").length() >= 1);
    }
}


