/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;
/**
 * <p>
 * The unit test of the abstract class <code>TemplateConfigurationObject</code>.
 * </p>
 *
 * @author haozhangr
 * @version 1.0
 */
public class TemplateConfigurationObjectTests extends TestCase {
    /**
     * The constant String name.
     */
    private static final String NAME = "name";
    /**
     * The TemplateConfigurationObject instance for test.
     */
    private TemplateConfigurationObject templateCo = null;
    /**
     * The TemplateConfigurationObject instance for test.
     */
    private TemplateConfigurationObject a = null;
    /**
     * The TemplateConfigurationObject instance for test.
     */
    private TemplateConfigurationObject b = null;
    /**
     * The TemplateConfigurationObject instance for test.
     */
    private TemplateConfigurationObject c = null;
    /**
     * The TemplateConfigurationObject instance for test.
     */
    private TemplateConfigurationObject d = null;
    /**
     * The TemplateConfigurationObject instance for test.
     */
    private TemplateConfigurationObject e = null;
    /**
     * The TemplateConfigurationObject instance for test.
     */
    private TemplateConfigurationObject f = null;
    /**
     * The TemplateConfigurationObject instance for test.
     */
    private TemplateConfigurationObject child = null;
    /**
     * <p>
     * Set up the TemplateConfigurationObject testcase,
     * create the TemplateConfigurationObject instance for test.
     * </p>
     *
     * <p>
     * The tree is as follow:
     *              name
     *             /    \
     *             a    b
     *              \  /
     *               c
     *              / \
     *             d   e
     *                  \
     *                   f
     * .</p>
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        templateCo = new DefaultConfigurationObject(NAME);
        //create the truee
        a = new DefaultConfigurationObject("a");
        b = new DefaultConfigurationObject("b");
        c = new DefaultConfigurationObject("c");
        d = new DefaultConfigurationObject("d");
        e = new DefaultConfigurationObject("e");
        f = new DefaultConfigurationObject("f");
        child = new DefaultConfigurationObject("child");
        templateCo.addChild(a);
        templateCo.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);
    }
    /**
     * The accuracy test of the constructor of TemplateConfigurationObject.
     *
     */
    public void test_ctor_accuracy() {
        assertNotNull("Failed to create TemplateConfigurationObject.", new TemplateConfigurationObjectMock());
    }
    /**
     * Test inheritance of TemplateConfigurationObject,
     * it is implements ConfigurationObject interface.
     *
     */
    public void test_inheritance() {
        assertTrue("TemplateConfigurationObject should extends BaseConfigurationObject.",
                templateCo instanceof BaseConfigurationObject);
    }
    /**
     * <p>
     * The failure test of the method setPropertyValue,
     * verify the path can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValue_failure1() throws Exception {
        try {
            templateCo.setPropertyValue(null, "key", "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method setPropertyValue,
     * verify the key can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValue_failure2() throws Exception {
        try {
            templateCo.setPropertyValue("path", null, "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method setPropertyValue,
     * verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValue_failure3() throws Exception {
        try {
            templateCo.setPropertyValue("path", "", "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method setPropertyValue,
     * verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValue_failure4() throws Exception {
        try {
            templateCo.setPropertyValue("path", "  ", "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method setPropertyValue,
     * verify the value is invalid for DefaultConfigurationObject,
     * </p>
     *
     * <p>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValue_failure5() throws Exception {
        try {
            templateCo.setPropertyValue("a", "key", new Object());
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            //ok
        }
    }
    /**
     * <p>
     * The accuracy test of the method setPropertyValue,
     * the value is set properly with the path.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValue_accuracy() throws Exception {
        templateCo.setPropertyValue("a", "key", "value");
        assertTrue("The value should be set to a.", a.containsProperty("key"));
    }
    /**
     * <p>
     * The failure test of the method setPropertyValues,
     * verify the path can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValues_failure1() throws Exception {
        try {
            templateCo.setPropertyValues(null, "key", new String[]{"value"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method setPropertyValues,
     * verify the key can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValues_failure2() throws Exception {
        try {
            templateCo.setPropertyValues("path", null, new String[]{"value"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method setPropertyValues,
     * verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValues_failure3() throws Exception {
        try {
            templateCo.setPropertyValues("path", "", new String[]{"value"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method setPropertyValues,
     * verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValues_failure4() throws Exception {
        try {
            templateCo.setPropertyValues("path", "  ", new String[]{"value"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method setPropertyValues,
     * verify the value is invalid for DefaultConfigurationObject,
     * </p>
     *
     * <p>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValues_failure5() throws Exception {
        try {
            templateCo.setPropertyValues("a", "key", new Object[]{"value", new Object()});
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            //ok
        }
    }
    /**
     * <p>
     * The accuracy test of the method setPropertyValues,
     * the value is set properly with the path.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_setPropertyValues_accuracy() throws Exception {
        templateCo.setPropertyValue("a", "key", new String[]{"value"});
        assertTrue("The value should be set to a.", a.containsProperty("key"));
    }
    /**
     * <p>
     * The failure test of the method removeProperty,
     * verify the path can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_removeProperty_failure1() throws Exception {
        try {
            templateCo.removeProperty(null, "key");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method removeProperty,
     * verify the key can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_removeProperty_failure2() throws Exception {
        try {
            templateCo.removeProperty("path", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method removeProperty,
     * verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_removeProperty_failure3() throws Exception {
        try {
            templateCo.removeProperty("path", "");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method removeProperty,
     * verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_removeProperty_failure4() throws Exception {
        try {
            templateCo.removeProperty("path", "  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The accuracy test of the method removeProperty,
     * the value is set properly with the path.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_removeProperty_accuracy() throws Exception {
        templateCo.setPropertyValue("b", "key", new String[]{"value"});
        assertTrue("The value should be set to b.", b.containsProperty("key"));
        templateCo.removeProperty("b", "key");
        //now the key for b is remove
        assertFalse("The value should be removed from b.", b.containsProperty("key"));
    }
    /**
     * <p>
     * The failure test of the method clearProperties,
     * verify the path can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_clearProperties_failure1() throws Exception {
        try {
            templateCo.clearProperties(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The accuracy test of the method clearProperties,
     * the value is set properly with the path.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_clearProperties_accuracy() throws Exception {
        templateCo.setPropertyValue("b", "key", new String[]{"value"});
        assertTrue("The value should be set to b.", b.containsProperty("key"));
        templateCo.clearProperties("b");
        //now the key for b is cleared
        assertFalse("The value should be removed from b.", b.containsProperty("key"));
    }
    /**
     * <p>
     * The failure test of the method addChild,
     * verify the path can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_addChild_failure1() throws Exception {
        try {
            templateCo.addChild(null, child);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method addChild,
     * verify the child can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_addChild_failure2() throws Exception {
        try {
            templateCo.addChild("b", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The accuracy test of the method addChild,
     * the value is set properly with the path.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_addChild_accuracy() throws Exception {
        templateCo.addChild("b", child);
        assertTrue("The child should be added to b.", b.containsChild(child.getName()));
    }
    /**
     * <p>
     * The failure test of the method removeChild,
     * verify the path can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_removeChild_failure1() throws Exception {
        try {
            templateCo.removeProperty(null, "child");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method removeChild,
     * verify the name can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_removeChild_failure2() throws Exception {
        try {
            templateCo.removeChild("path", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method removeChild,
     * verify the name can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_removeChild_failure3() throws Exception {
        try {
            templateCo.removeChild("path", "");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method removeChild,
     * verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_removeChild_failure4() throws Exception {
        try {
            templateCo.removeChild("path", "  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The accuracy test of the method removeChild,
     * the value is set properly with the path.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_removeChild_accuracy() throws Exception {
        templateCo.addChild("b", child);
        assertTrue("The child should be added to b.", b.containsChild(child.getName()));

        templateCo.removeChild("b", child.getName());
        //the child is remove now
        assertFalse("The child should be removed from b.", b.containsChild(child.getName()));
    }
    /**
     * <p>
     * The failure test of the method clearChildren,
     * verify the path can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_clearChildren_failure1() throws Exception {
        try {
            templateCo.clearChildren(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The accuracy test of the method clearChildren,
     * the value is set properly with the path.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_clearChildren_accuracy() throws Exception {
        templateCo.addChild("b", child);
        assertTrue("The child should be added to b.", b.containsChild(child.getName()));

        templateCo.clearChildren("b");
        //the child is remove now
        assertFalse("The child should be removed from b.", b.containsChild(child.getName()));
    }
    /**
     * <p>
     * The failure test of the method processDescendants,
     * verify the path can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_processDescendants_failure1() throws Exception {
        try {
            templateCo.processDescendants(null, new ProcessorMock());
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The failure test of the method processDescendants,
     * verify the Processor can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void test_processDescendants_failure2() throws Exception {
        try {
            templateCo.processDescendants("a", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }
    /**
     * <p>
     * The accuracy test of the method processDescendants.
     * </p>
     *
     *
     * @throws Exception to JUnit
     */
    public void test_processDescendants_accuracy() throws Exception {
        ProcessorMock processor = new ProcessorMock();
        templateCo.processDescendants("*", processor);
        assertEquals("two ConfigurationObject should be processed.", 2, processor.getCount());
    }
}
