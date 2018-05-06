/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author PE
 * @version 1.1
 */
public class AccuracyTests extends TestCase {
    /**
     * Get the tes tsuite.
     *
     * @return Test.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(BigDecimalConverterAccuracyTestV11.class);
        suite.addTestSuite(BlobConverterAccuracyTestV11.class);
        suite.addTestSuite(BooleanConverterAccuracyTestV11.class);
        suite.addTestSuite(ByteArrayConverterAccuracyTestV11.class);
        suite.addTestSuite(ByteConverterAccuracyTestV11.class);
        suite.addTestSuite(ClobConverterAccuracyTestV11.class);
        suite.addTestSuite(DateConverterAccuracyTestV11.class);
        suite.addTestSuite(DoubleConverterAccuracyTestV11.class);
        suite.addTestSuite(FloatConverterAccuracyTestV11.class);
        suite.addTestSuite(IntConverterAccuracyTestV11.class);
        suite.addTestSuite(LongConverterAccuracyTestV11.class);
        suite.addTestSuite(ShortConverterAccuracyTestV11.class);
        suite.addTestSuite(StringConverterAccuracyTestV11.class);
        suite.addTestSuite(TimeConverterAccuracyTestV11.class);
        suite.addTestSuite(TimestampConverterAccuracyTestV11.class);
        suite.addTestSuite(OnDemandMapperAccuracyTestV11.class);
        suite.addTestSuite(DatabaseAbstractorAccuracyTestV11.class);
        suite.addTestSuite(IllegalMappingExceptionAccuracyTestV11.class);
        suite.addTestSuite(InvalidCursorStateExceptionAccuracyTestV11.class);
        suite.addTestSuite(CustomResultSetAccuracyTestV11.class);
        suite.addTestSuite(URLConverterAccuracyTestV2.class);

        return suite;
    }
}
