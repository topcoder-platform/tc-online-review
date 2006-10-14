/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import com.cronos.onlinereview.autoscreening.management.ScreeningManager;
import com.cronos.onlinereview.autoscreening.management.ScreeningManagerFactory;
import com.cronos.onlinereview.deliverables.AggregationDeliverableChecker;
import com.cronos.onlinereview.deliverables.AggregationReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.AppealResponsesDeliverableChecker;
import com.cronos.onlinereview.deliverables.CommittedReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.FinalFixesDeliverableChecker;
import com.cronos.onlinereview.deliverables.FinalReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.IndividualReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.SubmissionDeliverableChecker;
import com.cronos.onlinereview.deliverables.SubmitterCommentDeliverableChecker;
import com.cronos.onlinereview.deliverables.TestCasesDeliverableChecker;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.external.impl.DBUserRetrieval;
import com.cronos.onlinereview.phases.AggregationPhaseHandler;
import com.cronos.onlinereview.phases.AggregationReviewPhaseHandler;
import com.cronos.onlinereview.phases.AppealsPhaseHandler;
import com.cronos.onlinereview.phases.AppealsResponsePhaseHandler;
import com.cronos.onlinereview.phases.ApprovalPhaseHandler;
import com.cronos.onlinereview.phases.FinalFixPhaseHandler;
import com.cronos.onlinereview.phases.FinalReviewPhaseHandler;
import com.cronos.onlinereview.phases.RegistrationPhaseHandler;
import com.cronos.onlinereview.phases.ReviewPhaseHandler;
import com.cronos.onlinereview.phases.ScreeningPhaseHandler;
import com.cronos.onlinereview.phases.SubmissionPhaseHandler;
import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.DeliverableChecker;
import com.topcoder.management.deliverable.DeliverableManager;
import com.topcoder.management.deliverable.PersistenceDeliverableManager;
import com.topcoder.management.deliverable.PersistenceUploadManager;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;
import com.topcoder.management.deliverable.persistence.DeliverablePersistence;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.deliverable.persistence.UploadPersistence;
import com.topcoder.management.deliverable.persistence.sql.SqlDeliverablePersistence;
import com.topcoder.management.deliverable.persistence.sql.SqlUploadPersistence;
import com.topcoder.management.deliverable.search.DeliverableFilterBuilder;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseOperationEnum;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.resource.Resource;
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
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.servlet.request.DisallowedDirectoryException;
import com.topcoder.servlet.request.FileUpload;
import com.topcoder.servlet.request.LocalFileUpload;
import com.topcoder.util.datavalidator.LongValidator;
import com.topcoder.util.datavalidator.StringValidator;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * This class contains handy helper-methods that perform frequently needed operations.
 *
 * @author George1
 * @author real_vg
 * @version 1.0
 */
class ActionsHelper {

    /**
     * This member variable is a string constant that defines the name of the configurtaion
     * namespace which the parameters for database connection factory is stored under.
     */
    private static final String DB_CONNECTION_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";


    /**
     * This constructor is declared private to prohibit instantiation of the
     * <code>ActionsHelper</code> class.
     */
    private ActionsHelper() {
    }


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
     * @param projectCategories
     *            an array of comment types to search for wanted comment type among.
     * @param typeId
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
     * @throws BaseException
     *             if any error occurs.
     */
    public static ActionForward produceErrorReport(ActionMapping mapping, MessageResources messages,
            HttpServletRequest request, String permission, String reasonKey)
        throws BaseException{
        // Gather roles, so tabs will be displayed,
        // but only do this if roles haven't been gathered yet
        if (request.getAttribute("roles") == null) {
            AuthorizationHelper.gatherUserRoles(request);
        }

        // Place error title into request
        if (permission == null) {
            request.setAttribute("errorTitle", messages.getMessage("Error.Title.General"));
        } else {
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
        addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, new ActionMessage(errorKey));
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
        addErrorToRequest(request, ActionErrors.GLOBAL_MESSAGE, errorMessage);
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

        // Place submitter's user ID into the request
        request.setAttribute("submitterId", submitter.getProperty("External Reference ID"));
        // Place submitter's resource into the request
        request.setAttribute("submitterResource", submitter);
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

        List roleNames = new ArrayList();
        // Add induvidual roles to the list
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

    public static String determineMyPayment(Resource[] myResources) {
        double totalPayment = -1.0; // -1 will mean N/A

        for (int i = 0; i < myResources.length; ++i) {
            // Get a resource for the current iteration
            Resource resource = myResources[i];
            String paymentStr = (String) resource.getProperty("Payment");
            if (paymentStr != null && paymentStr.trim().length() != 0) {
                double payment = Double.parseDouble(paymentStr);
                if (totalPayment == -1.0) {
                    totalPayment = payment;
                } else {
                    totalPayment += payment;
                }
            }
        }

        if (totalPayment == -1.0) {
            return null;
        }

        NumberFormat nf = new DecimalFormat("#,###.##");

        return nf.format(totalPayment);
    }

    public static Boolean determineMyPaymentPaid(Resource[] myResources) {
        for (int i = 0; i < myResources.length; ++i) {
            // Get a resource for the current iteration
            Resource resource = myResources[i];
            String paid = (String) resource.getProperty("Payment Status");
            if (!("Yes".equalsIgnoreCase(paid))) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
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
        Phase[] phases = phProj.getAllPhases();
        return phases;
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

        List activePhases = new ArrayList();

        for (int i = 0; i < phases.length; ++i) {
            // Get a phase for the current iteration
            Phase phase = phases[i];
            // Add the phase to list if it is open and, hence, active
            if (phase.getPhaseStatus().getName().equalsIgnoreCase("Open")) {
                activePhases.add(phase);
            }
        }

        // Convert the list to array and return it
        return (Phase[]) activePhases.toArray(new Phase[activePhases.size()]);
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
                if (phaseStatus.equalsIgnoreCase("Scheduled")) {
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

    public static Phase getPhaseForDeliverable(Phase[] phases, Deliverable deliverable) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");
        validateParameterNotNull(deliverable, "deliverable");

        for (int i = 0; i < phases.length; ++i) {
            if (phases[i].getPhaseType().getId() == deliverable.getPhase()) {
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

        List foundResources = new ArrayList();

        for (int i = 0; i < resources.length; ++i) {
            // Get a resource for the current iteration
            Resource resource = resources[i];
            // If this resource is from phase in question, add it to the list
            if (phase == null) {
                if (resource.getPhase() == null) {
                    foundResources.add(resource);
                }
            } else {
                if (resource.getPhase() != null && resource.getPhase().longValue() == phase.getId()) {
                    foundResources.add(resource);
                }
            }
        }

        // Convert the list of resources to an array and return it
        return (Resource[]) foundResources.toArray(new Resource[foundResources.size()]);
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
        // Validate parmaeter
        validateParameterNotNull(resources, "resources");

        List submitters = new ArrayList();
        // Search for the appropriate resources and add them to the list
        for (int j = 0; j < resources.length; ++j) {
            if (resources[j].getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                submitters.add(resources[j]);
            }
        }

        // Convert the list of found submitters to array and return it
        return (Resource[]) submitters.toArray(new Resource[submitters.size()]);
    }

    public static Resource getWinner(Resource[] resources) {
        // Validate parameter
        validateParameterNotNull(resources, "resources");

        for (int i = 0; i < resources.length; ++i) {
            if ("1".equals(resources[i].getProperty("Placement"))) {
                return resources[i];
            }
        }

        // No winners have been found
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

        List myResources = new ArrayList();

        for (int i = 0; i < allResources.length; ++i) {
            // Get a resource for the current iteration
            Resource resource = allResources[i];
            // Determine if the resource is for current project
            if (resource.getProject() != null && resource.getProject().longValue() == project.getId()) {
                myResources.add(resource);
            }
        }

        // Convert the list to array and return it
        return (Resource[]) myResources.toArray(new Resource[myResources.size()]);
    }

    /**
     * This static method returns the array of resources for the currently logged in user associated with
     * the specified phase. The list of all resources for the currently logged in user is retrieved
     * from the <code>HttpServletRequest</code> object specified by <code>request</code>
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
     *            a phase to search the resouce for. This parameter can be <code>null</code>, in
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
     * @param request
     *            an <code>HttpServeltRequest</code> object that contains additional information
     *            such as current project and a list of all phases for that project.
     * @param manager
     *            an instance of the <code>DeliverableManager</code> class.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>, or if request specified by
     *             <code>request</code> parameter does not contain needed prerequisites, such as
     *             an object representing current project and a list of all phases for that project.
     * @throws DeliverablePersistenceException
     *             if an error occurs while reading from the persistence store.
     * @throws SearchBuilderException
     *             if an error occurs executing the filter.
     * @throws DeliverableCheckingException
     *             if an error occurs when determining whether a Deliverable has been completed or
     *             not.
     */
    public static Deliverable[] getAllDeliverablesForActivePhases(
            HttpServletRequest request, DeliverableManager manager)
        throws DeliverablePersistenceException, SearchBuilderException, DeliverableCheckingException {
        // Validate parameters
        validateParameterNotNull(manager, "manager");
        Project project = (Project) validateAttributeNotNull(request, "project");
        Phase[] phases = (Phase[]) validateAttributeNotNull(request, "phases");

        // Prepare filter to search for deliverables for specific project
        Filter filterProject = DeliverableFilterBuilder.createProjectIdFilter(project.getId());
        // filter to search for deliverables for specific phase(s) of the project
        Filter filterPhase = null;

        // Obtain an array of all active phases of the project
        Phase[] activePhases = getActivePhases(phases);

        switch (activePhases.length) {
        case 0:
            // No active phases -- no deliverables
            return new Deliverable[0];

        case 1:
            // If there is currently only one active phase,
            // create filter for it directly (no OR filters needed)
            filterPhase = DeliverableFilterBuilder.createPhaseIdFilter(activePhases[0].getPhaseType().getId());
            break;

        default:
            List phaseFilters = new ArrayList();
            // Prepare a list of filters for each phase in the array of active phases
            for (int i = 0; i < activePhases.length; ++i) {
                phaseFilters.add(DeliverableFilterBuilder.createPhaseIdFilter(activePhases[i].getPhaseType().getId()));
            }
            // Combine all filters using OR operator
            filterPhase = new OrFilter(phaseFilters);
        }

        // Build final combined filter
        Filter filter = new AndFilter(filterProject, filterPhase);
        // Perform a search for the deliverables
        Deliverable[] allDeliverables = manager.searchDeliverables(filter, null);

        List deliverables = new ArrayList();

        for (int i = 0; i < allDeliverables.length; ++i) {
            if (allDeliverables[i].getProject() == project.getId()) {
                deliverables.add(allDeliverables[i]);
            }
        }

        // Return found deliverables
        return (Deliverable[]) deliverables.toArray(new Deliverable[deliverables.size()]);
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

        List deliverables = new ArrayList();
        // Perform a search for outstanding deliverables
        for (int i = 0; i < allDeliverables.length; ++i) {
            if (!allDeliverables[i].isComplete()) {
                deliverables.add(allDeliverables[i]);
            }
        }
        // Return a list of outstanding deliverables converted to array
        return (Deliverable[]) deliverables.toArray(new Deliverable[deliverables.size()]);
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

        List deliverables = new ArrayList();
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
                deliverables.add(deliverable);
            }
        }
        // Return a list of "my" deliverables converted to array
        return (Deliverable[]) deliverables.toArray(new Deliverable[deliverables.size()]);
    }

    public static Submission[] getMostRecentSubmissions(UploadManager manager, Project project)
        throws BaseException {
        SubmissionStatus[] allSubmissionStatuses = manager.getAllSubmissionStatuses();

        Filter filterProject = SubmissionFilterBuilder.createProjectIdFilter(project.getId());
        Filter filterStatus = SubmissionFilterBuilder.createSubmissionStatusIdFilter(
                findSubmissionStatusByName(allSubmissionStatuses, "Active").getId());

        Filter filter = new AndFilter(filterProject, filterStatus);

        return manager.searchSubmissions(filter);
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
                if (!phase.getPhaseStatus().getName().equalsIgnoreCase("Closed")) {
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
            if (phaseStatus.equalsIgnoreCase("Open") || phaseStatus.equalsIgnoreCase("Scheduled")) {
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
            if (!phase.getPhaseStatus().getName().equalsIgnoreCase("Closed")) {
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
     *            an <code>HttpServletRequest</code> obejct, where created
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

        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    System.out.println("REMOVE ME entering create Phase Manager: " + dateFormat.format(new Date()));

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
            System.out.println("REMOVE ME entering create Default Phase Manager: " + dateFormat.format(new Date()));
                manager = new DefaultPhaseManager("com.topcoder.management.phase");
            System.out.println("REMOVE ME finishing create Default Phase Manager: " + dateFormat.format(new Date()));
            }

            // Register phase handlers if this was requested
            if (registerPhaseHandlers) {
            System.out.println("REMOVE ME entering getAllPhaseTypes: " + dateFormat.format(new Date()));
                PhaseType[] phaseTypes = manager.getAllPhaseTypes();
            System.out.println("REMOVE ME finishing getAllPhaseTypes: " + dateFormat.format(new Date()));

            System.out.println("REMOVE ME entering register all phase handlers: " + dateFormat.format(new Date()));
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new RegistrationPhaseHandler(), Constants.REGISTRATION_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new SubmissionPhaseHandler(), Constants.SUBMISSION_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new ScreeningPhaseHandler(), Constants.SCREENING_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new ReviewPhaseHandler(), Constants.REVIEW_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new AppealsPhaseHandler(), Constants.APPEALS_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new AppealsResponsePhaseHandler(), Constants.APPEALS_RESPONSE_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new AggregationPhaseHandler(), Constants.AGGREGATION_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new AggregationReviewPhaseHandler(), Constants.AGGREGATION_REVIEW_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new FinalFixPhaseHandler(), Constants.FINAL_FIX_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new FinalReviewPhaseHandler(), Constants.FINAL_REVIEW_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new ApprovalPhaseHandler(), Constants.APPROVAL_PHASE_NAME);
            System.out.println("REMOVE ME finishing register all phase handlers: " + dateFormat.format(new Date()));
            }

            // Place newly-created object into the request as attribute
            request.setAttribute((registerPhaseHandlers) ? "phaseManager+handlers" : "phaseManager", manager);
        }

    System.out.println("REMOVE ME leaving create Phase Manager: " + dateFormat.format(new Date()));
        // Return the Phase Manager object
        return manager;
    }

    /**
     * TODO: Document it!
     *
     * @param manager
     * @param phaseTypes
     * @param handler
     * @param phaseName
     */
    private static void registerPhaseHandlerForOperation(PhaseManager manager, PhaseType[] phaseTypes, PhaseHandler handler, String phaseName) {
        manager.registerHandler(handler, findPhaseTypeByName(phaseTypes, phaseName), PhaseOperationEnum.START);
        manager.registerHandler(handler, findPhaseTypeByName(phaseTypes, phaseName), PhaseOperationEnum.END);
        manager.registerHandler(handler, findPhaseTypeByName(phaseTypes, phaseName), PhaseOperationEnum.CANCEL);
    }

    /**
     * This static method helps to create an object of the <code>ProjectManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> obejct, where created
     *            <code>ProjectManager</code> object can be stored to let reusing it later for the
     *            same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws com.topcoder.management.project.ConfigurationException
     *             if error occurs while loading configuration settings, or any of the required
     *             configuration parameters are missing.
     */
    public static ProjectManager createProjectManager(HttpServletRequest request)
        throws com.topcoder.management.project.ConfigurationException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Project Manager from the request's attribute first
        ProjectManager manager = (ProjectManager) request.getAttribute("projectManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = new ProjectManagerImpl();
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
     *            an <code>HttpServletRequest</code> obejct, where created
     *            <code>ResourceManager</code> object can be stored to let reusing it later for
     *            the same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseException
     *             if any error occurs.
     */
    public static ResourceManager createResourceManager(HttpServletRequest request) throws BaseException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Resource Manager from the request's attribute first
        ResourceManager manager = (ResourceManager) request.getAttribute("resourceManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            // get connection factory
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            // get the persistence
            ResourcePersistence persistence = new SqlResourcePersistence(dbconn);

            // get the id generators
            IDGenerator resourceIdGenerator =
                    IDGeneratorFactory.getIDGenerator(PersistenceResourceManager.RESOURCE_ID_GENERATOR_NAME);
            IDGenerator resourceRoleIdGenerator =
                    IDGeneratorFactory.getIDGenerator(PersistenceResourceManager.RESOURCE_ROLE_ID_GENERATOR_NAME);
            IDGenerator notificationTypeIdGenerator =
                    IDGeneratorFactory.getIDGenerator(PersistenceResourceManager.NOTIFICATION_TYPE_ID_GENERATOR_NAME);

            // get the search bundles
            SearchBundleManager searchBundleManager =
                    new SearchBundleManager("com.topcoder.searchbuilder.common");

            SearchBundle resourceSearchBundle = searchBundleManager.getSearchBundle(
                    PersistenceResourceManager.RESOURCE_SEARCH_BUNDLE_NAME);
            // set it searchable
            setAllFieldsSearchable(resourceSearchBundle);

            SearchBundle resourceRoleSearchBundle = searchBundleManager.getSearchBundle(
                    PersistenceResourceManager.RESOURCE_ROLE_SEARCH_BUNDLE_NAME);
            // set it searchable
            setAllFieldsSearchable(resourceRoleSearchBundle);

            SearchBundle notificationSearchBundle = searchBundleManager.getSearchBundle(
                    PersistenceResourceManager.NOTIFICATION_SEARCH_BUNDLE_NAME);
            // set it searchable
            setAllFieldsSearchable(notificationSearchBundle);

            SearchBundle notificationTypeSearchBundle = searchBundleManager.getSearchBundle(
                    PersistenceResourceManager.NOTIFICATION_TYPE_SEARCH_BUNDLE_NAME);
            // set it searchable
            setAllFieldsSearchable(notificationTypeSearchBundle);

            // initialize the PersistenceResourceManager
            manager = new PersistenceResourceManager(persistence, resourceSearchBundle,
                    resourceRoleSearchBundle, notificationSearchBundle,
                    notificationTypeSearchBundle, resourceIdGenerator,
                    resourceRoleIdGenerator, notificationTypeIdGenerator);
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
     *            an <code>HttpServletRequest</code> obejct, where created
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
     * This static method helps to create an object of the <code>ScorecardManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> obejct, where created
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
     *            an <code>HttpServletRequest</code> obejct, where created
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
            Map checkers = new HashMap();

            // Some checkers are used more than once
            DeliverableChecker committedChecker = new CommittedReviewDeliverableChecker(dbconn);
            DeliverableChecker testCasesChecker = new TestCasesDeliverableChecker(dbconn);

            checkers.put(Constants.SUBMISSION_DELIVERABLE_NAME, new SubmissionDeliverableChecker(dbconn));
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
            checkers.put(Constants.APPROVAL_DELIVERABLE_NAME, committedChecker);

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
     *            an <code>HttpServletRequest</code> obejct, where created
     *            <code>UploadManager</code> object can be stored to let reusing it later for the
     *            same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseException
     *             if any error occurs.
     */
    public static UploadManager createUploadManager(HttpServletRequest request) throws BaseException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Upload Manager from the request's attribute first
        UploadManager manager = (UploadManager) request.getAttribute("uploadManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            // Get connection factory
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl(DB_CONNECTION_NAMESPACE);
            // Get the persistence
            UploadPersistence persistence = new SqlUploadPersistence(dbconn);

            // Get the ID generators
            IDGenerator uploadIdGenerator =
                    IDGeneratorFactory.getIDGenerator(PersistenceUploadManager.UPLOAD_ID_GENERATOR_NAME);
            IDGenerator uploadTypeIdGenerator =
                    IDGeneratorFactory.getIDGenerator(PersistenceUploadManager.UPLOAD_TYPE_ID_GENERATOR_NAME);
            IDGenerator uploadStatusIdGenerator =
                    IDGeneratorFactory.getIDGenerator(PersistenceUploadManager.UPLOAD_STATUS_ID_GENERATOR_NAME);
            IDGenerator submissionIdGenerator =
                    IDGeneratorFactory.getIDGenerator(PersistenceUploadManager.SUBMISSION_ID_GENERATOR_NAME);
            IDGenerator submissionStatusIdGenerator =
                    IDGeneratorFactory.getIDGenerator(PersistenceUploadManager.SUBMISSION_STATUS_ID_GENERATOR_NAME);

            // Get the search bundles
            SearchBundleManager searchBundleManager =
                    new SearchBundleManager("com.topcoder.searchbuilder.common");

            SearchBundle uploadSearchBundle = searchBundleManager.getSearchBundle(
                    PersistenceUploadManager.UPLOAD_SEARCH_BUNDLE_NAME);
            SearchBundle submissionSearchBundle = searchBundleManager.getSearchBundle(
                    PersistenceUploadManager.SUBMISSION_SEARCH_BUNDLE_NAME);

            // Initialize the PersistenceUploadManager
            manager = new PersistenceUploadManager(persistence,
                    uploadSearchBundle, submissionSearchBundle,
                    uploadIdGenerator, uploadTypeIdGenerator, uploadStatusIdGenerator,
                    submissionIdGenerator, submissionStatusIdGenerator);
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
     *            an <code>HttpServletRequest</code> obejct, where created
     *            <code>ScreeningManager</code> object can be stored to let reusing it later for
     *            the same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseException
     *             if any error occurs.
     */
    public static ScreeningManager createScreeningManager(HttpServletRequest request) throws BaseException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Auto Screening Manager from the request's attribute first
        ScreeningManager manager = (ScreeningManager) request.getAttribute("screeningManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = ScreeningManagerFactory.createScreeningManager();
            // Place newly-created object into the request as attribute
            request.setAttribute("screeningManager", manager);
        }

        // Return the Screening Manager object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>UserRetrieval</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> obejct, where created
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
     *            an <code>HttpServletRequest</code> obejct, where created
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
}
