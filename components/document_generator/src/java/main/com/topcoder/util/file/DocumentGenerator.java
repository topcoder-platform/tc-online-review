/*
 * Copyright (C) 2007, 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.TemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;
import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import com.topcoder.util.file.xslttemplate.XsltTemplate;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This is the main class of the design and the only API class that the users will interact with in most
 * cases. The only configurable things are the template sources. However there is a method allowing the use of
 * runtime configured template sources for flexibility. It is also a facade for the rest of the classes,
 * hiding the details of the document generation process. It exposes methods for getting a template from the
 * template source (database, file system, in the future CVS and LDAP maybe), for parsing a given template (as
 * text) and for applying a template (to data coming from XML or data configured using the API classes for
 * field data entering).
 * </p>
 * <p>
 * Thread-safety: - This class is not thread-safe since it's mutable.
 * </p>
 * <p>
 * Sample usage 1.
 * </p>
 *
 * <pre>
 * // Create DocumentGenerator from constructor
 * DocumentGenerator docgen = new DocumentGenerator();
 *
 * // A workable ConfigurationObject
 * ConfigurationObject config = TestHelper
 *     .createConfigurationObject(&quot;demo.properties&quot;, &quot;com.topcoder.util.file&quot;);
 * // Create DocumentGenerator from factory
 * docgen = DocumentGeneratorFactory.getDocumentGenerator(config);
 *
 * // Modify TemplateSources
 * TemplateSource ts = new FileTemplateSource(&quot;file_source&quot;, config);
 * // Any number of TemplateSource can be added
 * docgen.setTemplateSource(&quot;file_source&quot;, ts);
 * docgen.getTemplateSource(&quot;file_source&quot;);
 * // The getter should return ts
 * docgen.removeTemplateSource(&quot;file_source&quot;);
 * // ts should be removed
 * docgen.clearTemplateSources();
 * // All template sources should be removed
 *
 * // Modify default TemplateSource
 * docgen.setDefaultTemplateSource(ts);
 * // Now ts is used as the default one
 * docgen.getDefaultTemplateSource();
 * // ts should be returned from getter
 * </pre>
 *
 * <p>
 * Sample usage 2.
 * </p>
 *
 * <pre>
 * Project project = new Project();
 * project.setProjectType(&quot;design&quot;);
 *
 * Component configManager = new Component();
 * configManager.setComponentName(&quot;configmanager&quot;);
 * configManager.setComponentLongName(&quot;configuration_manager&quot;);
 * configManager.setComponentVersion(&quot;2.1.5&quot;);
 *
 * Component baseException = new Component();
 * baseException.setComponentName(&quot;baseexception&quot;);
 * baseException.setComponentLongName(&quot;base_exception&quot;);
 * baseException.setComponentVersion(&quot;2.0&quot;);
 *
 * project.setDependencies(new Component[] {configManager, baseException });
 *
 * // A document can be programmatically retrieved by using:
 * // A workable ConfigurationObject
 * ConfigurationObject config = TestHelper
 *     .createConfigurationObject(&quot;demo.properties&quot;, &quot;com.topcoder.util.file&quot;);
 * // Create DocumentGenerator from factory
 * DocumentGenerator docGen = DocumentGeneratorFactory.getDocumentGenerator(config);
 *
 * Template buildTemplate = docGen.getTemplate(&quot;fileSource&quot;, &quot;test_files/buildTemplate.txt&quot;);
 *
 * TemplateFields root = docGen.getFields(buildTemplate);
 * Node[] nodes = root.getNodes();
 * NodeList nodeList = new NodeList(nodes);
 *
 * // This is a lot simpler than looping through all the nodes and looking
 * // up the respective project property. Note that it is also possible to
 * // do this manually.
 * NodeListUtility.populateNodeList(nodeList, project);
 * // Applying the template can also be done without using the Document Generator,
 * // using Template#applyTemplate method.
 * String designBuildTemplate = docGen.applyTemplate(root);
 * System.out.println(designBuildTemplate);
 *
 * XmlTemplateData xslTemplateData = new XmlTemplateData();
 * xslTemplateData.setTemplateData(root);
 *
 * project.setProjectType(&quot;development&quot;);
 * NodeListUtility.populateNodeList(new NodeList(nodes), project);
 * String devBuildTemplate = docGen.applyTemplate(root);
 * System.out.println(devBuildTemplate);
 *
 * displayNodes(nodes);
 * </pre>
 *
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @author gniuxiao, TCSDEVELOPER
 * @version 3.0
 * @since 2.1
 */
public class DocumentGenerator {
    /**
     * <p>
     * The current template processing implementation that is based on XSL transformations (using XML files as
     * data).
     * </p>
     * <p>
     * This attribute allows swapping the implementation in the future with something else (maybe velocity
     * templates).
     * </p>
     * @since 2.0
     */
    private static final Class TEMPLATECLASS = XsltTemplate.class;

    /**
     * <p>
     * Represents the current template data implementation that consists of XML data files (using XSL
     * transformations for template processing).
     * </p>
     * <p>
     * This attribute allows swapping the implementation in the future with something else (some kind of
     * property files maybe).
     * </p>
     * @since 2.0
     */
    private static final Class TEMPLATEDATACLASS = XmlTemplateData.class;

    /**
     * <p>
     * The map with the template sources (maps source identifiers to TemplateSource instances).
     * </p>
     * <p>
     * Accessed In: Contents are accessed in getTemplate, values are accessed in getTemplateSource
     * </p>
     * <p>
     * Modified In: Contents can be modified in setTemplateSource, removeTemplateSource
     * </p>
     * <p>
     * Utilized in: Contents are utilized in applyTemplate
     * </p>
     * <p>
     * Valid Values: non-null Map implementation. Valid keys are non-null and non-empty Strings; Valid values
     * are non-null TemplateSource instances.
     * </p>
     * <p>
     * Initialize to an empty HashMap.
     * </p>
     * @since 3.0
     */
    private final Map templateSources = new HashMap();

    /**
     * <p>
     * The default template source (chosen when an source id is not given). Can be null.
     * </p>
     * <p>
     * Accessed In: getTemplate, namesake getter
     * </p>
     * <p>
     * Modified In: Namesake setter
     * </p>
     * <p>
     * Utilized in: applyTemplate
     * </p>
     * <p>
     * Valid Values: Possibly null Template
     * </p>
     * <p>
     * Initialize to null.
     * </p>
     * @since 3.0
     */
    private TemplateSource defaultTemplateSource;

    /**
     * Creates a DocumentGenerator instance.
     * @since 3.0
     */
    public DocumentGenerator() {
    }

    /**
     * <p>
     * Gets a template from the default template source.
     * </p>
     * <p>
     * The <code>Template</code> instance will contain a precompiled template that can be subsequently used
     * for multiple transformations.
     * </p>
     * @return the <code>Template</code> instance
     * @param templateName
     *            the name of the template
     * @throws IllegalArgumentException
     *             if the argument is null or empty
     * @throws TemplateSourceException
     *             if the template cannot be loaded from the source
     *             <ol>
     *             <li> there is no template source (no default template source) </li>
     *             <li> there is no template with the given name in the template source </li>
     *             <li> other template source implementation specific exception (wrapped in this exception -
     *             such as SQLException, IOException) </li>
     *             </ol>
     * @throws TemplateFormatException
     *             if the template is invalid
     *             <ol>
     *             <li>can be signaled by the template processing class (Template instance) </li>
     *             <li> other implementation specific exception indicating a template format problem (wrapped
     *             in this exception - such as an XSLT exception) </li>
     *             </ol>
     * @since 2.0
     */
    public Template getTemplate(String templateName) throws TemplateSourceException, TemplateFormatException {
        // Check that we have default template source.
        if (defaultTemplateSource == null) {
            throw new TemplateSourceException("The are no default template source.");
        }

        // Call same method with other parameters.
        return getTemplate(defaultTemplateSource, templateName);
    }

    /**
     * <p>
     * Gets a template from the template source with the given id.
     * </p>
     * <p>
     * The <code>Template</code> instance will contain a precompiled template that can be subsequently used
     * for multiple transformations.
     * </p>
     * @return the Template instance
     * @param sourceId
     *            the source id
     * @param templateName
     *            the name of the template
     * @throws IllegalArgumentException
     *             if any of the arguments are null or empty
     * @throws TemplateSourceException
     *             if the template cannot be loaded from the source
     *             <ol>
     *             <li> there is no template source with that id </li>
     *             <li> there is no template with the given name in the template source </li>
     *             <li> other template source implementation specific exception (wrapped in this exception -
     *             such as SQLException, IOException) </li>
     *             </ol>
     * @throws TemplateFormatException
     *             if the template is invalid
     *             <ol>
     *             <li>can be signaled by the template processing class (Template instance) </li>
     *             <li> other implementation specific exception indicating a template format problem (wrapped
     *             in this exception - such as an XSLT exception) </li>
     *             </ol>
     * @since 2.0
     */
    public Template getTemplate(String sourceId, String templateName) throws TemplateSourceException,
        TemplateFormatException {
        // Check for null and empty string.
        Util.checkString(sourceId, "sourceId");

        // Get the source.
        TemplateSource source = (TemplateSource) templateSources.get(sourceId);

        // Check that we really have source with given id.
        if (source == null) {
            throw new TemplateSourceException("The are no template source with given id.");
        }

        // Call same method with other parameters.
        return getTemplate(source, templateName);
    }

    /**
     * <p>
     * Gets a template from the given template source.
     * </p>
     * <p>
     * The <code>Template</code> instance will contain a pre compiled template that can be subsequently used
     * for multiple transformations.
     * </p>
     * @return the Template instance
     * @param source
     *            the template source
     * @param templateName
     *            the name of the template
     * @throws IllegalArgumentException
     *             if any of the arguments are null or templateName is empty
     * @throws TemplateSourceException
     *             if the template cannot be loaded from the source
     *             <ol>
     *             <li> there is no template with the given name in the template source </li>
     *             <li> other template source implementation specific exception (wrapped in this exception -
     *             such as SQLException, IOException) </li>
     *             </ol>
     * @throws TemplateFormatException
     *             if the template is invalid
     *             <ol>
     *             <li>can be signaled by the template processing class (Template instance) </li>
     *             <li> other implementation specific exception indicating a template format problem (wrapped
     *             in this exception - such as an XSLT exception) </li>
     *             </ol>
     * @since 2.0
     */
    public Template getTemplate(TemplateSource source, String templateName) throws TemplateSourceException,
        TemplateFormatException {
        // Check for null and empty string.
        Util.checkNull(source, "source");
        Util.checkString(templateName, "templateName");

        // Get template from the source and compile it.
        return parseTemplate(source.getTemplate(templateName));
    }

    /**
     * <p>
     * Parses and compiles a template given as text.
     * </p>
     * <p>
     * The <code>Template</code> instance will contain a precompiled template that can be subsequently used
     * for multiple transformations.
     * </p>
     * @return the Template instance
     * @param templateText
     *            the template
     * @throws IllegalArgumentException
     *             if the argument is null or empty
     * @throws TemplateFormatException
     *             if the template is invalid
     *             <ol>
     *             <li>can be signaled by the template processing class (Template instance) </li>
     *             <li> other implementation specific exception indicating a template format problem (wrapped
     *             in this exception - such as an XSLT exception) </li>
     *             </ol>
     * @since 2.0
     */
    public Template parseTemplate(String templateText) throws TemplateFormatException {
        // Template.
        Template template = null;

        // Instantiate template.
        try {
            template = (Template) TEMPLATECLASS.newInstance();
        } catch (InstantiationException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        }

        // Set template, it will check templateText.
        template.setTemplate(templateText);

        // Return precompiled template.
        return template;
    }

    /**
     * <p>
     * Parses and compiles a template given as text from a reader.
     * </p>
     * <p>
     * The <code>Template</code> instance will contain a precompiled template that can be subsequently used
     * for multiple transformations.
     * </p>
     * @return the Template instance
     * @param templateReader
     *            the template reader
     * @throws IllegalArgumentException
     *             if the argument is null
     * @throws IOException
     *             if an I/O error occurs while reading from the reader
     * @throws TemplateFormatException
     *             if the template is invalid
     *             <ol>
     *             <li>can be signaled by the template processing class (Template instance) </li>
     *             <li> other implementation specific exception indicating a template format problem (wrapped
     *             in this exception - such as an XSLT exception) </li>
     *             </ol>
     * @since 2.0
     */
    public Template parseTemplate(Reader templateReader) throws IOException, TemplateFormatException {
        // Call same method with other parameter.
        return parseTemplate(Util.readString(templateReader));
    }

    /**
     * <p>
     * Returns an {@link TemplateFields} instance that can be used to configure the template field values
     * programmatically instead of using an XML data file.
     * </p>
     * @return a TemplateFields instance that can be used for programmatically setting of field values
     * @param template
     *            the template
     * @throws IllegalArgumentException
     *             if the argument is <code>null</code>
     * @throws TemplateFormatException
     *             if a format error is found while processing the template text (since the template is
     *             already processed once at precompilation time, under normal usage this should not happen)
     * @since 2.0
     */
    public TemplateFields getFields(Template template) throws TemplateFormatException {
        // Check for null.
        Util.checkNull(template, "template");

        // Return a TemplateFields instance.
        return template.getFields();
    }

    /**
     * <p>
     * Generates a text document by applying a given template to the given template data.
     * </p>
     * <p>
     * The given template data is currently in XML format but if the templateDataClass and templateClass are
     * changed, with something else than <code>XmlTemplateData</code> and <code>XsltTemplate</code>, then
     * other formats can be used.
     * </p>
     * @return the generated text document
     * @param template
     *            the template
     * @param templateData
     *            the template data (currently in XML format)
     * @throws IllegalArgumentException
     *             if any argument is null or the string argument is empty
     * @throws TemplateFormatException
     *             if a template format error is found applying the template
     *             <ol>
     *             <li> this wraps some implementation specific error indicating invalid template format (such
     *             as an XSLT exception) </li>
     *             </ol>
     * @throws TemplateDataFormatException
     *             if the template data is invalid
     *             <ol>
     *             <li> this wraps some implementation specific error indicating bad template data (such as an
     *             XML exception or an XSLT exception) </li>
     *             </ol>
     * @since 2.0
     */
    public String applyTemplate(Template template, String templateData) throws TemplateFormatException,
        TemplateDataFormatException {
        // Check template for null.
        Util.checkNull(template, "template");

        // Apply template.
        return template.applyTemplate(parseTemplateData(templateData));
    }

    /**
     * <p>
     * Generates a text document by applying a given template to the given template data (from a reader).
     * </p>
     * <p>
     * The given template data is currently in XML format but if the templateDataClass and templateClass are
     * changed, with something else than <code>XmlTemplateData</code> and <code>XsltTemplate</code>, then
     * other formats can be used.
     * </p>
     * @return the generated text document
     * @param template
     *            the template
     * @param templateData
     *            the reader with template data (currently in XML format)
     * @throws IllegalArgumentException
     *             if any argument is null
     * @throws IOException
     *             if an I/O error occurs while reading the template data from the reader
     * @throws TemplateFormatException
     *             if a template format error is found applying the template
     *             <ol>
     *             <li> this wraps some implementation specific error indicating invalid template format (such
     *             as an XSLT exception) </li>
     *             </ol>
     * @throws TemplateDataFormatException
     *             if the template data is invalid
     *             <ol>
     *             <li> this wraps some implementation specific error indicating bad template data (such as an
     *             XML exception or an XSLT exception) </li>
     *             </ol>
     * @since 2.0
     */
    public String applyTemplate(Template template, Reader templateData) throws IOException,
        TemplateFormatException, TemplateDataFormatException {
        // Call same method with other parameters.
        return applyTemplate(template, Util.readString(templateData));
    }

    /**
     * <p>
     * Generates a text document by applying a given template to the template data given programmatically
     * through the field configuration API classes.
     * </p>
     * @return the generated text document
     * @param templateData
     *            the template data
     * @throws IllegalArgumentException
     *             if the argument is null
     * @throws TemplateFormatException
     *             if a template format error is found applying the template
     *             <ol>
     *             <li> this wraps some implementation specific error indicating invalid template format (such
     *             as an XSLT exception)</li>
     *             </ol>
     * @throws TemplateDataFormatException
     *             if the template data is invalid
     *             <ol>
     *             <li> this wraps some implementation specific error indicating bad template data (such as an
     *             XML exception or an XSLT exception) - since the field config API ensures correct template
     *             data, normally this should not occur </li>
     *             </ol>
     * @since 2.0
     */
    public String applyTemplate(TemplateFields templateData) throws TemplateFormatException,
        TemplateDataFormatException {
        // Check for null first of all.
        Util.checkNull(templateData, "templateData");

        // Get template.
        Template template = templateData.getTemplate();

        // Apply template.
        return template.applyTemplate(parseTemplateData(templateData));
    }

    /**
     * <p>
     * Generates a text document by applying a given template to the given template data.
     * </p>
     * <p>
     * The given template data is currently in XML format but if the templateDataClass and templateClass are
     * changed, with something else than <code>XmlTemplateData</code> and <code>XsltTemplate</code>, then
     * other formats can be used.
     * </p>
     * @param template
     *            the template
     * @param templateData
     *            the template data (currently in XML format)
     * @param output
     *            the writer to write to
     * @throws IllegalArgumentException
     *             if any argument is <code>null</code> or the string argument is empty
     * @throws TemplateFormatException
     *             if a template format error is found applying the template
     *             <ol>
     *             <li> this wraps some implementation specific error indicating invalid template format (such
     *             as an XSLT exception) </li>
     *             </ol>
     * @throws TemplateDataFormatException
     *             if the template data is invalid
     *             <ol>
     *             <li> this wraps some implementation specific error indicating bad template data (such as an
     *             XML exception or an XSLT exception)</li>
     *             </ol>
     * @throws IOException
     *             if an I/O error occurs while writing to the writer
     * @since 2.0
     */
    public void applyTemplate(Template template, String templateData, Writer output)
        throws TemplateFormatException, TemplateDataFormatException, IOException {
        // Check for null.
        Util.checkNull(output, "output");

        // Write generated document to the writer.
        output.write(applyTemplate(template, templateData));
        output.flush();
    }

    /**
     * <p>
     * Generates a text document by applying a given template to the given template data (from a reader).
     * </p>
     * <p>
     * The given template data is currently in XML format but if the templateDataClass and templateClass are
     * changed, with something else than <code>XmlTemplateData</code> and <code>XsltTemplate</code>, then
     * other formats can be used.
     * </p>
     * @param template
     *            the template
     * @param templateData
     *            the reader with template data (currently in XML format)
     * @param output
     *            the writer to write to
     * @throws IllegalArgumentException
     *             if any argument is <code>null</code>
     * @throws IOException
     *             if an I/O error occurs while reading the template data from the reader or while writing to
     *             the writer
     * @throws TemplateFormatException
     *             if a template format error is found applying the template
     *             <ol>
     *             <li> this wraps some implementation specific error indicating invalid template format (such
     *             as an XSLT exception) </li>
     *             </ol>
     * @throws TemplateDataFormatException
     *             if the template data is invalid
     *             <ol>
     *             <li> this wraps some implementation specific error indicating bad template data (such as an
     *             XML exception or an XSLT exception) </li>
     *             </ol>
     * @since 2.0
     */
    public void applyTemplate(Template template, Reader templateData, Writer output) throws IOException,
        TemplateFormatException, TemplateDataFormatException {
        // Check for null.
        Util.checkNull(output, "output");
        // Write generated document to the writer.
        output.write(applyTemplate(template, templateData));
        output.flush();
    }

    /**
     * <p>
     * Generates a text document by applying a given template to the template data given programmatically
     * through the field configuration API classes.
     * </p>
     * @param templateData
     *            the template data
     * @param output
     *            the writer to write to
     * @throws IllegalArgumentException
     *             if any argument is <code>null</code>
     * @throws TemplateFormatException
     *             if a template format error is found applying the template
     *             <ol>
     *             <li> this wraps some implementation specific error indicating invalid template format (such
     *             as an XSLT exception) </li>
     *             </ol>
     * @throws TemplateDataFormatException
     *             if the template data is invalid
     *             <ol>
     *             <li> this wraps some implementation specific error indicating bad template data (such as an
     *             XML exception or an XSLT exception) - since the field config API ensures correct template
     *             data, normally this should not occur </li>
     *             </ol>
     * @throws IOException
     *             if an I/O error occurs while writing to the writer
     * @since 2.0
     */
    public void applyTemplate(TemplateFields templateData, Writer output) throws TemplateFormatException,
        TemplateDataFormatException, IOException {
        // Check for null.
        Util.checkNull(output, "output");
        // Write generated document to the writer.
        output.write(applyTemplate(templateData));
        output.flush();
    }

    /**
     * <p>
     * Parses template data given as text.
     * </p>
     * <p>
     * The <code>TemplateData</code> instance will contain this template data.
     * </p>
     * @return the TemplateData instance
     * @param templateDataText
     *            the template data
     * @throws IllegalArgumentException
     *             if the argument is <code>null</code> or empty
     * @throws TemplateDataFormatException
     *             if the template data is invalid
     * @since 2.0
     */
    private TemplateData parseTemplateData(String templateDataText) throws TemplateDataFormatException {
        // Template data.
        TemplateData templateData = null;
        // Instantiate template data.
        try {
            templateData = (TemplateData) TEMPLATEDATACLASS.newInstance();
        } catch (Exception e) {
        }
        // Set template data, it will check templateDataText.
        templateData.setTemplateData(templateDataText);
        // Return template data.
        return templateData;
    }

    /**
     * <p>
     * Parses template data given as API.
     * </p>
     * <p>
     * The <code>TemplateData</code> instance will contain this template data.
     * </p>
     * @return the TemplateData instance
     * @param templateFields
     *            the template data
     * @throws IllegalArgumentException
     *             if the argument is <code>null</code> or empty
     * @since 2.0
     */
    private TemplateData parseTemplateData(TemplateFields templateFields) {
        // Template data.
        TemplateData templateData = null;
        // Instantiate template data.
        try {
            templateData = (TemplateData) TEMPLATEDATACLASS.newInstance();
        } catch (Exception e) {
        }
        // Set template data, it will check templateDataText.
        templateData.setTemplateData(templateFields);
        // Return template data.
        return templateData;
    }

    /**
     * Set a specified template source with the source ID and TemplateSource instance.
     * @param sourceId
     *            the source id
     * @param source
     *            the template source
     * @return previous template source, null if no previous source
     * @throws IllegalArgumentException
     *             if sourceId is null or empty; if source is null
     * @since 3.0
     */
    public TemplateSource setTemplateSource(String sourceId, TemplateSource source) {
        Util.checkString(sourceId, "sourceId");
        Util.checkNull(source, "source");
        return (TemplateSource) templateSources.put(sourceId, source);
    }

    /**
     * Get a specified template source with the source ID.
     * @param sourceId
     *            the source id
     * @return the template source with the id, null if not found
     * @throws IllegalArgumentException
     *             if sourceId is null or empty
     * @since 3.0
     */
    public TemplateSource getTemplateSource(String sourceId) {
        Util.checkString(sourceId, "sourceId");
        return (TemplateSource) templateSources.get(sourceId);
    }

    /**
     * Remove a specified template source with the source ID.
     * @param sourceId
     *            the source id
     * @return the template source with the id, null if not found
     * @throws IllegalArgumentException
     *             if sourceId is null or empty
     * @since 3.0
     */
    public TemplateSource removeTemplateSource(String sourceId) {
        Util.checkString(sourceId, "sourceId");
        return (TemplateSource) templateSources.remove(sourceId);
    }

    /**
     * Set the default template source.
     * @param source
     *            the template source to set
     * @throws IllegalArgumentException
     *             if source is null
     * @since 3.0
     */
    public void setDefaultTemplateSource(TemplateSource source) {
        Util.checkNull(source, "source");
        this.defaultTemplateSource = source;
    }

    /**
     * Get the default template source.
     * @return the default template source
     * @since 3.0
     */
    public TemplateSource getDefaultTemplateSource() {
        return defaultTemplateSource;
    }

    /**
     * Clear all template sources (not including the default one).
     * @since 3.0
     */
    public void clearTemplateSources() {
        templateSources.clear();
    }
}
