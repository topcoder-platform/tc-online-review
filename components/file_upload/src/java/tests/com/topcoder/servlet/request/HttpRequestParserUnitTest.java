/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.UploadFileSpec;

import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;


/**
 * <p>
 * Tests functionality and error cases of <code>HttpRequestParser</code> class.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class HttpRequestParserUnitTest extends TestCase {
    /** Represents the dir to store the testing files. */
    private static final String DIR = "test_files/files/";

    /** Represents the value of content type for testing. */
    private static final String CONTENT_TYPE = "multipart/form-data; boundary=--HttpUnit-part0-aSgQ2M";

    /** Represents the <code>ServletRunner</code> instance for testing. */
    private static ServletRunner runner = null;

    /**
     * Represents the url of the web server for testing. e.g. http://localhost:8080. The value can be arbitrary since
     * we do not do the real job in the web server.
     */
    private static final String URL = "http://localhost:8080/";

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
     * Prepares the <code>ServletRequest</code> instance for testing. It will has the input map values as the
     * parameters.
     * </p>
     *
     * @param files the files to set the parameter values into the request.
     * @param parameters the parameters to set the parameter values into the request.
     *
     * @return the <code>ServletRequest</code> instance.
     *
     * @throws Exception any exception to JUnit.
     */
    public static ServletRequest prepareRequest(Map files, Map parameters) throws Exception {
        // create the webRequest
        PostMethodWebRequest webRequest = new PostMethodWebRequest(URL);
        webRequest.setMimeEncoded(true);

        for (Iterator iter = files.entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();
            String name = (String) entry.getKey();
            List value = (List) entry.getValue();
            webRequest.setParameter(name, (UploadFileSpec[]) value.toArray(new UploadFileSpec[value.size()]));
        }

        for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
            Entry entry = (Entry) iter.next();
            String name = (String) entry.getKey();
            List value = (List) entry.getValue();
            webRequest.setParameter(name, (String[]) value.toArray(new String[value.size()]));
        }

        // run this webRequest
        if (runner == null) {
            runner = new ServletRunner();
        }

        ServletUnitClient client = runner.newClient();
        InvocationContext ic = client.newInvocation(webRequest);

        return new MockHttpServletRequest(UnitTestHelper.readContent(ic.getRequest().getInputStream()), CONTENT_TYPE);
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>HttpRequestParser()</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testHttpRequestParser_Accuracy() throws Exception {
        assertNotNull("HttpRequestParser should be properly created.", this.parser);
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when content of the file is hudge.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_HudgeFileAccuracy1() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "hudge.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file)}));

        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 10000);
        UnitTestHelper.assertEquals("The file content should be correct.",
            UnitTestHelper.readContent(new FileInputStream(file)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file1", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when content of the file is hudge.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_HudgeFileAccuracy2() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "hudge.zip");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file)}));

        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 10000);
        UnitTestHelper.assertEquals("The file content should be correct.",
            UnitTestHelper.readContent(new FileInputStream(file)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "application/octet-stream", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file1", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when there is a single file.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_SingleFileAccuracy() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "1.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file)}));

        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100);
        UnitTestHelper.assertEquals("The file content should be correct.",
            UnitTestHelper.readContent(new FileInputStream(file)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file1", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when there is a single paramter.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_SingleParameterAccuracy() throws Exception {
        Map files = new HashMap();

        Map parameters = new HashMap();
        parameters.put("name1", Arrays.asList(new String[] {"value1"}));
        parser.parseRequest(prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when there is a single file and a single
     * paramter.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_SingleFileSingleParameterAccuracy() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "1.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file)}));

        Map parameters = new HashMap();
        parameters.put("name1", Arrays.asList(new String[] {"value1"}));
        parser.parseRequest(prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100);
        UnitTestHelper.assertEquals("The file content should be correct.",
            UnitTestHelper.readContent(new FileInputStream(file)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file1", parser.getFormFileName());
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
    public void testParseRequest_MultiFilesAccuracy() throws Exception {
        Map files = new HashMap();
        File file1 = new File(DIR, "1.txt");
        File file2 = new File(DIR, "10.txt");
        File file3 = new File(DIR, "100.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file1), new UploadFileSpec(file2)}));
        files.put("file2", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file3)}));

        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100);
        UnitTestHelper.assertEquals("The file content should be correct.",
            UnitTestHelper.readContent(new FileInputStream(file3)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file2", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file3, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100);
        UnitTestHelper.assertEquals("The file content should be correct.",
            UnitTestHelper.readContent(new FileInputStream(file1)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file1", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file1, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100);
        UnitTestHelper.assertEquals("The file content should be correct.",
            UnitTestHelper.readContent(new FileInputStream(file2)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file1", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file2, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when there is multi paramters.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_MultiParametersAccuracy() throws Exception {
        Map files = new HashMap();

        Map parameters = new HashMap();
        parameters.put("name1", Arrays.asList(new String[] {"value1", "value2"}));
        parameters.put("name2", Arrays.asList(new String[] {"value1"}));
        parser.parseRequest(prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when there is multi files and multi
     * paramters.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_MultiFilesMultiParametersAccuracy() throws Exception {
        Map files = new HashMap();
        File file1 = new File(DIR, "1.txt");
        File file2 = new File(DIR, "10.txt");
        File file3 = new File(DIR, "100.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file1), new UploadFileSpec(file2)}));
        files.put("file2", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file3)}));

        Map parameters = new HashMap();
        parameters.put("name1", Arrays.asList(new String[] {"value1", "value2"}));
        parameters.put("name2", Arrays.asList(new String[] {"value1"}));
        parser.parseRequest(prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100);
        UnitTestHelper.assertEquals("The file content should be correct.",
            UnitTestHelper.readContent(new FileInputStream(file3)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file2", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file3, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100);
        UnitTestHelper.assertEquals("The file content should be correct.",
            UnitTestHelper.readContent(new FileInputStream(file1)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file1", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file1, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        output = new ByteArrayOutputStream();
        parser.writeNextFile(output, 100);
        UnitTestHelper.assertEquals("The file content should be correct.",
            UnitTestHelper.readContent(new FileInputStream(file2)), output.toByteArray());

        // check the contentType, formFileName, RemoteFileName
        assertEquals("The contentType should be correct.", "text/plain", parser.getContentType());
        assertEquals("The formFileName should be correct.", "file1", parser.getFormFileName());
        assertEquals("The RemoteFileName should be correct.", file2, new File(DIR, parser.getRemoteFileName()));

        // check the file
        assertEquals("The hasNextFile should be correct.", false, parser.hasNextFile());

        // check the parameters
        assertEquals("The parameters map should be correct.", parameters, parser.getParameters());
    }

    /**
     * <p>
     * Tests whether the IllegalStateException is thrown properly.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testIllegalStateException_Accuracy1() throws Exception {
        try {
            parser.hasNextFile();
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests whether the IllegalStateException is thrown properly.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testIllegalStateException_Accuracy2() throws Exception {
        try {
            parser.writeNextFile(new ByteArrayOutputStream(), 100);
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests whether the IllegalStateException is thrown properly.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testIllegalStateException_Accuracy3() throws Exception {
        Map files = new HashMap();
        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));

        try {
            parser.writeNextFile(new ByteArrayOutputStream(), 100);
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests whether the IllegalStateException is thrown properly.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testIllegalStateException_Accuracy4() throws Exception {
        Map files = new HashMap();
        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));
        parser.hasNextFile();

        try {
            parser.writeNextFile(new ByteArrayOutputStream(), 100);
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests whether the IllegalStateException is thrown properly.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testIllegalStateException_Accuracy5() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "1.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file)}));

        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));
        parser.hasNextFile();

        try {
            parser.hasNextFile();
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests whether the IllegalStateException is thrown properly.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testIllegalStateException_Accuracy6() throws Exception {
        Map files = new HashMap();
        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));
        parser.hasNextFile();

        try {
            parser.hasNextFile();
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests whether the IllegalStateException is thrown properly.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testIllegalStateException_Accuracy7() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "1.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file)}));

        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));
        parser.hasNextFile();
        parser.writeNextFile(new ByteArrayOutputStream(), 100);

        try {
            parser.writeNextFile(new ByteArrayOutputStream(), 100);
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when the file size is too large,
     * FileSizeLimitExceededException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_FileSizeExceed() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "hudge.txt");
        files.put("file1", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file)}));

        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));

        // check the file
        assertEquals("The hasNextFile should be correct.", true, parser.hasNextFile());

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            parser.writeNextFile(output, 100);
            fail("FileSizeLimitExceededException should be thrown.");
        } catch (FileSizeLimitExceededException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when there is one parameter name is empty,
     * RequestParsingException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_EmptyParamterName() throws Exception {
        Map files = new HashMap();

        Map parameters = new HashMap();
        parameters.put(" ", Arrays.asList(new String[] {"value1"}));
        parser.parseRequest(prepareRequest(files, parameters));

        try {
            parser.hasNextFile();
            fail("RequestParsingException should be thrown.");
        } catch (RequestParsingException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the functionality of the <code>HttpRequestParser</code> class when there is one form file name is empty,
     * RequestParsingException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testParseRequest_EmptyFormFileName() throws Exception {
        Map files = new HashMap();
        File file = new File(DIR, "1.txt");
        files.put(" ", Arrays.asList(new UploadFileSpec[] {new UploadFileSpec(file)}));

        Map parameters = new HashMap();
        parser.parseRequest(prepareRequest(files, parameters));

        try {
            parser.hasNextFile();
            fail("RequestParsingException should be thrown.");
        } catch (RequestParsingException e) {
            // good
        }
    }
}
