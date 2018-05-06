/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * An ObjectByte converter which uses serialization to obtain the byte array representation of an object
 * and which then uses the process of deserialization to convert the byte array back to an Object.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class SerializableObjectByteConverter implements ObjectByteConverter {

    /**
     * Serialize an object into a byte stream.
     * If value is null then returns empty array.
     *
     * @param  value object to convert to a byte array.
     * @return byte array representation of an object.
     * @throws IllegalArgumentException if the object is not of type Serializable or Externalizable.
     * @throws ObjectByteConversionException if the serialization process fails or we catch an I/O
     *         exception or any other serialization exception.
     */
    public byte[] getBytes(Object value) throws ObjectByteConversionException {
        // if value is null return empty array.
        if (value == null) {
            return new byte[0];
        }

        // check that value is Serializable.
        if (!(value instanceof Serializable)) {
            throw new IllegalArgumentException("'value' should implement interface Serializable.");
        }

        // try to serialize value. If we catch any exception then wrap it in ObjectByteConversionException.
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = null;
        try {
            objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(value);
        } catch (Exception ex) {
            throw new ObjectByteConversionException("Can't serialize object.", ex);
        } finally {
            if (objectStream != null) {
                try {
                    objectStream.close();
                } catch (IOException ex) {
                    throw new ObjectByteConversionException("Can't serialize object.", ex);
                }
            }
        }
        return byteStream.toByteArray();
    }

    /**
     * This will deserialize stream representation of an object back into the actual Object representation.
     * If byteArray is null or an empty array then returns null.
     *
     * @param  byteArray byte array representation of an object.
     * @return the actual object represented by the array.
     * @throws ObjectByteConversionException if the deserialization process fails or we catch an I/O
     *         exception or any other serialization exception.
     */
    public Object getObject(byte[] byteArray) {
        // if byteArray is empty then return null.
        if (byteArray == null || byteArray.length == 0) {
            return null;
        }

        // try to deserialize object. If we catch any exception then wrap it in ObjectByteConversionException.
        ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
        Object res = null;
        ObjectInputStream objectStream = null;
        try {
            objectStream = new ObjectInputStream(byteStream);
            res = objectStream.readObject();
        } catch (Exception ex) {
            throw new ObjectByteConversionException("Could not deserialize object", ex);
        } finally {
            if (objectStream != null) {
                try {
                    objectStream.close();
                } catch (IOException ex) {
                    throw new ObjectByteConversionException("Can't serialize object.", ex);
                }
            }
        }
        return res;
    }
}
