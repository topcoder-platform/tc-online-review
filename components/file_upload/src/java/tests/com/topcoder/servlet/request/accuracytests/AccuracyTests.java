/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.servlet.request.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author TopCoder
 * @version 2.0
 */
public class AccuracyTests extends TestCase {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(HttpRequestParserAccuracyTest.class);
        suite.addTestSuite(LocalFileUploadAccuracyTest.class);
        suite.addTestSuite(MemoryFileUploadAccuracyTest.class);

        suite.addTestSuite(RemoteFileUploadAccuracyTest.class);
        suite.addTestSuite(FileUploadResultAccuracyTest.class);
        suite.addTestSuite(LocalUploadedFileAccuracyTest.class);
        
        return suite;
    }
}
