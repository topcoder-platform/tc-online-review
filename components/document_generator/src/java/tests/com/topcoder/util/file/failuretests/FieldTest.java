/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.file.fieldconfig.Field;

/**
 * @author AdamSelene This class tests Field for proper failure.
 */
public class FieldTest extends TestCase {

    /**
     * Creates a attachment point for this testcase.
     *
     * @return a wonderful testsuite for this case.
     */
    public static Test suite() {
        return new TestSuite(FieldTest.class);
    }

    /**
     * Tests Field constructor for null arg failure.
     */
    public void testConstructor() {
        try {
            new Field(null, null, null, true);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests Field constructor for empty arg failure.
     */
    public void testConstructorES() {
        try {
            new Field("", null, null, true);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests setValue for empty arg failure.
     */
    public void testSetValueNull() {
        Field f = new Field("test", null, null, false);
        try {
            f.setValue(null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests setValue for empty arg failure.
     */
    // public void testSetValueES()
    // {
    // Field f= new Field("test", null,null, false);
    // try {
    // f.setValue("");
    // fail("Did not fail.");
    // }
    // catch (IllegalArgumentException e) { /* expected */ }
    // catch (Exception e) { fail("Wrong exception. " + e.toString()); }
    // }
    /**
     * Tests setValue for readonly failure.
     */
    public void testSetValueRO() {
        Field f = new Field("test", null, null, true);
        try {
            f.setValue("duh");
            fail("Did not fail.");
        } catch (IllegalStateException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

}