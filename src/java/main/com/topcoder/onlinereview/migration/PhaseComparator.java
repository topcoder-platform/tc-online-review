/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import com.topcoder.onlinereview.migration.dto.newschema.phase.Phase;

import java.util.Comparator;


/**
 * The PhaseComparator which is used to sort phase by phase status.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PhaseComparator implements Comparator {
    /**
     * Compare two phases against their status.
     *
     * @param arg0 first phase
     * @param arg1 second phase 
     *
     * @return -1 if phase1 is before phase 2, 0 if in same phase status, 1 otherwise
     */
    public int compare(Object arg0, Object arg1) {
        Phase phase1 = (Phase) arg0;
        Phase phase2 = (Phase) arg1;

        return phase1.getPhaseStatusId() - phase2.getPhaseStatusId();
    }
}
