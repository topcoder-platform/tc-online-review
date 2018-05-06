/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import junit.framework.TestCase;

/**
 * <p>
 * The unit test of the class <code>SynchronizedConfigurationObject</code>.
 * </p>
 *
 * <p>
 * It's split due to the file length is too long. See SynchronizedConfigurationObjectTestsTwo.
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
public class SynchronizedConfigurationObjectTestsOne extends TestCase {
    /**The Synchronized instance for test.*/
    private SynchronizedConfigurationObject synchronizedCo;
    /**The DefaultConfigurationObject instance for test.*/
    private DefaultConfigurationObject defaultco;
    /**
     * <p>
     * Set up the TemplateConfigurationObject test case, create the TemplateConfigurationObject instance for test.<br>
     * The tree is as follow: name / \ a b \ / c / \ d e \ f
     * </p>
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        defaultco = new DefaultConfigurationObject("name");
        synchronizedCo = new SynchronizedConfigurationObject(defaultco);
    }
    /**
     * <p>
     * The accuracy test of the constructor.
     * </p>
     */
    public void test_ctor_accuracy() {
        assertNotNull("failed to create SynchronizedConfigurationObject.", synchronizedCo);
    }
    /**
     * <p>
     * The failure test of the constructor, verify the inner object can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     */
    public void test_ctor_failure() {
        try {
            new SynchronizedConfigurationObject(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
    /**
     * Test inheritance of SynchronizedConfigurationObject, it is implements ConfigurationObject interface.
     */
    public void test_inheritance() {
        assertTrue("SynchronizedConfigurationObject should implements ConfigurationObject interface.",
            synchronizedCo instanceof ConfigurationObject);
    }
    /**
     * The accuracy test of the method <code>getInnerConfigurationObject</code>.
     */
    public void test_getInnerConfigurationObject_accuracy() {
        assertTrue("The innerObject is not retrieved properly.", defaultco == synchronizedCo
            .getInnerConfigurationObject());
    }
    /**
     * The accuracy test of the method <code>getName</code>.
     * @throws Exception
     *             to JUnit
     */
    public void test_getName_accuracy() throws Exception {
        assertEquals("The name should be the one set in constructor.", "name", synchronizedCo.getName());
    }
    /**
     * <p>
     * The failure test of the method containsProperty, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_failure1() throws Exception {
        try {
            synchronizedCo.containsProperty(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method containsProperty, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_failure2() throws Exception {
        try {
            synchronizedCo.containsProperty("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method containsProperty, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_failure3() throws Exception {
        try {
            synchronizedCo.containsProperty("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>containsProperty</code>, if the value is an empty array, also return true.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertTrue("The key should be contained.", synchronizedCo.containsProperty("key"));
    }
    /**
     * <p>
     * The accuracy test of the method <code>containsProperty</code>, if the value is set by setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_accuracy2() throws Exception {
        synchronizedCo.setPropertyValues("key", new String[] {"v1"});
        assertTrue("The key should be contained.", synchronizedCo.containsProperty("key"));
    }
    /**
     * <p>
     * The accuracy test of the method <code>containsProperty</code>, if the value is set by setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "v1");
        assertTrue("The key should be contained.", synchronizedCo.containsProperty("key"));
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue1_failure1() throws Exception {
        try {
            synchronizedCo.getPropertyValue(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue1_failure2() throws Exception {
        try {
            synchronizedCo.getPropertyValue("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue1_failure3() throws Exception {
        try {
            synchronizedCo.getPropertyValue("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue</code>, if the value is an empty array, also null is
     * returned.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue1_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", synchronizedCo.getPropertyValue("key"));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue</code>.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue1_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.", synchronizedCo.getPropertyValue("key"));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue</code>, if the value is by setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue1_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "v1");
        assertEquals("The value set should be retrieved.", "v1", synchronizedCo.getPropertyValue("key"));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue</code>, if the value is by setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue1_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new String[] {"v3", "v1"});
        assertEquals("The first value of the array should be retrieved.", "v3", synchronizedCo
            .getPropertyValue("key"));
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;), verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue2_failure1() throws Exception {
        try {
            synchronizedCo.getPropertyValue(null, Object.class);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;), verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue2_failure2() throws Exception {
        try {
            synchronizedCo.getPropertyValue("", Object.class);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;), verify the key can not be full of
     * space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue2_failure3() throws Exception {
        try {
            synchronizedCo.getPropertyValue("  ", Object.class);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;), verify the value cannot be casted to
     * the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue2_failure4() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1});

        try {
            synchronizedCo.getPropertyValue("key", String.class);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;), verify the clazz can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue2_failure5() throws Exception {
        try {
            synchronizedCo.getPropertyValue("key", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue(String, Class&lt;T&gt;)</code>, if the value is an
     * empty array, also null is returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue2_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", synchronizedCo.getPropertyValue("key", Object.class));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue(String, Class&lt;T&gt;)</code>.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue2_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.", synchronizedCo.getPropertyValue("key", Object.class));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue(String, Class&lt;T&gt;)</code>, if the value is set
     * by setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue2_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "v1");
        assertEquals("The value set should be retrieved.", "v1", synchronizedCo.getPropertyValue("key", Object.class));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue(String, Class&lt;T&gt;)</code>, if the value is set
     * by setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue2_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new String[] {"v3", "v1"});
        assertEquals("The first value of the array should be retrieved.", "v3", synchronizedCo.getPropertyValue(
            "key", Object.class));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue(String, Class&lt;T&gt;)</code>, if the value is
     * casted to the desired type.
     * </p>
     * @throws Exception
     *             to JUnit
     *
     * @since 1.1
     */
    public void test_getPropertyValue2_accuracy5() throws Exception {
        synchronizedCo.setPropertyValue("key", 1);
        assertEquals("The value set should be retrieved.",
            1, synchronizedCo.getPropertyValue("key", Integer.class).intValue());
    }
    /**
     * <p>
     * The failure test of the method getPropertyValuesCount, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_failure1() throws Exception {
        try {
            synchronizedCo.getPropertyValuesCount(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValuesCount, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_failure2() throws Exception {
        try {
            synchronizedCo.getPropertyValuesCount("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValuesCount, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_failure3() throws Exception {
        try {
            synchronizedCo.getPropertyValuesCount("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValuesCount</code>, if the value is an empty array, 0 is
     * returned.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertEquals("No value exists, 0 should be return.", 0, synchronizedCo.getPropertyValuesCount("key"));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValuesCount</code>, if there is not value, -1 is returned.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_accuracy2() throws Exception {
        assertEquals("Null value exists, -1 should be return.", -1, synchronizedCo.getPropertyValuesCount("key"));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValuesCount</code>, if the value is set by
     * setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "v1");
        assertEquals("One value set should be retrieved.", 1, synchronizedCo.getPropertyValuesCount("key"));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValuesCount</code>, if the value is set by
     * setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new String[] {"v3", "v1"});
        assertEquals("Two values should be retrieved.", 2, synchronizedCo.getPropertyValuesCount("key"));
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String), verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues1_failure1() throws Exception {
        try {
            synchronizedCo.getPropertyValues(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String), verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues1_failure2() throws Exception {
        try {
            synchronizedCo.getPropertyValues("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String), verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues1_failure3() throws Exception {
        try {
            synchronizedCo.getPropertyValues("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String)</code>, if the value is an empty array,
     * empty array is returned.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues1_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertEquals("No value exists, empty array should be return.", 0,
            synchronizedCo.getPropertyValues("key").length);
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String)</code>, if there is not value, null is
     * returned.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues1_accuracy2() throws Exception {
        assertNull("Null value exists, null should be return.", synchronizedCo.getPropertyValues("key"));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String)</code>, if the value is set by
     * setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues1_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "v1");
        assertEquals("One value set should be retrieved.", 1, synchronizedCo.getPropertyValues("key").length);
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String)</code>, if the value is set by
     * setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues1_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new String[] {"v3", "v1"});
        assertEquals("Two values should be retrieved.", 2, synchronizedCo.getPropertyValues("key").length);
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;), verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_failure1() throws Exception {
        try {
            synchronizedCo.getPropertyValues(null, Object.class);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;), verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_failure2() throws Exception {
        try {
            synchronizedCo.getPropertyValues("", Object.class);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;), verify the key can not be full of
     * space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_failure3() throws Exception {
        try {
            synchronizedCo.getPropertyValues("  ", Object.class);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;), verify the clazz can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_failure4() throws Exception {
        try {
            synchronizedCo.getPropertyValues("key", null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;), verify the value cannot be casted to
     * the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_failure5() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1, "str"});

        try {
            synchronizedCo.getPropertyValues("key", String.class);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String, Class&lt;T&gt;)</code>, if the value is an
     * empty array, empty array is returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertEquals("No value exists, empty array should be return.", 0,
            synchronizedCo.getPropertyValues("key", Object.class).length);
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String, Class&lt;T&gt;)</code>, if there is no
     * value, null is returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_accuracy2() throws Exception {
        assertNull("Null value exists, null should be return.", synchronizedCo.getPropertyValues("key", Object.class));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String, Class&lt;T&gt;)</code>, if the value is set
     * by setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "v1");
        assertEquals("One value set should be retrieved.",
            1, synchronizedCo.getPropertyValues("key", Object.class).length);
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String, Class&lt;T&gt;)</code>, if the value is set
     * by setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new String[] {"v3", "v1"});
        assertEquals("Two values should be retrieved.",
            2, synchronizedCo.getPropertyValues("key", String.class).length);
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String, Class&lt;T&gt;)</code>, if the value is casted
     * to the desired type.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues2_accuracy5() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1, 2, null});

        Integer[] res = synchronizedCo.getPropertyValues("key", Integer.class);

        assertEquals("Three values should be retrieved.", 3, res.length);
        assertEquals("Correct values should be retrieved.", 1, res[0].intValue());
        assertEquals("Correct values should be retrieved.", 2, res[1].intValue());
        assertNull("Correct values should be retrieved.", res[2]);
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;, boolean), verify the key can not be
     * null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_failure1() throws Exception {
        try {
            synchronizedCo.getPropertyValue(null, Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;, boolean), verify the key can not be
     * empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_failure2() throws Exception {
        try {
            synchronizedCo.getPropertyValue("", Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;, boolean), verify the key can not be
     * full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_failure3() throws Exception {
        try {
            synchronizedCo.getPropertyValue("  ", Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;, boolean), verify the value cannot be
     * casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_failure4() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1});

        try {
            synchronizedCo.getPropertyValue("key", String.class, true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;, boolean), verify the value cannot be
     * casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_failure5() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1});

        try {
            synchronizedCo.getPropertyValue("key", String.class, true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValue(String, Class&lt;T&gt;, boolean), verify the clazz can not be
     * null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_failure6() throws Exception {
        try {
            synchronizedCo.getPropertyValue("key", null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue(String, Class&lt;T&gt;, boolean)</code>, if the value
     * is an empty array, also null is returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.",
            synchronizedCo.getPropertyValue("key", Object.class, false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue(String, Class&lt;T&gt;, boolean)</code>.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.",
            synchronizedCo.getPropertyValue("key", Object.class, false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue(String, Class&lt;T&gt;, boolean)</code>, if the value
     * is set by setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "v1");
        assertEquals("The value set should be retrieved.",
            "v1", synchronizedCo.getPropertyValue("key", Object.class, true));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue(String, Class&lt;T&gt;, boolean)</code>, if the value
     * is set by setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue3_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new String[] {"v3", "v1"});
        assertEquals("The first value of the array should be retrieved.", "v3", synchronizedCo.getPropertyValue(
            "key", Object.class, true));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue(String, Class&lt;T&gt;, boolean)</code>, if the value is
     * casted to the desired type.
     * </p>
     * @throws Exception
     *             to JUnit
     *
     * @since 1.1
     */
    public void test_getPropertyValue3_accuracy5() throws Exception {
        synchronizedCo.setPropertyValue("key", 1);
        assertEquals("The value set should be retrieved.", 1, synchronizedCo.getPropertyValue("key", Integer.class,
            true).intValue());
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;, boolean), verify the key can not be
     * null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_failure1() throws Exception {
        try {
            synchronizedCo.getPropertyValues(null, Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;, boolean), verify the key can not be
     * empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_failure2() throws Exception {
        try {
            synchronizedCo.getPropertyValues("", Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;, boolean), verify the key can not be
     * full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_failure3() throws Exception {
        try {
            synchronizedCo.getPropertyValues("  ", Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;, boolean), verify the clazz can not be
     * null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_failure4() throws Exception {
        try {
            synchronizedCo.getPropertyValues("key", null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;, boolean), verify the value cannot be
     * casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_failure5() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1, "str"});

        try {
            synchronizedCo.getPropertyValues("key", String.class, true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getPropertyValues(String, Class&lt;T&gt;, boolean), verify the key is required,
     * but missing.<br>
     * Should have thrown PropertyNotFoundException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_failure6() throws Exception {
        try {
            synchronizedCo.getPropertyValues("key", String.class, true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String, Class&lt;T&gt;, boolean)</code>, if the
     * value is an empty array, empty array is returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertEquals("No value exists, empty array should be return.", 0, synchronizedCo.getPropertyValues("key",
            Object.class, false).length);
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String, Class&lt;T&gt;, boolean)</code>, if there is
     * no value, null is returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_accuracy2() throws Exception {
        assertNull("Null value exists, null should be return.", synchronizedCo.getPropertyValues("key", Object.class,
            false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String, Class&lt;T&gt;, boolean)</code>, if the
     * value is set by setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "v1");
        assertEquals("One value set should be retrieved.", 1,
            synchronizedCo.getPropertyValues("key", Object.class, true).length);
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String, Class&lt;T&gt;, boolean)</code>, if the
     * value is set by setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new String[] {"v3", "v1"});
        assertEquals("Two values should be retrieved.", 2,
            synchronizedCo.getPropertyValues("key", String.class, true).length);
    }
    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues(String, Class&lt;T&gt;, boolean)</code>, if the
     * value is casted to the desired type.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues3_accuracy5() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1, 2, null});

        Integer[] res = synchronizedCo.getPropertyValues("key", Integer.class, true);

        assertEquals("Three values should be retrieved.", 3, res.length);
        assertEquals("Correct values should be retrieved.", 1, res[0].intValue());
        assertEquals("Correct values should be retrieved.", 2, res[1].intValue());
        assertNull("Correct values should be retrieved.", res[2]);
    }
    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure1() throws Exception {
        try {
            synchronizedCo.getIntegerProperty(null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure2() throws Exception {
        try {
            synchronizedCo.getIntegerProperty("", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure3() throws Exception {
        try {
            synchronizedCo.getIntegerProperty("  ", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the key is required, but missing.<br>
     * Should have thrown PropertyNotFoundException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure4() throws Exception {
        try {
            synchronizedCo.getIntegerProperty("key", true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the value cannot be casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure5() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {"str"});

        try {
            synchronizedCo.getIntegerProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the value cannot be casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure6() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1L});

        try {
            synchronizedCo.getIntegerProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getIntegerProperty</code>, if the value is an empty array, also null is
     * returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", synchronizedCo.getIntegerProperty("key", false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getIntegerProperty</code>, if the value does not exist, null is
     * returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.", synchronizedCo.getIntegerProperty("key", false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getIntegerProperty</code>, if the value is set by setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "1");
        assertEquals("The value set should be retrieved.",
            1, synchronizedCo.getIntegerProperty("key", true).intValue());
    }
    /**
     * <p>
     * The accuracy test of the method <code>getIntegerProperty</code>, if the value is set by setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new Integer[] {1, 2});
        assertEquals("The first value of the array should be retrieved.",
            1, synchronizedCo.getIntegerProperty("key", true).intValue());
    }
    /**
     * <p>
     * The failure test of the method getLongProperty, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure1() throws Exception {
        try {
            synchronizedCo.getLongProperty(null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getLongProperty, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure2() throws Exception {
        try {
            synchronizedCo.getLongProperty("", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getLongProperty, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure3() throws Exception {
        try {
            synchronizedCo.getLongProperty("  ", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getLongProperty, verify the key is required, but missing.<br>
     * Should have thrown PropertyNotFoundException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure4() throws Exception {
        try {
            synchronizedCo.getLongProperty("key", true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getLongProperty, verify the value cannot be casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure5() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {"str"});

        try {
            synchronizedCo.getLongProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getLongProperty, verify the value cannot be casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure6() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1});

        try {
            synchronizedCo.getLongProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getLongProperty</code>, if the value is an empty array, also null is
     * returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", synchronizedCo.getLongProperty("key", false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getLongProperty</code>, if the value does not exist, null is
     * returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.", synchronizedCo.getLongProperty("key", false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getLongProperty</code>, if the value is set by setPropertyValue.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "1");
        assertEquals("The value set should be retrieved.", 1L, synchronizedCo.getLongProperty("key", true).longValue());
    }
    /**
     * <p>
     * The accuracy test of the method <code>getLongProperty</code>, if the value is set by setPropertyValues.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new Long[] {1L, 2L});
        assertEquals("The first value of the array should be retrieved.",
            1L, synchronizedCo.getLongProperty("key", true).longValue());
    }
    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure1() throws Exception {
        try {
            synchronizedCo.getDoubleProperty(null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure2() throws Exception {
        try {
            synchronizedCo.getDoubleProperty("", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure3() throws Exception {
        try {
            synchronizedCo.getDoubleProperty("  ", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the key is required, but missing.<br>
     * Should have thrown PropertyNotFoundException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure4() throws Exception {
        try {
            synchronizedCo.getDoubleProperty("key", true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the value cannot be casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure5() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {"str"});

        try {
            synchronizedCo.getDoubleProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the value cannot be casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure6() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1L});

        try {
            synchronizedCo.getDoubleProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getDoubleProperty</code>, if the value is an empty array, also null is
     * returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", synchronizedCo.getDoubleProperty("key", false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getDoubleProperty</code>, if the value does not exist, null is
     * returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.", synchronizedCo.getDoubleProperty("key", false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getDoubleProperty</code>, if the value is set by setPropertyValue.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "1");
        assertEquals("The value set should be retrieved.",
            1D, synchronizedCo.getDoubleProperty("key", true).doubleValue(), 0.01);
    }
    /**
     * <p>
     * The accuracy test of the method <code>getDoubleProperty</code>, if the value is set by setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new Double[] {1D, 2D});
        assertEquals("The first value of the array should be retrieved.",
            1D, synchronizedCo.getDoubleProperty("key", true).doubleValue());
    }
    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure1() throws Exception {
        try {
            synchronizedCo.getDateProperty(null, "yyyy-MM-dd", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure2() throws Exception {
        try {
            synchronizedCo.getDateProperty("", "yyyy-MM-dd", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure3() throws Exception {
        try {
            synchronizedCo.getDateProperty("  ", "yyyy-MM-dd", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDateProperty, verify the format can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure4() throws Exception {
        try {
            synchronizedCo.getDateProperty("key", null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure5() throws Exception {
        try {
            synchronizedCo.getDateProperty("key", "", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure6() throws Exception {
        try {
            synchronizedCo.getDateProperty("key", "  ", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key is required, but missing.<br>
     * Should have thrown PropertyNotFoundException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure7() throws Exception {
        try {
            synchronizedCo.getDateProperty("key", "yyyy-MM-dd", true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDateProperty, verify the value cannot be casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure8() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {"str"});

        try {
            synchronizedCo.getDateProperty("key", "yyyy-MM-dd", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getDateProperty, verify the value cannot be casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure9() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1L});

        try {
            synchronizedCo.getDateProperty("key", "yyyy-MM-dd", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getDateProperty</code>, if the value is an empty array, also null is
     * returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.",
            synchronizedCo.getDateProperty("key", "yyyy-MM-dd", false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getDateProperty</code>, if the value does not exist, null is
     * returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.",
            synchronizedCo.getDateProperty("key", "yyyy-MM-dd", false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getDateProperty</code>, if the value is set by setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", "2011-01-01");
        assertEquals("The value set should be retrieved.", "2011-01-01",
            new SimpleDateFormat("yyyy-MM-dd").format(synchronizedCo.getDateProperty("key", "yyyy-MM-dd", true)));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getDateProperty</code>, if the value is set by setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_accuracy4() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2011, 0, 1);
        Date date1 = calendar.getTime();
        calendar.clear();
        calendar.set(2011, 0, 2);
        Date date2 = calendar.getTime();

        synchronizedCo.setPropertyValues("key", new Date[] {date1, date2});
        assertEquals("The first value of the array should be retrieved.", "2011-01-01",
            new SimpleDateFormat("yyyy-MM-dd").format(synchronizedCo.getDateProperty("key", "yyyy-MM-dd", true)));
    }
    /**
     * <p>
     * The failure test of the method getClassProperty, verify the key can not be null.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure1() throws Exception {
        try {
            synchronizedCo.getClassProperty(null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getClassProperty, verify the key can not be empty.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure2() throws Exception {
        try {
            synchronizedCo.getClassProperty("", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getClassProperty, verify the key can not be full of space.<br>
     * Should have thrown IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure3() throws Exception {
        try {
            synchronizedCo.getClassProperty("  ", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getClassProperty, verify the key is required, but missing.<br>
     * Should have thrown PropertyNotFoundException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure4() throws Exception {
        try {
            synchronizedCo.getClassProperty("key", true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getClassProperty, verify the value cannot be casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure5() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {"str"});

        try {
            synchronizedCo.getClassProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The failure test of the method getClassProperty, verify the value cannot be casted to the desired type.<br>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure6() throws Exception {
        synchronizedCo.setPropertyValues("key", new Object[] {1L});

        try {
            synchronizedCo.getClassProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }
    /**
     * <p>
     * The accuracy test of the method <code>getClassProperty</code>, if the value is an empty array, also null is
     * returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_accuracy1() throws Exception {
        synchronizedCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", synchronizedCo.getClassProperty("key", false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getClassProperty</code>, if the value does not exist, null is
     * returned.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.", synchronizedCo.getClassProperty("key", false));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getClassProperty</code>, if the value is set by setPropertyValue.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_accuracy3() throws Exception {
        synchronizedCo.setPropertyValue("key", String.class.getName());
        assertEquals("The value set should be retrieved.",
            String.class, synchronizedCo.getClassProperty("key", true));
    }
    /**
     * <p>
     * The accuracy test of the method <code>getClassProperty</code>, if the value is set by setPropertyValues.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_accuracy4() throws Exception {
        synchronizedCo.setPropertyValues("key", new Class<?>[] {String.class, Integer.class});
        assertEquals("The first value of the array should be retrieved.",
            String.class, synchronizedCo.getClassProperty("key", true));
    }
}
