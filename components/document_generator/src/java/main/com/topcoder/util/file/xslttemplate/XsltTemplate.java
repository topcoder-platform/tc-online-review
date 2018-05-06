/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.xslttemplate;

import com.topcoder.util.file.Template;
import com.topcoder.util.file.TemplateData;
import com.topcoder.util.file.TemplateDataFormatException;
import com.topcoder.util.file.TemplateFormatException;
import com.topcoder.util.file.Util;
import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Loop;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * <p>
 * This class is an implementation of the {@link Template} interface that uses
 * XSL transformations as method for applying template transformations to input
 * data, in order to generate text content.
 * </p>
 *
 * <p>
 * The idea behind this approach is to convert the template format defined by
 * this component to XSLT by simple text replacements.
 * </p>
 *
 * <p>
 * The input data is given in XML format (directly or indirectly thought the API for
 * programmatic data input). This way the text generation is reduced to a simple
 * XSL transformations.
 * </p>
 *
 * <p>
 * Changed in Version 2.1: This class has been enhanced to support conditional "if"
 * statements in the document template, and fix some bugs with regards to invalid
 * template characters.
 * </p>
 *
 * <p>
 * Thread-Safety: Thread safety is not necessary for this class. It is expected to be initialized
 * by the component and not modified afterwards.
 * </p>
 *
 * @author adic, roma
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 2.1
 * @since 2.0
 */
public class XsltTemplate implements Template {
    /**
     * The header of the XSL transformation file.
     *
     * @since 2.0
     */
    private static final String XSL_HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
        + "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"\n"
        + "                xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"\n" + "                version=\"1.0\" >\n"
        + "<xsl:output method=\"text\" indent=\"yes\"/>\n" + "<xsl:template match=\"/\">\n"
        + "<xsl:for-each select=\"DATA\">\n" + "<xsl:text>";

    /**
     * The footer of the XSL transformation file.
     *
     * @since 2.0
     */
    private static final String XSL_FOOTER = "</xsl:text>\n" + "</xsl:for-each>\n" + "</xsl:template>\n"
        + "</xsl:stylesheet>\n";

    /**
     * The text to insert for the special loop field, before the loop name.
     *
     * @since 2.0
     */
    private static final String XSL_LOOP1 = "</xsl:text>" + "<xsl:for-each select=\"";

    /**
     * The text to insert for the special loop field, after the loop name.
     *
     * @since 2.0
     */
    private static final String XSL_LOOP2 = "\">" + "<xsl:text>";

    /**
     * The text to insert for the end of loop special field.
     *
     * @since 2.0
     */
    private static final String XSL_LOOPEND = "</xsl:text>" + "</xsl:for-each>" + "<xsl:text>";

    /**
     * The text to insert for a data field, before the field name.
     *
     * @since 2.0
     */
    private static final String XSL_VALUE1 = "</xsl:text>" + "<xsl:value-of select=\"";

    /**
     * The text to insert for a data field, after the field name.
     *
     * @since 2.0
     */
    private static final String XSL_VALUE2 = "\"/>" + "<xsl:text>";

    /**
     * This is escape characters.
     *
     * @since 2.0
     */
    private static final String ESCAPE_CHARS = "{}%:=\\";

    /**
     * This is the number comparators array used in the if condition statement.
     *
     * @since 2.1
     */
    private static final String[] NUMBER_COMPARATORS = new String[] {"&lt;=", "&gt;=", "&lt;", "&gt;"};

    /**
     * This is the other comparators array used in the if condition statement.
     *
     * @since 2.1
     */
    private static final String[] COMPARATORS = new String[] {"!=", "="};

    /**
     * <p>
     * This is the constant that represents the start of an XSL:if statement.
     * </p>
     *
     * <p>
     * It comprises the start of the opening tag, until the value of the 'test' variable condition.
     * </p>
     *
     * <p>
     * It will be used when generating the XSL template supporting a template %if% condition.
     * </p>
     *
     * @since 2.1
     */
    private static final String XSL_IF1 = "</xsl:text><xsl:if test=\"";

    /**
     * <p>
     * This is the constant the represents the closing part of the XSL:if opening tag.
     * </p>
     *
     * <p>
     * It will comprise the parts after the "test" attribute is added.
     * </p>
     *
     * <p>
     * It will be used when generating the XSL template supporting a template %if% condition.
     * </p>
     *
     * @since 2.1
     */
    private static final String XSL_IF2 = "\"><xsl:text>";

    /**
     * <p>
     * This is the closing tag of an XSL:if condition.
     * </p>
     *
     * <p>
     * It is rendered as a counterpart to the %endif% part of a document template.
     * </p>
     *
     * @since 2.1
     */
    private static final String XSL_IFEND = "</xsl:text></xsl:if><xsl:text>";

    /**
     * Used when we parse loop, we should return loop structure, but we have
     * not subnodes, so we pass this.
     *
     * @since 2.0
     */
    private static final NodeList EMPTY_NODE_LIST = new NodeList(new Node[] {});

    /**
     * <p>
     * Holds text of template.
     * </p>
     * <p>
     * Initialized In: Constructor (to a null)
     * </p>
     * <p>
     * Accessed In: getTemplate
     * </p>
     * <p>
     * Modified In: setTemplate
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
    private String templateText;

    /**
     * <p>
     * This value is used to hold the parsed NodeList that is generated from the
     * template text.
     * </p>
     * <p>
     * Initialized In: setTemplate
     * </p>
     * <p>
     * Accessed In: Not directly accessed (although is used in getFields)
     * </p>
     * <p>
     * Modified In: setTemplate
     * </p>
     * <p>
     * Utilized In: getFields
     * </p>
     * <p>
     * Valid Values: Possibly null (before initialization) nodeList.
     * </p>
     *
     * @since 2.0
     */
    private NodeList nodeList;

    /**
     * <p>
     * This will hold the compiled transformer which is produced after the template has been set.
     * </p>
     * <p>
     * Initialized In: setTemplate
     * </p>
     * <p>
     * Accessed In: Not accessed
     * </p>
     * <p>
     * Modified In: setTemplate
     * </p>
     * <p>
     * Utilized In: ApplyTemplate
     * </p>
     * <p>
     * Valid Values: Possibly Null (before initialization), Transformer
     * </p>
     *
     * @since 2.0
     */
    private Transformer transformer;

    /**
     * Constructor.
     *
     * @since 2.0
     */
    public XsltTemplate() {
        // empty
    }

    /**
     * <p>
     * Sets the template text.
     * </p>
     *
     * <p>
     * The method parses the template text and produces the equivalent XSL transformation.
     * </p>
     *
     * <p>
     * The template is parsed as shown in the algorithms section in the spec and
     * some semantic checks are performed also in the effort to provide a
     * meaningful error message to the user.
     * </p>
     *
     * <p>
     * It then compiles the XSL transformation so that it can be subsequently applied multiple
     * times without needing recompilation.
     * </p>
     *
     * <p>
     * Changed in Version 2.1:
     * <li>A bug fix was introduced that allows support of the &quot;&lt;&quot;, &quot;&gt;&quot;
     * and &quot;}&quot; characters.</li>
     * <li>Support for condition template %if% statements</li>
     * </p>
     *
     * @param templateText the template text
     *
     * @throws IllegalArgumentException if the argument is <code>null</code> or empty
     * @throws TemplateFormatException if the format of the template is
     * invalid : <ul><li> if there is some syntax problem </li>
     * <li> if the XSL transformation compilation fails (the XSLT
     * exception is wrapped in this exception) </li> </ul>
     *
     * @since 2.0
     */
    public void setTemplate(String templateText) throws TemplateFormatException {
        // Check for null and empty string.
        Util.checkString(templateText, "templateText");

        // Remove comments and replace escaped characters with one character
        // from private use area.
        // Note : we add '%endloop%' for parseNodeList method.
        String template = handleEscapeChars(removeComments(templateText));
        // It will accumulate the XSLT template.
        StringBuffer buffer = new StringBuffer(template.length());

        // Note : we should return '%' as token in order to process empty tag.
        StringTokenizer tokenizer = new StringTokenizer(template, "%", true);

        // Write begin of the template.
        buffer.append(XSL_HEADER);

        // Parse template and return NodeList.
        nodeList = parseNodeList(buffer, tokenizer);

        // Write end of the template.
        buffer.append(XSL_FOOTER);

        // We should process all template.
        if (tokenizer.hasMoreTokens()) {
            throw new TemplateFormatException("End of loop was found, where not expected.");
        }

        // Get factory.
        TransformerFactory factory = TransformerFactory.newInstance();
        try {
            // Get transformer.
            transformer = factory.newTransformer(new StreamSource(new StringReader(new String(buffer))));
        } catch (TransformerConfigurationException e) {
            // If some exception happens than just rethrow it.
            throw new TemplateFormatException(e.getMessage(), e);
        }

        // Set property.
        this.templateText = templateText;
    }

    /**
     * Gets the template text.
     *
     * @return the template text
     *
     * @since 2.0
     */
    public String getTemplate() {
        return templateText;
    }

    /**
     * Process the template as shown in the algorithms section and produce a
     * data structure to be used for configuring programmatically input data
     * for this template.
     *
     * @return a TemplateFields instance
     * @throws TemplateFormatException if the format of the template is
     * invalid as specified in the algorithm
     *
     * @since 2.0
     */
    public TemplateFields getFields() throws TemplateFormatException {
        // Check that we set template before.
        if (transformer == null) {
            throw new TemplateFormatException("The setTemplate method was not called before.");
        }

        // Get the new copy of TemplateFields.
        return getTemplateFieldsCopy(nodeList);
    }

    /**
     * Applies the XSL transformation to the given XML template data and
     * produces the output text.
     *
     * @return the output text
     * @param templateData the template data (instance of XmlTemplateData)
     * @throws IllegalArgumentException if the argument is <code>null</code>
     * or not instance of XmlTemplateData
     * @throws TemplateFormatException if an XSLT exception occurs
     * indicating invalid template (wrapped)
     * @throws TemplateDataFormatException if an XSLT/XML transform occurs
     * indicating invalid template data (wrapped)
     *
     * @since 2.0
     */
    public String applyTemplate(TemplateData templateData) throws TemplateFormatException,
        TemplateDataFormatException {
        // Check that templateData is not null and that templateData instance
        // of XmlTemplateData.
        if ((templateData == null) || !(templateData instanceof XmlTemplateData)) {
            throw new IllegalArgumentException("The argument is null or not instance of XmlTemplateData.");
        }

        // Check that we set template before.
        if (transformer == null) {
            throw new TemplateFormatException("The setTemplate method was not called before.");
        }

        // Create writer.
        Writer writer = new StringWriter();
        try {
            // Apply transformation.
            transformer.transform(new StreamSource(new StringReader(templateData.getTemplateData())), new StreamResult(
                writer));
            // Return result of transformation.
            return writer.toString();
        } catch (Exception e) {
            // Note : XmlTemplateData.getTemplateData can throw IllegalStateException.
            throw new TemplateDataFormatException(e.getMessage(), e);
        }
    }

    /**
     * Returns new copy of the <code>TemplateFields</code>, in which first level
     * fields are not read-only, other fields and loops are read-only.
     *
     * @param nodeList the root of the <code>TemplateField</code>
     *
     * @return return new copy of the <code>TemplateFields</code>
     *
     * @since 2.0
     */
    private TemplateFields getTemplateFieldsCopy(NodeList nodeList) {
        return new TemplateFields(getNodeListCopy(nodeList, false).getNodes(), this);
    }

    /**
     * Gets deep copy of node list.
     *
     * @param nodeList node list
     * @param readOnly should be nodes read-only?
     *
     * @return new copy of node list
     *
     * @since 2.0
     */
    private NodeList getNodeListCopy(NodeList nodeList, boolean readOnly) {
        // Get new copy of nodes.
        Node[] nodes = nodeList.getNodes();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] instanceof Field) {
                // Get copy of field.
                nodes[i] = getFieldCopy((Field) nodes[i], readOnly);
            } else if (nodes[i] instanceof Condition) {
                // Get copy of condition.
                nodes[i] = getConditionCopy((Condition) nodes[i], readOnly);
            } else {
                // Get deep copy of loop.
                nodes[i] = getLoopCopy((Loop) nodes[i], readOnly);
            }
        }

        // Return new copy of node list.
        return new NodeList(nodes);
    }

    /**
     * Gets copy of the given condition.
     *
     * @param condition condition for copy
     * @param readOnly read-only argument for new copy of condition?
     *
     * @return return copy of the condition
     *
     * @since 2.1
     */
    private Condition getConditionCopy(Condition condition, boolean readOnly) {
        // Just return copy of the condition.
        return new Condition(condition.getName(), getNodeListCopy(condition.getSubNodes(), readOnly),
            condition.getDescription(), readOnly, condition.getConditionalStatement());
    }

    /**
     * Gets copy of the given field.
     *
     * @param field field for copy
     * @param readOnly read-only argument for new copy of field?
     *
     * @return return copy of the field
     *
     * @since 2.0
     */
    private Field getFieldCopy(Field field, boolean readOnly) {
        // Just return copy of the field.
        return new Field(field.getName(), field.getValue(), field.getDescription(), readOnly);
    }

    /**
     * Gets deep copy of the given loop.
     *
     * @param loop loop for copy
     * @param readOnly  whether is read only or not
     *
     * @return return copy of loop
     *
     * @since 2.0
     */
    private Loop getLoopCopy(Loop loop, boolean readOnly) {
        // Return copy of loop.
        return new Loop(loop.getLoopElement(), getNodeListCopy(loop.getSampleLoopItem(), readOnly),
            loop.getDescription(), readOnly);
    }

    /**
     * Removes comments from template. Note: this method also add end of loop
     * tag.
     *
     * @param template template with comments
     *
     * @return template without comments
     *
     * @since 2.0
     */
    private StringBuffer removeComments(String template) {
        // Get line separator.
        String lineSeparator = System.getProperty("line.separator");

        // Create buffer for template without comments.
        StringBuffer buffer = new StringBuffer(template.length());

        // Begin of the string.
        int first = 0;

        // End of the string.
        int last = 0;

        while ((last = template.indexOf(lineSeparator, first)) != -1) {
            if (template.charAt(first) != '#') {
                // If first character is '#' then this is comment.
                buffer.append(template.substring(first, last + lineSeparator.length()));
            }

            // Go to next line.
            first = last + lineSeparator.length();
        }

        // Work with last line, because last line can have not line separator
        // in the end.
        if ((first < template.length()) && (template.charAt(first) != '#')) {
            buffer.append(template.substring(first, template.length()));
        }

        // Add end of loop as mark.
        buffer.append("%endloop%");

        // Return template without comments.
        return buffer;
    }

    /**
     * Replaces escape characters to back to usual.
     *
     * @param string string with escape characters (it in private use area)
     *
     * @return return string without escape characters
     *
     * @since 2.0
     */
    private String backEscapeChars(String string) {
        // Input chars.
        char[] escaped = string.toCharArray();

        // Output chars.
        char[] chars = new char[string.length()];

        for (int i = 0; i < chars.length; i++) {
            // This is private use area, please see Character.java
            if ((escaped[i] ^ '\uE000') < 256) {
                chars[i] = (char) (escaped[i] ^ '\uE000');
            } else {
                chars[i] = escaped[i];
            }
        }

        // Return string with usual characters.
        return new String(chars);
    }

    /**
     * Replaces escape characters(two chars) with character from private use
     * area(one char).
     *
     * @param template the template with escape characters as two char
     *
     * @return template with escape characters as one chars
     *
     * @throws TemplateFormatException if not valid escape characters found
     *
     * @since 2.0
     */
    private String handleEscapeChars(StringBuffer template) throws TemplateFormatException {
        // Input/output chars.
        char[] chars = new String(template).toCharArray();

        StringBuffer result = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\\') {
                // This is escape character.
                i++;

                // Note : we increase index and then compare it and use it.
                if ((i < chars.length)
                    && ((chars[i] == '%') || (chars[i] == '{') || (chars[i] == '}') || (chars[i] == '=')
                        || (chars[i] == '\\') || (chars[i] == '#') || (chars[i] == ':'))) {
                    // Note : I use private use area.
                    // You can try to find 'Private Use' words in Character.java file.
                    result.append((char) ('\uE000' | chars[i]));
                } else {
                    throw new TemplateFormatException("The are incorrect escape chars.");
                }
            } else if (chars[i] == '<') {
                result.append("&lt;");
            } else if (chars[i] == '>') {
                result.append("&gt;");
            } else {
                // This is usual character.
                result.append(chars[i]);
            }
        }

        // Return string with escape characters as one char.
        return result.toString();
    }

    /**
     * Gets next token, if <code>tokenizer</code> have not token, then throw an
     * <code>TemplateFormatException</code> with given message.
     *
     * @param tokenizer the tokenizer
     * @param message the message for exception
     *
     * @return return next token
     *
     * @throws TemplateFormatException throw if tokenizer have not token
     *
     * @since 2.0
     */
    private String getNextToken(StringTokenizer tokenizer, String message) throws TemplateFormatException {
        if (tokenizer.hasMoreTokens()) {
            // If we have token than just return it.
            return tokenizer.nextToken();
        } else {
            // If have not token than throw this exception.
            throw new TemplateFormatException(message);
        }
    }

    /**
     * Parses the circle or root elements of the template.
     *
     * @param buffer write XSLT template here
     * @param tokenizer tokenizer of our template
     *
     * @return parsed NodeList.
     *
     * @throws TemplateFormatException if some error in the syntax of the
     *         template
     *
     * @since 2.0
     */
    private NodeList parseNodeList(StringBuffer buffer, StringTokenizer tokenizer) throws TemplateFormatException {
        // This list contain nodes of this NodeList, this can be subnodes
        // of the loop or root-level nodes.
        List nodes = new ArrayList();

        // This is circle forever, we will exit from it, when we get :
        // 1. an error during parsing
        // 2. get %endloop% tag
        while (true) {
            // Get next token.
            // Template should contain %endloop% at the end, we add it before.
            String token = getNextToken(tokenizer, "Unexpected end of template.");

            if (token.equals("%")) {
                // If this is begin of the tag, then read other part of tag.
                String tag = getNextToken(tokenizer, "Can not find end of tag.");

                if (tag.equals("%")) {
                    // This is empty tag ('%%').
                    throw new TemplateFormatException("Empty tag was found.");
                }

                // Now, we try to read end of the tag.
                // Read '%' symbol, end of tag.
                getNextToken(tokenizer, "Can not find end of tag.");

                // Now we will process tag.
                if (tag.equals("endloop") || tag.equals("endif")) {
                    // If this end of loop, %endloop% or end of if, "%endif%", then break this cycle.
                    break;
                }

                // If this is begin of loop, %loop:variable[{description}]%
                if (tag.startsWith("loop:")) {
                    // Parse loop.
                    Loop loop = parseLoop(tag);

                    // Write begin of the loop-title.
                    buffer.append(XSL_LOOP1);

                    // Write the name of the filed (variable).
                    buffer.append(backEscapeChars(loop.getLoopElement()));

                    // Write end of the loop-title.
                    buffer.append(XSL_LOOP2);

                    // Parse subnodes.
                    NodeList newNodeList = parseNodeList(buffer, tokenizer);

                    // Write end of the loop.
                    buffer.append(XSL_LOOPEND);

                    // Add this parsed loop to the our nodes.
                    nodes.add(new Loop(loop.getLoopElement(), newNodeList, loop.getDescription(), true));
                } else if (tag.startsWith("if:")) {
                    // Parse if.
                    Condition condition = parseCondition(tag);

                    // Write begin of the if-title.
                    buffer.append(XSL_IF1);

                    // Write the name of the filed (variable).
                    buffer.append(backEscapeChars(condition.getConditionalStatement()));

                    // Write end of the if-title.
                    buffer.append(XSL_IF2);

                    // Parse subnodes.
                    NodeList newNodeList = parseNodeList(buffer, tokenizer);

                    // Write end of the if.
                    buffer.append(XSL_IFEND);

                    // Add this parsed condition to the our nodes.
                    nodes.add(new Condition(condition.getName(), newNodeList, condition.getDescription(), true,
                        condition.getConditionalStatement()));
                } else {
                    // This is field.
                    // Parse field.
                    Field field = parseField(tag);

                    // Write begin of the filed.
                    buffer.append(XSL_VALUE1);

                    // Write name of the field.
                    buffer.append(backEscapeChars(field.getName()));

                    // Write end of the field.
                    buffer.append(XSL_VALUE2);

                    // Add this parsed field to the our nodes.
                    nodes.add(field);
                }
            } else {
                // This is just text. But we will check that it have not escape chars.
                if (haveSameChars(token, ESCAPE_CHARS)) {
                    throw new TemplateFormatException("The text [" + token + "] have forbidden chars : "
                        + ESCAPE_CHARS);
                }

                // Write this text.
                buffer.append(backEscapeChars(token));
            }
        }

        // Copy nodes from ArrayList to the array of Node.
        Object[] objects = nodes.toArray();
        Node[] nodesTemp = new Node[objects.length];
        System.arraycopy(objects, 0, nodesTemp, 0, objects.length);

        return new NodeList(nodesTemp);
    }

    /**
     * Parses condition from given string, the syntax of the condition following,
     * 'variable COMPARATOR 'VALUE' [{description}]', where words in [] can be
     * absent.
     *
     * @param tag condition tag
     *
     * @return parsed condition
     *
     * @throws TemplateFormatException if given string is not correct condition tag
     *
     * @since 2.1
     */
    private Condition parseCondition(String tag) throws TemplateFormatException {
        // Remove first three chars from tag, this is always will be 'if:'
        tag = tag.substring(3).trim();

        // The description for if.
        String description = null;

        // Look for begin of description.
        int openEar = tag.indexOf('{');

        // If we find begin of the description, then:
        // 1) We have correct description, than we will change variable
        // and description;
        // 2) We have incorrect description, than we throw an exception.
        if (openEar != -1) {
            // Get description, note we also verify the description,
            // in this method.
            description = parseDescription(tag.substring(openEar));

            tag = tag.substring(0, openEar).trim();
        }

        String value = parseIfConditionValue(tag);
        tag = tag.substring(0, tag.length() - value.length()).trim();

        String comparator = parseComparator(tag, value);

        String variable = tag.substring(0, tag.length() - comparator.length()).trim();

        // Check the syntax of variable.
        checkVariable(variable);

        // Return if condition
        return new Condition(variable, EMPTY_NODE_LIST, description, true, variable + comparator + value);
    }

    /**
     * <p>
     * This method parses the comparator part in the condition statement.
     * </p>
     *
     * @param tag the condition tag
     * @param value the value part in the condition statement
     * @return the comparator string
     *
     * @throws TemplateFormatException if unable to parse the compartor part of the value is
     * invalid according to the comparator
     *
     * @since 2.1
     */
    private String parseComparator(String tag, String value) throws TemplateFormatException {
        String comparator = endsWith(tag, NUMBER_COMPARATORS);
        if (comparator != null) {
            // remove the two '' characters
            value = value.substring(1, value.length() - 1);

            try {
                Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new TemplateFormatException(
                    "The value is not a double number when the comparator is one of <, >, >=, <=.");
            }

            return comparator;
        }

        comparator = endsWith(tag, COMPARATORS);
        if (comparator == null) {
            throw new TemplateFormatException(
                "The comparator in the if condition is missing. One of the following characters "
                    + "are required : <, >, >=, >=, =, !=.");
        }

        return comparator;
    }

    /**
     * <p>
     * This method checks whether the given string array has string that ends with the given value.
     * </p>
     *
     * <p>
     * If the string is found, then it will be returned, otherwise null will be returned.
     * </p>
     *
     * @param value the value to check
     * @param patterns the string array to check
     * @return the matched string in the patterns array, or null if not match is found
     *
     * @since 2.1
     */
    private String endsWith(String value, String[] patterns) {
        for (int i = 0; i < patterns.length; i++) {
            if (value.endsWith(patterns[i])) {
                return patterns[i];
            }
        }

        return null;
    }

    /**
     * <p>
     * This method parses the value part in the condition statement.
     * </p>
     *
     * @param tag the condition tag
     * @return the value string in the condition statement
     *
     * @throws TemplateFormatException if the value part in the condition statement is missing
     *
     * @since 2.1
     */
    private String parseIfConditionValue(String tag) throws TemplateFormatException {
        if (!tag.endsWith("'")) {
            throw new TemplateFormatException("The end ' character for value is missing. There must be a value in "
                + "the if condition and the value needs to be enclosed in the ' character.");
        }

        int index = tag.substring(0, tag.length() - 1).lastIndexOf("'");
        if (index == -1) {
            throw new TemplateFormatException("The open ' character for value is missing. There must be a value in "
                + "the if condition and the value needs to be enclosed in the ' character.");
        }

        return tag.substring(index);
    }

    /**
     * Parses and validates description. Description in the following format
     * {description}.
     *
     * @param description the description for parsing
     *
     * @return the description, without ears
     *
     * @throws TemplateFormatException if syntax of description is incorrect
     *
     * @since 2.0
     */
    private String parseDescription(String description) throws TemplateFormatException {
        // In order to verify that this is correct description(syntax)
        // we will do following:
        // 1) Close ear should be last symbol in the tag.
        // 2) Check that inside of description not be no escape chars.
        // 3) Check that description contain at least one chars.
        // 1) Close ear should be last symbol in the tag.
        if (description.charAt(description.length() - 1) != '}') {
            throw new TemplateFormatException("The last characters of description should be '}'");
        }

        // Delete open and close ears.
        description = description.substring(1, description.length() - 1);

        // 2) Check that inside of description not be no escape chars.
        if (haveSameChars(description, ESCAPE_CHARS)) {
            throw new TemplateFormatException("Inside of description we have one of the chars : " + ESCAPE_CHARS);
        }

        // 3) Check that description contain at least one chars.
        if (description.length() == 0) {
            throw new TemplateFormatException("The description should contain at lest one character.");
        }

        // Return description without ears.
        return description;
    }

    /**
     * Parses loop from given string, the syntax of the loop following,
     * 'loop:variable[{description}]', where words in [] can be absent.
     *
     * @param tag the loop tag, in following format
     *        'loop:variable[{description}]', where words in [] can be absent.
     *
     * @return the parsed <code>Loop</code> with <code>EMPTY_NODE_LIST</code>
     *         list as subnodes.
     *
     * @throws TemplateFormatException if loop tag is incorrect
     *
     * @since 2.0
     */
    private Loop parseLoop(String tag) throws TemplateFormatException {
        // Remove first five chars from tag, this is always will be 'loop:'
        tag = tag.substring(5);

        // The variable of the loop, so named 'field_name' in loop syntax.
        String variable = tag;

        // The description for loop.
        String description = null;

        // Look for begin of description.
        int openEar = tag.indexOf('{');

        // If we find begin of the description, then:
        // 1) We have correct description, than we will change variable
        // and description;
        // 2) We have incorrect description, than we throw an exception.
        if (openEar != -1) {
            // All to the begin of description is variable.
            variable = tag.substring(0, openEar);

            // Get description, note we also verify the description,
            // in this method.
            description = parseDescription(tag.substring(openEar));
        }

        // Check the syntax of variable.
        checkVariable(variable);

        // Return loop with empty node list.
        return new Loop(variable, EMPTY_NODE_LIST, description, true);
    }

    /**
     * Parses field from given string, the syntax of the field following,
     * 'variable[=defaultValue][{description}]', where words in [] can be
     * absent.
     *
     * @param tag field tag
     *
     * @return parsed field
     *
     * @throws TemplateFormatException if given string is not correct field tag
     *
     * @since 2.0
     */
    private Field parseField(String tag) throws TemplateFormatException {
        // Set the default value for parts of field, this will be so
        // if field have not defaultValue and description.
        String variable = tag;
        String defaultValue = null;
        String description = null;

        // Look for defaultValue.
        int beginDefault = tag.indexOf('=');

        // Look for description.
        // Note : String.indexOf('{', -1) work like in String.indexOf('{', 0).
        int beginDesctiption = tag.indexOf('{', beginDefault);

        // If we have defaultValue, then we will reset variable and
        // defaultValue.
        if (beginDefault != -1) {
            // Now variable till begin of defaultValue.
            variable = tag.substring(0, beginDefault);

            if (beginDesctiption != -1) {
                // If we have description, then defaultValue substring from
                // begin of defaultValue to the begin of description.
                defaultValue = tag.substring(beginDefault + 1, beginDesctiption);
            } else {
                // If we have no description, then defaultValue all string
                // after begin of defaultValue.
                defaultValue = tag.substring(beginDefault + 1);
            }
        }

        if (beginDesctiption != -1) {
            // If we have description.
            if (beginDesctiption < variable.length()) {
                // If variable contain description, then cut description from
                // variable.
                variable = variable.substring(0, beginDesctiption);
            }

            // Get description with ears, and parse and validate it.
            description = parseDescription(tag.substring(beginDesctiption));
        }

        // Check the syntax of variable.
        checkVariable(variable);

        // Check defaultValue, it should not contain any escape characters.
        checkVariable(defaultValue);

        // Return parsed field.
        return new Field(variable, defaultValue, description, true);
    }

    /**
     * Checks whether given string can be correct variable :
     *
     * <ol>
     * <li>
     * string should have at least one character
     * </li>
     * <li>
     * string should not contain escape characters
     * </li>
     * </ol>
     *
     * Note : <code>null</code> is special case and it correct.
     *
     * @param variable the string for validating
     *
     * @throws TemplateFormatException throw if given string incorrect variable
     *
     * @since 2.0
     */
    private void checkVariable(String variable) throws TemplateFormatException {
        // Check for null.
        if (variable == null) {
            // This is for default value checking, because null can be.
            return;
        }

        // Check that variable have at list one character.
        if (variable.length() == 0) {
            throw new TemplateFormatException("The variable can not be empty.");
        }

        // Check that variable have not escape chars.
        if (haveSameChars(variable, ESCAPE_CHARS)) {
            throw new TemplateFormatException("The variable has one of the following chars : " + ESCAPE_CHARS);
        }
    }

    /**
     * Checks whether two string have same character. Return <code>true</code>
     * if <code>string</code> and <code>set</code> have same character.
     *
     * @param string first string
     * @param set second string
     *
     * @return return <code>true</code> if <code>string</code> and
     *         <code>set</code> have same character.
     *
     * @since 2.0
     */
    private boolean haveSameChars(String string, String set) {
        // Count of characters in set.
        int n = set.length();

        // Check whether one characters from set exists in string.
        for (int i = 0; i < n; i++) {
            if (string.indexOf(set.charAt(i)) != -1) {
                // If we found such character, then return true.
                return true;
            }
        }

        // If we did not found same character, then return false.
        return false;
    }
}
