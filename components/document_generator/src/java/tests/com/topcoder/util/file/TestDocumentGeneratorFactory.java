/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.templatesource.FileTemplateSource;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for TestDocumentGeneratorFactory.java class.
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class TestDocumentGeneratorFactory extends TestCase {
    /**
     * This configuration is used in the test.
     */
    private ConfigurationObject config;

    /**
     * Aggregates all tests in this class.
     * @return Test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(TestDocumentGeneratorFactory.class);
    }

    /**
     * Sets up the environment for the TestCase.
     * @throws Exception
     *             throw exception to JUnit.
     */
    protected void setUp() throws Exception {
        config = TestHelper.createConfigurationObject("DocumentManager.xml", "myconfig");
    }

    /**
     * Tears down the environment for the TestCase.
     * @throws Exception
     *             throw exception to JUnit.
     */
    protected void tearDown() throws Exception {
        // do nothing
    }

    /**
     * Accuracy test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * with two source, and default source is exist.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_accuracy1() throws Exception {
        DocumentGenerator generator = DocumentGeneratorFactory.getDocumentGenerator(config);
        assertNotNull("generator should not be null.", generator);
        assertTrue("source for id 'file' should be created.",
            generator.getTemplateSource("file") instanceof FileTemplateSource);
        assertTrue("source for id 'my_file' should be created.",
            generator.getTemplateSource("my_file") instanceof MyFileTemplateSource);
        assertTrue("default source should be created.",
            generator.getDefaultTemplateSource() instanceof FileTemplateSource);
    }

    /**
     * Accuracy test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * with two source, and default source is not exist.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_accuracy2() throws Exception {
        config.removeProperty("default_source");
        DocumentGenerator generator = DocumentGeneratorFactory.getDocumentGenerator(config);

        assertNotNull("generator should not be null.", generator);
        assertTrue("source for id 'file' should be created.",
            generator.getTemplateSource("file") instanceof FileTemplateSource);
        assertTrue("source for id 'my_file' should be created.",
            generator.getTemplateSource("my_file") instanceof FileTemplateSource);
        assertNull("default source should be null.", generator.getDefaultTemplateSource());
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * configuration is null.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_null_configuration() throws Exception {
        try {
            DocumentGeneratorFactory.getDocumentGenerator(null);
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * sources property is not exist.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_null_sources() throws Exception {
        config.removeProperty("sources");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * sources property is empty.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_empty_sources() throws Exception {
        config.setPropertyValues("sources", new String[0]);
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * one source id is not String type.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_non_string_source() throws Exception {
        config.setPropertyValues("sources", new Object[] {"file", new Integer(1) });
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * one source id is empty.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_empty_string_source() throws Exception {
        config.setPropertyValues("sources", new Object[] {"file", "  " });
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * no class name for one source.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_no_class_name() throws Exception {
        config.removeProperty("file_class");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * class name for one source is not string type.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_non_string_class_name() throws Exception {
        config.setPropertyValue("file_class", new Integer(1));
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * class name for one source is invalid.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_invalid_class_name() throws Exception {
        config.setPropertyValue("file_class", "test");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * class name for one source do not have required ctor.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_invalid_class_name2() throws Exception {
        config.setPropertyValue("file_class", "java.lang.String");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * class name for one source is not TemplateSource type.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_invalid_class_name3() throws Exception {
        config.setPropertyValue("file_class", "com.topcoder.util.file.BadTemplateSource");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * class name for one source is a interface.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_invalid_class_name4() throws Exception {
        config.setPropertyValue("file_class", "com.topcoder.util.file.templatesource.TemplateSource");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getDocumentGenerator(ConfigurationObject configuration)</code> method.
     * <p>
     * default_source is empty.
     * </p>
     * <p>
     * Expect DocumentGeneratorConfigurationException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDocumentGenerator_failure_empty_default_source() throws Exception {
        config.setPropertyValue("default_source", "   ");
        try {
            DocumentGeneratorFactory.getDocumentGenerator(config);
            fail("Expect DocumentGeneratorConfigurationException.");
        } catch (DocumentGeneratorConfigurationException e) {
            // expect
        }
    }
}
