/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.project;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.configuration.ConfigurationException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationResourceRole;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.Helper;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;
import com.topcoder.management.review.assignment.ReviewAssignmentProjectManagementException;
import com.topcoder.management.review.assignment.ReviewAssignmentProjectManager;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.log.Log;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.web.ejb.forums.Forums;
import com.topcoder.web.ejb.forums.ForumsHome;

/**
 * <p>
 * Default implementation of ReviewAssignmentProjectManager, which manages project resources and phases
 * according to review assignment. It uses UserRetrieval, ResourceManager and PhaseManager to manage necessary
 * information.
 * </p>
 * <p>
 * This class performs the logging of errors and debug information using Logging Wrapper component.
 * </p>
 * <p>
 * <em>Sample Configuration:</em>
 *
 * <pre>
 * &lt;?xml version="1.0"?&gt;
 * &lt;CMConfig&gt;
 * &lt;Config name="com.topcoder.management.review.assignment.project.DefaultReviewAssignmentProjectManager"&gt;
 * &lt;Property name="loggerName"&gt;
 * &lt;Value&gt;myLogger&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="objectFactoryConfig"&gt;
 * &lt;property name="userRetrieval"&gt;
 * &lt;property name="type"&gt;
 * &lt;value&gt;com.topcoder.management.review.assignment.MockUserRetrieval&lt;/value&gt;
 * &lt;/property&gt;
 * &lt;/property&gt;
 * &lt;property name="phaseManager"&gt;
 * &lt;property name="type"&gt;
 * &lt;value&gt;com.topcoder.management.review.assignment.MockPhaseManager&lt;/value&gt;
 * &lt;/property&gt;
 * &lt;/property&gt;
 * &lt;property name="resourceManager"&gt;
 * &lt;property name="type"&gt;
 * &lt;value&gt;com.topcoder.management.review.assignment.MockResourceManager&lt;/value&gt;
 * &lt;/property&gt;
 * &lt;/property&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="registrationDateFormatString"&gt;
 * &lt;Value&gt;MM.dd.yyyy hh:mm a&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="forumsBeanUrl"&gt;
 * &lt;Value&gt;http://www.topcoder.com/tc&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="forumsBeanFactory"&gt;
 * &lt;Value&gt;com.topcoder.management.review.assignment.MockInitialContextFactory&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="forumsBeanName"&gt;
 * &lt;Value&gt;ForumsBean&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="phaseTypeExtensionRules"&gt;
 * &lt;Property name="1"&gt;
 * &lt;Value&gt;36000&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="2"&gt;
 * &lt;Value&gt;72000&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="operator"&gt;
 * &lt;Value&gt;plus&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;Property name="userRetrievalKey"&gt;
 * &lt;Value&gt;userRetrieval&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="phaseManagerKey"&gt;
 * &lt;Value&gt;phaseManager&lt;/Value&gt;
 * &lt;/Property&gt;
 * &lt;Property name="resourceManagerKey"&gt;
 * &lt;Value&gt;resourceManager&lt;/Value&gt;
 * &lt;/Property&gt;
 *
 * &lt;/Config&gt;
 * &lt;/CMConfig&gt;
 * </pre>
 *
 * </p>
 * <p>
 * Thread Safety: This class is mutable and not thread safe since it uses ResourceManager instance that is not
 * thread safe. It's assumed that {@link #configure(ConfigurationObject)} method will be called just once
 * right after instantiation, before calling any business methods.
 * </p>
 *
 * @author gevak, zhongqiangzhang
 * @version 1.0.1
 *
 */
public class DefaultReviewAssignmentProjectManager implements ReviewAssignmentProjectManager {
    /**
     * <p>
     * Represent the class name.
     * </p>
     */
    private static final String CLASS_NAME = DefaultReviewAssignmentProjectManager.class.getName();

    /**
     * <p>
     * Represent the default date format.
     * </p>
     */
    private static final String DEFAULT_DATE_FORMAT = "MM.dd.yyyy hh:mm a";

    /**
     * <p>
     * Represents &quot;userRetrievalKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_USER_RETRIEVAL = "userRetrievalKey";

    /**
     * <p>
     * Represents &quot;phaseManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_PHASE_MANAGER = "phaseManagerKey";

    /**
     * <p>
     * Represents &quot;resourceManagerKey&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_RESOURCE_MANAGER = "resourceManagerKey";

    /**
     * <p>
     * Represents &quot;registrationDateFormatString&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_REGISTRATION_DATE_FORMAT_STRING = "registrationDateFormatString";

    /**
     * <p>
     * Represents &quot;phaseTypeExtensionRules&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_PHASE_TYPE_EXTENSION_RULES = "phaseTypeExtensionRules";

    /**
     * <p>
     * Represents &quot;operator&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_OPERATOR = "operator";

    /**
     * <p>
     * Represents &quot;forumsBeanUrl&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_FORUMS_BEAN_URL = "forumsBeanUrl";

    /**
     * <p>
     * Represents &quot;forumsBeanFactory&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_FORUMS_BEAN_FACTORY = "forumsBeanFactory";

    /**
     * <p>
     * Represents &quot;forumsBeanName&quot; property key in configuration.
     * </p>
     */
    private static final String KEY_FORUMS_BEAN_NAME = "forumsBeanName";

    /**
     * <p>
     * Represents the java naming provider url constant.
     * </p>
     */
    private static final String JAVA_NAMING_PROVIDER_URL = "java.naming.provider.url";

    /**
     * <p>
     * Represents the java naming factory initial constant.
     * </p>
     */
    private static final String JAVA_NAMING_FACTORY_INITIAL = "java.naming.factory.initial";

    /**
     * <p>
     * Service for retrieving user information.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be null
     * after initialization. Is used in {@link #addReviewersToProject} method.
     * </p>
     */
    private UserRetrieval userRetrieval;

    /**
     * <p>
     * Service for retrieving phase information.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be null
     * after initialization. Is used in {@link #addReviewersToProject} method.
     * </p>
     */
    private PhaseManager phaseManager;

    /**
     * <p>
     * Service for managing resource information.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be null
     * after initialization. Is used in {@link #addReviewersToProject} method.
     * </p>
     */
    private ResourceManager resourceManager;

    /**
     * <p>
     * Mapping of resource roles by their IDs. Key - resource role ID, value - ResourceRole for that ID.
     * </p>
     * <p>
     * Is initialized in {@link #prepareResourceRoleById} method, called from
     * {@link #configure(ConfigurationObject)} and never changed after that. Cannot be null or contain null
     * key/value after initialization. Is used in {@link #addReviewersToProject} method.
     * </p>
     */
    private Map<Long, ResourceRole> resourceRoleById;

    /**
     * <p>
     * Format string for formatting registration date.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be
     * null/empty after initialization. Is used in {@link #addReviewersToProject} method.
     * </p>
     */
    private String registrationDateFormatString;

    /**
     * <p>
     * Map of phase extension rules. Key - phase type ID, value - extension length (in seconds) of that phase
     * type.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be
     * null/empty or contain null key/value after initialization. Is used in {@link #addReviewersToProject}
     * method.
     * </p>
     */
    private Map<Long, Long> phaseTypeExtensionRules;

    /**
     * <p>
     * Operator, used to update phases via PhaseManager.
     * </p>
     * <p>
     * Is initialized in {@link #configure(ConfigurationObject)} and never changed after that. Cannot be
     * null/empty after initialization. Is used in {@link #addReviewersToProject} method.
     * </p>
     */
    private String operator;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the {@link #configure(ConfigurationObject)} method and never changed after that. If
     * is null after initialization, logging is not performed. Is used in business methods.
     * </p>
     */
    private Log log;

    /**
     * <p>
     * URL string for the forums bean.
     * </p>
     * <p>
     * Is initialized in the {@link #configure(ConfigurationObject)} method and never changed after that.
     * </p>
     */
    private String forumsBeanUrl;

    /**
     * <p>
     * Name of the forums bean.
     * </p>
     * <p>
     * Is initialized in the {@link #configure(ConfigurationObject)} method and never changed after that.
     * </p>
     */
    private String forumsBeanName;

    /**
     * <p>
     * Factory name for the forums bean.
     * </p>
     * <p>
     * Is initialized in the {@link #configure(ConfigurationObject)} method and never changed after that.
     * </p>
     */
    private String forumsBeanFactory;

    /**
     * Create an instance of DefaultReviewAssignmentProjectManager.
     */
    public DefaultReviewAssignmentProjectManager() {
    }

    /**
     * Adds reviewers to project and extends project phases if necessary.
     *
     * @param reviewAuction
     *            the Review auction
     * @param project
     *            the Project
     * @param assignment
     *            Set of approved review applications.
     *
     * @throws IllegalArgumentException
     *             If any argument is null or assignment contains null key or argument is invalid.
     * @throws IllegalStateException
     *             if this class was not configured properly with use of
     *             {@link #configure(ConfigurationObject)} method.
     * @throws ReviewAssignmentProjectManagementException
     *             If any other error with managing projects resources and phases occurs.
     */
    public void addReviewersToProject(ReviewAuction reviewAuction, Project project,
                                      Set<ReviewApplication> assignment)
        throws ReviewAssignmentProjectManagementException {
        String signature = CLASS_NAME
            + "#addReviewersToProject(ReviewAuction, Project, Map<ReviewApplication, ReviewApplicationRole>)";
        final long start = System.currentTimeMillis();
        Helper.logEntrance(log, signature, new String[] { "reviewAuction", "project", "assignment" },
            new Object[] { reviewAuction, project, assignment });
        Helper.checkNullIAE(log, signature, reviewAuction, "reviewAuction");
        Helper.checkNullIAE(log, signature, project, "project");
        Helper.checkNullIAE(log, signature, assignment, "assignment");
        Helper.checkListIAE(log, signature, assignment, "assignment|key");

        checkConfigurations(signature);

        // If the assignment is empty, return.
        if (assignment.size() == 0) {
            return;
        }

        // Initialize forums EJB. If any exception occurs, log it and continue.
        Forums forumsBean = null;
        try {
            forumsBean = locateForumsEJB();
        } catch (RemoteException e) {
            Helper.logException(log, signature, new ReviewAssignmentProjectManagementException(
                "Error on loading forums EJB: " + e.getMessage(), e));
        } catch (CreateException e) {
            Helper.logException(log, signature, new ReviewAssignmentProjectManagementException(
                "Error on loading forums EJB: " + e.getMessage(), e));
        } catch (NamingException e) {
            Helper.logException(log, signature, new ReviewAssignmentProjectManagementException(
                "Error on loading forums EJB: " + e.getMessage(), e));
        }

        // Prepare date formatter.
        DateFormat registrationDateFormat = new SimpleDateFormat(registrationDateFormatString, Locale.US);

        // Find user IDs of applicants.
        Map<Long, ExternalUser> users = populateUsers(signature, assignment);
        try {
            // Retrieve project phases.
            com.topcoder.project.phases.Project phasesProject = phaseManager.getPhases(project.getId());
            Phase[] phases = phasesProject.getAllPhases();

            List<Phase> extendedPhases = new ArrayList<Phase>();
            // For each assignment entry.
            for (ReviewApplication reviewApplication : assignment) {
                if (users.get(reviewApplication.getUserId()) != null) {
                    // Get ReviewApplicationRole associated with this review application.
                    ReviewApplicationRole applicationRole = Helper.getReviewApplicationRoleByID(reviewAuction,
                            reviewApplication.getApplicationRoleId());
                    // For each review application resource role.
                    for (ReviewApplicationResourceRole reviewApplicationResourceRole :
                            applicationRole.getResourceRoles()) {
                        // Belong to invalid parameter.
                        Helper.checkNullIAE(log, signature,
                            reviewApplicationResourceRole.getResourceRoleId(),
                            "assignment|Value|ResourceRoles|ResourceRoleId");

                        // Get resource role by ID.
                        ResourceRole resourceRole = resourceRoleById.get(reviewApplicationResourceRole
                            .getResourceRoleId());
                        if (resourceRole == null) {
                            continue;
                        }
                        // Create a new Resource instance.
                        Resource resource = new Resource();
                        if (resourceRole.getPhaseType() != null) {
                            Phase phase = findPhase(phases, resourceRole.getPhaseType());
                            if (phase == null) {
                                // Skip current resource role (i.e. don't add it to the project).
                                Helper.logWarn(log, "Skipping resource role [" + resourceRole.getName() +
                                    "] for project [" + project.getId() + "] due to not being able to locate " +
                                    " the corresponding project phase [" + resourceRole.getPhaseType() + "]");
                                continue;
                            }
                            resource.setPhase(phase.getId());

                            if (extendPhase(phase)) {
                                extendedPhases.add(phase);
                            }
                        }

                        resource.setProject(project.getId());
                        resource.setResourceRole(resourceRole);
                        resource.setUserId(reviewApplication.getUserId());
                        resource.setProperty("External Reference ID", String.valueOf(reviewApplication.getUserId()));
                        resource.setProperty("Handle", users.get(reviewApplication.getUserId()).getHandle());
                        resource.setProperty("Registration Date", registrationDateFormat.format(new Date()));

                        // Save the new resource.
                        resourceManager.updateResource(resource, operator);

                        // Grant the member forum access if needed.
                        if (project.getProperty("Developer Forum ID") != null
                            && project.getProperty("Developer Forum ID").toString().trim().length() > 0) {
                            try {
                                if (forumsBean != null) {
                                    forumsBean.assignRole(reviewApplication.getUserId(),
                                        "Software_Users_" + project.getProperty("Developer Forum ID"));
                                }
                            } catch (EJBException e) {
                                Helper.logException(log, signature, new ReviewAssignmentProjectManagementException(
                                    "Error to assign role from Bean for " + e.getMessage(), e));
                            } catch (RemoteException e) {
                                Helper.logException(log, signature, new ReviewAssignmentProjectManagementException(
                                    "Error to assign role from Bean for " + e.getMessage(), e));
                            }
                        }
                    }
                }
            }

            // Recalculate start and end dates of project phases if an extension was made.
            if (extendedPhases.size() > 0) {
                List<StringBuilder> logMessages = new ArrayList<StringBuilder>();
                for (Phase logPhase : extendedPhases) {
                    StringBuilder logMessage = new StringBuilder();
                    logMessage.append("The phase has been extended: ID [").append(logPhase.getId());
                    logMessage.append("], Type [").append(logPhase.getPhaseType().getName());
                    logMessage.append("], Old end date [").append(logPhase.getScheduledStartDate());
                    logMessage.append("], New end date [").append(logPhase.calcEndDate()).append("]");
                    logMessages.add(logMessage);
                }

                // Recalculate scheduled dates for all project phases.
                for (Phase phase : phases) {
                    phase.setScheduledEndDate(phase.calcEndDate());
                    phase.setScheduledStartDate(phase.calcStartDate());
                }

                // Persist the phase changes.
                this.phaseManager.updatePhases(phasesProject, operator);

                for (StringBuilder logMessage : logMessages) {
                    Helper.logInfo(log, logMessage.toString());
                }
            } else {
                Helper.logInfo(log, "No phase was extended.");
            }
        } catch (PhaseManagementException e) {
            throw Helper.logException(log, signature, new ReviewAssignmentProjectManagementException(
                "Error to update phase for " + e.getMessage(), e));
        } catch (ResourcePersistenceException e) {
            throw Helper.logException(log, signature, new ReviewAssignmentProjectManagementException(
                "Error to update resource for " + e.getMessage(), e));
        } catch (NullPointerException e) {
            throw Helper.logException(log, signature, new ReviewAssignmentProjectManagementException(
                "Error to set property for " + e.getMessage(), e));
        }

        Helper.logExit(log, signature, null, start);
    }

    /**
     * Helper method that retrieves users info from the UserRetrieval interface.
     *
     * @param signature
     *            the 'addReviewersToProject' signature to log.
     * @param assignment
     *            Set of approved review applications.
     * @return mapping of user id and ExternalUser.
     * @throws IllegalArgumentException
     *             If assignment does not set properties correctly.
     * @throws ReviewAssignmentProjectManagementException
     *             if error to retrieve ExternalUser from db.
     */
    private Map<Long, ExternalUser> populateUsers(String signature,
                                                  Set<ReviewApplication> assignment)
        throws ReviewAssignmentProjectManagementException {
        Map<Long, ExternalUser> users = new HashMap<Long, ExternalUser>();
        try {

            for (ReviewApplication reviewApplication : assignment) {
                Helper.checkNullIAE(log, signature, reviewApplication.getUserId(), "assignment|Key|UserId");
                users.put(reviewApplication.getUserId(), null);
            }

            ExternalUser[] externalUsers = userRetrieval.retrieveUsers(Helper.convertLong(users.keySet()));
            for (ExternalUser externalUser : externalUsers) {
                users.put(externalUser.getId(), externalUser);
            }
        } catch (RetrievalException e) {
            throw Helper.logException(log, signature,
                new ReviewAssignmentProjectManagementException(e.getMessage()));
        }
        return users;
    }

    /**
     * Extend a project phase if necessary.
     *
     * @param phase
     *            the Phase to be extended.
     * @return the flag that phase is extended or not.
     */
    private boolean extendPhase(Phase phase) {
        boolean extended = false;

        if ((phase.getPhaseStatus().getId() == PhaseStatus.OPEN.getId())
            && (phase.getActualStartDate() != null)) {
            if (phaseTypeExtensionRules.containsKey(phase.getPhaseType().getId())) {

                long newLength = System.currentTimeMillis()
                    + phaseTypeExtensionRules.get(phase.getPhaseType().getId()) * 1000
                    - phase.getActualStartDate().getTime();

                if (newLength > phase.getLength()) {
                    extended = true;
                    phase.setLength(newLength);
                }
            }
        }

        return extended;
    }

    /**
     * Finds the phase of the specified type.
     *
     * @param phases
     *            the list Phase to find.
     * @param phaseTypeId
     *            ID of the phase type to look for.
     * @return the found Phase or null if not found.
     */
    private Phase findPhase(Phase[] phases, long phaseTypeId) {
        for (Phase phase : phases) {
            if (phase.getPhaseType().getId() == phaseTypeId) {
                return phase;
            }
        }
        return null;
    }

    /**
     * <p>
     * Configures this instance using the given configuration object.
     * </p>
     * <p>
     * See section 3.2 of CS for details about the configuration parameters. Please use value constraints
     * provided in that section to check whether values read from configuration object are valid.
     * </p>
     *
     * @param config
     *            the configuration object
     *
     * @throws IllegalArgumentException
     *             if config is null
     * @throws ReviewAssignmentConfigurationException
     *             if some error occurred when initializing an instance using the given configuration
     */
    public void configure(ConfigurationObject config) {
        ExceptionUtils.checkNull(config, null, null, "The parameter 'config' should not be null.");

        this.log = Helper.getLog(config);

        // create object factory
        ObjectFactory objectFactory = Helper.createObjectFactory(config);

        // Create dependencies via OF:
        this.userRetrieval = Helper.createObject(config, objectFactory, KEY_USER_RETRIEVAL,
            UserRetrieval.class);

        this.phaseManager = Helper.createObject(config, objectFactory, KEY_PHASE_MANAGER, PhaseManager.class);

        this.resourceManager = Helper.createObject(config, objectFactory, KEY_RESOURCE_MANAGER,
            ResourceManager.class);

        String dateFormatString = Helper.getPropertyValue(config, KEY_REGISTRATION_DATE_FORMAT_STRING, false);
        this.registrationDateFormatString = (dateFormatString == null) ? DEFAULT_DATE_FORMAT
            : dateFormatString;

        // Load extension rules:
        this.phaseTypeExtensionRules = getPhaseTypeExtensionRules(config);

        // Load operator parameter:
        this.operator = Helper.getPropertyValue(config, KEY_OPERATOR, true);

        // Prepare lookup map for resource roles:
        prepareResourceRoleById();
        
        this.forumsBeanUrl = Helper.getPropertyValue(config, KEY_FORUMS_BEAN_URL, true);
        this.forumsBeanName = Helper.getPropertyValue(config, KEY_FORUMS_BEAN_NAME, true);
        this.forumsBeanFactory = Helper.getPropertyValue(config, KEY_FORUMS_BEAN_FACTORY, true);
    }

    /**
     * Get phaseTypeExtensionRules from ConfigurationObject.
     *
     * @param config
     *            the ConfigurationObject object.
     * @return Map of phase extension rules. Key - phase type ID, value - extension length (in seconds) of
     *         that phase type.
     * @throws ReviewAssignmentConfigurationException
     *             If any other error when getting phaseTypeExtensionRules.
     */
    private Map<Long, Long> getPhaseTypeExtensionRules(ConfigurationObject config) {
        Map<Long, Long> phaseTypeExtensionRulesMap = new HashMap<Long, Long>();
        ConfigurationObject mapConfig = Helper.getChildConfig(config, KEY_PHASE_TYPE_EXTENSION_RULES);
        String[] keys;
        try {
            keys = mapConfig.getAllPropertyKeys();

            for (String key : keys) {
                Long val = mapConfig.getLongProperty(key, true);
                if (val < 0) {
                    throw new ReviewAssignmentConfigurationException("values must be non-negative.");
                }
                phaseTypeExtensionRulesMap.put(Long.valueOf(key), val);
            }

            return phaseTypeExtensionRulesMap;
        } catch (ConfigurationException e) {
            throw new ReviewAssignmentConfigurationException(
                "Error to get phaseTypeExtensionRules caused by " + e.getMessage(), e);
        } catch (NumberFormatException nfe) {
            throw new ReviewAssignmentConfigurationException(
                "Error to get phaseTypeExtensionRules with Long key and value " + nfe.getMessage(), nfe);
        }
    }

    /**
     * Prepares lookup map for resource roles.
     *
     * @throws ReviewAssignmentConfigurationException
     *             If any other error with managing projects resources and phases occurs.
     */
    private void prepareResourceRoleById() {
        if (resourceRoleById == null) {
            resourceRoleById = new HashMap<Long, ResourceRole>();
            ResourceRole[] resourceRoles;
            try {
                resourceRoles = resourceManager.getAllResourceRoles();
            } catch (ResourcePersistenceException e) {
                throw new ReviewAssignmentConfigurationException(e.getMessage());
            }
            for (ResourceRole resourceRole : resourceRoles) {
                resourceRoleById.put(resourceRole.getId(), resourceRole);
            }
        }
    }

    /**
     * Check if the class is configured properly.
     *
     * @param signature
     *            the signature used to log.
     *
     * @throws IllegalStateException
     *             if the any property is <code>null</code>.
     */
    private void checkConfigurations(String signature) {
        Helper.checkState(userRetrieval, "userRetrieval", log, signature);
        Helper.checkState(resourceManager, "resourceManager", log, signature);

        Helper.checkState(phaseManager, "phaseManager", log, signature);
        Helper.checkState(registrationDateFormatString, "registrationDateFormatString", log, signature);

        Helper.checkState(phaseTypeExtensionRules, "phaseTypeExtensionRules", log, signature);
        Helper.checkState(operator, "operator", log, signature);

        Helper.checkState(forumsBeanUrl, "forumsBeanUrl", log, signature);
        Helper.checkState(forumsBeanName, "forumsBeanName", log, signature);
        Helper.checkState(forumsBeanFactory, "forumsBeanFactory", log, signature);
        Helper.checkState(resourceRoleById, "resourceRoleById", log, signature);
    }


    /**
     * <p>Locates the reference to Forums EJB.</p>
     * 
     * @return a <code>Forums</code> providing the interface to Forums EJB.
     * @throws RemoteException
     *             if any error occurs while locating the forums EJB.
     * @throws CreateException
     *             if any error occurs while locating the forums EJB.
     * @throws NamingException
     *             if any error occurs while locating the forums EJB.
     */
    private Forums locateForumsEJB() throws RemoteException, CreateException, NamingException {
        // Load EJB
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(JAVA_NAMING_PROVIDER_URL, forumsBeanUrl);
        env.put(JAVA_NAMING_FACTORY_INITIAL, forumsBeanFactory);
        ForumsHome forumsHome = (ForumsHome) new InitialContext(env).lookup(forumsBeanName);
        return forumsHome.create();
    }
}
