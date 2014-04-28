/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import java.io.Serializable;

import javax.ejb.EJBMetaData;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;

import com.cronos.onlinereview.util.EJBLibraryServicesLocator;
import com.topcoder.security.login.LoginRemote;
import com.topcoder.security.login.LoginRemoteHome;

/**
 * <p>An implementation of {@link LoginRemoteHome} interface to be used for injecting {@link LoginLibrary} class into
 * <code>Authentication Factory</code> and <code>Online Review Login</code> components used by
 * <code>Online Review</code> application. The instance of this class is expected to be bound to JNDI context under name
 * which {@link com.cronos.onlinereview.login.authenticator.SecurityManagerAuthenticator} expects the remote home
 * interface for <code>Login EJB</code> to be bound to.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class LoginLibraryHome implements LoginRemoteHome, Serializable {

    /**
     * <p>Constructs new <code>LoginLibraryHome</code> instance. This implementation does nothing.</p>
     */
    public LoginLibraryHome() {
    }

    /**
     * <p>Creates new <code>LoginRemote</code> instance.</p>
     *
     * @return a <code>LoginRemote</code> instance to be used for calling functionality of <code>Login EJB</code> in
     *         local library style.
     */
    public LoginRemote create() {
        return EJBLibraryServicesLocator.getLoginService() ;
    }

    /**
     * <p>This implementation does nothing.</p>
     *
     * @param handle a <code>Handle</code> to be removed. 
     */
    public void remove(Handle handle) {
    }

    /**
     * <p>This implementation does nothing.</p>
     *
     * @param o an <code>Object</code> to be removed.
     */
    public void remove(Object o) {
    }

    /**
     * <p>This implementation does nothing.</p>
     *
     * @return <code>null</code> always.
     */
    public EJBMetaData getEJBMetaData() {
        return null;
    }

    /**
     * <p>This implementation does nothing.</p>
     *
     * @return <code>null</code> always.
     */
    public HomeHandle getHomeHandle() {
        return null;
    }
}
