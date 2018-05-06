/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.late.accuracytests;

import com.topcoder.management.deliverable.late.impl.LateDeliverableNotFoundException;
import com.topcoder.management.deliverable.late.impl.LateDeliverablePersistenceException;
import com.topcoder.util.errorhandling.ExceptionData;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p> Unit test cases for <code>LateDeliverableNotFoundException</code>. </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class LateDeliverableNotFoundExceptionTest {

    /**
     * <p> Error message used for test. </p>
     */
    private final String msg = "msg";

    /**
     * <p> id used for exception. </p>
     */
    private final long id = 1L;

    /**
     * <p> Instance of <code>Throwable</code> used for test. </p>
     */
    private final Throwable cause = new Exception();

    /**
     * <p> Exception data used for test. </p>
     */
    private final ExceptionData data = new ExceptionData();

    /**
     * <p> Accuracy test for <code>LateDeliverableNotFoundException(String)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableNotFoundException() throws Exception {
        LateDeliverableNotFoundException e = new LateDeliverableNotFoundException(msg, id);
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertTrue(e instanceof LateDeliverablePersistenceException);
    }

    /**
     * <p> Accuracy test for <code>LateDeliverableNotFoundException(String, Throwable)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableNotFoundException2() throws Exception {
        LateDeliverableNotFoundException e = new LateDeliverableNotFoundException(msg, cause, id);
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals(cause, e.getCause());
    }

    /**
     * <p> Accuracy test for <code>LateDeliverableNotFoundException(String, ExceptionData)</code>. </p>
     *
     * <p> Unit test case for the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableNotFoundException3() throws Exception {
        LateDeliverableNotFoundException e = new LateDeliverableNotFoundException(msg, data, id);
        data.setApplicationCode("code");
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals("code", e.getApplicationCode());
    }

    /**
     * <p> Accuracy test for <code>LateDeliverableNotFoundException(String, Throwable, ExceptionData)</code>. </p>
     *
     * <p> UnitTests the constructor.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_LateDeliverableNotFoundException4() throws Exception {
        LateDeliverableNotFoundException e =
            new LateDeliverableNotFoundException(msg, cause, data, id);
        data.setApplicationCode("code");
        
        Assert.assertEquals(msg, e.getMessage());
        Assert.assertEquals(cause, e.getCause());
        Assert.assertEquals("code", e.getApplicationCode());
    }
}
