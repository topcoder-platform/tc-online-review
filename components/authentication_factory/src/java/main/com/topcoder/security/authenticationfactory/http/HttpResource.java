/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory.http;

import java.io.IOException;

import com.topcoder.security.authenticationfactory.AuthenticateException;


/**
 * <p>
 * Represents a HTTP resource wrapper. Defines&nbsp; several methods to retrieve different
 * information about  HTTP server response such as HTTP header fields, server cookies, redirection
 * URL, content type and content itself.  Web authenticators can use this interface to provide
 * common access to returned HTTP resources.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public interface HttpResource {
    /**
     * <p>
     * Returns original URL for HTTP resource. Note that original URL can differ from the actual
     * URL if and only if HTTP redirection occurred.
     * </p>
     *
     * @return original resource URL
     */
    public String getOriginalURL();

    /**
     * <p>
     * Returns actual URL for HTTP resource. Note, that actual URL may differ the original one if
     * and only if HTTP redirection occurred.
     * </p>
     *
     * @return final resource URL
     */
    public String getActualURL();

    /**
     * <p>
     * Returns server response content type i.e. value of &quot;Content-Type&quot; header from
     * server response. Returns null if content-type is not known.
     * </p>
     *
     * @return server response contents MIME type or null if content-type is not known
     */
    public String getContentType();

    /**
     * <p>
     * Retrieves the contents of this URL connection. Note, that type of returned object is
     * dependent on &quot;Content-Type&quot; field in server response. The instanceOf operation
     * should be used to determine the specific kind of object returned.
     * </p>
     *
     * @return object representing contents
     *
     * @throws IOException if exception occur during contents retrieval
     */
    public Object getContent() throws IOException;

    /**
     * <p>
     * Returns header field by its name. Returns null if there is no such field in the HTTP header.
     * </p>
     *
     * @param name name of field to retrieve.
     *
     * @return field value or null if there is no such field.
     *
     * @throws NullPointerException if name is null.
     * @throws IllegalArgumentException if the name is empty string.
     */
    public String getHeaderField(String name);

    /**
     * <p>
     * Returns cookies provided by server i.e. value of &quot;Set-Cookie&quot; header field from
     * server response. Cookie is returned as raw (non-parsed) string. If no cookies were provided
     * by server, this function will return null.
     * </p>
     *
     * @return server cookies or null if there are no such
     */
    public String getSetCookie();

    /**
     * <p>
     * Returns parsed representation of HTTP server cookie as a HttpCookie object. If parsing fails
     * this function generates AuthenticateException.
     * </p>
     *
     * <p>
     * If no cookies were provided by the server, this function will return null.
     * </p>
     *
     * @return cookie object or null if cookie was not provided by server
     *
     * @throws AuthenticateException if cookie parsing failed.
     */
    public HttpCookie getCookie() throws AuthenticateException;
}
