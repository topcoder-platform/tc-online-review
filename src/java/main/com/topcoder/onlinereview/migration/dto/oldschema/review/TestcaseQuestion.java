/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.review;

/**
 * The TestcaseQuestion DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class TestcaseQuestion {
    /** Represents the testcase_question table name. */
    public static final String TABLE_NAME = "testcase_question";

    /** Represents total_tests field name. */
    public static final String TOTAL_TESTS_NAME = "total_tests";

    /** Represents total_pass field name. */
    public static final String TOTAL_PASS_NAME = "total_pass";

    /** Represents question_id field name. */
    public static final String QUESTION_ID_NAME = "question_id";
    private int totalPass;
    private int totalTests;

    /**
     *Returns the totalPass.
     *
     * @return Returns the totalPass.
     */
    public int getTotalPass() {
        return totalPass;
    }

    /**
     *Returns the totalPass.
     *
     * @param totalPass The totalPass to set.
     */
    public void setTotalPass(int totalPass) {
        this.totalPass = totalPass;
    }

    /**
     *Returns the totalTests.
     *
     * @return Returns the totalTests.
     */
    public int getTotalTests() {
        return totalTests;
    }

    /**
     *Returns the totalTests.
     *
     * @param totalTests The totalTests to set.
     */
    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }
}
