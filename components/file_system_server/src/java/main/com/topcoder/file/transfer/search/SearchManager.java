/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class performs file and group searches using FileSearcher and GroupSearcher instances. It's purpose is to
 * provide a centralized access point for the FileSystemHandler. It is not tread-safe, as this class should not be
 * changed after it is initialized. Only the two search methods (getFiles and getGroups) should be used after
 * initialization by the FileSystemHandler. Thus, the implementations of the FileSearcher and GroupSearcher interfaces
 * should be thread-safe.
 * @author Luca, FireIce
 * @version 1.0
 */
public class SearchManager {

    /**
     * Represents the map of file searchers. The fileSearchers map's values should be non-empty strings and the values
     * should be FileSearcher instances. Initialized in the constructor and never changed later. Not null. Can be empty.
     */
    private final Map fileSearchers = new HashMap();

    /**
     * Represents the map of group searchers. The groupSearchers map's values should be non-empty strings and the values
     * should be GroupSearcher instances. Initialized in the constructor and never changed later. Not null. Can be
     * empty.
     */
    private final Map groupSearchers = new HashMap();

    /**
     * Creates an instance with an empty map of file searchers and an empty map of group searchers.
     */
    public SearchManager() {
    }

    /**
     * Creates an instance with the given map of file searchers and the given map of group searchers. It creates shallow
     * copies of the given maps. The fileSearchers map's values should be non-empty strings and the values should be
     * FileSearcher instances. The groupSearchers map's values should be non-empty strings and the values should be
     * GroupSearcher instances.
     * @param fileSearchers
     *            the map of file searchers
     * @param groupSearchers
     *            the map of group searchers
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if the keys or values of the maps are illegal
     */
    public SearchManager(Map fileSearchers, Map groupSearchers) {
        validateMap(fileSearchers, FileSearcher.class, "fileSearchers");
        validateMap(groupSearchers, GroupSearcher.class, "groupSearchers");
        this.fileSearchers.putAll(fileSearchers);
        this.groupSearchers.putAll(groupSearchers);
    }

    /**
     * validate the searchers map, the key should be non-empty non-null string, the value should be FileSearcher or
     * GroupSearcher type.
     * @param searchers
     *            the map to validate
     * @param type
     *            the type of the values
     * @param searchersName
     *            the name of the serchers
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if the keys or values of the maps are illegal
     */
    private void validateMap(Map searchers, Class type, String searchersName) {
        if (searchers == null) {
            throw new NullPointerException(searchersName + " is null");
        } else {
            for (Iterator iter = searchers.entrySet().iterator(); iter.hasNext();) {
                Entry entry = (Entry) iter.next();
                Object obj = entry.getKey();
                if (obj instanceof String) {
                    String key = (String) obj;
                    if (key.trim().length() == 0) {
                        throw new IllegalArgumentException(searchersName + " contains key that is empty string");
                    }
                } else {
                    throw new IllegalArgumentException(searchersName + " contains non-String instances");
                }
                if (type == FileSearcher.class) {
                    if (!(entry.getValue() instanceof FileSearcher)) {
                        throw new IllegalArgumentException(searchersName + " contains non-FileSearcher instance");
                    }
                } else if (type == GroupSearcher.class) {
                    if (!(entry.getValue() instanceof FileSearcher) && !(entry.getValue() instanceof GroupSearcher)) {
                        throw new IllegalArgumentException(searchersName + " contains non-GroupSearcher instance");
                    }
                }
            }
        }
    }

    /**
     * Returns the files that match the given criteria. It delegates the work to the file searcher mapped under the
     * given file searcher name.
     * @param fileSearcherName
     *            the file searcher name
     * @param criteria
     *            the search criteria
     * @return a list with the files that match the given criteria
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if the fileSearcherName argument is empty
     * @throws FileSearcherNotFoundException
     *             if there is no file searcher associated with the file searcher name
     * @throws SearchException
     *             if an exception occurs while performing the search
     */
    public List getFiles(String fileSearcherName, String criteria) throws FileSearcherNotFoundException,
        SearchException {
        if (fileSearcherName == null) {
            throw new NullPointerException("fileSearcherName is null");
        } else if (fileSearcherName.trim().length() == 0) {
            throw new IllegalArgumentException("fileSearcherName is empty");
        }
        if (criteria == null) {
            throw new NullPointerException("criteria is null");
        }
        FileSearcher fileSearcher = (FileSearcher) this.fileSearchers.get(fileSearcherName);
        if (fileSearcher == null) {
            throw new FileSearcherNotFoundException("no file searcher for " + fileSearcherName, fileSearcherName);
        } else {
            return fileSearcher.getFiles(criteria);
        }
    }

    /**
     * Returns the groups that match the given criteria. It delegates the work to the group searcher mapped under the
     * given group searcher name.
     * @param groupSearcherName
     *            the group searcher name
     * @param criteria
     *            the search criteria
     * @return a list with the groups that match the given criteria
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if the groupSearcherName argument is empty
     * @throws GroupSearcherNotFoundException
     *             if there is no group searcher associated with the group searcher name
     * @throws SearchException
     *             if an exception occurs while performing the search
     */
    public List getGroups(String groupSearcherName, String criteria) throws GroupSearcherNotFoundException,
        SearchException {
        if (groupSearcherName == null) {
            throw new NullPointerException("groupSearcherName is null");
        } else if (groupSearcherName.trim().length() == 0) {
            throw new IllegalArgumentException("groupSearcherName is empty");
        }
        if (criteria == null) {
            throw new NullPointerException("criteria is null");
        }
        GroupSearcher groupSearcher = (GroupSearcher) this.groupSearchers.get(groupSearcherName);
        if (groupSearcher == null) {
            throw new GroupSearcherNotFoundException("no group searcher for " + groupSearcherName, criteria);
        } else {
            return groupSearcher.getGroups(criteria);
        }
    }

    /**
     * Adds the given file searcher to the map of file searchers, using the given file searcher name. If there was a
     * mapping for this key, the old value is replaced by the specified value.
     * @param fileSearcherName
     *            the file searcher name
     * @param fileSearcher
     *            the file searcher
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     */
    public void addFileSearcher(String fileSearcherName, FileSearcher fileSearcher) {
        if (fileSearcherName == null) {
            throw new NullPointerException("fileSearcherName is null");
        } else if (fileSearcherName.trim().length() == 0) {
            throw new IllegalArgumentException("fileSearcherName is empty");
        }
        if (fileSearcher == null) {
            throw new NullPointerException("fileSearcher is null");
        }
        fileSearchers.put(fileSearcherName, fileSearcher);
    }

    /**
     * Removes the file searcher identified by the given file searcher name from the map of file searchers.
     * @param fileSearcherName
     *            the file searcher name
     * @throws NullPointerException
     *             if the argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     */
    public void removeFileSearcher(String fileSearcherName) {
        if (fileSearcherName == null) {
            throw new NullPointerException("fileSearcherName is null");
        } else if (fileSearcherName.trim().length() == 0) {
            throw new IllegalArgumentException("fileSearcherName is empty");
        }
        fileSearchers.remove(fileSearcherName);
    }

    /**
     * Removes all the file searchers from the map of file searchers.
     */
    public void removeAllFileSearchers() {
        this.fileSearchers.clear();
    }

    /**
     * Returns a list with all the file searcher names from the map of file searchers (a list of the keys).
     * @return the list of file searcher names
     */
    public List getFileSearcherNames() {
        return new ArrayList(this.fileSearchers.keySet());
    }

    /**
     * Returns the file searcher identified by the given file searcher name from the map of file searchers.
     * @param fileSearcherName
     *            the file searcher name
     * @return the file searcher
     * @throws NullPointerException
     *             if the argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     */
    public FileSearcher getFileSearcher(String fileSearcherName) {
        if (fileSearcherName == null) {
            throw new NullPointerException("fileSearcherName is null");
        } else if (fileSearcherName.trim().length() == 0) {
            throw new IllegalArgumentException("fileSearcherName is empty");
        }
        return (FileSearcher) this.fileSearchers.get(fileSearcherName);
    }

    /**
     * Adds the given group searcher to the map of group searchers, using the given group searcher name. If there was a
     * mapping for this key, the old value is replaced by the specified value.
     * @param groupSearcherName
     *            the group searcher name
     * @param groupSearcher
     *            the group searcher
     * @throws NullPointerException
     *             if any argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     */
    public void addGroupSearcher(String groupSearcherName, GroupSearcher groupSearcher) {
        if (groupSearcherName == null) {
            throw new NullPointerException("groupSearcherName is null");
        } else if (groupSearcherName.trim().length() == 0) {
            throw new IllegalArgumentException("groupSearcherName is empty");
        }
        if (groupSearcher == null) {
            throw new NullPointerException("groupSearcher is null");
        }
        groupSearchers.put(groupSearcherName, groupSearcher);
    }

    /**
     * Removes the group searcher identified by the given group searcher name from the map of group searchers.
     * @param groupSearcherName
     *            the group searcher name
     * @throws NullPointerException
     *             if the argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     */
    public void removeGroupSearcher(String groupSearcherName) {
        if (groupSearcherName == null) {
            throw new NullPointerException("groupSearcherName is null");
        } else if (groupSearcherName.trim().length() == 0) {
            throw new IllegalArgumentException("groupSearcherName is empty");
        }
        this.groupSearchers.remove(groupSearcherName);
    }

    /**
     * Removes all the group searchers from the map of group searchers.
     */
    public void removeAllGroupSearchers() {
        this.groupSearchers.clear();
    }

    /**
     * Returns a list with all the group searcher names from the map of group searchers (a list of the keys).
     * @return the list of group searcher names
     */
    public List getGroupSearcherNames() {
        return new ArrayList(this.groupSearchers.keySet());
    }

    /**
     * Returns the group searcher identified by the given group searcher name from the map of group searchers.
     * @param groupSearcherName
     *            the group searcher name
     * @return the group searcher
     * @throws NullPointerException
     *             if the argument is null
     * @throws IllegalArgumentException
     *             if the string argument is empty
     */
    public GroupSearcher getGroupSearcher(String groupSearcherName) {
        if (groupSearcherName == null) {
            throw new NullPointerException("groupSearcherName is null");
        } else if (groupSearcherName.trim().length() == 0) {
            throw new IllegalArgumentException("groupSearcherName is empty");
        }
        return (GroupSearcher) this.groupSearchers.get(groupSearcherName);
    }
}
