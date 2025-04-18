/*
 * Copyright (C) 2006 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.model.DefaultScorecard;
import org.apache.struts2.ActionContext;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.text.TextProvider;
import com.topcoder.onlinereview.component.dataaccess.ClientProject;
import com.topcoder.onlinereview.component.dataaccess.CockpitProject;
import com.topcoder.onlinereview.component.dataaccess.ProjectDataAccess;
import com.topcoder.onlinereview.component.dataaccess.ResourceDataAccess;
import com.topcoder.onlinereview.component.deliverable.Deliverable;
import com.topcoder.onlinereview.component.deliverable.DeliverableCheckingException;
import com.topcoder.onlinereview.component.deliverable.DeliverableFilterBuilder;
import com.topcoder.onlinereview.component.deliverable.DeliverableManager;
import com.topcoder.onlinereview.component.deliverable.DeliverablePersistenceException;
import com.topcoder.onlinereview.component.deliverable.Submission;
import com.topcoder.onlinereview.component.deliverable.SubmissionFilterBuilder;
import com.topcoder.onlinereview.component.deliverable.Upload;
import com.topcoder.onlinereview.component.deliverable.UploadFilterBuilder;
import com.topcoder.onlinereview.component.deliverable.UploadManager;
import com.topcoder.onlinereview.component.deliverable.late.LateDeliverable;
import com.topcoder.onlinereview.component.deliverable.late.LateDeliverableManager;
import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.external.ExternalUser;
import com.topcoder.onlinereview.component.external.RetrievalException;
import com.topcoder.onlinereview.component.external.UserRetrieval;
import com.topcoder.onlinereview.component.fileupload.FileUpload;
import com.topcoder.onlinereview.component.fileupload.LocalFileUpload;
import com.topcoder.onlinereview.component.grpcclient.GrpcHelper;
import com.topcoder.onlinereview.component.grpcclient.actionshelper.ActionsHelperServiceRpc;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.management.ProjectLinkManager;
import com.topcoder.onlinereview.component.project.management.ProjectManager;
import com.topcoder.onlinereview.component.project.management.ProjectStatus;
import com.topcoder.onlinereview.component.project.payment.ProjectPayment;
import com.topcoder.onlinereview.component.project.payment.ProjectPaymentAdjustmentManager;
import com.topcoder.onlinereview.component.project.payment.ProjectPaymentManager;
import com.topcoder.onlinereview.component.project.phase.Dependency;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseManagementException;
import com.topcoder.onlinereview.component.project.phase.PhaseManager;
import com.topcoder.onlinereview.component.project.phase.PhaseStatus;
import com.topcoder.onlinereview.component.project.phase.handler.or.PRHelper;
import com.topcoder.onlinereview.component.project.phase.handler.or.PaymentsHelper;
import com.topcoder.onlinereview.component.project.phase.template.PhaseTemplate;
import com.topcoder.onlinereview.component.resource.Resource;
import com.topcoder.onlinereview.component.resource.ResourceFilterBuilder;
import com.topcoder.onlinereview.component.resource.ResourceManager;
import com.topcoder.onlinereview.component.resource.ResourcePersistenceException;
import com.topcoder.onlinereview.component.resource.ResourceRole;
import com.topcoder.onlinereview.component.review.Comment;
import com.topcoder.onlinereview.component.review.Review;
import com.topcoder.onlinereview.component.review.ReviewManagementException;
import com.topcoder.onlinereview.component.review.ReviewManager;
import com.topcoder.onlinereview.component.reviewfeedback.ReviewFeedbackManager;
import com.topcoder.onlinereview.component.scorecard.Group;
import com.topcoder.onlinereview.component.scorecard.PersistenceException;
import com.topcoder.onlinereview.component.scorecard.Scorecard;
import com.topcoder.onlinereview.component.scorecard.ScorecardManager;
import com.topcoder.onlinereview.component.scorecard.Section;
import com.topcoder.onlinereview.component.search.SearchBuilderException;
import com.topcoder.onlinereview.component.search.filter.AndFilter;
import com.topcoder.onlinereview.component.search.filter.EqualToFilter;
import com.topcoder.onlinereview.component.search.filter.Filter;
import com.topcoder.onlinereview.component.search.filter.NotFilter;
import com.topcoder.onlinereview.component.search.filter.OrFilter;
import com.topcoder.onlinereview.component.termsofuse.ProjectTermsOfUseDao;
import com.topcoder.onlinereview.component.termsofuse.TermsOfUseDao;
import com.topcoder.onlinereview.component.termsofuse.UserTermsOfUseDao;
import com.topcoder.onlinereview.grpc.actionshelper.proto.DefaultScorecardProto;
import com.topcoder.onlinereview.grpc.actionshelper.proto.DeliverabIdNameProto;
import com.topcoder.onlinereview.grpc.actionshelper.proto.RatingProto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.topcoder.onlinereview.component.util.SpringUtils.getBean;

/**
 * <p>
 * This class contains handy helper-methods that perform frequently needed
 * operations.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ActionsHelper {

    /** Represents the Constant GLOBAL_MESSAGE. */
    public static final String GLOBAL_MESSAGE = "global_message";

    /**
     * The logger instance.
     */
    private static final Logger log = LoggerFactory.getLogger(ActionsHelper.class.getName());

    /**
     * This member variable is a string constant that defines the name of the
     * configuration namespace which the parameters for database connection factory
     * are stored under.
     */
    private static final String DB_CONNECTION_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * This member variable is a string constant that defines the name of the
     * configuration namespace which the parameters for Phases Template persistence
     * are stored under.
     */
    private static final String PHASES_TEMPLATE_PERSISTENCE_NAMESPACE = "com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence";

    /**
     * This helper class is used for creating the managers.
     */
    private static ManagerCreationHelper managerCreationHelper;

    /**
     * Used for caching loaded scorecards.
     */
    private static final Map<Long, Scorecard> cachedScorecards = new HashMap<Long, Scorecard>();

    /**
     * constant for software user forum role prefix.
     */
    private static final String SOFTWARE_USER_FORUM_ROLE_PREFIX = "Software_Users_";

    /**
     * constant for software moderator forum role prefix.
     */
    private static final String SOFTWARE_MODERATOR_FORUM_ROLE_PREFIX = "Software_Moderators_";

    /** constant for "Open" phase status. */
    private static final String PHASE_STATUS_OPEN = "Open";

    /** constant for "Closed" phase status. */
    private static final String PHASE_STATUS_CLOSED = "Closed";

    /**
     * The AWS credentials file.
     */
    //private static final String AWS_CREDENTIALS_FILE = "AwsS3Credentials.properties";

    /**
     * FileUpload namespace for develop challenge
     */
    public static final String LOCAL_STORAGE_NAMESPACE = "com.topcoder.servlet.request.LocalFileUpload";

    /**
     * FileUpload namespace fro studio challenge
     */
    public static final String LOCAL_STUDIO_STORAGE_NAMESPACE = "com.topcoder.servlet.request.LocalStudioFileUpload";

    /**
     * AWS S3 client
     */
    private static final AmazonS3 s3Client;

    /**
     * Expire time for presigned s3 url in millis
     */
    private static long presignedExpireMillis;

    /**
     * AWS S3 bucket name
     */
    private static final String s3Bucket;

    /**
     * AWS S3 bucket DMZ name
     */
    private static final String s3BucketDmz;

    /**
     * AWS S3 bucket Quarantine name
     */
    private static final String s3BucketQuarantine;

    static {
        try {
            s3Bucket = ConfigHelper.getS3Bucket();
            s3BucketDmz = ConfigHelper.getS3BucketDmz();
            s3BucketQuarantine = ConfigHelper.getS3BucketQuarantine();
            presignedExpireMillis = ConfigHelper.getPreSignedExpTimeMilis();
            s3Client = AmazonS3ClientBuilder.standard().withRegion("us-east-1").build();
        } catch (Throwable e) {
            throw new RuntimeException("Failed load to Amazon S3 CLient", e);
        }
    }

    /**
     * <code>ActionsHelper</code> class.
     */
    public ActionsHelper() {
    }

    // ------------------------------------------------------------ Hardcoded
    // validations ---------

    /**
     * Lookup function for project categories that should have a project_result row.
     * These rows are used for ratings, reliability, and the Digital Run.
     *
     * @param categoryId the category id to look up.
     * @return whether the provided category id should have a project_result row.
     */
    private static boolean isProjectResultCategory(long categoryId) {
        return (categoryId == 1 // Component Design
                || categoryId == 2 // Component Development
                || categoryId == 5 // Component Testing
                || categoryId == 6 // Application Specification
                || categoryId == 7 // Application Architecture
                || categoryId == 9 // Bug Hunt
                || categoryId == 13 // Test Scenarios
                || categoryId == 26 // Test Suites
                || categoryId == 14 // Application Assembly
                || categoryId == 23 // Application Conceptualization
                || categoryId == 19 // UI Prototype
                || categoryId == 24 // RIA Build
                || categoryId == 25 // RIA Component
                || categoryId == 29 // Copilot Posting
                || categoryId == 35 // Content Creation
                || categoryId == 36 // Reporting
                || categoryId == 38 // First2Finish
                || categoryId == 39 // Code
        );
    }

    /**
     * Lookup function for project categories that are rated.
     *
     * @param categoryId the category id to look up.
     * @return whether the provided category id is rated.
     */
    private static boolean isRatedCategory(long categoryId) {
        return (categoryId == 1 // Component Design
                || categoryId == 2 // Component Development
                || categoryId == 23 // Conceptualization
                || categoryId == 6 // Specification
                || categoryId == 7 // Architecture
                || categoryId == 14 // Assembly
                || categoryId == 13 // Test Scenarios
                || categoryId == 26 // Test Suites
                || categoryId == 19); // UI Prototypes
    }

    // ------------------------------------------------------------ Validator type
    // of methods -----

    /**
     * This static method validates that parameter specified by <code>param</code>
     * parameter is not <code>null</code>, and throws an exception if validation
     * fails.
     *
     * @param param     a parameter to validate for non-null value.
     * @param paramName a name of the parameter that is being validated.
     * @throws IllegalArgumentException if parameter <code>param</code> is
     *                                  <code>null</code>.
     */
    public static void validateParameterNotNull(Object param, String paramName) {
        if (param == null) {
            throw new IllegalArgumentException("Parameter '" + paramName + "' must not be null.");
        }
    }

    /**
     * This static method validates that parameter specified by <code>str</code>
     * parameter is not <code>null</code> and not an empty string, and throws an
     * exception if validation fails.
     *
     * @param str       a string parameter to validate.
     * @param paramName a name of the parameter that is being validated.
     * @throws IllegalArgumentException if parameter <code>str</code> is
     *                                  <code>null</code> or empty string.
     */
    public static void validateParameterStringNotEmpty(String str, String paramName) {
        validateParameterNotNull(str, paramName);
        if (str.trim().length() == 0) {
            throw new IllegalArgumentException("Parameter '" + paramName + "' must not be empty string.");
        }
    }

    /**
     * This static method verifies that parameter of type <code>long</code>
     * specified by <code>value</code> parameter is not negative or zero value, and
     * throws an exception if validation fails.
     *
     * @param value     a <code>long</code> value to validate.
     * @param paramName a name of the parameter that is being validated.
     * @throws IllegalArgumentException if parameter <code>value</code> is zero or
     *                                  negative.
     */
    public static void validateParameterPositive(long value, String paramName) {
        if (value <= 0) {
            throw new IllegalArgumentException("Parameter '" + paramName + "' must not be negative or zero."
                    + " Current value of the parameters is " + value + ".");
        }
    }

    /**
     * This static method verifies that parameter of type <code>int</code> specified
     * by <code>value</code> parameter falls in the range of allowed values
     * specified by <code>minValue</code> and <code>maxValue</code> parameters. The
     * range's boundaries are inclusive. This method itself throws an exception if
     * the value of <code>minValue</code> is greater than the value of
     * <code>maxValue</code> parameter.
     *
     * @param value     an <code>int</code> value to validate.
     * @param paramName a name of the parameter that is being validated.
     * @param minValue  the lower boundary of the range. The boundary is inclusive.
     * @param maxValue  the upper boundary of the range. The boundary is inclusive.
     * @throws IllegalArgumentException if parameter <code>value</code> is less than
     *                                  <code>minValue</code> or greater than
     *                                  <code>maxValue</code>, or if
     *                                  <code>minValue</code> is greater than
     *                                  <code>maxValue</code>.
     */
    public static void validateParameterInRange(int value, String paramName, int minValue, int maxValue) {
        if (minValue > maxValue) {
            throw new IllegalArgumentException("Parameter 'minValue' is greater than 'maxValue'.");
        }
        if (value < minValue || value > maxValue) {
            throw new IllegalArgumentException("Parameter '" + paramName
                    + "' does not fall into the specified range of [" + minValue + "; " + maxValue + "].");
        }
    }

    /**
     * This static method verifies that the request specified by
     * <code>request</code> parameter contains non-<code>null</code> attribute and
     * returns the object stored under that attribute. This method throws an
     * exception is validation fails.
     *
     * @return a reference to object retrieved from the request's scope.
     * @param request       an <code>HttpServletRequest</code> object which
     *                      attribute should be retrieved and validated from.
     * @param attributeName a name of the attribute to retrieve from the request's
     *                      scope.
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>, or
     *                                  <code>attributeName</code> parameter is
     *                                  empty string, or if the request does not
     *                                  contain attribute with the specified name or
     *                                  attribute's value is <code>null</code>
     */
    private static Object validateAttributeNotNull(HttpServletRequest request, String attributeName) {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterStringNotEmpty(attributeName, "attributeName");

        Object obj = request.getAttribute(attributeName);
        if (obj == null) {
            throw new IllegalArgumentException("There is no attribute '" + attributeName
                    + "' stored in the request scope, or the attrubute stored is null.");
        }
        return obj;
    }

    /**
     * This static method searches for the project phase with the specified ID in a
     * provided array of project phases.
     *
     * @return found project phase, or <code>null</code> if a phase with the
     *         specified ID has not been found in the provided array of project
     *         phases.
     * @param phases  an array of project phases to search for wanted project phase
     *                among.
     * @param phaseId the ID of the needed project phase.
     * @throws IllegalArgumentException if <code>phases</code> parameter is
     *                                  <code>null</code>, or <code>phaseId</code>
     *                                  parameter is zero or negative.
     */
    public static Phase findPhaseById(Phase[] phases, long phaseId) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");
        validateParameterPositive(phaseId, "phaseId");

        for (Phase phase : phases) {
            if (phase.getId() == phaseId) {
                return phase;
            }
        }
        return null;
    }

    /**
     * <p>
     * This static method searches for the project phase with the specified phase
     * type name in a provided array of project phases.
     * </p>
     *
     * @param phases        an array of project phases to search for wanted project
     *                      phase among.
     * @param phaseTypeName the name of type of the needed project phase.
     * @return found project phase or <code>null</code> if a phase with the
     *         specified ID has not been found in the provided array of project
     *         phases.
     * @throws IllegalArgumentException if <code>phases</code> parameter is
     *                                  <code>null</code> or
     *                                  <code>phaseTypeName</code> parameter is
     *                                  <code>null</code> or empty.
     */
    public static Phase findPhaseByTypeName(Phase[] phases, String phaseTypeName) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");
        validateParameterStringNotEmpty(phaseTypeName, "phaseTypeName");

        for (Phase phase : phases) {
            if (phase.getPhaseType().getName().equalsIgnoreCase(phaseTypeName)) {
                return phase;
            }
        }
        return null;
    }

    /**
     * This static method counts the number of questions in a specified scorecard
     * template.
     *
     * @return a number of questions in the scorecard.
     * @param scorecardTemplate a scorecard template to count questions in.
     * @throws IllegalArgumentException if <code>scorecardTemplate</code> parameter
     *                                  is <code>null</code>.
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
     * This static method counts the number of uploads that are required or optional
     * in the whole scorecard template.
     *
     * @return a number of uploads in the scorecard.
     * @param scorecardTemplate a scorecard template to count uploads in.
     * @throws IllegalArgumentException if <code>scorecardTemplate</code> parameter
     *                                  is <code>null</code>.
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
                for (int k = 0; k < section.getNumberOfQuestions(); ++k) {
                    if (section.getQuestion(k).isUploadDocument()) {
                        ++uploadCount;
                    }
                }
            }
        }
        return uploadCount;
    }

    /**
     * This static method places certain attributes into the request and returns a
     * forward to the error page.
     *
     * @return an action forward to the appropriate error page.
     * @param textProvider              the text provider used
     * @param request                   the http request.
     * @param permission                permission to check against, or
     *                                  <code>null</code> if no check is required.
     * @param reasonKey                 a key in Message resources which the reason
     *                                  of the error is stored under.
     * @param getRedirectUrlFromReferer determines whether redirect link should be
     *                                  obtained from Referer request header. If
     *                                  this parameter is <code>null</code>, no
     *                                  redirect is needed at all (some other error
     *                                  happened, not denial of access).
     * @throws BaseException if any error occurs.
     */
    public static String produceErrorReport(TextProvider textProvider, HttpServletRequest request, String permission,
                                            String reasonKey, Boolean getRedirectUrlFromReferer) throws BaseException {
        log.warn("Produce error report: " + permission + " " + reasonKey);
        // If the user is not logged in, this is the reason
        // why they don't have permissions to do the job. Let the user login first
        if (getRedirectUrlFromReferer != null && !AuthorizationHelper.isUserLoggedIn(request)) {
            AuthorizationHelper.setLoginRedirect(request, getRedirectUrlFromReferer);
            request.setAttribute("redirectUrl",
                    ConfigHelper.getNewAuthUrl() + "?retUrl=" + request.getSession().getAttribute("redirectBackUrl"));

            return Constants.NOT_AUTHORIZED_FORWARD_NAME;
        }

        // Gather roles, so tabs will be displayed,
        // but only do this if roles haven't been gathered yet
        if (request.getAttribute("roles") == null) {
            AuthorizationHelper.gatherUserRoles(request);
        }

        // Place error title into request
        if (permission == null) {
            request.setAttribute("errorTitle", textProvider.getText("Error.Title.General"));
        } else {
            if ("Error.NoPermission".equalsIgnoreCase(reasonKey)) {
                log.warn("Authorization failures. User tried to perform " + permission
                        + " which he/she doesn't have permission.");
            }
            request.setAttribute("errorTitle", textProvider.getText("Error.Title." + permission.replaceAll(" ", "")));
        }
        // Place error message (reason) into request
        request.setAttribute("errorMessage", textProvider.getText(reasonKey));
        // Find appropriate forward and return it
        return Constants.USER_ERROR_FORWARD_NAME;
    }

    /**
     * Add the error to the request.
     *
     * @param request  the http servlet request
     * @param errorKey the error key
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>, or if
     *                                  <code>errorKey</code> parameter is empty
     *                                  string.
     */
    public static void addErrorToRequest(HttpServletRequest request, String errorKey) {
        // Validate the errorKey parameter. Other parameters will be validated in
        // overloaded method
        validateParameterStringNotEmpty(errorKey, "errorKey");
        // Call overload to do the actual job
        addErrorToRequest(request, GLOBAL_MESSAGE, errorKey);
    }

    /**
     * Add the error to the request.
     *
     * @param request         the http servlet request
     * @param messageProperty the message property
     * @param errorKey        the error key
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>, or if any of the
     *                                  <code>messageProperty</code> or
     *                                  <code>errorKey</code> parameters are empty
     *                                  strings.
     */
    public static void addErrorToRequest(HttpServletRequest request, String messageProperty, String errorKey) {
        // Validate the arguments
        validateParameterNotNull(request, "request");
        validateParameterNotNull(messageProperty, "messageProperty");
        validateParameterNotNull(errorKey, "errorKey");

        if (ActionContext.getContext() != null) {
            ActionSupport action = (ActionSupport) ActionContext.getContext().getActionInvocation().getAction();

            String text = action.getText(errorKey);

            if (messageProperty.equals(GLOBAL_MESSAGE)) {
                action.addActionError(text);

            } else {
                action.addFieldError(messageProperty, text);
            }
        }
    }

    /**
     * Add error to the request.
     *
     * @param request         the http servlet request
     * @param messageProperty the message property
     * @param errorKey        the error key
     * @param args            the arguments
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>, or if any of the
     *                                  <code>messageProperty</code> or
     *                                  <code>errorKey</code> parameters are empty
     *                                  strings.
     */
    public static void addErrorToRequest(HttpServletRequest request, String messageProperty, String errorKey,
            Object... args) {
        // Validate the parameters.
        validateParameterNotNull(request, "request");
        validateParameterNotNull(messageProperty, "messageProperty");
        validateParameterNotNull(errorKey, "errorKey");

        if (ActionContext.getContext() != null) {
            ActionSupport action = (ActionSupport) ActionContext.getContext().getActionInvocation().getAction();

            String text = action.getText(errorKey, Arrays.asList(args));

            if (messageProperty.equals(GLOBAL_MESSAGE)) {
                action.addActionError(text);

            } else {
                action.addFieldError(messageProperty, text);
            }
        }
    }

    /**
     * Check if the error presents or not.
     * 
     * @return true if error exists.
     * @param request the http request
     * @throws IllegalArgumentException if <code>request</code> parameter is
     *                                  <code>null</code>.
     */
    public static boolean isErrorsPresent(HttpServletRequest request) {
        // Validate parameter
        validateParameterNotNull(request, "request");

        if (ActionContext.getContext() != null) {
            ActionSupport action = (ActionSupport) ActionContext.getContext().getActionInvocation().getAction();
            return action.hasErrors();
        }
        return false;
    }

    /**
     * This static method determines if the specified comment is reviewer's comment.
     * Reviewer's comments are those of any of the following types:
     * &quot;Comment&quot;, &quot;Required&quot;, or &quot;Recommended&quot;.
     *
     * @return <code>true</code> if the specified comment is reviewer's comment,
     *         <code>false</code> if it is not.
     * @param comment a comment to determine type of.
     * @throws IllegalArgumentException if <code>comment</code> parameter is
     *                                  <code>null</code>.
     */
    public static boolean isReviewerComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null && (commentType.equalsIgnoreCase("Comment")
                || commentType.equalsIgnoreCase("Required") || commentType.equalsIgnoreCase("Recommended")));
    }

    /**
     * This static method determines if the specified comment is manager's comment.
     *
     * @return <code>true</code> if the specified comment is manager's comment,
     *         <code>false</code> if it is not.
     * @param comment a comment to determine type of.
     * @throws IllegalArgumentException if <code>comment</code> parameter is
     *                                  <code>null</code>.
     */
    public static boolean isManagerComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null && commentType.equalsIgnoreCase("Manager Comment"));
    }

    /**
     * This static method determines if the specified comment is appeal or appeal
     * response.
     *
     * @return <code>true</code> if the specified comment is appeal or appeal
     *         response, <code>false</code> if it is not.
     * @param comment a comment to determine type of.
     * @throws IllegalArgumentException if <code>comment</code> parameter is
     *                                  <code>null</code>.
     */
    public static boolean isAppealsComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null
                && (commentType.equalsIgnoreCase("Appeal") || commentType.equalsIgnoreCase("Appeal Response")));
    }

    /**
     * This static method determines if the specified comment is aggregator's
     * comment.
     *
     * @return <code>true</code> if the specified comment is aggregator's comment,
     *         <code>false</code> if it is not.
     * @param comment a comment to determine type of.
     * @throws IllegalArgumentException if <code>comment</code> parameter is
     *                                  <code>null</code>.
     */
    public static boolean isAggregatorComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null && commentType.equalsIgnoreCase("Aggregation Comment"));
    }

    /**
     * This static method determines if the specified comment is aggregation review
     * comment. Aggregation review comments are those that were put either by
     * reviewer or by submitter when they were doing the review of aggregation
     * scorecard.
     *
     * @return <code>true</code> if the specified comment is aggregation review
     *         comment, <code>false</code> if it is not.
     * @param comment a comment to determine type of.
     * @throws IllegalArgumentException if <code>comment</code> parameter is
     *                                  <code>null</code>.
     */
    public static boolean isAggregationReviewComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null && (commentType.equalsIgnoreCase("Aggregation Review Comment")
                || commentType.equalsIgnoreCase("Submitter Comment")));
    }

    /**
     * This static method determines if the specified comment is a final review
     * comment.
     *
     * @return <code>true</code> if the specified comment is final review comment,
     *         <code>false</code> if it is not.
     * @param comment a comment to determine type of.
     * @throws IllegalArgumentException if <code>comment</code> parameter is
     *                                  <code>null</code>.
     */
    public static boolean isFinalReviewComment(Comment comment) {
        // Validate parameter
        validateParameterNotNull(comment, "comment");

        String commentType = comment.getCommentType().getName();

        return (commentType != null && commentType.equalsIgnoreCase("Final Review Comment"));
    }

    /**
     * This method helps gather some commonly used information about the project.
     * When the information has been gathered, this method places it into the
     * request as a set of attributes.
     *
     * @param request      the http request.
     * @param project      a project to get the info for.
     * @param textProvider the text provider
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>.
     */
    public static void retrieveAndStoreBasicProjectInfo(HttpServletRequest request, Project project,
            TextProvider textProvider) {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterNotNull(project, "project");
        validateParameterNotNull(textProvider, "textProvider");

        // Retrieve the name of the Project Category icon
        String categoryIconName = ConfigHelper.getProjectCategoryIconName(project.getProjectCategory().getName());
        // And place it into request
        request.setAttribute("categoryIconName", categoryIconName);

        String rootCatalogID = (String) project.getProperty("Root Catalog ID");
        // Retrieve Root Catalog icon's filename
        String rootCatalogIcon = ConfigHelper.getRootCatalogIconNameSm(rootCatalogID);
        // Retrieve the name of Root Catalog for this project
        String rootCatalogName = textProvider.getText(ConfigHelper.getRootCatalogAltTextKey(rootCatalogID));

        // Place the filename of the icon for Root Catalog into request
        request.setAttribute("rootCatalogIcon", rootCatalogIcon);
        // Place the name of the Root Catalog for the current project into request
        request.setAttribute("rootCatalogName", rootCatalogName);
    }

    /**
     * This method helps to retrieve the roles currently logged in user has. This
     * method then places the retrieved info into the request as attribute named
     * &quot;myRole&quot;.
     *
     * @param request      the http request.
     * @param textProvider the text provider
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>.
     */
    public static void retrieveAndStoreMyRole(HttpServletRequest request, TextProvider textProvider) {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterNotNull(textProvider, "textProvider");

        // Obtain an array of "my" resources
        Resource[] myResources = (Resource[]) request.getAttribute("myResources");
        // Place a string that represents "my" current role(s) into the request
        request.setAttribute("myRole", ActionsHelper.determineRolesForResources(request, textProvider, myResources));
    }

    /**
     * Retrieve and store the submitter's information.
     *
     * @param request the http request
     * @param upload  the upload from the submitter
     * @throws BaseException if any error
     */
    public static void retrieveAndStoreSubmitterInfo(HttpServletRequest request, Upload upload) throws BaseException {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterNotNull(upload, "upload");

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager();
        // Get submitter's resource
        Resource submitter = resMgr.getResource(upload.getOwner());
        populateEmailProperty(request, submitter);

        // Place submitter's user ID into the request
        request.setAttribute("submitterId", submitter.getUserId());
        // Place submitter's resource into the request
        request.setAttribute("submitterResource", submitter);
    }

    /**
     * Populate resource email resource info to resource.
     *
     * @param request  the request to retrieve manager instance
     * @param resource Resource instance
     * @throws BaseException if error occurs
     */
    public static void populateEmailProperty(HttpServletRequest request, Resource resource) throws BaseException {
        populateEmailProperty(request, new Resource[] { resource });
    }

    /**
     * Populate resource email resource info to resources.
     *
     * @param request   the request to retrieve manager instance
     * @param resources array of Resource instance
     * @throws BaseException if error occurs
     */
    public static void populateEmailProperty(HttpServletRequest request, Resource[] resources) throws BaseException {
        Long[] userIDs = new Long[resources.length];
        for (int i = 0; i < resources.length; i++) {
            Long userID = resources[i].getUserId();
            if (userID == null) {
                throw new BaseException("the resourceId: " + resources[i].getId() + " doesn't refer a valid user");
            }
            userIDs[i] = userID;
        }

        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
        ExternalUser[] users = userRetrieval.retrieveUsers(userIDs);

        if (users == null) {
            throw new BaseException("Error during user retrieval in populateEmailProperty() method.");
        }
        Map<Long, String> emailsMap = new HashMap<Long, String>();
        for (ExternalUser user : users) {
            emailsMap.put(user.getId(), user.getEmail());
        }

        for (int i = 0; i < resources.length; i++) {
            String email = emailsMap.get(userIDs[i]);
            System.out.println("Email for " + resources[i].getId() + ":" + email);
            if (email == null) {
                throw new BaseException("Can't retrieve email property for the resourceId: " + resources[i].getId());
            }
            resources[i].setProperty("Email", email);
        }
    }

    /**
     * This static member function examines an array of supplied resources and forms
     * a string that specifies the roles based on the roles the resources in the
     * array have. All roles in the array are supposed to be assigned to the same
     * external user, although the check of meeting this condition is not performed
     * by this method.
     *
     * @return a string with the role(s) the resource from the specified array have.
     *         If there are more than one role, the roles will be separated by
     *         forward slash (<code>/</code>) character.
     * @param request      an <code>HttpServletRequest</code> object.
     * @param textProvider the text provider.
     * @param resources    an array specifying the resources to examine.
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>.
     */
    private static String determineRolesForResources(HttpServletRequest request, TextProvider textProvider,
            Resource[] resources) {
        // Validate parameter
        validateParameterNotNull(textProvider, "textProvider");
        validateParameterNotNull(resources, "resources");

        if (AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
            return textProvider.getText("ResourceRole." + Constants.MANAGER_ROLE_NAME.replaceAll(" ", ""));
        }
        if (AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)) {
            return textProvider.getText("ResourceRole." + Constants.MANAGER_ROLE_NAME.replaceAll(" ", ""));
        }

        List<String> roleNames = new ArrayList<String>();
        // Add individual roles to the list
        for (Resource resource : resources) {
            String roleName = resource.getResourceRole().getName();
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
            return textProvider.getText("ResourceRole." + roleNames.get(0).replaceAll(" ", ""));
        }

        StringBuilder roles = new StringBuilder(32);

        // Form a string with roles separated by forward slash character
        for (String roleName : roleNames) {
            if (roles.length() != 0) {
                roles.append('/');
            }
            roles.append(textProvider.getText("ResourceRole." + roleName.replaceAll(" ", "")));
        }
        // Return the resulting string
        return roles.toString();
    }

    /**
     * <p>
     * Gets the list of payments and payments paid status per resource roles
     * assigned to user.
     * </p>
     *
     * @param myResources  a <code>Resource</code> array listing the resources
     *                     associated with user.
     * @param allPayments  all the project payments.
     * @param payments     the payments per resource roles will be stored in this
     *                     instance.
     * @param paymentsPaid the payments paid status per resource roles will be
     *                     stored in this instance.
     */
    public static void getMyPayments(Resource[] myResources, List<ProjectPayment> allPayments,
            Map<ResourceRole, Double> payments, Map<ResourceRole, Boolean> paymentsPaid) {
        Map<Long, Resource> resourceLookup = new HashMap<Long, Resource>();
        for (Resource resource : myResources) {
            resourceLookup.put(resource.getId(), resource);
            payments.put(resource.getResourceRole(), null);
            paymentsPaid.put(resource.getResourceRole(), Boolean.FALSE);
        }

        for (ProjectPayment payment : allPayments) {
            if (resourceLookup.containsKey(payment.getResourceId())) {
                ResourceRole role = resourceLookup.get(payment.getResourceId()).getResourceRole();
                Double oldPayment = payments.get(role) == null ? 0.0 : payments.get(role);
                payments.put(role, oldPayment + payment.getAmount().doubleValue());
                if (payment.getPactsPaymentId() != null) {
                    paymentsPaid.put(role, Boolean.TRUE);
                }
            }
        }
    }

    /**
     * This static method retrieves an array of phases for the project specified by
     * <code>project</code> parameter, using <code>PhaseManager</code> object
     * specified by <code>manager</code> parameter.
     *
     * @return an array of phases for the project.
     * @param manager an instance of <code>PhaseManager</code> object.
     * @param project a project to retrieve phases for.
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>.
     * @throws PhaseManagementException if an error occurred querying the project
     *                                  from the persistent store.
     */
    public static Phase[] getPhasesForProject(PhaseManager manager, Project project) throws PhaseManagementException {
        // Validate parameters
        validateParameterNotNull(manager, "manager");
        validateParameterNotNull(project, "project");

        // Get all phases for the project
        com.topcoder.onlinereview.component.project.phase.Project phProj = manager.getPhases(project.getId());
        return (phProj != null) ? phProj.getAllPhases() : new Phase[0];
    }

    /**
     * This static method returns an array of all currently active phases.
     *
     * @return an array of active phases. This return value will never be
     *         <code>null</code>, but can be an empty array (for completed or
     *         cancelled projects, etc.)
     * @param phases an array of phases to retrieve all active phases from.
     * @throws IllegalArgumentException if <code>phases</code> parameter is
     *                                  <code>null</code>.
     */
    public static Phase[] getActivePhases(Phase[] phases) {
        // Validate parameter
        validateParameterNotNull(phases, "phases");

        List<Phase> activePhases = new ArrayList<Phase>();

        for (Phase phase : phases) {
            // Get a phase for the current iteration
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
     * @return the phase, or <code>null</code> if there is no phase with specified
     *         name.
     * @param phases     an array of phases to search for the particular phase
     *                   specified by <code>phaseName</code> and
     *                   <code>activeOnly</code> parameters.
     * @param activeOnly determines whether this method should search for active
     *                   phases only. If this parameter set to <code>false</code>,
     *                   the parameter <code>phaseName</code> is required.
     * @param phaseName  Optional name of the phase to search for if there is a
     *                   possibility that more than one phase is active, or the
     *                   search is not being performed for active phase.
     * @throws IllegalArgumentException if <code>phases</code> parameter is
     *                                  <code>null</code>.
     */
    public static Phase getPhase(Phase[] phases, boolean activeOnly, String phaseName) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");

        if (activeOnly) {
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
            for (Phase activePhase : activePhases) {
                if (activePhase.getPhaseType().getName().equalsIgnoreCase(phaseName)) {
                    return activePhase;
                }
            }
            // Active phase with specified name has not been found
            return null;
        } else {
            // Phase name should be specified if the search is done for (possibly) closed
            // phase
            validateParameterStringNotEmpty(phaseName, "phaseName");

            Phase phaseFound = null;

            for (Phase phase : phases) {
                // Get a phase for the current iteration
                // Get a name of status of this phase
                String phaseStatus = phase.getPhaseStatus().getName();
                // If the phase found that is not yet open, stop the search
                /*
                 * if (phaseFound != null &&
                 * phaseStatus.equals(PhaseStatus.SCHEDULED.getName())) { break; }
                 */

                // If the name of the current phase matches the one
                // specified by method's parameter, remember this phase
                if (!phaseStatus.equals(PhaseStatus.SCHEDULED.getName())
                        && phase.getPhaseType().getName().equalsIgnoreCase(phaseName)) {
                    phaseFound = phase;
                }
            }

            // The phaseFound variable will contain the latest phase that has already been
            // closed
            // or is currently open, or null if no phase with the required name has been
            // found
            return phaseFound;
        }
    }

    /**
     * Get phase for the deliverable.
     *
     * @return the phase found.
     * @param phases      the phases to find.
     * @param deliverable the deliverable.
     */
    public static Phase getPhaseForDeliverable(Phase[] phases, Deliverable deliverable) {
        // Validate parameters
        validateParameterNotNull(phases, "phases");
        validateParameterNotNull(deliverable, "deliverable");

        for (Phase phase : phases) {
            if (phase.getId() == deliverable.getPhase()) {
                return phase;
            }
        }

        return null;
    }

    /**
     * This static method retrieves a scorecard template for the specified phase.
     *
     * @return found Scorecard template, or <code>null</code> if no scorecard
     *         template was associated with the phase.
     * @param phase    a phase to retrieve scorecard template from.
     * @param useCache whether to look for the cached scorecards
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>.
     * @throws NumberFormatException    if an error occurred while converting
     *                                  scorecard ID to its integer representation.
     * @throws PersistenceException     if an error occurred while accessing the
     *                                  database.
     */
    public static Scorecard getScorecardTemplateForPhase(Phase phase, boolean useCache) throws PersistenceException {
        // Validate parameters
        validateParameterNotNull(phase, "phase");

        // Get an ID of scorecard template associated with this phase
        String scorecardId = (String) phase.getAttribute("Scorecard ID");
        // If no scorecard template is assigned to phase, return null value.
        if (scorecardId == null || scorecardId.trim().length() == 0) {
            return null;
        }

        Long longScorecardId = Long.parseLong(scorecardId, 10);
        if (!useCache || !cachedScorecards.containsKey(longScorecardId)) {
            Scorecard scorecard = createScorecardManager().getScorecard(longScorecardId);
            cachedScorecards.put(longScorecardId, scorecard);
        }

        return cachedScorecards.get(longScorecardId);
    }

    /**
     * This static method retrieves the resources for a project.
     *
     * @return an array of the resources for the specified project.
     * @param project a project to retrieve the resources for.
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>.
     * @throws BaseException            if any error occurs.
     */
    public static Resource[] getAllResourcesForProject(Project project) throws BaseException {
        // Validate parameters
        validateParameterNotNull(project, "project");

        // Build a filter to fetch all resources for the current project
        Filter filter = ResourceFilterBuilder.createProjectIdFilter(project.getId());
        // Perform a search for the resources and return them
        return createResourceManager().searchResources(filter);
    }

    /**
     * This static method retrieves the resources for a phase.
     *
     * @return an array of the resources for the specified phase.
     * @param phase a phase to retrieve the resources for.
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>.
     * @throws BaseException            if any error occurs.
     */
    public static Resource[] getAllResourcesForPhase(Phase phase) throws BaseException {
        // Validate parameters
        validateParameterNotNull(phase, "phase");

        // Prepare filter to select resource by project ID
        Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(phase.getProject().getId());
        // Prepare filter to select resource by phase ID
        Filter filterPhase = ResourceFilterBuilder.createPhaseIdFilter(phase.getId());
        // Combine the above filters into one
        Filter filter = new AndFilter(filterProject, filterPhase);

        // Perform a search for the resources and return them
        return createResourceManager().searchResources(filter);
    }

    /**
     * This static method retrieves the resources for a phase using provided array
     * of resources.
     *
     * @return an array of the resources for the specified phase.
     * @param resources an array of resources to retrieve needed resources from.
     * @param phase     a phase to retrieve the resources for.
     * @throws IllegalArgumentException if parameters <code>resources</code> is
     *                                  <code>null</code>.
     */
    public static Resource[] getResourcesForPhase(Resource[] resources, Phase phase) {
        // Validate parameters
        validateParameterNotNull(resources, "resources");

        List<Resource> foundResources = new ArrayList<Resource>();

        for (Resource resource : resources) {
            // Get a resource for the current iteration
            // If this resource is from phase in question, add it to the list
            if (phase == null) {
                if (resource.getPhase() == null) {
                    foundResources.add(resource);
                }
            } else {
                // Handle Post-Mortem, Approval, Iterative Review phases differently. Those
                // resources are not mapped
                // to phase type so they must be discovered based on resource role name
                if (phase.getPhaseType().getName().equals(Constants.POST_MORTEM_PHASE_NAME)) {
                    if (resource.getResourceRole().getName().equals(Constants.POST_MORTEM_REVIEWER_ROLE_NAME)) {
                        foundResources.add(resource);
                    }
                } else if (phase.getPhaseType().getName().equals(Constants.APPROVAL_PHASE_NAME)) {
                    if (resource.getResourceRole().getName().equals(Constants.APPROVER_ROLE_NAME)) {
                        foundResources.add(resource);
                    }
                } else if (phase.getPhaseType().getName().equals(Constants.ITERATIVE_REVIEW_PHASE_NAME)) {
                    if (resource.getResourceRole().getName().equals(Constants.ITERATIVE_REVIEWER_ROLE_NAME)) {
                        foundResources.add(resource);
                    }
                } else {
                    if (resource.getPhase() != null && resource.getPhase() == phase.getId()) {
                        foundResources.add(resource);
                    }
                }
            }
        }

        // Convert the list of resources to an array and return it
        return foundResources.toArray(new Resource[foundResources.size()]);
    }

    /**
     * This static method searches the provided array of resources for submitters
     * (resources that have Submitter role) and returns an array of all found
     * resources.
     *
     * @return an array of submitters found in the provided array of resources.
     * @param resources an array of resource to search for submitters among.
     * @throws IllegalArgumentException if parameter <code>resources</code> is
     *                                  <code>null</code>.
     */
    public static Resource[] getAllSubmitters(Resource[] resources) {
        // Validate parameter
        validateParameterNotNull(resources, "resources");

        List<Resource> submitters = new ArrayList<Resource>();
        // Search for the appropriate resources and add them to the list
        for (Resource resource : resources) {
            if (resource.getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                submitters.add(resource);
            }
        }

        // Convert the list of found submitters to array and return it
        return submitters.toArray(new Resource[submitters.size()]);
    }

    /**
     * Get the winner of an project.
     *
     * @param projectId the project id
     * @return the winner resource
     * @throws BaseException if any error
     */
    public static Resource getWinner(long projectId) throws BaseException {
        ProjectManager projectManager = createProjectManager();
        ResourceManager resourceManager = createResourceManager();

        Project project = projectManager.getProject(projectId);
        Long winnerId;
        try {
            winnerId = Long.parseLong((String) project.getProperty("Winner External Reference ID"));
        } catch (NumberFormatException nfe) {
            winnerId = null;
        }

        if (winnerId != null) {
            long submitterRoleId = LookupHelper.getResourceRole("Submitter").getId();

            AndFilter fullFilter = new AndFilter(
                    Arrays.asList(ResourceFilterBuilder.createResourceRoleIdFilter(submitterRoleId),
                            ResourceFilterBuilder.createProjectIdFilter(projectId),
                            ResourceFilterBuilder.createUserIdFilter(winnerId)));

            Resource[] submitters = resourceManager.searchResources(fullFilter);
            if (submitters.length > 0) {
                return submitters[0];
            }
            return null;
        }
        return null;
    }

    /**
     * This static method selects the resources for a project from the list of all
     * resources supplied to this method.
     *
     * @return an array containing just the resources that belong to the project
     *         specified. The returned array will be empty, if there are no such
     *         resources.
     * @param allResources all the resources to select a subset from.
     * @param project      project which the subset of resources is needed for.
     * @throws IllegalArgumentException if <code>project</code> parameter is
     *                                  <code>null</code>.
     */
    public static Resource[] getResourcesForProject(Resource[] allResources, Project project) {
        // Validate parameters
        validateParameterNotNull(project, "project");

        // If the given list of resources is null or empty, return empty subset
        // immediately
        if (allResources == null || allResources.length == 0) {
            return new Resource[0];
        }

        List<Resource> myResources = new ArrayList<Resource>();

        for (Resource resource : allResources) {
            // Get a resource for the current iteration
            // Determine if the resource is for current project
            if (resource.getProject() != null && (resource.getProject()) == project.getId()) {
                myResources.add(resource);
            }
        }

        // Convert the list to array and return it
        return myResources.toArray(new Resource[myResources.size()]);
    }

    /**
     * This static method returns the array of resources for the currently logged in
     * user associated with the specified phase. The list of all resources for the
     * currently logged in user is retrieved from the
     * <code>HttpServletRequest</code> object specified by <code>request</code>
     * parameter. Method <code>gatherUserRoles(HttpServletRequest, long)</code>
     * should be called prior making a call to this method.
     *
     * @return an array of found resources, or empty array if no resources for
     *         currently logged in user found such that those resources would be
     *         associated with the specified phase.
     * @param request an <code>HttpServletRequest</code> object containing
     *                additional information.
     * @param phase   a phase to search the resources for. This parameter can be
     *                <code>null</code>, in which case the search is made for
     *                resources with no phase assigned.
     * @throws IllegalArgumentException if <code>request</code> parameter is
     *                                  <code>null</code>.
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
     * This static method returns the resource for role for the currently logged in
     * user. The list of all resources for the currently logged in user is retrieved
     * from the <code>HttpServletRequest</code> object specified by
     * <code>request</code> parameter. Method
     * <code>gatherUserRoles(HttpServletRequest, long)</code> should be called prior
     * making a call to this method.
     *
     * @return the resource, or null if the current user doesn't have a resource
     *         with the role.
     * @param request      an <code>HttpServletRequest</code> object containing
     *                     additional information.
     * @param resourceRole the name of the resource role
     * @throws IllegalArgumentException if <code>request</code> parameter is
     *                                  <code>null</code>. if
     *                                  <code>resourceRole</code> parameter is
     *                                  <code>null</code>.
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
        return null;
    }

    /**
     * This static method retrieves the resource for the currently logged in user
     * associated with the specified phase. The list of all resources for the
     * currently logged in user is retrieved from the
     * <code>HttpServletRequest</code> object specified by <code>request</code>
     * parameter. Method <code>gatherUserRoles(HttpServletRequest, long)</code>
     * should be called prior making a call to this method.
     *
     * @return found resource, or <code>null</code> if no resource for currently
     *         logged in user found such that that resource would be associated with
     *         the specified phase.
     * @param request an <code>HttpServletRequest</code> object containing
     *                additional information.
     * @param phase   a phase to search the resource for. This parameter can be
     *                <code>null</code>, in which case the search is made for
     *                resources with no phase assigned.
     * @throws IllegalArgumentException if <code>request</code> parameter is
     *                                  <code>null</code>.
     */
    public static Resource getMyResourceForPhase(HttpServletRequest request, Phase phase) {
        Resource[] resources = getMyResourcesForPhase(request, phase);
        return (resources.length != 0) ? resources[0] : null;
    }

    /**
     * This static method retrieves all deliverables for all active phases for a
     * specific project. This method returns either completed or incomplete
     * deliverables.
     *
     * @return an array of deliverables.
     * @param phases    an array of phases to search deliverables for.
     * @param resources an array of all resources for the current project.
     * @throws IllegalArgumentException        if any of the <code>manager</code>,
     *                                         <code>phases</code> or
     *                                         <code>resources</code> parameters are
     *                                         <code>null</code>.
     * @throws DeliverablePersistenceException if an error occurs while reading from
     *                                         the persistence store.
     * @throws SearchBuilderException          if an error occurs executing the
     *                                         filter.
     * @throws DeliverableCheckingException    if an error occurs when determining
     *                                         whether a Deliverable has been
     *                                         completed or not.
     */
    public static Deliverable[] getAllDeliverablesForPhases(Phase[] phases, Resource[] resources)
            throws DeliverablePersistenceException, SearchBuilderException, DeliverableCheckingException {

        // Validate parameters
        validateParameterNotNull(phases, "phases");

        // A filter to search for deliverables for specific phase(s) of the project
        Filter phaseFilter;
        switch (phases.length) {
        case 0:
            // No phases -- no deliverables
            return new Deliverable[0];

        case 1:
            // If there is only one phase in the provided array,
            // create filter for it directly (no OR filters needed)
            phaseFilter = DeliverableFilterBuilder.createPhaseIdFilter(phases[0].getId());
            break;

        default:
            List<Filter> phaseFilters = new ArrayList<Filter>();
            // Prepare a list of filters for each phase in the array of phases
            for (Phase phase : phases) {
                phaseFilters.add(DeliverableFilterBuilder.createPhaseIdFilter(phase.getId()));
            }
            // Combine all filters using OR operator
            phaseFilter = new OrFilter(phaseFilters);
        }

        Filter resourceRoleFilter = new NotFilter(new EqualToFilter("resource_role_id", 1));
        Filter filter = new AndFilter(Arrays.asList(phaseFilter, resourceRoleFilter));

        // Perform a search for the deliverables
        Deliverable[] allDeliverables = createDeliverableManager().searchDeliverables(filter, null);

        List<Deliverable> deliverables = new ArrayList<Deliverable>();

        for (Deliverable deliverable : allDeliverables) {
            // Get a deliverable for the current iteration
            // Get an ID of resource this deliverable is for
            final long deliverableResourceId = deliverable.getResource();
            Resource forResource = null;
            // Find a resource this deliverable is for
            for (Resource resource : resources) {
                if (resource.getId() == deliverableResourceId) {
                    forResource = resource;
                    break;
                }
            }
            // There must be a resource associated with this deliverable, but
            // in case there isn't skip this deliverable for safety
            if (forResource == null) {
                continue;
            }

            // Add current deliverable to the list of deliverables
            deliverables.add(deliverable);
        }

        // Convert the list of deliverables into array and return it
        return deliverables.toArray(new Deliverable[deliverables.size()]);
    }

    /**
     * This static method retrieves outstanding deliverables from the list of
     * deliverables. A deliverable is considered to be outstanding if it is not
     * completed.
     *
     * @return an array of outstanding deliverables.
     * @param allDeliverables an array of all deliverables to search for outstanding
     *                        ones.
     * @throws IllegalArgumentException if <code>allDeliverables</code> parameter is
     *                                  <code>null</code>.
     */
    public static Deliverable[] getOutstandingDeliverables(Deliverable[] allDeliverables) {
        // Validate parameter
        validateParameterNotNull(allDeliverables, "allDeliverables");

        List<Deliverable> deliverables = new ArrayList<Deliverable>();
        // Perform a search for outstanding deliverables
        for (Deliverable deliverable : allDeliverables) {
            if (!deliverable.isComplete()) {
                addDeliverable(deliverable, allDeliverables, deliverables);
            }
        }
        // Return a list of outstanding deliverables converted to array
        return deliverables.toArray(new Deliverable[deliverables.size()]);
    }

    /**
     * This static method retrieves deliverables for resource roles the logged in
     * user has.
     *
     * @return an array of deliverables assigned to the currently logged in user.
     * @param allDeliverables an array of all deliverables to search for outstanding
     *                        ones.
     * @param myResources     an array of all resources assigned to the currently
     *                        logged in user for a particular project.
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>.
     */
    public static Deliverable[] getMyDeliverables(Deliverable[] allDeliverables, Resource[] myResources) {
        // Validate parameters
        validateParameterNotNull(allDeliverables, "allDeliverables");
        validateParameterNotNull(myResources, "myResources");

        List<Deliverable> deliverables = new ArrayList<Deliverable>();
        // Perform a search for "my" deliverables
        for (Deliverable deliverable : allDeliverables) {
            // Get a deliverable for current iteration
            boolean found = false;
            // Determine if this deliverable is assigned to currently logged in user
            for (Resource myResource : myResources) {
                if (deliverable.getResource() == myResource.getId()) {
                    found = true;
                    break;
                }
            }
            // If found is true, it means that current deliverable is assigned to currently
            // logged in user
            if (found) {
                addDeliverable(deliverable, allDeliverables, deliverables);
            }
        }
        // Return a list of "my" deliverables converted to array
        return deliverables.toArray(new Deliverable[deliverables.size()]);
    }

    /**
     * This static method retrieves an array of external user objects for the
     * specified array of resources. Each entry in the resulting array will
     * correspond to the corresponding entry in the input <code>resources</code>
     * array. If there are no matches found for some resource, the corresponding
     * item in the resulting array will contain <code>null</code>.
     *
     * @return an array of external user objects for the specified resources.
     * @param retrieval a <code>UserRetrieval</code> object used to retrieve
     *                  external user objects.
     * @param resources an array of resources to retrieve corresponding external
     *                  user objects for.
     * @throws IllegalArgumentException if any of the parameters are
     *                                  <code>null</code>.
     * @throws RetrievalException       if some error happened during external user
     *                                  retrieval.
     */
    public static ExternalUser[] getExternalUsersForResources(UserRetrieval retrieval, Resource[] resources)
            throws RetrievalException {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(retrieval, "retrieval");
        ActionsHelper.validateParameterNotNull(resources, "resources");

        // If there are no resource for this project defined, there will be no external
        // users
        if (resources.length == 0) {
            return new ExternalUser[0];
        }

        // Prepare an array to store External User IDs
        Long[] extUserIds = new Long[resources.length];
        // Fill the array with user IDs retrieved from resource properties
        for (int i = 0; i < resources.length; ++i) {
            extUserIds[i] = resources[i].getUserId();
        }

        // Retrieve external users to the temporary array
        ExternalUser[] extUsers = retrieval.retrieveUsers(extUserIds);

        // This is final array for External User objects. It is needed because the
        // previous
        // operation may return shorter array than there are resources for the project
        // (sometimes several resources can be associated with one external user)
        ExternalUser[] allExtUsers = new ExternalUser[resources.length];

        for (int i = 0; i < extUserIds.length; ++i) {
            for (ExternalUser extUser : extUsers) {
                if (extUser.getId() == extUserIds[i]) {
                    allExtUsers[i] = extUser;
                    break;
                }
            }
        }

        return allExtUsers;
    }

    /**
     * <p>
     * Gets the list of submissions of specified type and status for specified
     * project.
     * </p>
     *
     * @param projectId            ID of the project.
     * @param submissionTypeName   a <code>String</code> providing the name of
     *                             desired submission type. If null, all submission
     *                             types will be included.
     * @param submissionStatusName a <code>String</code> providing the name of
     *                             desired submission status. If null, all
     *                             submission statuses will be included.
     * @param includeDeleted       true if the Deleted status should be included and
     *                             false otherwise.
     * @return a <code>Submission</code> array listing the submissions of specified
     *         type for project.
     * @throws BaseException if an unexpected error occurs.
     */
    public static Submission[] getProjectSubmissions(long projectId, String submissionTypeName,
                                                     String submissionStatusName, boolean includeDeleted) throws BaseException {

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(SubmissionFilterBuilder.createProjectIdFilter(projectId));

        if (submissionTypeName != null) {
            long submissionTypeId = LookupHelper.getSubmissionType(submissionTypeName).getId();
            filters.add(SubmissionFilterBuilder.createSubmissionTypeIdFilter(submissionTypeId));
        }

        if (submissionStatusName != null) {
            long submissionStatusId = LookupHelper.getSubmissionStatus(submissionStatusName).getId();
            filters.add(SubmissionFilterBuilder.createSubmissionStatusIdFilter(submissionStatusId));
        }

        if (!includeDeleted) {
            // Exclude the Deleted status
            long deletedStatusId = LookupHelper.getSubmissionStatus("Deleted").getId();
            filters.add(new NotFilter(SubmissionFilterBuilder.createSubmissionStatusIdFilter(deletedStatusId)));
        }

        return createUploadManager().searchSubmissions(new AndFilter(filters));
    }

    /**
     * <p>
     * Gets the list of submissions of specified type and status for specified
     * resource.
     * </p>
     *
     * @param resourceID           ID of the resource.
     * @param submissionTypeName   a <code>String</code> providing the name of
     *                             desired submission type. If null, all submission
     *                             types will be included.
     * @param submissionStatusName a <code>String</code> providing the name of
     *                             desired submission status. If null, all
     *                             submission statuses will be included.
     * @param includeDeleted       true if the Deleted status should be included and
     *                             false otherwise.
     * @return a <code>Submission</code> array listing the submissions of specified
     *         type for project.
     * @throws BaseException if an unexpected error occurs.
     */
    public static Submission[] getResourceSubmissions(long resourceID, String submissionTypeName,
            String submissionStatusName, boolean includeDeleted) throws BaseException {

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(SubmissionFilterBuilder.createResourceIdFilter(resourceID));

        if (submissionTypeName != null) {
            long submissionTypeId = LookupHelper.getSubmissionType(submissionTypeName).getId();
            filters.add(SubmissionFilterBuilder.createSubmissionTypeIdFilter(submissionTypeId));
        }

        if (submissionStatusName != null) {
            long submissionStatusId = LookupHelper.getSubmissionStatus(submissionStatusName).getId();
            filters.add(SubmissionFilterBuilder.createSubmissionStatusIdFilter(submissionStatusId));
        }

        if (!includeDeleted) {
            // Exclude the Deleted status
            long deletedStatusId = LookupHelper.getSubmissionStatus("Deleted").getId();
            filters.add(new NotFilter(SubmissionFilterBuilder.createSubmissionStatusIdFilter(deletedStatusId)));
        }

        return createUploadManager().searchSubmissions(new AndFilter(filters));
    }

    /**
     * <p>
     * Gets the earliest (i.e. with the earliest upload time) active submission of
     * specified type for the specified project.
     * </p>
     *
     * @param projectId          ID of the project.
     * @param submissionTypeName a <code>String</code> providing the name of desired
     *                           submission type. If null, all submission types will
     *                           be considered.
     * @return <code>Submission</code> of the specified type for project with
     *         earliest possible upload time.
     * @throws BaseException if an unexpected error occurs.
     */
    public static Submission getEarliestActiveSubmission(long projectId, String submissionTypeName)
            throws BaseException {
        Submission[] submissions = getProjectSubmissions(projectId, submissionTypeName,
                Constants.ACTIVE_SUBMISSION_STATUS_NAME, false);
        Submission earliestSubmission = null;
        for (Submission submission : submissions) {
            if (earliestSubmission == null
                    || earliestSubmission.getCreationTimestamp().compareTo(submission.getCreationTimestamp()) > 0) {
                earliestSubmission = submission;
            }
        }
        return earliestSubmission;
    }

    /**
     * This static method retrieves the project that the submission specified by the
     * <code>submission</code> parameter was made for.
     *
     * @return a retrieved project.
     * @param submission a submission to retrieve the project for.
     * @throws IllegalArgumentException                             if any of the
     *                                                              parameters are
     *                                                              <code>null</code>.
     */
    public static Project getProjectForSubmission(Submission submission) {
        // Validate parameters
        ActionsHelper.validateParameterNotNull(submission, "submission");

        // Get an upload for this submission
        Upload upload = submission.getUpload();
        // Return Project by its id
        return createProjectManager().getProject(upload.getProject());
    }

    /**
     * Check if it is in or after the phase.
     *
     * @return true if in or after phase.
     * @param phases     the phases to be checked
     * @param phaseIndex the phase index
     * @param phaseName  the phase name
     * @throws IllegalArgumentException if <code>phases</code> parameter is
     *                                  <code>null</code> or if
     *                                  <code>phaseIndex</code> parameter is not
     *                                  within valid range of the array specified by
     *                                  <code>phases</code> parameter (thus, there
     *                                  is no way to pass an empty array to this
     *                                  method).
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
     * Check if the phase is after appeal response.
     *
     * @param phases     the phases to be checked.
     * @param phaseIndex the phase index
     * @return true if the phase is after appeal response.
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

            if (phaseName.equalsIgnoreCase(Constants.REGISTRATION_PHASE_NAME)
                    || phaseName.equalsIgnoreCase(Constants.SUBMISSION_PHASE_NAME)
                    || phaseName.equalsIgnoreCase(Constants.CHECKPOINT_SUBMISSION_PHASE_NAME)
                    || phaseName.equalsIgnoreCase(Constants.CHECKPOINT_SCREENING_PHASE_NAME)
                    || phaseName.equalsIgnoreCase(Constants.CHECKPOINT_REVIEW_PHASE_NAME)) {
                if (prevPhase) {
                    return true;
                }
                prevPhase = false;
                continue;
            }
            prevPhase = true;
            if (phaseName.equalsIgnoreCase(Constants.REVIEW_PHASE_NAME)
                    || phaseName.equalsIgnoreCase(Constants.ITERATIVE_REVIEW_PHASE_NAME)
                    || phaseName.equalsIgnoreCase(Constants.APPEALS_PHASE_NAME)
                    || phaseName.equalsIgnoreCase(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                if (!phase.getPhaseStatus().getName().equals(PhaseStatus.CLOSED.getName())) {
                    return false;
                }
                found = true;
                continue;
            }
            if (found) {
                return true;
            }
        }
        return true;
    }

    /**
     * Check if the phase is after appeal response.
     *
     * @param phases the phases to be checked.
     * @return true if after appeal response
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
        // If all phases are closed, then we should definitely be past Appeals Response
        // one
        if (i >= phases.length) {
            return true;
        }

        boolean anyOtherPhaseFound = false;

        for (; i >= 0; --i) {
            // Get a phase for the current iteration
            Phase phase = phases[i];
            String phaseName = phase.getPhaseType().getName();

            // If registration or Submission phase found before the Open one (or one of
            // those
            // phases is currently Open), return false, as we are not past Appeals Response
            // one
            if (phaseName.equalsIgnoreCase(Constants.REGISTRATION_PHASE_NAME)
                    || phaseName.equalsIgnoreCase(Constants.SUBMISSION_PHASE_NAME)) {
                return false;
            }
            // Skip the Open or Scheduled phase, as only Closed phases make interest
            if (!phase.getPhaseStatus().getName().equals(PhaseStatus.CLOSED.getName())) {
                continue;
            }
            // If Appeals response is the closed phase,
            // then definitely the project is at after Appeals Response stage
            if (phaseName.equalsIgnoreCase(Constants.APPEALS_RESPONSE_PHASE_NAME)) {
                return true;
            }
            // If the phase Review or Appeals is found, but there were other closed phases
            // after them,
            // regard this as after Appeals Response (if Appeals Response is actually
            // absent)
            if (anyOtherPhaseFound && (phaseName.equalsIgnoreCase(Constants.APPEALS_PHASE_NAME)
                    || phaseName.equalsIgnoreCase(Constants.REVIEW_PHASE_NAME))) {
                return true;
            }
            anyOtherPhaseFound = true;
        }

        // If i is negative, the needed phase has not been found
        // The project is not in after Appeals Response phase
        return (i >= 0);
    }

    // -------------------------------------------------------------- Creator type
    // of methods -----

    /**
     * This static method helps to create an object of the <code>PhaseManager</code>
     * class.
     *
     * @return instance of the class PhaseManager
     * @param registerPhaseHandlers a boolean parameter that determines whether
     *                              version of phase handlers with or without phase
     *                              handlers should be returned
     */
    public static PhaseManager createPhaseManager(boolean registerPhaseHandlers) {
        if (registerPhaseHandlers) {
            // get Phase Manager with handlers
            return managerCreationHelper.getPhaseManager();
        } else {
            // get Phase Manager without handlers
            return managerCreationHelper.getPhaseManagerWithoutHandlers();
        }
    }

    /**
     * This static method helps to create an object of
     * <code>ProjectPaymentAdjustmentManager</code> class.
     *
     * @return instance of the class ProjectPaymentAdjustmentManager
     */
    public static ProjectPaymentAdjustmentManager createProjectPaymentAdjustmentManager() {
        return managerCreationHelper.getProjectPaymentAdjustmentManager();
    }

    /**
     * This static method helps to create an object of
     * <code>ProjectPaymentManager</code> class.
     *
     * @return instance of the class ProjectPaymentManager
     */
    public static ProjectPaymentManager createProjectPaymentManager() {
        return managerCreationHelper.getProjectPaymentManager();
    }

    /**
     * This static method helps to create an object of the
     * <code>ProjectManager</code> class.
     *
     * @return a newly created instance of the class.
     * @throws IllegalArgumentException if <code>request</code> parameter is
     *                                  <code>null</code>.
     */
    public static ProjectManager createProjectManager() {
        return managerCreationHelper.getProjectManager();
    }

    /**
     * This static method helps to create an object of the
     * <code>ResourceManager</code> class.
     *
     * @return a newly created instance of the class.
     */
    public static ResourceManager createResourceManager() {
        return managerCreationHelper.getResourceManager();
    }

    /**
     * This static method helps to create an object of the
     * <code>ReviewManager</code> class.
     *
     * @return a newly created instance of the class.
     */
    public static ReviewManager createReviewManager() {
        return managerCreationHelper.getReviewManager();
    }

    /**
     * This static method helps to create an object of the
     * <code>LateDeliverableManager</code> class.
     *
     * @return a newly created instance of the class.
     */
    public static LateDeliverableManager createLateDeliverableManager() {
        return managerCreationHelper.getLateDeliverableManager();
    }

    /**
     * This static method helps to create an object of the
     * <code>ScorecardManager</code> class.
     *
     * @return a newly created instance of the class.
     */
    public static ScorecardManager createScorecardManager() {
        return managerCreationHelper.getScorecardManager();
    }

    /**
     * This static method helps to create an object of the
     * <code>DeliverableManager</code> class.
     *
     * @return a newly created instance of the class.
     */
    public static DeliverableManager createDeliverableManager() {
        return managerCreationHelper.getDeliverableManager();
    }

    /**
     * This static method helps to create an object of the
     * <code>UploadManager</code> class.
     *
     * @return a newly created instance of the class.
     */
    public static UploadManager createUploadManager() {
        return managerCreationHelper.getUploadManager();
    }

    /**
     * This static method helps to create an object of the
     * <code>ProjectLinkManager</code> class.
     *
     * @return a newly created instance of the class.
     */
    public static ProjectLinkManager createProjectLinkManager() {
        return managerCreationHelper.getProjectLinkManager();
    }

    /**
     * Gets the <code>UserTermsOfUseDao</code> instance.
     *
     * @return the <code>UserTermsOfUseDao</code> instance.
     */
    public static UserTermsOfUseDao getUserTermsOfUseDao() {
        return managerCreationHelper.getUserTermsOfUseDao();
    }

    /**
     * Gets the <code>ProjectTermsOfUseDao</code> instance.
     *
     * @return the <code>ProjectTermsOfUseDao</code> instance.
     */
    public static ProjectTermsOfUseDao getProjectTermsOfUseDao() {
        return managerCreationHelper.getProjectTermsOfUseDao();
    }

    /**
     * Gets the <code>ProjectTermsOfUseDao</code> instance.
     *
     * @return the <code>ProjectTermsOfUseDao</code> instance.
     */
    public static TermsOfUseDao getTermsOfUseDao() {
        return managerCreationHelper.getTermsOfUseDao();
    }

    /**
     * This static method helps to create an object of the
     * <code>ReviewFeedbackManager</code> class.
     *
     * @return a newly created instance of the class.
     */
    public static ReviewFeedbackManager createReviewFeedbackManager() {
        return managerCreationHelper.getReviewFeedbackManager();
    }

    /**
     * This static method helps to create an object of the
     * <code>UserRetrieval</code> class.
     *
     * @return a newly created instance of the class.
     * @param request an <code>HttpServletRequest</code> object, where created
     *                <code>UserRetrieval</code> object can be stored to let reusing
     *                it later for the same request.
     * @throws IllegalArgumentException                         if
     *                                                          <code>request</code>
     *                                                          parameter is
     *                                                          <code>null</code>.
     */
    public static UserRetrieval createUserRetrieval(HttpServletRequest request) {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Upload Retrieval from the request's attribute first
        UserRetrieval manager = (UserRetrieval) request.getAttribute("userRetrieval");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = managerCreationHelper.getUserRetrieval();
            // Place newly-created object into the request as attribute
            request.setAttribute("userRetrieval", manager);
        }

        // Return the Upload Retrieval object
        return manager;
    }

    /**
     * This static method helps to create an object of the <code>FileUpload</code>
     * class.
     *
     * @return a newly created instance of the class.
     * @param request an <code>HttpServletRequest</code> object, where created
     *                <code>UserRetrieval</code> object can be stored to let reusing
     *                it later for the same request.
     */
    public static FileUpload createFileUploadManager(HttpServletRequest request) {
        return createFileUploadManager(request, LOCAL_STORAGE_NAMESPACE);
    }

    
    public static FileUpload createFileUploadManager(HttpServletRequest request, String namespace) {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving File Upload from the request's attribute first
        FileUpload fileUpload = (FileUpload) request.getAttribute(namespace);
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (fileUpload == null) {
            fileUpload = getBean(LocalFileUpload.class);
            // Place newly-created object into the request as attribute
            request.setAttribute(namespace, fileUpload);
        }

        // Return the File Upload object
        return fileUpload;
    }

    /**
     * This static method helps to get a list of cockpit projects belonging to the
     * given user.
     *
     * @param request the request
     * @return a list of cockpit projects
     */
    public static List<CockpitProject> getCockpitProjects(HttpServletRequest request) {
        validateParameterNotNull(request, "request");

        long userId = AuthorizationHelper.getLoggedInUserId(request);

        List<CockpitProject> cockpitProjects = new ArrayList<CockpitProject>();

        CockpitProject project = new CockpitProject();
        project.setId(0);
        project.setName("-------------");
        cockpitProjects.add(project);

        if (AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
            cockpitProjects.addAll(getBean(ProjectDataAccess.class).getAllCockpitProjects());
        } else {
            cockpitProjects.addAll(getBean(ProjectDataAccess.class).getCockpitProjectsForUser(userId));
        }
        return cockpitProjects;
    }

    /**
     * This static method helps to get a list of <code>ClientProject</code>.
     *
     * @return a list of <code>ClientProject</code>
     * @param request an <code>HttpServletRequest</code> object, where created list
     *                of <code>ClientProject</code> can be stored to let reusing it
     *                later for the same request.
     * @throws IllegalArgumentException if <code>request</code> parameter is
     *                                  <code>null</code>.
     */
    public static List<ClientProject> getClientProjects(HttpServletRequest request) {
        validateParameterNotNull(request, "request");

        long userId = AuthorizationHelper.getLoggedInUserId(request);

        List<ClientProject> clientProjects = new ArrayList<ClientProject>();

        // We first add an empty client project for a default selection.
        ClientProject project = new ClientProject();
        project.setId(0);
        project.setName("-------------");
        clientProjects.add(project);

        if (AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)) {
            clientProjects.addAll(getBean(ProjectDataAccess.class).getAllClientProjects());
        } else {
            clientProjects.addAll(getBean(ProjectDataAccess.class).getClientProjectsForUser(userId));
        }
        return clientProjects;
    }

    /**
     * This static method helps to create an object of the
     * <code>PhaseTemplate</code> class.
     *
     * @return a newly created instance of the class. if <code>request</code>
     *         parameter is <code>null</code>.
     * @throws BaseException if any error happens during object creation.
     */
    public static PhaseTemplate createPhaseTemplate() throws BaseException {
        return getBean(PhaseTemplate.class);
    }

    /**
     * Set Completion Timestamp while the project turn to completed, Cancelled -
     * Failed Review or Deleted status.
     *
     * @param project          the project instance
     * @param newProjectStatus new project status
     * @param format           the date format
     */
    public static void setProjectCompletionDate(Project project, ProjectStatus newProjectStatus, Format format) {

        String name = newProjectStatus.getName();
        if ("Completed".equals(name) || "Cancelled - Failed Review".equals(name) || "Deleted".equals(name)
                || "Cancelled - Failed Screening".equals(name) || "Cancelled - Zero Submissions".equals(name)
                || "Cancelled - Winner Unresponsive".equals(name) || "Cancelled - Client Request".equals(name)
                || "Cancelled - Requirements Infeasible".equals(name)) {

            if (format == null) {
                format = new SimpleDateFormat(ConfigHelper.getDateFormat());
            }

            project.setProperty("Completion Timestamp", format.format(new Date()));
        }
    }

    /**
     * Set Rated Timestamp with the end date of submission phase.
     *
     * @param project       the project instance
     * @param format        the date format
     * @param projectPhases the project phases
     */
    public static void setProjectRatingDate(Project project, Phase[] projectPhases, Format format) {
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
     * This method verifies the request for certain conditions to be met. This
     * includes verifying if the user has specified an ID of the project he wants to
     * perform an operation on, if the ID of the project specified by user denotes
     * existing project, and whether the user has rights to perform the operation
     * specified by <code>permission</code> parameter.
     *
     * Eligibility checks: - If there is no logged in user and the project has
     * eligibility constraints, ask for login. - If the user is logged in, is not a
     * resource of the project and the project has eligibility constraints, don't
     * allow him access.
     *
     * @return an instance of the {@link CorrectnessCheckResult} class, which
     *         specifies whether the check was successful and, in the case it was,
     *         contains additional information retrieved during the check operation,
     *         which might be of some use for the calling method.
     * @param textProvider              the text provider.
     * @param request                   the http request.
     * @param permission                permission to check against, or
     *                                  <code>null</code> if no check is required.
     * @param getRedirectUrlFromReferer if it is a redirect url from referer
     * @throws BaseException if any error occurs.
     */
    public static CorrectnessCheckResult checkForCorrectProjectId(TextProvider textProvider, HttpServletRequest request,
            String permission, boolean getRedirectUrlFromReferer) throws BaseException {
        // Prepare bean that will be returned as the result
        CorrectnessCheckResult result = new CorrectnessCheckResult();

        if (permission == null || permission.trim().length() == 0) {
            permission = null;
        }

        // Verify that Project ID was specified and denotes correct project
        String pidParam = request.getParameter("pid");
        if (pidParam == null || pidParam.trim().length() == 0) {
            result.setResult(
                    produceErrorReport(textProvider, request, permission, "Error.ProjectIdNotSpecified", false));
            // Return the result of the check
            return result;
        }

        long pid;

        try {
            // Try to convert specified pid parameter to its integer representation
            pid = Long.parseLong(pidParam, 10);
        } catch (NumberFormatException nfe) {
            result.setResult(produceErrorReport(textProvider, request, permission, "Error.ProjectNotFound", false));
            // Return the result of the check
            return result;
        }

        // Obtain an instance of Project Manager
        ProjectManager projMgr = createProjectManager();
        // Get Project by its id
        Project project = projMgr.getProject(pid);
        // Verify that project with given ID exists
        if (project == null) {
            result.setResult(produceErrorReport(textProvider, request, permission, "Error.ProjectNotFound", false));
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
                AuthorizationHelper.hasUserRole(request, Constants.MANAGER_ROLE_NAME)
                        || AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)
                        || AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME));

        // If permission parameter was not null or empty string ...
        if (permission != null) {
            // ... verify that this permission is granted for currently logged in user
            if (!AuthorizationHelper.hasUserPermission(request, permission)) {
                // If it does not, and the user is logged in, display a message about the lack
                // of
                // permissions, otherwise redirect the request to the Login page
                result.setResult(produceErrorReport(textProvider, request, permission, "Error.NoPermission",
                        getRedirectUrlFromReferer));
                // Return the result of the check
                return result;
            }
        }

        // new eligibility constraints checks
        try {
            if (AuthorizationHelper.isUserLoggedIn(request)) {

                // if the user is logged in and is a resource of this project or a global
                // manager, continue
                Resource[] myResources = (Resource[]) request.getAttribute("myResources");
                if ((myResources == null || myResources.length == 0)
                        && !AuthorizationHelper.hasUserRole(request, Constants.GLOBAL_MANAGER_ROLE_NAME)
                        && !AuthorizationHelper.hasUserRole(request, Constants.COCKPIT_PROJECT_USER_ROLE_NAME)) {
                    // if he's not a resource, check if the project has eligibility constraints
                    if (EJBLibraryServicesLocator.getContestEligibilityService().hasEligibility(pid, false)) {
                        result.setResult(
                                produceErrorReport(textProvider, request, permission, "Error.ProjectNotFound", false));
                        // Return the result of the check
                        return result;
                    }
                }
            } else {
                // if the user is not logged in and the project has any eligibility constraint,
                // ask for login
                if (EJBLibraryServicesLocator.getContestEligibilityService().hasEligibility(pid, false)) {
                    result.setResult(produceErrorReport(textProvider, request, permission, "Error.NoPermission",
                            getRedirectUrlFromReferer));
                    // Return the result of the check
                    return result;
                }
            }
        } catch (Exception e) {
            throw new BaseException("It was not possible to verify eligibility for project id " + pid, e);
        }

        // At this point, redirect-after-login attribute should be removed (if it
        // exists)
        AuthorizationHelper.removeLoginRedirect(request);

        return result;
    }

    /**
     * Populate project_result and component_inquiry for new submitters.
     *
     * @param project       the project
     * @param newSubmitters new submitters external ids.
     * @throws BaseException if error occurs
     */
    public static void populateProjectResult(Project project, Collection<Long> newSubmitters) throws BaseException {
        long categoryId = project.getProjectCategory().getId();

        if (!isProjectResultCategory(categoryId)) {
            return;
        }
        ActionsHelperServiceRpc actionsHelperServiceRpc = GrpcHelper.getActionsHelperServiceRpc();
        long projectId = project.getId();
        // retrieve and update component_inquiry_id
        long componentInquiryId = getNextComponentInquiryId(newSubmitters.size());
        long componentId = getProjectLongValue(project, "Component ID");
        long phaseId = 111 + project.getProjectCategory().getId();
        log.debug("calculated phaseId for Project: " + projectId + " phaseId: " + phaseId);
        long version = getProjectLongValue(project, "Version ID");
        for (Long userId : newSubmitters) {

            // Check if projectResult exist
            boolean existPR = actionsHelperServiceRpc.isProjectResultExists(userId, projectId);

            // Check if component_inquiry exist
            boolean existCI = actionsHelperServiceRpc.isComponentInquiryExists(userId, projectId);

            // Retrieve oldRating
            double oldRating = 0;
            if (!existPR || !existCI) {
                List<RatingProto> ratings =  actionsHelperServiceRpc.getRatings(userId, projectId);
                // If the project belongs to a rated category, the user gets the rating that
                // belongs to the
                // category. Otherwise, the highest available rating is used.
                for (RatingProto rating: ratings) {
                    if (!isRatedCategory(rating.getProjectCategoryId())) {
                        if (oldRating < rating.getRating()) {
                            oldRating = rating.getRating();
                        }
                    } else if (rating.getProjectCategoryId() + 111 == rating.getPhaseId()) {
                        oldRating = rating.getRating();
                    }
                }
            }

            if (!existPR) {
                // add project_result
                actionsHelperServiceRpc.createProjectResult(userId, projectId, 0L, 0L, oldRating == 0 ? null : oldRating);
            }

            // add component_inquiry
            if (!existCI && componentId > 0) {
                log.debug("adding component_inquiry for projectId: " + projectId + " userId: " + userId);
                actionsHelperServiceRpc.createComponentInquiry(componentInquiryId++, componentId, userId, projectId,
                        categoryId == 1 || categoryId == 2 ? phaseId : null, userId, 1L, oldRating, version);
            }
        }
    }

    /**
     * Get the catalog for a component.
     *
     * @param componentId project id
     * @return the root category id
     */
    public static String getRootCategoryIdByComponentId(Object componentId) {
        ActionsHelperServiceRpc actionsHelperServiceRpc = GrpcHelper.getActionsHelperServiceRpc();
        try {
            String rootCategoryId = actionsHelperServiceRpc.getRootCategoryIdByComponentId(componentId.toString());
            if (rootCategoryId != null) {
                return rootCategoryId;
            }
        } catch (Exception e) {
            // Ignore if no corresponding root_category_id exist
        }

        return "9926572"; // If we can't find a catalog, assume it's an Application
    }

    /**
     * Retrieve all default scorecards.
     *
     * @return the default scorecards list
     * @throws BaseException if error occurs
     */
    public static List<DefaultScorecard> getDefaultScorecards() throws BaseException {
        ActionsHelperServiceRpc actionsHelperServiceRpc = GrpcHelper.getActionsHelperServiceRpc();
        List<DefaultScorecardProto> scorecards = actionsHelperServiceRpc.getDefaultScorecards();
        List<DefaultScorecard> list = new ArrayList<DefaultScorecard>();
        for (DefaultScorecardProto row : scorecards) {
            DefaultScorecard scorecard = new DefaultScorecard();
            scorecard.setCategory(row.getProjectCategoryId());
            scorecard.setScorecardType(row.getScorecardTypeId());
            scorecard.setScorecardId(row.getScorecardId());
            if (row.hasName()) {
                scorecard.setName(row.getName());
            }
            list.add(scorecard);
        }
        return list;
    }

    /**
     * Delete project_result and component_inquiry for new submitters if oldRole is
     * submitter, added otherwise.
     *
     * @param project   the project
     * @param userId    userId
     * @param oldRoleId roleId
     * @param newRoleId new role id
     * @throws BaseException if error occurs
     */
    public static void changeResourceRole(Project project, long userId, long oldRoleId, long newRoleId)
            throws BaseException {
        long categoryId = project.getProjectCategory().getId();

        if (isProjectResultCategory(categoryId)) {
            if (oldRoleId == 1) {
                // Delete project_result if the old role is submitter
                deleteProjectResult(project, userId, oldRoleId);
            }

            if (newRoleId == 1) {
                // added otherwise
                populateProjectResult(project, Arrays.asList(userId));
            }
        }
    }

    /**
     * Delete project_result and component_inquiry for new submitters.
     *
     * @param project the project
     * @param userId  userId
     * @param roleId  roleId
     * @throws BaseException if error occurs
     */
    public static void deleteProjectResult(Project project, long userId, long roleId) throws BaseException {
        long categoryId = project.getProjectCategory().getId();

        if (!isProjectResultCategory(categoryId)) {
            return;
        }

        if (roleId != 1) {
            // Only deal with submitters
            return;
        }
        ActionsHelperServiceRpc actionsHelperServiceRpc = GrpcHelper.getActionsHelperServiceRpc();
        // delete from project_result
        actionsHelperServiceRpc.deleteProjectResult(project.getId(), userId);
        // delete from component_inquiry
        actionsHelperServiceRpc.deleteComponentInquiry(project.getId(), userId);
    }

    /**
     * Reset ProjectResult after score change.
     *
     * @param project  the Project instance
     * @param operator the operator
     *
     * @throws BaseException if any error occurs
     */
    public static void resetProjectResultWithChangedScores(Project project, String operator) throws BaseException {
        try {
            if (isStudioProject(project)) {
                PaymentsHelper.processAutomaticPayments(project.getId(), operator);
            } else {
                PRHelper.populateProjectResult(GrpcHelper.getPhaseHandlerServiceRpc(), project.getId(), operator);
            }
        } catch (Exception e) {
            throw new BaseException("Failed to resetProjectResultWithChangedScores for project " + project.getId(), e);
        }
    }

    /**
     * Update the project_result table when the user's submission has advanced
     * screening.
     *
     * @param projectId the id of the project needs to be updated
     * @param userId    the user id whose submission will advance screening
     * @throws BaseException if any error occurs
     */
    public static void updateProjectResultForAdvanceScreening(long projectId, long userId) throws BaseException {
        ActionsHelperServiceRpc actionsHelperServiceRpc = GrpcHelper.getActionsHelperServiceRpc();
        actionsHelperServiceRpc.updateProjectResultForAdvanceScreening(projectId, userId);
    }

    /**
     * Gets version from comp_versions table.
     *
     * @param componentVersionId the component version id
     * @return the version used
     *
     * @throws BaseException if error occurs
     */
    public static int getVersionUsingComponentVersionId(long componentVersionId) throws BaseException {
        ActionsHelperServiceRpc actionsHelperServiceRpc = GrpcHelper.getActionsHelperServiceRpc();
        Integer version = actionsHelperServiceRpc.getVersionUsingComponentVersionId(componentVersionId);
        if (version == null) {
            return 0;
        }
        return version;
    }

    /**
     * Retrieve and update next ComponentInquiryId.
     *
     * @param jdbcTemplate  the jdbcTemplate
     * @param count the count of new submitters
     * @return next component_inquiry_id
     * @throws BaseException if any error
     */
    private static long getNextComponentInquiryId(int count) throws BaseException {
        ActionsHelperServiceRpc actionsHelperServiceRpc = GrpcHelper.getActionsHelperServiceRpc();
        while (true) {
            long currentValue = actionsHelperServiceRpc.getNextComponentInquiryId();

            // Update the next value
            int ret = actionsHelperServiceRpc.updateNextComponentInquiryId(currentValue + count, currentValue);
            if (ret > 0) {
                return currentValue;
            }
        }
    }

    /**
     * Return project property long value.
     *
     * @param project the project object
     * @param name    the property name
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
     * Returns true if the given project is of category studio.
     *
     * @param project the project to be be checked.
     * @return true, if the project is of type studio.
     */
    public static boolean isStudioProject(Project project) {
        return "Studio".equals(project.getProjectCategory().getProjectType().getName());
    }

    /**
     * <p>
     * Gets all uploads of the specified type associated with the specified project
     * phase.
     * </p>
     *
     * @param projectPhaseId a <code>long</code> providing the ID of a project
     *                       phase.
     * @param uploadTypeName a <code>String</code> providing the name of desired
     *                       upload type. If null, all upload types will be
     *                       included.
     * @return an arrays of <code>Upload</code> objects for the specified project
     *         phase.
     * @throws BaseException if an unexpected error occurs.
     */
    public static Upload[] getPhaseUploads(long projectPhaseId, String uploadTypeName) throws BaseException {
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(UploadFilterBuilder.createProjectPhaseIdFilter(projectPhaseId));
        if (uploadTypeName != null) {
            long uploadTypeId = LookupHelper.getUploadType(uploadTypeName).getId();
            filters.add(UploadFilterBuilder.createUploadTypeIdFilter(uploadTypeId));
        }

        return createUploadManager().searchUploads(new AndFilter(filters));
    }

    /**
     * <p>
     * Gets final fix upload for the specified approval phase.
     * </p>
     *
     * @param approvalPhase Approval phase.
     * @return an <code>Upload</code> for the specified approval phase or null if
     *         not present.
     * @throws BaseException if an unexpected error occurs.
     */
    public static Upload getFinalFixForApprovalPhase(Phase approvalPhase) throws BaseException {

        Phase finalReview = null;
        for (Dependency dep : approvalPhase.getAllDependencies()) {
            if (dep.getDependent() == approvalPhase
                    && dep.getDependency().getPhaseType().getName().equals("Final Review")) {
                finalReview = dep.getDependency();
                break;
            }
        }
        if (finalReview == null) {
            return null;
        }

        Phase finalFix = null;
        for (Dependency dep : finalReview.getAllDependencies()) {
            if (dep.getDependent() == finalReview && dep.getDependency().getPhaseType().getName().equals("Final Fix")) {
                finalFix = dep.getDependency();
                break;
            }
        }
        if (finalFix == null) {
            return null;
        }

        Upload[] uploads = getPhaseUploads(finalFix.getId(), "Final Fix");
        return uploads.length > 0 ? uploads[0] : null;
    }

    /**
     * <p>
     * Recalculates scheduled start date and end date for all phases when a phase is
     * moved.
     * </p>
     *
     * @param allPhases all the phases for the project.
     */
    static void recalculateScheduledDates(Phase[] allPhases) {
        for (Phase phase : allPhases) {
            Date newStartDate = phase.calcStartDate();
            Date newEndDate = phase.calcEndDate();
            phase.setScheduledStartDate(newStartDate);
            phase.setScheduledEndDate(newEndDate);
        }
    }

    /**
     * <p>
     * This static method finds and returns all reviews for the specified phase and
     * resource.
     * </p>
     *
     * @param phaseId    Phase ID.
     * @param resourceId an ID of the resource who made (created) the review. Can be
     *                   null.
     * @param complete   specifies whether retrieved review should have all
     *                   information (like all items and their comments).
     * @return found review or <code>null</code> if no review has been found.
     * @throws ReviewManagementException if any error occurs during review search or
     *                                   retrieval.
     */
    public static Review[] searchReviews(long phaseId, Long resourceId, boolean complete)
            throws ReviewManagementException {
        Filter filter = new EqualToFilter("projectPhase", phaseId);

        if (resourceId != null) {
            filter = new AndFilter(filter, new EqualToFilter("reviewer", resourceId));
        }

        return createReviewManager().searchReviews(filter, complete);
    }

    /**
     * Map single user to a users collection.
     * 
     * @param user the user
     * @return the users collection
     */
    private static Collection<Long> userToUsers(Long user) {
        ArrayList<Long> userCollection = new ArrayList<Long>();
        userCollection.add(user);
        return userCollection;
    }

    /**
     * <p>
     * Searches the resources for specified user for projects of specified status.
     * If status parameter is null it will search for the 'global' resources with no
     * project associated.
     * </p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @param status a <code>ProjectStatus</code> specifying the status of the
     *               projects.
     * @return a <code>Resource</code> array providing the details for found
     *         resources.
     * @throws ResourcePersistenceException                           if an error
     *                                                                occurs while
     *                                                                retrieving
     *                                                                resource
     *                                                                roles.
     *                                                                error occurs.
     */
    public static Resource[] searchUserResources(long userId, ProjectStatus status)
            throws ResourcePersistenceException {
        return getBean(ResourceDataAccess.class).searchUserResources(userId, status, createResourceManager());
    }

    /**
     * <p>
     * Checks if all all dependencies for specified phase (if any) are met.
     * </p>
     *
     * @param phase         a <code>Phase</code> providing the details for phase to
     *                      check.
     * @param phaseStarting <code>true</code> if specified phase is going to be
     *                      started; <code>false</code> otherwise.
     * @return <code>true</code> if specified phase has no dependencies or all
     *         existing dependencies have been met; <code>false</code> otherwise.
     */
    public static boolean arePhaseDependenciesMet(Phase phase, boolean phaseStarting) {
        Dependency[] dependencies = phase.getAllDependencies();

        if ((dependencies == null) || (dependencies.length == 0)) {
            return true;
        }

        for (Dependency dependency : dependencies) {
            Phase dependencyPhase = dependency.getDependency();
            if (phaseStarting) {
                if (dependency.isDependencyStart() && dependency.isDependentStart()) {
                    if (!(isPhaseOpen(dependencyPhase.getPhaseStatus())
                            || isPhaseClosed(dependencyPhase.getPhaseStatus()))) {
                        return false;
                    }
                } else if (!dependency.isDependencyStart() && dependency.isDependentStart()) {
                    if (!isPhaseClosed(dependencyPhase.getPhaseStatus())) {
                        return false;
                    }
                }
            } else {
                if (dependency.isDependencyStart() && !dependency.isDependentStart()) {
                    if (!(isPhaseOpen(dependencyPhase.getPhaseStatus())
                            || isPhaseClosed(dependencyPhase.getPhaseStatus()))) {
                        return false;
                    }
                } else if (!dependency.isDependencyStart() && !dependency.isDependentStart()) {
                    if (!isPhaseClosed(dependencyPhase.getPhaseStatus())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * <p>
     * Checks if specified phase status is <code>Closed</code> status.
     * </p>
     *
     * @param status a <code>PhaseStatus</code> to check.
     * @return <code>true</code> if specified phase status is <code>Closed</code>
     *         status; <code>false</code> otherwise.
     */
    public static boolean isPhaseClosed(PhaseStatus status) {
        return (PHASE_STATUS_CLOSED.equals(status.getName()));
    }

    /**
     * <p>
     * Checks if specified phase status is <code>Open</code> status.
     * </p>
     *
     * @param status a <code>PhaseStatus</code> to check.
     * @return <code>true</code> if specified phase status is <code>Open</code>
     *         status; <code>false</code> otherwise.
     */
    public static boolean isPhaseOpen(PhaseStatus status) {
        return (PHASE_STATUS_OPEN.equals(status.getName()));
    }

    /**
     * <p>
     * Adds specified deliverable to specified list of collected deliverables.
     * </p>
     *
     * @param deliverable           a <code>Deliverable</code> to be added to
     *                              collected list of deliverables.
     * @param allDeliverables       an <code>Deliverable</code> array listing all
     *                              deliverables for project.
     * @param collectedDeliverables a <code>List</code> collecting the valid
     *                              deliverables.
     */
    private static void addDeliverable(Deliverable deliverable, Deliverable[] allDeliverables,
            List<Deliverable> collectedDeliverables) {
        // For specification submission deliverables there is a need to check if there
        // is no specification
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
     * <p>
     * Checks if <code>Specification Submission</code> is already delivered by
     * another resource for same phase mapped to specified deliverable.
     * </p>
     *
     * @param deliverable     a <code>Deliverable</code> to be added to collected
     *                        list of deliverables.
     * @param allDeliverables an <code>Deliverable</code> array listing all
     *                        deliverables for project.
     * @return <code>true</code> if <code>Specification Submission</code> is already
     *         delivered; <code>false</code> otherwise.
     */
    private static boolean isSpecificationSubmissionAlreadyDelivered(Deliverable deliverable,
            Deliverable[] allDeliverables) {
        for (Deliverable otherDeliverable : allDeliverables) {
            if (Constants.SPECIFICATION_SUBMISSION_DELIVERABLE_NAME.equals(otherDeliverable.getName())
                    && (otherDeliverable.getPhase() == deliverable.getPhase()) && otherDeliverable.isComplete()
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
     * @throws BaseException if error occurs
     */
    public static Map<Long, String> getDeliverableIdToNameMap(HttpServletRequest request) throws BaseException {
        Map<Long, String> idToNameMap = (Map<Long, String>) request.getAttribute("deliverableIdToNameMap");

        if (idToNameMap == null) {
            idToNameMap = new HashMap<>();
            ActionsHelperServiceRpc actionsHelperServiceRpc = GrpcHelper.getActionsHelperServiceRpc();
            List<DeliverabIdNameProto> deliveribles = actionsHelperServiceRpc.getDeliverableIdToNameMap();
            for (DeliverabIdNameProto row: deliveribles) {
                idToNameMap.put(row.getDeliverableId(), row.getName());
            }

            request.setAttribute("deliverableIdToNameMap", idToNameMap);

        }

        return idToNameMap;
    }

    /**
     * <p>
     * Returns list of user IDs who have specified resource roles for the specified
     * project.
     * </p>
     *
     * @param roleNames a <code>String</code> array representing the resource role
     *                  names.
     * @param projectID ID of the project.
     * @return the user ids by role name
     * @throws BaseException if an unexpected error occurs.
     */
    public static List<Long> getUserIDsByRoleNames(String[] roleNames, long projectID) throws BaseException {
        if ((roleNames != null) && (roleNames.length > 0)) {
            // Build filters
            List<Filter> roleFilters = new ArrayList<Filter>();
            for (String roleName : roleNames) {
                ResourceRole role = LookupHelper.getResourceRole(roleName);
                roleFilters.add(ResourceFilterBuilder.createResourceRoleIdFilter(role.getId()));
            }
            Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(projectID);
            Filter filterRole = new OrFilter(roleFilters);
            Filter filter = new AndFilter(filterProject, filterRole);

            Resource[] resources = createResourceManager().searchResources(filter);

            // Collect unique external user IDs first as there may exist multiple resources
            // for the same user
            Set<Long> userIDs = new HashSet<Long>();
            for (Resource resource : resources) {
                userIDs.add(resource.getUserId());
            }

            return new ArrayList<Long>(userIDs);
        }

        return new ArrayList<Long>();
    }

    /**
     * <p>
     * Returns list of the email addresses for the specified user IDs.
     * </p>
     *
     * @param request the http request.
     * @param userIDs list of user IDs.
     * @return emails by user ids
     * @throws BaseException if an unexpected error occurs.
     */
    public static List<String> getEmailsByUserIDs(HttpServletRequest request, List<Long> userIDs) throws BaseException {
        UserRetrieval userRetrieval = createUserRetrieval(request);

        List<String> emails = new ArrayList<String>();
        for (Long userID : userIDs) {
            emails.add(userRetrieval.retrieveUser(userID).getEmail());
        }
        return emails;
    }

    /**
     * <p>
     * Returns the deadline date for submitting the explanation for the late
     * deliverable.
     * </p>
     *
     * @param lateDeliverable late deliverable.
     * @return explanation deadline date.
     */
    public static Date explanationDeadline(LateDeliverable lateDeliverable) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(lateDeliverable.getCreateDate());
        cal.add(Calendar.HOUR, 24);
        return cal.getTime();
    }

    /**
     * <p>
     * Saves the attempt to download a file to the project_download_audit table in
     * the DB.
     * </p>
     *
     * @param request    The http request.
     * @param upload     The upload object that is being downloaded.
     * @param successful True if the download attempt was successful and false
     *                   otherwise (e.g. no permission).
     * @throws BaseException if any error
     */
    public static void logDownloadAttempt(HttpServletRequest request, Upload upload, boolean successful)
            throws BaseException {
        ActionsHelperServiceRpc actionsHelperServiceRpc = GrpcHelper.getActionsHelperServiceRpc();
        actionsHelperServiceRpc.logDownloadAttempt(upload.getId(),
                AuthorizationHelper.isUserLoggedIn(request) ? AuthorizationHelper.getLoggedInUserId(request) : null,
                request.getRemoteAddr(), successful);
    }

    /**
     * Delete the Post-Mortem phase of a specified project.
     *
     * @param project         the specified project.
     * @param postMortemPhase the Post-Mortem phase to be deleted.
     * @param operator        the operator
     * @throws BaseException if any error occurs
     */
    public static void deletePostMortem(Project project, Phase postMortemPhase, String operator) throws BaseException {
        validateParameterNotNull(project, "project");
        validateParameterNotNull(postMortemPhase, "postMortemPhase");

        ReviewManager reviewMgr = createReviewManager();
        ResourceManager resMgr = createResourceManager();
        PhaseManager phaseManager = createPhaseManager(false);

        // Get and delete all the Post Mortem reviews
        Review[] reviews = searchReviews(postMortemPhase.getId(), null, false);
        for (Review review : reviews) {
            reviewMgr.removeReview(review.getId(), operator);
        }

        // Get all the Post Mortem Reviewers
        Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(project.getId());
        long postMortemRoleId = LookupHelper.getResourceRole(Constants.POST_MORTEM_REVIEWER_ROLE_NAME).getId();
        Filter filterResourceRole = ResourceFilterBuilder.createResourceRoleIdFilter(postMortemRoleId);
        Filter filter = new AndFilter(filterProject, filterResourceRole);
        Resource[] resources = resMgr.searchResources(filter);

        // Delete the Post Mortem Reviewers
        for (Resource resource : resources) {
            resMgr.removeResource(resource, operator);
        }

        // Delete the Post Mortem Phase
        com.topcoder.onlinereview.component.project.phase.Project phProject = postMortemPhase.getProject();
        phProject.removePhase(postMortemPhase);
        phaseManager.updatePhases(phProject, operator);
    }

    /**
     * Checks whether a string is a non-negative number, the number can only be
     * accurate to 0.01.
     *
     * @param value        the string value to check.
     * @param property     the form property name of the value.
     * @param invalidKey   the error message key to be used when the string is
     *                     invalid.
     * @param precisionKey the error message key to be used when the precision is
     *                     invalid.
     * @param request      the instance of HttpServletRequest.
     * @param allowZero    true if allowing zero, false otherwise.
     * @return true if the string value is valid, false otherwise.
     */
    public static boolean checkNonNegDoubleWith2Decimal(String value, String property, String invalidKey,
            String precisionKey, HttpServletRequest request, boolean allowZero) {
        BigDecimal bigNum = null;
        try {
            bigNum = new BigDecimal(value, MathContext.UNLIMITED);
        } catch (NumberFormatException e) {
            // ignore
        }
        if (bigNum == null) {
            addErrorToRequest(request, property, invalidKey);
            return false;
        }
        int cmp = bigNum.compareTo(BigDecimal.ZERO);
        if (cmp < 0 || (!allowZero && cmp == 0)) {
            addErrorToRequest(request, property, invalidKey);
            return false;
        }

        bigNum = bigNum.movePointRight(2);
        BigDecimal sub = bigNum.subtract(new BigDecimal(bigNum.toBigInteger()));
        if (sub.compareTo(BigDecimal.ZERO) != 0) {
            addErrorToRequest(request, property, precisionKey);
            return false;
        }
        return true;
    }

    /**
     * Gets the last modification time for a project.
     *
     * @param project the project.
     * @param phases  the phases of the project.
     * @return the last modification time.
     */
    public static Date getLastModificationTime(Project project, Phase[] phases) {
        Date lastModificationTime = project.getModificationTimestamp();
        for (Phase phase : phases) {
            if (phase.getModifyDate().after(lastModificationTime)) {
                lastModificationTime = phase.getModifyDate();
            }
        }
        return lastModificationTime;
    }

    public static void outputDownloadS3File(String url, String key, String contentDisposition,
            HttpServletResponse response) throws IOException {
        try {
            log.info("Will download from S3 with key " + key + " for url " + url);

            S3Object s3Object = s3Client.getObject(new GetObjectRequest(s3Bucket, key));
            InputStream in = (InputStream) s3Object.getObjectContent();

            response.setHeader("Content-Type", s3Object.getObjectMetadata().getContentType());
            response.setStatus(HttpServletResponse.SC_OK);
            response.setIntHeader("Content-Length", (int) s3Object.getObjectMetadata().getContentLength());
            response.setHeader("Content-Disposition", contentDisposition);

            response.flushBuffer();

            OutputStream out = null;

            try {
                out = response.getOutputStream();
                byte[] buffer = new byte[65536];

                for (;;) {
                    int numOfBytesRead = in.read(buffer);
                    if (numOfBytesRead == -1) {
                        break;
                    }
                    out.write(buffer, 0, numOfBytesRead);
                }
            } finally {
                in.close();
                if (out != null) {
                    out.close();
                }
            }
        } catch (Exception e) {
            log.error("ex: " + e.getMessage());
            throw new IOException("Error S3 download", e);
        }
    }

    /**
     * Check upload url is on dmz
     *
     * @param url upload url
     * @return true if uploadfile is on dmz bucket
     */
    public static boolean isDmzBucket(String url) {
        AmazonS3URI s3Uri = isS3Url(url);
        if (s3Uri == null) {
            return false;
        }
        log.info("S3 Bucket from url: " + s3Uri.getBucket());
        return s3BucketDmz.equals(s3Uri.getBucket());
    }

    /**
     * Check upload url is on quarantine
     *
     * @param url upload url
     * @return true if uploadfile is on quarantine bucket
     */
    public static boolean isQuarantineBucket(String url) {
        AmazonS3URI s3Uri = isS3Url(url);
        if (s3Uri == null) {
            return false;
        }
        return s3BucketQuarantine.equals(s3Uri.getBucket());
    }

    /**
     * Check upload url is a valid S3 url.
     *
     * @param url upload url
     * @return true if uploadfile is on dmz bucket
     */
    public static AmazonS3URI isS3Url(String url) {
        try {
            AmazonS3URI s3Uri = new AmazonS3URI(url);
            return s3Uri;
        } catch (Exception ex) {
            // url doesn't seem to be a valid
            return null;
        }
    }

    /**
     * Create local studio path
     * 
     * @param projectId  project id
     * @param userId     user id the owner of file
     * @param userHandle user handle of owner of file
     * @param parameter  upload parameter of file
     */
    public static String createStudioLocalFilePath(long projectId, long userId, String userHandle, String parameter) {
        StringBuffer buf = new StringBuffer(80);
        buf.append(projectId);
        buf.append(System.getProperty("file.separator"));
        buf.append(userHandle.toLowerCase());
        buf.append("_");
        buf.append(userId);
        buf.append(System.getProperty("file.separator"));
        buf.append(parameter);
        return buf.toString();
    }

    public void setManagerCreationHelper(ManagerCreationHelper managerCreationHelper) {
        ActionsHelper.managerCreationHelper = managerCreationHelper;
    }
}
