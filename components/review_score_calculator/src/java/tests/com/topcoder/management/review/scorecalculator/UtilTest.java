/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.util.config.ConfigManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contains unit tests for the Util class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class UtilTest extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The name of the configuration file containing all the configuration for unit testing this class.
     */
    private static final String CONFIG_FILENAME = "UnitTests/UtilTestConfig.xml";

    /**
     * The configuration namespace that does not exist in the configuration file.
     */
    private static final String MISSING_NAMESPACE = "missing.namespace";

    /**
     * The configuration namespace where the property is missing.
     */
    private static final String OPTIONAL_MISSING_PROPERTY_NAMESPACE = "optional.missing";

    /**
     * The configuration namespace where the property is configured.
     */
    private static final String OPTIONAL_CONFIGURED_PROPERTY_NAMESPACE = "optional.configured";

    /**
     * The configuration namespace where the property is missing.
     */
    private static final String REQUIRED_MISSING_PROPERTY_NAMESPACE = "required.missing";

    /**
     * The configuration namespace where the property is an empty string.
     */
    private static final String REQUIRED_EMPTY_PROPERTY_NAMESPACE = "required.empty";

    /**
     * The configuration namespace where the property is a blank string.
     */
    private static final String REQUIRED_BLANK_PROPERTY_NAMESPACE = "required.blank";

    /**
     * The configuration namespace where the property is configured.
     */
    private static final String REQUIRED_CONFIGURED_PROPERTY_NAMESPACE = "required.configured";

    /**
     * The name of the configuration property being tested.
     */
    private static final String PROPERTY = "property";

    /**
     * The expected value of the configuration property being tested.
     */
    private static final String VALUE = "value";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(UtilTest.class);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SetUp/TearDown

    /**
     * Reloads the configuration file.
     *
     * @throws  Exception
     *          An unknown error occured during setup.
     */
    protected void setUp() throws Exception {
        clearNamespaces();
        ConfigManager.getInstance().add(CONFIG_FILENAME);
    }

    /**
     * Clears the configuration manager of our namespaces.
     */
    protected void tearDown() {
        clearNamespaces();
    }

    /**
     * Clears the configuration manager of our namespaces.
     */
    private static void clearNamespaces() {
        TestUtil.clearNamespace(OPTIONAL_MISSING_PROPERTY_NAMESPACE);
        TestUtil.clearNamespace(OPTIONAL_CONFIGURED_PROPERTY_NAMESPACE);
        TestUtil.clearNamespace(REQUIRED_MISSING_PROPERTY_NAMESPACE);
        TestUtil.clearNamespace(REQUIRED_EMPTY_PROPERTY_NAMESPACE);
        TestUtil.clearNamespace(REQUIRED_BLANK_PROPERTY_NAMESPACE);
        TestUtil.clearNamespace(REQUIRED_CONFIGURED_PROPERTY_NAMESPACE);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // checkNotNonPositive Tests

    /**
     * Ensures that the checkNotNonPositive method throws an IllegalArgumentException when given a negative value.
     */
    public void testCheckNotNonPositiveThrowsOnNegative() {
        try {
            Util.checkNotNonPositive(-1, "argument");
            fail("An IllegalArgumentException is expected when given a negative value.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the checkNotNonPositive method throws an IllegalArgumentException when given a zero value.
     */
    public void testCheckNotNonPositiveThrowsOnZero() {
        try {
            Util.checkNotNonPositive(-1, "argument");
            fail("An IllegalArgumentException is expected when given a zero value.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // checkNotNonPositiveArray Tests

    /**
     * Ensures that the checkNotNonPositiveArray method throws an IllegalArgumentException when given a null array.
     */
    public void testCheckNotNonPositiveArrayThrowsOnNull() {
        try {
            Util.checkNotNonPositiveArray(null, "argument");
            fail("An IllegalArgumentException is expected when given a null array.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the checkNotNonPositiveArray method throws an IllegalArgumentException when given a negative
     * element.
     */
    public void testCheckNotNonPositiveArrayThrowsOnNegative() {
        try {
            Util.checkNotNonPositiveArray(new long[] {-1}, "argument");
            fail("An IllegalArgumentException is expected when given an array with a negative.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the checkNotNonPositiveArray method throws an IllegalArgumentException when given a zero
     * element.
     */
    public void testCheckNotNonPositiveArrayThrowsOnZero() {
        try {
            Util.checkNotNonPositiveArray(new long[] {0}, "argument");
            fail("An IllegalArgumentException is expected when given an array with a zero.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // checkNotNull Tests

    /**
     * Ensures that the checkNotNull method throws an IllegalArgumentException when given a null.
     */
    public void testCheckNotNullThrowsOnNull() {
        try {
            Util.checkNotNull(null, "argument");
            fail("An IllegalArgumentException is expected when given a null.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // checkNotNullArray Tests

    /**
     * Ensures that the checkNotNullArray method throws an IllegalArgumentException when given a null array.
     */
    public void testCheckNotNullArrayThrowsOnNullArray() {
        try {
            Util.checkNotNullArray(null, "argument");
            fail("An IllegalArgumentException is expected when given a null array.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the checkNotNull method throws an IllegalArgumentException when given a null element.
     */
    public void testCheckNotNullArrayThrowsOnNullElement() {
        try {
            Util.checkNotNullArray(new Object[] {null}, "argument");
            fail("An IllegalArgumentException is expected when given a null element.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // checkNotNullOrEmpty Tests

    /**
     * Ensures that the checkNotNullOrEmpty method throws an IllegalArgumentException when given a null.
     */
    public void testCheckNotNullOrEmptyThrowsOnNull() {
        try {
            Util.checkNotNullOrEmpty(null, "argument");
            fail("An IllegalArgumentException is expected when given a null.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the checkNotNullOrEmpty method throws an IllegalArgumentException when given an empty string.
     */
    public void testCheckNotNullOrEmptyThrowsOnEmpty() {
        try {
            Util.checkNotNullOrEmpty("", "argument");
            fail("An IllegalArgumentException is expected when given an empty string.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the checkNotNullOrEmpty method throws an IllegalArgumentException when given a blank string.
     */
    public void testCheckNotNullOrEmptyThrowsOnBlank() {
        try {
            Util.checkNotNullOrEmpty(" ", "argument");
            fail("An IllegalArgumentException is expected when given a blank string.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // isEmpty Tests

    /**
     * Ensures that the isEmpty method returns true when given an empty string.
     */
    public void testIsEmptyReturnsTrueOnEmpty() {
        assertTrue("True should be returned for an empty string.", Util.isEmpty(""));
    }

    /**
     * Ensures that the isEmpty method returns true when given a blank string.
     */
    public void testIsEmptyReturnsTrueOnBlank() {
        assertTrue("True should be returned for a blank string.", Util.isEmpty(" "));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // getOptionalProperty Tests

    /**
     * Ensures that the getOptionalProperty method throws a ConfigurationException when the namespace is missing.
     */
    public void testGetOptionalPropertyThrowsOnMissingNamespace() {
        try {
            Util.getOptionalProperty(MISSING_NAMESPACE, PROPERTY);
            fail("A ConfigurationException is expected when the namespace is missing.");
        } catch (ConfigurationException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the getOptionalProperty method returns null when the property is missing.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void testGetOptionalPropertyWorksOnMissingProperty() throws ConfigurationException {
        assertNull(
                "The property is missing, so a null should be returned.",
                Util.getOptionalProperty(OPTIONAL_MISSING_PROPERTY_NAMESPACE, PROPERTY));
    }

    /**
     * Ensures that the getOptionalProperty method returns the value when the property is configured.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void testGetOptionalPropertyWorksOnConfiguredProperty() throws ConfigurationException {
        assertEquals(
                "The property is configured, so the value should be returned.",
                VALUE, Util.getOptionalProperty(OPTIONAL_CONFIGURED_PROPERTY_NAMESPACE, PROPERTY));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // getRequiredProperty Tests

    /**
     * Ensures that the getRequiredProperty method throws a ConfigurationException when the namespace is missing.
     */
    public void testGetRequiredPropertyThrowsOnMissingNamespace() {
        checkGetRequiredPropertyThrowsCE(MISSING_NAMESPACE, "the namespace is missing.");
    }

    /**
     * Ensures that the getRequiredProperty method throws a ConfigurationException when the property is missing.
     */
    public void testGetRequiredPropertyThrowsOnMissingProperty() {
        checkGetRequiredPropertyThrowsCE(REQUIRED_MISSING_PROPERTY_NAMESPACE, "the property is missing.");
    }

    /**
     * Ensures that the getRequiredProperty method throws a ConfigurationException when the property is an empty
     * string.
     */
    public void testGetRequiredPropertyThrowsOnEmptyProperty() {
        checkGetRequiredPropertyThrowsCE(REQUIRED_EMPTY_PROPERTY_NAMESPACE, "the property is empty.");
    }

    /**
     * Ensures that the getRequiredProperty method throws a ConfigurationException when the property is a blank
     * string.
     */
    public void testGetRequiredPropertyThrowsOnBlankProperty() {
        checkGetRequiredPropertyThrowsCE(REQUIRED_BLANK_PROPERTY_NAMESPACE, "the property is blank.");
    }

    /**
     * Helper method to check that the getRequiredProperty throws a ConfigurationException when given the
     * specified namespace.
     *
     * @param   namespace
     *          The namespace that should cause a ConfigurationException.
     * @param   message
     *          The message to use in the error message if the unit test fails.
     */
    private void checkGetRequiredPropertyThrowsCE(String namespace, String message) {
        try {
            Util.getRequiredProperty(namespace, PROPERTY);
            fail("A ConfigurationException is expected when " + message);
        } catch (ConfigurationException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the getRequiredProperty method returns the value when the property is configured.
     *
     * @throws  ConfigurationException
     *          An unknown error occurred.
     */
    public void testGetRequiredPropertyWorksOnConfiguredProperty() throws ConfigurationException {
        assertEquals(
                "The property is configured, so the value should be returned.",
                VALUE, Util.getRequiredProperty(REQUIRED_CONFIGURED_PROPERTY_NAMESPACE, PROPERTY));
    }
}
