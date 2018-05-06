/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.stresstests;

/**
 * <p>
 * A helper class to perform those common operations which are helpful for the test.
 * </p>
 * <p>
 * Changes in version 3.1: A new method <code>getPrivateFieldValue</code> is added. And the methods to create new job
 * are also added.
 * </p>
 * 
 * @author 80x86
 * @version 3.1
 * @since 3.1
 */
public class StressTestHelper31 {

    /**
     * Repeat count of the stress test.
     */
    public static final int REPEAT_COUNT = 1000;

    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private StressTestHelper31() {
    }
}
