/*
 * Copyright (c) %{current_year}, TopCoder, Inc. All rights reserved
 */
package %{package.name}.failuretests {

    import flexunit.framework.TestCase;
    import flexunit.framework.TestSuite;

    /**
     * <p>This test case aggregates all failure test cases.</p>
     *
     * @author TopCoder
     * @version %{version.major}.%{version.minor}
     */
    public class FailureTests extends TestCase {

        /**
         * Returns all the failure test cases.
         *
         * @return all the failure test caess.
         **/
        public static function suite():TestSuite {

            // Add your failure tests here
            // failureTestSuite.addTestSuite(failureTestClassName)
            var failureTestSuite:TestSuite = new TestSuite();

            return failureTestSuite;
        }
    }
}
