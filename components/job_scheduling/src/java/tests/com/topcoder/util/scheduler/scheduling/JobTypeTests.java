/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for JobType.
 * </p>
 * <p>
 * Changes in version 3.1: two added job types are taken into consideration.
 * </p>
 * @author TCSDEVELOPER
 * @author TCSDEVELOPER
 * @version 3.1
 * @since 3.0
 */
public class JobTypeTests extends TestCase {
    /**
     * <p>
     * Returns all tests.
     * </p>
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(JobTypeTests.class);
    }

    /**
     * <p>
     * Tests JobType#getType() for accuracy.
     * </p>
     * <p>
     * It verifies JobType#getType() is correct.
     * </p>
     * <p>
     * Changes in version 3.1: two new added job types are taken into
     * consideration.
     * </p>
     * @since 3.0
     */
    public void testGetType() {
        assertEquals("Failed to return the value correctly.", 0,
                JobType.JOB_TYPE_EXTERNAL.getType());
        assertEquals("Failed to return the value correctly.", 1,
                JobType.JOB_TYPE_JAVA_CLASS.getType());
        assertEquals("Failed to return the value correctly.", 3,
                JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY.getType());
        assertEquals("Failed to return the value correctly.", 2,
                JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE.getType());
    }

}