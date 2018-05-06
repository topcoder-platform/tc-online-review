/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.cronos.onlinereview.ajax.handlers.ReviewCommonHandler;

import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.review.ReviewManager;

import junit.framework.TestCase;


/**
 * Tests for ReviewCommonHandler class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestReviewCommonHandler extends TestCase {
    /**
     * MockReviewCommonHandler instance used for test.
     */
    private MockReviewCommonHandler handler = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        TestHelper.clearNamespaces();
        TestHelper.loadFile("test_files/accuracy/accuracy.xml");
        handler = new MockReviewCommonHandler();
    }

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        TestHelper.clearNamespaces();
    }

    /**
     * Test ReviewCommonHandler with accuracy state. The manager instances should be created.
     */
    public void testReviewCommonHandlerAccuracy() {
        assertNotNull("creating ReviewCommonHandler fails.", handler);
        assertTrue("creating ReviewCommonHandler fails.",
            handler.getPhaseManager() instanceof MockPhaseManager);
        assertTrue("creating ReviewCommonHandler fails.",
            handler.getReviewManager() instanceof MockReviewManager);
    }

    /**
     * Test getPhaseManager method with accuracy case.
     */
    public void testGetPhaseManager() {
        assertTrue("getPhaseManager fails.", handler.getPhaseManager() instanceof MockPhaseManager);
    }

    /**
     * Test getReviewManager method with accuracy case.
     */
    public void testGetReviewManager() {
        assertTrue("getReviewManager fails.",
            handler.getReviewManager() instanceof MockReviewManager);
    }

    /**
     * Mock class.
     *
     * @author assistant
     * @version 1.0
     */
    final class MockReviewCommonHandler extends ReviewCommonHandler {
        /**
         * MockReviewCommonHandler ctor.
         *
         * @throws ConfigurationException to Junit.
         */
        public MockReviewCommonHandler() throws ConfigurationException {
            super();
        }

        /**
         * <p>
         * Returns the review manager used to get review data.
         * </p>
         *
         * @return the review manager used to manage reviews
         */
        public ReviewManager getReviewManager() {
            return super.getReviewManager();
        }

        /**
         * <p>
         * Returns the phase manager used to get project phase data.
         * </p>
         *
         * @return the phase manager used to manage project phases
         */
        public PhaseManager getPhaseManager() {
            return super.getPhaseManager();
        }

        /**
         * Mock mothod.
         *
         * @param request DOCUMENT ME!
         * @param userId DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AjaxResponse service(AjaxRequest request, Long userId) {
            return null;
        }
    }
}
