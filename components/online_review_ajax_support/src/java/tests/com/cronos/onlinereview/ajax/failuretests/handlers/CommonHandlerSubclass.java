/*
 * Copyright (C) 2006, TopCoder Inc. All rights reserved.
 */

package com.cronos.onlinereview.ajax.failuretests.handlers;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.cronos.onlinereview.ajax.handlers.CommonHandler;
import com.cronos.onlinereview.ajax.handlers.ResourceException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;

/**
 * <p>A subclass of <code>CommonHandler</code> class to be used to test the protected methods of the super class.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private
 * access so only the test cases could invoke them. The overridden methods simply call the corresponding method of a
 * super-class.
 *
 * @author isv
 * @version 1.0
 */
class CommonHandlerSubclass extends CommonHandler {

    /**
     * <p>Constructs new <code>CommonHandlerSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @throws ConfigurationException if there is a configuration exception
     */
    public CommonHandlerSubclass() throws ConfigurationException {
        super();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     * @param resource TODO
     * @param role the role to check for
     *
     * @throws ResourceException if the resource manager has thrown an exception
     * @throws IllegalArgumentException if role parameter is null or empty String
     */
    public boolean checkUserHasRole(Resource resource, String role) throws ResourceException {
        return super.checkResourceHasRole(resource, role);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @param userId the id of the user to check its role
     * @throws ResourceException if the resource manager has thrown an exception
     */
    public boolean checkUserHasGlobalManagerRole(long userId) throws ResourceException {
        return super.checkUserHasGlobalManagerRole(userId);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public ResourceManager getResourceManager() {
        return super.getResourceManager();
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
