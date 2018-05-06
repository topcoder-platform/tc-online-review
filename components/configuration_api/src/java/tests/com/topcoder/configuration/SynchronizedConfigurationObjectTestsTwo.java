/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import junit.framework.TestCase;

import com.topcoder.configuration.defaults.DefaultConfigurationObject;

/**
 * <p>
 * The unit test of the class <code>SynchronizedConfigurationObject</code>.
 * </p>
 *
 * <p>
 * It's split due to the file length is too long. See SynchronizedConfigurationObjectTestsOne.
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
public class SynchronizedConfigurationObjectTestsTwo extends TestCase {
    /**
     * The constant String name.
     */
    private static final String NAME = "name";

    /**
     * The Synchronized instance for test.
     */
    private SynchronizedConfigurationObject synchronizedCo = null;

    /**
     * The DefaultConfigurationObject instance for test.
     */
    private DefaultConfigurationObject defaultco = null;

    /**
     * The DefaultConfigurationObject instance for test.
     */
    private DefaultConfigurationObject a = null;

    /**
     * The DefaultConfigurationObject instance for test.
     */
    private DefaultConfigurationObject b = null;

    /**
     * The DefaultConfigurationObject instance for test.
     */
    private DefaultConfigurationObject c = null;

    /**
     * The DefaultConfigurationObject instance for test.
     */
    private DefaultConfigurationObject d = null;

    /**
     * The DefaultConfigurationObject instance for test.
     */
    private DefaultConfigurationObject e = null;

    /**
     * The DefaultConfigurationObject instance for test.
     */
    private DefaultConfigurationObject f = null;

    /**
     * The DefaultConfigurationObject instance for test.
     */
    private DefaultConfigurationObject child = null;

    /**
     * <p>
     * Set up the TemplateConfigurationObject test case, create the TemplateConfigurationObject instance for test.
     * </p>
     *
     * <p>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        defaultco = new DefaultConfigurationObject(NAME);
        // create the tree
        a = new DefaultConfigurationObject("a");
        b = new DefaultConfigurationObject("b");
        c = new DefaultConfigurationObject("c");
        d = new DefaultConfigurationObject("d");
        e = new DefaultConfigurationObject("e");
        f = new DefaultConfigurationObject("f");
        child = new DefaultConfigurationObject("child");
        defaultco.addChild(a);
        defaultco.addChild(b);
        a.addChild(c);
        b.addChild(c);
        c.addChild(e);
        c.addChild(d);
        e.addChild(f);
        synchronizedCo = new SynchronizedConfigurationObject(defaultco);
    }

    /**
     * <p>
     * The failure test of the method setPropertyValue, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValue_failure1() throws Exception {
        try {
            synchronizedCo.setPropertyValue(null, "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValue, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValue_failure2() throws Exception {
        try {
            synchronizedCo.setPropertyValue("", "value");
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
            synchronizedCo.setPropertyValue("  ", "value");
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
            synchronizedCo.setPropertyValue("key", new Object());
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
        Object[] values = synchronizedCo.setPropertyValue("key", null);
        assertNull("The init value is not set, null should be returned.", values);

        values = synchronizedCo.setPropertyValue("key", "value");
        assertEquals("The old value should be returned.", 1, values.length);

        // set again, now the old value should be returned
        values = synchronizedCo.setPropertyValue("key", "new_value");
        assertEquals("The old value should be returned.", 1, values.length);
        assertEquals("The old value should be returned.", "value", values[0]);
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValues_failure1() throws Exception {
        try {
            synchronizedCo.setPropertyValues(null, new String[] {"value1", "value2"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValues_failure2() throws Exception {
        try {
            synchronizedCo.setPropertyValues("", new String[] {"value1", "value2"});
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
            synchronizedCo.setPropertyValues("  ", new String[] {"value1", "value2"});
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
            synchronizedCo.setPropertyValues("key", new Object[] {new Object(), "value2"});
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
        Object[] values = synchronizedCo.setPropertyValues("key", null);
        assertNull("The init value is not set, null should be returned.", values);

        values = synchronizedCo.setPropertyValues("key", new String[] {"value1", "value2"});
        assertEquals("The old value should be returned, null should be considered as empty.", 0, values.length);

        // set again, now the old value should be returned
        values = synchronizedCo.setPropertyValues("key", null);
        assertEquals("The old value should be returned.", 2, values.length);
        assertEquals("The old value should be returned.", "value2", values[1]);
        assertEquals("The old value should be returned.", "value1", values[0]);
    }

    /**
     * <p>
     * The failure test of the method removeProperty, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeProperty_failure1() throws Exception {
        try {
            synchronizedCo.removeProperty(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method removeProperty, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeProperty_failure2() throws Exception {
        try {
            synchronizedCo.removeProperty("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method removeProperty, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeProperty_failure3() throws Exception {
        try {
            synchronizedCo.removeProperty("  ");
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
        synchronizedCo.setPropertyValue("key", "v1");

        // remove firstly
        assertEquals("One old value set should be retrieved by remove.", 1,
            synchronizedCo.removeProperty("key").length);
        // the property now is removed
        assertNull("The key is removed, should return null.", synchronizedCo.getPropertyValue("key", Object.class));
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
        synchronizedCo.setPropertyValue("key1", "v1");
        synchronizedCo.setPropertyValue("key2", "v2");

        synchronizedCo.clearProperties();
        // the property now is cleared
        assertNull("The key is removed, should return null.", synchronizedCo.getPropertyValue("key1", Object.class));
        assertNull("The key is removed, should return null.", synchronizedCo.getPropertyValue("key2", Object.class));
    }

    /**
     * <p>
     * The failure test of the method getPropertyKeys, verify the pattern can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyKeys_failure1() throws Exception {
        try {
            synchronizedCo.getPropertyKeys(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyKeys, verify the pattern can not be an invalid regex.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyKeys_failure2() throws Exception {
        try {
            synchronizedCo.getPropertyKeys("\\");
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
        synchronizedCo.setPropertyValue("key", "v1");
        synchronizedCo.setPropertyValue("aab", "v2");
        synchronizedCo.setPropertyValue("ab", "v3");
        String[] keys = synchronizedCo.getPropertyKeys("a*b");

        assertEquals("Two keys match the pattern.", 2, keys.length);
        // if no one is match , empty array is returned
        keys = synchronizedCo.getPropertyKeys("[cd]");
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
        synchronizedCo.setPropertyValue("key", "v1");
        synchronizedCo.setPropertyValue("aab", "v2");
        synchronizedCo.setPropertyValue("ab", "v3");
        String[] keys = synchronizedCo.getAllPropertyKeys();

        assertEquals("Three keys exist, and all should be returned.", 3, keys.length);
    }

    /**
     * <p>
     * The failure test of the method containsChild, verify the name can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsChild_failure1() throws Exception {
        try {
            synchronizedCo.containsChild(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method containsChild, verify the name can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsChild_failure2() throws Exception {
        try {
            synchronizedCo.containsChild("");
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
            synchronizedCo.containsChild("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
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
            synchronizedCo.getChild(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getChild, verify the name can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChild_failure2() throws Exception {
        try {
            synchronizedCo.getChild("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getChild, verify the name can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChild_failure3() throws Exception {
        try {
            synchronizedCo.getChild("  ");
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
        assertNull("No children, null should be returned.", synchronizedCo.getChild("child"));
        DefaultConfigurationObject addingchild = new DefaultConfigurationObject("child");
        synchronizedCo.addChild(addingchild);

        // now the child is added
        assertNotNull("Child is added, Non_null should be returned.", synchronizedCo.getChild("child"));
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
        assertFalse("No children, false should be returned.", synchronizedCo.containsChild("child"));
        DefaultConfigurationObject addingchild = new DefaultConfigurationObject("child");
        synchronizedCo.addChild(addingchild);

        // now the child is added
        assertTrue("Child is added, true should be returned.", synchronizedCo.containsChild("child"));
    }

    /**
     * <p>
     * The failure test of the method <code>addChild</code>, verify the child can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChild_failure1() throws Exception {
        try {
            synchronizedCo.addChild(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>addChild</code>, verify the child can not be itself.<br>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChild_failure2() throws Exception {
        try {
            synchronizedCo.addChild(synchronizedCo);
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>addChild</code>, verify after adding the child, cyclic relationship can
     * not occur.<br>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChild_failure3() throws Exception {
        // add synchronizedCo to the child firstly
        DefaultConfigurationObject addingchild = new DefaultConfigurationObject("child");
        addingchild.addChild(synchronizedCo.getInnerConfigurationObject());
        try {
            // the synchronizedCo is already Descendant of the child
            synchronizedCo.addChild(addingchild);
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method <code>addChild</code>, verify the child must be an instance of
     * DefaultConfigurationObject.<br>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChild_failure4() throws Exception {
        try {
            synchronizedCo.addChild(new BaseConfigurationObjectMock());
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
        DefaultConfigurationObject addingchild = new DefaultConfigurationObject("child");
        synchronizedCo.addChild(addingchild);
    }

    /**
     * <p>
     * The failure test of the method removeChild, verify the name can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChild_failure1() throws Exception {
        try {
            synchronizedCo.removeChild(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method removeChild, verify the name can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChild_failure2() throws Exception {
        try {
            synchronizedCo.removeChild("");
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
    public void test_removeChildfailure3() throws Exception {
        try {
            synchronizedCo.getChild("  ");
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
        assertNull("No children, null should be returned.", synchronizedCo.removeChild("child"));
        DefaultConfigurationObject addingchild = new DefaultConfigurationObject("child");
        synchronizedCo.addChild(addingchild);

        // now the child is added
        assertNotNull("Child is added, Non_null should be returned.", synchronizedCo.removeChild("child"));
        assertNull("The child is removed, null should be retrieved.", synchronizedCo.getChild("child"));
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
        DefaultConfigurationObject addingchild = new DefaultConfigurationObject("child");
        synchronizedCo.addChild(addingchild);

        synchronizedCo.clearChildren();
        // now the child is cleared
        assertNull("The child is removed, null should be retrieved.", synchronizedCo.getChild("child"));
    }

    /**
     * <p>
     * The failure test of the method getChildren, verify the pattern can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChildren_failure1() throws Exception {
        try {
            synchronizedCo.getChildren(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getChildren, verify the pattern can not be invalid regex.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getChildren_failure2() throws Exception {
        try {
            synchronizedCo.getChildren("\\");
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
        defaultco = new DefaultConfigurationObject(NAME);
        synchronizedCo = new SynchronizedConfigurationObject(defaultco);
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("child");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("aab");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("ab");
        synchronizedCo.addChild(child1);
        synchronizedCo.addChild(child2);
        synchronizedCo.addChild(child3);

        ConfigurationObject[] children = synchronizedCo.getChildren("a*b");

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
        defaultco = new DefaultConfigurationObject(NAME);
        synchronizedCo = new SynchronizedConfigurationObject(defaultco);
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("child");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("aab");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("ab");
        synchronizedCo.addChild(child1);
        synchronizedCo.addChild(child2);
        synchronizedCo.addChild(child3);

        String[] children = synchronizedCo.getAllChildrenNames();

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
        defaultco = new DefaultConfigurationObject(NAME);
        synchronizedCo = new SynchronizedConfigurationObject(defaultco);
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("child");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("aab");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("ab");
        synchronizedCo.addChild(child1);
        synchronizedCo.addChild(child2);
        synchronizedCo.addChild(child3);

        ConfigurationObject[] children = synchronizedCo.getAllChildren();

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
        defaultco = new DefaultConfigurationObject(NAME);
        synchronizedCo = new SynchronizedConfigurationObject(defaultco);
        DefaultConfigurationObject child1 = new DefaultConfigurationObject("child");
        DefaultConfigurationObject child2 = new DefaultConfigurationObject("aab");
        DefaultConfigurationObject child3 = new DefaultConfigurationObject("ab");
        synchronizedCo.addChild(child1);
        synchronizedCo.addChild(child2);
        synchronizedCo.addChild(child3);

        ConfigurationObject[] children = synchronizedCo.getAllDescendants();

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
        DefaultConfigurationObject aa = new DefaultConfigurationObject("a");
        DefaultConfigurationObject bb = new DefaultConfigurationObject("b");
        DefaultConfigurationObject cc = new DefaultConfigurationObject("c");
        DefaultConfigurationObject dd = new DefaultConfigurationObject("d");
        DefaultConfigurationObject ee = new DefaultConfigurationObject("e");
        DefaultConfigurationObject ff = new DefaultConfigurationObject("f");
        synchronizedCo.addChild(aa);
        synchronizedCo.addChild(bb);
        aa.addChild(cc);
        bb.addChild(cc);
        cc.addChild(ee);
        cc.addChild(dd);
        ee.addChild(ff);

        ConfigurationObject[] children = synchronizedCo.getAllDescendants();

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
     * The failure test of the method getDescendants, verify the pattern can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getDescendants_failure1() throws Exception {
        try {
            synchronizedCo.getDescendants(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDescendants, verify the pattern can not be invalid regex.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getDescendants_failure2() throws Exception {
        try {
            synchronizedCo.getDescendants("\\");
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
        DefaultConfigurationObject aa = new DefaultConfigurationObject("a");
        DefaultConfigurationObject bb = new DefaultConfigurationObject("b");
        DefaultConfigurationObject cc = new DefaultConfigurationObject("c");
        DefaultConfigurationObject dd = new DefaultConfigurationObject("d");
        DefaultConfigurationObject ee = new DefaultConfigurationObject("e");
        DefaultConfigurationObject ff = new DefaultConfigurationObject("f");
        synchronizedCo.addChild(aa);
        synchronizedCo.addChild(bb);
        aa.addChild(cc);
        bb.addChild(cc);
        cc.addChild(ee);
        cc.addChild(dd);
        ee.addChild(ff);

        ConfigurationObject[] children = synchronizedCo.getDescendants("a");
        assertEquals("1 Descendants should be retrieved.", 1, children.length);
        children = synchronizedCo.getDescendants("[abdy]");
        assertEquals("3 Descendants should be retrieved.", 3, children.length);
    }

    /**
     * <p>
     * The failure test of the method <code>findDescendants</code>, verify the path can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_findDescendants_failure() throws Exception {
        try {
            synchronizedCo.findDescendants(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>findDescendants</code>, no Descendant is found, empty array should be
     * return.<br>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f .
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_findDescendants_accuracy1() throws Exception {
        // create the truee
        DefaultConfigurationObject aa = new DefaultConfigurationObject("a");
        DefaultConfigurationObject bb = new DefaultConfigurationObject("b");
        DefaultConfigurationObject cc = new DefaultConfigurationObject("c");
        DefaultConfigurationObject dd = new DefaultConfigurationObject("d");
        DefaultConfigurationObject ee = new DefaultConfigurationObject("e");
        DefaultConfigurationObject ff = new DefaultConfigurationObject("f");
        synchronizedCo.addChild(aa);
        synchronizedCo.addChild(bb);
        aa.addChild(cc);
        bb.addChild(cc);
        cc.addChild(ee);
        cc.addChild(dd);
        ee.addChild(ff);

        ConfigurationObject[] descendants = synchronizedCo.findDescendants("p");
        assertEquals("No descendant should be retrieved.", 0, descendants.length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>findDescendants</code>, duplicated object can not be exists in the
     * result.<br>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f .
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_findDescendants_accuracy2() throws Exception {
        // create the truee
        DefaultConfigurationObject aa = new DefaultConfigurationObject("a");
        DefaultConfigurationObject bb = new DefaultConfigurationObject("b");
        DefaultConfigurationObject cc = new DefaultConfigurationObject("c");
        DefaultConfigurationObject dd = new DefaultConfigurationObject("d");
        DefaultConfigurationObject ee = new DefaultConfigurationObject("e");
        DefaultConfigurationObject ff = new DefaultConfigurationObject("f");
        synchronizedCo.addChild(aa);
        synchronizedCo.addChild(bb);
        aa.addChild(cc);
        bb.addChild(cc);
        cc.addChild(ee);
        cc.addChild(dd);
        ee.addChild(ff);

        ConfigurationObject[] descendants = synchronizedCo.findDescendants("*/c");
        assertEquals("One descendant should be retrieved.", 1, descendants.length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>findDescendants</code>, the slashes at both end should be trimmed.<br>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f .
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_findDescendants_accuracy3() throws Exception {
        // create the truee
        DefaultConfigurationObject aa = new DefaultConfigurationObject("a");
        DefaultConfigurationObject bb = new DefaultConfigurationObject("b");
        DefaultConfigurationObject cc = new DefaultConfigurationObject("c");
        DefaultConfigurationObject dd = new DefaultConfigurationObject("d");
        DefaultConfigurationObject ee = new DefaultConfigurationObject("e");
        DefaultConfigurationObject ff = new DefaultConfigurationObject("f");
        synchronizedCo.addChild(aa);
        synchronizedCo.addChild(bb);
        aa.addChild(cc);
        bb.addChild(cc);
        cc.addChild(ee);
        cc.addChild(dd);
        ee.addChild(ff);

        ConfigurationObject[] descendants = synchronizedCo.findDescendants("//  */c\\ \\");
        assertEquals("One descendant should be retrieved.", 1, descendants.length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>findDescendants</code>, Continuous slashes should be considered as
     * one.<br>
     * The tree is as follow: name / \ abbx yybzab \ / ppcqv / \ zaddd kkaole \ fz .
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_findDescendants_accuracy4() throws Exception {
        // create the truee
        DefaultConfigurationObject aa = new DefaultConfigurationObject("abbx");
        DefaultConfigurationObject bb = new DefaultConfigurationObject("yybzab");
        DefaultConfigurationObject cc = new DefaultConfigurationObject("ppcqv");
        DefaultConfigurationObject dd = new DefaultConfigurationObject("zaddd");
        DefaultConfigurationObject ee = new DefaultConfigurationObject("kkaole");
        DefaultConfigurationObject ff = new DefaultConfigurationObject("fz");
        synchronizedCo.addChild(aa);
        synchronizedCo.addChild(bb);
        aa.addChild(cc);
        bb.addChild(cc);
        cc.addChild(ee);
        cc.addChild(dd);
        ee.addChild(ff);

        ConfigurationObject[] descendants = synchronizedCo.findDescendants("//  */\\///*c??\\ \\");
        assertEquals("One descendant should be retrieved.", 1, descendants.length);

        descendants = synchronizedCo.findDescendants("//  *ab*/\\///??c*\\\\*a*\\ \\");
        assertEquals("Two descendants should be retrieved.", 2, descendants.length);

        descendants = synchronizedCo.findDescendants("a?*?/?p*v/kk*/??");
        assertEquals("One descendants should be retrieved.", 1, descendants.length);
    }

    /**
     * <p>
     * The failure test of the method <code>deleteDescendants</code>, verify the path can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_deleteDescendants_failure() throws Exception {
        try {
            synchronizedCo.deleteDescendants(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>deleteDescendants</code>.<br>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f<br>
     * Firstly delete a-c, then c-e.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_deleteDescendants_accuracy1() throws Exception {
        // create the truee
        DefaultConfigurationObject aa = new DefaultConfigurationObject("a");
        DefaultConfigurationObject bb = new DefaultConfigurationObject("b");
        DefaultConfigurationObject cc = new DefaultConfigurationObject("c");
        DefaultConfigurationObject dd = new DefaultConfigurationObject("d");
        DefaultConfigurationObject ee = new DefaultConfigurationObject("e");
        DefaultConfigurationObject ff = new DefaultConfigurationObject("f");
        synchronizedCo.addChild(aa);
        synchronizedCo.addChild(bb);
        aa.addChild(cc);
        bb.addChild(cc);
        cc.addChild(ee);
        cc.addChild(dd);
        ee.addChild(ff);

        ConfigurationObject[] descendants = synchronizedCo.deleteDescendants("a/c");
        assertEquals("One descendant should be retrieved.", 1, descendants.length);

        // a-c is delete
        descendants = synchronizedCo.getAllDescendants();
        // still have 6 Descendants
        assertEquals("6 descendants should be retrieved.", 6, descendants.length);
        descendants = aa.getAllDescendants();
        assertEquals("no descendant should be retrieved.", 0, descendants.length);

        // delete c-e
        cc.deleteDescendants("e");
        descendants = synchronizedCo.getAllDescendants();
        // still have 4 Descendants
        assertEquals("4 descendants should be retrieved.", 4, descendants.length);

        descendants = cc.getAllDescendants();
        // only one for c
        assertEquals("1 descendants should be retrieved.", 1, descendants.length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>deleteDescendants</code>.<br>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f<br>
     * Delete a-c, b-c at the same time.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_deleteDescendants_accuracy2() throws Exception {
        // create the truee
        DefaultConfigurationObject aa = new DefaultConfigurationObject("a");
        DefaultConfigurationObject bb = new DefaultConfigurationObject("b");
        DefaultConfigurationObject cc = new DefaultConfigurationObject("c");
        DefaultConfigurationObject dd = new DefaultConfigurationObject("d");
        DefaultConfigurationObject ee = new DefaultConfigurationObject("e");
        DefaultConfigurationObject ff = new DefaultConfigurationObject("f");
        synchronizedCo.addChild(aa);
        synchronizedCo.addChild(bb);
        aa.addChild(cc);
        bb.addChild(cc);
        cc.addChild(ee);
        cc.addChild(dd);
        ee.addChild(ff);

        ConfigurationObject[] descendants = synchronizedCo.deleteDescendants("*/c");
        assertEquals("One descendant should be retrieved.", 1, descendants.length);

        // a-c, b-c at the same time
        descendants = synchronizedCo.getAllDescendants();
        // still have 2 Descendants
        assertEquals("2 descendants should be retrieved.", 2, descendants.length);

        descendants = c.getAllDescendants();
        // only one for c
        assertEquals("3 descendants should be retrieved.", 3, descendants.length);
    }

    /**
     * <p>
     * The failure test of the method setPropertyValue, verify the path can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValueWithPash_failure1() throws Exception {
        try {
            synchronizedCo.setPropertyValue(null, "key", "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValue, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValueWithPash_failure2() throws Exception {
        try {
            synchronizedCo.setPropertyValue("path", null, "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValue, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValueWithPash_failure3() throws Exception {
        try {
            synchronizedCo.setPropertyValue("path", "", "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
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
    public void test_setPropertyValueWithPash_failure4() throws Exception {
        try {
            synchronizedCo.setPropertyValue("path", "  ", "value");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValue, verify the value is invalid for DefaultConfigurationObject,
     * </p>
     *
     * <p>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValueWithPash_failure5() throws Exception {
        try {
            synchronizedCo.setPropertyValue("a", "key", new Object());
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method setPropertyValue, the value is set properly with the path.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValueWithPash_accuracy() throws Exception {
        synchronizedCo.setPropertyValue("a", "key", "value");
        assertTrue("The value should be set to a.", a.containsProperty("key"));
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the path can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValuesWithPash_failure1() throws Exception {
        try {
            synchronizedCo.setPropertyValues(null, "key", new String[] {"value"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValuesWithPash_failure2() throws Exception {
        try {
            synchronizedCo.setPropertyValues("path", null, new String[] {"value"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValuesWithPash_failure3() throws Exception {
        try {
            synchronizedCo.setPropertyValues("path", "", new String[] {"value"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValuesWithPash_failure4() throws Exception {
        try {
            synchronizedCo.setPropertyValues("path", "  ", new String[] {"value"});
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method setPropertyValues, verify the value is invalid for DefaultConfigurationObject.<br>
     * Should have thrown InvalidConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValuesWithPash_failure5() throws Exception {
        try {
            synchronizedCo.setPropertyValues("a", "key", new Object[] {"value", new Object()});
            fail("Should have thrown InvalidConfigurationException.");
        } catch (InvalidConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method setPropertyValues, the value is set properly with the path.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_setPropertyValuesWithPash_accuracy() throws Exception {
        synchronizedCo.setPropertyValue("a", "key", new String[] {"value"});
        assertTrue("The value should be set to a.", a.containsProperty("key"));
    }

    /**
     * <p>
     * The failure test of the method removeProperty, verify the path can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removePropertyWithPash_failure1() throws Exception {
        try {
            synchronizedCo.removeProperty(null, "key");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method removeProperty, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removePropertyWithPash_failure2() throws Exception {
        try {
            synchronizedCo.removeProperty("path", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method removeProperty, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removePropertyWithPash_failure3() throws Exception {
        try {
            synchronizedCo.removeProperty("path", "");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method removeProperty, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeProperty_failure4() throws Exception {
        try {
            synchronizedCo.removeProperty("path", "  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method removeProperty, the value is set properly with the path.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removePropertyWithPash_accuracy() throws Exception {
        synchronizedCo.setPropertyValue("b", "key", new String[] {"value"});
        assertTrue("The value should be set to b.", b.containsProperty("key"));
        synchronizedCo.removeProperty("b", "key");
        // now the key for b is remove
        assertFalse("The value should be removed from b.", b.containsProperty("key"));
    }

    /**
     * <p>
     * The failure test of the method clearProperties, verify the path can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clearProperties_failure1() throws Exception {
        try {
            synchronizedCo.clearProperties(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method clearProperties, the value is set properly with the path.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clearPropertiesWithPash_accuracy() throws Exception {
        synchronizedCo.setPropertyValue("b", "key", new String[] {"value"});
        assertTrue("The value should be set to b.", b.containsProperty("key"));
        synchronizedCo.clearProperties("b");
        // now the key for b is cleared
        assertFalse("The value should be removed from b.", b.containsProperty("key"));
    }

    /**
     * <p>
     * The failure test of the method addChild, verify the path can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChildWithPash_failure1() throws Exception {
        try {
            synchronizedCo.addChild(null, child);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method addChild, verify the child can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChildWithPash_failure2() throws Exception {
        try {
            synchronizedCo.addChild("b", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method addChild, the value is set properly with the path.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_addChildWithPash_accuracy() throws Exception {
        synchronizedCo.addChild("b", child);
        assertTrue("The child should be added to b.", b.containsChild(child.getName()));
    }

    /**
     * <p>
     * The failure test of the method removeChild, verify the path can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChildWithPash_failure1() throws Exception {
        try {
            synchronizedCo.removeProperty(null, "child");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method removeChild, verify the name can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChildWithPash_failure2() throws Exception {
        try {
            synchronizedCo.removeChild("path", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method removeChild, verify the name can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChildWithPash_failure3() throws Exception {
        try {
            synchronizedCo.removeChild("path", "");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method removeChild, verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChildWithPash_failure4() throws Exception {
        try {
            synchronizedCo.removeChild("path", "  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method removeChild, the value is set properly with the path.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_removeChildWithPash_accuracy() throws Exception {
        synchronizedCo.addChild("b", child);
        assertTrue("The child should be added to b.", b.containsChild(child.getName()));

        synchronizedCo.removeChild("b", child.getName());
        // the child is remove now
        assertFalse("The child should be removed from b.", b.containsChild(child.getName()));
    }

    /**
     * <p>
     * The failure test of the method clearChildren, verify the path can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clearChildrenWithPash_failure() throws Exception {
        try {
            synchronizedCo.clearChildren(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method clearChildren, the value is set properly with the path.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clearChildrenWithPash_accuracy() throws Exception {
        synchronizedCo.addChild("b", child);
        assertTrue("The child should be added to b.", b.containsChild(child.getName()));

        synchronizedCo.clearChildren("b");
        // the child is remove now
        assertFalse("The child should be removed from b.", b.containsChild(child.getName()));
    }

    /**
     * <p>
     * The accuracy test of the method Clone.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_clone_accuracy() throws Exception {
        SynchronizedConfigurationObject clone = (SynchronizedConfigurationObject) synchronizedCo.clone();
        assertFalse("The clone should not be the same one as the init.", clone == synchronizedCo);
        // inner object also can not be the same one
        assertConfigurationObjectEquals(clone.getInnerConfigurationObject(), synchronizedCo
            .getInnerConfigurationObject());
    }

    /**
     * <p>
     * The failure test of the method processDescendants, verify the path can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_processDescendants_failure1() throws Exception {
        try {
            synchronizedCo.processDescendants(null, new ProcessorMock());
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the method processDescendants, verify the Processor can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_processDescendants_failure2() throws Exception {
        try {
            synchronizedCo.processDescendants("a", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The accuracy test of the method processDescendants.
     * </p>
     *
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_processDescendants_accuracy() throws Exception {
        ProcessorMock processor = new ProcessorMock();
        synchronizedCo.processDescendants("*", processor);
        assertEquals("two ConfigurationObject should be processed.", 2, processor.getCount());
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
