/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */



package com.cronos.onlinereview.login.cookies;

import com.cronos.onlinereview.login.AuthCookieManagementException;
import com.cronos.onlinereview.login.ConfigurationException;
import com.cronos.onlinereview.login.TestUtil;

import com.topcoder.security.authenticationfactory.Principal;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.sql.Connection;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Unit tests for <code>AuthCookieManagerImpl</code>.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AuthCookieManagerImplTest extends TestCase {

    /**
     * A <code>AuthCookieManagerImpl</code> instance to test against.
     */
    private AuthCookieManagerImpl instance = null;

    /**
     * Represents a <code>Principal</code> instance to help test.
     */
    private Principal principal = null;

    /**
     * The connection to use.
     */
    private Connection con;

    /**
     * The properties to use.
     */
    private Properties properties;

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

        // load config and create parser
        TestUtil.loadAllConfigurations();
        instance = new AuthCookieManagerImpl("com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl");
    }

    /**
     * Tear down. Clear all the configurations.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        TestUtil.clearAllConfigurations();

        properties = null;
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
                     TestUtil.getFieldValue(instance, "userIdentifierKey"));
        assertEquals("Failed to set cookieName.", "topcoderCookie", TestUtil.getFieldValue(instance, "cookieName"));
        assertEquals("Failed to set connectionName.", "MySqlJDBCConnection",
                     TestUtil.getFieldValue(instance, "connectionName"));
        assertNotNull("Failed to set dbConnectionFactory.", TestUtil.getFieldValue(instance, "dbConnectionFactory"));
    }

    /**
     * Test constructor with null namespace parameter.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_NullNamespace() throws Exception {
        try {
            new AuthCookieManagerImpl(null);
            fail("Should throw IllegalArgumentException for null namespace.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test constructor with empty namespace parameter.
     * <p>
     * It should throw IllegalArgumentException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_EmptyNamespace() throws Exception {
        try {
            new AuthCookieManagerImpl("  ");
            fail("Should throw IllegalArgumentException for empty namespace.");
        } catch (IllegalArgumentException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace does not exist.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_UnexistNamespace() throws Exception {
        try {
            new AuthCookieManagerImpl("namespace.does.not.exist");
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
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.authenticator.AuthCookieManagerImpl.no_user_identifer_key");
            fail("Should throw ConfigurationException for un-exist namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace contains empty userIdentifyKey.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_EmptyUserIndentifyKey() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.authenticator.AuthCookieManagerImpl.empty_user_identifer_key");
            fail("Should throw ConfigurationException for un-exist namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace does not contain cookie_name.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_NoCookieName() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.authenticator.AuthCookieManagerImpl.no_cookie_name");
            fail("Should throw ConfigurationException for un-exist namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace contains empty cookie_name.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_EmptyCookieName() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.authenticator.AuthCookieManagerImpl.empty_cookie_name");
            fail("Should throw ConfigurationException for un-exist namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace does not contain db_connection_factory.class.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_NoDBConnectionFactoryClass() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.authenticator.AuthCookieManagerImpl.no_db_connection_factory.class");
            fail("Should throw ConfigurationException for un-exist namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace contains empty db_connection_factory.class.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_EmptyDBConnectionFactoryClass() throws Exception {
        try {
            new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.authenticator.AuthCookieManagerImpl.empty_db_connection_factory.class");
            fail("Should throw ConfigurationException for un-exist namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test constructor when the namespace contains invalid db_connection_factory.class.
     * <p>
     * It should throw ConfigurationException.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConstructor_InvalidDBConnectionFactoryClass() throws Exception {
        try {
            new AuthCookieManagerImpl("com.cronos.onlinereview.login.authenticator."
                                      + "AuthCookieManagerImpl.invalid_db_connection_factory.class");
            fail("Should throw ConfigurationException for un-exist namespace.");
        } catch (ConfigurationException e) {

            // pass
        }
    }

    /**
     * Test setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method.
     * <p>
     * The cookie should be set correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetAuthCookie() throws Exception {
        properties = TestUtil.loadProperties("test_files/db.properties");

        con = TestUtil.createConnection(properties);
        TestUtil.setUpDataBase(con);

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        principal = new Principal("id");
        principal.addMapping("userName", "user1");
        instance.setAuthCookie(principal, request, response);

        List<Cookie> cookies = (List<Cookie>) TestUtil.getFieldValue(response, "cookies");

        assertNotNull("Should have cookie with the specified name", cookies);
        assertEquals("Should have one cookie with the specified name", 1, cookies.size());

        Cookie authCookie = cookies.get(0);

        assertEquals("Should be the same", "topcoderCookie", authCookie.getName());
        assertEquals("Should be the same", "1|2406d589d7d12f34560cee27f68bb47c", authCookie.getValue());
        assertEquals("Should be the same", Integer.MAX_VALUE, authCookie.getMaxAge());
        assertEquals("Should be the same", "localhost", authCookie.getDomain());
        assertEquals("Should be the same", "/", authCookie.getPath());
        TestUtil.clearDataBase(con);
        TestUtil.closeConnection(con);
    }

    /**
     * Test setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method. If principal is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetAuthCookieFailure1() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        try {
            instance.setAuthCookie(null, request, response);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {

            // good
        }
    }

    /**
     * Test setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method. If request is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetAuthCookieFailure2() throws Exception {
        principal = new Principal("id");

        HttpServletResponse response = new MockHttpServletResponse();

        try {
            instance.setAuthCookie(principal, null, response);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {

            // good
        }
    }

    /**
     * Test setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method. If response is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetAuthCookieFailure3() throws Exception {
        principal = new Principal("id");

        HttpServletRequest request = new MockHttpServletRequest();

        try {
            instance.setAuthCookie(principal, request, null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {

            // good
        }
    }

    /**
     * Test setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method. If principal has an invalid
     * 'userName', AuthCookieManagementException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetAuthCookieFailure4() throws Exception {
        principal = new Principal("id");
        principal.addMapping("userName", new Object());

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        try {
            instance.setAuthCookie(principal, request, response);
            fail("AuthCookieManagementException is expected");
        } catch (AuthCookieManagementException e) {

            // good
        }
    }

    /**
     * Test setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method. If principal is null,
     * AuthCookieManagementException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetAuthCookieFailure5() throws Exception {
        instance =
            new AuthCookieManagerImpl("com.cronos.onlinereview.login.cookies.wrongUserPassword_AuthCookieManagerImpl");

        properties = TestUtil.loadProperties("test_files/db.properties");

        con = TestUtil.createConnection(properties);
        TestUtil.setUpDataBase(con);
        principal = new Principal("id");
        principal.addMapping("userName", "user1");

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        try {
            instance.setAuthCookie(principal, request, response);
            fail("AuthCookieManagementException is expected");
        } catch (AuthCookieManagementException e) {

            // good
        } finally {
            TestUtil.clearDataBase(con);
            TestUtil.closeConnection(con);
        }
    }

    /**
     * Test setAuthCookie(Principal, HttpServletRequest, HttpServletResponse) method. If security_user table does NOT
     * have corresponding record, AuthCookieManagementException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetAuthCookieFailure6() throws Exception {
        properties = TestUtil.loadProperties("test_files/db.properties");

        con = TestUtil.createConnection(properties);
        TestUtil.setUpDataBase(con);
        TestUtil.executeSqlFile(con, "test_files/test.prepare/deleteFromSecurity_User.sql");
        principal = new Principal("id");
        principal.addMapping("userName", "user1");

        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        try {
            instance.setAuthCookie(principal, request, response);
            fail("AuthCookieManagementException is expected");
        } catch (AuthCookieManagementException e) {

            // good
        } finally {
            TestUtil.clearDataBase(con);
            TestUtil.closeConnection(con);
        }
    }

    /**
     * Test removeAuthCookie(HttpServletRequest, HttpServletResponse) method.
     * <p>
     * The cookie should be removed correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveAuthCookie() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        instance.removeAuthCookie(request, response);

        List<Cookie> cookies = (List<Cookie>) TestUtil.getFieldValue(response, "cookies");

        assertNotNull("Should have cookie with the specified name", cookies);
        assertEquals("Should have one cookie with the specified name", 1, cookies.size());

        Cookie authCookie = cookies.get(0);

        assertEquals("Should be the same", "topcoderCookie", authCookie.getName());
        assertEquals("Should be the same", 0, authCookie.getMaxAge());
        assertEquals("Should be the same", "localhost", authCookie.getDomain());
        assertEquals("Should be the same", "/", authCookie.getPath());
    }

    /**
     * Test removeAuthCookie(HttpServletRequest, HttpServletResponse) method. If response is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveAuthCookieFailure1() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();

        try {
            instance.removeAuthCookie(request, null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {

            // good
        }
    }

    /**
     * Test removeAuthCookie(HttpServletRequest, HttpServletResponse) method. If request is null,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveAuthCookieFailure2() throws Exception {
        HttpServletResponse response = new MockHttpServletResponse();

        try {
            instance.removeAuthCookie(null, response);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {

            // good
        }
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method.
     * <p>
     * The cookie should be checked correctly. Should return null while request has no cookie.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookie1() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();

        assertNull("Should NOT have cookie with the specified name", instance.checkAuthCookie(request));
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method.
     * <p>
     * The cookie should be checked correctly. Should return null while request does NOT have a cookie with name
     * topcoderCookie.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookie2() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();

        Cookie cookie = new Cookie("topcoderCook", "1|2406d589d7d12f34560cee27f68bb47c");

        TestUtil.setFieldValue(request, "cookies", new Cookie[] { cookie });

        assertNull("Should NOT have cookie with the specified name", instance.checkAuthCookie(request));
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method.
     * <p>
     * The cookie should be checked correctly. Should return null while the cookie in request has null value.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookie3() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();

        Cookie cookie = new Cookie("topcoderCookie", null);

        TestUtil.setFieldValue(request, "cookies", new Cookie[] { cookie });

        assertNull("Should NOT have cookie with the specified name", instance.checkAuthCookie(request));
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method.
     * <p>
     * The cookie should be checked correctly. Should return null while the cookie in request has value with more that
     * two '|'.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookie4() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();

        Cookie cookie = new Cookie("topcoderCookie", "val1|val2|val3");

        TestUtil.setFieldValue(request, "cookies", new Cookie[] { cookie });

        assertNull("Should NOT have cookie with the specified name", instance.checkAuthCookie(request));
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method.
     * <p>
     * The cookie should be checked correctly. Should return null while the cookie in request has userId which is
     * invalid.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookie5() throws Exception {

        HttpServletRequest request = new MockHttpServletRequest();

        Cookie cookie = new Cookie("topcoderCookie", "val1|val3");

        TestUtil.setFieldValue(request, "cookies", new Cookie[] { cookie });

        assertNull("Should NOT have cookie with the specified name", instance.checkAuthCookie(request));
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method.
     * <p>
     * The cookie should be checked correctly. Should return null while the database does NOT have such a record.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookie6() throws Exception {
        properties = TestUtil.loadProperties("test_files/db.properties");
        con = TestUtil.createConnection(properties);
        TestUtil.setUpDataBase(con);
        TestUtil.executeSqlFile(con, "test_files/test.prepare/deleteFromSecurity_User.sql");

        HttpServletRequest request = new MockHttpServletRequest();

        Cookie cookie = new Cookie("topcoderCookie", "1|val3");

        TestUtil.setFieldValue(request, "cookies", new Cookie[] { cookie });

        assertNull("Should NOT have cookie with the specified name", instance.checkAuthCookie(request));

        TestUtil.clearDataBase(con);
        TestUtil.closeConnection(con);
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method.
     * <p>
     * The cookie should be checked correctly. Should return the corresponding user id.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookie7() throws Exception {
        properties = TestUtil.loadProperties("test_files/db.properties");

        con = TestUtil.createConnection(properties);
        TestUtil.setUpDataBase(con);

        HttpServletRequest request = new MockHttpServletRequest();

        request.getSession();

        Cookie cookie = new Cookie("topcoderCookie", "1|2406d589d7d12f34560cee27f68bb47c");

        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setDomain("localhost");
        cookie.setPath("/");

        TestUtil.setFieldValue(request, "cookies", new Cookie[] { cookie });

        assertEquals("The returned user id Should be 1", 1L, (long) instance.checkAuthCookie(request));

        TestUtil.clearDataBase(con);
        TestUtil.closeConnection(con);
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method.
     * <p>
     * The cookie should be checked correctly. Should return null, since the password hash is wrong.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookie8() throws Exception {
        properties = TestUtil.loadProperties("test_files/db.properties");

        con = TestUtil.createConnection(properties);
        TestUtil.setUpDataBase(con);

        HttpServletRequest request = new MockHttpServletRequest();

        request.getSession();

        Cookie cookie = new Cookie("topcoderCookie", "1|2406d589d7d12f34560cee27f68c");

        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setDomain("localhost");
        cookie.setPath("/");

        TestUtil.setFieldValue(request, "cookies", new Cookie[] { cookie });

        assertNull("Should NOT have cookie with the specified name", instance.checkAuthCookie(request));

        TestUtil.clearDataBase(con);
        TestUtil.closeConnection(con);
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method. If request is null, IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookieFailure1() throws Exception {
        try {
            instance.checkAuthCookie(null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {

            // good
        }
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method. If the database connection can NOT be created,
     * AuthCookieManagementException is expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookieFailure2() throws Exception {
        try {
            instance = new AuthCookieManagerImpl(
                "com.cronos.onlinereview.login.cookies.wrongUserPassword_AuthCookieManagerImpl");

            HttpServletRequest request = new MockHttpServletRequest();

            Cookie cookie = new Cookie("topcoderCookie", "1|val3");

            TestUtil.setFieldValue(request, "cookies", new Cookie[] { cookie });

            instance.checkAuthCookie(request);

            fail("AuthCookieManagementException is expected");
        } catch (AuthCookieManagementException e) {

            // good
        }
    }

    /**
     * Test checkAuthCookie(HttpServletResponse) method. If the table does NOT exist, AuthCookieManagementException is
     * expected.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckAuthCookieFailure3() throws Exception {
        try {
            properties = TestUtil.loadProperties("test_files/db.properties");

            con = TestUtil.createConnection(properties);
            TestUtil.executeSqlFile(con, "test_files/test.prepare/dropSecurity_User.sql");

            HttpServletRequest request = new MockHttpServletRequest();

            request.getSession();

            Cookie cookie = new Cookie("topcoderCookie", "1|2406d589d7d12f34560cee27f68bb47c");

            cookie.setMaxAge(Integer.MAX_VALUE);
            cookie.setDomain("localhost");
            cookie.setPath("/");

            TestUtil.setFieldValue(request, "cookies", new Cookie[] { cookie });

            instance.checkAuthCookie(request);
            fail("AuthCookieManagementException is expected");
        } catch (AuthCookieManagementException e) {

            // good
        } finally {
            TestUtil.setUpDataBase(con);
            TestUtil.closeConnection(con);
        }
    }
}
