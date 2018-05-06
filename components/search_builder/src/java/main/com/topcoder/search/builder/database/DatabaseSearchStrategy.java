/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.search.builder.PersistenceOperationException;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.SearchStrategy;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.classassociations.ClassAssociator;
import com.topcoder.util.classassociations.IllegalHandlerException;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

/**
 * <p>
 * This is a Search Strategy that is tuned for searching a database. It is
 * responsible for building the necessary SQL search string appropriate to the
 * filters provided and executing the SQL against the database. This is done
 * with the help of the SearchFragmentBuilder implementations that are provided
 * in this package. Each SearchFragmentBuilder is responsible for building the
 * SQL for a specific filter, and a ClassAssociator is used to associate the
 * FragmentBuilders with the filters (making the filter - Fragment mapping
 * easier).
 * </p>
 *
 * <p>
 * A PreparedStatement is used, and the search parameters are bound to the
 * PreparedStatement after the entire JDBC SQL String has been generated.
 * </p>
 *
 * <p>
 * Thread Safety: This is thread safe. The state is maintained in a separate
 * SearchContext class, allowing concurrent calls to be supported.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class DatabaseSearchStrategy implements SearchStrategy {

    /**
     * The class array used to get the Constructor.
     */
    private static final Class [] CLASSES = new Class [] {String.class};
    /**
     * <p>
     * This is the connection factory that is used when connecting to the
     * database. It is used the search method, initalized during construction,
     * and never changed.
     * </p>
     *
     */
    private final DBConnectionFactory connectionFactory;

    /**
     * <p>
     * This is the classAssociator that will be mapping the Filter classes to
     * their respective SearchFragmentBuilders. The keys are non-null Filter
     * classes, and the values are non-null SearchFragmentBuilders. It is
     * initialized in the constructor, and not changed/modified afterwards. It
     * is used for providing the SearchFragmentBuilders lookup in the
     * SearchContext in buildSearchContext method.
     * </p>
     */
    private final ClassAssociator fragmentBuilders;

    /**
     * <p>
     * This is the connection name that is used when connecting to the database.
     * It is used the search method, initalized during construction, and never
     * changed. If null, it means the default connection is used.
     * </p>
     *
     */
    private final String connectionName;

    /**
     * <p>
     * Constructor that allows for the programmatic definition of a
     * DatabaseSearchStrategy. The Map is included to later support Object
     * Factory (if it ever supports Map-type constructors).
     * </p>
     *
     * <p>
     * The valid keys for fragmentBuilders are non-null Filters or Class
     * objects. The valid values for fragmentBuilders are non-null
     * SearchFragmentBuilder objects. The Map is used to populate
     * searchFragmentBuidlers ClassAssociator.
     * </p>
     *
     * @param connFactory
     *            The connectionFactory to use when connecting to the datastore.
     * @param connName
     *            The connectionName to use when connecting to the datastore (if
     *            null or an empty String, it means the default connection is
     *            used).
     * @param associations
     *            A map of filter to SearchFragmentBuilder mappings. The
     *            SearchFragmentBuilders should be SQL-database oriented to be
     *            useful in this component.
     * @throws IllegalArgumentException
     *             if connFactory or associations is null, or if associations
     *             contains invalid type (wrong class or null) keys or values.
     */
    public DatabaseSearchStrategy(DBConnectionFactory connFactory, String connName, Map associations) {
        if (connFactory == null) {
            throw new IllegalArgumentException("The connFactory should not be null.");
        }
        if (associations == null) {
            throw new IllegalArgumentException("The associations should not be null.");
        }
        this.connectionFactory = connFactory;
        this.connectionName = connName;
        this.fragmentBuilders = new ClassAssociator();

        // add all the filter class to SearchFragmentBuilder entry to the
        // fragmentBuilders
        for (Iterator it = associations.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            // check the key valid, it should be a filter class
            if (!(key instanceof Class) || !Filter.class.isAssignableFrom((Class) key)) {
                throw new IllegalArgumentException("Invalid key exists in associations.");
            }

            // check the value valid, it should be a SearchFragmentBuilder
            // instance
            if (!(value instanceof SearchFragmentBuilder)) {
                throw new IllegalArgumentException("Invalid value exists in associations.");
            }
            try {
                fragmentBuilders.addClassAssociation((Class) key, value);
            } catch (IllegalHandlerException e) {
                throw new IllegalArgumentException("invalid Handler exists in associations.");
            }
        }
    }

    /**
     * <p>
     * Configures the DatabaseSearchStrategy from a namspace. This constructor
     * is provided while the Object Factory is still unable to support Map-type
     * arguments.
     * </p>
     * <p>
     * Implementation Notes: - See the CS for the configuration parameters of
     * this.
     * </p>
     *
     * @param namespace
     *            The configuration namespace to use.
     * @throws IllegalArgumentException
     *             if namespace is null or an empty (trimmed) String.
     * @throws SearchBuilderConfigurationException
     *             if a configuration property that is required is not found, or
     *             if a configuration property does not make sense.
     */
    public DatabaseSearchStrategy(String namespace) throws SearchBuilderConfigurationException {
        if (namespace == null) {
            throw new IllegalArgumentException("The namespace should not be null.");
        }
        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException("The namespace should not be empty.");
        }
        String factoryNamespace = SearchBuilderHelper.getConfigProperty(namespace, "connectionFactory.name", true);
        String className = SearchBuilderHelper.getConfigProperty(namespace, "connectionFactory.class", false);
        // if class name is missing, using default on
        if (className == null || className.trim().length() == 0) {
            className = DBConnectionFactoryImpl.class.getName();
        }
        try {
            connectionFactory = (DBConnectionFactory) Class.forName(className).getConstructor(
                    CLASSES).newInstance(new Object[] {factoryNamespace});
        } catch (Exception e) {
            throw new SearchBuilderConfigurationException("can not construct the connectionFactory successfully.", e);
        }
        // get the connection name, which can be null and empty
        connectionName = SearchBuilderHelper.getConfigProperty(namespace, "connectionName", false);

        this.fragmentBuilders = SearchBuilderHelper.loadClassAssociator(namespace);
    }

    /**
     * <p>
     * This is a protected method that can be used to build the SearchContext
     * for the search. It may be overridden by subclasses to provide additional
     * or different methods of building up the search context.
     * </p>
     *
     * <p>
     * In this Strategy, the SearchContext is used to hold the state of
     * processing the Filters. The search String is built separately from the
     * binding parameters. The SQL SearchFragmentBuilders are expected to do the
     * following: 1. Update the searchString by appending the necessary String
     * fragment relevant to the Filter. Usually this is a String fragment that
     * contains a PreparedStatement-bindable parameter. For example: "a > ?" 2.
     * If a bindable parameter(s) were added, append them to the Bindable
     * parameters of the SearchContext.
     * </p>
     * <p>
     * This method may be overridden by subclasses to add additional behavior
     * (for example, processing the SQL String to selectively choose the table
     * joins for increased query performance). For such a method, all it would
     * do is call super.buildSearchContext, then modify the searchString buffer
     * of the built context befrore returning itself.
     * </p>
     *
     * @return A SearchContext object that is used to hold the state of building
     *         the search.
     * @param context
     *            This is the context under which the search is performed.
     * @param filter
     *            The search filter which will constrain the search results.
     * @param returnFields
     *            A list of return fields to further constrain the search an
     *            empty List means that all fields in the context should be
     *            returned by the search.
     * @param aliasMap
     *            a map of strings, holding the alternate names of fields as
     *            keys and their actual values in the datastore as the
     *            respective values.
     * @throws UnrecognizedFilterException
     *             propagated from SearchFragmentBuilder
     */
    protected SearchContext buildSearchContext(String context, Filter filter, List returnFields, Map aliasMap)
        throws UnrecognizedFilterException {
        SearchContext searchContext = new SearchContext(fragmentBuilders, aliasMap);

        // append the context firstly
        searchContext.getSearchString().append(includeReturnFieldsInSearchString(context, returnFields));
        SearchFragmentBuilder builder = searchContext.getFragmentBuilder(filter);

        // if no SearchFragmentBuilder can be looked up, throw
        // UnrecognizedFilterException
        if (builder == null) {
            throw new UnrecognizedFilterException("No SearchFragmentBuilder can be retrieved.", filter);
        }

        // builder the search
        builder.buildSearch(filter, searchContext);

        return searchContext;
    }

    /**
     * <p>
     * Searches the database using the provided context, filter and constraints
     * the returnFields.
     * </p>
     *
     * @return A CustomResultSet object containing the results of the search.
     * @param context
     *            The search context. This would be an SQL statement.
     * @param filter
     *            The filter to use.
     * @param returnFields
     *            The set of fields to return.
     * @param aliasMap
     *            a map of strings, holding the alternate names of fields as
     *            keys and their actual values in the datastore as the
     *            respective values.
     * @throws UnrecognizedFilterException
     *             propagated from SearchFragmentBuilder
     * @throws IllegalArgumentException
     *             if context or filter is null or if context is an empty String
     *             or if returnFields contains null/non-String contents or
     *             aliasMap contains null/empty String arguments.
     * @throws PersistenceOperationException
     *             to wrap any exception that occurs while searching (except
     *             UnrecognizedFilterException and IAE).
     */
    public Object search(String context, Filter filter, List returnFields, Map aliasMap)
        throws PersistenceOperationException, UnrecognizedFilterException {
        if (context == null) {
            throw new IllegalArgumentException("The context should not be null.");
        }
        if (context.trim().length() == 0) {
            throw new IllegalArgumentException("The context should not be empty.");
        }
        if (filter == null) {
            throw new IllegalArgumentException("The context should not be null.");
        }

        SearchBuilderHelper.checkList(returnFields, "returnFields", String.class);
        SearchBuilderHelper.checkaliasMap(aliasMap, "aliasMap");

        SearchContext searchContext = buildSearchContext(context, filter, returnFields, aliasMap);
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            if (connectionName != null && connectionName.trim().length() > 0) {
                connection = connectionFactory.createConnection(connectionName);
            } else {
                // if the connection name is missing, using default one
                connection = connectionFactory.createConnection();
            }
            ps = connection.prepareStatement(searchContext.getSearchString().toString());
            List list = searchContext.getBindableParameters();

            int index = 1;
            // set all the parameter
            for (Iterator it = list.iterator(); it.hasNext();) {
                Object obj = it.next();
                ps.setObject(index++, obj);

            }

            resultSet = ps.executeQuery();

            return new CustomResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenceOperationException("SQLException occurs.", e);
        } catch (DBConnectionException e) {
            throw new PersistenceOperationException("Can not get the connection.", e);
        } finally {
            doClose(connection, ps, resultSet);
        }
    }

    /**
     * <p>
     * Close the resources of the database.
     * </p>
     *
     * @param connection
     *            the Connection to be closed
     * @param statement
     *            the PreparedStatement to be closed.
     * @param resultSet
     *            the ResultSet to be closed
     */
    private void doClose(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        // close the resultSet
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            // ignore to continue close the Connection and Statement
        }

        // close the PreparedStatement
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            // ignore to continue close the Connection
        }

        // close the connection
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("closed failed.");
            // ignore
        }
    }

    /**
     * <p>
     * Build a complete search string via including return fields into the
     * search string at last,the return fields will include both of those in the
     * list and the searchString. The mothed do not need synchronized.
     * </p>
     *
     *
     * @param context
     *            a search string used to conduct the search
     * @param fields
     *            names of fileds should be returned
     * @return constructed search string
     * @throws IllegalArgumentException
     *             is the context is invalid
     */
    private String includeReturnFieldsInSearchString(String context, List fields) {
        if(fields.size() == 0) {
            return context + " ";
        }
        StringBuffer buffer = new StringBuffer();
        if (fields.size() > 0) {
            Iterator it = fields.iterator();
            buffer.append((String) it.next());
            while (it.hasNext()) {
                buffer.append(",").append((String) it.next());
            }
        }
        String returnFields = buffer.toString();

        // find the 'select' token
        int indexSelect = context.toLowerCase().indexOf("select");

        // find the 'from' token
        int indexFrom = context.toLowerCase().indexOf("from");

        // if not find the 'select token'
        if ((indexSelect < 0) || (indexFrom < 0)) {
            throw new IllegalArgumentException(
                    "The searchString should contain the 'select' token to includeReturnFieldsInSearchString.");
        }

        String filedNames = context.substring(indexSelect + "select".length(), indexFrom).trim();

        // if select all then return the searchString
        if (filedNames.equals("*")) {
            // select all the fields
            return context + " ";
        }

        // makeup the string and return
        buffer = new StringBuffer();
        buffer.append("select ").append(filedNames);
        // fields already exist in context
        if (filedNames.length() != 0) {
            buffer.append(", ");
        }
        buffer.append(returnFields).append(" ").append(context.substring(indexFrom)).append(" ");
        return buffer.toString();
    }
}
