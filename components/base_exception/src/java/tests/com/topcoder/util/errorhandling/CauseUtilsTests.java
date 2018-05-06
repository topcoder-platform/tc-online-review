/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import junit.framework.TestCase;

/**
 * Unit test for the <code>CauseUtils</code> class.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class CauseUtilsTests extends TestCase {
    /**
     * Test the protected constructor <code>CauseUtils()</code>.
     *
     */
    public void testCtor() {
        assertNotNull("CauseUtils instance created incorrect.", new CauseUtils());
    }

    /**
     * Accuracy test for the method <code>getCause(Throwable)</code>. Correct
     * value should be returned.
     */
    public void testGetCause() {
        Throwable cause = new NullPointerException("test");
        assertNull("null should be returned", CauseUtils.getCause(null));
        assertEquals("Correct value should be returned", cause, CauseUtils.getCause(new Throwable(cause)));
    }
}
