/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.persistence.ConfigManagerScheduler;

/**
 * <p>
 * Accuracy tests of <code>{@link ConfigManagerScheduler}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class ConfigManagerSchedulerTest extends SchedulerTest {
    /**
     * <p>
     * Represents the namespace in which the properties are loaded.
     * </p>
     */
    private static final String NAMESPACE = "com.topcoder.util.scheduler.scheduling.persistence.accuracy";

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to junit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        scheduler = new ConfigManagerScheduler(NAMESPACE);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to junit
     */
    protected void tearDown() throws Exception {
        scheduler = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(ConfigManagerSchedulerTest.class);
    }
}
