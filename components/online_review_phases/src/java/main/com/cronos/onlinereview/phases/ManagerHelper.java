/*
 * Copyright (C) 2009 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.termsofuse.dao.ProjectTermsOfUseDao;
import com.cronos.termsofuse.dao.UserTermsOfUseDao;
import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationParserException;
import com.topcoder.configuration.persistence.NamespaceConflictException;
import com.topcoder.configuration.persistence.UnrecognizedFileTypeException;
import com.topcoder.configuration.persistence.UnrecognizedNamespaceException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistence;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.link.ProjectLinkManager;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistence;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.resource.search.NotificationTypeFilterBuilder;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.resource.search.ResourceRoleFilterBuilder;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregatorConfigException;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManager;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.LongValidator;
import com.topcoder.util.datavalidator.StringValidator;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * <p>
 * This is the helper class to load manager instances from a default configuration namespace. The namespace is
 * &quot;com.cronos.onlinereview.phases.ManagerHelper&quot;. For configuration properties in this namespace, see
 * the Component Specification, section &quot;3.2 Configuration Parameters&quot;. This class is used by
 * PhaseHandler implementations to retrieve manager instances.
 * </p>
 * <p>
 * Sample configuration file is given below:
 *
 * <pre>
 * &lt;Config name="com.cronos.onlinereview.phases.ManagerHelper"&gt;
 *  &lt;Property name="ProjectManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.project.ProjectManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.project.ProjectManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ProjectLinkManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.project.persistence.link.ProjectLinkManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.project.persistence.link.ProjectLinkManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="PhaseManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.phase.DefaultPhaseManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.phase.DefaultPhaseManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ReviewManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.review.DefaultReviewManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.review.DefaultReviewManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ScorecardManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.scorecard.ScorecardManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.scorecard.ScorecardManagerImpl&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ConnectionFactoryNS"&gt;
 *      &lt;Value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/Value&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ConnectionName"&gt;
 *      &lt;Value&gt;informix_connection&lt;/Value&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="SearchBundleManagerNS"&gt;
 *      &lt;Value&gt;com.topcoder.search.builder.Upload_Resource_Search&lt;/Value&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ResourceManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.resource.persistence.PersistenceResourceManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="ResourceSearchBundleName"&gt;
 *          &lt;Value&gt;ResourceSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="ResourceRoleSearchBundleName"&gt;
 *          &lt;Value&gt;ResourceRoleSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="NotificationSearchBundleName"&gt;
 *          &lt;Value&gt;NotificationSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="NotificationTypeSearchBundleName"&gt;
 *          &lt;Value&gt;NotificationTypeSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="ResourceIdGeneratorName"&gt;
 *          &lt;Value&gt;resource_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="NotificationTypeIdGeneratorName"&gt;
 *          &lt;Value&gt;notification_type_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="ResourceRoleIdGeneratorName"&gt;
 *          &lt;Value&gt;resource_role_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="PersistenceClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.resource.persistence.sql.SqlResourcePersistence&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="UploadManager"&gt;
 *      &lt;Property name="ClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.deliverable.PersistenceUploadManager&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="UploadSearchBundleName"&gt;
 *          &lt;Value&gt;UploadSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="SubmissionSearchBundleName"&gt;
 *          &lt;Value&gt;SubmissionSearchBundle&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="UploadIdGeneratorName"&gt;
 *          &lt;Value&gt;upload_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="UploadTypeIdGeneratorName"&gt;
 *          &lt;Value&gt;upload_type_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="UploadStatusIdGeneratorName"&gt;
 *          &lt;Value&gt;upload_status_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="SubmissionIdGeneratorName"&gt;
 *          &lt;Value&gt;submission_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="SubmissionStatusIdGeneratorName"&gt;
 *          &lt;Value&gt;submission_status_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *       &lt;Property name="SubmissionStatusIdGeneratorName"&gt;
 *          &lt;Value&gt;submission_type_id_seq&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="PersistenceClassName"&gt;
 *          &lt;Value&gt;com.topcoder.management.deliverable.persistence.sql.SqlUploadPersistence&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="UserProjectDataStore"&gt;
 *      &lt;Property name="UserRetrievalClassName"&gt;
 *          &lt;Value&gt;com.cronos.onlinereview.external.impl.DBUserRetrieval&lt;/Value&gt;
 *      &lt;/Property&gt;
 *      &lt;Property name="UserRetrievalNamespace"&gt;
 *          &lt;Value&gt;com.cronos.onlinereview.external&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name="ScorecardAggregator"&gt;
 *      &lt;Property name="Namespace"&gt;
 *          &lt;Value&gt;com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator&lt;/Value&gt;
 *      &lt;/Property&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name=&quot;ProjectDetailsURL&quot;&gt;
 *      &lt;Value&gt;http://www.topcoder.com/tc?projectDetails=?&lt;/Value&gt;
 *  &lt;/Property&gt;
 *  &lt;Property name=&quot;StudioProjectDetailsURL&quot;&gt;
 *      &lt;Value&gt;http://studio.topcoder.com/?projectDetails=?&lt;/Value&gt;
 *  &lt;/Property&gt;
 * &lt;/Config&gt;
 * </pre>
 *
 * </p>
 * <p>
 * Change in version 1.4:
 * <ul>
 * <li>add submission type id generator used to create UploadManager.</li>
 * <li>use java generic type instead of the raw type.</li>
 * </ul>
 * </p>
 * <p>
 * Change in version 1.6:
 * <ul>
 * <li>add link property to studio contest for email reference.</li>
 * </ul>
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Removed screeningManager together with its getter.</li>
 * <li>Removed private initScreeningManager() method.</li>
 * </ul>
 * Version 1.6.2 changes note:
 * <ul>
 * <li>Add copilot specific notifications</li>
 * </ul>
 * </p>
 *
 * <p>
 * Version 1.7.14 (Module Assembly - Enhanced Review Feedback Integration) Change notes:
 *   <ol>
 *     <li>Added constants {@link #PROP_REVIEW_FEEDBACK_MGR_CLASS_NAME}, {@link #PROP_REVIEW_FEEDBACK_MGR_NAMESPACE},
 *     and {@link #REVIEW_FEEDBACK_MGR_PARAM_TYPES}.</li>
 *     <li>Updated {@link #initReviewFeedbackManager(String, String, String)} to adopt for the new review feedback
 *     manager component.</li>
 *     <li>Updated {@link #ManagerHelper(String)} to add the corresponding parameters when calling
 *     {@link #initReviewFeedbackManager(String, String, String)}.</li>
 *   </ol>
 * </p>
 *
 * @author tuenm, bose_java, waits, saarixx, myxgyy, FireIce, microsky, lmmortal, flexme
 * @version 1.8.5
 */
final class ManagerHelper {
    /**
     * The default configuration namespace of this class. It is used in the default constructor.
     */
    private static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.ManagerHelper";

    /** Property name constant for project manager implementation class name. */
    private static final String PROP_PROJECT_MGR_CLASS_NAME = "ProjectManager.ClassName";

    /** Property name constant for project link manager implementation class name. */
    private static final String PROP_PROJECT_LINK_MGR_CLASS_NAME = "ProjectLinkManager.ClassName";

    /**
     * Property name constant for namespace to be passed to project manager implementation constructor.
     */
    private static final String PROP_PROJECT_MGR_NAMESPACE = "ProjectManager.Namespace";

    /** Property name constant for namespace to be passed to project link manager implementation constructor. */
    private static final String PROP_PROJECT_LINK_MGR_NAMESPACE = "ProjectLinkManager.Namespace";

    /** Property name constant for phase manager implementation class name. */
    private static final String PROP_PHASE_MGR_CLASS_NAME = "PhaseManager.ClassName";

    /**
     * Property name constant for namespace to be passed to phase manager implementation constructor.
     */
    private static final String PROP_PHASE_MGR_NAMESPACE = "PhaseManager.Namespace";

    /** Property name constant for review manager implementation class name. */
    private static final String PROP_REVIEW_MGR_CLASS_NAME = "ReviewManager.ClassName";

    /**
     * Property name constant for namespace to be passed to review manager implementation constructor.
     */
    private static final String PROP_REVIEW_MGR_NAMESPACE = "ReviewManager.Namespace";

    /** Property name constant for scorecard manager implementation class name. */
    private static final String PROP_SCORECARD_MGR_CLASS_NAME = "ScorecardManager.ClassName";

    /**
     * Property name constant for namespace to be passed to scorecard manager implementation constructor.
     */
    private static final String PROP_SCORECARD_MGR_NAMESPACE = "ScorecardManager.Namespace";

    /** Property name constant for connection factory namespace. */
    private static final String PROP_CONNECTION_FACTORY_NS = "ConnectionFactoryNS";

    /** Property name constant for connection name. */
    private static final String PROP_CONNECTION_NAME = "ConnectionName";

    /**
     * Property name constant for namespace to be passed to SearchBundleManager constructor.
     */
    private static final String PROP_SEARCH_BUNDLE_MGR_NS = "SearchBundleManagerNS";

    /** Property name constant for resource manager implementation class name. */
    private static final String PROP_RESOURCE_MGR_CLASS_NAME = "ResourceManager.ClassName";

    /**
     * Property name constant for resource search bundle name when creating ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_RESOURCE_BUNDLE_NAME = "ResourceManager.ResourceSearchBundleName";

    /**
     * Property name constant for resource role search bundle name when creating ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_RESOURCE_ROLE_BUNDLE_NAME =
        "ResourceManager.ResourceRoleSearchBundleName";

    /**
     * Property name constant for notification search bundle name when creating ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_NOTIFICATION_BUNDLE_NAME =
        "ResourceManager.NotificationSearchBundleName";

    /**
     * Property name constant for notification type search bundle name when creating ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_NOTIFICATION_TYPE_BUNDLE_NAME =
        "ResourceManager.NotificationTypeSearchBundleName";

    /**
     * Property name constant for resource ID generator name when creating ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_RESOURCE_IDGEN_NAME = "ResourceManager.ResourceIdGeneratorName";

    /**
     * Property name constant for notification ID generator name when creating ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_NOTIFICATION_TYPE_IDGEN_NAME =
        "ResourceManager.NotificationTypeIdGeneratorName";

    /**
     * Property name constant for resource role ID generator name when creating ResourceManager instance.
     */
    private static final String PROP_RESOURCE_MGR_RESOURCE_ROLE_IDGEN_NAME =
        "ResourceManager.ResourceRoleIdGeneratorName";

    /**
     * Property name constant for ResourcePersistence implementation class name.
     */
    private static final String PROP_RESOURCE_MGR_PERSISTENCE_CLASS_NAME = "ResourceManager.PersistenceClassName";

    /** Property name constant for upload manager implementation class name. */
    private static final String PROP_UPLOAD_MGR_CLASS_NAME = "UploadManager.ClassName";

    /**
     * Property name constant for upload search bundle name when creating UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_UPLOAD_BUNDLE_NAME = "UploadManager.UploadSearchBundleName";

    /**
     * Property name constant for submission search bundle name when creating UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_SUBMISSION_BUNDLE_NAME = "UploadManager.SubmissionSearchBundleName";

    /**
     * Property name constant for upload ID generator name when creating UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_UPLOAD_IDGEN_NAME = "UploadManager.UploadIdGeneratorName";

    /**
     * Property name constant for upload type ID generator name when creating UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_UPLOAD_TYPE_IDGEN_NAME = "UploadManager.UploadTypeIdGeneratorName";

    /**
     * Property name constant for upload status ID generator name when creating UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_UPLOAD_STATUS_IDGEN_NAME = "UploadManager.UploadStatusIdGeneratorName";

    /**
     * Property name constant for submission ID generator name when creating UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_SUBMISSION_IDGEN_NAME = "UploadManager.SubmissionIdGeneratorName";

    /**
     * This constant stores Online Review's project details page url property name.
     * @since 1.3
     */
    private static final String PROP_PROJECT_DETAILS_URL = "ProjectDetailsURL";

    /**
     * This constant stores challenge page url property name.
     */
    private static final String CHALLENGE_PAGE_URL = "ChallengePageURL";

    /**
     * This constant stores studio project details page url property name.
     * @since 1.6
     */
    private static final String PROP_STUDIO_PROJECT_DETAILS_URL = "StudioProjectDetailsURL";

    /**
     * This constant stores property name for URL of contest details page in direct.
     * @since 1.7.6
     */
    private static final String PROP_DIRECT_CONTEST_URL = "DirectContestURL";

    /**
     * This constant stores property name for URL of contest final fix page in direct.
     * 
     * @since 1.7.15
     */
    private static final String PROP_DIRECT_CONTEST_FINAL_FIX_URL = "DirectContestFinalFixURL";

    /**
     * This constant stores property name for URL of contest final fix page in direct.
     *
     * @since 1.7.15
     */
    private static final String PROP_STUDIO_CONTEST_FINAL_FIX_URL = "StudioProjectFinalFixURL";

    /**
     * This constant stores property name for URL of copilot contest details page in direct.
     * @since 1.6.2
     */
    private static final String PROP_COPILOT_DIRECT_CONTEST_URL = "CopilotDirectContestURL";

    /**
     * This constant stores property name for URL of project details page in direct.
     * @since 1.7.6
     */
    private static final String PROP_DIRECT_PROJECT_URL = "DirectProjectURL";

    /**
     * Property name constant for submission status ID generator name when creating UploadManager instance.
     */
    private static final String PROP_UPLOAD_MGR_SUBMISSION_STATUS_IDGEN_NAME =
        "UploadManager.SubmissionStatusIdGeneratorName";

    /**
     * Property name constant for submission type ID generator name when creating UploadManager instance.
     * @since 1.4
     */
    private static final String PROP_UPLOAD_MGR_SUBMISSION_TYPE_IDGEN_NAME =
        "UploadManager.SubmissionTypeIdGeneratorName";

    /** Property name constant for UploadPersistence implementation class name. */
    private static final String PROP_UPLOAD_MGR_PERSISTENCE_CLASS_NAME = "UploadManager.PersistenceClassName";

    /** Property name constant for UserRetrieval implementation class name. */
    private static final String PROP_USER_RETRIEVAL_CLASS_NAME = "UserProjectDataStore.UserRetrievalClassName";

    /**
     * Property name constant for namespace to be passed to UserRetrieval implementation constructor.
     */
    private static final String PROP_USER_RETRIEVAL_NAMESPACE = "UserProjectDataStore.UserRetrievalNamespace";

    /**
     * Property name constant for namespace to be passed to ReviewScoreAggregator constructor.
     */
    private static final String PROP_SCORE_AGGREGATOR_NAMESPACE = "ScorecardAggregator.Namespace";


    /**
     * ScorecardManager, ReviewManager, ProjectManager, and UserRetrieval all use same constructor signature which
     * is
     * the one that takes a String parameter. This constant array is used as parameter types array when
     * instantiating
     * using reflection in the initManager() method.
     */
    private static final Class<?>[] MANAGER_PARAM_TYPES = new Class[] {String.class };

    /**
     * ProjectyLinkManager all use same constructor signature which is the one that takes a String and
     * ProjectManager
     * parameter. This constant array is used as parameter types array when instantiating using reflection in the
     * initManager() method.
     * @since 1.3
     */
    private static final Class<?>[] PROJECT_LINK_MANAGER_PARAM_TYPES = new Class[] {String.class,
        ProjectManager.class };

    /**
     * Property name constant for file to Configuration Manager file.
     */
    private static final String PROP_CONFIG_MANAGER_FILE = "ConfigurationManagerFile";

    /**
     * ProjectyLinkManager all use same constructor signature which is the one that takes a String and ProjectManager
     * parameter. This constant array is used as parameter types array when instantiating using reflection in the
     * initManager() method.
     *
     * @since 1.3
     */
    private static final Class<?>[] TERMS_OF_USE_DAOS_PARAM_TYPES = new Class[] {ConfigurationObject.class};

    /**
     * Property name constant for UserTermsOfUseDao implementation class name.
     */
    private static final String PROP_USER_TERMS_DAO_CLASS_NAME = "UserTermsOfUseDao.ClassName";

    /**
     * Property name constant for namespace to be passed to UserTermsOfUseDao implementation constructor.
     */
    private static final String PROP_USER_TERMS_DAO_NAMESPACE = "UserTermsOfUseDao.Namespace";

    /**
     * Property name constant for ProjectTermsOfUseDao implementation class name.
     */
    private static final String PROP_PROJECT_TERMS_DAO_CLASS_NAME = "ProjectTermsOfUseDao.ClassName";

    /**
     * Property name constant for namespace to be passed to ProjectTermsOfUseDao implementation constructor.
     */
    private static final String PROP_PROJECT_TERMS_DAO_NAMESPACE = "ProjectTermsOfUseDao.Namespace";

    /**
     * Property name constant for ReviewFeedbackManager implementation class name.
     *
     * @since 1.7.14
     */
    private static final String PROP_REVIEW_FEEDBACK_MGR_CLASS_NAME = "ReviewFeedbackManager.ClassName";

    /**
     * Property name constant for namespace to be passed to ReviewFeedbackManager implementation constructor.
     *
     * @since 1.7.14
     */
    private static final String PROP_REVIEW_FEEDBACK_MGR_NAMESPACE = "ReviewFeedbackManager.Namespace";

    /**
     * The types of ReviewFeedbackManager's constructor parameters. The first parameter is the configuration file
     * location, the second parameter is the configuration namespace. This constant array is used as parameter types
     * array when instantiating using reflection in the initManager() method.
     *
     * @since 1.7.14
     */
    private static final Class<?>[] REVIEW_FEEDBACK_MGR_PARAM_TYPES = new Class[] {String.class, String.class};

    /**
     * Represents the ProjectManager instance. It is initialized in the constructor and never changed after that.
     * It is
     * never null.
     */
    private final ProjectManager projectManager;

    /**
     * Represents the ProjectLinkManager instance. It is initialized in the constructor and never changed after
     * that. It
     * is never null.
     * @since 1.3
     */
    private final ProjectLinkManager projectLinkManager;

    /**
     * Represents the PhaseManager instance. It is initialized in the constructor and never changed after that. It
     * is
     * never null.
     */
    private final PhaseManager phaseManager;

    /**
     * Represents the ScorecardManager instance. It is initialized in the constructor and never changed after that.
     * It
     * is never null.
     */
    private final ScorecardManager scorecardManager;

    /**
     * Represents the ReviewManager instance. It is initialized in the constructor and never changed after that. It
     * is
     * never null.
     */
    private final ReviewManager reviewManager;

    /**
     * Represents the ResourceManager instance. It is initialized in the constructor and never changed after that.
     * It is
     * never null.
     */
    private final ResourceManager resourceManager;

    /**
     * Represents the UploadManager instance. It is initialized in the constructor and never changed after that. It
     * is
     * never null.
     */
    private final UploadManager uploadManager;

    /**
     * Represents the UserRetrieval instance. It is initialized in the constructor and never changed after that. It
     * is
     * never null.
     */
    private final UserRetrieval userRetrieval;

    /**
     * Represents the ReviewScoreAggregator instance. It is initialized in the constructor and never changed after
     * that.
     * It is never null.
     */
    private final ReviewScoreAggregator scorecardAggregator;

    /**
     * This constant stores Online Review's project details page URL.
     * @since 1.3
     */
    private final String projectDetailsBaseURL;


    /**
     * This constant stores the challenge page URL for the new topcoder site.
     */
    private final String challengePageBaseURL;

    /**
     * This constant stores studio project details page URL.
     * @since 1.6
     */
    private final String studioProjectDetailsBaseURL;

    /**
     * This constant stores URL for contest details page in direct.
     * @since 1.7.6
     */
    private final String directContestBaseURL;

    /**
     * This constant stores URL for copilot posting contest details page in direct.
     * @since 1.6.2
     */
    private final String copilotDirectContestBaseURL;

    /**
     * <p>A <code>String</code> providing the base URL for the page with final fixes for contest in Direct
     * application.</p>
     * 
     * @since 1.7.15
     */
    private String directContestFinalFixBaseURL;

    /**
     * <p>A <code>String</code> providing the base URL for the page with final fixes for contest in Studio
     * application.</p>
     * @since 1.7.15
     */
    private String studioProjectFinalFixBaseURL;

    /**
     * This constant stores URL for project page in direct.
     * @since 1.7.6
     */
    private final String directProjectBaseURL;

    /**
     * <p>A <code>ReviewFeedbackManager</code> providing the interface to review feedback management system.</p>
     * @since 1.7.6
     */
    private ReviewFeedbackManager reviewFeedbackManager;


    /**
     * <p>A <code>UserTermsOfUseDao</code> providing the access to user terms of use persistence.</p>
     * 
     * @since 1.6.3
     */
    private final UserTermsOfUseDao userTermsOfUseDao;

    /**
     * <p>A <code>ProjectTermsOfUseDao</code> providing the access to project terms of use persistence.</p>
     * 
     * @since 1.6.3
     */
    private ProjectTermsOfUseDao projectTermsOfUseDao;


    /**
     * Creates a new instance of ManagerHelper using the default configuration namespace of this class. This
     * constructor
     * loads the manager instances using the configuration settings in the default namespace. Please see
     * Configuration
     * Parameters section the Component Specification for properties used. Also see class documentation for sample
     * configuration file.
     * @throws ConfigurationException
     *             if required properties are missing or error occurs when instantiating the managers.
     */
    public ManagerHelper() throws ConfigurationException {
        this(DEFAULT_NAMESPACE);
    }

    /**
     * Creates a new instance of ManagerHelper using the given namespace. This constructor loads the manager
     * instances
     * using the configuration settings from given namespace. Please see Configuration Parameters section the
     * Component
     * Specification for properties used. Also see class documentation for sample configuration file. *
     * <p>
     * Change in version 1.6:
     * <ul>
     * <li>add link property to studio contest for email reference.</li>
     * <li>add configuration of ProjectRoleTermsOfUse and UserTermsOfUse for access.</li>
     * </ul>
     * </p>
     * <p>
     * Change in version 1.7.14:
     * <ul>
     * <li>Added the corresponding parameters when calling
     * {@link #initReviewFeedbackManager(String, String, String)}</li>
     * </ul>
     * </p>
     * @param namespace
     *            the namespace to load configuration settings from.
     * @throws ConfigurationException
     *             if required properties are missing or error occurs when instantiating the managers.
     * @throws IllegalArgumentException
     *             if the namespace is null or empty string.
     */
    public ManagerHelper(String namespace) throws ConfigurationException {
        PhasesHelper.checkString(namespace, "namespace");

        this.projectManager = initManager(namespace, PROP_PROJECT_MGR_CLASS_NAME, PROP_PROJECT_MGR_NAMESPACE,
                ProjectManager.class, false);

        this.projectLinkManager = initProjectLinkManager(namespace, PROP_PROJECT_LINK_MGR_CLASS_NAME,
                PROP_PROJECT_LINK_MGR_NAMESPACE);
        this.phaseManager = initManager(namespace, PROP_PHASE_MGR_CLASS_NAME, PROP_PHASE_MGR_NAMESPACE,
                PhaseManager.class, false);
        this.reviewManager = initManager(namespace, PROP_REVIEW_MGR_CLASS_NAME, PROP_REVIEW_MGR_NAMESPACE,
                ReviewManager.class, false);
        this.scorecardManager = initManager(namespace, PROP_SCORECARD_MGR_CLASS_NAME,
            PROP_SCORECARD_MGR_NAMESPACE,
                ScorecardManager.class, false);
        this.uploadManager = initUploadManager(namespace);
        this.resourceManager = initResourceManager(namespace);
        this.userRetrieval = initManager(namespace, PROP_USER_RETRIEVAL_CLASS_NAME, PROP_USER_RETRIEVAL_NAMESPACE,
                UserRetrieval.class, true);
        this.scorecardAggregator = initScorecardAggregator(namespace);
        this.projectDetailsBaseURL = PhasesHelper.getPropertyValue(namespace, PROP_PROJECT_DETAILS_URL, true);
        this.challengePageBaseURL = PhasesHelper.getPropertyValue(namespace, CHALLENGE_PAGE_URL, true);
        this.studioProjectDetailsBaseURL = PhasesHelper.getPropertyValue(namespace,
            PROP_STUDIO_PROJECT_DETAILS_URL, true);
        this.directContestBaseURL = PhasesHelper.getPropertyValue(namespace, PROP_DIRECT_CONTEST_URL, true);
        this.directContestFinalFixBaseURL = PhasesHelper.getPropertyValue(namespace, PROP_DIRECT_CONTEST_FINAL_FIX_URL, true);
        this.studioProjectFinalFixBaseURL = PhasesHelper.getPropertyValue(namespace, PROP_STUDIO_CONTEST_FINAL_FIX_URL, true);
        this.copilotDirectContestBaseURL = PhasesHelper.getPropertyValue(namespace, PROP_COPILOT_DIRECT_CONTEST_URL, true);
        this.directProjectBaseURL = PhasesHelper.getPropertyValue(namespace, PROP_DIRECT_PROJECT_URL, true);
        
        this.reviewFeedbackManager = initReviewFeedbackManager(namespace, PROP_REVIEW_FEEDBACK_MGR_CLASS_NAME,
                PROP_REVIEW_FEEDBACK_MGR_NAMESPACE);

        this.userTermsOfUseDao
            = (UserTermsOfUseDao) initTermsOfUseComponent(namespace, PROP_USER_TERMS_DAO_CLASS_NAME,
                                                          PROP_USER_TERMS_DAO_NAMESPACE);
        this.projectTermsOfUseDao
            = (ProjectTermsOfUseDao) initTermsOfUseComponent(namespace, PROP_PROJECT_TERMS_DAO_CLASS_NAME,
                                                             PROP_PROJECT_TERMS_DAO_NAMESPACE);

    }

    /**
     * Gets the non-null ProjectManager instance.
     * @return The non-null ProjectManager instance.
     */
    public ProjectManager getProjectManager() {
        return projectManager;
    }

    /**
     * Gets the non-null ProjectLinkManager instance.
     * @return The non-null ProjectLinkManager instance.
     * @since 1.3
     */
    public ProjectLinkManager getProjectLinkManager() {
        return projectLinkManager;
    }

    /**
     * Gets the non-null PhaseManager instance.
     * @return The non-null PhaseManager instance.
     */
    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    /**
     * Gets the non-null ScorecardManager instance.
     * @return The non-null ScorecardManager instance.
     */
    public ScorecardManager getScorecardManager() {
        return scorecardManager;
    }

    /**
     * Gets the non-null ReviewManager instance.
     * @return The non-null ReviewManager instance.
     */
    public ReviewManager getReviewManager() {
        return reviewManager;
    }

    /**
     * Gets the non-null ResourceManager instance.
     * @return The non-null ResourceManager instance.
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    /**
     * Gets the non-null UploadManager instance.
     * @return The non-null UploadManager instance.
     */
    public UploadManager getUploadManager() {
        return uploadManager;
    }

    /**
     * Gets the non-null UserRetrieval instance.
     * @return The non-null UserRetrieval instance.
     */
    public UserRetrieval getUserRetrieval() {
        return userRetrieval;
    }

    /**
     * Gets the non-null ReviewScoreAggregator instance.
     * @return The non-null ReviewScoreAggregator instance.
     */
    public ReviewScoreAggregator getScorecardAggregator() {
        return scorecardAggregator;
    }

    /**
     * Gets the non-null ReviewFeedbackManager instance.
     * @return The non-null ReviewFeedbackManager instance.
     */
    public ReviewFeedbackManager getReviewFeedbackManager() {
        return reviewFeedbackManager;
    }

    /**
     * <p>
     * Gets the project details base url.
     * </p>
     * @return the project details base url.
     * @since 1.3
     */
    public String getProjectDetailsBaseURL() {
        return projectDetailsBaseURL;
    }

    /**
     * <p>
     * Gets the challenge page base url.
     * </p>
     */
    public String getChallengePageBaseURL() {
        return challengePageBaseURL;
    }

    /**
     * <p>
     * Gets the studio project details base url.
     * </p>
     * @return the studio project details base url.
     * @since 1.6
     */
    public String getStudioProjectDetailsBaseURL() {
        return studioProjectDetailsBaseURL;
    }

    /**
     * <p>
     * Gets the base URL for contest details in direct.
     * </p>
     * @return the base url for contest details in direct.
     * @since 1.7.6
     */
    public String getDirectContestBaseURL() {
        return directContestBaseURL;
    }

    /**
     * <p>
     * Gets the base URL for copilot posting contest details in direct.
     * </p>
     * @return the base URL for copilot posting contest details in direct.
     * @since 1.6.2
     */
    public String getCopilotDirectContestBaseURL() {
        return copilotDirectContestBaseURL;
    }

    /**
     * <p>
     * Gets the base URL for project details in direct.
     * </p>
     * @return the base url for project details in direct.
     * @since 1.7.6
     */
    public String getDirectProjectBaseURL() {
        return directProjectBaseURL;
    }


    /**
     * <p>Gets the access to user terms of use persistence.</p>
     *
     * @return a <code>UserTermsOfUseDao</code> providing the access to user terms of use persistence.
     * @since 1.6.3
     */
    public UserTermsOfUseDao getUserTermsOfUseDao() {
        return this.userTermsOfUseDao;
    }

    /**
     * <p>Gets the access to project terms of use persistence.</p>
     *
     * @return a <code>ProjectTermsOfUseDao</code> providing the access to project terms of use persistence.
     * @since 1.6.3
     */
    public ProjectTermsOfUseDao getProjectTermsOfUseDao() {
        return this.projectTermsOfUseDao;
    }

    /**
     * <p>Gets the base URL for the page with final fixes for contest in Direct application.</p>
     *
     * @return a <code>String</code> providing the base URL for the page with final fixes for contest in Direct
     *         application.
     * @since 1.7.15
     */
    public String getDirectContestFinalFixBaseURL() {
        return this.directContestFinalFixBaseURL;
    }

    /**
     * <p>Sets the base URL for the page with final fixes for contest in Direct application.</p>
     *
     * @param directContestFinalFixBaseURL a <code>String</code> providing the base URL for the page with final fixes
     * for contest in Direct application.
     * @since 1.7.15
     */
    public void setDirectContestFinalFixBaseURL(String directContestFinalFixBaseURL) {
        this.directContestFinalFixBaseURL = directContestFinalFixBaseURL;
    }

    /**
     * <p>Gets the base URL for the page with final fixes for contest in Studio application.</p>
     *
     * @return a <code>String</code> providing the base URL for the page with final fixes for contest in Studio
     *         application.
     * @since 1.7.15
     */
    public String getStudioProjectFinalFixBaseURL() {
        return this.studioProjectFinalFixBaseURL;
    }

    /**
     * <p>Sets the base URL for the page with final fixes for contest in Studio application.</p>
     *
     * @param studioProjectFinalFixBaseURL a <code>String</code> providing the base URL for the page with final fixes
     * for contest in Studio application.
     * @since 1.7.15
     */
    public void setStudioProjectFinalFixBaseURL(String studioProjectFinalFixBaseURL) {
        this.studioProjectFinalFixBaseURL = studioProjectFinalFixBaseURL;
    }

    /**
     * This method is called by constructor to create an instance of UploadManager. It retrieves the required
     * properties
     * from the given namespace, and creates UploadPersistence, search bundle and id generator instances which are
     * required to create the UploadManager instance.
     * <p>
     * Change in version 1.4: add submission type id generator used to create UploadManager.
     * </p>
     * @param namespace
     *            the namespace to load configuration settings from.
     * @return a UploadManager instance.
     * @throws ConfigurationException
     *             if required properties are missing or error occurs during various instantiation processes.
     */
    private UploadManager initUploadManager(String namespace) throws ConfigurationException {
        // get all the property values
        String uploadManagerClassName = PhasesHelper.getPropertyValue(namespace, PROP_UPLOAD_MGR_CLASS_NAME, true);
        String uploadSearchBundleName = PhasesHelper.getPropertyValue(namespace,
            PROP_UPLOAD_MGR_UPLOAD_BUNDLE_NAME,
                true);
        String submissionSearchBundleName = PhasesHelper.getPropertyValue(namespace,
                PROP_UPLOAD_MGR_SUBMISSION_BUNDLE_NAME, true);
        String uploadIdGeneratorName = PhasesHelper
                .getPropertyValue(namespace, PROP_UPLOAD_MGR_UPLOAD_IDGEN_NAME, true);
        String uploadTypeIdGeneratorName = PhasesHelper.getPropertyValue(namespace,
                PROP_UPLOAD_MGR_UPLOAD_TYPE_IDGEN_NAME, true);
        String uploadStatusIdGeneratorName = PhasesHelper.getPropertyValue(namespace,
                PROP_UPLOAD_MGR_UPLOAD_STATUS_IDGEN_NAME, true);
        String submissionIdGeneratorName = PhasesHelper.getPropertyValue(namespace,
                PROP_UPLOAD_MGR_SUBMISSION_IDGEN_NAME, true);
        String submissionStatusIdGeneratorName = PhasesHelper.getPropertyValue(namespace,
                PROP_UPLOAD_MGR_SUBMISSION_STATUS_IDGEN_NAME, true);
        String submissionTypeIdGeneratorName = PhasesHelper.getPropertyValue(namespace,
                PROP_UPLOAD_MGR_SUBMISSION_TYPE_IDGEN_NAME, true);
        String persistenceClassName = PhasesHelper.getPropertyValue(namespace,
            PROP_UPLOAD_MGR_PERSISTENCE_CLASS_NAME,
                true);

        // create persistence instance
        UploadPersistence persistence = createPersistence(namespace, persistenceClassName, UploadPersistence.class);

        // create search bundle instances...
        SearchBundleManager searchBundleManager = createSearchBundleManager(namespace);
        SearchBundle uploadSearchBundle = searchBundleManager.getSearchBundle(uploadSearchBundleName);
        SearchBundle submissionSearchBundle = searchBundleManager.getSearchBundle(submissionSearchBundleName);
        setUploadFieldsSearchable(uploadSearchBundle);
        setUploadFieldsSearchable(submissionSearchBundle);

        // get the id generators...
        IDGenerator uploadIdGenerator;
        IDGenerator uploadTypeIdGenerator;
        IDGenerator uploadStatusIdGenerator;
        IDGenerator submissionIdGenerator;
        IDGenerator submissionStatusIdGenerator;
        // added in version 1.4
        IDGenerator submissionTypeIdGenerator;

        try {
            uploadIdGenerator = IDGeneratorFactory.getIDGenerator(uploadIdGeneratorName);
            uploadTypeIdGenerator = IDGeneratorFactory.getIDGenerator(uploadTypeIdGeneratorName);
            uploadStatusIdGenerator = IDGeneratorFactory.getIDGenerator(uploadStatusIdGeneratorName);
            submissionIdGenerator = IDGeneratorFactory.getIDGenerator(submissionIdGeneratorName);
            submissionStatusIdGenerator = IDGeneratorFactory.getIDGenerator(submissionStatusIdGeneratorName);
            submissionTypeIdGenerator = IDGeneratorFactory.getIDGenerator(submissionTypeIdGeneratorName);
        } catch (IDGenerationException e) {
            throw new ConfigurationException("Could not instantiate IDGenerator", e);
        }

        // create UploadManager instance using reflection...
        Object[] params = new Object[] {persistence, uploadSearchBundle, submissionSearchBundle,
            uploadIdGenerator,
            uploadTypeIdGenerator, uploadStatusIdGenerator, submissionIdGenerator, submissionStatusIdGenerator,
            submissionTypeIdGenerator };
        Class<?>[] paramTypes = new Class[] {UploadPersistence.class, SearchBundle.class, SearchBundle.class,
            IDGenerator.class, IDGenerator.class, IDGenerator.class, IDGenerator.class, IDGenerator.class,
            IDGenerator.class };

        return createObject(uploadManagerClassName, UploadManager.class, params, paramTypes);
    }

    /**
     * This method is called by constructor to create an instance of ResourceManager. It retrieves the required
     * properties from the given namespace, and creates ResourcePersistence, search bundle and id generator
     * instances
     * which are required to create the ResourceManager instance.
     * @param namespace
     *            the namespace to load configuration settings from.
     * @return a ResourceManager instance.
     * @throws ConfigurationException
     *             if required properties are missing or error occurs during various instantiation processes.
     */
    private ResourceManager initResourceManager(String namespace) throws ConfigurationException {
        // get all the property values
        String resourceManagerClassName = PhasesHelper.getPropertyValue(namespace, PROP_RESOURCE_MGR_CLASS_NAME, true);
        String resourceSearchBundleName = PhasesHelper.getPropertyValue(namespace,
                PROP_RESOURCE_MGR_RESOURCE_BUNDLE_NAME, true);
        String resourceRoleSearchBundleName = PhasesHelper.getPropertyValue(namespace,
                PROP_RESOURCE_MGR_RESOURCE_ROLE_BUNDLE_NAME, true);
        String notificationSearchBundleName = PhasesHelper.getPropertyValue(namespace,
                PROP_RESOURCE_MGR_NOTIFICATION_BUNDLE_NAME, true);
        String notificationTypeSearchBundleName = PhasesHelper.getPropertyValue(namespace,
                PROP_RESOURCE_MGR_NOTIFICATION_TYPE_BUNDLE_NAME, true);
        String resourceIdGeneratorName = PhasesHelper.getPropertyValue(namespace,
                PROP_RESOURCE_MGR_RESOURCE_IDGEN_NAME, true);
        String notificationIdGeneratorName = PhasesHelper.getPropertyValue(namespace,
                PROP_RESOURCE_MGR_NOTIFICATION_TYPE_IDGEN_NAME, true);
        String resourceRoleIdGeneratorName = PhasesHelper.getPropertyValue(namespace,
                PROP_RESOURCE_MGR_RESOURCE_ROLE_IDGEN_NAME, true);
        String persistenceClassName = PhasesHelper.getPropertyValue(namespace,
                PROP_RESOURCE_MGR_PERSISTENCE_CLASS_NAME, true);

        // create persistence instance
        ResourcePersistence persistence = createPersistence(namespace, persistenceClassName,
                ResourcePersistence.class);

        // create search bundle instances...
        SearchBundleManager searchBundleManager = createSearchBundleManager(namespace);
        SearchBundle resourceSearchBundle = searchBundleManager.getSearchBundle(resourceSearchBundleName);
        SearchBundle resourceRoleSearchBundle = searchBundleManager.getSearchBundle(resourceRoleSearchBundleName);
        SearchBundle notificationSearchBundle = searchBundleManager.getSearchBundle(notificationSearchBundleName);
        SearchBundle notificationTypeSearchBundle = searchBundleManager
                .getSearchBundle(notificationTypeSearchBundleName);

        // set all fields searchable
        setResourceFieldsSearchable(resourceSearchBundle);
        setResourceFieldsSearchable(resourceRoleSearchBundle);
        setResourceFieldsSearchable(notificationSearchBundle);
        setResourceFieldsSearchable(notificationTypeSearchBundle);

        // get the id generators...
        IDGenerator resourceIdGenerator;
        IDGenerator resourceRoleIdGenerator;
        IDGenerator notificationTypeIdGenerator;

        try {
            resourceIdGenerator = IDGeneratorFactory.getIDGenerator(resourceIdGeneratorName);
            resourceRoleIdGenerator = IDGeneratorFactory.getIDGenerator(resourceRoleIdGeneratorName);
            notificationTypeIdGenerator = IDGeneratorFactory.getIDGenerator(notificationIdGeneratorName);
        } catch (IDGenerationException e) {
            throw new ConfigurationException("Could not instantiate IDGenerator", e);
        }

        // create ResourceManager instance using reflection...
        Object[] params = new Object[] {persistence, resourceSearchBundle, resourceRoleSearchBundle,
            notificationSearchBundle, notificationTypeSearchBundle, resourceIdGenerator, resourceRoleIdGenerator,
            notificationTypeIdGenerator };
        Class<?>[] paramTypes = new Class[] {ResourcePersistence.class, SearchBundle.class, SearchBundle.class,
            SearchBundle.class, SearchBundle.class, IDGenerator.class, IDGenerator.class, IDGenerator.class };

        return createObject(resourceManagerClassName, ResourceManager.class, params, paramTypes);
    }

    /**
     * Sets the searchable fields to the search bundle.
     * @param searchBundle
     *            the search bundle to set
     */
    private void setResourceFieldsSearchable(SearchBundle searchBundle) {
        Map<String, AbstractObjectValidator> fields = new HashMap<String, AbstractObjectValidator>();

        // set the resource filter fields
        fields.put(ResourceFilterBuilder.RESOURCE_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.PHASE_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.PROJECT_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.SUBMISSION_ID_FIELD_NAME, LongValidator.isPositive());
        fields.put(ResourceFilterBuilder.USER_ID_FIELD_NAME, LongValidator.isPositive());
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
     * Sets the searchable fields to the search bundle.
     * @param searchBundle
     *            the search bundle to set
     */
    private void setUploadFieldsSearchable(SearchBundle searchBundle) {
        Map<String, AbstractObjectValidator> fields = new HashMap<String, AbstractObjectValidator>();

        // set the upload filter fields
        fields.put("upload_id", LongValidator.isPositive());
        fields.put("upload_type_id", LongValidator.isPositive());
        fields.put("upload_status_id", LongValidator.isPositive());
        fields.put("project_id", LongValidator.isPositive());
        fields.put("resource_id", LongValidator.isPositive());
        fields.put("submission_id", LongValidator.isPositive());
        fields.put("submission_status_id", LongValidator.isPositive());
        fields.put("submission_type_id", LongValidator.isPositive());
        fields.put("deliverable_id", LongValidator.isPositive());
        fields.put("phase_id", LongValidator.isPositive());
        fields.put("name", StringValidator.startsWith(""));

        searchBundle.setSearchableFields(fields);
    }

    /**
     * This method is called by initResourceManager() and initUploadManager() to create SearchBundleManager
     * instance.
     * @param namespace
     *            the namespace to load configuration settings from.
     * @return SearchBundleManager instance.
     * @throws ConfigurationException
     *             if 'SearchBundleManagerNS' property is missing or if error occurs during instantiation.
     */
    private SearchBundleManager createSearchBundleManager(String namespace) throws ConfigurationException {
        String searchBundleManagerNS = PhasesHelper.getPropertyValue(namespace, PROP_SEARCH_BUNDLE_MGR_NS, true);

        try {
            return new SearchBundleManager(searchBundleManagerNS);
        } catch (SearchBuilderConfigurationException e) {
            throw new ConfigurationException("Could not instantiate SearchBundleManager.", e);
        }
    }

    /**
     * This method is called by initResourceManager() and initUploadManager() to create either ResourcePersistence
     * or
     * UploadPersistence instance. Since constructor signatures for both these classes is same, this common method
     * is
     * used.
     * @param namespace
     *            the namespace to load configuration settings from.
     * @param className
     *            class name of ResourcePersistence or UploadPersistence implementation.
     * @param expectedType
     *            expected type of the returned instance.
     * @return a ResourcePersistence or UploadPersistence instance.
     * @throws ConfigurationException
     *             if some property is missing or an error occurs during instantiation.
     */
    private <T> T createPersistence(String namespace, String className, Class<T> expectedType)
        throws ConfigurationException {
        // initialize DBConnectionFactory from given namespace, throw exception
        // if property is missing.
        DBConnectionFactory dbConnFactory = PhasesHelper.createDBConnectionFactory(namespace,
                PROP_CONNECTION_FACTORY_NS);
        String connectionName = PhasesHelper.getPropertyValue(namespace, PROP_CONNECTION_NAME, false);

        Object[] params;
        Class<?>[] paramTypes;

        if (PhasesHelper.isStringNullOrEmpty(connectionName)) {
            // if connection name not specified, call constructor with
            // DBConnectionFactory argument.
            params = new Object[] {dbConnFactory };
            paramTypes = new Class<?>[] {DBConnectionFactory.class };
        } else {
            // else, call constructor with DBConnectionFactory and
            // connectionName arguments.
            params = new Object[] {dbConnFactory, connectionName };
            paramTypes = new Class<?>[] {DBConnectionFactory.class, String.class };
        }

        return createObject(className, expectedType, params, paramTypes);
    }

    /**
     * Helper method to instantiate ReviewScoreAggregator.
     * @param namespace
     *            the namespace to load configuration settings from.
     * @return ReviewScoreAggregator instance.
     * @throws ConfigurationException
     *             if instantiation fails.
     */
    private ReviewScoreAggregator initScorecardAggregator(String namespace) throws ConfigurationException {
        String scoreAggregatorNS = PhasesHelper
            .getPropertyValue(namespace, PROP_SCORE_AGGREGATOR_NAMESPACE, false);
        if (PhasesHelper.isStringNullOrEmpty(scoreAggregatorNS)) {
            return new ReviewScoreAggregator();
        } else {
            try {
                return new ReviewScoreAggregator(scoreAggregatorNS);
            } catch (ReviewScoreAggregatorConfigException e) {
                throw new ConfigurationException("could not instantiate ReviewScoreAggregator", e);
            }
        }
    }

    /**
     * This method is used to instantiate ScorecardManager, ReviewManager, ProjectManager, and UserRetrieval
     * instances
     * since all use the same constructor signature.
     * @param namespace
     *            the namespace to load configuration settings from.
     * @param classPropName
     *            name of property which holds the class name to instantiate.
     * @param nsPropName
     *            name of property which holds namespace argument for constructor.
     * @param expectedType
     *            type of instance to be returned.
     * @param nsPropertyReqd
     *            whether property by name nsPropName is required, which is true in case of Retrieval classes,
     *            false
     *            otherwise.
     * @return either a ScorecardManager, ReviewManager, ProjectManager, or UserRetrieval instance.
     * @throws ConfigurationException
     *             if a required property is missing or if a problem occurs during instantiation.
     */
    private <T> T initManager(String namespace, String classPropName, String nsPropName, Class<T> expectedType,
            boolean nsPropertyReqd) throws ConfigurationException {
        String mgrClassName = PhasesHelper.getPropertyValue(namespace, classPropName, true);
        String mgrNamespace = PhasesHelper.getPropertyValue(namespace, nsPropName, nsPropertyReqd);

        Object[] params = null;
        Class<?>[] paramTypes = null;

        if (!PhasesHelper.isStringNullOrEmpty(mgrNamespace)) {
            params = new Object[] {mgrNamespace };
            paramTypes = MANAGER_PARAM_TYPES;
        }

        return createObject(mgrClassName, expectedType, params, paramTypes);
    }

    /**
     * This method is used to instantiate ScorecardManager, ReviewManager, ProjectManager, and UserRetrieval
     * instances
     * since all use the same constructor signature.
     * @param namespace
     *            the namespace to load configuration settings from.
     * @param classPropName
     *            name of property which holds the class name to instantiate.
     * @param nsPropName
     *            name of property which holds namespace argument for constructor.
     * @return either a ScorecardManager, ReviewManager, ProjectManager, or UserRetrieval instance.
     * @throws ConfigurationException
     *             if a required property is missing or if a problem occurs during instantiation.
     * @since 1.3
     */
    private ProjectLinkManager initProjectLinkManager(String namespace, String classPropName, String nsPropName)
        throws ConfigurationException {
        String mgrClassName = PhasesHelper.getPropertyValue(namespace, classPropName, true);
        String mgrNamespace = PhasesHelper.getPropertyValue(namespace, nsPropName, true);

        Object[] params = new Object[] {mgrNamespace, this.projectManager };

        return createObject(mgrClassName, ProjectLinkManager.class, params, PROJECT_LINK_MANAGER_PARAM_TYPES);
    }

    /**
     * <p>Gets the interface to review feedback management system.</p>
     *
     * @param namespace
     *            the namespace to load configuration settings from.
     * @param classPropName
     *            name of property which holds the class name to instantiate.
     * @param nsPropName
     *            name of property which holds namespace argument for constructor.
     * @return a <code>ReviewFeedbackManager</code> providing the interface to review feedback management system.
     * @throws ConfigurationException if any error occurs or obtained configuration object contains invalid
     *         configuration.
     * @since 1.7.6
     */
    private ReviewFeedbackManager initReviewFeedbackManager(String namespace, String classPropName, String nsPropName)
        throws ConfigurationException {
        String configurationManagerFile = PhasesHelper.getPropertyValue(namespace, PROP_CONFIG_MANAGER_FILE, true);
        String mgrClassName = PhasesHelper.getPropertyValue(namespace, classPropName, true);
        String mgrNamespace = PhasesHelper.getPropertyValue(namespace, nsPropName, true);

        Object[] params = new Object[] {configurationManagerFile, mgrNamespace};

        return createObject(mgrClassName, ReviewFeedbackManager.class, params, REVIEW_FEEDBACK_MGR_PARAM_TYPES);
    }

    /**
     * Helper method to instantiate the specified className using reflection. The params is passed to constructor
     * during
     * reflection.
     * <p>
     * Change in version 1.4: Use generic type to create object.
     * </p>
     * @param className
     *            name of class to be instantiated.
     * @param expectedType
     *            expected type of the return instance.
     * @param params
     *            constructor params.
     * @param paramTypes
     *            the Class array specifying the type of Objects in the 'params' parameter
     * @return instance of type className.
     * @throws ConfigurationException
     *             if an error occurs during instantiation.
     */
    private <T> T createObject(String className, Class<T> expectedType, Object[] params, Class<?>[] paramTypes)
        throws ConfigurationException {
        // instantiate using reflection.
        try {
            Class<? extends T> clazz = Class.forName(className).asSubclass(expectedType);
            return clazz.getConstructor(paramTypes).newInstance(params);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Could not find class:" + className, e);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (SecurityException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (InstantiationException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (ClassCastException e) {
            throw new ConfigurationException(className + " must be of type " + expectedType.getName());
        } catch (Error e) {
            throw e;
        }
    }

    /**
     * <p>Instantiates the object from Terms Of Use component.</p>
     * 
     * @param namespace a <code>String</code> providing the configuration namespace for this manager helper.
     * @param classPropName a <code>String</code> providing the name of configuration property with class of object to
     *                      be created.
     * @param nsPropName a <code>String</code> providing the name of configuration property with namespace for object
     *                   initialization.
     * @return an <code>Object</code> constructed based on specified parameters. 
     * @throws ConfigurationException if an unexpected error occurs.
     * @since 1.6.3
     */
    private Object initTermsOfUseComponent(String namespace, String classPropName, String nsPropName)
        throws ConfigurationException {
        String configurationManagerFile = PhasesHelper.getPropertyValue(namespace, PROP_CONFIG_MANAGER_FILE, true);
        String daoClassName = PhasesHelper.getPropertyValue(namespace, classPropName, true);
        String daoNamespace = PhasesHelper.getPropertyValue(namespace, nsPropName, true);
        try {
            ConfigurationObject configurationObject 
                = new ConfigurationFileManager(configurationManagerFile).getConfiguration(daoNamespace).getChild(daoNamespace);

            Object[] params = new Object[]{configurationObject};

            return createObject(daoClassName, Class.forName(daoClassName), params, TERMS_OF_USE_DAOS_PARAM_TYPES);
        } catch (ConfigurationAccessException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (UnrecognizedNamespaceException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (IOException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (ConfigurationParserException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (NamespaceConflictException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (UnrecognizedFileTypeException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("The object could not be instantiated.", e);
        }
    }

}
