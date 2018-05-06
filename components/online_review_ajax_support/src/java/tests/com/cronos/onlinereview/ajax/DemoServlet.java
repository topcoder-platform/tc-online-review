/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * This is a demo helper. Because before working, the servelet should read
 * Configuration first, so , I use this servlet to wrap the AjaxSupportServelet.
 *
 * @author assistant
 * @version 1.0
 */
public class DemoServlet extends HttpServlet {

    /**
     * Represents the servlet to wrap.
     */
    private AjaxSupportServlet servlet;

    /**
     * Loading the configurations.
     * @throws Exception to the container
     */
    public DemoServlet() throws Exception {

        // load the configurations
        ConfigManager cm = ConfigManager.getInstance();
        if (!cm.existsNamespace("com.cronos.onlinereview.ajax")) {
            cm.add("default.xml");
            cm.add("objectfactory.xml");
            cm.add("scorecalculator.xml");
        }

        servlet = new AjaxSupportServlet();
    }

    /**
     * Initialize the servlet.
     * @param config the servlet config
     */
    public void init(ServletConfig config) {
        try {
            servlet.init(config);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    /**
     * Destory the servlet.
     */
    public void destroy() {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        while (it.hasNext()) {
            try {
                cm.removeNamespace(it.next().toString());
            } catch (UnknownNamespaceException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Do post.
     * @param request the request
     * @param response the response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            servlet.doPost(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
