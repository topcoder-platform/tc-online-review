/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.LikeFilter;


/**
 * <p>
 * This SearchFragmentBuilder is used to handle building the SearchContext for
 * the LikeFilter for LDAP.
 * </p>
 * <p>
 * Thread Safety: This class is thread-safe. All state information is handled
 * within the searchContext.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class LikeFragmentBuilder implements SearchFragmentBuilder {
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
     * <p>
     * Default Constructor.
     * </p>
     *
     */
    public LikeFragmentBuilder() {
        // your code here
    }

    /**
     * <p>
     * Builds the LDAP search String for a LikeFilter.
     * </p>
     *
     * @param filter
     *            the Filter from which to build the search String.
     * @param searchContext
     *            the searchContext to build the search String on.
     * @throws IllegalArgumentException
     *             if either parameter is null.
     * @throws UnrecognizedFilterException
     *             if this SearchFragmentBuilder cannot recognize the filter.
     */
    public void buildSearch(Filter filter, SearchContext searchContext)
        throws UnrecognizedFilterException {
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }

        if (searchContext == null) {
            throw new IllegalArgumentException(
                "The searchContext should not be null.");
        }

        if (!(filter instanceof LikeFilter)) {
            throw new UnrecognizedFilterException("The filter should be an LikeFilter, but a type of "
                    + filter.getClass().getName() +  ".",
                filter);
        }

        LikeFilter likeFilter = (LikeFilter) filter;

        StringBuffer buffer = searchContext.getSearchString();

        buffer.append("(")
              .append(SearchBuilderHelper.getRealName(likeFilter.getName(),
                searchContext)).append("=");

        // append the string constructed according the LikeFilter
        buffer.append(buildFromLikeFilter(likeFilter)).append(")");
    }

    /**
     * <p>
     * Build a LDAP searchable String from the LikeFilter, the wildcards in the
     * value will be replaced by the stationary pattern in LDAP. Four types of
     * <code>LikeFilter</code> are supported(refer the class doc of
     * <code>LikeFilter</code> for detail).
     * </p>
     *
     * <p>
     * This method is added in version 1.2.
     * </p>
     *
     * @param filter
     *            the LikeFilter object used to build the string
     * @return a searchable String on ldap according the LikeFilter given
     * @throws IllegalArgumentException
     *             if the LikeFilter is Null
     */
    protected String buildFromLikeFilter(LikeFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The filter should not be null.");
        }

        StringBuffer buffer = new StringBuffer();

        String value = filter.getValue();

        if (value.startsWith(LikeFilter.CONTAIN_TAGS) ||
                value.startsWith(LikeFilter.END_WITH_TAG)) {
            buffer.append("*");
        }

        // append the content after replacing all the wildcard
        buffer.append(escapeWildcard(value.substring(PREFIX_LENGTH),
                value.startsWith(LikeFilter.WITH_CONTENT),
                filter.getEscapeCharacter()));

        // if start with 'SS:' or 'SW:', add '*' at the end
        if (value.charAt(0) == 'S') {
            buffer.append("*");
        }

        return buffer.toString();
    }

    /**
     * Replace all the wildcard in the content using the stationary pattern in
     * LDAP.
     *
     * @param content
     *            the initial content
     * @param isStartWithWC
     *            the content is start with 'WC:' or not
     * @param escapeChar
     *            the escape char
     * @return the String after replacing all the wildcard
     */
    private String escapeWildcard(String content, boolean isStartWithWC,
        char escapeChar) {
        StringBuffer buffer = new StringBuffer();
        int length = content.length();

        for (int i = 0; i < length; i++) {
            char c = content.charAt(i);

            if ((c == escapeChar) && isStartWithWC && (i < (length - 1)) &&
                    (content.charAt(i + 1) == '*')) {
                // replace escapeCharacter + '*' by '\2a' if the value start
                // with 'WC:'
                buffer.append(STAR_CHAR);

                // skip the next char, which is '*'
                i++;
            } else if (c == '(') {
                buffer.append(LEFR_BRACKET);
            } else if (c == ')') {
                buffer.append(RIGHT_BRACKET);
            } else if ((c == '*') && !isStartWithWC) {
                // if value does not start with 'WC:', then the '*' should be
                // replaced.
                buffer.append(STAR_CHAR);
            } else if (c == '\\') {
                buffer.append(SLASH);
            } else {
                // c is not a key char, simple append it
                buffer.append(c);
            }
        }

        return buffer.toString();
    }
}
