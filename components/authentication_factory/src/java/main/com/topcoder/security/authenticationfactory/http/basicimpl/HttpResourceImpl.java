/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory.http.basicimpl;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.topcoder.security.authenticationfactory.AuthenticateException;
import com.topcoder.security.authenticationfactory.http.HttpCookie;
import com.topcoder.security.authenticationfactory.http.HttpResource;


/**
 * <p>
 * Particular implementation of HttpResource interface. Mantains reference to HTTP connection with
 * server.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class HttpResourceImpl implements HttpResource {
    /**
     * <p>
     * Represents the property name of 'Set-Cookie' used to get the content of
     * cookie in the http head field.
     * </p>
     */
    private static final String SETCOOKIE = "Set-Cookie";

    /**
     * <p>
     * Reference to HTTP connection with server. Whenever user requests some resource information
     * such as contents type or header field the request will be delegated to this object.
     * </p>
     */
    private HttpURLConnection connection = null;

    /**
     * <p>
     * String representation of original resource URL. Note, that actual URL may differ from
     * original one if HTTP redirection occured.
     * </p>
     */
    private String originalURL = null;

    /**
     * <p>
     * Constructs resource object for given HTTP connection.
     * </p>
     *
     * @param conn connection to use
     * @param originalURL original URL for this resource
     *
     * @throws NullPointerException if any of parameters is null
     */
    public HttpResourceImpl(HttpURLConnection conn, String originalURL) {
        if (conn == null) {
            throw new NullPointerException("The conn is null.");
        }

        if (originalURL == null) {
            throw new NullPointerException("The originalURL is null.");
        }

        this.connection = conn;
        this.originalURL = originalURL;
    }

    /**
     * <p>
     * Returns HTTP connection for this resource. Returned connection object represents opened and
     * authenticated HTTP connection with server.
     * </p>
     *
     * @return connection with server
     */
    public HttpURLConnection getHttpConnection() {
        return connection;
    }

    /**
     * <p>
     * Returns original URL for this HTTP resource. Original URL may differ from actual one if HTTP
     * redirection occured.
     * </p>
     *
     * @return original resource URL
     */
    public String getOriginalURL() {
        return originalURL;
    }

    /**
     * <p>
     * Returns actual URL for this HTTP resource. Actual URL may differ from original one if HTTP
     * redirection occured.
     * </p>
     *
     * @return actual resource URL
     */
    public String getActualURL() {
        return connection.getURL().toExternalForm();
    }

    /**
     * <p>
     * Returns content type for this HTTP resource. This function is equivalent to
     * getHttpConnection().getContentType().
     * </p>
     *
     * @return server response contents MIME type or null if content-type is not known
     */
    public String getContentType() {
        return connection.getContentType();
    }

    /**
     * <p>
     * Returns contents object for this HTTP resource. This function is equivalent to
     * getHttpConnection().getContent(). Check java.net package documentation for description of
     * content object creation.
     * </p>
     *
     * @return object representing contents
     *
     * @throws IOException if exception occur during contents retrieval
     */
    public Object getContent() throws IOException {
        return connection.getContent();
    }

    /**
     * <p>
     * Returns header field by its name. Returns null if there is no such field in the HTTP header.
     * This function is equivalent to getHttpConnection().getHeaderFiled(name).
     * </p>
     *
     * @param name name of field to retrieve
     *
     * @return field value or null if there is no such field in the header
     *
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public String getHeaderField(String name) {
        if (name == null) {
            throw new NullPointerException("The name is null.");
        }

        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("The name is empty string");
        }
        return connection.getHeaderField(name);
    }

    /**
     * <p>
     * Returns cookies provided by server i.e. value of &quot;Set-Cookie&quot; header field from
     * server response. Cookie is returned as raw (non-parsed) string. If no cookies were provided
     * by server, this function will return null.
     * </p>
     *
     * <p>
     * This function is equivalent to getHeaderFiled(&quot;Set-Cookie&quot;).
     * </p>
     *
     * @return server cookies or null if there are no such
     */
    public String getSetCookie() {
        return connection.getHeaderField(SETCOOKIE);
    }

    /**
     * <p>
     * Returns parsed representation of HTTP server cookie as a HttpCookie object or null if no
     * cookie was provided by server.
     * </p>
     *
     * @return parsed server cookie or null if there is no cookie
     *
     * @throws AuthenticateException if error occured during cookie parsing
     */
    public HttpCookie getCookie() throws AuthenticateException {
        String cookie = getSetCookie();
        if (cookie == null) {
            return null;
        }

        try {
            return new HttpCookie(cookie);
        } catch (IllegalArgumentException iae) {
            throw new AuthenticateException("Create HttpCookie fails", iae);
        }
    }
}
