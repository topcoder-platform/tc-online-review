/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.search.FileIdGroupSearcher;

import junit.framework.TestCase;


/**
 * Test <code>FileIdGroupSearcher</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureFileIdGroupSearcherTest extends TestCase {
    /** The main <code>FileIdGroupSearcher</code> instance used to test. */
    private FileIdGroupSearcher searcher;

    /**
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        FailureHelper.loadConfigs();
        searcher = new FileIdGroupSearcher(FailureHelper.getFileSystemXMLRegistry());
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
     * Test <code>FileIdGroupSearcher(FileSystemRegistry)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if argument is null.
     */
    public void testFileIdGroupSearcherForNullPointerException() {
        try {
            new FileIdGroupSearcher(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getGroups(String)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetGroupsForNullPointerException()
        throws Exception {
        try {
            searcher.getGroups(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }
}
