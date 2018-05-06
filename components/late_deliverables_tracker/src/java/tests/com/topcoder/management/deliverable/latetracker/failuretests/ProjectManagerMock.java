/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.failuretests;

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
 * The ProjectManager mock for failure tests.
 * @author gjw99
 * @version 1.0
 */
public class ProjectManagerMock implements ProjectManager {
    public void createProject(Project arg0, String arg1)
        throws PersistenceException, ValidationException {
    }

    public ProjectCategory[] getAllProjectCategories()
        throws PersistenceException {
        return null;
    }

    public ProjectPropertyType[] getAllProjectPropertyTypes()
        throws PersistenceException {
        return null;
    }

    public ProjectStatus[] getAllProjectStatuses() throws PersistenceException {
        return null;
    }

    public ProjectType[] getAllProjectTypes() throws PersistenceException {
        return null;
    }

    public Project getProject(long arg0) throws PersistenceException {
        return null;
    }

    public Project[] getProjects(long[] arg0) throws PersistenceException {
        return null;
    }

    public Project[] getProjectsByCreateDate(int arg0)
        throws PersistenceException {
        return null;
    }

    public Project[] getUserProjects(long arg0) throws PersistenceException {
        return null;
    }

    public Project[] searchProjects(Filter arg0) throws PersistenceException {
        throw new PersistenceException("failure");
    }

    public void updateProject(Project arg0, String arg1, String arg2)
        throws PersistenceException, ValidationException {
    }
}
