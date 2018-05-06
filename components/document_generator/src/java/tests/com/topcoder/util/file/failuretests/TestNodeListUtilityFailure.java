/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.file.fieldconfig.NodeListUtility;

import junit.framework.TestCase;

/**
 * Failure test cases for class <code>NodeListUtility </code>.
 *
 * @author Chenhong
 * @version 2.1
 */
public class TestNodeListUtilityFailure extends TestCase {

    /**
     * Class under test for void populateNodeList(NodeList, Object, boolean).
     *
     * @throws Exception
     *             to junit.
     */
    public void testPopulateNodeListNodeListObjectboolean_1() throws Exception {
        Object object = new HashMap();

        try {
            NodeListUtility.populateNodeList(null, object, false);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateNodeList(NodeList, Object, boolean)
     *
     * @throws Exception
     *             to junit.
     */
    public void testPopulateNodeListNodeListObjectboolean_2() throws Exception {
        Object object = new HashMap();

        NodeList nodeList = new NodeList(new Node[0]);

        try {
            NodeListUtility.populateNodeList(nodeList, null, false);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateNodeList(NodeList, Object, boolean).
     *
     * @throws Exception
     *             to junit.
     */
    public void testPopulateNodeListNodeListObjectboolean_3() throws Exception {
        Object object = new HashMap();

        Field field = new Field("name", "value", "des", false);
        NodeList nodeList = new NodeList(new Node[] {field});

        try {
            NodeListUtility.populateNodeList(nodeList, object, false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateNodeList(NodeList, Object, boolean).
     *
     * @throws Exception
     *             to junit.
     */
    public void testPopulateNodeListNodeListObjectboolean_4() throws Exception {
        Map object = new HashMap();
        object.put("name", "name");
        object.put("loop", "loop");

        Field field = new Field("name", "value", "des", false);

        NodeList loopNodeList = new NodeList(new Node[0]);
        Loop loop = new Loop("loop", loopNodeList, "des", false);

        NodeList nodeList = new NodeList(new Node[] {loop});

        try {
            NodeListUtility.populateNodeList(nodeList, object, true);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateNodeList(NodeList, Object, boolean).
     *
     * @throws Exception
     *             to junit.
     */
    public void testPopulateNodeListNodeListObjectboolean_5() throws Exception {
        Map object = new HashMap();
        object.put("name", "name");
        object.put("loop", "loop");

        NodeList nodeList = new NodeList(new Node[] {new Field("name", "value", "de", true)});

        try {
            NodeListUtility.populateNodeList(nodeList, object, true);
            fail("IllegalStateException is expected.");
        } catch (IllegalStateException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateNodeList(NodeList, Object, boolean).
     *
     * @throws Exception
     *             to junit.
     */
    public void testPopulateNodeListNodeListObjectboolean_6() throws Exception {
        Map object = new HashMap();
        object.put("name", "name");
        object.put("loop", "loop");
        object.put("con", "condition");

        NodeList nodeList = new NodeList(new Node[] {new Condition("con", new NodeList(new Node[0]), "de", true,
            "statement")});

        try {
            NodeListUtility.populateNodeList(nodeList, object, true);
            fail("IllegalStateException is expected.");
        } catch (IllegalStateException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateNodeList(NodeList, Object, boolean).
     *
     * @throws Exception
     *             to junit.
     */
    public void testPopulateNodeListNodeListObjectboolean_7() throws Exception {

        Object object = "abc";

        NodeList nodeList = new NodeList(new Node[] {new Condition("con", new NodeList(new Node[0]), "de", true,
            "statement")});

        try {
            NodeListUtility.populateNodeList(nodeList, object, false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateNodeList(NodeList, Object, boolean).
     *
     * @throws Exception
     *             to junit.
     */
    public void testPopulateNodeListNodeListObjectboolean_8() throws Exception {

        Object object = new MyPrivateClass();

        NodeList nodeList = new NodeList(new Node[] {new Condition("con", new NodeList(new Node[0]), "de", false,
            "statement")});

        try {
            NodeListUtility.populateNodeList(nodeList, object, false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateNodeList(NodeList, Object, boolean).
     *
     * @throws Exception
     *             to junit.
     */
    public void testPopulateNodeListNodeListObjectboolean_9() throws Exception {

        Object object = new MyPublicClass();

        NodeList nodeList = new NodeList(new Node[] {new Condition("con", new NodeList(new Node[0]), "de", false,
            "statement")});

        try {
            NodeListUtility.populateNodeList(nodeList, object, false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateLoop(Loop, List, boolean).
     */
    public void testPopulateLoopLoopListboolean_1() {
        try {
            NodeListUtility.populateLoop(null, new ArrayList(), false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateLoop(Loop, List, boolean).
     */
    public void testPopulateLoopLoopListboolean_2() {
        Field field = new Field("name", "value", "des", false);

        NodeList loopNodeList = new NodeList(new Node[0]);
        Loop loop = new Loop("loop", loopNodeList, "des", false);

        try {
            NodeListUtility.populateLoop(loop, (List) null, false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateLoop(Loop, List, boolean).
     */
    public void testPopulateLoopLoopListboolean_3() {
        Field field = new Field("name", "value", "des", false);

        NodeList loopNodeList = new NodeList(new Node[0]);
        Loop loop = new Loop("loop", loopNodeList, "des", false);

        Map object = new HashMap();
        object.put("name", "name");
        object.put("loop", "loop");

        List list = new ArrayList();
        list.add(object);
        list.add(null);

        try {
            NodeListUtility.populateLoop(loop, list, false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateLoop(Loop, List, boolean).
     */
    public void testPopulateLoopLoopListboolean_4() {
        Field field = new Field("name", "value", "des", false);

        NodeList loopNodeList = new NodeList(new Node[0]);
        Loop loop = new Loop("loop", loopNodeList, "des", true);

        Map object = new HashMap();
        object.put("name", "name");
        object.put("loop", "loop");

        List list = new ArrayList();
        list.add(object);

        try {
            NodeListUtility.populateLoop(loop, list, false);
            fail("IllegalStateException is expected.");
        } catch (IllegalStateException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateLoop(Loop, List, boolean).
     */
    public void testPopulateLoopLoopListboolean_5() {
        Field field = new Field("name", "value", "des", false);

        NodeList loopNodeList = new NodeList(new Node[0]);
        Loop loop = new Loop("loop", loopNodeList, "des", false);

        Map object = new HashMap();
        object.put("name", "name");
        object.put("loop", "loop");

        List list = new ArrayList();
        list.add(object);
        list.add(null);

        try {
            NodeListUtility.populateLoop(loop, list, false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateLoop(Loop, Object[], boolean).
     */
    public void testPopulateLoopLoopObjectArrayboolean_1() {
        try {
            NodeListUtility.populateLoop(null, new Object[0], false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateLoop(Loop, Object[], boolean).
     */
    public void testPopulateLoopLoopObjectArrayboolean_2() {
        Field field = new Field("name", "value", "des", false);

        NodeList loopNodeList = new NodeList(new Node[0]);
        Loop loop = new Loop("loop", loopNodeList, "des", false);

        try {
            NodeListUtility.populateLoop(loop, (Object[]) null, false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateLoop(Loop, Object[], boolean).
     */
    public void testPopulateLoopLoopObjectArrayboolean_3() {
        Field field = new Field("name", "value", "des", false);

        NodeList loopNodeList = new NodeList(new Node[0]);
        Loop loop = new Loop("loop", loopNodeList, "des", false);

        try {
            NodeListUtility.populateLoop(loop, new Object[100], false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Class under test for void populateLoop(Loop, Object[], boolean).
     */
    public void testPopulateLoopLoopObjectArrayboolean_4() {
        Field field = new Field("name", "value", "des", false);

        NodeList loopNodeList = new NodeList(new Node[0]);
        Loop loop = new Loop("loop", loopNodeList, "des", false);

        NodeListUtility.populateLoop(loop, new Object[100], true);
    }

    /**
     * Class under test for void populateLoop(Loop, Object[], boolean).
     */
    public void testPopulateLoopLoopObjectArrayboolean_5() {
        Field field = new Field("name", "value", "des", false);

        NodeList loopNodeList = new NodeList(new Node[0]);
        Loop loop = new Loop("loop", loopNodeList, "des", true);

        Map object = new HashMap();
        object.put("name", "name");
        object.put("loop", "loop");

        try {
            NodeListUtility.populateLoop(loop, new Object[] {object}, false);
            fail("IllegalStateException is expected.");
        } catch (IllegalStateException e) {
            // Ok.
        }
    }

    /**
     * A mocked class for testing.
     */
    private class MyPrivateClass {

        public String getCon() {
            return "Con";
        }
    }

    /**
     * A mocked class for tesitng.
     */
    public class MyPublicClass {
        public String getCon() {
            return null;
        }
    }
}