/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.project;

/**
 * The CompForumXref DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class CompForumXref {
    /** Represents the comp_forum_xref table name. */
    public static final String TABLE_NAME = "comp_forum_xref";

    /** Represents comp_vers_id field name. */
    public static final String COMP_VERS_ID_NAME = "comp_vers_id";

    /** Represents forum_id field name. */
    public static final String FORUM_ID_NAME = "forum_id";
    private int forumId;

    /**
     * Returns the forumId.
     *
     * @return Returns the forumId.
     */
    public int getForumId() {
        return forumId;
    }

    /**
     * Set the forumId.
     *
     * @param forumId The forumId to set.
     */
    public void setForumId(int forumId) {
        this.forumId = forumId;
    }
}
