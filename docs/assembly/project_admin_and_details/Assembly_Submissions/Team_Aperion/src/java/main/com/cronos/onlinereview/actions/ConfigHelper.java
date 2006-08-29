/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * This class is a helper class that loads application's configuration parameters on application
 * startup and stores them in the internal member variables.  These parameters can easily be
 * accessed by the application later by through calls to a set of exposed methods.
 * <p>
 * This class is thread-safe as its inner state is initialized only once and
 * is not changed afterwards.
 * </p>
 *
 * @author TCSAssemblyTeam
 * @version 1.0
 */
class ConfigHelper {

    /**
     * This member variable is a string constant that specifies the configurtaion namespace under
     * which all configuration parameters for the application itself are stored.
     */
    private static final String ONLINE_REVIEW_CFG_NS = "com.cronos.OnlineReview";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the name of the session attribute which the ID of the currently logged in user
     * will be stored in.
     */
    private static final String USER_ID_ATTR_NAME_PROP = "user_identifer_key";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains definitions of Root Catalogs' IDs, icon filenames, and alternative text keys.
     *
     * @see #ROOT_CATALOG_ID_PROP
     * @see #ROOT_CATALOG_ICON_SM_PROP
     * @see #ROOT_CATALOG_ALT_TEXT_KEY_PROP
     */
    private static final String ROOT_CATALOGS_PROP = "RootCatalogs";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains Root Catalog ID that should be matched with an icon file.
     *
     * @see #ROOT_CATALOGS_PROP
     * @see #ROOT_CATALOG_ICON_SM_PROP
     * @see #ROOT_CATALOG_ALT_TEXT_KEY_PROP
     */
    private static final String ROOT_CATALOG_ID_PROP = "id";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains filename of a small icon that is matched to Root Catalog ID.
     *
     * @see #ROOT_CATALOGS_PROP
     * @see #ROOT_CATALOG_ID_PROP
     */
    private static final String ROOT_CATALOG_ICON_SM_PROP = "IconSmall";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains name of the key in Resource Messages file.  This key will be used to retrieve
     * localized name of the Root Catalog.  This name is usually displayed as an alternative
     * text for the icon.
     *
     * @see #ROOT_CATALOGS_PROP
     * @see #ROOT_CATALOG_ID_PROP
     * @see #ROOT_CATALOG_ICON_SM_PROP
     */
    private static final String ROOT_CATALOG_ALT_TEXT_KEY_PROP = "AltTextKey";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains definitions of Project Category name/icon filename pairs for icons that should
     * be matched to certain Project Categories.
     *
     * @see #PROJECT_CATEGORY_ICON_SM_PROP
     * @see #PROJECT_CATEGORY_ICON_PROP
     */
    private static final String PROJECT_CATEGORY_ICONS_PROP = "ProjectCategoryIcons";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains filename of a small icon that is matched to Project Category.
     *
     * @see #PROJECT_CATEGORY_ICONS_PROP
     * @see #PROJECT_CATEGORY_ICON_PROP
     */
    private static final String PROJECT_CATEGORY_ICON_SM_PROP = "IconSmall";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains filename of an icon that is matched to Project Category.
     *
     * @see #PROJECT_CATEGORY_ICONS_PROP
     * @see #PROJECT_CATEGORY_ICON_SM_PROP
     */
    private static final String PROJECT_CATEGORY_ICON_PROP = "Icon";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains definition of the whole Permissions Matrix.  The Matrix is defined on
     * per-permission basis, i.e. for every permission name there is a list of values, each value
     * defines the name of single Resource Role which this permission is granted to.
     */
    private static final String PERMISSIONS_MATRIX_PROP = "Permissions Matrix";

    /**
     * This member variable holds the name of the session attribute which ID of the currently logged
     * in user will be stored in.
     */
    private static String userIdAttributeName = "";

    /**
     * This member variable holds the names of small icons (.gif) files that should be displayed
     * on the JSP pages for different Root Catalog IDs.
     */
    private static final Map rootCatalogIconsSm = new HashMap();

    /**
     * This member variable holds the keys of Message Resources that should be used to match
     * Root Catalog ID with the name of that same Root Catalog.
     */
    private static final Map rootCatalogAltTextKeys = new HashMap();

    /**
     * This member variable holds the names of small icons (.gif) files that should be displayed
     * on the JSP pages for different Project Categories.
     */
    private static final Map projectCategoryIconsSm = new HashMap();

    /**
     * This member variable holds the names of icons (.gif) files that should be displayed
     * on the JSP pages for different Project Categories.
     */
    private static final Map projectCategoryIcons = new HashMap();

    /**
     * This member variable holds the names of all permissions for the application (as keys), and
     * lists of roles that have every of the permissions (as values for the corresponding keys).
     */
    private static final Map permissionsMatrix = new HashMap();

    static {
        // Obtaining the instance of Configurtaion Manager
        ConfigManager cfgMgr = ConfigManager.getInstance();

        try {
            // Retrieve the value of the property that contains the name of User ID session attribute
            String userIdAttr = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, USER_ID_ATTR_NAME_PROP);
            // If the value has been retrieved successfully ...
            if (userIdAttr != null && userIdAttr.trim().length() != 0) {
                // ... store it for later use
                userIdAttributeName = userIdAttr;
            }

            // Retrieve property that contains definitions of ID/filename pairs
            Property propRootCatIcons = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, ROOT_CATALOGS_PROP);
            // Prepare to enumerate all the nested properties
            Enumeration propsIcons = propRootCatIcons.propertyNames();

            while (propsIcons.hasMoreElements()) {
                // Get the name of the next property in the list
                String strPropName = (String)propsIcons.nextElement();

                // Retrieve the ID of the Root Catalog
                String strID = propRootCatIcons.getValue(strPropName + "." + ROOT_CATALOG_ID_PROP);
                // Verify that ID was read from the configuration
                if (strID == null || strID.trim().length() == 0) {
                    continue;
                }

                // Retieve small icon's filename that should be associated with the ID
                String strFilenameSm = propRootCatIcons.getValue(strPropName + "." + ROOT_CATALOG_ICON_SM_PROP);
                // Retrieve Message Resources key for alternative text -- the name of the Root Catalog
                String strAltTextKey = propRootCatIcons.getValue(strPropName + "." + ROOT_CATALOG_ALT_TEXT_KEY_PROP);

                // If small icon's filename has been read from the configuration ...
                if (strFilenameSm != null && strFilenameSm.trim().length() != 0) {
                    // ... store the ID/filename pair for later use
                    rootCatalogIconsSm.put(strID, strFilenameSm);
                }
                // If alternative text key has been read from the configuration ...
                if (strAltTextKey != null && strAltTextKey.trim().length() != 0) {
                    // ... store the ID/message-key pair for later use
                    rootCatalogAltTextKeys.put(strID, strAltTextKey);
                }
            }

            // Retrieve property that contains definitions of Project Category name/icon filename pairs
            Property propProjCatIcons = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, PROJECT_CATEGORY_ICONS_PROP);
            // Prepare to enumerate all the nested properties
            propsIcons = propProjCatIcons.propertyNames();

            while (propsIcons.hasMoreElements()) {
                // Get the name of the next property in the list.
                // The property name retrieved is also the name of a Project Category
                String strPropName = (String)propsIcons.nextElement();
                // Retrieve small icon's filename that should be associated with the Project Category name
                String strFilenameSm = propProjCatIcons.getValue(strPropName + "." + PROJECT_CATEGORY_ICON_SM_PROP);
                // Retrieve icon's filename that should be associated with the Project Category name
                String strFilename = propProjCatIcons.getValue(strPropName + "." + PROJECT_CATEGORY_ICON_PROP);

                // If filename for a small icon has been read fine ...
                if (strFilenameSm != null && strFilenameSm.trim().length() != 0) {
                    // ... store the Project Category name/small icon's filename for later use
                    projectCategoryIconsSm.put(strPropName, strFilenameSm);
                }
                // If filename for an icon has been read fine ...
                if (strFilename != null && strFilename.trim().length() != 0) {
                    // ... store the Project Category name/icon's filename for later use
                    projectCategoryIcons.put(strPropName, strFilename);
                }
            }

            // Retrieve property that contains definition of Permissions Matrix
            Property propPermissionsMatrix = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, PERMISSIONS_MATRIX_PROP);
            // Prepare to enumerate all permission name properties that are nested inside the Permissions Matrix one
            Enumeration permissionNames = propPermissionsMatrix.propertyNames();

            while (permissionNames.hasMoreElements()) {
                // Get the name of the next property in the list.
                // This will be the name of the Permission at the same time.
                String permissionName = (String)permissionNames.nextElement();
                // Retrive the names of roles which that permission is granted to
                String[] roles = propPermissionsMatrix.getValues(permissionName);

                // If everything has been read fine ...
                if (roles != null && roles.length != 0) {
                    // ... store the Permission name/list of names of Resource Roles for later use
                    permissionsMatrix.put(permissionName, roles);
                }
            }
        } catch (UnknownNamespaceException une) {
            // TODO: Add proper logging here
            System.out.println(une.getMessage());
            une.printStackTrace();
        }
    }

    /**
     * This static method returns the name of the session attribute which the ID of the currently
     * logged in user will be stored in.
     *
     * @return the name of the sessin attribute.
     */
    public static String getUserIdAttributeName() {
        return userIdAttributeName;
    }

    /**
     * This static method returns the filename of small icon that will be displayed on a JSP page
     * for some Root Catalog.  Root Catalog is pecified by its ID.
     *
     * @return the filename of small icon that should be used to represent particular Root Catalog.
     * @param rootCatalogId
     *            Root Catalog ID which small icon's filename should be looked up for.
     */
    public static String getRootCatalogIconNameSm(String rootCatalogId) {
        return (String)rootCatalogIconsSm.get(rootCatalogId);
    }

    /**
     * This static method returns the Message Resources key which will be used to retrieve the name
     * of the Root Catalog.  This name may later be used to display it somewhere on a page, say, via
     * alternative text for the Root Catalog icon.
     *
     * @return the key that should be used to look up the text string containing the name of the
     *         Root Catalog
     * @param rootCatalogId
     *            Root Catalog ID which name should be looked up for.
     */
    public static String getRootCatalogAltTextKey(String rootCatalogId) {
        return (String)rootCatalogAltTextKeys.get(rootCatalogId);
    }

    /**
     * This static method returns the filename of small icon that will be displayed on JSP page for
     * certain Project Category, which is specified by <code>projectCategoryName</code> parameter.
     *
     * @return the filename of small icon that should be used to represent particular Project
     *         Category.
     * @param projectCategoryName
     *            Project Category name which small icon's filename should be looked up for.
     */
    public static String getProjectCategoryIconNameSm(String projectCategoryName) {
        return (String)projectCategoryIconsSm.get(projectCategoryName);
    }

    /**
     * This static method returns the filename of icon that will be displayed on JSP page for
     * certain Project Category, which is specified by <code>projectCategoryName</code> parameter.
     *
     * @return the filename of icon that should be used to represent particular Project Category.
     * @param projectCategoryName
     *            Project Category name which icon's filename should be looked up for.
     */
    public static String getProjectCategoryIconName(String projectCategoryName) {
        return (String)projectCategoryIcons.get(projectCategoryName);
    }

    /**
     * This static method returns the list of roles for some particular permission name as defined
     * by Permissions Matrix.  The array of roles returned denote Resource Roles that have
     * permission specified by <code>permissionName</code> parameter granted.  If permission with
     * the specified name has not been found, or found permission does not have any resource roles
     * assigned, this method returns empty array.
     *
     * @return array of names of Resource Roles that are granted some certain permission.
     * @param permissionName
     *            name of the permission which list of role names should be retrieved for.
     */
    public static String[] getRolesForPermission(String permissionName) {
        String[] roles = (String[])permissionsMatrix.get(permissionName);
        return (roles != null) ? roles : new String[0];
    }
}
