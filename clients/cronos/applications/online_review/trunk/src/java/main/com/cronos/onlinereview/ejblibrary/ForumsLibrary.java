/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import com.jivesoftware.base.UnauthorizedException;
import com.jivesoftware.base.UserNotFoundException;
import com.jivesoftware.forum.ForumCategoryNotFoundException;
import com.topcoder.web.ejb.forums.Forums;
import com.topcoder.web.ejb.forums.ForumsBean;
import com.topcoder.web.ejb.forums.ForumThreadData;
import com.topcoder.web.ejb.forums.ForumsException;
import com.topcoder.web.ejb.forums.ForumsSpecReviewComment;
import com.topcoder.web.ejb.forums.ForumsUserComment;

/**
 * <p>An implementation of {@link Forums} interface which provides the library-call style for API of <code>Forums EJB
 * </code>.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ForumsLibrary extends BaseEJBLibrary implements Forums {

    /**
     * <p>A <code>ForumsBean</code> which is delegated the processing of the calls to methods of this class.</p>
     */
    private final ForumsBean bean;

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
     * @return a <code>long</code> array providing the IDs for watches for specified forums.
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

    public long addSpecReviewComment(long categoryId, long userId, long questionId, ForumsUserComment comment)
                    throws EJBException, RemoteException, ForumsException {
        return bean.addSpecReviewComment(categoryId, userId, questionId, comment);
    }

    public List<ForumsSpecReviewComment> getSpecReviewComments(long categoryId) throws EJBException, RemoteException,
                    ForumsException {
        return bean.getSpecReviewComments(categoryId);
    }

    public void updateSpecReviewComment(long categoryId, long userId, long questionId, ForumsUserComment comment)
                    throws EJBException, RemoteException, ForumsException {
        bean.updateSpecReviewComment(categoryId, userId, questionId, comment);
    }

    public long[] areForumsWatched(long userID, long[] forumIDs) throws Exception {
        return bean.areForumsWatched(userID, forumIDs);
    }

    public void deleteForumWatches(long userID, long[] forumIDs) throws Exception {
        bean.deleteForumWatches(userID, forumIDs);
    }

    public void createForumWatches(long userID, long[] forumIDs) throws Exception {
        bean.createForumWatches(userID, forumIDs);
    }

    public void deleteForumWatch(long userID, long forumID) throws Exception {
        bean.deleteForumWatch(userID, forumID);
    }

    public void removeUserPermission(long userID, long forumCategoryID) throws Exception {
        bean.removeUserPermission(userID, forumCategoryID);
    }

    public long createTopCoderDirectProjectForums(String projectName, Long tcDirectProjectTypeId) throws
            Exception {
        return bean.createTopCoderDirectProjectForums(projectName, tcDirectProjectTypeId);
    }
    

    public long postThreadToQuestionForum(long categoryId, String subject, String body, long userId) throws Exception
    {
        return bean.postThreadToQuestionForum(categoryId, subject, body, userId );
    }
    
    

    public long createTopCoderDirectProjectForums(String projectName, Long tcDirectProjectTypeId,
            Map<String, String> forums) throws Exception
    {
        return bean.createTopCoderDirectProjectForums(projectName, tcDirectProjectTypeId, forums);
    }
    
    public void updateStudioForumName(long categoryID, String name) throws Exception
    {
        bean.updateStudioForumName(categoryID, name);
    }
    
        /**
     * <p>
     * Adds a forum to the existing TopCoder Direct project forum category.
     * </p>
     * @param forumCategoryId the TopCoder Direct project forum category id.
     * @param forumName the name of the forum to be added.
     * @param forumDescription the description of the forum to be added.
     * @return the id of the added forum.
     * @throws EJBException if an unexpected error occurs.
     * @throws Exception if an unexpected error occurs.
     */
    public long addTopCoderDirectProjectForum(long forumCategoryId, String forumName, String forumDescription)
            throws Exception
    {
        return bean.addTopCoderDirectProjectForum(forumCategoryId, forumName, forumDescription);
    }

    /**
     * <p>
     * Deletes an existing TopCoder Direct project forum.
     * </p>
     * @param forumCategoryId the id of the forum category.
     * @param forumId the id of the forum to be deleted.
     * @throws EJBException if an unexpected error occurs.
     * @throws Exception if an unexpected error occurs.
     */
    public void deleteTopCoderDirectProjectForum(long forumCategoryId, long forumId) throws Exception
    {
        bean.deleteTopCoderDirectProjectForum(forumCategoryId, forumId);
    }
    
    /**
     * Migrate the CloudSpokes Challenge discussions data to TopCoder forum. It creates a software forum and inserts
     * the given forum data into the software forum and assign permission for the contest users and admins. Finally
     * it updates the contest info (project_info) to link the created forum to the contest.
     *
     * @param contestId the id of the TopCoder contest
     * @param compVersionId the component version id
     * @param contestName the contest name
     * @param forumData the forum data
     * @param userIds the user ids to give the forum user permission
     * @param adminIds the user ids to give the forum admin permission
     * @param postUserId the user id to post the thread
     * @throws ForumsException if there is any error
     * @return the forum category id created
     */
    public long migrateCloudSpokesForumData(long contestId, long compVersionId, String contestName,
                                            ForumThreadData forumData, Long[] userIds,
                                            Long[] adminIds, long postUserId) throws ForumsException {
        return bean.migrateCloudSpokesForumData(contestId, compVersionId, contestName, forumData, userIds, adminIds, postUserId);
                                            
    }
}
