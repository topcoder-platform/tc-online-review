/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKExceptionFactoryTest.java
 *
 * Copyright (c), 2004. TopCoder, Inc. All rights reserved.
 *
 */
package com.topcoder.util.net.ldap.sdkinterface;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A test case testing the behavior of <code>LDAPSDKExceptionFactory</code> class. No special settings are required to
 * run this test.
 *
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class LDAPSDKExceptionFactoryTest extends TestCase {

    /**
     * A <code>Map</code> mapping the LDAP error codes to corresponding exception class names. This map is populated
     * within <code>setUp()</code> method with error codes and <code>String</code> names of subclasses of <cod>
     * LDAPSDKException</code> as specified by the design.
     */
    private Map code2Class = null;

    public LDAPSDKExceptionFactoryTest(String testName) {
        super(testName);
    }

    /**
     * Initializes the internal map and populates it with LDAP error code - Expected exception class name pairs in
     * accordance with design specification.
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        code2Class = new HashMap();

        code2Class.put(new Integer(1), LDAPSDKCommunicationException.class.getName());
        code2Class.put(new Integer(51), LDAPSDKCommunicationException.class.getName());
        code2Class.put(new Integer(52), LDAPSDKCommunicationException.class.getName());
        code2Class.put(new Integer(53), LDAPSDKCommunicationException.class.getName());
        code2Class.put(new Integer(81), LDAPSDKCommunicationException.class.getName());
        code2Class.put(new Integer(91), LDAPSDKCommunicationException.class.getName());
        code2Class.put(new Integer(7), LDAPSDKAuthenticationMethodException.class.getName());
        code2Class.put(new Integer(8), LDAPSDKAuthenticationMethodException.class.getName());
        code2Class.put(new Integer(13), LDAPSDKAuthenticationMethodException.class.getName());
        code2Class.put(new Integer(14), LDAPSDKAuthenticationMethodException.class.getName());
        code2Class.put(new Integer(48), LDAPSDKAuthenticationMethodException.class.getName());
        code2Class.put(new Integer(49), LDAPSDKAccessDeniedException.class.getName());
        code2Class.put(new Integer(50), LDAPSDKAccessDeniedException.class.getName());
        code2Class.put(new Integer(3), LDAPSDKLimitsExceededException.class.getName());
        code2Class.put(new Integer(4), LDAPSDKLimitsExceededException.class.getName());
        code2Class.put(new Integer(11), LDAPSDKLimitsExceededException.class.getName());
        code2Class.put(new Integer(85), LDAPSDKLimitsExceededException.class.getName());
        code2Class.put(new Integer(17), LDAPSDKSchemaViolationException.class.getName());
        code2Class.put(new Integer(19), LDAPSDKSchemaViolationException.class.getName());
        code2Class.put(new Integer(20), LDAPSDKSchemaViolationException.class.getName());
        code2Class.put(new Integer(64), LDAPSDKSchemaViolationException.class.getName());
        code2Class.put(new Integer(65), LDAPSDKSchemaViolationException.class.getName());
        code2Class.put(new Integer(67), LDAPSDKSchemaViolationException.class.getName());
        code2Class.put(new Integer(68), LDAPSDKSchemaViolationException.class.getName());
        code2Class.put(new Integer(18), LDAPSDKInvalidFilterException.class.getName());
        code2Class.put(new Integer(16), LDAPSDKNoSuchAttributeException.class.getName());
        code2Class.put(new Integer(32), LDAPSDKNoSuchObjectException.class.getName());
        code2Class.put(new Integer(21), LDAPSDKInvalidAttributeSyntaxException.class.getName());
        code2Class.put(new Integer(34), LDAPSDKInvalidDNSyntaxException.class.getName());
        code2Class.put(new Integer(2), LDAPSDKCallErrorException.class.getName());
        code2Class.put(new Integer(89), LDAPSDKCallErrorException.class.getName());
        code2Class.put(new Integer(92), LDAPSDKCallErrorException.class.getName());
        code2Class.put(new Integer(35), LDAPSDKOperationNotAllowedException.class.getName());
        code2Class.put(new Integer(66), LDAPSDKOperationNotAllowedException.class.getName());
        code2Class.put(new Integer(80), LDAPSDKException.class.getName());
    }

    /**
     * Releases the internal <code>Map</code> mapping error codes to expected class names.
     *
     * @throws Exception
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        code2Class = null;
    }

    public void testConstructor() {
        LDAPSDKExceptionFactory factory = null;
        try {
            factory = new LDAPSDKExceptionFactory();
        } catch(Exception e) {
            fail("There shouldn't be any reasons preventing the successful instantiation of LDAPSDKExceptionFactory");
        }
    }

    /**
     * Tests the <code>createException(String, int)</code> method. The test iterates though the list of pre-defined
     * LDAP error codes and verifies that the tested method returns an instances of LDAPSDKException of type
     * corresponding to specified error code in accordance with design specification. Also this test verifies that the
     * factory produces an instance of LDAPSDKException for any error code that is not in the list of pre-defined LDAP
     * error codes. The test verifies that the exceptions produced by this method do not contain the "cause" specified.
     */
    public void testMethodCreateException_String_int() {
        Integer errorCode = null;
        String expectedExceptionClassName = null;
        String errorMessage = null;
        Object createdException = null;

        // Tests the pre-defined list of LDAP error codes
        Iterator iterator = code2Class.keySet().iterator();

        while (iterator.hasNext()) {
            errorCode = (Integer) iterator.next();
            expectedExceptionClassName = (String) code2Class.get(errorCode);
            errorMessage = "The pre-defined error (" + errorCode.intValue() + ")";
            createdException = LDAPSDKExceptionFactory.createException(errorMessage, errorCode.intValue());

            assertTrue("The method should return an instance of LDAPSDKException",
                    createdException instanceof LDAPSDKException);
            assertEquals("The method should return an instance of appropriate subclass of LDAPSDKException for "
                    + "each of pre-defined LDAP error codes", expectedExceptionClassName,
                    createdException.getClass().getName());
            assertNull("The cause of exception should not be specified",
                    ((LDAPSDKException) createdException).getCause());
        }

        // Tests the LDAP error codes from outside of pre-defined list of LDAP error codes
        createdException = LDAPSDKExceptionFactory.createException("The undefined error ", -1);
        expectedExceptionClassName = LDAPSDKException.class.getName();

        assertEquals("Should return the instance of LDAPSDKException for any non-pre-defined LDAP error code",
                expectedExceptionClassName, LDAPSDKException.class.getName());
        assertNull("The cause of exception should not be specified",
                ((LDAPSDKException) createdException).getCause());

        createdException = LDAPSDKExceptionFactory.createException("The undefined error ", 141);
        assertEquals("Should return the instance of LDAPSDKException for any non-pre-defined LDAP error code",
                expectedExceptionClassName, LDAPSDKException.class.getName());
        assertNull("The cause of exception should not be specified",
                ((LDAPSDKException) createdException).getCause());

        createdException = LDAPSDKExceptionFactory.createException("The undefined error ", 100);
        assertEquals("Should return the instance of LDAPSDKException for any non-pre-defined LDAP error code",
                expectedExceptionClassName, LDAPSDKException.class.getName());
        assertNull("The cause of exception should not be specified",
                ((LDAPSDKException) createdException).getCause());

        createdException = LDAPSDKExceptionFactory.createException("The undefined error ", 1000);
        assertEquals("Should return the instance of LDAPSDKException for any non-pre-defined LDAP error code",
                expectedExceptionClassName, LDAPSDKException.class.getName());
        assertNull("The cause of exception should not be specified",
                ((LDAPSDKException) createdException).getCause());
    }

    /**
     * Tests the <code>createException(String, int, Throwable)</code> method. The test iterates though the list of
     * pre-defined LDAP error codes and verifies that the tested method returns an instances of LDAPSDKException of type
     * corresponding to specified error code in accordance with design specification. Also this test verifies that the
     * factory produces an instance of LDAPSDKException for any error code that is not in the list of pre-defined LDAP
     * error codes. The test verifies that the exceptions produced by this method do contain the "cause" as specified
     * by the parameters.
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void testMethodCreateException_String_int_Throwable() throws ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        Integer errorCode = null;
        String expectedExceptionClassName = null;
        String errorMessage = null;
        Object createdException = null;
        Exception cause = null;

        // Tests the pre-defined list of LDAP error codes
        Iterator iterator = code2Class.keySet().iterator();

        while (iterator.hasNext()) {
            errorCode = (Integer) iterator.next();
            expectedExceptionClassName = (String) code2Class.get(errorCode);
            errorMessage = "The pre-defined error (" + errorCode.intValue() + ")";
            cause = new RuntimeException("The cause of LDAPSDKException with error code ; " + errorCode.intValue());
            createdException = LDAPSDKExceptionFactory.createException(errorMessage, errorCode.intValue(), cause);

            assertTrue("The method should return an instance of LDAPSDKException",
                    createdException instanceof LDAPSDKException);
            assertEquals("The method should return an instance of appropriate subclass of LDAPSDKException for "
                    + "each of pre-defined LDAP error codes", expectedExceptionClassName,
                    createdException.getClass().getName());
            assertNotNull("The cause of exception should not be specified",
                    ((LDAPSDKException) createdException).getCause());
            assertEquals("The cause of exception should be set as specified", cause,
                    ((LDAPSDKException) createdException).getCause());
        }

        // Tests the LDAP error codes from outside of pre-defined list of LDAP error codes
        cause = new RuntimeException("The cause of LDAPSDKException with error code ; " + errorCode.intValue());
        createdException = LDAPSDKExceptionFactory.createException("The undefined error ", -1, cause);
        expectedExceptionClassName = LDAPSDKException.class.getName();

        assertEquals("Should return the instance of LDAPSDKException for any non-pre-defined LDAP error code",
                expectedExceptionClassName, LDAPSDKException.class.getName());
        assertNotNull("The cause of exception should be specified",
                ((LDAPSDKException) createdException).getCause());
        assertEquals("The cause of exception should be set as specified", cause,
                ((LDAPSDKException) createdException).getCause());

        createdException = LDAPSDKExceptionFactory.createException("The undefined error ", 141, cause);
        assertEquals("Should return the instance of LDAPSDKException for any non-pre-defined LDAP error code",
                expectedExceptionClassName, LDAPSDKException.class.getName());
        assertNotNull("The cause of exception should be specified",
                ((LDAPSDKException) createdException).getCause());
        assertEquals("The cause of exception should be set as specified", cause,
                ((LDAPSDKException) createdException).getCause());

        createdException = LDAPSDKExceptionFactory.createException("The undefined error ", 100, cause);
        assertEquals("Should return the instance of LDAPSDKException for any non-pre-defined LDAP error code",
                expectedExceptionClassName, LDAPSDKException.class.getName());
        assertNotNull("The cause of exception should be specified",
                ((LDAPSDKException) createdException).getCause());
        assertEquals("The cause of exception should be set as specified", cause,
                ((LDAPSDKException) createdException).getCause());

        createdException = LDAPSDKExceptionFactory.createException("The undefined error ", 1000, cause);
        assertEquals("Should return the instance of LDAPSDKException for any non-pre-defined LDAP error code",
                expectedExceptionClassName, LDAPSDKException.class.getName());
        assertNotNull("The cause of exception should be specified",
                ((LDAPSDKException) createdException).getCause());
        assertEquals("The cause of exception should be set as specified", cause,
                ((LDAPSDKException) createdException).getCause());
    }


    public static Test suite() {
        return new TestSuite(LDAPSDKExceptionFactoryTest.class);
    }
}
