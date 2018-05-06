/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.stresstests;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * Stress test for <code>ExceptionData</code>
 *
 * @author kzhu
 * @version 2.0
 */
public class ExceptionDataStressTests extends TestCase {
    /** The time to do the stress test. */
    private static final int STRESS_TIME = 200;

    /** Private instance used for test. */
    private ExceptionData data = null;

    /**
     * Setup the test environment.
     */
    protected void setUp() {
        data = new ExceptionData();
    }

    /**
     * Stress test for the constructor.
     */
    public void testConstructor() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The instance should be created.", new ExceptionData());
        }

        System.out.println("Test constructor " + STRESS_TIME + " times costs: " + (System.currentTimeMillis() - start)
            + "ms.");
    }

    /**
     * Stress test for the method <code>getInformation(Object)</code>.
     */
    public void testGetInformation() {
        data.setInformation("a", "b");

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("Get information should not return null.", data.getInformation("a"));
        }

        System.out.println("Test getInformation " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>setInformation(Object, Object)</code>.
     */
    public void testSetInformation() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            data.setInformation(i + "", "b");
        }

        System.out.println("Test setInformation " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>isLogged()</code>.
     */
    public void testIsLogged() {
        data.setLogged(false);

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertFalse("Is logged should be false.", data.isLogged());
        }

        System.out.println("Test isLogged " + STRESS_TIME + " times costs: " + (System.currentTimeMillis() - start)
            + "ms.");
    }

    /**
     * Stress test for the method <code>setLogged(boolean)</code>.
     */
    public void testSetLogged() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            data.setLogged(true);
        }

        System.out.println("Test setLogged " + STRESS_TIME + " times costs: " + (System.currentTimeMillis() - start)
            + "ms.");
    }

    /**
     * Stress test for the method <code>getApplicationCode()</code>.
     */
    public void testGetApplicationCode() {
        data.setApplicationCode("ApplicationCode");

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("Application code should be get.", "ApplicationCode", data.getApplicationCode());
        }

        System.out.println("Test getApplicationCode " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>setApplicationCode(String)</code>.
     */
    public void testSetApplicationCode() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            data.setApplicationCode("" + i);
            assertEquals("Wrong code is return.", "" + i, data.getApplicationCode());
        }

        System.out.println("Test setApplicationCode " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>getModuleCode()</code>.
     */
    public void testGetModuleCode() {
        data.setModuleCode("ModuleCode");

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("Application code should be get.", "ModuleCode", data.getModuleCode());
        }

        System.out.println("Test getModuleCode " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>setModuleCode(String)</code>.
     */
    public void testSetModuleCode() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            data.setModuleCode("" + i);
            assertEquals("Wrong code is return.", "" + i, data.getModuleCode());
        }

        System.out.println("Test setModuleCode " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>getErrorCode()</code>.
     */
    public void testGetErrorCode() {
        data.setErrorCode("ErrorCode");

        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("Application code should be get.", "ErrorCode", data.getErrorCode());
        }

        System.out.println("Test getErrorCode " + STRESS_TIME + " times costs: " + (System.currentTimeMillis() - start)
            + "ms.");
    }

    /**
     * Stress test for the method <code>setErrorCode(String)</code>.
     */
    public void testSetErrorCode() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            data.setErrorCode("" + i);
            assertEquals("Wrong code is return.", "" + i, data.getErrorCode());
        }

        System.out.println("Test setErrorCode " + STRESS_TIME + " times costs: " + (System.currentTimeMillis() - start)
            + "ms.");
    }

    /**
     * Stress test for the method <code>getCreationDate()</code>.
     */
    public void testGetCreationDate() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertNotNull("The creation date should be set.", data.getCreationDate());
        }

        System.out.println("Test getCreationDate " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for the method <code>getThreadName()</code>.
     */
    public void testGetThreadName() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("The thread name is wrong.", Thread.currentThread().getName(), data.getThreadName());
        }

        System.out.println("Test getThreadName " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }
}
