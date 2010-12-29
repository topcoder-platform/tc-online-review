/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.cronos.onlinereview.ejblibrary.SpringContextProvider;
import com.topcoder.security.login.LoginRemote;
import com.topcoder.service.contest.eligibility.dao.ContestEligibilityManager;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityService;
import com.topcoder.service.contest.eligibilityvalidation.ContestEligibilityValidationManager;
import com.topcoder.web.ejb.forums.Forums;
import com.topcoder.web.ejb.project.ProjectRoleTermsOfUse;
import com.topcoder.web.ejb.termsofuse.TermsOfUse;
import com.topcoder.web.ejb.user.UserTermsOfUse;
import org.springframework.context.ApplicationContext;

/**
 * <p>A service locator for classes implementing library-style calls to various EJBs.</p>
 *
 * @author isv
 * @version 1.0 (TopCoder Online Review Switch To Local Calls)
 */
public class EJBLibraryServicesLocator {

    /**
     * <p>Constructs new <code>EJBLibraryServicesLocator</code> instance. This implementation does nothing.</p>
     */
    private EJBLibraryServicesLocator() {
    }

    /**
     * <p>Gets the library interface to <code>User Terms Of Use</code> service.</p>
     *
     * @return a <code>UserTermsOfUse</code> implementation providing library-style calls to <code>User Terms Of Use EJB
     *         </code>.
     */
    public static UserTermsOfUse getUserTermsOfUseService() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return (UserTermsOfUse) appContext.getBean("userTermsOfUseLibrary");
    }

    /**
     * <p>Gets the library interface to <code>Terms Of Use</code> service.</p>
     *
     * @return a <code>TermsOfUse</code> implementation providing library-style calls to <code>Terms Of Use EJB</code>.
     */
    public static TermsOfUse getTermsOfUseService() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return (TermsOfUse) appContext.getBean("termsOfUseLibrary");
    }

    /**
     * <p>Gets the library interface to <code>Project Role Terms Of Use</code> service.</p>
     *
     * @return a <code>ProjectRoleTermsOfUse</code> implementation providing library-style calls to <code>Project Role
     *         Terms Of Use EJB</code>.
     */
    public static ProjectRoleTermsOfUse getProjectRoleTermsOfUseService() {
        ApplicationContext appContext = SpringContextProvider.getApplicationContext();
        return (ProjectRoleTermsOfUse) appContext.getBean("projectRoleTermsOfUseLibrary");
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
}
