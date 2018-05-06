/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.util.zip.Deflater;
import java.util.zip.Inflater;


/**
 * <p>
 * Deflate <code>Codec</code> implementation that returns Java's <code>Deflater</code> and <code>Inflater</code>
 * objects; this implementation thus supports encoding and decoding of the ZIP and GZIP formats.
 * </p>
 *
 * @author ThinMan, visualage
 * @version 2.0
 */
public class DeflateCodec implements Codec {
    /**
     * <p>
     * Returns a new <code>Deflater</code> object.
     * </p>
     *
     * @return a new <code>Deflater</code> object
     */
    public Deflater createDeflater() {
        return new Deflater();
    }

    /**
     * <p>
     * Returns a new <code>Inflater</code> object.
     * </p>
     *
     * @return a new <code>Inflater</code> object
     */
    public Inflater createInflater() {
        return new Inflater();
    }
}








