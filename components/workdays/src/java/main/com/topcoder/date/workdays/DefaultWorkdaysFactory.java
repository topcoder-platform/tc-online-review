/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays;

import java.io.IOException;
import java.util.Map;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This is the default implementation of WorkdaysFactory. It creates instances of DefaultWorkdays type. It loads the
 * configuration file under the constant namespace. If no error occurs and the namespace contains 2 properties:
 * file_name and file_format, it will create a DefaultWorkdays using the constructor with 2 string arguments. If the
 * file_format is missing, the DefaultWorkdays.XML_FILE_FORMAT is used.
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong> This class is immutable and thread safe.
 * </p>
 *
 * <p>
 * <strong> Change log:</strong> Added support of additional configuration strategy with use of Configuration API and
 * Configuration Persistence components.Usage of Configuration Manager for reading configuration is now deprecated.
 * </p>
 *
 * <p>
 * The sample configuration file.
 * </p>
 *
 * <p>
 * <em>file_name=test_files/workdays_unittest.properties</em>
 * </p>
 *
 * <p>
 * <em>ile_name=test_files/workdays_unittest.properties</em>
 * </p>
 *
 * <p>
 * Sample usage.
 * </p>
 *
 * <p>
 * WorkdaysFactory factory = new DefaultWorkdaysFactory(true);
 * Workdays w1 = factory.createWorkdaysInstance();
 * </p>
 *
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.1
 */
public class DefaultWorkdaysFactory implements WorkdaysFactory {
    /**
     * <p>
     * Represents the default name of the configuration file for the DefaultWorkdaysFactory. This is a string constant.
     * </p>
     */
    public static final String CONFIGURATION_FILE = "com/topcoder/date/workdays/defaultWorkdaysFactory.properties";

    /**
     * <p>
     * Represents the namespace under the file which is loaded by the Configuration Manager or Configuration
     * Persistence. This is a string constant.
     * </p>
     */
    public static final String NAMESPACE = "com.topcoder.date.workdays";

    /**
     * <p>
     * Represents the file_name property name under the default workdays factory namespace which is loaded from the
     * configuration. This is a string constant.
     * </p>
     */
    public static final String FILE_NAME_PROPERTY = "file_name";

    /**
     * <p>
     * Represents the file_format property name under the default workdays factory namespace which is loaded from the
     * configuration. This is a string constant.
     * </p>
     */
    public static final String FILE_FORMAT_PROPERTY = "file_format";

    /**
     * <p>
     * The value indicating whether Configuration API & Configuration Persistence or Configuration Manager component
     * must be used for loading configuration of this class. Is initialized in the constructor and never changed after
     * that. Is used in createWorkdaysInstance().
     * </p>
     *
     * @since 1.1
     */
    private final boolean useConfigurationAPI;

    /**
     * <p>
     * Creates an instance of DefaultWorkdaysFactory that uses Configuration Manager.
     * </p>
     */
    @Deprecated
    public DefaultWorkdaysFactory() {
        this(false);
    }

    /**
     * <p>
     * Creates an instance of DefaultWorkdaysFactory.
     * </p>
     *
     * @param useConfigurationAPI
     *            true if Configuration API and Configuration Persistence components should be used for loading
     *            configuration, false - for Configuration Manager (note that usage of false here is deprecated).
     */
    public DefaultWorkdaysFactory(boolean useConfigurationAPI) {
        this.useConfigurationAPI = useConfigurationAPI;
    }

    /**
     * <p>
     * Creates a DefaultWorkdays. It loads the configuration file under the constant namespace. If no error occurs and
     * the namespace contains 2 properties: file_name and file_format, it will create a DefaultWorkdays using the
     * constructor with 2 string arguments. If the file_format is missing, the DefaultWorkdays.XML_FILE_FORMAT is used.
     * </p>
     *
     * <p>
     * If an error loading the file occurs, or the namespace doesn't contain a file name, or the DefaultWorkdays throw
     * an exception, a DefaultWorkdays constructed with the constructor with no arguments is returned.
     * </p>
     *
     * @return a DefaultWorkdays instance.
     */
    public Workdays createWorkdaysInstance() {
        try {
            if (useConfigurationAPI) {
                // Create configuration file manager.
                ConfigurationFileManager configurationFileManager = new ConfigurationFileManager();
                // Get configuration map from the manager.
                Map<String, ConfigurationObject> configurationMap = configurationFileManager.getConfiguration();
                if (!configurationMap.containsKey(NAMESPACE)) {
                    // Load configuration file.
                    configurationFileManager.loadFile(NAMESPACE, CONFIGURATION_FILE);
                }
                // Get configuration for this class from the manager.
                ConfigurationObject configuration = configurationFileManager.getConfiguration(NAMESPACE);
                // Get DefaultWorkdays configuration file name.
                String fileName = (String) configuration.getChild("default").getPropertyValue(FILE_NAME_PROPERTY);
                if (fileName == null || fileName.trim().length() == 0) {
                    throw new IllegalArgumentException("the fileName can not be null or empty");
                }
                // Create DefaultWorkdays using the obtained configuration file.
                return new DefaultWorkdays(fileName);
            } else {
                // Get configuration manager instance.
                ConfigManager manager = ConfigManager.getInstance();
                if (!manager.existsNamespace(NAMESPACE)) {
                    // Add the namespace of DefaultWorkdaysFactory to configManager.
                    manager.add(NAMESPACE, CONFIGURATION_FILE, ConfigManager.CONFIG_PROPERTIES_FORMAT);
                }
                // Get DefaultWorkdays configuration file name.
                String fileName = manager.getString(NAMESPACE, FILE_NAME_PROPERTY);
                // Get DefaultWorkdays configuration file format.
                String fileFormat = manager.getString(NAMESPACE, FILE_FORMAT_PROPERTY);
                if (fileFormat == null) {
                    fileFormat = DefaultWorkdays.XML_FILE_FORMAT;
                }
                // Create DefaultWorkdays instance using Configuration Manager.
                return new DefaultWorkdays(fileName, fileFormat);
            }
        } catch (BaseException e) {
            // an error loading the file occurs, or the namespace doesn't contain a file name,
            // or the DefaultWorkdays throw an exception
            return new DefaultWorkdays();
        } catch (IOException e) {
            return new DefaultWorkdays();
        } catch (IllegalArgumentException e) {
            return new DefaultWorkdays();
        }
    }
}
