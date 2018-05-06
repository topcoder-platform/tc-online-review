/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.FileSystemHandler;
import com.topcoder.file.transfer.persistence.FilePersistence;
import com.topcoder.file.transfer.persistence.FileSystemPersistence;
import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.search.SearchManager;
import com.topcoder.file.transfer.validator.UploadRequestValidator;

import com.topcoder.util.datavalidator.ObjectValidator;

import junit.framework.TestCase;


/**
 * Test <code>FileSystemHandler</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureFileSystemHandlerTest extends TestCase {
    /** The argument used to construct <code>FileSystemHandler</code>. */
    private static final int MAX_REQUESTS = 10;

    /** The argument used to construct <code>FileSystemHandler</code>. */
    private static final String LOCATION = "test_files/failure/";

    /** The argument used to construct <code>FileSystemHandler</code>. */
    private FileSystemRegistry registry;

    /** The argument used to construct <code>FileSystemHandler</code>. */
    private FilePersistence persistence;

    /** The argument used to construct <code>FileSystemHandler</code>. */
    private ObjectValidator uploadRequestValidator;

    /** The argument used to construct <code>FileSystemHandler</code>. */
    private SearchManager searchManager;

    /** The main <code>FileSystemHandler</code> instance used to test. */
    private FileSystemHandler handler;

    /**
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        FailureHelper.loadConfigs();
        registry = FailureHelper.getFileSystemXMLRegistry();
        persistence = new FileSystemPersistence();
        uploadRequestValidator = new UploadRequestValidator(new MockFreeDiskSpaceChecker());
        searchManager = new SearchManager();
        handler = new FileSystemHandler(MAX_REQUESTS, registry, persistence, LOCATION, uploadRequestValidator,
                searchManager);
    }

    /**
     * Clear configurations.
     *
     * @throws Exception to Junit.
     */
    protected void tearDown() throws Exception {
        FailureHelper.clearConfigs();
    }

    /**
     * Test <code>FileSystemHandler(int, FileSystemRegistry, FilePersistence, String, ObjectValidator,
     * SearchManager)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * FileSystemRegistry is null.
     */
    public void testFileSystemHandlerForNullPointerArgument_FileSystemRegistry() {
        try {
            new FileSystemHandler(MAX_REQUESTS, null, persistence, LOCATION, uploadRequestValidator, searchManager);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemHandler(int, FileSystemRegistry, FilePersistence, String, ObjectValidator,
     * SearchManager)</code> method for failure. <code>NullPointerException</code> should be thrown if FilePersistence
     * is null.
     */
    public void testFileSystemHandlerForNullPointerArgument_FilePersistence() {
        try {
            new FileSystemHandler(MAX_REQUESTS, registry, null, LOCATION, uploadRequestValidator, searchManager);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemHandler(int, FileSystemRegistry, FilePersistence, String, ObjectValidator,
     * SearchManager)</code> method for failure. <code>NullPointerException</code> should be thrown if String is null.
     */
    public void testFileSystemHandlerForNullPointerArgument_String() {
        try {
            new FileSystemHandler(MAX_REQUESTS, registry, persistence, null, uploadRequestValidator, searchManager);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemHandler(int, FileSystemRegistry, FilePersistence, String, ObjectValidator,
     * SearchManager)</code> method for failure. <code>NullPointerException</code> should be thrown if ObjectValidator
     * is null.
     */
    public void testFileSystemHandlerForNullPointerArgument_ObjectValidator() {
        try {
            new FileSystemHandler(MAX_REQUESTS, registry, persistence, LOCATION, null, searchManager);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemHandler(int, FileSystemRegistry, FilePersistence, String, ObjectValidator,
     * SearchManager)</code> method for failure. <code>NullPointerException</code> should be thrown if SearchManager
     * is null.
     */
    public void testFileSystemHandlerForNullPointerArgument_SearchManager() {
        try {
            new FileSystemHandler(MAX_REQUESTS, registry, persistence, LOCATION, uploadRequestValidator, null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemHandler(int, FileSystemRegistry, FilePersistence, String, ObjectValidator,
     * SearchManager)</code> method for failure. <code>IllegalArgumentException</code> should be thrown if maxrequest
     * is negative.
     */
    public void testFileSystemHandlerForIllegalArgumentException_MAX() {
        try {
            new FileSystemHandler(-1, registry, persistence, LOCATION, uploadRequestValidator, searchManager);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>FileSystemHandler(int, FileSystemRegistry, FilePersistence, String, ObjectValidator,
     * SearchManager)</code> method for failure. <code>IllegalArgumentException</code> should be thrown if maxrequest
     * is negative or 0.
     */
    public void testFileSystemHandlerForIllegalArgumentException_Loc() {
        try {
            new FileSystemHandler(MAX_REQUESTS, registry, persistence, "  ", uploadRequestValidator, searchManager);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * Test <code>setTransferSessionTimeOut(long)</code> method for failure. <code>IllegalArgumentException</code>
     * should be thrown if if the argument is negative or 0.
     */
    public void testSetTransferSessionTimeOutForIllegalArgumentException() {
        try {
            handler.setTransferSessionTimeOut(0);
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }
}
