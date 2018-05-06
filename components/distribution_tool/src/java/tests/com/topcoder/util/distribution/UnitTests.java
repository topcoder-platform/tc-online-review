/**
 *
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.distribution;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.distribution.commands.BaseDistributionScriptCommandTest;
import com.topcoder.util.distribution.commands.CommandsUtilTest;
import com.topcoder.util.distribution.commands.ConvertToPDFCommandTest;
import com.topcoder.util.distribution.commands.CopyFileCommandTest;
import com.topcoder.util.distribution.commands.CopyFileTemplateCommandTest;
import com.topcoder.util.distribution.commands.CreateFolderCommandTest;
import com.topcoder.util.distribution.commands.DefineVariableCommandTest;
import com.topcoder.util.distribution.commands.ExecuteProcessCommandTest;
import com.topcoder.util.distribution.commands.UndefineVariableCommandTest;
import com.topcoder.util.distribution.commands.conditions.VariableDefinedConditionTest;
import com.topcoder.util.distribution.commands.conditions.VariableNotDefinedConditionTest;
import com.topcoder.util.distribution.parsers.DistributionScriptParserImplTest;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        // suite.addTest(XXX.suite());
        suite.addTest(Demo.suite());
        suite.addTest(UtilTest.suite());
        suite.addTest(CommandsUtilTest.suite());

        suite.addTest(DistributionScriptExecutionContextTest.suite());
        suite.addTest(VariableNotDefinedConditionTest.suite());
        suite.addTest(VariableDefinedConditionTest.suite());
        suite.addTest(UndefineVariableCommandTest.suite());
        suite.addTest(DefineVariableCommandTest.suite());
        suite.addTest(ExecuteProcessCommandTest.suite());
        suite.addTest(CreateFolderCommandTest.suite());
        suite.addTest(CopyFileCommandTest.suite());
        suite.addTest(CopyFileTemplateCommandTest.suite());
        suite.addTest(ConvertToPDFCommandTest.suite());
        suite.addTest(BaseDistributionScriptCommandTest.suite());
        suite.addTest(DistributionScriptParserImplTest.suite());

        suite.addTest(DistributionToolExceptionTest.suite());
        suite.addTest(DistributionToolConfigurationExceptionTest.suite());
        suite.addTest(MissingInputParameterExceptionTest.suite());
        suite.addTest(DistributionScriptCommandExecutionExceptionTest.suite());
        suite.addTest(DistributionScriptParsingExceptionTest.suite());
        suite.addTest(InputFileNotFoundExceptionTest.suite());
        suite.addTest(PDFConversionExceptionTest.suite());
        suite.addTest(ProcessExecutionExceptionTest.suite());
        suite.addTest(UnknownDistributionTypeExceptionTest.suite());
        suite.addTest(DistributionScriptTest.suite());
        suite.addTest(DistributionToolTest.suite());
        return suite;
    }

}
