/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests.impl;

import junit.framework.TestCase;

/**
 * <p>
 * This test case aggregates all test cases for <code>ExternalObjectImpl</code>.
 * </p>
 *
 * @author idx, liulike
 * @version 1.1
 */
public class ExternalObjectImplFailureTest extends TestCase {

    /**
     * Test constructor <code>ExternalObjectImpl(long)</code> with negative <code>id</code>.
     * IllegalArgumentException is expected.
     */
    public void testExternalObjectImpl_NegativeId() {
        try {
            new DummyExternalObjectImpl(-1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

}
