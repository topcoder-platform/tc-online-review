/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.topcoder.service.contest.eligibility.ContestEligibility;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidationManager;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidationManagerBean;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidationManagerConfigurationException;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidationManagerException;

import java.util.List;

/**
 * <p>An implementation of {@link ContestEligibilityValidationManager} interface which provides the library-call style
 * for API of <code>Contest Eligibility Validation EJB</code>.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author isv
 * @version 1.0 (TopCoder Online Review Switch To Local Calls)
 */
public class ContestEligibilityValidationManagerLibrary implements ContestEligibilityValidationManager {

    /**
     * <p>A <code>ContestEligibilityValidationManagerBean</code> which is delegated the processing of the calls to methods of this
     * class.</p>
     */
    private ContestEligibilityValidationManagerBean bean;

    /**
     * <p>Constructs new <code>ContestEligibilityValidationManagerLibrary</code> instance.</p>
     */
    public ContestEligibilityValidationManagerLibrary() {
        Extender extender = new Extender();
        extender.initialize();
        this.bean = extender;
    }

    /**
     * <p>Validates the specified eligibilities for user.</p>
     *
     * @param userId the id of user.
     * @param eligibilities a list of ContestEligibility entities.
     * @return <code>true</code> if eligible; <code>false</code> otherwise.
     * @throws ContestEligibilityValidationManagerException if an unexpected error occurs.
     */
    public boolean validate(long userId, List<ContestEligibility> eligibilities)
        throws ContestEligibilityValidationManagerException {
        return bean.validate(userId, eligibilities);
    }

    /**
     * <p>An extension to wrapped bean class providing access to it's protected methods.</p>
     *
     * @author isv
     * @version 1.0
     */
    private static class Extender extends ContestEligibilityValidationManagerBean {

        /**
         * <p>Constructs new <code>ContestEligibilityServiceLibrary$Extender</code> instance. This implementation does
         * nothing.</p>
         */
        private Extender() {
        }

        /**
         * <p>Handle the post-construct event. It will initialize any internal needed properties.</p>
         *
         * @throws ContestEligibilityValidationManagerConfigurationException if namespace or configFileName is empty or
         *         any errors occurred when initializing
         */
        @Override
        protected void initialize() {
            super.initialize();
        }
    }
}
