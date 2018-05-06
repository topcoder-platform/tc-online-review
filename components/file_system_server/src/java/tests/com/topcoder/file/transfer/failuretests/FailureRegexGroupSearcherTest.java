/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.search.RegexGroupSearcher;

import junit.framework.TestCase;


/**
 * Test <code>RegexGroupSearcher</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureRegexGroupSearcherTest extends TestCase {
    /** The main <code>RegexGroupSearcher</code> instance used to test. */
    private RegexGroupSearcher searcher;

    /**
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        FailureHelper.loadConfigs();
        searcher = new RegexGroupSearcher(FailureHelper.getFileSystemXMLRegistry());
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
     * Test <code>RegexGroupSearcher(FileSystemRegistry)</code> method for failure. <code>NullPointerException</code>
     * should be thrown if argument is null.
     */
    public void testRegexGroupSearcherForNullPointerException() {
        try {
            new RegexGroupSearcher(null);
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
