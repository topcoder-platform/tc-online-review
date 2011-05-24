/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.topcoder.web.ejb.user.UserTermsOfUse;
import com.topcoder.web.ejb.user.UserTermsOfUseBean;

/**
 * <p>An implementation of {@link UserTermsOfUse} interface which provides the library-call style for API of
 * <code>User Terms Of Use EJB</code>.</p>
 *
 * <p>
 * Version 1.0.1 (Milestone Support Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added constructor with namespace.</li>
 *   </ol>
 * </p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author isv, TCSDEVELOPER
 * @version 1.0.1 (TopCoder Online Review Switch To Local Calls)
 */
public class UserTermsOfUseLibrary extends BaseEJBLibrary implements UserTermsOfUse {

    /**
     * <p>A <code>UserTermsOfUseBean</code> which is delegated the processing of the calls to methods of this class.</p>
     */
    private UserTermsOfUseBean bean;

    /**
     * <p>Constructs new <code>UserTermsOfUseLibrary</code> instance.</p>
     */
    public UserTermsOfUseLibrary() {
        this.bean = new UserTermsOfUseBean();
    }

    /**
     * <p>Constructs new <code>UserTermsOfUseLibrary</code> instance.</p>
     * 
     * @param namespace a <code>String</code> providing the configuration namespace.
     * @since 1.0.1 
     */
    public UserTermsOfUseLibrary(String namespace) {
    }

    /**
     * <p>Records the fact of acceptance of specified terms of use by specified user.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @param termsOfUseId a <code>long</code> providing the terms of use ID.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     */
    public void createUserTermsOfUse(long userId, long termsOfUseId, String dataSource) {
        this.bean.createUserTermsOfUse(userId, termsOfUseId, dataSource);
    }

    /**
     * <p>Removes the record on the fact of acceptance of specified terms of use by specified user.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @param termsOfUseId a <code>long</code> providing the terms of use ID.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     */
    public void removeUserTermsOfUse(long userId, long termsOfUseId, String dataSource) {
        this.bean.removeUserTermsOfUse(userId, termsOfUseId, dataSource);
    }

    /**
     * <p>Checks if there is a record on the fact of acceptance of specified terms of use by specified user.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @param termsOfUseId a <code>long</code> providing the terms of use ID.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     * @return <code>true</code> if specified user accepted the specified terms of use; <code>false</code> otherwise.
     */
    public boolean hasTermsOfUse(long userId, long termsOfUseId, String dataSource) {
        return this.bean.hasTermsOfUse(userId, termsOfUseId, dataSource);
    }
}
