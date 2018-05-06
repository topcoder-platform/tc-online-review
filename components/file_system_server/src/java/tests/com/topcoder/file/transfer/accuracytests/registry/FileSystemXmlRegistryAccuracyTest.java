/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.registry;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.topcoder.file.transfer.accuracytests.AccuracyTestHelper;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.TestCase;

/**
 * <p>
 * Accuracy Test for FileSystemXmlRegistry class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class FileSystemXmlRegistryAccuracyTest extends TestCase {
    /**
     * Represents the valid files xml file.
     */
    public static final String FILES_FILE = "test_files/accuracy/files/files.xml";

    /**
     * Represents the valid groups xml file.
     */
    public static final String GROUPS_FILE = "test_files/accuracy/files/groups.xml";

    /**
     * Represents a temporary files xml file.
     */
    private static final String FILES_FILE_TEMP = "test_files/accuracy/files/tmpfiles.xml";

    /**
     * Represents a temporary groups xml file.
     */
    private static final String GROUPS_FILE_TEMP = "test_files/accuracy/files/tmpgroups.xml";

    /**
     * Rerepsents an instance of IDGenerator.
     */
    private IDGenerator idGenerator;

    /**
     * Represents an instance of FileSystemXmlRegistry.
     */
    private FileSystemXmlRegistry registry;

    /**
     * <p>
     * Represents the files map holding file id and file name.
     * </p>
     */
    private Map files = new Hashtable();

    /**
     * <p>
     * Represents the group map holding group name and a list of file ids.
     * </p>
     */
    private final Map groups = new HashMap();

    /**
     * <p>
     * Set up for each test.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        AccuracyTestHelper.addConfigFiles();
        files.clear();
        files.put("1", "file1.txt");
        files.put("2", "file2.txt");
        files.put("3", "file3.txt");
        files.put("4", "file4.jpg");
        files.put("5", "file5.jpg");
        files.put("6", "file6.dat");
        files.put("7", "file7.dat");

        groups.clear();
        List fileIds = new ArrayList();
        fileIds.add("1");
        fileIds.add("2");
        fileIds.add("3");
        fileIds.add("4");
        groups.put("group1", fileIds);
        groups.put("group2", new ArrayList());

        fileIds = new ArrayList();
        fileIds.add("4");
        fileIds.add("5");
        groups.put("group3", fileIds);

        idGenerator = IDGeneratorFactory
                .getIDGenerator(AccuracyTestHelper.IDGENERATOR_NAMESPACE);
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, FILES_FILE_TEMP,
                idGenerator);
    }

    /**
     * <p>
     * Tear down for each test.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    protected void tearDown() throws Exception {
        List list = registry.getGroupNames();
        if (list != null) {
            Iterator itor = list.iterator();
            while (itor.hasNext()) {
                registry.removeGroup((String) itor.next());
            }
        }

        list = registry.getFileIds();
        if (list != null) {
            Iterator itor = list.iterator();
            while (itor.hasNext()) {
                registry.removeFile((String) itor.next());
            }
        }
        
        AccuracyTestHelper.removeConfigFiles();
        super.tearDown();
    }

    /**
     * <p>
     * Test ctor FileSystemXmlRegistry(String filesFileLocation, String
     * groupsFileLocation, IDGenerator idGenerator), an instance should be
     * created.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testCtor1() throws Exception {
        FileSystemXmlRegistry reg = new FileSystemXmlRegistry(FILES_FILE,
                GROUPS_FILE, idGenerator);
        assertNotNull("Failed to create instance of FileSystemXmlRegistry", reg);
        verifyRegistry(reg);
    }

    /**
     * <p>
     * Test FileSystemXmlRegistry(File filesFile, File groupsFile, IDGenerator
     * idGenerator), an instance will be created.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testCtor2() throws Exception {
        FileSystemXmlRegistry reg = new FileSystemXmlRegistry(new File(
                FILES_FILE), new File(GROUPS_FILE), idGenerator);
        assertNotNull("Failed to create instance of FileSystemXmlRegistry", reg);
        verifyRegistry(reg);
    }

    /**
     * <p>
     * Verify the registry is expected.
     * </p>
     * 
     * @param registry
     *            the registry to verify
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    private void verifyRegistry(FileSystemXmlRegistry registry)
            throws Exception {
        Map allFiles = registry.getFiles();
        assertEquals("the files is not correct", files.size(), allFiles.size());
        Iterator itor = allFiles.keySet().iterator();
        while (itor.hasNext()) {
            String fileId = (String) itor.next();
            assertTrue("the files is not correct", files.containsKey(fileId));
            assertEquals("the files is not correct", files.get(fileId),
                    allFiles.get(fileId));
        }

        List allGroups = registry.getGroupNames();
        assertEquals("the files is not correct", groups.size(), allGroups
                .size());
        itor = allGroups.iterator();
        while (itor.hasNext()) {
            String groupName = (String) itor.next();
            assertTrue("the files is not correct", groups
                    .containsKey(groupName));
            List fileIds1 = (List) groups.get(groupName);
            List fileIds2 = registry.getGroupFiles(groupName);
            assertEquals("the files is not correct", fileIds1.size(), fileIds2
                    .size());
            Iterator idItor = fileIds1.iterator();
            while (idItor.hasNext()) {
                assertTrue("the files is not correct", fileIds2.contains(idItor
                        .next()));
            }
        }
    }

    /**
     * <p>
     * Test getNextFileId(), each time it should return a unique file id.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetNextFileId() throws Exception {
        String id1 = registry.getNextFileId();
        assertFalse("The file id should be unique.", id1.equals(registry
                .getNextFileId()));
    }

    /**
     * <p>
     * Test addFile(String fileId, String fileName), a file with the id should
     * be added to the source.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testAddFile() throws Exception {
        String id1 = registry.getNextFileId();
        registry.addFile(id1, "1.txt");
        files.clear();
        groups.clear();
        files.put(id1, "1.txt");

        verifyRegistry(registry);
    }

    /**
     * <p>
     * Test renameFile(String fileId, String fileName), the filename associated
     * with the fileid should be updated.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRenameFile() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        String id1 = registry.getNextFileId();
        registry.addFile(id1, "1.txt");
        files.clear();
        groups.clear();

        files.put(id1, "1.txt");
        verifyRegistry(registry);

        registry.renameFile(id1, "2.txt");
        files.put(id1, "2.txt");
        verifyRegistry(registry);
    }

    /**
     * <p>
     * Test removeFile(String fileId), the filename associated with the fileid
     * should be updated.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRemoveFile() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        List ids1 = new ArrayList();
        files.clear();
        groups.clear();
        String id = null;
        for (int i = 0; i < 4; i++) {
            id = registry.getNextFileId();
            registry.addFile(id, id);
            files.put(id, id);
            ids1.add(id);
        }
        List ids2 = new ArrayList();
        ids2.add(id);

        registry.createGroup("Group1", ids1);
        groups.put("Group1", ids1);
        groups.put("Group2", ids2);
        registry.createGroup("Group2", ids2);
        verifyRegistry(registry);

        files.remove(id);
        ids1.remove(id);
        ids2.remove(id);
        groups.put("Group1", ids1);
        groups.put("Group2", ids2);
        registry.removeFile(id);
        verifyRegistry(registry);
    }

    /**
     * <p>
     * Test getFile(String fileId), the filename associated with the fileid
     * should be updated.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetFile() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        String id1 = registry.getNextFileId();
        registry.addFile(id1, "1.txt");
        assertEquals("Failed to get the correct file name", "1.txt", registry
                .getFile(id1));
    }

    /**
     * <p>
     * Test getFileIds(), it should return all the file ids.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetFileIds() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        String id1 = registry.getNextFileId();
        String id2 = registry.getNextFileId();
        registry.addFile(id1, id1);
        registry.addFile(id2, id2);

        List ids = registry.getFileIds();
        assertEquals("Failed to return the corrrect file ids", 2, ids.size());
        assertTrue("Failed to return the corrrect file ids", ids.contains(id1));
        assertTrue("Failed to return the corrrect file ids", ids.contains(id2));
    }

    /**
     * <p>
     * Test getFiles(), it should return all the file ids.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetFiles() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        String id1 = registry.getNextFileId();
        String id2 = registry.getNextFileId();
        registry.addFile(id1, "1.txt");
        registry.addFile(id2, "2.txt");

        Map returned = registry.getFiles();
        assertEquals("Failed to return the corrrect files", 2, returned.size());
        assertEquals("Failed to return the corrrect files", "1.txt", returned
                .get(id1));
        assertEquals("Failed to return the corrrect files", "2.txt", returned
                .get(id2));
    }

    /**
     * <p>
     * Test createGroup(String groupName, List fileIds), the group with file id
     * will be created.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testCreateGroup() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        String id1 = registry.getNextFileId();
        String id2 = registry.getNextFileId();

        files.clear();
        groups.clear();
        files.put(id1, "1.txt");
        files.put(id2, "2.txt");

        List fileIds = new ArrayList();
        fileIds.add(id1);
        groups.put("group1", fileIds);

        registry.addFile(id1, "1.txt");
        registry.addFile(id2, "2.txt");

        registry.createGroup("group1", fileIds);
        verifyRegistry(registry);
    }

    /**
     * <p>
     * Test updateGroup(String groupName, List fileIds), the group with file id
     * will be updated.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testUpdateGroup() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        String id1 = registry.getNextFileId();
        String id2 = registry.getNextFileId();

        files.clear();
        groups.clear();
        files.put(id1, "1.txt");
        files.put(id2, "2.txt");

        List fileIds = new ArrayList();
        fileIds.add(id1);
        groups.put("group1", fileIds);

        registry.addFile(id1, "1.txt");
        registry.addFile(id2, "2.txt");

        registry.createGroup("group1", fileIds);
        verifyRegistry(registry);

        fileIds.add(id2);
        registry.updateGroup("group1", fileIds);

        groups.put("group1", fileIds);
        verifyRegistry(registry);
    }

    /**
     * <p>
     * Test removeGroup(String groupName), the group with the name will be
     * removed.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRemoveGroup() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        String id1 = registry.getNextFileId();
        String id2 = registry.getNextFileId();

        files.clear();
        groups.clear();
        files.put(id1, "1.txt");
        files.put(id2, "2.txt");

        List fileIds = new ArrayList();
        fileIds.add(id1);
        groups.put("group1", fileIds);

        registry.addFile(id1, "1.txt");
        registry.addFile(id2, "2.txt");

        registry.createGroup("group1", fileIds);
        verifyRegistry(registry);

        fileIds.add(id2);
        registry.removeGroup("group1");

        groups.clear();
        verifyRegistry(registry);
    }

    /**
     * <p>
     * Test getGroupNames(), all group name should be returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetGroupNames() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        assertEquals("Failed to get the group names", 0, registry
                .getGroupNames().size());

        String id1 = registry.getNextFileId();
        String id2 = registry.getNextFileId();
        List fileIds = new ArrayList();
        fileIds.add(id1);
        groups.put("group1", fileIds);

        registry.addFile(id1, "1.txt");
        registry.addFile(id2, "2.txt");
        registry.createGroup("group1", fileIds);

        List names = registry.getGroupNames();
        assertEquals("Failed to get the group names", 1, names.size());
        assertEquals("Failed to get the group names", "group1", names.get(0));
    }

    /**
     * <p>
     * Test getGroupFiles(String groupName), all file in groups should be
     * returned.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testGetGroupFiles() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        assertEquals("Failed to get the group names", 0, registry
                .getGroupNames().size());

        String id1 = registry.getNextFileId();
        String id2 = registry.getNextFileId();
        List fileIds = new ArrayList();
        fileIds.add(id1);
        fileIds.add(id2);

        registry.addFile(id1, "1.txt");
        registry.addFile(id2, "2.txt");
        registry.createGroup("group1", fileIds);

        List files = registry.getGroupFiles("group1");
        assertEquals("Failed to get files of the group ", 2, files.size());
        assertTrue("Failed to get files of the group ", files.contains(id1));
        assertTrue("Failed to get files of the group ", files.contains(id2));
    }

    /**
     * <p>
     * Test addFileToGroup(String groupName, String fileId), the file should be
     * added to the group.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testAddFileToGroup() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        assertEquals("Failed to get the group names", 0, registry
                .getGroupNames().size());
        files.clear();
        groups.clear();

        String id1 = registry.getNextFileId();
        String id2 = registry.getNextFileId();
        List fileIds = new ArrayList();
        files.put(id1, "1.txt");
        files.put(id2, "2.txt");
        groups.put("group1", fileIds);

        registry.addFile(id1, "1.txt");
        registry.addFile(id2, "2.txt");
        registry.createGroup("group1", new ArrayList());
        verifyRegistry(registry);
        registry.addFileToGroup("group1", id1);
        registry.addFileToGroup("group1", id2);

        fileIds.add(id1);
        fileIds.add(id2);
        groups.put("group1", fileIds);
        verifyRegistry(registry);
    }

    /**
     * <p>
     * Test removeFileFromGroup(String groupName, String fileId), the file
     * should be removed from the group.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRemoveFileFromGroup() throws Exception {
        registry = new FileSystemXmlRegistry(FILES_FILE_TEMP, GROUPS_FILE_TEMP,
                idGenerator);
        assertEquals("Failed to get the group names", 0, registry
                .getGroupNames().size());
        files.clear();
        groups.clear();

        String id1 = registry.getNextFileId();
        String id2 = registry.getNextFileId();

        List fileIds = new ArrayList();
        fileIds.add(id1);
        fileIds.add(id2);

        files.put(id1, "1.txt");
        files.put(id2, "2.txt");
        groups.put("group1", fileIds);

        registry.addFile(id1, "1.txt");
        registry.addFile(id2, "2.txt");
        registry.createGroup("group1", fileIds);
        verifyRegistry(registry);

        registry.removeFileFromGroup("group1", id1);
        registry.removeFileFromGroup("group1", id2);
        groups.put("group1", new ArrayList());
        verifyRegistry(registry);
    }

    /**
     * <p>
     * This case will test all the operation in registry.
     * </p>
     * 
     * @throws Exception
     *             Exception to JUnit
     */
    public void testRegistry() throws Exception {
        Iterator itor = files.keySet().iterator();
        while (itor.hasNext()) {
            String fileId = (String) itor.next();
            registry.addFile(fileId, (String) files.get(fileId));
        }

        itor = groups.keySet().iterator();
        while (itor.hasNext()) {
            String groupName = (String) itor.next();
            registry.createGroup(groupName, (List) groups.get(groupName));
        }
        verifyRegistry(registry);

        registry.removeFile("5");
        registry.removeFileFromGroup("group3", "4");
        registry.removeGroup("group3");
        groups.remove("group3");
        files.remove("5");
        verifyRegistry(registry);

        registry.renameFile("1", "xxx");
        files.put("1", "xxx");
        verifyRegistry(registry);

        registry.addFileToGroup("group2", "1");
        List fileIds = new ArrayList();
        fileIds.add("1");
        groups.put("group2", fileIds);
        verifyRegistry(registry);
    }
}