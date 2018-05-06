/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email.accuracytests;

import com.topcoder.message.email.TCSEmailMessage;

import junit.framework.TestCase;


/**
 * The accuracy test for the added content for the class {@link TCSEmailMessage} in version
 * 3.1.
 *
 * @author KLW
 * @version 3.1
  */
public class AccuracyTCSEmailMessageTestV31 extends TestCase {
    /**
     * test the new added default constructor.
     */
    public void testCtor(){
        TCSEmailMessage instance = new TCSEmailMessage();
        assertNotNull("The instance should not be null.",instance);
    }
    /**
     * the accuracy test for the method {@link TCSEmailMessage#setContentType(String)}.
     */
    public void testSetContentType(){
        TCSEmailMessage instance = new TCSEmailMessage();
        String contentType = "text/html";
        instance.setContentType(contentType);
        assertEquals("The content type is incorrect.", contentType,instance.getContentType());
    }
    /**
     * The accuracy test for the method {@link TCSEmailMessage#getContentType()}.
     */
    public void testGetContentType(){
        TCSEmailMessage instance = new TCSEmailMessage();
        assertEquals("The content type is incorrect.", "text/plain",instance.getContentType());
        String contentType = "text/html";
        instance.setContentType(contentType);
        assertEquals("The content type is incorrect.", contentType,instance.getContentType());
    }
}
