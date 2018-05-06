/*
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.date.workdays.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.date.workdays.DefaultWorkdaysFactory;
import com.topcoder.date.workdays.Workdays;

/**
 * Accuracy test for class DefaultWorkdaysFactory in version 1.1.
 *
 * @author extra
 * @version 1.1
 */
public class DefaultWorkdaysFactoryTestV11 extends TestCase {

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.
     * </p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(DefaultWorkdaysFactoryTestV11.class);
    }

    /**
     * <p>
     * Tests accuracy of the method createWorkdaysInstance().
     * </p>
     */
    public void testCreateWorkdaysInstanceAccuracy1() {
        DefaultWorkdaysFactory factory = new DefaultWorkdaysFactory(true);
        Workdays instance = factory.createWorkdaysInstance();

        assertNotNull("workdays created", instance);
    }
}
