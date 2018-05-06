package com.topcoder.util.weightedcalculator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case tests LineItem.</p>
 *
 * @author  WishingBone
 * @version 1.0
 * @version Copyright C 2002, TopCoder, Inc. All rights reserved
 */
public class LineItemTestCase extends TestCase {

    public void testCreateLineItem() {
        assertNotNull(new LineItem("test", 1, 1));
        try {
            new LineItem(null, 1, 1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new LineItem("", 1, 1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new LineItem("test", 0, 1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new LineItem("test", 1.1, 1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new LineItem("test", 1, 0);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        assertNotNull(new LineItem("test", 1, 1, 1));
        try {
            new LineItem(null, 1, 1, 1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new LineItem("", 1, 1, 1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new LineItem("test", 0, 1, 1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new LineItem("test", 1.1, 1, 1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new LineItem("test", 1, 0, 1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new LineItem("test", 1, 1, -0.1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new LineItem("test", 1, 1, 1.1);
            fail("Should have thrown IllegalArguementException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
    }

    public void testLineItemGetMethods() {
        LineItem li = new LineItem("test", 1, 1);
        assertTrue(li.getDescription().equals("test"));
        assertTrue(equal(li.getWeight(), 1));
        assertTrue(equal(li.getMaximumScore(), 1));
        assertTrue(equal(li.getActualScore(), 0));
        li = new LineItem("another test", 1, 1, 1);
        assertTrue(li.getDescription().equals("another test"));
        assertTrue(equal(li.getWeight(), 1));
        assertTrue(equal(li.getMaximumScore(), 1));
        assertTrue(equal(li.getActualScore(), 1));
    }

    public void testLineItemSetMethods() {
        LineItem li = new LineItem("test", 1, 1);
        li.setDescription("another test");
        assertTrue(li.getDescription().equals("another test"));
        li.setWeight(0.5);
        assertTrue(equal(li.getWeight(), 0.5));
        li.setMaximumScore(2);
        assertTrue(equal(li.getMaximumScore(), 2));
        li.setActualScore(2);
        assertTrue(equal(li.getActualScore(), 2));
        li = new LineItem("test", 1, 1, 1);
        li.setDescription("another test");
        assertTrue(li.getDescription().equals("another test"));
        li.setWeight(0.5);
        assertTrue(equal(li.getWeight(), 0.5));
        li.setMaximumScore(2);
        assertTrue(equal(li.getMaximumScore(), 2));
        li.setActualScore(2);
        assertTrue(equal(li.getActualScore(), 2));
    }

    public void testLineItemGetWeightedScore() {
        assertTrue(equal((new LineItem("test", 1, 1)).getWeightedScore(), 0));
        assertTrue(equal((new LineItem("test", 1, 1, 1)).getWeightedScore(), 1));
        assertTrue(equal((new LineItem("test", 1, 1, 0.5)).getWeightedScore(), 0.5));
        assertTrue(equal((new LineItem("test", 0.5, 1, 1)).getWeightedScore(), 0.5));
        assertTrue(equal((new LineItem("test", 0.5, 1, 0.5)).getWeightedScore(), 0.25));
        assertTrue(equal((new LineItem("test", 1, 2, 1)).getWeightedScore(), 1));
        assertTrue(equal((new LineItem("test", 1, 2, 0.5)).getWeightedScore(), 0.5));
        assertTrue(equal((new LineItem("test", 0.5, 2, 1)).getWeightedScore(), 0.5));
        assertTrue(equal((new LineItem("test", 0.5, 2, 0.5)).getWeightedScore(), 0.25));
    }

    public boolean equal(double a, double b) {
        return Math.abs(a - b) < MathMatrix.EPSILON;
    }

}
