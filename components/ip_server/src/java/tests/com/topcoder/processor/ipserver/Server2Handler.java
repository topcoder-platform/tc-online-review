/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;


/**
 * A simple Handler implementation for test. Only implement Constructor method with maxRequests.
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class Server2Handler extends Handler {
    /**
     * <p>
     * Server2Handler constructor with maxRequests.
     * </p>
     * @param maxRequests the maxRequests of this handler
     */
    public Server2Handler(int maxRequests) {
        super(maxRequests);
    }

}
