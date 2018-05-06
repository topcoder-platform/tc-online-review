/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LikeFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.OrFilter;

import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class represents a search bundle. It is the most important class of the component.It can Build Filter tree.
 * Since the Filter package employs the composite pattern, we have simple Filters and associative filters.
 * Thus all Filters defined for a search can form a tree. The leaves are simple Filters and the parent nodes
 * are associative Filters.And also thoses filters can be created in this class.
 * </p>
 *
 * <p>
 * It also can Validate the constructed Filter according to the rules holding by
 * ObjectValidator objects.Also it do the converting the Filter to search string via the builder.
 * </p>
 *
 * <p>
 * Most import, all the searchFunction is implemented by this class as the API of the component.
 * Both include the Database and LADP.
 * </p>
 *
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class SearchBundle {
    /**
     * It will hold the name of the search bundle.
     *
     */
    private final String name;

    /**
     * The context of the search bundle.
     */
    private final String context;

    /**
     * It will hold a Map of ObjectValidator objects. keys are the real name (or alias name) of searchable fileds.
     * ObjectValidator are as the stored object.
     *
     */
    private Map searchableFields;

    /**
     * It will hold a Map containing keys as the alias names of the fields and the names of fileds as stored objects.
     * The search Fields name in the filter can both be the alias name and real name.
     * But the name builded to the SearchString(include SQL) must be the real name.
     *
     */
    private final Map aliasMap;

    /**
     * <p>
     * This is the search strategy that is used to perform the actual searching work for the bundle.
     * The search strategy is responsible for connecting to the datastore,
     * building the necessary query String (if applicable) and executing it against the datastore.
     * The object returned from the search is dependent on the type of datastore that is used.
     * </p>
     * 
     * It is added in version 1.3.
     *
     */
    private SearchStrategy searchStrategy;

    /**
     * <p>
     * Create a new instance,providing the name ,searchable fields,map of alias.
     * All the other member are initialized to null.
     * </p>
     *
     * @param name name of the search bundle
     * @param fields a map of searchable fields
     * @param alias a map of strings, holding the names of fields as stored objects and alias names as keys
     * @param context the context of the search bundle
     * @throws IllegalArgumentException if any parameter is null or the keys and values in the map is null,
     * or name, or fileds, or context is empty
     */
    public SearchBundle(String name, Map fields, Map alias, String context) {
        if (name == null) {
            throw new IllegalArgumentException("The name should not be null to construct SearchBundle.");
        }

        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("The name should not be empty to construct SearchBundle.");
        }
        if (context == null) {
            throw new IllegalArgumentException("The context should not be null to construct SearchBundle.");
        }

        if (context.trim().length() == 0) {
            throw new IllegalArgumentException("The context should not be empty to construct SearchBundle.");
        }

        SearchBuilderHelper.checkaliasMap(alias, "alias");

        //set value for member
        this.name = name;
        this.context = context;
        setSearchableFields(fields);
        this.aliasMap = new HashMap(alias);

        this.searchStrategy = null;
    }

    /**
     * <p>
     * Create a new instance,providing the name ,map of alias.
     * All the other member are initialized to null.
     * </p>
     *
     * @param name name of the search bundle
     * @param alias a map of strings, holding the names of fields as stored objects and alias names as keys
     * @param context the context of the search bundle
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if name, or fileds, or context is empty
     */
    public SearchBundle(String name, Map alias, String context) {
        if (name == null) {
            throw new IllegalArgumentException("The param should not be null to construct SearchBundle.");
        }

        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("The param should not be empty to construct SearchBundle.");
        }
        if (context == null) {
            throw new IllegalArgumentException("The context should not be null to construct SearchBundle.");
        }

        if (context.trim().length() == 0) {
            throw new IllegalArgumentException("The context should not be empty to construct SearchBundle.");
        }

        SearchBuilderHelper.checkaliasMap(alias, "alias");

        this.name = name;
        this.context = context;
        this.aliasMap = new HashMap(alias);
        this.searchableFields = null;
        this.searchStrategy = null;
    }

    /**
     * <p> Create a new instance. </p>
     * <p> The fields parameter should contain the fields that are searchable within the datastore.
     * The keys of the fields are the names of the fields which are searchable.
     * The values of the fields are the ObjectValidator instances which may be used to
     * constrain the valid values that are used for the filters.
     * An empty fields means that ALL the fields in the context are searchable and no validation will need to occur.
     * A field mapped to a null value means that there are no constraints for that value.
     * </p>
     *
     * @param name name of the search bundle
     * @param fields a map of searchable fields
     * @param context the context of the search bundle
     * @param searchStrategy The search Strategy to use.
     * @throws IllegalArgumentException if any parameter is null or name is empty
     */
    public SearchBundle(String name, Map fields, String context, SearchStrategy searchStrategy) {
        this(name, fields, new HashMap(), context);
        setSearchStrategy(searchStrategy);
    }

    /**
     * <p> The fields parameter should contain the fields that are searchable within the datastore.
     * The keys of the fields are the names of the fields which are searchable.
     * The values of the fields are the ObjectValidator instances which may be used to constrain the valid
     * values that are used for the filters. An empty fields means that ALL the fields in the context are
     * searchable and no validation will need to occur.
     * A field mapped to a null value means that there are no constraints for that value.
     * </p>
     *
     * <p>
     * The alias parameter keys contain an alternate name for a searchable field,
     * (for example, the column widgets.widget_name may be better off being named "Widget Name").
     * The values will contain the search field as it would appear in the datastore context.
     * </p>
     *
     * @param name name of the search bundle
     * @param fields a map of searchable fields
     * @param alias a map of strings, holding the alternate names of fields as
     * keys and their actual values in the datastore as the respective values.
     * @param context the context of the search bundle
     * @param searchStrategy The search Strategy to use.
     * @throws IllegalArgumentException if any parameter is null
     * or name, or fileds is empty, or the alias is invalid
     */
    public SearchBundle(String name, Map fields, Map alias, String context, SearchStrategy searchStrategy) {
        this(name, fields, alias, context);
        setSearchStrategy(searchStrategy);
    }

    /**
     * <p>
     * Execut the search with given constructed Filter,both simple and AssociativeFilter will do.
     * The result will be returned as an object instanceof CustomResultSet for the DataBase search.
     * The result will be returned as an abject instanceof Iterator of the Entry for the LDAP.
     * </p>
     *
     * @param filter The Filter object used to conduct the search
     * @return an Object holding the search result
     * @throws IllegalArgumentException if any parameter is null
     * @throws SearchBuilderException if the filter is invalid
     * @throws PersistenceOperationException if any error when operating over data store or the filter is invalid
     */
    public  Object search(Filter filter) throws SearchBuilderException {
        return search(filter, new ArrayList());
    }

    /**
     * <p>
     * Execut the search with given constructed Filter and return fileds,
     * now the return fields are not only about the context,but also ref to returnFields,both will be returned.
     * The result will be returned as an object instanceof CustomResultSet for the DataBase search.
     * The result will be returned as an abject instanceof Iterator of the Entry for the LDAP.
     * </p>
     *
     *
     * @param filter The Filter object used to conduct the search
     * @param returnFields a list of names of return fields
     * @return an Object holding the search result
     * @throws IllegalArgumentException if any parameter is null
     * @throws SearchBuilderException if the filter is invalid
     * @throws PersistenceOperationException if any error when operating over data store or the filter is invalid.
     */
    public Object search(Filter filter, List returnFields) throws SearchBuilderException {
        if(filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }
        if(returnFields == null) {
            throw new IllegalArgumentException("The returnFields should not be null.");
        }

        ValidationResult result = null;

        try {
            result = validateFilter(filter);
        } catch (Exception e) {
            //wrap the PersistenceOperationException if error occurs during the validateFilter
            throw new PersistenceOperationException(
                    "Exception occurs while do the validateFilter to search in SearchBundle.", e);
        }

        //filter invalid
        if (!result.isValid()) {
            throw new SearchBuilderException("The filter to search is invalid in SearchBundle.");
        }

        try {
            synchronized(this) {
                return searchStrategy.search(context, filter, returnFields, aliasMap);
            }
        } catch (Exception e) {
            //wrap the PersistenceOperationException if error occurs during the validateFilter
            throw new PersistenceOperationException(
                    "Exception occurs while get the searchString and do search with returnfields in SearchBundle.", e);
        }
    }

    /**
     * <p>Build a GreaterThanFilter using the real name of the field.</p>
     *
     *
     *
     * @param name name of the field, might be alias, mihgt be real name
     * @param value a Comparable object representing the value constraint of the field
     * @return constructed Filter object
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if name is an empty string
     * @deprecated This method is now deprecated.
     */
    public static Filter buildGreaterThanFilter(String name, Comparable value) {
        //Exception will throw in GreaterThanFilter construct if param invalid
        return (Filter) new GreaterThanFilter(name, value);
    }

    /**
     * <p>Build a LessThanFilter using the real name of the field.</p>
     *
     * @param name name of the field, might be alias, mihgt be real name
     * @param value a Comparable object representing the value constraint of the field
     * @return constructed Filter object
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if name is an empty string
     * @deprecated This method is now deprecated.
     */
    public static Filter buildLessThanFilter(String name, Comparable value) {
        //Exception will throw in LessThanFilter construct if param invalid
        return (Filter) new LessThanFilter(name, value);
    }

    /**
     * <p>Build a GreaterThanOrEqualToFilter using the real name of the field.</p>
     *
     *
     * @param name name of the field, might be alias, mihgt be real name
     * @param value a Comparable object representing the value constraint of the field
     * @return constructed Filter object
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if name is an empty string
     * @deprecated This method is now deprecated.
     */
    public static Filter buildGreaterThanOrEqualToFilter(String name, Comparable value) {
        //Exception will throw in GreaterThanOrEqualToFilter construct if param invalid
        return (Filter) new GreaterThanOrEqualToFilter(name, value);
    }

    /**
     * <p>Build a LessThanOrEqualToFilter using the real name of the field.</p>
     *
     *
     * @param name name of the field, might be alias, mihgt be real name
     * @param value a Comparable object representing the value constraint of the field
     * @return constructed Filter object
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if name is an empty string
     * @deprecated This method is now deprecated.
     */
    public static Filter buildLessThanOrEqualToFilter(String name, Comparable value) {
        //Exception will throw in LessThanOrEqualToFilter construct if param invalid
        return (Filter) new LessThanOrEqualToFilter(name, value);
    }

    /**
     * <p>Build an EqualToFilter using the real name of the field.</p>
     *
     * @param name name of the field, might be alias, mihgt be real name
     * @param value a Comparable object representing the value constraint of the field
     * @return constructed Filter object
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if name is an empty string
     * @deprecated This method is now deprecated.
     */
    public static Filter buildEqualToFilter(String name, Comparable value) {
        //Exception will throw in EqualToFilter construct if param invalid
        return (Filter) new EqualToFilter(name, value);
    }

    /**
     * <p>Build a BetweenFilter using the real name of the field.</p>
     *
     * @param name name of the field, might be alias, mihgt be real name
     * @param upper a Comparable object representing the upper threshold value of the field
     * @param lower a Comparable object representing the lower threshold value of the field
     * @return constructed Filter object
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if name is an empty string
     * @deprecated This method is now deprecated.
     */
    public static Filter buildBetweenFilter(String name, Comparable upper, Comparable lower) {
        //Exception will throw in BetweenFilter construct if param invalid
        return (Filter) new BetweenFilter(name, upper, lower);
    }

    /**
     * <p>Build an InFilter using the real name of the field.</p>
     *
     *
     * @param name name of the field, might be alias, mihgt be real name
     * @param values a Comparable object representing the value constraints of the field
     * @return constructed Filter object
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if name is an empty string
     * @deprecated This method is now deprecated.
     */
    public static Filter buildInFilter(String name, List values) {
        //Exception will throw in InFilter construct if param invalid
        return (Filter) new InFilter(name, values);
    }

    /**
     * <p>Build an AndFilter using the real name of the field.</p>
     *
     *
     * @param first a Filter object to be used to build an associative Filter
     * @param second a Filter object to be used to build an associative Filter
     * @return constructed Filter object
     * @throws IllegalArgumentException if any parameter is null
     * @deprecated This method is now deprecated.
     */
    public static Filter buildAndFilter(Filter first, Filter second) {
        //Exception  will throw in AndFilter construct if param invalid
        return (Filter) new AndFilter(first, second);
    }

    /**
     * <p>Build an OrFilter using the real name of the field.</p>
     *
     * @param first a Filter object to be used to build an associative Filter
     * @param second a Filter object to be used to build an associative Filter
     * @return constructed Filter object
     * @throws IllegalArgumentException if any parameter is null
     * @deprecated This method is now deprecated.
     */
    public static Filter buildOrFilter(Filter first, Filter second) {
        //Exception  will throw in OrFilter construct if param invalid
        return (Filter) new OrFilter(first, second);
    }

    /**
     * <p>Build an NotFilter providing the notFilter.</p>
     *
     * @param filter a Filter to be negated
     * @return constructed Filter object
     * @throws IllegalArgumentException if any parameter is null
     * @deprecated This method is now deprecated.
     */
    public static Filter buildNotFilter(Filter filter) {
        //Exception  will throw in NotFilter construct if param invalid
        return (Filter) new NotFilter(filter);
    }

    /**
     * <p>Build a LikeFilter with the given name and value.</p>
     *
     * <p>This method is added in version 1.2.</p>
     *
     * @param name the name of the field, might be alias, might be real name
     * @param value the searching value in the filter
     * @return constructed LikeFilter object
     * @throws IllegalArgumentException if any parameter is null or empty,
     * or value does not begin with 'SS', 'SW', 'EW', 'WC',
     * or the value without the prefix is zero length
     * @deprecated This method is now deprecated.
     */
    public static Filter buildLikeFilter(String name, String value) {
        return new LikeFilter(name, value);
    }

    /**
     * <p>Build a LikeFilter with the given name, value and escape character.</p>
     *
     * <p>This method is added in version 1.2.</p>
     *
     * @param name the name of the field, might be alias, mihgt be real name
     * @param value the searching value in the filter
     * @param escapeCharacter the character used to escape
     * @return constructed LikeFilter object
     * @throws IllegalArgumentException if any parameter is null or empty,
     * or value does not begin with 'SS', 'SW', 'EW', 'WC',
     * or the value without the prefix is zero length,
     * or the escapeCharacter is of some masked chars('*', '%', '_')
     * @deprecated This method is now deprecated.
     */
    public static Filter buildLikeFilter(String name, String value, char escapeCharacter) {
        return new LikeFilter(name, value, escapeCharacter);
    }

    /**
     * <p>
     * Set searchable fields,providing the map of fields.
     * </p>
     *
     *
     * @param fields searchable fields  to be set
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if any parameter is empty
     */
    public synchronized void setSearchableFields(Map fields) {
        SearchBuilderHelper.checkfieldsMap(fields, "fields");

        searchableFields = new HashMap(fields);
        //using always true validator if any is missing
        for (Iterator it = searchableFields.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getValue() == null) {
                entry.setValue(new AlwaysTrueValidator());
            }
        }
    }

    /**
     * <p>
     * Validate the Filter according to the rules setted in the fields map.
     * The searchableFields names are the keys while the ObjectValidators are the values.
     * Also the keys may both the realname and alias name.
     * </p>
     *
     *
     * @param filter a Filter object to be validated
     * @return a ValidationResult object representing the validation result
     * @throws IllegalArgumentException if any parameter is null
     */
    public ValidationResult validateFilter(Filter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }
        //Exception will throw in Filter construct if param invalid
        return filter.isValid(searchableFields, aliasMap);
    }

    /**
     * <p>
     * This method is useless in version 1.3, it just do nothing.
     * </p>
     *
     *
     * @param statement just ignore
     * @deprecated This public method is not supported
     */
    public synchronized void compileStatement(String statement) {
        //do nothing
    }

    /**
     * <p>
     * Do not support this method in version 1.3, throw OperationNotSupportedException always.
     * </p>
     *
     *
     * @param parameters just ignore
     * @return none, always throw OperationNotSupportedException
     * @throws OperationNotSupportedException always
     * @deprecated This public method is not supported
     */
    public synchronized CustomResultSet executePrecompiledStatement(List parameters)
        throws OperationNotSupportedException {
        throw new OperationNotSupportedException("The method is not supported in version 1.3.");
    }

    /**
     * <p>Returns the name of the search bundle.</p>
     *
     * @return the name of the searchbundle
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Retrieves the search strategy that is used to perform the actual searching work for the bundle.
     * The search strategy is responsible for connecting to the datastore,
     * building the necessary query String (if applicable) and executing it against the datastore.
     * The object returned from the search is dependent on the type of datastore that is used.
     * </p>
     *
     * @return the search strategy that is used to perform the actual searching work for the bundle.
     */
    public SearchStrategy getSearchStrategy() {
        return searchStrategy;
    }

    /**
     * <p>
     * Sets the search strategy that is used to perform the actual searching work for the bundle.
     * The search strategy is responsible for connecting to the datastore, building the necessary query String
     * (if applicable) and executing it against the datastore.
     * The object returned from the search is dependent on the type of datastore that is used.
     * </p>
     *
     * @param searchStrategy the search strategy that is used to perform the actual searching work for the bundle.
     * @throws IllegalArgumentException if searchStrategy is null.
     */
    public synchronized void setSearchStrategy(SearchStrategy searchStrategy) {
        if (searchStrategy == null) {
            throw new IllegalArgumentException("The searchStrategy should not be null.");
        }
        this.searchStrategy = searchStrategy;
    }

    /**
     * <p>Not support this method.</p>
     *
     * @param builder just ignore
     * @deprecated This public method is not supported
     *
     */
    public synchronized void setSearchStringBuilder(SearchStringBuilder builder) {
    }

    /**
     * <p>Not support this method.</p>
     *
     * @param strategy just ignore
     * @deprecated This public method is not supported
     *
     */
    public synchronized void setConnectionStrategy(ConnectionStrategy strategy) {
        //do nothing
    }

    /**
     * <p>Not support this method.</p>
     *
     * @param filter just ignore
     * @deprecated This public method is not supported
     * @return null
     */
    protected String getSearchString(Filter filter) {
        //return null always
        return null;
    }
}
