/**
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.project.phases.template.stresstests;

import java.io.File;

/**
 * <p> This is the helper class used by stress tests. </p>
 *
 * @author yuanyeyuanye
 * @version 1.0
 */
public class StressHelper {

    /**
     * <p> Template file for "Design" template. </p>
     */
    static final String TEMPLATE_FILE_DESIGN = "test_files/stress/design_template.xml";


    /**
     * <p> Configuration file. </p>
     */
    static final String CONFIG_FILE = "test_files/stress/stress_config.xml";


    /**
     * <p> Configuration namespace for <code>DefaultPhaseTemplate</code>. </p>
     */
    static final String NAMESPACE = "com.topcoder.project.phases.template.DefaultPhaseTemplate";

    /**
     * Clear history logs.
     */
    static void clearLog() {
        File stressDir = new File("test_files/stress");
        if (!stressDir.isDirectory()) {
            stressDir.delete();
        }

        if (!stressDir.exists()) {
            stressDir.mkdirs();
        }

        File[] files = stressDir.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".log")) {
                file.delete();
            }
        }
    }
}
