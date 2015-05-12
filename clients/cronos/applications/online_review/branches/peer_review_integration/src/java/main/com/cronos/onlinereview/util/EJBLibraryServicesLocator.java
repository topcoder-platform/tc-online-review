/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import org.springframework.context.ApplicationContext;

import com.cronos.onlinereview.ejblibrary.SpringContextProvider;
import com.topcoder.security.login.LoginRemote;
import com.topcoder.service.contest.eligibility.dao.ContestEligibilityManager;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityService;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidationManager;
import com.topcoder.web.ejb.forums.Forums;
import com.topcoder.web.ejb.user.UserPreference;

/**
 * <p>A service locator for classes implementing library-style calls to various EJBs.</p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class EJBLibraryServicesLocator {

    /**
     * <p>Constructs new <code>EJBLibraryServicesLocator</code> instance. This implementation does nothing.</p>
     */
    private EJBLibraryServicesLocator() {
    }

    /**
     * <p>Gets the library interface to <code>Forums</code> service.</p>
     *
     * @return a <code>Forums</code> implementation providing library-style calls to <code>Forums EJB</code>.
     */
    public static Forums getForumsService() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return (Forums) appContext.getBean("forumsLibrary");
    }

    /**
     * <p>Gets the library interface to <code>Login</code> service.</p>
     *
     * @return a <code>LoginRemote</code> implementation providing library-style calls to <code>Login EJB</code>.
     */
    public static LoginRemote getLoginService() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return (LoginRemote) appContext.getBean("loginLibrary");
    }

    /**
     * <p>Gets the library interface to <code>Contest Eligibility</code> service.</p>
     *
     * @return a <code>ContestEligibilityService</code> implementation providing library-style calls to
     *         <code>Contest Eligibility EJB</code>.
     */
    public static ContestEligibilityService getContestEligibilityService() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return (ContestEligibilityService) appContext.getBean("contestEligibilityServiceLibrary");
    }

    /**
     * <p>Gets the library interface to <code>Contest Eligibility Management</code> service.</p>
     *
     * @return a <code>ContestEligibilityManager</code> implementation providing library-style calls to
     *         <code>Contest Eligibility Management EJB</code>.
     */
    public static ContestEligibilityManager getContestEligibilityManager() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return (ContestEligibilityManager) appContext.getBean("contestEligibilityManagerLibrary");
    }

    /**
     * <p>Gets the library interface to <code>Contest Eligibility Validation</code> service.</p>
     *
     * @return a <code>ContestEligibilityValidationManager</code> implementation providing library-style calls to
     *         <code>Contest Eligibility Validation EJB</code>.
     */
    public static ContestEligibilityValidationManager getContestEligibilityValidationManager() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return (ContestEligibilityValidationManager) appContext.getBean("contestEligibilityValidationManagerLibrary");
    }

    /**
     * <p>
     * Gets the library interface to <code>User Preference</code> service.
     * </p>
     *
     * @return a <code>UserPreference</code> implementation providing
     *         library-style calls to <code>User Preference EJB</code>.
     */
    public static UserPreference getUserPreference() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return (UserPreference) appContext.getBean("userPreferencelibrary");
    }
}
