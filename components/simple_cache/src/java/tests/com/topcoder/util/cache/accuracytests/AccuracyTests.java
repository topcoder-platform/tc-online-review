/**
 * Copyright (C) 2005, TopCoder, Inc. All Rights Reserved
 */
package com.topcoder.util.cache.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case aggregates all accuracy test cases.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class AccuracyTests extends TestCase {
  /**
     * Returns the Accuracy Tests as a suite of tests.
     *
     * @return the Accuracy Tests as a suite of tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        
        suite.addTest(new TestSuite(FIFOCacheEvictionStrategyAccuracyTest.class));
        suite.addTest(new TestSuite(LRUCacheEvictionStrategyAccuracyTest.class));
        suite.addTest(new TestSuite(BOFCacheEvictionStrategyAccuracyTest.class));
        suite.addTest(new TestSuite(SerializableObjectByteConverterAccuracyTest.class));
        suite.addTest(new TestSuite(DefaultCompressionHandlerAccuracyTest.class));
        suite.addTest(new TestSuite(SimpleCacheAccuracyTest.class));

        suite.addTest(new TestSuite(AccuracyCreateCacheTestCase.class));
        suite.addTest(new TestSuite(AccuracyGetObjectFromCacheTestCase.class));
        suite.addTest(new TestSuite(AccuracyPutObjectInCacheTestCase.class));
        suite.addTest(new TestSuite(AccuracyClearRemoveObjectFromCacheTestCase.class));
        suite.addTest(new TestSuite(AccuracyConcurrencyTestCase.class));
        suite.addTest(new TestSuite(AccuracyCacheEvictionStrategyTestCase.class));
        
        return suite;
    }
}
