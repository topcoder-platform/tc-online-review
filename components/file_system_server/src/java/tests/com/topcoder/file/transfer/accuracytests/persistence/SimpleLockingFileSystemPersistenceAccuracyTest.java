/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.persistence;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.topcoder.file.transfer.persistence.BytesIterator;
import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FilePersistenceException;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistence;

import junit.framework.TestCase;

/**
 * <p>
 * Tests for <code>SimpleLockingFileSystemPersistence</code>.
 * </p>
 * @author mayday
 * @version 1.1
 *
 */
public class SimpleLockingFileSystemPersistenceAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents the location to store test files.
     * </p>
     */
    private String fileLocation = "test_files/accuracy/files";

    /**
     * <p>
     * Represents the temporary file.
     * </p>
     */
    private String tempFile = "test_files/accuracy/files/test.tmp";

    /**
     * <p>
     * Represents an instance of SimpleLockingFileSystemPersistence.
     * </p>
     */
    private SimpleLockingFileSystemPersistence persistence;

    /**
     * <p>
     * Set up for each test.
     * </p>
     * @throws Exception Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        FilePersistence innerPersistence = new FileSystemPersistence();
        persistence = new SimpleLockingFileSystemPersistence(innerPersistence);
    }

    /**
     * <p>
     * Delete the temporary files.
     * </p>
     * @throws Exception Exception to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        new File(tempFile).delete();
        new File("test_files/accuracy/files/test.tmp.1").delete();
        persistence.dispose();
        super.tearDown();
    }

    /**
     * Test method for {@link com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistence
     * #SimpleLockingFileSystemPersistence(com.topcoder.file.transfer.persistence.FilePersistence)}.
     *
     * Test Ctor, and SimpleLockingFileSystemPersistence should be instantiated successfully.
     */
    public void testSimpleLockingFileSystemPersistence() {
        assertNotNull("SimpleLockingFileSystemPersistence should be instantiated successfully", persistence);
    }

    /**
     * Test method for {@link com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistence#
     * createFile(java.lang.String, java.lang.String)}.
     *
     * Test create file and check the result.
     */
    public void testCreateFile() throws Exception {
        String fileId1 = persistence.createFile(fileLocation, "file1.dat");
        String fileId2 = persistence.createFile(fileLocation, "file2.dat");
        assertTrue("should not be null/empty", fileId1 != null && fileId1.trim().length() > 0);
        assertFalse("The returned file id should be unique", fileId1.compareTo(fileId2) == 0);
    }

    /**
     * Test method for {@link com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistence#
     * appendBytes(java.lang.String, byte[])}.
     *
     * Test append bytes and check the result.
     */
    public void testAppendBytes() throws Exception {
        String fileId = persistence.createFile("test_files/accuracy/files", "test.tmp.1");
        byte[] datas = new byte[]{1, 2, 3};
        persistence.appendBytes(fileId, datas);
        persistence.closeFile(fileId);
        verifyDatas(datas, getFileBytes("test_files/accuracy/files/test.tmp.1"));
    }

    /**
     * <p>
     * Check the datas is same as expected.
     * </p>
     * @param expected expected data array
     * @param datas the data array to check
     */
    private void verifyDatas(byte[] expected, byte[] datas) {
        assertNotNull("Failed to get the correct data", datas);
        assertEquals("Failed to get the correct data", expected.length, datas.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Failed to get the correct data", expected[i], datas[i]);
        }
    }

    /**
     * <p>
     * Read file data.
     * </p>
     * @param fileName the file name
     * @return the data of file
     * @throws Exception Exception to JUnit
     */
    private byte[] getFileBytes(String fileName) throws Exception {
        File file = new File(fileName);
        int fileLen = (int) file.length();
        byte[] expected = new byte[fileLen];
        new DataInputStream(new FileInputStream(file)).read(expected, 0, fileLen);
        return expected;
    }

    /**
     * Test method for {@link com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistence#
     * closeFile(java.lang.String)}.
     *
     * Test close file and check if there are errors.
     */
    public void testCloseFile() throws Exception {
        String fileId = persistence.createFile("test_files/accuracy/files", "test.tmp.1");
        byte[] datas = new byte[]{1, 2, 3};
        persistence.appendBytes(fileId, datas);
        persistence.closeFile(fileId);

        try {
            persistence.appendBytes(fileId, datas);
            fail("there shouldn't exist output stream with id " + fileId);
        } catch (FilePersistenceException e) {
            // expected
        }
    }

    /**
     * Test method for {@link com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistence#
     * deleteFile(java.lang.String, java.lang.String)}.
     *
     * Test delete the file and check the result.
     */
    public void testDeleteFile() throws Exception {
        byte[] datas = new byte[]{1, 2, 3};
        String fileId = persistence.createFile(fileLocation, "test.tmp");
        persistence.appendBytes(fileId, datas);
        persistence.closeFile(fileId);
        assertTrue("the file should exist", new File(fileLocation, "test.tmp").exists());
        persistence.deleteFile(fileLocation, "test.tmp");
        assertFalse("Failed to delete file", new File(fileLocation, "test.tmp").exists());
    }

    /**
     * Test method for {@link com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistence#
     * getFileBytesIterator(java.lang.String, java.lang.String)}.
     *
     * Test get file bytes iterator and check the result.
     */
    public void testGetFileBytesIterator() throws Exception {
        BytesIterator itor = persistence.getFileBytesIterator(fileLocation, "file2.dat");
        assertNotNull("Failed to get the bytes iterator", itor);
        if (itor.hasNextBytes()) {
            byte[] datas = itor.nextBytes();
            assertNotNull("Failed to get the correct data", datas);
            byte[] expected = getFileBytes("test_files/accuracy/files/file2.dat");
            verifyDatas(expected, datas);
        }
    }

    /**
     * Test method for {@link com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistence#
     * getFileSize(java.lang.String, java.lang.String)}.
     *
     * Test get file size and check the result.
     */
    public void testGetFileSize() throws Exception {
        assertEquals("Failed to get the file size", new File("test_files/accuracy/files/file1.dat").length(),
            persistence.getFileSize(fileLocation, "file1.dat"));
        assertEquals("Failed to get the file size", new File("test_files/accuracy/files/file2.dat").length(),
            persistence.getFileSize(fileLocation, "file2.dat"));
    }

    /**
     * Test method for {@link com.topcoder.file.transfer.persistence.SimpleLockingFileSystemPersistence#
     * dispose()}.
     *
     * Test dispose and check the result.
     */
    public void testDispose() throws Exception {
        String fileId = persistence.createFile("test_files/accuracy/files", "file1.dat");
        persistence.dispose();
        try {
            persistence.appendBytes(fileId, new byte[]{1, 2});
            fail("all open output stream should closed");
        } catch (FilePersistenceException e) {
            // ok
        }
    }

}
