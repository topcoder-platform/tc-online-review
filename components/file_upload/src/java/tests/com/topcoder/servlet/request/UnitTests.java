/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author PE
 * @version 2.0
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * Creates a test suite of the tests contained in this class.
     * </p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(ConfigurationExceptionUnitTest.class);
        suite.addTestSuite(DisallowedDirectoryExceptionUnitTest.class);
        suite.addTestSuite(FileDoesNotExistExceptionUnitTest.class);
        suite.addTestSuite(FileSizeLimitExceededExceptionUnitTest.class);
        suite.addTestSuite(FileUploadExceptionUnitTest.class);
        suite.addTestSuite(InvalidContentTypeExceptionUnitTest.class);
        suite.addTestSuite(PersistenceExceptionUnitTest.class);
        suite.addTestSuite(RequestParsingExceptionUnitTest.class);

        suite.addTestSuite(FileUploadResultUnitTest.class);
        suite.addTestSuite(FileUploadUnitTest.class);
        suite.addTestSuite(HttpRequestParserUnitTest.class);
        suite.addTestSuite(LocalFileUploadUnitTest.class);
        suite.addTestSuite(LocalUploadedFileUnitTest.class);
        suite.addTestSuite(MemoryFileUploadUnitTest.class);
        suite.addTestSuite(MemoryUploadedFileUnitTest.class);
        suite.addTestSuite(RemoteFileUploadUnitTest.class);
        suite.addTestSuite(UploadedFileUnitTest.class);

        suite.addTestSuite(DemoTest.class);
        return suite;
    }
}
