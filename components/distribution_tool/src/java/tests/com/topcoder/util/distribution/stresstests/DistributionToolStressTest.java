/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.stresstests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.util.distribution.DistributionTool;

/**
 * <p>
 * Stress tests for <code>DistributionTool</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DistributionToolStressTest extends StressTestBase {
    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(DistributionToolStressTest.class);
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

        for (int i = 0; i < 10; ++i) {
            del(STRESSTEST_BASE + "stress_test_component" + i + "/");
        }

        del(STRESSTEST_BASE + "test_flex_component2/");
        del(STRESSTEST_BASE + "test_flex_component5/");
        del(STRESSTEST_BASE + "test_flex_component8/");
        del(STRESSTEST_BASE + "test_java_component0/");
        del(STRESSTEST_BASE + "test_java_component3/");
        del(STRESSTEST_BASE + "test_java_component6/");
        del(STRESSTEST_BASE + "test_javascript_component1/");
        del(STRESSTEST_BASE + "test_javascript_component4/");
        del(STRESSTEST_BASE + "test_javascript_component7/");
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

        for (int i = 0; i < 10; ++i) {
            del(STRESSTEST_BASE + "stress_test_component" + i + "/");
        }

        del(STRESSTEST_BASE + "test_flex_component2/");
        del(STRESSTEST_BASE + "test_flex_component5/");
        del(STRESSTEST_BASE + "test_flex_component8/");
        del(STRESSTEST_BASE + "test_java_component0/");
        del(STRESSTEST_BASE + "test_java_component3/");
        del(STRESSTEST_BASE + "test_java_component6/");
        del(STRESSTEST_BASE + "test_javascript_component1/");
        del(STRESSTEST_BASE + "test_javascript_component4/");
        del(STRESSTEST_BASE + "test_javascript_component7/");
    }

    /**
     * <p>
     * Stress test of the constructor DistributionTool(ConfigurationObject config).
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor_Stress() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager(
                "test_files/stresstests/stress_test.properties");
        ConfigurationObject config = configFileManager.getConfiguration(DistributionTool.DEFAULT_CONFIG_NAMESPACE);
        new DistributionTool(config.getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE));
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; ++i) {
            assertNotNull("The instance should be created.", new DistributionTool(config
                    .getChild(DistributionTool.DEFAULT_CONFIG_NAMESPACE)));
        }
        System.out.println("Running DistributionTool.DistributionTool(ConfigurationObject) 100 times used "
                + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * <p>
     * Stress test of method createDistribution(String distributionType, Map<String, String> parameters).
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Stress() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool(STRESSTEST_BASE + "stress_test.properties",
                "com.topcoder.util.distribution.DistributionTool");
        // Create Java design distribution for Test Component 1.1.1.1
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("output_dir", STRESSTEST_BASE);
        parameters.put(DistributionTool.VERSION_PARAM_NAME, "1.1.1.1");
//        parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Stress Test Component");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/stresstests/stresstest_rs.rtf");

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; ++i) {
            parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Stress Test Component" + i);
            distributionTool.createDistribution("java", parameters);
        }
        System.out.println("Running DistributionTool.createDistribution() 10 times used "
                + (System.currentTimeMillis() - start) + "ms.");

        // sleep a little while for the files to generate
        Thread.sleep(5000);

        for (int i = 0; i < 10; i++) {
            // check files
            assertTrue("The file should be generated.", checkFile(STRESSTEST_BASE + "stress_test_component" + i + "/README"));
            assertTrue("The file should be generated.", checkFile(STRESSTEST_BASE + "stress_test_component" + i + "/build.version"));
            assertTrue("The file should be generated.", checkFile(STRESSTEST_BASE + "stress_test_component" + i + "/build.xml"));
            assertTrue("The file should be generated.", checkFile(STRESSTEST_BASE + "stress_test_component" + i + "/build-dependencies.xml"));
            assertTrue("The file should be generated.", checkFile(STRESSTEST_BASE + "stress_test_component" + i + "/build_dist.xml"));
            assertTrue("The file should be generated.", checkFile(STRESSTEST_BASE + "stress_test_component" + i + "/stress_test_component" + i + "_1.1.1_design_dist.jar"));

            // check directories
            String componentBase = STRESSTEST_BASE + "stress_test_component" + i;
            assertTrue("The directory should be generated.", checkDir(componentBase + "/conf"));
            assertTrue("The directory should be generated.", checkDir(componentBase + "/test_files"));
            assertTrue("The directory should be generated.", checkDir(componentBase + "/src/java/main/com/topcoder/test"));
            assertTrue("The directory should be generated.", checkDir(componentBase + "/src/java/tests/com/topcoder/test"));
            assertTrue("The directory should be generated.", checkDir(componentBase + "/docs/javadocs"));
        }
    }

    /**
     * <p>
     * Check if the directory is existed or not.
     * </p>
     *
     * @param dir the name of the directory
     * @return true if it's a dir
     */
    private boolean checkDir(String dir) {
        File file = new File(dir);
        return file.isDirectory();
    }

    /**
     * <p>
     * Check if the file is existed or not.
     * </p>
     *
     * @param name the name of the file
     * @return true if the file exists
     */
    private boolean checkFile(String name) {
        File file = new File(name);

        return file.isFile();
    }

    /**
     * <p>
     * Stress test of method createDistribution(String distributionType, Map<String, String> parameters).
     * In this test, used three threads to generate 3 kinds of distribution and check if they are correctly
     * generated.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateDistribution_Multithread() throws Exception {
        // Create an instance of DistributionTool using the default configuration file and namespace
        DistributionTool distributionTool = new DistributionTool(STRESSTEST_BASE + "multithread_test.properties",
                "com.topcoder.util.distribution.DistributionTool");

        DistributionCreatorThread[] threads = new DistributionCreatorThread[9];
        for (int i = 0; i < 9; ++i) {
            threads[i] = new DistributionCreatorThread(distributionTool, i);
        }

        for (int i = 0; i < 9; ++i) {
            threads[i].start();
        }

        for (int i = 0; i < 9; ++i) {
            threads[i].join();
        }

        // sleep a little while for the files to generate
        Thread.sleep(10000);

        // check if the distributions are generated
        // Note: to simplify the check, here just check if the distributions are generated and pick
        // up some files to check their content, to verify the correctness of the files
        // here we check the file 'build.version'
        String buildVersion;
        String correctFile;
        for (int i = 0; i < 9; i += 3) {
            assertTrue("The Java component design distribution should be generated.", new File(STRESSTEST_BASE
                    + "test_java_component" + i + "/test_java_component" + i + "_1.1.1_design_dist.jar").isFile());
            buildVersion = STRESSTEST_BASE + "test_java_component" + i + "/build.version";
            correctFile = STRESSTEST_BASE + "files_to_verify/java/build" + i + ".version";
        }

        for (int i = 1; i < 9; i += 3) {
            assertTrue("The JavaScript component design distribution should be generated.", new File(STRESSTEST_BASE
                    + "test_javascript_component" + i + "/test_javascript_component" + i + "_2.1.0_design_dist.jar").isFile());
            buildVersion = STRESSTEST_BASE + "test_javascript_component" + i + "/build.version";
            correctFile = STRESSTEST_BASE + "files_to_verify/js/build" + i + ".version";
        }

        for (int i = 2; i < 9; i += 3) {
            assertTrue("The Flex component design distribution should be generated.", new File(STRESSTEST_BASE
                    + "test_flex_component" + i + "/test_flex_component" + i + "_1.1.0_design_dist.zip").exists());
            // check the 'build.version'
            buildVersion = STRESSTEST_BASE + "test_flex_component" + i + "/build.version";
            correctFile = STRESSTEST_BASE + "files_to_verify/flex/build" + i + ".version";
        }
    }
}
