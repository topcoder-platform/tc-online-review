/*
 * Copyright (c) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.cache;

import junit.framework.TestCase;

/**
 * Unit tests for memory leak issue of SimpleCache.
 * 
 * @author 80x86
 * @version 2.0
 */
public class MemLeakTest extends TestCase {
    /**
     * If memory leak issue exists, OutOfMemoryError will be thrown. Or nothing will happen.
     */
    public void testMemLeak() {
        try {
            int size = 20;
            SimpleCache[] sc = new SimpleCache[size];
            for (int i = 0; i < 10000; i++) {
                sc[i % size] = new SimpleCache();
                if (i % size == 0) {
                    System.gc();
                }
            }
        } catch (OutOfMemoryError e) {
            fail("should not be out of memory");
        }
    }
}
