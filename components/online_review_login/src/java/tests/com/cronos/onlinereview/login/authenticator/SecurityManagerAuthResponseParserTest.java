/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */



package com.cronos.onlinereview.login.authenticator;

import com.cronos.onlinereview.login.AuthResponseParsingException;
import com.cronos.onlinereview.login.ConfigurationException;
import com.cronos.onlinereview.login.TestUtil;

import com.topcoder.security.TCSubject;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;

import servletunit.struts.MockStrutsTestCase;

import javax.servlet.http.HttpSession;

/**
 * Unit tests for <code>SecurityManagerAuthResponseParser</code>.
 *
 * @author maone
 * @version 1.0
 */
public class SecurityManagerAuthResponseParserTest extends MockStrutsTestCase {

    /**
     * Represents a <code>Response</code> instance to help test.
     */
    private Response authResponse = null;

    /**
     * A <code>SecurityManagerAuthResponseParser</code> instance to test against.
     */
    private SecurityManagerAuthResponseParser parser = null;

    /**
     * Represents a <code>Principal</code> instance to help test.
     */
    private Principal principal = null;

    /**
     * Set up.
     * <p>
     * It will create all the helping objects to test, and load the configurations.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        // create helping objects.
        principal = new Principal("id");

        TCSubject subject = new TCSubject(12345);

        authResponse = new Response(true, "message", subject);

        // load config and create parser
        TestUtil.loadAllConfigurations();
        parser = new SecurityManagerAuthResponseParser(
            "com.cronos.onlinereview.login.authenticator.SecurityManagerAuthResponseParser");
    }

    /**
     * Tear down. Clear all the configurations.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        TestUtil.clearAllConfigurations();

        super.tearDown();
    }

    /**
     * Test constructor to validate the <code>userIdentifierKey</code> field is set.
     * <p>
     * The value should be set with the value in config file.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_Valid() throws Exception {
        assertEquals("Failed to set userIdentifierKey.", "topcoder",
                     TestUtil.getFieldValue(parser, "userIdentifierKey"));
    }

    /**
     * Test constructor with null namespace param.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_NullNamespace() throws Exception {
        try {
            new SecurityManagerAuthResponseParser(null);
            fail("Should throw IllegalArgumentException for null namespace.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test constructor with empty namespace param.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_EmptyNamespace() throws Exception {
        try {
            new SecurityManagerAuthResponseParser("  ");
            fail("Should throw IllegalArgumentException for empty namespace.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace doesnot exist.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_UnexistNamespace() throws Exception {
        try {
            new SecurityManagerAuthResponseParser("namespace.does.not.exist");
            fail("Should throw ConfigurationException for un-exist namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace does not contain userIdentifyKey.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_NoUserIndentifyKey() throws Exception {
        try {
            new SecurityManagerAuthResponseParser(
                "com.cronos.onlinereview.login.authenticator.SecurityManagerAuthResponseParser.invalid");
            fail("Should throw ConfigurationException for un-exist namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test <code>setLoginState</code> when the given <code>authResponse</code> is unsuccessful.
     * <p>
     * The method will return directly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetLoginState_UnsuccessfulResponse() throws Exception {
        authResponse = new Response(false);

        parser.setLoginState(principal, authResponse, request, response);
        assertEquals("No session attribute should be set.", null, request.getSession(true).getAttribute("topcoder"));
    }

    /**
     * Test <code>setLoginState</code> when the given <code>authResponse</code> contains non-TCSubject details.
     * <p>
     * The method will throw AuthResponseParsingException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetLoginState_InvalidDetails() throws Exception {
        authResponse = new Response(true, "message", "details");

        try {
            parser.setLoginState(principal, authResponse, request, response);
            fail("Should throw AuthResponseParsingException for invalid details.");
        } catch (AuthResponseParsingException e) {

            // pass
        }
    }

    /**
     * Test <code>setLoginState</code> to see an attribute is added to the session.
     * <p>
     * An attribute will be added to the reuqest session.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetLoginState_ValidateAttribute() throws Exception {
        parser.setLoginState(principal, authResponse, request, response);
        assertEquals("The userid should be add to the session attribute.", new Long(12345),
                     request.getSession().getAttribute("topcoder"));
    }

    /**
     * Test <code>setLoginState</code> to see a new session is created.
     * <p>
     * The old session will be invalidated.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetLoginState_ValidateSession() throws Exception {
        HttpSession oldSession = request.getSession();

        parser.setLoginState(principal, authResponse, request, response);

        HttpSession newSession = request.getSession(false);

        assertNotSame("New session should be created.", newSession, oldSession);

        try {
            oldSession.getAttribute("dummy");
            fail("Old session should be invalidated.");
        } catch (IllegalStateException e) {

            // pass
        }
    }

    /**
     * Test <code>setLoginState</code> with null principal param.
     * <p>
     * The null param will be ignored.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetLoginState_NullPrincipal() throws Exception {
        parser.setLoginState(null, authResponse, request, response);
        assertEquals("Null principal should be ignored.", new Long(12345),
                     request.getSession().getAttribute("topcoder"));
    }

    /**
     * Test <code>setLoginState</code> with null authResponse param.
     * <p>
     * This method will throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetLoginState_NullAuthResponse() throws Exception {
        try {
            parser.setLoginState(principal, null, request, response);
            fail("Should throw IllegalArgumentException for null authResponse.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test <code>setLoginState</code> with null request param.
     * <p>
     * This method will throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetLoginState_NullRequest() throws Exception {
        try {
            parser.setLoginState(principal, authResponse, null, response);
            fail("Should throw IllegalArgumentException for null request.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test <code>setLoginState</code> with null response param.
     * <p>
     * This method will throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetLoginState_NullResponse() throws Exception {
        try {
            parser.setLoginState(principal, authResponse, request, null);
            fail("Should throw IllegalArgumentException for null response.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test <code>unsetLoginState</code> when there is no session associated with the request.
     * <p>
     * Nothing will happen in this case.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUnsetLoginState_NoSession() throws Exception {
        parser.unsetLoginState(request, response);
        assertNull("The session should be kept as null.", request.getSession(false));
    }

    /**
     * Test <code>unsetLoginState</code> when there is session associated with the request.
     * <p>
     * The session will be invalidated.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUnsetLoginState_WithSession() throws Exception {
        HttpSession oldSession = request.getSession();

        parser.unsetLoginState(request, response);

        try {
            oldSession.getAttribute("dummy");
            fail("Session will be invalidated.");
        } catch (IllegalStateException e) {

            // pass
        }
    }

    /**
     * Test <code>unsetLoginState</code> with null request param.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUnsetLoginState_NullRequest() throws Exception {
        try {
            parser.unsetLoginState(null, response);
            fail("Should throw IllegalArgumentException for null request.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test <code>unsetLoginState</code> with null response param.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testUnsetLoginState_NullResponse() throws Exception {
        try {
            parser.unsetLoginState(request, null);
            fail("Should throw IllegalArgumentException for null response.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }
}
