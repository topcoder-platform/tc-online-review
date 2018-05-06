/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import junit.framework.TestCase;

/**
 * <p>Tests functionality of a <code>Codec</code> implementation. This
 * class tests functionality that should be common to all implementations;
 * subclasses can perform more specific tests.</p>
 *
 * @author  srowen
 * @version 2.0
 *
 * @since 1.0
 */
public abstract class AbstractCodecTestCase extends TestCase {

    private static final File TEST_FILES_DIR = new File("test_files");
    private static final File[] TEST_FILES = new File[] {
        new File(TEST_FILES_DIR, "midsummernightsdream.txt"),
        new File(TEST_FILES_DIR, "sunset.jpg"),
        new File(TEST_FILES_DIR, "ed")
    };

    /**
     * @return name of <code>Codec</code> implementation class
     */
    abstract String getCodecClassName();

    public void testCompressDecompressFile() throws Exception {
        for (int i = 0; i < TEST_FILES.length; i++) {
            doTestCompressDecompressFile(TEST_FILES[i]);
        }
    }

    private void doTestCompressDecompressFile(final File input)
        throws Exception
    {
        final File compressed = File.createTempFile("compress", null);
        compressed.deleteOnExit();
        final File decompressed = File.createTempFile("decompress", null);
        decompressed.deleteOnExit();

        final CompressionUtility compressor =
                new CompressionUtility(getCodecClassName(), compressed);

        compressor.compress(input);
        compressor.close();

        final CompressionUtility decompressor =
                new CompressionUtility(getCodecClassName(), decompressed);

        decompressor.decompress(compressed);
        decompressor.close();

        assertEquals(input.length(), decompressed.length());
        assertTrue(CompressionTestUtils.filesMatch(input, decompressed));

        compressed.delete();
        decompressed.delete();
    }

    public void testCompressDecompressInputStream() throws Exception {
        for (int i = 0; i < TEST_FILES.length; i++) {
            doTestCompressDecompressInputStream(TEST_FILES[i]);
        }
    }

    private void doTestCompressDecompressInputStream(final File input)
        throws Exception
    {
        final File compressed = File.createTempFile("compress", null);
        compressed.deleteOnExit();
        final File decompressed = File.createTempFile("decompress", null);
        decompressed.deleteOnExit();

        final CompressionUtility compressor =
                new CompressionUtility(getCodecClassName(), new FileOutputStream(compressed));

        compressor.compress(new FileInputStream(input));
        compressor.close();

        final CompressionUtility decompressor =
                new CompressionUtility(getCodecClassName(), new FileOutputStream(decompressed));

        decompressor.decompress(new FileInputStream(compressed));
        decompressor.close();

        assertEquals(input.length(), decompressed.length());
        assertTrue(CompressionTestUtils.filesMatch(input, decompressed));

        compressed.delete();
        decompressed.delete();
    }


    public void testSetInputIllegalArgs() throws Exception {
        final Codec codec = CompressionUtility.createCodec(getCodecClassName());
        assertNotNull(codec);

        final Deflater deflater = codec.createDeflater();
        assertNotNull(deflater);

        final Inflater inflater = codec.createInflater();
        assertNotNull(inflater);

        try {
            deflater.setInput(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // good
        }
        try {
            deflater.setInput(null, 0, 0);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // good
        }
        try {
            deflater.setInput(new byte[0], -1, 0);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            deflater.setInput(new byte[0], 0, -1);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            deflater.setInput(new byte[1], 0, 2);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }

        try {
            inflater.setInput(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // good
        }
        try {
            inflater.setInput(null, 0, 0);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // good
        }
        try {
            inflater.setInput(new byte[0], -1, 0);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            inflater.setInput(new byte[0], 0, -1);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            inflater.setInput(new byte[1], 0, 2);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
    }

    public void testDeflateInflateIllegalArgs() throws Exception {
        final Codec codec = CompressionUtility.createCodec(getCodecClassName());
        assertNotNull(codec);

        final Deflater deflater = codec.createDeflater();
        assertNotNull(deflater);

        final Inflater inflater = codec.createInflater();
        assertNotNull(inflater);

        try {
            deflater.deflate(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // good
        }
        try {
            deflater.deflate(null, 0, 0);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // good
        }
        try {
            deflater.deflate(new byte[0], -1, 0);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            deflater.deflate(new byte[0], 0, -1);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            deflater.deflate(new byte[1], 0, 2);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }

        try {
            inflater.inflate(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // good
        }
        try {
            inflater.inflate(null, 0, 0);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // good
        }
        try {
            inflater.inflate(new byte[0], -1, 0);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            inflater.inflate(new byte[0], 0, -1);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            inflater.inflate(new byte[1], 0, 2);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
    }

}








