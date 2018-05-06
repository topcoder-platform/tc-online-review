/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.resource;

import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.Filter;

/**
 * Mock implementation of <code>ResourceManager</code>.
 *
 *
 * @author assistant
 * @version 1.0
 */
public class MockResourceManager implements ResourceManager {

    /**
     * <p>A <code>Throwable</code> representing the exception to be thrown from any method of the mock class.</p>
     */
    private static Throwable globalException = null;

    /**
     * Add notifications.
     * @param users the users
     * @param project the project
     * @param notificationType the type
     * @param operator the operator
     */
    public void addNotifications(long[] users, long project, long notificationType, String operator)
        throws ResourcePersistenceException {
        if (MockResourceManager.globalException != null) {
            if (MockResourceManager.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourceManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly",
                                           MockResourceManager.globalException);
            }
        }
    }

    /**
     * Get all the notification types.
     * @return all the notification types.
     */
    public NotificationType[] getAllNotificationTypes() {
        NotificationType type = new MockNotificationType();
        type.setId(1);
        type.setName("Timeline Notification");
        return new NotificationType[] {type};
    }

    /**
     * Get all the resource roles.
     * @return all the resource roles.
     */
    public ResourceRole[] getAllResourceRoles() {
        ResourceRole[] ret = new MockResourceRole[1];
        ret[0] = new MockResourceRole();
        ret[0].setName("Manager");
        ret[0].setId(1);
        return ret;
    }

    /**
     * Get notifications with project.
     * @param project the project id
     * @param notificationType the notification type
     * @return ids of the notifications
     */
    public long[] getNotifications(long project, long notificationType) {
        return null;
    }

    /**
     * Get resource by id.
     * @param id the resource id
     * @return the resource object
     */
    public Resource getResource(long id) throws ResourcePersistenceException {
        if (MockResourceManager.globalException != null) {
            if (MockResourceManager.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourceManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly",
                                           MockResourceManager.globalException);
            }
        }
        Resource resource = new MockResource();
        resource.setId(2);
        resource.setProject(new Long(1));
        ResourceRole resourceRole = new MockResourceRole();
        resourceRole.setId(id);
        resourceRole.setName("Manager");
        resource.setResourceRole(resourceRole);

        if (id == 1) {
            return resource;
        } else if (id == 2) {
            resourceRole.setName("Submitter");
        } else if (id == 3) {
            resourceRole.setName("Reviewer");
            resource.setPhase(new Long(1));
        } else if (id == 4) {
            resourceRole.setName("Accuracy Reviewer");
            resource.setPhase(new Long(1));
        } else if (id == 5) {
            resourceRole.setName("Failure Reviewer");
            resource.setPhase(new Long(1));
        } else if (id == 6) {
            resourceRole.setName("Stress Reviewer");
            resource.setPhase(new Long(1));
        }
        return resource;
    }

    /**
     * Remove the notifications.
     * @param users the users
     * @param project the project
     * @param notificationType the notification type
     * @param operator the operator
     */
    public void removeNotifications(long[] users, long project, long notificationType, String operator) {
    }

    /**
     * Search the resource by filter.
     * @param filter the filter
     * @return the resources found
     */
    public Resource[] searchResource(Filter filter) {
        return new Resource[1];
    }

    /**
     * Update the resource.
     * @param resource the resource
     * @param operator the operator
     */
    public void updateResource(Resource resource, String operator) {
    }

    /**
     * Update resources.
     * @param resources the resources to update
     * @param project the project
     * @param operator the operator
     */
    public void updateResources(Resource[] resources, long project, String operator) {
    }

    public void removeNotificationType(NotificationType arg0, String arg1) throws ResourcePersistenceException {
        // TODO Auto-generated method stub

    }

    public void removeResource(Resource arg0, String arg1) throws ResourcePersistenceException {
        // TODO Auto-generated method stub

    }

    public void removeResourceRole(ResourceRole arg0, String arg1) throws ResourcePersistenceException {
        // TODO Auto-generated method stub

    }

    public NotificationType[] searchNotificationTypes(Filter arg0) throws ResourcePersistenceException, SearchBuilderException, SearchBuilderConfigurationException {
        // TODO Auto-generated method stub
        return null;
    }

    public Notification[] searchNotifications(Filter arg0) throws ResourcePersistenceException, SearchBuilderException, SearchBuilderConfigurationException {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourceRole[] searchResourceRoles(Filter arg0) throws ResourcePersistenceException, SearchBuilderException, SearchBuilderConfigurationException {
        return null;
    }

    public Resource[] searchResources(Filter arg0) throws ResourcePersistenceException, SearchBuilderException,
                                                          SearchBuilderConfigurationException {
        if (MockResourceManager.globalException != null) {
            if (MockResourceManager.globalException instanceof ResourcePersistenceException) {
                throw (ResourcePersistenceException) MockResourceManager.globalException;
            } else if (MockResourceManager.globalException instanceof SearchBuilderException) {
                throw (SearchBuilderException) MockResourceManager.globalException;
            } else if (MockResourceManager.globalException instanceof SearchBuilderConfigurationException) {
                throw (SearchBuilderConfigurationException) MockResourceManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly",
                                           MockResourceManager.globalException);
            }
        }
        return new Resource[1];
    }

    public void updateNotificationType(NotificationType arg0, String arg1) throws ResourcePersistenceException {
    }

    public void updateResourceRole(ResourceRole arg0, String arg1) throws ResourcePersistenceException {
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockResourceManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockLog</code> so all collected method arguments, configured method results and
     * exceptions are lost.</p>
     */
    public static void releaseState() {
        MockResourceManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockResourceManager</code> class.</p>
     */
    public static void init() {
    }
}
