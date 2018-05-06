/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.accuracytests.mock;

import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * <p>A mock implementation of {@link Project} class to be used for testing. Overrides the protected methods declared by
 * a super-class. The overridden methods are declared with package private access so only the test cases could invoke
 * them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author isv
 * @version 1.0
 */
public class MockProject extends Project {

    private long id;
    private ProjectStatus projectStatus;
    private ProjectCategory projectCategory;
    private String muser;
    private String cuser;
    private Date mt;
    private Date ct;
    private Map props = new HashMap();

    public MockProject() {
        super(null, null);
    }

    public MockProject(ProjectCategory projectCategory, ProjectStatus projectStatus) {
        super(projectCategory, projectStatus);
    }

    public MockProject(long projectId, ProjectCategory projectCategory, ProjectStatus projectStatus) {
        super(projectId, projectCategory, projectStatus);
    }

    public MockProject(long projectId, ProjectCategory projectCategory, ProjectStatus projectStatus, Map properties) {
        super(projectId, projectCategory, projectStatus, properties);
    }

    /**
     * @see Project#setId(long)
     */
    public void setId(long long0) {
        this.id = long0;
    }

    /**
     * @see Project#getId()
     */
    public long getId() {
        return this.id;
    }

    /**
     * @see Project#setProjectStatus(ProjectStatus)
     */
    public void setProjectStatus(ProjectStatus projectStatus0) {
        this.projectStatus = projectStatus0;
    }

    /**
     */
    public ProjectStatus getProjectStatus() {
        return this.projectStatus;
    }

    /**
     * @see Project#setProjectCategory(ProjectCategory)
     */
    public void setProjectCategory(ProjectCategory projectCategory0) {
        this.projectCategory = projectCategory0;
    }

    /**
     * @see Project#getProjectCategory()
     */
    public ProjectCategory getProjectCategory() {
        return this.projectCategory;
    }

    /**
     * @see Project#setProperty(String, Object)
     */
    public void setProperty(String string0, Object object0) {
        this.props.put(string0, object0);
    }

    /**
     * @see Project#getProperty(String)
     */
    public Object getProperty(String string0) {
        return this.props.get(string0);
    }

    /**
     * @see Project#getAllProperties()
     */
    public Map getAllProperties() {
        return this.props;
    }

    /**
     * @see Project#setCreationUser(String)
     */
    public void setCreationUser(String string0) {
        this.cuser = string0;
    }

    /**
     * @see Project#getCreationUser()
     */
    public String getCreationUser() {
        return this.cuser;
    }

    /**
     * @see Project#setCreationTimestamp(Date)
     */
    public void setCreationTimestamp(Date date0) {
        this.ct = date0;
    }

    /**
     * @see Project#getCreationTimestamp()
     */
    public Date getCreationTimestamp() {
        return this.ct;
    }

    /**
     * @see Project#setModificationUser(String)
     */
    public void setModificationUser(String string0) {
        this.muser = string0;
    }

    /**
     * @see Project#getModificationUser()
     */
    public String getModificationUser() {
        return this.muser;
    }

    /**
     * @see Project#setModificationTimestamp(Date)
     */
    public void setModificationTimestamp(Date date0) {
        this.mt = date0;
    }

    /**
     * @see Project#getModificationTimestamp()
     */
    public Date getModificationTimestamp() {
        return this.mt;
    }

}
