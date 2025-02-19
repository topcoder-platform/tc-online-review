/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.action.ServletRequestAware;
import org.apache.struts2.action.ServletResponseAware;

import org.apache.struts2.ActionSupport;


/**
 * This is the base class for all struts actions classes, except the login/logout actions.
 * It exposes the HttpServletRequest and HttpServletResponse to the action classes.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public abstract class BaseServletAwareAction extends ActionSupport
    implements ServletRequestAware, ServletResponseAware {
    /**
    * Represents the serial version id.
    */
    private static final long serialVersionUID = -2929958981318117044L;

    /**
    * Represents the http servlet request.
    */
    protected HttpServletRequest request;

    /**
     * Represents the http servlet response.
     */
    protected HttpServletResponse response;

    /**
     * Set the request field.
     * @param request the http servlet request to be set
     */
    public void withServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Set the response field.
     * @param response the http servlet response to be set
     */
    public void withServletResponse(HttpServletResponse response) {
        this.response = response;
    }
}
