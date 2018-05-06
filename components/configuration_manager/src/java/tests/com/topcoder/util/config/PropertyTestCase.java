/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the behavior of Property.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Added test cases for methods: getSeparator(), setSeparator(char) and containsProperty(String).</li>
 * </ol>
 * </p>
 *
 * @author WishingBone, sparemax
 * @version 2.2
 */
public class PropertyTestCase extends TestCase {

    /**
     * Tests create root Property.
     */
    public void testCreateRootProperty() {
        Property property = new Property();
        assertNotNull("'Property' should be correct.", property);
        // no further testing necessary
    }

    /**
     * Tests create Property with name.
     */
    public void testCreatePropertyName() {
        Property property = new Property("testprop");
        assertNotNull("'Property' should be correct.", property);
        assertTrue("'Property' should be correct.", property.getName().equals("testprop"));
        assertNull("'Property' should be correct.", property.getValue());
        assertTrue("'Property' should be correct.", property.list().size() == 0);
        // name is null
        try {
            new Property(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // name is empty
        try {
            new Property("   ");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // compound name
        try {
            new Property("test.prop");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
    }

    /**
     * Tests create Property with name and value.
     */
    public void testCreatePropertyNameValue() {
        Property property = new Property("testprop", "testvalue");
        assertNotNull("'Property' should be correct.", property);
        assertTrue("'Property' should be correct.", property.getName().equals("testprop"));
        assertTrue("'Property' should be correct.", property.getValue().equals("testvalue"));
        assertTrue("'Property' should be correct.", property.list().size() == 0);
        // name is null
        try {
            new Property(null, "testvalue");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // value is null
        try {
            new Property("testprop", (String) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // name is empty
        try {
            new Property("   ", "testvalue");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // compound name
        try {
            new Property("test.prop", "testvalue");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
    }

    /**
     * Tests create Property with name and values.
     */
    public void testCreatePropertyNameValues() {
        Property property = new Property("testprop", new String[] {"value1", "value2"});
        assertNotNull("'Property' should be correct.", property);
        assertTrue("'Property' should be correct.", property.getName().equals("testprop"));
        String[] values = property.getValues();
        assertTrue("'Property' should be correct.", values.length == 2);
        assertTrue("'Property' should be correct.", values[0].equals("value1"));
        assertTrue("'Property' should be correct.", values[1].equals("value2"));
        assertTrue("'Property' should be correct.", property.list().size() == 0);
        // name is null
        try {
            new Property(null, new String[] {"value1", "value2"});
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // values is null
        try {
            new Property("testprop", (String[]) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // values contains null entry
        try {
            new Property("testprop", new String[] {"testvalue", null});
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // name is empty
        try {
            new Property("   ", new String[] {"value1", "value2"});
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // compound name
        try {
            new Property("test.prop", new String[] {"value1", "value2"});
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>getSeparator()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     *
     * @since 2.2
     */
    public void test_getSeparator() {
        Property property = new Property();
        char value = '_';
        property.setSeparator(value);

        assertEquals("'getSeparator' should be correct.", value, property.getSeparator());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setSeparator(char separator)</code>.<br>
     * The value should be properly set.
     * </p>
     *
     * @since 2.2
     */
    public void test_setSeparator() {
        Property property = new Property();
        char value = '_';
        property.setSeparator(value);

        assertEquals("'setSeparator' should be correct.", value, TestsHelper.getField(property, "separator"));
    }

    /**
     * Tests addProperty(name, value).
     *
     * @throws Exception to JUnit.
     */
    public void testAddPropertyNameValue() throws Exception {
        Property parent = new Property("parent");
        parent.addProperty("sub", "value");
        assertTrue("'addProperty' should be correct.", parent.containsProperty("sub"));
        Property sub = (Property) parent.list().get(0);
        assertTrue("'addProperty' should be correct.", sub.getValue().equals("value"));
        // name is null
        try {
            parent.addProperty(null, "testvalue");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // value is null
        try {
            parent.addProperty("testprop", (String) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // name is empty
        try {
            parent.addProperty("   ", "testvalue");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // add duplicate
        try {
            parent.addProperty("sub", "value");
            fail("Should have thrown DuplicatePropertyException");
        } catch (DuplicatePropertyException dpe) {
            // Good
        }
    }

    /**
     * Tests addProperty(name, values).
     *
     * @throws Exception to JUnit.
     */
    public void testAddPropertyNameValues() throws Exception {
        Property parent = new Property("parent");
        parent.addProperty("sub", new String[] {"value1", "value2"});
        assertTrue("'addProperty' should be correct.", parent.containsProperty("sub"));
        Property sub = (Property) parent.list().get(0);
        String[] values = sub.getValues();
        assertTrue("'addProperty' should be correct.", values.length == 2);
        assertTrue("'addProperty' should be correct.", values[0].equals("value1"));
        assertTrue("'addProperty' should be correct.", values[1].equals("value2"));
        // name is null
        try {
            parent.addProperty(null, new String[] {"value1", "value2"});
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // values is null
        try {
            parent.addProperty("testprop", (String[]) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // values contains null entry
        try {
            parent.addProperty("testprop", new String[] {"testvalue", null});
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // name is empty
        try {
            parent.addProperty("   ", new String[] {"value1", "value2"});
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // add duplicate
        try {
            parent.addProperty("sub", new String[] {"value1", "value2"});
            fail("Should have thrown DuplicatePropertyException");
        } catch (DuplicatePropertyException dpe) {
            // Good
        }
    }

    /**
     * Tests addProperty(property).
     *
     * @throws Exception to JUnit.
     */
    public void testAddPropertyProperty() throws Exception {
        Property parent = new Property("parent");
        Property sub = new Property("sub");
        parent.addProperty(sub);
        assertTrue("'addProperty' should be correct.", parent.containsProperty("sub"));
        // add null
        try {
            parent.addProperty(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // add duplicate
        try {
            parent.addProperty(sub);
            fail("Should have thrown DuplicatePropertyException");
        } catch (DuplicatePropertyException dpe) {
            // Good
        }
    }

    /**
     * Tests addValue(value).
     */
    public void testAddValueValue() {
        Property property = new Property("testprop");
        assertNull("'addValue' should be correct.", property.getValue());
        property.addValue("value1");
        property.addValue("value2");
        String[] values = property.getValues();
        assertTrue("'addValue' should be correct.", values.length == 2);
        assertTrue("'addValue' should be correct.", values[0].equals("value1"));
        assertTrue("'addValue' should be correct.", values[1].equals("value2"));
        // value is null
        try {
            property.addValue(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * Tests removeProperty(name).
     */
    public void testRemovePropertyName() {
        Property root = new Property();
        root.setProperty("a.b.c", "value");
        root.removeProperty("a.b.c");
        assertNull("'removeProperty' should be correct.", root.getProperty("a.b.c"));
        assertNotNull(root.getProperty("a.b"));
        // remove all
        root.removeProperty("a");
        assertNull("'removeProperty' should be correct.", root.getProperty("a"));
        assertNull("'removeProperty' should be correct.", root.getProperty("a.b"));
        // name is null
        try {
            root.removeProperty(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // name is empty
        try {
            root.removeProperty("   ");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
    }

    /**
     * Tests removeValue(value).
     */
    public void testRemoveValueValue() {
        Property property = new Property("testprop", new String[] {"value1", "value2"});
        property.removeValue("value1");
        assertTrue("'removeValue' should be correct.", property.getValues().length == 1);
        assertTrue("'removeValue' should be correct.", property.getValue().equals("value2"));
        // remove value that does not exist
        property.removeValue("non-exist");
        assertTrue("'removeValue' should be correct.", property.getValues().length == 1);
        assertTrue("'removeValue' should be correct.", property.getValue().equals("value2"));
        // value is null
        try {
            property.removeValue(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * Tests setValue(value).
     */
    public void testSetValueValue() {
        Property property = new Property("testprop", "value1");
        property.setValue("value2");
        assertTrue("'setValue' should be correct.", property.getValue().equals("value2"));
        // clears multiple value
        property.addValue("value3");
        property.setValue("value2");
        assertTrue("'setValue' should be correct.", property.getValues().length == 1);
        assertTrue("'setValue' should be correct.", property.getValue().equals("value2"));
        // value is null
        try {
            property.setValue(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * Tests setValues(values).
     */
    public void testSetValuesValues() {
        Property property = new Property("testprop", "value1");
        property.setValues(new String[] {"value2", "value3"});
        assertTrue("'setValues' should be correct.", property.getValues().length == 2);
        assertTrue("'setValues' should be correct.", property.getValues()[0].equals("value2"));
        assertTrue("'setValues' should be correct.", property.getValues()[1].equals("value3"));
        // values is null
        try {
            property.setValues(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // no value
        try {
            property.setValues(new String[0]);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // values contains null entry
        try {
            property.setValues(new String[] {"value", null});
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * Tests setProperty(name, value).
     */
    public void testSetPropertyNameValue() {
        Property root = new Property();
        root.setProperty("a.b.c", "value");
        // construct the whole chain
        assertNotNull("'setProperty' should be correct.", root.getProperty("a"));
        assertNotNull("'setProperty' should be correct.", root.getProperty("a.b"));
        assertNotNull("'setProperty' should be correct.", root.getProperty("a.b.c"));
        assertTrue("'setProperty' should be correct.", root.getValue("a.b.c").equals("value"));
        // name is null
        try {
            root.setProperty(null, "value");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // name is empty
        try {
            root.setProperty("   ", "value");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // value is null
        try {
            root.setProperty("d", (String) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * Tests setProperty(name, values).
     */
    public void testSetPropertyNameValues() {
        Property root = new Property();
        root.setProperty("a.b.c", new String[] {"value1", "value2"});
        // construct the whole chain
        assertNotNull("'setProperty' should be correct.", root.getProperty("a"));
        assertNotNull("'setProperty' should be correct.", root.getProperty("a.b"));
        assertNotNull("'setProperty' should be correct.", root.getProperty("a.b.c"));
        assertTrue("'setProperty' should be correct.", root.getValues("a.b.c").length == 2);
        assertTrue("'setProperty' should be correct.", root.getValues("a.b.c")[0].equals("value1"));
        assertTrue("'setProperty' should be correct.", root.getValues("a.b.c")[1].equals("value2"));
        // name is null
        try {
            root.setProperty(null, new String[] {"value1", "value2"});
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // name is empty
        try {
            root.setProperty("   ", new String[] {"value1", "value2"});
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // value is null
        try {
            root.setProperty("d", (String[]) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>containsProperty(String property)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @since 2.2
     */
    public void test_containsProperty() {
        Property property = new Property();
        property.setProperty("a.b.c", "value");

        assertTrue("'containsProperty' should be correct.", property.containsProperty("a"));
        assertTrue("'containsProperty' should be correct.", property.containsProperty("a.b"));
        assertTrue("'containsProperty' should be correct.", property.containsProperty("a.b.c"));

        assertFalse("'containsProperty' should be correct.", property.containsProperty("a.b.c.d"));
        assertFalse("'containsProperty' should be correct.", property.containsProperty(" "));
    }

    /**
     * <p>
     * Failure test for the method <code>containsProperty(String property)</code>.<br>
     * <code>NullPointerException</code> is expected.
     * </p>
     *
     * @since 2.2
     */
    public void test_containsProperty_propertyNull() {
        Property property = new Property();
        property.setProperty("a.b.c", "value");

        try {
            property.containsProperty(null);

            fail("NullPointerException is expected.");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * Tests containsValue(value).
     */
    public void testContainsValueValue() {
        Property property = new Property("testprop", new String[] {"value1", "value2"});
        assertTrue("'containsValue' should be correct.", property.containsValue("value1"));
        assertTrue("'containsValue' should be correct.", property.containsValue("value2"));
        assertFalse("'containsValue' should be correct.", property.containsValue("non-exist"));
        // value is null
        try {
            property.containsValue(null);
            fail("Should have thrwon NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * Tests getValue().
     */
    public void testGetValue() {
        // single
        Property property = new Property("testprop", "value1");
        assertTrue("'getValue' should be correct.", property.getValue().equals("value1"));
        // multiple, return the first
        property.addValue("value2");
        assertTrue("'getValue' should be correct.", property.getValue().equals("value1"));
        // empty, return null
        property.removeValue("value1");
        property.removeValue("value2");
        assertNull("'getValue' should be correct.", property.getValue());
    }

    /**
     * Tests getValue(name).
     */
    public void testGetValueName() {
        Property root = new Property();
        root.setProperty("a", "value1");
        root.setProperty("a.b.c", "value2");
        assertTrue("'getValue' should be correct.", root.getValue("a").equals("value1"));
        assertTrue("'getValue' should be correct.", root.getValue("a.b.c").equals("value2"));
        assertNull("'getValue' should be correct.", root.getValue("a.b"));
        assertNull("'getValue' should be correct.", root.getValue("b.c"));
        // name is null
        try {
            root.getValue(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // name is empty
        try {
            root.getValue("   ");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
    }

    /**
     * Tests getValues(name).
     */
    public void testGetValuesName() {
        Property root = new Property();
        root.setProperty("a", "value1");
        root.setProperty("a.b.c", new String[] {"value2", "value3"});
        String[] values = root.getValues("a");
        assertTrue("'getValues' should be correct.", values.length == 1);
        assertTrue("'getValues' should be correct.", values[0].equals("value1"));
        values = root.getValues("a.b.c");
        assertTrue("'getValues' should be correct.", values.length == 2);
        assertTrue("'getValues' should be correct.", values[0].equals("value2"));
        assertTrue("'getValues' should be correct.", values[1].equals("value3"));
        assertNull("'getValues' should be correct.", root.getValues("a.b"));
        assertNull("'getValues' should be correct.", root.getValues("b.c"));
        // name is null
        try {
            root.getValues(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // name is empty
        try {
            root.getValues("   ");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
    }

    /**
     * Tests getValues().
     */
    public void testGetValues() {
        // single
        Property property = new Property("testprop", "value1");
        String[] values = property.getValues();
        assertTrue("'getValues' should be correct.", values.length == 1);
        assertTrue("'getValues' should be correct.", values[0].equals("value1"));
        // multiple
        property.addValue("value2");
        values = property.getValues();
        assertTrue("'getValues' should be correct.", values.length == 2);
        assertTrue("'getValues' should be correct.", values[0].equals("value1"));
        assertTrue("'getValues' should be correct.", values[1].equals("value2"));
        // empty, return null
        property.removeValue("value1");
        property.removeValue("value2");
        assertNull("'getValues' should be correct.", property.getValues());
    }

    /**
     * Tests getProperty(name).
     */
    public void testGetPropertyName() {
        Property root = new Property();
        root.setProperty("a.b.c", "value");
        Property a = root.getProperty("a");
        Property b = root.getProperty("a.b");
        Property c = root.getProperty("a.b.c");
        assertNotNull("'getProperty' should be correct.", a);
        assertNotNull("'getProperty' should be correct.", b);
        assertNotNull("'getProperty' should be correct.", c);
        assertNull("'getProperty' should be correct.", root.getProperty("b.c"));
        assertNull("'getProperty' should be correct.", root.getProperty("a.b.d"));
        // name is null
        try {
            root.getProperty(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * Tests list().
     *
     * @throws Exception to JUnit.
     */
    public void testList() throws Exception {
        Property property = new Property("testprop");
        property.addProperty("sub1", "value1");
        property.addProperty("sub2", "value2");
        List<Property> subs = property.list();
        assertTrue("'list' should be correct.", subs.size() == 2);
        Property sub = (Property) subs.get(0);
        assertTrue("'list' should be correct.", sub.getName().equals("sub1"));
        assertTrue("'list' should be correct.", sub.getValue().equals("value1"));
        sub = (Property) subs.get(1);
        assertTrue("'list' should be correct.", sub.getName().equals("sub2"));
        assertTrue("'list' should be correct.", sub.getValue().equals("value2"));
    }

    /**
     * Tests propertyNames().
     *
     * @throws Exception to JUnit.
     */
    public void testPropertyNames() throws Exception {
        Property property = new Property("testprop");
        property.addProperty("sub1", "value1");
        property.addProperty("sub2", "value2");
        Enumeration<String> enu = property.propertyNames();
        assertTrue("'propertyNames' should be correct.", enu.hasMoreElements());
        assertTrue("'propertyNames' should be correct.", enu.nextElement().equals("sub1"));
        assertTrue("'propertyNames' should be correct.", enu.hasMoreElements());
        assertTrue("'propertyNames' should be correct.", enu.nextElement().equals("sub2"));
        assertFalse("'propertyNames' should be correct.", enu.hasMoreElements());
    }

    /**
     * Tests find(name).
     */
    public void testFindName() {
        Property root = new Property();
        root.setProperty("a.b.c", "value");
        Property a = root.find("a");
        Property b = root.find("a.b");
        Property c = root.find("a.b.c");
        assertNotNull("'find' should be correct.", a);
        assertNotNull("'find' should be correct.", b);
        assertNotNull("'find' should be correct.", c);
        assertNull("'find' should be correct.", root.find("b.c"));
        assertNull("'find' should be correct.", root.find("a.b.d"));
    }

    /**
     * Test clone().
     */
    public void testClone() {
        Property root = new Property();
        root.setProperty("a.b.c", "value");
        Property copy = (Property) root.clone();
        // it is not shallow copy
        assertNotNull("'clone' should be correct.", copy.getProperty("a"));
        assertNotNull("'clone' should be correct.", copy.getProperty("a.b"));
        assertNotNull("'clone' should be correct.", copy.getProperty("a.b.c"));
        assertTrue("'clone' should be correct.", root.getProperty("a") != copy.getProperty("a"));
        assertTrue("'clone' should be correct.", root.getProperty("a.b") != copy.getProperty("a.b"));
        assertTrue("'clone' should be correct.", root.getProperty("a.b.c") != copy.getProperty("a.b.c"));
    }

    /**
     * Tests addComment(comment).
     */
    public void testAddCommentComment() {
        Property property = new Property("testprop");
        property.addComment("comment1");
        property.addComment("comment2");
        List<String> comments = property.getComments();
        assertTrue("'addComment' should be correct.", comments.size() == 2);
        assertTrue("'addComment' should be correct.", comments.get(0).equals("comment1"));
        assertTrue("'addComment' should be correct.", comments.get(1).equals("comment2"));
        // comment is null
        try {
            property.addComment(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * Tests setComments(comments).
     */
    public void testSetCommentsComments() {
        Property property = new Property("testprop");
        List<String> comments = new ArrayList<String>();
        comments.add("comment1");
        comments.add("comment2");
        property.setComments(comments);
        comments.clear();
        comments = property.getComments();
        assertTrue("'setComments' should be correct.", comments.size() == 2);
        assertTrue("'setComments' should be correct.", comments.get(0).equals("comment1"));
        assertTrue("'setComments' should be correct.", comments.get(1).equals("comment2"));
        // can set null, means clear
        property.setComments(null);
        assertNull("'setComments' should be correct.", property.getComments());
        // contains non String instance
        comments.clear();
        comments.add(null);
        try {
            property.setComments(comments);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
    }

    /**
     * Tests getComments().
     */
    public void testGetComments() {
        // defaultly, return null
        Property property = new Property("testprop");
        assertNull("'getComments' should be correct.", property.getComments());
        property.addComment("comment1");
        property.addComment("comment2");
        List<String> comments = property.getComments();
        assertTrue("'getComments' should be correct.", comments.size() == 2);
        assertTrue("'getComments' should be correct.", comments.get(0).equals("comment1"));
        assertTrue("'getComments' should be correct.", comments.get(1).equals("comment2"));
    }

    /**
     * Tests getName().
     */
    public void testGetName() {
        Property property = new Property("testprop");
        assertTrue("'getName' should be correct.", property.getName().equals("testprop"));
    }

    /**
     * Tests equals(obj).
     */
    public void testEquals() {
        Property p1 = new Property("testprop", "value1");
        Property p2 = new Property("testprop", "value2");
        Property p3 = new Property("otherprop", "value3");
        assertTrue("'equals' should be correct.", p1.equals(p2));
        assertFalse("'equals' should be correct.", p1.equals(p3));
        assertFalse("'equals' should be correct.", p1.equals(new Object()));
        assertFalse("'equals' should be correct.", p1.equals(null));
    }

    /**
     * Tests hashCode().
     */
    public void testHashCode() {
        Property p1 = new Property("testprop", "value1");
        Property p2 = new Property("testprop", "value2");
        assertTrue("'hashCode' should be correct.", p1.hashCode() == p2.hashCode());
    }

    /**
     * Tests getProperties(key, defaultValue).
     */
    public void testGetPropertiesKeyDefaultValue() {
        Property root = new Property();
        root.setProperty("a.b.c", new String[] {"value1", "value2"});
        String[] values = root.getProperties("a.b.c", "default");
        assertTrue("'getProperties' should be correct.", values.length == 2);
        assertTrue("'getProperties' should be correct.", values[0].equals("value1"));
        assertTrue("'getProperties' should be correct.", values[1].equals("value2"));
        values = root.getProperties("a.b", "default");
        assertTrue("'getProperties' should be correct.", values.length == 1);
        assertTrue("'getProperties' should be correct.", values[0].equals("default"));
        values = root.getProperties("non-exist", "default");
        assertTrue("'getProperties' should be correct.", values.length == 1);
        assertTrue("'getProperties' should be correct.", values[0].equals("default"));
        // key is null
        try {
            root.getProperties(null, "default");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is empty
        try {
            root.getProperties("   ", "default");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // defaultValue is null
        try {
            root.getProperties("a.b.c", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }
}
