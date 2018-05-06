/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Demo of this component.
 * </p>
 *
 *
 * @author saarixx, TCSDEVELOPER
 * @version 1.0
 */
public class Demo extends BaseTest {
    /**
     * <p>
     * The target base directory for the generates files when creating the distribution.
     * </p>
     */
    private static final String TARGET_DIR = "test_files/tc_dist";

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(Demo.class);
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

        deleteFolder(TARGET_DIR + "/test_component");

        deleteFolder(TARGET_DIR + "/test_javascript_component");

        deleteFolder(TARGET_DIR + "/test_dotnet_component");

        deleteFolder(TARGET_DIR + "/test_flex_component");
    }

    /**
     * <p>
     * The demo of this component.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testDemo() throws Exception {
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
    }
}
