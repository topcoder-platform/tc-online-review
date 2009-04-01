package com.cronos.onlinereview.commons;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cronos.onlinereview.actions.ActionsHelper;
import com.cronos.onlinereview.actions.Constants;
import com.cronos.onlinereview.autoscreening.management.ScreeningManager;
import com.cronos.onlinereview.autoscreening.management.ScreeningManagerFactory;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.external.impl.DBUserRetrieval;
import com.cronos.onlinereview.phases.AppealsPhaseHandler;
import com.cronos.onlinereview.phases.ApprovalPhaseHandler;
import com.cronos.onlinereview.phases.PRAggregationPhaseHandler;
import com.cronos.onlinereview.phases.PRAggregationReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRAppealResponsePhaseHandler;
import com.cronos.onlinereview.phases.PRFinalFixPhaseHandler;
import com.cronos.onlinereview.phases.PRFinalReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRRegistrationPhaseHandler;
import com.cronos.onlinereview.phases.PRReviewPhaseHandler;
import com.cronos.onlinereview.phases.PRScreeningPhaseHandler;
import com.cronos.onlinereview.phases.PRSubmissionPhaseHandler;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.PersistenceUploadManager;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistence;
import com.topcoder.management.deliverable.persistence.sql.SqlUploadPersistence;
import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.PhaseHandler;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseOperationEnum;
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
import com.topcoder.project.phases.PhaseType;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
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
 * @author Bauna
 * @version 1.0
 */
public class OnlineReviewHelper {
	
	/**
     * This member variable is a string constant that defines the name of the configurtaion
     * namespace which the parameters for database connection factory are stored under.
     */
	public static final String DB_CONNECTION_NAMESPACE = "com.topcoder.db.connectionfactory.OR";
	
	/**
     * This static method helps to create an object of the <code>ProjectManager</code> class.
     *
     * @return a newly created instance of the class.
     * @throws com.topcoder.management.project.ConfigurationException
     *             if error occurs while loading configuration settings, or any of the required
     *             configuration parameters are missing.
     *             
     * @see ProjectManager
     */
	public static ProjectManager createProjectManager() 
			throws com.topcoder.management.project.ConfigurationException {
		return new ProjectManagerImpl();
	}
	
	/**
     * This static method helps to create an object of the <code>UploadManager</code> class.
     *
     * @return a newly created instance of the class.
     * @throws BaseException
     *             if any error occurs.
     */
	public static FileUpload createFileUploadManager()
			throws DisallowedDirectoryException, com.topcoder.servlet.request.ConfigurationException {
		return new LocalFileUpload("com.topcoder.servlet.request.LocalFileUpload");
	}

    /**
     * This static method helps to create an object of the <code>PhaseManager</code> class.
     *
     * @return a newly created instance of the class.
     * @param registerPhaseHandlers
     *            a boolean parameter that determines whether phase handlers need to be registered
     *            with the newly-created (or already existing) Phase Manager.
     * @throws BaseException
     *             if any error happens during object creation.
     */	
	public static PhaseManager createPhaseManager(boolean registerPhaseHandlers) throws BaseException {
        
            PhaseManager  manager = new DefaultPhaseManager("com.topcoder.management.phase");

            // Register phase handlers if this was requested
            if (registerPhaseHandlers) {
                PhaseType[] phaseTypes = manager.getAllPhaseTypes();

                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new PRRegistrationPhaseHandler(), Constants.REGISTRATION_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new PRSubmissionPhaseHandler(), Constants.SUBMISSION_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new PRScreeningPhaseHandler(), Constants.SCREENING_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new PRReviewPhaseHandler(), Constants.REVIEW_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new AppealsPhaseHandler(), Constants.APPEALS_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new PRAppealResponsePhaseHandler(), Constants.APPEALS_RESPONSE_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new PRAggregationPhaseHandler(), Constants.AGGREGATION_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new PRAggregationReviewPhaseHandler(), Constants.AGGREGATION_REVIEW_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new PRFinalFixPhaseHandler(), Constants.FINAL_FIX_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new PRFinalReviewPhaseHandler(), Constants.FINAL_REVIEW_PHASE_NAME);
                registerPhaseHandlerForOperation(manager, phaseTypes,
                        new ApprovalPhaseHandler(), Constants.APPROVAL_PHASE_NAME);
            }
        return manager;
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
        ActionsHelper.validateParameterNotNull(phaseTypes, "phaseTypes");
        ActionsHelper.validateParameterStringNotEmpty(phaseTypeName, "phaseTypeName");

        for (int i = 0; i < phaseTypes.length; ++i) {
            if (phaseTypes[i].getName().equalsIgnoreCase(phaseTypeName)) {
                return phaseTypes[i];
            }
        }
        return null;
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
     * This static method helps to create an object of the <code>ResourceManager</code> class.
     *
     * @return a newly created instance of the class.
     * @throws IllegalArgumentException if <code>request</code> parameter is <code>null</code>.
     * @throws BaseException if any error occurs.
     */
    public static ResourceManager createResourceManager() throws BaseException {

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
            return new PersistenceResourceManager(persistence, resourceSearchBundle,
                    resourceRoleSearchBundle, notificationSearchBundle,
                    notificationTypeSearchBundle, resourceIdGenerator,
                    resourceRoleIdGenerator, notificationTypeIdGenerator);
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
    
    /**
     * This static method helps to create an object of the <code>UploadManager</code> class.
     *
     * @return a newly created instance of the class.
     * @throws BaseException
     *             if any error occurs.
     */
    public static UploadManager createUploadManager() throws BaseException {
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

		// Get the search bundles
		SearchBundleManager searchBundleManager = new SearchBundleManager("com.topcoder.searchbuilder.common");

		SearchBundle uploadSearchBundle = searchBundleManager
				.getSearchBundle(PersistenceUploadManager.UPLOAD_SEARCH_BUNDLE_NAME);
		SearchBundle submissionSearchBundle = searchBundleManager
				.getSearchBundle(PersistenceUploadManager.SUBMISSION_SEARCH_BUNDLE_NAME);

		// Initialize the PersistenceUploadManager
		return new PersistenceUploadManager(persistence, uploadSearchBundle, submissionSearchBundle, uploadIdGenerator,
				uploadTypeIdGenerator, uploadStatusIdGenerator, submissionIdGenerator, submissionStatusIdGenerator);
	}
    
    /**
     * This static method helps to create an object of the <code>ScreeningManager</code> class.
     *
     * @return a newly created instance of the class.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws BaseException
     *             if any error occurs.
     */
    public static ScreeningManager createScreeningManager() throws BaseException {
       return ScreeningManagerFactory.createScreeningManager();
    }
    
    
    /**
     * This static method retrieves all the <code>Resource</code> of an user on an project 
     * 
     * @param projectId the project's id 
     * @param userId the user's id
     * @return an array containing all the resource for user in the project 
     * @throws BaseException if any error occurs.
     * @see Resource
     */
    public static Resource[] findResourcesByProjectAndUser(long projectId, long userId) throws BaseException {
    	 // Prepare filter to select resources by the External ID of currently logged in user
        Filter filterExtIDname = ResourceFilterBuilder.createExtensionPropertyNameFilter(Constants.EXTERNAL_REFERENCE_ID);
        Filter filterExtIDvalue = ResourceFilterBuilder.createExtensionPropertyValueFilter(String.valueOf(userId));
        Filter filterExtID = new AndFilter(filterExtIDname, filterExtIDvalue);

        // Create filter to filter only the resources for the project in question
        Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(projectId);
        // Create combined final filter
        Filter filter = new AndFilter(filterExtID, filterProject);

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = OnlineReviewHelper.createResourceManager();
        // Perform search for resources
        return resMgr.searchResources(filter);
    }
    
    /**
     * This static method helps to create an object of the <code>UserRetrieval</code> class.
     *
     * @return a newly created instance of the class.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws com.cronos.onlinereview.external.ConfigException
     *             if error occurs while loading configuration settings, or any of the required
     *             configuration parameters are missing.
     */
    public static UserRetrieval createUserRetrieval()
        	throws com.cronos.onlinereview.external.ConfigException {
    	return new DBUserRetrieval(DB_CONNECTION_NAMESPACE);
    }
    
    /**
     * This static method retrieves the first <code>Resource</code> of an user on an project 
     * that the <code>Resource</code> contains the "External Reference ID" resource property
     * equals to the user's id
     * 
     * @param projectId the project's id 
     * @param userId the user's id
     * @return the user's <code>Resource</code> for project or null if it doesn't exists.  
     * @throws BaseException if any error occurs.
     */
    public static Resource findExternalUserResourceForProject(long projectId, long userId) throws BaseException {
		Resource[] resources = findResourcesByProjectAndUser(projectId, userId);
		if ((resources == null) || (resources.length == 0)) {
			return null;
		}
		// Get the name (id) of the user performing the operations
		String operator = Long.toString(userId);
		for (int i = 0; i < resources.length; i++) {
			for (Iterator j = resources[i].getAllProperties().entrySet().iterator(); j.hasNext();) {
				Map.Entry entry = (Map.Entry) j.next();
				if (Constants.EXTERNAL_REFERENCE_ID.equals(entry.getKey()) && operator.equals(entry.getValue())) {
					return resources[i];
				}
			}
		}
		return null;
	}
}
