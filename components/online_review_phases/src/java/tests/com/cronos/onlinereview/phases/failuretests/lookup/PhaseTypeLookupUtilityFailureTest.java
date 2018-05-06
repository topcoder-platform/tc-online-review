/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests.lookup;

import com.cronos.onlinereview.phases.failuretests.TestDataFactory;
import com.cronos.onlinereview.phases.failuretests.AbstractTestCase;
import com.cronos.onlinereview.phases.failuretests.ConfigHelper;
import com.cronos.onlinereview.phases.failuretests.mock.MockConnection;
import com.cronos.onlinereview.phases.lookup.PhaseTypeLookupUtility;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>A failure test for {@link PhaseTypeLookupUtility} class. Tests the proper
 * handling of invalid input data by the methods. Passes the invalid arguments to the methods and expects the
 * appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class PhaseTypeLookupUtilityFailureTest extends AbstractTestCase {

    /**
     * <p>Gets the test suite for {@link PhaseTypeLookupUtility} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link PhaseTypeLookupUtility} class.
     */
    public static Test suite() {
        return new TestSuite(PhaseTypeLookupUtilityFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigHelper.releaseSingletonInstance(PhaseTypeLookupUtility.class, "CACHED_PAIRS");
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>Failure test. Tests the {@link PhaseTypeLookupUtility#lookUpId(Connection,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>connection</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testLookUpId_Connection_String_connection_null() {
        try {
            PhaseTypeLookupUtility.lookUpId(null, "Submission");
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link PhaseTypeLookupUtility#lookUpId(Connection,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>value</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testLookUpId_Connection_String_value_null() {
        try {
            PhaseTypeLookupUtility.lookUpId(TestDataFactory.getConnection(), null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link PhaseTypeLookupUtility#lookUpId(Connection,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>value</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testLookUpId_Connection_String_value_ZERO_LENGTH_STRING() {
        try {
            PhaseTypeLookupUtility.lookUpId(TestDataFactory.getConnection(), TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link PhaseTypeLookupUtility#lookUpId(Connection,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>value</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testLookUpId_Connection_String_value_WHITESPACE_ONLY_STRING() {
        try {
            PhaseTypeLookupUtility.lookUpId(TestDataFactory.getConnection(), TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link PhaseTypeLookupUtility#lookUpId(Connection,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#UNKNOWN_LOOKUP_VALUE} as <code>value</code> and expects the
     * <code>SQLException</code> to be thrown.</p>
     */
    public void testLookUpId_Connection_String_value_UNKNOWN_LOOKUP_VALUE() {
        try {
            PhaseTypeLookupUtility.lookUpId(TestDataFactory.getConnection(), TestDataFactory.UNKNOWN_LOOKUP_VALUE);
            Assert.fail("SQLException should have been thrown");
        } catch (SQLException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("SQLException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link PhaseTypeLookupUtility#lookUpId,Connection,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockConnection</code> to throw an exception from any method and
     * expects the <code>SQLException</code> to be thrown.</p>
     */
    public void testLookUpId_Connection_String_SQLError() {
        MockConnection.throwGlobalException(new SQLException());
        try {
            PhaseTypeLookupUtility.lookUpId(TestDataFactory.getMockConnection(), "Approval");
            Assert.fail("SQLException should have been thrown");
        } catch (SQLException e) {
            // expected behavior
        } catch (Exception e) {
            fail("SQLException was expected but the original exception is : " + e);
        }
    }
}
