/*
 * ProjectTracker.java
 *
 * Copyright 2006, TopCoder, Inc. All rights reserved
 *
 */
package com.topcoder.apps.review.projecttracker;

import com.topcoder.security.TCSubject;

import com.topcoder.util.errorhandling.BaseException;

import java.rmi.RemoteException;

import java.sql.Date;

import javax.ejb.EJBObject;


/**
 * The ProjectTracker provides access to the project data stored within the system.
 *
 * @author TCSDeveloper
 */
public interface ProjectTrackerV2 extends EJBObject {
    /**
     * return winner and forum id array
     *
     * @param projectId projectId
     * @param requestor requestor
     *
     * @return winner and forum id array
     *
     * @throws RemoteException if error occurs while retrieve data from db
     */
    public long[] getProjectWinnerIdForumId(long projectId, TCSubject requestor)
        throws RemoteException;

    /**
     * Retrieves the id of a project based on the component version id of a component and the project type
     *
     * @param compVersId the component's component version id
     * @param projectType the project type (design or development)
     *
     * @return the project id, -1 if no project exists
     *
     * @throws RemoteException if error occurs while retrieve data from db
     */
    public long getProjectIdByComponentVersionId(long compVersId, long projectType)
        throws RemoteException;

    /**
     * Gets the PM for the project, or returns null if no PM is found.
     *
     * @param projectId projectId
     *
     * @return User
     *
     * @throws RemoteException if error occurs while retrieve data from db
     */
    public User getPM(long projectId) throws RemoteException;

    /**
     * Create a new Online Review Project.
     *
     * @param projectName projectName
     * @param projectVersion projectVersion
     * @param compVersId compVersId
     * @param projectTypeId projectTypeId
     * @param overview overview
     * @param dates dates
     * @param requestor requestor
     * @param levelId levelId
     *
     * @return project id
     *
     * @throws BaseException if error occurs while retrieve data from db
     * @throws RemoteException if error occurs while retrieve data from db
     */
    public long createProject(String projectName, String projectVersion, long compVersId, long projectTypeId,
        String overview, Date[] dates, TCSubject requestor, long levelId)
        throws BaseException, RemoteException;

    /**
     * User inquiry for one component.
     *
     * @param userId userId
     * @param projectId projectId
     *
     * @throws BaseException if error occurs while retrieve data from db
     * @throws RemoteException if error occurs while retrieve data from db
     */
    public void userInquiry(long userId, long projectId)
        throws BaseException, RemoteException;

    /**
     * Rename component version.
     *
     * @param compVersId compVersId
     * @param oldVersion oldVersion
     * @param newVersion newVersion
     *
     * @throws RemoteException if error occurs while retrieve data from db
     */
    public void versionRename(long compVersId, String oldVersion, String newVersion)
        throws RemoteException;

    /**
     * Rename component name.
     *
     * @param componentId componentId
     * @param oldName oldName
     * @param newName newName
     *
     * @throws RemoteException if error occurs while retrieve data from db
     */
    public void componentRename(long componentId, String oldName, String newName)
        throws RemoteException;
}
