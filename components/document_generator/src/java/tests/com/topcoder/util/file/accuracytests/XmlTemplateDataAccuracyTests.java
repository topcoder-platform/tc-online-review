/**
 * Copyright (C) 2004-2011, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.file.accuracytests;

import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import com.topcoder.util.file.xslttemplate.XsltTemplate;
import com.topcoder.util.file.templatesource.FileTemplateSource;

import junit.framework.TestCase;

/**
 * <p>Test the XmlTemplateData class</p>
 *
 * @author TCSDEVELOPER, JGeeks
 * @version 3.1.1
 */
public class XmlTemplateDataAccuracyTests extends TestCase 
{
    /**
     * The template file name
     */
    public static String TEMPLATE_FILE = "test_files/AccuracyTemplate.txt";

    /**
     * XmlTemplateData instance for test.
     */
    XmlTemplateData xtd = null;

    /**
     * FileTemplateSource instance for test.
     */
    FileTemplateSource fts = null;

    /**
     * Initialize XsltTemplate instance
     */
    public void setUp() {
        xtd = new XmlTemplateData();
        fts = new FileTemplateSource();
    }

    /**
     * Tests the setTemplateData and getTemplateData methods with string.
     * 
     * @throws Exception to JUnit
     */
    public void testTemplateDataString() throws Exception {
        xtd.setTemplateData("Test for template data");
        
        assertEquals("Test for template data", xtd.getTemplateData());
    }

    /**
     * Tests the setTemplateData and getTemplateData methods with fields.
     * 
     * @throws Exception to JUnit
     */
    public void testTemplateDataField() throws Exception {
        Node[] nodes = new Node[1];
            
        nodes[0] = new Field("testName2", "testVal2", "testDesc2", true);
        Loop loop = new Loop("innerElement", new NodeList(nodes), "loopDesc", false);
        nodes = new Node[2];
        nodes[0] = new Field("testName", "testVal]]>", "testDesc", false);
        nodes[1] = loop;
        xtd.setTemplateData(new TemplateFields(nodes, new XsltTemplate()));

        assertEquals("<DATA><testName><![CDATA[testVal]]]]><![CDATA[>]]></testName></DATA>",
                   myTrim(xtd.getTemplateData()));
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
}
