/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

/**
 * <p>
 * This interface holds various constants used in the web application.
 * </p>
 * 
 * @version 1.0
 * @author albertwang, flying2hk
 */
public interface Constants {
    /** Maximum length allowed for scorecard version. */
    public static final int VERSION_MAXLENGTH = 16;
    /** Maximum length allowed for name. */
    public static final int NAME_MAXLENGTH = 64;
    /** Maximum length allowed for question guideline. */
    public static final int GUIDELINE_MAXLENGTH = 4096;
    /** Maximum length allowed for question description. */
    public static final int DESCRIPTION_MAXLENGTH = 4096;
    /**
     * Constant for action "viewScorecard".
     */
    public static final String ACTION_VIEW_SCORECARD = "viewScorecard";

    /**
     * Constant for action "editScorecard".
     */
    public static final String ACTION_EDIT_SCORECARD = "editScorecard";

    /**
     * Constant for action "saveScorecard".
     */
    public static final String ACTION_SAVE_SCORECARD = "saveScorecard";

    /**
     * Constant for action "listScorecards".
     */
    public static final String ACTION_LIST_SCORECARDS = "listScorecards";

    /**
     * Constant for action "copyScorecard".
     */
    public static final String ACTION_COPY_SCORECARD = "copyScorecard";

    /**
     * Constant for action "newScorecard".
     */
    public static final String ACTION_NEW_SCORECARD = "newScorecard";

    /**
     * Constant for forward key "failure".
     */
    public static final String FORWARD_KEY_FAILURE = "failure";

    /**
     * Constant for forward key "listScorecards".
     */
    public static final String FORWARD_KEY_LIST_SCORECARDS = "listScorecards";

    /**
     * Constant for forward key "viewScorecard".
     */
    public static final String FORWARD_KEY_VIEW_SCORECARD = "viewScorecard";

    /**
     * Constant for forward key "editScorecard".
     */
    public static final String FORWARD_KEY_EDIT_SCORECARD = "editScorecard";

    /**
     * Constant for global error "global.error.authorization".
     */
    public static final String ERROR_KEY_AUTHORIZATION = "global.error.authorization";

    /**
     * Constant for global error "global.error.general".
     */
    public static final String ERROR_KEY_GENERAL = "global.error.general";

    /**
     * Constant for request parameter "projectTypeId".
     */
    public static final String PARAM_KEY_PROJECT_TYPE_ID = "projectTypeId";

    /**
     * Constant for request parameter "sid".
     */
    public static final String PARAM_KEY_SCORECARD_ID = "sid";

    /**
     * Request attribute key "scorecardList", used in "listScorecards" action.
     */
    public static final String ATTR_KEY_SCORECARD_LIST = "scorecardList";
}
