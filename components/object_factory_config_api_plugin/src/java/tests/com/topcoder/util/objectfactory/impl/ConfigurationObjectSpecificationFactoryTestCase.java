/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import java.io.File;

import junit.framework.TestCase;

import com.test.TestComplex;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.ObjectSpecification;
import com.topcoder.util.objectfactory.UnknownReferenceException;

/**
 * Test the <code>ConfigurationObjectSpecificationFactory</code> class, unit
 * test.
 *
 * @author sparemax
 * @version 1.0
 */
public class ConfigurationObjectSpecificationFactoryTestCase extends TestCase {
    /** The test files path. */
    private static final String TEST_FILES = "test_files" + File.separator;

    /**
     * The <code>ConfigurationObjectSpecificationFactory</code> instance used in
     * the unit test.
     */
    private ConfigurationObjectSpecificationFactory instance = null;

    /**
     * The <code>ConfigurationObject</code> instance used in the unit test.
     */
    private ConfigurationObject configurationObject = null;

    /**
     * Create the test instance.
     *
     * @throws Exception
     *             exception to JUnit.
     */
    public void setUp() throws Exception {
        // build the XMLFilePersistence
        XMLFilePersistence xmlFilePersistence = new XMLFilePersistence();

        // load the ConfigurationObject from the input file
        configurationObject = xmlFilePersistence.loadFile("test", new File(TEST_FILES + "config2.xml"));
    }

    /**
     * Test the Ctor
     * <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * . Use <code>ObjectFactory</code> created from a
     * <code>ConfigurationObject</code> (built manually).
     *
     * @throws Exception
     *             when error occurs
     */
    public void testCtor1() throws Exception {
        instance = new ConfigurationObjectSpecificationFactory(UnitTestHelper.createConfigurationObject());
        assertNotNull("'instance' created incorrect.", instance);

        // create the object factory
        ObjectFactory objFactory = new ObjectFactory(instance);

        // create defined objects
        Bar bar = (Bar) objFactory.createObject("bar");
        TestClass fracDefault = (TestClass) objFactory.createObject("frac", "default");

        // check the objects
        assertEquals("Bad data retrieved.", 100.0, bar.getF(), 0.000001);
        assertEquals("Bad data retrieved.", "string buffer", bar.getBuffer().toString());

        assertEquals("Bad data retrieved.", 1, fracDefault.getIntValue());
        assertEquals("Bad data retrieved.", "Strong", fracDefault.getStr());
        assertEquals("Bad data retrieved.", 100.0, fracDefault.getBar().getF(), 0.000001);
        assertEquals("Bad data retrieved.", "string buffer", fracDefault.getBar().getBuffer().toString());
    }

    /**
     * Test the Ctor
     * <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * . Use <code>ObjectFactory</code> created from a
     * <code>ConfigurationObject</code> (built using Configuration Persistence
     * component and configurations stored in a xml configuration file).
     *
     * @throws Exception
     *             when error occurs
     */
    public void testCtor2() throws Exception {
        instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
        assertNotNull("'instance' created incorrect.", instance);

        // create the object factory
        ObjectFactory objFactory = new ObjectFactory(instance);
        TestClass1 fracDefault = (TestClass1) objFactory.createObject("frac", "default");
        TestClass2 bar = (TestClass2) objFactory.createObject("bar");

        assertEquals("Bad data retrieved.", 2, fracDefault.getIntValue());
        assertEquals("Bad data retrieved.", "Strong", fracDefault.getStr());

        assertEquals("Bad data retrieved.", 2, bar.getTestClass1().getIntValue());
        assertEquals("Bad data retrieved.", "Strong", bar.getTestClass1().getStr());
        assertEquals("Bad data retrieved.", 2.5F, bar.getF(), 0.000001);
    }

    /**
     * Test the Ctor
     * <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * creates array correctly.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testCtorArray() throws Exception {
        instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
        assertNotNull("'instance' created incorrect.", instance);

        // create the object factory
        ObjectFactory objFactory = new ObjectFactory(instance);
        int[][] expected = { { 1, 2 }, { 3, 4 }, { 3, 4 }, { 3, 4 }, { 3, 4 }, { 3, 4 }, { 3, 4 }, { 3, 4 }, { 3, 4 },
            { 3, 4 }, { 3, 4 }, { 3, 4 }, { 3, 4 }, { 3, 4 }, { 3, 4 }, { 3, 4 }, { 3, 4 } };

        int[][] intArray = (int[][]) objFactory.createObject("intArray", "arrays");

        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals("Array created incorrect.", expected[i][j], intArray[i][j]);
            }
        }
    }

    /**
     * Test the Ctor
     * <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with jar source.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testCtorJarSource() throws Exception {
        instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
        assertNotNull("'instance' created incorrect.", instance);

        // create the object factory
        ObjectFactory objFactory = new ObjectFactory(instance);
        TestComplex testComplex = (TestComplex) objFactory.createObject("testComplex");

        assertEquals("Bad data retrieved.", "Complex2Strong", testComplex.toStrong());
    }

    /**
     * Test the Ctor
     * <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with <code>ConfigurationObject</code> empty.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testCtorWithConfigurationObjectEmpty() throws Exception {
        instance = new ConfigurationObjectSpecificationFactory(new DefaultConfigurationObject("empty"));
    }

    /**
     * Test the constructor
     * <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with <code>configurationurationObject</code> is <code>null</code>.
     * <code>ConfigurationObjectSpecificationFactory</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testCtorWithConfigurationObjectNull() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(null);
            fail("ConfigurationObjectSpecificationFactory is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig1() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config1"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration..
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig2() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config2"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig3() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config3"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig4() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config3"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig5() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config5"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig6() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config6"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig7() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config7"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig8() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config8"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig9() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config9"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig10() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config10"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig11() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config11"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig12() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config12"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig13() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config13"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig14() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config14"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig15() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config15"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig16() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config16"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig17() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config17"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig18() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config18"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig19() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config19"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig20() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config20"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig21() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config21"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig22() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config22"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig23() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config23"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig24() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config24"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with the invalid configuration.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidConfig25() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(configurationObject.getChild("invalid_config25"));
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with cyclical definition.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_Cyclical() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(UnitTestHelper.createConfigurationObjectWithCyclical());
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test the constructor with invalid reference.
     * <code>IllegalReferenceException</code> is expected.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testConstructor_InvalidReference() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(UnitTestHelper.createConfigurationObjectWithInvalidReference());
            fail("IllegalReferenceException should be thrown because of the invalid configuration.");
        } catch (IllegalReferenceException e) {
            // good
        }
    }

    /**
     * Test getObjectSpecification with null key. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetObjectSpecification_NullKey() throws Exception {
        try {
            instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
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
     *             when error occurs
     */
    public void testGetObjectSpecification_EmptyKey() throws Exception {
        try {
            instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
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
     *             when error occurs
     */
    public void testGetObjectSpecification_NotExist1() throws Exception {
        try {
            instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
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
     *             when error occurs
     */
    public void testGetObjectSpecification_NotExist2() throws Exception {
        try {
            instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
            instance.getObjectSpecification("frac", null);
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
     *             when error occurs
     */
    public void testGetObjectSpecification_NotExist3() throws Exception {
        try {
            instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
            instance.getObjectSpecification("NotExist", "default");
            fail("UnknownReferenceException should be thrown because of the not-existing key.");
        } catch (UnknownReferenceException e) {
            // expected.
        }
    }

    /**
     * Test getObjectSpecification with valid key. No exception will be thrown.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetObjectSpecification_Accuracy1() throws Exception {
        instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
        ObjectSpecification spec = instance.getObjectSpecification("frac", "default");

        assertEquals("The specType is wrong.", ObjectSpecification.COMPLEX_SPECIFICATION, spec.getSpecType());
        assertEquals("The type is wrong.", "com.topcoder.util.objectfactory.impl.TestClass1", spec.getType());
        assertEquals("The parameter is wrong.", 2, spec.getParameters().length);
    }

    /**
     * Test getObjectSpecification with valid key. No exception will be thrown.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetObjectSpecification_Accuracy2() throws Exception {
        instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
        ObjectSpecification spec = instance.getObjectSpecification("objectArray", null);

        assertEquals("The specType is wrong.", ObjectSpecification.ARRAY_SPECIFICATION, spec.getSpecType());
        assertEquals("The type is wrong.", "java.lang.Object", spec.getType());
        assertEquals("The parameter is wrong.", 1, spec.getParameters().length);
    }

    /**
     * Test getObjectSpecification with valid key. No exception will be thrown.
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetObjectSpecification_Accuracy3() throws Exception {
        instance = new ConfigurationObjectSpecificationFactory(configurationObject.getChild("valid_config"));
        ObjectSpecification spec = instance.getObjectSpecification("test", "collection");

        assertEquals("The specType is wrong.", ObjectSpecification.ARRAY_SPECIFICATION, spec.getSpecType());
        assertEquals("The type is wrong.", "java.util.Collection", spec.getType());
        assertEquals("The parameter is wrong.", 3, spec.getParameters().length);
    }
}
