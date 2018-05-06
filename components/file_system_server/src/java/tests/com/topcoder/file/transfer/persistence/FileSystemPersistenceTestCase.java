/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.persistence;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test FileSystemPersistence for correctness.
 * @author FireIce
 * @version 1.0
 */
public class FileSystemPersistenceTestCase extends TestCase {

    /**
     * Represents a valid fileLocation.
     */
    private static final String VALID_FILELOCATION = "test_files/";

    /**
     * Represents the directory name used in tests.
     */
    private static final String DIRNAME = "Directory";

    /**
     * Represents a file name.
     */
    private static final String FILENAME = "UnitTestFSP";

    /**
     * Represents a readonly file name.
     */
    private static final String READONLYFILENAME = "ReadOnlyFile";

    /**
     * Represents the FileSystemPersistence instance used in tests.
     */
    private FilePersistence filePersistence;

    /**
     * Represents the ReadOnly file used in tests.
     */
    private File readOnlyFile;

    /**
     * <p>
     * Sets up the test environment. The test instances are created.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void setUp() throws Exception {
        filePersistence = new FileSystemPersistence();
        // create ReadOnly file
        readOnlyFile = new File(VALID_FILELOCATION, READONLYFILENAME);
        if (!readOnlyFile.exists()) {
            readOnlyFile.createNewFile();
            readOnlyFile.setReadOnly();
        } else if (readOnlyFile.canWrite()) {
            readOnlyFile.setReadOnly();
        }
    }

    /**
     * <p>
     * Cleans up the test environment. The test instances are disposed.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void tearDown() throws Exception {
        filePersistence = null;
        readOnlyFile.delete();
    }

    /**
     * Test empty arugment list constructor.
     */
    public void testCtor() {
        new FileSystemPersistence();
    }

    /**
     * Test implementation.
     */
    public void testImplementaion() {
        assertTrue("this class should implement the FilePersistence interface",
                new FileSystemPersistence() instanceof FilePersistence);
    }

    /**
     * Test <code>FileSystemPersistence(int)</code> constructor, if the int argument is not positive, throw
     * IllegalArgumentException.
     */
    public void testCtorIllegalArgumentException() {
        try {
            new FileSystemPersistence(0);
            fail("if the int argument is not positive, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new FileSystemPersistence(-1);
            fail("if the int argument is not positive, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>FileSystemPersistence(int)</code> constructor, if the int argument is positive, create successfully.
     */
    public void testCtorSuccess() {
        new FileSystemPersistence(10);
    }

    /**
     * Test <code>createFile(String, String)</code> method, if any argument is null, throw NullPointerException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testCreateFileNullPointerException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.createFile(null, "valid");
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            filePersistence.createFile("valid", null);
            fail("if persistenceName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>createFile(String, String)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testCreateFileIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.createFile(" ", "valid");
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            filePersistence.createFile("valid", " ");
            fail("if persistenceName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>createFile(String, String)</code> method, if an exception occurs while performing the operation,
     * throw FilePersistenceException.
     * @throws Exception
     *             if any other statement throw exception
     */
    public void testCreateFileFilePersistenceException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.createFile(VALID_FILELOCATION, DIRNAME);
            fail("if an exception occurs while performing the operation, throw FilePersistenceException");
        } catch (FilePersistenceException e) {
            // good
        }
        try {
            filePersistence.createFile(VALID_FILELOCATION, READONLYFILENAME);
            fail("if an exception occurs while performing the operation, throw FilePersistenceException");
        } catch (FilePersistenceException e) {
            // good
        }
    }

    /**
     * Test <code>createFile(String, String)</code> method, create file Sucessfully.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testCreateFileSuccess() throws Exception {
        assertNotNull("setup fails", filePersistence);
        String fileCreationId = filePersistence.createFile(VALID_FILELOCATION, FILENAME);
        assertTrue("fileCreationId shouldn't be null or empty", fileCreationId != null
                && fileCreationId.trim().length() != 0);
        filePersistence.closeFile(fileCreationId);
        filePersistence.deleteFile(VALID_FILELOCATION, FILENAME);
    }

    // --------------------------------------------------------------------------------------------------
    /**
     * Test <code>appendBytes(String, byte[])</code> method, if any argument is null, throw NullPointerException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testAppendBytesNullPointerException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.appendBytes(null, new byte[0]);
            fail("if fileCreationId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            filePersistence.appendBytes("valid", null);
            fail("if bytes is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>appendBytes(String, byte[])</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testAppendBytesIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.appendBytes(" ", new byte[0]);
            fail("if fileCreationId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>appendBytes(String, byte[])</code> method, if an exception occurs while performing the operation,
     * throw FilePersistenceException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testAppendBytesFilePersistenceException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.appendBytes("NotExist", new byte[10]);
            fail("if no open output stream associate with the fileCreationId, throw FilePersistenceException");
        } catch (FilePersistenceException e) {
            // good
        }
    }

    /**
     * Test <code>appendBytes(String, byte[])</code> method, create file Sucessfully.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testAppendBytesSuccess() throws Exception {
        assertNotNull("setup fails", filePersistence);
        String writeString = "Hello World";
        String fileCreationId = filePersistence.createFile(VALID_FILELOCATION, FILENAME);
        filePersistence.appendBytes(fileCreationId, writeString.getBytes());
        filePersistence.closeFile(fileCreationId);
        File file = new File(VALID_FILELOCATION, FILENAME);
        assertTrue("the file should exist", file.exists());
        assertEquals("the current file size not correct", file.length(), writeString.getBytes().length);
        filePersistence.deleteFile(VALID_FILELOCATION, FILENAME);
    }

    // --------------------------------------------------------------------------------------------------
    /**
     * Test <code>closeFile(String)</code> method, if the argument is null, throw NullPointerException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testCloseFileNullPointerException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.closeFile(null);
            fail("if fileCreationId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>closeFile(String)</code> method, if any string argument is empty, throw IllegalArgumentException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testCloseFileIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.closeFile(" ");
            fail("if fileCreationId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>closeFile(String)</code> method, if an exception occurs while performing the operation, throw
     * FilePersistenceException.
     */
    public void testCloseFileFilePersistenceException() {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.closeFile("NotExist");
            fail("if no open output stream associate with the fileCreationId, throw FilePersistenceException");
        } catch (FilePersistenceException e) {
            // good
        }
    }

    /**
     * Test <code>closeFile(String)</code> method, create file Sucessfully.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void tesCloseFileSuccess() throws Exception {
        assertNotNull("setup fails", filePersistence);
        String writeString = "Hello World";
        String fileCreationId = filePersistence.createFile(VALID_FILELOCATION, FILENAME);
        filePersistence.appendBytes(fileCreationId, writeString.getBytes());
        filePersistence.closeFile(fileCreationId);
        try {
            filePersistence.appendBytes(fileCreationId, writeString.getBytes());
            fail("there shouldn't exist output stream with id " + fileCreationId);
        } catch (FilePersistenceException e) {
            // good
        }
        filePersistence.deleteFile(VALID_FILELOCATION, FILENAME);
    }

    // ---------------------------------------------------------------------------------------------
    /**
     * Test <code>deleteFile(String, String)</code> method, if any argument is null, throw NullPointerException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testDeleteFileNullPointerException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.deleteFile(null, "valid");
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            filePersistence.deleteFile("valid", null);
            fail("if persistenceName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>deleteFile(String, String)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testDeleteFileIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.deleteFile(" ", "valid");
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            filePersistence.deleteFile("valid", " ");
            fail("if persistenceName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>deleteFile(String, String)</code> method, if an exception occurs while performing the operation,
     * throw FilePersistenceException.
     */
    public void testDeleteFileFilePersistenceException() {
        assertNotNull("setup fails", filePersistence);
        assertTrue("should be a Directory", new File(VALID_FILELOCATION, DIRNAME).isDirectory());
        try {
            filePersistence.deleteFile(VALID_FILELOCATION, DIRNAME);
            fail("if the file to delete exist and is a directory, throw FilePersistenceException");
        } catch (FilePersistenceException e) {
            // good
        }
    }

    /**
     * Test <code>deleteFile(String, String)</code> method, create file Sucessfully.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testDeleteFileSuccess() throws Exception {
        assertNotNull("setup fails", filePersistence);
        String writeString = "Hello World";
        String fileCreationId = filePersistence.createFile(VALID_FILELOCATION, FILENAME);
        filePersistence.appendBytes(fileCreationId, writeString.getBytes());
        filePersistence.closeFile(fileCreationId);
        assertTrue("the file should exist", new File(VALID_FILELOCATION, FILENAME).exists());
        filePersistence.deleteFile(VALID_FILELOCATION, FILENAME);
        assertFalse("the file should not exist", new File(VALID_FILELOCATION, FILENAME).exists());
    }

    // -----------------------------------------------------------------------------------------------------
    /**
     * Test <code>getFileBytesIterator(String, String)</code> method, if any argument is null, throw
     * NullPointerException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testGetFileBytesIteratorNullPointerException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.getFileBytesIterator(null, "valid");
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            filePersistence.getFileBytesIterator("valid", null);
            fail("if persistenceName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>getFileBytesIterator(String, String)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testGetFileBytesIteratorIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.getFileBytesIterator(" ", "valid");
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            filePersistence.getFileBytesIterator("valid", " ");
            fail("if persistenceName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>getFileBytesIterator(String, String)</code> method, if an exception occurs while performing the
     * operation, throw FilePersistenceException.
     */
    public void testGetFileBytesIteratorFilePersistenceException() {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.getFileBytesIterator(VALID_FILELOCATION, DIRNAME);
            fail("if an exception occurs while performing the operation, throw FilePersistenceException");
        } catch (FilePersistenceException e) {
            // good
        }
    }

    /**
     * Test <code>getFileBytesIterator(String, String)</code> method, create file Sucessfully.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testGetFileBytesIteratorSuccess() throws Exception {
        assertNotNull("setup fails", filePersistence);
        String writeString = "Hello World";
        String fileCreationId = filePersistence.createFile(VALID_FILELOCATION, FILENAME);
        filePersistence.appendBytes(fileCreationId, writeString.getBytes());
        filePersistence.closeFile(fileCreationId);
        assertTrue("the file should exist", new File(VALID_FILELOCATION, FILENAME).exists());
        BytesIterator bytesIterator = filePersistence.getFileBytesIterator(VALID_FILELOCATION, FILENAME);
        assertTrue("as already append bytes to it, hasNextBytes method should return true", bytesIterator
                .hasNextBytes());
        bytesIterator.dispose();
        filePersistence.deleteFile(VALID_FILELOCATION, FILENAME);

    }

    // ----------------------------------------------------------------------------------------------------
    /**
     * Test <code>getFileSize(String, String)</code> method, if any argument is null, throw NullPointerException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testGetFileSizeNullPointerException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.getFileSize(null, "valid");
            fail("if fileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            filePersistence.getFileSize("valid", null);
            fail("if persistenceName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>getFileSize(String, String)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testGetFileSizeIllegalArgumentException() throws Exception {
        assertNotNull("setup fails", filePersistence);
        try {
            filePersistence.getFileSize(" ", "valid");
            fail("if fileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            filePersistence.getFileSize("valid", " ");
            fail("if persistenceName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>getFileSize(String, String)</code> method, create file Sucessfully.
     * @throws Exception
     *             if any other exception occurs.
     */
    public void testGetFileSizeSuccess() throws Exception {
        assertNotNull("setup fails", filePersistence);
        String fileCreationId = filePersistence.createFile(VALID_FILELOCATION, FILENAME);
        assertTrue("fileCreationId shouldn't be null or empty", fileCreationId != null
                && fileCreationId.trim().length() != 0);
        filePersistence.appendBytes(fileCreationId, new byte[10]);
        filePersistence.closeFile(fileCreationId);
        assertEquals("should be size of 10 bytes", filePersistence.getFileSize(VALID_FILELOCATION, FILENAME), 10);
        filePersistence.deleteFile(VALID_FILELOCATION, FILENAME);
    }

    /**
     * Test <code>dispose()</code> method, successfully close all open output streams.
     * @throws Exception
     *             if any exception occurs.
     */
    public void testDisposeSuccess() throws Exception {
        assertNotNull("setup fails", filePersistence);
        String fileCreationId = filePersistence.createFile(VALID_FILELOCATION, FILENAME);
        filePersistence.dispose();
        try {
            filePersistence.appendBytes(fileCreationId, new byte[10]);
            fail("all open output stream should closed");
        } catch (FilePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(FileSystemPersistenceTestCase.class);
    }
}
