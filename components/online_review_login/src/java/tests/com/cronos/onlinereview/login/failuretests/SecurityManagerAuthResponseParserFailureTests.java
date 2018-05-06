/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.failuretests;

import servletunit.struts.MockStrutsTestCase;

import com.cronos.onlinereview.login.AuthResponseParsingException;
import com.cronos.onlinereview.login.ConfigurationException;
import com.cronos.onlinereview.login.authenticator.SecurityManagerAuthResponseParser;
import com.topcoder.security.TCSubject;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;


/**
 * The failure tests for the SecurityManagerAuthResponseParser class.
 *
 * @author slion
 * @version 1.0
 */
public class SecurityManagerAuthResponseParserFailureTests extends MockStrutsTestCase {
    /**
     * Represents the invalid configuration for constructing SecurityManagerAuthResponseParser.
     */
    private static final String INVALID_CONFIG_FILE =
        "test_files/failure/SecurityManagerAuthResponseParser_Invalid.xml";
    
    /**
     * Represents the invalid configuration for constructing SecurityManagerAuthResponseParser.
     */
    private static final String INVALID_NAMESPACE1 =
        "com.cronos.onlinereview.login.authenticator.SecurityManagerAuthResponseParser.NullLoginBeanName";
    
    /**
     * Represents the invalid configuration for constructing SecurityManagerAuthResponseParser.
     */
    private static final String INVALID_NAMESPACE2 =
        "com.cronos.onlinereview.login.authenticator.SecurityManagerAuthResponseParser.NoLoginBeanName";

    /**
     * Represents the SecurityManagerAuthResponseParser instance.
     */
    private SecurityManagerAuthResponseParser parser;

    /**
     * Represents the Principal instance.
     */
    private Principal principal;

    /**
     * Represents the Response instance.
     */
    private Response authResponse;

    /**
     * Default Constructor.
     * @param testName the name of test.
     */
    public SecurityManagerAuthResponseParserFailureTests(String testName) {
        super(testName);
    }
    
    /**
     * If you override setUp(), you must explicitly call super.setUp()
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        CMHelper.clearCM();
        CMHelper.loadConfig(INVALID_CONFIG_FILE);
        principal = new Principal("test");
        TCSubject subject = new TCSubject(100);
        authResponse = new Response(true, "success", subject);
        parser =
            new SecurityManagerAuthResponseParser(SecurityManagerAuthResponseParser.class.getName());
    }
    
    /**
     * If you override tearDown(), you must explicitly call super.tearDown()
     *
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        CMHelper.clearCM();
        parser = null;
        principal = null;
        response = null;
    }
    
    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig1() throws Exception {
        try {
            new SecurityManagerAuthResponseParser(INVALID_NAMESPACE1);
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }
    
    /**
     * Test constructor when the invalid configuration.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_InvalidConfig2() throws Exception {
        try {
            new SecurityManagerAuthResponseParser(INVALID_NAMESPACE2);
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }

    /**
     * Test constructor when the null namespace.
     * IAE is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_NullNS() throws Exception {
        try {
            new SecurityManagerAuthResponseParser(null);
            fail("Failed to construct.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test constructor when the empty namespace.
     * IAE is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_EmptyNS() throws Exception {
        try {
            new SecurityManagerAuthResponseParser("  ");
            fail("Failed to construct.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test constructor when the unknown namespace.
     * ConfigurationException is expected.
     * @throws Exception to JUnit.
     */
    public void testCtor_UknownNS() throws Exception {
        try {
            new SecurityManagerAuthResponseParser("Unknown");
            fail("Failed to construct.");
        } catch (ConfigurationException e) {
            // pass
        }
    }
    
    /**
     * Test setLoginState method with null authResponse situation.
     * IAE is expected.
     * @throws Exception to JUnit.
     */
    public void testSetLoginState_NullAuthResponse() throws Exception {
        try {
            parser.setLoginState(principal, null, request, response);
            fail("Failed to set login state.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test setLoginState method with null request situation.
     * IAE is expected.
     * @throws Exception to JUnit.
     */
    public void testSetLoginState_NullRequest() throws Exception {
        try {
            parser.setLoginState(principal, authResponse, null, response);
            fail("Failed to set login state.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test setLoginState method with null response situation.
     * IAE is expected.
     * @throws Exception to JUnit.
     */
    public void testSetLoginState_NullResponse() throws Exception {
        try {
            parser.setLoginState(principal, authResponse, request, null);
            fail("Failed to set login state.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test setLoginState method with null response situation.
     * AuthResponseParsingException is expected.
     * @throws Exception to JUnit.
     */
    public void testSetLoginState_AuthResponseParsingException() throws Exception {
        try {
            authResponse = new Response(true, "Not TCSubject object.", new Long(10));
            parser.setLoginState(principal, authResponse, request, response);
            fail("Failed to set login state.");
        } catch (AuthResponseParsingException e) {
            // pass
        }
    }

    /**
     * Test unsetLoginState method with null request situation.
     * IAE is expected.
     * @throws Exception to JUnit.
     */
    public void testUnsetLoginState_NullRequest() throws Exception {
        try {
            parser.unsetLoginState(null, response);
            fail("Failed to set login state.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test unsetLoginState method with null response situation.
     * IAE is expected.
     * @throws Exception to JUnit.
     */
    public void testUnsetLoginState_NullResponse() throws Exception {
        try {
            parser.unsetLoginState(request, null);
            fail("Failed to set login state.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

}
