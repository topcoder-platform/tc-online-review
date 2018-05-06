/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.ConfigurationException;

/**
 * Mock implementation of <code>ReviewCommonHandler</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockReviewCommonHandler extends ReviewCommonHandler {

    /**
     * The constructor.
     *
     * @throws ConfigurationException anything wrong when loading configuration
     */
    protected MockReviewCommonHandler() throws ConfigurationException {
        super();
    }

    /**
     * This will do nothing but return null.
     * @param request the request
     * @param userId user id
     * @return always null
     */
    public AjaxResponse service(AjaxRequest request, Long userId) {
        return null;
    }

}
