/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

/**
 * The DefaultScorecard entity used to store default scorecard.
 *
 * @author brain_cn
 * @version 1.0
 */
public class DefaultScorecard {
	/** Project category id. */
    private int category;

	/** scorecard Type id. */
    private int scorecardType;

	/** scorecard id. */
    private long scorecardId;

	/** scorecard name id. */
    private String name;

    /**
     * Returns the category.
     *
     * @return Returns the category.
     */
    public int getCategory() {
        return category;
    }

    /**
     * Set category.
     *
     * @param category The category to set.
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * Returns the scorecard_id.
     *
     * @return Returns the scorecard_id.
     */
    public long getScorecardId() {
        return scorecardId;
    }

    /**
     * Set scorecardId.
     *
     * @param scorecardId The scorecardId to set.
     */
    public void setScorecardId(long scorecardId) {
        this.scorecardId = scorecardId;
    }

    /**
     * Returns the scorecard_type.
     *
     * @return Returns the scorecardType.
     */
    public int getScorecardType() {
        return scorecardType;
    }

    /**
     * Set scorecardType.
     *
     * @param scorecardType The scorecardType to set.
     */
    public void setScorecardType(int scorecardType) {
        this.scorecardType = scorecardType;
    }

	/**
	 * Returns the name.
	 * 
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name.
	 * 
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
