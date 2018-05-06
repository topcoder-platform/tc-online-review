/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import java.lang.reflect.InvocationTargetException;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.templatesource.TemplateSource;

/**
 * The DocumentGeneratorFactory class defines a static method to create DocumentGenerator instance based on
 * the passed in ConfigurationObject.
 * <p>
 * This class is thread-safe since it's immutable.
 * </p>
 * <p>
 * sample configuration:
 * </p>
 * Configuration file content:
 *
 * <pre>
 *  &lt;?xml version=&quot;1.0&quot;?&gt;
 *  &lt;CMConfig&gt;
 *  &lt;Property name=&quot;com.topcoder.util.file&quot;&gt;
 *  &lt;Value&gt;com/topcoder/util/file/default.xml&lt;/Value&gt;
 *  &lt;/Property&gt;
 *  &lt;/CMConfig&gt;
 * </pre>
 *
 * The content of default.xml
 *
 * <pre>
 *  &lt;CMConfig&gt;
 *  &lt;Config name=&quot;com.topcoder.util.file&quot;&gt;
 *  &lt;Property name=&quot;sources&quot;&gt;
 *  &lt;Value&gt;file&lt;/Value&gt;
 *  &lt;Value&gt;my_file&lt;/Value&gt;
 *  &lt;/Property&gt;
 *
 *  &lt;!-- the identifier of the template source to be used as default --&gt;
 *  &lt;Property name=&quot;default_source&quot;&gt;
 *  &lt;Value&gt;my_file&lt;/Value&gt;
 *  &lt;/Property&gt;
 *
 *  &lt;!-- here follow custom properties for each template source --&gt;
 *  &lt;!-- the &lt;sourceidentifier&gt;_class property is mandatory --&gt;
 *  &lt;!-- the other &lt;sourceidentifier&gt;_&lt;property_name&gt; properties are specific for each source --&gt;
 *
 *  &lt;!-- file template source properties --&gt;
 *  &lt;Property name=&quot;file_class&quot;&gt;
 *  &lt;Value&gt;com.topcoder.util.file.templatesource.FileTemplateSource&lt;/Value&gt;
 *  &lt;/Property&gt;
 *
 *  &lt;!-- my_file template source properties --&gt;
 *  &lt;Property name=&quot;my_file_class&quot;&gt;
 *  &lt;Value&gt;com.topcoder.util.file.MyFileTemplateSource&lt;/Value&gt;
 *  &lt;/Property&gt;
 *
 *  &lt;Property name=&quot;my_file_dir&quot;&gt;
 *  &lt;Value&gt;test_files/my_files/&lt;/Value&gt;
 *  &lt;/Property&gt;
 *  &lt;/Config&gt;
 *  &lt;/CMConfig&gt;
 *
 * </pre>
 *
 * @author gniuxiao, TCSDEVELOPER
 * @version 3.0
 */
public class DocumentGeneratorFactory {
    /**
     * Do nothing private ctor.
     */
    private DocumentGeneratorFactory() {
    }

    /**
     * Create a DocumentGenerator instance via the passed in ConfigurationObject.
     * @param configuration
     *            The configuration object used to construct the DocumentGenerator, can't be null
     * @return The created DocumentGenerator, never be null
     * @throws IllegalArgumentException
     *             if configuration is null
     * @throws DocumentGeneratorConfigurationException
     *             Any error occurs during configuring and creating DocumentGenerator
     */
    public static DocumentGenerator getDocumentGenerator(ConfigurationObject configuration)
        throws DocumentGeneratorConfigurationException {
        Util.checkNull(configuration, "configuration");
        DocumentGenerator documentGenerator = new DocumentGenerator();
        try {
            Object[] sources = configuration.getPropertyValues("sources");
            if (sources == null) {
                throw new DocumentGeneratorConfigurationException("miss [sources] property.");
            }
            if (sources.length == 0) {
                throw new DocumentGeneratorConfigurationException(
                    "[sources] property should contains at least one value.");
            }
            for (int i = 0; i < sources.length; i++) {
                // create the template source.
                TemplateSource templateSource = createTemplateSource(sources[i], configuration, "sources");
                // set it to document generator.
                documentGenerator.setTemplateSource((String) sources[i], templateSource);
            }
            Object defaultSource = configuration.getPropertyValue("default_source");
            if (defaultSource != null) {
                // set default template source
                documentGenerator.setDefaultTemplateSource(createTemplateSource(defaultSource, configuration,
                    "default_source"));
            }
        } catch (ConfigurationAccessException e) {
            throw new DocumentGeneratorConfigurationException(
                "error occurs when accessing the given ConfigurationObject.", e);
        }
        return documentGenerator;
    }

    /**
     * Create the TemplateSoure by given sourceId and configuration object.
     * @param sourceId
     *            the id of TemlateSource
     * @param configuration
     *            the configuration object used to create TemplateSource
     * @param propertyName
     *            the name of property which contains given sourceId
     * @return the created TemplateSource
     * @throws DocumentGeneratorConfigurationException
     *             Any error occurs during configuring and creating TemplateSource
     */
    private static TemplateSource createTemplateSource(Object sourceId, ConfigurationObject configuration,
        String propertyName) throws DocumentGeneratorConfigurationException {
        // check given sourceId
        if (!(sourceId instanceof String)) {
            throw new DocumentGeneratorConfigurationException("value of " + propertyName
                + " property should be String type.");
        }
        if (((String) sourceId).trim().length() == 0) {
            throw new DocumentGeneratorConfigurationException("value of " + propertyName
                + " property should not be empty string.");
        }
        try {
            // get class name.
            Object className = configuration.getPropertyValue(sourceId + "_class");
            if (className == null) {
                throw new DocumentGeneratorConfigurationException("property [" + sourceId
                    + "_class] is not exist.");
            }
            if (!(className instanceof String)) {
                throw new DocumentGeneratorConfigurationException("value of [" + sourceId
                    + "_class] property should be String type.");
            }
            // create instance via reflect.
            Object newInstance = Class.forName((String) className).getConstructor(
                new Class[] {String.class, ConfigurationObject.class }).newInstance(
                new Object[] {sourceId, configuration });
            if (!(newInstance instanceof TemplateSource)) {
                throw new DocumentGeneratorConfigurationException("Class [" + className
                    + "] does not implement TemplateSource interface.");
            }
            return (TemplateSource) newInstance;
        } catch (ConfigurationAccessException e) {
            throw new DocumentGeneratorConfigurationException(
                "error occurs when accessing the given configuration object.", e);
        } catch (ClassNotFoundException e) {
            throw new DocumentGeneratorConfigurationException("class for the source [" + sourceId
                + "] is not found.", e);
        } catch (InvocationTargetException e) {
            throw new DocumentGeneratorConfigurationException(
                "error occurs when creating the source template [" + sourceId + "]", e);
        } catch (NoSuchMethodException e) {
            throw new DocumentGeneratorConfigurationException(
                "can not find the ctor accepting String and ConfigurationObject.", e);
        } catch (IllegalAccessException e) {
            throw new DocumentGeneratorConfigurationException("error occurs when accessing the ctor.", e);
        } catch (InstantiationException e) {
            throw new DocumentGeneratorConfigurationException(
                "given class can not be created since it is abstract class or interface.", e);
        }
    }

}
