/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.util.Iterator;

import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectNew;
import com.topcoder.onlinereview.migration.dto.newschema.review.Review;
import com.topcoder.onlinereview.migration.dto.oldschema.ProjectOld;
import com.topcoder.onlinereview.migration.dto.oldschema.deliverable.SubmissionOld;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.RUserRole;

import junit.framework.TestCase;

/**
 * <p>
 * Scenarios involving "2.1.1.2	Transform Data":
 * </p>
 * <ul> 
 * <li>Scenario #64 (FTC 4) Both Review scorecards have the same entries as shown in table.</li>
 * <li>Scenario #65 (FTC 4) The Scorecard in the New OR system is missing the Reviewer Name (indicated red)</li>
 * <li>Scenario #66 (FTC 4) The Scorecard in the New OR system has different ratings (indicated red)</li>
 * <li>Scenario #67 (FTC 4) The Scorecard in the New OR system has not ratings or only few ratings</li>
 * <li>Scenario #68 (FTC 4) The Scorecard in the New OR system has no questions</li>
 * </ul>
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioStoreTransformData extends TestCase {
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
    	Utility.getInstance().clearProjects();
    	Utility.getInstance().clearScorecards();
    }

    /**
     * <p>
     * Scenario #64 (FTC 4) Both Review scorecards have the same entries as shown in table.
     * 1. Load project data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process for Data type was successful.
     * </p>
     * @throws Exception to JUnit
     */
    public void atestScenario64() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	SubmissionOld sub = (SubmissionOld) old.getSubmissions().get(0); 
    	sub.setSubmissionUrl("bad url");
    	
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	// aggregator score should be 
    	// 86.34	74.34	85.84	82.18
    	Review review = (Review) project.getReviews().get(4);
    	assertEquals("The average score is not correct", review.getScore(), 82.18, 1e-9);
    }

    /**
     * <p>
     * Scenario #65 (FTC 4) The Scorecard in the New OR system is missing the Reviewer Name (indicated red)
     * 1. Load project data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The storing process went wrong.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario65() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	// Remove three reviewer
    	for (Iterator iter = old.getRUserRoles().iterator(); iter.hasNext();) {
    		RUserRole role = (RUserRole) iter.next();
    		if (role.getRRoleId() == 3) {
    			iter.remove();
    		}
    	}
    	
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	assertTrue("Storing process went wrong", project.getReviews().size() < 6);
    }

    /**
     * <p>
     * Scenario #66 (FTC 4) The Scorecard in the New OR system has different ratings (indicated red)
     * 1. Load project data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The storing process went wrong.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario66() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();
    	
    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	try {
    		this.dataMigrator.getProjectPersistence().storeProject(project);
    		fail("Storing process should went wrong");
    	} catch(Exception e) {
    		// It's ok
    	} finally {
    		Utility.getInstance().deleteProject(project.getProjectId());
    	}
    }

    /**
     * <p>
     *Scenario #67 (FTC 4) The Scorecard in the New OR system has not ratings or only few ratings.
     * 1. Load project data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The storing process went wrong.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario67() throws Exception {
    	ProjectOld old = Utility.getInstance().prepareProjectOld();

    	ProjectNew project = dataMigrator.getProjectTransformer().transformProject(old);
    	try {
    		this.dataMigrator.getProjectPersistence().storeProject(project);
    		fail("Storing process should went wrong");
    	} catch(Exception e) {
    		// It's ok
    	} finally {
    		Utility.getInstance().deleteProject(project.getProjectId());
    	}
    }
}
