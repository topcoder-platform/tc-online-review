/*
 * Copyright (C) %{current_year} TopCoder Inc., All Rights Reserved.
 */
package %{package.name} {

    import flexunit.framework.Test;
    import flexunit.framework.TestCase;
    import flexunit.framework.TestSuite;

    import %{package.name}.accuracytests.AccuracyTests;
    import %{package.name}.failuretests.FailureTests;
    import %{package.name}.stresstests.StressTests;


    /**
     * <p>
     * This test case aggregates all test cases.
     * </p>
     *
     * @author TOPCODER
     * @version %{version.major}.%{version.minor}
     */
    public class AllTests extends TestCase {

        public static function suite() : Test {
            var testSuite:TestSuite = new TestSuite();

            // unit tests
            testSuite.addTest(UnitTests.suite());

            //accuracy tests
            testSuite.addTest(AccuracyTests.suite());

            //failure tests
            testSuite.addTest(FailureTests.suite());

            //stress tests
            testSuite.addTest(StressTests.suite());

            return testSuite;
        }

    }
}

