/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Scorecard;

/**
 * <p>
 * <code>ScorecardAdapter</code> extends
 * <code>com.topcoder.management.scorecard.data.Scorecard</code>, holds both
 * "group" model data and facility data which bridge the gap between
 * <code>Scorecard Data Structure</code> and the struts front-end mechanism.
 * </p>
 * 
 * @version 1.0
 * @author albertwang, flying2hk
 */
public class ScorecardAdapter extends Scorecard {
    /**
     * "Good" group count, used to eliminate dirty groups.
     */
    private int count;

    /**
     * <p>
     * Return the good group count.
     * </p>
     * 
     * @return the good group count
     */
    public int getCount() {
        return count;
    }

    /**
     * <p>
     * Set the good group count.
     * </p>
     * 
     * @param count
     *            the good group count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * <p>
     * Return group with given index.
     * </p>
     * 
     * @param index
     *            index
     * @return the group
     */
    public Group getAllGroups(int index) {
        if (index < this.getNumberOfGroups()) {
            return this.getGroup(index);
        } else {
            this.addGroup(new GroupAdapter());
            return getAllGroups(index);
        }
    }

    /**
     * <p>
     * Create an empty scorecard adapter.
     * </p>
     */
    public ScorecardAdapter() {
        super();
        this.count = 0;
    }

    /**
     * <p>
     * Create a ScorecardAdapter from given scorecard.
     * </p>
     * 
     * @param scorecard
     *            the scorecard
     */
    public ScorecardAdapter(Scorecard scorecard) {
        if (scorecard.getId() > 0) {
            this.setId(scorecard.getId());
        }
        this.setInUse(scorecard.isInUse());
        this.setMaxScore(scorecard.getMaxScore());
        this.setMinScore(scorecard.getMinScore());
        this.setName(scorecard.getName());
        this.setCategory(scorecard.getCategory());
        this.setScorecardStatus(scorecard.getScorecardStatus());
        this.setScorecardType(scorecard.getScorecardType());
        this.setVersion(scorecard.getVersion());
        Group[] gs = scorecard.getAllGroups();
        for (int i = 0; i < gs.length; i++) {
            this.addGroup(new GroupAdapter(gs[i]));
        }
        this.setCount(scorecard.getNumberOfGroups());
    }
}
