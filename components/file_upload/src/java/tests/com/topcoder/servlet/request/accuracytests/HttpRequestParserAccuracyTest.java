/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.accuracytests;

import com.meterware.httpunit.UploadFileSpec;

import com.topcoder.servlet.request.HttpRequestParser;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Tests accuracy of <code>HttpRequestParser</code> class.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public class HttpRequestParserAccuracyTest extends TestCase {
    /** Represents the dir to store the testing files. */
    private static final String DIR = "test_files/accuracytests/files/";

    /** Represents the <code>HttpRequestParser</code> instance used for testing. */
    private HttpRequestParser parser = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void setUp() throws Exception {
        parser = new HttpRequestParser();
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when the content type of the upload file is
     * text.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_Accuracy_Text() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "text.txt");
        files.put("FormFileName", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file) }));

        Map parameters = new HashMap();
        parser.parseRequest(AccuracyTestHelper.prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 10000);
        AccuracyTestHelper.assertEquals("The file content should be correct.",
            AccuracyTestHelper.readContent(new FileInputStream(file)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "FormFileName", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class  when the content type of the upload file is
     * jpeg.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_Accuracy_Jpeg() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "pic.jpg");
        files.put("FormFileName", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file) }));

        Map parameters = new HashMap();
        parser.parseRequest(AccuracyTestHelper.prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100000);
        AccuracyTestHelper.assertEquals("The file content should be correct.",
            AccuracyTestHelper.readContent(new FileInputStream(file)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "image/jpeg", parser.getContentType());
        assertEquals("The formFileName should be correct.", "FormFileName", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }
    
    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class  when the content type of the upload file is
     * jpeg.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_Accuracy_Empty() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "empty.txt");
        files.put("FormFileName", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file) }));

        Map parameters = new HashMap();
        parser.parseRequest(AccuracyTestHelper.prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100000);
        AccuracyTestHelper.assertEquals("The file content should be correct.",
            AccuracyTestHelper.readContent(new FileInputStream(file)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "FormFileName", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }
    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class  when the content type of the upload file is
     * jpeg.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_Accuracy_DOC() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "chn.doc");
        files.put("FormFileName", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file) }));

        Map parameters = new HashMap();
        parser.parseRequest(AccuracyTestHelper.prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100000);
        AccuracyTestHelper.assertEquals("The file content should be correct.",
            AccuracyTestHelper.readContent(new FileInputStream(file)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "FormFileName", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when there is multi files.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_Accuracy_MultiFiles()
        throws Exception {
        Map files = new HashMap();
        File file1 = new File(DIR, "text.txt");
        File file2 = new File(DIR, "pic.jpg");
        files.put("form1", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file1) }));
        files.put("form2", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file2) }));

        Map parameters = new HashMap();
        parser.parseRequest(AccuracyTestHelper.prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100000);
        AccuracyTestHelper.assertEquals("The file content should be correct.",
            AccuracyTestHelper.readContent(new FileInputStream(file2)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "image/jpeg", parser.getContentType());
        assertEquals("The formFileName should be correct.", "form2", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file2, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100000);
        AccuracyTestHelper.assertEquals("The file content should be correct.",
            AccuracyTestHelper.readContent(new FileInputStream(file1)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "form1", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file1, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests the accuracy of the <code>getParameters()</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetParameters_Accuracy1() throws Exception {
        Map files = new HashMap();
        Map parameters = new HashMap();
        parameters.put("form1", Arrays.asList(new String[] { "value1", "value2" }));
        parameters.put("form2", Arrays.asList(new String[] { "value1" }));
        parameters.put("form3",
            Arrays.asList(new String[] { "value1"}));
        parameters.put("form4",
            Arrays.asList(new String[] { "value1", "value2"}));
        parameters.put("form5",
            Arrays.asList(new String[] { "value1", "value2", "value3"}));
        parameters.put("form6",
            Arrays.asList(new String[] { "value1", "value2", "value3", "value4" }));
        parameters.put("form7",
            Arrays.asList(new String[] { "value1", "value2", "value3", "value4", "value5"}));
        parameters.put("form8",
            Arrays.asList(new String[] { "value1", "value2", "value3", "value4", "value5", "value6", }));
        parameters.put("form9",
            Arrays.asList(new String[] { "value1", "value2", "value3", "value4", "value5"}));
        parameters.put("form10",
            Arrays.asList(new String[] { "value1", "value2", "value3", "value4"}));

        parser.parseRequest(AccuracyTestHelper.prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests the accuracy of the <code>getParameters()</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetParameters_Accuracy2() throws Exception {
        Map files = new HashMap();
        File file1 = new File(DIR, "text.txt");
        File file2 = new File(DIR, "pic.jpg");
        files.put("file1", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file1) }));
        files.put("file2", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file2) }));

        Map parameters = new HashMap();
        parameters.put("form1", Arrays.asList(new String[] { "value1", "value2" }));
        parameters.put("form2", Arrays.asList(new String[] { "value1" }));
        parameters.put("form3",
            Arrays.asList(new String[] { "value1"}));
        parameters.put("form4",
            Arrays.asList(new String[] { "value1", "value2"}));
        parameters.put("form5",
            Arrays.asList(new String[] { "value1", "value2", "value3"}));
        parameters.put("form6",
            Arrays.asList(new String[] { "value1", "value2", "value3", "value4" }));
        parameters.put("form7",
            Arrays.asList(new String[] { "value1", "value2", "value3", "value4", "value5"}));
        parameters.put("form8",
            Arrays.asList(new String[] { "value1", "value2", "value3", "value4", "value5", "value6", }));
        parameters.put("form9",
            Arrays.asList(new String[] { "value1", "value2", "value3", "value4", "value5"}));
        parameters.put("form10",
            Arrays.asList(new String[] { "value1", "value2", "value3", "value4"}));

        parser.parseRequest(AccuracyTestHelper.prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100000);
        AccuracyTestHelper.assertEquals("The file content should be correct.",
            AccuracyTestHelper.readContent(new FileInputStream(file2)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "image/jpeg", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file2", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file2, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100000);
        AccuracyTestHelper.assertEquals("The file content should be correct.",
            AccuracyTestHelper.readContent(new FileInputStream(file1)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file1", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file1, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }
}
