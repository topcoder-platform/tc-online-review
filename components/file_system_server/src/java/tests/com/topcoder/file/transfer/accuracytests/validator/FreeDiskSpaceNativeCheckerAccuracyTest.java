/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.validator;

import com.topcoder.file.transfer.accuracytests.AccuracyTestHelper;
import com.topcoder.file.transfer.validator.FreeDiskSpaceNativeChecker;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy Test for FreeDiskSpaceNativeChecker class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class FreeDiskSpaceNativeCheckerAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents an instance of FreeDiskSpaceNativeChecker;
     * </p>
     */
    private FreeDiskSpaceNativeChecker checker;

    /**
     * <p>
     * Create an instance of FreeDiskSpaceNativeChecker.
     * </p>
     */
    protected void setUp() throws Exception {
        super.setUp();
        checker = new FreeDiskSpaceNativeChecker("test_files/accuracy");
    }

    /**
     * <p>
     * Test freeDiskSpaceExceedsSize(long fileSize), when the fileSize is large
     * enough (larger than the disk space), false is expected.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testFreeDiskSpaceExceedsSize_Exceeded() throws Exception {
        assertFalse("False is expected when file is exceed free space", checker
                .freeDiskSpaceExceedsSize(AccuracyTestHelper.MAX_FREE_DISKSPACE + 1));
    }

    /**
     * <p>
     * Test freeDiskSpaceExceedsSize(long fileSize), when the fileSize is large
     * enough (larger than the disk space), false is expected.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testFreeDiskSpaceExceedsSize_Free() throws Exception {
        assertTrue("False is expected when file is exceed free space", checker
                .freeDiskSpaceExceedsSize(AccuracyTestHelper.MAX_FREE_DISKSPACE));
    }
}