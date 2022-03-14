/*
 * Copyright (C) 2012 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.servlet;

import com.topcoder.util.log.LogManager;
import com.topcoder.util.log.log4j.Log4jLogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * <p>
 * This class does the application start-up initialization.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class OnlineReviewInitServlet extends HttpServlet {
    /** Represents the Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize the online review servlet with log4j supports.
     * @param config the servlet config
     */
    public void init(ServletConfig config) throws ServletException {
        // Init LogManager with the Log4j log factory
        LogManager.setLogFactory(new Log4jLogFactory(true));

        super.init(config);
    }
}
