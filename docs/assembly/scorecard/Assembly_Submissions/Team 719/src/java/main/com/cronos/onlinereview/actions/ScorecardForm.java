/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.scorecard.ConfigurationException;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
import com.topcoder.management.scorecard.ScorecardSearchBundle;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.QuestionType;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.management.scorecard.data.WeightedScorecardStructure;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * <code>ScorecardForm</code> is the <code>ActionForm</code> for the
 * scorecard actions.
 * </p>
 * <p>
 * Actually it is used in all actions except "list scorecards" action.
 * </p>
 * 
 * @version 1.0
 * @author albertwang, flying2hk
 */
public class ScorecardForm extends ActionForm {
    /** Regular expression for scorecard version. */
    private static final String VERSION_REGEX = "^\\d+.\\d+$";

    /** Epsilon used in float number comparison. */
    private static final float EPS = 1e-9f;

    /**
     * The scorecard with the form.
     */
    private Scorecard scorecard;

    /**
     * Project category name with the scorecard.
     */
    private String projectCategoryName;

    /**
     * Project type name with the scorecard.
     */
    private String projectTypeName;

    /**
     * Text representation of the minimum score with the scorecard.
     */
    private String minScoreText;

    /**
     * Text representation of the maximum score with the scorecard.
     */
    private String maxScoreText;

    /**
     * Old version before editing.
     */
    private String oldVersion;

    /**
     * If the scorecard name editable.
     */
    private boolean scorecardNameEditable;

    /**
     * If the scorecard version editable.
     */
    private boolean scorecardVersionEditable;

    /**
     * If the scorecard a newly created one.
     */
    private boolean newlyCreated;

    /**
     * If the scorecard is copied from another scorecard.
     */
    private boolean copy;

    /**
     * <p>
     * Validate the action form.
     * </p>
     * 
     * @param mapping
     *            the action mapping
     * @param request
     *            the request
     * @return the form validation errors
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        // retrieve the value of parameter(type of action)
        String parameterValue = request.getParameter(mapping.getParameter());
        // we only perform validation when the action is "saveScorecard" and the
        // operation is "doFinish"
        if (Constants.ACTION_SAVE_SCORECARD.equals(parameterValue)) {
            ActionErrors errors = new ActionErrors();
            ScorecardActionsHelper helper = ScorecardActionsHelper.getInstance();
            // 1. scorecard name is not empty and the length is less than 255
            if (!this.checkRequiredText(this.scorecard.getName())) {
                errors.add("scorecard", new ActionMessage("editScorecard.error.scorecard_name.required"));
            } else if (!this.checkTextLength(this.scorecard.getName(), Constants.NAME_MAXLENGTH)) {
                errors.add("scorecard", new ActionMessage("editScorecard.error.scorecard_name.length",
                        new Object[] { new Integer(Constants.NAME_MAXLENGTH) }));
            }
            // 2. scorecard version is valid
            boolean versionValid = this.checkVersion(this.scorecard.getVersion(), errors);

            // check if the (name, version) combination is unqiue if we're
            // creating a new scorecard
            if (versionValid && this.isNewlyCreated()) {
                try {
                    ScorecardManager mgr = new ScorecardManagerImpl();
                    Filter filter = ScorecardSearchBundle.buildAndFilter(ScorecardSearchBundle
                            .buildNameEqualFilter(scorecard.getName()), ScorecardSearchBundle
                            .buildVersionEqualFilter(scorecard.getVersion()));
                    Scorecard[] result = mgr.searchScorecards(filter, false);
                    if (result.length > 0) {
                        errors.add("scorecard", new ActionMessage(
                                "editScorecard.error.scorecard_name_version.not_unique", new Object[] {
                                        scorecard.getName(), scorecard.getVersion() }));
                    }
                } catch (ConfigurationException e) {
                    // ignore
                } catch (PersistenceException e) {
                    // ignore
                }
            }
            if (versionValid && this.isCopy()) {
                // minor version cannot be modified for copy
                if (!this.getMinorVersion(this.scorecard.getVersion()).equals(this.getMinorVersion(this.oldVersion))) {
                    errors.add("scorecard", new ActionMessage(
                            "editScorecard.error.scorecard_version.cannot_modify_minor", new Object[] { this
                                    .getMinorVersion(this.oldVersion) }));
                }
            }
            // 3. project category/project type combination is valid
            ProjectCategory category = helper.getProjectCategory(this.projectCategoryName, this.projectTypeName);
            if (category == null) {
                errors.add("scorecard", new ActionMessage("editScorecard.error.project_category.invalid"));
            } else {
                // set the category id
                this.scorecard.setCategory(category.getId());
            }
            // 4. scorecard status is valid
            ScorecardStatus status = helper.getScorecardStatus(this.scorecard.getScorecardStatus().getName());
            if (status == null) {
                errors.add("scorecard", new ActionMessage("editScorecard.error.scorecard_status.invalid"));
            } else {
                // set the scorecard status
                this.scorecard.setScorecardStatus(status);
            }
            // 5. scorecard type is valid
            ScorecardType type = helper.getScorecardType(this.scorecard.getScorecardType().getName());
            if (type == null) {
                errors.add("scorecard", new ActionMessage("editScorecard.error.scorecard_type.invalid"));
            } else {
                // set the scorecard type
                this.scorecard.setScorecardType(type);
            }
            // 6. 0 <= minScore <= maxScore
            float minScore = -2;
            float maxScore = -1;
            int counter = 0;
            try {
                minScore = Float.parseFloat(this.minScoreText);
            } catch (NumberFormatException nfe) {
                counter++;
                errors.add("scorecard", new ActionMessage("editScorecard.error.min_score.malformed"));
            }
            try {
                maxScore = Float.parseFloat(this.maxScoreText);
            } catch (NumberFormatException nfe) {
                counter++;
                errors.add("scorecard", new ActionMessage("editScorecard.error.max_score.malformed"));
            }
            if (counter == 0) {
                if (minScore < -EPS) {
                    errors.add("scorecard", new ActionMessage("editScorecard.error.min_score.negative"));
                } else if (maxScore < minScore + EPS) {
                    errors.add("scorecard", new ActionMessage("editScorecard.error.min_score.larger_than_max_score"));
                } else {
                    try {
                        this.scorecard.setMinScore(minScore);
                        this.scorecard.setMaxScore(maxScore);
                    } catch (IllegalStateException ise) {
                        // IllegalStateException may be thrown
                        // e.g.
                        // Previous minScore = 0
                        // Previous maxScore = 100
                        // New minScore = 150
                        // New maxScore = 250
                        this.scorecard.setMaxScore(maxScore);
                        this.scorecard.setMinScore(minScore);
                    }
                }
            }
            // clear dirty groups
            int gCount = ((ScorecardAdapter) this.scorecard).getCount();
            while (this.scorecard.getNumberOfGroups() > gCount) {
                this.scorecard.removeGroup(gCount);
            }
            // 7. weights of groups sum to 100
            if (!this.checkWeights(this.scorecard.getAllGroups())) {
                errors.add("scorecard", new ActionMessage("editScorecard.error.scorecard.weight"));
            }
            // 8. Check groups
            Group[] groups = this.scorecard.getAllGroups();
            for (int i = 0; i < groups.length; i++) {
                Group group = groups[i];
                // clear dirty sections
                int sCount = ((GroupAdapter) group).getCount();
                while (group.getNumberOfSections() > sCount) {
                    group.removeSection(sCount);
                }
                // 8.1 group name is not empty
                if (!this.checkRequiredText(group.getName())) {
                    errors.add("scorecard.allGroups[" + i + "]", new ActionMessage(
                            "editScorecard.error.group_name.required"));
                }
                if (!this.checkTextLength(group.getName(), Constants.NAME_MAXLENGTH)) {
                    errors.add("scorecard.allGroups[" + i + "]", new ActionMessage(
                            "editScorecard.error.group_name.length", new Object[] { new Integer(
                                    Constants.NAME_MAXLENGTH) }));
                }
                // 8.2 0 < weight <= 100
                if (!this.checkWeight(group.getWeight())) {
                    errors.add("scorecard.allGroups[" + i + "]", new ActionMessage(
                            "editScorecard.error.group_weight.out_of_range"));
                }
                // 8.3 weights of sections sum to 100
                if (!this.checkWeights(group.getAllSections())) {
                    errors.add("scorecard.allGroups[" + i + "]", new ActionMessage("editScorecard.error.group.weight"));
                }
                // 8.4 Check sections
                Section[] sections = group.getAllSections();
                for (int j = 0; j < sections.length; j++) {
                    Section section = sections[j];
                    // clear dirty questions
                    int qCount = ((SectionAdapter) section).getCount();
                    while (section.getNumberOfQuestions() > qCount) {
                        section.removeQuestion(qCount);
                    }
                    // 8.4.1 section name is not empty
                    if (!this.checkRequiredText(section.getName())) {
                        errors.add("scorecard.allGroups[" + i + "].allSections[" + j + "]", new ActionMessage(
                                "editScorecard.error.section_name.required"));
                    }
                    if (!this.checkTextLength(section.getName(), Constants.NAME_MAXLENGTH)) {
                        errors.add("scorecard.allGroups[" + i + "].allSections[" + j + "]", new ActionMessage(
                                "editScorecard.error.section_name.length", new Object[] { new Integer(
                                        Constants.NAME_MAXLENGTH) }));
                    }
                    // 8.4.2 0 < weight <= 100
                    if (!this.checkWeight(section.getWeight())) {
                        errors.add("scorecard.allGroups[" + i + "].allSections[" + j + "]", new ActionMessage(
                                "editScorecard.error.section_weight.out_of_range"));
                    }
                    // 8.4.3 weights of questions sum to 100
                    if (!this.checkWeights(section.getAllQuestions())) {
                        errors.add("scorecard.allGroups[" + i + "].allSections[" + j + "]", new ActionMessage(
                                "editScorecard.error.section.weight"));
                    }
                    // 8.4.4 Check questions
                    Question[] questions = section.getAllQuestions();
                    for (int k = 0; k < questions.length; k++) {
                        QuestionAdapter question = (QuestionAdapter) questions[k];
                        // 8.4.4.1 question description is not empty
                        if (!this.checkRequiredText(question.getDescription())) {
                            errors.add("scorecard.allGroups[" + i + "].allSections[" + j + "].allQuestions[" + k + "]",
                                    new ActionMessage("editScorecard.error.question_description.required"));
                        }
                        if (!this.checkTextLength(question.getDescription(), Constants.DESCRIPTION_MAXLENGTH)) {
                            errors.add("scorecard.allGroups[" + i + "].allSections[" + j + "].allQuestions[" + k + "]",
                                    new ActionMessage("editScorecard.error.question_description.length",
                                            new Object[] { new Integer(Constants.DESCRIPTION_MAXLENGTH) }));
                        }
                        // FIXED by flying2hk : question guideline is OPTIONAL
                        // 8.4.4.2 question guideline is not empty
                        // if (!this.checkRequiredText(question.getGuideline()))
                        // {
                        // errors.add("scorecard.allGroups[" + i +
                        // "].allSections[" + j + "].allQuestions[" + k + "]",
                        // new
                        // ActionMessage("editScorecard.error.question_guideline.required"));
                        // }
                        if (!this.checkTextLength(question.getGuideline(), Constants.GUIDELINE_MAXLENGTH)) {
                            errors.add("scorecard.allGroups[" + i + "].allSections[" + j + "].allQuestions[" + k + "]",
                                    new ActionMessage("editScorecard.error.question_guideline.length",
                                            new Object[] { new Integer(Constants.GUIDELINE_MAXLENGTH) }));
                        }
                        // 8.4.4.3 0 < weight <= 100
                        if (!this.checkWeight(question.getWeight())) {
                            errors.add("scorecard.allGroups[" + i + "].allSections[" + j + "].allQuestions[" + k + "]",
                                    new ActionMessage("editScorecard.error.question_weight.out_of_range"));
                        }
                        // 8.4.4.4 question type
                        QuestionType questionType = helper.getQuestionType(question.getQuestionType().getName());
                        if (questionType == null) {
                            errors.add("scorecard.allGroups[" + i + "].allSections[" + j + "].allQuestions[" + k + "]",
                                    new ActionMessage("editScorecard.error.question_type.invalid"));
                        } else {
                            question.setQuestionType(questionType);
                        }
                    }
                }
            }
            return errors;
        } else {
            // no need to perform the validation
            return null;
        }
    }

    /**
     * <p>
     * Reset the action form.
     * </p>
     * 
     * @param mapping
     *            action mapping
     * @param request
     *            the request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    /**
     * <p>
     * Return the scorecard.
     * </p>
     * 
     * @return the scorecard
     */
    public Scorecard getScorecard() {
        return this.scorecard;
    }

    /**
     * <p>
     * Set the scorecard and update some facility fields of the form, and adapt
     * the questions of the given scorecard.
     * </p>
     * 
     * @param scorecard
     *            the scorecard
     */
    public void setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;
        // set facility data fields
        this.maxScoreText = this.scorecard.getMaxScore() + "";
        this.minScoreText = this.scorecard.getMinScore() + "";
        ProjectCategory category = ScorecardActionsHelper.getInstance()
                .getProjectCategory(this.scorecard.getCategory());
        this.projectTypeName = category.getProjectType().getName();
        this.projectCategoryName = category.getName();
        // adapt the questions
        Group[] groups = this.scorecard.getAllGroups();
        for (int i = 0; i < groups.length; i++) {
            Section[] sections = groups[i].getAllSections();
            for (int j = 0; j < sections.length; j++) {
                Question[] questions = sections[j].getAllQuestions();
                for (int k = 0; k < questions.length; k++) {
                    if (!(questions[k] instanceof QuestionAdapter)) {
                        sections[j].removeQuestion(k);
                        sections[j].insertQuestion(ScorecardActionsHelper.buildQuestion(questions[k]), k);
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Return if the scorecard is a newly created one.
     * </p>
     * 
     * @return if the scorecard is a newly created one
     */
    public boolean isNewlyCreated() {
        return newlyCreated;
    }

    /**
     * <p>
     * Set if the scorecard is a newly created one.
     * </p>
     * 
     * @param newlyCreated
     *            if the scorecard is a newly created one
     */
    public void setNewlyCreated(boolean newlyCreated) {
        this.newlyCreated = newlyCreated;
    }

    /**
     * <p>
     * Return if the scorecard name is editable.
     * </p>
     * 
     * @return if the scorecard name is editable
     */
    public boolean isScorecardNameEditable() {
        return scorecardNameEditable;
    }

    /**
     * <p>
     * Set if the scorecard name is editable.
     * </p>
     * 
     * @param scorecardNameEditable
     *            if the scorecard name is editable
     */
    public void setScorecardNameEditable(boolean scorecardNameEditable) {
        this.scorecardNameEditable = scorecardNameEditable;
    }

    /**
     * <p>
     * Return if the scorecard version is editable.
     * </p>
     * 
     * @return if the scorecard version is editable
     */
    public boolean isScorecardVersionEditable() {
        return scorecardVersionEditable;
    }

    /**
     * <p>
     * Set if the scorecard version is editable.
     * </p>
     * 
     * @param scorecardVersionEditable
     *            if the scorecard version is editable
     */
    public void setScorecardVersionEditable(boolean scorecardVersionEditable) {
        this.scorecardVersionEditable = scorecardVersionEditable;
    }

    /**
     * Check if the version string is illegal.
     * 
     * @param version
     *            the version string to check.
     */
    private boolean checkVersion(String version, ActionErrors errors) {
        if (version == null || version.trim().length() == 0) {
            errors.add("scorecard", new ActionMessage("editScorecard.error.scorecard_version.required"));
            return false;
        } else if (version.trim().length() > Constants.VERSION_MAXLENGTH) {
            errors.add("scorecard", new ActionMessage("editScorecard.error.scorecard_version.exceed_max_length"));
            return false;
        } else {
            if (!Pattern.matches(VERSION_REGEX, version)) {
                errors.add("scorecard", new ActionMessage("editScorecard.error.scorecard_version.malformed"));
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Return the minor component of a given version.
     * </p>
     * 
     * @param version
     *            the version
     * @return the minor component
     */
    private String getMinorVersion(String version) {
        int idx = version.lastIndexOf('.');
        return version.substring(idx + 1);
    }

    /**
     * <p>
     * Check if the weight is between 0 and 100.
     * </p>
     * 
     * @param weight
     *            the weight
     * @return if the weight is between 0 and 100.
     */
    private boolean checkWeight(float weight) {
        return (weight > EPS && weight <= 100 + EPS);
    }

    /**
     * <p>
     * Check if the text is not null and not empty.
     * </p>
     * 
     * @param text
     *            the text
     * @return if the text is not null and not empty
     */
    private boolean checkRequiredText(String text) {
        return (text != null && text.trim().length() > 0);
    }

    /**
     * <p>
     * Check if the length of the text is less than or equal to given value.
     * </p>
     * 
     * @param text
     *            the text
     * @param length
     *            the max length
     * @return if the length of the text is less than given value
     */
    private boolean checkTextLength(String text, int length) {
        return (text != null && text.trim().length() <= length);
    }

    /**
     * <p>
     * Check if the weights of given array of structures sum up to 100.
     * </p>
     * 
     * @param structures
     *            the structures
     * @return if the weights of given array of structures sum up to 100.
     */
    private boolean checkWeights(WeightedScorecardStructure[] structures) {
        float[] weights = new float[structures.length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = structures[i].getWeight();
        }
        return this.checkWeights(weights);
    }

    /**
     * <p>
     * Check if the array of weights sum up to 100.
     * </p>
     * 
     * @param weights
     *            the weights
     * @return if the array of weights sum up to 100.
     */
    private boolean checkWeights(float[] weights) {
        float sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
        }
        return (Math.abs(sum - 100) < EPS);
    }

    /**
     * <p>
     * Return if the scorecard is copied from another one.
     * </p>
     * 
     * @return if the scorecard is copied from another one.
     */
    public boolean isCopy() {
        return copy;
    }

    /**
     * <p>
     * Set if the scorecard is copied from another one.
     * </p>
     * 
     * @param copy
     *            if the scorecard is copied from another one.
     */
    public void setCopy(boolean copy) {
        this.copy = copy;
    }

    /**
     * <p>
     * Return the max score text.
     * </p>
     * 
     * @return the max score text
     */
    public String getMaxScoreText() {
        return maxScoreText;
    }

    /**
     * <p>
     * Set the max score text.
     * </p>
     * 
     * @param maxScoreText
     *            the max score text
     */
    public void setMaxScoreText(String maxScoreText) {
        this.maxScoreText = maxScoreText;
    }

    /**
     * <p>
     * Return the min score text.
     * </p>
     * 
     * @return the min score text
     */
    public String getMinScoreText() {
        return minScoreText;
    }

    /**
     * <p>
     * Set the min score text.
     * </p>
     * 
     * @param minScoreText
     *            the min score text
     */
    public void setMinScoreText(String minScoreText) {
        this.minScoreText = minScoreText;
    }

    /**
     * <p>
     * Return the project category name.
     * </p>
     * 
     * @return the project category name
     */
    public String getProjectCategoryName() {
        return projectCategoryName;
    }

    /**
     * <p>
     * Set the project category name.
     * </p>
     * 
     * @param projectCategoryName
     *            the project category name
     */
    public void setProjectCategoryName(String projectCategoryName) {
        this.projectCategoryName = projectCategoryName;
    }

    /**
     * <p>
     * Return the project type name.
     * </p>
     * 
     * @return the project type name
     */
    public String getProjectTypeName() {
        return projectTypeName;
    }

    /**
     * <p>
     * Set the project type name.
     * </p>
     * 
     * @param projectTypeName
     *            the project type name
     */
    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }
    /**
     * <p>
     * Return the old version before editing.
     * </p>
     * @return the old version
     */
    public String getOldVersion() {
        return oldVersion;
    }
    /**
     * <p>
     * Set the old version before editing.
     * </p>
     * @param oldVersion the old version
     */
    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }
}