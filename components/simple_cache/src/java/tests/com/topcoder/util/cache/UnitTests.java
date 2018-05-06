/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This test case aggregates all Simple Cache test cases.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class UnitTests {

    /**
     * Returns test suite which aggregates all unit tests.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();

        // unit tests from version 1.0.1
        suite.addTest(CacheEvictionStrategyTestCase.suite());
        suite.addTest(GetObjectFromCacheTestCase.suite());
        suite.addTest(ClearRemoveObjectFromCacheTestCase.suite());
        suite.addTest(PutObjectInCacheTestCase.suite());
        suite.addTest(KeySetTestCase.suite());
        suite.addTest(CreateCacheTestCase.suite());
        suite.addTest(ConcurrencyTestCase.suite());

        // exception classes.
        suite.addTest(MemoryUtilizationExceptionTestCase.suite());
        suite.addTest(ObjectByteConversionExceptionTestCase.suite());
        suite.addTest(CompressionExceptionTestCase.suite());
        suite.addTest(TypeNotMatchedExceptionTestCase.suite());
        suite.addTest(CacheInstantiationExceptionTestCase.suite());
        suite.addTest(CacheExceptionTestCase.suite());

        // memory utilization handler.
        suite.addTest(SimpleMemoryUtilizationHandlerTestCase.suite());

        // compression handler.
        suite.addTest(DefaultCompressionHandlerTestCase.suite());

        // object byte converter.
        suite.addTest(SerializableObjectByteConverterTestCase.suite());

        // cache entry info.
        suite.addTest(CacheEntryInfoTestCase.suite());

        // cache eviction strategy.
        suite.addTest(BOFCacheEvictionStrategyTestCase.suite());
        suite.addTest(FIFOCacheEvictionStrategyTestCase.suite());
        suite.addTest(LRUCacheEvictionStrategyTestCase.suite());

        // simple cache.
        suite.addTest(SimpleCacheConstructorsTestCase.suite());
        suite.addTest(SimpleCacheFailTestCase.suite());
        suite.addTest(SimpleCacheTestCase.suite());
        suite.addTest(SimpleCacheConcurrencyTestCase.suite());

        // demonstration of intended usage of this component.
        suite.addTest(TestDemo.suite());
        
        // fix bug
        suite.addTestSuite(MemLeakTest.class);

        return suite;
    }
}
