/*
 * Copyright (C) 2005 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * The EncodeFilter to encode all requests.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class EncodeFilter implements Filter {
    /** Represents the encoding. */
    private String encoding = null;

    /** Represents the filter config. */
    private FilterConfig filterConfig = null;

    /** Represents the ignore. */
    private boolean ignore = true;

    /**
     * Release the properties.
     */
    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
    }

    /**
     * Filter the request.
     *
     * @param request request
     * @param response response
     * @param chain chain
     *
     * @throws IOException if error occurs
     * @throws ServletException if error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (ignore || (request.getCharacterEncoding() == null)) {
            if (encoding != null) {
                request.setCharacterEncoding(encoding);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Init the parameter.
     *
     * @param filterConfig filterConfig
     *
     * @throws ServletException if error occurs
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.encoding = filterConfig.getInitParameter("encoding");
        String value = filterConfig.getInitParameter("ignore");

        if (value == null) {
            this.ignore = true;
        } else if (value.equalsIgnoreCase("true")) {
            this.ignore = true;
        } else if (value.equalsIgnoreCase("yes")) {
            this.ignore = true;
        } else {
            this.ignore = false;
        }
    }
}
