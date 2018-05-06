/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao;

import java.util.List;
import java.util.Map;

import com.cronos.termsofuse.model.TermsOfUse;

/**
 * <p>
 * This interface defines the dao for manipulating the TermsOfUse to Project links. It simply provides create/delete
 * and search operations on the links.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>getTermsOfUse() method was updated to support filtering of terms of use groups by custom agreeability types
 * instead of member agreeable flag.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> The implementations are required to be thread safe.
 * </p>
 *
 * @author faeton, sparemax, saarixx
 * @version 1.1
 */
public interface ProjectTermsOfUseDao {
    /**
     * This method will create a project role terms of use association.
     *
     * @param groupIndex
     *            the group index to associate.
     * @param resourceRoleId
     *            the role id to associate.
     * @param sortOrder
     *            the association sort order.
     * @param termsOfUseId
     *            the terms of use id to associate.
     * @param projectId
     *            the project id to associate.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public void createProjectRoleTermsOfUse(int projectId, int resourceRoleId, long termsOfUseId, int sortOrder,
        int groupIndex) throws TermsOfUsePersistenceException;

    /**
     * This method will remove a project role terms of use association.
     *
     * @param groupIndex
     *            the group index to associate.
     * @param resourceRoleId
     *            the role id to associate.
     * @param termsOfUseId
     *            the terms of use id to associategroupIndex groupIndex to associate.
     * @param projectId
     *            the project id to associate.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     */
    public void removeProjectRoleTermsOfUse(int projectId, int resourceRoleId, long termsOfUseId, int groupIndex)
        throws EntityNotFoundException, TermsOfUsePersistenceException;

    /**
     * <p>
     * This method retrieves terms of use for specific pair of user and resource role and groups it by terms of use
     * groups. Additionally groups can be filtered by agreeability types.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Replaced includeNonMemberAgreeable:boolean parameter with agreeabilityTypeIds:int[].</li>
     * <li>Throws IllegalArgumentException if agreeabilityTypeIds is empty.</li>
     * </ol>
     * </p>
     *
     * <p>
     * <em>Changes in 1.1.1</em>
     * This method should return a map
     * where the key is the role id and the value is the TOU groups linked to that role.
     * </p>
     *
     * @param resourceRoleId
     *            the role id to associate.
     * @param projectId
     *            the project id to associate.
     * @param agreeabilityTypeIds
     *            the IDs of the agreeability types for terms of use to be retrieved (null if filtering by
     *            agreeability type is not required; if at least one terms of use in the group has agreeability type
     *            with not specified ID, the whole group is ignored)
     *
     * @return Map of lists of terms of use entities. The key of the map is the group id, the value is the
     *         TOUs for this role.
     *
     * @throws IllegalArgumentException
     *             if resourceRoleIds is null or empty array, or agreeabilityTypeIds is empty.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public Map<Integer, List<TermsOfUse>> getTermsOfUse(int projectId, int resourceRoleId,
        int[] agreeabilityTypeIds) throws TermsOfUsePersistenceException;

    /**
     * This method will remove all project role terms of use association for a given project.
     *
     * @param projectId
     *            the project id to remove.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public void removeAllProjectRoleTermsOfUse(int projectId) throws TermsOfUsePersistenceException;
}
