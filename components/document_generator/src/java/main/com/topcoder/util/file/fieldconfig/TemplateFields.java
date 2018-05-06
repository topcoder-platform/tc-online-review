/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.fieldconfig;

import com.topcoder.util.file.Template;
import com.topcoder.util.file.Util;

/**
 * <p>
 * This class represents the root of the structure used to configure
 * programmatically the template data. It is a subclass of NodeList.
 * </p>
 *
 * <p>
 * Thread-safety: This class is immutable and therefore thread-safe.
 * </p>
 *
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @version 2.1
 * @since 2.0
 */
public class TemplateFields extends NodeList {
    /**
     * <p>
     * This is the corresponding template to which the TemplateFields belong.
     * </p>
     * <p>
     * Initialized In: Constructor.
     * </p>
     * <p>
     * Accessed In: getTemplate
     * </p>
     * <p>
     * Modified In: Not Modified
     * </p>
     * <p>
     * Utilized in: Not Utilized in this class
     * </p>
     * <p>
     * Valid Values: Non-null Template
     * </p>
     *
     * @since 2.0
     */
    private Template template;

    /**
     * Constructor.
     *
     * @param nodes the nodes (fields and loops) in the "root" of the template
     * @param template the template
     *
     * @throws IllegalArgumentException if any argument is <code>null</code>
     *
     * @since 2.0
     */
    public TemplateFields(Node[] nodes, Template template) {
        super(nodes);

        // Check template for null.
        Util.checkNull(template, "template");

        // Set property.
        this.template = template;
    }

    /**
     * Returns the template associated with this instance.
     *
     * @return the template of this instance
     *
     * @since 2.0
     */
    public Template getTemplate() {
        return template;
    }
}
