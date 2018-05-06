/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.message;

import com.topcoder.file.transfer.message.BytesMessageType;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy Test for BytesMessageType class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class BytesMessageTypeAccuracyTest extends TestCase {
    /**
     * <p>
     * Tests the staic member START_UPLOAD_FILE_BYTES.
     * </p>
     */
    public void testSTART_UPLOAD_FILE_BYTES() {
        assertEquals("The START_UPLOAD_FILE_BYTES is not initialize correctly",
                BytesMessageType.START_UPLOAD_FILE_BYTES, new BytesMessageType(
                        "START_UPLOAD_FILE_BYTES"));
    }

    /**
     * <p>
     * Tests the staic member UPLOAD_FILE_BYTES.
     * </p>
     */
    public void testUPLOAD_FILE_BYTES() {
        assertEquals("The UPLOAD_FILE_BYTES is not initialize correctly",
                BytesMessageType.UPLOAD_FILE_BYTES, new BytesMessageType(
                        "UPLOAD_FILE_BYTES"));
    }

    /**
     * <p>
     * Tests the staic member STOP_UPLOAD_FILE_BYTES.
     * </p>
     */
    public void testSTOP_UPLOAD_FILE_BYTES() {
        assertEquals("The STOP_UPLOAD_FILE_BYTES is not initialize correctly",
                BytesMessageType.STOP_UPLOAD_FILE_BYTES, new BytesMessageType(
                        "STOP_UPLOAD_FILE_BYTES"));
    }

    /**
     * <p>
     * Tests the staic member START_RETRIEVE_FILE_BYTES.
     * </p>
     */
    public void testSTART_RETRIEVE_FILE_BYTES() {
        assertEquals("The START_UPLOAD_FILE_BYTES is not initialize correctly",
                BytesMessageType.START_RETRIEVE_FILE_BYTES,
                new BytesMessageType("START_RETRIEVE_FILE_BYTES"));
    }

    /**
     * <p>
     * Tests the staic member RETRIEVE_FILE_BYTES.
     * </p>
     */
    public void testRETRIEVE_FILE_BYTES() {
        assertEquals("The RETRIEVE_FILE_BYTES is not initialize correctly",
                BytesMessageType.RETRIEVE_FILE_BYTES, new BytesMessageType(
                        "RETRIEVE_FILE_BYTES"));
    }

    /**
     * <p>
     * Tests the staic member STOP_RETRIEVE_FILE_BYTES.
     * </p>
     */
    public void testSTOP_RETRIEVE_FILE_BYTES() {
        assertEquals(
                "The STOP_RETRIEVE_FILE_BYTES is not initialize correctly",
                BytesMessageType.STOP_RETRIEVE_FILE_BYTES,
                new BytesMessageType("STOP_RETRIEVE_FILE_BYTES"));
    }

    /**
     * <p>
     * Tests ctor BytesMessageType(String type), an instance should be create
     * with type.
     * </p>
     */
    public void testCtor() {
        BytesMessageType bytesType = new BytesMessageType("Bytes");
        assertNotNull("Failed to create instance of BytesMessageType",
                bytesType);
        assertEquals("Failed to create correct instance", "Bytes", bytesType
                .getType());
    }
}