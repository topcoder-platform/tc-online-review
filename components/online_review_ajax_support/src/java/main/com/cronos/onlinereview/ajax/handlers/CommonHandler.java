/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import java.util.Arrays;

import com.cronos.onlinereview.ajax.AjaxRequestHandler;
import com.cronos.onlinereview.ajax.AjaxSupportHelper;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.objectfactory.ObjectFactory;

/**
 * <p>
 * Defines a common Ajax request handler capable of getting a user's role using its ID,
 * and the Resource Management component;
 * this class implements the AjaxRequestHandler interface,
 * and keeps an instance of ResourceManager class in order to get resource related data.
 * <br>
 * This class's main purpose is to simplify Ajax request handlers' implementation.
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong>
 * This class is immutable an thread safe.
 * </p>
 *
 * @author topgear
 * @author assistant
 * @author George1
 * @version 1.1.2
 */
public abstract class CommonHandler implements AjaxRequestHandler {

    /**
     * The logger.
     */
    private static final Log logger = LogManager.getLog(CommonHandler.class.getName());
    
    /**
     * <p>
     * The resource manager used to get resource data
     * This variable is immutable, it is initialized by the constructor to a not null ReviewManager object,
     * and retrieved using its corresponding getter method.
     * </p>
     */
    private final ResourceManager resourceManager;

    /**
     * <p>
     * The id of the manager role.
     * This variable is immutable, it is initialized by the constructor to a negative/0/positive long number,
     * and used in several methods in this class.
     * </p>
     */
    private final long managerRoleId;

    /**
     * <p>
     * Creates an instance of this class and initialize its internal state.
     * </p>
     *
     * @throws ConfigurationException if there is a configuration exception
     */
    protected CommonHandler() throws ConfigurationException {
        try {
            // create an object factory that uses only the specification
            ObjectFactory factory = AjaxSupportHelper.createObjectFactory();

            resourceManager = (ResourceManager) factory.createObject(ResourceManager.class);

            logger.log(Level.DEBUG, "create ResourceManager from objectfactory with its class type.");
            // get all the resource roles
            ResourceRole[] roles = resourceManager.getAllResourceRoles();

            // find the role with name "manager" and save its id
            boolean found = false;
            long id = 0;
            for (ResourceRole role : roles) {
                if ("Manager".equals(role.getName())) {
                    id = role.getId();
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new ConfigurationException("Can't find manager role.");
            }
            this.managerRoleId = id;
        } catch (Exception e) {
            throw new ConfigurationException("Error happens during construct handler.", e);
        }
    }

    /**
     * Check whether the resource has been assigned to some user.
     *
     * @param resource the resource to check
     * @return true if the resource is assigned to the user
     * @param userId the id of the user to check
     * @throws IllegalArgumentException if resource parameter is null
     */
    protected boolean checkResourceAssignedToUser(Resource resource, long userId) {
        if (resource == null) {
            throw new IllegalArgumentException("The resource can't be null.");
        }
        return resource.getUserId() != null && resource.getUserId().equals(userId);
    }

    /**
     * <p>
     * Check if a user has the specified role or not.
     * </p>
     *
     * @return true if the user has the role
     * @param resource the resource of the user to check its role
     * @param role the role to check for
     * @throws IllegalArgumentException if role parameter is null or empty String
     */
    protected boolean checkResourceHasRole(Resource resource, String role) {
        if (resource == null) {
            throw new IllegalArgumentException("The resource can't be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("The role should not be null.");
        }
        if (role.trim().length() == 0) {
            throw new IllegalArgumentException("The role should not be empty.");
        }

        ResourceRole rr = resource.getResourceRole();
        if (rr == null) {
            return false;
        }
        // get the user role name
        String roleName = rr.getName();
        if (roleName == null) {
            return false;
        }
        return roleName.equals(role);
    }

    /**
     * <p>
     * Check if the user has the global manager role or not.
     * </p>
     *
     * @return true if the user has the global manager role
     * @param userId the id of the user to check its role
     * @throws ResourceException if the resource manager has thrown an exception
     */
    protected boolean checkUserHasGlobalManagerRole(long userId) throws ResourceException {
        // build the search filter
        Filter noProjectFilter = ResourceFilterBuilder.createNoProjectFilter();
        Filter resourceRoleIdFilter = ResourceFilterBuilder.createResourceRoleIdFilter(managerRoleId);
        Filter userIdFilter = ResourceFilterBuilder.createUserIdFilter(userId);

        Filter bundle = new AndFilter(Arrays.asList(noProjectFilter, resourceRoleIdFilter, userIdFilter));

        // find the resources using the bundle
        try {
            Resource[] resources = resourceManager.searchResources(bundle);
            if (resources == null || resources.length == 0) {
                return false;
            }
        } catch (Exception e) {
            throw new ResourceException("Error happens in Resource Manager.", e);
        }
        return true;
    }

    /**
     * <p>
     * Returns the resource manager used to get resource data.
     * </p>
     *
     * @return the resource manager used to manage resources
     */
    protected ResourceManager getResourceManager() {
        return resourceManager;
    }
}
