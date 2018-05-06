/*
 * @(#) ConfigureMatrixTestCase.java
 * 
 * 1.0  01/03/2003
 */

package com.topcoder.util.weightedcalculator.functionaltests;

import junit.framework.*;
import com.topcoder.util.weightedcalculator.*;

/**
 * Tests proper configuration of MathMatrix and correct instantiations
 * of Section and LineItem objects
 * 
 * @author isv
 * @version 1.0
 */
public class ConfigureMatrixTestCase extends TestCase {

    public ConfigureMatrixTestCase(String testName) {
        super(testName);
    }

    public void test() {

        MathMatrix matrix = new MathMatrix("Test matrix", 100);
        LineItem item;
        Section section;

        // testing LineItem

        item = new LineItem("Line item 1", 0.5, 15) ;
        assertNotNull("Properly defined item should be created", item);

        item = new LineItem("Line item 1", 0.5, 15, 10) ;
        assertNotNull("Properly defined item should be created", item);

        // testing for illegal arguments

        try {
            item = new LineItem("Illegal item", -1, 15);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 0, 15);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 1.5, 15);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", -1, 15);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 0, 15);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 1.5, 15);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, -1, 15);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 0, 15);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 1.5, 15);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, -1, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 0, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 1.5, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("", -1, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("", 0, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("", 1.5, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}


        try {
            item = new LineItem("Illegal item", -1, 15, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 0, 15, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 1.5, 15, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", -1, 15, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 0, 15, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 1.5, 15, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, -1, 15, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 0, 15, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 1.5, 15, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, -1, -1, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 0, -1, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 1.5, -1, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", -1, -1, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 0, -1, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 1.5, -1, -1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}


        try {
            item = new LineItem("Illegal item", -1, 15, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 0, 15, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 1.5, 15, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", -1, 15, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 0, 15, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("Illegal item", 1.5, 15, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, -1, 15, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 0, 15, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 1.5, 15, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, -1, -1, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 0, -1, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem((String) null, 1.5, -1, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("", -1, -1, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("", 0, -1, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item = new LineItem("", 1.5, -1, 21);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        item = new LineItem("Testing item", 0.5, 4);

        try {
            item.setWeight(1.1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item.setWeight(0);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item.setWeight(-1.1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        item.setActualScore(4);

        try {
            item.setMaximumScore(0);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item.setMaximumScore(3);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item.setMaximumScore(-1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        item.setMaximumScore(5);

        try {
            item.setActualScore(-1);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            item.setActualScore(6);
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}



        matrix.addItem(item);
        int index =matrix.indexOf(item);
        assertNotNull(matrix.getItem(index));
        assertTrue((LineItem) matrix.getItem(index) == item);
    
        section = new Section("Testing section", 0.5);
        matrix.addItem(section);
        assertNotNull(matrix.getItem(1));

        section.addItem(new LineItem("5", 1.0, 4.00));
        assertNotNull(section.getItem(0));

        try {
            section.addItem(new LineItem("5", 1.0, 4.00));
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}

        try {
            matrix.addItem(new LineItem("1", 0.5, 4.00));
            fail("IllegalArgumentException should be thrown");
        } catch(IllegalArgumentException e) {}


        // testing NullPointerException
        try {
            section.addItem((LineItem) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            section.addItem((Section) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            matrix.addItem((LineItem) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            matrix.addItem((Section) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}


        // test remove method
        matrix = new MathMatrix("Test Matrix");
        section = new Section("Section", 0.5);
        item = new LineItem("Section", 0.5, 4, 4);


        matrix.addItem(section);
        matrix.addItem(item);

        try {
            matrix.removeItem(-1);
            fail("IndexOutOfBoundsException should be thrown");
        } catch(IndexOutOfBoundsException e) {}

        try {
            matrix.removeItem(100);
            fail("IndexOutOfBoundsException should be thrown");
        } catch(IndexOutOfBoundsException e) {}

        section = (Section) matrix.removeItem(0);
        assertTrue(matrix.indexOf(section) == -1);

        item = (LineItem) matrix.removeItem(0);
        assertTrue(matrix.indexOf(item) == -1);
    }

    public static Test suite() {
        return new junit.framework.TestSuite(ConfigureMatrixTestCase.class);
    }
}
