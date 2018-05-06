/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.stresstests;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.util.distribution.DistributionScriptCommandExecutionException;
import com.topcoder.util.distribution.DistributionTool;
import com.topcoder.util.distribution.MissingInputParameterException;
import com.topcoder.util.distribution.UnknownDistributionTypeException;

/**
 * <p>
 * A thread class used to test the DistributionTool in multiple threads environment.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DistributionCreatorThread extends Thread {
    /**
     * <p>
     * The private field to store the DistributionTool instance.
     * </p>
     */
    private DistributionTool tool;

    /**
     * <p>
     * The private field to represent the distribution type for current thread.
     * </p>
     */
    private String distributionType;

    /**
     * <p>
     * The parameters.
     * </p>
     */
    private Map<String, String> parameters = new HashMap<String, String>();

    /**
     * <p>
     * The constructor.
     * </p>
     *
     * @param tool the DistributionTool instance
     * @param threadNum the thread number to initialize the threads
     */
    public DistributionCreatorThread(DistributionTool tool, int threadNum) {
        this.tool = tool;

        parameters.put("output_dir", "test_files/stresstests/");
        parameters.put(DistributionTool.PACKAGE_NAME_PARAM_NAME, "com.topcoder.test");
        parameters.put("rs", "test_files/stresstests/stresstest_rs.rtf");

        if (threadNum  % 3 == 0) {
            parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Java Component" + threadNum);
            distributionType = "java";
        } else if (threadNum % 3 == 1) {
            parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test JavaScript Component" + threadNum);
            distributionType = "js";
        } else if (threadNum % 3 == 2) {
            parameters.put(DistributionTool.COMPONENT_NAME_PARAM_NAME, "Test Flex Component" + threadNum);
            distributionType = "flex";
        }
    }

    /**
     * <p>
     * The implementation of the run() method. It will create the corresponding distribution.
     * </p>
     */
    public void run() {
        try {
            tool.createDistribution(distributionType, parameters);
        } catch (UnknownDistributionTypeException e) {
            // ignore
        } catch (MissingInputParameterException e) {
            // ignore
        } catch (DistributionScriptCommandExecutionException e) {
            // ignore
        }
    }
}
