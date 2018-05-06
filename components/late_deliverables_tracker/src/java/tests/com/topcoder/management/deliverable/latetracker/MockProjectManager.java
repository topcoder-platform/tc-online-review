/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.management.project.ConfigurationException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManagerImpl;

/**
 * <p>
 * A mock implementation of <code>ProjectManager</code>. Used for testing.
 * </p>
 *
 * @author sparemax
 * @version 1.2
 * @since 1.2
 */
public class MockProjectManager extends ProjectManagerImpl {
    /**
     * <p>
     * Represents the result.
     * </p>
     */
    private static Object result;

    /**
     * <p>
     * Constructs a new <code>MockProjectManager</code> instance.
     * </p>
     *
     * @throws ConfigurationException
     *             if error occurs while loading configuration settings, or required configuration parameter is
     *             missing.
     */
    public MockProjectManager() throws ConfigurationException {
        // Empty
    }

    /**
     * Retrieves the project instance from the persistence given its id. The project instance is retrieved with its
     * related items, such as properties.
     *
     * @param id
     *            The id of the project to be retrieved.
     *
     * @return null.
     *
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    @Override
    public Project getProject(long id) throws PersistenceException {
        if (result instanceof PersistenceException) {
            throw (PersistenceException) result;
        }
        return null;
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
        MockProjectManager.result = result;
    }
}
