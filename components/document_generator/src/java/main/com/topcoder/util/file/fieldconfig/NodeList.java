/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.fieldconfig;

import com.topcoder.util.file.Util;

/**
 * <p>
 * An immutable list of nodes used for better handling of the nodes (fields and
 * loops) within a loop.
 * </p>
 *
 * <p>
 * Thread-Safety: This class is immutable and therefore thread-safe.
 * </p>
 *
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @version 2.1
 * @since 2.0
 */
public class NodeList {
    /**
     * <p>
     * This is the array of nodes which are represented by this NodeList.
     * </p>
     * <p>
     * Initialized In: Constructor.
     * </p>
     * <p>
     * Accessed In: getNodes
     * </p>
     * <p>
     * Modified In: Not Modified
     * </p>
     * <p>
     * Utilized in: Not Utilized in this class
     * </p>
     * <p>
     * Valid Values: Non-empty Node array.  Should not contain null elements
     * </p>
     *
     * @since 2.0
     */
    private Node[] nodes;

    /**
     * Constructor.
     *
     * @param nodes the array of nodes
     *
     * @throws IllegalArgumentException if nodes is <code>null</code> or if
     * the elements are <code>null</code>
     *
     * @since 2.0
     */
    public NodeList(Node[] nodes) {
        // Check for null nodes first of all.
        Util.checkNull(nodes, "nodes");
        // Check each elements of array for null.
        for (int i = 0; i < nodes.length; i++) {
            Util.checkNull(nodes[i], "nodes[" + i + "]");
        }
        // Set property.
        this.nodes = nodes;
    }

    /**
     * Gets the list of nodes (a copy of the inner array!).
     *
     * @return the list of nodes as an array
     *
     * @since 2.0
     */
    public Node[] getNodes() {
        // Return copy of nodes.
        return (Node[]) nodes.clone();
    }
}
