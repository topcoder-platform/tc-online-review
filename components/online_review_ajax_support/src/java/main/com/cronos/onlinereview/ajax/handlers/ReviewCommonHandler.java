/*
 * Copyright (C) 2006-2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import com.cronos.onlinereview.ajax.AjaxSupportHelper;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.review.ReviewManager;

/**
 * <p>
 * Defines a common parent class to all handlers which service scorecard review related operations.
 * This class extends CommonHandler class, and keeps instances of ReviewManager, PhaseManager classes.
 * This class main purpose is to simplify implementation of Ajax request handlers
 * related to review scorecard operations.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong>
 * This class is immutable an thread safe.
 * </p>
 *
 * @author topgear
 * @author assistant
 * @version 1.0.1
 */
public abstract class ReviewCommonHandler extends CommonHandler {

    /**
     * <p>
     * The review manager used to get review data
     * This variable is immutable, it is initialized by the constructor to a not null ReviewManager object,
     * and retrieved using its corresponding getter method.
     * </p>
     */
    private final ReviewManager reviewManager;

    /**
     * <p>
     * The phase manager used to get project phase data
     * This variable is immutable, it is initialized by the constructor to a not null PhaseManager object,
     * and retrieved using its corresponding getter method.
     * </p>
     */
    private final PhaseManager phaseManager;

    /**
     * <p>
     * Creates an instance of this class and initialize its internal state.
     * </p>
     *
     * @throws ConfigurationException if there is a configuration exception
     */
    protected ReviewCommonHandler() throws ConfigurationException {
        reviewManager = (ReviewManager) AjaxSupportHelper.createObject(ReviewManager.class);
        phaseManager = (PhaseManager) AjaxSupportHelper.createObject(PhaseManager.class);
    }

    /**
     * <p>
     * Returns the review manager used to get review data.
     * </p>
     *
     * @return the review manager used to manage reviews
     */
    protected ReviewManager getReviewManager() {
        return reviewManager;
    }

    /**
     * <p>
     * Returns the phase manager used to get project phase data.
     * </p>
     *
     * @return the phase manager used to manage project phases
     */
    protected PhaseManager getPhaseManager() {
        return phaseManager;
    }
}
