/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests functionality of GroupSearcherNotFoundException. All consturctors and methods are tested.
 * </p>
 * @author FireIce
 * @version 1.0
 */
public class GroupSearcherNotFoundExceptionTestCase extends TestCase {
    /**
     * <p>
     * Represents a string with a detail message.
     * </p>
     */
    private static final String DETAIL_MESSAGE = "detail";

    /**
     * <p>
     * Represents the fileId used in tests.
     * </p>
     */
    private static final String GROUPSEARCHERNAME = "groupSearcherName";

    /**
     * <p>
     * Tests accuracy of <code>GroupSearcherNotFoundException(String, String)</code> constructor. The detail error
     * message and the fildId should be correct.
     * </p>
     */
    public void testGroupSearcherNotFoundExceptionStringStringAccuracy() {
        // Construct GroupSearcherNotFoundException with a detail message and a cause
        GroupSearcherNotFoundException except = new GroupSearcherNotFoundException(DETAIL_MESSAGE, GROUPSEARCHERNAME);

        // Verify that there is a detail message
        assertNotNull("Should have message", except.getMessage());
        assertEquals("Detailed error message with cause should be correct", DETAIL_MESSAGE, except.getMessage());

        // Verify that the fileId is correct
        assertEquals("the fileId returned not correct", GROUPSEARCHERNAME, except.getGroupSearcherName());
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(GroupSearcherNotFoundExceptionTestCase.class);
    }
}
