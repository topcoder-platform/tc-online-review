/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.file.Template;
import com.topcoder.util.file.xslttemplate.XsltTemplate;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.EmailEventHandler;
import com.topcoder.util.scheduler.scheduling.EventHandler;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.JobType;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains unit tests for EmailEventHandler class.
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class FailureTestEmailEventHandler extends TestCase {
    /** the Log instance used to test. */
    private Log log = null;

    /** the Template instance used to test. */
    private Template template = null;

    /** the List instance used to test. */
    private List recipients = null;

    /** the Job instance used to test. */
    private Job job = null;

    /** the EventHandler instance used to test. */
    private EventHandler handler = null;

    /**
     * Set Up the test environment before testing.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        FailureTestHelper.loadXMLConfig();
        log = LogManager.getLog();
        template = new XsltTemplate();
        recipients = new ArrayList();
        recipients.add("tc@topcoder.com");

        job = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "dir");

        handler = new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info",
            log);
    }

    /**
     * Clean up the test environment after testing.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        FailureTestHelper.clearConfig();
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with null Template. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerNullTemplate() throws Exception {
        try {
            new EmailEventHandler(null, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", log);
            fail("testEmailEventHandlerNullTemplate is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with null Recipients. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerNullRecipients() throws Exception {
        try {
            new EmailEventHandler(template, null, EventHandler.FAILED, "coder@topcoder.com", "info", log);
            fail("testEmailEventHandlerNullRecipients is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with invalid Recipients. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerEmptyRecipients() throws Exception {
        recipients.clear();

        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", log);
            fail("testEmailEventHandlerEmptyRecipients is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with invalid Recipients. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerEmptyWithRecipients() throws Exception {
        recipients.add(" ");

        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", log);
            fail("testEmailEventHandlerEmptyWithRecipients is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with invalid Recipients. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerInvalidWithRecipients() throws Exception {
        recipients.add(new Object());

        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", log);
            fail("testEmailEventHandlerInvalidWithRecipients is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with null Event. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerNullEvent() throws Exception {
        try {
            new EmailEventHandler(template, recipients, null, "coder@topcoder.com", "info", log);
            fail("testEmailEventHandlerNullEvent is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with empty Event, IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerEmptyEvent() throws Exception {
        try {
            new EmailEventHandler(template, recipients, " ", "coder@topcoder.com", "info", log);
            fail("testEmailEventHandlerEmptyEvent is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with null EmailFromAddress. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerNullEmailFromAddress() throws Exception {
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, null, "info", log);
            fail("testEmailEventHandlerNullEmailFromAddress is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with empty EmailFromAddress, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerEmptyEmailFromAddress() throws Exception {
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, " ", "info", log);
            fail("testEmailEventHandlerEmptyEmailFromAddress is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with null EmailAlertSubject. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerNullEmailAlertSubject() throws Exception {
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", null, log);
            fail("testEmailEventHandlerNullEmailAlertSubject is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with empty EmailAlertSubject, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerEmptyEmailAlertSubject() throws Exception {
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", " ", log);
            fail("testEmailEventHandlerEmptyEmailAlertSubject is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>EmailEventHandler(Template template, List recipients, String event,
     * String emailFromAddress,String emailAlertSubject, Log log)</code>
     * method for failure with null Log. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testEmailEventHandlerNullLog() throws Exception {
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", null);
            fail("testEmailEventHandlerNullLog is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>handle(Job job, String event)</code> method for failure
     * with null Job. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testHandleNullJob() throws Exception {
        try {
            handler.handle(null, EventHandler.SUCCESSFUL);
            fail("testHandleNullJob is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>handle(Job job, String event)</code> method for failure
     * with null Event. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testHandleNullEvent() throws Exception {
        try {
            handler.handle(job, null);
            fail("testHandleNullEvent is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>handle(Job job, String event)</code> method for failure
     * with empty Event, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testHandleEmptyEvent() throws Exception {
        try {
            handler.handle(job, " ");
            fail("testHandleEmptyEvent is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>handle(Job job, String event)</code> method for failure
     * with invalid Event, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testHandleInvalidEvent() throws Exception {
        try {
            handler.handle(job, "invalid");
            fail("testHandleInvalidEvent is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }
}
