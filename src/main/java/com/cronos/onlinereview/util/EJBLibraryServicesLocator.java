/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import com.cronos.onlinereview.ejblibrary.SpringContextProvider;
import com.topcoder.onlinereview.component.contest.ContestEligibilityManager;
import com.topcoder.onlinereview.component.contest.ContestEligibilityService;
import com.topcoder.onlinereview.component.contest.ContestEligibilityValidationManager;
import com.topcoder.onlinereview.component.security.login.LoginRemote;
import com.topcoder.web.ejb.user.UserPreference;
import org.springframework.context.ApplicationContext;

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
        return  appContext.getBean(ContestEligibilityService.class);
    }

    /**
     * <p>Gets the library interface to <code>Contest Eligibility Management</code> service.</p>
     *
     * @return a <code>ContestEligibilityManager</code> implementation providing library-style calls to
     *         <code>Contest Eligibility Management EJB</code>.
     */
    public static ContestEligibilityManager getContestEligibilityManager() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return appContext.getBean(ContestEligibilityManager.class);
    }

    /**
     * <p>Gets the library interface to <code>Contest Eligibility Validation</code> service.</p>
     *
     * @return a <code>ContestEligibilityValidationManager</code> implementation providing library-style calls to
     *         <code>Contest Eligibility Validation EJB</code>.
     */
    public static ContestEligibilityValidationManager getContestEligibilityValidationManager() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return appContext.getBean(ContestEligibilityValidationManager.class);
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
