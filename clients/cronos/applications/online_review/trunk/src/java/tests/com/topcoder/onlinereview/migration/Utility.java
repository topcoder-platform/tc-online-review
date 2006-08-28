/*
 * Copyright (C) 2006 TopCoder, Inc. All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import com.topcoder.onlinereview.migration.dto.oldschema.ProjectOld;
import com.topcoder.onlinereview.migration.dto.oldschema.ScorecardOld;
import com.topcoder.onlinereview.migration.dto.oldschema.deliverable.SubmissionOld;
import com.topcoder.onlinereview.migration.dto.oldschema.deliverable.Testcase;
import com.topcoder.onlinereview.migration.dto.oldschema.phase.PhaseInstance;
import com.topcoder.onlinereview.migration.dto.oldschema.project.CompCatalog;
import com.topcoder.onlinereview.migration.dto.oldschema.project.CompForumXref;
import com.topcoder.onlinereview.migration.dto.oldschema.project.CompVersionDates;
import com.topcoder.onlinereview.migration.dto.oldschema.project.CompVersions;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.PaymentInfo;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.ProjectResult;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.RUserRole;
import com.topcoder.onlinereview.migration.dto.oldschema.resource.RboardApplication;
import com.topcoder.onlinereview.migration.dto.oldschema.review.AggResponse;
import com.topcoder.onlinereview.migration.dto.oldschema.review.AggReview;
import com.topcoder.onlinereview.migration.dto.oldschema.review.AggWorksheet;
import com.topcoder.onlinereview.migration.dto.oldschema.review.Appeal;
import com.topcoder.onlinereview.migration.dto.oldschema.review.FinalReview;
import com.topcoder.onlinereview.migration.dto.oldschema.review.FixItem;
import com.topcoder.onlinereview.migration.dto.oldschema.review.ScorecardQuestion;
import com.topcoder.onlinereview.migration.dto.oldschema.review.SubjectiveResp;
import com.topcoder.onlinereview.migration.dto.oldschema.review.TestcaseQuestion;
import com.topcoder.onlinereview.migration.dto.oldschema.screening.ScreeningResults;

import junit.framework.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * The utility class.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Utility {
    private static final String DELETE_REVIEW_ITEM_COMMENT = 
    	"DELETE FROM review_item_comment WHERE review_item_id in " +
        "	(select review_item_id from review_item ri" + 
        "		inner join review r on r.review_id = ri.review_id" +
        "		inner join resource res on res.resource_id = r.resource_id" + 
        "		and res.project_id = ?)";
    private static final String DELETE_REVIEW_COMMENT = 
    	"DELETE FROM review_comment WHERE review_id in " +
        "	(select review_id from review ri" + 
        "		inner join resource res on res.resource_id = ri.resource_id" +
        "		and res.project_id = ?)";
    private static final String DELETE_REVIEW_ITEM = 
    	"DELETE FROM review_item WHERE review_id in " +
        "	(select review_id from review ri" + 
        "		inner join resource res on res.resource_id = ri.resource_id" +
        "		and res.project_id = ?)";
    private static final String DELETE_REVIEW = 
    	"DELETE FROM review WHERE resource_id in " +
        "	(select resource_id from resource res" + 
        "		res.project_id = ?)";
    private static final String DELETE_SCREENING_RESULT = 
    	"DELETE FROM screening_result WHERE screening_task_id in " +
        "	(select screening_task_id from screening_task st" + 
        "		inner join upload u on u.upload_id = st.upload_id" +
        "		and u.project_id = ?)";
    private static final String DELETE_SCREENING_TASK = 
    	"DELETE FROM screening_task WHERE upload_id in " +
        "	(select upload_id from upload u" + 
        "		u.project_id = ?)";
    private static final String DELETE_NOTIFICATION = 
    	"DELETE FROM notification WHERE project_id = ?";
    private static final String DELETE_RESOURCE_SUBMISSION = 
    	"DELETE FROM resource_submission WHERE resource_id in " +
        "	(select resource_id from resource " + 
        "		project_id = ?)";
    private static final String DELETE_RESOURCE_INFO = 
    	"DELETE FROM resource_info WHERE resource_id in " +
        "	(select resource_id from resource " + 
        "		project_id = ?)";
    private static final String DELETE_SUBMISSION = 
    	"DELETE FROM submission WHERE upload_id in " +
        "	(select upload_id from upload " + 
        "		project_id = ?)";
    private static final String DELETE_UPLOAD = "DELETE FROM upload WHERE project_id = ?";
    private static final String DELETE_PHASE_CRITERIA = 
    	"DELETE FROM phase_criteria WHERE phase_id in " +
        "	(select phase_id from phase " + 
        "		project_id = ?)";
    private static final String DELETE_PHASE_DEPENDENCY = 
    	"DELETE FROM phase_dependency WHERE dependency_phase_id in " +
        "	(select phase_id from phase " + 
        "		project_id = ?)" + 
        "	OR dependent_phase_id in " +
        "	(select phase_id from phase " + 
        "		project_id = ?)";
    private static final String DELETE_PROJECT_AUDIT = "DELETE FROM project_audit WHERE project_id = ?";
    private static final String DELETE_PROJECT_INFO = "DELETE FROM project_info WHERE project_id = ?";
    private static final String DELETE_RESOURCE = "DELETE FROM resource WHERE project_id = ?";
    private static final String DELETE_PHASE = "DELETE FROM phase WHERE project_id = ?";
    private static final String DELETE_PROJECT = "DELETE FROM project WHERE project_id = ?";

    private static final String EXISTS_REVIEW_ITEM_COMMENT =
        "SELECT count(*) FROM review_item_comment WHERE review_item_id in " +
        "	(select review_item_id from review_item ri" + 
        "		inner join review r on r.review_id = ri.review_id" +
        "		inner join resource res on res.resource_id = r.resource_id" + 
        "		and res.project_id = ?)";

    private static final String EXISTS_REVIEW_COMMENT = 
    	"SELECT count(*) FROM review_comment WHERE review_id in " +
        "	(select review_id from review ri" + 
        "		inner join resource res on res.resource_id = ri.resource_id" +
        "		and res.project_id = ?)";

    private static final String EXISTS_REVIEW_ITEM = 
    	"SELECT count(*) FROM review_item WHERE review_id in " +
        "	(select review_id from review ri" + 
        "		inner join resource res on res.resource_id = ri.resource_id" +
        "		and res.project_id = ?)";

    private static final String EXISTS_REVIEW = 
    	"SELECT count(*) FROM review WHERE resource_id in " +
        "	(select resource_id from resource res" + 
        "		res.project_id = ?)";

    private static final String EXISTS_SCREENING_RESULT =
        "SELECT count(*) FROM screening_result WHERE screening_task_id in " +
        "	(select screening_task_id from screening_task st" + 
        "		inner join upload u on u.upload_id = st.upload_id" +
        "		and u.project_id = ?)";

    private static final String EXISTS_SCREENING_TASK = 
    	"SELECT count(*) FROM screening_task WHERE upload_id in " +
        "	(select upload_id from upload u" + 
        "		u.project_id = ?)";

    private static final String EXISTS_NOTIFICATION = "SELECT count(*) FROM notification WHERE project_id = ?";

    private static final String EXISTS_RESOURCE_SUBMISSION =
        "SELECT count(*) FROM resource_submission WHERE resource_id in " + 
        "	(select resource_id from resource " +
        "		project_id = ?)";

    private static final String EXISTS_RESOURCE_INFO = 
    	"SELECT count(*) FROM resource_info WHERE resource_id in " +
        "	(select resource_id from resource " + 
        "		project_id = ?)";

    private static final String EXISTS_SUBMISSION = 
    	"SELECT count(*) FROM submission WHERE upload_id in " +
        "	(select upload_id from upload " + 
        "		project_id = ?)";

    private static final String EXISTS_UPLOAD = 
    	"SELECT count(*) FROM upload WHERE project_id = ?";

    private static final String EXISTS_PHASE_CRITERIA = 
    	"SELECT count(*) FROM phase_criteria WHERE phase_id in " +
        "	(select phase_id from phase " + 
        "		project_id = ?)";

    private static final String EXISTS_PHASE_DEPENDENCY =
        "SELECT count(*) FROM phase_dependency WHERE dependency_phase_id in " + "	(select phase_id from phase " +
        "		project_id = ?)" + "	OR dependent_phase_id in " + "	(select phase_id from phase " + "		project_id = ?)";

    private static final String EXISTS_PROJECT_AUDIT = "SELECT count(*) FROM project_audit WHERE project_id = ?";
    private static final String EXISTS_PROJECT_INFO = "SELECT count(*) FROM project_info WHERE project_id = ?";
    private static final String EXISTS_RESOURCE = "SELECT count(*) FROM resource WHERE project_id = ?";
    private static final String EXISTS_PHASE = "SELECT count(*) FROM phase WHERE project_id = ?";
    private static final String EXISTS_PROJECT = "SELECT count(*) FROM project WHERE project_id = ?";
    private static final String EXISTS_SCORECARD = "SELECT count(*) FROM scorecard";
    private static final String EXISTS_SCORECARD_GROUP = "SELECT count(*) FROM scorecard_group";
    private static final String EXISTS_SCORECARD_SECTION = "SELECT count(*) FROM scorecard_section";
    private static final String EXISTS_SCORECARD_QUESTION = "SELECT count(*) FROM scorecard_question";

    /** To avoid overhead Utility is kept as a singleton. */
    private static Utility instance = null;

    /** The configuration interface. */
    private static Configuration config = new Configuration(DataMigrator.class.getName());
    public static int COMP_VERS_ID = 1;
    public static int ROOT_CATALOG_ID = 1;
    public static int SUBMITTER_ID = 1;
    public static int PRIMARY_SCREENER_ID = 2;
    public static int STRESS_REVIEWER_ID = 3;
    public static int FAILUER_REVIEWER_ID = 2;
    public static int ACCURACY_REVIEWER_ID = 4;
    public static int AGGREGATOR_ID = 2;
    public static int FINAL_REVIEWER_ID = 2;
    public static int PRODUCT_MANAGER = 5;
    public static int SCREENING_TEPMLATE_ID = 1;
    public static int REVIEW_TEPMLATE_ID = 2;
    public static final int PHASE_ID_SUBMISSION = 1;
    public static final int PHASE_ID_SCREENING = 2;
    public static final int PHASE_ID_REVIEW = 3;
    public static final int PHASE_ID_AGGREGATION = 4;
    public static final int PHASE_ID_AGGREGATION_REVIEW = 5;
    public static final int PHASE_ID_FINAL_FIXES = 6;
    public static final int PHASE_ID_FINAL_REVIEW = 7;
    public static final int PHASE_ID_COMPONENT_PREPARATION = 8;
    public static final int PHASE_ID_APPEALS = 9;
    public static final int PHASE_ID_APPEALS_RESPONSE = 10;
    public static final int CURRENT_PHASE_ID = PHASE_ID_FINAL_REVIEW;
    public static final int ROLE_ID_DESIGNER_DEVELOPER = 1;
    public static final int ROLE_ID_PRIMARY_SCREENER = 2;
    public static final int ROLE_ID_REVIEWER = 3;
    public static final int ROLE_ID_AGGREGATOR = 4;
    public static final int ROLE_ID_FINAL_REVIEWER = 5;
    public static final int ROLE_ID_PRODUCT_MANAGER = 6;
    public static final int ROLE_ID_REMOVED = 7;
    public static final int RESP_ID_STRESS = 1;
    public static final int RESP_ID_FAILURE = 2;
    public static final int RESP_ID_ACCURACY = 3;
    static final String SUBMISSION_URL_REMOVED = "removed submission url";
    static final String PASSED_SUBMISSION_URL = "normal submission url";
    static final String FINAL_FIX_SUBMISSION_URL = "final fix submission url";

    /**
     * The database connection. In order to avoid overhead without connection pooling, an open connection is kept with
     * the functional test cases. There will not be any threading issue between the test cases are all executed in a
     * single thread.
     */
    private Connection loaderConn = null;

    /**
     * The database connection. In order to avoid overhead without connection pooling, an open connection is kept with
     * the functional test cases. There will not be any threading issue between the test cases are all executed in a
     * single thread.
     */
    private Connection persistConn = null;

    /** The DataMigrator instance. It is used for testing. */
    private DataMigrator dataMigrator = null;
    private int responseTypeId = 1;
    private int aggApprovalId = 1;

    /**
     * Constructor. Establishes the connection here.
     *
     * @throws Exception if connection can not be established.
     */
    public Utility() throws Exception {
        persistConn = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName()).createConnection(config.getProperty(
                    "persist_conn_name"));
        persistConn.setAutoCommit(true);
        this.dataMigrator = new DataMigrator();
    }

    /**
     * Get the singleton instance of the utility.
     *
     * @return Exception if instance can not be created for the first time.
     *
     * @throws Exception if error occurs
     */
    public static Utility getInstance() throws Exception {
        if (instance == null) {
            instance = new Utility();
        }

        return instance;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DataMigrator getDataMigrator() {
        return this.dataMigrator;
    }

    /**
     * Clear all scorecards data from current migrated to persistence.
     *
     * @throws Exception if error occur
     */
    public void clearScorecards() throws Exception {
        String[] deleteStatements = {
                "delete from scorecard_question;", "delete from scorecard_section;", "delete from scorecard_group;",
                "delete from scorecard;"
            };
        Statement pstmt = persistConn.createStatement();

        for (int i = 0; i < deleteStatements.length; i++) {
            pstmt.addBatch(deleteStatements[i]);
        }

        pstmt.executeBatch();
    }

    /**
     * Clear all projects data from current migrated to persistence.
     *
     * @throws Exception if error occur
     */
    public void clearProjects() throws Exception {
        String[] deleteStatements = {
                "delete from review_item_comment;", "delete from review_comment;", "delete from review_item;",
                "delete from review;", "delete from screening_result;", "delete from screening_task;",
                "delete from notification;", "delete from resource_submission;", "delete from resource_info;",
                "delete from submission;", "delete from upload;", "delete from phase_criteria;",
                "delete from phase_dependency;", "delete from project_audit;", "delete from project_info;",
                "delete from resource;", "delete from phase;", "delete from project;"
            };
        Statement pstmt = persistConn.createStatement();

        for (int i = 0; i < deleteStatements.length; i++) {
            pstmt.addBatch(deleteStatements[i]);
        }

        pstmt.executeBatch();
    }

    /**
     * Delete project related data
     *
     * @param projectId projectId
     *
     * @throws Exception if error occurs
     */
    public void deleteProject(long projectId) throws Exception {
        deleteEntityByProjectId(DELETE_REVIEW_ITEM_COMMENT, projectId);
        deleteEntityByProjectId(DELETE_REVIEW_COMMENT, projectId);
        deleteEntityByProjectId(DELETE_REVIEW_ITEM, projectId);
        deleteEntityByProjectId(DELETE_REVIEW, projectId);
        deleteEntityByProjectId(DELETE_SCREENING_RESULT, projectId);
        deleteEntityByProjectId(DELETE_SCREENING_TASK, projectId);
        deleteEntityByProjectId(DELETE_NOTIFICATION, projectId);
        deleteEntityByProjectId(DELETE_RESOURCE_SUBMISSION, projectId);
        deleteEntityByProjectId(DELETE_RESOURCE_INFO, projectId);
        deleteEntityByProjectId(DELETE_SUBMISSION, projectId);
        deleteEntityByProjectId(DELETE_UPLOAD, projectId);
        deleteEntityByProjectId(DELETE_PHASE_CRITERIA, projectId);
        deleteEntityByProjectId(DELETE_PHASE_DEPENDENCY, projectId);
        deleteEntityByProjectId(DELETE_PROJECT_AUDIT, projectId);
        deleteEntityByProjectId(DELETE_PROJECT_INFO, projectId);
        deleteEntityByProjectId(DELETE_RESOURCE, projectId);
        deleteEntityByProjectId(DELETE_PHASE, projectId);
        deleteEntityByProjectId(DELETE_PROJECT, projectId);
    }

    /**
     * Verify if all entities exist after migration.
     *
     * @param projectId the migrated project id
     *
     * @throws Exception if error occurs
     */
    public void existAllProjectEntities(int projectId)
        throws Exception {
        Assert.assertTrue("Miss project entity", getEntityCount(EXISTS_PROJECT, projectId) > 0);
        Assert.assertTrue("Miss phase entity", getEntityCount(EXISTS_PHASE, projectId) > 0);
        Assert.assertTrue("Miss resource entity", getEntityCount(EXISTS_RESOURCE, projectId) > 0);
        Assert.assertTrue("Miss project_info", getEntityCount(EXISTS_PROJECT_INFO, projectId) > 0);
        Assert.assertTrue("Miss project_audit", getEntityCount(EXISTS_PROJECT_AUDIT, projectId) > 0);
        Assert.assertTrue("Miss phase dependency", getEntityCount(EXISTS_PHASE_DEPENDENCY, projectId) > 0);
        Assert.assertTrue("Miss phase criteria entity", getEntityCount(EXISTS_PHASE_CRITERIA, projectId) > 0);
        Assert.assertTrue("Miss upload entity", getEntityCount(EXISTS_UPLOAD, projectId) > 0);
        Assert.assertTrue("Miss submission entity", getEntityCount(EXISTS_SUBMISSION, projectId) > 0);
        Assert.assertTrue("Miss resource info entity", getEntityCount(EXISTS_RESOURCE_INFO, projectId) > 0);
        Assert.assertTrue("Miss resource submission entity", getEntityCount(EXISTS_RESOURCE_SUBMISSION, projectId) > 0);
        Assert.assertTrue("Miss notification entity", getEntityCount(EXISTS_NOTIFICATION, projectId) > 0);
        Assert.assertTrue("Miss screening task entity", getEntityCount(EXISTS_SCREENING_TASK, projectId) > 0);
        Assert.assertTrue("Miss screening result entity", getEntityCount(EXISTS_SCREENING_RESULT, projectId) > 0);
        Assert.assertTrue("Miss review entity", getEntityCount(EXISTS_REVIEW, projectId) > 0);
        Assert.assertTrue("Miss review_item entity", getEntityCount(EXISTS_REVIEW_ITEM, projectId) > 0);
        Assert.assertTrue("Miss review_comment entity, ", getEntityCount(EXISTS_REVIEW_COMMENT, projectId) > 0);
        Assert.assertTrue("Miss review_item_comment entity", getEntityCount(EXISTS_REVIEW_ITEM_COMMENT, projectId) > 0);
    }

    /**
     * Return entity count for given table and project id.
     *
     * @param statement the query statement
     * @param projectId the projectId
     *
     * @return the entity count
     *
     * @throws Exception if error occurs
     */
    public int getEntityCount(String statement, int projectId)
        throws Exception {
        PreparedStatement pstmt = persistConn.prepareStatement(statement);
        pstmt.setLong(1, projectId);

        ResultSet rs = pstmt.executeQuery();
        int count = 0;

        if (rs.next()) {
            count = rs.getInt(1);
        }

        close(rs);
        close(pstmt);

        return count;
    }

    /**
     * Verify if all entities exist after migration.
     *
     * @throws Exception if error occurs
     */
    public void existAllScorecardEntities() throws Exception {
        Assert.assertTrue("Miss scorecard", getEntityCount(EXISTS_SCORECARD) > 0);
        Assert.assertTrue("Miss scorecard group", getEntityCount(EXISTS_SCORECARD_GROUP) > 0);
        Assert.assertTrue("Miss scorecard section", getEntityCount(EXISTS_SCORECARD_SECTION) > 0);
        Assert.assertTrue("Miss scorecard question", getEntityCount(EXISTS_SCORECARD_QUESTION) > 0);
    }

    /**
     * Return entity count for given table.
     *
     * @param statement the query statement
     *
     * @return the entity count
     *
     * @throws Exception if error occurs
     */
    public int getEntityCount(String statement) throws Exception {
        Statement stmt = persistConn.createStatement();
        ResultSet rs = stmt.executeQuery(statement);
        int count = 0;

        if (rs.next()) {
            count = rs.getInt(1);
        }

        close(rs);
        close(stmt);

        return count;
    }

    /**
     * Drop given table.
     *
     * @param table table
     *
     * @throws Exception if error occurs
     */
    public void drop(String table) throws Exception {
        Statement stmt = persistConn.createStatement();
        stmt.execute("DROP TABLE " + table);
        close(stmt);
    }

    /**
     * Execute the statement with given sql statement.
     *
     * @param statement statement
     *
     * @throws Exception if error occurs
     */
    public void executeStatement(String statement) throws Exception {
        Statement stmt = persistConn.createStatement();
        stmt.execute(statement);
        close(stmt);
    }

    /**
     * Delete entities for given project and table.
     *
     * @param statement DOCUMENT ME!
     * @param projectId the projectId
     *
     * @throws Exception if error occurs
     */
    private void deleteEntityByProjectId(String statement, long projectId)
        throws Exception {
        PreparedStatement pstmt = persistConn.prepareStatement(statement);
        pstmt.setLong(1, projectId);
        pstmt.execute();
        close(pstmt);
    }

    /**
     * Close jdbc resource.
     *
     * @param obj jdbc resource
     */
    private void close(Object obj) {
        if (obj instanceof Statement) {
            try {
                ((Statement) obj).close();
            } catch (Exception e) {
            }
        }

        if (obj instanceof ResultSet) {
            try {
                ((ResultSet) obj).close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ProjectOld prepareProjectOld() {
        ProjectOld table = new ProjectOld();
        table.setProjectId(1);

        // 1 and 3 map to 'Active', 2 maps to 'Deleted', 4 maps to 'Completed'
        table.setProjectStatId(1);

        // 1 maps to 'Component Design' and 2 maps to 'Component Development'
        table.setProjectTypeId(1);
        table.setCompVersId(COMP_VERS_ID);
        table.setWinnerId(1);

        // 1 maps to 'On', 0 maps to 'Off'
        table.setAutoPilotInd(true);
        table.setNotes("test notes");
        table.setRatingDate(new Date());
        table.setCompleteDate(new Date());
        table.setModifyReason("Create");
        table.setCompVersions(prepareCompVersions(table.getCompVersId()));
        prepareProjectResult(table);

        CompForumXref cfx = new CompForumXref();
        cfx.setForumId(1);
        table.setCompForumXref(cfx);

        CompCatalog cc = new CompCatalog();
        cc.setComponentName("Test component name");
        cc.setRootCategoryId(ROOT_CATALOG_ID);
        table.setCompCatalog(cc);

        CompVersionDates cv = new CompVersionDates();
        cv.setPrice((float) 500);
        table.setCompVersionDates(cv);

        table.setPhaseInstanceId(CURRENT_PHASE_ID);
        table.setPhaseInstances(preparePhaseInstances(table.getPhaseInstanceId()));

        prepareRUserRoles(table);
        prepareAggWorksheet(table);

        prepareSubmissions(table);
        prepareTestcases(table);
        prepareRboardApplications(table);

        for (int i = 0; i < 3; i++) {
            table.addModifiyReason("modify reasion " + i);
        }

        return table;
    }

    /**
     * DOCUMENT ME!
     *
     * @param project project
     */
    public void prepareTestcases(ProjectOld project) {
        Testcase table = new Testcase();
        table.setReviewerId(ACCURACY_REVIEWER_ID);
        table.setCurVersion(true);
        table.setTestcaseUrl("accuracy/test_case.jar");
        project.addTestcase(table);

        table = new Testcase();
        table.setReviewerId(STRESS_REVIEWER_ID);
        table.setCurVersion(true);
        table.setTestcaseUrl("stress/test_case.jar");
        project.addTestcase(table);

        table = new Testcase();
        table.setReviewerId(FAILUER_REVIEWER_ID);
        table.setCurVersion(true);
        table.setTestcaseUrl("failure/test_case.jar");
        project.addTestcase(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param project project
     */
    public void prepareRboardApplications(ProjectOld project) {
        RboardApplication table = new RboardApplication();
        table.setUserId(ACCURACY_REVIEWER_ID);
        table.setCreateDate(getDate(2006, 6, 10, 12));
        table.setPhaseId(113);
        project.addRboardApplication(table);

        table = new RboardApplication();
        table.setUserId(STRESS_REVIEWER_ID);
        table.setCreateDate(getDate(2006, 6, 10, 12));
        table.setPhaseId(113);
        project.addRboardApplication(table);

        table = new RboardApplication();
        table.setUserId(FAILUER_REVIEWER_ID);
        table.setCreateDate(getDate(2006, 6, 10, 12));
        table.setPhaseId(113);
        project.addRboardApplication(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param project project
     */
    public void prepareSubmissions(ProjectOld project) {
        SubmissionOld table = new SubmissionOld();
        table.setProjectId(project.getProjectId());
        table.setSubmissionVId(1);
        table.setSubmissionId(1);
        table.setSubmitterId(SUBMITTER_ID);
        table.setCurVersion(false);
        table.setRemoved(true);
        table.setSubmissionUrl(SUBMISSION_URL_REMOVED);
        table.setAdvancedToReview(false);
        table.setSubmissionDate(new Date());
        table.setSubmissionType(1);
        project.addSubmission(table);
        prepareScreeningResults(table);

        table = new SubmissionOld();
        table.setProjectId(project.getProjectId());
        table.setSubmissionVId(2);
        table.setSubmissionId(1);
        table.setSubmitterId(SUBMITTER_ID);
        table.setCurVersion(true);
        table.setRemoved(false);
        table.setSubmissionUrl(PASSED_SUBMISSION_URL);
        table.setAdvancedToReview(true);
        table.setSubmissionDate(new Date());
        project.addSubmission(table);
        table.setSubmissionType(1);
        prepareScreeningResults(table);
        prepareScorecards(project, table);

        table = new SubmissionOld();
        table.setProjectId(project.getProjectId());
        table.setSubmissionVId(3);
        table.setSubmissionId(2);
        table.setSubmitterId(SUBMITTER_ID);
        table.setCurVersion(true);
        table.setRemoved(false);
        table.setSubmissionUrl(FINAL_FIX_SUBMISSION_URL);
        table.setSubmissionDate(new Date());
        table.setSubmissionType(2);
        project.addSubmission(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param project project
     */
    public void prepareProjectResult(ProjectOld project) {
        ProjectResult table = new ProjectResult();
        table.setOldRating(1527);
        table.setOldReliability((float) 1.00);
        table.setCreateDate(getDate(2006, 8, 12, 12));
        table.setRawScore((float) 85);
        table.setFinalScore((float) 90);
        table.setPlaced(1);
        table.setUserId(SUBMITTER_ID);
        table.setPassedReviewInd(true);
        project.addProjectResult(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param project project
     * @param parent parent
     */
    public void prepareScorecards(ProjectOld project, SubmissionOld parent) {
        // screen
        ScorecardOld table = new ScorecardOld();
        table.setProjectId(parent.getProjectId());
        table.setScorecardId(1);
        table.setScore((float) 80);
        table.setAuthorId(PRIMARY_SCREENER_ID);
        table.setSubmissionId(1);
        table.setCompleted(true);
        table.setTemplateId(SCREENING_TEPMLATE_ID);
        table.setScorecardType(1);

        prepareScorecardQuestions(project, table);
        parent.addScorecard(table);

        // failure reviewer
        table = new ScorecardOld();
        table.setProjectId(parent.getProjectId());
        table.setScorecardId(2);
        table.setScore((float) 86.34);
        table.setAuthorId(FAILUER_REVIEWER_ID);
        table.setSubmissionId(1);
        table.setCompleted(true);
        table.setTemplateId(REVIEW_TEPMLATE_ID);
        table.setScorecardType(2);

        prepareScorecardQuestions(project, table);
        parent.addScorecard(table);

        // stress reviewer
        table = new ScorecardOld();
        table.setProjectId(parent.getProjectId());
        table.setScorecardId(2);
        table.setScore((float) 74.34);
        table.setAuthorId(STRESS_REVIEWER_ID);
        table.setSubmissionId(1);
        table.setCompleted(true);
        table.setTemplateId(REVIEW_TEPMLATE_ID);
        table.setScorecardType(2);

        prepareScorecardQuestions(project, table);
        parent.addScorecard(table);

        // accuracy reviewer
        table = new ScorecardOld();
        table.setProjectId(parent.getProjectId());
        table.setScorecardId(2);
        table.setScore((float) 85.84);
        table.setAuthorId(ACCURACY_REVIEWER_ID);
        table.setSubmissionId(1);
        table.setCompleted(true);
        table.setTemplateId(REVIEW_TEPMLATE_ID);
        table.setScorecardType(2);

        prepareScorecardQuestions(project, table);
        parent.addScorecard(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param project project
     * @param scorecard scorecard
     */
    public void prepareScorecardQuestions(ProjectOld project, ScorecardOld scorecard) {
        // Common question
        ScorecardQuestion table = new ScorecardQuestion();
        table.setScorecardId(scorecard.getScorecardId());
        table.setQuestionId(1);
        table.setEvaluationId(1);
        table.setQTemplateVId(1);
        scorecard.addScorecardQuestion(table);

        if (table.getEvaluationId() == 0) {
            // it maybe test case
            // prepareLoadTestcaseQuestion(table);
        }

        prepareSubjectiveResp(project, table, scorecard.getScorecardType(), 1);
        prepareAppeal(table);

        // test case question
        table = new ScorecardQuestion();
        table.setScorecardId(scorecard.getScorecardId());
        table.setQuestionId(2);
        table.setEvaluationId(0);
        table.setQTemplateVId(2);
        scorecard.addScorecardQuestion(table);

        prepareTestCaseQuestion(table);
        prepareSubjectiveResp(project, table, scorecard.getScorecardType(), 2);
        prepareAppeal(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param question question
     */
    public void prepareTestCaseQuestion(ScorecardQuestion question) {
        TestcaseQuestion table = new TestcaseQuestion();
        table.setTotalPass(10);
        table.setTotalTests(12);
        question.addTestcaseQuestion(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param question question
     */
    public void prepareAppeal(ScorecardQuestion question) {
        Appeal table = new Appeal();
        table.setAppealerId(SUBMITTER_ID);
        table.setQuestionId(question.getQuestionId());
        table.setAppealText("appeal text");
        table.setSuccessful(true);
        table.setAppealResponse("it's ok");
        table.setRawEvaluationId(1);
        table.setRawTotalTests(12);
        table.setRawTotalPass(11);

        // add Appeal to question
        question.addAppeal(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param project project
     * @param question question
     * @param type type
     * @param subjectiveRespId subjectiveRespId
     */
    public void prepareSubjectiveResp(ProjectOld project, ScorecardQuestion question, int type, int subjectiveRespId) {
        SubjectiveResp table = new SubjectiveResp();
        table.setQuestionId(question.getQuestionId());
        table.setSubjectiveRespId(subjectiveRespId);

        // 1 maps to 'Required', 2 maps to 'Recommended', 3 maps to 'Comment'
        table.setResponseTypeId(responseTypeId);
        table.setResponseText("Required");

        // add SubjectiveResp to question
        question.addSubjectiveResp(table);

        // prepareLoad agg_response
        if ((type == 2) && (project.getAggWorksheet() != null)) {
            // Only care for agg response while it's review scorecard
            prepareLoadAggResponse(project, table);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param responseTypeId responseTypeId
     */
    public void setResponseTypeId(int responseTypeId) {
        responseTypeId = responseTypeId;
    }

    /**
     * Prepare AggResponse for given scorecard.
     *
     * @param old DOCUMENT ME!
     * @param parent the project
     */
    private void prepareLoadAggResponse(ProjectOld old, SubjectiveResp parent) {
        try {
            parent.setAggResponse(old.getAggWorksheet().getAggResponse(parent.getSubjectiveRespId()));
        } catch (Exception e) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param year year
     * @param month month
     * @param day day
     * @param hour hour
     *
     * @return DOCUMENT ME!
     */
    public Date getDate(int year, int month, int day, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR, hour);

        return (Date) cal.getTime().clone();
    }

    /**
     * DOCUMENT ME!
     *
     * @param submission submission
     */
    public void prepareScreeningResults(SubmissionOld submission) {
        ScreeningResults table = new ScreeningResults();
        table.setScreeningResponse(1);
        table.setDynamicResponseText("screening response text");
        submission.addScreeningResults(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param project project
     */
    public void prepareAggWorksheet(ProjectOld project) {
        AggWorksheet table = new AggWorksheet();
        table.setAggWorksheetId(1);
        table.setAggregatorId(AGGREGATOR_ID);
        table.setCompleted(true);

        prepareAggReview(table);
        prepareFinalReview(table);
        prepareAggResponses(table);

        project.setAggWorksheet(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param aggWorksheet aggWorksheet
     */
    public void prepareAggResponses(AggWorksheet aggWorksheet) {
        int length = 10;

        for (int i = 0; i < length; i++) {
            AggResponse table = new AggResponse();
            table.setResponseText("response " + i);

            // 1 maps to 'Accepted', 2 maps to 'Rejected', 3 maps to 'Duplicate'
            table.setAggRespStatId(1);
            table.setAggResponseId(i + 1);
            table.setSubjectiveRespId(i + 1);

            // Prepare prepareLoadFixItem
            FixItem fi = new FixItem();

            // 1 maps to 'Not Fixed', 2 maps to 'Fixed'
            fi.setFinalFixSId(2);
            table.setFixItem(fi);

            // add AggResponse to aggWorksheet
            aggWorksheet.addAggResponse(table);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param aggWorksheet aggWorksheet
     */
    public void prepareFinalReview(AggWorksheet aggWorksheet) {
        FinalReview table = new FinalReview();
        table.setCompleted(true);
        table.setComments("Final review comments");

        // 0 maps to 'Rejected' and 1 maps to 'Accepted'
        table.setApproved(true);

        // add FinalReview to aggWorksheet
        aggWorksheet.setFinalReview(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param aggApprovalId aggApprovalId
     */
    public void setAggApprovalId(int aggApprovalId) {
        aggApprovalId = aggApprovalId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param aggWorksheet aggWorksheet
     */
    public void prepareAggReview(AggWorksheet aggWorksheet) {
        // stress reviewer
        AggReview table = new AggReview();
        table.setReviewerId(STRESS_REVIEWER_ID);
        table.setAggReviewText("agg review text from stress reviewer");

        // 1 maps to 'Approved', 2 maps to 'Rejected'
        table.setAggApprovalId(aggApprovalId);

        aggWorksheet.addAggReview(table);

        // accuracy reviewer
        table = new AggReview();
        table.setReviewerId(ACCURACY_REVIEWER_ID);
        table.setAggReviewText("agg review text from accuracy reviewer");

        // 1 maps to 'Approved', 2 maps to 'Rejected'
        table.setAggApprovalId(1);

        aggWorksheet.addAggReview(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param project project
     */
    public void prepareRUserRoles(ProjectOld project) {
        // submitter
        RUserRole table = new RUserRole();
        table.setProjectId(project.getProjectId());
        table.setRRoleId(ROLE_ID_DESIGNER_DEVELOPER);
        table.setRRespId(0);
        table.setLoginId(SUBMITTER_ID);
        project.addRUserRole(table);

        // prmiary screener
        table = new RUserRole();
        table.setProjectId(project.getProjectId());
        table.setRRoleId(ROLE_ID_PRIMARY_SCREENER);
        table.setRRespId(0);
        table.setLoginId(PRIMARY_SCREENER_ID);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPayment(100);

        // 1 maps to 'No', 2 maps to 'Yes'
        paymentInfo.setPaymentStatId(1);
        table.setPaymentInfo(paymentInfo);
        project.addRUserRole(table);

        // stress reviewer
        table = new RUserRole();
        table.setProjectId(project.getProjectId());
        table.setRRoleId(ROLE_ID_REVIEWER);
        table.setRRespId(RESP_ID_STRESS);
        table.setLoginId(STRESS_REVIEWER_ID);

        paymentInfo = new PaymentInfo();
        paymentInfo.setPayment(100);

        // 1 maps to 'No', 2 maps to 'Yes'
        paymentInfo.setPaymentStatId(1);
        table.setPaymentInfo(paymentInfo);
        project.addRUserRole(table);

        // accuracy reviewer
        table = new RUserRole();
        table.setProjectId(project.getProjectId());
        table.setRRoleId(ROLE_ID_REVIEWER);
        table.setRRespId(RESP_ID_ACCURACY);
        table.setLoginId(ACCURACY_REVIEWER_ID);

        paymentInfo = new PaymentInfo();
        paymentInfo.setPayment(100);

        // 1 maps to 'No', 2 maps to 'Yes'
        paymentInfo.setPaymentStatId(1);
        table.setPaymentInfo(paymentInfo);
        project.addRUserRole(table);

        // failure reviewer
        table = new RUserRole();
        table.setProjectId(project.getProjectId());
        table.setRRoleId(ROLE_ID_REVIEWER);
        table.setRRespId(RESP_ID_FAILURE);
        table.setLoginId(FAILUER_REVIEWER_ID);

        paymentInfo = new PaymentInfo();
        paymentInfo.setPayment(100);

        // 1 maps to 'No', 2 maps to 'Yes'
        paymentInfo.setPaymentStatId(1);
        table.setPaymentInfo(paymentInfo);
        project.addRUserRole(table);

        // aggregator
        table = new RUserRole();
        table.setProjectId(project.getProjectId());
        table.setRRoleId(ROLE_ID_AGGREGATOR);
        table.setRRespId(0);
        table.setLoginId(AGGREGATOR_ID);

        paymentInfo = new PaymentInfo();
        paymentInfo.setPayment(100);

        // 1 maps to 'No', 2 maps to 'Yes'
        paymentInfo.setPaymentStatId(1);
        table.setPaymentInfo(paymentInfo);
        project.addRUserRole(table);

        // final reviewer
        table = new RUserRole();
        table.setProjectId(project.getProjectId());
        table.setRRoleId(ROLE_ID_FINAL_REVIEWER);
        table.setRRespId(0);
        table.setLoginId(FINAL_REVIEWER_ID);

        paymentInfo = new PaymentInfo();
        paymentInfo.setPayment(100);

        // 1 maps to 'No', 2 maps to 'Yes'
        paymentInfo.setPaymentStatId(1);
        table.setPaymentInfo(paymentInfo);
        project.addRUserRole(table);

        // project manager
        table = new RUserRole();
        table.setProjectId(project.getProjectId());
        table.setRRoleId(ROLE_ID_PRODUCT_MANAGER);
        table.setRRespId(0);
        table.setLoginId(PRODUCT_MANAGER);
        project.addRUserRole(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param currentPhaseId currentPhaseId
     *
     * @return DOCUMENT ME!
     */
    public List preparePhaseInstances(int currentPhaseId) {
        int[] phaseIds = { 1, 2, 3, 9, 10, 4, 5, 6, 7, 8 };
        List list = new ArrayList();

        for (int i = 1; i <= 10; i++) {
            PhaseInstance table = new PhaseInstance();
            table.setPhaseInstanceId(i);
            table.setPhaseId(phaseIds[i - 1]);
            table.setStartDate(new Date());
            table.setEndDate(new Date());

            // Set phase status id
            if (table.getPhaseInstanceId() < currentPhaseId) {
                table.setPhaseStatusId(PhaseInstance.PHASE_STATUS_CLOSED);
            } else if (table.getPhaseInstanceId() == currentPhaseId) {
                table.setPhaseStatusId(PhaseInstance.PHASE_STATUS_OPEN);
            } else {
                table.setPhaseStatusId(PhaseInstance.PHASE_STATUS_SCHEDULED);
            }

            if ((table.getPhaseId() == 2) || (table.getPhaseId() == 3)) {
                table.setTemplateId((table.getPhaseId() == 2) ? SCREENING_TEPMLATE_ID : REVIEW_TEPMLATE_ID);
            }

            list.add(table);
        }

        return list;
    }

    /**
     * DOCUMENT ME!
     *
     * @param compVersId compVersId
     *
     * @return DOCUMENT ME!
     */
    public CompVersions prepareCompVersions(int compVersId) {
        CompVersions table = new CompVersions();
        table.setCompVersId(compVersId);
        table.setComponentId(1);
        table.setVersion(1);
        table.setVersionText("1.1");

        // 111, 112, 113, 114
        table.setPhaseId(112);

        return table;
    }
}
