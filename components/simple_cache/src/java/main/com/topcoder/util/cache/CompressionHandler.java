/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

/**
 * This is a contract for all compression handlers used in deflating and inflating entries.
 * In order for this class to be thread-safe the two methods should have no modifiable state associated.
 * <p>In order for SimpleCache to be able to create instances of this interface each implementing class
 * should have constructor of the following format:<br>
 * <code>
 * public void NameOfClass(Map map) {...}
 * </code><br>
 * Parameter map will contain information from configuration file. Each key will be name of property and
 * each value will be either String or List. String is used for single-value properties and List is
 * used for properties with multiple values.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public interface CompressionHandler {

    /**
     * Compresses the value passed in and returns the compressed value as a byte array.
     * If value is null then this method should return empty array.
     *
     * @param  value object to be compressed.
     * @return the compressed object as byte array.
     * @throws CompressionException if there are any issues with compressing the input value.
     * @throws TypeNotMatchedException if the type of the value object is unknown to the handler.
     */
    byte[] compress(Object value) throws CompressionException;

    /**
     * Decompresses the value passed in and returns the decompressed value.
     * If compressedValue is null or an empty array then this method should return null.
     *
     * @param  compressedValue the byte array to decompress.
     * @return decompressed (original) value.
     * @throws CompressionException if there are any issues with decompressing the compressed input value.
     */
    Object decompress(byte[] compressedValue) throws CompressionException;
}
