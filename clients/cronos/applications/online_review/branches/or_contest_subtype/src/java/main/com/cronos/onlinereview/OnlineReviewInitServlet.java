/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.topcoder.util.log.LogManager;
import com.topcoder.util.log.log4j.Log4jLogFactory;

/**
 * <p>
 * This class does the application start-up initialization.
 * </p>
 * @author VolodymyrK
 */
public class OnlineReviewInitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void init(ServletConfig config) throws ServletException {
        // Init LogManager with the Log4j log factory
        LogManager.setLogFactory(new Log4jLogFactory(true));

        super.init(config);
    }
}
