/**
 *
 * Copyright (c) 2007, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview.services.uploads.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all Failure test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * Aggregate all the failure cases.
     *
     * @return the aggregated cases
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(DefaultManagersProviderFailureTests.class);
        suite.addTestSuite(DefaultUploadExternalServicesFailureTests.class);
        suite.addTestSuite(DefaultUploadServicesFailureTests.class);

        return suite;
    }
}
