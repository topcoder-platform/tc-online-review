/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.late.accuracytests;

import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p> Accuracy test cases for <code>LateDeliverableManagementException</code>. </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
public class LateDeliverableManagementExceptionTest {

    /**
     * <p> Error message used for test. </p>
     */
    private final String msg = "msg";

    /**
     * <p> Instance of <code>Throwable</code> used for test. </p>
     */
    private final Throwable cause = new Exception();

    /**
     * <p> Exception data used for test. </p>
     */
    private final ExceptionData data = new ExceptionData();

    /**
     * <p> Accuracy test for <code>LateDeliverableManagementException(String)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableManagementException() throws Exception {
        LateDeliverableManagementException e = new LateDeliverableManagementException(msg);
        assertEquals(msg, e.getMessage());
        assertTrue(e instanceof BaseCriticalException);
    }

    /**
     * <p> Accuracy test for <code>LateDeliverableManagementException(String, Throwable)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableManagementException2() throws Exception {
        LateDeliverableManagementException e = new LateDeliverableManagementException(msg, cause);
        assertEquals(msg, e.getMessage());
        assertEquals(cause, e.getCause());
    }

    /**
     * <p> Accuracy test for <code>LateDeliverableManagementException(String, ExceptionData)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableManagementException3() throws Exception {
        LateDeliverableManagementException e = new LateDeliverableManagementException(msg, data);
        data.setApplicationCode("code");
        assertEquals(msg, e.getMessage());
        assertEquals("code", e.getApplicationCode());
    }

    /**
     * <p> Accuracy test for <code>LateDeliverableManagementException(String, Throwable, ExceptionData)</code>. </p>
     *
     * <p> UnitTests the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableManagementException4() throws Exception {
        LateDeliverableManagementException e =
            new LateDeliverableManagementException(msg, cause, data);
        data.setApplicationCode("code");
        assertEquals(msg, e.getMessage());
        assertEquals(cause, e.getCause());
        assertEquals("code", e.getApplicationCode());
    }
}
