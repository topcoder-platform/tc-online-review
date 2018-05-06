/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;

/**
 * <p>
 * Unit tests for <code>DistributionTool</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DistributionToolTest extends BaseTest {
    /**
     * <p>
     * The target base directory for the generates files when creating the distribution.
     * </p>
     */
    private static final String TARGET_DIR = "test_files/tc_dist";

    /**
     * <p>
     * The DistributionTool instance for test.
     * </p>
     */
    private DistributionTool target;

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DistributionToolTest.class);
    }

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void setUp() throws Exception {
        super.setUp();

        deleteFolder(TARGET_DIR + "/test_component");

        deleteFolder(TARGET_DIR + "/test_javascript_component");

        deleteFolder(TARGET_DIR + "/test_dotnet_component");

        deleteFolder(TARGET_DIR + "/test_flex_component");

        deleteFolder(TARGET_DIR + "/%{component_name}");
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception
     *             to junit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        target = null;
        deleteFolder(TARGET_DIR + "/test_component");

        deleteFolder(TARGET_DIR + "/test_javascript_component");

        deleteFolder(TARGET_DIR + "/test_dotnet_component");

        deleteFolder(TARGET_DIR + "/test_flex_component");

        deleteFolder(TARGET_DIR + "/%{component_name}");
    }

    /**
     * Test accuracy of the default constructor DistributionTool().
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testCtorDefault_Accuracy() throws Exception {
        target = new DistributionTool();
        assertNotNull("Unable to create the instance.", target);

        Map<String, DistributionScript> scripts = (Map<String, DistributionScript>) getPrivateField(
                DistributionTool.class, target, "scripts");

        assertEquals("One cofigured script should be retrieved.", 1, scripts.size());

        DistributionScript scriptJava = scripts.get("java");
        assertEquals("The value should match.", "test_files" + FILE_SEPARATOR + "scripts" + FILE_SEPARATOR + "java",
                scriptJava.getScriptFolder());
        // System.out.println("1 : " + scriptJava.getCommands().size() + " 2: " + scriptJava.getRequiredParams().size()
        // + " 3: " + scriptJava.getDefaultParams().size());
        assertEquals("The value should match.", 29, scriptJava.getCommands().size());
        assertEquals("The value should match.", 4, scriptJava.getRequiredParams().size());
        assertEquals("The value should match.", 2, scriptJava.getDefaultParams().size());
    }

    /**
     * Test accuracy of the constructor DistributionTool(String configFileName, String namespace).
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testCtorStrStr_Accuracy() throws Exception {
        target = new DistributionTool("test_files/DistributionTool.properties",
                DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        assertNotNull("Unable to create the instance.", target);

        Map<String, DistributionScript> scripts = (Map<String, DistributionScript>) getPrivateField(
                DistributionTool.class, target, "scripts");

        assertEquals("One cofigured script should be retrieved.", 1, scripts.size());

        DistributionScript scriptJava = scripts.get("java");
        assertEquals("The value should match.", "test_files" + FILE_SEPARATOR + "scripts" + FILE_SEPARATOR + "java",
                scriptJava.getScriptFolder());

        assertEquals("The value should match.", 29, scriptJava.getCommands().size());
        assertEquals("The value should match.", 4, scriptJava.getRequiredParams().size());
        assertEquals("The value should match.", 2, scriptJava.getDefaultParams().size());
    }

    /**
     * Test failure of the constructor DistributionTool(String configFileName, String namespace) when the configFileName
     * is null, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorStrStr_Fail1() throws Exception {
        try {
            new DistributionTool(null, DistributionTool.DEFAULT_CONFIG_NAMESPACE);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(String configFileName, String namespace) when the configFileName
     * is empty, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorStrStr_Fail2() throws Exception {
        try {
            new DistributionTool("\t", DistributionTool.DEFAULT_CONFIG_NAMESPACE);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(String configFileName, String namespace) when the namespace is
     * null, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorStrStr_Fail3() throws Exception {
        try {
            new DistributionTool(DistributionTool.DEFAULT_CONFIG_FILE_NAME, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(String configFileName, String namespace) when the namespace is
     * empty, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorStrStr_Fail4() throws Exception {
        try {
            new DistributionTool(DistributionTool.DEFAULT_CONFIG_FILE_NAME, "\t \n ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(String configFileName, String namespace) when IOException
     * happened during creating ConfigFileManager, DistributionToolConfigurationException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorStrStr_Fail5() throws Exception {
        try {
            new DistributionTool("no_such_config_file", DistributionTool.DEFAULT_CONFIG_NAMESPACE);
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(String configFileName, String namespace) when
     * UnrecognizedFileTypeException happened during creating ConfigFileManager, DistributionToolConfigurationException
     * is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorStrStr_Fail6() throws Exception {
        try {
            new DistributionTool("test_files/unrecoganized_config", DistributionTool.DEFAULT_CONFIG_NAMESPACE);
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(String configFileName, String namespace) when
     * ConfigurationParserException happened during creating ConfigFileManager, DistributionToolConfigurationException
     * is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorStrStr_Fail7() throws Exception {
        try {
            new DistributionTool("test_files/error_config.properties", DistributionTool.DEFAULT_CONFIG_NAMESPACE);
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(String configFileName, String namespace) when
     * NamespaceConflictException happened during creating ConfigFileManager, DistributionToolConfigurationException is
     * expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorStrStr_Fail8() throws Exception {
        try {
            new DistributionTool("test_files/conflict.properties", DistributionTool.DEFAULT_CONFIG_NAMESPACE);
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test accuracy of the constructor DistributionTool(ConfigurationObject config).
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testCtorConfigurationObject_Accuracy() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/DistributionTool.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        target = new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
        assertNotNull("Unable to create the instance.", target);

        Map<String, DistributionScript> scripts = (Map<String, DistributionScript>) getPrivateField(
                DistributionTool.class, target, "scripts");

        assertEquals("One cofigured script should be retrieved.", 1, scripts.size());

        DistributionScript scriptJava = scripts.get("java");
        assertEquals("The value should match.", "test_files" + FILE_SEPARATOR + "scripts" + FILE_SEPARATOR + "java",
                scriptJava.getScriptFolder());
        // System.out.println("1 : " + scriptJava.getCommands().size() + " 2: " + scriptJava.getRequiredParams().size()
        // + " 3: " + scriptJava.getDefaultParams().size());
        assertEquals("The value should match.", 29, scriptJava.getCommands().size());
        assertEquals("The value should match.", 4, scriptJava.getRequiredParams().size());
        assertEquals("The value should match.", 2, scriptJava.getDefaultParams().size());
    }

    /**
     * Test accuracy of the default constructor DistributionTool(ConfigurationObject config).
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testCtorConfigurationObject_Accuracy1() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/DistributionTool1.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        target = new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
        assertNotNull("Unable to create the instance.", target);

        Map<String, DistributionScript> scripts = (Map<String, DistributionScript>) getPrivateField(
                DistributionTool.class, target, "scripts");

        assertEquals("One cofigured script should be retrieved.", 1, scripts.size());

        DistributionScript scriptJava = scripts.get("java");
        assertEquals("The value should match.", "test_files" + FILE_SEPARATOR + "scripts" + FILE_SEPARATOR + "java",
                scriptJava.getScriptFolder());
        // System.out.println("1 : " + scriptJava.getCommands().size() + " 2: " + scriptJava.getRequiredParams().size()
        // + " 3: " + scriptJava.getDefaultParams().size());
        assertEquals("The value should match.", 29, scriptJava.getCommands().size());
        assertEquals("The value should match.", 4, scriptJava.getRequiredParams().size());
        assertEquals("The value should match.", 2, scriptJava.getDefaultParams().size());
    }

    /**
     * Test failure of the default constructor DistributionTool(ConfigurationObject config) when config is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorConfigurationObject_Fail() throws Exception {
        try {
            new DistributionTool((ConfigurationObject) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of the default constructor DistributionTool(ConfigurationObject config) when duplicate
     * distribution_type detected, DistributionToolConfigurationException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorConfigurationObject_Fail1() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/config_invalid1.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        try {
            new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test failure of the default constructor DistributionTool(ConfigurationObject config) when duplicate no script
     * configured, DistributionToolConfigurationException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorConfigurationObject_Fail2() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/config_invalid2.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        try {
            new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test failure of the default constructor DistributionTool(ConfigurationObject config) when duplicate no script
     * configured, DistributionToolConfigurationException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorConfigurationObject_Fail3() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/config_invalid3.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        try {
            new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(ConfigurationObject config) when Not empty property configured
     * to empty, DistributionToolConfigurationException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorConfigurationObject_Fail4() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/config_invalid4.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        try {
            new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(ConfigurationObject config) when required property is missing,
     * DistributionToolConfigurationException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorConfigurationObject_Fail5() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/config_invalid5.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        try {
            new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(ConfigurationObject config) when couldn't find the script
     * path, DistributionToolConfigurationException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorConfigurationObject_Fail6() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/config_invalid6.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        try {
            new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(ConfigurationObject config) when failed to parse command,
     * DistributionToolConfigurationException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorConfigurationObject_Fail7() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/config_invalid7.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        try {
            new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(ConfigurationObject config) when couldn't
     * create DistributionScriptParser instance, DistributionToolConfigurationException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorConfigurationObject_Fail8() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/config_invalid8.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        try {
            new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
            fail("DistributionToolConfigurationException is expected.");
        } catch (DistributionToolConfigurationException e) {
            // good
        }
    }

    /**
     * Test accuracy of the constructor DistributionTool(Map<String, DistributionScript> scripts).
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testCtorMap_Accuracy() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/DistributionTool.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        DistributionTool another = new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));

        Map<String, DistributionScript> scripts = (Map<String, DistributionScript>) getPrivateField(
                DistributionTool.class, another, "scripts");

        target = new DistributionTool(scripts);

        assertNotNull("Unable to create the instance.", target);
    }

    /**
     * Test failure of the constructor DistributionTool(Map<String, DistributionScript> scripts) when the argument is
     * null, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorMap_Fail1() throws Exception {
        try {
            new DistributionTool((Map<String, DistributionScript>) null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(Map<String, DistributionScript> scripts) when the argument
     * contains null key, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testCtorMap_Fail2() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/DistributionTool.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        DistributionTool another = new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));

        Map<String, DistributionScript> scripts = (Map<String, DistributionScript>) getPrivateField(
                DistributionTool.class, another, "scripts");
        scripts.put(null, scripts.get("java"));

        try {
            new DistributionTool(scripts);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(Map<String, DistributionScript> scripts) when the argument
     * contains empty key, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testCtorMap_Fail3() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/DistributionTool.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        DistributionTool another = new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));

        Map<String, DistributionScript> scripts = (Map<String, DistributionScript>) getPrivateField(
                DistributionTool.class, another, "scripts");
        scripts.put(null, scripts.get("java"));

        try {
            new DistributionTool(scripts);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of the constructor DistributionTool(Map<String, DistributionScript> scripts) when the argument
     * contains a value with null 'scriptFolder' field, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testCtorMap_Fail4() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/DistributionTool.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        DistributionTool another = new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));

        Map<String, DistributionScript> scripts = (Map<String, DistributionScript>) getPrivateField(
                DistributionTool.class, another, "scripts");
        DistributionScript nullFolder = new DistributionScript();
        scripts.put("null_folder", nullFolder);

        try {
            new DistributionTool(scripts);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test accuracy of method createDistribution(String distributionType, Map<String, String> parameters).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Accuracy() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", TARGET_DIR);
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");
        distributionTool.createDistribution("java", parameters);

        // check the file content of the generated 'build.version'
        String data = getFileAsString(TARGET_DIR + "/test_component/build.version");

        assertTrue("The value should match.", data.contains("# Property file defining the component's information."));
        assertTrue("The value should match.", data.contains("component.name=Test Component"));
        assertTrue("The value should match.", data.contains("component.distfilename=test_component"));
        assertTrue("The value should match.", data.contains("component.package=com.topcoder.test"));
        assertTrue("The value should match.", data.contains("component.packagedir=com/topcoder/test"));
        assertTrue("The value should match.", data.contains("component.version.major=1"));
        assertTrue("The value should match.", data.contains("component.version.minor=1"));
        assertTrue("The value should match.", data.contains("component.version.micro=0"));
        assertTrue("The value should match.", data.contains("component.version.build=1"));
        assertTrue("The value should match.",
                data.contains("# Defines the Main-Class property for the manifest file "
                        + "(e.g. com.topcoder.utility.SomeClass)"));
        assertTrue("The value should match.", data
                .contains("# This property is only used for components that can be run from command line."));
        assertTrue("The value should match.", data.contains("# Leave the property value empty if not applicable."));
        assertTrue("The value should match.", data.contains("component.mainclass="));

        // check the dir and file structures in the output directory
        List<String> files = new ArrayList<String>();
        files.add("conf" + FILE_SEPARATOR + "putYourConfigFilesHere.txt");
        files.add("docs" + FILE_SEPARATOR + "javadocs" + FILE_SEPARATOR + "stylesheet.css");
        files.add("docs" + FILE_SEPARATOR + "Test_Component_Requirements_Specification.pdf");
        files.add("docs" + FILE_SEPARATOR + "test_additional.gif");
        files.add("test_files" + FILE_SEPARATOR + "putYourTestFilesHere.txt");
        files.add("src" + FILE_SEPARATOR + "java" + FILE_SEPARATOR + "main"
                + FILE_SEPARATOR + "com" + FILE_SEPARATOR + "topcoder"
                + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "putYourSourceFilesHere.txt");
        files.add("src" + FILE_SEPARATOR + "java" + FILE_SEPARATOR + "tests"
                + FILE_SEPARATOR + "com" + FILE_SEPARATOR + "topcoder"
                + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "accuracytests"
                + FILE_SEPARATOR + "AccuracyTests.java");
        files.add("src" + FILE_SEPARATOR + "java" + FILE_SEPARATOR + "tests"
                + FILE_SEPARATOR + "com" + FILE_SEPARATOR + "topcoder"
                + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "failuretests"
                + FILE_SEPARATOR + "FailureTests.java");
        files.add("src" + FILE_SEPARATOR + "java" + FILE_SEPARATOR + "tests"
                + FILE_SEPARATOR + "com" + FILE_SEPARATOR + "topcoder"
                + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "stresstests"
                + FILE_SEPARATOR + "StressTests.java");
        files.add("src" + FILE_SEPARATOR + "java" + FILE_SEPARATOR + "tests"
                + FILE_SEPARATOR + "com" + FILE_SEPARATOR + "topcoder"
                + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "AllTests.java");
        files.add("src" + FILE_SEPARATOR + "java" + FILE_SEPARATOR + "tests"
                + FILE_SEPARATOR + "com" + FILE_SEPARATOR + "topcoder"
                + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "UnitTests.java");
        files.add("build.version");
        files.add("build.xml");
        files.add("build-dependencies.xml");
        files.add("build_dist.xml");
        files.add("README");
        files.add("test_component_1.1.0_design_dist.jar");

        assertTrue("All the required files should be existed.",
                checkDirectoryStructures(TARGET_DIR + "/test_component/", files));
    }

    /**
     * Test accuracy of method createDistribution(String distributionType, Map<String, String> parameters).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Accuracy1() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool2.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", TARGET_DIR);
        // This case is only for the branch test, as the default params are not set
        // it will generate a dir TARGET_DIR/%{component_dir}_%{name}
//        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
//        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
//        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");
        distributionTool.createDistribution("java", parameters);
    }

    /**
     * Test accuracy of method createDistribution(String distributionType, Map<String, String> parameters).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Accuracy2() throws Exception {
        // Create an instance of DistributionTool using the configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/full_config.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", "test_files/tc_dist");
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");
        distributionTool.createDistribution("java", parameters);

        // Create JavaScript design distribution for Test JavaScript Component 1.2.3.4
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.2.3.4");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test JavaScript Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test.javascript");
        distributionTool.createDistribution("js", parameters);

        // Create Flex design distribution for Test Flex Component 1.1.2.2
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1.2.2");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Flex Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test.flex");
        distributionTool.createDistribution("flex", parameters);

        // Create DotNet design distribution for Test DotNet Component 2.3.4.5
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "2.3.4.5");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test DotNet Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test.dotnet");
        distributionTool.createDistribution("dotnet", parameters);
        //
        assertTrue("The DotNet Design distribution should be generated.", checkDotNetDistribution());
    }

    /**
     * <p>
     * Check if the generated file structures are what we expected.
     * </p>
     *
     * @return true if correct, false otherwise
     */
    private boolean checkDotNetDistribution() {
        // check the dir and file structures in the output directory
        List<String> files = new ArrayList<String>();
        files.add("conf" + FILE_SEPARATOR + "PutConfigFilesHere.txt");
        files.add("docs" + FILE_SEPARATOR + "Test_DotNet_Component_Requirements_Specification.pdf");
        files.add("docs" + FILE_SEPARATOR + "test_additional.gif");
        files.add("test_files" + FILE_SEPARATOR + "TestFilesGoHere.txt");

        files.add("src" + FILE_SEPARATOR + "csharp" + FILE_SEPARATOR + "main"
                + FILE_SEPARATOR + "com" + FILE_SEPARATOR + "topcoder"
                + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "dotnet" + FILE_SEPARATOR + "AssemblyInfo.cs");

        files.add("src" + FILE_SEPARATOR + "csharp" + FILE_SEPARATOR + "tests"
                + FILE_SEPARATOR + "com" + FILE_SEPARATOR + "topcoder"
                + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "dotnet" + FILE_SEPARATOR + "AccuracyTests"
                + FILE_SEPARATOR + "AddYourAccuracyTests.txt");

        files.add("src" + FILE_SEPARATOR + "csharp" + FILE_SEPARATOR + "tests"
                + FILE_SEPARATOR + "com" + FILE_SEPARATOR + "topcoder"
                + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "dotnet" + FILE_SEPARATOR + "FailureTests"
                + FILE_SEPARATOR + "AddYourFailureTests.txt");

        files.add("src" + FILE_SEPARATOR + "csharp" + FILE_SEPARATOR + "tests"
                + FILE_SEPARATOR + "com" + FILE_SEPARATOR + "topcoder"
                + FILE_SEPARATOR + "test" + FILE_SEPARATOR + "dotnet" + FILE_SEPARATOR + "StressTests"
                + FILE_SEPARATOR + "AddYourStressTests.txt");

        files.add("Build.version");
        files.add("Component Sources.csproj");
        files.add("Build.dependencies");
        files.add("Test DotNet Component.sln");
        files.add("README.txt");
        files.add("Component Tests.csproj");
        return this.checkDirectoryStructures(TARGET_DIR + "/test_dotnet_component/", files);
    }

    /**
     * Test failure of method createDistribution(String distributionType, Map<String, String> parameters) when
     * the distributionType is null, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Fail1() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", TARGET_DIR);
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");
        try {
            distributionTool.createDistribution(null, parameters);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method createDistribution(String distributionType, Map<String, String> parameters) when
     * the distributionType is empty, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Fail2() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", TARGET_DIR);
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");
        try {
            distributionTool.createDistribution("\t\t\t", parameters);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method createDistribution(String distributionType, Map<String, String> parameters) when
     * the parameters is null, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Fail3() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool.properties",
                "com.topcoder.util.distribution.DistributionTool");
//        // Create Java design distribution for Test Component 1.1
//        Map<String, String> parameters = new HashMap<String, String>();
//        parameters.put("output_dir", TARGET_DIR);
//        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
//        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
//        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
//        parameters.put("rs", "test_files/test/test.rtf");
//        parameters.put("additional_doc1", "test_files/test/test_additional.gif");
        try {
            distributionTool.createDistribution("java", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method createDistribution(String distributionType, Map<String, String> parameters) when
     * the parameters contains null key, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Fail4() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", TARGET_DIR);
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");
        parameters.put(null, "null key");
        try {
            distributionTool.createDistribution("java", parameters);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method createDistribution(String distributionType, Map<String, String> parameters) when
     * the parameters contains null key, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Fail5() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", TARGET_DIR);
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");
        parameters.put("\t   ", "empty key");
        try {
            distributionTool.createDistribution("java", parameters);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method createDistribution(String distributionType, Map<String, String> parameters) when
     * the parameters contains null value, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Fail6() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", TARGET_DIR);
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");
        parameters.put("null value", null);
        try {
            distributionTool.createDistribution("java", parameters);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test failure of method createDistribution(String distributionType, Map<String, String> parameters) when
     * the distributionType is unknown, UnknownDistributionTypeException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Fail7() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", TARGET_DIR);
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");

        try {
            distributionTool.createDistribution("unknown type", parameters);
            fail("UnknownDistributionTypeException is expected.");
        } catch (UnknownDistributionTypeException e) {
            // good
        }
    }

    /**
     * Test failure of method createDistribution(String distributionType, Map<String, String> parameters) when
     * the distributionType is unknown, UnknownDistributionTypeException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Fail8() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", TARGET_DIR);
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");

        try {
            distributionTool.createDistribution("unknown type", parameters);
            fail("UnknownDistributionTypeException is expected.");
        } catch (UnknownDistributionTypeException e) {
            // good
        }
    }

    /**
     * Test failure of method createDistribution(String distributionType, Map<String, String> parameters) when
     * required parameter is missing, MissingInputParameterException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Fail9() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool("test_files/DistributionTool.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1
        Map<String, String> parameters = new HashMap<String, String>();
        // missing output_dir
        // parameters.put("output_dir", TARGET_DIR);
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1");
        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/test/test.rtf");
        parameters.put("additional_doc1", "test_files/test/test_additional.gif");

        try {
            distributionTool.createDistribution("java", parameters);
            fail("MissingInputParameterException is expected.");
        } catch (MissingInputParameterException e) {
            // good
        }
    }

    /**
     * <p>
     * Check the files of the given directory.
     * </p>
     *
     * @param baseDir the base directory
     * @param fileNames the list of file names to check
     * @return true if all the files are existed, false otherwise
     */
    private boolean checkDirectoryStructures(String baseDir, List<String> fileNames) {
        if (!new File(baseDir).isDirectory()) {
            return false;
        }

        // check the existence of all the files
        File cur;
        for (String fileName : fileNames) {
            cur = new File(baseDir + fileName);
            if (!cur.exists()) {
                System.out.println("Check failed :" + baseDir + fileName);
                return false;
            }
        }
        return true;
    }
}
