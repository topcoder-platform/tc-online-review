/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.accuracytests;

import com.meterware.httpunit.UploadFileSpec;
import com.topcoder.servlet.request.FileDoesNotExistException;
import com.topcoder.servlet.request.HttpRequestParser;
import com.topcoder.servlet.request.LocalFileUpload;
import com.topcoder.servlet.request.RequestParser;
import com.topcoder.servlet.request.UnitTestHelper;
import com.topcoder.servlet.request.UploadedFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;



/**
 * <p>
 * Tests functionality of <code>LocalFileUpload</code> class.
 * </p>
 *
 * @author lyt
 * @version 2.0
 */
public class LocalFileUploadAccuracyTest extends FileUploadAccuracyTest {
    /** Represents the default dir for testing. */
    private static final String DEFAULT_DIR = "test_files/accuracytests/temp";

    /** Represents the dir to store the testing files. */
    protected static final String DIR = "test_files/accuracytests/files";

    /** Represents the allowed dirs for testing. */
    private static final String[] ALLOWED_DIRS = new String[] { DEFAULT_DIR, DIR };

  

    /**
     * <p>
     * Sets up the test environment. The test instance is created. The configuration is loaded.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        AccuracyTestHelper.addConfig("accuracytests/LocalFileUploadConfig.xml");

        fileUpload = new LocalFileUpload("ValidWithOverwrite");
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
        AccuracyTestHelper.clearConfig();

        // remove all the temp files
        File[] files = new File(DEFAULT_DIR).listFiles();

        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArg_Accuracy1()
        throws Exception {
        fileUpload = new LocalFileUpload("ValidWithOverwrite");
        assertEquals("singleFileLimit should be properly loaded.", SINGLE_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "singleFileLimit")).longValue());
        assertEquals("totalFileLimit should be properly loaded.", TOTAL_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "totalFileLimit")).longValue());

        AccuracyTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DEFAULT_DIR,
            AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "true",
            AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArg_Accuracy2()
        throws Exception {
        fileUpload = new LocalFileUpload("ValidWithoutOverwrite");
        assertEquals("singleFileLimit should be properly loaded.", SINGLE_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "singleFileLimit")).longValue());
        assertEquals("totalFileLimit should be properly loaded.", TOTAL_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "totalFileLimit")).longValue());

        AccuracyTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DEFAULT_DIR,
            AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "false",
            AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String, String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgs_Accuracy()
        throws Exception {
        fileUpload = new LocalFileUpload("ValidWithOverwrite", DIR);
        assertEquals("singleFileLimit should be properly loaded.", SINGLE_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "singleFileLimit")).longValue());
        assertEquals("totalFileLimit should be properly loaded.", TOTAL_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "totalFileLimit")).longValue());

        AccuracyTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DIR,
            AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "true",
            AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String, String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgs_Accuracy2()
        throws Exception {
        fileUpload = new LocalFileUpload("ValidWithoutOverwrite", DIR);
        assertEquals("singleFileLimit should be properly loaded.", SINGLE_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "singleFileLimit")).longValue());
        assertEquals("totalFileLimit should be properly loaded.", TOTAL_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "totalFileLimit")).longValue());

        AccuracyTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DIR,
            AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "false",
            AccuracyTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String, String, boolean)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgs_Accuracy1()
        throws Exception {
        fileUpload = new LocalFileUpload("ValidWithOverwrite", DIR, false);
        assertEquals("singleFileLimit should be properly loaded.", SINGLE_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "singleFileLimit")).longValue());
        assertEquals("totalFileLimit should be properly loaded.", TOTAL_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "totalFileLimit")).longValue());

        UnitTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DIR,
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "false",
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String, String, boolean)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgs_Accuracy2()
        throws Exception {
        fileUpload = new LocalFileUpload("ValidWithoutOverwrite", DIR, true);
        assertEquals("singleFileLimit should be properly loaded.", SINGLE_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "singleFileLimit")).longValue());
        assertEquals("totalFileLimit should be properly loaded.", TOTAL_FILE_LIMIT,
            ((Long) AccuracyTestHelper.getPrivateField(fileUpload.getClass().getSuperclass(), fileUpload,
                "totalFileLimit")).longValue());
        UnitTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DIR,
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "true",
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
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
}
