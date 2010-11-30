/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import junit.framework.TestCase;

import com.thoughtworks.selenium.Selenium;


/**
 * Online review functional tests 1, the base test case.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class BaseTests extends TestCase {

    /**
     * Initialize static attributes.
     */
    static {
        try {
            TIMEOUT = TestHelper.getTimeout();
        } catch (Exception e) {
            // ignore
        }
    }

    /** 
     * Represent the timeout of an action. 
     */
    protected static String TIMEOUT;

    /**
     *  Represents the Selenium Instance. 
     */
    protected Selenium browser;

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
        browser = TestHelper.getLoginPage();
        browser.setSpeed(TestHelper.getBrowserSpeed());
        super.setUp();
    }

    /**
     * Tears down the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void tearDown() throws Exception {
        browser.stop();
        super.tearDown();
    }
    
    protected void assertNoErrorsOccurred() {
    	assertFalse("No validation errors should exist", browser.isTextPresent(TestHelper.VALIDATION_ERROR));
	    assertFalse("Error page shouldn't be displayed", browser.isTextPresent(TestHelper.ERROR_TEXT));
    }

}
