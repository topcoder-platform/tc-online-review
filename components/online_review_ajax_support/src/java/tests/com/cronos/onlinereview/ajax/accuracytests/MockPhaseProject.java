/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import java.util.Date;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;

/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockPhaseProject extends Project {
    MockPhaseProject() {
        super(new Date(), new DefaultWorkdays());
    }
    public Phase[] getAllPhases() {
        MockPhase m1 = new MockPhase();
        MockPhase m2 = new MockPhase(1);
        return new Phase[]{m1, m2};
    }
}
