/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.registry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;

/**
 * Represents an implementation of the FileSystemRegistry interface. It maintains a mapping between the file ids and
 * file names, and it maintains a list of groups and their associated files. The registry works with two XML files as a
 * persistent storage: one file is for the fileId-fileName mappings and one file is for the list of groups. It uses two
 * files because at the end of each method which alters a document field, the XML file is rewritten to persist the
 * changes, and saving only the files document or only the groups document takes less time. It is thread safe for atomic
 * method calls. All the methods are synchronized - this is required because some operations require several calls to
 * this instance and, in order to achieve transactional thread safety, the application should perform the calls inside a
 * synchronized block using this instance's lock. See Component Specification, Required Algorithms section for more
 * details about the structure of the xml files used by this class.
 * @author Luca, FireIce
 * @version 1.0
 */
public class FileSystemXmlRegistry implements FileSystemRegistry {

    /**
     * Represents the files xml file's file element name.
     */
    private static final String ELEMENT_FILES = "files";

    /**
     * Represents the groups xml file's root element name.
     */
    private static final String ELEMENT_GROUPS = "groups";

    /**
     * Represents file element name in the files xml file.
     */
    private static final String ELEMENT_FILE = "file";

    /**
     * Represents group element name in the groups xml file.
     */
    private static final String ELEMENT_GROUP = "group";

    /**
     * Represents the id attribute name of the file element.
     */
    private static final String ATTR_ID = "id";

    /**
     * Represents the name attribute name of the file element.
     */
    private static final String ATTR_NAME = "name";

    /**
     * Represents the DOM document used by this instance to work with the file mappings. Initialized in the constructor
     * and never changed later. Not null.
     */
    private final Document filesDocument;

    /**
     * Represents the DOM document used by this instance to work with the groups. Initialized in the constructor and
     * never changed later. Not null.
     */
    private final Document groupsDocument;

    /**
     * Represents the id generator used by this instance to generate unique ids for the files. Initialized in the
     * constructor and never changed later. Not null.
     */
    private final IDGenerator idGenerator;

    /**
     * Represents the xml file for the file mappings. Initialized in the constructor and never changed later. Not null.
     * Represents a valid file.
     */
    private final File filesFile;

    /**
     * Represents the xml file for the groups. Initialized in the constructor and never changed later. Not null.
     * Represents a valid file.
     */
    private final File groupsFile;

    /**
     * Represents the map used by this instance to work with the file mappings (fileId,fileName). The keys are of String
     * type, non-null, non-empty. The values are of String type, non-null, non-empty. Initialized in the constructor and
     * never changed later. Not null.
     */
    private final Map files = new HashMap();

    /**
     * Represents the map used by this instance to work with the groups (groupName,fileIds). The keys are of String
     * type, non-null, non-empty. The values are of List type, non-null, can be empty, with String elements (non-null,
     * non-empty). Initialized in the constructor and never changed later. Not null.
     */
    private final Map groups = new HashMap();

    /**
     * Creates an instance with the given arguments.
     * @param filesFileLocation
     *            the file location for the files' xml file
     * @param groupsFileLocation
     *            the file location for the groups' xml file
     * @param idGenerator
     *            the id generator
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws RegistryConfigurationException
     *             if ther is an exception while initializing the instance
     */
    public FileSystemXmlRegistry(String filesFileLocation, String groupsFileLocation, IDGenerator idGenerator)
        throws RegistryConfigurationException {
        this(new File(checkStr(filesFileLocation, "filesFileLocation")), new File(checkStr(groupsFileLocation,
                "groupsFileLocation")), idGenerator);
    }

    /**
     * Creates an instance with the given arguments.
     * @param filesFile
     *            the files' xml file
     * @param groupsFile
     *            the groups' xml file
     * @param idGenerator
     *            the id generator
     * @throws NullPointerException
     *             if any argument is empty
     * @throws RegistryConfigurationException
     *             if there is an exception while initializing the instance
     */
    public FileSystemXmlRegistry(File filesFile, File groupsFile, IDGenerator idGenerator)
        throws RegistryConfigurationException {
        if (filesFile == null) {
            throw new NullPointerException("filesFile is null");
        }
        if (groupsFile == null) {
            throw new NullPointerException("groupsFile is null");
        }
        if (idGenerator == null) {
            throw new NullPointerException("idGenerator is null");
        }
        this.filesFile = filesFile;
        this.groupsFile = groupsFile;
        this.idGenerator = idGenerator;
        // create document builder factory for making Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // create document builder
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RegistryConfigurationException("parser configuration fails", e);
        }
        try {
            if (filesFile.exists()) {
                // just parse it
                filesDocument = builder.parse(filesFile);
            } else {
                // create new one
                filesDocument = builder.newDocument();
                filesDocument.appendChild(filesDocument.createElement(ELEMENT_FILES));
            }
            if (groupsFile.exists()) {
                // parse it
                groupsDocument = builder.parse(groupsFile);
            } else {
                // create new one
                groupsDocument = builder.newDocument();
                groupsDocument.appendChild(groupsDocument.createElement(ELEMENT_GROUPS));
            }
            // init files map
            initFiles(filesDocument);
            // init groups map
            initGroups(groupsDocument);
        } catch (SAXException e) {
            throw new RegistryConfigurationException("invalid xml file", e);
        } catch (IOException e) {
            throw new RegistryConfigurationException("I/O error", e);
        }
    }

    /**
     * check method that check the null and empty state of String argument, and throw exception.
     * @param toCheck
     *            the string to be checked.
     * @param argumentName
     *            the argument name
     * @return the string to be checked
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if any string argument is empty
     */
    private static String checkStr(String toCheck, String argumentName) {
        if (toCheck == null) {
            throw new NullPointerException(argumentName + " is null");
        } else if (toCheck.trim().length() == 0) {
            throw new IllegalArgumentException(argumentName + " is empty");
        }
        return toCheck;
    }

    /**
     * init groups map.
     * @param groupsDocument
     *            the DOM document used by this instance to work with the groups
     * @throws RegistryConfigurationException
     *             if either id or name attribute not exist
     */
    private void initGroups(Document groupsDocument) throws RegistryConfigurationException {
        NodeList nodeList = groupsDocument.getDocumentElement().getElementsByTagName(ELEMENT_GROUP);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element groupElement = (Element) nodeList.item(i);
            if (!groupElement.hasAttribute(ATTR_NAME)) {
                throw new RegistryConfigurationException("group element doesn't have name attribute");
            }
            String groupName = groupElement.getAttribute(ATTR_NAME);
            NodeList fileNodeList = groupElement.getElementsByTagName(ELEMENT_FILE);
            List fileIds = new ArrayList();
            for (int j = 0; j < fileNodeList.getLength(); j++) {
                Element fileElement = (Element) fileNodeList.item(j);
                if (fileElement.hasAttribute(ATTR_ID)) {
                    String fileId = fileElement.getAttribute(ATTR_ID);
                    if (files.containsKey(fileId)) {
                        fileIds.add(fileId);
                    } else {
                        throw new RegistryConfigurationException("fileId " + fileId + " not exist in files xml fil");
                    }
                } else {
                    throw new RegistryConfigurationException("the file element in the groups xml file");
                }
            }
            groups.put(groupName, fileIds);
        }

    }

    /**
     * init files map.
     * @param filesDocument
     *            the DOM document used by this instance to work with the file mappings
     * @throws RegistryConfigurationException
     *             if either id or name attribute not exist
     */
    private void initFiles(Document filesDocument) throws RegistryConfigurationException {
        NodeList nodeList = filesDocument.getDocumentElement().getElementsByTagName(ELEMENT_FILE);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element fileElement = (Element) nodeList.item(i);
            if (fileElement.hasAttribute(ATTR_ID) && fileElement.hasAttribute(ATTR_NAME)) {
                files.put(fileElement.getAttribute(ATTR_ID), fileElement.getAttribute(ATTR_NAME));
            } else {
                throw new RegistryConfigurationException("both id and name attribute is required");
            }
        }
    }

    /**
     * Returns the next unique file id using the id generator. It has not been used in the registry and it will be used
     * by the file system handler to put a new file in the registry.
     * @return the file id
     * @throws RegistryException
     *             if an exception occured while performing the operation
     */
    public synchronized String getNextFileId() throws RegistryException {
        try {
            return Long.toString(idGenerator.getNextID());
        } catch (IDGenerationException e) {
            throw new RegistryException("fail to generate id", e);
        }
    }

    /**
     * Adds a new file record with the given fileId and fileName in the registry.
     * @param fileId
     *            the file id
     * @param fileName
     *            the file name
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws FileIdExistsException
     *             if the file id already exists
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public void addFile(String fileId, String fileName) throws FileIdExistsException, RegistryPersistenceException {
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        } else if (files.containsKey(fileId)) {
            throw new FileIdExistsException("file id exists", fileId);
        }
        if (fileName == null) {
            throw new NullPointerException("fileName is null");
        } else if (fileName.trim().length() == 0) {
            throw new IllegalArgumentException("fileName is empty");
        }
        // create the new element
        Element fileElement = filesDocument.createElement(ELEMENT_FILE);
        fileElement.setAttribute(ATTR_ID, fileId);
        fileElement.setAttribute(ATTR_NAME, fileName);
        // append the new element
        Element filesElement = filesDocument.getDocumentElement();
        filesElement.appendChild(fileElement);
        // commit changes
        commitDocument(filesDocument, filesFile);
        // add the file in the files:Map also
        files.put(fileId, fileName);
    }

    /**
     * Updates the file record with the given fileId, by setting the new fileName value in the registry.
     * @param fileId
     *            the file id
     * @param fileName
     *            the file name
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws FileIdNotFoundException
     *             if the file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized void renameFile(String fileId, String fileName) throws FileIdNotFoundException,
        RegistryPersistenceException {
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        } else if (!files.containsKey(fileId)) {
            throw new FileIdNotFoundException("file id not found", fileId);
        }
        if (fileName == null) {
            throw new NullPointerException("fileName is null");
        } else if (fileName.trim().length() == 0) {
            throw new IllegalArgumentException("fileName is empty");
        }

        NodeList nodeList = filesDocument.getElementsByTagName(ELEMENT_FILE);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            if (fileId.equals(element.getAttribute(ATTR_ID))) {
                element.setAttribute(ATTR_NAME, fileName);
                // commit changes
                commitDocument(filesDocument, filesFile);
                // update the file in the files:Map also
                files.put(fileId, fileName);
                break;
            }
        }
    }

    /**
     * Removes the file record with the file id equal with the given fileId from the registry. The files are removed
     * from the groups they belong to.
     * @param fileId
     *            the file id
     * @throws NullPointerException
     *             if the argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws FileIdNotFoundException
     *             if the file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized void removeFile(String fileId) throws FileIdNotFoundException, RegistryPersistenceException {
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        } else if (!files.containsKey(fileId)) {
            throw new FileIdNotFoundException("file id not found", fileId);
        }
        // gets the NodeList of "file" elements from the filesDocument
        NodeList nodeList = filesDocument.getElementsByTagName(ELEMENT_FILE);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element fileElement = (Element) nodeList.item(i);
            if (fileId.equals(fileElement.getAttribute(ATTR_ID))) {
                Element filesElement = filesDocument.getDocumentElement();
                filesElement.removeChild(fileElement);
                commitDocument(filesDocument, filesFile);
                files.remove(fileId);
                break;
            }
        }
        // remove the file from the groups it belongs to
        nodeList = groupsDocument.getElementsByTagName(ELEMENT_GROUP);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element groupElement = (Element) nodeList.item(i);
            // gets the NodeLists of "file" elements from groupElement
            NodeList fileNodeList = groupElement.getElementsByTagName(ELEMENT_FILE);
            for (int j = 0; j < fileNodeList.getLength(); j++) {
                Element fileElement = (Element) fileNodeList.item(j);
                if (fileId.equals(fileElement.getAttribute(ATTR_ID))) {
                    // remove the file element
                    groupElement.removeChild(fileElement);
                    // commit changes in both files and groups documents
                    commitDocument(groupsDocument, groupsFile);
                    List fileIds = (List) groups.get(groupElement.getAttribute(ATTR_NAME));
                    fileIds.remove(fileId);
                    break;
                }
            }
        }

    }

    /**
     * Gets the fileName with the given fileId from the registry.
     * @param fileId
     *            the file id
     * @return the file name
     * @throws NullPointerException
     *             if the argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws FileIdNotFoundException
     *             if the file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized String getFile(String fileId) throws FileIdNotFoundException, RegistryPersistenceException {
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        } else if (!files.containsKey(fileId)) {
            throw new FileIdNotFoundException("file id not found", fileId);
        }
        return (String) files.get(fileId);
    }

    /**
     * Gets the list of the file ids from the registry. The returned list contains String elements, non-null and
     * non-empty.
     * @return the list of file ids
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized List getFileIds() throws RegistryPersistenceException {
        return new ArrayList(files.keySet());
    }

    /**
     * Gets the map of files from the registry. The returned map contains listId-listName pairs. Both the keys and the
     * values are Strings, non-null, non-empty.
     * @return the map of files
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized Map getFiles() throws RegistryPersistenceException {
        return new HashMap(files);
    }

    /**
     * Creates a new group in the registry. The provided group name must be unique. The file ids must exist and they
     * will be set as the group's files.
     * @param groupName
     *            the group name
     * @param fileIds
     *            the list of file ids
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws GroupExistsException
     *             if the group name already exists
     * @throws FileIdNotFoundException
     *             if a file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized void createGroup(String groupName, List fileIds) throws GroupExistsException,
        FileIdNotFoundException, RegistryPersistenceException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        } else if (groups.containsKey(groupName)) {
            throw new GroupExistsException("group already exists", groupName);
        }
        if (fileIds == null) {
            throw new NullPointerException("fileIds is null");
        }
        // create the new element
        Element groupElement = groupsDocument.createElement(ELEMENT_GROUP);
        groupElement.setAttribute(ATTR_NAME, groupName);
        // add the file ids elements
        for (Iterator iter = fileIds.iterator(); iter.hasNext();) {
            String fileId = (String) iter.next();
            if (!this.files.containsKey(fileId)) {
                throw new FileIdNotFoundException("file id not found", fileId);
            } else {
                Element fileElement = groupsDocument.createElement(ELEMENT_FILE);
                fileElement.setAttribute(ATTR_ID, fileId);
                // append the new file element to the group element
                groupElement.appendChild(fileElement);
            }
        }
        // append the new group element to the groups element
        groupsDocument.getDocumentElement().appendChild(groupElement);
        // commit changes
        commitDocument(groupsDocument, groupsFile);
        // add the group in the groups:Map also
        groups.put(groupName, new ArrayList(fileIds));
    }

    /**
     * Updates the group in the registry. The file ids must exist and they will be set as the group's files.
     * @param groupName
     *            the group name
     * @param fileIds
     *            the list of file ids
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws GroupNotFoundException
     *             if the group name cannot be found
     * @throws FileIdNotFoundException
     *             if a file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized void updateGroup(String groupName, List fileIds) throws GroupNotFoundException,
        FileIdNotFoundException, RegistryPersistenceException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        } else if (!groups.containsKey(groupName)) {
            throw new GroupNotFoundException("group not exist", groupName);
        }
        if (fileIds == null) {
            throw new NullPointerException("fileIds is null");
        }

        // search the group element
        Element groupElement = null;
        // gets the NodeList of "group" elements from the groupsDocument
        NodeList groupNodeList = groupsDocument.getElementsByTagName(ELEMENT_GROUP);
        for (int i = 0; i < groupNodeList.getLength(); i++) {
            Element element = (Element) groupNodeList.item(i);
            if (groupName.equals(element.getAttribute(ATTR_NAME))) {
                groupElement = element;
                break;
            }
        }
        if (groupElement == null) {
            throw new GroupNotFoundException("group element not found in xml file", groupName);
        }
        Element groupsElement = groupsDocument.getDocumentElement();
        // create the new group element
        Element newGroupElement = groupsDocument.createElement(ELEMENT_GROUP);
        newGroupElement.setAttribute(ATTR_NAME, groupName);
        // add the file ids elements
        for (Iterator iter = fileIds.iterator(); iter.hasNext();) {
            String fileId = (String) iter.next();
            if (!files.containsKey(fileId)) {
                throw new FileIdNotFoundException("file id does not exist in files map", fileId);
            }
            Element fileElement = groupsDocument.createElement(ELEMENT_FILE);
            fileElement.setAttribute(ATTR_ID, fileId);
            // append the new file element to the group element
            newGroupElement.appendChild(fileElement);
        }
        // remove the old group element
        groupsElement.removeChild(groupElement);
        // append the new group element to the groups element
        groupsElement.appendChild(newGroupElement);
        // commit changes
        commitDocument(groupsDocument, groupsFile);
        // update the group in the groups:Map also
        groups.put(groupName, new ArrayList(fileIds));
    }

    /**
     * Removes the group from the registry.
     * @param groupName
     *            the group name
     * @throws NullPointerException
     *             if the argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws GroupNotFoundException
     *             if the group name cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized void removeGroup(String groupName) throws GroupNotFoundException, RegistryPersistenceException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        } else if (!groups.containsKey(groupName)) {
            throw new GroupNotFoundException("group not exist", groupName);
        }
        // search the group element
        NodeList groupNodeList = groupsDocument.getElementsByTagName(ELEMENT_GROUP);
        for (int i = 0; i < groupNodeList.getLength(); i++) {
            Element groupElement = (Element) groupNodeList.item(i);
            if (groupName.equals(groupElement.getAttribute(ATTR_NAME))) {
                // remove the group element
                groupsDocument.getDocumentElement().removeChild(groupElement);
                // commit changes
                commitDocument(groupsDocument, groupsFile);
                // remove the group from the groups:Map also
                groups.remove(groupName);
                break;
            }
        }

    }

    /**
     * Gets the list of the group names from the registry. The returned list contains String elements, non-null and
     * non-empty.
     * @return the list of group names
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized List getGroupNames() throws RegistryPersistenceException {
        return new ArrayList(groups.keySet());
    }

    /**
     * Gets the list of group's file ids from the registry. The returned list contains String elements, non-null and
     * non-empty.
     * @param groupName
     *            the group name
     * @return the list of file ids
     * @throws NullPointerException
     *             if the argument is empty
     * @throws IllegalArgumentException
     *             if the string argument is empty
     * @throws GroupNotFoundException
     *             if the group name cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized List getGroupFiles(String groupName) throws GroupNotFoundException,
        RegistryPersistenceException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        } else if (!groups.containsKey(groupName)) {
            throw new GroupNotFoundException("group not exist", groupName);
        }
        return new ArrayList((List) groups.get(groupName));
    }

    /**
     * Adds the file id to the group's list of files in the registry.
     * @param groupName
     *            the group name
     * @param fileId
     *            the file id
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws GroupNotFoundException
     *             if the group name cannot be found
     * @throws FileIdNotFoundException
     *             if the file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized void addFileToGroup(String groupName, String fileId) throws GroupNotFoundException,
        FileIdNotFoundException, RegistryPersistenceException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        } else if (!groups.containsKey(groupName)) {
            throw new GroupNotFoundException("group not exist", groupName);
        }
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        } else if (!files.containsKey(fileId)) {
            throw new FileIdNotFoundException("file id not found", fileId);
        }
        // search the group element
        NodeList groupNodeList = groupsDocument.getElementsByTagName(ELEMENT_GROUP);
        for (int i = 0; i < groupNodeList.getLength(); i++) {
            Element groupElement = (Element) groupNodeList.item(i);
            if (groupName.equals(groupElement.getAttribute(ATTR_NAME))) {
                // search the file element, in case it already exists
                NodeList fileNodeList = groupElement.getElementsByTagName(ELEMENT_FILE);
                for (int j = 0; j < fileNodeList.getLength(); j++) {
                    Element fileElement = (Element) fileNodeList.item(j);
                    if (fileElement.getAttribute(ATTR_ID).equals(fileId)) {
                        // already exist, avoid read
                        return;
                    }
                }
                // create the new file element
                Element newFileElement = groupsDocument.createElement(ELEMENT_FILE);
                newFileElement.setAttribute(ATTR_ID, fileId);
                // append the new element
                groupElement.appendChild(newFileElement);
                // commit changes
                commitDocument(groupsDocument, groupsFile);
                // add the file to the group in the groups:Map also
                List fileIds = (List) groups.get(groupName);
                if (!fileIds.contains(fileId)) {
                    fileIds.add(fileId);
                }
                return;
            }

        }
    }

    /**
     * Remove the file id from the group's list of files in the registry.
     * @param groupName
     *            the group name
     * @param fileId
     *            the file id
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if any string argument is empty
     * @throws GroupNotFoundException
     *             if the group name cannot be found
     * @throws FileIdNotFoundException
     *             if the file id cannot be found
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    public synchronized void removeFileFromGroup(String groupName, String fileId) throws GroupNotFoundException,
        FileIdNotFoundException, RegistryPersistenceException {
        if (groupName == null) {
            throw new NullPointerException("groupName is null");
        } else if (groupName.trim().length() == 0) {
            throw new IllegalArgumentException("groupName is empty");
        } else if (!groups.containsKey(groupName)) {
            throw new GroupNotFoundException("group not exist", groupName);
        }
        if (fileId == null) {
            throw new NullPointerException("fileId is null");
        } else if (fileId.trim().length() == 0) {
            throw new IllegalArgumentException("fileId is empty");
        } else if (!files.containsKey(fileId)) {
            throw new FileIdNotFoundException("file id not found", fileId);
        }
        // search the group element

        // gets the NodeList of "group" elements from the groupsDocument
        NodeList groupNodeList = groupsDocument.getElementsByTagName(ELEMENT_GROUP);
        for (int i = 0; i < groupNodeList.getLength(); i++) {
            Element groupElement = (Element) groupNodeList.item(i);
            if (groupName.equals(groupElement.getAttribute(ATTR_NAME))) {
                NodeList fileNodeList = groupElement.getElementsByTagName(ELEMENT_FILE);
                for (int j = 0; j < fileNodeList.getLength(); j++) {
                    Element fileElement = (Element) fileNodeList.item(j);
                    if (fileId.equals(fileElement.getAttribute(ATTR_ID))) {
                        // remove the file element
                        groupElement.removeChild(fileElement);
                        // commit changes
                        commitDocument(groupsDocument, groupsFile);
                        // remove the file id
                        List fileIds = (List) groups.get(groupName);
                        fileIds.remove(fileId);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Saves the given xml document in the given file. This method is used by all methods that alter a field document.
     * @param document
     *            the document to be persisted
     * @param file
     *            the xml file
     * @throws RegistryPersistenceException
     *             if an exception occured while performing the operation
     */
    private void commitDocument(Document document, File file) throws RegistryPersistenceException {
        // get instance of transformer factory
        TransformerFactory factory = TransformerFactory.newInstance();
        // create transformer to transform document object to file
        Transformer transformer;
        try {
            transformer = factory.newTransformer();
            // create file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }
            // transform the document to the output file
            Result result = new StreamResult(file);
            Source source = new DOMSource(document);
            transformer.transform(source, result);
        } catch (IOException e) {
            throw new RegistryPersistenceException("file creation fails", e);
        } catch (TransformerConfigurationException e) {
            throw new RegistryPersistenceException("bad transformer configuration", e);
        } catch (TransformerException e) {
            throw new RegistryPersistenceException("transformation fails", e);
        }
    }
}
