/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests.handlers;

import com.topcoder.processor.ipserver.Handler;

/**
 * <p>A custom <code>Handler</code> providing a protected constructor to be used for testing the <code>IPServerManager
 * </code>.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class ProtectedHandler extends Handler {

    /**
     * <p>Constructs new <code>ProtectedHandler</code> with specified maximum number of requests to be handled
     * simultaneously.</p>
     *
     * @param maxRequests an <code>int</code> providing the number of maximum requests to be handled by the handler
     *        simultaneously.
     */
    protected ProtectedHandler(int maxRequests) {
        super(maxRequests);
    }
}
