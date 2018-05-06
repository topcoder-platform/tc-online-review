/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email.failuretests;

import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.PriorityLevel;
import com.topcoder.message.email.SendingException;
import com.topcoder.message.email.TCSEmailMessage;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Failure tests for EmailEngine of version 3.2.
 *
 * @author mumujava
 * @version 3.2
 */
public class EmailEngineV32FailureTest extends TestCase {
    /**
     * The namespace.
     */
    private final static String NAMESPACE = "com.topcoder.message.email.EmailEngine";

    /**
     * The email message.
     */
    private TCSEmailMessage valid_msg = null;

    /**
     * Aggregate all the tests.
     *
     * @return
     */
    public static Test suite() {
        return new TestSuite(EmailEngineV32FailureTest.class);
    }

    /**
     * Create the test email message.
     */
    public void setUp() throws Exception {
        FailureTests.cleanConfiguration();
        valid_msg = new TCSEmailMessage();
        valid_msg.setFromAddress("from@topcoder.com", "ran");
        valid_msg.setToAddress("to@topcoder.com", TCSEmailMessage.TO);
        valid_msg.setSubject("subject");
        valid_msg.setBody("body");
    }

    /**
     * Clean the test environment.
     */
    public void tearDown() throws Exception {
        FailureTests.cleanConfiguration();
    }

    /**
     * Failure test for send.
     * SendingException should be thrown because of the invalid server information.
     */
    public void testSend_InvalidConfig() throws Exception {
        FailureTests.loadConfig(NAMESPACE, "failuretests/WrongPassword.xml");

        try {
            EmailEngine.send(valid_msg);
            fail("SendingException should be thrown because of the invalid password.");
        } catch (SendingException e) {
            // success.
        	e.printStackTrace();
        }
    }
}
