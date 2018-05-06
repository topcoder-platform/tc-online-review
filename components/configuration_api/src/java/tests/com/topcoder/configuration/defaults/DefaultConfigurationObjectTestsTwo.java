/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.defaults;

import junit.framework.TestCase;

import com.topcoder.configuration.BaseConfigurationObjectMock;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.InvalidConfigurationException;

/**
 * <p>
 * The unit test of the class <code>DefaultConfigurationObject</code>.
 * </p>
 *
 * <p>
 * It's split due to the file length is too long. See DefaultConfigurationObjectTestsOne.
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
public class DefaultConfigurationObjectTestsTwo extends TestCase {
    /**
     * The constant String name.
     */
    private static final String NAME = "name";

    /**
     * The DefaultConfigurationObject instance for test.
     */
    private DefaultConfigurationObject defaultCo = null;

    /**
     * Set up the DefaultConfigurationObject test case, create the DefaultConfigurationObject instance for test.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        defaultCo = new DefaultConfigurationObject(NAME);
    }

    /**
     * <p>
     * The failure test of the method setPropertyValue, verify the key can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValue_failure1() throws Exception {
        try {
            defaultCo.setPropertyValue(null, "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValue, verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValue_failure2() throws Exception {
        try {
            defaultCo.setPropertyValue("", "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValue, verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValue_failure3() throws Exception {
        try {
            defaultCo.setPropertyValue("  ", "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValue, verify the value can not be an instance that is not null or
     * Serailizable instance,
     * </p>
     *
     * <p>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValue_failure4() throws Exception {
        try {
            defaultCo.setPropertyValue("key", new Object());
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method setPropertyValue, old values of the property, or null if the key is new should
     * be returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValue_accuracy() throws Exception {
        // the values can be null
        Object[] values = defaultCo.setPropertyValue("key", null);
        assertNull("The init value is not set, null should be returned.", values);

        values = defaultCo.setPropertyValue("key", "value");
        assertEquals("The old value should be returned.", 1, values.length);

        // set again, now the old value should be returned
        values = defaultCo.setPropertyValue("key", "new_value");
        assertEquals("The old value should be returned.", 1, values.length);
        assertEquals("The old value should be returned.", "value", values[0]);
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the key can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValues_failure1() throws Exception {
        try {
            defaultCo.setPropertyValues(null, new String[] {"value1", "value2"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValues_failure2() throws Exception {
        try {
            defaultCo.setPropertyValues("", new String[] {"value1", "value2"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValues_failure3() throws Exception {
        try {
            defaultCo.setPropertyValues("  ", new String[] {"value1", "value2"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the values can not contains an instance that is not
     * null or Serailizable instance,
     * </p>
     *
     * <p>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValues_failure4() throws Exception {
        try {
            defaultCo.setPropertyValues("key", new Object[] {new Object(), "value2"});
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method setPropertyValues, old values of the property, or null if the key is new should
     * be returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValues_accuracy() throws Exception {
        // the values can be null
        Object[] values = defaultCo.setPropertyValues("key", null);
        assertNull("The init value is not set, null should be returned.", values);

        values = defaultCo.setPropertyValues("key", new String[] {"value1", "value2"});
        assertEquals("The old value should be returned, null should be considered as empty.", 0, values.length);

        // set again, now the old value should be returned
        values = defaultCo.setPropertyValues("key", null);
        assertEquals("The old value should be returned.", 2, values.length);
        assertEquals("The old value should be returned.", "value2", values[1]);
        assertEquals("The old value should be returned.", "value1", values[0]);
    }

    /**
     * <p>
     * The failure test of the method removeProperty, verify the key can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeProperty_failure1() throws Exception {
        try {
            defaultCo.removeProperty(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method removeProperty, verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeProperty_failure2() throws Exception {
        try {
            defaultCo.removeProperty("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method removeProperty, verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeProperty_failure3() throws Exception {
        try {
            defaultCo.removeProperty("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>removeProperty</code>, once the value is removed, null will return to
     * get the property value.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeProperty_accuracy() throws Exception {
        defaultCo.setPropertyValue("key", "v1");

        // remove firstly
        assertEquals("One old value set should be retrieved by remove.", 1, defaultCo.removeProperty("key").length);
        // the property now is removed
        assertNull("The key is removed, should return null.", defaultCo.getPropertyValue("key", Object.class));
    }

    /**
     * <p>
     * The accuracy test of the method <code>clearProperties</code>, once the value is cleared, null will return to
     * get the property value.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clearProperties_accuracy() throws Exception {
        defaultCo.setPropertyValue("key1", "v1");
        defaultCo.setPropertyValue("key2", "v2");

        defaultCo.clearProperties();
        // the property now is cleared
        assertNull("The key is removed, should return null.", defaultCo.getPropertyValue("key1", Object.class));
        assertNull("The key is removed, should return null.", defaultCo.getPropertyValue("key2", Object.class));
    }

    /**
     * <p>
     * The failure test of the method getPropertyKeys, verify the pattern can not be null.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyKeys_failure1() throws Exception {
        try {
            defaultCo.getPropertyKeys(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyKeys, verify the pattern can not be an invalid regex.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyKeys_failure2() throws Exception {
        try {
            defaultCo.getPropertyKeys("\\");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method getPropertyKeys, all the key match the pattern will be retrieved.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyKeys_accuracy() throws Exception {
        defaultCo.setPropertyValue("key", "v1");
        defaultCo.setPropertyValue("aab", "v2");
        defaultCo.setPropertyValue("ab", "v3");
        String[] keys = defaultCo.getPropertyKeys("a*b");

        assertEquals("Two keys match the pattern.", 2, keys.length);
        // if no one is match , empty array is returned
        keys = defaultCo.getPropertyKeys("[cd]");
        assertEquals("Empty array should be returned since no one is match.", 0, keys.length);
    }

    /**
     * <p>
     * The accuracy test of the method getAllPropertyKeys, all the key match the pattern will be retrieved.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getAllPropertyKeys_accuracy() throws Exception {
        defaultCo.setPropertyValue("key", "v1");
        defaultCo.setPropertyValue("aab", "v2");
        defaultCo.setPropertyValue("ab", "v3");
        String[] keys = defaultCo.getAllPropertyKeys();

        assertEquals("Three keys exist, and all should be returned.", 3, keys.length);
    }

    /**
     * <p>
     * The failure test of the method <code>addChild</code>, verify the child can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChild_failure1() throws Exception {
        try {
            defaultCo.addChild(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>addChild</code>, verify the child can not be itself,
     * </p>
     *
     * <p>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChild_failure2() throws Exception {
        try {
            defaultCo.addChild(defaultCo);
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>addChild</code>, verify after adding the child, cyclic relationship can
     * not occur.
     * </p>
     *
     * <p>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChild_failure3() throws Exception {
        // add defaultCo to the child firstly
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        child.addChild(defaultCo);
        try {
            // the defaultCo is already Descendant of the child
            defaultCo.addChild(child);
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>addChild</code>, verify the child must be an instance of
     * DefaultConfigurationObject.
     * </p>
     *
     * <p>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChild_failure4() throws Exception {
        try {
            defaultCo.addChild(new BaseConfigurationObjectMock());
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>addChild</code>.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChild_accuracy() throws Exception {
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        defaultCo.addChild(child);
    }

    /**
     * <p>
     * The failure test of the method containsChild, verify the name can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsChild_failure1() throws Exception {
        try {
            defaultCo.containsChild(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method containsChild, verify the name can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsChild_failure2() throws Exception {
        try {
            defaultCo.containsChild("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method containsChild, verify the name can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsChild_failure3() throws Exception {
        try {
            defaultCo.containsChild("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>containsChild</code>.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsChild_accuracy() throws Exception {
        assertFalse("No children, false should be returned.", defaultCo.containsChild("child"));
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        defaultCo.addChild(child);

        // now the child is added
        assertTrue("Child is added, true should be returned.", defaultCo.containsChild("child"));
    }

    /**
     * <p>
     * The failure test of the method getChild, verify the name can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChild_failure1() throws Exception {
        try {
            defaultCo.getChild(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getChild, verify the name can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChild_failure2() throws Exception {
        try {
            defaultCo.getChild("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getChild, verify the name can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChild_failure3() throws Exception {
        try {
            defaultCo.getChild("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>getChild</code>.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChild_accuracy() throws Exception {
        assertNull("No children, null should be returned.", defaultCo.getChild("child"));
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        defaultCo.addChild(child);

        // now the child is added
        assertNotNull("Child is added, Non_null should be returned.", defaultCo.getChild("child"));
    }

    /**
     * <p>
     * The failure test of the method removeChild, verify the name can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChild_failure1() throws Exception {
        try {
            defaultCo.removeChild(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method removeChild, verify the name can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChild_failure2() throws Exception {
        try {
            defaultCo.removeChild("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method removeChild, verify the name can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChild_failure3() throws Exception {
        try {
            defaultCo.removeChild("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>removeChild</code>.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChild_accuracy() throws Exception {
        assertNull("No children, null should be returned.", defaultCo.removeChild("child"));
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        defaultCo.addChild(child);

        // now the child is added
        assertNotNull("Child is added, Non_null should be returned.", defaultCo.removeChild("child"));
        assertNull("The child is removed, null should be retrieved.", defaultCo.getChild("child"));
    }

    /**
     * <p>
     * The accuracy test of the method <code>clearChildren</code>.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clearChildren_accuracy() throws Exception {
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        defaultCo.addChild(child);

        defaultCo.clearChildren();
        // now the child is cleared
        assertNull("The child is removed, null should be retrieved.", defaultCo.getChild("child"));
    }

    /**
     * <p>
     * The failure test of the method getChildren, verify the pattern can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChildren_failure1() throws Exception {
        try {
            defaultCo.getChildren(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getChildren, verify the pattern can not be invalid regex,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChildren_failure2() throws Exception {
        try {
            defaultCo.getChildren("\\");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>getChildren</code>, all the child whose name match the pattern will be
     * returned.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChildren_accuracy() throws Exception {
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("child");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("aab");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("ab");
        defaultCo.addChild(child1);
        defaultCo.addChild(child2);
        defaultCo.addChild(child3);

        ConfigurationObject[] children = defaultCo.getChildren("a*b");

        assertEquals("Two children should be retrieved.", 2, children.length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>getAllChildrenNames</code>, all the child whose name match the pattern
     * will be returned.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getAllChildrenNames_accuracy() throws Exception {
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("child");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("aab");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("ab");
        defaultCo.addChild(child1);
        defaultCo.addChild(child2);
        defaultCo.addChild(child3);

        String[] children = defaultCo.getAllChildrenNames();

        assertEquals("Three children should be retrieved.", 3, children.length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>getAllChildren</code>, all the child whose name match the pattern will
     * be returned.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getAllChildren_accuracy() throws Exception {
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("child");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("aab");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("ab");
        defaultCo.addChild(child1);
        defaultCo.addChild(child2);
        defaultCo.addChild(child3);

        ConfigurationObject[] children = defaultCo.getAllChildren();

        assertEquals("Three children should be retrieved.", 3, children.length);
    }

    /**
     * <p>
     * The accuracy test of the method getAllDescendants, the DefaultConfigurationObject only contains child, all the
     * children will be retrieved.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getAllDescendants_accuracy1() throws Exception {
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("child");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("aab");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("ab");
        defaultCo.addChild(child1);
        defaultCo.addChild(child2);
        defaultCo.addChild(child3);

        ConfigurationObject[] children = defaultCo.getAllDescendants();

        assertEquals("Three Descendants should be retrieved.", 3, children.length);
    }

    /**
     * <p>
     * The accuracy test of the method getAllDescendants, the tree is as follow: name / \ a b \ / c / \ d e \ f .
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getAllDescendants_accuracy2() throws Exception {
        // create the truee
        DefaultConfigurationObject a = new DefaultConfigurationObject("a");
        DefaultConfigurationObject b = new DefaultConfigurationObject("b");
        DefaultConfigurationObject c = new DefaultConfigurationObject("c");
        DefaultConfigurationObject d = new DefaultConfigurationObject("d");
        DefaultConfigurationObject e = new DefaultConfigurationObject("e");
        DefaultConfigurationObject f = new DefaultConfigurationObject("f");
        defaultCo.addChild(a);
        defaultCo.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);

        ConfigurationObject[] children = defaultCo.getAllDescendants();

        assertEquals("6 Descendants should be retrieved.", 6, children.length);
        children = a.getAllDescendants();
        assertEquals("Node a should have 4 Descendants.", 4, children.length);
        children = b.getAllDescendants();
        assertEquals("Node b should have 4 Descendants.", 4, children.length);
        children = c.getAllDescendants();
        assertEquals("Node c should have 3 Descendants.", 3, children.length);
        children = d.getAllDescendants();
        assertEquals("Node d should have 0 Descendants.", 0, children.length);
        children = e.getAllDescendants();
        assertEquals("Node e should have 1 Descendants.", 1, children.length);
        children = f.getAllDescendants();
        assertEquals("Node f should have 0 Descendants.", 0, children.length);
    }

    /**
     * <p>
     * The failure test of the method getDescendants, verify the pattern can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getDescendants_failure1() throws Exception {
        try {
            defaultCo.getDescendants(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDescendants, verify the pattern can not be invalid regex,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getDescendants_failure2() throws Exception {
        try {
            defaultCo.getDescendants("\\");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method getDescendants, the tree is as follow: name / \ a b \ / c / \ d e \ f .
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getDescendants_accuracy2() throws Exception {
        // create the truee
        DefaultConfigurationObject a = new DefaultConfigurationObject("a");
        DefaultConfigurationObject b = new DefaultConfigurationObject("b");
        DefaultConfigurationObject c = new DefaultConfigurationObject("c");
        DefaultConfigurationObject d = new DefaultConfigurationObject("d");
        DefaultConfigurationObject e = new DefaultConfigurationObject("e");
        DefaultConfigurationObject f = new DefaultConfigurationObject("f");
        defaultCo.addChild(a);
        defaultCo.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);

        ConfigurationObject[] children = defaultCo.getDescendants("a");
        assertEquals("1 Descendants should be retrieved.", 1, children.length);
        children = defaultCo.getDescendants("[abdy]");
        assertEquals("3 Descendants should be retrieved.", 3, children.length);
    }

    /**
     * <p>
     * The failure test of the method <code>findDescendants</code>, verify the path can not be null.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     */
    public void test_findDescendants_failure() {
        try {
            defaultCo.findDescendants(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>findDescendants</code>, no Descendant is found, empty array should be
     * return.
     * </p>
     *
     * <p>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_findDescendants_accuracy1() throws Exception {
        // create the truee
        DefaultConfigurationObject a = new DefaultConfigurationObject("a");
        DefaultConfigurationObject b = new DefaultConfigurationObject("b");
        DefaultConfigurationObject c = new DefaultConfigurationObject("c");
        DefaultConfigurationObject d = new DefaultConfigurationObject("d");
        DefaultConfigurationObject e = new DefaultConfigurationObject("e");
        DefaultConfigurationObject f = new DefaultConfigurationObject("f");
        defaultCo.addChild(a);
        defaultCo.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);

        ConfigurationObject[] descendants = defaultCo.findDescendants("p");
        assertEquals("No descendant should be retrieved.", 0, descendants.length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>findDescendants</code>, duplicated object can not be exists in the
     * result.
     * </p>
     *
     * <p>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_findDescendants_accuracy2() throws Exception {
        // create the truee
        DefaultConfigurationObject a = new DefaultConfigurationObject("a");
        DefaultConfigurationObject b = new DefaultConfigurationObject("b");
        DefaultConfigurationObject c = new DefaultConfigurationObject("c");
        DefaultConfigurationObject d = new DefaultConfigurationObject("d");
        DefaultConfigurationObject e = new DefaultConfigurationObject("e");
        DefaultConfigurationObject f = new DefaultConfigurationObject("f");
        defaultCo.addChild(a);
        defaultCo.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);

        ConfigurationObject[] descendants = defaultCo.findDescendants("*/c");
        assertEquals("One descendant should be retrieved.", 1, descendants.length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>findDescendants</code>, the slashes at both end should be trimmed.
     * </p>
     *
     * <p>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_findDescendants_accuracy3() throws Exception {
        // create the truee
        DefaultConfigurationObject a = new DefaultConfigurationObject("a");
        DefaultConfigurationObject b = new DefaultConfigurationObject("b");
        DefaultConfigurationObject c = new DefaultConfigurationObject("c");
        DefaultConfigurationObject d = new DefaultConfigurationObject("d");
        DefaultConfigurationObject e = new DefaultConfigurationObject("e");
        DefaultConfigurationObject f = new DefaultConfigurationObject("f");
        defaultCo.addChild(a);
        defaultCo.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);

        ConfigurationObject[] descendants = defaultCo.findDescendants("//  */c\\ \\");
        assertEquals("One descendant should be retrieved.", 1, descendants.length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>findDescendants</code>, Continuous slashes should be considered as
     * one.
     * </p>
     *
     * <p>
     * The tree is as follow: name / \ abbx yybzab \ / ppcqv / \ zaddd kkaole \ fz
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_findDescendants_accuracy4() throws Exception {
        // create the truee
        DefaultConfigurationObject a = new DefaultConfigurationObject("abbx");
        DefaultConfigurationObject b = new DefaultConfigurationObject("yybzab");
        DefaultConfigurationObject c = new DefaultConfigurationObject("ppcqv");
        DefaultConfigurationObject d = new DefaultConfigurationObject("zaddd");
        DefaultConfigurationObject e = new DefaultConfigurationObject("kkaole");
        DefaultConfigurationObject f = new DefaultConfigurationObject("fz");
        defaultCo.addChild(a);
        defaultCo.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);

        ConfigurationObject[] descendants = defaultCo.findDescendants("//  */\\///*c??\\ \\");
        assertEquals("One descendant should be retrieved.", 1, descendants.length);

        descendants = defaultCo.findDescendants("//  *ab*/\\///??c*\\\\*a*\\ \\");
        assertEquals("Two descendants should be retrieved.", 2, descendants.length);

        descendants = defaultCo.findDescendants("a?*?/?p*v/kk*/??");
        assertEquals("One descendants should be retrieved.", 1, descendants.length);
    }

    /**
     * <p>
     * The failure test of the method <code>deleteDescendants</code>, verify the path can not be null.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     */
    public void test_deleteDescendants_failure() {
        try {
            defaultCo.deleteDescendants(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>deleteDescendants</code>.
     * </p>
     *
     * <p>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f
     * </p>
     * <p>
     * Firstly delete a-c, then c-e
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_deleteDescendants_accuracy1() throws Exception {
        // create the truee
        DefaultConfigurationObject a = new DefaultConfigurationObject("a");
        DefaultConfigurationObject b = new DefaultConfigurationObject("b");
        DefaultConfigurationObject c = new DefaultConfigurationObject("c");
        DefaultConfigurationObject d = new DefaultConfigurationObject("d");
        DefaultConfigurationObject e = new DefaultConfigurationObject("e");
        DefaultConfigurationObject f = new DefaultConfigurationObject("f");
        defaultCo.addChild(a);
        defaultCo.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);

        ConfigurationObject[] descendants = defaultCo.deleteDescendants("a/c");
        assertEquals("One descendant should be retrieved.", 1, descendants.length);

        // a-c is delete
        descendants = defaultCo.getAllDescendants();
        // still have 6 Descendants
        assertEquals("6 descendants should be retrieved.", 6, descendants.length);
        descendants = a.getAllDescendants();
        assertEquals("no descendant should be retrieved.", 0, descendants.length);

        // delete c-e
        c.deleteDescendants("e");
        descendants = defaultCo.getAllDescendants();
        // still have 4 Descendants
        assertEquals("4 descendants should be retrieved.", 4, descendants.length);

        descendants = c.getAllDescendants();
        // only one for c
        assertEquals("1 descendants should be retrieved.", 1, descendants.length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>deleteDescendants</code>.
     * </p>
     *
     * <p>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f
     * </p>
     * <p>
     * Delete a-c, b-c at the same time.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_deleteDescendants_accuracy2() throws Exception {
        // create the truee
        DefaultConfigurationObject a = new DefaultConfigurationObject("a");
        DefaultConfigurationObject b = new DefaultConfigurationObject("b");
        DefaultConfigurationObject c = new DefaultConfigurationObject("c");
        DefaultConfigurationObject d = new DefaultConfigurationObject("d");
        DefaultConfigurationObject e = new DefaultConfigurationObject("e");
        DefaultConfigurationObject f = new DefaultConfigurationObject("f");
        defaultCo.addChild(a);
        defaultCo.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);

        ConfigurationObject[] descendants = defaultCo.deleteDescendants("*/c");
        assertEquals("One descendant should be retrieved.", 1, descendants.length);

        // a-c, b-c at the same time
        descendants = defaultCo.getAllDescendants();
        // still have 2 Descendants
        assertEquals("2 descendants should be retrieved.", 2, descendants.length);

        descendants = c.getAllDescendants();
        // only one for c
        assertEquals("3 descendants should be retrieved.", 3, descendants.length);
    }

    /**
     * The accuracy test of the method Clone.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clone_accuracy() throws Exception {
        // create the truee
        DefaultConfigurationObject a = new DefaultConfigurationObject("a");
        DefaultConfigurationObject b = new DefaultConfigurationObject("b");
        DefaultConfigurationObject c = new DefaultConfigurationObject("c");
        DefaultConfigurationObject d = new DefaultConfigurationObject("d");
        DefaultConfigurationObject e = new DefaultConfigurationObject("e");
        DefaultConfigurationObject f = new DefaultConfigurationObject("f");
        defaultCo.addChild(a);
        defaultCo.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);

        DefaultConfigurationObject clone = (DefaultConfigurationObject) defaultCo.clone();
        assertConfigurationObjectEquals(defaultCo, clone);
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
        if (obj1 == null && obj2 == null) {
            return;
        }
        if (obj1 == null || obj2 == null) {
            fail("The cloned objected is not equal to the init one.");
        }
        assertEquals(obj1.getAllChildren().length, obj2.getAllChildren().length);
        assertFalse("Should not the same instance.", obj1 == obj2);

        ConfigurationObject[] children = obj1.getAllChildren();
        for (int i = 0; i < children.length; i++) {
            assertConfigurationObjectEquals(children[i], obj2.getChild(children[i].getName()));
        }
    }
}
