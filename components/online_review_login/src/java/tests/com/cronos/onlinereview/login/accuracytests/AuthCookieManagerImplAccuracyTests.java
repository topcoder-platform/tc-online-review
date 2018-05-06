/*
 * Copyright (C) 2006-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.accuracytests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import servletunit.struts.MockStrutsTestCase;

import com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.security.authenticationfactory.Principal;


/**
 * Accuracy tests for class <code>AuthCookieManagerImpl</code>.
 *
 * @author onsky
 * @version 1.1
 */
public class AuthCookieManagerImplAccuracyTests extends MockStrutsTestCase {

	/**
	 * Represents the instance to be tested.
	 */
	private AuthCookieManagerImpl instance;

	/**
	 * Represents the Connection instance to be used.
	 */
	private Connection connection;
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

        AccuracyTestHelper.addConfig();
        instance = new AuthCookieManagerImpl("com.cronos.onlinereview.login.cookies.AuthCookieManagerImpl");
    	DBConnectionFactory factory = (DBConnectionFactory) AccuracyTestHelper.getPrivateField(instance.getClass(), instance, "dbConnectionFactory");
    	connection = factory.createConnection();
    	executeScript("test_files/accuracy/clear.sql", connection);
    	executeScript("test_files/accuracy/insert.sql", connection);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
    	executeScript("test_files/accuracy/clear.sql", connection);
        AccuracyTestHelper.clearConfig();
        instance = null;
        super.tearDown();
    }

    /**
     * <p>
     * Tests the accuracy of constructor AuthCookieManagerImpl(String).
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testAuthCookieManagerImpl_Accuracy() throws Exception {
        assertTrue("Failed to initialize connectionName.",
                AccuracyTestHelper.getPrivateField(instance.getClass(), instance, "connectionName") instanceof String);
        assertTrue("Failed to initialize cookieName.",
                AccuracyTestHelper.getPrivateField(instance.getClass(), instance, "cookieName") instanceof String);
        assertTrue("Failed to initialize dbConnectionFactory.",
                AccuracyTestHelper.getPrivateField(instance.getClass(), instance, "dbConnectionFactory") instanceof DBConnectionFactory);
        assertTrue("Failed to initialize userIdentifierKey.",
                AccuracyTestHelper.getPrivateField(instance.getClass(), instance, "userIdentifierKey") instanceof String);
    }

    /**
     * <p>
     * Tests the accuracy of method setAuthCookie(HttpServletRequest).
     * </p>
     *
     * @throws Exception if any error
     */
	public void testsetAuthCookie_Accuracy() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();

        Principal principal = new Principal("id");
        principal.addMapping("userName", "user2");
        instance.setAuthCookie(principal, request, response);

        List cookies = (List) AccuracyTestHelper.getPrivateField(response.getClass(), response, "cookies");
        assertEquals("one cookie must exist", 1, cookies.size());

        Cookie authCookie = (Cookie) cookies.get(0);

        assertEquals("cookie name must be correct", "accuracyTests", authCookie.getName());
        assertEquals("cookie value must be correct", "2|26c6e61a0c7e69eec953691e2cab927f", authCookie.getValue());
    }

    /**
     * <p>
     * Tests the accuracy of method checkAuthCookie(HttpServletRequest).
     * </p>
     *
     * @throws Exception if any error
     */
    public void testCheckAuthCookie_Accuracy1() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();

        assertNull("no user is found, hence it will return null", instance.checkAuthCookie(request));
    }

    /**
     * <p>
     * Tests the accuracy of method checkAuthCookie(HttpServletRequest).
     * </p>
     *
     * @throws Exception if any error
     */
    public void testCheckAuthCookie_Accuracy2() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Principal principal = new Principal("id");
        principal.addMapping("userName", "user2");
        instance.setAuthCookie(principal, request, response);

        request = new MockHttpServletRequest();

        request.getSession();

        Cookie cookie = new Cookie("accuracyTests", "2|26c6e61a0c7e69eec953691e2cab927f");
        AccuracyTestHelper.setPrivateField(request.getClass(), request, "cookies", new Cookie[] {cookie});

        assertEquals("user2 should return", 2L, (long) instance.checkAuthCookie(request));
    }

    /**
     * <p>
     * Tests the accuracy of method removeAuthCookie(HttpServletRequest).
     * </p>
     *
     * @throws Exception if any error
     */
	public void testremoveAuthCookie_Accuracy1() throws Exception {
        // set cookie first
    	HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Principal principal = new Principal("id");
        principal.addMapping("userName", "user2");
        instance.setAuthCookie(principal, request, response);
    	request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        // remove the cookie
        instance.removeAuthCookie(request, response);

        List cookies = (List) AccuracyTestHelper.getPrivateField(response.getClass(), response, "cookies");

        Cookie authCookie = (Cookie) cookies.get(0);

        assertEquals("cookie name must be correct", "accuracyTests", authCookie.getName());
        assertEquals("cookie is no longer active", 0, authCookie.getMaxAge());
    }

    /**
     * <p>Executes the sql file.</p>
     *
     * @param fileName The file to be loaded.
     * @param em the EntityManager instance
     *
     * @throws Exception if any error occurs
     */
    public static void executeScript(String fileName, Connection con)
        throws Exception {
    	BufferedReader reader = null;
        try {
        	reader = new BufferedReader(new FileReader(fileName));

            String line;
            StringBuffer sb = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Statement st = con.createStatement();

            String[] sqls = sb.toString().split(";");

            for (String sql : sqls) {
                if (!(sql.trim().trim().length() == 0)) {
                    st.addBatch(sql);
                }
            }

            st.executeBatch();
        } finally {
            if (reader != null) {
                try {
                	reader.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
