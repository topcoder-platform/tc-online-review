/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.validator.FreeDiskSpaceChecker;
import com.topcoder.file.transfer.validator.FreeDiskSpaceCheckerException;


/**
 * Mock implementation of <code>FreeDiskSpaceChecker</code> interface.  Used for testing purposes only.
 *
 * @author fairytale
 * @version 1.0
 */
public class MockFreeDiskSpaceChecker implements FreeDiskSpaceChecker {
    /**
     * Mock implementation.
     */
    public MockFreeDiskSpaceChecker() {
    }

    /**
     * Mock implementation.
     *
     * @param fileSize the file's size
     *
     * @return whether the disk is large enough.
     *
     * @throws FreeDiskSpaceCheckerException never.
     */
    public boolean freeDiskSpaceExceedsSize(long fileSize)
        throws FreeDiskSpaceCheckerException {
        return true;
    }
}
