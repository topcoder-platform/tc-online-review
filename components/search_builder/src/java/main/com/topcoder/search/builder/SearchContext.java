/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.classassociations.ClassAssociator;

/**
 * <p>
 * This is a State object that is used to hold the current status of building a
 * Search. It contains any bindable parameters, as well as the existing search
 * string. It may be used by any SearchStrategies as a convenience class to help
 * with building the search string and relevant parameters.
 * </p>
 *
 * <p>
 * Thread Safety: This class is not required to be thread safe. Only one
 * SearchContext will be used per thread.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class SearchContext {

    /**
     * <p>
     * This is the Search String that is currently being built. It is initially
     * empty, but may be built by direct access to the StringBuilder class via
     * <code>getSearchString</code>.
     * </p>
     *
     */
    private final StringBuffer searchString;

    /**
     * <p>
     * This is a list of bindableParameters that have been accumulated during
     * the Search Building process. It is initally empty, but may be modified by
     * direct access to the bindableParameters List via getBindableParameters.
     * </p>
     *
     */
    private final List bindableParameters;

    /**
     * <p>
     * This is the classAssociator that will be mapping the Filter classes to
     * their respective SearchFragmentBuilders. The keys are non-null Filter
     * classes, and the values are non-null FragmentBuilders. It is initialized
     * in the constructor, and not changed/modified afterwards. It is used for
     * providing the FragmentBuilder lookup for certain SearchFragmentBuilders
     * that need to delegate to other SearchFragmentBuilders.
     * </p>
     *
     */
    private final ClassAssociator fragmentBuilders;

    /**
     * <p>
     * It will hold a Map containing keys as the alias names of the fields and
     * the names of fileds as stored objects. After the initialization, it can
     * not be null, it will be initialized in the constructor. It is immutable.
     * Value will not be changed during the life time of the instance
     * </p>
     *
     */
    private Map aliasMap;

    /**
     * <p>
     * Default Constructor.
     * </p>
     *
     * @param aliasMap
     *            a map of strings, holding the alternate names of fields as
     *            keys and their actual values in the datastore as the
     *            respective values.
     * @throws IllegalArgumentException
     *             if aliasMap contains null or empty String elements or
     *             non-string elements.
     */
    public SearchContext(Map aliasMap) {
        this(null, aliasMap);
    }

    /**
     * <p>
     * Constructor that builds a SearchContext with the provided
     * SearchFragmentBuilders for lookup.
     * </p>
     *
     * <p>
     * fragmentBuilderAssociations may be null, to indicate that the
     * SearchContext does not need to provide lookups.
     * </p>
     *
     * @param fragmentBuilderAssociations
     *            A map (Class Associator) of filter to SearchFragmentBuilder
     *            mappings. The SearchFragmentBuilders should be SQL-database
     *            oriented to be useful in this component.
     * @param aliasMap
     *            a map of strings, holding the alternate names of fields as
     *            keys and their actual values in the datastore as the
     *            respective values.
     * @throws IllegalArgumentException
     *             if the aliasMap is null, or aliasMap contains null or empty
     *             String elements or non-string elements for both key and
     *             value.
     */
    public SearchContext(ClassAssociator fragmentBuilderAssociations, Map aliasMap) {
        SearchBuilderHelper.checkaliasMap(aliasMap, "aliasMap");

        // init the members
        this.aliasMap = new HashMap(aliasMap);
        this.fragmentBuilders = fragmentBuilderAssociations;
        this.searchString = new StringBuffer();
        this.bindableParameters = new ArrayList();
    }

    /**
     * <p>
     * Retrieves the Search String that is currently being built.
     * </p>
     *
     * @return the Search String that is currently being built.
     */
    public StringBuffer getSearchString() {
        return searchString;
    }

    /**
     * <p>
     * Retrieves a list of bindableParameters that have been accumulated during
     * the Search Building process.
     * </p>
     * <p>
     * Here can not used shadow copy since it will be used to add the bindable
     * paramters.
     * </p>
     *
     * @return a list of bindableParameters that have been accumulated during
     *         the Search Building process.
     */
    public List getBindableParameters() {
        return bindableParameters;
    }

    /**
     * <p>
     * Retrieves the SearchFragmentBuilder that is associated for the provided
     * filter. If the fragmentBuilders is null, or if no association can be
     * found, then a null is returned.
     * </p>
     *
     * @return the SearchFragmentBuilder that is associated for the provided
     *         filter.
     * @param filter
     *            The filter to retrieve a SearchFragmentBuidler form.
     * @throws IllegalArgumentException
     *             if filter is null.
     */
    public SearchFragmentBuilder getFragmentBuilder(Filter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }
        if (fragmentBuilders != null) {
            return (SearchFragmentBuilder) fragmentBuilders.getAssociations().get(filter.getClass());
        }
        return null;
    }

    /**
     * <p>
     * Retrieves the field name for a provided alias. The field name will be the
     * actual name used to distinguish the field within the datastore.
     * </p>
     *
     * @return The actual field name that was represented by the alias. A null
     *         is returned if the alias could not be recognized.
     * @param alias
     *            An alias that is used as a convenient form of representing the
     *            actual field name.
     * @throws IllegalArgumentException
     *             if the alias is null or an empty String.
     */
    public String getFieldName(String alias) {
        if (alias == null) {
            throw new IllegalArgumentException("The alias String should not be null.");
        }
        if (alias.trim().length() == 0) {
            throw new IllegalArgumentException("The alias String should not be empty.");
        }

        return (String) aliasMap.get(alias);
    }
}
