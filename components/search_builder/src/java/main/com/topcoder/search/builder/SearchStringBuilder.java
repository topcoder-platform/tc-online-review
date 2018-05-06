/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * This interface only defines one method. The method is used to
 * construct the search string from the Filter Each data store system
 * sub-package should implement this interface to convert the Filter to specific search string.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 * @deprecated This public class is not used any more
 */
public interface SearchStringBuilder {
    /**
     * Construct the search string from Filter
     * Please refer to the concrete implemetor in each sub-component (data store) for details.
     *
     *
     * @param filter Filter to be used to construct search string
     * @return a string constructed from the Filter
     */
    String buildSearchString(Filter filter);
}
