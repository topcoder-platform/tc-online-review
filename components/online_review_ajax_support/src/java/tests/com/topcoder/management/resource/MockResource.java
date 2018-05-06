/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.resource;

/**
 * A mock subclass for <code>Resource</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockResource extends Resource {

    /**
     * The id.
     */
    private long id;

    /**
     * The resource role.
     */
    private ResourceRole resourceRole;

    /**
     * The project.
     */
    private Long project;

    /**
     * The phase.
     */
    private Long phase;

    /**
     * Get the id.
     * @return the id.
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id.
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get project.
     * @return the project
     */
    public Long getProject() {
        return project;
    }

    /**
     * Set project.
     * @param project the project
     */
    public void setProject(Long project) {
        this.project = project;
    }

    /**
     * Get the resource role.
     * @return the resource role
     */
    public ResourceRole getResourceRole() {
        return resourceRole;
    }

    /**
     * Set the resource role.
     * @param resourceRole the resource role
     */
    public void setResourceRole(ResourceRole resourceRole) {
        this.resourceRole = resourceRole;
    }

    /**
     * Get the phase.
     * @return the phase.
     */
    public Long getPhase() {
        return phase;
    }

    /**
     * Set the phase.
     * @param phase
     */
    public void setPhase(Long phase) {
        this.phase = phase;
    }

    public Object getProperty(String name) {
//        return super.getProperty(name);
        return new Long(2);
    }

}
