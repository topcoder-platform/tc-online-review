/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.util.List;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;

import com.topcoder.web.ejb.project.ProjectRoleTermsOfUse;

/**
 * Mock ProjectRoleTermsOfUse.
 * Change in 1.6.1:
 * unchecked to rawtypes
 * @author TCSDEVELOPER, microsky
 * @version 1.6.1
 * @since 1.6
 */
public class MockProjectRoleTermsOfUse implements ProjectRoleTermsOfUse {

    /**
     * Returns the list of terms of use.
     * @param projectId
     *            project id
     * @param is
     *            int array
     * @param commonOltpDatasourceName
     *            common oltp datasource name
     * @return the list of terms of use.
     */
    @SuppressWarnings("rawtypes")
    public List[] getTermsOfUse(int projectId, int[] is, String commonOltpDatasourceName) {
        return new List[0];
    }

    /**
     * Returns the EJB home.
     * @return the ejb home.
     */
    public EJBHome getEJBHome() {
        return null;
    }

    /**
     * Returns the handle.
     * @return the handle.
     */
    public Handle getHandle() {
        return null;
    }

    /**
     * Returns the primary key.
     * @return the primary key.
     */
    public Object getPrimaryKey() {
        return null;
    }

    /**
     * Returns whether it is identical.
     * @param ejbObject
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
