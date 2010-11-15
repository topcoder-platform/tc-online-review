/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * Online review functional tests 1, login related features.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class LoginTests extends BaseTests {

	/**
     * Test Case Number: FTC1 RS1.1 Verify user can use remember me functionality.
     *
     * @throws Exception if any error occurs
     */
    public void testRememberMeFunctionality() throws Exception {
        browser.type("userName", TestHelper.getUsername());
        browser.type("password", TestHelper.getPassword());
        browser.click("rememberMe");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("user should be logged in", browser.isTextPresent("Hello, "+TestHelper.getUsername()));
        // reopen the browser
        browser.open(TestHelper.getBaseURL());
        // open the browser again
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("user should be logged in", browser.isTextPresent("Hello, "+TestHelper.getUsername()));
        // logout the user
        browser.click("link=Logout");
        browser.waitForPageToLoad(TIMEOUT);
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC2 RS1.1 Verify user can not login with invalid credentials.
     *
     * @throws Exception if any error occurs
     */
    public void testLoginWithInvalidCredentials() throws Exception {
        // login with invalid password
        browser.type("userName", "twight");
        browser.type("password", "invalidPassword");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("login should be rejected, error message must be correct",
            browser.isTextPresent("The following errors have occurred:"));
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC2 RS1.1 Verify user can not login with invalid credentials.
     *
     * @throws Exception if any error occurs
     */
    public void testLoginWithInvalidCredentials2() throws Exception {
        // login with invalid username
        browser.type("userName", "invalidUser");
        browser.type("password", "password");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("login should be rejected, error message must be correct",
            browser.isTextPresent("The following errors have occurred:"));
        assertNoErrorsOccurred();
    }
}
