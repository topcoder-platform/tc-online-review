/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.accuracytests;

import java.util.Set;

import com.topcoder.configuration.defaults.DefaultConfigurationObject;

/**
 * <p>
 * Accuracy test of the abstract class TemplateConfigurationObject, this testcase extends
 * DefaultConfigurationObjectAccuracyTests.
 * </p>
 *
 * @author KKD
 * @version 1.0
 */
public class TemplateConfigurationObjectAccuracyTests extends DefaultConfigurationObjectAccuracyTests {
    /**
     * The setUp of TemplateConfigurationObject.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * The test of the method setPropertyValue for accuracy, all the node alone the path set the property value.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_setPropertyValue() throws Exception {
        root.setPropertyValue("*c#ab*/*pc??", "key", new Integer(5));
        assertTrue("The property value should be set into the node.", node3.containsProperty("key"));
        assertTrue("The property value should be set into the node.", node4.containsProperty("key"));
        assertEquals("The property value should be set into the node.", new Integer(5), node3.getPropertyValue("key"));
        assertEquals("The property value should be set into the node.", new Integer(5), node4.getPropertyValue("key"));
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
        assertEquals("The property value should be set into the node.", new Integer(5), node3.getPropertyValue("key"));
        assertEquals("The property value should be set into the node.", new Integer(5), node4.getPropertyValue("key"));
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
    }

    /**
     * The test of the method clearChildren for accuracy, all the node alone the path clear the child.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_accuracy_clearChildren() throws Exception {
        root.addChild("*c#ab*/*pc??", new DefaultConfigurationObject("child"));
        root.clearChildren("*c#ab*/*pc??");
        assertFalse("The child should be removed.", node3.containsChild("child"));
        assertFalse("The child should be removed.", node4.containsChild("child"));
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
        assertEquals("2 values should be processed.", 2, processedNodes.size());
        assertTrue("set contains the name of the node processed.", processedNodes.contains("pcpcpc"));
        assertTrue("set contains the name of the node processed.", processedNodes.contains("aapcbb"));
    }
}
