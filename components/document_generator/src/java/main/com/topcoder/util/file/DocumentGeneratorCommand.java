/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationPersistenceException;
import com.topcoder.util.commandline.CommandLineUtility;
import com.topcoder.util.commandline.IllegalSwitchException;
import com.topcoder.util.commandline.Switch;
import com.topcoder.util.file.templatesource.TemplateSource;

/**
 * The DocumentGeneratorCommand class is the command line class used to execute document generation. This
 * class loads configurations via Configuration Persistence component, and creates an instance of
 * DocumentGenerator via DocumentGeneratorFactory. It executes document generation via the created
 * DocumentGenerator instance. Command Line Utility is used to process command line arguments and related
 * stuff.
 * <p>
 * Thread-safety: This class is thread-safe since it's immutable.
 * </p>
 * <p>
 * see the method documentation of <code>main</code> for command line usage of this class.
 * </p>
 * @author gniuxiao, TCSDEVELOPER
 * @version 3.0
 */
public class DocumentGeneratorCommand {
    /**
     * The default configuration file. It's used to load configuration by Configuration Persistence if the
     * configuration file is not provided by command line argument.
     */
    public static final String DEFAULT_CONFIGURATION_FILE = "com/topcoder/util/file/DocumentManager.xml";
    /**
     * The default configuration namespace. It's used to load ConfigurationObject by Configuration Persistence
     * if the configuration namespace is not provided by command line argument.
     */
    public static final String DEFAULT_NAMESPACE = DocumentGenerator.class.getPackage().getName();

    /**
     * Do nothing private ctor.
     */
    private DocumentGeneratorCommand() {
    }

    /**
     * <p>
     * Command line parameters.
     * </p>
     * <p>
     * Generate document.
     * </p>
     * <ul>
     * <li> [-s[ource] #source_id#] -t[emplate] #template_name# -x[ml] #xml_data_file# [-o[ut]
     * output_txt_file] [-c[onfig] #configuration_file#] [-n[amespace] #namespace#]</li>
     * </ul>
     * </p>
     * <p>
     * Add template:
     * <ul>
     * <li> [-s[ource] #source_id#] -t[emplate] #template_name# -a[dd] #template_file# [-c[onfig]
     * #configuration_file#] [-n[amespace] #namespace#]</li>
     * </ul>
     * </p>
     * <p>
     * Remove template:
     * <ul>
     * <li> [-s[ource] #source_id#] -t[emplate] #template_name# -r[emove] [-c[onfig] #configuration_file#]
     * [-n[amespace] #namespace#]</li>
     * </ul>
     * </p>
     * <p>
     * <ol>
     * <li> -s[ource] #source_id# The template source identifier to look the templates into (the sources are
     * configured in the config file). If it is missing then the default template file will be used (see the
     * sample config file). </li>
     * <li> -t[emplate] #template_name# The name of template to be used. The name is specific to the template
     * source. For database sources it is an actual name. For file sources it is the name of the file. For
     * future implementations it might mean other things (file name for CVS sources, name for LDAP). </li>
     * <li> -x[ml] #xml_data_file# The XML file with field data for the template. </li>
     * <li> -o[ut] #output_file# The output text file. If it is missing then the output will be written to the
     * standard output. </li>
     * <li> -a[dd] #template_file# The template file to add to the template source. It will overwrite existing
     * templates with the given name. </li>
     * <li> -r[emove] Removes the template from the template source. </li>
     * <li> -c[onfig] #configuration_file# The configuration file to use,
     * {@link DocumentGeneratorCommand#DEFAULT_CONFIGURATION_FILE} will be used if it's not provided</li>
     * <li> -n[amespace] #namespace# The namespace to load ConfigurationObject,
     * {@link DocumentGeneratorCommand#DEFAULT_NAMESPACE} will be used if it's not provided </li>
     * </ol>
     * </p>
     * <p>
     * Note: As it is indicated above by the [] parenthesis, either shortcuts (-s, -t, -x, -o, a, r, c, n) or
     * full names (-source, -template, -xml, -out, -add, -remove, -config, -namespace) can be used.
     * </p>
     * <p>
     * Note: All exceptions that occur while using the API will be caught and meaningful messages will be
     * displayed.
     * </p>
     * @param params
     *            the command line arguments
     * @since 3.0
     */
    public static void main(String[] params) {
        // Create command line utility.
        CommandLineUtility utility = initCommandLineUtility();
        // Parse params.
        try {
            utility.parse(params);
        } catch (Exception e) {
            // Print help.
            printUsage(utility);
            return;
        }
        // Get valid switches.
        List list = utility.getValidSwitches();
        // Map of valid switches name->value.
        Map map = new HashMap();
        // Fill map of valid switches.
        for (int i = 0; i < list.size(); i++) {
            Switch aSwitch = (Switch) list.get(i);
            map.put(aSwitch.getName(), aSwitch.getValue());
        }
        if (!validateSwitches(map)) {
            return;
        }
        DocumentGenerator generator = createDocumentGenerator(map);
        if (generator == null) {
            return;
        }
        // Try to get source.
        TemplateSource templateSource = generator.getDefaultTemplateSource();
        // Now do main work.
        String source = getSwitch(map, "s", "source");
        if (source != null) {
            // If user specify source.
            templateSource = generator.getTemplateSource(source);
        }
        if (templateSource == null) {
            // If we have not template source then, print help, and exit.
            System.out.println("Have no template source. You can specify template source with -s.");
            return;
        }
        // Try to get template.
        String templateName = getSwitch(map, "t", "template");
        String command = getSwitch(map, "x", "xml");
        if (command != null) {
            executeGenerateDocument(generator, command, templateSource, templateName, getSwitch(map, "o",
                "out"));
        } else {
            command = getSwitch(map, "a", "add");
            if (command != null) {
                executeAddTemplate(command, templateSource, templateName);
            } else {
                executeRemoveTemplate(templateSource, templateName);
            }
        }
    }

    /**
     * <p>
     * Prints available line commands.
     * </p>
     * @param utility
     *            command line utility.
     * @since 2.0
     */
    private static void printUsage(CommandLineUtility utility) {
        System.out.println("Some error happens during command line parsing:");
        System.out.println("You can use following command:");
        System.out.println("Generete document");
        System.out.println("[-s[ource] <source_id>] -t[emplate] <template_name> "
            + "-x[ml] <xml_data_file> [-o[ut] <output_txt_file>] [-c[onfig] "
            + "<configuration_file>] [-n[amespace] <namespace>]");
        System.out.println("Add template:");
        System.out.println("[-s[ource] <source_id>] -t[emplate] <template_name> "
            + "-a[dd] <template_file> [-c[onfig] <configuration_file>] [-n[amespace] <namespace>]");
        System.out.println("Remove template:");
        System.out.println("[-s[ource] <source_id>] -t[emplate] <template_name> "
            + "-r[emove] [-c[onfig] <configuration_file>] [-n[amespace] <namespace>]");
        System.out.println(utility.getUsageString());
    }

    /**
     * <p>
     * Checks that only one from <code>unique</code> array exist in <code>switches</code> set. If
     * <code>atLeastOne</code><code>true</code> then at least one switches should be in set. If some
     * condition not true, then print error message and exit.
     * </p>
     * @return return <code>true</code> if all OK, else <code>false</code>
     * @param unique
     *            the list of strings
     * @param atLeastOne
     *            should be at least one?
     * @param switches
     *            parsed switches
     * @since 2.0
     */
    private static boolean checkUnique(String[] unique, boolean atLeastOne, Map switches) {
        // Count of parsed switches from unique array.
        int count = 0;
        // Calculate count.
        for (int i = 0; i < unique.length; i++) {
            if (switches.containsKey(unique[i])) {
                count++;
            }
        }
        if (count > 1) {
            // If more than one switches was parsed, then print usage message.
            System.out.println("Only one switches from following list can be:");
            for (int i = 0; i < unique.length; i++) {
                System.out.print(unique[i] + " ");
            }
            System.out.println();
            return false;
        }
        if (atLeastOne && (count == 0)) {
            // If at lest one switches should be, then print usage message.
            System.out.println("At least one of the following switches must be present:");
            for (int i = 0; i < unique.length; i++) {
                System.out.print(unique[i] + " ");
            }
            System.out.println();
            return false;
        }
        return true;
    }

    /**
     * <p>
     * If <code>map</code> contain <code>key</code>, then return value for this key, else return value
     * for <code>key1</code>.
     * </p>
     * @return return value of the one of the keys or <code>null</code>
     * @param map
     *            the map
     * @param key1
     *            first key
     * @param key2
     *            second key
     * @since 2.0
     */
    private static String getSwitch(Map map, String key1, String key2) {
        if (map.containsKey(key1)) {
            return (String) map.get(key1);
        } else {
            return (String) map.get(key2);
        }
    }

    /**
     * <p>
     * Create ConfigurationObject instance from given namespace.
     * </p>
     * @param file
     *            The file path from which to create the ConfigurationFileManager.
     * @param namespace
     *            The namespace from which to retrieve configuration from ConfigurationFileManager.
     * @return The configuration object.
     * @throws DocumentGeneratorConfigurationException
     *             If any error occurs when trying to read configuration.
     */
    private static ConfigurationObject createConfigurationObject(String file, String namespace)
        throws DocumentGeneratorConfigurationException {
        try {
            ConfigurationFileManager cfm = new ConfigurationFileManager(file);
            ConfigurationObject config = cfm.getConfiguration(namespace).getChild(namespace);
            if (config == null) {
                throw new DocumentGeneratorConfigurationException("given namespace can not be found.");
            }
            return config;
        } catch (ConfigurationPersistenceException e) {
            throw new DocumentGeneratorConfigurationException(
                "Error occurs when trying to read configuration", e);
        } catch (ConfigurationAccessException e) {
            throw new DocumentGeneratorConfigurationException(
                "Error occurs when trying to read configuration", e);
        } catch (IOException e) {
            throw new DocumentGeneratorConfigurationException(
                "Error occurs when trying to read configuration", e);
        }
    }

    /**
     * Initialize CommandLineUtility for this class.
     * @return the CommandLineUtility for this class.
     * @since 2.0
     */
    private static CommandLineUtility initCommandLineUtility() {
        CommandLineUtility utility = new CommandLineUtility(false, 0, 0);
        // Add all switches.
        try {
            utility.addSwitch(new Switch("s", false, 1, 1, null,
                "<source_id> specify template source, not required"));
            utility.addSwitch(new Switch("source", false, 1, 1, null,
                "<source_id> specify template source, not required"));
            utility
                .addSwitch(new Switch("t", false, 1, 1, null, "<template_name> specify template, required"));
            utility.addSwitch(new Switch("template", false, 1, 1, null,
                "<template_name> specify template, required"));
            utility.addSwitch(new Switch("x", false, 1, 1, null,
                "<xml_data_file> the XML file with data, requred, for generating document"));
            utility.addSwitch(new Switch("xml", false, 1, 1, null,
                "<xml_data_file> the XML file with data, requred, for generating document"));
            utility.addSwitch(new Switch("o", false, 1, 1, null,
                "<output_file> output text file, not required, for generating document"));
            utility.addSwitch(new Switch("out", false, 1, 1, null,
                "<output_file> output text file, not required, for generating document"));
            utility.addSwitch(new Switch("a", false, 1, 1, null,
                "<template_file> the template file to add, required, for add template"));
            utility.addSwitch(new Switch("add", false, 1, 1, null,
                "<template_file> the template file to add, required, for add template"));
            utility.addSwitch(new Switch("r", false, 0, 0, null,
                "remove from template source, required, for remove template"));
            utility.addSwitch(new Switch("remove", false, 0, 0, null,
                "remove from template source, required, for remove template"));
            utility.addSwitch(new Switch("c", false, 1, 1, null,
                "<configuration_file> specify the configuration file to use, not required."));
            utility.addSwitch(new Switch("config", false, 1, 1, null,
                "<configuration_file> specify the configuration file to use, not required."));
            utility.addSwitch(new Switch("n", false, 1, 1, null,
                "<namespace> specify the namespace to load ConfigurationObject, not required."));
            utility.addSwitch(new Switch("namespace", false, 1, 1, null,
                "<namespace> specify the namespace to load ConfigurationObject, not required."));
        } catch (IllegalSwitchException e) {
            // Should not throw.
        }
        return utility;
    }

    /**
     * Validate the given switches map.
     * @param map
     *            the switches map to validate
     * @return true if valid, false otherwise
     */
    private static boolean validateSwitches(Map map) {
        // Only one switches from following should be: -s -source
        if (!checkUnique(new String[] {"s", "source" }, false, map)) {
            return false;
        }
        // Only one switches from following should be: -o -out
        if (!checkUnique(new String[] {"o", "out" }, false, map)) {
            return false;
        }
        // Exactly one switches from following should be: -t -template
        if (!checkUnique(new String[] {"t", "template" }, true, map)) {
            return false;
        }
        // at most one switches from following should be: -c -config
        if (!checkUnique(new String[] {"c", "config" }, false, map)) {
            return false;
        }
        // at most one switches from following should be: -n -namespace
        if (!checkUnique(new String[] {"n", "namespace" }, false, map)) {
            return false;
        }
        // Exactly one switches from following should be: -x -xml -a - add -r -remove
        if (!checkUnique(new String[] {"x", "xml", "a", "add", "r", "remove" }, true, map)) {
            return false;
        }
        // Check that switches -o or -out can be only with -x or -xml.
        if (map.containsKey("o") || map.containsKey("out")) {
            if (!(map.containsKey("x") || map.containsKey("xml"))) {
                System.out.println("The switches -o or -out can be used only with -x or -xml.");
                return false;
            }
        }
        return true;
    }

    /**
     * Create the document generator from the given switches map.
     * @param map
     *            switches map.
     * @return the created DocumentGenerator
     */
    private static DocumentGenerator createDocumentGenerator(Map map) {
        // Get the configuration file name.
        String configFileName = getSwitch(map, "c", "config");
        if (configFileName == null) {
            configFileName = DEFAULT_CONFIGURATION_FILE;
        }
        String namespace = getSwitch(map, "n", "namespace");
        if (namespace == null) {
            namespace = DEFAULT_NAMESPACE;
        }
        DocumentGenerator generator = null;
        try {
            generator = DocumentGeneratorFactory.getDocumentGenerator(createConfigurationObject(
                configFileName, namespace));
        } catch (DocumentGeneratorConfigurationException e) {
            System.out.println("Some exception happens when creating the DocumentGenerator.");
            System.out.println(e.getMessage());
            return null;
        }
        return generator;
    }

    /**
     * execute generate document operation.
     * @param generator
     *            the document generator
     * @param command
     *            The XML file with field data for the template.
     * @param templateSource
     *            the template source
     * @param templateName
     *            the template name
     * @param file
     *            the output file name
     */
    private static void executeGenerateDocument(DocumentGenerator generator, String command,
        TemplateSource templateSource, String templateName, String file) {
        // This is generate document command.
        Reader reader = null;
        try {
            reader = new FileReader(command);
            // Generate document.
            String result = generator.applyTemplate(generator.parseTemplate(templateSource
                .getTemplate(templateName)), reader);
            // Write result to the file or to the console.
            if (file != null) {
                // If user specify file.
                FileWriter writer = new FileWriter(file);
                writer.write(result);
                writer.close();
            } else {
                // User does not specify file.
                System.out.println(result);
            }
        } catch (Exception e) {
            System.out.println("Some exception happens when generate document:");
            System.out.println(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * execute add template operation.
     * @param command
     *            The template file to add to the template source.
     * @param templateSource
     *            the template source
     * @param templateName
     *            the template name
     */
    private static void executeAddTemplate(String command, TemplateSource templateSource, String templateName) {
        // This is add template command.
        try {
            templateSource.addTemplate(templateName, Util.readString(new FileReader(command)));
        } catch (Exception e) {
            System.out.println("Some exception happens when add template:" + e.getMessage());
        }
    }

    /**
     * execute remove template operation.
     * @param templateSource
     *            the template source
     * @param templateName
     *            the template name
     */
    private static void executeRemoveTemplate(TemplateSource templateSource, String templateName) {
        try {
            // This is should be remove command.
            templateSource.removeTemplate(templateName);
        } catch (Exception e) {
            System.out.println("Some exception happens when remove template:" + e.getMessage());
        }
    }
}
