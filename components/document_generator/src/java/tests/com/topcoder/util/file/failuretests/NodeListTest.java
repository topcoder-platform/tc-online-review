/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;

/**
 * @author AdamSelene This class tests NodeList for proper failure.
 */
public class NodeListTest extends TestCase {

    /**
     * Creates a attachment point for this testcase.
     *
     * @return a wonderful testsuite for this case.
     */
    public static Test suite() {
        return new TestSuite(NodeListTest.class);
    }

    /**
     * Tests NodeList constructor for null arg failure.
     */
    public void testConstructor() {
        try {
            new NodeList(null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests NodeList constructor for null arg failure.
     */
    public void testConstructor2() {
        try {
            new NodeList(new Node[500]);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

    /**
     * Tests NodeList constructor for null arg failure.
     */
    public void testConstructor3() {
        Node[] pass = new Node[500];

        for (int i = 0; i < 499; i++) {
            pass[i] = new Field("1", "2", "3", false);
        }

        pass[499] = null;

        try {
            new NodeList(pass);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception. " + e.toString());
        }
    }

}