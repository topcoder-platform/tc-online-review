/**
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.file.accuracytests;

import java.io.FileReader;

import junit.framework.TestCase;

import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import com.topcoder.util.file.xslttemplate.XsltTemplate;

/**
 * <p>Test the XsltTemplate class</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class XsltTemplateAccuracyTests extends TestCase 
{
    /**
     * The template file name
     */
    public static String TEMPLATE_FILE = "test_files/accuracy2.1/AccuracyTemplate.txt";

    /**
     * The result file name
     */
    public static String RESULT_FILE = "test_files/accuracy2.1/AccuracyResult.txt";

     /**
     * The template data file name
     */
    public static String TEMPLATE_DATA_FILE = "test_files/accuracy2.1/AccuracyTemplateData.xml";

    /**
     * XsltTemplate instance for test.
     */
    XsltTemplate xt = null;

    /**
     * FileTemplateSource instance for test.
     */
    FileTemplateSource fts = null;

    /**
     * Initialize XsltTemplate instance
     * 
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        xt = new XsltTemplate();
        fts = new FileTemplateSource();
        xt.setTemplate(fts.getTemplate(TEMPLATE_FILE));
    }

    /**
     * Tests the setTemplate and getTemplate methods.
     * 
     * @throws Exception to JUnit
     */
    public void testTemplate() throws Exception {
        assertEquals(fts.getTemplate(TEMPLATE_FILE), xt.getTemplate());
    }

    /**
     * Tests the getFields method.
     * 
     * @throws Exception to JUnit
     */
    public void testGetFields() throws Exception {
        assertTrue(xt.getFields().getTemplate() == xt);

        Node[] nodes = xt.getFields().getNodes();
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
     * 
     * @throws Exception to JUnit
     */
    public void testApplyTemplate() throws Exception {
        XmlTemplateData data = new XmlTemplateData();

        data.setTemplateData(fts.getTemplate(TEMPLATE_DATA_FILE));
        String result = xt.applyTemplate(data);

        // Read the reference
        FileReader reader = new FileReader(RESULT_FILE);
        String ref = readString(reader);
        reader.close();
        System.out.println(result);
        assertEquals(myTrim(ref), myTrim(result));
    }

    /**
     * Remove spaces and CR/LF from the string
     * 
     * @param s The original string
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
     * 
     * @param reader The reader
     * @return the string of content
     * @throws Exception to junit
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

        // Call same method with other parameter.
        return new String(sb);
    }
}
