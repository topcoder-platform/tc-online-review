/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.project.ValidationException;
import com.topcoder.search.builder.filter.Filter;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockProjectManager implements ProjectManager {


    public void createProject(Project project, String operator) {
    }

    public void updateProject(Project project, String operator) {
    }

    public Project getProject(long id) {
        if (id == -1) {
            return null;
        } else {
            return new MockProject(new ProjectCategory(1, "name", new ProjectType(1, "Java")),
                    new ProjectStatus(1, "Open"));
        }
        
    }

    public Project[] searchProjects(Filter filter) {
        return null;
    }

    public Project[] getUserProjects(long user) {
        return null;
    }

    public ProjectCategory[] getAllProjectCategories() {
        return null;
    }

    public ProjectStatus[] getAllProjectStatuses() {
        return null;
    }

    public ProjectPropertyType[] getAllProjectPropertyTypes() throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    public ProjectType[] getAllProjectTypes() throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    public Project[] getProjects(long[] arg0) throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    public void updateProject(Project arg0, String arg1, String arg2) throws PersistenceException, ValidationException {
        // TODO Auto-generated method stub
        
    }
}
