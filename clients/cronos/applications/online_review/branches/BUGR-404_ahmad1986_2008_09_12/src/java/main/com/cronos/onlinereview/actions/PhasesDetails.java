/*
 * Copyright (C) 2006-2007 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.List;

/**
 * TODO: Add docs for this class's methods.
 *
 * @author George1
 * @verion 1.0
 */
final class PhasesDetails {

    private PhaseGroup[] phaseGroups = null;

    private int[] phaseGroupIndexes = null;

    private Integer activeTabIndex = null;

    /**
     * Constructs a new instance of the <code>PhasesDetails</code> class setting all fields to their
     * default values.
     */
    public PhasesDetails() {
        // empty constructor
    }

    public PhaseGroup[] getPhaseGroups() {
        return (this.phaseGroups != null) ? this.phaseGroups : new PhaseGroup[0];
    }

    public void setPhaseGroup(PhaseGroup[] phaseGroups) {
        this.phaseGroups = phaseGroups;
    }

    public void setPhaseGroup(List phaseGroups) {
        this.phaseGroups = (PhaseGroup[]) phaseGroups.toArray(new PhaseGroup[phaseGroups.size()]);
    }

    public int[] getPhaseGroupIndexes() {
        return (this.phaseGroupIndexes != null) ? this.phaseGroupIndexes : new int[0];
    }

    public void setPhaseGroupIndexes(int[] phaseGroupIndexes) {
        this.phaseGroupIndexes = phaseGroupIndexes;
    }

    public Integer getActiveTabIndex() {
        return (this.activeTabIndex != null) ? this.activeTabIndex : new Integer(0);
    }

    public void setActiveTabIndex(Integer activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }

    public void setActiveTabIndex(int activeTabIndex) {
        this.activeTabIndex = new Integer((activeTabIndex >= 0) ? activeTabIndex : 0);
    }
}
