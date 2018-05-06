/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.topcoder.search.builder.filter.AbstractAssociativeFilter;
import com.topcoder.search.builder.filter.AbstractSimpleFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.classassociations.ClassAssociator;
import com.topcoder.util.classassociations.IllegalHandlerException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.SpecificationFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

/**
 * <p>
 * This helperClass of the package com.topcoder.search.builder.
 * The class offers some check mothed use to check some param valid in some motheds.
 * </p>
 *
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public final class SearchBuilderHelper {
    /**
     * The private construct.
     *
     */
    private SearchBuilderHelper() {
        //empty
    }

    /**
     * Get the Object Factory instance with the given namespace and property name.
     *
     * @param namespace the namespace of the configuration
     * @param propertyName the name of the property
     * @return a ObjectFactory instance according the configuration
     * @throws SearchBuilderConfigurationException if can not get the ObjectFactory successfully
     */
    public static ObjectFactory getObjectFactory(String namespace, String propertyName)
        throws SearchBuilderConfigurationException {
        String factoryNS = getConfigProperty(namespace, propertyName, true);

        try {
            SpecificationFactory specificationFactory = new ConfigManagerSpecificationFactory(factoryNS);

            return new ObjectFactory(specificationFactory);
        } catch (SpecificationConfigurationException e) {
            throw new SearchBuilderConfigurationException("can not get the Object"
                    + " Factory for SpecificationConfigurationException.", e);
        } catch (IllegalReferenceException e) {
            throw new SearchBuilderConfigurationException("can not get the Object"
                    + " Factory for IllegalReferenceException.", e);
        }
    }

    /**
     * Load the ClassAssociator from the namespace configuration.
     *
     * @param namespace the namespace to be loaded
     * @return a ClassAssociator according the configuration
     * @throws SearchBuilderConfigurationException if can not load successfully
     */
    public static ClassAssociator loadClassAssociator(String namespace) throws SearchBuilderConfigurationException {
        ClassAssociator fragmentBuilders = new ClassAssociator();
        try {
            ObjectFactory objectFactory = getObjectFactory(namespace, "searchFragmentFactoryNamespace");

            Property searchFragmentBuilders = getConfigPropertyObject(namespace, "searchFragmentBuilders", false);

            //if the search fragment is not null
            if (searchFragmentBuilders != null) {
                List subProperties = searchFragmentBuilders.list();

                for (Iterator it = subProperties.iterator(); it.hasNext();) {
                    Property searchFragmentBuilder = (Property) it.next();

                    Class filterClass = Class.forName(searchFragmentBuilder.getValue("targetFilter"));

                    //check the filter valid
                    if (!(Filter.class.isAssignableFrom(filterClass))) {
                        throw new SearchBuilderConfigurationException("invalid filter class.");
                    }
                    String key = searchFragmentBuilder.getValue("className");

                    if (key == null || key.trim().length() == 0) {
                        throw new SearchBuilderConfigurationException("missing key for searchFragmentBuilder.");
                    }

                    String identifier = searchFragmentBuilder.getValue("identifier");
                    Object builder = null;
                    //if identifier exists, then create with identifier
                    if (identifier != null && identifier.trim().length() > 0) {
                        builder = objectFactory.createObject(key, identifier);
                    } else {
                        builder = objectFactory.createObject(key);
                    }

                    //check the builder
                    if (!(builder instanceof SearchFragmentBuilder)) {
                        throw new SearchBuilderConfigurationException("invalid searchFragmentBuilder class.");
                    }

                    fragmentBuilders.addClassAssociation(filterClass, builder);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SearchBuilderConfigurationException("ClassNotFoundException occurs.", e);
        } catch (InvalidClassSpecificationException e) {
            throw new SearchBuilderConfigurationException("InvalidClassSpecificationException occurs.", e);
        } catch (IllegalHandlerException e) {
            throw new SearchBuilderConfigurationException("IllegalHandlerException occurs.", e);
        } catch (Exception e) {
            if (e instanceof SearchBuilderConfigurationException) {
                throw (SearchBuilderConfigurationException) e;
            }
            //catch any other exception
            throw new SearchBuilderConfigurationException("Exception occurs.", e);
        }
        return fragmentBuilders;
    }

    /**
     * Gets the string property value of the property name. When the property is mandatory, it must exist in the
     * configuration manager.
     *
     * @param namespace the namespace where the configuration is read.
     * @param propertyName the property name of the property to be read.
     * @param mandatory <code>true</code> if the property is mandatory; <code>false</code> otherwise.
     * @return the string value of the property in the configuration.
     * @throws SearchBuilderConfigurationException if <code>namespace</code> is missing in configuration;
     * or the property is
     *             missing in the configuration and the property is mandatory; or accessing the configuration manager
     *             fails.
     */
    public static String getConfigProperty(String namespace, String propertyName, boolean mandatory)
        throws SearchBuilderConfigurationException {
        String value;

        try {
            value = ConfigManager.getInstance().getString(namespace, propertyName);
        } catch (UnknownNamespaceException e) {
            throw new SearchBuilderConfigurationException("The namespace '" + namespace
                    + "' is missing in configuration.", e);
        }

        // Check the missing and empty
        if ((value == null || value.trim().length() == 0) && mandatory) {
            throw new SearchBuilderConfigurationException(propertyName + " is missing in configuration.");
        }

        return value;
    }

    /**
     * Gets the string property value of the property name. When the property is mandatory, it must exist in the
     * configuration manager.
     *
     * @param namespace the namespace where the configuration is read.
     * @param propertyName the property name of the property to be read.
     * @param mandatory <code>true</code> if the property is mandatory; <code>false</code> otherwise.
     * @return the Property value of the property in the configuration.
     * @throws SearchBuilderConfigurationException if <code>namespace</code> is missing in configuration;
     * or the property is
     *             missing in the configuration and the property is mandatory; or accessing the configuration manager
     *             fails.
     */
    public static Property getConfigPropertyObject(String namespace, String propertyName, boolean mandatory)
        throws SearchBuilderConfigurationException {
        Property value;

        try {
            value = ConfigManager.getInstance().getPropertyObject(namespace, propertyName);
        } catch (UnknownNamespaceException e) {
            throw new SearchBuilderConfigurationException("The namespace '" + namespace
                    + "' is missing in configuration.", e);
        }

        // Check the missing
        if (value == null && mandatory) {
            throw new SearchBuilderConfigurationException(propertyName + " is missing in configuration.");
        }

        return value;
    }

    /**
     * Get the real Name with the alias the context.
     *
     * @param alias the alias, maybe it is already the real name
     * @param searchContext the context used when build search
     * @return the real name
     */
    public static final String getRealName(String alias, SearchContext searchContext) {
        String realName = searchContext.getFieldName(alias);
        if (realName == null) {
            realName = alias;
        }
        return realName;
    }

    /**
     * Build the search String for Simple Filter, include NullFilter, EqualsToFilter, and RangeFragmentBuilder.
     * The filter is for database.
     *
     * This method is added in version 1.3.
     *
     * @param filter the Simple Filter to be built on.
     * @param operateKey the operate key
     * @param searchContext the context of the building
     * @param value the value of the filter, can be null
     * @throws UnrecognizedFilterException if can not get all the inner builder successfully
     */
    public static void buildDBSimpleFilter(AbstractSimpleFilter filter, String operateKey, SearchContext searchContext,
            Object value) throws UnrecognizedFilterException {
        //retrieve the search String
        StringBuffer buffer = searchContext.getSearchString();

        //get the real name and append to buffer
        buffer.append(getRealName(filter.getName(), searchContext));

        buffer.append(" ").append(operateKey).append(" ?");

        searchContext.getBindableParameters().add(value);
    }

    /**
     * Build the search String for AbstractAssociativeFilter, the filter is for database.
     *
     * This method is added in version 1.3.
     *
     * @param filter the AbstractAssociativeFilter to be built on.
     * @param connectKey the connect key, 'AND' for AndFilter, 'OR' for OrFilter
     * @param searchContext the context of the building
     * @throws UnrecognizedFilterException if can not get all the inner builder successfully
     */
    public static void buildDBAbstractAssociativeFilter(AbstractAssociativeFilter filter, String connectKey,
            SearchContext searchContext) throws UnrecognizedFilterException {
        //retrieve the search String
        StringBuffer buffer = searchContext.getSearchString();

        buffer.append("(");

        boolean isFirst = true;
        //get all the filters in the andFilter, and build their search
        for (Iterator it = filter.getFilters().iterator(); it.hasNext();) {
            Filter innerFilter = (Filter) it.next();

            if (isFirst) {
                isFirst = false;
            } else {
                buffer.append(" " + connectKey + " ");
            }
            //look up the SearchFragmentBuilder in the context with the innerFilter
            SearchFragmentBuilder builder = searchContext.getFragmentBuilder(innerFilter);
            if (builder == null) {
                throw new UnrecognizedFilterException("No SearchFragmentBuilder is looked up.", innerFilter);
            }

            //build the inner Filter firstly
            builder.buildSearch(innerFilter, searchContext);
        }
        buffer.append(")");
    }

    /**
     * Build the search String for Simple Filter, include NullFilter, EqualsToFilter, and RangeFragmentBuilder.
     * The filter is for LDAP.
     *
     * This method is added in version 1.3.
     *
     * @param filter the Simple Filter to be built on.
     * @param operateKey the operate key
     * @param searchContext the context of the building
     * @param value the value of the filter, can be null
     * @throws UnrecognizedFilterException if can not get all the inner builder successfully
     */
    public static void buildLDAPSimpleFilter(AbstractSimpleFilter filter, String operateKey,
            SearchContext searchContext, Object value) throws UnrecognizedFilterException {
        //retrieve the search String
        StringBuffer buffer = searchContext.getSearchString();

        buffer.append("(");
        //get the real name and append to buffer
        buffer.append(getRealName(filter.getName(), searchContext));

        buffer.append(operateKey).append(String.valueOf(value));

        buffer.append(")");
    }

    /**
     * Build the search String for AbstractAssociativeFilter, the filter is for LDAP.
     *
     * This method is added in version 1.3.
     *
     * @param filter the AbstractAssociativeFilter to be built on.
     * @param connectKey the connect key, '&' for AndFilter, '|' for OrFilter
     * @param searchContext the context of the building
     * @throws UnrecognizedFilterException if can not get all the inner builder successfully
     */
    public static void buildLDAPAbstractAssociativeFilter(AbstractAssociativeFilter filter, String connectKey,
            SearchContext searchContext) throws UnrecognizedFilterException {
        //retrieve the search String
        StringBuffer buffer = searchContext.getSearchString();

        //save the init length which is used to insert
        int initLength = buffer.toString().length();

        Iterator it = filter.getFilters().iterator();
        //if no filter exists, simple return
        if (!it.hasNext()) {
            return;
        }

        //builder the first filter, which do not need to append '(&' or '(|'
        Filter innerFilter = (Filter) it.next();

        //look up the SearchFragmentBuilder in the context with the innerFilter
        SearchFragmentBuilder builder = searchContext.getFragmentBuilder(innerFilter);
        if (builder == null) {
            throw new UnrecognizedFilterException("No SearchFragmentBuilder is looked up.", innerFilter);
        }
        builder.buildSearch(innerFilter, searchContext);

        //for the left filter in the AbstractAssociativeFilter, and build their search
        for (; it.hasNext();) {
            innerFilter = (Filter) it.next();

            //insert the '(' + connectKey
            buffer.insert(initLength, "(" + connectKey);
            //look up the SearchFragmentBuilder in the context with the innerFilter
            builder = searchContext.getFragmentBuilder(innerFilter);
            if (builder == null) {
                throw new UnrecognizedFilterException("No SearchFragmentBuilder is looked up.", innerFilter);
            }

            //build the inner Filter firstly
            builder.buildSearch(innerFilter, searchContext);

            //for each other filter, append the ')'
            buffer.append(")");
        }
    }

    /**
     * Check the list valid.The list should not be null also should not be empty.
     * The mothed also check the items in the list which also should be with the expected class type.
     *
     * This method is added in version 1.3.
     * @param list The list to be checked
     * @param listName the Name of the list, which denote the usage of the list
     * @param expectedClass the expected class the items should be
     * @throws IllegalArgumentException if the list is null,
     * the items in it is null, or empty String
     */
    public static void checkList(List list, String listName, Class expectedClass) {
        if (list == null) {
            throw new IllegalArgumentException("The list " + listName + " should not be null.");
        }

        //all the items in the list should not be null
        for (Iterator it = list.iterator(); it.hasNext();) {
            Object obj = it.next();
            if (obj == null) {
                throw new IllegalArgumentException("The items in the list " + listName + " should not be null.");
            }
            if (!(expectedClass.isAssignableFrom(obj.getClass()))) {
                throw new IllegalArgumentException("The items in the list " + listName + " is invalid.");
            }

            if ((obj instanceof String) && String.valueOf(obj).trim().length() == 0) {
                throw new IllegalArgumentException("The String in the list " + listName + " should not be empty.");
            }
        }
    }

    /**
     * <p>
     * Check the aliasMap valid.The map should not be null also should not be empty.
     * The mothed also check the key and value in the map which also should not be null.
     * More, both the key and value should String type.
     * </p>
     *
     * <p>This method is updated in version 1.3.</p>
     *
     * @param map The map to be checked
     * @param mapName the Name of the map, which denote the usage of the map
     * @throws IllegalArgumentException if the map is null,
     * if the keys and values illegal
     */
    public static void checkaliasMap(Map map, String mapName) {
        if (map == null) {
            throw new IllegalArgumentException("The " + mapName + " should not be null.");
        }
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            //the keys in the map should not be null
            if (!(key instanceof String)) {
                throw new IllegalArgumentException("The key in the list " + mapName + " illegal.");
            }
            Object value = entry.getValue();
            //the values in the map should not be null
            if (!(value instanceof String)) {
                throw new IllegalArgumentException("The value in the list " + mapName + " illegal.");
            }

            if (key.toString().trim().length() == 0 || value.toString().trim().length() == 0) {
                throw new IllegalArgumentException("Both the key and value in the list " + mapName + " should not be"
                        + " empty.");
            }
        }
    }

    /**
     * Check the fieldsMap valid.The map should not be null also should not be empty.
     * The mothed also check the key and value in the map which also should not be null.
     * More, key should be String type, and value should be ObjectValidator type.
     *
     * <p>This method is updated in version 1.3.</p>
     *
     * @param map The map to be checked
     * @param mapName the Name of the map, which denote the usage of the map
     * @throws IllegalArgumentException if the map is null,
     * or the map is empty or the keys and values illegal
     */
    public static void checkfieldsMap(Map map, String mapName) {
        if (map == null) {
            throw new IllegalArgumentException("The " + mapName + " should not be null.");
        }
        if (map.size() == 0) {
            throw new IllegalArgumentException("The " + mapName + " should not be empty.");
        }
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            //the keys in the map should not be null
            if (key == null || !(key instanceof String)) {
                throw new IllegalArgumentException("The key in the list " + mapName + " illegal.");
            }
            Object value = entry.getValue();

            //the values in the map can be null, but must be a ObjectValidator if non null
            if (value != null && !(value instanceof ObjectValidator)) {
                throw new IllegalArgumentException("The value in the list " + mapName + " illegal.");
            }
        }
    }

    /**
     * Check the SearchBundleMap valid.The map should not be null also should not be empty.
     * The mothed also check the key and value in the map which also should not be null.
     * More, key should be String type, and value should be SearchBundle type.
     *
     * <p>This method is updated in version 1.3.</p>
     *
     * @param map The map to be checked
     * @param mapName the Name of the map, which denote the usage of the map
     * @throws IllegalArgumentException if the map is null,
     * or the map is empty or the keys and values illegal
     */
    public static void checkSearchBundleMap(Map map, String mapName) {
        if (map == null) {
            throw new IllegalArgumentException("The " + mapName + " should not be null.");
        }
        if (map.size() == 0) {
            throw new IllegalArgumentException("The " + mapName + " should not be empty.");
        }
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            //the keys in the map should not be null
            if (key == null || !(key instanceof String)) {
                throw new IllegalArgumentException("The key in the list " + mapName + " illegal.");
            }
            Object value = entry.getValue();
            //the values in the map should not be null
            if (value == null || !(value instanceof SearchBundle)) {
                throw new IllegalArgumentException("The value in the list " + mapName + " illegal.");
            }
        }
    }
}
