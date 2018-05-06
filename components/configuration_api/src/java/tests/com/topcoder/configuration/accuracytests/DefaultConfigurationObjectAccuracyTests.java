/*
 * Copyright (C) 2007-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;

import java.util.Calendar;
import java.util.Date;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.TemplateConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy test of the class DefaultConfigurationObject.
 * </p>
 *
 * Changes in 1.1 : add test cases for added methods.
 *
 * @author KKD, TCSDEVLOPER
 *
 * @version 1.1
 * @since 1.0
 */
public class DefaultConfigurationObjectAccuracyTests extends TestCase {
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
        node5 = new DefaultConfigurationObject("pc");
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
     */
    public void test_accuracy_getName() {
        assertEquals("The name can not be retrieved successfully.", "root", root.getName());
    }

    /**
     * The test of the method setPropertyValue for accuracy, the value can be null, and the null is set in to the
     * property.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_setPropertyValue1() throws Exception {
        Object[] values = root.setPropertyValue("key", null);
        assertNull("The init value is not set, null should be returned.", values);

        // the value null can be set into the root, set again and should return the old value
        values = root.setPropertyValue("key", null);
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
        root.setPropertyValue("key", "oldvalue");
        root.setPropertyValue("key", "newvalue");
        assertNotNull("The result is unexpected.", root.getPropertyValue("key"));
        assertEquals("The old value should be overrideb.", "newvalue", root.getPropertyValue("key"));
    }

    /**
     * The test of the method setPropertyValue for accuracy, the value can be null, if null, empty array is used.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_setPropertyValues1() throws Exception {
        Object[] values = root.setPropertyValues("key", null);
        assertNull("The init value is not set, null should be returned.", values);

        // null should be used as empty property
        values = root.getPropertyValues("key");
        assertEquals("Empty value should be set in the property.", 0, values.length);
    }

    /**
     * The test of the method setPropertyValue for accuracy, the value array should be set into the property.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_setPropertyValues2() throws Exception {
        Object[] values = root.setPropertyValues("key", new Object[] { "v1", "v2", "v3", "v4" });
        values = root.getPropertyValues("key");
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
        assertFalse("The property key should not be contained.", root.containsProperty("key"));
        root.setPropertyValues("key", null);
        assertTrue("The property key should be contained.", root.containsProperty("key"));

        root.setPropertyValues("newkey", new Object[] { "v1", "v2" });
        assertTrue("The property newkey should be contained.", root.containsProperty("newkey"));
    }

    /**
     * The test of the method containsProperty for accuracy
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_containsProperty2() throws Exception {
        assertFalse("The property key should not be contained.", root.containsProperty("key"));
        root.setPropertyValue("key", null);
        assertTrue("The property key should be contained.", root.containsProperty("key"));

        root.setPropertyValue("newkey", "value");
        assertTrue("The property newkey should be contained.", root.containsProperty("newkey"));
    }

    /**
     * The test of the method getPropertyValue for accuracy, no value is set, should return null.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValue1() throws Exception {
        assertNull("should return null.", root.getPropertyValue("key"));
    }

    /**
     * The test of the method getPropertyValue for accuracy, null is set, should return null.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValue2() throws Exception {
        root.setPropertyValue("key", null);
        assertNull("should return null.", root.getPropertyValue("key"));
    }

    /**
     * The test of the method getPropertyValue for accuracy, empty value is set, should return null.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValue3() throws Exception {
        root.setPropertyValues("key", null);
        assertNull("should return null.", root.getPropertyValue("key"));
    }

    /**
     * The test of the method getPropertyValue for accuracy, first value of the array is retrieved.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValue4() throws Exception {
        root.setPropertyValues("key", new Object[] { new Integer(5), "5" });
        assertEquals("The first value should be retrieved.", new Integer(5), root.getPropertyValue("key"));
    }

    /**
     * The test of the method getPropertyValuesCount for accuracy, if the key does not exist, return -1.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValuesCount1() throws Exception {
        assertEquals("The property key does not exist, should return -1.", -1, root.getPropertyValuesCount("key"));
    }

    /**
     * The test of the method getPropertyValuesCount for accuracy, if the key does exist.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValuesCount2() throws Exception {
        root.setPropertyValues("key", new Object[] { "v1", new Double(10) });
        assertEquals("The property key does exist, two values exist.", 2, root.getPropertyValuesCount("key"));
    }

    /**
     * The test of the method getPropertyValues for accuracy, no value is set, should return null.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValues1() throws Exception {
        assertNull("should return null.", root.getPropertyValues("key"));
    }

    /**
     * The test of the method getPropertyValues for accuracy, null is set, but a array contains null should return.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValues2() throws Exception {
        root.setPropertyValue("key", null);
        assertNotNull("should not return null.", root.getPropertyValues("key"));
        assertNull("The array should contains null.", root.getPropertyValues("key")[0]);
    }

    /**
     * The test of the method getPropertyValues for accuracy, empty value is set, should return empty array.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValues3() throws Exception {
        root.setPropertyValues("key", null);
        assertEquals("Empty array should be returned.", 0, root.getPropertyValues("key").length);
    }

    /**
     * The test of the method getPropertyValues for accuracy, all the values of the array are retrieved.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyValues4() throws Exception {
        root.setPropertyValues("key", new Object[] { new Integer(5), "5" });
        assertEquals("All the values should be retrieved.", 2, root.getPropertyValues("key").length);
    }

    /**
     * The test of the method removeProperty for accuracy.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_removeProperty1() throws Exception {
        Object[] values = root.removeProperty("key");
        assertNull("The old value is null, should return null.", values);
    }

    /**
     * The test of the method removeProperty for accuracy, the old value is returned.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_removeProperty2() throws Exception {
        root.setPropertyValues("key", new Object[] {});
        Object[] values = root.removeProperty("key");
        assertNotNull("The old value is not null, should not return null.", values);
    }

    /**
     * The test of the method getAllPropertyKeys for accuracy, no key exist, empty array should be returned.
     *
     */
    public void test_accuracy_getPropertyKeys() {
        String[] keys = root.getAllPropertyKeys();
        assertEquals("The array should be empty.", 0, keys.length);
    }

    /**
     * The test of the method getAllPropertyKeys for accuracy, keys exist.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyKeys2() throws Exception {
        root.setPropertyValue("key1", "1");
        root.setPropertyValue("key2", "2");
        root.setPropertyValue("key3", "3");
        String[] keys = root.getAllPropertyKeys();
        assertEquals("The array should be not empty.", 3, keys.length);
    }

    /**
     * The test of the method getPropertyKeys for accuracy, all the keys satisfy the pattern is retrieved.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getPropertyKeys1() throws Exception {
        root.setPropertyValue("a", "1");
        root.setPropertyValue("b", "2");
        root.setPropertyValue("c", "3");
        String[] keys = root.getPropertyKeys("[abef]");
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
        root.addChild(child);
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
        root.addChild(child);
        DefaultConfigurationObject newChild = new DefaultConfigurationObject("child");
        root.addChild(newChild);
        assertFalse("The child should be overriden.", child == root.getChild("child"));
        assertTrue("The new child should be got.", newChild == root.getChild("child"));
    }

    /**
     * The test of the method containsChild
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_containsChild() throws Exception {
        root = new DefaultConfigurationObject("root");
        assertFalse("does not contain.", root.containsChild("child"));
        root.addChild(new DefaultConfigurationObject("child"));
        // now contains
        assertTrue("now contains.", root.containsChild("child"));
    }

    /**
     * The test of the method getChild for accuracy, the child is added with the same name, the old one is overriden.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getChild1() throws Exception {
        root = new DefaultConfigurationObject("root");
        assertNull("The root does not contain child, return null.", root.getChild("child"));
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        root.addChild(child);
        assertTrue("The child with the name is got.", child == root.getChild("child"));
    }

    /**
     * The test of the method removeChild for accuracy, the child is removed .
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_removedChild1() throws Exception {
        root = new DefaultConfigurationObject("root");
        assertNull("The root does not contain child, return null.", root.getChild("child"));
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        root.addChild(child);
        root.removeChild("child");
        assertNull("The root does not contain child, it is removed.", root.getChild("child"));
    }

    /**
     * The test of the method clearChildren for accuracy, the child is cleared .
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_clearChildren() throws Exception {
        root = new DefaultConfigurationObject("root");
        assertNull("The root does not contain child, return null.", root.getChild("child"));
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        root.addChild(child);
        root.clearChildren();
        assertNull("The root does not contain child, it is cleared.", root.getChild("child"));
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
        assertEquals("3 names should be got.", 3, root.getAllChildrenNames().length);
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
        assertEquals("3 children should be got.", 3, root.getAllChildren().length);
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
        root.addChild(child1);
        root.addChild(child2);
        root.addChild(child3);
        assertEquals("2 children should be got.", 2, root.getChildren("[bcz]").length);
    }

    /**
     * The test of the method getAllDescendants for accuracy, the configuration of the DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_getAllDescendants() throws Exception {
        ConfigurationObject[] descendants = root.getAllDescendants();
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
        ConfigurationObject[] descendants = root.getDescendants("not such path");
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
        ConfigurationObject[] descendants = root.getDescendants("[ab][ab]");
        assertEquals("2 descendants exists with the pattern.", 2, descendants.length);
    }

    /**
     * The test of the method findDescendants for accuracy, the configuration of the DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_findDescendants1() throws Exception {
        ConfigurationObject[] descendants = root.findDescendants("not such path");
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
        ConfigurationObject[] descendants = root.findDescendants("*c#ab*/*pc??");
        assertEquals("2 descendants exists with the path.", 2, descendants.length);
        descendants = root.findDescendants("*/*/*/*/*");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = root.findDescendants("*/*/*/*/?");
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
        ConfigurationObject[] descendants = root.findDescendants("// \\/*c#ab*/*pc??  \\/// /");
        assertEquals("2 descendants exists with the path.", 2, descendants.length);
        descendants = root.findDescendants("// // \\ \\*/*/*/*/*// // \\ \\");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = root.findDescendants("// // \\ \\*/*/*/*/?    \n//");
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
        ConfigurationObject[] descendants = root.findDescendants("// \\/*c#ab*\\/\\/*pc??  \\/// /");
        assertEquals("2 descendants exists with the path.", 2, descendants.length);
        descendants = root.findDescendants("// // \\ \\\\*//\\/*/*\\/*//*// // \\ \\");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = root.findDescendants("// // \\ \\*/*/*/*/?    \n//");
        assertEquals("0 descendants exists with the path.", 0, descendants.length);
    }

    /**
     * The test of the method deleteDescendants for accuracy, the configuration of the DAG see the doc of setUp.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_deleteDescendants1() throws Exception {
        ConfigurationObject[] descendants = root.deleteDescendants("not such path");
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
        ConfigurationObject[] descendants = root.deleteDescendants("*c#ab*/*pc??");
        assertEquals("2 descendants exists with the path.", 2, descendants.length);
        descendants = root.getAllDescendants();
        assertEquals("7 descendants exists for root now.", 7, descendants.length);
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
        ConfigurationObject[] descendants = root.deleteDescendants("// \\/*c#ab*  \\/// /");
        assertEquals("2 descendants exists with the path.", 2, descendants.length);
        descendants = root.getAllDescendants();
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
        ConfigurationObject[] descendants = root.deleteDescendants("// // \\ \\*/*/*/*/*    \n//");
        assertEquals("3 descendants exists with the path.", 3, descendants.length);
        descendants = root.getAllDescendants();
        assertEquals("9 descendants exists for root now.", 9, descendants.length);
        descendants = node1.getAllDescendants();
        assertEquals("4 descendants exists for node1 now.", 4, descendants.length);
    }

    /**
     * The test of the method Clone for accuracy.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clone_accuracy() throws Exception {
        DefaultConfigurationObject clone = (DefaultConfigurationObject) root.clone();
        assertConfigurationObjectEquals(root, clone);
    }

    /**
     * Assert two DefaultConfigurationObject instance is equal, but not the same one. Also include the child, the
     * compare is a deep one.
     *
     * @param obj1
     *            the first ConfigurationObject
     * @param obj2
     *            the second ConfigurationObject
     * @throws Exception
     *             to JUnit
     */
    private void assertConfigurationObjectEquals(ConfigurationObject obj1, ConfigurationObject obj2) throws Exception {
        assertEquals(obj1.getAllChildren().length, obj2.getAllChildren().length);
        assertEquals("The name should be same name.", obj1.getName(), obj2.getName());
        assertFalse("Should not the same instance.", obj1 == obj2);

        ConfigurationObject[] children = obj1.getAllChildren();
        for (int i = 0; i < children.length; i++) {
            assertConfigurationObjectEquals(children[i], obj2.getChild(children[i].getName()));
        }
    }

    /**
     * The test of the method getIntegerProperty for accuracy, the int value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getIntegerProperty1() throws Exception {
        root.setPropertyValue("key", "111");

        assertEquals("The getIntegerProperty should return the int value.", 111, root.getIntegerProperty("key", true)
            .longValue());
    }

    /**
     * The test of the method getIntegerProperty for accuracy, the int value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getIntegerProperty2() throws Exception {
        root.setPropertyValue("key", 111);
        assertEquals("The getIntegerProperty should return the int value.", 111, root.getIntegerProperty("key", true)
            .longValue());
    }

    /**
     * The test of the method getIntegerProperty for accuracy, the int value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getIntegerProperty3() throws Exception {
        root.setPropertyValue("key", String.class);
        assertEquals("The getIntegerProperty should return the int value.", null, root
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
        root.setPropertyValue("key", "111");

        assertEquals("The getLongProperty should return the long value.", 111, root.getLongProperty("key", true)
            .longValue());
    }

    /**
     * The test of the method getLongProperty for accuracy, the v value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getLongProperty2() throws Exception {
        root.setPropertyValue("key", 101111L);
        assertEquals("The getLongProperty should return the long value.", 101111, root.getLongProperty("key", true)
            .longValue());
    }

    /**
     * The test of the method getLongProperty for accuracy, the long value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getLongProperty3() throws Exception {
        root.setPropertyValue("key", String.class);
        assertEquals("The getLongProperty should return the long value.", null, root.getLongProperty("key2", false));
    }

    /**
     * The test of the method getDoubleProperty for accuracy, the double value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDoubleProperty1() throws Exception {
        root.setPropertyValue("key", "101,101.113");

        assertEquals("The getDoubleProperty should return the double value.", 101101.113D, root.getDoubleProperty("key",
            true));
    }

    /**
     * The test of the method getDoubleProperty for accuracy, the double value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDoubleProperty2() throws Exception {
        root.setPropertyValue("key", 0.123D);
        assertEquals("The getDoubleProperty should return the double value.", 0.123D, root.getDoubleProperty("key",
            true));
    }

    /**
     * The test of the method getDoubleProperty for accuracy, the double value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDoubleProperty3() throws Exception {
        root.setPropertyValue("key", String.class);
        assertEquals("The getDoubleProperty should return the double value.", null, root.getDoubleProperty("key2",
            false));
    }

    /**
     * The test of the method getDateProperty for accuracy, the date value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDateProperty1() throws Exception {
        root.setPropertyValue("key", "2011-01-02 03:04");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2011);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 4);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertEquals("The getDateProperty should return the date.", calendar.getTime(), root.getDateProperty("key",
            "yyyy-MM-dd hh:mm", true));
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
        root.setPropertyValue("key", date);
        assertEquals("The getDateProperty should return the date.", date, root.getDateProperty("key", "yyyy", true));
    }

    /**
     * The test of the method getClassProperty for accuracy, the date value.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getDateProperty3() throws Exception {
        root.setPropertyValue("key", String.class);
        assertEquals("The getDateProperty should return the Date value.", null, root.getClassProperty("key2", false));
    }

    /**
     * The test of the method getClassProperty for accuracy, parse the value and return class type.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getClassProperty1() throws Exception {
        root.setPropertyValue("key", "java.lang.Exception");
        assertEquals("The getClassProperty should return the Exception class.", Exception.class, root.getClassProperty(
            "key", true));
    }

    /**
     * The test of the method getClassProperty for accuracy, class type.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getClassProperty2() throws Exception {
        root.setPropertyValue("key", String.class);
        assertEquals("The getClassProperty should return the String class.", String.class, root.getClassProperty("key",
            true));
    }

    /**
     * The test of the method getClassProperty for accuracy, optional null.
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_accuracy_getClassProperty3() throws Exception {
        root.setPropertyValue("key", String.class);
        assertEquals("The getClassProperty should return the String class.", null, root.getClassProperty("key2", false));
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
        assertNull("should return null.", root.getPropertyValue("key", String.class, false));
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
        root.setPropertyValue("key", null);
        String value = root.getPropertyValue("key", String.class, false);
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
        root.setPropertyValues("key", new Object[] { new Integer(5), 6 });
        Integer value = root.getPropertyValue("key", Integer.class, false);
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
        assertNull("should return null.", root.getPropertyValues("key", String.class, false));
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
        root.setPropertyValue("key", null);
        String[] array = root.getPropertyValues("key", String.class, false);
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
        root.setPropertyValues("key", new Object[] { new Integer(5), 6 });
        Integer[] array = root.getPropertyValues("key", Integer.class, false);
        assertEquals("should return the array.", 2, array.length);
        assertEquals("All the values should be retrieved.", 5, array[0].intValue());
        assertEquals("All the values should be retrieved.", 6, array[1].intValue());
    }
}