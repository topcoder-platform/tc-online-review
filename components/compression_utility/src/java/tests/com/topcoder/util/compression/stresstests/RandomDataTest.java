package com.topcoder.util.compression.stresstests;

import com.topcoder.util.compression.*;
import junit.framework.TestCase;
import java.util.Random;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * This class tests the performance of the codecs when handling random data.
 * @author nrogers
 * @version 1.0
 *
 */
public class RandomDataTest extends TestCase {
    static final int size = 100000;
    byte[] original;
    public void setUp() {
        original = new byte[size];
        Random rand = new Random(42);
        rand.nextBytes(original);
    }

    public void testDefaultCodec() throws Exception {
        //Test JDK supplied codec
        System.out.println("Statistics for DefaultCodec with random data:");
        doTest("com.topcoder.util.compression.DefaultCodec");
    }

    public void testLZ77Codec() throws Exception {
        //Test component supplied codec
        System.out.println("Statistics for LZ77Codec with random data:");
        doTest("com.topcoder.util.compression.LZ77Codec");
    }

    private void doTest(String codec) throws Exception {
        Date start, end;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompressionUtility comp = new CompressionUtility(codec, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(original);
        start = new Date();
        comp.compress(inputStream);
        end = new Date();
        System.out.println(
            "\tCompression took "
                + (end.getTime() - start.getTime())
                + " milliseconds.");

        byte[] compressed = outputStream.toByteArray();
        double percentage =
            Math.round(100.0 * compressed.length / original.length);
        System.out.println(
            "\tCompressed data is "
                + percentage
                + " percent of the original size.");

        outputStream = new ByteArrayOutputStream();
        comp = new CompressionUtility(codec, outputStream);
        inputStream = new ByteArrayInputStream(compressed);
        start = new Date();
        comp.decompress(inputStream);
        end = new Date();
        byte[] decompressed = outputStream.toByteArray();
        System.out.println(
            "\tDecompression took "
                + (end.getTime() - start.getTime())
                + " milliseconds.");
        assertTrue(Arrays.equals(original, decompressed));
    }
}








