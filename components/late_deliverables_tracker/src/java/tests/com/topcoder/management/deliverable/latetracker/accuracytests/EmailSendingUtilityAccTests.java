/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.accuracytests;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.management.deliverable.latetracker.EmailSendingUtility;


/**
 * Accuracy tests for EmailSendingUtility.
 * @author mumujava
 * @version 1.0
 */
public class EmailSendingUtilityAccTests extends AccuracyHelper {
    /** Represents the EmailSendingUtility instance to test. */
    private EmailSendingUtility instance;

    /**
     * <p>Sets up the unit tests.</p>
     */
    public void setUp() throws Exception {
        super.setUp();
        instance = new EmailSendingUtility("from@topcoder.com", com.topcoder.util.log.LogFactory.getLog());
    }

    /**
     * <p>Cleans up the unit tests.</p>
     * @throws Exception to junit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        instance = null;
    }

    /**
     * Accuracy test for method EmailSendingUtility.
     */
    public void test_EmailSendingUtility() {
        assertNotNull(instance);
    }

    /**
     * Accuracy test for method sendEmail.
     *
     * Manually check the mails.
     *
     *
     * @throws Exception to junit
     */
    public void test_sendEmail() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        //empty maps
        instance.sendEmail("subject", "test_files\\accuracy\\email_template.html", "to@topcoder.com", map);

        //Manually check the mails, please
    }

    /**
     * Accuracy test for method sendEmail.
     *
     * Manually check the mails.
     *
     *
     * @throws Exception to junit
     */
    public void test_sendEmail2() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("PROJECT_NAME", "Type safe enum");
        map.put("PROJECT_VERSION", "1.01");
        map.put("PROJECT_ID", "32135");
        map.put("PHASE_NAME", "Review");
        instance.sendEmail("subject", "test_files\\accuracy\\email_template2.html", "to@topcoder.com", map);

        //Manually check the mails, please
    }
}
