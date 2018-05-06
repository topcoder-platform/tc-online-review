/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * The Mapper class simply maintains a column name to Converter map of the
 * mapping to apply when the {@link CustomResultSet#remap(Mapper)} is called.
 * This class is simply a wrapper around a HashMap.
 * </p>
 * <p>
 * This class of version 1.1 is the same with that of version 1.0.
 * </p>
 * <p>
 * <strong>Changes in 2.0:</strong>
 * <ol>
 * <li>Specified generic parameters for generic types.</li>
 * <li>Updated {@link #getMap()} to make it return a stored map.</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class is mutable, and not thread-safe.
 * </p>
 *
 * @author argolite, aubergineanode, saarixx, WishingBone, justforplay,
 *         suhugo
 * @version 2.0
 * @since 1.0
 */
public class Mapper {

    /**
     * <p>
     * The mapping of column names to Converter instances. The names in the map
     * are all non-null String values (all lowercase) and the values in the map
     * are all non-null Converter instances.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameter for the collection type.</li>
     * </ol>
     * </p>
     */
    private Map<String, Converter> map;

    /**
     * Creates a new Mapper with an initially empty map.
     */
    public Mapper() {
        // do nothing.
    }

    /**
     * <p>
     * Creates a new Mapper from the given map.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameters for map parameter.</li>
     * </ol>
     * </p>
     *
     * @param map
     *            The map that defines the mapping of column values.
     */
    public Mapper(Map<String, Converter> map) {
        setMap(map);
    }

    /**
     * <p>
     * Get the mapping from String names to Converters. This method returns a
     * stored map directly instead of cloning it (this approach is used to
     * achieve better performance).
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameters for return type.</li>
     * <li>Updated to make it return a stored map.</li>
     * </ol>
     * </p>
     *
     * @return The mapping from column names to Converters
     */
    public Map<String, Converter> getMap() {
        return map;
    }

    /**
     * <p>
     * Sets the mapping to the given map. To be consistent with the previous
     * version, this method copies all pairs in the map for which the key is a
     * non-null String and the value is a non-null Converter. The {key, value}
     * pair is put into the map field using the lower case version of the string
     * key. Pairs in the argument map which do not meet the type restrictions
     * are simply ignored.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Specified generic parameters for map parameter.</li>
     * </ol>
     * </p>
     *
     * @param map
     *            The mapping to replace the current one in this mapper with
     */
    public void setMap(Map<String, Converter> map) {
        if (map == null) {
            this.map = null;
        } else {
            this.map = new HashMap<String, Converter>();
            for (Entry<String, Converter> entry : map.entrySet()) {
                String key = entry.getKey();
                Converter value = entry.getValue();
                this.map.put(key.toLowerCase(), value);
            }
        }
    }
}
