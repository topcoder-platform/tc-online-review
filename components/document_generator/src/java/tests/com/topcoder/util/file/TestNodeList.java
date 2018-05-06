/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import junit.framework.TestCase;

/**
 * <p>
 * Title: TestNodeList
 * </p>
 *
 * <p>
 * Description: Test whole <code>NodeList</code> class.
 * </p>
 *
 * <p>
 * Company: TopCoder Software
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TestNodeList extends TestCase {
    /** Array of nodes. */
    private Node[] nodes = new Node[] {new Field("first", "fierst", "first", true),
        new Field("second", "second", "second", true)};

    /** NodeList. */
    private NodeList nodeList = null;

    /**
     * Creates <code>NodeList</code> object.
     */
    public void setUp() {
        nodeList = new NodeList(nodes);
    }

    /**
     * Test {@link NodeList#NodeList(Node[])} constructor on good data.
     */
    public void testConstructor() {
        // The nodeList was created in setUp method.
        assertNotNull("The NodeList object was not created", nodeList);
    }

    /**
     * Test {@link NodeList#NodeList(Node[])} constructor with
     * <code>null</code> as argument.
     */
    public void testConstructorWithNull() {
        try {
            // Try to create NodeList with null as argument.
            new NodeList(null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link NodeList#NodeList(Node[])} constructor with array
     * with<code>null</code> element as argument.
     */
    public void testConstructorWithNullElement() {
        try {
            // Try to create NodeList with array with null element as argument.
            new NodeList(new Node[] {null});

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Check that {@link NodeList#getNodes()} method return correct nodes.
     */
    public void testGetNodes() {
        // Get copy of nodes.
        Node[] copy = nodeList.getNodes();

        // Check that returned nodes is correct.
        assertEquals("The returned nodes is incorrect.", ((Field) copy[0]).getName(), "first");
        assertEquals("The returned nodes is incorrect.", ((Field) copy[1]).getName(), "second");
    }

    /**
     * Check that {@link NodeList#getNodes()} method return copy of array.
     */
    public void testGetNodesCopy() {
        assertNotSame("The getNodes method return same array.", nodeList.getNodes(), nodes);
    }
}
