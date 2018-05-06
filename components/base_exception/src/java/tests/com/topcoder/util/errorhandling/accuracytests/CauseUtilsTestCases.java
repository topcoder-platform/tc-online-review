/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.accuracytests;

import com.topcoder.util.errorhandling.CauseUtils;

import junit.framework.TestCase;


/**
 * <p>
 * Functionality test cases for class <code>CauseUtils</code>.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public class CauseUtilsTestCases extends TestCase {
    /**
     * <p>
     * Test accuracy for method 'getCause()'.<br>
     * </p>
     */
    public void testGetCause_Throwable_Accuracy1() {
        assertNull("Test accuracy for method getCause() failed.", CauseUtils.getCause(null));
    }

    /**
     * <p>
     * Test accuracy for method 'getCause()'.<br>
     * </p>
     */
    public void testGetCause_Throwable_Accuracy2() {
        Throwable cause = new Throwable("The Cause");
        Exception exception = new Exception(cause);
        assertEquals("Test accuracy for method getCause() failed.", cause, CauseUtils.getCause(exception));
    }
}
