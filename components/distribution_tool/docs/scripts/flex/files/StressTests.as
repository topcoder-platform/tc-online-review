/*
 * Copyright (c) %{current_year}, TopCoder, Inc. All rights reserved
 */
package %{package.name}.stresstests {

    import flexunit.framework.TestCase;
    import flexunit.framework.TestSuite;
    
    
    /**
     * <p>This test case aggregates all Stress test cases.</p>
     *
     * @author TCSDEVELOPER
     * @version %{version.major}.%{version.minor}
     */ 
    public class StressTests extends TestCase {
    
        /**
         * Returns all the stress test cases.
         
         * @return all the stress test cases.
         **/
        public static function suite():TestSuite {
        
            // Add your stress tests here
            // stressTestSuite.addTestSuite(StressTestsClassName)
            var stressTestSuite:TestSuite = new TestSuite();
            
            return stressTestSuite;
        }
    
    }
}