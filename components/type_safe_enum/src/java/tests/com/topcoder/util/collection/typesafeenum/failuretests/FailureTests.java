/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author enefem21
 * @version 1.1
 */
public class FailureTests extends TestCase {
    /**
     * Aggregates all Failure test cases.
     *
     * @return the aggregated all Failure test cases
     */
    public static Test suite() {
        return new TestSuite(EnumTest.class);
    }

}
