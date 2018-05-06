/*
 * Copyright (C) 2007-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import com.topcoder.util.file.xslttemplate.XsltTemplate;
import junit.framework.TestCase;

/**
 * <p>
 * Title: TestXmlTemplateData
 * </p>
 * <p>
 * Description: Test whole <code>XmlTemlateData</code> class.
 * </p>
 * <p>
 * Company: TopCoder Software
 * </p> * @author TCSDEVELOPER, JGeeks
 * @version 3.1.1
 */
public class TestXmlTemplateData extends TestCase {
    /** This is template. */
    private static final String LITTLE = "# This is little template.\r\n" + "" + "%TITLE%\r\n"
        + "%loop:WORD{Write world}%%loop:ALPHA{Write alpha}%%alpha=a%%endloop%\r\n" + "%endloop%";

    /** This is template. */
    private static final String LITTLE_WITH_CONDITION = "# This is little template.\r\n"
        + "%TITLE%\r\n%if:VALUE>'100'{Test VALUE}%\r\nHELLO WORLD\r\n%endif%\r\n"
        + "%loop:WORD{Write world}%%loop:ALPHA{Write alpha}%%alpha=a%%endloop%\r\n" + "%endloop%";

    /** XmlTemplateData. */
    private XmlTemplateData xmlTemplateData = null;

    /**
     * Create new XmlTemplateData object.
     */
    public void setUp() {
        // Create new XmlTemlateData object.
        xmlTemplateData = new XmlTemplateData();
    }

    /**
     * Test {@link XmlTemplateData#XmlTemplateData()} constructor.
     */
    public void testConstructor() {
        // This object was created in setUp method.
        assertNotNull("The XmlTemplateData object was not created.", xmlTemplateData);
    }

    /**
     * Test {@link XmlTemplateData#setTemplateData(String)} method on good data.
     */
    public void testSetTemplateDataString() {
        // Set template data.
        xmlTemplateData.setTemplateData("This is new day.");

        // Check template data.
        assertEquals("The template data is incorrect", xmlTemplateData.getTemplateData(), "This is new day.");
    }

    /**
     * Test {@link XmlTemplateData#setTemplateData(String)} method with null as argument.
     */
    public void testSetTemplateDataStringWithNull() {
        try {
            // Try to set template data with null.
            xmlTemplateData.setTemplateData((String) null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link XmlTemplateData#setTemplateData(String)} method with empty string as argument.
     */
    public void testSetTemplateDataStringWithEmpty() {
        try {
            // Try to set template data with empty string.
            xmlTemplateData.setTemplateData("   ");

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link XmlTemplateData#setTemplateData(TemplateFields)} method on good data.
     * @throws TemplateFormatException
     *             should not throw
     */
    public void testSetTemplateDataTemplateFields() throws TemplateFormatException {
        // Create template.
        Template template = new XsltTemplate();

        // Set template.
        template.setTemplate(LITTLE);

        // Get template fields.
        TemplateFields fields = template.getFields();

        // Our fields have following structure.
        // field "TITLE"
        // loop "WORD"
        // loop "ALPHA"
        // field "apha"
        // Set template data.
        xmlTemplateData.setTemplateData(fields);

        // Check template data. Note now loop is empty so it will not be in XML data.
        assertEquals("The template data is incorrect", xmlTemplateData.getTemplateData(),
            "<DATA><TITLE><![CDATA[]]></TITLE></DATA>");

        // Now add some information to the fields.
        Node[] nodes = fields.getNodes();

        // Add titles.
        ((Field) nodes[0]).setValue("This is title");

        // Add one loop.
        NodeList subnodes = ((Loop) nodes[1]).addLoopItem();

        // To this "WORD" add two loop "ALPHA"
        ((Loop) subnodes.getNodes()[0]).addLoopItem();
        ((Loop) subnodes.getNodes()[0]).addLoopItem();

        // Set template data again.
        xmlTemplateData.setTemplateData(fields);

        // Check template data now. Now it contain some information.
        assertEquals("The template data is incorrect", xmlTemplateData.getTemplateData(),
            "<DATA><TITLE><![CDATA[This is title]]></TITLE>" + "<WORD>" + "<ALPHA><alpha><![CDATA[a]]></alpha></ALPHA>"
                + "<ALPHA><alpha><![CDATA[a]]></alpha></ALPHA>" + "</WORD></DATA>");
    }

    /**
     * Test {@link XmlTemplateData#setTemplateData(TemplateFields)} method with null as argument.
     */
    public void testSetTemplateDataTemplateFieldsWithNull() {
        try {
            // Try to set template data with null.
            xmlTemplateData.setTemplateData((TemplateFields) null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link XmlTemplateData#getTemplateData()} method.
     */
    public void testGetTemplateData() {
        // Set template data.
        xmlTemplateData.setTemplateData("This is string.");

        // Check template data.
        assertEquals("The getTemplateData method work incorrectly.", xmlTemplateData.getTemplateData(),
            "This is string.");
    }

    /**
     * Test {@link XmlTemplateData#getTemplateData()} method, when template data was not set.
     */
    public void testGetTemplateDataIllegalState() {
        try {
            // Try to get template data.
            // Note : template data was not set.
            xmlTemplateData.getTemplateData();

            // Fail.
            fail("Should throw IllegalStateException.");
        } catch (IllegalStateException e) {
            // Success.
        }
    }

    /**
     * Test {@link XmlTemplateData#setTemplateData(TemplateFields)} method on good data.
     * @throws TemplateFormatException
     *             should not throw
     * @since 2.1
     */
    public void testSetTemplateDataTemplateFieldsWithCondition() throws TemplateFormatException {
        // Create template.
        Template template = new XsltTemplate();

        // Set template.
        template.setTemplate(LITTLE_WITH_CONDITION);

        // Get template fields.
        TemplateFields fields = template.getFields();

        // Our fields have following structure.
        // field "TITLE"
        // if "VALUE"
        // loop "WORD"
        // loop "ALPHA"
        // field "apha"

        // Set template data.
        xmlTemplateData.setTemplateData(fields);

        // Check template data. Note now loop is empty so it will not be in XML data.
        assertEquals("The template data is incorrect", "<DATA><TITLE><![CDATA[]]></TITLE><VALUE></VALUE></DATA>",
            xmlTemplateData.getTemplateData());

        // Now add some information to the fields.
        Node[] nodes = fields.getNodes();

        // Add titles.
        ((Field) nodes[0]).setValue("This is title");

        // Set the condition value
        ((Condition) nodes[1]).setValue("140");

        // Add one loop.
        NodeList subnodes = ((Loop) nodes[2]).addLoopItem();

        // To this "WORD" add two loop "ALPHA"
        ((Loop) subnodes.getNodes()[0]).addLoopItem();
        ((Loop) subnodes.getNodes()[0]).addLoopItem();

        // Set template data again.
        xmlTemplateData.setTemplateData(fields);

        // Check template data now. Now it contain some information.
        assertEquals("The template data is incorrect", xmlTemplateData.getTemplateData(),
            "<DATA><TITLE><![CDATA[This is title]]></TITLE><VALUE>140</VALUE>" + "<WORD>"
                + "<ALPHA><alpha><![CDATA[a]]></alpha></ALPHA>" + "<ALPHA><alpha><![CDATA[a]]></alpha></ALPHA>" + "</WORD></DATA>");
    }
}
