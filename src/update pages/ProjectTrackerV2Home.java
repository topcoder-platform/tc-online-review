/*
 * ProjectTrackerHome.java
 *
 * Copyright ï¿?2003, TopCoder, Inc. All rights reserved
 *
 */
package com.topcoder.apps.review.projecttracker;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;


/**
 * This class is provided as part of the EJB framework as a utility for obtaining a ProjectTrackerV2 implementation.
 *
 * @author TCSDeveloper
 */
public interface ProjectTrackerV2Home extends EJBHome {
    public static final String EJB_REF_NAME = "com.topcoder.apps.review.projecttracker.ProjectTrackerV2Home";

    /**
     * Create ProjectTrackerV2
     *
     * @return ProjectTrackerV2
     *
     * @throws CreateException if error occurs
     * @throws RemoteException if error occurs
     */
    public ProjectTrackerV2 create() throws CreateException, RemoteException;
}
