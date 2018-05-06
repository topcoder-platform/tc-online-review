/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import junit.framework.TestCase;

/**
 * <p>
 * Title: TestLoop
 * </p>
 *
 * <p>
 * Description: Test whole <code>Loop</code> class.
 * </p>
 *
 * <p>
 * Company: TopCoder Software
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TestLoop extends TestCase {
    /** Mutable field. */
    private Field fieldMutable;

    /** Read-only field. */
    private Field fieldReadOnly;

    /**
     * <p>
     * The Condition instance for testing.
     * </p>
     */
    private Condition conditionReadOnly;

    /**
     * <p>
     * The Loop instance for testing.
     * </p>
     */
    private Loop loopMutable;

    /**
     * <p>
     * The Loop instance for testing.
     * </p>
     */
    private Loop loopReadOnly;

    /**
     * <p>
     * The node list for testing.
     * </p>
     */
    private NodeList nodeList;

    /**
     * Create new mutable field, nodeList and both mutable and read-only loops.
     */
    public void setUp() {
        // Create mutable field.
        fieldMutable = new Field("name", "value", "description", false);

        fieldReadOnly = new Field("name", "value", "description", true);

        conditionReadOnly = new Condition("name", new NodeList(new Node[0]), "description", true, "name = 'test'");

        // Create node list.
        nodeList = new NodeList(new Node[] {fieldReadOnly, fieldMutable});

        // Create mutable loop.
        loopMutable = new Loop("loop", nodeList, "description", false);

        // Create read-only loop.
        loopReadOnly = new Loop("loop", nodeList, "description", true);
    }

    /**
     * Test {@link Loop#Loop(String, NodeList, String, boolean)} constructor on
     * good data.
     */
    public void testConstructor() {
        // Check that Loop objects, which was created in setUp method was
        // created successfully.
        assertNotNull("The Loop objects was not created.", loopMutable);
        assertNotNull("The Loop objects was not created.", loopReadOnly);
    }

    /**
     * Test {@link Loop#Loop(String, NodeList, String, boolean)} constructor
     * with <code>null</code> as first argument.
     */
    public void testConstructorWithFirstNull() {
        try {
            // Try to create Loop object with null as first argument.
            new Loop(null, nodeList, "description", true);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link Loop#Loop(String, NodeList, String, boolean)} constructor
     * with <code>null</code> as second argument.
     */
    public void testConstructorWithSecondNull() {
        try {
            // Try to create Loop object with null as second argument.
            new Loop("loop", null, "Description", true);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link Loop#Loop(String, NodeList, String, boolean)} constructor
     * with empty string as first argument.
     */
    public void testConstructorWithEmpty() {
        try {
            // Try to create Loop object with empty string as first argument.
            new Loop("   ", nodeList, "description", true);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link Loop#getLoopElement()} method.
     */
    public void testGetLoopElement() {
        // Check the loopElement property of loopMutable.
        assertEquals("The loopElement property is incorrect.", loopMutable.getLoopElement(), "loop");
    }

    /**
     * Test {@link Loop#getSampleLoopItem()} method.
     */
    public void testGetSampleLoopItem() {
        // Check how work getSampleLoopItem method.
        assertEquals("The getSampleLoopItem work incorrectly.", loopMutable.getSampleLoopItem(), nodeList);
    }

    /**
     * Test {@link Loop#addLoopItem()} method.
     */
    public void testAddLoopItem() {
        // Add two loop item.
        loopMutable.addLoopItem();
        loopMutable.addLoopItem();

        // Check loop items.
        NodeList[] nodeLists = loopMutable.getLoopItems();
        assertEquals("The count of node items is incorrect.", nodeLists.length, 2);

        // Test first node list, it should contain two mutable fields.
        nodeList = nodeLists[0];

        // Check that this is our fields, on first field.
        Field field = (Field) nodeList.getNodes()[0];
        assertEquals("The node list contain incorrect field.", field.getDescription(), "description");

        // Check that field is mutable.
        // Try to set new value.
        try {
            field.setValue("new_value");
        } catch (IllegalStateException e) {
            fail("The field should be mutable.");
        }
    }

    /**
     * Test {@link Loop#addLoopItem()} method, when loop is read-only.
     */
    public void testAddLoopItemWithReadOnly() {
        try {
            loopReadOnly.addLoopItem();

            // Fail.
            fail("Should throw IllegalStateException.");
        } catch (IllegalStateException e) {
            // Success.
        }
    }

    /**
     * Test {@link Loop#insertLoopItem(int)} method.
     */
    public void testInsertLoopItem() {
        // Create complex node list.
        NodeList complexNodeList = new NodeList(new Node[] {loopReadOnly, fieldReadOnly});

        // Create complex loop.
        Loop loop = new Loop("complexLoop", complexNodeList, "This is complex loop", false);

        // Insert data to the end.
        NodeList nodeLists = null;
        nodeLists = loop.insertLoopItem(0);

        // Configure inserted data.
        ((Loop) nodeLists.getNodes()[0]).insertLoopItem(0);
        ((Field) nodeLists.getNodes()[1]).setValue("This is marked field");

        // Insert data to the first position.
        nodeLists = loop.insertLoopItem(0);

        // Configure inserted data.
        ((Field) nodeLists.getNodes()[1]).setValue("This is not marked field");

        // Check that all data that we insert was inserted.
        NodeList[] lists = loop.getLoopItems();

        // Check that on the first(zero) position is not marked field.
        assertEquals("The data was inserted incorrectly.", ((Field) lists[0].getNodes()[1]).getValue(),
            "This is not marked field");

        // Check that on the second position we have loop, where inserted
        // one element.
        assertEquals("The data was inserted incorrectly.", ((Loop) lists[1].getNodes()[0]).getLoopItems().length, 1);
    }

    /**
     * Test {@link Loop#insertLoopItem(int)} method, when loop is read-only.
     */
    public void testInsertLoopItemWithReadOnly() {
        try {
            loopReadOnly.insertLoopItem(0);

            // Fail.
            fail("Should throw IllegalStateException.");
        } catch (IllegalStateException e) {
            // Success.
        }
    }

    /**
     * Test {@link Loop#insertLoopItem(int)} method, with bad index.
     */
    public void testInsertLoopItemWithBadIndex() {
        try {
            loopMutable.insertLoopItem(1);

            // Fail.
            fail("Should throw IllegalStateException.");
        } catch (IndexOutOfBoundsException e) {
            // Success.
        }
    }

    /**
     * Test {@link Loop#removeLoopItem(NodeList)} method.
     */
    public void testRemoveLoopItem() {
        // Add some date to the loop.
        nodeList = loopMutable.addLoopItem();
        ((Field) loopMutable.addLoopItem().getNodes()[0]).setValue("new_value");

        // Remove first item.
        assertTrue("The remove operation is incorrect.", loopMutable.removeLoopItem(nodeList));

        // Check that we have one items only.
        assertEquals("The remove operation is incorrect.", loopMutable.getLoopItems().length, 1);

        // Check that we have correct items, second.
        assertEquals("The remove operation is incorrect.",
            ((Field) loopMutable.getLoopItems()[0].getNodes()[0]).getValue(), "new_value");
    }

    /**
     * Test {@link Loop#removeLoopItem(NodeList)} method with null as argument.
     */
    public void testRemoveLoopItemWithNull() {
        try {
            // Try to remove null from loop.
            loopMutable.removeLoopItem(null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link Loop#removeLoopItem(NodeList)} method with read-only loop.
     */
    public void testRemoveLoopItemReadOnly() {
        try {
            // Try to remove from read-only loop.
            loopReadOnly.removeLoopItem(nodeList);

            // Fail.
            fail("Should throw IllegalStateException.");
        } catch (IllegalStateException e) {
            // Success.
        }
    }

    /**
     * Test {@link Loop#removeLoopItem(NodeList)} method with not exist node
     * list.
     */
    public void testRemoveLoopItemNotExist() {
        assertEquals("The remove operation should be unsuccessful", loopMutable.removeLoopItem(nodeList), false);
    }

    /**
     * Test {@link Loop#clearLoopItems()} method.
     */
    public void testClearLoopItems() {
        // Add some items to the loop.
        loopMutable.addLoopItem();
        loopMutable.addLoopItem();

        // Clear loop.
        loopMutable.clearLoopItems();

        // Check that loop is empty now.
        assertEquals("The loop should be empty.", loopMutable.getLoopItems().length, 0);
    }

    /**
     * Test {@link Loop#clearLoopItems} method with read-only loop.
     */
    public void testClearLoopItemReadOnly() {
        try {
            // Try to remove from read-only loop.
            loopReadOnly.clearLoopItems();

            // Fail.
            fail("Should throw IllegalStateException.");
        } catch (IllegalStateException e) {
            // Success.
        }
    }

    /**
     * Test {@link Loop#getLoopItems()} method.
     */
    public void testGetLoopItems() {
        // Add some items to the loop.
        loopMutable.addLoopItem();
        loopMutable.addLoopItem();

        // Check that loop have to items now.
        assertEquals("The loop should be empty.", loopMutable.getLoopItems().length, 2);
    }

    /**
     * Test {@link Loop#getDescription()} method.
     */
    public void testGetDescription() {
        assertEquals("The description property is incorrect.", loopMutable.getDescription(), "description");
    }

    /**
     * Test {@link Loop#addLoopItem()} method.
     *
     * @since 2.1
     */
    public void testAddLoopItemWithCondition() {
        // Create complex node list.
        NodeList complexNodeList = new NodeList(new Node[] {loopReadOnly, fieldReadOnly, conditionReadOnly});

        // Create complex loop.
        Loop loop = new Loop("complexLoop", complexNodeList, "This is complex loop", false);

        // Add two loop items.
        loop.addLoopItem();
        loop.addLoopItem();

        // Check loop items.
        NodeList[] nodeLists = loop.getLoopItems();
        assertEquals("The count of node items is incorrect.", nodeLists.length, 2);

        // Test first node list, it should contain three nodes
        Node[] nodes = nodeLists[0].getNodes();

        // check the three nodes
        assertEquals("Failed to get the correct nodes.", 3, nodes.length);
        assertEquals("Failed to clone the original nodes.", Loop.class, nodes[0].getClass());
        assertEquals("Failed to clone the original nodes.", Field.class, nodes[1].getClass());
        assertEquals("Failed to clone the original nodes.", Condition.class, nodes[2].getClass());
    }

    /**
     * Test {@link Loop#insertLoopItem(int)} method.
     *
     * @since 2.1
     */
    public void testInsertLoopItemWithConditionNode() {
        // Create complex node list.
        NodeList complexNodeList = new NodeList(new Node[] {loopReadOnly, fieldReadOnly, conditionReadOnly});

        // Create complex loop.
        Loop loop = new Loop("complexLoop", complexNodeList, "This is complex loop", false);

        // Insert data to the end.
        NodeList nodeLists = loop.insertLoopItem(0);

        // Configure inserted data.
        ((Loop) nodeLists.getNodes()[0]).insertLoopItem(0);
        ((Field) nodeLists.getNodes()[1]).setValue("This is marked field");
        ((Condition) nodeLists.getNodes()[2]).setValue("This is marked condition");

        // Insert data to the first position.
        nodeLists = loop.insertLoopItem(0);

        // Configure inserted data.
        ((Field) nodeLists.getNodes()[1]).setValue("This is not marked field");
        ((Condition) nodeLists.getNodes()[2]).setValue("This is not marked condition");

        // Check that all data that we insert was inserted.
        NodeList[] lists = loop.getLoopItems();

        // Check that on the first(zero) position is not marked field.
        assertEquals("The data was inserted incorrectly.", ((Field) lists[0].getNodes()[1]).getValue(),
            "This is not marked field");

        // Check that on the second(zero) position is not marked condition.
        assertEquals("The data was inserted incorrectly.", ((Condition) lists[0].getNodes()[2]).getValue(),
            "This is not marked condition");

        // Check that on the second position we have loop, where inserted
        // one element.
        assertEquals("The data was inserted incorrectly.", ((Loop) lists[1].getNodes()[0]).getLoopItems().length, 1);
    }

    /**
     * <p>
     * Tests Loop#isReadOnly() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Loop#isReadOnly() is correct.
     * </p>
     *
     * @since 2.1
     */
    public void testisReadOnly() {
        assertTrue("Failed to return the value correctly.", loopReadOnly.isReadOnly());
    }
}
