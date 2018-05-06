/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;

/**
 * This is class only for test.
 *
 * @author extra
 * @version 3.0
 */
public class MyFileTemplateSource extends FileTemplateSource {
    /** Base directory. */
    private String dir = null;

    /**
     * Constructor.
     *
     * @param config
     *            the ConfigurationObject to configure this instance
     * @param sourceId
     *            the id of this source (not needed - no config data for this implementation)
     * @throws IllegalArgumentException
     *             if any argument is <code>null</code> or empty.
     * @throws ConfigurationAccessException
     *             to junit
     */
    public MyFileTemplateSource(String sourceId, ConfigurationObject config) throws ConfigurationAccessException {
        // Check for null and empty string.
        if ((sourceId == null) || (sourceId.trim().length() == 0) || config == null) {
            throw new IllegalArgumentException("The argument is null or empty.");
        }

        // Set base directory.
        dir = (String) config.getPropertyValue(sourceId + "_dir");
    }

    /**
     * Retrieves the text of the template stored in the given file.
     * <ul>
     * <li> Valid args: non-null/empty string, existing file </li>
     * <li> Invalid args: null/empty string, missing file </li>
     * </ul>
     *
     * @param fileName
     *            the name of the template file
     * @return the text of the template
     * @throws IllegalArgumentException
     *             if the argument is <code>null</code> or empty
     * @throws TemplateSourceException
     *             indicates an error while retrieving the template:
     *             <ul>
     *             <li>there isn't a file with the given name</li>
     *             <li>wraps an I/O exception while reading the file</li>
     *             </ul>
     */
    public String getTemplate(String fileName) throws TemplateSourceException {
        // Check for null and empty string.
        if ((fileName == null) || (fileName.length() == 0)) {
            throw new IllegalArgumentException("The argument is null or empty.");
        }

        // Call super method.
        return super.getTemplate(dir + fileName);
    }

    /**
     * Adds the template to the template source with the given name. If there is a template with that name, it is
     * replaced.
     * <ul>
     * <li> Valid args: non-null/empty strings </li>
     * <li> Invalid args: null/empty strings </li>
     * </ul>
     *
     * @param templateName
     *            the name of the template
     * @param templateText
     *            the text of the template
     * @throws IllegalArgumentException
     *             if the argument is <code>null</code> or empty
     * @throws TemplateSourceException
     *             indicates an error while adding the template:
     *             <ul>
     *             <li>wraps some implementation specific exception (I/O exception)</li>
     *             </ul>
     */
    public void addTemplate(String templateName, String templateText) throws TemplateSourceException {
        // Check for null and empty string.
        if ((templateName == null) || (templateText == null) || (templateName.length() == 0)
                || (templateText.length() == 0)) {
            throw new IllegalArgumentException("The argument is null or empty.");
        }

        // Call super method.
        super.addTemplate(dir + templateName, templateText);
    }

    /**
     * Removes the template with the given name from the template source.
     * <ul>
     * <li> Valid args: non-null/empty string </li>
     * <li> Invalid args: null/empty string </li>
     * </ul>
     *
     * @param templateName
     *            the name of the template
     * @throws IllegalArgumentException
     *             if the argument is <code>null</code> or empty
     * @throws TemplateSourceException
     *             indicates an error while removing the template:
     *             <ul>
     *             <li>there isn't a template with the given name</li>
     *             <li>wraps some implementation specific exception (I/O exception)</li>
     *             </ul>
     */
    public void removeTemplate(String templateName) throws TemplateSourceException {
        // Check for null and empty string.
        if ((templateName == null) || (templateName.length() == 0)) {
            throw new IllegalArgumentException("The argument is null or empty.");
        }

        // Call super method.
        super.removeTemplate(dir + templateName);
    }
}
