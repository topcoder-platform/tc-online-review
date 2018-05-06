/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.accuracytests.notification;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.review.assignment.accuracytests.TestDataFactory;
import com.topcoder.management.review.assignment.notification.EmailSendingUtility;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>A test case for {@link EmailSendingUtility} class.</p>
 * 
 * @author isv
 * @version 1.0
 */
public class EmailSendingUtilityAccuracyTest {

    /**
     * <p>A <code>EmailSendingUtility</code> instance to run the test against.</p>
     */
    private EmailSendingUtility testedInstance;

    /**
     * <p>Gets the test suite with the tests for this test case.</p>
     *
     * @return a <code>Test</code> providing the test suite for this test case.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(EmailSendingUtilityAccuracyTest.class);
    }

    /**
     * <p>Constructs new <code>EmailSendingUtilityAccuracyTest</code> instance. This implementation
     * does nothing.</p>
     */
    public EmailSendingUtilityAccuracyTest() {
    }

    /**
     * <p>Sets up the test environment for the test case.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Before
    public void setUp() throws Exception {
        ConfigurationObject notificationManagerConfig = TestDataFactory.getNotificationManagerConfig(false);
        String mailFrom = (String) notificationManagerConfig.getPropertyValue("emailSender");
        this.testedInstance = new EmailSendingUtility(mailFrom, null);
    }

    /**
     * <p>Tears down the environment for the test case.</p>
     */
    @After
    public void tearDown() {
        this.testedInstance = null;
    }

    /**
     * <p>Tests the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method for accuracy.</p>
     *
     * <p>Simply verifies that method executed with no exception. The correctness of sent email is to be verified 
     * manually by examining the mail server logs and intended mailbox.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    @Test
    public void testSendEmail() throws Exception {
        ConfigurationObject notificationManagerConfig = TestDataFactory.getNotificationManagerConfig(false);
        String mailTo = (String) notificationManagerConfig.getPropertyValue("sampleEmailRecipient");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("PROJECT_NAME", "Review Assignment ");
        params.put("PROJECT_VERSION", "1.0.0");
        params.put("PROJECT_ID", "9999999998");

        this.testedInstance.sendEmail("Sample email sent from accuracy test", 
                "test_files/accuracy/templates/sampleEmailTemplate.txt", mailTo, params);
    }
}
