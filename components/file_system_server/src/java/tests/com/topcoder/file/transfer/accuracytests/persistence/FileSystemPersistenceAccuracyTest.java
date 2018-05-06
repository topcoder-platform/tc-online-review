/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.persistence;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.topcoder.file.transfer.persistence.BytesIterator;
import com.topcoder.file.transfer.persistence.FilePersistenceException;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy Test for FileSystemPersistence class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class FileSystemPersistenceAccuracyTest extends TestCase {
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
     * Represents an instance of FileSystemPersistence.
     * </p>
     */
    private FileSystemPersistence persistence;

    /**
     * <p>
     * Set up for each test.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    protected void setUp() throws Exception {
        persistence = new FileSystemPersistence();
    }

    /**
     * <p>
     * Delete the temporary files.
     * </p>
     */
    protected void tearDown() throws Exception {
        new File(tempFile).delete();
        new File("test_files/accuracy/files/test.tmp.1").delete();
        persistence.dispose();
        super.tearDown();
    }

    /**
     * <p>
     * Tests ctor FileSystemPersistence(), an instance with iteratorByteSize is
     * 1024 is created.
     * </p>
     */
    public void testCtor1() {
        FileSystemPersistence persistence = new FileSystemPersistence();
        assertNotNull("Failed to create instance of FileSystemPersistence",
                persistence);
    }

    /**
     * <p>
     * Test ctor FileSystemPersistence(int iteratorByteSize), an instance with
     * the specified iteratorByteSize is created.
     * </p>
     */
    public void testCtor2() {
        FileSystemPersistence persistence = new FileSystemPersistence(1111);
        assertNotNull("Failed to create instance of FileSystemPersistence",
                persistence);
    }

    /**
     * <p>
     * Test createFile(String fileLocation, String persistenceFileName), the
     * file in the supplied location with the name should be created.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testCreateFile() throws Exception {
        String fileId1 = persistence.createFile(fileLocation, "file1.dat");
        String fileId2 = persistence.createFile(fileLocation, "file2.dat");
        assertFalse("The returned file id should be unique", fileId1
                .compareTo(fileId2) == 0);
    }

    /**
     * <p>
     * Test appendBytes(String fileCreationId, byte[] bytes), the data should
     * append to file.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testAppendBytes() throws Exception {
        String fileId = persistence.createFile("test_files/accuracy/files",
                "test.tmp.1");
        byte[] datas = new byte[] { 1, 2, 3 };
        persistence.appendBytes(fileId, datas);
        persistence.closeFile(fileId);
        verifyDatas(datas, getFileBytes("test_files/accuracy/files/test.tmp.1"));
    }

    /**
     * <p>
     * Test deleteFile(String fileLocation, String persistenceFileName), the
     * file should be delete.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testDeleteFile() throws Exception {
        byte[] datas = new byte[] { 1, 2, 3 };
        String fileId = persistence.createFile(fileLocation, "test.tmp");
        persistence.appendBytes(fileId, datas);
        persistence.closeFile(fileId);
        assertTrue("the file should exist", new File(fileLocation, "test.tmp")
                .exists());
        persistence.deleteFile(fileLocation, "test.tmp");
        assertFalse("Failed to delete file", new File(fileLocation, "test.tmp")
                .exists());
    }

    /**
     * <p>
     * Test getFileBytesIterator(String fileLocation, String
     * persistenceFileName),
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetFileBytesIterator() throws Exception {
        BytesIterator itor = persistence.getFileBytesIterator(fileLocation,
                "file2.dat");
        assertNotNull("Failed to get the bytes iterator", itor);
        if (itor.hasNextBytes()) {
            byte[] datas = itor.nextBytes();
            assertNotNull("Failed to get the correct data", datas);
            byte[] expected = getFileBytes("test_files/accuracy/files/file2.dat");
            verifyDatas(expected, datas);
        }
    }

    /**
     * <p>
     * Check the datas is same as expected.
     * </p>
     * 
     * @param expected
     *            expected data array
     * @param datas
     *            the data array to check
     */
    private void verifyDatas(byte[] expected, byte[] datas) {
        assertNotNull("Failed to get the correct data", datas);
        assertEquals("Failed to get the correct data", expected.length,
                datas.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Failed to get the correct data", expected[i],
                    datas[i]);
        }
    }

    /**
     * <p>
     * Test getFileSize(String fileLocation, String persistenceFileName), it
     * should return th file length of persistence.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetFileSize() throws Exception {
        assertEquals("Failed to get the file size", new File(
                "test_files/accuracy/files/file1.dat").length(), persistence
                .getFileSize(fileLocation, "file1.dat"));
        assertEquals("Failed to get the file size", new File(
                "test_files/accuracy/files/file2.dat").length(), persistence
                .getFileSize(fileLocation, "file2.dat"));
    }

    /**
     * <p>
     * Test dispose(), all output stream should be closed.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testDispose() throws Exception {
        String fileId = persistence.createFile("test_files/accuracy/files",
                "file1.dat");
        persistence.dispose();
        try {
            persistence.appendBytes(fileId, new byte[] { 1, 2 });
            fail("all open output stream should closed");
        } catch (FilePersistenceException e) {
            // ok
        }
    }

    /**
     * <p>
     * Read file data.
     * </p>
     * 
     * @param fileName
     *            the file name
     * @return the data of file
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    private byte[] getFileBytes(String fileName) throws Exception {
        File file = new File(fileName);
        int fileLen = (int) file.length();
        byte[] expected = new byte[fileLen];
        new DataInputStream(new FileInputStream(file)).read(expected, 0,
                fileLen);

        return expected;
    }
}