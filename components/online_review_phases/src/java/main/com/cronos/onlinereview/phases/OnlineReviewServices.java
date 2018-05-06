/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * <p>
 * Contains various methods that are called directly from within the Online Review application.
 * </p>
 * @author VolodymyrK
 * @version 1.7.4
 */
public class OnlineReviewServices {

    /**
     * Represents the default name space of this class. It is used in the
     * default constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.OnlineReviewServices";

    /** Property name constant for manager helper namespace. */
    private static final String PROP_MANAGER_HELPER_NAMESPACE = "ManagerHelperNamespace";

    /**
     * <p>
     * This field is used to retrieve manager instances. It is initialized in the constructor
     * and never change after that. It is never null.
     * </p>
     */
    private final ManagerHelper managerHelper;

    /**
     * Create a new instance of OnlineReviewServices using the default namespace
     * for loading configuration settings.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     */
    public OnlineReviewServices() throws ConfigurationException {
        this(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of OnlineReviewServices using the given namespace
     * for loading configuration settings.
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public OnlineReviewServices(String namespace) throws ConfigurationException {
        PhasesHelper.checkString(namespace, "namespace");

        // initialize ManagerHelper from given namespace if provided.
        String managerHelperNamespace = PhasesHelper.getPropertyValue(namespace, PROP_MANAGER_HELPER_NAMESPACE, false);

        if (PhasesHelper.isStringNullOrEmpty(managerHelperNamespace)) {
            this.managerHelper = new ManagerHelper();
        } else {
            this.managerHelper = new ManagerHelper(managerHelperNamespace);
        }
    }

    /**
     * Updates the scores and placements of the submissions by aggregation the scores from the reviews.
     * Also updates the 1st place winner and the 2nd place winner in the project properties.
     *
     * @param phase Review phase.
     * @param operator The operator name.
     * @param updateInitialScore True if the "initial" scores of the submissions should be updated.
     * @param updateFinalResults True if the "final" scores and placements of the submissions should be updated.
     * @return Array of all updated submissions.
     * @throws com.topcoder.management.phase.PhaseHandlingException if any error occurs.
     */
    public Submission[] updateSubmissionsResults(Phase phase, String operator,
        boolean updateInitialScore, boolean updateFinalResults) throws PhaseHandlingException {
        return PhasesHelper.updateSubmissionsResults(managerHelper, phase, operator,
            updateInitialScore, updateFinalResults);
    }
}
