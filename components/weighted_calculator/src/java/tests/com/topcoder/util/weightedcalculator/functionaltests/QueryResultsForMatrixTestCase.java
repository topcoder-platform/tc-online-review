/*
 * @(#) QueryResultsForMatrixTestCase.java
 *
 * 1.0  01/03/2003
 */

package com.topcoder.util.weightedcalculator.functionaltests;

import junit.framework.*;
import com.topcoder.util.weightedcalculator.*;

/**
 * Tests main counting methods of LineItem, Section, MathMatrix
 *
 * @author isv
 * @version 1.0
 */
public class QueryResultsForMatrixTestCase extends TestCase {

    public QueryResultsForMatrixTestCase(String testName) {
        super(testName);
    }

    public void test() {
        MathMatrix matrix = new MathMatrix("Test Matrix", 100);
        Section section;
        LineItem item;

        // Empty matrix
        matrix = new MathMatrix("Empty matrix", 100);
        assertTrue("Empty matrix should return a zero-length List of items",
                   matrix.getItems().size() == 0);

        assertTrue("Empty matrix should always return -1 from indexOf()",
                   matrix.indexOf(new LineItem("Line item", 0.5, 4, 4)) == -1);

        assertTrue("Empty matrix should always return a maximum score",
                   matrix.getMaximumScore() == 100);


        try {
            matrix.getActualScore();
            fail("IllegalStateException should be thrown");
        } catch(IllegalStateException e) {}

        try {
            matrix.getWeightedScore();
            fail("IllegalStateException should be thrown");
        } catch(IllegalStateException e) {}


        // matrix with 1 item
        matrix = new MathMatrix("matrix with 1 item", 200);
        item = new LineItem("Item", 1.0, 4.5, 3.5);
        matrix.addItem(item);
        assertTrue("Matrix(1) should return a List of items with size 1",
                   matrix.getItems().size() == 1);
        assertTrue("Matrix(1) should always return 0 from indexOf() if item is found",
                   matrix.indexOf(item) == 0);
        assertTrue("Matrix(1) should always return a maximum score",
                   matrix.getMaximumScore() == 200);
        assertTrue("Matrix(1) section should always return actual score"
                   + " equal to nested item actual score" ,
                   matrix.getActualScore() == item.getActualScore());
        assertTrue("Matrix(1) should always return weighted score"
                   + " equal to nested item weighted score" ,
                   matrix.getWeightedScore() == item.getWeightedScore());


        matrix = new MathMatrix("matrix with multiple items", 100);
        try {
            section = new Section("1", 0.25);
            item = new LineItem("1", 0.5, 4, 1);
            section.addItem(item);
            item = new LineItem("2", 0.15, 4, 2);
            section.addItem(item);
            item = new LineItem("3", 0.15, 4, 3);
            section.addItem(item);
            item = new LineItem("4", 0.2, 4, 4);
            section.addItem(item);
            matrix.addItem(section);

            section = new Section("2", 0.20);
            item = new LineItem("1", 0.5, 4, 4);
            section.addItem(item);
            item = new LineItem("2", 0.5, 4, 2);
            section.addItem(item);
            matrix.addItem(section);

            section = new Section("3", 0.15);
            item = new LineItem("1", 1, 4, 4);
            section.addItem(item);
            matrix.addItem(section);

            section = new Section("4", 0.20);
            item = new LineItem("1", 0.33, 4, 1);
            section.addItem(item);
            item = new LineItem("2", 0.33, 4, 2);
            section.addItem(item);
            item = new LineItem("3", 0.34, 4, 3);
            section.addItem(item);
            matrix.addItem(section);

            section = new Section("5", 0.10);
            matrix.addItem(section);

            item = new LineItem("6", 0.10, 4, 2);
            matrix.addItem(item);



            // Check weighted score
			try {
            	assertTrue(equal(53.36,matrix.getWeightedScore()));
            	fail("Matrix had an empty section.  Expected IllegalStateException.");
			} catch (IllegalStateException ise) {
			}

            section = (Section) matrix.getItem(0);
            assertTrue(equal(12.81, section.getWeightedScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(3.125, item.getWeightedScore()));

            item = (LineItem) section.getItem(1);
            assertTrue(equal(1.875, item.getWeightedScore()));

            item = (LineItem) section.getItem(2);
            assertTrue(equal(2.8125, item.getWeightedScore()));

            item = (LineItem) section.getItem(3);
            assertTrue(equal(5.00, item.getWeightedScore()));



            section = (Section) matrix.getItem(1);
            assertTrue(equal(15.00, section.getWeightedScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(10.00, item.getWeightedScore()));

            item = (LineItem) section.getItem(1);
            assertTrue(equal(5.00, item.getWeightedScore()));





            section = (Section) matrix.getItem(2);
            assertTrue(equal(15.00, section.getWeightedScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(15.00, item.getWeightedScore()));




            section = (Section) matrix.getItem(3);
            assertTrue(equal(10.05, section.getWeightedScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(1.65, item.getWeightedScore()));

            item = (LineItem) section.getItem(1);
            assertTrue(equal(3.30, item.getWeightedScore()));

            item = (LineItem) section.getItem(2);
            assertTrue(equal(5.10, item.getWeightedScore()));




            section = (Section) matrix.getItem(4);
            try {
                section.getWeightedScore();
                fail("IllegalStateException should be thrown");
            } catch(IllegalStateException e) {}

            // hack so remaining calculations are correct
            section.addItem(new LineItem("blank",1.0,10,0));

            item = (LineItem) matrix.getItem(5);
            assertTrue(equal(5.0, item.getWeightedScore()));


            // Check actual score

            assertTrue(equal(28.00,matrix.getActualScore()));


            section = (Section) matrix.getItem(0);
            assertTrue(equal(10.00, section.getActualScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(1.00 , item.getActualScore()));

            item = (LineItem) section.getItem(1);
            assertTrue(equal(2.00 , item.getActualScore()));

            item = (LineItem) section.getItem(2);
            assertTrue(equal(3.00, item.getActualScore()));

            item = (LineItem) section.getItem(3);
            assertTrue(equal(4.00, item.getActualScore()));



            section = (Section) matrix.getItem(1);
            assertTrue(equal(6.00, section.getActualScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(4.00, item.getActualScore()));

            item = (LineItem) section.getItem(1);
            assertTrue(equal(2.00, item.getActualScore()));



            section = (Section) matrix.getItem(2);
            assertTrue(equal(4.00, section.getActualScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(4.00, item.getActualScore()));




            section = (Section) matrix.getItem(3);
            assertTrue(equal(6.00, section.getActualScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(1.00, item.getActualScore()));

            item = (LineItem) section.getItem(1);
            assertTrue(equal(2.00, item.getActualScore()));

            item = (LineItem) section.getItem(2);
            assertTrue(equal(3.00, item.getActualScore()));




            section = (Section) matrix.getItem(4);
			section.getActualScore();




            item = (LineItem) matrix.getItem(5);
            assertTrue(equal(2.00, item.getActualScore()));



            // Check maximum score

            assertTrue(equal(100.00,matrix.getMaximumScore()));


            section = (Section) matrix.getItem(0);
            assertTrue(equal(16.00, section.getMaximumScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(4.00 , item.getMaximumScore()));

            item = (LineItem) section.getItem(1);
            assertTrue(equal(4.00 , item.getMaximumScore()));

            item = (LineItem) section.getItem(2);
            assertTrue(equal(4.00, item.getMaximumScore()));

            item = (LineItem) section.getItem(3);
            assertTrue(equal(4.00, item.getMaximumScore()));



            section = (Section) matrix.getItem(1);
            assertTrue(equal(8.00, section.getMaximumScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(4.00, item.getMaximumScore()));

            item = (LineItem) section.getItem(1);
            assertTrue(equal(4.00, item.getMaximumScore()));





            section = (Section) matrix.getItem(2);
            assertTrue(equal(4.00, section.getMaximumScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(4.00, item.getMaximumScore()));


            section = (Section) matrix.getItem(3);
            assertTrue(equal(12.00, section.getMaximumScore()));

            item = (LineItem) section.getItem(0);
            assertTrue(equal(4.00, item.getMaximumScore()));

            item = (LineItem) section.getItem(1);
            assertTrue(equal(4.00, item.getMaximumScore()));

            item = (LineItem) section.getItem(2);
            assertTrue(equal(4.00, item.getMaximumScore()));


            section = (Section) matrix.getItem(4);
            section.getMaximumScore();


            item = (LineItem) matrix.getItem(5);
            assertTrue(equal(4.00, item.getMaximumScore()));



        } catch(Exception e) {
			e.printStackTrace();
            fail("No exception should be thrown: "+e);
        }


        // test for IllegalStateException
        matrix = new MathMatrix("Test matrix", 100);

        section = new Section("Section 1", 0.25);

        item = new LineItem("Line item 1", 0.5, 4, 4);
        section.addItem(item);

        item = new LineItem("Line item 2", 0.15, 4, 4);
        section.addItem(item);

        item = new LineItem("Line item 3", 0.15, 4, 4);
        section.addItem(item);

        item = new LineItem("Line item 4", 0.1, 4, 4);
        section.addItem(item);

        matrix.addItem(section);

        try {
            section.getWeightedScore();
            fail("IllegalStateException should be thrown");
        } catch(IllegalStateException e) {}

        try {
            matrix.getWeightedScore();
            fail("IllegalStateException should be thrown");
        } catch(IllegalStateException e) {}

        try {
            section.getMaximumScore();
            fail("IllegalStateException should be thrown");
        } catch(IllegalStateException e) {}

        try {
            section.getActualScore();
            fail("IllegalStateException should be thrown");
        } catch(IllegalStateException e) {}

        try {
            matrix.getActualScore();
            fail("IllegalStateException should be thrown");
        } catch(IllegalStateException e) {}

    }

    public static Test suite() {
        return new junit.framework.TestSuite(QueryResultsForMatrixTestCase.class);
    }

    private boolean equal(double v1, double v2) {
    	return Math.abs(v1-v2) < MathMatrix.EPSILON;
    }
}
