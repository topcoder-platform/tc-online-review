/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests.handlers;

import com.topcoder.processor.ipserver.Handler;

/**
 * <p>A custom <code>Handler</code> providing a package private constructor to be used for testing the <code>
 * IPServerManager</code>.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class PackagePrivateHandler extends Handler {

    /**
     * <p>Constructs new <code>PackagePrivateHandler</code> with specified maximum number of requests to be handled
     * simultaneously.</p>
     *
     * @param maxRequests an <code>int</code> providing the number of maximum requests to be handled by the handler
     *        simultaneously.
     */
    PackagePrivateHandler(int maxRequests) {
        super(maxRequests);
    }
}
