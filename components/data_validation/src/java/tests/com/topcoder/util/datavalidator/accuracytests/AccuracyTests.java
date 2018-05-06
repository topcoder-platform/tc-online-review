/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author KLW
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * <p>
     * get all the accuracy tests for this component.
     * </p>
     *
     * @return all the accuracy tests for this component
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(AndValidatorAccuracyTests.class);
        suite.addTestSuite(BundleInfoAccuracyTests.class);
        suite.addTestSuite(ByteValidatorAccuracyTests.class);
        suite.addTestSuite(CharacterValidatorAccuracyTests.class);
        suite.addTestSuite(DoubleValidatorAccuracyTests.class);
        suite.addTestSuite(FloatValidatorAccuracyTests.class);
        suite.addTestSuite(IntegerValidatorAccuracyTests.class);
        suite.addTestSuite(LongValidatorAccuracyTests.class);
        suite.addTestSuite(NotValidatorAccuracyTests.class);
        suite.addTestSuite(NullValidatorAccuracyTests.class);
        suite.addTestSuite(OrValidatorAccuracyTests.class);
        suite.addTestSuite(PrimitiveValidatorAccuracyTests.class);
        suite.addTestSuite(ShortValidatorAccuracyTests.class);
        suite.addTestSuite(StringValidatorAccuracyTests.class);
        suite.addTestSuite(TypeValidatorAccuracyTests.class);

        return suite;
    }
}
