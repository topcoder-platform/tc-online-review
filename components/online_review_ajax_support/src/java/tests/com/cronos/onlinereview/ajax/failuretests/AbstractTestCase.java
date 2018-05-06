/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.failuretests;

import junit.framework.TestCase;

import java.io.File;

import com.cronos.onlinereview.ajax.failuretests.mock.MockCalculationManager;
import com.cronos.onlinereview.ajax.failuretests.mock.MockPhaseManager;
import com.cronos.onlinereview.ajax.failuretests.mock.MockPhaseTemplate;
import com.cronos.onlinereview.ajax.failuretests.mock.MockProjectManager;
import com.cronos.onlinereview.ajax.failuretests.mock.MockReader;
import com.cronos.onlinereview.ajax.failuretests.mock.MockResourceManager;
import com.cronos.onlinereview.ajax.failuretests.mock.MockReviewManager;
import com.cronos.onlinereview.ajax.failuretests.mock.MockScorecardManager;
import com.cronos.onlinereview.ajax.failuretests.mock.MockServletConfig;
import com.cronos.onlinereview.ajax.failuretests.mock.MockUploadManager;

/**
 * <p>A base class for the test cases. Implements the test environment setup and release.</p>
 *
 * @author  isv
 * @version 1.0
 */
public abstract class AbstractTestCase extends TestCase {

    /**
     * <p>Sets up the fixture - loads the necessary configuration files. This method is called before a test is
     * executed.</p>
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigHelper.releaseNamespaces();
        ConfigHelper.loadConfiguration("test_files/failure/OnlineReview.xml");
        ConfigHelper.loadConfiguration("test_files/failure/ObjectFactory.xml");

        // Init the states of Mock classes
        MockCalculationManager.releaseState();
        MockPhaseManager.releaseState();
        MockPhaseTemplate.releaseState();
        MockProjectManager.releaseState();
        MockReader.releaseState();
        MockResourceManager.releaseState();
        MockReviewManager.releaseState();
        MockScorecardManager.releaseState();
        MockServletConfig.releaseState();
        MockUploadManager.releaseState();

        MockCalculationManager.init();
        MockPhaseManager.init();
        MockPhaseTemplate.init();
        MockProjectManager.init();
        MockReader.init();
        MockResourceManager.init();
        MockReviewManager.init();
        MockScorecardManager.init();
        MockServletConfig.init();
        MockUploadManager.init();

    }

    /**
     * <p>Tears down the fixture. Unloads the loaded configuration files. This method is called after a test is
     * executed.</p>
     */
    protected void tearDown() throws Exception {
        ConfigHelper.releaseNamespaces();
        // Release the states of the Mock classes
        MockCalculationManager.releaseState();
        MockPhaseManager.releaseState();
        MockPhaseTemplate.releaseState();
        MockProjectManager.releaseState();
        MockReader.releaseState();
        MockResourceManager.releaseState();
        MockReviewManager.releaseState();
        MockScorecardManager.releaseState();
        MockServletConfig.releaseState();
        MockUploadManager.releaseState();
        super.tearDown();
    }
}
