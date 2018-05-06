/**
 *
 * Copyright ? 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(TestNoSuchIDSequenceException.class);
        suite.addTestSuite(TestIDsExhaustedException.class);
        suite.addTestSuite(TestIDGenerationException.class);
        suite.addTestSuite(TestIDGeneratorFactory.class);
        suite.addTestSuite(TestIDGeneratorImpl.class);
        suite.addTestSuite(OracleSequenceGeneratorUnitTests.class);
        suite.addTestSuite(TestIDGeneratorBean.class);
        suite.addTestSuite(DemoTest.class);
        suite.addTestSuite(OracleSequenceGeneratorFixTest.class);
        return suite;
    }

}
