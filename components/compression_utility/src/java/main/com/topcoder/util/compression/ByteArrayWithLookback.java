/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

/**
 * <p>
 * This class encapsulates a byte array holding data and an associated "lookback" window of previous data. It manages
 * the complexity of maintaining this lookback window correctly as input is added, and copying sections of previous
 * input to current input.
 * </p>
 *
 * @author srowen
 * @version 2.0
 *
 * @since 1.0
 */
final class ByteArrayWithLookback {
    private static final byte[] EMPTY_LOOKBACK = new byte[0];
    private final int lookbackSize;
    private byte[] lookback;
    private byte[] buffer;
    private int offset;
    private int length;

    /**
     * <p>
     * Creates a <code>ByteArrayWithLookback</code> with a lookback window of a given size.
     * </p>
     *
     * @param lookbackSize lookback window size in bytes
     */
    ByteArrayWithLookback(final int lookbackSize) {
        if (lookbackSize <= 0) {
            throw new IllegalArgumentException();
        }

        this.lookbackSize = lookbackSize;
        this.lookback = EMPTY_LOOKBACK;
    }

    /**
     * <p>
     * Sets the current input byte array. Note that {@link #saveLookback(int)} should be called first to update the
     * lookback window first, before this method sets a new input byte array.
     * </p>
     *
     * @param buffer new byte array
     * @param offset index of first byte of data in array
     * @param length number of bytes of data in array
     *
     * @see #saveLookback(int)
     */
    void setNextBuffer(final byte[] buffer, final int offset, final int length) {
        if (buffer == null) {
            throw new IllegalArgumentException();
        }

        if ((offset < 0) || ((offset + length) > buffer.length)) {
            throw new ArrayIndexOutOfBoundsException();
        }

        this.buffer = buffer;
        this.offset = offset;
        this.length = length;
    }

    /**
     * <p>
     * Copies bytes from behind the current input position to the current input position. This handles cases where some
     * or all of the data to copy comes from the lookback window and/or the current input buffer as well.
     * </p>
     *
     * <p>
     * Note that <code>lookbackOffset</code> is an offset backwards from <code>bufferOffset</code>, not forwards from
     * the start of the window.
     * </p>
     *
     * @param lookbackOffset offset backwards from <code>bufferOffset</code> to start copying from
     * @param bufferOffset offset into buffer to copy to
     * @param copyLength number of bytes to copy
     *
     * @throws ArrayIndexOutOfBoundsException if <code>bufferOffset</code> is before the input array's
     *         <code>offset</code>, or <code>bufferOffset + copyLength</code> exceeds the end of the input array, or
     *         <code>copyLength</code> exceeds <code>lookbackOffset</code>
     */
    void copyFromLookbackToBuffer(final int lookbackOffset, final int bufferOffset, final int copyLength) {
        if ((bufferOffset < offset) || ((bufferOffset + copyLength) > (offset + length))
                || (copyLength > lookbackOffset)) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (copyLength > 0) {
            if ((bufferOffset - lookbackOffset) >= offset) {
                // Copy entirely from buffer
                copy(buffer, bufferOffset - lookbackOffset, buffer, bufferOffset, copyLength);
            } else {
                final int availableInBuffer = bufferOffset - offset;

                if ((lookbackOffset - copyLength) < availableInBuffer) {
                    // Copy spans both lookback and current buffer
                    final int toCopyFromLookback = lookbackOffset - availableInBuffer;
                    final int toCopyFromBuffer = copyLength - toCopyFromLookback;
                    copy(lookback, lookback.length - toCopyFromLookback, buffer, bufferOffset, toCopyFromLookback);
                    copy(buffer, offset, buffer, bufferOffset + toCopyFromLookback, toCopyFromBuffer);
                } else {
                    // Copy comes entirely from lookback
                    copy(lookback, lookback.length - (lookbackOffset - availableInBuffer), buffer, bufferOffset,
                        copyLength);
                }
            }
        }
    }

    /**
     * <p>
     * Updates the lookback window to hold the most recent bytes of data, and clears the input buffer. This handles the
     * cases where some of the saved data also comes from the current lookback window. Also, the entire input buffer
     * is not necessarily saved (since not necessarily all of it was processed); the <code>bufferLengthToSave</code>
     * parameter controls how many bytes to save.
     * </p>
     *
     * @param bufferLengthToSave number of bytes of input to save, at most
     */
    void saveLookback(int bufferLengthToSave) {
        // if there is any current buffer to save
        if (buffer != null) {
            // can't save more than all of the buffer
            if (bufferLengthToSave > length) {
                bufferLengthToSave = length;
            }

            // if there is more than enough data in the buffer
            if (bufferLengthToSave > lookbackSize) {
                // no need to also save from the current lookback;
                // keep part of the current buffer as lookback
                lookback = new byte[lookbackSize];
                copy(buffer, (offset + bufferLengthToSave) - lookbackSize, lookback, 0, lookback.length);
            } else {
                if ((lookback.length + bufferLengthToSave) > lookbackSize) {
                    final byte[] newLookback = new byte[lookbackSize];
                    final int toCopyFromLookback = (bufferLengthToSave > lookbackSize) ? 0
                                                                                       : (lookbackSize
                        - bufferLengthToSave);

                    if (toCopyFromLookback > 0) {
                        // copy some of lookback and all of current buffer
                        // into new lookback
                        copy(lookback, lookback.length - toCopyFromLookback, newLookback, 0, toCopyFromLookback);
                        copy(buffer, offset, newLookback, toCopyFromLookback, bufferLengthToSave);
                    } else {
                        copy(buffer, (offset + bufferLengthToSave) - lookbackSize, newLookback, 0, lookbackSize);
                    }

                    lookback = newLookback;
                } else {
                    // copy all of lookback and current buffer into
                    // new lookback
                    final byte[] newLookback = new byte[lookback.length + bufferLengthToSave];
                    copy(lookback, 0, newLookback, 0, lookback.length);
                    copy(buffer, offset, newLookback, lookback.length, bufferLengthToSave);
                    lookback = newLookback;
                }
            }

            buffer = null;
            offset = 0;
            length = 0;
        }

        // else nothing to do -- but hold on to current lookback, in case
    }

    /**
     * <p>
     * Clears all internal data structures, both lookback and input buffer.
     * </p>
     */
    void clear() {
        buffer = null;
        lookback = EMPTY_LOOKBACK;
        offset = 0;
        length = 0;
    }

    private static void copy(final byte[] src, final int srcPosition, final byte[] dest, final int destPosition,
        final int length) {
        if (length > 0) {
            if (length == 1) {
                // Don't bother with native call if just copying one byte;
                // this is the common case
                dest[destPosition] = src[srcPosition];
            } else {
                System.arraycopy(src, srcPosition, dest, destPosition, length);
            }
        }
    }
}








