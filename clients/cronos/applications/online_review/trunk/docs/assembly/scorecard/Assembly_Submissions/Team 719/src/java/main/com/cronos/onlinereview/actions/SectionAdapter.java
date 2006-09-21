/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Section;

/**
 * <p>
 * <code>SectionAdapter</code> extends
 * <code>com.topcoder.management.scorecard.data.Section</code>, holds both
 * "section" model data and facility data which bridge the gap between
 * <code>Scorecard Data Structure</code> and the struts front-end mechanism.
 * </p>
 * 
 * @version 1.0
 * @author albertwang, flying2hk
 */
public class SectionAdapter extends Section {
    /**
     * "Good" question count, used to eliminate dirty questions.
     */
    private int count;

    /**
     * <p>
     * Return the good question count.
     * </p>
     * 
     * @return the good question count
     */
    public int getCount() {
        return count;
    }

    /**
     * <p>
     * Set the good question count.
     * </p>
     * 
     * @param count
     *            the good question count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * <p>
     * Return question with given index.
     * </p>
     * 
     * @param index
     *            index
     * @return the question
     */
    public Question getAllQuestions(int index) {
        if (index < this.getNumberOfQuestions()) {
            return this.getQuestion(index);
        } else {
            this.addQuestion(new QuestionAdapter());
            return getAllQuestions(index);
        }

    }

    /**
     * <p>
     * Create an empty section adapter.
     * </p>
     */
    public SectionAdapter() {
        super();
        this.count = 0;
    }

    /**
     * <p>
     * Create a SectionAdapter from given section.
     * </p>
     * 
     * @param section
     *            the section
     */
    public SectionAdapter(Section section) {
        if (section.getId() > 0) {
            this.setId(section.getId());
        }
        this.setName(section.getName());
        this.setWeight(section.getWeight());
        this.setCount(section.getNumberOfQuestions());
        Question[] qs = section.getAllQuestions();
        for (int i = 0; i < qs.length; i++) {
            this.addQuestion(new QuestionAdapter(qs[i]));
        }

    }
}
