/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.topcoder.onlinereview.migration.dto.newschema.deliverable.Submission;
import com.topcoder.onlinereview.migration.dto.newschema.deliverable.Upload;
import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectNew;
import com.topcoder.onlinereview.migration.dto.newschema.resource.Resource;
import com.topcoder.onlinereview.migration.dto.newschema.resource.ResourceInfo;
import com.topcoder.onlinereview.migration.dto.newschema.review.Review;
import com.topcoder.onlinereview.migration.dto.newschema.review.ReviewComment;
import com.topcoder.onlinereview.migration.dto.newschema.review.ReviewItem;
import com.topcoder.onlinereview.migration.dto.newschema.review.ReviewItemComment;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.Scorecard;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardQuestionNew;
import com.topcoder.onlinereview.migration.dto.newschema.screening.ScreeningTask;
import com.topcoder.onlinereview.migration.dto.oldschema.ProjectOld;
import com.topcoder.onlinereview.migration.dto.oldschema.deliverable.SubmissionOld;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.RUserRole;
import com.topcoder.onlinereview.migration.dto.oldschema.scorecard.QuestionTemplate;
import com.topcoder.onlinereview.migration.dto.oldschema.scorecard.ScorecardTemplate;

import junit.framework.TestCase;

/**
 * <p>
 * Scenarios involving "2.1.1.2.1	 Data Mapping":
 * </p>
 * <ul>
 * <li>Scenario #27 (FTC 3) Verify the scorecard mappings are correct.</li>
 * <li>Scenario #28 (FTC 3) Screening Scorecard is mapped to 2.</li>
 * <li>Scenario #29 (FTC 3) Review Scorecard is mapped to 1.</li>
 * <li>Scenario #30 (FTC 3) A Component Design Scorecard is mapped to 2.</li>
 * <li>Scenario #31 (FTC 3) A Component Development Scorecard is mapped to 1.</li>
 * <li>Scenario #32 (FTC 3)  The Score of a scorecard is lower than 75.</li>
 * <li>Scenario #33 (FTC 3) The Score of a scorecard is higher than 100.</li>
 * <li>Scenario #34 (FTC 3) A Test Case question is not mapped to 3.</li>
 * <li>Scenario #35 (FTC 3) The mapping of projects is like defined in the following table.</li>
 * <li>Scenario #36 (FTC 3) An active project is not mapped to 1 or 3.</li>
 * <li>Scenario #37 (FTC 3) A deleted project is not mapped to 2.</li>
 * <li>Scenario #38 (FTC 3) A completed project is not mapped to 4.</li>
 * <li>Scenario #39 (FTC 3) A component design project is not mapped to 1.</li>
 * <li>Scenario #40 (FTC 3) A component development project is not mapped to 2.</li>
 * <li>Scenario #41 (FTC 3) A phase of a project is mapped to the project_id.</li>
 * <li>Scenario #42 (FTC 3) Phases are not mapped to project_id.</li>
 * <li>Scenario #43 (FTC 3) Phase is mapped to wrong project_id.</li>
 * <li>Scenario #44 (FTC 3) The Resources mapping is done as defined in the following table.</li>
 * <li>Scenario #45 (FTC 3) The resource is not mapped to the project_id.</li>
 * <li>Scenario #46 (FTC 3) User Role is not mapped to the resource id.</li>
 * <li>Scenario #47 (FTC 3) Payment status is not mapped to 1.</li>
 * <li>Scenario #48 (FTC 3) Payment status s is not mapped to 2.</li>
 * <li>Scenario #49 (FTC 3) All Deliverable fields are mapped as defined in the following table.</li>
 * <li>Scenario #50 (FTC 3) The uploaded file is not mapped to the project id.</li>
 * <li>Scenario #51 (FTC 3) An uploaded submission in the submission phase is not mapped to Submission.</li>
 * <li>Scenario #52 (FTC 3) Files are not stored in the file system server.</li>
 * <li>Scenario #54 (FTC 3) The mapping in the Review is as defined in the following table.</li>
 * <li>Scenario #55 (FTC 3) Response type required is not mapped to 1.</li>
 * <li>Scenario #56 (FTC 3) Response type Recommended is not mapped to 2.</li>
 * <li>Scenario #57 (FTC 3) Response type Comment is not mapped to 3.</li>
 * <li>Scenario #58 (FTC 3) Response type Approved is not mapped to 1.</li>
 * <li>Scenario #59 (FTC 3) Response type Rejected is not mapped to 2.</li>
 * <li>Scenario #60 (FTC 3) The mapping is as defined in the table below.</li>
 * <li>Scenario #61 (FTC 3) The submission_v_id is not mapped to the auto screening.</li>
 * <li>Scenario #62 (FTC 3) The submission_id is not mapped to the upload_id.</li>
 * <li>Scenario #63 (FTC 3) The submission is not mapped to the screening task id.</li>
 * </ul>
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioDataMapping extends TestCase {
	private static final int SCREENING_SCORECARD_TYPE_ID = 1;
	private static final int REVIEW_SCORECARD_TYPE_ID = 2;
	private static final int DESIGN_PROJECT_CATEGORY_ID = 1;
	private static final int DEVELOPMENT_PROJECT_CATEGORY_ID = 2;

	private static final int SCALE1_4_SCORECARD_QUESTION_TYPE_ID = 1;
	private static final int TESTCASE_SCORECARD_QUESTION_TYPE_ID = 3;
	private static final int YESNO_SCORECARD_QUESTION_TYPE_ID = 4;
	
	private static final int ACTIVE_PROJECT_STATUS_ID = 1;
	private static final int DELETED_PROJECT_STATUS_ID = 3;
	private static final int COMPLETED_PROJECT_STATUS_ID = 7;
	private DataMigrator dataMigrator = null;

    /**
     * <p>
     * Set up environment.
     * </p>
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
    	Utility.getInstance().clearProjects();
    	Utility.getInstance().clearScorecards();
    	dataMigrator = Utility.getInstance().getDataMigrator();    	
    }

    /**
     * Close the jdbc resource in this dataMigrator.
     * 
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
    	dataMigrator.close();
    	Utility.getInstance().clearScorecards();
    }

    /**
     * <p>
     * Scenario #27 (FTC 3) Verify the scorecard mappings are correct.
     * 1. Prepare scorecard data
     * 2. Transform
     * Expected Outcome:
     * Mapping process successful 
     * scorecard_type_id 	1 maps to 'Screening' 
     * project_category_id  1 maps to 'Component Design' 
     * min_score			75.0
     * max_score 			100.0
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario27_a() throws Exception {
    	ScorecardTemplate scorecardTemplate = prepareScorecardTemplate();
    	scorecardTemplate.setProjectType(1);
    	scorecardTemplate.setScorecardType(1);
    	Scorecard scorecard = dataMigrator.getScorecardTransformer().transformScorecardTemplate(scorecardTemplate);
    	assertEquals("Map incorrect scorecard_type_id", scorecard.getScorecardTypeId(), SCREENING_SCORECARD_TYPE_ID);
    	assertEquals("Map incorrect project_category_id", scorecard.getProjectCategoryId(), DESIGN_PROJECT_CATEGORY_ID); 
    	assertEquals("Map incorrect min_score", scorecard.getMinScore(), (float) 75.0, 1e-9);
    	assertEquals("Map incorrect max_score", scorecard.getMaxScore(), (float) 100.0, 1e-9);
    }

    /**
     * <p>
     * Scenario #27 (FTC 3) Verify the scorecard mappings are correct.
     * 1. Prepare scorecard data
     * 2. Transform
     * Expected Outcome:
     * Mapping process successful 
     * scorecard_type_id 	2 maps to 'Review'
     * project_category_id  2 maps to 'Component Development'
     * min_score			75.0
     * max_score 			100.0
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario27_b() throws Exception {
    	ScorecardTemplate scorecardTemplate = prepareScorecardTemplate();
    	scorecardTemplate.setProjectType(2);
    	scorecardTemplate.setScorecardType(2);
    	Scorecard scorecard = dataMigrator.getScorecardTransformer().transformScorecardTemplate(scorecardTemplate);
    	assertEquals("Map incorrect scorecard_type_id", scorecard.getScorecardTypeId(), REVIEW_SCORECARD_TYPE_ID);
    	assertEquals("Map incorrect project_category_id", scorecard.getProjectCategoryId(), DEVELOPMENT_PROJECT_CATEGORY_ID); 
    	assertEquals("Map incorrect min_score", scorecard.getMinScore(), (float) 75.0, 1e-9);
    	assertEquals("Map incorrect max_score", scorecard.getMaxScore(), (float) 100.0, 1e-9);
    }

    /**
     * <p>
     * Scenario #27 (FTC 3) Verify the scorecard mappings are correct.
     * 1. Prepare scorecard data
     * 2. Transform
     * Expected Outcome:
     * Mapping process successful 
     * scorecard_question_type_id 	2 maps to 'Scale 1-4', 3 maps to 'Test Case' and 4 maps to 'Yes/No'
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario27_c() throws Exception {
    	List list = new ArrayList();
    	QuestionTemplate qt = prepareQuestionTemplate();
    	qt.setQuestionType(2);
    	list.add(qt);
    	
    	qt = prepareQuestionTemplate();
    	qt.setQuestionType(3);
    	list.add(qt);
    	
    	qt = prepareQuestionTemplate();
    	qt.setQuestionType(4);
    	list.add(qt);
    	
    	List results = dataMigrator.getScorecardTransformer().transformScorecardQuestion(1, list);
    	assertEquals("Result size is incorrect", results.size(), 3);

    	ScorecardQuestionNew result = (ScorecardQuestionNew) results.get(0);
    	assertEquals("Map incorrect scorecard_question_type_id", result.getScorecardQuestionTypeId(), SCALE1_4_SCORECARD_QUESTION_TYPE_ID);
    	
    	result = (ScorecardQuestionNew) results.get(1);
    	assertEquals("Map incorrect scorecard_question_type_id", result.getScorecardQuestionTypeId(), TESTCASE_SCORECARD_QUESTION_TYPE_ID);
    	
    	result = (ScorecardQuestionNew) results.get(2);
    	assertEquals("Map incorrect scorecard_question_type_id", result.getScorecardQuestionTypeId(), YESNO_SCORECARD_QUESTION_TYPE_ID);
    }

    /**
     * Create ScorecardTemplate and set base properties.
     * 
     * @return ScorecardTemplate instance
     */
    private ScorecardTemplate prepareScorecardTemplate() {
    	ScorecardTemplate st = new ScorecardTemplate();
    	st.setStatusId(1);
    	st.setTemplateId(1);
    	st.setTemplateName("test");
    	return st;
    }

    /**
     * Create QuestionTemplate and set base properties.
     * 
     * @return QuestionTemplate instance
     */
    private QuestionTemplate prepareQuestionTemplate() {
    	QuestionTemplate qt = new QuestionTemplate();
    	qt.setQTemplateVid(1);
    	qt.setQTemplateId(1);
    	qt.setQuestionText("question text");
    	qt.setQuestionWeight(20);
    	qt.setQuestionSecLoc(1);
    	return qt;
    }

    /**
     * <p>
     * Scenario #28 (FTC 3) Screening Scorecard is mapped to 2.
     * 1. Prepare scorecard data
     * 2. Transform
     * Expected Outcome:
     * scorecard_type_id	1 maps to 'Screening' and 2 maps to 'Review'
     * Scorecard is not found under Screening. The scorecard will be assigned to Review, which is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario28() throws Exception {
    	ScorecardTemplate scorecardTemplate = prepareScorecardTemplate();
    	scorecardTemplate.setProjectType(1);
    	scorecardTemplate.setScorecardType(2);
    	Scorecard scorecard = dataMigrator.getScorecardTransformer().transformScorecardTemplate(scorecardTemplate);
    	assertTrue("Map incorrect scorecard_type_id", scorecard.getScorecardTypeId() != SCREENING_SCORECARD_TYPE_ID); 
    }

    /**
     * <p>
     * Scenario #29 (FTC 3) Review Scorecard is mapped to 1.
     * 1. Prepare scorecard data
     * 2. Transform
     * Expected Outcome:
     * scorecard_type_id	1 maps to 'Screening' and 2 maps to 'Review'
     * Scorecard is not found under Review. The scorecard will be assigned to Screening, which is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario29() throws Exception {
    	ScorecardTemplate scorecardTemplate = prepareScorecardTemplate();
    	scorecardTemplate.setProjectType(2);
    	scorecardTemplate.setScorecardType(1);
    	Scorecard scorecard = dataMigrator.getScorecardTransformer().transformScorecardTemplate(scorecardTemplate);
    	assertTrue("Map incorrect scorecard_type_id", scorecard.getScorecardTypeId() != REVIEW_SCORECARD_TYPE_ID);
    }

    /**
     * <p>
     * Scenario #30 (FTC 3) A Component Design Scorecard is mapped to 2.
     * 1. Prepare scorecard data
     * 2. Transform
     * Expected Outcome:
     * project_category_id	1 maps to 'Component Design' and 2 maps to 'Component Development'
     * Scorecard is mapped to the wrong Component type.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario30() throws Exception {
    	ScorecardTemplate scorecardTemplate = prepareScorecardTemplate();
    	scorecardTemplate.setProjectType(2);
    	scorecardTemplate.setScorecardType(1);
    	Scorecard scorecard = dataMigrator.getScorecardTransformer().transformScorecardTemplate(scorecardTemplate);
    	assertTrue("Map incorrect project_category_id", scorecard.getProjectCategoryId() != DESIGN_PROJECT_CATEGORY_ID);
    }

    /**
     * <p>
     * Scenario #31 (FTC 3) A Component Development Scorecard is mapped to 1.
     * 1. Prepare scorecard data
     * 2. Transform
     * Expected Outcome:
     * project_category_id	1 maps to 'Component Design' and 2 maps to 'Component Development'
     * Scorecard is mapped to the wrong Component type.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario31() throws Exception {
    	ScorecardTemplate scorecardTemplate = prepareScorecardTemplate();
    	scorecardTemplate.setProjectType(1);
    	scorecardTemplate.setScorecardType(1);
    	Scorecard scorecard = dataMigrator.getScorecardTransformer().transformScorecardTemplate(scorecardTemplate);
    	assertTrue("Map incorrect project_category_id", scorecard.getProjectCategoryId() != DEVELOPMENT_PROJECT_CATEGORY_ID);
    }

    /**
     * <p>
     * Scenario #32 (FTC 3)  The Score of a scorecard is lower than 75.
     * 1. Prepare scorecard data
     * 2. Transform
     * Expected Outcome:
     * Min_score mapping is not correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario32() throws Exception {
    	ScorecardTemplate scorecardTemplate = prepareScorecardTemplate();
    	scorecardTemplate.setProjectType(1);
    	scorecardTemplate.setScorecardType(1);
    	Scorecard scorecard = dataMigrator.getScorecardTransformer().transformScorecardTemplate(scorecardTemplate);
    	assertFalse("Min_score won't be lower than 75", scorecard.getMinScore() < 75.0);
    }

    /**
     * <p>
     * Scenario #33 (FTC 3) The Score of a scorecard is higher than 100.
     * 1. Prepare scorecard data
     * 2. Transform
     * Expected Outcome:
     * Max_score mapping is not correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario33() throws Exception {
    	ScorecardTemplate scorecardTemplate = prepareScorecardTemplate();
    	scorecardTemplate.setProjectType(1);
    	scorecardTemplate.setScorecardType(1);
    	Scorecard scorecard = dataMigrator.getScorecardTransformer().transformScorecardTemplate(scorecardTemplate);
    	assertFalse("Max_score won't be higher than 100", scorecard.getMaxScore() > 100.0);
    }

    /**
     * <p>
     * Scenario #34 (FTC 3) A Test Case question is not mapped to 3.
     * 1. Prepare scorecard data
     * 2. Transform
     * Expected Outcome:
     * scorecard_question_type_id	2 maps to 'Scale 1-4', 3 maps to 'Test Case' and 4 maps to 'Yes/No'
     * The mapping was not right.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario34() throws Exception {
    	List list = new ArrayList();
    	QuestionTemplate qt = prepareQuestionTemplate();
    	qt.setQuestionType(2);
    	list.add(qt);

    	List results = dataMigrator.getScorecardTransformer().transformScorecardQuestion(1, list);
    	assertEquals("Result size is incorrect", results.size(), 1);

    	ScorecardQuestionNew result = (ScorecardQuestionNew) results.get(0);
    	assertTrue("Map incorrect scorecard_question_type_id", result.getScorecardQuestionTypeId() != TESTCASE_SCORECARD_QUESTION_TYPE_ID);
    }

    /**
     * <p>
     * Scenario #35 (FTC 3) The mapping of projects is like defined in the following table.
     * 1. Load project data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * project_status_id	1 and 3 map to 'Active', 2 maps to 'Deleted', 4 maps to 'Completed'
     * project_category_id	1 maps to 'Component Design' and 2 maps to 'Component Development'
     * The mapping of projects is successful.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario35() throws Exception {
    	ProjectOld old = new ProjectOld();
    	
    	// 1 map to active, 1 maps to 'Component Design'
    	old.setProjectTypeId(1);
    	old.setProjectStatId(1);
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("Map incorrect project_status_id", project.getProjectStatusId(), ACTIVE_PROJECT_STATUS_ID);
    	assertEquals("Map incorrect project_category_id", project.getProjectCategoryId(), DESIGN_PROJECT_CATEGORY_ID);
    	
    	// 3 map to active, 2 maps to 'Component Development'
    	old.setProjectTypeId(2);
    	old.setProjectStatId(3);
    	project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("Map incorrect project_status_id", project.getProjectStatusId(), ACTIVE_PROJECT_STATUS_ID);
    	assertEquals("Map incorrect project_category_id", project.getProjectCategoryId(), DEVELOPMENT_PROJECT_CATEGORY_ID);
    	
    	// 2 maps to 'Deleted'
    	old.setProjectStatId(2);
    	project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("Map incorrect project_status_id", project.getProjectStatusId(), DELETED_PROJECT_STATUS_ID);

    	// 4 maps to 'Completed'
    	old.setProjectStatId(4);
    	project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("Map incorrect project_status_id", project.getProjectStatusId(), COMPLETED_PROJECT_STATUS_ID);
    }

    /**
     * <p>
     * Scenario #36 (FTC 3) An active project is not mapped to 1 or 3.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * project_status_id 1 and 3 map to 'Active', 2 maps to 'Deleted', 4 maps to 'Completed'
     * The project is not saved under active projects.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario36() throws Exception {
    	ProjectOld old = new ProjectOld();
    	old.setProjectTypeId(1);
    	old.setProjectStatId(2);
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertTrue("Map incorrect project_status_id", project.getProjectStatusId() != ACTIVE_PROJECT_STATUS_ID);
    }

    /**
     * <p>
     * Scenario #37 (FTC 3) A deleted project is not mapped to 2.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The project is not deleted.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario37() throws Exception {
    	ProjectOld old = new ProjectOld();
    	old.setProjectTypeId(1);
    	old.setProjectStatId(3);
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertTrue("Map incorrect project_status_id", project.getProjectStatusId() != DELETED_PROJECT_STATUS_ID);
    }

    /**
     * <p>
     * Scenario #38 (FTC 3) A completed project is not mapped to 4.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The project is saved under completed projects.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario38() throws Exception {
    	ProjectOld old = new ProjectOld();
    	old.setProjectTypeId(1);
    	old.setProjectStatId(1);
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertTrue("Map incorrect project_status_id", project.getProjectStatusId() != COMPLETED_PROJECT_STATUS_ID);
    }

    /**
     * <p>
     * Scenario #39 (FTC 3) A component design project is not mapped to 1.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * project_category_id 1 maps to 'Component Design' and 2 maps to 'Component Development'
     * The project is not assigned to Component Design.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario39() throws Exception {
    	ProjectOld old = new ProjectOld();
    	old.setProjectTypeId(2);
    	old.setProjectStatId(1);
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertTrue("Map incorrect project_category_id", project.getProjectCategoryId() != DESIGN_PROJECT_CATEGORY_ID);
    }

    /**
     * <p>
     * Scenario #40 (FTC 3) A component development project is not mapped to 2.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The project is not assigned to Component Development.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario40() throws Exception {
    	ProjectOld old = new ProjectOld();
    	old.setProjectTypeId(1);
    	old.setProjectStatId(1);
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertTrue("Map incorrect project_category_id", project.getProjectCategoryId() != DEVELOPMENT_PROJECT_CATEGORY_ID);
    }

    /**
     * <p>
     * Scenario #41 (FTC 3) A phase of a project is mapped to the project_id.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * The mapping is correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario41() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("all phases is migrated", project.getPhases().size(), 9);
    }

    /**
     * <p>
     * Scenario #42 (FTC 3) Phases are not mapped to project_id.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * The phases will be missing in the project screen.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario42() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	old.getPhaseInstances().remove(Utility.PHASE_ID_FINAL_REVIEW - 1);
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("The phase should be removed", project.getPhases().size(), 8);
    }

    /**
     * <p>
     * Scenario #43 (FTC 3) Phase is mapped to wrong project_id.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * The phase will be missing in the current project screen.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario43() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	old.getPhaseInstances().remove(Utility.PHASE_ID_FINAL_REVIEW - 1);
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("The phase should be removed", project.getPhases().size(), 8);
    }

    /**
     * <p>
     * Scenario #44 (FTC 3) The Resources mapping is done as defined in the following table.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * r_user_role_id	maps to resource id
     * All resources will be shown correctly in the project page.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario44() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("The Resources should be tranformed", project.getResources().size(), old.getRUserRoles().size());
    }

    /**
     * <p>
     * Scenario #45 (FTC 3) The resource is not mapped to the project_id.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * r_user_role_id	maps to resource id
     * Resources are missing on the project page.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario45() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	int origSize = old.getRUserRoles().size();
    	for (int i = 0; i < origSize; i++) {
    		// Move aggregator resource to other project
    		RUserRole role = (RUserRole) old.getRUserRoles().get(i);
    		if (role.getRRoleId() == Utility.ROLE_ID_AGGREGATOR) {
    			 old.getRUserRoles().remove(i);
    			 break;
    		}
    	}
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("The Resources should be moved", project.getResources().size() + 1, origSize);
    }

    /**
     * <p>
     * Scenario #46 (FTC 3) User Role is not mapped to the resource id.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * r_user_role_id	maps to resource id
     * The will be no roles in the resource table on the project page.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario46() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	int origSize = old.getRUserRoles().size();
    	for (int i = 0; i < origSize; i++) {
    		// Move aggregator resource to other project
    		RUserRole role = (RUserRole) old.getRUserRoles().get(i);
    		if (role.getRRoleId() == Utility.ROLE_ID_FINAL_REVIEWER) {
    			 old.getRUserRoles().remove(i);
    			 break;
    		}
    	}
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("The Resources should be moved", project.getResources().size() + 1, origSize);
    }

    /**
     * <p>
     * Scenario #47 (FTC 3) Payment status is not mapped to 1.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * payment_stat_id	1 maps to 'No', 2 maps to 'Yes'
     * User will not been shown as unpaid.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario47() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	for (int i = 0; i < old.getRUserRoles().size(); i++) {
    		RUserRole role = (RUserRole) old.getRUserRoles().get(i);
    		if (role.getRRoleId() == Utility.ROLE_ID_FINAL_REVIEWER) {
    			// change payment status to 2
    			role.getPaymentInfo().setPaymentStatId(2);
    			 break;
    		}
    	}
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	Resource res = null;
    	for (Iterator iter = project.getResources().iterator(); iter.hasNext();) {
    		Resource temp = (Resource) iter.next();
    		if (temp.getResourceRoleId() == Resource.FINA_REVIEWER_RESOURCE_ROLE) {
    			res = temp;
    			break;
    		}
    	}
    	assertNotNull("Failed to find wanted resource", res);

    	ResourceInfo paymentRI = null;
		List ris = project.getResourceInfosByResourceId(res.getResourceId());
		for (Iterator riIter = ris.iterator(); riIter.hasNext();) {
			ResourceInfo temp = (ResourceInfo) riIter.next();
			if (temp.getResourceInfoTypeId() == ResourceInfo.PAYMENT_STATUS) {
				paymentRI = temp;
				break;
			}
		}
    	assertNotNull("Failed to find wanted resource", paymentRI);
    	assertFalse("Should not be No", "No".equals(paymentRI.getValue()));
    }

    /**
     * <p>
     * Scenario #48 (FTC 3) Payment status s is not mapped to 2.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * payment_stat_id	1 maps to 'No', 2 maps to 'Yes'
     * User will not been shown as paid.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario48() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	for (int i = 0; i < old.getRUserRoles().size(); i++) {
    		RUserRole role = (RUserRole) old.getRUserRoles().get(i);
    		if (role.getRRoleId() == Utility.ROLE_ID_FINAL_REVIEWER) {
    			// change payment status to 1
    			role.getPaymentInfo().setPaymentStatId(1);
    			 break;
    		}
    	}
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	Resource res = null;
    	for (Iterator iter = project.getResources().iterator(); iter.hasNext();) {
    		Resource temp = (Resource) iter.next();
    		if (temp.getResourceRoleId() == Resource.FINA_REVIEWER_RESOURCE_ROLE) {
    			res = temp;
    			break;
    		}
    	}
    	assertNotNull("Failed to find wanted resource", res);

    	ResourceInfo paymentRI = null;
		List ris = project.getResourceInfosByResourceId(res.getResourceId());
		for (Iterator riIter = ris.iterator(); riIter.hasNext();) {
			ResourceInfo temp = (ResourceInfo) riIter.next();
			if (temp.getResourceInfoTypeId() == ResourceInfo.PAYMENT_STATUS) {
				paymentRI = temp;
				break;
			}
		}
    	assertNotNull("Failed to find wanted resource", paymentRI);
    	assertFalse("Should not be Yes", "Yes".equals(paymentRI.getValue()));
    }

    /**
     * <p>
     * Scenario #49 (FTC 3) All Deliverable fields are mapped as defined in the following table.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * All Deliverables are mapped correctly.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario49() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("Incorrect submissions", project.getSubmissions().size(), 2);
    	assertEquals("Incorrect uploads", project.getUploads().size(), 3);

    	// first removed submissions
    	Upload upload = (Upload) project.getUploads().get(0);
    	assertEquals(upload.getUploadTypeId(), Upload.UPLOAD_TYPE_SUBMISSION);
    	assertEquals(upload.getUploadStatusId(), Upload.UPLOAD_STATUS_DELETED);
    	assertEquals(upload.getParameter(), Utility.SUBMISSION_URL_REMOVED);
    	assertEquals(upload.getCreateUser(), "Converter");
    	assertEquals(upload.getModifyUser(), "Converter");
    	
    	// passed submissions
    	upload = (Upload) project.getUploads().get(1);
    	assertEquals(upload.getUploadTypeId(), Upload.UPLOAD_TYPE_SUBMISSION);
    	assertEquals(upload.getUploadStatusId(), Upload.UPLOAD_STATUS_ACTIVE);
    	assertEquals(upload.getParameter(), Utility.PASSED_SUBMISSION_URL);
    	assertEquals(upload.getCreateUser(), "Converter");
    	assertEquals(upload.getModifyUser(), "Converter");
    	int uploadId1 = upload.getUploadId();
    	
    	// final fix submissions
    	upload = (Upload) project.getUploads().get(2);
    	assertEquals(upload.getUploadTypeId(), Upload.UPLOAD_TYPE_FINAL_FIX);
    	assertEquals(upload.getUploadStatusId(), Upload.UPLOAD_STATUS_ACTIVE);
    	assertEquals(upload.getParameter(), Utility.FINAL_FIX_SUBMISSION_URL);
    	assertEquals(upload.getCreateUser(), "Converter");
    	assertEquals(upload.getModifyUser(), "Converter");
    	int uploadId2 = upload.getUploadId();
    	
    	// passed submissions
    	Submission submission = (Submission) project.getSubmissions().get(0);
    	assertEquals(submission.getSubmissionStatusId(), Submission.SUBMISSION_STATUS_ACTIVE);
    	assertEquals(submission.getUploadId(), uploadId1);
    	assertEquals(submission.getCreateUser(), "Converter");
    	assertEquals(submission.getModifyUser(), "Converter");
    	
    	// final fix submissions
    	submission = (Submission) project.getSubmissions().get(1);
    	assertEquals(submission.getSubmissionStatusId(), Submission.SUBMISSION_STATUS_ACTIVE);
    	assertEquals(submission.getUploadId(), uploadId2);
    	assertEquals(submission.getCreateUser(), "Converter");
    	assertEquals(submission.getModifyUser(), "Converter");
    }

    /**
     * <p>
     * Scenario #50 (FTC 3) The uploaded file is not mapped to the project id.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * project_id	maps to the project id
     * The file will be not linked and shown in the online review.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario50() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	old.getSubmissions().remove(0);
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("Incorrect submissions", project.getSubmissions().size(), 1);
    	assertEquals("Incorrect uploads", project.getUploads().size(), 5);
    }

    /**
     * <p>
     * Scenario #51 (FTC 3) An uploaded submission in the submission phase is not mapped to Submission.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * upload_type_id	all submissions made in the submission phase map to 'Submission', otherwise map to 'Final Fix'
     * The submission will be not been found in the submission phase.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario51() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	SubmissionOld sub = (SubmissionOld) old.getSubmissions().get(1);
    	// Set to final fix
    	sub.setSubmissionType(2);
    	
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);

    	// should not map to submission
    	Upload upload = (Upload) project.getUploads().get(1);
    	assertEquals(upload.getUploadTypeId(), Upload.UPLOAD_TYPE_FINAL_FIX);
    	assertEquals(upload.getUploadStatusId(), Upload.UPLOAD_STATUS_ACTIVE);
    	assertEquals(upload.getParameter(), Utility.PASSED_SUBMISSION_URL);
    	assertEquals(upload.getCreateUser(), "Converter");
    	assertEquals(upload.getModifyUser(), "Converter");
    }

    /**
     * <p>
     * Scenario #52 (FTC 3) Files are not stored in the file system server.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * parameter	upload the file to File System Server and store the parameter
     * The mapping for file upload was not correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario52() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	SubmissionOld sub = (SubmissionOld) old.getSubmissions().get(0); 
    	sub.setSubmissionUrl("bad url");
    	
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertEquals("Incorrect submissions", project.getSubmissions().size(), 2);
    	assertEquals("Incorrect uploads", project.getUploads().size(), 3);
    	
    	// first removed submissions
    	Upload upload = (Upload) project.getUploads().get(0);
    	assertEquals(upload.getUploadTypeId(), Upload.UPLOAD_TYPE_SUBMISSION);
    	assertEquals(upload.getUploadStatusId(), Upload.UPLOAD_STATUS_DELETED);
    	assertNotSame(upload.getParameter(), Utility.SUBMISSION_URL_REMOVED);
    	assertEquals(upload.getCreateUser(), "Converter");
    	assertEquals(upload.getModifyUser(), "Converter");
    }

    /**
     * <p>
     * Scenario #54 (FTC 3) The mapping in the Review is as defined in the following table.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * The mapping was successful.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario54() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	List reviews = project.getReviews();
    	assertEquals("incorrect review size", reviews.size(), 6);
    	
    	// Screen review
    	Review review = (Review) reviews.get(0);
    	assertEquals(project.getResourceById(review.getResourceId()).getResourceRoleId(), Resource.SCREENER_RESOURCE_ROLE);
    	
    	// failure review
    	review = (Review) reviews.get(1);
    	assertEquals(project.getResourceById(review.getResourceId()).getResourceRoleId(), Resource.FAILURE_REVIEWER_RESOURCE_ROLE);

    	// stress review
    	review = (Review) reviews.get(2);
    	assertEquals(project.getResourceById(review.getResourceId()).getResourceRoleId(), Resource.STRESS_REVIEWER_RESOURCE_ROLE);
    	
    	// accuracy review
    	review = (Review) reviews.get(3);
    	assertEquals(project.getResourceById(review.getResourceId()).getResourceRoleId(), Resource.ACCURACY_REVIEWER_RESOURCE_ROLE);
    	
    	// aggregate review
    	review = (Review) reviews.get(4);
    	assertEquals(project.getResourceById(review.getResourceId()).getResourceRoleId(), Resource.AGGREGATOR_RESOURCE_ROLE);
    	
    	// final review
    	review = (Review) reviews.get(5);
    	assertEquals(project.getResourceById(review.getResourceId()).getResourceRoleId(), Resource.FINA_REVIEWER_RESOURCE_ROLE);
    }

    /**
     * <p>
     * Scenario #55 (FTC 3) Response type required is not mapped to 1.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * response_type_id	1 maps to 'Required', 2 maps to 'Recommended', 3 maps to 'Comment'
     * The mapping is not correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario55() throws Exception {
    	Utility utility = Utility.getInstance();
    	utility.setResponseTypeId(1);
    	ProjectOld old = utility.prepareProjectOld();
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	
    	// Retreive the review item comment
    	assertTrue("Failed to convert review", project.getReviews().size() > 0);
    	Review review = (Review) project.getReviews().iterator().next();

    	assertTrue("Failed to convert ReviewItem", review.getReviewItems().size() > 0);
    	ReviewItem ri = (ReviewItem) review.getReviewItems().iterator().next();
    	
    	assertTrue("Failed to convert ReviewItemComment", ri.getReviewItemComments().size() > 0);
    	ReviewItemComment ric = (ReviewItemComment) ri.getReviewItemComments().iterator().next();
    	assertEquals("incorrect comment type", ric.getCommentTypeId(), ReviewItemComment.COMMENT_TYPE_REQUIRED);
    }

    /**
     * <p>
     * Scenario #56 (FTC 3) Response type Recommended is not mapped to 2.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * response_type_id	1 maps to 'Required', 2 maps to 'Recommended', 3 maps to 'Comment'
     * The mapping is not correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario56() throws Exception {
    	Utility utility = Utility.getInstance();
    	utility.setResponseTypeId(2);
    	ProjectOld old = utility.prepareProjectOld();
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	
    	// Retreive the review item comment
    	assertTrue("Failed to convert review", project.getReviews().size() > 0);
    	Review review = (Review) project.getReviews().iterator().next();

    	assertTrue("Failed to convert ReviewItem", review.getReviewItems().size() > 0);
    	ReviewItem ri = (ReviewItem) review.getReviewItems().iterator().next();
    	
    	assertTrue("Failed to convert ReviewItemComment", ri.getReviewItemComments().size() > 0);
    	ReviewItemComment ric = (ReviewItemComment) ri.getReviewItemComments().iterator().next();
    	assertEquals("incorrect comment type", ric.getCommentTypeId(), ReviewItemComment.COMMENT_TYPE_RECOMMENDED);
    }

    /**
     * <p>
     * Scenario #57 (FTC 3) Response type Comment is not mapped to 3.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * response_type_id	1 maps to 'Required', 2 maps to 'Recommended', 3 maps to 'Comment'
     * The mapping is not correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario57() throws Exception {
    	Utility utility = Utility.getInstance();
    	utility.setResponseTypeId(3);
    	ProjectOld old = utility.prepareProjectOld();
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	
    	// Retreive the review item comment
    	assertTrue("Failed to convert review", project.getReviews().size() > 0);
    	Review review = (Review) project.getReviews().iterator().next();

    	assertTrue("Failed to convert ReviewItem", review.getReviewItems().size() > 0);
    	ReviewItem ri = (ReviewItem) review.getReviewItems().iterator().next();
    	
    	assertTrue("Failed to convert ReviewItemComment", ri.getReviewItemComments().size() > 0);
    	ReviewItemComment ric = (ReviewItemComment) ri.getReviewItemComments().iterator().next();
    	assertEquals("incorrect comment type", ric.getCommentTypeId(), ReviewItemComment.COMMENT_TYPE_COMMENT);
    }

    /**
     * <p>
     * Scenario #58 (FTC 3) Response type Approved is not mapped to 1.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * agg_approval_id	1 maps to 'Approved', 2 maps to 'Rejected'
     * The response is not shown as approved.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario58() throws Exception {
    	Utility utility = Utility.getInstance();
    	utility.setAggApprovalId(1);
    	ProjectOld old = utility.prepareProjectOld();
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	
    	// Retreive the review item comment
    	assertTrue("Failed to convert review", project.getReviews().size() >= 5 );
    	// It's aggregate review
    	Review review = (Review) project.getReviews().get(4);

    	assertTrue("Failed to convert ReviewComment", review.getReviewComments().size() > 0);
    	ReviewComment ri = (ReviewComment) review.getReviewComments().iterator().next();
    	
    	assertEquals("incorrect comment type", ri.getCommentTypeId(), ReviewItemComment.COMMENT_TYPE_AGGREGATION_REVIEW_COMMENT);
    	assertEquals("incorrect extra info", ri.getExtraInfo(), "Approved");
    }

    /**
     * <p>
     * Scenario #59 (FTC 3) Response type Rejected is not mapped to 2.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * agg_approval_id	1 maps to 'Approved', 2 maps to 'Rejected'
     * The response is not shown as approved.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario59() throws Exception {
    	Utility utility = Utility.getInstance();
    	utility.setAggApprovalId(2);
    	ProjectOld old = utility.prepareProjectOld();
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	
    	// Retreive the review item comment
    	assertTrue("Failed to convert review", project.getReviews().size() >= 5 );
    	// It's aggregate review
    	Review review = (Review) project.getReviews().get(4);

    	assertTrue("Failed to convert ReviewComment", review.getReviewComments().size() > 0);
    	ReviewComment ri = (ReviewComment) review.getReviewComments().iterator().next();
    	
    	assertEquals("incorrect comment type", ri.getCommentTypeId(), ReviewItemComment.COMMENT_TYPE_AGGREGATION_REVIEW_COMMENT);
    	assertEquals("incorrect extra info", ri.getExtraInfo(), "Rejected");
    }

    /**
     * <p>
     * Scenario #60 (FTC 3) The mapping is as defined in the table below.
     * Scenario #61 (FTC 3) The submission_v_id is not mapped to the auto screening.
     * Scenario #62 (FTC 3) The submission_id is not mapped to the upload_id.
     * Scenario #63 (FTC 3) The submission is not mapped to the screening task id.
     * 1. Load project data
     * 2. Transform
     * Expected Outcome:
     * The screening mapping is correct.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario60() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	// No scrrening tasks for final fix submission
    	assertEquals("incorrect tasks", project.getScreeningTasks().size(), old.getSubmissions().size() - 1);
    	List tasks = project.getScreeningTasks();

    	// First upload
    	ScreeningTask task = (ScreeningTask) tasks.get(0);
    	Upload upload = (Upload) project.getUploads().get(0);
    	assertEquals("incorrect upload id", task.getUploadId(), upload.getUploadId());
    	
    	// second upload
    	task = (ScreeningTask) tasks.get(1);
    	upload = (Upload) project.getUploads().get(1);
    	assertEquals("incorrect upload id", task.getUploadId(), upload.getUploadId());
    }
}
