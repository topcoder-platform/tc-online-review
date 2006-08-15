/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.review;

/**
 * The FixItem DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class FixItem {
    /** Represents the fix_item table name. */
    public static final String TABLE_NAME = "fix_item";

    /** Represents final_fix_s_id field name. */
    public static final String FINAL_FIX_S_ID_NAME = "final_fix_s_id";

    /** Represents final_review_id field name. */
    public static final String FINAL_REVIEW_ID_NAME = "final_review_id";
    private int finalFixSId;

    /**
     * Returns the finalFixSId.
     *
     * @return Returns the finalFixSId.
     */
    public int getFinalFixSId() {
        return finalFixSId;
    }

    /**
     * Set the finalFixSId.
     *
     * @param finalFixSId The finalFixSId to set.
     */
    public void setFinalFixSId(int finalFixSId) {
        this.finalFixSId = finalFixSId;
    }
}
