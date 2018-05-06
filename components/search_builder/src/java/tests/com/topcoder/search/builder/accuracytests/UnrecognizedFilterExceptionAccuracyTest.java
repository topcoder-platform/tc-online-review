/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests;

import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>An accuracy test for {@link UnrecognizedFilterException} class. Tests the methods for
 * proper handling of valid input data and producing accurate results. Passes the valid arguments to the methods and
 * verifies that either the state of the tested instance have been changed appropriately or a correct result is produced
 * by the method.</p>
 *
 * @author isv
 * @version 1.0
 * @since 1.3
 */
public class UnrecognizedFilterExceptionAccuracyTest extends TestCase {

    /**
     * <p>The instances of {@link UnrecognizedFilterException} which are tested. These instances are initialized in
     * {@link #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private UnrecognizedFilterException[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link UnrecognizedFilterException} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link UnrecognizedFilterException} class.
     */
    public static Test suite() {
        return new TestSuite(UnrecognizedFilterExceptionAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new UnrecognizedFilterException[2];
        this.testedInstances[0] = new UnrecognizedFilterException(TestDataFactory.EXCEPTION_MESSAGE,
                                                                  TestDataFactory.EXCEPTION_FILTER);
        this.testedInstances[1] = new UnrecognizedFilterException(null, null);
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        this.testedInstances = null;
        super.tearDown();
    }

    /**
     * <p>Accuracy test. Tests the {@link UnrecognizedFilterException#UnrecognizedFilterException(String, Filter)}
     * constructor for proper behavior.</p>
     *
     * <p>Verifies that the provided message is maintained correctly.</p>
     */
    public void testConstructor_String_Filter_Message() {
        Assert.assertEquals("The message is not stored correctly",
                            TestDataFactory.EXCEPTION_MESSAGE, this.testedInstances[0].getMessage());
    }

    /**
     * <p>Accuracy test. Tests the {@link UnrecognizedFilterException#UnrecognizedFilterException(String, Filter)}
     * constructor for proper behavior.</p>
     *
     * <p>Verifies that the provided <code>null</code> message is maintained correctly.</p>
     */
    public void testConstructor_String_Filter_NullMessage() {
        Assert.assertNull("The message is not stored correctly", this.testedInstances[1].getMessage());
    }

    /**
     * <p>Accuracy test. Tests the {@link UnrecognizedFilterException#UnrecognizedFilterException(String, Filter)}
     * constructor for proper behavior.</p>
     *
     * <p>Verifies that the provided filter is maintained correctly.</p>
     */
    public void testConstructor_String_Filter_Filter() {
        Assert.assertSame("The filter is not stored correctly",
                          TestDataFactory.EXCEPTION_FILTER, this.testedInstances[0].getFilter());
    }

    /**
     * <p>Accuracy test. Tests the {@link UnrecognizedFilterException#UnrecognizedFilterException(String, Filter)}
     * constructor for proper behavior.</p>
     *
     * <p>Verifies that the provided <code>null</code> filter is maintained correctly.</p>
     */
    public void testConstructor_String_Filter_NullFilter() {
        Assert.assertNull("The filter is not stored correctly", this.testedInstances[1].getFilter());
    }
}
