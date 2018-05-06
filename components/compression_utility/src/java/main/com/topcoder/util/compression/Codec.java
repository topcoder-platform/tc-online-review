/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.util.zip.Deflater;
import java.util.zip.Inflater;


/**
 * <p>
 * Implementations of <code>Codec</code> encapsulate <code>Deflater</code> / <code>Inflater</code> pairs, which
 * together represent compression and decompression functionality for a particular algorithm.
 * </p>
 *
 * <p>
 * Note that the <code>Deflater</code>s and <code>Inflater</code>s produced by implementations may not be thread-safe.
 * </p>
 *
 * @author srowen
 * @version 2.0
 *
 * @since 1.0
 */
public interface Codec {
    /**
     * <p>
     * Returns <code>Deflater</code> that encapsulates compression functionality for this <code>Codec</code>'s
     * algorithm.
     * </p>
     *
     * @return <code>Deflater</code> that encapsulates compression functionality for this <code>Codec</code>'s
     *         algorithm.
     */
    Deflater createDeflater();

    /**
     * <p>
     * Returns <code>Inflater</code> that encapsulates decompression functionality for this <code>Codec</code>'s
     * algorithm.
     * </p>
     *
     * @return <code>Inflater</code> that encapsulates decompression functionality for this <code>Codec</code>'s
     *         algorithm.
     */
    Inflater createInflater();
}








