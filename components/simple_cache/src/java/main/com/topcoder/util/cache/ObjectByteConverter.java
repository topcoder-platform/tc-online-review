/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

/**
 * This is a general contract for a utility which can convert any java object into a byte array (i.e. a stream)
 * and then given a byte array can obtain the object representation.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public interface ObjectByteConverter {

    /**
     * Given any java object this method will return a byte array representation of this object.
     * A null value is allowed and will return an empty array.
     *
     * @param  value object to convert to a byte array.
     * @return byte array representation of an object.
     * @throws ObjectByteConversionException if the conversion fails for any reason (i.e. a caught exception or
     *         simply unable to complete).
     */
    byte[] getBytes(Object value) throws ObjectByteConversionException;

    /**
     * Given a byte array this method will return the object representation of this array.
     *
     * @param  byteArray byte array representation of an object.
     * @return object representation of the input array.
     * @throws ObjectByteConversionException if the conversion fails for any reason (i.e. a caught exception or
     *         simply unable to complete).
     */
    Object getObject(byte[] byteArray) throws ObjectByteConversionException;
}
