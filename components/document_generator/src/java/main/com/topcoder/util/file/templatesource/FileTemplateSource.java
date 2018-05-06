/*
 * Copyright (C) 2007 - 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.templatesource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.Util;

/**
 * <p>
 * Implementation of a template source that retrieves template from the file system. In this implementation
 * the name of the template is the filename of the template.
 * </p>
 * <p>
 * Thread-Safety: This class should be implemented in a thread-safe manner.
 * </p>
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @author gniuxiao, TCSDEVELOPER
 * @version 3.0
 * @since 2.1
 */
public class FileTemplateSource implements TemplateSource {
    /**
     * Creates a FileTemplateSource instance.
     * @since 2.0
     */
    public FileTemplateSource() {
        // empty
    }

    /**
     * Creates a FileTemplateSource instance.
     * <p>
     * Note: this this ctor is needed by the dynamic instantiation performed in <code>DocumentGenerator</code>
     * </p>
     * @param configuration
     *            the ConfigurationObject to configure this instance, never used
     * @param id
     *            the ID of the template source, never used
     * @throws IllegalArgumentException
     *             if config is null or id is null or id is empty
     * @since 3.0
     */
    public FileTemplateSource(String id, ConfigurationObject configuration) {
        Util.checkNull(configuration, "configuration");
        Util.checkString(id, "id");
    }

    /**
     * Retrieves the text of the template stored in the given file.
     * Change log:
     *      version 3.1: the file can be in the jar file, or can be read from
     *                  ear/war file when deploying in JBoss.
     * @return the text of the template
     * @param fileName
     *            the name of the template file
     * @throws IllegalArgumentException
     *             if the argument is <code>null</code> or empty
     * @throws TemplateSourceException
     *             indicates an error while retrieving the template:
     *             <ul>
     *             <li>there isn't a file with the given name</li>
     *             <li>wraps an I/O exception while reading the file</li>
     *             </ul>
     * @since 2.0
     */
    public String getTemplate(String fileName) throws TemplateSourceException {
        // Check for null and empty string.
        Util.checkString(fileName, "fileName");

        BufferedReader reader;
        try {
            reader = Util.getBufferedReaderFromFileOrResource(fileName);
        } catch (IOException e) {
            throw new TemplateSourceException("Cannot read template from file", e);
        }

        // Accumulate data from file.
        StringBuffer template = new StringBuffer();
        try {
            try {
                // Buffer for reading from file.
                char[] buffer = new char[1024];
                // Count of characters that was read.
                int count = 0;
                // Read data from file until EOF.
                while ((count = reader.read(buffer)) != -1) {
                    template.append(buffer, 0, count);
                }
            } finally {
                // Close file.
                reader.close();
            }
        } catch (IOException e) {
            // If some exception happens while read file then wrap IOException
            // in TemplateSourceException and throw it.
            throw new TemplateSourceException("Cannot read template from file", e);
        }

        // Return the test of template.
        return template.toString();
    }

    /**
     * Adds the template to the template source with the given name. If there is a template with that name, it
     * is replaced.
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
     * @since 2.0
     */
    public void addTemplate(String templateName, String templateText) throws TemplateSourceException {
        // Check for null and empty string.
        Util.checkString(templateName, "templateName");
        Util.checkString(templateText, "templateText");
        try {
            // Writer.
            FileWriter writer = new FileWriter(templateName);

            try {
                // Write template to the file.
                writer.write(templateText.toCharArray());
            } finally {
                // Close file.
                writer.close();
            }
        } catch (IOException e) {
            // If some exception happens while write file then wrap IOException
            // in TemplateSourceException and throw it.
            throw new TemplateSourceException("Cannot write template to file", e);
        }
    }

    /**
     * Removes the template with the given name from the template source.
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
     * @since 2.0
     */
    public void removeTemplate(String templateName) throws TemplateSourceException {
        Util.checkString(templateName, "templateName");

        // If template was not removed from system then throw an exception.
        if (!new File(templateName).delete()) {
            throw new TemplateSourceException("The template was not deleted.");
        }
    }
}
