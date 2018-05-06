/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.util.config.Property;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class manages a collection of SearchBundle objects.
 * It provides APIs to allow user application to manage the collection of SearchBundle objects..
 * Including set the collection of SearchBundle,remove one SearchBundle,add one SearchBundle.
 * Also clear the SearchBundles.The SearchBundles in the manager in initialized in the constructor,
 * in which all the SearchBundles are loaded from the configration.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class SearchBundleManager {

    /**
     * It holds a map of SearchBundle objects. the key of the map is the name of the SearchBundle.
     * while the value is the searchBundle instance.
     *
     */
    private Map searchBundleMap;

    /**
     * Create a new instance,with the SearchBundle store in the manager is empty.
     *
     */
    public SearchBundleManager() {
        searchBundleMap = new HashMap();
    }

    /**
     * <p>
     * Create a new instance through reading configuration file via the namespace.
     * And all the searchBundles in the configration are loaded from the configration
     * to the manager, stored in the searchBundleMap.
     * </p>
     *
     * @param namespace the configuration namespace from which to retrieve the configuration.
     * @throws IllegalArgumentException if any parameter is Null,
     * or any parameter is empty
     * @throws SearchBuilderConfigurationException if any error occurs
     */
    public SearchBundleManager(String namespace) throws SearchBuilderConfigurationException {
        if (namespace == null) {
            throw new IllegalArgumentException("The namespace should not be null to construct SearchBundleManager.");
        }

        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException("The namespace should not be empty to construct SearchBundleManager.");
        }

        //get the ObjectFactorys used to create the Object later
        ObjectFactory strategyFactory = SearchBuilderHelper.getObjectFactory(namespace,
                "searchStrategyFactoryNamespace");

        ObjectFactory validatorFactory = SearchBuilderHelper.getObjectFactory(namespace,
                "fieldValidatorFactoryNamespace");

        searchBundleMap = new HashMap();

        //get the searchBundles properties
        Property searchBundles = SearchBuilderHelper.getConfigPropertyObject(namespace, "searchBundles", false);

        //if the searchBundles is missing, let the searchBundleMap empty
        if (searchBundles == null) {
            return;
        }

        List subs = searchBundles.list();
        //create the SearchBundle for each sub properties
        for (Iterator it = subs.iterator(); it.hasNext();) {
            Property searchBundle = (Property) it.next();
            String name = searchBundle.getName();

            Map searchFields = getFields(searchBundle, validatorFactory);
            SearchStrategy searchStrategy = getSearchStrategy(searchBundle, strategyFactory);
            Map alias = getAlias(searchBundle);

            //the context is required
            String context = searchBundle.getValue("context");
            if (context == null || context.trim().length() == 0) {
                throw new SearchBuilderConfigurationException("The context should not be missing.");
            }

            SearchBundle bundle = null;

            try {
                //if the search fields is empty, construct without it
                bundle = new SearchBundle(name, searchFields, alias, context, searchStrategy);
            } catch (IllegalArgumentException e) {
                throw new SearchBuilderConfigurationException("failed to create the SearchBundle.", e);
            }

            //put the search bundle into the manager
            searchBundleMap.put(name, bundle);
        }
    }

    /**
     * Get the SearchStrategy of the search bundle.
     *
     * @param property the property of configuration
     * @param factory the ObjectFactory used to create the SearchStrategy
     * @return a SearchStrategy according the configuration
     * @throws SearchBuilderConfigurationException if can not create the SearchStrategy successfully
     */
    private static SearchStrategy getSearchStrategy(Property property, ObjectFactory factory)
        throws SearchBuilderConfigurationException {
        String key = property.getValue("searchStrategy.class");
        
        //the class is required
        if (key == null || key.trim().length() == 0) {
            throw new SearchBuilderConfigurationException("Property 'searchStrategy.class' is missing for validator.");
        }
        String identifier = property.getValue("searchStrategy.identifier");
        try {
            if (identifier == null || identifier.trim().length() == 0) {
                return (SearchStrategy) factory.createObject(key);
            } else {
                //if the identifier exists, create with identifier
                return (SearchStrategy) factory.createObject(key, identifier);
            }
        } catch (Exception e) {
            //any exception is wrapped by SearchBuilderConfigurationException
            throw new SearchBuilderConfigurationException("can not create the SearchStrategy.", e);
        }
    }

    /**
     * Get the searchable fields of the search bundle.
     *
     * @param property a Property of configuration
     * @param factory the ObjectFactory used to create the validators
     * @return a Map contains the searchable fields
     * @throws SearchBuilderConfigurationException if can not get the searchable fields successfully
     */
    private static Map getFields(Property property, ObjectFactory factory) throws SearchBuilderConfigurationException {
        Map map = new HashMap();
        Property fields = property.getProperty("searchableFields");
        if (fields == null) {
            throw new SearchBuilderConfigurationException("The 'searchableFields' should not be missed.");
        }
        List fieldList = fields.list();

        for (Iterator it = fieldList.iterator(); it.hasNext();) {
            Property field = (Property) it.next();
            String name = field.getName();
            Property validatorProperty = field.getProperty("validator");

            //the validatorProperty is optinal, let it be null, it will be set to be always true validator
            if (validatorProperty == null) {
                map.put(name, null);
                continue;
            }

            String key = validatorProperty.getValue("class");
            //if the validator exists, then the class must exist
            if (key == null || key.trim().length() == 0) {
                throw new SearchBuilderConfigurationException("Property 'class' is missing for validator.");
            }
            String identifier = validatorProperty.getValue("identifier");
            try {
                if (identifier == null || identifier.trim().length() == 0) {
                    map.put(name, factory.createObject(key));
                } else {
                    //if the identifier exists, create with identifier
                    map.put(name, factory.createObject(key, identifier));
                }
            } catch (InvalidClassSpecificationException e) {
                throw new SearchBuilderConfigurationException("faliled to create validator.", e);
            }
        }

        return map;
    }

    /**
     * Get the alias map of the search bundle.
     *
     * @param property a Property of configuration
     * @return a Map contains the alias
     * @throws SearchBuilderConfigurationException if can not get the alias successfully
     */
    private static Map getAlias(Property property) throws SearchBuilderConfigurationException {
        Map map = new HashMap();

        Property aliases = property.getProperty("alias");
        if (aliases == null) {
            //no fields exists, return empty map
            return map;
        }

        List aliasList = aliases.list();

        //load all the alias entry
        for (Iterator it = aliasList.iterator(); it.hasNext();) {
            Property alias = (Property) it.next();
            map.put(alias.getName(), alias.getValue());
        }

        return map;
    }

    /**
     * <p>
     * Get the SearchBundle object with given name via the searchBundleMap.
     * </p>
     *
     * @param name the name of BundleSearch intended to retrieve
     * @return SearchBundle object
     * @throws IllegalArgumentException if any parameter is Null
     * @throws IllegalArgumentException if any parameter is empty
     */
    public synchronized SearchBundle getSearchBundle(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The name should not be null to getSearchBundle.");
        }

        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("The name should not be empty to getSearchBundle.");
        }

        return (SearchBundle) searchBundleMap.get(name);
    }

    /**
     * <p>
     * Get all the names of the SearchBundles in the manager.
     * return as a list format.
     * </p>
     *
     * @return a list containing names of all SearchBundle objects
     */
    public synchronized List getSearchBundleNames() {
        return new ArrayList(searchBundleMap.keySet());
    }

    /**
     * <p>
     * Add a SearchBundle object to the manager.
     * </p>
     *
     * @param searchBundle the SearchBundle object to be added
     * @throws IllegalArgumentException if any parameter is Null
     * @throws DuplicatedElementsException if the SearchBundle with the same name already exists
     */
    public synchronized void addSearchBundle(SearchBundle searchBundle) throws DuplicatedElementsException {
        if (searchBundle == null) {
            throw new IllegalArgumentException("The searchBundle should not be null to addSearchBundle.");
        }

        String searchBundleName = searchBundle.getName();

        if (searchBundleMap.containsKey(searchBundleName)) {
            throw new DuplicatedElementsException("The SearchBundle with name:" + searchBundleName
                    + " already exist while do addSearchBundle.");
        }

        searchBundleMap.put(searchBundleName, searchBundle);
    }

    /**
     * <p>
     * Remove the SearchBundle object with a given name from the manager.
     * </p>
     *
     * @param name name of the SearchBundle object to be deleted
     * @throws IllegalArgumentException if any parameter is Null
     * @throws IllegalArgumentException if any parameter is empty
     */
    public synchronized void removeSearchBundle(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The name should not be null to removeSearchBundle.");
        }

        if (name.length() == 0) {
            throw new IllegalArgumentException("The name should not be empty to removeSearchBundle.");
        }

        searchBundleMap.remove(name);
    }

    /**
     * <p>
     * Set a Map of  searchBundle objects.
     * </p>
     *
     * @param searchBundleMap a Map containing SearchBundle objects
     * @throws IllegalArgumentException if any parameter is Null
     * @throws IllegalArgumentException if searchBundleMap parameter is an empty map
     */
    public synchronized void setSearchBundle(Map searchBundleMap) {
        SearchBuilderHelper.checkSearchBundleMap(searchBundleMap, "searchBundleMap");

        this.searchBundleMap = new HashMap(searchBundleMap);
    }

    /**
     * <p>
     * Empty all records managed by the class,clear all.
     * </p>
     *
     */
    public synchronized void clear() {
        searchBundleMap.clear();
    }
}
