/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)AccuracyTests.java
 */
package com.topcoder.message.email;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.mail.Address;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p><code>AccuracyTests</code></p>
 *
 * @author  smell
 * @version 3.0
 */
public class AccuracyTests extends TestCase {

    /**
     * Does..
     *
     * @return suite
     */
    public static Test suite() {
        TestSuite testSuite = new TestSuite("AccuracyTests");
        testSuite.addTestSuite(AccuracyTests.class);
        return testSuite;
    }

    /**
     * <p>Creates..</p>
     *
     * @param name
     */
    public AccuracyTests(String name) {
        super(name);
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testGetName() throws Exception {

        /**
         * From UnitTests
         */
        String name = "warren";
        InputStream is1 = new ByteArrayInputStream(name.getBytes());

        EmailEngine.AttachmentDataSource ds = new EmailEngine.AttachmentDataSource(is1, name);

        assertEquals(name, ds.getName());

        /**
         * Added by QA
         */
        is1.reset();
        boolean err = false;
        try {
            ds = new EmailEngine.AttachmentDataSource(is1, null);
        } catch (Exception e) {
            err = true;
        }
        assertTrue(err);
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testGetInputStream() throws Exception {

        /**
         * From UnitTests
         */
        String name = "warren";
        InputStream is1 = new ByteArrayInputStream(name.getBytes());

        EmailEngine.AttachmentDataSource ds = new EmailEngine.AttachmentDataSource(is1, name);

        is1.reset();

        byte[] ba1 = new byte[ds.getInputStream().available()];
        byte[] ba2 = new byte[is1.available()];

        is1.read(ba1);
        ds.getInputStream().read(ba2);

        // compare the raw byte data of both input streams
        for (int i = 0; i < ba1.length && i < ba2.length; i++) {
            assertEquals(ba1[i], ba2[i]);
        }

        /**
         * Added by QA
         */
        boolean err = false;
        try {
            ds = new EmailEngine.AttachmentDataSource(null, name);
        } catch (Exception e) {
            err = true;
        }
        assertTrue(err);

        /**
         * Added by QA
         */
        err = false;
        try {
            ds = new EmailEngine.AttachmentDataSource(is1, name);
        } catch (Exception e) {
            err = true;
        }
        assertTrue(!err);
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testSetFromError() throws Exception {
        /**
         * From UnitTests
         */
        TCSEmailMessage oneEmail = new TCSEmailMessage();

        String illegalAddress = "non!!$tnh3\n**";

        boolean caught = false;
        try {
            oneEmail.setFromAddress(illegalAddress);
        } catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);

        /**
         * Added by QA
         */
        boolean err = false;
        try {
            oneEmail.setFromAddress(null);
        } catch (Exception e) {
            err = true;
        }
        assertTrue(err);
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testSetAndGetTo() throws Exception {
        TCSEmailMessage oneEmail = new TCSEmailMessage();

        /**
         * Added by QA
         */
        Address[] testArray = oneEmail.getToAddress(TCSEmailMessage.TO);
        assertEquals(0, testArray.length);
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testSetToError() throws Exception {
        /**
         * From UnitTests
         */
        TCSEmailMessage oneEmail = new TCSEmailMessage();

        String illegalAddress = "non!!$tnh3\n**";

        boolean caught = false;
        try {
            oneEmail.setToAddress(illegalAddress, TCSEmailMessage.TO);
        } catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);

        /**
         * Added by QA
         */
        caught = false;
        try {
            oneEmail.setToAddress(null, TCSEmailMessage.TO);
        } catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);

        /**
         * Added by QA
         */
        caught = false;
        try {
            oneEmail.setToAddress("valid@address.com",
                                  (TCSEmailMessage.TO | TCSEmailMessage.CC | TCSEmailMessage.BCC) + 1);
        } catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     *  Unit tests for attachment functions.
     *
     *  @throws Exception to JUnit
     */
    public void testAddandGetAttachments() throws Exception {
        /**
         * From UnitTests
         */
        TCSEmailMessage oneEmail = new TCSEmailMessage();

        String name1 = "one";
        InputStream is1 = new ByteArrayInputStream(name1.getBytes());

        oneEmail.addAttachment(is1, name1);

        Object[] attachments = oneEmail.getAttachments().keySet().toArray();

        assertTrue(attachments.length == 1 && attachments[0] == is1);

        /**
         * Added by QA
         */
        boolean caught = false;
        try {
            oneEmail.addAttachment(null, name1);
        } catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);

        /**
         * Added by QA
         */
        caught = false;
        try {
            oneEmail.addAttachment(is1, name1);
        } catch (Exception e) {
            caught = true;
        }
        assertTrue(!caught);

        /**
         * Added by QA
         */
        caught = false;
        is1.reset();
        try {
            oneEmail.addAttachment(is1, null);
        } catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);

        //nothing should have been read from is1
        InputStream is2 = new ByteArrayInputStream(name1.getBytes());
        byte[] b1 = new byte[is1.available()];
        byte[] b2 = new byte[is2.available()];
        assertEquals(b2.length, b1.length);
        is1.read(b1, 0, b1.length);
        is2.read(b2, 0, b2.length);
        for (int i = 0; i < Math.min(b2.length, b1.length); i++) {
            assertEquals(b2[i], b1[i]);
        }

        /**
         * Added by QA
         */
        caught = false;
        try {
            oneEmail.addAttachment(null, name1);
        } catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);
    }
}
