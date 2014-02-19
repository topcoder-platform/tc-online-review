/*
 * Copyright (C) 2006 - 2013 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.model;

import java.util.List;


/**
 * <p>This class defines a phase details bean.</p>
 * <p>
 * Thread safety: This class is not thread safe.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PhasesDetails {
    /**
     * Represents the phase groups.
     */
    private PhaseGroup[] phaseGroups = null;

    /**
     * Represents the phase group indexes.
     */
    private int[] phaseGroupIndexes = null;

    /**
     * Represents the active tab index.
     */
    private Integer activeTabIndex = null;

    /**
     * Constructs a new instance of the <code>PhasesDetails</code> class setting all fields to their
     * default values.
     */
    public PhasesDetails() {
        // empty constructor
    }

    /**
     * Gets the phase groups.
     *
     * @return the phase groups
     */
    public PhaseGroup[] getPhaseGroups() {
        return (this.phaseGroups != null) ? this.phaseGroups : new PhaseGroup[0];
    }

    /**
     * Sets the phase group.
     *
     * @param phaseGroups the new phase group
     */
    public void setPhaseGroup(PhaseGroup[] phaseGroups) {
        this.phaseGroups = phaseGroups;
    }

    /**
     * Sets the phase group.
     *
     * @param phaseGroups the new phase group
     */
    public void setPhaseGroup(List<PhaseGroup> phaseGroups) {
        this.phaseGroups = phaseGroups.toArray(new PhaseGroup[phaseGroups.size()]);
    }

    /**
     * Gets the phase group indexes.
     *
     * @return the phase group indexes
     */
    public int[] getPhaseGroupIndexes() {
        return (this.phaseGroupIndexes != null) ? this.phaseGroupIndexes : new int[0];
    }

    /**
     * Sets the phase group indexes.
     *
     * @param phaseGroupIndexes the new phase group indexes
     */
    public void setPhaseGroupIndexes(int[] phaseGroupIndexes) {
        this.phaseGroupIndexes = phaseGroupIndexes;
    }

    /**
     * Gets the active tab index.
     *
     * @return the active tab index
     */
    public Integer getActiveTabIndex() {
        return (this.activeTabIndex != null) ? this.activeTabIndex : 0;
    }

    /**
     * Sets the active tab index.
     *
     * @param activeTabIndex the new active tab index
     */
    public void setActiveTabIndex(Integer activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }

    /**
     * Sets the active tab index.
     *
     * @param activeTabIndex the new active tab index
     */
    public void setActiveTabIndex(int activeTabIndex) {
        this.activeTabIndex = (activeTabIndex >= 0) ? activeTabIndex : 0;
    }
}
