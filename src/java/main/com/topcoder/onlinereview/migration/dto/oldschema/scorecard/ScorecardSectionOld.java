/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.scorecard;

import java.util.ArrayList;
import java.util.Collection;


/**
 * The ScorecardSectionOld dto.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScorecardSectionOld {
    /** Represents the scorecard_section table name. */
    public static final String TABLE_NAME = "scorecard_section";

    /** Represents section_id field name. */
    public static final String SECTION_ID_NAME = "section_id";

    /** Represents section_name field name. */
    public static final String SECTION_NAME_NAME = "section_name";

    /** Represents section_weight field name. */
    public static final String SECTION_WEIGHT_NAME = "section_weight";

    /** Represents section_seq_loc field name. */
    public static final String SECTION_SEQ_LOC_NAME = "section_seq_loc";
    private Collection questions = new ArrayList();
    private int sectionId;
    private String sectionName;
    private int sectionWeight;
    private int sectionSeqLoc;

    /**
     * Returns the sectionId.
     *
     * @return Returns the sectionId.
     */
    public int getSectionId() {
        return sectionId;
    }

    /**
     * Set the sectionId.
     *
     * @param sectionId The sectionId to set.
     */
    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * Returns the sectionName.
     *
     * @return Returns the sectionName.
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Set the sectionName.
     *
     * @param sectionName The sectionName to set.
     */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * Returns the sectionSeqLoc.
     *
     * @return Returns the sectionSeqLoc.
     */
    public int getSectionSeqLoc() {
        return sectionSeqLoc;
    }

    /**
     * Set the sectionSeqLoc.
     *
     * @param sectionSeqLoc The sectionSeqLoc to set.
     */
    public void setSectionSeqLoc(int sectionSeqLoc) {
        this.sectionSeqLoc = sectionSeqLoc;
    }

    /**
     * Returns the sectionWeight.
     *
     * @return Returns the sectionWeight.
     */
    public int getSectionWeight() {
        return sectionWeight;
    }

    /**
     * Set the sectionWeight.
     *
     * @param sectionWeight The sectionWeight to set.
     */
    public void setSectionWeight(int sectionWeight) {
        this.sectionWeight = sectionWeight;
    }

    /**
     * Returns the questions.
     *
     * @return Returns the questions.
     */
    public Collection getQuestions() {
        return questions;
    }

    /**
     * Set the questions.
     *
     * @param questions The questions to set.
     */
    public void setQuestions(Collection questions) {
        this.questions = questions;
    }
}
