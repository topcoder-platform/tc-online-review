/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)EmailEngineTest.java
 */
package com.topcoder.message.email;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.config.ConfigManager;

public class EmailEngineTest extends TestCase {

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
        TestSuite testSuite = new TestSuite("EmailEngineTest");
        testSuite.addTestSuite(EmailEngineTest.class);
        return testSuite;
    }

    public EmailEngineTest(String name) {
        super(name);
    }

    public void testSend() throws Exception {
        TCSEmailMessage oneEmail = new TCSEmailMessage();
        String subject = "TCS Email Engine Test Message";
        String body = "this is test text.  this message should not have any attachments.";

        oneEmail.setFromAddress(from);
        oneEmail.setToAddress(to, TCSEmailMessage.TO);
        oneEmail.setSubject(subject);
        oneEmail.setBody(body);

        EmailEngine.send(oneEmail);

        // no asserts here.  the recipient account should be checked manually.
    }

    public void testSendWithOneAttachment() throws Exception {
        TCSEmailMessage oneEmail = new TCSEmailMessage();
        String subject = "TCS Email Engine Test Message - one attachment";
        String body = "this is test text.  this message should have one attachment.";
        String attString = "this is the attachment text.";

        oneEmail.setFromAddress(from);
        oneEmail.setToAddress(to, TCSEmailMessage.TO);
        oneEmail.setSubject(subject);
        oneEmail.setBody(body);

        InputStream oneIS = new ByteArrayInputStream(attString.getBytes());
        oneEmail.addAttachment(oneIS, "the attachment");

        EmailEngine.send(oneEmail);

        // no asserts here.  the recipient account should be checked manually.
    }

    public void testSendWithManyAttachments() throws Exception {
        int numAtts = 20;
        TCSEmailMessage oneEmail = new TCSEmailMessage();
        String subject = "TCS Email Engine Test Message - " + numAtts + " attachments";
        String body = "this is test text.  this message should have " + numAtts + " attachments.";
        String attString = "this is attachment String ";

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

        // no asserts here.  the recipient account should be checked manually.
    }

    public void testSendWithImageAttachment() throws Exception {

        String imageFileName = "test_files/tcs_logo.gif";

        TCSEmailMessage oneEmail = new TCSEmailMessage();
        String subject = "TCS Email Engine Test Message - image attachment";
        String body = "this is test text.  this message should have an image attachment.";
        String attString = "this is attachment String ";

        oneEmail.setFromAddress(from);
        oneEmail.setToAddress(to, TCSEmailMessage.TO);
        oneEmail.setSubject(subject);
        oneEmail.setBody(body);

        InputStream oneIS = new FileInputStream(imageFileName);
        oneEmail.addAttachment(oneIS, imageFileName);

        EmailEngine.send(oneEmail);

        // no asserts here.  the recipient account should be checked manually.
    }

    public void testSendWithDocumentAttachment() throws Exception {

        String documentFileName = "docs/Email_Engine_Component_Specification.pdf";

        TCSEmailMessage oneEmail = new TCSEmailMessage();
        String subject = "TCS Email Engine Test Message - document attachment";
        String body = "this is test text.  this message should have a document attachment.";
        String attString = "this is attachment String ";

        oneEmail.setFromAddress(from);
        oneEmail.setToAddress(to, TCSEmailMessage.TO);
        oneEmail.setSubject(subject);
        oneEmail.setBody(body);

        InputStream oneIS = new FileInputStream(documentFileName);
        oneEmail.addAttachment(oneIS, documentFileName);

        EmailEngine.send(oneEmail);

        // no asserts here.  the recipient account should be checked manually.
    }

    public void testSendWithLargeAttachment() throws Exception {

        String largeFileName = "docs/Email_Engine_Requirements_Specification.rtf";

        TCSEmailMessage oneEmail = new TCSEmailMessage();
        String subject = "TCS Email Engine Test Message - large attachment";
        String body = "this is test text.  this message should have a large attachment.";
        String attString = "this is attachment String ";

        oneEmail.setFromAddress(from);
        oneEmail.setToAddress(to, TCSEmailMessage.TO);
        oneEmail.setSubject(subject);
        oneEmail.setBody(body);

        InputStream oneIS = new FileInputStream(largeFileName);
        oneEmail.addAttachment(oneIS, largeFileName);

        EmailEngine.send(oneEmail);

        // no asserts here.  the recipient account should be checked manually.
    }

    public void testSendWithZipAttachment() throws Exception {

        String zipFileName = "docs/Email_Engine.zargo";

        TCSEmailMessage oneEmail = new TCSEmailMessage();
        String subject = "TCS Email Engine Test Message - zargo attachment";
        String body = "this is test text.  this message should have a zargo attachment.";
        String attString = "this is attachment String ";

        oneEmail.setFromAddress(from);
        oneEmail.setToAddress(to, TCSEmailMessage.TO);
        oneEmail.setSubject(subject);
        oneEmail.setBody(body);

        InputStream oneIS = new FileInputStream(zipFileName);
        oneEmail.addAttachment(oneIS, zipFileName);

        EmailEngine.send(oneEmail);

        // no asserts here.  the recipient account should be checked manually.
    }
}
