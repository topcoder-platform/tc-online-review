/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import junit.framework.TestCase;

import com.topcoder.util.objectfactory.ObjectSpecification;
import com.topcoder.util.objectfactory.UnknownReferenceException;

/**
 * Unit test for ConfigManagerSpecificationFactory.
 *
 * @author mgmg, TCSDEVELOPER
 * @version 2.2
 */
public class ConfigManagerSpecificationFactoryUnitTest extends TestCase {
    /**
     * The test instance.
     */
    private ConfigManagerSpecificationFactory instance = null;

    /**
     * Create the test instance.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void setUp() throws Exception {
        TestHelper.clearConfig();
        TestHelper.loadSingleConfigFile();

        instance = new ConfigManagerSpecificationFactory("valid_config");
    }

    /**
     * Clean the config.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void tearDown() throws Exception {
        TestHelper.clearConfig();
    }

    /**
     * Test the constructor with the null namespace. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_NullParam() throws Exception {
        try {
            new ConfigManagerSpecificationFactory(null);
            fail("IllegalArgumentException should be thrown because of the invalid parameter.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the not-existing namespace.
     * SpecificationConfigurationException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidNamespace() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid namespace");
            fail("SpecificationConfigurationException should be thrown because of the invalid namespace.");
        } catch (SpecificationConfigurationException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig1() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config1");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig2() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config2");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig3() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config3");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig4() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config4");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig5() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config5");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig6() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config6");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig7() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config7");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig8() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config8");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig9() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config9");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig10() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config10");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig11() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config11");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig12() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config12");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig13() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config13");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig14() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config14");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig15() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config15");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig16() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config16");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig17() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config17");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig18() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config18");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig19() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config19");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig20() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config20");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig21() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config21");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig22() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config22");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with the invalid config. IllegalReferenceException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_InvalidConfig23() throws Exception {
        try {
            new ConfigManagerSpecificationFactory("invalid_config23");
            fail("IllegalReferenceException should be thrown because of the invalid config.");
        } catch (IllegalReferenceException e) {
            // expected.
        }
    }

    /**
     * Test the constructor with valid config namespace. No exception will be
     * thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testConstructor_Accuracy() throws Exception {
        ConfigManagerSpecificationFactory factory = new ConfigManagerSpecificationFactory("valid_config");

        assertNotNull("The object should not be null.", factory);
    }

    /**
     * Test getObjectSpecification with null key. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectSpecification_NullKey() throws Exception {
        try {
            instance.getObjectSpecification(null, "id");
            fail("IllegalArgumentException should be thrown because of the null key.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test getObjectSpecification with empty key. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectSpecification_EmptyKey() throws Exception {
        try {
            instance.getObjectSpecification(" ", "id");
            fail("IllegalArgumentException should be thrown because of the empty key.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Test getObjectSpecification with not-existing key.
     * UnknownReferenceException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectSpecification_NotExist1() throws Exception {
        try {
            instance.getObjectSpecification("frac", "id");
            fail("UnknownReferenceException should be thrown because of the not-existing key.");
        } catch (UnknownReferenceException e) {
            // expected.
        }
    }

    /**
     * Test getObjectSpecification with not-existing key.
     * UnknownReferenceException should be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectSpecification_NotExist2() throws Exception {
        try {
            instance.getObjectSpecification("frac", null);
            fail("UnknownReferenceException should be thrown because of the not-existing key.");
        } catch (UnknownReferenceException e) {
            // expected.
        }
    }

    /**
     * Test getObjectSpecification with valid key. No exception will be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectSpecification_Accuracy1() throws Exception {
        ObjectSpecification spec = instance.getObjectSpecification("frac", "default");

        assertEquals("The specType is wrong.", ObjectSpecification.COMPLEX_SPECIFICATION, spec.getSpecType());
        assertEquals("The type is wrong.", "com.topcoder.util.objectfactory.testclasses.TestClass1", spec.getType());
        assertEquals("The parameter is wrong.", 2, spec.getParameters().length);
    }

    /**
     * Test getObjectSpecification with valid key. No exception will be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectSpecification_Accuracy2() throws Exception {
        ObjectSpecification spec = instance.getObjectSpecification("objectArray", null);

        assertEquals("The specType is wrong.", ObjectSpecification.ARRAY_SPECIFICATION, spec.getSpecType());
        assertEquals("The type is wrong.", "java.lang.Object", spec.getType());
        assertEquals("The parameter is wrong.", 1, spec.getParameters().length);
    }

    /**
     * Test getObjectSpecification with valid key. No exception will be thrown.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void testGetObjectSpecification_Accuracy3() throws Exception {
        ObjectSpecification spec = instance.getObjectSpecification("test", "collection");

        assertEquals("The specType is wrong.", ObjectSpecification.ARRAY_SPECIFICATION, spec.getSpecType());
        assertEquals("The type is wrong.", "java.util.Collection", spec.getType());
        assertEquals("The parameter is wrong.", 3, spec.getParameters().length);
    }
}
