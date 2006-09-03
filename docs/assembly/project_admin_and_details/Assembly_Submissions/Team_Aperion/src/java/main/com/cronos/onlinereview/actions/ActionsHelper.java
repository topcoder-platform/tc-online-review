/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.PersistenceUploadManager;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.sql.SqlUploadPersistence;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.PersistenceResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistence;
import com.topcoder.management.resource.persistence.sql.SqlResourcePersistence;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.resource.search.NotificationTypeFilterBuilder;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.resource.search.ResourceRoleFilterBuilder;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
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
     */
    public static int getScorecardQuestionsCount(Scorecard scorecardTemplate) {
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
     *            a string that should be appended to the path of the newly-cloned forward
     */
    public static ActionForward cloneForwardAndAppendToPath(ActionForward forward, String arg0) {
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
     */
    public static void retrieveAndStoreBasicProjectInfo(
            HttpServletRequest request, Project project, MessageResources messages) {
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
     * This static method helps to create object of the <code>ResourceManager</code> class.
     *
     * @return a newly created instance of the class.
     * @throws BaseException
     *             if any error occurs.
     */
    public static ResourceManager createResourceManager() throws BaseException {
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
        ResourceManager manager = new PersistenceResourceManager(persistence, resourceSearchBundle,
                resourceRoleSearchBundle, notificationSearchBundle,
                notificationTypeSearchBundle, resourceIdGenerator,
                resourceRoleIdGenerator, notificationTypeIdGenerator);

        // Return newly created Resource Manager
        return manager;
    }

    /**
     * This static method helps to create object of the <code>UploadManager</code> class.
     *
     * @return a newly created instance of the class.
     * @throws BaseException
     *             if any error occurs.
     */
    public static UploadManager createUploadManager() throws BaseException {
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
        // set it searchable
//        setAllFieldsSearchable(uploadSearchBundle);

        SearchBundle submissionSearchBundle = searchBundleManager.getSearchBundle(
                PersistenceUploadManager.SUBMISSION_SEARCH_BUNDLE_NAME);
        // set it searchable
//        setAllFieldsSearchable(submissionSearchBundle);

        // initialize the PersistenceUploadManager
        UploadManager manager = new PersistenceUploadManager(persistence,
                uploadSearchBundle, submissionSearchBundle,
                uploadIdGenerator, uploadTypeIdGenerator, uploadStatusIdGenerator,
                submissionIdGenerator, submissionStatusIdGenerator);

        // Return newly created Upload Manager
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
