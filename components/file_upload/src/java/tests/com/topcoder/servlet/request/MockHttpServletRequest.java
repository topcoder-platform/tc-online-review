/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.security.Principal;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * <p>
 * A mock class which implements the <code>HttpServletRequest</code> interface for testing.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class MockHttpServletRequest implements HttpServletRequest {
    /** Represents the request data which will be hold by the ServletRequest. */
    private byte[] requestData;

    /** Represents the value of the content-type. */
    private String strContentType;

    /** Represents the header of the ServletRequest. */
    private Map headers = new HashMap();

    /**
     * Creates a new MockHttpServletRequest object.
     *
     * @param requestData the request data which will be hold by the ServletRequest.
     * @param strContentType the value of the content-type.
     */
    public MockHttpServletRequest(final byte[] requestData, final String strContentType) {
        this.requestData = requestData;
        this.strContentType = strContentType;
        this.headers.put("Content-type", strContentType);
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getAuthType()
     */
    public String getAuthType() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    public Cookie[] getCookies() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getDateHeader(String)
     */
    public long getDateHeader(String arg0) {
        return 0;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getHeader(String)
     */
    public String getHeader(String headerName) {
        return (String) headers.get(headerName);
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getHeaders(String)
     */
    public Enumeration getHeaders(String arg0) {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
     */
    public Enumeration getHeaderNames() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getIntHeader(String)
     */
    public int getIntHeader(String arg0) {
        return 0;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    public String getMethod() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    public String getPathInfo() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
     */
    public String getPathTranslated() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     */
    public String getContextPath() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
    public String getQueryString() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
     */
    public String getRemoteUser() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(String)
     */
    public boolean isUserInRole(String arg0) {
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
     */
    public Principal getUserPrincipal() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
     */
    public String getRequestedSessionId() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRequestURI()
     */
    public String getRequestURI() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getRequestURL()
     */
    public StringBuffer getRequestURL() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
     */
    public String getServletPath() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
     */
    public HttpSession getSession(boolean arg0) {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#getSession()
     */
    public HttpSession getSession() {
        return null;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
     */
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
     */
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    /**
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
     */
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    /**
     * A mock method which does nothing.
     *
     * @return always false.
     *
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
     * @deprecated
     */
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    /**
     * @see javax.servlet.ServletRequest#getAttribute(String)
     */
    public Object getAttribute(String arg0) {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    public Enumeration getAttributeNames() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    public String getCharacterEncoding() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#setCharacterEncoding(String)
     */
    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
    }

    /**
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    public int getContentLength() {
        int iLength = 0;

        if (null == requestData) {
            iLength = -1;
        } else {
            iLength = requestData.length;
        }

        return iLength;
    }

    /**
     * @see javax.servlet.ServletRequest#getContentType()
     */
    public String getContentType() {
        return strContentType;
    }

    /**
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream sis = new MyServletInputStream(requestData);

        return sis;
    }

    /**
     * @see javax.servlet.ServletRequest#getParameter(String)
     */
    public String getParameter(String arg0) {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    public Enumeration getParameterNames() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getParameterValues(String)
     */
    public String[] getParameterValues(String arg0) {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    public Map getParameterMap() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    public String getProtocol() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getScheme()
     */
    public String getScheme() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getServerName()
     */
    public String getServerName() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getServerPort()
     */
    public int getServerPort() {
        return 0;
    }

    /**
     * @see javax.servlet.ServletRequest#getReader()
     */
    public BufferedReader getReader() throws IOException {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    public String getRemoteAddr() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    public String getRemoteHost() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#setAttribute(String, Object)
     */
    public void setAttribute(String arg0, Object arg1) {
    }

    /**
     * @see javax.servlet.ServletRequest#removeAttribute(String)
     */
    public void removeAttribute(String arg0) {
    }

    /**
     * @see javax.servlet.ServletRequest#getLocale()
     */
    public Locale getLocale() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocales()
     */
    public Enumeration getLocales() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#isSecure()
     */
    public boolean isSecure() {
        return false;
    }

    /**
     * @see javax.servlet.ServletRequest#getRequestDispatcher(String)
     */
    public RequestDispatcher getRequestDispatcher(String arg0) {
        return null;
    }

    /**
     * A mock method which does nothing.
     *
     * @param name the path name.
     *
     * @return always null here.
     *
     * @see javax.servlet.ServletRequest#getRealPath(String)
     * @deprecated
     */
    public String getRealPath(String name) {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getRemotePort()
     */
    public int getRemotePort() {
        return 0;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalName()
     */
    public String getLocalName() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalAddr()
     */
    public String getLocalAddr() {
        return null;
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalPort()
     */
    public int getLocalPort() {
        return 0;
    }

    /**
     * <p>
     * A mock class which implements the <code>ServletInputStream</code> interface to hold the byte array data for
     * testing.
     * </p>
     */
    private static class MyServletInputStream extends ServletInputStream {
        /** Represents the byte array to hold. */
        private ByteArrayInputStream bais;

        /**
         * Creates a new MyServletInputStream instance with given byte array data to hold.
         *
         * @param data the byte array to hold.
         */
        public MyServletInputStream(byte[] data) {
            bais = new ByteArrayInputStream(data);
        }

        /**
         * Reads from the byte array.
         *
         * @return the count of the bytes has been read.
         */
        public int read() {
            return bais.read();
        }
    }
}
