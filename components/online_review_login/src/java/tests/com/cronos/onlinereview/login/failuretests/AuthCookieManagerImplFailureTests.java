/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.failuretests;

import javax.servlet.http.Cookie;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.cronos.onlinereview.login.AuthCookieManagementException;
import com.cronos.onlinereview.login.ConfigurationException;
import com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl;
import com.topcoder.security.authenticationfactory.Principal;

/**
 * <p>
 * Failure tests for the {@link AuthCookieManagerImpl} class.
 * </p>
 *
 * @author akinwale
 * @version 1.1
 */
public class AuthCookieManagerImplFailureTests extends TestCase {
    /**
     * <p>
     * The {@link AuthCookieManagerImpl} instance to be tested.
     * </p>
     */
    private AuthCookieManagerImpl manager;

    /**
     * <p>
     * Setup for each unit test in the test case.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        CMHelper.clearCM();
        CMHelper.loadConfig("test_files/failure/OnlineReviewLogin-1.1.xml");
        manager = new AuthCookieManagerImpl("com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl");
    }

    /**
     * <p>
     * Cleanup after each unit test in the test case.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        CMHelper.clearCM();
    }

    /**
     * <p>
     * Tests that the AuthCookieManagerImpl(String) constructor handles a null parameter by throwing
     * {@link IllegalArgumentException}.
     * </p>
     *
     * <p>
     * {@link IllegalArgumentException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCtor_Failure_NullParam() throws Exception {
        try {
            new AuthCookieManagerImpl(null);
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the AuthCookieManagerImpl(String) constructor handles an empty string parameter
     * value by throwing {@link IllegalArgumentException}.
     * </p>
     *
     * <p>
     * {@link IllegalArgumentException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCtor_Failure_EmptyStringParam() throws Exception {
        try {
            new AuthCookieManagerImpl("");
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the AuthCookieManagerImpl(String) constructor handles a case where the specified
     * namespace does not exist by throwing {@link ConfigurationException}.
     * </p>
     *
     * <p>
     * {@link ConfigurationException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCtor_Failure_MissingNamespace() throws Exception {
        try {
            new AuthCookieManagerImpl("com.namespace.does.not.exist");
            fail("ConfigurationException was expected");
        } catch (ConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the AuthCookieManagerImpl(String) constructor handles a case where the
     * "user_identifier_key" configuration value has not been specified by throwing
     * {@link ConfigurationException}.
     * </p>
     *
     * <p>
     * {@link ConfigurationException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCtor_Failure_MissingUserIdentifierKey() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl.MissingUserIdentifierKey");
            fail("ConfigurationException was expected");
        } catch (ConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the AuthCookieManagerImpl(String) constructor handles a case where the
     * "cookie_name" configuration value has not been specified by throwing
     * {@link ConfigurationException}.
     * </p>
     *
     * <p>
     * {@link ConfigurationException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCtor_Failure_MissingCookieName() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl.MissingCookieName");
            fail("ConfigurationException was expected");
        } catch (ConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the AuthCookieManagerImpl(String) constructor handles a case where the
     * "db_connection_factory.class" configuration value has not been specified by throwing
     * {@link ConfigurationException}.
     * </p>
     *
     * <p>
     * {@link ConfigurationException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCtor_Failure_MissingDbConnectionFactoryClass() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl.MissingDbConnFactoryClass");
            fail("ConfigurationException was expected");
        } catch (ConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the AuthCookieManagerImpl(String) constructor handles a case where the
     * "user_identifier_key" configuration value is an empty string by throwing
     * {@link ConfigurationException}.
     * </p>
     *
     * <p>
     * {@link ConfigurationException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCtor_Failure_EmptyStringUserIdentifierKey() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl.EmptyStringUserIdentifierKey");
            fail("ConfigurationException was expected");
        } catch (ConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the AuthCookieManagerImpl(String) constructor handles a case where the
     * "cookie_name" configuration value is an empty string by throwing
     * {@link ConfigurationException}.
     * </p>
     *
     * <p>
     * {@link ConfigurationException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCtor_Failure_EmptyStringCookieName() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl.EmptyStringCookieName");
            fail("ConfigurationException was expected");
        } catch (ConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the AuthCookieManagerImpl(String) constructor handles a case where the
     * "db_connection_factory.class" configuration value is an empty string by throwing
     * {@link ConfigurationException}.
     * </p>
     *
     * <p>
     * {@link ConfigurationException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCtor_Failure_EmptyStringDbConnectionFactoryClass() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl.EmptyStringDbConnFactoryClass");
            fail("ConfigurationException was expected");
        } catch (ConfigurationException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method
     * handles a null value set for the first parameter by throwing {@link IllegalArgumentException}
     * .
     * </p>
     *
     * <p>
     * {@link IllegalArgumentException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testSetAuthCookie_Failure_FirstParamNull() throws Exception {
        try {
            manager.setAuthCookie(null, new MockHttpServletRequest(), new MockHttpServletResponse());
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method
     * handles a null value set for the second parameter by throwing
     * {@link IllegalArgumentException} .
     * </p>
     *
     * <p>
     * {@link IllegalArgumentException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testSetAuthCookie_Failure_SecondParamNull() throws Exception {
        try {
            manager.setAuthCookie(new Principal("id"), null, new MockHttpServletResponse());
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method
     * handles a null value set for the third parameter by throwing
     * {@link IllegalArgumentException} .
     * </p>
     *
     * <p>
     * {@link IllegalArgumentException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testSetAuthCookie_Failure_ThirdParamNull() throws Exception {
        try {
            manager.setAuthCookie(new Principal("id"), new MockHttpServletRequest(), null);
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method
     * handles a case where the principal.getValue("userName") call returns an empty string by
     * throwing {@link IllegalArgumentException} .
     * </p>
     *
     * <p>
     * {@link IllegalArgumentException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testSetAuthCookie_Failure_PrincipalUserNameEmptyString() throws Exception {
        Principal principal = new Principal("id");
        principal.addMapping("userName", "");
        try {
            manager.setAuthCookie(principal, new MockHttpServletRequest(), new MockHttpServletResponse());
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method
     * handles a case where the principal username value does not exist in the DB by throwing
     * {@link AuthCookieManagementException}.
     * </p>
     *
     * <p>
     * {@link AuthCookieManagementException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testSetAuthCookie_Failure_ManagementException() throws Exception {
        Principal principal = new Principal("id");
        principal.addMapping("userName", "invalid");
        try {
            manager.setAuthCookie(principal, new MockHttpServletRequest(), new MockHttpServletResponse());
            fail("AuthCookieManagementException was expected");
        } catch (AuthCookieManagementException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the removeAuthCookie(HttpServletRequest, HttpServletResponse) method handles a
     * null value set for the first parameter by throwing {@link IllegalArgumentException}.
     * </p>
     *
     * <p>
     * {@link IllegalArgumentException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testRemoveAuthCookie_Failure_FirstParamNull() throws Exception {
        try {
            manager.removeAuthCookie(null, new MockHttpServletResponse());
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the removeAuthCookie(HttpServletRequest, HttpServletResponse) method handles a
     * null value set for the second parameter by throwing {@link IllegalArgumentException}.
     * </p>
     *
     * <p>
     * {@link IllegalArgumentException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testRemoveAuthCookie_Failure_SecondParamNull() throws Exception {
        try {
            manager.removeAuthCookie(new MockHttpServletRequest(), null);
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the checkAuthCookie(HttpServletRequest) method handles a null parameter by
     * throwing {@link IllegalArgumentException}.
     * </p>
     *
     * <p>
     * {@link IllegalArgumentException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCheckAuthCookie_Failure_NullParam() throws Exception {
        try {
            manager.checkAuthCookie(null);
            fail("IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * <p>
     * Tests that the checkAuthCookie(HttpServletRequest) method handles a case where a critical
     * error occurred while trying to check the authentication cookie from the servlet request by
     * throwing {@link AuthCookieManagementException}.
     * </p>
     *
     * <p>
     * {@link AuthCookieManagementException} is expected.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testCheckAuthCookie_Failure_ManagementException() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("cookie1", "value1"), new Cookie("topcoderCookie", "42|hash"));

        try {
            manager = new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl.InvalidDB");
            manager.checkAuthCookie(request);
            fail("AuthCookieManagementException was expected");
        } catch (AuthCookieManagementException e) {
            // success
        }
    }
}
