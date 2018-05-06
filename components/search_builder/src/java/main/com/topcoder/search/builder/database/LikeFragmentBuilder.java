/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import com.topcoder.search.builder.SearchBuilderHelper;
import com.topcoder.search.builder.SearchContext;
import com.topcoder.search.builder.SearchFragmentBuilder;
import com.topcoder.search.builder.UnrecognizedFilterException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.LikeFilter;


/**
 * <p>
 * This FragmentBuilder is used to handle building the SearchContext for the LikeFilter.
 * </p>
 *
 * <p>Thread Safety: This class is thread-safe. All state information is handled within the searchContext.</p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class LikeFragmentBuilder implements SearchFragmentBuilder {
    /**
     * The length of the prifix of the value in LikeFilter.
     */
    private static final int PREFIX_LENGTH = 3;

    /**
     * <p>Default constructor.</p>
     *
     */
    public LikeFragmentBuilder() {
    }

    /**
     * <p>Builds according to the LikeFilter.</p>
     *
     * @param filter the filter to build with.
     * @param searchContext the searchContext on which to build the filter.
     * @throws IllegalArgumentException if filter or searchContext is null.
     * @throws UnrecognizedFilterException if the filter cannot be handled by this SearchFragmentBuilder.
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

        //get the real name and append to buffer
        buffer.append(SearchBuilderHelper.getRealName(likeFilter.getName(),
                searchContext));

        buffer.append(" LIKE ?");

        //append the like string
        searchContext.getBindableParameters().add(buildFromLikeFilter(
                likeFilter));

        //set the escape char which should be set into the like filter
        buffer.append(" ESCAPE ?");
        searchContext.getBindableParameters().add(likeFilter.getEscapeCharacter() + "");
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
    private String buildFromLikeFilter(LikeFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The LikeFilter is null.");
        }

        StringBuffer buffer = new StringBuffer();

        String value = filter.getValue();
        String content = value.substring(PREFIX_LENGTH);

        //if value start with 'WC:', then no need to replace the escape char
        if (!value.startsWith(LikeFilter.WITH_CONTENT)) {
            content = transformString(content, filter.getEscapeCharacter());
        }

        //if the type required start with '%' tag, append a '%' firstly
        if (value.startsWith(LikeFilter.CONTAIN_TAGS) ||
                value.startsWith(LikeFilter.END_WITH_TAG)) {
            buffer.append('%');
        }

        buffer.append(content);

        //check whether should appead '%' at the end of the like filter
        if (value.charAt(0) == 'S') {
            buffer.append('%');
        }

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
}
