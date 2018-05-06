/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.TemplateFields;

/**
 * <p>
 * Abstracts a template processing class.
 * </p>
 *
 * <p>
 * This class is responsible for parsing the template defined by this component to some
 * precompiled internal format so it can be subsequently applied to multiple input data.
 * </p>
 *
 * <p>
 * Note that the format of the text template can be changed easily in the future. Only
 * XmlTemplateData and XsltTemplate depend on this format. Obviously, the
 * entire fieldConfig API depends on the logical structure of a template, but
 * not on the actual syntax. The implementations must have a public default
 * constructor (with no parameters). Currently an implementation based on XML
 * files and XSLT transformations is provided but other methods can be used as
 * well (such as Velocity templates).
 * </p>
 *
 * <p>
 * Thread-safety: Implementations are required to be thread-safe.
 * </p>
 *
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @version 2.1
 * @since 2.0
 */
public interface Template {
    /**
     * Sets the template text. It also should perform some implementation
     * specific processing on the template in order to produce a "precompiled"
     * version of the template that can be subsequently applied on multiple
     * input data sets.
     *
     * @param templateText the template text
     * @throws IllegalArgumentException if the argument is null or empty
     * @throws TemplateFormatException if the format of the template is
     * invalid : <ul><li> can be signaled by the Template instance
     * </li> <li> other implementation specific exception
     * indicating a template format problem (wrapped in this
     * exception - such as an XSLT exception)</li> </ul>
     *
     * @since 2.0
     */
    public void setTemplate(String templateText) throws TemplateFormatException;

    /**
     * Gets the template text.
     *
     * @return the template text
     *
     * @since 2.0
     */
    public String getTemplate();

    /**
     * Process the template and produce a data structure from fieldConfig API
     * instances to be used for programmatically configuring input data.
     *
     * @return a TemplateFields instance
     * @throws TemplateFormatException if the format of the template is
     * invalid
     *
     * @since 2.0
     */
    public TemplateFields getFields() throws TemplateFormatException;

    /**
     * Applies the template to the given template data and produces the output
     * text.
     *
     * @return the output text
     * @param templateData the template data
     * @throws IllegalArgumentException if the argument is null or not
     * supported by the implementation (ie XsltTemplate works only
     * with XmlTemplateData)
     * @throws TemplateFormatException if a template format error is found
     * applying the template : <ul><li>this wraps some
     * implementation specific error indicating invalid template
     * format (such as an XSLT exception) </li> </ul>
     * @throws TemplateDataFormatException if the template data is invalid :
     * <ul><li>this wraps some implementation specific error
     * indicating bad template data (such as an XML exception or an
     * XSLT exception)</li> </ul>
     *
     * @since 2.0
     */
    public String applyTemplate(TemplateData templateData) throws TemplateFormatException, TemplateDataFormatException;
}
