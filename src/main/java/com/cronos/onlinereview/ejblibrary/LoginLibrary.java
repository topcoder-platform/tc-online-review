/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.topcoder.onlinereview.component.security.GeneralSecurityException;
import com.topcoder.onlinereview.component.security.TCSubject;
import com.topcoder.onlinereview.component.security.login.AuthenticationException;
import com.topcoder.onlinereview.component.security.login.LoginBean;
import com.topcoder.onlinereview.component.security.login.LoginInterface;

import java.rmi.RemoteException;

/**
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class LoginLibrary implements LoginInterface {

    /**
     * <p>A <code>LoginBean</code> which is delegated the processing of the calls to methods of this class.</p>
     */
    private final LoginBean bean;

    /**
     * <p>Constructs new <code>LoginLibrary</code> instance.</p>
     *
     * @param impersonationRoleId a <code>long</code> providing the ID for impersonation role.
     */
    public LoginLibrary(Long impersonationRoleId) {
        this.bean = new Extender(impersonationRoleId);
    }

    /**
     * <p>Logs the user to application using specified credentials for authentication.</p>
     *
     * @param username a <code>String</code> providing the username for user authentication.
     * @param password a <code>String</code> providing the plain text password for user authentication.
     * @return a <code>TCSubject</code> providing the identity for the user successfully authenticated to application.
     * @throws AuthenticationException if provided credentials are invalid.
     * @throws GeneralSecurityException if an unexpected error occurs while authenticating user to application.
     */
    public TCSubject login(String username, String password) throws GeneralSecurityException {
        return this.bean.login(username, password);
    }

    /**
     * <p>Logs the user to application using the specified credentials. Checks username and password, returns a
     * TCSubject representation of the user that includes the user's roles.</p>
     *
     * @param username a <code>String</code> providing the username for user authentication.
     * @param password a <code>String</code> providing the plain text password for user authentication.
     * @param dataSource a <code>String</code> referencing the data source to be used for establishing connection to
     *        target database.
     * @return a <code>TCSubject</code> providing the identity for the user successfully authenticated to application.
     * @throws AuthenticationException if provided credentials are invalid.
     * @throws GeneralSecurityException if an unexpected error occurs while authenticating user to application.
     */
    public TCSubject login(String username, String password, String dataSource) throws GeneralSecurityException {
        return this.bean.login(username, password, dataSource);
    }

    @Override
    public boolean isCloudSpokesUser(String s) throws RemoteException, GeneralSecurityException {
        return this.bean.isCloudSpokesUser(s);
    }

    /**
     * <p>An extension to wrapped bean class providing access to it's protected methods.</p>
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    private static class Extender extends LoginBean {

        /**
         * <p>A <code>long</code> providing the ID for impersonation role.</p>
         */
        private final Long impersonationRoleId;

        /**
         * <p>Constructs new <code>Extender</code> instance. This implementation does nothing.</p>
         *
         * @param impersonationRoleId a <code>long</code> providing the ID for impersonation role.
         */
        private Extender(Long impersonationRoleId) {
            this.impersonationRoleId = impersonationRoleId;
        }

        /**
         * <p>Gets the ID for the role allowing impersonated logins.</p>
         *
         * @return a <code>long</code> providing the ID for impersonation role.
         */
        @Override
        protected Long getImpersonationRoleId() {
            return this.impersonationRoleId;
        }
    }
}
