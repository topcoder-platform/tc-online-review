/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import com.topcoder.search.builder.SearchStringBuilder;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * This class is an concrete impelementor of SearchStringBuilder interface.
 * It builds  LDAP filter string accordng to RFC 2254.
 *
 * For the version 1.2, build a LDAP searchable from <code>LikeFilter</code> is added.
 * Defail refer to the method buildFromLikeFilter.
 *
 * @author dmks, telly12
 * @version 1.2
 *
 */
public class LDAPSearchStringBuilder implements SearchStringBuilder {
    /**
     * A String represent the '*' in ldap search String.
     */
    private static final String STAR_CHAR = "\\2a";

    /**
     * A String represent the '(' in ldap search String.
     */
    private static final String LEFR_BRACKET = "\\28";

    /**
     * A String represent the ')' in ldap search String.
     */
    private static final String RIGHT_BRACKET = "\\29";

    /**
     * A String represent the '\' in ldap search String.
     */
    private static final String SLASH = "\\5c";

    /**
     * The length of the prifix of the value in LikeFilter.
     */
    private static final int PREFIX_LENGTH = 3;

    /**
    * It will hold a Map containing keys as the alias names of the fields and the names of fileds as stored objects.
    *
    */
    private final Map aliasMap;

    /**
     * <p>Create a new instance by setting the aliasMap.</p>
     *
     * @param aliasMap the aliasMap map to map the alias name and real name.
     * @throws NullPointerException if the aliasMap is null.
     */
    public LDAPSearchStringBuilder(Map aliasMap) {
        //empty construct
        super();

        if (aliasMap == null) {
            throw new NullPointerException(
                "The aliasMap should not be null to contruct LDAPSearchStringBuilder.");
        }

        this.aliasMap = new HashMap(aliasMap);
    }

    /**
     * <p>build the string from given filter.</p>
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    public String buildSearchString(Filter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildSearchString in LDAPSearchStringBuilder.");
        }

        return buildRecursively(filter);
    }

    /**
     * <p>build the string from a AndFilter.</p>
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromAndFilter(AndFilter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildFromAndFilter in LDAPSearchStringBuilder.");
        }

        List list = filter.getFilters();

        List result = new ArrayList();
        Iterator filterIt = list.iterator();

        while (filterIt.hasNext()) {
            result.add(buildSearchString((Filter) filterIt.next()));
        }

        Iterator resultIt = result.iterator();

        //makeup the searchString
        StringBuffer buffer = new StringBuffer((String) resultIt.next());

        while (resultIt.hasNext()) {
            buffer = new StringBuffer("(&" + buffer.toString());
            buffer.append(" ");
            buffer.append((String) resultIt.next());
            buffer.append(")");
        }

        return buffer.toString();
    }

    /**
     * <p>build the string from a OrFilter.</p>
     *
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromOrFilter(OrFilter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildFromOrFilter in LDAPSearchStringBuilder.");
        }

        List list = filter.getFilters();

        List result = new ArrayList();
        Iterator filterIt = list.iterator();

        while (filterIt.hasNext()) {
            result.add(buildSearchString((Filter) filterIt.next()));
        }

        Iterator resultIt = result.iterator();

        //makeup the searchString
        StringBuffer buffer = new StringBuffer((String) resultIt.next());

        while (resultIt.hasNext()) {
            buffer = new StringBuffer("(|" + buffer.toString());
            buffer.append(" ");
            buffer.append((String) resultIt.next());
            buffer.append(")");
        }

        return buffer.toString();
    }

    /**
     * <p>build the string from a NotFilter.</p>
     *
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromNotFilter(NotFilter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildFromNotFilter in LDAPSearchStringBuilder.");
        }

        StringBuffer buffer = new StringBuffer("(!");
        buffer.append(buildSearchString((filter).getFilter()));
        buffer.append(")");

        return buffer.toString();
    }

    /**
     * <p>build the string from a InFilter.</p>
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromInFilter(InFilter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildFromInFilter in LDAPSearchStringBuilder.");
        }

        String name = filter.getName();

        //if is alias name
        if (aliasMap.containsKey(name)) {
            name = (String) aliasMap.get(name);
        }

        List list = filter.getList();

        //makeup the searchString
        StringBuffer buffer = new StringBuffer("(");
        buffer.append(name);
        buffer.append("=");
        buffer.append(list.get(0).toString());
        buffer.append(")");

        for (int j = 1; j < list.size(); j++) {
            buffer = new StringBuffer("(|" + buffer.toString());
            buffer.append(" (");
            buffer.append(name);
            buffer.append("=");
            buffer.append(list.get(j).toString());
            buffer.append("))");
        }

        return buffer.toString();
    }

    /**
     * <p>build the string from a BetweenFilter.</p>
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromBetweenFilter(BetweenFilter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildFromBetweenFilter in LDAPSearchStringBuilder.");
        }

        String name = filter.getName();

        //if is alias name, get the real name
        if (aliasMap.containsKey(name)) {
            name = (String) aliasMap.get(name);
        }

        Comparable lowThreshold = filter.getLowerThreshold();

        Comparable upperThreshold = filter.getUpperThreshold();
        StringBuffer buffer = new StringBuffer("(&(");
        buffer.append(name);
        buffer.append(">=");
        buffer.append(lowThreshold.toString());
        buffer.append(")(");
        buffer.append(name);
        buffer.append("<=");
        buffer.append(upperThreshold.toString());
        buffer.append("))");

        return buffer.toString();
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
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildFromEqualToFilter in LDAPSearchStringBuilder.");
        }

        String name = filter.getName();

        //if is alias name
        if (aliasMap.containsKey(name)) {
            name = (String) aliasMap.get(name);
        }

        Comparable value = filter.getValue();

        StringBuffer buffer = new StringBuffer("(");
        buffer.append(name);
        buffer.append(" = ");
        buffer.append(value.toString());
        buffer.append(")");

        return buffer.toString();
    }

    /**
     * <p>build the string from a LessThanFilter.</p>
     *
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromLessThanFilter(LessThanFilter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildFromLessThanFilter in LDAPSearchStringBuilder.");
        }

        String name = filter.getName();

        //if is alias name
        if (aliasMap.containsKey(name)) {
            name = (String) aliasMap.get(name);
        }

        Comparable value = filter.getValue();
        StringBuffer buffer = new StringBuffer("(&(");
        buffer.append(name);
        buffer.append("<=");
        buffer.append(value.toString());
        buffer.append(") (!(");
        buffer.append(name);
        buffer.append("=");
        buffer.append(value.toString());
        buffer.append(")))");

        return buffer.toString();
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
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildFromLessThanOrEqualToFilter in LDAPSearchStringBuilder.");
        }

        String name = filter.getName();

        //if is alias name
        if (aliasMap.containsKey(name)) {
            name = (String) aliasMap.get(name);
        }

        Comparable value = filter.getValue();
        StringBuffer buffer = new StringBuffer("(");
        buffer.append(name);
        buffer.append("<=");
        buffer.append(value.toString());
        buffer.append(")");

        return buffer.toString();
    }

    /**
     * <p>build the string from a GreaterThanToFilter.</p>
     *
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromGreaterThanFilter(GreaterThanFilter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildFromGreaterThanFilter in LDAPSearchStringBuilder.");
        }

        String name = filter.getName();

        //if is alias name
        if (aliasMap.containsKey(name)) {
            name = (String) aliasMap.get(name);
        }

        Comparable value = filter.getValue();
        StringBuffer buffer = new StringBuffer("(&(");
        buffer.append(name);
        buffer.append(">=");
        buffer.append(value.toString());
        buffer.append(") (!(");
        buffer.append(name);
        buffer.append("=");
        buffer.append(value.toString());
        buffer.append(")))");

        return buffer.toString();
    }

    /**
     * <p>build the string from a GreaterThanOrEqualToFilter.</p>
     *
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildFromGreaterThanOrEqualToFilter(
        GreaterThanOrEqualToFilter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildFromGreaterThanOrEqualToFilter.");
        }

        String name = filter.getName();

        //if is alias name
        if (aliasMap.containsKey(name)) {
            name = (String) aliasMap.get(name);
        }

        Comparable value = filter.getValue();
        StringBuffer buffer = new StringBuffer("(");
        buffer.append(name);
        buffer.append(">=");
        buffer.append(value.toString());
        buffer.append(")");

        return buffer.toString();
    }

    /**
     * <p>
     * Build a LDAP searchable String from the LikeFilter,
     * the wildcards in the value will be replaced by the stationary pattern in LDAP.
     * Four types of <code>LikeFilter</code> are supported(refer the class doc of <code>LikeFilter</code> for detail).
     * </p>
     *
     * <p>This method is added in version 1.2.</p>
     *
     * @param filter the LikeFilter object used to build the string
     * @return a searchable String on ldap according the LikeFilter given
     * @throws IllegalArgumentException if the LikeFilter is Null
     */
    protected String buildFromLikeFilter(LikeFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }

        StringBuffer buffer = new StringBuffer();
        String name = filter.getName();

        //if is alias name
        if (aliasMap.containsKey(name)) {
            name = (String) aliasMap.get(name);
        }

        buffer.append("(").append(name).append("=");

        String value = filter.getValue();

        if (value.startsWith(LikeFilter.CONTAIN_TAGS)
                || value.startsWith(LikeFilter.END_WITH_TAG)) {
            buffer.append("*");
        }

        //append the content after replacing all the wildcard
        buffer.append(escapeWildcard(value.substring(PREFIX_LENGTH),
                value.startsWith(LikeFilter.WITH_CONTENT),
                filter.getEscapeCharacter()));

        //if start with 'SS:' or 'SW:', add '*' at the end
        if (value.charAt(0) == 'S') {
            buffer.append("*");
        }

        buffer.append(")");

        return buffer.toString();
    }

    /**
     * Replace all the wildcard in the content using the stationary pattern in LDAP.
     *
     * @param content the initial content
     * @param isStartWithWC  the content is start with 'WC:' or not
     * @param escapeChar the escape char
     * @return the String after replacing all the wildcard
     */
    private String escapeWildcard(String content, boolean isStartWithWC,
        char escapeChar) {
        StringBuffer buffer = new StringBuffer();
        int length = content.length();

        for (int i = 0; i < length; i++) {
            char c = content.charAt(i);

            if ((c == escapeChar) && isStartWithWC && (i < (length - 1))
                    && (content.charAt(i + 1) == '*')) {
                //replace escapeCharacter + '*' by '\2a' if the value start with 'WC:'
                buffer.append(STAR_CHAR);

                //skip the next char, which is '*'
                i++;
            } else if (c == '(') {
                buffer.append(LEFR_BRACKET);
            } else if (c == ')') {
                buffer.append(RIGHT_BRACKET);
            } else if ((c == '*') && !isStartWithWC) {
                //if value does not start with 'WC:', then the '*' should be replaced.
                buffer.append(STAR_CHAR);
            } else if (c == '\\') {
                buffer.append(SLASH);
            } else {
                //c is not a key char, simple append it
                buffer.append(c);
            }
        }

        return buffer.toString();
    }

    /**
     * <p>build the string recursively.</p>
     *
     *
     * <p>This method is updated in version 1.2.</p>
     *
     * @param filter The Filter object used to build the string
     * @return constructed search string
     * @throws NullPointerException if any parameter is Null
     */
    protected String buildRecursively(Filter filter) {
        if (filter == null) {
            throw new NullPointerException(
                "the filter should not be null to buildRecursively in LDAPSearchStringBuilder.");
        }

        //get type
        int type = filter.getFilterType();

        if (type == Filter.AND_FILTER) {
            return buildFromAndFilter((AndFilter) filter);
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
}
