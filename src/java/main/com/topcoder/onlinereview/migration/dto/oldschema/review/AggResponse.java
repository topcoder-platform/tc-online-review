/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.review;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The AggResponse dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class AggResponse {
    /** Represents the agg_response table name. */
    public static final String TABLE_NAME = "agg_response";

    /** Represents agg_response_id field name. */
    public static final String AGG_RESPONSE_ID_NAME = "agg_response_id";

    /** Represents subjective_resp_id field name. */
    public static final String SUBJECTIVE_RESP_ID_NAME = "subjective_resp_id";

    /** Represents response_text field name. */
    public static final String RESPONSE_TEXT_NAME = "response_text";

    /** Represents agg_resp_stat_id field name. */
    public static final String AGG_RESP_STAT_ID_NAME = "agg_resp_stat_id";
    private String responseText;
    private int aggRespStatId;
    private int aggResponseId;
    private FixItem fixItem = null;

    /**
     * Returns the aggRespStatId.
     *
     * @return Returns the aggRespStatId.
     */
    public int getAggRespStatId() {
        return aggRespStatId;
    }

    /**
     * Set the aggRespStatId.
     *
     * @param aggRespStatId The aggRespStatId to set.
     */
    public void setAggRespStatId(int aggRespStatId) {
        this.aggRespStatId = aggRespStatId;
    }

    /**
     * Returns the responseText.
     *
     * @return Returns the responseText.
     */
    public String getResponseText() {
        return responseText;
    }

    /**
     * Set the responseText.
     *
     * @param responseText The responseText to set.
     */
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    /**
     * Returns the fixItems.
     *
     * @return Returns the fixItems.
     */
    public FixItem getFixItem() {
        return fixItem;
    }

    /**
     * add the fixItem.
     *
     * @param fixItem The fixItem to add.
     */
    public void setFixItem(FixItem fixItem) {
        this.fixItem = fixItem;
    }

	/**
	 * @return Returns the aggResponseId.
	 */
	public int getAggResponseId() {
		return aggResponseId;
	}

	/**
	 * @param aggResponseId The aggResponseId to set.
	 */
	public void setAggResponseId(int aggResponseId) {
		this.aggResponseId = aggResponseId;
	}
}
