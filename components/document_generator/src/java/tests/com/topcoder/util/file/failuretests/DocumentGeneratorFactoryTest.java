/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.DocumentGeneratorConfigurationException;
import com.topcoder.util.file.DocumentGeneratorFactory;

/**
 * Failure test for class DocumentGeneratorFactory.
 *
 * @author extra
 * @version 3.0
 */
public class DocumentGeneratorFactoryTest extends TestCase {

    /**
     * This configuration is used in the test.
     */
    private ConfigurationObject configuration;

    /**
     * Aggregates all tests in this class.
     *
     * @return Test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(DocumentGeneratorFactoryTest.class);
    }

    /**
     * Sets up the environment for the TestCase.
     *
     * @throws Exception
     *             throw exception to JUnit.
     */
    protected void setUp() throws Exception {
        configuration = FailureTestHelper.createConfigurationObject("failure/DocumentManager.xml", "myconfig");
    }

    /**
     * Failure test for method getDocumentGenerator(ConfigurationObject configuration). With null configuration,
     * IllegalArgumentException expected.
     */
    public void testGtDocumentGenerator_Null_Configuration() {
        try {
            DocumentGeneratorFactory.getDocumentGenerator(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // expected
        } catch (DocumentGeneratorConfigurationException e) {
            fail("Invalid : " + e.getMessage());
        }
    }

    /**
     * Failure test for method getDocumentGenerator(ConfigurationObject configuration). With configuration miss source,
     * IllegalArgumentException expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator_Miss_Sources() throws Exception {
        configuration.removeProperty("sources");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(configuration);
            fail("DocumentGeneratorConfigurationException expected.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expected
        }
    }

    /**
     * Failure test for method getDocumentGenerator(ConfigurationObject configuration). With configuration empty source,
     * IllegalArgumentException expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator_Empty_Sources() throws Exception {
        configuration.setPropertyValues("sources", new String[0]);
        try {
            DocumentGeneratorFactory.getDocumentGenerator(configuration);
            fail("DocumentGeneratorConfigurationException expected.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expected
        }
    }

    /**
     * Failure test for method getDocumentGenerator(ConfigurationObject configuration). With configuration invalid
     * source, IllegalArgumentException expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator_Invalid_Sources() throws Exception {
        configuration.setPropertyValues("sources", new Object[] {"file", new Integer(1) });
        try {
            DocumentGeneratorFactory.getDocumentGenerator(configuration);
            fail("DocumentGeneratorConfigurationException expected.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expected
        }
    }

    /**
     * Failure test for method getDocumentGenerator(ConfigurationObject configuration). With configuration invalid
     * source, IllegalArgumentException expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator_Invalid_Sources2() throws Exception {
        configuration.setPropertyValues("sources", new String[] {"file", "  " });
        try {
            DocumentGeneratorFactory.getDocumentGenerator(configuration);
            fail("DocumentGeneratorConfigurationException expected.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expected
        }
    }

    /**
     * Failure test for method getDocumentGenerator(ConfigurationObject configuration). With configuration miss
     * file_class, IllegalArgumentException expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator_Miss_file_class() throws Exception {
        configuration.removeProperty("file_class");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(configuration);
            fail("DocumentGeneratorConfigurationException expected.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expected
        }
    }

    /**
     * Failure test for method getDocumentGenerator(ConfigurationObject configuration). With configuration empty
     * file_class, IllegalArgumentException expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator_Empty_file_class() throws Exception {
        configuration.setPropertyValue("file_class", new Integer(2));
        try {
            DocumentGeneratorFactory.getDocumentGenerator(configuration);
            fail("DocumentGeneratorConfigurationException expected.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expected
        }
    }

    /**
     * Failure test for method getDocumentGenerator(ConfigurationObject configuration). With configuration invalid
     * file_class, IllegalArgumentException expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator_Invalid_file_class() throws Exception {
        configuration.setPropertyValue("file_class", "file");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(configuration);
            fail("DocumentGeneratorConfigurationException expected.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expected
        }
    }

    /**
     * Failure test for method getDocumentGenerator(ConfigurationObject configuration). With configuration invalid
     * file_class, IllegalArgumentException expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator_Invalid_file_class2() throws Exception {
        configuration.setPropertyValue("file_class", "java.lang.String");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(configuration);
            fail("DocumentGeneratorConfigurationException expected.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expected
        }
    }

    /**
     * Failure test for method getDocumentGenerator(ConfigurationObject configuration). With configuration empty
     * default_source, IllegalArgumentException expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator_Empty_DefaultSource() throws Exception {
        configuration.setPropertyValue("default_source", " ");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(configuration);
            fail("DocumentGeneratorConfigurationException expected.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expected
        }
    }
}
