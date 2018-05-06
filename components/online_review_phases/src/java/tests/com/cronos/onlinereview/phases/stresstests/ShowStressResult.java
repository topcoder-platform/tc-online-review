/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This is a stress test helper.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0.0
 */
public class ShowStressResult extends TestCase {

    /**
     * <p>
     * Suit.
     * <p>
     *
     * @return the test
     */
    public static Test suite() {

        return new TestSuite(ShowStressResult.class);
    }

    /**
     * <p>
     * Show the stress test result.
     * </p>
     */
    public void test_show() {

        StressHelper stress = new StressHelper();
        stress.showAndDestroyFile();
    }
}
