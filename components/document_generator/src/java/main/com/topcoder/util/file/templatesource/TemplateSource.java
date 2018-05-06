/*
 * Copyright (C) 2007, 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.templatesource;

/**
 * <p>
 * This interface abstracts a template source, some kind of repository where templates can be retrieved from,
 * based on a given name.
 * </p>
 * <p>
 * The name has different interpretations specific to each implementation (the value of a field in the
 * database source, a file name in the file source, a file name in a future CVS source implementation).
 * </p>
 * <p>
 * All implementations must implement a public constructor accepting an ID and a ConfigurationObject instance.
 * - as it is needed by the dynamic instantiation performed in <code>DocumentGenerator</code>.
 * </p>
 * <p>
 * Thread-safety: Implementations are required to be thread-safe.
 * </p>
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @author gniuxiao, TCSDEVELOPER
 * @version 3.0
 * @since 2.1
 */
public interface TemplateSource {
    /**
     * Retrieves the text of the template with the given name.
     * @return the text of the template
     * @param templateName
     *            the name of the template
     * @throws IllegalArgumentException
     *             if the argument is null or empty
     * @throws TemplateSourceException
     *             indicates an error while retrieving the template:
     *             <ul>
     *             <li>there isn't a template with the given name
     *             <li>wraps some implementation specific exception (SQL exception, I/O exception)
     *             </ul>
     * @since 2.0
     */
    public String getTemplate(String templateName) throws TemplateSourceException;

    /**
     * Adds the template to the template source with the given name. If there is a template with that name, it
     * is replaced.
     * @param templateName
     *            the name of the template
     * @param templateText
     *            the text of the template
     * @throws IllegalArgumentException
     *             if the argument is null or empty
     * @throws TemplateSourceException
     *             indicates an error while adding the template:
     *             <ul>
     *             <li> wraps some implementation specific exception (SQL exception, I/O exception)
     *             </ul>
     * @since 2.0
     */
    public void addTemplate(String templateName, String templateText) throws TemplateSourceException;

    /**
     * Removes the template with the given name from the template source.
     * @param templateName
     *            the name of the template
     * @throws IllegalArgumentException
     *             if the argument is null or empty
     * @throws TemplateSourceException
     *             indicates an error while removing the template:
     *             <ul>
     *             <li>there isn't a template with the given name</li>
     *             <li>wraps some implementation specific exception (SQL exception, I/O exception)</li>
     *             </ul>
     * @since 2.0
     */
    public void removeTemplate(String templateName) throws TemplateSourceException;
}
