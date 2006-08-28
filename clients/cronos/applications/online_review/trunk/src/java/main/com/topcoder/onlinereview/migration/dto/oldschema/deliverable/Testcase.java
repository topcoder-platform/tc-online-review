/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.deliverable;

/**
 * The Testcase DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Testcase {
    /** Represents the testcases table name. */
    public static final String TABLE_NAME = "testcases";

    /** Represents testcases_v_id field name. */
    public static final String TESTCASES_V_ID_NAME = "testcases_v_id";

    /** Represents project_id field name. */
    public static final String PROJECT_ID_NAME = "project_id";

    /** Represents reviewer_id field name. */
    public static final String REVIEWER_ID_NAME = "reviewer_id";

    /** Represents cur_version field name. */
    public static final String CUR_VERSION_NAME = "cur_version";

    /** Represents testcases_url field name. */
    public static final String TESTCASES_URL_NAME = "testcases_url";
    private int testcaseVId;
    private int projectId;
    private int reviewerId;
    private boolean curVersion;
    private String testcaseUrl;

    /**
     * Returns the curVersion.
     *
     * @return Returns the curVersion.
     */
    public boolean isCurVersion() {
        return curVersion;
    }

    /**
     * Set the curVersion.
     *
     * @param curVersion The curVersion to set.
     */
    public void setCurVersion(boolean curVersion) {
        this.curVersion = curVersion;
    }

    /**
     * Returns the reviewerId.
     *
     * @return Returns the reviewerId.
     */
    public int getReviewerId() {
        return reviewerId;
    }

    /**
     * Set the reviewerId.
     *
     * @param reviewerId The reviewerId to set.
     */
    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    /**
     * Returns the testcaseUrl.
     *
     * @return Returns the testcaseUrl.
     */
    public String getTestcaseUrl() {
        return testcaseUrl;
    }

    /**
     * Set the testcaseUrl.
     *
     * @param testcaseUrl The testcaseUrl to set.
     */
    public void setTestcaseUrl(String testcaseUrl) {
        this.testcaseUrl = testcaseUrl;
    }
}
