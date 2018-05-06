/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.topcoder.file.transfer.ConfigHelper;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests functionality of FileSystemXmlRegistry. All consturctors and methods are tested.
 * </p>
 *
 * @author FireIce
 * @version 1.0
 */
public class FileSystemXmlRegistryTestCase extends TestCase {

    /**
     * Represents the fileId used in tests.
     */
    private static final String FILE_ID = "12345";

    /**
     * Represents the fileName used in tests.
     */
    private static final String FILE_NAME = "filename";

    /**
     * Represents the groupName used in tests.
     */
    private static final String GROUP_NAME = "group100";

    /**
     * Represents the file location for the files' xml file.
     */
    private static final String FILES_FILE_LOCATION = "test_files/files.xml";

    /**
     * Represents the file location for the groups' xml file.
     */
    private static final String GROUPS_FILE_LOCATION = "test_files/groups.xml";

    /**
     * Represents the invalid files xml file.
     */
    private static final String INVALID_FILES = "test_files/invalid_files.xml";

    /**
     * Represents the invalid groups xml file.
     */
    private static final String INVALID_GROUPS = "test_files/invalid_groups.xml";

    /**
     * Represents the valid files xml file.
     */
    private static final String VALID_FILES = "test_files/valid_files.xml";

    /**
     * Represents the valid groups xml file.
     */
    private static final String VALID_GROUPS = "test_files/valid_groups.xml";

    /**
     * Represents the files' xml file.
     */
    private static final File FILES_FILE = new File(FILES_FILE_LOCATION);

    /**
     * Represents the groups' xml file.
     */
    private static final File GROUPSFILE = new File(GROUPS_FILE_LOCATION);

    /**
     * Rerepsents the IDGenerator instance.
     */
    private IDGenerator idGenerator;

    /**
     * Represents the FileSystemXmlRegistry instance used in tests.
     */
    private FileSystemXmlRegistry registry;

    /**
     * Represents the fileId list used in tests.
     */
    private List fileIds;

    /**
     * <p>
     * Sets up the test environment. The test instances are created.
     * </p>
     *
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void setUp() throws Exception {
        idGenerator = IDGeneratorFactory.getIDGenerator(ConfigHelper.IDGENERATOR_NAMESPACE);
        registry = new FileSystemXmlRegistry(VALID_FILES, VALID_GROUPS, idGenerator);
        fileIds = createFileIdList();
    }

    /**
     * <p>
     * Cleans up the test environment. The test instances are disposed.
     * </p>
     *
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void tearDown() throws Exception {
        registry = null;
        idGenerator = null;
        fileIds.clear();
        fileIds = null;
    }

    /**
     * create a file id list used for testing.
     *
     * @return the file id list
     */
    private List createFileIdList() {
        List list = new ArrayList();
        list.add("1001");
        list.add("1002");
        return list;
    }

    /**
     * Test implementation.
     */
    public void testImplementation() {
        assertTrue("this class should implement the FileSystemRegistry interface",
                registry instanceof FileSystemRegistry);
    }

    /**
     * Test <code>FileSystemXmlRegistry(String, String, IDGenerator)</code> constructor, if any argument is null,
     * throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCtorStringStringIDGeneratorNPE() throws Exception {
        try {
            new FileSystemXmlRegistry(null, GROUPS_FILE_LOCATION, idGenerator);
            fail("if filesFileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemXmlRegistry(FILES_FILE_LOCATION, null, idGenerator);
            fail("if groupsFileLocation is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemXmlRegistry(FILES_FILE_LOCATION, GROUPS_FILE_LOCATION, null);
            fail("if idGenerator is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(String, String, IDGenerator)</code> constructor, if any string argument is
     * empty, throw IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCtorStringStringIDGeneratorIAE() throws Exception {
        try {
            new FileSystemXmlRegistry(" ", GROUPS_FILE_LOCATION, idGenerator);
            fail("if filesFileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new FileSystemXmlRegistry(FILES_FILE_LOCATION, " ", idGenerator);
            fail("if groupsFileLocation is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(String, String, IDGenerator)</code> constructor, if there is an exception
     * while initializing the instance, throw RegistryConfigurationException.
     */
    public void testCtorStringStringIDGeneratorRCE() {
        try {
            new FileSystemXmlRegistry(INVALID_FILES, VALID_GROUPS, idGenerator);
            fail("if filesFileLocation point to an invalid xml file, throw RegistryConfigurationException");
        } catch (RegistryConfigurationException e) {
            // good
        }
        try {
            new FileSystemXmlRegistry(VALID_FILES, INVALID_GROUPS, idGenerator);
            fail("if groupsFileLocation point to an invalid xml file, throw RegistryConfigurationException");
        } catch (RegistryConfigurationException e) {
            // good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(String, String, IDGenerator)</code> constructor, while providing correct
     * arguments.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testCtorStringStringIDGeneratorSuccess() throws Exception {
        new FileSystemXmlRegistry(VALID_FILES, VALID_GROUPS, idGenerator);
    }

    /**
     * Test <code>FileSystemXmlRegistry(File, File, IDGenerator)</code> constructor, if any argument is null, throw
     * NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCtorFileFileIDGeneratorNPE() throws Exception {
        try {
            new FileSystemXmlRegistry(null, GROUPSFILE, idGenerator);
            fail("if filesFile is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemXmlRegistry(FILES_FILE, null, idGenerator);
            fail("if groupsFile is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new FileSystemXmlRegistry(FILES_FILE, GROUPSFILE, null);
            fail("if idGenerator is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(File, File, IDGenerator)</code> constructor, if there is an exception while
     * initializing the instance, throw RegistryConfigurationException.
     */
    public void testCtorFileFIleIDGeneratorRCE() {
        try {
            new FileSystemXmlRegistry(new File(INVALID_FILES), GROUPSFILE, idGenerator);
            fail("if filesFile is invalid xml file, throw RegistryConfigurationException");
        } catch (RegistryConfigurationException e) {
            // good
        }
        try {
            new FileSystemXmlRegistry(FILES_FILE, new File(INVALID_GROUPS), idGenerator);
            fail("if groupsFile is invalid xml file, throw RegistryConfigurationException");
        } catch (RegistryConfigurationException e) {
            // good
        }
    }

    /**
     * Test <code>FileSystemXmlRegistry(File, File, IDGenerator)</code> constructor, while providing correct
     * arguments.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testCtorFileFIleIDGeneratorSuccess() throws Exception {
        new FileSystemXmlRegistry(FILES_FILE, GROUPSFILE, idGenerator);
    }

    /**
     * Tests <code>getNextFileId()</code> method, if no exception thrown, return the next file id.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetNextFileIdSuccess() throws Exception {
        String nextFileId = registry.getNextFileId();
        assertNotNull("should not be null", nextFileId);
        assertFalse("should not be trimmed empty", nextFileId.trim().length() == 0);
        assertFalse("should not be unique", registry.getNextFileId() == registry.getNextFileId());
    }

    /**
     * Tests <code>addFile(String, String)</code> method, if any argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileNPE() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.addFile(null, FILE_NAME);
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            registry.addFile(FILE_ID, null);
            fail("if fileName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Tests <code>addFile(String, String)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileIAE() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.addFile(" ", FILE_NAME);
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            registry.addFile(FILE_ID, "  ");
            fail("if fileName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests <code>addFile(String, String)</code> method, if the file id already exists, throw
     * FileIdExistsException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileFileIdExistsException() throws Exception {
        assertNotNull("setup fails", registry);
        registry.addFile(FILE_ID, FILE_NAME);
        try {
            registry.addFile(FILE_ID, FILE_NAME);
            fail("if the file id already exists, throw FileIdExistsException");
        } catch (FileIdExistsException e) {
            // good
        }
        registry.removeFile(FILE_ID);
    }

    /**
     * Tests <code>addFile(String, String)</code> method, when it successfully added.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileSuccess() throws Exception {
        assertNotNull("setup fails", registry);
        assertFalse("the file id shouldn't exist", registry.getFileIds().contains(FILE_ID));
        registry.addFile(FILE_ID, FILE_NAME);
        assertTrue("the file id should exist", registry.getFileIds().contains(FILE_ID));
        registry.removeFile(FILE_ID);
    }

    /**
     * Tests <code>renameFile(String, String)</code> method, if any argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRenameFileNPE() throws Exception {
        assertNotNull("setup fails", registry);
        registry.addFile(FILE_ID, FILE_NAME);
        try {
            registry.renameFile(null, FILE_NAME);
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            registry.renameFile(FILE_ID, null);
            fail("if fileName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        registry.removeFile(FILE_ID);
    }

    /**
     * Tests <code>renameFile(String, String)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRenameFileIAE() throws Exception {
        assertNotNull("setup fails", registry);
        registry.addFile(FILE_ID, FILE_NAME);
        try {
            registry.renameFile(" ", FILE_NAME);
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            registry.renameFile(FILE_ID, "  ");
            fail("if fileName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        registry.removeFile(FILE_ID);
    }

    /**
     * Tests <code>renameFile(String, String)</code> method, if the file id not exists, throw
     * FileIdNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRenameFileFileIdNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        assertFalse("the file id shouldn't exist", registry.getFileIds().contains(FILE_ID));
        try {
            registry.renameFile(FILE_ID, FILE_NAME);
            fail("if the file id not exist, throw FileIdNotFoundException");
        } catch (FileIdNotFoundException e) {
            // good
        }
    }

    /**
     * Tests <code>renameFile(String, String)</code> method, when it successfully added.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRenameFileSuccess() throws Exception {
        assertNotNull("setup fails", registry);
        assertFalse("the file id shouldn't exist", registry.getFileIds().contains(FILE_ID));
        registry.addFile(FILE_ID, "newFileName");
        assertTrue("the file id should exist", registry.getFileIds().contains(FILE_ID));
        registry.renameFile(FILE_ID, FILE_NAME);
        assertEquals("the file name should be the new one", FILE_NAME, registry.getFile(FILE_ID));
        registry.removeFile(FILE_ID);
    }

    /**
     * Tests <code>removeFile(String)</code> method, if the argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileNPE() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.removeFile(null);
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Tests <code>removeFile(String)</code> method, if the string argument is empty, throw
     * IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileIAE() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.removeFile(" ");
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests <code>removeFile(String)</code> method, if the file id not exists, throw FileIdNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileFileIdNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        assertFalse("the file id shouldn't exist", registry.getFileIds().contains(FILE_ID));
        try {
            registry.removeFile(FILE_ID);
            fail("if the file id not exist, throw FileIdNotFoundException");
        } catch (FileIdNotFoundException e) {
            // good
        }
    }

    /**
     * Tests <code>removeFile(String)</code> method, when it successfully added.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileSuccess() throws Exception {
        assertNotNull("setup fails", registry);
        assertFalse("the file id shouldn't exist", registry.getFileIds().contains(FILE_ID));
        registry.addFile(FILE_ID, "newFileName");
        assertTrue("the file id should exist", registry.getFileIds().contains(FILE_ID));
        registry.removeFile(FILE_ID);
        assertFalse("the file id shouldn't exist", registry.getFileIds().contains(FILE_ID));
    }

    /**
     * Tests <code>getFileIds()</code> method, return a shallow copy.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetFileIds() throws Exception {
        assertNotNull("setup fails", registry);
        registry.addFile(FILE_ID, FILE_NAME);
        assertNotNull("shouldn't return null", registry.getFileIds());
        assertTrue("should contain the newly added fileId", registry.getFileIds().contains(FILE_ID));
        assertNotSame("should always return a shallow copy", registry.getFileIds(), registry.getFileIds());
        registry.removeFile(FILE_ID);
    }

    /**
     * Tests <code>getFiles()</code> method, return a shallow copy.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testGetFiles() throws Exception {
        assertNotNull("setup fails", registry);
        registry.addFile(FILE_ID, FILE_NAME);
        Map files = registry.getFiles();
        assertNotNull("shouldn't return null", files);
        assertTrue("should contain the newly added fileId", files.containsKey(FILE_ID)
                && files.containsValue(FILE_NAME));
        assertNotSame("should always return a shallow copy", registry.getFileIds(), registry.getFileIds());
        registry.removeFile(FILE_ID);
    }

    /**
     * Tests <code>createGroup(String, List)</code> method, if any argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCreateGroupNPE() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.createGroup(null, fileIds);
            fail("if groupName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            registry.createGroup(GROUP_NAME, null);
            fail("if fileIds is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Tests <code>createGroup(String, List)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCreateGroupIAE() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        try {
            registry.createGroup(" ", fileIds);
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>createGroup(String, List)</code> method, if a file id cannot be found, throw
     * FileIdNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCreateGroupFileIdNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        assertFalse("the groupName shouldn't exist", registry.getGroupNames().contains(GROUP_NAME));
        fileIds.add("notExistId");
        try {
            registry.createGroup(GROUP_NAME, fileIds);
            fail("if a file id cannot be found, throw FileIdNotFoundException");
        } catch (FileIdNotFoundException e) {
            // good
        }
    }

    /**
     * Tests <code>createGroup(String, List)</code> method, if the group name already exists, throw
     * GroupExistsException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCreateGroupGroupExistsException() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        assertTrue("the groupName should exist", registry.getGroupNames().contains(GROUP_NAME));
        try {
            registry.createGroup(GROUP_NAME, fileIds);
            fail("if the group name already exists, throw GroupExistsException");
        } catch (GroupExistsException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>createGroup(String, List)</code> method, when it successfully added.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testCreateGroupSuccess() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        assertTrue("the groupName should exist", registry.getGroupNames().contains(GROUP_NAME));
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>updateGroup(String, String)</code> method, if any argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUpdateGroupNPE() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        try {
            registry.updateGroup(null, fileIds);
            fail("if groupName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            registry.updateGroup(GROUP_NAME, null);
            fail("if fileIds is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>updateGroup(String, String)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUpdateGroupIAE() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        try {
            registry.updateGroup(" ", fileIds);
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>updateGroup(String, String)</code> method, if a file id cannot be found, throw
     * FileIdNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUpdateGroupFileIdNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        assertTrue("the groupName should exist", registry.getGroupNames().contains(GROUP_NAME));
        fileIds.add("notExistFileId");
        try {
            registry.updateGroup(GROUP_NAME, fileIds);
            fail("if a file id cannot be found, throw FileIdNotFoundException");
        } catch (FileIdNotFoundException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
        assertFalse("the groupName shouldn't exist", registry.getGroupNames().contains(GROUP_NAME));
    }

    /**
     * Tests <code>updateGroup(String, String)</code> method, if the group name does not exists, throw
     * GroupNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUpdateGroupGroupNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.updateGroup(GROUP_NAME, fileIds);
            fail("if the group name doesn't exists, throw GroupNotFoundException");
        } catch (GroupNotFoundException e) {
            // good
        }
    }

    /**
     * Tests <code>updateGroup(String, String)</code> method, when it successfully added.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testUpdateGroupSuccess() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        List idList = new ArrayList();
        idList.add("1003");
        registry.updateGroup(GROUP_NAME, idList);
        assertEquals("the fileIds list should contains only one item", 1, registry.getGroupFiles(GROUP_NAME).size());
        assertTrue("the file id should exist", registry.getGroupFiles(GROUP_NAME).contains("1003"));
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>removeGroup(String)</code> method, if the argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveGroupNPE() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.removeGroup(null);
            fail("if groupName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Tests <code>removeGroup(String)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveGroupIAE() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.removeGroup(" ");
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Tests <code>removeGroup(String)</code> method, if the group name does not exists, throw
     * GroupNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveGroupGroupNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.removeGroup(GROUP_NAME);
            fail("if the group name doesn't exists, throw GroupNotFoundException");
        } catch (GroupNotFoundException e) {
            // good
        }
    }

    /**
     * Tests <code>removeGroup(String)</code> method, when it successfully added.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveGroupSuccess() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        assertTrue("the group name should exist", registry.getGroupNames().contains(GROUP_NAME));
        registry.removeGroup(GROUP_NAME);
        assertFalse("the group name shouldn't exist", registry.getGroupNames().contains(GROUP_NAME));
    }

    /**
     * Tests <code>getGroupNames()</code> method, return a group name list.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetGroupNames() throws Exception {
        assertNotNull("setup fails", registry);
        List groupNameList = registry.getGroupNames();
        assertNotNull("the method should never return null", groupNameList);
        assertNotSame("always return a shallow copy", registry.getGroupNames(), registry.getGroupNames());
        registry.createGroup(GROUP_NAME, new ArrayList());
        assertTrue("should contains the newly created group", registry.getGroupNames().contains(GROUP_NAME));
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>getGroupFiles()</code> method, if the argument is null, throw NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetGroupFilesNPE() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.getGroupFiles(null);
            fail("if the argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Tests <code>getGroupFiles()</code> method, if the argument is empty, throw IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetGroupFilesIAE() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.getGroupFiles(" ");
            fail("if the argument is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Tests <code>getGroupFiles()</code> method, if the group name cannot be found, throw GroupNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetGroupFilesGroupNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        try {
            registry.getGroupFiles(GROUP_NAME);
            fail("if the group name cannot be found, throw GroupNotFoundException");
        } catch (GroupNotFoundException e) {
            // Good
        }
    }

    /**
     * Tests <code>getGroupFiles()</code> method, if the group name found, return the fileId list.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testGetGroupFilesSuccess() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        assertNotNull("should never return null", registry.getGroupFiles(GROUP_NAME));
        assertNotSame("always return a shallow copy", registry.getGroupFiles(GROUP_NAME), registry
                .getGroupFiles(GROUP_NAME));
        registry.addFile(FILE_ID, FILE_NAME);
        fileIds.clear();
        fileIds.add(FILE_ID);
        registry.updateGroup(GROUP_NAME, fileIds);
        assertTrue("should contains the file id", registry.getGroupFiles(GROUP_NAME).contains(FILE_ID));
        registry.removeGroup(GROUP_NAME);
        registry.removeFile(FILE_ID);
    }

    /**
     * Tests <code>addFileToGroup(String, String)</code> method, if any argument is null, throw
     * NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileToGroupNPE() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        try {
            registry.addFileToGroup(null, FILE_ID);
            fail("if groupName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            registry.addFileToGroup(GROUP_NAME, null);
            fail("if fileIds is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>addFileToGroup(String, String)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileToGroupIAE() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        try {
            registry.addFileToGroup(" ", FILE_ID);
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            registry.addFileToGroup(GROUP_NAME, " ");
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>addFileToGroup(String, String)</code> method, if a file id cannot be found, throw
     * FileIdNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileToGroupFileIdNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        try {
            registry.addFileToGroup(GROUP_NAME, FILE_ID);
            fail("if a file id cannot be found, throw FileIdNotFoundException");
        } catch (FileIdNotFoundException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>addFileToGroup(String, String)</code> method, if the group name already exists, throw
     * GroupNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileToGroupGroupNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        assertFalse("the groupName shouldn't exist", registry.getGroupNames().contains(GROUP_NAME));
        try {
            registry.addFileToGroup(GROUP_NAME, FILE_ID);
            fail("if the group name cannot be found, throw GroupNotFoundException");
        } catch (GroupNotFoundException e) {
            // good
        }
    }

    /**
     * Tests <code>addFileToGroup(String, String)</code> method, when it successfully added.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testAddFileToGroupSuccess() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        registry.addFile(FILE_ID, FILE_NAME);
        registry.addFileToGroup(GROUP_NAME, FILE_ID);
        assertTrue("should contain the fileId now", registry.getGroupFiles(GROUP_NAME).contains(FILE_ID));
        registry.removeGroup(GROUP_NAME);
        registry.removeFile(FILE_ID);
    }

    /**
     * Tests <code>removeFileFromGroup(String, String)</code> method, if any argument is null, throw
     * NullPointerException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileFromGroupNPE() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        try {
            registry.removeFileFromGroup(null, FILE_ID);
            fail("if groupName is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            registry.removeFileFromGroup(GROUP_NAME, null);
            fail("if fileId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>removeFileFromGroup(String, String)</code> method, if any string argument is empty, throw
     * IllegalArgumentException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileFromGroupIAE() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        try {
            registry.removeFileFromGroup(" ", FILE_ID);
            fail("if groupName is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            registry.removeFileFromGroup(GROUP_NAME, " ");
            fail("if fileId is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>removeFileFromGroup(String, String)</code> method, if a file id cannot be found, throw
     * FileIdNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileFromGroupFileIdNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        try {
            registry.removeFileFromGroup(GROUP_NAME, FILE_ID);
            fail("if a file id cannot be found, throw FileIdNotFoundException");
        } catch (FileIdNotFoundException e) {
            // good
        }
        registry.removeGroup(GROUP_NAME);
    }

    /**
     * Tests <code>removeFileFromGroup(String, String)</code> method, if the group name already exists, throw
     * GroupNotFoundException.
     *
     * @throws Exception
     *             if any other exception occur.
     */
    public void testRemoveFileFromGroupGroupNotFoundException() throws Exception {
        assertNotNull("setup fails", registry);
        assertFalse("the groupName shouldn't exist", registry.getGroupNames().contains(GROUP_NAME));
        try {
            registry.removeFileFromGroup(GROUP_NAME, FILE_ID);
            fail("if the group name cannot be found, throw GroupNotFoundException");
        } catch (GroupNotFoundException e) {
            // good
        }
    }

    /**
     * Tests <code>removeFileFromGroup(String, String)</code> method, when it successfully added.
     *
     * @throws Exception
     *             if any exception occur.
     */
    public void testRemoveFileFromGroupSuccess() throws Exception {
        assertNotNull("setup fails", registry);
        registry.createGroup(GROUP_NAME, new ArrayList());
        registry.addFile(FILE_ID, FILE_NAME);
        assertFalse("should not contain the fileId yet", registry.getGroupFiles(GROUP_NAME).contains(FILE_ID));
        registry.addFileToGroup(GROUP_NAME, FILE_ID);
        assertTrue("should contain the fileId now", registry.getGroupFiles(GROUP_NAME).contains(FILE_ID));
        registry.removeFileFromGroup(GROUP_NAME, FILE_ID);
        assertFalse("should not contain the fileId yet", registry.getGroupFiles(GROUP_NAME).contains(FILE_ID));
        registry.removeGroup(GROUP_NAME);
        registry.removeFile(FILE_ID);
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     *
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(FileSystemXmlRegistryTestCase.class);
    }
}
