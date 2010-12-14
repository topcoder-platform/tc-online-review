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

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.phase.PhaseHandlingException;
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
                ResourceManager resourceManager = getResourceManager();
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
        } catch (com.topcoder.management.project.ConfigurationException e) {
            throw new PhaseHandlingException("Failed to create ProjectManager", e);
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

    /**
     * <p>
     * Returns a <code>ResourceManager</code> instance. This is used in <code>UploadServices</code> to retrieve this
     * manager and perform all its operations.
     * </p>
     *
     * @return a <code>ResourceManager</code> instance
     * @throws PhaseHandlingException if an unexpected error occurs.
     */
    private static ResourceManager getResourceManager() throws PhaseHandlingException {
        try {
            // get connection factory
            DBConnectionFactory dbconn
                = new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
            
            // get the persistence
            ResourcePersistence persistence = new SqlResourcePersistence(dbconn);
            // get the id generators
            IDGenerator resourceIdGenerator = IDGeneratorFactory
                    .getIDGenerator(PersistenceResourceManager.RESOURCE_ID_GENERATOR_NAME);
            IDGenerator resourceRoleIdGenerator = IDGeneratorFactory
                    .getIDGenerator(PersistenceResourceManager.RESOURCE_ROLE_ID_GENERATOR_NAME);
            IDGenerator notificationTypeIdGenerator = IDGeneratorFactory
                    .getIDGenerator(PersistenceResourceManager.NOTIFICATION_TYPE_ID_GENERATOR_NAME);
            // get the search bundles
            SearchBundleManager searchBundleManager = new SearchBundleManager("com.topcoder.searchbuilder.common");
            SearchBundle resourceSearchBundle = searchBundleManager
                    .getSearchBundle(PersistenceResourceManager.RESOURCE_SEARCH_BUNDLE_NAME);
            // set it searchable
            setAllFieldsSearchable(resourceSearchBundle);
            SearchBundle resourceRoleSearchBundle = searchBundleManager
                    .getSearchBundle(PersistenceResourceManager.RESOURCE_ROLE_SEARCH_BUNDLE_NAME);
            // set it searchable
            setAllFieldsSearchable(resourceRoleSearchBundle);
            SearchBundle notificationSearchBundle = searchBundleManager
                    .getSearchBundle(PersistenceResourceManager.NOTIFICATION_SEARCH_BUNDLE_NAME);
            // set it searchable
            setAllFieldsSearchable(notificationSearchBundle);
            SearchBundle notificationTypeSearchBundle = searchBundleManager
                    .getSearchBundle(PersistenceResourceManager.NOTIFICATION_TYPE_SEARCH_BUNDLE_NAME);
            // set it searchable
            setAllFieldsSearchable(notificationTypeSearchBundle);
            // initialize the PersistenceResourceManager
            return new PersistenceResourceManager(persistence, resourceSearchBundle, resourceRoleSearchBundle,
                    notificationSearchBundle, notificationTypeSearchBundle, resourceIdGenerator,
                    resourceRoleIdGenerator, notificationTypeIdGenerator);
        } catch (Exception e) {
            throw new PhaseHandlingException("Exception occurred while creating the resource manager.", e);
        }
    }

    /**
     * Sets the searchable fields to the search bundle.
     *
     * @param searchBundle the search bundle to set.
     */
    private static void setAllFieldsSearchable(SearchBundle searchBundle) {
        Map<String, ObjectValidator> fields = new HashMap<String, ObjectValidator>();

        // set the resource filter fields
        fields.put(ResourceFilterBuilder.RESOURCE_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.PHASE_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.PROJECT_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.SUBMISSION_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.RESOURCE_ROLE_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.EXTENSION_PROPERTY_NAME_FIELD_NAME, StringValidator.startsWith(""));
        fields.put(ResourceFilterBuilder.EXTENSION_PROPERTY_VALUE_FIELD_NAME, StringValidator.startsWith(""));

        // set the resource role filter fields
        fields.put(ResourceRoleFilterBuilder.NAME_FIELD_NAME, StringValidator.startsWith(""));
        fields.put(ResourceRoleFilterBuilder.PHASE_TYPE_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(ResourceRoleFilterBuilder.RESOURCE_ROLE_ID_FIELD_NAME, LongValidator.isPositive());

        // set the notification filter fields
        fields.put(NotificationFilterBuilder.EXTERNAL_REF_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(NotificationFilterBuilder.NOTIFICATION_TYPE_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(NotificationFilterBuilder.PROJECT_ID_FIELD_NAME, LongValidator.isPositive());

        // set the notification type filter fields
        fields.put(NotificationTypeFilterBuilder.NOTIFICATION_TYPE_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(NotificationTypeFilterBuilder.NAME_FIELD_NAME, StringValidator.startsWith(""));

        searchBundle.setSearchableFields(fields);
    }
}
