/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import junit.framework.TestCase;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Tests functionality and error cases of <code>FileUploadResult</code> class.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class FileUploadResultUnitTest extends TestCase {
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
        parameters.put("name1", Arrays.asList(new String[] {"value11", "value12"}));
        parameters.put("name2", Arrays.asList(new String[0]));

        File file = new File("test_files/temp/11_test.txt");
        file.deleteOnExit();
        uploadedFiles = new HashMap();
        uploadedFiles.put("file1",
            Arrays.asList(new UploadedFile[] {new LocalUploadedFile(file, "remoteFileName", "contentType")}));
        uploadedFiles.put("file2", Arrays.asList(new UploadedFile[0]));

        result = new FileUploadResult(parameters, uploadedFiles);
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the given parameters is null,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_NullParameters() {
        try {
            new FileUploadResult(null, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the key of given parameters is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_InvalidParametersKey1() {
        try {
            parameters.put(null, new ArrayList());
            new FileUploadResult(parameters, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the key of given parameters is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_InvalidParametersKey2() {
        try {
            parameters.put(new ArrayList(), new ArrayList());
            new FileUploadResult(parameters, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the value of given parameters is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_InvalidParametersValue1() {
        try {
            parameters.put("name", "name");
            new FileUploadResult(parameters, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the value of given parameters is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_InvalidParametersValue2() {
        try {
            parameters.put("name", Arrays.asList(new Object[] {null}));
            new FileUploadResult(parameters, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the value of given parameters is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_InvalidParametersValue3() {
        try {
            parameters.put("name", Arrays.asList(new Object[] {new ArrayList()}));
            new FileUploadResult(parameters, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the given uploadedFiles is null,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_NullUploadedFiles() {
        try {
            new FileUploadResult(parameters, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the key of given uploadedFiles is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_InvalidUploadedFilesKey1() {
        try {
            uploadedFiles.put(null, new ArrayList());
            new FileUploadResult(parameters, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the key of given uploadedFiles is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_InvalidUploadedFilesKey2() {
        try {
            uploadedFiles.put(new ArrayList(), new ArrayList());
            new FileUploadResult(parameters, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the value of given uploadedFiles is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_InvalidUploadedFilesValue1() {
        try {
            uploadedFiles.put("name", "name");
            new FileUploadResult(parameters, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the value of given uploadedFiles is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_InvalidUploadedFilesValue2() {
        try {
            uploadedFiles.put("name", Arrays.asList(new Object[] {null}));
            new FileUploadResult(parameters, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>FileUploadResult(Map, Map)</code> when the value of given uploadedFiles is invalid,
     * IllegalArgumentException is expected.
     * </p>
     */
    public void testFileUploadResult_InvalidUploadedFilesValue3() {
        try {
            uploadedFiles.put("name", Arrays.asList(new Object[] {new ArrayList()}));
            new FileUploadResult(parameters, uploadedFiles);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
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
     * Tests the accuracy of method <code>getParameterNames()</code>.
     * </p>
     */
    public void testGetParameterNames_Accuracy() {
        String[] ret = result.getParameterNames();
        Arrays.sort(ret);

        String[] expected = new String[] {"name1", "name2"};
        UnitTestHelper.assertEquals("The parameter names should be correct.", expected, ret);
    }

    /**
     * <p>
     * Tests the method <code>getParameterValues(String)</code> when the given name is null, IllegalArgumentException
     * is expected.
     * </p>
     */
    public void testGetParameterValues_NullName() {
        try {
            result.getParameterValues(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>getParameterValues(String)</code> when the given name is empty, IllegalArgumentException
     * is expected.
     * </p>
     */
    public void testGetParameterValues_EmptyName() {
        try {
            result.getParameterValues(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
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
        String[] ret = result.getParameterValues("name2");

        UnitTestHelper.assertEquals("The parameter values should be correct.", new String[0], ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getParameterValues(String)</code>.
     * </p>
     */
    public void testGetParameterValues_Accuracy3() {
        String[] ret = result.getParameterValues("name3");

        UnitTestHelper.assertEquals("The parameter values should be correct.", new String[0], ret);
    }

    /**
     * <p>
     * Tests the method <code>getParameter(String)</code> when the given name is null, IllegalArgumentException is
     * expected.
     * </p>
     */
    public void testGetParameter_NullName() {
        try {
            result.getParameter(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>getParameter(String)</code> when the given name is empty, IllegalArgumentException is
     * expected.
     * </p>
     */
    public void testGetParameter_EmptyName() {
        try {
            result.getParameter(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getParameter(String)</code>.
     * </p>
     */
    public void testGetParameter_Accuracy1() {
        String ret = result.getParameter("name1");

        String expected = "value11";
        assertEquals("The parameter values should be correct.", expected, ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getParameter(String)</code>.
     * </p>
     */
    public void testGetParameter_Accuracy2() {
        String ret = result.getParameter("name2");

        assertEquals("The parameter values should be correct.", null, ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getParameter(String)</code>.
     * </p>
     */
    public void testGetParameter_Accuracy3() {
        String ret = result.getParameter("name3");

        assertEquals("The parameter values should be correct.", null, ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getFormFileNames()</code>.
     * </p>
     */
    public void testGetFormFileNames_Accuracy() {
        String[] ret = result.getFormFileNames();
        Arrays.sort(ret);

        String[] expected = new String[] {"file1", "file2"};
        UnitTestHelper.assertEquals("The form file names should be correct.", expected, ret);
    }

    /**
     * <p>
     * Tests the method <code>getUploadedFiles(String)</code> when the given name is null, IllegalArgumentException is
     * expected.
     * </p>
     */
    public void testGetUploadedFiles_NullName() {
        try {
            result.getUploadedFiles(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>getUploadedFiles(String)</code> when the given name is empty, IllegalArgumentException is
     * expected.
     * </p>
     */
    public void testGetUploadedFiles_EmptyName() {
        try {
            result.getUploadedFiles(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getUploadedFiles(String)</code>.
     * </p>
     */
    public void testGetUploadedFiles_Accuracy1() {
        UploadedFile[] ret = result.getUploadedFiles("file1");

        UploadedFile[] expected = (UploadedFile[]) ((List) this.uploadedFiles.get("file1")).toArray(
                new UploadedFile[0]);
        UnitTestHelper.assertEquals("The upload files should be correct.", expected, ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getUploadedFiles(String)</code>.
     * </p>
     */
    public void testGetUploadedFiles_Accuracy2() {
        UploadedFile[] ret = result.getUploadedFiles("file2");

        UnitTestHelper.assertEquals("The upload files should be correct.", new UploadedFile[0], ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getUploadedFiles(String)</code>.
     * </p>
     */
    public void testGetUploadedFiles_Accuracy3() {
        UploadedFile[] ret = result.getUploadedFiles("file3");

        UnitTestHelper.assertEquals("The upload files should be correct.", new UploadedFile[0], ret);
    }

    /**
     * <p>
     * Tests the method <code>getUploadedFile(String)</code> when the given name is null, IllegalArgumentException is
     * expected.
     * </p>
     */
    public void testGetUploadedFile_NullName() {
        try {
            result.getUploadedFile(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>getUploadedFile(String)</code> when the given name is empty, IllegalArgumentException is
     * expected.
     * </p>
     */
    public void testGetUploadedFile_EmptyName() {
        try {
            result.getUploadedFile(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getUploadedFile(String)</code>.
     * </p>
     */
    public void testGetUploadedFile_Accuracy1() {
        UploadedFile ret = result.getUploadedFile("file1");

        UploadedFile expected = (UploadedFile) ((List) this.uploadedFiles.get("file1")).get(0);
        assertEquals("The upload files should be correct.", expected, ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getUploadedFile(String)</code>.
     * </p>
     */
    public void testGetUploadedFile_Accuracy2() {
        UploadedFile ret = result.getUploadedFile("file2");

        assertEquals("The upload files should be correct.", null, ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getUploadedFile(String)</code>.
     * </p>
     */
    public void testGetUploadedFile_Accuracy3() {
        UploadedFile ret = result.getUploadedFile("file3");

        assertEquals("The upload files should be correct.", null, ret);
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getAllUploadedFiles()</code>.
     * </p>
     */
    public void testGetAllUploadedFiles_Accuracy() {
        UploadedFile[] ret = result.getAllUploadedFiles();

        UploadedFile[] expected = (UploadedFile[]) ((List) this.uploadedFiles.get("file1")).toArray(
                new UploadedFile[0]);
        UnitTestHelper.assertEquals("The upload files should be correct.", expected, ret);
    }
}
