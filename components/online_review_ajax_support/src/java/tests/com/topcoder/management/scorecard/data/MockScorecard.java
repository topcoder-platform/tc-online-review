/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.scorecard.data;

/**
 * Mock subclass of Scorecard.
 *
 * @author assistant
 * @version 1.0
 */
public class MockScorecard extends Scorecard {

    /**
     * The id.
     */
    private long id;

    /**
     * The card status.
     */
    private ScorecardStatus scorecardStatus;

    /**
     * Get id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id.
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the status.
     * @return the status
     */
    public ScorecardStatus getScorecardStatus() {
        return scorecardStatus;
    }

    /**
     * Set the status.
     * @param scorecardStatus the status
     */
    public void setScorecardStatus(ScorecardStatus scorecardStatus) {
        this.scorecardStatus = scorecardStatus;
    }
}
