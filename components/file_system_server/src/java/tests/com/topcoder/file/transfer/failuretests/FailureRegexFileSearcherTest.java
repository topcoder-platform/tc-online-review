/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.search.RegexFileSearcher;

import junit.framework.TestCase;


/**
 * Test <code>RegexFileSearcher</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureRegexFileSearcherTest extends TestCase {
    /** The main <code>RegexFileSearcher</code> instance used to test. */
    private RegexFileSearcher searcher;

    /**
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        FailureHelper.loadConfigs();
        searcher = new RegexFileSearcher(FailureHelper.getFileSystemXMLRegistry());
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
     * Test <code>RegexFileSearcher(FileSystemRegistry)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if argument is null.
     */
    public void testRegexFileSearcherForNullPointerException() {
        try {
            new RegexFileSearcher(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>getFiles(String)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     *
     * @throws Exception to Junit.
     */
    public void testGetFilesForNullPointerException() throws Exception {
        try {
            searcher.getFiles(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }
}
