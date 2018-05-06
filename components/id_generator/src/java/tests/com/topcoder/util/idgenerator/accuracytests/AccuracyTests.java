/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();       
        
        
        suite.addTest(new TestSuite(SimpleGenerationAccuracyTest.class));
        suite.addTest(new TestSuite(ConcurrentGenerationAccuracyTest.class));
        suite.addTest(new TestSuite(TestIDGeneratorImpl.class));
        suite.addTest(new TestSuite(TestIDGeneratorFactory.class));
        suite.addTest(new TestSuite(TestIDGenerationException.class));
        suite.addTest(new TestSuite(TestIDsExhaustedException.class));
        suite.addTest(new TestSuite(TestNoSuchIDSequenceException.class));
        suite.addTest(new TestSuite(TestIDGeneratorEJB.class));
        suite.addTest(new TestSuite(TestExceptions.class));
        suite.addTest(new TestSuite(TestOracleSequenceGenerator.class));

        return suite;
    }

}
