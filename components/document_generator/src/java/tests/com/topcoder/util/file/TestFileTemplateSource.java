/*
 * Copyright (C) 2007 - 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import java.io.File;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;
import junit.framework.TestCase;

/**
 * <p>
 * Title: FileTemplateSource
 * </p>
 * <p>
 * Description: Test whole <code>FileTemplateSource</code> class.
 * </p>
 * <p>
 * Company: TopCoder Software
 * </p>
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class TestFileTemplateSource extends TestCase {
    /** Hold <code>FileTemplateSource</code> object. */
    private FileTemplateSource source;

    /**
     * ConfigurationObject used in the test.
     */
    private ConfigurationObject config;

    /**
     * Create new source with constructor without parameters.
     * @throws Exception
     *             to junit
     */
    public void setUp() throws Exception {
        // Create source.
        source = new FileTemplateSource();
        config = TestHelper.createConfigurationObject("DocumentManager.xml", "myconfig");
    }

    /**
     * Test {@link FileTemplateSource#FileTemplateSource()} constructor without parameters.
     */
    public void testConstructor() {
        // Create source.
        source = new FileTemplateSource();

        // Check whether source was created.
        assertNotNull("The FileTemplateSource object was not created", source);
    }

    /**
     * Test {@link FileTemplateSource#FileTemplateSource(String, ConfigurationObject)} constructor on good
     * data.
     */
    public void testConstructorWithGoodParameters() {
        // Create source.
        source = new FileTemplateSource("source_id", config);

        // Check whether source was created.
        assertNotNull("The FileTemplateSource object was not created", source);
    }

    /**
     * Test {@link FileTemplateSource#FileTemplateSource(String, ConfigurationObject)} constructor with
     * <code>null</code> as first argument.
     */
    public void testConstructorWithFirstNull() {
        try {
            // Try to create source with null as first argument.
            new FileTemplateSource(null, config);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#FileTemplateSource(String, ConfigurationObject)} constructor with
     * <code>null</code> as second argument.
     */
    public void testConstructorWithSecondNull() {
        try {
            // Try to create source with null as second argument.
            new FileTemplateSource("file", null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#FileTemplateSource(String, ConfigurationObject)} constructor with empty
     * string as first argument.
     */
    public void testConstructorWithFirstEmpty() {
        try {
            // Try to create source with empty string as first argument.
            new FileTemplateSource("   ", config);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#getTemplate(String)} method on good data.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplate() throws TemplateSourceException {
        // Check how getTemplate read data from file.
        assertTrue(source.getTemplate(TestHelper.TEST_FILES_DIR + "one.txt").startsWith("Yesterday"));
        assertTrue(source.getTemplate(TestHelper.TEST_FILES_DIR + "one.txt").endsWith("yesterday."));
    }

    /**
     * Test (@link FileTemplateSource#getTemplate(String)) method with a relative path.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplate_RelativePath() throws TemplateSourceException {
        // Check how getTemplate read data from file.
        assertTrue(source.getTemplate("test_files" + File.separator + "one.txt").startsWith("Yesterday"));
        assertTrue(source.getTemplate("test_files" + File.separator + "one.txt").endsWith("yesterday."));
    }

    /**
     * Test (@link FileTemplateSource#getTemplate(String)) method reading file from jar/ear/war file.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplate_ResourceFile() throws TemplateSourceException {
        assertTrue(source.getTemplate("resource/designer.xml").startsWith("<DATA>"));
        assertTrue(source.getTemplate("designer.xml").startsWith("<DATA>"));
    }

    /**
     * Test {@link FileTemplateSource#getTemplate(String)} method with <code>null</code> as argument.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateWithNull() throws TemplateSourceException {
        try {
            // Try to call getTemplate with null as argument.
            source.getTemplate(null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#getTemplate(String)} method with empty string as argument.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateWithEmpty() throws TemplateSourceException {
        try {
            // Try to call getTemplate with empty string as argument.
            source.getTemplate("   ");

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#getTemplate(String)} method with missing file as argument.
     */
    public void testGetTemplateWithMissingFile() {
        try {
            // Try to call getTemplate with missing file as argument.
            source.getTemplate("yesterday.txt");

            // Fail.
            fail("Should throw TemplateSourceException.");
        } catch (TemplateSourceException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#addTemplate(String, String)} on good data (file with given name not
     * exist).
     * @throws TemplateSourceException
     *             to JUnit
     */
    public void testAddTemplate() throws TemplateSourceException {
        // Add template to file system.
        source
            .addTemplate(TestHelper.TEST_FILES_DIR + "LetItBe.txt", "When I find myself in times of trouble");

        // Check whether template was added to file system.
        assertEquals("", source.getTemplate(TestHelper.TEST_FILES_DIR + "LetItBe.txt"),
            "When I find myself in times of trouble");

        // Remove template from file system.
        source.removeTemplate(TestHelper.TEST_FILES_DIR + "LetItBe.txt");
    }

    /**
     * Test {@link FileTemplateSource#addTemplate(String, String)} on good data (file with given name exist).
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testAddTemplateExist() throws TemplateSourceException {
        // Rewrite template to file system.
        source.addTemplate(TestHelper.TEST_FILES_DIR + "Girl.txt", "All about the girl who came to stay?");

        // Check whether template was rewrite to file system.
        assertEquals("", source.getTemplate(TestHelper.TEST_FILES_DIR + "Girl.txt"),
            "All about the girl who came to stay?");

        // Rewrite template back.
        source.addTemplate(TestHelper.TEST_FILES_DIR + "Girl.txt",
            "Is there anybody going to listen to my story");
    }

    /**
     * Test {@link FileTemplateSource#addTemplate(String, String)} with <code>null</code> as first argument.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testAddTemplateWithFirstNull() throws TemplateSourceException {
        try {
            // Try to call addTemplate with null as first argument.
            source.addTemplate(null, "null:)");

            // Fail.
            fail("Should throw IllegalAqrumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#addTemplate(String, String)} with <code>null</code> as second
     * argument.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testAddTemplateWithSecondNull() throws TemplateSourceException {
        try {
            // Try to call addTemplate with null as second argument.
            source.addTemplate("(:null", null);

            // Fail.
            fail("Should throw IllegalAqrumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#addTemplate(String, String)} with empty string as first argument.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testAddTemplateWithFirstEmpty() throws TemplateSourceException {
        try {
            // Try to call addTemplate with empty string as first argument.
            source.addTemplate("   ", "(:)");

            // Fail.
            fail("Should throw IllegalAqrumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#addTemplate(String, String)} with empty string as second argument.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testAddTemplateWithSecondEmpty() throws TemplateSourceException {
        try {
            // Try to call addTemplate with empty string as second argument.
            source.addTemplate("):(", "   ");

            // Fail.
            fail("Should throw IllegalAqrumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#removeTemplate(String)} on good data.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testRemoveTemplate() throws TemplateSourceException {
        // Add template to file system.
        source.addTemplate(TestHelper.TEST_FILES_DIR + "HeyJude.txt", "Hey Jude, don't make it bad");

        // Remove template from file system.
        source.removeTemplate(TestHelper.TEST_FILES_DIR + "HeyJude.txt");

        // Check that this template was removed from file system.
        try {
            source.getTemplate(TestHelper.TEST_FILES_DIR + "HeyJude.txt");

            // Fail.
            fail("Should throw TemplateSourceException.");
        } catch (TemplateSourceException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#removeTemplate(String)} with <code>null</code> as argument.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testRemoveTemplateWithNull() throws TemplateSourceException {
        try {
            // Try to call removeTemplate with null as argument.
            source.removeTemplate(null);

            // Fail.
            fail("Should throw IllegalAqrumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#removeTemplate(String)} with empty string as argument.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testRemoveTemplateWithEmpty() throws TemplateSourceException {
        try {
            // Try to call removeTemplate with empty string as argument.
            source.removeTemplate("   ");

            // Fail.
            fail("Should throw IllegalAqrumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link FileTemplateSource#removeTemplate(String)} with not existing file as argument.
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testRemoveTemplateWithNotExist() throws TemplateSourceException {
        try {
            // Try to call removeTemplate with not exist file as argument.
            source.removeTemplate("NotExist.txt");

            // Fail.
            fail("Should throw TemplateSourceException.");
        } catch (TemplateSourceException e) {
            // Success.
        }
    }
}
