/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import com.topcoder.search.builder.SearchStringBuilder;
import com.topcoder.search.builder.filter.AbstractAssociativeFilter;
import com.topcoder.search.builder.filter.AbstractSimpleFilter;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * This class is an concrete impelementor of SearchStringBuilder interface.
 * It builds sql query string accordng to SQL92 grammer according different filters.
 * </p>
 *
 * For the version 1.2, build a Database searchable from <code>LikeFilter</code> is added.
 * Defail refer to the method buildFromLikeFilter.
 *
 * <p>The class is not thread safe.</p>
 *
 * @author dmks, telly12
 * @version 1.2
 *
 */
public class DatabaseSearchStringBuilder implements SearchStringBuilder {
    /**
     * The key word 'Like' in sql.
     */
    private static final String LIKE_KEY_WORK = " LIKE '";

    /**
     * The key word 'escape' in sql.
     */
    private static final String ESCAPE_KEY_WORK = "' ESCAPE '";

    /**
     * The length of the prifix of the value in LikeFilter.
     */
    private static final int PREFIX_LENGTH = 3;

    /**
     * The DateFormat used to format Comparable Date type objects.
     * It is used when convert the Comparable Date into SQL String.
     *
     */
    private static final DateFormat DATEFORMAT = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * It will hold the context used in the search. For example,
     *  it is sql query statement (excluding where clause, but including select and from clauses);
     *
     */
    private final String context;

    /**
     * It will hold a Map containing keys as the alias names of the fields and the names of fileds as stored objects.
     *
     */
    private final Map aliasMap;

    /**
     * <p>
     * Create a new instance,by providing the context and the aliasMap.
     * </p>
     *
     * @param context context used to build the string from the filter
     * @param aliasMap the map between the alias name and real name
     * @throws NullPointerException if any parameter is Null
     * @throws IllegalArgumentException if the context is an empty string
     */
    public DatabaseSearchStringBuilder(String context, Map aliasMap) {
        //check context null
        if ((context == null) || (aliasMap == null)) {
            throw new NullPointerException(
                "the context should not be null to construct DatabaseSearchStringBuilder.");
        }

        //check context empty
        if (context.length() == 0) {
            throw new IllegalArgumentException(
                "the context should not be empty to construct DatabaseSearchStringBuilder.");
        }

        this.context = context;
        this.aliasMap = new HashMap(aliasMap);
    }

    /**
     * <p>
     * Build the sql string from given filter.
     * The sql String is integrated, both include the context and the where clause.
     * </p>
     *
     *
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    public String buildSearchString(Filter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "The filter should not be null to buildSearchString in DatabaseSearchStringBuilder.");
        }

        return context + " " + buildRecursively(filter);
    }

    /**
     * <p>build the sql string from a AndFilter.</p>
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromAndFilter(Filter filter) {
        return buildAssociativeFilter((AbstractAssociativeFilter) filter,
            " AND ");
    }

    /**
     * <p>build the string from a OrFilter.</p>
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromOrFilter(OrFilter filter) {
        return buildAssociativeFilter(filter, " OR ");
    }

    /**
     * <p>build the string from a NotFilter.</p>
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromNotFilter(NotFilter filter) {
        //check the filter null
        if (filter == null) {
            throw new NullPointerException(
                "The filter should not be null to buildFromNotFilter in DatabaseSearchStringBuilder.");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("NOT (").append(buildRecursively((filter).getFilter()))
              .append(")");

        return buffer.toString();
    }

    /**
     * <p>build the string from a InFilter.</p>
     *
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromInFilter(InFilter filter) {
        //check the filter null
        if (filter == null) {
            throw new NullPointerException(
                "The filter should not be null to buildFromNotFilter in DatabaseSearchStringBuilder.");
        }

        //get name and values
        String name = filter.getName();

        //if is alias name
        if (aliasMap.containsKey(name)) {
            name = (String) aliasMap.get(name);
        }

        List values = filter.getList();

        StringBuffer buffer = new StringBuffer();

        //is values is empty, return empty SQl
        if (values.size() == 0) {
            return " IN ()";
        }

        Iterator it = values.iterator();
        buffer.append(name);
        buffer.append(" IN (").append(getSQLString((Comparable) it.next()));

        while (it.hasNext()) {
            buffer.append(", ").append(getSQLString((Comparable) it.next()));
        }

        buffer.append(")");

        return buffer.toString();
    }

    /**
     * <p>build the string from a BetweenFilter.</p>
     *
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromBetweenFilter(BetweenFilter filter) {
        return buildSimpleFilter(filter);
    }

    /**
     * <p>build the string from a EqualToFilter.</p>
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromEqualToFilter(EqualToFilter filter) {
        return buildSimpleFilter(filter);
    }

    /**
     * <p>build the string from a LessThanFilter.</p>
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromLessThanFilter(LessThanFilter filter) {
        return buildSimpleFilter(filter);
    }

    /**
     * <p>build the string from a LessThanOrEqualToFilter.</p>
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromLessThanOrEqualToFilter(
        LessThanOrEqualToFilter filter) {
        return buildSimpleFilter(filter);
    }

    /**
     * <p>build the string from a GreaterThanFilter.</p>
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromGreaterThanFilter(GreaterThanFilter filter) {
        return buildSimpleFilter(filter);
    }

    /**
     * <p>build the string from a GreaterThanOrEqualToFilter.</p>
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromGreaterThanOrEqualToFilter(
        GreaterThanOrEqualToFilter filter) {
        return buildSimpleFilter(filter);
    }

    /**
     * <p>
     * Build a database searchable String from the LikeFilter,
     * Four types of <code>LikeFilter</code> are supported(refer the class doc of <code>LikeFilter</code> for detail).
     * </p>
     *
     * <p>This method is added in version 1.2.</p>
     *
     *
     * @return The Filter object used to build the string
     * @param filter constructed search string
     * @exception IllegalArgumentException if the filter parameter is Null
     */
    protected String buildFromLikeFilter(LikeFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The LikeFilter is null.");
        }

        StringBuffer buffer = new StringBuffer();
        String realName = filter.getName();

        //if the name in filter is only the alias, the get the realname via the alias
        if (aliasMap.containsKey(realName)) {
            realName = (String) aliasMap.get(realName);
        }

        //using the real name in the sql, it is required in the class doc
        buffer.append(realName).append(LIKE_KEY_WORK);

        String value = filter.getValue();
        String content = value.substring(PREFIX_LENGTH);

        //if value start with 'WC:', then no need to replace the escape char
        if (!value.startsWith(LikeFilter.WITH_CONTENT)) {
            content = transformString(content, filter.getEscapeCharacter());
        }

        //if the type required start with '%' tag, append a '%' firstly
        if (value.startsWith(LikeFilter.CONTAIN_TAGS)
                 || value.startsWith(LikeFilter.END_WITH_TAG)) {
            buffer.append('%');
        }

        buffer.append(content);

        //check whether should appead '%' at the end of the like filter
        if (value.charAt(0) == 'S') {
            buffer.append('%');
        }

        buffer.append(ESCAPE_KEY_WORK);
        buffer.append(filter.getEscapeCharacter()).append("'");

        return buffer.toString();
    }

    /**
     * <p>
     * Transform a String, escapes '%' and '_' in the content by adding escapeChar before them.
     * </p>
     *
     * <p>This method is updated in version 1.2.</p>
     *
     * @param content the content to be transformed.
     * @param escapeChar the char used to escape
     * @return a transformed String by escaping both '%' and '_'.
     */
    private String transformString(String content, char escapeChar) {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);

            if ((c == '%') || (c == '_') || (c == escapeChar)) {
                //add a escape Char before '%' and '_' and the escapeChar itself
                buffer.append(escapeChar);
            }

            buffer.append(c);
        }

        return buffer.toString();
    }

    /**
     * <p>build the string recursively.</p>
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildRecursively(Filter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "The filter should not be null to buildRecursively in DatabaseSearchStringBuilder.");
        }

        int type = filter.getFilterType();

        if (type == Filter.AND_FILTER) {
            return buildFromAndFilter(filter);
        } else if (type == Filter.OR_FILTER) {
            return buildFromOrFilter((OrFilter) filter);
        } else if (type == Filter.NOT_FILTER) {
            return buildFromNotFilter((NotFilter) filter);
        } else if (type == Filter.IN_FILTER) {
            return buildFromInFilter((InFilter) filter);
        } else if (type == Filter.BETWEEN_FILTER) {
            return buildFromBetweenFilter((BetweenFilter) filter);
        } else if (type == Filter.EQUAL_TO_FILTER) {
            return buildFromEqualToFilter((EqualToFilter) filter);
        } else if (type == Filter.LESS_THAN_FILTER) {
            return buildFromLessThanFilter((LessThanFilter) filter);
        } else if (type == Filter.LESS_THAN_OR_EQUAL_TO_FILTER) {
            return buildFromLessThanOrEqualToFilter((LessThanOrEqualToFilter) filter);
        } else if (type == Filter.GREATER_THAN_FILTER) {
            return buildFromGreaterThanFilter((GreaterThanFilter) filter);
        } else if (type == Filter.GREATER_THAN_OR_EQUAL_TO_FILTER) {
            return buildFromGreaterThanOrEqualToFilter((GreaterThanOrEqualToFilter) filter);
        } else if (type == Filter.LIKE_FILTER) {
            return buildFromLikeFilter((LikeFilter) filter);
        }

        return null;
    }

    /**
     * Returns the database string representation of specified Comparable value.
     * The Comparable type include: String, Date, Time, TimeStamp, Number.
     *
     * @param value the Comparable value to convert to the SQL format String.
     *
     * @return the SQL format string according to the Comparable value.
     */
    private String getSQLString(Comparable value) {
        if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Date) {
            return "'" + DATEFORMAT.format((java.util.Date) value) + "'";
        } else {
            return "'" + value.toString() + "'";
        }
    }

    /**
     * Get the SQL String according to the AbstractAssociativeFilter.
     * Two type AbstractAssociativeFilter will be include, AndFilter and OrFilter.
     * The a seperated by the param <code>associativeType</code>,
     * which is ' or ' for OrFitler, ' and ' for Andfilter.
     *
     * @param filter the AbstractAssociativeFilter to be builded
     * @param associativeType denote the type of the AbstractAssociativeFilter, And or Or.
     * @return the SQL String according to the AbstractAssociativeFilter.
     * @throws NullPointerException if the AbstractAssociativeFilter is null
     */
    private String buildAssociativeFilter(AbstractAssociativeFilter filter,
        String associativeType) {
        if (filter == null) {
            throw new NullPointerException(
                "The AbstractAssociativeFilter should not be null to build.");
        }

        StringBuffer buffer = new StringBuffer();

        // Iterate over all filters
        for (Iterator iter = filter.getFilters().iterator(); iter.hasNext();) {
            buffer.append("(").append(buildRecursively((Filter) iter.next()))
                  .append(")");

            // If any more filters follow, also append the glue
            if (iter.hasNext()) {
                buffer.append(associativeType);
            }
        }

        return buffer.toString();
    }

    /**
     * Get the SQL String according to the AbstractSimpleFilter.
     *
     * @param filter the AbstractSimpleFilter to be builded
     * @return the SQL String according to the AbstractSimpleFilter.
     * @throws NullPointerException if the AbstractSimpleFilter is null
     */
    private String buildSimpleFilter(AbstractSimpleFilter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "The AbstractSimpleFilter should not be null to build.");
        }

        String fieldName = filter.getName();
        String realName = null;

        StringBuffer buffer = new StringBuffer();

        //if the fieldName if alias name
        //get the real name
        if (aliasMap.containsKey(fieldName)) {
            realName = (String) aliasMap.get(fieldName);
        } else {
            realName = fieldName;
        }

        //if filter is betweenFilter
        if (filter.isLowerInclusive() && filter.isUpperInclusive()) {
            buffer.append(realName).append(">=").append(getSQLString(
                    filter.getLowerThreshold()));
            buffer.append(" AND ");
            buffer.append(realName).append("<=").append(getSQLString(
                    filter.getUpperThreshold()));
        } else if ((filter.getLowerThreshold() == null)
                && (filter.getUpperThreshold() == null)) {
            //if the filter is EqualFilter
            buffer.append(realName).append("=").append(getSQLString(
                    filter.getValue()));
        } else if (filter.getLowerThreshold() != null) {
            buffer.append(realName).append(">");

            if (filter.isLowerInclusive()) {
                buffer.append("=");
            }

            buffer.append(getSQLString(filter.getLowerThreshold()));
        } else {
            buffer.append(realName).append("<");

            if (filter.isUpperInclusive()) {
                buffer.append("=");
            }

            buffer.append(getSQLString(filter.getUpperThreshold()));
        }

        return buffer.toString();
    }
}
