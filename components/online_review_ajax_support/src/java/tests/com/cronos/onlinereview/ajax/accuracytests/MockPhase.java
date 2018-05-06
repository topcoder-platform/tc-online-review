/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import java.util.Date;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockPhase extends Phase {
    private int id = -1;
    public MockPhase() {
        super(new Project(new Date(), new DefaultWorkdays()), 1);
    }

    public MockPhase(int id) {
        super(new Project(new Date(), new DefaultWorkdays()), id);
        this.id = id;
    }
    public PhaseType getPhaseType() {
        if (id == 1) {
            return new MockPhaseType(2);
        }
        return new MockPhaseType();
    }
    public long getId() {
        
        return 1;
    }
    public PhaseStatus getPhaseStatus() {
        return new MockPhaseStatus(1, "name");
    } 
    public Date getFixedStartDate() {
        return new Date();
    }
    public Date calcEndDate() {
        return new Date();
    }
}
