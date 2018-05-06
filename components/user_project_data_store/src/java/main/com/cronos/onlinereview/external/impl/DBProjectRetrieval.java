/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalObject;
import com.cronos.onlinereview.external.ExternalProject;
import com.cronos.onlinereview.external.ProjectRetrieval;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserProjectDataStoreHelper;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * <p>
 * This is the Database implementation of the <code>{@link ProjectRetrieval}</code> interface.
 * </p>
 * <p>
 * All the methods (except <code>retrieveProject(long)</code>) call <code>super.getConnection()</code> to get a
 * connection from the <code>{@link DBConnectionFactory}</code>, and then call to <code>super.retrieveObjects</code>,
 * which calls <code>this.createObject</code>. Afterwards, the prepared statement, result set and connections are all
 * closed using <code>super.close()</code>.
 * </p>
 * <p>
 * All <code>{@link SQLException}</code>s in all methods should be wrapped in <code>{@link RetrievalException}</code>.
 * </p>
 * <p>
 * <b>New in Version 1.1</b>:<br>
 * Short description, functional description, and technologies associated with a component are loaded.
 * </p>
 * <p>
 * <b>Thread Safety</b>: This class is immutable and therefore thread-safe.
 * </p>
 *
 * @author dplass, oodinary
 * @author sql_lall, FireIce
 * @version 1.1
 * @since 1.0
 */
public class DBProjectRetrieval extends BaseDBRetrieval implements ProjectRetrieval {

    /**
     * <p>
     * The name of the forum type property in the config file.
     * </p>
     */
    private static final String FORUM_TYPE_STRING = "forumType";

    /**
     * <p>
     * The default value of the forum type, it is 2.
     * </p>
     */
    private static final int FORUM_TYPE_DEFAULT_VALUE = 2;

    /**
     * <p>
     * The string denotes the database column name of the component version id.
     * </p>
     */
    private static final String COMPONENT_VERSION_ID_STRING = "comp_vers_id";

    /**
     * <p>
     * The string denotes the database column name of the version.
     * </p>
     */
    private static final String VERSION_STRING = "version";

    /**
     * <p>
     * The string denotes the database column name of the version text.
     * </p>
     */
    private static final String VERSION_TEXT_STRING = "version_text";

    /**
     * <p>
     * The string denotes the database column name of the component id.
     * </p>
     */
    private static final String COMPONENT_ID_STRING = "component_id";

    /**
     * <p>
     * The string denotes the database column name of the comments.
     * </p>
     */
    private static final String COMMENTS_STRING = "comments";

    /**
     * <p>
     * The string denotes the database column name of the forum id.
     * </p>
     */
    private static final String FORUM_ID_STRING = "forum_id";

    /**
     * <p>
     * The string denotes the database column name of the component name.
     * </p>
     */
    private static final String COMPONENT_NAME_STRING = "component_name";

    /**
     * <p>
     * The string denotes the database column name of the description.
     * </p>
     */
    private static final String DESCRIPTION_STRING = "description";

    /**
     * <p>
     * The string denotes the database column name of the short description.
     * </p>
     */
    private static final String SHORT_DESCRIPTION_STRING = "short_desc";

    /**
     * <p>
     * The string denotes the database column name of the functional description.
     * </p>
     */
    private static final String FUNCTIONAL_DESCRIPTION_STRING = "function_desc";

    /**
     * <p>
     * The string denotes the database column name of the root category id.
     * </p>
     */
    private static final String ROOT_CATEGORY_ID_STRING = "category_id";

    /**
     * <p>
     * The string denotes the database column name of the technology name.
     * </p>
     */
    private static final String TECHNOLOGY_STRING = "technology";

    /**
     * <p>
     * The forum type code to use in the retrieve methods.
     * </p>
     * <p>
     * Sets by the constructor and never modified. The value will never be negative.
     * </p>
     */
    private final int forumType;

    /**
     * <p>
     * Constructs this object with the given parameters, by calling the super constructor.
     * </p>
     *
     * @param connFactory
     *            the connection factory to use with this object.
     * @param connName
     *            the connection name to use when creating connections.
     * @param forumType
     *            the forum type code to use in the retrieve queries.
     * @throws IllegalArgumentException
     *             if connFactory or connName is <code>null</code> or if forumType is negative or connName is empty
     *             after trimmed.
     * @throws ConfigException
     *             if the connection name doesn't correspond to a connection the factory knows about.
     */
    public DBProjectRetrieval(DBConnectionFactory connFactory, String connName, int forumType) throws ConfigException {
        super(connFactory, connName);

        UserProjectDataStoreHelper.validateNegative(forumType, "forumType");
        this.forumType = forumType;
    }

    /**
     * <p>
     * Constructs this object with the given namespace by calling the super constructor.
     * </p>
     * <p>
     * Retrieves the forum type property named "forumType" from the namespace to be used in each of the retrieve
     * methods.
     * </p>
     * <p>
     * If not present as a property, the forum type code defaults to 2.
     * </p>
     *
     * @param namespace
     *            the name of the ConfigManager namespace; see BaseDBRetrieval(String) for details.
     * @throws IllegalArgumentException
     *             if the parameter is <code>null</code> or empty after trim.
     * @throws ConfigException
     *             if the namespace could not be found, or if the connection factory could not be instantiated with the
     *             given namespace, or if the connection name is unknown to the connection factory, or if the
     *             "forumType" property was not positive.
     */
    public DBProjectRetrieval(String namespace) throws ConfigException {
        super(namespace);

        try {
            // Gets the string value of the forumType from the config file.
            String forumTypeStr = (String) ConfigManager.getInstance().getProperty(namespace, FORUM_TYPE_STRING);

            if (forumTypeStr == null) {
                // Sets the forumType value to 2 as default.
                this.forumType = FORUM_TYPE_DEFAULT_VALUE;
            } else {
                int forumTypeInt = Integer.parseInt(forumTypeStr);

                // If the forumType property is not positive.
                if (forumTypeInt <= 0) {
                    throw new ConfigException("The forumType property should be positive.");
                }
                this.forumType = forumTypeInt;
            }
        } catch (NumberFormatException e) {
            throw new ConfigException("The forumType property cannot be parsed into an integer.", e);
        } catch (UnknownNamespaceException e) {
            throw new ConfigException("The namespace of the ConfigManager could not be found.", e);
        }
    }

    /**
     * <p>
     * Retrieves the external project with the given id.
     * </p>
     * <p>
     * Simply calls retrieveProjects(long[]) and returns the first entry in the array. If the array is empty, return
     * null.
     * </p>
     *
     * @param id
     *            the id of the project we are interested in.
     * @return the external project which has the given id, or null if not found.
     * @throws IllegalArgumentException
     *             if id is not positive.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    public ExternalProject retrieveProject(long id) throws RetrievalException {
        // Gets projects by calling retrieveProjects(long[]).
        ExternalProject[] projects = retrieveProjects(new long[] {id});

        // If the array is empty, return null; else, return this first one.
        return (ExternalProject) UserProjectDataStoreHelper.retFirstObject(projects);
    }

    /**
     * <p>
     * Retrieves the external projects with the given name and version.
     * </p>
     * <p>
     * Simply calls retrieveProjects(String[], String[]) and returns the entire array. Note that since names and version
     * texts are not unique (probably due to being in different catalogs) there may be more than one project that
     * corresponds to this name and version.
     * </p>
     *
     * @param name
     *            the name of the project we are interested in.
     * @param version
     *            the version of the project we are interested in.
     * @throws IllegalArgumentException
     *             if either argument is <code>null</code> or empty after trim.
     * @return external projects which have the given name and version text.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    public ExternalProject[] retrieveProject(String name, String version) throws RetrievalException {
        // Delegates to retrieveProjects(String[], String[]).
        return retrieveProjects(new String[] {name}, new String[] {version});
    }

    /**
     * <p>
     * Retrieves the external projects with the given ids. Note that retrieveProjects(ids)[i] will not necessarily
     * correspond to ids[i].
     * </p>
     * <p>
     * If an entry in ids was not found, no entry in the return array will be present. If there are any duplicates in
     * the input array, the output will NOT contain a duplicate ExternalProject.
     * </p>
     *
     * @param ids
     *            the ids of the projects we are interested in.
     * @return an array of external projects who have the given ids. If none of the given ids were found, an empty array
     *         will be returned. The index of the entries in the array will not necessarily directly correspond to the
     *         entries in the ids array.
     * @throws IllegalArgumentException
     *             if ids is <code>null</code> or any entry is not positive.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    public ExternalProject[] retrieveProjects(long[] ids) throws RetrievalException {
        UserProjectDataStoreHelper.validateArray(ids, "ids");

        if (ids.length == 0) {
            return new ExternalProject[0];
        }

        // Constructs the queryAndClause string.
        String queryAndClause = "and cv.comp_vers_id in ";

        // Delegates to retrieveProjects(String, Object, int).
        return retrieveProjects(queryAndClause, ids, ids.length);
    }

    /**
     * <p>
     * Retrieves the external projects with the given names and versions. Both the name AND version has to match for the
     * record to be retrieved. Note that retrieveProjects(names, versions)[i] will not necessarily correspond to
     * names[i] and versions[i].
     * </p>
     * <p>
     * If an entry in names/versions was not found, no entry in the return array will be present. If there are any
     * duplicates in the input array, the output will NOT contain a duplicate ExternalProject.
     * </p>
     *
     * @param names
     *            the names of the projects we are interested in; each entry corresponds to the same indexed entry in
     *            the versions array.
     * @param versions
     *            the versions of the projects we are interested in; each entry corresponds to the same indexed entry in
     *            the names array.
     * @return an array of external projects who have the given names and versions. If none were found, an empty array
     *         will be returned. The index of the entries in the array will not necessarily directly correspond to the
     *         entries in the input array.
     * @throws IllegalArgumentException
     *             if either array is <code>null</code> or any entry in either array is null or empty after trim, or
     *             the sizes of these two array are not the same.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    public ExternalProject[] retrieveProjects(String[] names, String[] versions) throws RetrievalException {
        UserProjectDataStoreHelper.validateArray(names, "names");
        UserProjectDataStoreHelper.validateArray(versions, "versions");
        if (names.length != versions.length) {
            throw new IllegalArgumentException("The sizes of these two array are not the same.");
        }

        if (names.length == 0 && versions.length == 0) {
            return new ExternalProject[0];
        }

        // Constructs the queryAndClause string.
        String queryAndClause = "and cc.component_name || cv.version_text in ";

        String[] namesPlusVersions = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            namesPlusVersions[i] = names[i] + versions[i];
        }

        // Delegates to retrieveProjects(String, Object, int).
        return retrieveProjects(queryAndClause, namesPlusVersions, names.length);
    }

    /**
     * <p>
     * Creates an <code>{@link ExternalProjectImpl}</code> from the columns in the given result set.
     * </p>
     * <p>
     * This method is called by <code>super.retrieveObjects</code> which is why it must return an
     * <code>{@link ExternalObject}</code> instead of an <code>{@link ExternalProjectImpl}</code>.
     * </p>
     *
     * @param rs
     *            a result set row which contains the columns needed to instantiate an ExternalProjectImpl object.
     * @return an ExternalProjectImpl with the columns of the given result set.
     * @throws RetrievalException
     *             if rs didn't contain the required columns, or if any of them could not be retrieved.
     */
    protected ExternalObject createObject(ResultSet rs) throws RetrievalException {
        ExternalProjectImpl retProjectImpl;

        try {
            // Creates the ExternalProjectImpl instance.
            long id = rs.getLong(COMPONENT_VERSION_ID_STRING);
            long versionId = rs.getLong(VERSION_STRING);
            String version = rs.getString(VERSION_TEXT_STRING);

            retProjectImpl = new ExternalProjectImpl(id, versionId, version);

            // Sets other fields of the ExternalProjectImpl instance.
            retProjectImpl.setCatalogId(rs.getLong(ROOT_CATEGORY_ID_STRING));
            retProjectImpl.setComments(rs.getString(COMMENTS_STRING));
            retProjectImpl.setComponentId(rs.getLong(COMPONENT_ID_STRING));
            retProjectImpl.setDescription(rs.getString(DESCRIPTION_STRING));
            retProjectImpl.setName(rs.getString(COMPONENT_NAME_STRING));

            // short description
            String shortDescription = rs.getString(SHORT_DESCRIPTION_STRING);
            if (shortDescription == null) {
                retProjectImpl.setShortDescription("");
            } else {
                retProjectImpl.setShortDescription(shortDescription);
            }

            // functional description
            String functionalDescription = rs.getString(FUNCTIONAL_DESCRIPTION_STRING);
            if (functionalDescription == null) {
                retProjectImpl.setFunctionalDescription("");
            } else {
                retProjectImpl.setFunctionalDescription(functionalDescription);
            }

            Object forumIdObject = rs.getObject(FORUM_ID_STRING);
            if (forumIdObject != null) {
                // The project do have a forum id.
                retProjectImpl.setForumId(Long.parseLong(forumIdObject.toString()));
            }

        } catch (SQLException e) {
            throw new RetrievalException("Some of the project values cannot be retrieved.", e);
        } catch (NumberFormatException e) {
            throw new RetrievalException("The value of ForumId is not set as long value.", e);
        }

        return retProjectImpl;
    }

    /**
     * <p>
     * Retrieves the external projects with the given parameters. The parameters can be long[] or String[].
     * </p>
     * <p>
     * This method is called by <code>retrieveProjects(long[])</code> and
     * <code>retrieveProjects(String[], String[])</code>, it does the common operations for these two methods.
     * </p>
     *
     * @param queryAndClause
     *            the query of the prepareStatement, given be the caller.
     * @param queryParameter
     *            the parameter of the query, it can be long[] or String[], due to the different caller.
     * @param paramLength
     *            the length of the queryParameter array.
     * @return an array of external projects who have the given names and versions. If none were found, an empty array
     *         will be returned. The index of the entries in the array will not necessarily directly correspond to the
     *         entries in the input array.
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     */
    private ExternalProject[] retrieveProjects(String queryAndClause, Object queryParameter, int paramLength)
            throws RetrievalException {
        // Opens the connection.
        Connection conn = super.getConnection();

        // Constructs the query string.
        String query = "SELECT cv.comp_vers_id, cv.component_id, version, version_text, comments, "
                + "component_name, description, cc.root_category_id category_id, forum_id, "
                + "cc.short_desc short_desc, cc.function_desc function_desc "
                + "FROM comp_versions cv, comp_catalog cc, OUTER comp_forum_xref f "
                + "WHERE cv.component_id = cc.component_id AND cv.comp_vers_id = f.comp_vers_id ";
        query += queryAndClause + UserProjectDataStoreHelper.generateQuestionMarks(paramLength);

        // Prepares the statement.
        PreparedStatement ps = UserProjectDataStoreHelper.createStatement(conn, query, "projectQuery");

        // Sets the parameters
        try {
//            ps.setLong(1, this.forumType);

            if (queryParameter instanceof long[]) {
                for (int i = 0; i < paramLength; i++) {
                    ps.setLong(i + 1, ((long[]) queryParameter)[i]);
                }
            } else if (queryParameter instanceof String[]) {
                for (int i = 0; i < paramLength; i++) {
                    ps.setString(i + 1, ((String[]) queryParameter)[i]);
                }
            }
        } catch (SQLException e) {
            throw new RetrievalException("Database access error occurs while setting the parameters.", e);
        }

        // Retrieves the objects.
        Map objects = super.retrieveObjects(ps);

        // Closes the previously used prepared statement.
        super.close(ps, null);

        // Prepare the technologies query
        String techQuery = "SELECT cv.comp_vers_id comp_vers_id, tt.technology_name technology "
                + "FROM comp_versions cv, comp_catalog cc, technology_types tt, comp_technology ct, "
                + "OUTER comp_jive_category_xref f WHERE cv.component_id = cc.component_id "
                + "AND cv.comp_vers_id = ct.comp_vers_id AND ct.technology_type_id = tt.technology_type_id "
                + "AND cv.comp_vers_id = f.comp_vers_id ";

        techQuery += queryAndClause + UserProjectDataStoreHelper.generateQuestionMarks(paramLength);

        // Prepares the statement.
        ps = UserProjectDataStoreHelper.createStatement(conn, techQuery, "technologyQuery");

        // Sets the parameters
        try {
//            ps.setLong(1, this.forumType);

            if (queryParameter instanceof long[]) {
                for (int i = 0; i < paramLength; i++) {
                    ps.setLong(i + 1, ((long[]) queryParameter)[i]);
                }
            } else if (queryParameter instanceof String[]) {
                for (int i = 0; i < paramLength; i++) {
                    ps.setString(i + 1, ((String[]) queryParameter)[i]);
                }
            }
        } catch (SQLException e) {
            throw new RetrievalException("Database access error occurs while setting the parameters.", e);
        }
        // update the technologies
        updateTechnologies(objects, ps);
        // Closes the Prepared Statement and connection
        super.close(ps, conn);

        // Converts the map to an ExternalProject array.
        ExternalProject[] projects = (ExternalProject[]) new LinkedList(objects.values())
                .toArray(new ExternalProject[0]);

        return projects;
    }

    /**
     * <p>
     * Updates the respective technologies of the given objects using the prepared statement.
     * </p>
     *
     * @param objects
     *            a map from Long (id) to ExternalProjectImpl
     * @param ps
     *            a prepared statement with the following two fields: id (as a long), technology (as a varchar)
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     * @since 1.1
     */
    private void updateTechnologies(Map objects, PreparedStatement ps) throws RetrievalException {
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
            while (rs.next()) {
                long id = rs.getLong(COMPONENT_VERSION_ID_STRING);
                ExternalProjectImpl proj = (ExternalProjectImpl) objects.get(id);
                if (proj != null) {
                    proj.addTechnology(rs.getString(TECHNOLOGY_STRING));
                }
            }
        } catch (SQLException e) {
            throw new RetrievalException("Database access error occurs while updating technologies.", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // expected
                }
            }
        }
    }
}
