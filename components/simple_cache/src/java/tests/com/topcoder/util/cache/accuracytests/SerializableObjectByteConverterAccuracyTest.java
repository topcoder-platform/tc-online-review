/**
 * Copyright (C) 2005, TopCoder, Inc. All Rights Reserved
 */
package com.topcoder.util.cache.accuracytests;

import com.topcoder.util.cache.*;
import junit.framework.TestCase;

/**
 * Accuracy tests for the SerializableObjectByteConverter class.
 * 
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class SerializableObjectByteConverterAccuracyTest extends TestCase {

    /**
     * a String used for test object.
     */
    private String obj = "Obj for test";

    /**
     * Tests getByte and getObject
     */
    public void testGetBytesAndGetObject(){
        SerializableObjectByteConverter obc = new SerializableObjectByteConverter();
        
        byte[] objConvert;

        try {
            objConvert = obc.getBytes(obj);
            assertEquals(obj, obc.getObject(objConvert));
        } catch (ObjectByteConversionException e) {
            fail("the serialization or deserilization process fails");
        }
    }
}
