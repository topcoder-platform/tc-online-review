/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import junit.framework.TestCase;


/**
 * <p>
 * Tests functionality and error cases of method uploadFiles(ServletRequest, RequestParser) of the FileUpload related
 * classes.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public abstract class BaseFileUploadUnitTest extends TestCase {
    /** Represents the total file size limit for testing. */
    protected static final long SINGLE_FILE_LIMIT = 100;

    /** Represents the total file size limit for testing. */
    protected static final long TOTAL_FILE_LIMIT = 200;

    /** Represents the dir to store the testing files. */
    protected static final String DIR = "test_files/files/";

    /** Represents the <code>ServletRequest</code> instance used for testing. */
    protected ServletRequest request = new MockHttpServletRequest(null, null);

    /** Represents the <code>RequestParser</code> instance used for testing. */
    protected RequestParser parser = null;

    /** Represents the <code>FileUpload</code> instance used for testing. */
    protected FileUpload fileUpload = null;

    /** Represents the files to be parsed for testing. */
    private File[] files = null;

    /** Represents the form file name of the files to be parsed for testing. */
    private String[] formFileNames = null;

    /** Represents the remote file name of the files to be parsed for testing. */
    private String[] remoteFileNames = null;

    /** Represents the content type of the files to be parsed for testing. */
    private String[] contentTypes = null;

    /** Represents the parsed parameters for testing. */
    private Map parameters = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void setUp() throws Exception {
        // build the testing RequestParser instance
        files = new File[] {new File(DIR, "1.txt"), new File(DIR, "10.txt"), new File(DIR, "100.txt")};
        formFileNames = new String[] {"file1", "file1", "file2"};
        remoteFileNames = new String[] {"1.txt", "10.txt", "100.txt"};
        contentTypes = new String[] {"text/plain", "text/plain", "text/plain"};

        parameters = new HashMap();
        parameters.put("name1", Arrays.asList(new String[] {"value11", "value12"}));
        parameters.put("name2", Arrays.asList(new String[] {"value21"}));
        parser = new MockRequestParser(files, formFileNames, remoteFileNames, contentTypes, parameters);
    }

    /**
     * <p>
     * Tests the method <code>uploadFiles(ServletRequest, RequestParser)</code> when the given request is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testUploadFiles_NullRequest() throws Exception {
        try {
            fileUpload.uploadFiles(null, parser);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>uploadFiles(ServletRequest, RequestParser)</code> when the given parser is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testUploadFiles_NullParser() throws Exception {
        try {
            fileUpload.uploadFiles(request, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>uploadFiles(ServletRequest, RequestParser)</code> when the length of one file exceeds the
     * SingleFileLimit, FileSizeLimitExceededException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testUploadFiles_SingleFileLimitExceed() throws Exception {
        try {
            files[2] = new File(DIR, "101.txt");
            fileUpload.uploadFiles(request, parser);
            fail("FileSizeLimitExceededException should be thrown.");
        } catch (FileSizeLimitExceededException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>uploadFiles(ServletRequest, RequestParser)</code> when the total length of files exceeds
     * the SingleFileLimit, FileSizeLimitExceededException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testUploadFiles_TotalFileLimitExceed() throws Exception {
        try {
            files[0] = new File(DIR, "101.txt");
            fileUpload.uploadFiles(request, parser);
            fail("FileSizeLimitExceededException should be thrown.");
        } catch (FileSizeLimitExceededException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of method <code>uploadFiles(ServletRequest, RequestParser)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testUploadFiles_Accuracy() throws Exception {
        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);

        // check the uploaded files
        String[] actualFormFileNames = uploadResult.getFormFileNames();
        String[] expectedFormFileNames = new String[] {"file1", "file2"};
        Arrays.sort(actualFormFileNames);
        UnitTestHelper.assertEquals("FormFileNames should be correct.", expectedFormFileNames, actualFormFileNames);

        File[][] expectedFiles = new File[2][];
        expectedFiles[0] = new File[] {files[0], files[1]};
        expectedFiles[1] = new File[] {files[2]};

        for (int i = 0; i < actualFormFileNames.length; i++) {
            UploadedFile[] actualFiles = uploadResult.getUploadedFiles(actualFormFileNames[i]);
            assertEquals("Files should be correct.", expectedFiles[i].length, actualFiles.length);

            for (int j = 0; j < actualFiles.length; j++) {
                UnitTestHelper.assertEquals("Files should be correct.", expectedFiles[i][j], actualFiles[j]);
            }
        }

        // check the parameters
        String[] actualParameterNames = uploadResult.getParameterNames();
        String[] expectedParameterNames = (String[]) parameters.keySet().toArray(new String[0]);
        UnitTestHelper.assertEquals("parameter names should be correct.", expectedParameterNames, actualParameterNames);

        for (int i = 0; i < actualParameterNames.length; i++) {
            UnitTestHelper.assertEquals("parameter values should be correct.",
                (String[]) ((List) parameters.get(expectedParameterNames[i])).toArray(new String[0]),
                uploadResult.getParameterValues(actualParameterNames[i]));
        }
    }
}
