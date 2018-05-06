/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Tests functionality of the <code>LZ77Codec</code> implementation.</p>
 *
 * @author  srowen
 * @version 2.0
 *
 * @since 1.0
 */
public class LZ77CodecTestCase extends AbstractCodecTestCase {

    /**
     * @return name of <code>Codec</code> implementation class
     */
    String getCodecClassName() {
        return "com.topcoder.util.compression.LZ77Codec";
    }

    public void testDeflaterInflater() throws Exception {
        final Codec codec = new LZ77Codec();

        doTestDeflaterInflater(
                codec,
                new byte[]{0, 0, 1, 1, 0, 1, 0, 1},
                new byte[]{0, 0, 0, 1, 1, 1, 1, 1, 0, 2, 2, 1});

        doTestDeflaterInflater(
                codec,
                new byte[]{100},
                new byte[]{0, 0, 100});

        doTestDeflaterInflater(
                codec,
                new byte[]{100, 100},
                new byte[]{0, 0, 100, 0, 0, 100});

        doTestDeflaterInflater(
                codec,
                new byte[]{100, 100, 100},
                new byte[]{0, 0, 100, 1, 1, 100});

        byte[] zeroes = new byte[256];
        Arrays.fill(zeroes, (byte) 0);
        doTestDeflaterInflater(
                codec,
                zeroes,
                new byte[]{0, 0, 0, 1, 1, 0, 3, 3, 0, 7, 7, 0, 15, 15, 0,
                           31, 31, 0, 63, 63, 0, 127, 127, 0, 0, 0, 0});
        zeroes = new byte[257];
        Arrays.fill(zeroes, (byte) 0);
        doTestDeflaterInflater(
                codec,
                zeroes,
                new byte[]{0, 0, 0, 1, 1, 0, 3, 3, 0, 7, 7, 0, 15, 15, 0,
                           31, 31, 0, 63, 63, 0, 127, 127, 0, 2, 1, 0});

    }

    private void doTestDeflaterInflater(final Codec c,
                                        final byte[] uncompressed,
                                        final byte[] compressed)
            throws Exception
    {
        final Deflater deflater = c.createDeflater();
        final Inflater inflater = c.createInflater();

        for (int i = 0; i < 2; i++) {

            // Test deflater

            assertTrue(deflater.needsInput());

            deflater.setInput(uncompressed);

            final byte[] temp = new byte[3 * uncompressed.length];
            final int bytesEncoded = deflater.deflate(temp);
            assertEquals(uncompressed.length, deflater.getTotalIn());
            assertEquals(bytesEncoded, deflater.getTotalOut());

            deflater.finish();
            deflater.end();

            // Should have consumed all input
            assertTrue(deflater.needsInput());
            assertTrue(deflater.finished());

            if (compressed.length != bytesEncoded
                    || !CompressionTestUtils.arraysEqual(compressed,
                                                         0,
                                                         temp,
                                                         0,
                                                         compressed.length)) {
                fail("Arrays unequal; expected " +
                     CompressionTestUtils.arrayToString(compressed,
                                                        0,
                                                        compressed.length) +
                     " but was " +
                     CompressionTestUtils.arrayToString(temp, 0, bytesEncoded));
            }

            // Test inflater

            assertTrue(inflater.needsInput());

            inflater.setInput(compressed);
            assertEquals(compressed.length, inflater.getRemaining());

            final byte[] temp2 = new byte[10 * uncompressed.length];
            final int bytesDecoded = inflater.inflate(temp2);
            assertEquals(compressed.length, inflater.getTotalIn());
            assertEquals(bytesDecoded, inflater.getTotalOut());

            assertEquals(0, inflater.getRemaining());

            inflater.end();

            // Should have consumed all input
            assertTrue(inflater.needsInput());
            assertTrue(inflater.finished());

            if (uncompressed.length != bytesDecoded
                    || !CompressionTestUtils.arraysEqual(uncompressed,
                                                         0,
                                                         temp2,
                                                         0,
                                                         uncompressed.length))
            {
                fail("Arrays unequal; expected " +
                     CompressionTestUtils.arrayToString(uncompressed,
                                                        0,
                                                        uncompressed.length) +
                     " but was " +
                     CompressionTestUtils.arrayToString(temp2,
                                                        0,
                                                        bytesDecoded));
            }

            // make sure it works again after a reset
            deflater.reset();
            inflater.reset();
            assertEquals(0, deflater.getTotalIn());
            assertEquals(0, inflater.getTotalIn());
            assertEquals(0, deflater.getTotalOut());
            assertEquals(0, inflater.getTotalOut());
        }
    }

    public void testIllegalArgs() throws Exception {
        try {
            final Inflater i = new LZ77Decoder();
            i.setInput(new byte[5]);
            i.inflate(new byte[128]);
            fail("Should have thrown DataFormatException");
        } catch (DataFormatException dfe) {
            // good
        }
    }

    public static Test suite() {
        return new TestSuite(LZ77CodecTestCase.class);
    }
}








