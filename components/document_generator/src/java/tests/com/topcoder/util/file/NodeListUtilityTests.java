/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.file.fieldconfig.NodeListUtility;
import com.topcoder.util.file.fieldconfig.TemplateFields;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for NodeListUtility.
 * </p>
 * @author TCSDEVELOPER
 * @version 2.1
 */
public class NodeListUtilityTests extends TestCase {
    /**
     * <p>
     * The <code>NodeList</code> instance to help testing.
     * </p>
     */
    private NodeList nodeList;

    /**
     * <p>
     * The <code>Project</code> instance to help testing.
     * </p>
     */
    private Project project;

    /**
     * <p>
     * The <code>Loop</code> instance to help testing.
     * </p>
     */
    private Loop loop;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        project = new Project();
        project.setProjectType("design");

        Component configManager = new Component();
        configManager.setComponentName("configmanager");
        configManager.setComponentLongName("configuration_manager");
        configManager.setComponentVersion("2.1.5");

        Component baseException = new Component();
        baseException.setComponentName("baseexception");
        baseException.setComponentLongName("base_exception");
        baseException.setComponentVersion("2.0");

        project.setDependencies(new Component[] {configManager, baseException });

        DocumentGenerator docGen = DocumentGeneratorFactory.getDocumentGenerator(TestHelper
            .createConfigurationObject("demo.properties", "com.topcoder.util.file"));
        Template buildTemplate = docGen.getTemplate("fileSource", "test_files/nodeListUtilityTemplate.txt");

        TemplateFields root = docGen.getFields(buildTemplate);
        Node[] nodes = root.getNodes();
        nodeList = new NodeList(nodes);

        loop = (Loop) nodes[1];
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        nodeList = null;
        project = null;
        loop = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(NodeListUtilityTests.class);
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Object,boolean) for accuracy.
     * </p>
     * <p>
     * It verifies NodeListUtility#populateNodeList(NodeList,Object,boolean) is correct.
     * </p>
     */
    public void testPopulateNodeList1() {
        NodeListUtility.populateNodeList(nodeList, project, false);

        List values = new ArrayList();
        fillNodeValues(nodeList, values);

        List expectedValues = new ArrayList();

        expectedValues.add("design");

        List childValues = new ArrayList();
        childValues.add("configmanager");
        childValues.add("2.1.5");
        childValues.add("configmanager");
        childValues.add("configuration_manager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        expectedValues.add(childValues);

        childValues = new ArrayList();
        childValues.add("baseexception");
        childValues.add("2.0");
        childValues.add("baseexception");
        childValues.add("base_exception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        expectedValues.add(childValues);

        assertEquals("Failed to populate the node list.", expectedValues, values);
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Object,boolean) for failure.
     * </p>
     * <p>
     * It tests the case when the getter method cannot be found and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateNodeList1_MethodMissing() {
        try {
            NodeListUtility.populateNodeList(nodeList, new String(), false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * This method gets the values of all the nodes of the given nodeList to the given list.
     * </p>
     * @param nodeList
     *            the <code>NodeList</code> to get all the values of the nodes
     * @param values
     *            the list instance to fill with the values of the nodes
     */
    private void fillNodeValues(NodeList nodeList, List values) {
        Node[] nodes = nodeList.getNodes();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof Field) {
                values.add(((Field) nodes[i]).getValue());
            } else if (nodes[i] instanceof Condition) {
                values.add(((Condition) nodes[i]).getValue());
            } else {
                Loop subLoop = (Loop) nodes[i];

                NodeList[] loopNodeLists = subLoop.getLoopItems();
                for (int j = 0; j < loopNodeLists.length; j++) {
                    List subValues = new ArrayList();
                    fillNodeValues(loopNodeLists[j], subValues);
                    values.add(subValues);
                }
            }
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Object,boolean) for failure.
     * </p>
     * <p>
     * It tests the case that when nodeList is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateNodeList1_NullNodeList() {
        try {
            NodeListUtility.populateNodeList(null, project, false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Object,boolean) for failure.
     * </p>
     * <p>
     * It tests the case that when data is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateNodeList1_NullData() {
        try {
            NodeListUtility.populateNodeList(nodeList, null, false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Map) for accuracy.
     * </p>
     * <p>
     * It verifies NodeListUtility#populateNodeList(NodeList,Map) is correct.
     * </p>
     */
    public void testPopulateNodeList2() {
        Map map = new HashMap();
        map.put("projectType", "design");
        map.put("dependencies", project.getDependencies());

        NodeListUtility.populateNodeList(nodeList, map);

        List values = new ArrayList();
        fillNodeValues(nodeList, values);

        List expectedValues = new ArrayList();

        expectedValues.add("design");

        List childValues = new ArrayList();
        childValues.add("configmanager");
        childValues.add("2.1.5");
        childValues.add("configmanager");
        childValues.add("configuration_manager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        expectedValues.add(childValues);

        childValues = new ArrayList();
        childValues.add("baseexception");
        childValues.add("2.0");
        childValues.add("baseexception");
        childValues.add("base_exception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        expectedValues.add(childValues);

        assertEquals("Failed to populate the node list.", expectedValues, values);
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Map) for failure.
     * </p>
     * <p>
     * It tests the case that when nodeList is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateNodeList2_NullNodeList() {
        try {
            NodeListUtility.populateNodeList(null, new HashMap());
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Map) for failure.
     * </p>
     * <p>
     * It tests the case that when data is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateNodeList2_NullData() {
        try {
            NodeListUtility.populateNodeList(nodeList, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Map,boolean) for accuracy.
     * </p>
     * <p>
     * It verifies NodeListUtility#populateNodeList(NodeList,Map,boolean) is correct.
     * </p>
     */
    public void testPopulateNodeList3() {
        Map map = new HashMap();
        map.put("projectType", "design");
        map.put("dependencies", Arrays.asList(project.getDependencies()));

        NodeListUtility.populateNodeList(nodeList, map, false);

        List values = new ArrayList();
        fillNodeValues(nodeList, values);

        List expectedValues = new ArrayList();

        expectedValues.add("design");

        List childValues = new ArrayList();
        childValues.add("configmanager");
        childValues.add("2.1.5");
        childValues.add("configmanager");
        childValues.add("configuration_manager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        expectedValues.add(childValues);

        childValues = new ArrayList();
        childValues.add("baseexception");
        childValues.add("2.0");
        childValues.add("baseexception");
        childValues.add("base_exception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        expectedValues.add(childValues);

        assertEquals("Failed to populate the node list.", expectedValues, values);
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Map,boolean) for failure.
     * </p>
     * <p>
     * It tests the case when the entry is missing and expects IllegalArgumentException
     * </p>
     */
    public void testPopulateNodeList3_MissingEntry() {
        Map map = new HashMap();
        map.put("dependencies", project.getDependencies());

        try {
            NodeListUtility.populateNodeList(nodeList, map, false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Map,boolean) for failure.
     * </p>
     * <p>
     * It tests the case when the value for a loop is not List or Object[] and expects
     * IllegalArgumentException.
     * </p>
     */
    public void testPopulateNodeList3_InvalidValue() {
        Map map = new HashMap();
        map.put("projectType", "design");
        map.put("dependencies", "hello");

        try {
            NodeListUtility.populateNodeList(nodeList, map, false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Map,boolean) for failure.
     * </p>
     * <p>
     * It tests the case that when nodeList is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateNodeList3_NullNodeList() {
        try {
            NodeListUtility.populateNodeList(null, new HashMap(), false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Map,boolean) for failure.
     * </p>
     * <p>
     * It tests the case that when data is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateNodeList3_NullData() {
        try {
            NodeListUtility.populateNodeList(nodeList, null, false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Object) for accuracy.
     * </p>
     * <p>
     * It verifies NodeListUtility#populateNodeList(NodeList,Object) is correct.
     * </p>
     */
    public void testPopulateNodeList4() {
        NodeListUtility.populateNodeList(nodeList, project);

        List values = new ArrayList();
        fillNodeValues(nodeList, values);

        List expectedValues = new ArrayList();

        expectedValues.add("design");

        List childValues = new ArrayList();
        childValues.add("configmanager");
        childValues.add("2.1.5");
        childValues.add("configmanager");
        childValues.add("configuration_manager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        expectedValues.add(childValues);

        childValues = new ArrayList();
        childValues.add("baseexception");
        childValues.add("2.0");
        childValues.add("baseexception");
        childValues.add("base_exception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        expectedValues.add(childValues);

        assertEquals("Failed to populate the node list.", expectedValues, values);
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Object) for failure.
     * </p>
     * <p>
     * It tests the case that when nodeList is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateNodeList4_NullNodeList() {
        try {
            NodeListUtility.populateNodeList(null, project);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateNodeList(NodeList,Object) for failure.
     * </p>
     * <p>
     * It tests the case that when data is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateNodeList4_NullData() {
        try {
            NodeListUtility.populateNodeList(nodeList, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,Object[]) for accuracy.
     * </p>
     * <p>
     * It verifies NodeListUtility#populateLoop(Loop,Object[]) is correct.
     * </p>
     */
    public void testPopulateLoop1() {
        NodeListUtility.populateLoop(loop, project.getDependencies());

        List values = new ArrayList();
        fillNodeValues(new NodeList(new Node[] {loop }), values);

        List expectedValues = new ArrayList();

        List childValues = new ArrayList();
        childValues.add("configmanager");
        childValues.add("2.1.5");
        childValues.add("configmanager");
        childValues.add("configuration_manager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        expectedValues.add(childValues);

        childValues = new ArrayList();
        childValues.add("baseexception");
        childValues.add("2.0");
        childValues.add("baseexception");
        childValues.add("base_exception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        expectedValues.add(childValues);

        assertEquals("Failed to populate the node list.", expectedValues, values);
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,Object[]) for failure.
     * </p>
     * <p>
     * It tests the case that when loop is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateLoop1_NullLoop() {
        try {
            NodeListUtility.populateLoop(null, project.getDependencies());
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,Object[]) for failure.
     * </p>
     * <p>
     * It tests the case that when data is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateLoop1_NullData() {
        try {
            NodeListUtility.populateLoop(loop, (Object[]) null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,Object[],boolean) for accuracy.
     * </p>
     * <p>
     * It verifies NodeListUtility#populateLoop(Loop,Object[],boolean) is correct.
     * </p>
     */
    public void testPopulateLoop2() {
        NodeListUtility.populateLoop(loop, project.getDependencies(), false);

        List values = new ArrayList();
        fillNodeValues(new NodeList(new Node[] {loop }), values);

        List expectedValues = new ArrayList();

        List childValues = new ArrayList();
        childValues.add("configmanager");
        childValues.add("2.1.5");
        childValues.add("configmanager");
        childValues.add("configuration_manager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        expectedValues.add(childValues);

        childValues = new ArrayList();
        childValues.add("baseexception");
        childValues.add("2.0");
        childValues.add("baseexception");
        childValues.add("base_exception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        expectedValues.add(childValues);

        assertEquals("Failed to populate the node list.", expectedValues, values);
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,Object[],boolean) for failure.
     * </p>
     * <p>
     * It tests the case that when loop is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateLoop2_NullLoop() {
        try {
            NodeListUtility.populateLoop(null, project.getDependencies(), false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,Object[],boolean) for failure.
     * </p>
     * <p>
     * It tests the case that when data is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateLoop2_NullData() {
        try {
            NodeListUtility.populateLoop(loop, (Object[]) null, false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,List) for accuracy.
     * </p>
     * <p>
     * It verifies NodeListUtility#populateLoop(Loop,List) is correct.
     * </p>
     */
    public void testPopulateLoop3() {
        NodeListUtility.populateLoop(loop, Arrays.asList(project.getDependencies()));

        List values = new ArrayList();
        fillNodeValues(new NodeList(new Node[] {loop }), values);

        List expectedValues = new ArrayList();

        List childValues = new ArrayList();
        childValues.add("configmanager");
        childValues.add("2.1.5");
        childValues.add("configmanager");
        childValues.add("configuration_manager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        expectedValues.add(childValues);

        childValues = new ArrayList();
        childValues.add("baseexception");
        childValues.add("2.0");
        childValues.add("baseexception");
        childValues.add("base_exception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        expectedValues.add(childValues);

        assertEquals("Failed to populate the node list.", expectedValues, values);
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,List) for failure.
     * </p>
     * <p>
     * It tests the case that when loop is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateLoop3_NullLoop() {
        try {
            NodeListUtility.populateLoop(null, Arrays.asList(project.getDependencies()));
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,List) for failure.
     * </p>
     * <p>
     * It tests the case that when data is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateLoop3_NullData() {
        try {
            NodeListUtility.populateLoop(loop, (List) null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,List,boolean) for accuracy.
     * </p>
     * <p>
     * It verifies NodeListUtility#populateLoop(Loop,List,boolean) is correct.
     * </p>
     */
    public void testPopulateLoop4() {
        NodeListUtility.populateLoop(loop, Arrays.asList(project.getDependencies()), false);

        List values = new ArrayList();
        fillNodeValues(new NodeList(new Node[] {loop }), values);

        List expectedValues = new ArrayList();

        List childValues = new ArrayList();
        childValues.add("configmanager");
        childValues.add("2.1.5");
        childValues.add("configmanager");
        childValues.add("configuration_manager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        childValues.add("configmanager");
        expectedValues.add(childValues);

        childValues = new ArrayList();
        childValues.add("baseexception");
        childValues.add("2.0");
        childValues.add("baseexception");
        childValues.add("base_exception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        childValues.add("baseexception");
        expectedValues.add(childValues);

        assertEquals("Failed to populate the node list.", expectedValues, values);
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,List,boolean) for failure.
     * </p>
     * <p>
     * It tests the case that when loop is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateLoop4_NullLoop() {
        try {
            NodeListUtility.populateLoop(null, Arrays.asList(project.getDependencies()), false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,List,boolean) for failure.
     * </p>
     * <p>
     * It tests the case that when data is null and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateLoop4_NullData() {
        try {
            NodeListUtility.populateLoop(loop, (List) null, false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests NodeListUtility#populateLoop(Loop,List,boolean) for failure.
     * </p>
     * <p>
     * It tests the case that when data contains null element and expects IllegalArgumentException.
     * </p>
     */
    public void testPopulateLoop4_NullInData() {
        List data = new ArrayList();
        data.add(null);

        try {
            NodeListUtility.populateLoop(loop, data, false);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }
}