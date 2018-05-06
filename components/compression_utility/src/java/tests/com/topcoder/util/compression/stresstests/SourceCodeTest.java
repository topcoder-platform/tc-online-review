package com.topcoder.util.compression.stresstests;

import com.topcoder.util.compression.*;
import junit.framework.TestCase;
import java.util.Random;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * This class tests the performance of the codecs when handling Java source
 * code.  A file containing a concatanation of the source files from this project is
 * used.
 * @author nrogers
 * @version 1.0
 *
 */
public class SourceCodeTest extends TestCase {
    static final int copies = 1;
    byte[] original;
    public void setUp() throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream("test_files/java.txt");
        byte[] buffer = new byte[1000];
        int bytesRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((bytesRead = in.read(buffer)) > 0)
            out.write(buffer, 0, bytesRead);
        original = out.toByteArray();
    }

    public void testDefaultCodec() throws Exception {
        //Test JDK supplied codec
        System.out.println("Statistics for DefaultCodec with source code:");
        doTest("com.topcoder.util.compression.DefaultCodec");
    }

    public void testLZ77Codec() throws Exception {
        //Test component supplied codec
        System.out.println("Statistics for LZ77Codec with source code:");
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








