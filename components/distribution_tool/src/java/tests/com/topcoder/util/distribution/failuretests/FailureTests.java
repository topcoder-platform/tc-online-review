/*
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.distribution.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all failure test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(DistributionScriptExecutionContextTest.class);
        suite.addTestSuite(VariableDefinedConditionTest.class);
        suite.addTestSuite(VariableUndefinedConditionTest.class);
        suite.addTestSuite(BaseDistributionScriptCommandTest.class);
        suite.addTestSuite(DefineVariableCommandTest.class);
        suite.addTestSuite(UndefineVariableCommandTest.class);
        suite.addTestSuite(CreateFolderCommandTest.class);
        suite.addTestSuite(CopyFileCommandTest.class);
        suite.addTestSuite(CopyFileTemplateCommandTest.class);
        suite.addTestSuite(ConvertToPDFCommandTest.class);
        suite.addTestSuite(DistributionScriptTest.class);
        suite.addTestSuite(DistributionScriptParserImplTest.class);
        suite.addTestSuite(DistributionToolTest.class);
        return suite;
    }
}
