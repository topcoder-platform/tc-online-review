/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.topcoder.web.ejb.project.ProjectRoleTermsOfUse;
import com.topcoder.web.ejb.project.ProjectRoleTermsOfUseBean;

import javax.ejb.EJBException;
import java.util.List;

/**
 * <p>An implementation of {@link ProjectRoleTermsOfUse} interface which provides the library-call style for API of
 * <code>Project Role Terms Of Use EJB</code>.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * <p>
 * Version 1.0.1 (Milestone Support Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added constructor with namespace.</li>
 *   </ol>
 * </p>
 *
 * @author isv, TCSDEVELOPER
 * @version 1.0 (TopCoder Online Review Switch To Local Calls)
 */
public class ProjectRoleTermsOfUseLibrary extends BaseEJBLibrary implements ProjectRoleTermsOfUse {

    /**
     * <p>A <code>ProjectRoleTermsOfUseBean</code> which is delegated the processing of the calls to methods of this
     * class.</p>
     */
    private ProjectRoleTermsOfUseBean bean;

    /**
     * <p>Constructs new <code>ProjectRoleTermsOfUseLibrary</code> instance.</p>
     */
    public ProjectRoleTermsOfUseLibrary() {
        this.bean = new ProjectRoleTermsOfUseBean();
    }

    /**
     * <p>Constructs new <code>ProjectRoleTermsOfUseLibrary</code> instance.</p>
     * 
     * @param namespace a <code>String</code> providing the configuration namespace. 
     * @since 1.0.1 
     */
    public ProjectRoleTermsOfUseLibrary(String namespace) {
    }

    /**
     * <p>Creates a project role terms of use association.</p>
     *
     * @param projectId a <code>long</code> providing the project ID.
     * @param resourceRoleId a <code>long</code> providing the resource role ID.
     * @param sortOrder an <code>int</code> specifying the association sort order.
     * @param termsOfUseId a <code>long</code> providing the ID of terms of use to associate.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     */
    public void createProjectRoleTermsOfUse(int projectId, int resourceRoleId, long termsOfUseId, int sortOrder,
                                            String dataSource) {
        this.bean.createProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, sortOrder, dataSource);
    }

    /**
     * <p>Removes a project role terms of use association.</p>
     *
     * @param projectId a <code>long</code> providing the project ID.
     * @param resourceRoleId a <code>long</code> providing the resource role ID.
     * @param termsOfUseId a <code>long</code> providing the ID of terms of use to remove.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     */
    public void removeProjectRoleTermsOfUse(int projectId, int resourceRoleId, long termsOfUseId, String dataSource) {
        this.bean.removeProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, dataSource);
    }

    /**
     * <p>Checks if a project role terms of use association exists.</p>
     *
     * @param projectId a <code>long</code> providing the project ID.
     * @param resourceRoleId a <code>long</code> providing the resource role ID.
     * @param termsOfUseId a <code>long</code> providing the ID of terms of use to check.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     * @return true if the project role terms of use association exists
     */
    public boolean hasProjectRoleTermsOfUse(int projectId, int resourceRoleId, long termsOfUseId, String dataSource) {
        return this.bean.hasProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, dataSource);
    }

    /**
     * <p>Gets a list of associated terms of use ids to specific project id and roles.</p>
     *
     * @param projectId a <code>long</code> providing the project ID.
     * @param resourceRoleIds a <code>long</code> array providing the resource role IDs.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     * @return an array of <code>List<Long></code> containing associated the terms of use ids in sort_order position
     */
    public List<Long>[] getTermsOfUse(int projectId, int[] resourceRoleIds, String dataSource) {
        return this.bean.getTermsOfUse(projectId, resourceRoleIds, dataSource);
    }

    /**
     * <p>Removes all project role terms of use association for a given project.</p>
     *
     * @param projectId a <code>long</code> providing the project ID.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     */
    public void removeAllProjectRoleTermsOfUse(int projectId, String dataSource) throws EJBException {
        this.bean.removeAllProjectRoleTermsOfUse(projectId, dataSource);
    }
}
