/**
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.file.accuracytests;

import java.io.FileReader;
import java.io.StringWriter;

import junit.framework.TestCase;

import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.DocumentGeneratorFactory;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSource;
import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import com.topcoder.util.file.xslttemplate.XsltTemplate;

/**
 * <p>
 * Test the DocumentGenerator class.
 * </p>
 * <p>
 * <b>3.0 Modification</b>:
 * <ol>
 * <li>Test cases about command line and config are removed.</li>
 * <li>The codes of constructing DocumentGenerator is modified.</li>
 * <li>Test files are moved to accuracy2.1 directory.</li>
 * <li><b>Cases added for new methods since 3.0</b></li>
 * </ol>
 * Though the original accuracy tests are acceptable but too simple, I am not going to modify them since it is not my
 * duty.
 * </p>
 * @author TCSDEVELOPER
 * @author peony(version 3.0)
 * @version 3.0
 * @since 2.1
 */
public class DocumentGeneratorAccuracyTests extends TestCase {
    /**
     * The template file name.
     * <p>
     * <b>3.0 Modification</b>: This file is moved to accuracy2.1 dir.
     * </p>
     * @since 2.1
     */
    public static String TEMPLATE_FILE = "test_files/accuracy2.1/AccuracyTemplate.txt";

    /**
     * The result file name.
     * <p>
     * <b>3.0 Modification</b>: This file is moved to accuracy2.1 dir.
     * </p>
     * @since 2.1
     */
    public static String RESULT_FILE = "test_files/accuracy2.1/AccuracyResult.txt";

    /**
     * The template data file name.
     * <p>
     * <b>3.0 Modification</b>: This file is moved to accuracy2.1 dir.
     * </p>
     * @since 2.1
     */
    public static String TEMPLATE_DATA_FILE = "test_files/accuracy2.1/AccuracyTemplateData.xml";

    /**
     * The default config file used in this test class.
     * @since 3.0
     */
    private static final String CONFIG_FILE = "test_files/accuracy2.1/DocumentManager.xml";

    /**
     * With the help of CONFIG_FILE, a ConfigurationObject will be created for testing.
     * @since 3.0
     */
    private static final String CONFIG_NAMESPACE = "com.topcoder.util.file";

    /**
     * Prepare a template source for testing.
     * @since 3.0
     */
    private static TemplateSource source1 = new FileTemplateSource("source1",
        new DefaultConfigurationObject("root"));

    /**
     * Prepare a template source for testing.
     * @since 3.0
     */
    private static TemplateSource source2 = new MockTemplateSource1("source1", new DefaultConfigurationObject(
        "root"));

    /**
     * Prepare a template source for testing.
     * @since 3.0
     */
    private static TemplateSource source3 = new MockTemplateSource2("source1", new DefaultConfigurationObject(
        "root"));

    /**
     * DocumentGenerator instance for test.
     * @since 2.1
     */
    DocumentGenerator doc = null;

    /**
     * XsltTemplate instance for test.
     * @since 2.1
     */
    XsltTemplate template = null;

    /**
     * FileTemplateSource instance for test.
     * @since 2.1
     */
    FileTemplateSource source = null;

    /**
     * XmlTemplateData instance for test.
     * @since 2.1
     */
    XmlTemplateData data = null;

    /**
     * Initialize XsltTemplate instance
     * @throws Exception
     *             to JUnit
     */
    public void setUp() throws Exception {
        doc = DocumentGeneratorFactory.getDocumentGenerator(new ConfigurationFileManager(CONFIG_FILE)
            .getConfiguration(CONFIG_NAMESPACE).getChild(CONFIG_NAMESPACE));
        data = new XmlTemplateData();
        template = new XsltTemplate();
        source = new FileTemplateSource();

        template.setTemplate(source.getTemplate(TEMPLATE_FILE));
        data.setTemplateData(source.getTemplate(TEMPLATE_DATA_FILE));
    }

    /**
     * Tests the getTemplate methods.
     * @throws Exception
     *             to JUnit
     */
    public void testGetTemplate() throws Exception {
        Template tmpl = doc.getTemplate("file", TEMPLATE_FILE);
        assertEquals(template.getTemplate(), tmpl.getTemplate());

        tmpl = doc.getTemplate(source, TEMPLATE_FILE);
        assertEquals(template.getTemplate(), tmpl.getTemplate());
    }

    /**
     * Tests the parseTemplate methods.
     * @throws Exception
     *             to JUnit
     */
    public void testParseTemplate() throws Exception {
        Template tmpl = doc.parseTemplate(new FileReader(TEMPLATE_FILE));
        assertEquals(template.getTemplate(), tmpl.getTemplate());

        tmpl = doc.parseTemplate(readString(new FileReader(TEMPLATE_FILE)));
        assertEquals(template.getTemplate(), tmpl.getTemplate());
    }

    /**
     * Tests the getFields method.
     * @throws Exception
     *             to JUnit
     */
    public void testGetFields() throws Exception {
        assertTrue(doc.getFields(template).getTemplate() == template);

        Node[] nodes = doc.getFields(template).getNodes();
        assertEquals(8, nodes.length);
        assertEquals("CODER_HANDLE", ((Field) nodes[0]).getName());
        assertEquals("PROJECT_TYPE", ((Field) nodes[1]).getName());
        assertEquals("Development", ((Field) nodes[1]).getValue());
        assertEquals("CURRENT_DATE", ((Field) nodes[2]).getName());
        assertEquals("6/11 - 6/17", ((Field) nodes[2]).getValue());
        assertEquals("SCREENING_DATE", ((Field) nodes[3]).getName());
        assertEquals("REVIEW_DATE", ((Field) nodes[4]).getName());
        assertEquals("AGGREGATION_DATE", ((Field) nodes[5]).getName());
        assertEquals("FINAL_REVIEW_DATE", ((Field) nodes[6]).getName());
        assertEquals("PROJECT", ((Loop) nodes[7]).getLoopElement());

        nodes = ((Loop) nodes[7]).getSampleLoopItem().getNodes();
        assertEquals(3, nodes.length);
        assertEquals("PROJECT_NAME", ((Field) nodes[0]).getName());
        assertEquals("REVIEWER", ((Loop) nodes[1]).getLoopElement());
        assertEquals("PM_HANDLE", ((Field) nodes[2]).getName());
        assertEquals("TopCoder", ((Field) nodes[2]).getValue());
    }

    /**
     * Tests the applyTemplate method.
     * @throws Exception
     *             to JUnit
     */
    public void testApplyTemplate() throws Exception {
        // Read the reference
        String ref = readString(new FileReader(RESULT_FILE));
        ref = myTrim(ref);

        // Test methods with string
        StringWriter writer = new StringWriter();
        String result = doc.applyTemplate(template, new FileReader(TEMPLATE_DATA_FILE));
        assertEquals(ref, myTrim(result));
        result = doc.applyTemplate(template, data.getTemplateData());
        assertEquals(ref, myTrim(result));
        doc.applyTemplate(template, data.getTemplateData(), writer);
        assertEquals(ref, myTrim(writer.toString()));
        writer = new StringWriter();
        doc.applyTemplate(template, new FileReader(TEMPLATE_DATA_FILE), writer);
        assertEquals(ref, myTrim(writer.toString()));

        // Test methods with fields
        TemplateFields fields = template.getFields();
        ((Field) fields.getNodes()[0]).setValue("topcoder");
        result = doc.applyTemplate(fields);
        assertTrue(result.indexOf("Hello topcoder,") >= 0);
        writer = new StringWriter();
        doc.applyTemplate(fields, writer);
        assertTrue(writer.toString().indexOf("Hello topcoder,") >= 0);
    }

    /**
     * Remove spaces and CR/LF from the string
     * @param s
     *            The original string
     * @return the new string after removal of spaces and CR/LF
     */
    private String myTrim(String s) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != ' ' && c != '\r' && c != '\n') {
                buf.append(c);
            }
        }

        return new String(buf);
    }

    /**
     * Read the content from a file
     * @param reader
     *            The reader
     * @return the string of content
     * @throws Exception
     *             to junit.
     */
    private String readString(FileReader reader) throws Exception {
        // Accumulate buffer.
        StringBuffer sb = new StringBuffer();

        // Buffer for reading.
        char[] buffer = new char[1024];

        // Number of read chars.
        int n = 0;

        // Read all characters to string.
        while ((n = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, n);
        }
        reader.close();

        // Call same method with other parameter.
        return new String(sb);
    }

    /**
     * <p>
     * Accuracy test for <code>setTemplateSource</code>, <code>getTemplateSource</code>,
     * <code>removeTemplateSource</code>, <code>clearTemplateSources</code>.
     * </p>
     * <p>
     * Simply add something and get it.
     * </p>
     */
    public void testSetGetRemoveClearTemplateSource1() {
        doc.clearTemplateSources();
        doc.setTemplateSource("source1", source1);
        assertEquals("set/get/remove/clear TemplateSource incorrect", doc.getTemplateSource("source1"), source1);
    }

    /**
     * <p>
     * Accuracy test for <code>setTemplateSource</code>, <code>getTemplateSource</code>,
     * <code>removeTemplateSource</code>, <code>clearTemplateSources</code>.
     * </p>
     * <p>
     * Add three and remove one, then get them.
     * </p>
     */
    public void testSetGetRemoveClearTemplateSource2() {
        doc.clearTemplateSources();
        doc.setTemplateSource("source1", source1);
        doc.setTemplateSource("source2", source2);
        doc.setTemplateSource("source3", source3);
        doc.removeTemplateSource("source2");
        assertEquals("set/get/remove/clear TemplateSource incorrect", doc.getTemplateSource("source1"), source1);
        assertEquals("set/get/remove/clear TemplateSource incorrect", doc.getTemplateSource("source2"), null);
        assertEquals("set/get/remove/clear TemplateSource incorrect", doc.getTemplateSource("source3"), source3);
    }

    /**
     * <p>
     * Accuracy test for <code>setTemplateSource</code>, <code>getTemplateSource</code>,
     * <code>removeTemplateSource</code>, <code>clearTemplateSources</code>.
     * </p>
     * <p>
     * Set an existed one, and remove a non-existed one.
     * </p>
     */
    public void testSetGetRemoveClearTemplateSource3() {
        doc.clearTemplateSources();
        doc.setTemplateSource("source1", source1);
        doc.setTemplateSource("source1", source2);
        doc.setTemplateSource("source3", source3);
        doc.removeTemplateSource("source2");
        assertEquals("set/get/remove/clear TemplateSource incorrect", doc.getTemplateSource("source1"), source2);
        assertEquals("set/get/remove/clear TemplateSource incorrect", doc.getTemplateSource("source2"), null);
        assertEquals("set/get/remove/clear TemplateSource incorrect", doc.getTemplateSource("source3"), source3);
    }

    /**
     * <p>
     * Accuracy test for <code>setTemplateSource</code>, <code>getTemplateSource</code>,
     * <code>removeTemplateSource</code>, <code>clearTemplateSources</code>.
     * </p>
     * <p>
     * Set some and clear all.
     * </p>
     */
    public void testSetGetRemoveClearTemplateSource4() {
        doc.clearTemplateSources();
        doc.setTemplateSource("source1", source1);
        doc.setTemplateSource("source1", source2);
        doc.setTemplateSource("source3", source3);
        doc.removeTemplateSource("source2");
        doc.clearTemplateSources();
        assertEquals("set/get/remove/clear TemplateSource incorrect", doc.getTemplateSource("source1"), null);
        assertEquals("set/get/remove/clear TemplateSource incorrect", doc.getTemplateSource("source2"), null);
        assertEquals("set/get/remove/clear TemplateSource incorrect", doc.getTemplateSource("source3"), null);
    }

    /**
     * <p>
     * Accuracy test for <code>setTemplateSource</code>, <code>getTemplateSource</code>,
     * <code>removeTemplateSource</code>, <code>clearTemplateSources</code>.
     * </p>
     * <p>
     * Check the default value - null.
     * </p>
     */
    public void testSetGetDefaultTemplateSource1() {
        doc = new DocumentGenerator();
        assertEquals("set/getDefaultTemplateSource incorrect", doc.getDefaultTemplateSource(), null);
    }

    /**
     * <p>
     * Accuracy test for <code>setTemplateSource</code>, <code>getTemplateSource</code>,
     * <code>removeTemplateSource</code>, <code>clearTemplateSources</code>.
     * </p>
     * <p>
     * Set and get.
     * </p>
     */
    public void testSetGetDefaultTemplateSource2() {
        doc.setDefaultTemplateSource(source3);
        assertEquals("set/getDefaultTemplateSource incorrect", doc.getDefaultTemplateSource(), source3);
    }

    /**
     * <p>
     * Accuracy test for <code>setTemplateSource</code>, <code>getTemplateSource</code>,
     * <code>removeTemplateSource</code>, <code>clearTemplateSources</code>.
     * </p>
     * <p>
     * Set and reset.
     * </p>
     */
    public void testSetGetDefaultTemplateSource3() {
        doc.setDefaultTemplateSource(source3);
        doc.setDefaultTemplateSource(source2);
        assertEquals("set/getDefaultTemplateSource incorrect", doc.getDefaultTemplateSource(), source2);
    }
}
