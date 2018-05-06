/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.util.zip.Deflater;
import java.util.zip.Inflater;


/**
 * <p>
 * Default <code>Codec</code> implementation that returns Java's <code>Deflater</code> and <code>Inflater</code>
 * objects; this implementation thus supports encoding and decoding of the ZIP and GZIP formats.
 * </p>
 *
 * @author srowen, ThinMan, visualage
 * @version 2.0
 *
 * @since 1.0
 * @deprecated As of Compression Utility version 2.0, replaced by {@link DeflateCodec}.
 */
public class DefaultCodec implements Codec {
    /**
     * <p>
     * Returns a new <code>Deflater</code> object.
     * </p>
     *
     * @return a new <code>Deflater</code> object
     *
     * @deprecated As of Compression Utility version 2.0, replaced by {@link DeflateCodec#createDeflater()}.
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
     *
     * @deprecated As of Compression Utility version 2.0, replaced by {@link DeflateCodec#createInflater()}.
     */
    public Inflater createInflater() {
        return new Inflater();
    }
}








