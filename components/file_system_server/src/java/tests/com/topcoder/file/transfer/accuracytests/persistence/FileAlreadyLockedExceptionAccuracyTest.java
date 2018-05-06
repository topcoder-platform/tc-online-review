/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.persistence;

import com.topcoder.file.transfer.persistence.FileAlreadyLockedException;
import com.topcoder.file.transfer.persistence.FilePersistenceException;

import junit.framework.TestCase;

/**
 * <p>
 * Tests for <code>FileAlreadyLockedException</code>.
 * </p>
 * @author mayday
 * @version 1.1
 *
 */
public class FileAlreadyLockedExceptionAccuracyTest extends TestCase {

    /**
     * Test method for {@link com.topcoder.file.transfer.persistence.FileAlreadyLockedException
     * #FileAlreadyLockedException(java.lang.String)}.
     *
     * Test with string message and test whether it can be obtained later.
     */
    public void testFileAlreadyLockedExceptionString() {
        FileAlreadyLockedException e = new FileAlreadyLockedException("message");
        assertNotNull("should not be null", e);
        assertEquals("should be exception", e.getMessage(), "message");
        assertTrue("should be instance of FilePersistenceException", e instanceof FilePersistenceException);
    }

    /**
     * Test method for {@link com.topcoder.file.transfer.persistence.FileAlreadyLockedException
     * #FileAlreadyLockedException(java.lang.String, java.lang.Exception)}.
     *
     * Test with message as well as cause and test whether they can be obtained later.
     */
    public void testFileAlreadyLockedExceptionStringException() {
        Exception cause = new Exception("cause");
        FileAlreadyLockedException e = new FileAlreadyLockedException("message", cause);
        assertSame("should be same", cause, e.getCause());
        assertEquals("should be message", e.getMessage(), "message");
        assertTrue("should be instance of FilePersistenceException", e instanceof FilePersistenceException);
    }

}
