/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.accuracytests;

import javax.servlet.http.HttpSession;

import servletunit.struts.MockStrutsTestCase;

import com.cronos.onlinereview.login.authenticator.SecurityManagerAuthResponseParser;
import com.topcoder.security.TCSubject;
import com.topcoder.security.authenticationfactory.Principal;
import com.topcoder.security.authenticationfactory.Response;


/**
 * Accuracy tests for class <code>SecurityManagerAuthResponseParser</code>.
 *
 * @author PE
 * @version 1.0
 */
public class SecurityManagerAuthResponseParserAccuracyTest extends MockStrutsTestCase {
    /** Represents the <code>SecurityManagerAuthResponseParser</code> instance for testing. */
    private SecurityManagerAuthResponseParser parser = null;

    /** Represents the <code>Principal</code> instance for testing. */
    private Principal principal = null;

    /** Represents the <code>Response</code> instance for testing. */
    private Response authResponse = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        AccuracyTestHelper.clearConfig();

        principal = new Principal("principal");

        TCSubject subject = new TCSubject(123);
        authResponse = new Response(true, "message", subject);

        // creating the testing instance
        AccuracyTestHelper.addConfig();
        parser = new SecurityManagerAuthResponseParser(SecurityManagerAuthResponseParser.class.getName());
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        AccuracyTestHelper.clearConfig();
        super.tearDown();
    }

    /**
     * <p>
     * Tests the accuracy of constructor SecurityManagerAuthResponseParser(String).
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testSecurityManagerAuthResponseParser_Accuracy() throws Exception {
        assertEquals("The userIdentifierKey is not properly set.", "topcoder",
            AccuracyTestHelper.getPrivateField(parser.getClass(), parser, "userIdentifierKey"));
    }

    /**
     * <p>
     * Tests the accuracy of method setLoginState(Principal, Response, HttpServletRequest, HttpServletResponse) when
     * the given authResponse is unsuccessful.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testSetLoginState_UnsuccessfulResponseAccuracy() throws Exception {
        parser.setLoginState(principal, new Response(false), request, response);
        assertEquals("No session attribute should be set.", null, request.getSession(true).getAttribute("topcoder"));
    }

    /**
     * <p>
     * Tests the accuracy of method setLoginState(Principal, Response, HttpServletRequest, HttpServletResponse) when
     * the given authResponse is successful.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testSetLoginState_SuccessfulResponseAccuracy1() throws Exception {
        parser.setLoginState(principal, authResponse, request, response);
        assertEquals("The userid should be add to the session attribute.", new Long(123),
            request.getSession().getAttribute("topcoder"));
    }

    /**
     * <p>
     * Tests the accuracy of method setLoginState(Principal, Response, HttpServletRequest, HttpServletResponse) when
     * the given authResponse is successful.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testSetLoginState_SuccessfulResponseAccuracy2() throws Exception {
        HttpSession oldSession = request.getSession();
        parser.setLoginState(principal, authResponse, request, response);

        try {
            oldSession.getAttribute("dummy");
            fail("Old session should be invalidated.");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Tests the accuracy of method unsetLoginState(HttpServletRequest, HttpServletResponse).
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testUnsetLoginState_Accuracy() throws Exception {
        request.getSession();
        parser.unsetLoginState(request, response);

        HttpSession session = request.getSession(false);

        assertNull("Session will be invalidated.", session);
    }
}
