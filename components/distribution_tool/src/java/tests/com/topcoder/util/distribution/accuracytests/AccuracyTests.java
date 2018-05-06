/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.distribution.accuracytests;

import com.topcoder.util.distribution.accuracytests.commands.BaseDistributionScriptCommandTest;
import com.topcoder.util.distribution.accuracytests.commands.ConvertToPDFCommandTest;
import com.topcoder.util.distribution.accuracytests.commands.CopyFileCommandTest;
import com.topcoder.util.distribution.accuracytests.commands.CopyFileTemplateCommandTest;
import com.topcoder.util.distribution.accuracytests.commands.CreateFolderCommandTest;
import com.topcoder.util.distribution.accuracytests.commands.DefineVariableCommandTest;
import com.topcoder.util.distribution.accuracytests.commands.ExecuteProcessCommandTest;
import com.topcoder.util.distribution.accuracytests.commands.UndefineVariableCommandTest;
import com.topcoder.util.distribution.accuracytests.commands.conditions.VariableDefinedConditionTest;
import com.topcoder.util.distribution.accuracytests.commands.conditions.VariableNotDefinedConditionTest;
import com.topcoder.util.distribution.accuracytests.parsers.DistributionScriptParserImplTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author orange_cloud
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * Returns all accuracy tests.
     *
     * @return all accuracy tests
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(VariableDefinedConditionTest.suite());
        suite.addTest(VariableNotDefinedConditionTest.suite());

        suite.addTest(BaseDistributionScriptCommandTest.suite());
        suite.addTest(ConvertToPDFCommandTest.suite());
        suite.addTest(CopyFileCommandTest.suite());
        suite.addTest(CopyFileTemplateCommandTest.suite());
        suite.addTest(CreateFolderCommandTest.suite());
        suite.addTest(DefineVariableCommandTest.suite());
        suite.addTest(UndefineVariableCommandTest.suite());
        suite.addTest(ExecuteProcessCommandTest.suite());

        suite.addTest(DistributionScriptParserImplTest.suite());

        suite.addTest(DistributionScriptTest.suite());
        suite.addTest(DistributionScriptExecutionContextTest.suite());
        suite.addTest(DistributionToolTest.suite());

        return suite;
    }

}
