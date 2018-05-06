/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)StressTest.java
 */

/*   StressTest.java
 *
 *   A test class for stress testing of the Email Engine
 *     Enhancements componenent
 *
 *   @author Dan Pozdol
 *   @date 12/11/02
 *   @version 1.1
 */

package com.topcoder.message.email;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.config.ConfigManager;

public class StressTest extends TestCase {

    private String from = null;

    private String to = null;

    private String PROPERTIES_NAMESPACE = "EmailEngineTest";

    private String PROPERTIES_LOCATION = "com/topcoder/message/email/EmailEngineTest.xml";

    private String PROPERTIES_FORMAT = ConfigManager.CONFIG_XML_FORMAT;

    public void setUp() {
        if (from == null || to == null) {
            ConfigManager cm = ConfigManager.getInstance();

            try {
                if (cm.existsNamespace(PROPERTIES_NAMESPACE)) {
                    cm.refresh(PROPERTIES_NAMESPACE);
                } else {
                    cm.add(PROPERTIES_NAMESPACE, PROPERTIES_LOCATION, PROPERTIES_FORMAT);
                }

                if (!cm.existsNamespace(PROPERTIES_NAMESPACE)) {
                    throw new Exception();
                }
            } catch (Exception e) {
            }

            try {
                from = cm.getString(PROPERTIES_NAMESPACE, "from");
            } catch (Exception e) {
                e.printStackTrace();
                from = null;
            }

            try {
                to = cm.getString(PROPERTIES_NAMESPACE, "to");
            } catch (Exception e) {
                e.printStackTrace();
                to = null;
            }
        }
    }

    public static Test suite() {
        TestSuite testSuite = new TestSuite("StressTest");
        testSuite.addTestSuite(StressTest.class);
        return testSuite;
    }

    public StressTest(String name) {
        super(name);
    }

    public void testSendMultipleEmailsWithAttachments() throws Exception {
        TCSEmailMessage[] emails = new TCSEmailMessage[20];
        String subject = "Test - email quantity";
        String body = "this is test text.  this message should have one attachment.";
        String attString = "this is the attachment text.";

        for (int i = 0; i < 5; i++) {
            emails[i] = new TCSEmailMessage();
            emails[i].setFromAddress(from);
            emails[i].setToAddress(to, TCSEmailMessage.TO);
            emails[i].setSubject(subject);
            emails[i].setBody(body);
            InputStream oneIS = new ByteArrayInputStream(attString.getBytes());
            emails[i].addAttachment(oneIS, "the attachment");

            EmailEngine.send(emails[i]);
        }
    }

    public void testSendManyAttachmentsWithDifferentSizes() throws Exception {
        int numAtts = 200;
        TCSEmailMessage oneEmail = new TCSEmailMessage();
        String subject = "Test - vary number/size of attachments";
        String body = "this is test text.  this message should have " + numAtts + " attachments.";
        String attString = "this is attachment String ";
        for (int i = 0; i < 5; i++)
            attString += attString;

        oneEmail.setFromAddress(from);
        oneEmail.setToAddress(to, TCSEmailMessage.TO);
        oneEmail.setSubject(subject);
        oneEmail.setBody(body);

        for (int i = 0; i < numAtts; i++) {
            String oneAttachmentString = attString + i + ".";
            InputStream oneIS = new ByteArrayInputStream(oneAttachmentString.getBytes());
            oneEmail.addAttachment(oneIS, "attachment " + i);
        }

        EmailEngine.send(oneEmail);
    }

    public void testSendOneBigAttachment() throws Exception {
        TCSEmailMessage oneEmail = new TCSEmailMessage();
        String subject = "Test - huge attachment";
        String body = "this is test text.  this message should have one attachment.";
        String attString = "this is the attachment text.";
        for (int i = 0; i < 15; i++)
            attString += attString;

        oneEmail.setFromAddress(from);
        oneEmail.setToAddress(to, TCSEmailMessage.TO);
        oneEmail.setSubject(subject);
        oneEmail.setBody(body);
        InputStream oneIS = new ByteArrayInputStream(attString.getBytes());
        oneEmail.addAttachment(oneIS, "the attachment");

        EmailEngine.send(oneEmail);
    }
}
