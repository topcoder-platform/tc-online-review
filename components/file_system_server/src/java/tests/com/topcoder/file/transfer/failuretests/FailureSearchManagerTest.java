/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.search.FileSearcher;
import com.topcoder.file.transfer.search.FileSearcherNotFoundException;
import com.topcoder.file.transfer.search.GroupSearcher;
import com.topcoder.file.transfer.search.GroupSearcherNotFoundException;
import com.topcoder.file.transfer.search.RegexFileSearcher;
import com.topcoder.file.transfer.search.RegexGroupSearcher;
import com.topcoder.file.transfer.search.SearchManager;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;


/**
 * Test <code>SearchManager</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureSearchManagerTest extends TestCase {
    /** The main <code>SearchManager</code> instance used to test. */
    private SearchManager manager;

    /**
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        FailureHelper.loadConfigs();
        manager = new SearchManager();
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
     * Test <code>SearchManager(Map, Map)</code> method for failure. <code>NullPointerException</code> should be thrown
     * if fileSearchers is null.
     *
     * @throws Exception to Junit.
     */
    public void testSearchManagerForNullPointerException_fileSearchers()
        throws Exception {
        try {
            Map fileSearchers = null;
            Map groupSearchers = new HashMap();
            new SearchManager(fileSearchers, groupSearchers);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>SearchManager(Map, Map)</code> method for failure. <code>NullPointerException</code> should be thrown
     * if groupSearchers is null.
     *
     * @throws Exception to Junit.
     */
    public void testSearchManagerForNullPointerException_groupSearchers()
        throws Exception {
        try {
            Map fileSearchers = new HashMap();
            Map groupSearchers = null;
            new SearchManager(fileSearchers, groupSearchers);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>SearchManager(Map, Map)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if any key is null.
     *
     * @throws Exception to Junit.
     */
    public void testSearchManagerForIllegalArgumentException_NullKey()
        throws Exception {
        try {
            Map fileSearchers = new HashMap();
            fileSearchers.put(null, new RegexFileSearcher(FailureHelper.getFileSystemXMLRegistry()));

            Map groupSearchers = new HashMap();
            groupSearchers.put("RegexGroupSearcher", new RegexGroupSearcher(FailureHelper.getFileSystemXMLRegistry()));
            new SearchManager(fileSearchers, groupSearchers);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>SearchManager(Map, Map)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if any key is empty.
     *
     * @throws Exception to Junit.
     */
    public void testSearchManagerForIllegalArgumentException_EmptyKey()
        throws Exception {
        try {
            Map fileSearchers = new HashMap();
            fileSearchers.put("  ", new RegexFileSearcher(FailureHelper.getFileSystemXMLRegistry()));

            Map groupSearchers = new HashMap();
            groupSearchers.put("RegexGroupSearcher", new RegexGroupSearcher(FailureHelper.getFileSystemXMLRegistry()));
            new SearchManager(fileSearchers, groupSearchers);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>SearchManager(Map, Map)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if type is invalid.
     *
     * @throws Exception to Junit.
     */
    public void testSearchManagerForIllegalArgumentException_InvalidType()
        throws Exception {
        try {
            Map fileSearchers = new HashMap();
            fileSearchers.put("RegexFileSearcher", "RegexFileSearcher instance type");

            Map groupSearchers = new HashMap();
            groupSearchers.put("RegexGroupSearcher", new RegexGroupSearcher(FailureHelper.getFileSystemXMLRegistry()));
            new SearchManager(fileSearchers, groupSearchers);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>getFiles(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetFilesForNullPointerException_Name()
        throws Exception {
        try {
            String fileSearcherName = null;
            String criteria = "1004";
            manager.getFiles(fileSearcherName, criteria);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getFiles(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetFilesForNullPointerException_Cri()
        throws Exception {
        try {
            String fileSearcherName = "RegexFileSearcher";
            String criteria = null;
            manager.getFiles(fileSearcherName, criteria);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getFiles(String, String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if fileSearcherName is empty.
     *
     * @throws Exception to Junit.
     */
    public void testGetFilesForIllegalArgumentException()
        throws Exception {
        try {
            String fileSearcherName = " ";
            String criteria = "1004";
            manager.getFiles(fileSearcherName, criteria);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>getFiles(String, String)</code> method for failure. <code>FileSearcherNotFoundException</code> should
     * be thrown if file searcher does not exist.
     *
     * @throws Exception to Junit.
     */
    public void testGetFilesForFileSearcherNotFoundException()
        throws Exception {
        try {
            String fileSearcherName = "RegexFileSearcherNotExist";
            String criteria = "1004";
            manager.getFiles(fileSearcherName, criteria);
            fail("the FileSearcherNotFoundException should be thrown!");
        } catch (FileSearcherNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>getGroups(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if groupSearcherName is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetGroupsForNullPointerException_Name()
        throws Exception {
        try {
            String groupSearcherName = null;
            String criteria = "group1";
            manager.getGroups(groupSearcherName, criteria);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getGroups(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if criteria is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetGroupsForNullPointerException_Cri()
        throws Exception {
        try {
            String groupSearcherName = "RegexGroupSearcher";
            String criteria = null;
            manager.getGroups(groupSearcherName, criteria);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getGroups(String, String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if groupSearcherName is empty.
     *
     * @throws Exception to Junit.
     */
    public void testGetGroupsForIllegalArgumentException()
        throws Exception {
        try {
            String groupSearcherName = "  ";
            String criteria = "group1";
            manager.getGroups(groupSearcherName, criteria);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>getGroups(String, String)</code> method for failure. <code>GroupSearcherNotFoundException</code>
     * should be thrown if group searcher does not exist.
     *
     * @throws Exception to Junit.
     */
    public void testGetGroupsForGroupSearcherNotFoundException()
        throws Exception {
        try {
            String groupSearcherName = "RegexGroupSearcherNotExist";
            String criteria = "group1";
            manager.getGroups(groupSearcherName, criteria);
            fail("the GroupSearcherNotFoundException should be thrown!");
        } catch (GroupSearcherNotFoundException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileSearcher(String, FileSearcher)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileSearcherForNullPointerException_Name()
        throws Exception {
        try {
            String fileSearcherName = null;
            FileSearcher fileSearcher = new RegexFileSearcher(FailureHelper.getFileSystemXMLRegistry());
            manager.addFileSearcher(fileSearcherName, fileSearcher);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileSearcher(String, FileSearcher)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileSearcherForNullPointerException_FSeacher()
        throws Exception {
        try {
            String fileSearcherName = "RegexFileSearcher";
            FileSearcher fileSearcher = null;
            manager.addFileSearcher(fileSearcherName, fileSearcher);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>addFileSearcher(String, FileSearcher)</code> method for failure.
     * <code>IllegalArgumentException</code> should be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testAddFileSearcherForIllegalArgumentException()
        throws Exception {
        try {
            String fileSearcherName = "  ";
            FileSearcher fileSearcher = new RegexFileSearcher(FailureHelper.getFileSystemXMLRegistry());
            manager.addFileSearcher(fileSearcherName, fileSearcher);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileSearcher(String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileSearcherForNullPointerException()
        throws Exception {
        try {
            manager.removeFileSearcher(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>removeFileSearcher(String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveFileSearcherForIllegalArgumentException()
        throws Exception {
        try {
            manager.removeFileSearcher(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>getFileSearcher(String)</code> method for failure. <code>NullPointerException</code> should be thrown
     * if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileSearcherForNullPointerException()
        throws Exception {
        try {
            manager.getFileSearcher(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getFileSearcher(String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testGetFileSearcherForIllegalArgumentException()
        throws Exception {
        try {
            manager.getFileSearcher(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>addGroupSearcher(String, GroupSearcher)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if groupSearcherName is null.
     *
     * @throws Exception to Junit.
     */
    public void testAddGroupSearcherForNullPointerException_Name()
        throws Exception {
        try {
            String groupSearcherName = null;
            GroupSearcher groupSearcher = new RegexGroupSearcher(FailureHelper.getFileSystemXMLRegistry());
            manager.addGroupSearcher(groupSearcherName, groupSearcher);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>addGroupSearcher(String, GroupSearcher)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if groupSearcher is null.
     *
     * @throws Exception to Junit.
     */
    public void testAddGroupSearcherForNullPointerException_Searcher()
        throws Exception {
        try {
            String groupSearcherName = "RegexGroupSearcher";
            GroupSearcher groupSearcher = null;
            manager.addGroupSearcher(groupSearcherName, groupSearcher);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>addGroupSearcher(String, GroupSearcher)</code> method for failure.
     * <code>IllegalArgumentException</code> should be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testAddGroupSearcherForIllegalArgumentException()
        throws Exception {
        try {
            String groupSearcherName = "  ";
            GroupSearcher groupSearcher = new RegexGroupSearcher(FailureHelper.getFileSystemXMLRegistry());
            manager.addGroupSearcher(groupSearcherName, groupSearcher);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>removeGroupSearcher(String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveGroupSearcherForNullPointerException()
        throws Exception {
        try {
            manager.removeGroupSearcher(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>removeGroupSearcher(String)</code> method for failure. <code>IllegalArgumentException</code> should
     * be thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testRemoveGroupSearcherForIllegalArgumentException()
        throws Exception {
        try {
            manager.removeGroupSearcher(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>getGroupSearcher(String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetGroupSearcherForNullPointerException()
        throws Exception {
        try {
            manager.getGroupSearcher(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getGroupSearcher(String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     *
     * @throws Exception to Junit.
     */
    public void testGetGroupSearcherForIllegalArgumentException()
        throws Exception {
        try {
            manager.getGroupSearcher(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }
}
