/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;
import com.topcoder.util.cache.Cache;

/**
 * <p>
 * CachFactory interface defines the contract to create the Cache instance. The Cache interface is defined
 * in the Simple Cache component. This interface defines a single method to return the created Cache.
 * </p>
 *
 * <p>
 * The created cache will be used by the AbstractAuthenticator and its subclasses if the caching is turned on.
 * The principal's id is stored as the key in cache, and the response to the principal is stored as the value.
 * </p>
 *
 * <p>
 * This interface expect user provide a configuration namespace using Java reflection technology to
 * construct itself.
 * </p>
 *
 * <p>All implementation of this interface are expected to thread safe.</p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public interface CacheFactory {
    /**
     * <p>
     * Create a Cache instance to return. Implementations are expected to provide
     * extra information about the sort of cache which is created.
     * </p>
     * @return the created Cache instance.
     */
    public Cache createCache();
}









