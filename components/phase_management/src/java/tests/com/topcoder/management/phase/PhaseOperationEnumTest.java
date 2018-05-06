/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import junit.framework.TestCase;

/**
 * <p>
 * This class contains tests for PhaseOperationEnum.
 * </p>
 * @author sokol
 * @version 1.0
 */
public class PhaseOperationEnumTest extends TestCase {

    /**
     * Tests {@link PhaseOperationEnum#getName()}.
     * <p>
     * Name should be retrieved successfully.
     * </p>
     */
    public void testGetName() {
        assertEquals("Name should be retrieved successfully.", "start", PhaseOperationEnum.START.getName());
    }
}
