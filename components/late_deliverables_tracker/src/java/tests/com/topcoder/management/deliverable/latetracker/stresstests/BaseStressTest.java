/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.latetracker.stresstests;

import java.sql.Connection;
import java.util.Date;

import junit.framework.TestCase;

/**
 * <p>
 * BaseUnitTest class for the stress tests.
 * </p>
 * <p>
 * Thread safe: This class has no state, and thus it is thread safe.
 * </p>
 *
 * @author morehappiness
 * @version 1.0
 */
public class BaseStressTest extends TestCase {
    /** The test count. */
    static final int testCount = 1;

    /**
     * The connection to use.
     */
    static Connection con;

    /** time started to test */
    long start = 0;

    /**
     * Initialize variables.
     *
     * @throws Exception
     *             to JUnit
     */
    public void setUp() throws Exception {
        start = new Date().getTime();

        con = StressTestUtil.createConnection(StressTestUtil.loadProperties(StressTestUtil.DB_PROPERTIES_FILE));

        StressTestUtil.clearDataBase(con);
        StressTestUtil.setUpDataBase(con);

        StressTestUtil.addStressTestConfigurations();
    }

    /**
     * Tears down.
     *
     * @throws Exception
     *             to JUnit
     */
    public void tearDown() throws Exception {
        StressTestUtil.clearDataBase(con);
        StressTestUtil.closeConnection(con);

        StressTestUtil.clearConfigurations();
    }
}