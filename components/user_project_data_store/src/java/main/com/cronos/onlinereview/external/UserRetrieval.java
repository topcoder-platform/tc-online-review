/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

/**
 * <p>
 * This is the interface for the <b>User Project Data Store</b> component which retrieves users from persistent
 * storage.
 * </p>
 * <p>
 * There are two methods to retrieve individual users (by id or handle), and additional methods that retrieve sets of
 * users in bulk.
 * </p>
 * <p>
 * <b>Thread Safety</b>: Implementations of this interface should be implemented in a type-safe manner.
 * </p>
 *
 * @author dplass, oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public interface UserRetrieval {

    /**
     * <p>
     * Retrieves the external user with the given id.
     * </p>
     *
     * @param id
     *            the id of the user we are interested in.
     * @return the external user who has the given id, or null if not found.
     * @throws IllegalArgumentException
     *             if id is not positive.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    ExternalUser retrieveUser(long id) throws RetrievalException;

    /**
     * <p>
     * Retrieves the external user with the given handle.
     * </p>
     *
     * @param handle
     *            the handle of the user we are interested in.
     * @return the external user who has the given handle, or null if not found.
     * @throws IllegalArgumentException
     *             if handle is <code>null</code> or empty after trim.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    ExternalUser retrieveUser(String handle) throws RetrievalException;

    /**
     * <p>
     * Retrieves the external users with the given ids.
     * </p>
     * <p>
     * Note that retrieveUsers(ids)[i] will not necessarily correspond to ids[i].
     * </p>
     * <p>
     * If an entry in ids was not found, no entry in the return array will be present. If there are any duplicates in
     * the input array, the output need NOT contain a duplicate External User.
     * </p>
     *
     * @param ids
     *            the ids of the users we are interested in.
     * @return an array of external users who have the given ids. If none of the given ids were found, an empty array
     *         will be returned. The index of the entries in the array will not necessarily directly correspond to the
     *         entries in the ids array.
     * @throws IllegalArgumentException
     *             if ids is <code>null</code> or any entry is not positive.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    ExternalUser[] retrieveUsers(long[] ids) throws RetrievalException;

    /**
     * <p>
     * Retrieves the external users with the given handles.
     * </p>
     * <p>
     * Note that retrieveUsers(handles)[i] will not necessarily correspond to handles[i].
     * </p>
     * <p>
     * If an entry in handles was not found, no entry in the return array will be present. If there are any duplicates
     * in the input array, the output will NOT contain a duplicate External User.
     * </p>
     *
     * @param handles
     *            the handles of the users we are interested in.
     * @return an array of external users who have the given handles. If none of the given handles were found, an empty
     *         array will be returned. The entries in the array will not necessarily directly correspond to the entries
     *         in the handles array.
     * @throws IllegalArgumentException
     *             if handles is <code>null</code> or any entry is <code>null</code>, or empty after trim.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    ExternalUser[] retrieveUsers(String[] handles) throws RetrievalException;

    /**
     * <p>
     * Retrieves the external users with the given handles, ignoring case when doing the retrieval.
     * </p>
     * <p>
     * Note that retrieveUsers(handles)[i] will not necessarily correspond to handles[i].
     * </p>
     * <p>
     * If an entry in handles was not found, no entry in the return array will be present. If more than one entry was
     * found (e.g., "SMITH" and "smith" were both found for the input "Smith") per input handle, then they will both be
     * in the output array. Conversely, if there are two entries in the input array ("SMITH" and "smith") and there is
     * only a single corresponding user ("Smith") then there would only be a single entry (for "Smith") in the output
     * array.
     * </p>
     *
     * @param handles
     *            the handles of the users we are interested in finding by the lower-case version of their handle.
     * @return an array of external users who have the given handles. If none of the given handles were found, an empty
     *         array will be returned. The entries in the array will not necessarily directly correspond to the entries
     *         in the handles array.
     * @throws IllegalArgumentException
     *             if handles is <code>null</code> or any entry is <code>null</code>, or empty after trim.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    ExternalUser[] retrieveUsersIgnoreCase(String[] handles) throws RetrievalException;

    /**
     * <p>
     * Retrieves the external users whose first and last name start with the given first and last name.
     * </p>
     * <p>
     * If either parameter is empty, it will be ignored. (If both parameters are empty, this is an exception.)
     * </p>
     * <p>
     * To search for all users whose last name starts with "Smith", call retrieveUsersByName("", "Smith"). (It is case
     * <b>sensitive</b>.) To search for all users whose first name starts with "Jon" and last name starts with "Smith",
     * call retrieveUsersByname("Jon", "Smith").
     * </p>
     *
     * @param firstName
     *            the first name of the user(s) to find, or the empty string to represent "any first name". Both
     *            firstName and lastName cannot be empty.
     * @param lastName
     *            the last name of the user(s) to find, or the empty string to represent "any last name". Both firstName
     *            and lastName cannot be empty.
     * @return an array of external users whose first name and last name start with the given first and last name. If no
     *         users match, an empty array will be returned.
     * @throws IllegalArgumentException
     *             if firstName or lastName is <code>null</code>, or if BOTH are empty after trim. (I.e., if one is
     *             empty after trim, and the other is not empty after trim, this is not an exception.)
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    ExternalUser[] retrieveUsersByName(String firstName, String lastName) throws RetrievalException;
}
