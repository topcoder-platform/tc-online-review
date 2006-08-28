/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import junit.framework.TestCase;

/**
 * <p>
 * Scenarios involving "2.1.1.1	Select Data to Transform":
 * </p>
 * <p>
 * The requirement seems to indicate that the data can also be migrated by sections, for instance, you can pick to
 * migrate reviews but not projects.  That might be valid in the old system where there is no FK, but in the new
 * system all the data are keyed together.  It's just not possible to do that.
 * </p>
 * <ul>
 * <li>Scenario #1 (FTC 1)</li>
 * <li>Scenario #2 (FTC 1) Participants are not selected</li>
 * <li>Scenario #3 (FTC 1) Submission is not selected:</li>
 * <li>Scenario #4 (FTC 1) Screenings are not selected:</li>
 * <li>Scenario #5 (FTC 1) Permissions are not selected:</li>
 * <li>Scenario #6 (FTC 1) Scorecard data is not selected:</li>
 * <li>Scenario #7 (FTC 1) Scorecard metadata is not selected:</li>
 * <li>Scenario #8 (FTC 1) Appeal Data is not selected:</li>
 * <li>Scenario #9 (FTC 1) Payment table info is not selected:</li>
 * <li>Scenario #10 (FTC 1) Reviewers table is not selected:</li>
 * <li>Scenario #11 (FTC 1) Project  table info is not selected:</li>
 * </ul>
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioSelectDataToTransform extends TestCase { 
    /**
     * The configuration interface.
     */
    private static Configuration config = new Configuration(ScenarioSelectDataToTransform.class.getName());
	private DataMigrator dataMigrator = null;

    /**
     * <p>
     * Set up environment.
     * </p>
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
    	dataMigrator = Utility.getInstance().getDataMigrator();
    }
	
    /**
     * Close the jdbc resource in this dataMigrator.
     * 
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
    	dataMigrator.close();
    }

    /**
     * <p>
     * Scenario #1 (FTC 1):
     * 1. Load one project
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * Data migration all field contents are successful imported.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario1() throws Exception {
    	this.dataMigrator.migrateScorecard();

    	int projectId = Integer.parseInt(config.getProperty("project_id"));
    	this.dataMigrator.migrateProject();
    	Utility.getInstance().existAllProjectEntities(projectId);
    }
}
