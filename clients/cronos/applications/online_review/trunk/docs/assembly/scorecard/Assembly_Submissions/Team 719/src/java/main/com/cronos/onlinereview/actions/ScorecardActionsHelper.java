/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.QuestionType;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * This helper class provides various util methods used in this web application.
 * One of the major features of this class is to provide the lookup service for
 * the relative-constant stuff: scorecard statuses, scorecard types, question
 * types, project types and project categories. The Ajax Support service URL and
 * the name of the session attribute in which the user id is stored are also
 * provided in this class.
 * </p>
 * 
 * @version 1.0
 * @author albertwang, flying2hk
 */
public final class ScorecardActionsHelper {
    /**
     * Configuration property key for user id session attribute key.
     */
    private static final String KEY_USER_ID_SESSION_ATTRIBUTE = "userIdSessionAttributeKey";

    /**
     * Singleton instance.
     */
    private static ScorecardActionsHelper instance = null;


    /**
     * User ID session attribute key.
     */
    private String userIdSessionAttributeKey;

    /**
     * Project types.
     */
    private ProjectType[] projectTypes;

    /**
     * Project categories.
     */
    private ProjectCategory[] projectCategories;

    /**
     * Scorecard types.
     */
    private ScorecardType[] scorecardTypes;

    /**
     * Scorecard statuses.
     */
    private ScorecardStatus[] scorecardStatuses;

    /**
     * Question types.
     */
    private QuestionType[] questionTypes;

    /**
     * Question type names.
     */
    private String[] questionTypeNames;

    /**
     * Scorecard type names.
     */
    private String[] scorecardTypeNames;

    /**
     * Scorecard status names.
     */
    private String[] scorecardStatusNames;

    /**
     * Project category names.
     */
    private String[] projectCategoryNames;

    /**
     * Project type names.
     */
    private String[] projectTypeNames;

    /**
     * <p>
     * Private constructor.
     * </p>
     */
    private ScorecardActionsHelper() {
        ConfigManager cm = ConfigManager.getInstance();
        String namespace = getClass().getName();
        try {

            // User Id session attribute key
            this.userIdSessionAttributeKey = cm.getString(namespace, KEY_USER_ID_SESSION_ATTRIBUTE);

            // FIXED by flying2hk : 
            // fetch the data from corresponding management components instead of configuration
            ProjectManager projectManager = new ProjectManagerImpl();

            // project types
            this.projectTypes = projectManager.getAllProjectTypes();
            this.projectTypeNames = new String[this.projectTypes.length];
            for (int i = 0; i < this.projectTypes.length; i++) {
                this.projectTypeNames[i] = this.projectTypes[i].getName();
            }

            // project categories
            this.projectCategories = projectManager.getAllProjectCategories();
            this.projectCategoryNames = new String[this.projectCategories.length];
            Set set = new HashSet();
            for (int i = 0; i < this.projectCategories.length; i++) {
                if (!set.contains(projectCategories[i].getName())) {
                    set.add(projectCategories[i].getName());
                }
            }
            this.projectCategoryNames = new String[set.size()];
            set.toArray(this.projectCategoryNames);

            ScorecardManager scorecardManager = new ScorecardManagerImpl();

            // scorecard types
            this.scorecardTypes = scorecardManager.getAllScorecardTypes();
            this.scorecardTypeNames = new String[this.scorecardTypes.length];
            for (int i = 0; i < this.scorecardTypes.length; i++) {
                this.scorecardTypeNames[i] = this.scorecardTypes[i].getName();
            }

            // scorecard statuses
            List list = new ArrayList();
            ScorecardStatus[] statuses = scorecardManager.getAllScorecardStatuses();
            // we don't need "Deleted" status
            for (int i = 0; i < statuses.length; i++) {
                if (!statuses[i].getName().equals("Deleted")) {
                    list.add(statuses[i]);
                }
            }
            this.scorecardStatuses = new ScorecardStatus[list.size()];
            list.toArray(this.scorecardStatuses);
            this.scorecardStatusNames = new String[this.scorecardStatuses.length];
            for (int i = 0; i < this.scorecardStatuses.length; i++) {
                this.scorecardStatusNames[i] = this.scorecardStatuses[i].getName();
            }

            // question types
            this.questionTypes = scorecardManager.getAllQuestionTypes();
            this.questionTypeNames = new String[this.questionTypes.length];
            for (int i = 0; i < this.questionTypes.length; i++) {
                this.questionTypeNames[i] = this.questionTypes[i].getName();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * <p>
     * Return the singleton instance.
     * </p>
     * 
     * @return the singleton instance
     */
    public static synchronized ScorecardActionsHelper getInstance() {
        if (instance == null) {
            instance = new ScorecardActionsHelper();
        }
        return instance;
    }


    /**
     * <p>
     * Return the project type.
     * </p>
     * 
     * @param projectTypeId
     *            project type id
     * @return the project type
     */
    public ProjectType getProjectType(long projectTypeId) {
        for (int i = 0; i < this.projectTypes.length; i++) {
            if (this.projectTypes[i].getId() == projectTypeId) {
                return new ProjectType(this.projectTypes[i].getId(), this.projectTypes[i].getName());
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the project type.
     * </p>
     * 
     * @param projectTypeName
     *            project type name
     * @return the project type
     */
    public ProjectType getProjectType(String projectTypeName) {
        for (int i = 0; i < this.projectTypes.length; i++) {
            if (this.projectTypes[i].getName().equals(projectTypeName)) {
                return new ProjectType(this.projectTypes[i].getId(), this.projectTypes[i].getName());
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the project category.
     * </p>
     * 
     * @param projectCategoryId
     *            project category id
     * @return the project category
     */
    public ProjectCategory getProjectCategory(long projectCategoryId) {
        for (int i = 0; i < this.projectCategories.length; i++) {
            if (this.projectCategories[i].getId() == projectCategoryId) {
                ProjectCategory category = new ProjectCategory(this.projectCategories[i].getId(),
                        this.projectCategories[i].getName(), getProjectType(this.projectCategories[i].getProjectType()
                                .getId()));
                return category;
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the project category.
     * </p>
     * 
     * @param projectCategoryName
     *            project category name
     * @param projectTypeId
     *            project type id
     * @return the project category
     */
    public ProjectCategory getProjectCategory(String projectCategoryName, long projectTypeId) {
        for (int i = 0; i < this.projectCategories.length; i++) {
            if (this.projectCategories[i].getName().equals(projectCategoryName)
                    && this.projectCategories[i].getProjectType().getId() == projectTypeId) {
                ProjectCategory category = new ProjectCategory(this.projectCategories[i].getId(),
                        this.projectCategories[i].getName(), getProjectType(this.projectCategories[i].getProjectType()
                                .getId()));
                return category;
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the project category.
     * </p>
     * 
     * @param projectCategoryName
     *            project category name
     * @param projectTypeName
     *            project type name
     * @return the project category
     */
    public ProjectCategory getProjectCategory(String projectCategoryName, String projectTypeName) {
        for (int i = 0; i < this.projectCategories.length; i++) {
            if (this.projectCategories[i].getName().equals(projectCategoryName)
                    && this.projectCategories[i].getProjectType().getName().equals(projectTypeName)) {
                ProjectCategory category = new ProjectCategory(this.projectCategories[i].getId(),
                        this.projectCategories[i].getName(), getProjectType(this.projectCategories[i].getProjectType()
                                .getId()));
                return category;
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the scorecard type.
     * </p>
     * 
     * @param scorecardTypeId
     *            scorecard type id
     * @return the scorecard type
     */
    public ScorecardType getScorecardType(long scorecardTypeId) {
        for (int i = 0; i < this.scorecardTypes.length; i++) {
            if (this.scorecardTypes[i].getId() == scorecardTypeId) {
                return new ScorecardType(this.scorecardTypes[i].getId(), this.scorecardTypes[i].getName());
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the scorecard type.
     * </p>
     * 
     * @param scorecardTypeName
     *            scorecard type name
     * @return the scorecard type
     */
    public ScorecardType getScorecardType(String scorecardTypeName) {
        for (int i = 0; i < this.scorecardTypes.length; i++) {
            if (this.scorecardTypes[i].getName().equals(scorecardTypeName)) {
                return new ScorecardType(this.scorecardTypes[i].getId(), this.scorecardTypes[i].getName());
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the scorecard status.
     * </p>
     * 
     * @param scorecardStatusId
     *            scorecard status id
     * @return the scorecard status
     */
    public ScorecardStatus getScorecardStatus(long scorecardStatusId) {
        for (int i = 0; i < this.scorecardStatuses.length; i++) {
            if (this.scorecardStatuses[i].getId() == scorecardStatusId) {
                return new ScorecardStatus(this.scorecardStatuses[i].getId(), this.scorecardStatuses[i].getName());
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the scorecard status.
     * </p>
     * 
     * @param scorecardStatusName
     *            scorecard status name
     * @return the scorecard status
     */
    public ScorecardStatus getScorecardStatus(String scorecardStatusName) {
        for (int i = 0; i < this.scorecardStatuses.length; i++) {
            if (this.scorecardStatuses[i].getName().equals(scorecardStatusName)) {
                return new ScorecardStatus(this.scorecardStatuses[i].getId(), this.scorecardStatuses[i].getName());

            }
        }
        return null;
    }

    /**
     * <p>
     * Return the question type.
     * </p>
     * 
     * @param questionTypeId
     *            question type id
     * @return the question type
     */
    public QuestionType getQuestionType(long questionTypeId) {
        for (int i = 0; i < this.questionTypes.length; i++) {
            if (this.questionTypes[i].getId() == questionTypeId) {
                return new QuestionType(this.questionTypes[i].getId(), this.questionTypes[i].getName());
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the question type.
     * </p>
     * 
     * @param questionTypeName
     *            question type name
     * @return the question type
     */
    public QuestionType getQuestionType(String questionTypeName) {
        for (int i = 0; i < this.questionTypes.length; i++) {
            if (this.questionTypes[i].getName().equals(questionTypeName)) {
                return new QuestionType(this.questionTypes[i].getId(), this.questionTypes[i].getName());
            }
        }
        return null;
    }

    /**
     * <p>
     * Return the project categories with the project type id.
     * </p>
     * 
     * @param projectTypeId
     *            project type id
     * @return the project categories
     */
    public ProjectCategory[] getProjectCategories(long projectTypeId) {
        List list = new ArrayList();
        for (int i = 0; i < this.projectCategories.length; i++) {
            if (this.projectCategories[i].getProjectType().getId() == projectTypeId) {
                ProjectCategory category = new ProjectCategory(this.projectCategories[i].getId(),
                        this.projectCategories[i].getName(), getProjectType(this.projectCategories[i].getProjectType()
                                .getId()));
                list.add(category);
            }
        }
        ProjectCategory[] categories = new ProjectCategory[list.size()];
        list.toArray(categories);
        return categories;
    }

    /**
     * <p>
     * Return the project categories with the project type name.
     * </p>
     * 
     * @param projectTypeName
     *            project type name
     * @return the project categories
     */
    public ProjectCategory[] getProjectCategories(String projectTypeName) {
        List list = new ArrayList();
        for (int i = 0; i < this.projectCategories.length; i++) {
            if (this.projectCategories[i].getProjectType().getName().equals(projectTypeName)) {
                ProjectCategory category = new ProjectCategory(this.projectCategories[i].getId(),
                        this.projectCategories[i].getName(), getProjectType(this.projectCategories[i].getProjectType()
                                .getId()));
                list.add(category);
            }
        }
        ProjectCategory[] categories = new ProjectCategory[list.size()];
        list.toArray(categories);
        return categories;
    }

    /**
     * <p>
     * Return all project categories.
     * </p>
     * 
     * @return all project categories
     */
    public ProjectCategory[] getProjectCategories() {
        ProjectCategory[] categories = new ProjectCategory[this.projectCategories.length];
        for (int i = 0; i < this.projectCategories.length; i++) {
            categories[i] = new ProjectCategory(this.projectCategories[i].getId(), this.projectCategories[i].getName(),
                    new ProjectType(this.projectCategories[i].getProjectType().getId(), this.projectCategories[i]
                            .getProjectType().getName()));
        }
        return categories;
    }

    /**
     * <p>
     * Return all project types.
     * </p>
     * 
     * @return all project types
     */
    public ProjectType[] getProjectTypes() {
        ProjectType[] types = new ProjectType[this.projectTypes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = new ProjectType(this.projectTypes[i].getId(), this.projectTypes[i].getName());
        }
        return types;
    }

    /**
     * <p>
     * Return all scorecard types.
     * </p>
     * 
     * @return all scorecard types
     */
    public ScorecardType[] getScorecardTypes() {
        ScorecardType[] types = new ScorecardType[this.scorecardTypes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = new ScorecardType(this.scorecardTypes[i].getId(), this.scorecardTypes[i].getName());
        }
        return types;
    }

    /**
     * <p>
     * Return all scorecard statuses.
     * </p>
     * 
     * @return all scorecard statuses
     */
    public ScorecardStatus[] getScorecardStatuses() {
        ScorecardStatus[] statuses = new ScorecardStatus[this.scorecardStatuses.length];
        for (int i = 0; i < statuses.length; i++) {
            statuses[i] = new ScorecardStatus(this.scorecardStatuses[i].getId(), this.scorecardStatuses[i].getName());
        }
        return statuses;
    }

    /**
     * <p>
     * Return all question types.
     * </p>
     * 
     * @return all question types
     */
    public QuestionType[] getQuestionTypes() {
        QuestionType[] types = new QuestionType[this.questionTypes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = new QuestionType(this.questionTypes[i].getId(), this.questionTypes[i].getName());
        }
        return types;
    }

    /**
     * <p>
     * Return all project category names.
     * </p>
     * 
     * @return all project category names
     */
    public String[] getProjectCategoryNames() {
        return projectCategoryNames;
    }

    /**
     * <p>
     * Return all project type names.
     * </p>
     * 
     * @return all project type names
     */
    public String[] getProjectTypeNames() {
        return projectTypeNames;
    }

    /**
     * <p>
     * Return all question type names.
     * </p>
     * 
     * @return all question type names
     */
    public String[] getQuestionTypeNames() {
        return questionTypeNames;
    }

    /**
     * <p>
     * Return all scorecard status names.
     * </p>
     * 
     * @return all scorecard status names
     */
    public String[] getScorecardStatusNames() {
        return scorecardStatusNames;
    }

    /**
     * <p>
     * Return all scorecard type names.
     * </p>
     * 
     * @return all scorecard type names
     */
    public String[] getScorecardTypeNames() {
        return scorecardTypeNames;
    }

    /**
     * <p>
     * Build a new question.
     * </p>
     * 
     * @return the new question
     */
    public static Question buildNewQuestion() {
        return new QuestionAdapter();
    }

    /**
     * <p>
     * Build a question from given question, this method adapts the
     * <code>Question</code> to <code>QuestionAdapter</code>
     * </p>
     * 
     * @param question
     *            the question
     * @return the QuestionAdapter created from the question
     */
    public static Question buildQuestion(Question question) {
        return new QuestionAdapter(question);
    }

    /**
     * <p>
     * Build a new section with one question.
     * </p>
     * 
     * @return the new section
     */
    public static Section buildNewSection() {
        SectionAdapter section = new SectionAdapter();
        section.setName("Section name goes here.");
        section.setWeight(100);
        section.addQuestion(buildNewQuestion());
        section.setCount(section.getNumberOfQuestions());
        return section;
    }

    /**
     * <p>
     * Build a new group with one section and one question.
     * </p>
     * 
     * @return the new group
     */
    public static Group buildNewGroup() {
        GroupAdapter group = new GroupAdapter();
        group.setName("Group name goes here.");
        group.setWeight(100);
        group.addSection(buildNewSection());
        group.setCount(group.getNumberOfSections());
        return group;
    }

    /**
     * <p>
     * Build a new scorecard with one group, one section and one question.
     * </p>
     * 
     * @return the new scorecard
     */
    public static Scorecard buildNewScorecard() {
        ScorecardAdapter scorecard = new ScorecardAdapter();
        scorecard.setInUse(false);
        scorecard.setMaxScore(100);
        scorecard.setMinScore(0);
        scorecard.setName("Scorecard name goes here.");
        scorecard.setCategory(1);
        scorecard.setScorecardStatus(ScorecardActionsHelper.getInstance().getScorecardStatus("Inactive"));
        scorecard.setScorecardType(ScorecardActionsHelper.getInstance().getScorecardType(1));
        scorecard.setVersion("1.0");
        scorecard.addGroup(buildNewGroup());
        scorecard.setCount(scorecard.getNumberOfGroups());
        return scorecard;
    }

    /**
     * <p>
     * Copy a scorecard, the newly created scorecard and all
     * groups/sections/questions included are all with SENTINEL_ID, the major
     * version is increased.
     * </p>
     * 
     * @param scorecard
     *            the scorecard to copy
     * @return the newly created scorecard copied from the given scorecard
     */
    public static Scorecard copyScorecard(Scorecard scorecard) {
        ScorecardAdapter sa = new ScorecardAdapter(scorecard);
        // reset id and inUse
        sa.resetId();
        sa.setInUse(false);
        Group[] groups = sa.getAllGroups();
        for (int i = 0; i < groups.length; i++) {
            // reset group id
            groups[i].resetId();
            Section[] sections = groups[i].getAllSections();
            for (int j = 0; j < sections.length; j++) {
                // reset section id
                sections[j].resetId();
                Question[] questions = sections[j].getAllQuestions();
                for (int k = 0; k < questions.length; k++) {
                    // reset question id
                    questions[k].resetId();
                }
            }
        }
        // increase the major version
        String version = sa.getVersion();
        int dotIdx = version.indexOf(".");
        long major;
        if (dotIdx == -1) {
            major = Long.parseLong(version) + 1;
            sa.setVersion(major + "");
        } else {
            major = Long.parseLong(version.substring(0, dotIdx)) + 1;
            sa.setVersion(major + version.substring(dotIdx));
        }
        return sa;
    }

    /**
     * <p>
     * Generate the project categories JavaScript array. 
     * </p>
     * @return the project categories JavaScript array. 
     */
    public String generateProjectCategoriesJSArray() {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < this.projectTypes.length; i++) {
            sb.append("[");
            ProjectCategory[] categories = this.getProjectCategories(projectTypes[i].getId());
            for (int j = 0; j < categories.length; j++) {
                sb.append("\"" + categories[j].getName() + "\"");
                if (j < categories.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            if (i < this.projectTypes.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * <p>
     * Convert the given string to HTML string.
     * </p>
     * @param string the string
     * @return the HTML representation
     */
    public static String escapeToHTMLString(String string) {
        StringBuffer sb = new StringBuffer(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;

        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss 
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"')
                    sb.append("&quot;");
                else if (c == '&')
                    sb.append("&amp;");
                else if (c == '<')
                    sb.append("&lt;");
                else if (c == '>')
                    sb.append("&gt;");
                else if (c == '\n')
                    // Handle Newline
                    sb.append("<br/>");
                else {
                    int ci = 0xffff & c;
                    if (ci < 160)
                        // nothing special only 7 Bit
                        sb.append(c);
                    else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * <p>
     * Return the key of the session attribute in which the user id is stored.
     * </p>
     * 
     * @return
     */
    public String getUserIdSessionAttributeKey() {
        return userIdSessionAttributeKey;
    }
   
}
