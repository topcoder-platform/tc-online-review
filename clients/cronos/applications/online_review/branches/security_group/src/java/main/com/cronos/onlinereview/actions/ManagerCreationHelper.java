/*
 * Copyright (C) 2007-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.phases.AppealsPhaseHandler;
import com.cronos.onlinereview.phases.MilestoneSubmissionPhaseHandler;
import com.cronos.onlinereview.phases.PRAggregationPhaseHandler;
import com.cronos.onlinereview.phases.PRAppealResponsePhaseHandler;
import com.cronos.onlinereview.phases.PRApprovalPhaseHandler;
import com.cronos.onlinereview.phases.PRFinalFixPhaseHandler;
import com.cronos.onlinereview.phases.PRFinalReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRMilestoneReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRMilestoneScreeningPhaseHandler;
import com.cronos.onlinereview.phases.PRPostMortemPhaseHandler;
import com.cronos.onlinereview.phases.PRRegistrationPhaseHandler;
import com.cronos.onlinereview.phases.PRReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRScreeningPhaseHandler;
import com.cronos.onlinereview.phases.PRSubmissionPhaseHandler;
import com.cronos.onlinereview.phases.SpecificationReviewPhaseHandler;
import com.cronos.onlinereview.phases.SpecificationSubmissionPhaseHandler;
import com.cronos.onlinereview.services.uploads.ManagersProvider;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.PersistenceUploadManager;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl;
import com.topcoder.management.deliverable.persistence.UploadPersistence;
import com.topcoder.management.deliverable.persistence.sql.SqlUploadPersistence;
import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseOperationEnum;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.project.link.ProjectLinkManager;
import com.topcoder.management.project.persistence.link.ProjectLinkManagerImpl;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.PersistenceResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistence;
import com.topcoder.management.resource.persistence.sql.SqlResourcePersistence;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.resource.search.NotificationTypeFilterBuilder;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.resource.search.ResourceRoleFilterBuilder;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
import com.topcoder.management.review.DefaultReviewManager;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;

import com.topcoder.management.deliverable.DeliverableChecker;
import com.topcoder.management.deliverable.DeliverableManager;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;
import com.topcoder.management.deliverable.persistence.DeliverablePersistence;
import com.topcoder.management.deliverable.persistence.sql.SqlDeliverablePersistence;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.deliverable.PersistenceDeliverableManager;
import com.cronos.onlinereview.deliverables.AggregationDeliverableChecker;
import com.cronos.onlinereview.deliverables.AppealResponsesDeliverableChecker;
import com.cronos.onlinereview.deliverables.ApprovalDeliverableChecker;
import com.cronos.onlinereview.deliverables.CommittedReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.FinalFixesDeliverableChecker;
import com.cronos.onlinereview.deliverables.FinalReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.IndividualReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.SpecificationSubmissionDeliverableChecker;
import com.cronos.onlinereview.deliverables.SubmissionDeliverableChecker;
import com.cronos.onlinereview.deliverables.SubmitterCommentDeliverableChecker;
import com.cronos.onlinereview.deliverables.TestCasesDeliverableChecker;

import com.topcoder.project.phases.PhaseType;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.util.datavalidator.LongValidator;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.datavalidator.StringValidator;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * <p>
 * This class implements {@link ManagersProvider} and provides the implementation for creating the managers.
 * </p>
 * <p>
 * Change note for 1.1: adds a new manager : <code>ProjectLinkManager</code>. It is for "OR Linking Assembly".
 * </p>
 *
 * <p>
 * Version 1.2 (Online Review End Of Project Analysis Release Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #getPhaseManager()} method to set handler for Post-Mortem phase.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.3 (Specification Review Part 1 Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #getPhaseManager()} method to set handler for <code>Specification Review</code> and
 *     <code>Specification Submission</code>  phases.</li>
 *     <li>Updated {@link #getUploadManager()} method to use latest constructor for {@link UploadManager}
 *     implementation.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.3.1 (Milestone Support Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added support for <code>Milestone</code> phases.</li>
 *   </ol>
 * </p>
 * <p>
 * Version 1.4 (Online Review Payments and Status Automation Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #getPhaseManager()} method to use the new PRApprovalPhaseHandler for <code>Approval</code> phase.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.5 Change notes:
 *   <ol>
 *     <li>Added LateDeliverableManager and ScorecardManager.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.6 (Online Review Status Validation Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Created {@link #getPhaseManagerWithoutHandlers()} to allow creating/caching PhaseManager without phase handlers.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.7 (BUGR-4778) Change notes:
 *   <ol>
 *     <li>Use {@link PRMilestoneReviewPhaseHandler} instead of {@link MilestoneReviewPhaseHandler}.</li>
 *   </ol> 
 * </p>
 *
 * <p>
 * Version 1.8 (Online Review Build From Sources) Change notes:
 *   <ol>
 *     <li>Removed dependency on Auto Screening.</li>
 *   </ol>
 * </p>
 *
 * @author evilisneo, BeBetter, isv, FireIce, VolodymyrK, rac_, flexme, lmmortal
 * @version 1.8
 */
public class ManagerCreationHelper implements ManagersProvider {

    /**
     * This member variable is a string constant that defines the name of the configuration namespace which the
     * parameters for database connection factory are stored under.
     */
    private static final String DB_CONNECTION_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * <p>A <code>long</code> providing the ID for <code>Contest Submission</code> submission type.</p>
     *
     * @since 1.5
     */
    private static final long SUBMISSION_TYPE_CONTEST = 1;

    /**
     * <p>A <code>long</code> providing the ID for <code>Milestone Submission</code> submission type.</p>
     *
     * @since 1.5
     */
    private static final long SUBMISSION_TYPE_MILESTONE = 3;

    /**
     * Used for caching the created manager. This instance has no registered phase handlers.
     */
    private PhaseManager phaseManagerWithoutHandlers = null;
    /**
     * Used for caching the created manager.
     */
    private PhaseManager phaseManager = null;
    /**
     * Used for caching the created manager.
     */
    private UploadManager uploadManager = null;
    /**
     * Used for caching the created manager.
     */
    private ProjectManager projectManager = null;
    /**
     * Used for caching the created manager.
     */
    private ResourceManager resourceManager = null;

    /**
     * Used for caching the created manager.
     *
     * @since 1.1
     */
    private ProjectLinkManager projectLinkManager = null;

    /**
     * Used for caching the created manager.
     *
     * @since 1.5
     */
    private LateDeliverableManager lateDeliverableManager = null;

    /**
     * Used for caching the created manager.
     *
     * @since 1.5
     */
    private DeliverableManager deliverableManager = null;

    /**
     * Used for caching the created manager.
     *
     * @since 1.5
     */
    private ScorecardManager scorecardManager = null;

    /**
     * Used for caching the created manager.
     *
     * @since 1.5
     */
    private ReviewManager reviewManager = null;


    /**
     * <p>
     * Returns a <code>PhaseManager</code> instance without registered handlers.
     * </p>
     *
     * @return a <code>PhaseManager</code> instance without registered handlers
     */
    public PhaseManager getPhaseManagerWithoutHandlers() {
        if(phaseManagerWithoutHandlers != null) {
            return phaseManagerWithoutHandlers;
        }
        try {
            phaseManagerWithoutHandlers = new DefaultPhaseManager("com.topcoder.management.phase");
            return phaseManagerWithoutHandlers;
        } catch (Exception e) {
            throw new ManagerCreationException("Exception occurred while creating the PhaseManager.", e);
        }
    }

    /**
     * <p>
     * Returns a <code>PhaseManager</code> instance with phase handlers.
     * This is used in <code>UploadServices</code> to retrieve this manager and perform all its operations.
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
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRFinalFixPhaseHandler(),
                    Constants.FINAL_FIX_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRFinalReviewPhaseHandler(),
                    Constants.FINAL_REVIEW_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRApprovalPhaseHandler(),
                    Constants.APPROVAL_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRPostMortemPhaseHandler(),
                    Constants.POST_MORTEM_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new SpecificationSubmissionPhaseHandler(),
                    Constants.SPECIFICATION_SUBMISSION_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new SpecificationReviewPhaseHandler(),
                    Constants.SPECIFICATION_REVIEW_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new MilestoneSubmissionPhaseHandler(),
                    Constants.MILESTONE_SUBMISSION_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRMilestoneScreeningPhaseHandler(),
                    Constants.MILESTONE_SCREENING_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, phaseTypes, new PRMilestoneReviewPhaseHandler(),
                    Constants.MILESTONE_REVIEW_PHASE_NAME);
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
     * Returns a <code>ProjectLinkManager</code> instance.
     * </p>
     *
     * @return a <code>ProjectLinkManager</code> instance
     * @since 1.1
     */
    public ProjectLinkManager getProjectLinkManager() {
        try {
            if (projectLinkManager == null) {
                projectLinkManager = new ProjectLinkManagerImpl(getProjectManager());
            }
            return projectLinkManager;
        } catch (Exception e) {
            throw new ManagerCreationException("Exception occurred while creating the ProjectLinkManager.", e);
        }
    }

    /**
     * <p>
     * Returns a <code>ResourceManager</code> instance. This is used in <code>UploadServices</code> to retrieve this
     * manager and perform all its operations.
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
            IDGenerator submissionTypeIdGenerator = IDGeneratorFactory
                    .getIDGenerator(PersistenceUploadManager.SUBMISSION_TYPE_ID_GENERATOR_NAME);
            // Get the search bundles
            SearchBundleManager searchBundleManager = new SearchBundleManager("com.topcoder.searchbuilder.common");
            SearchBundle uploadSearchBundle = searchBundleManager
                    .getSearchBundle(PersistenceUploadManager.UPLOAD_SEARCH_BUNDLE_NAME);
            SearchBundle submissionSearchBundle = searchBundleManager
                    .getSearchBundle(PersistenceUploadManager.SUBMISSION_SEARCH_BUNDLE_NAME);
            // Initialize the PersistenceUploadManager
            uploadManager = new PersistenceUploadManager(persistence, uploadSearchBundle, submissionSearchBundle,
                    uploadIdGenerator, uploadTypeIdGenerator, uploadStatusIdGenerator, submissionIdGenerator,
                    submissionStatusIdGenerator, submissionTypeIdGenerator);
            return uploadManager;
        } catch (Exception e) {
            throw new ManagerCreationException("Exception occurred while creating the upload manager.", e);
        }
    }

    /**
     * <p>
     * Returns a <code>LateDeliverableManager</code> instance.
     * </p>
     *
     * @return a <code>LateDeliverableManager</code> instance
     */
    public LateDeliverableManager getLateDeliverableManager() {
        if(lateDeliverableManager == null) {
            lateDeliverableManager = new LateDeliverableManagerImpl("com/topcoder/util/config/ConfigManager.properties",
                LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE);
        }
        return lateDeliverableManager;
    }

    /**
     * <p>
     * Returns a <code>DeliverableManager</code> instance.
     * </p>
     *
     * @return a <code>DeliverableManager</code> instance
     */
    public DeliverableManager getDeliverableManager() {
        if(deliverableManager == null) {
            try {
                // Get connection factory
                DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
                // Get the persistence
                DeliverablePersistence deliverablePersistence = new SqlDeliverablePersistence(dbconn);

                // Get the search bundles
                SearchBundleManager searchBundleManager =
                        new SearchBundleManager("com.topcoder.searchbuilder.common");

                SearchBundle deliverableSearchBundle = searchBundleManager.getSearchBundle(
                        PersistenceDeliverableManager.DELIVERABLE_SEARCH_BUNDLE_NAME);
                SearchBundle deliverableWithSubmissionsSearchBundle = searchBundleManager.getSearchBundle(
                        PersistenceDeliverableManager.DELIVERABLE_WITH_SUBMISSIONS_SEARCH_BUNDLE_NAME);

                // The checkers are used when deliverable instances are retrieved
                Map<String, DeliverableChecker> checkers = new HashMap<String, DeliverableChecker>();

                // Some checkers are used more than once
                DeliverableChecker committedChecker = new CommittedReviewDeliverableChecker(dbconn);
                DeliverableChecker submissionIndependentReviewChecker
                    = new CommittedReviewDeliverableChecker(dbconn, false);
                DeliverableChecker testCasesChecker = new TestCasesDeliverableChecker(dbconn);

                checkers.put(Constants.SUBMISSION_DELIVERABLE_NAME, 
                             new SubmissionDeliverableChecker(dbconn, SUBMISSION_TYPE_CONTEST));
                checkers.put(Constants.MILESTONE_SUBMISSION_DELIVERABLE_NAME, 
                             new SubmissionDeliverableChecker(dbconn, SUBMISSION_TYPE_MILESTONE));
                checkers.put(Constants.MILESTONE_SCREENING_DELIVERABLE_NAME, committedChecker);
                checkers.put(Constants.MILESTONE_REVIEW_DELIVERABLE_NAME, committedChecker);
                checkers.put(Constants.SPECIFICATION_SUBMISSION_DELIVERABLE_NAME,
                             new SpecificationSubmissionDeliverableChecker(dbconn));
                checkers.put(Constants.SPECIFICATION_REVIEW_DELIVERABLE_NAME, committedChecker);
                checkers.put(Constants.SCREENING_DELIVERABLE_NAME, new IndividualReviewDeliverableChecker(dbconn));
                checkers.put(Constants.PRIMARY_SCREENING_DELIVERABLE_NAME, committedChecker);
                checkers.put(Constants.REVIEW_DELIVERABLE_NAME, committedChecker);
                checkers.put(Constants.ACC_TEST_CASES_DELIVERABLE_NAME, testCasesChecker);
                checkers.put(Constants.FAIL_TEST_CASES_DELIVERABLE_NAME, testCasesChecker);
                checkers.put(Constants.STRS_TEST_CASES_DELIVERABLE_NAME, testCasesChecker);
                checkers.put(Constants.APPEAL_RESP_DELIVERABLE_NAME, new AppealResponsesDeliverableChecker(dbconn));
                checkers.put(Constants.AGGREGATION_DELIVERABLE_NAME, new AggregationDeliverableChecker(dbconn));
                checkers.put(Constants.FINAL_FIX_DELIVERABLE_NAME, new FinalFixesDeliverableChecker(dbconn));
                checkers.put(Constants.SCORECARD_COMM_DELIVERABLE_NAME, new SubmitterCommentDeliverableChecker(dbconn));
                checkers.put(Constants.FINAL_REVIEW_PHASE_NAME, new FinalReviewDeliverableChecker(dbconn));
                checkers.put(Constants.APPROVAL_DELIVERABLE_NAME, new ApprovalDeliverableChecker(dbconn));
                checkers.put(Constants.POST_MORTEM_DELIVERABLE_NAME, submissionIndependentReviewChecker);

                // Initialize the PersistenceDeliverableManager
                deliverableManager = new PersistenceDeliverableManager(deliverablePersistence, checkers,
                    deliverableSearchBundle, deliverableWithSubmissionsSearchBundle);
            } catch (Exception e) {
                throw new ManagerCreationException("Exception occurred while creating the deliverable manager.", e);
            }
        }

        return deliverableManager;
    }

    /**
     * <p>
     * Returns a <code>ScorecardManager</code> instance.
     * </p>
     *
     * @return a <code>ScorecardManager</code> instance
     */
    public ScorecardManager getScorecardManager() {
        if(scorecardManager == null) {
            try {
                scorecardManager = new ScorecardManagerImpl();
            } catch (com.topcoder.management.scorecard.ConfigurationException e) {
                throw new ManagerCreationException("Exception occurred while creating the scorecard manager.", e);
            }
        }
        return scorecardManager;
    }

    /**
     * <p>
     * Returns a <code>ReviewManager</code> instance.
     * </p>
     *
     * @return a <code>ReviewManager</code> instance
     */
    public ReviewManager getReviewManager() {
        if(reviewManager == null) {
            try {
                reviewManager = new DefaultReviewManager();
            } catch (com.topcoder.management.review.ConfigurationException e) {
                throw new ManagerCreationException("Exception occurred while creating the review manager.", e);
            }
        }
        return reviewManager;
    }

    /**
     * Sets the searchable fields to the search bundle.
     *
     * @param searchBundle
     *            the search bundle to set.
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
    }
}
