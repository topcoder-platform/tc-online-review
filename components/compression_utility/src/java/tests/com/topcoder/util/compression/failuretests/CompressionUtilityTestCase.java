/*
 * @(#)CompressionUtilityTestCase.java  1.0  4/26/2003
 *
 * Copyright  2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.compression.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import com.topcoder.util.compression.*;

/**
 * <p>Compression Utility test case.</p>
 *
 * @author kolanovic
 * @version 1.0
 */
public class CompressionUtilityTestCase extends TestCase {
    private static String dirName = "test_files/failure";
    private static String nonExistingFileName = dirName + "/badTest";

    private static String existingCodec =
        "com.topcoder.util.compression.DefaultCodec";
    private static String nonExistingCodec =
        "com.topcoder.util.compression.failuretest.TestCodec";

    public void testConstructors() {
        // test the constructor with null OutputStream
        try {
            OutputStream output = null;

            new CompressionUtility(existingCodec,output);
            fail("Excpected an IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IllegalArgumentException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test the constructor with null File
        try {
            File output = null;

            new CompressionUtility(existingCodec,output);
            fail("Excpected an IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IllegalArgumentException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test the constructor with null StringBuffer
        /*
        try {
            StringBuffer output = null;

            new CompressionUtility(existingCodec,output);
            fail("Excpected an IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IllegalArgumentException to be thrown " +
                 "but received: " + e.getClass().getName());
        }
        */

        // test the constructor with a null Codec class name
        try {
            new CompressionUtility(null,new ByteArrayOutputStream());
            fail("Excpected an IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IllegalArgumentException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test the constructor with an invalid Codec class name
        try {
            new CompressionUtility(nonExistingCodec,new ByteArrayOutputStream());
            fail("Excpected an ClassNotFoundException to be thrown");
        } catch(ClassNotFoundException ce) {
            // good
        } catch(Exception e) {
            fail("Excpected an ClassNotFoundException to be thrown " +
                 "but received: " + e.getClass().getName());
        }
    }

    public void testCreateCodec() {
        try {
            CompressionUtility.createCodec(nonExistingCodec);
            fail("Excpected an ClassNotFoundException to be thrown");
        } catch(ClassNotFoundException ce) {
            // good
        } catch(Exception e) {
            fail("Excpected an ClassNotFoundException to be thrown " +
                 "but received: " + e.getClass().getName());
        }
    }

    public void testCompress() {
        // test compress with a null InputStream
        try {
            ByteArrayInputStream input = null;
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            CompressionUtility uc =
                new CompressionUtility(existingCodec,output);

            uc.compress(input);
            fail("Excpected an IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IllegalArgumentException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test compress with a null File
        try {
            File input = null;
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            CompressionUtility uc =
                new CompressionUtility(existingCodec,output);

            uc.compress(input);
            fail("Excpected an IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IllegalArgumentException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test compress with a null StringBuffer
        try {
            StringBuffer input = null;
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            CompressionUtility uc =
                new CompressionUtility(existingCodec,output);

            uc.compress(input);
            fail("Excpected an IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IllegalArgumentException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test compress with a directory set as
        // output, should result in an IOException
        try {
            File output = new File(dirName);
            File input = new File(dirName,"decompressed.txt");

            CompressionUtility uc =
                new CompressionUtility(existingCodec,output);

            uc.compress(input);
            fail("Excpected an IOException to be thrown");
        } catch(IOException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IOException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test compress with a non existent File set as
        // input, should result in an IOException
        try {
            ByteArrayOutputStream output =
                new ByteArrayOutputStream();
            File input =
                new File(nonExistingFileName);

            CompressionUtility uc =
                new CompressionUtility(existingCodec,output);

            uc.compress(input);
            fail("Excpected an IOException to be thrown");
        } catch(IOException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IOException to be thrown " +
                 "but received: " + e.getClass().getName());
        }
    }

    public void testDecompress() {
        // test decompress with a null InputStream
        try {
            ByteArrayInputStream input = null;
            ByteArrayOutputStream output =
                new ByteArrayOutputStream();

            CompressionUtility uc =
                new CompressionUtility(existingCodec,output);

            uc.decompress(input);
            fail("Excpected an IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IllegalArgumentException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test decompress with a null File
        try {
            File input = null;
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            CompressionUtility uc =
                new CompressionUtility(existingCodec,output);

            uc.decompress(input);
            fail("Excpected an IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IllegalArgumentException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test decompress with a null StringBuffer
        try {
            StringBuffer input = null;
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            CompressionUtility uc =
                new CompressionUtility(existingCodec,output);

            uc.decompress(input);
            fail("Excpected an IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IllegalArgumentException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test decompress with a directory set as
        // output, should result in an IOException
        try {
            File output = new File(dirName);
            File input =  new File(dirName,"comptessed.txt");

            CompressionUtility uc =
                new CompressionUtility(existingCodec,output);

            uc.decompress(input);
            fail("Excpected an IOException to be thrown");
        } catch(IOException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IOException to be thrown " +
                 "but received: " + e.getClass().getName());
        }

        // test decompress with a non existent File set as
        // input, should result in an IOException
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            File input = new File(nonExistingFileName);

            CompressionUtility uc =
                new CompressionUtility(existingCodec,output);

            uc.decompress(input);
            fail("Excpected an IOException to be thrown");
        } catch(IOException ie) {
            // good
        } catch(Exception e) {
            fail("Excpected an IOException to be thrown " +
                 "but received: " + e.getClass().getName());
        }
    }

    public static Test suite() {
        return(new TestSuite(CompressionUtilityTestCase.class));
    }
}








