/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.lookup.SubmissionTypeLookupUtility;

/**
 * <p>
 * Accuracy tests for the {@link SubmissionTypeLookupUtility} class.
 * </p>
 *
 * @author akinwale
 * @version 1.4
 */
public class SubmissionTypeLookupUtilityAccuracyTests extends BaseTestCase {
    /**
     * <p>
     * Tests that the lookUpIdConnection, String) method works properly.
     * </p>
     *
     * @throws Exception
     *             exception to pass to JUnit
     */
    public void testLookupIdAccuracy() throws Exception {
        long id = SubmissionTypeLookupUtility.lookUpId(getConnection(), "Specification Submission");
        assertEquals("lookupId does not work properly", 2, id);

        id = SubmissionTypeLookupUtility.lookUpId(getConnection(), "Contest Submission");
        assertEquals("lookupId does not work properly", 1, id);
    }
}
