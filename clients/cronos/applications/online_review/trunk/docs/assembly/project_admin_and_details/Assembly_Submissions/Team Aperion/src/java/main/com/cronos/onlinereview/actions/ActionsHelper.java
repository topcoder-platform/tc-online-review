/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.PersistenceResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistence;
import com.topcoder.management.resource.persistence.sql.SqlResourcePersistence;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.resource.search.NotificationTypeFilterBuilder;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.management.resource.search.ResourceRoleFilterBuilder;
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

    public static ResourceManager createResourceManager() throws BaseException {
        DBConnectionFactory dbconn = new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");

        // set the persistence
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
