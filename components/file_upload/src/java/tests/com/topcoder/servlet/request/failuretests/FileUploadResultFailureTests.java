/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.failuretests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.servlet.request.FileUploadResult;

/**
 * The class <code>FileUploadResultFailureTests</code> contains tests for the class
 * {@link <code>FileUploadResult</code>}.
 * @author FireIce
 * @version 2.0
 */
public class FileUploadResultFailureTests extends TestCase {

    /**
     * An instance of the class being tested.
     * @see FileUploadResult
     */
    private FileUploadResult fileUploadResult;

    /**
     * Run the FileUploadResult(Map,Map) constructor test.
     */
    public void testFileUploadResultIllegalArgumentExceptionNullParameters() {
        Map parameters = null;
        Map uploadedFiles = new HashMap();
        try {
            new FileUploadResult(parameters, uploadedFiles);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the FileUploadResult(Map,Map) constructor test.
     */
    public void testFileUploadResultIllegalArgumentExceptionNullUploadFiles() {
        Map parameters = new HashMap();
        Map uploadedFiles = null;
        try {
            new FileUploadResult(parameters, uploadedFiles);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the FileUploadResult(Map,Map) constructor test.
     */
    public void testFileUploadResultIllegalArgumentExceptionParametersContainsNonStringKey() {
        Map parameters = new HashMap();
        Map uploadedFiles = new HashMap();
        parameters.put(new Object(), new ArrayList());
        try {
            new FileUploadResult(parameters, uploadedFiles);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the FileUploadResult(Map,Map) constructor test.
     */
    public void testFileUploadResultIllegalArgumentExceptionParametersContainsNonListValue() {
        Map parameters = new HashMap();
        Map uploadedFiles = new HashMap();
        parameters.put("ValidKey", new Object());
        try {
            new FileUploadResult(parameters, uploadedFiles);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the FileUploadResult(Map,Map) constructor test.
     */
    public void testFileUploadResultIllegalArgumentExceptionParametersListElementContainsNonString() {
        Map parameters = new HashMap();
        Map uploadedFiles = new HashMap();
        List element = new ArrayList();
        element.add(new Object());
        parameters.put("ValidKey", element);
        try {
            new FileUploadResult(parameters, uploadedFiles);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the FileUploadResult(Map,Map) constructor test.
     */
    public void testFileUploadResultIllegalArgumentExceptionUploadFilesContainsNonStringKey() {
        Map parameters = new HashMap();
        Map uploadedFiles = new HashMap();
        uploadedFiles.put(new Object(), new ArrayList());
        try {
            new FileUploadResult(parameters, uploadedFiles);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the FileUploadResult(Map,Map) constructor test.
     */
    public void testFileUploadResultIllegalArgumentExceptionUploadFilesContainsNonListValue() {
        Map parameters = new HashMap();
        Map uploadedFiles = new HashMap();
        uploadedFiles.put("ValidKey", new Object());
        try {
            new FileUploadResult(parameters, uploadedFiles);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the FileUploadResult(Map,Map) constructor test.
     */
    public void testFileUploadResultIllegalArgumentExceptionUploadFilesListElementContainsNonUploadedFile() {
        Map parameters = new HashMap();
        Map uploadedFiles = new HashMap();
        List element = new ArrayList();
        element.add(new Object());
        uploadedFiles.put("ValidKey", element);
        try {
            new FileUploadResult(parameters, uploadedFiles);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the String getParameter(String) method test.
     */
    public void testGetParameterIllegalArgumentExceptionNullName() {
        try {
            fileUploadResult.getParameter(null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the String getParameter(String) method test.
     */
    public void testGetParameterIllegalArgumentExceptionEmptyName() {
        try {
            fileUploadResult.getParameter("");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileUploadResult.getParameter(" ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the String[] getParameterValues(String) method test.
     */
    public void testGetParameterValuesIllegalArgumentExceptionNullName() {
        try {
            fileUploadResult.getParameterValues(null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the String[] getParameterValues(String) method test.
     */
    public void testGetParameterValuesIllegalArgumentExceptionEmptyName() {
        try {
            fileUploadResult.getParameterValues("");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileUploadResult.getParameterValues(" ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the com.topcoder.servlet.request.UploadedFile getUploadedFile(String) method test.
     */
    public void testGetUploadedFileIllegalArgumentExceptionNullFormFileName() {
        try {
            fileUploadResult.getUploadedFile(null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the com.topcoder.servlet.request.UploadedFile getUploadedFile(String) method test.
     */
    public void testGetUploadedFileIllegalArgumentExceptionEmptyFormFileName() {
        try {
            fileUploadResult.getUploadedFile("");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileUploadResult.getUploadedFile(" ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the com.topcoder.servlet.request.UploadedFile[] getUploadedFiles(String) method test.
     */
    public void testGetUploadedFilesIllegalArgumentExceptionNullFormFileName() {
        try {
            fileUploadResult.getUploadedFiles(null);
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Run the com.topcoder.servlet.request.UploadedFile[] getUploadedFiles(String) method test.
     */
    public void testGetUploadedFilesIllegalArgumentExceptionEmptyFormFileName() {
        try {
            fileUploadResult.getUploadedFiles("");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            fileUploadResult.getUploadedFiles(" ");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Perform pre-test initialization.
     * @throws Exception
     *             if the initialization fails for some reason
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        fileUploadResult = new FileUploadResult(new HashMap(), new HashMap());
    }

    /**
     * Perform post-test clean-up.
     * @throws Exception
     *             if the clean-up fails for some reason
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        fileUploadResult = null;
    }

    /**
     * Aggregates all tests in this class.
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(FileUploadResultFailureTests.class);
    }
}
