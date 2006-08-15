/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.review;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * The ScorecardQuestion dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScorecardQuestion {
    /** Represents the scorecard_question table name. */
    public static final String TABLE_NAME = "scorecard_question";

    /** Represents question_id field name. */
    public static final String QUESTION_ID_NAME = "question_id";

    /** Represents scorecard_id field name. */
    public static final String SCORECARD_ID_NAME = "scorecard_id";

    /** Represents q_template_v_id field name. */
    public static final String Q_TEMPLATE_V_ID_NAME = "q_template_v_id";

    /** Represents evaluation_id field name. */
    public static final String EVALUATION_ID_NAME = "evaluation_id";
    private int questionId;
    private int scorecardId;
    private int qTemplateVId;
    private Collection subjectiveResps = new ArrayList();
    private Collection appeals = new ArrayList();
    private Collection testcaseQuestions = new ArrayList();
    
    /** maps the evaludation to answer, which could be 1/2/3/4 or Yes/No
     *  but from scorecard_question table, 
     */
    private int evaluationId;

    /**
     * Returns the evaluationId.
     *
     * @return Returns the evaluationId.
     */
    public int getEvaluationId() {
        return evaluationId;
    }

    /**
     * Set the evaluationId.
     *
     * @param evaluationId The evaluationId to set.
     */
    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    /**
     * Returns the templateVId.
     *
     * @return Returns the qTemplateVId.
     */
    public int getQTemplateVId() {
        return qTemplateVId;
    }

    /**
     * Set the templateVId.
     *
     * @param templateVId The qTemplateVId to set.
     */
    public void setQTemplateVId(int templateVId) {
        qTemplateVId = templateVId;
    }

    /**
     * Returns the questionId.
     *
     * @return Returns the questionId.
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * Set the questionId.
     *
     * @param questionId The questionId to set.
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    /**
     * Returns the scorecardId.
     *
     * @return Returns the scorecardId.
     */
    public int getScorecardId() {
        return scorecardId;
    }

    /**
     * Set the scorecardId.
     *
     * @param scorecardId The scorecardId to set.
     */
    public void setScorecardId(int scorecardId) {
        this.scorecardId = scorecardId;
    }

    /**
     * Returns the subjectiveResps.
     *
     * @return Returns the subjectiveResps.
     */
    public Collection getSubjectiveResps() {
        return subjectiveResps;
    }

    /**
     * Set the subjectiveResps.
     *
     * @param subjectiveResps The subjectiveResps to set.
     */
    public void setSubjectiveResps(Collection subjectiveResps) {
        this.subjectiveResps = subjectiveResps;
    }

    /**
     * add the subjectiveResp.
     *
     * @param subjectiveResp The subjectiveResp to add.
     */
    public void addSubjectiveResp(SubjectiveResp subjectiveResp) {
        if (this.subjectiveResps == null) {
            this.subjectiveResps = new ArrayList();
        }

        subjectiveResps.add(subjectiveResp);
    }

    /**
     * Returns the appeals.
     *
     * @return Returns the appeals.
     */
    public Collection getAppeals() {
        return appeals;
    }

    /**
     * Set the appeals.
     *
     * @param appeals The appeals to set.
     */
    public void setAppeals(Collection appeals) {
        this.appeals = appeals;
    }

    /**
     * add the appeal.
     *
     * @param appeal The appeal to add.
     */
    public void addAppeal(Appeal appeal) {
        if (this.appeals == null) {
            this.appeals = new ArrayList();
        }

        appeals.add(appeal);
    }

    /**
     * Returns the testcaseQuestions.
     *
     * @return Returns the testcaseQuestions.
     */
    public Collection getTestcaseQuestions() {
        return testcaseQuestions;
    }

    /**
     * Set the testcaseQuestions.
     *
     * @param testcaseQuestions The testcaseQuestions to set.
     */
    public void setTestcaseQuestions(Collection testcaseQuestions) {
        this.testcaseQuestions = testcaseQuestions;
    }

    /**
     * add the testcaseQuestion.
     *
     * @param testcaseQuestion The testcaseQuestion to add.
     */
    public void addTestcaseQuestion(TestcaseQuestion testcaseQuestion) {
        if (this.testcaseQuestions == null) {
            this.testcaseQuestions = new ArrayList();
        }

        testcaseQuestions.add(testcaseQuestion);
    }
    
    public boolean hasAggResponse() {
		for (Iterator iter = this.subjectiveResps.iterator(); iter.hasNext();) {
			SubjectiveResp instance = (SubjectiveResp) iter.next();
			if (instance.getAggResponse() != null) {
				return true;
			}
		}
		return false;
    }

    public boolean hasFinalFix() {
		for (Iterator iter = this.subjectiveResps.iterator(); iter.hasNext();) {
			SubjectiveResp instance = (SubjectiveResp) iter.next();
			if (instance.hasFinalFix()) {
				return true;
			}
		}
		return false;
    }
}
