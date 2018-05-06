/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.persistence.PersistenceResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistence;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * A mock implementation of <code>ResourceManager</code>. Used for testing.
 * </p>
 *
 * @author sparemax
 * @version 1.2
 * @since 1.2
 */
public class MockResourceManager extends PersistenceResourceManager {
    /**
     * <p>
     * Represents the result.
     * </p>
     */
    private static Object result;

    /**
     * <p>
     * Constructs a new <code>MockResourceManager</code> instance.
     * </p>
     *
     * @param persistence
     *            the persistence.
     * @param searchBundleManager
     *            the search bundle manager.
     */
    public MockResourceManager(ResourcePersistence persistence, SearchBundleManager searchBundleManager) {
        super(persistence, searchBundleManager);
    }

    /**
     * <p>
     * Searches the resources in the persistence store using the given filter. The filter can be formed using the
     * field names and utility methods in ResourceFilterBuilder. The return will always be a non-null (possibly 0
     * item) array.
     * </p>
     *
     * @param filter
     *            the filter to use.
     *
     * @return the loaded resources.
     *
     * @throws ResourcePersistenceException
     *             if there is an error reading the persistence store.
     * @throws SearchBuilderException
     *             if there is an error executing the filter.
     */
    @Override
    public Resource[] searchResources(Filter filter) throws ResourcePersistenceException, SearchBuilderException {

        if (result instanceof ResourcePersistenceException) {
            throw (ResourcePersistenceException) result;
        }
        if (result instanceof SearchBuilderException) {
            throw (SearchBuilderException) result;
        }

        return new Resource[0];
    }

    /**
     * <p>
     * Sets the result.
     * </p>
     *
     * @param result
     *            the result.
     */
    public static void setResult(Object result) {
        MockResourceManager.result = result;
    }
}
