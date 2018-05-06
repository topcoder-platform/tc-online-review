/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * <p>
 * Tests functionality and error cases of <code>LocalUploadedFile</code> class.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class LocalUploadedFileUnitTest extends TestCase {
    /** Represents the contentType for testing. */
    private static final String CONTENT_TYPE = "contentType";

    /** Represents the fileId for testing. */
    private static final String FILEID = "11";

    /** Represents the remoteFileName for testing. */
    private static final String REMOTE_FILE_NAME = "test.txt";

    /** Represents the data for testing. */
    private static final byte[] DATA = new byte[] {1, 0, 1};

    /** Represents the <code>File</code> instance used for testing. */
    private File file = null;

    /** Represents the <code>LocalUploadedFile</code> instance used for testing. */
    private LocalUploadedFile uploadedFile = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created.
     * </p>
     *
     * @throws Exception any exception to JUnit.
     */
    protected void setUp() throws Exception {
        file = new File("test_files/temp/11_test.txt");
        file.deleteOnExit();

        // write some content to the file
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(DATA);
        outputStream.flush();
        outputStream.close();

        uploadedFile = new LocalUploadedFile(file, CONTENT_TYPE);
    }

    /**
     * <p>
     * Tests the constructor <code>LocalUploadedFile(File, String)</code> when the given file is null,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testLocalUploadedFile_TwoArgNullFile() {
        try {
            new LocalUploadedFile(null, CONTENT_TYPE);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalUploadedFile(File, String)</code>.
     * </p>
     */
    public void testLocalUploadedFile_TwoArgAccuracy() {
        assertEquals("The contentType value should be set.", CONTENT_TYPE,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "contentType"));
        assertEquals("The fileId value should be set.", file.getName(),
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "fileId"));
        assertEquals("The file value should be set.", file,
            UnitTestHelper.getPrivateField(uploadedFile.getClass(), uploadedFile, "file"));
    }

    /**
     * <p>
     * Tests the constructor <code>LocalUploadedFile(File, String, String)</code> when the given file is null,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testLocalUploadedFile_ThreeArgNullFile() {
        try {
            new LocalUploadedFile(null, FILEID, CONTENT_TYPE);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalUploadedFile(File, String, String)</code> when the given fileId is null,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testLocalUploadedFile_ThreeArgNullFileId() {
        try {
            new LocalUploadedFile(file, null, CONTENT_TYPE);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalUploadedFile(File, String, String)</code> when the given fileId is empty,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testLocalUploadedFile_ThreeArgEmptyFileId() {
        try {
            new LocalUploadedFile(file, " ", CONTENT_TYPE);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalUploadedFile(File, String, String)</code>.
     * </p>
     */
    public void testLocalUploadedFile_ThreeArgAccuracy() {
        uploadedFile = new LocalUploadedFile(file, FILEID, CONTENT_TYPE);
        assertEquals("The contentType value should be set.", CONTENT_TYPE,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "contentType"));
        assertEquals("The fileId value should be set.", FILEID,
            UnitTestHelper.getParentClassPrivateField(uploadedFile.getClass(), uploadedFile, "fileId"));
        assertEquals("The file value should be set.", file,
            UnitTestHelper.getPrivateField(uploadedFile.getClass(), uploadedFile, "file"));
    }

    /**
     * <p>
     * Tests the method <code>getRemoteFileName()</code> when the file does not exist, FileDoesNotExistException is
     * expected.
     * </p>
     */
    public void testGetRemoteFileName_FileNotExist() {
        try {
            file.delete();
            uploadedFile.getRemoteFileName();
            fail("FileDoesNotExistException should be thrown.");
        } catch (FileDoesNotExistException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getRemoteFileName()</code>.
     * </p>
     *
     * @throws Exception any Exception to JUnit.
     */
    public void testGetRemoteFileName_Accuracy() throws Exception {
        assertEquals("The remoteFileName value should be got properly.", REMOTE_FILE_NAME,
            uploadedFile.getRemoteFileName());
    }

    /**
     * <p>
     * Tests the method <code>getSize()</code> when the file does not exist, FileDoesNotExistException is expected.
     * </p>
     */
    public void testGetSize_FileNotExist() {
        try {
            file.delete();
            uploadedFile.getSize();
            fail("FileDoesNotExistException should be thrown.");
        } catch (FileDoesNotExistException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getSize()</code>.
     * </p>
     *
     * @throws Exception any Exception to JUnit.
     */
    public void testGetSize_Accuracy() throws Exception {
        assertEquals("The size value should be got properly.", file.length(), uploadedFile.getSize());
    }

    /**
     * <p>
     * Tests the method <code>getInputStream()</code> when the file does not exist, FileDoesNotExistException is
     * expected.
     * </p>
     *
     * @throws Exception any Exception to JUnit.
     */
    public void testGetInputStream_FileNotExist() throws Exception {
        try {
            file.delete();
            uploadedFile.getInputStream();
            fail("FileDoesNotExistException should be thrown.");
        } catch (FileDoesNotExistException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getInputStream()</code>.
     * </p>
     *
     * @throws Exception any Exception to JUnit.
     */
    public void testGetInputStream_Accuracy() throws Exception {
        InputStream inputStream = uploadedFile.getInputStream();
        byte[] content = UnitTestHelper.readContent(inputStream);
        UnitTestHelper.assertEquals("The inputStream value should be got properly.", DATA, content);
        inputStream.close();
    }
}
