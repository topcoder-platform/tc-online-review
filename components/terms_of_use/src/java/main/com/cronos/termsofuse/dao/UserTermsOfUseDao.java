/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao;

import java.util.List;

import com.cronos.termsofuse.model.TermsOfUse;

/**
 * <p>
 * This interface defines the dao for manipulating the TermsOfUse to User links. It simply provides CRUD operations on
 * the links and several helper operations whether user has specific user terms or ban for them.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> The implementations are required to be thread safe.
 * </p>
 *
 * @author faeton, sparemax
 * @version 1.0
 */
public interface UserTermsOfUseDao {
    /**
     * Records the fact of acceptance of specified terms of use by specified user.
     *
     * @param userId
     *            a long providing the user ID.
     * @param termsOfUseId
     *            a long providing the terms of use ID.
     *
     * @throws UserBannedException
     *             if the user is banned to create terms of use.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public void createUserTermsOfUse(long userId, long termsOfUseId) throws UserBannedException,
        TermsOfUsePersistenceException;

    /**
     * Removes the fact of acceptance of specified terms of use by specified user.
     *
     * @param userId
     *            a long providing the user ID.
     * @param termsOfUseId
     *            a long providing the terms of use ID.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     */
    public void removeUserTermsOfUse(long userId, long termsOfUseId) throws TermsOfUsePersistenceException,
        EntityNotFoundException;

    /**
     * Retrieves terms of use entities associated with the given user from the database.
     *
     * @param userId
     *            a long containing the user id to retrieve terms of use.
     *
     * @return a TermsOfUse list with the requested terms of use or empty list if not found.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public List<TermsOfUse> getTermsOfUseByUserId(long userId) throws TermsOfUsePersistenceException;

    /**
     * Retrieves user ids associated with the given terms of use from the database.
     *
     * @param termsOfUseId
     *            a long containing the terms of use id to retrieve the user ids.
     *
     * @return a list of user ids or empty list if not found.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public List<Long> getUsersByTermsOfUseId(long termsOfUseId) throws TermsOfUsePersistenceException;

    /**
     * Checks if there is a record on the fact of acceptance of specified terms of use by specified user.
     *
     * @param userId
     *            a long providing the user ID.
     * @param termsOfUseId
     *            a long providing the terms of use ID.
     *
     * @return <code>true</code> if specified user accepted the specified terms of use; <code>false</code>
     *         otherwise.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public boolean hasTermsOfUse(long userId, long termsOfUseId) throws TermsOfUsePersistenceException;

    /**
     * Checks if there is a record on the fact of banning the specified user from accepting the specified terms of
     * use.
     *
     * @param userId
     *            a long providing the user ID.
     * @param termsOfUseId
     *            a long providing the terms of use ID.
     *
     * @return <code>true</code> if specified user has ban for the specified terms of use; <code>false</code>
     *         otherwise.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public boolean hasTermsOfUseBan(long userId, long termsOfUseId) throws TermsOfUsePersistenceException;
}
