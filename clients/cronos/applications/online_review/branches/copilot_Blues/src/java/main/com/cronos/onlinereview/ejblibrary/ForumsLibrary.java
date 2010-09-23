/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.jivesoftware.base.UnauthorizedException;
import com.jivesoftware.base.UserNotFoundException;
import com.jivesoftware.forum.ForumCategoryNotFoundException;
import com.topcoder.web.ejb.forums.Forums;
import com.topcoder.web.ejb.forums.ForumsBean;

import java.util.ArrayList;

/**
 * <p>An implementation of {@link Forums} interface which provides the library-call style for API of <code>Forums EJB
 * </code>.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author isv
 * @version 1.0 (TopCoder Online Review Switch To Local Calls)
 */
public class ForumsLibrary extends BaseEJBLibrary implements Forums {

    /**
     * <p>A <code>ForumsBean</code> which is delegated the processing of the calls to methods of this class.</p>
     */
    private ForumsBean bean;

    /**
     * <p>Constructs new <code>ForumsLibrary</code> instance. This implementation does nothing.</p>
     */
    public ForumsLibrary() {
        this.bean = new ForumsBean();
    }

    /**
     * <p>Creates watch for specified forum for specified user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param forumID a <code>long</code> providing the forum ID.
     * @throws Exception if an unexpected error occurs.
     */
    public void createForumWatch(long userID, long forumID) throws Exception {
        bean.createForumWatch(userID, forumID);
    }

    /**
     * <p>Creates specified Studio forum.</p>
     *
     * @param name a <code>String</code> providing the forum name.
     * @return a <code>long</code> providing the ID of created forum.
     */
    public long createStudioForum(String name) {
        return bean.createStudioForum(name);
    }

    /**
     * <p>Gets the data for forum categories watched by the specified user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param isWatched <code>true</code> if watched forum categories are to be returned; <code>false</code> otherwise.
     * @return a <code>String</code> array providing the data for watched forum categories.
     */
    public String[][] getWatchedSoftwareCategoriesData(long userID, boolean isWatched) {
        return bean.getWatchedSoftwareCategoriesData(userID, isWatched);
    }

    /**
     * <p>Gets the software roles for the specified user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @return a <code>String</code> array listing the software roles data.
     */
    public String[][] getSoftwareRolesData(long userID) {
        return bean.getSoftwareRolesData(userID);
    }

    /**
     * <p>Gets all existing software roles.</p>
     *
     * @return a <code>String</code> array listing the software roles data.
     */
    public String[][] getAllSoftwareRolesData() {
        return bean.getAllSoftwareRolesData();
    }

    /**
     * <p>Creates new forum for specified component.</p>
     *
     * @param componentName a <code>String</code> providing the component name.
     * @param componentID a <code>long</code> providing the component ID.
     * @param versionID a <code>long</code> providing the component version ID.
     * @param phaseID a <code>long</code> providing the phase ID.
     * @param componentStatusID a <code>long</code> providing the component status ID.
     * @param rootCategoryID a <code>long</code> providing the forum root category ID.
     * @param description a <code>String</code> providing the component description.
     * @param versionText a <code>String</code> providing the component version text.
     * @param isPublic <code>true</code> if forum is public; <code>false</code> otherwise.
     * @return a <code>long</code> providing the ID for created component.
     * @throws Exception if an unexpected error occurs.
     */
    public long createSoftwareComponentForums(String componentName, long componentID, long versionID, long phaseID,
                                              long componentStatusID, long rootCategoryID, String description,
                                              String versionText, boolean isPublic) throws Exception {
        return bean
            .createSoftwareComponentForums(componentName, componentID, versionID, phaseID, componentStatusID,
                                           rootCategoryID,
                                           description, versionText, isPublic);
    }

    /**
     * <p>Creates new software component forums.</p>
     *
     * @param componentName a <code>String</code> providing the component name.
     * @param componentID a <code>long</code> providing the component ID.
     * @param versionID a <code>long</code> providing the component version ID.
     * @param phaseID a <code>long</code> providing the phase ID.
     * @param componentStatusID a <code>long</code> providing the component status ID.
     * @param rootCategoryID a <code>long</code> providing the forum root category ID.
     * @param description a <code>String</code> providing the component description.
     * @param versionText a <code>String</code> providing the component version text.
     * @param isPublic <code>true</code> if forum is public; <code>false</code> otherwise.
     * @param projectCategoryId a <code>long</code> providing the ID for project category.
     * @return a <code>long</code> providing the ID for created component.
     * @throws Exception if an unexpected error occurs.
     */
    public long createSoftwareComponentForums(String componentName, long componentID, long versionID, long phaseID,
                                              long componentStatusID, long rootCategoryID, String description,
                                              String versionText, boolean isPublic, long projectCategoryId)
        throws Exception {
        return bean
            .createSoftwareComponentForums(componentName, componentID, versionID, phaseID, componentStatusID,
                                           rootCategoryID,
                                           description, versionText, isPublic, projectCategoryId);
    }

    /**
     * <p>Updates the specified component version.</p>
     *
     * @param categoryID a <code>long</code> providing forum category ID.
     * @param versionText a <code>String</code> providing the version text.
     * @throws Exception if an unexpected error occurs.
     */
    public void updateComponentVersion(long categoryID, String versionText) throws Exception {
        bean.updateComponentVersion(categoryID, versionText);
    }

    /**
     * <p>Updates the name for the forum mapped to specified component name.</p>
     *
     * @param categoryID a <code>long</code> providing forum category ID.
     * @param name a <code>String</code> providing the forum name.
     * @throws Exception if an unexpected error occurs.
     */
    public void updateComponentName(long categoryID, String name) throws Exception {
        bean.updateComponentName(categoryID, name);
    }

    /**
     * <p>Creates new forum for specified marathon round.</p>
     *
     * @param roundID a <code>long</code> providing the ID of marathon round.
     * @param name a <code>String</code> providing the forum name.
     * @return a <code>long</code> providing the ID of a created forum.
     */
    public long createMarathonForum(long roundID, String name) {
        return bean.createMarathonForum(roundID, name);
    }

    /**
     * <p>Assigns the specified role to specified user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param groupID a <code>long</code> providing the ID of a forum group.
     */
    public void assignRole(long userID, long groupID) {
        bean.assignRole(userID, groupID);
    }

    /**
     * <p>Assigns the specified role to specified user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param groupName a <code>String</code> providing the name of the forum category group.
     */
    public void assignRole(long userID, String groupName) {
        bean.assignRole(userID, groupName);
    }

    /**
     * <p>Removes the specified role from specified user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param groupID a <code>long</code> providing the ID of a forum group.
     */
    public void removeRole(long userID, long groupID) {
        bean.removeRole(userID, groupID);
    }

    /**
     * <p>Removes the specified role from specified user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param groupName a <code>String</code> providing the name of the forum category group.
     */
    public void removeRole(long userID, String groupName) {
        bean.removeRole(userID, groupName);
    }

    /**
     *
     * @param categoryID a <code>long</code> providing forum category ID.
     * @param isPublic <code>true</code> if forum is public; <code>false</code> otherwise.
     * @throws ForumCategoryNotFoundException if requested forum is not found.
     * @throws UnauthorizedException if specified user is not granted permission for accessing the forum.
     */
    public void setPublic(long categoryID, boolean isPublic)
        throws ForumCategoryNotFoundException, UnauthorizedException {
        bean.setPublic(categoryID, isPublic);
    }

    /**
     * <p>Checks if specified forum category is public.</p>
     *
     * @param categoryID a <code>long</code> providing forum category ID.
     * @return <code>true</code> if specified forum category is public; <code>false</code> otherwise.
     * @throws ForumCategoryNotFoundException if requested forum is not found.
     * @throws UnauthorizedException if specified user is not granted permission for accessing the forum.
     */
    public boolean isPublic(long categoryID) throws ForumCategoryNotFoundException, UnauthorizedException {
        return bean.isPublic(categoryID);
    }

    /**
     * <p>Closes specified forum category.</p>
     *
     * @param categoryID a <code>long</code> providing forum category ID.
     * @throws ForumCategoryNotFoundException if requested forum is not found.
     * @throws UnauthorizedException if specified user is not granted permission for accessing the forum.
     */
    public void closeCategory(long categoryID) throws ForumCategoryNotFoundException, UnauthorizedException {
        bean.closeCategory(categoryID);
    }

    /**
     * <p>Checks if specified user can read specified forum category.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param categoryID a <code>long</code> providing forum category ID.
     * @return <code>true</code> if user can read specified forum category; <code>false</code> otherwise.
     * @throws ForumCategoryNotFoundException if requested forum is not found.
     */
    public boolean canReadCategory(long userID, long categoryID) throws ForumCategoryNotFoundException {
        return bean.canReadCategory(userID, categoryID);
    }

    /**
     * <p>Creates watch for specified forum category and user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param categoryID a <code>long</code> providing forum category ID.
     * @throws ForumCategoryNotFoundException if requested forum is not found.
     * @throws UnauthorizedException if specified user is not granted permission for accessing the forum.
     * @throws UserNotFoundException if requested user is not found.
     */
    public void createCategoryWatch(long userID, long categoryID)
        throws ForumCategoryNotFoundException, UnauthorizedException, UserNotFoundException {
        bean.createCategoryWatch(userID, categoryID);
    }

    /**
     * <p>Creates watches for specified forum categories and user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param categoryIDs a <code>long</code> providing forum category IDs.
     * @throws ForumCategoryNotFoundException if requested forum is not found.
     * @throws UnauthorizedException if specified user is not granted permission for accessing the forum.
     * @throws UserNotFoundException if requested user is not found.
     */
    public void createCategoryWatches(long userID, long[] categoryIDs)
        throws ForumCategoryNotFoundException, UnauthorizedException, UserNotFoundException {
        bean.createCategoryWatches(userID, categoryIDs);
    }

    /**
     * <p>Deletes single watch for specified forum category and user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param categoryID a <code>long</code> providing forum category ID.
     * @throws ForumCategoryNotFoundException if requested forum is not found.
     * @throws UnauthorizedException if specified user is not granted permission for accessing the forum.
     * @throws UserNotFoundException if requested user is not found.
     */
    public void deleteCategoryWatch(long userID, long categoryID)
        throws ForumCategoryNotFoundException, UnauthorizedException, UserNotFoundException {
        bean.deleteCategoryWatch(userID, categoryID);
    }

    /**
     * <p>Deletes forum category watches for specified user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param categoryIDs a <code>long</code> array providing forum category IDs.
     * @throws ForumCategoryNotFoundException if requested forum is not found.
     * @throws UnauthorizedException if specified user is not granted permission for accessing the forum.
     * @throws UserNotFoundException if requested user is not found.
     */
    public void deleteCategoryWatches(long userID, long[] categoryIDs)
        throws ForumCategoryNotFoundException, UnauthorizedException, UserNotFoundException {
        bean.deleteCategoryWatches(userID, categoryIDs);
    }

    /**
     * <p>Gets the watches for specified forums and user.</p>
     *
     * @param userID a <code>long</code> providing the user ID.
     * @param categoryIDs a <code>long</code> array providing forum category IDs.
     * @return a <code>long</code> arrasy providing the IDs for watches for specified forums.
     * @throws ForumCategoryNotFoundException if requested forum is not found.
     * @throws UnauthorizedException if specified user is not granted permission for accessing the forum.
     * @throws UserNotFoundException if requested user is not found.
     */
    public long[] areCategoriesWatched(long userID, long[] categoryIDs)
        throws ForumCategoryNotFoundException, UnauthorizedException, UserNotFoundException {
        return bean.areCategoriesWatched(userID, categoryIDs);
    }

    /**
     * <p>Gets the data for specified software forum category.</p>
     *
     * @param categoryID a <code>long</code> providing forum category ID.
     * @return a <code>List</code> providing the data for specified software forum category.
     * @throws ForumCategoryNotFoundException if requested forum is not found.
     */
    public ArrayList getSoftwareForumCategoryData(long categoryID) throws ForumCategoryNotFoundException {
        return bean.getSoftwareForumCategoryData(categoryID);
    }

    /**
     * <p>Gets the data for software forum categories.</p>
     *
     * @return a <code>String</code> array providing the data for software forum categories.
     */
    public String[][] getSoftwareCategoriesData() {
        return bean.getSoftwareCategoriesData();
    }
}
