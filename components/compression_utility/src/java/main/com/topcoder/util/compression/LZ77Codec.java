/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.util.zip.Deflater;
import java.util.zip.Inflater;


/**
 * <p>
 * <code>Codec</code> implementation that provides and implementation of the original Lempel-Ziv (LZ77) compression
 * algorithm.
 * </p>
 *
 * <p>
 * LZ77 may be used freely; it is not patented like some other Lempel-Ziv variants. However, note that this original
 * variant does not achieve the same compression available from other algorithms (including that provided by the
 * <code>java.util.zip</code> package). It is pure Java code, and may not compress as quickly as other
 * implementations.
 * </p>
 *
 * <p>
 * Note that the <code>Deflater</code>s and <code>Inflater</code>s produced by this implementation are not thread-safe.
 * </p>
 *
 * @author srowen
 * @version 2.0
 *
 * @see <a href="http://www.free2code.net/tutorials/other/20/LZ77.php">LZ77 tutorial</a>
 * @since 1.0
 */
public class LZ77Codec implements Codec {
    /** Maximum size of lookback window used during encoding / decoding. */
    static final int WINDOW_SIZE = 255;

    /**
     * <p>
     * Returns a new <code>LZ77Encoder</code> object.
     * </p>
     *
     * @return a new <code>LZ77Encoder</code> object
     */
    public Deflater createDeflater() {
        return new LZ77Encoder();
    }

    /**
     * <p>
     * Returns a new <code>LZ77Decoder</code> object.
     * </p>
     *
     * @return a new <code>LZ77Decoder</code> object
     */
    public Inflater createInflater() {
        return new LZ77Decoder();
    }
}








