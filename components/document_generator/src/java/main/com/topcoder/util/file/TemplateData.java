/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.TemplateFields;

/**
 * <p>
 * This interface abstract the input data used by templates to the rest of the
 * design.
 * </p>
 *
 * <p>
 * Only the template implementation has intimate knowledge of the implementation of TemplateData.
 * </p>
 *
 * <p>
 * Typically there should be pairs of Template and TemplateData implementation (such as XmlTemplateData
 * and XsltTemplate).
 * </p>
 *
 * <p>
 * The interface exposes method for setting the template data, from raw text
 * (currently XML but that depends only on the current implementation of the
 * TemplateData, so it can be changed easily) and from programmatically
 * configured template data, and for getting the template data. The
 * implementations must have a public default constructor (with no
 * parameters).
 * </p>
 *
 * <p>
 * Thread-safety : Implementations are not required to be thread-safe.
 * </p>
 *
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @version 2.1
 * @since 2.0
 */
public interface TemplateData {
    /**
     * Sets the template data from a string.
     *
     * @param templateData the string representing the template data
     *
     * @throws IllegalArgumentException if the argument is null or empty
     * @throws TemplateDataFormatException if the template data is invalid -
     * this wraps some implementation specific error indicating bad
     * template data (such as an XML exception or an XSLT
     * exception)
     *
     * @since 2.0
     */
    public void setTemplateData(String templateData) throws TemplateDataFormatException;

    /**
     * Sets the template data from the data structure of the API for programatic
     * field configuration.
     *
     * @param fields the root of the data structure with programtically
     * configured template data
     * @throws IllegalArgumentException if the argument is null
     *
     * @since 2.0
     */
    public void setTemplateData(TemplateFields fields);

    /**
     * Returns the template data. The returned text is only meaningful to the
     * pair Template implementation and can be in some cases unimplemented if
     * it is not useful. If text format is not suitable for that, then
     * additional methods can be added to the TemplateData implementation and
     * the pair Template implementation can take advantage of those.
     *
     * @return the template data
     *
     * @since 2.0
     */
    public String getTemplateData();
}
