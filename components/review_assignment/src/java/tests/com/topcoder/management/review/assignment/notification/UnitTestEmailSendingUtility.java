/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.notification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.topcoder.management.review.assignment.BaseTestCase;
import com.topcoder.management.review.assignment.ReviewAssignmentNotificationException;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * Unit tests for <code>{@link EmailSendingUtility}</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 */
public class UnitTestEmailSendingUtility extends TestCase {
    /**
     * Represents the email body template file.
     */
    private static final String TEMPLATE_FILE = "test_files" + File.separator + "warn_email_template.html";

    /**
     * Represents the email subject template text.
     */
    private static final String EMAIL_SUBJECT = "WARNING\\: You are late when providing"
        + " a deliverable for %PROJECT_NAME%";

    /**
     * The <code>{@link EmailSendingUtility}</code> instance used for testing.
     */
    private EmailSendingUtility target;

    /**
     * The <code>Log</code> instance used for testing.
     */
    private Log log;

    /**
     * The parameters used for testing.
     */
    private Map<String, Object> params;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void setUp() throws Exception {
        super.setUp();

        log = LogManager.getLog("mylogger");
        params = new HashMap<String, Object>();
        params.put("DEADLINE", "2010-08-26 09:05:00");
        params.put("DELAY", "2 minutes");
        params.put("PROJECT_ID", "1001");
        params.put("PROJECT_NAME", "Sample Project");
        target = new EmailSendingUtility("service@topcoder.com", log);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * Accuracy test case for the {@link EmailSendingUtility#EmailSendingUtility()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor_1() throws Exception {
        assertEquals("emailSender field wrong", "service@topcoder.com",
            BaseTestCase.getField(target, "emailSender"));
        assertSame("log field wrong", log, BaseTestCase.getField(target, "log"));
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#EmailSendingUtility()} method.
     * </p>
     * <p>
     * The given <code>emailSender</code> is <code>null</code>, <code>IllegalArgumentException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor_2() throws Exception {
        try {
            new EmailSendingUtility(null, log);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#EmailSendingUtility()} method.
     * </p>
     * <p>
     * The given <code>emailSender</code> is empty, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor_3() throws Exception {
        try {
            new EmailSendingUtility("", log);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#EmailSendingUtility()} method.
     * </p>
     * <p>
     * The given <code>emailSender</code> is trimmed empty, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor_4() throws Exception {
        try {
            new EmailSendingUtility("   ", log);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * There will be email set to the test@topcoder.com.
     * Validation should be done by checking email box in smtp server.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_Acc() throws Exception {
        target.sendEmail(EMAIL_SUBJECT, TEMPLATE_FILE, "test@topcoder.com", params);

        // please manually check the email
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given subjectTemplateText is invalid, <code>ReviewAssignmentNotificationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_1() throws Exception {
        try {
            target.sendEmail("%Invadki%%", TEMPLATE_FILE, "test@topcoder.com", params);
            fail("should have thrown ReviewAssignmentNotificationException");
        } catch (ReviewAssignmentNotificationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given subjectTemplateText is <code>null</code>, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_2() throws Exception {
        try {
            target.sendEmail(null, TEMPLATE_FILE, "test@topcoder.com", params);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given bodyTemplatePath is <code>null</code>, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_3() throws Exception {
        try {
            target.sendEmail("subject", null, "test@topcoder.com", params);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given bodyTemplatePath is empty, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_4() throws Exception {
        try {
            target.sendEmail("subject", "", "test@topcoder.com", params);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given bodyTemplatePath is trimmed empty, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_5() throws Exception {
        try {
            target.sendEmail("subject", "   ", "test@topcoder.com", params);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given recipient is <code>null</code>, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_6() throws Exception {
        try {
            target.sendEmail("subject", "path", null, params);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given recipient is empty, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_7() throws Exception {
        try {
            target.sendEmail("subject", "path", "", params);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given recipient is trimmed empty, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_8() throws Exception {
        try {
            target.sendEmail("subject", "path", "   ", params);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given params is <code>null</code>, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_9() throws Exception {
        try {
            target.sendEmail("subject", "path", "test@topcoder", null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given params contains <code>null</code> key, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_10() throws Exception {
        params.put(null, "value");

        try {
            target.sendEmail("subject", "path", "test@topcoder", params);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given params contains empty key, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_11() throws Exception {
        params.put("", "value");

        try {
            target.sendEmail("subject", "path", "test@topcoder", params);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The given params contains <code>null</code> value, <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_12() throws Exception {
        params.put("key", null);

        try {
            target.sendEmail("subject", "path", "test@topcoder", params);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * Fails to parse the email subject template, <code>ReviewAssignmentNotificationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_13() throws Exception {
        try {
            target.sendEmail("subject:", TEMPLATE_FILE, "test@topcoder.com", params);
            fail("should have thrown ReviewAssignmentNotificationException");
        } catch (ReviewAssignmentNotificationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * Fails to read email body template, <code>ReviewAssignmentNotificationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_14() throws Exception {
        try {
            target.sendEmail("subject", "not_exist", "test@topcoder.com", params);
            fail("should have thrown ReviewAssignmentNotificationException");
        } catch (ReviewAssignmentNotificationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * Fails to parse email body template, <code>ReviewAssignmentNotificationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_15() throws Exception {
        try {
            target.sendEmail("subject", "test_files/invalid_template.html", "test@topcoder.com", params);
            fail("should have thrown ReviewAssignmentNotificationException");
        } catch (ReviewAssignmentNotificationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The sender address is invalid, <code>ReviewAssignmentNotificationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_16() throws Exception {
        try {
            new EmailSendingUtility("John Smith/City/COMP/@COMPUS", log).sendEmail("subject", TEMPLATE_FILE,
                "test@topcoder.com", params);
            fail("should have thrown ReviewAssignmentNotificationException");
        } catch (ReviewAssignmentNotificationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The recipient address is invalid, <code>ReviewAssignmentNotificationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_sendEmail_17() throws Exception {
        try {
            target.sendEmail("subject", TEMPLATE_FILE, "John Smith/City/COMP/@COMPUS", params);
            fail("should have thrown ReviewAssignmentNotificationException");
        } catch (ReviewAssignmentNotificationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link EmailSendingUtility#sendEmail(String, String, String, Map)} method.
     * </p>
     * <p>
     * The data is invalid, <code>ReviewAssignmentNotificationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     */
    public void test_sendEmail_18() throws Exception {
        params.put("RECORDS", new Object());

        try {
            target.sendEmail(EMAIL_SUBJECT, "test_files" + File.separator
                + "pm_notification_email_template.html", "test@topcoder.com", params);
            fail("should have thrown ReviewAssignmentNotificationException");
        } catch (ReviewAssignmentNotificationException e) {
            // pass
        }
    }
}
