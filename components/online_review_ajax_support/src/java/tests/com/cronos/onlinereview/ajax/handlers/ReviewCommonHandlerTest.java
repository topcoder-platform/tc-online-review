/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import java.util.Iterator;

import com.topcoder.management.phase.MockPhaseManager;
import com.topcoder.management.review.MockReviewManager;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * Test the class <code>ReviewCommonHandler</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class ReviewCommonHandlerTest extends TestCase {

    /**
     * Represents the handler to test.
     */
    private ReviewCommonHandler handler;

    /**
     * Set up the environment.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();

        // load the configurations
        ConfigManager cm = ConfigManager.getInstance();
        if (!cm.existsNamespace("com.cronos.onlinereview.ajax")) {
            cm.add("default.xml");
            cm.add("objectfactory.xml");
            cm.add("scorecalculator.xml");
        }

        handler = new MockReviewCommonHandler();
    }

    /**
     * Clean up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        while (it.hasNext()) {
            cm.removeNamespace(it.next().toString());
        }
    }

    /**
     * Test method for ReviewCommonHandler().
     */
    public void testReviewCommonHandler() {
        assertNotNull("The constructor doesn't work.", handler);
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.handlers.ReviewCommonHandler#getReviewManager()}.
     */
    public void testGetReviewManager() {
        assertNotNull("The review manager is not right.", handler.getReviewManager());

        assertTrue("The type is not right.", handler.getReviewManager() instanceof MockReviewManager);
    }

    /**
     * Test method for {@link com.cronos.onlinereview.ajax.handlers.ReviewCommonHandler#getPhaseManager()}.
     */
    public void testGetPhaseManager() {
        assertNotNull("The phase manager is not right.", handler.getPhaseManager());

        assertTrue("The type is not right.", handler.getPhaseManager() instanceof MockPhaseManager);
    }

}
