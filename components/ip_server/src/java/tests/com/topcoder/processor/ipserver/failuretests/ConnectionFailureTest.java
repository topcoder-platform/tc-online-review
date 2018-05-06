/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.processor.ipserver.IPServer;

import java.net.Socket;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>A failure test for <code>Connection</code> class. Tests the proper handling of invalid input data by the
 * constructor. Passes the invalid arguments to the constructor and expects the appropriate exception to be thrown.</p>
 *
 * <p>Uses <code>Java Reflection API</code> to locate the constructor provided by <code>Connection</code> class and
 * instantiate a <code>Connection</code> object. The test may fail if current security manager does not allow to change
 * the accessibility of a package private constructor provided by <code>Connection</code> class.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class ConnectionFailureTest extends FailureTestCase {

    /**
     * <p>A <code>String</code> providing a sample connection ID to be used for testing.</p>
     */
    public static final String ID = "01";

    /**
     * <p>An <code>IPServer</code> representing the sample  IP server to be used for testing.</p>
     */
    public static IPServer IP_SERVER; 

    /**
     * <p>A <code>Socket</code> representing a sample socket for conntection to be used  for testing.</p>
     */
    public static final Socket SOCKET = new Socket();

    static {
        try {
            loadNamespaces();
            IP_SERVER = new IPServer("127.0.0.1", 1024, 10, 10, "com.topcoder.processor.ipserver.message");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Gets the test suite for <code>Connection</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>Connection</code> class.
     */
    public static Test suite() {
        return new TestSuite(ConnectionFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        loadNamespaces();
    }

    /**
     * <p>Tests the <code>Connection(String, IPServer, Socket)</code> constructor for proper handling invalid input
     * data. Passes the <code>null</code> ID and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_IPServer_Socket_1() {
        try {
            create(null, IP_SERVER, SOCKET);
            fail("NullPointerException should be thrown in response to NULL ID");
        } catch (InvocationTargetException e) {
            assertTrue("NullPointerException should be thrown in response to NULL ID",
                e.getCause() instanceof NullPointerException);
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Connection(String, IPServer, Socket)</code> constructor for proper handling invalid input
     * data. Passes the <code>null</code> server and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_IPServer_Socket_2() {
        try {
            create(ID, null, SOCKET);
            fail("NullPointerException should be thrown in response to NULL server");
        } catch (InvocationTargetException e) {
            assertTrue("NullPointerException should be thrown in response to NULL server",
                e.getCause() instanceof NullPointerException);
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>Connection(String, IPServer, Socket)</code> constructor for proper handling invalid input
     * data. Passes the <code>null</code> socket and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_IPServer_Socket_3() {
        try {
            create(ID, IP_SERVER, null);
            fail("NullPointerException should be thrown in response to NULL socket");
        } catch (InvocationTargetException e) {
            assertTrue("NullPointerException should be thrown in response to NULL socket",
                e.getCause() instanceof NullPointerException);
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>A helper method to be used to instantiate the <code>Connection</code> object with specified parameters through
     * the <code>Java Reflection API</code>.</p>
     *
     * @param id a <code>String</code> ID to be passed to constructor.
     * @param server an <code>IPServer</code> to be passed to constructor.
     * @param socket a <code>Socket</code> to be passed to constructor.
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private void create(String id, IPServer server, Socket socket) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {

        Constructor constructor = getConnectionConstructor();
        boolean accessibility = constructor.isAccessible();
        constructor.setAccessible(true);

        Object[] parameters = new Object[] {id, server, socket};

        try {
            constructor.newInstance(parameters);
        } finally{
            try {
                // Restore the original accessibility of the constructor
                constructor.setAccessible(accessibility);
            } catch (Exception e) {
            }
        }
    }
}
