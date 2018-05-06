/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * <p>
 * Tests basic functionality of the <code>CompressionUtility</code> class. It does not test compression / decompression
 * functionality; that is tested in {@link AbstractCodecTestCase}.
 * </p>
 *
 * @author srowen, visualage
 * @version 2.0
 *
 * @since 1.0
 */
public class CompressionUtilityTestCase extends TestCase {
    private static final String DEFAULT_CODEC_CLASS_NAME = DefaultCodec.class.getName();

    /**
     * <p>
     * Test constructor with illegal arguments.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructorIllegalArgs() throws Exception {
        final OutputStream dummyStream = new ByteArrayOutputStream();
        final File dummyFile = new File(".");
        final File realFile = new File(new File("test_files"), "somefile");

        try {
            new CompressionUtility(DEFAULT_CODEC_CLASS_NAME, (OutputStream) null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }

        try {
            new CompressionUtility(null, dummyStream);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }

        try {
            new CompressionUtility(DEFAULT_CODEC_CLASS_NAME, (File) null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }

        try {
            new CompressionUtility(null, dummyFile);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }

        try {
            new CompressionUtility(DEFAULT_CODEC_CLASS_NAME, dummyFile);
            fail("Should have thrown FileNotFoundException");
        } catch (FileNotFoundException fnfe) {
            // good
        }

        try {
            new CompressionUtility("nosuchclass", dummyStream);
            fail("Should have thrown ClassNotFoundException");
        } catch (ClassNotFoundException cnfe) {
            // good
        }

        try {
            new CompressionUtility("nosuchclass", realFile);
            fail("Should have thrown ClassNotFoundException");
        } catch (ClassNotFoundException cnfe) {
            // good
        }
    }

    /**
     * <p>
     * Test <code>CreateCodec</code> with illegal arguments.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateCodecIllegalArgs() throws Exception {
        try {
            CompressionUtility.createCodec(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }

        try {
            CompressionUtility.createCodec("nosuchclass");
            fail("Should have thrown ClassNotFoundException");
        } catch (ClassNotFoundException cnfe) {
            // good
        }

        try {
            CompressionUtility.createCodec("java.io.Serializable");
            fail("Should have thrown InstantiationException");
        } catch (InstantiationException ie) {
            // good
        }

        try {
            CompressionUtility.createCodec("java.lang.Object");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ie) {
            // good
        }
    }

    /**
     * <p>
     * Test compress and decompress methods with illegal arguments.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCompressDecompressIllegalArgs() throws Exception {
        final File dummyFile = new File(".");
        final File realFile = new File(new File("test_files"), "somefile");
        final CompressionUtility cu = new CompressionUtility(DEFAULT_CODEC_CLASS_NAME, realFile);

        try {
            cu.compress((InputStream) null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ie) {
            // good
        }

        try {
            cu.compress((File) null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ie) {
            // good
        }

        try {
            cu.compress((StringBuffer) null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ie) {
            // good
        }

        try {
            cu.decompress((InputStream) null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ie) {
            // good
        }

        try {
            cu.decompress((File) null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ie) {
            // good
        }

        try {
            cu.decompress((StringBuffer) null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException ie) {
            // good
        }

        try {
            cu.compress(dummyFile);
            fail("Should have thrown FileNotFoundException");
        } catch (FileNotFoundException fnfe) {
            // good
        }

        try {
            cu.decompress(dummyFile);
            fail("Should have thrown FileNotFoundException");
        } catch (FileNotFoundException fnfe) {
            // good
        }
    }

    /**
     * <p>
     * Test functionality of creating codec.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateCodec() throws Exception {
        Codec c = CompressionUtility.createCodec(DEFAULT_CODEC_CLASS_NAME);
        assertNotNull(c);
        assertTrue(c instanceof DefaultCodec);

        c = CompressionUtility.createCodec("com.topcoder.util.compression.LZ77Codec");
        assertNotNull(c);
        assertTrue(c instanceof LZ77Codec);
    }

    /**
     * <p>
     * Return a new <code>TestSuite</code> containing all test cases in this class.
     * </p>
     *
     * @return a new <code>TestSuite</code> containing all test cases in this class
     */
    public static Test suite() {
        return new TestSuite(CompressionUtilityTestCase.class);
    }
}








