/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.cronos.onlinereview.actions.EJBLibraryServicesLocator;
import com.topcoder.service.contest.eligibility.dao.ContestEligibilityManager;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityService;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityServiceBean;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidationManager;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidatorException;

import java.util.Set;

/**
 * <p>An implementation of {@link ContestEligibilityService} interface which provides the library-call style for API of
 * <code>Contest Eligibility EJB</code>.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author isv
 * @version 1.0 (TopCoder Online Review Switch To Local Calls)
 */
public class ContestEligibilityServiceLibrary implements ContestEligibilityService {

    /**
     * <p>A <code>ContestEligibilityServiceBean</code> which is delegated the processing of the calls to methods of this
     * class.</p>
     */
    private ContestEligibilityServiceBean bean;

    /**
     * <p>Constructs new <code>ContestEligibilityServiceLibrary</code> instance. This implementation does nothing.</p>
     */
    public ContestEligibilityServiceLibrary() {
        Extender extender = new Extender();
        extender.setContestEligibilityManager(EJBLibraryServicesLocator.getContestEligibilityManager());
        extender.setContestEligibilityValidationManager(
            EJBLibraryServicesLocator.getContestEligibilityValidationManager());
        this.bean = extender;
    }

    /**
     * <p>Checks if specified user is eligible for accessing contest.</p>
     *
     * @param userId The user id.
     * @param contestId The contest id.
     * @param isStudio true if the contest is a studio contest, false otherwise.
     * @return <code>true</code> if eligible; <code>false</code> otherwise.
     * @throws ContestEligibilityValidatorException if an unexpected error occurs.
     */
    public boolean isEligible(long userId, long contestId, boolean isStudio)
        throws ContestEligibilityValidatorException {
        return bean.isEligible(userId, contestId, isStudio);
    }

    /**
     * <p>Checks if specified user is eligible for accessing contest.</p>
     *
     * @param contestId The contest id
     * @param isStudio true if the contest is a studio contest, false otherwise.
     * @return <code>true</code> if eligible; <code>false</code> otherwise.
     * @throws ContestEligibilityValidatorException if an unexpected error occurs.
     */
    public boolean hasEligibility(long contestId, boolean isStudio) throws ContestEligibilityValidatorException {
        return bean.hasEligibility(contestId, isStudio);
    }

    /**
     * <p>Gets eligibilities for specified contests.</p>
     *
     * @param contestids a list of contest ids.
     * @param isStudio the flag used to indicate whether it is studio
     * @return a list of eligibilities.
     * @throws ContestEligibilityValidatorException if an unexpected error occurs.
     */
    public Set<Long> haveEligibility(Long[] contestids, boolean isStudio) throws ContestEligibilityValidatorException {
        return bean.haveEligibility(contestids, isStudio);
    }

    /**
     * <p>An extension to wrapped bean class providing access to it's protected methods.</p>
     *
     * @author isv
     * @version 1.0
     */
    private static class Extender extends ContestEligibilityServiceBean {

        /**
         * <p>Constructs new <code>ContestEligibilityServiceLibrary$Extender</code> instance. This implementation does
         * nothing.</p>
         */
        private Extender() {
        }

        /**
         * <p>Sets the contest eligibility validation manager to be used by this service in local environment.</p>
         *
         * @param contestEligibilityValidationManager a <code>ContestEligibilityValidationManager</code> to be used by this
         *        service in local environment.
         * @throws IllegalArgumentException if specified <code>contestEligibilityValidationManager</code> is
         *         <code>null</code>.
         */
        protected void setContestEligibilityValidationManager(
            ContestEligibilityValidationManager contestEligibilityValidationManager) {
            super.setContestEligibilityValidationManager(contestEligibilityValidationManager);
        }

        /**
         * <p>Sets the contest eligibility manager to be used by this service in local environment.</p>
         *
         * @param contestEligibilityManager a <code>ContestEligibilityManager</code> to be used by this
         *        service in local environment.
         * @throws IllegalArgumentException if specified <code>contestEligibilityManager</code> is <code>null</code>.
         */
        protected void setContestEligibilityManager(ContestEligibilityManager contestEligibilityManager) {
            super.setContestEligibilityManager(contestEligibilityManager);
        }
    }
}
