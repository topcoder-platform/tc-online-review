/*
 * Copyright (C) 2006, TopCoder Inc. All rights reserved.
 */

package com.cronos.onlinereview.ajax.failuretests.handlers;

import com.cronos.onlinereview.ajax.handlers.ReviewCommonHandler;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.AjaxRequest;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.review.ReviewManager;

/**
 * <p>A subclass of <code>ReviewCommonHandler</code> class to be used to test the protected methods of the super class.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private
 * access so only the test cases could invoke them. The overridden methods simply call the corresponding method of a
 * super-class.
 *
 * @author isv
 * @version 1.0
 */
class ReviewCommonHandlerSubclass extends ReviewCommonHandler {

    /**
     * <p>Constructs new <code>ReviewCommonHandlerSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @throws ConfigurationException if there is a configuration exception
     */
    public ReviewCommonHandlerSubclass() throws ConfigurationException {
        super();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public ReviewManager getReviewManager() {
        return super.getReviewManager();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public PhaseManager getPhaseManager() {
        return super.getPhaseManager();
    }

    /**
     * <p> <strong>Description : </strong> Service an Ajax request and produce an Ajax response. the userId parameter
     * could be null. </p>
     *
     * @param request the request to service
     * @param userId the id of user who issued this request
     * @return the response to the request
     * @throws IllegalArgumentException if the request is null
     */
    public AjaxResponse service(AjaxRequest request, Long userId) throws IllegalArgumentException {
        return null;
    }
}
