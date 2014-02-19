/*
 * Copyright (C) 2007 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.deliverables.AggregationDeliverableChecker;
import com.cronos.onlinereview.deliverables.AppealResponsesDeliverableChecker;
import com.cronos.onlinereview.deliverables.CommittedReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.FinalFixesDeliverableChecker;
import com.cronos.onlinereview.deliverables.FinalReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.IndividualReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.SpecificationSubmissionDeliverableChecker;
import com.cronos.onlinereview.deliverables.SubmissionDeliverableChecker;
import com.cronos.onlinereview.deliverables.SubmitterCommentDeliverableChecker;
import com.cronos.onlinereview.deliverables.TestCasesDeliverableChecker;
import com.cronos.onlinereview.phases.AppealsPhaseHandler;
import com.cronos.onlinereview.phases.CheckpointSubmissionPhaseHandler;
import com.cronos.onlinereview.phases.PRAggregationPhaseHandler;
import com.cronos.onlinereview.phases.PRAppealResponsePhaseHandler;
import com.cronos.onlinereview.phases.PRApprovalPhaseHandler;
import com.cronos.onlinereview.phases.PRCheckpointReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRCheckpointScreeningPhaseHandler;
import com.cronos.onlinereview.phases.PRIterativeReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRFinalFixPhaseHandler;
import com.cronos.onlinereview.phases.PRFinalReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRPostMortemPhaseHandler;
import com.cronos.onlinereview.phases.PRRegistrationPhaseHandler;
import com.cronos.onlinereview.phases.PRReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRScreeningPhaseHandler;
import com.cronos.onlinereview.phases.PRSubmissionPhaseHandler;
import com.cronos.onlinereview.phases.SpecificationReviewPhaseHandler;
import com.cronos.onlinereview.phases.SpecificationSubmissionPhaseHandler;
import com.cronos.onlinereview.services.uploads.ManagersProvider;
import com.cronos.termsofuse.dao.ProjectTermsOfUseDao;
import com.cronos.termsofuse.dao.TermsOfUseDao;
import com.cronos.termsofuse.dao.UserTermsOfUseDao;
import com.cronos.termsofuse.dao.impl.ProjectTermsOfUseDaoImpl;
import com.cronos.termsofuse.dao.impl.TermsOfUseDaoImpl;
import com.cronos.termsofuse.dao.impl.UserTermsOfUseDaoImpl;
import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationParserException;
import com.topcoder.configuration.persistence.NamespaceConflictException;
import com.topcoder.configuration.persistence.UnrecognizedFileTypeException;
import com.topcoder.configuration.persistence.UnrecognizedNamespaceException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.DeliverableChecker;
import com.topcoder.management.deliverable.DeliverableManager;
import com.topcoder.management.deliverable.PersistenceDeliverableManager;
import com.topcoder.management.deliverable.PersistenceUploadManager;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl;
import com.topcoder.management.deliverable.persistence.DeliverablePersistence;
import com.topcoder.management.deliverable.persistence.UploadPersistence;
import com.topcoder.management.deliverable.persistence.sql.SqlDeliverablePersistence;
import com.topcoder.management.deliverable.persistence.sql.SqlUploadPersistence;
import com.topcoder.management.payment.ProjectPaymentAdjustmentManager;
import com.topcoder.management.payment.ProjectPaymentManager;
import com.topcoder.management.payment.impl.ProjectPaymentAdjustmentManagerImpl;
import com.topcoder.management.payment.impl.ProjectPaymentManagerImpl;
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
import com.topcoder.management.review.DefaultReviewManager;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManagementConfigurationException;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManager;
import com.topcoder.management.reviewfeedback.impl.JDBCReviewFeedbackManager;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
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
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ManagerCreationHelper implements ManagersProvider {

    /**
     * This member variable is a string constant that defines the name of the configuration namespace which the
     * parameters for database connection factory are stored under.
     */
    private static final String DB_CONNECTION_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * <p>A <code>long</code> providing the ID for <code>Contest Submission</code> submission type.</p>
     */
    private static final long SUBMISSION_TYPE_CONTEST = 1;

    /**
     * <p>A <code>long</code> providing the ID for <code>Checkpoint Submission</code> submission type.</p>
     */
    private static final long SUBMISSION_TYPE_CHECKPOINT = 3;

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
     */
    private ProjectLinkManager projectLinkManager = null;

    /**
     * Used for caching the created manager.
     */
    private LateDeliverableManager lateDeliverableManager = null;

    /**
     * Used for caching the created manager.
     */
    private DeliverableManager deliverableManager = null;

    /**
     * Used for caching the created manager.
     */
    private ScorecardManager scorecardManager = null;

    /**
     * Used for caching the created manager.
     */
    private ReviewManager reviewManager = null;

    /**
     * <p>A <code>UserTermsOfUseDao</code> providing the access to user terms of use persistence.</p>
     */
    private UserTermsOfUseDao userTermsOfUseDao;

    /**
     * <p>A <code>ProjectTermsOfUseDao</code> providing the access to project terms of use persistence.</p>
     */
    private ProjectTermsOfUseDao projectTermsOfUseDao;

    /**
     * <p>A <code>TermsOfUseDao</code> providing the access to terms of use persistence.</p>
     */
    private TermsOfUseDao termsOfUseDao;

    /**
     * <p>A <code>ReviewFeedbackManager</code> providing the interface to review feedback management system.</p>
     */
    private ReviewFeedbackManager reviewFeedbackManager;

    /**
     * <p>A <code>ProjectPaymentAdjustmentManager</code> providing the access to project payment
     * adjustment manager.</p>
     */
    private ProjectPaymentAdjustmentManager projectPaymentAdjustmentManager;

    /**
     * <p>A <code>ProjectPaymentManager</code> providing the access to project payment manager.</p>
     */
    private ProjectPaymentManager projectPaymentManager;

    /**
     * <p>
     * Returns a <code>PhaseManager</code> instance without registered handlers.
     * </p>
     *
     * @return a <code>PhaseManager</code> instance without registered handlers
     */
    public PhaseManager getPhaseManagerWithoutHandlers() {
        if (phaseManagerWithoutHandlers != null) {
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
     * Returns a <code>ProjectPaymentAdjustmentManager</code> instance.
     * </p>
     *
     * @return a <code>ProjectPaymentAdjustmentManager</code> instance.
     */
    public ProjectPaymentAdjustmentManager getProjectPaymentAdjustmentManager() {
        if (projectPaymentAdjustmentManager != null) {
            return projectPaymentAdjustmentManager;
        }
        try {
            projectPaymentAdjustmentManager = new ProjectPaymentAdjustmentManagerImpl(
                    Constants.CONFIG_MANAGER_FILE, ProjectPaymentAdjustmentManagerImpl.DEFAULT_CONFIG_NAMESPACE);
            return projectPaymentAdjustmentManager;
        } catch (Exception e) {
            throw new ManagerCreationException("Exception occurred while creating the ProjectPaymentAdjustmentManager.", e);
        }
    }

    /**
     * <p>
     * Returns a <code>ProjectPaymentManager</code> instance.
     * </p>
     *
     * @return a <code>ProjectPaymentManager</code> instance.
     */
    public ProjectPaymentManager getProjectPaymentManager() {
        if (projectPaymentManager != null) {
            return projectPaymentManager;
        }
        try {
            projectPaymentManager = new ProjectPaymentManagerImpl(Constants.CONFIG_MANAGER_FILE,
                    ProjectPaymentManagerImpl.DEFAULT_CONFIG_NAMESPACE);
            return projectPaymentManager;
        } catch (Exception e) {
            throw new ManagerCreationException("Exception occurred while creating the ProjectPaymentManager.", e);
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
        if (phaseManager != null) {
            return phaseManager;
        }
        try {
            phaseManager = new DefaultPhaseManager("com.topcoder.management.phase");
            // Register all the handles.
            registerPhaseHandlerForOperation(phaseManager, new PRRegistrationPhaseHandler(),
                    Constants.REGISTRATION_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRSubmissionPhaseHandler(),
                    Constants.SUBMISSION_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRScreeningPhaseHandler(),
                    Constants.SCREENING_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRReviewPhaseHandler(),
                    Constants.REVIEW_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new AppealsPhaseHandler(),
                    Constants.APPEALS_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRAppealResponsePhaseHandler(),
                    Constants.APPEALS_RESPONSE_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRAggregationPhaseHandler(),
                    Constants.AGGREGATION_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRFinalFixPhaseHandler(),
                    Constants.FINAL_FIX_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRFinalReviewPhaseHandler(),
                    Constants.FINAL_REVIEW_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRApprovalPhaseHandler(),
                    Constants.APPROVAL_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRPostMortemPhaseHandler(),
                    Constants.POST_MORTEM_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new SpecificationSubmissionPhaseHandler(),
                    Constants.SPECIFICATION_SUBMISSION_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new SpecificationReviewPhaseHandler(),
                    Constants.SPECIFICATION_REVIEW_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new CheckpointSubmissionPhaseHandler(),
                    Constants.CHECKPOINT_SUBMISSION_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRCheckpointScreeningPhaseHandler(),
                    Constants.CHECKPOINT_SCREENING_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRCheckpointReviewPhaseHandler(),
                    Constants.CHECKPOINT_REVIEW_PHASE_NAME);
            registerPhaseHandlerForOperation(phaseManager, new PRIterativeReviewPhaseHandler(),
                    Constants.ITERATIVE_REVIEW_PHASE_NAME);
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
            if (projectManager == null) {
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
        if (resourceManager != null) {
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
        if (uploadManager != null) {
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
        if (lateDeliverableManager == null) {
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
        if (deliverableManager == null) {
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
                checkers.put(Constants.CHECKPOINT_SUBMISSION_DELIVERABLE_NAME,
                             new SubmissionDeliverableChecker(dbconn, SUBMISSION_TYPE_CHECKPOINT));
                checkers.put(Constants.CHECKPOINT_SCREENING_DELIVERABLE_NAME, committedChecker);
                checkers.put(Constants.CHECKPOINT_REVIEW_DELIVERABLE_NAME, committedChecker);
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
                checkers.put(Constants.APPROVAL_DELIVERABLE_NAME, committedChecker);
                checkers.put(Constants.POST_MORTEM_DELIVERABLE_NAME, submissionIndependentReviewChecker);
                checkers.put(Constants.ITERATIVE_REVIEW_DELIVERABLE_NAME, committedChecker);

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
        if (scorecardManager == null) {
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
        if (reviewManager == null) {
            try {
                reviewManager = new DefaultReviewManager();
            } catch (com.topcoder.management.review.ConfigurationException e) {
                throw new ManagerCreationException("Exception occurred while creating the review manager.", e);
            }
        }
        return reviewManager;
    }

    /**
     * <p>Gets the access to user terms of use persistence.</p>
     *
     * @return a <code>UserTermsOfUseDao</code> providing the access to user terms of use persistence.
     */
    public UserTermsOfUseDao getUserTermsOfUseDao() {
        if (this.userTermsOfUseDao == null) {
            try {
                String namespace = Constants.USER_TERMS_DAO_NAMESPACE;
                ConfigurationObject configurationObject =
                    new ConfigurationFileManager(Constants.CONFIG_MANAGER_FILE).getConfiguration(namespace).getChild(namespace);
                this.userTermsOfUseDao = new UserTermsOfUseDaoImpl(configurationObject);
            } catch (ConfigurationAccessException e) {
                throw new ManagerCreationException("Exception occurred while creating the user terms of use dao.", e);
            } catch (UnrecognizedNamespaceException e) {
                throw new ManagerCreationException("Exception occurred while creating the user terms of use dao.", e);
            } catch (IOException e) {
                throw new ManagerCreationException("Exception occurred while creating the user terms of use dao.", e);
            } catch (ConfigurationParserException e) {
                throw new ManagerCreationException("Exception occurred while creating the user terms of use dao.", e);
            } catch (NamespaceConflictException e) {
                throw new ManagerCreationException("Exception occurred while creating the user terms of use dao.", e);
            } catch (UnrecognizedFileTypeException e) {
                throw new ManagerCreationException("Exception occurred while creating the user terms of use dao.", e);
            }
        }
        return this.userTermsOfUseDao;
    }

    /**
     * <p>Gets the access to project terms of use persistence.</p>
     *
     * @return a <code>ProjectTermsOfUseDao</code> providing the access to project terms of use persistence.
     */
    public ProjectTermsOfUseDao getProjectTermsOfUseDao() {
        if (this.projectTermsOfUseDao == null) {
            try {
                String namespace = Constants.PROJECT_TERMS_DAO_NAMESPACE;
                ConfigurationObject configurationObject =
                    new ConfigurationFileManager(Constants.CONFIG_MANAGER_FILE).getConfiguration(namespace).getChild(namespace);
                this.projectTermsOfUseDao = new ProjectTermsOfUseDaoImpl(configurationObject);
            } catch (ConfigurationAccessException e) {
                throw new ManagerCreationException("Exception occurred while creating the project terms of use dao.", e);
            } catch (UnrecognizedNamespaceException e) {
                throw new ManagerCreationException("Exception occurred while creating the project terms of use dao.", e);
            } catch (IOException e) {
                throw new ManagerCreationException("Exception occurred while creating the project terms of use dao.", e);
            } catch (ConfigurationParserException e) {
                throw new ManagerCreationException("Exception occurred while creating the project terms of use dao.", e);
            } catch (NamespaceConflictException e) {
                throw new ManagerCreationException("Exception occurred while creating the project terms of use dao.", e);
            } catch (UnrecognizedFileTypeException e) {
                throw new ManagerCreationException("Exception occurred while creating the project terms of use dao.", e);
            }
        }
        return this.projectTermsOfUseDao;
    }

    /**
     * <p>Gets the access to terms of use persistence.</p>
     *
     * @return a <code>TermsOfUseDao</code> providing the access to project terms of use persistence.
     */
    public TermsOfUseDao getTermsOfUseDao() {
        if (this.termsOfUseDao == null) {
            try {
                String namespace = Constants.TERMS_DAO_NAMESPACE;
                ConfigurationObject configurationObject =
                    new ConfigurationFileManager(Constants.CONFIG_MANAGER_FILE).getConfiguration(namespace)
                        .getChild(namespace);
                this.termsOfUseDao = new TermsOfUseDaoImpl(configurationObject);
            } catch (ConfigurationAccessException e) {
                throw new ManagerCreationException("Exception occurred while creating the terms of use dao.", e);
            } catch (UnrecognizedNamespaceException e) {
                throw new ManagerCreationException("Exception occurred while creating the terms of use dao.", e);
            } catch (IOException e) {
                throw new ManagerCreationException("Exception occurred while creating the terms of use dao.", e);
            } catch (ConfigurationParserException e) {
                throw new ManagerCreationException("Exception occurred while creating the terms of use dao.", e);
            } catch (NamespaceConflictException e) {
                throw new ManagerCreationException("Exception occurred while creating the terms of use dao.", e);
            } catch (UnrecognizedFileTypeException e) {
                throw new ManagerCreationException("Exception occurred while creating the terms of use dao.", e);
            }
        }
        return this.termsOfUseDao;
    }

    /**
     * <p>Gets the interface to review feedback management system.</p>
     *
     * @return a <code>ReviewFeedbackManager</code> providing the interface to review feedback management system.
     * @throws ManagerCreationException if any error occurs or obtained configuration object contains invalid
     *         configuration.
     */
    public ReviewFeedbackManager getReviewFeedbackManager() {
        if (this.reviewFeedbackManager == null) {
            try {
                this.reviewFeedbackManager
                    = new JDBCReviewFeedbackManager(Constants.CONFIG_MANAGER_FILE,
                        JDBCReviewFeedbackManager.DEFAULT_CONFIGURATION_NAMESPACE);
            } catch (ReviewFeedbackManagementConfigurationException e) {
                throw new ManagerCreationException("Exception occurred while creating the review feedback manager.", e);
            }
        }
        return this.reviewFeedbackManager;
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
     * @param handler
     *            the handler to be registered
     * @param phaseName
     *            the current phase name.
     * @throws LookupException if phase type entity can not be found
     */
    private static void registerPhaseHandlerForOperation(PhaseManager manager,
            PhaseHandler handler, String phaseName) throws LookupException {
        PhaseType phaseType = LookupHelper.getPhaseType(phaseName);
        manager.registerHandler(handler, phaseType, PhaseOperationEnum.START);
        manager.registerHandler(handler, phaseType, PhaseOperationEnum.END);
    }
}
