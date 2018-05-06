package com.topcoder.util.commandline.failuretests;

import com.topcoder.util.commandline.* ;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;
import java.util.ArrayList;

/**
 * <p>This class test the correct behavior when 
 * providing invalid input to the <code>Switch</code>
 *
 * @author Tomson
 * @version 1.0
 */
public class SwitchTestCase extends TestCase {
    private Switch int_s;

    public void setUp() {
        try {
            int_s = new Switch(
                    "INT",
                    false,
                    0,
                    1,
                    new IntegerValidator(
                            new Integer(-10),
                            new Integer(98)));
        } catch(Exception e) {
            fail("Error occured");
        }
    }

    /**
     * <p>test the constructor of the class <code>Switch
     * </code> 
     */
    public void testConstructor1() {
        try {
            Switch tmp = new Switch(
                    "Foo",
                    false,
                    4,
                    -1,
                    null);
        } catch(Exception e) {
            fail("construct failed");
        }
        try {
            Switch tmp = new Switch(
                    "Foo" ,
                    false ,
                    4 ,
                    -2 ,
                    null);
            fail("should throw exception");
        } catch(IllegalSwitchException e) {
            // correct
        }
    }
/*
    public void testConstructor2() {
        try {
            Switch tmp = new Switch(
                    "",
                    false,
                    1,
                    2,
                    null);
            fail("should throw an exception");
        } catch(IllegalSwitchException e) {
            // correct
        }
        try {
            Switch tmp = new Switch(
                    "Foo",
                    false,
                    1,
                    2,
                    null,
                    null);
            fail("should throw an exception");
        } catch(NullPointerException e) {
            // correct
        } catch(Exception e) {
            fail("should throw a NullPointerException");
        }
    }

    public void testConstructor3() {
        try {
            Switch tmp = new Switch(
                    null,
                    false,
                    1,
                    2,
                    null);
            fail("should throw an exception");
        } catch(NullPointerException e) {
                // correct
        } catch(Exception e) {
            fail("should throw a NullPointerException");
        }
    }
*/
    public void testConstructor4() {
        try {
            Switch tmp = new Switch(
                    "23",
                    false,
                    1,
                    2,
                    null);
            fail("should throw an exception");
        } catch(IllegalSwitchException e) {
            // correct
        } catch(Exception e) {
            fail("should throw an IllegalSwitchException");
        }
    }
    /**
     * <p> test the setValue(X,X,X) method when the given
     * arguments is invalid</p>
     */
    public void testSetValue() {
        try {
            int_s.setValue("32" , 0 , false);
        } catch (Exception e) {
            fail("should not throw exception");
        }
        try {
            int_s.setValue("32L" , 0 , false);
            fail("should throw exception");
        } catch (ArgumentValidationException e) {
            assertEquals(e.getSwitchIndex() , 0);
            assertEquals(
                    e.getSwitchName() , "INT");
            assertEquals(
                    e.getArgumentValue() , "32L");
        }
        try {
            int_s.setValue("32L" , 0 , true);
            // correct
        } catch(Exception e) {
            fail("should not throw exception");
        }
    }

    /**
     * <p> test the setValues(X,X,X) method when the given
     * argument is invalid</P>
     */
    public void testSetValues() {
        try {
            int_s.setValues(null , 0 , false);
            // correct
        } catch (Exception e) {
            fail("this case should not throw exception as design doc");
        }
        List values = new ArrayList();
        values.add("-8");
        values.add("-D3");
        try {
            int_s.setValues(values , 0 , true);
        } catch(Exception e) {
            fail("should not throw exception");
        }
        try {
            int_s.setValues(values , 0 , false);
            fail("should throw an exception");
        } catch(ArgumentValidationException e) {
            // correct
        }
    }
    public static Test suite() {
        return new TestSuite(SwitchTestCase.class);
    }
}