package com.topcoder.util.weightedcalculator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

/**
 * <p>This test case tests Section.</p>
 *
 * @author  WishingBone
 * @version 1.0
 * @version Copyright C 2002, TopCoder, Inc. All rights reserved
 */
public class SectionTestCase extends TestCase {

    public void testCreateSection() {
        assertNotNull(new Section("test", 1));
        try {
            new Section(null, 1);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new Section("", 1);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new Section("test", 0);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new Section("test", 1.1);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
    }

    public void testSectionGetMethods() {
        Section s = new Section("test", 1);
        assertTrue(s.getDescription().equals("test"));
        assertTrue(equal(s.getWeight(), 1));
    }

    public void testSectionSetMethods() {
        Section s = new Section("test", 1);
        s.setDescription("another test");
        assertTrue(s.getDescription().equals("another test"));
        s.setWeight(0.5);
        assertTrue(equal(s.getWeight(), 0.5));
    }

    public void testSectionAddItem() {
        Section s = new Section("test", 1);
        try {
            s.addItem(null);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException npe) {
            // good
        }
        try {
            s.addItem(new MathMatrix("test", 100));
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iae) {
            // good
        }
        s.addItem(new LineItem("1", 0.3, 1));
        s.addItem(new LineItem("2", 0.3, 1));
        s.addItem(new Section("3", 0.4));
        try {
            s.addItem(new Section("4", 0.1));
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException iea) {
            // good
        }
    }

    public void testSectionGetItem() {
        Section s = new Section("test", 1);
        s.addItem(new LineItem("1", 0.3, 1));
        s.addItem(new LineItem("2", 0.3, 1));
        s.addItem(new Section("3", 0.4));
        assertTrue(s.getItem(0).getDescription().equals("1"));
        assertTrue(s.getItem(1).getDescription().equals("2"));
        assertTrue(s.getItem(2).getDescription().equals("3"));
        try {
            s.getItem(-1);
            fail("Should have thrown IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe) {
            // good
        }
        try {
            s.getItem(3);
            fail("Should have thrown IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe) {
            // good
        }
    }

    public void testSectionGetItems() {
        Section s = new Section("test", 1);
        assertTrue(s.getItems().isEmpty());
        s.addItem(new LineItem("1", 0.3, 1));
        s.addItem(new LineItem("2", 0.3, 1));
        s.addItem(new Section("3", 0.4));
        List l = s.getItems();
        assertTrue(l.size() == 3);
        assertTrue(((Item)l.get(0)).getDescription().equals("1"));
        assertTrue(((Item)l.get(1)).getDescription().equals("2"));
        assertTrue(((Item)l.get(2)).getDescription().equals("3"));
    }

    public void testSectionIndexOf() {
        Section s = new Section("test", 1);
        Item i1 = new LineItem("1", 0.3, 1);
        s.addItem(i1);
        Item i2 = new LineItem("2", 0.3, 1);
        s.addItem(i2);
        Item i3 = new Section("3", 0.4);
        s.addItem(i3);
        assertTrue(s.indexOf(i1) == 0);
        assertTrue(s.indexOf(i2) == 1);
        assertTrue(s.indexOf(i3) == 2);
        assertTrue(s.indexOf(new LineItem("other", 1, 1)) == -1);
        try {
            s.indexOf(null);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException npe) {
            // good
        }
    }

    public void testSectionRemoveItem() {
        Section s = new Section("test", 1);
        Item i1 = new LineItem("1", 0.3, 1);
        s.addItem(i1);
        Item i2 = new LineItem("2", 0.3, 1);
        s.addItem(i2);
        Item i3 = new Section("3", 0.4);
        s.addItem(i3);
        try {
            s.removeItem(-1);
            fail("Should have thrown IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe) {
            // good
        }
        try {
            s.removeItem(3);
            fail("Should have thrown IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException ioobe) {
            // good
        }
        assertTrue(s.removeItem(2).getDescription().equals("3"));
        assertTrue(s.removeItem(1).getDescription().equals("2"));
        assertTrue(s.removeItem(0).getDescription().equals("1"));
        assertTrue(s.getItems().isEmpty());
    }

    public void testSectionGetMaximumScore() {
        Section s = new Section("test", 1);
        s.addItem(new LineItem("1", 0.3, 1, 0.5));
        s.addItem(new LineItem("2", 0.35, 2, 1.5));
        s.addItem(new LineItem("3", 0.15, 3, 0));
        s.addItem(new LineItem("4", 0.2, 4, 3));
        assertTrue(equal(s.getMaximumScore(), 1+2+3+4));
        Section ss = new Section("test 2", 1);
        s.setWeight(0.6);
        ss.addItem(s);
        ss.addItem(new LineItem("5", 0.4, 10, 3));
        assertTrue(equal(ss.getMaximumScore(), 1+2+3+4+10));
        ss.getItem(1).setWeight(0.35);
        try {
            ss.getMaximumScore();
            fail("Should have thrown IllegalStateException");
        }
        catch (IllegalStateException ise) {
            // good
        }
        ss.getItem(1).setWeight(0.4);
        s.getItem(0).setWeight(0.35);
        try {
            ss.getMaximumScore();
            fail("Should have thrown IllegalStateException");
        }
        catch (IllegalStateException ise) {
            // good
        }
    }

    public void testSectionGetActualScore() {
        Section s = new Section("test", 1);
        s.addItem(new LineItem("1", 0.3, 1, 0.5));
        s.addItem(new LineItem("2", 0.35, 2, 1.5));
        s.addItem(new LineItem("3", 0.15, 3, 0));
        s.addItem(new LineItem("4", 0.2, 4, 3));
        assertTrue(equal(s.getActualScore(), 0.5+1.5+0+3));
        Section ss = new Section("test 2", 1);
        s.setWeight(0.6);
        ss.addItem(s);
        ss.addItem(new LineItem("5", 0.4, 10, 3));
        assertTrue(equal(ss.getActualScore(), 0.5+1.5+0+3+3));
        ss.getItem(1).setWeight(0.35);
        try {
            ss.getActualScore();
            fail("Should have thrown IllegalStateException");
        }
        catch (IllegalStateException ise) {
            // good
        }
        ss.getItem(1).setWeight(0.4);
        s.getItem(0).setWeight(0.35);
        try {
            ss.getActualScore();
            fail("Should have thrown IllegalStateException");
        }
        catch (IllegalStateException ise) {
            // good
        }
    }

    public void testSectionGetWeightedScore() {
        Section s = new Section("test", 1);
        s.addItem(new LineItem("1", 0.3, 1, 0.5));
        s.addItem(new LineItem("2", 0.35, 2, 1.5));
        s.addItem(new LineItem("3", 0.15, 3, 0));
        s.addItem(new LineItem("4", 0.2, 4, 3));
        assertTrue(equal(s.getWeightedScore(),
        (1+2+3+4)*(0.3*0.5/1+0.35*1.5/2+0.15*0/3+0.2*3/4)));
        Section ss = new Section("test 2", 1);
        s.setWeight(0.6);
        ss.addItem(s);
        ss.addItem(new LineItem("5", 0.4, 10, 3));
        assertTrue(equal(ss.getWeightedScore(),
        (1+2+3+4+10)*(0.6*(0.3*0.5/1+0.35*1.5/2+0.15*0/3+0.2*3/4)+0.4*3/10)));
        ss.getItem(1).setWeight(0.35);
        try {
            ss.getWeightedScore();
            fail("Should have thrown IllegalStateException");
        }
        catch (IllegalStateException ise) {
            // good
        }
        ss.getItem(1).setWeight(0.4);
        s.getItem(0).setWeight(0.35);
        try {
            ss.getWeightedScore();
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
