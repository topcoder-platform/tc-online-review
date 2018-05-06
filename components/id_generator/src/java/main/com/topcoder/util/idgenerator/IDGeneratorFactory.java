/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * This class has two factory methods which produce implementation of IDGenerator for callers. Currently, there are two
 * kinds of implemtation for IDGenerator: IDGeneratorImpl and OracleSequenceGenerator.
 * </p>
 * 
 * <p>
 * This class stores references to the instances which it returns internally, so that the same instance is always
 * returned for the same sequence name.
 * </p>
 *
 * @author srowen, iggy36, gua
 * @version 3.0
 */
public class IDGeneratorFactory {
    /**
     * The HashMap used to store all the IDGenerator instance generated, the key is its IDName, so when requesting the
     * IDGenerator that  has been requested before, the stored one is returned.
     */
    private static Map generators = null;

    /** The class type array used for create IDGenerator instance by reflecting. */
    private static final Class[] CLASS_TYPE = new Class[] { String.class };

    /**
     * This class cannot be instantiated.
     */
    private IDGeneratorFactory() {
    }

    /**
     * Retrieves an implementation of IDGenerator for the named sequence, creating it if necessary. If an instance
     * already exists for the sequence then that instance is returned instead of a new object.
     *
     * @param idName name of desired ID sequence
     *
     * @return an instance of IDGenerator for the named sequence
     *
     * @throws IDGenerationException if an error occurs while retrieving ID sequence configuration (for example,
     *         database errors)
     * @throws NoSuchIDSequenceException if name is null, or no such ID sequence is configured in the database
     */
    public static IDGenerator getIDGenerator(String idName)
        throws IDGenerationException {
        if (idName == null) {
            throw new NoSuchIDSequenceException("The IDName is null.");
        }

        IDGenerator generator;

        synchronized (IDGeneratorFactory.class) {
            // query the HashMap first to see if it is cached
            if (generators == null) {
                generators = new HashMap();
            }

            generator = (IDGenerator) generators.get(idName);

            // create new instance if not exist
            if (generator == null) {
                generator = new IDGeneratorImpl(idName);
                generators.put(idName, generator);
            }
        }

        return generator;
    }

    /**
     * Creates an id generator instance.
     *
     * @param idName the id sequence name.
     *
     * @return the id generator instance created.
     *
     * @throws IDGenerationException if failed to create the id generator instance.
     */
    public static IDGenerator newIDGenerator(String idName)
        throws IDGenerationException {
        return new IDGeneratorImpl(idName);
    }

    /**
     * Creates an id generator instance.
     *
     * @param namespace the configuration namespace.
     * @param idName the id sequence name.
     *
     * @return the id generator instance created.
     *
     * @throws IDGenerationException if failed to create the id generator instance.
     */
    public static IDGenerator newIDGenerator(String namespace, String idName)
        throws IDGenerationException {
        return new IDGeneratorImpl(namespace, idName);
    }

    /**
     * Creates an id generator instance.
     *
     * @param namespace the configuration namespace.
     * @param idName the id sequence name.
     * @param blockSize the block size.
     *
     * @return the id generator instance created.
     *
     * @throws IDGenerationException DOCUMENT ME!
     */
    public static IDGenerator newIDGenerator(String namespace, String idName, int blockSize)
        throws IDGenerationException {
        return new IDGeneratorImpl(namespace, idName, blockSize);
    }

    /**
     * <p>
     * Retrieves a specific implementation of IDGenerator for the named sequence, creating it if necessary.&nbsp; The
     * desired implementation is specified by passing in a fully-qualified classname.&nbsp; The class must implement
     * the IDGenerator interface.&nbsp; If this parameter is null, then the default implementation will be
     * instantiated if a new generator needs to be created.
     * </p>
     * 
     * <p>
     * If an instance already exists for the sequence then that instance is returned instead of a new object, provided
     * it uses the requested implementation. There is also a possibility of an <em>implementation clash</em> which
     * occurs when two different calls to this method request generators with the same name but different requested
     * implementations.
     * </p>
     *
     * @param idName name of desired ID sequence
     * @param implClass class name of desired ID sequence
     *
     * @return an instance of IDGenerator with the given idName and implClass
     *
     * @throws IDGenerationException if an error occurs while retrieving ID sequence configuration (for example,
     *         database errors)
     * @throws ClassNotFoundException if implClass cannot be loaded
     * @throws NoSuchMethodException if error occurs while get constructor from the class.
     * @throws InstantiationException if there was a problem instantiaing implClass
     * @throws IllegalAccessException if there's a security problem instantiating implClass
     * @throws InvocationTargetException if error occurs while reflecting the class
     * @throws IllegalAccessException if error occurs while new instance for arguments reason
     * @throws IllegalStateException if a generator with the requested name already exists but uses a different
     *         implementation
     */
    public static IDGenerator getIDGenerator(String idName, String implClass)
        throws IDGenerationException, ClassNotFoundException, NoSuchMethodException, InstantiationException, 
            IllegalAccessException, InvocationTargetException {
        if (idName == null) {
            throw new NoSuchIDSequenceException("The idName is null.");
        }

        IDGenerator generator;

        synchronized (IDGeneratorFactory.class) {
            // query the HashMap first to see if it is cached
            if (generators == null) {
                generators = new HashMap();
            }

            generator = (IDGenerator) generators.get(idName);

            Class classType = Class.forName(implClass);

            // create new instance if not exist and save it.
            if (generator == null) {
                generator = createIDGenerator(idName, classType);
                generators.put(idName, generator);
            } else {
                // the existing generator uses different implementation
                if (!classType.isInstance(generator)) {
                    throw new IllegalStateException("The existing generator uses a different implementation");
                }
            }
        }

        return generator;
    }

    /**
     * <p>
     * Uses the constructor specified by the parameters argument to create a new instance of IDGenerator
     * implementation.
     * </p>
     *
     * @param idName the id generator name to create
     * @param classType the class of ID sequence implementation
     *
     * @return an instance of IDGenerator for the given idName and classType
     *
     * @throws NoSuchMethodException if error occurs while get constructor from the class.
     * @throws InstantiationException if there was a problem instantiaing implClass
     * @throws IllegalAccessException if there's a security problem instantiating implClass
     * @throws InvocationTargetException if error occurs while reflecting the class
     */
    private static IDGenerator createIDGenerator(String idName, Class classType)
        throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // get constructor with one string class type array parameter
        Constructor constructor = classType.getConstructor(CLASS_TYPE);

        // create IDGenerator using idName and implClassName
        return (IDGenerator) constructor.newInstance(new String[] { idName });
    }
}
