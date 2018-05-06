/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.resource.Notification;
import com.topcoder.management.resource.NotificationType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.Filter;


/**
 * Mock class implements ResourceManager.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockResourceManager implements ResourceManager {

    public void updateResource(Resource resource, String operator) {
    }

    public void updateResources(Resource[] resources, long project, String operator) {
    }

    public Resource getResource(long id) {
        if (id == -1) {
            return null;
        } else {
            return new MockResource(id);
        }
        
    }

    public Resource[] searchResource(Filter filter) {
        return null;
    }

    public ResourceRole[] getAllResourceRoles() {
        return new ResourceRole[] {new MockResourceRole()};
    }

    public void addNotifications(long[] users, long project, long notificationType, String operator) {
    }

    public void removeNotifications(long[] users, long project, long notificationType, String opertator) {
    }

    public long[] getNotifications(long project, long notificationType) {
        return null;
    }

    public NotificationType[] getAllNotificationTypes() {
        return new NotificationType[] {new MockNotificationType()};
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
        // TODO Auto-generated method stub
        return null;
    }

    public Resource[] searchResources(Filter arg0) throws ResourcePersistenceException, SearchBuilderException, SearchBuilderConfigurationException {
        // TODO Auto-generated method stub
        return null;
    }

    public void updateNotificationType(NotificationType arg0, String arg1) throws ResourcePersistenceException {
        // TODO Auto-generated method stub
        
    }

    public void updateResourceRole(ResourceRole arg0, String arg1) throws ResourcePersistenceException {
        // TODO Auto-generated method stub
        
    }

}
