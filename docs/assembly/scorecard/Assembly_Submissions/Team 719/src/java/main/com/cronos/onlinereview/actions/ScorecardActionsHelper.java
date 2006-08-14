/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.QuestionType;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

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
 * @author TCSDEVELOPER
 */
public final class ScorecardActionsHelper {
    /**
     * Configuration property key for Ajax Support service URL.
     */
    private static final String KEY_AJAX_SUPPORT_APP_URL = "ajaxSupportAppUrl";

    /**
     * Configuration property key for user id session attribute key.
     */
    private static final String KEY_USER_ID_SESSION_ATTRIBUTE = "userIdSessionAttributeKey";

    /**
     * Configuration property key for project types.
     */
    private static final String KEY_PROJECT_TYPES = "projectTypes";

    /**
     * Configuration property key for project categories.
     */
    private static final String KEY_PROJECT_CATEGORIES = "projectCategories";

    /**
     * Configuration property key for scorecard statuses.
     */
    private static final String KEY_SCORECARD_STATUSES = "scorecardStatuses";

    /**
     * Configuration property key for scorecard types.
     */
    private static final String KEY_SCORECARD_TYPES = "scorecardTypes";

    /**
     * Configuration property key for question types.
     */
    private static final String KEY_QUESTION_TYPES = "questionTypes";

    /**
     * Configuration property key for object Key.
     */
    private static final String KEY_OBJECT_KEY = "objectKey";

    /**
     * Configuration property key for object identifiers.
     */
    private static final String KEY_OBJECT_IDENTIFIERS = "objectIdentifiers";

    /**
     * Configuration property key for object specification namespace.
     */
    private static final String KEY_OBJECT_SPECS_NAMESPACE = "objectSpecsNamespace";

    /**
     * Singleton instance.
     */
    private static ScorecardActionsHelper instance = null;

    /**
     * Ajax Support Service application URL.
     */
    private String ajaxSupportAppUrl;

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
            // ajax support app url
            this.ajaxSupportAppUrl = cm.getString(namespace,
                    KEY_AJAX_SUPPORT_APP_URL);

            // User Id session attribute key
            this.userIdSessionAttributeKey = cm.getString(namespace,
                    KEY_USER_ID_SESSION_ATTRIBUTE);

            String objectSpecsNamespace = cm.getString(namespace,
                    KEY_OBJECT_SPECS_NAMESPACE);
            Object[] objects;
            String objectKey;
            String[] objectIdentifiers;
            ObjectFactory objFactory = new ObjectFactory(
                    new ConfigManagerSpecificationFactory(objectSpecsNamespace),
                    ObjectFactory.SPECIFICATION_ONLY);

            // project types
            objectKey = cm.getString(namespace, KEY_PROJECT_TYPES + "."
                    + KEY_OBJECT_KEY);
            objectIdentifiers = cm.getStringArray(namespace, KEY_PROJECT_TYPES
                    + "." + KEY_OBJECT_IDENTIFIERS);
            objects = this.createObjects(objFactory, objectKey,
                    objectIdentifiers);
            this.projectTypes = new ProjectType[objects.length];
            System.arraycopy(objects, 0, this.projectTypes, 0, objects.length);
            this.projectTypeNames = new String[this.projectTypes.length];
            for (int i = 0; i < this.projectTypes.length; i++) {
                this.projectTypeNames[i] = this.projectTypes[i].getName();
            }

            // project categories
            objectKey = cm.getString(namespace, KEY_PROJECT_CATEGORIES + "."
                    + KEY_OBJECT_KEY);
            objectIdentifiers = cm.getStringArray(namespace,
                    KEY_PROJECT_CATEGORIES + "." + KEY_OBJECT_IDENTIFIERS);
            objects = this.createObjects(objFactory, objectKey,
                    objectIdentifiers);
            this.projectCategories = new ProjectCategory[objects.length];
            System.arraycopy(objects, 0, this.projectCategories, 0,
                    objects.length);
            this.projectCategoryNames = new String[this.projectCategories.length];
            Set set = new HashSet();
            for (int i = 0; i < this.projectCategories.length; i++) {
                if (!set.contains(projectCategories[i].getName())) {
                    set.add(projectCategories[i].getName());
                }
            }
            this.projectCategoryNames = new String[set.size()];
            set.toArray(this.projectCategoryNames);

            // scorecard types
            objectKey = cm.getString(namespace, KEY_SCORECARD_TYPES + "."
                    + KEY_OBJECT_KEY);
            objectIdentifiers = cm.getStringArray(namespace,
                    KEY_SCORECARD_TYPES + "." + KEY_OBJECT_IDENTIFIERS);
            objects = this.createObjects(objFactory, objectKey,
                    objectIdentifiers);
            this.scorecardTypes = new ScorecardType[objects.length];
            System
                    .arraycopy(objects, 0, this.scorecardTypes, 0,
                            objects.length);
            this.scorecardTypeNames = new String[this.scorecardTypes.length];
            for (int i = 0; i < this.scorecardTypes.length; i++) {
                this.scorecardTypeNames[i] = this.scorecardTypes[i].getName();
            }

            // scorecard statuses
            objectKey = cm.getString(namespace, KEY_SCORECARD_STATUSES + "."
                    + KEY_OBJECT_KEY);
            objectIdentifiers = cm.getStringArray(namespace,
                    KEY_SCORECARD_STATUSES + "." + KEY_OBJECT_IDENTIFIERS);
            objects = this.createObjects(objFactory, objectKey,
                    objectIdentifiers);
            this.scorecardStatuses = new ScorecardStatus[objects.length];
            System.arraycopy(objects, 0, this.scorecardStatuses, 0,
                    objects.length);
            this.scorecardStatusNames = new String[this.scorecardStatuses.length];
            for (int i = 0; i < this.scorecardStatuses.length; i++) {
                this.scorecardStatusNames[i] = this.scorecardStatuses[i]
                        .getName();
            }

            // question types
            objectKey = cm.getString(namespace, KEY_QUESTION_TYPES + "."
                    + KEY_OBJECT_KEY);
            objectIdentifiers = cm.getStringArray(namespace, KEY_QUESTION_TYPES
                    + "." + KEY_OBJECT_IDENTIFIERS);
            objects = this.createObjects(objFactory, objectKey,
                    objectIdentifiers);
            this.questionTypes = new QuestionType[objects.length];
            System.arraycopy(objects, 0, this.questionTypes, 0, objects.length);
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
     * Create the objects from the object factory.
     * </p>
     * 
     * @param objFactory
     *            object factory
     * @param objectKey
     *            object key
     * @param objectIdentifiers
     *            object identifiers
     * @return the created objects
     * @throws UnknownNamespaceException
     *             from ConfigManager
     * @throws InvalidClassSpecificationException
     *             from ObjectFactory
     * @throws SpecificationConfigurationException
     *             from ObjectFactory
     * @throws IllegalReferenceException
     *             from ObjectFactory
     */
    private Object[] createObjects(ObjectFactory objFactory, String objectKey,
            String[] objectIdentifiers) throws UnknownNamespaceException,
            InvalidClassSpecificationException,
            SpecificationConfigurationException, IllegalReferenceException {
        Object[] objects = new Object[objectIdentifiers.length];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = objFactory.createObject(objectKey,
                    objectIdentifiers[i]);
        }
        return objects;
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
     * Return the Ajax Support Application URL.
     * </p>
     * 
     * @return the Ajax Support Application URL
     */
    public String getAjaxSupportAppUrl() {
        return this.ajaxSupportAppUrl;
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
                return new ProjectType(this.projectTypes[i].getId(),
                        this.projectTypes[i].getName());
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
                return new ProjectType(this.projectTypes[i].getId(),
                        this.projectTypes[i].getName());
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
                ProjectCategory category = new ProjectCategory(
                        this.projectCategories[i].getId(),
                        this.projectCategories[i].getName(),
                        getProjectType(this.projectCategories[i]
                                .getProjectType().getId()));
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
    public ProjectCategory getProjectCategory(String projectCategoryName,
            long projectTypeId) {
        for (int i = 0; i < this.projectCategories.length; i++) {
            if (this.projectCategories[i].getName().equals(projectCategoryName)
                    && this.projectCategories[i].getProjectType().getId() == projectTypeId) {
                ProjectCategory category = new ProjectCategory(
                        this.projectCategories[i].getId(),
                        this.projectCategories[i].getName(),
                        getProjectType(this.projectCategories[i]
                                .getProjectType().getId()));
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
    public ProjectCategory getProjectCategory(String projectCategoryName,
            String projectTypeName) {
        for (int i = 0; i < this.projectCategories.length; i++) {
            if (this.projectCategories[i].getName().equals(projectCategoryName)
                    && this.projectCategories[i].getProjectType().getName()
                            .equals(projectTypeName)) {
                ProjectCategory category = new ProjectCategory(
                        this.projectCategories[i].getId(),
                        this.projectCategories[i].getName(),
                        getProjectType(this.projectCategories[i]
                                .getProjectType().getId()));
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
                return new ScorecardType(this.scorecardTypes[i].getId(),
                        this.scorecardTypes[i].getName());
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
                return new ScorecardType(this.scorecardTypes[i].getId(),
                        this.scorecardTypes[i].getName());
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
                return new ScorecardStatus(this.scorecardStatuses[i].getId(),
                        this.scorecardStatuses[i].getName());
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
                return new ScorecardStatus(this.scorecardStatuses[i].getId(),
                        this.scorecardStatuses[i].getName());

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
                return new QuestionType(this.questionTypes[i].getId(),
                        this.questionTypes[i].getName());
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
                return new QuestionType(this.questionTypes[i].getId(),
                        this.questionTypes[i].getName());
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
                ProjectCategory category = new ProjectCategory(
                        this.projectCategories[i].getId(),
                        this.projectCategories[i].getName(),
                        getProjectType(this.projectCategories[i]
                                .getProjectType().getId()));
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
            if (this.projectCategories[i].getProjectType().getName().equals(
                    projectTypeName)) {
                ProjectCategory category = new ProjectCategory(
                        this.projectCategories[i].getId(),
                        this.projectCategories[i].getName(),
                        getProjectType(this.projectCategories[i]
                                .getProjectType().getId()));
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
            categories[i] = new ProjectCategory(this.projectCategories[i]
                    .getId(), this.projectCategories[i].getName(),
                    new ProjectType(this.projectCategories[i].getProjectType()
                            .getId(), this.projectCategories[i]
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
            types[i] = new ProjectType(this.projectTypes[i].getId(),
                    this.projectTypes[i].getName());
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
            types[i] = new ScorecardType(this.scorecardTypes[i].getId(),
                    this.scorecardTypes[i].getName());
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
            statuses[i] = new ScorecardStatus(
                    this.scorecardStatuses[i].getId(),
                    this.scorecardStatuses[i].getName());
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
            types[i] = new QuestionType(this.questionTypes[i].getId(),
                    this.questionTypes[i].getName());
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
        Section section = new Section();
        section.setName("Section name goes here.");
        section.setWeight(100);
        section.addQuestion(buildNewQuestion());
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
        Group group = new Group();
        group.setName("Group name goes here.");
        group.setWeight(100);
        group.addSection(buildNewSection());
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
        Scorecard scorecard = new Scorecard();
        scorecard.setInUse(false);
        scorecard.setMaxScore(100);
        scorecard.setMinScore(0);
        scorecard.setName("Scorecard name goes here.");
        scorecard.setCategory(1);
        scorecard.setScorecardStatus(ScorecardActionsHelper.getInstance()
                .getScorecardStatus("Inactive"));
        scorecard.setScorecardType(ScorecardActionsHelper.getInstance()
                .getScorecardType(1));
        scorecard.setVersion("1.0");
        scorecard.addGroup(buildNewGroup());
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
        // reset id and inUse
        scorecard.resetId();
        scorecard.setInUse(false);
        Group[] groups = scorecard.getAllGroups();
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
        String version = scorecard.getVersion();
        int dotIdx = version.indexOf(".");
        long major;
        if (dotIdx == -1) {
            major = Long.parseLong(version) + 1;
            scorecard.setVersion(major + "");
        } else {
            major = Long.parseLong(version.substring(0, dotIdx)) + 1;
            scorecard.setVersion(major + version.substring(dotIdx));
        }
        return scorecard;
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
