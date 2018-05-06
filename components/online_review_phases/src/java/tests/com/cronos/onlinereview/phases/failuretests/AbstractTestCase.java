/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests;

import junit.framework.TestCase;

import java.io.File;

import com.cronos.onlinereview.phases.failuretests.mock.MockReviewManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockProjectManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockUploadManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockPhaseManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockResourceManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockScorecardManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockConnection;
import com.cronos.onlinereview.phases.failuretests.mock.MockProjectRetrieval;
import com.cronos.onlinereview.phases.failuretests.mock.MockScreeningManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockUserRetrieval;

/**
 * <p>A base class for the test cases. Implements the test environment setup and release.</p>
 *
 * @author  isv, myxgyy
 * @version 1.2
 * @since 1.0
 */
public abstract class AbstractTestCase extends TestCase {

    /**
     * <p>Sets up the fixture - loads the necessary configuration files. This method is called before a test is
     * executed.</p>
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigHelper.releaseNamespaces();
        ConfigHelper.loadConfiguration(new File("failure/config.xml"));
        ConfigHelper.loadConfiguration(new File("failure/Failure.xml"));

        // Init the states of Mock classes
        MockConnection.releaseState();
        MockPhaseManager.releaseState();
        MockProjectManager.releaseState();
        MockProjectRetrieval.releaseState();
        MockResourceManager.releaseState();
        MockReviewManager.releaseState();
        MockScorecardManager.releaseState();
        MockScreeningManager.releaseState();
        MockUploadManager.releaseState();
        MockUserRetrieval.releaseState();

        MockConnection.init();
        MockPhaseManager.init();
        MockProjectManager.init();
        MockProjectRetrieval.init();
        MockResourceManager.init();
        MockReviewManager.init();
        MockScorecardManager.init();
        MockScreeningManager.init();
        MockUploadManager.init();
        MockUserRetrieval.init();
    }

    /**
     * <p>Tears down the fixture. Unloads the loaded configuration files. This method is called after a test is
     * executed.</p>
     */
    protected void tearDown() throws Exception {
        ConfigHelper.releaseNamespaces();
        // Release the states of the Mock classes
        MockConnection.releaseState();
        MockPhaseManager.releaseState();
        MockProjectManager.releaseState();
        MockProjectRetrieval.releaseState();
        MockResourceManager.releaseState();
        MockReviewManager.releaseState();
        MockScorecardManager.releaseState();
        MockScreeningManager.releaseState();
        MockUploadManager.releaseState();
        MockUserRetrieval.releaseState();
        super.tearDown();
    }
}
