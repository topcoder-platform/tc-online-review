/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * changed by ahmad1986 - 5/9/2008 - BUGR-404
 * @author George1
 * @author real_vg
 * @version 1.0
 */
public class ConfigHelper {

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
     * contains name of the key in Resource Messages file.  This key will be used to retrieve
     * a flag showing if the catalog is custom.  
     *
     * @see #ROOT_CATALOGS_PROP
     * @see #ROOT_CATALOG_ID_PROP
     */
    private static final String ROOT_CATALOG_CUSTOM_KEY_PROP = "Custom";
    
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
     * contains definitions of Project Type links to full description for the project.
     */
    private static final String PROJECT_TYPE_DESCRIPTION_LINKS_PROP = "ProjectTypeDescriptionLinks";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains definitions of Project Type links to forum for the project.
     */
    private static final String PROJECT_TYPE_FORUM_LINKS_PROP = "ProjectTypeForumLinks";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains default values used in the application, such as default phase length, default note
     * length, etc.
     *
     * @see #PIXELS_PER_HOUR_PROP
     * @see #PHASE_DURATION_PROP
     * @see #NOTE_LENGTH_PROP
     * @see #REQ_REGISTRANTS_PROP
     * @see #REQ_SUBMISSIONS_PROP
     * @see #REQ_REVIEWERS_PROP
     * @see #DEADLINE_NEAR_DURATION_PROP
     */
    private static final String DEFAULT_VALUES_PROP = "Defaults";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the amount of pixels displayed in the Gantt chart for every hour.
     *
     * @see #DEFAULT_VALUES_PROP
     */
    private static final String PIXELS_PER_HOUR_PROP = "PixelsPerHour";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the default duration, in hours, for all newly-created phases.
     *
     * @see #DEFAULT_VALUES_PROP
     */
    private static final String PHASE_DURATION_PROP = "PhaseDuration";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the default minimum length, in characters, of project's note text.
     *
     * @see #DEFAULT_VALUES_PROP
     */
    private static final String NOTE_LENGTH_PROP = "NoteLength";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the default minimum amount of registrants required before Registration phase can end.
     *
     * @see #DEFAULT_VALUES_PROP
     */
    private static final String REQ_REGISTRANTS_PROP = "RequiredRegistrants";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the default minimum amount of passing submissions before Submission phase can end.
     *
     * @see #DEFAULT_VALUES_PROP
     */
    private static final String REQ_SUBMISSIONS_PROP = "RequiredSubmissions";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the default minimum amount of registered reviewers before Review phase can end.
     *
     * @see #DEFAULT_VALUES_PROP
     */
    private static final String REQ_REVIEWERS_PROP = "RequiredReviewers";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the time duration, in hours, before phase ends during which outstanding deliverables
     * are displayed with &quot;Deadline&#160;Near&quot; status, and phases' statuses are shown as
     * &quot;Closing&quot;.
     *
     * @see #DEFAULT_VALUES_PROP
     */
    private static final String DEADLINE_NEAR_DURATION_PROP = "DeadlineNearDuration";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains format-strings to use to build formatting classes to format different values into
     * strings.
     *
     * @see #SCORECARD_SCORE_FORMAT_PROP
     * @see #MONETARY_VALUE_FULL_FORMAT_PROP
     * @see #MONETARY_VALUE_NO_FRAC_FORMAT_PROP
     * @see #DATE_FORMAT_PROP
     * @see #DATE_ONLY_FORMAT_PROP
     * @see #TIME_ONLY_FORMAT_PROP
     */
    private static final String FORMATS_PROP = "Formats";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the formatting string used to format scorecard scores.
     *
     * @see #FORMATS_PROP
     */
    private static final String SCORECARD_SCORE_FORMAT_PROP = "ScorecardScore";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the formatting string used to format monetary values (used to display payment
     * amounts).
     *
     * @see #FORMATS_PROP
     */
    private static final String MONETARY_VALUE_FULL_FORMAT_PROP = "MonetaryValueFull";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the formatting string used to format monetary values without fractional part (used to
     * display payment amounts).
     *
     * @see #FORMATS_PROP
     */
    private static final String MONETARY_VALUE_NO_FRAC_FORMAT_PROP = "MonetaryValueNoFrac";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the formatting string used to format dates.
     *
     * @see #FORMATS_PROP
     */
    private static final String DATE_FORMAT_PROP = "Date";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the formatting string used to format dates (only the date part of them).
     *
     * @see #FORMATS_PROP
     */
    private static final String DATE_ONLY_FORMAT_PROP = "DateOnly";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * defines the formatting string used to format dates (only the time part of them).
     *
     * @see #FORMATS_PROP
     */
    private static final String TIME_ONLY_FORMAT_PROP = "TimeOnly";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains definition of the whole Permissions Matrix.  The Matrix is defined on
     * per-permission basis, i.e. for every permission name there is a list of values, each value
     * defines the name of single Resource Role which this permission is granted to.
     */
    private static final String PERMISSIONS_MATRIX_PROP = "Permissions Matrix";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains definitions of the phase groups. The phases that belong to the same group will be
     * displayed under the same tab on View Project Details page.
     *
     * @see #PHASE_GROUP_RM_KEY_PROP
     * @see #PHASE_GROUP_TBL_NAME_KEY_PROP
     * @see #PHASES_DEFINITIONS_PROP
     * @see #PHASE_GROUP_APP_FUNCTION_PROP
     */
    private static final String PHASE_GROUPING_PROP = "PhaseGrouping";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the name of the key in message resources file. This key denotes message that should
     * be displayed for grouped phases.
     *
     * @see #PHASE_GROUPING_PROP
     * @see #PHASE_GROUP_TBL_NAME_KEY_PROP
     * @see #PHASES_DEFINITIONS_PROP
     * @see #PHASE_GROUP_APP_FUNCTION_PROP
     */
    private static final String PHASE_GROUP_RM_KEY_PROP = "NameKey";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the name of the key in message resources file. This key denotes message that should
     * be displayed for tables for grouped phases.
     *
     * @see #PHASE_GROUPING_PROP
     * @see #PHASE_GROUP_RM_KEY_PROP
     * @see #PHASES_DEFINITIONS_PROP
     * @see #PHASE_GROUP_APP_FUNCTION_PROP
     */
    private static final String PHASE_GROUP_TBL_NAME_KEY_PROP = "TableNameKey";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the names of phases that will be considered as belonging to the same.
     *
     * @see #PHASE_GROUPING_PROP
     * @see #PHASE_GROUP_RM_KEY_PROP
     * @see #PHASE_GROUP_TBL_NAME_KEY_PROP
     * @see #PHASE_GROUP_APP_FUNCTION_PROP
     */
    private static final String PHASES_DEFINITIONS_PROP = "Phases";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the name of the application's functionality that should be excuted for the phase
     * group.
     *
     * @see #PHASE_GROUPING_PROP
     * @see #PHASE_GROUP_RM_KEY_PROP
     * @see #PHASE_GROUP_TBL_NAME_KEY_PROP
     * @see #PHASES_DEFINITIONS_PROP
     */
    private static final String PHASE_GROUP_APP_FUNCTION_PROP = "AppFunction";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains definitions of other proerties that describe how mails sent to managers will be
     * gererated.
     *
     * @see #EMAIL_TEMPLATE_SOURCE_TYPE_PROP
     * @see #EMAIL_TEMPLATE_NAME_PROP
     * @see #EMAIL_SUBJECT_PROP
     */
    private static final String CONTACT_MANAGER_EMAIL_PROP = "ContactManagerEmail";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * specifies the type of source where email template can be loaded from.
     *
     * @see #CONTACT_MANAGER_EMAIL_PROP
     * @see #EMAIL_TEMPLATE_NAME_PROP
     * @see #EMAIL_SUBJECT_PROP
     */
    private static final String EMAIL_TEMPLATE_SOURCE_TYPE_PROP = "EmailTemplateSource";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * specifies the name of file (or any other type of source) where email teplate can be loaded
     * from using the type of source specified in the propety which name is defined by
     * {@link #EMAIL_TEMPLATE_SOURCE_TYPE_PROP} constant.
     *
     * @see #CONTACT_MANAGER_EMAIL_PROP
     * @see #EMAIL_TEMPLATE_SOURCE_TYPE_PROP
     * @see #EMAIL_SUBJECT_PROP
     */
    private static final String EMAIL_TEMPLATE_NAME_PROP = "EmailTemplateName";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * specifies the subject that will be used in outgoing email.
     *
     * @see #CONTACT_MANAGER_EMAIL_PROP
     * @see #EMAIL_TEMPLATE_SOURCE_TYPE_PROP
     * @see #EMAIL_TEMPLATE_NAME_PROP
     */
    private static final String EMAIL_SUBJECT_PROP = "EmailSubject";

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
     * This member variable holds the custom root catalogs ids 
     */
    private static final Set customRootCatalogs = new HashSet();
    
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
     * This member variable holds the links to full project's descriptions per project type.
     */
    private static final Map projectTypeDescriptionLinks = new HashMap();

    /**
     * This member variable holds the links to forums per project type.
     */
    private static final Map projectTypeForumLinks = new HashMap();

    /**
     * This member variable holds the amount of pixels displayed in the Gantt Chart per every hour.
     * The default value of this varibale is 5.
     */
    private static int pixelsPerHour = 5;

    /**
     * This member variable holds the default duration of newly-created phase, in hours.
     */
    private static int phaseDuration = 168;

    /**
     * This member variable holds the default minimum length of text, in characters, that should be
     * entered into Note field for every new project created.
     */
    private static int noteLength = 1;

    /**
     * This member variable holds the default minimum amount of registrants required for ending
     * Registration phase.
     */
    private static int reqRegistrants = -1;

    /**
     * This member variable holds the default minimum amount of passing submissions required for
     * ending Submission phase.
     */
    private static int reqSubmissions = -1;

    /**
     * This member variable holds the default minimum amount of registred reviewers required for
     * ending Review phase.
     */
    private static int reqReviewers = -1;

    /**
     * This member variable holds the time duration, in hours, before phase ends during which
     * outstanding deliverables are shown with &quot;Deadline&#160;Near&quot; status, and statuses
     * of open phases are shown as &quot;Closing&quot;.
     */
    private static long deadlineNearDuration = 48;

    /**
     * This member variable holds the formatting string used to format scorecard scores.
     */
    private static String scorecardScoreFormat = "0";

    /**
     * This member variable holds the formatting string used to format monetary values.
     */
    private static String monetaryValueFullFormat = "#.##";

    /**
     * This member variable holds the formatting string used to format monetary values without
     * fractional part.
     */
    private static String monetaryValueNoFracFormat = "0";

    /**
     * This member variable holds the formatting string used to format dates.
     */
    private static String dateFormat = "MM.dd.yyyy hh:mm a";

    /**
     * This member variable holds the formatting string used to format dates (onlt the date part of
     * them).
     */
    private static String dateOnlyFormat = "MM.dd.yyyy";

    /**
     * This member variable holds the formatting string used to format dates (onlt the time part of
     * them).
     */
    private static String timeOnlyFormat = "hh:mm a";

    /**
     * This member variable holds the names of all permissions for the application (as keys), and
     * lists of roles that have every of the permissions (as values for the corresponding keys).
     */
    private static final Map permissionsMatrix = new HashMap();

    /**
     * This member variable holds the list of names of the phase groups. The names are represented
     * as keys that should be used to retrieve localized group name from the message resources file.
     * Every item in this list should be of type <code>String</code> and cannot be
     * <code>null</code>.
     */
    private static final List phaseGroupNames = new ArrayList();

    /**
     * This member variable holds the list of names of tables for the phase groups. The names are
     * represented as keys that should be used to retrieve localized name from the message resources
     * file. Every item in this list should be of type <code>String</code> and cannot be
     * <code>null</code>.
     */
    private static final List phaseGroupTableNames = new ArrayList();

    /**
     * This member variable holds the list of sets. Every set in this list denotes a single phase
     * group and defines the phases included in that group.
     */
    private static final List phaseGroupPhases = new ArrayList();

    /**
     * This member variable holds the list of names of application's functionalities that should be
     * executed for the corresponding phase group. Every item in this list
     * should be of type <code>String</code> and cannot be <code>null</code>.
     */
    private static final List phaseGroupFunctions = new ArrayList();

    /**
     * This member variable holds the type of the source that will be used to load email template to
     * send message to project's manager.
     */
    private static String contactManagerEmailSrcType = "";

    /**
     * This member variable holds the path where email template can be loaded from to send message
     * to project's manager.
     */
    private static String contactManagerEmailTemplate = "";

    /**
     * This member variable holds the subject of email message that will be used when sending
     * messages to project's manager.
     * BUGR-404
     */
    //private static String contactManagerEmailSubject = "";

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
                String strPropName = (String) propsIcons.nextElement();

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
                
                // Retrieve custom catalog flag 
                String custom = propRootCatIcons.getValue(strPropName + "." + ROOT_CATALOG_CUSTOM_KEY_PROP);
                if (custom != null && custom.trim().length() != 0 && custom.trim().equalsIgnoreCase("true")) {
                    customRootCatalogs.add(strID);
                }
            }

            // Retrieve property that contains definitions of Project Category name/icon filename pairs
            Property propProjCatIcons = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, PROJECT_CATEGORY_ICONS_PROP);
            // Prepare to enumerate all the nested properties
            propsIcons = propProjCatIcons.propertyNames();

            while (propsIcons.hasMoreElements()) {
                // Get the name of the next property in the list.
                // The property name retrieved is also the name of a Project Category
                String strPropName = (String) propsIcons.nextElement();
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

            // Retrieve property that contains definitions of Project Type description links
            Property propProjTypeDesc = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, PROJECT_TYPE_DESCRIPTION_LINKS_PROP);
            // Prepare to enumerate all the nested properties
            Enumeration propsLinks = propProjTypeDesc.propertyNames();

            while (propsLinks.hasMoreElements()) {
                // Get the name of the next property in the list.
                // The property name retrieved is also the name of a Project Type
                String strPropName = (String) propsLinks.nextElement();
                // Retrieve link to full description
                String strLink = propProjTypeDesc.getValue(strPropName);

                // If the link has been read fine ...
                if (strLink != null && strLink.trim().length() != 0) {
                    // ... store it into the appropriate map for later use
                    projectTypeDescriptionLinks.put(strPropName, strLink);
                }
            }

            // Retrieve property that contains definitions of Project Type forum links
            Property propProjTypeForum = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, PROJECT_TYPE_FORUM_LINKS_PROP);
            // Prepare to enumerate all the nested properties
            propsLinks = propProjTypeForum.propertyNames();

            while (propsLinks.hasMoreElements()) {
                // Get the name of the next property in the list.
                // The property name retrieved is also the name of a Project Type
                String strPropName = (String) propsLinks.nextElement();
                // Retrieve link to forum
                String strLink = propProjTypeForum.getValue(strPropName);

                // If the link has been read fine ...
                if (strLink != null && strLink.trim().length() != 0) {
                    // ... store it into the appropriate map for later use
                    projectTypeForumLinks.put(strPropName, strLink);
                }
            }

            // Retrieve property that contains definitions of some default values
            Property propDefaults = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, DEFAULT_VALUES_PROP);
            // Get the amount of pixels to display for every hour
            String pixelsStr = propDefaults.getValue(PIXELS_PER_HOUR_PROP);
            // Get the default phase duration
            String phaseDurationStr = propDefaults.getValue(PHASE_DURATION_PROP);
            // Get the default note length
            String noteLengthStr = propDefaults.getValue(NOTE_LENGTH_PROP);
            // Get the default minimum registrants amount
            String reqRegistrantsStr = propDefaults.getValue(REQ_REGISTRANTS_PROP);
            // Get the default minimum submissions amount
            String reqSubmissionsStr = propDefaults.getValue(REQ_SUBMISSIONS_PROP);
            // Get the default minimum submissions amount
            String reqReviewersStr = propDefaults.getValue(REQ_REVIEWERS_PROP);
            // Get the duration of "Deadline Near" status
            String deadlineNearDurationStr = propDefaults.getValue(DEADLINE_NEAR_DURATION_PROP);

            // Verify that amount of pixels was specified, and assign it
            if (pixelsStr != null && pixelsStr.trim().length() != 0) {
                int pixels = Integer.parseInt(pixelsStr, 10);
                if (pixels > 0) {
                    pixelsPerHour = pixels;
                }
            }
            // Verify that default phase duration was specified, and assign it
            if (phaseDurationStr != null && phaseDurationStr.trim().length() != 0) {
                int duration = Integer.parseInt(phaseDurationStr, 10);
                if (duration >= 0) {
                    phaseDuration = duration;
                }
            }
            // Verify that default note length was specified, and assign it
            if (noteLengthStr != null && noteLengthStr.trim().length() != 0) {
                int length = Integer.parseInt(noteLengthStr, 10);
                if (length >= 0) {
                    noteLength = length;
                }
            }
            // Verify that default minimum registrants amount is specified, and assign it
            if (reqRegistrantsStr != null && reqRegistrantsStr.trim().length() != 0) {
                int minimum = Integer.parseInt(reqRegistrantsStr, 10);
                if (minimum >= 0) {
                    reqRegistrants = minimum;
                }
            }
            // Verify that default minimum submissions amount is specified, and assign it
            if (reqSubmissionsStr != null && reqSubmissionsStr.trim().length() != 0) {
                int minimum = Integer.parseInt(reqSubmissionsStr, 10);
                if (minimum >= 0) {
                    reqSubmissions = minimum;
                }
            }
            // Verify that default minimum submissions amount is specified, and assign it
            if (reqReviewersStr != null && reqReviewersStr.trim().length() != 0) {
                int minimum = Integer.parseInt(reqReviewersStr, 10);
                if (minimum >= 0) {
                    reqReviewers = minimum;
                }
            }
            // Verify that duration of "Deadline Near" status was specified, and assign it
            if (deadlineNearDurationStr != null && deadlineNearDurationStr.trim().length() != 0) {
                long duration = Long.parseLong(deadlineNearDurationStr, 10);
                if (duration >= 0) {
                    deadlineNearDuration = duration;
                }
            }

            // Retrieve property that contains definitions of formatting strings
            Property propFormats = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, FORMATS_PROP);
            // Get a formatting string for scorecard scores
            String formattingString = propFormats.getValue(SCORECARD_SCORE_FORMAT_PROP);
            if (formattingString != null && formattingString.trim().length() != 0) {
                scorecardScoreFormat = formattingString;
            }
            // Get a formatting string for monetary values (full form)
            formattingString = propFormats.getValue(MONETARY_VALUE_FULL_FORMAT_PROP);
            if (formattingString != null && formattingString.trim().length() != 0) {
                monetaryValueFullFormat = formattingString;
            }
            // Get a formatting string for monetary values (form with no fractional part)
            formattingString = propFormats.getValue(MONETARY_VALUE_NO_FRAC_FORMAT_PROP);
            if (formattingString != null && formattingString.trim().length() != 0) {
                monetaryValueNoFracFormat = formattingString;
            }
            // Get a formatting string for dates
            formattingString = propFormats.getValue(DATE_FORMAT_PROP);
            if (formattingString != null && formattingString.trim().length() != 0) {
                dateFormat = formattingString;
            }
            // Get a formatting string for dates-only
            formattingString = propFormats.getValue(DATE_ONLY_FORMAT_PROP);
            if (formattingString != null && formattingString.trim().length() != 0) {
                dateOnlyFormat = formattingString;
            }
            // Get a formatting string for times-only
            formattingString = propFormats.getValue(TIME_ONLY_FORMAT_PROP);
            if (formattingString != null && formattingString.trim().length() != 0) {
                timeOnlyFormat = formattingString;
            }

            // Retrieve property that contains definition of Permissions Matrix
            Property propPermissionsMatrix = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, PERMISSIONS_MATRIX_PROP);
            // Prepare to enumerate all permission name properties that are nested inside the Permissions Matrix one
            Enumeration permissionNames = propPermissionsMatrix.propertyNames();

            while (permissionNames.hasMoreElements()) {
                // Get the name of the next property in the list.
                // This will be the name of the Permission at the same time.
                String permissionName = (String) permissionNames.nextElement();
                // Retrive the names of roles which that permission is granted to
                String[] roles = propPermissionsMatrix.getValues(permissionName);

                // If everything has been read fine ...
                if (roles != null && roles.length != 0) {
                    // ... store the Permission name/list of names of Resource Roles for later use
                    permissionsMatrix.put(permissionName, roles);
                }
            }

            // Retrieve property that cantains definitions of phase groups
            Property propPhaseGrouping = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, PHASE_GROUPING_PROP);
            // Prepare to enumerate all group definition properties
            Enumeration phaseGroups = propPhaseGrouping.propertyNames();

            while (phaseGroups.hasMoreElements()) {
                // Get the name of the next property in the list.
                String propertyName = ((String) phaseGroups.nextElement()) + ".";
                // Retrieve a name of a key that will point to a message containing the name of group
                String strGroupNameKey = propPhaseGrouping.getValue(propertyName + PHASE_GROUP_RM_KEY_PROP);
                // Retrieve a name of a key that will point to a message containing the name of table
                String strGroupTableNameKey = propPhaseGrouping.getValue(propertyName + PHASE_GROUP_TBL_NAME_KEY_PROP);
                // Retrieve an array of phase names included in this group
                String[] strPhases = propPhaseGrouping.getValues(propertyName + PHASES_DEFINITIONS_PROP);
                // Retrieve a name of application's functionality
                String strAppFunction = propPhaseGrouping.getValue(propertyName + PHASE_GROUP_APP_FUNCTION_PROP);

                // If everything has been read fine ...
                if (strGroupNameKey != null && strGroupNameKey.trim().length() != 0 &&
                        strAppFunction != null && /*strAppFunction.trim().length() != 0 &&*/
                        strPhases != null && strPhases.length != 0) {
                    // ... store phase group definition for later use
                    phaseGroupNames.add(strGroupNameKey.trim());
                    phaseGroupFunctions.add(strAppFunction.trim());

                    if (strGroupTableNameKey != null && strGroupTableNameKey.trim().length() != 0) {
                        phaseGroupTableNames.add(strGroupTableNameKey.trim());
                    } else {
                        phaseGroupTableNames.add(phaseGroupNames.get(phaseGroupNames.size() - 1));
                    }

                    Set phasesSet = new HashSet();
                    phaseGroupPhases.add(phasesSet);

                    for (int i = 0; i < strPhases.length; ++i) {
                        phasesSet.add(strPhases[i]);
                    }
                }
            }

            Property propContactManagerEmail =
                cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, CONTACT_MANAGER_EMAIL_PROP);

            if (propContactManagerEmail != null) {
                contactManagerEmailSrcType = propContactManagerEmail.getValue(EMAIL_TEMPLATE_SOURCE_TYPE_PROP);
                contactManagerEmailTemplate = propContactManagerEmail.getValue(EMAIL_TEMPLATE_NAME_PROP);
		  // BUGR-404
                // contactManagerEmailSubject = propContactManagerEmail.getValue(EMAIL_SUBJECT_PROP);
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
        return (String) rootCatalogIconsSm.get(rootCatalogId);
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
        return (String) rootCatalogAltTextKeys.get(rootCatalogId);
    }

    /**
     * This static method returns true if the specified root catalog id is custom
     *
     * @return true if the specified root catalog id is custom
     * @param rootCatalogId
     *            Root Catalog ID which to look for.
     */
    public static boolean isCustomRootCatalog(String rootCatalogId) {
        return customRootCatalogs.contains(rootCatalogId);
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
        return (String) projectCategoryIconsSm.get(projectCategoryName);
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
        return (String) projectCategoryIcons.get(projectCategoryName);
    }

    /**
     * This static method returns the link to the full description for project based on the type of
     * project passed as parameter.
     * 
     * @return the link to full description of the project.
     * @param projectTypeName
     *            Project Type name which link to full description should be looked up for.
     * @param componentId
     *            ID of the component (numeric value) that should be substituded instead of
     *            &quot;<code>&lt;COMPONENT_ID&gt;</code>&quot; substring in the template link read
     *            from the configuration. If this value is zero or negative, the aforementioned
     *            substring will be simply removed from the template link.
     * @param versionId
     *            ID of the component version (numeric value) that should be substituded instead of
     *            &quot;<code>&lt;VERSION_ID&gt;</code>&quot; substring in the template link
     *            read from the configuration. If this value is zero or negative, the aforementioned
     *            substring will be simply removed from the template link.
     */
    public static String getProjectTypeDescriptionLink(String projectTypeName, long componentId, long versionId) {
        String templateLink = (String) projectTypeDescriptionLinks.get(projectTypeName);

        templateLink = templateLink.replaceFirst("\\<COMPONENT_ID\\>", (componentId > 0) ? String.valueOf(componentId) : "");
        templateLink = templateLink.replaceFirst("\\<VERSION_ID\\>", (versionId > 0) ? String.valueOf(versionId) : "");

        return templateLink;
    }

    /**
     * This static method returns the link to the forum for project based on the type of project
     * passed as parameter.
     *
     * @return the link to forum for the project.
     * @param projectTypeName
     *            Project Type name which link to full description should be looked up for.
     * @param forumId
     *            ID of the forum (numeric value) that should be substituded instead of
     *            &quot;<code>&lt;FORUM_ID&gt;</code>&quot; substring in the template link read from
     *            the configuration. If this value is zero or negative, the aforementioned substring
     *            will be simply removed from the template link.
     */
    public static String getProjectTypeForumLink(String projectTypeName, long forumId) {
        String templateLink = (String) projectTypeForumLinks.get(projectTypeName);

        templateLink = templateLink.replaceFirst("\\<FORUM_ID\\>", (forumId > 0) ? String.valueOf(forumId) : "");
        return templateLink;
    }

    /**
     * This static method returns the amount of pixels that should be displayed for each hour in
     * the project's Gantt chart.
     *
     * @return a value that reporesent the amount of pixels per each hour.
     */
    public static int getPixelsPerHour() {
        return pixelsPerHour;
    }

    /**
     * This static method returns default duration, in hours, that will be used on Create/Edit
     * Project page every time a new phase is added to the timeline.
     *
     * @return default duration of a phase, in hours.
     */
    public static int getDefaultPhaseDuration() {
        return phaseDuration;
    }

    /**
     * This static method returns default minimum length of the text, in characters, that should be
     * entered into Note field for every new project.
     *
     * @return default minimum length of text, in characters.
     */
    public static int getDefaultNoteLength() {
        return noteLength;
    }

    /**
     * This static method returns default minimum of registrants required before Registration phase
     * can be closed.
     *
     * @return default minimum amount, or -1 if there is no default minimum value.
     */
    public static int getDefaultRequiredRegistrants() {
        return reqRegistrants;
    }

    /**
     * This static method returns default minimum of submissions required before Submission phase
     * can be closed.
     *
     * @return default minimum amount, or -1 if there is no default minimum value.
     */
    public static int getDefaultRequiredSubmissions() {
        return reqSubmissions;
    }

    /**
     * This static method returns default minimum of registered reviewers required before Review
     * phase can be closed.
     *
     * @return default minimum amount, or -1 if there is no default minimum value.
     */
    public static int getDefaultRequiredReviewers() {
        return reqReviewers;
    }

    /**
     * This static method returns the time duration, in hours, before phase ends during which all
     * outstanding deliverables are shown with &quot;Deadline&#160;Near&quot; status, and all open
     * phases are shown with &quot;Closing&quot; status.
     *
     * @return the time duration, in hours.
     */
    public static long getDeadlineNearDuration() {
        return deadlineNearDuration;
    }

    /**
     * This static method returns the formatting string used to format scorecard scores.
     *
     * @return formatting string for scorecard scores.
     */
    public static String getScorecardScoreFormat() {
        return scorecardScoreFormat;
    }

    /**
     * This static method returns the formatting string used to format monetary values.
     *
     * @return formatting string for monetary values.
     */
    public static String getMonetaryValueFullFormat() {
        return monetaryValueFullFormat;
    }

    /**
     * This static method returns the formatting string used to format monetary values without
     * fractional part.
     *
     * @return formatting string for monetary values.
     */
    public static String getMonetaryValueNoFracFormat() {
        return monetaryValueNoFracFormat;
    }

    /**
     * This static method returns the formatting string used to format dates.
     *
     * @return formatting string for dates.
     */
    public static String getDateFormat() {
        return dateFormat;
    }

    /**
     * This static method returns the formatting string used to format dates.
     *
     * @return formatting string for dates. This string will output only the date part (no time).
     */
    public static String getDateOnlyFormat() {
        return dateOnlyFormat;
    }

    /**
     * This static method returns the formatting string used to format dates.
     *
     * @return formatting string for dates. This string will output only the time part (no date).
     */
    public static String getTimeOnlyFormat() {
        return timeOnlyFormat;
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
        String[] roles = (String[]) permissionsMatrix.get(permissionName);
        return (roles != null) ? roles : new String[0];
    }

    /**
     * This static method returns the number of phase groups defined in the configuration.
     *
     * @return the number of phase groups.
     */
    public static int getNumberOfPhaseGroups() {
        return phaseGroupPhases.size();
    }

    /**
     * This static method finds the first phase group that contains a phase specified by its name,
     * and returns an index of that phase group.
     *
     * @return an index of the phase group containing specified phase.
     * @param phaseName
     *            a name of the phase.
     */
    public static int findPhaseGroupForPhaseName(String phaseName) {
        for (int i = 0; i < phaseGroupPhases.size(); ++i) {
            if (((Set) phaseGroupPhases.get(i)).contains(phaseName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This static method returns the name of a key for a phase group referenced by its index.
     *
     * @return the name of a key. This key can later be used to retrieve the loclized name of phase
     *         group from message resources.
     * @param index
     *            an index of a phase group to retrieve the name of a key for.
     */
    public static String getPhaseGroupNameKey(int index) {
        return (String) phaseGroupNames.get(index);
    }

    /**
     * This static method returns the name of a key for a phase group referenced by its index.
     *
     * @return the name of a key. This key can later be used to retrieve the loclized name of table
     *         for some phase group from message resources.
     * @param index
     *            an index of a phase group to retrieve the name of a key for.
     */
    public static String getPhaseGroupTableNameKey(int index) {
        return (String) phaseGroupTableNames.get(index);
    }

    /**
     * This static method determines if a phase group referenced by its index contains specified
     * phase. The phase is specified by its name.
     *
     * @return <code>true</code> if specified phase group contains the phase, <code>false</code>
     *         if it doesn't.
     * @param index
     *            an index of a phase group.
     * @param phaseName
     *            a name of the phase which presense in the phase group is to be tested.
     */
    public static boolean isPhaseGroupContainsPhase(int index, String phaseName) {
        return ((Set) phaseGroupPhases.get(index)).contains(phaseName);
    }

    /**
     * This static method returns the name of application's functionality that should be executed
     * for the phase group specified by its index.
     *
     * @return the name of application's functionality for a particular phase group.
     * @param index
     *            an index of a phase group.
     */
    public static String getPhaseGroupAppFunction(int index) {
        return (String) phaseGroupFunctions.get(index);
    }

    /**
     * This static method returns the type of the source where email template to send to project's
     * manager can be loaded from.
     *
     * @return a string representing the type of source.
     */
    public static String getContactManagerEmailSrcType() {
        return contactManagerEmailSrcType;
    }

    /**
     * This static method returns the path where email template to send to project's manager can be
     * loaded from.
     *
     * @return a string containing the path to email template.
     */
    public static String getContactManagerEmailTemplate() {
        return contactManagerEmailTemplate;
    }

    /**
     * This method returns the subject of the email message that will be sent to project's manager.
     *
     * @param component the component name
     * @param handle the handle who sends the email
     * @param subject the subject value
     * @return a string containing the subject.
     *
     * BUGR-404
     */
    public static String getContactManagerEmailSubject(String component,String handle,String subject) {
        return component+"-"+handle+"-"+subject;
    } 

    /**
     * Return the property value of online_review namespace.
     *
     * @param name the property name
     * @param defaultValue the default value
     * @return property value
     */
    public static String getPropertyValue(String name, String defaultValue) {
		try {
			String value = ConfigManager.getInstance().getString(ONLINE_REVIEW_CFG_NS, name);
	    	if (value != null && value.trim().length() > 0) {
	    		return value;
	    	}
		} catch (UnknownNamespaceException e) {
			// Ignore
		}
		return defaultValue;
	}
}
