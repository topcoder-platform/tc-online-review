/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectmanagementconsole;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;


import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.actions.DynamicModelDrivenAction;
import com.cronos.onlinereview.dataaccess.CatalogDataAccess;
import com.cronos.onlinereview.ejblibrary.SpringContextProvider;
import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.model.FormFile;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.LookupException;
import com.cronos.onlinereview.util.LookupHelper;
import com.topcoder.dde.catalog.ComponentVersionInfo;
import com.topcoder.dde.catalog.Document;
import com.topcoder.management.payment.ProjectPaymentAdjustment;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculator;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseStatusEnum;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.reviewfeedback.ReviewFeedback;
import com.topcoder.management.reviewfeedback.ReviewFeedbackManager;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.OrFilter;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.distribution.DistributionTool;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.errorhandling.BaseRuntimeException;

/**
 * This is the base class for project management console actions classes.
 * It provides the basic functions which will be used by all project management actions.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public abstract class BaseProjectManagementConsoleAction extends DynamicModelDrivenAction {

    /**
     * <p>A <code>long</code> providing the constant value for single hour duration in milliseconds.</p>
     */
    protected static final long HOUR_DURATION_IN_MILLIS = 60 * 60 * 1000L;

    /**
     * <p>A <code>long</code> providing the constant value for single day duration in milliseconds.</p>
     */
    protected static final long DAY_DURATION_IN_MILLIS = 24 * 60 * 60 * 1000L;

    /**
     * <p>A <code>long</code> providing the constant value for copilot resource role id. </p>
     */
    protected static final long COPILOT_RESOURCE_ROLE_ID = 14;

    /**
     * <p>A <code>long</code> providing the constant value for client manager resource role id.</p>
     */
    protected static final long CLIENT_MANAGER_RESOURCE_ROLE_ID = 15;

        /**
     * <p>A <code>long</code> providing the constant value for observer resource role id.</p>
     */
    protected static final long OBSERVER_RESOURCE_ROLE_ID = 12;

    /**
     * <p>A <code>long</code> providing the constant value for designer resource role id.</p>
     */
    protected static final long DESIGNER_RESOURCE_ROLE_ID = 11;

    /**
     * The id of the design distribution document type.
     */
    protected static final long DESIGN_DISTRIBUTION_DOC_TYPE_ID = 25;

    /**
     * The id of the development distribution document type.
     */
    protected static final long DEVELOPMENT_DISTRIBUTION_DOC_TYPE_ID = 26;

    /**
     * The design distribution document type.
     */
    protected static final String DESIGN_DISTRIBUTION_DOC_TYPE = "Design Distribution";

    /**
     * The development distribution document type.
     */
    protected static final String DEVELOPMENT_DISTRIBUTION_DOC_TYPE = "Development Distribution";

    /**
     * Design project ID.
     */
    protected static final long DESIGN_PROJECT_ID = 1;

    /**
     * Development project ID.
     */
    protected static final long DEVELOPMENT_PROJECT_ID = 2;

    /**
     * <p>
     * The directory to upload files that will be used by distribution tool. It is appended to the distribution tool
     * output dir.
     * </p>
     */
    protected static final String UPLOADED_ARTIFACTS_DIR = "upload_0";

    /**
     * <p>Valid package names.</p>
     */
    protected static final Pattern PACKAGE_PATTERN = Pattern
        .compile("\\s*(([a-zA-Z])[a-zA-Z0-9_]*)(\\.([a-zA-Z])[a-zA-Z0-9_]*)*\\s*");

    /**
     * <p>Valid version numbers.</p>
     */
    protected static final Pattern VERSION_PATTERN = Pattern.compile("\\s*([1-9][0-9]*)(\\.[0-9]+){0,3}\\s*");

    /**
     * DistributionTool is thread-safe, so we can keep it as an instance variable.
     */
    protected static final DistributionTool DISTRIBUTION_TOOL = new DistributionTool();

    /**
     * Default date format.
     */
    protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM.dd.yyyy hh:mm a", Locale.US);

    /**
     * The max length of review feedback text.
     */
    protected static final int MAX_FEEDBACK_LENGTH = 4096;

    /**
     * The instance of default project payment calculator.
     */
    protected static final ProjectPaymentCalculator defaultProjectPaymentCalculator = createDefaultProjectPaymentCalculator();

    /**
     * This member variable is a constant array that holds names of different reviewer roles.
     */
    private static final String[] REVIEW_FEEDBACK_ELIGIBLE_ROLE_NAMES = new String[]{
        Constants.REVIEWER_ROLE_NAME, Constants.ACCURACY_REVIEWER_ROLE_NAME, Constants.FAILURE_REVIEWER_ROLE_NAME,
        Constants.STRESS_REVIEWER_ROLE_NAME, Constants.ITERATIVE_REVIEWER_ROLE_NAME};

    /**
     * Represents the project id.
     */
    private long pid;

    /**
     * Gets the available reviewer roles which we can set the review payments for a project.
     *
     * @param project the project.
     * @return the list of role ids which we can set the review payments for the project.
     * @throws BaseException if any error occurs.
     */
    private static List<Long> getAvailableReviewerRoles(Project project) throws BaseException {
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);
        Phase[] phases = ActionsHelper.getPhasesForProject(phaseManager, project);
        List<Long> roleIds = new ArrayList<Long>();
        List<String> roleNames = new ArrayList<String>();
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.SCREENING_PHASE_NAME) != null) {
            roleNames.add(Constants.PRIMARY_SCREENER_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.REVIEW_PHASE_NAME) != null) {
            if (project.getProjectCategory().getName().equals("Development")) {
                roleNames.add(Constants.ACCURACY_REVIEWER_ROLE_NAME);
                roleNames.add(Constants.STRESS_REVIEWER_ROLE_NAME);
                roleNames.add(Constants.FAILURE_REVIEWER_ROLE_NAME);
            } else {
                roleNames.add(Constants.REVIEWER_ROLE_NAME);
            }
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.AGGREGATION_PHASE_NAME) != null) {
            roleNames.add(Constants.AGGREGATOR_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.FINAL_REVIEW_PHASE_NAME) != null) {
            roleNames.add(Constants.FINAL_REVIEWER_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.SPECIFICATION_REVIEW_PHASE_NAME) != null) {
            roleNames.add(Constants.SPECIFICATION_REVIEWER_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.CHECKPOINT_SCREENING_PHASE_NAME) != null) {
            roleNames.add(Constants.CHECKPOINT_SCREENER_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.CHECKPOINT_REVIEW_PHASE_NAME) != null) {
            roleNames.add(Constants.CHECKPOINT_REVIEWER_ROLE_NAME);
        }
        if (ActionsHelper.findPhaseByTypeName(phases, Constants.ITERATIVE_REVIEW_PHASE_NAME) != null) {
            roleNames.add(Constants.ITERATIVE_REVIEWER_ROLE_NAME);
        }

        ResourceRole[] allRoles = ActionsHelper.createResourceManager().getAllResourceRoles();
        for (String roleName : roleNames) {
            for (ResourceRole role : allRoles) {
                if (role.getName().equals(roleName)) {
                    roleIds.add(role.getId());
                    break;
                }
            }
        }
        return roleIds;
    }

    /**
     * Sets the review payments data of a project into request attribute.
     *
     * @param request the HttpServletRequest instance.
     * @param project the project.
     * @throws BaseException if any error occurs.
     */
    private static void setReviewPaymentsRequestAttribute(HttpServletRequest request, Project project) throws BaseException {
        List<Long> resourceRoleIds = getAvailableReviewerRoles(project);
        ResourceRole[] allRoles = ActionsHelper.createResourceManager().getAllResourceRoles();

        String[] resourceRoles = new String[resourceRoleIds.size()];
        for (int i = 0; i < resourceRoleIds.size(); i++) {
            for (ResourceRole role : allRoles) {
                if (role.getId() == resourceRoleIds.get(i)) {
                    resourceRoles[i] = role.getName();
                    break;
                }
            }
        }

        Map<Long, BigDecimal> defaultPayments = defaultProjectPaymentCalculator.getDefaultPayments(project.getId(), resourceRoleIds);
        List<ProjectPaymentAdjustment> paymentAdjusts = ActionsHelper.createProjectPaymentAdjustmentManager().retrieveByProjectId(project.getId());
        Map<Long, Double> fixedAmount = new HashMap<Long, Double>();
        Map<Long, Integer> percentAmount = new HashMap<Long, Integer>();
        for (ProjectPaymentAdjustment paymentAdjust : paymentAdjusts) {
            if (paymentAdjust.getFixedAmount() != null) {
                fixedAmount.put(paymentAdjust.getResourceRoleId(), paymentAdjust.getFixedAmount().doubleValue());
            } else if (paymentAdjust.getMultiplier() != null) {
                percentAmount.put(paymentAdjust.getResourceRoleId(), (int) (paymentAdjust.getMultiplier().doubleValue() * 100.0));
            }
        }

        List<String> radios = new ArrayList<String>();
        List<String> fixed = new ArrayList<String>();
        List<String> percentage = new ArrayList<String>();
        for (Long resourceRoleId : resourceRoleIds) {
            boolean chooseDefault = true;
            if (fixedAmount.containsKey(resourceRoleId)) {
                radios.add("fixed");
                fixed.add(fixedAmount.get(resourceRoleId).toString());
                chooseDefault = false;
            } else {
                fixed.add(null);
            }
            if (percentAmount.containsKey(resourceRoleId)) {
                radios.add("percentage");
                percentage.add(percentAmount.get(resourceRoleId).toString());
                chooseDefault = false;
            } else {
                percentage.add("100");
            }
            if (chooseDefault) {
                radios.add("default");
            }
        }

        request.setAttribute("defaultPayments", defaultPayments);
        request.setAttribute("resourceRoleIds", resourceRoleIds);
        request.setAttribute("resourceRoleNames", resourceRoles);
        request.setAttribute("reviewPaymentsRadio", radios);
        request.setAttribute("reviewPaymentsFixed", fixed);
        request.setAttribute("reviewPaymentsPercentage", percentage);
    }

    /**
     * <p>
     * Saves (or updates) the distribution file to the component catalog.
     * </p>
     *
     * @param project the current project used to get component information.
     * @param descriptor an object defining the distribution file (may be the upload stream or a file stream).
     * @param request object used to add error information in case something goes wrong.
     * @param isDesign indicates if the distribution is for design or development.
     * @return <code>true</code> if the file was save properly, <code>false</code> otherwise
     * @throws Exception if any error occurs while uploading the file.
     */
    protected boolean saveDistributionFileToCatalog(Project project, DistributionFileDescriptor descriptor,
        HttpServletRequest request, boolean isDesign) throws Exception {

        long componentId = getProjectLongValue(project, "Component ID");
        long versionId = getProjectLongValue(project, "Version ID");

        if (componentId == 0 || versionId == 0) {
            if (componentId == 0) {
                ActionsHelper.addErrorToRequest(request,
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.Component.Invalid");
            }

            if (versionId == 0) {
                ActionsHelper.addErrorToRequest(request,
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.ComponentVersion.Invalid");
            }

            return false;
        }

        CatalogDataAccess catalogDataAccess = SpringContextProvider.getCatalogDataAccess();
        ComponentVersionInfo componentVersion = catalogDataAccess.getComponentVersionInfo(componentId, versionId);

        String rootDir = ConfigHelper.getCatalogOutputDir() + File.separator;
        String dir = "" + componentId + File.separator + componentVersion.getVersionId() + File.separator;

        File dirFile = new File(rootDir + dir);

        // Create the directories if they do not already exist.
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            ActionsHelper.addErrorToRequest(request,
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.Catalog.OutputDir");
            return false;
        }

        String url = dir + descriptor.getFileName();

        // Copy distribution file to catalog folder
        copyStream(descriptor.getInputStream(), new FileOutputStream(rootDir + url));

        long documentType;
        String documentName;

        // project is either Design or Development
        if (isDesign) {
            documentType = DESIGN_DISTRIBUTION_DOC_TYPE_ID;
            documentName = DESIGN_DISTRIBUTION_DOC_TYPE;

        } else {
            documentType = DEVELOPMENT_DISTRIBUTION_DOC_TYPE_ID;
            documentName = DEVELOPMENT_DISTRIBUTION_DOC_TYPE;
        }

        Document document
            = getDocumentOfType(catalogDataAccess.getDocuments(componentVersion.getVersionId()), documentType);

        if (document == null) {
            // Add document to component
            document = new Document(documentName, url, documentType);
            catalogDataAccess.addDocument(componentVersion.getVersionId(), document);
        } else {
            // Update document
            document.setURL(url);
            catalogDataAccess.updateDocument(componentVersion.getVersionId(), document);
        }

        return true;
    }

    /**
     * Return the document with the associated document type.
     *
     * @param documents the list of component documents.
     * @param documentType document type.
     * @return the document with the given document type, or null if not found.
     */
    private Document getDocumentOfType(Collection<?> documents, long documentType) {
        for (Object documentObject : documents) {
            Document document = (Document) documentObject;
            if (document.getType() == documentType) {
                // Assume a single document of this type will exist
                return document;
            }
        }
        // Not found
        return null;
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
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /**
     * <p>
     * Copies data from an InputStream to an OutputStream.
     * </p>
     *
     * @param in the input stream.
     * @param out the output stream.
     * @throws IOException if any exception occurs.
     */
    protected void copyStream(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] buffer = new byte[65536];

            for (;;) {
                int numOfBytesRead = in.read(buffer);
                if (numOfBytesRead == -1) {
                    break;
                }
                out.write(buffer, 0, numOfBytesRead);
            }
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                // ignore
            }

            try {
                out.close();
            } catch (IOException ex) {
                // ignore
            }
        }
    }

    /**
     * Validate the form for creating new distribution.
     * @param project the current project
     * @param model the model for this page
     * @param request the http servlet request.
     */
    protected void validateCreateDistributionForm(Project project, DynamicModel model, HttpServletRequest request) {

        // Validate project type - must be a Design or Development
        long projectCategoryId = project.getProjectCategory().getId();
        if ((projectCategoryId != DESIGN_PROJECT_ID) && (projectCategoryId != DEVELOPMENT_PROJECT_ID)) {
            ActionsHelper.addErrorToRequest(request, "error.com.cronos.onlinereview.actions.manageProject.Distributions.ProjectCategory");
        }

        String projectName = (String) project.getProperty("Project Name");

        // validate project name
        if (isEmpty(projectName)) {
            ActionsHelper.addErrorToRequest(request,
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.ProjectName.Invalid");
        }

        String version = (String) project.getProperty("Project Version");

        // validate project version
        if (isEmpty(version) || !VERSION_PATTERN.matcher(version).matches()) {
            ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.Version.Invalid", "version");
        }

        // Determines the script that will be used (if other is used, package is not mandatory)
        String rootCatalogID = (String) project.getProperty("Root Catalog ID");

        // validate root catalog id
        if (isEmpty(rootCatalogID)) {
            ActionsHelper.addErrorToRequest(request,
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.RootCatalog.Invalid");

        } else {
            String defaultScript = ConfigHelper.getDefaultDistributionScript();
            String distributionScript = ConfigHelper.getDistributionScript(rootCatalogID);

            // validate root catalog id is defined in configuration file
            if (distributionScript == null) {

                ActionsHelper.addErrorToRequest(request, ActionsHelper.GLOBAL_MESSAGE,
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.RootCatalog.NotDefined", "rootCatalogID");

            } else if (!defaultScript.equals(distributionScript)) {
                // Assume default script ('other') does not need package name
                String packageName = (String) getModel().get("distribution_package_name");

                // validate package is required
                if (isEmpty(packageName)) {
                    ActionsHelper.addErrorToRequest(request, "distribution_package_name",
                        "error.com.cronos.onlinereview.actions.manageProject.Distributions.PackageName.Empty");

                } else if (!PACKAGE_PATTERN.matcher(packageName).matches()) {
                    // Validate it is a valid package
                    ActionsHelper.addErrorToRequest(request, "distribution_package_name",
                        "error.com.cronos.onlinereview.actions.manageProject.Distributions.PackageName.Invalid");
                }
            }
        }

        Set<String> uploadedFiles = new HashSet<String>();
        FormFile distributionRSFile = (FormFile) getModel().get("distribution_rs");

        // Validate the RS form file
        if (distributionRSFile == null || isEmpty(distributionRSFile.getFileName())) {
            ActionsHelper.addErrorToRequest(request, "distribution_rs",
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.RS.Missing");

        } else if (distributionRSFile.getFileSize() == 0) {
            ActionsHelper.addErrorToRequest(request, "distribution_rs",
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.RS.Empty");

        } else {
            // Validate RS file name
            String lcFileName = distributionRSFile.getFileName().toLowerCase();
            if (!(lcFileName.endsWith("rtf") || lcFileName.endsWith("doc") || lcFileName.endsWith("pdf"))) {
                ActionsHelper.addErrorToRequest(request, "distribution_rs",
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.RS.Invalid");
            }

            uploadedFiles.add(lcFileName);
        }

        // Validate additional files - check if more than one file has the same name
        for (int i = 1; i <= 3; ++i) {
            FormFile additionalFormFile = (FormFile) getModel().get("distribution_additional" + i);

            // Only use additional file if it is uploaded and well set
            if ((additionalFormFile != null) && (additionalFormFile.getFileSize() > 0)
                && !isEmpty(additionalFormFile.getFileName())) {

                String lcFileName = additionalFormFile.getFileName().toLowerCase();
                if (uploadedFiles.contains(lcFileName)) {
                    ActionsHelper.addErrorToRequest(request, "distribution_additional" + i,
                        "error.com.cronos.onlinereview.actions.manageProject.Distributions.Files.SameName");
                }

                uploadedFiles.add(lcFileName);
            }
        }


        boolean uploadToServer = getBooleanFromForm(getModel(), "upload_to_server");
        boolean returnDistribution = getBooleanFromForm(getModel(), "return_distribution");

        // Must select at least one of these options
        if (!uploadToServer && !returnDistribution) {
            // Add the error message to both checkboxes if design distribution

            if (projectCategoryId == DESIGN_PROJECT_ID) {
                ActionsHelper.addErrorToRequest(request, "upload_to_server",
                    "error.com.cronos.onlinereview.actions.manageProject.Distributions.Upload.Unchecked");
            }

            ActionsHelper.addErrorToRequest(request, "return_distribution",
                "error.com.cronos.onlinereview.actions.manageProject.Distributions.Upload.Unchecked");
        }
    }

    /**
     * <p>
     * Checks if a string value is null/empty.
     * </p>
     *
     * @param value the string being validate.
     * @return <code>true</code> if a string value is null/empty.
     */
    protected static boolean isEmpty(String value) {
        return (value == null) || (value.trim().length() == 0);
    }


    /**
     * <p>
     * Read a boolean value from the form model.
     * </p>
     *
     * @param model the model to read the value from.
     * @param attribute the name of the attribute of the form (which must be of type Boolean).
     * @return <code>true</code> if the form value is Boolean.TRUE, <code>false</code> if it is Boolean.FALSE or null.
     */
    protected static boolean getBooleanFromForm(DynamicModel model, String attribute) {
        Boolean value = (Boolean) model.get(attribute);
        return (value != null) && value;
    }

    /**
     * <p>Initializes the <code>Project Management Console</code> view. Binds necessary objects to current request. Such
     * objects include: list of available roles for resources which can be added by current user to specified project;
     * registration/submission phase durations and object presentations; flags indicating whether
     * registration/submission phase can be extended or not.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @param project a <code>Project</code> providing the details for current project being managed by current user.
     * @throws BaseException if an unexpected error occurs.
     */
    protected void initProjectManagementConsole(HttpServletRequest request, Project project) throws BaseException {
        Phase[] phases = getProjectPhases(project);
        setAvailableResourceRoles(request);
        setRegistrationPhaseExtensionParameters(request, phases);
        setSubmissionPhaseExtensionParameters(request, phases);

        // Identifies if package name is needed
        String rootCatalogID = (String) project.getProperty("Root Catalog ID");
        if (!isEmpty(rootCatalogID)) {
            String distributionScript = ConfigHelper.getDistributionScript(rootCatalogID);
            if (!isEmpty(distributionScript)) {
                request.setAttribute("needsPackageName", !ConfigHelper.getDefaultDistributionScript().equals(
                    distributionScript));
            }
        }

        // Initialize the Review Feedback area
        initReviewFeedbackIntegration(request, project);

        setReviewPaymentsRequestAttribute(request, project);
    }

    /**
     * <p>Gets the phases for the specified project.</p>
     *
     * @param project a <code>Project</code> providing the details for current project being managed by current user.
     * @return a <code>Phase</code> array listing the phases for specified project.
     * @throws BaseException if an unexpected error occurs.
     */
    protected Phase[] getProjectPhases(Project project) throws BaseException {
        // Get details for requested project
        PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);
        com.topcoder.project.phases.Project phasesProject = phaseManager.getPhases(project.getId());
        return phasesProject.getAllPhases();
    }

    /**
     * <p>Binds to specified request the list of roles available for resources which current user can add to project.
     * Analyzes the assigned roles for current user and binds appropriate list of available resource roles to specified
     * request.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client to bind the list
     *        of available roles to.
     * @throws ResourcePersistenceException if an unexpected error occurs while instantiating resource manager.
     */
    private void setAvailableResourceRoles(HttpServletRequest request) throws ResourcePersistenceException {
        ResourceManager resourceManager = ActionsHelper.createResourceManager();
        ResourceRole[] existingResourceRoles = resourceManager.getAllResourceRoles();
        List<ResourceRole> availableRoles = new ArrayList<ResourceRole>();
        for (ResourceRole role : existingResourceRoles) {
            final String permission = "Can Add Resource Role " + role.getName();
            if (AuthorizationHelper.hasUserPermission(request, permission)) {
                availableRoles.add(role);
            }
        }
        request.setAttribute("availableRoles", availableRoles);
    }

    /**
     * <p>Checks if specified phase is closed by analyzing current phase status.</p>
     *
     * @param phase a <code>Phase</code> representing the phase to be verified.
     * @return <code>true</code> if specified phase is closed; <code>false</code> otherwise.
     */
    protected boolean isClosed(Phase phase) {
        long phaseStatusId = phase.getPhaseStatus().getId();
        return phaseStatusId == PhaseStatusEnum.CLOSED.getId();
    }

    /**
     * <p>Gets the number of hours left before the specified time is reached.</p>
     *
     * @param time a <code>Date</code> providing the date and time to calculate time left to.
     * @return a <code>long</code> providing the number of hours left from current time before the specified date and
     *         time is reached.
     */
    private long getHoursLeft(Date time) {
        Date now = new Date();
        long diff = time.getTime() - now.getTime();
        return diff / HOUR_DURATION_IN_MILLIS;
    }

    /**
     * <p>Gets the <code>Registration</code> phase for specified project if such a phase exists and if it can be
     * extended based on current project's state.</p>
     *
     * @param phases a <code>Phase</code> array listing the current project phases.
     * @return a <code>Phase</code> providing the <code>Registration</code> phase for current project which can be
     *         extended or <code>null</code> if there is no <code>Registration</code> phase for project or such a phase
     *         can not be extended since there is less than 48 hours left before <code>Submission</code> phase deadline
     *         or <code>Submission</code> phase is already closed.
     */
    protected Phase getRegistrationPhaseForExtension(Phase[] phases) {
        Phase registrationPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.REGISTRATION_PHASE_NAME);
        if ((registrationPhase != null)) {
            Phase submissionPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.SUBMISSION_PHASE_NAME);
            if (submissionPhaseAllowsExtension(submissionPhase)) {
                return registrationPhase;
            }
        }
        return null;
    }

    /**
     * <p>Binds the attributes to specified request which affect the <code>Extend Registration Phase</code>
     * functionality like indicating whether the <code>Registration</code> phase extension is allowed or not.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client to bind the flags
     *        related to phase extension to.
     * @param phases a <code>Phase</code> array listing the current project phases.
     */
    private void setRegistrationPhaseExtensionParameters(HttpServletRequest request, Phase[] phases) {
        Phase registrationPhase = getRegistrationPhaseForExtension(phases);
        if ((registrationPhase != null)) {
            // Registration phase can be extended
            request.setAttribute("allowRegistrationPhaseExtension", Boolean.TRUE);
        } else {
            // Registration phase can NOT be extended
            registrationPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.REGISTRATION_PHASE_NAME);
            request.setAttribute("allowRegistrationPhaseExtension", Boolean.FALSE);
        }
        if (registrationPhase != null) {
            request.setAttribute("registrationPhaseClosed", isClosed(registrationPhase));
            request.setAttribute("registrationPhase", registrationPhase);
            request.setAttribute("registrationPhaseDuration", registrationPhase.getLength() / HOUR_DURATION_IN_MILLIS);
        }
    }

    /**
     * <p>Binds the attributes to specified request which affect the <code>Extend Submission Phase</code> functionality
     * like indicating whether the <code>Submission</code> phase extension is allowed or not.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client to bind the flags
     *        related to phase extension to.
     * @param phases a <code>Phase</code> array listing the current project phases.
     */
    private void setSubmissionPhaseExtensionParameters(HttpServletRequest request, Phase[] phases) {
        Phase submissionPhase = ActionsHelper.findPhaseByTypeName(phases, Constants.SUBMISSION_PHASE_NAME);
        if (submissionPhase != null) {
            if (submissionPhaseAllowsExtension(submissionPhase)) {
                request.setAttribute("allowSubmissionPhaseExtension", Boolean.TRUE);
            }
            request.setAttribute("submissionPhase", submissionPhase);
            request.setAttribute("submissionPhaseDuration", submissionPhase.getLength() / HOUR_DURATION_IN_MILLIS);
            return;
        }
        request.setAttribute("allowSubmissionPhaseExtension", Boolean.FALSE);
    }

    /**
     * <p>Checks if specified <code>Submission</code> phase allows phase extension or not. The method returns
     * <code>true</code> if specified phase is not <code>null</code> and is not closed and there are at least 48 hours
     * left before submission phase deadline.</p>
     *
     * @param submissionPhase a <code>Phase</code> providing details for <code>Submission</code> phase.
     * @return <code>true</code> if specified phase allows phase extension; <code>false</code> otherwise.
     * @throws IllegalArgumentException if specified <code>submissionPhase</code> is not of <code>Submission</code>
     *         phase type.
     */
    protected boolean submissionPhaseAllowsExtension(Phase submissionPhase) {
        if (submissionPhase != null) {
            String phaseTypeName = submissionPhase.getPhaseType().getName();
            if (!Constants.SUBMISSION_PHASE_NAME.equals(phaseTypeName)) {
                throw new IllegalArgumentException("Wrong phase. Expected Submission phase but provide phase is: "
                                                   + phaseTypeName);
            }
            Date submissionScheduledEndDate = submissionPhase.getScheduledEndDate();
            Integer minimumHoursBeforeSubmissionDeadlineForExtension
                = ConfigHelper.getMinimumHoursBeforeSubmissionDeadlineForExtension();
            if ((!isClosed(submissionPhase))
                && (getHoursLeft(submissionScheduledEndDate) >= minimumHoursBeforeSubmissionDeadlineForExtension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Retrieves data for displaying by Review Performance tab when Project Management Console page is displayed to
     * user.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request from client.
     * @param project a <code>Project</code> providing the data for the project being managed.
     * @throws BaseException if an unexpected error occurs.
     */
    private void initReviewFeedbackIntegration(HttpServletRequest request, Project project) throws BaseException {

        // Retrieve existing review feedbacks for project
        ReviewFeedbackManager reviewFeedbackManager = ActionsHelper.createReviewFeedbackManager();
        List<ReviewFeedback> reviewFeedbacks = reviewFeedbackManager.getForProject(project.getId());

        if (reviewFeedbacks.size() > 1) {
            throw new BaseException("These should be at most 1 review feedback");
        }

        final boolean reviewFeedbackAllowed = isReviewFeedbackAllowed(project);

        // Get the resources for the reviewers (if necessary)
        Boolean toEdit = (Boolean) request.getAttribute("toEdit");
        toEdit = toEdit != null && toEdit;
        if (reviewFeedbackAllowed && (reviewFeedbacks.size() == 0 || toEdit)) {
            List<Resource> reviewerResources = getFeedbackEligibleReviewers(project.getId(), request);
            Map<Long, Resource> reviewerResourcesMap = new TreeMap<Long, Resource>();
            for (Resource reviewer : reviewerResources) {
                reviewerResourcesMap.put(reviewer.getUserId(), reviewer);
            }

            request.setAttribute("reviewerResourcesMap", reviewerResourcesMap);
        }

        // Bind data to request
        request.setAttribute("feedback", reviewFeedbacks.size() == 0 ? null : reviewFeedbacks.get(0));
        request.setAttribute("reviewFeedbackAllowed", reviewFeedbackAllowed);
    }

    /**
     * <p>Gets the list of reviewers eligible for review feedback for specified project. Such reviewer resources will
     * have their reviews committed and resource matching the current user will be excluded from the resulting list.</p>
     *
     * @param projectId a <code>long</code> providing the ID of a project.
     * @param request an <code>HttpServletRequest</code> representing incoming request from the client.
     * @return a <code>List</code> of reviewers for specified project which the feedback can be provided for.
     * @throws LookupException if an unexpected error occurs.
     * @throws ResourcePersistenceException if an unexpected error occurs.
     * @throws SearchBuilderException if an unexpected error occurs.
     * @throws ReviewManagementException if there is review management error
     */
    protected List<Resource> getFeedbackEligibleReviewers(long projectId, HttpServletRequest request)
        throws LookupException, ResourcePersistenceException, SearchBuilderException, ReviewManagementException {
        // Build filter for reviewer role names
        List<Filter> reviewerRoleFilters = new ArrayList<Filter>();
        for (String reviewerRoleName : REVIEW_FEEDBACK_ELIGIBLE_ROLE_NAMES) {
            Filter resourceRoleIdFilter = ResourceFilterBuilder
                .createResourceRoleIdFilter(LookupHelper.getResourceRole(reviewerRoleName).getId());
            reviewerRoleFilters.add(resourceRoleIdFilter);
        }
        OrFilter reviewerRolesFilter = new OrFilter(reviewerRoleFilters);

        // Build filter for searching reviewers for project
        AndFilter filter = new AndFilter(reviewerRolesFilter, ResourceFilterBuilder.createProjectIdFilter(projectId));

        // Search reviewers for project
        ResourceManager resourceManager = ActionsHelper.createResourceManager();
        List<Resource> reviewerResources
            = new ArrayList<Resource>(Arrays.asList(resourceManager.searchResources(filter)));

        // Build list of reviewer resource IDs
        List<Long> reviewerResourceIds = new ArrayList<Long>();
        for (Resource reviewerResource : reviewerResources) {
            reviewerResourceIds.add(reviewerResource.getId());
        }

        // Get reviews for project
        ReviewManager reviewManager = ActionsHelper.createReviewManager();
        Filter filterProject = new EqualToFilter("project", projectId);
        InFilter reviewerResourcesFilter = new InFilter("reviewer", reviewerResourceIds);
        Filter committedReviewFilter = new EqualToFilter("committed", 1);
        AndFilter reviewFilter = new AndFilter(Arrays.asList(filterProject, reviewerResourcesFilter,
                                                             committedReviewFilter));
        Review[] reviews = reviewManager.searchReviews(reviewFilter, false);

        // Collect the IDs of committed review author resources
        Set<Long> committedReviewAuthors = new HashSet<Long>();
        for (Review review : reviews) {
            if (review.isCommitted()) {
                committedReviewAuthors.add(review.getAuthor());
            }
        }

        // Filter out those reviewer resources who either do not have committed reviews or correspond to current user
        Long currentUserId = AuthorizationHelper.getLoggedInUserId(request);
        Iterator<Resource> reviewersIterator = reviewerResources.iterator();
        while (reviewersIterator.hasNext()) {
            Resource reviewer = reviewersIterator.next();
            if (committedReviewAuthors.contains(reviewer.getId())) {
                if (currentUserId != null && currentUserId.equals(reviewer.getUserId())) {
                    reviewersIterator.remove();
                }
            } else {
                reviewersIterator.remove();
            }
        }

        return reviewerResources;
    }

    /**
     * Checks whether the current user can manage the review feedback for the specific project.
     *
     * @param project the specific project.
     * @return true if the current user can manage the review feedback for the specific project, false otherwise.
     * @throws BaseException if any error occurs.
     */
    protected boolean isReviewFeedbackAllowed(Project project) throws BaseException {
        final Phase[] phases = getProjectPhases(project);

        // Check if project allows review feedback creation
        String reviewFeedbackFlag = (String) project.getProperty("Review Feedback Flag");
        boolean reviewFeedbackFlagSet = "true".equalsIgnoreCase(reviewFeedbackFlag);

        // Check if Appeals Response phase or Review phase (if Appeals Response phase is missing) is closed
        Phase phase = ActionsHelper.findPhaseByTypeName(phases, Constants.APPEALS_RESPONSE_PHASE_NAME);
        if (phase == null) {
            phase = ActionsHelper.findPhaseByTypeName(phases, Constants.REVIEW_PHASE_NAME);
        }
        final boolean feedbackStopperPhaseIsClosed
                = phase != null && ActionsHelper.isPhaseClosed(phase.getPhaseStatus());
        return reviewFeedbackFlagSet && feedbackStopperPhaseIsClosed;
    }

    /**
     * Getter of pid.
     * @return the pid
     */
    public long getPid() {
        return pid;
    }

    /**
      * Setter of pid.
      * @param pid the pid to set
      */
    public void setPid(long pid) {
        this.pid = pid;
    }



    /**
     * This static method helps to create the default project payment calculator.
     *
     * @return the instance of default project payment calculator.
     * @throws com.topcoder.util.errorhandling.BaseRuntimeException if any error occurs
     */
    private static ProjectPaymentCalculator createDefaultProjectPaymentCalculator() throws BaseRuntimeException {
        String className = null;
        try {
            ConfigManager cfgMgr = ConfigManager.getInstance();
            Property config = cfgMgr.getPropertyObject("com.cronos.OnlineReview", "DefaultProjectPaymentConfig");
            className = config.getValue("CalculatorClass");
            Class clazz = Class.forName(className);
            return (ProjectPaymentCalculator) clazz.newInstance();
        } catch (Exception e) {
            throw new BaseRuntimeException("Failed to instantiate the project payment calculator of type: "
                    + className, e);
        }
    }

    /**
     * This interface provides an abstraction to the origin of the distribution file. It may be
     * read directly from the file system, from memory or from an uploaded file.
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    protected static interface DistributionFileDescriptor {
        /**
         * <p>
         * Returns the file name.
         * </p>
         *
         * @return the file name.
         */
        public abstract String getFileName();

        /**
         * <p>
         * Returns an input stream that will be used read the file contents.
         * </p>
         *
         * @return the input stream that will be used read the file contents.
         * @throws IOException if any error occurs while creating the input stream.
         */
        public abstract InputStream getInputStream() throws IOException;
    }
}