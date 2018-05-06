/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for <code>DisallowedDirectoryException</code>.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class DisallowedDirectoryExceptionUnitTest extends TestCase {
    /**
     * <p>
     * The dir used for testing.
     * </p>
     */
    private static final String DIR = "dir";

    /**
     * <p>
     * Creation test.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated and the dir is properly set.
     * </p>
     */
    public void testDisallowedDirectoryException() {
        DisallowedDirectoryException ce = new DisallowedDirectoryException(DIR);

        assertNotNull("Unable to instantiate DisallowedDirectoryException.", ce);
        assertNotNull("Error message is not properly propagated to super class.", ce.getMessage());
        assertNotNull("Dir is not properly set.", ce.getDir());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies DisallowedDirectoryException subclasses FileUploadException.
     * </p>
     */
    public void testDisallowedDirectoryExceptionInheritance() {
        assertTrue("DisallowedDirectoryException does not subclass FileUploadException.",
            new DisallowedDirectoryException(DIR) instanceof FileUploadException);
    }

    /**
     * <p>
     * Tests the accuracy of the method getDir().
     * </p>
     */
    public void testGetDir() {
        DisallowedDirectoryException ce = new DisallowedDirectoryException(DIR);
        assertNotNull("Dir is not properly got.", ce.getDir());
    }
}
