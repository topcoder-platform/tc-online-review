/*
 * Copyright (C) 2006-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.util.MessageResources;

import com.cronos.onlinereview.autoscreening.management.ScreeningManager;
import com.cronos.onlinereview.dataaccess.ResourceDataAccess;
import com.cronos.onlinereview.deliverables.AggregationDeliverableChecker;
import com.cronos.onlinereview.deliverables.AggregationReviewDeliverableChecker;
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
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.external.impl.DBUserRetrieval;
import com.cronos.onlinereview.model.ClientProject;
import com.cronos.onlinereview.phases.AutoPaymentUtil;
import com.cronos.onlinereview.phases.PRHelper;
import com.topcoder.date.workdays.DefaultWorkdaysFactory;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.DeliverableChecker;
import com.topcoder.management.deliverable.DeliverableManager;
import com.topcoder.management.deliverable.PersistenceDeliverableManager;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.SubmissionType;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;
import com.topcoder.management.deliverable.persistence.DeliverablePersistence;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.persistence.sql.SqlDeliverablePersistence;
import com.topcoder.management.deliverable.search.DeliverableFilterBuilder;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhasePersistence;
import com.topcoder.management.phase.db.InformixPhasePersistence;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.project.link.ProjectLinkManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.DefaultReviewManager;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregator;
import com.topcoder.management.review.scoreaggregator.ReviewScoreAggregatorConfigException;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.template.DefaultPhaseTemplate;
import com.topcoder.project.phases.template.PhaseTemplate;
import com.topcoder.project.phases.template.PhaseTemplatePersistence;
import com.topcoder.project.phases.template.StartDateGenerator;
import com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence;
import com.topcoder.project.phases.template.startdategenerator.RelativeWeekTimeStartDateGenerator;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.servlet.request.DisallowedDirectoryException;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.LocalFileUpload;
import com.topcoder.shared.util.ApplicationServer;
import com.topcoder.shared.util.TCContext;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.errorhandling.BaseRuntimeException;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;
import com.topcoder.web.ejb.forums.Forums;
import com.topcoder.web.ejb.forums.ForumsHome;

/**
 * <p>
 * This class contains handy helper-methods that perform frequently needed operations.
 * </p>
 * <p>
 * Change note for 1.1: add method to create <code>ProjectLinkManager</code>. This is for
 * "OR Project Linking Assembly".
 * </p>
 *
 * <p>
 * Version 1.2 (Competition Registration Eligibility v1.0) Change notes:
 *   <ol>
 *     <li>Added contest eligibility validation to <code>checkForCorrectProjectId</code> method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.3 (Online Review Project Management Console Assembly v1.0) Change notes:
 *   <ol>
 *     <li>Added {@link #findPhaseByTypeName(com.topcoder.project.phases.Phase[], String)} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.4 (Contest Dependency Automation Assembly v1.0) Change notes:
 *   <ol>
 *     <li>Added <code>adjustDependentProjects(com.topcoder.project.phases.Project,
 *     com.topcoder.management.phase.PhaseManager, ContestDependencyAutomation, String)</code>
 *     and {@link #recalculateScheduledDates(com.topcoder.project.phases.Phase[])} methods.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.5 (Online Review End Of Project Analysis Assembly v1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #getResourcesForPhase(Resource[], Phase)} method to properly map resource to Post-Mortem and
 *     Approval phases.</li>
 *     <li>Updated {@link #createDeliverableManager(HttpServletRequest)} method to bind deliverbale checker for
 *     <code>Post-Mortem</code> phase.</li>
 *     <li>Added {@link #getApprovalPhaseReviews(Review[], Phase)} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.6 (Online Review Performance Refactoring 1.0) Change notes:
 *   <ol>
 *     <li>Added {@link #searchUserResources(long, ProjectStatus, ResourceManager)} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.7 (Members Post-Mortem Reviews Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #getApprovalPhaseReviews(Review[], Phase)} method to map <code>Approval</code> reviews to
 *     phase based on review and phase timestamps.</p>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.8 (Impersonation Login Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added {@link #arePhaseDependenciesMet(Phase, boolean)}, {@link #isPhaseOpen(PhaseStatus)},
 *     {@link #isPhaseClosed(PhaseStatus)} methods and {@link #PHASE_STATUS_OPEN} and {@link #PHASE_STATUS_CLOSED}
 *     constants.</li>
 *     <li>Added {@link #getMyPayments(Resource[], HttpServletRequest)} method.</li>
 *     <li>Added {@link #getMyPaymentStatuses(Resource[])} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.9 (Specification Review Part 1 Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Removed dependency on <code>ContestDependencyAutomation</code> class.</li>
 *     <li>Updated {@link #createDeliverableManager(HttpServletRequest)} method to set the checkers for
 *     <code>Specification Submission</code> and <code>Specification Review</code> deliverables.</li>
 *     <li>Added {@link #findSubmissionTypeByName(SubmissionType[], String)} method.</li>
 *     <li>Added {@link #getSpecificationSubmissions(long, UploadManager)} method.</li>
 *     <li>Added {@link #getActiveSpecificationSubmission(long, long, UploadManager)} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.10 (Online Review Late Deliverables Search Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added {@link #createPhasePersistence(HttpServletRequest)} method.</li>
 *     <li>Added {@link #createLateDeliverableManager(HttpServletRequest)} method.</li>
 *     <li>Added {@link #getDeliverableIdToNameMap()} method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.11 (Content Creation Contest Online Review & TC Site Integration Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated {@link #isProjectResultCategory(long)} method.</li>
 *   </ol>
 * </p>

 * @author George1, real_vg, pulky, isv, FireIce
 * @version 1.11
 * @since 1.0
 */
public class ActionsHelper {

    /**
     * The logger instance.
     */
    private static final Log log = LogFactory.getLog(ActionsHelper.class.getName());

    /**
     * This member variable is a string constant that defines the name of the configuration
     * namespace which the parameters for database connection factory are stored under.
     */
    private static final String DB_CONNECTION_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * The connection name for retrieving billing projects.
     *
     * @since Online Review Update - Add Billing Project Drop Down v1.0
     */
    private static final String DB_CONNECTION_TIMEDS = "timeDS";

    /**
     * This member variable is a string constant that defines the name of the configuration
     * namespace which the parameters for Phases Template persistence are stored under.
     */
    private static final String PHASES_TEMPLATE_PERSISTENCE_NAMESPACE = "com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence";

    /**
     * This helper class is used for creating the managers.
     */
    private static final ManagerCreationHelper managerCreationHelper = new ManagerCreationHelper();

    /**
     * constant for software user fourm role prefix
     */
    private static final String SOFTWARE_USER_FORUM_ROLE_PREFIX = "Software_Users_";

    /**
     * constant for software moderator fourm role prefix
     */
    private static final String SOFTWARE_MODERATOR_FORUM_ROLE_PREFIX = "Software_Moderators_";


    /** constant for "Open" phase status. */
    private static final String PHASE_STATUS_OPEN = "Open";

    /** constant for "Closed" phase status. */
    private static final String PHASE_STATUS_CLOSED = "Closed";


    /**
     * This constructor is declared private to prohibit instantiation of the <code>ActionsHelper</code> class.
     */
    private ActionsHelper() {
    }

    // ------------------------------------------------------------ Hardcoded validations ---------

    /**
     * Lookup function for project categories that should have a project_result row.  These rows are used
     * for ratings, reliability, and the Digital Run.
     *
     * @param categoryId the category id to look up.
     * @return whether the provided category id should have a project_result row.
     */
    private static boolean isProjectResultCategory(long categoryId) {
        return (categoryId == 1       // Component Design
                || categoryId == 2    // Component Development
                || categoryId == 5    // Component Testing
                || categoryId == 6    // Application Specification
                || categoryId == 7    // Application Architecture
                || categoryId == 13   // Test Scenarios
                || categoryId == 26   // Test Suites
                || categoryId == 14   // Application Assembly
                || categoryId == 23   // Application Conceptualization
                || categoryId == 19   // UI Prototype
                || categoryId == 24   // RIA Build
                || categoryId == 25   // RIA Component
                || categoryId == 29   // Copilot Posting
                || categoryId == 35   // Content Creation
        );
    }

    /**
     * Lookup function for project categories that are rated.
     *
     * @param categoryId the category id to look up.
     * @return whether the provided category id is rated.
     */
    private static boolean isRatedCategory(long categoryId) {
        return (categoryId == 1       // Component Design
                || categoryId == 2    // Component Development
                || categoryId == 23   // Conceptualization
                || categoryId == 6    // Specification
                || categoryId == 7    // Architecture
                || categoryId == 14   // Assembly
                || categoryId == 13   // Test Scenarios
                || categoryId == 26   // Test Suites
                || categoryId == 19); // UI Prototypes
    }

    /**
     * The query to select worker project.
     */
    private static final String SELECT_WORKER_PROJECT = "SELECT distinct project_id FROM project_worker p, user_account u "
            + "WHERE p.start_date <= current and current <= p.end_date and p.active =1 and "
            + "p.user_account_id = u.user_account_id and u.user_name = ";

    /**
     * The query string used to select projects.
     */
    private static final String SELECT_MANAGER_PROJECT = "SELECT distinct project_id FROM project_manager p, user_account u "
           + "WHERE p.user_account_id = u.user_account_id and p.active = 1 and  u.user_name = ";

    /**
     * The query string used to select projects.
     *
     * Updated for Cockpit Release Assembly for Receipts
     *     - now fetching client name too.
     *
     * Updated for Version 1.1.1 - added fetch for is_manual_prize_setting property too.
     */
    private static final String SELECT_PROJECT     = "select p.project_id, p.name "
              + " from project as p left join client_project as cp on p.project_id = cp.project_id left join client c "
              + "            on c.client_id = cp.client_id and (c.is_deleted = 0 or c.is_deleted is null) "
              + " where p.start_date <= current and current <= p.end_date ";

    // ------------------------------------------------------------ Validator type of methods -----

    /**
     * This static method validates that parameter specified by <code>param</code> parameter is
     * not <code>null</code>, and throws an exception if validation fails.
     *
     * @param param
     *            a parameter to validate for non-null value.
     * @param paramName
     *            a name of the parameter that is being validated.
     * @throws IllegalArgumentException
     *             if parameter <code>param</code> is <code>null</code>.
     */
    public static void validateParameterNotNull(Object param, String paramName) throws IllegalArgumentException {
        if (param == null) {
            throw new IllegalArgumentException("Paramter '" + paramName + "' must not be null.");
        }
    }

    /**
     * This static method validates that parameter specified by <code>str</code> parameter is not
     * <code>null</code> and not an empty string, and throws an exception if validation fails.
     *
     * @param str
     *            a string parameter to validate.
     * @param paramName
     *            a name of the parameter that is being validated.
     * @throws IllegalArgumentException
     *             if parameter <code>str</code> is <code>null</code> or empty string.
     */
    public static void validateParameterStringNotEmpty(String str, String paramName) throws IllegalArgumentException {
        validateParameterNotNull(str, paramName);
        if (str.trim().length() == 0) {
            throw new IllegalArgumentException("Paramter '" + paramName + "' must not be empty string.");
        }
    }

    /**
     * This static method verifies that parameter of type <code>long</code> specified by
     * <code>value</code> parameter is not negative or zero value, and throws an exception if
     * validation fails.
     *
     * @param value
     *            a <code>long</code> value to validate.
     * @param paramName
     *            a name of the parameter that is being validated.
     * @throws IllegalArgumentException
     *             if parameter <code>value</code> is zero or negative.
     */
    public static void validateParameterPositive(long value, String paramName) throws IllegalArgumentException {
        if (value <= 0) {
            throw new IllegalArgumentException("Parameter '" + paramName + "' must not be negative or zero." +
                    " Current value of the parameters is " + value + ".");
        }
    }

    /**
     * This static method verifies that parameter of type <code>int</code> specified by
     * <code>value</code> parameter falls in the range of allowed values specified by
     * <code>minValue</code> and <code>maxValue</code> parameters. The reange's boundaries are
     * inclusive. This method itself throws an exception if the value of <code>minValue</code> is
     * greater than the value of <code>maxValue</code> parameter.
     *
     * @param value
     *            an <code>int</code> value to validate.
     * @param paramName
     *            a name of the parameter that is being validated.
     * @param minValue
     *            the lower boundary of the range. The boundary is inclusive.
     * @param maxValue
     *            the upper boundary of the range. The boundary is inclusive.
     * @throws IllegalArgumentException
     *             if parameter <code>value</code> is less than <code>minValue</code> or greater
     *             than <code>maxValue</code>, or if <code>minValue</code> is greater than
     *             <code>maxValue</code>.
     */
    public static void validateParameterInRange(int value, String paramName, int minValue, int maxValue)
        throws IllegalArgumentException {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("Parameter 'minValue' is greater than 'maxValue'.");
        }
        if (value < minValue || value > maxValue) {
            throw new IllegalArgumentException("Parameter '" + paramName +
                    "' does not fall into the specifed range of [" + minValue + "; " + maxValue + "].");
        }
    }

    /**
     * This static method verifies that the request specified by <code>request</code> parameter
     * contains non-<code>null</code> attribute and returns the object stored under that
     * attribute. This method throws an exception is validation fails.
     *
     * @return a reference to object retrieved from the request's scope.
     * @param request
     *            an <code>HttpServletRequest</code> object which attribute should be retrieved
     *            and validated from.
     * @param attributeName
     *            a name of the attribute to retrieve from the request's scope.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or <code>attributeName</code>
     *             parameter is empty string, or if the request does not contain attribute with the
     *             specified name or attribute's value is <code>null</code>
     */
    public static Object validateAttributeNotNull(HttpServletRequest request, String attributeName) {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterStringNotEmpty(attributeName, "attributeName");

        Object obj = request.getAttribute(attributeName);
        if (obj == null) {
            throw new IllegalArgumentException("There is no attribute '" + attributeName +
                    "' stored in the request scope, or the attrubute stored is null.");
        }
        return obj;
    }


    // --------------------------------------------------------------- Finder type of methods -----

    /**
     * This static method searches for the scorecard type with the specified name in a provided
     * array of scorecard types.
     *
     * @return found scorecard type, or <code>null</code> if a type with the specified name has
     *         not been found in the provided array of scorecard types.
     * @param scorecardTypes
     *            an array of scorecard types to search for wanted scorecard type among.
     * @param typeName
     *            the name of the needed scorecard type.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or <code>typeName</code>
     *             parameter is empty string.
     */
    public static ScorecardType findScorecardTypeByName(ScorecardType[] scorecardTypes, String typeName) {
        // Validate parameters
        validateParameterNotNull(scorecardTypes, "scorecardTypes");
        validateParameterStringNotEmpty(typeName, "typeName");

        for (int i = 0; i < scorecardTypes.length; ++i) {
            if (scorecardTypes[i].getName().equalsIgnoreCase(typeName)) {
                return scorecardTypes[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the comment type with the specified ID in a provided array of
     * comment types.
     *
     * @return found comment type, or <code>null</code> if a type with the specified ID has not
     *         been found in the provided array of comment types.
     * @param commentTypes
     *            an array of comment types to search for wanted comment type among.
     * @param typeId
     *            the ID of the needed comment type.
     * @throws IllegalArgumentException
     *             if <code>commentTypes</code> parameter is <code>null</code>, or
     *             <code>typeId</code> parameter is zero or negative.
     */
    public static CommentType findCommentTypeById(CommentType[] commentTypes, long typeId) {
        // Validate parameters
        validateParameterNotNull(commentTypes, "commentTypes");
        validateParameterPositive(typeId, "typeId");

        for (int i = 0; i < commentTypes.length; ++i) {
            if (commentTypes[i].getId() == typeId) {
                return commentTypes[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the comment type with the specified name in a provided array
     * of comment types. The search is case-insensitive.
     *
     * @return found comment type, or <code>null</code> if a type with the specified name has not
     *         been found in the provided array of comment types.
     * @param commentTypes
     *            an array of comment types to search for wanted comment type among.
     * @param typeName
     *            the name of the needed comment type.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or <code>typeName</code>
     *             parameter is empty string.
     */
    public static CommentType findCommentTypeByName(CommentType[] commentTypes, String typeName) {
        // Validate parameters
        validateParameterNotNull(commentTypes, "commentTypes");
        validateParameterStringNotEmpty(typeName, "typeName");

        for (int i = 0; i < commentTypes.length; ++i) {
            if (commentTypes[i].getName().equalsIgnoreCase(typeName)) {
                return commentTypes[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the project category with the specified ID in a provided
     * array of project categories.
     *
     * @return found project category, or <code>null</code> if a category with the specified ID
     *         has not been found in the provided array of project categories.
     * @param projectCategories
     *            an array of project categories to search for wanted project category among.
     * @param categoryId
     *            the ID of the needed project category.
     * @throws IllegalArgumentException
     *             if <code>projectCategories</code> parameter is <code>null</code>, or
     *             <code>categoryId</code> parameter is zero or negative.
     * @see #findProjectCategoryByName(ProjectCategory[], String)
     */
    public static ProjectCategory findProjectCategoryById(ProjectCategory[] projectCategories, long categoryId) {
        // Validate parameters
        validateParameterNotNull(projectCategories, "projectCategories");
        validateParameterPositive(categoryId, "categoryId");

        for (int i = 0; i < projectCategories.length; ++i) {
            if (projectCategories[i].getId() == categoryId) {
                return projectCategories[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the project category with the specified name in a provided
     * array of project categories. The search is case-insensitive.
     *
     * @return found project category, or <code>null</code> if a category with the specified name
     *         has not been found in the provided array of project categories.
     * @param projectCategories
     *            an array of project categories to search for wanted project category among.
     * @param categoryName
     *            the name of the needed project category.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or <code>categoryName</code>
     *             parameter is empty string.
     * @see #findProjectCategoryById(ProjectCategory[], long)
     */
    public static ProjectCategory findProjectCategoryByName(ProjectCategory[] projectCategories, String categoryName) {
        // Validate parameters
        validateParameterNotNull(projectCategories, "projectCategories");
        validateParameterStringNotEmpty(categoryName, "categoryName");

        for (int i = 0; i < projectCategories.length; ++i) {
            if (projectCategories[i].getName().equalsIgnoreCase(categoryName)) {
                return projectCategories[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the project status with the specified ID in a provided array
     * of project statuses.
     *
     * @return found project status, or <code>null</code> if a status with the specified ID has
     *         not been found in the provided array of project statuses.
     * @param projectStatuses
     *            an array of project statuses to search for wanted project status among.
     * @param statusId
     *            the ID of the needed project status.
     * @throws IllegalArgumentException
     *             if <code>projectStatuses</code> parameter is <code>null</code>, or
     *             <code>statusId</code> parameter is zero or negative.
     * @see #findProjectStatusByName(ProjectStatus[], String)
     */
    public static ProjectStatus findProjectStatusById(ProjectStatus[] projectStatuses, long statusId) {
        // Validate parameters
        validateParameterNotNull(projectStatuses, "projectStatuses");
        validateParameterPositive(statusId, "statusId");

        for (int i = 0; i < projectStatuses.length; ++i) {
            if (projectStatuses[i].getId() == statusId) {
                return projectStatuses[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the project status with the specified name in a provided
     * array of project statuses. The search is case-insensitive.
     *
     * @return found project status, or <code>null</code> if a status with the specified name has
     *         not been found in the provided array of project statuses.
     * @param projectStatuses
     *            an array of project statuses to search for wanted project status among.
     * @param statusName
     *            the name of the needed project status.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or <code>statusName</code>
     *             parameter is empty string.
     * @see #findProjectStatusById(ProjectStatus[], long)
     */
    public static ProjectStatus findProjectStatusByName(ProjectStatus[] projectStatuses, String statusName) {
        // Validate parameters
        validateParameterNotNull(projectStatuses, "projectStatuses");
        validateParameterStringNotEmpty(statusName, "statusName");

        for (int i = 0; i < projectStatuses.length; ++i) {
            if (projectStatuses[i].getName().equalsIgnoreCase(statusName)) {
                return projectStatuses[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the resource role with the specified ID in a provided array
     * of resource roles.
     *
     * @return found resource role, or <code>null</code> if a role with the specified ID has not
     *         been found in the provided array of resource roles.
     * @param resourceRoles
     *            an array of resource roles to search for wanted resource role among.
     * @param roleId
     *            the ID of the needed resource role.
     * @throws IllegalArgumentException
     *             if <code>resourceRoles</code> parameter is <code>null</code>, or
     *             <code>roleId</code> parameter is zero or negative.
     */
    public static ResourceRole findResourceRoleById(ResourceRole[] resourceRoles, long roleId) {
        // Validate parameters
        validateParameterNotNull(resourceRoles, "resourceRoles");
        validateParameterPositive(roleId, "roleId");

        for (int i = 0; i < resourceRoles.length; ++i) {
            if (resourceRoles[i].getId() == roleId) {
                return resourceRoles[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the resource role with the specified name in a provided array
     * of resource roles.
     *
     * @return foundresource role, or <code>null</code> if a role with the specified name has not
     *         been found in the provided array of resource roles.
     * @param resourceRoles
     *            an array of resource roles to search for wanted resource role among.
     * @param roleName
     *            the name of the needed resource role.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or if <code>roleName</code>
     *             parameter is empty string.
     */
    public static ResourceRole findResourceRoleByName(ResourceRole[] resourceRoles, String roleName) {
        // Validate parameters
        validateParameterNotNull(resourceRoles, "resourceRoles");
        validateParameterStringNotEmpty(roleName, "roleName");

        for (int i = 0; i < resourceRoles.length; ++i) {
            if (resourceRoles[i].getName().equalsIgnoreCase(roleName)) {
                return resourceRoles[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the project phase with the specified ID in a provided array
     * of project phases.
     *
     * @return found project phase, or <code>null</code> if a phase with the specified ID has not
     *         been found in the provided array of project phases.
     * @param phases
     *            an array of project phases to search for wanted project phase among.
     * @param phaseId
     *            the ID of the needed project phase.
     * @throws IllegalArgumentException
     *             if <code>phases</code> parameter is <code>null</code>, or
     *             <code>phaseId</code> parameter is zero or negative.
     */
    public static Phase findPhaseById(Phase[] phases, long phaseId) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");
        validateParameterPositive(phaseId, "phaseId");

        for (int i = 0; i < phases.length; ++i) {
            if (phases[i].getId() == phaseId) {
                return phases[i];
            }
        }
        return null;
    }

    /**
     * <p>This static method searches for the project phase with the specified phase type name in a provided array of
     * project phases.</p>
     *
     * @param phases an array of project phases to search for wanted project phase among.
     * @param phaseTypeName the name of type of the needed project phase.
     * @return found project phase or <code>null</code> if a phase with the specified ID has not been found in the
     *         provided array of project phases.
     * @throws IllegalArgumentException if <code>phases</code> parameter is <code>null</code> or
     *         <code>phaseTypeName</code> parameter is <code>null</code> or empty.
     * @since 1.3
     */
    public static Phase findPhaseByTypeName(Phase[] phases, String phaseTypeName) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");
        validateParameterStringNotEmpty(phaseTypeName, "phaseTypeName");

        for (int i = 0; i < phases.length; ++i) {
            if (phases[i].getPhaseType().getName().equalsIgnoreCase(phaseTypeName)) {
                return phases[i];
            }
        }
        return null;
    }



    /**
     * This static method searches for the project phase with
     * the specified phase type ID in a provided array of project phases.
     *
     * @return found project phase, or <code>null</code> if a phase with the specified
     *         phase type ID has not been found in the provided array of project phases.
     * @param phases
     *            an array of project phases to search for wanted project phase among.
     * @param phaseTypeId
     *            the phase type ID of the needed project phase.
     * @throws IllegalArgumentException
     *             if <code>phases</code> parameter is <code>null</code>, or
     *             <code>phaseTypeId</code> parameter is zero or negative.
     */
    public static Phase findPhaseByPhaseTypeId(Phase[] phases, long phaseTypeId) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");
        validateParameterPositive(phaseTypeId, "phaseTypeId");

        for (int i = 0; i < phases.length; ++i) {
            if (phases[i].getPhaseType().getId() == phaseTypeId) {
                return phases[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the phase type with the specified ID in a provided array of
     * phase types.
     *
     * @return found phase type, or <code>null</code> if a type with the specified ID has not been
     *         found in the provided array of phase types.
     * @param phaseTypes
     *            an array of phase types to search for wanted phase type among.
     * @param phaseTypeId
     *            the ID of the needed phase type.
     * @throws IllegalArgumentException
     *             if <code>phaseTypes</code> parameter is <code>null</code>, or
     *             <code>phaseTypeId</code> parameter is zero or negative.
     */
    public static PhaseType findPhaseTypeById(PhaseType[] phaseTypes, long phaseTypeId) {
        // Validate parameters
        validateParameterNotNull(phaseTypes, "phaseTypes");
        validateParameterPositive(phaseTypeId, "phaseTypeId");

        for (int i = 0; i < phaseTypes.length; ++i) {
            if (phaseTypes[i].getId() == phaseTypeId) {
                return phaseTypes[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the phase type with the specified name in a provided array of
     * phase types. The search is case-insensitive.
     *
     * @return found phase type, or <code>null</code> if a type with the specified name has not been
     *         found in the provided array of phase types.
     * @param phaseTypes
     *            an array of phase types to search for wanted phase type among.
     * @param phaseTypeName
     *            the name of the needed phase type.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or <code>phaseTypeName</code>
     *             parameter is empty string.
     */
    public static PhaseType findPhaseTypeByName(PhaseType[] phaseTypes, String phaseTypeName) {
        // Validate parameters
        validateParameterNotNull(phaseTypes, "phaseTypes");
        validateParameterStringNotEmpty(phaseTypeName, "phaseTypeName");

        for (int i = 0; i < phaseTypes.length; ++i) {
            if (phaseTypes[i].getName().equalsIgnoreCase(phaseTypeName)) {
                return phaseTypes[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the phase status with the specified ID in a provided array of
     * phase statuses.
     *
     * @return found phase status, or <code>null</code> if a status with the specified ID has not
     *         been found in the provided array of phase statuses.
     * @param phaseStatuses
     *            an array of phase statuses to search for wanted phase status among.
     * @param phaseStatusId
     *            the ID of the needed phase status.
     * @throws IllegalArgumentException
     *             if <code>phaseStatuses</code> parameter is <code>null</code>, or
     *             <code>phaseStatusId</code> parameter is zero or negative.
     */
    public static PhaseStatus findPhaseStatusById(PhaseStatus[] phaseStatuses, long phaseStatusId) {
        // Validate parameters
        validateParameterNotNull(phaseStatuses, "phaseStatuses");
        validateParameterPositive(phaseStatusId, "phaseStatusId");

        for (int i = 0; i < phaseStatuses.length; ++i) {
            if (phaseStatuses[i].getId() == phaseStatusId) {
                return phaseStatuses[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the phase status with the specified name in a provided array
     * of phase statuses. The search is case-insensitive.
     *
     * @return found phase status, or <code>null</code> if a status with the specified name has
     *         not been found in the provided array of phase statuses.
     * @param phaseStatuses
     *            an array of phase statuses to search for wanted phase status among.
     * @param phaseStatusName
     *            the name of the needed phase status.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or
     *             <code>phaseStatusName</code> parameter is empty string.
     */
    public static PhaseStatus findPhaseStatusByName(PhaseStatus[] phaseStatuses, String phaseStatusName) {
        // Validate parameters
        validateParameterNotNull(phaseStatuses, "phaseStatuses");
        validateParameterStringNotEmpty(phaseStatusName, "phaseStatusName");

        for (int i = 0; i < phaseStatuses.length; ++i) {
            if (phaseStatuses[i].getName().equalsIgnoreCase(phaseStatusName)) {
                return phaseStatuses[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the submission status with the specified name in a provided
     * array of submission statuses. The search is case-insensitive.
     *
     * @return found submission status, or <code>null</code> if a status with the specified name
     *         has not been found in the provided array of submission statuses.
     * @param submissionStatuses
     *            an array of submission statuses to search for wanted submission status among.
     * @param submissionStatusName
     *            the name of the needed submission status.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or
     *             <code>submissionStatusName</code> parameter is empty string.
     */
    public static SubmissionStatus findSubmissionStatusByName(
            SubmissionStatus[] submissionStatuses, String submissionStatusName) {
        // Validate parameters
        validateParameterNotNull(submissionStatuses, "submissionStatuses");
        validateParameterStringNotEmpty(submissionStatusName, "submissionStatusName");

        for (int i = 0; i < submissionStatuses.length; ++i) {
            if (submissionStatuses[i].getName().equalsIgnoreCase(submissionStatusName)) {
                return submissionStatuses[i];
            }
        }
        return null;
    }

    /**
     * <p>This static method searches for the submission type with the specified name in a provided array of submission
     * types. The search is case-insensitive.</p>
     *
     * @param submissionTypes an array of submission types to search for wanted submission type among.
     * @param submissionTypeName the name of the needed submission type.
     * @return found submission type, or <code>null</code> if a type with the specified name has not been found in
     *         the provided array of submission types.
     * @throws IllegalArgumentException if any of the parameters are <code>null</code> or
     *         <code>submissionTypeName</code> parameter is empty string.
     * @since 1.9
     */
    public static SubmissionType findSubmissionTypeByName(SubmissionType[] submissionTypes, String submissionTypeName) {
        validateParameterNotNull(submissionTypes, "submissionTypes");
        validateParameterStringNotEmpty(submissionTypeName, "submissionTypeName");

        for (SubmissionType submissionType :  submissionTypes) {
            if (submissionType.getName().equalsIgnoreCase(submissionTypeName)) {
                return submissionType;
            }
        }
        return null;
    }

    /**
     * This static method searches for the upload status with the specified name in a provided array
     * of upload statuses. The search is case-insensitive.
     *
     * @return found upload status, or <code>null</code> if a status with the specified name has
     *         not been found in the provided array of upload statuses.
     * @param uploadStatuses
     *            an array of upload statuses to search for wanted upload status among.
     * @param uploadStatusName
     *            the name of the needed upload status.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or
     *             <code>uploadStatusName</code> parameter is empty string.
     */
    public static UploadStatus findUploadStatusByName(UploadStatus[] uploadStatuses, String uploadStatusName) {
        // Validate parameters
        validateParameterNotNull(uploadStatuses, "uploadStatuses");
        validateParameterStringNotEmpty(uploadStatusName, "uploadStatusName");

        for (int i = 0; i < uploadStatuses.length; ++i) {
            if (uploadStatuses[i].getName().equalsIgnoreCase(uploadStatusName)) {
                return uploadStatuses[i];
            }
        }
        return null;
    }

    /**
     * This static method searches for the upload type with the specified name in a provided array
     * of upload types. The search is case-insensitive.
     *
     * @return found upload type, or <code>null</code> if a type with the specified name has not
     *         been found in the provided array of upload types.
     * @param uploadTypes
     *            an array of upload types to search for wanted upload type among.
     * @param uploadTypeName
     *            the name of the needed upload type.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or <code>uploadTypeName</code>
     *             parameter is empty string.
     */
    public static UploadType findUploadTypeByName(UploadType[] uploadTypes, String uploadTypeName) {
        // Validate parameters
        validateParameterNotNull(uploadTypes, "uploadTypes");
        validateParameterStringNotEmpty(uploadTypeName, "uploadTypeName");

        for (int i = 0; i < uploadTypes.length; ++i) {
            if (uploadTypes[i].getName().equalsIgnoreCase(uploadTypeName)) {
                return uploadTypes[i];
            }
        }
        return null;
    }

    /**
     * This static method counts the number of questions in a specified scorecard template.
     *
     * @return a number of questions in the scorecard.
     * @param scorecardTemplate
     *            a scorecard template to count questions in.
     * @throws IllegalArgumentException
     *             if <code>scorecardTemplate</code> parameter is <code>null</code>.
     */
    public static int getScorecardQuestionsCount(Scorecard scorecardTemplate) {
        // Validate parameter
        validateParameterNotNull(scorecardTemplate, "scorecardTemplate");

        int questionCount = 0;
        // Determine the number of questions in scorecard template
        for (int i = 0; i < scorecardTemplate.getNumberOfGroups(); ++i) {
            Group group = scorecardTemplate.getGroup(i);
            for (int j = 0; j < group.getNumberOfSections(); ++j) {
                Section section = group.getSection(j);
                questionCount += section.getNumberOfQuestions();
            }
        }
        return questionCount;
    }

    /**
     * This static method counts the number of uploads that are required or optional in the whole
     * scorecard template.
     *
     * @return a number of uploads in the scorecard.
     * @param scorecardTemplate
     *            a scorecard template to count uploads in.
     * @throws IllegalArgumentException
     *             if <code>scorecardTemplate</code> parameter is <code>null</code>.
     */
    public static int getScorecardUploadsCount(Scorecard scorecardTemplate) {
        // Validate parameter
        validateParameterNotNull(scorecardTemplate, "scorecardTemplate");

        int uploadCount = 0;
        // Count the number of uploads for this scorecard template
        for (int i = 0; i < scorecardTemplate.getNumberOfGroups(); ++i) {
            Group group = scorecardTemplate.getGroup(i);
            for (int j = 0; j < group.getNumberOfSections(); ++j) {
                Section section = group.getSection(j);
                for (int k = 0; k < section.getNumberOfQuestions(); ++k){
                    if (section.getQuestion(k).isUploadDocument()) {
                        ++uploadCount;
                    }
                }
            }
        }
        return uploadCount;
    }

    /**
     * This static method clones specified action forward and appends specified string argument to
     * the path of the newly-created forward.
     *
     * @return cloned and mofied action forward.
     * @param forward
     *            an action forward to clone.
     * @param arg0
     *            a string that should be appended to the path of the newly-cloned forward. This
     *            parameter must not be <code>null</code>, but can be an empty string.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    public static ActionForward cloneForwardAndAppendToPath(ActionForward forward, String arg0) {
        // Validate parameters
        validateParameterNotNull(forward, "forward");
        validateParameterNotNull(arg0, "arg0");

        // Create new ActionForward object
        ActionForward clonedForward = new ActionForward();

        // Clone (copy) the fields
        clonedForward.setModule(forward.getModule());
        clonedForward.setName(forward.getName());
        clonedForward.setRedirect(forward.getRedirect());
        // Append string argument
        clonedForward.setPath(forward.getPath() + arg0);

        // Return the newly-created action forward
        return clonedForward;
    }

    /**
     * This static method places certain attributes into the request and returns a forward to the
     * error page.
     *
     * @return an action forward to the appropriate error page.
     * @param mapping
     *            action mapping.
     * @param messages
     *            a <code>MessageResources</code> object to load error messages from.
     * @param request
     *            the http request.
     * @param permission
     *            permission to check against, or <code>null</code> if no check is required.
     * @param reasonKey
     *            a key in Message resources which the reason of the error is stored under.
     * @param getRedirectUrlFromReferer
     *            determines whether redirect link should be obtained from Referer request header.
     *            If this parameter is <code>null</code>, no redirect is needed at all (some
     *            other error happened, not denial of access).
     * @throws BaseException
     *             if any error occurs.
     */
    public static ActionForward produceErrorReport(ActionMapping mapping, MessageResources messages,
            HttpServletRequest request, String permission, String reasonKey, Boolean getRedirectUrlFromReferer)
        throws BaseException{
        // If the user is not logged in, this is the reason
        // why they don't have permissions to do the job. Let the user login first
        if (getRedirectUrlFromReferer != null && !AuthorizationHelper.isUserLoggedIn(request)) {
            AuthorizationHelper.setLoginRedirect(request, getRedirectUrlFromReferer.booleanValue());
            return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
        }

        // Gather roles, so tabs will be displayed,
        // but only do this if roles haven't been gathered yet
        if (request.getAttribute("roles") == null) {
            AuthorizationHelper.gatherUserRoles(request);
        }

        // Place error title into request
        if (permission == null) {
            request.setAttribute("errorTitle", messages.getMessage("Error.Title.General"));
        } else {
            if ("Error.NoPermission".equalsIgnoreCase(reasonKey)){
                log.log(Level.WARN, "Authorization failures.User tried to perform "
                        + permission + " which he/she doesn't have permission.");
            }
            request.setAttribute("errorTitle", messages.getMessage("Error.Title." + permission.replaceAll(" ", "")));
        }
        // Place error message (reason) into request
        request.setAttribute("errorMessage", messages.getMessage(reasonKey));
        // Find appropriate forward and return it
        return mapping.findForward(Constants.USER_ERRROR_FORWARD_NAME);
    }

    /**
     * TODO: Document it
     *
     * @param request
     * @param errorKey
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or if <code>errorKey</code>
     *             parameter is empty string.
     */
    public static void addErrorToRequest(HttpServletRequest request, String errorKey) {
        // Validate the errorKey parameter. Other parameters will be validated in overloaded method
        validateParameterStringNotEmpty(errorKey, "errorKey");
        // Call overload to do the actual job
        addErrorToRequest(request, ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errorKey));
    }

    /**
     * TODO: Document it
     *
     * @param request
     * @param errorMessage
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    public static void addErrorToRequest(HttpServletRequest request, ActionMessage errorMessage) {
        // Call overload to do the actual job
        addErrorToRequest(request, ActionMessages.GLOBAL_MESSAGE, errorMessage);
    }

    /**
     * TODO: Document it
     *
     * @param request
     * @param messageProperty
     * @param errorKey
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or if any of the
     *             <code>messageProperty</code> or <code>errorKey</code> parameters are empty
     *             strings.
     */
    public static void addErrorToRequest(HttpServletRequest request, String messageProperty, String errorKey) {
        // Validate the errorKey parameter. Other parameters will be validated in overloaded method
        validateParameterStringNotEmpty(errorKey, "errorKey");
        // Call overload to do the actual job
        addErrorToRequest(request, messageProperty, new ActionMessage(errorKey));
    }

    /**
     * TODO: Document it
     *
     * @param request
     * @param messageProperty
     * @param errorMessage
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or if
     *             <code>messageProperty</code> parameter is empty string.
     */
    public static void addErrorToRequest(HttpServletRequest request,
            String messageProperty, ActionMessage errorMessage) {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterStringNotEmpty(messageProperty, "messageProperty");
        validateParameterNotNull(errorMessage, "errorMessage");

        // Retrieve the errors bean from the request
        ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
        // Check if the errors bean has been present in the request
        if (errors == null) {
            // If not, create it and store in the request
            errors = new ActionErrors();
            request.setAttribute(Globals.ERROR_KEY, errors);
        }

        // Add an error to the errors bean
        errors.add(messageProperty, errorMessage);
    }

    /**
     *
     * @return
     * @param request
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     */
    public static boolean isErrorsPresent(HttpServletRequest request) {
        // Validate parameter
        validateParameterNotNull(request, "request");

        return (request.getAttribute(Globals.ERROR_KEY) != null);
    }

    /**
     * This static method determines if the specifed comment is reviewer's comment. Reviewer's
     * comments are those of any of the following types: &quot;Comment&quot;, &quot;Required&quot;,
     * or &quot;Recommended&quot;.
     *
     * @return <code>true</code> if the specifed comment is reviewer's comment, <code>false</code>
     *         if it is not.
     * @param comment
     *            a comment to determine type of.
     * @throws IllegalArgumentException
     *             if <code>comment</code> parameter is <code>null</code>.
     */
    public static boolean isReviewerComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null && (commentType.equalsIgnoreCase("Comment") ||
                commentType.equalsIgnoreCase("Required") || commentType.equalsIgnoreCase("Recommended")));
    }

    /**
     * This static method determines if the specified comment is manager's comment.
     *
     * @return <code>true</code> if the specifed comment is manager's comment, <code>false</code>
     *         if it is not.
     * @param comment
     *            a comment to determine type of.
     * @throws IllegalArgumentException
     *             if <code>comment</code> parameter is <code>null</code>.
     */
    public static boolean isManagerComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null && commentType.equalsIgnoreCase("Manager Comment"));
    }

    /**
     * This static method determines if the specified comment is appeal or appeal response.
     *
     * @return <code>true</code> if the specifed comment is appeal or appeal response,
     *         <code>false</code> if it is not.
     * @param comment
     *            a comment to determine type of.
     * @throws IllegalArgumentException
     *             if <code>comment</code> parameter is <code>null</code>.
     */
    public static boolean isAppealsComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null &&
                (commentType.equalsIgnoreCase("Appeal") || commentType.equalsIgnoreCase("Appeal Response")));
    }

    /**
     * This static method determines if the specified comment is aggregator's comment.
     *
     * @return <code>true</code> if the specifed comment is aggregator's comment,
     *         <code>false</code> if it is not.
     * @param comment
     *            a comment to determine type of.
     * @throws IllegalArgumentException
     *             if <code>comment</code> parameter is <code>null</code>.
     */
    public static boolean isAggregatorComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null && commentType.equalsIgnoreCase("Aggregation Comment"));
    }

    /**
     * This static method determines if the specified comment is aggregation review comment.
     * Aggregation review comments are those that were put either by reviewer or by submitter when
     * they were doing the review of aggregation scorecard.
     *
     * @return <code>true</code> if the specifed comment is aggregation review comment,
     *         <code>false</code> if it is not.
     * @param comment
     *            a comment to determine type of.
     * @throws IllegalArgumentException
     *             if <code>comment</code> parameter is <code>null</code>.
     */
    public static boolean isAggregationReviewComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null &&
                (commentType.equalsIgnoreCase("Aggregation Review Comment") ||
                commentType.equalsIgnoreCase("Submitter Comment")));
    }

    /**
     * This static method determines if the specified comment is a final review comment.
     *
     * @return <code>true</code> if the specifed comment is final review comment,
     *         <code>false</code> if it is not.
     * @param comment
     *            a comment to determine type of.
     * @throws IllegalArgumentException
     *             if <code>comment</code> parameter is <code>null</code>.
     */
    public static boolean isFinalReviewComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null && commentType.equalsIgnoreCase("Final Review Comment"));
    }

    /**
     * This method helps gather some commonly used information about the project. When the
     * information has been gathered, this method places it into the request as a set of attributes.
     *
     * @param request
     *            the http request.
     * @param project
     *            a project to get the info for.
     * @param messages
     *            message resources.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    public static void retrieveAndStoreBasicProjectInfo(
            HttpServletRequest request, Project project, MessageResources messages) {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterNotNull(project, "project");
        validateParameterNotNull(messages, "messages");

        // Retrieve the name of the Project Category icon
        String categoryIconName = ConfigHelper.getProjectCategoryIconName(project.getProjectCategory().getName());
        // And place it into request
        request.setAttribute("categoryIconName", categoryIconName);

        String rootCatalogID = (String) project.getProperty("Root Catalog ID");
        // Retrieve Root Catalog icon's filename
        String rootCatalogIcon = ConfigHelper.getRootCatalogIconNameSm(rootCatalogID);
        // Retrieve the name of Root Catalog for this project
        String rootCatalogName = messages.getMessage(ConfigHelper.getRootCatalogAltTextKey(rootCatalogID));

        // Place the filename of the icon for Root Catalog into request
        request.setAttribute("rootCatalogIcon", rootCatalogIcon);
        // Place the name of the Root Catalog for the current project into request
        request.setAttribute("rootCatalogName", rootCatalogName);
    }

    /**
     * This method helps to retrieve the roles currently logged in user has. This method then places
     * the retrieved info into the request as attrobute named &quot;myRole&quot;.
     *
     * @param request
     *            the http request.
     * @param messages
     *            message resources.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    public static void retrieveAndStoreMyRole(HttpServletRequest request, MessageResources messages) {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterNotNull(messages, "messages");

        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");
        // Place a string that represents "my" current role(s) into the request
        request.setAttribute("myRole", ActionsHelper.determineRolesForResources(request, messages, myResources));
    }

    /**
     * TODO: Document it
     *
     * @param request
     * @param upload
     * @throws BaseException
     */
    public static void retrieveAndStoreSubmitterInfo(HttpServletRequest request, Upload upload) throws BaseException {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterNotNull(upload, "upload");

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        // Get submitter's resource
        Resource submitter = resMgr.getResource(upload.getOwner());
        populateEmailProperty(request, submitter);

        // Place submitter's user ID into the request
        request.setAttribute("submitterId", submitter.getProperty("External Reference ID"));
        // Place submitter's resource into the request
        request.setAttribute("submitterResource", submitter);
    }

    /**
     * Populate resource email resource info to resource.
     *
     * @param request the request to retrieve manager instance
     * @param resource resource instance
     * @throws BaseException if error occurs
     */
    static void populateEmailProperty(HttpServletRequest request, Resource resource) throws BaseException {
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
        ExternalUser user = null;
        String externalUserID = (String) resource.getProperty("External Reference ID");
        if (externalUserID != null) {
            user = userRetrieval.retrieveUser(Long.parseLong(externalUserID));
        }
        if (user == null) {
            log.log(Level.DEBUG, "using 'Handle' for retrieving the user for resource: " + resource.getId());
            String handle = (String) resource.getProperty("Handle");
            if (handle != null) {
                user = userRetrieval.retrieveUser(handle);
            }
        } else {
            log.log(Level.DEBUG, "using 'External Reference ID' for retrieving the user for resource: " + resource.getId());
        }
        if (user == null) {
            throw new BaseException("the resourceId: " + resource.getId() + " doesn't refer a valid user");
        }
        resource.setProperty("Email", user.getEmail());
    }

    /**
     * This static member function examines an array of supplied resources and forms a string that
     * specifies the roles based on the roles the resources in the array have. All roles in the
     * array are supposed to be assigned to the same external user, although the check of meeting
     * this condition is not perforemd by this method.
     *
     * @return a string with the role(s) the resource from the specified array have. If there are
     *         more than one role, the roles will be separated by forward slash (<code>/</code>)
     *         character.
     * @param request
     *            an <code>HttpServletRequest</code> object.
     * @param messages
     *            a message resources used to retrieve localized names of roles.
     * @param resources
     *            an array specifying the resources to examine.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    public static String determineRolesForResources(
            HttpServletRequest request, MessageResources messages, Resource[] resources) {
        // Validate parameter
        validateParameterNotNull(messages, "messages");
        validateParameterNotNull(resources, "resources");

        if (AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
            return messages.getMessage("ResourceRole." + Constants.MANAGER_ROLE_NAME.replaceAll(" ", ""));
        }
        if (AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)) {
            return messages.getMessage("ResourceRole." + Constants.MANAGER_ROLE_NAME.replaceAll(" ", ""));
        }

        List<String> roleNames = new ArrayList<String>();
        // Add individual roles to the list
        for (int i = 0; i < resources.length; ++i) {
            String roleName = resources[i].getResourceRole().getName();
            // Do not add the same role twice
            if (!roleNames.contains(roleName)) {
                roleNames.add(roleName);
            }
        }

        // If a list is empty, than the user either
        // is not logged in or belongs to the Public group
        if (roleNames.isEmpty()) {
            roleNames.add(Constants.PUBLIC_ROLE_NAME);
        }
        // Avoid unneeded object creation of the list contains single item
        if (roleNames.size() == 1) {
            return messages.getMessage("ResourceRole." + ((String) roleNames.get(0)).replaceAll(" ", ""));
        }

        StringBuffer roles = new StringBuffer(32);

        // Form a string with roles separated by forward slash character
        for (int i = 0; i < roleNames.size(); ++i) {
            if (roles.length() != 0) {
                roles.append('/');
            }
            roles.append(messages.getMessage("ResourceRole." + ((String) roleNames.get(i)).replaceAll(" ", "")));
        }
        // Return the resulting string
        return roles.toString();
    }

    /**
     * <p>Gets the list of payments per resource roles assigned to user.</p>
     *
     * @param myResources a <code>Resource</code> array listing the resources associated with user.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @return a <code>Map</code> mapping the resource roles to respective payments.
     * @throws ResourcePersistenceException if an unexpected error occurs.
     * @since 1.8
     */
    public static Map<ResourceRole, Double> getMyPayments(Resource[] myResources, HttpServletRequest request)
            throws ResourcePersistenceException {
        Map<ResourceRole, Double> payments = new LinkedHashMap<ResourceRole, Double>();

        for (int i = 0; i < myResources.length; ++i) {
            Resource resource = myResources[i];
            String paymentStr = (String) resource.getProperty("Payment");
            if (paymentStr != null && paymentStr.trim().length() != 0) {
                double payment = Double.parseDouble(paymentStr);
                payments.put(resource.getResourceRole(), payment);
            } else {
                payments.put(resource.getResourceRole(), null);
            }
        }

        return payments;
    }

    /**
     * <p>Gets the list of statuses of payments per resource roles assigned to user.</p>
     *
     * @param myResources a <code>Resource</code> array listing the resources associated with user.
     * @return a <code>Map</code> mapping the resource roles to respective payment statuses.
     * @since 1.8
     */
    public static Map<ResourceRole, Boolean> getMyPaymentStatuses(Resource[] myResources) {
        Map<ResourceRole, Boolean> paymentStatuses = new LinkedHashMap<ResourceRole, Boolean>();

        for (int i = 0; i < myResources.length; ++i) {
            Resource resource = myResources[i];
            String paid = (String) resource.getProperty("Payment Status");
            if ("Yes".equalsIgnoreCase(paid)) {
                paymentStatuses.put(resource.getResourceRole(), Boolean.TRUE);
            } else {
                paymentStatuses.put(resource.getResourceRole(), Boolean.FALSE);
            }
        }

        return paymentStatuses;
    }

    /**
     * This static method retrieves an array of phases for the project specified by
     * <code>project</code> parameter, using <code>PhaseManager</code> object specified by
     * <code>manager</code> parameter.
     *
     * @return an array of phases for the project.
     * @param manager
     *            an instance of <code>PhaseManager</code> object.
     * @param project
     *            a project to retrieve phases for.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws PhaseManagementException
     *             if an error occurred querying the project from the persistent store.
     */
    public static Phase[] getPhasesForProject(PhaseManager manager, Project project)
        throws PhaseManagementException {
        // Validate parameters
        validateParameterNotNull(manager, "manager");
        validateParameterNotNull(project, "project");

        // Get all phases for the project
        com.topcoder.project.phases.Project phProj = manager.getPhases(project.getId());
        return (phProj != null) ? phProj.getAllPhases() : new Phase[0];
    }

    /**
     * This static method returns an array of all currently active phases.
     *
     * @return an array of active phases. This return value will never be <code>null</code>, but
     *         can be an empty array (for completed or cancelled projects, etc.)
     * @param phases
     *            an array of phases to retrieve all active phases from.
     * @throws IllegalArgumentException
     *             if <code>phases</code> parameter is <code>null</code>.
     */
    public static Phase[] getActivePhases(Phase[] phases) {
        // Validate parameter
        validateParameterNotNull(phases, "phases");

        List<Phase> activePhases = new ArrayList<Phase>();

        for (int i = 0; i < phases.length; ++i) {
            // Get a phase for the current iteration
            Phase phase = phases[i];
            // Add the phase to list if it is open and, hence, active
            if (phase.getPhaseStatus().getName().equals(PhaseStatus.OPEN.getName())) {
                activePhases.add(phase);
            }
        }

        // Convert the list to array and return it
        return activePhases.toArray(new Phase[activePhases.size()]);
    }

    /**
     * This static method returns the phase with a particular name for a project.
     *
     * @return the phase, or <code>null</code> if there is no phase with specified name.
     * @param phases
     *            an array of phases to search for the particular phase specified by
     *            <code>phaseName</code> and <code>activeOnly</code> parameters.
     * @param activeOnly
     *            determines whether this method should search for active phases only. If this
     *            parameter set to <code>false</code>, the parameter <code>phaseName</code> is
     *            required.
     * @param phaseName
     *            Optional name of the phase to search for if there is a possiblity that more than
     *            one phase is active, or the search is not being performed for active phase.
     * @throws IllegalArgumentException
     *             if <code>phases</code> parameter is <code>null</code>.
     */
    public static Phase getPhase(Phase[] phases, boolean activeOnly, String phaseName) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");

        if (activeOnly == true) {
            // This method is a simpler version of the getActivePhases one
            // It will simply return the first phase in the array returned
            // from that method that has the specified name if the name was specified
            Phase[] activePhases = getActivePhases(phases);

            // No active phases?
            if (activePhases.length == 0) {
                return null;
            }
            // Return first phase in the active phases array if phase name was not specified
            if (phaseName == null) {
                return activePhases[0];
            }

            // Perform a search
            for (int i = 0; i < activePhases.length; ++i) {
                if (activePhases[i].getPhaseType().getName().equalsIgnoreCase(phaseName)) {
                    return activePhases[i];
                }
            }
            // Active phase with specified name has not been found
            return null;
        } else {
            // Phase name should be specified if the search is done for (possibly) closed phase
            validateParameterStringNotEmpty(phaseName, "phaseName");

            Phase phaseFound = null;

            for (int i = 0; i < phases.length; ++i) {
                // Get a phase for the current iteration
                Phase phase = phases[i];
                // Get a name of status of this phase
                String phaseStatus = phase.getPhaseStatus().getName();
                // If the phase found that is not yet open, stop the search
                if (phaseStatus.equals(PhaseStatus.SCHEDULED.getName())) {
                    break;
                }
                // If the name of the current phase matches the one
                // specified by method's parameter, remeber this phase
                if (phase.getPhaseType().getName().equalsIgnoreCase(phaseName)) {
                    phaseFound = phase;
                }
            }

            // The phaseFound variable will contain the latest phase that has already been closed
            // or is currently open, or null if no phase with the required name has been found
            return phaseFound;
        }
    }

    /**
     * TODO: Document this method.
     *
     * @return
     * @param phases
     * @param deliverable
     */
    public static Phase getPhaseForDeliverable(Phase[] phases, Deliverable deliverable) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");
        validateParameterNotNull(deliverable, "deliverable");

        for (int i = 0; i < phases.length; ++i) {
            if (phases[i].getId() == deliverable.getPhase()) {
                return phases[i];
            }
        }

        return null;
    }

    /**
     * This static method retrieves a scorecard template for the specified phase using provided
     * Scorecard Manager.
     *
     * @return found Scorecard template, or <code>null</code> if no scorecard template was
     *         associated with the phase.
     * @param manager
     *            an instance of <code>ScorecardManager</code> object used to retrieve scorecard
     *            template.
     * @param phase
     *            a phase to retrieve scorecard template from.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws NumberFormatException
     *             if an error occurred while converting scorecard ID to its integer representation.
     * @throws PersistenceException
     *             if an error occurred while accessing the database.
     */
    public static Scorecard getScorecardTemplateForPhase(ScorecardManager manager, Phase phase)
        throws NumberFormatException, PersistenceException {
        // Validate parameters
        validateParameterNotNull(manager, "manager");
        validateParameterNotNull(phase, "phase");

        // Get an ID of scorecard template associated with this phase
        String scorecardId = (String)phase.getAttribute("Scorecard ID");
        // If no scorecard template is assigned to phase, return null value.
        if (scorecardId == null || scorecardId.trim().length() == 0) {
            return null;
        }

        // Get the Scorecard template by its ID and return it
        return manager.getScorecard(Long.parseLong(scorecardId, 10));
    }

    /**
     * This static method retrieves the resources for a project using provided Resource Manager.
     *
     * @return an array of the resources for the specified project.
     * @param manager
     *            an instance of <code>ResourceManager</code> object used to retrieve resources.
     * @param project
     *            a project to retrieve the resources for.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws BaseException
     *             if any error occurs.
     */
    public static Resource[] getAllResourcesForProject(ResourceManager manager, Project project)
        throws IllegalArgumentException, BaseException {
        // Validate parameters
        validateParameterNotNull(manager, "manager");
        validateParameterNotNull(project, "project");

        // Build a filter to fetch all resources for the current project
        Filter filter = ResourceFilterBuilder.createProjectIdFilter(project.getId());
        // Perform a search for the resources and return them
        return manager.searchResources(filter);
    }

    /**
     * This static method retrieves the resources for a phase using provided Resource Manager.
     *
     * @return an array of the resources for the specified phase.
     * @param manager
     *            an instance of <code>ResourceManager</code> object used to retrieve resources.
     * @param phase
     *            a phase to retrieve the resources for.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws BaseException
     *             if any error occurs.
     */
    public static Resource[] getAllResourcesForPhase(ResourceManager manager, Phase phase)
        throws BaseException {
        // Validate parameters
        validateParameterNotNull(manager, "manager");
        validateParameterNotNull(phase, "phase");

        // Prepare filter to select resource by project ID
        Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(phase.getProject().getId());
        // Prepare filter to select resource by phase ID
        Filter filterPhase = ResourceFilterBuilder.createPhaseIdFilter(phase.getId());
        // Combine the above filters into one
        Filter filter = new AndFilter(filterProject, filterPhase);

        // Perform a search for the resources and return them
        return manager.searchResources(filter);
    }

    /**
     * This static method retrieves the resources for a phase using provided array of resources.
     *
     * @return an array of the resources for the specified phase.
     * @param resources
     *            an array of resources to retrieve needed resources from.
     * @param phase
     *            a phase to retrieve the resources for.
     * @throws IllegalArgumentException
     *             if parameters <code>resources</code> is <code>null</code>.
     */
    public static Resource[] getResourcesForPhase(Resource[] resources, Phase phase) {
        // Validate parameters
        validateParameterNotNull(resources, "resources");

        List<Resource> foundResources = new ArrayList<Resource>();

        for (int i = 0; i < resources.length; ++i) {
            // Get a resource for the current iteration
            Resource resource = resources[i];
            // If this resource is from phase in question, add it to the list
            if (phase == null) {
                if (resource.getPhase() == null) {
                    foundResources.add(resource);
                }
            } else {
                // Handle Post-Mortem and Approval phases differently. Those resources are not mapped to phase type
                // so they must be discovered based on resource role name
                if (phase.getPhaseType().getName().equals(Constants.POST_MORTEM_PHASE_NAME)) {
                    if (resource.getResourceRole().getName().equals(Constants.POST_MORTEM_REVIEWER_ROLE_NAME)) {
                        foundResources.add(resource);
                    }
                } else if (phase.getPhaseType().getName().equals(Constants.APPROVAL_PHASE_NAME)) {
                    if (resource.getResourceRole().getName().equals(Constants.APPROVER_ROLE_NAME)) {
                        foundResources.add(resource);
                    }
                } else {
                    if (resource.getPhase() != null && resource.getPhase().longValue() == phase.getId()) {
                        foundResources.add(resource);
                    }
                }
            }
        }

        // Convert the list of resources to an array and return it
        return foundResources.toArray(new Resource[foundResources.size()]);
    }

    /**
     * This static method searches the provided array of resources for submitters (resources that
     * have Submitter role) and returns an array of all found resources.
     *
     * @return an array of submitters found in the provided array of resources.
     * @param resources
     *            an array of resource to search for submitters among.
     * @throws IllegalArgumentException
     *             if parameter <code>resources</code> is <code>null</code>.
     */
    public static Resource[] getAllSubmitters(Resource[] resources) {
        // Validate parameter
        validateParameterNotNull(resources, "resources");

        List<Resource> submitters = new ArrayList<Resource>();
        // Search for the appropriate resources and add them to the list
        for (int j = 0; j < resources.length; ++j) {
            if (resources[j].getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                submitters.add(resources[j]);
            }
        }

        // Convert the list of found submitters to array and return it
        return submitters.toArray(new Resource[submitters.size()]);
    }

    /**
     * TODO: Document this method.
     *
     * @return
     * @param projectId
     */
    public static Resource getWinner(HttpServletRequest request, long projectId) throws BaseException {
        ProjectManager projectManager = createProjectManager(request);
        ResourceManager resourceManager = createResourceManager(request);

        Project project = projectManager.getProject(projectId);
        String winnerId = (String) project.getProperty("Winner External Reference ID");
        if (winnerId != null) {

            long submitterRoleId = findResourceRoleByName(resourceManager.getAllResourceRoles(), "Submitter").getId();
            ResourceFilterBuilder.createExtensionPropertyNameFilter("External Reference ID");

            AndFilter fullFilter = new AndFilter(Arrays.asList(new Filter[] {
                    ResourceFilterBuilder.createResourceRoleIdFilter(submitterRoleId),
                    ResourceFilterBuilder.createProjectIdFilter(projectId),
                    ResourceFilterBuilder.createExtensionPropertyNameFilter("External Reference ID"),
                    ResourceFilterBuilder.createExtensionPropertyValueFilter(winnerId)
            }));


            Resource[] submitters = resourceManager.searchResources(fullFilter);
            if (submitters.length > 0) {
                return submitters[0];
            }
            return null;
        }
        return null;
    }

    /**
     * This static method searches the array of resources specified and finds a resource with
     * &quot;External Reference ID&quot; property being equal to the parameter specified.
     *
     * @return a resource associated with given external user ID, or <code>null</code> if no such
     *         resource has been found.
     * @param resources
     *            an array of resources to search.
     * @param extUserId
     *            an external ID of the user to retrieve the resource for.
     * @throws IllegalArgumentException
     *             if <code>resources</code> parameter is <code>null</code>, or if
     *             <code>extUserId</code> parameter is negative or zero value.
     */
    public static Resource getResourceByExtUserId(Resource[] resources, long extUserId) {
        // Validate parameters
        validateParameterNotNull(resources, "resources");
        validateParameterPositive(extUserId, "extUserId");

        for (int i = 0; i < resources.length; ++i) {
            // Get a resource for the current iteration
            Resource resource = resources[i];
            // Get an associated "External Reference ID" property for the resource
            String extRefIdStr = (String)resource.getProperty("External Reference ID");
            // If the property was not specified, skip this resource
            if (extRefIdStr == null || extRefIdStr.trim().length() == 0) {
                continue;
            }

            // If this is the resource that the search is being performed for, return it
            if (extUserId == Long.parseLong(extRefIdStr)) {
                return resource;
            }
        }
        // Indicate that the resource with specified external user assigned does not exist
        return null;
    }

    /**
     * This static method selects the resources for a project from the list of all resources
     * supplied to this method.
     *
     * @return an array containing just the resources that belong to the project specified. The
     *         returned array will be empty, if there are no such resources.
     * @param allResources
     *            all the resources to select a subset from.
     * @param project
     *            project which the subset of resources is needed for.
     * @throws IllegalArgumentException
     *             if <code>project</code> parameter is <code>null</code>.
     */
    public static Resource[] getResourcesForProject(Resource[] allResources, Project project) {
        // Validate parameters
        validateParameterNotNull(project, "project");

        // If the given list of resources is null or empty, return empty subset immediately
        if (allResources == null || allResources.length == 0) {
            return new Resource[0];
        }

        List<Resource> myResources = new ArrayList<Resource>();

        for (int i = 0; i < allResources.length; ++i) {
            // Get a resource for the current iteration
            Resource resource = allResources[i];
            // Determine if the resource is for current project
            if (resource.getProject() != null && resource.getProject().longValue() == project.getId()) {
                myResources.add(resource);
            }
        }

        // Convert the list to array and return it
        return myResources.toArray(new Resource[myResources.size()]);
    }

    /**
     * This static method returns the array of resources for the currently logged in user associated
     * with the specified phase. The list of all resources for the currently logged in user is
     * retrieved from the <code>HttpServletRequest</code> object specified by <code>request</code>
     * parameter. Method <code>gatherUserRoles(HttpServletRequest, long)</code> should be called
     * prior making a call to this method.
     *
     * @return an array of found resources, or emtpy array if no resources for currently logged in user
     *         found such that those resources would be associated with the specified phase.
     * @param request
     *            an <code>HttpServletRequest</code> object containing additional information.
     * @param phase
     *            a phase to search the resouces for. This parameter can be <code>null</code>, in
     *            which case the search is made for resources with no phase assigned.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     */
    public static Resource[] getMyResourcesForPhase(HttpServletRequest request, Phase phase) {
        // Validate parameters
        validateParameterNotNull(request, "request");
        // Retrieve the list of "my" resources from the request's attribute
        Resource[] myResources = (Resource[]) validateAttributeNotNull(request, "myResources");

        // Return the resources using another helper-method
        return getResourcesForPhase(myResources, phase);
    }

    /**
     * This static method returns the resource for role for the currently logged in user.
     * The list of all resources for the currently logged in user is
     * retrieved from the <code>HttpServletRequest</code> object specified by <code>request</code>
     * parameter. Method <code>gatherUserRoles(HttpServletRequest, long)</code> should be called
     * prior making a call to this method.
     *
     * @return the resource, or null if the current user doesn't have a resource with the role.
     * @param request
     *            an <code>HttpServletRequest</code> object containing additional information.
     * @param resourceRole the name of the resource role
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     *             if <code>resourceRole</code> parameter is <code>null</code>.
     *
     */
    public static Resource getMyResourceForRole(HttpServletRequest request, String resourceRole) {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterNotNull(resourceRole, "resourceRole");
        // Retrieve the list of "my" resources from the request's attribute
        Resource[] myResources = (Resource[]) validateAttributeNotNull(request, "myResources");
        for (Resource resource : myResources) {
            if (resource.getResourceRole().getName().equalsIgnoreCase(resourceRole)) {
                return resource;
            }
        }
        // Return the resources using another helper-method
        return null;
    }

    /**
     * This static method retrieves the resource for the currently logged in user associated with
     * the specified phase. The list of all resources for the currently logged in user is retrieved
     * from the <code>HttpServletRequest</code> object specified by <code>request</code>
     * parameter. Method <code>gatherUserRoles(HttpServletRequest, long)</code> should be called
     * prior making a call to this method.
     *
     * @return found resource, or <code>null</code> if no resource for currently logged in user
     *         found such that that resource would be associated with the specified phase.
     * @param request
     *            an <code>HttpServletRequest</code> object containing additional information.
     * @param phase
     *            a phase to search the resource for. This parameter can be <code>null</code>, in
     *            which case the search is made for resources with no phase assigned.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     */
    public static Resource getMyResourceForPhase(HttpServletRequest request, Phase phase) {
        Resource[] resources = getMyResourcesForPhase(request, phase);
        return (resources.length != 0) ? resources[0] : null;
    }

    /**
     * This static method retrieves all deliverables for all active phases for a specific project.
     * This method returns either completed or incomplete deliverables.
     *
     * @return an array of deliverables.
     * @param manager
     *            an instance of the <code>DeliverableManager</code> class.
     * @param phases
     *            an array of phases to search deliverables for.
     * @param resources
     *            an array of all resources for the current project.
     * @param winnerExtUserId
     *            an External User ID of the user who is the winner for the project. If there is no
     *            winner for the project, this parameter must be negative.
     * @throws IllegalArgumentException
     *             if any of the <code>manager</code>, <code>phases</code> or
     *             <code>resources</code> parameters are <code>null</code>.
     * @throws DeliverablePersistenceException
     *             if an error occurs while reading from the persistence store.
     * @throws SearchBuilderException
     *             if an error occurs executing the filter.
     * @throws DeliverableCheckingException
     *             if an error occurs when determining whether a Deliverable has been completed or
     *             not.
     */
    public static Deliverable[] getAllDeliverablesForPhases(
            DeliverableManager manager, Phase[] phases, Resource[] resources, long winnerExtUserId)
            throws DeliverablePersistenceException, SearchBuilderException, DeliverableCheckingException {

        // Validate parameters
        validateParameterNotNull(manager, "manager");
        validateParameterNotNull(phases, "phases");

        // A filter to search for deliverables for specific phase(s) of the project
        Filter filter = null;
        switch (phases.length) {
        case 0:
            // No phases -- no deliverables
            return new Deliverable[0];

        case 1:
            // If there is only one phase in the provided array,
            // create filter for it directly (no OR filters needed)
            filter = DeliverableFilterBuilder.createPhaseIdFilter(phases[0].getId());
            break;

        default:
            List<Filter> phaseFilters = new ArrayList<Filter>();
            // Prepare a list of filters for each phase in the array of phases
            for (int i = 0; i < phases.length; ++i) {
                phaseFilters.add(DeliverableFilterBuilder.createPhaseIdFilter(phases[i].getId()));
            }
            // Combine all filters using OR operator
            filter = new OrFilter(phaseFilters);
        }

        // Perform a search for the deliverables
        Deliverable[] allDeliverables = manager.searchDeliverables(filter, null);

        List<Deliverable> deliverables = new ArrayList<Deliverable>();

        // Additionally filter deliverables because sometimes deliverables
        // for another phases get though the above filter
        for (int i = 0; i < allDeliverables.length; ++i) {
            // Get a deliverable for the current iteration
            final Deliverable deliverable = allDeliverables[i];
            // Get an ID of resource this deliverable is for
            final long deliverableResourceId = deliverable.getResource();
            Resource forResource = null;
            int j;
            // Find a resource this deliverable is for
            for (j = 0; j < resources.length; ++j) {
                if (resources[j].getId() == deliverableResourceId) {
                    forResource = resources[j];
                    break;
                }
            }
            // There must be a resource associated with this deliverable, but
            // in case there isn't skip this deliverable for safety
            if (forResource == null) {
                continue;
            }

            // Make sure this is the correct resource first. Some deliverables are
            // assigned to resources not in their phase, and that's still considered correct
            final String resourceRole = forResource.getResourceRole().getName();
            // If found resource is associated with a phase,
            // make sure this phase is among ones the deliverables needed for
            if (forResource.getPhase() != null &&
                    (resourceRole.equalsIgnoreCase(Constants.FINAL_REVIEWER_ROLE_NAME) ||
                    resourceRole.equalsIgnoreCase(Constants.AGGREGATOR_ROLE_NAME) ||
                    resourceRole.equalsIgnoreCase(Constants.SPECIFICATION_REVIEWER_ROLE_NAME) ||
                    resourceRole.equalsIgnoreCase(Constants.APPROVER_ROLE_NAME))) {
                final long resourcePhaseId = forResource.getPhase().longValue();
                for (j = 0; j < phases.length; ++j) {
                    if (phases[j].getId() == resourcePhaseId) {
                        break;
                    }
                }
                // No phases for this resource, wrong deliverable, skip it
                if (j == phases.length) {
                    continue;
                }
            }

            // If current deliverable is Aggregation Review, and it is assigned to one of the reviewers,
            // check to make sure this reviewer is not also an aggregator
            if (deliverable.getName().equalsIgnoreCase(Constants.AGGREGATION_REV_DELIVERABLE_NAME) &&
                    (resourceRole.equalsIgnoreCase(Constants.REVIEWER_ROLE_NAME) ||
                    resourceRole.equalsIgnoreCase(Constants.ACCURACY_REVIEWER_ROLE_NAME) ||
                    resourceRole.equalsIgnoreCase(Constants.FAILURE_REVIEWER_ROLE_NAME) ||
                    resourceRole.equalsIgnoreCase(Constants.STRESS_REVIEWER_ROLE_NAME))) {
                final String originalExtId = (String) forResource.getProperty("External Reference ID");

                for (j = 0; j < resources.length; ++j) {
                    // Skip resource that is being checked
                    if (forResource == resources[j]) {
                        continue;
                    }

                    // Get a resource for the current iteration
                    final Resource otherResource = resources[j];
                    // Verify whether this resource is an Aggregator, and skip it if it isn't
                    if (!otherResource.getResourceRole().getName().equalsIgnoreCase(Constants.AGGREGATOR_ROLE_NAME)) {
                        continue;
                    }

                    String otherExtId = (String) resources[j].getProperty("External Reference ID");
                    // If appropriate aggregator's resource has been found, stop the search
                    if (originalExtId.equals(otherExtId)) {
                        break;
                    }
                }
                // Skip this deliverable if it is assigned to aggregator
                if (j != resources.length) {
                    continue;
                }
            }

            // If there is a winner for the project,
            // verify that the current deliverable is not for non-winning submitter
            if (winnerExtUserId > 0) {
                // Check that found resource is submitter and non-winner. The deliverable will
                // be skipped in this case. In all other cases it will be added to the list
                if (resourceRole.equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                    if (winnerExtUserId !=
                            Long.parseLong((String) forResource.getProperty("External Reference ID"), 10)) {
                        continue;
                    }
                }
            }

            // Add current deliverable to the list of deliverables
            deliverables.add(deliverable);
        }

        // Convert the list of deliverables into array and return it
        return deliverables.toArray(new Deliverable[deliverables.size()]);
    }

    /**
     * This static method retrieves outstanding deliverables from the list of deliverables. A
     * deliverable is considered to be outstaning if it is not completed.
     *
     * @return an array of outstanding deliverables.
     * @param allDeliverables
     *            an array of all deliverables to search for outstanding ones.
     * @throws IllegalArgumentException
     *             if <code>allDeliverables</code> parameter is <code>null</code>.
     */
    public static Deliverable[] getOutstandingDeliverables(Deliverable[] allDeliverables) {
        // Validate parameter
        validateParameterNotNull(allDeliverables, "allDeliverables");

        List<Deliverable> deliverables = new ArrayList<Deliverable>();
        // Perform a search for outstanding deliverables
        for (int i = 0; i < allDeliverables.length; ++i) {
            Deliverable deliverable = allDeliverables[i];
            if (!deliverable.isComplete()) {
                addDeliverable(deliverable, allDeliverables, deliverables);
            }
        }
        // Return a list of outstanding deliverables converted to array
        return deliverables.toArray(new Deliverable[deliverables.size()]);
    }

    /**
     * This static method retrieves deliverables for resource roles the logged in user has.
     *
     * @return an array of deliverables assigned to the currently logged in user.
     * @param allDeliverables
     *            an array of all deliverables to search for outstanding ones.
     * @param myResources
     *            an array of all resources assigned to the currently logged in user for a
     *            particular project.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    public static Deliverable[] getMyDeliverables(Deliverable[] allDeliverables, Resource[] myResources) {
        // Validate parameters
        validateParameterNotNull(allDeliverables, "allDeliverables");
        validateParameterNotNull(myResources, "myResources");

        List<Deliverable> deliverables = new ArrayList<Deliverable>();
        // Perform a search for "my" deliverables
        for (int i = 0; i < allDeliverables.length; ++i) {
            // Get a deliverable for current iteration
            Deliverable deliverable = allDeliverables[i];
            boolean found = false;
            // Determine if this deliverable is assigned to currently logged in user
            for (int j = 0; j < myResources.length; ++j) {
                if (deliverable.getResource() == myResources[j].getId()) {
                    found = true;
                    break;
                }
            }
            // If found is true, it means that current
            // deliverable is assigned to currently logged in user
            if (found == true) {
                addDeliverable(deliverable, allDeliverables, deliverables);
            }
        }
        // Return a list of "my" deliverables converted to array
        return deliverables.toArray(new Deliverable[deliverables.size()]);
    }

    /**
     * This static method retrieves an array of external user objects for the specified array of
     * resources. Each entry in the resulting array will correspond to the corresponding entry in
     * the input <code>resources</code> array. If there are no matches found for some resource,
     * the corresponding item in the resulting array will contain <code>null</code>.
     *
     * @return an array of external user objects for the specified resources.
     * @param retrieval
     *            a <code>UserRetrieval</code> object used to retrieve external user objects.
     * @param resources
     *            an array of resources to retrieve corresponding external user objects for.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws RetrievalException
     *             if some error happend during external user retrieval.
     */
    public static ExternalUser[] getExternalUsersForResources(UserRetrieval retrieval, Resource[] resources)
        throws RetrievalException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(retrieval, "retrieval");
        ActionsHelper.validateParameterNotNull(resources, "resources");

        // If there are no resource for this project defined, there will be no external users
        if (resources.length == 0) {
            return new ExternalUser[0];
        }

        // Prepare an array to store External User IDs
        long[] extUserIds = new long[resources.length];
        // Fill the array with user IDs retrieved from resource properties
        for (int i = 0; i < resources.length; ++i) {
            String userID = (String) resources[i].getProperty("External Reference ID");
            extUserIds[i] = Long.parseLong(userID, 10);
        }

        // Retrieve external users to the temporary array
        ExternalUser[] extUsers = retrieval.retrieveUsers(extUserIds);

        // This is final array for External User objects. It is needed because the previous
        // operation may return shorter array than there are resources for the project
        // (sometimes several resources can be associated with one external user)
        ExternalUser[] allExtUsers = new ExternalUser[resources.length];

        for (int i = 0; i < extUserIds.length; ++i) {
            for (int j = 0; j < extUsers.length; ++j) {
                if (extUsers[j].getId() == extUserIds[i]) {
                    allExtUsers[i] = extUsers[j];
                    break;
                }
            }
        }

        return allExtUsers;
    }

    /**
     * TODO: Write documentation for this method.
     *
     * @return
     * @param manager
     * @param project
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws BaseException
     */
    public static Submission[] getMostRecentSubmissions(UploadManager manager, Project project)
        throws BaseException {
        // Validate parameters
        validateParameterNotNull(manager, "manager");
        validateParameterNotNull(project, "project");

        SubmissionStatus[] allSubmissionStatuses = manager.getAllSubmissionStatuses();
        SubmissionType[] allSubmissionTypes = manager.getAllSubmissionTypes();
        SubmissionType submissionType
            = ActionsHelper.findSubmissionTypeByName(allSubmissionTypes, "Contest Submission");


        Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterType = SubmissionFilterBuilder.createSubmissionTypeIdFilter(submissionType.getId());
        Filter filterStatus = createSubmissionStatusFilter(allSubmissionStatuses);

        Filter filter = new AndFilter(Arrays.asList(filterProject, filterStatus, filterType));

        return manager.searchSubmissions(filter);
    }

    /**
     * TODO: Write documentation for this method.
     *
     * @return
     * @param allSubmissionStatuses
     * @throws IllegalArgumentException
     *             if <code>allSubmissionStatuses</code> parameter is <code>null</code>.
     */
    public static Filter createSubmissionStatusFilter(SubmissionStatus[] allSubmissionStatuses) {
        // Validate parameter
        validateParameterNotNull(allSubmissionStatuses, "allSubmissionStatuses");

        List<Long> statusIds = new ArrayList<Long>();

        for (int i = 0; i < allSubmissionStatuses.length; ++i) {
            if (!allSubmissionStatuses[i].getName().equalsIgnoreCase("Deleted")) {
                statusIds.add(allSubmissionStatuses[i].getId());
            }
        }

        return new InFilter("submission_status_id", statusIds);
    }

    /**
     * This static method retrieves the project that the submission specified by the
     * <code>submission</code> parameter was made for.
     *
     * @return a retrieved project.
     * @param manager
     *            an instance of <code>ProjectManager</code> object used to retrieve project from
     *            the submission.
     * @param submission
     *            a submission to retrieve the project for.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     * @throws com.topcoder.management.project.PersistenceException
     *             if an error occurred while accessing the database.
     */
    public static Project getProjectForSubmission(ProjectManager manager, Submission submission)
        throws com.topcoder.management.project.PersistenceException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(manager, "manager");
        ActionsHelper.validateParameterNotNull(submission, "submission");

        // Get an upload for this submission
        Upload upload = submission.getUpload();
        // Get Project by its id
        Project project = manager.getProject(upload.getProject());
        // Return the project
        return project;
    }

    /**
     * This static method // TODO: should be documented
     *
     * @return
     * @param phases
     * @param phaseIndex
     * @param phaseName
     * @throws IllegalArgumentException
     *             if <code>phases</code> parameter is <code>null</code> or if
     *             <code>phaseIndex</code> parameter is not within valid range of the array
     *             specified by <code>phases</code> parameter (thus, there is no way to pass an
     *             empty array to this method).
     */
    public static boolean isInOrAfterPhase(Phase[] phases, int phaseIndex, String phaseName) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");
        validateParameterInRange(phaseIndex, "phaseIndex", 0, phases.length - 1);
        validateParameterStringNotEmpty(phaseName, "phaseName");

        for (int i = phaseIndex; i < phases.length; ++i) {
            // Get a phase for the current iteration
            final Phase phase = phases[i];

            if (phase.getPhaseType().getName().equalsIgnoreCase(phaseName)) {
                return (!phase.getPhaseStatus().getName().equalsIgnoreCase(Constants.SCHEDULED_PH_STATUS_NAME));
            }
        }
        return false;
    }

    /**
     * TODO: Document this method.
     *
     * @return
     * @param phases
     * @param phaseIndex
     */
    public static boolean isAfterAppealsResponse(Phase[] phases, int phaseIndex) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");
        validateParameterInRange(phaseIndex, "phaseIndex", 0, phases.length - 1);

        boolean prevPhase = false;
        boolean found = false;

        for (int i = phaseIndex; i < phases.length; ++i) {
            // Get a phase for the current iteration
            Phase phase = phases[i];
            // Get this phase's type name
            String phaseName = phase.getPhaseType().getName();

            if (phaseName.equalsIgnoreCase(Constants.REGISTRATION_PHASE_NAME) ||
                    phaseName.equalsIgnoreCase(Constants.SUBMISSION_PHASE_NAME)) {
                if (prevPhase == true) {
                    return true;
                }
                prevPhase = false;
                continue;
            }
            prevPhase = true;
            if (phaseName.equalsIgnoreCase(Constants.REVIEW_PHASE_NAME) ||
                    phaseName.equalsIgnoreCase(Constants.APPEALS_PHASE_NAME) ||
                    phaseName.equalsIgnoreCase(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                if (!phase.getPhaseStatus().getName().equals(PhaseStatus.CLOSED.getName())) {
                    return false;
                }
                found = true;
                continue;
            }
            if (found == true) {
                return true;
            }
        }
        return true;
    }

    /**
     * TODO: Document this method.
     *
     * @return
     * @param phases
     */
    public static boolean isAfterAppealsResponse(Phase[] phases) {
        // Validate parameter
        validateParameterNotNull(phases, "phases");

        int i;

        for (i = 0; i < phases.length; ++i) {
            // Get a phase for the current iteration
            Phase phase = phases[i];
            // Get this phase's status name
            String phaseStatus = phase.getPhaseStatus().getName();
            // If first Open or Scheduled phase found, stop the search
            if (phaseStatus.equals(PhaseStatus.OPEN.getName()) || phaseStatus.equals(PhaseStatus.SCHEDULED.getName())) {
                break;
            }
        }
        // If all phases are closed, then we should definitely be past Appeals Response one
        if (i >= phases.length) {
            return true;
        }

        boolean anyOtherPhaseFound = false;

        for (; i >= 0; --i) {
            // Get a phase for the current iteration
            Phase phase = phases[i];
            String phaseName = phase.getPhaseType().getName();

            // If registration or Submission phase found before the Open one (or one of those
            // phases is currently Open), return false, as we are not past Appeals Response one
            if (phaseName.equalsIgnoreCase(Constants.REGISTRATION_PHASE_NAME) ||
                    phaseName.equalsIgnoreCase(Constants.SUBMISSION_PHASE_NAME)) {
                return false;
            }
            // Skip the Open or Scheduled phase, as only Closed phases make interest
            if (!phase.getPhaseStatus().getName().equals(PhaseStatus.CLOSED.getName())) {
                continue;
            }
            // If Appeals response is the closed phase,
            // then definetely the project is at after Appeals Response stage
            if (phaseName.equalsIgnoreCase(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                return true;
            }
            // If the phase Review or Appeals is found, but there were other closed phases after them,
            // regard this as after Appeals Response (if Appeals Response is actually absent)
            if (anyOtherPhaseFound &&
                    (phaseName.equalsIgnoreCase(Constants.APPEALS_PHASE_NAME) ||
                            phaseName.equalsIgnoreCase(Constants.REVIEW_PHASE_NAME))) {
                return true;
            }
            anyOtherPhaseFound = true;
        }

        // If i is negative, the needed phase has not been found
        // The project is not in after Appeals Response phase
        return (i >= 0);
    }


    // -------------------------------------------------------------- Creator type of methods -----

    /**
     * This static method helps to create an object of the <code>PhaseManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>PhaseManager</code> object can be stored to let reusing it later for the
     *            same request.
     * @param registerPhaseHandlers
     *            a boolean parameter that determines whether phase handlers need to be registered
     *            with the newly-created (or already existing) Phase Manager.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseException
     *             if any error happens during object creation.
     */
    public static PhaseManager createPhaseManager(HttpServletRequest request, boolean registerPhaseHandlers)
        throws BaseException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Phase Manager from the request's attribute first
        PhaseManager manager = (PhaseManager) request.getAttribute("phaseManager+handlers");
        if (manager == null) {
            manager = (PhaseManager) request.getAttribute("phaseManager");
        } else {
            registerPhaseHandlers = false;
        }

        // If this is the first time this method is called for the request,
        // create a new instance of the object, and possibly register phase handlers
        if (manager == null || registerPhaseHandlers) {
            // Create Phase Manager object if needed
            if (manager == null) {
                manager = new DefaultPhaseManager("com.topcoder.management.phase");
            }

            // Register phase handlers if this was requested
            if (registerPhaseHandlers) {
                // create a fresh manager with the handlers set.
                manager = managerCreationHelper.getPhaseManager();
            }

            // Place newly-created object into the request as attribute
            request.setAttribute((registerPhaseHandlers) ? "phaseManager+handlers" : "phaseManager", manager);
        }

        // Return the Phase Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>PhasePersistence</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>PhaseManager</code> object can be stored to let reusing it later for the
     *            same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseException
     *             if any error happens during object creation.
     * @since 1.10
     */
    public static PhasePersistence createPhasePersistence(HttpServletRequest request)
        throws BaseException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Phase Persistence from the request's attribute first
        PhasePersistence persistence = (PhasePersistence) request.getAttribute("phasePersistence");

        // If this is the first time this method is called for the request,
        if (persistence == null) {
            persistence = new InformixPhasePersistence("com.topcoder.management.phase");

            // Place newly-created object into the request as attribute
            request.setAttribute("phasePersistence", persistence);
        }

        // Return the Phase Persistence object
        return persistence;
    }

    /**
     * This static method helps to create an object of the <code>ProjectManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>ProjectManager</code> object can be stored to let reusing it later for the
     *            same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     */
    public static ProjectManager createProjectManager(HttpServletRequest request) {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Project Manager from the request's attribute first
        ProjectManager manager = (ProjectManager) request.getAttribute("projectManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager  = managerCreationHelper.getProjectManager();
            // Place newly-created object into the request as attribute
            request.setAttribute("projectManager", manager);
        }

        // Return the Project Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>ResourceManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>ResourceManager</code> object can be stored to let reusing it later for
     *            the same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseRuntimeException
     *             if any error occurs.
     */
    public static ResourceManager createResourceManager(HttpServletRequest request) {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Resource Manager from the request's attribute first
        ResourceManager manager = (ResourceManager) request.getAttribute("resourceManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = managerCreationHelper.getResourceManager();
            // Place newly-created object into the request as attribute
            request.setAttribute("resourceManager", manager);
        }

        // Return the Resource Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>ReviewManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>ReviewManager</code> object can be stored to let reusing it later for the
     *            same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws com.topcoder.management.review.ConfigurationException
     *             if error occurs while loading configuration settings, or any of the required
     *             configuration parameters are missing.
     */
    public static ReviewManager createReviewManager(HttpServletRequest request)
        throws com.topcoder.management.review.ConfigurationException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Review Manager from the request's attribute first
        ReviewManager manager = (ReviewManager) request.getAttribute("reviewManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = new DefaultReviewManager();
            // Place newly-created object into the request as attribute
            request.setAttribute("reviewManager", manager);
        }

        // Return the Review Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>LateDeliverableManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>LateDeliverableManager</code> object can be stored to let reusing it later for the
     *            same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws LateDeliverableManagementConfigurationException
     *             if fail to initialize the <code>LateDeliverableManagerImpl</code> instance.
     * @since 1.10
     */
    public static LateDeliverableManager createLateDeliverableManager(HttpServletRequest request) {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Review Manager from the request's attribute first
        LateDeliverableManager manager = (LateDeliverableManager) request.getAttribute("lateDeliverableManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = new LateDeliverableManagerImpl("com/topcoder/util/config/ConfigManager.properties",
                    LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE);
            // Place newly-created object into the request as attribute
            request.setAttribute("lateDeliverableManager", manager);
        }

        // Return the Late Deliverable Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>ReviewScoreAggregator</code>
     * class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>ReviewScoreAggregator</code> object can be stored to let reusing it later
     *            for the same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws ReviewScoreAggregatorConfigException
     *             if any of the four required algorithm objects cannot be instantiated or the
     *             configuration is invalid.
     */
    public static ReviewScoreAggregator createScoreAggregator(HttpServletRequest request)
        throws ReviewScoreAggregatorConfigException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Review Score Aggregator from the request's attribute first
        ReviewScoreAggregator aggregator = (ReviewScoreAggregator) request.getAttribute("reviewScoreAggregator");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (aggregator == null) {
            aggregator = new ReviewScoreAggregator("com.topcoder.management.review.scoreaggregator");
            // Place newly-created object into the request as attribute
            request.setAttribute("reviewScoreAggregator", aggregator);
        }

        // Return the Review Score Aggregator object
        return aggregator;
    }

    /**
     * This static method helps to create an object of the <code>ScorecardManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>ScorecardManager</code> object can be stored to let reusing it later for
     *            the same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws com.topcoder.management.scorecard.ConfigurationException
     *             if error occurs while loading configuration settings, or any of the required
     *             configuration parameters are missing.
     */
    public static ScorecardManager createScorecardManager(HttpServletRequest request)
        throws com.topcoder.management.scorecard.ConfigurationException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Scorecard Manager from the request's attribute first
        ScorecardManager manager = (ScorecardManager) request.getAttribute("scorecardManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = new ScorecardManagerImpl();
            // Place newly-created object into the request as attribute
            request.setAttribute("scorecardManager", manager);
        }

        // Return the Scorecard Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>DeliverableManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>DeliverableManager</code> object can be stored to let reusing it later for
     *            the same request.
     * @throws ConfigurationException
     *             if any error occurs while reading the configuration properties and initializing
     *             the state of the database connection factory.
     * @throws UnknownConnectionException
     *             if the connectionProducers does not contain the defaultProducer name.
     * @throws SearchBuilderConfigurationException
     *             if any error occurs during creation of the search bundles.
     */
    public static DeliverableManager createDeliverableManager(HttpServletRequest request)
        throws ConfigurationException, UnknownConnectionException, SearchBuilderConfigurationException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Upload Manager from the request's attribute first
        DeliverableManager manager = (DeliverableManager) request.getAttribute("deliverableManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
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

            checkers.put(Constants.SUBMISSION_DELIVERABLE_NAME, new SubmissionDeliverableChecker(dbconn));
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
            checkers.put(Constants.AGGREGATION_REV_DELIVERABLE_NAME, new AggregationReviewDeliverableChecker(dbconn));
            checkers.put(Constants.FINAL_FIX_DELIVERABLE_NAME, new FinalFixesDeliverableChecker(dbconn));
            checkers.put(Constants.SCORECARD_COMM_DELIVERABLE_NAME, new SubmitterCommentDeliverableChecker(dbconn));
            checkers.put(Constants.FINAL_REVIEW_PHASE_NAME, new FinalReviewDeliverableChecker(dbconn));
            checkers.put(Constants.APPROVAL_DELIVERABLE_NAME, new ApprovalDeliverableChecker(dbconn));
            checkers.put(Constants.POST_MORTEM_DELIVERABLE_NAME, submissionIndependentReviewChecker);

            // Initialize the PersistenceDeliverableManager
            manager = new PersistenceDeliverableManager(deliverablePersistence, checkers,
                    deliverableSearchBundle, deliverableWithSubmissionsSearchBundle);
            // Place newly-created object into the request as attribute
            request.setAttribute("deliverableManager", manager);
        }

        // Return the Deliverable Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>UploadManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>UploadManager</code> object can be stored to let reusing it later for the
     *            same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseRuntimeException
     *             if any error occurs.
     */
    public static UploadManager createUploadManager(HttpServletRequest request) {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Upload Manager from the request's attribute first
        UploadManager manager = (UploadManager) request.getAttribute("uploadManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = managerCreationHelper.getUploadManager();
            // Place newly-created object into the request as attribute
            request.setAttribute("uploadManager", manager);
        }

        // Return the Upload Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>ScreeningManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>ScreeningManager</code> object can be stored to let reusing it later for
     *            the same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseRuntimeException
     *             if any error occurs.
     */
    public static ScreeningManager createScreeningManager(HttpServletRequest request) {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Auto Screening Manager from the request's attribute first
        ScreeningManager manager = (ScreeningManager) request.getAttribute("screeningManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = managerCreationHelper.getScreeningManager();
            // Place newly-created object into the request as attribute
            request.setAttribute("screeningManager", manager);
        }

        // Return the Screening Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>ProjectLinkManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request an <code>HttpServletRequest</code> object, where created <code>ResourceManager</code> object can
     *            be stored to let reusing it later for the same request.
     * @throws IllegalArgumentException if <code>request</code> parameter is <code>null</code>.
     * @throws BaseRuntimeException if any error occurs.
     * @since 1.1 OR Project Linking Assembly
     */
    public static ProjectLinkManager createProjectLinkManager(HttpServletRequest request) {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Project Link Manager from the request's attribute first
        ProjectLinkManager manager = (ProjectLinkManager) request.getAttribute("projectLinkManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            // manager = managerCreationHelper.getResourceManager();
            manager = managerCreationHelper.getProjectLinkManager();
            // Place newly-created object into the request as attribute
            request.setAttribute("projectLinkManager", manager);
        }

        // Return the Resource Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>UserRetrieval</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>UserRetrieval</code> object can be stored to let reusing it later for the
     *            same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws com.cronos.onlinereview.external.ConfigException
     *             if error occurs while loading configuration settings, or any of the required
     *             configuration parameters are missing.
     */
    public static UserRetrieval createUserRetrieval(HttpServletRequest request)
        throws com.cronos.onlinereview.external.ConfigException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Upload Retrieval from the request's attribute first
        UserRetrieval manager = (UserRetrieval) request.getAttribute("userRetrieval");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = new DBUserRetrieval(DB_CONNECTION_NAMESPACE);
            // Place newly-created object into the request as attribute
            request.setAttribute("userRetrieval", manager);
        }

        // Return the Upload Retrieval object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>FileUpload</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> object, where created
     *            <code>UserRetrieval</code> object can be stored to let reusing it later for the
     *            same request.
     * @throws com.topcoder.servlet.request.ConfigurationException
     *             if any error occurs while reading parameters from the configuration file.
     * @throws DisallowedDirectoryException
     *             if the directory is not one of the allowed directories.
     */
    public static FileUpload createFileUploadManager(HttpServletRequest request)
        throws DisallowedDirectoryException, com.topcoder.servlet.request.ConfigurationException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving File Upload from the request's attribute first
        FileUpload fileUpload = (FileUpload) request.getAttribute("fileUploadManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (fileUpload == null) {
//            fileUpload = new RemoteFileUpload("com.topcoder.servlet.request.RemoteFileUpload");
            fileUpload = new LocalFileUpload("com.topcoder.servlet.request.LocalFileUpload");
            // Place newly-created object into the request as attribute
            request.setAttribute("fileUploadManager", fileUpload);
        }

        // Return the File Upload object
        return fileUpload;
    }

    /**
     * This static method helps to get a list of <code>ClientProject</code>
     *
     * @return a list of <code>ClientProject</code>
     * @param request
     *            an <code>HttpServletRequest</code> object, where created list of <code>ClientProject</code> can be
     *            stored to let reusing it later for the same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseException
     *             if any error occurs.
     * @since Online Review Update - Add Project Dropdown v1.0
     */
    @SuppressWarnings("unchecked")
    public static List<ClientProject> getClientProjects(HttpServletRequest request) throws ManagerCreationException,
            BaseException {
        validateParameterNotNull(request, "request");

        long userId = AuthorizationHelper.getLoggedInUserId(request);

        List<ClientProject> clientProjects = (List<ClientProject>) request.getAttribute("clientProjectsList");
        if (clientProjects == null) {

            // If this function is called the first time after the user has logged in,
            // obtain and store in the session the handle of the user
            if (request.getSession().getAttribute("userHandle") == null) {
                // Obtain an instance of the User Retrieveal object
                UserRetrieval usrMgr = ActionsHelper.createUserRetrieval(request);
                // Get External User object for the currently logged in user
                ExternalUser extUser = usrMgr.retrieveUser(AuthorizationHelper.getLoggedInUserId(request));
                // Place handle of the user into session as attribute
                request.getSession().setAttribute("userHandle", extUser.getHandle());
            }

            String username = (String)(request.getSession().getAttribute("userHandle"));

            Connection conn = null;
            Statement selectStmt = null;
            ResultSet resultSet = null;
            try {

                clientProjects = new LinkedList<ClientProject>();

                // we first add the empty client project for a default selection.
                ClientProject cp = new ClientProject();
                // set the default id to 0.
                cp.setId(0);
                cp.setName("-------------");
                clientProjects.add(cp);

                DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
                conn = dbconn.createConnection(DB_CONNECTION_TIMEDS);
                log.log(Level.DEBUG, "create db connection with timeDS from DBConnectionFactoryImpl with namespace:"
                        + DB_CONNECTION_NAMESPACE);

                String queryString = "";

                String nonadminQueryString = SELECT_PROJECT + " and active = 1 and p.project_id in " + "("
                    + SELECT_MANAGER_PROJECT + "'" + username + "' " + "union "
                    + SELECT_WORKER_PROJECT + "'" + username + "')";
                nonadminQueryString += " order by upper(name) ";

                String adminQueryString = "SELECT project_id, name FROM project WHERE is_deleted = 0 or is_deleted IS NULL ORDER BY UPPER(name)";

                if (AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME))
                {
                    queryString = adminQueryString;
                }
                else
                {
                    queryString = nonadminQueryString;
                }


                selectStmt = conn.createStatement();
                resultSet = selectStmt.executeQuery(queryString);

                while (resultSet.next()) {
                    long projectID = resultSet.getLong(1);
                    String projectName = resultSet.getString(2);

                    ClientProject clientProject = new ClientProject();
                    clientProject.setId(projectID);
                    clientProject.setName(projectName);

                    clientProjects.add(clientProject);
                }
            } catch (UnknownConnectionException e) {
                throw new BaseException("Failed to create connection", e);
            } catch (ConfigurationException e) {
                throw new BaseException("Failed to config for DBNamespace", e);
            } catch (DBConnectionException e) {
                throw new BaseException("Failed to return DBConnection", e);
            } catch (SQLException e) {
                log.log(Level.WARN, "Failed to read from project table in time_oltp" + e);
            } finally {
                close(resultSet);
                close(selectStmt);
                close(conn);
            }

            // Place newly-created client projects into the request as attribute
            request.setAttribute("clientProjectsList", clientProjects);
        }

        // Return the client projects
        return clientProjects;
    }

    /**
     * This static method helps to create an object of the <code>PhaseTemplate</code> class.
     *
     * @return a newly created instance of the class.
     * @param projectType
     *            a project type for which the PhaseTemplate object should be created,
     *            can be null if start date generator type doesn't matter
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseException
     *             if any error happens during object creation.
     */
    public static PhaseTemplate createPhaseTemplate(ProjectType projectType) throws BaseException {
        // Create phase template persistence
        PhaseTemplatePersistence persistence = new XmlPhaseTemplatePersistence(PHASES_TEMPLATE_PERSISTENCE_NAMESPACE);
        // Create start date generator
        StartDateGenerator generator;
        if ("Component".equals(projectType)) {
            // TODO: Specify conf. namespace
            generator = new RelativeWeekTimeStartDateGenerator("");
        } else {
            // Create start date generator which always returns current date
            generator = new StartDateGenerator() {
                public Date generateStartDate() {
                    return new Date();
                }
            };
        }

        // Create workdays instance
        Workdays workdays = (new DefaultWorkdaysFactory()).createWorkdaysInstance();

        // Create phase template instance
        PhaseTemplate phaseTemplate = new DefaultPhaseTemplate(persistence, generator, workdays );

        return phaseTemplate;
    }

    /**
     * Set Completion Timestamp while the project turn to completed, Cancelled - Failed Review or Deleted status.
     *
     * @param project the project instance
     * @param newProjectStatus new project status
     * @param format the date format
     * @throws BaseException if any
     */
    static void setProjectCompletionDate(Project project, ProjectStatus newProjectStatus, Format format)
            throws BaseException {

        String name = newProjectStatus.getName();
        if ("Completed".equals(name)
                || "Cancelled - Failed Review".equals(name)
                || "Deleted".equals(name)
                || "Cancelled - Failed Screening".equals(name)
                || "Cancelled - Zero Submissions".equals(name)
                || "Cancelled - Winner Unresponsive".equals(name)
                || "Cancelled - Client Request".equals(name)
                || "Cancelled - Requirements Infeasible".equals(name)) {

            if (format == null) {
                format = new SimpleDateFormat(ConfigHelper.getDateFormat());
            }

            project.setProperty("Completion Timestamp", format.format(new Date()));

            if (!"Deleted".equals(name) && !ActionsHelper.isStudioProject(project)) {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
                    conn = dbconn.createConnection();
                    ps = conn.prepareStatement(
                            "update project_result set rating_ind = 1 where project_id = ? and valid_submission_ind = 1");
                    ps.setLong(1, project.getId());
                    ps.execute();
                } catch(SQLException e) {
                    throw new BaseException("Failed to update project result for rating_ind", e);
                } catch (UnknownConnectionException e) {
                    throw new BaseException("Failed to return DBConnection", e);
                } catch (ConfigurationException e) {
                    throw new BaseException("Failed to return DBConnection", e);
                } finally {
                    close(ps);
                    close(conn);
                }
            }
        }
    }

    /**
     * Set Rated Timestamp with the end date of submission phase.
     *
     * @param project the project instance
     * @param format the date format
     */
    static void setProjectRatingDate(Project project, Phase[] projectPhases, Format format) {
        Date endDate = null;
        for (int i = 0; projectPhases != null && i < projectPhases.length; i++) {
            if ("Submission".equals(projectPhases[i].getPhaseType().getName())) {
                endDate = projectPhases[i].getActualEndDate();
                if (endDate == null) {
                    endDate = projectPhases[i].getScheduledEndDate();
                }
                break;
            }
        }

        if (endDate == null) {
            return;
        }

        if (format == null) {
            format = new SimpleDateFormat(ConfigHelper.getDateFormat());
        }

        project.setProperty("Rated Timestamp", format.format(endDate));
    }

    /**
     * This method verifies the request for certain conditions to be met. This includes verifying if
     * the user has specified an ID of the project he wants to perform an operation on, if the ID of
     * the project specified by user denotes existing project, and whether the user has rights to
     * perform the operation specified by <code>permission</code> parameter.
     *
     * Eligibility checks:
     * - If there is no logged in user and the project has eligibility constraints, ask for login.
     * - If the user is logged in, is not a resource of the project and the project has eligibility constraints,
     *   don't allow him access.
     *
     * @return an instance of the {@link CorrectnessCheckResult} class, which specifies whether the
     *         check was successful and, in the case it was, contains additional information
     *         retrieved during the check operation, which might be of some use for the calling
     *         method.
     * @param mapping
     *            action mapping.
     * @param request
     *            the http request.
     * @param permission
     *            permission to check against, or <code>null</code> if no check is required.
     * @throws BaseException
     *             if any error occurs.
     */
    public static CorrectnessCheckResult checkForCorrectProjectId(ActionMapping mapping, MessageResources resources,
            HttpServletRequest request, String permission, boolean getRedirectUrlFromReferer)
        throws BaseException {
        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        if (permission == null || permission.trim().length() == 0) {
            permission = null;
        }

        // Verify that Project ID was specified and denotes correct project
        String pidParam = request.getParameter("pid");
        if (pidParam == null || pidParam.trim().length() == 0) {
            result.setForward(produceErrorReport(
                    mapping, resources, request, permission, "Error.ProjectIdNotSpecified", null));
            // Return the result of the check
            return result;
        }

        long pid;

        try {
            // Try to convert specified pid parameter to its integer representation
            pid = Long.parseLong(pidParam, 10);
        } catch (NumberFormatException nfe) {
            result.setForward(produceErrorReport(
                    mapping, resources, request, permission, "Error.ProjectNotFound", null));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Project Manager
        ProjectManager projMgr = createProjectManager(request);
        // Get Project by its id
        Project project = projMgr.getProject(pid);
        // Verify that project with given ID exists
        if (project == null) {
            result.setForward(produceErrorReport(
                    mapping, resources, request, permission, "Error.ProjectNotFound", null));
            // Return the result of the check
            return result;
        }

        // Store Project object in the result bean
        result.setProject(project);
        // Place project as attribute in the request
        request.setAttribute("project", project);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, pid);

        request.setAttribute("isAdmin",
                Boolean.valueOf(AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)
                        || AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)
                        || AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)));

        // If permission parameter was not null or empty string ...
        if (permission != null) {
            // ... verify that this permission is granted for currently logged in user
            if (!AuthorizationHelper.hasUserPermission(request, permission)) {
                // If it does not, and the user is logged in, display a message about the lack of
                // permissions, otherwise redirect the request to the Login page
                result.setForward(produceErrorReport(mapping, resources, request,
                        permission, "Error.NoPermission", Boolean.valueOf(getRedirectUrlFromReferer)));
                // Return the result of the check
                return result;
            }
        }

        // new eligibility constraints checks
        try {
            if (AuthorizationHelper.isUserLoggedIn(request)) {

                // if the user is logged in and is a resource of this project or a global manager, continue
                Resource[] myResources = (Resource[]) request.getAttribute("myResources");
                if ((myResources == null || myResources.length == 0) &&
                               !AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME) &&
                               !AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)) {
                    // if he's not a resource, check if the project has eligibility constraints
                    if (EJBLibraryServicesLocator.getContestEligibilityService().hasEligibility(pid, false)) {
                        result.setForward(produceErrorReport(
                                mapping, resources, request, permission, "Error.ProjectNotFound", null));
                        // Return the result of the check
                        return result;
                    }
                }
            } else {
                // if the user is not logged in and the project has any eligibility constraint, ask for login
                if (EJBLibraryServicesLocator.getContestEligibilityService().hasEligibility(pid, false)) {
                    result.setForward(produceErrorReport(mapping, resources, request,
                            permission, "Error.NoPermission", Boolean.valueOf(getRedirectUrlFromReferer)));
                    // Return the result of the check
                    return result;
                }
            }
        } catch (Exception e) {
            throw new BaseException("It was not possible to verify eligibility for project id " + pid, e);
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        return result;
    }

    /**
     * Populate project_result and component_inquiry for new submitters.
     *
     * @param project the project
     * @param newSubmitters new submitters external ids.
     * @throws BaseException if error occurs
     */
    public static void populateProjectResult(Project project, Collection<Long> newSubmitters) throws BaseException {
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement existStmt = null;
        PreparedStatement existCIStmt = null;
        PreparedStatement ratingStmt = null;
        PreparedStatement reliabilityStmt = null;
        PreparedStatement componentInquiryStmt = null;
        long categoryId = project.getProjectCategory().getId();

        if (!isProjectResultCategory(categoryId)) {
            return;
        }

        try {
            DBConnectionFactory dbconn;
                dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            conn = dbconn.createConnection();
            log.log(Level.INFO,
                    "create db connection with default connection name from DBConnectionFactoryImpl with namespace:" + DB_CONNECTION_NAMESPACE);
            long projectId = project.getId();
            // retrieve and update component_inquiry_id
            long componentInquiryId = getNextComponentInquiryId(conn, newSubmitters.size());
            long componentId = getProjectLongValue(project, "Component ID");
            long phaseId = 111 + project.getProjectCategory().getId();
            log.log(Level.DEBUG, "calculated phaseId for Project: " + projectId + " phaseId: " + phaseId);
            long version = getProjectLongValue(project, "Version ID");

            // add reliability_ind and old_reliability
            ps = conn.prepareStatement("INSERT INTO project_result " +
                    "(project_id, user_id, rating_ind, valid_submission_ind, old_rating, old_reliability) " +
                    "values (?, ?, ?, ?, ?, ?)");

            componentInquiryStmt = conn.prepareStatement("INSERT INTO component_inquiry " +
                    "(component_inquiry_id, component_id, user_id, project_id, phase, tc_user_id, agreed_to_terms, rating, version, create_time) " +
                    "values (?, ?, ?, ?, ?, ?, 1, ?, ?, current)");

            existStmt = conn.prepareStatement("SELECT 1 FROM PROJECT_RESULT WHERE user_id = ? and project_id = ?");

            existCIStmt = conn.prepareStatement("SELECT 1 FROM component_inquiry WHERE user_id = ? and project_id = ?");

            ratingStmt = conn.prepareStatement("SELECT rating, phase_id, (select project_category_id from project where project_id = ?) as project_category_id from user_rating where user_id = ? ");

            reliabilityStmt = conn.prepareStatement("SELECT rating from user_reliability where user_id = ? and phase_id = " +
                    "(select 111+project_category_id from project where project_id = ?)");

            for (Long userId : newSubmitters) {
                // Check if projectResult exist
                existStmt.clearParameters();
                existStmt.setLong(1, userId);
                existStmt.setLong(2, projectId);
                boolean existPR = existStmt.executeQuery().next();

                // Check if component_inquiry exist
                existCIStmt.clearParameters();
                existCIStmt.setLong(1, userId);
                existCIStmt.setLong(2, projectId);
                boolean existCI = existCIStmt.executeQuery().next();

                // Retrieve oldRating
                double oldRating = 0;
                ResultSet rs = null;
                if (!existPR || !existCI) {
                    ratingStmt.clearParameters();
                    ratingStmt.setLong(1, projectId);
                    ratingStmt.setLong(2, userId);
                    rs = ratingStmt.executeQuery();

                    // If the project belongs to a rated category, the user gets the rating that belongs to the
                    // category.  Otherwise, the highest available rating is used.
                    while (rs.next()) {
                        if (!isRatedCategory(rs.getLong(3))) {
                            if (oldRating < rs.getLong(1)) {
                                oldRating = rs.getLong(1);
                            }
                        } else if (rs.getLong(3) + 111 == rs.getLong(2)) {
                            oldRating = rs.getLong(1);
                        }
                    }
                    close(rs);
                }


                double oldReliability = 0;
                if (!existPR) {
                    //Retrieve Reliability
                    reliabilityStmt.clearParameters();
                    reliabilityStmt.setLong(1, userId);
                    reliabilityStmt.setLong(2, projectId);
                    rs = reliabilityStmt.executeQuery();

                    if (rs.next()) {
                        oldReliability = rs.getDouble(1);
                    }
                    close(rs);

                    //add project_result
                    ps.setLong(1, projectId);
                    ps.setLong(2, userId);
                    ps.setLong(3, 0);
                    ps.setLong(4, 0);

                    if (oldRating == 0) {
                        ps.setNull(5, Types.DOUBLE);
                    } else {
                        ps.setDouble(5, oldRating);
                    }

                    if (oldReliability == 0) {
                        ps.setNull(6, Types.DOUBLE);
                    } else {
                        ps.setDouble(6, oldReliability);
                    }
                    ps.addBatch();
                }

                // add component_inquiry
                if (!existCI && componentId > 0) {
                    log.log(Level.INFO, "adding component_inquiry for projectId: " + projectId + " userId: " + userId);
                    componentInquiryStmt.setLong(1, componentInquiryId++);
                    componentInquiryStmt.setLong(2, componentId);
                    componentInquiryStmt.setLong(3, userId);
                    componentInquiryStmt.setLong(4, projectId);
                    // All competition types except for design and development should have null phase id.
                    if (categoryId == 1 || categoryId == 2)  {
                        componentInquiryStmt.setLong(5, phaseId);
                    } else {
                        componentInquiryStmt.setNull(5, Types.INTEGER);
                    }
                    componentInquiryStmt.setLong(6, userId);
                    componentInquiryStmt.setDouble(7, oldRating);
                    componentInquiryStmt.setLong(8, version);
                    componentInquiryStmt.addBatch();
                }
            }
            ps.executeBatch();
            componentInquiryStmt.executeBatch();
        } catch (UnknownConnectionException e) {
            throw new BaseException("Failed to create connection", e);
        } catch (ConfigurationException e) {
            throw new BaseException("Failed to config for DBNamespace", e);
        } catch (SQLException e) {
            throw new BaseException("Failed to populate project_result", e);
        } catch (DBConnectionException e) {
            throw new BaseException("Failed to return DBConnection", e);
        } finally {
            close(componentInquiryStmt);
            close(ps);
            close(existStmt);
            close(existCIStmt);
            close(ratingStmt);
            close(reliabilityStmt);
            close(conn);
        }
    }


    /**
     * Synchronizes rboard_application with reviewers set in the OR.
     *
     * @param project the project
     * @throws BaseException if error occurs
     */
    public static void synchronizeRBoardApplications(Project project) throws BaseException {
        long projectId = project.getId();
        long phaseID = 111 + project.getProjectCategory().getId();

        log.log(Level.INFO,"synchronizeRBoardApplications projectId= " + projectId);

        Connection conn = null;
        Statement resourceSelectStmt = null;
        ResultSet resourceResultSet = null;
        try {

            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            conn = dbconn.createConnection();
            log.log(Level.INFO, "create db connection with default connection name from DBConnectionFactoryImpl with namespace:" + DB_CONNECTION_NAMESPACE);

            resourceSelectStmt = conn.createStatement();

            // The query to select all reviewer roles from the resources.
            // Actually, only the following roles get selected: Primary Screener, Reviewer, Accuracy, Failure, Stress, Aggregator, Final Reviewer
            resourceResultSet = resourceSelectStmt.executeQuery(
                    "SELECT resource_info.value, resource.resource_role_id, resource.create_date FROM resource, resource_info WHERE " +
                            "resource.project_id = "+projectId+" AND resource.resource_id = resource_info.resource_id AND " +
                            "resource.resource_role_id in (2,4,5,6,7,8,9) AND resource_info.resource_info_type_id=1");

            Map<Long,Long> roles = new HashMap<Long,Long>();
            Set<Long> primaries = new HashSet<Long>();
            Map<Long,java.sql.Timestamp> createDates = new HashMap<Long,java.sql.Timestamp>();
            while (resourceResultSet.next()) {
                long userID = resourceResultSet.getLong(1);
                long role = resourceResultSet.getLong(2);
                java.sql.Timestamp create_date = resourceResultSet.getTimestamp(3);

                // Role 4 for Reviewer, 5 for Accuracy Reviewer, 6 for Failure Reviewer and 7 for Stress Reviewer
                if (role==4 || role==5 || role==6 || role==7) {
                    roles.put(userID,role);
                    createDates.put(userID,create_date);
                }
                else
                    // Other roles could be 2 for Primary Screener, 8 for Aggregator and 9 for Final Reviewer
                    primaries.add(userID);
            }

            // Select at most three reviewers to be added into the rboard_application table.
            List<Long> newReviewers = selectReviewers(roles, primaries);

            List<Long> newPrimaries = new ArrayList<Long>();
            List<java.sql.Timestamp> newCreateDates = new ArrayList<java.sql.Timestamp>();
            List<Long> newRoles = new ArrayList<Long>();

            boolean primarySelected=false;
            for (Long reviewerID : newReviewers) {
                newRoles.add(roles.get(reviewerID));
                newCreateDates.add(createDates.get(reviewerID));

                // The first reviewer with a primary flag will be selected as a primary, other reviewers won't.
                if (!primarySelected && primaries.contains(reviewerID)) {
                    newPrimaries.add(1L);
                    primarySelected=true;
                }
                else
                    newPrimaries.add(0L);
            }
            List<Long> newResponseIDs = getRespIdFromRoleId(conn, newRoles, newPrimaries, phaseID);

            // Clear all entries from the rboard_application for the project.
            clearRBoardApplication(conn, project);

            // Add reviewers to the rboard_application.
            addRBoardApplications(conn, project, newReviewers, newResponseIDs, newPrimaries, newCreateDates);

            // Finally, add specification reviewer to the rboard_application if present
            addSpecReviewer(conn, project);

        } catch (UnknownConnectionException e) {
            throw new BaseException("Failed to create connection", e);
        } catch (ConfigurationException e) {
            throw new BaseException("Failed to config for DBNamespace", e);
        } catch (DBConnectionException e) {
            throw new BaseException("Failed to return DBConnection", e);
        } catch (SQLException e) {
            log.log(Level.WARN, "Failed to read from resource and resource_info table " + e);
        } finally {
            close(resourceResultSet);
            close(resourceSelectStmt);
            close(conn);
        }

    }

    /**
     * This private helper method selects reviewers to be inserted into rboard_application table.
     * No more that three reviewers can be selected.
     *
     * @param roles Maps user id to role id.
     * @param primaries Set of user ids that can be primaries.
     * @return List of user ids selected to be added to the rboard_application table. Can not have more than three elements.
     */
    private static List<Long> selectReviewers(Map<Long,Long> roles, Set<Long> primaries) {

        Set<Long> result = new HashSet<Long>();
        Set<Long> selectedRoles = new HashSet<Long>();

        // First we add a primary if present.
        for (Long reviewerID : roles.keySet()) {
            if (primaries.contains(reviewerID)) {
                result.add(reviewerID);
                selectedRoles.add(roles.get(reviewerID));
                break;
            }
        }

        // We try to select users with different roles (e.g. not to select two accuracy reviewers and forget about stress one).
        // This accounts for a pathological cases when there are more than one accuracy, stress or failure reviewers.
        for (Long reviewerID : roles.keySet()) {
            if (result.size()==3) break;

            Long role = roles.get(reviewerID);
            if (!result.contains(reviewerID) && !selectedRoles.contains(role)) {
                result.add(reviewerID);
                selectedRoles.add(role);
            }

        }

        // If we still don't have three reviewers, add the rest.
        for (Long reviewerID : roles.keySet()) {
            if (result.size()==3) break;
            if (!result.contains(reviewerID))
                result.add(reviewerID);
        }

        // Return the result as a list.
        return Arrays.asList(result.toArray(new Long[0]));
    }

    /**
     * This private helper method clears rboard_application table for the specified project.
     *
     * @param conn DB connection to be used.
     * @param project the project
     * @throws BaseException if error occurs
     */
    private static void clearRBoardApplication(Connection conn, Project project) throws BaseException {
        long projectId = project.getId();

        log.log(Level.INFO,"clearRBoardApplication projectId= " + projectId);

        PreparedStatement deleteStmt = null;
        try {
            deleteStmt = conn.prepareStatement("DELETE FROM rboard_application WHERE project_id=?");

            deleteStmt.setLong(1, projectId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new BaseException("Failed to clear rboard_application", e);
        } finally {
            close(deleteStmt);
        }
    }

    /**
     * This private helper method adds entries into the rboard_application table for the specified project.
     *
     * @param conn DB connection to be used.
     * @param project the project
     * @param reviewerIDs List of user ids to be added as reviewers.
     * @param responseIDs List of response ids.
     * @param primaries List of flags indicating the primary reviewer. The list can not have more than one 1, other values are all 0.
     * @param createDates List of timestamps when reviewers were added to the OR.
     * @throws BaseException if error occurs.
     */
    private static void addRBoardApplications(Connection conn, Project project,
                                             List<Long> reviewerIDs, List<Long> responseIDs,
                                             List<Long> primaries, List<java.sql.Timestamp> createDates) throws BaseException {
        long projectId = project.getId();
        long phaseId = 111 + project.getProjectCategory().getId();

        log.log(Level.INFO,"addRBoardApplication projectId= " + projectId);

        PreparedStatement addStmt = null;
        try {
            addStmt = conn.prepareStatement("INSERT INTO rboard_application VALUES (?, ?, ?, ?, ?, ?, current)");

            for (int i=0; i < reviewerIDs.size(); ++i) {
                addStmt.setLong(1, reviewerIDs.get(i));
                addStmt.setLong(2, projectId);
                addStmt.setLong(3, phaseId);
                addStmt.setLong(4, responseIDs.get(i));
                addStmt.setLong(5, primaries.get(i));
                addStmt.setTimestamp(6,createDates.get(i));
                addStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BaseException("Failed to populate rboard_application", e);
        } finally {
            close(addStmt);
        }
    }

    /**
     * This private helper method convert role ids (from the OR) to response ids (from the rboard_application table).
     *
     * @param conn DB connection to be used.
     * @param roles List of role ids to be converted to response ids.
     * @param phaseID phase id.
     * @return responseIDs List of response ids.
     * @throws BaseException if error occurs.
     */
    private static List<Long> getRespIdFromRoleId(Connection conn, List<Long> roles, List<Long> primaries, long phaseID) throws BaseException {
        List<Long> responseIDs = new ArrayList<Long>();

        log.log(Level.INFO,"getRespIdFromRoleId phaseID= " + phaseID);

        // For component development projects, response ids correspond to accuracy, stress and failure reviewer roles.
        if (phaseID == 113) {
            for(long roleID : roles) {
                // Accuracy
                if (roleID == 5)
                    responseIDs.add(3L); else

                // Failure
                    if (roleID == 6)
                    responseIDs.add(2L); else

                // Stress
                if (roleID == 7)
                    responseIDs.add(1L); else

                // Otherwise add him as Accuracy.
                // Should not normally happen, but is possible if a Reviewer is added to a dev project.
                responseIDs.add(3L);
            }
        }

        // For other projects, response ids all correspond to Reviewer role.
        if (phaseID != 113) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                // Due to a strange behavior of the RBoardApplication bean the primary reviewer should always get a specific response id.
                // For all tracks we have so far, the primary response id is the smallest response id (except for
                // component development track for which it corresponds to the failure reviewer).
                // So, we retrieve all response ids for the specified track ordered by its value.
                // The first one will be the primary then.
                ps = conn.prepareStatement("select review_resp_id from review_resp where phase_id = ? order by review_resp_id");
                ps.setLong(1, phaseID);

                long primaryResponseId=-1L;
                List<Long> secondaryResponseIDs = new ArrayList<Long>();

                rs = ps.executeQuery();
                while (rs.next()) {
                    if (primaryResponseId == -1L)
                        primaryResponseId = rs.getLong(1);
                    else
                        secondaryResponseIDs.add(rs.getLong(1));
                }

                // Iterate by all input reviewers and set the response id for each.
                for(long primary : primaries) {

                    // Assign the primary response id only to the primary reviewer or if all the secondary response ids are already assigned.
                    if (primary == 1L || (primary == 0L && secondaryResponseIDs.size() == 0))
                        if (primaryResponseId != -1L) {
                            responseIDs.add(primaryResponseId);
                            primaryResponseId = -1L;
                            continue;
                        }

                    // Assign the secondary response id only to a secondary reviewer or if the primary response id is already assigned.
                    if (primary == 0L || (primary == 1L && primaryResponseId == -1L))
                        if (secondaryResponseIDs.size() > 0) {
                            responseIDs.add(secondaryResponseIDs.get(secondaryResponseIDs.size()-1));
                            secondaryResponseIDs.remove(secondaryResponseIDs.size()-1);
                            continue;
                        }
                }

            } catch (SQLException e) {
                throw new BaseException("Failed to getRespIdFromRoleId", e);
            } finally {
                close(ps);
                close(rs);
            }

            // If there are less response ids in the review_resp_id than we need, throw an exception.
            // This can only mean that review_resp_id has wrong data as it needs to have at least three response ids.
            if (responseIDs.size() < roles.size()) {
                throw new BaseException("Not enough response ids for reviewers. Needed "+roles.size()+", present "+responseIDs.size());
            }
        }

        return responseIDs;
    }

    /**
     * This private helper method adds Specification Reviewer resource into the rboard_application table for the specified project.
     *
     * @param conn DB connection to be used.
     * @param project the project
     * @throws BaseException if error occurs.
     */
    private static void addSpecReviewer(Connection conn, Project project) throws BaseException {
        long projectId = project.getId();
        // 1111 is the offset for specification review.
        long specReviewPhaseId = 1111 + project.getProjectCategory().getId();

        log.log(Level.INFO,"addSpecReviewer projectId= " + projectId);

        PreparedStatement addStmt = null;
        PreparedStatement resourceSelectStmt = null;
        ResultSet resourceResultSet = null;
        try {
            // Select specification reviewer(s) for the project and order them by the create_date
            resourceSelectStmt = conn.prepareStatement(
                    "SELECT resource_info.value, resource.create_date FROM resource, resource_info WHERE " +
                            "resource.project_id = ? AND resource.resource_id = resource_info.resource_id AND " +
                            "resource.resource_role_id=18 AND resource_info.resource_info_type_id=1 " +
                            "order by resource.create_date");

            resourceSelectStmt.setLong(1, projectId);
            resourceResultSet = resourceSelectStmt.executeQuery();
            // If there is more than one Specification Reviewer resource we pick only the first one, which is the one
            // with the smallest create date.
            if (resourceResultSet.next()) {
                long userID = resourceResultSet.getLong(1);
                java.sql.Timestamp create_date = resourceResultSet.getTimestamp(2);

                // We retrieve review_resp_id from the review_resp table by the phase_id in the inner query below.
                addStmt = conn.prepareStatement(
                        "insert into rboard_application values (?, ?, ?, " +
                        "(select min(review_resp_id) from review_resp where resource_role_id=18 and phase_id=?), " +
                        "0, ?, current)");

                addStmt.setLong(1, userID);
                addStmt.setLong(2, projectId);
                addStmt.setLong(3, specReviewPhaseId);
                addStmt.setLong(4, specReviewPhaseId);
                addStmt.setTimestamp(5, create_date);
                addStmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new BaseException("Failed to populate rboard_application for the specification reviewer", e);
        } finally {
            close(resourceResultSet);
            close(resourceSelectStmt);
            close(addStmt);
        }
    }

    /**
     * Recalculate Screening reviewers payment.
     *
     * @param projectId project id
     *
     * @throws Exception if error occurs
     */
    public static void recaculateScreeningReviewerPayments(long projectId) throws BaseException {
        Connection conn = null;
        try {
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            conn = dbconn.createConnection();
            log.log(Level.INFO,
                    "create db connection with default connection name from DBConnectionFactoryImpl with namespace:"
                    + DB_CONNECTION_NAMESPACE);
            AutoPaymentUtil.populateReviewerPayments(projectId, conn, AutoPaymentUtil.SCREENING_PHASE);
        } catch (DBConnectionException e) {
            throw new BaseException("Failed to return DBConnection", e);
        } catch (SQLException e) {
            throw new BaseException("Failed to recaculateScreeningReviewerPayments for project " + projectId, e);
        } finally {
            close(conn);
        }
    }

    /**
     * Get the catalog for a component.
     *
     * @param componentId project id
     *
     * @throws Exception if error occurs
     */
    public static String getRootCategoryIdByComponentId(Object componentId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            conn = dbconn.createConnection();
            log.log(Level.INFO,
                    "create db connection with default connection name from DBConnectionFactoryImpl with namespace:"
                    + DB_CONNECTION_NAMESPACE);
            String sqlStr = "select root_category_id " +
                            "    from comp_catalog cc," +
                            "         categories pcat " +
                            "    where cc.component_id = ? " +
                            "    and cc.status_id = 102 " +
                            "    and pcat.category_id = cc.root_category_id";
            ps = conn.prepareStatement(sqlStr);
            ps.setString(1, componentId.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("root_category_id");
            }
        } catch (Exception e) {
            // Ignore if no corresponding root_category_id exist
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return "9926572"; // If we can't find a catalog, assume it's an Application
    }

    /**
     * Retrieve all default scorecards.
     *
     * @throws Exception if error occurs
     */
    public static List<DefaultScorecard> getDefaultScorecards() throws BaseException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            conn = dbconn.createConnection();
            log.log(Level.INFO,
                    "create db connection with default connection name from DBConnectionFactoryImpl with namespace:"
                    + DB_CONNECTION_NAMESPACE);
            String sqlString = "select ds.*, st.name from default_scorecard ds, scorecard_type_lu st " +
                    "where ds.scorecard_type_id = st.scorecard_type_id";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlString);
            List<DefaultScorecard> list = new ArrayList<DefaultScorecard>();
            while (rs.next()) {
                DefaultScorecard scorecard = new DefaultScorecard();
                scorecard.setCategory(rs.getInt("project_category_id"));
                scorecard.setScorecardType(rs.getInt("scorecard_type_id"));
                scorecard.setScorecardId(rs.getLong("scorecard_id"));
                scorecard.setName(rs.getString("name"));
                list.add(scorecard);
            }
            return list;
        } catch (DBConnectionException e) {
            throw new BaseException("Failed to return DBConnection", e);
        } catch (SQLException e) {
            throw new BaseException("Failed to retrieve default scorecard", e);
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
    }

    /**
     * Delete project_result and component_inquiry for new submitters if oldRole is submitter, added otherwise.
     *
     * @param project the project
     * @param userId userId
     * @param oldRoleId roleId
     * @throws BaseException if error occurs
     */
    public static void changeResourceRole(Project project, long userId, long oldRoleId, long newRoleId) throws BaseException {
        long categoryId = project.getProjectCategory().getId();

        if (isProjectResultCategory(categoryId)) {
            if (oldRoleId == 1) {
                // Delete project_result if the old role is submitter
                deleteProjectResult(project, userId, oldRoleId);
            }

            if (newRoleId == 1) {
                // added otherwise
                populateProjectResult(project, Arrays.asList(new Long[] { userId }));
            }
        }
    }

    /**
     * Delete project_result and component_inquiry for new submitters.
     *
     * @param project the project
     * @param userId userId
     * @param roleId roleId
     * @throws BaseException if error occurs
     */
    public static void deleteProjectResult(Project project, long userId, long roleId) throws BaseException {
        Connection conn = null;
        PreparedStatement ps = null;
        long categoryId = project.getProjectCategory().getId();

        if (!isProjectResultCategory(categoryId)) {
            return;
        }

        if (roleId != 1) {
            // Only deal with submitters
            return;
        }

        try {
            DBConnectionFactory dbconn;
                dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            conn = dbconn.createConnection();

            log.log(Level.INFO,
                    "create db connection with default connection name from DBConnectionFactoryImpl with namespace:"
                    + DB_CONNECTION_NAMESPACE);

            // delete from project_result
            ps = conn.prepareStatement("delete from project_result where project_id = ? and user_id = ?");
            ps.setLong(1, project.getId());
            ps.setLong(2, userId);
            ps.executeUpdate();
            close(ps);

            // delete from component_inquiry
            ps = conn.prepareStatement("delete from component_inquiry where project_id = ? and user_id = ?");
            ps.setLong(1, project.getId());
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (UnknownConnectionException e) {
            throw new BaseException("Failed to create connection", e);
        } catch (ConfigurationException e) {
            throw new BaseException("Failed to config for DBNamespace", e);
        } catch (SQLException e) {
            throw new BaseException("Failed to delete from project_result or component_inquiry", e);
        } catch (DBConnectionException e) {
            throw new BaseException("Failed to return DBConnection", e);
        } finally {
            close(ps);
            close(conn);
        }
    }

    /**
     * Reset ProjectResult With ChangedScores.
     *
     * @param projectId project id
     * @param userId userId
     *
     * @throws Exception if error occurs
     */
    public static void resetProjectResultWithChangedScores(long projectId, Object userId) throws BaseException {
        Connection conn = null;
        try {
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            conn = dbconn.createConnection();
            log.log(Level.INFO,
                    "create db connection with default connection name from DBConnectionFactoryImpl with namespace:"
                    + DB_CONNECTION_NAMESPACE);
            PRHelper.resetProjectResultWithChangedScores(projectId, userId, conn);
        } catch (DBConnectionException e) {
            throw new BaseException("Failed to return DBConnection", e);
        } catch (SQLException e) {
            throw new BaseException("Failed to resetProjectResultWithChangedScores for project " + projectId, e);
        } finally {
            close(conn);
        }
    }

    /**
     * Gets version from comp_versions table
     *
     * @param componentVersionId the component version id
     *
     * @throws Exception if error occurs
     */
    public static int getVersionUsingComponentVersionId(long componentVersionId) throws BaseException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            conn = dbconn.createConnection();
            log.log(Level.INFO,
                    "create db connection with default connection name from DBConnectionFactoryImpl with namespace:"
                    + DB_CONNECTION_NAMESPACE);

            String sqlString = "select version from comp_versions where comp_vers_id = ?";

            ps = conn.prepareStatement(sqlString);
            ps.setLong(1, componentVersionId);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        return rs.getInt("version");
                    }

                    return 0;
        } catch (DBConnectionException e) {
            throw new BaseException("Failed to return DBConnection", e);
        } catch (SQLException e) {
            throw new BaseException("Failed to retrieve version for " + componentVersionId, e);
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }
    }

    /**
     * Gets the scorecard minimum score from the given review.
     *
     * @param scorecardManager ScorecardManager instance.
     * @param review Review instance.
     *
     * @return the scorecard minimum score from the given review.
     *
     * @throws Exception if error occurs
     */
    static float getScorecardMinimumScore(ScorecardManager scorecardManager, Review review)
        throws BaseException {
        long scorecardId = review.getScorecard();

        try {
            Scorecard[] scoreCards = scorecardManager.getScorecards(new long[]{scorecardId}, false);
            if (scoreCards.length == 0) {
                throw new BaseException("No scorecards found for scorecard id: " + scorecardId);
            }
            Scorecard scoreCard = scoreCards[0];

            return scoreCard.getMinScore();
        } catch (PersistenceException e) {
            throw new BaseException("Problem with scorecard retrieval", e);
        }
    }

    /**
     * utility method to get a SubmissionStatus object for the given status name.
     *
     * @param request request instance to use for searching.
     * @param statusName submission status name.
     *
     * @return a SubmissionStatus object for the given status name.
     *
     * @throws BaseException if submission status could not be found.
     */
    static SubmissionStatus getSubmissionStatus(HttpServletRequest request, String statusName)
        throws BaseException {
        UploadManager upMgr = ActionsHelper.createUploadManager(request);
        SubmissionStatus[] statuses = null;
        try {
            statuses = upMgr.getAllSubmissionStatuses();
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Error finding submission status with name: " + statusName, e);
        }
        for (int i = 0; i < statuses.length; i++) {
            if (statusName.equals(statuses[i].getName())) {
                return statuses[i];
            }
        }
        throw new BaseException("Could not find submission status with name: " + statusName);
    }

    /**
     * retrieves all Reviewed submissions for the given project id.
     *
     * @param request request instance to use for searching.
     * @param project project.
     *
     * @return all active submissions for the given project id.
     *
     * @throws Exception if error occurs
     */
    static Submission[] searchReviewedContestSubmissions(HttpServletRequest request, Project project)
        throws BaseException {
        UploadManager upMgr = ActionsHelper.createUploadManager(request);

        SubmissionType[] allSubmissionTypes = upMgr.getAllSubmissionTypes();

        SubmissionType contestSubmissionType
            = ActionsHelper.findSubmissionTypeByName(allSubmissionTypes, "Contest Submission");

        //first get submission status id for "Active" status
        Filter submissionStatusFilter = new InFilter("submission_status_id",
                Arrays.asList(new Long[] {new Long(1), new Long(3), new Long(4)}));

        //then search for submissions
        Filter projectIdFilter = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
        Filter typeFilter = SubmissionFilterBuilder.createSubmissionTypeIdFilter(contestSubmissionType.getId());
        Filter fullFilter = new AndFilter(Arrays.asList(projectIdFilter, typeFilter, submissionStatusFilter));

        try {
            return upMgr.searchSubmissions(fullFilter);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("There was a submission retrieval error", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("There was a search builder error", e);
        }
    }

    /**
     * Retrieve and update next ComponentInquiryId.
     *
     * @param conn the connection
     * @param count the count of new submitters
     * @return next component_inquiry_id
     */
    private static long getNextComponentInquiryId(Connection conn, int count) throws BaseException {
        String tableName = ConfigHelper.getPropertyValue("component_inquiry.tablename", "sequence_object");
        String nameField = ConfigHelper.getPropertyValue("component_inquiry.name", "name");
        String currentValueField = ConfigHelper.getPropertyValue("component_inquiry.current_value", "current_value");
        String getNextID = "SELECT max(" + currentValueField + ") FROM " + tableName +
                    " WHERE " + nameField + " = 'main_sequence'";
        String updateNextID = "UPDATE " + tableName + " SET " + currentValueField + " = ? " +
                    " WHERE " + nameField + " = 'main_sequence'" + " AND " + currentValueField + " = ? ";
        PreparedStatement getNextIDStmt = null;
        PreparedStatement updateNextIDStmt = null;
        ResultSet rs = null;

        try {
            getNextIDStmt = conn.prepareStatement(getNextID);
            updateNextIDStmt = conn.prepareStatement(updateNextID);
            while (true) {
                rs = getNextIDStmt.executeQuery();
                rs.next();
                long currentValue = rs.getLong(1);

                // Update the next value
                updateNextIDStmt.clearParameters();
                updateNextIDStmt.setLong(1, currentValue + count);
                updateNextIDStmt.setLong(2, currentValue);
                int ret = updateNextIDStmt.executeUpdate();
                if (ret > 0) {
                    return currentValue;
                }
            }
        } catch (SQLException e) {
            throw new BaseException("Failed to retrieve next component_inquiry_id", e);
        } finally {
            close(rs);
            close(getNextIDStmt);
            close(updateNextIDStmt);
        }
    }

    /**
     * Return project property long value.
     *
     * @param project the project object
     * @param name the property name
     * @return the long value, 0 if it does not exist
     */
    private static long getProjectLongValue(Project project, String name) {
        Object obj = project.getProperty(name);
        if (obj == null) {
            return 0;
        } else {
            return Long.parseLong(obj.toString());
        }
    }

    /**
     * Close a JDBC Connection.
     *
     * @param connection JDBC Connection to close.
     */
    private static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                log.log(Level.INFO, "close the connection.");
            } catch (SQLException e) {
                log.log(Level.ERROR, "Error closing JDBC Connection: " + e.getMessage());
            }
        }
    }

    /**
     * Close a JDBC Statement.
     *
     * @param statement JDBC Statement to close.
     */
    private static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                log.log(Level.ERROR, "Error closing JDBC Statement: " + e.getMessage());
            }
        }
    }

    /**
     * Close a JDBC ResultSet.
     *
     * @param resultSet JDBC ResultSet to close.
     */
    private static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.log(Level.ERROR, "Error closing JDBC ResultSet: " + e.getMessage());
            }
        }
    }

    public static ActionForward findForwardNotAuthorized(ActionMapping mapping, Long projectId) {
        if (projectId != null && projectId.longValue() > 0) {
            ActionRedirect redirect = new ActionRedirect(mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME));
            redirect.addParameter("pid", projectId);
            redirect.addParameter("redirectToProjectID", projectId);
            return redirect;
        } else {
            return mapping.findForward(Constants.NOT_AUTHORIZED_FORWARD_NAME);
        }
    }

    /**
     * Returns true if the given project is of category studio.
     *
     * @param project
     *            the project to be be checked.
     * @return true, if the project is of type studio.
     */
    public static boolean isStudioProject(Project project) {
        return "Studio".equals(project.getProjectCategory().getProjectType().getName());
    }

    private static Forums getForumBean() throws RemoteException, CreateException, NamingException {
        Context context = TCContext.getInitial(ApplicationServer.FORUMS_HOST_URL);
        ForumsHome forumsHome = (ForumsHome) context.lookup(ForumsHome.EJB_REF_NAME);
        return forumsHome.create();
//        return EJBLibraryServicesLocator.getForumsService();
    }

    public static void addForumPermissions(Project project, Collection<Long> users) throws BaseException {
        addForumPermissions(project, users, false);
    }


    public static void addForumPermissions(Project project, Collection<Long> users, boolean moderator) throws BaseException {
        try {
            Forums forumBean = getForumBean();


            String roleId = SOFTWARE_USER_FORUM_ROLE_PREFIX + getProjectLongValue(project, "Developer Forum ID");

            if (moderator)
            {
                roleId = SOFTWARE_MODERATOR_FORUM_ROLE_PREFIX + getProjectLongValue(project, "Developer Forum ID");
            }

            for (Long userId : users) {
                forumBean.assignRole(userId, roleId);
            }
        } catch (Exception e) {
            throw new BaseException("Error adding forum permissions for project id " + project.getId(), e);
        }
    }

    public static void addForumPermissions(Project project, Long user) throws BaseException {
        addForumPermissions(project, userToUsers(user));
    }

    public static void removeForumPermissions(Project project, Collection<Long> users) throws BaseException {
        try {
            Forums forumBean = getForumBean();

            // just be safe, remove both roles, since we start assigning two roles.
            String userroleId = SOFTWARE_USER_FORUM_ROLE_PREFIX + getProjectLongValue(project, "Developer Forum ID");
            String moderatorroleId = SOFTWARE_MODERATOR_FORUM_ROLE_PREFIX + getProjectLongValue(project, "Developer Forum ID");

            for (Long userId : users) {
                forumBean.removeRole(userId, userroleId);
                forumBean.removeRole(userId, moderatorroleId);
            }

        } catch (Exception e) {
            throw new BaseException("Error removing forum permissions for project id " + project.getId(), e);
        }
    }

    public static void removeForumPermissions(Project project, Long user) throws BaseException {
        removeForumPermissions(project, userToUsers(user));
    }

    public static void addForumWatch(Project project, Collection<Long> users, long forumId) throws BaseException {
        try {
            Forums forumBean = getForumBean();

            if (forumId != 0) {
                for (Long userId : users) {
                      forumBean.createCategoryWatch(userId, forumId);
                }
            }
        } catch (Exception e) {
            throw new BaseException("Error adding forum permissions for project id " + project.getId(), e);
        }
    }

    public static void addForumWatch(Project project, Long user, long forumId) throws BaseException {
        addForumWatch(project, userToUsers(user), forumId);
    }

    public static void removeForumWatch(Project project, Collection<Long> users, long forumId) throws BaseException {
        try {
            Forums forumBean = getForumBean();

            if (forumId != 0) {
                for (Long userId : users) {
                    forumBean.deleteCategoryWatch(userId, forumId);
                }
            }
        } catch (Exception e) {
            throw new BaseException("Error removing forum permissions for project id " + project.getId(), e);
        }
    }

    public static void removeForumWatch(Project project, Long user, long forumId) throws BaseException {
        removeForumWatch(project, userToUsers(user), forumId);
    }

    /**
     * <p>Gets the specification submissions for the specified project.</p>
     *
     * @param projectId a <code>long</code> providing the ID of a project.
     * @param upMgr an <code>UploadManager</code> to be used for searching for submissions.
     * @return a <code>Submission</code> array listing the specification submissions for specified project.
     * @throws UploadPersistenceException if an unexpected error occurs.
     * @throws SearchBuilderException if an unexpected error occurs.
     * @since 1.9
     */
    public static Submission[] getSpecificationSubmissions(long projectId, UploadManager upMgr)
        throws UploadPersistenceException, SearchBuilderException {
        SubmissionType[] submissionTypes = upMgr.getAllSubmissionTypes();
        SubmissionType specSubmissionType
            = ActionsHelper.findSubmissionTypeByName(submissionTypes, "Specification Submission");

        Filter submissionTypeFilter
            = SubmissionFilterBuilder.createSubmissionTypeIdFilter(specSubmissionType.getId());
        Filter projectFilter = SubmissionFilterBuilder.createProjectIdFilter(projectId);

        Filter filter = new AndFilter(Arrays.asList(projectFilter, submissionTypeFilter));
        Submission[] specificationSubmissions = upMgr.searchSubmissions(filter);
        return specificationSubmissions;
    }

    /**
     * <p>Gets the active specification submission for the specified project and specified phase.</p>
     *
     * @param projectId a <code>long</code> providing the ID of a project.
     * @param upMgr an <code>UploadManager</code> to be used for searching for submissions.
     * @return a <code>Submission</code> array listing the specification submissions for specified project.
     * @throws UploadPersistenceException if an unexpected error occurs.
     * @throws SearchBuilderException if an unexpected error occurs.
     * @since 1.9
     */
    public static Submission getActiveSpecificationSubmission(long projectId, UploadManager upMgr)
        throws UploadPersistenceException, SearchBuilderException {

        SubmissionType[] submissionTypes = upMgr.getAllSubmissionTypes();
        SubmissionType specSubmissionType
            = ActionsHelper.findSubmissionTypeByName(submissionTypes, "Specification Submission");
        SubmissionStatus[] submissionStatuses = upMgr.getAllSubmissionStatuses();
        SubmissionStatus activeSubmissionStatus
            = ActionsHelper.findSubmissionStatusByName(submissionStatuses, "Active");

        Filter submissionTypeFilter
            = SubmissionFilterBuilder.createSubmissionTypeIdFilter(specSubmissionType.getId());
        Filter projectFilter = SubmissionFilterBuilder.createProjectIdFilter(projectId);
        Filter statusFilter = SubmissionFilterBuilder.createSubmissionStatusIdFilter(activeSubmissionStatus.getId());

        Filter filter = new AndFilter(Arrays.asList(projectFilter, submissionTypeFilter, statusFilter));
        Submission[] specificationSubmissions = upMgr.searchSubmissions(filter);
        if (specificationSubmissions == null || specificationSubmissions.length == 0) {
            return null;
        } else {
            return specificationSubmissions[0];
        }
    }

    /**
     * <p>Updates the payments for existing submitters.</p>
     *
     * @param projectId a <code>long</code> providing the ID for the project.
     * @param submitterPayments a <code>Map</code> mapping submitter IDs to submitter payments.
     * @throws BaseException if an unexpected error occurs.
     * @since BUGR-2807
     */
    static void updateSubmitterPayments(long projectId, Map<Long, Double> submitterPayments)
        throws BaseException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            conn = dbconn.createConnection();
            ps = conn.prepareStatement("UPDATE project_result SET payment = ? WHERE project_id = ? AND user_id = ?");
            ps.setLong(2, projectId);
            for (Long userId : submitterPayments.keySet()) {
                Double payment = submitterPayments.get(userId);
                if (payment == null) {
                    ps.setNull(1, Types.DOUBLE);
                } else {
                    ps.setDouble(1, payment);
                }
                ps.setLong(3, userId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BaseException("Failed to update project result for payment", e);
        } catch (UnknownConnectionException e) {
            throw new BaseException("Failed to return DBConnection", e);
        } catch (ConfigurationException e) {
            throw new BaseException("Failed to return DBConnection", e);
        } finally {
            close(ps);
            close(conn);
        }
    }

    /**
     * <p>Recalculates scheduled start date and end date for all phases when a phase is moved.</p>
     *
     * @param allPhases all the phases for the project.
     * @since 1.3
     */
    static void recalculateScheduledDates(Phase[] allPhases) {
        for (int i = 0; i < allPhases.length; ++i) {
            Phase phase = allPhases[i];
            Date newStartDate = phase.calcStartDate();
            Date newEndDate = phase.calcEndDate();
            phase.setScheduledStartDate(newStartDate);
            phase.setScheduledEndDate(newEndDate);
        }
    }

    /**
     * <p>Gets the last phase from specified list of project phase. Current implementation looks up for the <code>Final
     * Review</code> phase but this may change later.</p>
     *
     * @param phases a <code>Phase</code> array providing current project phases.
     * @return a <code>Phase</code> providing the last phase or <code>null</code> if there is no such phase found,
     * @since 1.3
     */
    static Phase getLastPhase(Phase[] phases) {
        Phase lastPhase = null;
        for (int i = 0; i < phases.length; i++) {
            Phase phase = phases[i];
            PhaseType phaseType = phase.getPhaseType();

            if ((phaseType != null)
                && (phaseType.getName().equalsIgnoreCase("Final Review")
                    || phaseType.getName().equalsIgnoreCase("Approval"))) {
                lastPhase = phase;
            }
        }
        return lastPhase;
    }

    /**
     * <p>Gets the reviews (if any) for specified <code>Approval</code> phase.</p>
     *
     * @param reviews a <code>Review</code> array providing the <code>Apporval</code> reviews for project.
     * @param thisPhase a <code>Phase</code> providing the <code>Approval</code> phases to get reviews for.
     * @return a <code>Review</code> array listing the reviews (if any) for specified <code>Approval</code> phase.
     * @since 1.3
     */
    static Review[] getApprovalPhaseReviews(Review[] reviews, Phase thisPhase) {
        List<Review> thisPhaseReviews = new ArrayList<Review>();
        for (int i = 0; i < reviews.length; i++) {
            Review review = reviews[i];
            Date reviewCreated = review.getCreationTimestamp();
            Date phaseActualStart = thisPhase.getActualStartDate();
            Date phaseActualEnd = thisPhase.getActualEndDate();
            if (phaseActualStart != null) {
                if (phaseActualStart.compareTo(reviewCreated) <= 0) {
                    if ((phaseActualEnd == null) || (phaseActualEnd.compareTo(reviewCreated) >= 0)) {
                        thisPhaseReviews.add(review);
                    }
                }
            }
        }

        return thisPhaseReviews.toArray(new Review[thisPhaseReviews.size()]);
    }

    /**
     * <p>This static method finds and returns last review of <code>Approval</code> type and made by specified resource.
     * </p>
     *
     * @param manager an instance of <code>ReviewManager</code> class that retrieves a review from the database.
     * @param phase approval phase.
     * @param scorecardType a scorecard template type that found review should have.
     * @param resourceId an ID of the resource who made (created) the review.
     * @param complete specifies whether retrieved review should have all infomration (like all items and their
     *        comments).
     * @return found review or <code>null</code> if no review has been found.
     * @throws ReviewManagementException if any error occurs during review search or retrieval.
     * @since 1.3
     */
    static Review findLastApprovalReview(ReviewManager manager, Phase phase, ScorecardType scorecardType,
                                                 long resourceId, boolean complete) throws ReviewManagementException {

        Filter filterProject = new EqualToFilter("project", new Long(phase.getProject().getId()));
        Filter filterScorecard = new EqualToFilter("scorecardType", new Long(scorecardType.getId()));
        Filter filter = new AndFilter(Arrays.asList(filterProject, filterScorecard));

        // Get a review(s) that pass filter
        Review[] reviews = manager.searchReviews(filter, complete);
        if (phase.getPhaseType().getName().equals(Constants.APPROVAL_PHASE_NAME)) {
            reviews = ActionsHelper.getApprovalPhaseReviews(reviews, phase);
        }

        for (int i = 0; i < reviews.length; i++) {
            Review review = reviews[i];
            if (review.getAuthor() == resourceId) {
                return review;
            }
        }
        return null;
    }

    private static Collection<Long> userToUsers(Long user) {
        ArrayList<Long> userCollection = new ArrayList<Long>();
        userCollection.add(user);
        return userCollection;
    }

    /**
     * <p>Searches the resources for specified user for projects of specified status.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @param status a <code>ProjectStatus</code> specifying the status of the projects.
     * @param resourceManager a <code>ResourceManager</code> to be used for searching.
     * @return a <code>Resource</code> array providing the details for found resources.
     * @throws ResourcePersistenceException if an error occurs while retrieveing resource roles.
     * @throws com.cronos.onlinereview.dataaccess.DataAccessException if an unexpected error occurs.
     * @since 1.6
     */
    static Resource[] searchUserResources(long userId, ProjectStatus status, ResourceManager resourceManager)
        throws ResourcePersistenceException {
        ResourceDataAccess resourceDataAccess = new ResourceDataAccess();
        return resourceDataAccess.searchUserResources(userId, status, resourceManager);
    }

    /**
     * <p>Checks if all all dependencies for specified phase (if any) are met.</p>
     *
     * @param phase a <code>Phase</code> providing the details for phase to check.
     * @param phaseStarting <code>true</code> if specified phase is going to be started; <code>false</code> otherwise.
     * @return <code>true</code> if specified phase has no dependencies or all existing dependencies have been met;
     *         <code>false</code> otherwise.
     * @since 1.8
     */
    static boolean arePhaseDependenciesMet(Phase phase, boolean phaseStarting) {
        Dependency[] dependencies = phase.getAllDependencies();

        if ((dependencies == null) || (dependencies.length == 0)) {
            return true;
        }

        for (int i = 0; i < dependencies.length; i++) {
            Phase dependency = dependencies[i].getDependency();
            if (phaseStarting) {
                if (dependencies[i].isDependencyStart() && dependencies[i].isDependentStart()) {
                    if (!(isPhaseOpen(dependency.getPhaseStatus()) || isPhaseClosed(dependency.getPhaseStatus()))) {
                        return false;
                    }
                } else if (!dependencies[i].isDependencyStart() && dependencies[i].isDependentStart()) {
                    if (!isPhaseClosed(dependency.getPhaseStatus())) {
                        return false;
                    }
                }
            } else {
                if (dependencies[i].isDependencyStart() && !dependencies[i].isDependentStart()) {
                    if (!(isPhaseOpen(dependency.getPhaseStatus()) || isPhaseClosed(dependency.getPhaseStatus()))) {
                        return false;
                    }
                } else if (!dependencies[i].isDependencyStart() && !dependencies[i].isDependentStart()) {
                    if (!isPhaseClosed(dependency.getPhaseStatus())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * <p>Checks if specified phase status is <code>Closed</code> status.</p>
     *
     * @param status a <code>PhaseStatus</code> to check.
     * @return <code>true</code> if specified phase status is <code>Closed</code> status; <code>false</code> otherwise.
     * @since 1.8
     */
    static boolean isPhaseClosed(PhaseStatus status) {
        return (PHASE_STATUS_CLOSED.equals(status.getName()));
    }

    /**
     * <p>Checks if specified phase status is <code>Open</code> status.</p>
     *
     * @param status a <code>PhaseStatus</code> to check.
     * @return <code>true</code> if specified phase status is <code>Open</code> status; <code>false</code> otherwise.
     * @since 1.8
     */
    static boolean isPhaseOpen(PhaseStatus status) {
        return (PHASE_STATUS_OPEN.equals(status.getName()));
    }

    /**
     * <p>Adds specified deliverable to specified list of collected deliverables.</p>
     *
     * @param deliverable a <code>Deliverable</code> to be added to collected list of deliverables.
     * @param allDeliverables an <code>Deliverable</code> array listing all deliverables for project.
     * @param collectedDeliverables a <code>List</code> collecting the valid deliverables.
     * @since 1.9
     */
    private static void addDeliverable(Deliverable deliverable, Deliverable[] allDeliverables,
                                       List<Deliverable> collectedDeliverables) {
        // For specification submission deliverables there is a need to check if there is no specification
        // submission deliverable already completed by other resource
        boolean toAdd = true;
        if (Constants.SPECIFICATION_SUBMISSION_DELIVERABLE_NAME.equals(deliverable.getName())) {
            toAdd = !isSpecificationSubmissionAlreadyDelivered(deliverable, allDeliverables);
        }
        if (toAdd) {
            collectedDeliverables.add(deliverable);
        }
    }

    /**
     * <p>Checks if <code>Specification Submission</code> is already delivered by another resource for same phase mapped
     * to specified deliverable.</p>
     *
     * @param deliverable a <code>Deliverable</code> to be added to collected list of deliverables.
     * @param allDeliverables an <code>Deliverable</code> array listing all deliverables for project.
     * @return <code>true</code> if <code>Specification Submission</code> is already delivered; <code>false</code>
     *         otherwise.
     * @since 1.9
     */
    public static boolean isSpecificationSubmissionAlreadyDelivered(Deliverable deliverable,
                                                                    Deliverable[] allDeliverables) {
        for (Deliverable otherDeliverable : allDeliverables) {
            if (Constants.SPECIFICATION_SUBMISSION_DELIVERABLE_NAME.equals(otherDeliverable.getName())
                && (otherDeliverable.getPhase() == deliverable.getPhase())
                && otherDeliverable.isComplete()
                && (otherDeliverable.getResource() != deliverable.getResource())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the mapping from deliverable id to deliverable name.
     *
     * @param request the http request
     * @return the mapping from deliverable id to deliverable name.
     * @throws BaseException
     *             if error occurs
     * @since 1.10
     */
    public static Map<Long, String> getDeliverableIdToNameMap(HttpServletRequest request) throws BaseException {
        Map<Long, String> idToNameMap = (Map<Long, String>) request.getAttribute("deliverableIdToNameMap");

        if (idToNameMap == null) {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                idToNameMap = new HashMap<Long, String>();

                DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
                conn = dbconn.createConnection();

                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT deliverable_id, name FROM deliverable_lu");

                while (rs.next()) {
                    idToNameMap.put(rs.getLong("deliverable_id"), rs.getString("name"));
                }

                request.setAttribute("deliverableIdToNameMap", idToNameMap);
            } catch (SQLException e) {
                throw new BaseException("Failed to retrieve map for deliverable id to deliverable name", e);
            } finally {
                close(rs);
                close(stmt);
                close(conn);
            }

        }

        return idToNameMap;
    }
}
