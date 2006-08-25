/*
 * ProjectTrackerLocal.java
 *
 * Copyright ï¿?2003, TopCoder, Inc. All rights reserved
 *
 */
package com.topcoder.apps.review.projecttracker;

import com.topcoder.security.TCSubject;

import com.topcoder.util.errorhandling.BaseException;

import java.sql.Date;

import javax.ejb.EJBLocalObject;


/**
 * The ProjectTrackerLocal provides access to the project data stored within the system.
 *
 * @author TCSDeveloper
 */
public interface ProjectTrackerV2Local extends EJBLocalObject {
    /**
     * return winner and forum id array
     *
     * @param projectId projectId
     * @param requestor requestor
     *
     * @return winner and forum id array
     */
    public long[] getProjectWinnerIdForumId(long projectId, TCSubject requestor);

    /**
     * Gets the PM for the project, or returns null if no PM is found.
     *
     * @param projectId projectId
     *
     * @return User
     */
    public User getPM(long projectId);

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
     */
    public long createProject(String projectName, String projectVersion, long compVersId, long projectTypeId,
        String overview, Date[] dates, TCSubject requestor, long levelId)
        throws BaseException;

    /**
     * User inquiry for one component.
     *
     * @param userId userId
     * @param projectId projectId
     *
     * @throws BaseException if error occurs while retrieve data from db
     */
    public void userInquiry(long userId, long projectId)
        throws BaseException;

    /**
     * Rename component version.
     *
     * @param compVersId compVersId
     * @param oldVersion oldVersion
     * @param newVersion newVersion
     */
    public void versionRename(long compVersId, String oldVersion, String newVersion);

    /**
     * Rename component name.
     *
     * @param componentId componentId
     * @param oldName oldName
     * @param newName newName
     */
    public void componentRename(long componentId, String oldName, String newName);
}
