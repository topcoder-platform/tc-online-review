/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Principal class represents the user entity of an application to be authenticated. It contains a
 * object type unique id,  which is used to uniquely identify the principal, so that if the cache
 * mechanism is turning on, the authentication  response can be retrieved from the cache directly
 * by the principal's id. It also holds a collection of attributes, which provide necessary
 * information for specific authentication implementation.
 * </p>
 *
 * <p>
 * This class is not thread-safe, but it is unlikely that multiple threads will share the same
 * principal instance.  And the application is ALWAYS encouraged to use separate Principal in
 * different threads.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class Principal {
    /**
     * <p>
     * Represents the unique id of the principal.
     * </p>
     */
    private final Object id;

    /**
     * <p>
     * Represents the attributes of the principal, which provide necessary information for
     * authentication.The key and value put into the map are all <code>String</code>.
     * </p>
     */
    private Map mappings = null;

    /**
     * <p>
     * Create a Principal instance with the unique principal id.
     * </p>
     *
     * @param id the unique principal id.
     * @throws NullPointerException if the id is null.
     */
    public Principal(Object id) {
        if (id == null) {
            throw new NullPointerException("id is null.");
        }
        this.id = id;
        mappings = new HashMap();
    }

    /**
     * <p>
     * Return the unique id of principal.
     * </p>
     *
     * @return the unique id of principal
     */
    public Object getId() {
        return id;
    }

    /**
     * <p>
     * Add a mapping into principal. If the key already exists, the old value will be overriden by
     * the new one,  otherwise, simply add the new mapping.
     * </p>
     *
     * @param key the key of mapping.
     * @param value the value of mapping.
     * @throws NullPointerException if the key or value is null.
     * @throws IllegalArgumentException if the key is empty string.
     */
    public void addMapping(String key, Object value) {
        if (key == null) {
            throw new NullPointerException("key is null.");
        }
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (key.trim().length() == 0) {
            throw new IllegalArgumentException("key is emtpy string.");
        }

        mappings.put(key, value);
    }

    /**
     * <p>
     * Remove the mapping of the key from the principal. If the key doesn't exist, nothing will
     * happen.
     * </p>
     *
     * @param key the key of mapping to remove.
     *
     * @return the value associated to the key, or null if not exist.
     *
     * @throws NullPointerException if the key is null.
     * @throws IllegalArgumentException if the key is emtpy string.
     */
    public Object removeMapping(String key) {
        if (key == null) {
            throw new NullPointerException("key is null.");
        }
        if (key.trim().length() == 0) {
            throw new IllegalArgumentException("key is empty string.");
        }
        return mappings.remove(key);
    }

    /**
     * <p>
     * Clear all mappings from the principal.
     * </p>
     */
    public void clearMappings() {
        mappings.clear();
    }

    /**
     * <p>
     * Return the value associated to the specified key in the principal, if the key doesn't exist,
     * null will be returned.
     * </p>
     *
     * @param key the key of mapping to get.
     * @return the value associated to the specified key in the principal, or null if not exist.
     *
     * @throws NullPointerException if the key is null.
     * @throws IllegalArgumentException if the key is emtpy string.
     */
    public Object getValue(String key) {
        if (key == null) {
            throw new NullPointerException("key is null.");
        }
        if (key.trim().length() == 0) {
            throw new IllegalArgumentException("key is empty string.");
        }
        return mappings.get(key);
    }

    /**
     * <p>
     * Return a copy of all the keys in the principal.
     * </p>
     *
     * @return all the keys in the principal.
     */
    public List getKeys() {
        return new ArrayList(mappings.keySet());
    }

    /**
     * <p>
     * Return true if the key exists in the principal, false otherwise.
     * </p>
     *
     * @param key the key of mapping to check.
     * @return true if the key exists in the principal, false otherwise
     *
     * @throws NullPointerException if the key is null.
     * @throws IllegalArgumentException if the key is emtpy string.
     */
    public boolean containsKey(String key) {
        if (key == null) {
            throw new NullPointerException("key is null.");
        }
        if (key.trim().length() == 0) {
            throw new IllegalArgumentException("key is empty string.");
        }
        return mappings.containsKey(key);
    }
}