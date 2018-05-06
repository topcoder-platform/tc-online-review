/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.cache.failuretests;

import java.util.ArrayList;
import java.util.Hashtable;

import com.topcoder.util.cache.CompressionException;
import com.topcoder.util.cache.DefaultCompressionHandler;
import com.topcoder.util.cache.TypeNotMatchedException;

import junit.framework.TestCase;

/**
 * Failure test for DefaultCompressionHandler class.
 *
 * @author semi_sleep
 * @version 2.0
 */
public class DefaultCompressionHandlerFailureTest extends TestCase {
    /**
     * Test the constructor of <code>DefaultCompressionHandler</code>.
     * This method test the case that typeMatchRegex is invalid.
     * an IllegalArgumentException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor1TypeMatchRegexInvalid() throws Exception {
        try {
            new DefaultCompressionHandler(null, "[ab", null, null);
            fail("Should throw IllegalArgumentException when typeMatchRegex is invalid.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
   
    /**
     * Test the constructor of <code>DefaultCompressionHandler</code>.
     * This method test the case that acceptedTypes contains invalid element,
     * an IllegalArgumentException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor1AcceptedTypesInvalid() throws Exception {
        try {
            ArrayList acceptedTypes = new ArrayList();
            acceptedTypes.add(new Integer(1));
            new DefaultCompressionHandler(null, ".*", null, acceptedTypes);
            fail("Should throw IllegalArgumentException when acceptedTypes contains invalid element.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the constructor of <code>DefaultCompressionHandler</code>.
     * This method test the case that parameters is null,
     * a NullPointerException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor2NullParameters() throws Exception {
        try {
            new DefaultCompressionHandler(null);
            fail("Should throw NullPointerException when parameters is null.");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    /**
     * Test the constructor of <code>DefaultCompressionHandler</code>.
     * This method test the case that value for OBJECT_BYTE_CONVERTER_CLASS is invalid,
     * an IllegalArgumentException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor2ParametersInvalid1() throws Exception {
        try {
            Hashtable parameters = new Hashtable();
            parameters.put("AcceptedObjectTypesRegex", "[ab");
            new DefaultCompressionHandler(parameters);
            fail("Should throw IllegalArgumentException when value for OBJECT_BYTE_CONVERTER_CLASS is invalid.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>compress</code>.
     * Test the case the type is not matched, an TypeNotMatchedException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testCompressTypeNotMatched() throws Exception {
        try {
            ArrayList acceptedTypes = new ArrayList();
            acceptedTypes.add(String.class.getName());
            DefaultCompressionHandler instance = new DefaultCompressionHandler(null, null, null, acceptedTypes);
            instance.compress(new Object());
            fail("Should throw TypeNotMatchedException if the type is not matched.");
        } catch (TypeNotMatchedException e) {
            // success
        }
    }

    /**
     * Test the method <code>compress</code>.
     * Test the case the given value is invalid, an CompressionException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testCompressValueInvalid() throws Exception {
        try {
            DefaultCompressionHandler instance = new DefaultCompressionHandler(
                    null,
                    ".*",
                    null,
                    new ArrayList());
            instance.compress(new UnSerializableEnclosing());
            fail("Should throw CompressionException if the given value is invalid.");
        } catch (CompressionException e) {
            // success
        }
    }

    /**
     * Test the method <code>decompress</code>.
     * Test the case the given compressedValue is invalid, an CompressionException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testDecompressValueInvalid() throws Exception {
        try {
            DefaultCompressionHandler instance = new DefaultCompressionHandler(
                    null,
                    ".*",
                    null,
                    new ArrayList());
            instance.decompress(new byte[5]);
            fail("Should throw CompressionException if the given compressedValue is invalid.");
        } catch (CompressionException e) {
            // success
        }
    }
}
