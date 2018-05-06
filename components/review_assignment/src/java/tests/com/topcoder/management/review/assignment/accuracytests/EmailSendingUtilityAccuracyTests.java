/**
 * Copyright (c) 2013, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.assignment.accuracytests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.topcoder.management.review.assignment.notification.EmailSendingUtility;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * Unit tests for {@link EmailSendingUtility} class. <br/>
 * </p>
 *
 * @author KennyAlive
 * @version 1.0
 */
public class EmailSendingUtilityAccuracyTests {
    /**
     * Constant for email sender address.
     */
    private static final String EMAIL_SENDER = "test@provider.com";

    /**
     * Constant for recipient email.
     */
    private static final String EMAIL_RECIPIENT = "recipient@provider2.com";

    /**
     * Constant for email template path.
     */
    private static final String BODY_TEMPLATE_PATH1 = "test_files/accuracy/Kenny/EmailTemplate1.txt";

    /**
     * Constant for email template path.
     */
    private static final String BODY_TEMPLATE_PATH2 = "test_files/accuracy/Kenny/EmailTemplate2.txt";

    /**
     * The {@code EmailSendingUtility} instance used for testing.
     */
    private EmailSendingUtility instance;

    /**
     * Creates a suite with all test methods for JUnix3.x runner.
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(EmailSendingUtilityAccuracyTests.class);
    }

    /**
     * Sets up the test environment.
     *
     * @throws Exception
     *             to JUnit
     */
    @Before
    public void setUp() throws Exception {
        instance = new EmailSendingUtility(EMAIL_SENDER, LogManager.getLog());
    }

    /**
     * Accuracy test constructor.
     */
    @Test
    public void test_constructor_EmailSendingUtility() {
        assertNotNull("The logger should not be null", Helper.getField(instance, "log"));
        assertNotNull("The email sender should not be null", Helper.getField(instance, "emailSender"));
        assertEquals("The email sender should be correct", EMAIL_SENDER, Helper.getField(instance, "emailSender"));
    }

    /**
     * Accuracy test for {@code sendEmail} method.
     * 
     * Expected behavior: no errors, check email on the server manually
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_sendEmail_1() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        instance.sendEmail("EmailSendingUtility Test", BODY_TEMPLATE_PATH1, EMAIL_RECIPIENT, params);
    }

    /**
     * Accuracy test for {@code sendEmail} method.
     * 
     * Expected behavior: no errors, check email on the server manually.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_sendEmail_2() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PARAM1", "firstParam");
        params.put("PARAM2", 1234);
        instance.sendEmail("EmailSendingUtility Test2", BODY_TEMPLATE_PATH2, EMAIL_RECIPIENT, params);
    }
}
