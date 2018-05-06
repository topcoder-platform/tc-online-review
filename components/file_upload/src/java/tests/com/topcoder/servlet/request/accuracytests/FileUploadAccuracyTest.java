/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.accuracytests;

import com.meterware.httpunit.UploadFileSpec;

import com.topcoder.servlet.request.ConfigurationException;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.HttpRequestParser;
import com.topcoder.servlet.request.RequestParser;
import com.topcoder.servlet.request.UploadedFile;

import junit.framework.TestCase;

import java.io.File;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;


/**
 * <p>
 * Tests functionality of <code>FileUpload</code> class.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public abstract class FileUploadAccuracyTest extends TestCase {
    /** Represents the dir to store the testing files. */
    protected static final String DIR = "test_files/accuracytests/files/";

    /** Represents the total file size limit for testing. */
    protected static final long SINGLE_FILE_LIMIT = 10000;

    /** Represents the total file size limit for testing. */
    protected static final long TOTAL_FILE_LIMIT = 20000;

    /** Represents the <code>FileUpload</code> instance used for testing. */
    protected FileUpload fileUpload = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created. The configuration is loaded.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void setUp() throws Exception {
        AccuracyTestHelper.clearConfig();
    }

    /**
     * <p>
     * Tears down the test environment. The configuration is cleared.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void tearDown() throws Exception {
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>FileUpload(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testFileUpload_Accuracy() throws Exception {
        assertEquals("singleFileLimit should be properly loaded.", SINGLE_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "singleFileLimit")).longValue());
        assertEquals("totalFileLimit should be properly loaded.", TOTAL_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "totalFileLimit")).longValue());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getUniqueFileName(String)</code>.
     * </p>
     */
    public void testGetUniqueFileName_Accuracy() throws Exception {
        String remoteFileName = "remoteFileName";
        assertTrue("The unique file name should be generated.",
            MockFileUpload.superGetUniqueFileName(remoteFileName).indexOf("_" + remoteFileName) > 0);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getRemoteFileName(String)</code>.
     * </p>
     */
    public void testGetRemoteFileName_Accuracy() throws ConfigurationException {
        String remoteFileName = "remoteFileName";
        String uniqueFileName = "xsgFrot_" + remoteFileName;
        assertEquals("The remote file name should be generated properly.", remoteFileName,
            MockFileUpload.superGetRemoteFileName(uniqueFileName));
    }

    /**
     * <p>
     * Tests the accuracy of method <code>uploadFiles(ServletRequest, RequestParser)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testUploadFiles_Accuracy1() throws Exception {
        Map files = new HashMap();
        File file0 = new File(DIR, "text.txt");
        File file1 = new File(DIR, "pic.jpg");

        files.put("form1", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file0), new UploadFileSpec(file1) }));

        Map parameters = new HashMap();

        ServletRequest request = AccuracyTestHelper.prepareRequest(files, parameters);
        RequestParser parser = new HttpRequestParser();

        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);

        // check the uploaded files
        String[] actualFormFileNames = uploadResult.getFormFileNames();
        String[] expectedFormFileNames = new String[] { "form1" };

        AccuracyTestHelper.assertEquals("FormFileNames should be correct.", expectedFormFileNames, actualFormFileNames);

        File[] expectedFiles = new File[] { file0, file1 };

        UploadedFile[] actualFiles = uploadResult.getUploadedFiles(actualFormFileNames[0]);
        assertEquals("Files should be correct.", expectedFiles.length, actualFiles.length);

        for (int i = 0; i < actualFiles.length; i++) {
            AccuracyTestHelper.assertEquals("Files should be correct.", expectedFiles[i], actualFiles[i]);
        }
    }

    /**
     * <p>
     * Tests the accuracy of method <code>uploadFiles(ServletRequest, RequestParser)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testUploadFiles_Accuracy2() throws Exception {
        Map files = new HashMap();
        File file0 = new File(DIR, "text.txt");
        File file1 = new File(DIR, "pic.jpg");

        files.put("form1", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file0) }));
        files.put("form2", Arrays.asList(new UploadFileSpec[] { new UploadFileSpec(file1) }));

        Map parameters = new HashMap();

        parameters.put("name1", Arrays.asList(new String[] { "value11", "value12" }));
        parameters.put("name2", Arrays.asList(new String[] { "value21" }));

        ServletRequest request = AccuracyTestHelper.prepareRequest(files, parameters);
        RequestParser parser = new HttpRequestParser();

        FileUploadResult uploadResult = fileUpload.uploadFiles(request, parser);

        // check the uploaded files
        String[] actualFormFileNames = uploadResult.getFormFileNames();
        String[] expectedFormFileNames = new String[] { "form1", "form2" };
        Arrays.sort(actualFormFileNames);
        AccuracyTestHelper.assertEquals("FormFileNames should be correct.", expectedFormFileNames, actualFormFileNames);

        File[][] expectedFiles = new File[2][];
        expectedFiles[0] = new File[] { file0 };
        expectedFiles[1] = new File[] { file1 };

        for (int i = 0; i < actualFormFileNames.length; i++) {
            UploadedFile[] actualFiles = uploadResult.getUploadedFiles(actualFormFileNames[i]);
            assertEquals("Files should be correct.", expectedFiles[i].length, actualFiles.length);

            for (int j = 0; j < actualFiles.length; j++) {
                AccuracyTestHelper.assertEquals("Files should be correct.", expectedFiles[i][j], actualFiles[j]);
            }
        }

        // check the parameters
        String[] actualParameterNames = uploadResult.getParameterNames();
        String[] expectedParameterNames = (String[]) parameters.keySet().toArray(new String[0]);
        AccuracyTestHelper.assertEquals("parameter names should be correct.", expectedParameterNames,
            actualParameterNames);

        for (int i = 0; i < actualParameterNames.length; i++) {
            AccuracyTestHelper.assertEquals("parameter values should be correct.",
                (String[]) ((List) parameters.get(expectedParameterNames[i])).toArray(new String[0]),
                uploadResult.getParameterValues(actualParameterNames[i]));
        }
    }
}
