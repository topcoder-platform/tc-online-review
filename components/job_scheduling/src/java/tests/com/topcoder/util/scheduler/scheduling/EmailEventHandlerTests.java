/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.topcoder.util.file.Template;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for EmailEventHandler.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class EmailEventHandlerTests extends TestCase {
    /**
     * <p>
     * The EmailEventHandler instance for testing.
     * </p>
     */
    private EmailEventHandler handler;

    /**
     * <p>
     * The recipients list for testing.
     * </p>
     */
    private List recipients;

    /**
     * <p>
     * The Template instance for testing.
     * </p>
     */
    private Template template;

    /**
     * <p>
     * The Job instance for testing.
     * </p>
     */
    private Job job;

    /**
     * <p>
     * The Log instance for testing.
     * </p>
     */
    private Log log;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    protected void setUp() throws Exception {
        TestHelper.loadSingleXMLConfig(TestHelper.LOG_NAMESPACE, TestHelper.LOG_CONFIGFILE);

        log = LogManager.getLog();
        template = TestHelper.readFileToTemplate("test_files" + File.separator + "sample_template.txt");
        recipients = new ArrayList();
        recipients.add("abc@topcoder.com");

        job = new Job("jobName", JobType.JOB_TYPE_EXTERNAL, "dir");

        handler = new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", log);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to JUnit
     *
     */
    protected void tearDown() throws Exception {
        handler = null;
        job = null;
        recipients = null;
        template = null;
        log = null;

        TestHelper.clearConfigFile(TestHelper.LOG_NAMESPACE);
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(EmailEventHandlerTests.class);
    }

    /**
     * <p>
     * Tests ctor EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created EmailEventHandler instance should not be null.
     * </p>
     */
    public void testCtor() {
        assertNotNull("Failed to create a new EmailEventHandler instance.", handler);
    }

    /**
     * <p>
     * Tests ctor EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when template is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullTemplate() {
        try {
            new EmailEventHandler(null, recipients, EventHandler.SUCCESSFUL, "coder@topcoder.com", "info", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when recipients is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullRecipients() {
        try {
            new EmailEventHandler(template, null, EventHandler.FAILED, "coder@topcoder.com", "info", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when recipients is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyRecipients() {
        recipients = new ArrayList();
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when recipients contains null element and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullInRecipients() {
        recipients.add(null);
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when recipients contains null element and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyInRecipients() {
        recipients.add(" ");
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when recipients contains element which is not of string type and expects
     * IllegalArgumentException.
     * </p>
     */
    public void testCtor_InvalidTypeInRecipients() {
        recipients.add(new Integer(8));
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullEvent() {
        try {
            new EmailEventHandler(template, recipients, null, "coder@topcoder.com", "info", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyEvent() {
        try {
            new EmailEventHandler(template, recipients, " ", "coder@topcoder.com", "info", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when emailFromAddress is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullEmailFromAddress() {
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, null, "info", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when emailFromAddress is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyEmailFromAddress() {
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, " ", "info", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when emailAlertSubject is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullEmailAlertSubject() {
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", null, log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when emailAlertSubject is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_EmptyEmailAlertSubject() {
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", " ", log);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests ctor EmailEventHandler#EmailEventHandler(Template,List,String,String,String,Log) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when log is null and expects IllegalArgumentException.
     * </p>
     */
    public void testCtor_NullLog() {
        try {
            new EmailEventHandler(template, recipients, EventHandler.FAILED, "coder@topcoder.com", "info", null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests EmailEventHandler#getTemplate() for accuracy.
     * </p>
     *
     * <p>
     * It verifies EmailEventHandler#getTemplate() is correct.
     * </p>
     */
    public void testGetTemplate() {
        assertEquals("Failed to get the template correctly.", template, handler.getTemplate());
    }

    /**
     * <p>
     * Tests EmailEventHandler#getRecipients() for accuracy.
     * </p>
     *
     * <p>
     * It verifies EmailEventHandler#getRecipients() is correct.
     * </p>
     */
    public void testGetRecipients() {
        assertEquals("Failed to get the recipients correctly.", "abc@topcoder.com", handler.getRecipients().get(0));
    }

    /**
     * <p>
     * Tests EmailEventHandler#getRequiredEvent() for accuracy.
     * </p>
     *
     * <p>
     * It verifies EmailEventHandler#getRequiredEvent() is correct.
     * </p>
     */
    public void testGetRequiredEvent() {
        assertEquals("Failed to get the event correctly.", EventHandler.FAILED, handler.getRequiredEvent());
    }

    /**
     * <p>
     * Tests EmailEventHandler#getEmailFromAddress() for accuracy.
     * </p>
     *
     * <p>
     * It verifies EmailEventHandler#getEmailFromAddress() is correct.
     * </p>
     */
    public void testGetEmailFromAddress() {
        assertEquals("Failed to get the address correctly.", "coder@topcoder.com", handler.getEmailFromAddress());
    }

    /**
     * <p>
     * Tests EmailEventHandler#getEmailAlertSubject() for accuracy.
     * </p>
     *
     * <p>
     * It verifies EmailEventHandler#getEmailAlertSubject() is correct.
     * </p>
     */
    public void testGetEmailAlertSubject() {
        assertEquals("Failed to get the subject correctly.", "info", handler.getEmailAlertSubject());
    }

    /**
     * <p>
     * Tests EmailEventHandler#handle(Job,String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies EmailEventHandler#handle(Job,String) is correct.
     * </p>
     */
    public void testHandle() {
        handler.handle(job, EventHandler.FAILED);
    }

    /**
     * <p>
     * Tests EmailEventHandler#handle(Job,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when job is null and expects IllegalArgumentException.
     * </p>
     */
    public void testHandle_NullJob() {
        try {
            handler.handle(null, EventHandler.FAILED);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests EmailEventHandler#handle(Job,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is null and expects IllegalArgumentException.
     * </p>
     */
    public void testHandle_NullEvent() {
        try {
            handler.handle(job, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests EmailEventHandler#handle(Job,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is empty and expects IllegalArgumentException.
     * </p>
     */
    public void testHandle_EmptyEvent() {
        try {
            handler.handle(null, " ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

    /**
     * <p>
     * Tests EmailEventHandler#handle(Job,String) for failure.
     * </p>
     *
     * <p>
     * It tests the case that when event is invalid and expects IllegalArgumentException.
     * </p>
     */
    public void testHandle_InvalidEvent() {
        try {
            handler.handle(null, "invalid");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            //good
        }
    }

}