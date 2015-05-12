/*
 * Copyright (C) 2005 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tmatesoft.svn.core.SVNException;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.PersistenceResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistence;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.persistence.sql.SqlResourcePersistence;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.resource.search.NotificationTypeFilterBuilder;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.resource.search.ResourceRoleFilterBuilder;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.datavalidator.LongValidator;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.datavalidator.StringValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * The extend from FinalReviewPhaseHandler to add on the logic to push data to project_result.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRFinalReviewPhaseHandler extends FinalReviewPhaseHandler {
    
    /**
    * Used for pulling data to project_result table and filling payments.
    */
    private final PRHelper prHelper = new PRHelper();
    
    /**
     * <p>A <code>String</code> providing the name of resource property providing the flag indicating whether the
     * resource has permission for accessing the project's SVN module set or not.</p>
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

        prHelper.processFinalReviewPR(phase.getProject().getId(), toStart, operator);

        // If stopping phase and final fix is approved.
        if (!toStart && !checkFinalReview(phase)) {
            // checks the existence of approval phase
            Phase approvalPhase = PhasesHelper.locatePhase(phase, "Approval", true, false);

            if (approvalPhase == null) {
                try {
                    // check "Approval Required" project property
                    ProjectManager projectManager = getManagerHelper().getProjectManager();
                    com.topcoder.management.project.Project project = projectManager.getProject(phase.getProject()
                            .getId());

                    if (!"true".equalsIgnoreCase((String) project.getProperty("Approval Required"))) {
                        // update project status to Complete
                        PRHelper.completeProject(getManagerHelper(), phase, operator);
                    }
                } catch (PersistenceException e) {
                    throw new PhaseHandlingException("Problem when retrieving project", e);
                }
            }
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
            long projectId = phase.getProject().getId();
            ProjectManager projectManager = new ProjectManagerImpl();
            Project project = projectManager.getProject(projectId);
            String svnModule = (String) project.getProperty("SVN Module");
            if ((svnModule != null) && (svnModule.trim().length() > 0)) {
                // Create SVN Module
                SVNHelper.createSVNDirectory(svnModule);

                // Find the resources which are to be granted permission for accessing SVN module
                ConfigManager cfgMgr = ConfigManager.getInstance();
                Property svnPermissionGrantResourceRolesConfig
                    = cfgMgr.getPropertyObject("com.cronos.OnlineReview", "SVNPermissionGrantResourceRoles");
                String[] allowedRoles = svnPermissionGrantResourceRolesConfig.getValues();

                List<Filter> resourceRoleFilters = new ArrayList<Filter>();
                for (String roleId : allowedRoles) {
                    resourceRoleFilters.add(ResourceFilterBuilder.createResourceRoleIdFilter(Long.parseLong(roleId)));
                }
                Filter resourceRolesFilter = new OrFilter(resourceRoleFilters);
                Filter projectIdFilter = ResourceFilterBuilder.createProjectIdFilter(projectId);
                Filter filter = new AndFilter(resourceRolesFilter, projectIdFilter);
                ResourceManager resourceManager = getManagerHelper().getResourceManager();
                Resource[] resources = resourceManager.searchResources(filter);

                // Collect the list of resources which indeed need to have permission granted
                Long winnerId;
                try {
                    winnerId = Long.parseLong((String) project.getProperty("Winner External Reference ID"));
                } catch (NumberFormatException nfe) {
                    winnerId = null;
                }

                Map<String, List<Resource>> candidates = new HashMap<String, List<Resource>>();
                for (Resource resource : resources) {
                    // Of resources with Submitter role only a winning Submitter is to be granted a permission
                    if (resource.getResourceRole().getId() == 1) {
                        if (winnerId == null || !winnerId.equals(resource.getUserId())) {
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
        } catch (com.topcoder.management.project.ConfigurationException e) {
            throw new PhaseHandlingException("Failed to create ProjectManager", e);
        }
    }

    /**
     * This method is called from perform method when the phase is stopping. It
     * checks if the final review is rejected.
     *
     * @param phase phase instance.
     * @return if pass the final review of not
     *
     * @throws PhaseHandlingException if an error occurs when retrieving/saving
     *         data.
     */
    private boolean checkFinalReview(Phase phase) throws PhaseHandlingException {
        try {
            ManagerHelper managerHelper = getManagerHelper();
            Review finalWorksheet = PhasesHelper.getWorksheet(managerHelper, phase.getId());

            // check for approved/rejected comments.
            Comment[] comments = finalWorksheet.getAllComments();
            boolean rejected = false;

            for (Comment comment : comments) {
                String value = (String) comment.getExtraInfo();

                if (comment.getCommentType().getName().equals("Final Review Comment")) {
                    if (Constants.COMMENT_VALUE_APPROVED.equalsIgnoreCase(value) || Constants.COMMENT_VALUE_ACCEPTED.equalsIgnoreCase(value)) {
                        continue;
                    } else if (Constants.COMMENT_VALUE_REJECTED.equalsIgnoreCase(value)) {
                        rejected = true;

                        break;
                    } else {
                        throw new PhaseHandlingException("Comment can either be Approved or Rejected.");
                    }
                }
            }

            return rejected;
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        }
    }
}
