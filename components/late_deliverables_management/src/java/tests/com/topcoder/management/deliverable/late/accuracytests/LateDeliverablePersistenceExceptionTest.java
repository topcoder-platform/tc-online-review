/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.late.accuracytests;

import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.management.deliverable.late.impl.LateDeliverablePersistenceException;
import com.topcoder.util.errorhandling.ExceptionData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p> Unit test cases for <code>LateDeliverablePersistenceException</code>. </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class LateDeliverablePersistenceExceptionTest {

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
     * <p> Accuracy test for <code>LateDeliverablePersistenceException(String)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverablePersistenceException() throws Exception {
        LateDeliverablePersistenceException e = new LateDeliverablePersistenceException(msg);
        assertEquals(msg, e.getMessage());
        assertTrue(e instanceof LateDeliverableManagementException);
    }

    /**
     * <p> Accuracy test for <code>LateDeliverablePersistenceException(String, Throwable)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverablePersistenceException2() throws Exception {
        LateDeliverablePersistenceException e = new LateDeliverablePersistenceException(msg, cause);
        assertEquals(msg, e.getMessage());
        assertEquals(cause, e.getCause());
    }

    /**
     * <p> Accuracy test for <code>LateDeliverablePersistenceException(String, ExceptionData)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverablePersistenceException3() throws Exception {
        LateDeliverablePersistenceException e = new LateDeliverablePersistenceException(msg, data);
        data.setApplicationCode("code");
        assertEquals(msg, e.getMessage());
        assertEquals("code", e.getApplicationCode());
    }

    /**
     * <p> Accuracy test for <code>LateDeliverablePersistenceException(String, Throwable, ExceptionData)</code>. </p>
     *
     * <p> UnitTests the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverablePersistenceException4() throws Exception {
        LateDeliverablePersistenceException e =
            new LateDeliverablePersistenceException(msg, cause, data);
        data.setApplicationCode("code");
        assertEquals(msg, e.getMessage());
        assertEquals(cause, e.getCause());
        assertEquals("code", e.getApplicationCode());
    }
}
