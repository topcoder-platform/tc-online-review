/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;

/**
 * <p>
 * Failure test for {@link FileTemplateSource}.
 * </p>
 *
 * @author mumujava
 * @version 1.0
 */
public class FileTemplateSourceFailureTest extends TestCase {
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(FileTemplateSourceFailureTest.class);
        return suite;
    }
    /**
     * Failure test for method FileTemplateSource() with invalid path.
     * Expects TemplateSourceException.
     */
    public void test_getTemplate_invalid() throws Exception {
    	// Create source.
    	FileTemplateSource source = new FileTemplateSource();
    	try {
        	source.getTemplate("notexist");
            fail("Should throw TemplateSourceException.");
        } catch (TemplateSourceException e) {
            // Success.
        }
    }
}
