package com.topcoder.util.compression.functionaltests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import com.topcoder.util.compression.*;

/**
 * <p>This test case contains tests for basic compression use cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class CompressionTests extends TestCase {

    public static void testFileCompressWithLZ77() throws Exception {
    doTestFileCompress( "com.topcoder.util.compression.LZ77Codec" );
    }

    public static void testFileCompressWithDefault() throws Exception {
    doTestFileCompress( "com.topcoder.util.compression.DefaultCodec" );
    }

    public static void testStreamCompressWithLZ77() throws Exception {
    doTestStreamCompress( "com.topcoder.util.compression.LZ77Codec" );
    }

    public static void testStreamCompressWithDefault() throws Exception {
    doTestStreamCompress( "com.topcoder.util.compression.DefaultCodec" );
    }


    public static void doTestFileCompress( String codecName ) throws Exception {
    // delete old files
    File oldCompressedFile = new File( "test_files/compressed.txt" );
    oldCompressedFile.delete();
    File oldDecompressedFile = new File( "test_files/decompressed.txt" );
    oldDecompressedFile.delete();

    // open an empty File object
    File compressedFile = new File( "test_files/compressed.txt" );
    compressedFile.createNewFile();
    // create a CompressionUtility object with "com.topcoder.util.compression.LZ77Codec" and the emtpy File
    CompressionUtility cuCom = new CompressionUtility( codecName, compressedFile );

    // open a pre-prepared File object
    File inFile = new File( "test_files/input.txt" );
    System.out.println("inFile1:"+inFile);
    // run CompressionUtility.compress( File )
    cuCom.compress( inFile );
    cuCom.close();

    // open an empty File object
    File decompressedFile = new File( "test_files/decompressed.txt" );
    decompressedFile.createNewFile();

    // create a CompressionUtility object with "com.topcoder.util.compression.LZ77Codec" and the emtpy File
    CompressionUtility cuDec = new CompressionUtility( codecName, decompressedFile );

    // run CompressionUtility.decompress( File )
    cuDec.decompress( compressedFile );
    cuDec.close();
    // verify that their lengths are the same
    assertTrue( inFile.length() == decompressedFile.length() );
    // verify that their contents are identical

    InputStream isIn = new FileInputStream( inFile );
    InputStream isCheck = new FileInputStream( decompressedFile );

    for ( int i = 0; i < inFile.length(); i++ ) {
        int byteIn = isIn.read();
        int byteCheck = isCheck.read();
        assertTrue( byteIn == byteCheck );
    }
    }

    public static void doTestStreamCompress( String codecName ) throws Exception {
    // open an empty StringBuffer object
    ByteArrayOutputStream compressedOS = new ByteArrayOutputStream();

    // create a CompressionUtility object with the emtpy StringBuffer
    CompressionUtility cuCom = new CompressionUtility( codecName, compressedOS );

    // declare the input Stream object
    StringBuffer inSB = new StringBuffer( "this is test text.  blah blah blah blah.  blah blah blah blah.  blah blah blah blah." );
    /*for ( int i = 0; i < 40; i++ ) {
        inSB.append( inSB.toString() );
    }*/
    byte[] inBytes = inSB.toString().getBytes();
    ByteArrayInputStream inIS = new ByteArrayInputStream( inBytes );

    // run CompressionUtility.compress( InputStream )
    cuCom.compress( inIS );

    // open an empty StringBuffer object
    ByteArrayOutputStream decompressedOS = new ByteArrayOutputStream();

    // create a CompressionUtility object with the emtpy StringBuffer
    CompressionUtility cuDec = new CompressionUtility( codecName, decompressedOS );

    // run CompressionUtility.decompress( InputStream )
    cuDec.decompress( new ByteArrayInputStream( compressedOS.toByteArray() ) );

    // verify that their lengths are the same
    byte[] checkBytes = decompressedOS.toByteArray();
    assertTrue( inBytes.length == checkBytes.length );

    // verify that their contents are identical
    for ( int i = 0; i < inBytes.length; i++ ) {
        assertTrue( inBytes[i] == checkBytes[i] );
    }
    }
}








