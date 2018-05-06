/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.project.phases.PhaseType;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockPhaseType extends PhaseType {

    private int identifier = 0;
    public MockPhaseType(int identifier) {
        super(identifier, "name");
        this.identifier = identifier;
    }
    
    public MockPhaseType() {
        super(1, "name");
    }
    
    public String getName() {
        if (identifier == 0) {
            return "Review";
        } else if (identifier == 1) {
            return "Appeal";
        } else if (identifier == 4) {
            return "Appeals";
        } else if (identifier == 2) {
            return "Appeals Response";
        } else if (identifier == 3) {
            return "Open";
        }
        return null;
    }
    
    public long getId() {
        if (identifier == 0) {
            return 999990;
        } else if (identifier == 1) {
            return 999991;
        } else if (identifier == 2) {
            return 999992;
        } else if (identifier == 3) {
            return 999993;
        } else if (identifier == 4) {
            return 999990;
        }
        return 0;
    }
}
