/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.persistence;

import com.topcoder.onlinereview.migration.DataMigrator;
import com.topcoder.onlinereview.migration.DatabaseUtils;
import com.topcoder.onlinereview.migration.Util;
import com.topcoder.onlinereview.migration.dto.newschema.project.ProjectNew;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.Scorecard;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardGroup;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardQuestionNew;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardSectionNew;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * The Persistence is used to persist transformed data.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScorecardPersistence extends DatabaseUtils {
	private DataMigrator migrator = null;
	private Connection conn = null;

    /**
     * Creates a new Persistence object.
     *
     * @param conn the connection to persist data
     */
    public ScorecardPersistence(DataMigrator migrator) {
        this.migrator = migrator;
    }

    /**
     * Store scorecard data to new online review schema
     *
     * @param input the scorecard data
     *
     * @throws SQLException if error occurs while execute sql statement
     */
    public boolean storeScorecard(List input) throws Exception {
    	long startTime = Util.startMain("storeScorecard");
        String[] fieldnames = {
                "scorecard_id", "scorecard_status_id", "scorecard_type_id", "project_category_id", "name", "version",
                "min_score", "max_score", "create_user", "create_date", "modify_user", "modify_date"
            };

        // store scorecard data to new online review schema
        conn = migrator.getPersistenceConnection();
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(Scorecard.TABLE_NAME, fieldnames));

        boolean successful = true;
        conn.setAutoCommit(false);
        try {
	        for (Iterator iter = input.iterator(); iter.hasNext();) {
	            Scorecard table = (Scorecard) iter.next();
	            int i = 1;
	            stmt.setInt(i++, table.getScorecardId());
	            stmt.setInt(i++, table.getScorecardStatusId());
	            stmt.setInt(i++, table.getScorecardTypeId());
	            stmt.setInt(i++, table.getProjectCategoryId());
	            stmt.setString(i++, table.getName());
	            stmt.setString(i++, table.getVersion());
	            stmt.setFloat(i++, table.getMinScore());
	            stmt.setFloat(i++, table.getMaxScore());
	            stmt.setString(i++, table.getCreateUser());
	            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
	            stmt.setString(i++, table.getModifyUser());
	            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
	            stmt.execute();
	            storeScorecardGroup(table.getGroups());
	        }
            conn.commit();
        } catch(Exception e) {
        	successful = false;
        	conn.rollback();
        	Util.warn(e);
        }

        Util.logMainAction(input.size(), "storeScorecard", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
        return successful;
    }

    /**
     * Store scorecard data to new online review schema
     *
     * @param input the scorecard data
     *
     * @throws Exception if error occurs while execute sql statement
     */
    private void storeScorecardGroup(Collection input) throws Exception {
    	long startTime = Util.start("storeScorecardGroup");
        String[] fieldnames = {
                "scorecard_group_id", "scorecard_id", "name", "weight", "sort", "create_user", "create_date",
                "modify_user", "modify_date"
            };

        // store scorecard data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ScorecardGroup.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ScorecardGroup table = (ScorecardGroup) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getScorecardGroupId());
            stmt.setInt(i++, table.getScorecardId());
            stmt.setString(i++, table.getName());
            stmt.setFloat(i++, table.getWeight());
            stmt.setInt(i++, table.getSort());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            stmt.execute();
            storeScorecardSection(table.getSections());
        }

        Util.logAction(input.size(), "storeScorecardGroup", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store ScorecardSection data to new online review schema
     *
     * @param input the ScorecardSection data
     *
     * @throws Exception if error occurs while execute sql statement
     */
    private void storeScorecardSection(Collection input) throws Exception {
    	long startTime = Util.start("storeScorecardSection");
        String[] fieldnames = {
                "scorecard_section_id", "scorecard_group_id", "name", "weight", "sort", "create_user", "create_date",
                "modify_user", "modify_date"
            };

        // store ScorecardSection data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ScorecardSectionNew.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ScorecardSectionNew table = (ScorecardSectionNew) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getScorecardSectionId());
            stmt.setInt(i++, table.getScorecardGroupId());
            stmt.setString(i++, table.getName());
            stmt.setFloat(i++, table.getWeight());
            stmt.setInt(i++, table.getSort());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            stmt.execute();
            storeScorecardQuestion(table.getQuestions());
        }

        Util.logAction(input.size(), "storeScorecardSection", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }

    /**
     * Store ScorecardQuestion data to new online review schema
     *
     * @param input the ScorecardQuestion data
     *
     * @throws Exception if error occurs while execute sql statement
     */
    private void storeScorecardQuestion(Collection input) throws Exception {
    	long startTime = Util.start("storeScorecardQuestion");
        String[] fieldnames = {
                "scorecard_question_id", "scorecard_question_type_id", "scorecard_section_id", "description",
                "guideline", "weight", "sort", "upload_document", "upload_document_required", "create_user",
                "create_date", "modify_user", "modify_date"
            };

        // store ScorecardQuestion data to new online review schema
        PreparedStatement stmt = conn.prepareStatement(makeInsertSql(ScorecardQuestionNew.TABLE_NAME, fieldnames));

        for (Iterator iter = input.iterator(); iter.hasNext();) {
            ScorecardQuestionNew table = (ScorecardQuestionNew) iter.next();
            int i = 1;
            stmt.setInt(i++, table.getScorecardQuestionId());
            stmt.setInt(i++, table.getScorecardQuestionTypeId());
            stmt.setInt(i++, table.getScorecardSectionId());
            stmt.setString(i++, table.getDescription());
            stmt.setString(i++, table.getGuideline());
            stmt.setFloat(i++, table.getWeight());
            stmt.setInt(i++, table.getSort());
            stmt.setBoolean(i++, table.isUploadDocument());
            stmt.setBoolean(i++, table.isUploadDocumentRequired());
            stmt.setString(i++, table.getCreateUser());
            stmt.setDate(i++, new Date(table.getCreateDate().getTime()));
            stmt.setString(i++, table.getModifyUser());
            stmt.setDate(i++, new Date(table.getModifyDate().getTime()));
            stmt.execute();
        }

        Util.logAction(input.size(), "storeScorecardQuestion", startTime);
        DatabaseUtils.closeStatementSilently(stmt);
    }
}
