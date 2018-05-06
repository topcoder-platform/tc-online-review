/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.accuracytests;

import com.topcoder.servlet.request.LocalUploadedFile;
import com.topcoder.servlet.request.UploadedFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;


/**
 * <p>
 * Tests functionality of <code>LocalUploadedFile</code> class.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public class LocalUploadedFileAccuracyTest extends TestCase {
    /** Represents the contentType for testing. */
    private static final String CONTENT_TYPE = "contentType";

    /** Represents the fileId for testing. */
    private static final String FILEID = "lyt";

    /** Represents the remoteFileName for testing. */
    private static final String REMOTE_FILE_NAME = "test.txt";

    /** Represents the data for testing. */
    private static final byte[] DATA = new byte[] { 1, 0, 1, 0, 1, 0, 0, 1, 1, 'k', 'l', 'a' };

    /** Represents the <code>LocalUploadedFile</code> instance used for testing. */
    private UploadedFile uploadedFile = null;

    /** Represents the <code>File</code> instance used for testing. */
    private File file = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created.
     * </p>
     *
     * @throws Exception any exception to JUnit.
     */
    protected void setUp() throws Exception {
        file = new File("test_files/accuracytests/temp/" + FILEID + "_" + REMOTE_FILE_NAME);
        file.deleteOnExit();

        // write some content to the file
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(DATA);
        outputStream.flush();
        outputStream.close();

        uploadedFile = (LocalUploadedFile) AccuracyTestHelper.instanciateClassByPrivateConstructor(LocalUploadedFile.class,
                new Class[] { File.class, String.class }, new Object[] { file, CONTENT_TYPE });
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalUploadedFile(File, String)</code>.
     * </p>
     */
    public void testLocalUploadedFile_TwoArgAccuracy() {
        assertEquals("The contentType value should be set.", CONTENT_TYPE,
            AccuracyTestHelper.getPrivateField(uploadedFile.getClass().getSuperclass(), uploadedFile, "contentType"));
        assertEquals("The fileId value should be set.", file.getName(),
            AccuracyTestHelper.getPrivateField(uploadedFile.getClass().getSuperclass(), uploadedFile, "fileId"));
        assertEquals("The file value should be set.", file,
            AccuracyTestHelper.getPrivateField(uploadedFile.getClass(), uploadedFile, "file"));
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalUploadedFile(File, String, String)</code>.
     * </p>
     */
    public void testLocalUploadedFile_ThreeArgAccuracy()
        throws Exception {
        uploadedFile = (LocalUploadedFile) AccuracyTestHelper.instanciateClassByPrivateConstructor(LocalUploadedFile.class,
                new Class[] { File.class, String.class, String.class }, new Object[] { file, FILEID, CONTENT_TYPE });
        assertEquals("The contentType value should be set.", CONTENT_TYPE,
            AccuracyTestHelper.getPrivateField(uploadedFile.getClass().getSuperclass(), uploadedFile, "contentType"));
        assertEquals("The fileId value should be set.", FILEID,
            AccuracyTestHelper.getPrivateField(uploadedFile.getClass().getSuperclass(), uploadedFile, "fileId"));
        assertEquals("The file value should be set.", file,
            AccuracyTestHelper.getPrivateField(uploadedFile.getClass(), uploadedFile, "file"));
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
     * Tests the accuracy of method <code>getSize()</code>.
     * </p>
     *
     * @throws Exception any Exception to JUnit.
     */
    public void testGetSize_Accuracy() throws Exception {
        assertEquals("The size value should be got properly.", DATA.length, uploadedFile.getSize());
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
        byte[] content = AccuracyTestHelper.readContent(inputStream);
        AccuracyTestHelper.assertEquals("The inputStream value should be got properly.", DATA, content);
        inputStream.close();
    }
}
