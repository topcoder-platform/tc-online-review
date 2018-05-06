/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.message;

import com.topcoder.file.transfer.message.MessageType;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy Test for MessageType class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class MessageTypeAccuracyTest extends TestCase {
    /**
     * <p>
     * Tests the staic member UPLOAD_FILE.
     * </p>
     */
    public void testUPLOAD_FILE() {
        assertEquals("The UPLOAD_FILE is not initialize correctly",
                MessageType.UPLOAD_FILE, new MessageType("UPLOAD_FILE"));
    }

    /**
     * <p>
     * Tests the staic member CHECK_UPLOAD_FILE.
     * </p>
     */
    public void testCHECK_UPLOAD_FILE() {
        assertEquals("The CHECK_UPLOAD_FILE is not initialize correctly",
                MessageType.CHECK_UPLOAD_FILE, new MessageType(
                        "CHECK_UPLOAD_FILE"));
    }

    /**
     * <p>
     * Tests the staic member REMOVE_FILE.
     * </p>
     */
    public void testREMOVE_FILE() {
        assertEquals("The GET_FILE_NAME is not initialize correctly",
                MessageType.REMOVE_FILE, new MessageType("REMOVE_FILE"));
    }

    /**
     * <p>
     * Tests the staic member GET_FILE_NAME.
     * </p>
     */
    public void testGET_FILE_NAME() {
        assertEquals("The GET_FILE_NAME is not initialize correctly",
                MessageType.GET_FILE_NAME, new MessageType("GET_FILE_NAME"));
    }

    /**
     * <p>
     * Tests the staic member GET_FILE_SIZE.
     * </p>
     */
    public void testGET_FILE_SIZE() {
        assertEquals("The GET_FILE_SIZE is not initialize correctly",
                MessageType.GET_FILE_SIZE, new MessageType("GET_FILE_SIZE"));
    }

    /**
     * <p>
     * Tests the staic member RENAME_FILE.
     * </p>
     */
    public void testRENAME_FILE() {
        assertEquals("The RENAME_FILE is not initialize correctly",
                MessageType.RENAME_FILE, new MessageType("RENAME_FILE"));
    }

    /**
     * <p>
     * Tests the staic member RETRIEVE_FILE.
     * </p>
     */
    public void testRETRIEVE_FILE() {
        assertEquals("The RETRIEVE_FILE is not initialize correctly",
                MessageType.RETRIEVE_FILE, new MessageType("RETRIEVE_FILE"));
    }

    /**
     * <p>
     * Tests the staic member CREATE_GROUP.
     * </p>
     */
    public void testCREATE_GROUP() {
        assertEquals("The CREATE_GROUP is not initialize correctly",
                MessageType.CREATE_GROUP, new MessageType("CREATE_GROUP"));
    }

    /**
     * <p>
     * Tests the staic member UPDATE_GROUP.
     * </p>
     */
    public void testUPDATE_GROUP() {
        assertEquals("The UPDATE_GROUP is not initialize correctly",
                MessageType.UPDATE_GROUP, new MessageType("UPDATE_GROUP"));
    }

    /**
     * <p>
     * Tests the staic member RETRIEVE_GROUP.
     * </p>
     */
    public void testRETRIEVE_GROUP() {
        assertEquals("The RETRIEVE_GROUP is not initialize correctly",
                MessageType.RETRIEVE_GROUP, new MessageType("RETRIEVE_GROUP"));
    }

    /**
     * <p>
     * Tests the staic member REMOVE_GROUP.
     * </p>
     */
    public void testREMOVE_GROUP() {
        assertEquals("The REMOVE_GROUP is not initialize correctly",
                MessageType.REMOVE_GROUP, new MessageType("REMOVE_GROUP"));
    }

    /**
     * <p>
     * Tests the staic member ADD_FILE_TO_GROUP.
     * </p>
     */
    public void testADD_FILE_TO_GROUP() {
        assertEquals("The ADD_FILE_TO_GROUP is not initialize correctly",
                MessageType.ADD_FILE_TO_GROUP, new MessageType(
                        "ADD_FILE_TO_GROUP"));
    }

    /**
     * <p>
     * Tests the staic member SEARCH_FILES.
     * </p>
     */
    public void testSEARCH_FILES() {
        assertEquals("The SEARCH_FILES is not initialize correctly",
                MessageType.SEARCH_FILES, new MessageType("SEARCH_FILES"));
    }

    /**
     * <p>
     * Tests the staic member SEARCH_GROUPS.
     * </p>
     */
    public void testSEARCH_GROUPS() {
        assertEquals("The SEARCH_GROUPS is not initialize correctly",
                MessageType.SEARCH_GROUPS, new MessageType("SEARCH_GROUPS"));
    }

    /**
     * <p>
     * Test ctor MessageType(String type), an message type with the supplied
     * type should be created.
     * </p>
     */
    public void testCtor() {
        MessageType msgType = new MessageType("Accuracy");
        assertNotNull("Failed to create instance of MessageType", msgType);
        assertEquals("Failed to create MessageType correctly", "Accuracy",
                msgType.getType());
    }

    /**
     * <p>
     * Test equals(Object obj), with obj's type is same, true is expected.
     * </p>
     */
    public void testEquals_True() {
        assertTrue("Failed to get the correct result", new MessageType(
                "Accuracy").equals(new MessageType("Accuracy")));
    }

    /**
     * <p>
     * Test equals(Object obj), with the type of obj is different, false is
     * expected.
     * </p>
     */
    public void testEquals_False() {
        assertFalse("Failed to get the correct result", new MessageType(
                "Accuracy").equals(new MessageType("xxx")));
    }

    /**
     * <p>
     * Test equals(Object obj), with obj is null, false is expected.
     * </p>
     */
    public void testEquals_ObjIsNull() {
        assertFalse("Failed to get the correct result", new MessageType(
                "Accuracy").equals(null));
    }

    /**
     * <p>
     * Test equals(Object obj), with obj is not MessageType, false is expected.
     * </p>
     */
    public void testEquals_ObjNotMessageType() {
        assertFalse("Failed to get the correct result", new MessageType(
                "Accuracy").equals(new Object()));
    }

    /**
     * <p>
     * Test hashCode(), the returned hashCode should same as the hashCode of
     * type.
     * </p>
     */
    public void test() {
        assertEquals("Failed to get the correct hashCode", "Accuracy"
                .hashCode(), new MessageType("Accuracy").hashCode());
    }
}