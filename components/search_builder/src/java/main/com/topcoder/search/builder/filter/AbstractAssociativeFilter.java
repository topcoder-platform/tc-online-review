/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * This abstract class is a concrete implementor of the Filter interface.
 * It is used to construct associative Filter. This abstract class also provides concrete addFilter()
 * method to add a Filter to the associative Filter.
 * However, the getFilterType() method and isValid() methods are abstract,
 * which are intended to be  implemented by subclasses differently.
 * The constructor is protected, since it is only used by its subclasses.
 * The attribute is protected not private, because the subclass of this abstract class
 * should have the access to the attribute.
 * </p>
 *
 * <p>The clone method is updated in version 1.3.</p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public abstract class AbstractAssociativeFilter implements Filter {
    /**
     * It will hold a list of Filter objcts, which consists of the associative filter. Null elemnt is not allowed.
     *
     */
    protected List filters;

    /**
     * <p>
     * Create a new instance,by providing the firstFilter and secondFilter
     * this contructor is create to AndFilter and OrFilter.
     * </p>
     *
     *
     * @param firstFilter a <code> Filter </code> object
     * @param secondFilter a <code> Filter </code> object
     * @throws IllegalArgumentException if any parameter is Null
     */
    protected AbstractAssociativeFilter(Filter firstFilter, Filter secondFilter) {
        if(firstFilter == null) {
            throw new IllegalArgumentException("The firstFilter should not be null.");
        }
        if (secondFilter == null) {
            throw new IllegalArgumentException(
                "The secondFilter should not be null to construct AbstractAssociativeFilter");
        }

        filters = new ArrayList();

        //add the param
        filters.add(firstFilter);

        filters.add(secondFilter);
    }

    /**
     * <p>
     * Create a new instance,by providing the filters
     * this contructor is create to AndFilter and OrFilter.
     * </p>
     *
     *
     * @param filters to set the filters member
     * @throws IllegalArgumentException if param is null
     * @throws IllegalArgumentException if param is empty or the members in the list is not instanceof filter
     */
    protected AbstractAssociativeFilter(List filters) {
        FilterHelper.checkList(filters, "filters");
        //check all the members in the filters.
        for (int i = 0; i < filters.size(); i++) {
            Object object = filters.get(i);

            if (object instanceof Filter) {
                continue;
            }

            throw new IllegalArgumentException(
                "The member of the filters should instance of Filter");
        }

        this.filters = new ArrayList(filters);
    }


    /**
     * <p>
     * Add a component filter to the associative filter
     * this mothed is to AndFilter and OrFilter.
     * </p>
     *
     * @param filter a <code> Filter </code> object
     * @throws IllegalArgumentException if any parameter is Null
     */
    public void addFilter(Filter filter) {
        if (filter == null) {
            throw new IllegalArgumentException(
                "The filter should not be null when add to fiters");
        }

        filters.add(filter);
    }
    /**
     * <p>return the component filters.</p>
     *
     *
     *
     * @return a colne of list of filters
     */
    public List getFilters() {
        return new ArrayList(filters);
    }
    /**
     * Set the filters.
     *
     * @param filters the Filter list to be set
     */
    public void setFilters(List filters) {
        FilterHelper.checkList(filters, "filters");
        //check all the members in the filters.
        for (int i = 0; i < filters.size(); i++) {
            Object object = filters.get(i);

            if (object instanceof Filter) {
                continue;
            }

            throw new IllegalArgumentException(
                "The member of the filters should instance of Filter");
        }

        this.filters = new ArrayList(filters);
    }

    /**
     * <p>This method overrides the clone method to provide a deep copy of all the filters within.</p>
     *
     * <p>This method is updated in version 1.3.</p>
     *
     * @return a clone copy of this AbstractAssociativeFilter
     */
    public Object clone() {
        AbstractAssociativeFilter associativeFilter = null;
        try {
            associativeFilter = (AbstractAssociativeFilter) super.clone();
        } catch (CloneNotSupportedException e) {
            //will never be thrown
        }

        List list = associativeFilter.getFilters();

        List colneFilters = new ArrayList();
        for(Iterator it = list.iterator(); it.hasNext();) {
            Filter filter = (Filter) it.next();
            colneFilters.add((Filter)filter.clone());
        }
        associativeFilter.setFilters(colneFilters);
        return associativeFilter;
    }
}
