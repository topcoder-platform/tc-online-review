/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

/**
 * <p>
 * Interface to the factory that will supply specifications. Currently there is
 * one implementation - ConfigManagerSpecificationFactory. There are no
 * restrictions on the constructors or the way the specifications are assembled
 * before or during the call.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> Implementations of this interface are not
 * required to be thread safe.
 * </p>
 *
 * @author codedoc, mgmg
 * @version 2.0
 */
public interface SpecificationFactory {
    /**
     * <p>
     * Obtains an ObjectSpecification from the Source, using a key and an
     * identifier. Typically the key will be the class name, and the identifier
     * would identify the specific configuration for this class. However, the
     * can be any String that the Source understands, and the identifier then
     * becomes optional. In other words, the source can use one or two keys to
     * uniquely identify an configuration for an object.
     * </p>
     *
     * @return An object specification
     * @param key
     *            key to the object specification
     * @param identifier
     *            additional identifier to an object specification
     * @throws UnknownReferenceException
     *             If the key/identifier combination is not recognized
     * @throws IllegalArgumentException
     *             if key is null or empty
     */
    public ObjectSpecification getObjectSpecification(String key, String identifier) throws UnknownReferenceException;
}
