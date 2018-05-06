/*
 * Copyright (C) 2005-2008 TopCoder Inc., All Rights Reserved.
 *
 * @(#)TCSEmailMessageTest.java
 */
package com.topcoder.message.email;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.mail.Address;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * the junit for the TCSEmailMessage class.
 * @author TCSDEVELOPER
 * @version 3.1
 */
public class TCSEmailMessageTest extends TestCase {

    public static Test suite() {
        TestSuite testSuite = new TestSuite("TCSEmailMessageTest");
        testSuite.addTestSuite(TCSEmailMessageTest.class);
        return testSuite;
    }

    public TCSEmailMessageTest(String name) {
        super(name);
    }

    public void testSetAndGetSubject() throws Exception {
        String text1 = "bsaoseuthoasnut aos saoneusoan s";
        String text2 = ")(*&SRCXDHDNH  TSNUESU S S SNTH SNT(*&)(*Y";

        // if these are different, test does not make sense
        assertTrue(!text1.equals(text2));

        TCSEmailMessage oneEmail = new TCSEmailMessage();

        String test = null;

        // set the text
        oneEmail.setSubject(text1);
        test = oneEmail.getSubject();
        assertTrue(text1.equals(test));

        // change the text
        oneEmail.setSubject(text2);
        test = oneEmail.getSubject();
        assertTrue(text2.equals(test));

        // set the text to null
        oneEmail.setSubject(null);
        test = oneEmail.getSubject();
        assertTrue(test == null);
    }

    public void testSetAndGetBody() throws Exception {
        String text1 = "bsaoseuthoasnut aos saoneusoan s";
        String text2 = ")(*&SRCXDHDNH  TSNUESU S S SNTH SNT(*&)(*Y";

        // if these are different, test does not make sense
        assertTrue(!text1.equals(text2));

        TCSEmailMessage oneEmail = new TCSEmailMessage();

        String test = null;

        // set the text
        oneEmail.setBody(text1);
        test = oneEmail.getBody();
        assertTrue(text1.equals(test));

        // change the text
        oneEmail.setBody(text2);
        test = oneEmail.getBody();
        assertTrue(text2.equals(test));

        // set the text to null
        oneEmail.setBody(null);
        test = oneEmail.getBody();
        assertTrue(test == null);
    }

    public void testSetAndGetFrom() throws Exception {
        TCSEmailMessage oneEmail = new TCSEmailMessage();

        String address = "none@here.com";

        oneEmail.setFromAddress(address);
        Address testAddress = oneEmail.getFromAddress();
        String test = testAddress.toString();

        assertTrue(test != null);
        assertTrue(test.equals(address));
    }

    public void testSetFromError() throws Exception {
        TCSEmailMessage oneEmail = new TCSEmailMessage();

        String illegalAddress = "non!!$tnh3\n**";

        boolean caught = false;
        try {
            oneEmail.setFromAddress(illegalAddress);
        } catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);
    }

    public void testSetAndGetTo() throws Exception {
        TCSEmailMessage oneEmail = new TCSEmailMessage();

        String address = "none@here.com";

        oneEmail.setToAddress(address, TCSEmailMessage.TO);
        Address[] testArray = oneEmail.getToAddress(TCSEmailMessage.TO);
        assertTrue(testArray.length == 1);
        String test = testArray[0].toString();

        assertTrue(test != null);
        assertTrue(test.equals(address));
    }

    public void testSetToError() throws Exception {
        TCSEmailMessage oneEmail = new TCSEmailMessage();

        String illegalAddress = "non!!$tnh3\n**";

        boolean caught = false;
        try {
            oneEmail.setToAddress(illegalAddress, TCSEmailMessage.TO);
        } catch (Exception e) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     *  Unit tests for attachment functions.
     */
    public void testAttachFile() throws Exception {
        TCSEmailMessage oneEmail = new TCSEmailMessage();

        String name1 = "one";
        String name2 = "two";
        InputStream is1 = new ByteArrayInputStream(name1.getBytes());
        InputStream is2 = new ByteArrayInputStream(name2.getBytes());

        oneEmail.addAttachment(is1, name1);
        Map attachments = oneEmail.getAttachments();
        assertTrue(attachments != null);
        assertTrue(attachments.keySet().size() == 1);
        assertTrue(attachments.containsKey(is1));
        assertTrue(attachments.get(is1).equals(name1));

        oneEmail.addAttachment(is2, name2);
        attachments = oneEmail.getAttachments();
        assertTrue(attachments != null);
        assertTrue(attachments.keySet().size() == 2);
        assertTrue(attachments.containsKey(is1));
        assertTrue(attachments.get(is1).equals(name1));
        assertTrue(attachments.containsKey(is2));
        assertTrue(attachments.get(is2).equals(name2));

        oneEmail.clearAttachments();
        attachments = oneEmail.getAttachments();
        assertTrue(attachments == null || attachments.keySet().size() == 0);
    }
    /**
     * junit test for the ctor.
     */
    public void testCtor() {
        assertNotNull("the return should not be null", new TCSEmailMessage());
    }

    /**
     * junit test for the setContentType method
     */
    public void testSetContentType() {
        TCSEmailMessage oneEmail = new TCSEmailMessage();
        assertEquals("the return should be text/plain", "text/plain", oneEmail.getContentType());
        oneEmail.setContentType("text/html");
        assertEquals("the return should be text/html", "text/html", oneEmail.getContentType());
    }

    /**
     * junit test for the setContentType method
     */
    public void testSetContentTypeWithNull() {
        try {
            new TCSEmailMessage().setContentType(null);
            fail("IAE should be thrown");

        } catch (IllegalArgumentException e) {
            //success
        }

    }
    /**
     * junit test for the setContentType method
     */
    public void testSetContentTypeWithEmpty(){
        try {
            new TCSEmailMessage().setContentType("  ");
            fail("IAE should be thrown");

        } catch (IllegalArgumentException e) {
            //success
        }
        try {
            new TCSEmailMessage().setContentType("");
            fail("IAE should be thrown");

        } catch (IllegalArgumentException e) {
            //success
        }
    }
    /**
     * junit test for the getContentType method
     */
    public void testGetContentType(){
        TCSEmailMessage oneEmail = new TCSEmailMessage();
        assertEquals("the return should be text/plain","text/plain",oneEmail.getContentType());
        oneEmail.setContentType("text/html");
    }

}
