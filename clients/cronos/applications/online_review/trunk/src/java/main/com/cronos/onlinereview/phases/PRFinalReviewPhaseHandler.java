/**
 * Copyright (C) 2005-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cronos.onlinereview.actions.ConfigHelper;
import com.cronos.onlinereview.actions.ManagerCreationHelper;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;
import org.tmatesoft.svn.core.SVNException;

/**
 * The PRFinalReviewPhaseHandler.
 *
 * <p>
 * Version 1.0.1 (SVN Automation and Late Deliverables Tracker Integration Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Implemented the logic for initializing the SVN module and granting access permissions to intended resources
 *     in case Final Review phase starts and project has SVN module specified by settings.</li>
 *   </ol>
 * </p>

 * @author brain_cn, isv
 * @version 1.0.1
 */
public class PRFinalReviewPhaseHandler extends FinalReviewPhaseHandler {

    /**
     * <p>A <code>String</code> providing the name of resource property providing the flag indicating whether the
     * resource has permission for accessing the project's SVN module set or not.</p>
     *
     * @since 1.0.1
     */
    private static final String SVN_PERMISSION_ADDED_RESOURCE_INFO = "SVN Permission Added";

    /**
     * Create a new instance of FinalReviewPhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
    public PRFinalReviewPhaseHandler() throws ConfigurationException {
        super();
    }

    /**
     * Create a new instance of FinalReviewPhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRFinalReviewPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Provides additional logic to execute a phase. this extension will update placed, final_score field of
     * project_result table.</p>
     *
     * @param phase    The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not "Submission" type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        // If phase is to be open then create SVN repository and grant access to it to appropriate project resources
        if (toStart) {
            prepareSVNModule(phase, operator);
        }

        super.perform(phase, operator);

        Connection conn = this.createConnection();
        try {
            processPR(phase.getProject().getId(), conn, toStart);
        } finally {
            PRHelper.close(conn);
        }
    }

    /**
     * <p>Prepares the SVN module for the project associated with the specified phase if settings for such a project
     * require SVN module initialization.</p>
     *
     * @param phase a <code>Phase</code> providing the details for <code>Final Review</code> phase which is going to be
     *        started.
     * @param operator a <code>String</code> referencing the operator attempting to advance the phase.
     * @throws PhaseHandlingException if an unexpected error occurs.
     */
    private void prepareSVNModule(Phase phase, String operator) throws PhaseHandlingException {
        try {
            ManagerCreationHelper managerHelper = new ManagerCreationHelper();
            long projectId = phase.getProject().getId();
            ProjectManager projectManager = managerHelper.getProjectManager();
            Project project = projectManager.getProject(projectId);
            String svnModule = (String) project.getProperty("SVN Module");
            if ((svnModule != null) && (svnModule.trim().length() > 0)) {
                // Create SVN Module
                SVNHelper.createSVNDirectory(svnModule);

                // Find the resources which are to be granted permission for accessing SVN module
                String[] allowedRoles = ConfigHelper.getSvnPermissionGrantResourceRoles();
                List<Filter> resourceRoleFilters = new ArrayList<Filter>();
                for (String roleId : allowedRoles) {
                    resourceRoleFilters.add(ResourceFilterBuilder.createResourceRoleIdFilter(Long.parseLong(roleId)));
                }
                Filter resourceRolesFilter = new OrFilter(resourceRoleFilters);
                Filter projectIdFilter = ResourceFilterBuilder.createProjectIdFilter(projectId);
                Filter filter = new AndFilter(resourceRolesFilter, projectIdFilter);
                ResourceManager resourceManager = managerHelper.getResourceManager();
                Resource[] resources = resourceManager.searchResources(filter);

                // Collect the list of resources which indeed need to have permission granted
                String winnerId = (String) project.getProperty("Winner External Reference ID");
                Map<String, List<Resource>> candidates = new HashMap<String, List<Resource>>();
                for (int i = 0; i < resources.length; i++) {
                    Resource resource = resources[i];

                    // Of resources with Submitter role only a winning Submitter is to be granted a permission
                    if (resource.getResourceRole().getId() == 1) {
                        if ((winnerId == null) || !(winnerId.equals(resource.getProperty("External Reference ID")))) {
                            continue;
                        }
                    }

                    String svnPermissionAddedProperty
                        = (String) resource.getProperty(SVN_PERMISSION_ADDED_RESOURCE_INFO);
                    if (!"true".equals(svnPermissionAddedProperty)) {
                        String handle = (String) resource.getProperty(PhasesHelper.HANDLE);
                        if (!candidates.containsKey(handle)) {
                            candidates.put(handle, new ArrayList<Resource>());
                        }
                        List<Resource> userResources = candidates.get(handle);
                        userResources.add(resource);
                    }
                }

                // Grant permissions for accessing project's SVN module to intended resources
                if (!candidates.isEmpty()) {
                    String[] handles = candidates.keySet().toArray(new String[candidates.size()]);
                    SVNHelper.grantSVNPermission(svnModule, handles, "rw");
                    for (Map.Entry<String, List<Resource>> entry : candidates.entrySet()) {
                        List<Resource> userResources = entry.getValue();
                        for (Resource resource : userResources) {
                            resource.setProperty(SVN_PERMISSION_ADDED_RESOURCE_INFO, "true");
                            resourceManager.updateResource(resource, operator);
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("Failed to access resources", e);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Failed to access resources", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Failed to access resources", e);
        } catch (SVNException e) {
            throw new PhaseHandlingException("Failed to access SVN repository", e);
        } catch (IOException e) {
            throw new PhaseHandlingException("Failed to access SVN repository", e);
        }
    }

    /**
     * Pull data to project_result.
     *
     * @param projectId the projectId
     * @throws PhaseHandlingException if error occurs
     */
    public void processPR(long projectId, Connection conn, boolean toStart) throws PhaseHandlingException {
        try {
            PRHelper.processFinalReviewPR(projectId, conn, toStart);
        } catch (SQLException e) {
            throw new PhaseHandlingException("Failed to push data to project_result", e);
        }
    }
}
