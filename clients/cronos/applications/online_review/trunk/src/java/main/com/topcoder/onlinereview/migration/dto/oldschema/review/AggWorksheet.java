/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.review;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The AggWorksheet dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class AggWorksheet {
    /** Represents the agg_worksheet table name. */
    public static final String TABLE_NAME = "agg_worksheet";

    /** Represents agg_worksheet_id field name. */
    public static final String AGG_WORKSHEET_ID_NAME = "agg_worksheet_id";

    /** Represents aggregator_id field name. */
    public static final String AGGREGATOR_ID_NAME = "aggregator_id";

    /** Represents is_completed field name. */
    public static final String IS_COMPLETED_NAME = "is_completed";

    /** Represents project_id field name. */
    public static final String PROJECT_ID_NAME = "project_id";
    private int aggWorksheetId;
    private int aggregatorId;
    private boolean isCompleted;
    private Collection aggReviews = new ArrayList();
    private Map aggResponses = new HashMap(); 
    private FinalReview finalReview;

    /**
     * Returns the aggregatorId.
     *
     * @return Returns the aggregatorId.
     */
    public int getAggregatorId() {
        return aggregatorId;
    }

    /**
     * Set the aggregatorId.
     *
     * @param aggregatorId The aggregatorId to set.
     */
    public void setAggregatorId(int aggregatorId) {
        this.aggregatorId = aggregatorId;
    }

    /**
     * Returns the aggWorksheetId.
     *
     * @return Returns the aggWorksheetId.
     */
    public int getAggWorksheetId() {
        return aggWorksheetId;
    }

    /**
     * Set the aggWorksheetId.
     *
     * @param aggWorksheetId The aggWorksheetId to set.
     */
    public void setAggWorksheetId(int aggWorksheetId) {
        this.aggWorksheetId = aggWorksheetId;
    }

    /**
     * Returns the isCompleted.
     *
     * @return Returns the isCompleted.
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Set the isCompleted.
     *
     * @param isCompleted The isCompleted to set.
     */
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * Returns the aggReviews.
     *
     * @return Returns the aggReviews.
     */
    public Collection getAggReviews() {
        return aggReviews;
    }

    /**
     * Set the aggReviews.
     *
     * @param aggReviews The aggReviews to set.
     */
    public void setAggReviews(Collection aggReviews) {
        this.aggReviews = aggReviews;
    }

    /**
     * add the aggReview.
     *
     * @param aggReview The aggReview to add.
     */
    public void addAggReview(AggReview aggReview) {
        aggReviews.add(aggReview);
    }

    /**
     * Returns the finalReviews.
     *
     * @return Returns the finalReviews.
     */
    public FinalReview getFinalReview() {
        return finalReview;
    }

    /**
     * add the finalReview.
     *
     * @param finalReview The finalReview to add.
     */
    public void setFinalReview(FinalReview finalReview) {
        this.finalReview = finalReview;
    }

    /**
     * Returns the aggResponses.
     *
     * @return Returns the aggResponses.
     */
    public Collection getAggResponses() {
        return aggResponses.keySet();
    }

    /**
     * add the aggResponse.
     *
     * @param aggResponse The aggResponse to add.
     */
    public void addAggResponse(AggResponse aggResponse) {
    	this.aggResponses.put(String.valueOf(aggResponse.getSubjectiveRespId()), aggResponse);
    }
    
    public AggResponse getAggResponse(int subjectiveRespId) {
    	return (AggResponse) this.aggResponses.get(String.valueOf(subjectiveRespId));
    }
}
