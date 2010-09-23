/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.topcoder.security.GeneralSecurityException;
import com.topcoder.security.TCSubject;
import com.topcoder.security.login.AuthenticationException;
import com.topcoder.security.login.LoginBean;
import com.topcoder.security.login.LoginRemote;

/**
 * <p>An implementation of {@link LoginRemote} interface which provides the library-call style for API of <code>Login
 * EJB</code>.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author isv
 * @version 1.0 (TopCoder Online Review Switch To Local Calls)
 */
public class LoginLibrary extends BaseEJBLibrary implements LoginRemote {

    /**
     * <p>A <code>LoginBean</code> which is delegated the processing of the calls to methods of this class.</p>
     */
    private LoginBean bean;

    /**
     * <p>Constructs new <code>LoginLibrary</code> instance.</p>
     */
    public LoginLibrary() {
        this.bean = new LoginBean();
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
}
