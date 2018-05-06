/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests.keepalive;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.processor.ipserver.keepalive.KeepAliveIPClient;
import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;
import com.topcoder.processor.ipserver.failuretests.FailureTestCase;

import java.io.IOException;

/**
 * <p>A failure test for <code>KeepAliveIPClient</code> class. Tests the proper behavior of the methods under
 * inappropriate use. Puts the tested instance into illegal state and expects the appropriate exception to be thrown.
 * </p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class KeepAliveIPClientFailureTest extends FailureTestCase {

    /**
     * <p>An <code>int</code> providing the sample valid delay to be used for testing.</p>
     */
    public static final int DELAY = 1000;

    /**
     * <p>An instance of <code>KeepAliveIPClient</code> which is tested.</p>
     */
    private KeepAliveIPClient testedInstance = null;

    /**
     * <p>Gets the test suite for <code>KeepAliveIPClient</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>KeepAliveIPClient</code> class.
     */
    public static Test suite() {
        return new TestSuite(KeepAliveIPClientFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        getServer().addHandler("failure_keep_alive", new KeepAliveHandler());
        startServer();

    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        stopServer();
        getServer().removeHandler("failure_keep_alive");
    }

    /**
     * <p>Tests the <code>connect()</code> method for proper handling invalid input data. Attempts to connect a
     * previously connected client to a server and expects the <code>IllegalStateException</code> to be thrown.</p>
     *
     * @throws Exception if a client can not connect to a server for the first time. Such an error should not be
     *         interpreted as test failure.
     */
    public void testConnect_1() throws Exception {
        testedInstance = new KeepAliveIPClient(getProperty("host"),
                                               Integer.parseInt(getProperty("server_port")),
                                               DELAY, MESSAGE_NAMESPACE);
        testedInstance.connect();   
        try {
            testedInstance.connect();
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        } finally {
            try {
                testedInstance.disconnect();
            } catch (Exception e) {}
        }
    }

    /**
     * <p>Tests the <code>connect()</code> method for proper handling invalid input data. Attempts to connect a client
     * to an unserviced port and expects the <code>IOException</code> to be thrown.</p>
     * @throws Exception to JUnit
     */
    public void testConnect_2() throws Exception {
        testedInstance = new KeepAliveIPClient(getProperty("host"),
                                               Integer.parseInt(getProperty("unserviced_port")),
                                               DELAY, MESSAGE_NAMESPACE);

        try {
            testedInstance.connect();
            fail("IOException should be thrown since a socket exception occurs while connecting to the server");
        } catch (IOException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IOException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>disconnect()</code> method for proper handling invalid input data. Attempts to disconnect a
     * client which is not connected to a server and expects the <code>IllegalStateException</code> to be thrown.</p>
     * @throws Exception to JUnit
     */
    public void testDisconnect_1() throws Exception {
        testedInstance = new KeepAliveIPClient(getProperty("host"),
                                               Integer.parseInt(getProperty("server_port")),
                                               DELAY, MESSAGE_NAMESPACE);

        try {
            testedInstance.disconnect();
            fail("IllegalStateException should be thrown since the client is not connected to the server");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }
}
