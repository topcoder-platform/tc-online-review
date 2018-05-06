package com.topcoder.util.weightedcalculator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

/**
 * <p>This test case tests MathMatrix.</p>
 *
 * @author  WishingBone
 * @version 1.0
 * @version Copyright C 2002, TopCoder, Inc. All rights reserved
 */
public class MathMatrixTestCase extends TestCase {

    public void testCreateMathMatrix(){
        assertNotNull(new MathMatrix("test"));
        try {
            new MathMatrix(null);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new MathMatrix("");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        assertNotNull(new MathMatrix("test", 100));
        try {
            new MathMatrix(null, 100);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new MathMatrix("", 100);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new MathMatrix("test", 0);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
    }

    public void testMathMatrixGetMethods() {
        MathMatrix mm = new MathMatrix("test");
        assertTrue(mm.getDescription().equals("test"));
        assertTrue(equal(mm.getWeight(), 1));
        assertTrue(equal(mm.getMaximumScore(), 100));
        mm = new MathMatrix("another test", 50);
        assertTrue(mm.getDescription().equals("another test"));
        assertTrue(equal(mm.getWeight(), 1));
        assertTrue(equal(mm.getMaximumScore(), 50));
    }

    public void testMathMatrixSetMethods() {
        MathMatrix mm = new MathMatrix("test");
        mm.setDescription("another test");
        assertTrue(mm.getDescription().equals("another test"));
        mm.setWeight(0.5);
        // this is the right behavior!
        assertTrue(equal(mm.getWeight(), 1));
        mm.addItem(new LineItem("foo", 1, 100, 50));
        mm.setMaximumScore(50);
        assertTrue(equal(mm.getMaximumScore(), 50));
        try {
            mm.setMaximumScore(49);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        mm = new MathMatrix("test", 49);
        mm.setDescription("another test");
        assertTrue(mm.getDescription().equals("another test"));
        mm.setWeight(0.5);
        // this is the right behavior!
        assertTrue(equal(mm.getWeight(), 1));
        mm.addItem(new LineItem("foo", 1, 100, 50));
        mm.setMaximumScore(50);
        assertTrue(equal(mm.getMaximumScore(), 50));
        try {
            mm.setMaximumScore(49);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
    }

    public void testMathMatrixAddItem() {
        MathMatrix mm = new MathMatrix("test");
        try {
            mm.addItem(null);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException npe) {
            // good
        }
        try {
            mm.addItem(new MathMatrix("test", 100));
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        mm.addItem(new LineItem("1", 0.3, 1));
        mm.addItem(new LineItem("2", 0.3, 1));
        mm.addItem(new Section("3", 0.4));
        try {
            mm.addItem(new Section("4", 0.1));
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iea) {
            // good
        }
    }

    public void testMathMatrixGetItem() {
        MathMatrix mm = new MathMatrix("test");
        mm.addItem(new LineItem("1", 0.3, 1));
        mm.addItem(new LineItem("2", 0.3, 1));
        mm.addItem(new Section("3", 0.4));
        assertTrue(mm.getItem(0).getDescription().equals("1"));
        assertTrue(mm.getItem(1).getDescription().equals("2"));
        assertTrue(mm.getItem(2).getDescription().equals("3"));
        try {
            mm.getItem(-1);
            fail("Should have thrown IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe) {
            // good
        }
        try {
            mm.getItem(3);
            fail("Should have thrown IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe) {
            // good
        }
    }

    public void testMathMatrixGetItems() {
        MathMatrix mm = new MathMatrix("test");
        assertTrue(mm.getItems().isEmpty());
        mm.addItem(new LineItem("1", 0.3, 1));
        mm.addItem(new LineItem("2", 0.3, 1));
        mm.addItem(new Section("3", 0.4));
        List l = mm.getItems();
        assertTrue(l.size() == 3);
        assertTrue(((Item)l.get(0)).getDescription().equals("1"));
        assertTrue(((Item)l.get(1)).getDescription().equals("2"));
        assertTrue(((Item)l.get(2)).getDescription().equals("3"));
    }

    public void testMathMatrixIndexOf() {
        MathMatrix mm = new MathMatrix("test");
        Item i1 = new LineItem("1", 0.3, 1);
        mm.addItem(i1);
        Item i2 = new LineItem("2", 0.3, 1);
        mm.addItem(i2);
        Item i3 = new Section("3", 0.4);
        mm.addItem(i3);
        assertTrue(mm.indexOf(i1) == 0);
        assertTrue(mm.indexOf(i2) == 1);
        assertTrue(mm.indexOf(i3) == 2);
        assertTrue(mm.indexOf(new LineItem("other", 1, 1)) == -1);
        try {
            mm.indexOf(null);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException npe) {
            // good
        }
    }

    public void testMathMatrixRemoveItem() {
        MathMatrix mm = new MathMatrix("test");
        Item i1 = new LineItem("1", 0.3, 1);
        mm.addItem(i1);
        Item i2 = new LineItem("2", 0.3, 1);
        mm.addItem(i2);
        Item i3 = new Section("3", 0.4);
        mm.addItem(i3);
        try {
            mm.removeItem(-1);
            fail("Should have thrown IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe) {
            // good
        }
        try {
            mm.removeItem(3);
            fail("Should have thrown IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe) {
            // good
        }
        assertTrue(mm.removeItem(2).getDescription().equals("3"));
        assertTrue(mm.removeItem(1).getDescription().equals("2"));
        assertTrue(mm.removeItem(0).getDescription().equals("1"));
        assertTrue(mm.getItems().isEmpty());
    }

    public void testMathMatrixGetActualScore() {
        Section s = new Section("test", 0.6);
        s.addItem(new LineItem("1", 0.3, 1, 0.5));
        s.addItem(new LineItem("2", 0.35, 2, 1.5));
        s.addItem(new LineItem("3", 0.15, 3, 0));
        s.addItem(new LineItem("4", 0.2, 4, 3));
        MathMatrix mm = new MathMatrix("test 2", 50);
        mm.addItem(s);
        mm.addItem(new LineItem("5", 0.4, 10, 3));
        assertTrue(equal(mm.getActualScore(), 0.5+1.5+0+3+3));
        mm.getItem(1).setWeight(0.35);
        try {
            mm.getActualScore();
            fail("Should have thrown IllegalStateException");
        }
        catch (IllegalStateException ise) {
            // good
        }
        mm.getItem(1).setWeight(0.4);
        s.getItem(0).setWeight(0.35);
        try {
            mm.getActualScore();
            fail("Should have thrown IllegalStateException");
        }
        catch (IllegalStateException ise) {
            // good
        }
    }

    public void testMathMatrixGetWeightedScore() {
        Section s = new Section("test", 0.6);
        s.addItem(new LineItem("1", 0.3, 1, 0.5));
        s.addItem(new LineItem("2", 0.35, 2, 1.5));
        s.addItem(new LineItem("3", 0.15, 3, 0));
        s.addItem(new LineItem("4", 0.2, 4, 3));
        MathMatrix mm = new MathMatrix("test 2", 50);
        mm.addItem(s);
        mm.addItem(new LineItem("5", 0.4, 10, 3));
        assertTrue(equal(mm.getWeightedScore(),
        50*(0.6*(0.3*0.5/1+0.35*1.5/2+0.15*0/3+0.2*3/4)+0.4*3/10)));
        mm.getItem(1).setWeight(0.35);
        try {
            mm.getWeightedScore();
            fail("Should have thrown IllegalStateException");
        }
        catch (IllegalStateException ise) {
            // good
        }
        mm.getItem(1).setWeight(0.4);
        s.getItem(0).setWeight(0.35);
        try {
            mm.getWeightedScore();
            fail("Should have thrown IllegalStateException");
        }
        catch (IllegalStateException ise) {
            // good
        }
    }

    public boolean equal(double a, double b) {
        return Math.abs(a - b) < MathMatrix.EPSILON;
    }

}
