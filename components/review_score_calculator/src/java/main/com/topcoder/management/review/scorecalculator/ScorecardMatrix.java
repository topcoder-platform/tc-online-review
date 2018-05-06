/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import java.util.HashMap;
import java.util.Map;

import com.topcoder.management.scorecard.data.Question;
import com.topcoder.util.weightedcalculator.LineItem;
import com.topcoder.util.weightedcalculator.MathMatrix;

/**
 * <p>
 * A bridge between a Scorecard and its MathMatrix representation used for score calculations.
 * </p>
 *
 * <p>
 * This class keeps a reference to the weighted calculator used, and also allows the ability to quickly locate a
 * particular question and its associated line item by question identifier.
 * </p>
 *
 * <p>
 * Instances of this class may be optionally cached by the CalculationManager class.
 * </p>
 *
 * <p>
 * <b>Thread Safety</b>: This class is not thread safe. You must lock on instances of this class to ensure thread
 * safety (as is done by the CalculationManager class).
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class ScorecardMatrix {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * <p>
     * The weighted calculator that should be used to calculate scores.
     * </p>
     *
     * <p>
     * <b>Invariant</b>: Cannot be null.
     * </p>
     */
    private final MathMatrix mathMatrix;

    /**
     * <p>
     * A mapping between a question identifier and its associated LineItem and Question instances (which are
     * wrapped inside an Entry instance).
     * </p>
     *
     * <p>
     * This was used instead of two maps because it provides more atomicity to modify one map than it does to
     * modify multiple maps.
     * </p>
     *
     * <p>
     * <b>Invariant</b>: Cannot be null;
     * </p>
     */
    private final Map entryMap = new HashMap();

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new ScorecardMatrix using the specified weighted calculator matrix.
     *
     * @param   mathMatrix
     *          The weighted calculator to initialize with.
     *
     * @throws  IllegalArgumentException
     *          The mathMatrix is a null reference.
     */
    public ScorecardMatrix(MathMatrix mathMatrix) {
        Util.checkNotNull(mathMatrix, "mathMatrix");
        this.mathMatrix = mathMatrix;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Accessors

    /**
     * Gets the weighted calculator instance used by this ScorecardMatrix.
     *
     * @return  The weighted calculator instance used by this ScorecardMatrix.
     */
    public MathMatrix getMathMatrix() {
        return mathMatrix;
    }

    /**
     * Gets the number of questions associated with this scorecard matrix.
     *
     * @return  The number of questions associated with this scorecard matrix.
     */
    public int getNumberOfQuestions() {
        return entryMap.size();
    }

    /**
     * Gets the LineItem instance associated with the given question identifier.
     *
     * @param   questionId
     *          The question identifier to lookup the LineItem instance for.
     *
     * @return  The LineItem instance associated with the given question identifier, or null if no LineItem has
     *          been associated yet.
     */
    public LineItem getLineItem(long questionId) {
        Entry entry = getEntry(questionId);
        return (entry == null) ? null : entry.getItem();
    }

    /**
     * Gets the Question instance associated with the given question identifier.
     *
     * @param   questionId
     *          The question identifier to lookup the Question Item instance for.
     *
     * @return  The Question instance associated with the given question identifier, or null if no Question has
     *          been associated yet.
     */
    public Question getQuestion(long questionId) {
        Entry entry = getEntry(questionId);
        return (entry == null) ? null : entry.getQuestion();
    }

    /**
     * Gets the Entry instance associated with the given question identifier.
     *
     * @param   questionId
     *          The question identifier to lookup the Entry instance for.
     *
     * @return  The Entry instance associated with the given question identifier, or null if no Entry has been
     *          associated yet.
     */
    private Entry getEntry(long questionId) {
        return (Entry) entryMap.get(new Long(questionId));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Mutators

    /**
     * <p>
     * Adds a mapping between the given question identifier and the given item and question instances into this
     * ScorecardMatrix.
     * </p>
     *
     * <p>
     * If the question identifier is already mapped to something, it will be overwritten with the specified values.
     * </p>
     *
     * @param   questionId
     *          The question identifier to associate.
     * @param   item
     *          The LineItem instance to be associated with the question identifier.
     * @param   question
     *          The Question instance to be associated with the question identifier.
     *
     * @throws  IllegalArgumentException
     *          The questionId is not a positive long, item is a null reference, or question is a null reference.
     */
    public void addEntry(long questionId, LineItem item, Question question) {
        Util.checkNotNonPositive(questionId, "questionId");
        Util.checkNotNull(item, "item");
        Util.checkNotNull(question, "question");

        entryMap.put(new Long(questionId), new Entry(item, question));
    }

    /**
     * Removes the entry associated with the given question identifier from this ScorecardMatrix.
     *
     * @param   questionId
     *          The question identifier of the entry to remove.
     *
     * @return  True if the entry was found and removed; false otherwise.
     */
    public boolean removeEntry(long questionId) {
        // Since we don't allow null values, it is fine to check like this.
        return (entryMap.remove(new Long(questionId)) != null);
    }

    /**
     * Clears all entries associated with this ScorecardMatrix.
     */
    public void clearEntries() {
        entryMap.clear();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private Classes

    /**
     * A simple wrapper class that contains a LineItem instance and a Question instance.
     */
    private class Entry {

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Member Fields

        /**
         * The entry's line item.
         */
        private final LineItem item;

        /**
         * The entry's question.
         */
        private final Question question;

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Constructors

        /**
         * Creates a new Entry with the given arguments.
         *
         * @param   item
         *          The line item to initialize with (is not null).
         * @param   question
         *          The question to initialize with (is not null)
         */
        public Entry(LineItem item, Question question) {
            this.item = item;
            this.question = question;
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Member Accessors

        /**
         * Gets the entry's line item.
         *
         * @return  The entry's line item.
         */
        public LineItem getItem() {
            return item;
        }

        /**
         * Gets the entry's question.
         *
         * @return  The entry's question.
         */
        public Question getQuestion() {
            return question;
        }
    }
}
