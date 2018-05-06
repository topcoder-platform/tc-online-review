/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.accuracytests;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.meterware.httpunit.UploadFileSpec;
import com.topcoder.file.transfer.FileSystemClient;
import com.topcoder.processor.ipserver.IPServer;

import com.topcoder.servlet.request.FileDoesNotExistException;
import com.topcoder.servlet.request.HttpRequestParser;
import com.topcoder.servlet.request.RemoteFileUpload;
import com.topcoder.servlet.request.RequestParser;
import com.topcoder.servlet.request.UnitTestHelper;
import com.topcoder.servlet.request.UploadedFile;


/**
 * <p>
 * Tests functionality of <code>LocalFileUpload</code> class.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public class RemoteFileUploadAccuracyTest extends FileUploadAccuracyTest {
	 /** Represents the temp dir for testing. */
    private static final String TEMP_DIR = "test_files/accuracytests/temp/";
    /** the IPServer instance used in tests. */
    private IPServer ipServer;

    /**
     * <p>
     * Sets up the test environment. The test instance is created. The configuration is loaded.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        AccuracyTestHelper.addConfig("accuracytests/RemoteFileUploadConfig.xml");
        AccuracyTestHelper.addConfig("accuracytests/MessageFactory.xml");
        AccuracyTestHelper.addConfig("accuracytests/IPServerManager.xml");
        AccuracyTestHelper.addConfig("accuracytests/DBConnectionFactoryImpl.xml");

        // create the server
        ipServer = UnitTestHelper.createServer();
        UnitTestHelper.startServer(ipServer);

        fileUpload = new RemoteFileUpload("RemoteFileUploadConfig");
    }

    /**
     * <p>
     * Tears down the test environment. The configuration is cleared.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        UnitTestHelper.stopServer(ipServer);
    }
    /**
     * <p>
     * Tests the accuracy of constructor <code>RemoteFileUpload(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_Accuracy() throws Exception {
        assertEquals("singleFileLimit should be properly loaded.", "" + SINGLE_FILE_LIMIT,
            AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload, "singleFileLimit").toString());
        assertEquals("totalFileLimit should be properly loaded.", "" + TOTAL_FILE_LIMIT,
            AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload, "totalFileLimit").toString());

        assertEquals("tempDir should be properly loaded.", TEMP_DIR,
            AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "tempDir"));
    }
    /**
     * <p>
     * Tests the accuracy of method <code>getTempDir()</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetTempDir_Accuracy() throws Exception {
        assertEquals("tempDir should be properly got.", TEMP_DIR, ((RemoteFileUpload) fileUpload).getTempDir());
    }
    /**
     * <p>
     * Tests the accuracy of method <code>getUploadedFile(String, boolean)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetUploadedFile_Accuracy() throws Exception {
    	Map files = new HashMap();
        File file0 = new File(DIR, "text.txt");
        File file1 = new File(DIR, "pic.jpg");
        
        files.put("form1", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file0), new UploadFileSpec(file1) }));

        Map parameters = new HashMap();

        ServletRequest request = AccuracyTestHelper.prepareRequest(files, parameters);
        RequestParser parser = new HttpRequestParser();

        UploadedFile[] result = fileUpload.uploadFiles(request, parser).getAllUploadedFiles();

        // get the uploaded file
        for (int i = 0; i < result.length; i++) {
            UploadedFile uploadedFile = fileUpload.getUploadedFile(result[i].getFileId(), true);
            AccuracyTestHelper.assertEquals("The uploaded file should be correct.", result[i].getInputStream(),
                uploadedFile.getInputStream());
        }
    }
    /**
     * <p>
     * Tests the accuracy of method <code>removeUploadedFile(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoveUploadedFile_Accuracy() throws Exception {
    	Map files = new HashMap();
        File file0 = new File(DIR, "text.txt");
        File file1 = new File(DIR, "pic.jpg");
        
        files.put("form1", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file0), new UploadFileSpec(file1) }));

        Map parameters = new HashMap();

        ServletRequest request = AccuracyTestHelper.prepareRequest(files, parameters);
        RequestParser parser = new HttpRequestParser();

        UploadedFile[] result = fileUpload.uploadFiles(request, parser).getAllUploadedFiles();

        // remove the uploaded file
        for (int i = 0; i < result.length; i++) {
            fileUpload.removeUploadedFile(result[i].getFileId());

            try {
                fileUpload.getUploadedFile(result[i].getFileId(), true);
                fail("The file should be removed.");
            } catch (FileDoesNotExistException e) {
                // good
            }
        }
    }
}
