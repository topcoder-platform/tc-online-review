/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.cache.failuretests;

import java.io.Serializable;

import com.topcoder.util.cache.ObjectByteConversionException;
import com.topcoder.util.cache.SerializableObjectByteConverter;

import junit.framework.TestCase;

/**
 * Failure test for SerializableObjectByteConverter class.
 *
 * @author semi_sleep
 * @version 2.0
 */
public class SerializableObjectByteConverterFailureTest extends TestCase {
    /**
     * Test the method <code>getBytes</code>.
     * Test the case the value is invalid, an IllegalArgumentException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testGetBytesValueInvalid1() throws Exception {
        try {
            new SerializableObjectByteConverter().getBytes(new Unserializable());
            fail("Should throw IllegalArgumentException if value is invalid.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>getBytes</code>.
     * Test the case the value is can not be serialized, an ObjectByteConversionException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testGetBytesValueInvalid2() throws Exception {
        try {
            new SerializableObjectByteConverter().getBytes(new UnSerializableEnclosing());
            fail("Should throw ObjectByteConversionException if value is can not be serialized.");
        } catch (ObjectByteConversionException e) {
            // success
        }
    }

    /**
     * Test the method <code>getObject</code>.
     * Test the case the byteArray is can not be deserialized, an ObjectByteConversionException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testGetObjectByteArrayInvalid() throws Exception {
        try {
            new SerializableObjectByteConverter().getObject(new byte[5]);
            fail("Should throw ObjectByteConversionException if byteArray is can not be deserialized.");
        } catch (ObjectByteConversionException e) {
            // success
        }
    }

}

/**
 * Dummy class used for test.
 */
class UnSerializableEnclosing implements Serializable{
    /**
     * Contains unserializable variable.
     */
    private Unserializable dummy = new Unserializable();
}

/**
 * Dummy class used for test.
 */
class Unserializable {
}
