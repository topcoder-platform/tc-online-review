/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.accuracytests;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.DocumentGeneratorFactory;
import com.topcoder.util.file.templatesource.FileTemplateSource;

/**
 * Test class for <code>DocumentGeneratorFactory</code>.
 * @author peony
 * @version 3.0
 */
public class DocumentGeneratorFactoryAccuracyTest extends TestCase {
    /**
     * Provide a ConfigurationObject for testing.
     */
    private ConfigurationObject co;

    /**
     * Provide a DocumentGenerator for testing.
     */
    private DocumentGenerator generator;

    /**
     * Sets up the environment.
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        co = new DefaultConfigurationObject("root");
        co.setPropertyValues("sources", new String[] {"source1"});
        co.setPropertyValue("source1_class", "com.topcoder.util.file.templatesource.FileTemplateSource");
        co.setPropertyValue("source2_class", "com.topcoder.util.file.accuracytests.MockTemplateSource1");
        co.setPropertyValue("source3_class", "com.topcoder.util.file.accuracytests.MockTemplateSource2");
    }

    /**
     * Tears down the environment.
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test with a single source and default source.
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator1() throws Exception {
        co.setPropertyValue("default_source", "source1");
        generator = DocumentGeneratorFactory.getDocumentGenerator(co);
        assertTrue("getDocumentGenerator wrong",
            generator.getTemplateSource("source1") instanceof FileTemplateSource);
        assertTrue("getDocumentGenerator wrong", generator.getTemplateSource("source2") == null);
        assertTrue("getDocumentGenerator wrong", generator.getTemplateSource("source3") == null);

        assertTrue("getDocumentGenerator wrong", generator.getDefaultTemplateSource() instanceof FileTemplateSource);
    }

    /**
     * Test with a single source and a different default source.
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator2() throws Exception {
        co.setPropertyValue("default_source", "source2");
        generator = DocumentGeneratorFactory.getDocumentGenerator(co);
        assertTrue("getDocumentGenerator wrong",
            generator.getTemplateSource("source1") instanceof FileTemplateSource);
        assertTrue("getDocumentGenerator wrong", generator.getTemplateSource("source2") == null);
        assertTrue("getDocumentGenerator wrong", generator.getTemplateSource("source3") == null);

        assertTrue("getDocumentGenerator wrong",
            generator.getDefaultTemplateSource() instanceof MockTemplateSource1);
    }

    /**
     * Test with a template source and without default source.
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator3() throws Exception {
        generator = DocumentGeneratorFactory.getDocumentGenerator(co);
        assertTrue("getDocumentGenerator wrong",
            generator.getTemplateSource("source1") instanceof FileTemplateSource);
        assertTrue("getDocumentGenerator wrong", generator.getTemplateSource("source2") == null);
        assertTrue("getDocumentGenerator wrong", generator.getTemplateSource("source3") == null);

        assertTrue("getDocumentGenerator wrong", generator.getDefaultTemplateSource() == null);
    }

    /**
     * Test with three source and without default source.
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator4() throws Exception {
        co.setPropertyValues("sources", new String[] {"source1", "source2", "source3"});
        generator = DocumentGeneratorFactory.getDocumentGenerator(co);
        assertTrue("getDocumentGenerator wrong",
            generator.getTemplateSource("source1") instanceof FileTemplateSource);
        assertTrue("getDocumentGenerator wrong",
            generator.getTemplateSource("source2") instanceof MockTemplateSource1);
        assertTrue("getDocumentGenerator wrong",
            generator.getTemplateSource("source3") instanceof MockTemplateSource2);

        assertTrue("getDocumentGenerator wrong", generator.getDefaultTemplateSource() == null);
    }

    /**
     * Test with three source and with a default source.
     * @throws Exception
     *             to JUnit
     */
    public void testGetDocumentGenerator5() throws Exception {
        co.setPropertyValues("sources", new String[] {"source1", "source2", "source3"});
        co.setPropertyValue("default_source", "source3");
        generator = DocumentGeneratorFactory.getDocumentGenerator(co);
        assertTrue("getDocumentGenerator wrong",
            generator.getTemplateSource("source1") instanceof FileTemplateSource);
        assertTrue("getDocumentGenerator wrong",
            generator.getTemplateSource("source2") instanceof MockTemplateSource1);
        assertTrue("getDocumentGenerator wrong",
            generator.getTemplateSource("source3") instanceof MockTemplateSource2);

        assertTrue("getDocumentGenerator wrong",
            generator.getDefaultTemplateSource() instanceof MockTemplateSource2);
    }
}
