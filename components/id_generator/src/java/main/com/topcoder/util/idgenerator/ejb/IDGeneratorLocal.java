/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator.ejb;

import com.topcoder.util.idgenerator.IDGenerationException;

import java.math.BigInteger;

import javax.ejb.EJBLocalObject;


/**
 * Local component interface to the IDGeneratorBean stateless session bean.
 *
 * @author srowen, iggy36, gua
 * @version 3.0
 */
public interface IDGeneratorLocal extends EJBLocalObject {
    /**
     * <p>
     * Returns the next ID in the named ID sequence.
     * </p>
     *
     * @param idName the name of the ID sequence
     * @return the next ID in the named ID sequence
     *
     * @throws IDGenerationException if an error occurs while generating the ID (for example, error while connecting to
     *         the database)
     */
    public long getNextID(String idName) throws IDGenerationException;

    /**
     * <p>
     * Returns the next BigInteger ID in the named ID sequence.
     * </p>
     *
     * @param idName the name of the ID sequence
     *
     * @return next value that would be returned by getNextID() as a BigInteger
     *
     * @throws IDGenerationException if an error occurs while generating the ID (for example, error while connecting to
     *         the database)
     */
    public BigInteger getNextBigID(String idName) throws IDGenerationException;
}
