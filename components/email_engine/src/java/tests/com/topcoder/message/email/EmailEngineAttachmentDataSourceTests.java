/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)EmailEngineAttachmentDataSourceTests.java
 */
package com.topcoder.message.email;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p><code>EmailEngineAttachmentDataSourceTests</code></p>
 *
 * @author  smell
 * @version 3.0
 */
public class EmailEngineAttachmentDataSourceTests extends TestCase {

    /** Represents..  */
    final static String imageFileName = "test_files/tcs_logo.gif";
    /** Represents..  */
    final static String pdfFileName = "docs/Email_Engine_Component_Specification.pdf";
    /** Represents..  */
    final static String largeFileName = "docs/Email_Engine_Requirements_Specification.rtf";
    /** Represents..  */
    final static String zipFileName = "test_files/emailenginetest.jar";
    /** Represents..  */
    final static String name = "test_files/tcs_logo.gif";
    /**
     * Does..
     *
     * @return suite
     */
    public static Test suite() {
        TestSuite testSuite = new TestSuite("EmailEngineAttachmentDataSourceTests");
        testSuite.addTestSuite(EmailEngineAttachmentDataSourceTests.class);
        return testSuite;
    }

    /**
     * <p>Creates..</p>
     *
     * @param name
     */
    public EmailEngineAttachmentDataSourceTests(String name) {
        super(name);
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testGetName() throws Exception {
        String name = "empty";
        InputStream is1 = new ByteArrayInputStream(name.getBytes());

        EmailEngine.AttachmentDataSource ds = new EmailEngine.AttachmentDataSource(is1, name);

        assertEquals(name, ds.getName());
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testGetInputStream() throws Exception {
        String name = "empty";
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

    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testGetInputStreamImageFile() throws Exception {

        InputStream is1 = new FileInputStream(imageFileName);

        EmailEngine.AttachmentDataSource ds = new EmailEngine.AttachmentDataSource(is1, imageFileName);

        is1.close();
        is1 = new FileInputStream(imageFileName);

        DataInputStream is2 = new DataInputStream(is1);

        byte[] ba1 = new byte[ds.getInputStream().available()];
        byte[] ba2 = new byte[is2.available()];

        is2.readFully(ba1);
        new DataInputStream(ds.getInputStream()).readFully(ba2);

        // compare the raw byte data of both input streams
        for (int i = 0; i < ba1.length && i < ba2.length; i++) {
            assertEquals(ba1[i], ba2[i]);
        }

        is1.close();
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testGetInputStreamDocumentFile() throws Exception {

        InputStream is1 = new FileInputStream(pdfFileName);

        EmailEngine.AttachmentDataSource ds = new EmailEngine.AttachmentDataSource(is1, pdfFileName);

        is1.close();
        is1 = new FileInputStream(pdfFileName);

        DataInputStream is2 = new DataInputStream(is1);

        byte[] ba1 = new byte[ds.getInputStream().available()];
        byte[] ba2 = new byte[is2.available()];

        is2.readFully(ba1);
        new DataInputStream(ds.getInputStream()).readFully(ba2);

        // compare the raw byte data of both input streams
        for (int i = 0; i < ba1.length && i < ba2.length; i++) {
            assertEquals(ba1[i], ba2[i]);
        }
        is1.close();
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testGetInputStreamLargeFile() throws Exception {

        InputStream is1 = new FileInputStream(largeFileName);

        EmailEngine.AttachmentDataSource ds = new EmailEngine.AttachmentDataSource(is1, largeFileName);

        is1.close();
        is1 = new FileInputStream(largeFileName);

        DataInputStream is2 = new DataInputStream(is1);

        byte[] ba1 = new byte[ds.getInputStream().available()];
        byte[] ba2 = new byte[is2.available()];

        is2.readFully(ba1);
        new DataInputStream(ds.getInputStream()).readFully(ba2);

        // compare the raw byte data of both input streams
        for (int i = 0; i < ba1.length && i < ba2.length; i++) {
            assertEquals(ba1[i], ba2[i]);
        }
        is1.close();
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testGetInputStreamZipFile() throws Exception {

        InputStream is1 = new FileInputStream(zipFileName);

        EmailEngine.AttachmentDataSource ds = new EmailEngine.AttachmentDataSource(is1, zipFileName);

        is1.close();
        is1 = new FileInputStream(zipFileName);

        DataInputStream is2 = new DataInputStream(is1);

        byte[] ba1 = new byte[ds.getInputStream().available()];
        byte[] ba2 = new byte[is2.available()];

        is2.readFully(ba1);
        new DataInputStream(ds.getInputStream()).readFully(ba2);

        // compare the raw byte data of both input streams
        for (int i = 0; i < ba1.length && i < ba2.length; i++) {
            assertEquals(ba1[i], ba2[i]);
        }
        is1.close();
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testGetOutputStream() throws Exception {
        String name = "empty";
        InputStream is1 = new ByteArrayInputStream(name.getBytes());

        EmailEngine.AttachmentDataSource ds = new EmailEngine.AttachmentDataSource(is1, name);

        boolean err = false;
        try {
            ds.getOutputStream();
        } catch (IOException ioe) {
            err = true;
        }

        assertTrue(err);
    }

    /**
     * Does..
     *
     * @throws Exception to JUnit
     */
    public void testGetContentType() throws Exception {
        InputStream is1 = new ByteArrayInputStream(name.getBytes());

        EmailEngine.AttachmentDataSource ds = new EmailEngine.AttachmentDataSource(is1, name);

        assertEquals("image/gif", ds.getContentType());
    }
}
