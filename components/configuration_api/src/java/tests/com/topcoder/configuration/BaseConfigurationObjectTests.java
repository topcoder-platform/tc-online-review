/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import junit.framework.TestCase;

/**
 * <p>
 * The unit test of the abstract class <code>BaseConfigurationObject</code>.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Added test cases for new methods.</li>
 * <li>Fixed some test cases.</li>
 * </ol>
 * </p>
 *
 * @author haozhangr, sparemax
 * @version 1.1
 */
public class BaseConfigurationObjectTests extends TestCase {
    /**
     * The BaseConfigurationObject instance for test.
     */
    private BaseConfigurationObject bco = new BaseConfigurationObjectMock();

    /**
     * The accuracy test of the empty constructor.
     *
     */
    public void test_ctor_accuracy() {
        assertNotNull("Failed to create the BaseConfigurationObject instance.", new BaseConfigurationObjectMock());
    }

    /**
     * Test inheritance of BaseConfigurationObject, it is implements ConfigurationObject interface.
     *
     */
    public void test_inheritance() {
        assertTrue("BaseConfigurationObject should implements ConfigurationObject interface.",
            bco instanceof ConfigurationObject);
    }

    /**
     * <p>
     * The failure test of the method <code>getName</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getName_failure() throws Exception {
        try {
            bco.getName();
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>containsProperty</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_failure() throws Exception {
        try {
            bco.containsProperty("key");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getPropertyValue(String)</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue1_failure() throws Exception {
        try {
            bco.getPropertyValue("key");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getPropertyValue(String, Class&lt;T&gt;)</code>, this method is not
     * supported by the BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue2_failure() throws Exception {
        try {
            bco.getPropertyValue("key", Object.class);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getPropertyValuesCount</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_failure() throws Exception {
        try {
            bco.getPropertyValuesCount("key");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getPropertyValues(String)</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues1_failure() throws Exception {
        try {
            bco.getPropertyValues("key");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getPropertyValues(String, Class&lt;T&gt;)</code>, this method is not
     * supported by the BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_failure() throws Exception {
        try {
            bco.getPropertyValues("key", Object.class);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getPropertyValue(String, Class&lt;T&gt;, boolean)</code>, this method is not
     * supported by the BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_failure() throws Exception {
        try {
            bco.getPropertyValue("key", Object.class, false);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getPropertyValues(String, Class&lt;T&gt;, boolean)</code>, this method
     * is not supported by the BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_failure() throws Exception {
        try {
            bco.getPropertyValues("key", Object.class, false);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getIntegerProperty</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure() throws Exception {
        try {
            bco.getIntegerProperty("key", false);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getLongProperty</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure() throws Exception {
        try {
            bco.getLongProperty("key", false);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getDoubleProperty</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure() throws Exception {
        try {
            bco.getDoubleProperty("key", false);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getDateProperty</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure() throws Exception {
        try {
            bco.getDateProperty("key", "yyyy-MM-dd", false);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getClassProperty</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure() throws Exception {
        try {
            bco.getClassProperty("key", false);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>setPropertyValue</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValue_failure() throws Exception {
        try {
            bco.setPropertyValue("key", new Object());
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>setPropertyValues</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValues_failure() throws Exception {
        try {
            bco.setPropertyValues("key", new Object[0]);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>removeProperty</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeProperty_failure() throws Exception {
        try {
            bco.removeProperty("key");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>clearProperties</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clearProperties_failure() throws Exception {
        try {
            bco.clearProperties();
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getPropertyKeys</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyKeys_failure() throws Exception {
        try {
            bco.getPropertyKeys("pattern");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getAllPropertyKeys</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getAllPropertyKeys_failure() throws Exception {
        try {
            bco.getAllPropertyKeys();
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>containsChild</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsChild_failure() throws Exception {
        try {
            bco.containsChild("child");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getChild</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChild_failure() throws Exception {
        try {
            bco.getChild("child");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>addChild</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChild_failure() throws Exception {
        try {
            bco.addChild(new BaseConfigurationObjectMock());
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>removeChild</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChild_failure() throws Exception {
        try {
            bco.removeChild("name");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>clearChildren</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clearChildren_failure() throws Exception {
        try {
            bco.clearChildren("name");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getChildren</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChildren_failure() throws Exception {
        try {
            bco.getChildren("name");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getAllChildrenNames</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getAllChildrenNames_failure() throws Exception {
        try {
            bco.getAllChildrenNames();
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getAllChildren</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getAllChildren_failure() throws Exception {
        try {
            bco.getAllChildren();
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getAllDescendants</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getAllDescendants_failure() throws Exception {
        try {
            bco.getAllDescendants();
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>getDescendants</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getDescendants_failure() throws Exception {
        try {
            bco.getDescendants("pattern");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>findDescendants</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_findDescendants_failure() throws Exception {
        try {
            bco.findDescendants("path");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>deleteDescendants</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_deleteDescendants_failure() throws Exception {
        try {
            bco.deleteDescendants("path");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>setPropertyValue</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValueWithPath_failure() throws Exception {
        try {
            bco.setPropertyValue("path", "key", new Object());
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>setPropertyValues</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValuesWithPath_failure() throws Exception {
        try {
            bco.setPropertyValues("path", "key", new Object[0]);
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>removeProperty</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removePropertyWithPath_failure() throws Exception {
        try {
            bco.removeProperty("path", "key");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>clearProperties</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clearPropertiesWithPath_failure() throws Exception {
        try {
            bco.clearProperties("path");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>addChild</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChildWithPath_failure() throws Exception {
        try {
            bco.addChild("path", new BaseConfigurationObjectMock());
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>removeChild</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChildWithPath_failure() throws Exception {
        try {
            bco.removeChild("path", "name");
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>clearChildren</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clearChildrenWithPath_failure() throws Exception {
        try {
            bco.clearChildren();
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>processDescendants</code>, this method is not supported by the
     * BaseConfigurationObject.
     * </p>
     *
     * <p>
     * Should throw UnsupportedOperationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_processDescendantsWithPath_failure() throws Exception {
        try {
            bco.processDescendants("path", new ProcessorMock());
            fail("Should have thrown UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
            // ok
        }
    }

    /**
     * The accuracy test of the <code>clone</code> method.
     *
     */
    public void test_clone_accuracy() {
        Object clone = bco.clone();
        assertTrue("The object should be instance of BaseConfigurationObject.",
            clone instanceof BaseConfigurationObject);
        assertFalse("Clone should not the same one as the init one.", bco == clone);
    }
}
