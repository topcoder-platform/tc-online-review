/*
 * Copyright (C) 2007, 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;
import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * <p>
 * Title: TestDocumentGenerator
 * </p>
 * <p>
 * Description: Test whole <code>DocumentGenerator</code> class.
 * </p>
 * <p>
 * Company: TopCoder Software
 * </p>
 * @author TCSDEVELOPER
 * @version 3.0
 * @since 2.1
 */
public class TestDocumentGenerator extends TestCase {
    /** This is template from file system. */
    private static final String LITTLE = "# This is little template.\r\n%TITLE%\r\n"
            + "%loop:WORD{Write world}%%loop:ALPHA{Write alpha}%%alpha%%endloop%\r\n" + "%endloop%";

    /** This constant represents the begin of result. */
    private static final String BEGIN_OF_RESULT = "\r\nHello someone,";

    /** Contain instance of document generator. */
    private DocumentGenerator generator = null;

    /** Contain template from "designer.txt". */
    private Template template = null;

    /**
     * This instance is used in the test.
     */
    private TemplateSource templateSource = new FileTemplateSource();

    /** Contain template data form "designer.xml". */
    private TemplateData templateData = null;

    /**
     * Get Document Generator instance and get template and template data.
     * @throws Exception
     *             to JUnit
     */
    public void setUp() throws Exception {
        // Get generator.
        generator = DocumentGeneratorFactory.getDocumentGenerator(TestHelper.createConfigurationObject(
                "DocumentManager.xml", "myconfig"));

        // Get template.
        template = generator.getTemplate("my_file", "designer.txt");

        // Get template data.
        templateData = new XmlTemplateData();

        // Set template data.
        templateData.setTemplateData(new FileTemplateSource().getTemplate(TestHelper.TEST_FILES_DIR
                + "designer.xml"));
    }

    /**
     * <p>
     * Tests ctor DocumentGenerator#DocumentGenerator(String) for accuracy.
     * </p>
     * <p>
     * It verifies the newly created DocumentGenerator instance should not be null.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 2.1
     */
    public void testCtor() throws Exception {
        assertNotNull("Failed to create a new DocumentGenerator instance.", new DocumentGenerator());
    }

    /**
     * <p>
     * Tests DocumentGenerator#getTemplate(String) for failure.
     * </p>
     * <p>
     * Expects TemplateSourceException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 2.1
     */
    public void testGetTemplate1_TemplateSourceException() throws Exception {
        generator = new DocumentGenerator();
        try {
            generator.getTemplate(TestHelper.TEST_FILES_DIR + "lit.txt");
            fail("TemplateSourceException expected.");
        } catch (TemplateSourceException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests DocumentGenerator#getTemplate(String,String) for failure.
     * </p>
     * <p>
     * Expects TemplateSourceException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 2.1
     */
    public void testGetTemplate2_TemplateSourceException() throws Exception {
        try {
            generator.getTemplate("unknown", "little.txt");
            fail("TemplateSourceException expected.");
        } catch (TemplateSourceException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests DocumentGenerator#applyTemplate(Template,String) for failure.
     * </p>
     * <p>
     * It tests the case that when template is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 2.1
     */
    public void testApplyTemplate_NullTemplate() throws Exception {
        try {
            generator.applyTemplate(null, templateData.getTemplateData());
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests DocumentGenerator#applyTemplate(TemplateFields) for failure.
     * </p>
     * <p>
     * It tests the case that when templateData is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 2.1
     */
    public void testApplyTemplate_NullTemplateData() throws Exception {
        try {
            generator.applyTemplate(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests DocumentGenerator#applyTemplate(Template,String,Writer) for failure.
     * </p>
     * <p>
     * It tests the case that when output is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 2.1
     */
    public void testApplyTemplate1_NullOutput() throws Exception {
        try {
            generator.applyTemplate(template, templateData.getTemplateData(), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests DocumentGenerator#applyTemplate(Template,Reader,Writer) for failure.
     * </p>
     * <p>
     * It tests the case that when output is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 2.1
     */
    public void testApplyTemplate2_NullOutput() throws Exception {
        try {
            generator.applyTemplate(template, new StringReader(templateData.getTemplateData()), null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * <p>
     * Tests DocumentGenerator#applyTemplate(TemplateFields,Writer) for failure.
     * </p>
     * <p>
     * It tests the case that when output is null and expects IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit
     * @since 2.1
     */
    public void testApplyTemplate_NullOutput() throws Exception {
        TemplateFields fields = template.getFields();
        ((Field) fields.getNodes()[0]).setValue("someone");

        try {
            generator.applyTemplate(fields, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(String, String)} method.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateStringString() throws TemplateFormatException, TemplateSourceException {
        assertEquals("The getTemplate method work incorrectly.", generator.getTemplate("my_file",
                "little.txt").getTemplate(), LITTLE);
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(String, String)} method with null as first argument.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateStringStringWithFirstNull() throws TemplateFormatException,
            TemplateSourceException {
        try {
            // Try to get template with null as first argument.
            generator.getTemplate((String) null, "little.txt");

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(String, String)} method with empty string as first argument.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateStringStringWithFirstEmpty() throws TemplateFormatException,
            TemplateSourceException {
        try {
            // Try to get template with empty first argument.
            generator.getTemplate("   ", "little.txt");

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(String, String)} method with null as second argument.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateStringStringWithSecondNull() throws TemplateFormatException,
            TemplateSourceException {
        try {
            // Try to get template with null as second argument.
            generator.getTemplate("my_file", null);

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(String, String)} method with empty string as second argument.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateStringStringWithSecondEmpty() throws TemplateFormatException,
            TemplateSourceException {
        try {
            // Try to get template with empty second argument.
            generator.getTemplate("my_file", "   ");

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(TemplateSource, String)} method.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateSourceString() throws TemplateFormatException, TemplateSourceException {
        assertEquals("The getTemplate method work incorrectly.", generator.getTemplate(
                new FileTemplateSource(), TestHelper.TEST_FILES_DIR + "lit.txt").getTemplate(), LITTLE);
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(TemplateSource, String)} method with null as first argument.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateSourceStringWithFirstNull() throws TemplateFormatException,
            TemplateSourceException {
        try {
            // Try to get template with null as first argument.
            generator.getTemplate((TemplateSource) null, "little.txt");

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(TemplateSource, String)} method with null as second argument.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateSourceStringWithSecondNull() throws TemplateFormatException,
            TemplateSourceException {
        try {
            // Try to get template with null as second argument.
            generator.getTemplate(new FileTemplateSource(), null);

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(TemplateSource, String)} method with empty string as second
     * argument.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateSourceStringWithSecondEmpty() throws TemplateFormatException,
            TemplateSourceException {
        try {
            // Try to get template with empty second argument.
            generator.getTemplate(new FileTemplateSource(), "   ");

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(String)} method.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateString() throws TemplateFormatException, TemplateSourceException {
        assertEquals("The getTemplate method work incorrectly.", generator.getTemplate(
                TestHelper.TEST_FILES_DIR + "lit.txt").getTemplate(), LITTLE);
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(String)} method with null as argument.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateStringWithNull() throws TemplateFormatException, TemplateSourceException {
        try {
            // Try to get template with name null.
            generator.getTemplate(null);

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#getTemplate(String)} method with empty string as argument.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateSourceException
     *             should not throw
     */
    public void testGetTemplateStringWithEmpty() throws TemplateFormatException, TemplateSourceException {
        try {
            // Try to get template with empty name.
            generator.getTemplate("   ");

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#parseTemplate(String)} method.
     * @throws Exception
     *             to junit
     */
    public void testParseTemplateString() throws Exception {
        assertEquals("The template was parsed incorrectly.", generator.parseTemplate(
                "This is simple template, with one field %field%").getTemplate(),
                "This is simple template, with one field %field%");
    }

    /**
     * Test {@link DocumentGenerator#parseTemplate(String)} method with <code>null</code> as argument.
     * @throws TemplateFormatException
     *             should not throw
     */
    public void testParseTemplateStringWithNull() throws TemplateFormatException {
        try {
            // Try to parse null template.
            generator.parseTemplate((String) null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#parseTemplate(String)} method with empty string as argument.
     * @throws TemplateFormatException
     *             should not throw
     */
    public void testParseTemplateStringWithEmpty() throws TemplateFormatException {
        try {
            // Try to parse empty template.
            generator.parseTemplate("   ");

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#parseTemplate(String)} method with bad template as argument.
     */
    public void testParseTemplateStringWithBad() {
        try {
            // Try to parse strange loop.
            generator.parseTemplate("%endloop%%loop:x%");

            // Fail.
            fail("Should throw TemplateFormatException.");
        } catch (TemplateFormatException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#parseTemplate(Reader)} method.
     * @throws TemplateFormatException
     *             should not throw
     * @throws IOException
     *             should not throw
     */
    public void testParseTemplateReader() throws TemplateFormatException, IOException {
        assertEquals("The template was parsed incorrectly.", generator.parseTemplate(
                new StringReader("This is simple template, with one field %field%")).getTemplate(),
                "This is simple template, with one field %field%");
    }

    /**
     * Test {@link DocumentGenerator#parseTemplate(Reader)} method with <code>null</code> as argument.
     * @throws TemplateFormatException
     *             should not throw
     * @throws IOException
     *             should not throw
     */
    public void testParseTemplateReaderWithNull() throws TemplateFormatException, IOException {
        try {
            // Try to parse with null reader.
            generator.parseTemplate((Reader) null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#parseTemplate(Reader)} method with closed reader as argument.
     * @throws TemplateFormatException
     *             should not throw
     */
    public void testParseTemplateReaderWithClose() throws TemplateFormatException {
        try {
            // Create and close reader.
            Reader reader = new StringReader("Just text.");
            reader.close();

            // Try to parse empty template.
            generator.parseTemplate(reader);

            // Fail.
            fail("Should throw IOException.");
        } catch (IOException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#parseTemplate(Reader)} method with bad template as argument.
     * @throws IOException
     *             should not throw
     */
    public void testParseTemplateReaderWithBad() throws IOException {
        try {
            // Try to parse strange loop.
            generator.parseTemplate(new StringReader("%endloop%%loop:x%"));

            // Fail.
            fail("Should throw TemplateFormatException.");
        } catch (TemplateFormatException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#getFields(Template)} method.
     * @throws TemplateFormatException
     *             should not throw
     */
    public void testGetFields() throws TemplateFormatException {
        // Get template.
        template = generator.parseTemplate("%loop:var%%endloop%%field%");

        // Get fields from template.
        TemplateFields fields = generator.getFields(template);

        // Check that we return correct fields.
        assertNotNull("The fields can not be null.", fields);

        // Check that fields contain two elements.
        assertEquals("The fields should contain two elements.", fields.getNodes().length, 2);
    }

    /**
     * Test {@link DocumentGenerator#getFields(Template)} method with <code>null</code> as argument.
     * @throws TemplateFormatException
     *             should not throw
     */
    public void testGetFieldsWithNull() throws TemplateFormatException {
        try {
            // Try to get fields from null.
            generator.getFields(null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link DocumentGenerator#applyTemplate(Template, String)} method.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateDataFormatException
     *             should not throw
     */
    public void testApplyTemplateTemplateString() throws TemplateFormatException, TemplateDataFormatException {
        assertTrue("The begin of the result is incorrect", generator.applyTemplate(template,
                templateData.getTemplateData()).startsWith(BEGIN_OF_RESULT));
    }

    /**
     * Test {@link DocumentGenerator#applyTemplate(Template, Reader)} method.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateDataFormatException
     *             should not throw
     * @throws IOException
     *             should not throw
     */
    public void testApplyTemplateTemplateReader() throws TemplateFormatException,
            TemplateDataFormatException, IOException {
        assertTrue("The begin of the result is incorrect", generator.applyTemplate(template,
                new StringReader(templateData.getTemplateData())).startsWith(BEGIN_OF_RESULT));
    }

    /**
     * Test {@link DocumentGenerator#applyTemplate(TemplateFields)} method.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateDataFormatException
     *             should not throw
     * @throws IOException
     *             should not throw
     */
    public void testApplyTemplateTemplateFields() throws TemplateFormatException,
            TemplateDataFormatException, IOException {
        // Get template fields.
        TemplateFields fields = template.getFields();

        // Set first node to someone, because for checking we need only
        // first one.
        ((Field) fields.getNodes()[0]).setValue("someone");
        assertTrue("The begin of the result is incorrect", generator.applyTemplate(fields).startsWith(
                BEGIN_OF_RESULT));
    }

    /**
     * Test {@link DocumentGenerator#applyTemplate(Template, String, Writer)} method.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateDataFormatException
     *             should not throw
     * @throws IOException
     *             should not throw
     */
    public void testApplyTemplateTemplateStringWriter() throws TemplateFormatException,
            TemplateDataFormatException, IOException {
        // Create writer.
        Writer writer = new StringWriter();

        // Apply template.
        generator.applyTemplate(template, templateData.getTemplateData(), writer);

        // Check the result
        assertTrue("The begin of the result is incorrect", writer.toString().startsWith(BEGIN_OF_RESULT));
    }

    /**
     * Test {@link DocumentGenerator#applyTemplate(Template, Reader, Writer)} method.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateDataFormatException
     *             should not throw
     * @throws IOException
     *             should not throw
     */
    public void testApplyTemplateTemplateReaderWriter() throws TemplateFormatException,
            TemplateDataFormatException, IOException {
        // Create writer.
        Writer writer = new StringWriter();

        // Apply template.
        generator.applyTemplate(template, new StringReader(templateData.getTemplateData()), writer);

        // Check the result
        assertTrue("The begin of the result is incorrect", writer.toString().startsWith(BEGIN_OF_RESULT));
    }

    /**
     * Test {@link DocumentGenerator#applyTemplate(TemplateFields, Writer)} method.
     * @throws TemplateFormatException
     *             should not throw
     * @throws TemplateDataFormatException
     *             should not throw
     * @throws IOException
     *             should not throw
     */
    public void testApplyTemplateTemplateFieldsWriter() throws TemplateFormatException,
            TemplateDataFormatException, IOException {
        // Create writer.
        Writer writer = new StringWriter();

        // Get template fields.
        TemplateFields fields = template.getFields();

        // Set first node to someone, because for checking we need only
        // first one.
        ((Field) fields.getNodes()[0]).setValue("someone");

        // Apply template.
        generator.applyTemplate(fields, writer);

        // Check the result
        assertTrue("The begin of the result is incorrect", writer.toString().startsWith(BEGIN_OF_RESULT));
    }

    /**
     * Accuracy test of <code>setTemplateSource(String sourceId, TemplateSource source)</code> method.
     * <p>
     * set the template source with id not exist.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testSetTemplateSource_accuracy1() throws Exception {
        assertNull("should return null.", generator.setTemplateSource("id1", templateSource));
        assertEquals("result is not expected.", templateSource, generator.getTemplateSource("id1"));
    }

    /**
     * Accuracy test of <code>setTemplateSource(String sourceId, TemplateSource source)</code> method.
     * <p>
     * set the template source with id already exist.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testSetTemplateSource_accuracy2() throws Exception {
        assertNotNull("should return non-null.", generator.setTemplateSource("file", templateSource));
        assertEquals("result is not expected.", templateSource, generator.getTemplateSource("file"));
    }

    /**
     * Failure test of <code>setTemplateSource(String sourceId, TemplateSource source)</code> method.
     * <p>
     * sourceId is null.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testSetTemplateSource_failure_null_sourceId() throws Exception {
        try {
            generator.setTemplateSource(null, templateSource);
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>setTemplateSource(String sourceId, TemplateSource source)</code> method.
     * <p>
     * sourceId is empty.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testSetTemplateSource_failure_empty_sourceId() throws Exception {
        try {
            generator.setTemplateSource("   ", templateSource);
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>setTemplateSource(String sourceId, TemplateSource source)</code> method.
     * <p>
     * source is null.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testSetTemplateSource_failure_null_source() throws Exception {
        try {
            generator.setTemplateSource("id", null);
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Accuracy test of <code>getTemplateSource(String sourceId)</code> method.
     * <p>
     * sourceId is not exist. expect return null.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetTemplateSource_accuracy1() throws Exception {
        assertNull("expect return null.", generator.getTemplateSource("test"));
    }

    /**
     * Accuracy test of <code>getTemplateSource(String sourceId)</code> method.
     * <p>
     * sourceId is exist. expect return non-null.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetTemplateSource_accuracy2() throws Exception {
        assertNotNull("expect return null.", generator.getTemplateSource("file"));
        assertTrue("result is incorrect.", generator.getTemplateSource("file") instanceof FileTemplateSource);
    }

    /**
     * Failure test of <code>getTemplateSource(String sourceId)</code> method.
     * <p>
     * sourceId is null.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetTemplateSource_failure_null_sourceId() throws Exception {
        try {
            generator.getTemplateSource(null);
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>getTemplateSource(String sourceId)</code> method.
     * <p>
     * sourceId is empty.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetTemplateSource_failure_empty_sourceId() throws Exception {
        try {
            generator.getTemplateSource("   ");
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Accuracy test of <code>removeTemplateSource(String sourceId)</code> method.
     * <p>
     * sourceId is not exist, should return null.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveTemplateSource_accuracy1() throws Exception {
        assertNull("should return null.", generator.removeTemplateSource("test"));
    }

    /**
     * Accuracy test of <code>removeTemplateSource(String sourceId)</code> method.
     * <p>
     * sourceId is exist, should return the removed template source.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveTemplateSource_accuracy2() throws Exception {
        generator.setTemplateSource("test", templateSource);
        assertEquals("removed source is incorrect.", templateSource, generator.removeTemplateSource("test"));
        assertNull("source should be removed.", generator.getTemplateSource("test"));
    }

    /**
     * Failure test of <code>removeTemplateSource(String sourceId)</code> method.
     * <p>
     * sourceId is null.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveTemplateSource_failure_null_sourceId() throws Exception {
        try {
            generator.removeTemplateSource(null);
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Failure test of <code>removeTemplateSource(String sourceId)</code> method.
     * <p>
     * sourceId is empty.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveTemplateSource_failure_empty_sourceId() throws Exception {
        try {
            generator.removeTemplateSource("   ");
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Accuracy test of <code>setDefaultTemplateSource(TemplateSource source)</code> method.
     * @throws Exception
     *             to JUnit.
     */
    public void testSetDefaultTemplateSource_accuracy() throws Exception {
        generator.setDefaultTemplateSource(templateSource);
        assertEquals("default templateSource is incorrect.", templateSource, generator
                .getDefaultTemplateSource());
    }

    /**
     * Failure test of <code>setDefaultTemplateSource(TemplateSource source)</code> method.
     * <p>
     * source is null.
     * </p>
     * <p>
     * Expect IllegalArgumentException.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testSetDefaultTemplateSource_failure_null_source() throws Exception {
        try {
            generator.setDefaultTemplateSource(null);
            fail("Expect IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expect
        }
    }

    /**
     * Accuracy test of <code>getDefaultTemplateSource()</code> method.
     * <p>
     * set one source, try to get it.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDefaultTemplateSource_accuracy() throws Exception {
        generator.setDefaultTemplateSource(templateSource);
        assertEquals("default templateSource is incorrect.", templateSource, generator
                .getDefaultTemplateSource());
    }

    /**
     * Accuracy test of <code>clearTemplateSources()</code> method.
     * <p>
     * add one source, and clear it.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testClearTemplateSources_accuracy() throws Exception {
        generator.setTemplateSource("test", templateSource);
        generator.clearTemplateSources();
        assertNull("template sources should be cleared.", generator.getTemplateSource("test"));
    }
}
