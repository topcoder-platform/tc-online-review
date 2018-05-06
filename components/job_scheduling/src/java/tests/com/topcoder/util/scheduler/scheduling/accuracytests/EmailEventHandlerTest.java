/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.file.Template;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.EmailEventHandler;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;

/**
 * <p>
 * Accuracy tests of <code>{@link EmailEventHandler}</code> class.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class EmailEventHandlerTest extends TestCase {
    /**
     * <p>
     * Represents the recipient email address.
     * </p>
     * <p>
     * Note: Please change this to get the email to your mail box.
     * </p>
     */
    private static final String EMAIL_TESTER = "abc@topcoder.com";

    /**
     * <p>
     * Represents the from address used for testing.
     * </p>
     */
    private static final String FROM = "admin@topcoder.com";

    /**
     * <p>
     * Represents the subject used for testing.
     * </p>
     */
    private static final String SUBJECT = "accuracy notification";

    /**
     * <p>
     * Represents the <code>{@link EmailEventHandler}</code> instance for testing.
     * </p>
     */
    private EmailEventHandler emailEventHandler;

    /**
     * <p>
     * The email template used for testing.
     * </p>
     */
    private Template template;

    /**
     * <p>
     * The list of recipients to send the mail.
     * </p>
     */
    private List recipients;

    /**
     * <p>
     * The Log instance for testing.
     * </p>
     */
    private Log log;

    /**
     * <p>
     * The Job instance for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to junit.
     */
    protected void setUp() throws Exception {
        AccuracyHelper.loadConfig();
        template = AccuracyHelper.getEmailTemplate();
        recipients = new ArrayList();
        recipients.add(EMAIL_TESTER);
        log = LogManager.getLog();
        job = new Job("FOLDER_CREATION", JobType.JOB_TYPE_EXTERNAL, "mkdir");
        emailEventHandler =
            new EmailEventHandler(template, recipients, EventHandler.FAILED, FROM, SUBJECT, log);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     */
    protected void tearDown() {
        emailEventHandler = null;
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(EmailEventHandlerTest.class);
    }

    /**
     * <p>
     * Accuracy test for
     * <code>{@link EmailEventHandler#EmailEventHandler(Template, List, String, String, String, Log)}</code>.
     * </p>
     * <p>
     * Newly created instance should not be <code>null</code>.
     * </p>
     */
    public void testEmailEventHandler() {
        assertNotNull("failed to create the EmailEventHandler.", emailEventHandler);
    }

    /**
     * <p>
     * Accuracy test for
     * <code>{@link EmailEventHandler#handle(com.topcoder.util.scheduler.scheduling.Job, String)}</code>.
     * </p>
     * <p>
     * Check only one email is present (verify manually).
     * </p>
     */
    public void testHandle() {
        // send the mail
        emailEventHandler.handle(job, EmailEventHandler.FAILED);
        // don't send the mail
        emailEventHandler.handle(job, EmailEventHandler.SUCCESSFUL);
    }

    /**
     * <p>
     * Accuracy test for <code>{@link EmailEventHandler#getRecipients()}</code>.
     * </p>
     * <p>
     * Expect the same recipient which is set initially.
     * </p>
     */
    public void testGetRecipients() {
        assertEquals("failed to get the recipients", EMAIL_TESTER, (String) emailEventHandler.getRecipients()
            .get(0));
    }

    /**
     * <p>
     * Accuracy test for <code>{@link EmailEventHandler#getRequiredEvent()}</code>.
     * </p>
     * <p>
     * Expect the same event which is set initially.
     * </p>
     */
    public void testGetRequiredEvent() {
        assertEquals("failed to get the required event", EmailEventHandler.FAILED, emailEventHandler
            .getRequiredEvent());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link EmailEventHandler#getTemplate()}</code>.
     * </p>
     * <p>
     * Expect the same template which is set initially.
     * </p>
     */
    public void testGetTemplate() {
        assertEquals("failed to get the template", template, emailEventHandler.getTemplate());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link EmailEventHandler#getEmailFromAddress()}</code>.
     * </p>
     * <p>
     * Expect the same FROM which is set initially.
     * </p>
     */
    public void testGetEmailFromAddress() {
        assertEquals("failed to get the from email address", FROM, emailEventHandler.getEmailFromAddress());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link EmailEventHandler#getEmailAlertSubject()}</code>.
     * </p>
     * <p>
     * Expect the same SUBJECT which is set initially.
     * </p>
     */
    public void testGetEmailAlertSubject() {
        assertEquals("failed to get the email subject", SUBJECT, emailEventHandler.getEmailAlertSubject());
    }

}
