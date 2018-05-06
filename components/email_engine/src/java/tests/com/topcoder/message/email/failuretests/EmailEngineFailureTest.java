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
 * Failure tests for the new version of EmailEngine.
 *
 * @author mgmg
 * @version 3.0
 */
public class EmailEngineFailureTest extends TestCase {
    /**
     * The namespace.
     */
    private final static String NAMESPACE = "com.topcoder.message.email.EmailEngine";

    /**
     * The email message.
     */
    private TCSEmailMessage message = null;

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
        return new TestSuite(EmailEngineFailureTest.class);
    }

    /**
     * Create the test email message.
     */
    public void setUp() throws Exception {
        FailureTests.cleanConfiguration();
        message = new TCSEmailMessage();
        valid_msg = new TCSEmailMessage();
        valid_msg.setFromAddress("mgmg@topcoder.com", "ran");
        valid_msg.setToAddress("target@topcoder.com", TCSEmailMessage.TO);
        valid_msg.addToAddress("target_bcc1@topcoder.com", TCSEmailMessage.BCC);
        valid_msg.addToAddress("target_bcc2@topcoder.com", TCSEmailMessage.BCC);
        valid_msg.setSubject("subject");
        valid_msg.setBody("body");
        valid_msg.setPriority(PriorityLevel.NORMAL);
    }

    /**
     * Clean the test environment.
     */
    public void tearDown() throws Exception {
        FailureTests.cleanConfiguration();
    }

    /**
     * Failure test for send.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testSend_NullParam() throws Exception {
        try {
            EmailEngine.send(null);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for send.
     * IllegalArgumentException should be thrown because there's no from address.
     */
    public void testSend_InvalidFrom() throws Exception {
        message.addToAddress("mgmg@topcoder.com", TCSEmailMessage.TO);

        try {
            EmailEngine.send(message);
            fail("IllegalArgumentException should be thrown because there's no from address.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for send.
     * IllegalArgumentException should be thrown because there's no to address.
     */
    public void testSend_InvalidTo() throws Exception {
        message.setFromAddress("mgmg@topcoder.com");

        try {
            EmailEngine.send(message);
            fail("IllegalArgumentException should be thrown because there's no to address.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for send.
     * SendingException should be thrown because of the invalid server information.
     */
    public void testSend_InvalidConfig() throws Exception {
        FailureTests.loadConfig(NAMESPACE, "failuretests/InvalidEmailEngine.xml");

        try {
            EmailEngine.send(valid_msg);
            fail("SendingException should be thrown because of the invalid server information.");
        } catch (SendingException e) {
            // success.
        }
    }
}
