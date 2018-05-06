/*
 * @(#) ExploreMatrixTestCase.java
 *
 * 1.0  01/03/2003
 */

package com.topcoder.util.weightedcalculator.functionaltests;

import junit.framework.*;
import java.util.*;
import com.topcoder.util.weightedcalculator.*;

/**
 *
 * @author isv
 * @version 1.0
 */
public class ExploreMatrixTestCase extends TestCase {

    public ExploreMatrixTestCase(String testName) {
        super(testName);
    }

    public void test() {

        MathMatrix matrix = new MathMatrix("Test matrix", 100);
        Section section;
        LineItem item;

        section = new Section("Section 1", 0.25);
        item = new LineItem("Line item 1", 0.5, 4, 1);
        section.addItem(item);
        item = new LineItem("Line item 2", 0.15, 4, 2);
        section.addItem(item);
        item = new LineItem("Line item 3", 0.15, 4, 3);
        section.addItem(item);
        item = new LineItem("Line item 4", 0.2, 4, 4);
        section.addItem(item);
        matrix.addItem(section);

        List sect = section.getItems();
        double wscore = 0;

        for(int i=0; i<4;i++) {
            assertTrue(((LineItem)sect.get(i)).getDescription().equals("Line item " + (i+1)));
            assertTrue(((LineItem)sect.get(i)).getMaximumScore() == (double) 4);
            assertTrue(((LineItem)sect.get(i)).getActualScore() == (double) (i+1));
            wscore += ((LineItem)sect.get(i)).getWeightedScore();
        }
        assertTrue("Weighted score for section should be equal to sum of "
                   + "weighted scores of nested items",
                   (section.getWeightedScore() - wscore) < MathMatrix.EPSILON);

        // Empty section
        section = new Section("Empty section", 0.5);
        assertTrue("Empty section should return a zero-length List of items",
                   section.getItems().size() == 0);
        assertTrue("Empty section should always return -1 from indexOf()",
                   section.indexOf(new LineItem("Line item", 1.0, 4, 4)) == -1);

		try {
			section.getMaximumScore();
			fail("Empty section .getMaximumScore() should throw an IllegalStateException");
		} catch (IllegalStateException ise) {
		}
		// this assertion contradicts the spec
        //assertTrue("Empty section should always return 0 as maximum score",
        //           section.getMaximumScore() == 0.00);
        try {
            section.getActualScore();
            fail("IllegalStateException should be thrown");
        } catch(IllegalStateException e) {}

        try {
            section.getWeightedScore();
            fail("IllegalStateException should be thrown");
        } catch(IllegalStateException e) {}

        // Section with 1 item
        section = new Section("Section with 1 item", 0.5);
        item = new LineItem("Item", 1.0, 4.5, 3.5);
        section.addItem(item);
        assertTrue("Section(1) should return a List of items with size 1",
                   section.getItems().size() == 1);
        assertTrue("Section(1) should always return 0 from indexOf() if item is found",
                   section.indexOf(item) == 0);
        assertTrue("Section(1) section should always return maximum score"
                   + " equal to nested item maximum score" ,
                   section.getMaximumScore() == item.getMaximumScore());
        assertTrue("Section(1) section should always return actual score"
                   + " equal to nested item actual score" ,
                   section.getActualScore() == item.getActualScore());
        assertTrue("Section(1) section should always return weighted score"
                   + " equal to nested item weighted score" ,
                   section.getWeightedScore() == item.getWeightedScore());



        // testing NullPointerException
        matrix  = new MathMatrix("Test matrix", 100.00);
        section = new Section("Section", 0.5) ;

        try {
            matrix.addItem((LineItem) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            matrix.indexOf((LineItem) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            section.addItem((LineItem) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}

        try {
            section.indexOf((LineItem) null);
            fail("NullPointerException should be thrown");
        } catch(NullPointerException e) {}


        // testing IndexOutOfBoundsException
        matrix  = new MathMatrix("Test matrix", 100.00);
        section = new Section("Section", 0.5) ;

        item = new LineItem("Line item", 0.5, 4, 2);

        matrix.addItem(item);
        section.addItem(item);
        matrix.addItem(section);

        try {
            matrix.getItem(10);
            fail("IndexOutOfBoundsException should be thrown");
        } catch(IndexOutOfBoundsException e) {}

        try {
            matrix.getItem(-1);
            fail("IndexOutOfBoundsException should be thrown");
        } catch(IndexOutOfBoundsException e) {}

        try {
            matrix.getItem(matrix.indexOf(new LineItem("1", 0.5, 4, 2)));
            fail("IndexOutOfBoundsException should be thrown");
        } catch(IndexOutOfBoundsException e) {}


    }

    public static Test suite() {
        return new junit.framework.TestSuite(ExploreMatrixTestCase.class);
    }
}
