/*
 * Copyright (C) 2007 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import com.cronos.onlinereview.Constants;
import com.topcoder.onlinereview.component.deliverable.DeliverableManager;
import com.topcoder.onlinereview.component.deliverable.UploadManager;
import com.topcoder.onlinereview.component.deliverable.late.LateDeliverableManager;
import com.topcoder.onlinereview.component.external.UserRetrieval;
import com.topcoder.onlinereview.component.project.management.ProjectLinkManager;
import com.topcoder.onlinereview.component.project.management.ProjectManager;
import com.topcoder.onlinereview.component.project.payment.ProjectPaymentAdjustmentManager;
import com.topcoder.onlinereview.component.project.payment.ProjectPaymentManager;
import com.topcoder.onlinereview.component.project.phase.PhaseHandler;
import com.topcoder.onlinereview.component.project.phase.PhaseManager;
import com.topcoder.onlinereview.component.project.phase.PhaseOperationEnum;
import com.topcoder.onlinereview.component.project.phase.PhaseType;
import com.topcoder.onlinereview.component.project.phase.handler.AppealsPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.CheckpointSubmissionPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.SpecificationReviewPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.SpecificationSubmissionPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRAggregationPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRAppealResponsePhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRApprovalPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRCheckpointReviewPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRCheckpointScreeningPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRFinalFixPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRFinalReviewPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRIterativeReviewPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRPostMortemPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRRegistrationPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRReviewPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRScreeningPhaseHandler;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRSubmissionPhaseHandler;
import com.topcoder.onlinereview.component.resource.ResourceManager;
import com.topcoder.onlinereview.component.review.ReviewManager;
import com.topcoder.onlinereview.component.reviewfeedback.ReviewFeedbackManager;
import com.topcoder.onlinereview.component.reviewupload.ManagersProvider;
import com.topcoder.onlinereview.component.scorecard.ScorecardManager;
import com.topcoder.onlinereview.component.termsofuse.ProjectTermsOfUseDao;
import com.topcoder.onlinereview.component.termsofuse.TermsOfUseDao;
import com.topcoder.onlinereview.component.termsofuse.UserTermsOfUseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import jakarta.annotation.PostConstruct;

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
    @Autowired
    @Qualifier("phaseManagerWithoutHandlers")
    private PhaseManager phaseManagerWithoutHandlers;
    /**
     * Used for caching the created manager.
     */
    @Autowired
    @Qualifier("phaseManager")
    private PhaseManager phaseManager;
    /**
     * Used for caching the created manager.
     */
    @Autowired
    private UploadManager uploadManager;
    /**
     * Used for caching the created manager.
     */
    @Autowired
    private ProjectManager projectManager;
    /**
     * Used for caching the created manager.
     */
    @Autowired
    private ResourceManager resourceManager;

    /**
     * Used for caching the created manager.
     */
    @Autowired
    private ProjectLinkManager projectLinkManager;

    /**
     * Used for caching the created manager.
     */
    @Autowired
    private LateDeliverableManager lateDeliverableManager;

    /**
     * Used for caching the created manager.
     */
    @Autowired
    private DeliverableManager deliverableManager;

    /**
     * Used for caching the created manager.
     */
    @Autowired
    private ScorecardManager scorecardManager;

    /**
     * Used for caching the created manager.
     */
    @Autowired
    private ReviewManager reviewManager;

    /**
     * <p>A <code>UserTermsOfUseDao</code> providing the access to user terms of use persistence.</p>
     */
    @Autowired
    private UserTermsOfUseDao userTermsOfUseDao;

    /**
     * <p>A <code>ProjectTermsOfUseDao</code> providing the access to project terms of use persistence.</p>
     */
    @Autowired
    private ProjectTermsOfUseDao projectTermsOfUseDao;

    /**
     * <p>A <code>TermsOfUseDao</code> providing the access to terms of use persistence.</p>
     */
    @Autowired
    private TermsOfUseDao termsOfUseDao;

    /**
     * <p>A <code>ReviewFeedbackManager</code> providing the interface to review feedback management system.</p>
     */
    @Autowired
    private ReviewFeedbackManager reviewFeedbackManager;

    /**
     * <p>A <code>ProjectPaymentAdjustmentManager</code> providing the access to project payment
     * adjustment manager.</p>
     */
    @Autowired
    private ProjectPaymentAdjustmentManager projectPaymentAdjustmentManager;

    /**
     * <p>A <code>ProjectPaymentManager</code> providing the access to project payment manager.</p>
     */
    @Autowired
    private ProjectPaymentManager projectPaymentManager;

    @Autowired
    private UserRetrieval userRetrieval;

    @Autowired
    private PRRegistrationPhaseHandler pRRegistrationPhaseHandler;
    @Autowired
    private PRSubmissionPhaseHandler pRSubmissionPhaseHandler;
    @Autowired
    private PRScreeningPhaseHandler prScreeningPhaseHandler;
    @Autowired
    private PRReviewPhaseHandler prReviewPhaseHandler;
    @Autowired
    private AppealsPhaseHandler appealsPhaseHandler;
    @Autowired
    private PRAppealResponsePhaseHandler prAppealResponsePhaseHandler;
    @Autowired
    private PRAggregationPhaseHandler prAggregationPhaseHandler;
    @Autowired
    private PRFinalFixPhaseHandler prFinalFixPhaseHandler;
    @Autowired
    private PRFinalReviewPhaseHandler prFinalReviewPhaseHandler;
    @Autowired
    private PRApprovalPhaseHandler prApprovalPhaseHandler;
    @Autowired
    private PRPostMortemPhaseHandler prPostMortemPhaseHandler;
    @Autowired
    private SpecificationSubmissionPhaseHandler specificationSubmissionPhaseHandler;
    @Autowired
    private SpecificationReviewPhaseHandler specificationReviewPhaseHandler;
    @Autowired
    private CheckpointSubmissionPhaseHandler checkpointSubmissionPhaseHandler;
    @Autowired
    private PRCheckpointScreeningPhaseHandler prCheckpointScreeningPhaseHandler;
    @Autowired
    private PRCheckpointReviewPhaseHandler prCheckpointReviewPhaseHandler;
    @Autowired
    private PRIterativeReviewPhaseHandler prIterativeReviewPhaseHandler;

    @PostConstruct
    public void postRun() {
        // Register all the handles.
        PhaseType[] entities = phaseManager.getAllPhaseTypes();
        registerPhaseHandlerForOperation(phaseManager, pRRegistrationPhaseHandler,
                entities, Constants.REGISTRATION_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, pRSubmissionPhaseHandler,
                entities, Constants.SUBMISSION_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prScreeningPhaseHandler,
                entities, Constants.SCREENING_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prReviewPhaseHandler,
                entities, Constants.REVIEW_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, appealsPhaseHandler,
                entities, Constants.APPEALS_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prAppealResponsePhaseHandler,
                entities, Constants.APPEALS_RESPONSE_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prAggregationPhaseHandler,
                entities, Constants.AGGREGATION_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prFinalFixPhaseHandler,
                entities, Constants.FINAL_FIX_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prFinalReviewPhaseHandler,
                entities, Constants.FINAL_REVIEW_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prApprovalPhaseHandler,
                entities, Constants.APPROVAL_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prPostMortemPhaseHandler,
                entities, Constants.POST_MORTEM_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, specificationSubmissionPhaseHandler,
                entities, Constants.SPECIFICATION_SUBMISSION_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, specificationReviewPhaseHandler,
                entities, Constants.SPECIFICATION_REVIEW_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, checkpointSubmissionPhaseHandler,
                entities, Constants.CHECKPOINT_SUBMISSION_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prCheckpointScreeningPhaseHandler,
                entities, Constants.CHECKPOINT_SCREENING_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prCheckpointReviewPhaseHandler,
                entities, Constants.CHECKPOINT_REVIEW_PHASE_NAME);
        registerPhaseHandlerForOperation(phaseManager, prIterativeReviewPhaseHandler,
                entities, Constants.ITERATIVE_REVIEW_PHASE_NAME);
    }

    public UserRetrieval getUserRetrieval() {
        return userRetrieval;
    }

    /**
     * <p>
     * Returns a <code>PhaseManager</code> instance without registered handlers.
     * </p>
     *
     * @return a <code>PhaseManager</code> instance without registered handlers
     */
    public PhaseManager getPhaseManagerWithoutHandlers() {
       return phaseManagerWithoutHandlers;
    }

    /**
     * <p>
     * Returns a <code>ProjectPaymentAdjustmentManager</code> instance.
     * </p>
     *
     * @return a <code>ProjectPaymentAdjustmentManager</code> instance.
     */
    public ProjectPaymentAdjustmentManager getProjectPaymentAdjustmentManager() {
        return projectPaymentAdjustmentManager;
    }

    /**
     * <p>
     * Returns a <code>ProjectPaymentManager</code> instance.
     * </p>
     *
     * @return a <code>ProjectPaymentManager</code> instance.
     */
    public ProjectPaymentManager getProjectPaymentManager() {
        return projectPaymentManager;
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
        return phaseManager;
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
       return projectManager;
    }

    /**
     * <p>
     * Returns a <code>ProjectLinkManager</code> instance.
     * </p>
     *
     * @return a <code>ProjectLinkManager</code> instance
     */
    public ProjectLinkManager getProjectLinkManager() {
        return projectLinkManager;
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
        return resourceManager;
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
        return uploadManager;
    }

    /**
     * <p>
     * Returns a <code>LateDeliverableManager</code> instance.
     * </p>
     *
     * @return a <code>LateDeliverableManager</code> instance
     */
    public LateDeliverableManager getLateDeliverableManager() {
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
        return reviewManager;
    }

    /**
     * <p>Gets the access to user terms of use persistence.</p>
     *
     * @return a <code>UserTermsOfUseDao</code> providing the access to user terms of use persistence.
     */
    public UserTermsOfUseDao getUserTermsOfUseDao() {
        return this.userTermsOfUseDao;
    }

    /**
     * <p>Gets the access to project terms of use persistence.</p>
     *
     * @return a <code>ProjectTermsOfUseDao</code> providing the access to project terms of use persistence.
     */
    public ProjectTermsOfUseDao getProjectTermsOfUseDao() {
        return this.projectTermsOfUseDao;
    }

    /**
     * <p>Gets the access to terms of use persistence.</p>
     *
     * @return a <code>TermsOfUseDao</code> providing the access to project terms of use persistence.
     */
    public TermsOfUseDao getTermsOfUseDao() {

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
        return this.reviewFeedbackManager;
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
                                                         PhaseHandler handler,
                                                         PhaseType[] entities,
                                                         String phaseName) throws LookupException {
        for (PhaseType entity : entities) {
            if (phaseName.equals(entity.getName())) {
                manager.registerHandler(handler, entity, PhaseOperationEnum.START);
                manager.registerHandler(handler, entity, PhaseOperationEnum.END);
                return;
            }
        }
    }
}
