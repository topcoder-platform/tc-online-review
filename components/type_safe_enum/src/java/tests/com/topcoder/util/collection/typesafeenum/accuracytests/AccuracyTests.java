/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author georgepf, Yeung
 * @version 1.1
 * @since 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * Aggregates all accuracy test cases.
     *
     * @return the aggregated all accuracy test cases
     */
    public static Test suite() {
        return new TestSuite(EnumAccuracyTests.class);
    }

}
