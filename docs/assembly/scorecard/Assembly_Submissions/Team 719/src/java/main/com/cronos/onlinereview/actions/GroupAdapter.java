/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Section;

/**
 * <p>
 * <code>GroupAdapter</code> extends
 * <code>com.topcoder.management.scorecard.data.Group</code>, holds both
 * "group" model data and facility data which bridge the gap between
 * <code>Scorecard Data Structure</code> and the struts front-end mechanism.
 * </p>
 * 
 * @version 1.0
 * @author albertwang, flying2hk
 */
public class GroupAdapter extends Group {
    /**
     * "Good" section count, used to eliminate dirty sections.
     */
    private int count;

    /**
     * <p>
     * Return section with given index.
     * </p>
     * 
     * @param index
     *            index
     * @return the section
     */
    public Section getAllSections(int index) {
        if (index < this.getNumberOfSections()) {
            return this.getSection(index);
        } else {
            this.addSection(new SectionAdapter());
            return getAllSections(index);
        }
    }

    /**
     * <p>
     * Return the good section count.
     * </p>
     * 
     * @return the good section count
     */
    public int getCount() {
        return count;
    }

    /**
     * <p>
     * Set the good section count.
     * </p>
     * 
     * @param count
     *            the good section count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * <p>
     * Create an empty group adapter.
     * </p>
     */
    public GroupAdapter() {
        super();
        this.count = 0;
    }

    /**
     * <p>
     * Create a GroupAdapter from given group.
     * </p>
     * 
     * @param group
     *            the group
     */
    public GroupAdapter(Group group) {
        if (group.getId() > 0) {
            this.setId(group.getId());
        }
        this.setName(group.getName());
        this.setWeight(group.getWeight());
        this.setCount(group.getNumberOfSections());
        Section[] ss = group.getAllSections();
        for (int i = 0; i < ss.length; i++) {
            this.addSection(new SectionAdapter(ss[i]));
        }
    }
}
