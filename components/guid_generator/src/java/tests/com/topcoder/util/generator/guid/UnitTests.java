/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class UnitTests extends TestCase {

    /**
     * return a suite of tests.
     * 
     * @return a suite of tests
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(Int32GeneratorTests.suite());
        suite.addTest(UUID128ImplementationTests.suite());
        suite.addTest(UUID32ImplementationTests.suite());
        suite.addTest(IDGeneratorAdapterTests.suite());
        suite.addTest(UUIDTypeTests.suite());
        suite.addTest(AbstractUUIDTests.suite());
        suite.addTest(UUIDUtilityTests.suite());
        suite.addTest(UUIDVersion1GeneratorTests.suite());
        suite.addTest(UUIDVersion4GeneratorTests.suite());
        
        suite.addTest(DemoTests.suite());
        
        // Please uncomment this line when a IDGenerator version capable of instantiating IDGeneartorAdapter is used
        //suite.addTest(IDGeneratorTests.suite());
        return suite;
    }

}
