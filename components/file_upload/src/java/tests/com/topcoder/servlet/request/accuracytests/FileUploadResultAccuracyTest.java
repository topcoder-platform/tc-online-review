/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.accuracytests;

import com.topcoder.servlet.request.FileUploadResult;
import com.topcoder.servlet.request.MockUploadedFile;
import com.topcoder.servlet.request.UnitTestHelper;
import com.topcoder.servlet.request.UploadedFile;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Tests functionality of <code>FileUploadResult</code> class.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public class FileUploadResultAccuracyTest extends TestCase {
    /** Represents the mapping of form parameters used for testing. */
    private Map parameters;

    /** Represents the mapping from form file names to a list of uploaded files used for testing. */
    private Map uploadedFiles;

    /** Represents the <code>FileUploadResult</code> instance used for testing. */
    private FileUploadResult result = null;

    /**
     * <p>
     * Sets up the test environment. The test instance is created.
     * </p>
     */
    protected void setUp() {
        parameters = new HashMap();
        parameters.put("name1", Arrays.asList(new String[] { "value11", "value12" }));
        parameters.put("name2", Arrays.asList(new String[0]));
        parameters.put("name3", Arrays.asList(new String[] { "value3" }));

        uploadedFiles = new HashMap();
        uploadedFiles.put("file1",
            Arrays.asList(new UploadedFile[] { new MockUploadedFile("remoteFileName1", "contentType") }));
        uploadedFiles.put("file2", Arrays.asList(new UploadedFile[0]));
        uploadedFiles.put("file3",
            Arrays.asList(new UploadedFile[] { new MockUploadedFile("remoteFileName2", "contentType") }));

        result = new FileUploadResult(parameters, uploadedFiles);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getParameterValues(String)</code>.
     * </p>
     */
    public void testGetParameterValues_Accuracy1() {
        String[] ret = result.getParameterValues("name1");
        Arrays.sort(ret);

        String[] expected = new String[] {"value11", "value12"};
        UnitTestHelper.assertEquals("The parameter values should be correct.", expected, ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getParameterValues(String)</code>.
     * </p>
     */
    public void testGetParameterValues_Accuracy2() {
        String[] ret = result.getParameterValues("name4");
        Arrays.sort(ret);

        String[] expected = new String[0];
        UnitTestHelper.assertEquals("The parameter values should be correct.", expected, ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getParameter(String)</code>.
     * </p>
     */
    public void testGetParameter_Accuracy1() {
        String ret = result.getParameter("name4");

        assertNull("The parameter values should be correct.", ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getParameter(String)</code>.
     * </p>
     */
    public void testGetParameter_Accuracy2() {
        String ret = result.getParameter("name3");

        assertEquals("The parameter values should be correct.", "value3", ret);
    }
    /**
     * <p>
     * Tests the accuracy of method <code>getFormFileNames()</code>.
     * </p>
     */
    public void testGetFormFileNames_Accuracy() {
        String[] ret = result.getFormFileNames();
        Arrays.sort(ret);

        String[] expected = new String[] {"file1", "file2", "file3"};
        UnitTestHelper.assertEquals("The form file names should be correct.", expected, ret);
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>FileUploadResult(Map, Map)</code>.
     * </p>
     */
    public void testFileUploadResult_Accuracy() {
        assertEquals("The parameters should be correct.", parameters,
            UnitTestHelper.getPrivateField(result.getClass(), result, "parameters"));
        assertEquals("The uploadedFiles should be correct.", uploadedFiles,
            UnitTestHelper.getPrivateField(result.getClass(), result, "uploadedFiles"));
    }
    /**
     * <p>
     * Tests the accuracy of method <code>getUploadedFiles(String)</code>.
     * </p>
     */
    public void testGetUploadedFiles_Accuracy() {
        UploadedFile[] ret = result.getUploadedFiles("file3");
        
        UploadedFile[] expected = (UploadedFile[]) ((List) this.uploadedFiles.get("file3")).toArray(
                new UploadedFile[0]);
        UnitTestHelper.assertEquals("The upload files should be correct.", expected, ret);
    }
    /**
     * <p>
     * Tests the accuracy of method <code>getUploadedFile(String)</code>.
     * </p>
     */
    public void testGetUploadedFile_Accuracy() {
        UploadedFile ret = result.getUploadedFile("file1");

        UploadedFile expected = (UploadedFile) ((List) this.uploadedFiles.get("file1")).get(0);
        assertEquals("The upload files should be correct.", expected, ret);
    }
    /**
     * <p>
     * Tests the accuracy of method <code>getAllUploadedFiles()</code>.
     * </p>
     */
    public void testGetAllUploadedFiles_Accuracy() {
        UploadedFile[] ret = result.getAllUploadedFiles();

        List list = new ArrayList();
        list.addAll((List) this.uploadedFiles.get("file3"));
        list.addAll((List) this.uploadedFiles.get("file1"));
        list.addAll((List) this.uploadedFiles.get("file2"));
        
        UploadedFile[] expected =(UploadedFile[])list.toArray(new UploadedFile[0]);
        AccuracyTestHelper.assertEquals("The upload files should be correct.", expected, ret);
    }
}
