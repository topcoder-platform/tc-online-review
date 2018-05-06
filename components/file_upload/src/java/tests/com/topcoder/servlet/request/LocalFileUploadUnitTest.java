/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.File;


/**
 * <p>
 * Tests functionality and error cases of <code>LocalFileUpload</code> class.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class LocalFileUploadUnitTest extends BaseFileUploadUnitTest {
    /** Represents the allowed dirs for testing. */
    private static final String[] ALLOWED_DIRS = new String[] {"test_files/temp", "test_files/files"};

    /** Represents the default dir for testing. */
    private static final String DEFAULT_DIR = "test_files/temp";

    /**
     * <p>
     * Sets up the test environment. The test instance is created. The configuration is loaded.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        UnitTestHelper.clearConfig();
        UnitTestHelper.addConfig("LocalFileUploadConfig.xml");
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
        UnitTestHelper.clearConfig();

        // remove all the temp files
        File[] files = new File(DEFAULT_DIR).listFiles();

        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String)</code> when the given namespace is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgNullNamespace() throws Exception {
        try {
            new LocalFileUpload(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String)</code> when the given namespace is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgEmptyNamespace() throws Exception {
        try {
            new LocalFileUpload(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String)</code> when the given namespace does not exist,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgNotExistNamespace() throws Exception {
        try {
            new LocalFileUpload("NotExist");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String)</code> when the allowedDirs property is missing,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgAllowedDirsPropertyMissing() throws Exception {
        try {
            new LocalFileUpload("AllowedDirsPropertyMissing");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String)</code> when the allowedDirs property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgAllowedDirsPropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("AllowedDirsPropertyEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String)</code> when the defaultDir property is missing,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgDefaultDirMissing() throws Exception {
        try {
            new LocalFileUpload("DefaultDirMissing");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String)</code> when the defaultDir property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgDefaultDirEmpty() throws Exception {
        try {
            new LocalFileUpload("DefaultDirEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String)</code> when the overwrite property is missing,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgOverwritePropertyMissing() throws Exception {
        try {
            new LocalFileUpload("OverwritePropertyMissing");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String)</code> when the default dir is not allowed,
     * DisallowedDirectoryException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgDefaultDirNotAllowed() throws Exception {
        try {
            new LocalFileUpload("DefaultDirNotAllowed");
            fail("DisallowedDirectoryException should be thrown.");
        } catch (DisallowedDirectoryException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgAccuracy1() throws Exception {
        fileUpload = new LocalFileUpload("ValidWithOverwrite");
        assertEquals("singleFileLimit should be properly loaded.", "" + SINGLE_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit").toString());
        assertEquals("totalFileLimit should be properly loaded.", "" + TOTAL_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit").toString());

        UnitTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DEFAULT_DIR,
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "true",
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_OneArgAccuracy2() throws Exception {
        fileUpload = new LocalFileUpload("ValidWithoutOverwrite");
        assertEquals("singleFileLimit should be properly loaded.", "" + SINGLE_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit").toString());
        assertEquals("totalFileLimit should be properly loaded.", "" + TOTAL_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit").toString());

        UnitTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DEFAULT_DIR,
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "false",
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String)</code> when the given namespace is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsNullNamespace() throws Exception {
        try {
            new LocalFileUpload(null, DEFAULT_DIR);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String)</code> when the given namespace is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsEmptyNamespace() throws Exception {
        try {
            new LocalFileUpload(" ", DEFAULT_DIR);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String)</code> when the given namespace does not exist,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsNotExistNamespace() throws Exception {
        try {
            new LocalFileUpload("NotExist", DEFAULT_DIR);
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String)</code> when the given dir is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsNullDir() throws Exception {
        try {
            new LocalFileUpload("ValidWithOverwrite", null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String)</code> when the given dir is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsEmptyDir() throws Exception {
        try {
            new LocalFileUpload("ValidWithOverwrite", " ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String)</code> when the allowedDirs property is missing,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsAllowedDirsPropertyMissing() throws Exception {
        try {
            new LocalFileUpload("AllowedDirsPropertyMissing", DEFAULT_DIR);
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String)</code> when the allowedDirs property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsAllowedDirsPropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("AllowedDirsPropertyEmpty", DEFAULT_DIR);
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String)</code> when the overwrite property is missing,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsOverwritePropertyMissing() throws Exception {
        try {
            new LocalFileUpload("OverwritePropertyMissing", DEFAULT_DIR);
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String)</code> when the default dir is not allowed,
     * DisallowedDirectoryException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsDefaultDirNotAllowed() throws Exception {
        try {
            new LocalFileUpload("DefaultDirNotAllowed", "test_files");
            fail("DisallowedDirectoryException should be thrown.");
        } catch (DisallowedDirectoryException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String, String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsAccuracy1() throws Exception {
        fileUpload = new LocalFileUpload("ValidWithOverwrite", DEFAULT_DIR);
        assertEquals("singleFileLimit should be properly loaded.", "" + SINGLE_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit").toString());
        assertEquals("totalFileLimit should be properly loaded.", "" + TOTAL_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit").toString());

        UnitTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DEFAULT_DIR,
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "true",
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String, String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_TwoArgsAccuracy2() throws Exception {
        fileUpload = new LocalFileUpload("ValidWithoutOverwrite", DEFAULT_DIR);
        assertEquals("singleFileLimit should be properly loaded.", "" + SINGLE_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit").toString());
        assertEquals("totalFileLimit should be properly loaded.", "" + TOTAL_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit").toString());

        UnitTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DEFAULT_DIR,
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "false",
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String, boolean)</code> when the given namespace is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgsNullNamespace() throws Exception {
        try {
            new LocalFileUpload(null, DEFAULT_DIR, true);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String, boolean)</code> when the given namespace is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgsEmptyNamespace() throws Exception {
        try {
            new LocalFileUpload(" ", DEFAULT_DIR, true);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String, boolean)</code> when the given namespace does not
     * exist, ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgsNotExistNamespace() throws Exception {
        try {
            new LocalFileUpload("NotExist", DEFAULT_DIR, true);
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String, boolean)</code> when the given dir is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgsNullDir() throws Exception {
        try {
            new LocalFileUpload("ValidWithOverwrite", null, true);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String, boolean)</code> when the given dir is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgsEmptyDir() throws Exception {
        try {
            new LocalFileUpload("ValidWithOverwrite", " ", true);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String, boolean)</code> when the allowedDirs property is
     * missing, ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgsAllowedDirsPropertyMissing() throws Exception {
        try {
            new LocalFileUpload("AllowedDirsPropertyMissing", DEFAULT_DIR, true);
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String, boolean)</code> when the allowedDirs property is
     * empty, ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgsAllowedDirsPropertyEmpty() throws Exception {
        try {
            new LocalFileUpload("AllowedDirsPropertyEmpty", DEFAULT_DIR, true);
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>LocalFileUpload(String, String, boolean)</code> when the default dir is not allowed,
     * DisallowedDirectoryException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgsDefaultDirNotAllowed() throws Exception {
        try {
            new LocalFileUpload("DefaultDirNotAllowed", "test_files", true);
            fail("DisallowedDirectoryException should be thrown.");
        } catch (DisallowedDirectoryException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String, String, boolean)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgsAccuracy1() throws Exception {
        fileUpload = new LocalFileUpload("ValidWithOverwrite", DEFAULT_DIR, true);
        assertEquals("singleFileLimit should be properly loaded.", "" + SINGLE_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit").toString());
        assertEquals("totalFileLimit should be properly loaded.", "" + TOTAL_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit").toString());

        UnitTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DEFAULT_DIR,
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "true",
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>LocalFileUpload(String, String, boolean)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testLocalFileUpload_ThreeArgsAccuracy2() throws Exception {
        fileUpload = new LocalFileUpload("ValidWithoutOverwrite", DEFAULT_DIR, false);
        assertEquals("singleFileLimit should be properly loaded.", "" + SINGLE_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit").toString());
        assertEquals("totalFileLimit should be properly loaded.", "" + TOTAL_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit").toString());

        UnitTestHelper.assertEquals("allowedDirs should be properly loaded.", ALLOWED_DIRS,
            (String[]) UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "allowedDirs"));
        assertEquals("dir should be properly loaded.", DEFAULT_DIR,
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "dir").toString());
        assertEquals("overwrite should be properly loaded.", "false",
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "overwrite").toString());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getAllowedDirs()</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetAllowedDirs_Accuracy() throws Exception {
        UnitTestHelper.assertEquals("allowedDirs should be properly got.", ALLOWED_DIRS,
            ((LocalFileUpload) fileUpload).getAllowedDirs());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getDir()</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetDir_Accuracy() throws Exception {
        assertEquals("dir should be properly loaded.", DEFAULT_DIR, ((LocalFileUpload) fileUpload).getDir());
    }

    /**
     * <p>
     * Tests the accuracy of method <code>canOverwrite()</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testCanOverwrite_Accuracy() throws Exception {
        assertEquals("overwrite should be properly loaded.", true, ((LocalFileUpload) fileUpload).canOverwrite());
    }

    /**
     * <p>
     * Tests the method <code>getUploadedFile(String, boolean)</code> when the fileId is null, IllegalArgumentException
     * is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetUploadedFile_NullFileId() throws Exception {
        try {
            fileUpload.getUploadedFile(null, true);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>getUploadedFile(String, boolean)</code> when the fileId is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetUploadedFile_EmptyFileId() throws Exception {
        try {
            fileUpload.getUploadedFile(" ", true);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>getUploadedFile(String, boolean)</code> when the fileId does not exist,
     * FileDoesNotExistException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetUploadedFile_NotExistFileId() throws Exception {
        try {
            fileUpload.getUploadedFile("overwrite_1.txt", true);
            fail("FileDoesNotExistException should be thrown.");
        } catch (FileDoesNotExistException e) {
            // good
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
        UploadedFile[] result = fileUpload.uploadFiles(request, parser).getAllUploadedFiles();

        // get the uploaded file
        for (int i = 0; i < result.length; i++) {
            UploadedFile uploadedFile = fileUpload.getUploadedFile(result[i].getFileId(), true);
            UnitTestHelper.assertEquals("The uploaded file should be correct.", result[i].getInputStream(),
                uploadedFile.getInputStream());
        }
    }

    /**
     * <p>
     * Tests the method <code>removeUploadedFile(String)</code> when the fileId is null, IllegalArgumentException is
     * expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoveUploadedFile_NullFileId() throws Exception {
        try {
            fileUpload.removeUploadedFile(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>removeUploadedFile(String)</code> when the fileId is empty, IllegalArgumentException is
     * expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoveUploadedFile_EmptyFileId() throws Exception {
        try {
            fileUpload.removeUploadedFile(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the method <code>removeUploadedFile(String)</code> when the fileId does not exist,
     * FileDoesNotExistException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoveUploadedFile_NotExistFileId() throws Exception {
        try {
            fileUpload.removeUploadedFile("overwrite_1.txt");
            fail("FileDoesNotExistException should be thrown.");
        } catch (FileDoesNotExistException e) {
            // good
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
