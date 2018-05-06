/**
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.file.accuracytests;

import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.NodeList;

import junit.framework.TestCase;

/**
 * <p>Test the Loop class</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class LoopAccuracyTests extends TestCase {
    /**
     * Loop instance for test..
     */
    Loop loop = null;

    /** 
     * Initialize loop instance
     */
    public void setUp() {
        Node[] nodes = new Node[1];
            
        nodes[0] = new Field("testName2", "testVal2", "testDesc2", true);
        loop = new Loop("innerElement", new NodeList(nodes), "loopDesc", false);
        nodes = new Node[2];
        nodes[0] = new Field("testName", "testVal", "testDesc", false);
        nodes[1] = loop;
        loop = new Loop("testElement", new NodeList(nodes), "loopDesc2", false);
    }

    /**
     * Tests constructor.
     */
    public void testConstructor() {
        assertEquals("testElement", loop.getLoopElement());
        assertEquals(2, loop.getSampleLoopItem().getNodes().length);
        assertTrue(loop.getSampleLoopItem().getNodes()[0] instanceof Field);
        assertTrue(loop.getSampleLoopItem().getNodes()[1] instanceof Loop);
        assertEquals("testName", ((Field) (loop.getSampleLoopItem().getNodes()[0])).getName());
        assertEquals("innerElement", ((Loop) (loop.getSampleLoopItem().getNodes()[1])).getLoopElement());
        assertEquals(0, loop.getLoopItems().length);
        assertFalse("test read only", loop.isReadOnly());
    }

    /**
     * Tests addLoopItem method.
     */
    public void testAddLoopItem() {
        NodeList list = loop.addLoopItem();

        assertTrue(list.getNodes()[0] instanceof Field);
        assertTrue(list.getNodes()[1] instanceof Loop);
        ((Field) (list.getNodes()[0])).setValue("newVal");
        assertEquals(1, loop.getLoopItems().length);
        assertEquals("newVal", ((Field) (loop.getLoopItems()[0].getNodes()[0])).getValue());
    }

    /**
     * Tests insertLoopItem method.
     */
    public void testInsertLoopItem() {
        NodeList list = loop.addLoopItem();

        assertTrue(list.getNodes()[0] instanceof Field);
        assertTrue(list.getNodes()[1] instanceof Loop);
        ((Field) (list.getNodes()[0])).setValue("newVal");
        assertEquals(1, loop.getLoopItems().length);
        assertEquals("newVal", ((Field)(loop.getLoopItems()[0].getNodes()[0])).getValue());

        list = loop.insertLoopItem(0);

        assertTrue(list.getNodes()[0] instanceof Field);
        assertTrue(list.getNodes()[1] instanceof Loop);
        ((Field) (list.getNodes()[0])).setValue("insertVal");
        assertEquals(2, loop.getLoopItems().length);
        assertEquals("insertVal", ((Field) (loop.getLoopItems()[0].getNodes()[0])).getValue());
    }

    /**
     * Tests removeLoopItem method.
     */
    public void testRemoveLoopItem() {
        NodeList list = loop.addLoopItem();

        ((Field) (list.getNodes()[0])).setValue("newVal");
        assertEquals("newVal", ((Field) (loop.getLoopItems()[0].getNodes()[0])).getValue());

        list = loop.addLoopItem();
        ((Field) (list.getNodes()[0])).setValue("newVal2");
        assertEquals("newVal2", ((Field) (loop.getLoopItems()[1].getNodes()[0])).getValue());
        assertEquals(2, loop.getLoopItems().length);

        assertTrue(loop.removeLoopItem(list));
        assertEquals(1, loop.getLoopItems().length);
        assertTrue(!loop.removeLoopItem(list));
        assertEquals(1, loop.getLoopItems().length);
    }

    /**
     * Tests clearLoopItems method.
     */
    public void testClearLoopItems() {
        loop.addLoopItem();

        assertEquals(1, loop.getLoopItems().length);
        loop.clearLoopItems();
        assertEquals(0, loop.getLoopItems().length);
    }
    


    /**
     * <p>
     * Tests {@link Condition#isReadOnly()} for accuracy.
     * </p>
     */
    public void testIsReadOnlyAccuracy() {
        assertFalse("isReadOnly failed", loop.isReadOnly());
    }
}
