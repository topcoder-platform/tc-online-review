/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;

/**
 * @author AdamSelene This class tests FileTemplateSource for proper failure.
 */
public class FileTemplateSourceTest extends TestCase {

    /**
     * ConfigurationObject used in the test.
     */
    private ConfigurationObject config;

    /**
     * Sets up test environment.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        config = FailureTestHelper.createConfigurationObject("failure/DocumentManager.xml", "myconfig");
        super.setUp();
    }

    /**
     * Creates a attachment point for this testcase.
     *
     * @return a wonderful testsuite for this case.
     */
    public static Test suite() {
        return new TestSuite(FileTemplateSourceTest.class);
    }

    /**
     * Tests null parameter to constructor.
     */
    public void testConstructor_1() {
        try {
            FileTemplateSource fts = new FileTemplateSource(null, config);
            fail("Did not fail on null namespace.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Incorrect exception thrown.");
        }
    }

    /**
     * Tests null parameter to constructor.
     */
    public void testConstructor_3() {
        try {
            FileTemplateSource fts = new FileTemplateSource(null, config);
            fail("Did not fail on null namespace and ID.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Incorrect exception thrown.");
        }
    }

    /**
     * Tests empty string to constructor.
     */
    public void testConstructor_1ES() {
        try {
            FileTemplateSource fts = new FileTemplateSource("", config);
            fail("Did not fail on empty namespace.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Incorrect exception thrown.");
        }
    }

    /**
     * Tests empty parameter to constructor.
     */
    public void testConstructor_3ES() {
        try {
            FileTemplateSource fts = new FileTemplateSource("", config);
            fail("Did not fail on empty namespace and ID.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Incorrect exception thrown.");
        }
    }

    /**
     * Tests non-existant path.
     */
    public void testGetTemplate() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.getTemplate("/conf/nonexistant");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (IllegalArgumentException e) {
            fail("IAE only on nulls.");
        } catch (Exception e) {
            fail("wrong exception");
        }
    }

    /**
     * Tests locked file.
     */
    public void testGetLockedTemplate() {
        FileTemplateSource fts = new FileTemplateSource("file", config);
        File file = null;
        FileChannel ch = null;
        FileLock lock = null;
        try {
            file = new File("test_files/failure/valid-simple-comment.txt");
            ch = new RandomAccessFile(file, "rw").getChannel();
            lock = ch.lock();
        } catch (IOException e) {
            fail("Non-developer error: could not obtain lock on file.");
        }

        try {
            fts.getTemplate("test_files/failure/valid-simple-comment.txt");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (IllegalArgumentException e) {
            fail("IAE only on nulls.");
        } catch (Exception e) {
            fail("wrong exception");
        }

        try {

            lock.release();
            ch.close();
        } catch (IOException e) {
        }
    }

    /**
     * Tests null path.
     */
    public void testGetTemplateNull() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.getTemplate(null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests empty path.
     */
    public void testGetTemplateES() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.getTemplate("");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests null path in addTemplate.
     */
    public void testAddTemplate() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.addTemplate(null, "# whatever!");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests null path in addTemplate.
     */
    public void testAddTemplate_2() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.addTemplate("test_files/failure/created-at2.txt", null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }

        assertFalse("Component created file on invalid add.", new File("test_files/failure/created-at2.txt").exists());

    }

    /**
     * Tests null path in addTemplate.
     */
    public void testAddTemplate_3() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.addTemplate(null, null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests empty path in addTemplate.
     */
    public void testAddTemplateES() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.addTemplate("", "# whatever!");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests empty path in addTemplate.
     */
    public void testAddTemplate_2ES() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.addTemplate("test_files/failure/created-at2.txt", "");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }

        assertFalse("Component created file on invalid add.", new File("test_files/failure/created-at2.txt").exists());

    }

    /**
     * Tests empty path in addTemplate.
     */
    public void testAddTemplate_3ES() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.addTemplate("", "");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests bad path in addTemplate.
     */
    public void testAddTemplateBadPath() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.addTemplate("ZZ:", "# whatever!");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests locked path in addTemplate.
     */
    public void testAddTemplateLocked() {
        FileTemplateSource fts = new FileTemplateSource("file", config);
        File file = null;
        FileChannel ch = null;
        FileLock lock = null;
        try {
            file = new File("test_files/failure/created-atl.txt");
            ch = new RandomAccessFile(file, "rw").getChannel();
            lock = ch.lock();
        } catch (IOException e) {
            fail("Non-developer error: could not obtain lock on file.");
        }

        try {
            fts.addTemplate("test_files/failure/created-atl.txt", "# whatever!");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }

        try {
            lock.release();
            ch.close();
            file.delete();
        } catch (IOException e) {
        }
    }

    /**
     * Tests null path in removeTemplate.
     */
    public void testRemoveTemplateNull() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.removeTemplate(null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests empty path in removeTemplate.
     */
    public void testRemoveTemplateES() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.removeTemplate("");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests bad path in removeTemplate.
     */
    public void testRemoveTemplateBadPath() {
        FileTemplateSource fts = new FileTemplateSource("file", config);

        try {
            fts.removeTemplate("test_files/failure/created-rtbp.txt");
            fail("Did not fail.");
        } catch (TemplateSourceException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

}
