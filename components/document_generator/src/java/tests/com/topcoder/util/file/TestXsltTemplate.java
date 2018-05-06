/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;
import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import com.topcoder.util.file.xslttemplate.XsltTemplate;
import junit.framework.TestCase;

/**
 * <p>
 * Title: TestXsltTemplate
 * </p>
 *
 * <p>
 * Description: Test whole <code>XsltTemplate</code> class.
 * </p>
 *
 * <p>
 * Company: TopCoder Software
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TestXsltTemplate extends TestCase {
    /** Number of test with bad template, see test_files/bad subdir. */
    private static final int BAD_TEST_NUMBER = 16;

    /** XsltTemplate. */
    private XsltTemplate xsltTemplate = null;

    /** Template source. */
    private TemplateSource templateSource = new FileTemplateSource();

    /**
     * Create new <code>XsltTemplate</code> object and set template from
     * template source.
     *
     * @throws TemplateSourceException should not throw
     * @throws TemplateFormatException should not throw
     */
    public void setUp() throws TemplateSourceException, TemplateFormatException {
        // Create new template.
        xsltTemplate = new XsltTemplate();

        // Set template.
        xsltTemplate.setTemplate(templateSource.getTemplate(TestHelper.TEST_FILES_DIR + "designer.txt"));
    }

    /**
     * Test {@link XsltTemplate#XsltTemplate()} constructor.
     */
    public void testConstructor() {
        // This object should created in setUp method.
        assertNotNull("The XsltTemplate object was not created.", xsltTemplate);
    }

    /**
     * Test {@link XsltTemplate#getTemplate()} method.
     *
     * @throws TemplateSourceException should not throw
     */
    public void testGetTemplate() throws TemplateSourceException {
        // Check template property.
        // Template was set in setUp.
        assertEquals("The template property is incorrect.", xsltTemplate.getTemplate(),
            templateSource.getTemplate(TestHelper.TEST_FILES_DIR + "designer.txt"));
    }

    /**
     * Test what do {@link XsltTemplate#getTemplate()} method, if template was
     * not set before.
     */
    public void testGetTemplateWithBadState() {
        try {
            // Should throw IllegalStateException.
            new XsltTemplate().getTemplate();

            // Fail.
            //fail("Should throw IllegalStateException.");
        } catch (IllegalStateException e) {
            // Success.
        }
    }

    /**
     * Test {@link XsltTemplate#setTemplate(String)} with <code>null</code> as
     * argument.
     *
     * @throws TemplateFormatException should not throw
     */
    public void testSetTemplateWithNull() throws TemplateFormatException {
        try {
            // Try set template with null.
            xsltTemplate.setTemplate(null);

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link XsltTemplate#setTemplate(String)} with empty string as
     * argument.
     *
     * @throws TemplateFormatException should not throw
     */
    public void testSetTemplateWithEmpty() throws TemplateFormatException {
        try {
            // Try set template with empty string.
            xsltTemplate.setTemplate("   ");

            // Fail.
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link XsltTemplate#setTemplate(String)} with bad template.
     *
     * @throws TemplateSourceException should not throw
     */
    public void testSetTemplateWithBadTemplate() throws TemplateSourceException {
        // Contain files on which method does not throw an exception.
        String result = "";

        for (int i = 0; i < BAD_TEST_NUMBER; i++) {
            try {
                xsltTemplate.setTemplate(templateSource.getTemplate(TestHelper.TEST_FILES_DIR + "bad/bad"
                    + i + ".txt"));

                // Fail. Should throw TemplateFormatException.
                result += ("bad" + i + ".txt\n");
            } catch (TemplateFormatException e) {
                // Success.
            }
        }

        // If some tests fail, then fail main test.
        if (result.length() > 0) {
            fail("The following files was processed without exception :\n" + result);
        }
    }

    /**
     * Test {@link XsltTemplate#getFields} method.
     *
     * @throws TemplateFormatException should not throw
     */
    public void testGetFields() throws TemplateFormatException {
        // Get fields.
        TemplateFields fields = xsltTemplate.getFields();

        // Our fields should have following structure.
        // <CODER_HANDLE> field
        // <PROJECT_TYPE> field
        // <CURRENT_DATE> field
        // <SCREENING_DATE> field
        // <REVIEW_DATE> field
        // <AGGREGATION_DATE> field
        // <FINAL_REVIEW_DATE> field
        // <PROJECT> loop
        //   <PROJECT_NAME> field
        //   <REVIEWER> loop
        //     <REVIEWER_ROLE> field
        //     <REVIEWER_HANDLE> field
        //     <REVIEWER_PAYMENT> field
        //   <PM_HANDLE> loop
        // <COMPONENT_PAYMENT> condition
        // Try to check something.
        Node[] root = fields.getNodes();

        // Check all first level nodes.
        checkField((Field) root[0], "CODER_HANDLE", null, "The handle of the coder", false);
        checkField((Field) root[1], "PROJECT_TYPE", null, "Design/Development", false);
        checkField((Field) root[2], "CURRENT_DATE", null, "6/11 - 6/17", false);
        checkField((Field) root[3], "SCREENING_DATE", null, "m/dd format ie 6/11", false);
        checkField((Field) root[4], "REVIEW_DATE", null, null, false);
        checkField((Field) root[5], "AGGREGATION_DATE", null, null, false);
        checkField((Field) root[6], "FINAL_REVIEW_DATE", null, null, false);
        checkLoop((Loop) root[7], "PROJECT", "loop for each project", false);
        checkCondition((Condition) root[8], "COMPONENT_PAYMENT", "",
            "Display special text for high-paying 'rush' components", false, "COMPONENT_PAYMENT&gt;='1000'");

        // Check the number of nodes in first level.
        assertEquals("The number of nodes incorrect.", root.length, 9);

        // Check project loop.
        Node[] projectLoop = ((Loop) root[7]).getSampleLoopItem().getNodes();
        checkField((Field) projectLoop[0], "PROJECT_NAME", null, null, false);
        checkLoop((Loop) projectLoop[1], "REVIEWER", null, false);
        checkField((Field) projectLoop[2], "PM_HANDLE", null, null, false);

        // Check the number of nodes in this loop.
        assertEquals("The number of nodes incorrect.", projectLoop.length, 3);

        // Check reviewer loop.
        Node[] reviwerLoop = ((Loop) projectLoop[1]).getSampleLoopItem().getNodes();
        checkField((Field) reviwerLoop[0], "REVIEWER_ROLE", "empty", null, false);
        checkField((Field) reviwerLoop[1], "REVIEWER_HANDLE", "empty", "for testing", false);
        checkField((Field) reviwerLoop[2], "REVIEWER_PAYMENT", "space ", null, false);

        // Check the number of nodes in this loop.
        assertEquals("The number of nodes incorrect.", reviwerLoop.length, 3);
    }

    /**
     * Check that {@link XsltTemplate#getFields()} return new copy each time.
     *
     * @throws TemplateFormatException should not throw
     */
    public void testGetFieldsNotSame() throws TemplateFormatException {
        // Get two copies of fields.
        Node[] firstCopy = xsltTemplate.getFields().getNodes();
        Node[] secondCopy = xsltTemplate.getFields().getNodes();

        // Check that some nodes not same.
        // Each copy contain 8 elements.
        for (int i = 0; i < 8; i++) {
            assertNotSame("The nodes same, getFields method work incorrectly.", firstCopy[i], secondCopy[i]);
        }

        // Check inner project loop.
        firstCopy = ((Loop) firstCopy[7]).getSampleLoopItem().getNodes();
        secondCopy = ((Loop) secondCopy[7]).getSampleLoopItem().getNodes();

        // Each copy contain 3 elements.
        for (int i = 0; i < 3; i++) {
            assertNotSame("The nodes same, getFields method work incorrectly.", firstCopy[i], secondCopy[i]);
        }

        // Check inner reviewer loop.
        firstCopy = ((Loop) firstCopy[1]).getSampleLoopItem().getNodes();
        secondCopy = ((Loop) secondCopy[1]).getSampleLoopItem().getNodes();

        // Each copy contain 3 elements.
        for (int i = 0; i < 3; i++) {
            assertNotSame("The nodes same, getFields method work incorrectly.", firstCopy[i], secondCopy[i]);
        }
    }

    /**
     * Test what do {@link XsltTemplate#getFields()} method, if template was
     * not set before.
     */
    public void testGetFieldsWithBadState() {
        try {
            // Should throw TemplateFormatException.
            new XsltTemplate().getFields();

            // Fail.
            fail("Should throw TemplateFormatException.");
        } catch (TemplateFormatException e) {
            // Success.
        }
    }

    /**
     * Test {@link XsltTemplate#applyTemplate(TemplateData)} method on good
     * data.
     *
     * @throws TemplateSourceException should not throw
     * @throws TemplateDataFormatException should not throw
     * @throws TemplateFormatException should not throw
     */
    public void testApplyTemplate() throws TemplateSourceException, TemplateDataFormatException,
        TemplateFormatException {
        // Get data.
        XmlTemplateData data = new XmlTemplateData();

        // Set template data.
        data.setTemplateData(templateSource.getTemplate(TestHelper.TEST_FILES_DIR + "designer.xml"));

        // Check applyTemplate method.
        // The template can used multiply times.
        assertTrue("The begin of output document is incorrect", xsltTemplate.applyTemplate(data).startsWith(
            "\r\nHello someone,"));
        assertTrue("The begin of output document is incorrect", xsltTemplate.applyTemplate(data).startsWith(
            "\r\nHello someone,"));
    }

    /**
     * Test {@link XsltTemplate#applyTemplate(TemplateData)} with <code>new
     * MyTempateData()</code> as argument.
     *
     * @throws TemplateDataFormatException should not throw
     * @throws TemplateFormatException should not throw
     */
    public void testApplyTemplateWithMyTemplate() throws TemplateDataFormatException, TemplateFormatException {
        try {
            // Try to apply template to the MyTemplatData object
            // (should be XmlTemplateData).
            xsltTemplate.applyTemplate(new MyTemplateData());

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link XsltTemplate#applyTemplate(TemplateData)} with
     * <code>null</code> as argument.
     *
     * @throws TemplateDataFormatException should not throw
     * @throws TemplateFormatException should not throw
     */
    public void testApplyTemplateWithNull() throws TemplateDataFormatException, TemplateFormatException {
        try {
            // Try to apply template to the null.
            xsltTemplate.applyTemplate(null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test what do {@link XsltTemplate#applyTemplate(TemplateData)} method, if
     * template was not set before.
     *
     * @throws TemplateDataFormatException should not throw
     */
    public void testApplyTemplateWithBadState() throws TemplateDataFormatException {
        try {
            // Should throw TemplateFormatException.
            new XsltTemplate().applyTemplate(new XmlTemplateData());

            // Fail.
            fail("Should throw TemplateFormatException.");
        } catch (TemplateFormatException e) {
            // Success.
        }
    }

    /**
     * Test what do {@link XsltTemplate#applyTemplate(TemplateData)} method, if
     * TemplateData is incorrect.
     *
     * @throws TemplateFormatException should not throw
     */
    public void testApplyTemplateWithBadData() throws TemplateFormatException {
        try {
            // Should throw TemplatDataFormatException,
            // because data was not set.
            xsltTemplate.applyTemplate(new XmlTemplateData());

            // Fail.
            fail("Should throw TemplateDataFormatException.");
        } catch (TemplateDataFormatException e) {
            // Success.
        }
    }

    /**
     * Check field, field should contain correct name, value, description, and
     * be read-only only if <code>readOnly</code> is <code>true</code>.
     *
     * @param field field for validating
     * @param name expected name
     * @param value expected value
     * @param description expected description
     * @param readOnly expected read-only mode
     */
    private void checkField(Field field, String name, String value, String description, boolean readOnly) {
        // Check name.
        assertEquals("The name property is incorrect.", field.getName(), name);

        // Check value.
        assertEquals("The value property is incorrect.", field.getValue(), value);

        // Check description.
        assertEquals("The description property is incorrect.", field.getDescription(), description);

        // Check read-only mode.
        assertEquals("The read only property is incorrect.", field.isReadOnly(), readOnly);
    }

    /**
     * Check field, field should contain correct name, value, description, and
     * be read-only only if <code>readOnly</code> is <code>true</code>.
     *
     * @param condition condition for validating
     * @param name expected name
     * @param value expected value
     * @param description expected description
     * @param readOnly expected read-only mode
     * @param conditionStatement the condition statement
     *
     * @since 2.1
     */
    private void checkCondition(Condition condition, String name, String value, String description, boolean readOnly,
        String conditionStatement) {
        // Check name.
        assertEquals("The name property is incorrect.", condition.getName(), name);

        // Check value.
        assertEquals("The value property is incorrect.", condition.getValue(), value);

        // Check description.
        assertEquals("The description property is incorrect.", condition.getDescription(), description);

        // Check read-only mode.
        assertEquals("The read-only property is incorrect.", condition.isReadOnly(), readOnly);

        // Check condition statement.
        assertEquals("The condition statement property is incorrect.", condition.getConditionalStatement(),
            conditionStatement);
    }

    /**
     * Check loop, loop should contain correct loop element, description, and
     * be read-only only if <code>readOnly</code> is <code>true</code>.
     *
     * @param loop loop for validating
     * @param loopElement expected loop element
     * @param description expected description
     * @param readOnly excepted read-only mode
     */
    private void checkLoop(Loop loop, String loopElement, String description, boolean readOnly) {
        // Check loop element.
        assertEquals("The loop element property is incorrect.", loop.getLoopElement(), loopElement);

        // Check description.
        assertEquals("The description property is incorrect.", loop.getDescription(), description);

        // Check read-only mode.
        //      Check read-only mode.
        assertEquals("The read only property is incorrect.", loop.isReadOnly(), readOnly);
    }
}
