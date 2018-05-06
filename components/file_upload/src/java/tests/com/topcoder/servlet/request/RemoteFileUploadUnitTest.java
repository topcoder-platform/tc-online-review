/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import java.io.File;

import com.topcoder.processor.ipserver.IPServer;


/**
 * <p>
 * Tests functionality and error cases of <code>RemoteFileUpload</code> class.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class RemoteFileUploadUnitTest extends BaseFileUploadUnitTest {
    /** Represents the temp dir for testing. */
    private static final String TEMP_DIR = "test_files/temp/";

    /** the IPServer instance used in tests. */
    private IPServer ipServer;

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
        UnitTestHelper.addConfig("RemoteFileUpload.xml");
        UnitTestHelper.addConfig("MessageFactory.xml");
        UnitTestHelper.addConfig("IPServerManager.xml");
        UnitTestHelper.addConfig("DBConnectionFactoryImpl.xml");

        // create the server
        ipServer = UnitTestHelper.createServer();
        UnitTestHelper.startServer(ipServer);

        fileUpload = new RemoteFileUpload("Valid");

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

        // remove all the temp files
        File[] files = new File(TEMP_DIR).listFiles();

        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }

        UnitTestHelper.stopServer(ipServer);

        // clear the server files
        UnitTestHelper.clearServerFiles();
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the given namespace is null,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_NullNamespace() throws Exception {
        try {
            new RemoteFileUpload(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the given namespace is empty,
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_EmptyNamespace() throws Exception {
        try {
            new RemoteFileUpload(" ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the given namespace does not exist,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_NotExistNamespace() throws Exception {
        try {
            new RemoteFileUpload("NotExist");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the ip_address property is missing,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_IPADDRESSPropertyMissing() throws Exception {
        try {
            new RemoteFileUpload("IPADDRESSPropertyMissing");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the ip_address property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_IPADDRESSPropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("IPADDRESSPropertyEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the ip_address property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_IPADDRESSPropertyInvalid() throws Exception {
        try {
            new RemoteFileUpload("IPADDRESSPropertyInvalid");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the port property is missing,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_PortPropertyMissing() throws Exception {
        try {
            new RemoteFileUpload("PortPropertyMissing");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the port property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_PortPropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("PortPropertyEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the port property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_PortPropertyInvalid1() throws Exception {
        try {
            new RemoteFileUpload("PortPropertyInvalid1");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the port property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_PortPropertyInvalid2() throws Exception {
        try {
            new RemoteFileUpload("PortPropertyInvalid2");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the message_namespace property is missing,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_MessageNamespacePropertyMissing() throws Exception {
        try {
            new RemoteFileUpload("MessageNamespacePropertyMissing");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the message_namespace property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_MessageNamespacePropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("MessageNamespacePropertyEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the message_namespace property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_MessageNamespacePropertyInvalid() throws Exception {
        try {
            new RemoteFileUpload("MessageNamespacePropertyInvalid");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the handler_id property is missing,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_HandleIDPropertyMissing() throws Exception {
        try {
            new RemoteFileUpload("HandleIDPropertyMissing");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the handler_id property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_HandleIDPropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("HandleIDPropertyEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the tempDir property is empty,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_TempDirPropertyEmpty() throws Exception {
        try {
            new RemoteFileUpload("TempDirPropertyEmpty");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the constructor <code>RemoteFileUpload(String)</code> when the tempDir property is invalid,
     * ConfigurationException is expected.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_TempDirPropertyInvalid() throws Exception {
        try {
            new RemoteFileUpload("TempDirPropertyInvalid");
            fail("ConfigurationException should be thrown.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests the accuracy of constructor <code>RemoteFileUpload(String)</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testRemoteFileUpload_Accuracy() throws Exception {
        assertEquals("singleFileLimit should be properly loaded.", "" + SINGLE_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "singleFileLimit").toString());
        assertEquals("totalFileLimit should be properly loaded.", "" + TOTAL_FILE_LIMIT,
            UnitTestHelper.getParentClassPrivateField(fileUpload.getClass(), fileUpload, "totalFileLimit").toString());

        assertEquals("tempDir should be properly loaded.", TEMP_DIR,
            UnitTestHelper.getPrivateField(fileUpload.getClass(), fileUpload, "tempDir"));
    }

    /**
     * <p>
     * Tests the accuracy of method <code>getTempDir()</code>.
     * </p>
     *
     * @throws Exception any exception to junit.
     */
    public void testGetTempDir_Accuracy() throws Exception {
        assertEquals("tempDir should be properly got.", TEMP_DIR, ((RemoteFileUpload) fileUpload).getTempDir());
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
            fileUpload.getUploadedFile("12", true);
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
            fileUpload.removeUploadedFile("12");
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
