/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;

import com.topcoder.web.ejb.user.UserTermsOfUse;

/**
 * Mock class for testing purpose.
 *
 * @author TCSDEVELOPER
 * @version 1.6
 * @since 1.6
 */
public class MockUserTermsOfUse implements UserTermsOfUse {

    /**
     * Checks whether has terms of use.
     *
     * @param userId
     *            the user id.
     * @param termsId
     *            the terms id.
     * @param commonOltpDatasourceName
     *            the datasource name.
     * @return the flag
     */
    public boolean hasTermsOfUse(long userId, long termsId, String commonOltpDatasourceName) {
        return false;
    }

    /**
     * Returns the EJB home.
     *
     * @return the ejb home.
     */
    public EJBHome getEJBHome() {
        return null;
    }

    /**
     * Returns the handle.
     *
     * @return the handle.
     */
    public Handle getHandle() {
        return null;
    }

    /**
     * Returns the primary key.
     *
     * @return the primary key.
     */
    public Object getPrimaryKey() {
        return null;
    }

    /**
     * Returns whether it is identical.
     *
     *@param ejbObject
     *            ejb object
     * @return whether it is identical.
     */
    public boolean isIdentical(EJBObject ejbObject) {
        return false;
    }

    /**
     * Do removal.
     */
    public void remove() {
    }

}
