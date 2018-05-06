/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

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
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDK;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKConnection;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKException;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.SpecificationFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * This is a Search Strategy that is tuned for searching an LDAP data store. It
 * is responsible for building the necessary LDAP search string appropriate to
 * the filters provided and executing the search string on the LDAP server. This
 * is done with the help of the SearchFragmentBuilder implementations that are
 * provided in this package.
 * </p>
 *
 * <p>
 * Each SearchFragmentBuilder is responsible for building the LDAP search string
 * fragment for a specific filter, and a ClassAssociator is used to associate
 * the FragmentBuilders with the filters.
 * </p>
 *
 * <p>
 * The LDAPConnectionInformation from the previous version is used to connect to
 * the LDAP server. It uses the Topcoder LDAP SDK to establish and manage the
 * connections. Since 1.3, the connection to the LDAP server is not cached.
 * </p>
 *
 * <p>
 * Thread Safety: - This is thread safe. The state is maintained in a separate
 * SearchContext class, allowing concurrent calls to be supported.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class LDAPSearchStrategy implements SearchStrategy {
    /**
     * It will hold a LDAPConnectionInformation object, used to create a LDAP
     * connection. After the initialization, it should not be null. It will be
     * initialized from being null by the constructor. It is mutable. After
     * initializaion, it will be changed if connect() is first called.
     *
     */
    private final LDAPConnectionInformation connectionInfo;

    /**
     * <p>
     * This is the classAssociator that will be mapping the Filter classes to
     * their respective SearchFragmentBuilders. The keys are non-null Filter
     * classes, and the values are non-null SearchFragmentBuilders. It is
     * initialized in the constructor, and not changed/modified afterwards. It
     * is used for providing the SearchFragmentBuilders lookup in the
     * SearchContext in buildSearchContext method.
     * </p>
     *
     */
    private final ClassAssociator searchFragmentBuilders;

    /**
     * <p>
     * Create a new instance
     * </p>
     *
     * <p>
     * The valid keys for fragmentBuilders are non-null Filters or Class
     * objects. The valid values for fragmentBuilders are non-null
     * SearchFragmentBuilder objects. The Map is used to populate
     * searchFragmentBuidlers ClassAssociator.
     * </p>
     *
     * @param connectionInfo
     *            a ConnectionInformation object
     * @param associations
     *            A map of filter to SearchFragmentBuilder mappings. The
     *            SearchFragmentBuilders should be SQL-database oriented to be
     *            useful in this component.
     * @throws IllegalArgumentException
     *             if any parameter is Null, or if associations contains invalid
     *             values.
     */
    public LDAPSearchStrategy(LDAPConnectionInformation connectionInfo,
        Map associations) {
        if (connectionInfo == null) {
            throw new IllegalArgumentException(
                "The LDAPConnectionInformation should not be null.");
        }

        if (associations == null) {
            throw new IllegalArgumentException(
                "The associations should not be null.");
        }

        this.connectionInfo = connectionInfo;
        this.searchFragmentBuilders = new ClassAssociator();

        // add all the filter class to SearchFragmentBuilder entry to the
        // fragmentBuilders
        for (Iterator it = associations.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();

            // check the key valid, it should be a filter class
            if (!(key instanceof Class)
                    || !Filter.class.isAssignableFrom((Class) key)) {
                throw new IllegalArgumentException(
                    "Invalid key exists in associations.");
            }

            // check the value valid, it should be a SearchFragmentBuilder
            // instance
            if (!(value instanceof SearchFragmentBuilder)) {
                throw new IllegalArgumentException(
                    "Invalid value exists in associations.");
            }

            try {
                searchFragmentBuilders.addClassAssociation((Class) key, value);
            } catch (IllegalHandlerException e) {
                throw new IllegalArgumentException(
                    "invalid Handler exists in associations.");
            }
        }
    }

    /**
     * <p>
     * Configures the LDAPSearchStrategy from a namspace. This constructor is
     * provided while the Object Factory is still unable to support Map-type
     * arguments.
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
    public LDAPSearchStrategy(String namespace)
        throws SearchBuilderConfigurationException {
        if (namespace == null) {
            throw new IllegalArgumentException(
                "The namespace should not be null.");
        }

        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException(
                "The namespace should not be empty.");
        }

        this.searchFragmentBuilders = SearchBuilderHelper.loadClassAssociator(namespace);

        try {
            String objectFactoryNS = SearchBuilderHelper.getConfigProperty(namespace,
                    "connectionInfoFactoryNamespace", true);

            // create the object factory first
            SpecificationFactory specificationFactory = new ConfigManagerSpecificationFactory(objectFactoryNS);

            ObjectFactory objectFactory = new ObjectFactory(specificationFactory);

            String key = SearchBuilderHelper.getConfigProperty(namespace,
                    "connectionInfo.classname", false);

            if ((key == null) || (key.trim().length() == 0)) {
                key = LDAPConnectionInformation.class.getName();
            }

            String identifier = SearchBuilderHelper.getConfigProperty(namespace,
                    "connectionInfo.identifier", false);

            // create the LDAPConnectionInformation instance
            if ((identifier == null) || (identifier.trim().length() == 0)) {
                connectionInfo = (LDAPConnectionInformation) objectFactory.createObject(key);
            } else {
                connectionInfo = (LDAPConnectionInformation) objectFactory.createObject(key,
                        identifier);
            }
        } catch (SpecificationConfigurationException e) {
            throw new SearchBuilderConfigurationException("SpecificationConfigurationException occurs.",
                e);
        } catch (IllegalReferenceException e) {
            throw new SearchBuilderConfigurationException("IllegalReferenceException occurs.",
                e);
        } catch (InvalidClassSpecificationException e) {
            throw new SearchBuilderConfigurationException("InvalidClassSpecificationException occurs.",
                e);
        }
    }

    /**
     * <p>
     * Search LDAP directory and return specified fields
     * </p>
     *
     * <p>
     * Parameter Range: None-Null , None_empty. Each element in fields parameter
     * should not be null and should not be empty. The fileds parameter contains
     * a List of Strings specifying the fields should be returned.
     * </p>
     *
     * @param context
     *            a search string used to conduct the search
     * @param filter
     *            The filter that is used to constrain the search.
     * @param returnFields
     *            names of fileds should be returned
     * @return an Iterator of Entry objects holding search result
     * @param aliasMap
     *            a map of strings, holding the alternate names of fields as
     *            keys and their actual values in the datastore as the
     *            respective values.
     * @throws PersistenceOperationException
     * @throws IllegalArgumentException
     *             if any parameter is Null, or if aliasMap contains non-String
     *             or null or empty String elements. or if any paramter is empty
     *             or any element in the fields parameter is empty
     * @throws PersistenceOperationException
     *             if any error when operating over data store
     * @throws UnrecognizedFilterException
     *             if an appropriate SearchFragmentHandler cannot be found for a
     *             filter.
     */
    public Object search(String context, Filter filter, List returnFields,
        Map aliasMap)
        throws PersistenceOperationException, UnrecognizedFilterException {
        if (context == null) {
            throw new IllegalArgumentException(
                "The context should not be null.");
        }

        if (context.trim().length() == 0) {
            throw new IllegalArgumentException(
                "The context should not be empty.");
        }

        if (filter == null) {
            throw new IllegalArgumentException(
                "The context should not be null.");
        }

        // the fields can be empty
        SearchBuilderHelper.checkList(returnFields, "returnFields", String.class);
        SearchBuilderHelper.checkaliasMap(aliasMap, "aliasMap");

        LDAPSDKConnection connection = null;

        try {
            connection = connect();

            // get the array of the return fields
            String[] array = (String[]) returnFields.toArray(new String[0]);

            // get the context firstly
            SearchContext searchContext = buildSearchContext(context, filter,
                    returnFields, aliasMap);

            Iterator it = connection.search(context, connectionInfo.getScope(),
                    searchContext.getSearchString().toString(), array);

            // here we should shadow copy the iterator, otherwise the pre
            // iterator is related to the connection
            // when the connection is closed, it can not work any more
            List list = new ArrayList();

            while (it.hasNext()) {
                list.add(it.next());
            }

            return list.iterator();
        } catch (LDAPSDKException e) {
            throw new PersistenceOperationException("Search the  ldap failed.",
                e);
        } finally {
            if (connection != null) {
                // close the connection for each search in the finally block
                try {
                    connection.disconnect();
                } catch (LDAPSDKException e) {
                    throw new PersistenceOperationException("can not disconnect the ldap connection.",
                        e);
                }
            }
        }
    }

    /**
     * <p>
     * This is used to build the search context to be used when executing the
     * search. In this Strategy, the SearchContext that is build will simply
     * contain the LDAP search String to use - parameter binding is not
     * supported because it is not explicitly needed.
     * </p>
     * <p>
     * This relies on the implementations of SearchFragmentBuilder to support
     * the building of the search string. Each implemntation is expected to read
     * in the contents of a Filter and build the appropriate LDAP search string
     * fragment according to that filter. This fragment is then appended to the
     * SearchContext that is being used.
     * </p>
     *
     * <p>
     * The end-product would be a SearchContext that contains the LDAP search
     * String reflecting the Filter constraints.
     * </p>
     *
     * @param context
     *            a search string used to conduct the search
     * @return A SearchContext object that is used to hold the state of building
     *         the search.
     * @param filter
     *            The filter that is used to constrain the search.
     * @param returnFields
     *            A list of fields that should be returned by the strategy.
     * @param aliasMap
     *            a map of strings, holding the alternate names of fields as
     *            keys and their actual values in the datastore as the
     *            respective values.
     * @throws UnrecognizedFilterException
     *             if an appropriate SearchFragmentHandler cannot be found for a
     *             filter.
     */
    protected SearchContext buildSearchContext(String context, Filter filter,
        List returnFields, Map aliasMap) throws UnrecognizedFilterException {
        SearchContext searchContext = new SearchContext(searchFragmentBuilders,
                aliasMap);

        SearchFragmentBuilder builder = searchContext.getFragmentBuilder(filter);

        // if no SearchFragmentBuilder can be looked up, throw
        // UnrecognizedFilterException
        if (builder == null) {
            throw new UnrecognizedFilterException("No SearchFragmentBuilder can be retrieved.",
                filter);
        }

        // builder the search
        builder.buildSearch(filter, searchContext);

        return searchContext;
    }

    /**
     * <p>
     * Creates a connection to the LDAP server.
     * </p>
     *
     * @return An open connection to the LDAP server.
     * @throws PersistenceOperationException
     *             if any error when get the connection
     */
    protected LDAPSDKConnection connect() throws PersistenceOperationException {
        LDAPSDKConnection connection = null;

        try {
            LDAPSDK factory = connectionInfo.getFactory();

            if (connectionInfo.isSecure()) {
                connection = factory.createSSLConnection();
            } else {
                connection = factory.createConnection();
            }

            connection.connect(connectionInfo.getHost(),
                connectionInfo.getPort());

            // bind the connection to the server
            String dnroot = connectionInfo.getDnroot();
            String password = connectionInfo.getPassword();

            // if it is not anonymous, bind the connection
            if ((dnroot != null) && (dnroot.length() > 0)
                    && (password != null) && (password.length() > 0)) {
                connection.authenticate(3, connectionInfo.getDnroot(),
                    connectionInfo.getPassword());
            } else {
                connection.authenticateAnonymous(3);
            }

            return connection;
        } catch (LDAPSDKException e) {
            // wrap the LDAPSDKException with PersistenceOperationException
            throw new PersistenceOperationException("LDAPSDKException occurs whilt connect.",
                e);
        }
    }
}
