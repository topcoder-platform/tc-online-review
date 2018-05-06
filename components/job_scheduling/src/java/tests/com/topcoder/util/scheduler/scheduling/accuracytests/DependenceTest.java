/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.Dependence;
import com.topcoder.util.scheduler.scheduling.EventHandler;

/**
 * <p>
 * Accuracy tests of <code>{@link Dependence}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class DependenceTest extends TestCase {
    /**
     * <p>
     * Represents the <code>{@link Dependence}</code> instance for testing.
     * </p>
     */
    private Dependence dependence;

    /**
     * <p>
     * Represents the job name used for testing.
     * </p>
     */
    private static final String JOB_NAME = "JUNIT_ACCURACY";

    /**
     * <p>
     * Represents the delay used for testing.
     * </p>
     */
    private static final int DELAY = 20;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     */
    protected void setUp() {
        dependence = new Dependence(JOB_NAME, EventHandler.SUCCESSFUL, DELAY);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     */
    protected void tearDown() {
        dependence = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DependenceTest.class);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Dependence#Dependence(String, String, int)}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     */
    public void testDependence() {
        assertNotNull("failed to create the Dependence.", dependence);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Dependence#getDependentJobName()}</code>.
     * </p>
     * <p>
     * Expects the same JOB_NAME which is set initially.
     * </p>
     */
    public void testGetDependentJobName() {
        assertEquals("failed to get the dependent job name", JOB_NAME, dependence.getDependentJobName());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Dependence#getDependentEvent()}</code>.
     * </p>
     * <p>
     * Expects the same EVENT which is set initially.
     * </p>
     */
    public void testGetDependentEvent() {
        assertEquals("failed to get the dependent event", EventHandler.SUCCESSFUL, dependence
            .getDependentEvent());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link Dependence#getDelay()}</code>.
     * </p>
     * <p>
     * Expects the same DELAY which is set initially.
     * </p>
     */
    public void testGetDelay() {
        assertEquals("failed to get the delay", DELAY, dependence.getDelay());
    }

}
