/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for Dependence.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class DependenceTests extends TestCase {
    /**
     * <p>
     * The Dependence instance for testing.
     * </p>
     */
    private Dependence dependence;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     */
    protected void setUp() {
        dependence = new Dependence("jobName", EventHandler.FAILED, 1);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     */
    protected void tearDown() {
        dependence = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DependenceTests.class);
    }

    /**
     * <p>
     * Tests ctor Dependence#Dependence(String,String,int) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created Dependence instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new Dependence instance.", dependence);
    }

    /**
     * <p>
     * Tests ctor Dependence#Dependence(String,String,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobName is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullJobName() {
        try {
            new Dependence(null, EventHandler.FAILED, 1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Dependence#Dependence(String,String,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when jobName is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyJobName() {
        try {
            new Dependence(" ", EventHandler.FAILED, 1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Dependence#Dependence(String,String,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullEvent() {
        try {
            new Dependence("jobName", null, 1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Dependence#Dependence(String,String,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyEvent() {
        try {
            new Dependence("jobName", " ", 1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Dependence#Dependence(String,String,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is invalid and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_InvalidEvent() {
        try {
            new Dependence("jobName", "invalid", 1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor Dependence#Dependence(String,String,int) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when delay is negative and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NegativeDelay() {
        try {
            new Dependence("jobName", EventHandler.FAILED, -1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests Dependence#getDependentJobName() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Dependence#getDependentJobName() is correct.
     * </p>
     */
    public void testGetDependentJobName() {
        assertEquals("Failed to get the job name correctly.", "jobName", dependence.getDependentJobName());
    }

    /**
     * <p>
     * Tests Dependence#getDependentEvent() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Dependence#getDependentEvent() is correct.
     * </p>
     */
    public void testGetDependentEvent() {
        assertEquals("Failed to get the event correctly.", EventHandler.FAILED, dependence.getDependentEvent());
    }

    /**
     * <p>
     * Tests Dependence#getDelay() for accuracy.
     * </p>
     *
     * <p>
     * It verifies Dependence#getDelay() is correct.
     * </p>
     */
    public void testGetDelay() {
        assertEquals("Failed to get the delay correctly.", 1, dependence.getDelay());
    }

}