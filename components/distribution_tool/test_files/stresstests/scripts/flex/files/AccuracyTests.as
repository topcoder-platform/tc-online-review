/*
 * Copyright (C) %{current_year} TopCoder Inc., All Rights Reserved.
 */
package %{package.name}.accuracytests {

    import flexunit.framework.Test;
    import flexunit.framework.TestCase;
    import flexunit.framework.TestSuite;

    /**
     * <p>
     * This test case aggregates all Accuracy test cases.
     * </p>
     *
     * @author TopCoder
     * @version %{version.major}.%{version.minor}
     */
    public class AccuracyTests extends TestCase {
        /**
         * <p>
         * Returns all the accuracy test cases.
         * </p>
         *
         * @return all the accuracy test cases.
         */
        public static function suite() : Test {
        
            // Add your accuracy tests here
            // accuracyTestSuite.addTestSuite(accuracyTestClassName)
            var accuracyTestSuite:TestSuite = new TestSuite();


            return accuracyTestSuite;
        }
    }
}
