/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.ConfigurationException;

/**
 * Mock implementation of <code>CommonHandler</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockCommonHandler extends CommonHandler {

    /**
     * Create the handler.
     *
     * @throws ConfigurationException if error happens when loading configuration
     */
    public MockCommonHandler() throws ConfigurationException {
        super();
    }

    /**
     * Serve the request.
     * @param request the ajax request to serve
     * @param userId the userId
     * @return a simple response
     */
    public AjaxResponse service(AjaxRequest request, Long userId) {
        return new AjaxResponse("MockCommon", "Success", null);
    }

}
