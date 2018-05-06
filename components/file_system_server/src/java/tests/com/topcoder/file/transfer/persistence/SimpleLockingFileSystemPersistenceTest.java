/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

import java.io.File;

import junit.framework.TestCase;

/**
 * <p>
 * Unit tests for the <code>SimpleLockingFileSystemPersistence</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class SimpleLockingFileSystemPersistenceTest extends TestCase {

    /**
     * Represents a valid fileLocation.
     */
    private static final String FILE_LOCATION = "test_files/";

    /**
     * Represents the directory name used in tests.
     */
    private static final String DIR_NAME = "Directory";

    /**
     * Represents a file name.
     */
    private static final String FILE_NAME = "UnitTestSLFSP";

    /**
     * <p>
     * Represents the <code>SimpleLockingFileSystemPersistence</code> instance for test.
     * </p>
     */
    private SimpleLockingFileSystemPersistence persistence;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        FilePersistence innerPersistence = new FileSystemPersistence();
        persistence = new SimpleLockingFileSystemPersistence(innerPersistence);
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>SimpleLockingFileSystemPersistence</code>, expects the instance is
     * created properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorAccuracy() throws Exception {
        assertNotNull("Failed to create the SimpleLockingFileSystemPersistence instance.", persistence);
    }

    /**
     * <p>
     * Failure test for the constructor <code>SimpleLockingFileSystemPersistence</code> with the innerPersistence is
     * null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCtorWithInnerPersistenceNull() throws Exception {
        try {
            new SimpleLockingFileSystemPersistence(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Accuracy test for the <code>createFile</code> method, expects the file is created properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateFileAccuracy() throws Exception {
        String fileCreationId = persistence.createFile(FILE_LOCATION, FILE_NAME);
        assertTrue("fileCreationId shouldn't be null or empty", fileCreationId != null
                && fileCreationId.trim().length() != 0);
        persistence.closeFile(fileCreationId);
        persistence.deleteFile(FILE_LOCATION, FILE_NAME);
    }

    /**
     * <p>
     * Failure test for the <code>createFile</code> method with the fileLocation is null, NullPointerException is
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateFileWithFileLocationNull() throws Exception {
        try {
            persistence.createFile(null, FILE_NAME);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>createFile</code> method with the persistenceFileName is null,
     * NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateFileWithPersistenceFileNameNull() throws Exception {
        try {
            persistence.createFile(FILE_LOCATION, null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>createFile</code> method with the fileLocation is empty, IllegalArgumentException
     * is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateFileWithFileLocationEmpty() throws Exception {
        try {
            persistence.createFile(" ", FILE_NAME);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>createFile</code> method with the persistenceFileName is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateFileWithPersistenceFileNameEmpty() throws Exception {
        try {
            persistence.createFile(FILE_LOCATION, " ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>createFile</code> method with the directory already exists,
     * FileAlreadyLockedException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCreateFileWithFileAlreadyLockedException() throws Exception {
        try {
            persistence.createFile(FILE_LOCATION, DIR_NAME);
            fail("FileAlreadyLockedException should be thrown.");
        } catch (FileAlreadyLockedException e) {
            // expected
        }
    }

    /**
     * <p>
     * Accuracy test for the <code>appendBytes</code> method, expects the bytes are appended properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAppendBytesAccuracy() throws Exception {
        String writeString = "Hello World";
        String fileCreationId = persistence.createFile(FILE_LOCATION, FILE_NAME);
        persistence.appendBytes(fileCreationId, writeString.getBytes());
        persistence.closeFile(fileCreationId);
        File file = new File(FILE_LOCATION, FILE_NAME);
        assertTrue("the file should exist", file.exists());
        assertEquals("the current file size not correct", file.length(), writeString.getBytes().length);
        persistence.deleteFile(FILE_LOCATION, FILE_NAME);
    }

    /**
     * <p>
     * Failure test for the <code>appendBytes</code> method with the fileCreationId is null, NullPointerException is
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAppendBytesWithFileCreationIdNull() throws Exception {
        String writeString = "Hello World";

        try {
            persistence.appendBytes(null, writeString.getBytes());
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>appendBytes</code> method with the bytes is null, NullPointerException is
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAppendBytesWithBytesNull() throws Exception {
        try {
            persistence.appendBytes("fileCreationId", null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>appendBytes</code> method with the fileCreationId is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAppendBytesWithFileCreationIdEmpty() throws Exception {
        String writeString = "Hello World";

        try {
            persistence.appendBytes(" ", writeString.getBytes());
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>appendBytes</code> method with the file is not locked, FileNotYetLockedException
     * is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testAppendBytesWithFileNotLocked() throws Exception {
        String writeString = "Hello World";
        String fileCreationId = persistence.createFile(FILE_LOCATION, FILE_NAME);
        persistence.closeFile(fileCreationId);

        try {
            persistence.appendBytes(fileCreationId, writeString.getBytes());
            fail("FileNotYetLockedException should be thrown.");
        } catch (FileNotYetLockedException e) {
            // expected
        } finally {
            persistence.deleteFile(FILE_LOCATION, FILE_NAME);
        }
    }

    /**
     * <p>
     * Accuracy test for the <code>closeFile</code> method, expects the file is closed properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCloseFileAccuracy() throws Exception {
        String writeString = "Hello World";
        String fileCreationId = persistence.createFile(FILE_LOCATION, FILE_NAME);
        persistence.appendBytes(fileCreationId, writeString.getBytes());
        persistence.closeFile(fileCreationId);

        try {
            persistence.appendBytes(fileCreationId, writeString.getBytes());
            fail("there shouldn't exist output stream with id " + fileCreationId);
        } catch (FilePersistenceException e) {
            // expected
        } finally {
            persistence.deleteFile(FILE_LOCATION, FILE_NAME);
        }
    }

    /**
     * <p>
     * Failure test for the <code>closeFile</code> method with the fileCreationId is null, NullPointerException is
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCloseFileWithFileCreationIdNull() throws Exception {
        try {
            persistence.closeFile(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>closeFile</code> method with the fileCreationId is empty, IllegalArgumentException
     * is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCloseFileWithFileCreationIdEmpty() throws Exception {
        try {
            persistence.closeFile(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>closeFile</code> method with the file is not locked, FileNotYetLockedException is
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testCloseFileWithFileNotLocked() throws Exception {
        String fileCreationId = persistence.createFile(FILE_LOCATION, FILE_NAME);
        persistence.closeFile(fileCreationId);

        try {
            persistence.closeFile(fileCreationId);
            fail("FileNotYetLockedException should be thrown.");
        } catch (FileNotYetLockedException e) {
            // expected
        } finally {
            persistence.deleteFile(FILE_LOCATION, FILE_NAME);
        }
    }

    /**
     * <p>
     * Accuracy test for the <code>deleteFile</code> method, expects the file is deleted properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testDeleteFileAccuracy() throws Exception {
        String fileCreationId = persistence.createFile(FILE_LOCATION, FILE_NAME);
        persistence.closeFile(fileCreationId);
        persistence.deleteFile(FILE_LOCATION, FILE_NAME);
        assertFalse("the file should not exist", new File(FILE_LOCATION, FILE_NAME).exists());
    }

    /**
     * <p>
     * Failure test for the <code>deleteFile</code> method with the fileLocation is null, NullPointerException is
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testDeleteFileWithFileLocationNull() throws Exception {
        try {
            persistence.deleteFile(null, "persistenceFileName");
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>deleteFile</code> method with the persistenceFileName is null,
     * NullPointerException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testDeleteFileWithPersistenceFileNameNull() throws Exception {
        try {
            persistence.deleteFile("fileLocation", null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>deleteFile</code> method with the fileLocation is empty, IllegalArgumentException
     * is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testDeleteFileWithFileLocationEmpty() throws Exception {
        try {
            persistence.deleteFile(" ", "persistenceFileName");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>deleteFile</code> method with the persistenceFileName is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testDeleteFileWithPersistenceFileNameEmpty() throws Exception {
        try {
            persistence.deleteFile("fileLocation", " ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for the <code>deleteFile</code> method with the file is already locked,
     * FileAlreadyLockedException is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testDeleteFileWithFileAlreadyLocked() throws Exception {
        try {
            persistence.deleteFile(FILE_LOCATION, DIR_NAME);
            fail("FileAlreadyLockedException should be thrown.");
        } catch (FileAlreadyLockedException e) {
            // expected
        }
    }

    /**
     * <p>
     * Accuracy test for the <code>getFileBytesIterator</code> method, expects the file bytes iterator is returned
     * properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetFileBytesIteratorAccuracy() throws Exception {
        String writeString = "Hello World";
        String fileCreationId = persistence.createFile(FILE_LOCATION, FILE_NAME);
        persistence.appendBytes(fileCreationId, writeString.getBytes());
        persistence.closeFile(fileCreationId);
        assertTrue("the file should exist", new File(FILE_LOCATION, FILE_NAME).exists());
        BytesIterator bytesIterator = persistence.getFileBytesIterator(FILE_LOCATION, FILE_NAME);
        assertTrue("as already append bytes to it, hasNextBytes method should return true", bytesIterator
                .hasNextBytes());
        bytesIterator.dispose();
        persistence.deleteFile(FILE_LOCATION, FILE_NAME);
    }

    /**
     * Test <code>getFileBytesIterator(String, String)</code> method with the fileLocation is null,
     * NullPointerException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetFileBytesIteratorWithFileLocationNull() throws Exception {
        try {
            persistence.getFileBytesIterator(null, "valid");
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test <code>getFileBytesIterator(String, String)</code> method with the persistenceFileName is null,
     * NullPointerException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetFileBytesIteratorWithPersistenceFileNameNull() throws Exception {
        try {
            persistence.getFileBytesIterator("valid", null);
            fail("if persistenceName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test <code>getFileBytesIterator(String, String)</code> method with the fileLocation is empty,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetFileBytesIteratorWithFileLocationEmpty() throws Exception {
        try {
            persistence.getFileBytesIterator(" ", "valid");
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test <code>getFileBytesIterator(String, String)</code> method with the persistenceFileName is empty,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetFileBytesIteratorWithPersistenceFileNameEmpty() throws Exception {
        try {
            persistence.getFileBytesIterator("valid", " ");
            fail("if persistenceName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test <code>getFileBytesIterator(String, String)</code> method, if an exception occurs while performing the
     * operation, throw FilePersistenceException.
     */
    public void testGetFileBytesIteratorFilePersistenceException() {
        try {
            persistence.getFileBytesIterator(FILE_LOCATION, DIR_NAME);
            fail("if an exception occurs while performing the operation, throw FilePersistenceException");
        } catch (FilePersistenceException e) {
            // expected
        }
    }

    /**
     * Test <code>getFileSize(String, String)</code> method, expects the file size is returned properly .
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetFileSizeAccuracy() throws Exception {
        String fileCreationId = persistence.createFile(FILE_LOCATION, FILE_NAME);
        assertTrue("fileCreationId shouldn't be null or empty", fileCreationId != null
                && fileCreationId.trim().length() != 0);
        persistence.appendBytes(fileCreationId, new byte[10]);
        persistence.closeFile(fileCreationId);
        assertEquals("should be size of 10 bytes", persistence.getFileSize(FILE_LOCATION, FILE_NAME), 10);
        persistence.deleteFile(FILE_LOCATION, FILE_NAME);
    }

    /**
     * Test <code>getFileSize(String, String)</code> method with the fileLocation is null, NullPointerException is
     * expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetFileSizeWithFileLocationNull() throws Exception {
        try {
            persistence.getFileSize(null, "valid");
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test <code>getFileSize(String, String)</code> method with the persistenceName is null, NullPointerException
     * is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetFileSizeWithPersistenceNameNull() throws Exception {
        try {
            persistence.getFileSize("valid", null);
            fail("if persistenceName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test <code>getFileSize(String, String)</code> method with the fileLocation is empty, IllegalArgumentException
     * is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetFileSizeWithFileLocationEmpty() throws Exception {
        try {
            persistence.getFileSize(" ", "valid");
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test <code>getFileSize(String, String)</code> method with the persistenceName is empty,
     * IllegalArgumentException is expected.
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetFileSizeWithPersistenceNameEmpty() throws Exception {
        try {
            persistence.getFileSize("valid", " ");
            fail("if persistenceName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Accuracy test for the <code>dispose</code> method, expects the value is returned properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testDisposeAccuracy() throws Exception {
        String fileCreationId = persistence.createFile(FILE_LOCATION, FILE_NAME);
        persistence.dispose();
        try {
            persistence.appendBytes(fileCreationId, new byte[10]);
            fail("all open output stream should be closed");
        } catch (FilePersistenceException e) {
            // expected
        }
    }
}