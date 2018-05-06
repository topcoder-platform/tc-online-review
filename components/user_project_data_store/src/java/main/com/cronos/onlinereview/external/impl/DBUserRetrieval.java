/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalObject;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RatingInfo;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserProjectDataStoreHelper;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.external.RatingType;
import com.topcoder.db.connectionfactory.DBConnectionFactory;

/**
 * <p>
 * This is the Database implementation of the <code>{@link UserRetrieval}</code> interface.
 * </p>
 * <p>
 * All the methods (except <code>retrieveUser(long)</code> and <code>retrieveUser(String)</code>) call
 * <code>super.getConnection</code> to get a connection from the DBConnectionFactory, and then call to
 * <code>super.retrieveObjects</code>, which calls <code>this.createObject</code>.
 * </p>
 * <p>
 * Then, they call <code>updateEmails</code> and <code>updateRatings</code>. Afterwards, the prepared statement,
 * result set and connections are all closed using <code>super.close()</code>.
 * </p>
 * <p>
 * All <code>SQLException</code>s in all methods should be wrapped in <code>RetrievalException</code>.
 * </p>
 * <p>
 * <b>Thread Safety</b>: This class is immutable and therefore thread-safe.
 * </p>
 *
 * @author dplass, oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class DBUserRetrieval extends BaseDBRetrieval implements UserRetrieval {

    /**
     * <p>
     * The string denotes the database column name of the user id.
     * </p>
     */
    private static final String ID_STRING = "id";

    /**
     * <p>
     * The string denotes the database column name of the user first name.
     * </p>
     */
    private static final String FIRST_NAME_STRING = "first_name";

    /**
     * <p>
     * The string denotes the database column name of the user last name.
     * </p>
     */
    private static final String LAST_NAME_STRING = "last_name";

    /**
     * <p>
     * The string denotes the database column name of the user handle.
     * </p>
     */
    private static final String HANDLE_STRING = "handle";

    /**
     * <p>
     * The string denotes the database column name of the user address.
     * </p>
     */
    private static final String ADDRESS_STRING = "address";

    /**
     * <p>
     * The string denotes the database column name of the user phase id.
     * </p>
     */
    private static final String PHASE_ID_STRING = "phaseId";

    /**
     * <p>
     * The string denotes the database column name of the user rating.
     * </p>
     */
    private static final String RATING_STRING = "rating";

    /**
     * <p>
     * The string denotes the database column name of the user volatility.
     * </p>
     */
    private static final String VOLATILITY_STRING = "volatility";

    /**
     * <p>
     * The string denotes the database column name of the user reliability.
     * </p>
     */
    private static final String RELIABILITY_STRING = "reliability";

    /**
     * <p>
     * The string denotes the database column name of the user numRatings.
     * </p>
     */
    private static final String NUMBER_RATING_STRING = "numRatings";

    /**
     * <p>
     * Constructs this object with the given parameters, by calling the super constructor.
     * </p>
     *
     * @param connFactory
     *            the connection factory to use with this object.
     * @param connName
     *            the connection name to use when creating connections.
     * @throws IllegalArgumentException
     *             if either parameter is <code>null</code> or connName is empty after trimmed.
     * @throws ConfigException
     *             if the connection name doesn't correspond to a connection the factory knows about.
     */
    public DBUserRetrieval(DBConnectionFactory connFactory, String connName) throws ConfigException {
        super(connFactory, connName);
    }

    /**
     * <p>
     * Constructs this object with the given namespace by calling the super constructor.
     * </p>
     *
     * @param namespace
     *            the name of the ConfigManager namespace; see <code>BaseDBRetrieval(String)</code> for details.
     * @throws IllegalArgumentException
     *             if the parameter is <code>null</code> or empty after trim.
     * @throws ConfigException
     *             if the namespace could not be found, or if the connection factory. could not be instantiated with the
     *             given namespace, or if the connection name is unknown to the connection factory.
     */
    public DBUserRetrieval(String namespace) throws ConfigException {
        super(namespace);
    }

    /**
     * <p>
     * Retrieves the external user with the given id.
     * </p>
     * <p>
     * Simply calls retrieveUsers(long[]) and returns the first entry in the array. If the array is empty, return null.
     * </p>
     *
     * @return the external user who has the given id, or null if not found.
     * @param id
     *            the id of the user we are interested in.
     * @throws IllegalArgumentException
     *             if id is not positive.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    public ExternalUser retrieveUser(long id) throws RetrievalException {
        // Gets Users by calling retrieveUsers(long[]).
        ExternalUser[] users = retrieveUsers(new long[] {id});

        // If the array is empty, return null; else, return this first one.
        return (ExternalUser) UserProjectDataStoreHelper.retFirstObject(users);
    }

    /**
     * <p>
     * Retrieves the external user with the given handle.
     * </p>
     * <p>
     * Simply calls retrieveUsers(String[]) and returns the first entry in the array. If the array is empty, return
     * null.
     * </p>
     *
     * @return the external user who has the given handle, or null if not found.
     * @param handle
     *            the handle of the user we are interested in.
     * @throws IllegalArgumentException
     *             if handle is <code>null</code> or empty after trim.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    public ExternalUser retrieveUser(String handle) throws RetrievalException {
        // Gets Users by calling retrieveUsers(String[]).
        ExternalUser[] users = retrieveUsers(new String[] {handle});

        // If the array is empty, return null; else, return this first one.
        return (ExternalUser) UserProjectDataStoreHelper.retFirstObject(users);
    }

    /**
     * <p>
     * Retrieves the external users with the given ids. Note that retrieveUsers(ids)[i] will not necessarily correspond
     * to ids[i].
     * </p>
     * <p>
     * If an entry in ids was not found, no entry in the return array will be present. If there are any duplicates in
     * the input array, the output will NOT contain a duplicate External User.
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
    public ExternalUser[] retrieveUsers(long[] ids) throws RetrievalException {
        UserProjectDataStoreHelper.validateArray(ids, "ids");

        if (ids.length == 0) {
            return new ExternalUser[0];
        }

        // Selects users by ids.
        String queryAndClause = " AND u.user_id in ";

        // Delegates to retrieveUsers(String, Object, int, boolean).
        return retrieveUsers(queryAndClause, ids, ids.length, false);
    }

    /**
     * <p>
     * Retrieves the external users with the given handles. Note that retrieveUsers(handles)[i] will not necessarily
     * correspond to handles[i].
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
    public ExternalUser[] retrieveUsers(String[] handles) throws RetrievalException {
        UserProjectDataStoreHelper.validateArray(handles, "handles");

        if (handles.length == 0) {
            return new ExternalUser[0];
        }

        // Selects users by handles.
        String queryAndClause = " AND u.handle in ";

        // Delegates to retrieveUsers(String, Object, int, boolean).
        return retrieveUsers(queryAndClause, handles, handles.length, false);
    }

    /**
     * <p>
     * Retrieves the external users with the given handles, ignoring case when doing the retrieval. Note that
     * retrieveUsers(handles)[i] will not necessarily correspond to handles[i].
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
    public ExternalUser[] retrieveUsersIgnoreCase(String[] handles) throws RetrievalException {
        UserProjectDataStoreHelper.validateArray(handles, "handles");

        if (handles.length == 0) {
            return new ExternalUser[0];
        }

        // Selects users by handles.
        String queryAndClause = " AND u.handle_lower in ";

        // Delegates to retrieveUsers(String, Object, int, boolean).
        return retrieveUsers(queryAndClause, handles, handles.length, true);
    }

    /**
     * <p>
     * Retrieves the external users whose first and last name start with the given first and last name. If either
     * parameter is empty, it will be ignored. (If both parameters are empty, this is an exception.)
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
     *             if firstName or lastName is <code>null</code>, or if BOTH are empty after trim.
     *             (I.e., if one is empty after trim, and the other is not empty after trim, this is not an exception.)
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    public ExternalUser[] retrieveUsersByName(String firstName, String lastName) throws RetrievalException {
        UserProjectDataStoreHelper.validateNull(firstName, "firstName");
        UserProjectDataStoreHelper.validateNull(lastName, "lastName");
        if (firstName.trim().equals("") && lastName.trim().equals("")) {
            throw new IllegalArgumentException("The firstName and lastName cannot both be empty.");
        }

        // Creates an ArrayList for storing the names.
        List names = new ArrayList();
        names.add(firstName);
        names.add(lastName);

        // Selects users by names.
        String queryAndClause = " AND u.first_name like ? and u.last_name like ?";

        // Delegates to retrieveUsers(String, Object, int, boolean).
        return retrieveUsers(queryAndClause, names, names.size(), false);
    }

    /**
     * <p>
     * Retrieves the external users with the given ids or handles(ignore case and not) or first name and last name.
     * </p>
     * <p>
     * All the other retrieveUsers method delegate to this one.
     * </p>
     *
     * @param queryAndClause
     *            the query of the prepareStatement, given be the caller.
     * @param parameters
     *            the parameter of the query, it can be long[] or String[], due to the different caller.
     * @param parametersLength
     *            the length of the parameters array.
     * @param ignoreCase
     *            whether need to ignore case.
     * @return an array of external users whose first name and last name start with the given first and last name. If no
     *         users match, an empty array will be returned.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    private ExternalUser[] retrieveUsers(String queryAndClause, Object parameters, int parametersLength,
            boolean ignoreCase) throws RetrievalException {
        // Selects Users.
        Map objects = selectUsers(queryAndClause, parameters, parametersLength, ignoreCase);

        // Selects and then updates email and rating of the users.
        selectEmail(queryAndClause, parameters, parametersLength, objects, ignoreCase);
        selectRating(queryAndClause, parameters, parametersLength, objects, ignoreCase);

        // Convert the map to an array.
        ExternalUser[] users = (ExternalUser[]) new LinkedList(objects.values()).toArray(new ExternalUser[0]);

        return users;
    }

    /**
     * <p>
     * Creates an ExternalUserImpl from the columns in the given result set.
     * </p>
     * <p>
     * This method is called by super.retrieveObjects which is why it must return an ExternalObject instead of an
     * ExternalUserImpl.
     * </p>
     *
     * @param rs
     *            a result set row which contains the columns needed to instantiate an ExternalUserImpl object.
     * @return an ExternalUserImpl with the columns of the given result set.
     * @throws RetrievalException
     *             if rs didn't contain the required columns, or if any of them could not be retrieved.
     */
    protected ExternalObject createObject(ResultSet rs) throws RetrievalException {
        ExternalUserImpl retUserImpl;

        try {
            long id = rs.getLong(ID_STRING);
            String handle = rs.getString(HANDLE_STRING);
            String firstName = rs.getString(FIRST_NAME_STRING);
            String lastName = rs.getString(LAST_NAME_STRING);
            String email = rs.getString(ADDRESS_STRING);

            // Creates the ExternalUserImpl instance.
            retUserImpl = new ExternalUserImpl(id, handle, firstName, lastName, email);

        } catch (SQLException e) {
            throw new RetrievalException("Some of the user values cannot be retrieved.", e);
        }

        return retUserImpl;
    }

    /**
     * <p>
     * This private method is used by all the other retrieve methods, to update the respective alternate email addresses
     * of the given objects using the prepared statement.
     * </p>
     *
     * @param objects
     *            a map from Long (id) to ExternalUserImpl.
     * @param ps
     *            a prepared statement with the following two fields: id (as a long), address (as a varchar).
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    private void updateEmails(Map objects, PreparedStatement ps) throws RetrievalException {
        ResultSet rs = null;

        try {
            // Executes the PreparedStatement to get the ResultSet.
            rs = ps.executeQuery();

            while (rs.next()) {
                // Gets values from the ResultSet.
                long id = rs.getLong(ID_STRING);
                String email = rs.getString(ADDRESS_STRING);

                // Gets ExternalUserImpl instance due to the id, and add AlternativeEmail into it.
                ExternalUserImpl userImpl = (ExternalUserImpl) objects.get(id);
                if (userImpl != null) {
                    userImpl.addAlternativeEmail(email);
                }
            }

        } catch (SQLException e) {
            throw new RetrievalException("ResultSet execute error or some of the user email values "
                    + "cannot be retrieved.", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * <p>
     * This private method is used by all the other retrieve methods, to update the ratings information of the given
     * objects using the prepared statement.
     * </p>
     *
     * @param objects
     *            a map from Long (id) to ExternalUserImpl.
     * @param ps
     *            a prepared statement with the following six fields: id, rating, phaseId, volatility, reliability and
     *            numRatings.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    private void updateRatings(Map objects, PreparedStatement ps) throws RetrievalException {
        ResultSet rs = null;
        try {
            // Executes the PreparedStatement to get the ResultSet.
            rs = ps.executeQuery();

            while (rs.next()) {
                // Gets values from the ResultSet.
                long id = rs.getLong(ID_STRING);
                int phaseId = rs.getInt(PHASE_ID_STRING);
                int rating = rs.getInt(RATING_STRING);
                int volatility = rs.getInt(VOLATILITY_STRING);
                double reliability = rs.getDouble(RELIABILITY_STRING);
                int numRatings = rs.getInt(NUMBER_RATING_STRING);

                // Gets the correct RatingType due to the phase id.
                RatingType ratingType = RatingType.getRatingType(phaseId);

                // Creates the RatingInfo instance by the retrieved values.
                RatingInfo ratingInfo;
                if (rs.wasNull()) {
                    // No reliability, use 4-arg ctor.
                    ratingInfo = new RatingInfo(ratingType, rating, numRatings, volatility);
                } else {
                    // Has reliability, use 5-arg ctor.
                    ratingInfo = new RatingInfo(ratingType, rating, numRatings, volatility, reliability);
                }

                // Gets ExternalUserImpl instance due to the id, and add RatingInfo into it.
                ExternalUserImpl userImpl = (ExternalUserImpl) objects.get(id);
                if (userImpl != null) {
                    userImpl.addRatingInfo(ratingInfo);
                }
            }
        } catch (SQLException e) {
            throw new RetrievalException("ResultSet execute error or some of the user rating values "
                    + "cannot be retrieved.", e);
        } catch (IllegalArgumentException e) {
            throw new RetrievalException("Some parameters of RatingType.getRatingType() or "
                    + "RatingInfo.ctor() invalid.", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * <p>
     * Selects users by using the userQuery, and then call the super.retrieveObjects to get the Map of the user for
     * returning.
     * </p>
     *
     * @param queryAndClause
     *            the query of the prepareStatement, given be the caller.
     * @param queryParameter
     *            the parameter of the query, it can be long[] or String[], due to the different caller.
     * @param paramLength
     *            the length of the queryParameter array.
     * @param ignoreCase
     *            whether need to ignore case.
     * @return the Map of the user.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    private Map selectUsers(String queryAndClause, Object queryParameter, int paramLength, boolean ignoreCase)
            throws RetrievalException {
        // Opens the connection.
        Connection conn = super.getConnection();

        // Constructs the query string.
        String userQuery = "SELECT u.user_id id, first_name, last_name, handle, address "
                + "FROM user u, email WHERE u.user_id = email.user_id AND email.primary_ind = 1 ";

        // Prepares the PreparedStatement.
        PreparedStatement ps = generatePreparedStatement(userQuery, queryAndClause, queryParameter, paramLength, conn,
                "userQuery", ignoreCase);

        // Calls super method which calls this.createObject.
        Map objects = super.retrieveObjects(ps);

        // Closes the Prepared Statement and connection.
        super.close(ps, conn);

        return objects;
    }

    /**
     * <p>
     * Selects emails by using the emailQuery, and then call the private method updateEmails to update the email
     * information of the user.
     * </p>
     *
     * @param queryAndClause
     *            the query of the prepareStatement, given be the caller.
     * @param queryParameter
     *            the parameter of the query, it can be long[] or String[], due to the different caller.
     * @param paramLength
     *            the length of the queryParameter array.
     * @param objects
     *            the Map of the user retrieved from the selectUsers method.
     * @param ignoreCase
     *            whether need to ignore case.
     * @return the Map of the user after email updated.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    private Map selectEmail(String queryAndClause, Object queryParameter, int paramLength, Map objects,
            boolean ignoreCase) throws RetrievalException {
        // Opens the connection.
        Connection conn = super.getConnection();

        // Prepares the email query.
        String emailQuery = "SELECT u.user_id id, address FROM user u, email "
                + "WHERE u.user_id = email.user_id AND email.primary_ind = 0 ";

        // Prepares the PreparedStatement.
        PreparedStatement ps = generatePreparedStatement(emailQuery, queryAndClause, queryParameter, paramLength, conn,
                "emailQuery", ignoreCase);

        // Update emails.
        updateEmails(objects, ps);

        // Closes the Prepared Statement and connection.
        super.close(ps, conn);

        return objects;
    }

    /**
     * <p>
     * Selects ratings by using the ratingsQuery, and then call the private method updateRatings to update the rating
     * information of the user.
     * </p>
     *
     * @param queryAndClause
     *            the query of the prepareStatement, given be the caller.
     * @param queryParameter
     *            the parameter of the query, it can be long[] or String[], due to the different caller.
     * @param paramLength
     *            the length of the queryParameter array.
     * @param objects
     *            the Map of the user retrieved from the selectUsers method.
     * @param ignoreCase
     *            whether need to ignore case.
     * @return the Map of the user after rating updated.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    private Map selectRating(String queryAndClause, Object queryParameter, int paramLength, Map objects,
            boolean ignoreCase) throws RetrievalException {
        // Opens the connection.
        Connection conn = super.getConnection();

        // Prepares the ratings query.
        String ratingsQuery = "SELECT u.user_id id, r.rating rating, r.phase_id phaseId, "
                + "vol volatility, num_ratings numRatings, ur.rating reliability "
                + "FROM user u, user_rating r, OUTER user_reliability ur WHERE u.user_id = r.user_id "
                + "AND u.user_id = ur.user_id AND r.phase_id = ur.phase_id ";

        // Prepares the PreparedStatement.
        PreparedStatement ps = generatePreparedStatement(ratingsQuery, queryAndClause, queryParameter, paramLength,
                conn, "ratingsQuery", ignoreCase);

        // Update ratings.
        updateRatings(objects, ps);

        // Closes the Prepared Statement and connection.
        super.close(ps, conn);

        return objects;
    }

    /**
     * <p>
     * This private method is used for generating the PreparedStatement, and having all the parameters of the
     * PreparedStatement been set correctly.
     * </p>
     *
     * @param query
     *            the query for creating the PreparedStatement.
     * @param queryAndClause
     *            the query of the prepareStatement, given be the caller.
     * @param queryParameter
     *            the parameter of the query, it can be long[] or String[], due to the different caller.
     * @param paramLength
     *            the length of the queryParameter array.
     * @param conn
     *            the connection for creating the PreparedStatement.
     * @param name
     *            the name of the query.
     * @param ignoreCase
     *            whether need to ignore case.
     * @return the created PreparedStatement.
     * @throws RetrievalException
     *             database access error occurs while preparing the statement or setting the parameters.
     */
    private PreparedStatement generatePreparedStatement(String query, String queryAndClause, Object queryParameter,
            int paramLength, Connection conn, String name, boolean ignoreCase) throws RetrievalException {
        // Appends the query.
        query += queryAndClause;
        if ((queryParameter instanceof long[]) || (queryParameter instanceof String[])) {
            query += UserProjectDataStoreHelper.generateQuestionMarks(paramLength);
        }

        // Prepares the statement.
        PreparedStatement ps = UserProjectDataStoreHelper.createStatement(conn, query, name);

        // Sets parameters.
        try {

            if (queryParameter instanceof long[]) {
                // Sets the long values.
                for (int i = 0; i < paramLength; i++) {
                    ps.setLong(i + 1, ((long[]) queryParameter)[i]);
                }

            } else if (queryParameter instanceof String[]) {
                // Sets the String values, be care of the ignoreCase.
                for (int i = 0; i < paramLength; i++) {
                    String value = ((String[]) queryParameter)[i];
                    if (ignoreCase) {
                        value = value.toLowerCase();
                    }
                    ps.setString(i + 1, value);
                }

            } else if (queryParameter instanceof ArrayList) {
                // Sets the names.
                for (int i = 0; i < paramLength; i++) {
                    ps.setString(i + 1, ((String) (((ArrayList) queryParameter).get(i))).trim() + "%");
                }
            }
        } catch (SQLException e) {
            throw new RetrievalException("Database access error occurs while setting the " + name + " parameters.", e);
        }

        return ps;
    }
}
