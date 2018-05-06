/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays.stresstests;

import junit.framework.TestCase;

import com.topcoder.date.workdays.DefaultWorkdaysFactory;

/**
 * <p>
 * Stress test cases for <code>DefaultWorkdaysFactory</code> class.
 * </p>
 *
 * @author chuchao333
 * @version 1.1
 * @since 1.1
 */
public class DefaultWorkdaysFactoryStressTest extends TestCase {
    public void testCreateWorkdaysInstance_Stress() {
        DefaultWorkdaysFactory factory = new DefaultWorkdaysFactory(true);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            assertNotNull("The instance should be created.", factory.createWorkdaysInstance());
        }
        long cost = System.currentTimeMillis() - start;
        System.out.println("Create 1000 workdays instancs using configuration api & configuration persistence cost"
                + cost + " ms.");
    }
}
