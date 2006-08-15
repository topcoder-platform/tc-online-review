/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.scorecard;

import java.util.ArrayList;
import java.util.Collection;


/**
 * The ScSectionGroup data object.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScSectionGroup {
    /** Represents the ScSectionGroup table name. */
    public static final String TABLE_NAME = "sc_section_group";

    /** Represents group_id field name. */
    public static final String GROUP_ID_NAME = "group_id";

    /** Represents group_name field name. */
    public static final String GROUP_NAME_NAME = "group_name";

    /** Represents group_seq_loc field name. */
    public static final String GROUP_SEQ_LOC_NAME = "group_seq_loc";
    private Collection sections = new ArrayList();
    private int groupId;
    private String groupName;
    private int groupSeqLoc;

    /**
     * Empty constructor.
     */
    public ScSectionGroup() {
    }

    /**
     * Returns the groupId.
     *
     * @return Returns the groupId.
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * Set the groupId.
     *
     * @param groupId The groupId to set.
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * Returns the groupName.
     *
     * @return Returns the groupName.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Set the groupName.
     *
     * @param groupName The groupName to set.
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Returns the groupSeqLoc.
     *
     * @return Returns the groupSeqLoc.
     */
    public int getGroupSeqLoc() {
        return groupSeqLoc;
    }

    /**
     * Set the groupSeqLoc.
     *
     * @param groupSeqLoc The groupSeqLoc to set.
     */
    public void setGroupSeqLoc(int groupSeqLoc) {
        this.groupSeqLoc = groupSeqLoc;
    }

    /**
     * Returns the sections.
     *
     * @return Returns the sections.
     */
    public Collection getSections() {
        return sections;
    }

    /**
     * Set the sections.
     *
     * @param sections The sections to set.
     */
    public void setSections(Collection sections) {
        this.sections = sections;
    }
}
