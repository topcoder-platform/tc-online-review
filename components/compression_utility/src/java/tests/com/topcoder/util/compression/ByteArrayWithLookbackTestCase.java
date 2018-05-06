/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.compression;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>Tests functionality of the internal <code>ByteArrayWithLookback</code>
 * utility class.</p>
 *
 * @author  srowen
 * @version 2.0
 *
 * @since 1.0
 */
public class ByteArrayWithLookbackTestCase extends TestCase {

    public void testUsage() {
        // this class can't really be tested
        final ByteArrayWithLookback bawl = new ByteArrayWithLookback(10);
        final byte[] buffer = new byte[] {0,1,2,3,4,5,6,7,8,9};
        bawl.setNextBuffer(buffer, 1, 8); // only using 1 - 8
        bawl.saveLookback(5); // saves 1 - 5
        final byte[] buffer2 = new byte[5];
        bawl.setNextBuffer(buffer2, 0, 5);
        bawl.copyFromLookbackToBuffer(5, 1, 3); // copies 2 - 4

        assertTrue(Arrays.equals(buffer, new byte[] {0,1,2,3,4,5,6,7,8,9}));
        assertTrue(Arrays.equals(buffer2, new byte[] {0,2,3,4,0}));

        bawl.saveLookback(5); // pushes 0,2,3,4,0 into lookback
        final byte[] buffer3 = new byte[10];
        bawl.setNextBuffer(buffer3, 0, 10);
        bawl.copyFromLookbackToBuffer(10, 0, 10);

        assertTrue(Arrays.equals(buffer3, new byte[] {1,2,3,4,5,0,2,3,4,0}));

        bawl.saveLookback(11); // should not throw an error, but copy 10 bytes
        bawl.clear();

        bawl.setNextBuffer(new byte[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15},
                           0, 15);
        bawl.saveLookback(15);

        final byte[] buffer4 = new byte[10];
        bawl.setNextBuffer(buffer4, 0, 10);
        bawl.copyFromLookbackToBuffer(10, 0, 10);

        assertTrue(Arrays.equals(buffer4,
                                 new byte[] {6,7,8,9,10,11,12,13,14,15}));
    }

    public void testIllegalArgs() {
        try {
            new ByteArrayWithLookback(0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }

        final ByteArrayWithLookback bawl = new ByteArrayWithLookback(10);
        try {
            bawl.setNextBuffer(null, 0, 0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
        try {
            bawl.setNextBuffer(new byte[2], -1, 1);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            bawl.setNextBuffer(new byte[2], 0, 3);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }

        bawl.setNextBuffer(new byte[10], 1, 8);
        try {
            bawl.copyFromLookbackToBuffer(3, 0, 3);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            bawl.copyFromLookbackToBuffer(9, 1, 9);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
        try {
            bawl.copyFromLookbackToBuffer(5, 1, 6);
            fail("Should have thrown ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // good
        }
    }

    public static Test suite() {
        return new TestSuite(ByteArrayWithLookbackTestCase.class);
    }
}








