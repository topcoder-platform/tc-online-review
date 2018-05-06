/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.xslttemplate.XsltTemplate;
import junit.framework.TestCase;

/**
 * <p>
 * Title: TestTemplateFields
 * </p>
 *
 * <p>
 * Description: Test whole <code>TemplateFields</code> class.
 * </p>
 *
 * <p>
 * Company: TopCoder Software
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TestTemplateFields extends TestCase {
    /** Array of nodes. */
    private Node[] nodes = new Node[] {new Field("first", "fierst", "first", true),
        new Field("second", "second", "second", true)};

    /** Template. */
    private XsltTemplate template = new XsltTemplate();

    /** TemplateFields. */
    private TemplateFields templateFields = null;

    /**
     * Creates <code>TemplateFields</code> object.
     */
    public void setUp() {
        // Create template fields.
        templateFields = new TemplateFields(nodes, template);
    }

    /**
     * Test {@link TemplateFields#TemplateFields(Node[], Template)} on good
     * data.
     */
    public void testConstructor() {
        // The templateFields should be created in setUp.
        assertNotNull("The TemplateFields object was not created.", templateFields);
    }

    /**
     * Test {@link TemplateFields#TemplateFields(Node[], Template)} with
     * <code>null</code> as first argument.
     */
    public void testConstructorWithFirstNull() {
        try {
            // Try to create TemplateFields object with null as first argument.
            new TemplateFields(null, template);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link TemplateFields#TemplateFields(Node[], Template)} with array,
     * which contain <code>null</code> element as first argument.
     */
    public void testConstructorWithNullElement() {
        try {
            // Try to create TemplateFields object with array,
            // which contain null element as first argument.
            new TemplateFields(new Node[] {null}, template);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link TemplateFields#TemplateFields(Node[], Template)} with
     * <code>null</code> as second argument.
     */
    public void testConstructorWithSecondNull() {
        try {
            // Try to create TemplateFields object with null as second argument.
            new TemplateFields(nodes, null);

            // Fail.
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * Test {@link TemplateFields#getTemplate} method.
     */
    public void test() {
        // Check template property.
        assertEquals("The template property is incorrect.", templateFields.getTemplate(), template);
    }
}
