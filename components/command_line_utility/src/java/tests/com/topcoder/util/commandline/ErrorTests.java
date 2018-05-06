package com.topcoder.util.commandline;

import junit.framework.TestCase;
import com.topcoder.util.commandline.*;
import java.util.*;

/**
 * <p>Tests to make sure switches work.</p>
 * @author TCSDEVELOPER
 * @version 1.0
 */

public class ErrorTests extends TestCase {
    private Switch sw_d;
    private Switch sw_f;
    private Switch sw_roo;

    public void setUp() {
        String[] args = {"U1"};
        String[] args2 = {"U2"};
        String[] args3 = {"U3"};
        EnumValidator argsValidator = new EnumValidator(Arrays.asList(args)) ;
        EnumValidator args2Validator = new EnumValidator(Arrays.asList(args2)) ;
        EnumValidator args3Validator = new EnumValidator(Arrays.asList(args3)) ;
        
        try {
            sw_d = new Switch("D", false, 0, -1, argsValidator);
            sw_f = new Switch("F", true, 1, 2, args2Validator);
            sw_roo = new Switch("Roo", false, 3, 3, args3Validator);
        } catch (IllegalSwitchException e) {
            fail() ;
        }
    }
    
    /**
     * <p>Test getErrors().</p>
     */
    public void testErrorFunctions() {
        //Reverse the order so it won't work
        try {
            sw_d.setValue("green", 0, true);
            sw_f.setValue("yellow", 0, true);
            sw_roo.setValue("red", 0, true);
        }
        catch (Exception e) {
            //we're ignoring exceptions
            fail("Should not throw exception");
        }
        
        List dlist = sw_d.getErrors();
        List flist = sw_f.getErrors();
        List roolist = sw_roo.getErrors();
        
        ArgumentValidationException e = (ArgumentValidationException) dlist.get(0);
        assertNotNull(e);
        assertEquals("D", e.getSwitchName());
        assertEquals(0, e.getSwitchIndex());
        e = (ArgumentValidationException) flist.get(0);
        assertNotNull(e);
        assertEquals("F", e.getSwitchName());
        assertEquals(0, e.getSwitchIndex());
        e = (ArgumentValidationException) roolist.get(0);
        assertNotNull(e);
        assertEquals("Roo", e.getSwitchName());
        assertEquals(0, e.getSwitchIndex());
    }
    
    /**
     * <p>Test the IntegerValidator().</p>
     */
    public void testIntegerValidator() {
        IntegerValidator bigValidator = new IntegerValidator(new Integer(100), null);
        IntegerValidator smallValidator = new IntegerValidator(new Integer(0),
            new Integer(10));
        IntegerValidator negativeValidator = new IntegerValidator(null,
            new Integer(0));
        IntegerValidator nothing = new IntegerValidator();
        
        try {
            bigValidator.validate("101");
            bigValidator.validate("151");
            bigValidator.validate("99991");
            smallValidator.validate("2");
            smallValidator.validate("0");
            smallValidator.validate("10");
            negativeValidator.validate("0");
            negativeValidator.validate("-9991");
            negativeValidator.validate("-41");
            nothing.validate("150");
            nothing.validate("0");
            nothing.validate("-150");
        }
        catch (Exception e) {
            fail("Validation shouldn't fail");
        }
        
        try {
            bigValidator.validate("1");
            fail("Should have thrown an error");
        }
        catch (ArgumentValidationException e) {
            //success
        }
        try {
            smallValidator.validate("11");
            fail("Should have thrown an error");
        }
        catch (ArgumentValidationException e) {
            //success
        }
        try {
            negativeValidator.validate("45");
            fail("Should have thrown an error");
        }
        catch (ArgumentValidationException e) {
            //success
        }
        try {
            nothing.validate("one hundred");
            fail("Should have thrown an error");
        }
        catch (ArgumentValidationException e) {
            //success
        }
    }
}
