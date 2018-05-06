/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.search.builder.accuracytests;

import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.NullFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.GreaterThanFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LessThanFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LikeFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.database.NullFragmentBuilder;
import com.topcoder.search.builder.database.NotFragmentBuilder;
import com.topcoder.search.builder.database.EqualsFragmentBuilder;
import com.topcoder.search.builder.database.RangeFragmentBuilder;
import com.topcoder.search.builder.database.OrFragmentBuilder;
import com.topcoder.search.builder.database.AndFragmentBuilder;
import com.topcoder.search.builder.database.InFragmentBuilder;
import com.topcoder.search.builder.database.LikeFragmentBuilder;
import com.topcoder.util.classassociations.ClassAssociator;
import com.topcoder.util.classassociations.IllegalHandlerException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>A factory producing the sample data which could be used for testing purposes. This class provides a set of static
 * constants and a set of static methods producing the sample data. Note that the methods produce a new copy of sample
 * data on each method call.</p>
 *
 * @author  isv
 * @version 1.0
 */
public class TestDataFactory {

    /**
     *
     */
    public static final String EXCEPTION_MESSAGE = "Test Exception Message";

    /**
     *
     */
    public static final Exception EXCEPTION_CAUSE = new Exception("Cause Exception Message");

    /**
     *
     */
    public static final Filter EXCEPTION_FILTER = new NullFilter("NullFilter");


    /**
     * <p>Generates a new instance of <code>Map</code> type initialized with random data.</p>
     *
     * @return a new <code>Map</code> instance.
     */
    public static Map getSearchContextAliasMap() {
        List filters = TestDataFactory.getAccuracyFilters();
        Map result = new HashMap();
        for (int i = 0; i < filters.size(); i++) {
            AccuracyFilter filter = (AccuracyFilter) filters.get(i);
            if (filter.getName().startsWith("alias")) {
                result.put(filter.getName(), filter.getName().replaceAll("alias", "attr"));
            }
        }
        return result;
    }

    /**
     * <p>Generates a new instance of <code>ClassAssociator</code> type initialized with random data.</p>
     *
     * @return a new <code>ClassAssociator</code> instance.
     */
    public static ClassAssociator getSearchContextClassAssociator() throws IllegalHandlerException {
        ClassAssociator result = new ClassAssociator();
        Filter[] filters = TestDataFactory.getSearchContextFilters();
        SearchFragmentBuilder[] builders = TestDataFactory.getSearchContextFragmentBuilders();
        for (int i = 0; i < builders.length; i++) {
            result.addClassAssociation(filters[i].getClass(), builders[i]);
        }
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Filter[]</code> type initialized with random data.</p>
     *
     * @return a new <code>Filter[]</code> instance.
     */
    public static Filter[] getSearchContextFilters() {
        Filter[] result = new Filter[13];
        result[0] = new AccuracyFilter(true, "attr1", "1");
        result[1] = new NullFilter("NullFilter");
        result[2] = new NotFilter(result[1]);
        result[3] = new EqualToFilter("EqualToFilter", "3");
        result[4] = new GreaterThanFilter("GreaterThanFilter", "4");
        result[5] = new GreaterThanOrEqualToFilter("GreaterThanOrEqualToFilter", "5");
        result[6] = new InFilter("InFilter", Arrays.asList(new String[] {"6", "6.1", "6.2"}));
        result[7] = new LessThanFilter("LessThanFilter", "7");
        result[8] = new LessThanOrEqualToFilter("LessThanOrEqualToFilter", "8");
        result[9] = new LikeFilter("LikeFilter", "WC:9");
        result[10] = new AndFilter(result[0], result[0]);
        result[11] = new OrFilter(result[0], result[0]);
        result[12] = new BetweenFilter("BetweenFilter", "12.1", "12.9");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>SearchFragmentBuilder[]</code> type initialized with random data.</p>
     *
     * @return a new <code>SearchFragmentBuilder[]</code> instance.
     */
    public static SearchFragmentBuilder[] getSearchContextFragmentBuilders() {
        SearchFragmentBuilder[] result = new SearchFragmentBuilder[13];
        result[0] = new AccuracyFragmentBuilder();
        result[1] = new NullFragmentBuilder();
        result[2] = new NotFragmentBuilder();
        result[3] = new EqualsFragmentBuilder();
        result[4] = new RangeFragmentBuilder();
        result[5] = new RangeFragmentBuilder();
        result[6] = new InFragmentBuilder();
        result[7] = new RangeFragmentBuilder();
        result[8] = new RangeFragmentBuilder();
        result[9] = new LikeFragmentBuilder();
        result[10] = new AndFragmentBuilder();
        result[11] = new OrFragmentBuilder();
        result[12] = new RangeFragmentBuilder();
        return result;
    }

    /**
     * <p>Generates a new instance of <code>SearchContext</code> type initialized with random data.</p>
     *
     * @return a new <code>SearchContext</code> instance.
     */
    public static SearchContext getSearchContext() throws IllegalHandlerException {
        return new SearchContext(TestDataFactory.getSearchContextClassAssociator(),
                                 TestDataFactory.getSearchContextAliasMap());
    }

    /**
     * <p>Generates a new instance of <code>AndFilter</code> type initialized with random data.</p>
     *
     * @return a new <code>AndFilter</code> instance.
     */
    public static AndFilter getAndFilter() {
        return new AndFilter(TestDataFactory.getAccuracyFilters());
    }

    /**
     * <p>Generates the list of accuracy filters for testing purposes.
     *
     * @return
     */
    public static List getAccuracyFilters() {
        List result = new ArrayList();
        result.add(new AccuracyFilter(true, "attr1", "1"));
        result.add(new AccuracyFilter(true, "alias2", "2"));
        result.add(new AccuracyFilter(true, "attr3", "3"));
        result.add(new AccuracyFilter(true, "alias4", "4"));
        result.add(new AccuracyFilter(true, "attr5", "5"));
        return result;
    }


}
