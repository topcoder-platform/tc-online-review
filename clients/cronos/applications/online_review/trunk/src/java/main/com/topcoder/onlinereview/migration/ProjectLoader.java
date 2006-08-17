/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * The Loader which is used to load project information.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ProjectLoader {
    private Connection conn = null;

    /**
     * Creates a new Loader object.
     *
     * @param conn the connection to persist data
     */
    public ProjectLoader(Connection conn) {
        this.conn = conn;
    }

    /**
     * Load project one by one.
     * 
     * @param projectId
     * @return
     * @throws SQLException
     */
    public List loadProject() throws SQLException {
		Util.start("loadProject");
    	List list = new ArrayList();
        //PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + ProjectOld.TABLE_NAME + " WHERE cur_version = 1 and " +
        //		ProjectOld.PROJECT_ID_NAME + " = ?");
    	PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + ProjectOld.TABLE_NAME + " WHERE cur_version = 1 order by project_id");
        //stmt.setInt(1, projectId);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
	        ProjectOld table = new ProjectOld();
	        table.setProjectId(rs.getInt(ProjectOld.PROJECT_ID_NAME));
	        table.setProjectStatId(rs.getInt(ProjectOld.PROJECT_STAT_ID_NAME));
	        table.setProjectTypeId(rs.getInt(ProjectOld.PROJECT_TYPE_ID_NAME));
	        table.setCompVersId(rs.getInt(ProjectOld.COMP_VERS_ID_NAME));
	        table.setWinnerId(rs.getInt(ProjectOld.WINNER_ID_NAME));
	        table.setAutoPilotInd(rs.getBoolean(ProjectOld.AUTOPILOT_IND_NAME));
	        table.setNotes(rs.getString(ProjectOld.NOTES_NAME));
	        table.setRatingDate(rs.getDate(ProjectOld.RATING_DATE_NAME));
	        table.setCompleteDate(rs.getDate(ProjectOld.COMPLETE_DATE_NAME));
	        table.setModifyReason(rs.getString(ProjectOld.MODIFY_REASON_NAME));
	        table.setCompVersions(getCompVersions(table.getCompVersId()));
	        table.setCompForumXref(getCompForumXref(table.getCompVersId()));
	
	        if (table.getCompVersions() != null) {
	            table.setCompCatalog(getCompCatalog(table.getCompVersions().getComponentId()));
	        }
	
	        table.setCompVersionDates(getCompVersionDates(table.getCompVersId(), table.getCompVersions().getPhaseId()));
	
	        table.setPhaseInstanceId(rs.getInt(ProjectOld.PHASE_INSTANCE_ID_NAME));
	        table.setPhaseInstances(getPhaseInstances(table.getProjectId(), table.getPhaseInstanceId()));
	        prepareRUserRoles(table);
	        prepareSubmissions(table);
	        prepareTestcases(table);
	        prepareProjectResults(table);
	        prepareRboardApplications(table);
	        prepareAggWorksheet(table);    
	        // Used for project_audit
	        prepareModifyReasons(table);
	        list.add(table);
        }

        Util.logAction(list.size(), "loadProject");
        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
        return list;
    }

    /**
     * Prepare modify reason for this project.
     * 
     * @param project the project
     * @throws SQLException if error occurs 
     */
    private void prepareModifyReasons(ProjectOld project) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT modify_reason FROM " + ProjectOld.TABLE_NAME + " WHERE " +
        		ProjectOld.PROJECT_ID_NAME + " = ?");
        stmt.setInt(1, project.getProjectId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
        	project.addModifiyReason(rs.getString(ProjectOld.MODIFY_REASON_NAME));
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);    	
    }
    
    /**
     * Load Project data to transform.
     *
     * @param compVersId the compVersId
     *
     * @return the loaded Project data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private CompVersions getCompVersions(int compVersId)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + CompVersions.TABLE_NAME + " WHERE " +
                CompVersions.COMP_VERS_ID_NAME + " = ?");
        stmt.setInt(1, compVersId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            CompVersions table = new CompVersions();
            table.setCompVersId(compVersId);
            table.setComponentId(rs.getInt(CompVersions.COMPONENT_ID_NAME));
            table.setVersion(rs.getInt(CompVersions.VERSION_NAME));
            table.setVersionText(rs.getString(CompVersions.VERSION_TEXT_NAME));
            table.setPhaseId(rs.getInt(CompVersions.PHASE_ID_NAME));

            return table;
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);

        return null;
    }

    /**
     * Load Project data to transform.
     *
     * @param compVersId the compVersId
     *
     * @return the loaded Project data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private CompForumXref getCompForumXref(int compVersId)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + CompForumXref.TABLE_NAME + " WHERE " +
                CompForumXref.COMP_VERS_ID_NAME + " = ? and forum_type = 2");
        stmt.setInt(1, compVersId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            CompForumXref table = new CompForumXref();
            table.setForumId(rs.getInt(CompForumXref.FORUM_ID_NAME));

            return table;
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);

        return null;
    }

    /**
     * Load Project data to transform.
     *
     * @param componentId the componentId
     *
     * @return the loaded Project data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private CompCatalog getCompCatalog(int componentId)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + CompCatalog.TABLE_NAME + " WHERE " +
                CompCatalog.COMPONENT_ID_NAME + " = ?");
        stmt.setInt(1, componentId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            CompCatalog table = new CompCatalog();
            table.setComponentName(rs.getString(CompCatalog.COMPONENT_NAME_NAME));
            table.setRootCategoryId(rs.getInt(CompCatalog.ROOT_CATEGORY_ID_NAME));

            return table;
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);

        return null;
    }

    /**
     * Load Project data to transform.
     *
     * @param compVersId the componentId
     *
     * @return the loaded Project data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private CompVersionDates getCompVersionDates(int compVersId, int phaseId)
        throws SQLException {
        // comp_vers_id and phase_id used to locate comp_version_dates
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + CompVersionDates.TABLE_NAME + " WHERE " +
                CompVersionDates.COMP_VERS_ID_NAME + " = ? and phase_id = ?" );
        stmt.setInt(1, compVersId);
        stmt.setInt(2, phaseId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            CompVersionDates table = new CompVersionDates();
            table.setPrice(rs.getFloat(CompVersionDates.PRICE_NAME));

            return table;
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);

        return null;
    }

    /**
     * Load PhaseInstances data to transform.
     *
     * @param projectId the projectId
     * @param currentPhaseId the currentPhaseId
     *
     * @return the loaded PhaseInstances data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private Collection getPhaseInstances(int projectId, int currentPhaseId)
        throws SQLException {
		Util.start("getPhaseInstances");
        List list = new ArrayList();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + PhaseInstance.TABLE_NAME + " WHERE " +
                PhaseInstance.PROJECT_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, projectId);

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            PhaseInstance table = new PhaseInstance();
            table.setPhaseInstanceId(rs.getInt(PhaseInstance.PHASE_INSTANCE_ID_NAME));
            table.setPhaseId(rs.getInt(PhaseInstance.PHASE_ID_NAME));
            table.setStartDate(rs.getDate(PhaseInstance.START_DATE_NAME));
            table.setEndDate(rs.getDate(PhaseInstance.END_DATE_NAME));

            // Set phase status id
            if (table.getPhaseInstanceId() < currentPhaseId) {
                table.setPhaseStatusId(PhaseInstance.PHASE_STATUS_CLOSED);
            } else if (table.getPhaseInstanceId() == currentPhaseId) {
                table.setPhaseStatusId(PhaseInstance.PHASE_STATUS_OPEN);
            } else {
                table.setPhaseStatusId(PhaseInstance.PHASE_STATUS_SCHEDULED);
            }

            // scorecard_type 1 = screening, 2 = review
            // phase id Screening: 3 Review: 4
            if ((table.getPhaseId() == 3) || (table.getPhaseId() == 4)) {
                int scorecardType = ((table.getPhaseId() == 3) ? 1 : 2);
                table.setTemplateId(getTemplateId(projectId, scorecardType));
            }

            list.add(table);
        }

        Util.logAction(list.size(), "getPhaseInstances");
        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);

        return list;
    }

    /**
     * Retrieve template id from project_template table.
     *
     * @param projectId the project id
     * @param scorecardType the scorecard type,  1 = screening, 2 = review
     *
     * @return template id, 0 if does not exist
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private int getTemplateId(int projectId, int scorecardType)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM project_template " + " WHERE " +
                PhaseInstance.PROJECT_ID_NAME + " = ? AND scorecard_type = ?");
        stmt.setInt(1, projectId);
        stmt.setInt(2, scorecardType);

        ResultSet rs = stmt.executeQuery();

        try {
            if (rs.next()) {
                return rs.getInt("template_id");
            }

            return 0;
        } finally {
            DatabaseUtils.closeResultSetSilently(rs);
            DatabaseUtils.closeStatementSilently(stmt);
        }
    }

    /**
     * Prepare rUserRoles for given project.
     *
     * @param project the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareRUserRoles(ProjectOld project)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + RUserRole.TABLE_NAME + " WHERE " +
                RUserRole.PROJECT_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, project.getProjectId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            RUserRole table = new RUserRole();
            table.setProjectId(project.getProjectId());
            table.setRUserRoleId(rs.getInt(RUserRole.R_USER_ROLE_ID_NAME));
            table.setRRoleId(rs.getInt(RUserRole.R_ROLE_ID_NAME));
            table.setRRespId(rs.getInt(RUserRole.R_RESP_ID_NAME));
            table.setLoginId(rs.getInt(RUserRole.LOGIN_ID_NAME));
            table.setPaymentInfo(preparePaymentInfo(rs.getInt("payment_info_id")));
            project.addRUserRole(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare Submissions for given project.
     *
     * @param project the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareSubmissions(ProjectOld project)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + SubmissionOld.TABLE_NAME + " WHERE " +
                SubmissionOld.PROJECT_ID_NAME + " = ?");
        stmt.setInt(1, project.getProjectId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            SubmissionOld table = new SubmissionOld();
            table.setProjectId(project.getProjectId());
            table.setSubmissionVId(rs.getInt(SubmissionOld.SUBMISSION_V_ID_NAME));
            table.setSubmissionId(rs.getInt(SubmissionOld.SUBMISSION_ID_NAME));
            table.setSubmitterId(rs.getInt(SubmissionOld.SUBMITTER_ID_NAME));
            table.setCurVersion(rs.getBoolean(SubmissionOld.CUR_VERSION_NAME));
            table.setRemoved(rs.getBoolean(SubmissionOld.IS_REMOVED_NAME));
            table.setSubmissionUrl(rs.getString(SubmissionOld.SUBMISSION_URL_NAME));
            table.setAdvancedToReview(rs.getBoolean(SubmissionOld.ADVANCED_TO_REVIEW_NAME));
            table.setSubmissionDate(rs.getDate(SubmissionOld.SUBMISSION_DATE_NAME));
            project.addSubmission(table);
            // Prepare screening results for every submission
            prepareScreeningResults(table);
            if (table.isCurVersion()) {
            	prepareScorecards(table);
            }
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare Testcases for given project.
     *
     * @param project the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareTestcases(ProjectOld project) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + Testcase.TABLE_NAME + " WHERE " +
                Testcase.PROJECT_ID_NAME + " = ?");
        stmt.setInt(1, project.getProjectId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            Testcase table = new Testcase();
            table.setProjectId(project.getProjectId());
            table.setTestcaseVId(rs.getInt(Testcase.TESTCASES_V_ID_NAME));
            table.setReviewerId(rs.getInt(Testcase.REVIEWER_ID_NAME));
            table.setCurVersion(rs.getBoolean(Testcase.CUR_VERSION_NAME));
            table.setTestcaseUrl(rs.getString(Testcase.TESTCASES_URL_NAME));
            project.addTestcase(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare ScreeningResults for given submission.
     *
     * @param project the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareScreeningResults(SubmissionOld submission)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * from screening_results where submission_v_id = ?");
        stmt.setInt(1, submission.getSubmissionVId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            ScreeningResults table = new ScreeningResults();
            table.setSubmissionVId(rs.getInt(ScreeningResults.SUBMISSION_V_ID_NAME));
            table.setScreeningResultsId(rs.getInt(ScreeningResults.SCREENING_RESULTS_ID_NAME));
            table.setScreeningResponse(rs.getInt(ScreeningResults.SCREENING_RESPONSE_ID_NAME));
            table.setDynamicResponseText(rs.getString(ScreeningResults.DYNAMIC_RESPONSE_NAME));
            submission.addScreeningResults(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare ProjectResult for given project.
     *
     * @param project the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareProjectResults(ProjectOld project)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + ProjectResult.TABLE_NAME + " WHERE " +
                Testcase.PROJECT_ID_NAME + " = ?");
        stmt.setInt(1, project.getProjectId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            ProjectResult table = new ProjectResult();
            table.setOldRating(rs.getInt(ProjectResult.OLD_RATING_NAME));
            table.setOldReliability(rs.getInt(ProjectResult.OLD_RELIABILITY_NAME));
            table.setCreateDate(rs.getDate(ProjectResult.CREATE_DATE_NAME));
            table.setRawScore(rs.getFloat(ProjectResult.RAW_SCORE_NAME));
            table.setFinalScore(rs.getFloat(ProjectResult.FINAL_SCORE_NAME));
            table.setPlaced(rs.getInt(ProjectResult.PLACED_NAME));
            table.setUserId(rs.getInt(ProjectResult.USER_ID_NAME));
            table.setPassedReviewInd(rs.getBoolean(ProjectResult.PASSED_REVIEW_IND_NAME));
            project.addProjectResult(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare PaymentInfo for given project.
     *
     * @param project the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private PaymentInfo preparePaymentInfo(int paymentInfoId)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "select * from payment_info where payment_info_id = ? and cur_version = 1");
        stmt.setInt(1, paymentInfoId);

        ResultSet rs = stmt.executeQuery();

        PaymentInfo table = null;
        if (rs.next()) {
            table = new PaymentInfo();
            table.setPayment(rs.getFloat(PaymentInfo.PAYMENT_NAME));
            table.setPaymentStatId(rs.getInt(PaymentInfo.PAYMENT_STAT_ID_NAME));
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
        return table;
    }

    /**
     * Prepare RboardApplication for given project.
     *
     * @param project the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareRboardApplications(ProjectOld project)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + RboardApplication.TABLE_NAME + " WHERE " +
        		ProjectOld.PROJECT_ID_NAME + " = ?");
        stmt.setInt(1, project.getProjectId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            RboardApplication table = new RboardApplication();
            table.setUserId(rs.getInt(RboardApplication.USER_ID_NAME));
            table.setCreateDate(rs.getDate(RboardApplication.CREATE_DATE_NAME));
            table.setPhaseId(rs.getInt(RboardApplication.PHASE_ID_NAME));
            project.addRboardApplication(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare Scorecard for given project.
     *
     * @param parent the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareScorecards(SubmissionOld parent)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + ScorecardOld.TABLE_NAME + " WHERE " +
        		ScorecardOld.PROJECT_ID_NAME + " = ? and submission_id = ? and cur_version = 1");
        stmt.setInt(1, parent.getProjectId());
        stmt.setInt(2, parent.getSubmissionId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            ScorecardOld table = new ScorecardOld();
            table.setProjectId(parent.getProjectId());
            table.setScorecardId(rs.getInt(ScorecardOld.SCORECARD_ID_NAME));
            table.setScore(rs.getFloat(ScorecardOld.SCORE_NAME));
            table.setAuthorId(rs.getInt(ScorecardOld.AUTHOR_ID_NAME));
            table.setSubmissionId(rs.getInt(ScorecardOld.SUBMISSION_ID_NAME));
            table.setCompleted(rs.getBoolean(ScorecardOld.IS_COMPLETED_NAME));
            int scorecardType = rs.getInt(ScorecardOld.SCORECARD_TYPE_NAME);
            table.setTemplateId(getTemplateId(parent.getProjectId(), scorecardType));
            table.setScorecardType(scorecardType);
            
            // prepare scorecard question
            prepareScorecardQuestions(table);
            
            parent.addScorecard(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare ScorecardQuestion for given scorecard.
     *
     * @param scorecard the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareScorecardQuestions(ScorecardOld scorecard)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + ScorecardQuestion.TABLE_NAME + " WHERE " +
        		ScorecardQuestion.SCORECARD_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, scorecard.getScorecardId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            ScorecardQuestion table = new ScorecardQuestion();
            table.setScorecardId(scorecard.getScorecardId());
            table.setQuestionId(rs.getInt(ScorecardQuestion.QUESTION_ID_NAME));
            table.setEvaluationId(rs.getInt(ScorecardQuestion.EVALUATION_ID_NAME));
            table.setQTemplateVId(rs.getInt(ScorecardQuestion.Q_TEMPLATE_V_ID_NAME));
            scorecard.addScorecardQuestion(table);

            if (table.getEvaluationId() == 0) {
            	// it maybe test case
            	prepareTestcaseQuestion(table);
                if (table.getTestcaseQuestions().size() == 0) {
                	// it should be bad data
                	continue;
                }
            }
            prepareSubjectiveResp(table, scorecard.getScorecardType());
            prepareAppeal(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare TestcaseQuestion for given scorecard.
     *
     * @param question the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareTestcaseQuestion(ScorecardQuestion question)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TestcaseQuestion.TABLE_NAME + " WHERE " +
        		TestcaseQuestion.QUESTION_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, question.getQuestionId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            TestcaseQuestion table = new TestcaseQuestion();
            table.setTotalPass(rs.getInt(TestcaseQuestion.TOTAL_PASS_NAME));
            table.setTotalTests(rs.getInt(TestcaseQuestion.TOTAL_TESTS_NAME));
            // intend to add TestcaseQuestion to question
            question.addTestcaseQuestion(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare SubjectiveResp for given scorecard.
     *
     * @param question the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareSubjectiveResp(ScorecardQuestion question, int scorecardType)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + SubjectiveResp.TABLE_NAME + " WHERE " +
        		SubjectiveResp.QUESTION_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, question.getQuestionId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            SubjectiveResp table = new SubjectiveResp();
            table.setQuestionId(question.getQuestionId());
            table.setSubjectiveRespId(rs.getInt(SubjectiveResp.SUBJECTIVE_RESP_ID_NAME));
            table.setResponseTypeId(rs.getInt(SubjectiveResp.RESPONSE_TYPE_ID_NAME));
            table.setResponseText(rs.getString(SubjectiveResp.RESPONSE_TEXT_NAME));
            // add SubjectiveResp to question
            question.addSubjectiveResp(table);
            // prepare agg_response
            if (scorecardType == 2) {
            	// Only care for agg response while it's review scorecard
            	prepareAggResponse(table);
            }
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare Appeal for given scorecard.
     *
     * @param question the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareAppeal(ScorecardQuestion question)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + Appeal.TABLE_NAME + " WHERE " +
        		Appeal.QUESTION_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, question.getQuestionId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            Appeal table = new Appeal();
            table.setAppealId(rs.getInt(Appeal.APPEAL_ID_NAME));
            table.setAppealerId(rs.getInt(Appeal.APPEALER_ID_NAME));
            table.setQuestionId(question.getQuestionId());
            table.setAppealText(rs.getString(Appeal.APPEAL_TEXT_NAME));
            table.setSuccessful(rs.getBoolean(Appeal.SUCCESSFUL_IND_NAME));
            table.setAppealResponse(rs.getString(Appeal.APPEAL_RESPONSE_NAME));
            table.setRawEvaluationId(rs.getInt(Appeal.RAW_EVALUATION_ID_NAME));
            table.setRawTotalTests(rs.getInt(Appeal.RAW_TOTAL_TESTS_NAME));
            table.setRawTotalPass(rs.getInt(Appeal.RAW_TOTLA_PASS_NAME));
            // add Appeal to question
            question.addAppeal(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare AggWorksheet for given project.
     *
     * @param project the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareAggWorksheet(ProjectOld project)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + AggWorksheet.TABLE_NAME + " WHERE " +
        		AggWorksheet.PROJECT_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, project.getProjectId());

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            AggWorksheet table = new AggWorksheet();
            table.setAggWorksheetId(rs.getInt(AggWorksheet.AGG_WORKSHEET_ID_NAME));
            table.setAggregatorId(rs.getInt(AggWorksheet.AGGREGATOR_ID_NAME));
            table.setCompleted(rs.getBoolean(AggWorksheet.IS_COMPLETED_NAME));
            
            // prepare agg_review
            prepareAggReview(table);
            // prepare final_review
            prepareFinalReview(table);

            project.setAggWorksheet(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare AggResponse for given scorecard.
     *
     * @param parent the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareAggResponse(SubjectiveResp parent)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + AggResponse.TABLE_NAME + " WHERE " +
        		AggResponse.SUBJECTIVE_RESP_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, parent.getSubjectiveRespId());

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            AggResponse table = new AggResponse();
            table.setResponseText(rs.getString(AggResponse.RESPONSE_TEXT_NAME));
            table.setAggRespStatId(rs.getInt(AggResponse.AGG_RESP_STAT_ID_NAME));
            table.setAggResponseId(rs.getInt(AggResponse.AGG_RESPONSE_ID_NAME));
            // add AggResponse to aggWorksheet
            parent.setAggResponse(table);
            
            // Prepare prepareFixItem
            prepareFixItem(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare AggReview for given scorecard.
     *
     * @param aggWorksheet the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareAggReview(AggWorksheet aggWorksheet)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + AggReview.TABLE_NAME + " WHERE " +
        		AggReview.AGG_WORKSHEET_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, aggWorksheet.getAggWorksheetId());

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            AggReview table = new AggReview();
            table.setAggReviewId(rs.getInt(AggReview.AGG_REVIEW_ID_NAME));
            table.setReviewerId(rs.getInt(AggReview.REVIEWER_ID_NAME));
            table.setAggWorksheetId(rs.getInt(AggReview.AGG_WORKSHEET_ID_NAME));
            table.setAggReviewText(rs.getString(AggReview.AGG_REVIEW_TEXT_NAME));
            table.setAggApprovalId(rs.getInt(AggReview.AGG_APPROVAL_ID_NAME));
            // add AggReview to aggWorksheet
            aggWorksheet.addAggReview(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare FinalReview for given scorecard.
     *
     * @param aggWorksheet the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareFinalReview(AggWorksheet aggWorksheet)
        throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + FinalReview.TABLE_NAME + " WHERE " +
        		FinalReview.AGG_WORKSHEET_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, aggWorksheet.getAggWorksheetId());

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            FinalReview table = new FinalReview();
            table.setFinalReviewId(rs.getInt(FinalReview.FINAL_REVIEW_ID_NAME));
            table.setCompleted(rs.getBoolean(FinalReview.IS_COMPLETED_NAME));
            table.setComments(rs.getString(FinalReview.COMMENTS_NAME));
            table.setApproved(rs.getBoolean(FinalReview.IS_APPROVED_NAME));

            // add FinalReview to aggWorksheet
            aggWorksheet.setFinalReview(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Prepare FixItem for given final review.
     *
     * @param aggWorksheet the project
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    private void prepareFixItem(AggResponse parent)
        throws SQLException {
    	if (parent.getAggRespStatId() != 1) {
    		// this response does not accept the subjective resp
    		return;
    	}
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + FixItem.TABLE_NAME + " WHERE " +
        		AggResponse.AGG_RESPONSE_ID_NAME + " = ? and cur_version = 1");
        stmt.setInt(1, parent.getAggResponseId());

        ResultSet rs = stmt.executeQuery();

        // Should map to final_review_id and agg_worksheet_id
        if(rs.next()) {
            FixItem table = new FixItem();
            table.setFinalFixSId(rs.getInt(FixItem.FINAL_FIX_S_ID_NAME));
            // add FixItem to finalReview
            parent.setFixItem(table);
        }

        DatabaseUtils.closeResultSetSilently(rs);
        DatabaseUtils.closeStatementSilently(stmt);
    }
}
