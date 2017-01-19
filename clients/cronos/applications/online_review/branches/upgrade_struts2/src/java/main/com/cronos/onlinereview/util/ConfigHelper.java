/*
 * Copyright (C) 2004 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ConfigHelper {

    /**
     * This member variable is a string constant that specifies the configuration namespace under
     * which all configuration parameters for the application itself are stored.
     */
    private static final String ONLINE_REVIEW_CFG_NS = "com.cronos.OnlineReview";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the submitter role id.
     */
    private static final String SUBMITTER_ROLE_ID_NAME_PROP = "submitter_role_id";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the reviewer role id.
     */
    private static final String REVIEWER_ROLE_ID_NAME_PROP = "reviewer_role_id";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the accuracy reviewer role id.
     */
    private static final String ACCURACY_REVIEWER_ROLE_ID_NAME_PROP = "accuracy_reviewer_role_id";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the failure reviewer role id.
     */
    private static final String FAILURE_REVIEWER_ROLE_ID_NAME_PROP = "failure_reviewer_role_id";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the stress reviewer role id.
     */
    private static final String STRESS_REVIEWER_ROLE_ID_NAME_PROP = "stress_reviewer_role_id";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the primary screener role id.
     */
    private static final String PRIMARY_SCREENER_ROLE_ID_NAME_PROP = "primary_screener_role_id";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the aggregator role id.
     */
    private static final String AGGREGATOR_ROLE_ID_NAME_PROP = "aggregator_role_id";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the final reviewer role id.
     */
    private static final String FINAL_REVIEWER_ROLE_ID_NAME_PROP = "final_reviewer_role_id";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains the pacts payment detail URL.
     */
    private static final String PACTS_PAYMENT_DETAIL_URL_PROP = "pacts_payment_url";

    /**
     * This constant stores Online Review's project details page url property name.
     */
    private static final String PROP_PROJECT_DETAILS_URL = "ProjectDetailsURL";

    /**
     * This constant stores Direct's project page url property name
     */
    private static final String PROP_DIRECT_PROJECT_URL = "DirectProjectURL";

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
     * contains name of the key in Resource Messages file.  This key will be used to retrieve
     * the distribution script.
     *
     * @see #ROOT_CATALOGS_PROP
     * @see #ROOT_CATALOG_ID_PROP
     */
    private static final String ROOT_CATALOG_DISTRIBUTION_SCRIPT_KEY_PROP = "DistributionScript";

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
     * contains definitions of Project Type links to view contest page for the project.
     */
    private static final String PROJECT_TYPE_VIEW_CONTEST_LINKS_PROP = "ProjectTypeViewContestLinks";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains definitions of Project Type links to forum for the project.
     */
    private static final String PROJECT_TYPE_FORUM_LINKS_PROP = "ProjectTypeForumLinks";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains definitions of Deliverable Type Lookups for 'Late Deliverable' page.
     */
    private static final String DELIVERABLE_TYPES_PROP = "DeliverableTypes";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * contains default values used in the application, such as default phase length, default note
     * length, etc.
     *
     * @see #PIXELS_PER_HOUR_PROP
     * @see #PHASE_DURATION_PROP
     * @see #NOTE_LENGTH_PROP
     * @see #REQ_REGISTRANTS_PROP
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
     * contains the name of the application's functionality that should be executed for the phase
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
     * contains definitions of other properties that describe how mails sent to managers will be
     * generated.
     *
     * @see #EMAIL_TEMPLATE_NAME_PROP
     */
    private static final String CONTACT_MANAGER_EMAIL_PROP = "ContactManagerEmail";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * specifies the name of the file where email template can be loaded.
     *
     * @see #CONTACT_MANAGER_EMAIL_PROP
     */
    private static final String EMAIL_TEMPLATE_NAME_PROP = "EmailTemplateName";

    /**
     * This member variable is a string constant that specifies the name of the property which
     * specifies the email address of the email sender.
     *
     * @see #CONTACT_MANAGER_EMAIL_PROP
     */
    private static final String EMAIL_FROM_ADDRESS_PROP = "EmailFromAddress";

    /**
     * <p>This member variable is a string constant that specifies the name of the property which contains the
     * maximum number of days to extend the <code>Registration</code> phase.</p>
     */
    private static final String REGISTRATION_PHASE_MAX_EXTENSION_PROP = "registration_phase_extension_days_maximum";

    /**
     * <p>This member variable is a string constant that specifies the name of the property which contains the
     * maximum number of days to extend the <code>Submission</code> phase.</p>
     */
    private static final String SUBMISSION_PHASE_MAX_EXTENSION_PROP = "submission_phase_extension_days_maximum";

    /**
     * <p>This member variable is a string constant that specifies the name of the property which contains the
     * minimum number of hours before <code>Submission</code> phase deadline to allow the extension of
     * <code>Registration</code> or <code>Submission</code> phases.</p>
     */
    private static final String MINIMUM_HOURS_BEFORE_SUBMISSION_DEADLINE_FOR_EXTENSION_PROP
        = "minimum_hours_before_submission_deadline_for_phase_extension";

    /**
     * <p>An <code>int</code> providing the minimum time (in hours) to be left before project's submission deadline in
     * order to allow the extension of desired project phase. If less than specified time is left then phase extension
     * must be prohibited.</p>
     */
    private static final int DEFAULT_MINIMUM_HOURS_LEFT = 48;

    /**
     * <p>This member variable is a string that specifies the name of the property which contains the
     * output dir for the distribution tool.</p>
     */
    private static final String DISTRIBUTION_TOOL_OUTPUT_DIR_PROP = "distribution_tool_output_dir";

    /**
     * <p>This is the default distribution tool output dir.</p>
     */
    private static final String DEFAULT_DISTRIBUTION_TOOL_OUTPUT_DIR = "/tmp";

    /**
     * <p>This member variable is a string constant that specifies the name of the property which contains the
     * output dir for the TopCoder catalog.</p>
     */
    private static final String CATALOG_OUTPUT_DIR_PROP = "catalog_output_dir";

    /**
     * <p>This is the default catalog output dir.</p>
     */
    private static final String DEFAULT_CATALOG_OUTPUT_DIR = "/tmp";

    /**
     * <p>This member variable is a string constant that specifies the name of the property which contains the
     * default distribution tool script to use when no script is defined.</p>
     */
    private static final String DEFAULT_DISTRIBUTION_SCRIPT_PROP = "default_distribution_script";

    /**
     * <p>This is the default distribution tool script to use when no script is defined.</p>
     */
    private static final String DEFAULT_DISTRIBUTION_SCRIPT = "other";

    /**
     * <p>A <code>String</code> providing the name for configuration property listing the disabled resource roles.</p>
     */
    private static final String DISABLED_RESOURCE_ROLES_PROP = "DisabledResourceRoles";

    /**
     * <p>A <code>String</code> providing the name for configuration property listing the resource roles which are to be
     * granted permission for accessing SVN module for project once the project enters <code>Final Review</code> phase.
     * </p>
     */
    private static final String SVN_PERM_GRANT_RESOURCE_ROLES_PROP = "SVNPermissionGrantResourceRoles";

    /**
     * <p>A <code>String</code> providing the name for configuration property listing the parameters for SVN repository.
     * </p>
     */
    private static final String SVN_CONFIG_PROP = "SVNConfig";

    /**
     * <p>A <code>String</code> providing the name for configuration property listing the parameters of the Resources tabs
     * to be displayed in the Resource section in project detail page.</p>
     */
    private static final String RESOURCE_TABS_PROP_STRING = "ResourceTabs";

    /**
     * <p>A <code>String</code> providing the name for configuration property listing the admin user IDs.</p>
     */
    private static final String ADMIN_USERS_PROP = "AdminUsers";

    /**
     * <p>A <code>String</code> providing the name for thurgood api url property.</p>
     */
    private static final String THURGOOD_API_URL_PROP = "thurgood_api_url";

    /**
     * <p>A <code>String</code> providing the name for thurgood api key property.</p>
     */
    private static final String THURGOOD_API_KEY_PROP = "thurgood_api_key";

    /**
     * <p>A <code>String</code> providing the name for thurgood timeout property.</p>
     */
    private static final String THURGOOD_TIMEOUT_PROP = "thurgood_timeout";

    /**
     * <p>A <code>String</code> providing the name for thurgood code url property.</p>
     */
    private static final String THURGOOD_CODE_URL_PROP = "thurgood_code_url";

    /**
     * <p>A <code>String</code> providing the name for thurgood job base UI url property.</p>
     */
    private static final String THURGOOD_JOB_BASE_UI_URL_PROP = "thurgood_job_base_ui_url";

    /**
     * <p>A <code>String</code> providing the name for thurgood username property.</p>
     */
    private static final String THURGOOD_USERNAME_PROP = "thurgood_username";

    /**
     * <p>A <code>String</code> providing the name for thurgood password property.</p>
     */
    private static final String THURGOOD_PASSWORD_PROP = "thurgood_password";

    /**
     * This member variable holds the submitter role id.
     */
    private static int submitterRoleId = 1;

    /**
     * This member variable holds the reviewer role id.
     */
    private static int reviewerRoleId = 4;

    /**
     * This member variable holds the accuracy reviewer role id.
     */
    private static int accuracyReviewerRoleId = 5;

    /**
     * This member variable holds the failure reviewer role id.
     */
    private static int failureReviewerRoleId = 6;

    /**
     * This member variable holds the stress reviewer role id.
     */
    private static int stressReviewerRoleId = 7;

    /**
     * This member variable holds the primary screener role id.
     */
    private static int primaryScreenerRoleId = 2;

    /**
     * This member variable holds the aggregator role id.
     */
    private static int aggregatorRoleId = 8;

    /**
     * This member variable holds the final reviewer role id.
     */
    private static int finalReviewerRoleId = 9;

    /**
     * This constant stores Online Review's project details page URL.
     */
    private static String projectDetailsBaseURL;

    /**
     * This constant stores base URL for the project page in direct
     */
    private static String directProjectBaseURL;

    /**
     * This constant stores base URL for pacts payment detail page.
     * @see #PACTS_PAYMENT_DETAIL_URL_PROP
     */
    private static String pactsPaymentDetailBaseURL;

    /**
     * This member variable holds the names of small icons (.gif) files that should be displayed
     * on the JSP pages for different Root Catalog IDs.
     */
    private static final Map<String, String> rootCatalogIconsSm = new HashMap<String, String>();

    /**
     * This member variable holds the keys of Message Resources that should be used to match
     * Root Catalog ID with the name of that same Root Catalog.
     */
    private static final Map<String, String> rootCatalogAltTextKeys = new HashMap<String, String>();

    /**
     * This member variable holds the custom root catalogs ids
     */
    private static final Set<String> customRootCatalogs = new HashSet<String>();

    /**
     * This member variable holds the distribution tool script for root catalogs ids.
     */
    private static final Map<String, String> distributionScriptRootCatalogs = new HashMap<String, String>();

    /**
     * This member variable holds the names of small icons (.gif) files that should be displayed
     * on the JSP pages for different Project Categories.
     */
    private static final Map<String, String> projectCategoryIconsSm = new HashMap<String, String>();

    /**
     * This member variable holds the names of icons (.gif) files that should be displayed
     * on the JSP pages for different Project Categories.
     */
    private static final Map<String, String> projectCategoryIcons = new HashMap<String, String>();

    /**
     * This member variable holds the links to view contest pages per project type.
     */
    private static final Map<String, String> projectTypeViewContestLinks = new HashMap<String, String>();

    /**
     * This member variable holds the links to forums per project type.
     */
    private static final Map<String, String> projectTypeForumLinks = new HashMap<String, String>();

    /**
     * <p>
     * A <code>Map</code> mapping the deliverable type to deliverable id, use LinkedHashMap as order
     * should be maintained.
     * </p>
     */
    private static final Map<String, String> deliverableTypes = new LinkedHashMap<String, String>();

    /**
     * This member variable holds the amount of pixels displayed in the Gantt Chart per every hour.
     * The default value of this variable is 5.
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
     * This member variable holds the default minimum amount of registered reviewers required for
     * ending Review phase.
     */
    private static int reqReviewers = -1;

    /**
     * <p>An <code>int</code> providing the default number of required approvers for Approval phase.</p>
     */
    private static int reqApprovers = -1;

    /**
     * <p>An <code>int</code> providing the default number of required reviewers for Post-Mortem phase.</p>
     */
    private static int reqPostMortemReviewers = -1;

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
    private static String dateFormat = "MM.dd.yyyy HH:mm z";

    /**
     * This member variable holds the formatting string used to format dates (onlt the date part of
     * them).
     */
    private static String dateOnlyFormat = "MM.dd.yyyy";

    /**
     * This member variable holds the formatting string used to format dates (onlt the time part of
     * them).
     */
    private static String timeOnlyFormat = "HH:mm";

    /**
     * This member variable holds the names of all permissions for the application (as keys), and
     * lists of roles that have every of the permissions (as values for the corresponding keys).
     */
    private static Map<String, String[]> permissionsMatrix = new HashMap<String, String[]>();

    /**
     * This member variable holds the list of names of the phase groups. The names are represented
     * as keys that should be used to retrieve localized group name from the message resources file.
     * Every item in this list should be of type <code>String</code> and cannot be
     * <code>null</code>.
     */
    private static List<String> phaseGroupNames = new ArrayList<String>();

    /**
     * This member variable holds the list of names of tables for the phase groups. The names are
     * represented as keys that should be used to retrieve localized name from the message resources
     * file. Every item in this list should be of type <code>String</code> and cannot be
     * <code>null</code>.
     */
    private static List<String> phaseGroupTableNames = new ArrayList<String>();

    /**
     * This member variable holds the list of sets. Every set in this list denotes a single phase
     * group and defines the phases included in that group.
     */
    private static List<Set<String>> phaseGroupPhases = new ArrayList<Set<String>>();

    /**
     * This member variable holds the list of names of application's functionality that should be
     * executed for the corresponding phase group. Every item in this list
     * should be of type <code>String</code> and cannot be <code>null</code>.
     */
    private static List<String> phaseGroupFunctions = new ArrayList<String>();

    /**
     * This member variable holds the path where email template can be loaded from to send message
     * to project's manager.
     */
    private static String contactManagerEmailTemplate = "";

    /**
     * This member variable holds the "from" address for the messages to project's manager.
     */
    private static String contactManagerEmailFromAddress = "";

    /**
     * <p>An <code>Integer</code> providing the maximum number of days which <code>Registration</code> phase can be
     * extended for. <code>null</code> value means that such a limit is not specified.</p>
     */
    private static Integer registrationPhaseMaxExtensionDays = null;

    /**
     * <p>An <code>Integer</code> providing the maximum number of days which <code>Submission</code> phase can be
     * extended for. <code>null</code> value means that such a limit is not specified.</p>
     */
    private static Integer submissionPhaseMaxExtensionDays = null;

    /**
     * <p>An <code>Integer</code> providing the minimum number of hours before <code>Submission</code> phase deadline
     * to allow extension for <code>Registration</code> and <code>Submission</code> phases.</p>
     */
    private static Integer minimumHoursBeforeSubmissionDeadlineForExtension = DEFAULT_MINIMUM_HOURS_LEFT;

    /**
     * <p>
     * The distribution tool output dir.
     * </p>
     */
    private static String distributionToolOutputDir = DEFAULT_DISTRIBUTION_TOOL_OUTPUT_DIR;

    /**
     * <p>
     * The TopCoder catalog output dir.
     * </p>
     */
    private static String catalogOutputDir = DEFAULT_CATALOG_OUTPUT_DIR;

    /**
     * <p>
     * The default distribution script.
     * </p>
     */
    private static String defaultDistributionScript = DEFAULT_DISTRIBUTION_SCRIPT;

    /**
     * <p>A <code>String</code> array listing the IDs for resource roles which are not allowed for selection.</p>
     */
    private static String[] disabledResourceRoles;

    /**
     * <p>A <code>String</code> array listing the IDs for resource roles which are to be granted permission for
     * accessing SVN repository for project once the project enters <code>Final Review</code> phase.</p>
     */
    private static String[] svnPermissionGrantResourceRoles;

    /**
     * <p>A <code>String</code> array providing the SVN configuration.</p>
     */
    private static String[] svnConfig;

    /**
     * <p>A <code>String</code> array providing the configuration for email message to be sent when late deliverables
     * are updated by managers.</p>
     */
    private static String[] lateDeliverablesUpdatedByManagerNotificationConfig;

    /**
     * <p>A <code>String</code> array providing the configuration for email message to be sent when late deliverables
     * are updated by late members.</p>
     */
    private static String[] lateDeliverablesUpdatedByMemberNotificationConfig;

    /**
     * <p>A <code>String</code> providing the base URL for <code>Edit Late Deliverable</code> page.</p>
     */
    private static String lateDeliverableBaseURL;

    /**
     * <p>A <code>String</code> array providing the configuration for email message to be sent when a member
     * reuploads submission for an F2F project.</p>
     */
    private static String[] f2fSubmissionReuploadedNotificationConfig;

    /**
     * <p>A <code>Map</code> providing the Resources tabs to be displayed in the Resource section in project detail page.
     * The key is the tab name, the value is a <code>Set</code> of resource role IDs.</p>
     */
    private static final Map<String, Set<String>> resourceTabs = new LinkedHashMap<String, Set<String>>();

    /**
     * <p>A <code>List</code> for the admin user IDs.</p>
     */
    private static final List<Long> adminUsers = new ArrayList<Long>();

    /**
     * <p>Represents the Thurgood URL for creating and submitting the Thurgood job.</p>
     */
    private static String thurgoodApiURL;

    /**
     * <p>Represents the API Key for creating and submitting the Thurgood job.</p>
     */
    private static String thurgoodApiKey;

    /**
     * <p>Represents the timeout for creating and submitting the Thurgood job.</p>
     */
    private static int thurgoodTimeout = 5000;

    /**
     * <p>Represents the submission's code url for creating and submitting the Thurgood job.</p>
     */
    private static String thurgoodCodeURL;

    /**
     * <p>Represents the job base UI url of the submitted the Thurgood job.</p>
     */
    private static String thurgoodJobBaseUIURL;

    /**
     * <p>Represents the username of the Thurgood user.</p>
     */
    private static String thurgoodUsername;

    /**
     * <p>Represents the password of the Thurgood user.</p>
     */
    private static String thurgoodPassword;

    static {
        // Obtaining the instance of Configuration Manager
        ConfigManager cfgMgr = ConfigManager.getInstance();

        try {
            // Retrieve the value of the property that contains the submitter_role_id
            String value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, SUBMITTER_ROLE_ID_NAME_PROP);
            // If the value has been retrieved successfully ...
            if (value != null && value.trim().length() != 0) {
                // ... store it for later use
                try {
                    submitterRoleId = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    // don't do anything, keep the default
                }
            }

            // Retrieve the value of the property that contains the reviewer_role_id
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, REVIEWER_ROLE_ID_NAME_PROP);
            // If the value has been retrieved successfully ...
            if (value != null && value.trim().length() != 0) {
                // ... store it for later use
                try {
                    reviewerRoleId = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    // don't do anything, keep the default
                }
            }

            // Retrieve the value of the property that contains the accuracy_reviewer_role_id
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, ACCURACY_REVIEWER_ROLE_ID_NAME_PROP);
            // If the value has been retrieved successfully ...
            if (value != null && value.trim().length() != 0) {
                // ... store it for later use
                try {
                    accuracyReviewerRoleId = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    // don't do anything, keep the default
                }
            }

            // Retrieve the value of the property that contains the failure_reviewer_role_id
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, FAILURE_REVIEWER_ROLE_ID_NAME_PROP);
            // If the value has been retrieved successfully ...
            if (value != null && value.trim().length() != 0) {
                // ... store it for later use
                try {
                    failureReviewerRoleId = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    // don't do anything, keep the default
                }
            }

            // Retrieve the value of the property that contains the stress_reviewer_role_id
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, STRESS_REVIEWER_ROLE_ID_NAME_PROP);
            // If the value has been retrieved successfully ...
            if (value != null && value.trim().length() != 0) {
                // ... store it for later use
                try {
                    stressReviewerRoleId = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    // don't do anything, keep the default
                }
            }


            // Retrieve the value of the property that contains the primary_screener_role_id
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, PRIMARY_SCREENER_ROLE_ID_NAME_PROP);
            // If the value has been retrieved successfully ...
            if (value != null && value.trim().length() != 0) {
                // ... store it for later use
                try {
                    primaryScreenerRoleId = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    // don't do anything, keep the default
                }
            }

            // Retrieve the value of the property that contains the aggregator_role_id
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, AGGREGATOR_ROLE_ID_NAME_PROP);
            // If the value has been retrieved successfully ...
            if (value != null && value.trim().length() != 0) {
                // ... store it for later use
                try {
                    aggregatorRoleId = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    // don't do anything, keep the default
                }
            }

            // Retrieve the value of the property that contains the final_reviewer_role_id
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, FINAL_REVIEWER_ROLE_ID_NAME_PROP);
            // If the value has been retrieved successfully ...
            if (value != null && value.trim().length() != 0) {
                // ... store it for later use
                try {
                    finalReviewerRoleId = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    // don't do anything, keep the default
                }
            }

            // Retrieve the value of the property that contains the project details page base URL
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, PROP_PROJECT_DETAILS_URL);
            if (value != null && value.trim().length() != 0) {
                projectDetailsBaseURL = value;
            }

            // Retrieve the value of the property that contains the direct project page base URL
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, PROP_DIRECT_PROJECT_URL);
            if (value != null && value.trim().length() != 0) {
                directProjectBaseURL = value;
            }

            // Retrieve the value of the property that contains the pacts payment detail page base URL
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, PACTS_PAYMENT_DETAIL_URL_PROP);
            if (value != null && value.trim().length() != 0) {
                pactsPaymentDetailBaseURL = value;
            }

            // Retrieve the value of the default distribution script
            defaultDistributionScript = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, DEFAULT_DISTRIBUTION_SCRIPT_PROP);
            if (defaultDistributionScript == null || defaultDistributionScript.trim().length() == 0) {
                System.err.println("The value of " + DEFAULT_DISTRIBUTION_SCRIPT_PROP
                    + " configuration property is null. "
                    + "This value will be ignored and value of " + DEFAULT_DISTRIBUTION_SCRIPT
                    + " will be used instead");

                defaultDistributionScript = DEFAULT_DISTRIBUTION_SCRIPT;
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

                // Retrieve small icon's filename that should be associated with the ID
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

                if (propRootCatIcons.containsProperty(strPropName + "." + ROOT_CATALOG_DISTRIBUTION_SCRIPT_KEY_PROP)) {
                    String script = propRootCatIcons.getValue(strPropName + "."
                        + ROOT_CATALOG_DISTRIBUTION_SCRIPT_KEY_PROP);

                    distributionScriptRootCatalogs.put(strID, script);
                } else {

                    // Use the default distribution script
                    distributionScriptRootCatalogs.put(strID, defaultDistributionScript);
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

            // Retrieve property that contains definitions of Project Type view contest links
            Property propProjTypeDesc = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, PROJECT_TYPE_VIEW_CONTEST_LINKS_PROP);
            // Prepare to enumerate all the nested properties
            Enumeration propsLinks = propProjTypeDesc.propertyNames();

            while (propsLinks.hasMoreElements()) {
                // Get the name of the next property in the list.
                // The property name retrieved is also the name of a Project Type
                String strPropName = (String) propsLinks.nextElement();
                // Retrieve link to view contest
                String strLink = propProjTypeDesc.getValue(strPropName);

                // If the link has been read fine ...
                if (strLink != null && strLink.trim().length() != 0) {
                    // ... store it into the appropriate map for later use
                    projectTypeViewContestLinks.put(strPropName, strLink);
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

            // Retrieve property that contains deliverable types definitions
            Property propDeliverableType = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, DELIVERABLE_TYPES_PROP);
            // Prepare to enumerate all the nested properties
            Enumeration propDeliverableTypes = propDeliverableType.propertyNames();

            while (propDeliverableTypes.hasMoreElements()) {
                String strDeliverableTypeName = (String) propDeliverableTypes.nextElement();
                String strDeliverableIds = propDeliverableType.getValue(strDeliverableTypeName);
                if (strDeliverableIds != null && strDeliverableIds.trim().length() != 0) {
                    deliverableTypes.put(strDeliverableTypeName, strDeliverableIds);
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
            if (reqReviewersStr != null && reqReviewersStr.trim().length() != 0) {
                int minimum = Integer.parseInt(reqReviewersStr, 10);
                if (minimum >= 0) {
                    reqReviewers = minimum;
                }
            }
            // Parse the number of required reviewers for Post-Mortem phase
            String postMortemReviewersStr
                = cfgMgr.getPropertyObject("com.cronos.onlinereview.phases.PostMortemPhaseHandler",
                                           "PostMortemPhaseDefaultReviewersNumber").getValue();
            if (postMortemReviewersStr != null && postMortemReviewersStr.trim().length() != 0) {
                int minimum = Integer.parseInt(postMortemReviewersStr, 10);
                if (minimum >= 0) {
                    reqPostMortemReviewers = minimum;
                }
            }
            // Parse the number of required reviewers for Approval phase
            String approversStr
                = cfgMgr.getPropertyObject("com.cronos.onlinereview.phases.ApprovalPhaseHandler",
                                           "ApprovalPhaseDefaultReviewersNumber").getValue();
            if (approversStr != null && approversStr.trim().length() != 0) {
                int minimum = Integer.parseInt(approversStr, 10);
                if (minimum >= 0) {
                    reqApprovers = minimum;
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
                // Retrieve the names of roles which that permission is granted to
                String[] roles = propPermissionsMatrix.getValues(permissionName);

                // If everything has been read fine ...
                if (roles != null && roles.length != 0) {
                    // ... store the Permission name/list of names of Resource Roles for later use
                    permissionsMatrix.put(permissionName, roles);
                }
            }

            // Retrieve property that contains definitions of phase groups
            Property propPhaseGrouping = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, PHASE_GROUPING_PROP);
            // Prepare to enumerate all group definition properties
            Enumeration phaseGroups = propPhaseGrouping.propertyNames();

            while (phaseGroups.hasMoreElements()) {
                // Get the name of the next property in the list.
                String propertyName = phaseGroups.nextElement() + ".";
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

                    Set<String> phasesSet = new HashSet<String>();
                    phaseGroupPhases.add(phasesSet);

                    Collections.addAll(phasesSet, strPhases);
                }
            }

            Property propContactManagerEmail =
                cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, CONTACT_MANAGER_EMAIL_PROP);

            if (propContactManagerEmail != null) {
                contactManagerEmailTemplate = propContactManagerEmail.getValue(EMAIL_TEMPLATE_NAME_PROP);
                contactManagerEmailFromAddress = propContactManagerEmail.getValue(EMAIL_FROM_ADDRESS_PROP);
            }

            // Get the configurable maximum values for extension days for registration and submission phases
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, REGISTRATION_PHASE_MAX_EXTENSION_PROP);
            if (value != null && value.trim().length() != 0) {
                try {
                    registrationPhaseMaxExtensionDays = new Integer(value);
                } catch (NumberFormatException nfe) {
                    System.err.println("The value of " + REGISTRATION_PHASE_MAX_EXTENSION_PROP
                                       + " configuration property is not numeric: " + value
                                       + ". This value will be ignored.");
                }
            }

            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, SUBMISSION_PHASE_MAX_EXTENSION_PROP);
            if (value != null && value.trim().length() != 0) {
                try {
                    submissionPhaseMaxExtensionDays = new Integer(value);
                } catch (NumberFormatException nfe) {
                    System.err.println("The value of " + SUBMISSION_PHASE_MAX_EXTENSION_PROP
                                       + " configuration property is not numeric: " + value
                                       + ". This value will be ignored.");
                }
            }

            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, MINIMUM_HOURS_BEFORE_SUBMISSION_DEADLINE_FOR_EXTENSION_PROP);
            if (value != null && value.trim().length() != 0) {
                try {
                    minimumHoursBeforeSubmissionDeadlineForExtension = new Integer(value);
                } catch (NumberFormatException nfe) {
                    System.err.println("The value of " + MINIMUM_HOURS_BEFORE_SUBMISSION_DEADLINE_FOR_EXTENSION_PROP
                                       + " configuration property is not numeric: " + value
                                       + ". This value will be ignored and value of " + DEFAULT_MINIMUM_HOURS_LEFT
                                       + " will be used instead");
                }
            }

            distributionToolOutputDir = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, DISTRIBUTION_TOOL_OUTPUT_DIR_PROP);

            if (distributionToolOutputDir == null || distributionToolOutputDir.trim().length() == 0) {
                System.err.println("The value of " + DISTRIBUTION_TOOL_OUTPUT_DIR_PROP
                    + " configuration property is null. "
                    + "This value will be ignored and value of " + DEFAULT_DISTRIBUTION_TOOL_OUTPUT_DIR
                    + " will be used instead");

                distributionToolOutputDir = DEFAULT_DISTRIBUTION_TOOL_OUTPUT_DIR;
            }

            catalogOutputDir = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, CATALOG_OUTPUT_DIR_PROP);

            if (catalogOutputDir == null || catalogOutputDir.trim().length() == 0) {
                System.err.println("The value of " + CATALOG_OUTPUT_DIR_PROP
                    + " configuration property is null. "
                    + "This value will be ignored and value of " + DEFAULT_CATALOG_OUTPUT_DIR
                    + " will be used instead");

                catalogOutputDir = DEFAULT_CATALOG_OUTPUT_DIR;
            }

            Property disabledResourceRolesConfig
                = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, DISABLED_RESOURCE_ROLES_PROP);
            disabledResourceRoles = disabledResourceRolesConfig.getValues();

            Property svnPermissionGrantResourceRolesConfig
                = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, SVN_PERM_GRANT_RESOURCE_ROLES_PROP);
            svnPermissionGrantResourceRoles = svnPermissionGrantResourceRolesConfig.getValues();

            Property svnRepoConfig = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, SVN_CONFIG_PROP);
            svnConfig = new String[] {svnRepoConfig.getValue("Root"),
                                      svnRepoConfig.getValue("AuthUsername"),
                                      svnRepoConfig.getValue("AuthPassword"),
                                      svnRepoConfig.getValue("MkDirCommitMessage"),
                                      svnRepoConfig.getValue("TempFilesBaseDir"),
                                      svnRepoConfig.getValue("PathBasedPermissionsFileURL")};

            Property lateDeliverableEmailConfig
                = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, "LateDeliverableUpdateNotificationEmail");

            lateDeliverablesUpdatedByManagerNotificationConfig = new String[] {
                lateDeliverableEmailConfig.getValue("ByManager.EmailTemplateName"),
                lateDeliverableEmailConfig.getValue("ByManager.EmailFromAddress"),
                lateDeliverableEmailConfig.getValue("ByManager.EmailSubject"),
                lateDeliverableEmailConfig.getValue("ByManager.Roles")};

            lateDeliverablesUpdatedByMemberNotificationConfig = new String[] {
                lateDeliverableEmailConfig.getValue("ByMember.EmailTemplateName"),
                lateDeliverableEmailConfig.getValue("ByMember.EmailFromAddress"),
                lateDeliverableEmailConfig.getValue("ByMember.EmailSubject"),
                lateDeliverableEmailConfig.getValue("ByMember.Roles")};

            lateDeliverableBaseURL = lateDeliverableEmailConfig.getValue("EditLateDeliverablePageBaseURL");

            Property f2fSubmissionReuploadedConfig
                    = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, "F2FSubmissionReuploadNotificationEmail");

            f2fSubmissionReuploadedNotificationConfig = new String[] {
                    f2fSubmissionReuploadedConfig.getValue("EmailTemplateName"),
                    f2fSubmissionReuploadedConfig.getValue("EmailFromAddress"),
                    f2fSubmissionReuploadedConfig.getValue("EmailSubject")};

            // Retrieve the property that contains the definitions of resource tabs to be displayed in Resource section
            Property propResourceTabs = cfgMgr.getPropertyObject(ONLINE_REVIEW_CFG_NS, RESOURCE_TABS_PROP_STRING);
            // Prepare to enumerate all the nested properties
            Enumeration<String> propsResourceTab = propResourceTabs.propertyNames();
            while (propsResourceTab.hasMoreElements()) {
                // Get the name of the next property in the list
                String strPropName = propsResourceTab.nextElement();

                String[] resourceIds = propResourceTabs.getValues(strPropName);
                if (resourceIds != null && resourceIds.length > 0) {
                    resourceTabs.put(strPropName, new HashSet<String>(Arrays.asList(resourceIds)));
                }
            }

            // Read the admin users property
            String adminUsersProperty = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, ADMIN_USERS_PROP);
            if (adminUsersProperty != null && adminUsersProperty.trim().length() != 0) {
                String[] adminUserIDs = adminUsersProperty.split(",");
                for(String adminUserID : adminUserIDs) {
                    try {
                        adminUsers.add(Long.parseLong(adminUserID.trim()));
                    } catch (NumberFormatException nfe) {
                        // don't do anything
                    }
                }
            }

            // Get the configuration for Thurgood
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, THURGOOD_API_URL_PROP);
            if (value != null && value.trim().length() != 0) {
                thurgoodApiURL = value;
            }
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, THURGOOD_API_KEY_PROP);
            if (value != null && value.trim().length() != 0) {
                thurgoodApiKey = value;
            }
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, THURGOOD_TIMEOUT_PROP);
            if (value != null && value.trim().length() != 0) {
                try {
                    thurgoodTimeout = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    // don't do anything, keep the default
                }
            }
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, THURGOOD_CODE_URL_PROP);
            if (value != null && value.trim().length() != 0) {
                thurgoodCodeURL = value;
            }
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, THURGOOD_JOB_BASE_UI_URL_PROP);
            if (value != null && value.trim().length() != 0) {
                thurgoodJobBaseUIURL = value;
            }
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, THURGOOD_USERNAME_PROP);
            if (value != null && value.trim().length() != 0) {
                thurgoodUsername = value;
            }
            value = cfgMgr.getString(ONLINE_REVIEW_CFG_NS, THURGOOD_PASSWORD_PROP);
            if (value != null && value.trim().length() != 0) {
                thurgoodPassword = value;
            }

        } catch (UnknownNamespaceException une) {
            System.out.println(une.getMessage());
            une.printStackTrace();
        }
    }

    /**
     * This static method returns the submitter role id.
     *
     * @return the submitter role id.
     */
    public static int getSubmitterRoleId() {
        return submitterRoleId;
    }

    /**
     * This static method returns the reviewer role id.
     *
     * @return the reviewer role id.
     */
    public static int getReviewerRoleId() {
        return reviewerRoleId;
    }

    /**
     * This static method returns the accuracy reviewer role id.
     *
     * @return the accuracy reviewer role id.
     */
    public static int getAccuracyReviewerRoleId() {
        return accuracyReviewerRoleId;
    }

    /**
     * This static method returns the failure reviewer role id.
     *
     * @return the failure reviewer role id.
     */
    public static int getFailureReviewerRoleId() {
        return failureReviewerRoleId;
    }

    /**
     * This static method returns the stress reviewer role id.
     *
     * @return the stress reviewer role id.
     */
    public static int getStressReviewerRoleId() {
        return stressReviewerRoleId;
    }

    /**
     * This static method returns the primary screener role id.
     *
     * @return the primary screener role id.
     */
    public static int getPrimaryScreenerRoleId() {
        return primaryScreenerRoleId;
    }

    /**
     * This static method returns the aggregator role id.
     *
     * @return the aggregator role id.
     */
    public static int getAggregatorRoleId() {
        return aggregatorRoleId;
    }

    /**
     * This static method returns the final reviewer role id.
     *
     * @return the final reviewer role id.
     */
    public static int getFinalReviewerRoleId() {
        return finalReviewerRoleId;
    }

    /**
     * This static method returns the OR project detail page base URL.
     *
     * @return the OR project detail page base URL.
     */
    public static String getProjectDetailsBaseURL() {
        return projectDetailsBaseURL;
    }

    /**
     * This static method returns the base URL for the direct project page.
     *
     * @return the base URL for the direct project page.
     */
    public static String getDirectProjectBaseURL() {
        return directProjectBaseURL;
    }

    /**
     * This static method returns the base URL for the pacts payment detail page.
     *
     * @return the base URL for the pacts payment detail page.
     */
    public static String getPactsPaymentDetailBaseURL() {
        return pactsPaymentDetailBaseURL;
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
        return rootCatalogIconsSm.get(rootCatalogId);
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
        return rootCatalogAltTextKeys.get(rootCatalogId);
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
     * This static method returns the distribution script for a catalog.
     *
     * @return the distribution script for a catalog.
     * @param rootCatalogId
     *            Root Catalog ID which to look for.
     */
    public static String getDistributionScript(String rootCatalogId) {
        return distributionScriptRootCatalogs.get(rootCatalogId);
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
        return projectCategoryIconsSm.get(projectCategoryName);
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
        return projectCategoryIcons.get(projectCategoryName);
    }

    /**
     * This static method returns the link to the view contest page for project based on the type of
     * project passed as parameter.
     *
     * @return the link to view contest page of the project.
     * @param projectTypeName
     *            Project Type name which link to view contest page should be looked up for.
     * @param projectId
     *            ID of the project (numeric value) that should be substituted instead of
     *            &quot;<code>&lt;PROJECT_ID&gt;</code>&quot; substring in the template link read
     *            from the configuration. If this value is zero or negative, the aforementioned
     *            substring will be simply removed from the template link.
     */
    public static String getProjectTypeViewContestLink(String projectTypeName, long projectId) {
        String templateLink = projectTypeViewContestLinks.get(projectTypeName);

        templateLink = templateLink.replaceFirst("\\<PROJECT_ID\\>", (projectId > 0) ? String.valueOf(projectId) : "");
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
     *            ID of the forum (numeric value) that should be substituted instead of
     *            &quot;<code>&lt;FORUM_ID&gt;</code>&quot; substring in the template link read from
     *            the configuration. If this value is zero or negative, the aforementioned substring
     *            will be simply removed from the template link.
     */
    public static String getProjectTypeForumLink(String projectTypeName, long forumId) {
        String templateLink = projectTypeForumLinks.get(projectTypeName);

        templateLink = templateLink.replaceFirst("\\<FORUM_ID\\>", (forumId > 0) ? String.valueOf(forumId) : "");
        return templateLink;
    }

    /**
     * This static method returns the amount of pixels that should be displayed for each hour in
     * the project's GANTT chart.
     *
     * @return a value that represent the amount of pixels per each hour.
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
     * This static method returns default minimum of registered reviewers required before Review
     * phase can be closed.
     *
     * @return default minimum amount, or -1 if there is no default minimum value.
     */
    public static int getDefaultRequiredReviewers() {
        return reqReviewers;
    }

    /**
     * This static method returns default minimum of registered approvers required before Approval
     * phase can be closed.
     *
     * @return default minimum amount, or -1 if there is no default minimum value.
     */
    public static int getDefaultRequiredApprovers() {
        return reqApprovers;
    }

    /**
     * This static method returns default minimum of registered reviewers required before Post-Mortem
     * phase can be closed.
     *
     * @return default minimum amount, or -1 if there is no default minimum value.
     */
    public static int getDefaultRequiredPostMortemReviewers() {
        return reqPostMortemReviewers;
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
        String[] roles = permissionsMatrix.get(permissionName);
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
            if (phaseGroupPhases.get(i).contains(phaseName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * This static method returns the name of a key for a phase group referenced by its index.
     *
     * @return the name of a key. This key can later be used to retrieve the localized name of phase
     *         group from message resources.
     * @param index
     *            an index of a phase group to retrieve the name of a key for.
     */
    public static String getPhaseGroupNameKey(int index) {
        return phaseGroupNames.get(index);
    }

    /**
     * This static method returns the name of a key for a phase group referenced by its index.
     *
     * @return the name of a key. This key can later be used to retrieve the localized name of table
     *         for some phase group from message resources.
     * @param index
     *            an index of a phase group to retrieve the name of a key for.
     */
    public static String getPhaseGroupTableNameKey(int index) {
        return phaseGroupTableNames.get(index);
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
     *            a name of the phase which presence in the phase group is to be tested.
     */
    public static boolean isPhaseGroupContainsPhase(int index, String phaseName) {
        return phaseGroupPhases.get(index).contains(phaseName);
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
        return phaseGroupFunctions.get(index);
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
     * This static method returns the "from" address for the messages to project's manager.
     *
     * @return a string containing the "from" address.
     */
    public static String getContactManagerEmailFromAddress() {
        return contactManagerEmailFromAddress;
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

    /**
     * <p>Gets the maximum allowed number of days to extend <code>Registration</code> phase for.</p>
     *
     * @return an <code>Integer</code> providing the maximum allowed number of days to extend <code>Registration</code>
     *         phase or <code>null</code> if there is no such limit specified.
     */
    public static Integer getRegistrationPhaseMaxExtensionDays() {
        return registrationPhaseMaxExtensionDays;
    }

    /**
     * <p>Gets the maximum allowed number of days to extend <code>Submission</code> phase for.</p>
     *
     * @return an <code>Integer</code> providing the maximum allowed number of days to extend <code>Submission</code>
     *         phase or <code>null</code> if there is no such limit specified.
     */
    public static Integer getSubmissionPhaseMaxExtensionDays() {
        return submissionPhaseMaxExtensionDays;
    }

    /**
     * <p>Gets the maximum allowed number of days to extend <code>Submission</code> phase for.</p>
     *
     * @return an <code>Integer</code> providing the maximum allowed number of days to extend <code>Submission</code>
     *         phase or <code>null</code> if there is no such limit specified.
     */
    public static Integer getMinimumHoursBeforeSubmissionDeadlineForExtension() {
        return minimumHoursBeforeSubmissionDeadlineForExtension;
    }

    /**
     * <p>Gets the distribution tool output dir.</p>
     *
     * @return the distribution tool output dir.
     */
    public static String getDistributionToolOutputDir() {
        return distributionToolOutputDir;
    }

    /**
     * <p>Gets the TopCoder Catalog output dir.</p>
     *
     * @return the TopCoder Catalog output dir.
     */
    public static String getCatalogOutputDir() {
        return catalogOutputDir;
    }

    /**
     * <p>Gets the Distribution Tool default script.</p>
     *
     * @return the Distribution Tool default script.
     */
    public static String getDefaultDistributionScript() {
        return defaultDistributionScript;
    }

    /**
     * <p>Gets the list of disabled resource role IDs.</p>
     *
     * @return a <code>String</code> array listing the IDs for resource roles which are not allowed for selection.
     */
    public static String[] getDisabledResourceRoles() {
        return disabledResourceRoles;
    }

    /**
     * <p>Gets the list of resource role IDs which are to be granted permission for accessing SVN module for project
     * once the project enters the <code>Final Review</code> phase.</p>
     *
     * @return a <code>String</code> array listing the IDs for resource roles which are to be granted SVN permission.
     */
    public static String[] getSvnPermissionGrantResourceRoles() {
        return svnPermissionGrantResourceRoles;
    }

    /**
     * <p>Gets the URL for SVN repository.</p>
     *
     * @return a <code>String</code> providing the URL for SVN repository.
     */
    public static String getSVNRoot() {
        return svnConfig[0];
    }

    /**
     * <p>Gets the username for authentication to SVN repository.</p>
     *
     * @return a <code>String</code> providing the username to be used for authenticating to SVN repository.
     */
    public static String getSVNAuthnUsername() {
        return svnConfig[1];
    }

    /**
     * <p>Gets the password for authentication to SVN repository.</p>
     *
     * @return a <code>String</code> providing the password to be used for authenticating to SVN repository.
     */
    public static String getSVNAuthnPassword() {
        return svnConfig[2];
    }

    /**
     * <p>Gets the message for committing the new directories to SVN repository.</p>
     *
     * @return a <code>String</code> providing message for committing the new directories to SVN repository.
     */
    public static String getSVNCommitMessage() {
        return svnConfig[3];
    }

    /**
     * <p>Gets the path to local directory where the SVN files can be temporarily checked to.</p>
     *
     * @return a <code>String</code> providing the path to local directory where the SVN files can be temporarily
     *         checked to.
     */
    public static String getSVNTemporaryFilesBaseDir() {
        return svnConfig[4];
    }

    /**
     * <p>Gets the URL for path-based permissions file in SVN repository.</p>
     *
     * @return a <code>String</code> providing the URL for path-based permissions file in SVN repository.
     */
    public static String getSVNPathBasedPermissionsFileURL() {
        return svnConfig[5];
    }

    /**
     * <p>Gets the mapping from deliverable type name to deliverable ids.</p>
     *
     * @return a map of mapping from deliverable type name to deliverable ids.
     */
    public static Map<String, String> getDeliverableTypes() {
        return deliverableTypes;
    }

    /**
     * <p>Gets the name for template for email to be sent to intended recipients when late deliverable is updated by
     * manager.</p>
     *
     * @return a <code>String</code> referencing the email template.
     */
    public static String getLateDeliverableUpdateByManagerEmailTemplateName() {
        return lateDeliverablesUpdatedByManagerNotificationConfig[0];
    }

    /**
     * <p>Gets the FROM address for email message to be sent to intended recipients when late deliverable is updated by
     * manager.</p>
     *
     * @return a <code>String</code> providing the FROM address for email message.
     */
    public static String getLateDeliverableUpdateByManagerEmailFromAddress() {
        return lateDeliverablesUpdatedByManagerNotificationConfig[1];
    }

    /**
     * <p>Gets the subject for email message to be sent to intended recipients when late deliverable is updated by
     * manager.</p>
     *
     * @return a <code>String</code> providing the subject for email message.
     */
    public static String getLateDeliverableUpdateByManagerEmailTemplateSubject() {
        return lateDeliverablesUpdatedByManagerNotificationConfig[2];
    }

    /**
     * <p>Gets the list of names for resource roles to be notified when late deliverable is updated by manager.</p>
     *
     * @return a <code>String</code> providing the list of names for resource roles to be notified when late deliverable
     *         is updated by manager or <code>null</code> if such a list is not set.
     */
    public static String[] getLateDeliverableUpdateByManagerRecipientRoleNames() {
        if ((lateDeliverablesUpdatedByManagerNotificationConfig[3] != null)
            && (lateDeliverablesUpdatedByManagerNotificationConfig[3].trim().length() > 0)) {
            return lateDeliverablesUpdatedByManagerNotificationConfig[3].split(",");
        } else {
            return null;
        }
    }

    /**
     * <p>Gets the base URL for <code>Edit Late Deliverable</code> page.</p>
     *
     * @return a <code>String</code> providing the base URL for <code>Edit Late Deliverable</code> page.
     */
    public static String getLateDeliverableBaseURL() {
        return lateDeliverableBaseURL;
    }

    /**
     * <p>Gets the name for template for email to be sent to intended recipients when late deliverable is updated by
     * member.</p>
     *
     * @return a <code>String</code> referencing the email template.
     */
    public static String getLateDeliverableUpdateByMemberEmailTemplateName() {
        return lateDeliverablesUpdatedByMemberNotificationConfig[0];
    }

    /**
     * <p>Gets the FROM address for email message to be sent to intended recipients when late deliverable is updated by
     * member.</p>
     *
     * @return a <code>String</code> providing the FROM address for email message.
     */
    public static String getLateDeliverableUpdateByMemberEmailFromAddress() {
        return lateDeliverablesUpdatedByMemberNotificationConfig[1];
    }

    /**
     * <p>Gets the subject for email message to be sent to intended recipients when late deliverable is updated by
     * member.</p>
     *
     * @return a <code>String</code> providing the subject for email message.
     */
    public static String getLateDeliverableUpdateByMemberEmailTemplateSubject() {
        return lateDeliverablesUpdatedByMemberNotificationConfig[2];
    }

    /**
     * <p>Gets the list of names for resource roles to be notified when late deliverable is updated by member.</p>
     *
     * @return a <code>String</code> providing the list of names for resource roles to be notified when late deliverable
     *         is updated by member or <code>null</code> if such a list is not set.
     */
    public static String[] getLateDeliverableUpdateByMemberRecipientRoleNames() {
        if ((lateDeliverablesUpdatedByMemberNotificationConfig[3] != null)
            && (lateDeliverablesUpdatedByMemberNotificationConfig[3].trim().length() > 0)) {
            return lateDeliverablesUpdatedByMemberNotificationConfig[3].split(",");
        } else {
            return null;
        }
    }

    /**
     * <p>Gets the name for template for email to be sent to iterative reviewers when a member reuploads submission
     * for an F2F project.</p>
     *
     * @return a <code>String</code> referencing the email template.
     */
    public static String getF2FSubmissionReuploadedEmailTemplateName() {
        return f2fSubmissionReuploadedNotificationConfig[0];
    }

    /**
     * <p>Gets the FROM address for email message to be sent to iterative reviewers when a member reuploads submission
     * for an F2F project.</p>
     *
     * @return a <code>String</code> providing the FROM address for email message.
     */
    public static String getF2FSubmissionReuploadedEmailFromAddress() {
        return f2fSubmissionReuploadedNotificationConfig[1];
    }

    /**
     * <p>Gets the subject for email message to be sent to iterative reviewers when a member reuploads submission
     * for an F2F project.</p>
     *
     * @return a <code>String</code> providing the subject for email message.
     */
    public static String getF2FSubmissionReuploadedEmailTemplateSubject() {
        return f2fSubmissionReuploadedNotificationConfig[2];
    }

    /**
     * Gets the Resources tabs to be displayed in the Resource section in project detail page.
     *
     * @return the Resources tabs to be displayed in the Resource section in project detail page.
     */
    public static Map<String, Set<String>> getResourceTabs() {
        return resourceTabs;
    }

    /**
     * Gets the list of the admin user IDs.
     *
     * @return the list of the admin user IDs.
     */
    public static List<Long> getAdminUsers() {
        return adminUsers;
    }

    /**
     * Getter of the thurgood API URL.
     * @return the thurgood API URL
     */
    public static String getThurgoodApiURL() {
        return thurgoodApiURL;
    }

    /**
     * Getter of thurgood API key.
     * @return the thurgood API key
     */
    public static String getThurgoodApiKey() {
        return thurgoodApiKey;
    }

    /**
     * Getter of thurgood timeout.
     * @return the thurgood timeout
     */
    public static int getThurgoodTimeout() {
        return thurgoodTimeout;
    }

    /**
     * Getter of thurgood code URL.
     * @return the thurgood code URL
     */
    public static String getThurgoodCodeURL() {
        return thurgoodCodeURL;
    }

    /**
     * Getter of thurgood job base UI URL.
     * @return the thurgood job base UI URL
     */
    public static String getThurgoodJobBaseUIURL() {
        return thurgoodJobBaseUIURL;
    }

    /**
     * Getter of Thurgood user username.
     * @return the Thurgood user username.
     */
    public static String getThurgoodUsername() {
        return thurgoodUsername;
    }

    /**
     * Getter of Thurgood user password.
     * @return the Thurgood user password.
     */
    public static String getThurgoodPassword() {
        return thurgoodPassword;
    }

}
