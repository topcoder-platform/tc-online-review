/*
 * ProjectTrackerHome.java
 *
 * Copyright ï¿?2003, TopCoder, Inc. All rights reserved
 *
 */
package com.topcoder.apps.review.projecttracker;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;


/**
 * This class is provided as part of the EJB framework as a utility for obtaining a ProjectTrackerV2 implementation.
 *
 * @author TCSDeveloper
 */
public interface ProjectTrackerV2LocalHome extends EJBLocalHome {
    public static final String EJB_REF_NAME = "com.topcoder.apps.review.projecttracker.ProjectTrackerV2LocalHome";

    /**
     * Return ProjectTrackerV2Local
     *
     * @return ProjectTrackerV2Local
     *
     * @throws CreateException if error occurs
     */
    public ProjectTrackerV2Local create() throws CreateException;
}
