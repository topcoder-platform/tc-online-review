/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import com.meterware.httpunit.UploadFileSpec;

import com.topcoder.processor.ipserver.IPServer;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;


/**
 * <p>
 * Demonstrates the usage of this component. The usage includes four parts.
 * </p>
 *
 * <p>
 * The first part shows the usage of creating FileUpload instances.
 * </p>
 *
 * <p>
 * The second part shows the usage of manipulating results after parsing the request.
 * </p>
 *
 * <p>
 * The third part shows the usage of performing file operations with file id.
 * </p>
 *
 * <p>
 * The fourth part shows the usage of parsing servlet request.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class DemoTest extends TestCase {
    /** Represents the namespace for MemoryFileUpload instance. */
    private static final String MEMORY_FILE_UPLOAD_NAMESPACE = "MemoryFileUpload";

    /** Represents the namespace for LocalFileUpload instance. */
    private static final String LOCAL_FILE_UPLOAD_NAMESPACE = "LocalFileUpload";

    /** Represents the namespace for RemoteFileUpload instance. */
    private static final String REMOTE_FILE_UPLOAD_NAMESPACE = "RemoteFileUpload";

    /** Represents the temp dir for testing. */
    private static final String TEMP_DIR = "test_files/temp/";

    /** Represents the dir to store the testing files. */
    private static final String DIR = "test_files/files/";

    /** Represents the IPServer instance used in tests. */
    private IPServer ipServer;

    /**
     * <p>
     * Sets up the test environment. The test instance is created. The configuration is loaded.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void setUp() throws Exception {
        UnitTestHelper.clearConfig();
        UnitTestHelper.addConfig("demoConfig.xml");
        UnitTestHelper.addConfig("MessageFactory.xml");
        UnitTestHelper.addConfig("IPServerManager.xml");
        UnitTestHelper.addConfig("DBConnectionFactoryImpl.xml");

        // create the server
        ipServer = UnitTestHelper.createServer();
        UnitTestHelper.startServer(ipServer);

        // clear the server files
        UnitTestHelper.clearServerFiles();
    }

    /**
     * <p>
     * Tears down the test environment. The configuration is cleared.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void tearDown() throws Exception {
        UnitTestHelper.clearConfig();
        UnitTestHelper.stopServer(ipServer);

        // clear the server files
        UnitTestHelper.clearServerFiles();

        // remove all the temp files
        File[] files = new File(TEMP_DIR).listFiles();

        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    /**
     * Show the usage of creating FileUpload instances.
     *
     * @throws Exception any exception to junit.
     */
    public void testCreateFileUploadUsage() throws Exception {
        // create instance for file upload to memory
        FileUpload upload = new MemoryFileUpload(MEMORY_FILE_UPLOAD_NAMESPACE);

        // create instance for file upload to a directory in local file system,
        // the directory can be specified dynamically
        upload = new LocalFileUpload(LOCAL_FILE_UPLOAD_NAMESPACE, TEMP_DIR);

        // create instance for file upload to remote file system
        upload = new RemoteFileUpload(REMOTE_FILE_UPLOAD_NAMESPACE);
    }

    /**
     * Show the usage of manipulating results after parsing the request.
     *
     * @throws Exception any exception to junit.
     */
    public void testManipulateResultUsage() throws Exception {
        // create the FileUpload instance
        FileUpload upload = new LocalFileUpload(LOCAL_FILE_UPLOAD_NAMESPACE, TEMP_DIR);

        // prepare the request with some uploaded files and parameters
        Map files = new HashMap();
        File file1 = new File(DIR, "1.txt");
        File file2 = new File(DIR, "10.txt");
        File file3 = new File(DIR, "100.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file1), new UploadFileSpec(file2)}));
        files.put("file2", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file3)}));

        Map parameters = new HashMap();
        parameters.put("name1", Arrays.asList(new String[] {"value1", "value2"}));
        parameters.put("name2", Arrays.asList(new String[] {"value1"}));

        ServletRequest request = HttpRequestParserUnitTest.prepareRequest(files, parameters);

        // parse the request, this will save the uploaded files
        // to local file system
        FileUploadResult result = upload.uploadFiles(request);

        // get all the parameter names
        String[] names = result.getParameterNames();

        for (int i = 0; i < names.length; i++) {
            System.out.println("parameter name: " + names[i]);
        }

        // get multiple values of the parameter
        String[] values = result.getParameterValues("name1");

        for (int i = 0; i < values.length; i++) {
            System.out.println("parameter values for name1: " + values[i]);
        }

        // get single values of the parameter
        String value = result.getParameter("name2");
        System.out.println("parameter value for name2: " + value);

        // get all the form file names
        names = result.getFormFileNames();

        for (int i = 0; i < names.length; i++) {
            System.out.println("form file name: " + names[i]);
        }

        // get multiple uploaded files with the form file name
        UploadedFile[] uploadedFiles = result.getUploadedFiles("file1");

        for (int i = 0; i < uploadedFiles.length; i++) {
            System.out.println("remote file name for file1: " + uploadedFiles[i].getRemoteFileName());
        }

        // get single uploaded file with the form file name
        UploadedFile file = result.getUploadedFile("file2");
        System.out.println("remote file name for file2: " + file.getRemoteFileName());

        // get all the uploaded files
        uploadedFiles = result.getAllUploadedFiles();

        for (int i = 0; i < uploadedFiles.length; i++) {
            System.out.println("remote file names: " + uploadedFiles[i].getRemoteFileName());
        }
    }

    /**
     * Show the usage of performing file operations with file id.
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUploadUsage() throws Exception {
        // create the FileUpload instance
        FileUpload upload = new RemoteFileUpload(REMOTE_FILE_UPLOAD_NAMESPACE);

        // prepare the request with some uploaded files and parameters
        Map files = new HashMap();
        File file1 = new File(DIR, "1.txt");
        File file2 = new File(DIR, "10.txt");
        File file3 = new File(DIR, "100.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file1), new UploadFileSpec(file2)}));
        files.put("file2", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file3)}));

        Map parameters = new HashMap();
        parameters.put("name1", Arrays.asList(new String[] {"value1", "value2"}));
        parameters.put("name2", Arrays.asList(new String[] {"value1"}));

        ServletRequest request = HttpRequestParserUnitTest.prepareRequest(files, parameters);

        // parse the request, this will save the uploaded files to file system server
        FileUploadResult result = upload.uploadFiles(request);

        // get the file id of an uploaded file from the result
        // and store it somewhere (e.g. session) for later use
        String fileId = result.getUploadedFile("file1").getFileId();

        // later, we can retrieve the uploaded file from the file system server
        // with that file id
        UploadedFile file = upload.getUploadedFile(fileId);

        // get the remote file name
        String remoteFileName = file.getRemoteFileName();
        System.out.println("The uploaded file's remote file name: " + remoteFileName);

        // get the file size
        long size = file.getSize();
        System.out.println("The uploaded file's size: " + size);

        // get the underlying input stream
        InputStream in = file.getInputStream();

        // close the input stream after use
        in.close();

        // remove the uploaded file from the file system server
        upload.removeUploadedFile(fileId);
    }

    /**
     * Show the usage of parsing servlet request.
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequestUsage() throws Exception {
        // Create the HttpRequestParser instance
        HttpRequestParser parser = new HttpRequestParser();

        // prepare the request with some uploaded files and parameters
        Map files = new HashMap();
        File file = new File(DIR, "1.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file)}));

        Map parameters = new HashMap();
        parameters.put("name1", Arrays.asList(new String[] {"value1"}));

        ServletRequest request = HttpRequestParserUnitTest.prepareRequest(files, parameters);

        // parses the request
        parser.parseRequest(request);

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100);
        System.out.println("The file content is :" + output.toString());
        output.close();

        // get the contentType, formFileName, RemoteFileName
        System.out.println("The file content type is :" + parser.getContentType());
        System.out.println("The file form file name is :" + parser.getFormFileName());
        System.out.println("The file remote file name is :" + parser.getRemoteFileName());

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // get the parameters
        parser.getParameters();
    }
}
