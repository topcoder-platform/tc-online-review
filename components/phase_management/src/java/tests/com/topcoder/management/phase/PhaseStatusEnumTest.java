/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import junit.framework.TestCase;

/**
 * <p>
 * This class contains test cases for PhaseStatusEnum.
 * </p>
 * @author sokol
 * @version 1.0
 */
public class PhaseStatusEnumTest extends TestCase {

    /**
     * <p>
     * Tests {@link PhaseStatusEnum#getName()}.
     * </p>
     * <p>
     * Name should be retrieved successfully.
     * </p>
     */
    public void testGetName() {
        assertEquals("Name should be retrieved successfully.", "Open", PhaseStatusEnum.OPEN.getName());
    }

    /**
     * <p>
     * Tests {@link PhaseStatusEnum#getName()}.
     * </p>
     * <p>
     * Id should be retrieved successfully.
     * </p>
     */
    public void testGetId() {
        assertEquals("Id should be retrieved successfully.", 3, PhaseStatusEnum.CLOSED.getId());
    }
}
