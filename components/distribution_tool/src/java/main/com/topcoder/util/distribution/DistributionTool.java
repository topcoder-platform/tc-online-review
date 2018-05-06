/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationParserException;
import com.topcoder.configuration.persistence.NamespaceConflictException;
import com.topcoder.configuration.persistence.UnrecognizedFileTypeException;
import com.topcoder.configuration.persistence.UnrecognizedNamespaceException;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigurationObjectSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

/**
 * <p>
 * This is the main class of this component. It can be used for creating
 * distributions of different types. For each supported distribution type this
 * class holds an associated DistributionScript instance. This script is
 * executed when creating a distribution of this type. DistributionTool can be
 * configured using Configuration API and Configuration Persistence components.
 * In this case distribution scripts are read from text files and parsed using
 * DistributionScriptParser instance. DistributionScriptParser instance to be
 * used for this is created with Object Factory. Most of input parameters for
 * distribution script are provided by the user in a string map. But this class
 * can additionally use default parameter values specified in the configuration.
 * All input parameters are used as context variables when executing script
 * commands. Additionally "version.major", "version.minor", "version.micro" and
 * "version.build" variables can be constructed from "version" parameter;
 * "component name", "component_name" and "Component_Name" - from "Component
 * Name" parameter; "package/name" and "package\\name" - from "package.name"
 * parameter. Also DistributionTool always defines the following variables:
 * "distribution_type", "current_year" and "script_dir".
 * </p>
 *
 * <p>
 * <em>Sample Configuration: </em>
 *
 * <pre>
 * &lt;?xml version=&quot;1.0&quot;?&gt;
 * &lt;CMConfig&gt;
 *     &lt;Config name=&quot;com.topcoder.util.distribution.DistributionTool&quot;&gt;
 *         &lt;Property name=&quot;logger_name&quot;&gt;
 *             &lt;Value&gt;my_logger&lt;/Value&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name=&quot;object_factory_config&quot;&gt;
 *             &lt;!-- Put Object Factory configuration here --&gt;
 *             &lt;Property name=&quot;type&quot;&gt;
 *                 &lt;Value&gt;com.topcoder.util.distribution.parsers.DistributionScriptParserImpl&lt;/Value&gt;
 *             &lt;/Property&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name=&quot;script_parser_key&quot;&gt;
 *             &lt;Value&gt;com.topcoder.util.distribution.parsers.DistributionScriptParserImpl&lt;/Value&gt;
 *         &lt;/Property&gt;
 *         &lt;Property name=&quot;scripts&quot;&gt;
 *             &lt;Property name=&quot;script_java&quot;&gt;
 *                 &lt;Property name=&quot;distribution_type&quot;&gt;
 *                     &lt;Value&gt;java&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;script_path&quot;&gt;
 *                     &lt;Value&gt;test_files/scripts/java/script.txt&lt;/Value&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;param_1&quot;&gt;
 *                     &lt;Property name=&quot;name&quot;&gt;
 *                         &lt;Value&gt;output_dir&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;param_2&quot;&gt;
 *                     &lt;Property name=&quot;name&quot;&gt;
 *                         &lt;Value&gt;version&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                     &lt;Property name=&quot;default_value&quot;&gt;
 *                         &lt;Value&gt;1.0.0.1&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;param_3&quot;&gt;
 *                     &lt;Property name=&quot;name&quot;&gt;
 *                         &lt;Value&gt;Component Name&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;param_4&quot;&gt;
 *                     &lt;Property name=&quot;name&quot;&gt;
 *                         &lt;Value&gt;package.name&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;param_5&quot;&gt;
 *                     &lt;Property name=&quot;name&quot;&gt;
 *                         &lt;Value&gt;component_description&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                     &lt;Property name=&quot;default_value&quot;&gt;
 *                         &lt;Value&gt;&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *                 &lt;Property name=&quot;param_6&quot;&gt;
 *                     &lt;Property name=&quot;name&quot;&gt;
 *                         &lt;Value&gt;rs&lt;/Value&gt;
 *                     &lt;/Property&gt;
 *                 &lt;/Property&gt;
 *             &lt;/Property&gt;
 *         &lt;/Property&gt;
 *     &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * </p>
 *
 * <p>
 * <em>Sample Usage of this class: </em>
 *
 * <pre>
 * // Create an instance of DistributionTool using the configuration file and namespace
 * DistributionTool distributionTool = new DistributionTool(
 *         &quot;test_files/DistributionTool.properties&quot;,
 *         &quot;com.topcoder.util.distribution.DistributionTool&quot;);
 * // Create Java design distribution for Test Component 1.1
 * Map&lt;String, String&gt; parameters = new HashMap&lt;String, String&gt;();
 * parameters.put(&quot;output_dir&quot;, &quot;/home/chao/tc_dist&quot;);
 * parameters.put(DistributionTool.VERSION_PARAM_NAME, &quot;1.1&quot;);
 * parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, &quot;Test Component&quot;);
 * parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, &quot;com.topcoder.test&quot;);
 * parameters.put(&quot;rs&quot;, &quot;/home/chao/test/test.rtf&quot;);
 * parameters.put(&quot;additional_doc1&quot;, &quot;/home/chao/test/test_additional.gif&quot;);
 * distributionTool.createDistribution(&quot;java&quot;, parameters);
 * </pre>
 *
 * </p>
 *
 * <p>
 * Thread Safety: This class is immutable and thread safe. It uses
 * DistributionScript instances in thread safe manner (and assumes that the
 * caller uses them in thread safe manner too when these instances are provided
 * by the caller). DistributionScriptCommand instances used by this class are
 * thread safe.
 * </p>
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class DistributionTool {
    /**
     * <p>
     * The default configuration file name used by this class. This is a string
     * constant. It's used in the default constructor.
     * </p>
     */
    public static final String DEFAULT_CONFIG_FILE_NAME = "com/topcoder/util/distribution/DistributionTool.properties";

    /**
     * <p>
     * The default configuration namespace used by this class. This is a string
     * constant. It's used in the default constructor.
     * </p>
     */
    public static final String DEFAULT_CONFIG_NAMESPACE = "com.topcoder.util.distribution.DistributionTool";

    /**
     * <p>
     * The name of the input parameter that holds a component version. This is a
     * string constant. Is used in createDistribution().
     * </p>
     */
    public static final String VERSION_PARAM_NAME = "version";

    /**
     * <p>
     * The name of the input parameter that holds a component name. This is a
     * string constant. Is used in createDistribution().
     * </p>
     */
    public static final String COMPONENT_NAME_PARAM_NAME = "Component Name";

    /**
     * <p>
     * The name of the input parameter that holds a package name. This is a
     * string constant. Is used in createDistribution().
     * </p>
     */
    public static final String PACKAGE_NAME_PARAM_NAME = "package.name";

    /**
     * <p>
     * Represents the build bit index in the version.
     * </p>
     */
    private static final int BUILD_BIT = 3;

    /**
     * <p>
     * The mapping between distribution types and distribution scripts
     * associated with them. Is initialized in the constructor and never changed
     * after that. Cannot be null. Cannot contain null/empty key or null value.
     * Is used in createDistribution().
     * </p>
     */
    private final Map<String, DistributionScript> scripts;

    /**
     * <p>
     * Creates an instance of DistributionTool using the default configuration
     * file and namespace.
     * </p>
     *
     * @throws DistributionToolConfigurationException
     *             if some error occurred when reading configuration of this
     *             class
     */
    public DistributionTool() {
        this(DEFAULT_CONFIG_FILE_NAME, DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Creates an instance of DistributionTool using the given configuration
     * file and namespace.
     * </p>
     *
     * @param namespace
     *            the configuration namespace
     * @param configFileName
     *            the configuration file name
     *
     * @throws IllegalArgumentException
     *             if any argument is null or empty
     * @throws DistributionToolConfigurationException
     *             if some error occurred when reading configuration of this
     *             class
     */
    public DistributionTool(String configFileName, String namespace) {
        this(getConfig(configFileName, namespace));
    }

    /**
     * <p>
     * Creates an instance of DistributionTool using the given scripts.
     * </p>
     *
     * @param scripts
     *            the scripts to be used by this class (keys are distribution
     *            types, values - associated scripts)
     *
     * @throws IllegalArgumentException
     *             if scripts is null/empty, contains null/empty key, null value
     *             or any DistributionScript instance has null scriptFolder
     *             property
     */
    public DistributionTool(Map<String, DistributionScript> scripts) {
        Util.checkNonNull(scripts, "scripts");
        if (scripts.isEmpty()) {
            throw new IllegalArgumentException("The scripts should not be empty.");
        }

        for (Map.Entry<String, DistributionScript> entry : scripts.entrySet()) {
            Util.checkNonNullNonEmpty(entry.getKey(), "key in scripts");
            Util.checkNonNull(entry.getValue(), "value in scripts");

            Util.checkNonNull(entry.getValue().getScriptFolder(),
                    "scriptFolder of DistributionScript");
        }

        this.scripts = new HashMap<String, DistributionScript>(scripts);
    }

    /**
     * <p>
     * Creates an instance of DistributionTool using the given configuration
     * object.
     * </p>
     *
     * @param config
     *            the configuration for this class
     *
     * @throws IllegalArgumentException
     *             if config is null
     * @throws DistributionToolConfigurationException
     *             if some error occurred when reading configuration of this
     *             class or initializing this class using this configuration
     */
    public DistributionTool(ConfigurationObject config) {
        Util.checkNonNull(config, "config");

        // Create map for distribution type-to-script mapping:
        scripts = new HashMap<String, DistributionScript>();

        Log log = getLogFromConfig(config);

        DistributionScriptParser scriptParser = getScriptParser(config);

        // Get scripts configuration:
        ConfigurationObject scriptsConfig = getChild(config, "scripts");

        for (ConfigurationObject scriptConfig : getAllChildren(scriptsConfig,
                "scripts")) {
            if (getConfigName(scriptConfig).startsWith("script_")) {
                // Get distribution type property:
                String distributionType = getStringProperty(scriptConfig,
                        "distribution_type", true, false);
                if (scripts.containsKey(distributionType)) {
                    throw new DistributionToolConfigurationException(
                            "Duplicate distribution type");
                }
                // Create new DistributionScript instance:
                DistributionScript script = new DistributionScript();
                processScriptCommands(script, scriptConfig, scriptParser, log);

                processScriptParams(script, scriptConfig);

                scripts.put(distributionType, script);
            }
        }
        // At least one script is required, if not, throw
        // DistributionToolConfigurationException
        if (scripts.isEmpty()) {
            throw new DistributionToolConfigurationException(
                    "At least one script should be configured.");
        }
    }


    /**
     * <p>
     * Get the root configuration object with specified namespace from the given
     * configuration file.
     * </p>
     *
     * @param configFileName
     *            the configuration file name
     * @param namespace
     *            the namespace
     * @return the configuration object
     *
     * @throws IllegalArgumentException
     *             if any argument is null or empty
     * @throws DistributionToolConfigurationException
     *             if some error occurred when reading configuration of this
     *             class
     */
    private static ConfigurationObject getConfig(String configFileName,
            String namespace) {
        Util.checkNonNullNonEmpty(configFileName, "configFileName");
        Util.checkNonNullNonEmpty(namespace, "namespace");

        ConfigurationFileManager manager;
        try {
            manager = new ConfigurationFileManager(configFileName);
            ConfigurationObject config = manager.getConfiguration(namespace);
            config = getChild(config, namespace);
            return config;
        } catch (ConfigurationParserException e) {
            throw new DistributionToolConfigurationException(
                    "The specified file " + configFileName
                            + "could not be parsed.", e);
        } catch (NamespaceConflictException e) {
            throw new DistributionToolConfigurationException(
                    "Two or more files are associated with the namespace "
                            + namespace, e);
        } catch (UnrecognizedFileTypeException e) {
            throw new DistributionToolConfigurationException(
                    "The specified file " + configFileName
                            + "could not be recoganized.", e);
        } catch (IOException e) {
            throw new DistributionToolConfigurationException(
                    "Error occurs while creating the config file manager.", e);
        } catch (UnrecognizedNamespaceException e) {
            throw new DistributionToolConfigurationException(
                    "The specified namespace " + namespace
                            + "isn't in the currentConfiguration", e);
        }
    }

    /**
     * <p>
     * Get the name of the given configuration object.
     * </p>
     *
     * @param config
     *            the config
     * @return the name of the given configuration object
     *
     * @throws DistributionToolConfigurationException
     *             if fail to get the name
     */
    private String getConfigName(ConfigurationObject config) {
        try {
            return config.getName();
        } catch (ConfigurationAccessException e) {
            throw new DistributionToolConfigurationException(
                    "Failed to get the config's name.", e);
        }
    }

    /**
     * <p>
     * Get all the children configuration objects of the given configuration
     * object.
     * </p>
     *
     * @param config
     *            the config
     * @param name
     *            the name of this config (used to add into exception message if
     *            occurred)
     * @return the array of configuration objects
     *
     * @throws DistributionToolConfigurationException
     *             if fail to get the children configuration objects
     */
    private ConfigurationObject[] getAllChildren(ConfigurationObject config,
            String name) {
        try {
            return config.getAllChildren();
        } catch (ConfigurationAccessException e) {
            throw new DistributionToolConfigurationException(
                    "Failed to get all the children configurations of  " + name
                            + ".", e);
        }
    }

    /**
     * <p>
     * Process the parameters for the script. Get the required parameters and
     * default parameters from the configuration of this script and set them to
     * the corresponding fields of the script.
     * </p>
     *
     * @param script
     *            the script to process
     * @param scriptConfig
     *            the configuration object for this script
     *
     * @throws DistributionToolConfigurationException
     *             if any error occurs during the process
     */
    private void processScriptParams(DistributionScript script,
            ConfigurationObject scriptConfig) {
        // Create new list for required parameter names:
        List<String> requiredParams = new ArrayList<String>();
        // Create new map for optional parameter names and
        // corresponding default values:
        Map<String, String> defaultParams = new HashMap<String, String>();

        for (ConfigurationObject paramConfig : getAllChildren(scriptConfig,
                getConfigName(scriptConfig))) {
            if (getConfigName(paramConfig).startsWith("param_")) {
                // Get parameter name from the configuration:
                String paramName = getStringProperty(paramConfig, "name", true,
                        false);
                String defaultValue = getStringProperty(paramConfig,
                        "default_value", false, true);

                if (defaultValue != null) {
                    // Put optional parameter data to the map:
                    defaultParams.put(paramName, defaultValue);
                } else {
                    // Put required parameter name to the list:
                    requiredParams.add(paramName);
                }
            }
        }
        script.setRequiredParams(requiredParams);
        script.setDefaultParams(defaultParams);
    }

    /**
     * <p>
     * Process the commands for the script. It will first get the script file
     * from 'script_path' and then parse the commands using the scriptParser.
     * </p>
     *
     * @param script
     *            the script to process
     * @param scriptConfig
     *            the config for this script
     * @param scriptParser
     *            the parser to parse commands
     * @param log
     *            the log used when parsing the commands
     *
     * @throws DistributionToolConfigurationException
     *             if any error occurs during the process
     */
    private void processScriptCommands(DistributionScript script,
            ConfigurationObject scriptConfig,
            DistributionScriptParser scriptParser, Log log) {

        // Get script file path from configuration:
        String scriptPath = getStringProperty(scriptConfig, "script_path",
                true, false);
        String scriptFolder = new File(scriptPath).getParent();
        if (scriptFolder == null) {
            // set the script folder to current folder when it's at root
            script.setScriptFolder("./");
        } else {
            script.setScriptFolder(scriptFolder);
        }

        InputStream inputStream = null;
        try {

            inputStream = new FileInputStream(scriptPath);

            // Parse script command from script file:
            scriptParser.parseCommands(inputStream, script, log);
        } catch (FileNotFoundException e) {
            throw new DistributionToolConfigurationException(
                    "The scriptPath doesn't exist.", e);
        } catch (DistributionScriptParsingException e) {
            throw new DistributionToolConfigurationException(
                    "Fail to parse commands in the script.", e);
        } finally {
            Util.safeClose(inputStream);
        }
    }

    /**
     * <p>
     * Get the script parser from the configuration. It will first create the
     * object factory from the 'object_factory_config' and then create the
     * parser object.
     * </p>
     *
     * @param config
     *            the parent configuration object
     * @return the DistributionScriptParser object created
     *
     * @throws DistributionToolConfigurationException
     *             if fail to get the parser
     */
    private DistributionScriptParser getScriptParser(ConfigurationObject config) {
        // Get object factory configuration:
        ConfigurationObject objectFactoryConfig = getChild(config,
                "object_factory_config");

        // Create configuration object specification factory:
        ConfigurationObjectSpecificationFactory cosf;
        try {
            cosf = new ConfigurationObjectSpecificationFactory(
                    objectFactoryConfig);
            // Create object factory:
            ObjectFactory objectFactory = new ObjectFactory(cosf);
            // Get script parser OF key from the configuration:
            String scriptParserKey = getStringProperty(config,
                    "script_parser_key", true, false);
            // Create script parser with OF:
            DistributionScriptParser scriptParser = (DistributionScriptParser) objectFactory
                    .createObject(scriptParserKey);
            return scriptParser;
        } catch (IllegalReferenceException e) {
            throw new DistributionToolConfigurationException(
                    "Error occurs when creating the DistributionScriptParser object.",
                    e);
        } catch (SpecificationConfigurationException e) {
            throw new DistributionToolConfigurationException(
                    "Error occurs when creating the specification factory from the config.",
                    e);
        } catch (InvalidClassSpecificationException e) {
            throw new DistributionToolConfigurationException(
                    "The specification is invalid for the DistributionScriptParser object.",
                    e);
        } catch (ClassCastException e) {
            throw new DistributionToolConfigurationException(
                    "The got object is not instance of DistributionScriptParser.",
                    e);
        }

    }

    /**
     * <p>
     * Get the Log from the configuration object.
     * </p>
     *
     * @param config
     *            the configuration object
     * @return the Log object, or null if 'logger_name' property is not provided
     *
     * @throws DistributionToolConfigurationException
     *             if the property 'logger_name' is empty while provided
     */
    private Log getLogFromConfig(ConfigurationObject config) {
        String loggerName = getStringProperty(config, "logger_name", false,
                false);
        if (loggerName != null) {
            // Get Log instance to be used by commands:
            return LogManager.getLog(loggerName);
        }
        return null;
    }

    /**
     * <p>
     * Get the required child configuration object.
     * </p>
     *
     * @param config
     *            the parent config
     * @param name
     *            the name of the required configuration object
     * @return the child configuration object retrieved
     *
     * @throws DistributionToolConfigurationException
     *             if fail to get the child or the retrieved child is null
     */
    private static ConfigurationObject getChild(ConfigurationObject config, String name) {
        try {
            ConfigurationObject child = config.getChild(name);
            if (child == null) {
                throw new DistributionToolConfigurationException(
                        "The required child configuration object " + name
                                + " is missing.");
            }
            return child;
        } catch (ConfigurationAccessException e) {
            throw new DistributionToolConfigurationException(
                    "Fail to get the child configuration object " + name, e);
        }
    }

    /**
     * <p>
     * Get the string property value for the given property name.
     * </p>
     *
     * @param config
     *            the configuration object
     * @param name
     *            the name of the property
     * @param isRequired
     *            if the property is required not
     * @param canBeEmpty
     *            if the property can be empty or not
     * @return the property value
     *
     * @throws DistributionToolConfigurationException
     *             if the property is missing while it's required, or it's empty
     *             while not allowed
     */
    private String getStringProperty(ConfigurationObject config, String name,
            boolean isRequired, boolean canBeEmpty) {
        String value = null;
        try {
            value = (String) config.getPropertyValue(name);
            if (value == null) {
                if (isRequired) {
                    throw new DistributionToolConfigurationException(
                            "The property " + name + " is required.");
                }
            } else {
                if (value.trim().length() == 0 && !canBeEmpty) {
                    throw new DistributionToolConfigurationException(
                            "The property" + name + " shouldn't be empty.");
                }
            }
            return value;
        } catch (ConfigurationAccessException e) {
            throw new DistributionToolConfigurationException(
                    "Error occurs while getting the property " + name, e);
        }
    }

    /**
     * <p>
     * Creates a distribution of the specified type using the given parameters.
     * </p>
     *
     * @param parameters
     *            the input parameters to be used
     * @param distributionType
     *            the distribution type
     *
     * @throws IllegalArgumentException
     *             if distributionType is null/empty, parameters is null or
     *             contains null/empty key or null value
     * @throws UnknownDistributionTypeException
     *             if the specified distribution type is unknown
     * @throws MissingInputParameterException
     *             if any of required input parameters is missing
     * @throws DistributionScriptCommandExecutionException
     *             if some error occurred when executing one of script commands
     */
    public void createDistribution(String distributionType, Map<String, String> parameters)
        throws UnknownDistributionTypeException, MissingInputParameterException,
        DistributionScriptCommandExecutionException {
        Util.checkNonNullNonEmpty(distributionType, "distributionType");
        Util.checkParams(parameters, "parameters");

        // Get script for the specified distribution type:
        DistributionScript script = scripts.get(distributionType);
        if (script == null) {
            throw new UnknownDistributionTypeException(
                    "Unknown distribution type " + distributionType);
        }

        List<String> requiredParams = script.getRequiredParams();
        for (String requiredParam : requiredParams) {
            // Check if parameters contains a required parameter:
            if (!parameters.containsKey(requiredParam)) {
                throw new MissingInputParameterException(
                        "The required parameter " + requiredParam
                                + " is missing.");
            }
        }
        // Create new script execution context:
        DistributionScriptExecutionContext context = new DistributionScriptExecutionContext();
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            context.setVariable(param.getKey(), param.getValue());
        }
        // Get default parameters for the script:
        Map<String, String> defaultParams = script.getDefaultParams();
        for (Map.Entry<String, String> defaultParam : defaultParams.entrySet()) {
            if (!parameters.containsKey(defaultParam.getKey())) {
                context.setVariable(defaultParam.getKey(), defaultParam
                        .getValue());
            }
        }

        // Prepare special context variables:
        String version = getContextVariable(context, VERSION_PARAM_NAME);
        if (version != null) {
            // Note:
            String[] versionParts = version.split("\\.");

            context.setVariable("version.major", versionParts[0]);
            context.setVariable("version.minor",
                    versionParts.length > 1 ? versionParts[1] : "0");
            context.setVariable("version.micro",
                    versionParts.length > 2 ? versionParts[2] : "0");
            context.setVariable("version.build",
                    versionParts.length > BUILD_BIT ? versionParts[BUILD_BIT] : "1");
        }
        // Get value of "Component Name" variable:
        String componentName = getContextVariable(context, COMPONENT_NAME_PARAM_NAME);
        if (componentName != null) {
            // Get component name with underscores:
            String underscoreComponentName = componentName.replace(" ", "_");
            // Define new variable:
            context.setVariable("component name", componentName.toLowerCase());
            // Define new variable:
            context.setVariable("Component_Name", underscoreComponentName);
            // Define new variable:
            context.setVariable("component_name", underscoreComponentName
                    .toLowerCase());
        }

        // Get value of "package.name" variable:
        String packageName = getContextVariable(context, PACKAGE_NAME_PARAM_NAME);
        if (packageName != null) {
            context.setVariable("package/name", packageName.replace(".", "/"));
            context
                    .setVariable("package\\name", packageName
                            .replace(".", "\\"));
        }
        context.setVariable("distribution_type", distributionType);
        context.setVariable("current_year", new SimpleDateFormat("yyyy")
                .format(new Date()));
        context.setVariable("script_dir", script.getScriptFolder());

        // Get the list of script commands:
        List<DistributionScriptCommand> commands = script.getCommands();
        for (DistributionScriptCommand cmd : commands) {
            cmd.execute(context);
        }
    }

    /**
     * <p>
     * Get the value of given variable name from context.
     * </p>
     *
     * @param context
     *            the context
     * @param name
     *            the name of the variable
     * @return the value, or null if it's not defined in the context or empty
     */
    private String getContextVariable(DistributionScriptExecutionContext context, String name) {
        String value = context.getVariable(name);
        if (value != null) {
            return value.trim().length() == 0 ? null : value;
        }
        return value;
    }
}
