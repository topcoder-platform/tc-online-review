/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator.builders;

import com.topcoder.management.review.scorecalculator.ScorecardMatrix;
import com.topcoder.management.review.scorecalculator.ScorecardMatrixBuilder;
import com.topcoder.management.review.scorecalculator.ScorecardStructureException;
import com.topcoder.management.review.scorecalculator.Util;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.management.scorecard.data.WeightedScorecardStructure;
import com.topcoder.util.weightedcalculator.LineItem;
import com.topcoder.util.weightedcalculator.MathMatrix;

/**
 * <p>
 * Represents the default scorecard matrix builder, which creates a weighted calculator with one section for
 * each scorecard group, one subsection for each scorecard section in the group, and one LineItem for each
 * question in the scorecard section.
 * </p>
 *
 * <p>
 * <b>Thread Safety</b>: This class is thread safe.
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0.4
 */
public class DefaultScorecardMatrixBuilder implements ScorecardMatrixBuilder {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The maximum possible allowable score in the matrix.
     */
    private static final double MAX_SCORE = 100.0;

    /**
     * The maximum possible allowable score for a line item.
     */
    private static final double MAX_ITEM_SCORE = 1.0;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new DefaultScorecardMatrixBuilder.
     */
    public DefaultScorecardMatrixBuilder() {
        // Do nothing.
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ScorecardMatrixBuilder Interface Methods

    /**
     * Creates a ScorecardMatrix from the given Scorecard by creating a matrix Section for each scorecard group,
     * a sub-section for each scorecard section, and a line item for each scorecard question.
     *
     * @param   scorecard
     *          The Scorecard instance to build a ScorecardMatrix for.
     *
     * @return  A ScorecardMatrix that represents the given Scorecard.
     *
     * @throws  IllegalArgumentException
     *          The scorecard is a null reference.
     * @throws  ScorecardStructureException
     *          A scorecard matrix cannot be built from the given scorecard because its name is null or empty,
     *          it contains no groups, one of the groups contains no sections, one of the sections contains no
     *          questions, any of the scorecard structures contain an item with 0 weight, or has a null or empty
     *          name (description in case of a question).
     */
    public ScorecardMatrix buildScorecardMatrix(Scorecard scorecard) throws ScorecardStructureException {
        // Sanity check argument.
        Util.checkNotNull(scorecard, "scorecard");

        // Create an empty MathMatrix and wrap it in a ScorecardMatrix.
        final MathMatrix matrix;

        try {
            matrix = new MathMatrix(scorecard.getName(), MAX_SCORE);
        } catch (IllegalArgumentException ex) {
            throw new ScorecardStructureException(
                    "The scorecard's name cannot be null or an empty string.", ex, scorecard);
        }

        final ScorecardMatrix result = new ScorecardMatrix(matrix);

        // For each group, add a Section as an item to the matrix.
        final Group[] groups = scorecard.getAllGroups();
        final double totalGroupsWeight = sumWeights(groups, scorecard, -1, -1);

        for (int groupIndex = 0; groupIndex < groups.length; ++groupIndex) {
            final Group group = groups[groupIndex];
            if (group.getWeight() == 0.0) {
                continue;
            }
            final com.topcoder.util.weightedcalculator.Section groupSection =
                createSection(group, totalGroupsWeight, scorecard, groupIndex, -1);

            matrix.addItem(groupSection);

            // For each section, add a Section as an item to the groupSection.
            final Section[] sections = group.getAllSections();
            final double totalSectionsWeight = sumWeights(sections, scorecard, groupIndex, -1);

            for (int sectionIndex = 0; sectionIndex < sections.length; ++sectionIndex) {
                final Section section = sections[sectionIndex];
                if (section.getWeight() == 0.0) {
                    continue;
                }
                final com.topcoder.util.weightedcalculator.Section sectionSection =
                    createSection(section, totalSectionsWeight, scorecard, groupIndex, sectionIndex);

                groupSection.addItem(sectionSection);

                // For each question, add a LineItem as an item to the sectionSection.
                final Question[] questions = section.getAllQuestions();
                final double totalQuestionsWeight = sumWeights(questions, scorecard, groupIndex, sectionIndex);

                for (int questionIndex = 0; questionIndex < questions.length; ++questionIndex) {
                    final Question question = questions[questionIndex];
                    if (question.getWeight() == 0.0) {
                        continue;
                    }
                    final LineItem questionLineItem = createLineItem(question, totalQuestionsWeight,
                            result, scorecard, groupIndex, sectionIndex, questionIndex);

                    sectionSection.addItem(questionLineItem);
                    result.addEntry(question.getId(), questionLineItem, question);
                }
            }
        }

        // Return the built scorecard matrix.
        return result;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Helper Methods

    /**
     * Sums the weights in the given list of WeightedScorecardStructure instances (the other arguments are used
     * for generating exception messages).
     *
     * @param   list
     *          The weighted list whose sum is being requested for.
     * @param   scorecard
     *          The scorecard that is being built.
     * @param   groupIndex
     *          The index of the current group being processed, or -1 if processing at the scorecard level.
     * @param   sectionIndex
     *          The index of the current section being processed, or -1 if processing at the scorecard or
     *          group level.
     *
     * @return  The sum of the weights in the given list.
     *
     * @throws  ScorecardStructureException
     *          The list is empty or contains an element with an uninitialized weight (i.e. a zero weighting).
     */
    private static double sumWeights(WeightedScorecardStructure[] list, Scorecard scorecard, int groupIndex,
        int sectionIndex) throws ScorecardStructureException {

        // 0-length arrays should be disallowed.
        if (list.length == 0) {
            final String message;

            if (sectionIndex != -1) {
                message = "The scorecard contains a group (index " + groupIndex
                    + ") which contains a section (index " + sectionIndex + ") with no questions.";
            } else if (groupIndex != -1) {
                message = "The scorecard contains a group (index " + groupIndex + ") with no sections.";
            } else {
                message = "The scorecard contains no groups.";
            }

            throw new ScorecardStructureException(message, scorecard);
        }

        // Total the weights of each element.
        double sum = 0.0;

        for (int i = 0; i < list.length; ++i) {
            /*
            // The weighted calculator Section and LineItem classes disallow 0 weights anyway, so we
            // should mark them invalid.
            if (list[i].getWeight() == 0.0) {
                final String message;

                if (sectionIndex != -1) {
                    message = "The scorecard contains a group (index " + groupIndex
                        + ") which contains a section (index " + sectionIndex
                        + ") with a question (index " + i + ") that has no weight.";
                } else if (groupIndex != -1) {
                    message = "The scorecard contains a group (index " + groupIndex
                        + ") with a section (index " + i + ") that has no weight.";
                } else {
                    message = "The scorecard contains a group (index " + i + ") that has no weight.";
                }

                throw new ScorecardStructureException(message, scorecard);
            }*/

            // Accumulate the item's weight.
            sum += list[i].getWeight();
        }

        // Return the sum, which is definitely a positive number, so we know we won't encounter a division
        // by zero error.
        return sum;
    }

    /**
     * Creates a section for the given structure and total weight (the other arguments are used for generating
     * exception messages).
     *
     * @param   structure
     *          The WeightedScorecardStructure instance to create a Weighted Calculator Section for.
     * @param   totalWeight
     *          The sum of the weights in the current weighted list being processed.
     * @param   scorecard
     *          The scorecard currently being processed.
     * @param   groupIndex
     *          The index of the current group being processed, or -1 if processing at the scorecard level.
     * @param   sectionIndex
     *          The index of the current section being processed, or -1 if processing at the scorecard or
     *          group level.
     *
     * @return  A Weighted Calculator Section object encapsulating the structure's name and normalized weight.
     *
     * @throws  ScorecardStructureException
     *          The structure's name is null or an empty string.
     */
    private static com.topcoder.util.weightedcalculator.Section createSection(WeightedScorecardStructure structure,
        double totalWeight, Scorecard scorecard, int groupIndex, int sectionIndex) throws ScorecardStructureException {

        try {
            return new com.topcoder.util.weightedcalculator.Section(
                    structure.getName(), structure.getWeight() / totalWeight);
        } catch (IllegalArgumentException ex) {
            final String message;

            if (sectionIndex != -1) {
                message = "The scorecard contains a group (index " + groupIndex
                    + ") with a section (index " + sectionIndex + ") that has a null or empty name.";
            } else {
                message = "The scorecard contains a group (index " + groupIndex + ") that has a null or empty name.";
            }

            throw new ScorecardStructureException(message, ex, scorecard);
        }
    }

    /**
     * Creates a line item for the given question and total weight (the other arguments are used for generating
     * exception messages).
     *
     * @param   question
     *          The Question instance to create a Weighted Calculator LineItem for.
     * @param   totalWeight
     *          The sum of the weights in the current questions list being processed.
     * @param   scMatrix
     *          The current ScorecardMatrix result.
     * @param   scorecard
     *          The scorecard currently being processed.
     * @param   groupIndex
     *          The index of the current group being processed.
     * @param   sectionIndex
     *          The index of the current section being processed.
     * @param   questionIndex
     *          The index of the current question being processed.
     *
     * @return  A Weighted Calculator LineItem object encapsulating the question's description and normalized weight.
     *
     * @throws  ScorecardStructureException
     *          The question's description is null or an empty string.
     */
    private static LineItem createLineItem(Question question, double totalWeight, ScorecardMatrix scMatrix,
        Scorecard scorecard, int groupIndex, int sectionIndex, int questionIndex)
        throws ScorecardStructureException {

        try {
            if (scMatrix.getQuestion(question.getId()) != null) {
                throw new ScorecardStructureException(
                        "The scorecard contains a group (index " + groupIndex
                            + ") which contains a section (index " + sectionIndex
                            + ") with a question (index " + questionIndex
                            + ") that has an id that was already seen.", scorecard);
            }
        } catch (IllegalStateException ex) {
            throw new ScorecardStructureException(
                    "The scorecard contains a group (index " + groupIndex
                        + ") which contains a section (index " + sectionIndex
                        + ") with a question (index " + questionIndex
                        + ") that has an uninitialized id.", ex, scorecard);
        }

        try {
            return new LineItem(question.getDescription(), question.getWeight() / totalWeight, MAX_ITEM_SCORE);
        } catch (IllegalArgumentException ex) {
            throw new ScorecardStructureException(
                    "The scorecard contains a group (index " + groupIndex
                        + ") which contains a section (index " + sectionIndex
                        + ") with a question (index " + questionIndex
                        + ") that has a null or empty description.", ex, scorecard);
        }
    }
}
