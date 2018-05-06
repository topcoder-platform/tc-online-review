/*
 * Copyright (C) 2007-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.xslttemplate;

import com.topcoder.util.file.TemplateData;
import com.topcoder.util.file.Util;
import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.file.fieldconfig.TemplateFields;

/**
 * <p>
 * Implementation of the TemplateData interface that uses XML as format for
 * template input data.
 * </p>
 *
 * <p>
 * Changed in version 2.1: Support for the Condition node was added.
 * </p>
 *
 * <p>
 * Changed in version 3.1.1: The field value is now set within the CDATA
 * section to properly handle special characters in the XML.
 * </p>
 *
 * <p>
 * Thread Safety : This class is mutable and so is not thread safe.
 * </p>
 *
 * @author adic, roma
 * @author ShindouHikaru, TCSDEVELOPER, JGeeks
 * @version 3.1.1
 * @since 2.0
 */
public class XmlTemplateData implements TemplateData {
    /**
     * <p>
     * This is the XML representation of the template data.
     * </p>
     *
     * <p>
     * Initialized In: Constructor (to a null)
     * </p>
     * <p>
     * Accessed In: getTemplateData
     * </p>
     * <p>
     * Modified In: setTemplateData
     * </p>
     * <p>
     * Utilized In: Not utilized in this class
     * </p>
     * <p>
     * Valid Values: Possibly Null (before initialization), but never empty Strings
     * </p>
     *
     * @since 2.0
     */
    private String xmlData;

    /**
     * Constructor.
     *
     * @since 2.0
     */
    public XmlTemplateData() {
        // empty
    }

    /**
     * Sets the template XML data from a string.
     *
     * @param xmlData the string representing the XML template data
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     * or empty
     *
     * @since 2.0
     */
    public void setTemplateData(String xmlData) {
        // Check for null and empty string.
        Util.checkString(xmlData, "xmlData");

        // Set template data.
        this.xmlData = xmlData;
    }

    /**
     * <p>
     * Sets the template data from the data structure of the API for programmatic
     * field configuration.
     * </p>
     *
     * <p>
     * This method will need to explore the given data structure and create corresponding
     * XML content for it. This can be easily achieved with a two recursive functions.
     * </p>
     *
     * <p>
     * Changed in Version 2.1 : This has to be modified to also support the Condition node.
     * </p>
     *
     * @param fields the root of the data structure with programmatically
     * configured template data
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     *
     * @since 2.0
     */
    public void setTemplateData(TemplateFields fields) {
        // Check for null first of all.
        Util.checkNull(fields, "fields");

        // Accumulate XML data.
        StringBuffer buffer = new StringBuffer();
        // Write begin of the data.
        buffer.append("<DATA>");
        // Write all data.
        write(buffer, fields);
        // Write end of the data.
        buffer.append("</DATA>");
        // Set template data property.
        xmlData = new String(buffer);
    }

    /**
     * Returns the XML template data.
     *
     * @return the template data
     * @throws IllegalStateException if setTemplateData was not called
     * successfully before.
     *
     * @since 2.0
     */
    public String getTemplateData() {
        // Check that we have template data.
        if (xmlData == null) {
            // If templateData is null, then it was not set with set method.
            throw new IllegalStateException("The setTemplateData method was not called before.");
        }

        // Return the template data.
        return xmlData;
    }

    /**
     * Writes field to the buffer in XML format.
     *
     * @param buffer accumulate XML data
     * @param field field for writing
     *
     * @since 2.0
     */
    private void write(StringBuffer buffer, Field field) {
        // Write open tag.
        buffer.append("<" + field.getName() + ">");
        // Get value.
        String value = field.getValue();
        if (value == null) {
            // If value is null, we will write it as empty string.
            value = "";
        }
        // Write value.
        buffer.append("<![CDATA["); //BUGR-4595
        buffer.append(value.replaceAll("]]>","]]]]><![CDATA[>"));
        buffer.append("]]>");
        // Write end tag.
        buffer.append("</" + field.getName() + ">");
    }

    /**
     * Writes loop to the buffer in XML format.
     *
     * @param buffer accumulate XML data
     * @param loop loop for writing
     *
     * @since 2.0
     */
    private void write(StringBuffer buffer, Loop loop) {
        // Get subelements.
        NodeList[] nodeLists = loop.getLoopItems();
        // Write all subelement.
        for (int i = 0; i < nodeLists.length; i++) {
            // Write open loop tag.
            buffer.append("<" + loop.getLoopElement() + ">");
            // Write subelement.
            write(buffer, nodeLists[i]);
            // Write end loop tag.
            buffer.append("</" + loop.getLoopElement() + ">");
        }
    }

    /**
     * Writes node list (one sub-element of list) in XML format.
     *
     * @param buffer accumulate XML data
     * @param nodeList node list for writing
     *
     * @since 2.0
     */
    private void write(StringBuffer buffer, NodeList nodeList) {
        // Get all subelements.
        Node[] nodes = nodeList.getNodes();

        // Write all subelements.
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof Field) {
                // If this is field, then write it as field.
                write(buffer, (Field) nodes[i]);
            } else if (nodes[i] instanceof Condition) {
                // If this is condition, then write it as condition.
                write(buffer, (Condition) nodes[i]);
            } else {
                // If this is loop, then write it as loop.
                write(buffer, (Loop) nodes[i]);
            }
        }
    }

    /**
     * Writes condition to the buffer in XML format.
     *
     * @param buffer accumulate XML data
     * @param condition condition node for writing
     *
     * @since 2.1
     */
    private void write(StringBuffer buffer, Condition condition) {
        // Write open tag.
        buffer.append("<" + condition.getName() + ">");

        // Write value.
        buffer.append(condition.getValue());

        // Write end tag.
        buffer.append("</" + condition.getName() + ">");

        write(buffer, condition.getSubNodes());
    }
}
