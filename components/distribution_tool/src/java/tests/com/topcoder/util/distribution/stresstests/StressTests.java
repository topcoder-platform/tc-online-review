/**
 *
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.distribution.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class StressTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        // suite.addTest(XXX.suite());
        suite.addTest(DistributionScriptParserImplStressTest.suite());
        suite.addTest(BaseDistributionScriptCommandStressTest.suite());
        suite.addTest(ConvertToPDFCommandStressTest.suite());
        suite.addTest(CopyFileCommandStressTest.suite());
        suite.addTest(CopyFileTemplateCommandStressTest.suite());
        suite.addTest(CreateFolderCommandStressTest.suite());
        suite.addTest(DefineVariableCommandStressTest.suite());
        suite.addTest(UndefineVariableCommandStressTest.suite());
        suite.addTest(DistributionToolStressTest.suite());
        return suite;
    }
}
