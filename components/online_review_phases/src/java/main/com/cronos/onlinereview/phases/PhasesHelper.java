/*
 * Copyright (C) 2004 - 2015 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.io.Serializable;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cronos.onlinereview.phases.logging.LogMessage;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Prize;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ValidationException;
import com.topcoder.management.project.link.ProjectLink;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.resource.search.ResourceRoleFilterBuilder;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.InconsistentDataException;
import com.topcoder.management.review.scoreaggregator.RankedSubmission;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;

/**
 * <p>
 * A class having helper methods to perform argument validation and other phase related methods used by the
 * PhaseHandler implementations.
 * </p>
 * <p>
 * Version 1.1 (Appeals Early Completion Release Assembly 1.0) Change notes:
 * <ol>
 * <li>Added support for Early Appeals Completion.</li>
 * <li>Removed aggregator/final reviewer payment duplication.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.2 (Contest Dependency Automation Release Assembly 1.0) Change notes:
 * <ol>
 * <li>Added a method for checking if all projects which requested project depends on are completed to the project
 * could start.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.3 (Online Review End Of Project Analysis Release Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #insertPostMortemPhase(Project, Phase, ManagerHelper, String)} method to fix the bugs with
 * creation of <code>Post-Mortem</code> phase.</li>
 * <li>Added {@link #insertApprovalPhase(Project, Phase, ManagerHelper, String)} method.</li>
 * <li>Added {@link #searchProjectResourcesForRoleNames(ManagerHelper, Connection, String[], long)} method.</li>
 * <li>Added {@link #getApprovalPhaseReviews(Review[], Phase)} method.</li>
 * <li>Added {@link #searchProjectReviewsForResourceRoles(Connection, ManagerHelper, long, String[], Long)} method
 * to handle Post-Mortem phase correctly.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4 (Member Post-Mortem Review Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #insertPostMortemPhase(Project, Phase, ManagerHelper, String)} method to create Post-Mortem
 * phase only if there is respective flag set in project properties.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4 Change notes:
 * <ol>
 * <li>Updated {@link #getScreeningTasks(ManagerHelper, Connection, Phase)} method to add function to get
 * submission with "Contest Submission" submission type.</li>
 * <li>Updated {@link #searchActiveSubmissions(UploadManager, Connection, long, String)} method to add function to
 * search all active submissions with specific submission type.</li>
 * <li>Added {@link #isFirstPhase(Phase)} method.</li>
 * <li>Added {@link #insertSpecSubmissionSpecReview(Phase, PhaseManager, String)} method.</li>
 * <li>Added {@link #hasOneSpecificationSubmission(long, ManagerHelper, Connection, Log)} method.</li>
 * <li>Added some constants.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4.3 Change notes:
 * <ol>
 * <li>Phase attributes are copied for newly created Specification Review phase.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.5 Change notes:
 * <ol>
 * <li>Added {@link #logPhaseHandlingException(Log, PhaseHandlingException, String, Long)} method to add function
 * to log PhaseHandlingException instance.</li>
 * </ol>
 * </p>
 * <p>
 * <p>
 * Version 1.6 Change notes:
 * <ol>
 * <li>Added {@link #getSubmissionById(Submission[], long)} method.</li>
 * <li>Added {@link #breakTies(RankedSubmission, Submission[], RankedSubmission[])} method.</li>
 * <li>Added {@link #searchActiveCheckpointSubmissions(UploadManager, Connection, long, long, Log)} method.</li>
 * <li>Change to use getUploads().get(0) to retrieve the unique upload for software competitions.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.7 (Online Review Replatforming Release 2 ) Change notes:
 * <ol>
 * <li>Change submission.getUploads() to submission.getUpload().</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.7.1 Change notes:
 * <ol>
 * <li>Remove method getScreeningTasks.</li>
 * <li>Remove method isScreeningManual.</li>
 * <li>Updated arePhaseDependenciesMet(), reachedPhaseStartTime() and canPhaseStart() methods to return
 * OperationCheckResult instead of simply boolean value.</li>
 * <li>Change the name of arePhaseDependenciesMet() to checkPhaseDependenciesMet().</li>
 * <li>Change the name of reachedPhaseStartTime() to checkPhaseStartTimeReached() and return OperationCheckResult.</li>
 * <li>Change the name of canPhaseStart() to checkPhaseCanStart() and return OperationCheckResult.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Version 1.7.1 (BUGR-4779) Change notes:
 * <ol>
 * <li>Added {@link #setSubmissionPrize(com.topcoder.management.project.Project, Submission[], String, double)} method to populate prizes for submissions.</li>
 * </ol>
 * </p>
 * 
 * <p>
 * Version 1.7.2 (TCCC-3631) Change notes:
 * <ol>
 * <li>Added {@link #autoScreenStudioSubmissions(com.topcoder.management.project.Project, ManagerHelper, String, Connection, String) method to delete studio submission 
 * if they have user rank more than configured for the project.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Version 1.8.6 (TC Online Review Update for Peer Review) Change notes:
 * <ol>
 * <li>Added methods to check if the given project/phase is of the "PEER" review type.</li>
 * </ol>
 * </p>
 * 
 * @author tuenm, bose_java, pulky, aroglite, waits, isv, saarixx, myxgyy, microsky, flexme, lmmortal, Vovka
 * @version 1.8.6
 */
final class PhasesHelper {
    /**
     * Constant for reviewer role names to be used when searching for reviewer resources
     * and review scorecards.
     */
    static final String[] REVIEWER_ROLE_NAMES = new String[] {
        Constants.ROLE_REVIEWER,
        Constants.ROLE_ACCURACY_REVIEWER,
        Constants.ROLE_FAILURE_REVIEWER,
        Constants.ROLE_STRESS_REVIEWER };

    /**
     * This constant stores Payment property key.
     * @since 1.1
     */
    static final String PAYMENT_PROPERTY_KEY = "Payment";

    /**
     * This constant stores Payment Status property key.
     * @since 1.4
     */
    static final String PAYMENT_STATUS_PROPERTY_KEY = "Payment Status";

    /**
     * <p>
     * A <code>String</code> providing the name for handle property of the resource.
     * </p>
     * @since 1.4
     */
    static final String HANDLE = "Handle";

    /**
     * This constant stores Appeals Completed Early flag property key.
     * @since 1.1
     */
    private static final String APPEALS_COMPLETED_EARLY_PROPERTY_KEY = "Appeals Completed Early";

    /**
     * This constant stores &quot;Yes&quot; value for Appeals Completed Early flag property.
     * @since 1.1
     */
    private static final String YES_VALUE = "Yes";

    /**
     * an array of comment types which denote that a comment is a reviewer comment.
     */
    private static final String[] REVIEWER_COMMENT_TYPES = new String[] {
        Constants.COMMENT_TYPE_COMMENT,
        Constants.COMMENT_TYPE_REQUIRED,
        Constants.COMMENT_TYPE_RECOMMENDED };

    /**
     * Represents the maximum submissions property key.
     * @since 1.7.2
     */
    static final String MAXIMUM_SUBMISSIONS = "Maximum Submissions";

    /**
     * Represents the review type property key.
     * @since 1.8.6
     */
    static final String REVIEW_TYPE = "Review Type";

    /**
     * private to prevent instantiation.
     */
    private PhasesHelper() {
        // do nothing.
    }

    /**
     * Checks whether the given Object is null and throws IllegalArgumentException if yes.
     * @param arg
     *            the argument to check
     * @param name
     *            the name of the argument
     * @throws IllegalArgumentException
     *             if the given Object is null
     */
    static void checkNull(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * Checks whether the given String is null or empty.
     * @param arg
     *            the String to check
     * @param name
     *            the name of the argument
     * @throws IllegalArgumentException
     *             if the given string is null or empty
     */
    static void checkString(String arg, String name) {
        checkNull(arg, name);

        if (arg.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty.");
        }
    }

    /**
     * Returns true if given string is either null or empty, false otherwise.
     * @param str
     *            string to check.
     * @return true if given string is either null or empty, false otherwise.
     */
    static boolean isStringNullOrEmpty(String str) {
        return ((str == null) || (str.trim().length() == 0));
    }

    /**
     * Checks if the map is valid. The key should not be null/empty, the value should not
     * be null.
     * @param map
     *            the map to verify
     * @throws IllegalArgumentException
     *             if the map is invalid
     */
    static void checkValuesMap(Map<String, Object> map) {
        checkNull(map, "Values map");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            checkString(entry.getKey(), "key in values map");
            checkNull(entry.getValue(), "value in values map");
        }
    }

    /**
     * Helper method to retrieve a property value from given configuration namespace. If
     * the isRequired flag is true and if the property does not exist, then this method
     * throws a ConfigurationException.
     * @param namespace
     *            configuration namespace to use.
     * @param propertyName
     *            name of the property.
     * @param isRequired
     *            whether property is required.
     * @return value for given property name.
     * @throws ConfigurationException
     *             if namespace is unknown or if required property does not exist.
     */
    static String getPropertyValue(String namespace, String propertyName, boolean isRequired)
        throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getInstance();

        try {
            String value = configManager.getString(namespace, propertyName);

            if (isRequired && isStringNullOrEmpty(value)) {
                throw new ConfigurationException("Property '" + propertyName + "' must have a value.");
            }

            return value;
        } catch (UnknownNamespaceException ex) {
            throw new ConfigurationException("Namespace '" + namespace + "' does not exist.", ex);
        }
    }

    /**
     * Returns true if the property by the given name exists in the namespace, false
     * otherwise.
     * @param namespace
     *            configuration namespace to use.
     * @param propertyName
     *            name of the property.
     * @return true if the property by the given name exists in the namespace, false
     *         otherwise.
     * @throws ConfigurationException
     *             if namespace is unknown.
     */
    static boolean doesPropertyExist(String namespace, String propertyName)
        throws ConfigurationException {
        try {
            ConfigManager configManager = ConfigManager.getInstance();
            Enumeration<?> propNames = configManager.getPropertyNames(namespace);

            while (propNames.hasMoreElements()) {
                if (propNames.nextElement().equals(propertyName)) {
                    return true;
                }
            }

            return false;
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException("Namespace '" + namespace + "' does not exist.", e);
        }
    }

    /**
     * Returns true if the property by the given name exists in the namespace, false
     * otherwise.
     * @param namespace
     *            configuration namespace to use.
     * @param propertyName
     *            name of the property.
     * @return true if the property by the given name exists in the namespace, false
     *         otherwise.
     * @throws ConfigurationException
     *             if namespace is unknown.
     */
    static boolean doesPropertyObjectExist(String namespace, String propertyName)
        throws ConfigurationException {
        try {
            return ConfigManager.getInstance().getPropertyObject(namespace, propertyName) != null;
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException("Namespace '" + namespace + "' does not exist.", e);
        }
    }

    /**
     * A helper method to create an instance of DBConnectionFactory. This method retrieves
     * the value for connection factory namespace from the given property name and
     * namespace and uses the same to create an instance of DBConnectionFactoryImpl.
     * @param namespace
     *            configuration namespace to use.
     * @param connFactoryNSPropName
     *            name of property which holds connection factory namespace value.
     * @return DBConnectionFactory instance.
     * @throws ConfigurationException
     *             if property is missing or if there was an error when instantiating
     *             DBConnectionFactory.
     */
    @SuppressWarnings("deprecation")
    static DBConnectionFactory createDBConnectionFactory(String namespace, String connFactoryNSPropName)
        throws ConfigurationException {
        String connectionFactoryNS = getPropertyValue(namespace, connFactoryNSPropName, true);

        try {
            return new DBConnectionFactoryImpl(connectionFactoryNS);
        } catch (UnknownConnectionException ex) {
            throw new ConfigurationException("Could not instantiate DBConnectionFactoryImpl", ex);
        } catch (com.topcoder.db.connectionfactory.ConfigurationException ex) {
            throw new ConfigurationException("Could not instantiate DBConnectionFactoryImpl", ex);
        }
    }

    /**
     * Verifies that the phase is of desired type. Throws PhaseNotSupportedException if
     * not.
     * @param phase
     *            phase to check.
     * @param type
     *            desired phase type.
     * @throws PhaseNotSupportedException
     *             if phase is not of desired type.
     */
    static void checkPhaseType(Phase phase, String type) throws PhaseNotSupportedException {
        String givenPhaseType = phase.getPhaseType().getName();

        if (!type.equals(givenPhaseType)) {
            throw new PhaseNotSupportedException("Phase must be of type " + type + ". It is of type "
                + givenPhaseType);
        }
    }

    /**
     * Returns true if phase status is &quot;Scheduled&quot;, false if status is &quot;Open&quot; and throws
     * PhaseHandlingException for any other status value.
     * @param phaseStatus
     *            the phase status.
     * @return true if phase status is &quot;Scheduled&quot;, false if status is &quot;Open&quot;.
     * @throws PhaseHandlingException
     *             if phase status is neither &quot;Scheduled&quot; nor &quot;Open&quot;.
     */
    static boolean checkPhaseStatus(PhaseStatus phaseStatus) throws PhaseHandlingException {
        checkNull(phaseStatus, "phaseStatus");

        if (isPhaseToStart(phaseStatus)) {
            return true;
        } else if (isPhaseToEnd(phaseStatus)) {
            return false;
        } else {
            throw new PhaseHandlingException("Phase status '" + phaseStatus.getName() + "' is not valid.");
        }
    }

    /**
     * Returns whether the phase is to end or not by checking if status is &quot;Scheduled&quot;.
     * @param status
     *            the phase status.
     * @return true if status is &quot;Scheduled&quot;, false otherwise.
     */
    static boolean isPhaseToStart(PhaseStatus status) {
        return (Constants.PHASE_STATUS_SCHEDULED.equals(status.getName()));
    }

    /**
     * Returns whether the phase is to end or not by checking if status is &quot;Open&quot;.
     * @param status
     *            the phase status.
     * @return true if status is &quot;Open&quot;, false otherwise.
     */
    static boolean isPhaseToEnd(PhaseStatus status) {
        return isPhaseOpen(status);
    }

    /**
     * Returns if phase is closed, i.e. has status &quot;Closed&quot;.
     * @param status
     *            the phase status.
     * @return true if phase status is &quot;Closed&quot;, false otherwise.
     */
    static boolean isPhaseClosed(PhaseStatus status) {
        return (Constants.PHASE_STATUS_CLOSED.equals(status.getName()));
    }

    /**
     * Returns if phase has started, i.e. has status &quot;Open&quot;.
     * @param status
     *            the phase status.
     * @return true if phase status is &quot;Closed&quot;, false otherwise.
     */
    static boolean isPhaseOpen(PhaseStatus status) {
        return (Constants.PHASE_STATUS_OPEN.equals(status.getName()));
    }

    /**
     * Returns true if all the dependencies of the given phase have started/stopped based
     * on the type of dependency, or if the phase has no dependencies.
     * <p>
     * Change in version 1.4:<br/ >
     * If phase B is configured to start when phase A starts, if the phase A is already closed, phase B should
     * start.<br/ >
     * If phase B is configured to end when phase A starts. It should end if the phase A is already closed.
     * </p>
     * <p>
     * Version 1.6.1 changes note:
     * <ul>
     * <li>The return changes from boolean to OperationCheckResult.</li>
     * <li>Change the name of PhasesHelper#arePhaseDependenciesMet() to checkPhaseDependenciesMet().</li>
     * </ul>
     * </p>
     * @param phase
     *            the phase to check.
     * @param bPhaseStarting
     *            true if phase is starting, false if phase is ending.
     * @return the validation result indicating whether the dependencies are met, and if not,
     *         providing a reasoning message (not null)
     */
    static OperationCheckResult checkPhaseDependenciesMet(Phase phase, boolean bPhaseStarting) {
        Dependency[] dependencies = phase.getAllDependencies();

        if ((dependencies == null) || (dependencies.length == 0)) {
            return OperationCheckResult.SUCCESS;
        }

        for (Dependency dependency : dependencies) {
            // get the dependency phase.
            Phase dependencyPhase = dependency.getDependency();

            if (bPhaseStarting) {
                if (dependency.isDependencyStart() && dependency.isDependentStart()) {
                    // S2S dependencies should be started change in version 1.4
                    // If phase B is configured to start when phase A starts, if the phase
                    // A is already closed, phase B should start in this case
                    if (isDependencyMet(dependencyPhase)) {
                        return new OperationCheckResult("Dependency " + dependencyPhase.getPhaseType().getName()
                            + " phase is not yet started.");
                    }
                } else if (!dependency.isDependencyStart() && dependency.isDependentStart()) {
                    // S2F dependencies should be closed
                    if (!isPhaseClosed(dependencyPhase.getPhaseStatus())) {
                        return new OperationCheckResult("Dependency " + dependencyPhase.getPhaseType().getName()
                            + " phase is not yet ended.");
                    }
                }
            } else {
                if (dependency.isDependencyStart() && !dependency.isDependentStart()) {
                    // F2S dependencies should be started change in version 1.4
                    // If phase B is configured to end when phase A starts. It should end
                    // if the phase A is already closed.
                    if (isDependencyMet(dependencyPhase)) {
                        return new OperationCheckResult("Dependency " + dependencyPhase.getPhaseType().getName()
                            + " phase is not yet started.");
                    }
                } else if (!dependency.isDependencyStart() && !dependency.isDependentStart()) {
                    // F2F dependencies should be closed
                    if (!isPhaseClosed(dependencyPhase.getPhaseStatus())) {
                        return new OperationCheckResult("Dependency " + dependencyPhase.getPhaseType().getName()
                            + " phase is not yet ended.");
                    }
                }
            }
        }
        // all are met.
        return OperationCheckResult.SUCCESS;
    }

    /**
     * Check whether the dependency is met.
     * @param dependency
     * @return true if the dependecy is met
     * @since 1.6.1
     */
    private static boolean isDependencyMet(Phase dependency) {
        return !(isPhaseOpen(dependency.getPhaseStatus()) || isPhaseClosed(dependency.getPhaseStatus()));
    }

    /**
     * Returns true if current time is later or equal to the start time of the given
     * phase. This will return true in case phase.calcStartDate() returns null.
     * <p>
     * Version 1.6.1 changes note:
     * <ul>
     * <li>The return changes from boolean to OperationCheckResult.</li>
     * <li>Change the name of PhasesHelper#reachedPhaseStartTime() to checkPhaseStartTimeReached() and return
     * OperationCheckResult.</li>
     * </ul>
     * </p>
     * @param phase the phase to check.
     * @return the validation result indicating whether the phase reaches the start time, and if not,
     *         providing a reasoning message (not null)
     */
    private static OperationCheckResult checkPhaseStartTimeReached(Phase phase) {
        Date startDate = phase.calcStartDate();

        if (startDate == null) {
            return OperationCheckResult.SUCCESS;
        } else {
            if (new Date().before(startDate)) {
                return new OperationCheckResult("Phase start time is not yet reached.");
            }
            return OperationCheckResult.SUCCESS;
        }
    }

    /**
     * Returns true if current time is later or equal to the end time of the given phase.
     * @param phase
     *            the phase to check.
     * @return true if current time is later or equal to the end time of the given phase.
     */
    static boolean reachedPhaseEndTime(Phase phase) {
        return (!new Date().before(phase.calcEndDate()));
    }

    /**
     * This method is used to check if a phase can start. It checks for following:<br/>
     * 1. if phase dependencies have been met.<br/ >
     * 2. if start time has been reached.<br/ >
     * The method will return true only if both the conditions are true, false otherwise.
     * <p>
     * Version 1.6.1 changes note:
     * <ul>
     * <li>The return changes from boolean to OperationCheckResult.</li>
     * </ul>
     * </p>
     * @param phase the phase instance to start.
     * @return the validation result indicating whether the phase can start, and if not,
     *         providing a reasoning message (not null)
     */
    static OperationCheckResult checkPhaseCanStart(Phase phase) {
        OperationCheckResult result = PhasesHelper.checkPhaseDependenciesMet(phase, true);
        if (!result.isSuccess()) {
            return result;
        }
        return PhasesHelper.checkPhaseStartTimeReached(phase);
    }

    /**
     * Helper method to find a backward phase or forward phase from a given phase with
     * given phase type.
     * @param phase
     *            phase to search from.
     * @param phaseType
     *            phase type to search.
     * @param forward
     *            true to search forwards, false to search backwards.
     * @param required
     *            whether the target phase is required.
     * @return nearest backward or forward phase.
     * @throws PhaseHandlingException
     *             if no such phase exists.
     */
    static Phase locatePhase(Phase phase, String phaseType, boolean forward, boolean required)
        throws PhaseHandlingException {
        // get all phases for the project
        Phase[] phases = phase.getProject().getAllPhases();
        int currentPhaseIndex = 0;

        for (int i = 0; i < phases.length; i++) {
            if (phase.getId() == phases[i].getId()) {
                currentPhaseIndex = i;

                break;
            }
        }

        if (forward) {
            // get the next phase with desired type
            for (int i = currentPhaseIndex + 1; i < phases.length; i++) {
                if (phaseType.equals(phases[i].getPhaseType().getName())) {
                    return phases[i];
                }
            }
        } else {
            // get the previous phase with desired type
            for (int i = currentPhaseIndex - 1; i >= 0; i--) {
                if (phaseType.equals(phases[i].getPhaseType().getName())) {
                    return phases[i];
                }
            }
        }

        // could not find phase with desired type...
        if (required) {
            throw new PhaseHandlingException("Could not find nearest phase of type " + phaseType);
        } else {
            return null;
        }
    }

    /**
     * Returns all the reviews for a phase.
     * @param managerHelper
     *            ManagerHelper instance.
     * @param phaseId Phase ID to search reviews for.
     * @param submissionId
     *            submission id to be used as filter when searching for reviews.
     * @return Review[] which match filter conditions.
     * @throws PhaseHandlingException
     *             if there was an error during retrieval.
     */
    static Review[] searchReviewsForPhase(ManagerHelper managerHelper, long phaseId, Long submissionId)
            throws PhaseHandlingException {
            try {
                Filter phaseFilter = new EqualToFilter("projectPhase", phaseId);
                Filter fullReviewFilter = phaseFilter;

                // if submission id filter is given, add it as filter condition
                if (submissionId != null) {
                    Filter submissionFilter = new EqualToFilter("submission", submissionId);
                    fullReviewFilter = new AndFilter(phaseFilter, submissionFilter);
                }

                return managerHelper.getReviewManager().searchReviews(fullReviewFilter, true);
            } catch (ReviewManagementException e) {
                throw new PhaseHandlingException("Problem with review retrieval", e);
            }
    }

    /**
     * Returns all the reviews for a phase based on resource.
     * @param managerHelper
     *            ManagerHelper instance.
     * @param reviewers
     *            the reviewer resource array
     * @param phaseId
     *            phase id to be used as filter.
     * @param submissionId
     *            submission id to be used as filter when searching for reviews.
     * @return Review[] which match filter conditions.
     * @throws PhaseHandlingException
     *             if there was an error during retrieval.
     */
    static Review[] searchReviewsForResources(ManagerHelper managerHelper,
        Resource[] reviewers, long phaseId, Long submissionId) throws PhaseHandlingException {
        if (reviewers.length == 0) {
            return new Review[0];
        }

        try {
            // create reviewer ids array
            Long[] reviewerIds = new Long[reviewers.length];

            for (int i = 0; i < reviewers.length; i++) {
                reviewerIds[i] = reviewers[i].getId();
            }

            Filter reviewFilter = new InFilter("reviewer", Arrays.asList(reviewerIds));
            Filter fullReviewFilter = new AndFilter(reviewFilter, new EqualToFilter("projectPhase", phaseId));

            // if submission id filter is given, add it as filter condition
            if (submissionId != null) {
                Filter submissionFilter = new EqualToFilter("submission", submissionId);
                fullReviewFilter = new AndFilter(fullReviewFilter, submissionFilter);
            }

            return managerHelper.getReviewManager().searchReviews(fullReviewFilter, true);
        } catch (ReviewManagementException e) {
            throw new PhaseHandlingException("Problem with review retrieval", e);
        }
    }

    /**
     * searches for resources based on resource role names and phase id filters.
     * @param managerHelper
     *            ManagerHelper instance.
     * @param resourceRoleNames
     *            array of resource role names.
     * @param phaseId
     *            phase id.
     * @return Resource[] which match search criteria.
     * @throws PhaseHandlingException
     *             if an error occurs during retrieval.
     */
    static Resource[] searchResourcesForRoleNames(ManagerHelper managerHelper,
        String[] resourceRoleNames, long phaseId) throws PhaseHandlingException {
        List<Long> resourceRoleIds = new ArrayList<Long>();

        ResourceManager resourceManager = managerHelper.getResourceManager();
        try {
            for (String resourceRoleName : resourceRoleNames) {
                resourceRoleIds.add(LookupHelper.getResourceRole(resourceManager, resourceRoleName).getId());
            }

            Filter resourceRoleFilter = new InFilter(
                ResourceRoleFilterBuilder.RESOURCE_ROLE_ID_FIELD_NAME, resourceRoleIds);
            Filter phaseIdFilter = ResourceFilterBuilder.createPhaseIdFilter(phaseId);
            Filter fullFilter = new AndFilter(resourceRoleFilter, phaseIdFilter);

            return resourceManager.searchResources(fullFilter);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("Problem with search builder configuration", e);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("There was a resource retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        }
    }

    /**
     * <p>
     * Searches for resources associated with specified project and the specified resource roles assigned.
     * </p>
     * @param managerHelper
     *            ManagerHelper instance.
     * @param resourceRoleNames
     *            array of resource role names.
     * @param projectId
     *            ID for the project to find associated resources for.
     * @return Resource[] which match search criteria.
     * @throws PhaseHandlingException
     *             if an error occurs during retrieval.
     * @since 1.3
     */
    static Resource[] searchProjectResourcesForRoleNames(ManagerHelper managerHelper,
        String[] resourceRoleNames, long projectId) throws PhaseHandlingException {
        List<Long> resourceRoleIds = new ArrayList<Long>();

        ResourceManager resourceManager = managerHelper.getResourceManager();
        try {
            for (String resourceRoleName : resourceRoleNames) {
                resourceRoleIds.add(LookupHelper.getResourceRole(resourceManager, resourceRoleName).getId());
            }

            Filter resourceRoleFilter = new InFilter(
                ResourceRoleFilterBuilder.RESOURCE_ROLE_ID_FIELD_NAME, resourceRoleIds);
            Filter projectIdFilter = ResourceFilterBuilder.createProjectIdFilter(projectId);
            Filter fullFilter = new AndFilter(resourceRoleFilter, projectIdFilter);

            return resourceManager.searchResources(fullFilter);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("Problem with search builder configuration", e);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("There was a resource retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        }
    }

    /**
     * Gets the scorecard minimum score from the given review.
     * @param scorecardManager
     *            ScorecardManager instance.
     * @param review
     *            Review instance.
     * @return the scorecard minimum score from the given review.
     * @throws PhaseHandlingException
     *             if a problem occurs when retrieving scorecard.
     */
    static float getScorecardMinimumScore(ScorecardManager scorecardManager, Review review)
        throws PhaseHandlingException {
        long scorecardId = review.getScorecard();

        try {
            Scorecard[] scoreCards = scorecardManager.getScorecards(new long[] {scorecardId }, false);

            if (scoreCards.length == 0) {
                throw new PhaseHandlingException("No scorecards found for scorecard id: " + scorecardId);
            }

            Scorecard scoreCard = scoreCards[0];

            return scoreCard.getMinScore();
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("Problem with scorecard retrieval", e);
        }
    }

    /**
     * A utility method to get the integer value for the given phase attribute. This
     * method throws PhaseHandlingException if the attribute value is null or could not be
     * parsed into an integer.
     * @param phase
     *            phase instance.
     * @param attrName
     *            name of attribute.
     * @return integer value for the given phase attribute.
     * @throws PhaseHandlingException
     *             if the attribute value is null or could not be parsed into an integer.
     */
    static int getIntegerAttribute(Phase phase, String attrName) throws PhaseHandlingException {
        String sValue = (String) phase.getAttribute(attrName);

        if (sValue == null) {
            throw new PhaseHandlingException("Phase attribute '" + attrName + "' was null.");
        }

        try {
            return Integer.parseInt(sValue);
        } catch (NumberFormatException e) {
            throw new PhaseHandlingException("Phase attribute '" + attrName + "' was non-integer:" + sValue, e);
        }
    }

    /**
     * Retrieves all submissions for the given phase id, submission type and submission statuses.
     * @param uploadManager
     *            UploadManager instance to use for searching.
     * @param projectPhaseId
     *            project phase id.
     * @param typeName
     *            the submission type name.
     * @param statusName
     *            the submission status name.
     * @return all active submissions for the given project id.
     * @throws PhaseHandlingException
     *             if an error occurs during retrieval.
     */
    static Submission[] getPhaseSubmissions(UploadManager uploadManager,
        long projectPhaseId, String typeName, String statusName) throws PhaseHandlingException {

        try {
            long statusId = LookupHelper.getSubmissionStatus(uploadManager, statusName).getId();
            Filter statusFilter = SubmissionFilterBuilder.createSubmissionStatusIdFilter(statusId);

            long typeId = LookupHelper.getSubmissionType(uploadManager, typeName).getId();
            Filter typeFilter = SubmissionFilterBuilder.createSubmissionTypeIdFilter(typeId);

            Filter phaseIdFilter = SubmissionFilterBuilder.createProjectPhaseIdFilter(projectPhaseId);

            Filter fullFilter = new AndFilter(Arrays.asList(phaseIdFilter, statusFilter, typeFilter));
            return uploadManager.searchSubmissions(fullFilter);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("There was a submission retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("There was a search builder error", e);
        }
    }

    /**
     * Retrieves all submissions for the given project id, submission type and submission statuses.
     * @param uploadManager
     *            UploadManager instance to use for searching.
     * @param projectId
     *            project id.
     * @param submissionTypeName
     *            the submission type name.
     * @param submissionStatusNames
     *            the submission status names.
     * @return all active submissions for the given project id.
     * @throws PhaseHandlingException
     *             if an error occurs during retrieval.
     */
    static Submission[] getProjectSubmissions(UploadManager uploadManager,
        long projectId, String submissionTypeName, String[] submissionStatusNames) throws PhaseHandlingException {

        try {
            List<Filter> statusFilters = new ArrayList<Filter>();
            for(String statusName : submissionStatusNames) {
                long statusId = LookupHelper.getSubmissionStatus(uploadManager, statusName).getId();
                statusFilters.add(SubmissionFilterBuilder.createSubmissionStatusIdFilter(statusId));
            }

            long submissionTypeId = LookupHelper.getSubmissionType(uploadManager, submissionTypeName).getId();
            Filter submissionTypeFilter = SubmissionFilterBuilder.createSubmissionTypeIdFilter(submissionTypeId);

            Filter projectIdFilter = SubmissionFilterBuilder.createProjectIdFilter(projectId);

            Filter fullFilter = new AndFilter(Arrays.asList(projectIdFilter,
                new OrFilter(statusFilters), submissionTypeFilter));

            return uploadManager.searchSubmissions(fullFilter);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("There was a submission retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("There was a search builder error", e);
        }
    }

    /**
     * Retrieves all active submissions for the given project id and submission type.
     * @param uploadManager
     *            UploadManager instance to use for searching.
     * @param projectId
     *            project id.
     * @param submissionTypeName
     *            the submission type name.
     * @return all active submissions for the given project id.
     * @throws PhaseHandlingException
     *             if an error occurs during retrieval.
     */
    static Submission[] getActiveProjectSubmissions(UploadManager uploadManager,
        long projectId, String submissionTypeName) throws PhaseHandlingException {
        return getProjectSubmissions(uploadManager, projectId, submissionTypeName, new String[]{Constants.SUBMISSION_STATUS_ACTIVE});
    }

    /**
     * Searches the earliest active submission.
     * @param uploadManager
     *            UploadManager instance to use for searching.
     * @param projectId
     *            project ID.
     * @return the earliest active submission or null if no record found.
     * @throws PhaseHandlingException
     *             if any error occurred when searching submission or more than one
     *             submission exist.
     */
    static Submission getEarliestActiveSubmission(UploadManager uploadManager, long projectId) throws PhaseHandlingException {
        // Search all "Active" submissions for current project with contest submission type
        Submission[] submissions = PhasesHelper.getActiveProjectSubmissions(uploadManager,
                projectId, Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION);

        // Find submission with the earliest creation timestamp.
        Submission earliestSubmission = null;
        for (Submission sub : submissions) {
            if (earliestSubmission == null ||
                    sub.getCreationTimestamp().before(earliestSubmission.getCreationTimestamp())) {
                earliestSubmission = sub;
            }
        }

        return earliestSubmission;
    }

    /**
     * This method checks if the winning submission has one aggregated review scorecard
     * committed and returns the same.
     * @param managerHelper
     *            ManagerHelper instance.
     * @param phaseId
     *            phase id.
     * @return review scorecard, null if does not exist.
     * @throws PhaseHandlingException
     *             if an error occurs when retrieving data or if there are multiple scorecards.
     */
    static Review getWorksheet(ManagerHelper managerHelper, long phaseId)
        throws PhaseHandlingException {
        // Search the scorecard
        Review[] reviews = searchReviewsForPhase(managerHelper, phaseId, null);

        if (reviews.length == 0) {
            return null;
        } else if (reviews.length == 1) {
            return reviews[0];
        } else {
            throw new PhaseHandlingException("Cannot have multiple scorecards for phase " + phaseId);
        }
    }

    /**
     * Returns the winning submitter for the given project id.
     * @param managerHelper
     *            ManagerHelper instance.
     * @param projectId
     *            project id.
     * @return the winning submitter Resource.
     * @throws PhaseHandlingException
     *             if an error occurs when searching for resource.
     */
    static Resource getWinningSubmitter(ManagerHelper managerHelper, long projectId)
        throws PhaseHandlingException {
        try {
            ResourceManager resourceManager = managerHelper.getResourceManager();
            ProjectManager projectManager = managerHelper.getProjectManager();
            com.topcoder.management.project.Project project = projectManager.getProject(projectId);

            Long winnerId;
            try {
                winnerId = Long.parseLong((String) project.getProperty("Winner External Reference ID"));
            } catch (NumberFormatException nfe) {
                winnerId = null;
            }

            if (winnerId != null) {
                long submitterRoleId = LookupHelper.getResourceRole(resourceManager,
                    Constants.ROLE_SUBMITTER).getId();

                AndFilter fullFilter = new AndFilter(Arrays.asList(new Filter[] {
                    ResourceFilterBuilder.createResourceRoleIdFilter(submitterRoleId),
                    ResourceFilterBuilder.createProjectIdFilter(projectId),
                    ResourceFilterBuilder.createUserIdFilter(winnerId)}));

                Resource[] submitters = resourceManager.searchResources(fullFilter);

                if (submitters.length > 0) {
                    return submitters[0];
                }

                return null;
            }

            return null;
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when retrieving resource", e);
        } catch (com.topcoder.management.project.PersistenceException e) {
            throw new PhaseHandlingException("Problem retrieving project id: " + projectId, e);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("Problem with search builder configuration", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        }
    }

    /**
     * <p>
     * Inserts a post-mortem phase into persistence.
     * </p>
     * @param currentPrj
     *            current project.
     * @param currentPhase
     *            current phase to insert a post-mortem phase.
     * @param managerHelper
     *            a helper for accessing various managers.
     * @param operator
     *            the operator name.
     * @throws PhaseHandlingException
     *             if any error occurs.
     * @since 1.1
     */
    static void insertPostMortemPhase(Project currentPrj, Phase currentPhase,
        ManagerHelper managerHelper, String operator) throws PhaseHandlingException {
        // Check if Post-Mortem phase already exists for the project. If so then do
        // nothing
        if (null != getPostMortemPhase(currentPrj)) {
            return;
        }

        // Check if Post-Mortem is required based on project properties. If not then do nothing
        ProjectManager projectManager = managerHelper.getProjectManager();

        try {
            com.topcoder.management.project.Project project = projectManager.getProject(currentPrj.getId());
            String postMortemRequired = (String) project.getProperty("Post-Mortem Required");

            if ((postMortemRequired == null) || !(postMortemRequired.equalsIgnoreCase("true"))) {
                return;
            }
        } catch (com.topcoder.management.project.PersistenceException e) {
            throw new PhaseHandlingException("Failed to retrieve details for project " + currentPrj.getId(), e);
        }

        PhaseManager phaseManager = managerHelper.getPhaseManager();

        // create phase type and status objects
        PhaseType postMortemPhaseType = LookupHelper.getPhaseType(phaseManager, Constants.PHASE_POST_MORTEM);
        PhaseStatus phaseStatus = LookupHelper.getPhaseStatus(phaseManager, Constants.PHASE_STATUS_SCHEDULED);

        try {
            // Create new Post-Mortem phase
            String postMortemPhaseDuration = getPropertyValue(PostMortemPhaseHandler.class.getName(),
                "PostMortemPhaseDuration", true);

            createNewPhases(currentPrj, currentPhase, new PhaseType[] {postMortemPhaseType },
                new Long[] {Long.parseLong(postMortemPhaseDuration) * Constants.HOUR }, phaseStatus, false);

            // Set the number of required reviewers for Post-Mortem phase to default value
            String postMortemPhaseDefaultReviewerNumber = getPropertyValue(PostMortemPhaseHandler.class.getName(),
                "PostMortemPhaseDefaultReviewersNumber", true);
            String postMortemPhaseDefaultScorecardID = getPropertyValue(PostMortemPhaseHandler.class.getName(),
                "PostMortemPhaseDefaultScorecardID", true);
            Phase postMortemPhase = getPostMortemPhase(currentPrj);
            postMortemPhase.setAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER, postMortemPhaseDefaultReviewerNumber);
            postMortemPhase.setAttribute(Constants.PHASE_CRITERIA_SCORECARD_ID, postMortemPhaseDefaultScorecardID);

            phaseManager.updatePhases(currentPrj, operator);
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        } catch (ConfigurationException e) {
            throw new PhaseHandlingException("Problem when reading configuration file", e);
        }
    }

    /**
     * Inserts a final fix and final review phases.
     * @param currentPhase
     *            current phase to insert a post-mortem phase.
     * @param phaseManager
     *            the PhaseManager instance.
     * @param operator
     *            the operator name.
     * @param finalFixDuration
     *            the duration of final fix phase.
     * @return index of current phase.
     * @throws PhaseHandlingException
     *             if any error occurs.
     * @since 1.1
     */
    static int insertFinalFixAndFinalReview(Phase currentPhase, PhaseManager phaseManager,
        String operator, Long finalFixDuration) throws PhaseHandlingException {
        PhaseType finalFixPhaseType = LookupHelper.getPhaseType(phaseManager, Constants.PHASE_FINAL_FIX);
        PhaseType finalReviewPhaseType = LookupHelper.getPhaseType(phaseManager, Constants.PHASE_FINAL_REVIEW);
        PhaseStatus phaseStatus = LookupHelper.getPhaseStatus(phaseManager, Constants.PHASE_STATUS_SCHEDULED);

        // find current phase index and also the lengths of 'final fix' and 'final review' phases.
        Project currentPrj = currentPhase.getProject();

        // use helper method to create the new phases
        int currentPhaseIndex = PhasesHelper.createNewPhases(currentPrj, currentPhase, new PhaseType[] {
            finalFixPhaseType, finalReviewPhaseType }, new Long[] {finalFixDuration * Constants.HOUR, null}, phaseStatus, true);

        // save the phases
        try {
            phaseManager.updatePhases(currentPrj, operator);
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Problem when persisting phases", e);
        }

        return currentPhaseIndex;
    }

    /**
     * Checks if the given phase if the first phase in the project. Note that if multiple
     * phases start at the same date/time at the beginning of the project, all they are
     * considered to be first phases of the project.
     * @param phase
     *            the phase to be checked.
     * @return true if phase is the first phase in the project, false otherwise.
     * @since 1.4
     */
    static boolean isFirstPhase(Phase phase) {
        // Get all phases for the project
        Phase[] phases = phase.getProject().getAllPhases();

        // Get index of the input phase in phases array
        int phaseIndex = 0;

        for (int i = 0; i < phases.length; i++) {
            if (phases[i].getId() == phase.getId()) {
                phaseIndex = i;
            }
        }

        while ((phaseIndex > 0)
            && (phases[phaseIndex - 1].calcStartDate().compareTo(phases[phaseIndex].calcStartDate()) == 0)) {
            phaseIndex--;
        }

        return phaseIndex == 0;
    }

    /**
     * <p>
     * Helper method to add new phases of given type to the given project. This method does the following:
     * <ol>
     * <li>finds the index of given phase in the current phases array of the project.</li>
     * <li>finds the lengths of new phases (if not specified explicitly in the parameter).</li>
     * <li>creates new Phase instance with given type and status</li>
     * <li>creates a new Phases array with additional elements for new phase instances.</li>
     * <li>removes all phases of the project.</li>
     * <li>adds each Phase from the new Phases array to the project.</li>
     * </ol>
     * </p>
     * @param currentPrj
     *            project to add/remove phases from.
     * @param currentPhase
     *            current phase instance.
     * @param newPhaseTypes
     *            types of new phases to create.
     * @param newPhaseLengths
     *            the lengths for the new phases, elements can be null, in which case the lengths for the
     *            corresponding phases will be copied from the existing phase of the same type
     * @param newPhaseStatus
     *            the status to set for all the phases.
     * @param adjustPhaseDependencies
     *            <code>true</code> if old dependencies to the current phase must be replaced
     *            with dependencies to the new phase.
     * @return returns the index of the current phase in the phases array.
     */
    static int createNewPhases(Project currentPrj, Phase currentPhase, PhaseType[] newPhaseTypes, Long[] newPhaseLengths,
                               PhaseStatus newPhaseStatus, boolean adjustPhaseDependencies) {
        // find current phase index and also the lengths of new phases
        Phase[] phases = currentPrj.getAllPhases();

        // find the lengths of corresponding phase types, if not specified explicitly in the method parameter
        for (int p = 0; p < newPhaseLengths.length; p++) {
            if (newPhaseLengths[p] == null) {
                for (Phase phase : phases) {
                    if (phase.getPhaseType().getId() == newPhaseTypes[p].getId()) {
                        newPhaseLengths[p] = phase.getLength();
                    }
                }
            }

            // If there was no phase of the same type, assume the default phase duration of 24 hours.
            if (newPhaseLengths[p] == null) {
                newPhaseLengths[p] = 24 * Constants.HOUR;
            }
        }

        int currentPhaseIndex = 0;
        for (int i = 0; i < phases.length; i++) {
            if (phases[i].getId() == currentPhase.getId()) {
                currentPhaseIndex = i;
            }
        }

        // create new phases array which will hold the new phase order
        Phase[] newPhases = new Phase[phases.length + newPhaseTypes.length];
        // set the old phases into the new phases array
        for (int i = 0; i < phases.length; i++) {
            if (i > currentPhaseIndex) {
                newPhases[i + newPhaseTypes.length] = phases[i];
            } else {
                newPhases[i] = phases[i];
            }
        }

        // create new phases for each phase type...
        for (int p = 0; p < newPhaseTypes.length; p++) {
            Phase newPhase = new Phase(currentPrj, newPhaseLengths[p]);
            newPhase.setPhaseStatus(newPhaseStatus);
            newPhase.setPhaseType(newPhaseTypes[p]);

            // the new phase is dependent on the earlier phase
            newPhase.addDependency(new Dependency(newPhases[currentPhaseIndex + p], newPhase, false, true, 0));

            newPhases[currentPhaseIndex + (p + 1)] = newPhase;
        }

        // Replace all old dependencies to the current phase with dependencies to the last new phase
        if (adjustPhaseDependencies) {
            Phase lastNewPhase = newPhases[currentPhaseIndex + newPhaseTypes.length];
            for(Phase phase : phases) {
                for (Dependency dependency : phase.getAllDependencies()) {
                    if (dependency.getDependency().getId() == currentPhase.getId()) {
                        dependency.setDependency(lastNewPhase);
                    }
                }
            }
        }

        // add the new phases to the project
        for (int p = 0; p < newPhaseTypes.length; p++) {
            Phase newPhase = newPhases[currentPhaseIndex + (p + 1)];
            currentPrj.addPhase(newPhase);
        }

        // set the scheduled start and end times after dependencies are changed
        for (Phase phase : newPhases) {
            phase.setScheduledStartDate(phase.calcStartDate());
            phase.setScheduledEndDate(phase.calcEndDate());
        }

        return currentPhaseIndex;
    }

    /**
     * Creates an Final Reviewer/Specification Reviewer resource. This method is
     * called when a new final fix/review or specification submission/review
     * cycle is inserted when final review worksheet or specification review is rejected.
     * It simply copies the old resource properties, except for the id and
     * phase id and inserts the new resource in the database.
     * <p>
     * Change in version 1.4: it now can create specification reviewer resource.
     * </p>
     * @param oldResource
     *            the old resource to be copied
     * @param managerHelper
     *            ManagerHelper instance.
     * @param roleName
     *            role name of the new resource.
     * @param newPhaseId
     *            the new phase id the new resource is to be associated with.
     * @param operator
     *            operator name.
     * @return the id of the newly created resource.
     * @throws PhaseHandlingException if any error occurred.
     */
    static long copyReviewerResource(Resource oldResource, ManagerHelper managerHelper,
                                     String roleName, long newPhaseId, String operator)
        throws PhaseHandlingException {

        if (oldResource == null) {
            throw new IllegalArgumentException("oldResource can not be null.");
        }

        // copy resource properties
        Resource newResource = new Resource();
        newResource.setProject(oldResource.getProject());
        newResource.setResourceRole(LookupHelper.getResourceRole(managerHelper.getResourceManager(), roleName));
        newResource.setUserId(oldResource.getUserId());

        // OrChange - modified to set the submissions
        newResource.setSubmissions(oldResource.getSubmissions());

        Map<?, ?> properties = oldResource.getAllProperties();

        if ((properties != null) && !properties.isEmpty()) {
            Set<?> entries = properties.entrySet();

            for (Object entry1 : entries) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) entry1;

                if (!PAYMENT_PROPERTY_KEY.equals(entry.getKey())) {
                    newResource.setProperty((String) entry.getKey(), entry.getValue());
                }
            }
        }

        // don't duplicate payments
        newResource.setProperty(PAYMENT_STATUS_PROPERTY_KEY, "N/A");

        // set phase id
        newResource.setPhase(newPhaseId);

        // update resource into persistence.
        try {
            managerHelper.getResourceManager().updateResource(newResource, operator);

            return newResource.getId();
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when persisting resource with role:" + roleName, e);
        }
    }

    /**
     * copies the comments from one worksheet to another. Which comments are copied are
     * determined by the typesToCopy and extraInfoToCheck parameters.
     * @param fromWorkSheet
     *            source worksheet for the comments.
     * @param toWorkSheet
     *            destination worksheet.
     * @param typesToCopy
     *            types of comments to copy.
     * @param extraInfoToCheck
     *            extra info to check the comment against.
     */
    static void copyComments(Review fromWorkSheet, Review toWorkSheet, String[] typesToCopy,
        String extraInfoToCheck) {
        Comment[] comments = fromWorkSheet.getAllComments();

        // copy all comments with given type and extra info.
        for (Comment comment : comments) {
            if (isCommentToBeCopied(comment, typesToCopy, extraInfoToCheck)) {
                toWorkSheet.addComment(copyComment(comment));
            }
        }
    }

    /**
     * This helper method copies the review items from one worksheet to another. It will
     * also copy the comments for each review item from one worksheet to another. Which
     * comments are copied are determined by the typesToCopy.
     * @param fromWorkSheet
     *            source worksheet for the comments.
     * @param toWorkSheet
     *            destination worksheet.
     * @param typesToCopy
     *            types of comments to copy.
     */
    static void copyReviewItems(Review fromWorkSheet, Review toWorkSheet, String[] typesToCopy) {
        Item[] reviewItems = fromWorkSheet.getAllItems();

        for (Item item : reviewItems) {
            // create a new review item and copy all properties
            Item newItem = new Item(item.getId());
            newItem.setDocument(item.getDocument());
            newItem.setQuestion(item.getQuestion());
            newItem.setAnswer(item.getAnswer());

            // copy all comments with given type and extra info.
            Comment[] comments = item.getAllComments();

            for (Comment comment : comments) {
                if (isCommentToBeCopied(comment, typesToCopy, null)) {
                    newItem.addComment(copyComment(comment));
                }
            }

            // add the item to the destination worksheet
            toWorkSheet.addItem(newItem);
        }
    }

    /**
     * This helper method copies the review items from one worksheet to another. It will
     * also copy the comments for each review item from one worksheet to another. Only
     * reviewer item comments which are marked as "Accept" will be copied. Once such an
     * item comment is found, the follow-up comments are copied until the next reviewer
     * item is found which is not accepted.
     * @param fromWorkSheet
     *            source worksheet for the comments.
     * @param toWorkSheet
     *            destination worksheet.
     */
    static void copyFinalReviewItems(Review fromWorkSheet, Review toWorkSheet) {
        Item[] reviewItems = fromWorkSheet.getAllItems();

        for (Item item : reviewItems) {
            // create a new review item and copy all properties
            Item newItem = new Item(item.getId());
            newItem.setDocument(item.getDocument());
            newItem.setQuestion(item.getQuestion());
            newItem.setAnswer(item.getAnswer());

            // copy all comments with given type and extra info.
            Comment[] comments = item.getAllComments();
            boolean copy = false;

            for (Comment comment : comments) {
                // if it is a reviewer comment
                if (isReviewerComment(comment)) {
                    // mark copy flag as true or false based on whether it is accepted.
                    copy = "Accept".equals(comment.getExtraInfo());
                }

                // if copy flag is marked true, then copy all comments.
                if (copy) {
                    newItem.addComment(copyComment(comment));
                }
            }

            // add the item to the destination worksheet
            toWorkSheet.addItem(newItem);
        }
    }

    /**
     * Deep clone a review effectually making all items new.
     * @param review
     *            the review to be cloned.
     * @return the cloned review.
     */
    static Review cloneReview(Review review) {
        Review copiedReview = new Review();
        copiedReview.setAuthor(review.getAuthor());
        copiedReview.setCommitted(review.isCommitted());
        copiedReview.setScore(review.getScore());
        copiedReview.setScorecard(review.getScorecard());
        copiedReview.setSubmission(review.getSubmission());
        copiedReview.setProjectPhase(review.getProjectPhase());

        Comment[] comments = review.getAllComments();

        for (Comment comment : comments) {
            Comment copiedComment = new Comment();
            copiedComment.setAuthor(comment.getAuthor());
            copiedComment.setComment(comment.getComment());
            copiedComment.setCommentType(comment.getCommentType());
            copiedComment.setExtraInfo(comment.getExtraInfo());
            copiedReview.addComment(copiedComment);
        }

        Item[] items = review.getAllItems();

        for (Item item : items) {
            Item copiedItem = new Item();
            copiedItem.setAnswer(item.getAnswer());
            copiedItem.setDocument(item.getDocument());
            copiedItem.setQuestion(item.getQuestion());
            copiedReview.addItem(copiedItem);

            comments = item.getAllComments();

            for (Comment comment : comments) {
                Comment copiedComment = new Comment();
                copiedComment.setAuthor(comment.getAuthor());
                copiedComment.setComment(comment.getComment());
                copiedComment.setCommentType(comment.getCommentType());
                copiedComment.setExtraInfo(comment.getExtraInfo());
                copiedItem.addComment(copiedComment);
            }
        }

        return copiedReview;
    }

    /**
     * <p>
     * Gets the submitter information and submission result for different phases.
     * </p>
     * <p>
     * Change in version 1.4, it will now search active submission with contest submission type.
     * </p>
     * @param helper
     *            the ManagerHelper class
     * @param projectId
     *            the project id
     * @param appealPhase
     *            true if it is in appeals phase
     * @return List of values map, map for each submission
     * @throws PhaseHandlingException
     *             if any error occurs
     */
    static List<Map<String, Object>> getSubmitterValueArray(ManagerHelper helper,
        long projectId, boolean appealPhase) throws PhaseHandlingException {
        // get the submissions
        Submission[] submissions;

         // changes in version 1.4
         submissions = PhasesHelper.getActiveProjectSubmissions(helper.getUploadManager(),
             projectId, Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION);

        // for each submission, get the submitter and its scores
        try {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			DecimalFormat df = new DecimalFormat("#.##");

            for (Submission submission : submissions) {
                Map<String, Object> values = new HashMap<String, Object>();
                values.put("SUBMITTER_HANDLE", notNullValue(helper.getResourceManager().getResource(
                    submission.getUpload().getOwner()).getProperty("Handle")));
                values.put(appealPhase ? "SUBMITTER_SCORE" : "SUBMITTER_PRE_APPEALS_SCORE",
                    df.format(submission.getInitialScore()));
                result.add(values);
            }

            return result;
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when looking up resource for the submission.", e);
        }
    }

    /**
     * <p>
     * Constructs the submitter information value map list for email generation content.
     * </p>
     * @param submissions
     *            the submissions
     * @param resourceManager
     *            the ResourceManager in the application
     * @param screeningEnd
     *            true if it is the end of screening phase
     * @return List of map values
     * @throws PhaseHandlingException
     *             any error occurs
     * @since 1.2
     */
    static List<Map<String, Object>> constructSubmitterValues(Submission[] submissions,
        ResourceManager resourceManager, boolean screeningEnd) throws PhaseHandlingException {
        try {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

            for (Submission submission : submissions) {
                Map<String, Object> values = new HashMap<String, Object>();

                // find the submitter (it is a resource) by the id
                Resource submitter = resourceManager.getResource(submission.getUpload().getOwner());
                values.put("SUBMITTER_HANDLE", notNullValue(submitter.getProperty("Handle")));

                if (!screeningEnd) {
                    values.put("SUBMITTER_RELIABILITY", notNullValue(submitter.getProperty("Reliability")));
                    values.put("SUBMITTER_RATING", notNullValue(submitter.getProperty("Rating")));
                } else {
                    Double initialScore = submission.getScreeningScore();
                    DecimalFormat df = new DecimalFormat("#.##");
                    values.put("SUBMITTER_SCORE", initialScore != null ? df.format(initialScore) : notNullValue(initialScore));

                    boolean failedScreening = false;
                    if (submission.getSubmissionStatus() != null) {
                        String statusName = submission.getSubmissionStatus().getName();
                        failedScreening = statusName.equalsIgnoreCase(Constants.SUBMISSION_STATUS_FAILED_SCREENING) ||
                            statusName.equalsIgnoreCase(Constants.SUBMISSION_STATUS_FAILED_CHECKPOINT_SCREENING);
                    }
                    values.put("SUBMITTER_RESULT", failedScreening ? "Failed Screening": "Passed Screening");
                }

                result.add(values);
            }

            return result;
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when looking up resource for the submission.", e);
        }
    }

    /**
     * <p>
     * Gets not null/empty property value for the given value. If it is null/empty, return 'N/A'.
     * </p>
     * @param value
     *            the value of property
     * @return not null value
     * @since 1.2
     */
    static Object notNullValue(Object value) {
        if ((value == null) || (value instanceof String && (((String) value).trim().length() == 0))) {
            return "N/A";
        }

        return value;
    }

    /**
     * Returns true if the comment is a reviewer comment, false otherwise. The comment is
     * said to be a reviewer comment if it is one of the REVIEWER_COMMENT_TYPES elements.
     * @param comment
     *            comment to check.
     * @return true if the comment is a reviewer comment, false otherwise.
     */
    private static boolean isReviewerComment(Comment comment) {
        return Arrays.asList(REVIEWER_COMMENT_TYPES).contains(comment.getCommentType().getName());
    }

    /**
     * Returns a new comment which is a copy of the given comment, only with no extra info
     * set.
     * @param comment
     *            comment to be copied.
     * @return a new comment which is a copy of the given comment.
     */
    private static Comment copyComment(Comment comment) {
        Comment newComment = new Comment(comment.getId());
        newComment.setAuthor(comment.getAuthor());
        newComment.setComment(comment.getComment());
        newComment.setCommentType(comment.getCommentType());

        return newComment;
    }

    /**
     * checks if the comment is to be copied i.e. is one of the comment types that have to
     * be copied to the review worksheet.
     * @param comment
     *            comment to check.
     * @param typesToCopy
     *            types of comments to copy.
     * @param extraInfoToCheck
     *            extra info to check the comment against.
     * @return true if it is to be copied, false otherwise.
     */
    private static boolean isCommentToBeCopied(Comment comment, String[] typesToCopy, String extraInfoToCheck) {
        String commentType = comment.getCommentType().getName();

        for (String typeTocopy : typesToCopy) {
            // return true if it is one of the types to be copied and it is marked as "Accept"
            if (commentType.equals(typeTocopy)) {
                if (extraInfoToCheck != null) {
                    if (extraInfoToCheck.equals(comment.getExtraInfo())) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns whether all submitters agreed to early appeals phase completion.
     * @param managerHelper
     *            ManagerHelper instance.
     * @param projectId
     *            project id.
     * @return true if all submitters agreed to early appeals phase completion
     * @throws PhaseHandlingException
     *             if an error occurs when searching for resource.
     * @since 1.1
     */
    static boolean canCloseAppealsEarly(ManagerHelper managerHelper, long projectId) throws PhaseHandlingException {
        try {
            ResourceManager resourceManager = managerHelper.getResourceManager();
            UploadManager uploadManager = managerHelper.getUploadManager();
            long submitterRoleId = LookupHelper.getResourceRole(resourceManager, Constants.ROLE_SUBMITTER).getId();

            AndFilter fullFilter = new AndFilter(Arrays.asList(new Filter[] {
                ResourceFilterBuilder.createResourceRoleIdFilter(submitterRoleId),
                ResourceFilterBuilder.createProjectIdFilter(projectId),
                ResourceFilterBuilder.createExtensionPropertyNameFilter(APPEALS_COMPLETED_EARLY_PROPERTY_KEY),
                ResourceFilterBuilder.createExtensionPropertyValueFilter(YES_VALUE) }));

            Resource[] earlyAppealCompletionsSubmitters = resourceManager.searchResources(fullFilter);

            // move resource ids to a hashset to speed up lookup
            Set<Long> earlyAppealResourceIds = new HashSet<Long>(earlyAppealCompletionsSubmitters.length);

            for (Resource r : earlyAppealCompletionsSubmitters) {
                earlyAppealResourceIds.add(r.getId());
            }

            // check all submitters with active submission statuses (this will leave out failed screening and deleted)
            Submission[] activeSubmissions = getActiveProjectSubmissions(uploadManager, projectId, Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION);

            for (Submission s : activeSubmissions) {
                if (!earlyAppealResourceIds.contains(s.getUpload().getOwner())) {
                    return false;
                }
            }

            return true;
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when retrieving resource", e);
        } catch (SearchBuilderConfigurationException e) {
            throw new PhaseHandlingException("Problem with search builder configuration", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        }
    }

    /**
     * <p>
     * Checks if all direct parent projects for specified project are completed or not. The check is performed only
     * for those parent projects which are linked with links which have <code>allow_overlap</code> flag set to
     * <code>false</code>.
     * </p>
     * @param projectId
     *            a <code>long</code> providing the ID of a project to check the
     *            completeness of parent projects for.
     * @param managerHelper
     *            a <code>ManagerHelper</code> to be used for getting the links to
     *            parent projects.
     * @return <code>true</code> if all parent projects for specified project are
     *         completed or there are no parent projects at all; <code>false</code> otherwise.
     * @throws com.topcoder.management.project.PersistenceException
     *             if an unexpected error occurs while accessing the persistent data
     *             store.
     * @throws com.topcoder.management.phase.PhaseManagementException
     *             if an error occurs while reading phases data.
     * @since 1.3
     */
    static boolean areParentProjectsCompleted(long projectId, ManagerHelper managerHelper)
        throws com.topcoder.management.project.PersistenceException,
        com.topcoder.management.phase.PhaseManagementException {
        ProjectLink[] links = managerHelper.getProjectLinkManager().getDestProjectLinks(projectId);

        for (ProjectLink link : links) {
            if (!link.getType().isAllowOverlap()) {
                com.topcoder.management.project.Project parentProject = link.getDestProject();

                if (!parentProject.getProjectStatus().getName().equals(Constants.PROJECT_STATUS_COMPLETED)) {
                    // if not active
                    if (parentProject.getProjectStatus().getId() != 1) {
                        return false;
                    }

                    Project phasesProject = managerHelper.getPhaseManager().getPhases(parentProject.getId());

                    // Check if all phases are closed.
                    Phase[] phases = phasesProject.getAllPhases();
                    for (Phase phase : phases) {
                        if (!phase.getPhaseStatus().getName().equals(Constants.PHASE_STATUS_CLOSED)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * <p>
     * Gets the <code>Post-Mortem</code> phase for specified project.
     * </p>
     * @param project
     *            a <code>Project</code> to get the post-mortem phase for.
     * @return a <code>Phase</code> providing details for <code>Post-Mortem</code> phase for specified project or
     *         <code>null</code> if such phase does not
     *         exist.
     * @since 1.3
     */
    static Phase getPostMortemPhase(Project project) {
        return getLastPhaseByType(project, Constants.PHASE_POST_MORTEM);
    }

    /**
     * <p>
     * Gets the <code>Approval</code> phase for specified project.
     * </p>
     * @param project
     *            a <code>Project</code> to get the approval phase for.
     * @return a <code>Phase</code> providing details for <code>Approval</code> phase
     *         for specified project or <code>null</code> if such phase does not exist.
     * @since 1.3
     */
    static Phase getApprovalPhase(Project project) {
        return getLastPhaseByType(project, Constants.PHASE_APPROVAL);
    }

    /**
     * <p>
     * Gets the <code>Review</code> phase for specified project.
     * </p>
     * @param project
     *            a <code>Project</code> to get the approval phase for.
     * @return a <code>Phase</code> providing details for <code>Review</code> phase
     *         for specified project or <code>null</code> if such phase does not exist.
     * @since 1.7.6
     */
    static Phase getReviewPhase(Project project) {
        return getLastPhaseByType(project, Constants.PHASE_REVIEW);
    }

    /**
     * <p>
     * Gets the last phase of specified type for specified project.
     * </p>
     * @param project
     *            a <code>Project</code> to get the phase for.
     * @param phaseTypeName
     *            a <code>String</code> providing the name of the phase type.
     * @return a <code>Phase</code> providing details for phase of specified type for
     *         specified project or <code>null</code> if such phase does not exist.
     * @since 1.3
     */
    private static Phase getLastPhaseByType(Project project, String phaseTypeName) {
        Phase[] phases = project.getAllPhases();

        for (int i = phases.length - 1; i >= 0; i--) {
            Phase phase = phases[i];

            if (phase.getPhaseType().getName().equals(phaseTypeName)) {
                return phase;
            }
        }

        return null;
    }

    /**
     * Searches the specification submission for the specified phase.
     * @param phase
     *            the specification submission phase.
     * @param managerHelper
     *            the manager helper.
     * @param log
     *            the log instance.
     * @return the specification submission or null if no record found.
     * @throws PhaseHandlingException
     *             if any error occurred when searching submission or more than one
     *             submission exist.
     * @since 1.4
     */
    static Submission getSpecificationSubmission(Phase phase, ManagerHelper managerHelper,
                                                 Log log) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_SPECIFICATION_SUBMISSION);
        
        Submission[] submissions = getPhaseSubmissions(managerHelper.getUploadManager(), phase.getId(),
            Constants.SUBMISSION_TYPE_SPECIFICATION_SUBMISSION, Constants.SUBMISSION_STATUS_ACTIVE);

        if (submissions.length > 1) {
            log.log(Level.ERROR, "Multiple specification submissions exist.");
            throw new PhaseHandlingException("Multiple specification submissions exist.");
        }

        return (submissions.length == 0) ? null : submissions[0];
    }

    /**
     * Checks whether all parent projects are completed. Note the connection is closed.
     * @param phase
     *            the phase.
     * @param managerHelper manager helper.
     * @param log the log.
     * @return true if the all parent projects are completed, false otherwise.
     * @throws PhaseHandlingException
     *             if any error occurred when checking parent projects completed.
     * @since 1.4
     */
    static boolean areParentProjectsCompleted(Phase phase,
        ManagerHelper managerHelper, Log log) throws PhaseHandlingException {
        try {
            return PhasesHelper.areParentProjectsCompleted(phase.getProject().getId(), managerHelper);
        } catch (com.topcoder.management.project.PersistenceException e) {
            log.log(Level.ERROR, new LogMessage(phase.getId(), null,
                "PersistenceException when checking parent projects completed.", e));
            throw new PhaseHandlingException("PersistenceException when checking parent projects completed.", e);
        } catch (PhaseManagementException e) {
            log.log(Level.ERROR, new LogMessage(phase.getId(), null, "Fail to check parent projects completed.", e));
            throw new PhaseHandlingException("PhaseManagementException when checking parent projects completed.", e);
        }
    }

    /**
     * Break ties by submission timestamp.
     * @param submission
     *            the submission to calculate
     * @param submissions
     *            all the submission records
     * @param placements
     *            all the ranked submission records
     * @return the submission with fixed placement
     * @throws PhaseHandlingException
     *             if an error occurs when retrieving data.
     */
    static RankedSubmission breakTies(RankedSubmission submission, Submission[] submissions,
            RankedSubmission[] placements) throws PhaseHandlingException {
        int rank = submission.getRank();
        Date timestamp1 = getSubmissionById(submissions, submission.getId()).getUpload().getCreationTimestamp();
        for (RankedSubmission sub : placements) {
            if (sub.getRank() == submission.getRank()) {
                Submission tie = getSubmissionById(submissions, sub.getId());
                Date timestamp2 = tie.getUpload().getCreationTimestamp();

                if (timestamp1 != null && timestamp2 != null && timestamp2.before(timestamp1)) {
                    ++rank;
                }
            }
        }
        return new RankedSubmission(submission, rank);
    }

    /**
     * Return suitable submission for given submissionId.
     * @param submissions
     *            the submission array
     * @param submissionId
     *            the submissionId
     * @return submission
     * @throws PhaseHandlingException
     *             if no submission found
     */
    static Submission getSubmissionById(Submission[] submissions, long submissionId) throws PhaseHandlingException {
        for (Submission sub : submissions) {
            if (sub.getId() == submissionId) {
                return sub;
            }
        }
        throw new PhaseHandlingException("submissions not found for submissionId: " + submissionId);
    }

    /**
     * Populate the prize for the submissions.
     *
     * @param project the project which the submissions belong to
     * @param subs the submissions
     * @param prizeType the prize type needs to be populated
     * @param minScore the minimum score the submission can receive prize
     * @since 1.7.1
     */
    static void setSubmissionPrize(com.topcoder.management.project.Project project, Submission[] subs,
        String prizeType, double minScore) {

        List<Prize> originalPrizes = project.getPrizes();
        if (originalPrizes == null || originalPrizes.isEmpty()) {
            return;
        }

        Prize minPrize = null;
        List<Prize> prizes = new ArrayList<Prize>();
        for (Prize prize : originalPrizes) {
            if (!prizeType.equals(prize.getPrizeType().getDescription())) {
                continue;
            }

            // Repeat the prize times the value of prize.getNumberOfSubmissions()
            for (int i=0;i<prize.getNumberOfSubmissions();i++) {
                prizes.add(prize);
            }

            // Update minPrize variable.
            if (minPrize == null || minPrize.getPrizeAmount() > prize.getPrizeAmount()) {
                minPrize = prize;
            }
        }

        // Sort the prizes by placement.
        Collections.sort(prizes, new Comparator<Prize>() {
            public int compare(Prize o1, Prize o2) {
                return o1.getPlace() - o2.getPlace();
            }
        });

        // Sort the submissions by placement.
        Arrays.sort(subs, new Comparator<Submission>() {
            public int compare(Submission o1, Submission o2) {
                // Should be no placement ties at this point.
                return o1.getPlacement().compareTo(o2.getPlacement());
            }
        });

        int currentPrizeIndex = 0;
        for (Submission submission : subs) {
            // Skip the failed submissions.
            if (submission.getFinalScore() < minScore ||
                    !(submission.getSubmissionStatus().getName().equals(Constants.SUBMISSION_STATUS_ACTIVE)
                    || submission.getSubmissionStatus().getName().equals(Constants.SUBMISSION_STATUS_COMPLETED_WITHOUT_WIN))) {
                submission.setPrize(null);
                continue;
            }
            // Submissions with extra prize don't get the regular prize, just the extra one.
            if (submission.isExtra()) {
                submission.setPrize(minPrize);
            } else if (currentPrizeIndex < prizes.size()) {
                // Set the prize if there is any left.
                submission.setPrize(prizes.get(currentPrizeIndex));
                currentPrizeIndex++;
            } else {
                // This is needed in case the submission was previously assigned a prize.
                // This can happen when the review results change as a result of a reappeal.
                submission.setPrize(null);
            }
        }
    }


    /**
     * Updates the scores and placements of the submissions by aggregation the scores from the reviews.
     * Also updates the 1st place winner and the 2nd place winner in the project properties.
     *
     * @param managerHelper ManagerHelper instance.
     * @param phase Review phase.
     * @param operator The operator name.
     * @param updateInitialScore True if the "initial" scores of the submissions should be updated.
     * @param updateFinalResults True if the "final" scores and placements of the submissions should be updated.
     * @return Array of all updated submissions.
     * @throws PhaseHandlingException if any error occurs.
     * @since 1.7.1
     */
    static Submission[] updateSubmissionsResults(ManagerHelper managerHelper, Phase phase, String operator,
        boolean updateInitialScore, boolean updateFinalResults) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");

        boolean isCheckpoint = false, isIterative = false;
        if (phase.getPhaseType().getName().equals(Constants.PHASE_ITERATIVE_REVIEW)) {
            isIterative = true;
        } else if (phase.getPhaseType().getName().equals(Constants.PHASE_CHECKPOINT_REVIEW)) {
            isCheckpoint = true;
        } else if (!phase.getPhaseType().getName().equals(Constants.PHASE_REVIEW)) {
            throw new PhaseNotSupportedException("Phase type must be either Review, Checkpoint Review or Iterative Review");
        }

        long projectId = phase.getProject().getId(); 
        Submission[] subs;
        try {
            UploadManager uploadManager = managerHelper.getUploadManager();
            com.topcoder.management.project.Project project =
                managerHelper.getProjectManager().getProject(projectId);
            boolean isStudioProject = PhasesHelper.isStudio(project);

            if (isIterative) {
                // For Iterative Review just get the next submission (i.e. active submission with earliest timestamp).
                subs = new Submission[] { getEarliestActiveSubmission(uploadManager, projectId) };
            } else {
                // Search all reviewed submissions for current project.
                // We also consider submissions with the failed review status because this method is also called directly
                // from within the Online Review application when a manager edits a review scorecard.
                subs = getProjectSubmissions(uploadManager, phase.getProject().getId(),
                        isCheckpoint ? Constants.SUBMISSION_TYPE_CHECKPOINT_SUBMISSION : Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION,
                        new String[]{
                                Constants.SUBMISSION_STATUS_ACTIVE,
                                Constants.SUBMISSION_STATUS_FAILED_REVIEW,
                                Constants.SUBMISSION_STATUS_FAILED_CHECKPOINT_REVIEW,
                                Constants.SUBMISSION_STATUS_COMPLETED_WITHOUT_WIN});
            }

            // Search the reviewIds.
            Resource[] reviewers = searchProjectResourcesForRoleNames(managerHelper,
                isCheckpoint ? new String[] {Constants.ROLE_CHECKPOINT_REVIEWER} :
                (isIterative ? new String[] {Constants.ROLE_ITERATIVE_REVIEWER} : REVIEWER_ROLE_NAMES), projectId);

            // Search all review scorecards for the current phase.
            Review[] reviews = searchReviewsForResources(managerHelper, reviewers, phase.getId(), null);

            // Sort all reviews by the reviewer resource ID
            Arrays.sort(reviews, new Comparator<Review>() {
                public int compare(Review r1, Review r2) {
                    return (int)(r1.getAuthor()-r2.getAuthor());
                }
            });

            if (reviews.length == 0) {
                throw new PhaseHandlingException("No reviews found for phase: " + phase.getId());
            }

            // Get minimum passing review score.
            float minScore = getScorecardMinimumScore(managerHelper.getScorecardManager(), reviews[0]);

            // create array to hold scores from all reviewers for all submissions.
            com.topcoder.management.review.scoreaggregator.Submission[] submissionScores =
                new com.topcoder.management.review.scoreaggregator.Submission[subs.length];

            // for each submission, populate scores array to use with review score aggregator.
            for (int iSub = 0; iSub < subs.length; iSub++) {
                Submission submission = subs[iSub];
                long subId = submission.getId();
                int numberOfReviews = 0;
                List<Double> scoresList = new ArrayList<Double>();

                // Match the submission with its reviews.
                for (Review review : reviews) {
                    if (subId == review.getSubmission()) {
                        numberOfReviews++;
                        scoresList.add(review.getScore());
                    }
                }

                // if number of reviews does not match number of reviewers throw exception.
                if (numberOfReviews != reviewers.length) {
                    throw new PhaseHandlingException("Number of reviews does not match number of reviewers");
                }

                double[] scores = new double[scoresList.size()];
                for (int iScore = 0; iScore < scores.length; iScore++) {
                    scores[iScore] = scoresList.get(iScore);
                }

                submissionScores[iSub] = new com.topcoder.management.review.scoreaggregator.Submission(subId, scores);
            }

            // now calculate the aggregated scores and placements.
            ReviewScoreAggregator scoreAggregator = managerHelper.getScorecardAggregator();

            // this will hold as many elements as submissions.
            AggregatedSubmission[] aggregations = scoreAggregator.aggregateScores(submissionScores);
            RankedSubmission[] placements = scoreAggregator.calcPlacements(aggregations);

            // status objects.
            SubmissionStatus activeStatus = LookupHelper.getSubmissionStatus(uploadManager, Constants.SUBMISSION_STATUS_ACTIVE);
            SubmissionStatus failedStatus = LookupHelper.getSubmissionStatus(uploadManager, Constants.SUBMISSION_STATUS_FAILED_REVIEW);
            SubmissionStatus failedCheckpointStatus = LookupHelper.getSubmissionStatus(uploadManager, Constants.SUBMISSION_STATUS_FAILED_CHECKPOINT_REVIEW);
            SubmissionStatus noWinStatus = LookupHelper.getSubmissionStatus(uploadManager, Constants.SUBMISSION_STATUS_COMPLETED_WITHOUT_WIN);

            Resource winningSubmitter = null, runnerUpSubmitter = null;

            // again iterate over submissions to set the initial score and placement.
            for (RankedSubmission rankedSubmission : placements) {
                rankedSubmission = breakTies(rankedSubmission, subs, placements);
                Submission submission = getSubmissionById(subs, rankedSubmission.getId());
                double aggScore = rankedSubmission.getAggregatedScore();
                int placement = rankedSubmission.getRank();

                if (updateInitialScore) {
                    submission.setInitialScore(aggScore);
                }

                if (updateFinalResults) {
                    submission.setFinalScore(aggScore);
                    submission.setPlacement((long)placement);

                    if (aggScore >= minScore) {
                        long submitterId = submission.getUpload().getOwner();
                        Resource submitter = managerHelper.getResourceManager().getResource(submitterId);

                        // cache winning submitter.
                        if (placement == 1) {
                            winningSubmitter = submitter;
                            submission.setSubmissionStatus(activeStatus);
                        } else {
                            // cache runner up submitter.
                            if (placement == 2) {
                                runnerUpSubmitter = submitter;
                            }
                            submission.setSubmissionStatus(isCheckpoint ? activeStatus : noWinStatus);
                        }
                    } else {
                        if (isStudioProject) {
                            submission.setSubmissionStatus(noWinStatus);
                        } else {
                            submission.setSubmissionStatus(isCheckpoint ? failedCheckpointStatus : failedStatus);
                        }
                    }
                }

            } // loop end

            // set the contest prize.
            if (updateFinalResults) {
                setSubmissionPrize(project, subs, isCheckpoint ? "Checkpoint Prize" : "Contest Prize", minScore);
            }

            // update 1st place winner and 2nd place winner in the project properties.
            if (updateFinalResults && !isCheckpoint) {
                // cannot be the case where there is a runner up but no winner.
                if (runnerUpSubmitter != null && winningSubmitter == null) {
                    throw new PhaseHandlingException("Runner up present, but no winner for project with id:" +
                        phase.getProject().getId());
                }

                // if there is a winner.
                if (winningSubmitter != null) {
                    // Set project properties to store the winner and the runner up.

                    Object winnerExtId = winningSubmitter.getUserId().toString();
                    project.setProperty("Winner External Reference ID", winnerExtId);

                    // if there is a runner up.
                    if (runnerUpSubmitter != null) {
                        Object runnerExtId = runnerUpSubmitter.getUserId().toString();
                        project.setProperty("Runner-up External Reference ID", runnerExtId);
                    }

                    // update the project.
                    managerHelper.getProjectManager().updateProject(project, "Update the winner and runner up.", operator);
                }
            }

            // update the submissions.
            for (Submission sub : subs) {
                uploadManager.updateSubmission(sub, operator);
            }
        }  catch (com.topcoder.management.project.PersistenceException e) {
            throw new PhaseHandlingException("Problem with project persistence", e);
        } catch (InconsistentDataException e) {
            throw new PhaseHandlingException("Problem when aggregating scores", e);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Problem with upload persistence", e);
        } catch (ValidationException e) {
            throw new PhaseHandlingException("Problem with project validation", e);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem with resource persistence", e);
        }
    
        return subs;
    }
    
    /**
     * Checks whether passed project is studio project.
     * @param project to check.
     * @return true if passed studio project, false otherwise.
     */
    static boolean isStudio(com.topcoder.management.project.Project project) {
        return project.getProjectCategory().getProjectType().getName().equals(Constants.PROJECT_TYPE_STUDIO);
    }

    /**
     * Checks whether the review type is PEER.
     * @param project to check.
     * @return true if the review is of the PEER type, false otherwise.
     */
    static boolean isPeerReview(com.topcoder.management.project.Project project) {
        Object reviewType = project.getProperty("Review Type");
        return reviewType != null && Constants.REVIEW_TYPE_PEER.equals(reviewType.toString());
    }

    /**
     * Checks whether the review type is PEER.
     * @param helper the helper manager to use.
     * @param phase Review phase to check.
     * @return true if the review is of the PEER type, false otherwise.
     * @throws PhaseHandlingException
     *             if any error occurred when retrieving data.
     */
    static boolean isPeerReview(ManagerHelper helper, Phase phase) throws PhaseHandlingException {
        try {
            com.topcoder.management.project.Project project =
                    helper.getProjectManager().getProject(phase.getProject().getId());
            return PhasesHelper.isPeerReview(project);
        } catch (com.topcoder.management.project.PersistenceException e) {
            throw new PhaseHandlingException("Fail to retrieve the corresponding project.", e);
        }
    }
    
    /**
     * Delete submissions which have user rank more than configured number of the project.
     *  
     * @param project the project to upload submissions
     * @param helper the manager helper to use
     * @param submissionsType the submission type to use
     * @param operator the operator to use 
     * @throws PhaseHandlingException if any error occurred
     */
    static void autoScreenStudioSubmissions(com.topcoder.management.project.Project project, ManagerHelper helper,
        String submissionsType, String operator) throws PhaseHandlingException {
        try {
            Object maxSubmissions = project.getProperty(MAXIMUM_SUBMISSIONS);
            if (maxSubmissions == null || maxSubmissions.toString().trim().length() == 0 || Integer.parseInt(maxSubmissions.toString()) < 1) {
                return;
            }
            
            int maxAllowedSubs = Integer.parseInt(maxSubmissions.toString());

            Submission[] subs = getActiveProjectSubmissions(helper.getUploadManager(), project.getId(), submissionsType);

            UploadStatus deletedUploadStatus = LookupHelper.getUploadStatus(helper.getUploadManager(), Constants.UPLOAD_STATUS_DELETED);
            SubmissionStatus deletedSubmissionStatus = LookupHelper.getSubmissionStatus(helper.getUploadManager(), Constants.SUBMISSION_STATUS_DELETED);
            
            for (Submission s : subs) {
                if (s.getUserRank() > maxAllowedSubs) {
                    s.setSubmissionStatus(deletedSubmissionStatus);
                    s.getUpload().setUploadStatus(deletedUploadStatus);
                    helper.getUploadManager().updateUpload(s.getUpload(), operator);
                    helper.getUploadManager().updateSubmission(s, operator);
                }
            }
        } catch (NumberFormatException e) {
            throw new PhaseHandlingException("Error when reading maximum submissions property", e);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Error in submission persistence", e);
        }
    }
    
}
