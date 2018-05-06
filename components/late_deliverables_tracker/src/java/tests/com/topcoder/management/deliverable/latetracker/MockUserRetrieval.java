/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.impl.DBUserRetrieval;

/**
 * <p>
 * A mock implementation of <code>UserRetrieval</code>. Used for testing.
 * </p>
 *
 * @author sparemax
 * @version 1.2
 * @since 1.2
 */
public class MockUserRetrieval extends DBUserRetrieval {
    /**
     * <p>
     * Represents the result.
     * </p>
     */
    private static Object result;

    /**
     * <p>
     * Constructs a new <code>MockUserRetrieval</code> instance.
     * </p>
     *
     * @param namespace
     *            the namespace.
     *
     * @throws ConfigException
     *             if the connection name doesn't correspond to a connection the factory knows about.
     */
    public MockUserRetrieval(String namespace) throws ConfigException {
        super(namespace);
    }

    /**
     * <p>
     * Retrieves the external user with the given id.
     * </p>
     *
     * @param id
     *            the id of the user we are interested in.
     *
     * @return null.
     *
     * @throws RetrievalException
     *             if any exception occurred during processing; it will wrap the underlying exception.
     * @throws RuntimeException
     *             if an unexpected error occurs.
     */
    @Override
    public ExternalUser retrieveUser(long id) throws RetrievalException {
        if (result instanceof RetrievalException) {
            throw (RetrievalException) result;
        }

        if (result instanceof RuntimeException) {
            throw (RuntimeException) result;
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
        MockUserRetrieval.result = result;
    }
}
