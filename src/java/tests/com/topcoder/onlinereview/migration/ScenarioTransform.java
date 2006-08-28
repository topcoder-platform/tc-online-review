/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.util.List;

import com.topcoder.onlinereview.migration.dto.newschema.scorecard.Scorecard;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardGroup;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardQuestionNew;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardSectionNew;

import junit.framework.TestCase;

/**
 * <p>
 * Scenarios involving "2.1.1.2	Transform Data":
 * </p>
 * <ul>
 * <li>Scenario #12 (FTC 2)</li>
 * <li>Scenario #13 (FTC 2) Score_card_group_id data type is not an Integer</li>
 * <li>Scenario #14 (FTC 2) Name data type is not a String</li>
 * <li>Scenario #14 (FTC 2) Name data type is not a String</li>
 * <li>Scenario #15 (FTC 2) Sort data type is not an Integer</li>
 * <li>Scenario #16 (FTC 2) Project_category_id data type is not an Integer</li>
 * <li>Scenario #17 (FTC 2) scorecard_question_id data type is not an Integer</li>
 * <li>Scenario #18 (FTC 2) description data type is not an Integer</li>
 * <li>Scenario #19 (FTC 2) guideline data type is not an Integer</li>
 * <li>Scenario #20 (FTC 2) scorecard_question_type_id data type is not an Integer</li>
 * <li>Scenario #21 (FTC 2) weight data type is not an Integer</li>
 * <li>Scenario #22 (FTC 2) Scorecard_type_id data type is not an Integer.</li>
 * <li>Scenario #23 (FTC 2) scorecard_section_id data type is not an Integer</li>
 * <li>Scenario #24 (FTC 2) scorecard_status_id data type is not an Integer</li>
 * <li>Scenario #25 (FTC 2) scorecard_id data type is not an Integer</li>
 * <li>Scenario #26 (FTC 2) Version  data type is not an Integer</li>
 * </ul>
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioTransform extends TestCase {
    /**
     * The configuration interface.
     */
    private static Configuration config = new Configuration(ScenarioTransform.class.getName());
	private DataMigrator dataMigrator = null;
	private List output = null;

    /**
     * <p>
     * Set up environment.
     * </p>
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
    	//Utility.getInstance().clearProjects();
    	//Utility.getInstance().clearScorecards();
    	dataMigrator = Utility.getInstance().getDataMigrator();    	
    }
    
    /**
     * This method ensure only one load/transform occurs since most of this scenario used to test persistence.
     * 
     * @return the data to store
     * @throws Exception if error occur
     */
    private List getOutput() throws Exception {
    	if (output == null) {
    		List input = this.dataMigrator.getScorecardLoader().loadScorecardTemplate();    		
    		output = this.dataMigrator.getScorecardTransformer().transformScorecardTemplates(input);
    	}
    	return output;
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
     * Scenario #12 (FTC 2)
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process for Data type was successful.
     * </p>
     * @throws Exception to JUnit
     */
    public void atestScenario12() throws Exception {
    	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    	Utility.getInstance().existAllScorecardEntities();
    }

    /**
     * <p>
     * Scenario #13 (FTC 2) Score_card_group_id data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario13() throws Exception {
    	// Change table to incorrect table
    	String origTableName = ScorecardGroup.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("Score_card_group_id.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("Score_card_group_id.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect since Score_card_group_id data type is not an Integer");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("Score_card_group_id.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #14 (FTC 2) Name data type is not a String
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario14() throws Exception {
    	// Change table to incorrect table
    	String origTableName = ScorecardGroup.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("Name.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("Name.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("Name.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #15 (FTC 2) Sort data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario15() throws Exception {
    	// Change table to incorrect table
    	String origTableName = ScorecardGroup.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("sort.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("sort.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("sort.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #16 (FTC 2) Project_category_id data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario16() throws Exception {
    	// Change table to incorrect table
    	String origTableName = Scorecard.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("project_category_id.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("project_category_id.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("project_category_id.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #17 (FTC 2) scorecard_question_id data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario17() throws Exception {
    	// Change table to incorrect table
    	String origTableName = ScorecardQuestionNew.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("scorecard_question_id.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("scorecard_question_id.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("scorecard_question_id.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #18 (FTC 2) description data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario18() throws Exception {
    	// Change table to incorrect table
    	String origTableName = ScorecardQuestionNew.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("description.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("description.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("description.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #19 (FTC 2) guideline data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario19() throws Exception {
    	// Change table to incorrect table
    	String origTableName = ScorecardQuestionNew.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("guideline.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("guideline.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("guideline.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #20 (FTC 2) scorecard_question_type_id data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario20() throws Exception {
    	// Change table to incorrect table
    	String origTableName = ScorecardQuestionNew.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("scorecard_question_type_id.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("scorecard_question_type_id.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("scorecard_question_type_id.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #21 (FTC 2) weight data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario21() throws Exception {
    	// Change table to incorrect table
    	String origTableName = ScorecardQuestionNew.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("weight.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("weight.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("weight.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #22 (FTC 2) Scorecard_type_id data type is not an Integer.
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario22() throws Exception {
    	// Change table to incorrect table
    	String origTableName = Scorecard.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("scorecard_type_id.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("scorecard_type_id.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("scorecard_type_id.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #23 (FTC 2) scorecard_section_id data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario23() throws Exception {
    	// Change table to incorrect table
    	String origTableName = ScorecardSectionNew.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("scorecard_section_id.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("scorecard_section_id.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("scorecard_section_id.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #24 (FTC 2) scorecard_status_id data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario24() throws Exception {
    	// Change table to incorrect table
    	String origTableName = Scorecard.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("scorecard_status_id.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("scorecard_status_id.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("scorecard_status_id.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #25 (FTC 2) scorecard_id data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario25() throws Exception {
    	// Change table to incorrect table
    	String origTableName = Scorecard.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("scorecard_id.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("scorecard_id.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("scorecard_id.tablename"));
    	}
    }

    /**
     * <p>
     * Scenario #26 (FTC 2) Version  data type is not an Integer
     * 1. Load scorecard data
     * 2. Transform
     * 3. Store tranformed data
     * Expected Outcome:
     * The migration process is incorrect.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario26() throws Exception {
    	// Change table to incorrect table
    	String origTableName = Scorecard.TABLE_NAME;
    	try {
    		ScorecardGroup.TABLE_NAME = config.getProperty("version.tablename");
    		Utility.getInstance().executeStatement(config.getProperty("version.ddl_statement")); 
        	this.dataMigrator.getScorecardPersistence().storeScorecard(this.getOutput());
    		fail("Exception is expect");
    	} catch(Exception e) {
    		// expect
    	} finally {
    		ScorecardGroup.TABLE_NAME = origTableName;
        	Utility.getInstance().drop(config.getProperty("version.tablename"));
    	}
    }
}
