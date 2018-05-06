/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl.failuretests;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationParserException;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.util.objectfactory.UnknownReferenceException;
import com.topcoder.util.objectfactory.impl.ConfigurationObjectSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;

/**
 * <p>
 * Failure test fixture for <code>ConfigurationObjectSpecificationFactory</code>.
 * </p>
 * 
 * @author Thinfox
 * @version 1.0
 */
public class ConfigurationObjectSpecificationFactoryFailureTests extends TestCase {
    /**
     * <p>
     * An instance of <code>XMLFilePersistence</code> for loading object specifications from
     * config files.
     * </p>
     */
    private static XMLFilePersistence xmlFilePersistence = new XMLFilePersistence();

    /**
     * <p>
     * Load the object specifications from the specified namespace.
     * </p>
     * 
     * @param namespace the namespace
     * @return the ConfigurationObject that provides the ObjectSpecifications
     * @throws IOException if error occurs reading the file.
     * @throws ConfigurationParserException if error occurs parsing the configuration
     * @throws ConfigurationAccessException if error occurs accessing the configuration
     * @throws Exception if any error occurred
     */
    private static ConfigurationObject loadConfigObj(String namespace)
        throws ConfigurationParserException, IOException, ConfigurationAccessException {
        return xmlFilePersistence.loadFile(namespace, new File("test_files/failure/config.xml"))
            .getChild(namespace);
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with null ConfigurationObject.
     * </p>
     * <p>
     * IllegalArgumentException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_Null() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(null);
            fail("IllegalArgumentException was expected.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_MissingType() throws Exception {

        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("MissingType"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_MissingParamType() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("MissingParamType"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_MissingParamValue() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("MissingParamValue"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_ParamName() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("ParamName"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_InvalidType() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("InvalidType"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_InvalidParamType() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("InvalidParamType"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_InvalidArrayType() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("InvalidArrayType"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_ArrayDimensionMismatch() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("ArrayDimensionMismatch"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_NegativeDimension() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("NegativeDimension"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_NonIntegerDimension() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("NonIntegerDimension"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_InvalidArrayValues() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("InvalidArrayValues"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_InvalidArrayValue() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("InvalidArrayValue"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_InvalidArrayValueSizes() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("InvalidArrayValueSizes"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_MissingArrayType() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("MissingArrayType"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_MissingDimension() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("MissingDimension"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_MissingArrayValues() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("MissingArrayValues"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_NullArrayValue() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("NullArrayValue"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_MissingReference() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("MissingReference"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the ctor <code>ConfigurationObjectSpecificationFactory(ConfigurationObject)</code>
     * with invalid config.
     * </p>
     * <p>
     * IllegalReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testCtor_CircularReference() throws Exception {
        try {
            new ConfigurationObjectSpecificationFactory(loadConfigObj("CircularReference"));
            fail("IllegalReferenceException was expected.");
        } catch (IllegalReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the method <code>getObjectSpecification(String, String)</code> with null key.
     * </p>
     * <p>
     * IllegalArgumentException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testGetObjectSpecification_NullKey() throws Exception {
        ConfigurationObjectSpecificationFactory factory = new ConfigurationObjectSpecificationFactory(
            loadConfigObj("Valid"));
        try {
            factory.getObjectSpecification(null, null);
            fail("IllegalArgumentException was expected.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the method <code>getObjectSpecification(String, String)</code> with empty key.
     * </p>
     * <p>
     * IllegalArgumentException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testGetObjectSpecification_EmptyKey() throws Exception {
        ConfigurationObjectSpecificationFactory factory = new ConfigurationObjectSpecificationFactory(
            loadConfigObj("Valid"));
        try {
            factory.getObjectSpecification("  ", null);
            fail("IllegalArgumentException was expected.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the method <code>getObjectSpecification(String, String)</code> with unknown key.
     * </p>
     * <p>
     * UnknownReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testGetObjectSpecification_UnknownKey() throws Exception {
        ConfigurationObjectSpecificationFactory factory = new ConfigurationObjectSpecificationFactory(
            loadConfigObj("Valid"));
        try {
            factory.getObjectSpecification("unknown", null);
            fail("UnknownReferenceException was expected.");
        } catch (UnknownReferenceException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the method <code>getObjectSpecification(String, String)</code> with unknown
     * identifier.
     * </p>
     * <p>
     * UnknownReferenceException is expected.
     * </p>
     * 
     * @throws Exception to JUnit
     */
    public void testGetObjectSpecification_UnknownIdentifier() throws Exception {
        ConfigurationObjectSpecificationFactory factory = new ConfigurationObjectSpecificationFactory(
            loadConfigObj("Valid"));
        try {
            factory.getObjectSpecification("frac", "unknown");
            fail("UnknownReferenceException was expected.");
        } catch (UnknownReferenceException e) {
            // pass
        }
    }

}
