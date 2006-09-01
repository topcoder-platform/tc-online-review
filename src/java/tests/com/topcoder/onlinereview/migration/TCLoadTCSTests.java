/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import com.topcoder.shared.util.dwload.TCLoadTCS;

/**
 * The test of TCLoadTCS.
 *
 * @author brain_cn
 * @version 1.0
 */
public class TCLoadTCSTests extends BaseTests {
    private TCLoadTCS tcload = null;

    /**
     * Create connection for testing.
     *
     * @throws Exception if error occurs
     */
    protected void setUp() throws Exception {
        super.setUp();
        tcload = new TCLoadTCS();
        tcload.setSourceConnection(this.getSourceConn());
        tcload.setTargetConnection(this.getTargetConn());
    }

    /**
     * Release jdbc resource.
     *
     * @throws Exception if error occurs
     */
    protected void tearDown() throws Exception {
        tcload.closeDBConnections();
        super.tearDown();
    }

    /**
     * This test case verify other non-modified part should go through without error
     * @throws Exception
     */
    public void testNoError() throws Exception {
    	tcload.performLoad();
    }
    
    /**
     * Test method for 'TCLoadTCS.doLoadProjects()'
     *
     * @throws Exception error occurs
     */
    public void testDoLoadProjects() throws Exception {
        tcload.doLoadProjects();

        String tableName = "project";
        String sql = "select * from " + tableName + " where project_id = " + PROJECT_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }

    /**
     * Test method for 'TCLoadTCS.doLoadProjectResults()'
     *
     * @throws Exception error occurs
     */
    public void atestDoLoadProjectResults() throws Exception {
        tcload.doLoadProjectResults();

        String tableName = "project_result";
        String sql = "select * from " + tableName + " where project_id = " + PROJECT_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }

    /**
     * Test method for 'TCLoadTCS.doLoadSubmissionReview()'
     *
     * @throws Exception error occurs
     */
    public void testDoLoadSubmissionReview() throws Exception {
        tcload.doLoadSubmissionReview();

        String tableName = "submission_review";
        String sql = "select * from " + tableName + " where project_id = " + PROJECT_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }

    /**
     * Test method for 'TCLoadTCS.doLoadSubmissionScreening()'
     *
     * @throws Exception error occurs
     */
    public void testDoLoadSubmissionScreening() throws Exception {
        tcload.doLoadSubmissionScreening();

        String tableName = "submission_screening";
        String sql = "select * from " + tableName + " where project_id = " + PROJECT_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }

    /**
     * Test method for 'TCLoadTCS.doLoadScorecardTemplate()'
     *
     * @throws Exception error occurs
     */
    public void testDoLoadScorecardTemplate() throws Exception {
        tcload.doLoadScorecardTemplate();

        String tableName = "scorecard_template";
        String sql = "select * from " + tableName + " where scorecard_template_id = " + SCORECARD_TEMPLATE_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }

    /**
     * Test method for 'TCLoadTCS.doLoadScorecardQuestion()'
     *
     * @throws Exception error occurs
     */
    public void testDoLoadScorecardQuestion() throws Exception {
        tcload.doLoadScorecardQuestion();

        String tableName = "scorecard_question";
        String sql = "select * from " + tableName + " where scorecard_template_id = " + SCORECARD_TEMPLATE_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }

    /**
     * Test method for 'TCLoadTCS.doLoadScorecardResponse()'
     *
     * @throws Exception error occurs
     */
    public void testDoLoadScorecardResponse() throws Exception {
        tcload.doLoadScorecardResponse();

        String tableName = "scorecard_response";
        String sql = "select * from " + tableName + " where project_id = " + PROJECT_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }

    /**
     * Test method for 'TCLoadTCS.doLoadTestcaseResponse()'
     *
     * @throws Exception error occurs
     */
    public void testDoLoadTestcaseResponse() throws Exception {
        tcload.doLoadTestcaseResponse();

        String tableName = "testcase_response";
        String sql = "select * from " + tableName + " where project_id = " + PROJECT_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }

    /**
     * Test method for 'TCLoadTCS.doLoadSubjectiveResponse()'
     *
     * @throws Exception error occurs
     */
    public void testDoLoadSubjectiveResponse() throws Exception {
        tcload.doLoadSubjectiveResponse();

        String tableName = "subjective_response";
        String sql = "select * from " + tableName + " where project_id = " + PROJECT_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }

    /**
     * Test method for 'TCLoadTCS.doLoadAppeal()'
     *
     * @throws Exception error occurs
     */
    public void testDoLoadAppeal() throws Exception {
        tcload.doLoadAppeal();

        String tableName = "appeal";
        String sql = "select * from " + tableName + " where project_id = " + PROJECT_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }

    /**
     * Test method for 'TCLoadTCS.doLoadTestcaseAppeal()'
     *
     * @throws Exception error occurs
     */
    public void testDoLoadTestcaseAppeal() throws Exception {
        tcload.doLoadTestcaseAppeal();

        String tableName = "testcase_appeal";
        String sql = "select * from " + tableName + " where project_id = " + PROJECT_ID;
        this.assertData(tableName, "data/expected/dw_results.xml", sql, tableName + " load is incorrect");
    }
}
