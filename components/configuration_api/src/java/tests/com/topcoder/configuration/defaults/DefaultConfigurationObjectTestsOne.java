/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.defaults;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import com.topcoder.configuration.PropertyNotFoundException;
import com.topcoder.configuration.PropertyTypeMismatchException;
import com.topcoder.configuration.TemplateConfigurationObject;

/**
 * <p>
 * The unit test of the class <code>DefaultConfigurationObject</code>.
 * </p>
 *
 * <p>
 * It's split due to the file length is too long. See DefaultConfigurationObjectTestsTwo.
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
public class DefaultConfigurationObjectTestsOne extends TestCase {
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
     * The accuracy test of the constructor.
     */
    public void test_ctor_accuracy() {
        assertNotNull("Failed to create DefaultConfigurationObject with name.", defaultCo);
    }

    /**
     * Test inheritance of DefaultConfigurationObject, it is implements ConfigurationObject interface.
     */
    public void test_inheritance() {
        assertTrue("DefaultConfigurationObject should extends TemplateConfigurationObject.",
            defaultCo instanceof TemplateConfigurationObject);
    }

    /**
     * <p>
     * The failure test of the constructor, verify the name can not be null,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     */
    public void test_ctor_failure1() {
        try {
            new DefaultConfigurationObject(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the constructor, verify the name can not be empty,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     */
    public void test_ctor_failure2() {
        try {
            new DefaultConfigurationObject("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * The failure test of the constructor, verify the name can not be full of space,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     */
    public void test_ctor_failure3() {
        try {
            new DefaultConfigurationObject("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * The accuracy test of the method <code>getName</code>.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getName_accuracy() throws Exception {
        assertEquals("The name should be the one set in constructor.", NAME, defaultCo.getName());
    }

    /**
     * <p>
     * The failure test of the method containsProperty, verify the key can not be null,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_failure1() throws Exception {
        try {
            defaultCo.containsProperty(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method containsProperty, verify the key can not be empty,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_failure2() throws Exception {
        try {
            defaultCo.containsProperty("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method containsProperty, verify the key can not be full of space,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_failure3() throws Exception {
        try {
            defaultCo.containsProperty("  ");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>containsProperty</code>, if the value is an empty array, also return
     * true.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_accuracy1() throws Exception {
        defaultCo.setPropertyValues("key", null);
        assertTrue("The key should be contained.", defaultCo.containsProperty("key"));
    }

    /**
     * <p>
     * The accuracy test of the method <code>containsProperty</code>, if the value is set by setPropertyValues.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_accuracy2() throws Exception {
        defaultCo.setPropertyValues("key", new String[] {"v1"});
        assertTrue("The key should be contained.", defaultCo.containsProperty("key"));
    }

    /**
     * <p>
     * The accuracy test of the method <code>containsProperty</code>, if the value is set by setPropertyValue.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_containsProperty_accuracy3() throws Exception {
        defaultCo.setPropertyValue("key", "v1");
        assertTrue("The key should be contained.", defaultCo.containsProperty("key"));
    }

    /**
     * <p>
     * The failure test of the method getPropertyValue, verify the key can not be null,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue_failure1() throws Exception {
        try {
            defaultCo.getPropertyValue(null, Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValue, verify the key can not be empty,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue_failure2() throws Exception {
        try {
            defaultCo.getPropertyValue("", Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValue, verify the key can not be full of space,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue_failure3() throws Exception {
        try {
            defaultCo.getPropertyValue("  ", Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValue, verify the key is required, but missing.
     * </p>
     * <p>
     * Should have thrown PropertyNotFoundException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue_failure4() throws Exception {
        try {
            defaultCo.getPropertyValue("key", Object.class, true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValue, verify the value cannot be casted to the desired type,
     * </p>
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue_failure5() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {1});

        try {
            defaultCo.getPropertyValue("key", String.class, true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValue, verify the clazz can not be null.
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue_failure6() throws Exception {
        try {
            defaultCo.getPropertyValue("key", null, true);
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
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue_accuracy1() throws Exception {
        defaultCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", defaultCo.getPropertyValue("key", Object.class, false));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue</code>, if the value does not exist, null is
     * returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.", defaultCo.getPropertyValue("key", Object.class, false));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue</code>, if the value is set by setPropertyValue.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue_accuracy3() throws Exception {
        defaultCo.setPropertyValue("key", "v1");
        assertEquals("The value set should be retrieved.", "v1", defaultCo
            .getPropertyValue("key", Object.class, true));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue</code>, if the value is set by setPropertyValues.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValue_accuracy4() throws Exception {
        defaultCo.setPropertyValues("key", new String[] {"v3", "v1"});
        assertEquals("The first value of the array should be retrieved.", "v3", defaultCo.getPropertyValue("key",
            Object.class, true));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValue</code>, if the value is casted to the desired type.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValue_accuracy5() throws Exception {
        defaultCo.setPropertyValue("key", 1);
        assertEquals("The value set should be retrieved.", 1, defaultCo.getPropertyValue("key", Integer.class, true)
            .intValue());
    }

    /**
     * <p>
     * The failure test of the method getPropertyValuesCount, verify the key can not be null,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_failure1() throws Exception {
        try {
            defaultCo.getPropertyValuesCount(null);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValuesCount, verify the key can not be empty,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_failure2() throws Exception {
        try {
            defaultCo.getPropertyValuesCount("");
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValuesCount, verify the key can not be full of space,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_failure3() throws Exception {
        try {
            defaultCo.getPropertyValuesCount("  ");
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
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_accuracy1() throws Exception {
        defaultCo.setPropertyValues("key", null);
        assertEquals("No value exists, 0 should be return.", 0, defaultCo.getPropertyValuesCount("key"));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValuesCount</code>, if there is not value, -1 is returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_accuracy2() throws Exception {
        assertEquals("Null value exists, -1 should be return.", -1, defaultCo.getPropertyValuesCount("key"));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValuesCount</code>, if the value is set by
     * setPropertyValue.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_accuracy3() throws Exception {
        defaultCo.setPropertyValue("key", "v1");
        assertEquals("One value set should be retrieved.", 1, defaultCo.getPropertyValuesCount("key"));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValuesCount</code>, if the value is set by
     * setPropertyValues.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValuesCount_accuracy4() throws Exception {
        defaultCo.setPropertyValues("key", new String[] {"v3", "v1"});
        assertEquals("Two values should be retrieved.", 2, defaultCo.getPropertyValuesCount("key"));
    }

    /**
     * <p>
     * The failure test of the method getPropertyValues, verify the key can not be null,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues_failure1() throws Exception {
        try {
            defaultCo.getPropertyValues(null, Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValues, verify the key can not be empty,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues_failure2() throws Exception {
        try {
            defaultCo.getPropertyValues("", Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValues, verify the key can not be full of space,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues_failure3() throws Exception {
        try {
            defaultCo.getPropertyValues("  ", Object.class, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValues, verify the clazz can not be null.
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues_failure4() throws Exception {
        try {
            defaultCo.getPropertyValues("key", null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValues, verify the key is required, but missing.
     * </p>
     * <p>
     * Should have thrown PropertyNotFoundException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues_failure5() throws Exception {
        try {
            defaultCo.getPropertyValues("key", Object.class, true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getPropertyValues, verify the value cannot be casted to the desired type.
     * </p>
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues_failure6() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {1, "str"});

        try {
            defaultCo.getPropertyValues("key", Integer.class, true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues</code>, if the value is an empty array, empty array
     * is returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues_accuracy1() throws Exception {
        defaultCo.setPropertyValues("key", null);
        assertEquals("No value exists, empty array should be return.", 0, defaultCo.getPropertyValues("key",
            Object.class, true).length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues</code>, if there is not value, null is returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues_accuracy2() throws Exception {
        assertNull("Null value exists, null should be return.", defaultCo.getPropertyValues("key", Object.class,
            false));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues</code>, if the value is set by setPropertyValue.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues_accuracy3() throws Exception {
        defaultCo.setPropertyValue("key", "v1");
        assertEquals("One value set should be retrieved.", 1,
            defaultCo.getPropertyValues("key", Object.class, true).length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues</code>, if the value is set by setPropertyValues.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_getPropertyValues_accuracy4() throws Exception {
        defaultCo.setPropertyValues("key", new String[] {"v3", "v1"});
        assertEquals("Two values should be retrieved.", 2,
            defaultCo.getPropertyValues("key", Object.class, true).length);
    }

    /**
     * <p>
     * The accuracy test of the method <code>getPropertyValues</code>, if value is casted to the desired type.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getPropertyValues_accuracy5() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {1, 2, null});

        Integer[] res = defaultCo.getPropertyValues("key", Integer.class, true);

        assertEquals("Three values should be retrieved.", 3, res.length);
        assertEquals("Correct values should be retrieved.", 1, res[0].intValue());
        assertEquals("Correct values should be retrieved.", 2, res[1].intValue());
        assertNull("Correct values should be retrieved.", res[2]);
    }

    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the key can not be null,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure1() throws Exception {
        try {
            defaultCo.getIntegerProperty(null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the key can not be empty,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure2() throws Exception {
        try {
            defaultCo.getIntegerProperty("", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the key can not be full of space,
     * </p>
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure3() throws Exception {
        try {
            defaultCo.getIntegerProperty("  ", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the key is required, but missing.
     * </p>
     * <p>
     * Should have thrown PropertyNotFoundException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure4() throws Exception {
        try {
            defaultCo.getIntegerProperty("key", true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the value cannot be casted to the desired type,
     * </p>
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure5() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {"str"});

        try {
            defaultCo.getIntegerProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getIntegerProperty, verify the value cannot be casted to the desired type,
     * </p>
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_failure6() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {1L});

        try {
            defaultCo.getIntegerProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }

    /**
     * <p>
     * The accuracy test of the method <code>getIntegerProperty</code>, if the value is an empty array, also null
     * is returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_accuracy1() throws Exception {
        defaultCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", defaultCo.getIntegerProperty("key", false));
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
        assertNull("No value exists, null should be return.", defaultCo.getIntegerProperty("key", false));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getIntegerProperty</code>, if the value is set by setPropertyValue.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_accuracy3() throws Exception {
        defaultCo.setPropertyValue("key", "1");
        assertEquals("The value set should be retrieved.", 1, defaultCo.getIntegerProperty("key", true).intValue());
    }

    /**
     * <p>
     * The accuracy test of the method <code>getIntegerProperty</code>, if the value is set by setPropertyValues.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getIntegerProperty_accuracy4() throws Exception {
        defaultCo.setPropertyValues("key", new Integer[] {1, 2});
        assertEquals("The first value of the array should be retrieved.", 1, defaultCo
            .getIntegerProperty("key", true).intValue());
    }

    /**
     * <p>
     * The failure test of the method getLongProperty, verify the key can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure1() throws Exception {
        try {
            defaultCo.getLongProperty(null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getLongProperty, verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure2() throws Exception {
        try {
            defaultCo.getLongProperty("", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getLongProperty, verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure3() throws Exception {
        try {
            defaultCo.getLongProperty("  ", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getLongProperty, verify the key is required, but missing.
     * </p>
     *
     * <p>
     * Should have thrown PropertyNotFoundException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure4() throws Exception {
        try {
            defaultCo.getLongProperty("key", true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getLongProperty, verify the value cannot be casted to the desired type,
     * </p>
     *
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure5() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {"str"});

        try {
            defaultCo.getLongProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getLongProperty, verify the value cannot be casted to the desired type,
     * </p>
     *
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_failure6() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {1});

        try {
            defaultCo.getLongProperty("key", true);
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
        defaultCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", defaultCo.getLongProperty("key", false));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getLongProperty</code>, if the value does not exist, null is returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getLongProperty_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.", defaultCo.getLongProperty("key", false));
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
        defaultCo.setPropertyValue("key", "1");
        assertEquals("The value set should be retrieved.", 1L, defaultCo.getLongProperty("key", true).longValue());
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
        defaultCo.setPropertyValues("key", new Long[] {1L, 2L});
        assertEquals("The first value of the array should be retrieved.", 1L, defaultCo.getLongProperty("key", true)
            .longValue());
    }

    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the key can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure1() throws Exception {
        try {
            defaultCo.getDoubleProperty(null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure2() throws Exception {
        try {
            defaultCo.getDoubleProperty("", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure3() throws Exception {
        try {
            defaultCo.getDoubleProperty("  ", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the key is required, but missing.
     * </p>
     *
     * <p>
     * Should have thrown PropertyNotFoundException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure4() throws Exception {
        try {
            defaultCo.getDoubleProperty("key", true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the value cannot be casted to the desired type,
     * </p>
     *
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure5() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {"str"});

        try {
            defaultCo.getDoubleProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDoubleProperty, verify the value cannot be casted to the desired type,
     * </p>
     *
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_failure6() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {1L});

        try {
            defaultCo.getDoubleProperty("key", true);
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
        defaultCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", defaultCo.getDoubleProperty("key", false));
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
        assertNull("No value exists, null should be return.", defaultCo.getDoubleProperty("key", false));
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
        defaultCo.setPropertyValue("key", "1");
        assertEquals("The value set should be retrieved.", 1D,
            defaultCo.getDoubleProperty("key", true).doubleValue(), 0.01);
    }

    /**
     * <p>
     * The accuracy test of the method <code>getDoubleProperty</code>, if the value is set by setPropertyValues.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDoubleProperty_accuracy4() throws Exception {
        defaultCo.setPropertyValues("key", new Double[] {1D, 2D});
        assertEquals("The first value of the array should be retrieved.", 1D, defaultCo
            .getDoubleProperty("key", true).doubleValue());
    }

    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure1() throws Exception {
        try {
            defaultCo.getDateProperty(null, "yyyy-MM-dd", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure2() throws Exception {
        try {
            defaultCo.getDateProperty("", "yyyy-MM-dd", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure3() throws Exception {
        try {
            defaultCo.getDateProperty("  ", "yyyy-MM-dd", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDateProperty, verify the format can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure4() throws Exception {
        try {
            defaultCo.getDateProperty("key", null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure5() throws Exception {
        try {
            defaultCo.getDateProperty("key", "", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure6() throws Exception {
        try {
            defaultCo.getDateProperty("key", "  ", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDateProperty, verify the key is required, but missing.
     * </p>
     *
     * <p>
     * Should have thrown PropertyNotFoundException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure7() throws Exception {
        try {
            defaultCo.getDateProperty("key", "yyyy-MM-dd", true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDateProperty, verify the value cannot be casted to the desired type,
     * </p>
     *
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure8() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {"str"});

        try {
            defaultCo.getDateProperty("key", "yyyy-MM-dd", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getDateProperty, verify the value cannot be casted to the desired type,
     * </p>
     *
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_failure9() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {1L});

        try {
            defaultCo.getDateProperty("key", "yyyy-MM-dd", true);
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
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_accuracy1() throws Exception {
        defaultCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", defaultCo.getDateProperty("key", "yyyy-MM-dd", false));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getDateProperty</code>, if the value does not exist, null is returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.", defaultCo.getDateProperty("key", "yyyy-MM-dd", false));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getDateProperty</code>, if the value is set by setPropertyValue.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getDateProperty_accuracy3() throws Exception {
        defaultCo.setPropertyValue("key", "2011-01-01");
        assertEquals("The value set should be retrieved.", "2011-01-01", new SimpleDateFormat("yyyy-MM-dd")
            .format(defaultCo.getDateProperty("key", "yyyy-MM-dd", true)));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getDateProperty</code>, if the value is set by setPropertyValues.
     * </p>
     *
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

        defaultCo.setPropertyValues("key", new Date[] {date1, date2});
        assertEquals("The first value of the array should be retrieved.", "2011-01-01", new SimpleDateFormat(
            "yyyy-MM-dd").format(defaultCo.getDateProperty("key", "yyyy-MM-dd", true)));
    }

    /**
     * <p>
     * The failure test of the method getClassProperty, verify the key can not be null,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure1() throws Exception {
        try {
            defaultCo.getClassProperty(null, true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getClassProperty, verify the key can not be empty,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure2() throws Exception {
        try {
            defaultCo.getClassProperty("", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getClassProperty, verify the key can not be full of space,
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure3() throws Exception {
        try {
            defaultCo.getClassProperty("  ", true);
            fail("Should have thrown IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getClassProperty, verify the key is required, but missing.
     * </p>
     *
     * <p>
     * Should have thrown PropertyNotFoundException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure4() throws Exception {
        try {
            defaultCo.getClassProperty("key", true);
            fail("Should have thrown PropertyNotFoundException.");
        } catch (PropertyNotFoundException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getClassProperty, verify the value cannot be casted to the desired type,
     * </p>
     *
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure5() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {"str"});

        try {
            defaultCo.getClassProperty("key", true);
            fail("Should have thrown PropertyTypeMismatchException.");
        } catch (PropertyTypeMismatchException e) {
            // pass
        }
    }

    /**
     * <p>
     * The failure test of the method getClassProperty, verify the value cannot be casted to the desired type,
     * </p>
     *
     * <p>
     * Should have thrown PropertyTypeMismatchException.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_failure6() throws Exception {
        defaultCo.setPropertyValues("key", new Object[] {1L});

        try {
            defaultCo.getClassProperty("key", true);
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
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_accuracy1() throws Exception {
        defaultCo.setPropertyValues("key", null);
        assertNull("No value exists, null should be return.", defaultCo.getClassProperty("key", false));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getClassProperty</code>, if the value does not exist, null is
     * returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_accuracy2() throws Exception {
        assertNull("No value exists, null should be return.", defaultCo.getClassProperty("key", false));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getClassProperty</code>, if the value is set by setPropertyValue.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_accuracy3() throws Exception {
        defaultCo.setPropertyValue("key", String.class.getName());
        assertEquals("The value set should be retrieved.", String.class, defaultCo.getClassProperty("key", true));
    }

    /**
     * <p>
     * The accuracy test of the method <code>getClassProperty</code>, if the value is set by setPropertyValues.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     * @since 1.1
     */
    public void test_getClassProperty_accuracy4() throws Exception {
        defaultCo.setPropertyValues("key", new Class<?>[] {String.class, Integer.class});
        assertEquals("The first value of the array should be retrieved.", String.class, defaultCo.getClassProperty(
            "key", true));
    }
}
