/**
 * Copyright (C) 2005, TopCoder, Inc. All Rights Reserved
 */
package com.topcoder.util.cache.accuracytests;

import com.topcoder.util.cache.*;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Accuracy tests for the DefaultCompressionHandler class.
 * 
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class DefaultCompressionHandlerAccuracyTest extends TestCase {

    /**
     * A DefaultCompressionHandler instance to test.
     */
    private DefaultCompressionHandler dch;
    
    /**
     * a String to represents a valid codec. 
     */
    private final String codec = "com.topcoder.util.compression.LZ77Codec";
    
    /**
     * a String to Represents an valid regular expression which can be used to match
     * different types of objects that this CompressionHandler can actually
     * process
     */
    private final String validRegex = "java.util.*";
    
    /**
     * a String to Represents an invalid regular expression which can be used to match
     * different types of objects that this CompressionHandler can actually
     * process
     */
    private final String invalidRegex = "((((";
    
    /**
     * a ObjectByteConverter instance used to construct the test instance.
     */
    private final ObjectByteConverter obc = new SerializableObjectByteConverter();
    
    /**
     * a List instance used to construct the test instance.
     */
    List accpList = null;
    
    /**
     * Sets up the environment
     */
    public void setUp() {
        // nothing to set up
    }

    /**
     * Tears down the environment
     */
    public void tearDown(){
        dch = null;
    }
    
    /**
     * Test the compress and decompress methods
     */
    public void testCompressAndDecompress() throws CompressionException {
        ArrayList toComp = new ArrayList();
        toComp.add("obj1");
        toComp.add("obj2");
        byte[] objComped;
        
        // use default 
        dch = new DefaultCompressionHandler();
        objComped = dch.compress(toComp);
        assertEquals(toComp, dch.decompress(objComped));
        
        // use another codec
        dch = new DefaultCompressionHandler(codec, validRegex, obc, accpList);
        objComped = dch.compress(toComp);
        assertEquals(toComp, dch.decompress(objComped));
        
        // use another acceptedTypes
        String accpType1 = "java.util.ArrayList";
        String accpType2 = "java.util.Set";
        accpList = new ArrayList();
        accpList.add(accpType1);
        accpList.add(accpType2);
        dch = new DefaultCompressionHandler(codec, null, obc, accpList);
        objComped = dch.compress(toComp);
        assertEquals(toComp, dch.decompress(objComped));
        
        // use another ObjectByteConverter
        dch = new DefaultCompressionHandler(codec, null, obc, null);
        objComped = dch.compress(toComp);
        assertEquals(toComp, dch.decompress(objComped));
    }
}
