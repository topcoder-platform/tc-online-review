/*
 * Copyright (C) 2009 TopCoder Inc.  All Rights Reserved.
 */
package com.topcoder.management.project.link;

/**
 * <p>
 * Project link manager. It handles all aspects for project links.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ProjectLinkManager {
    public ProjectLinkType[] getAllProjectLinkTypes() {
        return new ProjectLinkType[] {};
    }

    public ProjectLink[] getDestProjectLinks() {
        return null;
    }

    public ProjectLink[] getSourceProjectLinks() {
        return null;
    }

    public void updateProjectLinks(long sourceProjectId, ProjectLink[] links) {

    }
}
