/*
 * Copyright (C) 2007-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.SynchronizedConfigurationObject;
import com.topcoder.configuration.TemplateConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy test of the class SynchronizedConfigurationObject.
 * </p>
 *
 * Changes in 1.1 : add test cases for added methods.
 *
 * @author KKD, TCSDEVLOPER
 *
 * @version 1.1
 * @since 1.0
 */
public class SynchronizedConfigurationObjectAccuracyTests extends TestCase {
    /**
     * The instance for test.
     */
    private SynchronizedConfigurationObject synchronizedObject = null;
    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject root = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node1 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node2 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node3 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node4 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node5 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node6 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node7 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node8 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node9 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node10 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node11 = null;

    /**
     * The instance for test.
     */
    protected DefaultConfigurationObject node12 = null;

    /**
     * SetUp for accuracy test of the DefaultConfigurationObject, create the configuration tree for accuacy test. The
     * tree as following: root / \ node1 node2 / \ / \ node3 node4 node5 \ / node6 node8 \ / node7 node9 / \ / \ node12
     * node10 node11
     *
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        root = new DefaultConfigurationObject("root");
        node1 = new DefaultConfigurationObject("abc#abc");
        node2 = new DefaultConfigurationObject("zzc#abz");
        node3 = new DefaultConfigurationObject("pcpcpc");
        node4 = new DefaultConfigurationObject("aapcbb");
        node5 = new DefaultConfigurationObject("pckk");
        node6 = new DefaultConfigurationObject("node6");
        node7 = new DefaultConfigurationObject("node7");
        node8 = new DefaultConfigurationObject("node8");
        node9 = new DefaultConfigurationObject("node9");
        node10 = new DefaultConfigurationObject("aa");
        node11 = new DefaultConfigurationObject("bb");
        node12 = new DefaultConfigurationObject("ccc");
        root.addChild(node1);
        root.addChild(node2);
        node1.addChild(node3);
        node1.addChild(node4);
        node2.addChild(node4);
        node2.addChild(node5);
        node3.addChild(node6);
        node5.addChild(node8);
        node6.addChild(node7);
        node8.addChild(node9);
        node7.addChild(node12);
        node7.addChild(node10);
        node9.addChild(node10);
        node9.addChild(node11);
        synchronizedObject = new SynchronizedConfigurationObject(root);
    }

    /**
     * The test of the constructor for accuracy.
     *
     */
    public void test_accuracy_ctor() {
        assertNotNull("Failed to create DefaultConfigurationObject with name.", new DefaultConfigurationObject("z"));
    }

    /**
     * Test inheritance of DefaultConfigurationObject, it is implements ConfigurationObject interface.
     *
     */
    public void test_inheritance() {
        assertTrue("DefaultConfigurationObject should extends TemplateConfigurationObject.",
            new DefaultConfigurationObject("z") instanceof TemplateConfigurationObject);
    }

    /**
     * The test of the method getName for accuracy.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getName() throws Exception {
        assertEquals("The name can not be retrieved successfully.", "root", synchronizedObject.getName());
    }

    /**
     * The test of the method setPropertyValue for accuracy, the value can be null, and the null is set in to the
     * property.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_setPropertyValue1() throws Exception {
        Object[] values = synchronizedObject.setPropertyValue("key", null);
        assertNull("The init value is not set, null should be returned.", values);

        // the value null can be set into the root, set again and should return the old value
        values = synchronizedObject.setPropertyValue("key", null);
        assertNotNull("The result is unexpected.", values);
        assertNull("The first value should be null.", values[0]);
    }

    /**
     * The test of the method setPropertyValue for accuracy, if set twice, the old value is overriden.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_setPropertyValue2() throws Exception {
        synchronizedObject.setPropertyValue("key", "oldvalue");
        synchronizedObject.setPropertyValue("key", "newvalue");
        assertNotNull("The result is unexpected.", synchronizedObject.getPropertyValue("key"));
        assertEquals("The old value should be overrideb.", "newvalue", synchronizedObject.getPropertyValue("key"));
    }

    /**
     * The test of the method setPropertyValue for accuracy, the value can be null, if null, empty array is used.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_setPropertyValues1() throws Exception {
        Object[] values = synchronizedObject.setPropertyValues("key", null);
        assertNull("The init value is not set, null should be returned.", values);

        // null should be used as empty property
        values = synchronizedObject.getPropertyValues("key");
        assertEquals("Empty value should be set in the property.", 0, values.length);
    }

    /**
     * The test of the method setPropertyValue for accuracy, the value array should be set into the property.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_setPropertyValues2() throws Exception {
        Object[] values = synchronizedObject.setPropertyValues("key", new Object[] { "v1", "v2", "v3", "v4" });
        values = synchronizedObject.getPropertyValues("key");
        assertEquals("Empty value should be set in the property.", 4, values.length);
        // the order should be kept
        assertEquals("The order of the values is not kept.", "v1", values[0]);
        assertEquals("The order of the values is not kept.", "v2", values[1]);
        assertEquals("The order of the values is not kept.", "v3", values[2]);
        assertEquals("The order of the values is not kept.", "v4", values[3]);
    }

    /**
     * The test of the method containsProperty for accuracy, the property is not contains initally, after set a null
     * values, the property is also considered as contains.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_containsProperty1() throws Exception {
        assertFalse("The property key should not be contained.", synchronizedObject.containsProperty("key"));
        synchronizedObject.setPropertyValues("key", null);
        assertTrue("The property key should be contained.", synchronizedObject.containsProperty("key"));

        synchronizedObject.setPropertyValues("newkey", new Object[] { "v1", "v2" });
        assertTrue("The property newkey should be contained.", synchronizedObject.containsProperty("newkey"));
    }

    /**
     * The test of the method containsProperty for accuracy
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_containsProperty2() throws Exception {
        assertFalse("The property key should not be contained.", synchronizedObject.containsProperty("key"));
        synchronizedObject.setPropertyValue("key", null);
        assertTrue("The property key should be contained.", synchronizedObject.containsProperty("key"));

        synchronizedObject.setPropertyValue("newkey", "value");
        assertTrue("The property newkey should be contained.", synchronizedObject.containsProperty("newkey"));
    }

    /**
     * The test of the method getPropertyValue for accuracy, no value is set, should return null.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValue1() throws Exception {
        assertNull("should return null.", synchronizedObject.getPropertyValue("key"));
    }

    /**
     * The test of the method getPropertyValue for accuracy, null is set, should return null.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValue2() throws Exception {
        synchronizedObject.setPropertyValue("key", null);
        assertNull("should return null.", synchronizedObject.getPropertyValue("key"));
    }

    /**
     * The test of the method getPropertyValue for accuracy, empty value is set, should return null.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValue3() throws Exception {
        synchronizedObject.setPropertyValues("key", null);
        assertNull("should return null.", synchronizedObject.getPropertyValue("key"));
    }

    /**
     * The test of the method getPropertyValue for accuracy, first value of the array is retrieved.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValue4() throws Exception {
        synchronizedObject.setPropertyValues("key", new Object[] { new Integer(5), "5" });
        assertEquals("The first value should be retrieved.", new Integer(5), synchronizedObject.getPropertyValue("key"));
    }

    /**
     * The test of the method getPropertyValues for accuracy, no value is set, should return null.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValues1() throws Exception {
        assertNull("should return null.", synchronizedObject.getPropertyValues("key"));
    }

    /**
     * The test of the method getPropertyValues for accuracy, null is set, but a array contains null should return.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValues2() throws Exception {
        synchronizedObject.setPropertyValue("key", null);
        assertNotNull("should not return null.", synchronizedObject.getPropertyValues("key"));
        assertNull("The array should contains null.", synchronizedObject.getPropertyValues("key")[0]);
    }

    /**
     * The test of the method getPropertyValues for accuracy, empty value is set, should return empty array.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValues3() throws Exception {
        synchronizedObject.setPropertyValues("key", null);
        assertEquals("Empty array should be returned.", 0, synchronizedObject.getPropertyValues("key").length);
    }

    /**
     * The test of the method getPropertyValues for accuracy, all the values of the array are retrieved.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValues4() throws Exception {
        synchronizedObject.setPropertyValues("key", new Object[] { new Integer(5), "5" });
        assertEquals("All the values should be retrieved.", 2, synchronizedObject.getPropertyValues("key").length);
    }

    /**
     * The test of the method removeProperty for accuracy.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_removeProperty1() throws Exception {
        Object[] values = synchronizedObject.removeProperty("key");
        assertNull("The old value is null, should return null.", values);
    }

    /**
     * The test of the method removeProperty for accuracy, the old value is returned.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_removeProperty2() throws Exception {
        synchronizedObject.setPropertyValues("key", new Object[] {});
        Object[] values = synchronizedObject.removeProperty("key");
        assertNotNull("The old value is not null, should not return null.", values);
    }

    /**
     * The test of the method getAllPropertyKeys for accuracy, no key exist, empty array should be returned.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyKeys() throws Exception {
        String[] keys = synchronizedObject.getAllPropertyKeys();
        assertEquals("The array should be empty.", 0, keys.length);
    }

    /**
     * The test of the method getAllPropertyKeys for accuracy, keys exist.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyKeys2() throws Exception {
        synchronizedObject.setPropertyValue("key1", "1");
        synchronizedObject.setPropertyValue("key2", "2");
        synchronizedObject.setPropertyValue("key3", "3");
        String[] keys = synchronizedObject.getAllPropertyKeys();
        assertEquals("The array should be not empty.", 3, keys.length);
    }

    /**
     * The test of the method getPropertyKeys for accuracy, all the keys satisfy the pattern is retrieved.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyKeys1() throws Exception {
        synchronizedObject.setPropertyValue("a", "1");
        synchronizedObject.setPropertyValue("b", "2");
        synchronizedObject.setPropertyValue("c", "3");
        String[] keys = synchronizedObject.getPropertyKeys("[abef]");
        assertEquals("The array should be not empty, length should be 2.", 2, keys.length);
    }

    /**
     * The test of the method addChild for accuracy, the child is added.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_addChild1() throws Exception {
        root = new DefaultConfigurationObject("root");
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        synchronizedObject.addChild(child);
    }

    /**
     * The test of the method addChild for accuracy, the child is added with the same name, the old one is overriden.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_addChild2() throws Exception {
        root = new DefaultConfigurationObject("root");
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        synchronizedObject.addChild(child);
        DefaultConfigurationObject newChild = new DefaultConfigurationObject("child");
        synchronizedObject.addChild(newChild);
        assertFalse("The child should be overriden.", child == synchronizedObject.getChild("child"));
        assertTrue("The new child should be got.", newChild == synchronizedObject.getChild("child"));
    }

    /**
     * The test of the method containsChild
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_containsChild() throws Exception {
        root = new DefaultConfigurationObject("root");
        assertFalse("does not contain.", synchronizedObject.containsChild("child"));
        synchronizedObject.addChild(new DefaultConfigurationObject("child"));
        // now contains
        assertTrue("now contains.", synchronizedObject.containsChild("child"));
    }

    /**
     * The test of the method getChild for accuracy, the child is added with the same name, the old one is overriden.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getChild1() throws Exception {
        root = new DefaultConfigurationObject("root");
        assertNull("The root does not contain child, return null.", synchronizedObject.getChild("child"));
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        synchronizedObject.addChild(child);
        assertTrue("The child with the name is got.", child == synchronizedObject.getChild("child"));
    }

    /**
     * The test of the method removeChild for accuracy, the child is removeed .
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_removedChild1() throws Exception {
        root = new DefaultConfigurationObject("root");
        assertNull("The root does not contain child, return null.", synchronizedObject.getChild("child"));
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        synchronizedObject.addChild(child);
        synchronizedObject.removeChild("child");
        assertNull("The root does not contain child, it is removed.", synchronizedObject.getChild("child"));
    }

    /**
     * The test of the method clearChildren for accuracy, the child is cleared .
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_clearChildren() throws Exception {
        root = new DefaultConfigurationObject("root");
        assertNull("The root does not contain child, return null.", synchronizedObject.getChild("child"));
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        synchronizedObject.addChild(child);
        synchronizedObject.clearChildren();
        assertNull("The root does not contain child, it is cleared.", synchronizedObject.getChild("child"));
    }

    /**
     * The test of the method getAllChildrenNames for accuracy.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getAllChildrenNames() throws Exception {
        root = new DefaultConfigurationObject("root");
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("child1");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("child2");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("child3");
        root.addChild(child1);
        root.addChild(child2);
        root.addChild(child3);
        synchronizedObject = new SynchronizedConfigurationObject(root);
        assertEquals("3 names should be got.", 3, synchronizedObject.getAllChildrenNames().length);
    }

    /**
     * The test of the method getAllChildren for accuracy.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getAllChildren() throws Exception {
        root = new DefaultConfigurationObject("root");
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("child1");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("child2");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("child3");
        root.addChild(child1);
        root.addChild(child2);
        root.addChild(child3);
        synchronizedObject = new SynchronizedConfigurationObject(root);
        assertEquals("3 children should be got.", 3, synchronizedObject.getAllChildren().length);
    }

    /**
     * The test of the method getChildren for accuracy, all the children whose name satisfy the pattern is returned.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getChildren() throws Exception {
        root = new DefaultConfigurationObject("root");
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("a");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("b");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("c");
        synchronizedObject.addChild(child1);
        synchronizedObject.addChild(child2);
        synchronizedObject.addChild(child3);
        assertEquals("2 children should be got.", 2, synchronizedObject.getChildren("[bcz]").length);
    }

    /**
     * The test of the method getAllDescendants for accuracy, the configuration of the DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getAllDescendants() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.getAllDescendants();
        assertEquals("root have 12 descendants.", 12, descendants.length);
        descendants = node6.getAllDescendants();
        assertEquals("node6 have 3 descendant.", 3, descendants.length);
        descendants = node7.getAllDescendants();
        assertEquals("node7 have 2 descendant.", 2, descendants.length);
        descendants = node10.getAllDescendants();
        assertEquals("node10 have 0 descendant.", 0, descendants.length);
        descendants = node1.getAllDescendants();
        assertEquals("node1 have 6 descendant.", 6, descendants.length);
        descendants = node5.getAllDescendants();
        assertEquals("node5 have 4 descendant.", 4, descendants.length);
    }

    /**
     * The test of the method getDescendants for accuracy, the configuration of the DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getDescendants1() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.getDescendants("not such path");
        assertEquals("no descendants exists with the pattern, empty array is expected.", 0, descendants.length);
    }

    /**
     * The test of the method getDescendants for accuracy, values are retrieved. the configuration of the DAG see the
     * doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getDescendants2() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.getDescendants("[ab][ab]");
        assertEquals("2 descendants exists with the pattern.", 2, descendants.length);
    }

    /**
     * The test of the method findDescendants for accuracy, the configuration of the DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_findDescendants1() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.findDescendants("not such path");
        assertEquals("no descendants exists with the pattern, empty array is expected.", 0, descendants.length);
    }

    /**
     * The test of the method findDescendants for accuracy, the value with the path exists. the configuration of the DAG
     * see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_findDescendants2() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.findDescendants("*c#ab*/*pc??");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = synchronizedObject.findDescendants("*/*/*/*/*");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = synchronizedObject.findDescendants("*/*/*/*/?");
        assertEquals("0 descendants exists with the path.", 0, descendants.length);
    }

    /**
     * The test of the method findDescendants for accuracy, the slashes will be trimmed. the configuration of the DAG
     * see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_findDescendants3() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.findDescendants("// \\/*c#ab*/*pc??  \\/// /");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = synchronizedObject.findDescendants("// // \\ \\*/*/*/*/*// // \\ \\");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = synchronizedObject.findDescendants("// // \\ \\*/*/*/*/?    \n//");
        assertEquals("0 descendants exists with the path.", 0, descendants.length);
    }

    /**
     * The test of the method findDescendants for accuracy, Continuous slashes will be considered as one.. the
     * configuration of the DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_findDescendants4() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.findDescendants("// \\/*c#ab*\\/\\/*pc??  \\/// /");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = synchronizedObject.findDescendants("// // \\ \\\\*//\\/*/*\\/*//*// // \\ \\");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = synchronizedObject.findDescendants("// // \\ \\*/*/*/*/?    \n//");
        assertEquals("0 descendants exists with the path.", 0, descendants.length);
    }

    /**
     * The test of the method deleteDescendants for accuracy, the configuration of the DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_deleteDescendants1() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.deleteDescendants("not such path");
        assertEquals("no descendants exists with the pattern, empty array is expected.", 0, descendants.length);
    }

    /**
     * The test of the method deleteDescendants for accuracy, the value with the path exists. the configuration of the
     * DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_deleteDescendants2() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.deleteDescendants("*c#ab*/*pc??");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = synchronizedObject.getAllDescendants();
        assertEquals("2 descendants exists for root now.", 2, descendants.length);
        descendants = node1.getAllDescendants();
        assertEquals("0 descendants exists for node1 now.", 0, descendants.length);
    }

    /**
     * The test of the method deleteDescendants for accuracy, the value with the path exists. the configuration of the
     * DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_deleteDescendants3() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.deleteDescendants("// \\/*c#ab*  \\/// /");
        assertEquals("2 descendants exists with the path.", 2, descendants.length);
        descendants = synchronizedObject.getAllDescendants();
        assertEquals("0 descendants exists for root now.", 0, descendants.length);
        descendants = node1.getAllDescendants();
        assertEquals("6 descendants exists for node1 now.", 6, descendants.length);
    }

    /**
     * The test of the method deleteDescendants for accuracy, the value with the path exists. the configuration of the
     * DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_deleteDescendants4() throws Exception {
        ConfigurationObject[] descendants = synchronizedObject.deleteDescendants("// // \\ \\*/*/*/*/*    \n//");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = synchronizedObject.getAllDescendants();
        assertEquals("9 descendants exists for root now.", 9, descendants.length);
        descendants = node1.getAllDescendants();
        assertEquals("4 descendants exists for node1 now.", 4, descendants.length);
    }

    /**
     * The test of the method setPropertyValue for accuracy, all the node alone the path set the property value.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_setPropertyValue_withPath() throws Exception {
        root.setPropertyValue("*c#ab*/*pc??", "key", new Integer(5));
        assertTrue("The property value should be set into the node.", node3.containsProperty("key"));
        assertTrue("The property value should be set into the node.", node4.containsProperty("key"));
        assertTrue("The property value should be set into the node.", node5.containsProperty("key"));
        assertEquals("The property value should be set into the node.", new Integer(5), node3.getPropertyValue("key"));
        assertEquals("The property value should be set into the node.", new Integer(5), node4.getPropertyValue("key"));
        assertEquals("The property value should be set into the node.", new Integer(5), node5.getPropertyValue("key"));
    }

    /**
     * The test of the method setPropertyValues for accuracy, all the node alone the path set the property value.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_setPropertyValues() throws Exception {
        root.setPropertyValues("*c#ab*/*pc??", "key", new Object[] { new Integer(5) });
        assertTrue("The property value should be set into the node.", node3.containsProperty("key"));
        assertTrue("The property value should be set into the node.", node4.containsProperty("key"));
        assertTrue("The property value should be set into the node.", node5.containsProperty("key"));
        assertEquals("The property value should be set into the node.", new Integer(5), node3.getPropertyValue("key"));
        assertEquals("The property value should be set into the node.", new Integer(5), node4.getPropertyValue("key"));
        assertEquals("The property value should be set into the node.", new Integer(5), node5.getPropertyValue("key"));
    }

    /**
     * The test of the method removeProperty for accuracy, all the node alone the path remove the property value.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_removePropertyValue() throws Exception {
        root.setPropertyValue("*c#ab*/*pc??", "key", new Integer(5));
        root.removeProperty("*c#ab*/*pc??", "key");
        assertFalse("The property value should be removed.", node3.containsProperty("key"));
        assertFalse("The property value should be removed.", node4.containsProperty("key"));
        assertFalse("The property value should be removed.", node5.containsProperty("key"));
    }

    /**
     * The test of the method clearProperties for accuracy, all the node alone the path clear the property value.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_clearProperties() throws Exception {
        root.setPropertyValue("*c#ab*/*pc??", "key", new Integer(5));
        root.clearProperties("*c#ab*/*pc??");
        assertFalse("The property value should be removed.", node3.containsProperty("key"));
        assertFalse("The property value should be removed.", node4.containsProperty("key"));
        assertFalse("The property value should be removed.", node5.containsProperty("key"));
    }

    /**
     * The test of the method addChild for accuracy, all the node alone the path add the child.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_addChild() throws Exception {
        root.addChild("*c#ab*/*pc??", new DefaultConfigurationObject("child"));
        assertTrue("The child should be set into the node.", node3.containsChild("child"));
        assertTrue("The child should be set into the node.", node4.containsChild("child"));
        assertTrue("The child should be set into the node.", node5.containsChild("child"));
    }

    /**
     * The test of the method removeChild for accuracy, all the node alone the path remove the child.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_removeChild() throws Exception {
        root.addChild("*c#ab*/*pc??", new DefaultConfigurationObject("child"));
        root.removeChild("*c#ab*/*pc??", "child");
        assertFalse("The child should be removed.", node3.containsChild("child"));
        assertFalse("The child should be removed.", node4.containsChild("child"));
        assertFalse("The child should be removed.", node5.containsChild("child"));
    }

    /**
     * The test of the method clearChildren for accuracy, all the node alone the path clear the child.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_clearChildren_withPath() throws Exception {
        root.addChild("*c#ab*/*pc??", new DefaultConfigurationObject("child"));
        root.clearChildren("*c#ab*/*pc??");
        assertFalse("The child should be removed.", node3.containsChild("child"));
        assertFalse("The child should be removed.", node4.containsChild("child"));
        assertFalse("The child should be removed.", node5.containsChild("child"));
    }

    /**
     * The test of the method processDescendants for accuracy.
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void test_accuracy_processDescendants() throws Exception {
        ProcessorImpl processor = new ProcessorImpl();
        root.processDescendants("*c#ab*/*pc??", processor);
        Set processedNodes = processor.getAllProcessedNodes();
        assertEquals("3 value should be processed.", 3, processedNodes.size());
        assertTrue("set contains the name of the node processed.", processedNodes.contains("pcpcpc"));
        assertTrue("set contains the name of the node processed.", processedNodes.contains("aapcbb"));
        assertTrue("set contains the name of the node processed.", processedNodes.contains("pckk"));
    }

    /**
     * The test of the method Clone for accuracy.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clone_accuracy() throws Exception {
        SynchronizedConfigurationObject clone = (SynchronizedConfigurationObject) synchronizedObject.clone();
        assertTrue("clone should not be the same reference.", clone != synchronizedObject);
        assertTrue("clone inner object should not be the same reference.", getPrivateField(
            SynchronizedConfigurationObject.class, clone, "configObj") != getPrivateField(
            SynchronizedConfigurationObject.class, synchronizedObject, "configObj"));
    }

    /**
     * Gets the value of a private field in the given class. The field has the given name. The value is retrieved from
     * the given instance. If the instance is null, the field is a static field. If any error occurs, null is returned.
     *
     * @param type
     *            the class which the private field belongs to
     * @param instance
     *            the instance which the private field belongs to
     * @param name
     *            the name of the private field to be retrieved
     * @return the value of the private field
     */
    static Object getPrivateField(Class<?> type, Object instance, String name) {
        Field field = null;
        Object obj = null;
        try {
            // get the reflection of the field
            field = type.getDeclaredField(name);

            // set the field accessible.
            field.setAccessible(true);

            // get the value
            obj = field.get(instance);
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        } finally {
            if (field != null) {
                // reset the accessibility
                field.setAccessible(false);
            }
        }
        return obj;
    }

    /**
     * The test of the method getIntegerProperty for accuracy, the int value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getIntegerProperty1() throws Exception {
        synchronizedObject.setPropertyValue("key", "111");

        assertEquals("The getIntegerProperty should return the int value.", 111, synchronizedObject.getIntegerProperty(
            "key", true).longValue());
    }

    /**
     * The test of the method getIntegerProperty for accuracy, the int value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getIntegerProperty2() throws Exception {
        synchronizedObject.setPropertyValue("key", 111);
        assertEquals("The getIntegerProperty should return the int value.", 111, synchronizedObject.getIntegerProperty(
            "key", true).longValue());
    }

    /**
     * The test of the method getIntegerProperty for accuracy, the int value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getIntegerProperty3() throws Exception {
        synchronizedObject.setPropertyValue("key", String.class);
        assertEquals("The getIntegerProperty should return the int value.", null, synchronizedObject
            .getIntegerProperty("key2", false));
    }

    /**
     * The test of the method getLongProperty for accuracy, the long value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getLongProperty1() throws Exception {
        synchronizedObject.setPropertyValue("key", "111");

        assertEquals("The getLongProperty should return the long value.", 111, synchronizedObject.getLongProperty(
            "key", true).longValue());
    }

    /**
     * The test of the method getLongProperty for accuracy, the v value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getLongProperty2() throws Exception {
        synchronizedObject.setPropertyValue("key", 111L);
        assertEquals("The getLongProperty should return the long value.", 111, synchronizedObject.getLongProperty(
            "key", true).longValue());
    }

    /**
     * The test of the method getLongProperty for accuracy, the long value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getLongProperty3() throws Exception {
        synchronizedObject.setPropertyValue("key", String.class);
        assertEquals("The getLongProperty should return the long value.", null, synchronizedObject.getLongProperty(
            "key2", false));
    }

    /**
     * The test of the method getDoubleProperty for accuracy, the double value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDoubleProperty1() throws Exception {
        synchronizedObject.setPropertyValue("key", "101.113");

        assertEquals("The getDoubleProperty should return the double value.", 101.113D, synchronizedObject
            .getDoubleProperty("key", true));
    }

    /**
     * The test of the method getDoubleProperty for accuracy, the double value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDoubleProperty2() throws Exception {
        synchronizedObject.setPropertyValue("key", 0.123D);
        assertEquals("The getDoubleProperty should return the double value.", 0.123D, synchronizedObject
            .getDoubleProperty("key", true));
    }

    /**
     * The test of the method getDoubleProperty for accuracy, the double value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDoubleProperty3() throws Exception {
        synchronizedObject.setPropertyValue("key", String.class);
        assertEquals("The getDoubleProperty should return the double value.", null, synchronizedObject
            .getDoubleProperty("key2", false));
    }

    /**
     * The test of the method getDateProperty for accuracy, the date value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDateProperty1() throws Exception {
        synchronizedObject.setPropertyValue("key", "2011-01-02 03:04");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2011);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 4);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertEquals("The getDateProperty should return the date.", calendar.getTime(), synchronizedObject
            .getDateProperty("key", "yyyy-MM-dd hh:mm", true));
    }

    /**
     * The test of the method getDateProperty for accuracy, the date value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDateProperty2() throws Exception {
        Date date = new Date();
        synchronizedObject.setPropertyValue("key", date);
        assertEquals("The getDateProperty should return the date.", date, synchronizedObject.getDateProperty("key",
            "yyyy", true));
    }

    /**
     * The test of the method getClassProperty for accuracy, the date value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDateProperty3() throws Exception {
        synchronizedObject.setPropertyValue("key", String.class);
        assertEquals("The getDateProperty should return the Date value.", null, synchronizedObject.getClassProperty(
            "key2", false));
    }

    /**
     * The test of the method getClassProperty for accuracy, parse the value and return class type.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getClassProperty1() throws Exception {
        synchronizedObject.setPropertyValue("key", "java.lang.Exception");
        assertEquals("The getClassProperty should return the Exception class.", Exception.class, synchronizedObject
            .getClassProperty("key", true));
    }

    /**
     * The test of the method getClassProperty for accuracy, class type.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getClassProperty2() throws Exception {
        synchronizedObject.setPropertyValue("key", String.class);
        assertEquals("The getClassProperty should return the String class.", String.class, synchronizedObject
            .getClassProperty("key", true));
    }

    /**
     * The test of the method getClassProperty for accuracy, optional null.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getClassProperty3() throws Exception {
        synchronizedObject.setPropertyValue("key", String.class);
        assertEquals("The getClassProperty should return the String class.", null, synchronizedObject.getClassProperty(
            "key2", false));
    }

    /**
     * The test of the method getPropertyValue(String key, Class&lt;T&gt; clazz, boolean required) for accuracy, no
     * value is set, should return null.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getPropertyValue21() throws Exception {
        assertNull("should return null.", synchronizedObject.getPropertyValue("key", String.class, false));
    }

    /**
     * The test of the method getPropertyValue(String key, Class&lt;T&gt; clazz, boolean required) for accuracy, null is
     * set, but null should return.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getPropertyValue22() throws Exception {
        synchronizedObject.setPropertyValue("key", null);
        String value = synchronizedObject.getPropertyValue("key", String.class, false);
        assertNull("The array should contains null.", value);
    }

    /**
     * The test of the method getPropertyValue(String key, Class&lt;T&gt; clazz, boolean required) for accuracy, the
     * value should be returned.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getPropertyValue23() throws Exception {
        synchronizedObject.setPropertyValues("key", new Object[] { new Integer(5), 6 });
        Integer value = synchronizedObject.getPropertyValue("key", Integer.class, false);
        assertEquals("All the values should be retrieved.", 5, value.intValue());
    }

    /**
     * The test of the method getPropertyValues(String key, Class&lt;T&gt; clazz, boolean required) for accuracy, no
     * value is set, should return null.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getPropertyValues21() throws Exception {
        assertNull("should return null.", synchronizedObject.getPropertyValues("key", String.class, false));
    }

    /**
     * The test of the method getPropertyValues(String key, Class&lt;T&gt; clazz, boolean required) for accuracy, null
     * is set, but a array contains null should return.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getPropertyValues22() throws Exception {
        synchronizedObject.setPropertyValue("key", null);
        String[] array = synchronizedObject.getPropertyValues("key", String.class, false);
        assertEquals("should return the array.", 1, array.length);
        assertNull("The array should contains null.", root.getPropertyValues("key")[0]);
    }

    /**
     * The test of the method getPropertyValues for accuracy, all the values of the array are retrieved.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getPropertyValues23() throws Exception {
        synchronizedObject.setPropertyValues("key", new Object[] { new Integer(5), 6 });
        Integer[] array = synchronizedObject.getPropertyValues("key", Integer.class, false);
        assertEquals("should return the array.", 2, array.length);
        assertEquals("All the values should be retrieved.", 5, array[0].intValue());
        assertEquals("All the values should be retrieved.", 6, array[1].intValue());
    }
}
