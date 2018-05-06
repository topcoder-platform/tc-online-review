/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.util.zip.Deflater;


/**
 * <p>
 * This subclass of <code>Deflater</code> overrides its methods to implement the Lempel-Ziv (LZ77) compression
 * algorithm.
 * </p>
 *
 * <p>
 * Note that this class is not thread-safe.
 * </p>
 *
 * @author srowen
 * @version 2.0
 *
 * @see <a href="http://www.free2code.net/tutorials/other/20/LZ77.php">LZ77 tutorial</a>
 * @see <a href="http://www.stanford.edu/class/ee398a/resources/ziv:77-SDC.pdf">Universal Algorithm for Sequential Data
 *      Compression</a>
 * @since 1.0
 */
public class LZ77Encoder extends Deflater {
    /** Maximum size of lookback window used during encoding. */
    private static final int WINDOW_SIZE = LZ77Codec.WINDOW_SIZE;
    private byte[] inputBytes;
    private int inputOffset;
    private int inputPosition;
    private int inputMax;
    private int totalIn;
    private int totalOut;

    /**
     * <p>
     * Sets input data for compression. This should be called whenever {@link #needsInput()} returns <code>true</code>
     * indicating that more input data is required.
     * </p>
     *
     * @param inputBytes the input data bytes
     *
     * @throws NullPointerException if input is <code>null</code>
     */
    public void setInput(final byte[] inputBytes) {
        if (inputBytes == null) {
            throw new NullPointerException("Input bytes cannot be null");
        }

        setInput(inputBytes, 0, inputBytes.length);
    }

    /**
     * <p>
     * Sets input data for compression. This should be called whenever {@link #needsInput()} returns <code>true</code>
     * indicating that more input data is required.
     * </p>
     *
     * <p>
     * Note that if <code>inputOffset + length</code> exceeds the length of the input array, no exception will be
     * thrown, but only bytes through the end of the array will be processed.
     * </p>
     *
     * @param bytes the input data bytes
     * @param offset the start offset of the data
     * @param length the length of the data
     *
     * @throws NullPointerException if input is <code>null</code>
     * @throws ArrayIndexOutOfBoundsException if <code>inputOffset</code> or <code>length</code> is negative, or their
     *         sum exceeds the length of the array
     */
    public void setInput(final byte[] bytes, final int offset, final int length) {
        if (bytes == null) {
            throw new NullPointerException("Input bytes cannot be null");
        }

        if ((offset < 0) || (length < 0) || ((offset + length) > bytes.length)) {
            throw new ArrayIndexOutOfBoundsException("Invalid offset/length");
        }

        if (length > 0) {
            inputBytes = bytes;
            inputOffset = offset;
            inputPosition = offset;

            if ((offset + length) > bytes.length) {
                inputMax = bytes.length;
                totalIn += (bytes.length - offset);
            } else {
                inputMax = offset + length;
                totalIn += length;
            }
        } else {
            clearInput();
        }
    }

    /**
     * <p>
     * Fills specified buffer with compressed data. Returns actual number of bytes of compressed data. A return value
     * of 0 indicates that {@link #needsInput()} should be called in order to determine if more input data is
     * required.
     * </p>
     *
     * @param outputBytes the buffer for the compressed data
     *
     * @return the actual number of bytes of compressed data
     *
     * @throws NullPointerException if output is <code>null</code>
     */
    public int deflate(final byte[] outputBytes) {
        if (outputBytes == null) {
            throw new NullPointerException("Output bytes cannot be null");
        }

        return deflate(outputBytes, 0, outputBytes.length);
    }

    /**
     * <p>
     * Fills specified buffer with compressed data. Returns actual number of bytes of compressed data. A return value
     * of 0 indicates that {@link #needsInput()} should be called in order to determine if more input data is
     * required.
     * </p>
     *
     * <p>
     * Note that if <code>outputOffset + outputLength</code> exceeds the length of the output array, no exception will
     * be thrown, but only bytes through the end of the array will be written with compressed data.
     * </p>
     *
     * @param outputBytes the buffer for the compressed data
     * @param outputOffset the start offset of the data
     * @param outputLength the maximum number of bytes of compressed data
     *
     * @return the actual number of bytes of compressed data
     *
     * @throws NullPointerException if output is <code>null</code>
     * @throws ArrayIndexOutOfBoundsException if <code>outputOffset</code> or <code>outputLength</code> is negative, or
     *         their sum exceeds the length of the array
     */
    public int deflate(final byte[] outputBytes, final int outputOffset, final int outputLength) {
        if (outputBytes == null) {
            throw new NullPointerException("Output bytes cannot be null");
        }

        if ((outputOffset < 0) || (outputLength < 0) || ((outputOffset + outputLength) > outputBytes.length)) {
            throw new ArrayIndexOutOfBoundsException("Invalid offset/length");
        }

        if (needsInput()) {
            return 0;
        }

        int outputPosition = outputOffset;

        // "- 2" is to make sure that there is always room to write 3
        // bytes in the output, since every step of the encoding produces
        // 3 bytes
        final int outputMax = (((outputOffset + outputLength) > outputBytes.length) ? outputBytes.length
                                                                                    : (outputOffset + outputLength))
            - 2;

        // "inputMax - 1" since we always want to leave one byte at
        // the end to output in the last triplet of bytes
        final int kMax = inputMax - 1;

        // Use local reference for better performance
        final byte[] inputBytes = this.inputBytes;

        int bytesEncoded = 0;

        while ((outputPosition < outputMax) && (inputPosition < inputMax)) {
            /*
               // Why "+ 1"? When there is a full WINDOW_SIZE byte behind
               // the input position, a match could start at the start of
               // the window, meaning matchOffset below would be
               // WINDOW_SIZE. But if WINDOW_SIZE = 256, then this results
               // in the byte value 0 being encoded. So, we prevent this by
               // not allowing a match starting at the very start of the window
               final int windowStart =
                       inputPosition - inputOffset >= WINDOW_SIZE
                       ? inputPosition - WINDOW_SIZE + 1
                       : inputOffset;
             */

            // The window size now is 255. So no need to "+ 1".
            final int windowStart = ((inputPosition - inputOffset) >= WINDOW_SIZE) ? (inputPosition - WINDOW_SIZE)
                                                                                   : inputOffset;

            int maximalMatchStart = 0;
            int maximalMatchLength = 0;

            final int jMax = inputPosition;

            // Look for longest match from start of window
            for (int i = windowStart, iMax = inputPosition; i < iMax; i++) {
                // shortcut for the common case:
                if ((inputPosition < kMax) && (inputBytes[i] == inputBytes[inputPosition])) {
                    // Find length of match from index i
                    int j = i + 1;

                    for (int k = inputPosition + 1; (j < jMax) && (k < kMax) && (inputBytes[j] == inputBytes[k]);
                            j++, k++)
                        ; // note: no loop body

                    // If there is any match,
                    if (j > i) {
                        // and it is the longest so far, record it
                        final int matchLength = j - i;

                        // ">=" so as to get *last* best match --
                        // a little better for the decoder
                        if (matchLength >= maximalMatchLength) {
                            maximalMatchLength = matchLength;
                            iMax = inputPosition - maximalMatchLength;
                            maximalMatchStart = i;
                        }
                    }
                }
            }

            if (maximalMatchLength > 0) {
                // Found a match.
                // Output offset of start of match, as a number of bytes
                // *back* from the current input position, not *forwards*
                // from the window start
                outputBytes[outputPosition] = (byte) (inputPosition - maximalMatchStart);
                outputBytes[outputPosition + 1] = (byte) maximalMatchLength;
                outputBytes[outputPosition + 2] = inputBytes[inputPosition + maximalMatchLength];
                inputPosition += (maximalMatchLength + 1);
            } else {
                // No match
                outputBytes[outputPosition] = 0;
                outputBytes[outputPosition + 1] = 0;
                outputBytes[outputPosition + 2] = inputBytes[inputPosition];
                inputPosition++;
            }

            outputPosition += 3;
            bytesEncoded += 3;
        }

        totalOut += bytesEncoded;

        if (inputPosition >= inputMax) {
            clearInput();
        }

        return bytesEncoded;
    }

    /**
     * <p>
     * Returns <code>true</code> iff there are no input bytes left to process.
     * </p>
     *
     * @return <code>true</code> iff there are no input bytes left to process
     */
    public boolean needsInput() {
        return inputBytes == null;
    }

    /**
     * <p>
     * Returns <code>true</code> iff there are no input bytes left to process.
     * </p>
     *
     * @return <code>true</code> iff there are no input bytes left to process
     */
    public boolean finished() {
        return inputBytes == null;
    }

    /**
     * <p>
     * Ends encoding process and discards any unprocessed input data.
     * </p>
     */
    public void end() {
        clearInput();
    }

    /**
     * <p>
     * Resets this encoder to its initial state. This also means that any unprocessed input data is discarded.
     * </p>
     */
    public void reset() {
        clearInput();
        totalIn = 0;
        totalOut = 0;
    }

    /**
     * <p>
     * Lets the encoder know that no more input data will be provided. Currently this does nothing.
     * </p>
     */
    public void finish() {
        // do nothing
    }

    /**
     * <p>
     * Returns the total number of bytes input so far.
     * </p>
     *
     * @return the total number of bytes input so far
     */
    public int getTotalIn() {
        return totalIn;
    }

    /**
     * <p>
     * Returns the total number of bytes output so far.
     * </p>
     *
     * @return the total number of bytes output so far
     */
    public int getTotalOut() {
        return totalOut;
    }

    private void clearInput() {
        inputBytes = null;
        inputOffset = 0;
        inputPosition = 0;
        inputMax = 0;
    }
}








