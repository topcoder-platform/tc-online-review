/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.topcoder.service.contest.eligibility.ContestEligibility;
import com.topcoder.service.contest.eligibility.dao.ContestEligibilityManager;
import com.topcoder.service.contest.eligibility.dao.ContestEligibilityManagerBean;
import com.topcoder.service.contest.eligibility.dao.ContestEligibilityPersistenceException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Set;

/**
 * <p>An implementation of {@link ContestEligibilityManager} interface which provides the library-call style
 * for API of <code>Contest Eligibility Manager EJB</code>.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author isv
 * @version 1.0 (TopCoder Online Review Switch To Local Calls)
 */
public class ContestEligibilityManagerLibrary implements ContestEligibilityManager {

    /**
     * <p>A <code>ContestEligibilityManagerBean</code> which is delegated the processing of the calls to methods of this
     * class.</p>
     */
    private ContestEligibilityManagerBean bean;

    /**
     * <p>Constructs new <code>ContestEligibilityManagerLibrary</code> instance.</p>
     */
    public ContestEligibilityManagerLibrary() {
        Extender extender = new Extender();
        extender.initialize();
        this.bean = extender;
    }

    /**
     * <p>Creates eligibility.</p>
     *
     * @param contestEligibility contest eligibility
     * @return created eligibility.
     * @throws ContestEligibilityPersistenceException if an unexpected error occurs.
     */
    public ContestEligibility create(ContestEligibility contestEligibility)
        throws ContestEligibilityPersistenceException {
        return bean.create(contestEligibility);
    }

    /**
     * <p>Removes the eligibility.</p>
     *
     * @param contestEligibility the contest eligibility to remove
     * @throws ContestEligibilityPersistenceException if an unexpected error occurs.
     */
    public void remove(ContestEligibility contestEligibility) throws ContestEligibilityPersistenceException {
        bean.remove(contestEligibility);
    }

    /**
     * <p>Gets the eligibilities.</p>
     *
     * @param list a list of eligibilities
     * @return a list of eligibilities.
     * @throws ContestEligibilityPersistenceException if an unexpected error occurs.
     */
    public List<ContestEligibility> save(List<ContestEligibility> list) throws ContestEligibilityPersistenceException {
        return bean.save(list);
    }

    /**
     * <p>Gets the eligibility.</p>
     *
     * @param contestId the contest id
     * @param isStudio the flag used to indicate whether it is studio
     * @return an eligibility.
     * @throws ContestEligibilityPersistenceException if an unexpected error occurs.
     */
    public List<ContestEligibility> getContestEligibility(long contestId, boolean isStudio)
        throws ContestEligibilityPersistenceException {
        return bean.getContestEligibility(contestId, isStudio);
    }

    /**
     * <p>Gets eligibility IDs.</p>
     *
     * @param contestIds the contest id list
     * @param isStudio the flag used to indicate whether it is studio
     * @return a list of IDs.
     * @throws ContestEligibilityPersistenceException if an unexpected error occurs.
     */
    public Set<Long> haveEligibility(Long[] contestIds, boolean isStudio)
        throws ContestEligibilityPersistenceException {
        return bean.haveEligibility(contestIds, isStudio);
    }

    /**
     * <p>An extension to wrapped bean class providing access to it's protected methods.</p>
     *
     * @author isv
     * @version 1.0
     */
    private static class Extender extends ContestEligibilityManagerBean {

        /**
         * <p>Constructs new <code>Extender</code> instance. This implementation does nothing.</p>
         */
        private Extender() {
        }

        /**
         * Handle the post-construct event. It will initialize the logger.
         */
        @Override
        protected void initialize() {
            super.initialize();
        }

        /**
         * <p> Returns the <code>EntityManager</code> looked up from the session context. </p>
         *
         * @return the EntityManager looked up from the session context
         * @throws ContestEligibilityPersistenceException if fail to get the EntityManager from the sessionContext.
         */
        protected EntityManager getEntityManager() throws ContestEligibilityPersistenceException {
            EntityManagerFactory emf = SpringContextProvider.getEntityManagerFactory();
            return emf.createEntityManager();
        }
    }
}
