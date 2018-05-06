/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import java.lang.reflect.InvocationTargetException;
import junit.framework.TestCase;

/**
 * This class tests the constructors of a generic Exception.
 * It expects the Exception to have a single parameter constructor (String),
 * and a two-parameters constructor (String, Throwable).
 *
 * @author TexWiller
 * @version 1.0
 */
public abstract class GenericExceptionTest extends TestCase {

    /**
     * A message used to build the exception.
     */
    private static final String MESSAGE = "Test Message";

    /**
     * An Exception used as cause.
     */
    private static final Throwable EXCEPTION = new Exception("Test causing exception");

    /**
     * The exception class which is going to be tested.
     */
    private Class exceptionClass;

    /**
     * Specifies whether the constructor with cause must be tested.
     */
    private boolean testCause;

    /**
     * Builds a new test case for a specific exception class.
     * The class will test also the constructors with cause.
     *
     * @param exceptionClass The Exception class to be tested
     * @param testName The name of this test case
     */
    public GenericExceptionTest(Class exceptionClass, String testName) {
        this(exceptionClass, testName, true);
    }

    /**
     * Builds a new test case for a specific exception class.
     *
     * @param exceptionClass The Exception class to be tested
     * @param testName The name of this test case
     * @param testCause Specifies whether the constructor with causing
     * exception should be tested. <code>true</code> if the test should be
     * done, <code>false</code> otherwise.
     */
    public GenericExceptionTest(Class exceptionClass, String testName, boolean testCause) {
        super(testName);
        this.exceptionClass = exceptionClass;
        this.testCause = testCause;
    }

    /**
     * Tests the no argument constructor.
     */
    public void testConstructor0() {
        Exception testEx = newInstance(new Class[0], new Object[0]);
        assertNull(exceptionClass.getName() + "contains unexpected cause", testEx.getCause());
    }


    /**
     * Tests the single-argument constructor for maintaining the
     * exception message, and not reporting any cause.
     */
    public void testConstructor1() {
        Exception testEx = newInstance(new Class[] {String.class}, new Object[] {MESSAGE});
        assertEquals(exceptionClass.getName() + " message differs from the one given in construction",
                MESSAGE, testEx.getMessage());
        assertNull(exceptionClass.getName() + "contains unexpected cause", testEx.getCause());
    }

    /**
     * Tests the two-arguments constructor for maintaining the
     * exception message and the exception cause. BaseException appends
     * to the original message with this constructor, so a startsWith() check
     * is performed, insted of equals().
     */
    public void testConstructor2() {
        if (!testCause) {
            return;
        }

        Exception testEx = newInstance(new Class[] {String.class, Throwable.class}, new Object[] {MESSAGE, EXCEPTION});
        assertTrue(exceptionClass.getName() + " message differs from the one given in construction",
                testEx.getMessage().startsWith(MESSAGE));
        assertEquals(exceptionClass.getName() + " cause differs from the one given in construction",
                EXCEPTION, testEx.getCause());
    }

    /**
     * Tests the constructor with both <code>null</code> parameters.
     * No error should arise.
     */
    public void testConstructor3() {
        if (testCause) {
            Exception testEx = newInstance(new Class[] {String.class, Throwable.class}, new Object[] {null, null});
            assertNull(exceptionClass.getName() + " contains unexpected message", testEx.getMessage());
            assertNull(exceptionClass.getName() + " contains unexpected cause", testEx.getCause());
        } else {
            Exception testEx = newInstance(new Class[] {String.class}, new Object[] {null});
            assertNull(exceptionClass.getName() + " contains unexpected message", testEx.getMessage());
            assertNull(exceptionClass.getName() + " contains unexpected cause", testEx.getCause());
        }
    }

    /**
     * Convenience method to build an instance of the Exception class
     * specified in construction.
     * @param types The class types of the parameters of the constructor
     * @param values The values of the parameteres of the constructor
     * @return The built Exception (non-<code>null</code>)
     */
    private Exception newInstance(Class[] types, Object[] values) {
        try {
            return (Exception) exceptionClass.getConstructor(types).newInstance(values);
        } catch (SecurityException ex) {
            fail(exceptionClass.getName() + " does not have a public constructor with arguments "
                    + TestsHelper.arrayToString(types));
        } catch (NoSuchMethodException ex) {
            fail(exceptionClass.getName() + " does not have a public constructor with arguments "
                    + TestsHelper.arrayToString(types));
        } catch (IllegalAccessException ex) {
            fail(exceptionClass.getName() + " does not have a public constructor with arguments "
                    + TestsHelper.arrayToString(types));
        } catch (IllegalArgumentException ex) {
            fail("Wrong arguments for constructor " + TestsHelper.arrayToString(types) + " of class "
                    + exceptionClass.getName() + ": " + TestsHelper.arrayToString(values));
        } catch (InvocationTargetException ex) {
            fail(exceptionClass.getName() + " constructor with arguments " + TestsHelper.arrayToString(types)
                + " threw an exception: " + ex.toString());
        } catch (InstantiationException ex) {
            fail(exceptionClass.getName() + " is abstract");
        }
        return null; // Unreachable: fail raises an Error
    }

}
