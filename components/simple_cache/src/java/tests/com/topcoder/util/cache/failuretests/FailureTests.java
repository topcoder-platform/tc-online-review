package com.topcoder.util.cache.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Failure tests for Simple Cache
 *
 * @author semi_sleep
 * @version 2.0
 */
public class FailureTests extends TestCase {
    /**
     * Runs all failure test
     *
     * @return instance of Test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(BOFCacheEvictionStrategyFailureTest.class);
        suite.addTestSuite(CacheEntryInfoFailureTest.class);
        suite.addTestSuite(DefaultCompressionHandlerFailureTest.class);
        suite.addTestSuite(FIFOCacheEvictionStrategyFailureTest.class);
        suite.addTestSuite(LRUCacheEvictionStrategyFailureTest.class);
        suite.addTestSuite(SerializableObjectByteConverterFailureTest.class);
        suite.addTestSuite(SimpleCacheFailureTest.class);
        suite.addTestSuite(SimpleMemoryUtilizationHandlerFailureTest.class);
        
        return suite;
    }
}
