/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.scorecard.data.ScorecardStatus;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockScorecardStatus extends ScorecardStatus {

    private int id = -1;
    public MockScorecardStatus(int id) {
        this.id = id;
    }
    public String getName() {
        if (id == 1) {
            return "Active";
        } else if (id == 2) {
            return "Inactive";
        }
        return null;
    }
}
