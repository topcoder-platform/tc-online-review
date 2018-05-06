/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;


/**
 * <p>
 * This subclass of <code>Inflater</code> overrides its methods to implement decompression according to the Lempel-Ziv
 * (LZ77) algorithm.
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
 * @since 1.0
 */
public class LZ77Decoder extends Inflater {
    /** Maximum size of lookback window used during decoding. */
    private static final int WINDOW_SIZE = LZ77Codec.WINDOW_SIZE;
    private byte[] inputBytes;
    private int inputPosition;
    private int inputMax;
    private final ByteArrayWithLookback output;
    private int totalIn;
    private int totalOut;

    /**
     * <p>
     * Creates a new <code>LZ77Decoder</code>.
     * </p>
     */
    public LZ77Decoder() {
        output = new ByteArrayWithLookback(WINDOW_SIZE);
    }

    /**
     * <p>
     * Sets input data for decompression. This should be called whenever {@link #needsInput()} returns
     * <code>true</code> indicating that more input data is required.
     * </p>
     *
     * <p>
     * Note that if <code>offset + length</code> exceeds the length of the input array, no exception will be thrown,
     * but only bytes through the end of the array will be processed.
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
     * Sets input data for decompression. This should be called whenever {@link #needsInput()} returns
     * <code>true</code> indicating that more input data is required.
     * </p>
     *
     * <p>
     * Note that if <code>offset + length</code> exceeds the length of the input array, no exception will be thrown,
     * but only bytes through the end of the array will be processed.
     * </p>
     *
     * @param bytes the input data bytes
     * @param offset the start offset of the data
     * @param length the length of the data
     *
     * @throws NullPointerException if input is <code>null</code>
     * @throws ArrayIndexOutOfBoundsException if <code>offset</code> or <code>length</code> is negative, or their sum
     *         exceeds the length of the array
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
            inputPosition = offset;

            if ((offset + length) > bytes.length) {
                inputMax = bytes.length;
                totalIn += (bytes.length - offset);
            } else {
                inputMax = offset + length;
                totalIn += length;
            }

            // Will throw an exception in inflate if inputMax - inputPosition
            // is not divisible by 3
        } else {
            clearInput();
        }
    }

    /**
     * <p>
     * Fills specified buffer with decompressed data. Returns actual number of bytes of decompressed data. A return
     * value of 0 indicates that {@link #needsInput()} should be called in order to determine if more input data is
     * required.
     * </p>
     *
     * <p>
     * Note that if <code>outputOffset + outputLength</code> exceeds the length of the output array, no exception will
     * be thrown, but only bytes through the end of the array will be written with decompressed data.
     * </p>
     *
     * <p>
     * Also note that the input array must have a length that is a multiple of 3.
     * </p>
     *
     * @param outputBytes the buffer for the decompressed data
     *
     * @return the actual number of bytes of decompressed data
     *
     * @throws NullPointerException if output is <code>null</code>
     * @throws DataFormatException if the array length is not a multiple of 3, or if any triplet of bytes contains a
     *         match offset and length whose sum exceeds the current output position (that is, it indicates a match
     *         that extends beyond the end of the current window)
     */
    public int inflate(final byte[] outputBytes) throws DataFormatException {
        if (outputBytes == null) {
            throw new NullPointerException("Output bytes cannot be null");
        }

        return inflate(outputBytes, 0, outputBytes.length);
    }

    /**
     * <p>
     * Fills specified buffer with decompressed data. Returns actual number of bytes of decompressed data. A return
     * value of 0 indicates that {@link #needsInput()} should be called in order to determine if more input data is
     * required.
     * </p>
     *
     * <p>
     * Note that if <code>outputOffset + outputLength</code> exceeds the length of the output array, no exception will
     * be thrown, but only bytes through the end of the array will be written with decompressed data.
     * </p>
     *
     * <p>
     * Also note that the number of bytes must be a multiple of 3.
     * </p>
     *
     * @param outputBytes the buffer for the decompressed data
     * @param outputOffset the start offset of the data
     * @param outputLength the maximum number of bytes of decompressed data
     *
     * @return the actual number of bytes of decompressed data
     *
     * @throws NullPointerException if output is <code>null</code>
     * @throws ArrayIndexOutOfBoundsException if <code>outputOffset</code> or <code>outputLength</code> is negative, or
     *         their sum exceeds the length of the array
     * @throws DataFormatException if <code>outputLength</code> is not a multiple of 3, or if any triplet of bytes
     *         contains a match offset and length whose sum exceeds the current output position (that is, it indicates
     *         a match that extends beyond the end of the current window)
     */
    public int inflate(final byte[] outputBytes, final int outputOffset, final int outputLength)
        throws DataFormatException {
        if (outputBytes == null) {
            throw new NullPointerException("Output bytes cannot be null");
        }

        if ((outputOffset < 0) || (outputLength < 0) || ((outputOffset + outputLength) > outputBytes.length)) {
            throw new ArrayIndexOutOfBoundsException("Invalid offset/length");
        }

        if (needsInput()) {
            return 0;
        }

        if (((inputMax - inputPosition) % 3) != 0) {
            throw new DataFormatException("Input length is not divisible by 3");
        }

        int outputPosition = outputOffset;
        final int outputMax;

        if ((outputOffset + outputLength) > outputBytes.length) {
            output.setNextBuffer(outputBytes, outputOffset, outputBytes.length - outputOffset);
            outputMax = outputBytes.length;
        } else {
            output.setNextBuffer(outputBytes, outputOffset, outputLength);
            outputMax = outputOffset + outputLength;
        }

        int bytesDecoded = 0;

        while ((inputPosition < inputMax) && (outputPosition < outputMax)) {
            // "& 0xFF" because byte should be treated as unsigned
            final int matchLength = inputBytes[inputPosition + 1] & 0xFF;

            if (matchLength > 0) {
                // Match in window; copy match to output
                if ((outputPosition + matchLength + 1) > outputMax) {
                    // Not enough room to copy decoded bytes, so stop
                    break;
                } else {
                    final int matchOffset = inputBytes[inputPosition] & 0xFF;

                    // Note that matchOffset is relative to current output
                    // position, not from start of lookback window
                    // Copy match to output
                    try {
                        output.copyFromLookbackToBuffer(matchOffset, outputPosition, matchLength);
                    } catch (ArrayIndexOutOfBoundsException aioobe) {
                        throw new DataFormatException("Invalid match offset/length");
                    }

                    outputPosition += matchLength;

                    // Copy last byte to output
                    outputBytes[outputPosition] = inputBytes[inputPosition + 2];
                    outputPosition++;

                    bytesDecoded += (matchLength + 1);
                }
            } else {
                // No match in window
                outputBytes[outputPosition] = inputBytes[inputPosition + 2];
                outputPosition++;
                bytesDecoded++;
            }

            inputPosition += 3;
        }

        if (inputPosition >= inputMax) {
            clearInput();
        }

        totalOut += bytesDecoded;

        // Remember that not all of the output array was actuall written
        // with output; this ensures that the unused parts are not saved
        // as part of the output
        output.saveLookback(bytesDecoded);

        return bytesDecoded;
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
     * Ends decoding process and discards any unprocessed input data.
     * </p>
     */
    public void end() {
        clearInput();
        output.clear();
    }

    /**
     * <p>
     * Resets this decoder to its initial state. This also means that any unprocessed input data is discarded.
     * </p>
     */
    public void reset() {
        clearInput();
        output.clear();
        totalIn = 0;
        totalOut = 0;
    }

    /**
     * <p>
     * Returns the number of bytes left in the input.
     * </p>
     *
     * @return number of bytes left in the input
     */
    public int getRemaining() {
        return (inputBytes == null) ? 0 : (inputMax - inputPosition);
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
        inputPosition = 0;
        inputMax = 0;
    }
}








