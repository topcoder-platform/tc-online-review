/*
 * Copyright (C) %{current_year} TopCoder Inc., All Rights Reserved.
 */
 
 
package %{package.name} {

    import flexunit.framework.Test;
    import flexunit.framework.TestCase;
    import flexunit.framework.TestSuite;

    /**
     * <p>
     * This test case aggregates all Unit test cases.
     * </p>
     *
     * @author TopCoder
     * @version %{version.major}.%{version.minor}
     */
    public class UnitTests extends TestCase {
        /**
         * <p>
         * All unit test cases.
         * </p>
         *
         * @return The test suite.
         */
        public static function suite() : Test {
            var testSuite:TestSuite = new TestSuite();

            // Add your unit tests here
            // testSuite.addTestSuite(TestClassName);
            
            return testSuite;
        }
    }
}
