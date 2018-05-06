/**
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.project.phases.template.stresstests;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */

@RunWith(Suite.class)
@SuiteClasses({
    ProjectPhaseStressTests.class,
    XmlPhaseTemplatePersistenceStressTests.class,
    DBPhaseTemplatePersistenceStressTests.class
})
public class StressTests {

    /**
     * Clear log before run the test cases.
     */
    @BeforeClass
    public static void beforeClass() {
        StressHelper.clearLog();
    }

    /**
     * <p> Add stress test cases to test suite. </p>
     *
     * @return instance of <code>Test</code>.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new JUnit4TestAdapter(StressTests.class));
        return suite;
    }
}
