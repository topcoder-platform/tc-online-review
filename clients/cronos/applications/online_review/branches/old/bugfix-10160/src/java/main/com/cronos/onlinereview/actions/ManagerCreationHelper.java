/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.autoscreening.management.ConfigurationException;
import com.cronos.onlinereview.autoscreening.management.ScreeningManager;
import com.cronos.onlinereview.autoscreening.management.ScreeningManagerFactory;
import com.cronos.onlinereview.phases.AppealsPhaseHandler;
import com.cronos.onlinereview.phases.ApprovalPhaseHandler;
import com.cronos.onlinereview.phases.PRAggregationPhaseHandler;
import com.cronos.onlinereview.phases.PRAggregationReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRAppealResponsePhaseHandler;
import com.cronos.onlinereview.phases.PRFinalFixPhaseHandler;
import com.cronos.onlinereview.phases.PRFinalReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRRegistrationPhaseHandler;
import com.cronos.onlinereview.phases.PRReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRScreeningPhaseHandler;
import com.cronos.onlinereview.phases.PRSubmissionPhaseHandler;
import com.cronos.onlinereview.services.uploads.ManagersProvider;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.PersistenceUploadManager;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistence;
import com.topcoder.management.deliverable.persistence.sql.SqlUploadPersistence;
import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseOperationEnum;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.PersistenceResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistence;
import com.topcoder.management.resource.persistence.sql.SqlResourcePersistence;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.resource.search.NotificationTypeFilterBuilder;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.resource.search.ResourceRoleFilterBuilder;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.util.datavalidator.LongValidator;
import com.topcoder.util.datavalidator.StringValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * This class implements {@link ManagersProvider} and provides the implementation for creating the managers.
 * 
 * @author evilisneo
 * @version 1.0
 */
public class ManagerCreationHelper implements ManagersProvider {

    /**
     * This member variable is a string constant that defines the name of the configuration namespace which the
     * parameters for database connection factory are stored under.
     */
    private static final String DB_CONNECTION_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * Used for caching the created the manager.
     */
    private PhaseManager phaseManager = null;
    /**
     * Used for caching the created the manager.
     */
    private UploadManager uploadManager = null;
    /**
     * Used for caching the created the manager.
     */
    private ProjectManager projectManager = null;
    /**
     * Used for caching the created the manager.
     */
    private ResourceManager resourceManager = null;
    /**
     * Used for caching the created the manager.
     */
    private ScreeningManager screeningManager = null;

    /**
     * <p>
     * Returns a <code>PhaseManager</code> instance. This is used in <code>UploadServices</code> to retrieve
     * this manager and perform all its operations.
     * </p>
     * 
     * @return a <code>PhaseManager</code> instance
     * @see ManagersProvider#getPhaseManager()
     */
    public PhaseManager getPhaseManager() {
        if(phaseManager != null) {
            return phaseManager;
        }
        try {
            phaseManager = new DefaultPhaseManager("com.topcoder.management.phase");
            PhaseType[] phaseTypes = phaseManager.getAllPhaseTypes();
            // Register all the handles.
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRRegistrationPhaseHandler(),
                    Constants.REGISTRATION_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRSubmissionPhaseHandler(),
                    Constants.SUBMISSION_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRScreeningPhaseHandler(),
                    Constants.SCREENING_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRReviewPhaseHandler(),
                    Constants.REVIEW_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new AppealsPhaseHandler(),
                    Constants.APPEALS_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRAppealResponsePhaseHandler(),
                    Constants.APPEALS_RESPONSE_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRAggregationPhaseHandler(),
                    Constants.AGGREGATION_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRAggregationReviewPhaseHandler(),
                    Constants.AGGREGATION_REVIEW_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRFinalFixPhaseHandler(),
                    Constants.FINAL_FIX_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRFinalReviewPhaseHandler(),
                    Constants.FINAL_REVIEW_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new ApprovalPhaseHandler(),
                    Constants.APPROVAL_PHASE_NAME);
            return phaseManager;
        } catch (Exception e) {
            throw new ManagerCreationException("Exception occurred while creating the PhaseManager.", e);
        }
    }

    /**
     * <p>
     * Returns a <code>ProjectManager</code> instance. This is used in <code>UploadServices</code> to retrieve
     * this manager and perform all its operations.
     * </p>
     * 
     * @return a <code>ProjectManager</code> instance
     * @see ManagersProvider#getProjectManager()
     */
    public ProjectManager getProjectManager() {
        try {
            if(projectManager == null) {
                projectManager = new ProjectManagerImpl();
            }
            return projectManager;
        } catch (Exception e) {
            throw new ManagerCreationException("Exception occurred while creating the ProjectManager.", e);
        }
    }

    /**
     * <p>
     * Returns a <code>ResourceManager</code> instance. This is used in <code>UploadServices</code> to retrieve
     * this manager and perform all its operations.
     * </p>
     * 
     * @return a <code>ResourceManager</code> instance
     * @see ManagersProvider#getResourceManager
     */
    public ResourceManager getResourceManager() {
        if(resourceManager != null) {
            return resourceManager;
        }
        try {
            // get connection factory
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
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
            resourceManager = new PersistenceResourceManager(persistence, resourceSearchBundle, resourceRoleSearchBundle,
                    notificationSearchBundle, notificationTypeSearchBundle, resourceIdGenerator,
                    resourceRoleIdGenerator, notificationTypeIdGenerator);
            return resourceManager;
        } catch (Exception e) {
            throw new ManagerCreationException("Exception occurred while creating the resource manager.", e);
        }
    }

    /**
     * <p>
     * Returns a <code>ScreeningManager</code> instance. This is used in <code>UploadServices</code> to
     * retrieve this manager and perform all its operations.
     * </p>
     * 
     * @return a <code>ScreeningManager</code> instance
     * @see ManagersProvider#getScreeningManager()
     */
    public ScreeningManager getScreeningManager() {
        try {
            if(screeningManager == null) {
                screeningManager = ScreeningManagerFactory.createScreeningManager();
            }
            return screeningManager;
        } catch (ConfigurationException e) {
            throw new ManagerCreationException("Exception occurred while creating the ScreeningManager.", e);
        }
    }

    /**
     * <p>
     * Returns a <code>UploadManager</code> instance. This is used in <code>UploadServices</code> to retrieve
     * this manager and perform all its operations.
     * </p>
     * 
     * @return a <code>UploadManager</code> instance
     * @see ManagersProvider#getUploadManager()
     */
    public UploadManager getUploadManager() {
        if(uploadManager != null) {
            return uploadManager;
        }
        try {
            // Get connection factory
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            // Get the persistence
            UploadPersistence persistence = new SqlUploadPersistence(dbconn);
            // Get the ID generators
            IDGenerator uploadIdGenerator = IDGeneratorFactory
                    .getIDGenerator(PersistenceUploadManager.UPLOAD_ID_GENERATOR_NAME);
            IDGenerator uploadTypeIdGenerator = IDGeneratorFactory
                    .getIDGenerator(PersistenceUploadManager.UPLOAD_TYPE_ID_GENERATOR_NAME);
            IDGenerator uploadStatusIdGenerator = IDGeneratorFactory
                    .getIDGenerator(PersistenceUploadManager.UPLOAD_STATUS_ID_GENERATOR_NAME);
            IDGenerator submissionIdGenerator = IDGeneratorFactory
                    .getIDGenerator(PersistenceUploadManager.SUBMISSION_ID_GENERATOR_NAME);
            IDGenerator submissionStatusIdGenerator = IDGeneratorFactory
                    .getIDGenerator(PersistenceUploadManager.SUBMISSION_STATUS_ID_GENERATOR_NAME);
            // Get the search bundles
            SearchBundleManager searchBundleManager = new SearchBundleManager("com.topcoder.searchbuilder.common");
            SearchBundle uploadSearchBundle = searchBundleManager
                    .getSearchBundle(PersistenceUploadManager.UPLOAD_SEARCH_BUNDLE_NAME);
            SearchBundle submissionSearchBundle = searchBundleManager
                    .getSearchBundle(PersistenceUploadManager.SUBMISSION_SEARCH_BUNDLE_NAME);
            // Initialize the PersistenceUploadManager
            uploadManager = new PersistenceUploadManager(persistence, uploadSearchBundle, submissionSearchBundle,
                    uploadIdGenerator, uploadTypeIdGenerator, uploadStatusIdGenerator, submissionIdGenerator,
                    submissionStatusIdGenerator);
            return uploadManager;
        } catch (Exception e) {
            throw new ManagerCreationException("Exception occurred while creating the upload manager.", e);
        }
    }

    /**
     * Sets the searchable fields to the search bundle.
     * 
     * @param searchBundle
     *            the search bundle to set.
     */
    private static void setAllFieldsSearchable(SearchBundle searchBundle) {
        Map fields = new HashMap();

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

    /**
     * Sets the phase operation with the handler to the given phase manager.
     * 
     * @param manager
     *            the phase manager
     * @param phaseTypes
     *            the phase types
     * @param handler
     *            the handler to be registered
     * @param phaseName
     *            the current phase name.
     */
    private static void registerPhaseHandlerForOperation(PhaseManager manager, PhaseType[] phaseTypes,
            PhaseHandler handler, String phaseName) {
        manager.registerHandler(handler, ActionsHelper.findPhaseTypeByName(phaseTypes, phaseName),
                PhaseOperationEnum.START);
        manager.registerHandler(handler, ActionsHelper.findPhaseTypeByName(phaseTypes, phaseName),
                PhaseOperationEnum.END);
        manager.registerHandler(handler, ActionsHelper.findPhaseTypeByName(phaseTypes, phaseName),
                PhaseOperationEnum.CANCEL);
    }
}
