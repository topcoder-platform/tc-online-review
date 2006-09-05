/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;

import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.external.impl.DBUserRetrieval;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.PersistenceUploadManager;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.sql.SqlUploadPersistence;
import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
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
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseDateComparator;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.datavalidator.LongValidator;
import com.topcoder.util.datavalidator.StringValidator;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * This class contains handy helper-methods that perform frequently needed operations.
 *
 * @author TCSAssemblyTeam
 * @version 1.0
 */
class ActionsHelper {

    /**
     * This constructor is declared private to prohibit instantiation of the
     * <code>ActionsHelper</code> class.
     */
    private ActionsHelper() {
    }

    /**
     * This static method validates that parameter specified by <code>param</code> parameter is
     * not <code>null</code>, and throws an exception if validation fails.
     *
     * @param param
     *            a parameter to validate for non-null value.
     * @param paramName
     *            a name of the parameter that is being validated.
     * @throws IllegalArgumentException
     *             if parameter <code>param</code> if <code>null</code>.
     */
    public static void validateParameterNotNull(Object param, String paramName) throws IllegalArgumentException {
        if (param == null) {
            throw new IllegalArgumentException("Paramter '" + paramName + "' must not be null.");
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
     *             if parameter <code>value</code> is zero or neative.
     */
    public static void validateParameterPositive(long value, String paramName) throws IllegalArgumentException {
        if (value <= 0) {
            throw new IllegalArgumentException("Parameter '" + paramName + "' must not be negative or zero." +
                    " Current value of the parameters is " + value + ".");
        }
    }

    /**
     * This static method converts all line terminators found in the provided text into
     * <code>&lt;br /&gt;</code> tag, so the resulting converted text can be displayed on a JSP
     * page.  The line terminators are the ones specified in the description of the class
     * <code>java.util.regex.Pattern</code>.
     * <p>
     * This class is thread safe as it contains only static methods and no inner state.
     * </p>
     *
     * @return converted text.
     * @param text
     *            text that needs conversion of line-breaks.
     */
    public static String addLineBreaks(String text) {
        return text.replaceAll("(\r\n)|[\n\r\u0085\u2029]", "<br />");
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
        Phase[] phases = phProj.getAllPhases(new PhaseDateComparator());
        return phases;
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

        for (int i = 0; i < phases.length; ++i) {
            // Get a phase for the current iteration
            Phase phase = phases[i];
            // Get a name of status of this phase
            String phaseStatus = phase.getPhaseStatus().getName();
            // If the search is being performed for active phase only, skip already closed phase
            if (activeOnly == true && phaseStatus.equalsIgnoreCase("Closed")) {
                continue;
            }
/* TODO: Uncomment this when phases have correct status
            // There is no active phase with specified name, or there is an error in database
            if (activeOnly == true && strPhaseStatus.equalsIgnoreCase("Sheduled")) {
                return null;
            }
*/
            // If the name of the phase was not specified,
            // or the name of the current phase equals desired name, return this phase
            if (phaseName == null || phaseName.equalsIgnoreCase(phase.getPhaseType().getName())) {
                // Return it
                return phase;
            }
        }
        // No phase has been found
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
     * This static method retrieves the resources for a phase using provided Resource Manager.
     *
     * @return an array of the resources for the specified phase.
     * @param manager
     *            an instance of <code>ResourceManager</code> object used to retrieve resources.
     * @param phase
     *            a phase to retrieve the resources for.
     * @throws IllegalArgumentException
     *             if any of the arguments are <code>null</code>.
     * @throws BaseException
     *             if any error occurs.
     */
    public static Resource[] getAllResourcesForPhase(ResourceManager manager, Phase phase)
        throws IllegalArgumentException, BaseException {
        // Validate parameters
        validateParameterNotNull(manager, "manager");
        validateParameterNotNull(phase, "phase");

        // Prepare filter to select resource by project ID
        Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(phase.getProject().getId());
        // Prepare filterr to select resource by phase ID
        Filter filterPhase = ResourceFilterBuilder.createPhaseIdFilter(phase.getId());
        // Combine the above filters into one
        Filter filter = new AndFilter(filterProject, filterPhase);

        // Perform a search for the resources and return them
        return manager.searchResources(filter);
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
     * This static method retrieves the resource for the currently logged in user associated with
     * the specified phase. The list of all resources for the currently logged in user is retrieved
     * from the <code>HttpServletRequest</code> object specified by <code>request</code>
     * parameter. Method <code>gatherUserRoles(HttpServletRequest, long)</code> should be called
     * prior making a call to this method.
     * 
     * @return found resource, or <code>null</code> if no resource for currently logged in user
     *         found such that that resource would be associated with the specified phase.
     * @param request
     *            an <code>HtppServletRequest</code> object containing additional information.
     * @param phase
     *            a phase to search the resouce for.
     * @throws IllegalArgumentException
     *             if any of the parameters are <code>null</code>.
     */
    public static Resource getMyResourceForPhase(HttpServletRequest request, Phase phase) {
        // Validate parameters
        validateParameterNotNull(request, "request");
        validateParameterNotNull(phase, "phase");
        
        // Retrieve the list of "My" resources from the request's attribute
        Resource[] resources = (Resource[]) request.getAttribute("myResources");
        if (resources == null) {
            // Incorrect usage of method detected.
            // Method gatherUserRoles(HttpServletRequest, long) should have been called first
            return null;
        }
        
        for (int i = 0; i < resources.length; ++i) {
            // Get a resource for current iteration
            Resource resource = resources[i];
            // Find the resource for phase in question
            if (resource.getPhase() != null && resource.getPhase().longValue() == phase.getId()) {
                // Return it
                return resource;
            }
        }
        
        // No "My" resource has been found for the specified phase
        return null;
    }

    /**
     * This static method helps to create an object of the <code>PhaseManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param request
     *            an <code>HttpServletRequest</code> obejct, where created
     *            <code>PhaseManager</code> object can be stored to let reusing it later for the
     *            same request.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws com.topcoder.management.phase.ConfigurationException
     *             if any required configuration parameter is missing, or if any of the supplied
     *             parameters in cofiguration are invalid.
     */
    public static PhaseManager createPhaseManager(HttpServletRequest request)
        throws com.topcoder.management.phase.ConfigurationException {
        // Validate parameter
        validateParameterNotNull(request, "request");

        // Try retrieving Phase Manager from the request's attribute first
        PhaseManager manager = (PhaseManager) request.getAttribute("phaseManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            manager = new DefaultPhaseManager("com.topcoder.management.phase");
            // Place newly-created object into the request as attribute
            request.setAttribute("phaseManager", manager);
        }

        // Return the Phase Manager object
        return manager;
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
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
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
                    new SearchBundleManager("com.topcoder.searchbuilder.ResourceManagement");

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

        // Try retrieving Scorecard Manager from the request's attribute first
        UploadManager manager = (UploadManager) request.getAttribute("uploadManager");
        // If this is the first time this method is called for the request,
        // create a new instance of the object
        if (manager == null) {
            // get connection factory
            DBConnectionFactory dbconn = new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
            // get the persistence
            SqlUploadPersistence persistence = new SqlUploadPersistence(dbconn);

            // get the id generators
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

            // get the search bundles
            SearchBundleManager searchBundleManager =
                    new SearchBundleManager("com.topcoder.searchbuilder.ResourceManagement");

            SearchBundle uploadSearchBundle = searchBundleManager.getSearchBundle(
                    PersistenceUploadManager.UPLOAD_SEARCH_BUNDLE_NAME);
            SearchBundle submissionSearchBundle = searchBundleManager.getSearchBundle(
                    PersistenceUploadManager.SUBMISSION_SEARCH_BUNDLE_NAME);

            // initialize the PersistenceUploadManager
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
            manager = new DBUserRetrieval("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
            // Place newly-created object into the request as attribute
            request.setAttribute("userRetrieval", manager);
        }

        // Return the Upload Retrieval object
        return manager;
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
