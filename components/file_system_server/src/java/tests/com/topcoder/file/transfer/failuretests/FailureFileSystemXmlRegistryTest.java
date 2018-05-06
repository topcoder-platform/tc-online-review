/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.registry.FileIdExistsException;
import com.topcoder.file.transfer.registry.FileIdNotFoundException;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.file.transfer.registry.GroupExistsException;
import com.topcoder.file.transfer.registry.GroupNotFoundException;
import com.topcoder.file.transfer.registry.RegistryConfigurationException;

import com.topcoder.util.idgenerator.IDGenerator;

import junit.framework.TestCase;

import java.io.File;

import java.util.ArrayList;
import java.util.List;


/**
 * Test <code>FileSystemXmlRegistry</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureFileSystemXmlRegistryTest extends TestCase {
    /** The files file persistence location. */
    private static final String FILES_FILE_LOCATION = "test_files/failure/filesfile.xml";

    /** The invalid files file persistence location. */
    private static final String INVALID_FILES_FILE_LOCATION = "test_files/failure/invalidfilesfile.xml";

    /** The groups file persistence location. */
    private static final String GROUPS_FILE_LOCATION = "test_files/failure/groupsfile.xml";

    /** The main <code>FileSystemXmlRegistry</code> instance used to test. */
    private FileSystemXmlRegistry registry;

    /** The IDGenerator used to construct <code>FileSystemXmlRegistry</code>. */
    private IDGenerator idGen;

    /**
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        FailureHelper.clearConfigs();
        FailureHelper.loadConfigs();
        idGen = FailureHelper.getIDGenerator();
        registry = new FileSystemXmlRegistry(FILES_FILE_LOCATION, GROUPS_FILE_LOCATION, idGen);
    }

    /**
     * Clears Configurations.
     *
     * @throws Exception to Junit.
     */
    protected void tearDown() throws Exception {
        FailureHelper.clearConfigs();
    }

    /**
     * Test <code>FileSystemXmlRegistry(String, String, IDGenerator)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if filesfile is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemXmlRegistryForNullPointerException_Filsfile()
        throws Exception {
        try {
            new FileSystemXmlRegistry(null, GROUPS_FILE_LOCATION, idGen);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(String, String, IDGenerator)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if groupsfile is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemXmlRegistryForNullPointerException_Groupsfile()
        throws Exception {
        try {
            new FileSystemXmlRegistry(FILES_FILE_LOCATION, null, idGen);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(String, String, IDGenerator)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if IDGenerator is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemXmlRegistryForNullPointerException_IDGen()
        throws Exception {
        try {
            new FileSystemXmlRegistry(FILES_FILE_LOCATION, GROUPS_FILE_LOCATION, null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(String, String, IDGenerator)</code> method for failure.
     * <code>IllegalArgumentException</code> should be thrown if Filesfile is empty.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemXmlRegistryForIllegalArgumentException_Filesfile()
        throws Exception {
        try {
            new FileSystemXmlRegistry(" ", GROUPS_FILE_LOCATION, idGen);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(String, String, IDGenerator)</code> method for failure.
     * <code>IllegalArgumentException</code> should be thrown if Groupsfile is empty.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemXmlRegistryForIllegalArgumentException_Groupsfile()
        throws Exception {
        try {
            new FileSystemXmlRegistry(FILES_FILE_LOCATION, " ", idGen);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(String, String, IDGenerator)</code> method for failure.
     * <code>RegistryConfigurationException</code> should be thrown if id or name attribute is missing.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemXmlRegistryForRegistryConfigurationException_IDMissing()
        throws Exception {
        try {
            new FileSystemXmlRegistry(INVALID_FILES_FILE_LOCATION, GROUPS_FILE_LOCATION, idGen);
            fail("the RegistryConfigurationException should be thrown!");
        } catch (RegistryConfigurationException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(File, File, IDGenerator)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemXmlRegistryForNullPointerExceptionFile()
        throws Exception {
        try {
            new FileSystemXmlRegistry(null, new File(GROUPS_FILE_LOCATION), idGen);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(File, File, IDGenerator)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemXmlRegistryForNullPointerExceptionGroup()
        throws Exception {
        try {
            new FileSystemXmlRegistry(new File(FILES_FILE_LOCATION), null, idGen);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(File, File, IDGenerator)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemXmlRegistryForNullPointerExceptionIDG()
        throws Exception {
        try {
            new FileSystemXmlRegistry(new File(FILES_FILE_LOCATION), new File(GROUPS_FILE_LOCATION), null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(File, File, IDGenerator)</code> method for failure.
     * <code>RegistryConfigurationException</code> should be thrown .
     *
     * @throws Exception to Junit.
     */
    public void testFileSystemXmlRegistryForRegistryConfigurationException()
        throws Exception {
        try {
            new FileSystemXmlRegistry(new File(INVALID_FILES_FILE_LOCATION), new File(GROUPS_FILE_LOCATION), idGen);
            fail("the RegistryConfigurationException should be thrown!");
        } catch (RegistryConfigurationException e) {
            // Good
        }
    }

    /**
     * Test <code>addFile(String, String)</code> method for failure. <code>NullPointerException</code> should be thrown
     * if fileid is null.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileForNullPointerException_ID()
        throws Exception {
        try {
            String fileId = null;
            String filename = "filename5";
            registry.addFile(fileId, filename);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>addFile(String, String)</code> method for failure. <code>NullPointerException</code> should be thrown
     * if filename is null.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileForNullPointerException_Name()
        throws Exception {
        try {
            String fileId = "1005";
            String filename = null;
            registry.addFile(fileId, filename);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>addFile(String, String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if fileid is empty.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileForIllegalArgumentException_ID()
        throws Exception {
        try {
            String fileId = "  ";
            String filename = "filename5";
            registry.addFile(fileId, filename);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>addFile(String, String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if filename is empty.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileForIllegalArgumentException_Name()
        throws Exception {
        try {
            String fileId = "1005";
            String filename = "  ";
            registry.addFile(fileId, filename);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>addFile(String, String)</code> method for failure. <code>FileIdExistsException</code> should be
     * thrown if fileid already exists.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileForFileIdExistsException() throws Exception {
        try {
            String fileId = "1001";
            String filename = "filename5";
            registry.addFile(fileId, filename);
            fail("the FileIdExistsException should be thrown!");
        } catch (FileIdExistsException e) {
            // Good
        }
    }

    /**
     * Test <code>renameFile(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if fileid is null.
     *
     * @throws Exception to Junit.
     */
    public void testRenameFileForNullPointerException_ID()
        throws Exception {
        try {
            String fileId = null;
            String filename = "filename5";
            registry.renameFile(fileId, filename);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>renameFile(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if filename is null.
     *
     * @throws Exception to Junit.
     */
    public void testRenameFileForNullPointerException_Name()
        throws Exception {
        try {
            String fileId = "1004";
            String filename = null;
            registry.renameFile(fileId, filename);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>renameFile(String, String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if fileid is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRenameFileForIllegalArgumentException_ID()
        throws Exception {
        try {
            String fileId = "  ";
            String filename = "filename5";
            registry.renameFile(fileId, filename);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>renameFile(String, String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if filename is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRenameFileForIllegalArgumentException_Name()
        throws Exception {
        try {
            String fileId = "1004";
            String filename = "  ";
            registry.renameFile(fileId, filename);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>renameFile(String, String)</code> method for failure. <code>FileIdNotFoundException</code> should be
     * thrown if fileid already exists.
     *
     * @throws Exception to Junit.
     */
    public void testRenameFileForFileIdNotFoundException()
        throws Exception {
        try {
            String fileId = "1005";
            String filename = "filename5";
            registry.renameFile(fileId, filename);
            fail("the FileIdNotFoundException should be thrown!");
        } catch (FileIdNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFile(String)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileForNullPointerException()
        throws Exception {
        try {
            registry.removeFile(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFile(String)</code> method for failure. <code>IllegalArgumentException</code> should be thrown
     * if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileForIllegalArgumentException()
        throws Exception {
        try {
            registry.removeFile(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFile(String)</code> method for failure. <code>FileIdNotFoundException</code> should be thrown
     * if fileid does not exist.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileForFileIdNotFoundException()
        throws Exception {
        try {
            registry.removeFile("12345");
            fail("the FileIdNotFoundException should be thrown!");
        } catch (FileIdNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>getFile(String)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileForNullPointerException() throws Exception {
        try {
            registry.getFile(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getFile(String)</code> method for failure. <code>IllegalArgumentException</code> should be thrown if
     * string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileForIllegalArgumentException()
        throws Exception {
        try {
            registry.getFile(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>getFile(String)</code> method for failure. <code>FileIdNotFoundException</code> should be thrown if
     * fileid does not exist.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileForFileIdNotFoundException()
        throws Exception {
        try {
            registry.getFile("12345");
            fail("the FileIdNotFoundException should be thrown!");
        } catch (FileIdNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>createGroup(String, List)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if groupname is null.
     *
     * @throws Exception to Junit.
     */
    public void testCreateGroupForNullPointerException_Name()
        throws Exception {
        try {
            String groupName = null;
            List fileIds = new ArrayList();
            fileIds.add("12345");
            registry.createGroup(groupName, fileIds);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>createGroup(String, List)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if list is null.
     *
     * @throws Exception to Junit.
     */
    public void testCreateGroupForNullPointerException_list()
        throws Exception {
        try {
            String groupName = "failureGroup";
            List fileIds = null;
            registry.createGroup(groupName, fileIds);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>createGroup(String, List)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testCreateGroupForIllegalArgumentException()
        throws Exception {
        try {
            String groupName = "  ";
            List fileIds = new ArrayList();
            fileIds.add("12345");
            registry.createGroup(groupName, fileIds);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>createGroup(String, List)</code> method for failure. <code>GroupExistsException</code> should be
     * thrown if group exists.
     *
     * @throws Exception to Junit.
     */
    public void testCreateGroupForGroupExistsException()
        throws Exception {
        try {
            String groupName = "group1";
            List fileIds = new ArrayList();
            fileIds.add("12345");
            registry.createGroup(groupName, fileIds);
            fail("the GroupExistsException should be thrown!");
        } catch (GroupExistsException e) {
            // Good
        }
    }

    /**
     * Test <code>createGroup(String, List)</code> method for failure. <code>FileIdNotFoundException</code> should be
     * thrown if fileid does not exist.
     *
     * @throws Exception to Junit.
     */
    public void testCreateGroupForFileIdNotFoundException()
        throws Exception {
        try {
            String groupName = "failureGroup";
            List fileIds = new ArrayList();
            fileIds.add("12345");
            registry.createGroup(groupName, fileIds);
            fail("the FileIdNotFoundException should be thrown!");
        } catch (FileIdNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>updateGroup(String, List)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if groupname is null.
     *
     * @throws Exception to Junit.
     */
    public void testUpdateGroupForNullPointerException_Name()
        throws Exception {
        try {
            String groupName = null;
            List fileIds = new ArrayList();
            fileIds.add("12345");
            registry.updateGroup(groupName, fileIds);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>updateGroup(String, List)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if List is null.
     *
     * @throws Exception to Junit.
     */
    public void testUpdateGroupForNullPointerException_List()
        throws Exception {
        try {
            String groupName = "group1";
            List fileIds = null;
            registry.updateGroup(groupName, fileIds);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>updateGroup(String, List)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testUpdateGroupForIllegalArgumentException()
        throws Exception {
        try {
            String groupName = "  ";
            List fileIds = new ArrayList();
            fileIds.add("12345");
            registry.updateGroup(groupName, fileIds);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>updateGroup(String, List)</code> method for failure. <code>GroupNotFoundException</code> should be
     * thrown of group does not exist.
     *
     * @throws Exception to Junit.
     */
    public void testUpdateGroupForGroupNotFoundException()
        throws Exception {
        try {
            String groupName = "failureGroup";
            List fileIds = new ArrayList();
            fileIds.add("12345");
            registry.updateGroup(groupName, fileIds);
            fail("the GroupNotFoundException should be thrown!");
        } catch (GroupNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>updateGroup(String, List)</code> method for failure. <code>FileIdNotFoundException</code> should be
     * thrown if fileid does not exist.
     *
     * @throws Exception to Junit.
     */
    public void testUpdateGroupForFileIdNotFoundException()
        throws Exception {
        try {
            String groupName = "group1";
            List fileIds = new ArrayList();
            fileIds.add("12345");
            registry.updateGroup(groupName, fileIds);
            fail("the FileIdNotFoundException should be thrown!");
        } catch (FileIdNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>removeGroup(String)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveGroupForNullPointerException()
        throws Exception {
        try {
            registry.removeGroup(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>removeGroup(String)</code> method for failure. <code>IllegalArgumentException</code> should be thrown
     * if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveGroupForIllegalArgumentException()
        throws Exception {
        try {
            registry.removeGroup(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>removeGroup(String)</code> method for failure. <code>GroupNotFoundException</code> should be thrown
     * if group name is not found.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveGroupForGroupNotFoundException()
        throws Exception {
        try {
            registry.removeGroup("failureGroup");
            fail("the GroupNotFoundException should be thrown!");
        } catch (GroupNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>getGroupFiles(String)</code> method for failure. <code>NullPointerException</code> should be thrown
     * if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetGroupFilesForNullPointerException()
        throws Exception {
        try {
            registry.getGroupFiles(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getGroupFiles(String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testGetGroupFilesForIllegalArgumentException()
        throws Exception {
        try {
            registry.getGroupFiles(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>getGroupFiles(String)</code> method for failure. <code>GroupNotFoundException</code> should be thrown
     * if the group cannot be found.
     *
     * @throws Exception to Junit.
     */
    public void testGetGroupFilesForGroupNotFoundException()
        throws Exception {
        try {
            registry.getGroupFiles("failuregroup");
            fail("the GroupNotFoundException should be thrown!");
        } catch (GroupNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if groupname is null.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForNullPointerException_Name()
        throws Exception {
        try {
            String groupName = null;
            String fileId = "1001";
            registry.addFileToGroup(groupName, fileId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if fileid is null.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForNullPointerException_ID()
        throws Exception {
        try {
            String groupName = "group1";
            String fileId = null;
            registry.addFileToGroup(groupName, fileId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForIllegalArgumentException_Groupname()
        throws Exception {
        try {
            String groupName = "  ";
            String fileId = "1001";
            registry.addFileToGroup(groupName, fileId);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForIllegalArgumentException_ID()
        throws Exception {
        try {
            String groupName = "group1";
            String fileId = "  ";
            registry.addFileToGroup(groupName, fileId);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>GroupNotFoundException</code> should
     * be thrown if group not found.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForGroupNotFoundException()
        throws Exception {
        try {
            String groupName = "failureGroup";
            String fileId = "1004";
            registry.addFileToGroup(groupName, fileId);
            fail("the GroupNotFoundException should be thrown!");
        } catch (GroupNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileToGroup(String, String)</code> method for failure. <code>FileIdNotFoundException</code> should
     * be thrown if fileid not found.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileToGroupForFileIdNotFoundException()
        throws Exception {
        try {
            String groupName = "group1";
            String fileId = "1234";
            registry.addFileToGroup(groupName, fileId);
            fail("the FileIdNotFoundException should be thrown!");
        } catch (FileIdNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForNullPointerException_Name()
        throws Exception {
        try {
            String groupName = null;
            String fileId = "1002";
            registry.removeFileFromGroup(groupName, fileId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForNullPointerException_ID()
        throws Exception {
        try {
            String groupName = "group1";
            String fileId = null;
            registry.removeFileFromGroup(groupName, fileId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForIllegalArgumentException_Name()
        throws Exception {
        try {
            String groupName = "  ";
            String fileId = "1002";
            registry.removeFileFromGroup(groupName, fileId);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForIllegalArgumentException_ID()
        throws Exception {
        try {
            String groupName = "group1";
            String fileId = "  ";
            registry.removeFileFromGroup(groupName, fileId);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>GroupNotFoundException</code>
     * should be thrown if group name not found.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForGroupNotFoundException()
        throws Exception {
        try {
            String groupName = "failuregroup";
            String fileId = "1002";
            registry.removeFileFromGroup(groupName, fileId);
            fail("the GroupNotFoundException should be thrown!");
        } catch (GroupNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileFromGroup(String, String)</code> method for failure. <code>FileIdNotFoundException</code>
     * should be thrown if fileid not found.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileFromGroupForFileIdNotFoundException()
        throws Exception {
        try {
            String groupName = "group1";
            String fileId = "2002";
            registry.removeFileFromGroup(groupName, fileId);
            fail("the FileIdNotFoundException should be thrown!");
        } catch (FileIdNotFoundException e) {
            // Good
        }
    }
}
