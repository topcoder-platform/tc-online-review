/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.late.accuracytests;

import com.topcoder.management.deliverable.late.LateDeliverableManagementConfigurationException;
import com.topcoder.util.errorhandling.BaseRuntimeException;
import com.topcoder.util.errorhandling.ExceptionData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p> Accuracy test cases for <code>LateDeliverableManagementConfigurationException</code>. </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
public class LateDeliverableManagementConfigurationExceptionTest {

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
     * <p> Accuracy test for <code>LateDeliverableManagementConfigurationException(String)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableManagementConfigurationException() throws Exception {
        LateDeliverableManagementConfigurationException e = new LateDeliverableManagementConfigurationException(msg);
        assertEquals(msg, e.getMessage());

        assertTrue(e instanceof BaseRuntimeException);
    }

    /**
     * <p> Accuracy test for <code>LateDeliverableManagementConfigurationException(String, Throwable)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableManagementConfigurationException2() throws Exception {
        LateDeliverableManagementConfigurationException e =
            new LateDeliverableManagementConfigurationException(msg, cause);
        assertEquals(msg, e.getMessage());
        assertEquals(cause, e.getCause());
    }

    /**
     * <p> Accuracy test for <code>LateDeliverableManagementConfigurationException(String, ExceptionData)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableManagementConfigurationException3() throws Exception {
        LateDeliverableManagementConfigurationException e =
            new LateDeliverableManagementConfigurationException(msg, data);
        data.setApplicationCode("code");
        assertEquals(msg, e.getMessage());
        assertEquals("code", e.getApplicationCode());
    }

    /**
     * <p> Accuracy test for <code>LateDeliverableManagementConfigurationException(String, Throwable,
     * ExceptionData)</code>. </p>
     *
     * <p> UnitTests the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableManagementConfigurationException4() throws Exception {
        LateDeliverableManagementConfigurationException e =
            new LateDeliverableManagementConfigurationException(msg, cause, data);
        data.setApplicationCode("code");
        assertEquals(msg, e.getMessage());
        assertEquals(cause, e.getCause());
        assertEquals("code", e.getApplicationCode());
    }
}
