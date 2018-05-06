/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests functionality of GroupException. All consturctors and methods are tested.
 * </p>
 * @author FireIce
 * @version 1.0
 */
public class GroupNotFoundExceptionTestCase extends TestCase {
    /**
     * <p>
     * Represents a string with a detail message.
     * </p>
     */
    private static final String DETAIL_MESSAGE = "detail";

    /**
     * <p>
     * Represents the groupName used in tests.
     * </p>
     */
    private static final String GROUPNAME = "groupA";

    /**
     * <p>
     * Tests accuracy of <code>GroupNotFoundException(String, String)</code> constructor. The detail error message and
     * the groupName should be correct.
     * </p>
     */
    public void testGroupNotFoundExceptionStringStringAccuracy() {
        // Construct GroupNotFoundException with a detail message and a cause
        GroupNotFoundException exception = new GroupNotFoundException(DETAIL_MESSAGE, GROUPNAME);

        // Verify that there is a detail message
        assertNotNull("Should have message", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct", DETAIL_MESSAGE, exception.getMessage());

        // Verify that the groupName is correct
        assertEquals("the fileId returned not correct", GROUPNAME, exception.getGroupName());
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(GroupNotFoundExceptionTestCase.class);
    }
}
