/**
 * AbstractCodecTestCase.java
 *
 * @author TCSREVIEWER
 * @version 1.0
 */
package com.topcoder.util.compression.accuracytests;

import com.topcoder.util.compression.CompressionUtility;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileReader;

public abstract class AbstractCodecTestCase extends TestCase {

    private static File TEST_FILES_DIR = new File("test_files");
    private static File[] TEST_FILES = {
        new File(TEST_FILES_DIR, "aaa"),
        new File(TEST_FILES_DIR, "bbb"),
        new File(TEST_FILES_DIR, "ccc")
    };

    /**
     * Method fileToString reads a file and returns the content
     * of the file as a string
     *
     * @param    input               a  File
     *
     * @return   a String
     *
     * @throws   IOException
     *
     */
    private String fileToString(File input) throws IOException {
        FileReader fr = new FileReader(input);
        StringBuffer sb = new StringBuffer();
        char[] chars = new char[1024];
        int numChars = 0;
        try {
            while ((numChars = fr.read(chars)) > 0) {
                sb.append(chars, 0, numChars);
            }
        } finally {
            fr.close();
        }
        return sb.toString();
    }

    public abstract  String getCodecName();

    /**
     * Method testCDFiles tests the the methods of CompressionUtility
     * wich use files.
     *
     * @throws   Exception
     *
     */
    public void testCDFiles() throws Exception {
        for (int i = 0; i < TEST_FILES.length; i++) {
            doTestCDFile(TEST_FILES[i]);
        }
    }

    /**
     * Method doTestCDFile compresses and decompresses a file
     * given as parameter and then it verifies if the input
     * and the result are equal.
     *
     * @param    input               a  File
     *
     * @throws   Exception
     *
     */
    public void doTestCDFile(final File input) throws Exception {
        File compressed;
        File decompressed;
        compressed = File.createTempFile("compressed", null);
        decompressed = File.createTempFile("decompressed", null);

        CompressionUtility comp = new CompressionUtility(getCodecName(),
                                                       compressed);
        comp.compress(input);
        comp.close();

        CompressionUtility decomp = new CompressionUtility(getCodecName(),
                                                         decompressed);
        decomp.decompress(compressed);
        decomp.close();

        assertTrue(fileToString(input).equals(fileToString(decompressed)));

        compressed.delete();
        decompressed.delete();
    }

    /**
     * Method testCDStreams tests the the methods of CompressionUtility
     * wich use streams.
     *
     * @throws   Exception
     *
     */
    public void testCDStreams() throws Exception {
        for (int i = 0; i < TEST_FILES.length; i++) {
            doTestCDStream(TEST_FILES[i]);
        }
    }

    /**
     * Method doTestCDStream  compresses and decompresses a file wich
     * is converted to FileInputStream and then it verifies if the input
     * and the result are equal.
     *
     * @param    input               a  File
     *
     * @throws   Exception
     *
     */
    public void doTestCDStream(final File input) throws Exception {
        File compressed;
        File decompressed;
        compressed = File.createTempFile("compressed", null);
        decompressed = File.createTempFile("decompressed", null);

        CompressionUtility comp =
            new CompressionUtility(getCodecName(),
                                   new FileOutputStream(compressed));
        comp.compress(new FileInputStream(input));
        comp.close();

        CompressionUtility decomp =
            new CompressionUtility(getCodecName(),
                                   new FileOutputStream(decompressed));
        decomp.decompress(new FileInputStream(compressed));
        decomp.close();

        assertTrue(fileToString(input).equals(fileToString(decompressed)));

        compressed.delete();
        decompressed.delete();
    }

    /**
     * Method testCDBuffers tests the the methods of CompressionUtility
     * wich use StringBuffers.
     *
     * @throws   Exception
     *
     */
    public void testCDBuffers() throws Exception {
        for (int i = 0; i < TEST_FILES.length; i++) {
            doTestCDStream(TEST_FILES[i]);
        }
    }

    /**
     * Method doTestCDStringBuffer compresses and decompresses a file which
     * is converted to a StringBuffer and then it verifies if the input
     * and the result are equal.
     *
     * @param    input               a  File
     *
     * @throws   Exception
     *
     */
    /*
    public void doTestCDStringBuffer(final File input) throws Exception {
        StringBuffer compressed = new StringBuffer();
        StringBuffer decompressed = new StringBuffer();

        CompressionUtility comp = new CompressionUtility(getCodecName(),
                                                       compressed);
        comp.compress(new StringBuffer(fileToString(input)));
        comp.close();

        CompressionUtility decomp = new CompressionUtility(getCodecName(),
                                                         decompressed);
        decomp.decompress(compressed);
        decomp.close();

        assertTrue(fileToString(input).equals(decompressed));
    }
    */
}









