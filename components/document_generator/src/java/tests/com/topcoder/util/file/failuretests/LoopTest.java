/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;

/**
 * @author AdamSelene This class tests Loop for proper failure.
 */
public class LoopTest extends TestCase {

    /**
     * Creates a attachment point for this testcase.
     *
     * @return a wonderful testsuite for this case.
     */
    public static Test suite() {
        return new TestSuite(LoopTest.class);
    }

    /**
     * Tests Loop constructor for null arg failure.
     */
    public void testConstructor() {
        try {
            new Loop(null, new NodeList(new Node[0]), null, true);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests Loop constructor for null arg failure.
     */
    public void testConstructor_2() {
        try {
            new Loop("test", null, null, true);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests Loop constructor for null arg failure.
     */
    public void testConstructorES() {
        try {
            new Loop("", new NodeList(new Node[0]), null, true);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests addLoopItem for readonly fail.
     */
    public void testAdd() {
        try {
            Loop l = new Loop("test", new NodeList(new Node[0]), null, true);
            l.addLoopItem();
            fail("Did not fail.");
        } catch (IllegalStateException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests insertLoopItem for readonly fail.
     */
    public void testInsertRO() {
        try {
            Loop l = new Loop("test", new NodeList(new Node[] {new Field("1", "2", "3", false) }), null, true);
            l.insertLoopItem(0);
            fail("Did not fail.");
        } catch (IllegalStateException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests insertLoopItem for -1 fail.
     */
    public void testInsertNeg1() {
        try {
            Loop l = new Loop("test", new NodeList(new Node[] {new Field("1", "2", "3", false) }), null, false);
            l.insertLoopItem(-1);
            fail("Did not fail.");
        } catch (IndexOutOfBoundsException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests insertLoopItem for far fail.
     */
    public void testInsertNeg() {
        try {
            Loop l = new Loop("test", new NodeList(new Node[] {new Field("1", "2", "3", false) }), null, false);
            l.insertLoopItem(Integer.MIN_VALUE);
            fail("Did not fail.");
        } catch (IndexOutOfBoundsException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests insertLoopItem for far fail.
     */
    public void testInsertOOB() {
        try {
            Loop l = new Loop("test", new NodeList(new Node[] {new Field("1", "2", "3", false) }), null, false);
            l.insertLoopItem(Integer.MAX_VALUE);
            fail("Did not fail.");
        } catch (IndexOutOfBoundsException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests insertLoopItem for near fail.
     */
    public void testInsertOOB1() {
        try {
            Loop l = new Loop("test", new NodeList(new Node[] {new Field("1", "2", "3", false) }), null, false);
            l.insertLoopItem(2);
            fail("Did not fail.");
        } catch (IndexOutOfBoundsException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests insertLoopItem for proper behavior on simple insert.
     */
    public void testInsertNoFail() {
        try {
            Loop l = new Loop("test", new NodeList(new Node[0]), null, false);
            l.insertLoopItem(0);
        } catch (Exception e) {
            fail("Should not fail - exception. " + e.toString());
        }
    }

    /**
     * Tests removeLoopItem for readonly fail.
     */
    public void testRemoveRO() {
        NodeList nl = new NodeList(new Node[] {new Field("1", "2", "3", false) });
        try {
            new Loop("test", nl, null, true).removeLoopItem(nl);
            fail("Did not fail.");
        } catch (IllegalStateException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests removeLoopItem for readonly fail.
     */
    public void testRemoveNull() {
        NodeList nl = new NodeList(new Node[] {new Field("1", "2", "3", false) });
        try {
            new Loop("test", nl, null, true).removeLoopItem(null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests removeLoopItem for readonly fail.
     */
    public void testClearRO() {
        NodeList nl = new NodeList(new Node[] {new Field("1", "2", "3", false) });
        try {
            new Loop("test", nl, null, true).clearLoopItems();
            fail("Did not fail.");
        } catch (IllegalStateException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

}