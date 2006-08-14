/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.scorecard.ConfigurationException;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
import com.topcoder.management.scorecard.ScorecardSearchBundle;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.validation.ValidationException;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * <code>ScorecardActions</code> aggregates all actions related to scorecard
 * administration:
 * <ul>
 * <li> Create Scorecard("newScorecard"). </li>
 * <li> Edit Scorecard("editScorecard"). </li>
 * <li> Copy Scorecard("copyScorecard"). </li>
 * <li> Save Scorecard("saveScorecard"). </li>
 * <li> List Scorecards("listScorecards"). </li>
 * <li> View Scorecard("viewScorecard"). </li>
 * </ul>
 * </p>
 * 
 * @struts.action validate="true"
 * @version 1.0
 * @author TCSDEVELOPER
 */
public class ScorecardActions extends DispatchAction {
    /**
     * <p>
     * <code>Log</code> instance used to log the operation information.
     * </p>
     */
    private Log logger = LogFactory.getLog(getClass().getName());

    /**
     * <p>
     * <code>ScorecardActionsHelper</code> instance used in this class.
     * </p>
     */
    private ScorecardActionsHelper helper = ScorecardActionsHelper
            .getInstance();

    /**
     * <p>
     * Process "new scorecard" requests.
     * </p>
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request
     * @param request
     *            The servlet request we are processing
     * @param response
     *            The servlet response we are creating
     */
    public ActionForward newScorecard(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // extract the user id
        Long userId = getUserId(request);
        // potential error messages
        ActionMessages errors = new ActionMessages();
        // forward
        ActionForward forward = null;
        // verify if the user is authorized to edit the scorecards
        if (!this.isAuthorized(userId)) {
            // no authorized
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    Constants.ERROR_KEY_AUTHORIZATION));
            this.logger
                    .log(
                            Level.INFO,
                            "User with ID "
                                    + userId
                                    + " is not authorized to perform the newScorecard actions.");
        } else {
            this.logger.log(Level.INFO, "User with ID " + userId
                    + " is requesting to perform the newScorecard actions.");
            // ScorecardForm
            ScorecardForm scorecardForm = (ScorecardForm) form;
            // create a new scorecard bean and attach it to the ActionForm
            scorecardForm.setScorecard(ScorecardActionsHelper
                    .buildNewScorecard());
            ProjectCategory projectCategory = null;
            try {
                projectCategory = helper
                        .getProjectCategories(Long
                                .parseLong(request
                                        .getParameter(Constants.PARAM_KEY_PROJECT_TYPE_ID)))[0];
            } catch (Exception e) {
                // ignore
            }
            // set scorecard category if available
            if (projectCategory != null) {
                scorecardForm.getScorecard().setCategory(
                        projectCategory.getId());
            }
            // the scorecard is newly created one, name and version are both
            // editable
            scorecardForm.setNewlyCreated(true);
            scorecardForm.setCopy(false);
            scorecardForm.setScorecardNameEditable(true);
            scorecardForm.setScorecardVersionEditable(true);
        }

        if (errors.isEmpty()) {
            // no errors, forward to "editScorecard"
            forward = mapping.findForward(Constants.FORWARD_KEY_EDIT_SCORECARD);
        } else {
            // save the errors and forward to "failure"
            this.saveErrors(request, errors);
            forward = mapping.findForward(Constants.FORWARD_KEY_FAILURE);
        }

        return forward;
    }

    /**
     * <p>
     * Process "edit scorecard" requests.
     * </p>
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request
     * @param request
     *            The servlet request we are processing
     * @param response
     *            The servlet response we are creating
     */
    public ActionForward editScorecard(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // extract the user id
        Long userId = getUserId(request);
        // potential error messages
        ActionMessages errors = new ActionMessages();
        // forward
        ActionForward forward = null;
        // verify if the user is authorized to edit the scorecards
        if (!this.isAuthorized(userId)) {
            // no authorized
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    Constants.ERROR_KEY_AUTHORIZATION));
            this.logger
                    .log(
                            Level.INFO,
                            "User with ID "
                                    + userId
                                    + " is not authorized to perform the editScorecard actions.");
        } else {
            // ScorecardForm
            ScorecardForm scorecardForm = (ScorecardForm) form;
            // Scorecard
            Scorecard scorecard = null;
            // authorized, perform the view operation
            long scorecardId = Scorecard.SENTINEL_ID;
            try {
                scorecardId = Long.parseLong(request
                        .getParameter(Constants.PARAM_KEY_SCORECARD_ID));
            } catch (Exception e) {
                // ignore
            }
            try {
                if (scorecardId <= 0) {
                    this.logger
                            .log(
                                    Level.ERROR,
                                    "User with ID "
                                            + userId
                                            + " is issuing an invalid edit scorecard request with scorecard id "
                                            + scorecardId + ".");
                    errors
                            .add(ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage(
                                            "global.error.no_such_scorecard"));
                } else {
                    // get the scorecard
                    ScorecardManager mgr = new ScorecardManagerImpl();
                    scorecard = mgr.getScorecard(scorecardId);
                    // check if the scorecard can be edited
                    if (scorecard == null) {
                        this.logger
                                .log(
                                        Level.ERROR,
                                        "User with ID "
                                                + userId
                                                + " is requesting to edit a nonexist scorecard with id "
                                                + scorecardId + ".");
                        errors.add(ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage(
                                        "global.error.no_such_scorecard"));
                    } else if (scorecard.isInUse()) {
                        // scorecard is in use
                        errors
                                .add(
                                        ActionMessages.GLOBAL_MESSAGE,
                                        new ActionMessage(
                                                "editScorecard.error.scorecard_in_use"));
                        this.logger.log(Level.ERROR, "Scorecard with id "
                                + scorecardId
                                + " is in use and cannot be edited.");
                    } else if ("Active".equalsIgnoreCase(scorecard
                            .getScorecardStatus().getName())) {
                        // scorecard is active
                        errors
                                .add(
                                        ActionMessages.GLOBAL_MESSAGE,
                                        new ActionMessage(
                                                "editScorecard.error.scorecard_is_active"));
                        this.logger.log(Level.ERROR, "Scorecard with id "
                                + scorecardId
                                + " is active and cannot be edited.");
                    } else {
                        this.logger
                                .log(
                                        Level.INFO,
                                        "User with ID "
                                                + userId
                                                + " is requesting to edit the scorecard with id "
                                                + scorecardId + ".");
                        // attach scorecard to the scorecard form
                        scorecardForm.setScorecard(scorecard);
                        // the scorecard is not newly created one, name is not
                        // editable, version is not editable
                        scorecardForm.setNewlyCreated(false);
                        scorecardForm.setScorecardNameEditable(false);
                        scorecardForm.setScorecardVersionEditable(false);
                        scorecardForm.setCopy(false);
                    }
                }
            } catch (ConfigurationException e) {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        Constants.ERROR_KEY_GENERAL));
                this.logger.log(Level.ERROR,
                        "Exception caught while editing the scorecard: "
                                + e.getMessage());
            } catch (PersistenceException e) {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        Constants.ERROR_KEY_GENERAL));
                this.logger.log(Level.ERROR,
                        "Exception caught while editing the scorecard: "
                                + e.getMessage());
            }
        }

        if (errors.isEmpty()) {
            // no errors, forward to "editScorecard"
            forward = mapping.findForward(Constants.FORWARD_KEY_EDIT_SCORECARD);
        } else {
            // save the errors and forward to "failure"
            this.saveErrors(request, errors);
            forward = mapping.findForward(Constants.FORWARD_KEY_FAILURE);
        }

        return forward;
    }

    /**
     * <p>
     * Process the "copy scorecard" requests.
     * </p>
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request
     * @param request
     *            The servlet request we are processing
     * @param response
     *            The servlet response we are creating
     */
    public ActionForward copyScorecard(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // extract the user id
        Long userId = getUserId(request);
        // potential error messages
        ActionMessages errors = new ActionMessages();
        // forward
        ActionForward forward = null;
        // verify if the user is authorized to copy the scorecards
        if (!this.isAuthorized(userId)) {
            // no authorized
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    Constants.ERROR_KEY_AUTHORIZATION));
            this.logger
                    .log(
                            Level.INFO,
                            "User with ID "
                                    + userId
                                    + " is not authorized to perform the copyScorecard actions.");
        } else {
            // ScorecardForm
            ScorecardForm scorecardForm = (ScorecardForm) form;
            // Scorecard
            Scorecard scorecard = null;
            long scorecardId = Scorecard.SENTINEL_ID;
            try {
                scorecardId = Long.parseLong(request
                        .getParameter(Constants.PARAM_KEY_SCORECARD_ID));
            } catch (Exception e) {
                // ignore
            }
            try {
                if (scorecardId <= 0) {
                    this.logger
                            .log(
                                    Level.ERROR,
                                    "User with ID "
                                            + userId
                                            + " is issuing an invalid copy scorecard request with scorecard id "
                                            + scorecardId + ".");
                    errors
                            .add(ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage(
                                            "global.error.no_such_scorecard"));
                } else {
                    // retrieve the scorecard
                    ScorecardManager mgr = new ScorecardManagerImpl();
                    scorecard = mgr.getScorecard(scorecardId);
                    if (scorecard == null) {
                        this.logger
                                .log(
                                        Level.ERROR,
                                        "User with ID "
                                                + userId
                                                + " is requesting to copy a nonexist scorecard with id "
                                                + scorecardId + ".");
                        errors.add(ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage(
                                        "global.error.no_such_scorecard"));
                    } else {
                        // attach the scorecard to the ActionForm
                        scorecardForm.setScorecard(ScorecardActionsHelper
                                .copyScorecard(scorecard));
                        // the scorecard is newly created one, name and version
                        // are both editable
                        scorecardForm.setNewlyCreated(true);
                        scorecardForm.setCopy(true);
                        scorecardForm.setScorecardNameEditable(true);
                        scorecardForm.setScorecardVersionEditable(true);
                    }
                }
            } catch (NumberFormatException e) {
                this.logger.log(Level.ERROR,
                        "Exception caught while copying the scorecard: "
                                + e.getMessage());
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        Constants.ERROR_KEY_GENERAL));
            } catch (ConfigurationException e) {
                this.logger.log(Level.ERROR,
                        "Exception caught while copying the scorecard: "
                                + e.getMessage());
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        Constants.ERROR_KEY_GENERAL));
            } catch (PersistenceException e) {
                this.logger.log(Level.ERROR,
                        "Exception caught while copying the scorecard: "
                                + e.getMessage());
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        Constants.ERROR_KEY_GENERAL));
            }
        }
        if (errors.isEmpty()) {
            // no errors, forward to "editScorecard"
            forward = mapping.findForward(Constants.FORWARD_KEY_EDIT_SCORECARD);
        } else {
            // save the errors and forward to "failure"
            this.saveErrors(request, errors);
            forward = mapping.findForward(Constants.FORWARD_KEY_FAILURE);
        }

        return forward;
    }

    /**
     * <p>
     * Process the "save scorecard" requests.
     * </p>
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request
     * @param request
     *            The servlet request we are processing
     * @param response
     *            The servlet response we are creating
     */
    public ActionForward saveScorecard(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // extract the user id
        Long userId = getUserId(request);
        // potential error messages
        ActionMessages errors = new ActionMessages();
        // forward
        ActionForward forward = null;
        // verify if the user is authorized to list the scorecards
        if (!this.isAuthorized(userId)) {
            // no authorized
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    Constants.ERROR_KEY_AUTHORIZATION));
            this.logger
                    .log(
                            Level.INFO,
                            "User with ID "
                                    + userId
                                    + " is not authorized to perform the saveScorecard actions.");
        } else {
            // ScorecardForm
            ScorecardForm scorecardForm = (ScorecardForm) form;
            Scorecard scorecard = scorecardForm.getScorecard();
            // Operation name
            String operation = scorecardForm.getOperation();
            // group index, section index and question index
            int gIdx = scorecardForm.getGroupIndex();
            int sIdx = scorecardForm.getSectionIndex();
            int qIdx = scorecardForm.getQuestionIndex();

            if (operation.equals(Constants.DO_ADD_GROUP)) {
                // create a new group bean with one new section with one new
                // question, and
                // insert it into given position
                scorecard.insertGroup(ScorecardActionsHelper.buildNewGroup(),
                        gIdx + 1);
                this.logger.log(Level.INFO, "User with ID " + userId
                        + " is requesting to add a group at the position ("
                        + gIdx + ") to scorecard with id " + scorecard.getId()
                        + ".");
            } else if (operation.equals(Constants.DO_ADD_SECTION)) {
                // create a new section bean with one new question, and insert
                // it into given position
                scorecard.getGroup(gIdx).insertSection(
                        ScorecardActionsHelper.buildNewSection(), sIdx + 1);
                this.logger.log(Level.INFO, "User with ID " + userId
                        + " is requesting to add a section at the position ("
                        + gIdx + ", " + (sIdx + 1) + ") to scorecard with id "
                        + scorecard.getId() + ".");
            } else if (operation.equals(Constants.DO_ADD_QUESTION)) {
                // create a new question bean, and insert it into given position
                scorecard.getGroup(gIdx).getSection(sIdx).addQuestion(
                        ScorecardActionsHelper.buildNewQuestion());
                this.logger.log(Level.INFO, "User with ID " + userId
                        + " is requesting to add a question at the position ("
                        + gIdx + ", " + sIdx + ") to scorecard with id "
                        + scorecard.getId() + ".");
            } else if (operation.equals(Constants.DO_REMOVE_GROUP)) {
                // remove the given group
                scorecard.removeGroup(gIdx);
                this.logger.log(Level.INFO, "User with ID " + userId
                        + " is requesting to remove a group at the position ("
                        + gIdx + ") from scorecard with id "
                        + scorecard.getId() + ".");
            } else if (operation.equals(Constants.DO_REMOVE_SECTION)) {
                // remove the given section
                scorecard.getGroup(gIdx).removeSection(sIdx);
                this.logger
                        .log(
                                Level.INFO,
                                "User with ID "
                                        + userId
                                        + " is requesting to remove a section at the position ("
                                        + gIdx + ", " + sIdx
                                        + ") from scorecard with id "
                                        + scorecard.getId() + ".");
            } else if (operation.equals(Constants.DO_REMOVE_QUESTION)) {
                // remove the given question
                scorecard.getGroup(gIdx).getSection(sIdx).removeQuestion(qIdx);
                this.logger
                        .log(
                                Level.INFO,
                                "User with ID "
                                        + userId
                                        + " is requesting to remove a question at the position ("
                                        + gIdx + ", " + sIdx + ", " + qIdx
                                        + ") from scorecard with id "
                                        + scorecard.getId() + ".");
            } else if (operation.equals(Constants.DO_FINISH)) {
                try {
                    this.logger.log(Level.INFO, "User with ID " + userId
                            + " is requesting to save scorecard with id "
                            + scorecard.getId() + ".");
                    // search the scorecards
                    ScorecardManager mgr = new ScorecardManagerImpl();
                    // create or update the scorecard
                    if (scorecardForm.isNewlyCreated()) {
                        mgr.createScorecard(scorecard, userId.toString());
                        // forward to the "listScorecards"
                        forward = listScorecards(mapping, form, request,
                                response);
                    } else {
                        // retrieve the scorecard from persistence to check if
                        // we can update it
                        Scorecard sc = mgr.getScorecard(scorecard.getId());
                        if (sc.isInUse()) {
                            // scorecard is in use
                            errors
                                    .add(
                                            ActionMessages.GLOBAL_MESSAGE,
                                            new ActionMessage(
                                                    "editScorecard.error.scorecard_in_use"));
                            this.logger.log(Level.ERROR, "Scorecard with id "
                                    + scorecard.getId()
                                    + " is in use and cannot be updated.");
                        } else if ("Active".equalsIgnoreCase(sc
                                .getScorecardStatus().getName())) {
                            // scorecard is active
                            errors
                                    .add(
                                            ActionMessages.GLOBAL_MESSAGE,
                                            new ActionMessage(
                                                    "editScorecard.error.scorecard_is_active"));
                            this.logger.log(Level.ERROR, "Scorecard with id "
                                    + scorecard.getId()
                                    + " is active and cannot be updated.");
                        } else {
                            mgr.updateScorecard(scorecard, userId.toString());
                            // forward to the "listScorecards"
                            forward = listScorecards(mapping, form, request,
                                    response);
                        }
                    }
                } catch (ConfigurationException e) {
                    this.logger.log(Level.ERROR,
                            "Exception caught while saving the scorecard: "
                                    + e.getMessage());
                    errors.add(ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage(Constants.ERROR_KEY_GENERAL));
                } catch (PersistenceException e) {
                    this.logger.log(Level.ERROR,
                            "Exception caught while saving the scorecard: "
                                    + e.getMessage());
                    errors.add(ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage(Constants.ERROR_KEY_GENERAL));
                } catch (ValidationException e) {
                    this.logger.log(Level.ERROR,
                            "Exception caught while saving the scorecard: "
                                    + e.getMessage());
                    errors.add(ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage(Constants.ERROR_KEY_GENERAL));
                }
            }
        }
        if (forward == null) {
            if (errors.isEmpty()) {
                // no errors, forward to "editScorecard"
                forward = mapping
                        .findForward(Constants.FORWARD_KEY_EDIT_SCORECARD);
            } else {
                // save the errors and forward to "failure"
                this.saveErrors(request, errors);
                forward = mapping.findForward(Constants.FORWARD_KEY_FAILURE);
            }
        }

        return forward;
    }

    /**
     * <p>
     * Process the "list scorecards" requests.
     * </p>
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request
     * @param request
     *            The servlet request we are processing
     * @param response
     *            The servlet response we are creating
     */
    public ActionForward listScorecards(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // extract the user id
        Long userId = getUserId(request);
        // forward
        ActionForward forward = null;
        // potential error messages
        ActionMessages errors = new ActionMessages();
        // verify if the user is authorized to list the scorecards
        if (!isAuthorized(userId)) {
            // not authorized
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    Constants.ERROR_KEY_AUTHORIZATION));
            this.logger
                    .log(
                            Level.INFO,
                            "User with ID "
                                    + userId
                                    + " is not authorized to perform the listScorecards actions.");
        } else {
            // authorized, perform the listing operation
            long projectTypeId = 1;
            try {
                projectTypeId = Long.parseLong(request
                        .getParameter(Constants.PARAM_KEY_PROJECT_TYPE_ID));
            } catch (Exception e) {
                // ignore
            }

            this.logger
                    .log(
                            Level.INFO,
                            "User with ID "
                                    + userId
                                    + " is requesting to list the scorecards with project type id "
                                    + projectTypeId + ".");
            // look up categories
            ProjectCategory[] projectCategories = helper
                    .getProjectCategories(projectTypeId);

            ScorecardGroupBean[] scorecardGroups = new ScorecardGroupBean[projectCategories.length];
            try {
                // search the scorecards
                ScorecardManager mgr = new ScorecardManagerImpl();
                for (int i = 0; i < projectCategories.length; i++) {
                    Filter filter = ScorecardSearchBundle
                            .buildProjectCategoryIdEqualFilter(projectCategories[i]
                                    .getId());
                    scorecardGroups[i] = new ScorecardGroupBean();
                    scorecardGroups[i].setProjectCategory(projectCategories[i]);
                    scorecardGroups[i].setScorecards(mgr.searchScorecards(
                            filter, false));
                }
                // assemble a ScorecardList bean
                ScorecardListBean scorecardList = new ScorecardListBean();
                scorecardList.setScorecardGroups(scorecardGroups);
                scorecardList.setProjectTypeName(helper.getProjectType(
                        projectTypeId).getName());
                // save the scorecards to request as an attribute
                request.setAttribute(Constants.ATTR_KEY_SCORECARD_LIST,
                        scorecardList);
            } catch (ConfigurationException e) {
                this.logger.log(Level.ERROR,
                        "Exception caught while listing the scorecard: "
                                + e.getMessage());
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        Constants.ERROR_KEY_GENERAL));
            } catch (PersistenceException e) {
                this.logger.log(Level.ERROR,
                        "Exception caught while listing the scorecard: "
                                + e.getMessage());
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        Constants.ERROR_KEY_GENERAL));
            } catch (NullPointerException e) {
                this.logger.log(Level.ERROR,
                        "Exception caught while listing the scorecard: "
                                + e.getMessage());
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        Constants.ERROR_KEY_GENERAL));
            }
        }
        if (errors.isEmpty()) {
            // no errors, forward to "listScorecards"
            forward = mapping
                    .findForward(Constants.FORWARD_KEY_LIST_SCORECARDS);
        } else {
            // save the errors and forward to "failure"
            this.saveErrors(request, errors);
            forward = mapping.findForward(Constants.FORWARD_KEY_FAILURE);
        }

        return forward;
    }

    /**
     * <p>
     * Process the "view scorecard" requests.
     * </p>
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request
     * @param request
     *            The servlet request we are processing
     * @param response
     *            The servlet response we are creating
     */
    public ActionForward viewScorecard(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // extract the user id
        Long userId = getUserId(request);
        // forward
        ActionForward forward = null;
        // potential error messages
        ActionMessages errors = new ActionMessages();
        // ScorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) form;
        // verify if the user is authorized to view the scorecard
        if (!isAuthorized(userId)) {
            // not authorized
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    Constants.ERROR_KEY_AUTHORIZATION));
            this.logger
                    .log(
                            Level.INFO,
                            "User with ID "
                                    + userId
                                    + " is not authorized to perform the viewScorecard actions.");
        } else {
            // authorized, perform the view operation
            long scorecardId = Scorecard.SENTINEL_ID;
            try {
                scorecardId = Long.parseLong(request
                        .getParameter(Constants.PARAM_KEY_SCORECARD_ID));
            } catch (Exception e) {
                // ignore
            }

            try {
                if (scorecardId <= 0) {
                    this.logger
                            .log(
                                    Level.ERROR,
                                    "User with ID "
                                            + userId
                                            + " is issuing an invalid view scorecard request with scorecard id "
                                            + scorecardId + ".");
                    errors
                            .add(ActionMessages.GLOBAL_MESSAGE,
                                    new ActionMessage(
                                            "global.error.no_such_scorecard"));
                } else {
                    // retrieve the scorecard with given scorecard id
                    ScorecardManager mgr = new ScorecardManagerImpl();
                    Scorecard scorecard = mgr.getScorecard(scorecardId);
                    if (scorecard == null) {
                        this.logger
                                .log(
                                        Level.ERROR,
                                        "User with ID "
                                                + userId
                                                + " is requesting to view a nonexist scorecard with id "
                                                + scorecardId + ".");
                        errors.add(ActionMessages.GLOBAL_MESSAGE,
                                new ActionMessage(
                                        "global.error.no_such_scorecard"));
                    } else {
                        this.logger
                                .log(
                                        Level.INFO,
                                        "User with ID "
                                                + userId
                                                + " is requesting to view the scorecard with id "
                                                + scorecardId + ".");
                        // attach the scorecard to the ActionForm
                        scorecardForm.setScorecard(scorecard);
                    }
                }

            } catch (ConfigurationException e) {
                this.logger.log(Level.ERROR,
                        "Exception caught while viewing the scorecard: "
                                + e.getMessage());
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        Constants.ERROR_KEY_GENERAL));
            } catch (PersistenceException e) {
                this.logger.log(Level.ERROR,
                        "Exception caught while viewing the scorecard: "
                                + e.getMessage());
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        Constants.ERROR_KEY_GENERAL));
            }
        }
        if (errors.isEmpty()) {
            // no errors, forward to "viewScorecard"
            forward = mapping.findForward(Constants.FORWARD_KEY_VIEW_SCORECARD);
        } else {
            // save the errors and forward to "failure"
            this.saveErrors(request, errors);
            forward = mapping.findForward(Constants.FORWARD_KEY_FAILURE);
        }

        return forward;
    }

    /**
     * <p>
     * Check if the user with the user id is authorized to perform the scorecard
     * administration actions.
     * </p>
     * 
     * @param request
     *            the request
     * @return if the user is authorized to perform the scorecard administration
     *         actions
     */
    public boolean isAuthorized(Long userId) {
        // TODO Wishingbone said it's not necessary to perform authorization, so
        // here we leave a placeholder
        return userId != null;
    }

    /**
     * <p>
     * Return the user id of the user who issued the request.
     * </p>
     * 
     * @param request
     *            the request
     * @return the user id of the user who issued the request
     */
    public Long getUserId(HttpServletRequest request) {
        Object userId = request.getSession().getAttribute(
                helper.getUserIdSessionAttributeKey());
        if (userId == null || !(userId instanceof Long)) {
            this.logger.log(Level.ERROR, "User is not logged on.");
            return null;
        } else {
            return (Long) userId;
        }
    }
}