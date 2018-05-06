/*
 * Copyright (c) 2005-2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.message.email.stresstests;

import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.PriorityLevel;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.Date;

/**
 * Stress tests for EmailEngine of this component.
 *
 * @author zjq, TCSDEVELOPER
 * @version 3.2
 */
public class EmailEngineStressTest extends TestCase {
    /** from email address */
    private String from = "from@topcoder.com";

    /** to email address */
    private String to = "to@topcoder.com";

    /** The test count. */
    private int testCount = 5;

    /** time started to test */
    private long start = 0;

    /** TCSEmailMessage instance for test */
    private TCSEmailMessage email = null;

    /**
     * Initialize variables.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    public void setUp() throws Exception {
        addNamespace("EmailEngineTest", "com/topcoder/message/email/EmailEngineTest.xml",
            ConfigManager.CONFIG_XML_FORMAT);

        // init
        email = new TCSEmailMessage();

        TCSEmailMessage oneEmail = new TCSEmailMessage();
        String subject = "Email subject for stress test";
        String body = "Email body text for test.\n";

        email.setFromAddress(from);
        email.setToAddress(to, TCSEmailMessage.TO);
        email.setSubject(subject);
        email.setBody(body);

        start = new Date().getTime();
    }

    /**
     * Test to send an email without attachment.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    public void testSendNoAttachment() {
        email.setBody(email.getBody() + "No Attchment.");
        email.setPriority(PriorityLevel.HIGHEST);

        for (int i = 0; i < testCount; i++) {
            try {
                EmailEngine.send(email);
            } catch (Exception e) {
                fail("should not throw any execption.");
            }
        }

        System.out.println("Run testSendNoAttachment for " + testCount
                + " times takes " + (new Date().getTime() - start) + "ms");

        // no assert here, no exception have been thrown will be ok
    }

    /**
     * Test to send an email with a byte-type attachment.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    public void testSendOneAttachment() {
        email.setBody(email.getBody() + "One Attchment.");
        email.addAttachment(new ByteArrayInputStream("Byte attachment"
                .getBytes()), "attachment");
        email.setPriority(PriorityLevel.HIGH);

        for (int i = 0; i < testCount; i++) {
            try {
                EmailEngine.send(email);
            } catch (Exception e) {
                fail("should not throw any execption.");
            }
        }

        System.out.println("Run testSendOneAttachment for " + testCount
                + " times takes " + (new Date().getTime() - start) + "ms");

        // no assert here, no exception have been thrown will be ok
    }

    /**
     * Test to send an email with multiple byte-type attachments.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    public void testSendMulipleAttachment() {
        email.setBody(email.getBody() + "Multiple Attchment.");
        email.setPriority(PriorityLevel.LOW);

        // 100 attachments
        for (int i = 0; i < 100; ++i) {
            email.addAttachment(new ByteArrayInputStream(
                    ("Byte attachment " + i).getBytes()), "attachment no." + i);
        }

        for (int i = 0; i < testCount; i++) {
            try {
                EmailEngine.send(email);
            } catch (Exception e) {
                fail("should not throw any execption.");
            }
        }

        System.out.println("Run testSendMulipleAttachment for " + testCount
                + " times takes " + (new Date().getTime() - start) + "ms");

        // no assert here, no exception have been thrown will be ok
    }

    /**
     * Test to send an email with large file attachments.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    public void testSendLargeAttachment() {
        email.setBody(email.getBody() + "Large Attchment.");

        try {
            email.addAttachment(new FileInputStream(
                    "test_files/stresstests/largefile.txt"), "large file");
        } catch (FileNotFoundException e) {
            fail("You should put largefile.txt in test_files/stresstests/");
        }

        email.setPriority(PriorityLevel.LOWEST);

        for (int i = 0; i < testCount; i++) {
            try {
                EmailEngine.send(email);
            } catch (Exception e) {
                fail("should not throw any execption.");
            }
        }

        System.out.println("Run testSendLargeAttachment for " + testCount
                + " times takes " + (new Date().getTime() - start) + "ms");

        // no assert here, no exception have been thrown will be ok
    }

    /**
     * Tests send() with message using a configuration file with missing
     * parameters values.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testSendNoAuth() throws Exception {
        String namespace = "com.topcoder.message.email.EmailEngine";
        String file = "stresstests/NoAuth.xml";
        addNamespace(namespace, file, ConfigManager.CONFIG_XML_FORMAT);

        try {
            email.addAttachment(new FileInputStream(
                    "test_files/stresstests/largefile.txt"), "large file");
        } catch (FileNotFoundException e) {
            fail("You should put largefile.txt in test_files/stresstests/");
        }

        email.setPriority(PriorityLevel.LOWEST);

        for (int i = 0; i < testCount; i++) {
            try {
                EmailEngine.send(email);
            } catch (Exception e) {
                fail("should not throw any execption.");
            }
        }

        System.out.println("Run testSendNoAuth for " + testCount
                + " times takes " + (new Date().getTime() - start) + "ms");

        // no asserts here. the recipient account should be checked manually.
    }
    /**
     * Adds the configuration namespace.
     *
     * @param namespace
     *            the namespace to add
     * @param file
     *            the configuration file
     * @param format
     *            the file format
     * @throws Exception
     *             to JUnit
     */
    private void addNamespace(String namespace, String file, String format) throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        removeNamespace(namespace);
        configManager.add(namespace, file, format);
    }

    /**
     * Removes the namespace.
     *
     * @param namespace
     *            the namespace to remove.
     * @throws Exception
     *             to JUnit
     */
    private void removeNamespace(String namespace) throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        if (configManager.existsNamespace(namespace)) {
            configManager.removeNamespace(namespace);
        }
    }
}