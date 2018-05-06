/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.fieldconfig;

import java.util.ArrayList;
import java.util.List;

import com.topcoder.util.file.Util;

/**
 * <p>
 * The loop class is part of the API for programmatically configuring field values. It corresponds to a loop
 * in the template.
 * </p>
 * <p>
 * The class has a method to retrieve the list of children nodes (the fields and other loops inside the loop)
 * in a read-only structure as a "sample".
 * </p>
 * <p>
 * The idea is to provide useful information for an interactive GUI (the only reason why anyone would use this
 * API - otherwise it's much easier to write XML data).
 * </p>
 * <p>
 * A loop may be repeated 0 to many times but the user needs to see some information about the loop before
 * deciding to create data for loop items and that's what the "sample" is for.
 * </p>
 * <p>
 * An instance itself can be read-only if it is placed inside such a "sample" parent loop. Read-only means
 * deep read-only, that is every element, up to any depth is read-only.
 * </p>
 * <p>
 * The loop data can be configured using standard methods for adding, inserting, removing, clearing and getter
 * loop items. A "loop item" means the data that is used for one repetition of the loop. For example repeating
 * the loop 3 times would require 3 "loop items" containing the data for each repetition.
 * </p>
 * <p>
 * Changed in version 2.1 :
 * <li>isReadOnly method was added to conveniently determine whether a Loop is read only.</li>
 * <li>When cloning the loop subnodes, the condition nodes are cloned too.</li>
 * </p>
 * <p>
 * Thread Safety: The loopItems of this class are mutable, and so this class is not thread-safe. It is
 * recommended that multiple threads that utilize this class work on their own instance or treat it in a
 * read-only manner.
 * </p>
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @version 2.1
 * @since 2.0
 */
public class Loop implements Node {
    /**
     * <p>
     * The name of the loop (corresponds to the XML element that contains the loop sub-elements and is used in
     * for-each in the XSL transformation).
     * </p>
     * <p>
     * Initialized In: Constructor
     * </p>
     * <p>
     * Accessed In: getLoopElement
     * </p>
     * <p>
     * Modified In: Not Modified
     * </p>
     * <p>
     * Utilized In: Not utilized in this class
     * </p>
     * <p>
     * Valid Values: Not Null and Not empty Strings
     * </p>
     * @since 2.0
     */
    private final String loopElement;

    /**
     * The read-only list of subnodes (fields and other loops inside this loop).
     * @since 2.0
     */
    private NodeList loopSubnodes;

    /**
     * <p>
     * This is the loop's description.
     * </p>
     * <p>
     * Initialized In: Constructor
     * </p>
     * <p>
     * Accessed In: getDescription
     * </p>
     * <p>
     * Modified In: Not modified
     * </p>
     * <p>
     * Utilized In: Not utilized in this class
     * </p>
     * <p>
     * Valid Values: Possibly Null, possibly empty Strings
     * </p>
     * @since 2.0
     */
    private final String description;

    /**
     * <p>
     * Specifies whether this instance is read only.
     * </p>
     * <p>
     * Initialized In: Constructor
     * </p>
     * <p>
     * Accessed In: isReadOnly
     * </p>
     * <p>
     * Modified In: Not modified
     * </p>
     * <p>
     * Utilized In: addLoopItem, insertLoopItem, removeLoopItem, clearLoopItems
     * </p>
     * <p>
     * Valid Values: true or false
     * </p>
     * @since 2.0
     */
    private final boolean readOnly;

    /**
     * <p>
     * The list of data for each loop.
     * </p>
     * <p>
     * Initialized In: Constructor
     * </p>
     * <p>
     * Accessed In: Contents accessed in getLoopItems
     * </p>
     * <p>
     * Modified In: Contents modified in addLoopItem, insertLoopItem, removeLoopItem, clearLoopItems
     * </p>
     * <p>
     * Utilized In: Not utilized in this class
     * </p>
     * <p>
     * Valid Values: Non-null List implementation. Contents should not be non-null NodeList objects.
     * </p>
     * @since 2.0
     */
    private final List loopItems = new ArrayList();

    /**
     * Constructor.
     * @param loopElement
     *            the name of the loop (corresponds to the XML element that contains the loop sub-elements and
     *            is used in for-each in the XSL transformation).
     * @param loopSubnodes
     *            the read-only list of subnodes (fields and other loops inside this loop)
     * @param description
     *            description of the loop
     * @param readOnly
     *            should it be read-only?
     * @throws IllegalArgumentException
     *             if loopElement, loopSubnodes argument is <code>null</code> or if the loopElement is empty
     * @since 2.0
     */
    public Loop(String loopElement, NodeList loopSubnodes, String description, boolean readOnly) {
        // Check for null and for empty string.
        Util.checkNull(loopSubnodes, "loopSubnodes");
        Util.checkString(loopElement, "loopElement");

        // Set properties.
        this.loopElement = loopElement;
        this.loopSubnodes = loopSubnodes;
        this.description = description;
        this.readOnly = readOnly;
    }

    /**
     * Returns the name of the loop (corresponds to the XML element that contains the loop sub-elements and is
     * used in for-each in the XSL transformation).
     * @return the name of loop
     * @since 2.0
     */
    public String getLoopElement() {
        return loopElement;
    }

    /**
     * Gets a "sample" read-only list of the children nodes (fields and other "loops" inside this loop).
     * @return the read-only list of nodes
     * @since 2.0
     */
    public NodeList getSampleLoopItem() {
        return loopSubnodes;
    }

    /**
     * Adds a new loop data item and returns a node list that can be used to configure the data inside it. The
     * node list is not read-only (the contained fields and loops actually).
     * @return the new node list
     * @throws IllegalStateException
     *             if the current list is read-only
     * @since 2.0
     */
    public NodeList addLoopItem() {
        // Check whether this object is read-only.
        if (readOnly) {
            throw new IllegalStateException("The list is read-only.");
        }

        // Get mutable copy of 'sample'
        NodeList mutableCopy = makeMutableCopy();
        // Add this copy to our internal list.
        loopItems.add(mutableCopy);

        // Return it to user, it will configure it.
        return mutableCopy;
    }

    /**
     * Adds a new loop data item at a given position and returns a node list that can be used to configure the
     * data inside it. The node list is not read-only (the contained fields and loops actually).
     * @return the new node list
     * @param index
     *            the position to insert at (index == 0 means in front, index == current length means in the
     *            end)
     * @throws IndexOutOfBoundsException
     *             if the index is out of bounds (less than 0, greater than current count)
     * @throws IllegalStateException
     *             if the current list is read-only
     * @since 2.0
     */
    public NodeList insertLoopItem(int index) {
        // Check whether this object is read-only.
        if (readOnly) {
            throw new IllegalStateException("The list is read-only.");
        }

        // Check that index is correct.
        // Note : we validate here in order to make mutable copy only with
        // good arguments.
        if ((index < 0) || (index > loopItems.size())) {
            throw new IndexOutOfBoundsException("The index is out of bounds.");
        }

        // Get mutable copy of 'sample'
        NodeList mutableCopy = makeMutableCopy();
        // Add this copy to our internal list.
        loopItems.add(index, mutableCopy);
        // Return it to user, it will configure it.

        return mutableCopy;
    }

    /**
     * Removes a loop data item.
     * @return if the remove was successful (the item was present)
     * @param loopItem
     *            the loop data item to remove
     * @throws IllegalArgumentException
     *             if the argument is <code>null</code>
     * @throws IllegalStateException
     *             if the current list is read-only
     * @since 2.0
     */
    public boolean removeLoopItem(NodeList loopItem) {
        // Check for null first of all.
        Util.checkNull(loopItem, "loopItem");

        // Check whether this object is read-only.
        if (readOnly) {
            throw new IllegalStateException("The list is read-only.");
        }

        // Note: the list can not contain two same object.
        return loopItems.remove(loopItem);
    }

    /**
     * Clears all loop data items.
     * @throws IllegalStateException
     *             if the current list is read-only
     * @since 2.0
     */
    public void clearLoopItems() {
        // Check whether this object is read-only.
        if (readOnly) {
            throw new IllegalStateException("the list is read-only.");
        }

        // Clear loopItems.
        loopItems.clear();
    }

    /**
     * Gets the current loop items.
     * @return the loop items
     * @since 2.0
     */
    public NodeList[] getLoopItems() {
        // Get loop items.
        Object[] objects = loopItems.toArray();
        // Copy this items NodeList array.
        NodeList[] nodeLists = new NodeList[objects.length];
        System.arraycopy(objects, 0, nodeLists, 0, objects.length);
        // Return loop items.
        return nodeLists;
    }

    /**
     * Returns description of the loop.
     * @return return description of the loop
     * @since 2.0
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the mutable copy of <code>loopSubnodes</code>.
     * @return the mutable copy of <code>loopSubnodes</code>
     * @since 2.0
     */
    private NodeList makeMutableCopy() {
        // The array of original (read-only) subnodes.
        Node[] original = loopSubnodes.getNodes();
        // The array of mutable copy.
        Node[] mutableCopy = new Node[original.length];
        // Copy all subnodes to mutable copy.
        for (int i = 0; i < mutableCopy.length; i++) {
            if (original[i] instanceof Field) {
                // This node is Field.
                Field field = (Field) original[i];
                mutableCopy[i] = new Field(field.getName(), field.getValue(), field.getDescription(), false);
            } else if (original[i] instanceof Condition) {
                // This node is Condition
                Condition condition = (Condition) original[i];
                mutableCopy[i] = new Condition(condition.getName(), condition.getSubNodes(), condition
                        .getDescription(), false, condition.getConditionalStatement());
            } else {
                // This node is loop.
                Loop loop = (Loop) original[i];
                mutableCopy[i] = new Loop(loop.getLoopElement(), loop.getSampleLoopItem(), loop
                        .getDescription(), false);
            }
        }

        // Return mutable copy of nodes wrapped in NodeList.
        return new NodeList(mutableCopy);
    }

    /**
     * <p>
     * This indicates whether the <code>Loop</code> is read-only or not.
     * </p>
     * <p>
     * A read-only <code>Loop</code> cannot have any loop items added or removed from it.
     * </p>
     * @return whether the <code>Loop</code> is read-only or not.
     * @since 2.1
     */
    public boolean isReadOnly() {
        return readOnly;
    }
}